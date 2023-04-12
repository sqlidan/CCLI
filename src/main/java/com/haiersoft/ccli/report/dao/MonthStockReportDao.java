package com.haiersoft.ccli.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;

@Repository
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class MonthStockReportDao extends HibernateDao<Object, String>{

	public Page<Object> getPage(Page<Object> page, String month,String names) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("select * from test ");
        sb.append("where  1=1 ");
       
        if (month != null && !"".equals(month)) {
        	sb.append("and  months = '"+month+"' ");
        	
        }
        if(names != null && !"".equals(names)) {
        	sb.append(" and names = '"+names+"'");
        }
        
        
        sb.append(" ORDER BY MONTHS desc ");
        //SQLQuery sqlQuery = createSQLQuery(sb.toString());
        
		return findPageSql(page, sb.toString(),parme);
	}

	public List<Map<String, Object>>  getMonths() {
        StringBuffer sb = new StringBuffer();      
        sb.append("SELECT DISTINCT(MONTHS) FROM  TEST ORDER BY MONTHS desc ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
}
