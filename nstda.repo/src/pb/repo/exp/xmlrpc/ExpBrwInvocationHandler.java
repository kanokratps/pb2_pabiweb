package pb.repo.exp.xmlrpc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PermissionService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;
import pb.repo.exp.util.ExpUtil;

@Service
public class ExpBrwInvocationHandler
{
	
	private static Logger log = Logger.getLogger(ExpBrwInvocationHandler.class);

	@Autowired
	private ExpBrwService expBrwService;
	
	@Autowired
	private ExpBrwWorkflowService mainWorkflowService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	DataSource dataSource;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> action(Map<String, Object> params)
    {
    	log.info("av.action()");
    	log.info("  action:"+params.get("action")
    			+", avNo:"+params.get("avNo")
    			+", by:"+params.get("by")
    			);
    	
    	final SqlSession session = ExpUtil.openSession(dataSource);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
    		String statusDesc = null;
    		
	    	ExpBrwModel expBrwModel = null;
	    	
	    	String action = (String)params.get("action");
	    	String type = null;
	    	String avNo = (String)params.get("avNo");
	    	final String by = (String)params.get("by");

			if (action.equals("1")) {
				type = ExpBrwConstant.ST_CLOSED_BY_FIN;
				statusDesc = "Approve";
				
		    	expBrwModel = expBrwService.get(avNo, null);
		    	CommonUtil.checkNull(expBrwModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+avNo);
		    	
				final NodeRef folderNodeRef = new NodeRef(expBrwModel.getFolderRef());
				
		        AuthenticationUtil.runAs(new RunAsWork<String>()
	    	    {
	    			public String doWork() throws Exception
	    			{
		        		if(!permissionService.getPermissions(folderNodeRef).contains(by)) {
		        			permissionService.setPermission(folderNodeRef, by, "SiteCollaborator", true);
		        		}

	    	        	return null;
	    			}
	    	    }, AuthenticationUtil.getAdminUserName());  
				
			} else 
			if (action.equals("2")) {
				statusDesc = "Cancel";
				type = ExpBrwConstant.ST_CANCEL_BY_FIN;
//			} else
//			if (action.equals("3")) {
//				statusDesc = "Paid";
//				type = ExpBrwConstant.ST_PAID_BY_FIN;
			} else {
    			result.put("success",false);
    			result.put("message","Invalid Action : "+action);
    			return result;
			}
    		
			if (expBrwModel==null) {
				expBrwModel = expBrwService.get(avNo, null);
		    	CommonUtil.checkNull(expBrwModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+avNo);
			}
			expBrwModel.setUpdatedBy(by);
	    	expBrwModel.setStatus(type);
	    	expBrwService.update(session, expBrwModel);
    		
	    	mainWorkflowService.setModuleService(expBrwService);
			mainWorkflowService.saveWorkflowHistory(session, null, by, MainWorkflowConstant.TN_FINANCE ,  (String)params.get("comment"), statusDesc, null,  avNo, null, type);
			
			session.commit();
			result.put("success",true);
			result.put("message","Success");
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
    
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> history(Map<String, Object> params)
    {
    	log.info("av.history()");
    	log.info("  avNo:"+params.get("avNo")
    			+", by:"+params.get("by")
    			+", task:"+params.get("task")
    			+", task_th:"+params.get("task_th")
    			+", status:"+params.get("status")
    			+", status_th:"+params.get("status_th")
    			);
    	
    	final SqlSession session = ExpUtil.openSession(dataSource);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
	    	String avNo = (String)params.get("avNo");
	    	final String by = (String)params.get("by");
	    	String task = (String)params.get("task");
	    	String taskTh = (String)params.get("task_th");
	    	String status = (String)params.get("status");
	    	String statusTh = (String)params.get("status_th");
	    	
	    	ExpBrwModel expBrwModel = expBrwService.get(avNo, null);
			if (expBrwModel==null) {
				log.info("  Invalid AV : "+avNo);
    			result.put("success",true);
    			result.put("message",CommonConstant.EXT_ERR_MSG_MISMATCHED+avNo);
    			return result;
			}
    		
			mainWorkflowService.saveWorkflowHistory(session, avNo, by, task, taskTh, status, statusTh, (String)params.get("comment"));
			
			session.commit();
			result.put("success",true);
			result.put("message","Success");
    	} catch (Exception ex) {
    		session.rollback();
    		log.error(ex);
    		ex.printStackTrace();
			result.put("success",false);
			result.put("message","Error : "+ex.toString());
    	} finally {
    		session.close();
    	}
    	
        return result;
    }

}
