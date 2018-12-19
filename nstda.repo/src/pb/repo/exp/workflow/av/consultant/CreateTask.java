package pb.repo.exp.workflow.av.consultant;


import java.util.List;

import javax.sql.DataSource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.common.model.FileModel;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.exp.constant.ExpBrwWorkflowConstant;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;
import pb.repo.exp.util.ExpUtil;

@Component("pb.exp.workflow.av.consultant.CreateTask")
public class CreateTask implements TaskListener {
	
	private static Logger log = Logger.getLogger(CreateTask.class);

	@Autowired
	ExpBrwWorkflowService mainWorkflowService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	ExpBrwService expBrwService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	DataSource dataSource;
	
	private static final String WF_PREFIX = ExpBrwWorkflowConstant.MODEL_PREFIX;
	
	@Override
	public void notify(final DelegateTask task) {
		
		ExecutionEntity executionEntity = ((ExecutionEntity)task.getExecution()).getProcessInstance();
	
		String taskKey = task.getTaskDefinitionKey();
		log.info("<- av.consultant.CreateTask -> Name:"+task.getName()+", ID:"+taskKey);
		String curUser = authenticationService.getCurrentUserName();
		task.setVariable(WF_PREFIX+"currentTaskKey", taskKey);
		executionEntity.setVariable(WF_PREFIX+"currentTaskKey", taskKey);
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
		
			String id = (String)ObjectUtils.defaultIfNull(executionEntity.getVariable(WF_PREFIX+"id"), "");
			log.info("  id:" + id);
			ExpBrwModel model = expBrwService.get(id.toString(), null);
			Integer level = model.getWaitingLevel();
			
			task.setVariable("bpm_status", mainWorkflowService.getWorkflowStatus(WF_PREFIX, executionEntity, taskKey, level));
			task.setVariable("bpm_reassignable", Boolean.FALSE);
			
			task.setName(MainWorkflowConstant.WF_TASK_NAMES.get(MainWorkflowConstant.TN_CONSULTANT));
			
			log.info("consultant assignee:"+task.getAssignee());
			mainWorkflowService.setFolderPermission(new NodeRef(model.getFolderRef()), task.getAssignee());
			
			final List<FileModel> fileList = expBrwService.listFile(model.getId(), false);
			
	        AuthenticationUtil.runAs(new RunAsWork<String>()
    	    {
    			public String doWork() throws Exception
    			{
    				
					if (fileList!=null) {
		        		for(FileModel fileModel : fileList) {
		        			NodeRef att = new NodeRef(fileModel.getNodeRef());
		        			if(nodeService.exists(att)) {
			        			ChildAssociationRef parent = nodeService.getPrimaryParent(att);
			        			
				        		if(!permissionService.getPermissions(parent.getParentRef()).contains(task.getAssignee())) {
				        			permissionService.setPermission(parent.getParentRef(), task.getAssignee(), "SiteConsumer", true);
				        		}
		        			}
		        		}
					}
	
					return null;
    			}
    	    }, AuthenticationUtil.getAdminUserName());			

			/*
			 * Update DB
			 */
			String varName = WF_PREFIX+"workflowStatus";
			if (executionEntity.getVariable(varName)!=null) {
				MainWorkflowModel workflowModel = new MainWorkflowModel();
				workflowModel.setMasterId(id.toString());
				
				workflowModel = mainWorkflowService.getLastWorkflow(session, workflowModel);
				
				workflowModel.setAssignee(task.getAssignee());
				workflowModel.setTaskId("activiti$"+task.getId());
				mainWorkflowService.update(session, workflowModel);
			}
	
			session.commit();
		} catch (Exception ex) {
			session.rollback();
			log.error("", ex);
		} finally {
			session.close();
		}
        
	}
}

