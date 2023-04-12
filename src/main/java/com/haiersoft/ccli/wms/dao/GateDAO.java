package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.*;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/20.
 */
@Repository
public class GateDAO extends HibernateDao<GateEntity, String> {

    public Page<GateEntity> page(Page<GateEntity> page, GateEntity entity) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bio.XH          AS ctnNum,\n" +
                "  bio.XX          AS ctnVersion,\n" +
                "  bio.TDH         AS billNum,\n" +
                "  bio.ASN         AS asn,\n" +
                "  bio.OUT_CAR_NUM AS outCarNum,\n" +
                "  bio.OUT_DRIVER_NAME AS outDriverName,\n" +
                "  bpb.CWID        AS positionId,\n" +
                "  bpb.CW          AS position,\n" +
                "  bpb.ZXDW        AS clientId,\n" +
                "  bpb.ZXDWM       AS clientName,\n" +
                "  bio.IN_CAR_NUM     AS carNum,\n" +
                "  bio.IN_DRIVER_NAME AS driverName,\n" +
                "  bio.CREATE_DATE AS createDate\n" +
                "FROM BIS_IN_OUT bio\n" +
                "  LEFT JOIN BIS_PRESENCE_BOX bpb ON bio.XH = bpb.XH\n" +
                "  LEFT JOIN BIS_GATE_CAR bgc ON bio.XH = bgc.CTN_NUM\n" +
                "WHERE 1 = 1 \n";

        if (isNotNull(entity.getCtnNum())) {
            sql += " AND bio.XH = :ctnNum AND bpb.XH = :ctnNum AND bgc.CTN_NUM = :ctnNum \n";
            params.put("ctnNum", entity.getCtnNum());
        }

        if (isNotNull(entity.getBillNum())) {
            sql += " AND bio.TDH = :billNum \n";
            params.put("billNum", entity.getBillNum());
        }

        if (isNotNull(entity.getAsn())) {
            sql += " AND bio.ASN = :asn \n";
            params.put("asn", entity.getAsn());
        }

        if (isNotNull(entity.getClientId())) {
            sql += " AND bpb.ZXDW = :clientId \n";
            params.put("clientId", entity.getClientId());
        }

        if (entity.getStartDate() != null) {
            sql += " AND bio.CREATE_DATE >= :startDate \n";
            params.put("startDate", entity.getStartDate());
        }

        if (entity.getEndDate() != null) {
            sql += " AND bio.CREATE_DATE <= :endDate \n";
            params.put("endDate", entity.getEndDate());
        }

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("ctnNum", String.class);
        paramType.put("ctnVersion", String.class);
        paramType.put("billNum", String.class);
        paramType.put("asn", String.class);
        paramType.put("outCarNum", String.class);
        paramType.put("outDriverName", String.class);
        paramType.put("positionId", Integer.class);
        paramType.put("position", String.class);
        paramType.put("clientId", String.class);
        paramType.put("clientName", String.class);
        paramType.put("carNum", String.class);
        paramType.put("driverName", String.class);
        paramType.put("createDate", Date.class);

        return findPageSql(page, sql, paramType, params);
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAsnAndBillNumByCtnNum(String ctnNum) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  ba.ASN      AS asn,\n" +
                "  ba.BILL_NUM AS billNum\n" +
                "FROM BIS_ASN ba\n" +
                "WHERE ba.CTN_NUM = :ctnNum and ba.asn_state='1' ";

        params.put("ctnNum", ctnNum);

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

}
