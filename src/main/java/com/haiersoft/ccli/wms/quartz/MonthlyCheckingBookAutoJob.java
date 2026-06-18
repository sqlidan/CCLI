package com.haiersoft.ccli.wms.quartz;

import com.haiersoft.ccli.cost.service.BisCheckingBookService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class MonthlyCheckingBookAutoJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyCheckingBookAutoJob.class);

    @Autowired
    private BisCheckingBookService bisCheckingBookService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        String jobName = scheduleJob == null ? "MonthlyCheckingBookAutoJob" : scheduleJob.getName();
        try {
            int count = bisCheckingBookService.insertCurrentMonthAutoCheckingBook();
            LOGGER.info("Job [{}] inserted {} monthly auto checking book records.", jobName, count);
        } catch (Exception e) {
            LOGGER.error("Job [{}] failed to insert monthly auto checking book.", jobName, e);
            throw new JobExecutionException(e);
        }
    }
}
