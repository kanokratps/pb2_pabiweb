package pb.repo.hr.workflow.sal.requester;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
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

import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.model.MainWorkflowReviewerModel;
import pb.repo.admin.service.AdminCompleteNotificationService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.AlfrescoService;
import pb.repo.admin.util.MainUserGroupUtil;
import pb.repo.admin.util.MainWorkflowUtil;
import pb.repo.hr.constant.HrSalConstant;
import pb.repo.hr.constant.HrSalWorkflowConstant;
import pb.repo.hr.model.HrSalModel;
import pb.repo.hr.service.HrSalService;
import pb.repo.hr.service.HrSalWorkflowService;
import pb.repo.hr.service.HrSalSignatureService;
import pb.repo.hr.util.HrUtil;

@Component("pb.hr.workflow.sal.requester.EndRemoteExecutor")
public class EndRemoteExecutor implements ExecutionListener {
	
	private static Logger log = Logger.getLogger(EndRemoteExecutor.class);
	
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
	
	@Autowired
	DataSource dataSource;
	
	private static final String WF_PREFIX = HrSalWorkflowConstant.MODEL_PREFIX;
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	@Override
	public void notify(final DelegateExecution execution) throws Exception {
		log.info("<- hr.requester.EndRemoteExecutor ->");

		AuthenticationUtil.runAs(new RunAsWork<String>() {
			public String doWork() throws Exception
			{
				SqlSession session = HrUtil.openSession(dataSource);
				try {
					Object id = ObjectUtils.defaultIfNull(execution.getVariable(WF_PREFIX+"id"), "");

					log.info("  id:" + id.toString());
					log.info("  execution.id:"+execution.getId());

					Set<String> names =  execution.getVariableNames();
					for(String name : names) {
						log.info(" - name:"+name+":"+execution.getVariable(name));
					}
					
					String finalAction = (String)execution.getVariable(WF_PREFIX+"reSubmitOutcome");
					HrSalModel model = hrSalService.get(id.toString(), null);
					Integer level = model.getWaitingLevel();
					log.info("finalAction:"+finalAction);
					mainWorkflowService.setModuleService(hrSalService);
					
					String curUser = (String)execution.getVariable("reqBy");
					String taskKey = MainWorkflowConstant.TN_PREPARER;
					if (finalAction.equals(MainWorkflowConstant.TA_CANCEL)) {
						model.setStatus(HrSalConstant.ST_CANCEL_BY_PCM);
						model.setWaitingLevel(null);
					} 
					else 
					if (finalAction.equals(MainWorkflowConstant.TA_RESUBMIT)) {
						String docType = (String)execution.getVariable("docType");
						
	    				Map<String, String> bossMap = mainWorkflowService.getBossMap(docType, model);
						mainWorkflowService.setReviewer(session, null, model, new NodeRef(model.getFolderRef()), bossMap);
				        mainWorkflowService.replaceReviewer(session, id.toString());
						
						model.setStatus(HrSalConstant.ST_WAITING);
						model.setWaitingLevel(1);
						
						MainWorkflowReviewerModel paramModel = new MainWorkflowReviewerModel();
						paramModel.setMasterId(id.toString());
						paramModel.setLevel(model.getWaitingLevel());
						MainWorkflowReviewerModel reviewerModel = mainWorkflowService.getReviewer(session, paramModel);
						if (reviewerModel != null) {
							execution.setVariable(WF_PREFIX+"nextReviewers", MainUserGroupUtil.codes2logins(reviewerModel.getReviewerUser()));
						} else {
							execution.setVariable(WF_PREFIX+"nextReviewers", "");
						}
					}
					execution.setVariable("LEVEL", model.getWaitingLevel());
					
					hrSalService.prepareModelForWfDesc(model, "th");
					String desc = hrSalService.getWorkflowDescription(model);
					
					execution.setVariable("bpm_"+WorkflowModel.PROP_DESCRIPTION.getLocalName(), desc);
					execution.setVariable("bpm_"+WorkflowModel.PROP_WORKFLOW_DESCRIPTION.getLocalName(), desc);	         
					
					// Keep TaskId to pcmwf:taskHistory.
					String taskHistory = (String)execution.getVariable(WF_PREFIX+"taskHistory");
					String finalTaskHistory = MainWorkflowUtil.appendTaskKey(taskHistory, taskKey, level);
					execution.setVariable(WF_PREFIX+"taskHistory", finalTaskHistory);

					log.info("  status:"+model.getStatus()+", waitingLevel:"+model.getWaitingLevel());
					hrSalService.updateStatus(model);
					
					// Comment History
					String taskComment = "";
					Object tmpComment = execution.getVariable("comment");
					if(tmpComment != null && !tmpComment.equals("")){
						taskComment = tmpComment.toString();
					}
					
					String action = mainWorkflowService.saveWorkflowHistory(session, execution, curUser, taskKey, taskComment, finalAction, null,  model.getId(), level, model.getStatus());
					
					if (finalAction.equals(MainWorkflowConstant.TA_RESUBMIT)) {
						MainWorkflowModel workflowModel = new MainWorkflowModel();
						workflowModel.setMasterId(id.toString());
						workflowModel = mainWorkflowService.getLastWorkflow(session, workflowModel);
				        MainWorkflowHistoryModel workflowHistoryModel = hrSalService.getAppByWorkflowHistory(model); // Extra History
				        if (workflowHistoryModel != null) {
					        workflowHistoryModel.setMasterId(workflowModel.getId());
					        mainWorkflowService.addWorkflowHistory(session,workflowHistoryModel);
				        }
					}
					session.commit();
				}
				catch (Exception ex) {
					session.rollback();
					log.error(ex);
				} finally {
					session.close();
				}
				
				return null;
			}
		}, AuthenticationUtil.getAdminUserName()); // runAs()		
	}
	
}
