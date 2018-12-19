package pb.repo.pcm.wscript;


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
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.util.MainUtil;
import pb.repo.pcm.util.PcmReqUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;


@Component
@WebScript
public class PcmMessageWebScript {
	
	private static Logger log = Logger.getLogger(PcmMessageWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/pcm/message";
	
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
		  
		  String msg = PcmReqUtil.getMessage(key, new Locale(lang));
		  
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
	  
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  for(String key : keyArr) {
		  
		  String msg = PcmReqUtil.getMessage(key, locale);
		  
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
  @Uri(URI_PREFIX+"/req")
  public void handleReq(@RequestParam(required=false) String lang,
		  				final WebScriptResponse response) throws IOException, JSONException {
//	  String json = "{a:\"ทดสอบ 2\"}";
	  
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  JSONObject jObj = new JSONObject();
	  /*
	   * Main
	   */
	  JSONObject lbl = new JSONObject();
	  String prefix = "pr.main.";
	  lbl.put("prNo", PcmReqUtil.getMessage(prefix+"prNo", locale));
	  lbl.put("prType", PcmReqUtil.getMessage(prefix+"prType", locale));
	  lbl.put("budget", PcmReqUtil.getMessage(prefix+"budget", locale));
	  lbl.put("objective", PcmReqUtil.getMessage(prefix+"objective", locale));
	  lbl.put("amount", PcmReqUtil.getMessage(prefix+"amount", locale));
	  lbl.put("currency", PcmReqUtil.getMessage(prefix+"currency", locale));
	  lbl.put("requester", PcmReqUtil.getMessage(prefix+"requester", locale));
	  lbl.put("preparer", PcmReqUtil.getMessage(prefix+"preparer", locale));
	  lbl.put("requestTime", PcmReqUtil.getMessage(prefix+"requestTime", locale));
	  lbl.put("status", PcmReqUtil.getMessage(prefix+"status", locale));
	  
	  jObj.put("m", lbl);

	  /*
	   * User Tab
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.usr.";
	  lbl.put("lbw", PcmReqUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("reqBy", PcmReqUtil.getMessage(prefix+"reqBy", locale));
	  lbl.put("reqBu", PcmReqUtil.getMessage(prefix+"reqBu", locale));
	  lbl.put("reqOu", PcmReqUtil.getMessage(prefix+"reqOu", locale));
	  lbl.put("createdTime", PcmReqUtil.getMessage(prefix+"createdTime", locale));
	  lbl.put("createdBy", PcmReqUtil.getMessage(prefix+"createdBy", locale));
	  lbl.put("telNo", PcmReqUtil.getMessage(prefix+"telNo", locale));
	  
	  jObj.put("u", lbl);
	  
	  /*
	   * Info
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.info.";
	  lbl.put("lbw", PcmReqUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("objType", PcmReqUtil.getMessage(prefix+"objectiveType", locale));
	  lbl.put("err_objType", PcmReqUtil.getMessage(prefix+"err.objectiveType", locale));
	  lbl.put("obj", PcmReqUtil.getMessage(prefix+"objective", locale));
	  lbl.put("reason", PcmReqUtil.getMessage(prefix+"reason", locale));
	  lbl.put("reasonOth", PcmReqUtil.getMessage(prefix+"reasonOth", locale));
	  lbl.put("currency", PcmReqUtil.getMessage(prefix+"currency", locale));
	  lbl.put("currencyRate", PcmReqUtil.getMessage(prefix+"currencyRate", locale));
	  lbl.put("budgetSrc", PcmReqUtil.getMessage(prefix+"budgetSrc", locale));
	  lbl.put("invAsset", PcmReqUtil.getMessage(prefix+"invAsset", locale));
	  lbl.put("constr", PcmReqUtil.getMessage(prefix+"construction", locale));
	  lbl.put("isPtt", PcmReqUtil.getMessage(prefix+"isPrototype", locale));
	  lbl.put("err_isPtt", PcmReqUtil.getMessage(prefix+"err.isPrototype", locale));
	  lbl.put("prototypeType", PcmReqUtil.getMessage(prefix+"prototypeType", locale));
	  lbl.put("prototypeNo", PcmReqUtil.getMessage(prefix+"prototypeNo", locale));
	  lbl.put("err_prototypeNo", PcmReqUtil.getMessage(prefix+"err.prototypeNo", locale));
	  lbl.put("contractDate", PcmReqUtil.getMessage(prefix+"contractDate", locale));
	  lbl.put("cc", PcmReqUtil.getMessage(prefix+"costControl", locale));
	  lbl.put("loc", PcmReqUtil.getMessage(prefix+"location", locale));
	  lbl.put("isSA", PcmReqUtil.getMessage(prefix+"isSmallAmount", locale));
	  lbl.put("isAB", PcmReqUtil.getMessage(prefix+"isAcrossBudget", locale));
	  lbl.put("total", PcmReqUtil.getMessage(prefix+"total", locale));
	  lbl.put("err_total", PcmReqUtil.getMessage(prefix+"err.total", locale));
	  lbl.put("isRefId", PcmReqUtil.getMessage(prefix+"isRefId", locale));
	  lbl.put("refId", PcmReqUtil.getMessage(prefix+"refId", locale));
	  lbl.put("err_refId", PcmReqUtil.getMessage(prefix+"err.refId", locale));
	  
	  jObj.put("n", lbl);
	  
	  /*
	   * Item
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.item.";
	  lbl.put("lbw", PcmReqUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("act", PcmReqUtil.getMessage(prefix+"act", locale));
	  lbl.put("actGrp", PcmReqUtil.getMessage(prefix+"actGrp", locale));
	  lbl.put("asset", PcmReqUtil.getMessage(prefix+"asset", locale));
	  lbl.put("name", PcmReqUtil.getMessage(prefix+"name", locale));
	  lbl.put("qty", PcmReqUtil.getMessage(prefix+"qty", locale));
	  lbl.put("uom", PcmReqUtil.getMessage(prefix+"uom", locale));
	  lbl.put("prc", PcmReqUtil.getMessage(prefix+"prc", locale));
      lbl.put("prcCnv", PcmReqUtil.getMessage(prefix+"prcCnv", locale));
      lbl.put("fiscalYear", PcmReqUtil.getMessage(prefix+"fiscalYear", locale));
      lbl.put("subtotal", PcmReqUtil.getMessage(prefix+"subtotal", locale));
      lbl.put("gross", PcmReqUtil.getMessage(prefix+"gross", locale));
      lbl.put("calcVat", PcmReqUtil.getMessage(prefix+"calcVat", locale));
      lbl.put("vat", PcmReqUtil.getMessage(prefix+"vat", locale));
      lbl.put("total", PcmReqUtil.getMessage(prefix+"total", locale));
      
      lbl.put("dlg_prc", PcmReqUtil.getMessage(prefix+"dlg.prc", locale));
      
      lbl.put("noFiscalYear", PcmReqUtil.getMessage(prefix+"noFiscalYear", locale));
      lbl.put("noCmtMember", PcmReqUtil.getMessage(prefix+"noCmtMember", locale));
      lbl.put("totalZero", PcmReqUtil.getMessage(prefix+"totalZero", locale));

      jObj.put("t", lbl);
	  
	  /*
	   * File
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.file.";
	  lbl.put("lbw", PcmReqUtil.getMessage(prefix+"lbw", locale));
	  
	  jObj.put("f", lbl);
	  
	  /*
	   * Committee
	   */
	  lbl = new JSONObject();
	  prefix = "pr.form.tab.cmt.";
	  lbl.put("lbw", PcmReqUtil.getMessage(prefix+"lbw", locale));
	  lbl.put("pcmMethod", PcmReqUtil.getMessage(prefix+"pcmMethod", locale));
	  lbl.put("dupCmt", PcmReqUtil.getMessage(prefix+"dupCmt", locale));
	  
	  jObj.put("c", lbl);
	  
	  /*
	   * Status
	   */
	  JSONObject st = new JSONObject();
	  List<Map<String, Object>> list = masterService.listByType(MainMasterConstant.TYPE_PCM_REQ_STATUS, null, true, null, 0, 100);
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
  @Uri(URI_PREFIX+"/ord")
  public void handleOrd(@RequestParam(required=false) String lang,
		  				final WebScriptResponse response) throws IOException, JSONException {
	  Locale locale = new Locale(CommonUtil.getValidLang(lang));
	  
	  JSONObject jObj = new JSONObject();
	  /*
	   * Main
	   */
	  JSONObject lbl = new JSONObject();
	  String prefix = "pd.main.";
	  lbl.put("search", PcmReqUtil.getMessage(prefix+"search", locale));
	  
	  lbl.put("pdNo", PcmReqUtil.getMessage(prefix+"pdNo", locale));
	  lbl.put("org", PcmReqUtil.getMessage(prefix+"org", locale));
	  lbl.put("objective", PcmReqUtil.getMessage(prefix+"objective", locale));
	  lbl.put("method", PcmReqUtil.getMessage(prefix+"method", locale));
	  lbl.put("total", PcmReqUtil.getMessage(prefix+"total", locale));
	  lbl.put("preparer", PcmReqUtil.getMessage(prefix+"preparer", locale));
	  lbl.put("requestTime", PcmReqUtil.getMessage(prefix+"requestTime", locale));
	  lbl.put("status", PcmReqUtil.getMessage(prefix+"status", locale));

	  jObj.put("m", lbl);
	  
	  /*
	   * Status
	   */
	  JSONObject st = new JSONObject();
	  List<Map<String, Object>> list = masterService.listByType(MainMasterConstant.TYPE_PCM_ORD_STATUS, null, true, null, 0, 100);
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
