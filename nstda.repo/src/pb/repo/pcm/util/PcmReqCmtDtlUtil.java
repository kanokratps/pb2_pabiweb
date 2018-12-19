package pb.repo.pcm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pb.common.constant.JsonConstant;
import pb.repo.pcm.constant.PcmReqCmtDtlConstant;
import pb.repo.pcm.model.PcmReqCmtDtlModel;

public class PcmReqCmtDtlUtil {
	
	public static void addAction(List<Map<String, Object>> list) {
		if (list!=null) {
			for (Map<String, Object> map : list) {
				map.put("action", "ED");
			}
		}
	}
	
//	public static String jsonSuccess(List<PcmReqCmtDtlModel> list) throws JSONException {
//		
//		JSONObject jsonObj = new JSONObject();
//		
//		for(PcmReqCmtDtlModel model : list) {
//			jsonObj.put(model.getId(), model.getFirstName()+" "+model.getLastName());
//		}
//		
//		return jsonObj.toString();
//	}
	
	public static String jsonSuccess(List<Map<String, Object>> list, String lang) throws JSONException {
		
		JSONObject jsonObj = new JSONObject();
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		lang = lang!=null && lang.startsWith("th") ? "_th" : "";
		
		int i = 0;
		
		for(Map<String, Object> model : list) {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put(PcmReqCmtDtlConstant.JFN_ID, model.get(PcmReqCmtDtlConstant.TFN_ID));
			String code = (String)model.get(PcmReqCmtDtlConstant.TFN_EMPLOYEE_CODE);
			map.put(PcmReqCmtDtlConstant.JFN_EMPLOYEE_CODE, code);
			if (code!=null && !code.equalsIgnoreCase("")) {
				map.put(PcmReqCmtDtlConstant.JFN_TITLE, model.get("etitle"+lang));
				map.put(PcmReqCmtDtlConstant.JFN_FIRST_NAME, model.get("efirst_name"+lang));
				map.put(PcmReqCmtDtlConstant.JFN_LAST_NAME, model.get("elast_name"+lang));
			} else {
				map.put(PcmReqCmtDtlConstant.JFN_TID, model.get("tid"));
				map.put(PcmReqCmtDtlConstant.JFN_TITLE, model.get("title"));
				map.put(PcmReqCmtDtlConstant.JFN_TITLE_TH, model.get(PcmReqCmtDtlConstant.TFN_TITLE_TH));
				map.put(PcmReqCmtDtlConstant.JFN_FIRST_NAME, model.get(PcmReqCmtDtlConstant.TFN_FIRST_NAME));
				map.put(PcmReqCmtDtlConstant.JFN_LAST_NAME, model.get(PcmReqCmtDtlConstant.TFN_LAST_NAME));
			}
			map.put(PcmReqCmtDtlConstant.JFN_POSITION, model.get(PcmReqCmtDtlConstant.TFN_POSITION));

			if (code!=null && !code.equals("")) {
				map.put(PcmReqCmtDtlConstant.JFN_ACTION, "D" + (i==0 ? "Z" : "U"));
			} else {
				map.put(PcmReqCmtDtlConstant.JFN_ACTION, "ED" + (i==0 ? "Z" : "U"));
			}
			
			i++;
			data.add(map);
		}
		
		jsonObj.put(JsonConstant.SUCCESS,  true);
		jsonObj.put(JsonConstant.DATA, data);
		
		return jsonObj.toString();
	}
	
	public static JSONArray convertToJSONArray(List<PcmReqCmtDtlModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(PcmReqCmtDtlModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(PcmReqCmtDtlConstant.JFN_ID, model.getId());
			jsObj.put(PcmReqCmtDtlConstant.JFN_MASTER_ID, model.getMasterId());
			jsObj.put(PcmReqCmtDtlConstant.JFN_EMPLOYEE_CODE, model.getEmployeeCode());
			jsObj.put(PcmReqCmtDtlConstant.JFN_TITLE, model.getTitle());
			jsObj.put(PcmReqCmtDtlConstant.JFN_FIRST_NAME, model.getFirstName());
			jsObj.put(PcmReqCmtDtlConstant.JFN_LAST_NAME, model.getLastName());
			jsObj.put(PcmReqCmtDtlConstant.JFN_POSITION, String.valueOf(model.getPosition()));
			jsObj.put(PcmReqCmtDtlConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(PcmReqCmtDtlConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(PcmReqCmtDtlConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(PcmReqCmtDtlConstant.JFN_UPDATED_BY, model.getUpdatedBy());
			jsObj.put(PcmReqCmtDtlConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}

}
