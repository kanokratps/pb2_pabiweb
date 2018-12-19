package pb.repo.admin.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pb.repo.admin.dao.MainModuleDAO;
import pb.repo.common.mybatis.DbConnectionFactory;

@Service
public class AdminModuleService {
	
	private static Logger log = Logger.getLogger(AdminModuleService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthenticationService authService;
	
	@Autowired
	@Qualifier("mainInterfaceService")
	InterfaceService interfaceService;

	public Map<String, Object> getTotalPreBudget(String budgetCcType, Integer budgetCc, Integer fundId, String prId, String exId, Boolean isInternalCharge) {
		
		Map<String, Object> map = null;
		
        SqlSession session = DbConnectionFactory.getSqlSessionFactory(dataSource).openSession();
        try {
            MainModuleDAO dao = session.getMapper(MainModuleDAO.class);
            
            Map<String, Object> params = new HashMap<String, Object>();
            
            params.put("budgetCc", budgetCc);
            params.put("budgetCcType", budgetCcType);
            params.put("fundId", fundId);
            params.put("prId", prId);
            params.put("exId", exId);
            
    		map = dao.getTotalPreBudget(params);
    		
    		if (map==null) {
    			map = new HashMap<String, Object>();
    			map.put("pre", 0.0);
    		}
    		DecimalFormat df = new DecimalFormat("#,##0.00");
    		
    		Map<String, Object> budget = interfaceService.getBudget(budgetCcType, budgetCc, fundId, isInternalCharge);
    		Double balance = 0.0;
    		if (budget!=null) {
    			balance = (Double)budget.get("balance");
    			map.put("checkBudget", budget.get("checkBudget"));
    		} else {
    			map.put("checkBudget", true);
    		}
    		
    		map.put("ebalance", df.format(balance-(Double)map.get("pre")));
    		map.put("balance", df.format(balance));
    		map.put("pre", df.format(map.get("pre")));
    		
        } catch (Exception ex) {
        	log.error(ex);
        } finally {
        	session.close();
        }
        
        return map;
	}
	
	public JSONObject validateAssetPrice(Integer assetRuleId, Double amount) {
		
		JSONObject map = null;
        try {
    		map = interfaceService.validateAssetPrice(assetRuleId, amount);
        } catch (Exception ex) {
        	log.error(ex);
        }
        
        return map;
	}
	
	public JSONObject _checkFundSpending(List<Map<String, Object>> items) {
		
		JSONObject map = null;
        try {
    		map = interfaceService.checkFundSpending(items);
        } catch (Exception ex) {
        	log.error(ex);
        }
        
        return map;
	}
	
	public JSONObject checkBudget(String docType, String budgetCcType,Integer budgetCc, Integer fundId, List<Map<String, Object>> items) {
		
		JSONObject map = null;
        try {
    		map = interfaceService.checkBudget(docType,budgetCcType,budgetCc,fundId,items);
        } catch (Exception ex) {
        	log.error(ex);
        }
        
        return map;
	}
}
