package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BasePlatformDao extends HibernateDao<BasePlatform, Integer> {


    public List<Map<String, Object>> getActivePlatform() {
        StringBuffer sb= new StringBuffer();
        sb.append(" select * from BASE_PLATFORM where 1=1 ");
        sb.append(" and platform_no is not null ");
        sb.append(" and deleted_flag = '0' ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<BasePlatform> getActivePlatforms(Page<BasePlatform> page, BasePlatform basePlatform) {
        StringBuffer sb= new StringBuffer();
        sb.append(" select {b.*} from BASE_PLATFORM b where 1=1 ");
        sb.append(" and b.platform_no is not null ");
        sb.append(" and b.deleted_flag = '0' ");
        Map<String, Object> paramType = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        sqlQuery.addEntity("b",BasePlatform.class);
        return sqlQuery.list();
    }

    public BasePlatform findByPlatformNo(Integer platformId) {
        StringBuffer sb= new StringBuffer();
        sb.append(" select {b.*} from BASE_PLATFORM b where 1=1 ");
        sb.append(" and b.platform_no = "+platformId);
        sb.append(" and b.deleted_flag = '0' ");

        SQLQuery sqlQuery =createSQLQuery(sb.toString());
        sqlQuery.addEntity("b",BasePlatform.class);
        return (BasePlatform) sqlQuery.uniqueResult();
    }



    public List<Map<String, Object>> getCurrentInfo() {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();

        sb.append("SELECT bp.PLATFORM AS platform ,bp.PLATFORM_NO AS platformNo ,bp.TRAY_ROOM_NUM AS warehouseNo,bp.PLATFORM_STATUS AS platformStatus,");
        sb.append("info.queue_status AS queueStatus,info.operation_flag AS operationFlag,info.car_number AS carNumber,");
        sb.append("info.bill_no AS billNo ,info.CONTAINER_NO AS ctnNum,info.yyid AS yyid ");
        sb.append("FROM BASE_PLATFORM bp ");
        sb.append("LEFT JOIN PLATFORM_CURRENT_INFO info ON bp.ID = info.platform_id ");
        sb.append("WHERE bp.deleted_FLAG = 0  ");
        sb.append("ORDER BY bp.id ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
