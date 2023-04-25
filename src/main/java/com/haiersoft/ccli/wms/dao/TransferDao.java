package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisTransferStock;

@Repository
public class TransferDao  extends HibernateDao<BisTransferStock, String> {
	/**
	 * 获取用户仓库内货品数量
	 * @param userid 客户id
	 * @param ckId 仓库id
	 * @param skus sku集合
	 * @param bills 提单集合
	 * @param cuns 厢号集合
	 * @param ents 货物状态集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getUserStockNum(String userid,String ckId,String skus,String bills,String cuns,String ents){
		if(userid!=null && !"".equals(userid) && ckId!=null && !"".equals(ckId)){
			StringBuffer sb=new StringBuffer();
			HashMap<String,Object> parme=new HashMap<String,Object>();
			sb.append(" select t.sku_id,t.bill_num,t.ctn_num,t.enter_state,sum(now_piece) as piece from bis_tray_info t where t.stock_in=:userid and t.warehouse_id=:ckid   ");
			parme.put("ckid", ckId);
			parme.put("userid", userid);
			
			if(skus!=null && !"".equals(skus) ){
				sb.append("  and  t.sku_id in(").append(skus).append(") ");
			}
			if(bills!=null && !"".equals(bills) ){
				sb.append("  and  t.bill_num in(").append(bills).append(") ");
			}
			if(cuns!=null && !"".equals(cuns) ){
				sb.append("  and  t.ctn_num in(").append(cuns).append(") ");
			}
			if(ents!=null && !"".equals(ents) ){
				sb.append("  and  t.enter_state in(").append(ents).append(") ");
			}
			sb.append("  group by t.sku_id,t.bill_num,t.ctn_num,t.enter_state  ");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	
	/**
	 * 货转出库报告书--普通客户
	 * @param transferNum 货转单号
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotPT(String ifBonded,String transferNum){
		List<Map<String,Object>> getList=null;
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT    ");
		sb.append("	t.cargo_name, ");
		sb.append("	t.bill_num,");
		sb.append("	t.ctn_num, ");
		sb.append("	t.sku_id,");
		sb.append("	t.OPERATE_TIME, ");
		sb.append("	m.start_store_date, ");
		sb.append("	m.receiver_name,");
		sb.append("	m.stock_in, ");
		sb.append("	(     ");
		sb.append("		CASE t.enter_state  ");
		sb.append("		WHEN '0' THEN  ");
		sb.append("			'INTACT'   ");
		sb.append("		WHEN '1' THEN  ");
		sb.append("			'BROKEN'   ");
		sb.append("		WHEN '2' THEN  ");
		sb.append("			'COVER TORN' ");
		sb.append("		END  ");
		sb.append("	) AS enter_state,  ");
		sb.append("	MAX(t.piece) AS piece,  ");
		sb.append("	round(MAX(t.NET_WEIGHT), 2) AS NET_WEIGHT, ");
		sb.append("	round(MAX(t.GROSS_WEIGHT), 2) AS GROSS_WEIGHT ");
		sb.append("FROM   ");
		sb.append("	BIS_TRANSFER_STOCK_INFO t  ");
		sb.append("LEFT JOIN BIS_TRANSFER_STOCK m ON t.transfer_link_id = m.transfer_id      ");
		sb.append("left join bis_asn a  ");
		sb.append("  on (t.bill_num = a.bill_num and t.ctn_num = a.ctn_num)                  ");
		sb.append("WHERE 1 = 1    ");
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
		if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND a.is_bonded='"+ifBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
		sb.append("GROUP BY    "); 
		sb.append("	t.cargo_name,");
		sb.append("	t.bill_num,");
		sb.append("	t.ctn_num,");
		sb.append("	t.sku_id,");
		sb.append("	t.OPERATE_TIME,");
		sb.append("	m.start_store_date,");
		sb.append("	m.receiver_name,");
		sb.append("	m.stock_in,");
		sb.append("	t.enter_state ");
		sb.append(" order by t.bill_num,t.ctn_num,t.operate_time ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * 货转出库报告书--JP客户
	 * @param itemNum 货转单号
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotJP(String ifBonded,String transferNum){
		List<Map<String,Object>> getList=null;
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT        ");
		sb.append(" s.rkdh,       ");
		sb.append(" t.cargo_name, ");
		sb.append(" t.bill_num,   ");
		sb.append(" t.ctn_num,    ");
		sb.append(" t.sku_id,     ");
		sb.append(" t.OPERATE_TIME,     ");
		sb.append(" m.start_store_date, ");
		sb.append(" m.receiver_name,    ");
		sb.append(" m.stock_in,         ");
		sb.append(" (                   ");
		sb.append(" 	CASE t.enter_state  ");
		sb.append(" 	WHEN '0' THEN       ");
		sb.append(" 		'INTACT'          ");
		sb.append(" 	WHEN '1' THEN       ");
		sb.append(" 		'BROKEN'          ");
		sb.append(" 	WHEN '2' THEN       ");
		sb.append(" 		'COVER TORN'      ");
		sb.append(" 	END                 ");
		sb.append(" ) AS enter_state,     ");
		sb.append(" MAX(t.piece) piece,   ");
		sb.append(" round(MAX(t.NET_WEIGHT), 2) AS NET_WEIGHT,    ");
		sb.append(" round(MAX(t.GROSS_WEIGHT), 2) AS GROSS_WEIGHT ");
		sb.append(" FROM                                          ");
		sb.append(" 	BIS_TRANSFER_STOCK_INFO t                   ");
		sb.append(" LEFT JOIN base_sku_base_info s ON s.SKU_ID = t.sku_id                    ");
		sb.append(" LEFT JOIN BIS_TRANSFER_STOCK m ON t.transfer_link_id = m.transfer_id     ");
		sb.append(" LEFT JOIN bis_asn a       ");
		sb.append("   on (t.bill_num = a.bill_num and t.ctn_num = a.ctn_num) ");
		sb.append(" WHERE 1 = 1     ");
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
		if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND a.is_bonded='"+ifBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
	    sb.append("GROUP BY            "); 
	    sb.append(" s.rkdh,            ");
		sb.append(" t.cargo_name,      ");
		sb.append(" t.bill_num,        ");
		sb.append(" t.ctn_num,         ");
		sb.append(" t.sku_id,          ");
		sb.append(" t.OPERATE_TIME,    ");
		sb.append(" m.start_store_date,");
		sb.append(" m.receiver_name,   ");
		sb.append(" m.stock_in,        ");
		sb.append(" t.enter_state      ");
		sb.append(" order by t.bill_num,t.ctn_num,t.operate_time ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 货转出库报告书--OTE客户
	 * @param itemNum 货转单号
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotOTE(String ifBonded,String transferNum){
		List<Map<String,Object>> getList=null;
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT   ");
		sb.append("  t.cargo_name, ");
		sb.append("  t.bill_num, ");
		sb.append("  t.ctn_num, ");
		sb.append("  t.sku_id,  ");
		sb.append("  k.type_size, ");
		sb.append("  k.PRO_NUM,");
		sb.append("  e.SHIP_NUM, ");
		sb.append("  k.LOT_NUM, ");
		sb.append("  k.MSC_NUM, ");
		sb.append("  e.MAKE_TIME, ");
		sb.append("  t.OPERATE_TIME,  ");
		sb.append("  m.start_store_date, ");
		sb.append("  m.receiver_name, ");
		sb.append("  m.stock_in,  ");
		sb.append("  (   ");
		sb.append("    CASE t.enter_state ");
		sb.append("    WHEN '0' THEN ");
		sb.append("      'INTACT'  ");
		sb.append("    WHEN '1' THEN ");
		sb.append("      'BROKEN' ");
		sb.append("    WHEN '2' THEN  ");
		sb.append("      'COVER TORN' ");
		sb.append("    END ");
		sb.append("  ) AS enter_state, ");
		sb.append("  MAX(t.piece) AS piece,  ");
		sb.append("  round(MAX(t.NET_WEIGHT), 2) AS NET_WEIGHT,  ");
		sb.append("  round(MAX(t.GROSS_WEIGHT), 2) AS GROSS_WEIGHT, ");
		sb.append("  e.ORDER_NUM  ");
		sb.append("FROM    ");
		sb.append("  BIS_TRANSFER_STOCK_INFO t  ");
		sb.append("LEFT JOIN bis_enter_stock_info e ON t.bill_num = e.ITEM_NUM ");
		sb.append("AND t.sku_id = e.sku   ");
		sb.append("LEFT JOIN base_sku_base_info k ON k.sku_id = t.sku_id  ");
		sb.append("LEFT JOIN BIS_TRANSFER_STOCK m ON t.transfer_link_id = m.transfer_id    ");
		sb.append("LEFT JOIN bis_asn a   ");
		sb.append("  on (t.bill_num = a.bill_num and t.ctn_num = a.ctn_num)  ");
		sb.append("WHERE     ");
		sb.append("  1 = 1    ");
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
		if(null!=ifBonded&&!"".equals(ifBonded)){
        	if("1".equals(ifBonded)){
        		sb.append(" AND a.is_bonded='"+ifBonded+"'");
        	}else{
        		sb.append(" AND (a.is_bonded ='0' or a.is_bonded is null)    ");
        	}
        }
		sb.append(" GROUP BY    "); 
		sb.append("  t.cargo_name,  ");
		sb.append("  t.bill_num,  ");
		sb.append("  t.ctn_num, ");
		sb.append("  t.sku_id, ");
		sb.append("  k.type_size,  ");
		sb.append("  k.PRO_NUM,   ");
		sb.append("  e.SHIP_NUM,  ");
		sb.append("  k.LOT_NUM,  ");
		sb.append("  k.MSC_NUM,   ");
		sb.append("  e.MAKE_TIME, ");
		sb.append("  t.OPERATE_TIME,  ");
		sb.append("  m.start_store_date, ");
		sb.append("  m.receiver_name,");
		sb.append("  m.stock_in,  ");
		sb.append("  t.enter_state,  ");
		sb.append("  e.ORDER_NUM   ");
		sb.append(" order by t.bill_num,t.ctn_num,t.operate_time ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString());
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDisObj(String transferId) {
		List<Map<String,Object>> getList=null;
		StringBuffer sql=new StringBuffer();
		sql.append(" select * ");
		sql.append(" from bis_dismantle_tray d ");
		sql.append(" where d.old_tray_code in ( "
				+ " select t.stocknum from bis_transfer_stock_trayinfo t where t.transfer_link_id=?0 and t.OPERATE_TIME<d.DISMANTLE_TIME) " );
		SQLQuery sqlQuery=createSQLQuery(sql.toString(),transferId);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
}
