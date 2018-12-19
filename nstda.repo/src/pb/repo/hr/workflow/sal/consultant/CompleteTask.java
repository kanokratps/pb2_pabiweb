package pb.repo.hr.workflow.sal.consultant;

import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.alfresco.repo.forms.FormException;
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
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;

import pb.common.constant.CommonConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.admin.util.MainUtil;
import pb.repo.admin.util.MainWorkflowUtil;
import pb.repo.hr.constant.HrSalConstant;
import pb.repo.hr.constant.HrSalWorkflowConstant;
import pb.repo.hr.model.HrSalModel;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;
import pb.repo.hr.service.HrSalSignatureService;
import pb.repo.hr.util.HrUtil;

@Component("pb.hr.workflow.sal.consultant.CompleteTask")
public class CompleteTask implements TaskListener {
	
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
	HrSalService hrSalService;
	
	@Autowired
	HrSalWorkflowService mainWorkflowService;

	@Autowired
	HrSalSignatureService memoSignatureService;
	
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

	@Autowired
	DataSource dataSource;
	
	private static final String WF_PREFIX = HrSalWorkflowConstant.MODEL_PREFIX;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	public void notify(final DelegateTask task)  {
		
		log.info("<- hr.consultant.CompleteTask ->");
		
		try {
		
			AuthenticationUtil.runAs(new RunAsWork<String>() {
				public String doWork() throws Exception
				{
					MainWorkflowUtil.logTask(log, task);
					
					Object id = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+"id"), "");
					log.info("  id:" + id.toString());
					HrSalModel model = hrSalService.get(id.toString(), null);
					Integer level = model.getWaitingLevel();
					Integer lastLevel = mainWorkflowService.getLastReviewerLevel(model.getId());
					ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
					
					String curUser = authenticationService.getCurrentUserName();
					String taskKey = task.getTaskDefinitionKey();
					String outcomeName = MainWorkflowConstant.TO_CONSULT;
					String action = task.getVariable(WF_PREFIX+outcomeName)!=null ? task.getVariable(WF_PREFIX+outcomeName).toString():null;
					
					log.info("  level:"+level);
					log.info("  last level:"+lastLevel);
					log.info("  action:"+action);
					
					mainWorkflowService.setModuleService(hrSalService);
					
					String finalAction = action;
					if (action.equalsIgnoreCase(MainWorkflowConstant.TA_COMMENT)) {
						Object comment = task.getVariable("bpm_comment");
						if (comment==null || comment.toString().trim().equals("")) {
							String lang = (String)task.getVariable(WF_PREFIX+"lang");
							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_COMMENT_NO_COMMENT", new Locale(lang));
							throw new FormException(CommonConstant.FORM_ERR+errMsg);
						}						
						
						Object counselee = task.getVariable(WF_PREFIX+"counselee");
						log.info("::::counselee:::::"+counselee);
						
						executionEntity.setVariable(WF_PREFIX+"nextReviewers", counselee);
						
						model.setStatus(HrSalConstant.ST_WAITING);
						executionEntity.setVariable(WF_PREFIX+"workflowStatus", action);
					}
					
					executionEntity.setVariable(WF_PREFIX+outcomeName, action);
					
					mainWorkflowService.updateExecutionEntity(executionEntity, task, "requestedTime");
					mainWorkflowService.updateExecutionEntity(executionEntity, task, "remark");
					
					mainWorkflowService.updateExecutionEntity(executionEntity, task, "document");
					mainWorkflowService.updateExecutionEntity(executionEntity, task, "attachDocument");
					
					
					// Keep TaskId to pcmwf:taskHistory.
					String taskHistory = (String)executionEntity.getVariable(WF_PREFIX+"taskHistory");
					String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
					executionEntity.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);
					log.info("  taskHistory:" + finalTaskHistory);

					log.info("  status:"+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
//					hrSalService.updateStatus(model);
										
					// Comment History
					String taskComment = "";
					Object tmpComment = task.getVariable("bpm_comment");
					if(tmpComment != null && !tmpComment.equals("")){
						taskComment = tmpComment.toString();
					}
					
					SqlSession session = HrUtil.openSession(dataSource);
					
					try {
						action = mainWorkflowService.saveWorkflowHistory(session, executionEntity, curUser, MainWorkflowConstant.TN_CONSULTANT, taskComment, finalAction, task,  model.getId(), level, model.getStatus());
					
						hrSalService.update(model);
						mainWorkflowService.updateWorkflow(model, task);
						session.commit();
					} catch (Exception ex) {
						session.rollback();
						throw ex;
					} finally {
						session.close();
					}
					
					return null;
				}
			}, AuthenticationUtil.getAdminUserName()); // runAs()
		
		}
		catch (Exception ex) {
			if (ex instanceof FormException) {
				log.error(ex.getMessage());
			} else {
				log.error("",ex);
			}
			throw ex;
		}
	
	}
}
