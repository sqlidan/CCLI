package com.haiersoft.ccli.wms.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BisCheckStock;

@Repository
public class StockFeedbackDAO extends HibernateDao<BisCheckStock, Integer> {

    /**
     * 分页获取盘点反馈明细
     */
    public Page<BisCheckStock> pageRecordStock(Page<BisCheckStock> page, BisCheckStock checkStock) {

        String sql = "SELECT"
                + " t.ID AS id, "
                + " t.TRAY_ID AS trayId, "
                + " t.BILL_NUM AS billNum, "
                + " t.CTN_NUM AS ctnNum, "
                + " t.SKU_ID AS skuId, "
                + " t.CARGO_NAME AS cargoName, "
                + " t.NOW_PIECE AS nowPiece, "
                + " t.NET_WEIGHT AS netWeight, "
                + " t.GROSS_WEIGHT AS grossWeight, "
                + " t.UNITS AS units, "
                + " t.REAL_CARGO_NAME AS realCargoName, "
                + " t.REAL_PIECE AS realPiece, "
                + " t.DESCRIPTION AS description, "
                + " t.CHECK_NAME AS checkName, "
                + " t.CHECK_DATE AS checkTime "
                + " FROM BIS_CHECK_STOCK t WHERE 1=1 ";

        Map<String, Object> params = new HashMap<String, Object>();

        if (isNotNull(checkStock.getTrayId())) {
            sql += (" AND t.TRAY_ID = :trayId ");
            params.put("trayId", checkStock.getTrayId());
        }

        if (isNotNull(checkStock.getCtnNum())) {
            sql += (" AND t.CTN_NUM = :ctnName ");
            params.put("ctnName", checkStock.getCtnNum());
        }

        //查询对象属性转换
        Map<String, Object> paramType = new HashMap<String, Object>();
        paramType.put("id", Integer.class);
        paramType.put("trayId", String.class);
        paramType.put("billNum", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("skuId", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("nowPiece", Integer.class);
        paramType.put("netWeight", Double.class);
        paramType.put("grossWeight", Double.class);
        paramType.put("units", String.class);
        paramType.put("realCargoName", String.class);
        paramType.put("realPiece", Integer.class);
        paramType.put("description", String.class);
        paramType.put("checkName", String.class);
        paramType.put("checkTime", Date.class);

        return findPageSql(page, sql, paramType, params);
    }

}
