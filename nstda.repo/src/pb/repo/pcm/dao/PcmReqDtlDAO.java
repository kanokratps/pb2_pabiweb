package pb.repo.pcm.dao;

import java.util.List;
import java.util.Map;

import pb.repo.pcm.model.PcmReqDtlModel;

public interface PcmReqDtlDAO {

	public void add(PcmReqDtlModel model);

	public void delete(Long id);
	public void deleteByMasterId(String masterId);
	
	public Long count();
	
	public PcmReqDtlModel get(Long id);

	public List<PcmReqDtlModel> list(Map<String, Object> params);
	
	public List<Map<String, Object>> listForInf(Map<String, Object> params);
}