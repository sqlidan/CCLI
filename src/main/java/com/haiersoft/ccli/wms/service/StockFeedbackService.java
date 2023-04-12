package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.StockFeedbackDAO;
import com.haiersoft.ccli.wms.entity.BisCheckStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockFeedbackService extends BaseService<BisCheckStock, Integer> {

    @Autowired
    private StockFeedbackDAO stockFeedbackDAO;

    @Override
    public HibernateDao<BisCheckStock, Integer> getEntityDao() {
        return stockFeedbackDAO;
    }

    public Page<BisCheckStock> pageRecordStock(Page<BisCheckStock> page, BisCheckStock checkStock) {
        return stockFeedbackDAO.pageRecordStock(page,checkStock);
    }

}
