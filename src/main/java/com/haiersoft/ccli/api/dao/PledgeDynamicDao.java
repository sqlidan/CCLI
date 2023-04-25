package com.haiersoft.ccli.api.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.TrayInfo;
@Repository
public class PledgeDynamicDao  extends HibernateDao<ApiPledge, Integer> {

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
		

}
