package pb.repo.pcm.workflow.pd.reviewer;

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
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.model.MainWorkflowReviewerModel;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.admin.util.MainUserGroupUtil;
import pb.repo.admin.util.MainUtil;
import pb.repo.admin.util.MainWorkflowUtil;
import pb.repo.pcm.constant.PcmOrdWorkflowConstant;
import pb.repo.pcm.constant.PcmOrdConstant;
import pb.repo.pcm.model.PcmOrdModel;
import pb.repo.pcm.service.InterfaceService;
import pb.repo.pcm.service.PcmOrdService;
import pb.repo.pcm.service.PcmOrdWorkflowService;
import pb.repo.pcm.util.PcmUtil;

@Component("pb.pcm.workflow.pd.reviewer.CompleteTask")
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
	PcmOrdService pcmOrdService;
	
	@Autowired
	PcmOrdWorkflowService mainWorkflowService;

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
	InterfaceService interfaceService;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	DataSource dataSource;
	
	private static final String WF_PREFIX = PcmOrdWorkflowConstant.MODEL_PREFIX;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	public void notify(final DelegateTask task) {
		
		log.info("<- pd.reviewer.CompleteTask ->");
		
		try {
			
			AuthenticationUtil.runAs(new RunAsWork<String>() {
				public String doWork() throws Exception
				{
					MainWorkflowUtil.logTask(log, task);
					
						Object id = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+"id"), "");
						log.info("  id:" + id.toString());
						PcmOrdModel model = pcmOrdService.get(id.toString(), null);
						Integer level = model.getWaitingLevel();
						Integer lastLevel = mainWorkflowService.getLastReviewerLevel(model.getId());
						ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
						
						String curUser = authenticationService.getCurrentUserName();
						String taskKey = task.getTaskDefinitionKey();
						String outcomeName = MainWorkflowConstant.TO_REVIEW;
						String action = task.getVariable(WF_PREFIX+outcomeName)!=null ? task.getVariable(WF_PREFIX+outcomeName).toString():null;
						
						log.info("  level:"+level);
						log.info("  last level:"+lastLevel);
						log.info("  action:"+action);
						
						if (level==null) {
							String lang = (String)task.getVariable(WF_PREFIX+"lang");
							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_LEVEL_IS_NULL", new Locale(lang));
							throw new FormException(CommonConstant.FORM_ERR+errMsg);
						}
						else
						if (lastLevel==null) {
							String lang = (String)task.getVariable(WF_PREFIX+"lang");
							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_LAST_LEVEL_IS_NULL", new Locale(lang));
							throw new FormException(CommonConstant.FORM_ERR+errMsg);
						}
						else
						if (model.getDocRef()==null) {
							String lang = (String)task.getVariable(WF_PREFIX+"lang");
							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_DOC_REF_IS_NULL", new Locale(lang));
							throw new FormException(CommonConstant.FORM_ERR+errMsg);
						}
						
						SqlSession session = PcmUtil.openSession(dataSource);
						
						try {
							mainWorkflowService.setModuleService(pcmOrdService);
							
							String finalAction = action;
							if (action.equalsIgnoreCase(MainWorkflowConstant.TA_REJECT)) {
								Object comment = task.getVariable("bpm_comment");
								if (comment==null || comment.toString().trim().equals("")) {
									String lang = (String)task.getVariable(WF_PREFIX+"lang");
									String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_REJECT_NO_COMMENT", new Locale(lang));
									throw new FormException(CommonConstant.FORM_ERR+errMsg);
								}
								
								String result = interfaceService.updateStatusPD(model, finalAction, curUser, (String)comment, authService.getCurrentUserName());
								
								if (!result.equals("OK")) {
									throw new FormException(CommonConstant.FORM_ERR+result);
								}
								
								model.setStatus(PcmOrdConstant.ST_WAITING_REJECT);
								model.setWaitingLevel(0);
								
								MainWorkflowModel wfModel = new MainWorkflowModel();
								wfModel.setMasterId(id.toString());
										
								wfModel = mainWorkflowService.getLastWorkflow(session, wfModel);
								wfModel.setExecutionId(task.getExecutionId());
								mainWorkflowService.update(session, wfModel);
							}
							else
							if (action.equalsIgnoreCase(MainWorkflowConstant.TA_APPROVE)) {
								if (lastLevel.equals(level)) {
									Object comment = task.getVariable("bpm_comment");
									if (comment==null) {
										comment = "";
									}
									String result = interfaceService.updateStatusPD(model, finalAction, curUser, (String)comment, model.getCreatedBy());
									
									if (!result.equals("OK")) {
										throw new FormException(CommonConstant.FORM_ERR+result);
									}
								}
								
								model.setWaitingLevel(model.getWaitingLevel()!=null ? model.getWaitingLevel()+1 : null);
								
								MainWorkflowReviewerModel paramModel = new MainWorkflowReviewerModel();
								paramModel.setMasterId(id.toString());
								paramModel.setLevel(model.getWaitingLevel());
								MainWorkflowReviewerModel reviewerModel = mainWorkflowService.getReviewer(session, paramModel);
								if (reviewerModel != null) {
									executionEntity.setVariable(WF_PREFIX+"nextReviewers", MainUserGroupUtil.codes2logins(reviewerModel.getReviewerUser()));
								} else {
									executionEntity.setVariable(WF_PREFIX+"nextReviewers", "");
								}
							}
							else
							if (action.equalsIgnoreCase(MainWorkflowConstant.TA_CONSULT)) {
								Object  consultant = task.getVariable(WF_PREFIX+"consultant");
								if (consultant==null || consultant.equals("")) {
									String lang = (String)task.getVariable(WF_PREFIX+"lang");
									String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_CONSULT_NO_CONSULTANT", new Locale(lang));
									throw new FormException(CommonConstant.FORM_ERR+errMsg);
								}
								
								model.setStatus(PcmOrdConstant.ST_CONSULT);
								executionEntity.setVariable(WF_PREFIX+"nextReviewers", consultant);
								executionEntity.setVariable(WF_PREFIX+"counselee", curUser);
							}
							task.getExecution().setVariable("LEVEL", model.getWaitingLevel());
							
							executionEntity.setVariable(WF_PREFIX+outcomeName, action);
							
							mainWorkflowService.updateExecutionEntity(executionEntity, task, "requestedTime");
							mainWorkflowService.updateExecutionEntity(executionEntity, task, "remark");
							
							mainWorkflowService.updateExecutionEntity(executionEntity, task, "document");
							mainWorkflowService.updateExecutionEntity(executionEntity, task, "attachDocument");
							
							
							// Keep TaskId to pcmwf:taskHistory.
							String taskHistory = (String)executionEntity.getVariable(WF_PREFIX+"taskHistory");
							String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
							executionEntity.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);
		
							log.info("  status:"+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
	//						pcmOrdService.updateStatus(model);
							
							executionEntity.setVariable(WF_PREFIX+"workflowStatus", action);
												
							// Comment History
							String taskComment = "";
							Object tmpComment = task.getVariable("bpm_comment");
							if(tmpComment != null && !tmpComment.equals("")){
								taskComment = tmpComment.toString();
							}
						
							action = mainWorkflowService.saveWorkflowHistory(session, executionEntity, curUser, MainWorkflowConstant.TN_REVIEWER, taskComment, finalAction, task,  model.getId(), level, model.getStatus());
							
							pcmOrdService.update(model);
							mainWorkflowService.updateWorkflow(model, task);
							
//							if (finalAction.equalsIgnoreCase(MainWorkflowConstant.TA_APPROVE) && level.equals(lastLevel)) {
//								signatureService.addSignature(task, curUser, lastLevel);
//							}
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
