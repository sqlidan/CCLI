package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.web.scm.ScmDict;
import org.hibernate.SQLQuery;
import org.hibernate.transform.BasicTransformerAdapter;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
@Repository
public class ScmDictDao extends HibernateDao<ScmDict, String> {
    //全量查询实时库存
    public List<Map<String,Object>> queryFullInventoryData(String startTime, String endTime){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ");
        sql.append(" '青岛港怡之航冷链物流有限公司' as warehouseName, ");//String-仓库名称-是-
        sql.append(" '91370220395949850B' as warehouseCode, ");//String-仓库税号-是-
        sql.append(" b.CLIENT_NAME as customerName, ");//String-客户全称-是-
        sql.append(" b.TAX_ACCOUNT as socialCreditCode, ");//String-客户统一社会信用代码-是-
        sql.append(" '0' as isFutures, ");//String-是否期货：-是-配置字典项:is_futures
        sql.append(" '普货' as isFuturesLabel, ");//String-是否期货：-是-字典项：is_futures
        sql.append(" '1' as businessType, ");//String-业务类型，字典项编码-是-字典项：business_type
        sql.append(" '进口' as businessTypeLabel, ");//String-业务类型，字典项标签-是-字典项：business_type
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then 'BS' else 'FBS' end as tradeType, ");//String-贸易类型，字典项编码-是-字典项：trade_type
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then '保税' else '非保税' end as tradeTypeLabel, ");//String-贸易类型，字典项标签-是-字典项：trade_type
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then '4' else '1' end as tradeMode, ");//String-贸易方式，字典项编码-否-字典项：trade_mode
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then '保税仓库货物' else '一般贸易' end as tradeModeLabel, ");//String-贸易方式，字典项标签-否-字典项：trade_mode
        sql.append(" tray.BILL_NUM as masterBillNo, ");//String-提单号-是-用来索引各环节仓储作业记录
        sql.append(" '' as futuresReceiptNo, ");//String-期货仓单号-否-期货业务场景下传值
        sql.append(" '' as futuresReceiptStatus, ");//String-期货仓单状态-否-字典项：product_status
        sql.append(" tray.CTN_NUM as containerNo, ");//String-箱号-否-整箱堆存场景下传值
        sql.append(" tray.TRAY_ID as trayNo, ");//String-托盘号-否-
        sql.append(" '' as lotNo, ");//String-批次号-否-
        sql.append(" 'QT' as productType, ");//String-货种，字典项编码-是-字典项：product_type
        sql.append(" '其他' as productTypeLabel, ");//String-货种，字典项标签-是-字典项：product_type
        sql.append(" tray.CARGO_NAME as productName, ");//String-商品名称-是-
        sql.append(" tray.CARGO_TYPE as productBrand, ");//String-规格型号-是-包含商品的品牌、品级、规格等信息
        sql.append(" tray.NOW_PIECE as countL1, ");//BigDecimal-在库数量 (一级)-是-最多支持小数点后2位
        sql.append(" tray.NOW_PIECE as countL2, ");//BigDecimal-在库数量（二级）-是-最多支持小数点后2位
        sql.append(" tray.GROSS_WEIGHT as grossWeight, ");//BigDecimal-在库毛重-是-最多支持小数点后3位
        sql.append(" tray.NET_WEIGHT as netWeight, ");//BigDecimal-在库净重-是-最多支持小数点后3位
        sql.append(" 0.000 as volume, ");//BigDecimal-在库体积-是-最多支持小数点后3位
        sql.append(" 0.000 as length, ");//BigDecimal-在库长度-是-最多支持小数点后3位
        sql.append(" '11' as countOneUnit, ");//String-数量一级单位，字典项编码-是-字典项：unit
        sql.append(" '件' as countOneUnitLabel, ");//String-数量一级单位，字典项标签-是-字典项：unit
        sql.append(" '11' as countTwoUnit, ");//String-数量二级单位，字典项编码-是-字典项：unit
        sql.append(" '件' as countTwoUnitLabel, ");//String-数量二级单位，字典项标签-是-字典项：unit
        sql.append(" '35' as weightUnit, ");//String-重量单位，字典项编码-是-字典项：unit
        sql.append(" '千克' as weightUnitLabel, ");//String-重量单位，字典项标签-是-字典项：unit
        sql.append(" '113' as volumeUnit, ");//String-体积单位，字典项编码-是-字典项：unit
        sql.append(" '立方米' as volumeUnitLabel, ");//String-体积单位，字典项标签-是-字典项：unit
        sql.append(" '30' as lengthUnit, ");//String-长度单位，字典项编码-是-字典项：unit
        sql.append(" '米' as lengthUnitLabel, ");//String-长度单位，字典项标签-是-字典项：unit
        sql.append(" '' as productionDate, ");//Date-商品生产日期-否-格式：yyyy-MM-dd
        sql.append(" tray.ENTER_TALLY_TIME as inboundTime, ");//Date-实际入库时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" TO_NUMBER(tray.FLOOR_NUM) as storageLocationKq, ");//String-在库库区-是-存放位置的一级
        sql.append(" TO_NUMBER(tray.ROOM_NUM) as storageLocationCk, ");//String-在库仓库-否-存放位置的二级
        sql.append(" TO_NUMBER(tray.AREA_NUM) as storageLocationQy, ");//String-在库区域-否-存放位置的三级
        sql.append(" STOREROOM_NUM as storageLocationKw, ");//String-在库库位-是-存放位置的四级
        sql.append(" tray.ENTER_TALLY_TIME as operateTime ");//Date-业务操作时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" FROM bis_tray_info tray ");
        sql.append(" left join BIS_ENTER_STOCK a on tray.CONTACT_NUM = a.LINK_ID ");
        sql.append(" left join BASE_CLIENT_INFO b on a.STOCK_ID = b.IDS ");
        sql.append(" where 1=1 AND tray.NOW_PIECE > 0 ");
        sql.append(" AND tray.CARGO_STATE = '01' ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
//        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String[] originalAliases = {
                "warehouseName", "warehouseCode", "customerName", "socialCreditCode",
                "isFutures", "isFuturesLabel", "businessType", "businessTypeLabel",
                "tradeType", "tradeTypeLabel", "tradeMode", "tradeModeLabel",
                "masterBillNo", "futuresReceiptNo", "futuresReceiptStatus", "containerNo",
                "trayNo", "lotNo", "productType", "productTypeLabel", "productName",
                "productBrand", "countL1", "countL2", "grossWeight", "netWeight",
                "volume", "length", "countOneUnit", "countOneUnitLabel", "countTwoUnit",
                "countTwoUnitLabel", "weightUnit", "weightUnitLabel", "volumeUnit",
                "volumeUnitLabel", "lengthUnit", "lengthUnitLabel", "productionDate",
                "inboundTime", "storageLocationKq", "storageLocationCk", "storageLocationQy",
                "storageLocationKw", "operateTime"
        };
        // 使用自定义转换器保持别名的原始大小写
        return sqlQuery.setResultTransformer(new BasicTransformerAdapter() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                Map<String, Object> result = new HashMap<>(tuple.length);
                for (int i = 0; i < tuple.length; i++) {
                    // 直接使用原始别名作为key，不做大小写转换
                    result.put(aliases[i], tuple[i]);
                }
                return result;
            }
        }).list();
    }

    //查询增量推送入库订单明细
    public List<Map<String,Object>> queryInboundOrderAddList(String startTime, String endTime){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ");
        sql.append(" c.CLIENT_NAME as customerName, ");//String-客户全称-是-
        sql.append(" c.TAX_ACCOUNT as socialCreditCode, ");//String-客户统一社会信用代码-是-
        sql.append(" '1' as businessType, ");//String-业务类型，字典项编码-是-字典项：business_type。
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then 'BS' else 'FBS' end as tradeType, ");//String-贸易类型，字典项编码-是-字典项：trade_type
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then '4' else '1' end as tradeMode, ");//String-贸易方式，字典项编码-是-字典项：trade_mode
        sql.append(" b.LINK_ID as businessNo, ");//String-业务编号，代表同一个订单-是-
        sql.append(" tray.BILL_NUM as masterBillNo, ");//String-提单号-是-用来索引各环节仓储作业记录
        sql.append(" case when (nvl(b.CTN_TYPE_SIZE,'') = '散货' or nvl(b.CTN_TYPE_SIZE,'') = '散货船') then '2' else '1' end as inboundType, ");//String-入库类型，字典项编码-是-字典项：inbound_type
        sql.append(" b.CTN_TYPE_SIZE || 'RF*1' as containerCount, ");//String-箱型 / 箱量-否-多个用逗号分隔 (如 20GP×10, 40HC×20)
        sql.append(" 'QT' as productType, ");//String-货种，字典项编码-是-多个逗号分隔。字典项：product_type
        sql.append(" tray.CARGO_NAME as productName, ");//String-商品名称-是-多个逗号分隔
        sql.append(" tray.NOW_PIECE as planInboundCountL1, ");//BigDecimal-计划入库数量 (一级)-是-最多支持小数点后2位
        sql.append(" tray.NOW_PIECE as planInboundCountL2, ");//BigDecimal-计划入库数量（二级）-是-最多支持小数点后2位
        sql.append(" tray.GROSS_WEIGHT as planInboundGrossWeight, ");//BigDecimal-计划入库毛重-是-最多支持小数点后3位
        sql.append(" tray.NET_WEIGHT as planInboundNetWeight, ");//BigDecimal-计划入库净重-是-最多支持小数点后3位
        sql.append(" 0.000 as planInboundVolume, ");//BigDecimal-计划入库体积-是-最多支持小数点后3位
        sql.append(" 0.000 as planInboundLength, ");//BigDecimal-计划入库长度-是-最多支持小数点后3位
        sql.append(" b.OPERATE_TIME as operateTime ");//Date-业务操作时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" FROM bis_tray_info tray ");
        sql.append(" left join BIS_ENTER_STOCK b on tray.CONTACT_NUM = b.LINK_ID ");
        sql.append(" left join BASE_CLIENT_INFO c on b.STOCK_ID = c.IDS ");
        sql.append(" where 1=1 AND tray.NOW_PIECE > 0 ");
        sql.append(" AND b.OPERATE_TIME >= to_date(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append(" AND b.OPERATE_TIME <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append(" AND tray.CARGO_STATE = '01' ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("startTime", startTime);
        parme.put("endTime", endTime);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
//        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String[] originalAliases = {
                "customerName", "socialCreditCode", "businessType", "tradeType",
                "tradeMode", "businessNo", "masterBillNo", "inboundType",
                "containerCount", "productType", "productName", "planInboundCountL1",
                "planInboundCountL2", "planInboundGrossWeight", "planInboundNetWeight",
                "planInboundVolume", "planInboundLength", "operateTime"
        };
        // 使用自定义转换器保持别名的原始大小写
        return sqlQuery.setResultTransformer(new BasicTransformerAdapter() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                Map<String, Object> result = new HashMap<>(tuple.length);
                for (int i = 0; i < tuple.length; i++) {
                    // 直接使用原始别名作为key，不做大小写转换
                    result.put(originalAliases[i], tuple[i]);
                }
                return result;
            }
        }).list();
    }

    //查询增量推送入库明细
    public List<Map<String,Object>> queryInboundAddList(String startTime, String endTime){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ");
        sql.append(" c.CLIENT_NAME as customerName, ");//String-客户全称-是-
        sql.append(" c.TAX_ACCOUNT as socialCreditCode, ");//String-客户统一社会信用代码-是-
        sql.append(" '1' as businessType, ");//String-业务类型，字典项编码-是-字典项：business_type。
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then 'BS' else 'FBS' end as tradeType, ");//String-贸易类型，字典项编码-是-字典项：trade_type
        sql.append(" case when nvl(tray.IS_BONDED,0) = 1 then '4' else '1' end as tradeMode, ");//String-贸易方式，字典项编码-是-字典项：trade_mode
        sql.append(" b.LINK_ID as businessNo, ");//String-业务编号，代表同一个订单-是-
        sql.append(" tray.BILL_NUM as masterBillNo, ");//String-提单号-是-用来索引各环节仓储作业记录
        sql.append(" case when (nvl(b.CTN_TYPE_SIZE,'') = '散货' or nvl(b.CTN_TYPE_SIZE,'') = '散货船') then '2' else '1' end as inboundType, ");//String-入库类型，字典项编码-是-字典项：inbound_type
        sql.append(" tray.CTN_NUM as containerNo, ");//String-箱号-否-
        sql.append(" 'RF' as containerType, ");//String-箱型-否-
        sql.append(" nvl(b.CTN_TYPE_SIZE,'') as containerSize, ");//String-尺寸-否-
        sql.append(" 'QT' as productType, ");//String-货种，字典项编码-是-多个逗号分隔。字典项：product_type
        sql.append(" tray.CARGO_NAME as productName, ");//String-商品名称-是-多个逗号分隔
        sql.append(" tray.CARGO_TYPE as productBrand, ");//String-规格型号-是-包含商品的品牌、品级、规格等信息
        sql.append(" tray.NOW_PIECE as planInboundCountL1, ");//BigDecimal-计划入库数量 (一级)-是-最多支持小数点后2位
        sql.append(" tray.NOW_PIECE as planInboundCountL2, ");//BigDecimal-计划入库数量（二级）-是-最多支持小数点后2位
        sql.append(" tray.GROSS_WEIGHT as planInboundGrossWeight, ");//BigDecimal-计划入库毛重-是-最多支持小数点后3位
        sql.append(" tray.NET_WEIGHT as planInboundNetWeight, ");//BigDecimal-计划入库净重-是-最多支持小数点后3位
        sql.append(" 0.000 as planInboundVolume, ");//BigDecimal-计划入库体积-是-最多支持小数点后3位
        sql.append(" 0.000 as planInboundLength, ");//BigDecimal-计划入库长度-是-最多支持小数点后3位
        sql.append(" b.OPERATE_TIME as inboundTime, ");//Date-实际入库时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" TO_NUMBER(tray.FLOOR_NUM) as inboundKq, ");//String-入库库区-是-存放位置的一级
        sql.append(" TO_NUMBER(tray.ROOM_NUM) as inboundCk, ");//String-入库仓库-否-存放位置的二级
        sql.append(" TO_NUMBER(tray.AREA_NUM) as inboundQy, ");//String-入库区域-否-存放位置的三级
        sql.append(" STOREROOM_NUM as inboundKw, ");//String-入库库位-是-存放位置的四级
        sql.append(" b.OPERATE_TIME as operateTime ");//Date-业务操作时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" FROM bis_tray_info tray ");
        sql.append(" left join BIS_ENTER_STOCK b on tray.CONTACT_NUM = b.LINK_ID ");
        sql.append(" left join BASE_CLIENT_INFO c on b.STOCK_ID = c.IDS ");
        sql.append(" where 1=1 AND tray.NOW_PIECE > 0 ");
        sql.append(" AND b.OPERATE_TIME >= to_date(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append(" AND b.OPERATE_TIME <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append(" AND tray.CARGO_STATE = '01' ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("startTime", startTime);
        parme.put("endTime", endTime);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
//        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String[] originalAliases = {
                "customerName", "socialCreditCode", "businessType", "tradeType",
                "tradeMode", "businessNo", "masterBillNo", "inboundType",
                "containerNo", "containerType", "containerSize", "productType",
                "productName", "productBrand", "planInboundCountL1", "planInboundCountL2",
                "planInboundGrossWeight", "planInboundNetWeight", "planInboundVolume",
                "planInboundLength", "inboundTime", "inboundKq", "inboundCk",
                "inboundQy", "inboundKw", "operateTime"
        };
        // 使用自定义转换器保持别名的原始大小写
        return sqlQuery.setResultTransformer(new BasicTransformerAdapter() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                Map<String, Object> result = new HashMap<>(tuple.length);
                for (int i = 0; i < tuple.length; i++) {
                    // 直接使用原始别名作为key，不做大小写转换
                    result.put(aliases[i], tuple[i]);
                }
                return result;
            }
        }).list();
    }

    //查询增量推送出库明细
    public List<Map<String,Object>> queryOutboundAddList(String startTime, String endTime){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ");
        sql.append(" b.CLIENT_NAME as customerName, ");//String-客户全称-是-
        sql.append(" b.TAX_ACCOUNT as socialCreditCode, ");//String-客户统一社会信用代码-是-
        sql.append(" '1' as businessType, ");//String-业务类型，字典项编码-是-字典项：business_type。
        sql.append(" case when nvl(c.IF_BONDED,0) = 1 then 'BS' else 'FBS' end as tradeType, ");//String-贸易类型，字典项编码-是-字典项：trade_type
        sql.append(" case when nvl(c.IF_BONDED,0) = 1 then '4' else '1' end as tradeMode, ");//String-贸易方式，字典项编码-是-字典项：trade_mode
        sql.append(" a.OUT_LINK_ID as businessNo, ");//String-业务编号，代表同一车-是-
        sql.append(" a.BILL_NUM as masterBillNo, ");//String-提单号-是-用来索引各环节仓储作业记录
        sql.append(" 'QT' as productType, ");//String-货种，字典项编码-是-字典项：product_type
        sql.append(" a.CARGO_NAME as productName, ");//String-商品名称-是-
        sql.append(" a.CARGO_TYPE as productBrand, ");//String-规格型号-是-包含商品的品牌、品级、规格等信息
        sql.append(" a.PIECE as outboundCountL1, ");//BigDecimal-出库数量 (一级)-是-最多支持小数点后2位
        sql.append(" a.PIECE as outboundCountL2, ");//BigDecimal-出库数量（二级）- -最多支持小数点后2位
        sql.append(" a.GROSS_WEIGHT as outboundGrossWeight, ");//BigDecimal-出库毛重-是-最多支持小数点后3位
        sql.append(" a.NET_WEIGHT as outboundNetWeight, ");//BigDecimal-出库净重-是-最多支持小数点后3位
        sql.append(" 0.000 as outboundVolume, ");//BigDecimal-出库体积-是-最多支持小数点后3位
        sql.append(" 0.000 as outboundLength, ");//BigDecimal-出库长度-是-最多支持小数点后3位
        sql.append(" a.LOADING_TIME as outboundTime, ");//Date-实际出库时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" TO_NUMBER(a.FLOOR_NUM) as outboundKq, ");//String-出库库区-是-存放位置的一级
        sql.append(" TO_NUMBER(a.ROOM_NUM) as outboundCk, ");//String-出库仓库-否-存放位置的二级
        sql.append(" TO_NUMBER(a.AREA_NUM) as outboundQy, ");//String-出库区域-否-存放位置的三级
        sql.append(" a.STOREROOM_NUM as outboundKw, ");//String-出库库位-是-存放位置的四级
        sql.append(" a.LIBRARY_OPE_TIME as operateTime ");//Date-业务操作时间-是-格式：yyyy-MM-dd HH:mm:ss
        sql.append(" FROM BIS_LOADING_INFO a ");
        sql.append(" left join BASE_CLIENT_INFO b on a.STOCK_ID = b.IDS ");
        sql.append(" left join BIS_OUT_STOCK c on a.OUT_LINK_ID = c.OUT_LINK_ID ");
        sql.append(" where 1=1 and a.PIECE > 0 ");
        sql.append(" AND a.LOADING_TIME >= to_date(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append(" AND a.LOADING_TIME <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
        sql.append("  AND a.LOADING_STATE = '2' ");
        HashMap<String,Object> parme=new HashMap<String,Object>();
        parme.put("startTime", startTime);
        parme.put("endTime", endTime);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(),parme);
//        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String[] originalAliases = {
                "customerName", "socialCreditCode", "businessType", "tradeType",
                "tradeMode", "businessNo", "masterBillNo", "productType",
                "productName", "productBrand", "outboundCountL1", "outboundCountL2",
                "outboundGrossWeight", "outboundNetWeight", "outboundVolume",
                "outboundLength", "outboundTime", "outboundKq", "outboundCk",
                "outboundQy", "outboundKw", "operateTime"
        };
        // 使用自定义转换器保持别名的原始大小写
        return sqlQuery.setResultTransformer(new BasicTransformerAdapter() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                Map<String, Object> result = new HashMap<>(tuple.length);
                for (int i = 0; i < tuple.length; i++) {
                    // 直接使用原始别名作为key，不做大小写转换
                    result.put(aliases[i], tuple[i]);
                }
                return result;
            }
        }).list();
    }
}
