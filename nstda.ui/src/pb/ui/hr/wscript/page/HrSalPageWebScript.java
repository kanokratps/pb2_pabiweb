package pb.ui.hr.wscript.page;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptSession;
import org.springframework.stereotype.Component;

import pb.ui.admin.constant.AdminUiConstant;

@Component
public class HrSalPageWebScript extends DeclarativeWebScript {
	
//	 @Autowired
//	 MainUserService userService;
	
	 @Override
	 protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		 WebScriptSession session = req.getRuntime().getSession();
		 
//		 StringBuffer sb = new StringBuffer();
		 session.removeValue(AdminUiConstant.SESSION_PAGE_UUID);
		 UUID uuid = UUID.randomUUID();
		 session.setValue(AdminUiConstant.SESSION_PAGE_UUID, uuid);
		 
	     Map<String, Object> map = new HashMap<String, Object>();
	     map.put("result", "T");
	     
	     String tasks = "S";
//	     if (userService.isUserInGroup(ThreadLocalRequestContext.getRequestContext(), "MEMO_ADMIN")) {
//	    	 tasks += "S";
//	     }
	     
	     map.put("tasks", tasks);
	     
	     map.put("lang", "th");
	     
	     return map;
	     
	 }
}