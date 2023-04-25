package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.platform.entity.StationOperationRecord;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Repository
public class StationOperationRecordDao extends HibernateDao<StationOperationRecord,Integer> {
    public StationOperationRecord getRecord(Integer id) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" select {p.*} from PLATFORM_OPERATION_LOG p where 1=1 ");
        stringBuffer.append(" and p.PLATFORM_ID = "+id);
        stringBuffer.append(" and p.START_TIME is not null ");
        stringBuffer.append(" and p.LEAVE_TIME is null ");
        SQLQuery sqlQuery = createSQLQuery(stringBuffer.toString());
        sqlQuery.addEntity("p",StationOperationRecord.class);
        return (StationOperationRecord) sqlQuery.uniqueResult();
    }
}
