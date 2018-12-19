package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface AdminWorkflowDAO {

	public List<Map<String,Object>> list(Map<String, Object> params);
	
	public Map<String, Object> getByDocId(String id);
	
	public void update(Map<String,Object> params);
	public void updateReviewer(Map<String,Object> params);
	
	public Map<String,Object> getForWfPath(Map<String, Object> params);
}
