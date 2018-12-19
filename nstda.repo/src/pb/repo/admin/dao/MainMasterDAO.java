package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

import pb.repo.admin.model.MainMasterModel;

public interface MainMasterDAO {

	public void add(MainMasterModel model);
	public void update(MainMasterModel model);
	
	public void delete(Long id);
	
	public Long count();
	
	public MainMasterModel get(Long id);
	public MainMasterModel getByTypeAndCode(Map<String, Object> params);
	public List<MainMasterModel> listByTypeAndCode(Map<String, Object> params);

	public List<MainMasterModel> list(Map<String,Object> params);
	
	public List<Map<String, Object>> listByType(Map<String, Object> map); // Constant.MST_*
	public List<MainMasterModel> listAuthType(Map<String, String> map);
	public List<MainMasterModel> listByAuthType(Map<String, String> map);
	public List<MainMasterModel> listMasterWithOutMatrix(Map<String, String> map);
	
	public List<MainMasterModel> listCmb(Map params);
	public List<MainMasterModel> listColumnSort(Map<String, Object> map); 
	
	public Long reset(String seqName);
}
