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

import pb.repo.admin.dao.MainPrototypeDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminPrototypeService {
	
	private static Logger log = Logger.getLogger(AdminPrototypeService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(String type, String projectId, String lang) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainPrototypeDAO dao = session.getMapper(MainPrototypeDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
			params.put("type", type);
			params.put("projectId", Integer.parseInt(projectId));
        	
        	lang = lang!=null && lang.startsWith("th") ? "_th" : "";
        	params.put("orderBy", "org_name_short"+lang+", name"+lang+", pm_name"+lang);
        	params.put("lang", lang);
        	
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
        	MainPrototypeDAO dao = session.getMapper(MainPrototypeDAO.class);
            
            map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
}
