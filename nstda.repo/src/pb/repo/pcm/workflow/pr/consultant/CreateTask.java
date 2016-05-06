package pb.repo.pcm.workflow.pr.consultant;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminViewerService;
import pb.repo.admin.service.MainWorkflowService;
import pb.repo.pcm.constant.PcmReqWorkflowConstant;
import pb.repo.pcm.model.PcmReqModel;
import pb.repo.pcm.service.PcmReqService;
import pb.repo.pcm.service.PcmSignatureService;

@Component("pb.pcm.workflow.pr.consultant.CreateTask")
public class CreateTask implements TaskListener {
	
	private static Logger log = Logger.getLogger(CreateTask.class);

	@Autowired
	MainWorkflowService mainWorkflowService;
	
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
	PcmSignatureService memoSignatureService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	private static final String WF_PREFIX = PcmReqWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(DelegateTask task) {
		
		ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
	
		String taskKey = task.getTaskDefinitionKey();
		log.info("<- pr.consultant.CreateTask -> Name:"+task.getName()+", ID:"+taskKey);
		String curUser = authenticationService.getCurrentUserName();
		task.setVariable(WF_PREFIX+"currentTaskKey", taskKey);
		executionEntity.setVariable(WF_PREFIX+"currentTaskKey", taskKey);
		try {
		
			String id = (String)ObjectUtils.defaultIfNull(executionEntity.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			PcmReqModel model = pcmReqService.get(id.toString());
			Integer level = model.getWaitingLevel();
			
			task.setVariable("bpm_status", mainWorkflowService.getWorkflowStatus(WF_PREFIX, executionEntity, taskKey, level));
			task.setVariable("bpm_reassignable", Boolean.FALSE);
			
			task.setName(MainWorkflowConstant.WF_TASK_NAMES.get(MainWorkflowConstant.TN_CONSULTANT));
			
			/*
			 * Update DB
			 */
			String varName = WF_PREFIX+"workflowStatus";
			if (executionEntity.getVariable(varName)!=null) {
				MainWorkflowModel pcmWorkflowModel = new MainWorkflowModel();
				pcmWorkflowModel.setMasterId(id.toString());
				
				pcmWorkflowModel = mainWorkflowService.getLastWorkflow(pcmWorkflowModel);
				
				pcmWorkflowModel.setAssignee(task.getAssignee());
				pcmWorkflowModel.setTaskId("activiti$"+task.getId());
				mainWorkflowService.update(pcmWorkflowModel);
			}
	
		} catch (Exception ex) {
			log.error("", ex);
		}
        
	}
}

