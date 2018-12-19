package pb.repo.exp.xmlrpc;

import java.util.HashMap;
import java.util.List;
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
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.ExpUseConstant;
import pb.repo.admin.constant.MainWkfConfigDocTypeConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.service.AdminInterfaceService;
import pb.repo.exp.model.ExpUseModel;
import pb.repo.exp.service.ExpUseService;
import pb.repo.exp.service.ExpUseWorkflowService;
import pb.repo.exp.service.InterfaceService;
import pb.repo.exp.util.ExpUtil;

@Service
public class ExpUseInvocationHandler
{
	
	private static Logger log = Logger.getLogger(ExpUseInvocationHandler.class);

	@Autowired
	private ExpUseService expUseService;
	
	@Autowired
	private ExpUseWorkflowService mainWorkflowService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private AdminInterfaceService adminInterfaceService;
	
	@Autowired
	InterfaceService interfaceService;
	
	@Autowired
	DataSource dataSource;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> action(Map<String, Object> params)
    {
    	log.info("ex.action()");
    	log.info("  action:"+params.get("action")
    			+", exNo:"+params.get("exNo")
    			+", by:"+params.get("by")
    			);
    	
    	SqlSession session = ExpUtil.openSession(dataSource);
    	
    	Long infId = adminInterfaceService.add((String)params.get("BG_SOURCE"), "exp.use.action", params.toString());
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
    		String statusDesc = null;
    		
	    	ExpUseModel expUseModel = null;
	    	
	    	String action = (String)params.get("action");
	    	String type = null;
	    	String exNo = (String)params.get("exNo");
	    	final String by = (String)params.get("by");

			if (action.equals("1")) {
				type = ExpUseConstant.ST_CLOSED_BY_FIN;
				statusDesc = "Approve";
				
		    	expUseModel = expUseService.get(exNo, null);
		    	CommonUtil.checkNull(expUseModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+exNo);

				final NodeRef folderNodeRef = new NodeRef(expUseModel.getFolderRef());
				
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
				type = ExpUseConstant.ST_CANCEL_BY_FIN;
//			} else
//			if (action.equals("3")) {
//				statusDesc = "Paid";
//				type = ExpUseConstant.ST_PAID_BY_FIN;
			} else {
    			result.put("success",false);
    			result.put("message","Invalid Action : "+action);
    			return result;
			}
    		
			if (expUseModel==null) {
				expUseModel = expUseService.get(exNo, null);
		    	CommonUtil.checkNull(expUseModel, CommonConstant.EXT_ERR_MSG_MISMATCHED+exNo);
			}
			expUseModel.setUpdatedBy(by);
	    	expUseModel.setStatus(type);
	    	expUseService.update(session, expUseModel);
    		
	    	String actor = null;
	    	if (expUseModel.getPayType().equals("3")) { // 3 = internal charge
	    		actor = MainWorkflowConstant.TN_SERVICE_UNIT;
	    	} else {
	    		actor = MainWorkflowConstant.TN_FINANCE;
	    	}
	    	
	    	mainWorkflowService.setModuleService(expUseService);
			mainWorkflowService.saveWorkflowHistory(session, null, by, actor,  (String)params.get("comment"), statusDesc, null,  exNo, null, type);
			
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
    	log.info("ex.history()");
    	log.info("  exNo:"+params.get("exNo")
    			+", by:"+params.get("by")
    			+", task:"+params.get("task")
    			+", task_th:"+params.get("task_th")
    			+", status:"+params.get("status")
    			+", status_th:"+params.get("status_th")
    			);
    	
    	SqlSession session = ExpUtil.openSession(dataSource);

    	Long infId = adminInterfaceService.add((String)params.get("BG_SOURCE"), "exp.use.history", params.toString());
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
	    	String exNo = (String)params.get("exNo");
	    	final String by = (String)params.get("by");
	    	String task = (String)params.get("task");
	    	String taskTh = (String)params.get("task_th");
	    	String status = (String)params.get("status");
	    	String statusTh = (String)params.get("status_th");
	    	
	    	ExpUseModel expUseModel = expUseService.get(exNo, null);
			if (expUseModel==null) {
				log.info("  Invalid EX : "+exNo);
    			result.put("success",true);
    			result.put("message",CommonConstant.EXT_ERR_MSG_MISMATCHED+exNo);
    			return result;
			}
    		
			mainWorkflowService.saveWorkflowHistory(session, exNo, by, task, taskTh, status, statusTh, (String)params.get("comment"));
			
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
    
	@Transaction(value=TransactionType.REQUIRES_NEW)
    public Map<String, Object> create(Map<String, Object> params)
    {
    	log.info("ex.create()");
    	log.info("  action:"+params.get("action")
    			+", reqBy:"+params.get("reqBy")
    			);
    	
    	final SqlSession session = ExpUtil.openSession(dataSource);
    	
		Long infId = adminInterfaceService.add((String)params.get("BG_SOURCE"), "exp.use.create", params.toString());
		log.info("  infId:"+infId);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
    		
//    		String statusDesc = null;
    		
//	    	ExpUseModel expUseModel = null;
	    	
	    	String action = (String)params.get("action");
//	    	String exNo = (String)params.get("exNo");
	    	String exNo = null;
	    	final String by = params.get("createdBy")!=null ? (String)params.get("createdBy") : (String)params.get("reqBy");
	    	
	    	Boolean found = false;
	    	ExpUseModel model;
	    	ExpUseModel tmpModel = expUseService.get(exNo, null);
    		if (tmpModel==null) {
    			model = new ExpUseModel();
        		model.setId(exNo);
        		model.setCreatedBy(by);
    		} else {
    			model = tmpModel;
    			found = true;
    		}
    		model.setUpdatedBy(by);
    		model.setInfId(infId);

			if (action.equals("1")) {
    			model.setReqBy((String)params.get("reqBy"));
        		
    			model.setObjective((String)params.get("objective"));
    			model.setIsSmallAmount((String)params.get("isSmallAmount"));
    			model.setIsReason((String)params.get("isReason"));
    			model.setReason((String)params.get("reason"));
    			model.setNote((String)params.get("note"));
				model.setBudgetCcType((String)params.get("budgetSrcType"));
				model.setBudgetCc(params.get("budgetSrc") != null && !params.get("budgetSrc").equals("") ? Integer.parseInt((String)params.get("budgetSrc")) : null);
				model.setFundId(params.get("fundId") != null && !params.get("fundId").equals("") ? Integer.parseInt((String)params.get("fundId")) : null);
				
				if (params.get("costControlId")!=null) {
					model.setCostControlTypeId(Integer.parseInt((String)params.get("costControlTypeId")));
					model.setCostControlId(Integer.parseInt((String)params.get("costControlId")));
					model.setCostControl((String)params.get("costControl"));
					model.setCostControlFrom(CommonDateTimeUtil.convertOdooStringToTimestamp((String)params.get("costControlFrom")));
					model.setCostControlTo(CommonDateTimeUtil.convertOdooStringToTimestamp((String)params.get("costControlTo")));
				}
				
				model.setBankType((String)params.get("bankType"));
				model.setBank(params.get("bank") != null && !params.get("bank").equals("") ? Integer.parseInt((String)params.get("bank")) : null);
				
				model.setPayType((String)params.get("payType"));
				model.setPayDtl1(params.get("payDtl1") !=null ? (String)params.get("payDtl1") : null);
				model.setPayDtl2(params.get("payDtl2") !=null ? (String)params.get("payDtl2") : null);
				model.setPayDtl3(params.get("payDtl3") !=null ? (String)params.get("payDtl3") : null);
				
	    		model.setTotal(Double.parseDouble((String)params.get("total")));
				
				model.setRequestedTime(CommonDateTimeUtil.now());
				
				model.setStatus(ExpUseConstant.ST_WAITING);
				model.setWaitingLevel(1);

	    		final List<Map<String, Object>> fileList = (List<Map<String,Object>>) params.get("attachments");
	    		
	    		final Object attendees = params.get("attendees");
	    		final Object items = params.get("items");
	    		
				final ExpUseModel eModel = expUseService.checkEmotion(model, items);
				
		        AuthenticationUtil.runAs(new RunAsWork<String>()
	    	    {
	    			public String doWork() throws Exception
	    			{
	    				ExpUseModel savedModel = expUseService.save(session, eModel, attendees, fileList, true);
	    				
	    				mainWorkflowService.setModuleService(expUseService);
	    				Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_EX, eModel);
	    				mainWorkflowService.startWorkflow(session, savedModel, bossMap);
	    				
	    	        	return null;
	    			}
	    	    }, AuthenticationUtil.getAdminUserName());  
			} else {
    			result.put("success",false);
    			result.put("message","Invalid Action : "+action);
    			return result;
			}
    		
			result.put("success",true);
			result.put("exNO", model.getId());
			result.put("message","Success");
			session.commit();
			
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
    
    public Map<String, Object> test(Map<String, Object> params)
    {
    	log.info("ex.test()");
//    	log.info("params:"+params.toString());
    	
		Long infId = adminInterfaceService.add((String)params.get("BG_SOURCE"), "exp.use.test", params.toString());
		log.info("infId:"+infId);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	SqlSession session = ExpUtil.openSession(dataSource);
    	
    	try {
    		
//    		String statusDesc = null;
    		
	    	String action = (String)params.get("action");
	    	String exNo = (String)params.get("exNo");
//	    	String exNo = null;
	    	final String by = (String)params.get("by");
	    	
	    	ExpUseModel model = new ExpUseModel();
        	model.setId(exNo);
        	model.setCreatedBy((String)params.get("reqBy"));
        		
    		model.setUpdatedBy(by);
    		model.setInfId(infId);

			if (action.equals("1")) {
    			model.setReqBy((String)params.get("reqBy"));
        		
    			model.setObjective((String)params.get("objective"));
    			model.setIsSmallAmount((String)params.get("isSmallAmount"));
    			model.setIsReason((String)params.get("isReason"));
    			model.setReason((String)params.get("reason"));
    			model.setNote((String)params.get("note"));
				model.setBudgetCcType((String)params.get("budgetSrcType"));
				model.setBudgetCc(params.get("budgetSrc") != null && !params.get("budgetSrc").equals("") ? Integer.parseInt((String)params.get("budgetSrc")) : null);
				model.setFundId(params.get("fundId") != null && !params.get("fundId").equals("") ? Integer.parseInt((String)params.get("fundId")) : null);
				
				model.setCostControlTypeId(Integer.parseInt((String)params.get("costControlTypeId")));
				model.setCostControlId(Integer.parseInt((String)params.get("costControlId")));
				model.setCostControl((String)params.get("costControl"));
				model.setCostControlFrom(CommonDateTimeUtil.convertOdooStringToTimestamp((String)params.get("costControlFrom")));
				model.setCostControlTo(CommonDateTimeUtil.convertOdooStringToTimestamp((String)params.get("costControlTo")));
				
				model.setBankType((String)params.get("bankType"));
				model.setBank(params.get("bank") != null && !params.get("bank").equals("") ? Integer.parseInt((String)params.get("bank")) : null);
				
				model.setPayType((String)params.get("payType"));
				model.setPayDtl1(params.get("payDtl1") !=null ? (String)params.get("payDtl1") : null);
				model.setPayDtl2(params.get("payDtl2") !=null ? (String)params.get("payDtl2") : null);
				model.setPayDtl3(params.get("payDtl3") !=null ? (String)params.get("payDtl3") : null);
				
	    		model.setTotal(Double.parseDouble((String)params.get("total")));
				
				model.setRequestedTime(CommonDateTimeUtil.now());
				
				model.setStatus(ExpUseConstant.ST_WAITING);
				model.setWaitingLevel(1);

	    		List<Map<String, Object>> fileList = (List<Map<String,Object>>) params.get("attachments");
	    		
	    		Object attendees = params.get("attendees");
	    		Object items = params.get("items");
	    		
				model = expUseService.save(session, model, attendees, fileList, true);
				
				String createResult = interfaceService.createEX(model);
			} else {
				session.rollback();
    			result.put("success",false);
    			result.put("message","Invalid Action : "+action);
    			return result;
			}
    		
			result.put("success",true);
			result.put("exNO", model.getId());
			result.put("message","Success");
    		session.commit();
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
