package com.haiersoft.ccli.cost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.BisReceivableFeeStatDao;
import com.haiersoft.ccli.cost.entity.BisReceivableFeeStat;

@Service
public class BisReceivableFeeStatService extends BaseService<BisReceivableFeeStat, Long> {

    @Autowired
    private BisReceivableFeeStatDao bisReceivableFeeStatDao;

    @Override
    public HibernateDao<BisReceivableFeeStat, Long> getEntityDao() {
        return bisReceivableFeeStatDao;
    }

    @Transactional(readOnly = false)
    public int rebuildYesterdayReceivableFeeStat() {
        return bisReceivableFeeStatDao.rebuildYesterdayReceivableFeeStat();
    }


}
