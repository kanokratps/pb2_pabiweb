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

import pb.repo.admin.dao.MainCompleteNotificationDAO;
import pb.repo.admin.model.MainCompleteNotificationModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminCompleteNotificationService {
	
	private static Logger log = Logger.getLogger(AdminCompleteNotificationService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public void save(MainCompleteNotificationModel model) {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainCompleteNotificationDAO dao = session.getMapper(MainCompleteNotificationDAO.class);
            
    		model.setCreatedBy(authService.getCurrentUserName());
        	
        	dao.add(model);
            
            session.commit();
        } catch (Exception ex) {
        	log.error(ex);
        	session.rollback();
        } finally {
        	session.close();
        }

	}
	
	public List<MainCompleteNotificationModel> list() {
		
		List<MainCompleteNotificationModel> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainCompleteNotificationDAO dao = session.getMapper(MainCompleteNotificationDAO.class);
            
    		Map<String, Object> map = new HashMap<String, Object>();

    		list = dao.list(map);
            
        } catch (Exception ex) {
        	session.close();
        }
        
        return list;
	}
	
	public void delete(Long id) throws Exception {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainCompleteNotificationDAO dao = session.getMapper(MainCompleteNotificationDAO.class);

    		dao.delete(id);
            
            session.commit();
        } catch (Exception ex) {
        	session.rollback();
        } finally {
        	session.close();
        }
	}
	
	public MainCompleteNotificationModel get(Long id) {
		
		MainCompleteNotificationModel model = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainCompleteNotificationDAO dao = session.getMapper(MainCompleteNotificationDAO.class);
            
    		model = dao.get(id);
    		model.setTotalRowCount(1l);
            
        } catch (Exception ex) {
        	session.close();
        }
        
        return model;
	}

}
