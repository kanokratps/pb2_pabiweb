package pb.repo.admin.wscript;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AdminCostControlService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class AdminCostControlWebScript {
	
	private static Logger log = Logger.getLogger(AdminCostControlWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/costcontrol";

	@Autowired
	AdminCostControlService costControlService;
	
	@Uri(URI_PREFIX+"/list")
	public void handleList(@RequestParam String t,
						   @RequestParam(required=false) String s,
						   @RequestParam(required=false) Integer sid,
						   @RequestParam(required=false) String lang,
			 final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			List<Map<String, Object>> list = costControlService.list(Integer.parseInt(t!=null && !t.equals("") ? t : "0"),s,sid,lang);
			
			json = CommonUtil.jsonSuccess(list);
		} catch (Exception ex) {
			log.error("", ex);
			try {
				json = CommonUtil.jsonFail(ex.toString());
			} catch (JSONException e) {
				log.error("", e);
			}
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	@Uri(URI_PREFIX+"/type/list")
	public void handleTypeList(@RequestParam(required=false) String lang,
							   final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			List<Map<String, Object>> list = costControlService.listType(lang);
			
			json = CommonUtil.jsonSuccess(list);
		} catch (Exception ex) {
			log.error("", ex);
			try {
				json = CommonUtil.jsonFail(ex.toString());
			} catch (JSONException e) {
				log.error("", e);
			}
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	
}
