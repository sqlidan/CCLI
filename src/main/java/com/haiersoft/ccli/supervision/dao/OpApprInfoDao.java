package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.entity.OpApprInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OpApprInfoDao extends HibernateDao<OpApprInfo, String> {

    public Integer getApprGnoByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "SELECT count(*) FROM FLJG_OP_APPR_INFO t WHERE t.HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return Integer.parseInt(value.toString())+1;
    }

	public String sumQtyByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "SELECT NVL(SUM(t.G_QTY),0） FROM FLJG_OP_APPR_INFO t WHERE t.HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return value.toString();
	}

	public String sumWeightByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "SELECT NVL(SUM(t.GROSS_WT),0） FROM FLJG_OP_APPR_INFO t WHERE t.HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return value.toString();
	}

	public List<OpApprInfo> listByHeadId(String headId) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		List<OpApprInfo> list = findBy(params);
		return list;
	}

    public void deleteByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "delete FLJG_OP_APPR_INFO where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
    }
}
