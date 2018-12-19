package pb.repo.admin.service;

import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.TaskService;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.namespace.QName;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.repo.admin.constant.MainWorkflowConstant;
import pb.repo.admin.dao.AdminWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowDAO;
import pb.repo.admin.dao.MainWorkflowHistoryDAO;
import pb.repo.admin.model.MainWorkflowHistoryModel;
import pb.repo.admin.model.MainWorkflowModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminWorkflowService {
	
	private static Logger log = Logger.getLogger(AdminWorkflowService.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	WorkflowService workflowService;

	@Autowired
	MainWorkflowService mainWorkflowService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	NodeService nodeService;
	
	public List<Map<String,Object>> list(Map<String,Object> params) {
		
		List<Map<String,Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);
            
            if (params.get("t")!=null && !params.get("t").equals("")) {
            	params.put("type", params.get("t"));
            }
            
    		list = dao.list(params);
            
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public void cancel(JSONArray jsArr) throws Exception {
		
		List<String> ids = new ArrayList<String>();
		for (int i=0; i<jsArr.length(); i++) {
			JSONObject jsObj = jsArr.getJSONObject(i);
			
			ids.add(jsObj.getString("id"));
			
			log.info(" - id:"+jsObj.getString("id"));
		}
		
		if (ids.size()>0) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("ids",ids);
				
			List<Map<String,Object>> wfList = list(params);
			
	        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
	        try {
	            AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);
	            MainWorkflowDAO wfDao = session.getMapper(MainWorkflowDAO.class);
	            MainWorkflowHistoryDAO wfhDao = session.getMapper(MainWorkflowHistoryDAO.class);
	            
				log.info(" wfList.size:"+wfList.size());

				for(Map<String,Object> wf:wfList) {
					log.info("  cancel wf:"+wf.get("activititask")+","+wf.get("docid"));
					workflowService.cancelWorkflow((String)wf.get("workflow_ins_id"));
					
					String status = "X1";
					Integer level = null;
					
		    		MainWorkflowModel workflowModel = null;
		    		
					String id = (String)wf.get("docid");
					if (id!=null) {
						params.put("table", getTableName(id));
						params.put("id", id);
						params.put("updatedBy", authService.getCurrentUserName());
						params.put("status", status);
						// update doc table
			            dao.update(params);
			            
			    		workflowModel = new MainWorkflowModel();
			    		workflowModel.setMasterId(id.toString());
			    		workflowModel = wfDao.getLastWorkflow(workflowModel);
					} else {
						params.put("taskId", wf.get("activititask"));
						List<MainWorkflowModel> list = wfDao.list(params);
						if(list!=null && !list.isEmpty()) {
							workflowModel = list.get(0);
						}
					}
		    		
					log.info("  workflowModel:"+workflowModel);
		    		if (workflowModel!=null) {
						log.info("  workflowModel.id:"+workflowModel.getId());
		    			
			            String taskComment = "Cancelled by Admin";
			            String action = null;
			            		
			    		Timestamp now = CommonDateTimeUtil.now();
			    		String actionTh = "ยกเลิก";
			    		action = "Cancelled";
			    		
			    		String stateTask = MainWorkflowConstant.TN_ADMIN;
			    		
			    		String stateTaskTh = mainWorkflowService.getTaskCaption(stateTask, "th", level);
			    		stateTask = mainWorkflowService.getTaskCaption(stateTask, "", level);
		    			
		    			MainWorkflowHistoryModel workflowHistoryModel = new MainWorkflowHistoryModel();
		    			workflowHistoryModel.setTime(now);
		    			workflowHistoryModel.setBy(authService.getCurrentUserName());
		    			workflowHistoryModel.setAction(action);
		    			workflowHistoryModel.setActionTh(actionTh);
		    			workflowHistoryModel.setTask(stateTask);
		    			workflowHistoryModel.setTaskTh(stateTaskTh);
		    			workflowHistoryModel.setComment((taskComment!=null && !taskComment.equalsIgnoreCase(""))?taskComment:null);
		    			workflowHistoryModel.setMasterId(workflowModel.getId());
		    			workflowHistoryModel.setLevel(level);
		    			workflowHistoryModel.setStatus(status);
		    			wfhDao.add(workflowHistoryModel);
		    			
						log.info("  update");
		    			workflowModel.setStatus(action);
		    			workflowModel.setStatusTh(actionTh);
		    			workflowModel.setBy(authService.getCurrentUserName());
		    			workflowModel.setByTime(workflowHistoryModel.getTime());
		    			wfDao.update(workflowModel);
		    		} else {
		    			log.info("Not Found WorkflowModel");
		    		}		    		
				}
	            
	            session.commit();
	        } catch (Exception ex) {
	        	session.rollback();
	        	throw ex;
	        } finally {
	        	session.close();
	        }
			
		}
	}
	
	public void fixDoc(JSONArray jsArr, WebScriptRequest request) throws Exception {
		
		List<String> ids = new ArrayList<String>();
		for (int i=0; i<jsArr.length(); i++) {
			JSONObject jsObj = jsArr.getJSONObject(i);
			
			ids.add(jsObj.getString("id"));
			
			log.info(" - id:"+jsObj.getString("id"));
		}
		
		if (ids.size()>0) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("ids",ids);
				
			List<Map<String,Object>> wfList = list(params);
			
	        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
	        try {
	            AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);
	            
				log.info(" wfList.size:"+wfList.size());

				for(Map<String,Object> wf:wfList) {
					String taskId = (String)wf.get("activititask");
					final String id = (String)wf.get("docid");
					log.info("  fix doc wf:"+taskId+","+id);
					
					// get folder ref
					params.put("table", getTableName(id));
					params.put("id", id);
					Map<String,Object> docInfo = dao.getForWfPath(params);
					final NodeRef folderNodeRef = new NodeRef((String)docInfo.get("folder_ref"));
					
					Map<QName, Serializable> wfparams = new HashMap<QName, Serializable>();
					
					// attached
					Map<String, Object> result = AuthenticationUtil.runAs(new RunAsWork<Map<String, Object>>()
				    {
						public Map<String, Object> doWork() throws Exception
						{
							Map<String, Object> result = new HashMap<String, Object>();
							
							List<NodeRef> files = new ArrayList<NodeRef>();
							
					    	Set<QName> qnames = new HashSet<QName>();
					    	qnames.add(ContentModel.TYPE_CONTENT);
					    	List<ChildAssociationRef> docs = nodeService.getChildAssocs(folderNodeRef, qnames);
					    	for(ChildAssociationRef doc : docs) {
					    		log.info("doc:"+doc.toString());
					    		log.info("   childRef:"+doc.getChildRef().toString());
					    		
					    		String localName = doc.getQName().getLocalName();
					    		log.info("   qname:"+localName);
					    		
					    		if (localName.startsWith(id) && localName.endsWith(".pdf")) {
					    			result.put("doc", doc.getChildRef());
					    		} else {
					    			files.add(doc.getChildRef());
					    		}
					    	}
					    	
					    	result.put("attached",files);
					    	
					    	return result;
						}
				    }, AuthenticationUtil.getAdminUserName());
					
					if (!id.startsWith("PD") && !id.startsWith("SAL")) {
						List<NodeRef> attachDocList = (List<NodeRef>)result.get("attached");
						if (attachDocList.size()>0) {
					        wfparams.put(QName.createQName(getModelURI(id), "attachDocument"), (Serializable)attachDocList);
						}
					}
					
					// doc
					String docRef = result.get("doc")!=null ? ((NodeRef)result.get("doc")).toString() : null; 
					if (docRef == null) {
						if (!id.startsWith("PD") && !id.startsWith("SAL")) {
							docRef = genDoc(id, request);
						}
					}
					
					if (docRef!=null) {
						// update doc ref
						params.put("doc_ref", docRef);
						dao.update(params);
						
				        List<NodeRef> docList = new ArrayList<NodeRef>();
				        docList.add(new NodeRef(docRef));
						wfparams.put(QName.createQName(getModelURI(id) , "document"), (Serializable)docList);
					}					
					// update wf
				    workflowService.updateTask(taskId, wfparams, null, null);
				}
	            
	            session.commit();
	        } catch (Exception ex) {
	        	session.rollback();
	        	throw ex;
	        } finally {
	        	session.close();
	        }
			
		}
	}	
	
	private String getTableName(String id) {
		String tableName = null;
		
		if (id!=null) {
			if (id.startsWith("PR")) {
				tableName = "pb2_pcm_req";
			}
			else
			if (id.startsWith("PD")) {
				tableName = "pb2_pcm_ord";
			}
			else
			if (id.startsWith("EX")) {
				tableName = "pb2_exp_use";
			}
			else
			if (id.startsWith("AV")) {
				tableName = "pb2_exp_brw";
			}
			else
			if (id.startsWith("HR")) {
				tableName = "pb2_hr_salary";
			}
		}
		
		return tableName;
	}
	
	private String getModelURI(String id) {
		String uri = null;
		
		if (id!=null) {
			if (id.startsWith("PR")) {
				uri = "http://www.nstda.or.th/model/workflow/pcmreqwf/1.0";
			}
			else
			if (id.startsWith("PD")) {
				uri = "http://www.nstda.or.th/model/workflow/pcmordwf/1.0";
			}
			else
			if (id.startsWith("EX")) {
				uri = "http://www.nstda.or.th/model/workflow/expusewf/1.0";
			}
			else
			if (id.startsWith("AV")) {
				uri = "http://www.nstda.or.th/model/workflow/expbrwwf/1.0";
			}
			else
			if (id.startsWith("HR")) {
				uri = "http://www.nstda.or.th/model/workflow/hrsalwf/1.0";
			}
		}
		
		return uri;
	}
	
	private String getUrl(String id) {
		String uri = null;
		
		if (id!=null) {
			if (id.startsWith("PR")) {
				uri = "pcm/req";
			}
			else
			if (id.startsWith("PD")) {
				uri = "pcm/ord";
			}
			else
			if (id.startsWith("EX")) {
				uri = "exp/use";
			}
			else
			if (id.startsWith("AV")) {
				uri = "exp/brw";
			}
			else
			if (id.startsWith("HR")) {
				uri = "hr/sal";
			}
		}
		
		return uri;
	} 
	
	private String genDoc(String id, WebScriptRequest request) {
		String docRef = null;
		
		StringBuilder url = new StringBuilder(request.getServerPath()+"/alfresco/service/pb/"+getUrl(id)+"/genDoc");
		url.append("?alf_ticket="+authService.getCurrentTicket());	
		url.append("&id="+URLEncoder.encode(id));
		log.info("url : " + url);
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpGet = new HttpPost(url.toString());
		try {
			HttpResponse response = httpclient.execute(httpGet);
			log.info(response.getStatusLine());
			
			JSONObject jsObj = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			docRef = jsObj.getJSONObject("data").getString("docRef");
			log.info("docRef:"+docRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return docRef;
	}
	
	public String reassign(JSONArray jsArr,String srcAssignee, String assignee,String reason) throws Exception {
		
		StringBuffer result = new StringBuffer();
		
		List<String> ids = new ArrayList<String>();
		for (int i=0; i<jsArr.length(); i++) {
			JSONObject jsObj = jsArr.getJSONObject(i);
			
			ids.add(jsObj.getString("id"));
			
			log.info(" - id:"+jsObj.getString("id"));
		}
		
		if (ids.size()>0) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("ids",ids);
				
			List<Map<String,Object>> wfList = list(params);
			
	        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
	        try {
	            AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);
	            MainWorkflowDAO wfDao = session.getMapper(MainWorkflowDAO.class);
	            MainWorkflowHistoryDAO wfhDao = session.getMapper(MainWorkflowHistoryDAO.class);
	            
				log.info(" wfList.size:"+wfList.size());

				for(Map<String,Object> wf:wfList) {
					log.info("  reassign wf:"+wf.get("activititask")+","+wf.get("docid"));
					log.info("  		 folderRef:"+wf.get("folder_ref"));
					
					Integer level = null;
					
		    		MainWorkflowModel wfModel = null;
		    		
					String id = (String)wf.get("docid");
					String reqCode = (String)wf.get("requester_code");
					if (id!=null) {
						wfModel = new MainWorkflowModel();
						wfModel.setMasterId(id.toString());
						wfModel = wfDao.getLastWorkflow(wfModel);
					} else {
						params.put("taskId", wf.get("activititask"));
						List<MainWorkflowModel> list = wfDao.list(params);
						if(list!=null && !list.isEmpty()) {
							wfModel = list.get(0);
						}
					}
		    		
					log.info("  workflowModel:"+wfModel);
		    		if (wfModel!=null) {
						log.info("  workflowModel.id:"+wfModel.getId());
						
						String taskComment = "Reassign by Admin from "+srcAssignee+" to "+assignee;
						
						boolean reqMatch = reqCode.equals(assignee);
						log.info("   reqMatch("+reqCode+","+assignee+"):"+reqMatch);
						if(!reqMatch) {
							reassignWorkflow(dao, wfModel, srcAssignee, assignee, (String)wf.get("folder_ref"));
							
			    			Map<String, Object> newAssignee = new HashMap<String, Object>();
			    			newAssignee.put("wkId", wfModel.getWorkflowInsId());
			    			newAssignee.put("srcUser", srcAssignee);
			    			newAssignee.put("destUser", assignee);
			    			newAssignee.put("active", CommonConstant.V_ENABLE);
			    			newAssignee.put("createdBy", authService.getCurrentUserName());
			    			
			    			mainWorkflowService.addWorkflowAssignee(newAssignee);
						}
						else {
							taskComment += " *** Requester("+wfModel.getCreatedBy()+") matches assignee ***";
							if (result.length()>0) {
								result.append("<br/>");
							} else {
								result.append("The following codes' requester match assignee:<br/>");
							}
							result.append(wf.get("docid"));
						}

						taskComment += ": "+reason;
			            
			    		Timestamp now = CommonDateTimeUtil.now();
			    		String actionTh = "มอบหมาย";
			    		String action = "Reassign";
			    		
			    		String stateTask = MainWorkflowConstant.TN_ADMIN;
			    		
			    		String stateTaskTh = mainWorkflowService.getTaskCaption(stateTask, "th", level);
			    		stateTask = mainWorkflowService.getTaskCaption(stateTask, "", level);
						
						params.put("masterId", wfModel.getId());
						MainWorkflowHistoryModel lastWfhModel = wfhDao.getLastByMasterId(params); 
						
		    			MainWorkflowHistoryModel workflowHistoryModel = new MainWorkflowHistoryModel();
		    			workflowHistoryModel.setTime(now);
		    			workflowHistoryModel.setBy(authService.getCurrentUserName());
		    			workflowHistoryModel.setAction(action);
		    			workflowHistoryModel.setActionTh(actionTh);
		    			workflowHistoryModel.setTask(stateTask);
		    			workflowHistoryModel.setTaskTh(stateTaskTh);
		    			workflowHistoryModel.setComment((taskComment!=null && !taskComment.equalsIgnoreCase(""))?taskComment:null);
		    			workflowHistoryModel.setMasterId(wfModel.getId());
		    			workflowHistoryModel.setLevel(lastWfhModel.getLevel());
		    			workflowHistoryModel.setStatus(lastWfhModel.getStatus());
		    			wfhDao.add(workflowHistoryModel);
		    		} else {
		    			log.info("Not Found WorkflowModel");
		    		}		    		
				}
	            
	            session.commit();
	        } catch (Exception ex) {
	        	log.error(ex);
	        	session.rollback();
	        	throw ex;
	        } finally {
	        	session.close();
	        }
			
		}
		
		return result.toString();
	}
	
	private void reassignWorkflow(AdminWorkflowDAO dao, final MainWorkflowModel wfModel, String srcAssignee, final String assignee, String folderRef) throws Exception {
		// update task assignee
		log.info("set assignee:"+wfModel.getTaskId()+","+assignee);
		String taskId = wfModel.getTaskId().substring(9); // cut "activiti$"
		log.info("	  task id:"+taskId);
		
		WorkflowTask task = AuthenticationUtil.runAs(new RunAsWork<WorkflowTask>()
	    {
			public WorkflowTask doWork() throws Exception
			{
		    	return workflowService.getTaskById(wfModel.getTaskId());
			}
	    }, AuthenticationUtil.getAdminUserName());
		
//		String ouser = null;
//		
//		Map<QName, Serializable> props = task.getProperties();
//		for(Entry<QName, Serializable> e:props.entrySet()) {
////			log.info(e.getKey().toPrefixString()+" : "+e.getValue());
//			if(e.getKey().toPrefixString().equals("owner")) {
//				ouser = (String)e.getValue();
//				break;
//			}
//		}
		
		String ouser = (String)task.getProperties().get(ContentModel.PROP_OWNER);
		log.info("old user:"+ouser);
		if (ouser.equals(srcAssignee)) {
			log.info("reassigne current task:"+ouser+" to "+assignee);
			taskService.setAssignee(taskId, assignee);
		}
		
		if (folderRef!=null) {
			final NodeRef folderNodeRef = new NodeRef(folderRef);
	
			// set folder permission
	        AuthenticationUtil.runAs(new RunAsWork<String>()
		    {
				public String doWork() throws Exception
				{
					permissionService.setPermission(folderNodeRef, assignee, "SiteCollaborator", true);
					
			    	return null;
				}
		    }, AuthenticationUtil.getAdminUserName());
		}
		
		// update table workflow_reviewer
		log.info("update reviewer table:"+wfModel.getMasterId());
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ouser", srcAssignee);
		params.put("user", assignee);
		params.put("master_id", wfModel.getMasterId());

		dao.updateReviewer(params);
	}

	public Map<String,Object> getByDocId(String id) {
		
		Map<String,Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            AdminWorkflowDAO dao = session.getMapper(AdminWorkflowDAO.class);
            
    		map = dao.getByDocId(id);
            
        } finally {
        	session.close();
        }
        
        return map;
	}
	
}
