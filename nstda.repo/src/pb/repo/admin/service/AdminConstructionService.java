package pb.repo.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainConstructionDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminConstructionService {

	private static Logger log = Logger.getLogger(AdminConstructionService.class);

	@Autowired
	DataSource dataSource;

	public List<Map<String, Object>> list(String searchTerm, String lang) {

		List<Map<String, Object>> list = null;

        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainConstructionDAO dao = session.getMapper(MainConstructionDAO.class);

        	Map<String, Object> params = new HashMap<String, Object>();

        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");

        		params.put("terms", terms);
        	}
        	lang = lang!=null && lang.startsWith("th") ? "_th" : "";
        	params.put("orderBy", "org"+lang+", construction"+lang+", costcenter");
        	
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
        	MainConstructionDAO dao = session.getMapper(MainConstructionDAO.class);
            
            map = dao.getFromView(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
}
