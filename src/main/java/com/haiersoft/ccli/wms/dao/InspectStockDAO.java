package com.haiersoft.ccli.wms.dao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.wms.entity.InspectStock;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class InspectStockDAO extends HibernateDao<InspectStock, String> {

    public Page<InspectStock> findInspectPlans(Page<InspectStock> page, InspectStock obj) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  t.INSPECT_ID        AS inspectId,\n" +
                "  t.BILL_NUM          AS billNum,\n" +
                "  t.CTN_NUM           AS ctnNum,\n" +
                "  t.SKU_ID            AS skuId,\n" +
                "  t.STOCK_NAME        AS stockName,\n" +
                "  t.TRAY_ID           AS trayId,\n" +
                "  t.WAREHOUSE         AS warehouse,\n" +
                "  t.CARGO_NAME        AS cargoName,\n" +
                "  t.CONTACT_NUM       AS contactNum,\n" +
                "  t.ASN               AS asn,\n" +
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
                "FROM BIS_INSPECT_STOCK t\n" +
                "WHERE 1 = 1 AND t.STATE = 0 ";

        if (isNotNull(obj.getInspectId())) {
            sql += " AND t.INSPECT_ID = :inspectId ";
            params.put("inspectId", obj.getInspectId());
        }

        if (isNotNull(obj.getBillNum())) {
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
        }

        if (isNotNull(obj.getStockName())) {
            sql += " AND t.STOCK_NAME = :stockName ";
            params.put("stockName", obj.getStockName());
        }

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("inspectId", String.class);
        paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("skuId", String.class);
        paramType.put("stockName", String.class);
        paramType.put("trayId", String.class);
        paramType.put("warehouse", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("contactNum", String.class);
        paramType.put("asn", String.class);
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

    public Page<InspectStock> findInspectPlansInfo(Page<InspectStock> page, InspectStock obj) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  t.INSPECT_ID        AS inspectId,\n" +
                "  t.BILL_NUM          AS billNum,\n" +
                "  t.CTN_NUM           AS ctnNum,\n" +
                "  t.SKU_ID            AS skuId,\n" +
                "  t.STOCK_NAME        AS stockName,\n" +
                "  t.TRAY_ID           AS trayId,\n" +
                "  t.WAREHOUSE         AS warehouse,\n" +
                "  t.CARGO_NAME        AS cargoName,\n" +
                "  t.CONTACT_NUM       AS contactNum,\n" +
                "  t.ASN               AS asn,\n" +
                "  t.INSPECT_TOTAL     AS inspectTotal,\n" +
                "  t.OPERATE_STATE     AS operateState,\n" +
                "  t.OPERATE_DATE      AS operateDate,\n" +
                "  t.OPERATE_USER_NAME AS operateUserName,\n" +
                "  t.OPERATE_USER_ID   AS operateUserId,\n" +
                "  t.CREATE_DATE       AS createDate,\n" +
                "  t.CREATE_USER_NAME  AS createUserName,\n" +
                "  t.CREATE_USER_ID    AS createUserId,\n" +
                "  t.DESCRIPTION       AS description,\n" +
                "  t.SAMPLE_DATE       AS sampleDate,\n" +
                "  t.INSPECT_MASTER_ID AS inspectMasterId,\n" +
                "  t.CARGO_LOCATION AS cargoLocation,\n" +
                "  t.SAMPLE_UNIT AS sampleUnit\n" +
                "FROM BIS_INSPECT_STOCK t\n" +
                "WHERE 1 = 1  AND t.STATE <> 2";//AND t.STATE = 0  状态0未保存，1已保存 2已删除

        if (isNotNull(obj.getInspectId())) {
            sql += " AND t.INSPECT_ID = :inspectId ";
            params.put("inspectId", obj.getInspectId());
        }

        if (isNotNull(obj.getBillNum())) {
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
        }

        if (isNotNull(obj.getStockName())) {
            sql += " AND t.STOCK_NAME = :stockName ";
            params.put("stockName", obj.getStockName());
        }
        if (isNotNull(obj.getInspectMasterId())) {
            sql += " AND t.INSPECT_MASTER_ID = :inspectMasterId ";
            params.put("inspectMasterId", obj.getInspectMasterId());
        }
        
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("inspectId", String.class);
        paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("skuId", String.class);
        paramType.put("stockName", String.class);
        paramType.put("trayId", String.class);
        paramType.put("warehouse", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("contactNum", String.class);
        paramType.put("asn", String.class);
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
        paramType.put("inspectMasterId", String.class);
        paramType.put("cargoLocation", String.class);
        paramType.put("sampleUnit", String.class);
        return findPageSql(page, sql, paramType, params);
    }
    
    public String auditInspectPlans(User user, String inspectId) {
        return "";
    }

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findHebingBy(Map<String, Object> params) {
		 StringBuffer sb = new StringBuffer(" select listagg(sum(t.inspect_total),',') WITHIN GROUP (order by t.sample_unit )  aa from bis_inspect_stock t ");

		// StringBuffer sb = new StringBuffer(" select wmsys.wm_concat(sum(t.inspect_total)||t.sample_unit) aa from bis_inspect_stock t ");
		 	sb.append(" where 1=1 ");
	        if (params.keySet().size()>0) {
	            sb.append(" and t.sku_id = :skuId ");
	        }
	        sb.append(" group by sample_unit");
	        
	        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
	        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


}
