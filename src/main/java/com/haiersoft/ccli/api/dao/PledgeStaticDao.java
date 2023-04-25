package com.haiersoft.ccli.api.dao;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
@Repository
public class PledgeStaticDao  extends HibernateDao<ApiPledge, Integer> {

	//查询
			public ApiPledge Query(Integer id) {
				ApiPledge apiPledge=new ApiPledge();
				StringBuffer buffer =new StringBuffer();
				buffer.append("SELECT ");
				buffer.append("ID, ");
				buffer.append("ACCOUNT_ID, ");
				buffer.append("TRAYINFO_ID, ");
				buffer.append("PLEDGE_NUMBER, ");
				buffer.append("PLEDGE_WEIGHT, ");
				buffer.append("STATE, ");
				buffer.append("CREATE_DATE, ");
				buffer.append("CUSTOMER_NUMBER, ");
				buffer.append("ITEM_CLASS, ");
				buffer.append("STATUS, ");
				buffer.append("COMFIRM_DATE,");
				buffer.append("TREND_ID, ");
				buffer.append("RELATED_TREND_ID ");
				buffer.append("FROM ");
				buffer.append("API_PLEDGE bis ");
				buffer.append("WHERE ");
				buffer.append("bis.id = "+id);
				SQLQuery sqlQuery = this.getSession().createSQLQuery(buffer.toString()).addEntity(ApiPledge.class);
				apiPledge=(ApiPledge) sqlQuery.uniqueResult();
				
				
				return apiPledge;  
			}

			 public  BaseClientInfo QueryClientInfo(Integer clientId) {
		    	 BaseClientInfo  baseClientInfo=new  BaseClientInfo();
		    	 StringBuffer sql =new StringBuffer();
		    	 sql.append("SELECT * FROM BASE_CLIENT_INFO WHERE ");
		    	 sql.append("IDS = '" + clientId + "'");
		    	 SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString()).addEntity(BaseClientInfo.class);
		    	 baseClientInfo=(BaseClientInfo) sqlQuery.uniqueResult();
		    	 return baseClientInfo;
		     }
}
