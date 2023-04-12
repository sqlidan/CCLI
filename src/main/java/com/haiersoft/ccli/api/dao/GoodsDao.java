package com.haiersoft.ccli.api.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class GoodsDao  extends HibernateDao<BaseProductClass, Integer> {


	//查询
	public BaseProductClass Query(Integer id) {
		BaseProductClass baseProductClass=new BaseProductClass();
		StringBuffer buffer =new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("ID, ");
		buffer.append("PNAME, ");
		buffer.append("PRINTID ");
		buffer.append("FROM ");
		buffer.append("BASE_PRODUCT_CALSS bis ");
		buffer.append("WHERE ");
		buffer.append("bis.id = '"+id+"' ");
		SQLQuery sqlQuery = this.getSession().createSQLQuery(buffer.toString()).addEntity(BaseProductClass.class);
		baseProductClass=(BaseProductClass) sqlQuery.uniqueResult();
		
		
		return baseProductClass;  
	}
	
	
	
}
