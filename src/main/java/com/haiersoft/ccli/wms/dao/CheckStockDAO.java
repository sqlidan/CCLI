package com.haiersoft.ccli.wms.dao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.entity.Stock;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CheckStockDAO extends HibernateDao<Stock, String> {

    public Page<Stock> checkStockByTrayId(Page<Stock> page, Stock stock) {

        Map<String, Object> params = new HashMap<String, Object>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " t.TRAY_ID AS trayCode, "
                + " t.BILL_NUM AS billCode, "
                + " t.CTN_NUM AS ctnNum, "
                + " t.ASN AS asn, "
                + " t.SKU_ID AS sku, "
                + " t.CONTACT_NUM AS contactCode, "
                + " t.STOCK_NAME AS clientName, "
                + " t.CARGO_LOCATION AS locationCode, "
                + " t.WAREHOUSE AS warehouse, " //仓库名 gzq 20160627 添加
                + " t.CARGO_NAME AS cargoName, "
                + " t.CARGO_TYPE AS cargoType, "
                + " t.NOW_PIECE AS nowNum, "
                + " t.NET_WEIGHT AS netWeight, "
                + " t.GROSS_WEIGHT AS grossWeight, "
                + " t.UNITS AS units, "
                + " t.CARGO_STATE AS state, "
                + " t.ENTER_TALLY_TIME as enterTime, "
                + " t.ENTER_STOCK_TIME as inTime, "
                + " t.ENTER_TALLY_OPERSON as enterPerson, "
                + " t.ENTER_OPERSON as enterOp, "
                + " t.AREA_NUM as areaNum, "
                + " sum(t.now_piece)over(partition by null) as allpiece, "
                + " sum(t.now_piece * t.net_single)over(partition by null) as allnet, "
                + " sum(t.now_piece * t.gross_single)over(partition by null) as allgross "
                + " FROM bis_tray_info t "
                + " WHERE 1 = 1 ");

        if (!StringUtils.isNull(stock.getTrayCode())) {
            String tmp = formatWhereInParams(stock.getTrayCode(), ",");
            sql.append(" and t.TRAY_ID IN (").append(tmp).append(") ");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            params.put("billCode", stock.getBillCode());
            sql.append(" and t.BILL_NUM = :billCode");
        }

        if (!StringUtils.isNull(stock.getCtnNum())) {
            params.put("ctnNum", stock.getCtnNum());
            sql.append(" and t.CTN_NUM = :ctnNum");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            params.put("clientName", stock.getClientName());
            sql.append(" and t.STOCK_NAME = :clientName");
        }

        if (!StringUtils.isNull(stock.getFloorNum())) {
            params.put("floorNum", stock.getFloorNum());
            sql.append(" and t.FLOOR_NUM = :floorNum");
        }

        if (!StringUtils.isNull(stock.getRoomNum())) {
            params.put("roomNum", stock.getRoomNum());
            sql.append(" and t.ROOM_NUM = :roomNum");
        }

        String limitSQL = "SELECT * FROM (" + sql.toString() + ") WHERE nowNum != 0 ORDER BY areaNum ";

        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("trayCode", String.class);
        parm.put("billCode", String.class);
        parm.put("ctnNum", String.class);
        parm.put("asn", String.class);
        parm.put("sku", String.class);
        parm.put("contactCode", String.class);
        parm.put("clientName", String.class);
        parm.put("locationCode", String.class);
        parm.put("warehouse", String.class);
        parm.put("cargoName", String.class);
        parm.put("cargoType", String.class);
        parm.put("nowNum", Integer.class);
        parm.put("netWeight", Double.class);
        parm.put("grossWeight", Double.class);
        parm.put("allpiece", Integer.class);
        parm.put("allnet", Double.class);
        parm.put("allgross", Double.class);
        parm.put("units", String.class);
        parm.put("state", String.class);
        parm.put("enterTime", Date.class);
        parm.put("inTime", Date.class);
        parm.put("enterPerson", String.class);
        parm.put("enterOp", String.class);
        parm.put("areaNum", String.class);

        return findPageSql(page, limitSQL, parm, params);

    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findStockByTrayId(Stock stock) {

        Map<String, Object> params = new HashMap<String, Object>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " t.TRAY_ID AS trayCode, "
                + " t.BILL_NUM AS billCode, "
                + " t.CTN_NUM AS ctnNum, "
                + " t.ASN AS asn, "
                + " t.SKU_ID AS sku, "
                + " t.CONTACT_NUM AS contactCode, "
                + " t.STOCK_NAME AS clientName, "
                + " t.CARGO_LOCATION AS locationCode, "
                + " t.WAREHOUSE AS warehouse, " //仓库名 gzq 20160627 添加
                + " t.CARGO_NAME AS cargoName, "
                + " t.CARGO_TYPE AS cargoType, "
                + " t.NOW_PIECE AS nowNum, "
                + " t.NET_WEIGHT AS netWeight, "
                + " t.GROSS_WEIGHT AS grossWeight, "
                + " t.UNITS AS units, "
                + " t.CARGO_STATE AS state, "
                + " t.ENTER_TALLY_TIME as enterTime, "
                + " t.ENTER_STOCK_TIME as inTime, "
                + " t.ENTER_TALLY_OPERSON as enterPerson, "
                + " t.ENTER_OPERSON as enterOp, "
                + " t.AREA_NUM as areaNum, "
                + " sum(t.now_piece)over(partition by null) as allpiece, "
                + " sum(t.now_piece * t.net_single)over(partition by null) as allnet, "
                + " sum(t.now_piece * t.gross_single)over(partition by null) as allgross "
                + " FROM bis_tray_info t "
                + " WHERE 1 = 1 ");

        if (!StringUtils.isNull(stock.getTrayCode())) {
            String tmp = formatWhereInParams(stock.getTrayCode(), ",");
            sql.append(" and t.TRAY_ID IN (").append(tmp).append(") ");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            params.put("billCode", stock.getBillCode());
            sql.append(" and t.BILL_NUM = :billCode");
        }

        if (!StringUtils.isNull(stock.getCtnNum())) {
            params.put("ctnNum", stock.getCtnNum());
            sql.append(" and t.CTN_NUM = :ctnNum");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            params.put("clientName", stock.getClientName());
            sql.append(" and t.STOCK_NAME = :clientName");
        }

        if (!StringUtils.isNull(stock.getFloorNum())) {
            params.put("floorNum", stock.getFloorNum());
            sql.append(" and t.FLOOR_NUM = :floorNum");
        }

        if (!StringUtils.isNull(stock.getRoomNum())) {
            params.put("roomNum", stock.getRoomNum());
            sql.append(" and t.ROOM_NUM = :roomNum");
        }

        String limitSQL = "SELECT * FROM (" + sql.toString() + ") WHERE nowNum != 0 ORDER BY areaNum ";

        SQLQuery sqlQuery = createSQLQuery(limitSQL, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

    // ------------------------------------------ 箱号盘点 ------------------------------------------

    public Page<Stock> checkStockByCtn(Page<Stock> page, Stock stock) {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT\n" +
                "        t.CTN_NUM                         AS ctnNum,\n" +
                "        t.BILL_NUM                        AS billCode,\n" +
                "        t.SKU_ID                          AS sku,\n" +
                "        t.CARGO_NAME                      AS cargoName,\n" +
                "        MAX(t.CARGO_TYPE)                 AS cargoType,\n" +
                "        t.CARGO_LOCATION                  AS locationCode,\n" +
                "        t.STOCK_NAME                      AS clientName,\n" +
                "        SUM(t.NOW_PIECE)                  AS allpiece,\n" +
                "        SUM(t.NET_SINGLE * t.NOW_PIECE)   AS allnet,\n" +
                "        SUM(t.GROSS_SINGLE * t.NOW_PIECE) AS allgross,\n" +
                "        MAX(t.UNITS)                      AS units\n" +
                "      FROM BIS_TRAY_INFO t\n" +
                "      WHERE 1 = 1";

        if (isNotNull(stock.getCtnNum())) {
            String tmp = formatWhereInParams(stock.getCtnNum(), ",");
            sql += (" and t.CTN_NUM IN (" + tmp + ") ");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            params.put("billCode", stock.getBillCode());
            sql += (" and t.BILL_NUM = :billCode");
        }

        if (!StringUtils.isNull(stock.getCtnNum())) {
            params.put("ctnNum", stock.getCtnNum());
            sql += (" and t.CTN_NUM = :ctnNum");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            params.put("clientName", stock.getClientName());
            sql += (" and t.STOCK_NAME = :clientName");
        }

        if (!StringUtils.isNull(stock.getFloorNum())) {
            params.put("floorNum", stock.getFloorNum());
            sql += (" and t.FLOOR_NUM = :floorNum");
        }

        if (!StringUtils.isNull(stock.getRoomNum())) {
            params.put("roomNum", stock.getRoomNum());
            sql += (" and t.ROOM_NUM = :roomNum");
        }

        sql += " GROUP BY t.CTN_NUM, t.BILL_NUM, t.SKU_ID, t.UNITS, t.CARGO_NAME, t.CARGO_TYPE, t.CARGO_LOCATION, t.STOCK_NAME ";

        String limitSQL = "SELECT * FROM (" + sql + ") WHERE allpiece != 0 ORDER BY locationCode ";

        //查询对象属性转换
        Map<String, Object> paramType = new HashMap<String, Object>();
        paramType.put("ctnNum", String.class);
        paramType.put("billCode", String.class);
        paramType.put("sku", String.class);
        paramType.put("clientName", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("cargoType", String.class);
        paramType.put("locationCode", String.class);
        paramType.put("allpiece", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class);
        paramType.put("units", String.class);

        return findPageSql(page, limitSQL, paramType, params);
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findStockByCtn(Stock stock) {

        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT" +
                "  t.CTN_NUM                               AS ctnNum," +
                "  t.AREA_NUM                              AS areaNum," +
                "  SUM(t.NOW_PIECE)                        AS allpiece," +
                "  SUM(t.NET_SINGLE * t.NOW_PIECE)         AS allnet," +
                "  SUM(t.GROSS_SINGLE * t.NOW_PIECE)       AS allgross," +
                "  t.UNITS                                 AS units," +
                "  t.CARGO_NAME                            AS cargoName" +
                "  FROM BIS_TRAY_INFO t WHERE 1 = 1 ";

        if (isNotNull(stock.getCtnNum())) {
            String tmp = formatWhereInParams(stock.getCtnNum(), ",");
            sql += (" and t.CTN_NUM IN (" + tmp + ") ");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            params.put("billCode", stock.getBillCode());
            sql += (" and t.BILL_NUM = :billCode");
        }

        if (!StringUtils.isNull(stock.getCtnNum())) {
            params.put("ctnNum", stock.getCtnNum());
            sql += (" and t.CTN_NUM = :ctnNum");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            params.put("clientName", stock.getClientName());
            sql += (" and t.STOCK_NAME = :clientName");
        }

        if (!StringUtils.isNull(stock.getFloorNum())) {
            params.put("floorNum", stock.getFloorNum());
            sql += (" and t.FLOOR_NUM = :floorNum");
        }

        if (!StringUtils.isNull(stock.getRoomNum())) {
            params.put("roomNum", stock.getRoomNum());
            sql += (" and t.ROOM_NUM = :roomNum");
        }

        sql += " GROUP BY t.CTN_NUM, t.UNITS, t.CARGO_NAME,t.AREA_NUM ";

        String limitSQL = "SELECT * FROM (" + sql + ") WHERE allpiece != 0 ORDER BY areaNum ";

        SQLQuery sqlQuery = createSQLQuery(limitSQL, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
