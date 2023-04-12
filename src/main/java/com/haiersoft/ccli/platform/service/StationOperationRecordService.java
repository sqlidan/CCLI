package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.StationOperationRecordDao;
import com.haiersoft.ccli.platform.entity.StationOperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class StationOperationRecordService extends BaseService<StationOperationRecord,Integer> {

    @Autowired
    private StationOperationRecordDao stationOperationRecordDao;

    @Override
    public HibernateDao<StationOperationRecord, Integer> getEntityDao() {
        return stationOperationRecordDao;
    }

    public StationOperationRecord getRecord(Integer id) {
        return stationOperationRecordDao.getRecord(id);
    }
}
