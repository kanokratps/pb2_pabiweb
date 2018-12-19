package pb.repo.hr.workflow.sal.requester;

import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.TemplateService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.hr.constant.HrSalWorkflowConstant;
import pb.repo.hr.model.HrSalModel;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;
import pb.repo.hr.service.HrSalSignatureService;

@Component("pb.hr.workflow.sal.requester.StartRemoteExecutor")
public class StartRemoteExecutor implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(StartRemoteExecutor.class);
	
	private static final long serialVersionUID = 1L;
	
	Properties properties = new Properties();
	
	@Autowired
	FileFolderService fileFolderService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	CheckOutCheckInService checkOutCheckInService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	AuthenticationUtil authenticationUtil;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	HrSalService hrSalService;
	
	@Autowired
	HrSalWorkflowService mainWorkflowService;

	@Autowired
	HrSalSignatureService signatureService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AdminCompleteNotificationService completeNotificationService;
	
	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	AdminViewerService viewerService;
	
	@Autowired
	TemplateService templateService;
	
	private static final String WF_PREFIX = HrSalWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		log.info("<- hr.requester.StartRemoteExecutor ->");
		
		AuthenticationUtil.runAs(new RunAsWork<String>() {
			public String doWork() throws Exception
			{
				try {
					Object id = ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
					
					log.info("  id:" + id.toString());
					log.info("  executeion.id:"+execution.getId());
//					HrSalModel model = hrSalService.get(id.toString());
//					Integer level = model.getWaitingLevel();
//					
//					String curUser = authenticationService.getCurrentUserName();
//					String taskKey = MainWorkflowConstant.TN_REQUESTER_CAPTION;
//					String finalAction = MainWorkflowConstant.TA_START;
//					
//					String taskComment = model.getObjective()+ " " + model.getDocType();
//					
//					mainWorkflowService.setModuleService(hrSalService);
//					finalAction = mainWorkflowService.saveWorkflowHistory(execution, curUser, taskKey, taskComment, finalAction, null,  model.getId(), level);
				}
				catch (Exception ex) {
					log.error(ex);
				}
				
				return null;
			}
		}, AuthenticationUtil.getAdminUserName()); // runAs()		
	}
	
}
