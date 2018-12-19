package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainHrExpenseRuleModel;

public interface MainHrExpenseRuleDAO {

	public List<Map<String, Object>> list(Map<String, Object> params);
	public List<Map<String, Object>> listDistinct(Map<String, Object> params);
	
	public Long count();
	
	public MainHrExpenseRuleModel get(String code);

}
