package pb.repo.pcm.wscript;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.TemplateService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.exception.FolderNoPermissionException;
import pb.common.model.FileModel;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.common.util.FileUtil;
import pb.common.util.FolderUtil;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWkfConfigDocTypeConstant;
import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminAccountFiscalYearService;
import pb.repo.admin.service.AdminHrEmployeeService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminModuleService;
import pb.repo.admin.service.AdminTestSystemService;
import pb.repo.admin.service.AdminUserGroupService;
import pb.repo.admin.service.AdminWorkflowService;
import pb.repo.admin.service.InterfaceService;
import pb.repo.admin.util.MainUtil;
import pb.repo.pcm.model.PcmReqDtlModel;
import pb.repo.pcm.model.PcmReqModel;
import pb.repo.pcm.service.PcmReqService;
import pb.repo.pcm.service.PcmReqWorkflowService;
import pb.repo.pcm.util.PcmReqCmtDtlUtil;
import pb.repo.pcm.util.PcmReqCmtHdrUtil;
import pb.repo.pcm.util.PcmReqCmtUtil;
import pb.repo.pcm.util.PcmReqDtlUtil;
import pb.repo.pcm.util.PcmReqUtil;
import pb.repo.pcm.util.PcmUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class PcmWebScript {
	
	private static Logger log = Logger.getLogger(PcmWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/pcm";
	
	@Autowired
	private PcmReqService pcmReqService;
	
	@Autowired
	TemplateService templateService;

	@Autowired
	private PcmReqWorkflowService mainWorkflowService;
	
	@Autowired
	private AdminMasterService masterService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private AdminUserGroupService userGroupService;
	
	@Autowired
	private AdminTestSystemService adminTestSystemService;
	
	@Autowired
	private AdminHrEmployeeService adminHrEmployeeService;
	
	@Autowired
	private AdminWorkflowService adminWorkflowService;
	
	@Autowired
	private AdminModuleService moduleService;
	
	@Autowired
	AdminAccountFiscalYearService fiscalYearService;
	
	@Autowired
	@Qualifier("mainInterfaceService")
	InterfaceService interfaceService;
	
	@Autowired
	DataSource dataSource;
	
  @Uri(URI_PREFIX+"/req/list")
  public void handleList(@RequestParam(required=false) final String s
		  	  , @RequestParam(required=false) final String fields
			  , @RequestParam(required=false) final Integer start
			  , @RequestParam(required=false) final Integer limit
			  , @RequestParam(required=false) final String sort
			  , @RequestParam(required=false) final String lang
			  , final WebScriptResponse response)  throws Exception {

	  	/*
	  	 * Prepare Criteria
	  	 */
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (s != null && !s.equals("")) {
    		String[] terms = s.split(" ");
        	
    		params.put("terms", terms);
		}
		params.put("start", start);
		params.put("limit", limit);
		
		String curUser = authService.getCurrentUserName();
		params.put("loginE", curUser);
//		params.put("loginL", "%,"+curUser+",%");
		params.put("loginL", curUser);
		
		String userRoles = userGroupService.getAuthoritiesForUser(curUser)
						.replace("GROUP_", "")
						.replace("','", ",")
						;
		if (userRoles.startsWith("'")) {
			userRoles = userRoles.substring(1);
		}
		if (userRoles.endsWith("'")) {
			userRoles = userRoles.substring(0, userRoles.length()-1);
		}
		
		String[] roles = userRoles.split(",");
		List<String> roleList = new ArrayList<String>();
		for(int i=0; i<roles.length; i++) {
			if (!roles[i].startsWith("site_")) {
//				roleList.add("%,"+roles[i]+",%");
				roleList.add(roles[i]);
			}
		}
		params.put("roleList",  roleList);
		
		
		if (fields != null) {
			JSONObject jsObj = new JSONObject(fields);
			
			putOneParam(params, jsObj, PcmReqConstant.JFN_STATUS, null);
			putOneParam(params, jsObj, PcmReqConstant.JFN_OBJECTIVE_TYPE, "N");
		}		

		// Order By
		StringBuffer orderBy = new StringBuffer();
		StringBuffer oriOrderBy = new StringBuffer();
		
		if (sort!=null) {
			JSONArray sortArr = new JSONArray(sort);
			for(int i=0; i<sortArr.length(); i++) {
				if (oriOrderBy.length()>0) {
					oriOrderBy.append(",");
				}
				JSONObject sortObj = sortArr.getJSONObject(i);
				oriOrderBy.append(sortObj.getString("property"));
				oriOrderBy.append(" ");
				oriOrderBy.append(sortObj.getString("direction"));
			}
		} else {
			MainMasterModel orderByModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_ORDER_BY,false);
			oriOrderBy.append(orderByModel.getFlag1());
		}
		
		String[] orders = oriOrderBy.toString().split(",");
		for(int i=0; i<orders.length; i++) {
			if (orderBy.length()>0) {
				orderBy.append(",");
			}
			
			String[] os = orders[i].trim().split(" ");
			
			String f = os[0];
			if (f.equals("wfstatus")) {
				f = "wf_status";
			}
			else
			if (f.equals("created_time_show")) {
				f = "created_time";
			}
			else
			if (f.equals("requested_time_show")) {
				f = "requested_time";
			}
			
			orderBy.append(f);
			orderBy.append((f.indexOf("_time")<0 
						 && f.indexOf("_date")<0) 
						 && !f.equalsIgnoreCase("order_field") 
						 && !f.equalsIgnoreCase("total") 
						 ? " collate \"C\" " : " "
			);
			if (os.length>1) {
				orderBy.append(os[os.length-1]);
			}
		}
		
		log.info("orderBy:"+orderBy.toString());
		params.put("orderBy", orderBy.toString());
		
		params.put("lang", lang!=null && lang.startsWith("th") ? "_th" : "");
		
		MainMasterModel monitorUserModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_MONITOR_USER,false);
		if (monitorUserModel!=null && monitorUserModel.getFlag1()!=null) {
			String mu = ","+monitorUserModel.getFlag1()+",";
			if (mu.indexOf(","+curUser+",") >= 0) {
				params.put("monitorUser", "1");
			}
		}
	  
		/*
		 * Search
		 */
		String json = null;
		
		try {
			List<Map<String, Object>> list = pcmReqService.list(params);
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
  }
  
  @Uri(URI_PREFIX+"/req/listForSearch")
  public void handleListForSearch(@RequestParam(required=false) final String s
		  	  , @RequestParam(required=false) final String fields
			  , @RequestParam(required=false) final Integer start
			  , @RequestParam(required=false) final Integer limit
			  , @RequestParam(required=false) final String lang
			  , final WebScriptResponse response)  throws Exception {

	  	/*
	  	 * Prepare Criteria
	  	 */
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (s != null && !s.equals("")) {
    		String[] terms = s.split(" ");
        	
    		params.put("terms", terms);
		}
		params.put("start", start);
		params.put("limit", limit);
		
		String curUser = authService.getCurrentUserName();
		params.put("loginE", curUser);
//		params.put("loginL", "%,"+curUser+",%");
		params.put("loginL", curUser);
		
		String userRoles = userGroupService.getAuthoritiesForUser(curUser)
						.replace("GROUP_", "")
						.replace("','", ",")
						;
		if (userRoles.startsWith("'")) {
			userRoles = userRoles.substring(1);
		}
		if (userRoles.endsWith("'")) {
			userRoles = userRoles.substring(0, userRoles.length()-1);
		}
		
		String[] roles = userRoles.split(",");
		List<String> roleList = new ArrayList<String>();
		for(int i=0; i<roles.length; i++) {
			if (!roles[i].startsWith("site_")) {
//				roleList.add("%,"+roles[i]+",%");
				roleList.add(roles[i]);
			}
		}
		params.put("roleList",  roleList);
		
		if (fields != null) {
			JSONObject jsObj = new JSONObject(fields);
			
			putOneParam(params, jsObj, PcmReqConstant.JFN_STATUS, null);
			putOneParam(params, jsObj, PcmReqConstant.JFN_OBJECTIVE_TYPE, "N");
		}		
		
		params.put("orderBy", "REQ.id DESC");
		
		params.put("lang", lang!=null && lang.startsWith("th") ? "_th" : "");
		
	  
		/*
		 * Search
		 */
		String json = null;
		
		try {
			List<Map<String, Object>> list = pcmReqService.listForSearch(params);
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
  }
  
  @Uri(URI_PREFIX+"/req/old/list")
  public void handleListOld(@RequestParam final String r
	  	  	  , @RequestParam final String bgt
		  	  , @RequestParam(required=false) final String bg
			  , @RequestParam(required=false) final String f
			  , @RequestParam(required=false) final String exid
			  , @RequestParam(required=false) final String avid
			  , final WebScriptResponse response)  throws Exception {

	  	/*
	  	 * Prepare Criteria
	  	 */
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("r", r);
		params.put("bgt", bgt);
		params.put("bg", bg!=null && !bg.equals("") ? Integer.parseInt(bg) : 0);
		params.put("f", f!=null && !f.equals("") ? Integer.parseInt(f) : 0);
		
		log.info("exid:"+exid+", avid:"+avid);
		if (exid!=null) {
			params.put("exid", exid);
		}
		if (avid!=null) {
			params.put("avid", avid);
		}
		
		params.put("orderBy", "id");
	  
		/*
		 * Search
		 */
		String json = null;
		
		try {
			List<Map<String,Object>> list = pcmReqService.listOld(params);
			
//			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//			
//			Map map = new HashMap<String, Object>();
//			map.put("id","PR18000097");
//			map.put("objective_type_name","ABC");
//			map.put("objective","DEF");
//			map.put("total_cnv",1000);
//			list.add(map);
//			
//			map = new HashMap<String, Object>();
//			map.put("id","PR18000098");
//			map.put("objective_type_name","ABCD");
//			map.put("objective","DEFG");
//			map.put("total_cnv",2000);
//			list.add(map);
			
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
  }  
  
  private void putOneParam(Map<String, Object> params, JSONObject jsObj, String fieldName, String type) {
	  try {
		String field = jsObj.getString(fieldName);  
		if (field!=null && !field.equals("") && !field.equals("null")) {
			if (type!=null && type.equals("N")) {
				params.put(fieldName, Long.parseLong(field));
			}
			else {
				params.put(fieldName, field);
			}
		}
	  } catch (Exception ex) {
		  // do nothing
	  }
  }
  
  @Transaction(value=TransactionType.REQUIRES_NEW)
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/save")
  public void handleSave(@RequestParam(required=false) final String id
						,@RequestParam(required=false) final String reqBy
		  				,@RequestParam(required=false) final String reqOu
		  				,@RequestParam(required=false) final String objectiveType
		  				,@RequestParam(required=false) final String objective
		  				,@RequestParam(required=false) final String reason
		  				,@RequestParam(required=false) final String currency
		  				,@RequestParam(required=false) final String currencyRate
						,@RequestParam(required=false) final String budgetCc
						,@RequestParam(required=false) final String budgetCcType
						,@RequestParam(required=false) final String fundId
						,@RequestParam(required=false) final String isStock
						,@RequestParam(required=false) final String stockOu
						,@RequestParam(required=false) final String isPrototype
						,@RequestParam(required=false) final String prototypeType
						,@RequestParam(required=false) final String prototypeNo
						,@RequestParam(required=false) final String costControlTypeId
						,@RequestParam(required=false) final String costControlId
						,@RequestParam(required=false) final String pcmOu
						,@RequestParam(required=false) final String contractDate
						,@RequestParam(required=false) final String location
						,@RequestParam(required=false) final String isSmallAmount
						,@RequestParam(required=false) final String isAcrossBudget
						,@RequestParam(required=false) final String isRefId
						,@RequestParam(required=false) final String refId
						,@RequestParam(required=false) final String method
						,@RequestParam(required=false) final String methodCond2Rule
						,@RequestParam(required=false) final String methodCond2
						,@RequestParam(required=false) final String methodCond2Dtl
						,@RequestParam(required=false) final String vat
						,@RequestParam(required=false) final String vatId
						,@RequestParam(required=false) final String priceInclude
						,@RequestParam(required=false) final String total
						,@RequestParam(required=false) final String totalCnv
		  				,@RequestParam(required=false) final String items
		  				,@RequestParam(required=false) final String files
		  				,@RequestParam(required=false) final String cmts
						,@RequestParam(required=false) String lang
		  				,final WebScriptResponse response) throws Exception {
	
	log.info("/save"); 
	 
	String json = null;
	
	SqlSession session = PcmUtil.openSession(dataSource);
	
	try {
		PcmReqModel model = null;
		
		if (CommonUtil.isValidId(id)) {
			model = pcmReqService.get(id, null);
		}
		
		if (model==null) {
			model = new PcmReqModel();
		}
		
		model.setReqBy(reqBy);
		if (reqOu != null && !reqOu.equals("")) {
			model.setReqSectionId(Integer.parseInt(reqOu));
		}
		if (objectiveType != null && !objectiveType.equals("")) {
			model.setObjectiveType(Integer.parseInt(objectiveType));
		}
		model.setObjective(objective);
		model.setReason(reason);
		model.setCurrency(currency);
		model.setCurrencyRate(Double.parseDouble(currencyRate));
		if (budgetCc != null && !budgetCc.equals("")) {
			model.setBudgetCc(Integer.parseInt(budgetCc));
		}
		model.setBudgetCcType(budgetCcType);
		if (fundId != null && !fundId.equals("")) {
			model.setFundId(Integer.parseInt(fundId));
		}
		model.setIsStock(isStock);
		if (stockOu != null && !stockOu.equals("")) {
			model.setStockSectionId(Integer.parseInt(stockOu));
		}
		model.setIsPrototype(isPrototype);
		model.setPrototypeType(prototypeType);
		model.setPrototypeNo(prototypeNo);
		model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
		model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
		if (pcmOu != null && !pcmOu.equals("")) {
			model.setPcmSectionId(Integer.parseInt(pcmOu));
		}
		model.setContractDate(CommonDateTimeUtil.convertSenchaStringToTimestamp(contractDate));
		model.setLocation(location);
		model.setIsSmallAmount(isSmallAmount);
		model.setIsAcrossBudget(isAcrossBudget);
		if (isAcrossBudget != null && isAcrossBudget.equals("1")) {
			model.setAcrossBudget(Double.parseDouble(totalCnv));
		}
		model.setIsRefId(isRefId);
		model.setRefId(refId);
		model.setPrWebMethodId(method!=null && !method.equals("") ? Long.parseLong(method) : null);
		model.setMethodCond2Rule(methodCond2Rule);
		model.setMethodCond2(methodCond2);
		model.setMethodCond2Dtl(methodCond2Dtl);
		model.setVat(Double.parseDouble(vat));
		model.setVatId(Integer.parseInt(vatId!=null && !vatId.equals("") ? vatId : "0"));
		model.setPriceInclude(Boolean.parseBoolean(priceInclude!=null && !priceInclude.equals("") ? priceInclude : "false"));
		model.setTotal(Double.parseDouble(total!=null && !total.equals("")  ? total : "0"));
		model.setTotalCnv(Double.parseDouble(totalCnv!=null && !totalCnv.equals("")  ? totalCnv : "0"));
		
		if (model.getStatus()!=null && model.getStatus().equals(PcmReqConstant.ST_WAITING)) {
			JSONObject data = new JSONObject();
			data.put("valid", false);
			data.put("msg", "PR status is Waiting for approval");
			data.put("close", true);
			json = CommonUtil.jsonFail(data);
			session.rollback();
		} else {
			Map<String,Object> wfModel = adminWorkflowService.getByDocId(model.getId());
			if (wfModel!=null) {
				JSONObject data = new JSONObject();
				data.put("valid", false);
				data.put("msg", "PR has already been sent to Approver");
				data.put("close", true);
				json = CommonUtil.jsonFail(data);
				session.rollback();
			} else {
				model.setStatus(PcmReqConstant.ST_DRAFT);
				
				JSONObject jobj = new JSONObject(model); 
				log.info("model="+jobj.toString());
				
				model = pcmReqService.save(session, model, items, files, cmts, false);
				
				JSONObject jsObj = new JSONObject();
				jsObj.put("id", model.getId());
				jsObj.put("status", model.getStatus());
				
				json = CommonUtil.jsonSuccess(jsObj);
				session.commit();
			}
		}

	} catch (FolderNoPermissionException ex) {
		session.rollback();
		
    	if (ex.getMessage().startsWith("PB_ERR:")) {
    		String[] s = ex.getMessage().split(":");
    		String errMsg = MainUtil.getMessageWithOutCode(s[1], new Locale(lang))+s[2];
    		
    		JSONObject jsObj = new JSONObject();
    		jsObj.put("msg", errMsg);
    		jsObj.put("valid", false);
    		
			json = CommonUtil.jsonFail(jsObj);
    	} else {
			json = CommonUtil.jsonFail(ex.getMessage());
    	}	
	} catch (Exception ex) {
		session.rollback();
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		session.close();
		CommonUtil.responseWrite(response, json);
	}
	  
  }

  @Transaction(value=TransactionType.REQUIRES_NEW)
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/send")
  public void handleSendToReview(@RequestParam(required=false) final String id
								,@RequestParam(required=false) final String reqBy
								,@RequestParam(required=false) final String reqOu
								,@RequestParam(required=false) final String objectiveType
								,@RequestParam(required=false) final String objective
								,@RequestParam(required=false) final String reason
								,@RequestParam(required=false) final String currency
								,@RequestParam(required=false) final String currencyRate
								,@RequestParam(required=false) final String budgetCc
								,@RequestParam(required=false) final String budgetCcType
								,@RequestParam(required=false) final String fundId
								,@RequestParam(required=false) final String isStock
								,@RequestParam(required=false) final String stockOu
								,@RequestParam(required=false) final String isPrototype
								,@RequestParam(required=false) final String prototypeType
								,@RequestParam(required=false) final String prototypeNo
								,@RequestParam(required=false) final String costControlTypeId
								,@RequestParam(required=false) final String costControlId
								,@RequestParam(required=false) final String pcmOu
								,@RequestParam(required=false) final String contractDate
								,@RequestParam(required=false) final String location
								,@RequestParam(required=false) final String isSmallAmount
								,@RequestParam(required=false) final String isAcrossBudget
								,@RequestParam(required=false) final String isRefId
								,@RequestParam(required=false) final String refId
								,@RequestParam(required=false) final String method
								,@RequestParam(required=false) final String methodCond2Rule
								,@RequestParam(required=false) final String methodCond2
								,@RequestParam(required=false) final String methodCond2Dtl
								,@RequestParam(required=false) final String vat
								,@RequestParam(required=false) final String vatId
								,@RequestParam(required=false) final String priceInclude
								,@RequestParam(required=false) final String total
								,@RequestParam(required=false) final String totalCnv
								,@RequestParam(required=false) final String status
								,@RequestParam(required=false) final String items
								,@RequestParam(required=false) final String files
				  				,@RequestParam(required=false) final String cmts
				  				,@RequestParam(required=false) String lang
		  				,final WebScriptResponse response) throws Exception {

	log.info("/send"); 

	String json = null;
	
	SqlSession session = PcmUtil.openSession(dataSource);
	
	try {
		PcmReqModel model = null;
		
		if (CommonUtil.isValidId(id)) {
			model = pcmReqService.get(id, null);
		}
		
		if (model==null) {
			model = new PcmReqModel();
		}
		model.setWaitingLevel(0);
		
		model.setReqBy(reqBy);
		if (reqOu != null && !reqOu.equals("")) {
			model.setReqSectionId(Integer.parseInt(reqOu));
		}
		if (objectiveType != null && !objectiveType.equals("")) {
			model.setObjectiveType(Integer.parseInt(objectiveType));
		}
		model.setObjective(objective);
		model.setReason(reason);
		model.setCurrency(currency);
		model.setCurrencyRate(Double.parseDouble(currencyRate));
		if (budgetCc!=null && !budgetCc.equals("")) {
			model.setBudgetCc(Integer.parseInt(budgetCc));
		}
		model.setBudgetCcType(budgetCcType);
		if (fundId != null && !fundId.equals("")) {
			model.setFundId(Integer.parseInt(fundId));
		}
		model.setIsStock(isStock);
		if (stockOu != null && !stockOu.equals("")) {
			model.setStockSectionId(Integer.parseInt(stockOu));
		}
		model.setIsPrototype(isPrototype);
		model.setPrototypeType(prototypeType);
		model.setPrototypeNo(prototypeNo);
		model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
		model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
		if (pcmOu!=null && !pcmOu.equals("")) {
			model.setPcmSectionId(Integer.parseInt(pcmOu));
		}
		model.setContractDate(CommonDateTimeUtil.convertSenchaStringToTimestamp(contractDate));
		model.setLocation(location);
		model.setIsSmallAmount(isSmallAmount);
		model.setIsAcrossBudget(isAcrossBudget);
		if (isAcrossBudget!=null && isAcrossBudget.equals("1")) {
			model.setAcrossBudget(Double.parseDouble(totalCnv));
		}
		model.setIsRefId(isRefId);
		model.setRefId(refId);
		model.setPrWebMethodId(method!=null && !method.equals("") ? Long.parseLong(method) : null);
		model.setMethodCond2Rule(methodCond2Rule);
		model.setMethodCond2(methodCond2);
		model.setMethodCond2Dtl(methodCond2Dtl);
		model.setVat(Double.parseDouble(vat));
		model.setVatId(Integer.parseInt(vatId!=null && !vatId.equals("") ? vatId : "0"));
		model.setPriceInclude(Boolean.parseBoolean(priceInclude!=null && !priceInclude.equals("") ? priceInclude : "false"));
		model.setTotal(Double.parseDouble(total));
		model.setTotalCnv(Double.parseDouble(totalCnv));
		model.setStatus(status);
		model.setRequestedTime(CommonDateTimeUtil.now());

		
		JSONObject jobj = new JSONObject(model); 
		log.info("model="+jobj.toString());
		
//		JSONObject validateResult = pcmReqService.validateAssignee(model);
//		if (!(Boolean)validateResult.get("valid")) {
//			json = CommonUtil.jsonFail(validateResult);
//		}
//		else {
			mainWorkflowService.setModuleService(pcmReqService);
			Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_PR, model);
			JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
			if (!(Boolean)validateWfPath.get("valid")) {
				json = CommonUtil.jsonFail(validateWfPath);
			}
			else {
				model = pcmReqService.setPcmSectionInfo(model);
				JSONObject validateNextActor = mainWorkflowService.validateNextActor(model);
				if (!(Boolean)validateNextActor.get("valid")) {
					json = CommonUtil.jsonFail(validateNextActor);
				} else {
				
					MainMasterModel chkBudgetModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_INF_CHECK_BUDGET);
					
					Boolean checkBudget = chkBudgetModel.getFlag1().equals(CommonConstant.V_ENABLE);
					Boolean budgetOk = false;
					String budgetResult = null;
					if (checkBudget) {
						Map<String, Object> budget = moduleService.getTotalPreBudget(budgetCcType, model.getBudgetCc(), model.getFundId(), null, null, false);
						
						if ((Boolean)budget.get("checkBudget")) {
	
							List<PcmReqDtlModel> dtlList = PcmReqDtlUtil.convertJsonToList(items, model.getId());
							Double rate = model.getCurrencyRate();
							
							Double checkTotal = model.getTotalCnv();
							if (model.getIsAcrossBudget().equals("1")) {
								Map<String, Object> fiscalYear = fiscalYearService.getCurrent();
								for(PcmReqDtlModel d : dtlList) {
									if (d.getFiscalYear().equals(fiscalYear.get("fiscalyear"))) {
										checkTotal = (d.getTotal()+(model.getVat()*d.getTotal()))*rate;
										break;
									}
								}
							}
							
							log.info("balance:"+Double.parseDouble(((String)budget.get("balance")).replaceAll(",", "")));
							log.info("checkTotal:"+checkTotal);
							
							budgetOk = Double.parseDouble(((String)budget.get("balance")).replaceAll(",", "")) >= checkTotal;
							if (!budgetOk) {
								budgetResult = MainUtil.getMessageWithOutCode("ERR_WF_BUDGET_NOT_ENOUGH", new Locale(lang));
							} else {
								
								if (model.getBudgetCcType().equals("P")) {
									
									List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
									for(PcmReqDtlModel dtl : dtlList) {
										
										Map<String, Object> map = new HashMap<String,Object>();
										
										map.put("activity_rpt_id", dtl.getActId());
										if (model.getPriceInclude()) {
											map.put("amount", dtl.getTotal() * rate);
										} else {
											map.put("amount", (dtl.getTotal() + (model.getVat()*dtl.getTotal())) * rate);
										}
		
										if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
											map.put("select_asset_id", dtl.getAssetRuleId());
										}
										
										list.add(map);
									}
		
									JSONObject vResult = moduleService.checkBudget("pr",model.getBudgetCcType(), model.getBudgetCc(), model.getFundId(), list);
									
									if (!(Boolean)vResult.get("valid")) {
										budgetOk = false;
										budgetResult = (String)vResult.get("msg");
									}
								}
							
		//						Map<Integer, Map<String, Object>> sumDtls = new HashMap<Integer, Map<String,Object>>();
		//					
		//						for(PcmReqDtlModel dtl : dtlList) {
		//							if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
		//								JSONObject vResult = moduleService.validateAssetPrice(dtl.getAssetRuleId(), (dtl.getTotal()+(model.getVat()*dtl.getTotal())) * rate);
		//								if (!(Boolean)vResult.get("valid")) {
		//									budgetOk = false;
		//									budgetResult = (String)vResult.get("msg");
		//									break;
		//								}
		//							}
		//							
		//							if (model.getBudgetCcType().equals("P")) {
		//								
		//								Map<String, Object> map = sumDtls.get(dtl.getActId());
		//								Double oldTotal = 0.0;
		//								if (map==null) {
		//									map = new HashMap<String,Object>();
		//								} else {
		//									oldTotal = (Double)map.get("amount");
		//								}
		//								
		//								map.put("fund_id", model.getFundId());
		//								map.put("project_id", model.getBudgetCc());
		//								map.put("activity_rpt_id", dtl.getActId());
		//								map.put("amount", oldTotal + (dtl.getTotal() + (model.getVat()*dtl.getTotal())) * rate);
		//								
		//								sumDtls.put(dtl.getActId(), map);
		//							}
		//						}
		//						
		//						if (budgetOk) {
		//							if (model.getBudgetCcType().equals("P")) {
		//								List<Map<String,Object>> list = new ArrayList();
		//								
		//								for(Entry<Integer,Map<String,Object>> e : sumDtls.entrySet()) {
		//									list.add((Map<String,Object>)e.getValue());
		//								}
		//								JSONObject vResult = moduleService.checkFundSpending(list);
		//								if (!(Boolean)vResult.get("valid")) {
		//									budgetOk = false;
		//									budgetResult = (String)vResult.get("msg");
		//								}
		//							}		
		//						}
							}
						
		//					Double budgetBalance = interfaceService.getBudget(model.getBudgetCcType(), model.getBudgetCc(), model.getFundId(), model.getCreatedBy());
		//					log.info("Budget Balance:"+budgetBalance);
		//					budgetOk = (Boolean)budgetResult.get("budget_ok");
						} else {
							budgetOk = true;
						}
					}
					
					if (checkBudget && !budgetOk) {
						JSONObject data = new JSONObject();
						data.put("valid", false);
						data.put("msg", budgetResult);
						json = CommonUtil.jsonFail(data);
					} else {
						if (model.getStatus()!=null && model.getStatus().equals(PcmReqConstant.ST_WAITING)) {
							JSONObject data = new JSONObject();
							data.put("valid", false);
							data.put("msg", "PR status is Waiting for approval");
							data.put("close", true);
							json = CommonUtil.jsonFail(data);
						} else {
							if (model.getId() == null || (status!=null && status.equals(PcmReqConstant.ST_DRAFT))) {
								model.setStatus(PcmReqConstant.ST_WAITING);
								model.setWaitingLevel(1);
							}
							
							model = pcmReqService.save(session, model, items, files, cmts, true);
							
							Map<String,Object> wfModel = adminWorkflowService.getByDocId(model.getId());
							if (wfModel!=null) {
								JSONObject data = new JSONObject();
								data.put("valid", false);
								data.put("msg", "PR has already been sent to Approver");
								data.put("close", true);
								json = CommonUtil.jsonFail(data);
								session.rollback();
							} else {
								mainWorkflowService.startWorkflow(session, model, bossMap);
								
								JSONObject jsObj = new JSONObject();
								jsObj.put("id", model.getId());
								json = CommonUtil.jsonSuccess(jsObj);
					            session.commit();
							}
						}
					}
				}
			}
//		}
		
	} catch (FolderNoPermissionException ex) {
		session.rollback();
		
    	if (ex.getMessage().startsWith("PB_ERR:")) {
    		String[] s = ex.getMessage().split(":");
    		String errMsg = MainUtil.getMessageWithOutCode(s[1], new Locale(lang))+s[2];
    		
    		JSONObject jsObj = new JSONObject();
    		jsObj.put("msg", errMsg);
    		jsObj.put("valid", false);
    		
			json = CommonUtil.jsonFail(jsObj);
    	} else {
			json = CommonUtil.jsonFail(ex.getMessage());
    	}		
    	
	} catch (Exception ex) {
    	session.rollback();
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
    	session.close();
		CommonUtil.responseWrite(response, json);
	}
	  
  }
  
  @Transaction(value=TransactionType.REQUIRES_NEW)
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/am")
  public void handleAM(@RequestParam(required=false) final String id
								,@RequestParam(required=false) final String reqBy
								,@RequestParam(required=false) final String reqOu
								,@RequestParam(required=false) final String objectiveType
								,@RequestParam(required=false) final String objective
								,@RequestParam(required=false) final String reason
								,@RequestParam(required=false) final String currency
								,@RequestParam(required=false) final String currencyRate
								,@RequestParam(required=false) final String budgetCc
								,@RequestParam(required=false) final String budgetCcType
								,@RequestParam(required=false) final String fundId
								,@RequestParam(required=false) final String isStock
								,@RequestParam(required=false) final String stockOu
								,@RequestParam(required=false) final String isPrototype
								,@RequestParam(required=false) final String prototypeType
								,@RequestParam(required=false) final String prototypeNo
								,@RequestParam(required=false) final String costControlTypeId
								,@RequestParam(required=false) final String costControlId
								,@RequestParam(required=false) final String pcmOu
								,@RequestParam(required=false) final String contractDate
								,@RequestParam(required=false) final String location
								,@RequestParam(required=false) final String isSmallAmount
								,@RequestParam(required=false) final String isAcrossBudget
								,@RequestParam(required=false) final String isRefId
								,@RequestParam(required=false) final String refId
								,@RequestParam(required=false) final String method
								,@RequestParam(required=false) final String methodCond2Rule
								,@RequestParam(required=false) final String methodCond2
								,@RequestParam(required=false) final String methodCond2Dtl
								,@RequestParam(required=false) final String vat
								,@RequestParam(required=false) final String vatId
								,@RequestParam(required=false) final String priceInclude
								,@RequestParam(required=false) final String total
								,@RequestParam(required=false) final String totalCnv
								,@RequestParam(required=false) final String status
								,@RequestParam(required=false) final String items
								,@RequestParam(required=false) final String files
				  				,@RequestParam(required=false) final String cmts
				  				,@RequestParam(required=false) String lang
		  				,final WebScriptResponse response) throws Exception {

	String json = null;
	
	SqlSession session = PcmUtil.openSession(dataSource);
	
	try {
		PcmReqModel model = null;
		
//		if (CommonUtil.isValidId(id)) {
//			model = pcmReqService.get(id, null);
//		}
//		
		if (model==null) {
			model = new PcmReqModel();
		}
//		model.setWaitingLevel(0);
		
		model.setReqBy(reqBy);
//		if (reqOu != null && !reqOu.equals("")) {
//			model.setReqSectionId(Integer.parseInt(reqOu));
//		}
//		if (objectiveType != null && !objectiveType.equals("")) {
//			model.setObjectiveType(Integer.parseInt(objectiveType));
//		}
//		model.setObjective(objective);
//		model.setReason(reason);
//		model.setCurrency(currency);
//		model.setCurrencyRate(Double.parseDouble(currencyRate));
		if (budgetCc!=null && !budgetCc.equals("")) {
			model.setBudgetCc(Integer.parseInt(budgetCc));
		}
		model.setBudgetCcType(budgetCcType);
//		if (fundId != null && !fundId.equals("")) {
//			model.setFundId(Integer.parseInt(fundId));
//		}
//		model.setIsStock(isStock);
//		if (stockOu != null && !stockOu.equals("")) {
//			model.setStockSectionId(Integer.parseInt(stockOu));
//		}
//		model.setIsPrototype(isPrototype);
//		model.setPrototypeType(prototypeType);
//		model.setPrototypeNo(prototypeNo);
//		model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
//		model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
		if (pcmOu!=null && !pcmOu.equals("")) {
			model.setPcmSectionId(Integer.parseInt(pcmOu));
		}
//		model.setContractDate(CommonDateTimeUtil.convertSenchaStringToTimestamp(contractDate));
//		model.setLocation(location);
		model.setIsSmallAmount(isSmallAmount);
		model.setIsAcrossBudget(isAcrossBudget);
		if (isAcrossBudget!=null && isAcrossBudget.equals("1")) {
			model.setAcrossBudget(Double.parseDouble(totalCnv));
		}
		model.setIsRefId(isRefId);
		model.setRefId(refId);
		model.setPrWebMethodId(method!=null && !method.equals("") ? Long.parseLong(method) : null);
//		model.setMethodCond2Rule(methodCond2Rule);
//		model.setMethodCond2(methodCond2);
//		model.setMethodCond2Dtl(methodCond2Dtl);
		model.setVat(Double.parseDouble(vat));
		model.setVatId(Integer.parseInt(vatId!=null && !vatId.equals("") ? vatId : "0"));
		model.setPriceInclude(Boolean.parseBoolean(priceInclude!=null && !priceInclude.equals("") ? priceInclude : "false"));
		model.setTotal(Double.parseDouble(total));
		model.setTotalCnv(Double.parseDouble(totalCnv));
//		model.setStatus(status);
//		model.setRequestedTime(CommonDateTimeUtil.now());

		JSONObject jobj = new JSONObject(model); 
		log.info("model="+jobj.toString());
		
		mainWorkflowService.setModuleService(pcmReqService);
		Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_PR, model);
		JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
		if (!(Boolean)validateWfPath.get("valid")) {
			json = CommonUtil.jsonFail(validateWfPath);
		} else {
			String msg = pcmReqService.getWorkflowPath(model, bossMap);
			
			json = CommonUtil.jsonSuccess(msg);
		}
		
	} catch (FolderNoPermissionException ex) {
		json = CommonUtil.jsonFail(ex.getMessage());
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
    	session.close();
		CommonUtil.responseWrite(response, json);
	}
	  
  }
 

  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/req/delete")
  public void handleDelete(@RequestParam final String id, final WebScriptResponse response) throws Exception {
	String json = null;
	
	try {
		pcmReqService.delete(id);
		
		json = CommonUtil.jsonSuccess();
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		CommonUtil.responseWrite(response, json);
	}
  }

  @Uri(URI_PREFIX+"/req/get")
  public void handleGet(@RequestParam final String id,
		  				@RequestParam final String lang,
						  final WebScriptResponse response)
      throws Exception {
		
	String json = null;

	try {
	  log.info("/req/get("+id+")");
	  
	  PcmReqModel model = pcmReqService.get(id, lang);

	  List<PcmReqModel> list = new ArrayList<PcmReqModel>();
	  list.add(model);

	  List<PcmReqDtlModel> dtlList = pcmReqService.listDtlByMasterId(id);  

	  json = PcmReqUtil.jsonSuccess(list, dtlList);

	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	
  }
  
  @Uri(URI_PREFIX+"/req/userDtl")
  public void handleInitForm(@RequestParam(required=false) final String r,
		  					 @RequestParam(required=false) final String c,
		  					 @RequestParam(required=false) final String m,
		  					 @RequestParam(required=false) final String lang,
		  					 final WebScriptResponse response)
      throws Exception {
		
	String json = null;

	try {
      String langSuffix = lang!=null && lang.startsWith("th") ? "_th" : "";
		
	  List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  String reqUser = (r!=null) ? r : authService.getCurrentUserName();
	  
	  Map<String,Object> dtl = adminHrEmployeeService.getWithDtl(reqUser,m);
	  
	  map.put(PcmReqConstant.JFN_REQ_BY, reqUser);
	  
	  String ename = dtl.get("title"+langSuffix) + " " + dtl.get("first_name"+langSuffix) + " " + dtl.get("last_name"+langSuffix);

	  String mphone = StringUtils.defaultIfEmpty((String)dtl.get("mobile_phone"),"");
	  String wphone = StringUtils.defaultIfEmpty((String)dtl.get("work_phone"),"");
	  String comma = (!mphone.equals("") && !wphone.equals("")) ? "," : "";
	  
	  map.put(PcmReqConstant.JFN_REQ_BY_NAME, ename);
	  map.put(PcmReqConstant.JFN_REQ_TEL_NO, wphone+comma+mphone);
	  map.put(PcmReqConstant.JFN_REQ_BY_DEPT, dtl.get("position"+langSuffix));
	  
	  map.put(PcmReqConstant.JFN_REQ_BU, dtl.get("org_desc"+langSuffix));
	  
	  map.put(PcmReqConstant.JFN_REQ_SECTION_ID, dtl.get("section_id"));
	  map.put(PcmReqConstant.JFN_REQ_SECTION_NAME, dtl.get("section_desc"+langSuffix));
	  
	  String createdUser = (c!=null) ? c : authService.getCurrentUserName();
	  if (!createdUser.equals(reqUser)) {
		  dtl = adminHrEmployeeService.getWithDtl(createdUser,m);
		  ename = dtl.get("title"+langSuffix) + " " + dtl.get("first_name"+langSuffix) + " " + dtl.get("last_name"+langSuffix);
		  
		  mphone = StringUtils.defaultIfEmpty((String)dtl.get("mobile_phone"),"");
		  wphone = StringUtils.defaultIfEmpty((String)dtl.get("work_phone"),"");
		  comma = (!mphone.equals("") && !wphone.equals("")) ? "," : "";
	  }
	  map.put(PcmReqConstant.JFN_CREATED_BY_SHOW, ename);
	  
	  map.put(PcmReqConstant.JFN_TEL_NO, wphone+comma+mphone);
	  
	  list.add(map);
	  
	  json = CommonUtil.jsonSuccess(list);

	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	
  }
  
  
  @Uri(URI_PREFIX+"/file/list")
  public void handleFileList(@RequestParam final String id, final WebScriptResponse response)
      throws Exception {
		
	String json = null;
	 
	try {
		
	  List<FileModel> files = pcmReqService.listFile(id, false);
		
	  json = FileUtil.jsonSuccess(files);
		
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	
  }
  
  @Uri(URI_PREFIX+"/req/item/list")
  public void handleItemList(@RequestParam final String id, final WebScriptResponse response)
      throws Exception {
		
	String json = null;
	 
	try {
		List<PcmReqDtlModel> list = pcmReqService.listDtlByMasterId(id);
		PcmReqDtlUtil.addAction(list);
		
		json = CommonUtil.jsonSuccess(list);
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	
  }
  
  @Uri(URI_PREFIX+"/req/sa/list")
  public void handleSmallAmountList(@RequestParam final String id, final WebScriptResponse response)
      throws Exception {
		
	String json = null;
	 
	try {
		List<PcmReqDtlModel> tmpList = pcmReqService.listDtlByMasterId(id);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(PcmReqDtlModel model:tmpList) {
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("id", model.getId());
			map.put("actGrpId", model.getActGrpId());
			map.put("actGrpName",model.getActGrpName());
			map.put("actId",model.getActId());
			map.put("actName",model.getActName());
			map.put("activity",model.getDescription());
			map.put("amount",model.getTotal());
			map.put("assetRuleId",null);
			map.put("condition1",null);
			map.put("totalRowCount",model.getTotalRowCount());
			
			map.put("action", "E");
			
			list.add(map);
		}
		
		json = CommonUtil.jsonSuccess(list);
	} catch (Exception ex) {
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
		
	} finally {
		CommonUtil.responseWrite(response, json);
	}
	
  }  
  
  @Uri(method=HttpMethod.GET, value=URI_PREFIX+"/preview")
  public void handlePreview(@RequestParam final String id
		  					,final WebScriptResponse response) throws Exception {
	  
	  String tmpDir = FolderUtil.getTmpDir();
	  String fullName = tmpDir+File.separator+id+".pdf";
	  Path path = Paths.get(fullName);
	  byte[] data = Files.readAllBytes(path);
		
	  response.setContentType("application/pdf");
	  java.io.OutputStream out = response.getOutputStream();
	    
	  out.write(data);
	  out.flush();
	  out.close();
	  
	  File file = new File(fullName);
	  file.delete();
  }
  
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/preview")
  public void handlePreviewGen(@RequestParam(required=false) final String id
							,@RequestParam(required=false) final String reqBy
							,@RequestParam(required=false) final String reqOu
							,@RequestParam(required=false) final String objectiveType
							,@RequestParam(required=false) final String objective
							,@RequestParam(required=false) final String reason
							,@RequestParam(required=false) final String currency
							,@RequestParam(required=false) final String currencyRate
							,@RequestParam(required=false) final String budgetCcType
							,@RequestParam(required=false) final String budgetCc
							,@RequestParam(required=false) final String fundId
							,@RequestParam(required=false) final String isStock
							,@RequestParam(required=false) final String stockOu
							,@RequestParam(required=false) final String isPrototype
							,@RequestParam(required=false) final String prototypeType
							,@RequestParam(required=false) final String prototypeNo
							,@RequestParam(required=false) final String costControlTypeId
							,@RequestParam(required=false) final String costControlId
							,@RequestParam(required=false) final String pcmOu
							,@RequestParam(required=false) final String contractDate
							,@RequestParam(required=false) final String location
							,@RequestParam(required=false) final String isSmallAmount
							,@RequestParam(required=false) final String isAcrossBudget
							,@RequestParam(required=false) final String isRefId
							,@RequestParam(required=false) final String refId
							,@RequestParam(required=false) final String method
							,@RequestParam(required=false) final String methodCond2Rule
							,@RequestParam(required=false) final String methodCond2
							,@RequestParam(required=false) final String methodCond2Dtl
							,@RequestParam(required=false) final String vat
							,@RequestParam(required=false) final String vatId
							,@RequestParam(required=false) final String priceInclude
							,@RequestParam(required=false) final String total
							,@RequestParam(required=false) final String totalCnv
							,@RequestParam(required=false) final String status
							,@RequestParam(required=false) final String items
							,@RequestParam(required=false) final String files
							,@RequestParam(required=false) final String cmts
							,final WebScriptResponse response) throws Exception {
	
	String json = null;
	
	SqlSession session = PcmUtil.openSession(dataSource);
	
	try {
		PcmReqModel model = null;
		
		if (CommonUtil.isValidId(id)) {
			model = pcmReqService.get(id, null);
		}
		
		if (model==null) {
			model = new PcmReqModel();
		}
		model.setWaitingLevel(0);
		
		model.setReqBy(reqBy);
		if (reqOu != null && !reqOu.equals("")) {
			model.setReqSectionId(Integer.parseInt(reqOu));
		}
		if (objectiveType != null && !objectiveType.equals("")) {
			model.setObjectiveType(Integer.parseInt(objectiveType));
		}
		model.setObjective(objective);
		model.setReason(reason);
		model.setCurrency(currency);
		model.setCurrencyRate(Double.parseDouble(currencyRate));
		model.setBudgetCcType(budgetCcType);
		if (budgetCc != null && !budgetCc.equals("")) {
			model.setBudgetCc(Integer.parseInt(budgetCc));
		}
		if (fundId != null && !fundId.equals("")) {
			model.setFundId(Integer.parseInt(fundId));
		}
		model.setIsStock(isStock);
		if (stockOu != null && !stockOu.equals("")) {
			model.setStockSectionId(Integer.parseInt(stockOu));
		}
		model.setIsPrototype(isPrototype);
		model.setPrototypeType(prototypeType);
		model.setPrototypeNo(prototypeNo);
		model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
		model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
		if (pcmOu != null && !pcmOu.equals("")) {
			model.setPcmSectionId(Integer.parseInt(pcmOu));
		}
		model.setContractDate(CommonDateTimeUtil.convertSenchaStringToTimestamp(contractDate));
		model.setLocation(location);
		model.setIsSmallAmount(isSmallAmount);
		model.setIsAcrossBudget(isAcrossBudget);
		if (isAcrossBudget!=null && isAcrossBudget.equals("1")) {
			model.setAcrossBudget(Double.parseDouble(totalCnv));
		}
		model.setIsRefId(isRefId);
		model.setRefId(refId);
		model.setPrWebMethodId(method!=null && !method.equals("") ? Long.parseLong(method) : null);
		model.setMethodCond2Rule(methodCond2Rule);
		model.setMethodCond2(methodCond2);
		model.setMethodCond2Dtl(methodCond2Dtl);
		model.setVat(Double.parseDouble(vat));
		model.setVatId(Integer.parseInt(vatId!=null && !vatId.equals("") ? vatId : "0"));
		model.setPriceInclude(Boolean.parseBoolean(priceInclude!=null && !priceInclude.equals("") ? priceInclude : "false"));
		model.setTotal(Double.parseDouble(total));
		model.setTotalCnv(Double.parseDouble(totalCnv));
		model.setStatus(status);
		
		if (model.getId() == null || (status!=null && status.equals(PcmReqConstant.ST_DRAFT))) {
			model.setStatus(PcmReqConstant.ST_WAITING);
			model.setWaitingLevel(1);
		}
		
		log.info("model="+model);
		
//		JSONObject validateResult = pcmReqService.validateAssignee(model);
//		if (!(Boolean)validateResult.get("valid")) {
//			json = CommonUtil.jsonFail(validateResult);
//		}
//		else {		
			
			model.setDtlList(PcmReqDtlUtil.convertJsonToList(items, model.getId()));
			model.setCmtList(PcmReqCmtHdrUtil.convertJsonToList(cmts, model.getId()));
			String fileName = pcmReqService.doGenDoc(session, PcmReqConstant.JR_PR, model);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(fileName, "");
			
			json = CommonUtil.jsonSuccess(map);
			session.commit();
//		}
	} catch (FolderNoPermissionException ex) {
		session.rollback();
		json = CommonUtil.jsonFail(ex.getMessage());
	} catch (Exception ex) {
		session.rollback();
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		session.close();
		CommonUtil.responseWrite(response, json);
	}
	  
  }
  
  @Uri(URI_PREFIX+"/req/criteria/list")
  public void handleCriteriaList(final WebScriptResponse response)  throws Exception {

		String json = null;
		
		try {
			JSONArray jsArr = pcmReqService.listCriteria();
			json = CommonUtil.jsonSuccess(jsArr);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
			
		}
    
  }
  
  
  @Uri(URI_PREFIX+"/req/gridfield/list")
  public void handleGridFieldList(final WebScriptResponse response)  throws Exception {

		String json = null;
		
		try {
			JSONArray jsArr = pcmReqService.listGridField();
			json = CommonUtil.jsonSuccess(jsArr);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
			
		}
    
  }
  
  @Transaction(value=TransactionType.REQUIRES_NEW)
  @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/finish")
  public void handleFinish(@RequestParam(required=false) final String id
						,@RequestParam(required=false) final String reqBy
						,@RequestParam(required=false) final String reqOu
						,@RequestParam(required=false) final String objectiveType
						,@RequestParam(required=false) final String objective
						,@RequestParam(required=false) final String reason
						,@RequestParam(required=false) final String currency
						,@RequestParam(required=false) final String currencyRate
						,@RequestParam(required=false) final String budgetCc
						,@RequestParam(required=false) final String budgetCcType
						,@RequestParam(required=false) final String fundId
						,@RequestParam(required=false) final String isStock
						,@RequestParam(required=false) final String stockOu
						,@RequestParam(required=false) final String isPrototype
						,@RequestParam(required=false) final String prototypeType
						,@RequestParam(required=false) final String prototypeNo
						,@RequestParam(required=false) final String costControlTypeId
						,@RequestParam(required=false) final String costControlId
						,@RequestParam(required=false) final String pcmOu
						,@RequestParam(required=false) final String contractDate
						,@RequestParam(required=false) final String location
						,@RequestParam(required=false) final String isSmallAmount
						,@RequestParam(required=false) final String isAcrossBudget
						,@RequestParam(required=false) final String isRefId
						,@RequestParam(required=false) final String refId
						,@RequestParam(required=false) final String method
						,@RequestParam(required=false) final String methodCond2Rule
						,@RequestParam(required=false) final String methodCond2
						,@RequestParam(required=false) final String methodCond2Dtl
						,@RequestParam(required=false) final String vat
						,@RequestParam(required=false) final String vatId
						,@RequestParam(required=false) final String priceInclude
						,@RequestParam(required=false) final String total
						,@RequestParam(required=false) final String totalCnv
						,@RequestParam(required=false) final String status
						,@RequestParam(required=false) final String items
						,@RequestParam(required=false) final String files
						,@RequestParam(required=false) final String cmts
						,@RequestParam(required=false) String lang
		  				,final WebScriptResponse response) throws Exception {
	
	log.info("/finish"); 
	  
	String json = null;
	
	SqlSession session = PcmUtil.openSession(dataSource);
	
	try {
		PcmReqModel model = null;
		
		if (CommonUtil.isValidId(id)) {
			model = pcmReqService.get(id, null);
		}
		
		if (model==null) {
			model = new PcmReqModel();
		}
		model.setWaitingLevel(0);
		
		model.setReqBy(reqBy);
		if (reqOu != null && !reqOu.equals("")) {
			model.setReqSectionId(Integer.parseInt(reqOu));
		}
		if (objectiveType != null && !objectiveType.equals("")) {
			model.setObjectiveType(Integer.parseInt(objectiveType));
		}
		model.setObjective(objective);
		model.setReason(reason);
		model.setCurrency(currency);
		model.setCurrencyRate(Double.parseDouble(currencyRate));
		if (budgetCc!=null && !budgetCc.equals("")) {
			model.setBudgetCc(Integer.parseInt(budgetCc));
		}
		model.setBudgetCcType(budgetCcType);
		if (fundId != null && !fundId.equals("")) {
			model.setFundId(Integer.parseInt(fundId));
		}
		model.setIsStock(isStock);
		if (stockOu != null && !stockOu.equals("")) {
			model.setStockSectionId(Integer.parseInt(stockOu));
		}
		model.setIsPrototype(isPrototype);
		model.setPrototypeType(prototypeType);
		model.setPrototypeNo(prototypeNo);
		model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
		model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
		if (pcmOu!=null && !pcmOu.equals("")) {
			model.setPcmSectionId(Integer.parseInt(pcmOu));
		}
		model.setContractDate(CommonDateTimeUtil.convertSenchaStringToTimestamp(contractDate));
		model.setLocation(location);
		model.setIsSmallAmount(isSmallAmount);
		model.setIsAcrossBudget(isAcrossBudget);
		if (isAcrossBudget!=null && isAcrossBudget.equals("1")) {
			model.setAcrossBudget(Double.parseDouble(totalCnv));
		}
		model.setIsRefId(isRefId);
		model.setRefId(refId);
		if (method!=null && !method.equals("")) {
			model.setPrWebMethodId(Long.parseLong(method));
		}
		model.setMethodCond2Rule(methodCond2Rule);
		model.setMethodCond2(methodCond2);
		model.setMethodCond2Dtl(methodCond2Dtl);
		model.setVat(Double.parseDouble(vat));
		model.setVatId(Integer.parseInt(vatId!=null && !vatId.equals("") ? vatId : "0"));
		model.setPriceInclude(Boolean.parseBoolean(priceInclude!=null && !priceInclude.equals("") ? priceInclude : "false"));
		model.setTotal(Double.parseDouble(total));
		model.setTotalCnv(Double.parseDouble(totalCnv));
		model.setStatus(status);	
		
		if (model.getId() == null || (status!=null && status.equals(PcmReqConstant.ST_DRAFT))) {
			model.setStatus(PcmReqConstant.ST_WAITING);
			model.setWaitingLevel(1);
		}
		
		JSONObject jobj = new JSONObject(model); 
		log.info("model="+jobj.toString());
		
//		JSONObject validateResult = pcmReqService.validateAssignee(model);
//		if (!(Boolean)validateResult.get("valid")) {
//			json = CommonUtil.jsonFail(validateResult);
//		}
//		else {
			mainWorkflowService.setModuleService(pcmReqService);
			Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_PR, model);
			JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
			if (!(Boolean)validateWfPath.get("valid")) {
				json = CommonUtil.jsonFail(validateWfPath);
			}
			else {
				model = pcmReqService.setPcmSectionInfo(model);
				JSONObject validateNextActor = mainWorkflowService.validateNextActor(model);
				if (!(Boolean)validateNextActor.get("valid")) {
					json = CommonUtil.jsonFail(validateNextActor);
				} else {
					model = pcmReqService.save(session, model, items, files, cmts, true);
					mainWorkflowService.updateWorkflow(session, model, files, cmts, bossMap);
					
					JSONObject jsObj = new JSONObject();
					jsObj.put("id", model.getId());
					jsObj.put("status", model.getStatus());
					json = CommonUtil.jsonSuccess(jsObj);
					
					session.commit();
				}
			}
//		}
		
	} catch (FolderNoPermissionException ex) {
		session.rollback();
		
    	if (ex.getMessage().startsWith("PB_ERR:")) {
    		String[] s = ex.getMessage().split(":");
    		String errMsg = MainUtil.getMessageWithOutCode(s[1], new Locale(lang))+s[2];
    		
    		JSONObject jsObj = new JSONObject();
    		jsObj.put("msg", errMsg);
    		jsObj.put("valid", false);
    		
			json = CommonUtil.jsonFail(jsObj);
    	} else {
			json = CommonUtil.jsonFail(ex.getMessage());
    	}		
	} catch (Exception ex) {
		session.rollback();
		log.error("", ex);
		json = CommonUtil.jsonFail(ex.toString());
		throw ex;
	} finally {
		session.close();
		CommonUtil.responseWrite(response, json);
	}
	  
  }
  
	@Uri(URI_PREFIX + "/userTask")
	public void handleUserTask(final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			JSONObject tasks = new JSONObject();

//			MainMasterModel rptModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_RPT_TAB);
//			tasks.put("pcmReqRptTab", rptModel!=null && rptModel.getFlag1()!=null && rptModel.getFlag1().equals("1"));
			
			JSONObject jsObj = new JSONObject();
			jsObj.put("tasks", tasks);
			
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	}
	
	@Uri(URI_PREFIX+"/req/cmt/list")
	public void handleCmtList(@RequestParam final Integer objType,
							  @RequestParam(required=false) final String total,
			  					final WebScriptResponse response)  throws Exception {
	
		String json = null;
			
		try {
			List<Map<String, Object>> list = pcmReqService.listCmt(objType, total);
				
			json = PcmReqCmtUtil.jsonSuccess(list);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
				
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	    
	}
	
	
	@Uri(URI_PREFIX+"/req/cmt/dtl/list")
	public void handleCmtDtlList(@RequestParam final String id,
								 @RequestParam final String cmt,
								 @RequestParam final String lang,
			  					final WebScriptResponse response)  throws Exception {
	
		String json = null;
			
		try {
			List<Map<String,Object>> list = pcmReqService.listCmtDtl(id, Integer.parseInt(cmt));
			PcmReqCmtDtlUtil.addAction(list);
			
			json = PcmReqCmtDtlUtil.jsonSuccess(list,lang);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
				
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	    
	}
	  
	public void genRpt(byte[] file, String fileName, WebScriptResponse response, JasperPrint jasperPrint) throws IOException, JRException {
		
		file = JasperExportManager.exportReportToPdf(jasperPrint);
			
		InputStream is = new ByteArrayInputStream(file);
		response.setContentType("application/pdf");
		
		OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0){
            out.write(buffer, 0, length);
        }
      
        is.close();
        out.flush();	
        out.close();
	}
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/req/copy")
	public void handleCopy(@RequestParam final String id,
						   @RequestParam(required=false) final String lang
						   , final WebScriptResponse response)
	      throws Exception {
			
		String json = null;
	
		try {
		  String newId = pcmReqService.copy(id);
	
		  json = CommonUtil.jsonSuccess(newId);
	
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/req/checkRefPR")
	public void handleCheckRefPR(@RequestParam final String refId,
						         @RequestParam final String total
						   , final WebScriptResponse response)
	      throws Exception {
			
		String json = null;
	
		try {
		  MainMasterModel percentModel = masterService.getSystemConfig(MainMasterConstant.SCC_PCM_REQ_REF_PR_PERCENT);
			
		  PcmReqModel model = pcmReqService.get(refId, null);
		  
		  JSONObject jobj = new JSONObject();
		  
		  Boolean ok = Double.parseDouble(total) <= model.getTotal() * model.getCurrencyRate() * Double.parseDouble(percentModel.getFlag1()) / 100; 
		  
		  jobj.put("ok", ok);
		  jobj.put("percent", Double.parseDouble(percentModel.getFlag1()));
	
		  json = CommonUtil.jsonSuccess(jobj);
	
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	@Uri(method=HttpMethod.GET, value=URI_PREFIX+"/req/test")
	public void handleTest(final WebScriptResponse response)
	      throws Exception {
			
		String json = null;
	
		try {
		  ///////////
		  Map<String, String> map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  
		  log.info("map 1:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
		  ///////////
		  map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  map.put("1", "0");
		  
		  log.info("map 2:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
		  ///////////
		  map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  map.put("1", "1");
		  map.put("2", "1");
		  
		  log.info("map 3:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
		  ///////////
		  map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  map.put("1", "1");
		  map.put("2", "2");
		  map.put("3", "1");
		  
		  log.info("map 4:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
		  ///////////
		  map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  map.put("1", "1");
		  map.put("2", "2");
		  map.put("3", "2");
		  
		  log.info("map 5:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
		  ///////////
		  map = new LinkedHashMap<String, String>();
		  map.put("0", "0");
		  map.put("1", "0");
		  map.put("2", "1");
		  map.put("3", "1");
		  map.put("4", "2");
		  map.put("5", "2");
		  
		  log.info("map 6:"+map);
			
		  map = pcmReqService.removeDuplicatedBoss(map);
		  
	
		  json = CommonUtil.jsonSuccess(1);
	
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}	
	
    @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/req/genDoc")
    public void handleGenDoc(@RequestParam(required=false) final String id
			  , final WebScriptResponse response)  throws Exception {
    	String json = null;
    	
		SqlSession session = PcmUtil.openSession(dataSource);
		
		try {
			PcmReqModel model = pcmReqService.get(id, "th");
			
			model.setDtlList(pcmReqService.listDtlByMasterId(model.getId()));
			model.setCmtList(pcmReqService.listCmtHdrByMasterId(model.getId(),true));
			
			model = pcmReqService.genDoc(session, model);
			
			JSONObject jsObj = new JSONObject();
			jsObj.put("docRef", model.getDocRef());
			json = CommonUtil.jsonSuccess(jsObj);
		} catch (Exception ex) {
			session.rollback();
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
		} finally {
			session.close();
			CommonUtil.responseWrite(response, json);
		}
    }  
}
