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

import pb.repo.admin.dao.MainProjectDAO;
import pb.repo.admin.dao.MainProjectMemberDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminProjectService {
	
	private static Logger log = Logger.getLogger(AdminProjectService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(String searchTerm, String lang) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainProjectDAO dao = session.getMapper(MainProjectDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");
        	
        		params.put("terms", terms);
        	}
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
        	MainProjectDAO dao = session.getMapper(MainProjectDAO.class);
            
            map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
//	public List<Map<String, Object>> listProjectManager(Integer projectId) {
//		
//		List<Map<String, Object>> list = null;
//		
//        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
//        try {
//        	MainProjectMemberDAO dao = session.getMapper(MainProjectMemberDAO.class);
//            
//        	Map<String, Object> params = new HashMap<String, Object>();
//        	
//        	params.put("projectId", projectId);
//        	
//            list = dao.listProjectManager(params);
//            
//        } catch (Exception ex) {
//        	log.error(ex);
//        } finally {
//        	session.close();
//        }
//		
//		return list;
//	}
	
	public List<Map<String, Object>> listPMSpecialBudget(Integer projectId, String module) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainProjectMemberDAO dao = session.getMapper(MainProjectMemberDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	params.put("projectId", projectId);
        	params.put("module", module);
        	
            list = dao.listPMSpecialBudget(params);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
}
