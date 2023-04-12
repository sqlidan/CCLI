package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.VehicleCheckDao;
import com.haiersoft.ccli.platform.entity.VehicleCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author A0047794
 * @日期 2021/11/30
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class VehicleCheckService extends BaseService<VehicleCheck,Integer> {

    @Autowired
    private VehicleCheckDao vehicleCheckDao;

    @Override
    public HibernateDao<VehicleCheck, Integer> getEntityDao() {
        return vehicleCheckDao;
    }

    public int updateStatus(String id, String actualVehicleNo) {
       return vehicleCheckDao.updateStatus(id,actualVehicleNo);
    }

    public VehicleCheck getbyId(String id) {
        return vehicleCheckDao.findUniqueBy("id",id);
    }
}
