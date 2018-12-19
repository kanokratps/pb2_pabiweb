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
import pb.repo.admin.dao.MainUtilDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminUtilService {
	
	private static Logger log = Logger.getLogger(AdminUtilService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public List<Map<String, Object>> listOldPr(Map<String, Object> params) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainUtilDAO dao = session.getMapper(MainUtilDAO.class);
            
    		list = dao.listOldPr(params);
    		
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return list;
	}
	
}
