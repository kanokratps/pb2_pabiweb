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
import pb.repo.admin.constant.MainAssetRuleConstant;
import pb.repo.admin.dao.MainAssetRuleDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminAssetRuleService {
	
	private static Logger log = Logger.getLogger(AdminAssetRuleService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> list(Map<String, Object> params) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAssetRuleDAO dao = session.getMapper(MainAssetRuleDAO.class);
            
    		List<Map<String, Object>> tmpList = dao.list(params);
    		
//    		String name = MainAssetRuleConstant.TFN_NAME+params.get("lang");
    		String name = MainAssetRuleConstant.TFN_ASSET_NAME;
    		
    		for(Map<String,Object> tmpMap : tmpList) {
    			Map<String, Object> map = new HashMap<String, Object>();
	    		map.put(JsonConstant.COMBOBOX_ID, (Integer)tmpMap.get(MainAssetRuleConstant.TFN_ID));
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
	
	public Map<String, Object> get(Integer id) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainAssetRuleDAO dao = session.getMapper(MainAssetRuleDAO.class);
            
    		map = dao.get(id);
            
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
	
}
