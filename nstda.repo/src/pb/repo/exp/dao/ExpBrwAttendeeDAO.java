package pb.repo.exp.dao;

import java.util.List;
import java.util.Map;

import pb.repo.exp.model.ExpBrwAttendeeModel;

public interface ExpBrwAttendeeDAO {

	public void add(ExpBrwAttendeeModel model);

	public void delete(Long id);
	public void deleteByMasterId(String masterId);
	
	public Long count();
	
	public ExpBrwAttendeeModel get(Long id);

	public List<Map<String, Object>> list(Map<String, Object> params);
	public List<ExpBrwAttendeeModel> listByMasterId(Map<String, Object> params);
	
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
}