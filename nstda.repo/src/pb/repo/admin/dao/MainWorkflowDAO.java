package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainWorkflowModel;

public interface MainWorkflowDAO {

	public void add(MainWorkflowModel model);
	public void update(MainWorkflowModel model);
	
	public void deleteByMasterId(String masterId);
	
	public MainWorkflowModel getLastWorkflow(MainWorkflowModel model);
	public Long getKey();
	
    public List<MainWorkflowModel> listByMasterId(String id);
	
	public List<MainWorkflowModel> list(Map<String, Object> params);
	
	public Map<String, Object> getFirstApprover(Map<String, Object> params);
	public Map<String, Object> getLastApprover(Map<String, Object> params);
}
