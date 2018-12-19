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
import pb.repo.admin.service.AdminAccountTaxService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
//@Authentication(value=AuthenticationType.ADMIN)
public class AdminMainAccountTaxWebScript {
	
	private static Logger log = Logger.getLogger(AdminMainAccountTaxWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/tax";
	
	@Autowired
	private AdminAccountTaxService taxService;


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
  public void handleList(final WebScriptResponse response)  throws Exception {
    
		String json = null;
		
		try {
			List<Map<String, Object>> list = taxService.list();
			
//			Map<String, Object> noVatMap = new HashMap<String, Object>();
//			noVatMap.put(JsonConstant.COMBOBOX_NAME, "ไม่มีภาษีมูลค่าเพิ่ม");
//			noVatMap.put(JsonConstant.COMBOBOX_ID, 0);
//			noVatMap.put(JsonConstant.COMBOBOX_DATA, new HashMap<String, Object>());
//			
//			list.add(0, noVatMap);
			
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
