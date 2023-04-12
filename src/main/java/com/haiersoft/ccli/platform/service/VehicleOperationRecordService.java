package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.VehicleOperationRecordDao;
import com.haiersoft.ccli.platform.entity.VehicleOperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class VehicleOperationRecordService extends BaseService<VehicleOperationRecord,Integer> {

    @Autowired
    private VehicleOperationRecordDao vehicleOperationRecordDao;
    @Override
    public HibernateDao<VehicleOperationRecord, Integer> getEntityDao() {
        return vehicleOperationRecordDao;
    }
}
