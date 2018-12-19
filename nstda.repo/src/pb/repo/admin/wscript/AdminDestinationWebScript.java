package pb.repo.admin.wscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AdminBudgetSrcService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class AdminDestinationWebScript {
	
	private static Logger log = Logger.getLogger(AdminDestinationWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/admin/main/destination";

	@Autowired
	AdminBudgetSrcService budgetSrcService;
	
	@Uri(URI_PREFIX+"/list")
	public void handleList(@RequestParam String t,
						   @RequestParam(required=false) String s,
			 final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
			if(t.equals("D")) {
				map = new HashMap<String, Object>();
				map.put("id",  1);
				map.put("name", "กรุงเทพฯ");
				list.add(map);
				
				map = new HashMap<String, Object>();
				map.put("id",  2);
				map.put("name", "เชียงใหม่");
				list.add(map);

				map = new HashMap<String, Object>();
				map.put("id",  3);
				map.put("name", "ภูเก็ต");
				list.add(map);
			} else {
				map = new HashMap<String, Object>();
				map.put("id",  1);
				map.put("name", "USA");
				list.add(map);
				
				map = new HashMap<String, Object>();
				map.put("id",  2);
				map.put("name", "China");
				list.add(map);

				map = new HashMap<String, Object>();
				map.put("id",  3);
				map.put("name", "Japan");
				list.add(map);
			}
			
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
