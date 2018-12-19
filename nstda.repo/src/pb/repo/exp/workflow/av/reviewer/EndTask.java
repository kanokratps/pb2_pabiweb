package pb.repo.exp.workflow.av.reviewer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AdminWkfConfigService;
import pb.repo.exp.constant.ExpBrwWorkflowConstant;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;

@Component("pb.exp.workflow.av.reviewer.EndTask")
public class EndTask implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(EndTask.class);

	@Autowired
	ExpBrwWorkflowService mainWorkflowService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	ExpBrwService expBrwService;
	
	@Autowired
	AdminViewerService viewerService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	AdminWkfConfigService adminWkfConfigService;
	
	private static final String WF_PREFIX = ExpBrwWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		log.info("<- av.reviewer.EndTask ->");
//		String curUser = authenticationService.getCurrentUserName();
		try {
		
			String id = (String)ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			
			ExpBrwModel model = expBrwService.get(id.toString(), null);
			model.setStatus(ExpBrwConstant.ST_CLOSED_BY_ACT);
			expBrwService.updateStatus(model);
			
//			String varName = WF_PREFIX+"reviewOutcome";
//			String action = (String)execution.getVariable(varName);
//			log.info("  reviewOutcome:" + action);
//
//			varName = WF_PREFIX+"nextReviewers";
//			String value = (String)execution.getVariable(varName);
//			log.info("  nextReviewers:" + value);

//			String action = "Complete";
//			
//			mainWorkflowService.saveWorkflowHistory(execution, curUser, "Sent", "", action, null, id, null);
			
		} catch (Exception ex) {
			log.error("", ex);
		}
        
	}
	
}

