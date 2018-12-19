package pb.repo.exp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pb.repo.exp.constant.ExpBrwAttendeeConstant;
import pb.repo.exp.model.ExpBrwAttendeeModel;

public class ExpBrwAttendeeUtil {
	
	public static String jsonSuccess(List<ExpBrwAttendeeModel> list) throws JSONException {
		
		JSONObject jsonObj = new JSONObject();
		
		for(ExpBrwAttendeeModel model : list) {
			jsonObj.put(model.getFname(), model.getLname());
		}
		
		return jsonObj.toString();
	}
	
	public static JSONArray convertToJSONArray(List<ExpBrwAttendeeModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(ExpBrwAttendeeModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(ExpBrwAttendeeConstant.JFN_ID, model.getId());
			jsObj.put(ExpBrwAttendeeConstant.JFN_MASTER_ID, model.getMasterId());
			jsObj.put(ExpBrwAttendeeConstant.JFN_TYPE, model.getType());
			jsObj.put(ExpBrwAttendeeConstant.JFN_CODE, model.getCode());
			jsObj.put(ExpBrwAttendeeConstant.JFN_TITLE, model.getTitle());
			jsObj.put(ExpBrwAttendeeConstant.JFN_FNAME, model.getFname());
			jsObj.put(ExpBrwAttendeeConstant.JFN_LNAME, model.getLname());
			jsObj.put(ExpBrwAttendeeConstant.JFN_UNIT_TYPE, model.getUnitType());
			jsObj.put(ExpBrwAttendeeConstant.JFN_POSITION, model.getPosition());
			jsObj.put(ExpBrwAttendeeConstant.JFN_POSITION_ID, model.getPositionId());
			jsObj.put(ExpBrwAttendeeConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(ExpBrwAttendeeConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(ExpBrwAttendeeConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(ExpBrwAttendeeConstant.JFN_UPDATED_BY, model.getUpdatedBy());
			jsObj.put(ExpBrwAttendeeConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}
	
	public static List<ExpBrwAttendeeModel> convertJsonToList(String json, String masterId) throws Exception {
		List<ExpBrwAttendeeModel> list = new ArrayList<ExpBrwAttendeeModel>();
		
		if (json!=null && !json.equals("")) {
			JSONArray jsonArr = new JSONArray(json);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				ExpBrwAttendeeModel model = new ExpBrwAttendeeModel();
				model.setMasterId(masterId);
				model.setType(jsonObj.getString(ExpBrwAttendeeConstant.JFN_TYPE));
				model.setCode(jsonObj.getString(ExpBrwAttendeeConstant.JFN_CODE));
				
				String tid = null;
				try {
					tid = jsonObj.getString(ExpBrwAttendeeConstant.JFN_TID);
				} catch (Exception ex) {
					
				}
				if (tid!=null && !tid.equals("null")) {
					model.setTitle(jsonObj.getString(ExpBrwAttendeeConstant.JFN_TID));
				} else {
					model.setTitle(jsonObj.getString(ExpBrwAttendeeConstant.JFN_TITLE)!=null && !jsonObj.getString(ExpBrwAttendeeConstant.JFN_TITLE).equals("null") ? jsonObj.getString(ExpBrwAttendeeConstant.JFN_TITLE) : "");
				}
				model.setFname(jsonObj.getString(ExpBrwAttendeeConstant.JFN_FNAME));
				model.setLname(jsonObj.getString(ExpBrwAttendeeConstant.JFN_LNAME));
				model.setUnitType(jsonObj.getString(ExpBrwAttendeeConstant.JFN_UNIT_TYPE));
				model.setPosition(jsonObj.getString(ExpBrwAttendeeConstant.JFN_POSITION));
				String posId = jsonObj.getString(ExpBrwAttendeeConstant.JFN_POSITION_ID);
				if (posId!=null && !posId.equals("")) {
					model.setPositionId(Integer.parseInt(posId));
				}
				
				list.add(model);
			}
		}
		
		return list;
	}
	
	public static void addAction(List<Map<String, Object>> list) {
		for (Map<String, Object> model : list) {
			model.put("action","ED");
		}
	}	
}
