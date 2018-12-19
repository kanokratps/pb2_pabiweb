package pb.repo.hr.workflow.sal.reviewer;

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
import pb.repo.hr.constant.HrSalConstant;
import pb.repo.hr.constant.HrSalWorkflowConstant;
import pb.repo.hr.model.HrSalModel;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;
import pb.repo.hr.service.HrSalSignatureService;

@Component("pb.hr.workflow.sal.reviewer.EndTask")
public class EndTask implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(EndTask.class);

	@Autowired
	HrSalWorkflowService mainWorkflowService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	HrSalService hrSalService;
	
	@Autowired
	AdminViewerService viewerService;
	
	@Autowired
	HrSalSignatureService memoSignatureService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	private static final String WF_PREFIX = HrSalWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		log.info("<- hr.reviewer.EndTask ->");
//		String curUser = authenticationService.getCurrentUserName();
		try {
			String id = (String)ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			
			HrSalModel model = hrSalService.get(id.toString(), null);
			model.setStatus(HrSalConstant.ST_CLOSED_BY_ACT);
			hrSalService.updateStatus(model);
		} catch (Exception ex) {
			log.error("", ex);
		}
        
	}
	
}

