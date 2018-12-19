package pb.repo.exp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pb.common.constant.CommonConstant;
import pb.repo.exp.constant.ExpBrwDtlConstant;
import pb.repo.exp.model.ExpBrwDtlModel;
import pb.repo.exp.model.ExpBrwModel;

public class ExpBrwDtlUtil {
	
	public static String jsonSuccess(List<ExpBrwDtlModel> list) throws JSONException {
		
		JSONObject jsonObj = new JSONObject();
		
		for(ExpBrwDtlModel model : list) {
			jsonObj.put(model.getActivity(), model.getActivity());
		}
		
		return jsonObj.toString();
	}
	
	public static JSONArray convertToJSONArray(List<ExpBrwDtlModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(ExpBrwDtlModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(ExpBrwDtlConstant.JFN_ID, model.getId());
			jsObj.put(ExpBrwDtlConstant.JFN_MASTER_ID, model.getMasterId());
			jsObj.put(ExpBrwDtlConstant.JFN_ACT_ID, model.getActId());
			jsObj.put(ExpBrwDtlConstant.JFN_ACT_GRP_ID, model.getActGrpId());
			jsObj.put(ExpBrwDtlConstant.JFN_ASSET_RULE_ID, model.getAssetRuleId());
			jsObj.put(ExpBrwDtlConstant.JFN_CONDITION_1, model.getCondition1());
			jsObj.put(ExpBrwDtlConstant.JFN_ACTIVITY, model.getActivity());
			jsObj.put(ExpBrwDtlConstant.JFN_AMOUNT, model.getAmount());
			jsObj.put(ExpBrwDtlConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(ExpBrwDtlConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(ExpBrwDtlConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(ExpBrwDtlConstant.JFN_UPDATED_BY, model.getUpdatedBy());
			jsObj.put(ExpBrwDtlConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}
	
	public static List<ExpBrwDtlModel> convertJsonToList(String json, String masterId) throws Exception {
		List<ExpBrwDtlModel> list = new ArrayList<ExpBrwDtlModel>();
		
		if (json!=null && !json.equals("")) {
			JSONArray jsonArr = new JSONArray(json);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				ExpBrwDtlModel model = new ExpBrwDtlModel();
				model.setMasterId(masterId);
				model.setActId(jsonObj.getInt(ExpBrwDtlConstant.JFN_ACT_ID));
				model.setActGrpId(jsonObj.getInt(ExpBrwDtlConstant.JFN_ACT_GRP_ID));
				model.setActName(jsonObj.getString(ExpBrwDtlConstant.JFN_ACT_NAME));
				model.setActGrpName(jsonObj.getString(ExpBrwDtlConstant.JFN_ACT_GRP_NAME));
				Object o = jsonObj.get(ExpBrwDtlConstant.JFN_ASSET_RULE_ID);
				model.setAssetRuleId(o!=null && !o.equals("null") && !o.equals("") ? jsonObj.getInt(ExpBrwDtlConstant.JFN_ASSET_RULE_ID) : null);
				model.setAssetName(jsonObj.getString(ExpBrwDtlConstant.JFN_ASSET_NAME));
				model.setCondition1(jsonObj.getString(ExpBrwDtlConstant.JFN_CONDITION_1));
				model.setActivity(jsonObj.getString(ExpBrwDtlConstant.JFN_ACTIVITY));
				model.setAmount(jsonObj.getDouble(ExpBrwDtlConstant.JFN_AMOUNT));
				
				list.add(model);
			}
		}
		
		return list;
	}
	
	public static void addAction(List<ExpBrwDtlModel> list, ExpBrwModel model) {
		String action = null;
		if (model!=null && model.getIsSmallAmount()!=null && model.getIsSmallAmount().equals(CommonConstant.V_ENABLE)) {
			action = "E";
		} else {
			action = "CED";
		}
		
		for (ExpBrwDtlModel item : list) {
			item.setAction(action);
		}
	}	
}
