package pb.repo.pcm.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import pb.common.constant.CommonConstant;
import pb.common.constant.JsonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.PcmReqConstant;
import pb.repo.pcm.model.PcmReqDtlModel;
import pb.repo.pcm.model.PcmReqModel;

public class PcmReqUtil {
	
	private static Logger log = Logger.getLogger(PcmReqUtil.class);
	
	public static JSONObject convertToJSONObject(PcmReqModel model, Boolean showDelBtn) throws Exception {
		if (model == null) {
			return null;
		}
		
		JSONObject jsObj = new JSONObject();
		
		jsObj.put(PcmReqConstant.JFN_ID, model.getId());
		
		jsObj.put(PcmReqConstant.JFN_REQ_BY, model.getReqBy());
		jsObj.put(PcmReqConstant.JFN_REQ_SECTION_ID, model.getReqSectionId());
		jsObj.put(PcmReqConstant.JFN_REQ_BY_NAME, model.getReqByName());
		
		jsObj.put(PcmReqConstant.JFN_OBJECTIVE_TYPE, model.getObjectiveType());
		jsObj.put(PcmReqConstant.JFN_OBJECTIVE_TYPE_NAME, model.getObjectiveTypeName());
		jsObj.put(PcmReqConstant.JFN_OBJECTIVE, model.getObjective());
		jsObj.put(PcmReqConstant.JFN_REASON, model.getReason());
		
		jsObj.put(PcmReqConstant.JFN_CURRENCY, model.getCurrency());
		jsObj.put(PcmReqConstant.JFN_CURRENCY_RATE, model.getCurrencyRate());
		
		jsObj.put(PcmReqConstant.JFN_BUDGET_CC, model.getBudgetCc());
		jsObj.put(PcmReqConstant.JFN_BUDGET_CC_NAME, model.getBudgetCcName());
		jsObj.put(PcmReqConstant.JFN_BUDGET_CC_TYPE, model.getBudgetCcType());
		jsObj.put(PcmReqConstant.JFN_BUDGET_CC_TYPE_NAME, model.getBudgetCcTypeName());

		jsObj.put(PcmReqConstant.JFN_FUND_ID, model.getFundId());
		jsObj.put(PcmReqConstant.JFN_FUND_NAME, model.getFundName());
		
		jsObj.put(PcmReqConstant.JFN_IS_STOCK, model.getIsStock());
		jsObj.put(PcmReqConstant.JFN_STOCK_SECTION_ID, model.getStockSectionId());
		
		jsObj.put(PcmReqConstant.JFN_IS_PROTOTYPE, model.getIsPrototype());
		jsObj.put(PcmReqConstant.JFN_PROTOTYPE_TYPE, model.getPrototypeType());
		jsObj.put(PcmReqConstant.JFN_PROTOTYPE_NO, model.getPrototypeNo());
		
		jsObj.put(PcmReqConstant.JFN_COST_CONTROL_ID, model.getCostControlId());
		jsObj.put(PcmReqConstant.JFN_COST_CONTROL_NAME, model.getCostControlName());
		jsObj.put(PcmReqConstant.JFN_COST_CONTROL_TYPE_ID, model.getCostControlTypeId());
		jsObj.put(PcmReqConstant.JFN_COST_CONTROL_TYPE_NAME, model.getCostControlTypeName());
		
		jsObj.put(PcmReqConstant.JFN_CONTRACT_DATE, model.getContractDate());
		
		jsObj.put(PcmReqConstant.JFN_PCM_SECTION_ID, model.getPcmSectionId());
		jsObj.put(PcmReqConstant.JFN_LOCATION, model.getLocation());
		
		jsObj.put(PcmReqConstant.JFN_IS_SMALL_AMOUNT, model.getIsSmallAmount());
		
		jsObj.put(PcmReqConstant.JFN_IS_ACROSS_BUDGET, model.getIsAcrossBudget());
		jsObj.put(PcmReqConstant.JFN_ACROSS_BUDGET, model.getAcrossBudget());
		
		jsObj.put(PcmReqConstant.JFN_IS_REF_ID, model.getIsRefId());
		jsObj.put(PcmReqConstant.JFN_REF_ID, model.getRefId());
		
		jsObj.put(PcmReqConstant.JFN_RID, model.getRid());
		
		jsObj.put(PcmReqConstant.JFN_PRWEB_METHOD_ID, model.getPrWebMethodId());
		jsObj.put(PcmReqConstant.JFN_METHOD_COND2_RULE, model.getMethodCond2Rule());
		jsObj.put(PcmReqConstant.JFN_METHOD_COND2, model.getMethodCond2()!=null && !model.getMethodCond2().equals("") ? Integer.parseInt(model.getMethodCond2()) :null);
		jsObj.put(PcmReqConstant.JFN_METHOD_COND2_DTL, model.getMethodCond2Dtl());
		
		jsObj.put(PcmReqConstant.JFN_VAT, model.getVat());
		jsObj.put(PcmReqConstant.JFN_VAT_ID, model.getVatId());
		jsObj.put(PcmReqConstant.JFN_PRICE_INCLUDE, model.getPriceInclude());

		jsObj.put(PcmReqConstant.JFN_FILE_NAME, model.getFileName());
		
		jsObj.put(PcmReqConstant.JFN_TOTAL, model.getTotal());
		DecimalFormat df = new DecimalFormat(CommonConstant.MONEY_FORMAT);
		jsObj.put(PcmReqConstant.JFN_TOTAL_SHOW, df.format(model.getTotal()!=null ? model.getTotal() : 0));
		jsObj.put(PcmReqConstant.JFN_WORKFLOW_INS_ID, model.getWorkflowInsId());
		jsObj.put(PcmReqConstant.JFN_DOC_REF, model.getDocRef());
		jsObj.put(PcmReqConstant.JFN_FOLDER_REF, model.getFolderRef());
		jsObj.put(PcmReqConstant.JFN_WAITING_LEVEL, String.valueOf(model.getWaitingLevel()));
		jsObj.put(PcmReqConstant.JFN_STATUS, model.getStatus());
		jsObj.put(PcmReqConstant.JFN_WF_STATUS, model.getWfStatus());
		jsObj.put(PcmReqConstant.JFN_OVER_DUE, model.getOverDue());
		jsObj.put(PcmReqConstant.JFN_CREATED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getCreatedTime()));
		jsObj.put(PcmReqConstant.JFN_CREATED_TIME, CommonDateTimeUtil.convertToSenchaFieldDateTime(model.getCreatedTime()));
		jsObj.put(PcmReqConstant.JFN_CREATED_BY, model.getCreatedBy());
		jsObj.put(PcmReqConstant.JFN_UPDATED_TIME, CommonDateTimeUtil.convertToGridDate(model.getUpdatedTime()));
		jsObj.put(PcmReqConstant.JFN_UPDATED_BY, model.getUpdatedBy());
		jsObj.put(PcmReqConstant.JFN_ACTION, getAction(model, showDelBtn));
		
		jsObj.put(PcmReqConstant.JFN_REQUESTED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getRequestedTime()));
		
		return jsObj;
	}
	
	public static Map<String, Object> convertToMap(PcmReqModel model, Boolean showDelBtn) {
		if (model == null) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(PcmReqConstant.JFN_ID, model.getId());
		
		map.put(PcmReqConstant.JFN_REQ_BY, model.getReqBy());
		map.put(PcmReqConstant.JFN_REQ_SECTION_ID, model.getReqSectionId());
		
		map.put(PcmReqConstant.JFN_OBJECTIVE_TYPE, model.getObjectiveType());
		map.put(PcmReqConstant.JFN_OBJECTIVE, model.getObjective());
		map.put(PcmReqConstant.JFN_REASON, model.getReason());
		
		map.put(PcmReqConstant.JFN_CURRENCY, model.getCurrency());
		map.put(PcmReqConstant.JFN_CURRENCY_RATE, model.getCurrencyRate());
		
		map.put(PcmReqConstant.JFN_BUDGET_CC, model.getBudgetCc());
		map.put(PcmReqConstant.JFN_STOCK_SECTION_ID, model.getStockSectionId());
		
		map.put(PcmReqConstant.JFN_FUND_ID, model.getFundId());
		
		map.put(PcmReqConstant.JFN_PROTOTYPE_TYPE, model.getPrototypeType());
		map.put(PcmReqConstant.JFN_PROTOTYPE_NO, model.getPrototypeNo());
		map.put(PcmReqConstant.JFN_COST_CONTROL_ID, model.getCostControlId());
		map.put(PcmReqConstant.JFN_COST_CONTROL_TYPE_ID, model.getCostControlTypeId());
		
		map.put(PcmReqConstant.JFN_PCM_SECTION_ID, model.getPcmSectionId());
		map.put(PcmReqConstant.JFN_LOCATION, model.getLocation());
		
		map.put(PcmReqConstant.JFN_ACROSS_BUDGET, model.getAcrossBudget());
		map.put(PcmReqConstant.JFN_REF_ID, model.getRefId());
		
		map.put(PcmReqConstant.JFN_PRWEB_METHOD_ID, model.getPrWebMethodId());
		map.put(PcmReqConstant.JFN_METHOD_COND2_RULE, model.getMethodCond2Rule());
		map.put(PcmReqConstant.JFN_METHOD_COND2, model.getMethodCond2());
		map.put(PcmReqConstant.JFN_METHOD_COND2_DTL, model.getMethodCond2Dtl());
		
		map.put(PcmReqConstant.JFN_TOTAL, model.getTotal());
		map.put(PcmReqConstant.JFN_WORKFLOW_INS_ID, model.getWorkflowInsId());
		map.put(PcmReqConstant.JFN_DOC_REF, model.getDocRef());
		map.put(PcmReqConstant.JFN_FOLDER_REF, model.getFolderRef());
		map.put(PcmReqConstant.JFN_WAITING_LEVEL, String.valueOf(model.getWaitingLevel()));
		map.put(PcmReqConstant.JFN_STATUS, model.getStatus());
		map.put(PcmReqConstant.JFN_WF_STATUS, model.getWfStatus());
		map.put(PcmReqConstant.JFN_OVER_DUE, model.getOverDue());
		map.put(PcmReqConstant.JFN_CREATED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getCreatedTime()));
		map.put(PcmReqConstant.JFN_CREATED_TIME, model.getCreatedTime());
		map.put(PcmReqConstant.JFN_CREATED_BY, model.getCreatedBy());
		map.put(PcmReqConstant.JFN_UPDATED_TIME, CommonDateTimeUtil.convertToGridDate(model.getUpdatedTime()));
		map.put(PcmReqConstant.JFN_UPDATED_BY, model.getUpdatedBy());
		map.put(PcmReqConstant.JFN_ACTION, getAction(model, showDelBtn));
		
		return map;
	}	
	
	private static String getAction(PcmReqModel model, Boolean showDelBtn) {
		StringBuffer action = new StringBuffer();
		
		action.append(PcmReqConstant.ACTION_COPY);
		if (model.getStatus().equals(PcmReqConstant.ST_DRAFT)) {
			action.append(PcmReqConstant.ACTION_EDIT);
			action.append(PcmReqConstant.ACTION_DELETE);
		}
		else {
			action.append(PcmReqConstant.ACTION_SHOW_HISTORY);
			if (model.getStatus().equals(PcmReqConstant.ST_WAITING)) {
				action.append(PcmReqConstant.ACTION_SHOW_DIAGRAM);
			}
		}
		
		if (model.getFolderRef() != null && !model.getFolderRef().trim().equals("")) {
			action.append(PcmReqConstant.ACTION_GOTO_FOLDER);
		}
		
		if (model.getDocRef() != null && !model.getDocRef().trim().equals("")) {
			action.append(PcmReqConstant.ACTION_SHOW_DETAIL);
		}
		
		return action.toString();
	}
	
	public static String getAction(Map<String,Object> map) {
		StringBuffer action = new StringBuffer();
		
		action.append(PcmReqConstant.ACTION_COPY);
		if (map.get(PcmReqConstant.JFN_STATUS).equals(PcmReqConstant.ST_DRAFT)) {
			action.append(PcmReqConstant.ACTION_EDIT);
			action.append(PcmReqConstant.ACTION_DELETE);
		}
		else {
			action.append(PcmReqConstant.ACTION_SHOW_HISTORY);
			if (map.get(PcmReqConstant.JFN_STATUS).equals(PcmReqConstant.ST_WAITING)) {
				action.append(PcmReqConstant.ACTION_SHOW_DIAGRAM);
			}
			action.append(PcmReqConstant.ACTION_VIEW);
		}
		
		if (map.get(PcmReqConstant.JFN_FOLDER_REF) != null && !((String)map.get(PcmReqConstant.JFN_FOLDER_REF)).trim().equals("")) {
			action.append(PcmReqConstant.ACTION_GOTO_FOLDER);
		}
		
		if (map.get(PcmReqConstant.JFN_DOC_REF) != null && !((String)map.get(PcmReqConstant.JFN_DOC_REF)).trim().equals("")) {
			action.append(PcmReqConstant.ACTION_SHOW_DETAIL);
		}
		
		return action.toString();
	}
	
	public static JSONArray convertToJSONArray(List<PcmReqModel> inList, Boolean showDelBtn) throws Exception {
		JSONArray jsArr = new JSONArray();
		
		if (inList!=null) {
			for(PcmReqModel model : inList) {
				jsArr.put(convertToJSONObject(model, showDelBtn));
			}
		}
		
		return jsArr;
	}
	
	public static String jsonSuccess(List<PcmReqModel> list, Boolean showDelBtn) throws Exception {
		
		Long total = list.size() > 0 ? list.get(0).getTotalRowCount() : 0;

		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.TOTAL,  total);
		jsonObj.put(JsonConstant.DATA, convertToJSONArray(list, showDelBtn));
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(List<PcmReqModel> list, List<PcmReqDtlModel> dtlList) throws Exception {
		
		Long total = list!=null && list.size() > 0 ? list.get(0).getTotalRowCount() : 0;

		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.TOTAL,  total);
		jsonObj.put(JsonConstant.DATA, convertToJSONArray(list, false));
		
		JSONObject jsonObjDtl = new JSONObject();
		if (dtlList!=null) {
			for(PcmReqDtlModel model : dtlList) {
				jsonObjDtl.put(model.getDescription(), model.getQuantity());
			}
			jsonObj.put("dtls", jsonObjDtl);
		}
		
		return jsonObj.toString();
	}

	public static String getMessage(String key, Locale locale) {
		return CommonUtil.getMessage(CommonConstant.MODULE_PCM, key, locale);
	}
	
	public static String jsonReportSuccess(List<Map<String,Object>> list) throws Exception {

		Map<String,Object> obj = new HashMap<String,Object>();
	
		obj.put(JsonConstant.SUCCESS,  true);
		obj.put(JsonConstant.TOTAL,  0);
		obj.put(JsonConstant.DATA, list);
		
		return com.alibaba.fastjson.JSONObject.toJSONString(obj);
	}
	
	public static Map<String, Object> dateParameter(String dateFrom, String dateTo, Map<String, Object> params) throws Exception {
		StringBuffer criteria = new StringBuffer();
		if ((dateFrom==null || dateFrom.equalsIgnoreCase("")) && (dateTo==null || dateTo.equalsIgnoreCase(""))) {
			criteria.append("ทั้งหมด");
			params.put("s", "00/00/0000");
			params.put("f", "99/99/9999");
		} else {
			criteria.append("วันที่ : ");
			if(dateFrom==null || dateFrom.equalsIgnoreCase("")) {
				criteria.append("		");
				params.put("s", "00/00/0000");
			}else {
				criteria.append(dateFrom);
				params.put("s", dateFrom);
			}
			criteria.append(" ถึง : ");
	
			if(dateTo==null || dateTo.equalsIgnoreCase("")) {
				criteria.append("		");
				params.put("f", "99/99/9999");
			}else {
				criteria.append(dateTo);
				params.put("f", dateTo);
			}
		}
		params.put("criteria", criteria);
		return params;
	}
	
	public static StringBuffer appenCriteria(StringBuffer sb, String value, String label) throws Exception {
		//log.info("value : " + value);
		if(value!=null && !value.equalsIgnoreCase("")) {
			if(sb.length()>0) sb.append("		");
			sb.append(label + value);
		}
		//log.info("params : " + sb.toString());
		return sb;
		
	}


}
