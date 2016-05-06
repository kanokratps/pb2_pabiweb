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

import pb.repo.admin.dao.MainSectionDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminSectionService {
	
	private static Logger log = Logger.getLogger(AdminSectionService.class);

	@Autowired
	DataSource dataSource;
	
	public List<Map<String, Object>> list(String searchTerm) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainSectionDAO dao = session.getMapper(MainSectionDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	params.put("searchTerm", searchTerm);
        	
            list = dao.list(params);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public Map<String, Object> get(Integer id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainSectionDAO dao = session.getMapper(MainSectionDAO.class);
            
            map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
}
