package com.haiersoft.ccli.wms.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.entity.InspectStockMasterline;
@Repository
public class InspectStockMasterLineDao extends HibernateDao<InspectStockMasterline, String>{

	public Page<InspectStockMasterline> findInspectPlans(
			Page<InspectStockMasterline> page, InspectStockMasterline obj) {
		Map<String, Object> params = new HashMap<>();
		String sql = "SELECT\n" +
                "  t.INSPECT_ID        AS inspectId,\n" +
                
                "  t.STOCK_NAME        AS stockName,\n" +
                
                "  t.INSPECT_TOTAL     AS inspectTotal,\n" +
                "  t.OPERATE_STATE     AS operateState,\n" +
                "  t.OPERATE_DATE      AS operateDate,\n" +
                "  t.OPERATE_USER_NAME AS operateUserName,\n" +
                "  t.OPERATE_USER_ID   AS operateUserId,\n" +
                "  t.CREATE_DATE       AS createDate,\n" +
                "  t.CREATE_USER_NAME  AS createUserName,\n" +
                "  t.CREATE_USER_ID    AS createUserId,\n" +
                "  t.DESCRIPTION       AS description,\n" +
                "  t.SAMPLE_DATE       AS sampleDate\n" +
                "FROM BIS_INSPECT_STOCK_MASTERLINE t\n" +
                "WHERE 1 = 1 AND t.STATE <> 2 ";//AND t.STATE = 0

        if (isNotNull(obj.getInspectId())) {
            sql += " AND t.INSPECT_ID = :inspectId ";
            params.put("inspectId", obj.getInspectId());
        }

       /* if (isNotNull(obj.getBillNum())) {
            sql += " AND t.BILL_NUM = :billNum ";
            params.put("billNum", obj.getBillNum());
        }

        if (isNotNull(obj.getCtnNum())) {
            sql += " AND t.CTN_NUM = :ctnNum ";
            params.put("ctnNum", obj.getCtnNum());
        }

        if (isNotNull(obj.getSkuId())) {
            sql += " AND t.SKU_ID = :skuId ";
            params.put("skuId", obj.getSkuId());
        }*/

        if (isNotNull(obj.getStockName())) {
            sql += " AND t.STOCK_NAME = :stockName ";
            params.put("stockName", obj.getStockName());
        }
        
        sql += " order by t.INSPECT_ID desc  ";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("inspectId", String.class);
        /*paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("skuId", String.class);*/
        paramType.put("stockName", String.class);
       /* paramType.put("trayId", String.class);
        paramType.put("warehouse", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("contactNum", String.class);
        paramType.put("asn", String.class);*/
        paramType.put("inspectTotal", Integer.class);
        paramType.put("operateState", Integer.class);
        paramType.put("operateDate", Date.class);
        paramType.put("operateUserName", String.class);
        paramType.put("operateUserId", String.class);
        paramType.put("createDate", Date.class);
        paramType.put("createUserName", String.class);
        paramType.put("createUserId", String.class);
        paramType.put("description", String.class);
        paramType.put("sampleDate", Date.class);

        return findPageSql(page, sql, paramType, params);
		
		
	}

	public String findInspectPlans(User user, String inspectId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
