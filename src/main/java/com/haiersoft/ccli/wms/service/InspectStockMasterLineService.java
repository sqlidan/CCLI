package com.haiersoft.ccli.wms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.dao.InspectStockMasterLineDao;

import com.haiersoft.ccli.wms.entity.InspectStockMasterline;

@Service
public class InspectStockMasterLineService extends BaseService<InspectStockMasterline, String>{
	@Autowired
	private InspectStockMasterLineDao inspectStockMasterLineDao;
	
	@Override
    public HibernateDao<InspectStockMasterline, String> getEntityDao() {
        return inspectStockMasterLineDao;
    }
	
	public Page<InspectStockMasterline> findInspectPlans(Page<InspectStockMasterline> page, InspectStockMasterline params) {
        return inspectStockMasterLineDao.findInspectPlans(page, params);
    }

    public String auditInspectPlans(User user, String inspectId) {
        return inspectStockMasterLineDao.findInspectPlans(user, inspectId);
    }
    
    public int getNo() {
        return inspectStockMasterLineDao.getSequenceId("SEQ_INSPECT_STOCK_MASTERLINE");
    }
}
