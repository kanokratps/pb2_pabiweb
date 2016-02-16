package nstda.common.constant;

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

	/*
	 * Module
	 */
	public static final String MODULE_ADMIN = "ADMIN"; 
	public static final String MODULE_ISO = "ISO"; 
	public static final String MODULE_MEMO = "MEMO"; 
	public static final String MODULE_MOBILE = "MOBILE"; 
	public static final String MODULE_VIEWER = "VIEWER";
	public static final String MODULE_CAR = "CAR"; 
	public static final String MODULE_CC = "CC"; 
	public static final String MODULE_IQA = "IQA"; 
	public static final String MODULE_NC = "NC"; 
	
	public static final String MODULE_PROP_FILE = "bgear-module.properties";
	
	public static final Map<String, String> MODULES = new HashMap<String, String>();
	
    static {
    	MODULES.put(MODULE_ADMIN, "Administrator");
    	MODULES.put(MODULE_ISO, "ISO");
    	MODULES.put(MODULE_MEMO, "Memo");
    	MODULES.put(MODULE_MOBILE, "Mobile");
    	MODULES.put(MODULE_VIEWER, "Viewer");
    	MODULES.put(MODULE_CAR, "CAR/PAR");
    	MODULES.put(MODULE_CC, "CC");
    	MODULES.put(MODULE_IQA, "IQA");
    	MODULES.put(MODULE_NC, "NC");
    }
    
    public static final String GLOBAL_URI_PREFIX = "/bgear";
    
    public static final String SESSION_PAGE_UUID = "SESSION_PAGE_UUID";
    
    public static final String ENGLISH = "en";

    public static final String TFN_TOTAL_ROW_COUNT = "TOTALROWCOUNT";
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
    public static final String SENCHA_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static final String GRID_COLUMN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String GRID_COLUMN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String SENCHA_DATE_FIELD_DATE_FORMAT = "dd/MM/yyyy";
    
    public static final String DATE_FORMAT_THAI_DAY = "ว";
    public static final String DATE_FORMAT_THAI_MONTH = "ด";
    public static final String DATE_FORMAT_BUDDHIST_ERA_YEAR = "ป";
    
    public static final int DIFF_BE_AD = 543; 
    
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
     * used by approval matrix and memo
     */
    public static final int MAX_APPROVER = 10;
    public static final int ADMIN_MAX_APPROVER = 15;

    
    /*
     * Function Name
     */
    public static final String FUNC_FULL_NAME = "fullName()";
    public static final String FUNC_FIRST_NAME = "firstName()";
    public static final String FUNC_LAST_NAME = "lastName()";
}
