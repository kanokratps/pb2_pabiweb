package pb.repo.exp.wscript;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.stereotype.Component;

import pb.common.constant.CommonConstant;
import pb.common.exception.FolderNoPermissionException;
import pb.common.model.FileModel;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.common.util.FileUtil;
import pb.common.util.FolderUtil;
import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.constant.MainWkfConfigDocTypeConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminHrEmployeeService;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminModuleService;
import pb.repo.admin.service.AdminBudgetSrcService;
import pb.repo.admin.service.AdminUserGroupService;
import pb.repo.admin.service.AdminWorkflowService;
import pb.repo.admin.util.MainUtil;
import pb.repo.exp.model.ExpBrwAttendeeModel;
import pb.repo.exp.model.ExpBrwDtlModel;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.service.ExpBrwService;
import pb.repo.exp.service.ExpBrwWorkflowService;
import pb.repo.exp.service.InterfaceService;
import pb.repo.exp.util.ExpBrwAttendeeUtil;
import pb.repo.exp.util.ExpBrwDtlUtil;
import pb.repo.exp.util.ExpBrwUtil;
import pb.repo.exp.util.ExpUtil;

import com.github.dynamicextensionsalfresco.webscripts.annotations.HttpMethod;
import com.github.dynamicextensionsalfresco.webscripts.annotations.RequestParam;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;
import com.github.dynamicextensionsalfresco.webscripts.annotations.Uri;
import com.github.dynamicextensionsalfresco.webscripts.annotations.WebScript;

@Component
@WebScript
public class ExpWebScript {
	
	private static Logger log = Logger.getLogger(ExpWebScript.class);
	
	private static final String URI_PREFIX = CommonConstant.GLOBAL_URI_PREFIX + "/exp";
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private AdminUserGroupService userGroupService;
	
	@Autowired
	private ExpBrwService expBrwService;

	@Autowired
	private AdminMasterService masterService;
	
	@Autowired
	private AdminHrEmployeeService adminHrEmployeeService;

	@Autowired
	private AdminWorkflowService adminWorkflowService;

	@Autowired
	private InterfaceService interfaceService;
	
	@Autowired
	private ExpBrwWorkflowService mainWorkflowService;
	
	@Autowired
	private AdminBudgetSrcService budgetSrcService;
	
	@Autowired
	private AdminModuleService moduleService;
	
	@Autowired
	DataSource dataSource;
	
    @Uri(URI_PREFIX+"/brw/list")
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
//			params.put("loginL", "%,"+curUser+",%");
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
//					roleList.add("%,"+roles[i]+",%");
				roleList.add(roles[i]);
			}
		}
		params.put("roleList",  roleList);
		
		
		if (fields != null) {
			JSONObject jsObj = new JSONObject(fields);
			
			putOneParam(params, jsObj, ExpBrwConstant.JFN_STATUS);
			putOneParam(params, jsObj, ExpBrwConstant.JFN_OBJECTIVE_TYPE);
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
			MainMasterModel orderByModel = masterService.getSystemConfig(MainMasterConstant.SCC_EXP_BRW_ORDER_BY,false);
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
			List<Map<String,Object>> list = expBrwService.list(params);
			json = CommonUtil.jsonSuccess(list);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
	  
    }
    
    @Uri(URI_PREFIX+"/brw/old/list")
    public void handleOldList(@RequestParam(required=false) String r,
    						  @RequestParam(required=false) String av,
    						  @RequestParam(required=false) String lang,
    		final WebScriptResponse response)  throws Exception {

	  	/*
	  	 * Prepare Criteria
	  	 */
		Map<String, Object> params = new HashMap<String, Object>();
		
		/*
		 * Search
		 */
		String json = null;
		
		try {
			List<Map<String, Object>> list = null;
			
			if (av!=null) {
				
				ExpBrwModel model = expBrwService.getForWfPath(av, lang);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("id", 1);
				map.put("ord", 1);
				map.put("number", av);
				map.put("name", model!=null ? model.getObjective() : "Not Found");
				map.put("waitamt", 0);
				map.put("balance", 0);
				map.put("is_small_amount", "0");
				map.put("af_number", av);
				
				list = new ArrayList<Map<String,Object>>();
				list.add(map);
			
			} else {
				
				if (r==null) {
					r = authService.getCurrentUserName();
				}
				
				params.put("code", r);
				
				list = expBrwService.listOld(params);
				
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
  
	@Uri(URI_PREFIX + "/userTask")
	public void handleUserTask(final WebScriptResponse response)
								throws Exception {

		String json = null;

		try {
			JSONObject tasks = new JSONObject();

//			MainMasterModel rptModel = masterService.getSystemConfig(MainMasterConstant.SCC_EXP_BRW_RPT_TAB);
//			tasks.put("expBrwRptTab", rptModel.getFlag1()!=null && rptModel.getFlag1().equals("1"));
			
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
	
	@Uri(URI_PREFIX+"/brw/gridfield/list")
	public void handleGridFieldList(final WebScriptResponse response)  throws Exception {

		String json = null;
		
		try {
			JSONArray jsArr = expBrwService.listGridField();
			json = CommonUtil.jsonSuccess(jsArr);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
			
		}
		
	}
	
	@Uri(URI_PREFIX+"/brw/criteria/list")
	public void handleCriteriaList(final WebScriptResponse response)  throws Exception {

			String json = null;
			
			try {
				JSONArray jsArr = expBrwService.listCriteria();
				json = CommonUtil.jsonSuccess(jsArr);
				
			} catch (Exception ex) {
				log.error("", ex);
				json = CommonUtil.jsonFail(ex.toString());
				throw ex;
				
			} finally {
				CommonUtil.responseWrite(response, json);
				
			}
	    
	}
	
	@Uri(URI_PREFIX+"/brw/userDtl")
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
		  
		  Map<String,Object> dtl = adminHrEmployeeService.getWithDtl(reqUser, m);
		  
		  map.put(ExpBrwConstant.JFN_REQ_BY, reqUser);
		  
		  String ename = dtl.get("title"+langSuffix) + " " + dtl.get("first_name"+langSuffix) + " " + dtl.get("last_name"+langSuffix);
		  
		  String mphone = StringUtils.defaultIfEmpty((String)dtl.get("mobile_phone"),"");
		  String wphone = StringUtils.defaultIfEmpty((String)dtl.get("work_phone"),"");
		  String comma = (!mphone.equals("") && !wphone.equals("")) ? "," : "";
		  
		  map.put(ExpBrwConstant.JFN_REQ_BY_NAME, ename);
		  map.put(ExpBrwConstant.JFN_REQ_TEL_NO, wphone+comma+mphone);
		  map.put(ExpBrwConstant.JFN_REQ_BY_DEPT, dtl.get("position"+langSuffix));
		  
		  map.put(ExpBrwConstant.JFN_REQ_BU, dtl.get("org_desc"+langSuffix));
		  
		  map.put(ExpBrwConstant.JFN_REQ_SECTION_ID, dtl.get("section_id"));
		  map.put(ExpBrwConstant.JFN_REQ_SECTION_NAME, dtl.get("section_desc"+langSuffix));
		  
		  map.put(ExpBrwConstant.JFN_CREATED_BY, authService.getCurrentUserName());
		  String createdUser = (c!=null) ? c : authService.getCurrentUserName();
		  if (!createdUser.equals(reqUser)) {
			  dtl = adminHrEmployeeService.getWithDtl(createdUser, m);
			  ename = dtl.get("title"+langSuffix) + " " + dtl.get("first_name"+langSuffix) + " " + dtl.get("last_name"+langSuffix);
			  
			  mphone = StringUtils.defaultIfEmpty((String)dtl.get("mobile_phone"),"");
			  wphone = StringUtils.defaultIfEmpty((String)dtl.get("work_phone"),"");
			  comma = (!mphone.equals("") && !wphone.equals("")) ? "," : "";
		  }
		  
		  map.put(ExpBrwConstant.JFN_CREATED_BY_SHOW, ename);
		  
		  map.put(ExpBrwConstant.JFN_TEL_NO, wphone+comma+mphone);
		  
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
	
	@Uri(URI_PREFIX+"/brw/attendee/list")
	public void handleAttendeeList(@RequestParam final String id, 
								  @RequestParam final String type,
								  @RequestParam final String lang,
								  final WebScriptResponse response)
	    throws Exception {
			
		String json = null;
		 
		try {
			List<Map<String, Object>> list = expBrwService.listAttendeeByMasterIdAndType(id, type, lang);
			ExpBrwAttendeeUtil.addAction(list);
			
			json = CommonUtil.jsonSuccess(list);
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	private void putOneParam(Map<String, Object> params, JSONObject jsObj, String fieldName) {
	  try {
		String field = jsObj.getString(fieldName);  
		if (field!=null && !field.equals("") && !field.equals("null")) {
			params.put(fieldName,  field);
		}
	  } catch (Exception ex) {
		  // do nothing
	  }
	}
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/save")
	public void handleSave(@RequestParam(required=false) final String id
							,@RequestParam(required=false) final String reqBy
			  				,@RequestParam(required=false) final String objectiveType
			  				,@RequestParam(required=false) final String objective
			  				,@RequestParam(required=false) final String avRemark
							,@RequestParam(required=false) final String isReason
			  				,@RequestParam(required=false) final String reason
			  				,@RequestParam(required=false) final String note
							,@RequestParam(required=false) final String isSmallAmount
							,@RequestParam(required=false) final String originPrNumber
							,@RequestParam(required=false) final String budgetCc
							,@RequestParam(required=false) final String budgetCcType
							,@RequestParam(required=false) final String fundId
							,@RequestParam(required=false) final String costControlTypeId
							,@RequestParam(required=false) final String costControlId
							,@RequestParam(required=false) final String dateBack
							,@RequestParam(required=false) final String bankType
							,@RequestParam(required=false) final String bank
							,@RequestParam(required=false) final String total
			  				,@RequestParam(required=false) final String items
			  				,@RequestParam(required=false) final String attendees
			  				,@RequestParam(required=false) final String files
							,@RequestParam(required=false) String lang
			  				,final WebScriptResponse response) throws Exception {
		
		log.info("/save"); 

		String json = null;
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = null;
			
			if (CommonUtil.isValidId(id)) {
				model = expBrwService.get(id, null);
			}
			
			if (model==null) {
				model = new ExpBrwModel();
			}
			
			model.setReqBy(reqBy);
			model.setObjectiveType(objectiveType);
			model.setObjective(objective);
			model.setAvRemark(avRemark);
			model.setReason(reason);
			model.setNote(note);
			
			model.setIsSmallAmount(isSmallAmount);
			model.setOriginPrNumber(originPrNumber);
			
			model.setIsReason(isReason);
			
			if (budgetCc != null && !budgetCc.equals("")) {
				model.setBudgetCc(Integer.parseInt(budgetCc));
			}
			model.setBudgetCcType(budgetCcType);
			if (fundId != null && !fundId.equals("")) {
				model.setFundId(Integer.parseInt(fundId));
			}
			
			model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
			model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);

			if (dateBack!=null) {
				model.setDateBack(CommonDateTimeUtil.convertSenchaStringToTimestamp(dateBack));
			}
			
			model.setBankType(bankType);
			if (bank != null && !bank.equals("")) {
				model.setBank(Integer.parseInt(bank));
			}
			model.setTotal(Double.parseDouble(total!=null && !total.equals("") ? total : "0"));
			
			if (model.getStatus()!=null && model.getStatus().equals(ExpBrwConstant.ST_WAITING)) {
				JSONObject data = new JSONObject();
				data.put("valid", false);
				data.put("msg", "AV status is Waiting for approval");
				data.put("close", true);
				json = CommonUtil.jsonFail(data);
				session.rollback();
			} else {
				Map<String,Object> wfModel = adminWorkflowService.getByDocId(model.getId());
				if (wfModel!=null) {
					JSONObject data = new JSONObject();
					data.put("valid", false);
					data.put("msg", "AV has already been sent to Approver");
					data.put("close", true);
					json = CommonUtil.jsonFail(data);
					session.rollback();
				} else {
					model.setStatus(ExpBrwConstant.ST_DRAFT);
					
					JSONObject jobj = new JSONObject(model); 
					log.info("model="+jobj.toString());

					model = expBrwService.save(session, model, items, attendees, files, false);
					
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
			  				,@RequestParam(required=false) final String objectiveType
			  				,@RequestParam(required=false) final String objective
			  				,@RequestParam(required=false) final String avRemark
							,@RequestParam(required=false) final String isReason
			  				,@RequestParam(required=false) final String reason
			  				,@RequestParam(required=false) final String note
							,@RequestParam(required=false) final String isSmallAmount
							,@RequestParam(required=false) final String originPrNumber
							,@RequestParam(required=false) final String budgetCc
							,@RequestParam(required=false) final String budgetCcType
							,@RequestParam(required=false) final String fundId
							,@RequestParam(required=false) final String costControlTypeId
							,@RequestParam(required=false) final String costControlId
							,@RequestParam(required=false) final String dateBack
							,@RequestParam(required=false) final String bankType
							,@RequestParam(required=false) final String bank
							,@RequestParam(required=false) final String total
			  				,@RequestParam(required=false) final String items
			  				,@RequestParam(required=false) final String attendees
			  				,@RequestParam(required=false) final String files
							,@RequestParam(required=false) final String status
							,@RequestParam(required=false) String lang
			  				,final WebScriptResponse response) throws Exception {
		
		log.info("/send"); 

		String json = null;
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = null;
			
			if (CommonUtil.isValidId(id)) {
				model = expBrwService.get(id, null);
			}
			
			if (model==null) {
				model = new ExpBrwModel();
			}
			
			model.setReqBy(reqBy);
			
			model.setObjectiveType(objectiveType);
			model.setObjective(objective);
			model.setAvRemark(avRemark);
			model.setReason(reason);
			model.setNote(note);
			
			model.setIsSmallAmount(isSmallAmount);
			model.setOriginPrNumber(originPrNumber);
			
			model.setIsReason(isReason);
			
			if (budgetCc != null && !budgetCc.equals("")) {
				model.setBudgetCc(Integer.parseInt(budgetCc));
			}
			
			model.setBudgetCcType(budgetCcType);
			if (fundId != null && !fundId.equals("")) {
				model.setFundId(Integer.parseInt(fundId));
			}
			
			model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
			model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
			if (dateBack!=null) {
				model.setDateBack(CommonDateTimeUtil.convertSenchaStringToTimestamp(dateBack));
			}
			
			model.setBankType(bankType);
			if (bank != null && !bank.equals("")) {
				model.setBank(Integer.parseInt(bank));
			}
			model.setTotal(Double.parseDouble(total));
			model.setStatus(status);
			
			model.setRequestedTime(CommonDateTimeUtil.now());
			
			JSONObject jobj = new JSONObject(model); 
			log.info("model="+jobj.toString());
			
//			JSONObject validateResult = expBrwService.validateAssignee(model);
//			if (!(Boolean)validateResult.get("valid")) {
//				json = CommonUtil.jsonFail(validateResult);
//			}
//			else {
				mainWorkflowService.setModuleService(expBrwService);
				Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_AV, model);
				JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
				if (!(Boolean)validateWfPath.get("valid")) {
					json = CommonUtil.jsonFail(validateWfPath);
				}
				else {
					JSONObject validateAssetPrice = null;
					
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

					List<ExpBrwDtlModel> dtls = ExpBrwDtlUtil.convertJsonToList(items, model.getId());
				
					for(ExpBrwDtlModel dtl : dtls) {
						
						Map<String, Object> map = new HashMap<String,Object>();
						
						map.put("activity_rpt_id", dtl.getActId());
						map.put("amount", dtl.getAmount());

						if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
							map.put("select_asset_id", dtl.getAssetRuleId());
						}
						
						list.add(map);
					}

					MainMasterModel chkBudgetModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_INF_CHECK_BUDGET);
					Boolean checkBudget = chkBudgetModel.getFlag1().equals(CommonConstant.V_ENABLE);
					
					if (checkBudget) {
						JSONObject vResult = moduleService.checkBudget("av",model.getBudgetCcType(), model.getBudgetCc(), model.getFundId(), list);
						if (!(Boolean)vResult.get("valid")) {
							validateAssetPrice = vResult;
						}					
					}
					
//					Map<Integer, Map<String, Object>> sumDtls = new HashMap<Integer, Map<String,Object>>();
//					
//					List<ExpBrwDtlModel> dtls = ExpBrwDtlUtil.convertJsonToList(items, model.getId());
//					for(ExpBrwDtlModel dtl : dtls) {
//						if (dtl.getAssetRuleId()!=null && !dtl.getAssetRuleId().equals(0)) {
//							JSONObject vResult = moduleService.validateAssetPrice(dtl.getAssetRuleId(), dtl.getAmount());
//							if (!(Boolean)vResult.get("valid")) {
//								validateAssetPrice = vResult;
//								break;
//							}
//						}
//						
//						if (model.getBudgetCcType().equals("P")) {
//							
//							Map<String, Object> map = sumDtls.get(dtl.getActId());
//							Double oldTotal = 0.0;
//							if (map==null) {
//								map = new HashMap<String,Object>();
//							} else {
//								oldTotal = (Double)map.get("amount");
//							}
//							
//							map.put("fund_id", model.getFundId());
//							map.put("project_id", model.getBudgetCc());
//							map.put("activity_rpt_id", dtl.getActId());
//							map.put("amount", oldTotal + dtl.getAmount());
//							
//							sumDtls.put(dtl.getActId(), map);
//						}
//						
//					}
//					
//					if (validateAssetPrice==null) {
//						if (model.getBudgetCcType().equals("P")) {
//							List<Map<String,Object>> list = new ArrayList();
//							
//							for(Entry<Integer,Map<String,Object>> e : sumDtls.entrySet()) {
//								list.add((Map<String,Object>)e.getValue());
//							}
//							JSONObject vResult = moduleService.checkFundSpending(list);
//							if (!(Boolean)vResult.get("valid")) {
//								validateAssetPrice = vResult;
//							}
//						}
//					}
					
					if (validateAssetPrice!=null) {
						json = CommonUtil.jsonFail(validateAssetPrice);
					}
					else {
						if (model.getStatus()!=null && model.getStatus().equals(ExpBrwConstant.ST_WAITING)) {
							JSONObject data = new JSONObject();
							data.put("valid", false);
							data.put("msg", "AV status is Waiting for approval");
							data.put("close", true);
							json = CommonUtil.jsonFail(data);
						} else {
							if (model.getId() == null || (status!=null && status.equals(ExpBrwConstant.ST_DRAFT))) {
								model.setStatus(ExpBrwConstant.ST_WAITING);
								model.setWaitingLevel(1);
							}
							
							model = expBrwService.save(session, model, items, attendees, files, true);
							
							Map<String,Object> wfModel = adminWorkflowService.getByDocId(model.getId());
							if (wfModel!=null) {
								JSONObject data = new JSONObject();
								data.put("valid", false);
								data.put("msg", "AV has already been sent to Approver");
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
//			}		

//			String createResult = interfaceService.createAV(model);
//			log.info("createAVResult="+createResult);
			
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
	
	@Uri(URI_PREFIX+"/brw/get")
	public void handleGet(@RequestParam final String id, 
						  @RequestParam final String lang,
						  final WebScriptResponse response)
	      throws Exception {
			
		String json = null;

		try {
		  ExpBrwModel model = expBrwService.get(id, lang);

		  List<ExpBrwModel> list = new ArrayList<ExpBrwModel>();
		  if (model!=null) {
			  list.add(model);
		  }
		  
//		  List<ExpBrwDtlModel> dtlList = expBrwService.listDtlByMasterIdAndType(id, ExpBrwDtlConstant.T_EMPLOYEE);  

		  json = ExpBrwUtil.jsonSuccess(list, (List<ExpBrwAttendeeModel>)null);

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
			
		  List<FileModel> files = expBrwService.listFile(id, false);
			
		  json = FileUtil.jsonSuccess(files);
			
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}
	
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/brw/copy")
	public void handleCopy(@RequestParam final String id,
						   @RequestParam(required=false) final String lang,
						   final WebScriptResponse response)
	      throws Exception {
			
		String json = null;
	
		try {
		  String newId = expBrwService.copy(id);
	
		  json = CommonUtil.jsonSuccess(newId);
	
		} catch (Exception ex) {
			log.error("", ex);
			json = CommonUtil.jsonFail(ex.toString());
			throw ex;
			
		} finally {
			CommonUtil.responseWrite(response, json);
		}
		
	}	
  
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/brw/delete")
	public void handleDelete(@RequestParam final String id, final WebScriptResponse response) throws Exception {
		String json = null;
		
		try {
			expBrwService.delete(id);
			
			json = CommonUtil.jsonSuccess();
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
								,@RequestParam(required=false) final String objectiveType
								,@RequestParam(required=false) final String objective
				  				,@RequestParam(required=false) final String avRemark
								,@RequestParam(required=false) final String isReason
								,@RequestParam(required=false) final String reason
				  				,@RequestParam(required=false) final String note
								,@RequestParam(required=false) final String isSmallAmount
								,@RequestParam(required=false) final String originPrNumber
								,@RequestParam(required=false) final String budgetCc
								,@RequestParam(required=false) final String budgetCcType
								,@RequestParam(required=false) final String fundId
								,@RequestParam(required=false) final String costControlTypeId
								,@RequestParam(required=false) final String costControlId
								,@RequestParam(required=false) final String dateBack
								,@RequestParam(required=false) final String bankType
								,@RequestParam(required=false) final String bank
								,@RequestParam(required=false) final String activity
								,@RequestParam(required=false) final String total
								,@RequestParam(required=false) final String items
								,@RequestParam(required=false) final String attendees
								,@RequestParam(required=false) final String files
								,@RequestParam(required=false) final String status
								,final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = null;
			
			if (CommonUtil.isValidId(id)) {
				model = expBrwService.get(id, null);
			}
			
			if (model==null) {
				model = new ExpBrwModel();
			}
			
			if (model.getId() == null) {
				model.setStatus(ExpBrwConstant.ST_DRAFT);
			}
			
			model.setReqBy(reqBy);
			model.setObjectiveType(objectiveType);
			model.setObjective(objective);
			model.setAvRemark(avRemark);
			model.setReason(reason);
			model.setNote(note);
			
			model.setIsSmallAmount(isSmallAmount);
			model.setOriginPrNumber(originPrNumber);
			
			model.setIsReason(isReason);
			
			if (budgetCc != null && !budgetCc.equals("")) {
				model.setBudgetCc(Integer.parseInt(budgetCc));
			}
			model.setBudgetCcType(budgetCcType);
			if (fundId != null && !fundId.equals("")) {
				model.setFundId(Integer.parseInt(fundId));
			}
			
			model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
			model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
			if (dateBack!=null) {
				model.setDateBack(CommonDateTimeUtil.convertSenchaStringToTimestamp(dateBack));
			}
			
			model.setBankType(bankType);
			if (bank != null && !bank.equals("")) {
				model.setBank(Integer.parseInt(bank));
			}
			model.setTotal(Double.parseDouble(total));
			model.setStatus(status);
			
			if (model.getId() == null || (status!=null && status.equals(ExpBrwConstant.ST_DRAFT))) {
				model.setStatus(ExpBrwConstant.ST_WAITING);
				model.setWaitingLevel(1);
			}
			
//			JSONObject validateResult = expBrwService.validateAssignee(model);
//			if (!(Boolean)validateResult.get("valid")) {
//				json = CommonUtil.jsonFail(validateResult);
//			}
//			else {		
				
				model.setAttendeeList(ExpBrwAttendeeUtil.convertJsonToList(attendees, model.getId()));
				model.setDtlList(ExpBrwDtlUtil.convertJsonToList(items, model.getId()));
				String fileName = expBrwService.doGenDoc(session, ExpBrwConstant.JR_AV, model);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(fileName, "");
				
				json = CommonUtil.jsonSuccess(map);
				session.commit();
//			}
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
	
	@Uri(URI_PREFIX+"/brw/item/list")
	public void handleItemList(@RequestParam final String id,
			  final WebScriptResponse response)
	      throws Exception {
			
		String json = null;
		 
		try {
			List<ExpBrwDtlModel> list = expBrwService.listDtlByMasterId(id);
			if (list!=null && list.size()>0) {
				ExpBrwModel model = expBrwService.get(id, "");
				
				ExpBrwDtlUtil.addAction(list, model);
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
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/finish")
	public void handleFinish(@RequestParam(required=false) final String id
							,@RequestParam(required=false) final String reqBy
			  				,@RequestParam(required=false) final String objectiveType
			  				,@RequestParam(required=false) final String objective
			  				,@RequestParam(required=false) final String avRemark
							,@RequestParam(required=false) final String isReason
			  				,@RequestParam(required=false) final String reason
			  				,@RequestParam(required=false) final String note
							,@RequestParam(required=false) final String isSmallAmount
							,@RequestParam(required=false) final String originPrNumber
							,@RequestParam(required=false) final String budgetCc
							,@RequestParam(required=false) final String budgetCcType
							,@RequestParam(required=false) final String fundId
							,@RequestParam(required=false) final String costControlTypeId
							,@RequestParam(required=false) final String costControlId
							,@RequestParam(required=false) final String dateBack
							,@RequestParam(required=false) final String bankType
							,@RequestParam(required=false) final String bank
							,@RequestParam(required=false) final String total
			  				,@RequestParam(required=false) final String items
			  				,@RequestParam(required=false) final String attendees
			  				,@RequestParam(required=false) final String files
							,@RequestParam(required=false) final String status
							,@RequestParam(required=false) String lang
			  				,final WebScriptResponse response) throws Exception {
		
		log.info("/finish"); 

		String json = null;
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = null;
			
			if (CommonUtil.isValidId(id)) {
				model = expBrwService.get(id, null);
			}
			
			if (model==null) {
				model = new ExpBrwModel();
			}
			model.setWaitingLevel(0);
			
			model.setReqBy(reqBy);
			model.setObjectiveType(objectiveType);
			model.setObjective(objective);
			model.setAvRemark(avRemark);
			model.setReason(reason);
			model.setNote(note);
			
			model.setIsSmallAmount(isSmallAmount);
			model.setOriginPrNumber(originPrNumber);
			
			model.setIsReason(isReason);
			
			if (budgetCc != null && !budgetCc.equals("")) {
				model.setBudgetCc(Integer.parseInt(budgetCc));
			}
			model.setBudgetCcType(budgetCcType);
			if (fundId != null && !fundId.equals("")) {
				model.setFundId(Integer.parseInt(fundId));
			}
			
			model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
			model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
			if (dateBack!=null) {
				model.setDateBack(CommonDateTimeUtil.convertSenchaStringToTimestamp(dateBack));
			}
			
			model.setBankType(bankType);
			if (bank != null && !bank.equals("")) {
				model.setBank(Integer.parseInt(bank));
			}
			model.setTotal(Double.parseDouble(total));
			model.setStatus(status);
			
			if (model.getId() == null || (status!=null && status.equals(ExpBrwConstant.ST_DRAFT))) {
				model.setStatus(ExpBrwConstant.ST_WAITING);
				model.setWaitingLevel(1);
			}
			
			JSONObject jobj = new JSONObject(model); 
			log.info("model="+jobj.toString());
			
//			JSONObject validateResult = expBrwService.validateAssignee(model);
//			if (!(Boolean)validateResult.get("valid")) {
//				json = CommonUtil.jsonFail(validateResult);
//			}
//			else {
				mainWorkflowService.setModuleService(expBrwService);
				Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_AV, model);
				JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
				if (!(Boolean)validateWfPath.get("valid")) {
					json = CommonUtil.jsonFail(validateWfPath);
				}
				else {
					model = expBrwService.save(session, model, items, attendees, files, true);
					mainWorkflowService.updateWorkflow(session, model, null, null, bossMap);
					
					JSONObject jsObj = new JSONObject();
					jsObj.put("id", model.getId());
					json = CommonUtil.jsonSuccess(jsObj);
					
					session.commit();
				}
//			}		

//			String createResult = interfaceService.createAV(model);
//			log.info("createAVResult="+createResult);
			
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

    @Uri(method=HttpMethod.POST, value=URI_PREFIX+"/brw/genDoc")
    public void handleGenDoc(@RequestParam(required=false) final String id
			  , final WebScriptResponse response)  throws Exception {
    	String json = null;
    	
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = expBrwService.get(id, "th");
			
			model.setDtlList(expBrwService.listDtlByMasterId(model.getId()));
			model.setAttendeeList(expBrwService.listAttendeeByMasterId(model.getId()));
			
			model = expBrwService.genDoc(session, model);
			
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
    
	@Transaction(value=TransactionType.REQUIRES_NEW)
	@Uri(method=HttpMethod.POST, value=URI_PREFIX+"/am")
	public void handleAM(@RequestParam(required=false) final String id
							,@RequestParam(required=false) final String reqBy
			  				,@RequestParam(required=false) final String objectiveType
			  				,@RequestParam(required=false) final String objective
			  				,@RequestParam(required=false) final String avRemark
							,@RequestParam(required=false) final String isReason
			  				,@RequestParam(required=false) final String reason
			  				,@RequestParam(required=false) final String note
							,@RequestParam(required=false) final String isSmallAmount
							,@RequestParam(required=false) final String originPrNumber
							,@RequestParam(required=false) final String budgetCc
							,@RequestParam(required=false) final String budgetCcType
							,@RequestParam(required=false) final String fundId
							,@RequestParam(required=false) final String costControlTypeId
							,@RequestParam(required=false) final String costControlId
							,@RequestParam(required=false) final String dateBack
							,@RequestParam(required=false) final String bankType
							,@RequestParam(required=false) final String bank
							,@RequestParam(required=false) final String total
			  				,@RequestParam(required=false) final String items
			  				,@RequestParam(required=false) final String attendees
			  				,@RequestParam(required=false) final String files
							,@RequestParam(required=false) final String status
							,@RequestParam(required=false) String lang
			  				,final WebScriptResponse response) throws Exception {
		
		String json = null;
		
		SqlSession session = ExpUtil.openSession(dataSource);
		
		try {
			ExpBrwModel model = null;
			
//			if (CommonUtil.isValidId(id)) {
//				model = expBrwService.get(id, null);
//			}
//			
			if (model==null) {
				model = new ExpBrwModel();
			}
			
			model.setReqBy(reqBy);
			
//			model.setObjectiveType(objectiveType);
//			model.setObjective(objective);
//			model.setAvRemark(avRemark);
//			model.setReason(reason);
//			model.setNote(note);
			
			model.setIsSmallAmount(isSmallAmount);
			model.setOriginPrNumber(originPrNumber);
			
//			model.setIsReason(isReason);
			
			if (budgetCc != null && !budgetCc.equals("")) {
				model.setBudgetCc(Integer.parseInt(budgetCc));
			}
			model.setBudgetCcType(budgetCcType);
//			if (fundId != null && !fundId.equals("")) {
//				model.setFundId(Integer.parseInt(fundId));
//			}
			
//			model.setCostControlTypeId((costControlTypeId != null && !costControlTypeId.equals("")) ? Integer.parseInt(costControlTypeId) : null);
//			model.setCostControlId((costControlId != null && !costControlId.equals("")) ? Integer.parseInt(costControlId) : null);
//			if (dateBack!=null) {
//				model.setDateBack(CommonDateTimeUtil.convertSenchaStringToTimestamp(dateBack));
//			}
			
//			model.setBankType(bankType);
//			if (bank != null && !bank.equals("")) {
//				model.setBank(Integer.parseInt(bank));
//			}
			model.setTotal(Double.parseDouble(total));
//			model.setStatus(status);
//			
//			model.setRequestedTime(CommonDateTimeUtil.now());
			
			JSONObject jobj = new JSONObject(model); 
			log.info("model="+jobj.toString());
			
			mainWorkflowService.setModuleService(expBrwService);
			Map<String, String> bossMap = mainWorkflowService.getBossMap(MainWkfConfigDocTypeConstant.DT_AV, model);
			JSONObject validateWfPath = mainWorkflowService.validateWfPath(bossMap, model);
			if (!(Boolean)validateWfPath.get("valid")) {
				json = CommonUtil.jsonFail(validateWfPath);
			}
			else {
				String msg = expBrwService.getWorkflowPath(model, bossMap);
				json = CommonUtil.jsonSuccess(msg);
			}

//			String createResult = interfaceService.createAV(model);
//			log.info("createAVResult="+createResult);
			
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
	
}
