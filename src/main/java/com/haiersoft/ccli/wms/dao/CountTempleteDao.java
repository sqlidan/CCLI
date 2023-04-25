package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.CountTemplete;

/**
 * 财务仓储统计模版
 */
@Repository
public class CountTempleteDao extends HibernateDao<CountTemplete, String> {
    public Page<CountTemplete> searchStockReport(Page<CountTemplete> page, CountTemplete countTemplete) {

        Map<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  asn.STOCK_NAME   AS rukuhuozhu,   TO_CHAR(kc.BILL_NUM)     AS tidanhao, TO_CHAR(kc.CTN_NUM)    AS jizhuangxianghao,"
                + "   TO_CHAR(kc.SKU_ID)        AS sku, TO_CHAR(kc.CARGO_NAME)    AS huowumingcheng, kc.CARGO_TYPE    AS zhonglei, '入库'   AS churu,"
                + "  (SUBSTR(asn.INBOUND_DATE,4,8)||'年')        AS yuefen , asn.INBOUND_DATE     AS shijian,"
                + "  sum(kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)        AS shuliang, kc.NET_SINGLE           AS danjing,"
                + " kc.GROSS_SINGLE   AS danmao,  sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.NET_SINGLE)   AS zongjing,"
                + " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.GROSS_SINGLE) AS zongmao ,''  AS shouhuokehu"
                + " FROM BIS_ASN asn  INNER JOIN   BIS_TRAY_INFO kc  ON   asn.ASN =kc.ASN AND asn.IF_SECOND_ENTER='1' AND kc.CONTACT_TYPE='1' ");
        if (countTemplete.getBILL_NUM() != null && !"".equals(countTemplete.getBILL_NUM())) {//提单号
            sql.append(" AND kc.BILL_NUM =:BILL_NUM ");
            parme.put("BILL_NUM", countTemplete.getBILL_NUM());
        }
        if (countTemplete.getSTOCK_NAME() != null && !"".equals(countTemplete.getSTOCK_NAME())) {//--客户
            sql.append(" and asn.STOCK_NAME =:STOCK_NAME ");
            parme.put("STOCK_NAME", countTemplete.getSTOCK_NAME());
        }
        if (countTemplete.getCTN_NUM() != null && !"".equals(countTemplete.getCTN_NUM())) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            parme.put("CTN_NUM", countTemplete.getCTN_NUM());
        }
        if (countTemplete.getStarTime() != null && !"".equals(countTemplete.getStarTime())) {//开始时间
            sql.append(" and asn.INBOUND_DATE>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("starTime", countTemplete.getStarTime());
        }
        if (countTemplete.getEndTime() != null && !"".equals(countTemplete.getEndTime())) {//结束时间
            sql.append(" and asn.INBOUND_DATE<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("endTime", countTemplete.getEndTime());
        }
        sql.append(" GROUP BY asn.STOCK_NAME ,kc.BILL_NUM,kc.CTN_NUM,kc.SKU_ID,asn.INBOUND_DATE,kc.CARGO_NAME,kc.CARGO_TYPE , "
                + "kc.NET_SINGLE ,kc.GROSS_SINGLE ");
        sql.append(" union all "
                + "SELECT kc.STOCK_NAME    AS rukuhuozhu,  TO_CHAR(ck.BILL_NUM)  AS tidanhao,"
                + " TO_CHAR(kc.CTN_NUM)  AS jizhuangxianghao, TO_CHAR(kc.SKU_ID)  AS sku,"
                + " TO_CHAR(kc.CARGO_NAME) AS huowumingcheng, kc.CARGO_TYPE    AS zhonglei,"
                + " '出库'     AS churu,SUBSTR(ck.LOADING_TIME,4,8)||'年' AS yuefen ,"
                + " ck.LOADING_TIME  AS shijian, ck.PIECE   AS shuliang,"
                + " kc.NET_SINGLE   AS danjing, kc.GROSS_SINGLE  AS danmao,"
                + " ck.PIECE*kc.NET_SINGLE  AS zongjing, ck.PIECE*kc.GROSS_SINGLE AS zongmao,"
                + " ck.RECEIVER_NAME AS shouhuokehu "
                + " FROM( SELECT c.*, lor.RECEIVER_NAME , lor.STOCK_NAME"
                + "  FROM  BIS_LOADING_INFO c INNER JOIN  BIS_LOADING_ORDER lor"
                + " ON c.STOCK_ID=lor.STOCK_ID  where  c.LOADING_STATE='2')ck "
                + " INNER JOIN  BIS_TRAY_INFO kc "
                + "ON ck.TRAY_ID=kc.TRAY_ID ");
        if (countTemplete.getBILL_NUM() != null && !"".equals(countTemplete.getBILL_NUM())) {//提单号
            sql.append(" AND ck.BILL_NUM =:BILL_NUM ");
            parme.put("BILL_NUM", countTemplete.getBILL_NUM());
        }
        if (countTemplete.getSTOCK_NAME() != null && !"".equals(countTemplete.getSTOCK_NAME())) {//--客户
            sql.append(" and kc.STOCK_NAME =:STOCK_NAME ");
            parme.put("STOCK_NAME", countTemplete.getSTOCK_NAME());
        }
        if (countTemplete.getCTN_NUM() != null && !"".equals(countTemplete.getCTN_NUM())) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            parme.put("CTN_NUM", countTemplete.getCTN_NUM());
        }
        if (countTemplete.getStarTime() != null && !"".equals(countTemplete.getStarTime())) {//开始时间
            sql.append(" and ck.LOADING_TIME>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("starTime", countTemplete.getStarTime());
        }
        if (countTemplete.getEndTime() != null && !"".equals(countTemplete.getEndTime())) {//结束时间
            sql.append(" and ck.LOADING_TIME<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("endTime", countTemplete.getEndTime());
        }
        sql.append("GROUP BY kc.STOCK_NAME ,ck.BILL_NUM,kc.CTN_NUM,kc.SKU_ID,kc.CARGO_NAME,kc.CARGO_TYPE , kc.NET_SINGLE ,"
                + "kc.GROSS_SINGLE ,ck.LOADING_TIME, ck.PIECE   ,ck.RECEIVER_NAME  ");
        sql.append(" union all "
                + " SELECT  asn.STOCK_NAME   AS rukuhuozhu,   TO_CHAR(kc.BILL_NUM)     AS tidanhao, TO_CHAR(kc.CTN_NUM)    AS jizhuangxianghao,"
                + "   TO_CHAR(kc.SKU_ID)        AS sku, TO_CHAR(kc.CARGO_NAME)    AS huowumingcheng, kc.CARGO_TYPE    AS zhonglei, '分拣'   AS churu,"
                + "  (SUBSTR(asn.INBOUND_DATE,4,8)||'年')        AS yuefen , asn.INBOUND_DATE     AS shijian,"
                + "  sum(kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)        AS shuliang, kc.NET_SINGLE           AS danjing,"
                + " kc.GROSS_SINGLE   AS danmao,  sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.NET_SINGLE)   AS zongjing,"
                + " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.GROSS_SINGLE) AS zongmao ,''  AS shouhuokehu"
                + " FROM BIS_ASN asn  INNER JOIN   BIS_TRAY_INFO kc  ON   asn.ASN =kc.ASN AND asn.IF_SECOND_ENTER='3' AND kc.CONTACT_TYPE='1' ");
        if (countTemplete.getBILL_NUM() != null && !"".equals(countTemplete.getBILL_NUM())) {//提单号
            sql.append(" AND kc.BILL_NUM =:BILL_NUM ");
            parme.put("BILL_NUM", countTemplete.getBILL_NUM());
        }
        if (countTemplete.getSTOCK_NAME() != null && !"".equals(countTemplete.getSTOCK_NAME())) {//--客户
            sql.append(" and asn.STOCK_NAME =:STOCK_NAME ");
            parme.put("STOCK_NAME", countTemplete.getSTOCK_NAME());
        }
        if (countTemplete.getCTN_NUM() != null && !"".equals(countTemplete.getCTN_NUM())) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            parme.put("CTN_NUM", countTemplete.getCTN_NUM());
        }
        if (countTemplete.getStarTime() != null && !"".equals(countTemplete.getStarTime())) {//开始时间
            sql.append(" and asn.INBOUND_DATE>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("starTime", countTemplete.getStarTime());
        }
        if (countTemplete.getEndTime() != null && !"".equals(countTemplete.getEndTime())) {//结束时间
            sql.append(" and asn.INBOUND_DATE<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("endTime", countTemplete.getEndTime());
        }
        sql.append(" GROUP BY asn.STOCK_NAME ,kc.BILL_NUM,kc.CTN_NUM,kc.SKU_ID,asn.INBOUND_DATE,kc.CARGO_NAME,kc.CARGO_TYPE , "
                + "kc.NET_SINGLE ,kc.GROSS_SINGLE ");
        sql.append(" union all "
                + "SELECT s.RECEIVER_NAME as rukuhuozhu,"
                + " to_char(t.BILL_NUM) as tidanhao,to_char(t.CTN_NUM) as jizhuangxianghao,"
                + " to_char(t.SKU_ID) as sku,to_char(t.CARGO_NAME) as huowumingcheng,"
                + " t.TYPE_NAME as zhonglei,'货转入' as churu ,substr(s.OPERATE_TIME,4,8)||'年' AS yuefen,"
                + " s.OPERATE_TIME as shijian,t.PIECE as shuliang,"
                + " t.NET_SINGLE as danjing,t.GROSS_SINGLE as danmao,t.PIECE*t.NET_SINGLE as zongjing,"
                + " t.PIECE*t.GROSS_SINGLE as zongmao,'' as shouhuokehu "
                + " FROM BIS_TRANSFER_STOCK s JOIN ("
                + " SELECT sq.TYPE_NAME as TYPE_NAME,sq.NET_SINGLE as NET_SINGLE,sq.GROSS_SINGLE AS GROSS_SINGLE ,"
                + " b.TRANSFER_LINK_ID AS TRANSFER_LINK_ID,b.PIECE AS PIECE,b.CARGO_NAME AS CARGO_NAME,b.SKU_ID AS SKU_ID,b.CTN_NUM AS CTN_NUM,"
                + " b.BILL_NUM AS BILL_NUM "
                + " FROM BASE_SKU_BASE_INFO sq JOIN BIS_TRANSFER_STOCK_TRAYINFO b on sq.SKU_ID=b.SKU_ID "
                + " ) t ON t.TRANSFER_LINK_ID=s.TRANSFER_ID ");
        if (countTemplete.getBILL_NUM() != null && !"".equals(countTemplete.getBILL_NUM())) {//提单号
            sql.append(" AND t.BILL_NUM =:BILL_NUM ");
            parme.put("BILL_NUM", countTemplete.getBILL_NUM());
        }
        if (countTemplete.getSTOCK_NAME() != null && !"".equals(countTemplete.getSTOCK_NAME())) {//--客户
            sql.append(" and s.RECEIVER_NAME =:STOCK_NAME ");
            parme.put("STOCK_NAME", countTemplete.getSTOCK_NAME());
        }
        if (countTemplete.getCTN_NUM() != null && !"".equals(countTemplete.getCTN_NUM())) {//箱号
            sql.append(" and t.CTN_NUM=:CTN_NUM ");
            parme.put("CTN_NUM", countTemplete.getCTN_NUM());
        }
        if (countTemplete.getStarTime() != null && !"".equals(countTemplete.getStarTime())) {//开始时间
            sql.append(" and s.OPERATE_TIME>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("starTime", countTemplete.getStarTime());
        }
        if (countTemplete.getEndTime() != null && !"".equals(countTemplete.getEndTime())) {//结束时间
            sql.append(" and s.OPERATE_TIME<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("endTime", countTemplete.getEndTime());
        }
        sql.append(" GROUP BY s.RECEIVER_NAME,t.BILL_NUM,t.CTN_NUM ,t.SKU_ID , t.CARGO_NAME ,t.TYPE_NAME ,s.OPERATE_TIME , "
                + "t.PIECE , t.NET_SINGLE  ,t.GROSS_SINGLE");
        sql.append(" union all "
                + "SELECT s.OLD_OWNER as rukuhuozhu,"
                + " to_char(t.BILL_NUM) as tidanhao,to_char(t.CTN_NUM) as jizhuangxianghao,to_char(t.SKU_ID) as sku,to_char(t.CARGO_NAME) as huowumingcheng,"
                + "t.TYPE_NAME as zhonglei,'货转出'as churu,substr(s.OPERATE_TIME,4,8)||'年' AS yuefen,"
                + "s.OPERATE_TIME as shijian ,t.PIECE as shuliang,"
                + "t.NET_SINGLE as danjing,t.GROSS_SINGLE as danmao,t.PIECE*t.NET_SINGLE as zongjing,t.PIECE*t.GROSS_SINGLE as zongmao,"
                + " s.RECEIVER_NAME as shouhuokehu "
                + "FROM BIS_TRANSFER_STOCK s JOIN ("
                + "SELECT sq.TYPE_NAME as TYPE_NAME,sq.NET_SINGLE as NET_SINGLE,sq.GROSS_SINGLE AS GROSS_SINGLE ,"
                + "a.TRANSFER_LINK_ID AS TRANSFER_LINK_ID,a.PIECE AS PIECE,a.CARGO_NAME AS CARGO_NAME,a.SKU_ID AS SKU_ID,a.CTN_NUM AS CTN_NUM, "
                + "a.BILL_NUM AS BILL_NUM "
                + "FROM BASE_SKU_BASE_INFO sq JOIN BIS_TRANSFER_STOCK_TRAYINFO a on sq.SKU_ID=a.SKU_ID "
                + ") t ON t.TRANSFER_LINK_ID=s.TRANSFER_ID ");
        if (countTemplete.getBILL_NUM() != null && !"".equals(countTemplete.getBILL_NUM())) {//提单号
            sql.append(" AND t.BILL_NUM =:BILL_NUM ");
            parme.put("BILL_NUM", countTemplete.getBILL_NUM());
        }
        if (countTemplete.getSTOCK_NAME() != null && !"".equals(countTemplete.getSTOCK_NAME())) {//--客户
            sql.append(" and s.OLD_OWNER =:STOCK_NAME ");
            parme.put("STOCK_NAME", countTemplete.getSTOCK_NAME());
        }
        if (countTemplete.getCTN_NUM() != null && !"".equals(countTemplete.getCTN_NUM())) {//箱号
            sql.append(" and t.CTN_NUM=:CTN_NUM ");
            parme.put("CTN_NUM", countTemplete.getCTN_NUM());
        }
        if (countTemplete.getStarTime() != null && !"".equals(countTemplete.getStarTime())) {//开始时间
            sql.append(" and s.OPERATE_TIME>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("starTime", countTemplete.getStarTime());
        }
        if (countTemplete.getEndTime() != null && !"".equals(countTemplete.getEndTime())) {//结束时间
            sql.append(" and s.OPERATE_TIME<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            parme.put("endTime", countTemplete.getEndTime());
        }
        sql.append(" GROUP BY s.RECEIVER_NAME,t.BILL_NUM,t.CTN_NUM ,t.SKU_ID , t.CARGO_NAME ,t.TYPE_NAME ,s.OPERATE_TIME , "
                + "t.PIECE , t.NET_SINGLE  ,t.GROSS_SINGLE,s.OLD_OWNER ");

        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("rukuhuozhu", String.class);
        parm.put("tidanhao", String.class);
        parm.put("jizhuangxianghao", String.class);
        parm.put("sku", String.class);
        parm.put("huowumingcheng", String.class);
        parm.put("zhonglei", String.class);
        parm.put("churu", String.class);
        parm.put("yuefen", String.class);
        parm.put("shijian", String.class);
        parm.put("shuliang", String.class);
        parm.put("danjing", String.class);
        parm.put("danmao", String.class);
        parm.put("zongjing", String.class);
        parm.put("zongmao", String.class);
        parm.put("shouhuokehu", String.class);

        System.err.println("Search - " + sql.toString());

        return findPageSql(page, sql.toString(), parm, parme);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findReport(String BILL_NUM, String STOCK_NAME, String CTN_NUM, String starTime, String endTime) {

        HashMap<String, Object> params = new HashMap<>();

        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT " +
                " asn.STOCK_NAME AS rukuhuozhu, " +
                " TO_CHAR(kc.BILL_NUM) AS tidanhao, " +
                " TO_CHAR(kc.CTN_NUM) AS jizhuangxianghao, " +
                " TO_CHAR(kc.SKU_ID) AS sku, " +
                " TO_CHAR(kc.CARGO_NAME) AS huowumingcheng, " +
                " kc.CARGO_TYPE AS zhonglei, " +
                " '入库' AS churu, " +
                " (SUBSTR(asn.INBOUND_DATE,4,8)||'年') AS yuefen, " +
                " asn.INBOUND_DATE AS shijian, " +
                " sum(kc.ORIGINAL_PIECE-kc.REMOVE_PIECE) AS shuliang, " + // 数量
                " kc.NET_SINGLE AS danjing, " +
                " kc.GROSS_SINGLE AS danmao, " +
                " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.NET_SINGLE) AS zongjing, " +   // 总重
                " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.GROSS_SINGLE) AS zongmao, " +  // 毛重
                " '' AS shouhuokehu  " +
                " FROM BIS_ASN asn  " +
                " INNER JOIN BIS_TRAY_INFO kc ON asn.ASN =kc.ASN AND asn.IF_SECOND_ENTER = '1' AND kc.CONTACT_TYPE = '1' ");

        if (BILL_NUM != null && !"".equals(BILL_NUM)) {//提单号
            sql.append(" AND kc.BILL_NUM =:BILL_NUM ");
            params.put("BILL_NUM", BILL_NUM);
        }

        if (STOCK_NAME != null && !"".equals(STOCK_NAME)) {//--客户
            sql.append(" and asn.STOCK_NAME =:STOCK_NAME ");
            params.put("STOCK_NAME", STOCK_NAME);
        }

        if (CTN_NUM != null && !"".equals(CTN_NUM)) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            params.put("CTN_NUM", CTN_NUM);
        }

        if (starTime != null && !"".equals(starTime)) {//开始时间
            sql.append(" and asn.INBOUND_DATE>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("starTime", starTime);
        }

        if (endTime != null && !"".equals(endTime)) {//结束时间
            sql.append(" and asn.INBOUND_DATE<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("endTime", endTime);
        }

        sql.append(" GROUP BY asn.STOCK_NAME," +
                " kc.BILL_NUM," +
                " kc.CTN_NUM," +
                " kc.SKU_ID," +
                " asn.INBOUND_DATE," +
                " kc.CARGO_NAME," +
                " kc.CARGO_TYPE," +
                " kc.NET_SINGLE," +
                " kc.GROSS_SINGLE");

        sql.append(" UNION ALL " +
                " SELECT kc.STOCK_NAME AS rukuhuozhu," +
                " TO_CHAR(ck.BILL_NUM) AS tidanhao," +
                " TO_CHAR(kc.CTN_NUM) AS jizhuangxianghao," +
                " TO_CHAR(kc.SKU_ID) AS sku," +
                " TO_CHAR(kc.CARGO_NAME) AS huowumingcheng," +
                " kc.CARGO_TYPE AS zhonglei," +
                " '出库' AS churu," +
                " SUBSTR(ck.LOADING_TIME,4,8)||'年' AS yuefen ," +
                " ck.LOADING_TIME AS shijian," +
                " sum(ck.PIECE) AS shuliang," +                  // 数量
                " kc.NET_SINGLE AS danjing," +
                " kc.GROSS_SINGLE AS danmao," +
                " sum(ck.PIECE*kc.NET_SINGLE) AS zongjing," +    // 净重
                " sum(ck.PIECE*kc.GROSS_SINGLE) AS zongmao," +   // 毛重
                " ck.RECEIVER_NAME AS shouhuokehu " +
                " FROM" +
                "(SELECT c.*,lor.RECEIVER_NAME,lor.STOCK_NAME FROM BIS_LOADING_INFO c " +
                " INNER JOIN BIS_LOADING_ORDER lor ON c.STOCK_ID=lor.STOCK_ID where c.LOADING_STATE='2'" +
                ")ck " +
                " INNER JOIN BIS_TRAY_INFO kc ON ck.TRAY_ID = kc.TRAY_ID ");

        if (BILL_NUM != null && !"".equals(BILL_NUM)) {//提单号
            sql.append(" AND ck.BILL_NUM =:BILL_NUM ");
            params.put("BILL_NUM", BILL_NUM);
        }

        if (STOCK_NAME != null && !"".equals(STOCK_NAME)) {//--客户
            sql.append(" and kc.STOCK_NAME =:STOCK_NAME ");
            params.put("STOCK_NAME", STOCK_NAME);
        }

        if (CTN_NUM != null && !"".equals(CTN_NUM)) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            params.put("CTN_NUM", CTN_NUM);
        }

        if (starTime != null && !"".equals(starTime)) {//开始时间
            sql.append(" and ck.LOADING_TIME >=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("starTime", starTime);
        }

        if (endTime != null && !"".equals(endTime)) {//结束时间
            sql.append(" and ck.LOADING_TIME <to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("endTime", endTime);
        }

        sql.append("GROUP BY kc.STOCK_NAME," +
                "ck.BILL_NUM,kc.CTN_NUM," +
                "kc.SKU_ID,kc.CARGO_NAME," +
                "kc.CARGO_TYPE," +
                "kc.NET_SINGLE," +
                "kc.GROSS_SINGLE," +
                "ck.LOADING_TIME," +
                "ck.PIECE," +
                "ck.RECEIVER_NAME");

        sql.append(" UNION ALL " +
                " SELECT asn.STOCK_NAME AS rukuhuozhu, " +
                " TO_CHAR(kc.BILL_NUM) AS tidanhao, " +
                " TO_CHAR(kc.CTN_NUM) AS jizhuangxianghao," +
                " TO_CHAR(kc.SKU_ID) AS sku, " +
                " TO_CHAR(kc.CARGO_NAME) AS huowumingcheng, " +
                " kc.CARGO_TYPE AS zhonglei, " +
                " '分拣' AS churu, " +
                " (SUBSTR(asn.INBOUND_DATE,4,8)||'年') AS yuefen, " +
                " asn.INBOUND_DATE AS shijian," +
                " sum(kc.ORIGINAL_PIECE-kc.REMOVE_PIECE) AS shuliang, " +                   // 数量
                " kc.NET_SINGLE AS danjing," +
                " kc.GROSS_SINGLE AS danmao,  " +
                " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.NET_SINGLE) AS zongjing, " +   // 净重
                " sum((kc.ORIGINAL_PIECE-kc.REMOVE_PIECE)*kc.GROSS_SINGLE) AS zongmao , " + // 毛重
                " '' AS shouhuokehu" +
                " FROM BIS_ASN asn " +
                " INNER JOIN BIS_TRAY_INFO kc ON asn.ASN = kc.ASN AND asn.IF_SECOND_ENTER = '3' AND kc.CONTACT_TYPE = '1' ");

        if (BILL_NUM != null && !"".equals(BILL_NUM)) {//提单号
            sql.append(" AND kc.BILL_NUM =:BILL_NUM ");
            params.put("BILL_NUM", BILL_NUM);
        }

        if (STOCK_NAME != null && !"".equals(STOCK_NAME)) {//--客户
            sql.append(" and asn.STOCK_NAME =:STOCK_NAME ");
            params.put("STOCK_NAME", STOCK_NAME);
        }

        if (CTN_NUM != null && !"".equals(CTN_NUM)) {//箱号
            sql.append(" and kc.CTN_NUM=:CTN_NUM ");
            params.put("CTN_NUM", CTN_NUM);
        }

        if (starTime != null && !"".equals(starTime)) {//开始时间
            sql.append(" and asn.INBOUND_DATE>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("starTime", starTime);
        }

        if (endTime != null && !"".equals(endTime)) {//结束时间
            sql.append(" and asn.INBOUND_DATE<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("endTime", endTime);
        }

        sql.append(" GROUP BY asn.STOCK_NAME, " +
                " kc.BILL_NUM, " +
                " kc.CTN_NUM, " +
                " kc.SKU_ID, " +
                " asn.INBOUND_DATE, " +
                " kc.CARGO_NAME, " +
                " kc.CARGO_TYPE, " +
                " kc.NET_SINGLE, " +
                " kc.GROSS_SINGLE ");

        sql.append(" UNION ALL " +
                " SELECT s.RECEIVER_NAME as rukuhuozhu, " +
                " to_char(t.BILL_NUM) as tidanhao, " +
                " to_char(t.CTN_NUM) as jizhuangxianghao, " +
                " to_char(t.SKU_ID) as sku, " +
                " to_char(t.CARGO_NAME) as huowumingcheng, " +
                " t.TYPE_NAME as zhonglei, " +
                " '货转入' as churu , " +
                " substr(s.OPERATE_TIME,4,8)||'年' AS yuefen, " +
                " s.OPERATE_TIME as shijian, " +
                " sum(t.PIECE) as shuliang, " +                  //数量
                " t.NET_SINGLE as danjing, " +
                " t.GROSS_SINGLE as danmao, " +
                " sum(t.PIECE*t.NET_SINGLE) as zongjing, " +     // 净重
                " sum(t.PIECE*t.GROSS_SINGLE) as zongmao, " +    // 毛重
                " '' as shouhuokehu " +
                " FROM BIS_TRANSFER_STOCK s " +
                " JOIN " +
                " (SELECT sq.TYPE_NAME as TYPE_NAME," +
                " sq.NET_SINGLE as NET_SINGLE," +
                " sq.GROSS_SINGLE AS GROSS_SINGLE," +
                " b.TRANSFER_LINK_ID AS TRANSFER_LINK_ID," +
                " b.PIECE AS PIECE," +
                " b.CARGO_NAME AS CARGO_NAME," +
                " b.SKU_ID AS SKU_ID," +
                " b.CTN_NUM AS CTN_NUM," +
                " b.BILL_NUM AS BILL_NUM " +
                " FROM BASE_SKU_BASE_INFO sq " +
                " JOIN BIS_TRANSFER_STOCK_TRAYINFO b on sq.SKU_ID = b.SKU_ID " +
                " ) t " +
                "ON t.TRANSFER_LINK_ID = s.TRANSFER_ID ");

        if (BILL_NUM != null && !"".equals(BILL_NUM)) {//提单号
            sql.append(" AND t.BILL_NUM =:BILL_NUM ");
            params.put("BILL_NUM", BILL_NUM);
        }

        if (STOCK_NAME != null && !"".equals(STOCK_NAME)) {//--客户
            sql.append(" and s.RECEIVER_NAME =:STOCK_NAME ");
            params.put("STOCK_NAME", STOCK_NAME);
        }

        if (CTN_NUM != null && !"".equals(CTN_NUM)) {//箱号
            sql.append(" and t.CTN_NUM=:CTN_NUM ");
            params.put("CTN_NUM", CTN_NUM);
        }

        if (starTime != null && !"".equals(starTime)) {//开始时间
            sql.append(" and s.OPERATE_TIME>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("starTime", starTime);
        }

        if (endTime != null && !"".equals(endTime)) {//结束时间
            sql.append(" and s.OPERATE_TIME<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("endTime", endTime);
        }

        sql.append(" GROUP BY s.RECEIVER_NAME, " +
                " t.BILL_NUM, " +
                " t.CTN_NUM, " +
                " t.SKU_ID, " +
                " t.CARGO_NAME, " +
                " t.TYPE_NAME, " +
                " s.OPERATE_TIME, " +
                " t.PIECE, " +
                " t.NET_SINGLE, " +
                " t.GROSS_SINGLE ");

        sql.append(" UNION ALL " +
                " SELECT s.OLD_OWNER as rukuhuozhu, " +
                " to_char(t.BILL_NUM) as tidanhao, " +
                " to_char(t.CTN_NUM) as jizhuangxianghao, " +
                " to_char(t.SKU_ID) as sku, " +
                " to_char(t.CARGO_NAME) as huowumingcheng, " +
                " t.TYPE_NAME as zhonglei, " +
                " '货转出'as churu, " +
                " substr(s.OPERATE_TIME,4,8)||'年' AS yuefen, " +
                " s.OPERATE_TIME as shijian, " +
                " sum(t.PIECE) as shuliang," +                   // 数量
                " t.NET_SINGLE as danjing, " +
                " t.GROSS_SINGLE as danmao, " +
                " sum(t.PIECE*t.NET_SINGLE) as zongjing," +      // 净重
                " sum(t.PIECE*t.GROSS_SINGLE) as zongmao," +     // 毛重
                " s.RECEIVER_NAME as shouhuokehu " +
                " FROM BIS_TRANSFER_STOCK s " +
                " JOIN " +
                " (SELECT sq.TYPE_NAME as TYPE_NAME, " +
                " sq.NET_SINGLE as NET_SINGLE, " +
                " sq.GROSS_SINGLE AS GROSS_SINGLE, " +
                " a.TRANSFER_LINK_ID AS TRANSFER_LINK_ID, " +
                " a.PIECE AS PIECE, " +
                " a.CARGO_NAME AS CARGO_NAME, " +
                " a.SKU_ID AS SKU_ID, " +
                " a.CTN_NUM AS CTN_NUM, " +
                " a.BILL_NUM AS BILL_NUM " +
                " FROM BASE_SKU_BASE_INFO sq " +
                " JOIN BIS_TRANSFER_STOCK_TRAYINFO a on sq.SKU_ID = a.SKU_ID " +
                " ) t " +
                " ON t.TRANSFER_LINK_ID = s.TRANSFER_ID ");

        if (BILL_NUM != null && !"".equals(BILL_NUM)) {//提单号
            sql.append(" AND t.BILL_NUM =:BILL_NUM ");
            params.put("BILL_NUM", BILL_NUM);
        }

        if (STOCK_NAME != null && !"".equals(STOCK_NAME)) {//--客户
            sql.append(" and s.RECEIVER_NAME =:STOCK_NAME ");
            params.put("STOCK_NAME", STOCK_NAME);
        }

        if (CTN_NUM != null && !"".equals(CTN_NUM)) {//箱号
            sql.append(" and t.CTN_NUM=:CTN_NUM ");
            params.put("CTN_NUM", CTN_NUM);
        }

        if (starTime != null && !"".equals(starTime)) {//开始时间
            sql.append(" and s.OPERATE_TIME>=to_date(:starTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("starTime", starTime);
        }

        if (endTime != null && !"".equals(endTime)) {//结束时间
            sql.append(" and s.OPERATE_TIME<to_date(:endTime,'yyyy-mm-dd,hh24:mi:ss') ");
            params.put("endTime", endTime);
        }

        sql.append(" GROUP BY s.RECEIVER_NAME, " +
                " t.BILL_NUM,t.CTN_NUM, " +
                " t.SKU_ID, " +
                " t.CARGO_NAME, " +
                " t.TYPE_NAME, " +
                " s.OPERATE_TIME, " +
                " t.PIECE, " +
                " t.NET_SINGLE, " +
                " t.GROSS_SINGLE, " +
                " s.OLD_OWNER ");

        System.err.println("Excel - " + sql.toString());

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
