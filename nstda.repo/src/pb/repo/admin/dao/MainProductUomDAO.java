package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainProductUomModel;

public interface MainProductUomDAO {

	public List<MainProductUomModel> list();
	
	public Long count();
	
	public Map<String, Object> get(Integer id);
	
}
