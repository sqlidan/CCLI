package com.haiersoft.ccli.base.dao;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.base.entity.BisAsnWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
@Repository
public class BisAsnWorkerDao extends HibernateDao<BisAsnWorker, String>{

	public Page<BisAsnWorker> getAllAsnWorker(Page<BisAsnWorker> page,
			BisAsnWorker bisAsnWorker,String asn) {
		
		 String sql = "select " +
	                "t.ID as id," +
	                "t.LINK_ID as linkID," +
	                "t.WORKER_TYPEID as workerTypeId," +
	                "t.WORKER_YPENAME as workerTypeName," +
	                "t.WORKER_NAME as workerName," +
	                "b.RATIO as ratio," +
	                "t.PIECE as piece," +
	                "t.NET_WEIGHT as netWeight," +
	                "t.GROSS_WEIGHT as grossWeight" +
	               
	                " from BIS_ASN_WORKER t left join BASE_PERSONTYPE_RULE b " +
	                " on t.WORKER_TYPEID = b.PERSON_TYPEID "+
	                " where 1 = 1 ";
		 	if(asn!=null && !("".equals(asn))){
		 		sql += "and t.LINK_ID = '"+asn+ "'";
		 	}

	        Map<String, Object> queryParams = new HashMap<>();
	        sql+=" order by t.LINK_ID desc";
	        Map<String, Object> paramType = new HashMap<>();
	        paramType.put("id", String.class);
	        paramType.put("linkID", String.class);
	        paramType.put("workerTypeId", Integer.class);
	        paramType.put("workerTypeName", String.class);
	        paramType.put("workerName", String.class);
	        paramType.put("ratio", Double.class);
	        paramType.put("piece", Integer.class);
	        paramType.put("netWeight", Double.class);
	        paramType.put("grossWeight", Double.class);
	        return findPageSql(page, sql, paramType, queryParams);
	    
	}
	
}
