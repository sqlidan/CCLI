package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class ApprGno2SequenceDao extends HibernateDao<ApprHead, String> {
	
	public Integer getNextval() {

		String sql = "SELECT SEQ_FLJG_APPR_GNO2.NEXTVAL FROM dual";
		SQLQuery sqlQuery=createSQLQuery(sql);
		BigDecimal value = (BigDecimal)sqlQuery.uniqueResult();
		return Integer.parseInt(value.toString());
	}


	
}

