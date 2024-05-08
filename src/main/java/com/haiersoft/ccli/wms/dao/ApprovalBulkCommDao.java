package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.AuditRecord;
import com.haiersoft.ccli.wms.entity.BisLuodiInfo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApprovalBulkCommDao extends HibernateDao<AuditRecord, Integer> {

    public Page<AuditRecord> pageList(Page<AuditRecord> page, AuditRecord entity) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  CUSTOMER_CODE                     AS customerCode,\n" +
                "  CUSTOMER_NAME             AS customerName,\n" +
                "  OPER_CODE              AS operCode,\n" +
                "  OPER_TYPE              AS operType,\n" +
                "  MK_DD          AS mkDd,\n" +
                "  EXEC_DD          AS execDd,\n" +
                "  REJECT_REASON          AS rejectReason,\n" +
                "  OPER_STATE                 AS operState \n" +
                "FROM AUDIT_RECORD \n" +
                "WHERE 1 = 1";
        if (isNotNull(entity.getCustomerCode())) {
            sql += " AND CUSTOMER_CODE = '" + entity.getCustomerCode() + "'";
        }

        if (isNotNull(entity.getCustomerName())) {
            sql += " AND CUSTOMER_NAME = '" + entity.getCustomerName() + "'";
        }

        if (isNotNull(entity.getOperState())) {
            sql += " AND OPER_STATE = '" + entity.getOperState() + "'";
        }

        if (isNotNull(entity.getOperType())) {
            sql += " AND OPER_TYPE = '" + entity.getOperType() + "'";
        }

        sql += " ORDER BY MK_DD DESC";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("customerCode", String.class);
        paramType.put("customerName", String.class);
        paramType.put("operCode", String.class);
        paramType.put("operType", String.class);
        paramType.put("mkDd", Date.class);
        paramType.put("execDd", Date.class);
        paramType.put("operState", String.class);
        paramType.put("rejectReason", String.class);

        return findPageSql(page, sql, paramType, params);
    }

    public List<Map<String, Object>> findInfoById(String operCode) {
        StringBuffer sb = new StringBuffer("SELECT * ");
        sb.append(" FROM AUDIT_RECORD_DETAIL WHERE ZID='" + operCode + "'");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String, Object>> customerQuery(Map<String, String> params) {
        StringBuffer sb = new StringBuffer("SELECT CLIENT_CODE AS CustCode,CLIENT_NAME AS CustName,SYNC_CLIENT_NAME AS LHTCustName,SYNC_COMCODE AS LHTCustCode ,PLEDGE_TYPE AS STATUS");
        sb.append(" FROM BASE_CLIENT_INFO WHERE CLIENT_CODE='" + params.get("ClientCode") + "' ");
        sb.append(" AND  CLIENT_NAME='" + params.get("ClientName") + "' ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String, Object>> findAudit(String operCode) {
        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        sb.append(" AUDIT_RECORD WHERE OPER_CODE='" + operCode + "'");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public int updateAudit(Map<String, Object> data) {
        String sql = "UPDATE AUDIT_RECORD SET REJECT_REASON='"+data.get("reason")+"', EXEC_DD=to_date('" + data.get("reqDd") + "','yyyy-mm-dd hh24:mi:ss') ,OPER_STATE='" + data.get("state") + "' WHERE OPER_CODE='" + data.get("operaCode") + "'";
        SQLQuery sqlQuery = createSQLQuery(sql);
        int result = sqlQuery.executeUpdate();
        return result;
    }

    public List<Map<String, Object>> typeQuery(Map<String, String> params) {
        StringBuffer sb = new StringBuffer("SELECT CARGO_STATE AS cargoState,CARGO_NAME AS cargoName,TYPE_NAME AS typeName,CLASS_NAME AS className,SHIPNUM AS shipnum ");
        sb.append("  FROM BASE_SKU_BASE_INFO ");
        sb.append(" WHERE TYPE_NAME='" + params.get("typeName") + "'");
        sb.append(" AND CLASS_NAME='" + params.get("className") + "'");
        sb.append(" AND CARGO_STATE='" + params.get("cargoState") + "'");
        SQLQuery sqlQuery = createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
