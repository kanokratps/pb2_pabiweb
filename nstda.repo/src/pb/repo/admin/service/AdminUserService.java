package pb.repo.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainUserDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminUserService {
	
	private static Logger log = Logger.getLogger(AdminUserService.class);

	@Autowired
	DataSource dataSource;
	
	public List<Map<String, Object>> list(String searchTerm, String lang) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainUserDAO dao = session.getMapper(MainUserDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");
        	
        		params.put("terms", terms);
        	}
        	lang = lang!=null && lang.startsWith("th") ? "_th" : "";
        	params.put("lang", lang);
        	params.put("orderBy", "org_name"+lang+", section_name"+lang+", emp_id, first_name"+lang+", last_name"+lang+", pos_name"+lang);
        	
            List<Map<String, Object>> tmpList = dao.list(params);
            
            for(Map<String,Object> tmpMap : tmpList) {
            	Map<String, Object> map = new HashMap<String, Object>();
            	
            	map.put("id", tmpMap.get("id"));
            	map.put("emp_id", tmpMap.get("emp_id"));
            	map.put("title", tmpMap.get("title"+lang));
            	map.put("first_name", tmpMap.get("first_name"+lang));
            	map.put("last_name", tmpMap.get("last_name"+lang));
            	map.put("pos_name", tmpMap.get("pos_name"+lang));
            	map.put("org_name", tmpMap.get("org_name_short"+lang));
            	map.put("section_name", tmpMap.get("section_name"+lang));
            	map.put("work_phone", tmpMap.get("work_phone"));
            	map.put("mobile_phone", tmpMap.get("mobile_phone"));
            	map.put("section_code", tmpMap.get("scode"));
            	map.put("section_id", tmpMap.get("section_id"));
            	map.put("org_id", tmpMap.get("org_id"));
            	map.put("section_desc", "["+((String)tmpMap.get("scode")).trim()+"] "+tmpMap.get("section_name"+lang));
            	map.put("org_desc", "["+((String)tmpMap.get("ocode")).trim()+"] "+tmpMap.get("org_name"+lang));

            	list.add(map);
            }
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public Map<String, Object> getByLogin(String login) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainUserDAO dao = session.getMapper(MainUserDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	params.put("login", login);
        	
            map = dao.getByLogin(params);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
}
