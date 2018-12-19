package pb.repo.admin.wscript;

import java.util.Map;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AdminModuleService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class MainModuleWebScript {
	
	private static Logger log = Logger.getLogger(MainModuleWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main";
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	private AdminModuleService moduleService;

  /**
   * Handles the "totalPreBudget" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws Exception
   */
  @Uri(URI_PREFIX+"/totalPreBudget")
  public void handleTotalPreBudget(@RequestParam String budgetCcType,
								  @RequestParam Integer budgetCc,
								  @RequestParam Integer fundId,
								  final WebScriptResponse response)  throws Exception {

	  	JSONObject jobj = new JSONObject();
	  	String json = null;
		try {
			log.info("budgetSrc:"+budgetCcType+":"+budgetCc+", fundId:"+fundId);
			
		  	Map<String, Object> map = moduleService.getTotalPreBudget(budgetCcType, budgetCc, fundId, null, null, false);
		  	
		  	jobj.put("data", map);
			
		  	json = jobj.toString();
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
}
