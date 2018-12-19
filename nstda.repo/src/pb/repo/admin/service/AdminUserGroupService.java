package pb.repo.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.alfresco.query.PagingRequest;
import org.alfresco.query.PagingResults;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.constant.MainUserGroupConstant;
import pb.repo.admin.model.MainUserGroupModel;

@Service
public class AdminUserGroupService {
	
	private static Logger log = Logger.getLogger(AdminUserGroupService.class);

	@Autowired
	AuthorityService authorityService;

	@Autowired
	PersonService personService;
	
	public List<MainUserGroupModel> listUserGroup(String searchText, String type, String notIn) throws Exception {
		
		List<MainUserGroupModel> userGroupList = new ArrayList<MainUserGroupModel>();
		
		List<String> groupList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		
		if (notIn != null) {
			JSONArray jsonArr = new JSONArray(notIn);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jobj = jsonArr.getJSONObject(i);
				
				if (jobj.getString("type").equals(MainUserGroupConstant.TYPE_GROUP)) {
					groupList.add(jobj.getString("id"));
				}
				else {
					userList.add(jobj.getString("id"));
				}
				
			}
		}
		
		PagingRequest pgrq = new PagingRequest(1000);
		
		MainUserGroupModel model;
		
		if (type.equalsIgnoreCase(MainUserGroupConstant.TYPE_GROUP) || type.equalsIgnoreCase(MainUserGroupConstant.TYPE_BOTH)) {
			PagingResults<String> groups = authorityService.getAuthorities(AuthorityType.GROUP, null, searchText, true, true, pgrq);
			
			List<String> list = groups.getPage();
			for(String group : list) {
				
	    		if(!group.startsWith("GROUP_site_")) {
	    			String id = group.replace("GROUP_", "");
	    			if (groupList.indexOf(id) < 0) {
						model = new MainUserGroupModel();
						
						model.setId(id);
						model.setName(authorityService.getAuthorityDisplayName(group));
						model.setType(MainUserGroupConstant.TYPE_GROUP);
						
						userGroupList.add(model);
	    			}
	    		}

			}
		}
		
		if (type.equalsIgnoreCase(MainUserGroupConstant.TYPE_USER) || type.equalsIgnoreCase(MainUserGroupConstant.TYPE_BOTH)) {
			PagingResults<String> users = authorityService.getAuthorities(AuthorityType.USER, null, searchText, true, true, pgrq);
			
			List<String> list = users.getPage();
			for(String user : list) {
    			if (userList.indexOf(user) < 0) {
					model = new MainUserGroupModel();
					
					model.setId(user);
					model.setName(authorityService.getAuthorityDisplayName(user));
					model.setType(MainUserGroupConstant.TYPE_USER);
					
					userGroupList.add(model);
    			}
			}
		}
		
		return userGroupList;
	}
	
	public List<MainUserGroupModel> listUserGroupDetail(String listStr, String type) throws Exception {
		
		List<MainUserGroupModel> userGroupList = new ArrayList<MainUserGroupModel>();
		
		String[] list = listStr.split(",");

		MainUserGroupModel model;
		for(String id : list) {
			
			if (id.length() > 0) {
				model = new MainUserGroupModel();
				
				model.setId(id);
				model.setName(authorityService.getAuthorityDisplayName((type.equals(MainUserGroupConstant.TYPE_GROUP) ? "GROUP_" : "") +id));
				model.setType(type);
				
				userGroupList.add(model);
			}
		}
		
		return userGroupList;
	}

	public String getAuthoritiesForUser(final String userId) {
		Set<String> authSet = AuthenticationUtil.runAs(new RunAsWork<Set<String>>()
			    {
					public Set<String> doWork() throws Exception
					{
						return authorityService.getAuthoritiesForUser(userId);
					}
			    }, AuthenticationUtil.getAdminUserName());

		StringBuffer authorities = new StringBuffer();
		
		for(String auth : authSet) {
			if (authorities.length()>0) {
				authorities.append(",");
			}
			authorities.append("'"+auth+"'");
		}
		
		return authorities.toString();
	}
	
	public NodeRef getAuthorityNodeRef(String ug) {
		return authorityService.getAuthorityNodeRef(ug);
	}
	
	public Set<String> getGroupMember(final String groupName) throws Exception {
		Set<String> authorities = AuthenticationUtil.runAs(new RunAsWork<Set<String>>()
			    {
					public Set<String> doWork() throws Exception
					{
						return authorityService.getContainedAuthorities(AuthorityType.USER, groupName, false);
					}
			    }, AuthenticationUtil.getAdminUserName());

		return authorities;
	}
	
	public Boolean isValidGroupName(String groupName) throws Exception {
		Boolean result = true;
		
		NodeRef groupNodeRef = getAuthorityNodeRef("GROUP_"+groupName);
		if (groupNodeRef == null) {
			result = false;
		} else {
			Set<String> groupMember = getGroupMember("GROUP_"+groupName);
			if (groupMember==null || groupMember.size()==0) {
				result = false;
			}
		}
		
		return result;
	}
	
	public List<String> listInvalidUser(String v) {
		List<String> invalidUsers = new ArrayList<String>();
		
        if (v!=null && !v.equals("")) {
        	String[] users = v.split(",");
        	for(int i=0; i<users.length; i++) {
        		if (users[i] != null && !users[i].equals("")) {
        			NodeRef person = personService.getPersonOrNull(users[i]);
        			if (person == null) {
        				invalidUsers.add(users[i]);
        			}
        		}
        	}
        }		
        
        return invalidUsers;
	}
	
	public List<String> listInvalidGroup(final String v) {
		
		List<String> invalidGroups = AuthenticationUtil.runAs(new RunAsWork<List<String>>()
	    {
			public List<String> doWork() throws Exception
			{
				List<String> invalidGroups = new ArrayList<String>();
				
		        if (v!=null && !v.equals("")) {
		        	String[] groups = v.split(",");
		        	for(int i=0; i<groups.length; i++) {
		        		if (groups[i] != null && !groups[i].equals("")) {
		            		if (!isValidGroupName(groups[i])) {
		            			invalidGroups.add(groups[i]);
		            		}
		        		}
		        	}
		        }	
				
		    	return invalidGroups;
			}
	    }, AuthenticationUtil.getAdminUserName());
		
        return invalidGroups;
	}
	
	public List<String> listRole(String user) {
		String userRoles = getAuthoritiesForUser(user)
				.replace("GROUP_", "")
				.replace("','", ",")
				;
		if (userRoles.startsWith("'")) {
			userRoles = userRoles.substring(1);
		}
		if (userRoles.endsWith("'")) {
			userRoles = userRoles.substring(0, userRoles.length()-1);
		}
		
		String[] roles = userRoles.split(",");
		List<String> roleList = new ArrayList<String>();
		for(int i=0; i<roles.length; i++) {
			if (!roles[i].startsWith("site_")) {
				roleList.add(roles[i]);
			}
		}
		
		return roleList;
	}
}
