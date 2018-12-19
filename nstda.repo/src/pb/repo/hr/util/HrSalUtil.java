package pb.repo.hr.util;

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
import pb.repo.hr.constant.HrSalConstant;
import pb.repo.hr.model.HrSalModel;

public class HrSalUtil {
	
	private static Logger log = Logger.getLogger(HrSalUtil.class);
	
	public static JSONObject convertToJSONObject(HrSalModel model) throws Exception {
		if (model == null) {
			return null;
		}
		
		JSONObject jsObj = new JSONObject();
		
		jsObj.put(HrSalConstant.JFN_ID, model.getId());
		
		jsObj.put(HrSalConstant.JFN_OBJECTIVE, model.getObjective());
		
		jsObj.put(HrSalConstant.JFN_REQ_BY_NAME, model.getReqByName());
		
		jsObj.put(HrSalConstant.JFN_SECTION_ID, model.getSectionId());
		jsObj.put(HrSalConstant.JFN_DOC_TYPE, model.getDocType());
		
		jsObj.put(HrSalConstant.JFN_ORG_NAME, model.getOrgName());
		
		jsObj.put(HrSalConstant.JFN_TOTAL, String.valueOf(model.getTotal()));
		DecimalFormat df = new DecimalFormat(CommonConstant.MONEY_FORMAT);
		jsObj.put(HrSalConstant.JFN_TOTAL_SHOW, df.format(model.getTotal()!=null ? model.getTotal() : 0));
		
		jsObj.put(HrSalConstant.JFN_WORKFLOW_INS_ID, model.getWorkflowInsId());
		jsObj.put(HrSalConstant.JFN_DOC_REF, model.getDocRef());
		jsObj.put(HrSalConstant.JFN_FOLDER_REF, model.getFolderRef());
		jsObj.put(HrSalConstant.JFN_WAITING_LEVEL, String.valueOf(model.getWaitingLevel()));
		jsObj.put(HrSalConstant.JFN_STATUS, model.getStatus());
		jsObj.put(HrSalConstant.JFN_WF_STATUS, model.getWfStatus());
		jsObj.put(HrSalConstant.JFN_CREATED_TIME_SHOW, CommonDateTimeUtil.convertToGridDateTime(model.getCreatedTime()));
		jsObj.put(HrSalConstant.JFN_CREATED_TIME, model.getCreatedTime());
		jsObj.put(HrSalConstant.JFN_CREATED_BY, model.getCreatedBy());
		jsObj.put(HrSalConstant.JFN_UPDATED_TIME, CommonDateTimeUtil.convertToGridDate(model.getUpdatedTime()));
		jsObj.put(HrSalConstant.JFN_UPDATED_BY, model.getUpdatedBy());
		jsObj.put(HrSalConstant.JFN_ACTION, getAction(model));
		
		return jsObj;
	}
	
	private static String getAction(HrSalModel model) {
		StringBuffer action = new StringBuffer();
		
		action.append(HrSalConstant.ACTION_SHOW_HISTORY);
		if (model.getStatus()!=null) {
			if (model.getStatus().equals(HrSalConstant.ST_WAITING)) {
				action.append(HrSalConstant.ACTION_SHOW_DIAGRAM);
			}
			
			if (model.getFolderRef() != null && !model.getFolderRef().trim().equals("")) {
				action.append(HrSalConstant.ACTION_GOTO_FOLDER);
			}
			
			if (model.getDocRef() != null && !model.getDocRef().trim().equals("")) {
				action.append(HrSalConstant.ACTION_SHOW_DETAIL);
			}
		}
		
		return action.toString();
	}
	
	public static String getAction(Map<String,Object> map) {
		StringBuffer action = new StringBuffer();
		
		action.append(HrSalConstant.ACTION_SHOW_HISTORY);
		if (map.get(HrSalConstant.JFN_STATUS)!=null) {
			if (map.get(HrSalConstant.JFN_STATUS).equals(HrSalConstant.ST_WAITING)) {
				action.append(HrSalConstant.ACTION_SHOW_DIAGRAM);
			}
			
			if (map.get(HrSalConstant.JFN_FOLDER_REF) != null && !((String)map.get(HrSalConstant.JFN_FOLDER_REF)).trim().equals("")) {
				action.append(HrSalConstant.ACTION_GOTO_FOLDER);
			}
			
			if (map.get(HrSalConstant.JFN_DOC_REF) != null && !((String)map.get(HrSalConstant.JFN_DOC_REF)).trim().equals("")) {
				action.append(HrSalConstant.ACTION_SHOW_DETAIL);
			}
		}
		
		return action.toString();
	}
	
	public static JSONArray convertToJSONArray(List<HrSalModel> inList) throws Exception {
		JSONArray jsArr = new JSONArray();
		
		for(HrSalModel model : inList) {
			jsArr.put(convertToJSONObject(model));
		}
		
		return jsArr;
	}
	
	public static String jsonSuccess(List<HrSalModel> list) throws Exception {
		
		Long total = list.size() > 0 ? list.get(0).getTotalRowCount() : 0;

		JSONObject jsonObj = new JSONObject();

		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.TOTAL,  total);
		jsonObj.put(JsonConstant.DATA, convertToJSONArray(list));
		
		return jsonObj.toString();
	}
	
	public static String getMessage(String key, Locale locale) {
		return CommonUtil.getMessage(CommonConstant.MODULE_HR, key, locale);
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
