package com.haiersoft.ccli.report.dao;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.PageUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;


@Repository
public class StockReportDao extends HibernateDao<Stock, String> {

    /**
     * @param page
     * @param expenseScheme
     * @return
     * @throws
     * @author Connor.M
     * @Description: 分页查询
     * @date 2016年3月2日 下午5:11:21
     */
    public Page<Stock> searchStockReport(Page<Stock> page, Stock stock) {
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
                + " sum(t.now_piece)over(partition by null) as allpiece, "
                + " sum(t.now_piece * t.net_single)over(partition by null) as allnet, "
                + " sum(t.now_piece * t.gross_single)over(partition by null) as allgross, "
                + " st.BGDHDATE as bgdhdate, "
                + " st.OPERATOR as createUser, "
                + " nvl(t.ACTUAL_STOREROOM_X, 0) || '_' || nvl(t.ACTUAL_STOREROOM_Z, 0) AS xz, "
                + " months_between(sysdate,st.BGDHDATE) days,t.is_bonded as isBonded,t.UPLOADER as  uploader,t.UPLOAD_DATE  as uploadDate "
               + " FROM bis_tray_info t "
                + " LEFT JOIN BIS_ENTER_STOCK st ON "
                + " T.CONTACT_NUM=ST.LINK_ID "   
                + " WHERE 1 = 1 AND t.NOW_PIECE > 0 ");//20170821 增加库存大于0的条件

        
        if(!StringUtils.isNull(stock.getCreateUser())){
        	sql.append(" and st.OPERATOR like :createUser");
            params.put("createUser", "%"+stock.getCreateUser()+"%");
        }
        if (!StringUtils.isNull(stock.getStrartTime())) {//--开始日期
        	sql.append(" and st.BGDHDATE>=to_date(:strTime,'yyyy-MM-dd hh24:mi:ss')  ");
            params.put("strTime",stock.getStrartTime());
        }
        if (!StringUtils.isNull(stock.getEndTime())) {//--结束日期
            sql.append(" and st.BGDHDATE<to_date(:endTime,'yyyy-MM-dd hh24:mi:ss')  ");
            params.put("endTime",stock.getEndTime());
        }
        
        if (!StringUtils.isNull(stock.getAsn())) {
            sql.append(" and lower(t.ASN) like lower(:asn) ");
            params.put("asn", "%" + stock.getAsn() + "%");
        }
        if (!StringUtils.isNull(stock.getSku())) {
            sql.append(" and lower(t.SKU_ID) like lower(:sku) ");
            params.put("sku", "%" + stock.getSku() + "%");
        }
        if (!StringUtils.isNull(stock.getTrayCode())) {
            sql.append(" and lower(t.TRAY_ID) like lower(:trayCode) ");
            params.put("trayCode", "%" + stock.getTrayCode() + "%");
        }
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
        if (!StringUtils.isNull(stock.getContactCode())) {
            sql.append(" and lower(t.CONTACT_NUM) like lower(:contactCode) ");
            params.put("contactCode", "%" + stock.getContactCode() + "%");
        }
        if (!StringUtils.isNull(stock.getState())) {
            sql.append(" and t.CARGO_STATE = :state ");
            params.put("state", stock.getState());
        }
        if (!StringUtils.isNull(stock.getWarehouseId())) {
            sql.append(" and t.WAREHOUSE_ID = '" + stock.getWarehouseId() + "' ");
        }
        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("trayCode", String.class);
        parm.put("billCode", String.class);
        parm.put("ctnNum", String.class);
        parm.put("bgdhdate", String.class);
        parm.put("createUser", String.class);
        parm.put("asn", String.class);
        parm.put("sku", String.class);
        parm.put("contactCode", String.class);
        parm.put("clientName", String.class);
        parm.put("locationCode", String.class);
        parm.put("warehouse", String.class);//仓库名 gzq 20160627 添加
        //parm.put("warehouseId", String.class);//仓库名 gzq 20160627 添加
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
        parm.put("days", Integer.class);
        parm.put("isBonded",  String.class);
        parm.put("uploader",  String.class);
        parm.put("uploadDate", Date.class);
        parm.put("xz", String.class);

        return findPageSql(page, sql.toString(), parm, params);
    }

    /**
     * 在库明细--普通客户
     *
     * @param itemNum      提单号
     * @param cunNum       厢号
     * @param stockIn      客户id
     * @param linkId       联系单号
     * @param strTime      入库时间开始
     * @param endTime      入库时间结束
     * @param locationType
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotPT(Integer ntype,String ifBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime, String locationType) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sb=new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" tray.stock_in AS clientId,");
        sb.append(" tray.stock_name AS clientName,");
        sb.append(" (CASE st.IF_BONDED WHEN '1' THEN '保税货物' ELSE '非保税货物' END ) AS isBonded,");
        sb.append(" tray.bill_num AS billCode, ");
        sb.append(" tray.ctn_num AS ctnNum, ");
        sb.append(" nvl(info.bgdh,st.bgdh) as bgdh,");
        sb.append(" nvl(info.ycg,st.ycg) as ycg,");
        sb.append(" TO_CHAR (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd') AS bgdhdate,");
        sb.append(" tray.sku_id AS sku, ");
        sb.append(" st.CTN_TYPE_SIZE AS cz, ");
        sb.append(" s.type_name AS bigName, ");
        sb.append(" s.cargo_type AS bigType, ");
        sb.append(" s.class_name AS simName, ");
        sb.append(" s.class_type AS simType, ");
        sb.append(" tray.CARGO_NAME AS cargoName,");
        sb.append(" tray.rkTime, ");
        sb.append(" tray.enter_state AS state, ");
        sb.append(" SUM (tray.num) AS nowNum, ");
        sb.append(" round(SUM (tray.net_weight),2) AS allnet, ");
        sb.append(" round(SUM (tray.gross_weight),2) AS allgross, ");
        sb.append(" tray.CONTACT_NUM AS contactCode,  ");
        sb.append(" st. OPERATOR AS createUser,  ");
        sb.append(" tray.asn ");
        if ("1".equals(locationType)) {
            sb.append(" ,tray.cargo_location AS locationCode ");
        }
        if(2==ntype){
        	sb.append(" ,ai.rk_num as rkNum  ");
        }
        if(3==ntype){
        	sb.append(" ,ai.pro_time as makeTime ");
        	sb.append(" ,s.shipnum as shipNum ");
        	sb.append("	,s.type_size as typeSize ");
        	sb.append("	,s.pro_num as projectNum ");
        	sb.append("	,s.lot_num as lotNum");
        	sb.append("	,s.msc_num as mscNum ");
        	sb.append("	,a.order_num as orderNum ");
        	
        }
       // sb.append("	,info.HS_CODE as hsCode,	info.ACCOUNT_BOOK as accountBook, info.HS_ITEMNAME as hsItemname");
        sb.append(",decode(info.HS_CODE,NULL,ai.HS_CODE,info.HS_CODE) as hsCode, ");
        sb.append(" decode(info.ACCOUNT_BOOK,NULL,ai.ACCOUNT_BOOK ,info.ACCOUNT_BOOK) as accountBook, ");
        sb.append(" decode(info.HS_ITEMNAME,NULL,ai.HS_ITEMNAME,info.HS_ITEMNAME) as hsItemname");
        sb.append(" FROM(  ");
        sb.append("  SELECT   ");
        sb.append("    tray.stock_in,  ");
        sb.append("    tray.stock_name, ");
        sb.append("    tray.bill_num, ");
        sb.append("    tray.ctn_num,");
        sb.append("    tray.sku_id,");
        sb.append("    SUM (tray.now_piece) AS num, ");
        sb.append("    (CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state,");
        sb.append("    tray.CARGO_NAME,   ");
        sb.append("    SUM (tray.net_weight) AS net_weight, ");
        sb.append("    SUM (tray.gross_weight) AS gross_weight,   ");
        sb.append("    tray.CONTACT_NUM,   ");
        sb.append("    to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd') AS rkTime, ");
        sb.append("    tray.asn,  ");
        sb.append("    tray.cargo_location  ");
        sb.append(" FROM   ");
        sb.append("    BIS_TRAY_INFO tray ");
        sb.append(" WHERE 1 = 1 AND (tray.cargo_state = '01' OR tray.cargo_state = '10')  ");
        sb.append(" AND tray.now_piece != 0  ");
        sb.append(" GROUP BY ");
        sb.append("   tray.stock_in, ");
        sb.append("   tray.stock_name, ");
        sb.append("   tray.bill_num, ");
        sb.append("   tray.ctn_num, ");
        sb.append("   tray.sku_id, ");
        sb.append("   tray.enter_state, ");
        sb.append("   tray.CARGO_NAME, ");
        sb.append("   tray.CONTACT_NUM,  ");
        sb.append("   tray.asn,  ");
        sb.append("   tray.enter_state, ");
        sb.append("   to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd'), ");
        sb.append("   tray.cargo_location  ");
        sb.append("  ) tray   ");
        sb.append(" LEFT JOIN (  ");
        sb.append("   SELECT  ");
        sb.append(" 	ba.asn, ");
        sb.append(" 	TRUNC (ba.inbound_date) AS inbound_date,  ");
        sb.append(" 	ba.ctn_num, ");
        sb.append(" 	ba.bill_num, ");
        sb.append("     ba.ORDER_NUM, ");
        sb.append("     ba.stock_in ");
        sb.append(" FROM  ");
        sb.append(" 	bis_asn ba ");
        sb.append(" ) A ON (tray.asn = A.asn AND A.ctn_num = tray.ctn_num AND A.bill_num = tray.bill_num)        ");
        sb.append(" LEFT JOIN bis_asn_info ai ON (ai.asn_id =tray.asn AND ai.sku_id=tray.sku_id)                 ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (  ");
        sb.append(" 	tray.bill_num = st.ITEM_NUM ");
        sb.append(" 	AND tray.CONTACT_NUM = st.LINK_ID ");
        sb.append(" )     ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON ( ");
        sb.append(" 	st.ITEM_NUM = info.ITEM_NUM  ");
        sb.append(" 	AND st.LINK_ID = info.LINK_ID ");
        sb.append(" 	AND tray.ctn_num = info.ctn_num ");
        sb.append(" 	AND tray.sku_id = info.sku ");
        sb.append(" )   ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id       ");
        sb.append(" where 1=1 ");
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and to_date(tray.rkTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and to_date(tray.rkTime,'yyyy-mm-dd')<=to_date(:endTime,'yyyy-mm-dd')");
            parme.put("endTime", endTime);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and tray.bill_num=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and tray.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and tray.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and tray.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND st.IF_BONDED=:ifBonded");
        		parme.put("ifBonded",ifBonded);
        	}else{
        		sb.append(" AND (st.IF_BONDED='0' or st.IF_BONDED is null)    ");
        	}
        }
        sb.append(" GROUP BY ");
        sb.append(" tray.stock_in,  ");
        sb.append(" tray.stock_name, ");
        sb.append(" st.IF_BONDED, ");
        sb.append(" tray.bill_num, ");
        sb.append(" tray.ctn_num, ");
        sb.append(" info.bgdh, ");
        sb.append(" info.ycg, ");
        sb.append(" st.bgdh,");
        sb.append(" st.ycg,");
        sb.append(" to_char (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd'),");
        sb.append(" tray.sku_id,  ");
        sb.append(" st.CTN_TYPE_SIZE, ");
        sb.append(" s.type_name,  ");
        sb.append(" s.cargo_type, ");
        sb.append(" s.class_name, ");
        sb.append(" s.class_type, ");
        sb.append(" tray.CARGO_NAME, ");
        sb.append(" tray.rkTime,");
        sb.append(" tray.enter_state,");
        sb.append(" tray.CONTACT_NUM, ");
        sb.append(" st.OPERATOR, ");
        sb.append(" tray.asn   ");
        if ("1".equals(locationType)) {
        	sb.append(" ,tray.cargo_location   ");
        }
        if(2==ntype){
        	sb.append(" ,ai.rk_num ");
        }
        if(3==ntype){
        	sb.append(" ,ai.pro_time ");
        	sb.append(" ,s.shipnum ");
        	sb.append("	,s.type_size ");
        	sb.append("	,s.pro_num ");
        	sb.append("	,s.lot_num ");
        	sb.append("	,s.msc_num ");
        	sb.append("	,a.order_num ");
        }
        sb.append(",info.HS_CODE,info.ACCOUNT_BOOK,info.HS_ITEMNAME ");
        sb.append(" ,ai.HS_CODE,ai.HS_ITEMNAME,ai.ACCOUNT_BOOK ");
        sb.append(" ORDER BY  ");
        sb.append(" tray.bill_num, ");
        sb.append(" tray.ctn_num, ");
        sb.append(" tray.rkTime  ");
        if(2==ntype){
        	sb.append(" ,ai.rk_num  ");
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 在库明细--JP客户
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotJP(String ifBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime, String locationType) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("SELECT                                                          ");
        sb.append("	c.client_name,                                                 ");
        sb.append("	aa.stock_in,                                                   ");
        sb.append("	aa.rk_num,                                                     ");
        sb.append("	aa.bill_num,                                                   ");
        sb.append("	aa.ctn_num,                                                    ");
        sb.append("	aa.sku_id,                                                     ");
        sb.append("	s.cargo_name,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" aa.cargo_location, ");
        }
        sb.append("	aa.is_bonded,                                                  ");
        sb.append("	aa.inbound_date,                                               ");
        sb.append("	aa.enter_state,                                                ");
        sb.append("	aa.now_piece_sum,                                              ");
        sb.append("	round(aa.net_weight_sum, 2) AS net_weight_sum,                 ");
        sb.append("	round(aa.gross_weight_sum, 2) AS gross_weight_sum              ");
        sb.append("FROM                                                            ");
        sb.append("	(                                                              ");
        sb.append("		SELECT                                                       ");
        sb.append("			ai.rk_num,                                                 ");
        sb.append("			a.is_bonded,                                               ");
        sb.append("			a.inbound_date,                                            ");
        sb.append("			t.stock_in,                                                ");
        sb.append("			t.bill_num,                                                ");
        sb.append("			t.ctn_num,                                                 ");
        sb.append("			t.sku_id,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" t.cargo_location, ");
        }
        sb.append("			(                                                          ");
        sb.append("				CASE t.enter_state                                       ");
        sb.append("				WHEN '0' THEN                                            ");
        sb.append("					'INTACT'                                               ");
        sb.append("				WHEN '1' THEN                                            ");
        sb.append("					'BROKEN'                                               ");
        sb.append("				WHEN '2' THEN                                            ");
        sb.append("					'COVER TORN'                                           ");
        sb.append("				END                                                      ");
        sb.append("			) AS enter_state,                                          ");
        sb.append("			sum(t.now_piece) AS now_piece_sum,                         ");
        sb.append("			sum(t.net_weight) AS net_weight_sum,                       ");
        sb.append("			sum(t.gross_weight) AS gross_weight_sum                    ");
        sb.append("		FROM                                                         ");
        sb.append("			(                                                          ");
        sb.append("				SELECT                                                   ");
        sb.append("					bt.sku_id,                                             ");
        sb.append("					bt.asn,                                                ");
        sb.append("					bt.bill_num,                                           ");
        sb.append("					bt.stock_in,                                           ");
        sb.append("					bt.ctn_num,                                            ");
        if ("1".equals(locationType)) {
            sb.append(" bt.cargo_location, ");
        }
        sb.append("					bt.enter_state,                                        ");
        sb.append("					bt.now_piece,                                          ");
        sb.append("					bt.net_weight,                                         ");
        sb.append("					bt.gross_weight                                        ");
        sb.append("				FROM                                                     ");
        sb.append("					BIS_TRAY_INFO bt                                       ");
        sb.append("				WHERE                                                    ");
        sb.append("					(                                                      ");
        sb.append("						bt.cargo_state = '01'                                ");
        sb.append("						OR bt.cargo_state = '10'                             ");
        sb.append("					)                                                      ");
        sb.append("				AND bt.now_piece != 0                                    ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and bt.bill_num=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and bt.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and bt.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        sb.append("			) t                                                        ");
        sb.append("		INNER JOIN (                                                 ");
        sb.append("			SELECT                                                     ");
        sb.append("				ba.asn,                                                  ");
        sb.append("				ba.is_bonded,                                            ");
        sb.append("				trunc (ba.inbound_date) AS inbound_date,                 ");
        sb.append("				ba.ctn_num,                                              ");
        sb.append("				ba.bill_num,                                             ");
        sb.append("				ba.stock_in                                              ");
        sb.append("			FROM                                                       ");
        sb.append("				bis_asn ba                                               ");
        sb.append("			WHERE                                                      ");
        sb.append("				1 = 1                                                    ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and ba.bill_num=:billnum1  ");
            parme.put("billnum1", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and ba.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("		) a ON t.asn = a.asn                                         ");
        sb.append("		AND a.ctn_num = t.ctn_num                                    ");
        sb.append("		AND a.stock_in = t.stock_in                                  ");
        sb.append("		AND a.bill_num = t.bill_num                                  ");
        sb.append("		LEFT JOIN bis_asn_info ai ON ai.asn_id = a.asn               ");
        sb.append("		AND ai.sku_id = t.sku_id                                     ");
        sb.append("		GROUP BY                                                     ");
        sb.append("			ai.rk_num,                                                 ");
        sb.append("			t.stock_in,                                                ");
        sb.append("			t.bill_num,                                                ");
        sb.append("			t.ctn_num,                                                 ");
        sb.append("			t.sku_id,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" t.cargo_location, ");
        }
        sb.append("			a.is_bonded,                                               ");
        sb.append("			a.inbound_date,                                            ");
        sb.append("			t.enter_state                                              ");
        sb.append("	) aa                                                           ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in             ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id          ");
        sb.append(" where 1=1  ");
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND aa.is_bonded='"+ifBonded+"'");
        	}else{
        		sb.append(" AND (aa.is_bonded ='0' or aa.is_bonded is null)    ");
        	}
        }
        sb.append("ORDER BY                                                        ");
        sb.append("	aa.bill_num,                                                   ");
        sb.append("	aa.ctn_num,                                                    ");
        sb.append("	aa.inbound_date,                                               ");
        sb.append("	aa.rk_num                                                      ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 在库明细--ote客户
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findRepotOTE(String ifBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime, String locationType) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb=new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("SELECT                                                          ");
        sb.append("	c.client_name,                                                 ");
        sb.append("	aa.stock_in,                                                   ");
        sb.append("	aa.bill_num,                                                   ");
        sb.append("	aa.ctn_num,                                                    ");
        sb.append("	aa.sku_id,                                                     ");
        sb.append("	s.cargo_name,                                                  ");
        sb.append("	s.CLASS_NAME,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" aa.cargo_location, ");
        }
        sb.append("	aa.pro_time,                                                   ");
        sb.append("	aa.is_bonded,                                                  ");
        sb.append("	aa.inbound_date,                                               ");
        sb.append("	aa.enter_state,                                                ");
        sb.append("	aa.now_piece_sum,                                              ");
        sb.append("	round(aa.net_weight_sum, 2) AS net_weight_sum,                 ");
        sb.append("	round(aa.gross_weight_sum, 2) AS gross_weight_sum,             ");
        sb.append("	s.type_size,                                                   ");
        sb.append("	s.pro_num,                                                     ");
        sb.append("	s.lot_num,                                                     ");
        sb.append("	s.msc_num,                                                     ");
        sb.append("	ei.order_num                                                   ");
        sb.append("FROM                                                            ");
        sb.append("	(                                                              ");
        sb.append("		SELECT                                                       ");
        sb.append("			ai.pro_time,                                               ");
        sb.append("			a.is_bonded,                                               ");
        sb.append("			a.inbound_date,                                            ");
        sb.append("			t.stock_in,                                                ");
        sb.append("			t.bill_num,                                                ");
        sb.append("			t.ctn_num,                                                 ");
        sb.append("			t.sku_id,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" t.cargo_location,   ");
        }
        sb.append("			(                                                          ");
        sb.append("				CASE t.enter_state                                       ");
        sb.append("				WHEN '0' THEN                                            ");
        sb.append("					'INTACT'                                               ");
        sb.append("				WHEN '1' THEN                                            ");
        sb.append("					'BROKEN'                                               ");
        sb.append("				WHEN '2' THEN                                            ");
        sb.append("					'COVER TORN'                                           ");
        sb.append("				END                                                      ");
        sb.append("			) AS enter_state,                                          ");
        sb.append("			sum(t.now_piece) AS now_piece_sum,                         ");
        sb.append("			sum(t.net_weight) AS net_weight_sum,                       ");
        sb.append("			sum(t.gross_weight) AS gross_weight_sum                    ");
        sb.append("		FROM                                                         ");
        sb.append("			(                                                          ");
        sb.append("				SELECT                                                   ");
        sb.append("					bt.bill_num,                                           ");
        sb.append("					bt.ctn_num,                                            ");
        sb.append("					bt.sku_id,                                             ");
        sb.append("					bt.asn,                                                ");
        sb.append("					bt.stock_in,                                           ");
        if ("1".equals(locationType)) {
            sb.append(" bt.cargo_location, ");
        }
        sb.append("					bt.enter_state,                                        ");
        sb.append("					bt.now_piece,                                          ");
        sb.append("					bt.net_weight,                                         ");
        sb.append("					bt.gross_weight                                        ");
        sb.append("				FROM                                                     ");
        sb.append("					BIS_TRAY_INFO bt                                       ");
        sb.append("				WHERE                                                    ");
        sb.append("					(                                                      ");
        sb.append("						bt.cargo_state = '01'                                ");
        sb.append("						OR bt.cargo_state = '10'                             ");
        sb.append("					)                                                      ");
        sb.append("				AND bt.now_piece != 0                                    ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and bt.bill_num=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and bt.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and bt.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        sb.append("			) t                                                        ");
        sb.append("		INNER JOIN (                                                 ");
        sb.append("			SELECT                                                     ");
        sb.append("				ba.bill_num,                                             ");
        sb.append("				ba.ctn_num,                                              ");
        sb.append("				ba.asn,                                                  ");
        sb.append("				ba.is_bonded,                                            ");
        sb.append("				trunc (ba.inbound_date) AS inbound_date,                 ");
        sb.append("				ba.stock_in                                              ");
        sb.append("			FROM                                                       ");
        sb.append("				bis_asn ba                                               ");
        sb.append("			WHERE                                                      ");
        sb.append("				1 = 1                                                    ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and ba.bill_num=:billnum1  ");
            parme.put("billnum1", itemNum);
        }
        /*if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sql.append(" and ba.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }*/
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("		) a ON t.asn = a.asn                                         ");
        sb.append("		AND a.ctn_num = t.ctn_num                                    ");
        sb.append("		AND a.bill_num = t.bill_num                                  ");
        sb.append("		LEFT JOIN bis_asn_info ai ON ai.asn_id = a.asn               ");
        sb.append("		AND ai.sku_id = t.sku_id                                     ");
        sb.append("		GROUP BY                                                     ");
        sb.append("			ai.pro_time,                                               ");
        sb.append("			t.stock_in,                                                ");
        sb.append("			t.bill_num,                                                ");
        sb.append("			t.ctn_num,                                                 ");
        sb.append("			t.sku_id,                                                  ");
        if ("1".equals(locationType)) {
            sb.append(" t.cargo_location, ");
        }
        sb.append("			a.is_bonded,                                               ");
        sb.append("			a.inbound_date,                                            ");
        sb.append("			t.enter_state                                              ");
        sb.append("	) aa                                                           ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in             ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id          ");
        sb.append("LEFT JOIN bis_enter_stock_info ei ON ei.item_num = aa.bill_num  ");
        sb.append("AND ei.sku = aa.sku_id                                          ");
        sb.append(" where 1=1  ");
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND aa.is_bonded='"+ifBonded+"'");
        	}else{
        		sb.append(" AND (aa.is_bonded ='0' or aa.is_bonded is null)    ");
        	}
        }
        sb.append("ORDER BY                                                        ");
        sb.append("	aa.bill_num,                                                   ");
        sb.append("	aa.ctn_num,                                                    ");
        sb.append("	aa.inbound_date                                                ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 出入库明细
     *
     * @param itemNum 提单号
     * @param cunNum  厢号
     * @param stockIn 客户id
     * @param linkId  联系单号
     * @param strTime 入库时间开始
     * @param endTime 入库时间结束
     * @return
     */
    @SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String, Object>> findRepotInAndOutbyNum(String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime,int start,int end) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer row=new StringBuffer();
        row.append("select * from (select row_.*, rownum rownum_ ");
       
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("SELECT                                                                              ");
        sb.append("	c.client_name,                                                                     ");
        sb.append("	aa.stock_in AS stock_id,                                                           ");
        sb.append("	(                                                                                  ");
        sb.append("		CASE aa.if_second_enter                                                          ");
        sb.append("		WHEN '1' THEN                                                                    ");
        sb.append("			'入库'                                                                         ");
        sb.append("		WHEN '3' THEN                                                                    ");
        sb.append("			'分拣'                                                                         ");
        sb.append("		END                                                                              ");
        sb.append("	) AS leixing,                                                                      ");
        sb.append("	(                                                                                  ");
        sb.append("		CASE aa.if_second_enter                                                          ");
        sb.append("		WHEN '1' THEN                                                                    ");
        sb.append("			'1'                                                                            ");
        sb.append("		WHEN '3' THEN                                                                    ");
        sb.append("			'3'                                                                            ");
        sb.append("		END                                                                              ");
        sb.append("	) AS leixing_sort,                                                                 ");
        sb.append("	'' AS receiver_name,                                                               ");
        sb.append("	'' AS receiver_id,                                                                 ");
        sb.append("	aa.bill_num,                                                                       ");
        sb.append("	aa.ctn_num,                                                                        ");
        sb.append("	aa.sku_id,                                                                         ");
        sb.append("	s.cargo_name,                                                                      ");
        sb.append("	s.type_name,                                                                       ");
        sb.append("	s.cargo_type,                                                                      ");
        sb.append("	s.class_name,                                                                      ");
        sb.append("	s.class_type,                                                                      ");
        sb.append("	aa.cargo_location,                                                                 ");
        sb.append("	aa.is_bonded,                                                                      ");
        sb.append("	aa.inbound_date AS churuku_date,                                                   ");
        sb.append("	aa.enter_state,                                                                    ");
        sb.append("	aa.piece_sum,                                                                      ");
        sb.append("	round(                                                                             ");
        sb.append("		aa.piece_sum * s.net_single,                                                     ");
        sb.append("		2                                                                                ");
        sb.append("	) AS net_weight_sum,                                                               ");
        sb.append("	round(                                                                             ");
        sb.append("		aa.piece_sum * s.gross_single,                                                   ");
        sb.append("		2                                                                                ");
        sb.append("	) AS gross_weight_sum                                                              ");
        sb.append("FROM                                                                                ");
        sb.append("	(                                                                                  ");
        sb.append("		SELECT                                                                           ");
        sb.append("			asnt.inbound_date,                                                             ");
        sb.append("			asnt.if_second_enter,                                                          ");
        sb.append("			asnt.is_bonded,                                                                ");
        sb.append("			t.stock_in,                                                                    ");
        sb.append("			t.bill_num,                                                                    ");
        sb.append("			t.ctn_num,                                                                     ");
        sb.append("			t.sku_id,                                                                      ");
        sb.append("			t.cargo_location,                                                              ");
        sb.append("			(                                                                              ");
        sb.append("				CASE t.enter_state                                                           ");
        sb.append("				WHEN '0' THEN                                                                ");
        sb.append("					'INTACT'                                                                   ");
        sb.append("				WHEN '1' THEN                                                                ");
        sb.append("					'BROKEN'                                                                   ");
        sb.append("				WHEN '2' THEN                                                                ");
        sb.append("					'COVER TORN'                                                               ");
        sb.append("				END                                                                          ");
        sb.append("			) AS enter_state,                                                              ");
        sb.append("			sum(                                                                           ");
        sb.append("				t.original_piece - t.remove_piece                                            ");
        sb.append("			) AS piece_sum                                                                 ");
        sb.append("		FROM                                                                             ");
        sb.append("			(                                                                              ");
        sb.append("				SELECT                                                                       ");
        sb.append("					a.asn,                                                                     ");
        sb.append("					a.is_bonded,                                                               ");
        sb.append("					a.inbound_date,                                                            ");
        sb.append("					a.ctn_num,                                                                 ");
        sb.append("					a.bill_num,                                                                ");
        sb.append("					a.stock_in,                                                                ");
        sb.append("					a.if_second_enter                                                          ");
        sb.append("				FROM                                                                         ");
        sb.append("					bis_asn a                                                                  ");
        sb.append("				WHERE                                                                        ");
        sb.append("					(a.if_second_enter = '1' OR a.if_second_enter = '3')                       ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and a.stock_in =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and a.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  a.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and a.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and a.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("			) asnt                                                                         ");
        sb.append("		INNER JOIN (                                                                     ");
        sb.append("			SELECT                                                                         ");
        sb.append("				sku_id,                                                                      ");
        sb.append("				asn,                                                                         ");
        sb.append("				bill_num,                                                                    ");
        sb.append("				stock_in,                                                                    ");
        sb.append("				ctn_num,                                                                     ");
        sb.append("				enter_state,                                                                 ");
        sb.append("				original_piece,                                                              ");
        sb.append("				remove_piece,                                                                ");
        sb.append("				ti.cargo_location                                                            ");
        sb.append("			FROM                                                                           ");
        sb.append("				BIS_TRAY_INFO ti                                                             ");
        sb.append("			WHERE                                                                          ");
        sb.append("				1 = 1                                                                        ");
        sb.append("			AND ti.if_transfer = '0'                                                       ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and bill_num=:billnumo ");
            parme.put("billnumo", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  ctn_num=:ctnnumo  ");
            parme.put("ctnnumo", cunNum);
        }
        sb.append("		) t ON t.asn = asnt.asn                                                          ");
        sb.append("		AND asnt.bill_num = t.bill_num                                                   ");
        sb.append("		GROUP BY                                                                         ");
        sb.append("			t.stock_in,                                                                    ");
        sb.append("			t.bill_num,                                                                    ");
        sb.append("			t.ctn_num,                                                                     ");
        sb.append("			t.sku_id,                                                                      ");
        sb.append("			t.enter_state,                                                                 ");
        sb.append("			asnt.is_bonded,                                                                ");
        sb.append("			asnt.inbound_date,                                                             ");
        sb.append("			t.cargo_location,                                                              ");
        sb.append("			asnt.if_second_enter                                                           ");
        sb.append("	) aa                                                                               ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in                                 ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                              ");
        sb.append("UNION ALL                                                                           ");
        sb.append("	SELECT                                                                             ");
        sb.append("		aa.stock_name,                                                                   ");
        sb.append("		aa.stock_id,                                                                     ");
        sb.append("		'出库' AS leixing,                                                               ");
        sb.append("		'4' AS leixing_sort,                                                             ");
        sb.append("		aa.receiver_name,                                                                ");
        sb.append("		aa.receiver_id,                                                                  ");
        sb.append("		aa.bill_num,                                                                     ");
        sb.append("		aa.ctn_num,                                                                      ");
        sb.append("		aa.sku_id,                                                                       ");
        sb.append("		s.cargo_name,                                                                    ");
        sb.append("		s.type_name,                                                                     ");
        sb.append("		s.cargo_type,                                                                    ");
        sb.append("		s.class_name,                                                                    ");
        sb.append("		s.class_type,                                                                    ");
        sb.append("		aa.cargo_location,                                                               ");
        sb.append("		aa.is_bonded,                                                                    ");
        sb.append("		aa.loading_tiem AS churuku_date,                                                 ");
        sb.append("		aa.enter_state,                                                                  ");
        sb.append("		aa.piece_sum,                                                                    ");
        sb.append("		net_weight_sum,                                                                  ");
        sb.append("		gross_weight_sum                                                                 ");
        sb.append("	FROM                                                                               ");
        sb.append("		(                                                                                ");
        sb.append("			SELECT                                                                         ");
        sb.append("				lor.loading_tiem,                                                            ");
        sb.append("				t.stock_id,                                                                  ");
        sb.append("				lor.stock_name,                                                              ");
        sb.append("				t.bill_num,                                                                  ");
        sb.append("				t.ctn_num,                                                                   ");
        sb.append("				t.sku_id,                                                                    ");
        sb.append("				t.cargo_location,                                                            ");
        sb.append("				a.is_bonded,                                                                 ");
        sb.append("				(                                                                            ");
        sb.append("					CASE t.enter_state                                                         ");
        sb.append("					WHEN '0' THEN                                                              ");
        sb.append("						'INTACT'                                                                 ");
        sb.append("					WHEN '1' THEN                                                              ");
        sb.append("						'BROKEN'                                                                 ");
        sb.append("					WHEN '2' THEN                                                              ");
        sb.append("						'COVER TORN'                                                             ");
        sb.append("					END                                                                        ");
        sb.append("				) AS enter_state,                                                            ");
        sb.append("				sum(t.piece) AS piece_sum,                                                   ");
        sb.append("				sum(t.net_weight) AS net_weight_sum,                                         ");
        sb.append("				sum(t.gross_weight) AS gross_weight_sum,                                     ");
        sb.append("				lo.receiver_name,                                                            ");
        sb.append("				lo.receiver_id                                                               ");
        sb.append("			FROM                                                                           ");
        sb.append("				(                                                                            ");
        sb.append("					SELECT                                                                     ");
        sb.append("						a.loading_plan_num,                                                      ");
        sb.append("						a.bill_num,                                                              ");
        sb.append("						a.ctn_num,                                                               ");
        sb.append("						a.loading_tiem,                                                          ");
        sb.append("						a.stock_id,                                                              ");
        sb.append("						a.stock_name,                                                            ");
        sb.append("						a.sku_id                                                                 ");
        sb.append("					FROM                                                                       ");
        sb.append("						bis_loading_order_info a                                                 ");
        sb.append("					WHERE                                                                      ");
        sb.append("						1 = 1                                                                    ");
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and a.stock_id =:sockid2");
            parme.put("sockid2", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and a.bill_num=:billnum2 ");
            parme.put("billnum2", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  a.ctn_num=:ctnnum2  ");
            parme.put("ctnnum2", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--出库日期
            sb.append(" and a.loading_tiem>=to_date(:strTime2,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime2", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--出库日期
            sb.append(" and a.loading_tiem<to_date(:endTime2,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime2", endTime);
        }
        sb.append("					GROUP BY                                                                   ");          
        sb.append("						a.loading_plan_num,                                                      ");          
        sb.append("						a.bill_num,                                                              ");          
        sb.append("						a.ctn_num,                                                               ");          
        sb.append("						a.loading_tiem,                                                          ");          
        sb.append("						a.stock_id,                                                              ");          
        sb.append("						a.stock_name,                                                            ");          
        sb.append("						a.sku_id                                                                 ");          
        sb.append("				) lor                                                                        ");          
        sb.append("			LEFT JOIN bis_asn a ON (                                                       ");          
        sb.append("				lor.ctn_num = a.ctn_num                                                      ");          
        sb.append("				AND lor.bill_num = a.bill_num                                                ");          
        sb.append("			)                                                                              ");          
        sb.append("			LEFT JOIN (                                                                    ");          
        sb.append("				SELECT                                                                       ");          
        sb.append("					sku_id,                                                                    ");          
        sb.append("					loading_plan_num,                                                          ");          
        sb.append("					bill_num,                                                                  ");          
        sb.append("					stock_id,                                                                  ");          
        sb.append("					ctn_num,                                                                   ");          
        sb.append("					enter_state,                                                               ");          
        sb.append("					li.piece,                                                                  ");          
        sb.append("					li.net_weight,                                                             ");          
        sb.append("					li.gross_weight,                                                           ");          
        sb.append("					li.cargo_location                                                          ");          
        sb.append("				FROM                                                                         ");          
        sb.append("					bis_loading_info li                                                        ");          
        sb.append("				WHERE                                                                        ");          
        sb.append("					li.loading_state = '2'                                                     ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and bill_num=:billnumt ");
            parme.put("billnumt", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  ctn_num=:ctnnumt  ");
            parme.put("ctnnumt", cunNum);
        }
        sb.append("			) t ON t.loading_plan_num = lor.loading_plan_num                               ");          
        sb.append("			AND lor.BILL_NUM = t.BILL_NUM                                                  ");          
        sb.append("			AND lor.SKU_ID = t.sku_id                                                      ");          
        sb.append("			LEFT JOIN bis_loading_order lo ON lo.order_num = lor.loading_plan_num          ");          
        sb.append("			WHERE                                                                          ");          
        sb.append("				lo.order_state = '4'                                                         "); 
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and lo.stock_id =:sockidt");
            parme.put("sockidt", stockIn);
        }
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        sb.append("			GROUP BY                                                                         ");          
        sb.append("				t.stock_id,                                                                  ");          
        sb.append("				t.bill_num,                                                                  ");          
        sb.append("				t.ctn_num,                                                                   ");          
        sb.append("				t.sku_id,                                                                    ");          
        sb.append("				t.enter_state,                                                               ");          
        sb.append("				lor.loading_tiem,                                                            ");          
        sb.append("				lor.stock_name,                                                              ");          
        sb.append("				lo.receiver_name,                                                            ");          
        sb.append("				lo.receiver_id,                                                              ");          
        sb.append("				a.is_bonded,                                                                 ");          
        sb.append("				t.cargo_location                                                             ");          
        sb.append("		) aa                                                                             ");          
        sb.append("	LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                             ");          
        sb.append("	UNION ALL                                                                          ");          
        sb.append("		SELECT                                                                           ");          
        sb.append("			t.stock_name AS client_name,                                                   ");          
        sb.append("			t.stock_in,                                                                    ");          
        sb.append("			'货转入' AS leixing,                                                           ");          
        sb.append("			'3' AS leixing_sort,                                                           ");          
        sb.append("			'' AS receiver_name,                                                           ");          
        sb.append("			'' AS receiver_id,                                                             ");          
        sb.append("			t.bill_num,                                                                    ");          
        sb.append("			t.ctn_num,                                                                     ");         
        sb.append("			t.sku_id,                                                                      ");         
        sb.append("			t.cargo_name,                                                                  ");         
        sb.append("			s.type_name,                                                                   ");         
        sb.append("			s.cargo_type,                                                                  ");         
        sb.append("			s.class_name,                                                                  ");         
        sb.append("			s.class_type,                                                                  ");         
        sb.append("			t.cargo_location,                                                              ");         
        sb.append("			a.is_bonded,                                                                   ");         
        sb.append("			ts.operate_time AS churuku_date,                                               ");         
        sb.append("			(                                                                              ");         
        sb.append("				CASE t.enter_state                                                           ");         
        sb.append("				WHEN '0' THEN                                                                ");         
        sb.append("					'INTACT'                                                                   ");         
        sb.append("				WHEN '1' THEN                                                                ");         
        sb.append("					'BROKEN'                                                                   ");         
        sb.append("				WHEN '2' THEN                                                                ");         
        sb.append("					'COVER TORN'                                                               ");         
        sb.append("				END                                                                          ");         
        sb.append("			) AS enter_state,                                                              ");         
        sb.append("			t.now_piece AS piece_sum,                                                      ");         
        sb.append("			t.now_piece * s.net_single AS net_weight_sum,                                  ");         
        sb.append("			t.now_piece * s.gross_single AS gross_weight_sum                               ");          
        sb.append("		FROM                                                                             ");          
        sb.append("			bis_tray_info t                                                                ");          
        sb.append("		LEFT JOIN bis_asn a ON t.asn = a.asn                                             ");          
        sb.append("		LEFT JOIN base_sku_base_info s ON s.sku_id = t.sku_id                            ");          
        sb.append("		LEFT JOIN bis_transfer_stock ts ON ts.transfer_id = t.contact_num                ");          
        sb.append("		WHERE                                                                            ");          
        sb.append("			t.if_transfer = '1' AND t.contact_type = '3'                                 ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append(" and t.stock_in =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and t.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append(" and t.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and ts.operate_time >= to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and ts.operate_time < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("		ORDER BY leixing_sort,bill_num,ctn_num                                           ");
        row.append(sb.toString());
        row.append("  ) row_) ");
        row.append("  where rownum_>"+start+" and rownum_<="+end+"");
        SQLQuery sqlQuery = createSQLQuery(row.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    /**
     * 总数量
     * @param isBonded
     * @param itemNum
     * @param cunNum
     * @param stockIn
     * @param linkId
     * @param strTime
     * @param endTime
     * @return
     */
	public BigDecimal countInAndOut(String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer count=new StringBuffer();
        count.append("select count(*) from ( ");
        sb.append(" select                                                                             ");
        sb.append("   t.client_name,                                                                   ");
        sb.append("   t.stock_id,                                                                      ");
        sb.append("   t.leixing,                                                                       ");
        sb.append("   t.leixing_sort,                                                                  ");
        sb.append("   t.receiver_name,                                                                 ");
        sb.append("   t.receiver_id,                                                                   ");
        sb.append("   t.bill_num,                                                                      ");
        sb.append("   t.ctn_num,                                                                       ");
        sb.append("   t.sku_id,                                                                        ");
        sb.append("   t.cargo_name,                                                                    ");
        sb.append("   t.type_name,                                                                     ");
        sb.append("   t.cargo_type,                                                                    ");
        sb.append("   t.class_name,                                                                    ");
        sb.append("   t.class_type,                                                                    ");
        sb.append("   t.cargo_location,                                                                ");
        sb.append("   t.is_bonded,                                                                     ");
        sb.append("   t.churuku_date,                                                                  ");
        sb.append("   t.enter_state,                                                                   ");
        sb.append("   t.piece_sum,                                                                     ");
        sb.append("   t.net_weight_sum,                                                                ");
        sb.append("   t.gross_weight_sum                                                               ");
        sb.append(" from                                                                               ");
        sb.append(" (                                                                                  ");
        sb.append(" SELECT c.client_name,                                                              ");
        sb.append("        aa.stock_in AS stock_id,                                                    ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '入库'                                                                   ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '分拣'                                                                   ");
        sb.append("        END) AS leixing,                                                            ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '1'                                                                      ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '3'                                                                      ");
        sb.append("        END) AS leixing_sort,                                                       ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.inbound_date AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        round(aa.piece_sum * s.net_single, 2) AS net_weight_sum,                    ");
        sb.append("        round(aa.piece_sum * s.gross_single, 2) AS gross_weight_sum                 ");
        sb.append("   FROM (SELECT asnt.inbound_date,                                                  ");
        sb.append("                asnt.if_second_enter,                                               ");
        sb.append("                asnt.is_bonded,                                                     ");
        sb.append("                t.stock_in,                                                         ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.original_piece - t.remove_piece) AS piece_sum                 ");
        sb.append("           FROM (SELECT a.asn,                                                      ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '1'                                     ");
        sb.append("                  union all                                                         ");
        sb.append("                  SELECT a.asn,                                                     ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '3') asnt                               ");
        sb.append("          INNER JOIN (SELECT sku_id,                                                ");
        sb.append("                            asn,                                                    ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_in,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            original_piece,                                         ");
        sb.append("                            remove_piece,                                           ");
        sb.append("                            ti.cargo_location                                       ");
        sb.append("                       FROM BIS_TRAY_INFO ti                                        ");
        sb.append("                      WHERE 1 = 1                                                   ");
        sb.append("                        AND ti.if_transfer = '0') t                                 ");
        sb.append("             ON t.asn = asnt.asn                                                    ");
        sb.append("            AND asnt.bill_num = t.bill_num                                          ");
        sb.append("          GROUP BY t.stock_in,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   asnt.is_bonded,                                                  ");
        sb.append("                   asnt.inbound_date,                                               ");
        sb.append("                   t.cargo_location,                                                ");
        sb.append("                   asnt.if_second_enter) aa                                         ");
        sb.append("   LEFT JOIN base_client_info c                                                     ");
        sb.append("     ON c.ids = aa.stock_in                                                         ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT aa.stock_name,                                                              ");
        sb.append("        aa.stock_id,                                                                ");
        sb.append("        '出库' AS leixing,                                                          ");
        sb.append("        '4' AS leixing_sort,                                                        ");
        sb.append("        aa.receiver_name,                                                           ");
        sb.append("        aa.receiver_id,                                                             ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.loading_tiem AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        net_weight_sum,                                                             ");
        sb.append("        gross_weight_sum                                                            ");
        sb.append("   FROM (SELECT lor.loading_tiem,                                                   ");
        sb.append("                t.stock_id,                                                         ");
        sb.append("                lor.stock_name,                                                     ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                a.is_bonded,                                                        ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.piece) AS piece_sum,                                          ");
        sb.append("                sum(t.net_weight) AS net_weight_sum,                                ");
        sb.append("                sum(t.gross_weight) AS gross_weight_sum,                            ");
        sb.append("                lo.receiver_name,                                                   ");
        sb.append("                lo.receiver_id                                                      ");
        sb.append("           FROM (SELECT a.loading_plan_num,                                         ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.loading_tiem,                                             ");
        sb.append("                        a.stock_id,                                                 ");
        sb.append("                        a.stock_name,                                               ");
        sb.append("                        a.sku_id                                                    ");
        sb.append("                   FROM bis_loading_order_info a                                    ");
        sb.append("                  WHERE 1 = 1                                                       ");
        sb.append("                                                                                    ");
        sb.append("                  GROUP BY a.loading_plan_num,                                      ");
        sb.append("                           a.bill_num,                                              ");
        sb.append("                           a.ctn_num,                                               ");
        sb.append("                           a.loading_tiem,                                          ");
        sb.append("                           a.stock_id,                                              ");
        sb.append("                           a.stock_name,                                            ");
        sb.append("                           a.sku_id) lor                                            ");
        sb.append("           LEFT JOIN bis_asn a                                                      ");
        sb.append("             ON (lor.ctn_num = a.ctn_num AND lor.bill_num = a.bill_num)             ");
        sb.append("           LEFT JOIN (SELECT sku_id,                                                ");
        sb.append("                            loading_plan_num,                                       ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_id,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            li.piece,                                               ");
        sb.append("                            li.net_weight,                                          ");
        sb.append("                            li.gross_weight,                                        ");
        sb.append("                            li.cargo_location                                       ");
        sb.append("                       FROM bis_loading_info li                                     ");
        sb.append("                      WHERE li.loading_state = '2') t                               ");
        sb.append("             ON t.loading_plan_num = lor.loading_plan_num                           ");
        sb.append("            AND lor.BILL_NUM = t.BILL_NUM                                           ");
        sb.append("            AND lor.SKU_ID = t.sku_id                                               ");
        sb.append("           LEFT JOIN bis_loading_order lo                                           ");
        sb.append("             ON lo.order_num = lor.loading_plan_num                                 ");
        sb.append("          WHERE lo.order_state = '4'                                                ");
        sb.append("          GROUP BY t.stock_id,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   lor.loading_tiem,                                                ");
        sb.append("                   lor.stock_name,                                                  ");
        sb.append("                   lo.receiver_name,                                                ");
        sb.append("                   lo.receiver_id,                                                  ");
        sb.append("                   a.is_bonded,                                                     ");
        sb.append("                   t.cargo_location) aa                                             ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT t.stock_name AS client_name,                                                ");
        sb.append("        t.stock_in,                                                                 ");
        sb.append("        '货转入' AS leixing,                                                        ");
        sb.append("        '3' AS leixing_sort,                                                        ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        t.bill_num,                                                                 ");
        sb.append("        t.ctn_num,                                                                  ");
        sb.append("        t.sku_id,                                                                   ");
        sb.append("        t.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        t.cargo_location,                                                           ");
        sb.append("        a.is_bonded,                                                                ");
        sb.append("        ts.operate_time AS churuku_date,                                            ");
        sb.append("        (CASE t.enter_state                                                         ");
        sb.append("          WHEN '0' THEN                                                             ");
        sb.append("           'INTACT'                                                                 ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           'BROKEN'                                                                 ");
        sb.append("          WHEN '2' THEN                                                             ");
        sb.append("           'COVER TORN'                                                             ");
        sb.append("        END) AS enter_state,                                                        ");
        sb.append("        t.now_piece AS piece_sum,                                                   ");
        sb.append("        t.now_piece * s.net_single AS net_weight_sum,                               ");
        sb.append("        t.now_piece * s.gross_single AS gross_weight_sum                            ");
        sb.append("   FROM bis_tray_info t                                                             ");
        sb.append("   LEFT JOIN bis_asn a                                                              ");
        sb.append("     ON t.asn = a.asn                                                               ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = t.sku_id                                                         ");
        sb.append("   LEFT JOIN bis_transfer_stock ts                                                  ");
        sb.append("     ON ts.transfer_id = t.contact_num                                              ");
        sb.append("  WHERE t.if_transfer = '1'                                                         ");
        sb.append("    AND t.contact_type = '3'                                                        ");
        sb.append("  )t                                                                                ");   
        count.append(sb.toString());
        count.append(" ) t");
        StringBuffer search=new StringBuffer();
        search.append(" where 1=1 ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		search.append(" AND t.is_bonded='"+isBonded+"'");
        	}else{
        		search.append(" AND (t.is_bonded ='0' or t.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	search.append("    and t.stock_id =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	search.append("   and t.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	search.append("   and  t.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	search.append(" and t.churuku_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	search.append(" and t.churuku_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        count.append(search.toString());
        SQLQuery sqlQuery = createSQLQuery(count.toString(), parme);
        return (BigDecimal) sqlQuery.uniqueResult();
	}
	
    @SuppressWarnings({ "unchecked", "unused" })
	public List<Object[]> findRepotInAndOutObject(Integer ntype,String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" SELECT ");
        sb.append(" temp.stockId,   ");
        sb.append(" temp.stockName, ");
        sb.append(" (CASE temp.isBonded  ");
        sb.append(" WHEN '1' THEN ");
        sb.append(" '保税货物' ");
        sb.append(" ELSE   ");
        sb.append(" '非保税货物' ");
        sb.append(" END   ");
        sb.append(" ) AS isBonded,");
        sb.append(" temp.reportType, ");
        sb.append(" temp.shf, ");
        sb.append(" temp.shfid,");
        sb.append(" temp.billNum,");
        sb.append(" temp.ctnNum,");
        sb.append(" temp.bgdh,");
        sb.append(" temp.ycg, ");
        sb.append(" temp.BGDHDATE,");
        sb.append(" temp.sku,");
        sb.append(" temp.cz,");
        sb.append(" temp.bigName,");
        sb.append(" temp.bigType,");
        sb.append(" temp.simName,");
        sb.append(" temp.simType,");
        if(1==ntype){
        	sb.append("sum(temp.num) as num, ");
        }else{
        	sb.append("sum(temp.num) as num, ");
        }
        sb.append(" TO_CHAR(temp.enterTime,'yyyy-mm-dd') AS enterTime, ");
        sb.append(" temp.enterStats,      ");
        sb.append(" temp.cargoName,       ");
        if(1==ntype){
        	sb.append("sum(temp.zjz) as zjz,");
        }else{
        	sb.append("sum(temp.zjz) as zjz,");
        }
        if(1==ntype){
        	sb.append("sum(temp.zmz) as zmz,");
        }else{
        	sb.append("sum(temp.zmz) as zmz,");
        }
        sb.append(" temp.linkId, ");
        sb.append(" temp.createUser, ");
        sb.append(" temp.asn ");
        if(2==ntype){
        	sb.append(" ,temp.cargo_location  ");
        }
        sb.append(" FROM  ");
        sb.append(" (SELECT ");
        sb.append(" tray.stock_in AS stockId,  ");
        sb.append(" tray.stock_name AS stockName,");
        sb.append(" (CASE st.IF_BONDED WHEN '1' THEN '1' ELSE '0' END ) AS isBonded, ");
        sb.append(" (CASE asn.if_second_enter WHEN '1' THEN '入库' WHEN '2' THEN '重收' WHEN '3' THEN '分拣' END ) AS reportType, ");
        sb.append(" tray.shf,       ");
        sb.append(" '' AS shfid,    ");
        sb.append(" tray.bill_num AS billNum,  ");
        sb.append(" tray.ctn_num AS ctnNum,   ");
        sb.append(" info.bgdh,    ");
        sb.append(" info.ycg,     ");
        sb.append(" to_char(info.BGDHDATE,'yyyy-mm-dd') AS BGDHDATE,   ");
        sb.append(" tray.sku_id AS sku,     ");
        sb.append(" st.CTN_TYPE_SIZE AS cz, ");
        sb.append(" s.type_name AS bigName, ");
        sb.append(" s.cargo_type AS bigType,");
        sb.append(" s.class_name AS simName,");
        sb.append(" s.class_type AS simType,");
        sb.append(" tray.num, ");
        sb.append(" ASN.INBOUND_DATE AS enterTime, ");
        sb.append(" tray.enter_state AS enterStats, ");
        sb.append(" tray.CARGO_NAME AS cargoName, ");
        sb.append(" tray.num * tray.NET_SINGLE AS zjz,");
        sb.append(" tray.num * tray.GROSS_SINGLE AS zmz,");
        sb.append(" tray.CONTACT_NUM AS linkId,");
        sb.append(" st. OPERATOR AS createUser,");
        sb.append(" tray.asn,  ");
        sb.append(" tray.cargo_location   ");
        sb.append(" FROM   ");
        sb.append(" (SELECT ");
        sb.append(" tray.stock_in,          ");
        sb.append(" tray.stock_name,        ");
        sb.append(" '' AS shf,              ");
        sb.append(" tray.bill_num, ");
        sb.append(" tray.ctn_num, ");
        sb.append(" tray.sku_id, ");
        sb.append(" SUM (tray.original_piece - tray.remove_piece) AS num, ");
        sb.append(" (  ");
        sb.append(" CASE tray.enter_state    ");
        sb.append(" WHEN '0' THEN  ");
        sb.append(" 'INTACT' ");
        sb.append(" WHEN '1' THEN ");
        sb.append(" 'BROKEN' ");
        sb.append(" WHEN '2' THEN  ");
        sb.append(" 'COVER TORN'      ");
        sb.append(" END  ");
        sb.append(" ) AS enter_state,");
        sb.append(" tray.CARGO_NAME,  ");
        sb.append(" MAX (tray.NET_SINGLE) AS NET_SINGLE,   ");
        sb.append(" MAX (tray.GROSS_SINGLE) AS GROSS_SINGLE, ");
        sb.append(" tray.CONTACT_NUM, ");
        sb.append(" tray.asn, ");
        sb.append(" tray.cargo_location ");
        sb.append(" FROM  ");
        sb.append(" BIS_TRAY_INFO tray    ");
        sb.append(" WHERE 1 = 1 AND tray.if_transfer = '0' ");
        sb.append(" GROUP BY      ");
        sb.append(" tray.stock_in,   ");
        sb.append(" tray.stock_name, ");
        sb.append(" tray.bill_num,   ");
        sb.append(" tray.ctn_num,    ");
        sb.append(" tray.sku_id,     ");
        sb.append(" tray.enter_state,");
        sb.append(" tray.CARGO_NAME, ");
        sb.append(" tray.CONTACT_NUM, ");
        sb.append(" tray.asn, ");
        sb.append(" tray.enter_state, ");
        sb.append(" tray.cargo_location ");
        sb.append(" ) tray  ");
        sb.append(" LEFT JOIN BIS_ASN asn ON tray.ASN = asn.ASN ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (    ");
        sb.append(" tray.bill_num = st.ITEM_NUM      ");
        sb.append(" AND tray.CONTACT_NUM = st.LINK_ID    ");
        sb.append(" )                                        ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON ( ");
        sb.append(" st.ITEM_NUM = info.ITEM_NUM      ");
        sb.append(" AND st.LINK_ID = info.LINK_ID    ");
        sb.append(" AND tray.ctn_num = info.ctn_num  ");
        sb.append(" AND tray.sku_id = info.sku       ");
        sb.append(" )                                    ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id    ");
        sb.append(" UNION ALL    ");
        sb.append(" SELECT  ");
        sb.append(" ST.STOCK_IN_ID AS stockId,   ");
        sb.append(" ST.STOCK_IN AS stockName,    ");
        sb.append(" '0' AS isBonded,  ");
        sb.append(" '出库' AS reportType,  ");
        sb.append(" ST.RECEIVER AS shf,  ");
        sb.append(" ST.RECEIVER_ID AS shfid,    ");
        sb.append(" tray.bill_num AS billNum,   ");
        sb.append(" tray.ctn_num AS ctnNum,     ");
        sb.append(" st.CD_NUM AS bgdh, ");
        sb.append(" '' AS ycg, ");
        sb.append(" '' AS BGDHDATE, ");
        sb.append(" tray.sku_id AS sku,");
        sb.append(" '' AS cz, ");
        sb.append(" s.type_name AS bigName, ");
        sb.append(" s.cargo_type AS bigType,    ");
        sb.append(" s.class_name AS simName,    ");
        sb.append(" s.class_type AS simType,    ");
        sb.append(" tray.num, ");
        sb.append(" tray.LOADING_TIME AS enterTime, ");
        sb.append(" tray.enter_state AS enterStats,  ");
        sb.append(" tray.CARGO_NAME AS cargoName,  ");
        sb.append(" tray.zjz, ");
        sb.append(" tray.zmz, ");
        sb.append(" TRAY.CONTACT_NUM AS linkId,    ");
        sb.append(" st. OPERATOR AS createUser,    ");
        sb.append(" tray.asn_id AS asn,   ");
        sb.append(" tray.cargo_location  ");
        sb.append(" FROM   ");
        sb.append(" (SELECT   ");
        sb.append(" L.OUT_LINK_ID AS CONTACT_NUM,  ");
        sb.append(" L.BILL_NUM,");
        sb.append(" L.LOADING_PLAN_NUM,");
        sb.append(" L.CARGO_LOCATION,      ");
        sb.append(" L.LOADING_TIME,   ");
        sb.append(" (CASE L.enter_state     ");
        sb.append(" WHEN '0' THEN  ");
        sb.append(" 'INTACT'   ");
        sb.append(" WHEN '1' THEN  ");
        sb.append(" 'BROKEN' ");
        sb.append(" WHEN '2' THEN   ");
        sb.append(" 'COVER TORN'  ");
        sb.append(" END  ");
        sb.append(" ) AS enter_state,        ");
        sb.append(" L.ASN_ID,                     ");
        sb.append(" L.SKU_ID,                     ");
        sb.append(" L.CARGO_NAME,                 ");
        sb.append(" L.CTN_NUM,                    ");
        sb.append(" SUM (L.PIECE) AS num,         ");
        sb.append(" SUM (L.net_weight) AS zjz,    ");
        sb.append(" SUM (L.GROSS_WEIGHT) AS zmz   ");
        sb.append(" FROM  BIS_LOADING_INFO L ");
        sb.append(" WHERE L.LOADING_STATE = '2'  ");
        sb.append(" GROUP BY  ");
        sb.append(" L.LOADING_TIME, ");
        sb.append(" L.OUT_LINK_ID, ");
        sb.append(" L.BILL_NUM, ");
        sb.append(" L.LOADING_PLAN_NUM, ");
        sb.append(" L.CARGO_LOCATION, ");
        sb.append(" L.enter_state,");
        sb.append(" L.ASN_ID, ");
        sb.append(" L.SKU_ID,");
        sb.append(" L.CARGO_NAME, ");
        sb.append(" L.CTN_NUM  ");
        sb.append(" ) tray  ");
        sb.append(" LEFT JOIN BIS_OUT_STOCK st ON (st.OUT_LINK_ID = tray.CONTACT_NUM)  ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id  ");
        sb.append(" UNION ALL ");    
		sb.append(" SELECT  ");
		sb.append(" M.STOCK_IN_ID AS stockId,  ");
		sb.append(" M.STOCK_IN AS stockName,   ");
		sb.append(" '0' AS isBonded,            ");
		sb.append(" '货转出' AS reportType,     ");
		sb.append(" M.RECEIVER_NAME AS shf,     ");
		sb.append(" M.RECEIVER AS shfid,");
		sb.append(" T .bill_num AS billNum,     ");
		sb.append(" T .ctn_num AS ctnNum,       ");
		sb.append(" M .CD_NUM AS bgdh,          ");
		sb.append(" '' AS ycg,   ");
		sb.append(" '' AS BGDHDATE, ");
		sb.append(" T .sku_id AS sku,  ");
		sb.append(" '' AS cz,  ");
		sb.append(" s.type_name AS bigName,     ");
		sb.append(" s.cargo_type AS bigType,    ");
		sb.append(" s.class_name AS simName,    ");
		sb.append(" s.class_type AS simType,    ");
		sb.append(" SUM (T .PIECE) AS num,      ");
		sb.append(" NVL (    ");
		sb.append(" 	M .START_STORE_DATE-1,    ");
		sb.append(" 	M .OPERATE_TIME-1         ");
		sb.append(" ) AS enterTime,             ");
		sb.append(" (      ");
		sb.append(" CASE T .enter_state ");
		sb.append(" WHEN '0' THEN   ");
		sb.append(" 'INTACT'  ");
		sb.append(" WHEN '1' THEN  ");
		sb.append(" 'BROKEN'  ");
		sb.append(" WHEN '2' THEN ");
		sb.append(" 'COVER TORN'  ");
		sb.append(" END  ");
		sb.append(" ) AS enterStats, ");
		sb.append(" T .CARGO_NAME AS cargoName, ");
		sb.append(" SUM (T .NET_WEIGHT) AS zjz, ");
		sb.append(" SUM (T .GROSS_WEIGHT) AS zmz,");
		sb.append(" M .transfer_id AS linkId,   ");
		sb.append(" M . OPERATOR AS createUser, ");
		sb.append(" '' AS asn,  ");
		sb.append(" '' AS cargo_location        ");
		sb.append(" FROM  ");
		sb.append(" BIS_TRANSFER_STOCK M    ");
		sb.append(" LEFT JOIN BIS_TRANSFER_STOCK_INFO T ON M .transfer_id = T .transfer_link_id      ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = T .sku_id  ");
		sb.append(" GROUP BY ");
		sb.append(" s.type_name, ");
		sb.append(" s.cargo_type,");
		sb.append(" s.class_name,");
		sb.append(" s.class_type,");
		sb.append(" M.CD_NUM, ");
		sb.append(" M.STOCK_IN, ");
		sb.append(" M.STOCK_IN_ID, ");
		sb.append(" M.START_STORE_DATE, ");
		sb.append(" M.OPERATE_TIME, ");
		sb.append(" T.bill_num,  ");
		sb.append(" T.ctn_num, ");
		sb.append(" T.sku_id,  ");
		sb.append(" T.enter_state,");
		sb.append(" T.CARGO_NAME, ");
		sb.append(" M.transfer_id, ");
		sb.append(" M.RECEIVER_NAME,");
		sb.append(" M.OPERATOR, ");
		sb.append(" M.RECEIVER ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT   ");
		sb.append(" M .STOCK_IN_ID AS stockId,  ");
		sb.append(" M .STOCK_IN AS stockName,   ");
		sb.append(" '0' AS isBonded,            ");
		sb.append(" '货转入' AS reportType,     ");
		sb.append(" M.RECEIVER_NAME AS shf,     ");
		sb.append(" M.RECEIVER AS shfid,");
		sb.append(" T.bill_num AS billNum,     ");
		sb.append(" T.ctn_num AS ctnNum,       ");
		sb.append(" M.CD_NUM AS bgdh,          ");
		sb.append(" '' AS ycg,  ");
		sb.append(" '' AS BGDHDATE, ");
		sb.append(" T .sku_id AS sku, ");
		sb.append(" '' AS cz,  ");
		sb.append(" s.type_name AS bigName,  ");
		sb.append(" s.cargo_type AS bigType, ");
		sb.append(" s.class_name AS simName, ");
		sb.append(" s.class_type AS simType,");
		sb.append(" SUM (T .PIECE) AS num,  ");
		sb.append(" NVL ( ");
		sb.append(" M .START_STORE_DATE, ");
		sb.append(" M .OPERATE_TIME   ");
		sb.append(" ) AS enterTime, ");
		sb.append(" (   ");
		sb.append(" CASE T .enter_state ");
		sb.append(" WHEN '0' THEN  ");
		sb.append(" 'INTACT'   ");
		sb.append(" WHEN '1' THEN ");
		sb.append(" 'BROKEN'    ");
		sb.append(" WHEN '2' THEN ");
		sb.append(" 'COVER TORN'  ");
		sb.append(" END   ");
		sb.append(" ) AS enterStats, ");
		sb.append(" T .CARGO_NAME AS cargoName,   ");
		sb.append(" SUM (T .NET_WEIGHT) AS zjz,   ");
		sb.append(" SUM (T .GROSS_WEIGHT) AS zmz, ");
		sb.append(" M .transfer_id AS linkId,     ");
		sb.append(" M . OPERATOR AS createUser,   ");
		sb.append(" '' AS asn,  ");
		sb.append(" '' AS cargo_location    ");
		sb.append(" FROM  ");
		sb.append(" BIS_TRANSFER_STOCK M    ");
		sb.append(" LEFT JOIN BIS_TRANSFER_STOCK_INFO T ON M .transfer_id = T .transfer_link_id    ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = T .sku_id  ");
		sb.append(" GROUP BY  ");
		sb.append(" M.STOCK_IN_ID, ");
		sb.append(" M.STOCK_IN,  ");
		sb.append(" s.type_name,  ");
		sb.append(" s.cargo_type, ");
		sb.append(" s.class_name, ");
		sb.append(" s.class_type, ");
		sb.append(" M.CD_NUM,    ");
		sb.append(" M.START_STORE_DATE,");
		sb.append(" M.OPERATE_TIME, ");
		sb.append(" M.RECEIVER_NAME, ");
		sb.append(" M.RECEIVER, ");
		sb.append(" T.bill_num, ");
		sb.append(" T.ctn_num,  ");
		sb.append(" T.sku_id,   ");
		sb.append(" T.enter_state,");
		sb.append(" T.CARGO_NAME,");
		sb.append(" M. OPERATOR,");
		sb.append(" M.transfer_id ");
		sb.append(" ) temp ");
        sb.append(" where 1=1 ");
        if(null!=isBonded&&!"".equals(isBonded)){
             sb.append(" AND temp.isBonded=:isBonded");
             parme.put("isBonded",isBonded);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append(" and temp.stockId =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("   and temp.billNum=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("   and  temp.ctnNum=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and temp.enterTime>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and temp.enterTime<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')   ");
            parme.put("endTime", endTime);
        }
        sb.append(" group BY ");
        sb.append("  temp.stockId,     ");
        sb.append("  temp.stockName,   ");
        sb.append("  temp.isBonded,    ");
        sb.append("  temp.reportType,  ");
        sb.append("  temp.shf,         ");
        sb.append("  temp.shfid,       ");
        sb.append("  temp.billNum,     ");
        sb.append("  temp.ctnNum,      ");
        sb.append("  temp.bgdh,        ");
        sb.append("  temp.ycg,         ");
        sb.append("  temp.BGDHDATE,    ");
        sb.append("  temp.sku,         ");
        sb.append("  temp.cz,          ");
        sb.append("  temp.bigName,     ");
        sb.append("  temp.bigType,     ");
        sb.append("  temp.simName,     ");
        sb.append("  temp.simType,     ");
        sb.append("  TO_CHAR(temp.enterTime,'yyyy-mm-dd'), ");
        sb.append("  temp.enterStats,  ");
        sb.append("  temp.cargoName,   ");
        sb.append("  temp.linkId,      ");
        sb.append("  temp.createUser,  ");
        sb.append("  temp.asn  ");
	    if(2==ntype){
	      sb.append(" ,temp.cargo_location  ");  
	    }
        sb.append(" ORDER BY temp.reportType ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.list();
    }
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String, Object>> findRepotInAndOut(String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" select                                                                             ");
        sb.append("   t.client_name,                                                                   ");
        sb.append("   t.stock_id,                                                                      ");
        sb.append("   t.leixing,                                                                       ");
        sb.append("   t.leixing_sort,                                                                  ");
        sb.append("   t.receiver_name,                                                                 ");
        sb.append("   t.receiver_id,                                                                   ");
        sb.append("   t.bill_num,                                                                      ");
        sb.append("   t.ctn_num,                                                                       ");
        sb.append("   t.sku_id,                                                                        ");
        sb.append("   t.cargo_name,                                                                    ");
        sb.append("   t.type_name,                                                                     ");
        sb.append("   t.cargo_type,                                                                    ");
        sb.append("   t.class_name,                                                                    ");
        sb.append("   t.class_type,                                                                    ");
        sb.append("   t.cargo_location,                                                                ");
        sb.append("   t.is_bonded,                                                                     ");
        sb.append("   t.churuku_date,                                                                  ");
        sb.append("   t.enter_state,                                                                   ");
        sb.append("   t.piece_sum,                                                                     ");
        sb.append("   t.net_weight_sum,                                                                ");
        sb.append("   t.gross_weight_sum                                                               ");
        sb.append(" from                                                                               ");
        sb.append(" (                                                                                  ");
        sb.append(" SELECT c.client_name,                                                              ");
        sb.append("        aa.stock_in AS stock_id,                                                    ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '入库'                                                                   ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '分拣'                                                                   ");
        sb.append("        END) AS leixing,                                                            ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '1'                                                                      ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '3'                                                                      ");
        sb.append("        END) AS leixing_sort,                                                       ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.inbound_date AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        round(aa.piece_sum * s.net_single, 2) AS net_weight_sum,                    ");
        sb.append("        round(aa.piece_sum * s.gross_single, 2) AS gross_weight_sum                 ");
        sb.append("   FROM (SELECT asnt.inbound_date,                                                  ");
        sb.append("                asnt.if_second_enter,                                               ");
        sb.append("                asnt.is_bonded,                                                     ");
        sb.append("                t.stock_in,                                                         ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.original_piece - t.remove_piece) AS piece_sum                 ");
        sb.append("           FROM (SELECT a.asn,                                                      ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '1'                                     ");
        sb.append("                  union all                                                         ");
        sb.append("                  SELECT a.asn,                                                     ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '3'                                     ");
        sb.append("          ) asnt                                                                    ");
        sb.append("          INNER JOIN (SELECT sku_id,                                                ");
        sb.append("                            asn,                                                    ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_in,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            original_piece,                                         ");
        sb.append("                            remove_piece,                                           ");
        sb.append("                            ti.cargo_location                                       ");
        sb.append("                       FROM BIS_TRAY_INFO ti                                        ");
        sb.append("                      WHERE 1 = 1                                                   ");
        sb.append("                        AND ti.if_transfer = '0') t                                 ");
        sb.append("             ON (t.asn = asnt.asn AND asnt.bill_num = t.bill_num)                   ");
        sb.append("    where 1=1                                                                       ");
        sb.append("          GROUP BY t.stock_in,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   asnt.is_bonded,                                                  ");
        sb.append("                   asnt.inbound_date,                                               ");
        sb.append("                   t.cargo_location,                                                ");
        sb.append("                   asnt.if_second_enter) aa                                         ");
        sb.append("   LEFT JOIN base_client_info c                                                     ");
        sb.append("     ON c.ids = aa.stock_in                                                         ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT aa.stock_name,                                                              ");
        sb.append("        aa.stock_id,                                                                ");
        sb.append("        '出库' AS leixing,                                                          ");
        sb.append("        '4' AS leixing_sort,                                                        ");
        sb.append("        aa.receiver_name,                                                           ");
        sb.append("        aa.receiver_id,                                                             ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.loading_tiem AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        net_weight_sum,                                                             ");
        sb.append("        gross_weight_sum                                                            ");
        sb.append("   FROM (SELECT lor.loading_tiem,                                                   ");
        sb.append("                t.stock_id,                                                         ");
        sb.append("                lor.stock_name,                                                     ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                a.is_bonded,                                                        ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.piece) AS piece_sum,                                          ");
        sb.append("                sum(t.net_weight) AS net_weight_sum,                                ");
        sb.append("                sum(t.gross_weight) AS gross_weight_sum,                            ");
        sb.append("                lo.receiver_name,                                                   ");
        sb.append("                lo.receiver_id                                                      ");
        sb.append("           FROM (SELECT a.loading_plan_num,                                         ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.loading_tiem,                                             ");
        sb.append("                        a.stock_id,                                                 ");
        sb.append("                        a.stock_name,                                               ");
        sb.append("                        a.sku_id,                                                   ");
        sb.append("                        a.asn                                                       ");
        sb.append("                   FROM bis_loading_order_info a                                    ");
        sb.append("                  WHERE 1 = 1                                                       ");
        sb.append("                                                                                    ");
        sb.append("                  GROUP BY a.loading_plan_num,                                      ");
        sb.append("                           a.bill_num,                                              ");
        sb.append("                           a.ctn_num,                                               ");
        sb.append("                           a.loading_tiem,                                          ");
        sb.append("                           a.stock_id,                                              ");
        sb.append("                           a.stock_name,                                            ");
        sb.append("                           a.sku_id,a.asn) lor                                      ");
        sb.append("           LEFT JOIN bis_asn a                                                      ");
        sb.append("             ON (lor.asn=a.asn AND lor.ctn_num = a.ctn_num AND lor.bill_num = a.bill_num) ");
        sb.append("           LEFT JOIN (SELECT sku_id,                                                ");
        sb.append("                            loading_plan_num,                                       ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_id,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            li.piece,                                               ");
        sb.append("                            li.net_weight,                                          ");
        sb.append("                            li.gross_weight,                                        ");
        sb.append("                            li.cargo_location                                       ");
        sb.append("                       FROM bis_loading_info li                                     ");
        sb.append("                      WHERE li.loading_state = '2') t                               ");
        sb.append("             ON t.loading_plan_num = lor.loading_plan_num                           ");
        sb.append("            AND lor.BILL_NUM = t.BILL_NUM                                           ");
        sb.append("            AND lor.SKU_ID = t.sku_id                                               ");
        sb.append("           LEFT JOIN bis_loading_order lo                                           ");
        sb.append("             ON lo.order_num = lor.loading_plan_num                                 ");
        sb.append("          WHERE lo.order_state = '4'                                                ");
        sb.append("          GROUP BY t.stock_id,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   lor.loading_tiem,                                                ");
        sb.append("                   lor.stock_name,                                                  ");
        sb.append("                   lo.receiver_name,                                                ");
        sb.append("                   lo.receiver_id,                                                  ");
        sb.append("                   a.is_bonded,                                                     ");
        sb.append("                   t.cargo_location) aa                                             ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT t.stock_name AS client_name,                                                ");
        sb.append("        t.stock_in,                                                                 ");
        sb.append("        '货转入' AS leixing,                                                        ");
        sb.append("        '3' AS leixing_sort,                                                        ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        t.bill_num,                                                                 ");
        sb.append("        t.ctn_num,                                                                  ");
        sb.append("        t.sku_id,                                                                   ");
        sb.append("        t.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        t.cargo_location,                                                           ");
        sb.append("        a.is_bonded,                                                                ");
        sb.append("        ts.operate_time AS churuku_date,                                            ");
        sb.append("        (CASE t.enter_state                                                         ");
        sb.append("          WHEN '0' THEN                                                             ");
        sb.append("           'INTACT'                                                                 ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           'BROKEN'                                                                 ");
        sb.append("          WHEN '2' THEN                                                             ");
        sb.append("           'COVER TORN'                                                             ");
        sb.append("        END) AS enter_state,                                                        ");
        sb.append("        t.now_piece AS piece_sum,                                                   ");
        sb.append("        t.now_piece * s.net_single AS net_weight_sum,                               ");
        sb.append("        t.now_piece * s.gross_single AS gross_weight_sum                            ");
        sb.append("   FROM bis_tray_info t                                                             ");
        sb.append("   LEFT JOIN bis_asn a                                                              ");
        sb.append("     ON t.asn = a.asn                                                               ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = t.sku_id                                                         ");
        sb.append("   LEFT JOIN bis_transfer_stock ts                                                  ");
        sb.append("     ON ts.transfer_id = t.contact_num                                              ");
        sb.append("  WHERE t.if_transfer = '1'                                                         ");
        sb.append("    AND t.contact_type = '3'                                                        ");
        sb.append("  )t                                                                                ");
        sb.append(" where 1=1 ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND t.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (t.is_bonded ='0' or t.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("    and t.stock_id =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("   and t.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("   and  t.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and t.churuku_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and t.churuku_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
    @SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String, Object>> findRepotInAndOutByPage(String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime,int page) {
        List<Map<String, Object>> getList = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer row=new StringBuffer();
        row.append("select * from (select row_.*,rownum rownum_  FROM ( ");
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" select                                                                             ");
        sb.append("   t.client_name,                                                                   ");
        sb.append("   t.stock_id,                                                                      ");
        sb.append("   t.leixing,                                                                       ");
        sb.append("   t.leixing_sort,                                                                  ");
        sb.append("   t.receiver_name,                                                                 ");
        sb.append("   t.receiver_id,                                                                   ");
        sb.append("   t.bill_num,                                                                      ");
        sb.append("   t.ctn_num,                                                                       ");
        sb.append("   t.sku_id,                                                                        ");
        sb.append("   t.cargo_name,                                                                    ");
        sb.append("   t.type_name,                                                                     ");
        sb.append("   t.cargo_type,                                                                    ");
        sb.append("   t.class_name,                                                                    ");
        sb.append("   t.class_type,                                                                    ");
        sb.append("   t.cargo_location,                                                                ");
        sb.append("   t.is_bonded,                                                                     ");
        sb.append("   t.churuku_date,                                                                  ");
        sb.append("   t.enter_state,                                                                   ");
        sb.append("   t.piece_sum,                                                                     ");
        sb.append("   t.net_weight_sum,                                                                ");
        sb.append("   t.gross_weight_sum                                                               ");
        sb.append(" from                                                                               ");
        sb.append(" (                                                                                  ");
        sb.append(" SELECT c.client_name,                                                              ");
        sb.append("        aa.stock_in AS stock_id,                                                    ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '入库'                                                                   ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '分拣'                                                                   ");
        sb.append("        END) AS leixing,                                                            ");
        sb.append("        (CASE aa.if_second_enter                                                    ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           '1'                                                                      ");
        sb.append("          WHEN '3' THEN                                                             ");
        sb.append("           '3'                                                                      ");
        sb.append("        END) AS leixing_sort,                                                       ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.inbound_date AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        round(aa.piece_sum * s.net_single, 2) AS net_weight_sum,                    ");
        sb.append("        round(aa.piece_sum * s.gross_single, 2) AS gross_weight_sum                 ");
        sb.append("   FROM (SELECT asnt.inbound_date,                                                  ");
        sb.append("                asnt.if_second_enter,                                               ");
        sb.append("                asnt.is_bonded,                                                     ");
        sb.append("                t.stock_in,                                                         ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.original_piece - t.remove_piece) AS piece_sum                 ");
        sb.append("           FROM (SELECT a.asn,                                                      ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '1'                                     ");
        sb.append("                  union all                                                         ");
        sb.append("                  SELECT a.asn,                                                     ");
        sb.append("                        a.is_bonded,                                                ");
        sb.append("                        a.inbound_date,                                             ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.stock_in,                                                 ");
        sb.append("                        a.if_second_enter                                           ");
        sb.append("                   FROM bis_asn a                                                   ");
        sb.append("                  WHERE a.if_second_enter = '3') asnt                               ");
        sb.append("          INNER JOIN (SELECT sku_id,                                                ");
        sb.append("                            asn,                                                    ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_in,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            original_piece,                                         ");
        sb.append("                            remove_piece,                                           ");
        sb.append("                            ti.cargo_location                                       ");
        sb.append("                       FROM BIS_TRAY_INFO ti                                        ");
        sb.append("                      WHERE 1 = 1                                                   ");
        sb.append("                        AND ti.if_transfer = '0') t                                 ");
        sb.append("             ON t.asn = asnt.asn                                                    ");
        sb.append("            AND asnt.bill_num = t.bill_num                                          ");
        sb.append("          GROUP BY t.stock_in,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   asnt.is_bonded,                                                  ");
        sb.append("                   asnt.inbound_date,                                               ");
        sb.append("                   t.cargo_location,                                                ");
        sb.append("                   asnt.if_second_enter) aa                                         ");
        sb.append("   LEFT JOIN base_client_info c                                                     ");
        sb.append("     ON c.ids = aa.stock_in                                                         ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT aa.stock_name,                                                              ");
        sb.append("        aa.stock_id,                                                                ");
        sb.append("        '出库' AS leixing,                                                          ");
        sb.append("        '4' AS leixing_sort,                                                        ");
        sb.append("        aa.receiver_name,                                                           ");
        sb.append("        aa.receiver_id,                                                             ");
        sb.append("        aa.bill_num,                                                                ");
        sb.append("        aa.ctn_num,                                                                 ");
        sb.append("        aa.sku_id,                                                                  ");
        sb.append("        s.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        aa.cargo_location,                                                          ");
        sb.append("        aa.is_bonded,                                                               ");
        sb.append("        aa.loading_tiem AS churuku_date,                                            ");
        sb.append("        aa.enter_state,                                                             ");
        sb.append("        aa.piece_sum,                                                               ");
        sb.append("        net_weight_sum,                                                             ");
        sb.append("        gross_weight_sum                                                            ");
        sb.append("   FROM (SELECT lor.loading_tiem,                                                   ");
        sb.append("                t.stock_id,                                                         ");
        sb.append("                lor.stock_name,                                                     ");
        sb.append("                t.bill_num,                                                         ");
        sb.append("                t.ctn_num,                                                          ");
        sb.append("                t.sku_id,                                                           ");
        sb.append("                t.cargo_location,                                                   ");
        sb.append("                a.is_bonded,                                                        ");
        sb.append("                (CASE t.enter_state                                                 ");
        sb.append("                  WHEN '0' THEN                                                     ");
        sb.append("                   'INTACT'                                                         ");
        sb.append("                  WHEN '1' THEN                                                     ");
        sb.append("                   'BROKEN'                                                         ");
        sb.append("                  WHEN '2' THEN                                                     ");
        sb.append("                   'COVER TORN'                                                     ");
        sb.append("                END) AS enter_state,                                                ");
        sb.append("                sum(t.piece) AS piece_sum,                                          ");
        sb.append("                sum(t.net_weight) AS net_weight_sum,                                ");
        sb.append("                sum(t.gross_weight) AS gross_weight_sum,                            ");
        sb.append("                lo.receiver_name,                                                   ");
        sb.append("                lo.receiver_id                                                      ");
        sb.append("           FROM (SELECT a.loading_plan_num,                                         ");
        sb.append("                        a.bill_num,                                                 ");
        sb.append("                        a.ctn_num,                                                  ");
        sb.append("                        a.loading_tiem,                                             ");
        sb.append("                        a.stock_id,                                                 ");
        sb.append("                        a.stock_name,                                               ");
        sb.append("                        a.sku_id                                                    ");
        sb.append("                   FROM bis_loading_order_info a                                    ");
        sb.append("                  WHERE 1 = 1                                                       ");
        sb.append("                                                                                    ");
        sb.append("                  GROUP BY a.loading_plan_num,                                      ");
        sb.append("                           a.bill_num,                                              ");
        sb.append("                           a.ctn_num,                                               ");
        sb.append("                           a.loading_tiem,                                          ");
        sb.append("                           a.stock_id,                                              ");
        sb.append("                           a.stock_name,                                            ");
        sb.append("                           a.sku_id) lor                                            ");
        sb.append("           LEFT JOIN bis_asn a                                                      ");
        sb.append("             ON (lor.ctn_num = a.ctn_num AND lor.bill_num = a.bill_num)             ");
        sb.append("           LEFT JOIN (SELECT sku_id,                                                ");
        sb.append("                            loading_plan_num,                                       ");
        sb.append("                            bill_num,                                               ");
        sb.append("                            stock_id,                                               ");
        sb.append("                            ctn_num,                                                ");
        sb.append("                            enter_state,                                            ");
        sb.append("                            li.piece,                                               ");
        sb.append("                            li.net_weight,                                          ");
        sb.append("                            li.gross_weight,                                        ");
        sb.append("                            li.cargo_location                                       ");
        sb.append("                       FROM bis_loading_info li                                     ");
        sb.append("                      WHERE li.loading_state = '2') t                               ");
        sb.append("             ON t.loading_plan_num = lor.loading_plan_num                           ");
        sb.append("            AND lor.BILL_NUM = t.BILL_NUM                                           ");
        sb.append("            AND lor.SKU_ID = t.sku_id                                               ");
        sb.append("           LEFT JOIN bis_loading_order lo                                           ");
        sb.append("             ON lo.order_num = lor.loading_plan_num                                 ");
        sb.append("          WHERE lo.order_state = '4'                                                ");
        sb.append("          GROUP BY t.stock_id,                                                      ");
        sb.append("                   t.bill_num,                                                      ");
        sb.append("                   t.ctn_num,                                                       ");
        sb.append("                   t.sku_id,                                                        ");
        sb.append("                   t.enter_state,                                                   ");
        sb.append("                   lor.loading_tiem,                                                ");
        sb.append("                   lor.stock_name,                                                  ");
        sb.append("                   lo.receiver_name,                                                ");
        sb.append("                   lo.receiver_id,                                                  ");
        sb.append("                   a.is_bonded,                                                     ");
        sb.append("                   t.cargo_location) aa                                             ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = aa.sku_id                                                        ");
        sb.append(" UNION ALL                                                                          ");
        sb.append(" SELECT t.stock_name AS client_name,                                                ");
        sb.append("        t.stock_in,                                                                 ");
        sb.append("        '货转入' AS leixing,                                                        ");
        sb.append("        '3' AS leixing_sort,                                                        ");
        sb.append("        '' AS receiver_name,                                                        ");
        sb.append("        '' AS receiver_id,                                                          ");
        sb.append("        t.bill_num,                                                                 ");
        sb.append("        t.ctn_num,                                                                  ");
        sb.append("        t.sku_id,                                                                   ");
        sb.append("        t.cargo_name,                                                               ");
        sb.append("        s.type_name,                                                                ");
        sb.append("        s.cargo_type,                                                               ");
        sb.append("        s.class_name,                                                               ");
        sb.append("        s.class_type,                                                               ");
        sb.append("        t.cargo_location,                                                           ");
        sb.append("        a.is_bonded,                                                                ");
        sb.append("        ts.operate_time AS churuku_date,                                            ");
        sb.append("        (CASE t.enter_state                                                         ");
        sb.append("          WHEN '0' THEN                                                             ");
        sb.append("           'INTACT'                                                                 ");
        sb.append("          WHEN '1' THEN                                                             ");
        sb.append("           'BROKEN'                                                                 ");
        sb.append("          WHEN '2' THEN                                                             ");
        sb.append("           'COVER TORN'                                                             ");
        sb.append("        END) AS enter_state,                                                        ");
        sb.append("        t.now_piece AS piece_sum,                                                   ");
        sb.append("        t.now_piece * s.net_single AS net_weight_sum,                               ");
        sb.append("        t.now_piece * s.gross_single AS gross_weight_sum                            ");
        sb.append("   FROM bis_tray_info t                                                             ");
        sb.append("   LEFT JOIN bis_asn a                                                              ");
        sb.append("     ON t.asn = a.asn                                                               ");
        sb.append("   LEFT JOIN base_sku_base_info s                                                   ");
        sb.append("     ON s.sku_id = t.sku_id                                                         ");
        sb.append("   LEFT JOIN bis_transfer_stock ts                                                  ");
        sb.append("     ON ts.transfer_id = t.contact_num                                              ");
        sb.append("  WHERE t.if_transfer = '1'                                                         ");
        sb.append("    AND t.contact_type = '3'                                                        ");
        sb.append("  )t                                                                                ");
        sb.append(" where 1=1 ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND t.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (t.is_bonded ='0' or t.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("    and t.stock_id =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("   and t.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("   and  t.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and t.churuku_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and t.churuku_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        row.append(sb.toString());
        row.append("  ) row_) ");
        row.append("  where rownum_>"+(page-1)*1000+" and rownum_<="+page*1000+"");
        SQLQuery sqlQuery = createSQLQuery(row.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    /**
     * 根据条件获取总数据
     * @param isBonded
     * @param itemNum
     * @param cunNum
     * @param stockIn
     * @param linkId
     * @param strTime
     * @param endTime
     * @return
     */
	public Integer getRepotInAndOutCount(String isBonded,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
        StringBuffer sb = new StringBuffer();
        StringBuffer count=new StringBuffer();
        count.append("select count(*) from ( ");
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append("SELECT                                                                              ");
        sb.append("	c.client_name,                                                                     ");
        sb.append("	aa.stock_in AS stock_id,                                                           ");
        sb.append("	(                                                                                  ");
        sb.append("		CASE aa.if_second_enter                                                          ");
        sb.append("		WHEN '1' THEN                                                                    ");
        sb.append("			'入库'                                                                         ");
        sb.append("		WHEN '3' THEN                                                                    ");
        sb.append("			'分拣'                                                                         ");
        sb.append("		END                                                                              ");
        sb.append("	) AS leixing,                                                                      ");
        sb.append("	(                                                                                  ");
        sb.append("		CASE aa.if_second_enter                                                          ");
        sb.append("		WHEN '1' THEN                                                                    ");
        sb.append("			'1'                                                                            ");
        sb.append("		WHEN '3' THEN                                                                    ");
        sb.append("			'3'                                                                            ");
        sb.append("		END                                                                              ");
        sb.append("	) AS leixing_sort,                                                                 ");
        sb.append("	'' AS receiver_name,                                                               ");
        sb.append("	'' AS receiver_id,                                                                 ");
        sb.append("	aa.bill_num,                                                                       ");
        sb.append("	aa.ctn_num,                                                                        ");
        sb.append("	aa.sku_id,                                                                         ");
        sb.append("	s.cargo_name,                                                                      ");
        sb.append("	s.type_name,                                                                       ");
        sb.append("	s.cargo_type,                                                                      ");
        sb.append("	s.class_name,                                                                      ");
        sb.append("	s.class_type,                                                                      ");
        sb.append("	aa.cargo_location,                                                                 ");
        sb.append("	aa.is_bonded,                                                                      ");
        sb.append("	aa.inbound_date AS churuku_date,                                                   ");
        sb.append("	aa.enter_state,                                                                    ");
        sb.append("	aa.piece_sum,                                                                      ");
        sb.append("	round(                                                                             ");
        sb.append("		aa.piece_sum * s.net_single,                                                     ");
        sb.append("		2                                                                                ");
        sb.append("	) AS net_weight_sum,                                                               ");
        sb.append("	round(                                                                             ");
        sb.append("		aa.piece_sum * s.gross_single,                                                   ");
        sb.append("		2                                                                                ");
        sb.append("	) AS gross_weight_sum                                                              ");
        sb.append("FROM                                                                                ");
        sb.append("	(                                                                                  ");
        sb.append("		SELECT                                                                           ");
        sb.append("			asnt.inbound_date,                                                             ");
        sb.append("			asnt.if_second_enter,                                                          ");
        sb.append("			asnt.is_bonded,                                                                ");
        sb.append("			t.stock_in,                                                                    ");
        sb.append("			t.bill_num,                                                                    ");
        sb.append("			t.ctn_num,                                                                     ");
        sb.append("			t.sku_id,                                                                      ");
        sb.append("			t.cargo_location,                                                              ");
        sb.append("			(                                                                              ");
        sb.append("				CASE t.enter_state                                                           ");
        sb.append("				WHEN '0' THEN                                                                ");
        sb.append("					'INTACT'                                                                   ");
        sb.append("				WHEN '1' THEN                                                                ");
        sb.append("					'BROKEN'                                                                   ");
        sb.append("				WHEN '2' THEN                                                                ");
        sb.append("					'COVER TORN'                                                               ");
        sb.append("				END                                                                          ");
        sb.append("			) AS enter_state,                                                              ");
        sb.append("			sum(                                                                           ");
        sb.append("				t.original_piece - t.remove_piece                                            ");
        sb.append("			) AS piece_sum                                                                 ");
        sb.append("		FROM                                                                             ");
        sb.append("			(                                                                              ");
        sb.append("				SELECT                                                                       ");
        sb.append("					a.asn,                                                                     ");
        sb.append("					a.is_bonded,                                                               ");
        sb.append("					a.inbound_date,                                                            ");
        sb.append("					a.ctn_num,                                                                 ");
        sb.append("					a.bill_num,                                                                ");
        sb.append("					a.stock_in,                                                                ");
        sb.append("					a.if_second_enter                                                          ");
        sb.append("				FROM                                                                         ");
        sb.append("					bis_asn a                                                                  ");
        sb.append("				WHERE                                                                        ");
        sb.append("					(a.if_second_enter = '1' OR a.if_second_enter = '3')                       ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and a.stock_in =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and a.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  a.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and a.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and a.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("			) asnt                                                                         ");
        sb.append("		INNER JOIN (                                                                     ");
        sb.append("			SELECT                                                                         ");
        sb.append("				sku_id,                                                                      ");
        sb.append("				asn,                                                                         ");
        sb.append("				bill_num,                                                                    ");
        sb.append("				stock_in,                                                                    ");
        sb.append("				ctn_num,                                                                     ");
        sb.append("				enter_state,                                                                 ");
        sb.append("				original_piece,                                                              ");
        sb.append("				remove_piece,                                                                ");
        sb.append("				ti.cargo_location                                                            ");
        sb.append("			FROM                                                                           ");
        sb.append("				BIS_TRAY_INFO ti                                                             ");
        sb.append("			WHERE                                                                          ");
        sb.append("				1 = 1                                                                        ");
        sb.append("			AND ti.if_transfer = '0'                                                       ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and bill_num=:billnumo ");
            parme.put("billnumo", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  ctn_num=:ctnnumo  ");
            parme.put("ctnnumo", cunNum);
        }
        sb.append("		) t ON t.asn = asnt.asn                                                          ");
        sb.append("		AND asnt.bill_num = t.bill_num                                                   ");
        sb.append("		GROUP BY                                                                         ");
        sb.append("			t.stock_in,                                                                    ");
        sb.append("			t.bill_num,                                                                    ");
        sb.append("			t.ctn_num,                                                                     ");
        sb.append("			t.sku_id,                                                                      ");
        sb.append("			t.enter_state,                                                                 ");
        sb.append("			asnt.is_bonded,                                                                ");
        sb.append("			asnt.inbound_date,                                                             ");
        sb.append("			t.cargo_location,                                                              ");
        sb.append("			asnt.if_second_enter                                                           ");
        sb.append("	) aa                                                                               ");
        sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in                                 ");
        sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                              ");
        sb.append("UNION ALL                                                                           ");
        sb.append("	SELECT                                                                             ");
        sb.append("		aa.stock_name,                                                                   ");
        sb.append("		aa.stock_id,                                                                     ");
        sb.append("		'出库' AS leixing,                                                               ");
        sb.append("		'4' AS leixing_sort,                                                             ");
        sb.append("		aa.receiver_name,                                                                ");
        sb.append("		aa.receiver_id,                                                                  ");
        sb.append("		aa.bill_num,                                                                     ");
        sb.append("		aa.ctn_num,                                                                      ");
        sb.append("		aa.sku_id,                                                                       ");
        sb.append("		s.cargo_name,                                                                    ");
        sb.append("		s.type_name,                                                                     ");
        sb.append("		s.cargo_type,                                                                    ");
        sb.append("		s.class_name,                                                                    ");
        sb.append("		s.class_type,                                                                    ");
        sb.append("		aa.cargo_location,                                                               ");
        sb.append("		aa.is_bonded,                                                                    ");
        sb.append("		aa.loading_tiem AS churuku_date,                                                 ");
        sb.append("		aa.enter_state,                                                                  ");
        sb.append("		aa.piece_sum,                                                                    ");
        sb.append("		net_weight_sum,                                                                  ");
        sb.append("		gross_weight_sum                                                                 ");
        sb.append("	FROM                                                                               ");
        sb.append("		(                                                                                ");
        sb.append("			SELECT                                                                         ");
        sb.append("				lor.loading_tiem,                                                            ");
        sb.append("				t.stock_id,                                                                  ");
        sb.append("				lor.stock_name,                                                              ");
        sb.append("				t.bill_num,                                                                  ");
        sb.append("				t.ctn_num,                                                                   ");
        sb.append("				t.sku_id,                                                                    ");
        sb.append("				t.cargo_location,                                                            ");
        sb.append("				a.is_bonded,                                                                 ");
        sb.append("				(                                                                            ");
        sb.append("					CASE t.enter_state                                                         ");
        sb.append("					WHEN '0' THEN                                                              ");
        sb.append("						'INTACT'                                                                 ");
        sb.append("					WHEN '1' THEN                                                              ");
        sb.append("						'BROKEN'                                                                 ");
        sb.append("					WHEN '2' THEN                                                              ");
        sb.append("						'COVER TORN'                                                             ");
        sb.append("					END                                                                        ");
        sb.append("				) AS enter_state,                                                            ");
        sb.append("				sum(t.piece) AS piece_sum,                                                   ");
        sb.append("				sum(t.net_weight) AS net_weight_sum,                                         ");
        sb.append("				sum(t.gross_weight) AS gross_weight_sum,                                     ");
        sb.append("				lo.receiver_name,                                                            ");
        sb.append("				lo.receiver_id                                                               ");
        sb.append("			FROM                                                                           ");
        sb.append("				(                                                                            ");
        sb.append("					SELECT                                                                     ");
        sb.append("						a.loading_plan_num,                                                      ");
        sb.append("						a.bill_num,                                                              ");
        sb.append("						a.ctn_num,                                                               ");
        sb.append("						a.loading_tiem,                                                          ");
        sb.append("						a.stock_id,                                                              ");
        sb.append("						a.stock_name,                                                            ");
        sb.append("						a.sku_id                                                                 ");
        sb.append("					FROM                                                                       ");
        sb.append("						bis_loading_order_info a                                                 ");
        sb.append("					WHERE                                                                      ");
        sb.append("						1 = 1                                                                    ");
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and a.stock_id =:sockid2");
            parme.put("sockid2", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and a.bill_num=:billnum2 ");
            parme.put("billnum2", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  a.ctn_num=:ctnnum2  ");
            parme.put("ctnnum2", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--出库日期
            sb.append(" and a.loading_tiem>=to_date(:strTime2,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime2", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--出库日期
            sb.append(" and a.loading_tiem<to_date(:endTime2,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime2", endTime);
        }
        sb.append("					GROUP BY                                                                   ");          
        sb.append("						a.loading_plan_num,                                                      ");          
        sb.append("						a.bill_num,                                                              ");          
        sb.append("						a.ctn_num,                                                               ");          
        sb.append("						a.loading_tiem,                                                          ");          
        sb.append("						a.stock_id,                                                              ");          
        sb.append("						a.stock_name,                                                            ");          
        sb.append("						a.sku_id                                                                 ");          
        sb.append("				) lor                                                                        ");          
        sb.append("			LEFT JOIN bis_asn a ON (                                                       ");          
        sb.append("				lor.ctn_num = a.ctn_num                                                      ");          
        sb.append("				AND lor.bill_num = a.bill_num                                                ");          
        sb.append("			)                                                                              ");          
        sb.append("			LEFT JOIN (                                                                    ");          
        sb.append("				SELECT                                                                       ");          
        sb.append("					sku_id,                                                                    ");          
        sb.append("					loading_plan_num,                                                          ");          
        sb.append("					bill_num,                                                                  ");          
        sb.append("					stock_id,                                                                  ");          
        sb.append("					ctn_num,                                                                   ");          
        sb.append("					enter_state,                                                               ");          
        sb.append("					li.piece,                                                                  ");          
        sb.append("					li.net_weight,                                                             ");          
        sb.append("					li.gross_weight,                                                           ");          
        sb.append("					li.cargo_location                                                          ");          
        sb.append("				FROM                                                                         ");          
        sb.append("					bis_loading_info li                                                        ");          
        sb.append("				WHERE                                                                        ");          
        sb.append("					li.loading_state = '2'                                                     ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("               and bill_num=:billnumt ");
            parme.put("billnumt", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("             and  ctn_num=:ctnnumt  ");
            parme.put("ctnnumt", cunNum);
        }
        sb.append("			) t ON t.loading_plan_num = lor.loading_plan_num                               ");          
        sb.append("			AND lor.BILL_NUM = t.BILL_NUM                                                  ");          
        sb.append("			AND lor.SKU_ID = t.sku_id                                                      ");          
        sb.append("			LEFT JOIN bis_loading_order lo ON lo.order_num = lor.loading_plan_num          ");          
        sb.append("			WHERE                                                                          ");          
        sb.append("				lo.order_state = '4'                                                         "); 
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append("                   and lo.stock_id =:sockidt");
            parme.put("sockidt", stockIn);
        }
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        sb.append("			GROUP BY                                                                         ");          
        sb.append("				t.stock_id,                                                                  ");          
        sb.append("				t.bill_num,                                                                  ");          
        sb.append("				t.ctn_num,                                                                   ");          
        sb.append("				t.sku_id,                                                                    ");          
        sb.append("				t.enter_state,                                                               ");          
        sb.append("				lor.loading_tiem,                                                            ");          
        sb.append("				lor.stock_name,                                                              ");          
        sb.append("				lo.receiver_name,                                                            ");          
        sb.append("				lo.receiver_id,                                                              ");          
        sb.append("				a.is_bonded,                                                                 ");          
        sb.append("				t.cargo_location                                                             ");          
        sb.append("		) aa                                                                             ");          
        sb.append("	LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                             ");          
        sb.append("	UNION ALL                                                                          ");          
        sb.append("		SELECT                                                                           ");          
        sb.append("			t.stock_name AS client_name,                                                   ");          
        sb.append("			t.stock_in,                                                                    ");          
        sb.append("			'货转入' AS leixing,                                                           ");          
        sb.append("			'3' AS leixing_sort,                                                           ");          
        sb.append("			'' AS receiver_name,                                                           ");          
        sb.append("			'' AS receiver_id,                                                             ");          
        sb.append("			t.bill_num,                                                                    ");          
        sb.append("			t.ctn_num,                                                                     ");         
        sb.append("			t.sku_id,                                                                      ");         
        sb.append("			t.cargo_name,                                                                  ");         
        sb.append("			s.type_name,                                                                   ");         
        sb.append("			s.cargo_type,                                                                  ");         
        sb.append("			s.class_name,                                                                  ");         
        sb.append("			s.class_type,                                                                  ");         
        sb.append("			t.cargo_location,                                                              ");         
        sb.append("			a.is_bonded,                                                                   ");         
        sb.append("			ts.operate_time AS churuku_date,                                               ");         
        sb.append("			(                                                                              ");         
        sb.append("				CASE t.enter_state                                                           ");         
        sb.append("				WHEN '0' THEN                                                                ");         
        sb.append("					'INTACT'                                                                   ");         
        sb.append("				WHEN '1' THEN                                                                ");         
        sb.append("					'BROKEN'                                                                   ");         
        sb.append("				WHEN '2' THEN                                                                ");         
        sb.append("					'COVER TORN'                                                               ");         
        sb.append("				END                                                                          ");         
        sb.append("			) AS enter_state,                                                              ");         
        sb.append("			t.now_piece AS piece_sum,                                                      ");         
        sb.append("			t.now_piece * s.net_single AS net_weight_sum,                                  ");         
        sb.append("			t.now_piece * s.gross_single AS gross_weight_sum                               ");          
        sb.append("		FROM                                                                             ");          
        sb.append("			bis_tray_info t                                                                ");          
        sb.append("		LEFT JOIN bis_asn a ON t.asn = a.asn                                             ");          
        sb.append("		LEFT JOIN base_sku_base_info s ON s.sku_id = t.sku_id                            ");          
        sb.append("		LEFT JOIN bis_transfer_stock ts ON ts.transfer_id = t.contact_num                ");          
        sb.append("		WHERE                                                                            ");          
        sb.append("			t.if_transfer = '1' AND t.contact_type = '3'                                 ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND a.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append(" and t.stock_in =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and t.bill_num=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append(" and t.ctn_num=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and ts.operate_time >= to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and ts.operate_time < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        sb.append("		ORDER BY leixing_sort,bill_num,ctn_num                                           ");
        count.append(sb.toString());
        count.append(" ) ");
        SQLQuery sqlQuery = createSQLQuery(count.toString(), parme);
        return sqlQuery.list()!=null?sqlQuery.list().size():0;
	}

    /**
     * 根据提单号获取检验单号
     *
     * @param billNum
     * @return
     */
    public String getBillNumToJY(String billNum) {
        if (billNum != null && !"".equals(billNum)) {
            HashMap<String, Object> parme = new HashMap<String, Object>();
            String hql = "from BisCiqDeclaration where billNum=:bill";
            parme.put("bill", billNum);
            List<BisCiqDeclaration> getObjList = this.find(hql, parme);
            if (null != getObjList && getObjList.size() > 0) {
                BisCiqDeclaration getObj = getObjList.get(0);
                return getObj.getCiqCode();
            }
        }
        return "";
    }


    /**
     * 根据提单号和日期获取肉类出入库统计
     *
     * @param billNum
     * @param starTime
     * @param endTime
     * @param npage
     * @param pageSize
     * @return
     */
    public Map<String, Object> getMeatBillNumTrayInfo(String billNum, String starTime, String endTime, int npage, int pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (billNum != null && !"".equals(billNum)) {
            //获取检验号
            String getJYCode = getBillNumToJY(billNum);
            StringBuffer sql = new StringBuffer();
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("billNum", billNum);
            StringBuffer sqlO = new StringBuffer("");
            StringBuffer sqlT = new StringBuffer("");
            if (starTime != null && !"".equals(starTime)) {
                sqlO.append(" and trunc(a.enter_stock_time,'mm')>=to_date(:starTime,'yyyy-mm-dd')");
                sqlT.append(" and trunc(a.LOADING_TIME,'mm')>=to_date(:starTime,'yyyy-mm-dd')");
                parme.put("starTime", starTime);
            }
            if (starTime != null && !"".equals(starTime)) {
                sqlO.append(" and trunc(a.enter_stock_time,'mm')<=to_date(:endTime,'yyyy-mm-dd') ");
                sqlT.append(" and trunc(a.LOADING_TIME,'mm')<=to_date(:endTime,'yyyy-mm-dd') ");
                parme.put("endTime", endTime);
            }
            sql.append("  select 1 as type,' ' as IDS, min(a.stock_name)as stock_name,min(a.enter_stock_time) as stock_time,min(a.cargo_name) as cargo_name, '").append(getJYCode).append("' as JY ");
            sql.append("    ,sum(ORIGINAL_PIECE-REMOVE_PIECE) as piece,sum((ORIGINAL_PIECE-REMOVE_PIECE)*a.NET_SINGLE) as  SUM_NET_WEIGHT,a.CTN_NUM,min(a.bill_num)  as bill_num ");
            sql.append("    ,wm_concat(a.FLOOR_NUM ||a.ROOM_NUM) as lab ");
            sql.append("  from ");
            sql.append("     (select * from bis_tray_info  t where t.bill_num=:billNum ").append(sqlO.toString()).append(" ) a  ");
            sql.append("     left join   base_sku_base_info sku on sku.sku_id=a.sku_id   ");
            sql.append("  where sku.cargo_type=1 and a.CONTACT_TYPE='1'group by a.sku_id,a.ctn_num   ");
            sql.append(" union ");
            sql.append("  select 2 as type,' ' as IDS,min(outs.receiver) as stock_name,trunc(b.LOADING_TIME,'dd') as stock_time,min(b.cargo_name) as cargo_name, '").append(getJYCode).append("' as JY ");
            sql.append("     ,sum(b.piece) as piece,sum(b.net_weight)as SUM_NET_WEIGHT ,b.ctn_num,min(b.bill_num) as bill_num ");
            sql.append("     ,wm_concat(b.FLOOR_NUM ||b.ROOM_NUM) as lab ");
            sql.append("  from ");
            sql.append("      ( select * from bis_loading_info l where l.bill_num=:billNum and l.LOADING_STATE=2 ").append(sqlT.toString()).append(") b  ");
            sql.append("      left join base_sku_base_info sku on sku.sku_id=b.sku_id  ");
            sql.append("      left join bis_out_stock outs on outs.out_link_id=b.out_link_id ");
            sql.append("  where sku.cargo_type=1  group by b.sku_id,b.ctn_num,trunc(b.LOADING_TIME,'dd') ");

            //long totalCount=getCountBySql(sql.toString(), parme);
            SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
            if (pageSize > 0) {
                long totalCount = countSqlResult(sql.toString(), parme);
                sqlQuery.setFirstResult(PageUtils.calBeginIndex(npage, pageSize, Integer.valueOf(String.valueOf(totalCount))));
                sqlQuery.setMaxResults(pageSize);
                returnMap.put("total", totalCount);
            }
            returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
        }
        return returnMap;
    }

    /**
     * 根据提单号和日期获取水产出入库统计
     *
     * @param starTime
     * @param endTime
     * @param npage
     * @param pageSize
     * @return
     */
    public Map<String, Object> getFisheriesBillNumTrayInfo(String starTime, String endTime, int npage, int pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (starTime != null && !"".equals(starTime)) {
            StringBuffer sql = new StringBuffer();
            HashMap<String, Object> parme = new HashMap<String, Object>();
            parme.put("starTime", starTime);
            sql.append(" select trunc(instore.stock_time,'dd') as tdate,instore.bill_num,ciq.CIQ_CODE,instore.cargo_name,instore.SUM_NET_WEIGHT as ISUM_NET_WEIGHT,ciq.TRADE_TYPE, ");
            sql.append(" outstore.lab,outstore.stock_time,ciq.CERTIFICATE_TIME,outstore.piece, nvl(outstore.SUM_NET_WEIGHT,0) as OSUM_NET_WEIGHT,outstore.stock_name,instore.sku_id ");
            sql.append(" from  ");
            sql.append(" ( ");
            sql.append(" select 1 as type, a.bill_num,a.sku_id,min(a.stock_name)as stock_name,trunc(a.enter_stock_time,'dd') as stock_time,min(a.cargo_name) as cargo_name ");
            sql.append(" ,sum(ORIGINAL_PIECE-REMOVE_PIECE) as piece,sum((ORIGINAL_PIECE-REMOVE_PIECE)*a.NET_SINGLE) as  SUM_NET_WEIGHT,a.CTN_NUM ");
            sql.append(" ,wm_concat(a.FLOOR_NUM ||a.ROOM_NUM) as lab ");
            sql.append(" from  ");
            sql.append(" (select * from bis_tray_info  t where trunc(t.enter_stock_time,'mm')>=to_date(:starTime,'yyyy-mm')    ");
            if (endTime != null && !"".equals(endTime)) {
                sql.append("  and trunc(t.enter_stock_time,'mm')<= to_date(:endTime,'yyyy-mm') ");
                parme.put("endTime", endTime);
            }
            sql.append("  ) a  ");
            sql.append("    left join   base_sku_base_info sku on sku.sku_id=a.sku_id  ");
            sql.append("  where sku.cargo_type=2 and a.CONTACT_TYPE='1' ");
            sql.append("  group by a.bill_num,a.sku_id,a.ctn_num,trunc(a.enter_stock_time,'dd') ");
            sql.append("  )  instore  ");
            sql.append("  left join  BIS_CIQ_DECLARATION ciq on ciq.BILL_NUM=instore.bill_num and ciq.IN_OUT_SIGN=1    ");
            sql.append(" left join ");
            sql.append(" (  ");
            sql.append(" select 2 as type,b.bill_num ,b.sku_id , ");
            sql.append("  min(outs.receiver) as stock_name,trunc(b.LOADING_TIME,'dd') as stock_time, ");
            sql.append("  min(b.cargo_name) as cargo_name,sum(b.piece) as piece,sum(b.net_weight)as SUM_NET_WEIGHT,b.ctn_num ");
            sql.append(" ,wm_concat(b.FLOOR_NUM ||b.ROOM_NUM) as lab ");
            sql.append(" from ");
            sql.append("   ( select * from bis_loading_info l where  l.LOADING_STATE=2 and trunc(l.LOADING_TIME,'mm')>=to_date(:starTime,'yyyy-mm') ");
            if (endTime != null && !"".equals(endTime)) {
                sql.append("     and trunc(l.LOADING_TIME,'mm')<=to_date(:endTime,'yyyy-mm') ");
                parme.put("endTime", endTime);
            }
            sql.append("    ) b     ");
            sql.append("   left join base_sku_base_info sku on sku.sku_id=b.sku_id  ");
            sql.append("   left join bis_out_stock outs on outs.out_link_id=b.out_link_id ");
            sql.append("  where sku.cargo_type=2  ");
            sql.append("  group by b.bill_num,b.sku_id,b.ctn_num,trunc(b.LOADING_TIME,'dd') ");
            sql.append("  )  outstore  on instore.bill_num=outstore.bill_num and instore.sku_id=outstore.sku_id ");
            sql.append("  order by instore.bill_num,sku_id,outstore.stock_time ");

            SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
            if (pageSize > 0) {
                long totalCount = countSqlResult(sql.toString(), parme);
                sqlQuery.setFirstResult(PageUtils.calBeginIndex(npage, pageSize, Integer.valueOf(String.valueOf(totalCount))));
                sqlQuery.setMaxResults(pageSize);
                returnMap.put("total", totalCount);
            }
            returnMap.put("rows", sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
        }
        return returnMap;
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStockNameByBill(String billNum) {
        StringBuffer sql = new StringBuffer();
        sql.append("select min(t.stock_in) as stockName from bis_enter_stock t where t.item_num = ?0 ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), billNum);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
    
    @SuppressWarnings("unchecked")
	public List getTrayInfoByTrayId(String trayId) {
        StringBuffer sql = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sql.append("select t.tray_id from bis_tray_info t where t.tray_id = :trayId ");
        parme.put("trayId", trayId);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        sqlQuery.addScalar("tray_id");  
        sqlQuery.setResultTransformer(Transformers.aliasToBean(Stock.class));
        return sqlQuery.list();
    }
    
    /**
     * 查询 在库明细页面数据
     * @param page
     * @param itemNum
     * @param cunNum
     * @param stockIn
     * @param linkId
     * @param strTime
     * @param endTime
     * @param locationType
     * @return
     */
    public Page<Stock> getStockStocks(Page<Stock> page,String ifBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime, String locationType) {
    	StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" SELECT  ");
        sb.append("  tray.stock_in AS clientId, ");
        sb.append("  tray.stock_name AS clientName,");
        sb.append("  (CASE st.IF_BONDED WHEN '1' THEN '1' ELSE '0' END) AS isBonded,");
        sb.append("  tray.bill_num AS billCode, ");
        sb.append("  tray.ctn_num AS ctnNum, ");
        sb.append("  nvl(info.bgdh,st.bgdh) as bgdh, ");
        sb.append("  nvl(info.ycg,st.ycg) as ycg, ");
        sb.append("  TO_CHAR (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd') AS bgdhdate,");
        sb.append("  tray.sku_id AS sku, ");
        sb.append("  st.CTN_TYPE_SIZE AS cz, ");
        sb.append("  s.type_name AS bigName, ");
        sb.append("  s.cargo_type AS bigType,  ");
        sb.append("  s.class_name AS simName, ");
        sb.append("  s.class_type AS simType, ");
        sb.append("  tray.CARGO_NAME AS cargoName, ");
        sb.append("  tray.rkTime, ");
        sb.append("  tray.enter_state AS state,  ");
        sb.append("  SUM (tray.num) AS nowNum,  ");
        sb.append("  SUM (tray.net_weight) AS allnet,");
        sb.append("  SUM (tray.gross_weight) AS allgross, ");
        sb.append("  tray.CONTACT_NUM AS contactCode,");
        sb.append("  st. OPERATOR AS createUser, ");
        sb.append("  tray.asn   ");
        if ("1".equals(locationType)) {
            sb.append(" ,tray.cargo_location AS locationCode  ");
        }
      //  sb.append("	,info.HS_CODE as hsCode,	info.ACCOUNT_BOOK as accountBook, info.HS_ITEMNAME as hsItemname");
        sb.append(",decode(info.HS_CODE,NULL,bai.HS_CODE,info.HS_CODE) as hsCode, ");
        sb.append(" decode(info.ACCOUNT_BOOK,NULL,bai.ACCOUNT_BOOK ,info.ACCOUNT_BOOK) as accountBook, ");
        sb.append(" decode(info.HS_ITEMNAME,NULL,bai.HS_ITEMNAME,info.HS_ITEMNAME) as hsItemname");
        sb.append(" FROM(  ");
        sb.append(" SELECT  ");
        sb.append("  tray.stock_in,  ");
        sb.append("  tray.stock_name,  ");
        sb.append("  tray.bill_num, ");
        sb.append("  tray.ctn_num, ");
        sb.append("  tray.sku_id,");
        sb.append("  SUM (tray.now_piece) AS num,");
        sb.append(" (CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state, ");
        sb.append(" tray.CARGO_NAME,  ");
        sb.append(" SUM (tray.net_weight) AS net_weight, ");
        sb.append(" SUM (tray.gross_weight) AS gross_weight,");
        sb.append(" tray.CONTACT_NUM,  ");
        sb.append(" to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd') AS rkTime, ");
        sb.append(" tray.asn,  ");
        sb.append(" tray.cargo_location ");
        sb.append(" FROM   ");
        sb.append(" BIS_TRAY_INFO tray ");
        sb.append(" WHERE 1 = 1  ");
        sb.append(" AND (tray.cargo_state = '01' OR tray.cargo_state = '10' ) ");
        sb.append(" AND tray.now_piece != 0  ");
        sb.append("  GROUP BY         ");
        sb.append("  tray.stock_in,   ");
        sb.append("  tray.stock_name, ");
        sb.append("  tray.bill_num,   ");
        sb.append("  tray.ctn_num,    ");
        sb.append("  tray.sku_id,      ");
        sb.append("  tray.enter_state, ");
        sb.append("  tray.CARGO_NAME,  ");
        sb.append("  tray.CONTACT_NUM, ");
        sb.append("  tray.asn,");
        sb.append("  tray.enter_state,  ");
        sb.append("  to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd'), ");
        sb.append("  tray.cargo_location ");
        sb.append(" ) tray ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (  ");
        sb.append(" 	tray.bill_num = st.ITEM_NUM   ");
        sb.append(" 	AND tray.CONTACT_NUM = st.LINK_ID  ");
        sb.append(" )     ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON ( ");
        sb.append(" 	st.ITEM_NUM = info.ITEM_NUM    ");
        sb.append(" 	AND st.LINK_ID = info.LINK_ID  ");
        sb.append(" 	AND tray.ctn_num = info.ctn_num ");
        sb.append(" 	AND tray.sku_id = info.sku  ");
        sb.append(" )    ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id       ");
        sb.append(" LEFT JOIN BIS_ASN_INFO bai ON bai.ASN_ID = tray.asn  AND bai.SKU_ID = tray.SKU_ID");
        sb.append(" LEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE");
        sb.append(" where 1=1 ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and tray.bill_num=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and tray.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and tray.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and tray.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND st.IF_BONDED=:ifBonded");
        		parme.put("ifBonded",ifBonded);
        	}else{
        		sb.append(" AND (st.IF_BONDED='0' or st.IF_BONDED is null)    ");
        	}
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and to_date(tray.rkTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and to_date(tray.rkTime,'yyyy-mm-dd')<=to_date(:endTime,'yyyy-mm-dd')");
            parme.put("endTime", endTime);
        }
        sb.append(" GROUP BY ");
        sb.append(" tray.stock_in, ");
        sb.append(" tray.stock_name, ");
        sb.append(" st.IF_BONDED,   ");
        sb.append(" tray.bill_num,");
        sb.append(" tray.ctn_num, ");
        sb.append(" info.bgdh,    ");
        sb.append(" info.ycg,  ");
        sb.append(" st.bgdh,");
        sb.append(" st.ycg,");
        sb.append(" to_char (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd'), ");
        sb.append(" tray.sku_id,    ");
        sb.append(" st.CTN_TYPE_SIZE,  ");
        sb.append(" s.type_name,      ");
        sb.append(" s.cargo_type,     ");
        sb.append(" s.class_name,   ");
        sb.append(" s.class_type,    ");
        sb.append(" tray.CARGO_NAME,   ");
        sb.append(" tray.rkTime,   ");
        sb.append(" tray.enter_state,  ");
        sb.append(" tray.CONTACT_NUM,  ");
        sb.append(" st. OPERATOR,   ");
        sb.append(" tray.asn      ");
        if ("1".equals(locationType)) {
        	sb.append("  ,tray.cargo_location  ");
        }
        sb.append(",info.HS_CODE,info.ACCOUNT_BOOK,info.HS_ITEMNAME,bai.HS_CODE,bai.HS_ITEMNAME,bai.ACCOUNT_BOOK ");
        sb.append(",bai.HS_CODE,bai.HS_ITEMNAME,bai.ACCOUNT_BOOK  ");
        sb.append(" ORDER BY      ");
        sb.append(" 	tray.bill_num,");
        sb.append(" 	tray.ctn_num, ");
        sb.append(" 	tray.rkTime ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("clientName", String.class);//客户名称
        paramType.put("clientId", String.class);//客户id
        paramType.put("isBonded", String.class);//货物类别
        paramType.put("billCode", String.class);//提单号
        paramType.put("bgdh", String.class);//报关单号
        paramType.put("ycg", String.class);//原产国
        paramType.put("bgdhdate", String.class);//申报日期
        paramType.put("ctnNum", String.class);//箱号
        paramType.put("sku", String.class);
        paramType.put("cz", String.class);//箱型尺寸
        paramType.put("bigName", String.class);//大类
        paramType.put("simName", String.class);//小类
        paramType.put("cargoName", String.class);
        paramType.put("state", String.class);
        paramType.put("rkTime", String.class);
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class);
        if ("1".equals(locationType)) {
        	paramType.put("locationCode", String.class);
        }
        paramType.put("contactCode", String.class);
        paramType.put("createUser", String.class);
        paramType.put("hsCode",  String.class);
        paramType.put("accountBook",  String.class);
        paramType.put("hsItemname",  String.class);
        return findPageSql(page, sb.toString(), paramType, parme);
    }
    
    
    
    /**
     *  出入库明细查询方法
     * @param page
     * @param itemNum
     * @param cunNum
     * @param stockIn
     * @param linkId
     * @param strTime
     * @param endTime
     * @param locationType
     * @return
     */
    public Page<Stock> getInOutStockStocks(Page<Stock> page,String isBonded,String itemNum, String cunNum, String stockIn, String linkId, String strTime, String endTime) {
    	StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" SELECT   ");
        sb.append("   temp.stockId as clientId, ");
        sb.append("   temp.stockName as clientName,");
        sb.append("   temp.isBonded, ");
        sb.append("   temp.reportType, ");
        sb.append("   temp.shf, ");
        sb.append("   temp.shfid,");
        sb.append("   temp.billNum as billCode,");
        sb.append("   temp.ctnNum,");
        sb.append("   temp.bgdh, ");
        sb.append("   temp.ycg,  ");
        sb.append("   temp.BGDHDATE,");
        sb.append("   temp.sku, ");
        sb.append("   temp.cz,  ");
        sb.append("   temp.bigName, ");
        sb.append("   temp.bigType, ");
        sb.append("   temp.simName, ");
        sb.append("   temp.simType, ");
        sb.append("   temp.num as allpiece, ");
        sb.append("   temp.endTime AS enterTime, ");
        sb.append("   temp.enterStats as state, ");
        sb.append("   temp.cargoName, ");
        sb.append("   temp.zjz as allnet, ");
        sb.append("   temp.zmz as allgross,");
        sb.append("   temp.linkId as contactCode, ");
        sb.append("   temp.createUser, ");
        sb.append("   temp.asn, ");
        sb.append("   temp.cargo_location as locationCode ");
        sb.append(" FROM  ");
        sb.append("   (    ");
        sb.append("   SELECT ");
        sb.append(" 	tray.stock_in AS stockId, ");
        sb.append(" 	tray.stock_name AS stockName, ");
        sb.append(" 	(CASE st.IF_BONDED WHEN '1' THEN '1' ELSE '0' END) AS isBonded,");
        sb.append(" 	(CASE asn.if_second_enter WHEN '1' THEN '入库' WHEN '3' THEN '分拣' END ) AS reportType,");
        sb.append(" 	tray.shf, ");
        sb.append(" 	'' AS shfid, ");
        sb.append(" 	tray.bill_num AS billNum, ");
        sb.append(" 	tray.ctn_num AS ctnNum, ");
        sb.append(" 	info.bgdh,");
        sb.append(" 	info.ycg, ");
        sb.append(" 	to_char(info.BGDHDATE,'yyyy-mm-dd') AS BGDHDATE, ");
        sb.append(" 	tray.sku_id AS sku,");
        sb.append(" 	st.CTN_TYPE_SIZE AS cz, ");
        sb.append(" 	s.type_name AS bigName, ");
        sb.append(" 	s.cargo_type AS bigType,");
        sb.append(" 	s.class_name AS simName,");
        sb.append(" 	s.class_type AS simType,");
        sb.append(" 	tray.num,  ");
        sb.append(" 	ASN.INBOUND_DATE AS endTime, ");
        sb.append(" 	tray.enter_state AS enterStats, ");
        sb.append(" 	tray.CARGO_NAME AS cargoName, ");
        sb.append(" 	tray.num * tray.NET_SINGLE AS zjz, ");
        sb.append(" 	tray.num * tray.GROSS_SINGLE AS zmz, ");
        sb.append(" 	tray.CONTACT_NUM AS linkId, ");
        sb.append(" 	st. OPERATOR AS createUser, ");
        sb.append(" 	tray.asn,  ");
        sb.append(" 	tray.cargo_location ");
        sb.append("  FROM   ");
        sb.append(" 	(      ");
        sb.append(" 	  SELECT ");
        sb.append(" 		tray.stock_in,   ");
        sb.append(" 		tray.stock_name, ");
        sb.append(" 		'' AS shf,   ");
        sb.append(" 	    tray.bill_num, ");
        sb.append(" 		tray.ctn_num, ");
        sb.append(" 		tray.sku_id, ");
        sb.append(" 		SUM (tray.original_piece - tray.remove_piece) AS num, ");
        sb.append(" 		(CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state, ");
        sb.append(" 		tray.CARGO_NAME,   ");
        sb.append(" 		MAX (tray.NET_SINGLE) AS NET_SINGLE, ");
        sb.append(" 		MAX (tray.GROSS_SINGLE) AS GROSS_SINGLE,   ");
        sb.append(" 		tray.CONTACT_NUM,  ");
        sb.append(" 		tray.asn, ");
        sb.append(" 		tray.cargo_location ");
        sb.append(" 	FROM    ");
        sb.append(" 		BIS_TRAY_INFO tray  ");
        sb.append(" 	WHERE     ");
        sb.append(" 	    1 = 1 AND tray.if_transfer = '0'   ");
        sb.append(" 	GROUP BY   ");
        sb.append(" 		tray.stock_in,    ");
        sb.append(" 		tray.stock_name,  ");
        sb.append(" 		tray.bill_num,  ");
        sb.append(" 		tray.ctn_num, ");
        sb.append(" 		tray.sku_id,  ");
        sb.append(" 		tray.enter_state, ");
        sb.append(" 		tray.CARGO_NAME,  ");
        sb.append(" 		tray.CONTACT_NUM, ");
        sb.append(" 		tray.asn, ");
        sb.append(" 		tray.enter_state, ");
        sb.append(" 		tray.cargo_location  ");
        sb.append("   ) tray   ");
        sb.append(" 	LEFT JOIN BIS_ASN asn ON tray.ASN = asn.ASN  ");
        sb.append(" 	LEFT JOIN BIS_ENTER_STOCK st ON (tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID ) ");
        sb.append(" 	LEFT JOIN BIS_ENTER_STOCK_INFO info ON (  ");
        sb.append(" 		st.ITEM_NUM = info.ITEM_NUM       ");
        sb.append(" 		AND st.LINK_ID = info.LINK_ID     ");
        sb.append(" 		AND tray.ctn_num = info.ctn_num   ");
        sb.append(" 		AND tray.sku_id = info.sku        ");
        sb.append(" 	)     ");
        sb.append("    LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id  ");
        sb.append("    UNION ALL   ");
        sb.append(" 	SELECT  ");
        sb.append(" 	ST.STOCK_IN_ID AS stockId,");
        sb.append(" 	ST.STOCK_IN AS stockName, ");
        sb.append(" 	'0' AS isBonded,     ");
        sb.append(" 	'出库' AS reportType,   ");
        sb.append(" 	ST.RECEIVER AS shf,   ");
        sb.append(" 	ST.RECEIVER_ID AS shfid, ");
        sb.append(" 	tray.bill_num AS billNum, ");
        sb.append(" 	tray.ctn_num AS ctnNum,");
        sb.append(" 	st.CD_NUM AS bgdh, ");
        sb.append(" 	'' AS ycg,  ");
        sb.append(" 	'' AS BGDHDATE, ");
        sb.append(" 	tray.sku_id AS sku,");
        sb.append(" 	'' AS cz,  ");
        sb.append(" 	s.type_name AS bigName,  ");
        sb.append(" 	s.cargo_type AS bigType,  ");
        sb.append(" 	s.class_name AS simName,  ");
        sb.append(" 	s.class_type AS simType,  ");
        sb.append(" 	tray.num,   ");
        sb.append(" 	tray.LOADING_TIME AS endTime, ");
        sb.append(" 	tray.enter_state AS enterStats,");
        sb.append(" 	tray.CARGO_NAME AS cargoName, ");
        sb.append(" 	tray.zjz,");
        sb.append(" 	tray.zmz, ");
        sb.append(" 	TRAY.CONTACT_NUM AS linkId,");
        sb.append(" 	st. OPERATOR AS createUser, ");
        sb.append(" 	tray.asn_id AS asn,  ");
        sb.append(" 	tray.cargo_location ");
        sb.append(" FROM   ");
        sb.append(" 	(   ");
        sb.append(" 	  SELECT   ");
        sb.append(" 		L.OUT_LINK_ID AS CONTACT_NUM, ");
        sb.append(" 		L.BILL_NUM,  ");
        sb.append(" 		L.LOADING_PLAN_NUM, ");
        sb.append(" 		L.CARGO_LOCATION, ");
        sb.append("         L.LOADING_TIME,");
        sb.append(" 		(        ");
        sb.append(" 		CASE L.enter_state  ");
        sb.append(" 			WHEN '0' THEN ");
        sb.append(" 			'INTACT' ");
        sb.append(" 			WHEN '1' THEN    ");
        sb.append(" 			'BROKEN'    ");
        sb.append(" 			WHEN '2' THEN   ");
        sb.append(" 			'COVER TORN'  ");
        sb.append(" 			END  ");
        sb.append(" 	    ) AS enter_state, ");
        sb.append(" 		L.ASN_ID,  ");
        sb.append(" 		L.SKU_ID, ");
        sb.append(" 		L.CARGO_NAME, ");
        sb.append(" 		L.CTN_NUM, ");
        sb.append(" 		SUM (L.PIECE) AS num,  ");
        sb.append(" 		SUM (L.net_weight) AS zjz, ");
        sb.append(" 		SUM (L.GROSS_WEIGHT) AS zmz   ");
        sb.append(" 	FROM   ");
        sb.append(" 		BIS_LOADING_INFO L  ");
        sb.append(" 	WHERE    ");
        sb.append(" 		L.LOADING_STATE = '2'  ");
        sb.append(" 	GROUP BY    ");
        sb.append("         L.LOADING_TIME, ");
        sb.append(" 	    L.OUT_LINK_ID,  ");
        sb.append(" 		L.BILL_NUM,  ");
        sb.append(" 		L.LOADING_PLAN_NUM, ");
        sb.append(" 		L.CARGO_LOCATION, ");
        sb.append(" 		L.enter_state, ");
        sb.append(" 		L.ASN_ID, ");
        sb.append(" 		L.SKU_ID,  ");
        sb.append(" 		L.CARGO_NAME, ");
        sb.append(" 		L.CTN_NUM ");
        sb.append("  ) tray  ");
        sb.append(" 	LEFT JOIN BIS_OUT_STOCK st ON (  ");
        sb.append(" 	st.OUT_LINK_ID = tray.CONTACT_NUM ");
        sb.append("  )     ");
        sb.append(" 	LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id  ");
        sb.append(" UNION ALL ");
        sb.append(" SELECT   ");
        sb.append(" M.STOCK_IN_ID AS stockId, ");
        sb.append(" M.STOCK_IN AS stockName,  ");
        sb.append(" '0' AS isBonded,  ");
        sb.append(" '货转' AS reportType,  ");
        sb.append(" M.RECEIVER_NAME AS shf,  ");
        sb.append(" M.RECEIVER AS shfid,");
        sb.append(" T .bill_num AS billNum,");
        sb.append(" T .ctn_num AS ctnNum, ");
        sb.append(" M.CD_NUM AS bgdh, ");
        sb.append(" '' AS ycg,  ");
        sb.append(" '' AS BGDHDATE,");
        sb.append(" T .sku_id AS sku, ");
        sb.append(" '' AS cz, ");
        sb.append(" s.type_name AS bigName, ");
        sb.append(" s.cargo_type AS bigType,");
        sb.append(" s.class_name AS simName,");
        sb.append(" s.class_type AS simType,");
        sb.append(" SUM (T.PIECE) AS num,  ");
        sb.append(" NVL(M.START_STORE_DATE-1,M.OPERATE_TIME-1) AS endTime,    ");
        sb.append(" (  ");
        sb.append(" CASE T.enter_state  ");
        sb.append(" WHEN '0' THEN ");
        sb.append(" 'INTACT'  ");
        sb.append(" WHEN '1' THEN ");
        sb.append(" 'BROKEN' ");
        sb.append(" WHEN '2' THEN ");
        sb.append(" 'COVER TORN' ");
        sb.append(" END  ");
        sb.append(" ) AS enterStats,  ");
        sb.append(" T.CARGO_NAME AS cargoName,  ");
        sb.append(" SUM (T.NET_WEIGHT) AS zjz,  ");
        sb.append(" SUM (T.GROSS_WEIGHT) AS zmz,");
        sb.append(" M.transfer_id AS linkId,    ");
        sb.append(" M.OPERATOR AS createUser,   ");
        sb.append(" '' AS asn,                  ");
        sb.append(" '' AS cargo_location        ");
        sb.append(" FROM                          ");
        sb.append(" BIS_TRANSFER_STOCK M   ");
        sb.append(" LEFT JOIN BIS_TRANSFER_STOCK_INFO T ON M .transfer_id = T .transfer_link_id ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = T.sku_id ");
        sb.append(" GROUP BY  ");
        sb.append(" s.type_name, ");
        sb.append(" s.cargo_type,");
        sb.append(" s.class_name, ");
        sb.append(" s.class_type, ");
        sb.append(" M.CD_NUM,           ");
        sb.append(" M.STOCK_IN,         ");
        sb.append(" M.STOCK_IN_ID,      ");
        sb.append(" M.START_STORE_DATE, ");
        sb.append(" M.OPERATE_TIME,   ");
        sb.append(" T .bill_num,      ");
        sb.append(" T .ctn_num,       ");
        sb.append(" T .sku_id,        ");
        sb.append(" T.enter_state,    ");
        sb.append(" T.CARGO_NAME,     ");
        sb.append(" M.transfer_id,    ");
        sb.append(" M.RECEIVER_NAME,  ");
        sb.append(" M.OPERATOR,   ");
        sb.append(" M.RECEIVER   ");
        sb.append(" ) temp      ");
        sb.append(" where 1=1 ");
        if(null!=isBonded&&!"".equals(isBonded)){
             sb.append(" AND temp.isBonded=:isBonded");
             parme.put("isBonded",isBonded);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append(" and temp.stockId =:sockid");
            parme.put("sockid", stockIn);
        }
        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append("   and temp.billNum=:billnum ");
            parme.put("billnum", itemNum);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append("   and  temp.ctnNum=:ctnnum  ");
            parme.put("ctnnum", cunNum);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and temp.endTime>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and temp.endTime<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
        }
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("clientName", String.class);//客户名称
        paramType.put("clientId", String.class);//客户id
        paramType.put("isBonded", String.class);//保税非保税
        paramType.put("reportType", String.class);//类型
        paramType.put("shf", String.class);//【赋值】收货方名称
        paramType.put("billCode", String.class);//提单号
        paramType.put("ctnNum", String.class);//箱号
        paramType.put("bgdh", String.class);//报关箱号
        paramType.put("ycg", String.class);//原产国
        paramType.put("bgdhdate", String.class);//报关申报日期
        paramType.put("sku", String.class);//sku
        paramType.put("cz", String.class);//尺寸
        paramType.put("bigName", String.class);//大类
        paramType.put("simName", String.class);//小类
        paramType.put("allpiece", Integer.class);//件数
        paramType.put("enterTime",Date.class);//【赋值】出入库时间
        paramType.put("state", String.class);//货物状态
        paramType.put("cargoName", String.class);//货物名称
        paramType.put("allnet", Double.class);//总净重
        paramType.put("allgross", Double.class);//总毛重
        paramType.put("asn", String.class);//【赋值】
        paramType.put("contactCode", String.class);//联系单号
        paramType.put("createUser", String.class);//客服人员
        paramType.put("locationCode", String.class);//库位
        //paramType.put("areaNum", String.class);
        //paramType.put("roomNum", String.class);
        //paramType.put("floorNum", String.class);
        return findPageSql(page, sb.toString(), paramType, parme);
    }
    
    public Page<Stock> getBoxCheckStocks(Page<Stock> page,String ifBonded,String itemNum, String cunNum, String stockIn,String strTime, String endTime) {
    	StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sb.append(" SELECT ");
        sb.append("  TEMP.enterTime AS bgdhdate, ");
        sb.append("  TEMP.ISBONDED, ");
        sb.append("  TEMP.clientId,  ");
        sb.append("  TEMP.clientName, ");
        sb.append("  TEMP.billCode, ");
        sb.append("  TEMP.ctnNum, ");
        sb.append("  TEMP.nowNum, ");
        sb.append("  TEMP.allnet,  ");
        sb.append("  TEMP.allgross, ");
        sb.append("  TEMP.bigName, ");
        sb.append("  TEMP.simName, ");
        sb.append("  TEMP.cargoName,");
        sb.append("  TEMP.contactCode, ");
        sb.append("  TEMP.createUser  ");
        sb.append(" FROM(  ");
        sb.append(" SELECT(  ");
        sb.append(" CASE WHEN TEMP.lxType = '倒箱' THEN to_char(ST.BACKDATE,'yyyy-mm-dd') ELSE to_char(ST.ETA_WAREHOUSE,'yyyy-mm-dd') END ) AS enterTime,");
        sb.append(" TEMP.lxType AS isBonded, ");
        sb.append(" st.STOCK_ID AS clientId,");
        sb.append(" st.STOCK_IN AS clientName,  ");
        sb.append(" TEMP.item_num AS billCode, ");
        sb.append(" TEMP.ctn_num AS ctnNum, ");
        sb.append(" TEMP.nowNum, ");
        sb.append(" TEMP.allnet, ");
        sb.append(" TEMP.allgross,");
        sb.append(" TEMP.big_type_name AS bigName, ");
        sb.append(" TEMP.little_type_name AS simName,");
        sb.append(" TEMP.cargo_name AS cargoName,");
        sb.append(" TEMP.link_id AS contactCode,");
        sb.append(" st. OPERATOR AS createUser ");
        sb.append(" FROM(     ");
        sb.append("   SELECT  ");
        sb.append(" 	'倒箱' AS lxType,   ");
        sb.append(" 	INFO.link_id, ");
        sb.append(" 	INFO.item_num, ");
        sb.append(" 	INFO.ctn_num, ");
        sb.append(" 	SUM (INFO.piece) AS nowNum, ");
        sb.append(" 	SUM (info.gross_weight) AS allnet, ");
        sb.append(" 	SUM (info.net_weight) AS allgross, ");
        sb.append(" 	INFO.big_type_name, ");
        sb.append(" 	INFO.little_type_name, ");
        sb.append(" 	INFO.cargo_name   ");
        sb.append(" FROM   ");
        sb.append(" 	BIS_ENTER_STOCK_INFO info   ");
        sb.append(" WHERE  ");
        sb.append(" 	INFO.IF_BACK = '1'  ");
        sb.append(" GROUP BY   ");
        sb.append(" 	INFO.link_id, ");
        sb.append(" 	INFO.item_num,");
        sb.append(" 	INFO.ctn_num,");
        sb.append(" 	INFO.big_type_name,");
        sb.append(" 	INFO.little_type_name, ");
        sb.append(" 	INFO.cargo_name  ");
        sb.append(" UNION ALL ");
        sb.append(" SELECT     ");
        sb.append(" 	'查验' AS lxType,   ");
        sb.append(" 	INFO.link_id, ");
        sb.append(" 	INFO.item_num,");
        sb.append(" 	INFO.ctn_num, ");
        sb.append(" 	SUM (INFO.piece) AS nowNum,  ");
        sb.append(" 	SUM (info.gross_weight) AS allnet,  ");
        sb.append(" 	SUM (info.net_weight) AS allgross,  ");
        sb.append(" 	INFO.big_type_name, ");
        sb.append(" 	INFO.little_type_name, ");
        sb.append(" 	INFO.cargo_name ");
        sb.append(" FROM   ");
        sb.append(" 	BIS_ENTER_STOCK_INFO info ");
        sb.append(" WHERE   ");
        sb.append(" 	INFO.IF_CHECK = '1' ");
        sb.append(" GROUP BY   ");
        sb.append(" 	INFO.link_id,   ");
        sb.append(" 	INFO.item_num,  ");
        sb.append(" 	INFO.ctn_num,  ");
        sb.append(" 	INFO.big_type_name,  ");
        sb.append(" 	INFO.little_type_name,  ");
        sb.append(" 	INFO.cargo_name   ");
        sb.append(" ) temp ");
        sb.append("     LEFT JOIN BIS_ENTER_STOCK st ON TEMP.LINK_ID = ST.LINK_ID ");
        sb.append(" 	AND TEMP.ITEM_NUM = ST.ITEM_NUM  ");
        sb.append(" ) temp ");
        sb.append(" where 1=1 ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and temp.billCode=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and temp.clientId=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and temp.ctnNum=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	sb.append(" AND temp.ISBONDED=:ifBonded");
        	if("1".equals(ifBonded)){
        		parme.put("ifBonded","倒箱");
        	}else{
        		parme.put("ifBonded","查验");
        	}
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')<=to_date(:endTime,'yyyy-mm-dd') ");
            parme.put("endTime", endTime);
        }
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("bgdhdate",String.class);//日期
        paramType.put("isBonded",String.class);//类型
        paramType.put("clientName", String.class);//客户名称
        paramType.put("clientId", String.class);//客户id
        paramType.put("billCode", String.class);//提单号
        paramType.put("ctnNum", String.class);//箱号
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class);
        paramType.put("bigName", String.class);//大类
        paramType.put("simName", String.class);//小类
        paramType.put("cargoName", String.class);
        paramType.put("contactCode", String.class);
        paramType.put("createUser", String.class);
        return findPageSql(page, sb.toString(), paramType, parme);
    }
    
    
    public Page<Stock> getCompanyStocks(Page<Stock> page,String reportType,String itemNum,String stockIn,String strTime, String endTime) {
    	StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
     	sb.append(" SELECT    ");
     	sb.append("  temp.enterTime AS bgdhdate, ");
     	sb.append("  temp.reportType, ");
     	sb.append("  temp.contactCode, ");
     	sb.append("  temp.createUser, ");
     	sb.append("  temp.clientId, ");
     	sb.append("  temp.clientName,");
     	sb.append("  temp.shfId,");
     	sb.append("  temp.SHF, ");
     	sb.append("  temp.billCode, ");
     	sb.append("  temp.companyId, ");
     	sb.append("  temp.companyName,");
     	sb.append("  temp.companyNum, ");
     	sb.append("  temp.ciqId,");
     	sb.append("  temp.ciqName, ");
     	sb.append("  temp.ciqNum,");
     	sb.append("  temp.orgId, ");
     	sb.append("  temp.orgName, ");
        sb.append("  temp.spNum, ");
        sb.append("  temp.REMARK ");
     	sb.append(" FROM(        ");
     	sb.append("   SELECT      ");
     	sb.append(" 	TO_CHAR (T .OPERATE_TIME,'yyyy-mm-dd') AS enterTime, ");
     	sb.append(" 	'入库' AS reportType,      ");
     	sb.append(" 	T .link_id AS contactCode,  ");
     	sb.append(" 	T . OPERATOR AS createUser,  ");
     	sb.append(" 	T .STOCK_ID AS clientId, ");
     	sb.append(" 	T .STOCK_IN AS clientName, ");
     	sb.append(" 	'' AS shfId, ");
     	sb.append(" 	'' AS SHF,  ");
     	sb.append(" 	T .ITEM_NUM AS billCode, ");
     	sb.append(" 	T .CUSTOMS_COMPANY_ID AS companyId, ");
     	sb.append(" 	T .CUSTOMS_COMPANY AS companyName, ");
     	sb.append(" 	SUM (CASE WHEN T .IF_TO_CUSTOMS = '1' THEN 1 ELSE 0 END) AS companyNum,");
     	sb.append(" 	T .CIQ_COMPANY_ID AS ciqId,  ");
     	sb.append(" 	T .CIQ_COMPANY AS ciqName, ");
     	sb.append(" 	SUM (CASE WHEN T .IF_TO_CIQ = '1' THEN 1 ELSE 0 END) AS ciqNum, ");
     	sb.append(" 	T .STOCK_ORG_ID AS orgId,  ");
     	sb.append(" 	T .STOCK_ORG AS orgName,  ");
        sb.append("     SUM (CASE WHEN T .IF_RECORD = '1' THEN 1 ELSE 0 END) AS spNum,");
        sb.append("     T .REMARK  ");
     	sb.append(" FROM    ");
     	sb.append("  BIS_ENTER_STOCK T   ");
     	sb.append(" WHERE 1 = 1 AND (T .IF_TO_CUSTOMS = '1' OR T .IF_TO_CIQ = '1' OR T .IF_RECORD = '1')   ");
     	sb.append(" GROUP BY  ");
     	sb.append("  TO_CHAR (T .OPERATE_TIME,'yyyy-mm-dd'), ");
     	sb.append("  T .link_id, ");
     	sb.append("  T . OPERATOR,");
     	sb.append("  T .STOCK_ID,");
     	sb.append("  T .STOCK_IN,  ");
     	sb.append("  T .ITEM_NUM, ");
     	sb.append("  T .CUSTOMS_COMPANY_ID, ");
     	sb.append("  T .CUSTOMS_COMPANY,");
     	sb.append("  T .CIQ_COMPANY_ID, ");
     	sb.append("  T .CIQ_COMPANY,  ");
     	sb.append("  T .STOCK_ORG_ID, ");
     	sb.append("  T .STOCK_ORG,  ");
        sb.append("  T.REMARK    ");
     	sb.append(" UNION ALL   ");
     	sb.append("  SELECT   ");
     	sb.append(" 	TO_CHAR (st.OPERATE_TIME,'yyyy-mm-dd') AS enterTime, ");
     	sb.append(" 	'出库' AS reportType, ");
     	sb.append(" 	st.OUT_LINK_ID AS contactCode, ");
     	sb.append(" 	st. OPERATOR AS createUser, ");
     	sb.append(" 	st.STOCK_IN_ID AS clientId, ");
     	sb.append(" 	st.STOCK_IN AS clientName,  ");
     	sb.append(" 	st.RECEIVER_ID AS shfId,   ");
     	sb.append(" 	st.RECEIVER AS SHF,      ");
     	sb.append(" 	MAX(info.BILL_NUM) AS billCode, ");
     	sb.append(" 	st.CUSTOMS_COMPANY_ID AS companyId, ");
     	sb.append(" 	st.CUSTOMS_COMPANY AS companyName, ");
     	sb.append(" 	NVL(st.OUT_CUSTOMS_COUNT,0) AS companyNum, ");
     	sb.append(" 	st.CIQ_COMPANY_ID AS ciqId, ");
     	sb.append(" 	st.CIQ_COMPANY AS ciqName, ");
     	sb.append(" 	NVL(st.OUT_CIQ_COUNT,0) AS ciqNum, ");
     	sb.append(" 	st.SETTLE_ORG_ID AS orgId, ");
     	sb.append(" 	st.SETTLE_ORG AS orgName,  ");
        sb.append("     NVL(st.APPROVE_COUNT,0) AS spNum, ");
        sb.append("     st.REMARK   ");
     	sb.append(" FROM     ");
     	sb.append("   bis_out_stock st ");
        sb.append(" LEFT JOIN(  ");
        sb.append("   SELECT  ");
     	sb.append(" 	INFO.OUT_LINK_ID, ");
     	sb.append(" 	LISTAGG (INFO.BILL_NUM, ',') WITHIN GROUP (ORDER BY INFO.BILL_NUM) AS BILL_NUM ");
     	sb.append("   FROM(   ");
     	sb.append(" 	SELECT DISTINCT  ");
     	sb.append(" 	 info.OUT_LINK_ID,  ");
     	sb.append(" 	 info.BILL_NUM  ");
     	sb.append(" 	FROM   ");
     	sb.append(" 	 BIS_OUT_STOCK_INFO info  ");
     	sb.append(" 	LEFT JOIN bis_enter_stock et ON info.BILL_NUM = et.item_num ");
     	sb.append(" ) info     ");
        sb.append("      GROUP BY INFO.OUT_LINK_ID       ");
        sb.append("   ) info ON ST.OUT_LINK_ID = info.OUT_LINK_ID ");
     	sb.append(" WHERE  1 = 1  ");
     	sb.append(" AND (ST.CUSTOMS_COMPANY_ID IS NOT NULL OR ST.CIQ_COMPANY_ID IS NOT NULL OR st.IF_RECORD='1')  ");
     	sb.append(" GROUP BY                                                                                      ");
     	sb.append(" 	TO_CHAR (st.OPERATE_TIME,'yyyy-mm-dd'), ");
     	sb.append(" 	st.OUT_LINK_ID,    ");
     	sb.append(" 	st. OPERATOR,");
     	sb.append("     st.STOCK_IN_ID,  ");
     	sb.append(" 	st.STOCK_IN, ");
     	sb.append(" 	st.RECEIVER_ID,");
     	sb.append(" 	st.RECEIVER,  ");
     	sb.append(" 	st.CUSTOMS_COMPANY_ID, ");
     	sb.append(" 	st.CUSTOMS_COMPANY, ");
     	sb.append(" 	st.CIQ_COMPANY_ID, ");
     	sb.append(" 	st.CIQ_COMPANY, ");
     	sb.append(" 	st.SETTLE_ORG_ID, ");
     	sb.append(" 	st.SETTLE_ORG,  ");
     	sb.append(" 	st.OUT_CUSTOMS_COUNT,");
     	sb.append(" 	st.OUT_CIQ_COUNT, ");
        sb.append("     st.APPROVE_COUNT, ");
        sb.append("     st.IF_RECORD,  ");
        sb.append("     st.REMARK  ");
     	sb.append(" 	) temp   ");
     	sb.append(" WHERE                                                                                                      ");
     	sb.append(" 	1 = 1                                                                                                  ");	
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and temp.billCode like:billnum  ");
            parme.put("billnum","%"+itemNum+"%");
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and temp.clientId=:sockid  ");
            parme.put("sockid", stockIn);
        }
        
        if(null!=reportType&&!"".equals(reportType)){
        	sb.append(" AND temp.reportType=:reportType");
        	if("1".equals(reportType)){
        		parme.put("reportType","入库");
        	}else{
        		parme.put("reportType","出库");
        	}
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')<=to_date(:endTime,'yyyy-mm-dd')");
            parme.put("endTime", endTime);
        }
        sb.append(" ORDER BY temp.enterTime DESC                                                                 ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("bgdhdate",String.class);//日期
        paramType.put("reportType",String.class);//类型
        paramType.put("contactCode", String.class);
        paramType.put("clientName", String.class);//客户名称
        paramType.put("clientId", String.class);//客户id
        paramType.put("billCode", String.class);//提单号
        paramType.put("shf", String.class);//收货方
        paramType.put("companyName", String.class);//报关公司
        paramType.put("companyNum", Integer.class);//报关数量
        paramType.put("ciqName", String.class);//报检公司
        paramType.put("ciqNum", Integer.class);//报检数量
        paramType.put("orgName", String.class);//结算单位
        paramType.put("spNum", Integer.class);//审批数量
        paramType.put("remark", String.class);//备注
        paramType.put("createUser", String.class);
        return findPageSql(page, sb.toString(), paramType, parme);
    }
    
    /**
     * 在库明细--普通客户
     *
     * @param itemNum      提单号
     * @param cunNum       厢号
     * @param stockIn      客户id
     * @param linkId       联系单号
     * @param strTime      入库时间开始
     * @param endTime      入库时间结束
     * @param locationType
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> boxCheckExcel(String ifBonded,String itemNum, String cunNum, String stockIn,String strTime, String endTime) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sb=new StringBuffer();
        sb.append(" SELECT  ");
        sb.append(" TEMP.enterTime AS bgdhdate, ");
        sb.append(" TEMP.ISBONDED,  ");
        sb.append(" TEMP.clientId,  ");
        sb.append(" TEMP.clientName,");
        sb.append(" TEMP.billCode, ");
        sb.append(" TEMP.ctnNum,");
        sb.append(" TEMP.nowNum,");
        sb.append(" TEMP.allnet, ");
        sb.append(" TEMP.allgross, ");
        sb.append(" TEMP.bigName, ");
        sb.append(" TEMP.simName, ");
        sb.append(" TEMP.cargoName,");
        sb.append(" TEMP.contactCode, ");
        sb.append(" TEMP.createUser ");
        sb.append(" FROM(  ");
        sb.append(" SELECT( ");
        sb.append(" CASE         ");
        sb.append(" 	WHEN TEMP.lxType = '倒箱' THEN  ");
        sb.append(" 	to_char(ST.BACKDATE,'yyyy-mm-dd') ");
        sb.append(" ELSE  ");
        sb.append(" 	to_char(ST.ETA_WAREHOUSE,'yyyy-mm-dd') ");
        sb.append(" END     ");
        sb.append(" ) AS enterTime,   ");
        sb.append(" TEMP.lxType AS isBonded,  ");
        sb.append(" st.STOCK_ID AS clientId,  ");
        sb.append(" st.STOCK_IN AS clientName, ");
        sb.append(" TEMP.item_num AS billCode, ");
        sb.append(" TEMP.ctn_num AS ctnNum, ");
        sb.append(" TEMP.nowNum,  ");
        sb.append(" TEMP.allnet, ");
        sb.append(" TEMP.allgross,");
        sb.append(" TEMP.big_type_name AS bigName, ");
        sb.append(" TEMP.little_type_name AS simName,  ");
        sb.append(" TEMP.cargo_name AS cargoName,  ");
        sb.append(" TEMP.link_id AS contactCode, ");
        sb.append(" st. OPERATOR AS createUser  ");
        sb.append(" FROM(   ");
        sb.append(" SELECT       ");
        sb.append(" 	'倒箱' AS lxType, ");
        sb.append(" 	INFO.link_id, ");
        sb.append(" 	INFO.item_num, ");
        sb.append(" 	INFO.ctn_num,  ");
        sb.append(" 	SUM (INFO.piece) AS nowNum, ");
        sb.append(" 	SUM (info.gross_weight) AS allnet, ");
        sb.append(" 	SUM (info.net_weight) AS allgross,");
        sb.append(" 	INFO.big_type_name, ");
        sb.append(" 	INFO.little_type_name, ");
        sb.append(" 	INFO.cargo_name  ");
        sb.append(" FROM   ");
        sb.append(" 	BIS_ENTER_STOCK_INFO info  ");
        sb.append(" WHERE   ");
        sb.append(" 	INFO.IF_BACK = '1'  ");
        sb.append(" GROUP BY   ");
        sb.append(" 	INFO.link_id,  ");
        sb.append(" 	INFO.item_num,  ");
        sb.append(" 	INFO.ctn_num, ");
        sb.append(" 	INFO.big_type_name,  ");
        sb.append(" 	INFO.little_type_name,");
        sb.append(" 	INFO.cargo_name");
        sb.append(" UNION ALL   ");
        sb.append(" SELECT   ");
        sb.append(" 	'查验' AS lxType,");
        sb.append(" 	INFO.link_id,   ");
        sb.append(" 	INFO.item_num, ");
        sb.append(" 	INFO.ctn_num,  ");
        sb.append(" 	SUM (INFO.piece) AS nowNum,  ");
        sb.append(" 	SUM (info.gross_weight) AS allnet, ");
        sb.append(" 	SUM (info.net_weight) AS allgross,");
        sb.append(" 	INFO.big_type_name, ");
        sb.append(" 	INFO.little_type_name,");
        sb.append(" 	INFO.cargo_name  ");
        sb.append(" FROM   ");
        sb.append(" BIS_ENTER_STOCK_INFO info  ");
        sb.append(" WHERE  ");
        sb.append(" 	INFO.IF_CHECK = '1'  ");
        sb.append(" GROUP BY  ");
        sb.append(" 	INFO.link_id, ");
        sb.append(" 	INFO.item_num, ");
        sb.append(" 	INFO.ctn_num,");
        sb.append(" 	INFO.big_type_name,  ");
        sb.append(" 	INFO.little_type_name,  ");
        sb.append(" 	INFO.cargo_name ");
        sb.append(" ) temp    ");
        sb.append(" 	LEFT JOIN BIS_ENTER_STOCK st ON TEMP.LINK_ID = ST.LINK_ID  ");
        sb.append(" 	AND TEMP.ITEM_NUM = ST.ITEM_NUM     ");
        sb.append(" ) temp    ");
        sb.append(" where 1=1 ");
        if (itemNum != null && !"".equals(itemNum)) {//提单号
        	sb.append(" and temp.billCode=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and temp.clientId=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and temp.ctnNum=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if(null!=ifBonded&&!"".equals(ifBonded)){
        	sb.append(" AND temp.ISBONDED=:ifBonded");
        	if("1".equals(ifBonded)){
        		parme.put("ifBonded","倒箱");
        	}else{
        		parme.put("ifBonded","查验");
        	}
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
            sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')<=to_date(:endTime,'yyyy-mm-dd')");
            parme.put("endTime", endTime);
        }
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }
    
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> companyExcel(String reportType,String itemNum,String stockIn,String strTime, String endTime) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sb=new StringBuffer();
        sb.append(" SELECT ");
       	sb.append(" temp.enterTime AS bgdhdate, ");
       	sb.append(" temp.reportType, ");
       	sb.append(" temp.contactCode,");
       	sb.append(" temp.createUser, ");
       	sb.append(" temp.clientId,   ");
       	sb.append(" temp.clientName, ");
       	sb.append(" temp.shfId,      ");
       	sb.append(" temp.SHF,        ");
       	sb.append(" temp.billCode,   ");
       	sb.append(" temp.companyId,  ");
       	sb.append(" temp.companyName,");
       	sb.append(" temp.companyNum, ");
       	sb.append(" temp.ciqId,      ");
       	sb.append(" temp.ciqName,    ");
       	sb.append(" temp.ciqNum,     ");
       	sb.append(" temp.orgId,      ");
       	sb.append(" temp.orgName,    ");
        sb.append(" temp.spNum,  ");
        sb.append(" temp.REMARK  ");
       	sb.append(" FROM   ");
       	sb.append(" (    ");
       	sb.append(" SELECT   ");
       	sb.append(" TO_CHAR (T .OPERATE_TIME,'yyyy-mm-dd') AS enterTime, ");
       	sb.append(" '入库' AS reportType,      ");
       	sb.append(" T .link_id AS contactCode, ");
       	sb.append(" T . OPERATOR AS createUser,");
       	sb.append(" T .STOCK_ID AS clientId,   ");
       	sb.append(" T .STOCK_IN AS clientName, ");
       	sb.append(" '' AS shfId, ");
       	sb.append(" '' AS SHF,");
       	sb.append(" T .ITEM_NUM AS billCode,   ");
       	sb.append(" T .CUSTOMS_COMPANY_ID AS companyId, ");
       	sb.append(" T .CUSTOMS_COMPANY AS companyName,  ");
       	sb.append(" SUM (CASE WHEN T .IF_TO_CUSTOMS = '1' THEN 1 ELSE 0 END) AS companyNum,");
       	sb.append(" T .CIQ_COMPANY_ID AS ciqId, ");
       	sb.append(" T .CIQ_COMPANY AS ciqName,  ");
       	sb.append(" SUM (CASE WHEN T .IF_TO_CIQ = '1' THEN 1 ELSE 0 END) AS ciqNum, ");
       	sb.append(" T .STOCK_ORG_ID AS orgId, ");
       	sb.append(" T .STOCK_ORG AS orgName,  ");
        sb.append(" SUM (CASE WHEN T .IF_RECORD = '1' THEN 1 ELSE 0 END) AS spNum,");
        sb.append(" T .REMARK  ");
       	sb.append(" FROM BIS_ENTER_STOCK T ");
       	sb.append(" WHERE 1 = 1 ");
       	sb.append(" AND (T .IF_TO_CUSTOMS = '1' OR T .IF_TO_CIQ = '1' OR T .IF_RECORD = '1') ");
       	sb.append(" GROUP BY  ");
       	sb.append("  TO_CHAR (T .OPERATE_TIME,'yyyy-mm-dd'),  ");
       	sb.append("  T .link_id,      ");
       	sb.append("  T . OPERATOR,    ");
       	sb.append("  T .STOCK_ID,     ");
       	sb.append("  T .STOCK_IN,     ");
       	sb.append("  T .ITEM_NUM,     ");
       	sb.append("  T .CUSTOMS_COMPANY_ID, ");
       	sb.append("  T .CUSTOMS_COMPANY,    ");
       	sb.append("  T .CIQ_COMPANY_ID,     ");
       	sb.append("  T .CIQ_COMPANY,        ");
       	sb.append("  T .STOCK_ORG_ID,       ");
       	sb.append("  T .STOCK_ORG,          ");
        sb.append("  T.REMARK      ");
       	sb.append(" UNION ALL      ");
       	sb.append(" SELECT   ");
       	sb.append(" 	TO_CHAR (st.OPERATE_TIME,'yyyy-mm-dd') AS enterTime,  ");
       	sb.append(" 	'出库' AS reportType,             ");
       	sb.append(" 	st.OUT_LINK_ID AS contactCode,    ");
       	sb.append(" 	st. OPERATOR AS createUser,       ");
       	sb.append(" 	st.STOCK_IN_ID AS clientId,       ");
       	sb.append(" 	st.STOCK_IN AS clientName,        ");
       	sb.append(" 	st.RECEIVER_ID AS shfId,          ");
       	sb.append(" 	st.RECEIVER AS SHF,               ");
       	sb.append(" 	MAX(info.BILL_NUM) AS billCode,      ");
       	sb.append(" 	st.CUSTOMS_COMPANY_ID AS companyId,  ");
       	sb.append(" 	st.CUSTOMS_COMPANY AS companyName,   ");
       	sb.append(" 	NVL(st.OUT_CUSTOMS_COUNT,0) AS companyNum,");
       	sb.append(" 	st.CIQ_COMPANY_ID AS ciqId,               ");
       	sb.append(" 	st.CIQ_COMPANY AS ciqName,                ");
       	sb.append(" 	NVL(st.OUT_CIQ_COUNT,0) AS ciqNum,        ");
       	sb.append(" 	st.SETTLE_ORG_ID AS orgId,                ");
       	sb.append(" 	st.SETTLE_ORG AS orgName,                 ");
        sb.append("   NVL(st.APPROVE_COUNT,0) AS spNum,         ");
        sb.append("   st.REMARK  ");
       	sb.append(" FROM      ");
       	sb.append("	bis_out_stock st   ");
        sb.append(" LEFT JOIN       ");
        sb.append(" (                ");
        sb.append("  SELECT         ");
       	sb.append("	 INFO.OUT_LINK_ID, ");
       	sb.append("	 LISTAGG (INFO.BILL_NUM, ',') WITHIN GROUP (ORDER BY INFO.BILL_NUM) AS BILL_NUM ");
       	sb.append(" FROM     ");
       	sb.append("	(       ");
       	sb.append(" SELECT DISTINCT   ");
       	sb.append(" info.OUT_LINK_ID, ");
       	sb.append(" info.BILL_NUM  ");
       	sb.append(" FROM BIS_OUT_STOCK_INFO info ");
       	sb.append(" LEFT JOIN bis_enter_stock et ON info.BILL_NUM = et.item_num   ");
       	sb.append(" ) info  ");
        sb.append(" GROUP BY INFO.OUT_LINK_ID   ");
        sb.append(" ) info ON ST.OUT_LINK_ID = info.OUT_LINK_ID ");
       	sb.append("	WHERE 1 = 1 ");
       	sb.append(" AND (ST.CUSTOMS_COMPANY_ID IS NOT NULL OR ST.CIQ_COMPANY_ID IS NOT NULL OR st.IF_RECORD='1')  ");
       	sb.append("	GROUP BY  ");
       	sb.append("	TO_CHAR (st.OPERATE_TIME,'yyyy-mm-dd'),");
       	sb.append(" st.OUT_LINK_ID, ");
       	sb.append(" st. OPERATOR, ");
       	sb.append(" st.STOCK_IN_ID, ");
       	sb.append(" st.STOCK_IN, ");
       	sb.append(" st.RECEIVER_ID, ");
       	sb.append(" st.RECEIVER, ");
       	sb.append(" st.CUSTOMS_COMPANY_ID,  ");
       	sb.append(" st.CUSTOMS_COMPANY,  ");
       	sb.append(" st.CIQ_COMPANY_ID, ");
       	sb.append(" st.CIQ_COMPANY,  ");
       	sb.append(" st.SETTLE_ORG_ID, ");
       	sb.append(" st.SETTLE_ORG, ");
       	sb.append(" st.OUT_CUSTOMS_COUNT,   ");
       	sb.append(" st.OUT_CIQ_COUNT, ");
        sb.append(" st.APPROVE_COUNT, ");
        sb.append(" st.IF_RECORD,");
        sb.append(" st.REMARK  ");
       	sb.append(" ) temp  ");
       	sb.append(" WHERE 1 = 1  ");
          if (itemNum != null && !"".equals(itemNum)) {//提单号
          	sb.append(" and temp.billCode like:billnum  ");
              parme.put("billnum","%"+itemNum+"%");
          }
          if (stockIn != null && !"".equals(stockIn)) {//--客户ID
          	sb.append(" and temp.clientId=:sockid  ");
              parme.put("sockid", stockIn);
          }
          
          if(null!=reportType&&!"".equals(reportType)){
          	sb.append(" AND temp.reportType=:reportType");
          	if("1".equals(reportType)){
          		parme.put("reportType","入库");
          	}else{
          		parme.put("reportType","出库");
          	}
          }
          if (strTime != null && !"".equals(strTime)) {//--入库日期
              sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')>=to_date(:strTime,'yyyy-mm-dd')  ");
              parme.put("strTime", strTime);
          }
          if (endTime != null && !"".equals(endTime)) {//--入库日期
              sb.append(" and to_date(temp.enterTime,'yyyy-mm-dd')<to_date(:endTime,'yyyy-mm-dd')");
              parme.put("endTime", endTime);
          }
        sb.append(" ORDER BY temp.enterTime DESC                                                                 ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }


    /**
     * @param itemNum  提单号
     * @param ctnNum  箱号
     * @param realClientName  客户名称
     * @throws Exception
     * @throws
     * @Description: 在库明细  接口信息（海路通系统）
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> inStockDetailReportInfo(String itemNum, String ctnNum, String realClientName) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sb=new StringBuffer();
        sb.append(" SELECT ");
        sb.append(" tray.stock_in AS clientId,");
        sb.append(" c.REAL_CLIENT_NAME AS client_name,");
        sb.append(" (CASE st.IF_BONDED WHEN '1' THEN '保税货物' ELSE '非保税货物' END ) AS isBonded,");
        sb.append(" tray.bill_num AS billCode, ");
        sb.append(" tray.ctn_num AS ctnNum, ");
        sb.append(" nvl(info.bgdh,st.bgdh) as bgdh,");
        sb.append(" nvl(info.ycg,st.ycg) as ycg,");
        sb.append(" TO_CHAR (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd') AS bgdhdate,");
        sb.append(" tray.sku_id AS sku, ");
        sb.append(" st.CTN_TYPE_SIZE AS cz, ");
        sb.append(" s.type_name AS bigName, ");
        sb.append(" s.cargo_type AS bigType, ");
        sb.append(" s.class_name AS simName, ");
        sb.append(" s.class_type AS simType, ");
        sb.append(" tray.CARGO_NAME AS cargoName,");
        sb.append(" tray.rkTime, ");
        sb.append(" tray.enter_state AS state, ");
        sb.append(" SUM (tray.num) AS nowNum, ");
        sb.append(" round(SUM (tray.net_weight),2) AS allnet, ");
        sb.append(" round(SUM (tray.gross_weight),2) AS allgross, ");
        sb.append(" tray.CONTACT_NUM AS contactCode,  ");
        sb.append(" st. OPERATOR AS createUser,  ");
        sb.append(" tray.asn ");
        sb.append(",decode(info.HS_CODE,NULL,ai.HS_CODE,info.HS_CODE) as hsCode, ");
        sb.append(" decode(info.ACCOUNT_BOOK,NULL,ai.ACCOUNT_BOOK ,info.ACCOUNT_BOOK) as accountBook, ");
        sb.append(" decode(info.HS_ITEMNAME,NULL,ai.HS_ITEMNAME,info.HS_ITEMNAME) as hsItemname");
        sb.append(" FROM(  ");
        sb.append("  SELECT   ");
        sb.append("    tray.stock_in,  ");
        sb.append("    tray.stock_name, ");
        sb.append("    tray.bill_num, ");
        sb.append("    tray.ctn_num,");
        sb.append("    tray.sku_id,");
        sb.append("    SUM (tray.now_piece) AS num, ");
        sb.append("    (CASE tray.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state,");
        sb.append("    tray.CARGO_NAME,   ");
        sb.append("    SUM (tray.net_weight) AS net_weight, ");
        sb.append("    SUM (tray.gross_weight) AS gross_weight,   ");
        sb.append("    tray.CONTACT_NUM,   ");
        sb.append("    to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd') AS rkTime, ");
        sb.append("    tray.asn,  ");
        sb.append("    tray.cargo_location  ");
        sb.append(" FROM   ");
        sb.append("    BIS_TRAY_INFO tray ");
        sb.append(" WHERE 1 = 1 AND (tray.cargo_state = '01' OR tray.cargo_state = '10')  ");
        sb.append(" AND tray.now_piece != 0  ");
        sb.append(" GROUP BY ");
        sb.append("   tray.stock_in, ");
        sb.append("   tray.stock_name, ");
        sb.append("   tray.bill_num, ");
        sb.append("   tray.ctn_num, ");
        sb.append("   tray.sku_id, ");
        sb.append("   tray.enter_state, ");
        sb.append("   tray.CARGO_NAME, ");
        sb.append("   tray.CONTACT_NUM,  ");
        sb.append("   tray.asn,  ");
        sb.append("   tray.enter_state, ");
        sb.append("   to_char (tray.ENTER_STOCK_TIME,'yyyy-mm-dd'), ");
        sb.append("   tray.cargo_location  ");
        sb.append("  ) tray   ");
        sb.append(" LEFT JOIN (  ");
        sb.append("   SELECT  ");
        sb.append(" 	ba.asn, ");
        sb.append(" 	TRUNC (ba.inbound_date) AS inbound_date,  ");
        sb.append(" 	ba.ctn_num, ");
        sb.append(" 	ba.bill_num, ");
        sb.append("     ba.ORDER_NUM, ");
        sb.append("     ba.stock_in ");
        sb.append(" FROM  ");
        sb.append(" 	bis_asn ba ");
        sb.append(" ) A ON (tray.asn = A.asn AND A.ctn_num = tray.ctn_num AND A.bill_num = tray.bill_num)        ");
        sb.append(" LEFT JOIN bis_asn_info ai ON (ai.asn_id =tray.asn AND ai.sku_id=tray.sku_id)                 ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (  ");
        sb.append(" 	tray.bill_num = st.ITEM_NUM ");
        sb.append(" 	AND tray.CONTACT_NUM = st.LINK_ID ");
        sb.append(" )     ");
        sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON ( ");
        sb.append(" 	st.ITEM_NUM = info.ITEM_NUM  ");
        sb.append(" 	AND st.LINK_ID = info.LINK_ID ");
        sb.append(" 	AND tray.ctn_num = info.ctn_num ");
        sb.append(" 	AND tray.sku_id = info.sku ");
        sb.append(" )   ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id       ");
        sb.append(" LEFT JOIN base_client_info c ON c.ids = tray.stock_in     ");
        sb.append(" where 1=1 ");

        if (itemNum != null && !"".equals(itemNum)) {//提单号
            sb.append(" and tray.bill_num=:billnum  ");
            parme.put("billnum", itemNum);
        }
        if (ctnNum != null && !"".equals(ctnNum)) {//--箱号
            sb.append(" and tray.ctn_num=:ctnnum   ");
            parme.put("ctnnum", ctnNum);
        }
        if (realClientName != null && !"".equals(realClientName)) {//--客户名称
            sb.append(" and c.REAL_CLIENT_NAME=:sockid  ");
            parme.put("sockid", realClientName);
        }
        sb.append(" GROUP BY ");
        sb.append(" tray.stock_in,  ");
        sb.append(" c.REAL_CLIENT_NAME, ");
        sb.append(" st.IF_BONDED, ");
        sb.append(" tray.bill_num, ");
        sb.append(" tray.ctn_num, ");
        sb.append(" info.bgdh, ");
        sb.append(" info.ycg, ");
        sb.append(" st.bgdh,");
        sb.append(" st.ycg,");
        sb.append(" to_char (nvl(info.BGDHDATE,st.BGDHDATE), 'yyyy-mm-dd'),");
        sb.append(" tray.sku_id,  ");
        sb.append(" st.CTN_TYPE_SIZE, ");
        sb.append(" s.type_name,  ");
        sb.append(" s.cargo_type, ");
        sb.append(" s.class_name, ");
        sb.append(" s.class_type, ");
        sb.append(" tray.CARGO_NAME, ");
        sb.append(" tray.rkTime,");
        sb.append(" tray.enter_state,");
        sb.append(" tray.CONTACT_NUM, ");
        sb.append(" st.OPERATOR, ");
        sb.append(" tray.asn   ");

        sb.append(",info.HS_CODE,info.ACCOUNT_BOOK,info.HS_ITEMNAME ");
        sb.append(" ,ai.HS_CODE,ai.HS_ITEMNAME,ai.ACCOUNT_BOOK ");
        sb.append(" ORDER BY  ");
        sb.append(" tray.bill_num, ");
        sb.append(" tray.ctn_num, ");
        sb.append(" tray.rkTime  ");

        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

}
