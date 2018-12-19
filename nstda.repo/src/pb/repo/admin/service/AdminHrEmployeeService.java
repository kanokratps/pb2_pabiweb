package pb.repo.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainHrEmployeeDAO;
import pb.repo.admin.model.MainHrEmployeeModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminHrEmployeeService {
	
	private static Logger log = Logger.getLogger(AdminHrEmployeeService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public MainHrEmployeeModel get(String id) {
			
		MainHrEmployeeModel model = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
    		model = dao.get(id);
    		if (model!=null) {
    			model.setTotalRowCount(1l);
    		}
            
        } finally {
        	session.close();
        }
        
        return model;
	}
	
	public Map<String, Object> getWithDtl(String id, String mode) {
		
		Map<String, Object> map = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
        	
        	Map<String, Object> params = new HashMap<String, Object>();

        	params.put("code", id);
        	if (mode!=null && !mode.equals("v")) {
        		params.put("active", true);
        	}
        	
    		map = dao.getWithDtl(params);
    		
    		if(map!=null) {
    			map.put("title", map.get("title")!=null && !map.get("title").equals("null") ? map.get("title") : "");
    			map.put("title_th", map.get("title_th")!=null && !map.get("title_th").equals("null") ? map.get("title_th") : "");
    		}
            
        } finally {
        	session.close();
        }
        
        return map;
	}
	
	public List<Map<String, Object>> listInSet(List<String> codes) {
		
		List<Map<String, Object>> list = null;
		
		SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
        	
        	Map<String, Object> params = new HashMap<String, Object>();

        	if (codes!=null && codes.size()>0) {
        		params.put("codes", codes);
        	}
        	
    		list = dao.listInSet(params);
            
        } finally {
        	session.close();
        }
        
        return list;
	}

	public List<Map<String, Object>> list(String type, String code, String searchTerm) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

        	params.put("code", Integer.parseInt(code));
        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");
        	
        		params.put("terms", terms);
        	}
        	
        	if (type.equals("P")) {
                List<Map<String,Object>>tmpList = dao.listByProject(params);
                
            	for(Map<String, Object> tmpMap : tmpList) {
            		Map<String, Object> map = new HashMap<String, Object>();
            		
            		String name = tmpMap.get("first_name")+" "+tmpMap.get("last_name");
            		map.put("id", tmpMap.get("employee_code")+"|"+name);
            		map.put("code", tmpMap.get("employee_code"));
            		map.put("name", name);
            		map.put("utype", tmpMap.get("utype"));
            		map.put("position", tmpMap.get("position"));
            		
            		list.add(map);
            	}                
        	
        	} else {
                List<Map<String,Object>>tmpList = dao.listBySection(params);
            	for(Map<String, Object> tmpMap : tmpList) {
            		Map<String, Object> map = new HashMap<String, Object>();
            		
            		String name = tmpMap.get("first_name")+" "+tmpMap.get("last_name");
            		map.put("id", tmpMap.get("employee_code")+"|"+name);
            		map.put("code", tmpMap.get("employee_code"));
            		map.put("name", name);
            		map.put("utype", tmpMap.get("utype"));
            		map.put("position", tmpMap.get("position"));
            		
            		list.add(map);
            	}                
        	}
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public List<Map<String, Object>> list(String searchTerm, String lang) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");
        	
        		params.put("terms", terms);
        	}
        	
        	lang = lang!=null && lang.startsWith("th") ? "_th" : "";
        	params.put("lang", lang);
        	params.put("active", true);
        	
        	log.info("params="+params);
        	
            List<Map<String,Object>>tmpList = dao.listForSearch(params);
            
        	for(Map<String, Object> tmpMap : tmpList) {
        		Map<String, Object> map = new HashMap<String, Object>();
        		
        		String name = tmpMap.get("title"+lang)+" "+tmpMap.get("first_name"+lang)+" "+tmpMap.get("last_name"+lang);
        		map.put("id", tmpMap.get("employee_code")+"|"+name);
        		map.put("code", tmpMap.get("employee_code"));
        		map.put("title", tmpMap.get("title"+lang));
        		map.put("fname", tmpMap.get("first_name"+lang));
        		map.put("lname", tmpMap.get("last_name"+lang));
        		map.put("utype", tmpMap.get("utype"+lang));
        		map.put("org", tmpMap.get("org_name_short"+lang));
        		map.put("org_name", tmpMap.get("org_name"+lang));
        		map.put("div_name", tmpMap.get("division"+lang));
        		map.put("position", tmpMap.get("position"+lang));
        		map.put("position_id", tmpMap.get("position_id"));
        		map.put("wphone", tmpMap.get("work_phone"));
        		map.put("mphone", tmpMap.get("mobile_phone"));
        		
        		list.add(map);
        	}                
    	
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public List<Map<String, Object>> listPcmMember(Integer orgId) {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

        	params.put("orgId", orgId);
        	
        	log.info("params="+params);
        	
            list = dao.listPcmMember(params);
    	
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}	
	
	public List<Map<String, Object>> listExpMember() {
		
		List<Map<String, Object>> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainHrEmployeeDAO dao = session.getMapper(MainHrEmployeeDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();

            list = dao.listExpMember(params);
    	
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}	
}
