package pb.repo.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainPurchaseConditionDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminPurchaseConditionService {
	
	private static Logger log = Logger.getLogger(AdminPurchaseConditionService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	public List<Map<String, Object>> list(Integer id) {
		
		List<Map<String,Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainPurchaseConditionDAO dao = session.getMapper(MainPurchaseConditionDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("id", id);
        	
            list = dao.list(params);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
}
