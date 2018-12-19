package pb.repo.admin.wscript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminUserGroupService;

@Component
@WebScript
public class AdminMenuWebScript {
	
	private static Logger log = Logger.getLogger(AdminMenuWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/menu";
	
	@Autowired
	private AdminMasterService masterService;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private PersonService personService;
	
	@Autowired
	AdminUserGroupService adminUserGroupService;
	
	@Uri(URI_PREFIX+"/allow")
	public void handleAllow(final WebScriptRequest request
			  ,final WebScriptResponse response)  throws Exception {
		  
	  	String json = null;
	  	
		try {
			JSONObject jsObj = new JSONObject();
			
			MainMasterModel model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_HOME, false);
			jsObj.put("home", CommonConstant.V_ENABLE.equals(model.getFlag1()));
			
			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_MY_FILES, false);
			jsObj.put("myFiles", CommonConstant.V_ENABLE.equals(model.getFlag1()));

			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_SHARED_FILES, false);
			jsObj.put("sharedFiles", CommonConstant.V_ENABLE.equals(model.getFlag1()));

			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_SITES, false);
			jsObj.put("sites", CommonConstant.V_ENABLE.equals(model.getFlag1()));
			
			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_TASKS, false);
			jsObj.put("tasks", CommonConstant.V_ENABLE.equals(model.getFlag1()));

			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_PEOPLE, false);
			jsObj.put("people", CommonConstant.V_ENABLE.equals(model.getFlag1()));

			model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ALF_MENU_REPOSITORY, false);
			jsObj.put("repository", CommonConstant.V_ENABLE.equals(model.getFlag1()));
			
			json = jsObj.toString();
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	  /**
	   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
	   * and request handling objects.
	   * 
	   * @param response
	   * @throws Exception
	   */
	  @Uri(URI_PREFIX+"/widgets")
	  public void handleWidgets(@RequestParam final String lang
			  ,final WebScriptRequest request
			  ,final WebScriptResponse response)  throws Exception {
		  log.info("/widgets:"+authService.getCurrentUserName());
		  processMenus(getMenus(), lang, response);
	  }
	  
	  private void logGroups(List<String> groups) {
		  StringBuffer grps = new StringBuffer();
		  for(String g:groups) {
			  if (grps.length()>0) {
				  grps.append(",");
			  }
			  grps.append(g);
		  }
		  log.info(grps.toString());
	  }
	  
	  private List<MainMasterModel> getMenus() {
		  Map<String, Boolean> allow = new HashMap<String, Boolean>();

		  allow.put("PCM_REQ", true);
		  allow.put("PCM_ORD", false);
		  allow.put("PCM_USE", true);
		  
		  allow.put("EXP_USE", true);
		  allow.put("EXP_BRW", true);
		  
		  allow.put("HR_SAL", false);
		  
		  List<String> groups = adminUserGroupService.listRole(authService.getCurrentUserName());
		  logGroups(groups);
		  
		  if (isProcurement(groups)) {
			  allow.put("PCM_ORD", true);
		  }
		  if (isHR(groups)) {
			  allow.put("HR_SAL", true);
		  }
		  
		  return getMenus(allow);
	  }
	  
	  private List<MainMasterModel> getMenus(Map<String,Boolean> allow) {
		  List<MainMasterModel> menu = new ArrayList<MainMasterModel>();
		  
		  menu.add(getMenu("HEADER_PB_ADMIN","10","header.menu.pb-admin.label","I","","admin","icon:\"admin\",admin:\"1\""));
		  
		  if (allow.get("PCM_REQ") || allow.get("PCM_ORD") || allow.get("PCM_USE")) {
			  menu.add(getMenu("HEADER_PB_PCM","20","header.menu.pb-pcm.label","M","","","icon:\"pcm\""));
			  if (allow.get("PCM_REQ")) {
				  menu.add(getMenu("HEADER_PB_PCM_REQ","21","header.menu.pb-pcm-req.label","I","20","pcm-req","icon:\"pcm\""));
			  }
			  if (allow.get("PCM_ORD")) {
				  menu.add(getMenu("HEADER_PB_PCM_ORD","22","header.menu.pb-pcm-ord.label","I","20","pcm-ord","icon:\"pcm\""));
			  }
			  if (allow.get("PCM_USE")) {
				  MainMasterModel urlModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_STOCK_REQUEST_URL,false);
//				  menu.add(getMenu("HEADER_PB_PCM_USE","23","header.menu.pb-pcm-use.label","I","20","https://pabi2o.nstda.or.th/stock_request","icon:\"pcm\",targetNew:\"1\""));
//				  menu.add(getMenu("HEADER_PB_PCM_USE","23","header.menu.pb-pcm-use.label","I","20","https://pabi2o-test.intra.nstda.or.th/stock_request","icon:\"pcm\",targetNew:\"1\""));
				  menu.add(getMenu("HEADER_PB_PCM_USE","23","header.menu.pb-pcm-use.label","I","20",urlModel.getFlag1(),"icon:\"pcm\",targetNew:\"1\""));
			  }
		  }

		  if (allow.get("EXP_USE") || allow.get("EXP_BRW")) {
			  menu.add(getMenu("HEADER_PB_EXP","30","header.menu.pb-exp.label","M","","","icon:\"exp\""));
			  if (allow.get("EXP_USE")) {
				  menu.add(getMenu("HEADER_PB_EXP_USE","31","header.menu.pb-exp-use.label","I","30","exp-use","icon:\"exp\""));
			  }
			  if (allow.get("EXP_BRW")) {
				  menu.add(getMenu("HEADER_PB_EXP_BRW","32","header.menu.pb-exp-brw.label","I","30","exp-brw","icon:\"exp\""));
			  }
		  }
		  
		  if (allow.get("HR_SAL")) {
			  menu.add(getMenu("HEADER_PB_HR","40","header.menu.pb-hr.label","M","","","icon:\"hr\""));
			  menu.add(getMenu("HEADER_PB_HR_SAL","41","header.menu.pb-hr-sal.label","I","40","hr-sal","icon:\"hr\""));
		  }
		  
		  return menu;
	  }
	  
	  private MainMasterModel getMenu(String id, String code, String name, String type, String parentCode, String url, String spcParam) {
		  MainMasterModel menu = new MainMasterModel();
		  
		  menu.setType(id);
		  menu.setCode(code);
		  menu.setName(name);
		  menu.setFlag1(type);
		  menu.setFlag2(parentCode);
		  menu.setFlag3(url);
		  menu.setFlag5(spcParam);
		  
		  return menu;
	  }
	  
	  private boolean isGroup(List<String> groups, String group) {
		  boolean found = false;
		  
		  MainMasterModel topGroup = masterService.getSystemConfig(group,false);
		  String[] tgs = topGroup!=null && topGroup.getFlag1()!=null ? topGroup.getFlag1().split(",") : null;
		  List<String> list = Arrays.asList(tgs);
		  
		  for(String tg:list) {
//			  log.info("tg:"+tg);
			  for(String g:groups) {
//				  log.info(" g:"+g);
				  if (g.equals(tg)) {
					  log.info(tg);
					  found = true;
					  break;
				  }
			  }
		  }
		  
		  return found;
	  }
	  
	  private boolean isProcurement(List<String> groups) {
		  return isGroup(groups, MainMasterConstant.SCC_PCM_ORD_TOP_GROUP);
	  }
	  
	  private boolean isHR(List<String> groups) {
		  return isGroup(groups, MainMasterConstant.SCC_HR_SAL_TOP_GROUP);
	  }

	  @Uri(URI_PREFIX+"/widgets-budget")
	  public void handleWidgetsBudget(@RequestParam final String lang
			  ,final WebScriptRequest request
			  ,final WebScriptResponse response)  throws Exception {
		  log.info("/widgets-budget:"+authService.getCurrentUserName());
		  processMenus(getMenusBudget(), lang, response);
	  }
	  
	  private void processMenus(List<MainMasterModel> menus, final String lang, final WebScriptResponse response) throws Exception {
		  
		  	JSONObject jobj = new JSONObject();
		  	JSONArray widgets = new JSONArray();
		  	
		  	JSONObject widget = null;
		  	JSONObject config = null;
		  	
		  	Map<String, JSONObject> parents = new HashMap<String, JSONObject>();
		  	
		  	for(MainMasterModel menu : menus) {
			  	String spcParams = ((String)menu.getFlag5());
			  	JSONObject params = null;
			  	if (spcParams!=null && !spcParams.equals("")) {
			  		try {
			  			params = new JSONObject("{"+spcParams+"}");
			  		} catch (Exception ex) {
			  			log.error(ex);
			  		}
			  	}
			  	
			  	Boolean allow = true;
			  	
			  	String admin = null;
			  	try {
			  		admin = params.getString("admin");
			  	} catch (Exception ex) {
			  		// do nothing
			  	}
			  	if (admin!=null && admin.equals(CommonConstant.V_ENABLE)) {
			  		allow = authorityService.hasAdminAuthority();
			  	}
			  	
//			  	log.info("name:"+menu.getName()+":"+allow);
			  	
			  	if (allow) {
			  	
				  	widget = new JSONObject();
				  	config = new JSONObject();
				  	
				  	String id = menu.getType();
				  	String code = menu.getCode();
				  	String name = menu.getName();
				  	String type = ((String)menu.getFlag1()).toUpperCase();
				  	String parentCode = ((String)menu.getFlag2());
				  	String url = ((String)menu.getFlag3());
				  	
				  	widget.put("type", type);
				  	
//				  	config.put("label","header.menu.bgear-"+code+".label");
//				  	config.put("label", translator.getLabel(name, LangUtil.getShortLang(lang)));
				  	config.put("label", name);
				  	
				  	String icon = "";
				  	try {
				  		icon = params.getString("icon");
				  	} catch (Exception ex) {
				  		// do nothing
				  	}
				  	
				  	widget.put("id", id);
				  	config.put("iconClass","alf-pb-"+icon+"-icon");
				  	
				  	widget.put("config", config);
				  	
				  	if (type.equals("G")) {
				  		widget.put("name", "alfresco/menus/AlfMenuGroup");
				  	}
				  	else
				  	if (type.equals("I")) {
				  		widget.put("name", "alfresco/header/AlfMenuItem");
				  		
					  	if (url!=null && !url.equals("")) {
					  		config.put("targetUrl",url);
						  	String targetNew = null;
						  	try {
						  		targetNew = params.getString("targetNew");
						  	} catch (Exception ex) {
						  		// do nothing
						  	}
					  		if (targetNew!=null) {
					  			config.put("targetUrlType","FULL_PATH");
					  			config.put("targetUrlLocation","NEW");
					  		}
					  	}
				  	}
				  	else
				  	if (type.equals("M")) {
				  		widget.put("name", "alfresco/header/AlfCascadingMenu");
				  		JSONArray cws = new JSONArray();
				  		JSONObject cw = new JSONObject();
				  		
				  		cw.put("name", "alfresco/menus/AlfMenuGroup");
				  		cw.put("config", new JSONObject());
				  		
				  		cws.put(cw);
				  		config.put("widgets", cws);
				  		
				  		parents.put(code+"G", cw);
				  	}
				  	
				  	if (parentCode==null || parentCode.equals("")) {
				  		widgets.put(widget);
				  		
				  		if (type.equals("I")) {
				  			widget.put("name", "alfresco/header/AlfMenuItem");
				  		}
				  	}
				  	
				  	parents.put(code,widget);
			  	
			  	}
		  	}
		  	
		  	for(MainMasterModel menu : menus) {
		  		String parentCode = (String)menu.getFlag2(); 
		  		if (parentCode!=null && !parentCode.equals("")) {
		  			widget = parents.get(menu.getCode());

		  			if (widget!=null) {
			  			JSONObject parent = parents.get(parentCode);
		
			  			if (parent!=null) {
			  				if (parent.get("type").equals("M")) {
			  					parent = (JSONObject)parents.get(parentCode+"G");
			  				}
			  				
			  				config = parent.getJSONObject("config");
			  				
				  			JSONArray parentWidgets = null;
				  			try {
				  				parentWidgets = (JSONArray)config.get("widgets");
				  			} catch (Exception ex) {
				  				// do nothing
				  			}
				  			if (parentWidgets==null) {
				  				parentWidgets = new JSONArray();
				  			}
				  			
				  			parentWidgets.put(widget);
				  			
				  			config.put("widgets", parentWidgets);
			  			}
		  			}
		  		}
		  	}
		  	
		  	jobj.put("widgets", widgets);

		  	String json = null;
		  	
			try {
				json = jobj.toString();
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
			}
	  }
	  
	  private List<MainMasterModel> getMenusBudget() {
		  Map<String, Boolean> allow = new HashMap<String, Boolean>();

		  allow.put("BGT_PLAN", true);
		  allow.put("BGT_REVISE", true);
		  allow.put("BGT_TRANSFER", true);
		  
		  return getMenusBudget(allow);
	  }
	  
	  private List<MainMasterModel> getMenusBudget(Map<String,Boolean> allow) {
		  List<MainMasterModel> menu = new ArrayList<MainMasterModel>();
		  
		  if (allow.get("BGT_PLAN")) {
			  MainMasterModel urlModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_BGT_PLAN_URL,false);
			  menu.add(getMenu("HEADER_PB_BGT_PLAN","50","header.menu.pb-bgt-plan.label","I","",urlModel.getFlag1(),"icon:\"bgt\",targetNew:\"1\""));
		  }
		  if (allow.get("BGT_REVISE")) {
			  MainMasterModel urlModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_BGT_REVISE_URL,false);
			  menu.add(getMenu("HEADER_PB_BGT_REVISE","51","header.menu.pb-bgt-revise.label","I","",urlModel.getFlag1(),"icon:\"bgt\",targetNew:\"1\""));
		  }
		  if (allow.get("BGT_TRANSFER")) {
			  MainMasterModel urlModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_BGT_TRANSFER_URL,false);
			  menu.add(getMenu("HEADER_PB_BGT_TRANSFER","52","header.menu.pb-bgt-transfer.label","I","",urlModel.getFlag1(),"icon:\"bgt\",targetNew:\"1\""));
		  }

		  return menu;
	  }
}
