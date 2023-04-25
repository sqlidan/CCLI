package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Repository
public class PlatformReservationInboundDao extends HibernateDao<PlatformReservationInbound,String> {


    public Page<PlatformReservationInbound> selectList(Page<PlatformReservationInbound> page) {
        StringBuffer sb =  new StringBuffer();
        sb.append("select t.* from PLATFORM_RESERVATION_INBOUND t where 1=1");
        sb.append("order by created_time desc");
        Map<String, Object> queryParams = new HashMap<>();

        return findPageSql(page,sb.toString(), queryParams);
    }

    public int updateByYyid(String yyid, String actualVehicleNo) {
        StringBuffer sb= new StringBuffer();
        sb.append(" update PLATFORM_RESERVATION_INBOUND set CAR_NUMBER = :actualVehicleNo,UPDATED_TIME =:updatedTime where 1=1 ");
        sb.append(" and yyid = :yyid");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("actualVehicleNo",actualVehicleNo);
        queryParams.put("yyid",yyid);
        queryParams.put("updatedTime",new Date());

        SQLQuery sqlQuery = createSQLQuery(sb.toString(),queryParams);
        return sqlQuery.executeUpdate();
    }
    public int updateQueueingTimeByYyid(String yyid) {
        StringBuffer sb= new StringBuffer();
        sb.append(" update PLATFORM_RESERVATION_INBOUND set QUEUING_TIME = :updatedTime,UPDATED_TIME =:updatedTime where 1=1 ");
        sb.append(" and yyid = :yyid");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("yyid",yyid);
        queryParams.put("updatedTime",new Date());

        SQLQuery sqlQuery = createSQLQuery(sb.toString(),queryParams);
        return sqlQuery.executeUpdate();
    }
}
