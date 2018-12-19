package pb.repo.exp.dao;

import java.util.List;
import java.util.Map;

import pb.repo.exp.model.ExpUseAttendeeModel;

public interface ExpUseAttendeeDAO {

	public void add(ExpUseAttendeeModel model);

	public void delete(Long id);
	public void deleteByMasterId(String masterId);
	
	public Long count();
	
	public ExpUseAttendeeModel get(Long id);

	public List<Map<String, Object>> list(Map<String, Object> params);
	public List<ExpUseAttendeeModel> listByMasterId(Map<String, Object> params);
	
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
}