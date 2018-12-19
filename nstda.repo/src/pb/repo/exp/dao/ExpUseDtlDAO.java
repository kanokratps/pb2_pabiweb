package pb.repo.exp.dao;

import java.util.List;
import java.util.Map;

import pb.repo.exp.model.ExpUseDtlModel;

public interface ExpUseDtlDAO {

	public void add(ExpUseDtlModel model);

	public void delete(Long id);
	public void deleteByMasterId(String masterId);
	
	public Long count();
	
	public ExpUseDtlModel get(Long id);

	public List<ExpUseDtlModel> list(Map<String, Object> params);
	
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
}