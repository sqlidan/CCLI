package com.haiersoft.ccli.supervision.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.ManiInfo;

@Repository
public class ManiInfoDao extends HibernateDao<ManiInfo, String> {

	public void deleteByHeadId(String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headId", headId);
		String sql = "delete FLJG_MANI_INFO where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
	}

	public void updateManiFestIdbyHeadId(String maniFestId, String headId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("maniFestId", maniFestId);
		params.put("headId", headId);
		String sql = "update FLJG_MANI_INFO set MANIFEST_ID = :maniFestId where HEAD_ID = :headId ";
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		sqlQuery.executeUpdate();
		
	}

}
