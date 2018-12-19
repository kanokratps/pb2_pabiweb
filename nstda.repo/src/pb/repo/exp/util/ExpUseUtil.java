package pb.repo.exp.util;

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
import pb.repo.admin.constant.ExpBrwConstant;
import pb.repo.admin.constant.ExpUseConstant;
import pb.repo.exp.model.ExpUseAttendeeModel;
import pb.repo.exp.model.ExpUseModel;

public class ExpUseUtil {
	
	private static Logger log = Logger.getLogger(ExpUseUtil.class);
	
	public static JSONObject convertToJSONObject(ExpUseModel model, Boolean showDelBtn) throws Exception {
		if (model == null) {
			return null;
		}
		
		JSONObject jsObj = new JSONObject();
		
		jsObj.put(ExpUseConstant.JFN_ID, model.getId());
		
		jsObj.put(ExpUseConstant.JFN_REQ_BY, model.getReqBy());
		jsObj.put(ExpUseConstant.JFN_REQ_BY_NAME, model.getReqByName());
		
		jsObj.put(ExpUseConstant.JFN_OBJECTIVE, model.getObjective());
		jsObj.put(ExpUseConstant.JFN_IS_REASON, model.getIsReason());
		jsObj.put(ExpUseConstant.JFN_REASON, model.getReason());
		jsObj.put(ExpUseConstant.JFN_NOTE, model.getNote());
		
		jsObj.put(ExpBrwConstant.JFN_IS_SMALL_AMOUNT, model.getIsSmallAmount());
		
		jsObj.put(ExpUseConstant.JFN_BUDGET_CC, model.getBudgetCc());
		jsObj.put(ExpUseConstant.JFN_BUDGET_CC_NAME, model.getBudgetCcName());
		jsObj.put(ExpUseConstant.JFN_BUDGET_CC_TYPE, model.getBudgetCcType());
		jsObj.put(ExpUseConstant.JFN_BUDGET_CC_TYPE_NAME, model.getBudgetCcTypeName());
		
		jsObj.put(ExpUseConstant.JFN_FUND_ID, model.getFundId());
		jsObj.put(ExpUseConstant.JFN_FUND_NAME, model.getFundName());
		
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_ID, model.getCostControlId());
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_TYPE_ID, model.getCostControlTypeId());
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL, model.getCostControl());
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_FROM, CommonDateTimeUtil.convertToSenchaFieldDateTime(model.getCostControlFrom()));
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_TO, CommonDateTimeUtil.convertToSenchaFieldDateTime(model.getCostControlTo()));
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_NAME, model.getCostControlName());
		jsObj.put(ExpUseConstant.JFN_COST_CONTROL_TYPE_NAME, model.getCostControlTypeName());
		
		jsObj.put(ExpUseConstant.JFN_BANK_TYPE, model.getBankType());
		jsObj.put(ExpUseConstant.JFN_BANK, model.getBank());
		
		jsObj.put(ExpUseConstant.JFN_PR_TOTAL, model.getPrTotal());

		jsObj.put(ExpUseConstant.JFN_PAY_TYPE, model.getPayType());
		jsObj.put(ExpUseConstant.JFN_PAY_TYPE_NAME, model.getPayTypeName());
		jsObj.put(ExpUseConstant.JFN_PAY_DTL1, model.getPayDtl1());
		jsObj.put(ExpUseConstant.JFN_PAY_DTL2, model.getPayDtl2());
		jsObj.put(ExpUseConstant.JFN_PAY_DTL3, model.getPayDtl3());
		
		jsObj.put(ExpUseConstant.JFN_ICHARGE_TYPE_NAME, model.getIchargeTypeName());
		jsObj.put(ExpUseConstant.JFN_ICHARGE_NAME, model.getIchargeName());
		
		jsObj.put(ExpUseConstant.JFN_EMOTION, model.getEmotion()!=null && model.getEmotion()==1 ? "1" : "");
		
		jsObj.put(ExpUseConstant.JFN_TOTAL, model.getTotal());
		DecimalFormat df = new DecimalFormat(CommonConstant.MONEY_FORMAT);
		jsObj.put(ExpUseConstant.JFN_TOTAL_SHOW, df.format(model.getTotal()!=null ? model.getTotal() : 0));
		jsObj.put(ExpUseConstant.JFN_WORKFLOW_INS_ID, model.getWorkflowInsId());
		jsObj.put(ExpUseConstant.JFN_DOC_REF, model.getDocRef());
		jsObj.put(ExpUseConstant.JFN_FOLDER_REF, model.getFolderRef());
		jsObj.put(ExpUseConstant.JFN_WAITING_LEVEL, String.valueOf(model.getWaitingLevel()));
		jsObj.put(ExpUseConstant.JFN_STATUS, model.getStatus());
		jsObj.put(ExpUseConstant.JFN_WF_STATUS, model.getWfStatus());
		jsObj.put(ExpUseConstant.JFN_OVER_DUE, model.getOverDue());
		jsObj.put(ExpUseConstant.JFN_CREATED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getCreatedTime()));
		jsObj.put(ExpUseConstant.JFN_CREATED_TIME, CommonDateTimeUtil.convertToSenchaFieldDateTime(model.getCreatedTime()));
		jsObj.put(ExpUseConstant.JFN_CREATED_BY, model.getCreatedBy());
		jsObj.put(ExpUseConstant.JFN_UPDATED_TIME, CommonDateTimeUtil.convertToGridDate(model.getUpdatedTime()));
		jsObj.put(ExpUseConstant.JFN_UPDATED_BY, model.getUpdatedBy());
		jsObj.put(ExpUseConstant.JFN_ACTION, getAction(model, showDelBtn));
		
		jsObj.put(ExpUseConstant.JFN_REQUESTED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getRequestedTime()));
		
		return jsObj;
	}
	
	public static Map<String, Object> convertToMap(ExpUseModel model, Boolean showDelBtn) {
		if (model == null) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(ExpUseConstant.JFN_ID, model.getId());
		
		map.put(ExpUseConstant.JFN_REQ_BY, model.getReqBy());
		
		map.put(ExpUseConstant.JFN_OBJECTIVE, model.getObjective());
		map.put(ExpUseConstant.JFN_NOTE, model.getNote());
		
		map.put(ExpUseConstant.JFN_IS_SMALL_AMOUNT, model.getIsSmallAmount());
		
		map.put(ExpUseConstant.JFN_BUDGET_CC, model.getBudgetCc());
		
		map.put(ExpUseConstant.JFN_FUND_ID, model.getFundId());
		
		map.put(ExpUseConstant.JFN_COST_CONTROL_ID, model.getCostControlId());
		map.put(ExpUseConstant.JFN_COST_CONTROL_TYPE_ID, model.getCostControlTypeId());
		
		map.put(ExpUseConstant.JFN_EMOTION, model.getEmotion());
		
		map.put(ExpUseConstant.JFN_BANK_TYPE, model.getBankType());
		map.put(ExpUseConstant.JFN_BANK, model.getBank());

		map.put(ExpUseConstant.JFN_PAY_TYPE, model.getPayType());
		map.put(ExpUseConstant.JFN_PAY_DTL1, model.getPayDtl1());
		map.put(ExpUseConstant.JFN_PAY_DTL2, model.getPayDtl2());
		map.put(ExpUseConstant.JFN_PAY_DTL3, model.getPayDtl3());
		
		map.put(ExpUseConstant.JFN_TOTAL, model.getTotal());
		map.put(ExpUseConstant.JFN_WORKFLOW_INS_ID, model.getWorkflowInsId());
		map.put(ExpUseConstant.JFN_DOC_REF, model.getDocRef());
		map.put(ExpUseConstant.JFN_FOLDER_REF, model.getFolderRef());
		map.put(ExpUseConstant.JFN_WAITING_LEVEL, String.valueOf(model.getWaitingLevel()));
		map.put(ExpUseConstant.JFN_STATUS, model.getStatus());
		map.put(ExpUseConstant.JFN_WF_STATUS, model.getWfStatus());
		map.put(ExpUseConstant.JFN_OVER_DUE, model.getOverDue());
		map.put(ExpUseConstant.JFN_CREATED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getCreatedTime()));
		map.put(ExpUseConstant.JFN_CREATED_TIME, model.getCreatedTime());
		map.put(ExpUseConstant.JFN_CREATED_BY, model.getCreatedBy());
		map.put(ExpUseConstant.JFN_UPDATED_TIME, CommonDateTimeUtil.convertToGridDate(model.getUpdatedTime()));
		map.put(ExpUseConstant.JFN_UPDATED_BY, model.getUpdatedBy());
		map.put(ExpUseConstant.JFN_ACTION, getAction(model, showDelBtn));
		
		return map;
	}
	
	private static String getAction(ExpUseModel model, Boolean showDelBtn) {
		StringBuffer action = new StringBuffer();
		
		action.append(ExpUseConstant.ACTION_COPY);
		if (model.getStatus().equals(ExpUseConstant.ST_DRAFT)) {
			action.append(ExpUseConstant.ACTION_EDIT);
			action.append(ExpUseConstant.ACTION_DELETE);
		}
		else {
			action.append(ExpUseConstant.ACTION_SHOW_HISTORY);
			if (model.getStatus().equals(ExpUseConstant.ST_WAITING)) {
				action.append(ExpUseConstant.ACTION_SHOW_DIAGRAM);
			}
		}
		
		if (model.getFolderRef() != null && !model.getFolderRef().trim().equals("")) {
			action.append(ExpUseConstant.ACTION_GOTO_FOLDER);
		}
		
		if (model.getDocRef() != null && !model.getDocRef().trim().equals("")) {
			action.append(ExpUseConstant.ACTION_SHOW_DETAIL);
		}
		
		return action.toString();
	}
	
	public static String getAction(Map<String, Object> map) {
		StringBuffer action = new StringBuffer();
		
		action.append(ExpUseConstant.ACTION_COPY);
		if (map.get(ExpUseConstant.TFN_STATUS).equals(ExpUseConstant.ST_DRAFT)) {
			action.append(ExpUseConstant.ACTION_EDIT);
			action.append(ExpUseConstant.ACTION_DELETE);
		}
		else {
			action.append(ExpUseConstant.ACTION_SHOW_HISTORY);
			if (map.get(ExpUseConstant.TFN_STATUS).equals(ExpUseConstant.ST_WAITING)) {
				action.append(ExpUseConstant.ACTION_SHOW_DIAGRAM);
			}
			action.append(ExpUseConstant.ACTION_VIEW);
		}
		
		if (map.get(ExpUseConstant.TFN_FOLDER_REF) != null && !((String)map.get(ExpUseConstant.TFN_FOLDER_REF)).trim().equals("")) {
			action.append(ExpUseConstant.ACTION_GOTO_FOLDER);
		}
		
		if (map.get(ExpUseConstant.TFN_DOC_REF) != null && !((String)map.get(ExpUseConstant.TFN_DOC_REF)).trim().equals("")) {
			action.append(ExpUseConstant.ACTION_SHOW_DETAIL);
		}
		
		return action.toString();
	}
	
	public static JSONArray convertToJSONArray(List<ExpUseModel> inList, Boolean showDelBtn) throws Exception {
		JSONArray jsArr = new JSONArray();
		
		for(ExpUseModel model : inList) {
			jsArr.put(convertToJSONObject(model, showDelBtn));
		}
		
		return jsArr;
	}
	
	public static String jsonSuccess(List<ExpUseModel> list, Boolean showDelBtn) throws Exception {
		
		Long total = list.size() > 0 ? list.get(0).getTotalRowCount() : 0;

		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.TOTAL,  total);
		jsonObj.put(JsonConstant.DATA, convertToJSONArray(list, showDelBtn));
		
		return jsonObj.toString();
	}
	
	public static String jsonSuccess(List<ExpUseModel> list, List<ExpUseAttendeeModel> dtlList) throws Exception {
		
		Long total = list.size() > 0 ? list.get(0).getTotalRowCount() : 0;

		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.TOTAL,  total);
		jsonObj.put(JsonConstant.DATA, convertToJSONArray(list, false));
		
		return jsonObj.toString();
	}

	public static String getMessage(String key, Locale locale) {
		return CommonUtil.getMessage(CommonConstant.MODULE_EXP, key, locale);
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
