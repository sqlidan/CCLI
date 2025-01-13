package com.haiersoft.ccli.supervision.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.supervision.entity.CopBaseInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class SupervisionDao extends HibernateDao<CopBaseInfo, String> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HashMap> searchStockReport(int aa) {
		List<HashMap> getList = new ArrayList<HashMap>();
		try {
			StringBuffer stringBuffer2 = new StringBuffer();
			stringBuffer2.append("   SELECT");
			stringBuffer2.append("	t1.*, ");
			stringBuffer2.append("  	t2.*,");
			stringBuffer2.append(" (t1.WmsDclQty-t2.grossWeight ) 库中保税数量,");
			stringBuffer2.append("	t2.grossWeight 库中一般贸易数量");
			stringBuffer2.append("  from (");
		
			stringBuffer2.append(" SELECT       ");
			stringBuffer2.append(" B.sku as WmsMtsNo,   ");
			stringBuffer2.append(" B.asn as GoodsMtsNo,");
			stringBuffer2.append(" B.billNum AS billNum,");
			stringBuffer2.append("  SUM( B.jz )  as WmsDclQty,");
			stringBuffer2.append(" B.cargoName   as GoodsName ,");
		//	stringBuffer2.append(" ");
			stringBuffer2.append(" B.IF_BONDED as GoodsType ,");
			stringBuffer2.append(" 	REPLACE ( listagg ( B.areanum, ',' ) WITHIN GROUP ( ORDER BY B.areanum ), ',', '|' ) AS PlaceIds, ");
			stringBuffer2.append(" 	REPLACE ( listagg ( B.locationCode, ',' ) WITHIN GROUP ( ORDER BY B.locationCode ), ',', '|' ) AS LocationIds, ");
			//stringBuffer2.append(" CAST(regexp_replace(( xmlagg( xmlparse ( content B.areanum || '|' wellformed ) ORDER BY B.areanum ).getclobval ()), '([^|]+)(,)+', '' )AS VARCHAR2 ( 255 ) )  as PlaceIds ,");
			//stringBuffer2.append(	" CAST(regexp_replace(( xmlagg( xmlparse ( content B.locationCode || '|' wellformed ) ORDER BY B.locationCode ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 ))as LocationIds, ");
			stringBuffer2.append("  B.hs_code,b.ITEM_NUM as itemnum,b.ACCOUNT_BOOK as EMSSEQNO ,b.TYPE_SIZE as TYPESIZE,SUM( B.dclQTY ) AS dclQTY," +
					" B.dclUnit");
			stringBuffer2.append(" FROM");
			stringBuffer2.append("	(");
			stringBuffer2.append(" SELECT");
			stringBuffer2.append(" A.sku AS sku,");
			stringBuffer2.append(" SUM( A.netWeight ) AS JZ,");
			stringBuffer2.append(" 	A.asn,");
			stringBuffer2.append(" 	A.billNum,");
			stringBuffer2.append(" A.cargoName,");
			//stringBuffer2.append(" ");
			stringBuffer2.append(" A.state,");
			stringBuffer2.append("listagg(A.locationCode,',') WITHIN GROUP (order by A.locationCode )AS locationCode,");
			stringBuffer2.append(" listagg( A.areanum ,',') WITHIN GROUP (order by  A.areanum  ) AS areanum, ");

			//stringBuffer2.append(" to_char (WMSYS.WM_CONCAT ( DISTINCT A.locationCode)) AS locationCode,");
			//stringBuffer2.append(" to_char (WMSYS.WM_CONCAT ( DISTINCT  A.areanum )) AS areanum, ");
			//stringBuffer2.append("	A.locationCode,");
			//stringBuffer2.append("  A.areanum ,  ");
			stringBuffer2.append(" A.IF_BONDED,A.hs_code,a.ITEM_NUM,a.ACCOUNT_BOOK,a.TYPE_SIZE,SUM( A.dclQTY ) dclQTY,A.dclUnit");
			stringBuffer2.append(" FROM");
			stringBuffer2.append(" ( ");
			stringBuffer2.append(" SELECT");
		//	stringBuffer2.append(" t.TRAY_ID AS trayCode,");
			stringBuffer2.append(" t.SKU_ID AS sku ,");
			stringBuffer2.append(" t.BILL_NUM AS billNum,");
			stringBuffer2.append(" info.hs_code  AS asn,");
			stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
			stringBuffer2.append("  t.AREA_NUM AS areanum,");
		//	stringBuffer2.append(" 	WMSYS.WM_CONCAT (DISTINCT t.CARGO_LOCATION) AS locationCode, ");
		//	stringBuffer2.append(" WMSYS.WM_CONCAT (DISTINCT t.AREA_NUM )AS areanum, ");
			
			//stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
			//stringBuffer2.append(" t.AREA_NUM AS areanum,");
			stringBuffer2.append(" t.WAREHOUSE AS warehouse,");
			stringBuffer2.append(" INFO.HS_ITEMNAME AS cargoName,");
		//	stringBuffer2.append(" ");
			stringBuffer2.append(" sum( t.NET_WEIGHT ) AS netWeight,");
		//	stringBuffer2.append(" sum( t.GROSS_WEIGHT ) AS grossWeight,");
			stringBuffer2.append("  t.UNITS AS units,");
			stringBuffer2.append(" t.CARGO_STATE AS state ,");
			stringBuffer2.append(" st.IF_BONDED,info.hs_code,bh.ITEM_NUM,info.ACCOUNT_BOOK,info.TYPE_SIZE,bh.ITEM_NUM as dclUnit,sum( t.NET_WEIGHT ) as dclQTY ");
			stringBuffer2.append(" FROM");
			stringBuffer2.append(" bis_tray_info t");
			stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.CONTACT_NUM = ST.LINK_ID");
			stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM  and ba.CTN_NUM=T.CTN_NUM ");
			stringBuffer2.append(" LEFT JOIN	BASE_SKU_BASE_INFO bak on bak.SKU_ID=t.SKU_ID");
			stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON t.SKU_ID = INFO.sku  AND ST.ITEM_NUM = INFO.ITEM_NUM ");
			stringBuffer2.append(" left JOIN BASE_HSCODE  bh on info.HS_CODE=bh.CODE ");
			stringBuffer2.append(" LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME=INFO.HS_ITEMNAME");
			stringBuffer2.append(" WHERE");
			stringBuffer2.append("	1 = 1 ");
			stringBuffer2.append("	AND t.NOW_PIECE > 0 and st.IF_SORTING='0'   and st.IF_BONDED!='1'	 AND st.AUDITING_STATE = '2' ");//不是分拣已经审核
			stringBuffer2.append(" GROUP BY");
			//stringBuffer2.append(" t.TRAY_ID,");
			stringBuffer2.append("	info.hs_code,");
			stringBuffer2.append("	t.SKU_ID,");
			stringBuffer2.append("	t.BILL_NUM,");
			stringBuffer2.append("	t.WAREHOUSE,");
			stringBuffer2.append("	INFO.HS_ITEMNAME,");
		//	stringBuffer2.append("	");
			stringBuffer2.append(" t.UNITS,");
			stringBuffer2.append(" t.CARGO_STATE,");
			stringBuffer2.append(" t.AREA_NUM,");
			stringBuffer2.append(" t.CARGO_LOCATION ,");
			stringBuffer2.append("	st.IF_BONDED,info.hs_code,bh.ITEM_NUM,info.ACCOUNT_BOOK,info.TYPE_SIZE ");
			stringBuffer2.append(" UNION");
			stringBuffer2.append(" SELECT   t.SKU_ID AS sku,");
			stringBuffer2.append(" t.BILL_NUM AS billNum,");
			stringBuffer2.append(" bai.HS_CODE as asn,");
			stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
			stringBuffer2.append(" t.AREA_NUM AS areanum,");
			stringBuffer2.append(" t.WAREHOUSE AS warehouse,");
			stringBuffer2.append(" bai.HS_ITEMNAME  AS cargoName,");
		//	stringBuffer2.append(" ");
			stringBuffer2.append(" sum( t.NET_WEIGHT ) AS netWeight,");
			stringBuffer2.append(" t.UNITS AS units,");
			stringBuffer2.append(" 	t.CARGO_STATE AS state,");
			stringBuffer2.append("st.IF_BONDED,");
			stringBuffer2.append(" bai.HS_CODE,");
			stringBuffer2.append(" bh.ITEM_NUM,");
			stringBuffer2.append(" bai.ACCOUNT_BOOK,bak.TYPE_SIZE,bh.ITEM_NUM as dclUnit,sum( t.NET_WEIGHT ) as dclQTY");
			stringBuffer2.append("  FROM  bis_tray_info t");
			stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.BILL_NUM = ST.ITEM_NUM");
			stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM  and ba.CTN_NUM=T.CTN_NUM ");
			stringBuffer2.append(" LEFT JOIN BASE_SKU_BASE_INFO bak ON bak.SKU_ID = t.SKU_ID");
			stringBuffer2.append(" left join BIS_ASN_INFO bai on bai.ASN_ID=ba.asn and bai.SKU_ID=t.SKU_ID");
			stringBuffer2.append(" LEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE");
			stringBuffer2.append(" 	LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME = bai.HS_ITEMNAME ");
			stringBuffer2.append(" WHERE 1 = 1 AND t.NOW_PIECE > 0   AND st.IF_BONDED != '1'   and ba.IF_SECOND_ENTER!='1' ");
			//stringBuffer2.append("  AND st.IF_SORTING = '1' AND st.IF_RECORD = '1'");//入库联系单已审核是分拣
			//stringBuffer2.append("	 and   TO_CHAR(t.UPDATE_TIME,'yyyy-MM-dd')='"+yesterdayDate+"'");
			stringBuffer2.append(" GROUP BY   	t.SKU_ID,t.BILL_NUM,");
			stringBuffer2.append("	bai.HS_CODE , 	t.CARGO_LOCATION ,");
			stringBuffer2.append("t.AREA_NUM ,t.WAREHOUSE ,bai.HS_ITEMNAME  , ");
			stringBuffer2.append("t.UNITS ,t.CARGO_STATE ,st.IF_BONDED,");
			stringBuffer2.append("bai.HS_CODE, bh.ITEM_NUM,	bai.ACCOUNT_BOOK,bak.TYPE_SIZE");			
			//stringBuffer2.append("  and t.CARGO_STATE='01'");状态为已上架
			stringBuffer2.append("  UNION  SELECT " +
					" id AS sku, " +
					" BILL_NUM AS billNum, " +
					" HS_CODE AS asn, " +
					" CARGO_LOCATION AS locationCode, " +
					" CARGO_AREA AS areanum, " +
					" '怡之航' AS warehouse, " +
					" HS_ITEMNAME AS cargoName, " +
					" sum( HS_QTY ) AS netWeight, " +
					" '1' AS units, " +
					"  '00' AS state, " +
					" '1' AS IF_BONDED, " +
					" HS_CODE, " +
					" '035' AS ITEM_NUM, " +
					" ACCOUNT_BOOK, " +
					" '' AS TYPE_SIZE ,DCL_UNIT as dclUnit,DCL_QTY as dclQTY " +
					" FROM " +
					" BASE_BOUNDED  " +
					" GROUP BY " +
					" id, " +
					" BILL_NUM, " +
					" HS_CODE, " +
					" CARGO_LOCATION, " +
					" CARGO_AREA, " +
					" HS_ITEMNAME, " +
					" ACCOUNT_BOOK  , DCL_UNIT ,DCL_QTY   ");
			stringBuffer2.append("	) A ");
			stringBuffer2.append("  GROUP BY");
			stringBuffer2.append("  A.sku,");
			stringBuffer2.append("	 A.asn,");
			stringBuffer2.append("	 A.billNum,");
			stringBuffer2.append("	 A.cargoName,");
			stringBuffer2.append("	");
			stringBuffer2.append("	A.state,");
			stringBuffer2.append("	A.locationCode,");
			stringBuffer2.append("  A.areanum ,");
			stringBuffer2.append(" 	A.IF_BONDED,A.hs_code,a.ITEM_NUM,a.ACCOUNT_BOOK,a.TYPE_SIZE,a.dclUnit");
			stringBuffer2.append("	) B");
			stringBuffer2.append("  GROUP BY");
			stringBuffer2.append("  B.sku,");
			stringBuffer2.append("  B.asn,");
			stringBuffer2.append("  B.billNum,");
			stringBuffer2.append("  B.cargoName,");
			stringBuffer2.append("   ");
			stringBuffer2.append("	B.IF_BONDED,B.hs_code,b.ITEM_NUM,b.ACCOUNT_BOOK ,b.TYPE_SIZE,B.dclUnit)t1");
		
            
			stringBuffer2.append(" 	left   join (");
			
			stringBuffer2.append(" SELECT  ");
			stringBuffer2.append(" A.SKU_ID,");
			stringBuffer2.append(" a.ASN,");
             stringBuffer2.append(" 	CAST(regexp_replace(( xmlagg( xmlparse ( content A.locationCode || '|' wellformed ) ORDER BY A.locationCode ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 )) AS locationCode,");
			stringBuffer2.append(" CAST(regexp_replace(( xmlagg( xmlparse ( content A.areanum || '|' wellformed ) ORDER BY A.areanum ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 )) AS areanum,");
			stringBuffer2.append(" SUM(A.grossWeight) AS grossWeight");
			stringBuffer2.append(" from ( SELECT  DISTINCT");
			stringBuffer2.append("  t.SKU_ID ,");
			stringBuffer2.append("  t.ASN,");
			stringBuffer2.append(" 	t.CARGO_LOCATION AS locationCode,");
			stringBuffer2.append(" t.AREA_NUM AS areanum,");
			stringBuffer2.append(" t.NET_WEIGHT 	AS grossWeight");
			stringBuffer2.append(" FROM");
			stringBuffer2.append(" bis_tray_info t");
			stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.CONTACT_NUM = ST.LINK_ID");
			stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM ");
			stringBuffer2.append(" LEFT JOIN	BASE_SKU_BASE_INFO bak on bak.SKU_ID=t.SKU_ID");
			stringBuffer2.append(" 	LEFT JOIN BIS_ENTER_STOCK_INFO info ON t.SKU_ID = INFO.SKU AND ST.ITEM_NUM = INFO.ITEM_NUM ");
			stringBuffer2.append(" left JOIN BASE_HSCODE  bh on info.HS_CODE=bh.CODE ");
			stringBuffer2.append(" LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME=INFO.HS_ITEMNAME");
			stringBuffer2.append(" WHERE");
			stringBuffer2.append("	1 = 1 ");
			stringBuffer2.append("	AND t.NOW_PIECE >= 0 	and t.IS_BONDED is not  null  )a ");
			stringBuffer2.append(" GROUP BY A.SKU_ID, a.ASN)t2 ");
			stringBuffer2.append(" on   t2.SKU_ID=t1.WmsMtsNo and t2.asn=t1.GoodsMtsNo");
	        stringBuffer2.append(" where t1.wmsdclqty>0 and t1.LocationIds IS NOT NULL and t1.goodsname is not null  and ((t1.goodstype='1' and t1.emsseqno is not null)or t1.goodstype = '0')");
			Map<String, Object> params = new HashMap<String, Object>();
			SQLQuery sqlQuery = createSQLQuery(stringBuffer2.toString(), params);
			SQLQuery sqlQuery2=this.getSession().createSQLQuery(stringBuffer2.toString());
			List<Object[]> datas = sqlQuery2.list();
			for (Object[] ob:datas){
				HashMap data = new HashMap();
				data.put("WMSMTSNO", ob[0]==null?"":ob[0].toString());
				data.put("GOODSMTSNO", ob[1]==null?"":ob[1].toString());
				data.put("WMSDCLQTY", ob[2]==null?"":ob[2].toString());
				data.put("GOODSNAME", ob[3]==null?"":ob[3].toString());
		//		data.put("CARGOTYPE", ob[4]==null?"":ob[4].toString());
				//data.put("CARGONAME", ob[5]==null?"":ob[5].toString());
				data.put("GOODSTYPE", ob[4]==null?"":ob[4].toString());
				data.put("PLACEIDS", ob[5]==null?"":ob[5].toString());
				data.put("LOCATIONIDS", ob[6]==null?"":ob[6].toString());
				data.put("hscode", ob[7]==null?"":ob[7].toString());
				data.put("itemnum",ob[8]==null?"035":ob[8].toString());
				data.put("EMSSEQNO", ob[9]==null?"":ob[9].toString());
				data.put("TYPESIZE", ob[10]==null?"":ob[10].toString());
				data.put("DCLQTY", ob[11]==null?"":ob[11].toString());
				data.put("DCLUNIT", ob[12]==null?"035":ob[12].toString());
//				data.put("ZKPLACEIDS", ob[13]==null?"":ob[13].toString());
//				data.put("ZKLOCATIONIDS", ob[14]==null?"":ob[14].toString());
//				data.put("DCLQTY", ob[11]==null?"":ob[11].toString());
//				data.put("DCLUNIT", ob[12]==null?"":ob[12].toString());
//				data.put("ZKTRADE", ob[16]==null?"":ob[16].toString());//在库保税
//				data.put("ZKFORWARD", ob[17]==null?"":ob[17].toString());//在库一般贸易
				getList.add(data);
				
				
				/////1111111
			}
			
		//	getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getList;

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HashMap> UpdateStockReport(int aa) {
		List<HashMap> getList = new ArrayList<HashMap>();
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,-24);
		String yesterdayDate=dateFormat.format(calendar.getTime());
		System.out.println(yesterdayDate);
		try {
				StringBuffer stringBuffer2 = new StringBuffer();
				stringBuffer2.append("   SELECT");
				stringBuffer2.append("	t1.*, ");
				stringBuffer2.append("  	t2.*,");
				stringBuffer2.append(" (t1.WmsDclQty-t2.grossWeight ) 库中保税数量,");
				stringBuffer2.append("	t2.grossWeight 库中一般贸易数量");
				stringBuffer2.append("  from (");
			
				stringBuffer2.append(" SELECT       ");
				stringBuffer2.append(" B.sku as WmsMtsNo,   ");
				stringBuffer2.append(" B.asn as GoodsMtsNo,");
				stringBuffer2.append("  SUM( B.jz )  as WmsDclQty,");
				stringBuffer2.append(" B.cargoName   as GoodsName ,");
				stringBuffer2.append(" ");
				stringBuffer2.append(" B.IF_BONDED as GoodsType ,");
				stringBuffer2.append(" 	REPLACE ( listagg ( B.areanum, ',' ) WITHIN GROUP ( ORDER BY B.areanum ), ',', '|' ) AS PlaceIds, ");
				stringBuffer2.append(" 	REPLACE ( listagg ( B.locationCode, ',' ) WITHIN GROUP ( ORDER BY B.locationCode ), ',', '|' ) AS LocationIds, ");
				//stringBuffer2.append(" CAST(regexp_replace(( xmlagg( xmlparse ( content B.areanum || '|' wellformed ) ORDER BY B.areanum ).getclobval ()), '([^|]+)(,)+', '' )AS VARCHAR2 ( 255 ) )  as PlaceIds ,");
				//stringBuffer2.append(	" CAST(regexp_replace(( xmlagg( xmlparse ( content B.locationCode || '|' wellformed ) ORDER BY B.locationCode ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 ))as LocationIds, ");
				stringBuffer2.append("  B.hs_code,b.ITEM_NUM as itemnum,b.ACCOUNT_BOOK as EMSSEQNO,b.TYPE_SIZE as TYPESIZE");
				stringBuffer2.append(" FROM");
				stringBuffer2.append("	(");
				stringBuffer2.append(" SELECT");
				stringBuffer2.append(" 	A.sku,");
				stringBuffer2.append(" SUM( A.netWeight ) AS JZ,");
				stringBuffer2.append(" 	A.asn,");
				stringBuffer2.append(" A.cargoName,");
				stringBuffer2.append(" ");
				stringBuffer2.append(" A.state,");
				stringBuffer2.append("listagg(A.locationCode,',') WITHIN GROUP (order by A.locationCode ) AS locationCode,");
				stringBuffer2.append("listagg(  A.areanum ,',') WITHIN GROUP (order by   A.areanum  ) AS areanum, ");

				//stringBuffer2.append("to_char ( WMSYS.WM_CONCAT ( DISTINCT A.locationCode)) AS locationCode,");
				//stringBuffer2.append("to_char ( WMSYS.WM_CONCAT ( DISTINCT  A.areanum )) AS areanum, ");
				//stringBuffer2.append("	A.locationCode,");
				//stringBuffer2.append("  A.areanum ,  ");
				stringBuffer2.append(" A.IF_BONDED,A.hs_code,a.ITEM_NUM,a.ACCOUNT_BOOK,a.TYPE_SIZE");
				stringBuffer2.append(" FROM");
				stringBuffer2.append(" ( ");
				stringBuffer2.append(" SELECT");
			//	stringBuffer2.append(" t.TRAY_ID AS trayCode,");
				stringBuffer2.append(" t.SKU_ID AS sku,");
				stringBuffer2.append(" t.ASN AS asn,");
				stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
				stringBuffer2.append("  t.AREA_NUM AS areanum,");
			//	stringBuffer2.append(" 	WMSYS.WM_CONCAT (DISTINCT t.CARGO_LOCATION) AS locationCode, ");
			//	stringBuffer2.append(" WMSYS.WM_CONCAT (DISTINCT t.AREA_NUM )AS areanum, ");
				
				//stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
				//stringBuffer2.append(" t.AREA_NUM AS areanum,");
				stringBuffer2.append(" t.WAREHOUSE AS warehouse,");
				stringBuffer2.append(" INFO.HS_ITEMNAME AS cargoName,");
				stringBuffer2.append("");
				stringBuffer2.append(" sum( t.NET_WEIGHT ) AS netWeight,");
			//	stringBuffer2.append(" sum( t.GROSS_WEIGHT ) AS grossWeight,");
				stringBuffer2.append("  t.UNITS AS units,");
				stringBuffer2.append(" t.CARGO_STATE AS state ,");
				stringBuffer2.append(" st.IF_BONDED,info.hs_code,bh.ITEM_NUM,info.ACCOUNT_BOOK,info.TYPE_SIZE");
				stringBuffer2.append(" FROM");
				stringBuffer2.append(" bis_tray_info t");
				stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.CONTACT_NUM = ST.LINK_ID");
				stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM ");
				stringBuffer2.append(" LEFT JOIN	BASE_SKU_BASE_INFO bak on bak.SKU_ID=t.SKU_ID");
				stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON t.SKU_ID = INFO.sku  AND ST.ITEM_NUM = INFO.ITEM_NUM ");
				stringBuffer2.append(" left JOIN BASE_HSCODE  bh on info.HS_CODE=bh.CODE ");
				stringBuffer2.append(" LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME=INFO.HS_ITEMNAME");
				stringBuffer2.append(" WHERE");
				stringBuffer2.append("	1 = 1 ");
				stringBuffer2.append("	 and   TO_CHAR(t.UPDATE_TIME,'yyyy-MM-dd')='"+"2020-04-11"+"'");
				//stringBuffer2.append("  and t.CARGO_STATE='01'");状态为已上架
				stringBuffer2.append(" GROUP BY");
				//stringBuffer2.append(" t.TRAY_ID,");
				stringBuffer2.append("	t.ASN,");
				stringBuffer2.append("	t.SKU_ID,");
				stringBuffer2.append("	t.WAREHOUSE,");
				stringBuffer2.append("	INFO.HS_ITEMNAME,");
				stringBuffer2.append("	t.CARGO_TYPE,");
				stringBuffer2.append(" t.UNITS,");
				stringBuffer2.append(" t.CARGO_STATE,");
				stringBuffer2.append(" t.AREA_NUM,");
				stringBuffer2.append(" t.CARGO_LOCATION ,");
				stringBuffer2.append("	st.IF_BONDED,info.hs_code,bh.ITEM_NUM,info.ACCOUNT_BOOK,info.TYPE_SIZE");
				stringBuffer2.append("  UNION");
				stringBuffer2.append(" SELECT   t.SKU_ID AS sku,");
				stringBuffer2.append(" bai.HS_CODE as asn,");
				stringBuffer2.append(" t.CARGO_LOCATION AS locationCode,");
				stringBuffer2.append(" t.AREA_NUM AS areanum,");
				stringBuffer2.append(" t.WAREHOUSE AS warehouse,");
				stringBuffer2.append(" bai.HS_ITEMNAME  AS cargoName,");
				stringBuffer2.append("");
				stringBuffer2.append(" sum( t.NET_WEIGHT ) AS netWeight,");
				stringBuffer2.append(" t.UNITS AS units,");
				stringBuffer2.append(" 	t.CARGO_STATE AS state,");
				stringBuffer2.append("st.IF_BONDED,");
				stringBuffer2.append(" bai.HS_CODE,");
				stringBuffer2.append(" bh.ITEM_NUM,");
				stringBuffer2.append(" bai.ACCOUNT_BOOK,bak.TYPE_SIZE");
				stringBuffer2.append("  FROM  bis_tray_info t");
				stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.BILL_NUM = ST.ITEM_NUM");
				stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM and ba.IF_SECOND_ENTER!='1'");
				stringBuffer2.append(" LEFT JOIN BASE_SKU_BASE_INFO bak ON bak.SKU_ID = t.SKU_ID");
				stringBuffer2.append(" left join BIS_ASN_INFO bai on bai.ASN_ID=ba.asn and bai.SKU_ID=t.SKU_ID");
				stringBuffer2.append(" LEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE");
				stringBuffer2.append(" 	LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME = bai.HS_ITEMNAME ");
				stringBuffer2.append(" WHERE 1 = 1 AND t.NOW_PIECE > 0 AND st.IF_SORTING = '1' AND st.IF_RECORD = '1' ");
				stringBuffer2.append("	 and   TO_CHAR(t.UPDATE_TIME,'yyyy-MM-dd')='"+"2020-04-11"+"'");
				stringBuffer2.append(" GROUP BY   	t.SKU_ID ,");
				stringBuffer2.append("	bai.HS_CODE , 	t.CARGO_LOCATION ,");
				stringBuffer2.append("t.AREA_NUM ,t.WAREHOUSE ,bai.HS_ITEMNAME  , ");
				stringBuffer2.append("t.CARGO_TYPE , t.UNITS ,t.CARGO_STATE ,st.IF_BONDED,");
				stringBuffer2.append("bai.HS_CODE, bh.ITEM_NUM,	bai.ACCOUNT_BOOK,bak.TYPE_SIZE");
			
				stringBuffer2.append("	) A ");
				stringBuffer2.append("  GROUP BY");
				stringBuffer2.append("  A.sku,");
				stringBuffer2.append("	 A.asn,");
				stringBuffer2.append("	 A.cargoName,");
				stringBuffer2.append("	");
				stringBuffer2.append("	A.state,");
				stringBuffer2.append("	A.locationCode,");
				stringBuffer2.append("  A.areanum ,");
				stringBuffer2.append(" 	A.IF_BONDED,A.hs_code,a.ITEM_NUM,a.ACCOUNT_BOOK,a.TYPE_SIZE");
				stringBuffer2.append("	) B");
				stringBuffer2.append("  GROUP BY");
				stringBuffer2.append("  B.sku,");
				stringBuffer2.append("  B.asn,");
				stringBuffer2.append("  B.cargoName,");
				stringBuffer2.append("   ");
				stringBuffer2.append("	B.IF_BONDED,B.hs_code,b.ITEM_NUM,b.ACCOUNT_BOOK,b.TYPE_SIZE )t1");
			
	            
				stringBuffer2.append(" 	left   join (");
				
				stringBuffer2.append(" SELECT  ");
				stringBuffer2.append(" A.SKU_ID,");
				stringBuffer2.append(" a.ASN,");
	             stringBuffer2.append(" 	CAST(regexp_replace(( xmlagg( xmlparse ( content A.locationCode || '|' wellformed ) ORDER BY A.locationCode ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 )) AS locationCode,");
				stringBuffer2.append(" CAST(regexp_replace(( xmlagg( xmlparse ( content A.areanum || '|' wellformed ) ORDER BY A.areanum ).getclobval ()), '([^|]+)(,)+', '' ) AS VARCHAR2 ( 255 )) AS areanum,");
				stringBuffer2.append(" SUM(A.grossWeight) AS grossWeight");
				stringBuffer2.append(" from ( SELECT  DISTINCT");
				stringBuffer2.append("  t.SKU_ID ,");
				stringBuffer2.append("  t.ASN,");
				stringBuffer2.append(" 	t.CARGO_LOCATION AS locationCode,");
				stringBuffer2.append(" t.AREA_NUM AS areanum,");
				stringBuffer2.append(" t.NET_WEIGHT 	AS grossWeight");
				stringBuffer2.append(" FROM");
				stringBuffer2.append(" bis_tray_info t");
				stringBuffer2.append(" LEFT JOIN BIS_ENTER_STOCK st ON T.CONTACT_NUM = ST.LINK_ID");
				stringBuffer2.append(" LEFT JOIN BIS_ASN ba ON ba.BILL_NUM = t.BILL_NUM ");
				stringBuffer2.append(" LEFT JOIN	BASE_SKU_BASE_INFO bak on bak.SKU_ID=t.SKU_ID");
				stringBuffer2.append(" 	LEFT JOIN BIS_ENTER_STOCK_INFO info ON t.SKU_ID = INFO.SKU AND ST.ITEM_NUM = INFO.ITEM_NUM");
				stringBuffer2.append(" left JOIN BASE_HSCODE  bh on info.HS_CODE=bh.CODE ");
				stringBuffer2.append(" LEFT JOIN BASE_ITEMNAME BI ON BI.CARGO_NAME=INFO.HS_ITEMNAME");
				stringBuffer2.append(" WHERE");
				stringBuffer2.append("	1 = 1 ");
				stringBuffer2.append("		and t.IS_BONDED is not  null and  TO_CHAR(t.UPDATE_TIME,'yyyy-MM-dd')='"+"2020-04-11"+ "')a");
				stringBuffer2.append(" GROUP BY A.SKU_ID, a.ASN)t2 ");
				stringBuffer2.append(" on   t2.SKU_ID=t1.WmsMtsNo and t2.asn=t1.GoodsMtsNo");
		        stringBuffer2.append("  where t1.goodsname is not null  and ((t1.goodstype='1' and t1.emsseqno is not null)  or t1.goodstype='0')");
				Map<String, Object> params = new HashMap<String, Object>();
				SQLQuery sqlQuery = createSQLQuery(stringBuffer2.toString(), params);
				SQLQuery sqlQuery2=this.getSession().createSQLQuery(stringBuffer2.toString());
				List<Object[]> datas = sqlQuery2.list();
				for (Object[] ob:datas){
					HashMap data = new HashMap();
					data.put("WMSMTSNO", ob[0]==null?"":ob[0].toString());
					data.put("GOODSMTSNO", ob[1]==null?"":ob[1].toString());
					data.put("WMSDCLQTY", ob[2]==null?"":ob[2].toString());
					data.put("GOODSNAME", ob[3]==null?"":ob[3].toString());
					data.put("CARGOTYPE", ob[4]==null?"":ob[4].toString());
					//data.put("CARGONAME", ob[5]==null?"":ob[5].toString());
					data.put("GOODSTYPE", ob[5]==null?"":ob[5].toString());
					data.put("PLACEIDS", ob[6]==null?"":ob[6].toString());
					data.put("LOCATIONIDS", ob[7]==null?"":ob[7].toString());
					data.put("hscode", ob[8]==null?"":ob[8].toString());
					data.put("itemnum",ob[9]==null?"035":ob[9].toString());
					data.put("EMSSEQNO", ob[10]==null?"":ob[10].toString());
					data.put("TYPESIZE", ob[11]==null?"":ob[11].toString());
					data.put("ZKPLACEIDS", ob[14]==null?"":ob[14].toString());
					data.put("ZKLOCATIONIDS", ob[15]==null?"":ob[15].toString());				
					data.put("ZKTRADE", ob[17]==null?"":ob[17].toString());//在库保税
					data.put("ZKFORWARD", ob[18]==null?"":ob[18].toString());//在库一般贸易
					getList.add(data);
				}
				
			//	getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();


			return getList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getList;

	}

	public List<HashMap> declarationMessage(int aa) {
		
		List<HashMap> getList = new ArrayList<HashMap>();
        StringBuffer stringBuffer= new StringBuffer();
        stringBuffer.append(" SELECT ");
        stringBuffer.append(" listagg (b.billCode , ',') WITHIN GROUP (ORDER BY b.billCode ) billCode ,  ");
        stringBuffer.append(" listagg (b.bgdh , ',') WITHIN GROUP (ORDER BY b.bgdh ) bgdh , ");
        stringBuffer.append(" B.bigName,");
        stringBuffer.append(" B.simName, ");
        //stringBuffer.append(" B.rkTime, ");
        stringBuffer.append(" SUM(B.allnet) as allnet, ");
        stringBuffer.append(" 	B.hsCode, ");
        stringBuffer.append(" B.hsItemname");
        stringBuffer.append(" FROM( ");
        stringBuffer.append(" SELECT  listagg(A.billCode,',') WITHIN GROUP (order by A.billCode) AS billCode ,");
        stringBuffer.append("  listagg(A.bgdh,',') WITHIN GROUP (order by A.bgdh ) AS bgdh ,");
       
        //stringBuffer.append(" SELECT  to_char (WMSYS.WM_CONCAT ( DISTINCT A.billCode )) AS billCode ,");
       // stringBuffer.append("  to_char (WMSYS.WM_CONCAT ( DISTINCT A.bgdh )) AS bgdh ,");
        stringBuffer.append("  A.bigName,");
        stringBuffer.append(" A.simName,");
        stringBuffer.append(" A.rkTime,");
        stringBuffer.append("  SUM(A.allnet) as allnet,");
        stringBuffer.append("  A.hsCode,");
        stringBuffer.append("  A.hsItemname");
        stringBuffer.append(" FROM( ");
        stringBuffer.append(" SELECT ");
        stringBuffer.append(" tray.bill_num AS billCode, ");
        stringBuffer.append(" nvl( info.bgdh, st.bgdh ) AS bgdh,");
        stringBuffer.append(" s.type_name AS bigName, ");
        stringBuffer.append(" s.class_name AS simName, ");
        stringBuffer.append(" to_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ) AS rkTime, ");
        stringBuffer.append(" SUM( tray.net_weight ) AS allnet, ");
        stringBuffer.append(" nvl( info.HS_CODE,  bai.HS_CODE )AS hsCode, ");
        stringBuffer.append(" nvl( info.HS_ITEMNAME, bai.HS_ITEMNAME )AS hsItemname");
        stringBuffer.append(" FROM ");
        stringBuffer.append(" BIS_TRAY_INFO tray ");
        stringBuffer.append(" LEFT JOIN BIS_ENTER_STOCK st ON ( tray.bill_num = st.ITEM_NUM AND tray.CONTACT_NUM = st.LINK_ID ) ");
        stringBuffer.append(" 	LEFT JOIN BIS_ENTER_STOCK_INFO info ON (st.ITEM_NUM = info.ITEM_NUM AND st.LINK_ID = info.LINK_ID AND tray.ctn_num = info.ctn_num AND tray.sku_id = info.sku ) ");
        stringBuffer.append(" LEFT JOIN BIS_ASN ba ON ba.ASN = tray.asn  ");
        stringBuffer.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id ");
        stringBuffer.append(" LEFT JOIN BIS_ASN_INFO bai ON bai.ASN_ID = ba.asn  AND bai.SKU_ID = tray.SKU_ID");
        stringBuffer.append(" LEFT JOIN BASE_HSCODE bh ON bai.HS_CODE = bh.CODE ");
        stringBuffer.append(" WHERE 1 = 1 AND ( tray.cargo_state = '01' OR tray.cargo_state = '10' ) AND tray.now_piece != 0 ");
        stringBuffer.append(" GROUP BY 	tray.bill_num,s.type_name, 	s.class_name,to_char( tray.ENTER_STOCK_TIME, 'yyyy-mm-dd' ),");
        stringBuffer.append(" info.bgdh,	st.bgdh,info.HS_CODE,info.HS_ITEMNAME,bai.HS_CODE,	bai.HS_ITEMNAME)A");
        stringBuffer.append(" GROUP BY  A.bigName,A.simName,A.rkTime,	A.hsCode,	A.hsItemname)B");
        stringBuffer.append(" WHERE B.hsItemname is not null  and (B.bigName='肉类') ");
        stringBuffer.append(" GROUP BY B.bigName, 	B.simName,		B.hsCode,	B.hsItemname");
       // stringBuffer.append(" ORDER BY 	B.rkTime ");      
        
		
		Map<String, Object> params = new HashMap<String, Object>();
		SQLQuery sqlQuery = createSQLQuery(stringBuffer.toString(), params);
		SQLQuery sqlQuery2=this.getSession().createSQLQuery(stringBuffer.toString());
		List<Object[]> datas = sqlQuery2.list();
		for (Object[] ob:datas){
			HashMap data = new HashMap();
			data.put("BillNo", ob[0]==null?"":ob[0].toString());
			data.put("EntryId", ob[1]==null?"":ob[1].toString());
			data.put("GoodsClassify", ob[2]==null?"":ob[2].toString());
			data.put("GoodsSubclass", ob[3]==null?"":ob[3].toString());
		//	data.put("EntranceDate", ob[4]==null?"":ob[4].toString());
			data.put("Quantity", ob[4]==null?"":ob[4].toString());
			data.put("GoodsCode", ob[5]==null?"":ob[5].toString());
			data.put("GoodsName", ob[6]==null?"":ob[6].toString());		
			getList.add(data);
		}
		
		return getList;
	}
	
	
	
	
}
