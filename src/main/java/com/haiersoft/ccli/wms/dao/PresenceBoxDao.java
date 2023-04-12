package com.haiersoft.ccli.wms.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;
/**
 * 
 * @author PYL
 * @ClassName: PresenceBoxDao
 * @Description: 在场箱管理DAO
 */
@Repository
public class PresenceBoxDao extends HibernateDao<BisPresenceBox, Integer> {

	public void updateCd(String ctnNum, Date now, String type) {
		  Map<String,Object> params = new HashMap<String,Object>();
		  params.put("ctnNum", ctnNum);
		  if(!type.equals("3")){
			  params.put("type", type);
		  }
		  StringBuffer sb = new StringBuffer("update bis_presence_box set ");
		  if(type.equals("1")){
			  sb.append("START_CD_TIME=:now ");
			  sb.append(",CD_STATE=:type ");
			  params.put("now", now);
		  }else if(type.equals("2")){
			  sb.append("END_CD_TIME=:now ");
			  sb.append(",CD_STATE=:type ");
			  params.put("now", now);
		  }else{
			  sb.append("IF_CD='1' ");
		  }
		  sb.append("where CTN_NUM=:ctnNum");
	      SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
	      sqlQuery.executeUpdate();
	}



}
