package pb.repo.exp.util;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.exp.dao.ExpBrwDAO;
import pb.repo.exp.dao.ExpBrwDtlDAO;
import pb.repo.exp.dao.ExpBrwAttendeeDAO;
import pb.repo.exp.dao.ExpUseDAO;
import pb.repo.exp.dao.ExpUseDtlDAO;
import pb.repo.exp.dao.ExpUseAttendeeDAO;
import pb.repo.exp.model.ExpBrwDtlModel;
import pb.repo.exp.model.ExpBrwAttendeeModel;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.model.ExpUseDtlModel;
import pb.repo.exp.model.ExpUseAttendeeModel;
import pb.repo.exp.model.ExpUseModel;

public class ExpUtil {
	
	private static Logger log = Logger.getLogger(ExpUtil.class);
	
	public static SqlSession openSession(DataSource dataSource) {
		
		SqlSessionFactory sqlSessionFactory = DbConnectionFactory.getSqlSessionFactory(dataSource);

		Configuration config = sqlSessionFactory.getConfiguration();
		if (!config.hasMapper(ExpBrwDAO.class)) {
	        config.getTypeAliasRegistry().registerAlias("expBrwModel", ExpBrwModel.class);
	        config.getTypeAliasRegistry().registerAlias("expBrwAttendeeModel", ExpBrwAttendeeModel.class);
	        config.getTypeAliasRegistry().registerAlias("expBrwDtlModel", ExpBrwDtlModel.class);
	        
	        config.addMapper(ExpBrwDAO.class);
	        config.addMapper(ExpBrwAttendeeDAO.class);
	        config.addMapper(ExpBrwDtlDAO.class);
		}
		
		if (!config.hasMapper(ExpUseDAO.class)) {
	        config.getTypeAliasRegistry().registerAlias("expUseModel", ExpUseModel.class);
	        config.getTypeAliasRegistry().registerAlias("expUseAttendeeModel", ExpUseAttendeeModel.class);
	        config.getTypeAliasRegistry().registerAlias("expUseDtlModel", ExpUseDtlModel.class);
	        
	        config.addMapper(ExpUseDAO.class);
	        config.addMapper(ExpUseAttendeeDAO.class);
	        config.addMapper(ExpUseDtlDAO.class);
		}		
		
        return sqlSessionFactory.openSession();
	}
	
}
