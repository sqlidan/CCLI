package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApprGnoSequenceDao extends HibernateDao<ApprHead, String> {
	
	public Integer getNextval() {

		String sql = "SELECT SEQ_FLJG_APPR_GNO.NEXTVAL FROM dual";
		SQLQuery sqlQuery=createSQLQuery(sql);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return Integer.parseInt(value.toString());
	}


	
}

