package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainWorkflowHistoryModel;

public interface MainWorkflowHistoryDAO {

	public void add(MainWorkflowHistoryModel model);
	public void update(MainWorkflowHistoryModel model);
	public List<MainWorkflowHistoryModel> listHistory(Map<String,Object> params);
	public void deleteByMasterId(String id); // id

	public List<MainWorkflowHistoryModel> listByMasterId(Map<String, Object> params);
	
	public MainWorkflowHistoryModel getLastByMasterId(Map<String, Object> params);
	
	public MainWorkflowHistoryModel getLastInf(Map<String, Object> params);
}
