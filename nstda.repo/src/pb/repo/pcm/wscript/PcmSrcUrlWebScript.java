package pb.repo.pcm.wscript;

import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.service.AdminPurchaseTypeService;
import pb.repo.pcm.service.PcmOrdService;
import pb.repo.pcm.service.PcmReqService;
import pb.repo.pcm.service.PcmSrcUrlService;

import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class PcmSrcUrlWebScript {
	
	private static Logger log = Logger.getLogger(PcmSrcUrlWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/srcUrl/pcm";
	
	@Autowired
	private PcmSrcUrlService srcUrlService;
	
	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	private PcmOrdService pcmOrdService;

	@Autowired
	private PcmReqService pcmReqService;
	
	@Autowired
	AdminPurchaseTypeService adminPurchaseTypeService;
	
	/*
	 * p1 = where condition : type='M'
	 * p2 = join condition M.flag2=A.field4
	 * p3 = code Value : 'n'=name, other=code
	 */
	@Uri(URI_PREFIX + "/master")
	public void handleMaster(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String p3
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								,@RequestParam(required=false, defaultValue="C") final String formMode
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleMaster:p1:"+p1+",p2:"+p2+",p3:"+p3+",orderBy:"+orderBy+",all:"+all);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.listMainMaster(p1, orderBy, all, p2, p3, false, formMode);
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
	 * p2 = join condition M.flag2=A.field4
	 */
	@Uri(URI_PREFIX + "/master/requester")
	public void handleMasterReq(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String p3
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
			                    ,@RequestParam(required=false, defaultValue="C") final String formMode
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleMasterReq:p1:"+p1+",p2:"+p2+",p3:"+p3+",orderBy:"+orderBy+",all:"+all);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.listMainMaster(p1, orderBy, all, p2, p3, true, formMode);
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
	public void handleMasterField(@RequestParam final String p1,
			                      @RequestParam final String p2,
			                      @RequestParam(required=false) final String p3,
			                      @RequestParam(required=false, defaultValue="C") final String formMode,
			                      final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> field = srcUrlService.getMainMasterField(p1, p2, p3, false, formMode);
			
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
	 * p1 = where condition : type='M'
	 * p2 = result field : flag1
	 */
	@Uri(URI_PREFIX + "/masterField/requester")
	public void handleMasterFieldReq(@RequestParam final String p1,
								     @RequestParam final String p2,
								     @RequestParam(required=false) final String p3,
				                     @RequestParam(required=false, defaultValue="C") final String formMode,
								     final WebScriptResponse response)
			throws Exception {

		String json = null;

		try {
			Map<String, Object> field = srcUrlService.getMainMasterField(p1, p2, p3, true, formMode);
			
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
	 * p1 = where condition : type='M'
	 * p2 = join condition M.flag2=A.field4
	 * p3 = code Value : 'n'=name, other=code
	 */
	@Uri(URI_PREFIX + "/list")
	public void handleList(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String p3
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								,@RequestParam(required=false, defaultValue="C") final String formMode
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleList:p1:"+p1+",p2:"+p2+",p3:"+p3+",orderBy:"+orderBy+",all:"+all);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.listMemo(p1, orderBy, all, p2, p3, false, formMode);
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
	 * p2 = join condition M.flag2=A.field4
	 * p3 = code Value : 'n'=name, other=code
	 */
	@Uri(URI_PREFIX + "/view/list")
	public void handleViewList(@RequestParam final String p1
								,@RequestParam(required=false) final String p2
								,@RequestParam(required=false) final String p3
								,@RequestParam(required=false) final String orderBy
								,@RequestParam(required=false) final Boolean all
								,@RequestParam(required=false, defaultValue="C") final String formMode
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleViewList:p1:"+p1+",p2:"+p2+",p3:"+p3+",orderBy:"+orderBy+",all:"+all);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.listViewMemo(p1, orderBy, all, p2, p3, false, formMode);
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
	 * p1 = where condition : master_id='PR16000001' and description='NAME'
	 */
	@Uri(URI_PREFIX + "/dtl/field")
	public void handleDtlField(@RequestParam final String p1
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleDtlField:p1:"+p1);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.getMemoDtlField(p1);
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
	 * id = PR id
	 */
	@Uri(URI_PREFIX + "/field/total")
	public void handleFieldTotal(@RequestParam final String id
								, @RequestParam(required=false) final String lang
								, final WebScriptResponse response)
			throws Exception {

		log.info("handleFieldTotal:id:"+id+","+lang);
		String json = null;

		try {
			Map<String, Object> map = srcUrlService.getMemoFieldTotal(id,lang);
			json = CommonUtil.jsonSuccess(map);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}	
	
	  @Uri(URI_PREFIX+"/ord/method/list")
	  public void handleMethodList(@RequestParam(required=false) final String lang
			  					   ,final WebScriptResponse response)  throws Exception {

			String json = null;
			
			try {
				JSONArray jsArr = pcmOrdService.listMethod(lang);
				json = CommonUtil.jsonSuccess(jsArr);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
				
			}
	    
	  }
	  
	  @Uri(URI_PREFIX+"/objType/list")
	  public void handleObjTypeList(@RequestParam(required=false) final String lang
			  					   ,@RequestParam(required=false) final Boolean all
			  					   ,final WebScriptResponse response)  throws Exception {

			String json = null;
			
			try {
				JSONArray jsArr = pcmReqService.listObjectiveType(all,lang);
				json = CommonUtil.jsonSuccess(jsArr);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
				
			}
	    
	  }
	  
	  @Uri(URI_PREFIX+"/objType/value")
	  public void handleObjTypeValue(@RequestParam(required=false) final String lang
			  					   ,@RequestParam(required=false) final Integer id
			  					   ,final WebScriptResponse response)  throws Exception {

			String json = null;
			
			try {
				Map<String, Object> map = adminPurchaseTypeService.get(id);
				
				map.put("data", map.get("name"+(lang!=null && lang.startsWith("th") ? "_th" : "")));
				
				json = CommonUtil.jsonSuccess(map);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
				
			}
	    
	  }
}
