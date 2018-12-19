package pb.repo.pcm.dao;

import java.util.List;
import java.util.Map;

import pb.repo.pcm.model.PcmReqMethodModel;

public interface PcmReqMethodDAO {

	public List<Map<String, Object>> list(Map<String, Object> params);
	
	public Long count();
	
	public PcmReqMethodModel get(Long id);

}
