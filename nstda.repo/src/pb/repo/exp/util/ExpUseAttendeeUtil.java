package pb.repo.exp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pb.repo.exp.constant.ExpUseAttendeeConstant;
import pb.repo.exp.model.ExpUseAttendeeModel;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;

public class ExpUseAttendeeUtil {
	
	public static String jsonSuccess(List<ExpUseAttendeeModel> list) throws JSONException {
		
		JSONObject jsonObj = new JSONObject();
		
		for(ExpUseAttendeeModel model : list) {
			jsonObj.put(model.getFname(), model.getLname());
		}
		
		return jsonObj.toString();
	}
	
	public static JSONArray convertToJSONArray(List<ExpUseAttendeeModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(ExpUseAttendeeModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(ExpUseAttendeeConstant.JFN_ID, model.getId());
			jsObj.put(ExpUseAttendeeConstant.JFN_MASTER_ID, model.getMasterId());
			jsObj.put(ExpUseAttendeeConstant.JFN_TYPE, model.getType());
			jsObj.put(ExpUseAttendeeConstant.JFN_CODE, model.getCode());
			jsObj.put(ExpUseAttendeeConstant.JFN_TITLE, model.getTitle());
			jsObj.put(ExpUseAttendeeConstant.JFN_FNAME, model.getFname());
			jsObj.put(ExpUseAttendeeConstant.JFN_LNAME, model.getLname());
			jsObj.put(ExpUseAttendeeConstant.JFN_UNIT_TYPE, model.getUnitType());
			jsObj.put(ExpUseAttendeeConstant.JFN_POSITION, model.getPosition());
			jsObj.put(ExpUseAttendeeConstant.JFN_POSITION_ID, model.getPositionId());
			jsObj.put(ExpUseAttendeeConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(ExpUseAttendeeConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(ExpUseAttendeeConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(ExpUseAttendeeConstant.JFN_UPDATED_BY, model.getUpdatedBy());
			jsObj.put(ExpUseAttendeeConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}
	
	public static List<ExpUseAttendeeModel> convertJsonToList(String json, String masterId) throws Exception {
		List<ExpUseAttendeeModel> list = new ArrayList<ExpUseAttendeeModel>();
		
		if (json!=null && !json.equals("")) {
			JSONArray jsonArr = new JSONArray(json);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				ExpUseAttendeeModel model = new ExpUseAttendeeModel();
				model.setMasterId(masterId);
				model.setType(jsonObj.getString(ExpUseAttendeeConstant.JFN_TYPE));
				model.setCode(jsonObj.getString(ExpUseAttendeeConstant.JFN_CODE));
				
				String tid = null;
				try {
					tid = jsonObj.getString(ExpUseAttendeeConstant.JFN_TID);
				} catch (Exception ex) {
					
				}
				if (tid!=null && !tid.equals("null")) {
					model.setTitle(jsonObj.getString(ExpUseAttendeeConstant.JFN_TID));
				} else {
					model.setTitle(jsonObj.getString(ExpUseAttendeeConstant.JFN_TITLE)!=null && !jsonObj.getString(ExpUseAttendeeConstant.JFN_TITLE).equals("null") ? jsonObj.getString(ExpUseAttendeeConstant.JFN_TITLE) : "");
				}
				model.setFname(jsonObj.getString(ExpUseAttendeeConstant.JFN_FNAME));
				model.setLname(jsonObj.getString(ExpUseAttendeeConstant.JFN_LNAME));
				model.setUnitType(jsonObj.getString(ExpUseAttendeeConstant.JFN_UNIT_TYPE));
				model.setPosition(jsonObj.getString(ExpUseAttendeeConstant.JFN_POSITION));
				String posId = jsonObj.getString(ExpUseAttendeeConstant.JFN_POSITION_ID);
				if (posId!=null && !posId.equals("")) {
					model.setPositionId(Integer.parseInt(posId));
				}
				
				list.add(model);
			}
		}
		
		return list;
	}
	
	public static List<ExpUseAttendeeModel> convertXmlRpcArrayToList(XmlRpcArray array, String masterId) throws Exception {
		List<ExpUseAttendeeModel> list = new ArrayList<ExpUseAttendeeModel>();
		
		if (array!=null) {
			for(Object o : array) {
				XmlRpcStruct s = (XmlRpcStruct)o;
				
				ExpUseAttendeeModel model = new ExpUseAttendeeModel();
				model.setMasterId(masterId);
				model.setType(s.getString(ExpUseAttendeeConstant.JFN_TYPE));
				model.setCode(s.getString(ExpUseAttendeeConstant.JFN_CODE));
				model.setTitle(s.getString(ExpUseAttendeeConstant.JFN_TITLE)!=null && !s.getString(ExpUseAttendeeConstant.JFN_TITLE).equals("null") ? s.getString(ExpUseAttendeeConstant.JFN_TITLE) : "");
				model.setFname(s.getString(ExpUseAttendeeConstant.JFN_FNAME));
				model.setLname(s.getString(ExpUseAttendeeConstant.JFN_LNAME));
				model.setUnitType(s.getString(ExpUseAttendeeConstant.JFN_UNIT_TYPE));
				model.setPosition(s.getString(ExpUseAttendeeConstant.JFN_POSITION));
				String posId = s.getString(ExpUseAttendeeConstant.JFN_POSITION_ID);
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
