package pb.repo.pcm.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import pb.repo.pcm.constant.PcmReqCmtDtlConstant;
import pb.repo.pcm.constant.PcmReqCmtHdrConstant;
import pb.repo.pcm.model.PcmReqCmtDtlModel;
import pb.repo.pcm.model.PcmReqCmtHdrModel;

public class PcmReqCmtHdrUtil {
	
	private static Logger log = Logger.getLogger(PcmReqCmtHdrUtil.class);
	
//	public static String jsonSuccess(List<PcmReqCmtHdrModel> list) {
//		
//		List<Map<String, Object>> cmbList = new ArrayList<Map<String, Object>>();
//		
//		for(PcmReqCmtHdrModel model : list) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			
//			map.put(JsonConstant.COMBOBOX_ID, model.getId());
//			map.put(JsonConstant.COMBOBOX_NAME, model.getObjType() + " - " + model.getMethod() + " - " + model.getCond1());
//			map.put(JsonConstant.COMBOBOX_DATA, model);
//			
//			cmbList.add(map);
//		}
//		
//		return CommonUtil.jsonSuccess(cmbList);
//	}
	
	public static List<PcmReqCmtHdrModel> convertJsonToList(String json, String masterId) throws Exception {
		List<PcmReqCmtHdrModel> list = new ArrayList<PcmReqCmtHdrModel>();
		
		if (json!=null && !json.equals("")) {
			JSONArray jsonArr = new JSONArray(json);
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				
				PcmReqCmtHdrModel model = new PcmReqCmtHdrModel();
				model.setPcmReqId(masterId);
				model.setCommittee(jsonObj.getString(PcmReqCmtHdrConstant.JFN_COMMITTEE));
				model.setCommitteeId(jsonObj.getInt(PcmReqCmtHdrConstant.JFN_COMMITTEE_ID));
				
				list.add(model);

				
				JSONArray dtlArr = jsonObj.getJSONArray(PcmReqCmtHdrConstant.JFN_COMMITTEES);
				for(int j=0; j<dtlArr.length(); j++) {
					JSONObject dtlObj = dtlArr.getJSONObject(j);
					
					PcmReqCmtDtlModel dtlModel = new PcmReqCmtDtlModel();
					dtlModel.setEmployeeCode(dtlObj.getString(PcmReqCmtDtlConstant.JFN_EMPLOYEE_CODE));
					dtlModel.setTitle("0");

					String tid = null;
					try {
						tid = dtlObj.getString(PcmReqCmtDtlConstant.JFN_TID);
					} catch (Exception ex) {
						
					}
					if (tid!=null && !tid.equals("null") && !tid.equals("")) {
						dtlModel.setTitle(dtlObj.getString(PcmReqCmtDtlConstant.JFN_TID));
					} else {
						dtlModel.setTitle("0");
//						dtlModel.setTitle(dtlObj.getString(PcmReqCmtDtlConstant.JFN_TITLE)!=null && !dtlObj.getString(PcmReqCmtDtlConstant.JFN_TITLE).equals("null") ? dtlObj.getString(PcmReqCmtDtlConstant.JFN_TITLE) : "");
					}
					
					dtlModel.setFirstName(dtlObj.getString(PcmReqCmtDtlConstant.JFN_FIRST_NAME));
					dtlModel.setLastName(dtlObj.getString(PcmReqCmtDtlConstant.JFN_LAST_NAME));
					dtlModel.setPosition(dtlObj.getString(PcmReqCmtDtlConstant.JFN_POSITION));
					
					model.getDtlList().add(dtlModel);
				}

			}
		}
		
		return list;
	}
	
	public static JSONArray convertToJSONArray(List<PcmReqCmtHdrModel> inList) throws Exception {
		
		List<JSONObject> outList = new ArrayList<JSONObject>();
		
		JSONObject jsObj = null;
		for(PcmReqCmtHdrModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(PcmReqCmtHdrConstant.JFN_ID, model.getId());
			jsObj.put(PcmReqCmtHdrConstant.JFN_PCM_REQ_ID, model.getPcmReqId());
			jsObj.put(PcmReqCmtHdrConstant.JFN_COMMITTEE, model.getCommittee());
			jsObj.put(PcmReqCmtHdrConstant.JFN_COMMITTEE_ID, model.getCommitteeId());
			
			jsObj.put(PcmReqCmtHdrConstant.JFN_COMMITTEES, PcmReqCmtDtlUtil.convertToJSONArray(model.getDtlList()));
			
			jsObj.put(PcmReqCmtHdrConstant.JFN_CREATED_TIME, model.getCreatedTime());
			jsObj.put(PcmReqCmtHdrConstant.JFN_CREATED_BY, model.getCreatedBy());
			jsObj.put(PcmReqCmtHdrConstant.JFN_UPDATED_TIME, model.getUpdatedTime());
			jsObj.put(PcmReqCmtHdrConstant.JFN_UPDATED_BY, model.getUpdatedBy());		
			
			jsObj.put(PcmReqCmtHdrConstant.JFN_ACTION, "ED");

			outList.add(jsObj);
		}
		
		return new JSONArray(outList);
	}	
	
}
