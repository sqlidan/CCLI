package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.platform.entity.VehicleCheck;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/30
 * @描述
 */
@Repository
public class VehicleCheckDao extends HibernateDao<VehicleCheck,Integer> {

    public int updateStatus(String id, String actualVehicleNo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("update PLATFORM_VEHICLE_CHECK p set p.CHECK_STATUS = '1', p.CHECK_TIME = :checkTime ");




        Map<String, Object> queryParams = new HashMap<>();
        if (StringUtils.isNotEmpty(actualVehicleNo)){
            stringBuffer.append(" ,ACTUAL_VEHICLE_NO = :actualVehicleNo ");
            //  stringBuffer.append(" and p.CHECK_TIME= :checkTime ");
            queryParams.put("actualVehicleNo",actualVehicleNo);
        }
        stringBuffer.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(id)){
            stringBuffer.append(" and p.id = :id ");
          //  stringBuffer.append(" and p.CHECK_TIME= :checkTime ");
            queryParams.put("id",id);
        }
        queryParams.put("checkTime",new Date());
        SQLQuery sqlQuery=createSQLQuery(stringBuffer.toString(),queryParams);
       return sqlQuery.executeUpdate();
    }
}
