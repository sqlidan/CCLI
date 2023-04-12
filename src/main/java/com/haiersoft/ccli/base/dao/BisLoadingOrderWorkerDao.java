package com.haiersoft.ccli.base.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;




import com.haiersoft.ccli.base.entity.BisLoadingOrderWorker;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;

@Repository
public class BisLoadingOrderWorkerDao extends HibernateDao<BisLoadingOrderWorker, String>{

	public Page<BisLoadingOrderWorker> getAllLoadingOrderWorker(
			Page<BisLoadingOrderWorker> page, BisLoadingOrderWorker blow,
			String loadingPlanNum) {
		String sql = "select " +
                "t.ID as id," +
                "t.LINK_ID as linkId," +
                "t.WORKER_TYPEID as workerTypeId," +
                "t.WORKER_YPENAME as workerTypeName," +
                "t.WORKER_NAME as workerName," +
                "b.RATIO as ratio," +
                "t.PIECE as piece," +
                "t.NET_WEIGHT as netWeight," +
                "t.GROSS_WEIGHT as grossWeight" +
               
                " from BIS_LOADING_ORDER_WORKER t left join BASE_PERSONTYPE_RULE b " +
                " on t.WORKER_TYPEID = b.PERSON_TYPEID "+
                " where 1 = 1 ";
	 	if(loadingPlanNum!=null && !("".equals(loadingPlanNum))){
	 		sql += "and t.LINK_ID = '"+loadingPlanNum+ "'";
	 	}

        Map<String, Object> queryParams = new HashMap<>();

        
        sql+=" order by t.LINK_ID desc";
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", String.class);
        paramType.put("linkId", String.class);
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
