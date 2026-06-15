package com.haiersoft.ccli.wms.quartz;

import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class EnterStockAutoFeeJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterStockAutoFeeJob.class);

    @Autowired
    private StandingBookService standingBookService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        String jobName = scheduleJob == null ? "EnterStockAutoFeeJob" : scheduleJob.getName();
        try {
            int count = standingBookService.autoGeneratePendingInFees();
            LOGGER.info("Job [{}] processed auto pending inbound fee generation for {} enter stocks.", jobName, count);
        } catch (Exception e) {
            LOGGER.error("Job [{}] failed to auto generate pending inbound fees.", jobName, e);
            throw new JobExecutionException(e);
        }
    }
}
