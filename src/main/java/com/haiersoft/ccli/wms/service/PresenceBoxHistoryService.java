package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.PresenceBoxHistoryDao;
import com.haiersoft.ccli.wms.entity.BisPresenceBoxHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by galaxy on 2017/6/23.
 */
@Service
public class PresenceBoxHistoryService extends BaseService<BisPresenceBoxHistory,Integer> {

    @Autowired
    private PresenceBoxHistoryDao presenceBoxHistoryDao;

    @Override
    public HibernateDao<BisPresenceBoxHistory, Integer> getEntityDao() {
        return presenceBoxHistoryDao;
    }

}
