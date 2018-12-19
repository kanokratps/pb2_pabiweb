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
import pb.repo.admin.constant.MainAccountPettyCashConstant;
import pb.repo.admin.dao.MainAccountPettyCashDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminAccountPettyCashService {
	
	private static Logger log = Logger.getLogger(AdminAccountPettyCashService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(String lang) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAccountPettyCashDAO dao = session.getMapper(MainAccountPettyCashDAO.class);
            
   		Map<String, Object> params = new HashMap<String, Object>();
    		
    		lang = lang!=null && lang.startsWith("th") ? "_th" : "";
			params.put("lang", lang);
			
    		List<Map<String, Object>> tmpList = dao.list(params);
    		
    		String nameLang = MainAccountPettyCashConstant.TFN_NAME.toLowerCase()+lang; 
    		
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, (Integer)tmpMap.get(MainAccountPettyCashConstant.TFN_ID));
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(nameLang));
	    		
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
	
	public Map<String, Object> get(Integer id) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAccountPettyCashDAO dao = session.getMapper(MainAccountPettyCashDAO.class);
            
    		map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
	
}
