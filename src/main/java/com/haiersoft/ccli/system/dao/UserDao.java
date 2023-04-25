package com.haiersoft.ccli.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.system.entity.User;


/**
 * 用户DAO
 * @author ty
 * @date 2015年1月13日
 */
@Repository
public class UserDao extends HibernateDao<User, Integer>{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findUserByName(String param) {
		StringBuffer sb=new StringBuffer("select name from users where name like :name and (del_flag is null or del_flag='0') ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("name", "%"+param+"%");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

}
