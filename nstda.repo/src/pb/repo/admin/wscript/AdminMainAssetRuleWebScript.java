package pb.repo.admin.wscript;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.constant.JsonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainAssetRuleConstant;
import pb.repo.admin.service.AdminAccountActivityService;
import pb.repo.admin.service.AdminAssetRuleService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
//@Authentication(value=AuthenticationType.ADMIN)
public class AdminMainAssetRuleWebScript {
	
	private static Logger log = Logger.getLogger(AdminMainAssetRuleWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/asset/rule";
	
	@Autowired
	private AdminAssetRuleService assetRuleService;


  /**
   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param t : type
   * @param s : searchTerm
   * @param response
   * @throws Exception
   */
  @Uri(URI_PREFIX+"/list")
  public void handleList(@RequestParam(required=true) String query,
		  				 @RequestParam(required=false) Integer fundId,
		  				 @RequestParam(required=false) Integer projectId,
		  				final WebScriptResponse response)  throws Exception {
    
		String json = null;
		
		try {
    		Map<String, Object> params = new HashMap<String, Object>();
    		
        	if (query!=null) {
        		int pos = query.indexOf(" ");
        		String lang = query.substring(0,  pos);
        		lang = lang!=null && lang.startsWith("th") ? "_th" : "";
        		params.put("lang",  lang);
        		params.put("orderBy", "asset_name");
        		params.put("fundId", fundId!=null ? fundId : 0);
        		params.put("projectId", projectId!=null ? projectId : 0);
        		
        		String[] terms = query.substring(pos+1).split(" ");
        		params.put("terms", terms);
        		
        	}
    		
			List<Map<String, Object>> list = assetRuleService.list(params);
			
			Map<String,Object> map = new HashMap<String, Object>();

    		String name = MainAssetRuleConstant.TFN_ASSET_NAME;
			
    		map.put(JsonConstant.COMBOBOX_ID, 0);
    		map.put(JsonConstant.COMBOBOX_NAME, "");

			list.add(0,map);
			
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }

}
