package pb.repo.admin.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pb.common.constant.CommonConstant;
import pb.repo.admin.constant.MainUserGroupConstant;
import pb.repo.admin.model.MainUserGroupModel;

public class MainUserGroupUtil {
	
	public static String jsonSuccess(List<MainUserGroupModel> list) throws Exception {
		
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("success",  true);
		jsonObj.put("total",  list.size());
		jsonObj.put("data", convertToJSONArray(list));
		
		return jsonObj.toString();
	}
	
	public static List<MainUserGroupModel> convertJsonToUserGroupList(String json) throws Exception {
		List<MainUserGroupModel> list = new ArrayList<MainUserGroupModel>();
		
		JSONArray jsonArr = new JSONArray(json);
		for(int i=0; i<jsonArr.length(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			
			MainUserGroupModel model = new MainUserGroupModel();
			model.setId(jsonObj.getString(MainUserGroupConstant.JFN_ID));
			model.setType(jsonObj.getString(MainUserGroupConstant.JFN_TYPE));
			
			list.add(model);
		}
		
		return list;
	}
	
	public static JSONArray convertToJSONArray(List<MainUserGroupModel> inList) throws Exception {
		JSONArray jsArr = new JSONArray();

		JSONObject jsObj;
		for(MainUserGroupModel model : inList) {
			jsObj = new JSONObject();
			
			jsObj.put(MainUserGroupConstant.JFN_ID, model.getId());
			jsObj.put(MainUserGroupConstant.JFN_TYPE, model.getType());
			jsObj.put(MainUserGroupConstant.JFN_NAME, model.getName());
			jsObj.put(MainUserGroupConstant.JFN_ACTION, "A");
			
			jsArr.put(jsObj);
		}
		
		return jsArr;
	}
	
	/*
	 * convert String '1' to '000001'
	 */
	public static String code2login(String code) {
		if (code!=null && !code.equals(CommonConstant.DUMMY_EMPLOYEE_CODE)) {
			DecimalFormat dformat = new DecimalFormat(MainUserGroupConstant.LOGIN_FORMAT);
			return dformat.format(Integer.parseInt(code));
		}
		
		return code;
	}
	
	/*
	 * convert String '1,2' to '000001,000002'
	 */
	public static String codes2logins(String codes) {
		String[] cs = codes.split(",");
		StringBuffer logins = new StringBuffer();
		
		for(int i = 0; i < cs.length; i++) {
			if (logins.length()>0) {
				logins.append(",");
			}
			logins.append(code2login(cs[i]));
		}
		
		return logins.toString();
	}
	
	/*
	 * convert String '000001' to '1'
	 */
	public static String login2code(String login) {
		return String.valueOf(Integer.parseInt(login));
	}
	
	/*
	 * convert String '000001,000002' to '1,2'
	 */
	public static String logins2codes(String logins) {
		String[] ls = logins.split(",");
		StringBuffer codes = new StringBuffer();
		
		for(int i = 0; i < ls.length; i++) {
			if (codes.length()>0) {
				codes.append(",");
			}
			codes.append(login2code(ls[i]));
		}
		
		return codes.toString();
	}
	
}
