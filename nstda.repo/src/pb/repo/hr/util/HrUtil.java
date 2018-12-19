package pb.repo.hr.util;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import pb.repo.common.mybatis.DbConnectionFactory;
import pb.repo.hr.dao.HrSalDAO;
import pb.repo.hr.model.HrSalModel;

public class HrUtil {
	
	private static Logger log = Logger.getLogger(HrUtil.class);
	
	public static SqlSession openSession(DataSource dataSource) {
		
		SqlSessionFactory sqlSessionFactory = DbConnectionFactory.getSqlSessionFactory(dataSource);

		Configuration config = sqlSessionFactory.getConfiguration();
		if (!config.hasMapper(HrSalDAO.class)) {
	        config.getTypeAliasRegistry().registerAlias("hrSalModel", HrSalModel.class);
	        
	        config.addMapper(HrSalDAO.class);
		}		
		
        return sqlSessionFactory.openSession();
	}
	
}
