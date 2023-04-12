package com.haiersoft.ccli.base.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.BaseClientRank;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class ClientRankDao  extends HibernateDao<BaseClientRank, String> {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRank(String param) {
			StringBuffer sb=new StringBuffer("select rank,nick_name from base_client_rank where lower(rank) like lower(:rank)");
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("rank", "%"+param+"%");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}
