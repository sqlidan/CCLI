package com.haiersoft.ccli.wms.quartz;

import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.system.entity.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每日汇总保税托盘库存并回填保税货物底账。
 */
@DisallowConcurrentExecution
public class BondedLedgerStockSyncJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(BondedLedgerStockSyncJob.class);

    @Autowired
    private BaseBoundedService baseBoundedService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        String jobName = scheduleJob == null ? "BondedLedgerStockSyncJob" : scheduleJob.getName();
        try {
            int count = baseBoundedService.syncAutoBaseBoundedStockInfo();
            LOGGER.info("任务[{}]完成保税货物底账库存回填，共处理{}条底账。", jobName, count);
        } catch (Exception e) {
            LOGGER.error("任务[{}]执行保税货物底账库存回填失败。", jobName, e);
            throw new JobExecutionException(e);
        }
    }
}
