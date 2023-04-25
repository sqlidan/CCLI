package com.haiersoft.ccli.outsidequery.dao;

import com.haiersoft.ccli.api.entity.InStockInfo;
import com.haiersoft.ccli.api.entity.InStockInfoSort;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.outsidequery.entity.OutsideQuery;
import com.haiersoft.ccli.report.entity.Stock;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class OutsideQueryDao extends HibernateDao<OutsideQuery, String> {

	/**
	 * 库存查询
	 */

	public List<OutsideQuery> getStocks(Map map) {
		Map<String,Object> params = new HashMap<String, Object>();


		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT                                                                 ");
		//sb.append("			tray.stock_in AS clientId,                                           ");
		sb.append("			tray.stock_name AS clientName,                                       ");
/*		sb.append("			(                                                                    ");
		sb.append("				CASE st.IF_BONDED                                                  ");
		sb.append("				WHEN '1' THEN                                                      ");
		sb.append("					'1'                                                              ");
		sb.append("				ELSE                                                               ");
		sb.append("					'0'                                                              ");
		sb.append("				END                                                                ");
		sb.append("			) AS isBonded,                                                       ");*/
	//	sb.append("			tray.bill_num AS billCode,                                           ");
		sb.append("			tray.ctn_num AS ctnNum,                                              ");
	//	sb.append("			tray.sku_id AS sku,                                                  ");
		sb.append("         tray.CARGO_NAME AS cargoName,                                       ");
		sb.append("			tray.rkTime as operationTime,                                                         ");
	//	sb.append("			tray.enter_state AS state,                                           ");
		sb.append("			SUM (tray.num) AS pieces,                                            ");
		sb.append("			SUM (tray.net_weight) AS allnet,                                     ");
		sb.append("			SUM (tray.gross_weight) AS allgross,                                 ");
		//sb.append("         max(s.net_single) AS netWeight,                                     ");
		//sb.append("			max(s.gross_single) AS grossWeight,                                  ");
		sb.append("         s.class_name AS simName                                            ");
	//	sb.append("			s.class_type AS simType                                             ");
		sb.append("		FROM                                                                     ");
		sb.append("			(                                                                    ");
		sb.append("				SELECT                                                             ");
		sb.append("					tray.stock_in,                                                   ");
		sb.append("					tray.stock_name,                                                 ");
		sb.append("					tray.bill_num,                                                   ");
		sb.append("					tray.ctn_num,                                                    ");
		sb.append("					tray.sku_id,                                                     ");
		sb.append("					SUM (tray.now_piece) AS num,                                     ");
		sb.append("					(                                                                ");
		sb.append("						CASE tray.enter_state                                          ");
		sb.append("						WHEN '0' THEN                                                  ");
		sb.append("							'INTACT'                                                     ");
		sb.append("						WHEN '1' THEN                                                  ");
		sb.append("							'BROKEN'                                                     ");
		sb.append("						WHEN '2' THEN                                                  ");
		sb.append("							'COVER TORN'                                                 ");
		sb.append("						END                                                            ");
		sb.append("					) AS enter_state,                                                ");
		sb.append("					tray.CARGO_NAME,                                                 ");
		sb.append("					SUM (tray.net_weight) AS net_weight,                             ");
		sb.append("					SUM (tray.gross_weight) AS gross_weight,                         ");
		sb.append("					tray.CONTACT_NUM,                                                ");
		sb.append("					TO_CHAR (                                                        ");
		sb.append("						tray.ENTER_STOCK_TIME,                                         ");
		sb.append("						'yyyy-MM-dd'                                                   ");
		sb.append("					) AS rkTime,                                                     ");
		sb.append("					tray.cargo_location                                              ");
		sb.append("				FROM                                                               ");
		sb.append("					BIS_TRAY_INFO tray                                               ");
		sb.append("				WHERE                                                              ");
		sb.append("					1 = 1                                                            ");
		sb.append("				AND (                                                              ");
		sb.append("					tray.cargo_state = '01'                                          ");
		sb.append("					OR tray.cargo_state = '10'                                       ");
		sb.append("				)                                                                  ");
		sb.append("				AND tray.now_piece != 0                                            ");
		sb.append("				GROUP BY                                                           ");
		sb.append("					tray.stock_in,                                                   ");
		sb.append("					tray.stock_name,                                                 ");
		sb.append("					tray.bill_num,                                                   ");
		sb.append("					tray.ctn_num,                                                    ");
		sb.append("					tray.sku_id,                                                     ");
		sb.append("					tray.enter_state,                                                ");
		sb.append("					tray.CARGO_NAME,                                                 ");
		sb.append("					tray.CONTACT_NUM,                                                ");
		sb.append("					tray.enter_state,                                                ");
		sb.append("					TO_CHAR (                                                        ");
		sb.append("						tray.ENTER_STOCK_TIME,                                         ");
		sb.append("						'yyyy-MM-dd'                                                   ");
		sb.append("					),                                                               ");
		sb.append("					tray.cargo_location                                              ");
		sb.append("			) tray                                                               ");
		sb.append("		LEFT JOIN BIS_ENTER_STOCK st ON (                                      ");
		sb.append("			tray.bill_num = st.ITEM_NUM                                          ");
		sb.append("			AND tray.CONTACT_NUM = st.LINK_ID                                    ");
		sb.append("		)                                                                      ");
		sb.append("		LEFT JOIN BIS_ENTER_STOCK_INFO info ON (                               ");
		sb.append("			st.ITEM_NUM = info.ITEM_NUM                                          ");
		sb.append("			AND st.LINK_ID = info.LINK_ID                                        ");
		sb.append("			AND tray.ctn_num = info.ctn_num                                      ");
		sb.append("			AND tray.sku_id = info.sku                                           ");
		sb.append("		)                                                                      ");
		sb.append("		LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id               ");
		sb.append("		WHERE                                                                  ");
		sb.append("			1 = 1                                                                ");

		if(!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("startTime")))){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')>= to_date(:startTime, 'yyyy-mm-dd') ");
			params.put("startTime",map.get("startTime"));
		}
		if(!StringUtils.isNull(String.valueOf(map.get("endTime")))){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')< to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime",map.get("endTime"));
		}
		if(!StringUtils.isNull(String.valueOf(map.get("defaultTime")))){
		sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')= trunc(sysdate) ");
		}

		sb.append("		GROUP BY                                                               ");
		sb.append("			tray.stock_in,                                                       ");
		sb.append("			tray.stock_name,                                                     ");
		sb.append("			st.IF_BONDED,                                                        ");
		sb.append("			tray.bill_num,                                                       ");
		sb.append("			tray.ctn_num,                                                        ");
		sb.append("			tray.sku_id,                                                         ");
		sb.append("			s.class_name,                                                        ");
		sb.append("			s.class_type,                                                        ");
		sb.append("			tray.CARGO_NAME,                                                     ");
		sb.append("			tray.rkTime,                                                         ");
		sb.append("			tray.enter_state                                                     ");

		sb.append("		ORDER BY                                                                 ");
		sb.append("			tray.bill_num,                                                       ");
		sb.append("			tray.ctn_num,                                                        ");
		sb.append("			tray.rkTime                                                          ");
		Map<String, Object> paramType = new HashMap<>();
		paramType.put("clientName", String.class);
		//paramType.put("clientId", String.class);
	//	paramType.put("isBonded", String.class);
		//paramType.put("billCode", String.class);
		//paramType.put("ctnNum", String.class);
		//paramType.put("sku", String.class);
		paramType.put("cargoName", String.class);

		paramType.put("operationTime",String.class);
		//paramType.put("state",String.class);
		paramType.put("pieces",Integer.class);
		paramType.put("allnet",Double.class);
		paramType.put("allgross",Double.class);
		paramType.put("simName", String.class);
		//paramType.put("netWeight", Double.class);
		//paramType.put("grossWeight", Double.class);
		return findSql(sb.toString(), paramType, params);

	}

	/**
	 * 入库查询
	 */

	public  List<OutsideQuery> getEnterStockStocks(Map map) {
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> parme = new HashMap<String, Object>();
		String billCode = "";

		sb.append("SELECT  ");
		sb.append("	c.client_name AS clientName,  ");
		sb.append("	aa.stock_in AS clientId,      ");
		sb.append("	aa.bill_num AS billCode,      ");
		sb.append("	aa.ctn_num AS ctnNum,         ");
		sb.append("	aa.sku_id AS sku,             ");
		sb.append("	s.cargo_name AS cargoName,    ");
		sb.append("	aa.inbound_date AS operationTime, ");
		sb.append("         s.class_name AS simName ,  ");

		sb.append("	aa.enter_state AS state,      ");
		sb.append("	aa.ruku_piece_sum AS pieces,  ");
		sb.append("	aa.ruku_piece_sum * s.net_single AS allnet,    ");
		sb.append("	aa.ruku_piece_sum * s.gross_single AS allgross,");
		sb.append("	aa.is_bonded AS isBonded ");
		sb.append("FROM( ");
		sb.append("	SELECT  ");
		sb.append("	a.inbound_date, ");
		sb.append("	a.is_bonded,");
		sb.append("	t.stock_in,");
		sb.append("	t.bill_num,");
		sb.append("	t.ctn_num, ");
		sb.append("	t.sku_id,");
		sb.append("	(  ");
		sb.append("	CASE t.enter_state ");
		sb.append("	WHEN '0' THEN ");
		sb.append("	'INTACT'      ");
		sb.append("	WHEN '1' THEN ");
		sb.append("	'BROKEN'     ");
		sb.append("	WHEN '2' THEN ");
		sb.append("	'COVER TORN'  ");
		sb.append("	END         ");
		sb.append("	) AS enter_state, ");
		sb.append("	sum( ");
		sb.append("	t.original_piece - t.remove_piece ");
		sb.append("	) AS ruku_piece_sum  ");
		sb.append("	FROM( ");
		sb.append("	SELECT ");
		sb.append("	ba.asn,          ");
		sb.append("	ba.inbound_date, ");
		sb.append("	ba.is_bonded,    ");
		sb.append("	ba.ctn_num,      ");
		sb.append("	ba.bill_num,     ");
		sb.append("	ba.stock_in      ");
		sb.append("	FROM   ");
		sb.append("	bis_asn ba       ");
		sb.append("	WHERE  ");
		sb.append("	1 = 1  AND ba.ASN_STATE >= '3' ");




		if (!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("startTime")))) {//--入库日期
			sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
			parme.put("strTime", map.get("startTime"));
		}
		if (!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("endTime")))) {//--入库日期
			sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			parme.put("endTime", map.get("endTime"));
		}
		if(!StringUtils.isNull(String.valueOf(map.get("defaultTime")))){

			sb.append(" and ba.inbound_date<trunc(sysdate+1) and  ba.inbound_date>=trunc(sysdate)");
		}

		sb.append("	) a  ");
		sb.append("	INNER JOIN (  ");
		sb.append("	SELECT   ");
		sb.append("	bt.sku_id, ");
		sb.append("	bt.asn, ");
		sb.append("	bt.bill_num, ");
		sb.append("	bt.stock_in, ");
		sb.append("	bt.ctn_num,  ");
		sb.append("	bt.enter_state, ");
		sb.append("	bt.original_piece, ");
		sb.append("	bt.remove_piece  ");
		sb.append("	FROM  ");
		sb.append("	BIS_TRAY_INFO bt ");
		sb.append("	WHERE 1 = 1  AND bt.cargo_state != '99' AND bt.cargo_state >= '01' ");

		sb.append("	) t ON t.asn = a.asn ");
		sb.append("	AND a.bill_num = t.bill_num ");
		sb.append("	AND a.ctn_num = t.ctn_num  ");
		sb.append("	GROUP BY  ");
		sb.append("	t.stock_in,  ");
		sb.append("	t.bill_num,  ");
		sb.append("	t.ctn_num,   ");
		sb.append("	t.sku_id,    ");
		sb.append("	t.enter_state, ");
		sb.append("	a.is_bonded,");
		sb.append("	a.inbound_date ");
		sb.append("	) aa     ");
		sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
		sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
		sb.append("ORDER BY  ");
		sb.append("	aa.bill_num,   ");
		sb.append("	aa.ctn_num,   ");
		sb.append("	aa.inbound_date   ");
		Map<String, Object> paramType = new HashMap<>();
		paramType.put("clientName", String.class);
	//	paramType.put("clientId", String.class);//
	//	paramType.put("billCode", String.class);
	//	paramType.put("isBonded", String.class);
//		paramType.put("ctnNum", String.class);
	//	paramType.put("sku", String.class);
		paramType.put("cargoName", String.class);
		paramType.put("operationTime", String.class);
	//	paramType.put("state", String.class);
		paramType.put("pieces", Integer.class);
		paramType.put("allnet", Double.class);
		paramType.put("allgross", Double.class);
		paramType.put("simName", String.class);

		return findSql(sb.toString(), paramType, parme);
	}


	/**
	 * 出库查询
	 */

	public  List<OutsideQuery> getOutStockStocks(Map map) {
		StringBuffer sb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		Map<String, Object> paramType = new HashMap<>();

		sb.append(" SELECT  ");
		sb.append(" aa.stock_name AS clientName,  ");
		sb.append(" s.cargo_name AS cargoName, ");
		sb.append(" aa.bill_num AS billCode, ");
		sb.append(" '0' AS isBonded, ");
		sb.append(" aa.ctn_num AS ctnNum, ");
		sb.append(" aa.sku_id AS sku,  ");
		sb.append(" aa.loading_time AS operationTime, ");
		sb.append(" aa.loading_truck_num AS cargoType,  ");
		sb.append(" aa.stock_id AS clientId,  ");
		sb.append(" aa.enter_state AS state,  ");
		sb.append(" aa.piece_sum AS pieces,  ");
		sb.append(" net_weight_sum AS allnet,  ");
		sb.append(" gross_weight_sum AS allgross,  ");
		sb.append("     s.class_name AS simName  ,  ");
		sb.append(" aa.RECEIVER_NAME AS warehouse  ");
		sb.append(" FROM       ");
		sb.append(" 	(                                                            ");
		sb.append(" 		SELECT                                                     ");
		sb.append(" 			t.loading_time,                                          ");
		sb.append(" 			t.loading_truck_num,                                     ");
		sb.append(" 			lo.stock_name,                                           ");
		sb.append(" 			stock.if_bonded,                                         ");
		sb.append(" 			t.stock_id,                                              ");
		sb.append(" 			t.bill_num,                                              ");
		sb.append(" 			t.ctn_num,                                               ");
		sb.append(" 			t.sku_id,                                                ");
		sb.append(" 			(                                                        ");
		sb.append(" 				CASE t.enter_state                                     ");
		sb.append(" 				WHEN '0' THEN                                          ");
		sb.append(" 					'INTACT'                                             ");
		sb.append(" 				WHEN '1' THEN                                          ");
		sb.append(" 					'BROKEN'                                             ");
		sb.append(" 				WHEN '2' THEN                                          ");
		sb.append(" 					'COVER TORN'                                         ");
		sb.append(" 				END                                                    ");
		sb.append(" 			) AS enter_state,                                        ");
		sb.append(" 			sum(t.piece) AS piece_sum,                               ");
		sb.append(" 			sum(t.net_weight) AS net_weight_sum,                     ");
		sb.append(" 			sum(t.gross_weight) AS gross_weight_sum,                 ");
		sb.append(" 			MIN(lo.RECEIVER_NAME) AS RECEIVER_NAME                   ");
		sb.append(" 		FROM                                                       ");
		sb.append(" 			(                                                        ");
		sb.append(" 				SELECT                                                 ");
		sb.append(" 					sku_id,                                              ");
		sb.append(" 					loading_plan_num,                                    ");
		sb.append(" 					bill_num,                                            ");
		sb.append(" 					stock_id,                                            ");
		sb.append(" 					li.out_link_id,                                      ");
		sb.append(" 					ctn_num,                                             ");
		sb.append(" 					enter_state,                                         ");
		sb.append(" 					li.piece,                                            ");
		sb.append(" 					li.net_weight,                                       ");
		sb.append(" 					li.gross_weight,                                     ");
		sb.append(" 					li.loading_time AS loading_time,                     ");
		sb.append(" 					li.loading_time AS loading_real_time,                ");
		sb.append(" 					li.loading_truck_num                                 ");
		sb.append(" 				FROM                                                   ");
		sb.append(" 					bis_loading_info li                                  ");
		sb.append(" 				WHERE                                                  ");
		sb.append(" 					li.loading_state = '2'                               ");


		sb.append(" 			) t                                                      ");
		sb.append(" 		LEFT JOIN (                                                ");
		sb.append(" 			SELECT                                                   ");
		sb.append(" 				out_link_id,                                           ");
		sb.append(" 				if_bonded                                              ");
		sb.append(" 			FROM                                                     ");
		sb.append(" 				bis_out_stock                                          ");
		sb.append(" 		) stock ON t.out_link_id = stock.out_link_id               ");
		sb.append(" 		LEFT JOIN (                                                ");
		sb.append(" 			SELECT                                                   ");
		sb.append(" 				blo.order_num,                                         ");
		sb.append(" 				blo.stock_name,                                        ");
		sb.append(" 				blo.order_state,                                       ");
		sb.append(" 				RECEIVER_NAME                                          ");
		sb.append(" 			FROM                                                     ");
		sb.append(" 				bis_loading_order blo                                  ");

		sb.append(" 		) lo ON lo.order_num = t.loading_plan_num                  ");
		sb.append(" 		WHERE                                                      ");
		sb.append(" 			lo.order_state = '4'                                     ");

		sb.append(" 		GROUP BY                                                   ");
		sb.append(" 			t.stock_id,                                              ");
		sb.append(" 			t.bill_num,                                              ");
		sb.append(" 			t.ctn_num,                                               ");
		sb.append(" 			t.sku_id,                                                ");
		sb.append(" 			t.enter_state,                                           ");
		sb.append(" 			t.loading_time,                                          ");
		sb.append(" 			t.loading_truck_num,                                     ");
		sb.append(" 			stock.if_bonded,                                         ");
		sb.append(" 			lo.stock_name                                            ");
		if((!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("startTime"))))||(!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("endTime"))))){
			sb.append(" having 1=1 ");
			if(!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("startTime")))){//--入库日期
				sb.append(" and min(t.loading_real_time)>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
				parme.put("strTime",  map.get("startTime"));
			}
			if(!com.haiersoft.ccli.common.utils.StringUtils.isNull(String.valueOf(map.get("endTime")))){//--入库日期
				sb.append(" and min(t.loading_real_time)<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
				parme.put("endTime", map.get("endTime"));
			}
		}


		if(!StringUtils.isNull(String.valueOf(map.get("defaultTime")))){

			sb.append(" having 1=1 and min(t.loading_real_time)<trunc(sysdate+1) and  min(t.loading_real_time)>=trunc(sysdate)");
		}


		sb.append(" 	) aa                                                         ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id         ");
		sb.append(" ORDER BY                                                       ");
		sb.append(" 	aa.bill_num,                                                 ");
		sb.append(" 	aa.ctn_num                                                   ");

		paramType.put("clientName", String.class);//存货方名称
		//paramType.put("clientId", String.class);//存货方id
	//	paramType.put("billCode", String.class);//提单号
	//	paramType.put("isBonded", String.class);//保税非保税
	//	paramType.put("ctnNum", String.class);//箱号
	//	paramType.put("sku", String.class);
		paramType.put("cargoName", String.class);//货物名称
		paramType.put("operationTime", String.class);//【赋值】 出库日期
	//	paramType.put("state", String.class);//货物状态
		paramType.put("pieces", Integer.class);
		paramType.put("allnet", Double.class);
		paramType.put("allgross", Double.class);
		paramType.put("simName", String.class);

		//	paramType.put("cargoType", String.class);//【赋值】装车单号
	//	paramType.put("warehouse", String.class);//【赋值】收货方名称

		return findSql(sb.toString(), paramType, parme);
	}

}
