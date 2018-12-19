package pb.repo.pcm.xmlrpc;

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
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.exception.NotFoundApprovalMatrixException;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.pcm.constant.PcmOrdConstant;
import pb.repo.pcm.model.PcmOrdModel;
import pb.repo.pcm.service.PcmOrdService;
import pb.repo.pcm.service.PcmOrdWorkflowService;
import pb.repo.pcm.util.PcmUtil;

@Service
public class PcmOrdInvocationHandler
{
	
	private static Logger log = Logger.getLogger(PcmOrdInvocationHandler.class);

	@Autowired
	private PcmOrdService pcmOrdService;
	
	@Autowired
	private PcmOrdWorkflowService mainWorkflowService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	DataSource dataSource;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> action(final Map<String, Object> params)
    {
    	log.info("pd.action()");
    	
    	final SqlSession session = PcmUtil.openSession(dataSource);
    	Map<String, Object> result = new HashMap<String, Object>();
    	String action = null;
    	try {
    		/*
    		 * Log Parameters
    		 */
    		log.info("  action:"+params.get("action")
    				+", pdNo:"+params.get("pdNo")
        			+", docType:"+params.get("docType")
    				+", reqBy:"+params.get("reqBy")
    		);

    		/*
    		 * Create Pcm Ord
    		 */
    		Boolean found = false;
    		final PcmOrdModel model;
    		String pdNo = (String)params.get("pdNo");
    		PcmOrdModel tmpModel = pcmOrdService.get(pdNo, null);

    		action = (String)params.get("action");
    		if (action.equals("3") && tmpModel==null) {
		    	CommonUtil.checkNull(tmpModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+pdNo);
    		}
    		
    		String reqBy = (String)params.get("reqBy");
    		if (tmpModel==null) {
    			model = new PcmOrdModel();
        		model.setId(pdNo);
        		model.setCreatedBy(reqBy);
    		} else {
    			found = true;
    			model = tmpModel;
    		}
    		model.setUpdatedBy(reqBy);
    		
    		final Map<String, Object> srcDocMap;
    		final List<Map<String, Object>> srcAttList;
    		
    		if (action.equals("1") || action.equals("2")) {
        		log.info("  sectionId:"+params.get("sectionId")
        				+", prNo:"+params.get("prNo")
        				+", objective:"+params.get("objective")
        				+", total:"+params.get("total")
        				+", appBy:"+params.get("appBy")
        		);
//        		log.info(params.get("doc"));
//        		log.info(params.get("attachments"));

	    		model.setSectionId(Integer.parseInt((String)params.get("sectionId")));
	    		model.setPrId((String)params.get("prNo"));
	    		model.setDocType((String)params.get("docType"));
	    		model.setObjective((String)params.get("objective"));
	    		model.setTotal(Double.parseDouble((String)params.get("total")));
	    		model.setAppBy((String)params.get("appBy"));
	    		
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
    				throw new Exception("Existing PD No."+model.getId());
    			}
	    		model.setStatus(PcmOrdConstant.ST_WAITING);
	    		model.setWaitingLevel(1);
	    		
	    		PcmOrdModel savedModel = AuthenticationUtil.runAs(new RunAsWork<PcmOrdModel>()
	    	    {
	    			public PcmOrdModel doWork() throws Exception
	    			{
	    				PcmOrdModel savedModel = pcmOrdService.save(model, srcDocMap, srcAttList);
	    		   		
	    	    		/*
	    	    		 * Start Workflow
	    	    		 */
	    	    		mainWorkflowService.setModuleService(pcmOrdService);
	    				Map<String, String> bossMap = mainWorkflowService.getBossMap((String)params.get("docType"), model);
	    				mainWorkflowService.startWorkflow(session, savedModel, bossMap);

	    	        	return savedModel;
	    			}
	    	    }, AuthenticationUtil.getAdminUserName());  
//    	    	}, reqBy);  
	    		
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
//		    		attMap.put("desc", srcAttMap.get("desc"));
		    		attList.add(attMap);
	    		}
	    		
				result.put("success",true);
				result.put("message","Success");
				result.put("doc", docMap);
				result.put("attachments", attList);
    		}
    		else 
    		if (action.equals("2")) { // Resubmit
	    		model.setStatus(PcmOrdConstant.ST_WAITING);
	    		model.setWaitingLevel(1);
	    		
	    		PcmOrdModel savedModel = AuthenticationUtil.runAs(new RunAsWork<PcmOrdModel>()
	    	    {
	    			public PcmOrdModel doWork() throws Exception
	    			{
	    				return pcmOrdService.save(model, srcDocMap, srcAttList);
	    			}
	    	    }, AuthenticationUtil.getAdminUserName()); 	    		
	    		
    			MainWorkflowModel wfModel = new MainWorkflowModel();
    			wfModel.setMasterId(savedModel.getId());
    			wfModel = mainWorkflowService.getLastWorkflow(session, wfModel);
    			
    			String comment = (String)params.get("comment");
    			
    			pcmOrdService.continueRequesterTask(wfModel.getExecutionId(), MainWorkflowConstant.TA_RESUBMIT, savedModel, comment, (String)params.get("docType"));
    			
	    		Map<String, Object> docMap = new HashMap<String, Object>();
	    		docMap.put("name", srcDocMap.get("name"));
	    		docMap.put("url", NodeUtil.trimNodeRef(savedModel.getDocRef()));
	    		
	    		List<Map<String, Object>> attList = new ArrayList<Map<String, Object>>();
	    		int i = 0;
	    		for(Map<String, Object> srcAttMap:srcAttList) {
		    		Map<String, Object> attMap = new HashMap<String, Object>();
		    		attMap.put("name", srcAttMap.get("name"));
		    		attMap.put("url", NodeUtil.trimNodeRef(savedModel.getListAttachDoc().get(i++)));
//		    		attMap.put("desc", srcAttMap.get("desc"));
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

    			pcmOrdService.continueRequesterTask(wfModel.getExecutionId(), MainWorkflowConstant.TA_CANCEL, model, comment,(String)params.get("docType"));
    			
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
			result.put("message","Error : ไม่พบสายงานอนุมัติใบ พด. กรุณาติดต่อ ผู้ดูแลระบบ");
			if(action.equals("1")) {
				try {
					pcmOrdService.delete((String)params.get("pdNo"));
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
    
//    public Map<String, Object> updateStatus(String pdNo, String type, String comment, String by) {
//    	
//    	log.info("updateStatus("+pdNo+","+type+","+comment+","+by+")");
//    	
//    	Map<String, Object> result = new HashMap<String, Object>();
//    	
//       	try {
//    		String statusDesc = null;
//    		
//			if (type.equals(PcmOrdConstant.ST_WAITING)) {
//				statusDesc = MainWorkflowConstant.TA_RESUBMIT;
//			} 
//			else
//			if (type.equals(PcmOrdConstant.ST_CANCEL_BY_PCM)) {
//				statusDesc = MainWorkflowConstant.TA_CANCEL;
//			} else {
//    			result.put("success",false);
//    			result.put("message","Invalid Type : "+type);
//    			return result;
//			}
//    		
//	    	PcmOrdModel pcmOrdModel = pcmOrdService.get(pdNo);
//	    	pcmOrdModel.setStatus(type);
//	    	pcmOrdService.update(pcmOrdModel);
//    		
//	    	mainWorkflowService.setModuleService(pcmOrdService);
//			mainWorkflowService.saveWorkflowHistory(null, by, "ผู้จัดซื้อ" , comment, statusDesc, null,  pdNo, null);
//			
//			result.put("success",true);
//			result.put("message","Success");
//		} catch (Exception ex) {
//			log.error("",ex);
//			result.put("success",false);
//			result.put("message","Error : "+ex.toString());
//		}
//		
//	    return result;
//    }

}
