package pb.repo.hr.dao;

import java.util.List;
import java.util.Map;

import pb.repo.hr.model.HrSalModel;

public interface HrSalDAO {

	public void add(HrSalModel model);
	public void update(HrSalModel model);
	public void updateStatus(HrSalModel model);
	public void delete(String id);
	
	public Long count();
	
	public HrSalModel get(String id);
	public HrSalModel getForWfPath(String id);

	public List<Map<String,Object>> list(Map<String, Object> params);
	public List<Map<String, Object>> listWorkflowPath(String id);
	
}
