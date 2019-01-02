package pb.repo.admin.schedule;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pb.repo.admin.constant.MainMasterConstant;
import pb.repo.admin.model.MainMasterModel;
import pb.repo.admin.service.AdminMasterService;

import com.github.dynamicextensionsalfresco.jobs.ScheduledQuartzJob;

/**
 * New Quartz support coming to Dynamic extensions 1.3
 */
@Component
@ScheduledQuartzJob(name = "resetJob", cron="0 5 0 1 10 ?", group="pb") // real : 5 Minutes after New Fiscal Year
//@ScheduledQuartzJob(name = "resetJob", cron="0 */5 * * * ?", group="pb") // test : every 5 Minutes
public class ResetScheduleJob implements Job {
	
	private final Logger log = Logger.getLogger(ResetScheduleJob.class);

	@Autowired
	AdminMasterService masterService;

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("--- | Reset Schedule | ---");
		
		try {
	   		MainMasterModel resetModel = masterService.getSystemConfig(MainMasterConstant.SCC_MAIN_RESET_SEQUENCE);
	   		if (resetModel!=null) {
		   		String seqNames = resetModel.getFlag1();
		   		
		   		if (seqNames!=null && !seqNames.trim().equals("")) {
		   			String[] names = seqNames.split(",");
		   			for(int i=0; i<names.length; i++) {
		   				String name = names[i];
		   				log.info(" - "+name);
		   				masterService.reset(name.trim());
		   			}
		   		}
	   		}
	   		
		} catch (Exception ex) {
			log.error("", ex);
		}
	}
	
}
