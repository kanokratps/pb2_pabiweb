package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainCostControlDAO {

	public List<Map<String, Object>> list(Map<String, Object> params);
	
	public Long count();
	
}
