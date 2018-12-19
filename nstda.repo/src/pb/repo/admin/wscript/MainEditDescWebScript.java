package pb.repo.admin.wscript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AlfrescoService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class MainEditDescWebScript {
	
	private static Logger log = Logger.getLogger(MainEditDescWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/main";
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	NodeService nodeService;
	
	@Autowired
	WorkflowService workflowService;
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/editDesc")
	public void handleEditDescPost(@RequestParam String d,
								   @RequestParam String n,
								   @RequestParam(required=false) String t,
								   @RequestParam(required=false) String[] a,
								   final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		log.info("/editdesc");
		log.info("d:"+d);
		log.info("n:"+n);
		log.info("t:"+t);
		log.info("a:"+a);
		log.info("a[0]:"+a[0]);
		try {
			JSONObject jsObj = new JSONObject();
			
			NodeRef nodeRef = new NodeRef(n);
			
			nodeService.setProperty(nodeRef, ContentModel.PROP_DESCRIPTION, d);
			
			List<String> resList = new ArrayList<String>();

			/*
			 * Update Attached Docs
			 */
			if (t!=null) {
				WorkflowTask task = workflowService.getTaskById(t);
				log.info("task="+task);
				if (task!=null) {
					
					List<NodeRef> attachDocList = null;
					
					boolean doUpdate = false;
					
					String MODEL_URI = null;
					
					Map<QName,Serializable> props = task.getProperties();
					for(Entry<QName, Serializable> e : props.entrySet()) {
//						log.info(" - "+e.getKey()+":"+e.getValue()+":"+e.getKey().toPrefixString());
						int pos = e.getKey().toString().indexOf("}attachDocument");
						if (pos>=0) {
							
							MODEL_URI = e.getKey().toString().substring(1, pos);
							log.info("MODEL_URI="+MODEL_URI);

							attachDocList = (ArrayList<NodeRef>)e.getValue();
							
//							String[] adds = a.split(",");
							if (a.length > 0 && !a[0].equals("")) {
								for(String addNode : a) {
									NodeRef addNodeRef = new NodeRef(addNode);
									boolean found = attachDocList.indexOf(addNodeRef)>=0;
	//								for(NodeRef o : attachDocList) {
	//									log.info(" - " +o.toString());
	//									if (addNodeRef.equals(o)) {
	//										found = true;
	//									}
	//								}
									
									if (!found) {
										attachDocList.add(addNodeRef);
										doUpdate = true;
									}
								}
	
								log.info("list.size()="+attachDocList.size());
								
								if (doUpdate) {
									log.info("*** Update Task ***");
									Map<QName, Serializable> params = new HashMap<QName, Serializable>();
									params.put(QName.createQName(MODEL_URI, "attachDocument"), (Serializable)attachDocList);
									workflowService.updateTask(task.getId(), params, null, null);
								}
							}
							break;
						}
					}
					
					for(NodeRef o : attachDocList) {
						resList.add(o.toString());
					}
					
//			        List<NodeRef> attachDocList = new ArrayList<NodeRef>();
	//		        if(model.getListAttachDoc()!=null) {
	//		         	for(String nodeRef : model.getListAttachDoc()) {
	//		         		attachDocList.add(new NodeRef(nodeRef));
	//		            }
	//		        }
			        
	//		        params.put(PcmReqWorkflowConstant.PROP_ATTACH_DOCUMENT, (Serializable)attachDocList);
				}
			}
			
			jsObj.put("result", true);
			jsObj.put("list", String.join(",", resList));
			
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	@Uri(method=HttpMethod.GET, value=URI_PREFIX+"/editDesc")
	public void handleEditDescGet(@RequestParam String n,
								   final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		try {
			JSONObject jsObj = new JSONObject();
			
			NodeRef nodeRef = new NodeRef(n);
			
			String desc = (String)nodeService.getProperty(nodeRef, ContentModel.PROP_DESCRIPTION);
			
			jsObj.put("desc", desc);
			
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
}
