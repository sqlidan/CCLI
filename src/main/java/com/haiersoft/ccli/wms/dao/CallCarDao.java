package com.haiersoft.ccli.wms.dao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.CallCar;
/**
 * 
 * @ClassName: CallCarDao
 */
@Repository
public class CallCarDao extends HibernateDao<CallCar, String> {

	public void deleteZero() {
		  StringBuffer sb = new StringBuffer("delete from bis_call_car where time=0 ");
	      SQLQuery sqlQuery = createSQLQuery(sb.toString());
	      sqlQuery.executeUpdate();
	}
	
}
