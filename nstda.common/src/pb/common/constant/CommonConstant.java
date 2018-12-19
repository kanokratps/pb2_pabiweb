package pb.common.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.namespace.QName;

public class CommonConstant {
	
	/*
	 * Value
	 */
	public static final String V_ENABLE = "1";
	public static final String V_DISABLE = "0";
	
	/*
	 * Global Properties
	 */
	public static final String GP_SHARE_PROTOCOL = "share.protocol";
	public static final String GP_SHARE_HOST = "share.host";
	public static final String GP_SHARE_PORT = "share.port";
	public static final String GP_SHARE_CONTEXT = "share.context";
	
	public static final String GP_ALF_PROTOCOL = "alfresco.protocol";
	public static final String GP_ALF_HOST = "alfresco.host";
	public static final String GP_ALF_PORT = "alfresco.port";
	public static final String GP_ALF_CONTEXT = "alfresco.context";
	
	public static final String GP_MAIL_HOST = "mail.host";
	public static final String GP_MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

	public static final String GP_MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String GP_MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String GP_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String GP_MAIL_DEBUG = "mail.debug";

	public static final String GP_MAIL_SMTP_SOCKET_PORT = "mail.smtp.socketFactory.port";//May be not used
	public static final String GP_MAIL_SOCKET_CLASS = "mail.smtp.socketFactory.class";//May be not used
	
	public static final String GP_MAIL_SOCKET_FALLBACK = "mail.smtp.socketFactory.fallback";

	public static final String GP_MAIL_USERNAME = "mail.username";
	public static final String GP_MAIL_PASSWORD = "mail.password";	

	public static final String GP_REPORT_PATH = "/META-INF/report";	
	public static final String GP_REPORT_IMG_PATH = GP_REPORT_PATH+"/img";	
	
	public static final String GP_ODOO_ADMIN_PASSWORD = "odoo.admin.password";	
	
	
	/*
	 * Module
	 */
	public static final String MODULE_ADMIN = "ADMIN"; 
	public static final String MODULE_PCM = "PCM"; 
	public static final String MODULE_EXP = "EXP"; 
	public static final String MODULE_HR = "HR"; 
	
	public static final String MODULE_PROP_FILE = "pb-module.properties";
	
	public static final Map<String, String> MODULES = new HashMap<String, String>();
	
    static {
    	MODULES.put(MODULE_ADMIN, "Administrator");
    	MODULES.put(MODULE_PCM, "Procurement");
    	MODULES.put(MODULE_EXP, "Expenses");
    	MODULES.put(MODULE_EXP, "HumanResource");
    }
    
	/*
	 * Sub Module
	 */
	public static final String SUB_MODULE_PCM_REQ = MODULE_PCM + "_" + "REQ"; 
	public static final String SUB_MODULE_PCM_ORD = MODULE_PCM + "_" + "ORD"; 
	public static final String SUB_MODULE_EXP_BRW = MODULE_EXP + "_" + "BRW"; 
	public static final String SUB_MODULE_EXP_USE = MODULE_EXP + "_" + "USE"; 
	public static final String SUB_MODULE_HR_SAL = MODULE_HR + "_" + "SAL"; 
	
	public static final String SUB_MODULE_PREFIX_PCM_REQ = "PR";
	public static final String SUB_MODULE_PREFIX_PCM_ORD = "PD";
	public static final String SUB_MODULE_PREFIX_EXP_BRW = "AV";
	public static final String SUB_MODULE_PREFIX_EXP_USE = "EX";
	public static final String SUB_MODULE_PREFIX_HR_SAL = "SL";
	
	public static final Map<String, String> SUB_MODULE_TABLE_NAMES = new HashMap<String, String>();
	
    static {
    	SUB_MODULE_TABLE_NAMES.put(SUB_MODULE_PREFIX_PCM_REQ, "pb2_pcm_req");
    	SUB_MODULE_TABLE_NAMES.put(SUB_MODULE_PREFIX_PCM_ORD, "pb2_pcm_ord");
    	SUB_MODULE_TABLE_NAMES.put(SUB_MODULE_PREFIX_EXP_BRW, "pb2_exp_brw");
    	SUB_MODULE_TABLE_NAMES.put(SUB_MODULE_PREFIX_EXP_USE, "pb2_exp_use");
    	SUB_MODULE_TABLE_NAMES.put(SUB_MODULE_PREFIX_HR_SAL, "pb2_hr_salary");
    }
    
    public static final String GLOBAL_URI_PREFIX = "/pb";
    
    public static final String SESSION_PAGE_UUID = "SESSION_PAGE_UUID";
    
    public static final String ENGLISH = "en";

    public static final String TFN_TOTAL_ROW_COUNT = "TOTALROWCOUNT";
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    public static final String SENCHA_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static final String ODOO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ODOO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static final String GRID_COLUMN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String GRID_COLUMN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String SENCHA_DATE_FIELD_DATE_FORMAT = "dd/MM/yyyy";
    
    public static final String DATE_FORMAT_THAI_DAY = "ว";
    public static final String DATE_FORMAT_THAI_MONTH = "ด";
    public static final String DATE_FORMAT_BUDDHIST_ERA_YEAR = "ป";
    
    public static final int DIFF_BE_AD = 543; 
    
    public static final String MONEY_FORMAT = "#,##0.00";
    
    /*
     * Replace Signature
     */
	public static final String REPS_PREFIX = "${";
	public static final String REPS_SUFFIX = "}";
    
    /*
     * Format Signature
     */
	public static final String FMTS_WRONG_PREFIX = "?[&quot;";
	public static final String FMTS_PREFIX = "?[\"";
	
	public static final String FMTS_WRONG_PREFIX_2 = "?string[&quot;";
	public static final String FMTS_PREFIX_2 = "?string[\"";
	
	public static final String FMTS_WRONG_SUFFIX = "&quot;]}";
	public static final String FMTS_SUFFIX = "\"]}";


	/*
	 * REPORT_DEFAULT_FORMAT
	 */
	public static final String RDF_FLOAT = "#,##0.00";
	public static final String RDF_DATE = "dd/MM/yyyy"; /**"dd MMMM yyyy"**/;
	
	/*
	 * Thai Month
	 */
	public static final List<String> THAI_DAY_LONG; // day of week
	public static final List<String> THAI_DAY_SHORT; // day of week
	
	public static final List<String> THAI_MONTH_LONG;
	public static final List<String> THAI_MONTH_SHORT;
	
	public static final Map<String, List<String>> THAI_DAY;
	public static final Map<String, List<String>> THAI_MONTH;
	
	static {
		THAI_DAY_LONG = new ArrayList<String>();
		THAI_DAY_SHORT = new ArrayList<String>();
		
		THAI_MONTH_LONG = new ArrayList<String>();
		THAI_MONTH_SHORT = new ArrayList<String>();
		
		THAI_DAY = new HashMap<String, List<String>>();
		THAI_MONTH = new HashMap<String, List<String>>();
		
		THAI_DAY.put(DATE_FORMAT_THAI_DAY, THAI_DAY_SHORT);
		THAI_DAY.put(DATE_FORMAT_THAI_DAY+DATE_FORMAT_THAI_DAY, THAI_DAY_LONG);
		
		THAI_MONTH.put(DATE_FORMAT_THAI_MONTH, THAI_MONTH_SHORT);
		THAI_MONTH.put(DATE_FORMAT_THAI_MONTH+DATE_FORMAT_THAI_MONTH, THAI_MONTH_LONG);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2007);
		
		Locale thLocale = new Locale("th");
		
		DateFormat ldf = new SimpleDateFormat("EEEE", thLocale);
		DateFormat sdf = new SimpleDateFormat("EE", thLocale);
		
		for(int i=0; i<7; i++) {
			THAI_DAY_LONG.add(ldf.format(cal.getTime()).substring(3)); // remove prefix 'วัน'
			THAI_DAY_SHORT.add(sdf.format(cal.getTime()));
			
			cal.add(Calendar.DATE, 1);
		}

		ldf = new SimpleDateFormat("MMMM", thLocale);
		sdf = new SimpleDateFormat("MMM", thLocale);

		for(int i=0; i<12; i++) {
			THAI_MONTH_LONG.add(ldf.format(cal.getTime()));
			THAI_MONTH_SHORT.add(sdf.format(cal.getTime()));
			
			cal.add(Calendar.MONTH, 1);
		}

	}
	

    /*
     * User Profile Property Map
     */
	public static final Map<String, QName> USER_PROFILE_PROP_MAP = new LinkedHashMap<String, QName>();
	
    static {
    	USER_PROFILE_PROP_MAP.put("organization" , ContentModel.PROP_ORGANIZATION);
    	USER_PROFILE_PROP_MAP.put("location" , ContentModel.PROP_LOCATION);
    	USER_PROFILE_PROP_MAP.put("googleusername" , ContentModel.PROP_GOOGLEUSERNAME);
    	USER_PROFILE_PROP_MAP.put("mobile" , ContentModel.PROP_MOBILE);
    	USER_PROFILE_PROP_MAP.put("telephone" , ContentModel.PROP_TELEPHONE);
    	USER_PROFILE_PROP_MAP.put("firstname" , ContentModel.PROP_FIRSTNAME);
    	USER_PROFILE_PROP_MAP.put("lastname" , ContentModel.PROP_LASTNAME);
    }	
    
    /*
     * used by approval matrix and pcm
     */
    public static final int MAX_APPROVER = 10;
    public static final int ADMIN_MAX_APPROVER = 15;

    
    /*
     * Function Name
     */
    public static final String FUNC_FULL_NAME = "fullName()";
    public static final String FUNC_FIRST_NAME = "firstName()";
    public static final String FUNC_LAST_NAME = "lastName()";
    
    /*
     * Error
     */
    public static final String FORM_ERR = "|PB_ERR|";
    
    /*
     * Dummy
     */
    public static final String DUMMY_EMPLOYEE_CODE = "XXXXXX";

    /*
     * EXT
     */
    public static final String EXT_XMLRPC_URL = "/xmlrpc/2/object";
    public static final String EXT_ERR_MSG_MISMATCHED = "PABIWEB_NUMBER_MISMATCHED : ";
    
}
