package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainWorkflowAssigneeDAO {

	public void add(Map<String, Object> model);
	public void update(Map<String, Object> model);
	public List<Map<String, Object>> list(Map<String,Object> params);
	public void deleteByWorkflowId(String masterId); // id

	public List<Map<String,Object>> listByWorkflowId(Map<String, Object> params);
	
	public Long count(Map<String, Object> params);
}
