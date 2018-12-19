package pb.repo.exp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pb.common.constant.CommonConstant;
import pb.repo.exp.constant.ExpBrwDtlConstant;
import pb.repo.exp.constant.ExpUseDtlConstant;
import pb.repo.exp.model.ExpUseDtlModel;
import pb.repo.exp.model.ExpUseModel;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;

public class ExpUseDtlUtil {
	
	public static String jsonSuccess(List<ExpUseDtlModel> list) throws JSONException {
		
		JSONObject jsonObj = new JSONObject();
		
		for(ExpUseDtlModel model : list) {
			jsonObj.put(model.getCondition1(), model.getCondition1());
		}
		
		return jsonObj.toString();
	}
	
	public static JSONArray convertToJSONArray(List<ExpUseDtlModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(ExpUseDtlModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(ExpUseDtlConstant.JFN_ID, model.getId());
			jsObj.put(ExpUseDtlConstant.JFN_MASTER_ID, model.getMasterId());
			jsObj.put(ExpUseDtlConstant.JFN_ACT_ID, model.getActId());
			jsObj.put(ExpUseDtlConstant.JFN_ACT_GRP_ID, model.getActGrpId());
			jsObj.put(ExpUseDtlConstant.JFN_ASSET_RULE_ID, model.getAssetRuleId());
			jsObj.put(ExpUseDtlConstant.JFN_CONDITION_1, model.getCondition1());
			jsObj.put(ExpUseDtlConstant.JFN_CONDITION_2, model.getCondition2());
			jsObj.put(ExpUseDtlConstant.JFN_POSITION, model.getPosition());
			jsObj.put(ExpUseDtlConstant.JFN_UOM, model.getUom());
			jsObj.put(ExpUseDtlConstant.JFN_AMOUNT, model.getAmount());
			jsObj.put(ExpUseDtlConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(ExpUseDtlConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(ExpUseDtlConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(ExpUseDtlConstant.JFN_UPDATED_BY, model.getUpdatedBy());
			jsObj.put(ExpUseDtlConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}
	
	public static List<ExpUseDtlModel> convertJsonToList(String json, String masterId) throws Exception {
		List<ExpUseDtlModel> list = new ArrayList<ExpUseDtlModel>();
		
		if (json!=null && !json.equals("")) {
			JSONArray jsonArr = new JSONArray(json);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				ExpUseDtlModel model = new ExpUseDtlModel();
				model.setMasterId(masterId);
				model.setActivity(jsonObj.getString(ExpUseDtlConstant.JFN_ACTIVITY));
				model.setActId(jsonObj.getInt(ExpUseDtlConstant.JFN_ACT_ID));
				model.setActGrpId(jsonObj.getInt(ExpUseDtlConstant.JFN_ACT_GRP_ID));
				model.setActName(jsonObj.getString(ExpUseDtlConstant.JFN_ACT_NAME));
				model.setActGrpName(jsonObj.getString(ExpUseDtlConstant.JFN_ACT_GRP_NAME));
				Object o = jsonObj.get(ExpUseDtlConstant.JFN_ASSET_RULE_ID);
				model.setAssetRuleId(o!=null && !o.equals("null") && !o.equals("") ? jsonObj.getInt(ExpUseDtlConstant.JFN_ASSET_RULE_ID) : null);
				model.setAssetName(jsonObj.getString(ExpUseDtlConstant.JFN_ASSET_NAME));
				model.setCondition1(jsonObj.getString(ExpUseDtlConstant.JFN_CONDITION_1));
				model.setCondition2(jsonObj.getString(ExpUseDtlConstant.JFN_CONDITION_2));
				model.setPosition(jsonObj.getString(ExpUseDtlConstant.JFN_POSITION));
				model.setUom(jsonObj.getString(ExpUseDtlConstant.JFN_UOM));
				model.setAmount(jsonObj.getDouble(ExpUseDtlConstant.JFN_AMOUNT));
				model.setSpecialWorkflow(jsonObj.getString(ExpUseDtlConstant.JFN_SPECIAL_WORKFLOW));
				
				list.add(model);
			}
		}
		
		return list;
	}
	
	public static List<ExpUseDtlModel> convertXmlRpcArrayToList(XmlRpcArray array, String masterId) throws Exception {
		List<ExpUseDtlModel> list = new ArrayList<ExpUseDtlModel>();
		
		if (array!=null) {
			for(Object o : array) {
				XmlRpcStruct s = (XmlRpcStruct)o;
				
				ExpUseDtlModel model = new ExpUseDtlModel();
				model.setMasterId(masterId);
				model.setActivity(s.getString(ExpUseDtlConstant.JFN_ACTIVITY));
				model.setActId(s.getIntegerWrapper(ExpUseDtlConstant.JFN_ACT_ID));
				model.setActGrpId(s.getIntegerWrapper(ExpUseDtlConstant.JFN_ACT_GRP_ID));
				model.setAssetRuleId(s.getIntegerWrapper(ExpUseDtlConstant.JFN_ASSET_RULE_ID));
				model.setCondition1(s.getString(ExpUseDtlConstant.JFN_CONDITION_1));
				model.setCondition2(s.getString(ExpUseDtlConstant.JFN_CONDITION_2));
				model.setPosition(s.getString(ExpUseDtlConstant.JFN_POSITION));
				model.setUom(s.getString(ExpUseDtlConstant.JFN_UOM));
				model.setAmount(s.getDouble(ExpUseDtlConstant.JFN_AMOUNT));
				
				list.add(model);
			}
		}
		
		return list;
	}
	
	public static void addAction(List<ExpUseDtlModel> list, Map<String,Object> model) {
		String action = null;
		if(model!=null && 
				((model.get("is_small_amount")!=null && model.get("is_small_amount").equals(CommonConstant.V_ENABLE))
			 || (model.get("brw_is_small_amount")!=null && model.get("brw_is_small_amount").equals(CommonConstant.V_ENABLE)))
				) {
			action = "E";
		} else {
			action = "CED";
		}
		
		for (ExpUseDtlModel item : list) {
			item.setAction(action);
		}
	}	
}
