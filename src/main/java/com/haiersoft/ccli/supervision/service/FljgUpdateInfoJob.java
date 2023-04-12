package com.haiersoft.ccli.supervision.service;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.haiersoft.ccli.common.mapper.JsonMapper;

/**
 * 定时获取海关监管信息
 * @author
 * @date
 */
@DisallowConcurrentExecution 
public class FljgUpdateInfoJob implements Job  {

	private static Logger logger = LoggerFactory.getLogger(FljgUpdateInfoJob.class);
	
	@Autowired
	GetKeyService getKeyService;
	
	@Autowired
	FljgWsClient fljgWsClient;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("FljgUpdateInfoJob:  "+new Date().toString());
		System.out.println("FljgUpdateInfoJob:  "+new Date().toString());
		//1.获取key
		String tickId = getKeyService.builderTest();
		
	}

}
