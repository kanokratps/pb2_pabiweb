package pb.repo.admin.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.TicketComponent;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;

@Service
public class AdminTestSystemService {
	
	private static Logger log = Logger.getLogger(AdminTestSystemService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private FileFolderService fileFolderService;
	
	@Autowired
	private AdminUserGroupService userGroupService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	private TicketComponent ticketComponent;

	private void putUserToSet(String s, Set<String> set) {
		  if (s!=null) {
			  String[] ss = s.split(",");
			  for(String a : ss) {
				  if (a!=null && !a.equals("")) {
					  set.add(a);
				  }
			  }
		  }
	}
	
	private void putGroupToSet(String s, Set<String> set, Set<String> invalidGroups) {
	  if (s!=null) {
		  String[] ss = s.split(",");
		  for(String a : ss) {
			  if (a!=null && !a.equals("")) {
				  try {
		  			  Set<String> userList = userGroupService.getGroupMember("GROUP_"+a.trim());
		  			  for(String user : userList) {
		  				  set.add(user);
		  			  }
				  } catch (Exception ex) {
					  invalidGroups.add(a.trim());
				  }
			  }
		  }
	  }
	}
	
	public JSONObject testUserSignature() throws Exception {
		
		JSONObject result = new JSONObject();
		
        try {
        	Set<String> invalidGroups = new HashSet<String>();
        	
        	Map<String, Object> params = new HashMap<String, Object>();
//        	List<Map<String, Object>> list = approvalMatrixService.list(params);
        	List<Map<String, Object>> list = null;
            
  		  	Set<String> set = new HashSet<String>();
            for(Map<String, Object> amModel : list) {
//            	putUserToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_REQUESTER_USER), set);
//            	putUserToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_ACTION_USER), set);
//            	putUserToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_RELATED_USER), set);
//	            
//	            putGroupToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_REQUESTER_GROUP), set, invalidGroups);
//	            putGroupToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_ACTION_GROUP), set, invalidGroups);
//	            putGroupToSet((String)amModel.get(MainApprovalMatrixConstant.TFN_RELATED_GROUP), set, invalidGroups);
	            
//	           	List<MainApprovalMatrixDtlModel> listDtl = approvalMatrixService.listDtl((Long)amModel.get(MainApprovalMatrixConstant.TFN_ID));
//	           	for (MainApprovalMatrixDtlModel dtlModel : listDtl) {
//	           		putUserToSet(dtlModel.getReviewerUser(), set);
//	           		putGroupToSet(dtlModel.getReviewerGroup(), set, invalidGroups);
//	           	}
            }
            
    		MainMasterModel folderModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_SIGNATURE_PATH);
    		NodeRef folderRef = new NodeRef(folderModel.getFlag1());
    		
        	Set<QName> qnames = new HashSet<QName>();
        	qnames.add(ContentModel.TYPE_CONTENT);
        	List<ChildAssociationRef> docs = nodeService.getChildAssocs(folderRef, qnames);
        	List<String> fileNames = new ArrayList<String>();
        	for(ChildAssociationRef doc : docs) {
        		FileInfo fileInfo = fileFolderService.getFileInfo(doc.getChildRef());
        		fileNames.add(fileInfo.getName());
        	}
  		
        	log.info("users:"+set);
        	
        	Set<String> invalidUsers = new HashSet<String>();
  		  	for(String userId : set) {
  			  if (!fileNames.contains(userId.toLowerCase()+".png")) {
  				  invalidUsers.add(userId);
  			  }
  		  	}
  		  
            log.info("invalidUsers:"+invalidUsers);
            
        	if (invalidUsers.size()>0 || invalidGroups.size()>0) {
				result.put("valid", false);
        		result.put("users", invalidUsers);
        		result.put("groups", invalidGroups);
        		result.put("folder", URLDecoder.decode(alfrescoService.getFolderPath(folderRef),StandardCharsets.UTF_8.toString()));
        	} else {
        		result.put("valid", true);
        	}
        	
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        }

        return result;
    }

	public JSONObject testUserSignature(String userId) throws Exception {
		
		JSONObject result = new JSONObject();
		
        try {
    		MainMasterModel folderModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_SIGNATURE_PATH);
    		NodeRef folderRef = new NodeRef(folderModel.getFlag1());
    		
        	Set<QName> qnames = new HashSet<QName>();
        	qnames.add(ContentModel.TYPE_CONTENT);
        	List<ChildAssociationRef> docs = nodeService.getChildAssocs(folderRef, qnames);
        	List<String> fileNames = new ArrayList<String>();
        	for(ChildAssociationRef doc : docs) {
        		FileInfo fileInfo = fileFolderService.getFileInfo(doc.getChildRef());
        		fileNames.add(fileInfo.getName());
        	}
  		
        	log.info("test signature user:"+userId);
        	
        	Set<String> invalidUsers = new HashSet<String>();
		    if (!fileNames.contains(userId.toLowerCase()+".png")) {
			  invalidUsers.add(userId);
		    }
  		  
            log.info("invalidUsers:"+invalidUsers);
            
        	if (invalidUsers.size()>0) {
				result.put("valid", false);
        		result.put("users", invalidUsers);
        		result.put("folder", URLDecoder.decode(alfrescoService.getFolderPath(folderRef),StandardCharsets.UTF_8.toString()));
        	} else {
        		result.put("valid", true);
        	}
        	
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        }

        return result;
    }
	
	public JSONObject currentUser() throws Exception {
		
		JSONObject result = new JSONObject();
		
        try {
        	Set<String> users = ticketComponent.getUsersWithTickets(true);
        	String rm = null;
        	for(String u : users) {
        		if (u.equals("guest")) {
        			rm = u;
        		}
        	}
        	
        	if (rm!=null) {
        		users.remove(rm);
        	}
        	
        	log.info("users:"+users);
            
			result.put("cnt", users.size());
    		result.put("users", users);
        	
        } catch (Exception ex) {
			log.error("", ex);
        	throw ex;
        }

        return result;
    }
}
