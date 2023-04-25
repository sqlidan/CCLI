package com.haiersoft.ccli.platform.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.PlatformReservationInboundDao;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class ReservationInboundService extends BaseService<PlatformReservationInbound,String> {
   @Autowired
   private PlatformReservationInboundDao dao;
    @Override
    public HibernateDao<PlatformReservationInbound, String> getEntityDao() {
        return dao;
    }


    public int updateByYyid(String yyid, String actualVehicleNo) {
       return dao.updateByYyid(yyid,actualVehicleNo);
    }

    public int updateQueueingTimeByYyid(String yyid) {
        return dao.updateQueueingTimeByYyid(yyid);
    }
}
