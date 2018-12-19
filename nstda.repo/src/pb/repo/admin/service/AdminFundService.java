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

import pb.repo.admin.constant.MainBudgetSrcConstant;
import pb.repo.admin.dao.MainFundDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminFundService {
	
	private static Logger log = Logger.getLogger(AdminFundService.class);

	@Autowired
	DataSource dataSource;

	public List<Map<String, Object>> list(String searchTerm,String lang) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainFundDAO dao = session.getMapper(MainFundDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	if (searchTerm!=null) {
        		String[] terms = searchTerm.split(" ");
        	
        		params.put("terms", terms);
        	}
        	
        	lang = (lang!=null && lang.startsWith("th") ? "_th" : "");
    		String name = "name"+lang;
        	params.put("orderBy", name);
        	
    		List<Map<String,Object>> tmpList = dao.list(params);
    		
    		
        	for(Map<String, Object> tmpMap : tmpList) {
        		Map<String, Object> map = new HashMap<String, Object>();
        		
        		map.put("id", tmpMap.get("id"));
        		map.put("name", tmpMap.get(name));
        		
        		list.add(map);
        	}
            
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return list;
	}
	
	public List<Map<String, Object>> list(String type, String id, String lang) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainFundDAO dao = session.getMapper(MainFundDAO.class);
            
        	Map<String, Object> params = new HashMap<String, Object>();
        	
        	params.put("id", id!=null && !id.equals("") ? Integer.parseInt(id) : null);
        	
        	lang = (lang!=null && lang.startsWith("th") ? "_th" : "");
    		String name = "name"+lang;
        	params.put("orderBy", name);
        	
    		List<Map<String,Object>> tmpList = null; 
    				
    		if (type.equals(MainBudgetSrcConstant.TYPE_UNIT)) {
    			tmpList = dao.listForSection(params);
    		} else 
    		if (type.equals(MainBudgetSrcConstant.TYPE_PROJECT)) {
    			tmpList = dao.listForProject(params);
    		} else 
    		if (type.equals(MainBudgetSrcConstant.TYPE_CONSTRUCTION)) {
    			tmpList = dao.listForConstruction(params);
    		} else 
    		if (type.equals(MainBudgetSrcConstant.TYPE_ASSET)) {
    			tmpList = dao.listForAsset(params);
    		}
    		
    		if (tmpList!=null) {
	        	for(Map<String, Object> tmpMap : tmpList) {
	        		Map<String, Object> map = new HashMap<String, Object>();
	        		
	        		map.put("id", tmpMap.get("id"));
	        		map.put("name", tmpMap.get(name));
	        		
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
	
	public Map<String, Object> get(Integer id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainFundDAO dao = session.getMapper(MainFundDAO.class);
            
            map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
		
		return map;
	}
	
}
