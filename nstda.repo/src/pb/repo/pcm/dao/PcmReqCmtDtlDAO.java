package pb.repo.pcm.dao;

import java.util.List;
import java.util.Map;

import pb.repo.pcm.model.PcmReqCmtDtlModel;

public interface PcmReqCmtDtlDAO {
	
	public void add(PcmReqCmtDtlModel model);

	public Long count();
	
	public PcmReqCmtDtlModel get(String id);

	public List<Map<String,Object>> list(Map<String, Object> params);
	public List<PcmReqCmtDtlModel> listByMasterId(Map<String, Object> params);
	
	public void deleteByMasterId(String masterId);
	
}
