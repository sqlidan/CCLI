package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.InOutHistoryDAO;
import com.haiersoft.ccli.wms.entity.BisInOutHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by galaxy on 2017/6/23.
 */
@Service
public class InOutHistoryService extends BaseService<BisInOutHistory,String> {

    @Autowired
    private InOutHistoryDAO inOutHistoryDAO;

    @Override
    public HibernateDao<BisInOutHistory, String> getEntityDao() {
        return inOutHistoryDAO;
    }

}
