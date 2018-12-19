package pb.repo.admin.dao;

import java.util.List;
import java.util.Map;

public interface MainBossEmotionDAO {

	public Long count();
	
	public List<Map<String, Object>> list(Map<String, Object> params);
	
}
