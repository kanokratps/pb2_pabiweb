package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainPartnerTitleDAO {

	public List<Map<String, Object>> list(Map<String,Object> params);
	
	public Long count();
	
	public Map<String, Object> getByName(String name);
	public Map<String, Object> get(Integer id);

}
