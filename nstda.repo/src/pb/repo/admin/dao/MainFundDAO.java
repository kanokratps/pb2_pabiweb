package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainFundDAO {

	public List<Map<String, Object>> list(Map<String, Object> params);

	public List<Map<String, Object>> listForSection(Map<String, Object> params);
	public List<Map<String, Object>> listForProject(Map<String, Object> params);
	public List<Map<String, Object>> listForConstruction(Map<String, Object> params);
	public List<Map<String, Object>> listForAsset(Map<String, Object> params);
	
	public Map<String, Object> get(Integer id);
	
	public Long count();
	
}
