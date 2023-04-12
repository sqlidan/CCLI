package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BisPlanTransport;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/6.
 */
@Repository
public class PlanTransportDAO extends HibernateDao<BisPlanTransport, Integer> {

    public Page<BisPlanTransport> pagePlans(Page<BisPlanTransport> page, BisPlanTransport obj) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bpt.ID           AS id,\n" +
                "  bpt.PLAN_CODE    AS planCode,\n" +
                "  bpt.CLIENT_NAME  AS clientName,\n" +
                "  bpt.BILL_NUM     AS billNum,\n" +
                "  bpt.CTN_NUM      AS ctnNum,\n" +
                "  bpt.SKU_ID       AS skuId,\n" +
                "  bpt.LOAD_NUM     AS loadNum,\n" +
                "  bpt.QUANTITY     AS quantity,\n" +
                "  bpt.CAR_CODE     AS carCode,\n" +
                "  bpt.CHECK_STATE  AS checkState,\n" +
                "  bpt.CHECK_DATE   AS checkDate,\n" +
                "  bpt.CHECK_USER   AS checkUser,\n" +
                "  bpt.DESCRIPTION  AS description,\n" +
                "  bpt.PLAN_DATE    AS planDate,\n" +
                "  bpt.CREATE_DATE  AS createDate,\n" +
                "  bpt.PLAN_TIME    AS planTime\n" +
                "FROM BIS_PLAN_TRANSPORT bpt WHERE 1 = 1 \n";

        if (isNotNull(obj.getBillNum())) {
            sql += " AND bpt.BILL_NUM = :billNum \n";
            params.put("billNum", obj.getBillNum());
        }

        if (isNotNull(obj.getCtnNum())) {
            sql += " AND bpt.CTN_NUM = :ctnNum \n";
            params.put("ctnNum", obj.getCtnNum());
        }

        if (isNotNull(obj.getClient())) {
            sql += " AND bpt.CLIENT = :client \n";
            params.put("client", obj.getClient());
        }

        if (isNotNull(obj.getCarCode())) {
            sql += " AND bpt.CAR_CODE = :carCode \n";
            params.put("carCode", obj.getCarCode());
        }

        if (isNotNull(obj.getPlanTime())) {
            sql += " AND bpt.PLAN_TIME = :planTime \n";
            params.put("planTime", obj.getPlanTime());
        }

        sql += " ORDER BY bpt.ID DESC";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("planCode", String.class);
        paramType.put("clientName", String.class);
        paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("skuId", String.class);
        paramType.put("loadNum", String.class);
        paramType.put("quantity", String.class);
        paramType.put("carCode", String.class);
        paramType.put("checkState", String.class);
        paramType.put("checkDate", Date.class);
        paramType.put("checkUser", String.class);
        paramType.put("description", String.class);
        paramType.put("planDate", Date.class);
        paramType.put("planTime", String.class);
        paramType.put("createDate", Date.class);

        return findPageSql(page, sql, paramType, params);
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> listPlanTime(String q) {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT\n" +
                "  bpt.PLAN_DATE                                                                        AS planTime,\n" +
                "  concat(SUBSTR(bpt.PLAN_DATE, 0, 2), concat(':', SUBSTR(bpt.PLAN_DATE, 3, 4))) AS planTimeText\n" +
                "FROM BASE_PLAN_TIME bpt\n" +
                "WHERE bpt.STATE = 0 ";

        if (isNotNull(q)) {
            sql += " AND bpt.PLAN_DATE LIKE '%" + q + "%' ";
        }

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

}
