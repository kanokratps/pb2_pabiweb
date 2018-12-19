package pb.repo.admin.wscript;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminModuleService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Authentication;
import com.github.dynamicextensionsalfresco.webscripts.annotations.AuthenticationType;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
@Authentication(value=AuthenticationType.ADMIN)
public class AdminModuleWebScript {
	
	private static Logger log = Logger.getLogger(AdminModuleWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/module";
	
	@Autowired
	private AdminMasterService masterService;

	@Autowired
	private AdminModuleService moduleService;

  /**
   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws Exception
   */
  @Uri(URI_PREFIX+"/list")
  public void handleList(final WebScriptResponse response)  throws Exception {
    
	  	JSONObject jobj = new JSONObject();
	  	JSONArray modules = new JSONArray();
	  	
//		Properties props = new Properties();
//		props.load(this.getClass().getResourceAsStream("/"+CommonConstant.MODULE_PROP_FILE));

		int pos = CommonConstant.MODULE_PROP_FILE.indexOf(".");
		String resName = CommonConstant.MODULE_PROP_FILE.substring(0, pos);
		ResourceBundle props = ResourceBundle.getBundle(resName);
			
		String[] menus = props.getString("menu.order").split(",");

		for(String menu : menus) {
			
			String key = menu;
			String value = null;
			try {
				value = props.getString(key);
			} catch (Exception ex) {
				// do nothing
			}
			
			if (value != null && value.equals("1")) {
				if (key.equals("admin")) {		
				  	modules.add(getModuleMain());
				}
				else
				if (key.equals("pcm")) {
//				  	modules.add(getModulePcm());
				}
				else
				if (key.equals("exp")) {
//				  	modules.add(getModuleExp());
				}
			}
		}
	  	
	  	jobj.put("modules", modules);
		
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
  
  private JSONObject getModuleMain() throws Exception {
	  	JSONObject module = new JSONObject();
	  	module.put("name", "Main");
	  	module.put("iconCls", "icon_module_main");
	  	
	  	JSONArray items = new JSONArray();
	  	
	  	JSONObject item = new JSONObject();
	  	
	  	item.put("title", "Settings");
	  	item.put("title_th", "ข้อมูลระบบ");
	  	item.put("xtype", "adminMainSettingsMain");
	  	item.put("store", "main.SettingsGridStore");
	  	
	  	items.add(item);
	  	
	  	MainMasterModel systemModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_SHOW_SYSTEM_VALUE,false);
	  	item.put("showSystem",  systemModel!=null && systemModel.getFlag1().equals("1"));

//	  	MainMasterModel sysConfModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ADMIN_AM);
//	  	if (sysConfModel!=null && sysConfModel.getFlag1().equals("1")) {
//		  	item = new JSONObject();
//		  	
//		  	item.put("title", "Approval Matrix Form");
//		  	item.put("title_th", "ฟอร์มสายงานอนุมัติ");
//		  	item.put("xtype", "adminMainApprovalMatrixFormatMain");
//		  	item.put("store", "main.ApprovalMatrixFormatGridStore");
//		  	
//		  	items.add(item);
//	  	}
	  	
//	  	item = new JSONObject();
//	  	
//	  	item.put("title", "Delegate");
//	  	item.put("title_th", "Delegate");
//	  	item.put("xtype", "adminMemoDelegateMain");
//	  	item.put("store", "memo.DelegateGridStore");
//	  	items.add(item);
	  	
	  	item = new JSONObject();
	  	
	  	item.put("title", "Utility");
	  	item.put("title_th", "Utility");
	  	item.put("xtype", "adminMainUtilMain");
	  	
	  	items.add(item);
	  	
	  	
	  	item = new JSONObject();
	  	
	  	item.put("title", "Workflow");
	  	item.put("title_th", "Workflow");
	  	item.put("xtype", "adminMainWorkflowMain");
	  	
	  	items.add(item);
	  	
	  	
//	  	item = new JSONObject();
//	  	
//	  	item.put("title", "Log");
//	  	item.put("title_th", "Log");
//	  	item.put("xtype", "adminMainLogMain");
//	  	
//	  	items.add(item);
	  	
	  	
	  	item = new JSONObject();
	  	
	  	item.put("title", "System");
	  	item.put("title_th", "ทดสอบระบบ");
	  	item.put("xtype", "adminMainTestSystemMain");
	  	
	  	items.add(item);
	  	
	  	module.put("items", items);

		return module;  
  }
  
  private JSONObject getModulePcm() throws Exception {
	  	JSONObject module = new JSONObject();
	  	module.put("name", "PCM");
	  	module.put("iconCls", "icon_module_pcm");
	  	
	  	JSONArray items = new JSONArray();
	  	
	  	JSONObject item; 

//	  	item = new JSONObject();
//	  	
//	  	item.put("title", "Approval Matrix");
//	  	item.put("title_th", "สายอนุมัติ");
//	  	item.put("xtype", "adminMainApprovalMatrixMain");
//	  	item.put("store", "main.ApprovalMatrixGridStore");
//	  	item.put("MODULE", CommonConstant.MODULE_PCM);
//	  	
//	  	items.add(item);
	  	
	  	module.put("items", items);

		return module;  
  }
  
  
  private JSONObject getModuleExp() throws Exception {
	  	JSONObject module = new JSONObject();
	  	module.put("name", "EXP");
	  	module.put("iconCls", "icon_module_exp");
	  	
	  	JSONArray items = new JSONArray();
	  	
	  	JSONObject item; 
	  	
//	  	item = new JSONObject();
//	  	
//	  	item.put("title", "Approval Matrix");
//	  	item.put("title_th", "สายอนุมัติ");
//	  	item.put("xtype", "adminMainApprovalMatrixMain");
//	  	item.put("store", "main.ApprovalMatrixGridStore");
//	  	item.put("MODULE", CommonConstant.MODULE_EXP);
//	  	
//	  	items.add(item);
	  	
	  	module.put("items", items);

		return module;  
  }

  /**
   * Handles the "list" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
   * and request handling objects.
   * 
   * @param response
   * @throws Exception
   */
  @Uri(URI_PREFIX+"/listForCmb")
  public void handleListForCmb(final WebScriptResponse response)  throws Exception {
    
	  	JSONObject jobj = new JSONObject();
	  	JSONArray modules = new JSONArray();
	  	
		int pos = CommonConstant.MODULE_PROP_FILE.indexOf(".");
		String resName = CommonConstant.MODULE_PROP_FILE.substring(0, pos);
		ResourceBundle props = ResourceBundle.getBundle(resName);
			
		String[] menus = props.getString("menu.order").split(",");

		for(String menu : menus) {
			
			String key = menu;
			String value = null;
			try {
				value = props.getString(key);
			} catch (Exception ex) {
				// do nothing
			}
			
			if (value != null && value.equals("1")) {
				if (key.equals("pcm")) {
					JSONObject o = new JSONObject();
					o.put("id",CommonConstant.MODULE_PCM);
					o.put("name",CommonConstant.MODULE_PCM);
				  	modules.add(o);
				}
			}
			if (value != null && value.equals("1")) {
				if (key.equals("exp")) {
					JSONObject o = new JSONObject();
					o.put("id",CommonConstant.MODULE_EXP);
					o.put("name",CommonConstant.MODULE_EXP);
				  	modules.add(o);
				}
			}
		}
	  	
	  	jobj.put("data", modules);
		
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
  
}
