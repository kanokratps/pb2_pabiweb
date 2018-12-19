package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainAccountPettyCashDAO {

	public List<Map<String, Object>> list(Map<String,Object> params);
	
	public Map<String, Object> get(Integer id);

}
