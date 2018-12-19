package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainConstructionDAO {

	public List<Map<String, Object>> list(Map<String, Object> params);
//	public List<Map<String, Object>> listWarehouse(Map<String, Object> params);
	
	public Map<String, Object> getFromView(Integer id);
	public Map<String, Object> get(Integer id);
	
	public Long count();
	
}
