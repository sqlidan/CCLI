package com.haiersoft.ccli.base.dao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.HibernateDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlatformDao  extends HibernateDao<BasePlatform, Integer> {


    public List<Map<String, Object>> getActivePlatform() {
        StringBuffer sb= new StringBuffer();
        sb.append(" select * from BASE_PLATFORM where 1=1 ");
        sb.append(" and platform_no is not null ");
        sb.append(" and deleted_flag = '0' ");
        sb.append(" order by PLATFORM_NO ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<BasePlatform> getActivePlatforms(Page<BasePlatform> page, BasePlatform basePlatform) {
        StringBuffer sb= new StringBuffer();
        sb.append(" select {b.*} from BASE_PLATFORM where 1=1 ");
        sb.append(" and platform_no is not null ");
        sb.append(" and deleted_flag = '0' ");
        Map<String, Object> paramType = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        sqlQuery.addEntity("b",BasePlatform.class);
        return sqlQuery.list();
    }
}
