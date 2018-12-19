package pb.repo.hr.xmlrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.common.util.FileUtil;
import pb.common.util.NodeUtil;
import pb.repo.admin.constant.MainWkfConfigDocTypeConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.exception.NotFoundApprovalMatrixException;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.hr.constant.HrSalConstant;
import pb.repo.hr.model.HrSalModel;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;
import pb.repo.hr.util.HrUtil;

@Service
public class HrSalInvocationHandler
{
	
	private static Logger log = Logger.getLogger(HrSalInvocationHandler.class);

	@Autowired
	private HrSalService hrSalService;
	
	@Autowired
	private HrSalWorkflowService mainWorkflowService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	DataSource dataSource;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> action(Map<String, Object> params)
    {
    	log.info("hr.action()");
    	
    	final SqlSession session = HrUtil.openSession(dataSource);
    	Map<String, Object> result = new HashMap<String, Object>();
    	String action = null;
		String salNo = (String)params.get("salaryNo");

		/*
		 * Log Parameters
		 */
		log.info("  action:"+params.get("action")
				+", salNo:"+salNo
				+", reqBy:"+params.get("reqBy")
				);
		
    	try {
    		/*
    		 * Create Hr Salary
    		 */
    		Boolean found = false;
    		final HrSalModel model;
    		HrSalModel tmpModel = hrSalService.get(salNo, null);
    		
    		action = (String)params.get("action");
    		if (action.equals("3") && tmpModel==null) {
		    	CommonUtil.checkNull(tmpModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+salNo);
    		}
    		
    		String reqBy = (String)params.get("reqBy");
    		if (tmpModel==null) {
    			model = new HrSalModel();
        		model.setId(salNo);
        		model.setCreatedBy(reqBy);
    		} else {
    			model = tmpModel;
    			found = true;
    		}
    		model.setUpdatedBy(reqBy);
    		
    		final Map<String, Object> srcDocMap;
    		final List<Map<String, Object>> srcAttList;
    		
    		action = (String)params.get("action");
    		if (action.equals("1") || action.equals("2")) {
        		log.info("  sectionId:"+params.get("sectionId")
        				+", objective:"+params.get("objective")
        				+", total:"+params.get("total")
        				);
//        		log.info(params.get("docType"));
//        		log.info(params.get("appBy"));
//        		log.info(params.get("doc"));
//        		log.info(params.get("attachments"));

	    		model.setSectionId(Integer.parseInt((String)params.get("sectionId")));
//	    		model.setPrId((String)params.get("prNo"));
	    		model.setDocType(MainWkfConfigDocTypeConstant.DT_EX);
	    		model.setObjective((String)params.get("objective"));
	    		model.setTotal(Double.parseDouble((String)params.get("total")));
//	    		model.setAppBy((String)params.get("appBy"));
	    		
	    		srcDocMap = (Map<String, Object>)params.get("doc");
	    		srcAttList = (List<Map<String,Object>>) params.get("attachments");
	    		
	    		String fileName = (String)srcDocMap.get("name");
	    		FileUtil.validateFileName(fileName);
	    		for(Map<String, Object> srcAttMap:srcAttList) {
		    		fileName = (String)srcAttMap.get("name");
		    		FileUtil.validateFileName(fileName);
	    		}	    		
    		} else {
	    		srcDocMap = null;
	    		srcAttList = null;
    		}
    		
    		if (action.equals("1")) { // Start New Workflow
    			if (found) {
    				throw new Exception("Existing Salary No."+model.getId());
    			}
	    		model.setStatus(HrSalConstant.ST_WAITING);
	    		model.setWaitingLevel(1);
	    		
	    		HrSalModel savedModel = AuthenticationUtil.runAs(new RunAsWork<HrSalModel>()
	    	    {
	    			public HrSalModel doWork() throws Exception
	    			{
	    				HrSalModel savedModel = hrSalService.save(model, srcDocMap, srcAttList);
	    		   		
	    	    		/*
	    	    		 * Start Workflow
	    	    		 */
	    	    		mainWorkflowService.setModuleService(hrSalService);
	    				Map<String, String> bossMap = mainWorkflowService.getBossMap(savedModel.getDocType(), model);
	    				mainWorkflowService.startWorkflow(session, savedModel, bossMap);

	    	        	return savedModel;
	    			}
	    	    }, AuthenticationUtil.getAdminUserName());  
	    		
	    		
	    		/*
	    		 * Result
	    		 */
	    		Map<String, Object> docMap = new HashMap<String, Object>();
	    		docMap.put("name", srcDocMap.get("name"));
	    		docMap.put("url", NodeUtil.trimNodeRef(savedModel.getDocRef()));
	    		
	    		List<Map<String, Object>> attList = new ArrayList<Map<String, Object>>();
	    		int i = 0;
	    		for(Map<String, Object> srcAttMap:srcAttList) {
		    		Map<String, Object> attMap = new HashMap<String, Object>();
		    		attMap.put("name", srcAttMap.get("name"));
		    		attMap.put("url", NodeUtil.trimNodeRef(savedModel.getListAttachDoc().get(i++)));
		    		attList.add(attMap);
	    		}
	    		
				result.put("success",true);
				result.put("message","Success");
				result.put("doc", docMap);
				result.put("attachments", attList);
    		}
    		else 
    		if (action.equals("2")) { // Resubmit
	    		model.setStatus(HrSalConstant.ST_WAITING);
	    		model.setWaitingLevel(1);
	    		
	    		HrSalModel savedModel = AuthenticationUtil.runAs(new RunAsWork<HrSalModel>()
	    	    {
	    			public HrSalModel doWork() throws Exception
	    			{
	    				HrSalModel savedModel = hrSalService.save(model, srcDocMap, srcAttList);
	    		   		
	    	        	return savedModel;
	    			}
	    	    }, AuthenticationUtil.getAdminUserName());  
	    		
    			MainWorkflowModel wfModel = new MainWorkflowModel();
    			wfModel.setMasterId(savedModel.getId());
    			wfModel = mainWorkflowService.getLastWorkflow(session, wfModel);
    			
    			String comment = (String)params.get("comment");
    			
    			hrSalService.continueRequesterTask(wfModel.getExecutionId(), MainWorkflowConstant.TA_RESUBMIT, savedModel, comment, savedModel.getDocType());
    			
	    		Map<String, Object> docMap = new HashMap<String, Object>();
	    		docMap.put("name", srcDocMap.get("name"));
	    		docMap.put("url", NodeUtil.trimNodeRef(savedModel.getDocRef()));
	    		
	    		List<Map<String, Object>> attList = new ArrayList<Map<String, Object>>();
	    		int i = 0;
	    		for(Map<String, Object> srcAttMap:srcAttList) {
		    		Map<String, Object> attMap = new HashMap<String, Object>();
		    		attMap.put("name", srcAttMap.get("name"));
		    		attMap.put("url", NodeUtil.trimNodeRef(savedModel.getListAttachDoc().get(i++)));
		    		attList.add(attMap);
	    		}
    			
    			result.put("success",true);
    			result.put("message","Success");
				result.put("doc", docMap);
				result.put("attachments", attList);
    		}
    		else
    		if (action.equals("3")) { // Cancel
    			MainWorkflowModel wfModel = new MainWorkflowModel();
    			wfModel.setMasterId(model.getId());
    			wfModel = mainWorkflowService.getLastWorkflow(session, wfModel);
    			
    			String comment = (String)params.get("comment");

    			hrSalService.continueRequesterTask(wfModel.getExecutionId(), MainWorkflowConstant.TA_CANCEL, model, comment,model.getDocType());
    			
    			result.put("success",true);
    			result.put("message","Success");
    		} 
    		else {
    			result.put("success",false);
    			result.put("message","Error : Invalid Action : "+action);
    		}
    		session.commit();
    	} catch (NotFoundApprovalMatrixException ex) {
        	session.rollback();
    		log.error(ex);
    		ex.printStackTrace();
			result.put("success",false);
			result.put("message","Error : ไม่พบสายงานอนุมัติเงินเดือน กรุณาติดต่อ ผู้ดูแลระบบ");
			if(action.equals("1")) {
				try {
					hrSalService.delete(salNo);
				} catch (Exception ex2) {
					log.error(ex2);
					ex2.printStackTrace();
				}
			}
    	} catch (Exception ex) {
        	session.rollback();
    		log.error(ex);
    		ex.printStackTrace();
    		
    		if (ex.getMessage().startsWith(CommonConstant.EXT_ERR_MSG_MISMATCHED)) {
    			result.put("success",true);
    			result.put("message",ex.getMessage());
    		} else {
    			result.put("success",false);
    			result.put("message","Error : "+ex.toString());
    		}
		} finally {
	    	session.close();
		}
    	
        return result;
    }

}
