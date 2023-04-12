package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.cost.entity.BisStandingBook;

import net.sf.json.util.JSONUtils;
@Repository
public class HscodeDao  extends HibernateDao<BaseHscode, Integer> {

	public List<Object[]> getHsData(String hs) {
		   StringBuffer sql=new StringBuffer();
	 	   sql.append("SELECT DISTINCT id,code FROM BaseHscode  where   rownum <=10  ");
	 	   if(null!=hs&&!"".equals(hs)){
	 		  sql.append(" and  code like '%"+hs.trim()+"%'");
	 	   }
	 	  sql.append("  order by code   ");
	 	   Query query= createQuery(sql.toString());
	 	   List<Object[]> list=query.list();


	 	   return list;

	
	}
	
}
