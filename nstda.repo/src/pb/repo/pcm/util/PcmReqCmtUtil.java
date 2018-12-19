package pb.repo.pcm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import pb.common.constant.JsonConstant;
import pb.common.util.CommonUtil;

public class PcmReqCmtUtil {
	
	private static Logger log = Logger.getLogger(PcmReqCmtUtil.class);
	
	public static String jsonSuccess(List<Map<String, Object>> list) {
		
		List<Map<String, Object>> cmbList = new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> model : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put(JsonConstant.COMBOBOX_ID, model.get("id"));
			map.put(JsonConstant.COMBOBOX_NAME, model.get("method"));
			map.put(JsonConstant.COMBOBOX_DATA, model);
			
			cmbList.add(map);
		}
		
		return CommonUtil.jsonSuccess(cmbList);
	}
	
}
