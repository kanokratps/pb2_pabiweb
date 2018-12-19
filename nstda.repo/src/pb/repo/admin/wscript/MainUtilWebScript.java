package pb.repo.admin.wscript;

import java.util.List;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.model.FileModel;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.model.MainMsgModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminMsgService;
import pb.repo.admin.service.AlfrescoService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class MainUtilWebScript {
	
	private static Logger log = Logger.getLogger(MainUtilWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/util";
	
	@Autowired
	AlfrescoService alfrescoService;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminMsgService msgService;
	
	@Autowired
	AuthenticationService authService;
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/getFolderName")
	public void handleGetFolderName(@RequestParam(required=false) String n
			, final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			log.info("folderRef="+n);
			
			NodeRef folderRef = new NodeRef(n);
			
			String folderName = alfrescoService.getFolderPath(folderRef);
			
			log.info("folderName="+folderName);
			
		    JSONObject jsObj = new JSONObject();
		    jsObj.put("name", folderName.replaceAll("/", "%2F"));
			
			json = CommonUtil.jsonSuccess(jsObj);
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
	
	@Uri(URI_PREFIX+"/gotoFolder")
	public void handleGotoFolder(@RequestParam(required=false) String n
			, final WebScriptResponse response)  throws Exception {
		
		String html = null;

		try {
			log.info("folderRef="+n);
			
			NodeRef folderRef = new NodeRef(n);
			
			String folderName = alfrescoService.getFolderPath(folderRef);
			
			log.info("folderName="+folderName);
			
			String context = CommonUtil.getGlobalProperty(CommonConstant.GP_SHARE_CONTEXT);
			String host = CommonUtil.getGlobalProperty(CommonConstant.GP_SHARE_HOST);
			String port = CommonUtil.getGlobalProperty(CommonConstant.GP_SHARE_PORT);
			String protocol = CommonUtil.getGlobalProperty(CommonConstant.GP_SHARE_PROTOCOL);
			
			html = "<html><script>function a(){window.location=\""+protocol+"://"+host+":"+port+"/"+context+"/page/repository#filter=path%7C"+folderName.replaceAll("/", "%2F")+"%7C\"}</script><body onload=\"a()\"></body></html>";
		} catch (Exception ex) {
			log.error("", ex);
			html = "<html><body>"+ex.toString()+"</body></html>";
			throw ex;
			
		} finally {
			CommonUtil.responseWriteHtml(response, html);
		}
		
	}
	
	@Uri(URI_PREFIX+"/getPageSize")
	public void handleGetPageSize(final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		try {
			MainMasterModel model = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_PAGING_SIZE,false);
			
			JSONObject jsObj = new JSONObject();
			jsObj.put("pageSize", Integer.parseInt(model.getFlag1()));
			
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	@Uri(URI_PREFIX+"/flashMsg")
	public void handleFlashMsg(final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		try {
			JSONObject jsObj = new JSONObject();
			
			List<MainMsgModel> list = msgService.listByUser(authService.getCurrentUserName());
			if (!list.isEmpty()) {
				MainMsgModel msgModel = list.get(0);
				jsObj.put("msg", msgModel.getMsg());
				
				msgService.delete(authService.getCurrentUserName());
			}
			
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	@Uri(method=HttpMethod.GET, value=URI_PREFIX+"/getFolderDtl")
	public void handleGetFolderDtl(@RequestParam(required=false) String n
			, @RequestParam(required=false) String lang
			, final WebScriptResponse response)  throws Exception {
		
		String json = null;

		try {
			log.info("folderRef="+n);
			
			NodeRef folderRef = new NodeRef(n);
			
			List<FileModel> fileList = alfrescoService.listFile(folderRef,lang);
			
			log.info("fileList="+fileList);
			
			json = CommonUtil.jsonSuccess(fileList);
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
