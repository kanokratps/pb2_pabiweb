package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainWkfCmdBossLevelApprovalModel;

public interface MainWkfCmdBossLevelApprovalDAO {

	public List<MainWkfCmdBossLevelApprovalModel> list();
	
	public Long count();
	
	public List<Map<String, Object>> listBoss(Map<String, Object> params);
	
}
