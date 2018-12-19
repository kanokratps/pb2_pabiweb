package pb.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.mortbay.log.Log;

import pb.common.constant.CommonConstant;

public class CommonDateTimeUtil {
	
	/*
	 * Convert String Format 'yyyy-MM-dd'T'HH:mm:ss' to Timestamp
	 */
	public static Timestamp convertSenchaStringToTimestamp(String str) throws ParseException {
		
		if (str == null || str.equals("null") || str.equals("")) {
			return null;
		}
		
		if (str.indexOf("T") <= 0) {
			str = "2000-1-1T"+str;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.SENCHA_DATE_TIME_FORMAT,Locale.US);
		Timestamp timestamp = new Timestamp(dateFormat.parse(str).getTime());
		
		return timestamp;
	}
	
	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy HH:mm'
	 */
	public static String convertToGridDateTime(Timestamp timestamp) {
		if (timestamp==null) {
			return "";
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.GRID_COLUMN_DATE_TIME_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}
	
	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy'
	 */
	public static String convertToGridDate(Timestamp timestamp) {
		
		if (timestamp==null) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.GRID_COLUMN_DATE_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}
	
	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy'
	 */
	public static String convertToSenchaFieldDate(Timestamp timestamp) {
		
		if (timestamp==null) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.SENCHA_DATE_FIELD_DATE_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}
	
	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy'T'HH:mm:ss.SSS'
	 */
	public static String convertToSenchaFieldDateTime(Timestamp timestamp) {
		
		if (timestamp==null) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.SENCHA_DATE_TIME_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}	

	/*
	 * Convert Date to String Format 'dd-MM-yyyy'
	 */
	public  String getDisplayDate(Date date) {
		if (date == null) {
			return "";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
		
		String d = df.format(date.getTime());
		String[] splitS = d.split(" ");
		String[] splitD = splitS[0].split("/");
//		Integer yearThai = Integer.valueOf(splitD[2])+543;
		Integer yearThai = Integer.valueOf(splitD[2]);
//		return splitD[0]+"/"+splitD[1]+"/"+yearThai.toString()+" "+ splitS[1];
		return splitD[0]+"/"+splitD[1]+"/"+yearThai.toString();
	}
	
	public  String getSignatureDate(Date date) {
		if (date == null) {
			return "";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
		
		String d = df.format(date.getTime());
		String[] splitS = d.split(" ");
		String[] splitD = splitS[0].split("/");
		Integer yearThai = Integer.valueOf(splitD[2])+CommonConstant.DIFF_BE_AD;
		return splitD[0]+"/"+splitD[1]+"/"+yearThai.toString();
	}
	
	
	/*
	 * Get Now Timestamp
	 */
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy'
	 */
	public static String convertToOdooFieldDate(Timestamp timestamp) {
		
		if (timestamp==null) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.ODOO_DATE_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}	

	/*
	 * Convert Timestamp to String Format 'dd-MM-yyyy'T'HH:mm:ss.SSS'
	 */
	public static String convertToOdooFieldDateTime(Timestamp timestamp) {
		
		if (timestamp==null) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.ODOO_DATE_TIME_FORMAT,Locale.US);
		
		return dateFormat.format(timestamp);
	}	
	
	/*
	 * Convert String Format 'yyyy-MM-dd' to Timestamp
	 */
	public static Timestamp convertOdooStringToTimestamp(String str) throws ParseException {
		
		if (str == null || str.equals("null") || str.equals("")) {
			return null;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.ODOO_DATE_FORMAT,Locale.US);
		Timestamp timestamp = new Timestamp(dateFormat.parse(str).getTime());
		
		return timestamp;
	}

}
