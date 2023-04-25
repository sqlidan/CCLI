package com.haiersoft.ccli.remoting.platform.dao;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class StockWebDAO extends HibernateDao<BaseClientInfo, Integer> {

    @SuppressWarnings("unchecked")
	public String hasAuthWithStock(String clientName) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientName", clientName);

        String sql = "SELECT LIMIT FROM BASE_CLIENT_INFO WHERE CLIENT_NAME = :clientName";

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        if (list == null || list.size() != 1) return "1";

        Map<String, Object> result = list.get(0);

        return result.get("LIMIT") == null ? "1" : result.get("LIMIT").toString();

    }

}
