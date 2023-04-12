package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Repository
public class ReservationOutboundDao extends HibernateDao<PlatformReservationOutbound,String> {

    /**
     *
     * @param maniFestId
     * @param localStatus
     * @param id
     */
    public void updateManiFestIdbyId(String maniFestId, String localStatus, String id) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("maniFestId", maniFestId);
        params.put("id", id);
        params.put("localStatus", localStatus);
        String sql = "update PLATFORM_RESERVATION_OUTBOUND set MANIFEST_ID = :maniFestId, LOCAL_STATUS = :localStatus where ID = :id ";
        SQLQuery sqlQuery=createSQLQuery(sql, params);
        sqlQuery.executeUpdate();
    }

}
