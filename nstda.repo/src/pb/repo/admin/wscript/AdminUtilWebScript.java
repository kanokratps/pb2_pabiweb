package pb.repo.admin.wscript;

import java.util.Set;

import org.alfresco.repo.security.authority.UnknownAuthorityException;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Authentication;
import com.github.dynamicextensionsalfresco.webscripts.annotations.AuthenticationType;
import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
@Authentication(value=AuthenticationType.ADMIN)
public class AdminUtilWebScript {
	
	private static Logger log = Logger.getLogger(AdminUtilWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/util";
	
	@Autowired
	private AuthorityService authorityService;

  /**
   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws Exception
   */
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/u2g")
  public void handleAddUserToGroup(@RequestParam(required=false) String u
		  					  ,@RequestParam(required=false) String g
		  					  ,final WebScriptResponse response)  throws Exception {
    
	  	JSONObject jobj = new JSONObject();
	  	String json = null;
	  	
		try {
	  	
		  	log.info("users:"+u);
		  	final String group = authorityService.getName(AuthorityType.GROUP, g);
		  	log.info("group:"+group);
		  	Set<String> ouser = authorityService.getContainedAuthorities(AuthorityType.USER, group, true);
		  	
		  	if (u!=null) {
		  		String[] users = u.split(",");
		  		for(String user : users) {
		  			if (!ouser.contains(user)) {
		  				authorityService.addAuthority(group, user);
		  			}
		  		}
		  	}
		  	jobj.put("success", true);
		
			json = jobj.toString();
			
		} catch (UnknownAuthorityException ex) {
			log.error("", ex);
			
			int pos = ex.toString().indexOf("GROUP_");
			if (pos>=0) {
				String exmsg = ex.toString().substring(pos+6);
				
				json = CommonUtil.jsonFail("Group '"+exmsg+"' not found.");
			} else {
				pos = ex.toString().lastIndexOf(" ");
				String exmsg = ex.toString().substring(pos+1);
				
				json = CommonUtil.jsonFail("User '"+exmsg+"' not found.");
			}
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
}
