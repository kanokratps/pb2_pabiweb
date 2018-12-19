package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainProjectMemberModel;

public interface MainProjectMemberDAO {

	public List<MainProjectMemberModel> list();
//	public List<Map<String, Object>> listProjectManager(Map<String, Object> params);
	public List<Map<String, Object>> listPMSpecialBudget(Map<String, Object> params);
	
	public Long count();
}
