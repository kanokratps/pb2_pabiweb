package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainAssetRuleDAO {

	public List<Map<String, Object>> list(Map<String,Object> params);
	
//	public List<Map<String, Object>> listIcharge(Map<String,Object> params);
	
	public Map<String, Object> get(Integer id);

}
