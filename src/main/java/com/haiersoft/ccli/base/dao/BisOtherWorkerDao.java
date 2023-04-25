package com.haiersoft.ccli.base.dao;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BisOtherWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.cost.entity.WorkReport;
@Repository
public class BisOtherWorkerDao extends HibernateDao<BisOtherWorker, String>{

	public Page<WorkReport> searchOtherWorkerReport(Page<WorkReport> page,
			WorkReport workReport) {
		return null;
	}
	
}
