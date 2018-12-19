package pb.common.util;

import java.util.List;
import org.apache.log4j.Logger;

public class InterfaceUtil {

	public static void logInfo(List args, Logger log) {
		String pwd = (String)args.get(2); // password
		args.remove(2);
		
        log.info("args="+args);

		args.add(2, pwd);
	}
}
