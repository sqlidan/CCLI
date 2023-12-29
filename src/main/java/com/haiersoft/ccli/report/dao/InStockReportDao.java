package com.haiersoft.ccli.report.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.report.entity.Stock;



/**
 *
 * @author Connor.M
 * @ClassName: InStockReportDao
 * @Description: 在库报告书DAO
 * @date 2016年4月26日 下午3:16:08
 */
@Repository
public class InStockReportDao extends HibernateDao<Stock, String>{

	/**
	 *
	 * @author Connor.M
	 * @Description: 在库报告书 -- 普通
	 * @date 2016年4月26日 下午3:39:46
	 * @param stock
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchInStockReport(Stock stock){
		Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillCode())){
			String[] billList=stock.getBillCode().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT   ");
		sb.append("  tray.stock_in,  ");
		sb.append("  tray.stock_name AS client_name, ");
		sb.append("  (CASE st.IF_BONDED WHEN '1' THEN '保税货物' ELSE '非保税货物' END ) AS is_bonded, ");
		sb.append("  tray.bill_num, ");
		sb.append("  tray.ctn_num, ");
		sb.append("  tray.sku_id, ");
		sb.append("  tray.CARGO_NAME, ");
		sb.append("  tray.rkTime AS inbound_date, ");
		sb.append("  tray.enter_state, ");
		sb.append("  SUM (tray.num) AS NOW_PIECE_SUM, ");
		sb.append("  ROUND (SUM(tray.net_weight), 2) AS net_weight_sum, ");
		sb.append("  ROUND (SUM(tray.gross_weight), 2) AS gross_weight_sum, ");
		sb.append("  ROUND (MAX(s.net_single), 2) AS net_single, ");
		sb.append("  ROUND (MAX(s.gross_single), 2) AS gross_single, ");
		sb.append("  s.class_name, ");
		sb.append("  s.class_type  ");
		if("1".equals(stock.getLocationType())){
			sb.append("   ,tray.cargo_location ");
		}
		if("2".equals(stock.getReportType())){
			sb.append("   ,nvl(ai.rk_num,s.rkdh) as RK_NUM ");
		}
		if("3".equals(stock.getReportType())){
			sb.append(" ,s.type_size ");
			sb.append(" ,s.pro_num   ");
			sb.append(" ,s.shipnum   ");
			sb.append(" ,s.msc_num   ");
			sb.append(" ,ba.order_num ");
			sb.append(" ,s.lot_num   ");
			sb.append(" ,ai.pro_time ");
		}
		sb.append(" FROM(   ");
		sb.append("   SELECT ");
		sb.append(" 	tray.stock_in,     ");
		sb.append(" 	tray.stock_name,   ");
		sb.append(" 	tray.bill_num,     ");
		sb.append(" 	tray.ctn_num,      ");
		sb.append("     tray.sku_id,       ");
		sb.append("     tray.asn,          ");
		sb.append(" 	SUM (tray.now_piece) AS num, ");
		sb.append(" 	( ");
		sb.append(" 	CASE tray.enter_state    ");
		sb.append(" 	WHEN '0' THEN ");
		sb.append(" 	'INTACT'      ");
		sb.append(" 	WHEN '1' THEN ");
		sb.append(" 	'BROKEN'      ");
		sb.append(" 	WHEN '2' THEN ");
		sb.append(" 	'COVER TORN'  ");
		sb.append(" 	END    ");
		sb.append("    ) AS enter_state,  ");
		sb.append("    tray.CARGO_NAME,              ");
		sb.append("    SUM (tray.net_weight) AS net_weight, ");
		sb.append("    SUM (tray.gross_weight) AS gross_weight, ");
		sb.append("    tray.CONTACT_NUM, ");
		sb.append("    TO_CHAR (tray.ENTER_STOCK_TIME,'yyyy-MM-dd') AS rkTime,");
		sb.append("    tray.cargo_location  ");
		sb.append("  FROM  ");
		sb.append("    BIS_TRAY_INFO tray ");
		sb.append("  WHERE 1 = 1 AND (tray.cargo_state = '01' OR tray.cargo_state = '10') ");
		sb.append("  AND tray.now_piece != 0  ");
		sb.append("  GROUP BY  ");
		sb.append("   tray.stock_in,          ");
		sb.append("   tray.stock_name,        ");
		sb.append("   tray.bill_num,          ");
		sb.append("   tray.ctn_num,           ");
		sb.append("   tray.asn,               ");
		sb.append("   tray.sku_id,            ");
		sb.append("   tray.enter_state,       ");
		sb.append("   tray.CARGO_NAME,        ");
		sb.append("   tray.CONTACT_NUM,       ");
		sb.append("   tray.enter_state,       ");
		sb.append("   TO_CHAR (tray.ENTER_STOCK_TIME,'yyyy-MM-dd'),  ");
		sb.append("   tray.cargo_location ");
		sb.append("  ) tray  ");
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
        sb.append(" ) ba ON (tray.asn = ba.asn AND ba.ctn_num = tray.ctn_num AND ba.bill_num = tray.bill_num)        ");
        sb.append(" LEFT JOIN bis_asn_info ai ON (ai.asn_id =tray.asn AND ai.sku_id=tray.sku_id)                 ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (           ");
		sb.append(" 	tray.bill_num = st.ITEM_NUM             ");
		sb.append(" 	AND tray.CONTACT_NUM = st.LINK_ID       ");
		sb.append(" )      ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON (    ");
		sb.append(" 	st.ITEM_NUM = info.ITEM_NUM             ");
		sb.append(" 	AND st.LINK_ID = info.LINK_ID           ");
		sb.append(" 	AND tray.ctn_num = info.ctn_num         ");
		sb.append(" 	AND tray.sku_id = info.sku              ");
		sb.append(" )     ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id    ");
		sb.append(" WHERE 1 = 1 ");
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND tray.stock_in=:clientId1 ");
			params.put("clientId1", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND tray.bill_num in ("+billCode+")   ");
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND tray.ctn_num=:ctnCode1 ");
			params.put("ctnCode1", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getCargoType())){
			sb.append(" AND s.CLASS_TYPE=:sclass ");
			params.put("sclass", stock.getCargoType());
		}
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND st.IF_BONDED=:isBonded");
        		params.put("isBonded",stock.getIsBonded());
        	}else{
        		sb.append(" AND (st.IF_BONDED='0' or st.IF_BONDED is null)    ");
        	}
        }
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')>= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')< to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" GROUP BY   ");
		sb.append("  tray.stock_in, ");
		sb.append("  tray.stock_name, ");
		sb.append("  st.IF_BONDED, ");
		sb.append("  tray.bill_num, ");
		sb.append("  tray.ctn_num,  ");
		sb.append("  tray.sku_id,  ");
		sb.append("  s.class_name, ");
		sb.append("  s.class_type, ");
		sb.append("  tray.CARGO_NAME, ");
		sb.append("  tray.rkTime,  ");
		sb.append("  tray.enter_state  ");
		if("1".equals(stock.getLocationType())){
			sb.append("   ,tray.cargo_location  ");
		}
		if("2".equals(stock.getReportType())){
			sb.append("   ,ai.rk_num,s.rkdh  ");
		}
		if("3".equals(stock.getReportType())){
			sb.append("   ,s.type_size  ");
			sb.append("   ,s.pro_num ");
			sb.append("   ,s.lot_num ");
			sb.append("   ,s.msc_num ");
			sb.append("   ,s.shipnum ");
			sb.append("   ,ba.order_num ");
			sb.append("   ,ai.pro_time ");
		}
		sb.append(" ORDER BY                                                                   ");
		sb.append(" 	tray.bill_num,  ");
		sb.append(" 	tray.ctn_num,   ");
		sb.append(" 	tray.rkTime  ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	/**
	 *
	 * @author Connor.M
	 * @Description: 在库报告书 -- 日本
	 * @date 2016年4月27日 上午9:55:18
	 * @param stock
	 * @return
	 * @throws
	 */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchInStockReportRB(Stock stock) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb=new StringBuffer();
        String billCode = "";
        if (!StringUtils.isNull(stock.getBillCode())) {
            String[] billList = stock.getBillCode().split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        /* bt.stock_in = '6940' and --客户ID bt.bill_num = '20160422' and --提单号
       	* bt.ctn_num = '20' and --箱号
        * where ba.bill_num = '20160422' and --提单号 ba.ctn_num = '20' and --箱号
       	* ba.stock_in = '6940' and --客户ID ba.inbound_date >=
       	* to_date('2016-01-02', 'yyyy-mm-dd') and ba.inbound_date <
       	* to_date('2016-12-21', 'yyyy-mm-dd') + 1 --入库日期*/
        sb.append(" SELECT                                                  ");
        sb.append(" 	c.client_name,                                      ");
        sb.append(" 	aa.stock_in,                                        ");
        sb.append(" 	nvl (aa.rk_num, s.rkdh) AS RK_NUM,                  ");
        sb.append(" 	aa.bill_num,                                        ");
        sb.append(" 	aa.ctn_num,                                         ");
        sb.append(" 	aa.sku_id,                                          ");
        sb.append(" 	s.cargo_name,                                       ");
        if ("1".equals(stock.getLocationType())) {
            sb.append(" aa.cargo_location, ");
        }
        sb.append(" 	aa.inbound_date,                                    ");
        sb.append(" 	aa.enter_state,                                     ");
        sb.append(" 	aa.now_piece_sum,                                   ");
        sb.append(" 	round(aa.net_weight_sum, 2) AS net_weight_sum,      ");
        sb.append(" 	round(aa.gross_weight_sum, 2) AS gross_weight_sum,  ");
        sb.append(" 	s.class_name,                                       ");
        sb.append(" 	round(s.net_single, 2) AS net_single,               ");
        sb.append(" 	round(s.gross_single, 2) AS gross_single,           ");
        sb.append(" 	aa.is_bonded                                        ");
        sb.append(" FROM                                                    ");
        sb.append(" 	(                                                   ");
        sb.append(" 		SELECT                                          ");
        sb.append(" 			ai.rk_num,                                  ");
        sb.append(" 			a.inbound_date,                             ");
        sb.append(" 			t.stock_in,                                 ");
        sb.append(" 			t.bill_num,                                 ");
        sb.append(" 			t.ctn_num,                                  ");
        sb.append(" 			t.sku_id,                                   ");
        sb.append(" 			a.is_bonded,                                ");
        if ("1".equals(stock.getLocationType())) {
            sb.append(" t.cargo_location,  ");
        }
        sb.append(" 			(                                           ");
        sb.append(" 				CASE t.enter_state                      ");
        sb.append(" 				WHEN '0' THEN                           ");
        sb.append(" 					'INTACT'                            ");
        sb.append(" 				WHEN '1' THEN                           ");
        sb.append(" 					'BROKEN'                            ");
        sb.append(" 				WHEN '2' THEN                           ");
        sb.append(" 					'COVER TORN'                        ");
        sb.append(" 				END                                     ");
        sb.append(" 			) AS enter_state,                           ");
        sb.append(" 			sum(t.now_piece) AS now_piece_sum,          ");
        sb.append(" 			sum(t.net_weight) AS net_weight_sum,        ");
        sb.append(" 			sum(t.gross_weight) AS gross_weight_sum     ");
        sb.append(" 		FROM                                            ");
        sb.append(" 			(                                           ");
        sb.append(" 				SELECT                                  ");
        sb.append(" 					bt.sku_id,                          ");
        sb.append(" 					bt.asn,                             ");
        sb.append(" 					bt.bill_num,                        ");
        sb.append(" 					bt.stock_in,                        ");
        sb.append(" 					bt.ctn_num,                         ");
        if ("1".equals(stock.getLocationType())) {
            sb.append(" bt.cargo_location, ");
        }
        sb.append(" 					bt.enter_state,                     ");
        sb.append(" 					bt.now_piece,                       ");
        sb.append(" 					bt.net_weight,                      ");
        sb.append(" 					bt.gross_weight                     ");
        sb.append(" 				FROM                                    ");
        sb.append(" 					BIS_TRAY_INFO bt                    ");
        sb.append(" 				WHERE                                   ");
        sb.append(" 					(                                   ");
        sb.append(" 						bt.cargo_state = '01'           ");
        sb.append(" 						OR bt.cargo_state = '10'        ");
        sb.append(" 					)                                   ");
        sb.append(" 				AND bt.now_piece != 0                   ");
        if (!StringUtils.isNull(stock.getClientId())) {
            sb.append(" AND bt.stock_in = :clientId1 ");
            params.put("clientId1", stock.getClientId());
        }

        if (!StringUtils.isNull(stock.getBillCode())) {
            sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }

        if (!StringUtils.isNull(stock.getCtnNum())) {
            sb.append(" AND bt.ctn_num = :ctnCode1 ");
            params.put("ctnCode1", stock.getCtnNum());
        }
        sb.append(" 			) t                                         ");
        sb.append(" 		LEFT JOIN (                                     ");
        sb.append(" 			SELECT                                      ");
        sb.append(" 				ba.asn,                                 ");
        sb.append(" 				trunc (ba.inbound_date) AS inbound_date,");
        sb.append(" 				ba.ctn_num,                             ");
        sb.append(" 				ba.bill_num,                            ");
        sb.append(" 				ba.stock_in,                            ");
        sb.append(" 				ba.is_bonded                            ");
        sb.append(" 			FROM                                        ");
        sb.append(" 				bis_asn ba                              ");
        sb.append(" 			WHERE                                       ");
        sb.append(" 				1 = 1                                   ");
        if (!StringUtils.isNull(stock.getBillCode())) {
            sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }

        if (!StringUtils.isNull(stock.getStrartTime())) {
            sb.append(" AND ba.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
            params.put("strartTime", stock.getStrartTime());
        }

        if (!StringUtils.isNull(stock.getEndTime())) {
            sb.append(" AND ba.inbound_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
            params.put("endTime", stock.getEndTime());
        }
        sb.append(" 		) a ON t.asn = a.asn                            ");
        sb.append(" 		AND a.ctn_num = t.ctn_num                       ");
        sb.append(" 		AND a.bill_num = t.bill_num                     ");
        sb.append(" 		LEFT JOIN bis_asn_info ai ON ai.asn_id = a.asn  ");
        sb.append(" 		AND ai.sku_id = t.sku_id                        ");
        sb.append(" 		GROUP BY                                        ");
        sb.append(" 			ai.rk_num,                                  ");
        sb.append(" 			t.stock_in,                                 ");
        sb.append(" 			t.bill_num,                                 ");
        sb.append(" 			a.is_bonded,                                ");
        sb.append(" 			t.ctn_num,                                  ");
        sb.append(" 			t.sku_id,                                   ");
        if ("1".equals(stock.getLocationType())) {
            sb.append(" t.cargo_location, ");
        }
        sb.append(" 			a.inbound_date,                             ");
        sb.append(" 			t.enter_state                               ");
        sb.append(" 	) aa                                                ");
        sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_in     ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id  ");
        sb.append(" WHERE 1=1                                               ");
        if (!StringUtils.isNull(stock.getCargoType())) {
            sb.append("  AND s.CLASS_TYPE=:sclass ");
            params.put("sclass", stock.getCargoType());
        }
        if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND aa.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (aa.is_bonded ='0' or aa.is_bonded is null)    ");
        	}
        }
        sb.append(" ORDER BY                                                ");
        sb.append(" 	RK_NUM,                                             ");
        sb.append(" 	aa.bill_num,                                        ");
        sb.append(" 	aa.ctn_num,                                         ");
        sb.append(" 	aa.inbound_date                                     ");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

	/**
	 *
	 * @author Connor.M
	 * @Description: 在库报告书 -- OTE
	 * @date 2016年4月27日 上午11:28:13
	 * @param stock
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchInStockReportOTE(Stock stock){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillCode())){
			String[] billList=stock.getBillCode().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		sb.append("select c.client_name,                                                   ");
		sb.append("       aa.stock_in,                                                     ");
		sb.append("       aa.bill_num,                                                     ");
		sb.append("       aa.ctn_num,                                                      ");
		sb.append("       aa.sku_id,                                                       ");
		sb.append("       s.cargo_name,                                                    ");
		if("1".equals(stock.getLocationType())){
			sb.append(" aa.cargo_location, ");
		}
		sb.append("       aa.pro_time,                                                     ");
		sb.append("       aa.inbound_date,                                                 ");
		sb.append("       aa.enter_state,                                                  ");
		sb.append("       aa.now_piece_sum,                                                ");
		sb.append("       round(aa.net_weight_sum, 2) as net_weight_sum,                   ");
		sb.append("       round(aa.gross_weight_sum, 2) as gross_weight_sum,               ");
		sb.append("       s.type_size,                                                     ");
		sb.append("       s.pro_num,                                                       ");
		sb.append("       s.lot_num,                                                       ");
		sb.append("       s.msc_num,                                                       ");
		sb.append("       s.shipnum,                                                       ");
		sb.append("       s.class_name,                                                    ");
		sb.append("       round(s.net_single, 2) as net_single,                            ");
		sb.append("       round(s.gross_single, 2) as gross_single,                        ");
		sb.append("       aa.order_num,                                                    ");
		sb.append("       aa.is_bonded                                                     ");
		sb.append("  from (select ai.pro_time,                                             ");
		sb.append("               a.inbound_date,                                          ");
		sb.append("               a.is_bonded,                                             ");
		sb.append("               t.stock_in,                                              ");
		sb.append("               t.bill_num,                                              ");
		sb.append("               t.ctn_num,                                               ");
		sb.append("               t.sku_id,                                                ");
		if("1".equals(stock.getLocationType())){
			sb.append(" t.cargo_location, ");
		}
		sb.append("               (case t.enter_state                                      ");
		sb.append("                 when '0' then                                          ");
		sb.append("                  'INTACT'                                              ");
		sb.append("                 when '1' then                                          ");
		sb.append("                  'BROKEN'                                              ");
		sb.append("                 when '2' then                                          ");
		sb.append("                  'COVER TORN'                                          ");
		sb.append("               end) as enter_state,                                     ");
		sb.append("               sum(t.now_piece) as now_piece_sum,                       ");
		sb.append("               sum(t.net_weight) as net_weight_sum,                     ");
		sb.append("               sum(t.gross_weight) as gross_weight_sum,                 ");
		sb.append("               a.order_num as order_num                                 ");
		sb.append("          from (select bt.bill_num,                                     ");
		sb.append("                       bt.ctn_num,                                      ");
		sb.append("                       bt.sku_id,                                       ");
		sb.append("                       bt.asn,                                          ");
		sb.append("                       bt.stock_in,                                     ");
		if("1".equals(stock.getLocationType())){
			sb.append(" bt.cargo_location, ");
		}
		sb.append("                       bt.enter_state,                                  ");
		sb.append("                       bt.now_piece,                                    ");
		sb.append("                       bt.net_weight,                                   ");
		sb.append("                       bt.gross_weight                                  ");
		sb.append("                  from BIS_TRAY_INFO bt                                 ");
		sb.append("                 where (bt.cargo_state = '01' or bt.cargo_state = '10') ");
		sb.append("                   and bt.now_piece != 0                                ");
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND bt.stock_in = :clientId1 ");
			params.put("clientId1", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND bt.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND bt.ctn_num = :ctnCode1 ");
			params.put("ctnCode1", stock.getCtnNum());
		}
		sb.append("        ) t                                                             ");
		sb.append("          left join (select ba.bill_num,                                ");
		sb.append("                           ba.ctn_num,                                  ");
		sb.append("                           ba.asn,                                      ");
		sb.append("                           ba.is_bonded,                                ");
		sb.append("                           trunc(ba.inbound_date) as inbound_date,      ");
		sb.append("                           ba.stock_in,                                 ");
		sb.append("                           ba.order_num as order_num                    ");
		sb.append("                      from bis_asn ba                                   ");
		sb.append("                     where 1 = 1                                        ");
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND ba.bill_num in ("+billCode+") ");
		}
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND ba.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND ba.inbound_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append("          ) a                                                           ");
		sb.append("            on t.asn = a.asn                                            ");
		sb.append("           and a.ctn_num = t.ctn_num                                    ");
		sb.append("           and a.bill_num = t.bill_num                                  ");
		sb.append("          left join bis_asn_info ai                                     ");
		sb.append("            on ai.asn_id = a.asn                                        ");
		sb.append("           and ai.sku_id = t.sku_id                                     ");
		sb.append("         group by ai.pro_time,                                          ");
		sb.append("                  t.stock_in,                                           ");
		sb.append("                  t.bill_num,                                           ");
		sb.append("                  t.ctn_num,                                            ");
		sb.append("                  t.sku_id,                                             ");
		if("1".equals(stock.getLocationType())){
			sb.append("  t.cargo_location, ");
		}
		sb.append("                  a.inbound_date,                                       ");
		sb.append("                  a.is_bonded,                                          ");
		sb.append("                  a.order_num,                                          ");
		sb.append("                  t.enter_state) aa                                     ");
		sb.append("  left join base_client_info c                                          ");
		sb.append("    on c.ids = aa.stock_in                                              ");
		sb.append("  left join base_sku_base_info s                                        ");
		sb.append("    on s.sku_id = aa.sku_id                                             ");
		sb.append(" WHERE 1=1                                                              ");
		if(!StringUtils.isNull(stock.getCargoType())){
			sb.append(" ADN s.CLASS_TYPE=:sclass ");
			params.put("sclass", stock.getCargoType());
		}
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND aa.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (aa.is_bonded ='0' or aa.is_bonded is null)    ");
        	}
        }
		sb.append(" order by aa.bill_num, aa.ctn_num, aa.inbound_date                      ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
	
    public Page<Stock> getStocks(Page<Stock> page, Stock stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if(!StringUtils.isNull(stock.getBillCode())){
			String[] billList=stock.getBillCode().split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT                                                                 ");  
		sb.append("			tray.stock_in AS clientId,                                           ");
		sb.append("			tray.stock_name AS clientName,                                       ");
		sb.append("			(                                                                    ");
		sb.append("				CASE st.IF_BONDED                                                  ");
		sb.append("				WHEN '1' THEN                                                      ");
		sb.append("					'1'                                                              ");
		sb.append("				ELSE                                                               ");
		sb.append("					'0'                                                              ");
		sb.append("				END                                                                ");
		sb.append("			) AS isBonded,                                                       ");
		sb.append("			tray.bill_num AS billCode,                                           ");
		sb.append("			tray.ctn_num AS ctnNum,                                              ");
		sb.append("			tray.sku_id AS sku,                                                  ");
		sb.append("         tray.CARGO_NAME AS cargoName,                                       ");
		sb.append("			tray.rkTime,                                                         ");
		sb.append("			tray.enter_state AS state,                                           ");
		sb.append("			SUM (tray.num) AS nowNum,                                            ");
		sb.append("			SUM (tray.net_weight) AS allnet,                                     ");
		sb.append("			SUM (tray.gross_weight) AS allgross,                                 ");
		sb.append("         max(s.net_single) AS netWeight,                                     ");
		sb.append("			max(s.gross_single) AS grossWeight,                                  ");
		sb.append("         s.class_name AS simName,                                            ");
		sb.append("			s.class_type AS simType                                             ");
		if("1".equals(stock.getLocationType())){
				sb.append("	 ,tray.cargo_location AS locationCode                               ");
		}
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
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND tray.stock_in=:clientId1 ");
			params.put("clientId1", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND tray.bill_num in ("+billCode+")   ");
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND tray.ctn_num=:ctnCode1 ");
			params.put("ctnCode1", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getCargoType())){
			sb.append(" AND s.CLASS_TYPE=:sclass ");
			params.put("sclass", stock.getCargoType());
		}
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND st.IF_BONDED=:isBonded");
        		params.put("isBonded",stock.getIsBonded());
        	}else{
        		sb.append(" AND (st.IF_BONDED='0' or st.IF_BONDED is null)    ");
        	}
        }
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')>= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND to_date(tray.rkTime,'yyyy-mm-dd')< to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
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
		if("1".equals(stock.getLocationType())){
			sb.append("	    ,tray.cargo_location                                                 ");
	    }
		sb.append("		ORDER BY                                                                 ");
		sb.append("			tray.bill_num,                                                       ");
		sb.append("			tray.ctn_num,                                                        ");
		sb.append("			tray.rkTime                                                          ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("clientName", String.class);
        paramType.put("clientId", String.class);
        paramType.put("isBonded", String.class);
        paramType.put("billCode", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);
        if("1".equals(stock.getLocationType())){
        	paramType.put("locationCode", String.class);
		}
        paramType.put("rkTime",String.class);
        paramType.put("state",String.class);
        paramType.put("nowNum",Integer.class);
        paramType.put("allnet",Double.class);
        paramType.put("allgross",Double.class);
        paramType.put("simName", String.class);
        paramType.put("netWeight", Double.class);
        paramType.put("grossWeight", Double.class);
        return findPageSql(page, sb.toString(), paramType, params);
    }
    
    /**
     * 入库报告书页面展示数据查询方法
     * @param page
     * @param 
     * @return
     */
    public Page<Stock> getEnterStockStocks(Page<Stock> page, String itemNum, String cunNum, String stockIn, String linkId, String sku, String strTime, String endTime,String isBonded) {
    	StringBuffer sb = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        String billCode = "";
        if (!StringUtils.isNull(itemNum)) {
            String[] billList = itemNum.split(",");
            for (String billNum : billList) {
                billCode += "'" + billNum + "'" + ",";
            }
            if (!billCode.equals("")) {
                billCode = billCode.substring(0, billCode.length() - 1);
            }
        }
        sb.append("SELECT  "); 
        sb.append("	c.client_name AS clientName,  ");
        sb.append("	aa.stock_in AS clientId,      ");
        sb.append("	aa.bill_num AS billCode,      ");
        sb.append("	aa.ctn_num AS ctnNum,         ");
        sb.append("	aa.sku_id AS sku,             ");
        sb.append("	s.cargo_name AS cargoName,    ");
        sb.append("	aa.inbound_date AS enterTime, ");
        sb.append("	aa.enter_state AS state,      ");
        sb.append("	aa.ruku_piece_sum AS nowNum,  ");
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
        if(!StringUtils.isNull(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND ba.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (ba.is_bonded ='0' or ba.is_bonded is null)      ");
        	}
        }
        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND ba.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
        	sb.append(" and ba.ctn_num=:ctnnum   ");
            parme.put("ctnnum", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
        	sb.append(" and ba.stock_in=:sockid  ");
            parme.put("sockid", stockIn);
        }
        if (strTime != null && !"".equals(strTime)) {//--入库日期
        	sb.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
            parme.put("strTime", strTime);
        }
        if (endTime != null && !"".equals(endTime)) {//--入库日期
        	sb.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
            parme.put("endTime", endTime);
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
        if (!StringUtils.isNull(itemNum)) {//提单号
            sb.append(" AND bt.bill_num in (" + billCode + ") ");
        }
        if (cunNum != null && !"".equals(cunNum)) {//--箱号
            sb.append(" and bt.ctn_num=:ctnnum1   ");
            parme.put("ctnnum1", cunNum);
        }
        if (stockIn != null && !"".equals(stockIn)) {//--客户ID
            sb.append(" and bt.stock_in=:sockid1  ");
            parme.put("sockid1", stockIn);
        }
        if (sku != null && !"".equals(sku)) {//--sku
            sb.append(" and  bt.sku_id=:sku  ");
            parme.put("sku", sku);
        }
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
        paramType.put("clientId", String.class);//
        paramType.put("billCode", String.class);
        paramType.put("isBonded", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("enterTime", Date.class);
        paramType.put("state", String.class);
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class);
        return findPageSql(page, sb.toString(), paramType, parme);
    }
    
    /**
     * 出库报告书 页面展示数据查询方法
     * @param page
     * @param itemNum
     * @param cunNum
     * @param stockIn
     * @param linkId
     * @param strTime
     * @param endTime
     * @return
     */
    public Page<Stock> getOutStockStocks(Page<Stock> page,String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded) {
    	StringBuffer sb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		Map<String, Object> paramType = new HashMap<>();
		String trucknum = "";
		String billnum="";
        if (!StringUtils.isNull(itemNum)) {
            String[] trucknumList = itemNum.split(",");
            for (String truck : trucknumList) {
            	trucknum += "'" + truck + "'" + ",";
            }
            if (!trucknum.equals("")) {
            	trucknum = trucknum.substring(0, trucknum.length() - 1);
            }
        }
        if (!StringUtils.isNull(billNum)) {
            String[] billNumList = billNum.split(",");
            for (String truck : billNumList) {
            	billnum += "'" + truck + "'" + ",";
            }
            if (!billnum.equals("")) {
            	billnum = billnum.substring(0, billnum.length() - 1);
            }
        }
        sb.append(" SELECT  ");
        sb.append(" aa.stock_name AS clientName,  ");
        sb.append(" s.cargo_name AS cargoName, ");
        sb.append(" aa.bill_num AS billCode, ");
        sb.append(" '0' AS isBonded, ");
        sb.append(" aa.ctn_num AS ctnNum, ");
        sb.append(" aa.sku_id AS sku,  ");
        sb.append(" aa.loading_time AS enterTime, ");
        sb.append(" aa.loading_truck_num AS cargoType,  ");
        sb.append(" aa.stock_id AS clientId,  ");
        sb.append(" aa.enter_state AS state,  ");
        sb.append(" aa.piece_sum AS nowNum,  ");
        sb.append(" net_weight_sum AS allnet,  ");
        sb.append(" gross_weight_sum AS allgross,  ");
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
        if(trucknum!=null && !"".equals(trucknum)){//装车单号 
			sb.append("             and  li.loading_truck_num in (" + trucknum + ") ");// --装车单号
		}
        if(billnum!=null && !"".equals(billnum)){//提单号 
			sb.append("             and  li.BILL_NUM in (" + billnum + ") ");// --提单号
		}
	    if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sb.append("             and   li.ctn_num =:ctnnum");//--箱号 
			parme.put("ctnnum", cunNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sb.append("             and  li.stock_id  =:sockid  ");//--客户ID
			parme.put("sockid", stockIn);
		}
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
        if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			  sb.append("              where blo.stock_id =:sockid1 "); 
			  parme.put("sockid1", stockIn);
		}
        sb.append(" 		) lo ON lo.order_num = t.loading_plan_num                  ");
        sb.append(" 		WHERE                                                      ");
        sb.append(" 			lo.order_state = '4'                                     ");
        if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND stock.if_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (stock.if_bonded ='0' or stock.if_bonded is null)    ");
        	}
        }
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
        if((strTime!=null && !"".equals(strTime))||(endTime!=null && !"".equals(endTime))){
			  sb.append(" having 1=1 ");
			  if(strTime!=null && !"".equals(strTime)){//--入库日期 
					sb.append(" and min(t.loading_real_time)>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
					parme.put("strTime", strTime);
				}
			  if(endTime!=null && !"".equals(endTime)){//--入库日期 
					sb.append(" and min(t.loading_real_time)<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
					parme.put("endTime", endTime);
			  }
		}
        sb.append(" 	) aa                                                         ");
        sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id         ");
        sb.append(" ORDER BY                                                       ");
        sb.append(" 	aa.bill_num,                                                 ");
        sb.append(" 	aa.ctn_num                                                   ");

        paramType.put("clientName", String.class);//存货方名称
        paramType.put("clientId", String.class);//存货方id
        paramType.put("billCode", String.class);//提单号
        paramType.put("isBonded", String.class);//保税非保税
        paramType.put("ctnNum", String.class);//箱号
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);//货物名称
        paramType.put("enterTime", Date.class);//【赋值】 出库日期
        paramType.put("state", String.class);//货物状态
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class); 
        paramType.put("cargoType", String.class);//【赋值】装车单号
        paramType.put("warehouse", String.class);//【赋值】收货方名称
        return findPageSql(page, sb.toString(), paramType, parme);
    }


    public Page<Stock> getTransferStockStocks(Page<Stock> page,String isBonded,String transferNum) {
    	StringBuffer sb=new StringBuffer();
    	sb.append("SELECT ");
    	sb.append("	m.transfer_id AS contactCode,");
    	sb.append("	t.cargo_name AS cargoName,");
    	sb.append("	t.is_bonded AS isBonded,");
    	sb.append("	t.bill_num AS billCode,");
    	sb.append("	t.ctn_num AS ctnNum,");
    	sb.append("	t.sku_id AS sku,");
    	sb.append("	t.OPERATE_TIME AS inTime, ");
    	sb.append("	m.start_store_date AS enterTime, ");
    	sb.append("	m.receiver_name AS warehouse,");
    	sb.append("	m.stock_in AS clientName,");
    	sb.append("	(CASE t.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS state, ");
    	sb.append("	SUM(t.piece) AS nowNum,");
    	sb.append("	SUM(t.NET_WEIGHT) AS allnet, ");
    	sb.append("	SUM(t.GROSS_WEIGHT) AS allgross ");
		sb.append(" FROM  (SELECT t.bill_num,t.ctn_num,t.cargo_name,t.sku_id,t.OPERATE_TIME,t.enter_state,t.piece,t.NET_WEIGHT,t.GROSS_WEIGHT,t.transfer_link_id,a.is_bonded ");
		sb.append(" FROM bis_transfer_stock_info t ");
		sb.append(" left join bis_asn a  on (t.bill_num = a.bill_num and t.ctn_num = a.ctn_num)  ");
		sb.append(" GROUP BY t.bill_num,t.ctn_num,t.cargo_name,t.sku_id,t.OPERATE_TIME,t.enter_state,t.piece,t.NET_WEIGHT,t.GROSS_WEIGHT,t.transfer_link_id,a.is_bonded) t  ");
//    	sb.append(" FROM  ");
//    	sb.append("	BIS_TRANSFER_STOCK_INFO t  ");
    	sb.append("LEFT JOIN BIS_TRANSFER_STOCK m ON t.transfer_link_id = m.transfer_id      ");
//    	sb.append("LEFT JOIN bis_asn a ON ( t.bill_num = a.bill_num	AND t.ctn_num = a.ctn_num ) ");
    	sb.append("WHERE 1 = 1  ");
		String transferId="";
		if(!StringUtils.isNull(transferNum)){
			String[] transferList=transferNum.split(",");
			for(String obj:transferList){
				transferId+="'"+obj+"'"+",";
			}
			if(!transferId.equals("")){
				transferId=transferId.substring(0, transferId.length()-1);
			}
			sb.append(" and t.TRANSFER_LINK_ID in ("+transferId+") ");
		}
		if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND t.is_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (t.is_bonded ='0' or t.is_bonded is null)    ");
        	}
        }
		sb.append("GROUP BY  "); 
		sb.append(" M .transfer_id,");
		sb.append("	T .cargo_name,");
		sb.append("	t .is_bonded, ");
		sb.append(" T .bill_num,  ");
		sb.append("	T .ctn_num,  ");
		sb.append("	T .sku_id,  ");
		sb.append("	T .OPERATE_TIME, ");
		sb.append("	M .start_store_date,");
		sb.append("	M .receiver_name, ");
		sb.append("	M .stock_in, ");
		sb.append(" T.enter_state ");
    	sb.append(" ORDER BY   ");
    	sb.append("	 t.bill_num, ");
    	sb.append("	 t.ctn_num,  ");
    	sb.append("	 t.operate_time ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
        Map<String, Object> paramType = new HashMap<>();
        
        paramType.put("clientName", String.class);//存货方名称
        paramType.put("isBonded", String.class);//是否保税
        paramType.put("billCode", String.class);//提单号
        paramType.put("ctnNum", String.class);//箱号
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);//货物名称
        paramType.put("enterTime", Date.class);//【赋值】 货转日期
        paramType.put("state", String.class);//货物状态
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class); 
        paramType.put("inTime", Date.class);//【赋值】操作时间
        paramType.put("warehouse", String.class);//【赋值】收货方名称
        paramType.put("contactCode", String.class);//【赋值】货转单号
        return findPageSql(page, sb.toString(), paramType,parme);
    }
    
    
    public Page<Stock> getASNReportStocks(Page<Stock> page, String asnId) {

    	StringBuffer sql = new StringBuffer();
        HashMap<String, Object> parme = new HashMap<String, Object>();
        sql=new StringBuffer("select o.sku_id as sku,"
						+"o.cargo_name as cargoName,"
						+"o.piece as nowNum,"
						+"a.ctn_num as ctnNum,"
						+"a.bill_num as billCode,"
						+"t.tray_id as trayCode,"
						+"t.cargo_location as locationCode,"
						+"o.asn_id as asn");
        sql.append(" from bis_asn_info o ");
        sql.append(" inner join bis_asn a on a.asn = o.asn_id ");
        sql.append(" inner join bis_tray_info t on t.asn = a.asn and t.bill_num = a.bill_num and t.sku_id = o.sku_id and t.ctn_num = a.ctn_num ");
        
        if(!StringUtils.isNull(asnId)){
        	sql.append(" where o.asn_id =:asnId");
        	parme.put("asnId", asnId);
		}
        
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("asn", String.class);
        paramType.put("billCode", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("trayCode", String.class);
        paramType.put("locationCode", String.class);
        paramType.put("nowNum", Integer.class);

        return findPageSql(page, sql.toString(), paramType, parme);
    }

	public List<Map<String, Object>> getASNReportStocks(String billCode, String customer, String ctnNum) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> findInventoryList(Stock stock, String clientId) {
			Map<String,Object> params = new HashMap<String, Object>();
			String billCode="";

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT   ");
			sb.append("  tray.stock_name AS clientName, ");
			sb.append("  tray.bill_num as billCode ,");
			sb.append("  tray.ctn_num   as ctnNum,  ");
			sb.append("  tray.CARGO_NAME  as cargoName , ");
			sb.append("	 SUM (tray.now_piece) AS nowNum,  ");
			sb.append("  ROUND (SUM(tray.net_weight), 2) AS netWeight, ");
			sb.append("  ROUND (SUM(tray.gross_weight), 2) AS grossWeight, ");

		//sb.append("  sum( tray.now_piece * tray.net_single ) AS allnet,");
		//sb.append(" sum( tray.now_piece * tray.gross_single )  AS allgross,");

		sb.append(" tray.cargo_location  as locationCode ");
			sb.append("FROM   BIS_TRAY_INFO tray ");
            sb.append(" where 	1 = 1  AND ( tray.cargo_state = '01' OR tray.cargo_state = '10' ) ");
            sb.append(" 	AND tray.now_piece != 0 ");
			if(!StringUtils.isNull(clientId)){
				sb.append(" AND"+ clientId );
				
			}
			if(!StringUtils.isNull(stock.getBillCode())&&!"null".equals(stock.getBillCode())){
				sb.append(" AND tray.bill_num=:billCode ");
				params.put("billCode", stock.getBillCode());
			}
			if(!StringUtils.isNull(stock.getCtnNum())&&!"null".equals(stock.getCtnNum())){
				sb.append(" AND tray.ctn_num=:ctnCode1 ");
				params.put("ctnCode1", stock.getCtnNum());
			}
			sb.append(" GROUP BY                                                                 ");
			sb.append(" 		tray.stock_name,  ");
			sb.append(" 		tray.bill_num,   ");
			sb.append(" 		tray.ctn_num,	tray.CARGO_NAME,	tray.cargo_location  ");
			SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return sqlQuery.list();
		
	}

	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 在库报告书 接口信息（海路通系统）
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> inStockReportInfo(String itemNum, String ctnNum, String realClientName){
		Map<String,Object> params = new HashMap<String, Object>();
		String billCode="";
		if (!StringUtils.isNull(itemNum)) {
			String[] billList=itemNum.split(",");
			for(String billNum:billList){
				billCode+="'"+billNum+"'"+",";
			}
			if(!billCode.equals("")){
				billCode=billCode.substring(0, billCode.length()-1);
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT   ");
		sb.append("  tray.stock_in,  ");
		sb.append("  c.REAL_CLIENT_NAME AS client_name, ");
		sb.append("  (CASE st.IF_BONDED WHEN '1' THEN '保税货物' ELSE '非保税货物' END ) AS is_bonded, ");
		sb.append("  tray.bill_num, ");
		sb.append("  tray.ctn_num, ");
		sb.append("  tray.sku_id, ");
		sb.append("  tray.CARGO_NAME, ");
		sb.append("  tray.rkTime AS inbound_date, ");
		sb.append("  tray.enter_state, ");
		sb.append("  SUM (tray.num) AS NOW_PIECE_SUM, ");
		sb.append("  ROUND (SUM(tray.net_weight), 2) AS net_weight_sum, ");
		sb.append("  ROUND (SUM(tray.gross_weight), 2) AS gross_weight_sum, ");
		sb.append("  ROUND (MAX(s.net_single), 2) AS net_single, ");
		sb.append("  ROUND (MAX(s.gross_single), 2) AS gross_single, ");
		sb.append("  s.class_name, ");
		sb.append("  s.class_type  ");
		sb.append(" FROM(   ");
		sb.append("   SELECT ");
		sb.append(" 	tray.stock_in,     ");
		sb.append(" 	tray.stock_name,   ");
		sb.append(" 	tray.bill_num,     ");
		sb.append(" 	tray.ctn_num,      ");
		sb.append("     tray.sku_id,       ");
		sb.append("     tray.asn,          ");
		sb.append(" 	SUM (tray.now_piece) AS num, ");
		sb.append(" 	( ");
		sb.append(" 	CASE tray.enter_state    ");
		sb.append(" 	WHEN '0' THEN ");
		sb.append(" 	'INTACT'      ");
		sb.append(" 	WHEN '1' THEN ");
		sb.append(" 	'BROKEN'      ");
		sb.append(" 	WHEN '2' THEN ");
		sb.append(" 	'COVER TORN'  ");
		sb.append(" 	END    ");
		sb.append("    ) AS enter_state,  ");
		sb.append("    tray.CARGO_NAME,              ");
		sb.append("    SUM (tray.net_weight) AS net_weight, ");
		sb.append("    SUM (tray.gross_weight) AS gross_weight, ");
		sb.append("    tray.CONTACT_NUM, ");
		sb.append("    TO_CHAR (tray.ENTER_STOCK_TIME,'yyyy-MM-dd') AS rkTime,");
		sb.append("    tray.cargo_location  ");
		sb.append("  FROM  ");
		sb.append("    BIS_TRAY_INFO tray ");
		sb.append("  WHERE 1 = 1 AND (tray.cargo_state = '01' OR tray.cargo_state = '10') ");
		sb.append("  AND tray.now_piece != 0  ");
		sb.append("  GROUP BY  ");
		sb.append("   tray.stock_in,          ");
		sb.append("   tray.stock_name,        ");
		sb.append("   tray.bill_num,          ");
		sb.append("   tray.ctn_num,           ");
		sb.append("   tray.asn,               ");
		sb.append("   tray.sku_id,            ");
		sb.append("   tray.enter_state,       ");
		sb.append("   tray.CARGO_NAME,        ");
		sb.append("   tray.CONTACT_NUM,       ");
		sb.append("   tray.enter_state,       ");
		sb.append("   TO_CHAR (tray.ENTER_STOCK_TIME,'yyyy-MM-dd'),  ");
		sb.append("   tray.cargo_location ");
		sb.append("  ) tray  ");
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
		sb.append(" ) ba ON (tray.asn = ba.asn AND ba.ctn_num = tray.ctn_num AND ba.bill_num = tray.bill_num)        ");
		sb.append(" LEFT JOIN bis_asn_info ai ON (ai.asn_id =tray.asn AND ai.sku_id=tray.sku_id)                 ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK st ON (           ");
		sb.append(" 	tray.bill_num = st.ITEM_NUM             ");
		sb.append(" 	AND tray.CONTACT_NUM = st.LINK_ID       ");
		sb.append(" )      ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK_INFO info ON (    ");
		sb.append(" 	st.ITEM_NUM = info.ITEM_NUM             ");
		sb.append(" 	AND st.LINK_ID = info.LINK_ID           ");
		sb.append(" 	AND tray.ctn_num = info.ctn_num         ");
		sb.append(" 	AND tray.sku_id = info.sku              ");
		sb.append(" )     ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = tray.sku_id    ");
		sb.append(" LEFT JOIN base_client_info c ON c.ids = tray.stock_in     ");
		sb.append(" WHERE 1 = 1 ");

		if (!StringUtils.isNull(itemNum)) {
			sb.append(" AND tray.bill_num in ("+billCode+")   ");
		}
		if (!StringUtils.isNull(ctnNum)) {
			sb.append(" AND tray.ctn_num=:ctnCode1 ");
			params.put("ctnCode1", ctnNum);
		}
		if (realClientName != null && !"".equals(realClientName)) {//--客户名称
			sb.append(" and c.REAL_CLIENT_NAME=:sockid  ");
			params.put("sockid", realClientName);
		}
		sb.append(" GROUP BY   ");
		sb.append("  tray.stock_in, ");
		sb.append("  c.REAL_CLIENT_NAME, ");
		sb.append("  st.IF_BONDED, ");
		sb.append("  tray.bill_num, ");
		sb.append("  tray.ctn_num,  ");
		sb.append("  tray.sku_id,  ");
		sb.append("  s.class_name, ");
		sb.append("  s.class_type, ");
		sb.append("  tray.CARGO_NAME, ");
		sb.append("  tray.rkTime,  ");
		sb.append("  tray.enter_state  ");
		sb.append(" ORDER BY                                                                   ");
		sb.append(" 	tray.bill_num,  ");
		sb.append(" 	tray.ctn_num,   ");
		sb.append(" 	tray.rkTime  ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
    
}
