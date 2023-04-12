package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisGateCarWrongInfo;

@Repository
public class GateCarWrongInfoDao extends HibernateDao<BisGateCarWrongInfo, Integer> {


    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findWrongCarInfoList() {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bgcwi.CAR_NUM AS carNum,\n" +
                "  bgcwi.CTN_NUM AS ctnNum,\n" +
                "  bgcwi.REASON AS reason\n" +
                "FROM BIS_GATE_CAR_WRONG_INFO bgcwi";

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

    @SuppressWarnings("unchecked")
	public String findWrongCar(String carNum, String driverName) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT bgcwi.CTN_NUM AS ctnNum,\n" +
        		" bgcwi.CTN_NUM AS ctnNum\n" +
                "FROM BIS_GATE_CAR_WRONG_INFO bgcwi\n" +
                "WHERE CAR_NUM = :carNum ";
        params.put("carNum", carNum);

//        if (isNotNull(driverName)) {
//            sql += " AND DRIVER_NAME = :driverName ";
//            params.put("driverName", driverName);
//        }

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        List<Map<String, Object>> result = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        if (result.size() == 1) {
            return result.get(0).get("ctnNum").toString();
        }

        return null;

    }



}
