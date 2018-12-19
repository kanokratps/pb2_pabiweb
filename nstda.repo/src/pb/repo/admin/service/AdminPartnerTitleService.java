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
import pb.repo.admin.constant.MainPartnerTitleConstant;
import pb.repo.admin.dao.MainAccountActivityDAO;
import pb.repo.admin.dao.MainPartnerTitleDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminPartnerTitleService {
	
	private static Logger log = Logger.getLogger(AdminPartnerTitleService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(Map<String, Object> params) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainPartnerTitleDAO dao = session.getMapper(MainPartnerTitleDAO.class);
            
    		List<Map<String, Object>> tmpList = dao.list(params);
    		
//    		String name = MainPartnerTitleConstant.TFN_NAME+params.get("lang");
    		
			Map<String, Object> tmap = new HashMap<String,Object>();
			tmap.put("id", 0);
			tmap.put("name", "");
			tmap.put("name_th", "");

			int o = 0;
			
			Map<String, Object> blank = new HashMap<String, Object>();

			blank.put(JsonConstant.COMBOBOX_ID, 0);
			blank.put(JsonConstant.COMBOBOX_NAME, "\u00A0");
			blank.put("o", o++);
			blank.put(JsonConstant.COMBOBOX_DATA, tmap);
			
			list.add(blank);

    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, tmpMap.get(MainPartnerTitleConstant.TFN_ID));
//	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get(name.toUpperCase()));
	    		map.put(JsonConstant.COMBOBOX_NAME, (String)tmpMap.get("NAME_TH")+"/"+(String)tmpMap.get("NAME"));
	    		blank.put("o", o++);
//	    		tmpMap = CommonUtil.removeThElement(tmpMap);
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
	
	public Map<String, Object> getByName(String name) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainPartnerTitleDAO dao = session.getMapper(MainPartnerTitleDAO.class);
            
    		map = dao.getByName(name);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
	
	public Map<String, Object> get(Integer id) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
        	MainPartnerTitleDAO dao = session.getMapper(MainPartnerTitleDAO.class);
            
    		map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
}
