package pb.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.alibaba.fastjson.JSON;

import pb.common.constant.CommonConstant;
import pb.common.constant.JsonConstant;
import pb.common.model.ResultModel;

public class CommonUtil {
	
	private static Logger log = Logger.getLogger(CommonUtil.class);
	
	protected NodeService nodeService;
//    private FileFolderService fileFolderService;
//    private SearchService searchService;
//    private ContentService contentService;
//    private TransactionService transactionService;
//	private PersonService personService;
//	private AuthenticationService authService;
//	private AuthorityService authorityService;
	
	public void setNodeService(NodeService nodeService) {this.nodeService = nodeService;}
//	public void setAuthorityService(AuthorityService authorityService){this.authorityService = authorityService;}
//	public void setAuthenticationService(AuthenticationService authService){this.authService = authService;}
//	public void setFileFolderService(FileFolderService fileFolderService) {this.fileFolderService = fileFolderService;}
//	public void setContentService(ContentService contentService) {this.contentService = contentService;}
//	public void setTransactionService(TransactionService transactionService){this.transactionService = transactionService;}
//	public void setSearchService(SearchService searchService) {this.searchService = searchService;}
//	public void setPersonService(PersonService personService) {this.personService = personService;}
	
	private static Properties globalProperties;
	
	static {
		globalProperties = new Properties();
		
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("alfresco-global.properties");
		
		try {
			globalProperties.load(inputStream);
		} catch (IOException ex) {
		     log.error("", ex);
		} finally {
			 if (inputStream != null) {
			    try {
			       inputStream.close();
			    } catch (IOException e) {
			       log.error("", e);
			    }
			 }
		}
	}
	
	public static Properties getGlobalProperties() { 
		return globalProperties;
	}
	
	public static String getGlobalProperty(String key) {
		return globalProperties.getProperty(key);
	}
	
	public static JSONArray convertToCmbJSONArray(Map<String, Object> map) throws Exception {
		
		JSONArray jsArr = new JSONArray();
		JSONObject jsObj;
		
		for(Entry<String, Object> e : map.entrySet()) {
			
			jsObj = new JSONObject();
				
			jsObj.put(JsonConstant.COMBOBOX_ID, e.getKey());
			jsObj.put(JsonConstant.COMBOBOX_NAME, e.getValue());
			
			jsArr.put(jsObj);
			
		}
		
		return jsArr;
	}
	
	public static JSONArray convertToCmbJSONArray(JSONArray data, String keyField, String valueField) throws Exception {
		
		JSONArray jsArr = new JSONArray();
		JSONObject jsObj;
		JSONObject dataObj;
		
		for(int i=0; i<data.length(); i++) {
			dataObj = data.getJSONObject(i);
			
			jsObj = new JSONObject();
				
			jsObj.put(JsonConstant.COMBOBOX_ID, dataObj.get(keyField));
			jsObj.put(JsonConstant.COMBOBOX_NAME, dataObj.get(valueField));
			
			jsArr.put(jsObj);
			
		}
		
		return jsArr;
	}
	
	public static String jsonSuccess() throws JSONException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(JSONObject data) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA, data);
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(JSONArray data) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA, data);
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(Object data) {

		Long total = null;
		
		if(data instanceof List) {
			List list = (List)data;
			if (list.size()>0) {
				Object obj = list.get(0);
				if (obj instanceof Map) {
					Map map = (Map)obj;
					total = (Long)map.get("totalrowcount");
				}
			}
		}
		
		ResultModel resultModel;
		if(total!=null) {
			resultModel = new ResultModel(true, data, total);
		} else {
			resultModel = new ResultModel(true, data);
		}
		
		return JSON.toJSONString(resultModel);
	}	
	
	public static String jsonSuccess(Map<String ,Object> map) throws Exception {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA, convertToCmbJSONArray(map));
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(JSONArray data, String keyField, String valueField) throws Exception {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA, convertToCmbJSONArray(data, keyField, valueField));
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccessEmptyList() throws Exception {
		Map<String ,Object> map = new HashMap<String, Object>();
		
		return jsonSuccess(map);
	}
	
	public static String jsonFail(JSONObject jsObj) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  false);
		jsonObj.put(JsonConstant.DATA, jsObj);
		
		return jsonObj.toString();
	}
	
	public static String jsonFail(String message) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  false);
		jsonObj.put(JsonConstant.MESSAGE, message);
		
		return jsonObj.toString();
	}
	
	public static void responseWrite(final WebScriptResponse response, String msg) throws IOException {
        response.setContentType("application/json");
        response.setContentEncoding("utf-8");
        response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(msg);
	}
	
	public static void responseWriteHtml(final WebScriptResponse response, String html) throws IOException {
        response.setContentType("text/html");
        response.setContentEncoding("utf-8");
        response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(html);
	}
	
	public static String jsonMessage(String msg) throws JSONException {
		
		JSONObject msgObj = new JSONObject();

		msgObj.put(JsonConstant.CODE, msg.split(",")[0]);
		msgObj.put(JsonConstant.MESSAGE, msg.split(",")[1]);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(msgObj);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA,  jsonArray);
		
		return jsonObj.toString();
	}
	
	public static String jsonMessage(List<String> msgs) throws JSONException {

		JSONArray jsonArray = new JSONArray();
		for(String msg : msgs) {
			JSONObject msgObj = new JSONObject();
	
			msgObj.put(JsonConstant.CODE, msg.split(",")[0]);
			msgObj.put(JsonConstant.MESSAGE, msg.split(",")[1]);
			
			jsonArray.put(msgObj);
		}
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA,  jsonArray);
		
		return jsonObj.toString();
	}

	public static Boolean isValidId(String id) {
		return (id != null && !id.trim().equals("") && !id.equalsIgnoreCase("null"));
	}
	
	public static String getMessage(String module, String key, Locale locale) {
		  
		String resName = "alfresco.extension.messages.pb-"+module.toLowerCase();
		
		String[] localeArr = locale.toString().split("_");
		
		String lang = localeArr[0];
		
		if (!lang.startsWith(CommonConstant.ENGLISH)) {
			resName += "_"+lang;
		}
		
		try {
			ResourceBundle r = ResourceBundle.getBundle(resName);
			
			return r.getString(key);
			
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static String getInvalidKeyMsg(String key) {
		return key+",Invalid Key : "+key;
	}
	
	public static String getValidLang(String lang) {
		if (lang!=null) {
			String[] ls = lang.split("_");
			lang = ls[0];
		} else {
			lang = "en";
		}
		  
		return lang;
	}
	
	public static Map removeThElement(Map<String, Object> map) {
		List<String> list = new ArrayList<String>();
		for (String key : map.keySet()) {
			if (key.endsWith("_th")) {
				list.add(key);
			}
		}
		
		for(String key : list) {
			map.remove(key);
		}
		
		return map;
	}
	
	public static String formatMoney(Double money) {
		DecimalFormat df = new DecimalFormat(CommonConstant.MONEY_FORMAT);
		return df.format(money != null ? money : null);
	}
	
	public static void checkNull(Object obj, String msg) throws Exception {
		if (obj==null) {
			log.info(msg);
			throw new Exception(msg);
		}
	}
}
