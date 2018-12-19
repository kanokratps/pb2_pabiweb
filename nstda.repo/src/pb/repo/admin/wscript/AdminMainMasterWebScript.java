package pb.repo.admin.wscript;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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
import pb.repo.admin.util.MainMasterUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
//@Authentication(value=AuthenticationType.ADMIN)
public class AdminMainMasterWebScript {
	
	private static Logger log = Logger.getLogger(AdminMainMasterWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/master";
	
	@Autowired
	private AdminMasterService masterService;


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
  public void handleList(@RequestParam(required=true) final String t
		  			   , @RequestParam(required=false) final String s
		  			   , @RequestParam(required=false) final Boolean active
		  			   , @RequestParam(required=false) final Boolean system
		  			   , @RequestParam(required=false) final Integer start
		  			   , @RequestParam(required=false) final Integer limit
		  			   , final WebScriptResponse response)  throws Exception {
    
		String json = null;
		
		try {
			List<Map<String, Object>> list = masterService.listByType(t, s, active, system, start, limit);
			MainMasterUtil.addAction(list);
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
  @Uri(URI_PREFIX+"/listMasterColumn")
  public void handleListMasterColumn(@RequestParam(required=true) final String t
		  				,@RequestParam(required=true) final String f1
		  			   , final WebScriptResponse response)  throws Exception {
    
		String json = null;
		
		try {
			
			List<MainMasterModel> list = masterService.listColumnSort(t, f1);
			json = MainMasterUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
  @Uri(URI_PREFIX+"/listByAuthType")
  public void listByAuthType(@RequestParam(required=true) final String t, final WebScriptResponse response)  throws Exception {
    
		String type = t;

		String json = null;
		
		try {
			List<MainMasterModel> list = masterService.listByAuthType(type);
			json = MainMasterUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
  @Uri(URI_PREFIX+"/listMasterWithOutMatrix")
  public void listMasterWithOutMatrix(@RequestParam(required=true) final String t, @RequestParam(required=false) final String f1, final WebScriptResponse response)  throws Exception {
    
		String type = t;
		
		String json = null;
		
		try {
			List<MainMasterModel> list = masterService.listMasterWithOutMatrix(type, f1);
			json = MainMasterUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
    
  }
  
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/save")
  public void handleAdd(@RequestParam(required=false) final String id
		  				,@RequestParam final String type
		  				,@RequestParam final String code
		  				,@RequestParam final String name
		  				,@RequestParam final String flag1
		  				,@RequestParam final String flag2
		  				,@RequestParam final String flag3
		  				,@RequestParam final String flag4
		  				,@RequestParam final String flag5
		  				,@RequestParam final Boolean active
		  				,@RequestParam final Boolean system
		  				,final WebScriptResponse response) throws IOException, JSONException {
	
	String json = null;
	
	try {
		MainMasterModel model = new MainMasterModel();
		
		if (CommonUtil.isValidId(id)) {
			model = masterService.get(Long.parseLong(id));
		}
		
		if (model==null) {
			model = new MainMasterModel();
		}
		
		model.setType(type);
		model.setCode(code);
		model.setName(name);
		model.setFlag1(flag1);
		model.setFlag2(flag2);
		model.setFlag3(flag3);
		model.setFlag4(flag4);
		model.setFlag5(flag5);
		model.setActive(active);
		model.setSystem(system);
		
		masterService.save(model);
		
		json = CommonUtil.jsonSuccess();
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	  
  }

  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/delete")
  public void handleDelete(@RequestParam final String id, final WebScriptResponse response) throws Exception {
	String json = null;
	
	try {
		Long idLong = Long.parseLong(id);
		
		masterService.delete(idLong);
		
		json = CommonUtil.jsonSuccess();
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
  }

  @Uri(URI_PREFIX+"/get")
  public void handleGet(@RequestParam final Long id, final WebScriptResponse response)
      throws Exception {
	  
	String json = null;
	 
	try {
	  MainMasterModel model = masterService.get(id);
	  List<MainMasterModel> list = new ArrayList<MainMasterModel>();
	  list.add(model);
		
	  json = MainMasterUtil.jsonSuccess(list);
		
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
		
	}
	
  }

  @Uri(URI_PREFIX+"/listAuthType")
  public void handleListAuthType(@RequestParam final String t, @RequestParam final String f, final WebScriptResponse response) throws Exception {
	
	String json = null;
	
	try {
		List<MainMasterModel> list = masterService.listAuthType(t, f);
		
		Map<String, Object> map = MainMasterUtil.convertToMap(list);
		
		json = CommonUtil.jsonSuccess(map);
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
  }
  
  @Uri(URI_PREFIX+"/listType")
  public void handleListType(@RequestParam final String t, 
		  					 @RequestParam(required=false) final Boolean active,
		  					 @RequestParam(required=false) final Boolean system,
		  					final WebScriptResponse response) throws Exception {
	
	String json = null;
	
	try {
		List<Map<String, Object>> list = masterService.listByType(t, null, active, system, null, null);
		
		JSONArray jsArr = MainMasterUtil.convertToJSONArray(list);
		
		json = CommonUtil.jsonSuccess(jsArr, MainMasterConstant.JFN_CODE, MainMasterConstant.JFN_NAME);
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
  }
  
 
  
//	@Autowired
//	protected ResourceHelper resourceHelper;
//	
//	@Autowired
//	BootstrapService bootstrapService;
//	
//	public void afterPropertiesSet() throws Exception {
//		log.info("**** PBBootstrap work *****");
////		bootstrapService.deployResources(
////				"osgibundle:/META-INF/alfresco/bootstrap/*.xml",
////				new RepositoryLocation(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, "/app:company_home/app:dictionary", SearchService.LANGUAGE_XPATH),
////				new ContentCompareStrategy(resourceHelper), null, null,
////				ContentModel.TYPE_CONTENT);
////		
////      for (String resourceBundle : resourceBundles)
////      {
////          I18NUtil.registerResourceBundle("pb-admin");
////          I18NUtil.registerResourceBundle("alfresco.extension.messages.pb-memo");
////          I18NUtil.registerResourceBundle("alfresco.messages.pb-admin");
////          I18NUtil.registerResourceBundle("alfresco.messages.pb-memo");
////      }
//
//	}

  @Uri(URI_PREFIX+"/showSystem")
  public void handleShowSystem(final WebScriptResponse response) throws Exception, JSONException {
	
	String json = null;
	
	try {
	  	MainMasterModel systemModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_SHOW_SYSTEM_VALUE,false);
		
	  	JSONObject jsObj = new JSONObject();
	  	jsObj.put("showSystem", systemModel!=null && systemModel.getFlag1().equals("1"));
	  	
		json = CommonUtil.jsonSuccess(jsObj);
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	  
  }
}
