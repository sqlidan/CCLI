package com.haiersoft.ccli.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class ClientDao  extends HibernateDao<BaseClientInfo, Integer> {

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findClient(String param, String clientSort) {
		
		StringBuffer sb=new StringBuffer("select ids,client_name from base_client_info where lower(client_name) like lower(:clientName) and del_flag='0' ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("clientName", "%"+param+"%");
		
		if(!"".equals(clientSort) && null != clientSort){
			sb.append(" and client_sort = :clientSort");
			parme.put("clientSort", clientSort);
		}
		
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findZX() {
		StringBuffer sb=new StringBuffer("select ids,client_name from base_client_info where client_sort='2' and del_flag='0' ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}
