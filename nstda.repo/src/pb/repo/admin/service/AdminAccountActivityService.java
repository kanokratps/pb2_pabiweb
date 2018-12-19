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

import pb.common.constant.JsonConstant;
import pb.common.util.CommonUtil;
import pb.repo.admin.constant.MainAccountActivityConstant;
import pb.repo.admin.dao.MainAccountActivityDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminAccountActivityService {
	
	private static Logger log = Logger.getLogger(AdminAccountActivityService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(Map<String, Object> params) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAccountActivityDAO dao = session.getMapper(MainAccountActivityDAO.class);
            
    		List<Map<String, Object>> tmpList = dao.list(params);
    		
    		String name = MainAccountActivityConstant.TFN_NAME+params.get("lang");
    		
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, (Integer)tmpMap.get(MainAccountActivityConstant.TFN_ID));
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(name.toUpperCase()));

	    		tmpMap = CommonUtil.removeThElement(tmpMap);
	    		map.put(JsonConstant.COMBOBOX_DATA, tmpMap);
	    		
	    		list.add(map);
    		}    		
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public List<Map<String, Object>> listIcharge(Map<String, Object> params) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAccountActivityDAO dao = session.getMapper(MainAccountActivityDAO.class);
            
    		List<Map<String, Object>> tmpList = dao.listIcharge(params);
    		
    		String name = MainAccountActivityConstant.TFN_NAME+params.get("lang");
    		
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, (Integer)tmpMap.get(MainAccountActivityConstant.TFN_ID));
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(name.toUpperCase()));

	    		tmpMap = CommonUtil.removeThElement(tmpMap);
	    		map.put(JsonConstant.COMBOBOX_DATA, tmpMap);
	    		
	    		list.add(map);
    		}    		
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return list;
	}
	
	public Map<String, Object> get(Integer id, Integer agId) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAccountActivityDAO dao = session.getMapper(MainAccountActivityDAO.class);
            
            Map<String, Object> params = new HashMap<String, Object>();
            
            params.put("id", id);
            params.put("agId", agId);
            
    		map = dao.get(params);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
	
}
