package com.haiersoft.ccli.system.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.haiersoft.ccli.cost.service.StoreFeeService;
import com.haiersoft.ccli.supervision.service.SupervisionService;
import com.haiersoft.ccli.supervision.web.SupervisionController;
import com.haiersoft.ccli.system.entity.ScheduleJob;

/**
 * 定时任务工作类
 * @author ty
 * @date 2015年1月13日
 */
@DisallowConcurrentExecution  
public class TaskA implements Job {
	@Autowired
	private SupervisionService supervisionService;
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//SupervisionController supervisionController=new SupervisionController();
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"); 
        supervisionService.createXml();
	    System.out.println("任务名称 = [" + scheduleJob.getName() + "]"+ " 在 " + dateFormat.format(new Date())+" 时运行"); 
    }
}
