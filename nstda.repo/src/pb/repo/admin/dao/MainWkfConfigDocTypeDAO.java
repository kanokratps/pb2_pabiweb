package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainWkfConfigDocTypeModel;

public interface MainWkfConfigDocTypeDAO {

	public List<MainWkfConfigDocTypeModel> list(Map<String, Object> params);
	
	public Long count();
	
	public Map<String, Object> getByName(String name);
	
}
