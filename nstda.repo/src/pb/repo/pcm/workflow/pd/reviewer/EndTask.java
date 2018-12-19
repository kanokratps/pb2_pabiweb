package pb.repo.pcm.workflow.pd.reviewer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.pcm.constant.PcmOrdConstant;
import pb.repo.pcm.constant.PcmOrdWorkflowConstant;
import pb.repo.pcm.model.PcmOrdModel;
import pb.repo.pcm.service.PcmOrdService;
import pb.repo.pcm.service.PcmOrdWorkflowService;

@Component("pb.pcm.workflow.pd.reviewer.EndTask")
public class EndTask implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(EndTask.class);

	@Autowired
	PcmOrdWorkflowService mainWorkflowService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PcmOrdService pcmOrdService;
	
	@Autowired
	AdminViewerService viewerService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	private static final String WF_PREFIX = PcmOrdWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		log.info("<- pd.reviewer.EndTask ->");
//		String curUser = authenticationService.getCurrentUserName();
		try {
			String id = (String)ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			
			PcmOrdModel model = pcmOrdService.get(id.toString(), null);
			model.setStatus(PcmOrdConstant.ST_CLOSED_BY_ACT);
			pcmOrdService.updateStatus(model);
		} catch (Exception ex) {
			log.error("", ex);
		}
        
	}
	
}

