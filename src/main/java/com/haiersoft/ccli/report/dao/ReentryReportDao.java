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
 * @author yaohn 
 * @ClassName: ReentryReportDao
 * @Description: 重收报告书 DAO
 * @date 2017年8月24日 上午11:33:17
 */
@Repository
public class ReentryReportDao extends HibernateDao<Stock, String>{
	
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收报告书--普通
	 * @date 2017年8月24日 上午11:33:17
	 * @param stock
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchReentryReport(Stock stock){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
	    sb.append(" SELECT  ");  
			sb.append(" c.client_name,");
			sb.append(" s.cargo_name, ");
			sb.append(" aa.is_bonded, ");
			sb.append(" aa.sku_id,    ");
			sb.append(" aa.bill_num,  ");
			sb.append(" TO_CHAR (aa.inbound_date,'yyyy-mm-dd') AS inbound_date, ");
			sb.append(" aa.stock_in,    ");
			sb.append(" aa.ctn_num,     ");
			sb.append(" aa.enter_state, ");
			sb.append(" aa.original_piece_sum, ");
			sb.append(" round(aa.original_piece_sum * s.net_single,2) AS net_weight_sum,");
			sb.append(" round(aa.original_piece_sum * s.gross_single,2) AS gross_weight_sum ");
			sb.append(" FROM                      ");
			sb.append("(                       ");
			sb.append("	SELECT                ");
			sb.append("		asnt.inbound_date,  ");
			sb.append("		asnt.is_bonded,     ");
			sb.append("		t.stock_in,         ");
			sb.append("		t.bill_num,         ");
			sb.append("		t.ctn_num,          ");
			sb.append("		t.sku_id,           ");
			sb.append("		(                   ");
			sb.append("			CASE t.enter_state ");
			sb.append("			WHEN '0' THEN      ");
			sb.append("				'INTACT'         ");
			sb.append("			WHEN '1' THEN      ");
			sb.append("				'BROKEN'         ");
			sb.append("			WHEN '2' THEN      ");
			sb.append("				'COVER TORN'     ");
			sb.append("			END                ");
			sb.append("		) AS enter_state,    ");
			sb.append("		SUM(t.original_piece) AS original_piece_sum  ");
			sb.append("	FROM                   ");
			sb.append(" (                      ");
			sb.append(" 	SELECT               ");
			sb.append(" 		a.asn,             ");
			sb.append(" 		a.is_bonded,       ");
			sb.append(" 		a.inbound_date,    ");
			sb.append(" 		a.ctn_num,         ");
			sb.append(" 		a.bill_num,        ");
			sb.append(" 		a.stock_in         ");
			sb.append(" 	FROM                 ");
			sb.append(" 		bis_asn a          ");
			sb.append(" 	WHERE                ");
			sb.append(" 		a.if_second_enter = '2' ");
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND a.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND a.bill_num = :billCode ");
			params.put("billCode", stock.getBillCode());
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND a.ctn_num = :ctnNum ");
			params.put("ctnNum", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND a.stock_in = :clientId ");
			params.put("clientId", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND a.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND a.inbound_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" ) asnt   ");
		sb.append(" INNER JOIN (  ");
		sb.append(" SELECT   ");
		sb.append(" sku_id,  ");
		sb.append(" asn,   ");
		sb.append(" bill_num,  ");
		sb.append(" stock_in,  ");
		sb.append(" ctn_num,");
		sb.append(" enter_state,  ");
		sb.append(" original_piece  ");
		sb.append(" FROM    ");
		sb.append(" BIS_TRAY_INFO  ");
		sb.append(" ) t ON t.asn = asnt.asn  ");
		sb.append(" GROUP BY   ");
		sb.append(" t.stock_in, ");
		sb.append(" t.bill_num, ");
		sb.append(" t.ctn_num,  ");
		sb.append(" t.sku_id,   ");
		sb.append(" t.enter_state, ");
		sb.append(" asnt.is_bonded,  ");
		sb.append(" asnt.inbound_date  ");
		sb.append(" ) aa      ");
		sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_in            ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id         ");
		sb.append(" ORDER BY   ");
		sb.append(" 	aa.bill_num,");
		sb.append(" 	aa.ctn_num  ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收报告书--日本
	 * @date 2017年8月24日 上午11:33:17
	 * @param stock
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchReentryReportRB(Stock stock){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT  ");
		sb.append("   c.client_name,");
		sb.append("   aa.rk_num,");
		sb.append("   aa.is_bonded,");
		sb.append("   s.cargo_name,");
		sb.append("   aa.sku_id, ");
		sb.append("   aa.bill_num, ");
		sb.append("   TO_CHAR (aa.inbound_date,'yyyy-mm-dd') AS inbound_date,");
		sb.append("   aa.stock_in, ");
		sb.append("   aa.ctn_num,  ");
		sb.append("   aa.enter_state,");
		sb.append("   aa.original_piece_sum,");
		sb.append("   round(aa.original_piece_sum * s.net_single,2 ) AS net_weight_sum,");
		sb.append("   round(aa.original_piece_sum * s.gross_single,2) AS gross_weight_sum ");
		sb.append(" FROM  ");
		sb.append("   (  ");
		sb.append("  SELECT    ");
		sb.append("   ai.rk_num, ");
		sb.append("   asnt.is_bonded, ");
		sb.append("   asnt.inbound_date,  ");
		sb.append("   t.stock_in, ");
		sb.append("   t.bill_num, ");
		sb.append("   t.ctn_num, ");
		sb.append("   t.sku_id, ");
		sb.append("   (    ");
		sb.append("    CASE t.enter_state  ");
		sb.append("    WHEN '0' THEN  ");
		sb.append("    'INTACT'      ");
		sb.append("    WHEN '1' THEN  ");
		sb.append("    'BROKEN'       ");
		sb.append("    WHEN '2' THEN ");
		sb.append("    'COVER TORN'   ");
		sb.append("     END       ");
		sb.append("   ) AS enter_state,   ");
		sb.append("   SUM(t.original_piece) AS original_piece_sum ");
		sb.append("  FROM (    ");
		sb.append("  SELECT   ");
		sb.append("   a.asn,  ");
		sb.append("   a.is_bonded,  ");
		sb.append("   a.inbound_date,");
		sb.append("   a.ctn_num,  ");
		sb.append("   a.bill_num, ");
		sb.append("   a.stock_in  ");
		sb.append("  FROM  ");
		sb.append("   bis_asn a ");
		sb.append("  WHERE  ");
		sb.append("  a.if_second_enter = '2'  ");
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND a.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND a.bill_num = :billCode ");
			params.put("billCode", stock.getBillCode());
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND a.ctn_num = :ctnNum ");
			params.put("ctnNum", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND a.stock_in = :clientId ");
			params.put("clientId", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND a.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND a.inbound_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append(" ) asnt   ");
		sb.append(" LEFT JOIN bis_asn_info ai ON ai.asn_id = asnt.asn ");
		sb.append(" INNER JOIN (     ");
		sb.append("  SELECT  ");
		sb.append("  sku_id, ");
		sb.append("  asn,      ");
		sb.append("  bill_num,  ");
		sb.append("  stock_in,   ");
		sb.append("  ctn_num,     ");
		sb.append("  enter_state,   ");
		sb.append("  original_piece  ");
		sb.append(" FROM           ");
		sb.append("  BIS_TRAY_INFO     ");
		sb.append(" ) t ON t.asn = asnt.asn ");
		sb.append(" AND ai.sku_id = t.sku_id  ");
		sb.append(" GROUP BY   ");
		sb.append("   ai.rk_num,  ");
		sb.append("   t.stock_in, ");
		sb.append("   t.bill_num,  ");
		sb.append("   t.ctn_num,  ");
		sb.append("   t.sku_id,   ");
		sb.append("   asnt.is_bonded, ");
		sb.append("   asnt.inbound_date,");
		sb.append("   t.enter_state  ");
		sb.append("  ) aa  ");
		sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
		sb.append(" ORDER BY   ");
		sb.append("   aa.bill_num,");
		sb.append("   aa.ctn_num, ");
		sb.append("   aa.rk_num   ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收报告书--OTC
	 * @date 2017年8月24日 上午11:33:17
	 * @param stock
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchReentryReportOTE(Stock stock){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT  ");
		sb.append("  c.client_name, ");
		sb.append("  s.cargo_name, ");
		sb.append("  aa.is_bonded,");
		sb.append("  aa.sku_id,  ");
		sb.append("  aa.bill_num, ");
		sb.append("  TO_CHAR (aa.inbound_date,'yyyy-mm-dd') AS inbound_date,");
		sb.append("  aa.stock_in, ");
		sb.append("  aa.ctn_num, ");
		sb.append("  aa.pro_time,  ");
		sb.append("  aa.enter_state,");
		sb.append("  aa.original_piece_sum,");
		sb.append("  round(aa.original_piece_sum * s.net_single,2) AS net_weight_sum,");
		sb.append("  round(aa.original_piece_sum * s.gross_single,2) AS gross_weight_sum,");
		sb.append("  s.type_size, ");
		sb.append("  s.pro_num,");
		sb.append("  s.lot_num,");
		sb.append("  s.msc_num ");
		sb.append("FROM( ");
		sb.append("   SELECT                                             ");
		sb.append("    ai.pro_time,                                     ");
		sb.append("    asnt.is_bonded,                                  ");
		sb.append("    asnt.inbound_date,                               ");
		sb.append("    t.stock_in,                                      ");
		sb.append("    t.bill_num,                                      ");
		sb.append("    t.ctn_num,                                       ");
		sb.append("    t.sku_id,                                        ");
		sb.append("    (                                                ");
		sb.append("        CASE t.enter_state                             ");
		sb.append("        WHEN '0' THEN                                  ");
		sb.append("          'INTACT'                                     ");
		sb.append("        WHEN '1' THEN                                  ");
		sb.append("          'BROKEN'                                     ");
		sb.append("        WHEN '2' THEN                                  ");
		sb.append("          'COVER TORN'                                 ");
		sb.append("        END                                            ");
		sb.append("      ) AS enter_state,                                ");
		sb.append("      SUM(t.original_piece) AS original_piece_sum      ");
		sb.append("    FROM                                               ");
		sb.append("      (                                                ");
		sb.append("        SELECT                                         ");
		sb.append("          a.asn,                                       ");
		sb.append("          a.is_bonded,                                 ");
		sb.append("          a.inbound_date,                              ");
		sb.append("          a.ctn_num,                                   ");
		sb.append("          a.bill_num,                                  ");
		sb.append("          a.stock_in                                   ");
		sb.append("        FROM                                           ");
		sb.append("          bis_asn a                                    ");
		sb.append("        WHERE                                          ");
		sb.append("          a.if_second_enter = '2'                      ");
		if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND a.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
		if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND a.bill_num = :billCode ");
			params.put("billCode", stock.getBillCode());
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND a.ctn_num = :ctnNum ");
			params.put("ctnNum", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND a.stock_in = :clientId ");
			params.put("clientId", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND a.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND a.inbound_date < to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
		sb.append("      ) asnt                                           ");
		sb.append("    LEFT JOIN bis_asn_info ai ON ai.asn_id = asnt.asn  ");
		sb.append("  INNER JOIN (   ");
		sb.append("   SELECT    ");
		sb.append("    sku_id,  ");
		sb.append("    asn,     ");
		sb.append("    bill_num, ");
		sb.append("    stock_in,  ");
		sb.append("    ctn_num,  ");
		sb.append("    enter_state, ");
		sb.append("    original_piece  ");
		sb.append("   FROM    ");
		sb.append("    BIS_TRAY_INFO  ");
		sb.append("  ) t ON t.asn = asnt.asn   ");
		sb.append("   AND ai.sku_id = t.sku_id ");
		sb.append(" GROUP BY  ");
		sb.append("   t.bill_num, ");
		sb.append("   t.ctn_num, ");
		sb.append("   t.sku_id, ");
		sb.append("   ai.pro_time, ");
		sb.append("   asnt.is_bonded, ");
		sb.append("   asnt.inbound_date, ");
		sb.append("   t.enter_state, ");
		sb.append("   t.stock_in ");
		sb.append("  ) aa  ");
		sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
		sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
		sb.append("ORDER BY  ");
		sb.append("  aa.bill_num,");
		sb.append("  aa.ctn_num,");
		sb.append("  aa.pro_time ");
		SQLQuery sqlQuery = createSQLQuery(sb.toString(), params);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
	/**
	 * 重收报告书 页面数据查询
	 * @param page
	 * @param stock
	 * @return
	 */
    public Page<Stock> getStocks(Page<Stock> page, Stock stock) {
    	Map<String,Object> params = new HashMap<String, Object>();
    	StringBuffer sb=new StringBuffer();
        sb.append("SELECT ");
      	sb.append("	c.client_name AS clientName, ");
      	sb.append("	s.cargo_name AS cargoName,   ");
      	sb.append("	aa.is_bonded AS isBonded,    ");
      	sb.append("	aa.sku_id AS sku,            ");
      	sb.append("	aa.bill_num AS billCode,     ");
      	sb.append("	aa.inbound_date AS enterTime,");
      	sb.append("	aa.stock_in AS clientId,     ");
      	sb.append("	aa.ctn_num AS ctnNum,        ");
      	sb.append("	aa.enter_state AS state,     ");
      	sb.append("	aa.original_piece_sum AS nowNum, ");
      	sb.append("	(          ");
      	sb.append("		aa.original_piece_sum * s.net_single   ");
      	sb.append("	) AS allnet,  ");
      	sb.append("	(             ");
      	sb.append("		aa.original_piece_sum * s.gross_single ");
      	sb.append("	) AS allgross    ");
      	sb.append("FROM                       ");
      	sb.append("	(                         ");
      	sb.append("		SELECT                  ");
      	sb.append("			asnt.inbound_date,    ");
      	sb.append("			asnt.is_bonded,       ");
      	sb.append("			t.stock_in,           ");
      	sb.append("			t.bill_num,           ");
      	sb.append("			t.ctn_num,            ");
      	sb.append("			t.sku_id,             ");
      	sb.append("			(                     ");
      	sb.append("				CASE t.enter_state  ");
      	sb.append("				WHEN '0' THEN       ");
      	sb.append("					'INTACT'          ");
      	sb.append("				WHEN '1' THEN       ");
      	sb.append("					'BROKEN'          ");
      	sb.append("				WHEN '2' THEN       ");
      	sb.append("					'COVER TORN'      ");
      	sb.append("				END                 ");
      	sb.append("			) AS enter_state,     ");
      	sb.append("			SUM(t.original_piece) AS original_piece_sum ");
      	sb.append("		FROM                  ");
      	sb.append("			(                   ");
      	sb.append("				SELECT            ");
      	sb.append("					a.asn,          ");
      	sb.append("					a.is_bonded,    ");
      	sb.append("					a.inbound_date, ");
      	sb.append("					a.ctn_num,      ");
      	sb.append("					a.bill_num,     ");
      	sb.append("					a.stock_in      ");
      	sb.append("				FROM              ");
      	sb.append("					bis_asn a       ");
      	sb.append("				WHERE             ");
      	sb.append("					a.if_second_enter = '2' ");
    	if(null!=stock.getIsBonded()&&!"".equals(stock.getIsBonded())){
        	if("1".equals(stock.getIsBonded())){
        		sb.append(" AND a.is_bonded='"+stock.getIsBonded()+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
    	if(!StringUtils.isNull(stock.getBillCode())){
			sb.append(" AND a.bill_num = :billCode ");
			params.put("billCode", stock.getBillCode());
		}
		if(!StringUtils.isNull(stock.getCtnNum())){
			sb.append(" AND a.ctn_num = :ctnNum ");
			params.put("ctnNum", stock.getCtnNum());
		}
		if(!StringUtils.isNull(stock.getClientId())){
			sb.append(" AND a.stock_in = :clientId ");
			params.put("clientId", stock.getClientId());
		}
		if(!StringUtils.isNull(stock.getStrartTime())){
			sb.append(" AND a.inbound_date >= to_date(:strartTime, 'yyyy-mm-dd') ");
			params.put("strartTime", stock.getStrartTime());
		}
		if(!StringUtils.isNull(stock.getEndTime())){
			sb.append(" AND a.inbound_date <= to_date(:endTime, 'yyyy-mm-dd')+1 ");
			params.put("endTime", stock.getEndTime());
		}
    	sb.append("	) asnt  ");
    	sb.append("	INNER JOIN ( ");
    	sb.append("	SELECT   ");
    	sb.append("	sku_id,   ");
    	sb.append("	asn,      ");
    	sb.append("	bill_num, ");
    	sb.append("	stock_in,  ");
    	sb.append("	ctn_num,  ");
    	sb.append("	enter_state,  ");
    	sb.append("	original_piece  ");
    	sb.append("	FROM      ");
    	sb.append("	 BIS_TRAY_INFO   ");
    	sb.append("	) t ON t.asn = asnt.asn  ");
    	sb.append("	GROUP BY    ");
    	sb.append("	 t.stock_in, ");
    	sb.append("	 t.bill_num, ");
    	sb.append("	 t.ctn_num,  ");
    	sb.append("	 t.sku_id,    ");
    	sb.append("	 t.enter_state, ");
    	sb.append("	 asnt.is_bonded,");
    	sb.append("	 asnt.inbound_date ");
    	sb.append("	) aa      ");
    	sb.append("LEFT JOIN base_client_info c ON c.ids = aa.stock_in    ");
    	sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id ");
    	sb.append("ORDER BY      ");
    	sb.append("	aa.bill_num, ");
    	sb.append("	aa.ctn_num   ");
        Map<String, Object> paramType = new HashMap<>();
        paramType.put("clientName", String.class);
        paramType.put("clientId", String.class);//
        paramType.put("isBonded", String.class);//是否保税
        paramType.put("billCode", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("sku", String.class);
        paramType.put("cargoName", String.class);
        paramType.put("enterTime", Date.class);
        paramType.put("state", String.class);
        paramType.put("nowNum", Integer.class);
        paramType.put("allnet", Double.class);
        paramType.put("allgross", Double.class);
        return findPageSql(page, sb.toString(), paramType, params);
    }
}
