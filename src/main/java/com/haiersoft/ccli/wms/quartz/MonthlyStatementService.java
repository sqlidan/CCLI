package com.haiersoft.ccli.wms.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.cost.service.StoreFeeService;
import com.haiersoft.ccli.system.entity.ScheduleJob;

/**
 * 存储费定时任务工作类
 * @author lv
 * @date 2015年1月13日
 */
@Service
@DisallowConcurrentExecution 
public class MonthlyStatementService  implements Job {
	@Autowired
	private StoreFeeService storeFeeService;
	private static Logger logger = LoggerFactory.getLogger(MonthlyStatementService.class);
	public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");    
        logger.info(">>>>>>>>>>>>>>>>>任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行");
	    logger.error(">>>>>>>>>>>>>>>>>任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行");
	    int nDay=Integer.valueOf(DateUtils.getDay());
	    storeFeeService.creteStortFeeList(nDay);
	    logger.error(">>>>>>>>>>>>>>>>>任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行结束");
	    logger.info(">>>>>>>>>>>>>>>>>任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行结束");
    }
}
