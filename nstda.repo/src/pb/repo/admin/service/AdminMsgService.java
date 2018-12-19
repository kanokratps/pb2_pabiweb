package pb.repo.admin.service;

import java.util.List;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainMsgDAO;
import pb.repo.admin.model.MainMsgModel;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminMsgService {
	
	private static Logger log = Logger.getLogger(AdminMsgService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;

	public void save(MainMsgModel model) {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainMsgDAO msgDAO = session.getMapper(MainMsgDAO.class);
            
            msgDAO.add(model);
            
            session.commit();
        } catch (Exception ex) {
        	session.rollback();
        } finally {
        	session.close();
        }

	}
	
	public List<MainMsgModel> listByUser(String user) {
		
		List<MainMsgModel> list = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainMsgDAO mainMsgDAO = session.getMapper(MainMsgDAO.class);
            
    		list = mainMsgDAO.listByUser(user);
            
        } finally {
        	session.close();
        }
        
        return list;
	}

	public void delete(String user) throws Exception {
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainMsgDAO mainMsgDAO = session.getMapper(MainMsgDAO.class);

    		mainMsgDAO.delete(user);
            
            session.commit();
        } catch (Exception ex) {
        	session.rollback();
        } finally {
        	session.close();
        }
	}

}
