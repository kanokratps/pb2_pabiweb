package pb.repo.admin.wscript;


import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.AdminConstant;
import pb.repo.admin.service.AdminTestSystemService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class AdminMainTestSystemWebScript {
	
	private static Logger log = Logger.getLogger(AdminMainTestSystemWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/testSystem";
	
	@Autowired
	private AdminTestSystemService testSystemService;
  
	@Uri(URI_PREFIX+"/userSignature")
	public void handleUserSignature(final WebScriptResponse response) throws Exception {
	
		String json = null;
		
		try {
			JSONObject jsObj = testSystemService.testUserSignature();
			json = CommonUtil.jsonSuccess(jsObj);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
	}
	
	@Uri(URI_PREFIX+"/currentUser")
	public void handleCurrentUser(final WebScriptResponse response) throws Exception {
	
		String json = null;
		
		try {
			JSONObject jsObj = testSystemService.currentUser();
			json = CommonUtil.jsonSuccess(jsObj);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
	}
	
	@Uri(URI_PREFIX+"/version")
	public void handleVersion(final WebScriptResponse response) throws Exception {
	
		String json = null;
		
		try {
			JSONObject jsObj = new JSONObject();
			
			jsObj.put("msg", AdminConstant.CHANGE_LOG.toString());
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
