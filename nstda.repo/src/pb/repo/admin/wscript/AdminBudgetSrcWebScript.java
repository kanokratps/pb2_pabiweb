package pb.repo.admin.wscript;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AdminFundService;
import pb.repo.admin.service.AdminBudgetSrcService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class AdminBudgetSrcWebScript {
	
	private static Logger log = Logger.getLogger(AdminBudgetSrcWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/budgetSrc";

	@Autowired
	AdminBudgetSrcService budgetSrcService;
	
	@Autowired
	AdminFundService fundService;
	
	@Uri(URI_PREFIX+"/list")
	public void handleList(@RequestParam String t,
						   @RequestParam(required=false) String s,
						   @RequestParam(required=false) String lang,
						   @RequestParam(required=false) String onlySectProj,
			 final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			List<Map<String, Object>> list = null;
			
			list = budgetSrcService.list(t,s, lang, onlySectProj==null || onlySectProj.equals("") ? null : new Boolean(onlySectProj));
			
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
	
	@Uri(URI_PREFIX+"/get")
	public void handleGet(@RequestParam String v,
						   @RequestParam(required=false) String lang,
			 final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			String[] s = v.split(",");
			
			Map<String, Object> map = budgetSrcService.get(s[0], s[1], lang);
			
			json = CommonUtil.jsonSuccess(map);
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

	@Uri(URI_PREFIX+"/getDtl")
	public void handleGetDtl(@RequestParam String v,
						   @RequestParam(required=false) String lang,
			 final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			String[] s = v.split(",");
			
			JSONObject jsObj = budgetSrcService.getDtl(s[0], s[1], lang);
			
			List<Map<String, Object>> list = fundService.list(s[0], String.valueOf(jsObj.getInt("id")), lang);
			if (list!=null) {
				Map<String, Object> map = list.get(0);
	    		jsObj.put("fund_id", map.get("id"));
	    		jsObj.put("fund_name", map.get("name"));
			}
			json = CommonUtil.jsonSuccess(jsObj);
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
