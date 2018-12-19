package pb.repo.admin.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.TemplateService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.alfresco.service.cmr.security.OwnableService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowPath;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.cmr.workflow.WorkflowTaskQuery;
import org.alfresco.service.cmr.workflow.WorkflowTaskQuery.OrderBy;
import org.alfresco.service.cmr.workflow.WorkflowTaskState;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.repo.admin.constant.MainBudgetSrcConstant;
import pb.repo.admin.constant.MainUserGroupConstant;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.dao.MainBossDAO;
import pb.repo.admin.dao.MainBossEmotionDAO;
import pb.repo.admin.dao.MainWorkflowAssigneeDAO;
import pb.repo.admin.dao.MainWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowHistoryDAO;
import pb.repo.admin.dao.MainWorkflowNextActorDAO;
import pb.repo.admin.dao.MainWorkflowReviewerDAO;
import pb.repo.admin.exception.NotFoundApprovalMatrixException;
import pb.repo.admin.model.MainHrEmployeeModel;
import pb.repo.admin.model.MainUserGroupModel;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.admin.model.MainWorkflowNextActorModel;
import pb.repo.admin.model.MainWorkflowReviewerModel;
import pb.repo.admin.model.SubModuleModel;
import pb.repo.admin.util.MainUserGroupUtil;
import pb.repo.admin.util.MainUtil;
import pb.repo.admin.util.MainWorkflowUtil;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class MainWorkflowService {
	
	private static Logger log = Logger.getLogger(MainWorkflowService.class);
	
	@Autowired
	ServiceRegistry services;
	
	@Autowired
	AuthenticationService authService;

	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	FileFolderService fileFolderService;

	@Autowired
	ContentService contentService;
	
	@Autowired
	AdminMasterService adminMasterService;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	OwnableService ownableService;
	
	@Autowired
	TemplateService templateService;
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AdminHrEmployeeService adminHrEmployeeService;
	
	@Autowired
	AdminWkfConfigService adminWkfConfigService;
	
	SubModuleService moduleService;
	
	@Autowired
	AdminProjectService adminProjectService;

	@Autowired
	AdminAssetService adminAssetService;
	
	@Autowired
	AdminConstructionService adminConstructionService;
	
	private String MODEL_URI;
	private String WF_URI;
	private String WF_PREFIX;
	
	public void setModuleService(SubModuleService moduleService) {
		this.moduleService = moduleService;
		
		MODEL_URI = moduleService.getModelUri();
		WF_URI = moduleService.getWfUri();
		WF_PREFIX = moduleService.getModelPrefix();
	}
	
	public String startWorkflow(SqlSession session, SubModuleModel model, Map<String, String> bossMap) throws Exception {
		
		//Map<String, Object> map = moduleService.convertToMap(model);
		
		log.info("<- Start Workflow -> "+model.getId());
		
		String action = moduleService.getActionCaption(MainWorkflowConstant.TA_START,"");
		String actionTh = moduleService.getActionCaption(MainWorkflowConstant.TA_START,"th");
        String stateTask = getTaskCaption(MainWorkflowConstant.TN_PREPARER, "",null);
        String stateTaskTh = getTaskCaption(MainWorkflowConstant.TN_PREPARER, "th",null);
		String wfName = moduleService.getWorkflowName();
		log.info("  WF Name:"+wfName);
		WorkflowDefinition workflow = workflowService.getDefinitionByName("activiti$"+wfName);
		log.info("  workflow:"+workflow);
	
     	NodeRef workflowPackage = workflowService.createPackage(null);
     	
     	// Set to WorkFlow
        Map<QName, Serializable> parameters = new HashMap<QName, Serializable>();
        parameters.put(WorkflowModel.ASSOC_PACKAGE, workflowPackage);
        
        final NodeRef folderNodeRef = new NodeRef(model.getFolderRef());
        final String createdBy = model.getCreatedBy();
        		
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
		        permissionService.deletePermissions(folderNodeRef);
				permissionService.setInheritParentPermissions(folderNodeRef, false);
				ownableService.setOwner(folderNodeRef, "admin");
				if (createdBy!=null && !createdBy.equals("")) {
					permissionService.setPermission(folderNodeRef, createdBy, "SiteCollaborator", true);
				}
				
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
        
        setRequesterPermission(folderNodeRef, model);
        
        setRelatedUserPermission(folderNodeRef, model);
		
        setReviewer(session, parameters, model, folderNodeRef, bossMap);
        
        setNextActor(session, model, folderNodeRef);
        
        setSpecialUserPermission(folderNodeRef, model);
        
        setSpecialGroupPermission(folderNodeRef, model);
        
        MainWorkflowReviewerModel paramModel = new MainWorkflowReviewerModel();
        paramModel.setMasterId(model.getId());
        paramModel.setLevel(1);
        MainWorkflowReviewerModel reviewerModel = getReviewer(session, paramModel);
        if (reviewerModel != null) {
        	parameters.put(moduleService.getPropNextReviewers(), MainUserGroupUtil.codes2logins(reviewerModel.getReviewerUser()));
        } else {
        	log.error("ERR:No Reviewer Level 1 of "+model.getId());
        }
        
        model.setWaitingLevel(1);
        moduleService.setFirstTaskAssignee(parameters, model);
        log.info("waitingLevel:"+model.getWaitingLevel());
        
        log.info("Doc Ref : " + model.getDocRef());
        List<NodeRef> docList = new ArrayList<NodeRef>();
        docList.add(new NodeRef(model.getDocRef()));

        List<NodeRef> attachDocList = new ArrayList<NodeRef>();
        if(model.getListAttachDoc() !=null) {
        	for(String nodeRef : model.getListAttachDoc()) {
        		attachDocList.add(new NodeRef(nodeRef));
            }
        }
        
        moduleService.setWorkflowParameters(parameters, model, docList, attachDocList);
        moduleService.prepareModelForWfDesc(model,"th");
        
        parameters.put(WorkflowModel.PROP_WORKFLOW_DESCRIPTION, moduleService.getWorkflowDescription(model));
        parameters.put(moduleService.getPropDescEn(), moduleService.getWorkflowDescriptionEn(model));
        
        parameters.put(WorkflowModel.PROP_HIDDEN_TRANSITIONS, model.getCreatedBy());
        
        WorkflowPath workflowPath = workflowService.startWorkflow(workflow.getId(), parameters);
        log.info("Start Workflow Successfully");
        
        final String instanceId = workflowPath.getInstance().getId();
        final WorkflowTask startTask = AuthenticationUtil.runAs(new RunAsWork<WorkflowTask>()
	    {
			public WorkflowTask doWork() throws Exception
			{
		    	return workflowService.getStartTask(instanceId);
			}
	    }, AuthenticationUtil.getAdminUserName());
        
	    WorkflowTaskState taskState = WorkflowTaskState.IN_PROGRESS;
        
        WorkflowTaskQuery taskQuery = new WorkflowTaskQuery();
        taskQuery.setProcessId(instanceId);
        taskQuery.setTaskState(taskState);
        taskQuery.setActive(true);
        taskQuery.setOrderBy(new OrderBy[] { OrderBy.TaskDue_Asc });
        List<WorkflowTask> taskList = workflowService.queryTasks(taskQuery);
        
        WorkflowTask curTask = startTask;
        for(WorkflowTask task:taskList) {
        	log.info("task:"+task.getId());
        	curTask = task;
        }
        
        model.setWorkflowInsId(instanceId);
    	moduleService.update(session, model);
        
        Long wfKey = Long.valueOf(this.getKey());
        MainWorkflowModel workflowModel = new MainWorkflowModel();
        workflowModel.setId(wfKey);
        workflowModel.setMasterId(model.getId());
		workflowModel.setType(moduleService.getSubModuleType());
		workflowModel.setWorkflowInsId(instanceId);
		workflowModel.setTaskId(curTask.getId());
		workflowModel.setExecutionId("");
		workflowModel.setStatus(action);
		workflowModel.setStatusTh(actionTh);
		workflowModel.setBy(model.getCreatedBy());
		workflowModel.setCreatedBy(model.getCreatedBy());
		workflowModel.setAssignee(reviewerModel.getReviewerUser());
        
        addWorkflow(session, workflowModel);
        
        MainWorkflowHistoryModel workflowHistoryModel = moduleService.getReqByWorkflowHistory(model); // Extra History
        if (workflowHistoryModel != null) {
	        workflowHistoryModel.setMasterId(wfKey);
	        addWorkflowHistory(session, workflowHistoryModel);
        }
        
		workflowHistoryModel = new MainWorkflowHistoryModel();
        workflowHistoryModel.setMasterId(wfKey);
		workflowHistoryModel.setLevel(0);
		workflowHistoryModel.setBy(model.getCreatedBy());
		workflowHistoryModel.setAction(action);
		workflowHistoryModel.setActionTh(actionTh);
		workflowHistoryModel.setTask(stateTask);
		workflowHistoryModel.setTaskTh(stateTaskTh);
		workflowHistoryModel.setComment(moduleService.getFirstComment(model));
		workflowHistoryModel.setStatus(moduleService.getFirstStatus());
        addWorkflowHistory(session, workflowHistoryModel);
        
        
        workflowHistoryModel = moduleService.getAppByWorkflowHistory(model); // Extra History
        if (workflowHistoryModel != null) {
	        workflowHistoryModel.setMasterId(wfKey);
	        addWorkflowHistory(session, workflowHistoryModel);
        }
        
        log.info("Update Database Successfully");
        
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
				workflowService.endTask(startTask.getId(), null);
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
        log.info("End Start Task : "+startTask.getId());
        
		return instanceId;
	}
	
//	public String _startWorkflow(SubModuleModel model, String docType) throws Exception {
//		
//		//Map<String, Object> map = moduleService.convertToMap(model);
//		
//		log.info("<- Start Workflow -> "+model.getId());
//		
//		String instanceId = null;
//		
//		try {
//			String action = moduleService.getActionCaption(MainWorkflowConstant.TA_START,"");
//			String actionTh = moduleService.getActionCaption(MainWorkflowConstant.TA_START,"th");
//	        String stateTask = getTaskCaption(MainWorkflowConstant.TN_PREPARER, "",null);
//	        String stateTaskTh = getTaskCaption(MainWorkflowConstant.TN_PREPARER, "th",null);
//			String wfName = moduleService.getWorkflowName();
//			log.info("  WF Name:"+wfName);
//			WorkflowDefinition workflow = workflowService.getDefinitionByName("activiti$"+wfName);
//			log.info("  workflow:"+workflow);
//		
//	     	NodeRef workflowPackage = workflowService.createPackage(null);
//	     	
//	     	// Set to WorkFlow
//	        Map<QName, Serializable> parameters = new HashMap<QName, Serializable>();
//	        parameters.put(WorkflowModel.ASSOC_PACKAGE, workflowPackage);
//	        
//	        final NodeRef folderNodeRef = new NodeRef(model.getFolderRef());
//	        final String createdBy = model.getCreatedBy();
//	        		
//	        AuthenticationUtil.runAs(new RunAsWork<String>()
//		    {
//				public String doWork() throws Exception
//				{
//			        permissionService.deletePermissions(folderNodeRef);
//					permissionService.setInheritParentPermissions(folderNodeRef, false);
//					ownableService.setOwner(folderNodeRef, "admin");
//					if (createdBy!=null && !createdBy.equals("")) {
//						permissionService.setPermission(folderNodeRef, createdBy, "SiteCollaborator", true);
//					}
//					
//			    	return null;
//				}
//		    }, AuthenticationUtil.getAdminUserName());
//	        
//	        setRequesterPermission(folderNodeRef, model);
//	        
//	        setRelatedUserPermission(folderNodeRef, model);
//			
//	        setReviewer(session, parameters, model, folderNodeRef, docType);
//	        
//	        setNextActor(session, model, folderNodeRef);
//	        
//	        setSpecialUserPermission(folderNodeRef, model);
//	        
//	        setSpecialGroupPermission(folderNodeRef, model);
//	        
//	        MainWorkflowReviewerModel paramModel = new MainWorkflowReviewerModel();
//	        paramModel.setMasterId(model.getId());
//	        paramModel.setLevel(1);
//	        MainWorkflowReviewerModel reviewerModel = getReviewer(session, paramModel);
//	        if (reviewerModel != null) {
//	        	parameters.put(moduleService.getPropNextReviewers(), MainUserGroupUtil.codes2logins(reviewerModel.getReviewerUser()));
//	        } else {
//	        	log.error("ERR:No Reviewer Level 1 of "+model.getId());
//	        }
//	        
//	        model.setWaitingLevel(1);
//	        moduleService.setFirstTaskAssignee(parameters, model);
//	        log.info("waitingLevel:"+model.getWaitingLevel());
//	        
//	        log.info("Doc Ref : " + model.getDocRef());
//	        List<NodeRef> docList = new ArrayList<NodeRef>();
//	        docList.add(new NodeRef(model.getDocRef()));
//	
//	        List<NodeRef> attachDocList = new ArrayList<NodeRef>();
//	        if(model.getListAttachDoc() !=null) {
//	        	for(String nodeRef : model.getListAttachDoc()) {
//	        		attachDocList.add(new NodeRef(nodeRef));
//	            }
//	        }
//	        
//	        moduleService.setWorkflowParameters(parameters, model, docList, attachDocList);
//	        moduleService.prepareModelForWfDesc(model,"th");
//	        
//	        parameters.put(WorkflowModel.PROP_WORKFLOW_DESCRIPTION, moduleService.getWorkflowDescription(model));
//	        parameters.put(moduleService.getPropDescEn(), moduleService.getWorkflowDescriptionEn(model));
//	        
//	        WorkflowPath workflowPath = workflowService.startWorkflow(workflow.getId(), parameters);
//	        log.info("Start Workflow Successfully");
//	        
//	        instanceId = workflowPath.getInstance().getId();
//	        WorkflowTask startTask = workflowService.getStartTask(instanceId);
//	        
//		    WorkflowTaskState taskState = WorkflowTaskState.IN_PROGRESS;
//	        
//	        WorkflowTaskQuery taskQuery = new WorkflowTaskQuery();
//	        taskQuery.setProcessId(instanceId);
//	        taskQuery.setTaskState(taskState);
//	        taskQuery.setActive(true);
//	        taskQuery.setOrderBy(new OrderBy[] { OrderBy.TaskDue_Asc });
//	        List<WorkflowTask> taskList = workflowService.queryTasks(taskQuery);
//	        
//	        WorkflowTask curTask = workflowService.getStartTask(instanceId);
//	        for(WorkflowTask task:taskList) {
//	        	log.info("task:"+task.getId());
//	        	curTask = task;
//	        }
//	        
//	        model.setWorkflowInsId(instanceId);
////	    	moduleService.update(session, model);
//	        
//	        Long wfKey = Long.valueOf(this.getKey());
//	        MainWorkflowModel workflowModel = new MainWorkflowModel();
//	        workflowModel.setId(wfKey);
//	        workflowModel.setMasterId(model.getId());
//			workflowModel.setType(moduleService.getSubModuleType());
//			workflowModel.setWorkflowInsId(instanceId);
//			workflowModel.setTaskId(curTask.getId());
//			workflowModel.setExecutionId("");
//			workflowModel.setStatus(action);
//			workflowModel.setStatusTh(actionTh);
//			workflowModel.setBy(model.getCreatedBy());
//			workflowModel.setCreatedBy(model.getCreatedBy());
//			workflowModel.setAssignee(reviewerModel.getReviewerUser());
//	        
//	        addWorkflow(workflowModel);
//	        
//	        MainWorkflowHistoryModel workflowHistoryModel = moduleService.getReqByWorkflowHistory(model); // Extra History
//	        if (workflowHistoryModel != null) {
//		        workflowHistoryModel.setMasterId(wfKey);
////		        addWorkflowHistory(session, workflowHistoryModel);
//	        }
//	        
//			workflowHistoryModel = new MainWorkflowHistoryModel();
//	        workflowHistoryModel.setMasterId(wfKey);
//			workflowHistoryModel.setLevel(0);
//			workflowHistoryModel.setBy(model.getCreatedBy());
//			workflowHistoryModel.setAction(action);
//			workflowHistoryModel.setActionTh(actionTh);
//			workflowHistoryModel.setTask(stateTask);
//			workflowHistoryModel.setTaskTh(stateTaskTh);
//			workflowHistoryModel.setComment(moduleService.getFirstComment(model));
//			workflowHistoryModel.setStatus(moduleService.getFirstStatus());
////	        addWorkflowHistory(session, workflowHistoryModel);
//	        
//	        
//	        workflowHistoryModel = moduleService.getAppByWorkflowHistory(model); // Extra History
//	        if (workflowHistoryModel != null) {
//		        workflowHistoryModel.setMasterId(wfKey);
////		        addWorkflowHistory(session, workflowHistoryModel);
//	        }
//	        
//	        log.info("Update Database Successfully");
//	        
//	        workflowService.endTask(startTask.getId(), null);
//	        log.info("End Start Task : "+startTask.getId());
//        
//		} catch (Exception ex) {
//			log.error("", ex);
//			throw ex;
//		}
//
//		return instanceId;
//	}
	
	public void setFolderPermission(final NodeRef folderNodeRef, final String user) {
		
		// add permission to current user
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
				if (user!=null && !user.equals("")) {
					permissionService.setPermission(folderNodeRef, user, "SiteCollaborator", true);
				}
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
	}	
	
	private void setRequesterPermission(final NodeRef folderNodeRef, Object model) {
		
		// add permission to requester
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
				permissionService.setPermission(folderNodeRef, authService.getCurrentUserName(), "SiteCollaborator", true);
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
	}
	
	private void setRelatedUserPermission(final NodeRef folderNodeRef, Object model) {
		
		final List<String> list = moduleService.listRelatedUser((SubModuleModel)model); 
		if (list!=null) {
			// add permission to related user
	        AuthenticationUtil.runAs(new RunAsWork<String>()
		    {
				public String doWork() throws Exception
				{
					for(String u : list) {
						if (u!=null && !u.equals("")) {
							permissionService.setPermission(folderNodeRef, u, "SiteCollaborator", true);
						}
					}
			    	return null;
				}
		    }, AuthenticationUtil.getAdminUserName());
		}
	}	
	
    private void setRelatedAssignee(Map<QName, Serializable> parameters, String relatedUserGroups, final NodeRef folderNodeRef) throws Exception {
       	List<NodeRef> userList = new ArrayList<NodeRef>();
    	List<NodeRef> groupList = new ArrayList<NodeRef>();
    	
    	List<MainUserGroupModel> ugList = MainUserGroupUtil.convertJsonToUserGroupList(relatedUserGroups);
		for(MainUserGroupModel model : ugList){
			
			String type = model.getType();	
			String id = null;

			if (type.equals(MainUserGroupConstant.TYPE_USER)) {
				id = model.getId();
				NodeRef personNodeRef = personService.getPerson(id);
	            if(!userList.contains(personNodeRef)){
	            	userList.add(personNodeRef);
	            }
	            
			}
			else
			if (type.equals(MainUserGroupConstant.TYPE_GROUP)) {
				id = "GROUP_" + model.getId();
				NodeRef groupNodeRef = authorityService.getAuthorityNodeRef(id);
	            if(!groupList.contains(groupNodeRef)){
	            	groupList.add(groupNodeRef);
	            }
			}
	            
			final String fid = id;
	        AuthenticationUtil.runAs(new RunAsWork<String>()
    	    {
    			public String doWork() throws Exception
    			{
					if (fid!=null && !fid.equals("")) {
						permissionService.setPermission(folderNodeRef, fid, "SiteConsumer", true);
					}
    		    	return "A";
    			}
    	    }, AuthenticationUtil.getAdminUserName());
		}
		
    }

    private void setNextActor(final SqlSession session, final SubModuleModel model, final NodeRef folderNodeRef) throws Exception {
    	    		
	        AuthenticationUtil.runAs(new RunAsWork<String>()
    	    {
    			public String doWork() throws Exception
    			{
    				deleteNextActorByMasterId(session, model.getId());
    				    				
    		    	List<MainWorkflowNextActorModel> list = moduleService.listNextActor(model);
    		    	for(MainWorkflowNextActorModel actorModel : list) {
    		    		addNextActor(session, actorModel);

    		    		String u = MainUserGroupUtil.code2login(actorModel.getActorUser());
		        		if(u!=null && !u.equals(CommonConstant.DUMMY_EMPLOYEE_CODE) && !permissionService.getPermissions(folderNodeRef).contains(u)) {
							if (!u.equals("")) {
								permissionService.setPermission(folderNodeRef, u, "SiteCollaborator", true);
							}
		        		}
    		    	}

    	        	return "A";
    			}
    	    }, AuthenticationUtil.getAdminUserName());    		
    }
    
    public void setReviewer(SqlSession session, Map<QName, Serializable> parameters, final SubModuleModel model, final NodeRef folderNodeRef, Map<String, String> bossMap) throws Exception {
    	//Map<String, String> bossMap = getBossMap(docType, model);

//        	Map<String, String> bossMap = new LinkedHashMap<String, String>();
//        	bossMap.put("L01", "001509");
//        	bossMap.put("L02", "000511");
//        	bossMap.put("L03", "000090");
    	
		log.info("setReviewer :");
    	for(Entry e : bossMap.entrySet()) {
    		log.info("  - "+e.getKey()+":"+e.getValue());
    	}
    	
    	final List<String> permissionGroup = new ArrayList<String>();
    	final List<String> permissionUser = new ArrayList<String>();
    	//List<Double> percents = new ArrayList<Double>();
    	List<String> userCollectionList = null; 
    	
    	deleteReviewerByMasterId(session, model.getId());
    	MainWorkflowReviewerModel reviewerModel = null;
    	
    	Map<Integer,List<String>> lvl = new HashMap<Integer, List<String>>();
//        	for(MainApprovalMatrixDtlModel dtl : listDtl) {
    	int level = 1;
    	for(Entry e : bossMap.entrySet()) {
    		reviewerModel = new MainWorkflowReviewerModel();
    		userCollectionList = new ArrayList<String>();
    		String reviewerUser = (String)e.getValue();
    		String reviewerGroup = "";
    		
    		reviewerModel.setReviewerGroup(reviewerGroup);
    		reviewerModel.setReviewerUser(reviewerUser);
    		reviewerModel.setMasterId(model.getId());
    		reviewerModel.setLevel(level);
    		reviewerModel.setPercent(0.0);
    		reviewerModel.setRewarning(0);
    		reviewerModel.setCreatedBy(model.getCreatedBy());
    		addReviewer(session, reviewerModel);
    		
    		log.info("   user : "+reviewerUser);
    		
    		if(reviewerGroup!=null && !reviewerGroup.equalsIgnoreCase("")) {
    			
    			reviewerGroup = reviewerGroup.substring(1, reviewerGroup.length()-1);
    			
    			
    			for(String appGroup : reviewerGroup.split(",")) {
    				
    				if(!permissionGroup.contains(appGroup)) {
        				permissionGroup.add(appGroup);
        			}
    				
    				Set<String> authorities = authorityService.getContainedAuthorities(AuthorityType.USER, "GROUP_"+appGroup, false);
    				for(String u : authorities) {
    					if(!userCollectionList.contains(u)) {
    						userCollectionList.add(u);
    					}

    				}
    				
    			}

    		}
    		
    		if(reviewerUser!=null && !reviewerUser.equalsIgnoreCase("")) {
    			
    			//reviewerUser = reviewerUser.substring(1, reviewerUser.length()-1);
 
        		for(String appUser : reviewerUser.split(",")) {
           			if(!permissionUser.contains(appUser)) {
        				permissionUser.add(appUser);
        			}
           			if(!userCollectionList.contains(appUser)) {
           				userCollectionList.add(appUser);
           			}
    				
    			}
    			
    		}
    		log.info("user collection:"+userCollectionList.toString());
    		
    		lvl.put(level, userCollectionList);

    		level++;
    	}
    	
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
	        	for(String g : permissionGroup) {
	        		
	        		if(!permissionService.getPermissions(folderNodeRef).contains("GROUP_"+g)) {
	        			permissionService.setPermission(folderNodeRef, "GROUP_"+g, "SiteCollaborator", true);
	        		}
	        			
	        	}
	        	for(String u : permissionUser) {
	        		String uu = MainUserGroupUtil.code2login(u);
	        		if(!permissionService.getPermissions(folderNodeRef).contains(uu)) {
	        			permissionService.setPermission(folderNodeRef, uu, "SiteCollaborator", true);
	        		}
	        		
	        		if (moduleService.addPermissionToAttached()) {
	        			if (model.getListAttachDoc()!=null) {
			        		for(String attNodeRef : model.getListAttachDoc()) {
			        			NodeRef att = new NodeRef(attNodeRef);
			        			if(nodeService.exists(att)) {
				        			ChildAssociationRef parent = nodeService.getPrimaryParent(att);
				        			
					        		if(!permissionService.getPermissions(parent.getParentRef()).contains(uu)) {
					        			permissionService.setPermission(parent.getParentRef(), uu, "SiteCollaborator", true);
					        		}
			        			}
			        		}
	        			}
	        		}
	        		
        			if (model.getIsRefId()!=null && model.getIsRefId().equals(CommonConstant.V_ENABLE)) {
        				
        				SubModuleModel refModel = (SubModuleModel)moduleService.get(model.getRefId(), null);

	        			NodeRef refDocRef = new NodeRef(refModel.getDocRef());
	        			if(nodeService.exists(refDocRef)) {
		        			ChildAssociationRef parent = nodeService.getPrimaryParent(refDocRef);
		        			
			        		if(!permissionService.getPermissions(parent.getParentRef()).contains(uu)) {
			        			permissionService.setPermission(parent.getParentRef(), uu, "SiteConsumer", true);
			        		}
	        			}
        			}
	        		
	        	}
	        	return "A";
			}
	    }, AuthenticationUtil.getAdminUserName());
    	
    }

	public Long getKey() {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();;
		Long key = null;
		try {
			
            MainWorkflowDAO dao = session.getMapper(MainWorkflowDAO.class);
            
            key = dao.getKey();
            
        } finally {
        	session.close();
        }
		return key;
		
	}
	
	public void addWorkflow(SqlSession session, MainWorkflowModel mainWorkflowModel) throws Exception {
        MainWorkflowDAO dao = session.getMapper(MainWorkflowDAO.class);
        dao.add(mainWorkflowModel);
	}
	
	public void _addWorkflow(MainWorkflowModel mainWorkflowModel) {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			addWorkflow(session, mainWorkflowModel);
            
            session.commit();
            
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
		
	}
	
	public void update(SqlSession session, MainWorkflowModel workflowModel) {
		
        MainWorkflowDAO dao = session.getMapper(MainWorkflowDAO.class);
        
        workflowModel.setByTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        
        log.info("ext id:"+workflowModel.getExecutionId());
        
        dao.update(workflowModel);
	}
	
	public MainWorkflowModel getLastWorkflow(SqlSession session, MainWorkflowModel mainWorkflowModel) {
		
        MainWorkflowDAO dao = session.getMapper(MainWorkflowDAO.class);
            
        MainWorkflowModel model = dao.getLastWorkflow(mainWorkflowModel);
            
		return model;
	}
	
	public void addWorkflowHistory(SqlSession session, MainWorkflowHistoryModel mainWorkflowHistoryModel) {
            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
            
            mainWorkflowHistoryModel.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            
            dao.add(mainWorkflowHistoryModel);
	}
	
	public void _addWorkflowHistory(MainWorkflowHistoryModel mainWorkflowHistoryModel) {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
            
            mainWorkflowHistoryModel.setTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            
            dao.add(mainWorkflowHistoryModel);
            
            session.commit();
            
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public void updateWorkflowHistory(MainWorkflowHistoryModel workflowHistoryModel) {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
            
            dao.update(workflowHistoryModel);
            
            session.commit();
            
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
		
	}
	
	
	public List<MainWorkflowHistoryModel> listHistory(Map<String,Object> params) {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		List<MainWorkflowHistoryModel> list = new ArrayList<MainWorkflowHistoryModel>();
		
		try {
			
            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
            
            list =  dao.listHistory(params);
            
        } finally {
        	session.close();
        }
		
		return list;
		
	}
	

	public String updateWorkflow(SqlSession session, SubModuleModel model, String actionUserGroups, String relatedUserGroups, Map<String, String> bossMap) throws Exception {
		
		WorkflowTaskQuery query = new WorkflowTaskQuery();

		query.setTaskState(WorkflowTaskState.IN_PROGRESS);
		query.setProcessId(model.getWorkflowInsId());

		List<WorkflowTask> tasks = workflowService.queryTasks(query, true);
		final NodeRef folderNodeRef = new NodeRef(model.getFolderRef());

		if (tasks.size() > 0) {
		     Map<QName, Serializable> params = new HashMap<QName, Serializable>();
		     
//	         NodeRef folderNodeRef = new NodeRef(model.getFolderRef());
	        
	         //setRelatedAssignee(params, relatedUserGroups, folderNodeRef);
		     
	         setReviewer(session, params, model, folderNodeRef, bossMap);
	        
	         setNextActor(session, model, folderNodeRef);
	        
	         setSpecialUserPermission(folderNodeRef, model);
			
	         List<NodeRef> docList = new ArrayList<NodeRef>();
	         docList.add(new NodeRef(model.getDocRef()));
	
	         List<NodeRef> attachDocList = new ArrayList<NodeRef>();
	         if(model.getListAttachDoc()!=null) {
	         	for(String nodeRef : model.getListAttachDoc()) {
	         		attachDocList.add(new NodeRef(nodeRef));
	             }
	         }
	         
	         params.put(QName.createQName(MODEL_URI , "document"), (Serializable)docList);
	         
	         moduleService.prepareModelForWfDesc(model,"th");
	         
	         params.put(WorkflowModel.PROP_DESCRIPTION, moduleService.getWorkflowDescription(model));
	         params.put(moduleService.getPropDescEn(), moduleService.getWorkflowDescriptionEn(model));
	         
	         moduleService.setWorkflowParameters(params, model, docList, attachDocList);
	         
			 for (WorkflowTask task : tasks)
			 {
			     String taskId = task.getId();
				 log.info("update task.id="+taskId);
				 
			     workflowService.updateTask(taskId, params, null, null);
			 }

		}
		
		return null;
	}
	
	/*
	 * Backup 19/01/2016
	 * For Paknampo
	 */
	public String _updateWorkflow(SubModuleModel model, DelegateTask task) throws Exception {
		
		WorkflowTaskQuery query = new WorkflowTaskQuery();

		query.setTaskState(WorkflowTaskState.IN_PROGRESS);
		query.setProcessId(model.getWorkflowInsId());

		List<WorkflowTask> tasks = workflowService.queryTasks(query, true);

		if (tasks.size() > 0) {
		     Map<QName, Serializable> params = new HashMap<QName, Serializable>();
		     
	         NodeRef folderNodeRef = new NodeRef(model.getFolderRef());
	        
//	         setReviewer(session, params, model, folderNodeRef, null);
	         
//	         for(Entry<QName, Serializable> e : params.entrySet()) {
//	        	 log.info("params:"+e.getKey().toString());
//	         }
//		 
	         List<NodeRef> docList = new ArrayList<NodeRef>();
	         docList.add(new NodeRef(model.getDocRef()));
	
	         List<NodeRef> attachDocList = new ArrayList<NodeRef>();
	         if( model.getListAttachDoc()!=null) {
	         	for(String nodeRef : model.getListAttachDoc()) {
	         		attachDocList.add(new NodeRef(nodeRef));
	             }
	         }
	         
	         String desc = moduleService.getWorkflowDescription(model);
	         params.put(WorkflowModel.PROP_DESCRIPTION, desc);
	         
	         String descEn = moduleService.getWorkflowDescriptionEn(model);
	         params.put(moduleService.getPropDescEn(), descEn);
	         
			 for (WorkflowTask t : tasks)
			 {
			     String taskId = t.getId();
				 log.info("update task.id="+taskId);
				 
			     workflowService.updateTask(taskId, params, null, null);   
			 }
			 
			 // update delegate task
			 task.getExecution().setVariable(WorkflowModel.PROP_DESCRIPTION.getLocalName(), desc);
			 
        	 for(int i=1; i<=CommonConstant.MAX_APPROVER;i++) {
				 updateExecutionVariable(params, task, "reviewer"+i+"Group");
				 updateExecutionVariable(params, task, "reviewer"+i+"List");
				 
				 updateExecutionVariable(params, task, "reviewer"+i+"RequirePercentComplete");
        	 }			
		}
		
		return null;
	}
	
	/*
	 * For NBTC
	 */
	public String updateWorkflow(SubModuleModel model, DelegateTask task) throws Exception {
	     Map<QName, Serializable> params = new HashMap<QName, Serializable>();

//         List<NodeRef> docList = new ArrayList<NodeRef>();
//         docList.add(new NodeRef(model.getDocRef()));
//
//         List<NodeRef> attachDocList = new ArrayList<NodeRef>();
//         if( model.getListAttachDoc()!=null) {
//         	for(String nodeRef : model.getListAttachDoc()) {
//         		attachDocList.add(new NodeRef(nodeRef));
//             }
//         }
         
	     moduleService.prepareModelForWfDesc(model, "th");
         String desc = moduleService.getWorkflowDescription(model);
         params.put(WorkflowModel.PROP_DESCRIPTION, desc);
         
         String descEn = moduleService.getWorkflowDescriptionEn(model);
         params.put(moduleService.getPropDescEn(), descEn);

	     String taskId = task.getId();
	     workflowService.updateTask("activiti$"+taskId, params, null, null);   

		 // update delegate task
		 task.getExecution().setVariable("bpm_"+WorkflowModel.PROP_DESCRIPTION.getLocalName(), desc);
		 task.getExecution().setVariable("bpm_"+WorkflowModel.PROP_WORKFLOW_DESCRIPTION.getLocalName(), desc);
		 
//		 ActivitiScriptNodeList l = new ActivitiScriptNodeList();
//			log.info("pass 17.1 "+model.getDocRef()+","+services);
//		 ActivitiScriptNode n = new ActivitiScriptNode(new NodeRef(model.getDocRef()), services);
//			log.info("pass 17.2 "+n);
//		 l.add(n);
//			log.info("pass 17.3 "+WF_PREFIX);
//		 task.getExecution().setVariable(WF_PREFIX + "document", l);
//			log.info("pass 18");
		 
//		 for (int i=1; i<=PcmReqConstant.MAX_DUMMY_FIELD; i++) {
//			 updateExecutionVariable(params, task, "field"+i);
//		 }
		 

//    	 for(int i=1; i<=CommonConstant.MAX_APPROVER;i++) {
//			 updateExecutionVariable(params, task, "reviewer"+i+"Group");
//			 updateExecutionVariable(params, task, "reviewer"+i+"List");
//			 
//			 updateExecutionVariable(params, task, "reviewer"+i+"RequirePercentComplete");
//    	 }
		 
		return null;
	}
	
	public List<Map<String, Object>> listWorkflowPath(String id, String lang) {
		List<Map<String, Object>> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
            
            Map<String, Object> params = new HashMap<String, Object>();
            
            params.put("id", id);
            params.put("reviewer", getTaskCaption(MainWorkflowConstant.TN_REVIEWER, lang, null));

    		list = dao.listWorkflowPath(params);
            
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public JSONArray listAssignee(SqlSession session, String id, String lang) throws Exception {
		JSONArray jsArr = new JSONArray();

		MainWorkflowModel mainWorkflowModel = new MainWorkflowModel();
		mainWorkflowModel.setMasterId(id);
		mainWorkflowModel = getLastWorkflow(session, mainWorkflowModel);
		
		int index = 0;
		List<Map<String, Object>> list = moduleService.listWorkflowPath(id, lang);
		for (Map<String, Object> map : list) {
			jsArr.put(MainWorkflowUtil.createAssigneeGridModel(index++, (String)map.get("LEVEL"), (String)map.get("U") , (String)map.get("G"), (Boolean)map.get("IRA"), (String)map.get("C")));
		}

		return jsArr;
	}
	
	public JSONArray listTask(String id, String lang) throws Exception {
		JSONArray jsArr = new JSONArray();

		int index = 0;
		try {
			Object obj = moduleService.getForWfPath(id, null);
			Map<String, Object> model = null;
			if (obj instanceof Map) {
				model = (Map<String,Object>)obj;
			} else {
				SubModuleModel subModuleModel = (SubModuleModel)obj;
				
				model = new HashMap<String,Object>();
				model.put("id", subModuleModel.getId());
				model.put("status", subModuleModel.getStatus());
				model.put("workflow_ins_id", subModuleModel.getWorkflowInsId());
				model.put("pay_type", subModuleModel.getId());
			}
			 
			final String workflowInsId = (String)model.get("workflow_ins_id");
			
			lang = (lang!=null && lang.startsWith("th") ? "_th" : "");
			
			log.info("workflowInsId:" + workflowInsId);
			if (workflowInsId!=null) {
				final List<WorkflowTask> tasks = AuthenticationUtil.runAs(new RunAsWork<List<WorkflowTask>>()
					    {
							public List<WorkflowTask> doWork() throws Exception
							{
								WorkflowTaskQuery workflowTaskQuery = new WorkflowTaskQuery();
						    	workflowTaskQuery.setActive(true);
						    	workflowTaskQuery.setTaskState(WorkflowTaskState.IN_PROGRESS);
						    	workflowTaskQuery.setProcessId(workflowInsId);
						    	
								return workflowService.queryTasks(workflowTaskQuery);
							}
					    }, AuthenticationUtil.getAdminUserName());
				
				String taskJson = "";
				for(WorkflowTask task : tasks) {
					log.info("task:"+task.getId());
					if (taskJson.length() > 0) {
						taskJson += ",";
					}
					
					QName q1 = null;
					QName q1_1 = null;
					QName q2 = null;
					for(QName key : task.getProperties().keySet()) {
	//					log.info("    "+key.toString()+":"+task.getProperties().get(key));
						
						if (key.toString().contains("}owner")) {
							q1 = key;
						}
						else
						if (key.toString().contains("}nextActionGroupAssignee")) {
							q1_1 = key;
						}
						else
						if (key.toString().contains("}status")) {
							q2 = key;
						}
					}
					
					String assignedTo = "";
					if(task.getProperties().get(q1) == null) {
						List<NodeRef> list = (List<NodeRef>)task.getProperties().get(q1_1);
						for(NodeRef nodeRef : list) {
							if (assignedTo.length() > 0) {
								assignedTo += ",";
							}
							Map<QName, Serializable> props = nodeService.getProperties(nodeRef);
							for(QName key : props.keySet()) {
	//							log.info("  prop : "+key.toString()+":"+props.get(key));
								if (key.toString().contains("}authorityDisplayName")) {
									assignedTo += props.get(key);
									break;
								}
							}
						}
					}
					else {
						assignedTo = task.getProperties().get(q1).toString();
					}
					log.info("assignedTo:"+assignedTo+", taskTitle:"+task.getTitle());
					
					Map<String, Object> empModel = adminHrEmployeeService.getWithDtl(assignedTo, null);
					
					jsArr.put(createTaskGridModel(index++, getTaskTitle(task.getTitle(),lang), assignedTo + " - " + (empModel!=null ? empModel.get("first_name"+lang) : ""), (String)task.getProperties().get(q2)));
				}
			}
			
			if (jsArr.length()==0) {
				jsArr.put(createTaskGridModel(index++, moduleService.getNextActionInfo(model, lang), "", ""));
			}
			
		 } catch (Exception ex) {
			log.error("", ex);
		 }

		return jsArr;
	}
	
	private JSONObject createTaskGridModel(int id, String type, String assignedTo, String status)  throws Exception {
		JSONObject jsObj = new JSONObject();
		
		jsObj.put("id", id);
		jsObj.put("type", type);
		jsObj.put("assignedTo", MainUtil.trimComma(assignedTo));
		jsObj.put("status", status);
		
		return jsObj;
	}
	
	public JSONArray listDetail(String id, String lang) throws Exception {
		JSONArray jsArr = new JSONArray();

		int index = 0;
		List<MainWorkflowModel> list = listWorkflowById(id);
		
		if (list!=null && list.size()>0) {
			List<Long> workflowIds = new ArrayList<Long>();
			for(MainWorkflowModel wfModel : list) {
				workflowIds.add(wfModel.getId());
			}
			  
			List<MainWorkflowHistoryModel> hList = listWorkflowHistory(workflowIds);
			
	//
	//		if (hList.isEmpty()) {
	//			for (MainWorkflowModel wfModel : list) {
	//				hList = listWorkflowHistory(wfModel.getTaskId());
	//				break;
	//			}
	//			Collections.reverse(hList);
	//		}
			lang = (lang!=null && lang.startsWith("th") ? "_th" : "");
			Boolean showTh = (lang!=null && lang.indexOf("th")>=0);
			
			if (hList!=null && hList.size()>0) {
				for (MainWorkflowHistoryModel model : hList) {
					Map<String, Object> empModel = adminHrEmployeeService.getWithDtl(model.getBy(), null);
		
					jsArr.put(createDetailGridModel(index++, model.getTime(), empModel!=null ? (String)empModel.get("first_name"+lang) : model.getBy(), showTh ? model.getActionTh() : model.getAction(), showTh ? model.getTaskTh() : model.getTask(), StringUtils.defaultIfBlank(model.getComment(),"").replace("\\n", "<br>").replace("'", "\\'")));
				}
			} else {
				log.info("Workflow History not found : "+id+","+workflowIds);
			}
		} else {
			log.info("Workflow Data not found : "+id);
		}

		return jsArr;
	}
	
	private JSONObject createDetailGridModel(int id, Timestamp time, String by, String status, String task, String comment)  throws Exception {
		JSONObject jsObj = new JSONObject();
		
		jsObj.put("id", id);
		jsObj.put("time", CommonDateTimeUtil.convertToGridDateTime(time));
		jsObj.put("by", by);
		jsObj.put("status", status);
		jsObj.put("task", task);
		jsObj.put("comment", comment);
		
		return jsObj;
	}
	
	public List<MainWorkflowModel> listWorkflowById(String id) {
		List<MainWorkflowModel> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowDAO dao = session.getMapper(MainWorkflowDAO.class);
            
    		list = dao.listByMasterId(id);
            
        } finally {
        	session.close();
        }
		
		return list;	
	}
	
	public List<MainWorkflowHistoryModel> listWorkflowHistory(List<Long> workflowIds) {
		List<MainWorkflowHistoryModel> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowHistoryDAO dao = session.getMapper(MainWorkflowHistoryDAO.class);
            
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("ids", workflowIds);
    		
    		list = dao.listByMasterId(params);
            
        } finally {
        	session.close();
        }
		
		return list;
	}
	
//	public List<MainWorkflowHistoryModel> listWorkflowHistory(String taskId) {
//
//		List<MainWorkflowHistoryModel> list = new ArrayList<MainWorkflowHistoryModel>();
//		try {
//			WorkflowTask task = workflowService.getTaskById(taskId);
//			String historyStr = (String)task.getProperties().get("pcmwf_commentHistory");
//			
//			JSONArray history = new JSONArray("["+historyStr.replaceAll("/", "")+"]");
//			 
//			log.info("history.length="+history.length());
//			
//			for(int i=0; i < history.length(); i++) {
//				 JSONObject h = history.getJSONObject(i);
//				 
//				 list.add(new MainWorkflowHistoryModel(h));
//			}
//		} catch (Exception ex) {
//			log.error("", ex);
//		}
//		
//		return list;
//	}
	
	private Object updateExecutionVariable(Map<QName, Serializable> params, DelegateTask task, String varName) {
		Object obj = ObjectUtils.defaultIfNull(params.get(QName.createQName(WF_URI + varName)), "");
		
		task.getExecution().setVariable(WF_PREFIX+varName, obj);
		
		return obj;
	}
	
	public void addReviewer(SqlSession session, MainWorkflowReviewerModel reviewerModel) throws Exception {
        MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
		dao.add(reviewerModel);
	}
	
	public void _addReviewer(MainWorkflowReviewerModel reviewerModel) throws Exception {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
           
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);

    		dao.add(reviewerModel);
            
            session.commit();
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public void addNextActor(SqlSession session, MainWorkflowNextActorModel actorModel) throws Exception {
        MainWorkflowNextActorDAO dao = session.getMapper(MainWorkflowNextActorDAO.class);
		dao.add(actorModel);
	}
	
	public void _addNextActor(MainWorkflowNextActorModel actorModel) throws Exception {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
           
            MainWorkflowNextActorDAO dao = session.getMapper(MainWorkflowNextActorDAO.class);

    		dao.add(actorModel);
            
            session.commit();
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public MainWorkflowReviewerModel getReviewer(SqlSession session, MainWorkflowReviewerModel paramModel) throws Exception {
		
		MainWorkflowReviewerModel model = null;
		
        MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);

		List<MainWorkflowReviewerModel> list = dao.listByLevel(paramModel);
		if (list.size()>0) {
			model = list.get(0);
		}
        
        return model;
	}		
	
	public void deleteReviewerByMasterId(SqlSession session, String id) throws Exception {
        MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
        dao.deleteByMasterId(id);
	}
	
	public void _deleteReviewerByMasterId(String id) throws Exception {
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
            dao.deleteByMasterId(id);
            session.commit();
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public void deleteNextActorByMasterId(SqlSession session, String id) throws Exception {
        MainWorkflowNextActorDAO dao = session.getMapper(MainWorkflowNextActorDAO.class);
        dao.deleteByMasterId(id);
	}

	public void deleteNextActorByMasterId(String id) throws Exception {
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainWorkflowNextActorDAO dao = session.getMapper(MainWorkflowNextActorDAO.class);
            dao.deleteByMasterId(id);
            session.commit();
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public Integer getLastReviewerLevel(String id) throws Exception {
		Integer lastLevel = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
            lastLevel = dao.getLastLevel(id);
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
        
        return lastLevel;
	}
	
	public String saveWorkflowHistory(SqlSession session, DelegateExecution execution, String user, String stateTask, String taskComment, String action, DelegateTask task, String id, Integer level, String status) throws Exception {
		
		Timestamp now = CommonDateTimeUtil.now();
		String actionTh = moduleService.getActionCaption(action, "th");
		action = moduleService.getActionCaption(action, "");
		
		String stateTaskTh = getTaskCaption(stateTask, "th", level);
		stateTask = getTaskCaption(stateTask, "", level);
//		if (execution!=null) {
//		
//			String commentHistoryAttr = WF_PREFIX+"commentHistory";
//	
//			String allComment = execution.getVariable(commentHistoryAttr)!= null?
//						 execution.getVariable(commentHistoryAttr).toString():"";
//			
//			if(!allComment.equals("")){
//				allComment += ",";
//			}
//			
//			SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM yyyy HH:mm", Locale.ENGLISH);
//			String timeAction = formatter.format(now);
//			
//			// Build JSON string
//			String currentComment = "";
//			
//			currentComment +="{";
//			currentComment += "\"time\":\""+timeAction+"\",";
//			currentComment += "\"user\":\""+user+"\",";
//			currentComment += "\"action\":\""+action+"\",";
//			currentComment += "\"task\":\""+stateTask+"\",";
//			currentComment += "\"comment\":\""+org.json.simple.JSONObject.escape(taskComment)+"\" ";
//			currentComment += "}";
//			
//			execution.setVariable(commentHistoryAttr, allComment + currentComment);
//			if (task!=null) {
//				task.setVariable(commentHistoryAttr, allComment + currentComment);
//			}
//		}
		
		MainWorkflowModel workflowModel = new MainWorkflowModel();
		workflowModel.setMasterId(id.toString());
		workflowModel = getLastWorkflow(session, workflowModel);
		if (workflowModel!=null) {
			MainWorkflowHistoryModel workflowHistoryModel = new MainWorkflowHistoryModel();
			workflowHistoryModel.setTime(now);
			workflowHistoryModel.setBy(user);
			workflowHistoryModel.setAction(action);
			workflowHistoryModel.setActionTh(actionTh);
			workflowHistoryModel.setTask(stateTask);
			workflowHistoryModel.setTaskTh(stateTaskTh);
			workflowHistoryModel.setComment((taskComment!=null && !taskComment.equalsIgnoreCase(""))?taskComment:null);
			workflowHistoryModel.setMasterId(workflowModel.getId());
			workflowHistoryModel.setLevel(level);
			workflowHistoryModel.setStatus(status);
			addWorkflowHistory(session, workflowHistoryModel);
			
			workflowModel.setStatus(action);
			workflowModel.setStatusTh(actionTh);
			workflowModel.setBy(user);
			workflowModel.setByTime(workflowHistoryModel.getTime());
//			workflowModel.setExecutionId(execution.getId());
			update(session, workflowModel);	
		} else {
			log.info("Not Found WorkflowModel");
		}
		log.info("saveHistory finish");

		return action;
	}
	
	/*
	 * For Interface
	 */
	public void saveWorkflowHistory(SqlSession session, String id, String by, String task, String taskTh, String action, String actionTh, String comment) throws Exception {
		
		Timestamp now = CommonDateTimeUtil.now();
		
		MainWorkflowModel workflowModel = new MainWorkflowModel();
		workflowModel.setMasterId(id.toString());
		workflowModel = getLastWorkflow(session, workflowModel);
		if (workflowModel!=null) {
			MainWorkflowHistoryModel workflowHistoryModel = new MainWorkflowHistoryModel();
			workflowHistoryModel.setTime(now);
			workflowHistoryModel.setBy(by);
			workflowHistoryModel.setAction(action);
			workflowHistoryModel.setActionTh(actionTh);
			workflowHistoryModel.setTask(task);
			workflowHistoryModel.setTaskTh(taskTh);
			workflowHistoryModel.setComment((comment!=null && !comment.equalsIgnoreCase(""))?comment:null);
			workflowHistoryModel.setMasterId(workflowModel.getId());
			workflowHistoryModel.setLevel(0);
			workflowHistoryModel.setStatus("");
			addWorkflowHistory(session, workflowHistoryModel);
			
			workflowModel.setStatus(action);
			workflowModel.setStatusTh(actionTh);
			workflowModel.setBy(by);
			workflowModel.setByTime(workflowHistoryModel.getTime());
			update(session, workflowModel);	
		} else {
			log.info("Not Found WorkflowModel");
		}
		log.info("saveHistory finish");
	}		

	public Object updateExecutionEntity(ExecutionEntity executionEntity, DelegateTask task, String varName) throws Exception {
		Object obj = ObjectUtils.defaultIfNull(task.getVariable(WF_PREFIX+varName), "");
		
		executionEntity.setVariable(WF_PREFIX+varName, obj);
		
		return obj;
	}
	
	public String getWorkflowStatus(String WF_PREFIX, ExecutionEntity executionEntity, String taskKey, Integer level) {
		/*
		 * Status
		 */
		String varName = WF_PREFIX+"workflowStatus";
		String workflowStatus = (String)executionEntity.getVariable(varName);

		log.info("  workflowStatus:" + workflowStatus);
		
		if(workflowStatus == null || !(workflowStatus.toUpperCase().equals("REJECT") || workflowStatus.toUpperCase().equals("CONSULT"))){
			workflowStatus = "WAPPR";
			
			//Check current task is in taskHistory?
			log.info("  Check Task History");
			String taskHistory = (String)executionEntity.getVariable(WF_PREFIX+"taskHistory");
			log.info("  taskHistory:" + taskHistory);
			String finalTaskKey = MainWorkflowUtil.getFinalTaskKey(taskKey, level);
			if(taskHistory.contains("|" + finalTaskKey)){
				log.info("  Found Task in History -> RESUBMIT");
				workflowStatus = "RESUBMIT";
			}
		}
		
//		for(String n : task.getVariableNames()) {
//			Object obj = task.getVariable(n);
//			log.info(n+":"+obj+":+"+((obj!=null) ? obj.getClass().getName() : ""));
//		}
		
		return workflowStatus.toUpperCase();
	}
	
	
	private void setSpecialUserPermission(final NodeRef folderNodeRef, final Object model) {
		
		// add permission to special user
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
				List<String> list = moduleService.getSpecialUserForAddPermission((SubModuleModel)model);
				if (list!=null) {
					for(String s : list) {
						if (s!=null && !s.equals("")) {
							permissionService.setPermission(folderNodeRef, s, "SiteCollaborator", true);
						}
					}
				}
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
	}
	
	private void setSpecialGroupPermission(final NodeRef folderNodeRef, final Object model) {
		
		// add permission to special group
        AuthenticationUtil.runAs(new RunAsWork<String>()
	    {
			public String doWork() throws Exception
			{
				List<String> list = moduleService.getSpecialGroupForAddPermission();
				if (list!=null) {
					for(String s : list) {
						permissionService.setPermission(folderNodeRef, "GROUP_"+s, "SiteCollaborator", true);
					}
				}
		    	return null;
			}
	    }, AuthenticationUtil.getAdminUserName());
	}
	
	public String getTaskCaption(String task, String lang, Integer level) {
		StringBuffer sb = new StringBuffer();

		if (lang!=null && lang.indexOf("th")>=0) {
			sb.append(MainWorkflowConstant.WF_TASK_NAMES_TH.get(task));
		} else {
			sb.append(MainWorkflowConstant.WF_TASK_NAMES.get(task));
		}
		
		if (task.equals(MainWorkflowConstant.TN_REVIEWER) && level!=null) {
			sb.append(" "+level);
		}
		
		return sb.toString();
		
	}
	
	public String getTaskTitle(String task, String lang) {
		String key = task;
		Integer level = null;
		
		if (task.indexOf(MainWorkflowConstant.TN_REVIEWER)>=0 || task.indexOf(MainWorkflowConstant.WF_TASK_NAMES.get(MainWorkflowConstant.TN_REVIEWER))>=0) {
			int pos = task.indexOf(" ");
			key = MainWorkflowConstant.TN_REVIEWER;
			level = Integer.parseInt(task.substring(pos+1));
		}
		
		return getTaskCaption(key, lang, level);
	}
	
	public void addWorkflowAssignee(Map<String, Object> map) {
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowAssigneeDAO dao = session.getMapper(MainWorkflowAssigneeDAO.class);

            Long cnt = dao.count(map);

            if(cnt==0) {
            	dao.add(map);
            }
            
            session.commit();
            
        } catch (Exception ex) {
			log.error("", ex);
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public Map<String, String> getAssigneeMap(String wkId) {
		
		Map<String, String> map = new HashMap<String,String>();
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowAssigneeDAO dao = session.getMapper(MainWorkflowAssigneeDAO.class);
            
            Map<String,Object> params = new HashMap<String, Object>();
            
            params.put("wkId", wkId);
            params.put("active", "1");
            
            params.put("orderBy", "created_time");
            
            List<Map<String, Object>> list = dao.listByWorkflowId(params);
            for(Map<String,Object> assignee:list) {
            	map.put((String)assignee.get("src_user"), (String)assignee.get("dest_user"));
            	for(Entry<String, String> e:map.entrySet()) {
            		if (e.getValue().equals(assignee.get("src_user"))) {
            			e.setValue((String)assignee.get("dest_user"));
            		}
            	}
            }
            
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
	public void replaceReviewer(SqlSession session, String id) throws Exception {
		log.info("replaceReviewer("+id+")");
		
		MainWorkflowModel wfModel = new MainWorkflowModel();
		wfModel.setMasterId(id);
		wfModel = getLastWorkflow(session, wfModel);
		
		log.info("  wfModel:"+wfModel);
		
		List<Map<String, Object>> revList = listReviewer(id);
		
		log.info("  revList:"+revList);
		
		Map<String, String> amap = getAssigneeMap(wfModel.getWorkflowInsId());
		
		log.info("  amap:"+amap);
		
		if (amap!=null && amap.size()>0) {
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			for(Map<String,Object> m:revList) {
				log.info("  id:"+m.get("id"));
				log.info("  assignee:"+m.get("reviewer_user"));
				String newAssignee = amap.get(m.get("reviewer_user"));
				log.info("  newAssignee:"+newAssignee);
				if (newAssignee!=null) {
					log.info("  update db");
					// update db
					params.put("id", m.get("id"));
					params.put("reviewer_user", newAssignee);
					
					updateReviewer(params);
				}
			}
		}
	}
	
	public List<Map<String, Object>> listAssignee(Map<String,Object> params) {
		
		List<Map<String, Object>> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
		
		try {
			
            MainWorkflowAssigneeDAO dao = session.getMapper(MainWorkflowAssigneeDAO.class);
            
            list = dao.listByWorkflowId(params);
            
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public void updateAssignee(Map<String, Object> params) {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainWorkflowAssigneeDAO dao = session.getMapper(MainWorkflowAssigneeDAO.class);
            
           	dao.update(params);
            
            session.commit();
        } catch (Exception ex) {
        	log.info(ex.toString());
        	session.rollback();
        } finally {
        	session.close();
        }

	}
	
	public List<Map<String, Object>> listReviewer(String id) {
		List<Map<String, Object>> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
            
            Map<String, Object> params = new HashMap<String, Object>();
            
            params.put("id", id);

    		list = dao.list(params);
            
        } catch (Exception ex) {
			log.error("", ex);
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public void updateReviewer(Map<String, Object> params) {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainWorkflowReviewerDAO dao = session.getMapper(MainWorkflowReviewerDAO.class);
            
           	dao.update(params);
            
            session.commit();
        } catch (Exception ex) {
        	session.rollback();
        } finally {
        	session.close();
        }

	}
	
	public Map<String, String> getBossMap(String docType, SubModuleModel subModuleModel) throws Exception {
		log.info("getBossMap()");

		Map<String,Object> map = moduleService.getWorkflowPathParamters(subModuleModel);
		map.put(MainWorkflowConstant.WPP_DOC_TYPE,docType);
		
		/*
		 * Search Original Boss List
		 */
		Map<String, String> bossMap = new LinkedHashMap<String, String>();

		log.info("  "+docType
				+","+map.get(MainWorkflowConstant.WPP_BGT_SRC_TYPE)
				+","+map.get(MainWorkflowConstant.WPP_BGT_SRC)
				+","+map.get(MainWorkflowConstant.WPP_REQ_BY)
		);

		String bgtSrcType = (String)map.get(MainWorkflowConstant.WPP_BGT_SRC_TYPE);
		Integer bgtSrc = (Integer)map.get(MainWorkflowConstant.WPP_BGT_SRC);
		String reqBy = (String)map.get(MainWorkflowConstant.WPP_REQ_BY);
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	List<Map<String, Object>> bossList = null;
        	
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("docType", docType);
            if (bgtSrcType.equals(MainBudgetSrcConstant.TYPE_PROJECT)) {
        		// find project manager section id
        		Map<String,Object> proj = adminProjectService.get(bgtSrc);
        		String pmCode = (String)proj.get("PM_CODE");
        		log.info("  pm:"+pmCode);
        		bossMap.put("00", pmCode);
        		
        		MainHrEmployeeModel emp = adminHrEmployeeService.get(pmCode);
        		params.put("sectionId", emp.getSectionId());
        		
        		map.put(MainWorkflowConstant.WPP_TOTAL, moduleService.getWorkflowPathParamterTotalForProject(subModuleModel));
        		
                for(Entry<String,Object> p:params.entrySet()) {
                	log.info("-"+p.getKey()+":"+p.getValue());
                }
                
        		MainBossDAO dao = session.getMapper(MainBossDAO.class);
        		bossList = dao.list(params);
        	} else {
	            if (bgtSrcType.equals(MainBudgetSrcConstant.TYPE_UNIT)) {
	            	params.put("sectionId", bgtSrc);
	        		map.put(MainWorkflowConstant.WPP_TOTAL, moduleService.getWorkflowPathParamterTotalForSection(subModuleModel));
	        	} else
	            if (bgtSrcType.equals(MainBudgetSrcConstant.TYPE_ASSET)) {
	    			Map<String,Object> assetMap = adminAssetService.get(bgtSrc);
	            	params.put("sectionId", assetMap.get("section_id"));
	        		map.put(MainWorkflowConstant.WPP_TOTAL, moduleService.getWorkflowPathParamterTotalForSection(subModuleModel));
	        	} else
	            if (bgtSrcType.equals(MainBudgetSrcConstant.TYPE_CONSTRUCTION)) {
	    			Map<String,Object> conMap = adminConstructionService.get(bgtSrc);
	            	params.put("sectionId", conMap.get("section_id"));
	        		map.put(MainWorkflowConstant.WPP_TOTAL, moduleService.getWorkflowPathParamterTotalForSection(subModuleModel));
	        	}

	            for(Entry<String,Object> p:params.entrySet()) {
	            	log.info("-"+p.getKey()+":"+p.getValue());
	            }

	            Integer emotion = map.get(MainWorkflowConstant.WPP_EMOTION)!=null ? (Integer)map.get(MainWorkflowConstant.WPP_EMOTION) : null;
        		log.info("  emotion:"+emotion);
            	if (emotion!=null && emotion==1) {
            		MainBossEmotionDAO dao = session.getMapper(MainBossEmotionDAO.class);
                	bossList = dao.list(params);
            	} else {
                	MainBossDAO dao = session.getMapper(MainBossDAO.class);
                	bossList = dao.list(params);
            	}
        	}
        	log.info("  bossList:"+bossList);
        	if (bossList==null || bossList.size()==0) {
        		throw new NotFoundApprovalMatrixException();
        	}
        	
        	log.info("  total:"+map.get(MainWorkflowConstant.WPP_TOTAL));
        	
        	if (bgtSrcType.equals(MainBudgetSrcConstant.TYPE_PROJECT)) {
        		bossMap = getProjectBossMap(map, bossList, bossMap);
        	}
        	else {
        		bossMap = getUnitBossMap(map, bossList, bossMap);
        	}
        	
        	/*
        	 * Remove Requester
        	 */
    		List<String> rmList = new ArrayList<String>();
    		
    		for(Entry<String, String> boss : bossMap.entrySet()) {
    			if (boss.getValue().equals(reqBy)) {
    				rmList.add(boss.getKey());
    			}
    		}

        	for(String boss:rmList) {
        		bossMap.remove(boss);
        	}
        	
            log.info("  bossMap:"+bossMap);
            
        } catch (Exception ex) {
        	log.error(ex);
        	bossMap = null;
        } finally {
        	session.close();
        }
		
		return bossMap;
	}
	
	public Map<String, String> getProjectBossMap(Map<String, Object> map, List<Map<String, Object>> bossList,Map<String, String> tmpMap) throws Exception {
		
    	Double total = (Double)map.get(MainWorkflowConstant.WPP_TOTAL);
    	
    	String reqBy = (String)map.get(MainWorkflowConstant.WPP_REQ_BY);

    	/*
    	 * Case PM get special budget
    	 */
		String pm = tmpMap.get("00");
    	
    	if (pm!=null) {
			List<Map<String, Object>> list = adminProjectService.listPMSpecialBudget((Integer)map.get(MainWorkflowConstant.WPP_BGT_SRC), (String)map.get(MainWorkflowConstant.WPP_DOC_TYPE));
			if (list!=null && list.size()>0) {
				Map<String, Object> firstMap = list.get(0);
				Double specialBudget = (Double)firstMap.get("amount_max");
				log.info("  spcBgt:"+specialBudget);
				if (total <= specialBudget) {
					log.info("    >= total");
					if (!pm.equals((String)map.get(MainWorkflowConstant.WPP_REQ_BY))) {
						log.info("    pm != requester("+reqBy+")");
						return tmpMap;
					} else {
						log.info("    pm = requester("+reqBy+")");
					}
				} else {
					log.info("    < total");
//					List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>();
//			    	for(Map<String, Object> boss : bossList) {
//			    		log.info("  "+boss.get("lvl")+" "+boss.get("employee_code")+" (amt:"+boss.get("amount_max")+")");
//			    		if (specialBudget < (Double)boss.get("amount_max")) {
//				    		log.info("  added");
//			    			tmpList.add(boss);
//			    		}
//			    	}
//		    		bossList = tmpList;
				}
			} else {
				log.info("  ** no spcBgt");
			}
    	}
    	
		/*
		 * Other cases
		 */
    	
    	/*
    	 * Find Boss by level, if reqBy exist in workflow path
    	 */
    	int start = 0;
    	int i = 0;
    	
    	int len = bossList.size();
    	if (len>0) {
	    	for(int j=len-1;j>=0;j--) {
	    		Map<String, Object> boss = bossList.get(j);
	    		
	    		log.info("  "+boss.get("lvl")+" "+boss.get("employee_code"));
	    		
	    		if (boss.get("employee_code").equals(pm)) {
	    			log.info("  "+pm+" in path");
	    			start = j;
	    			break;
	    		}
	    	}
    	}
    	
    	Map<String, Object> reservedBoss = null;
    	Map<String, Object> lastBossMap = new HashMap<String, Object>();
    	lastBossMap.put("amount_max", (Double)0.0);
    	i = 0;
    	log.info("  start:"+start);
    	for(Map<String, Object> boss : bossList) {
    		
    		log.info("  "+boss.get("lvl")+" "+boss.get("employee_code")+" (amt:"+boss.get("amount_max")+")");
    		
    		if (i>=start) {
    			if (total > (Double)lastBossMap.get("amount_max")) {
    				log.info("   add");
    				tmpMap.put((String)boss.get("lvl"), (String)boss.get("employee_code"));
    				lastBossMap = boss;
    			}
    			else
    			{
    				log.info("   reserve");
    				reservedBoss = boss;
    				break;
    			}
    		}
    		i++;
    	}
    	
    	log.info("  lastBoss:"+lastBossMap.get("employee_code"));
    	
    	/*
    	 * Replace Last Boss if he is Requester
    	 */
    	if (lastBossMap.get("employee_code").equals(reqBy)) {
    		log.info("    is requester");
    		tmpMap.remove(lastBossMap.get("lvl"));
    		if (reservedBoss!=null) {
        		log.info("    replace lastBoss with reservedBoss:"+reservedBoss);
    			tmpMap.put((String)reservedBoss.get("lvl"), MainUserGroupUtil.code2login((String)reservedBoss.get("employee_code")));
    		} else {
        		log.info("    no reservedBoss");
    		}
    	}
    	
		/*
         * Remove duplicated Boss
         */
		tmpMap = removeDuplicatedBoss(tmpMap);
 
		return tmpMap;
	}
	
	public Map<String, String> removeDuplicatedBoss(Map<String, String> tmpMap) {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		Entry<String, String> prevEntry = null;
		for(Entry<String, String> e : tmpMap.entrySet()) {
			
//			log.info("  "+((prevEntry!=null) ? prevEntry.getValue() : "null")+":"+e.getValue());
			
			if (prevEntry==null || !e.getValue().equals(prevEntry.getValue())) {
				map.put(e.getKey(), e.getValue());
			}
			
			prevEntry = e;
		}
		
//		if (prevEntry != null) {
//			map.put((String)prevEntry.getKey(), (String)prevEntry.getValue());
//		}
		
//        log.info("  map="+map);

        return map;
	}
	
	public Map<String, String> getUnitBossMap(Map<String, Object> map, List<Map<String, Object>> bossList,Map<String, String> tmpMap ) throws Exception {
		
//		String reqByCode = MainUserGroupUtil.login2code(model.getReqBy());
    	String reqBy = (String)map.get(MainWorkflowConstant.WPP_REQ_BY);
    	
    	Map<String, Object> reservedBoss = null;
    	Map<String, Object> lastBossMap = null;
    	
    	/*
    	 * Find Boss by level, if reqByCode exist in workflow path
    	 */
    	int start = 0;
    	int i = 0;
    	
//    	int len = bossList.size();
//    	if (len>0) {
//	    	for(int j=len-1;j>=0;j--) {
//	    		Map<String, Object> boss = bossList.get(j);
//	    		
//	    		log.info("  "+boss.get("lvl")+" "+boss.get("employee_code"));
//	    		
//	    		if (boss.get("employee_code").equals(reqBy)) {
//	    			log.info("  "+reqBy+" in path");
//	    			start = j;
//	    			break;
//	    		}
//	    	}
//    	}
    	
    	/*
    	 * Find Boss by money, if reqByCode not exist in workflow path
    	 */
    	Double total = (Double)map.get(MainWorkflowConstant.WPP_TOTAL);
    	
    	i = 0;
    	log.info("  start:"+start);
    	for(Map<String, Object> boss : bossList) {
    		
    		log.info("  "+boss.get("lvl")+" "+boss.get("employee_code")+" (amt:"+boss.get("amount_max")+")");
    		
    		if (i>=start) {
    			if (tmpMap.size()==0 || total > (Double)lastBossMap.get("amount_max")) {
    				tmpMap.put((String)boss.get("lvl"), (String)boss.get("employee_code"));
    				lastBossMap = boss;
    			}
    			else
    			if (reservedBoss==null) {
    				reservedBoss = boss;
    				break;
    			}
    		}
    		i++;
    	}
    	
    	log.info("  lastBoss:"+lastBossMap.get("employee_code"));
    	/*
    	 * Replace Last Boss if he is Requester
    	 */
    	if (lastBossMap.get("employee_code").equals(reqBy)) {
    		log.info("    is requester");
    		tmpMap.remove(lastBossMap.get("lvl"));
    		if (reservedBoss!=null) {
        		log.info("    replace lastBoss with reservedBoss:"+reservedBoss);
    			tmpMap.put((String)reservedBoss.get("lvl"), (String)reservedBoss.get("employee_code"));
    		}
    		else {
        		log.info("    no reservedBoss");
    			// Waiting SA
    			
    		}
    	}
    	
    	/*
    	 * Remove Requester from first position
    	 */
//    	if (tmpMap.size()>1) {
//    		i = 0;
//    		for(Entry<String, String> e : tmpMap.entrySet()) {
//    			if(i==0) {
//    				if (e.getValue().equals(reqBy)) {
//    					tmpMap.remove(e.getKey());
//    					break;
//    				}
//    			}
//    			i++;
//    		}
//    	}
		
		 /*
         * Remove duplicated Boss
         */
		Map<String, String> bossMap = removeDuplicatedBoss(tmpMap);
		
		return bossMap;
	}
	
	public JSONObject validateWfPath(Map<String, String> bossMap, SubModuleModel model) throws Exception {
		
		JSONObject result = new JSONObject();
		
//        try {
        	
        	if (bossMap==null || bossMap.size()==0) {
				result.put("valid", false);
				
				String msg = moduleService.getMessage("ERR_NOT_FOUND_WORKFLOW_PATH");
				result.put("msg",  msg.split(",")[1]);
        	} else {
        		result.put("valid", true);
        	}
        	
//        } catch (Exception ex) {
//        	result.put("valid", false);
//			String msg = moduleService.getMessage("ERR_NOT_FOUND_WORKFLOW_PATH");
//			result.put("msg",  msg.split(",")[1]);
//			log.error("", ex);
//        }

        return result;
	}
	
	public JSONObject validateNextActor(SubModuleModel model) throws Exception {
		
		List<MainWorkflowNextActorModel> list = moduleService.listNextActor(model);
		
		JSONObject result = new JSONObject();
		
    	if (list==null || list.size()==0) {
			result.put("valid", false);
			
			String msg = moduleService.getMessage("ERR_NOT_FOUND_WORKFLOW_PATH_NEXT_ACTOR");
			result.put("msg",  msg.split(",")[1]);
    	} else {
    		result.put("valid", true);
    	}

        return result;
	}
}
