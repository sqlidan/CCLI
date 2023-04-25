package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BasePlanTime;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlanTimeDAO extends HibernateDao<BasePlanTime, Integer> {

    public Page<BasePlanTime> pageTime(Page<BasePlanTime> page, BasePlanTime obj) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bpt.ID          AS id,\n" +
                "  bpt.PLAN_DATE   AS planDate,\n" +
                "  bpt.LARGE_NO    AS largeNo,\n" +
                "  bpt.DESCRIPTION AS description,\n" +
                "  bpt.STATE       AS state\n" +
                "FROM BASE_PLAN_TIME bpt\n" +
                "WHERE bpt.STATE = 0";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("planDate", String.class);
        paramType.put("largeNo", Integer.class);
        paramType.put("description", String.class);
        paramType.put("state", Integer.class);

        return findPageSql(page, sql, paramType, params);
    }

    /**
     * 查询第二天预约车次的数据，预约时间、最大车次、以约车次
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> listTime() {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT\n" +
                "  id,\n" +
                "  planDate,\n" +
                "  largeNo,\n" +
                "  sum(total) AS total\n" +
                "FROM (SELECT\n" +
                "        b.ID               AS id,\n" +
                "        b.PLAN_DATE        AS planDate,\n" +
                "        b.LARGE_NO         AS largeNo,\n" +
                "        count(a.PLAN_TYPE) AS total\n" +
                "      FROM BIS_PLAN_TRANSPORT a RIGHT JOIN BASE_PLAN_TIME b ON a.PLAN_TYPE = b.ID\n" +
                "      WHERE b.STATE = 0 AND a.PLAN_DATE = (SELECT to_date(to_char(sysdate + 1, 'yyyy-MM-dd'), 'yyyy-MM-dd')\n" +
                "                                           FROM dual)\n" +
                "      GROUP BY b.ID, b.PLAN_DATE, b.LARGE_NO\n" +
                "      UNION ALL\n" +
                "      SELECT\n" +
                "        b.ID        AS id,\n" +
                "        b.PLAN_DATE AS planDate,\n" +
                "        b.LARGE_NO  AS largeNo,\n" +
                "        0           AS total\n" +
                "      FROM BIS_PLAN_TRANSPORT a RIGHT JOIN BASE_PLAN_TIME b ON a.PLAN_TYPE = b.ID\n" +
                "      WHERE b.STATE = 0\n" +
                "      GROUP BY b.ID, b.PLAN_DATE, b.LARGE_NO)\n" +
                "GROUP BY id,planDate, largeNo\n" +
                "ORDER BY planDate";

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
