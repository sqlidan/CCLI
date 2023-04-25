package com.haiersoft.ccli.report.dao;
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

/**
 * Created by dxl584327830 on 16/8/15.
 */
@Repository
public class TrayHistoryReportDao extends HibernateDao<Stock, String> {

    public Page<Stock> searchTrayHistory(Page<Stock> page, Stock stock) {

        Map<String, Object> params = new HashMap<>();

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
                + " t.WAREHOUSE AS warehouse, "
                + " t.CARGO_NAME AS cargoName, "
                + " t.CARGO_TYPE AS cargoType, "
                + " t.NOW_PIECE AS nowNum, "
                + " t.NET_WEIGHT AS netWeight, "
                + " t.GROSS_WEIGHT AS grossWeight, "
                + " t.UNITS AS units, "
                + " t.CARGO_STATE AS state, "
                + " t.ENTER_TALLY_TIME as enterTime, "
                + " t.ENTER_TALLY_OPERSON as enterPerson, "
                + " t.ENTER_OPERSON as enterOp, "
                + " t.BACKUP_TIME AS backupTime " // CRAETE BY ddd 2016-08-15 数据库备份日期
                + " FROM BIS_TRAY_HISTORY_INFO t "
                + " WHERE 1 = 1 ");

        if (!StringUtils.isNull(stock.getCtnNum())) {
            sql.append(" and lower(t.CTN_NUM) like lower(:ctnNum) ");
            params.put("ctnNum", "%" + stock.getCtnNum() + "%");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            sql.append(" and lower(t.BILL_NUM) like lower(:billCode) ");
            params.put("billCode", "%" + stock.getBillCode() + "%");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            sql.append(" and lower(t.STOCK_NAME) like lower(:clientName) ");
            params.put("clientName", "%" + stock.getClientName() + "%");
        }

        if (stock.getBackupTime() != null) {
            sql.append(" and t.BACKUP_TIME like to_date(to_char(:backupTime,'yyyy-MM-dd'),'yyyy-MM-dd')");
            params.put("backupTime", stock.getBackupTime());
        }

        //查询对象属性转换
        Map<String, Object> parm = new HashMap<>();
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
        parm.put("units", String.class);
        parm.put("state", String.class);
        parm.put("enterTime", Date.class);
        parm.put("enterPerson", String.class);
        parm.put("enterOp", String.class);
        parm.put("backupTime", Date.class);

        return findPageSql(page, sql.toString(), parm, params);

    }


    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchAllTrayHistoryByParams(Stock stock) {

        Map<String, Object> params = new HashMap<>();

        StringBuffer sql = new StringBuffer(""
                + " SELECT "
                + " t.TRAY_ID, "
                + " t.BILL_NUM, "
                + " t.CTN_NUM, "
                + " t.ASN, "
                + " t.SKU_ID, "
                + " t.CONTACT_NUM, "
                + " t.STOCK_NAME, "
                + " t.CARGO_LOCATION, "
                + " t.WAREHOUSE, "
                + " t.CARGO_NAME, "
                + " t.CARGO_TYPE, "
                + " t.NOW_PIECE, "
                + " t.NET_WEIGHT, "
                + " t.GROSS_WEIGHT, "
                + " t.UNITS,"
                + " t.CARGO_STATE,"
                + " t.ENTER_TALLY_TIME ,"
                + " t.ENTER_TALLY_OPERSON , "
                + " t.ENTER_OPERSON  , "
                + " t.BACKUP_TIME   " // CRAETE BY ddd 2016-08-15 数据库备份日期
                + " FROM BIS_TRAY_HISTORY_INFO t "
                + " WHERE 1 = 1 ");

        if (!StringUtils.isNull(stock.getCtnNum())) {
            sql.append(" and lower(t.CTN_NUM) like lower(:ctnNum) ");
            params.put("ctnNum", "%" + stock.getCtnNum() + "%");
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            sql.append(" and lower(t.BILL_NUM) like lower(:billCode) ");
            params.put("billCode", "%" + stock.getBillCode() + "%");
        }

        if (!StringUtils.isNull(stock.getClientName())) {
            sql.append(" and lower(t.STOCK_NAME) like lower(:clientName) ");
            params.put("clientName", "%" + stock.getClientName() + "%");
        }

        if (stock.getBackupTime() != null) {
            sql.append(" and t.BACKUP_TIME = :backupTime");
            params.put("backupTime", stock.getBackupTime());
        }

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
