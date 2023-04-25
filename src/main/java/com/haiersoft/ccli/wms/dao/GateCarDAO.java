package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/21.
 */
@Repository
public class GateCarDAO extends HibernateDao<BisGateCar, Integer> {

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findInCarList() {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bgc.CAR_NUM     AS carNum,\n" +
                "  bgc.DRIVER_NAME AS driverName\n" +
                "FROM BIS_GATE_CAR bgc";

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

    @SuppressWarnings("unchecked")
	public String findCar(String carNum, String driverName) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT bgc.CTN_NUM AS ctnNum\n" +
                "FROM BIS_GATE_CAR bgc\n" +
                "WHERE CAR_NUM = :carNum ";
        params.put("carNum", carNum);

        if (isNotNull(driverName)) {
            sql += " AND DRIVER_NAME = :driverName ";
            params.put("driverName", driverName);
        }

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        List<Map<String, Object>> result = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        if (result.size() == 1) {
            return result.get(0).get("ctnNum").toString();
        }

        return null;

    }


}
