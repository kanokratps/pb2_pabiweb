package pb.repo.exp.workflow.av.requester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.alfresco.repo.forms.FormException;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.workflow.WorkflowModel;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;

import pb.common.constant.CommonConstant;
import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.model.MainWorkflowReviewerModel;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminModuleService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.admin.util.MainUserGroupUtil;
import pb.repo.admin.util.MainUtil;
import pb.repo.admin.util.MainWorkflowUtil;
import pb.repo.exp.constant.ExpBrwWorkflowConstant;
import pb.repo.exp.model.ExpBrwDtlModel;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;
import pb.repo.exp.util.ExpUtil;

@Component("pb.exp.workflow.av.requester.CompleteTask")
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
	
	@Autowired
	AdminModuleService moduleService;
	
	@Autowired
	DataSource dataSource;
	
	private static final String WF_PREFIX = ExpBrwWorkflowConstant.MODEL_PREFIX;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	public void notify(final DelegateTask task)  {
		
		log.info("<- av.requester.CompleteTask ->");
		
		try {
		
			AuthenticationUtil.runAs(new RunAsWork<String>() {
				public String doWork() throws Exception
				{
					MainWorkflowUtil.logTask(log, task);
					
					Object id = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+"id"), "");
					log.info("  id:" + id.toString());
					ExpBrwModel model = expBrwService.get(id.toString(), null);
					Integer level = model.getWaitingLevel();
					Integer lastLevel = mainWorkflowService.getLastReviewerLevel(model.getId());
					ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
					
					String curUser = authenticationService.getCurrentUserName();
					String taskKey = task.getTaskDefinitionKey();
					String outcomeName = MainWorkflowConstant.TO_RESUBMIT;
					String action = task.getVariable(WF_PREFIX+outcomeName)!=null ? task.getVariable(WF_PREFIX+outcomeName).toString():null;
					
					log.info("  level:"+level);
					log.info("  last level:"+lastLevel);
					log.info("  action:"+action);
					
					SqlSession session = ExpUtil.openSession(dataSource);
					
					try {
						mainWorkflowService.setModuleService(expBrwService);
						
						String finalAction = action;
						if (action.equalsIgnoreCase(MainWorkflowConstant.TA_CANCEL)) {
							Object comment = task.getVariable("bpm_comment");
							if (comment==null || comment.toString().trim().equals("")) {
	//							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_REJECT_NO_COMMENT", I18NUtil.getLocale());
								String lang = (String)task.getVariable(WF_PREFIX+"lang");
								String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_REJECT_NO_COMMENT", new Locale(lang));
								throw new FormException(CommonConstant.FORM_ERR+errMsg);
							}
							
							model.setStatus(ExpBrwConstant.ST_CANCEL_BY_REQ);
							model.setWaitingLevel(null);
						}
						else
						if (action.equalsIgnoreCase(MainWorkflowConstant.TA_RESUBMIT)) {
							Object comment = task.getVariable("bpm_comment");
							if (comment==null || comment.toString().trim().equals("")) {
								String lang = (String)task.getVariable(WF_PREFIX+"lang");
								String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_RESUBMIT_NO_COMMENT", new Locale(lang));
	//							String errMsg = MainUtil.getMessageWithOutCode("ERR_WF_RESUBMIT_NO_COMMENT", I18NUtil.getLocale());
								throw new FormException(CommonConstant.FORM_ERR+errMsg);
							}
							
							MainMasterModel chkBudgetModel = adminMasterService.getSystemConfig(MainMasterConstant.SCC_MAIN_INF_CHECK_BUDGET);
							Boolean checkBudget = chkBudgetModel.getFlag1().equals(CommonConstant.V_ENABLE);
							
							if (checkBudget) {
	//							Map<String, Object> budget = moduleService.getTotalPreBudget(model.getBudgetCcType(), model.getBudgetCc(), model.getFundId(), null, model.getId());
	//							Boolean budgetOk = Double.parseDouble(((String)budget.get("balance")).replaceAll(",", "")) >= model.getTotal();
								Boolean budgetOk = true;
								if (!budgetOk) {
									String lang = (String)task.getVariable(WF_PREFIX+"lang");
									throw new FormException(CommonConstant.FORM_ERR+MainUtil.getMessageWithOutCode("ERR_WF_BUDGET_NOT_ENOUGH", new Locale(lang)));
								} else {
									
									List<Map<String,Object>> list = new ArrayList();
	
									List<ExpBrwDtlModel> dtls = expBrwService.listDtlByMasterId(model.getId());
								
									for(ExpBrwDtlModel dtl : dtls) {
										
										Map<String, Object> map = new HashMap<String,Object>();
										
										map.put("activity_rpt_id", dtl.getActId());
										map.put("amount", dtl.getAmount());
	
										if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
											map.put("select_asset_id", dtl.getAssetRuleId());
										}
										
										list.add(map);
									}
	
									JSONObject vResult = moduleService.checkBudget("av",model.getBudgetCcType(), model.getBudgetCc(), model.getFundId(), list);
									
									if (!(Boolean)vResult.get("valid")) {
										String lang = (String)task.getVariable(WF_PREFIX+"lang");
										throw new FormException(CommonConstant.FORM_ERR+vResult.get("msg"));
									}
									
	//								Map<Integer, Map<String, Object>> sumDtls = new HashMap<Integer, Map<String,Object>>();
	//								
	//								String budgetResult = null;
	//								
	//								List<ExpBrwDtlModel> dtls = expBrwService.listDtlByMasterId(model.getId());
	//								for(ExpBrwDtlModel dtl : dtls) {
	//									if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
	//										JSONObject vResult = moduleService.validateAssetPrice(dtl.getAssetRuleId(), dtl.getAmount());
	//										if (!(Boolean)vResult.get("valid")) {
	//											budgetOk = false;
	//											budgetResult = (String)vResult.get("msg");
	//											break;
	//										}
	//									}
	//									
	//									if (model.getBudgetCcType().equals("P")) {
	//										
	//										Map<String, Object> map = sumDtls.get(dtl.getActId());
	//										Double oldTotal = 0.0;
	//										if (map==null) {
	//											map = new HashMap<String,Object>();
	//										} else {
	//											oldTotal = (Double)map.get("amount");
	//										}
	//										
	//										map.put("fund_id", model.getFundId());
	//										map.put("project_id", model.getBudgetCc());
	//										map.put("activity_rpt_id", dtl.getActId());
	//										map.put("amount", oldTotal + dtl.getAmount());
	//										
	//										sumDtls.put(dtl.getActId(), map);
	//									}
	//									
	//								}
	//								
	//								if (budgetOk) {
	//									if (model.getBudgetCcType().equals("P")) {
	//										List<Map<String,Object>> list = new ArrayList();
	//										
	//										for(Entry<Integer,Map<String,Object>> e : sumDtls.entrySet()) {
	//											list.add((Map<String,Object>)e.getValue());
	//										}
	//										JSONObject vResult = moduleService.checkFundSpending(list);
	//										if (!(Boolean)vResult.get("valid")) {
	//											budgetOk = false;
	//											budgetResult = (String)vResult.get("msg");
	//										}
	//									}						
	//								}
	//								
	//								if (!budgetOk) {
	//									String lang = (String)task.getVariable(WF_PREFIX+"lang");
	//									throw new FormException(CommonConstant.FORM_ERR+budgetResult);
	//								}
	//								
								}		
							}				
							
							model.setStatus(ExpBrwConstant.ST_WAITING);
							
							if (!model.getCreatedBy().equals(model.getReqBy())) {
								model.setWaitingLevel(0);
								executionEntity.setVariable(WF_PREFIX+"nextReviewers", model.getReqBy());
							}
							else {
								model.setWaitingLevel(1);
								
								// replace reviewer with pb2_main_workflow_assignee
						        mainWorkflowService.replaceReviewer(session, id.toString());
								
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
						}
						task.getExecution().setVariable("LEVEL", model.getWaitingLevel());
						
						executionEntity.setVariable(WF_PREFIX+outcomeName, action);
						
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "objectiveType");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "objective");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "reason");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "budgetCc");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "total");
						
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "requestedTime");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "remark");
						
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "document");
						mainWorkflowService.updateExecutionEntity(executionEntity, task, "attachDocument");
						
						expBrwService.prepareModelForWfDesc(model,"th");
						String desc = expBrwService.getWorkflowDescription(model);
						
						task.getExecution().setVariable("bpm_"+WorkflowModel.PROP_DESCRIPTION.getLocalName(), desc);
						task.getExecution().setVariable("bpm_"+WorkflowModel.PROP_WORKFLOW_DESCRIPTION.getLocalName(), desc);	         
						
						// Keep TaskId to expbrwwf:taskHistory.
						String taskHistory = (String)executionEntity.getVariable(WF_PREFIX+"taskHistory");
						String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
						executionEntity.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);
		
						log.info("  status:"+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
	//					expBrwService.updateStatus(model);
						
						executionEntity.setVariable(WF_PREFIX+"workflowStatus", action);
											
						// Comment History
						String taskComment = "";
						Object tmpComment = task.getVariable("bpm_comment");
						if(tmpComment != null && !tmpComment.equals("")){
							taskComment = tmpComment.toString();
						}
					
						action = mainWorkflowService.saveWorkflowHistory(session, executionEntity, curUser, MainWorkflowConstant.TN_PREPARER, taskComment, finalAction, task,  model.getId(), level, model.getStatus());
						
						expBrwService.update(session, model);
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
