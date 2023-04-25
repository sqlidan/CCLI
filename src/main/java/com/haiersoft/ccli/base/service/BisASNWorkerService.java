package com.haiersoft.ccli.base.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.BisAsnWorkerDao;
import com.haiersoft.ccli.base.entity.BisAsnWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class BisASNWorkerService extends BaseService<BisAsnWorker, String>{
	@Autowired
	private BisAsnWorkerDao bisAsnWorkerDao;
	
	@Override
	public HibernateDao<BisAsnWorker, String> getEntityDao() {
		return bisAsnWorkerDao;
	}
	
	public Page<BisAsnWorker> getAllAsnWorker(Page<BisAsnWorker> page, BisAsnWorker bisAsnWorker ,String asn) {
        return bisAsnWorkerDao.getAllAsnWorker(page, bisAsnWorker ,asn);
    }
	
}
