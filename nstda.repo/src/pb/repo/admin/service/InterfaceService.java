package pb.repo.admin.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pb.common.constant.CommonConstant;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.common.util.InterfaceUtil;
import pb.repo.admin.constant.InterfaceConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcStruct;

@Service("mainInterfaceService")
public class InterfaceService {
	
	/*
	 * https://mobileapp.nstda.or.th/redmine/issues/1454   ->  	account.budget.simple_check_budget()
	 */

	private static Logger log = Logger.getLogger(InterfaceService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AdminMasterService masterService;
	
	@Autowired
	AdminSectionService sectionService;
	
	@Autowired
	AdminUserService userService;
	
	@Autowired
	AuthenticationService authService;
	
	private List<Object> getInitArgs(Map<String, Object> cfg) {
		List<Object> args = new ArrayList();
		args.add(cfg.get("db")); // db name
		args.add(cfg.get("usr")); // uid 1='admin'
		args.add(cfg.get("pwd")); // password
		
		return args;
	}

	private Map<String, Object> getConnectionConfig(String password) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		MainMasterModel sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_URL,false);
		String host = sysCfgModel.getFlag1();
		
		sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_DB,false);
		String db = sysCfgModel.getFlag1();
		
		sysCfgModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_ODOO_USER,false);
		String login = sysCfgModel.getFlag1();
		
		Map<String,Object> user = userService.getByLogin(login);
		
		Integer usr = (Integer)user.get("id"); // uid 1='admin'
		String pwd = password;
		
		log.info("host:"+host);
//		log.info("db:"+db);
//		log.info("usr:"+usr);
		
		map.put("host", host);
		map.put("db", db);
		map.put("usr", usr);
		map.put("pwd", pwd);

		return map;
	}
	
	private XmlRpcClient getXmlRpcClient(Map<String, Object> cfg) throws Exception {
		return new XmlRpcClient(cfg.get("host")+CommonConstant.EXT_XMLRPC_URL, false);
	}
	
	public Map<String, Object> getBudgetControlLevel(Timestamp time, String login) throws Exception {
		log.info("sub interface : getBudgetControlLevel");
		
		log.info("  time:"+time.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		Map<String, Object> cfg = getConnectionConfig(odooPassword);
		XmlRpcClient client = getXmlRpcClient(cfg);
		
		List args = getInitArgs(cfg);
		args.add("account.budget"); // Remote Object
		args.add("get_fiscal_and_budget_level");
		
		List a = new ArrayList();
//		Map<String, Object> map = new HashMap<String, Object>();
//		a.add(map);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		a.add(df.format(time));
		
		args.add(a);

		Object obj = client.invoke("execute_kw", args);
		
		
		XmlRpcStruct strc = (XmlRpcStruct)obj;
		
		for(Object k : strc.keySet()) {
			Object v = strc.get(k);

			log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			
			map.put((String)k, v);
		}

		
		return map;
	}	
	
	public Map<String, Object> getBudget(String budgetCcType,Integer budgetCc, Integer fundId, Boolean isInternalCharge) throws Exception {
		log.info("interface : getBudget");
		
		Map<String, Object> map = null;
		
		Boolean success = false;
		Double balance = 0.0;
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		List args = null;
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			args = getInitArgs(cfg);
			args.add("account.budget"); // Remote Object
			args.add("simple_check_budget"); // method
			
			/*
			 * Parameters
			 */
			List a = new ArrayList();
			
    		String budgetType = (String)InterfaceConstant.BUDGET_TYPE.get(budgetCcType);
    		
	        a.add(CommonDateTimeUtil.convertToOdooFieldDate(CommonDateTimeUtil.now()));
	        a.add(budgetType);
	        
	        a.add(false);
	        a.add(budgetCc);
	        a.add(isInternalCharge ? "True" : "False");
//	        a.add(budgetCcType.equals("P") ? fundId : false);
	        
	        /*
	         * Final
	         */
			args.add(a);

			/*
			 * log
			 */
			InterfaceUtil.logInfo(args, log);
			
	        /*
	         * Call
	         */
			Object res = client.invoke("execute_kw", args);
			
			/*
			 * Result
			 */
			log.info("res="+res);
			
			XmlRpcStruct strc = (XmlRpcStruct)res;
			
			for(Object k : strc.keySet()) {
				Object v = strc.get(k);
	
				log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			}
			
			success = (Boolean)strc.get("budget_ok");
			XmlRpcStruct budget = (XmlRpcStruct)strc.get("budget_status");
			if (success) {
				map = new HashMap<String, Object>();
				map.put("balance", budget.get("amount_balance"));
				map.put("checkBudget", strc.get("force_no_budget_check")!=null ? !(Boolean)strc.get("force_no_budget_check") : true);
			}
		}
		catch (Exception ex) {
			log.error(args.toString(),ex);
		}
		
		return map;
	}	
	
	public JSONObject validateAssetPrice(Integer assetRuleId, Double amount) throws Exception {
		log.info("interface : validate_asset_price");
		
		JSONObject map = new JSONObject();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		List args = null;
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			args = getInitArgs(cfg);
			args.add("budget.fund.rule"); // Remote Object
			args.add("validate_asset_price"); // method
			
			/*
			 * Parameters
			 */
			List a = new ArrayList();
			
	        a.add(assetRuleId);
	        a.add(amount);
	        
	        /*
	         * Final
	         */
			args.add(a);

			/*
			 * log
			 */
			InterfaceUtil.logInfo(args, log);
	        
	        /*
	         * Call
	         */
			Object res = client.invoke("execute_kw", args);
			
			/*
			 * Result
			 */
			log.info("res="+res);
			
			XmlRpcStruct strc = (XmlRpcStruct)res;
			
			for(Object k : strc.keySet()) {
				Object v = strc.get(k);
	
				log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			}
			
			map.put("valid", (Boolean)strc.get("budget_ok"));
			map.put("msg", strc.get("message").toString());
		}
		catch (Exception ex) {
			log.error(args.toString(),ex);
		}
		
		return map;
	}
	
	public JSONObject checkFundSpending(List<Map<String, Object>> items) {
		log.info("interface : document_check_fund_spending");
		
		JSONObject map = new JSONObject();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		List args = null;
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			args = getInitArgs(cfg);
			args.add("budget.fund.rule"); // Remote Object
			args.add("document_check_fund_spending"); // method
			
			/*
			 * Parameters
			 */
			List a = new ArrayList();
			
	        a.add(items);
	        
	        /*
	         * Final
	         */
			args.add(a);

			/*
			 * log
			 */
			InterfaceUtil.logInfo(args, log);
	        
	        /*
	         * Call
	         */
			Object res = client.invoke("execute_kw", args);
			
			/*
			 * Result
			 */
			log.info("res="+res);
			
			XmlRpcStruct strc = (XmlRpcStruct)res;
			
			for(Object k : strc.keySet()) {
				Object v = strc.get(k);
	
				log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			}
			
			map.put("valid", (Boolean)strc.get("budget_ok"));
			map.put("msg", strc.get("message").toString());
		}
		catch (Exception ex) {
			log.error(args.toString(),ex);
		}
		
		return map;
	}	
	
	public JSONObject checkBudget(String docType, String budgetCcType,Integer budgetCc, Integer fundId, List<Map<String, Object>> items) throws Exception {
		log.info("interface : pabiweb_check_budget");
		
		JSONObject map = new JSONObject();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		List args = null;
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			args = getInitArgs(cfg);
			args.add("account.budget"); // Remote Object
			args.add("pabiweb_check_budget"); // method
			
			/*
			 * Parameters
			 */
			List a = new ArrayList();
			
	        a.add(docType);
			
    		String budgetType = (String)InterfaceConstant.BUDGET_TYPE.get(budgetCcType);
    		
	        a.add(CommonDateTimeUtil.convertToOdooFieldDate(CommonDateTimeUtil.now()));
	        a.add(budgetType);
	        
	        a.add(budgetCc);
	        a.add(budgetCcType.equals("P") ? fundId : false);
	        
	        a.add(items);
	        
	        /*
	         * Final
	         */
			args.add(a);


			/*
			 * log
			 */
			InterfaceUtil.logInfo(args, log);
	        
	        
	        /*
	         * Call
	         */
			Object res = client.invoke("execute_kw", args);
			
			/*
			 * Result
			 */
			log.info("res="+res);
			
			XmlRpcStruct strc = (XmlRpcStruct)res;
			
			for(Object k : strc.keySet()) {
				Object v = strc.get(k);
	
				log.info(" - "+k+" : "+v+":"+v.getClass().getName());
			}
			
			map.put("valid", (Boolean)strc.get("budget_ok"));
			map.put("msg", strc.get("message").toString());
		}
		catch (Exception ex) {
			log.error(args.toString(),ex);
		}
		
		return map;
	}	
}
