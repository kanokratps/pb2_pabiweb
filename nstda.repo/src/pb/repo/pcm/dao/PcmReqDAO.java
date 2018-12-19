package pb.repo.pcm.dao;

import java.util.List;
import java.util.Map;

import pb.repo.pcm.model.PcmReqModel;

public interface PcmReqDAO {

	public void add(PcmReqModel model);
	public void update(PcmReqModel model);
	public void updateStatus(PcmReqModel model);
	public void delete(String id);
	
	public Long count();
	
	public PcmReqModel get(Map<String, Object> params);
	public PcmReqModel getForWfPath(Map<String, Object> params);

	public List<Map<String, Object>> list(Map<String, Object> params);
	public List<Map<String, Object>> listForSearch(Map<String, Object> params);
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
	public List<Map<String, Object>> listOld(Map<String, Object> params);
	
	public List<Map<String, Object>> listExAv(Map<String, Object> params);
	
	public String genNewId(Map<String, Object> params);
	public String getLastId(Map<String, Object> params);
	
	public Long getNewRunningNo();
	public Long resetRunningNo();
}
