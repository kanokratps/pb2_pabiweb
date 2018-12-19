package pb.common.util;

import java.text.DecimalFormat;

public class StringUtil {
	public static String convertEnterToBrTag(String s) {
		return s.replace("\n", "<br/>");
	}
	
	public static String forShow(Double d, String format) {
		DecimalFormat df = new DecimalFormat(format);
		
		return df.format(d); 
	}
	public static String addZWSP(String string) {
		if (string !=null && string.length()>0) {
			char[] chars = string.toCharArray();
			StringBuilder builder = new StringBuilder(string.substring(0, 1));
			for (int i = 1; i < chars.length; i++) {
				builder.append("\u200B");
				builder.append(chars[i]);
			}
			return builder.toString();
		}
		else {
			return string;
		}
	}
	
	public static String replaceNBSP(String string) {
		if (string !=null && string.length()>0) {
			return string.replace(" ", "\u00A0");
		}
		else {
			return string;
		}
	}
	
}
