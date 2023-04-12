package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Repository
public class VehicleQueueDao extends HibernateDao<VehicleQueue,String> {
    public VehicleQueue getMinQueueTime() {

        String sql2="select {pq.*} from PLATFORM_QUEUE pq WHERE pq.STATUS_FLAG ='0' and rownum= 1 order by queue_time asc ";
        SQLQuery sqlQuery = createSQLQuery(sql2);
        sqlQuery.addEntity("pq",VehicleQueue.class);
        return (VehicleQueue) sqlQuery.uniqueResult();
    }

    public List<VehicleQueue> getTodayQueue() {

        String hql = "  from VehicleQueue where 1=1 and to_char(QUEUE_TIME,'yyyy-MM-dd') = '" + DateUtils.formatDate(new Date(), "yyyy-MM-dd")+"' order by queue_time desc";
        Query query = createQuery(hql);
      return query.list();

    }
}
