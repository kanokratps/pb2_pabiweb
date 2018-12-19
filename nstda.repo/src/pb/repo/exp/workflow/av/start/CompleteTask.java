package pb.repo.exp.workflow.av.start;

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

import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.exp.constant.ExpBrwWorkflowConstant;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;

@Component("pb.exp.workflow.av.start.CompleteTask")
public class CompleteTask implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(CompleteTask.class);
	
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
	ExpBrwService expBrwService;
	
	@Autowired
	ExpBrwWorkflowService mainWorkflowService;

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
	
	private static final String WF_PREFIX = ExpBrwWorkflowConstant.MODEL_PREFIX;
	
//	public void notify(final DelegateTask task)  {
//		
//		log.info("<- pr.start.CompleteTask ->");
//		
//		AuthenticationUtil.runAs(new RunAsWork<String>() {
//			public String doWork() throws Exception
//			{
//				log.info("  task.getTaskDefinitionKey():" + task.getTaskDefinitionKey());
//				log.info("  task.id="+task.getId());
//				log.info("  task.Description="+task.getDescription());
//				log.info("  task.EventName="+task.getEventName());
//				log.info("  task.Name="+task.getName());
//				log.info("  task.Owner="+task.getOwner());
//				
//				try {
//					Object id = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+"id"), "");
//					log.info("  id :: " + id.toString());
//					ExpBrwModel model = expBrwService.get(id.toString());
//					Integer level = model.getWaitingLevel();
//					Integer lastLevel = mainWorkflowService.getLastReviewerLevel(model.getId());
//					ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
//					
//					String curUser = authenticationService.getCurrentUserName();
//					String taskKey = task.getTaskDefinitionKey();
//					String outcomeName = MainWorkflowConstant.TO_REVIEW;
//					String finalAction = MainWorkflowConstant.TA_START;
//					
//					log.info("  level:"+level);
//					log.info("  last level:"+lastLevel);
//					log.info("  action:"+finalAction);
//					
//					model.setStatus(ExpBrwConstant.ST_WAITING);
//					model.setWaitingLevel(1);
//					
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "requestedTime");
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "remark");
//					
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "document");
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "attachDocument");
//					
//					
//					// Keep TaskId to expusewf:taskHistory.
//					String taskHistory = (String)executionEntity.getVariable(WF_PREFIX+"taskHistory");
//					String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
//					executionEntity.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);
//
//					log.info("  status : "+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
//					expBrwService.updateStatus(model);
//					
//					String taskComment = model.getObjectiveType() + " " + model.getObjective() + " " + model.getReason();
//					
//					mainWorkflowService.setModuleService(expBrwService);
//					finalAction = mainWorkflowService.saveWorkflowHistory(executionEntity, curUser, task.getName(), taskComment, finalAction, task,  model.getId(), level);
//				}
//				catch (Exception ex) {
//					log.error(ex);
//				}
//				
//				return null;
//			}
//		}, AuthenticationUtil.getAdminUserName()); // runAs()
//	}

	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		log.info("<- av.start.CompleteTask ->");
		
		AuthenticationUtil.runAs(new RunAsWork<String>() {
			public String doWork() throws Exception
			{
//				log.info("  task.getTaskDefinitionKey():" + task.getTaskDefinitionKey());
//				log.info("  task.id="+task.getId());
//				log.info("  task.Description="+task.getDescription());
//				log.info("  task.EventName="+task.getEventName());
//				log.info("  task.Name="+task.getName());
//				log.info("  task.Owner="+task.getOwner());
				
				try {
//					Object id = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+"id"), "");
					Object id = ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");
					
					log.info("  id:" + id.toString());
//					ExpBrwModel model = expBrwService.get(id.toString(), null);
//					Integer level = model.getWaitingLevel();
//					Integer lastLevel = mainWorkflowService.getLastReviewerLevel(model.getId());
//					ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
					
//					String curUser = authenticationService.getCurrentUserName();
//					String taskKey = MainWorkflowConstant.WF_TASK_NAMES.get(MainWorkflowConstant.TN_PREPARER);;
//					String finalAction = MainWorkflowConstant.TA_START;
					
//					log.info("  level:"+level);
//					log.info("  last level:"+lastLevel);
//					log.info("  action:"+finalAction);
//					
//					model.setStatus(ExpBrwConstant.ST_WAITING);
//					model.setWaitingLevel(1);
					
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "requestedTime");
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "remark");
//					
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "document");
//					mainWorkflowService.updateExecutionEntity(executionEntity, task, "attachDocument");
					
					
					// Keep TaskId to expbrwwf:taskHistory.
//					String taskHistory = (String)execution.getVariable(WF_PREFIX+"taskHistory");
//					String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
//					execution.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);

//					log.info("  status : "+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
//					expBrwService.updateStatus(model);
					
//					String taskComment = model.getObjectiveType() + " " + model.getObjective() + " " + model.getReason();
//					
//					mainWorkflowService.setModuleService(expBrwService);
//					finalAction = mainWorkflowService.saveWorkflowHistory(execution, curUser, taskKey, taskComment, finalAction, null,  model.getId(), level, ExpBrwConstant.ST_WAITING);
					execution.setVariable("LEVEL", 1);
				}
				catch (Exception ex) {
					log.error(ex);
				}
				
				return null;
			}
		}, AuthenticationUtil.getAdminUserName()); // runAs()		
	}
	
}
