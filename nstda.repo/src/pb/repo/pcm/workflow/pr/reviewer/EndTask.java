package pb.repo.pcm.workflow.pr.reviewer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AdminWkfConfigService;
import pb.repo.pcm.constant.PcmReqWorkflowConstant;
import pb.repo.pcm.model.PcmReqModel;
import pb.repo.pcm.service.PcmReqService;
import pb.repo.pcm.service.PcmReqWorkflowService;

@Component("pb.pcm.workflow.pr.reviewer.EndTask")
public class EndTask implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(EndTask.class);

	@Autowired
	PcmReqWorkflowService mainWorkflowService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PcmReqService pcmReqService;
	
	@Autowired
	AdminViewerService viewerService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	AdminWkfConfigService adminWkfConfigService;
	
	private static final String WF_PREFIX = PcmReqWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		log.info("<- pr.reviewer.EndTask ->");
//		String curUser = authenticationService.getCurrentUserName();
		try {
		
			String id = (String)ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			
			PcmReqModel model = pcmReqService.get(id.toString(), null);
			model.setStatus(PcmReqConstant.ST_CLOSED_BY_ACT);
			pcmReqService.updateStatus(model);
			
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

