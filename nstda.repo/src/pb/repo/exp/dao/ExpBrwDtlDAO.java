package pb.repo.exp.dao;

import java.util.List;
import java.util.Map;

import pb.repo.exp.model.ExpBrwDtlModel;

public interface ExpBrwDtlDAO {

	public void add(ExpBrwDtlModel model);

	public void delete(Long id);
	public void deleteByMasterId(String masterId);
	
	public Long count();
	
	public ExpBrwDtlModel get(Long id);

	public List<ExpBrwDtlModel> list(Map<String, Object> params);
	
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
}