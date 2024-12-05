package com.haiersoft.ccli.report.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.entity.ATray;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ATrayDao extends HibernateDao<ATray, String> {

    public int deleteData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" delete from A_TRAY ");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sb.toString());
        int i = sqlQuery.executeUpdate();
        return i;
    }

    public List<Map<String,Object>> queryData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT y.*,ROWNUM AS xh " +
                " FROM ( " +
                " SELECT " +
                " X.billCode, " +
                " SUM( x.nowNum ) AS nowNum, " +
                " SUM( x.allnet ) AS allnet, " +
                " SUM( x.allgross ) AS allgross  " +
                " FROM " +
                " ( " +
                " SELECT " +
                "  tray.stock_in AS clientId, " +
                "  tray.stock_name AS clientName, " +
                "  ( CASE st.IF_BONDED WHEN '1' THEN '1' ELSE '0' END ) AS isBonded, " +
                "  tray.bill_num AS billCode, " +
                "  tray.ctn_num AS ctnNum, " +
                "  nvl( info.bgdh, st.bgdh ) AS bgdh, " +
                "  nvl( info.ycg, st.ycg ) AS ycg, " +
                "  TO_CHAR( nvl( info.BGDHDATE, st.BGDHDATE ), 'yyyy-mm-dd' ) AS bgdhdate, " +
                "  tray.sku_id AS sku, " +
                "  st.CTN_TYPE_SIZE AS cz, " +
                "  s.type_name AS bigName, " +
                "  s.cargo_type AS bigType, " +
                "  s.class_name AS simName, " +
                "  s.class_type AS simType, " +
                "  tray.CARGO_NAME AS cargoName, " +
                "  tray.rkTime, " +
                "  tray.enter_state AS state, " +
                "  SUM( tray.num ) AS nowNum, " +
                "  SUM( tray.net_weight ) AS allnet, " +
                "  SUM( tray.gross_weight ) AS allgross, " +
                "  tray.CONTACT_NUM AS contactCode, " +
                "  st.OPERATOR AS createUser, " +
                "  tray.asn, " +
                "  tray.cargo_location AS locationCode, " +
                "  decode( info.HS_CODE, NULL, bai.HS_CODE, info.HS_CODE ) AS hsCode, " +
                "  decode( info.ACCOUNT_BOOK, NULL, bai.ACCOUNT_BOOK, info.ACCOUNT_BOOK ) AS accountBook, " +
                "  decode( info.HS_ITEMNAME, NULL, bai.HS_ITEMNAME, info.HS_ITEMNAME ) AS hsItemname  " +
                " FROM " +
                "  ( " +
                "  SELECT " +
                "   tray.stock_in, " +
                "   tray.stock_name, " +
                "   tray.bill_num, " +
                "   tray.ctn_num, " +
                "   tray.sku_id, " +
                "   SUM( tray.now_piece ) AS num, " +
                "   ( CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state, " +
                "   tray.CARGO_NAME, " +
                "   SUM( tray.net_weight ) AS net_weight, " +
                "   SUM( tray.gross_weight ) AS gross_weight, " +
                "   tray.CONTACT_NUM, " +
                "   to_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ) AS rkTime, " +
                "   tray.asn, " +
                "   tray.cargo_location  " +
                "  FROM " +
                "   BIS_TRAY_INFO tray  " +
                "  WHERE " +
                "   1 = 1  " +
                "   AND ( tray.cargo_state = '01' OR tray.cargo_state = '10' )  " +
                "   AND tray.now_piece != 0  " +
                "  GROUP BY " +
                "   tray.stock_in, " +
                "   tray.stock_name, " +
                "   tray.bill_num, " +
                "   tray.ctn_num, " +
                "   tray.sku_id, " +
                "   tray.enter_state, " +
                "   tray.CARGO_NAME, " +
                "   tray.CONTACT_NUM, " +
                "   tray.asn, " +
                "   tray.enter_state, " +
                "   to_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ), " +
                "   tray.cargo_location  " +
                "  ) tray " +
                "  LEFT JOIN BIS_ENTER_STOCK st ON ( tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID ) " +
                "  LEFT JOIN BIS_ENTER_STOCK_INFO info ON ( " +
                "   st.ITEM_NUM = info.ITEM_NUM  " +
                "   AND st.LINK_ID = info.LINK_ID  " +
                "   AND tray.ctn_num = info.ctn_num  " +
                "   AND tray.sku_id = info.sku  " +
                "  ) " +
                "  LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id " +
                "  LEFT JOIN BIS_ASN_INFO bai ON bai.ASN_ID = tray.asn  " +
                "  AND bai.SKU_ID = tray.SKU_ID " +
                "  LEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE  " +
                " WHERE " +
                "  1 = 1  " +
                "  AND ( st.IF_BONDED = '0' OR st.IF_BONDED IS NULL )  " +
                " GROUP BY " +
                "  tray.stock_in, " +
                "  tray.stock_name, " +
                "  st.IF_BONDED, " +
                "  tray.bill_num, " +
                "  tray.ctn_num, " +
                "  info.bgdh, " +
                "  info.ycg, " +
                "  st.bgdh, " +
                "  st.ycg, " +
                "  to_char( nvl( info.BGDHDATE, st.BGDHDATE ), 'yyyy-mm-dd' ), " +
                "  tray.sku_id, " +
                "  st.CTN_TYPE_SIZE, " +
                "  s.type_name, " +
                "  s.cargo_type, " +
                "  s.class_name, " +
                "  s.class_type, " +
                "  tray.CARGO_NAME, " +
                "  tray.rkTime, " +
                "  tray.enter_state, " +
                "  tray.CONTACT_NUM, " +
                "  st.OPERATOR, " +
                "  tray.asn, " +
                "  tray.cargo_location, " +
                "  info.HS_CODE, " +
                "  info.ACCOUNT_BOOK, " +
                "  info.HS_ITEMNAME, " +
                "  bai.HS_CODE, " +
                "  bai.HS_ITEMNAME, " +
                "  bai.ACCOUNT_BOOK, " +
                "  bai.HS_CODE, " +
                "  bai.HS_ITEMNAME, " +
                "  bai.ACCOUNT_BOOK  " +
                " ORDER BY " +
                "  tray.bill_num, " +
                "  tray.ctn_num, " +
                "  tray.rkTime  " +
                " ) x  " +
                "WHERE " +
                " 1 = 1  " +
                " GROUP BY " +
                " x.billCode " +
                ") y ");
        SQLQuery sqlQuery=createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public int saveATray(ATray aTray) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", aTray.getXh());
        params.put("billCode", aTray.getBillCode());
        params.put("nowNum", aTray.getNowNum());
        params.put("allnet", aTray.getAllnet());
        params.put("allgross", aTray.getAllgross());
        params.put("xh", aTray.getXh());
        String sql = "insert into A_TRAY (ID,BILLCODE,NOWNUM,ALLNET,ALLGROSS,XH) " +
                " values (:id,:billCode,:nowNum,:allnet,:allgross,:xh)";
        SQLQuery logSQLQuery = createSQLQuery(sql, params);
       return logSQLQuery.executeUpdate();
    }

    public List<Map<String,Object>> querySQDData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT a.*,b.LINK_ID as linkId " +
                " from A_TRAY a " +
                " left join BIS_ENTER_STOCK b on a.BILLCODE = b.ITEM_NUM and b.DEL_FLAG = '0' " +
                " where a.CREATESQD = '0' ");

        SQLQuery sqlQuery=createSQLQuery(sb.toString());
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
