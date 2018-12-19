package pb.repo.admin.util;

public class RptUtil {
	
	public static String radio(Boolean on) {
		return on ? "/radio_on.png" : "/radio_off.png";
	}
	
	public static String checkbox(Boolean on) {
		return on ? "/checked.png" : "/unchecked.png";
	}
}
