package pb.repo.pcm.xmlrpc;

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
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.pcm.model.PcmReqModel;
import pb.repo.pcm.service.PcmReqService;
import pb.repo.pcm.service.PcmReqWorkflowService;
import pb.repo.pcm.util.PcmUtil;

@Service
public class PcmReqInvocationHandler
{
	
	private static Logger log = Logger.getLogger(PcmReqInvocationHandler.class);

	@Autowired
	private PcmReqService pcmReqService;
	
	@Autowired
	private PcmReqWorkflowService mainWorkflowService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	DataSource dataSource;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> action(String prNo, String type, String comment, final String by)
//    public Map<String, Object> action(String prNo, String type, final String by)
    {
//    	String comment = null;
    	log.info("pr.action("+prNo+","+type+","+comment+","+by+")");
    	SqlSession session = PcmUtil.openSession(dataSource);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
    		String statusDesc = null;
    		
	    	PcmReqModel pcmReqModel = null;

			if (type.equals(PcmReqConstant.ST_CLOSED_BY_PCM) || type.equals(PcmReqConstant.ST_CLOSED_BY_PCM_DIY)) {
				statusDesc = "Approve";
				
		    	pcmReqModel = pcmReqService.get(prNo, null);
		    	CommonUtil.checkNull(pcmReqModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+prNo);
		    	
				final NodeRef folderNodeRef = new NodeRef(pcmReqModel.getFolderRef());
				
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
			if (type.equals(PcmReqConstant.ST_CANCEL_BY_PCM)) {
				statusDesc = "Cancel";
			} else {
    			result.put("success",false);
    			result.put("message","Invalid Type : "+type);
    			return result;
			}
    		
			if (pcmReqModel==null) {
				pcmReqModel = pcmReqService.get(prNo, null);
		    	CommonUtil.checkNull(pcmReqModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+prNo);
			}
			pcmReqModel.setUpdatedBy(by);
	    	pcmReqModel.setStatus(type);
	    	pcmReqService.update(session, pcmReqModel);
    		
	    	mainWorkflowService.setModuleService(pcmReqService);
			mainWorkflowService.saveWorkflowHistory(session, null, by, MainWorkflowConstant.TN_PROCUREMENT , comment, statusDesc, null,  prNo, null, type);
			
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
    	log.info("pr.history()");
    	log.info("  prNo:"+params.get("prNo")
    			+", by:"+params.get("by")
    			+", task:"+params.get("task")
    			+", task_th:"+params.get("task_th")
    			+", status:"+params.get("status")
    			+", status_th:"+params.get("status_th")
    			);

    	SqlSession session = PcmUtil.openSession(dataSource);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
	    	String prNo = (String)params.get("prNo");
	    	final String by = (String)params.get("by");
	    	String task = (String)params.get("task");
	    	String taskTh = (String)params.get("task_th");
	    	String status = (String)params.get("status");
	    	String statusTh = (String)params.get("status_th");
	    	
	    	PcmReqModel pcmReqModel = pcmReqService.get(prNo, null);
			if (pcmReqModel==null) {
				log.info("  Invalid PR : "+prNo);
    			result.put("success",true);
    			result.put("message",CommonConstant.EXT_ERR_MSG_MISMATCHED+prNo);
    			return result;
			}
    		
			mainWorkflowService.saveWorkflowHistory(session, prNo, by, task, taskTh, status, statusTh, (String)params.get("comment"));
			
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
