package pb.repo.admin.wscript;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.MainSrcUrlService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class MainSrcUrlWebScript {
	
	private static Logger log = Logger.getLogger(MainSrcUrlWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/srcUrl/main";
	
	@Autowired
	private MainSrcUrlService mainSrcUrlService;
	
	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	AuthenticationService authService;
	
	/*
	 * p1 = where condition : type='M'
	 */
	@Uri(URI_PREFIX + "/master")
	public void handleMainMaster(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								,@RequestParam(required=false) final String lang
								, final WebScriptResponse response)
			throws Exception {

		log.info("/master:p1:"+p1+",p2:"+p2+",orderBy:"+orderBy+",all:"+all+",lang:"+lang);
		String json = null;

		try {
			Map<String, Object> map = mainSrcUrlService.listMainMaster(p1, p2, orderBy, all, lang);
			json = CommonUtil.jsonSuccess(map);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	
	/*
	 * p1 = where condition : type='M'
	 */
	@Uri(URI_PREFIX + "/master2")
	public void handleMainMaster2(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								, final WebScriptResponse response)
			throws Exception {

		log.info("/master2:p1:"+p1+",p2:"+p2+",orderBy:"+orderBy+",all:"+all);
		String json = null;

		try {
			List<Map<String, Object>> list = mainSrcUrlService.listMainMaster2(p1, p2, orderBy, all);
			json = CommonUtil.jsonSuccess(list);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}	
	
	/*
	 * p1 = where condition : type='M'
	 */
	@Uri(URI_PREFIX + "/master3")
	public void handleMainMaster3(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								,@RequestParam(required=false) final String lang
								, final WebScriptResponse response)
			throws Exception {

		log.info("/master3:p1:"+p1+",p2:"+p2+",orderBy:"+orderBy+",all:"+all+",lang:"+lang);
		String json = null;

		try {
			Map<String, Object> map = mainSrcUrlService.listMainMaster3(p1, p2, orderBy, all, lang);
			json = CommonUtil.jsonSuccess(map);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}	
	
	/*
	 * p1 = where condition : type='M'
	 * p2 = result field : flag1
	 */
	@Uri(URI_PREFIX + "/masterField")
	public void handleMainMasterField(@RequestParam final String p1, @RequestParam final String p2, final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> field = mainSrcUrlService.getMainMasterField(p1, p2);
			
			json = CommonUtil.jsonSuccess(field);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	/*
	 * t = type
	 * c = code
	 * lang = language
	 */
	@Uri(URI_PREFIX + "/masterValue")
	public void handleMainMasterValue(@RequestParam final String t, 
									  @RequestParam final String c,
									  @RequestParam final String lang,
									  final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> value = mainSrcUrlService.getMainMasterValue(t,c,lang);
			
			json = CommonUtil.jsonSuccess(value);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	/*
	 * p1 = user property : organization
	 */
	@Uri(URI_PREFIX + "/userProfile")
	public void handleUserProfile(@RequestParam final String p1, final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if (p1.equals("fullname")) {
				Object firstName = nodeService.getProperty(personService.getPerson(authService.getCurrentUserName()), ContentModel.PROP_FIRSTNAME);
				Object lastName = nodeService.getProperty(personService.getPerson(authService.getCurrentUserName()), ContentModel.PROP_LASTNAME);
				
				map.put((String)firstName+ (lastName!=null ? " "+(String)lastName : ""), "");
			} else {
				QName qName = CommonConstant.USER_PROFILE_PROP_MAP.get(p1);
				
				Object prop = nodeService.getProperty(personService.getPerson(authService.getCurrentUserName()), qName);
				
				map.put((String)prop, "");
			}
			
			json = CommonUtil.jsonSuccess(map);
			
//			Map<QName,Serializable> props = nodeService.getProperties(personService.getPerson(authService.getCurrentUserName()));
//			for(Entry<QName, Serializable> e : props.entrySet()) {
//				log.info(e.getKey()+":"+e.getValue());
//			}

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	/*
	 * p1 = groupName
	 * p2 = codeValue
	 */
	@Uri(URI_PREFIX + "/userList")
	public void handleUserList(@RequestParam(required=false) final String p1, 
							   @RequestParam(required=false) final String p2,
							   @RequestParam(required=false) final String p3,
							   final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> map = mainSrcUrlService.listUser(p1,p2,p3);
			json = CommonUtil.jsonSuccess(map);

		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	/*
	 * c = code
	 */
	@Uri(URI_PREFIX + "/userName")
	public void handleUserName(@RequestParam final String c,
								  @RequestParam final String lang,
								  final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> value = mainSrcUrlService.getUserName(c,lang);
			
			json = CommonUtil.jsonSuccess(value);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;

		} finally {
			CommonUtil.responseWrite(response, json);

		}
	}
	
	
}
