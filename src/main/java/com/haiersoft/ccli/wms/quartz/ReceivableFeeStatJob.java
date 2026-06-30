package com.haiersoft.ccli.wms.quartz;

import com.haiersoft.ccli.cost.service.finance.BisReceivableFeeStatService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component("receivableFeeStatJob")
public class ReceivableFeeStatJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceivableFeeStatJob.class);

    @Autowired
    private BisReceivableFeeStatService bisReceivableFeeStatService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        String jobName = scheduleJob == null ? "ReceivableFeeStatJob" : scheduleJob.getName();
        executeJob(jobName);
    }

    public void execute() throws JobExecutionException {
        executeJob("ReceivableFeeStatJob");
    }

    private void executeJob(String jobName) throws JobExecutionException {
        try {
            int count = bisReceivableFeeStatService.rebuildYesterdayReceivableFeeStat();
            LOGGER.info("Job [{}] rebuilt {} previous-day receivable fee statistic records.", jobName, count);
        } catch (Exception e) {
            LOGGER.error("Job [{}] failed to rebuild receivable fee statistics.", jobName, e);
            throw new JobExecutionException(e);
        }
    }
}
