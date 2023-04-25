package com.haiersoft.ccli.api.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class ExchangeDao  extends HibernateDao<ExchangeInfo, String> {

	//查询
			public ExchangeInfo Query(String id) {
				ExchangeInfo exchangeInfo=new ExchangeInfo();
				StringBuffer buffer =new StringBuffer();
				buffer.append("SELECT ");
				buffer.append("ID, ");
				buffer.append("ACCOUNT_ID, ");
				buffer.append("NEW_PLEDGE_ID, ");
				buffer.append("PLEDGE_NUMBER, ");
				buffer.append("PLEDGE_WEIGHT, ");
				buffer.append("PLEDGE_STATUS, ");
				buffer.append("ORIGINAL_PLEDGE_ID, ");
				buffer.append("RELIEVE_NUMBER, ");
				buffer.append("RELIEVE_WEIGHT, ");
				buffer.append("RELIEVE_STATUS, ");
				buffer.append("CREATE_TIME, ");
				buffer.append("STATUS, ");
				buffer.append("SOURCE_TREND_ID, ");
				buffer.append("RELATED_TREND_ID, ");
				buffer.append("TREND_ID ");
				buffer.append("FROM ");
				buffer.append("API_EXCHANGE_INFO bis ");
				buffer.append("WHERE ");
				buffer.append("bis.id = '"+id+"' ");
				SQLQuery sqlQuery = this.getSession().createSQLQuery(buffer.toString()).addEntity(ExchangeInfo.class);
				exchangeInfo=(ExchangeInfo) sqlQuery.uniqueResult();
				
				
				return exchangeInfo;  
			}
	
}
