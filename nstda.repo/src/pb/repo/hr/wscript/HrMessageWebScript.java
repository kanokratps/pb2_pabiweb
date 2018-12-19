package pb.repo.hr.wscript;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.util.MainUtil;
import pb.repo.hr.util.HrSalUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;


@Component
@WebScript
public class HrMessageWebScript {
	
	private static Logger log = Logger.getLogger(HrMessageWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/hr/message";
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	AdminMasterService masterService;

  /**
   * Handles the "get" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param key
   * @param response
   * @throws IOException
   */
  @Uri(URI_PREFIX+"/get")
  public void handleGet(@RequestParam final String key,
		  				@RequestParam(required=false) String lang, 
		  				final WebScriptResponse response) throws IOException, JSONException {
	  
	  String json = null;
	  
	  log.info(key+":"+lang);
	  if (MainUtil.validSession(authService)) {
	  
		  lang = CommonUtil.getValidLang(lang);
		  
		  String msg = HrSalUtil.getMessage(key, new Locale(lang));
		  
		  if (StringUtils.isEmpty(msg)) {
			  msg = CommonUtil.getInvalidKeyMsg(key);
		  }
		  json = CommonUtil.jsonMessage(msg);
	  } else {
		  json = CommonUtil.jsonFail("");
	  }
	  
	  CommonUtil.responseWrite(response, json);
  }
  
  /**
   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param keys
   * @param response
   * @throws IOException
   */
  @Uri(URI_PREFIX+"/list")
  public void handleList(@RequestParam final String keys,
		  				 @RequestParam(required=false) String lang,
		  				 final WebScriptResponse response) throws IOException, JSONException {
	  log.info(keys+":"+lang);
	  
	  String[] keyArr = keys.split(",");
	  
	  List<String> msgs = new LinkedList<String>();
	  
	  lang = CommonUtil.getValidLang(lang);
	  
	  for(String key : keyArr) {
		  
		  String msg = HrSalUtil.getMessage(key, new Locale(lang));
		  
		  if (StringUtils.isEmpty(msg)) {
			msg = CommonUtil.getInvalidKeyMsg(key);
		  }
		  msgs.add(msg);
	  }
	  
	  String json = CommonUtil.jsonMessage(msgs);
	  
	  CommonUtil.responseWrite(response, json);
  }
  
  /**
   * Handles the "ord" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws IOException
   */
  @Uri(URI_PREFIX+"/sal")
  public void handleSal(@RequestParam(required=false) String lang,
		  				final WebScriptResponse response) throws IOException, JSONException {
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  JSONObject jObj = new JSONObject();
	  /*
	   * Main
	   */
	  JSONObject lbl = new JSONObject();
	  String prefix = "sal.main.";
	  lbl.put("search", HrSalUtil.getMessage(prefix+"search", locale));
	  
	  lbl.put("salNo", HrSalUtil.getMessage(prefix+"salNo", locale));
	  lbl.put("org", HrSalUtil.getMessage(prefix+"org", locale));
	  lbl.put("objective", HrSalUtil.getMessage(prefix+"objective", locale));
	  lbl.put("method", HrSalUtil.getMessage(prefix+"method", locale));
	  lbl.put("total", HrSalUtil.getMessage(prefix+"total", locale));
	  lbl.put("preparer", HrSalUtil.getMessage(prefix+"preparer", locale));
	  lbl.put("requestTime", HrSalUtil.getMessage(prefix+"requestTime", locale));
	  lbl.put("status", HrSalUtil.getMessage(prefix+"status", locale));
	  lbl.put("budgetSrc", HrSalUtil.getMessage(prefix+"budgetSrc", locale));

	  jObj.put("m", lbl);
	  
	  /*
	   * Status
	   */
	  JSONObject st = new JSONObject();
	  List<Map<String, Object>> list = masterService.listByType(MainMasterConstant.TYPE_SAL_STATUS, null, true, null, 0, 100);
	  for(Map<String, Object> model:list) {
		  lbl = new JSONObject();
		  lbl.put("color", model.get("flag3"));
		  lbl.put("text_en", model.get("flag2"));
		  lbl.put("text_th", model.get("name"));
		  
		  st.put((String)model.get("code"), lbl);
	  }
	  
	  jObj.put("s", st);

	  
	  String json = jObj.toString();
	  
	  CommonUtil.responseWrite(response, json);
  }  

}
