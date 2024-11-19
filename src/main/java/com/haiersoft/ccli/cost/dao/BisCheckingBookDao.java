package com.haiersoft.ccli.cost.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

//import oracle.net.aso.s;

@Repository
public class BisCheckingBookDao  extends HibernateDao<BisCheckingBook, String> {

	private final static String  STATU = "已上传";
	private final static String  NOSTATU = "未上传";

	public Integer updateBisCheckBook(BisCheckingBook bisCheckingBook){
		String sql="update BIS_CHEKING_BOOK set SRC_CUST_NAME=:custom,INVOICECODE=:invoiceCode,INVOICENUM=:invoiceNum,JSFS=:jsfs,RESULT=:result where CODENUM=:codeNum ";
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(null!=bisCheckingBook.getCodeNum()){
			parme.put("codeNum",bisCheckingBook.getCodeNum());
		}
		if(null!=bisCheckingBook.getSrcCustName()){
		    parme.put("custom",bisCheckingBook.getSrcCustName());
		}
		if(null!=bisCheckingBook.getSrcCustName()){
		    parme.put("invoiceCode",bisCheckingBook.getInvoiceCode());
		}
		if(null!=bisCheckingBook.getSrcCustName()){
		    parme.put("invoiceNum",bisCheckingBook.getInvoiceNum());
		}
		if(null!=bisCheckingBook.getJsfs()){
		    parme.put("jsfs",bisCheckingBook.getJsfs());
		}
		if(null!=bisCheckingBook.getResult()){
		    parme.put("result",bisCheckingBook.getResult());
		}
		SQLQuery sqlQuery=createSQLQuery(sql, parme);
		return sqlQuery.executeUpdate();
	}
	/**
	 * 根据账单号 确认对账信息
	 * @param sCode 对账id
	 *
	 */
	public void addStingBookCodeList(String sCode){
		if(sCode!=null && !"".equals(sCode)  ){
			String sql="update bis_standing_book b set RECONCILE_SIGN='1' where b.reconcile_num=:code ";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("code", sCode);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}

	/**
	 * 根据账单号 删除对账信息
	 * @param sCode 对账id
	 *
	 */
	public void delStingBookCodeList(String sCode){
		if(sCode!=null && !"".equals(sCode)  ){
			String sql="update bis_standing_book b set RECONCILE_SIGN='0',b.reconcile_num=null where b.reconcile_num=:code ";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("code", sCode);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> getBookRows(String checkingBookCode,List<Map<String,Object>> feeList,String ntype,String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer();
		StringBuffer sumt=new StringBuffer();
		sb.append(" SELECT ");
		sb.append(" temp.bill_num, ");
        sb.append(" count(temp.bill_num) AS hs ");
        sb.append(" FROM  (       ");
        sb.append(" SELECT  ");
		sb.append("  bill_num, ");
		sb.append("  ntype, ");
		sb.append("  uname, ");
		sb.append("  order_STORAGESTATE, ");
		sb.append("  CRK_SIGN, ");
		sb.append("  hs, ");
		sb.append("  cargo_name, ");
		sb.append("  (CASE ntype WHEN '2' THEN (CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE piece END) ELSE piece END) AS piece,  ");
		sb.append("  (CASE ntype WHEN '2' THEN (CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE net_weight END) ELSE net_weight END) AS net_weight, ");
		sb.append("  (CASE ntype WHEN '2' THEN(CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE gross_weight END) ELSE gross_weight END) AS gross_weight, ");
		sb.append("  charge_day, ");
		sb.append("  charge_start_date, ");
		sb.append("  charge_end_date, ");
		for (int i = 0; i <feeList.size(); i++) {
			sb.append(" NVL(ku"+i+"_SHOULD_RMB, 0) AS kuRmb"+i+", ");
			sumt.append(" NVL(ku"+i+"_SHOULD_RMB, 0) ");
			if(i!=feeList.size()-1){
				sumt.append(" + " );
			}
		}
		sb.append(" ( ");
		sb.append(sumt.toString());
		sb.append(" )  sumrmb ");
		if(null!=type&&!"".equals(type)){
			if("1".equals(type)){
				sb.append("  ,CTN_NUM  ");
			}else{
				sb.append("  ,LOT_NUM  ");
			}
		}
		sb.append(" FROM( ");
		sb.append("  SELECT  ");
		sb.append("   temp.bill_num, ");
		sb.append("   temp.ntype, ");
		sb.append("   MIN (temp.uname) AS uname, ");
		sb.append("   temp.order_STORAGESTATE, ");
		sb.append("   temp.STORAGE_STATE, ");
		sb.append("   MIN (temp.CRK_SIGN) AS CRK_SIGN, ");
		sb.append("   MIN (temp.hs) AS hs, ");
		sb.append("   min (temp.cargo_name) AS cargo_name, ");
		sb.append("   temp.piece,");
		sb.append("   MIN (temp.net_weight) AS net_weight,");
		sb.append("   MIN (temp.gross_weight) AS gross_weight, ");
		sb.append("   MIN (temp.charge_day) AS charge_day, ");
		sb.append("   temp.charge_start_date, ");
		sb.append("   temp.charge_end_date,");
		sb.append("   SUM (temp.SHOULD_RMB) AS SHOULD_RMB, ");
		sb.append("   temp.lab, ");
		sb.append("   MIN (temp.CTN_NUM) AS CTN_NUM, ");
		sb.append("   MIN (temp.LOT_NUM) AS LOT_NUM  ");
		sb.append(" FROM(  ");
		sb.append("   SELECT sb.link_id,");
		sb.append(" 	(CASE sb.crk_sign WHEN '2' THEN NVL (sb.bill_num,(  ");
		sb.append("       SELECT LISTAGG (info.BILL_NUM, ',') WITHIN GROUP (ORDER BY info.BILL_NUM) AS BILL_NUM FROM ");
		sb.append(" 	  (SELECT OUT_LINK_ID,BILL_NUM FROM BIS_OUT_STOCK_INFO GROUP BY OUT_LINK_ID,BILL_NUM) info WHERE OUT_LINK_ID=link_id ");
		sb.append(" 		GROUP BY info.OUT_LINK_ID ) ) ELSE sb.bill_num END) AS bill_num, ");
		sb.append("   sb.crk_sign AS ntype,");
		sb.append("   sb.customs_name AS uname, ");
		sb.append("   sb.order_STORAGESTATE,  ");
		sb.append("   sb.STORAGE_STATE, ");
		sb.append("   (CASE sb.STORAGE_STATE WHEN '1' THEN '在库' WHEN '2' THEN '出' WHEN '3' THEN '在库' WHEN '4' THEN '在库' ");
		sb.append("   ELSE(CASE sb.crk_sign WHEN '1' THEN '入库' ELSE '出' END)END) AS CRK_SIGN,");
		sb.append("   (CASE sb.STORAGE_STATE WHEN '4' THEN '1' ELSE	'0' END) AS hs, ");
		sb.append("   NVL(sb.cargo_name,(CASE crk_sign WHEN '1' THEN pe.cargo_name WHEN '2' THEN ");
		sb.append("   (CASE WHEN sb.bill_num IS NULL THEN ");
		sb.append("   (SELECT LISTAGG (info.cargo_name, ',') WITHIN GROUP (ORDER BY info.cargo_name) AS cargo_name FROM  ");
		sb.append("   ( ");
		sb.append("    SELECT  ");
		sb.append("     OUT_LINK_ID,  ");
		sb.append("     cargo_name    ");
		sb.append("   FROM  ");
		sb.append("    BIS_OUT_STOCK_INFO  ");
		sb.append("   GROUP BY ");
		sb.append("    OUT_LINK_ID, ");
		sb.append("    cargo_name   ");
		sb.append(" ) info ");
		sb.append("  WHERE OUT_LINK_ID=link_id   ");
		sb.append("  GROUP BY info.OUT_LINK_ID   ");
		sb.append(" ) ELSE ");
		sb.append(" (SELECT LISTAGG (info.cargo_name, ',') WITHIN GROUP (ORDER BY info.cargo_name) AS cargo_name FROM ");
		sb.append(" (SELECT bill_num,cargo_name FROM BIS_OUT_STOCK_INFO GROUP BY bill_num,cargo_name) info WHERE bill_num=sb.bill_num GROUP BY info.bill_num) END)  ELSE '' END) ) AS cargo_name, ");
		sb.append(" NVL (sb.piece,(   ");
		sb.append("   CASE sb.crk_sign    ");
		sb.append("   WHEN '1' THEN    ");
		sb.append("   pe.piece   ");
		sb.append("   WHEN '2' THEN ");
		sb.append("   po.piece   ");
		sb.append("   ELSE 0 END ");
		sb.append("  )  ");
		sb.append("  ) AS piece,");
		sb.append("  ROUND (  ");
		sb.append("  NVL (   ");
		sb.append("  sb.net_weight,  ");
		sb.append("  (  ");
		sb.append("  CASE sb.crk_sign ");
		sb.append("  WHEN '1' THEN  ");
		sb.append("  pe.net_weight  ");
		sb.append("  WHEN '2' THEN ");
		sb.append("  po.net_weight  ");
		sb.append("  ELSE 0 END)),2) AS net_weight, ");
		sb.append("  ROUND (NVL (sb.gross_weight,(  ");
		sb.append("   CASE sb.crk_sign  ");
		sb.append("   WHEN '1' THEN   ");
		sb.append("   pe.gross_weight ");
		sb.append("   WHEN '2' THEN   ");
		sb.append("   po.gross_weight  ");
		sb.append("   ELSE 0 END)),2) AS gross_weight, ");
		sb.append("   sb.charge_day, ");
		sb.append(" (     ");
		sb.append(" CASE pe.IF_BACK WHEN '1' THEN TO_CHAR(pe.BACKDATE,'yyyy-mm-dd') ");
		sb.append(" ELSE NVL(TO_CHAR(sb.charge_start_date,'yyyy-mm-dd'),TO_CHAR(pe.enterTime,'yyyy-mm-dd')) ");
		sb.append(" END ");
		sb.append("  ) AS charge_start_date, ");
		sb.append("  sb.charge_end_date,  ");
		sb.append("  sb.SHOULD_RMB,  ");
		sb.append("  sb.fee_name, ");
		sb.append("  sb.price, ");
		sb.append("  sb.lab,  ");
		sb.append("  (CASE sb.crk_sign WHEN '2' THEN (CASE WHEN sb.bill_num IS NULL THEN  ");
		sb.append("  nvl(po.CTN_NUM,(  ");
		sb.append(" 	SELECT LISTAGG (info.CTN_NUM, ',') WITHIN GROUP (ORDER BY info.CTN_NUM) AS CTN_NUM FROM ");
		sb.append(" 	(SELECT OUT_LINK_ID,CTN_NUM FROM BIS_OUT_STOCK_INFO GROUP BY OUT_LINK_ID,CTN_NUM) info WHERE OUT_LINK_ID=link_id GROUP BY info.OUT_LINK_ID ) ");
		sb.append("  ) ELSE (CASE WHEN sb.cargo_name IS NULL THEN  ");
		sb.append("    (    ");
		sb.append(" 	 SELECT LISTAGG (info.CTN_NUM, ',') WITHIN GROUP (ORDER BY info.CTN_NUM) AS CTN_NUM FROM ");
		sb.append("    (SELECT bill_num,CTN_NUM FROM BIS_OUT_STOCK_INFO GROUP BY bill_num,CTN_NUM) info WHERE bill_num=sb.bill_num GROUP BY  info.bill_num    ");
		sb.append("    ) ELSE  ");
		sb.append("   (     ");
		//sb.append("SELECT to_char (listagg(CTN_NUM,',') WITHIN GROUP (order by CTN_NUM ) AS CTN_NUM FROM BIS_OUT_STOCK_INFO ");
		sb.append(" SELECT to_char (wmsys.wm_concat (DISTINCT CTN_NUM)) AS CTN_NUM FROM BIS_OUT_STOCK_INFO ");
		sb.append(" WHERE bill_num=sb.bill_num AND instr(cargo_name,po.cargo_name)>0 GROUP BY bill_num ");
		sb.append("  ) END ) END )     ");
		sb.append("   ELSE pe.CTN_NUM END) AS CTN_NUM,  ");
		sb.append("   (CASE sb.crk_sign WHEN '2' THEN '' ELSE pe.LOT_NUM END) AS LOT_NUM ");
		sb.append("  FROM  ");
		sb.append(" 	(     ");
		sb.append(" 	SELECT   ");
		sb.append(" 	 SB.LINK_ID,  ");
		sb.append(" 	 SB.BILL_NUM, ");
		sb.append(" 	 SB.crk_sign, ");
		sb.append(" 	(   ");
		sb.append(" 	 CASE  ");
		sb.append(" 	 WHEN STORAGE_STATE = '3' THEN   ");
		sb.append(" 	 '0'   ");
		sb.append(" 	 ELSE  ");
		sb.append(" 	 STORAGE_STATE ");
		sb.append(" 	 END   ");
		sb.append(" 	) AS order_STORAGESTATE,");
		sb.append(" 	NVL (MIN(sb.customs_name), '') AS customs_name,  ");
		sb.append("     sb.STORAGE_STATE,  ");
		sb.append(" 	LISTAGG (sb.CARGO_NAME, ',') WITHIN GROUP (ORDER BY sb.CARGO_NAME) AS CARGO_NAME,   ");
		sb.append(" 	SUM (sb.piece) AS piece, ");
		sb.append(" 	ROUND (SUM(sb.net_weight), 2) AS net_weight,  ");
		sb.append(" 	ROUND (SUM(sb.gross_weight), 2) AS gross_weight, ");
		sb.append(" 	MIN (sb.charge_day) AS charge_day,  ");
		sb.append(" 	NVL (sb.charge_start_date, '') AS charge_start_date, ");
		sb.append(" 	NVL (sb.charge_end_date, '') AS charge_end_date,  ");
		sb.append("     ROUND (SUM(sb.SHOULD_RMB), 2) AS SHOULD_RMB, ");
		sb.append(" 	MIN (sb.fee_name) AS fee_name, ");
		sb.append(" 	MIN (sb.price) AS price,  ");
		sb.append("     CASE WHEN min(NVL(ci.fee_type, '1'))= '1' THEN MIN(sb.fee_code) ELSE CONCAT (MIN (sb.fee_code),MIN (sb.price)) END AS lab, ");
		//sb.append(" 	CONCAT (MIN (sb.fee_code),MIN (sb.price)) AS lab,  ");
		sb.append(" 	MIN (SB.BILL_DATE) AS BILL_DATE   ");
		sb.append(" FROM  ");
		sb.append(" 	bis_standing_book sb    ");
		sb.append(" LEFT JOIN BASE_EXPENSE_CATEGORY_INFO ci ON sb.fee_code = ci. CODE ");
		sb.append(" WHERE reconcile_num =:code AND reconcile_sign = '1'  AND EXAMINE_SIGN = 1  ");
		sb.append(" GROUP BY                ");
		sb.append(" 	SB.LINK_ID,           ");
		sb.append(" 	SB.BILL_NUM,          ");
		sb.append(" 	SB.crk_sign,          ");
		sb.append(" 	SB.STORAGE_STATE,     ");
		sb.append(" 	SB.charge_start_date, ");
		sb.append(" 	SB.charge_end_date,   ");
		sb.append(" 	SB.piece,             ");
		sb.append(" 	SB.fee_code,          ");
		sb.append(" 	SB.price  ) sb        ");
		sb.append(" LEFT JOIN (         ");
		sb.append(" SELECT            ");
		sb.append(" tray.bill_num,  ");
		sb.append(" MIN (tray.enterTime) AS enterTime, ");
		sb.append(" LISTAGG (tray.CARGO_NAME, ',') WITHIN GROUP (ORDER BY tray.CARGO_NAME) AS CARGO_NAME,  ");
		sb.append(" MIN (tray.ctn_num) AS CTN_NUM,    ");
		sb.append(" MIN (tray.LOT_NUM) AS LOT_NUM,    ");
		sb.append(" MIN (tray.IF_BACK) AS IF_BACK,    ");
		sb.append(" MIN (tray.BACKDATE) AS BACKDATE,  ");
		sb.append(" SUM (tray.piece) AS piece,        ");
		sb.append(" SUM (tray.net_weight) AS net_weight,    ");
		sb.append(" SUM (tray.gross_weight) AS gross_weight ");
		sb.append(" FROM(    ");
		sb.append(" SELECT   ");
		sb.append(" tray.bill_num,  ");
		sb.append(" MIN (tray.ENTER_STOCK_TIME) AS enterTime,");
		sb.append(" tray.CARGO_NAME, ");
		sb.append(" MIN (info.ctn_num) AS CTN_NUM,  ");
		sb.append(" MIN (info.LOT_NUM) AS LOT_NUM,  ");
		sb.append(" MIN (st.IF_BACK) AS IF_BACK,    ");
		sb.append(" MIN (st.BACKDATE) AS BACKDATE,  ");
		sb.append(" SUM (tray.original_piece - tray.remove_piece) AS piece, ");
		sb.append(" SUM ((tray.original_piece - tray.remove_piece) * tray.net_single) AS net_weight,");
		sb.append(" SUM ((tray.original_piece - tray.remove_piece) * tray.gross_single) AS gross_weight ");
		sb.append(" FROM  ");
		sb.append(" bis_tray_info tray ");
		sb.append(" INNER JOIN bis_asn asn ON tray.asn = asn.asn   ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (  ");
		sb.append("  tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID )");
		sb.append(" LEFT JOIN (   ");
		sb.append(" SELECT  ");
		sb.append(" INFO.LINK_ID,");
		sb.append(" INFO.ITEM_NUM,   ");
		sb.append(" LISTAGG (INFO.CTN_NUM, ',') WITHIN GROUP (ORDER BY INFO.CTN_NUM) AS CTN_NUM, ");
		sb.append(" LISTAGG (INFO.LOT_NUM, ',') WITHIN GROUP (ORDER BY INFO.LOT_NUM) AS LOT_NUM  ");
		sb.append(" FROM(    ");
		sb.append(" SELECT DISTINCT  ");
		sb.append(" LINK_ID, ");
		sb.append(" ITEM_NUM,");
		sb.append(" CTN_NUM, ");
		sb.append(" LOT_NUM  ");
		sb.append(" FROM     ");
		sb.append(" BIS_ENTER_STOCK_INFO ");
		sb.append(" ) info ");
		sb.append(" GROUP BY  ");
		sb.append(" INFO.LINK_ID, ");
		sb.append(" INFO.ITEM_NUM ");
		sb.append(" ) info ON ST.LINK_ID = INFO.LINK_ID  ");
		sb.append(" WHERE tray.CARGO_STATE <> '99' AND asn.if_second_enter <> '2' ");
		sb.append(" GROUP BY   ");
		sb.append(" tray.bill_num,");
		sb.append(" tray.CARGO_NAME ");
		sb.append(" ) tray ");
		sb.append(" GROUP BY ");
		sb.append(" tray.bill_num  ");
		sb.append(" ) pe ON pe.bill_num = sb.bill_num    ");
		sb.append(" LEFT JOIN (  ");
		sb.append(" SELECT  ");
		sb.append(" load.out_link_id, ");
		sb.append(" load.BILL_NUM,    ");
		sb.append(" load.cargo_name,  ");
		sb.append(" LISTAGG (load.ctn_num, ',') WITHIN GROUP (ORDER BY load.ctn_num) AS ctn_num,  ");
		sb.append(" SUM (load.piece) AS piece, ");
		sb.append(" SUM (load.net_weight) AS net_weight,     ");
		sb.append(" SUM (load.gross_weight) AS gross_weight  ");
		sb.append(" FROM(   ");
		sb.append(" SELECT  ");
		sb.append(" NVL (load.out_link_id,INFO.out_link_id) AS out_link_id,");
		sb.append(" NVL (load.bill_num,INFO.BILL_NUM) AS BILL_NUM,         ");
		sb.append(" NVL (load.cargo_name,info.cargo_name) AS cargo_name,   ");
		sb.append(" NVL (load.CTN_NUM, info.ctn_num) AS ctn_num,           ");
		sb.append(" load.piece,      ");
		sb.append(" load.net_weight,   ");
		sb.append(" load.gross_weight  ");
		sb.append(" FROM(       ");
		sb.append(" SELECT ");
		sb.append(" info.out_link_id, ");
		sb.append(" info.BILL_NUM,    ");
		sb.append(" info.cargo_name,  ");
		sb.append(" LISTAGG (info.ctn_num, ',') WITHIN GROUP (ORDER BY info.ctn_num) AS ctn_num  ");
		sb.append(" FROM ");
		sb.append(" bis_out_stock_info info  ");
		sb.append(" group BY ");
		sb.append(" info.out_link_id,");
		sb.append(" info.BILL_NUM,  ");
		sb.append(" info.cargo_name ");
		sb.append(" )info  ");
		sb.append(" LEFT JOIN (   ");
		sb.append(" SELECT ");
		sb.append(" load.out_link_id,   ");
		sb.append(" load.BILL_NUM,      ");
		sb.append(" load.cargo_name,    ");
		sb.append(" LISTAGG (load.ctn_num, ',') WITHIN GROUP (ORDER BY load.ctn_num) AS ctn_num,    ");
		sb.append(" SUM (load.piece) AS piece,  ");
		sb.append(" SUM (load.net_weight) AS net_weight,    ");
		sb.append(" SUM (load.gross_weight) AS gross_weight ");
		sb.append(" FROM(  ");
		sb.append(" SELECT  ");
		sb.append(" out_link_id,  ");
		sb.append(" BILL_NUM,  ");
		sb.append(" ctn_num,   ");
		sb.append(" cargo_name,");
		sb.append(" SUM (piece) AS piece, ");
		sb.append(" SUM (net_weight) AS net_weight,     ");
		sb.append(" SUM (gross_weight) AS gross_weight  ");
		sb.append(" FROM  bis_loading_info  ");
		sb.append(" WHERE  ");
		sb.append(" (LOADING_STATE = '2' OR LOADING_STATE = '3')  ");
		sb.append(" GROUP BY         ");
		sb.append(" out_link_id,   ");
		sb.append(" BILL_NUM,      ");
		sb.append(" ctn_num,       ");
		sb.append(" cargo_name     ");
		sb.append(" ) load  ");
		sb.append(" GROUP BY  ");
		sb.append(" load.out_link_id,  ");
		sb.append(" load.BILL_NUM,     ");
		sb.append(" load.cargo_name    ");
		sb.append(" ) load ON ( ");
		sb.append(" info.out_link_id = load.out_link_id    ");
		sb.append(" AND info.BILL_NUM = load.BILL_NUM      ");
		sb.append(" AND info.CTN_NUM = load.CTN_NUM        ");
		sb.append(" AND info.CARGO_NAME = load.CARGO_NAME  ");
		sb.append(" )     ");
		sb.append(" ) load ");
		sb.append(" GROUP BY ");
		sb.append(" load.out_link_id,    ");
		sb.append(" load.BILL_NUM,       ");
		sb.append(" load.cargo_name      ");
		sb.append(" ) po ON (po.out_link_id = sb.link_id AND po.BILL_NUM=sb.BILL_NUM AND instr(sb.cargo_name,po.cargo_name)>0)  ");
		sb.append(" ) temp  ");
		sb.append(" GROUP BY ");
		sb.append(" temp.bill_num,");
		sb.append(" temp.ntype, ");
		sb.append(" temp.order_STORAGESTATE, ");
		sb.append(" temp.STORAGE_STATE,      ");
		sb.append(" temp.piece,  ");
		sb.append(" temp.charge_start_date,  ");
		sb.append(" temp.charge_end_date,    ");
		sb.append(" temp.lab, ");
		sb.append(" temp.price ");
		sb.append(" ) PIVOT (SUM (SHOULD_RMB) SHOULD_RMB FOR lab IN (     ");
		for (int i = 0; i < feeList.size(); i++) {
			Map map=feeList.get(i);
			sb.append("'"+map.get("LAB").toString()+"' ku"+i);
			if(i!=feeList.size()-1){
				sb.append(" ,");
			}
		}
		sb.append(" ) ");
		sb.append(" ) ");
		sb.append(" ORDER BY ");
		if("1".equals(ntype)||"3".equals(ntype)){
			sb.append("  bill_num  ");
		}else{
		    sb.append(" crk_sign DESC           ");
		}
		sb.append(" ) temp ");
		sb.append("  GROUP BY  temp.bill_num  ");
		params.put("code", checkingBookCode);
	    SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
	    List<Object[]> list=sqlQuery.list();
	    Map<String, Object> map=new HashMap<String, Object>();
	    for (int i = 0; i < list.size(); i++) {
	    	Object[] object=list.get(i);
			map.put((String) object[0], object[1]);
		}
	    return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getCheckingBookInfos(String checkingBookCode,List<Map<String,Object>> feeList,String ntype,String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer();
		StringBuffer sumt=new StringBuffer();
		sb.append(" SELECT  ");
		sb.append("  bill_num, ");
		sb.append("  ntype, ");
		sb.append("  uname, ");
		sb.append("  order_STORAGESTATE, ");
		sb.append("  CRK_SIGN, ");
		sb.append("  hs, ");
		sb.append("  cargo_name, ");
		sb.append("  (CASE ntype WHEN '2' THEN (CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE piece END) ELSE piece END) AS piece,  ");
		sb.append("  (CASE ntype WHEN '2' THEN (CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE net_weight END) ELSE net_weight END) AS net_weight, ");
		sb.append("  (CASE ntype WHEN '2' THEN(CASE WHEN order_STORAGESTATE IS NULL THEN 0 ELSE gross_weight END) ELSE gross_weight END) AS gross_weight, ");
		sb.append("  charge_day, ");
		sb.append("  charge_start_date, ");
		sb.append("  charge_end_date, ");
		for (int i = 0; i <feeList.size(); i++) {
			sb.append(" NVL(ku"+i+"_SHOULD_RMB, 0) AS kuRmb"+i+", ");
			sumt.append(" NVL(ku"+i+"_SHOULD_RMB, 0) ");
			if(i!=feeList.size()-1){
				sumt.append(" + " );
			}
		}
		sb.append(" ( ");
		sb.append(sumt.toString());
		sb.append(" )  sumrmb ");
		if(null!=type&&!"".equals(type)){
			if("1".equals(type)){
				sb.append("  ,CTN_NUM  ");
			}else{
				sb.append("  ,LOT_NUM  ");
			}
		}
		sb.append(" FROM( ");
		sb.append("  SELECT  ");
		sb.append("   temp.bill_num, ");
		sb.append("   temp.ntype, ");
		sb.append("   MIN (temp.uname) AS uname, ");
		sb.append("   temp.order_STORAGESTATE, ");
		sb.append("   temp.STORAGE_STATE, ");
		sb.append("   MIN (temp.CRK_SIGN) AS CRK_SIGN, ");
		sb.append("   MIN (temp.hs) AS hs, ");
		//sb.append("   min (temp.cargo_name) AS cargo_name, ");
		sb.append("  temp.cargo_name AS cargo_name, ");
		sb.append("   temp.piece,");
		sb.append("   MIN (temp.net_weight) AS net_weight,");
		sb.append("   MIN (temp.gross_weight) AS gross_weight, ");
		sb.append("   MIN (temp.charge_day) AS charge_day, ");
		sb.append("   temp.charge_start_date, ");
		sb.append("   temp.charge_end_date,");
		sb.append("   SUM (temp.SHOULD_RMB) AS SHOULD_RMB, ");
		sb.append("   temp.lab, ");
		sb.append("   MIN (temp.CTN_NUM) AS CTN_NUM, ");
		sb.append("   MIN (temp.LOT_NUM) AS LOT_NUM  ");
		sb.append(" FROM(  ");
		sb.append("   SELECT sb.link_id,");
		sb.append(" 	(CASE sb.crk_sign WHEN '2' THEN NVL (sb.bill_num,(  ");
		sb.append("       SELECT LISTAGG (info.BILL_NUM, ',') WITHIN GROUP (ORDER BY info.BILL_NUM) AS BILL_NUM FROM ");
		sb.append(" 	  (SELECT OUT_LINK_ID,BILL_NUM FROM BIS_OUT_STOCK_INFO GROUP BY OUT_LINK_ID,BILL_NUM) info WHERE OUT_LINK_ID=link_id ");
		sb.append(" 		GROUP BY info.OUT_LINK_ID ) ) ELSE sb.bill_num END) AS bill_num, ");
		sb.append("   sb.crk_sign AS ntype,");
		sb.append("   sb.customs_name AS uname, ");
		sb.append("   sb.order_STORAGESTATE,  ");
		sb.append("   sb.STORAGE_STATE, ");
		sb.append("   (CASE sb.STORAGE_STATE WHEN '1' THEN '在库' WHEN '2' THEN '出' WHEN '3' THEN '在库' WHEN '4' THEN '在库' ");
		sb.append("   ELSE(CASE sb.crk_sign WHEN '1' THEN '入库' ELSE '出' END)END) AS CRK_SIGN,");
		sb.append("   (CASE sb.STORAGE_STATE WHEN '4' THEN '1' ELSE	'0' END) AS hs, ");
		sb.append("   NVL(sb.cargo_name,(CASE crk_sign WHEN '1' THEN pe.cargo_name WHEN '2' THEN ");
		sb.append("   (CASE WHEN sb.bill_num IS NULL THEN ");
		sb.append("   (SELECT LISTAGG (info.cargo_name, ',') WITHIN GROUP (ORDER BY info.cargo_name) AS cargo_name FROM  ");
		sb.append("   ( ");
		sb.append("    SELECT  ");
		sb.append("     OUT_LINK_ID,  ");
		sb.append("     cargo_name    ");
		sb.append("   FROM  ");
		sb.append("    BIS_OUT_STOCK_INFO  ");
		sb.append("   GROUP BY ");
		sb.append("    OUT_LINK_ID, ");
		sb.append("    cargo_name   ");
		sb.append(" ) info ");
		sb.append("  WHERE OUT_LINK_ID=link_id   ");
		sb.append("  GROUP BY info.OUT_LINK_ID   ");
		sb.append(" ) ELSE ");
		sb.append(" (SELECT LISTAGG (info.cargo_name, ',') WITHIN GROUP (ORDER BY info.cargo_name) AS cargo_name FROM ");
		sb.append(" (SELECT bill_num,cargo_name FROM BIS_OUT_STOCK_INFO GROUP BY bill_num,cargo_name) info WHERE bill_num=sb.bill_num GROUP BY info.bill_num) END)  ELSE '' END) ) AS cargo_name, ");
		sb.append(" NVL (sb.piece,(   ");
		sb.append("   CASE sb.crk_sign    ");
		sb.append("   WHEN '1' THEN    ");
		sb.append("   pe.piece   ");
		sb.append("   WHEN '2' THEN ");
		sb.append("   po.piece   ");
		sb.append("   ELSE 0 END ");
		sb.append("  )  ");
		sb.append("  ) AS piece,");
		sb.append("  ROUND (  ");
		sb.append("  NVL (   ");
		sb.append("  sb.net_weight,  ");
		sb.append("  (  ");
		sb.append("  CASE sb.crk_sign ");
		sb.append("  WHEN '1' THEN  ");
		sb.append("  pe.net_weight  ");
		sb.append("  WHEN '2' THEN ");
		sb.append("  po.net_weight  ");
		sb.append("  ELSE 0 END)),2) AS net_weight, ");
		sb.append("  ROUND (NVL (sb.gross_weight,(  ");
		sb.append("   CASE sb.crk_sign  ");
		sb.append("   WHEN '1' THEN   ");
		sb.append("   pe.gross_weight ");
		sb.append("   WHEN '2' THEN   ");
		sb.append("   po.gross_weight  ");
		sb.append("   ELSE 0 END)),2) AS gross_weight, ");
		sb.append("   sb.charge_day, ");
		sb.append(" (     ");
		sb.append(" CASE pe.IF_BACK WHEN '1' THEN TO_CHAR(pe.BACKDATE,'yyyy-mm-dd') ");
		sb.append(" ELSE NVL(TO_CHAR(sb.charge_start_date,'yyyy-mm-dd'),TO_CHAR(pe.enterTime,'yyyy-mm-dd')) ");
		sb.append(" END ");
		sb.append("  ) AS charge_start_date, ");
		sb.append("  sb.charge_end_date,  ");
		sb.append("  sb.SHOULD_RMB,  ");
		sb.append("  sb.fee_name, ");
		sb.append("  sb.price, ");
		sb.append("  sb.lab,  ");
		sb.append("  (CASE sb.crk_sign WHEN '2' THEN (CASE WHEN sb.bill_num IS NULL THEN  ");
		sb.append("  nvl(po.CTN_NUM,(  ");
		sb.append(" 	SELECT LISTAGG (info.CTN_NUM, ',') WITHIN GROUP (ORDER BY info.CTN_NUM) AS CTN_NUM FROM ");
		sb.append(" 	(SELECT OUT_LINK_ID,CTN_NUM FROM BIS_OUT_STOCK_INFO GROUP BY OUT_LINK_ID,CTN_NUM) info WHERE OUT_LINK_ID=link_id GROUP BY info.OUT_LINK_ID ) ");
		sb.append("  ) ELSE (CASE WHEN sb.cargo_name IS NULL THEN  ");
		sb.append("    (    ");
		sb.append(" 	 SELECT LISTAGG (info.CTN_NUM, ',') WITHIN GROUP (ORDER BY info.CTN_NUM) AS CTN_NUM FROM ");
		sb.append("    (SELECT bill_num,CTN_NUM FROM BIS_OUT_STOCK_INFO GROUP BY bill_num,CTN_NUM) info WHERE bill_num=sb.bill_num GROUP BY  info.bill_num    ");
		sb.append("    ) ELSE  ");
		sb.append("   (     ");
		//sb.append(" SELECT listagg(CTN_NUM,',') WITHIN GROUP (order by CTN_NUM ) AS CTN_NUM FROM BIS_OUT_STOCK_INFO ");

		sb.append(" SELECT to_char (wmsys.wm_concat (DISTINCT CTN_NUM)) AS CTN_NUM FROM BIS_OUT_STOCK_INFO ");
		sb.append(" WHERE bill_num=sb.bill_num AND instr(cargo_name,po.cargo_name)>0 GROUP BY bill_num ");
		sb.append("  ) END ) END )     ");
		sb.append("   ELSE pe.CTN_NUM END) AS CTN_NUM,  ");
		sb.append("   (CASE sb.crk_sign WHEN '2' THEN '' ELSE pe.LOT_NUM END) AS LOT_NUM ");
		sb.append("  FROM  ");
		sb.append(" 	(     ");
		sb.append(" 	SELECT   ");
		sb.append(" 	 SB.LINK_ID,  ");
		sb.append(" 	 SB.BILL_NUM, ");
		sb.append(" 	 SB.crk_sign, ");
		sb.append(" 	(   ");
		sb.append(" 	 CASE  ");
		sb.append(" 	 WHEN STORAGE_STATE = '3' THEN   ");
		sb.append(" 	 '0'   ");
		sb.append(" 	 ELSE  ");
		sb.append(" 	 STORAGE_STATE ");
		sb.append(" 	 END   ");
		sb.append(" 	) AS order_STORAGESTATE,");
		sb.append(" 	NVL (MIN(sb.customs_name), '') AS customs_name,  ");
		sb.append("     sb.STORAGE_STATE,  ");
		//sb.append(" 	LISTAGG (sb.CARGO_NAME, ',') WITHIN GROUP (ORDER BY sb.CARGO_NAME) AS CARGO_NAME,   ");
		sb.append(" sb.CARGO_NAME AS CARGO_NAME,   ");
		sb.append(" 	SUM (sb.piece) AS piece, ");
		sb.append(" 	ROUND (SUM(sb.net_weight), 2) AS net_weight,  ");
		sb.append(" 	ROUND (SUM(sb.gross_weight), 2) AS gross_weight, ");
		sb.append(" 	MIN (sb.charge_day) AS charge_day,  ");
		sb.append(" 	NVL (sb.charge_start_date, '') AS charge_start_date, ");
		sb.append(" 	NVL (sb.charge_end_date, '') AS charge_end_date,  ");
		sb.append("     ROUND (SUM(sb.SHOULD_RMB), 2) AS SHOULD_RMB, ");
		sb.append(" 	MIN (sb.fee_name) AS fee_name, ");
		sb.append(" 	MIN (sb.price) AS price,  ");
		sb.append("     CASE WHEN min(NVL(ci.fee_type, '1'))= '1' THEN MIN(sb.fee_code) ELSE CONCAT (MIN (sb.fee_code),MIN (sb.price)) END AS lab, ");
		//sb.append(" 	CONCAT (MIN (sb.fee_code),MIN (sb.price)) AS lab,  ");
		sb.append(" 	MIN (SB.BILL_DATE) AS BILL_DATE   ");
		sb.append("  FROM  ");
		sb.append(" 	bis_standing_book sb    ");
		sb.append("  LEFT JOIN BASE_EXPENSE_CATEGORY_INFO ci ON sb.fee_code = ci. CODE ");
		sb.append("  WHERE reconcile_num =:code AND reconcile_sign = '1'  AND EXAMINE_SIGN = 1  ");
		sb.append("  GROUP BY                ");
		sb.append(" 	SB.LINK_ID,           ");
		sb.append(" 	SB.BILL_NUM,          ");
		sb.append(" 	SB.crk_sign,          ");
		sb.append(" 	SB.STORAGE_STATE,     ");
		sb.append(" 	SB.charge_start_date, ");
		sb.append(" 	SB.charge_end_date,   ");
		sb.append(" 	SB.piece,             ");
		sb.append(" 	SB.fee_code,          ");
		sb.append(" 	SB.price, sb.CARGO_NAME  ) sb        ");
		sb.append(" LEFT JOIN (         ");
		sb.append(" SELECT            ");
		sb.append(" tray.bill_num,  ");
		sb.append(" MIN (tray.enterTime) AS enterTime, ");
		sb.append(" LISTAGG (tray.CARGO_NAME, ',') WITHIN GROUP (ORDER BY tray.CARGO_NAME) AS CARGO_NAME,  ");
		sb.append(" MIN (tray.ctn_num) AS CTN_NUM,    ");
		sb.append(" MIN (tray.LOT_NUM) AS LOT_NUM,    ");
		sb.append(" MIN (tray.IF_BACK) AS IF_BACK,    ");
		sb.append(" MIN (tray.BACKDATE) AS BACKDATE,  ");
		sb.append(" SUM (tray.piece) AS piece,        ");
		sb.append(" SUM (tray.net_weight) AS net_weight,    ");
		sb.append(" SUM (tray.gross_weight) AS gross_weight ");
		sb.append(" FROM(    ");
		sb.append(" SELECT   ");
		sb.append(" tray.bill_num,  ");
		sb.append(" MIN (tray.ENTER_STOCK_TIME) AS enterTime,");
		sb.append(" tray.CARGO_NAME, ");
		sb.append(" MIN (info.ctn_num) AS CTN_NUM,  ");
		sb.append(" MIN (info.LOT_NUM) AS LOT_NUM,  ");
		sb.append(" MIN (st.IF_BACK) AS IF_BACK,    ");
		sb.append(" MIN (st.BACKDATE) AS BACKDATE,  ");
		sb.append(" SUM (tray.original_piece - tray.remove_piece) AS piece, ");
		sb.append(" SUM ((tray.original_piece - tray.remove_piece) * tray.net_single) AS net_weight,");
		sb.append(" SUM ((tray.original_piece - tray.remove_piece) * tray.gross_single) AS gross_weight ");
		sb.append(" FROM  ");
		sb.append(" bis_tray_info tray ");
		sb.append(" INNER JOIN bis_asn asn ON tray.asn = asn.asn   ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (  ");
		sb.append("  tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID )");
		sb.append(" LEFT JOIN (   ");
		sb.append(" SELECT  ");
		sb.append(" INFO.LINK_ID,");
		sb.append(" INFO.ITEM_NUM,   ");
		sb.append(" LISTAGG (INFO.CTN_NUM, ',') WITHIN GROUP (ORDER BY INFO.CTN_NUM) AS CTN_NUM, ");
		sb.append(" LISTAGG (INFO.LOT_NUM, ',') WITHIN GROUP (ORDER BY INFO.LOT_NUM) AS LOT_NUM  ");
		sb.append(" FROM(    ");
		sb.append(" SELECT DISTINCT  ");
		sb.append(" LINK_ID, ");
		sb.append(" ITEM_NUM,");
		sb.append(" CTN_NUM, ");
		sb.append(" LOT_NUM  ");
		sb.append(" FROM     ");
		sb.append(" BIS_ENTER_STOCK_INFO ");
		sb.append(" ) info ");
		sb.append(" GROUP BY  ");
		sb.append(" INFO.LINK_ID, ");
		sb.append(" INFO.ITEM_NUM ");
		sb.append(" ) info ON ST.LINK_ID = INFO.LINK_ID  ");
		sb.append(" WHERE tray.CARGO_STATE <> '99' AND asn.if_second_enter <> '2' ");
		sb.append(" GROUP BY   ");
		sb.append(" tray.bill_num,");
		sb.append(" tray.CARGO_NAME ");
		sb.append(" ) tray ");
		sb.append(" GROUP BY ");
		sb.append(" tray.bill_num  ");
		sb.append(" ) pe ON pe.bill_num = sb.bill_num    ");
		sb.append(" LEFT JOIN (  ");
		sb.append(" SELECT  ");
		sb.append(" load.out_link_id, ");
		sb.append(" load.BILL_NUM,    ");
		sb.append(" load.cargo_name,  ");
		sb.append(" LISTAGG (load.ctn_num, ',') WITHIN GROUP (ORDER BY load.ctn_num) AS ctn_num,  ");
		sb.append(" SUM (load.piece) AS piece, ");
		sb.append(" SUM (load.net_weight) AS net_weight,     ");
		sb.append(" SUM (load.gross_weight) AS gross_weight  ");
		sb.append(" FROM(   ");
		sb.append(" SELECT  ");
		sb.append(" NVL (load.out_link_id,INFO.out_link_id) AS out_link_id,");
		sb.append(" NVL (load.bill_num,INFO.BILL_NUM) AS BILL_NUM,         ");
		sb.append(" NVL (load.cargo_name,info.cargo_name) AS cargo_name,   ");
		sb.append(" NVL (load.CTN_NUM, info.ctn_num) AS ctn_num,           ");
		sb.append(" load.piece,      ");
		sb.append(" load.net_weight,   ");
		sb.append(" load.gross_weight  ");
		sb.append(" FROM(       ");
		sb.append(" SELECT ");
		sb.append(" info.out_link_id, ");
		sb.append(" info.BILL_NUM,    ");
		sb.append(" info.cargo_name,  ");
		sb.append(" LISTAGG (info.ctn_num, ',') WITHIN GROUP (ORDER BY info.ctn_num) AS ctn_num  ");
		sb.append(" FROM ");
		sb.append(" bis_out_stock_info info  ");
		sb.append(" group BY ");
		sb.append(" info.out_link_id,");
		sb.append(" info.BILL_NUM,  ");
		sb.append(" info.cargo_name ");
		sb.append(" )info  ");
		sb.append(" LEFT JOIN (   ");
		sb.append(" SELECT ");
		sb.append(" load.out_link_id,   ");
		sb.append(" load.BILL_NUM,      ");
		sb.append(" load.cargo_name,    ");
		sb.append(" LISTAGG (load.ctn_num, ',') WITHIN GROUP (ORDER BY load.ctn_num) AS ctn_num,    ");
		sb.append(" SUM (load.piece) AS piece,  ");
		sb.append(" SUM (load.net_weight) AS net_weight,    ");
		sb.append(" SUM (load.gross_weight) AS gross_weight ");
		sb.append(" FROM(  ");
		sb.append(" SELECT  ");
		sb.append(" out_link_id,  ");
		sb.append(" BILL_NUM,  ");
		sb.append(" ctn_num,   ");
		sb.append(" cargo_name,");
		sb.append(" SUM (piece) AS piece, ");
		sb.append(" SUM (net_weight) AS net_weight,     ");
		sb.append(" SUM (gross_weight) AS gross_weight  ");
		sb.append(" FROM  bis_loading_info  ");
		sb.append(" WHERE  ");
		sb.append(" (LOADING_STATE = '2' OR LOADING_STATE = '3')  ");
		sb.append(" GROUP BY         ");
		sb.append(" out_link_id,   ");
		sb.append(" BILL_NUM,      ");
		sb.append(" ctn_num,       ");
		sb.append(" cargo_name     ");
		sb.append(" ) load  ");
		sb.append(" GROUP BY  ");
		sb.append(" load.out_link_id,  ");
		sb.append(" load.BILL_NUM,     ");
		sb.append(" load.cargo_name    ");
		sb.append(" ) load ON ( ");
		sb.append(" info.out_link_id = load.out_link_id    ");
		sb.append(" AND info.BILL_NUM = load.BILL_NUM      ");
		sb.append(" AND info.CTN_NUM = load.CTN_NUM        ");
		sb.append(" AND info.CARGO_NAME = load.CARGO_NAME  ");
		sb.append(" )     ");
		sb.append(" ) load ");
		sb.append(" GROUP BY ");
		sb.append(" load.out_link_id,    ");
		sb.append(" load.BILL_NUM,       ");
		sb.append(" load.cargo_name      ");
		sb.append(" ) po ON (po.out_link_id = sb.link_id AND po.BILL_NUM=sb.BILL_NUM AND instr(sb.cargo_name,po.cargo_name)>0)  ");
		sb.append(" ) temp  ");
		sb.append(" GROUP BY ");
		sb.append(" temp.bill_num,");
		sb.append(" temp.ntype, ");
		sb.append(" temp.order_STORAGESTATE, ");
		sb.append(" temp.STORAGE_STATE,      ");
		sb.append(" temp.piece,  ");
		sb.append(" temp.charge_start_date,  ");
		sb.append(" temp.charge_end_date,    ");
		sb.append(" temp.lab, ");
		sb.append(" temp.price, temp.cargo_name ");
		sb.append(" ) PIVOT (SUM (SHOULD_RMB) SHOULD_RMB FOR lab IN (     ");
		for (int i = 0; i < feeList.size(); i++) {
			Map map=feeList.get(i);
			sb.append("'"+map.get("LAB").toString()+"' ku"+i);
			if(i!=feeList.size()-1){
				sb.append(" ,");
			}
		}
		sb.append(" ) ");
		sb.append(" ) ");
		sb.append(" ORDER BY ");
		if("1".equals(ntype)||"3".equals(ntype)){
			sb.append("  bill_num  ");
		}else{
		    sb.append(" crk_sign DESC           ");
		}
		params.put("code", checkingBookCode);
	    SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
	    return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 对账单明细导出  修改sql yaohn 20180903
	 * @param checkingBookCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getRepCheckingBookInfo(String checkingBookCode,String type) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer();
			sb.append("SELECT                                                                                                                ");
			sb.append("  sb.link_id AS link_id,                                                                                              ");
			sb.append("  (                                                                                                                   ");
			sb.append("    CASE sb.crk_sign                                                                                                  ");
			sb.append("    WHEN '2' THEN                                                                                                     ");
			sb.append("      nvl (                                                                                                           ");
			sb.append("        sb.bill_num,                                                                                                  ");
			sb.append("        (                                                                                                             ");
			sb.append("          SELECT                                                                                                      ");
			//sb.append("           listagg(bill_num,',') WITHIN GROUP (order by bill_num ) AS billNum                                               ");

			sb.append("            to_char (wmsys.wm_concat (DISTINCT bill_num)) AS billNum                                               ");
			sb.append("          FROM                                                                                                        ");
			sb.append("            bis_out_stock_info                                                                                     ");
			sb.append("          WHERE                                                                                                       ");
			sb.append("            out_link_id = link_id                                                                                  ");
			sb.append("        )                                                                                                             ");
			sb.append("      )                                                                                                               ");
			sb.append("    ELSE                                                                                                              ");
			sb.append("      sb.bill_num                                                                                                     ");
			sb.append("    END                                                                                                               ");
			sb.append("  ) AS bill_num,                                                                                                      ");
			sb.append("  sb.crk_sign AS ntype,                                                                                               ");
			sb.append("  nvl (min(sb.customs_name), '') AS uname,                                                                            ");
			sb.append("  sb.order_STORAGESTATE,                                                                                              ");
			sb.append("  sb.STORAGE_STATE,                                                                                                   ");
			sb.append("  (                                                                                                                   ");
			sb.append("    CASE sb.STORAGE_STATE                                                                                             ");
			sb.append("    WHEN '1' THEN                                                                                                     ");
			sb.append("      '在库'                                                                                                          ");
			sb.append("    WHEN '2' THEN                                                                                                     ");
			sb.append("      '出'                                                                                                            ");
			sb.append("    WHEN '3' THEN                                                                                                     ");
			sb.append("      '在库'                                                                                                          ");
			sb.append("    WHEN '4' THEN                                                                                                     ");
			sb.append("      '在库'                                                                                                          ");
			sb.append("    END                                                                                                               ");
			sb.append("  ) AS CRK_SIGN,                                                                                                      ");
			sb.append("  (                                                                                                                   ");
			sb.append("    CASE sb.STORAGE_STATE                                                                                             ");
			sb.append("    WHEN '4' THEN                                                                                                     ");
			sb.append("      '1'                                                                                                             ");
			sb.append("    ELSE                                                                                                              ");
			sb.append("      '0'                                                                                                             ");
			sb.append("    END                                                                                                               ");
			sb.append("  ) AS hs,                                                                                                            ");
			//sb.append("  listagg(cargo_name,',') WITHIN GROUP (order by cargo_name )AS cargo_name,                                                                                                    ");

			sb.append("  to_char (                                                                                                           ");
			sb.append("    wmsys.wm_concat (DISTINCT cargo_name)                                                                             ");
			sb.append("  ) AS cargo_name,                                                                                                    ");
		sb.append("  sum(                                                                                                                ");
			sb.append("    nvl (                                                                                                             ");
			sb.append("      sb.piece,                                                                                                       ");
			sb.append("      (                                                                                                               ");
			sb.append("        CASE sb.crk_sign                                                                                              ");
			sb.append("        WHEN '1' THEN                                                                                                 ");
			sb.append("          pe.piece                                                                                                    ");
			sb.append("        WHEN '2' THEN                                                                                                 ");
			sb.append("          po.piece                                                                                                    ");
			sb.append("        ELSE                                                                                                          ");
			sb.append("          0                                                                                                           ");
			sb.append("        END                                                                                                           ");
			sb.append("      )                                                                                                               ");
			sb.append("    )                                                                                                                 ");
			sb.append("  ) AS piece,                                                                                                         ");
			sb.append(" round(sum(                                                                                                                ");
			sb.append("    nvl (sb.net_weight,(                                                                                              ");
			sb.append("        CASE sb.crk_sign                                                                                              ");
			sb.append("        WHEN '1' THEN                                                                                                 ");
			sb.append("          pe.net_weight                                                                                               ");
			sb.append("        WHEN '2' THEN                                                                                                 ");
			sb.append("          po.net_weight                                                                                               ");
			sb.append("        ELSE                                                                                                          ");
			sb.append("          0                                                                                                           ");
			sb.append("        END                                                                                                           ");
			sb.append("      )                                                                                                               ");
			sb.append("    )                                                                                                                 ");
			sb.append("  ),2) AS net_weight,                                                                                                    ");
			sb.append("  round(sum(                                                                                                                ");
			sb.append("    nvl (                                                                                                             ");
			sb.append("      sb.gross_weight,                                                                                                ");
			sb.append("      (                                                                                                               ");
			sb.append("        CASE sb.crk_sign                                                                                              ");
			sb.append("        WHEN '1' THEN                                                                                                 ");
			sb.append("          pe.gross_weight                                                                                             ");
			sb.append("        WHEN '2' THEN                                                                                                 ");
			sb.append("          po.gross_weight                                                                                             ");
			sb.append("        ELSE                                                                                                          ");
			sb.append("          0                                                                                                           ");
			sb.append("        END                                                                                                           ");
			sb.append("      )                                                                                                               ");
			sb.append("    )                                                                                                                 ");
			sb.append("  ),2) AS gross_weight,                                                                                                  ");
			sb.append("  min(charge_day) AS charge_day,                                                                                      ");
			sb.append("  nvl (sb.charge_start_date, '') AS charge_start_date,                                                                ");
			sb.append("  nvl (sb.charge_end_date, '') AS charge_end_date,                                                                    ");
			sb.append("  round(sum(sb.SHOULD_RMB),2) AS SHOULD_RMB,                                                                                   ");
			sb.append("  min(sb.fee_name) AS fee_name,                                                                                       ");
			sb.append("  min(sb.price) AS price,                                                                                             ");
			sb.append("  CONCAT(                                                                                                             ");
			sb.append("    min(sb.fee_code),                                                                                                 ");
			sb.append("    min(sb.price)                                                                                                     ");
			sb.append("  ) AS lab,                                                                                                           ");
			sb.append("  (                                                                                                                   ");
			sb.append("    CASE sb.crk_sign                                                                                                  ");
			sb.append("    WHEN '2' THEN                                                                                                     ");
			sb.append("      nvl (                                                                                                           ");
			sb.append("        sb.bill_num,                                                                                                  ");
			sb.append("        (                                                                                                             ");
			sb.append("          SELECT                                                                                                      ");
			//sb.append("          listagg(bill_num,',') WITHIN GROUP (order by bill_num ) AS billNum                                                                                              ");

					sb.append("            to_char (                                                                                                 ");
			sb.append("              wmsys.wm_concat (DISTINCT bill_num)                                                                 ");
			sb.append("            ) AS billNum                                                                                              ");
			sb.append("          FROM                                                                                                        ");
			sb.append("            bis_out_stock_info                                                                                    ");
			sb.append("          WHERE                                                                                                       ");
			sb.append("            out_link_id = link_id                                                                                 ");
			sb.append("        )                                                                                                             ");
			sb.append("      )                                                                                                               ");
			sb.append("    ELSE                                                                                                              ");
			sb.append("      sb.bill_num                                                                                                     ");
			sb.append("    END                                                                                                               ");
			sb.append("  ) || sb.link_id || ':' || CRK_SIGN || ':' || charge_start_date || ':' || charge_end_date || ':' || sb.piece AS KEYS,");
			sb.append("  min(BILL_DATE) AS BILL_DATE                                                                                         ");
			sb.append(" FROM                                                                                                                  ");
			sb.append("  (                                                                                                                   ");
			sb.append("    SELECT                                                                                                            ");
			sb.append("      CASE                                                                                                            ");
			sb.append("    WHEN STORAGE_STATE = '3' THEN                                                                                     ");
			sb.append("      '0'                                                                                                             ");
			sb.append("    ELSE                                                                                                              ");
			sb.append("      STORAGE_STATE                                                                                                   ");
			sb.append("    END order_STORAGESTATE,                                                                                           ");
			sb.append("    bis_standing_book.*                                                                                               ");
			sb.append("  FROM                                                                                                                ");
			sb.append("    bis_standing_book                                                                                                 ");
			sb.append("  WHERE                                                                                                               ");
			sb.append("    reconcile_num =:code                                                                                              ");
			sb.append("  AND reconcile_sign = '1'                                                                                            ");
			sb.append("  AND EXAMINE_SIGN = 1                                                                                                ");
			sb.append("  AND STORAGE_STATE IS NOT NULL                                                                                       ");
			sb.append("  ) sb                                                                                                                ");
			sb.append("LEFT JOIN (                                                                                                           ");
			sb.append("  SELECT                                                                                                              ");
			sb.append("    tray.bill_num,                                                                                                         ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      tray.original_piece - tray.remove_piece                                                                                   ");
			sb.append("    ) AS piece,                                                                                                       ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      (                                                                                                               ");
			sb.append("        tray.original_piece - tray.remove_piece                                                                                 ");
			sb.append("      ) * tray.net_single                                                                                                  ");
			sb.append("    ) AS net_weight,                                                                                                  ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      (                                                                                                               ");
			sb.append("        tray.original_piece - tray.remove_piece                                                                       ");
			sb.append("      ) * tray.gross_single                                                                                           ");
			sb.append("    ) AS gross_weight                                                                                                 ");
			sb.append("  FROM                                                                                                                ");
			sb.append("    bis_tray_info tray                                                                                                ");
			sb.append("  inner join                                                                                                          ");
			sb.append("    bis_asn asn                                                                                                   ");
			sb.append("  on tray.asn=asn.asn                                                                                                 ");
			sb.append("  WHERE                                                                                                               ");
			sb.append("    tray.CARGO_STATE <> '99' AND asn.if_second_enter<> '2'                                                            ");//去掉重收的数量
			sb.append("  GROUP BY                                                                                                            ");
			sb.append("    tray.bill_num                                                                                                     ");
			sb.append(") pe ON pe.bill_num = sb.bill_num                                                                                     ");
			sb.append("LEFT JOIN (                                                                                                           ");
			sb.append("  SELECT                                                                                                              ");
			sb.append("    out_link_id,                                                                                                      ");
			sb.append("    sum(piece) AS piece,                                                                                              ");
			sb.append("    sum(net_weight) AS net_weight,                                                                                    ");
			sb.append("    sum(gross_weight) AS gross_weight                                                                                 ");
			sb.append("  FROM                                                                                                                ");
			sb.append("    bis_loading_info                                                                                                  ");
			sb.append("  GROUP BY                                                                                                            ");
			sb.append("    out_link_id                                                                                                       ");
			sb.append(") po ON po.out_link_id = sb.link_id                                                                                   ");
			sb.append("GROUP BY                                                                                                              ");
			sb.append("  link_id,                                                                                                            ");
			sb.append("  sb.bill_num,                                                                                                        ");
			sb.append("  crk_sign,                                                                                                           ");
			sb.append("  order_STORAGESTATE,                                                                                                 ");
			sb.append("  STORAGE_STATE,                                                                                                      ");
			sb.append("  charge_start_date,                                                                                                  ");
			sb.append("  charge_end_date,                                                                                                    ");
			sb.append("  sb.piece,                                                                                                           ");
			sb.append("  fee_code,                                                                                                           ");
			sb.append("  price                                                                                                               ");
			sb.append("ORDER BY                                                                                                              ");
			sb.append("  order_STORAGESTATE,                                                                                                 ");
			sb.append("  bill_num DESC,                                                                                                      ");
			sb.append("  charge_start_date,                                                                                                  ");
			sb.append("  charge_end_date,                                                                                                    ");
			sb.append("  KEYS                                                                                                                ");
			params.put("code", checkingBookCode);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			System.out.println(sb.toString());
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}

	/**
	 * 对账单明细导出 原备份20180903
	 * @param checkingBookCode
	 * @return
	 */
	/*public  List<Map<String, Object>> getRepCheckingBookInfo(String checkingBookCode,String type) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select  sb.link_id as link_id, ");
//			sb.append("(CASE sb.crk_sign WHEN '1' THEN  nvl(sb.bill_num,(select wmsys.wm_concat(t.item_num) as billNum from bis_enter_stock t where t.link_id= link_id))  WHEN '2' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(tt.bill_num) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   WHEN '3' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(ttt.bill_num) as billNum from bis_transfer_stock_info ttt where ttt.transfer_link_id= link_id)) END  ) as bill_num,");
			sb.append("(CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct tt.bill_num)) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) as bill_num,");
			sb.append("sb.crk_sign as ntype,nvl(min(sb.customs_name),'') as uname,sb.order_STORAGESTATE,sb.STORAGE_STATE,(case sb.STORAGE_STATE when '1' then '在库'  when '2' then '出' when '3' then '在库' when '4' then '在库' end) as CRK_SIGN, ");
			sb.append("(case sb.STORAGE_STATE when '4' then '1' else '0' end) as hs,");
			sb.append(" to_char(wmsys.wm_concat(distinct cargo_name)) as cargo_name,");
			sb.append("sum(nvl(sb.piece,(case sb.crk_sign when '1' then (select sum(pe.original_piece-pe.remove_piece) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.piece) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as piece,");
			sb.append("sum(nvl(sb.net_weight,(case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.net_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.net_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as net_weight,");
			sb.append("sum( nvl(sb.gross_weight, (case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.gross_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.gross_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as gross_weight,");
			sb.append(" min(charge_day) as charge_day,nvl(sb.charge_start_date,'') as charge_start_date,nvl(sb.charge_end_date,'') as charge_end_date, sum(sb.SHOULD_RMB) as SHOULD_RMB,  ");
			sb.append(" min(sb.fee_name) as fee_name, min(sb.price) as price, CONCAT(min(sb.fee_code),min(sb.price)) as lab, ");
			sb.append(" (CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct ttt.bill_num)) as billNum from bis_out_stock_info ttt where ttt.out_link_id= link_id))   ELSE sb.bill_num END  ) ||sb.link_id || ':' || CRK_SIGN||':'||charge_start_date||':'||charge_end_date||':'||piece  as keys,min(BILL_DATE) as BILL_DATE ");
			sb.append(" from (select case when STORAGE_STATE='3' then '0' else STORAGE_STATE end order_STORAGESTATE,bis_standing_book.* from bis_standing_book ");
			sb.append(" where reconcile_num=:code and reconcile_sign='1' and EXAMINE_SIGN=1");
//			if(type.equals("2")){
				sb.append(" and STORAGE_STATE is not null");
//			}
			sb.append(") sb ");
			params.put("code", checkingBookCode);
			sb.append(" group by link_id,bill_num,crk_sign,order_STORAGESTATE,STORAGE_STATE,charge_start_date,charge_end_date,piece,fee_code,price");
//			if(type.equals("1")){
//				sb.append(" order by bill_num desc,order_STORAGESTATE,charge_start_date,charge_end_date ");
//			}else{
				sb.append(" order by order_STORAGESTATE,bill_num desc,charge_start_date,charge_end_date,keys");
//			}
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}*/

	/**
	 * 根据对账单导出实际费用金额
	 * @param codeNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getFeeRmb(String codeNum){
		StringBuffer sb=new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		sb.append(" SELECT                                                                    ");
		sb.append("   TEMP.FEECODE,                                                           ");
		sb.append("   TEMP.FEENAME,                                                           ");
		sb.append("   TEMP.LABEL,                                                             ");
		sb.append("   TEMP.EXCHANGE_RATE RATE,                                                ");
		sb.append("   round(SUM(TEMP.NUM),2) NUM,                                             ");
		sb.append("   round(SUM(TEMP.AMOUNT),2) AMOUNT,                                       ");
		sb.append("   round(SUM(TEMP.rmb),2) RMB                                              ");
		sb.append(" FROM                                                                      ");
		sb.append(" (                                                                         ");
		sb.append("    SELECT                                                                 ");
		sb.append("       NVL(fee.YSCODE, book.fee_code) feecode,                             ");
		sb.append(" 	  NVL(fee.YSINFO, book.fee_name) feename,                             ");
		sb.append("       dict.LABEL,                                                         ");
		sb.append(" 	  book.EXCHANGE_RATE,                                                 ");
		sb.append("       book.NUM,                                                           ");
		sb.append("       book.RECEIVE_AMOUNT amount,                                         ");
		sb.append("       book.SHOULD_RMB rmb                                                 ");
		sb.append(" 	FROM                                                                  ");
		sb.append(" 	  bis_standing_book book                                              ");
		sb.append("     LEFT JOIN                                                             ");
		sb.append("       BASE_EXPENSE_CATEGORY_INFO fee                                      ");
		sb.append("     ON                                                                    ");
		sb.append("       book.fee_code = fee.CODE                                            ");
		sb.append("     LEFT JOIN                                                             ");
		sb.append("       (SELECT LABEL,VALUE FROM dict where type='currencyType') dict       ");
		sb.append("     ON                                                                    ");
		sb.append("       book.CURRENCY=dict.value                                            ");
		sb.append(" 		WHERE                                                             ");
		sb.append(" 		  book.reconcile_sign = '1'                                       ");
		sb.append(" 		  AND book.EXAMINE_SIGN = 1                                       ");
		sb.append("           AND book.IF_RECEIVE=1                                           ");
		if(codeNum!=null&&!"".equals(codeNum)){
			params.put("codeNum", codeNum);
			sb.append(" 	  AND  book.reconcile_num =:codeNum                               ");
		}
		sb.append(" )temp                                                                     ");
		sb.append(" GROUP BY                                                                  ");
		sb.append("   temp.FEECODE,                                                           ");
		sb.append("   TEMP.feename,                                                           ");
		sb.append("   TEMP.LABEL,                                                             ");
		sb.append("   TEMP.EXCHANGE_RATE                                                      ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 对账单明细导出
	 * @param checkingBookCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getRepCheckingBookInfo2(String checkingBookCode) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select  sb.link_id as link_id, ");
			sb.append("(CASE sb.crk_sign WHEN '1' THEN  nvl(sb.bill_num,(select wmsys.wm_concat(t.item_num) as billNum from bis_enter_stock t where t.link_id= link_id))  WHEN '2' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(tt.bill_num) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   WHEN '3' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(ttt.bill_num) as billNum from bis_transfer_stock_info ttt where ttt.transfer_link_id= link_id)) END  ) as bill_num,");
		//	sb.append("(CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select listagg(tt.bill_num,',') WITHIN GROUP (order by tt.bill_num ) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) as bill_num,");

			sb.append("(CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct tt.bill_num)) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) as bill_num,");
			sb.append("sb.crk_sign as ntype,nvl(min(sb.customs_name),'') as uname,sb.order_STORAGESTATE,sb.STORAGE_STATE,(case sb.STORAGE_STATE when '1' then '在库'  when '2' then '出' when '3' then '在库' when '4' then '在库' end) as CRK_SIGN, ");
			sb.append("(case sb.STORAGE_STATE when '4' then '1' else '0' end) as hs,");
			//sb.append("nvl(listagg(cargo_name,',') WITHIN GROUP (order by cargo_name),(case crk_sign when '1' then (select to_char(wmsys.wm_concat(distinct enter.cargo_name)) from bis_enter_stock_info enter where enter.item_num=sb.bill_num) when '2' then (select to_char(wmsys.wm_concat(distinct outt.cargo_name)) from bis_out_stock_info outt where outt.out_link_id=sb.link_id) else ' ' end))as cargo_name,");

			sb.append("nvl(to_char(wmsys.wm_concat(distinct cargo_name)),(case crk_sign when '1' then (select to_char(wmsys.wm_concat(distinct enter.cargo_name)) from bis_enter_stock_info enter where enter.item_num=sb.bill_num) when '2' then (select to_char(wmsys.wm_concat(distinct outt.cargo_name)) from bis_out_stock_info outt where outt.out_link_id=sb.link_id) else ' ' end))as cargo_name,");
			sb.append("sum(nvl(sb.piece,(case sb.crk_sign when '1' then (select sum(pe.original_piece-pe.remove_piece) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.piece) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as piece,");
			sb.append("sum(nvl(sb.net_weight,(case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.net_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.net_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as net_weight,");
			sb.append("sum( nvl(sb.gross_weight, (case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.gross_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.gross_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as gross_weight,");
			sb.append(" min(charge_day) as charge_day,nvl(sb.charge_start_date,'') as charge_start_date,nvl(sb.charge_end_date,'') as charge_end_date, sum(sb.SHOULD_RMB) as SHOULD_RMB,  ");
			sb.append(" min(sb.fee_name) as fee_name, min(sb.price) as price, CONCAT(min(sb.fee_code),min(sb.price)) as lab, ");
			//sb.append(" (CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select listagg(tt.bill_num,',') WITHIN GROUP (order by tt.bill_num ) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) || ':' || CRK_SIGN||':'||charge_start_date||':'||charge_end_date||':'||piece  as keys,min(BILL_DATE) as BILL_DATE ");

			sb.append(" (CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct tt.bill_num)) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) || ':' || CRK_SIGN||':'||charge_start_date||':'||charge_end_date||':'||piece  as keys,min(BILL_DATE) as BILL_DATE ");
			sb.append(" from (select case when STORAGE_STATE='3' then '0' else STORAGE_STATE end order_STORAGESTATE,bis_standing_book.* from bis_standing_book ");
			sb.append(" where reconcile_num=:code and reconcile_sign='1' and EXAMINE_SIGN=1");
			sb.append(" and STORAGE_STATE is null");
			sb.append(") sb ");
			params.put("code", checkingBookCode);
			sb.append(" group by link_id,bill_num,crk_sign,order_STORAGESTATE,STORAGE_STATE,charge_start_date,charge_end_date,piece,fee_code,price");
			sb.append(" order by ntype ");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getRepCheckingBookInfo11(String checkingBookCode) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer();
			sb.append(" SELECT                                                                                                                     ");
			sb.append("   sb.link_id AS link_id,                                                                                                   ");
			sb.append("   (                                                                                                                        ");
			sb.append("     CASE sb.crk_sign                                                                                                       ");
			sb.append("     WHEN '2' THEN                                                                                                          ");
			sb.append("       nvl (                                                                                                                ");
			sb.append("         sb.bill_num,                                                                                                       ");
			sb.append("         (                                                                                                                  ");
			sb.append("           SELECT                                                                                                           ");
			//sb.append("         listagg(bill_num,',') WITHIN GROUP (order by bill_num )   AS billNum                                                                                                     ");

						sb.append("             to_char (                                                                                                      ");
			sb.append("               wmsys.wm_concat (DISTINCT bill_num)                                                                       ");
			sb.append("             ) AS billNum                                                                                                   ");
			sb.append("           FROM                                                                                                             ");
			sb.append("             bis_out_stock_info                                                                                          ");
			sb.append("           WHERE                                                                                                            ");
			sb.append("             out_link_id = link_id                                                                                       ");
			sb.append("         )                                                                                                                  ");
			sb.append("       )                                                                                                                    ");
			sb.append("     ELSE                                                                                                                   ");
			sb.append("       sb.bill_num                                                                                                          ");
			sb.append("     END                                                                                                                    ");
			sb.append("   ) AS bill_num,                                                                                                           ");
			sb.append("   sb.crk_sign AS ntype,                                                                                                    ");
			sb.append("   nvl (min(sb.customs_name), '') AS uname,                                                                                 ");
			sb.append("   sb.order_STORAGESTATE,                                                                                                   ");
			sb.append("   sb.STORAGE_STATE,                                                                                                        ");
			sb.append("   (                                                                                                                        ");
			sb.append("     CASE sb.STORAGE_STATE                                                                                                  ");
			sb.append("     WHEN '1' THEN                                                                                                          ");
			sb.append("       '在库'                                                                                                               ");
			sb.append("     WHEN '2' THEN                                                                                                          ");
			sb.append("       '出'                                                                                                                 ");
			sb.append("     WHEN '3' THEN                                                                                                          ");
			sb.append("       '在库'                                                                                                               ");
			sb.append("     WHEN '4' THEN                                                                                                          ");
			sb.append("       '在库'                                                                                                               ");
			sb.append("     END                                                                                                                    ");
			sb.append("   ) AS CRK_SIGN,                                                                                                           ");
			sb.append("   (                                                                                                                        ");
			sb.append("     CASE sb.STORAGE_STATE                                                                                                  ");
			sb.append("     WHEN '4' THEN                                                                                                          ");
			sb.append("       '1'                                                                                                                  ");
			sb.append("     ELSE                                                                                                                   ");
			sb.append("       '0'                                                                                                                  ");
			sb.append("     END                                                                                                                    ");
			sb.append("   ) AS hs,                                                                                                                 ");
			sb.append("   nvl (                                                                                                                    ");
       //      sb.append("listagg(cargo_name,',') WITHIN GROUP (order by cargo_name ),");
					sb.append("     to_char (                                                                                                              ");
			sb.append("       wmsys.wm_concat (DISTINCT cargo_name)                                                                                ");
		sb.append("     ),                                                                                                                     ");
			sb.append("     (                                                                                                                      ");
			sb.append("       CASE crk_sign                                                                                                        ");
			sb.append("       WHEN '1' THEN                                                                                                        ");
			sb.append("         (                                                                                                                  ");
			sb.append("           SELECT                                                                                                           ");
			//sb.append("           listagg(cargo_name,',') WITHIN GROUP (order by cargo_name )                                                                                                    ");

			sb.append("             to_char (                                                                                                      ");
			sb.append("               wmsys.wm_concat (DISTINCT cargo_name)                                                                  ");
			sb.append("             )                                                                                                              ");
			sb.append("           FROM                                                                                                             ");
			sb.append("             bis_enter_stock_info                                                                                     ");
			sb.append("           WHERE                                                                                                            ");
			sb.append("             item_num = sb.bill_num                                                                                   ");
			sb.append("         )                                                                                                                  ");
			sb.append("       WHEN '2' THEN                                                                                                        ");
			sb.append("         (                                                                                                                  ");
			sb.append("           SELECT                                                                                                           ");
			//sb.append("          listagg(cargo_name,',') WITHIN GROUP (order by cargo_name )       )                                                                                                              ");

			sb.append("             to_char (            wmsys.wm_concat (DISTINCT cargo_name)        )                                                                                                              ");
			sb.append("           FROM                                                                                                             ");
			sb.append("             bis_out_stock_info                                                                                       ");
			sb.append("           WHERE                                                                                                            ");
			sb.append("             out_link_id = sb.link_id                                                                                  ");
			sb.append("         )                                                                                                                  ");
			sb.append("       ELSE                                                                                                                 ");
			sb.append("         ' '                                                                                                                ");
			sb.append("       END                                                                                                                  ");
			sb.append("     )                                                                                                                      ");
			sb.append("   ) AS cargo_name,                                                                                                         ");
			sb.append("   sum(                                                                                                                     ");
			sb.append("     nvl (                                                                                                                  ");
			sb.append("       sb.piece,                                                                                                            ");
			sb.append("       (                                                                                                                    ");
			sb.append("         CASE sb.crk_sign                                                                                                   ");
			sb.append("         WHEN '1' THEN                                                                                                      ");
			sb.append("           pe.piece                                                                                                         ");
			sb.append("         WHEN '2' THEN                                                                                                      ");
			sb.append("           po.piece                                                                                                         ");
			sb.append("         ELSE                                                                                                               ");
			sb.append("           0                                                                                                                ");
			sb.append("         END                                                                                                                ");
			sb.append("       )                                                                                                                    ");
			sb.append("     )                                                                                                                      ");
			sb.append("   ) AS piece,                                                                                                              ");
			sb.append("   round(sum(                                                                                                                     ");
			sb.append("     nvl (                                                                                                                  ");
			sb.append("       sb.net_weight,                                                                                                       ");
			sb.append("       (                                                                                                                    ");
			sb.append("         CASE sb.crk_sign                                                                                                   ");
			sb.append("         WHEN '1' THEN                                                                                                      ");
			sb.append("           pe.net_weight                                                                                                    ");
			sb.append("         WHEN '2' THEN                                                                                                      ");
			sb.append("           po.net_weight                                                                                                    ");
			sb.append("         ELSE                                                                                                               ");
			sb.append("           0                                                                                                                ");
			sb.append("         END                                                                                                                ");
			sb.append("       )                                                                                                                    ");
			sb.append("     )                                                                                                                      ");
			sb.append("   ),2) AS net_weight,                                                                                                         ");
			sb.append("   round(sum(                                                                                                                     ");
			sb.append("     nvl (                                                                                                                  ");
			sb.append("       sb.gross_weight,                                                                                                     ");
			sb.append("       (                                                                                                                    ");
			sb.append("         CASE sb.crk_sign                                                                                                   ");
			sb.append("         WHEN '1' THEN                                                                                                      ");
			sb.append("           pe.gross_weight                                                                                                  ");
			sb.append("         WHEN '2' THEN                                                                                                      ");
			sb.append("           po.gross_weight                                                                                                  ");
			sb.append("         ELSE                                                                                                               ");
			sb.append("           0                                                                                                                ");
			sb.append("         END                                                                                                                ");
			sb.append("       )                                                                                                                    ");
			sb.append("     )                                                                                                                      ");
			sb.append("   ),2) AS gross_weight,                                                                                                       ");
			sb.append("   min(charge_day) AS charge_day,                                                                                           ");
			sb.append("   nvl (sb.charge_start_date, '') AS charge_start_date,                                                                     ");
			sb.append("   nvl (sb.charge_end_date, '') AS charge_end_date,                                                                         ");
			sb.append("   round(sum(sb.SHOULD_RMB),2) AS SHOULD_RMB,                                                                                        ");
			sb.append("   min(sb.fee_name) AS fee_name,                                                                                            ");
			sb.append("   min(sb.price) AS price,                                                                                                  ");
			sb.append("   CASE                                                                                                                     ");
			sb.append("      when min(ci.fee_type) is null then min(sb.fee_code)                                                                   ");
			sb.append("      WHEN min(ci.fee_type)= '1' THEN                                                                                       ");
			sb.append("      min(sb.fee_code)                                                                                                      ");
			sb.append("   ELSE                                                                                                                     ");
		    sb.append("      CONCAT(min(sb.fee_code), min(sb.price))                                                                               ");
		    sb.append("   END AS lab,                                                                                                              ");
			//sb.append("   CASE min(ci.fee_type)                                                                                                    ");
			//sb.append(" WHEN '1' THEN                                                                                                              ");
			//sb.append("   min(sb.fee_code)                                                                                                         ");
			//sb.append(" ELSE                                                                                                                       ");
			//sb.append("   CONCAT(                                                                                                                  ");
			//sb.append("     min(sb.fee_code),                                                                                                      ");
			//sb.append("     min(sb.price)                                                                                                          ");
			//sb.append("   )                                                                                                                        ");
			//sb.append(" END AS lab,                                                                                                                ");
			sb.append("  (                                                                                                                         ");
			sb.append("   CASE sb.crk_sign                                                                                                         ");
			sb.append("   WHEN '2' THEN                                                                                                            ");
			sb.append("     nvl (                                                                                                                  ");
			sb.append("       sb.bill_num,                                                                                                         ");
			sb.append("       (                                                                                                                    ");
			sb.append("         SELECT                                                                                                             ");
			//sb.append("           listagg(bill_num,',') WITHIN GROUP (order by bill_num ) AS billNum                                                                                                     ");

			sb.append("           to_char (  wmsys.wm_concat (DISTINCT bill_num)     ) AS billNum                                                                                                     ");
			sb.append("         FROM                                                                                                               ");
			sb.append("           bis_out_stock_info                                                                                            ");
			sb.append("         WHERE                                                                                                              ");
			sb.append("           out_link_id = link_id                                                                                         ");
			sb.append("       )                                                                                                                    ");
			sb.append("     )                                                                                                                      ");
			sb.append("   ELSE                                                                                                                     ");
			sb.append("     sb.bill_num                                                                                                            ");
			sb.append("   END                                                                                                                      ");
			sb.append(" ) || sb.link_id || ':' || CRK_SIGN || ':' || charge_start_date || ':' || charge_end_date || ':' || sb.piece AS KEYS,       ");
			sb.append("  min(BILL_DATE) AS BILL_DATE                                                                                               ");
			sb.append(" FROM                                                                                                                       ");
			sb.append("   (                                                                                                                        ");
			sb.append("     SELECT                                                                                                                 ");
			sb.append("       CASE                                                                                                                 ");
			sb.append("     WHEN STORAGE_STATE = '3' THEN                                                                                          ");
			sb.append("       '0'                                                                                                                  ");
			sb.append("     ELSE                                                                                                                   ");
			sb.append("       STORAGE_STATE                                                                                                        ");
			sb.append("     END order_STORAGESTATE,                                                                                                ");
			sb.append("     bis_standing_book.*                                                                                                    ");
			sb.append("   FROM                                                                                                                     ");
			sb.append("     bis_standing_book                                                                                                      ");
			sb.append("   WHERE                                                                                                                    ");
			sb.append("     reconcile_num =:code                                                                                                   ");
			sb.append("   AND reconcile_sign = '1'                                                                                                 ");
			sb.append("   AND EXAMINE_SIGN = 1                                                                                                     ");
			sb.append("   AND STORAGE_STATE IS NULL                                                                                                ");
			sb.append("   ) sb                                                                                                                     ");
			sb.append(" LEFT JOIN (                                                                                                                ");
			sb.append("  SELECT                                                                                                              ");
			sb.append("    tray.bill_num,                                                                                                    ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      tray.original_piece - tray.remove_piece                                                                         ");
			sb.append("    ) AS piece,                                                                                                       ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      (                                                                                                               ");
			sb.append("        tray.original_piece - tray.remove_piece                                                                       ");
			sb.append("      ) * tray.net_single                                                                                             ");
			sb.append("    ) AS net_weight,                                                                                                  ");
			sb.append("    sum(                                                                                                              ");
			sb.append("      (                                                                                                               ");
			sb.append("        tray.original_piece - tray.remove_piece                                                                       ");
			sb.append("      ) * tray.gross_single                                                                                           ");
			sb.append("    ) AS gross_weight                                                                                                 ");
			sb.append("  FROM                                                                                                                ");
			sb.append("    bis_tray_info tray                                                                                                ");
			sb.append("  inner join                                                                                                          ");
			sb.append("    bis_asn asn                                                                                                   ");
			sb.append("  on tray.asn=asn.asn                                                                                                 ");
			sb.append("  WHERE                                                                                                               ");
			sb.append("    tray.CARGO_STATE <> '99' AND asn.if_second_enter<> '2'                                                            ");//去掉重收的数量
			sb.append("  GROUP BY                                                                                                            ");
			sb.append("    tray.bill_num                                                                                                     ");
			sb.append(" ) pe ON pe.bill_num = sb.bill_num                                                                                    ");
			sb.append(" LEFT JOIN (                                                                                                          ");
			sb.append("   SELECT                                                                                                             ");
			sb.append("     out_link_id,                                                                                                     ");
			sb.append("     sum(piece) AS piece,                                                                                             ");
			sb.append("     sum(net_weight) AS net_weight,                                                                                   ");
			sb.append("     sum(gross_weight) AS gross_weight                                                                                ");
			sb.append("   FROM                                                                                                               ");
			sb.append("     bis_loading_info                                                                                                 ");
			sb.append("   GROUP BY                                                                                                           ");
			sb.append("     out_link_id                                                                                                      ");
			sb.append(" ) po ON po.out_link_id = sb.link_id                                                                                  ");
			sb.append(" LEFT JOIN BASE_EXPENSE_CATEGORY_INFO ci ON sb.fee_code = ci. CODE                                                    ");
			sb.append(" GROUP BY                                                                                                             ");
			sb.append("   link_id,                                                                                                           ");
			sb.append("   sb.bill_num,                                                                                                       ");
			sb.append("   crk_sign,                                                                                                          ");
			sb.append("   order_STORAGESTATE,                                                                                                ");
			sb.append("   STORAGE_STATE,                                                                                                     ");
			sb.append("   charge_start_date,                                                                                                 ");
			sb.append("   charge_end_date,                                                                                                   ");
			sb.append("   sb.piece,                                                                                                          ");
			sb.append("   fee_code,                                                                                                          ");
			sb.append("   price                                                                                                              ");
			sb.append(" ORDER BY                                                                                                             ");
			sb.append("   ntype                                                                                                              ");
			params.put("code", checkingBookCode);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}

	//原方法备份20180903
/*	public  List<Map<String, Object>> getRepCheckingBookInfo11(String checkingBookCode) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select  sb.link_id as link_id, ");
//			sb.append("(CASE sb.crk_sign WHEN '1' THEN  nvl(sb.bill_num,(select wmsys.wm_concat(t.item_num) as billNum from bis_enter_stock t where t.link_id= link_id))  WHEN '2' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(tt.bill_num) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   WHEN '3' THEN   nvl(sb.bill_num,(select wmsys.wm_concat(ttt.bill_num) as billNum from bis_transfer_stock_info ttt where ttt.transfer_link_id= link_id)) END  ) as bill_num,");
			sb.append("(CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct tt.bill_num)) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) as bill_num,");
			sb.append("sb.crk_sign as ntype,nvl(min(sb.customs_name),'') as uname,sb.order_STORAGESTATE,sb.STORAGE_STATE,(case sb.STORAGE_STATE when '1' then '在库'  when '2' then '出' when '3' then '在库' when '4' then '在库' end) as CRK_SIGN, ");
			sb.append("(case sb.STORAGE_STATE when '4' then '1' else '0' end) as hs,");
			sb.append("nvl(to_char(wmsys.wm_concat(distinct cargo_name)),(case crk_sign when '1' then (select to_char(wmsys.wm_concat(distinct enter.cargo_name)) from bis_enter_stock_info enter where enter.item_num=sb.bill_num) when '2' then (select to_char(wmsys.wm_concat(distinct outt.cargo_name)) from bis_out_stock_info outt where outt.out_link_id=sb.link_id) else ' ' end))as cargo_name,");
			sb.append("sum(nvl(sb.piece,(case sb.crk_sign when '1' then (select sum(pe.original_piece-pe.remove_piece) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.piece) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as piece,");
			sb.append("sum(nvl(sb.net_weight,(case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.net_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.net_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as net_weight,");
			sb.append("sum( nvl(sb.gross_weight, (case sb.crk_sign when '1' then (select sum((pe.original_piece-pe.remove_piece)*pe.gross_single) from bis_tray_info pe where pe.bill_num=sb.bill_num) when '2' then (select sum(po.gross_weight) from bis_loading_info po where po.out_link_id=sb.link_id) else 0 end))) as gross_weight,");
			sb.append(" min(charge_day) as charge_day,nvl(sb.charge_start_date,'') as charge_start_date,nvl(sb.charge_end_date,'') as charge_end_date, sum(sb.SHOULD_RMB) as SHOULD_RMB,  ");
			sb.append(" min(sb.fee_name) as fee_name, min(sb.price) as price,  case min(ci.fee_type) when '1' then  min(sb.fee_code) else CONCAT(min(sb.fee_code), min(sb.price)) end as lab, ");
			sb.append(" (CASE sb.crk_sign   WHEN '2' THEN   nvl(sb.bill_num,(select to_char(wmsys.wm_concat(distinct tt.bill_num)) as billNum from bis_out_stock_info tt where tt.out_link_id= link_id))   ELSE sb.bill_num END  ) ||sb.link_id || ':' || CRK_SIGN||':'||charge_start_date||':'||charge_end_date||':'||piece  as keys,min(BILL_DATE) as BILL_DATE ");
			sb.append(" from (select case when STORAGE_STATE='3' then '0' else STORAGE_STATE end order_STORAGESTATE,bis_standing_book.* from bis_standing_book ");
			sb.append(" where reconcile_num=:code and reconcile_sign='1' and EXAMINE_SIGN=1");
			sb.append(" and STORAGE_STATE is null");
			sb.append(") sb ");
			params.put("code", checkingBookCode);
			sb.append("  left join  BASE_EXPENSE_CATEGORY_INFO ci  on sb.fee_code = ci.code ");

			sb.append(" group by link_id,bill_num,crk_sign,order_STORAGESTATE,STORAGE_STATE,charge_start_date,charge_end_date,piece,fee_code,price");
			sb.append(" order by ntype ");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}*/
	/**
	 * 获取excel导出列---费目明细
	 * @param checkingBookCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getRepCheckingBookHead(String checkingBookCode) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer(" select a.fee_code,a.price,a.lab,ci.name_c,ci.name_e,ci.fee_type from ( ");
			sb.append("  select sb.fee_code,sb.price,CONCAT(sb.fee_code, sb.price )as lab  ");
			sb.append(" from bis_standing_book sb  ");
			sb.append(" where sb.reconcile_num=:code and sb.reconcile_sign='1' and sb.EXAMINE_SIGN=1");
			params.put("code", checkingBookCode);
			sb.append(" group by fee_code,price ");
			sb.append(" ) a left join BASE_EXPENSE_CATEGORY_INFO ci on a.fee_code=ci.code ");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getRepCheckingBookHead11(String checkingBookCode) {
		if(checkingBookCode!=null && !"".equals(checkingBookCode)){
			Map<String,Object> params = new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer();
            /*
            StringBuffer sb=new StringBuffer(" select sb.fee_code, 0 as price, sb.fee_code as lab,ci.name_c, ci.name_e, ci.fee_type ");
			sb.append(" from bis_standing_book sb  ");
			sb.append(" inner join BASE_EXPENSE_CATEGORY_INFO ci on sb.fee_code = ci.code  ");
			sb.append(" where sb.reconcile_num=:code and sb.reconcile_sign='1' and sb.EXAMINE_SIGN=1 and  ci.FEE_TYPE='1' ");
			sb.append(" group by fee_code,ci.name_c, ci.name_e, ci.fee_type ");
			sb.append(" union ");
			sb.append(" select sb.fee_code, sb.price as price, CONCAT(min(sb.fee_code), min(sb.price)) as lab,ci.name_c, ci.name_e, ci.fee_type ");
			sb.append(" from bis_standing_book sb  ");
			sb.append(" inner join BASE_EXPENSE_CATEGORY_INFO ci on sb.fee_code = ci.code  ");
			sb.append(" where sb.reconcile_num=:code and sb.reconcile_sign='1' and sb.EXAMINE_SIGN=1 and  ci.FEE_TYPE<>'1' ");
			params.put("code", checkingBookCode);
			sb.append(" group by fee_code, sb.price,ci.name_c, ci.name_e, ci.fee_type ");
			*/
			sb.append(" SELECT  ");
			sb.append("  g.fee_code,");
			sb.append("  g.price,");
			sb.append("  g.name_c,");
			sb.append("  g.lab, ");
			sb.append("  g.name_e, ");
			sb.append("  g.fee_type ");
			sb.append(" FROM(  ");
			sb.append("  SELECT ");
			sb.append(" temp.fee_code,");
			sb.append(" CASE WHEN temp.fee_type = '1' THEN 0 ELSE temp.price END AS price,");
			sb.append(" CASE WHEN temp.fee_type = '1' THEN temp.fee_code ELSE CONCAT (MIN (temp.fee_code),MIN (temp.price)) END AS lab, ");
			sb.append(" 	temp.name_c, ");
			sb.append(" 	temp.name_e, ");
			sb.append(" 	temp.fee_type ");
			sb.append(" FROM (   ");
			sb.append("   SELECT   ");
			sb.append(" 	sb.fee_code,");
			sb.append(" 	sb.price AS price,");
			sb.append(" 	NVL (sb.fee_code, sb.fee_code) AS lab,");
			sb.append(" 	NVL (ci.name_c, sb.fee_name) AS name_c,");
			sb.append(" 	NVL (ci.name_e, 'English') AS name_e,");
			sb.append(" 	NVL (ci.fee_type, '1') AS fee_type  ");
			sb.append("   FROM  ");
			sb.append(" 	bis_standing_book sb  ");
			sb.append(" 	LEFT JOIN BASE_EXPENSE_CATEGORY_INFO ci ON sb.fee_code = ci. CODE  ");
			sb.append("   WHERE sb.reconcile_num =:code AND sb.reconcile_sign = '1' AND sb.EXAMINE_SIGN = 1 ");
			sb.append("  ) temp  ");
			sb.append(" GROUP BY  ");
			sb.append("   temp.fee_code, ");
			sb.append("   temp.price,");
			sb.append("   temp.name_c, ");
			sb.append("   temp.lab, ");
			sb.append("   temp.name_e, ");
			sb.append("   temp.fee_type ");
			sb.append("  ) g  ");
			sb.append(" GROUP BY ");
			sb.append(" 	g.fee_code, ");
			sb.append(" 	g.price, ");
			sb.append(" 	g.name_c, ");
			sb.append(" 	g.lab,   ");
			sb.append(" 	g.name_e, ");
			sb.append(" 	g.fee_type ");
			params.put("code", checkingBookCode);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTotal(String codeNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select sum(nvl(SHOULD_RMB,0)) as RMB from bis_standing_book t where t.reconcile_num =:codeNum group by reconcile_num";
		params.put("codeNum", codeNum);
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> connectBillNum(String linkId) {
		//String sql = "select t.out_link_id, listagg(t.bill_num,',') WITHIN GROUP (order by t.bill_num ) as billNum from bis_out_stock_info t where t.out_link_id=?0 group by t.out_link_id";
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select t.out_link_id, to_char(wmsys.wm_concat(distinct t.bill_num)) as billNum from bis_out_stock_info t where t.out_link_id=:linkId group by t.out_link_id";
		params.put("linkId", linkId);
		SQLQuery sqlQuery=createSQLQuery(sql, params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	public void updateStatusByPayId(List<String> origBizIds) {
		for (String origBizId : origBizIds) {
			if(!StringUtils.isEmpty(origBizId)){
				StringBuffer sql = new StringBuffer();
				Map<String,Object> parems=new HashMap<>();
				sql.append("UPDATE BIS_CHEKING_BOOK set midGroupStatic = :midGroupStatic where 1 =1 and codeNum = :codeNum");
				parems.put("midGroupStatic",STATU);
				parems.put("codeNum",origBizId);
				SQLQuery sqlQuery=createSQLQuery(sql.toString(), parems);
				sqlQuery.executeUpdate();
			}
		}

	}
	public void updateStatusByPayId(String origBizId) {
		if(!StringUtils.isEmpty(origBizId)){
			StringBuffer sql = new StringBuffer();
			Map<String,Object> parems=new HashMap<>();
			//hmj 未上传的时候，把结算单号置为空值
			sql.append("UPDATE BIS_CHEKING_BOOK set midGroupStatic = :midGroupStatic,statement_No= :statementNo where 1 =1 and codeNum = :codeNum");
//			parems.put("midGroupStatic",NOSTATU);
			parems.put("midGroupStatic",null);
			parems.put("statementNo",null);
			parems.put("codeNum",origBizId);
			SQLQuery sqlQuery=createSQLQuery(sql.toString(), parems);
			sqlQuery.executeUpdate();
		}
	}

    public List<Map<String, Object>> selectAll(List<String> ids) {
		Map<String, Object> queryParams = new HashMap<>();
		String sql = new String();
		if(!CollectionUtils.isEmpty(ids)){
			sql="SELECT codeNum,MIDGROUPSTATIC from BIS_CHEKING_BOOK where 1 =1   ";
			String sqlPing = new String();
			for (int i = 0; i < ids.size(); i++) {
				if(i== ids.size()-1){
					sqlPing+="'"+ids.get(i)+"'";
					break;
				}
				sqlPing+="'"+ids.get(i)+"',";
			}

			sql+=" and codeNum in ("+sqlPing+") ";
		}
		List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);

		return sqlLe;
    }

	public String getBillNum(String payId) {
		Map<String, Object> queryParams = new HashMap<>();
		String arr = new String();
		String sql = new String();
		sql ="SELECT " +
				" bb.standing_num AS standingNum " +
				" from " +
				" BIS_CHEKING_BOOK  " +
				" LEFT JOIN BIS_STANDING_BOOK  bb ON BIS_CHEKING_BOOK.CODENUM = bb.reconcile_num " +
				" WHERE " +
				" 1 = 1 ";
		if(payId != null){
			sql+=" and BIS_CHEKING_BOOK.CODENUM = "+"'"+payId+"'";
		}
		List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);
		for (Map<String, Object> stringObjectMap : sqlLe) {
			Object billnum = stringObjectMap.get("STANDINGNUM");
			if(!StringUtils.isEmpty(billnum)){
				String billNum = billnum.toString();
				arr+=billNum+",";
			}

		}

		return arr;
	}

	public List<String> getBillNums(String payId) {
		Map<String, Object> queryParams = new HashMap<>();
		List<String> strings = new ArrayList<>();
		String arr = new String();
		String sql = new String();
		sql ="SELECT " +
				" bb.standing_num AS standingNum " +
				" from " +
				" BIS_CHEKING_BOOK  " +
				" LEFT JOIN BIS_STANDING_BOOK  bb ON BIS_CHEKING_BOOK.CODENUM = bb.reconcile_num " +
				" WHERE " +
				" 1 = 1 ";
		if(payId != null){
			sql+=" and BIS_CHEKING_BOOK.CODENUM = "+"'"+payId+"'";
		}
		List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);
		for (Map<String, Object> stringObjectMap : sqlLe) {
			String billnum = stringObjectMap.get("STANDINGNUM").toString();
			if(!StringUtils.isEmpty(billnum)){
				strings.add(billnum);
			}

		}

		return strings;
	}

	public void addStatementNoAndCostId(String id,String statementNo) {
		Map<String, Object> queryParams = new HashMap<>();
		String arr = new String();
		String sql = new String();
		sql ="UPDATE BIS_CHEKING_BOOK set BIS_CHEKING_BOOK.STATEMENT_NO =:statementNo WHERE CODENUM = :id  ";
		queryParams.put("statementNo",statementNo);
		queryParams.put("id",id);
		SQLQuery sqlQuery=createSQLQuery(sql, queryParams);
		sqlQuery.executeUpdate();

	}

	public String selectStatementNo(String id) {
		String statementNo = "";
		Map<String, Object> queryParams = new HashMap<>();
		String sql = new String();
		sql ="SELECT BIS_CHEKING_BOOK.STATEMENT_NO FROM BIS_CHEKING_BOOK  WHERE CODENUM = :id  ";

		queryParams.put("id",id);
		List<Map<String, Object>> sqlLe = findSqlOrSelect(sql, queryParams);

		for (Map<String, Object> stringObjectMap : sqlLe) {

			Object statementNoObj = stringObjectMap.get("STATEMENT_NO");
			if(StringUtils.isEmpty(statementNoObj)){
				return "数据库中未查询到改单号";
			}
			statementNo = statementNoObj.toString();
		}
		return  statementNo;
	}
}
