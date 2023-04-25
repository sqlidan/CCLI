package com.haiersoft.ccli.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.BisLoadingOrderWorkerDao;
import com.haiersoft.ccli.base.entity.BisLoadingOrderWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class BisLoadingOrderWorkerService extends BaseService<BisLoadingOrderWorker, String>{
	@Autowired
	private BisLoadingOrderWorkerDao bisLoadingOrderWorkerDao;
	
	@Override
	public HibernateDao<BisLoadingOrderWorker, String> getEntityDao() {
		return bisLoadingOrderWorkerDao;
	}

	public Page<BisLoadingOrderWorker> getAllLoadingOrderWorker(
			Page<BisLoadingOrderWorker> page, BisLoadingOrderWorker blow,
			String loadingPlanNum) {
		
		return bisLoadingOrderWorkerDao.getAllLoadingOrderWorker(page, blow ,loadingPlanNum);
	}
}
