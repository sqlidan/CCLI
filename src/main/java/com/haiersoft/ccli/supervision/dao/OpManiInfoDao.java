package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.OpManiInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OpManiInfoDao extends HibernateDao<OpManiInfo, String> {

	public void deleteByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "delete FLJG_OP_MANI_INFO where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}

	public void updateManiFestIdbyHeadId(String maniFestId, String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("maniFestId", maniFestId);
		params.put("headId", headId);
		String sql = "update FLJG_OP_MANI_INFO set MANIFEST_ID = :maniFestId where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
		
	}

    public String sumGrossWtByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "SELECT NVL(SUM(t.GROSS_WT),0) FROM FLJG_OP_MANI_INFO t WHERE t.HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return value.toString();
    }
}
