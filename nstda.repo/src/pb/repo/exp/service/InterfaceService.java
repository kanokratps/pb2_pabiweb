package pb.repo.exp.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.alfresco.service.cmr.security.AuthenticationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dynamicextensionsalfresco.webscripts.annotations.Transaction;
import com.github.dynamicextensionsalfresco.webscripts.annotations.TransactionType;

import pb.common.constant.CommonConstant;
import pb.common.model.FileModel;
import pb.common.util.CommonDateTimeUtil;
import pb.common.util.CommonUtil;
import pb.common.util.InterfaceUtil;
import pb.common.util.NodeUtil;
import pb.repo.admin.constant.MainBudgetSrcConstant;
import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;
import pb.repo.admin.service.AdminSectionService;
import pb.repo.admin.service.AdminUserService;
import pb.repo.exp.constant.ExpBrwAttendeeConstant;
import pb.repo.exp.model.ExpBrwModel;
import pb.repo.exp.model.ExpUseModel;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcStruct;

@Service
public class InterfaceService {

	private static Logger log = Logger.getLogger(InterfaceService.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	ExpBrwService expBrwService;
	
	@Autowired
	ExpUseService expUseService;
	
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
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	public String createAV(ExpBrwModel model) throws Exception {
		log.info("interface : createAV");
		
		Boolean success = false;
		String msgs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);
		
		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			List<Map<String, Object>> list = expBrwService.listForInf(model.getId());
			Map<String, Object> data = list.size() > 0 ? list.get(0) : null;
			
			List args = getInitArgs(cfg);
			args.add("hr.expense.expense"); // Remote Object
			args.add("generate_hr_expense"); // method
			
			List a = new ArrayList();
			
			/*
			 * Header
			 */
	        map.put("is_employee_advance", "True");
	        map.put("number",data.get("av_id"));
	        map.put("employee_code",data.get("req_by"));
	        map.put("preparer_code",data.get("created_by"));
	        map.put("date",CommonDateTimeUtil.convertToOdooFieldDate(CommonDateTimeUtil.now()));
//	        map.put("write_date",CommonDateTimeUtil.convertToOdooFieldDateTime((Timestamp) data.get("updated_time")));
	        map.put("advance_type",data.get("objective_type"));
	        map.put("date_back",CommonDateTimeUtil.convertToOdooFieldDate(data.get("date_back")!=null ? (Timestamp) data.get("date_back") : CommonDateTimeUtil.now()));
	        map.put("name", data.get("objective"));
	        map.put("apweb_ref_url", NodeUtil.trimNodeRef((String)data.get("doc_ref")));
	        map.put("receive_method", receiveMethodForInf((String)data.get("bank_type")));
	        map.put("employee_bank_id.id", data.get("bank")!=null ? data.get("bank") : "");
	        map.put("reason_bypass_procure", data.get("reason")!=null ? data.get("reason") : "");
	        map.put("note", data.get("note")!=null ? data.get("note") : "");
	        map.put("remark", data.get("av_remark")!=null ? data.get("av_remark") : "");
	        map.put("origin_pr_number", data.get("is_small_amount").equals("1") && data.get("origin_pr_number")!=null ? data.get("origin_pr_number") : "");
	        
//			Map<String, Object> lastAppMap = expBrwService.getLastApprover(model.getId());
	        map.put("approver_code", authService.getCurrentUserName());
//			map.put("approver_time", lastAppMap != null ? CommonDateTimeUtil.convertToGridDateTime((Timestamp)lastAppMap.get("time")) : "");
	        
	        
//	        for(String key : map.keySet()) {
//	        	log.info(" - "+key+":"+map.get(key));
//	        }
	        
	        /*
	         * Line Item
	         */
			list = expBrwService.listDtlForInf(model.getId());
	        
	        List orderLine = new ArrayList();
	        
	        String bgType = (String)data.get("budget_cc_type");
	        String bg = ((Integer)data.get("budget_cc")).toString();
	        String sid = bgType.equals(MainBudgetSrcConstant.TYPE_UNIT) ? bg : "";
	        String pid = bgType.equals(MainBudgetSrcConstant.TYPE_PROJECT) ? bg : "";
	        String aid = bgType.equals(MainBudgetSrcConstant.TYPE_ASSET) ? bg : "";
	        String cid = bgType.equals(MainBudgetSrcConstant.TYPE_CONSTRUCTION) ? bg : "";
	        
	        String ccid = data.get("cost_control_id")!=null ? data.get("cost_control_id").toString() : "";
	        
	        String fundid = data.get("fund_id")!=null ? data.get("fund_id").toString() : "";
	        
	        for(Map<String, Object> dtl:list) {
		        Map<String, Object> line = new HashMap<String, Object>();
		        line.put("section_id.id",sid);
		        line.put("project_id.id",pid);
		        line.put("invest_asset_id.id",aid);
		        line.put("invest_construction_phase_id.id",cid);
		        line.put("fund_id.id",fundid);
		        line.put("is_advance_product_line","True");
		        line.put("activity_group_id.id",String.valueOf(dtl.get("act_grp_id")));
		        line.put("activity_id.id",String.valueOf(dtl.get("act_id")));
		        line.put("name",dtl.get("activity"));
		        line.put("unit_amount",String.valueOf(dtl.get("amount")));
		        line.put("cost_control_id.id", ccid);
		        orderLine.add(line);
		        
//		        for(String key : line.keySet()) {
//		        	log.info(" -- "+key+":"+line.get(key));
//		        }
	        }
	        
	        map.put("line_ids", orderLine);
	
	        /*
	         * Employee
	         */
	        List<Map<String, Object>> empList = expBrwService.listAttendeeByMasterIdAndType(model.getId(), ExpBrwAttendeeConstant.T_EMPLOYEE, null);
	        log.info("empList.size()="+empList.size());
	        
	        List employees = new ArrayList();
	        
	        int seq = 1;
	        
	        for(Map<String, Object> emp:empList) {
	        	
		        Map<String, Object> employee = new HashMap<String, Object>();
		        employee.put("sequence", String.valueOf(seq++));
		        employee.put("employee_code", emp.get("code")); 
		        employees.add(employee);
		        
//		        for(String key : employee.keySet()) {
//		        	log.info(" ---- "+key+":"+employee.get(key));
//		        }
	        }
	        
	        map.put("attendee_employee_ids", employees);
	        
			/*
			 * Non employee
			 */
	        List<Map<String, Object>> nonempList = expBrwService.listAttendeeByMasterIdAndType(model.getId(), ExpBrwAttendeeConstant.T_OTHER, null);
	        log.info("nonempList.size()="+nonempList.size());
	        
	        List nonemployees = new ArrayList();
	        
	        seq = 1;
     	        
	        for(Map<String, Object> nonemp:nonempList) {
	        	
		        String title = nonemp.get("title_th")!=null ? (String)nonemp.get("title_th") : null;
	        	
		        Map<String, Object> nonemployee = new HashMap<String, Object>();
		        nonemployee.put("sequence", String.valueOf(seq++));
		        nonemployee.put("attendee_name", (title != null && !title.equals("null") ? title+" " : "")+nonemp.get("fname")+" "+nonemp.get("lname")); 
		        nonemployee.put("position", nonemp.get("position")!=null ? nonemp.get("position") : ""); 
		        nonemployee.put("organization", nonemp.get("unit_type")!=null ? nonemp.get("unit_type") : ""); 
		        nonemployees.add(nonemployee);
		        
//		        for(String key : nonemployee.keySet()) {
//		        	log.info(" ----- "+key+":"+nonemployee.get(key));
//		        }
	        }
	        
	        map.put("attendee_external_ids", nonemployees);
	        
	        /*
	         * Attachment
	         */
	        List<FileModel> fileList = expBrwService.listFile(model.getId(), false);
	        log.info("fileList.size()="+fileList.size());
	        
	        List attachment = new ArrayList();
	        
	        for(FileModel file:fileList) {
	// detail : http://localhost:18080/share/page/document-details?nodeRef=workspace://SpacesStore/5190b027-00c6-4322-98c3-1be01314bdd9
	//download format url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/<url>/<name>?a=true
	//example url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/260cdb8d-5a9c-43af-9c8e-178c75f1fe38/PR16000050.pdf?a=true
	        	
		        Map<String, Object> att = new HashMap<String, Object>();
		        att.put("name", file.getName()); 
		        att.put("description", file.getDesc()!=null ? file.getDesc() : ""); 
		        att.put("url",NodeUtil.trimNodeRef(file.getNodeRef().toString()));
		        att.put("attach_by", file.getBy());
		        attachment.add(att);
		        
//		        for(String key : att.keySet()) {
//		        	log.info(" ------ "+key+":"+att.get(key));
//		        }
	        }
	        
	        map.put("attachment_ids", attachment);
	        
	        /*
	         * Final
	         */
			a.add(map);
			
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
			
			success = (Boolean)strc.get("is_success");
			msgs = strc.get("messages")!=null ? strc.get("messages").toString() : "";

		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return ex.toString()+":"+map.toString();
		}
		
		return success ? "OK" : msgs+":"+map.toString();
	}	
	
	@Transaction(value=TransactionType.REQUIRES_NEW)
	public String createEX(ExpUseModel model) throws Exception {
		log.info("interface : createEX");
		
		Boolean success = false;
		String msgs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		final Properties gProp = CommonUtil.getGlobalProperties();
		String odooPassword = gProp.getProperty(CommonConstant.GP_ODOO_ADMIN_PASSWORD);

		try {
			Map<String, Object> cfg = getConnectionConfig(odooPassword);
			XmlRpcClient client = getXmlRpcClient(cfg);
			
			List<Map<String, Object>> list = expUseService.listForInf(model.getId());
			Map<String, Object> data = list.size() > 0 ? list.get(0) : null;
			
			List args = getInitArgs(cfg);
			args.add("hr.expense.expense"); // Remote Object
			args.add("generate_hr_expense"); // method
			
			List a = new ArrayList();
			
			/*
			 * Header
			 */
	        map.put("is_employee_advance", "False");
	        map.put("number",data.get("ap_id"));
	        map.put("employee_code",data.get("req_by"));
	        map.put("preparer_code",data.get("created_by"));
	        map.put("date",CommonDateTimeUtil.convertToOdooFieldDate(CommonDateTimeUtil.now()));
//	        map.put("write_date",CommonDateTimeUtil.convertToOdooFieldDateTime((Timestamp) data.get("updated_time")));
	        map.put("advance_type","");
	        map.put("date_back",CommonDateTimeUtil.convertToOdooFieldDate(data.get("cost_control_to")!=null ? (Timestamp) data.get("cost_control_to") : CommonDateTimeUtil.now()));
	        map.put("name", data.get("objective"));
	        map.put("note", data.get("note")!=null ? data.get("note") : "");
	        map.put("apweb_ref_url", NodeUtil.trimNodeRef((String)data.get("doc_ref")));
	        map.put("reason_bypass_procure", data.get("reason")!=null ? data.get("reason") : "");
	        map.put("remark", "");
//	        map.put("is_small_amount", data.get("is_small_amount").equals("1"));
//	        map.put("origin_pr_number", data.get("origin_pr_number")!=null ? data.get("origin_pr_number") : "");
	        map.put("origin_pr_number", data.get("is_small_amount").equals("1") && data.get("pay_dtl2")!=null ? data.get("pay_dtl2") : "");
	        
//			Map<String, Object> lastAppMap = expUseService.getLastApprover(model.getId());
	        map.put("approver_code", authService.getCurrentUserName());
//			map.put("approver_time", lastAppMap != null ? CommonDateTimeUtil.convertToGridDateTime((Timestamp)lastAppMap.get("time")) : "");
	        
	        String payType = (String)data.get("pay_type");
	        String payTo = null;
	        String supText = null;
	        String avId = null;
	        String avClear = "False";
	        String iSecId = "";
	        String iPrjId = "";
	        String iActId = "";
	        
	        if (payType.equals("0")) {
	        	payTo = "employee";
	        	avId = "";
	        	supText = "";
	        }
	        else
	        if (payType.equals("1")) {
	        	payTo = "supplier";
	        	avId = "";
	        	supText = (String)data.get("pay_dtl1");
	        }
	        else
	        if (payType.equals("2")) {
	        	payTo = "employee";
	        	avId = (String)data.get("pay_dtl1");
	        	supText = "";
	        	avClear = "True";
	        }
	        else
	        if (payType.equals("3")) {
	        	payTo = "internal";
	        	avId = "";
	        	supText = "";
	        	if (data.get("pay_dtl2").equals(MainBudgetSrcConstant.TYPE_UNIT)) {
	        		iSecId = (String)data.get("pay_dtl1");
	        	} else {
	        		iPrjId = (String)data.get("pay_dtl1");
	        	}
	        	//iActId = (String)data.get("pay_dtl3");
	        }
	        else
	        if (payType.equals("4")) {
	        	payTo = "pettycash";
	        	avId = "";
	        	supText = "";
		        map.put("pettycash_id.id", (String)data.get("pay_dtl1"));
	        }
	        
	        map.put("receive_method", payType.equals("0") || payType.equals("2") ? receiveMethodForInf((String)data.get("bank_type")) : "");
	        map.put("employee_bank_id.id", data.get("bank")!=null ? data.get("bank") : "");
	        map.put("pay_to", payTo);
	        map.put("supplier_text", supText);
	        map.put("is_advance_clearing", avClear);
	        map.put("advance_expense_number", avId);
	        map.put("internal_section_id.id", iSecId);
	        map.put("internal_project_id.id", iPrjId);
//	        map.put("activity_id.id", iActId);
	        
//	        for(String key : map.keySet()) {
//	        	log.info(" - "+key+":"+map.get(key));
//	        }
	        
	        /*
	         * Line Item
	         */
			list = expUseService.listDtlForInf(model.getId());
	        
	        List orderLine = new ArrayList();
	        
	        String bgType = (String)data.get("budget_cc_type");
	        String bg = ((Integer)data.get("budget_cc")).toString();
	        String sid = bgType.equals(MainBudgetSrcConstant.TYPE_UNIT) ? bg : "";
	        String pid = bgType.equals(MainBudgetSrcConstant.TYPE_PROJECT) ? bg : "";
	        String aid = bgType.equals(MainBudgetSrcConstant.TYPE_ASSET) ? bg : "";
	        String cid = bgType.equals(MainBudgetSrcConstant.TYPE_CONSTRUCTION) ? bg : "";
	        
	        String ccid = data.get("cost_control_id")!=null ? data.get("cost_control_id").toString() : "";
	        
	        String fundid = data.get("fund_id")!=null ? data.get("fund_id").toString() : "";
	        
	        for(Map<String, Object> dtl:list) {
		        Map<String, Object> line = new HashMap<String, Object>();
		        line.put("section_id.id",sid);
		        line.put("project_id.id",pid);
		        line.put("invest_asset_id.id",aid);
		        line.put("invest_construction_phase_id.id",cid);
		        line.put("fund_id.id",fundid);
		        line.put("is_advance_product_line","False");
		        line.put("activity_group_id.id",String.valueOf(dtl.get("act_grp_id")));
		        line.put("activity_id.id",String.valueOf(dtl.get("act_id")));
		        line.put("name",dtl.get("activity"));
		        line.put("unit_amount",String.valueOf(dtl.get("amount")));
		        line.put("cost_control_id.id", ccid);
		        
		        orderLine.add(line);
		        
//		        for(String key : line.keySet()) {
//		        	log.info(" -- "+key+":"+line.get(key));
//		        }
	        }
	        
	        map.put("line_ids", orderLine);
	
	        /*
	         * Employee
	         */
	        List<Map<String, Object>> empList = expUseService.listAttendeeByMasterIdAndType(model.getId(), ExpBrwAttendeeConstant.T_EMPLOYEE, null);
	        log.info("empList.size()="+empList.size());
	        
	        List employees = new ArrayList();
	        
	        int seq = 1;
	        
	        for(Map<String, Object> emp:empList) {
	        	
		        Map<String, Object> employee = new HashMap<String, Object>();
		        employee.put("sequence", String.valueOf(seq++));
		        employee.put("employee_code", emp.get("code")); 
		        employees.add(employee);
		        
//		        for(String key : employee.keySet()) {
//		        	log.info(" ---- "+key+":"+employee.get(key));
//		        }
	        }
	        
	        map.put("attendee_employee_ids", employees);
	        
			/*
			 * Non employee
			 */
	        List<Map<String, Object>> nonempList = expUseService.listAttendeeByMasterIdAndType(model.getId(), ExpBrwAttendeeConstant.T_OTHER, null);
	        log.info("nonempList.size()="+nonempList.size());
	        
	        List nonemployees = new ArrayList();
	        
	        seq = 1;
	        
	        for(Map<String, Object> nonemp:nonempList) {
	        	
	        	String title = nonemp.get("title_th")!=null ? (String)nonemp.get("title_th") : null;
	        	
		        Map<String, Object> nonemployee = new HashMap<String, Object>();
		        nonemployee.put("sequence", String.valueOf(seq++));
		        nonemployee.put("attendee_name", (title != null && !title.equals("null") ? title+" " : "")+nonemp.get("fname")+" "+nonemp.get("lname")); 
		        nonemployee.put("position", nonemp.get("position")!=null ? nonemp.get("position") : ""); 
		        nonemployee.put("organization", nonemp.get("unit_type")!=null ? nonemp.get("unit_type") : ""); 
		        nonemployees.add(nonemployee);
		        
//		        for(String key : nonemployee.keySet()) {
//		        	log.info(" ----- "+key+":"+nonemployee.get(key));
//		        }
	        }
//	        Missing required value for the field 'Description' (name)
//	        Map<String, Object> nonemployee = new HashMap<String, Object>();
//	        nonemployee.put("sequence", "1");
//	        nonemployee.put("attendee_name", "a b"); 
//	        nonemployee.put("position", "c"); 
//	        nonemployees.add(nonemployee);
	        
	        map.put("attendee_external_ids", nonemployees);
	        
	        /*
	         * Attachment
	         */
	        List<FileModel> fileList = expUseService.listFile(model.getId(), false);
	        log.info("fileList.size()="+fileList.size());
	        
	        List attachment = new ArrayList();
	        
	        for(FileModel file:fileList) {
	// detail : http://localhost:18080/share/page/document-details?nodeRef=workspace://SpacesStore/5190b027-00c6-4322-98c3-1be01314bdd9
	//download format url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/<url>/<name>?a=true
	//example url : http://localhost:18080/share/proxy/alfresco/api/node/content/workspace/SpacesStore/260cdb8d-5a9c-43af-9c8e-178c75f1fe38/PR16000050.pdf?a=true
	        	
		        Map<String, Object> att = new HashMap<String, Object>();
		        att.put("name", file.getName()); 
		        att.put("description", file.getDesc()!=null ? file.getDesc() : ""); 
		        att.put("url",NodeUtil.trimNodeRef(file.getNodeRef().toString()));
		        att.put("attach_by", file.getBy());
		        attachment.add(att);
		        
//		        for(String key : att.keySet()) {
//		        	log.info(" ------ "+key+":"+att.get(key));
//		        }
	        }
	        
	        map.put("attachment_ids", attachment);
	        
	        /*
	         * Final
	         */
			a.add(map);
			
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
			
			success = (Boolean)strc.get("is_success");
			msgs = strc.get("messages")!=null ? strc.get("messages").toString() : "";
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return ex.toString()+":"+map.toString();
		}
		
		return success ? "OK" : msgs+":"+map.toString();
	}
	
	private String receiveMethodForInf(String r) {
		String result = null;
		
		if (r.equals("0")) {
			result = "salary_bank";
		} else
		{
			result = "other_bank";
		}
			
		return result;
	}
	
}
