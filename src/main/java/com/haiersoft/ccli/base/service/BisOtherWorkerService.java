package com.haiersoft.ccli.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.BisOtherWorkerDao;
import com.haiersoft.ccli.base.entity.BisOtherWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.WorkReportDao;
import com.haiersoft.ccli.cost.entity.WorkReport;

@Service
public class BisOtherWorkerService extends BaseService<BisOtherWorker, String>{
	
	@Autowired
	private BisOtherWorkerDao bisOtherWorkerDao;
	
	@Autowired
	private WorkReportDao workReportDao;
	
	@Override
	public HibernateDao<BisOtherWorker, String> getEntityDao() {
		return bisOtherWorkerDao;
	}

	public Page<WorkReport> searchOtherWorkerReport(Page<WorkReport> page,
			WorkReport workReport) {
		
		return workReportDao.searchOtherWorkerReport(page,workReport);
	}

	public Page<WorkReport> searchOtherWorkerReportHeji(Page<WorkReport> page,
			WorkReport workReport) {
		
		return workReportDao.searchOtherWorkerReportHeji(page,workReport);
	} 
}
