package pb.repo.exp.wscript;


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
import pb.repo.exp.util.ExpBrwUtil;
import pb.repo.exp.util.ExpUseUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;


@Component
@WebScript
public class ExpMessageWebScript {
	
	private static Logger log = Logger.getLogger(ExpMessageWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/exp/message";
	
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
		  				@RequestParam(required=false) String lang
		  				, final WebScriptResponse response) throws IOException, JSONException {

	  String json = null;
	  
	  log.info(key+":"+lang);
	  if (MainUtil.validSession(authService)) {
	  
		  lang = CommonUtil.getValidLang(lang);
	
		  String msg = ExpBrwUtil.getMessage(key, new Locale(lang));
		  
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
	  
	  lang = CommonUtil.getValidLang(lang);
	
	  String[] keyArr = keys.split(",");
	  
	  List<String> msgs = new LinkedList<String>();
	  
	  for(String key : keyArr) {
		  
		  String msg = ExpBrwUtil.getMessage(key, new Locale(lang));
		  
		  if (StringUtils.isEmpty(msg)) {
			msg = CommonUtil.getInvalidKeyMsg(key);
		  }
		  msgs.add(msg);
	  }
	  
	  String json = CommonUtil.jsonMessage(msgs);
	  
	  CommonUtil.responseWrite(response, json);
  }
  
  /**
   * Handles the "req" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws IOException
   */
  @Uri(URI_PREFIX+"/brw")
  public void handleBrw(@RequestParam(required=false) String lang,
		  			final WebScriptResponse response) throws IOException, JSONException {
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  JSONObject jObj = new JSONObject();
	  /*
	   * Main
	   */
	  JSONObject lbl = new JSONObject();
	  String prefix = "av.main.";
	  lbl.put("avNo", ExpBrwUtil.getMessage(prefix+"avNo", locale));
	  lbl.put("avType", ExpBrwUtil.getMessage(prefix+"avType", locale));
	  lbl.put("budget", ExpBrwUtil.getMessage(prefix+"budget", locale));
	  lbl.put("objective", ExpBrwUtil.getMessage(prefix+"objective", locale));
	  lbl.put("amount", ExpBrwUtil.getMessage(prefix+"amount", locale));
	  lbl.put("requester", ExpBrwUtil.getMessage(prefix+"requester", locale));
	  lbl.put("preparer", ExpBrwUtil.getMessage(prefix+"preparer", locale));
	  lbl.put("requestTime", ExpBrwUtil.getMessage(prefix+"requestTime", locale));
	  lbl.put("status", ExpBrwUtil.getMessage(prefix+"status", locale));
	  
	  jObj.put("m", lbl);

	  /*
	   * User Tab
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.usr.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("reqBy", ExpBrwUtil.getMessage(prefix+"reqBy", locale));
	  lbl.put("reqBu", ExpBrwUtil.getMessage(prefix+"reqBu", locale));
	  lbl.put("reqOu", ExpBrwUtil.getMessage(prefix+"reqOu", locale));
	  lbl.put("createdTime", ExpBrwUtil.getMessage(prefix+"createdTime", locale));
	  lbl.put("createdBy", ExpBrwUtil.getMessage(prefix+"createdBy", locale));
	  lbl.put("telNo", ExpBrwUtil.getMessage(prefix+"telNo", locale));
	  
	  jObj.put("u", lbl);
	  
	  /*
	   * Info
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.info.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("objType", ExpBrwUtil.getMessage(prefix+"objectiveType", locale));
	  lbl.put("err_objType", ExpBrwUtil.getMessage(prefix+"err.objectiveType", locale));
	  lbl.put("obj", ExpBrwUtil.getMessage(prefix+"objective", locale));
	  lbl.put("item", ExpBrwUtil.getMessage(prefix+"item", locale));
	  lbl.put("amount", ExpBrwUtil.getMessage(prefix+"amount", locale));
	  lbl.put("total", ExpBrwUtil.getMessage(prefix+"total", locale));
	  lbl.put("budgetSrc", ExpBrwUtil.getMessage(prefix+"budgetSrc", locale));
	  lbl.put("cc", ExpBrwUtil.getMessage(prefix+"costControl", locale));
	  lbl.put("invAsset", ExpBrwUtil.getMessage(prefix+"invAsset", locale));
	  lbl.put("constr", ExpBrwUtil.getMessage(prefix+"construction", locale));
	  lbl.put("itemTitle", ExpBrwUtil.getMessage(prefix+"item.title", locale));
	  lbl.put("reason", ExpBrwUtil.getMessage(prefix+"reason", locale));
	  lbl.put("err_refId", ExpBrwUtil.getMessage(prefix+"err.refId", locale));
	  lbl.put("itemTitle", ExpBrwUtil.getMessage(prefix+"item.title", locale));
	  lbl.put("oweOrder", ExpBrwUtil.getMessage(prefix+"owe.order", locale));
	  lbl.put("oweTitle", ExpBrwUtil.getMessage(prefix+"owe.title", locale));
	  lbl.put("oweDocNo", ExpBrwUtil.getMessage(prefix+"owe.docno", locale));
	  lbl.put("oweName", ExpBrwUtil.getMessage(prefix+"owe.name", locale));
	  lbl.put("oweWait", ExpBrwUtil.getMessage(prefix+"owe.wait", locale));
	  lbl.put("oweBalance", ExpBrwUtil.getMessage(prefix+"owe.balance", locale));
	  lbl.put("methodTitle", ExpBrwUtil.getMessage(prefix+"method.title", locale));
	  lbl.put("bankBbl", ExpBrwUtil.getMessage(prefix+"bank.bbl", locale));
	  lbl.put("bank", ExpBrwUtil.getMessage(prefix+"bank", locale));
	  lbl.put("bankWarn", ExpBrwUtil.getMessage(prefix+"bank.warn", locale));
	  lbl.put("dateBack", ExpBrwUtil.getMessage(prefix+"dateBack", locale));
	  lbl.put("avRemark", ExpBrwUtil.getMessage(prefix+"avRemark", locale));
	  lbl.put("isSA", ExpBrwUtil.getMessage(prefix+"isSmallAmount", locale));
	  lbl.put("prNumber", ExpBrwUtil.getMessage(prefix+"prNumber", locale));
	  
	  jObj.put("n", lbl);
	  
	  /*
	   * Item
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.item.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("actGrp", ExpBrwUtil.getMessage(prefix+"actGrp", locale));
	  lbl.put("act", ExpBrwUtil.getMessage(prefix+"act", locale));
	  lbl.put("asset", ExpBrwUtil.getMessage(prefix+"asset", locale));
	  lbl.put("desc", ExpBrwUtil.getMessage(prefix+"desc", locale));
	  lbl.put("cond", ExpBrwUtil.getMessage(prefix+"cond", locale));
	  lbl.put("pos", ExpBrwUtil.getMessage(prefix+"pos", locale));
	  lbl.put("amt", ExpBrwUtil.getMessage(prefix+"amt", locale));
	  lbl.put("amtAllow", ExpBrwUtil.getMessage(prefix+"amt.allow", locale));
	  lbl.put("total", ExpBrwUtil.getMessage(prefix+"total", locale));
	  
	  lbl.put("isReason", ExpBrwUtil.getMessage(prefix+"is_reason", locale));
	  lbl.put("reason", ExpBrwUtil.getMessage(prefix+"reason", locale));
	  lbl.put("note", ExpBrwUtil.getMessage(prefix+"note", locale));
	  
	  lbl.put("totalExceed", ExpBrwUtil.getMessage(prefix+"totalExceed", locale));
	  lbl.put("totalExceedPR", ExpBrwUtil.getMessage(prefix+"totalExceedPR", locale));
	  lbl.put("totalZero", ExpBrwUtil.getMessage(prefix+"totalZero", locale));
	  
	  jObj.put("i", lbl);
	  
	  /*
	   * Attendee
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.att.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("expType", ExpBrwUtil.getMessage(prefix+"expType", locale));
	  lbl.put("empList", ExpBrwUtil.getMessage(prefix+"empList", locale));
	  lbl.put("othList", ExpBrwUtil.getMessage(prefix+"othList", locale));
	  lbl.put("from", ExpBrwUtil.getMessage(prefix+"fromDate", locale));
	  lbl.put("to", ExpBrwUtil.getMessage(prefix+"toDate", locale));
	  lbl.put("training", ExpBrwUtil.getMessage(prefix+"training", locale));
	  lbl.put("seminar", ExpBrwUtil.getMessage(prefix+"seminar", locale));
	  lbl.put("other", ExpBrwUtil.getMessage(prefix+"other", locale));
	  lbl.put("dupAtt", ExpBrwUtil.getMessage(prefix+"dupAtt", locale));
	  
	  jObj.put("a", lbl);
	  
	  /*
	   * File
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.file.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  
	  jObj.put("f", lbl);
	  
	  /*
	   * Status
	   */
	  JSONObject st = new JSONObject();
	  List<Map<String, Object>> list = masterService.listByType(MainMasterConstant.TYPE_EXP_BRW_STATUS, null, true, null, 0, 100);
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
  
  /**
   * Handles the "ord" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws IOException
   */
  @Uri(URI_PREFIX+"/use")
  public void handleUse(@RequestParam(required=false) String lang,
		  			final WebScriptResponse response) throws IOException, JSONException {
	  
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  JSONObject jObj = new JSONObject();
	  /*
	   * Main
	   */
	  JSONObject lbl = new JSONObject();
	  String prefix = "ex.main.";
	  lbl.put("exNo", ExpUseUtil.getMessage(prefix+"exNo", locale));
	  lbl.put("exType", ExpUseUtil.getMessage(prefix+"exType", locale));
	  lbl.put("budget", ExpUseUtil.getMessage(prefix+"budget", locale));
	  lbl.put("objective", ExpUseUtil.getMessage(prefix+"objective", locale));
	  lbl.put("amount", ExpUseUtil.getMessage(prefix+"amount", locale));
	  lbl.put("requester", ExpUseUtil.getMessage(prefix+"requester", locale));
	  lbl.put("preparer", ExpUseUtil.getMessage(prefix+"preparer", locale));
	  lbl.put("requestTime", ExpUseUtil.getMessage(prefix+"requestTime", locale));
	  lbl.put("status", ExpUseUtil.getMessage(prefix+"status", locale));
	  
	  jObj.put("m", lbl);

	  /*
	   * User Tab
	   */
	  lbl = new JSONObject();
	  prefix = "av.form.tab.usr.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("reqBy", ExpBrwUtil.getMessage(prefix+"reqBy", locale));
	  lbl.put("reqBu", ExpBrwUtil.getMessage(prefix+"reqBu", locale));
	  lbl.put("reqOu", ExpBrwUtil.getMessage(prefix+"reqOu", locale));
	  lbl.put("createdTime", ExpBrwUtil.getMessage(prefix+"createdTime", locale));
	  lbl.put("createdBy", ExpBrwUtil.getMessage(prefix+"createdBy", locale));
	  lbl.put("telNo", ExpBrwUtil.getMessage(prefix+"telNo", locale));
	  
	  jObj.put("u", lbl);
	  
	  /*
	   * Info
	   */
	  lbl = new JSONObject();
	  prefix = "ex.form.tab.info.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("obj", ExpBrwUtil.getMessage(prefix+"objective", locale));
	  lbl.put("budgetSrc", ExpBrwUtil.getMessage(prefix+"budgetSrc", locale));
	  lbl.put("cc", ExpBrwUtil.getMessage(prefix+"costControl", locale));
	  lbl.put("typeTitle", ExpBrwUtil.getMessage(prefix+"type.title", locale));
	  lbl.put("payTitle", ExpBrwUtil.getMessage(prefix+"pay.title", locale));
	  lbl.put("payEmp", ExpBrwUtil.getMessage(prefix+"pay.employee", locale));	  
	  lbl.put("paySA", ExpBrwUtil.getMessage(prefix+"pay.sa", locale));
	  lbl.put("paySAPrNo", ExpBrwUtil.getMessage(prefix+"pay.prNo", locale));
	  lbl.put("paySup", ExpBrwUtil.getMessage(prefix+"pay.supplier", locale));	  
	  lbl.put("paySupEmpty", ExpBrwUtil.getMessage(prefix+"pay.supplier.empty", locale));	  
	  lbl.put("payAv", ExpBrwUtil.getMessage(prefix+"pay.av", locale));	  
	  lbl.put("payAvNo", ExpBrwUtil.getMessage(prefix+"pay.avNo", locale));	  
	  lbl.put("payIntUnit", ExpBrwUtil.getMessage(prefix+"pay.intUnit", locale));	  
	  lbl.put("payCash", ExpBrwUtil.getMessage(prefix+"pay.cash", locale));	  
	  lbl.put("methodTitle", ExpBrwUtil.getMessage(prefix+"method.title", locale));
	  lbl.put("bankBbl", ExpBrwUtil.getMessage(prefix+"bank.bbl", locale));
	  lbl.put("bank", ExpBrwUtil.getMessage(prefix+"bank", locale));
	  lbl.put("bankWarn", ExpBrwUtil.getMessage(prefix+"bank.warn", locale));
	  lbl.put("isSA", ExpBrwUtil.getMessage(prefix+"isSmallAmount", locale));
	  lbl.put("prNumber", ExpBrwUtil.getMessage(prefix+"prNumber", locale));
	  
	  jObj.put("n", lbl);
	  
	  /*
	   * Item
	   */
	  lbl = new JSONObject();
	  prefix = "ex.form.tab.item.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("actGrp", ExpBrwUtil.getMessage(prefix+"actGrp", locale));
	  lbl.put("act", ExpBrwUtil.getMessage(prefix+"act", locale));
	  lbl.put("asset", ExpBrwUtil.getMessage(prefix+"asset", locale));
	  lbl.put("desc", ExpBrwUtil.getMessage(prefix+"desc", locale));
	  lbl.put("cond", ExpBrwUtil.getMessage(prefix+"cond", locale));
	  lbl.put("pos", ExpBrwUtil.getMessage(prefix+"pos", locale));
	  lbl.put("amt", ExpBrwUtil.getMessage(prefix+"amt", locale));
	  lbl.put("amtAllow", ExpBrwUtil.getMessage(prefix+"amt.allow", locale));
	  lbl.put("total", ExpBrwUtil.getMessage(prefix+"total", locale));
	  
	  lbl.put("isReason", ExpBrwUtil.getMessage(prefix+"is_reason", locale));
	  lbl.put("reason", ExpBrwUtil.getMessage(prefix+"reason", locale));
	  lbl.put("note", ExpBrwUtil.getMessage(prefix+"note", locale));
	  
	  lbl.put("totalExceedPR", ExpBrwUtil.getMessage(prefix+"totalExceedPR", locale));
	  lbl.put("totalZero", ExpBrwUtil.getMessage(prefix+"totalZero", locale));
	  
	  jObj.put("i", lbl);
	  
	  /*
	   * Attendee
	   */
	  lbl = new JSONObject();
	  prefix = "ex.form.tab.att.";
	  lbl.put("lbw", ExpBrwUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("empList", ExpBrwUtil.getMessage(prefix+"empList", locale));
	  lbl.put("othList", ExpBrwUtil.getMessage(prefix+"othList", locale));
	  lbl.put("dupAtt", ExpBrwUtil.getMessage(prefix+"dupAtt", locale));
	  
	  jObj.put("a", lbl);
	  
	  /*
	   * File
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.file.";
	  lbl.put("lbw", ExpUseUtil.getMessage(prefix+"lbw", locale));
	  
	  jObj.put("f", lbl);
	  
	  /*
	   * Status
	   */
	  JSONObject st = new JSONObject();
	  List<Map<String, Object>> list = masterService.listByType(MainMasterConstant.TYPE_EXP_USE_STATUS, null, true, null, 0, 100);
	  for(Map<String, Object> model:list) {
		  lbl = new JSONObject();
		  lbl.put("color", model.get("flag3"));
		  lbl.put("text_en", model.get("flag2"));
		  lbl.put("text_th", model.get("name"));
		  lbl.put("text3_en", model.get("flag5"));
		  lbl.put("text3_th", model.get("flag4"));
		  
		  st.put((String)model.get("code"), lbl);
	  }
	  
	  jObj.put("s", st);
	  
	  String json = jObj.toString();
	  
	  CommonUtil.responseWrite(response, json);
  }  

}
