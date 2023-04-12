package com.haiersoft.ccli.wms.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisOutStock;
/**
 * 
 * @author pyl
 * @ClassName: OutStockDao
 * @Description: 出库联系单DAO
 * @date 2016年3月11日 下午3:52:06
 */
@Repository
public class OutStockDao extends HibernateDao<BisOutStock, String>{
	/**
	 * 计算抄码费用调整
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getFeeCodeList(String out_link_id){
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                                                   "); 
		sb.append(" 	outstock.OUT_LINK_ID,                                                ");
		sb.append(" 	outstock.BILL_NUM,                                                   ");
		sb.append(" 	SUM (outstock.OUT_NUM) AS PIECE,                                     ");
		sb.append(" 	SUM (outstock.code_num) AS CODE_NUM,                                 ");
		sb.append(" 	enter.FEE_ID,                                                        ");
		sb.append("     max(fee.unit) AS UNIT,                                               ");
		sb.append("     sum(outstock.code_num*fee.unit) AS AMOUNT                            ");
		sb.append(" FROM                                                                     ");
		sb.append(" 	BIS_OUT_STOCK_INFO outstock                                          ");
		sb.append(" LEFT JOIN BIS_ENTER_STOCK enter ON outstock.BILL_NUM = enter.ITEM_NUM    ");
		sb.append(" LEFT JOIN (                                                              ");
		sb.append(" 	SELECT                                                               ");
		sb.append(" 		SCHEME_NUM,                                                      ");
		sb.append(" 		FEE_CODE,                                                        ");
		sb.append(" 		MAX (UNIT) AS unit                                               ");
		sb.append(" 	FROM                                                                 ");
		sb.append(" 		BASE_EXPENSE_SCHEME_INFO                                         ");
		sb.append(" 	WHERE                                                                ");
		sb.append(" 	  FEE_CODE = 'cmf'                                                   ");
		sb.append(" 	GROUP BY                                                             ");
		sb.append(" 		SCHEME_NUM,                                                      ");
		sb.append(" 		FEE_CODE                                                         ");
		sb.append(" ) fee                                                                    ");
		sb.append(" ON                                                                       ");
		sb.append("  enter.FEE_ID=fee.SCHEME_NUM                                             ");
		sb.append(" WHERE                                                                    ");
		sb.append(" 	outstock.OUT_LINK_ID =:out_link_id                                   ");
		sb.append(" GROUP BY                                                                 ");
		sb.append(" 	outstock.OUT_LINK_ID,                                                ");
		sb.append(" 	outstock.BILL_NUM,                                                   ");
		sb.append(" 	enter.FEE_ID                                                         ");
		HashMap<String,Object> parme=new HashMap<String,Object>();
		parme.put("out_link_id", out_link_id);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 按条件查询出库联系单信息
	 * @param outNum 出库联系单id
	 * @param stockIn 存货方客户id
	 * @param receiverId 收货方客户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String,Object>> findList(String outNum,String stockIn,String receiverId){
		StringBuffer sb=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		if(outNum!=null && !"".equals(outNum) ){
			sb.append("select out_link_id,RECEIVER,RECEIVER_ID,RECEIVER_LINKER,STOCK_IN_ID,STOCK_IN,o.OPERATOR,OPERATE_TIME,IF_CLEAR_STORE  from bis_out_stock o where o.out_link_id=:outid");
			parme.put("outid", outNum);
		}else{
			if((stockIn!=null && !"".equals(stockIn)) || (receiverId!=null && !"".equals(receiverId))){
				sb.append("select out_link_id,RECEIVER,RECEIVER_ID,RECEIVER_LINKER,STOCK_IN_ID,STOCK_IN,o.OPERATOR,OPERATE_TIME,IF_CLEAR_STORE  from bis_out_stock o where 1=1 ");
				if(!"".equals(stockIn)){
					sb.append(" and STOCK_IN_ID=:stockIn");
					parme.put("stockIn", stockIn);
				}
				if(!"".equals(receiverId)){
					sb.append(" and RECEIVER_ID=:receiverId");
					parme.put("receiverId", receiverId);
				}
			}
		}
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * 出库报告书--普通客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotPT(String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded){

	    List<Map<String,Object>> getList=null;
	    StringBuffer sb=new StringBuffer();
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
	    HashMap<String,Object> parme=new HashMap<String,Object>();
	    sb.append("SELECT                                                          ");
	    sb.append("	aa.stock_name,                                                 ");
	    sb.append("	s.cargo_name,                                                  ");
	    sb.append("	aa.bill_num,                                                   ");
	    sb.append("	aa.ctn_num,                                                    ");
	    sb.append("	aa.sku_id,                                                     ");
		sb.append("	aa.loading_time,                                               ");
		sb.append("	aa.loading_truck_num,                                          ");
		sb.append("	aa.stock_id,                                                   ");
		sb.append("	aa.enter_state,                                                ");
		sb.append("	aa.piece_sum,                                                  ");
		sb.append("	round(net_weight_sum, 2) AS net_weight_sum,                    ");
		sb.append("	round(gross_weight_sum, 2) AS gross_weight_sum,                ");
		sb.append("	aa.RECEIVER_NAME                                               ");
		sb.append("FROM                                                            ");
		sb.append("	(                                                              ");
		sb.append("		SELECT                                                       ");
		sb.append("			t.loading_time,                                            ");
		sb.append("			t.loading_truck_num,                                       ");
		sb.append("			lo.stock_name,                                             ");
		sb.append("			t.stock_id,                                                ");
		sb.append("			t.bill_num,                                                ");
		sb.append("			t.ctn_num,                                                 ");
		sb.append("			t.sku_id,                                                  ");
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
		sb.append("			sum(t.piece) AS piece_sum,                                 ");
		sb.append("			sum(t.net_weight) AS net_weight_sum,                       ");
		sb.append("			sum(t.gross_weight) AS gross_weight_sum,                   ");
		sb.append("			MIN(lo.RECEIVER_NAME) AS RECEIVER_NAME                     ");
		sb.append("		FROM                                                         ");
		sb.append("			(                                                          ");
		sb.append("				SELECT                                                   ");
		sb.append("					sku_id,                                                ");
		sb.append("					loading_plan_num,                                      ");
		sb.append("					bill_num,                                              ");
		sb.append("					stock_id,                                              ");
		sb.append("					ctn_num,                                               ");
		sb.append("					enter_state,                                           ");
		sb.append("					li.out_link_id,                                        ");
		sb.append("					li.piece,                                              ");
		sb.append("					li.net_weight,                                         ");
		sb.append("					li.gross_weight,                                       ");
		sb.append("					to_char (li.loading_time, 'YY-MM-DD') AS loading_time, ");
		sb.append("					li.loading_time AS loading_real_time,                  ");
		sb.append("					li.loading_truck_num                                   ");
		sb.append("				FROM                                                       ");
		sb.append("					bis_loading_info li                                    ");
		sb.append("				WHERE                                                      ");
		sb.append("					li.loading_state = '2'                                 ");
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
		sb.append("			) t                                                            ");
		sb.append("		LEFT JOIN (                                                        ");
		sb.append("			SELECT                                                         ");
		sb.append("				out_link_id,                                               ");
		sb.append("				if_bonded                                                  ");
		sb.append("			FROM                                                           ");
		sb.append("				bis_out_stock                                              ");
		sb.append("		) stock ON t.out_link_id = stock.out_link_id                       ");
		sb.append("		LEFT JOIN (                                                        ");
		sb.append("			SELECT                                                     ");
		sb.append("				blo.order_num,                                           ");
		sb.append("				blo.stock_name,                                          ");
		sb.append("				blo.order_state,                                         ");
		sb.append("				RECEIVER_NAME                                            ");
		sb.append("			FROM                                                       ");
		sb.append("				bis_loading_order blo                                    ");
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			  sb.append("  where blo.stock_id =:sockid1 "); 
			  parme.put("sockid1", stockIn);
		}
		sb.append("		) lo ON lo.order_num = t.loading_plan_num                    ");
		sb.append("		WHERE                                                        ");
		sb.append("			lo.order_state = '4'                                       ");
		if(null!=isBonded&&!"".equals(isBonded)){
        	if("1".equals(isBonded)){
        		sb.append(" AND stock.if_bonded='"+isBonded+"'");
        	}else{
        		sb.append(" AND (stock.if_bonded ='0' or stock.if_bonded is null)    ");
        	}
        }
		sb.append("		GROUP BY                                                     ");
		sb.append("			t.stock_id,                                                ");
		sb.append("			t.bill_num,                                                ");
		sb.append("			t.ctn_num,                                                 ");
		sb.append("			t.sku_id,                                                  ");
		sb.append("			t.enter_state,                                             ");
		sb.append("			t.loading_time,                                            ");
		sb.append("			t.loading_truck_num,                                       ");
		sb.append("			lo.stock_name                                              ");
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
		sb.append("	) aa                                                           ");
		sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id          ");
		sb.append("ORDER BY                                                        ");
		sb.append("	aa.bill_num,                                                   ");
		sb.append("	aa.ctn_num                                                     ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
	    return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * 出库报告书--JP客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotJP(String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded){

		List<Map<String,Object>> getList=null;
		StringBuffer sb=new StringBuffer();
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
		HashMap<String,Object> parme=new HashMap<String,Object>();
		sb.append(" SELECT ");
		sb.append(" aa.rk_num, ");
		sb.append(" aa.stock_name, ");
		sb.append(" s.cargo_name, ");
		sb.append(" aa.bill_num, ");
		sb.append(" aa.ctn_num,   ");
		sb.append(" aa.sku_id,");
		sb.append(" aa.loading_time,  ");
		sb.append(" aa.loading_truck_num, ");
		sb.append(" aa.stock_id, ");
		sb.append(" aa.enter_state, ");
		sb.append(" aa.piece_sum,  ");
		sb.append(" round(net_weight_sum, 2) AS net_weight_sum, ");
		sb.append(" round(gross_weight_sum, 2) AS gross_weight_sum, ");
		sb.append(" aa.RECEIVER_NAME   ");
		sb.append(" FROM (   ");
		sb.append(" SELECT   ");
		sb.append(" ai.rkdh AS rk_num,  ");
		sb.append(" t.loading_time, ");
		sb.append(" t.loading_truck_num, ");
		sb.append(" t.stock_id, ");
		sb.append(" lo.stock_name, ");
		sb.append(" t.bill_num, ");
		sb.append(" t.ctn_num, ");
		sb.append(" t.sku_id,  ");
		sb.append(" (   ");
		sb.append(" CASE t.enter_state  ");
		sb.append(" WHEN '0' THEN ");
		sb.append(" 'INTACT'   ");
		sb.append(" WHEN '1' THEN  ");
		sb.append(" 'BROKEN' ");
		sb.append(" WHEN '2' THEN  ");
		sb.append(" 'COVER TORN' ");
		sb.append(" END  ");
		sb.append(" ) AS enter_state, ");
		sb.append(" sum(t.piece) AS piece_sum,");
		sb.append(" sum(t.net_weight) AS net_weight_sum,");
		sb.append(" sum(t.gross_weight) AS gross_weight_sum, ");
		sb.append(" MIN(lo.RECEIVER_NAME) AS RECEIVER_NAME ");
		sb.append(" FROM(  ");
		sb.append(" SELECT  ");
		sb.append(" sku_id, ");
		sb.append(" loading_plan_num,  ");
		sb.append(" bill_num,");
		sb.append(" stock_id, ");
		sb.append(" ctn_num,  ");
		sb.append(" enter_state, ");
		sb.append(" li.out_link_id,");
		sb.append(" li.piece,   ");
		sb.append(" li.net_weight,  ");
		sb.append(" li.gross_weight, ");
		sb.append(" to_char (li.loading_time, 'YY-MM-DD') AS loading_time,         ");
		sb.append(" li.loading_time AS loading_real_time,  ");
		sb.append(" li.loading_truck_num   ");
		sb.append(" FROM     ");
		sb.append(" 	bis_loading_info li  ");
		sb.append(" WHERE    ");
		sb.append("     li.loading_state = '2'  ");
		if(trucknum!=null && !"".equals(trucknum)){//装车单号 
			sb.append(" and  li.loading_truck_num in (" + trucknum + ") ");// --装车单号
		}
	    if(billnum!=null && !"".equals(billnum)){//提单号 
		    sb.append("  and  li.BILL_NUM in (" + billnum + ") ");// --提单号
		}
	    if(cunNum!=null && !"".equals(cunNum)){//--箱号
		    sb.append("  and   li.ctn_num =:ctnnum");//--箱号 
			parme.put("ctnnum", cunNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sb.append("    and  li.stock_id  =:sockid  ");//--客户ID
			parme.put("sockid", stockIn);
		}
		sb.append(" ) t  ");
		sb.append(" LEFT JOIN ( ");
		sb.append(" SELECT      ");
		sb.append(" out_link_id, ");
		sb.append(" if_bonded ");
		sb.append(" FROM     ");
		sb.append(" bis_out_stock   ");
		sb.append(" ) stock ON t.out_link_id = stock.out_link_id   ");
		sb.append(" LEFT JOIN ( ");
		sb.append(" SELECT    ");
		sb.append(" 	blo.order_num,   ");
		sb.append(" 	blo.stock_name, ");
		sb.append(" 	blo.order_state, ");
		sb.append(" 	RECEIVER_NAME  ");
		sb.append(" FROM     ");
		sb.append("     bis_loading_order blo ");
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
		   sb.append(" where blo.stock_id =:sockid1 "); 
		   parme.put("sockid1", stockIn);
		}
		sb.append(" ) lo ON lo.order_num = t.loading_plan_num     ");
		sb.append(" LEFT JOIN base_sku_base_info ai ON ai.sku_id = t.sku_id ");
		sb.append(" WHERE  lo.order_state = '4'   ");
		if(null!=isBonded&&!"".equals(isBonded)){
	       if("1".equals(isBonded)){
	       sb.append(" AND stock.if_bonded='"+isBonded+"'");
	     }else{
	       sb.append(" AND (stock.if_bonded ='0' or stock.if_bonded is null)    ");
	     }
	   }
	   sb.append(" GROUP BY   ");
	   sb.append(" ai.rkdh,  ");
	   sb.append(" t.stock_id, ");
	   sb.append(" t.bill_num, ");
	   sb.append(" t.ctn_num,  ");
	   sb.append(" t.sku_id, ");
	   sb.append(" t.enter_state,  ");
	   sb.append(" t.loading_time, ");
	   sb.append(" t.loading_truck_num, ");
	   sb.append(" lo.stock_name  ");
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
	   sb.append(" 	) aa                                                                           ");
	   sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id                             ");
	   sb.append(" ORDER BY ");
	   sb.append(" aa.bill_num,  ");
	   sb.append(" aa.ctn_num, ");
	   sb.append(" aa.loading_time  ");
	   SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
	   return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 出库报告书--OTE客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> findRepotOTE(String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded){
			List<Map<String,Object>> getList=null;
			StringBuffer sb=new StringBuffer();
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
			HashMap<String,Object> parme=new HashMap<String,Object>();
			sb.append(" SELECT  ");  
			sb.append(" aa.stock_name, ");
			sb.append(" s.cargo_name, ");
			sb.append(" aa.bill_num, ");
			sb.append(" aa.ctn_num,  ");
			sb.append(" aa.sku_id, ");
			sb.append(" aa.stock_id, ");
			sb.append(" aa.pro_time, ");
			sb.append(" aa.loading_time, ");
			sb.append(" aa.loading_truck_num,");
			sb.append(" aa.enter_state,");
			sb.append(" aa.piece_sum,");
			sb.append(" round(net_weight_sum, 2) AS net_weight_sum,");
			sb.append(" round(gross_weight_sum, 2) AS gross_weight_sum, ");
			sb.append(" s.type_size, ");
			sb.append(" s.pro_num, ");
			sb.append(" s.lot_num,");
			sb.append(" s.msc_num,");
			sb.append(" osi.sales_num,");
			sb.append(" aa.loading_plan_num,");
			sb.append(" aa.RECEIVER_NAME, ");
			sb.append(" aa.order_num");
			sb.append(" FROM(  ");
			sb.append("  SELECT ");
			sb.append("   ai.rk_num, ");
			sb.append("   t.loading_time, ");
			sb.append("   t.loading_truck_num,");
			sb.append("   loading_plan_num,");
			sb.append("   t.stock_id, ");
			sb.append("   lo.stock_name,");
			sb.append("   t.bill_num,");
			sb.append("   t.ctn_num, ");
			sb.append("   t.sku_id,");
			sb.append("   ai.pro_time, ");
			sb.append("   (CASE t.enter_state WHEN '0' THEN 'INTACT' WHEN '1' THEN 'BROKEN' WHEN '2' THEN 'COVER TORN' END ) AS enter_state,");
			sb.append("   sum(t.piece) AS piece_sum, ");
			sb.append("   sum(t.net_weight) AS net_weight_sum,");
			sb.append("   sum(t.gross_weight) AS gross_weight_sum,");
			sb.append("   t.out_link_id,");
			sb.append("   min(lo.RECEIVER_NAME) AS RECEIVER_NAME,");
			sb.append("   ba.order_num ");
			sb.append(" FROM(  ");
			sb.append("   SELECT  ");
			sb.append("    sku_id,  ");
			sb.append("    asn_id,   ");
			sb.append("    loading_plan_num, ");
			sb.append("    bill_num,   ");
			sb.append("    stock_id,  ");
			sb.append("    ctn_num,  ");
			sb.append("    enter_state, ");
			sb.append("    li.piece,   ");
			sb.append("    li.net_weight, ");
			sb.append("    li.gross_weight, ");
			sb.append("    to_char (li.loading_time, 'YY-MM-DD') AS loading_time,");
			sb.append("    li.loading_time AS loading_real_time,");
			sb.append("    li.loading_truck_num,");
			sb.append("    li.out_link_id  ");
			sb.append(" FROM  ");
			sb.append("    bis_loading_info li ");
			sb.append(" WHERE li.loading_state = '2'  ");
			if(trucknum!=null && !"".equals(trucknum)){//装车单号 
				sb.append("  and  li.loading_truck_num in (" + trucknum + ") ");// --装车单号
			}
	        if(billnum!=null && !"".equals(billnum)){//提单号 
				sb.append("  and  li.BILL_NUM in (" + billnum + ") ");// --提单号
			}
			if(cunNum!=null && !"".equals(cunNum)){//--箱号
				  sb.append("   and   li.ctn_num =:ctnnum");//--箱号 
				  parme.put("ctnnum", cunNum);
			}
		    if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
				  sb.append("   and  li.stock_id  =:sockid  ");//--客户ID
				  parme.put("sockid", stockIn);
			}
			sb.append(" ) t   ");
			sb.append(" LEFT JOIN ( ");
			sb.append(" SELECT   ");
			sb.append(" out_link_id, ");
			sb.append(" if_bonded  ");
			sb.append(" FROM    ");
			sb.append(" bis_out_stock  ");
			sb.append(" ) stock ON t.out_link_id = stock.out_link_id     ");
			sb.append(" LEFT JOIN (    ");
			sb.append(" SELECT   ");
			sb.append(" blo.order_num,  ");
			sb.append(" blo.stock_name,  ");
			sb.append(" blo.order_state,");
			sb.append(" RECEIVER_NAME ");
			sb.append(" FROM   ");
			sb.append(" bis_loading_order blo    ");
			if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
				 sb.append("  where blo.stock_id =:sockid1 "); 
				 parme.put("sockid1", stockIn);
			}
			sb.append(" ) lo ON lo.order_num = t.loading_plan_num                   ");
			sb.append(" LEFT JOIN bis_asn ba ON (ba.bill_num = t.bill_num AND ba.ctn_num = t.ctn_num AND t.asn_id=ba.asn) ");
			sb.append(" LEFT JOIN bis_asn_info ai ON (ai.asn_id = t.asn_id AND ai.sku_id = t.sku_id)            ");
			sb.append(" WHERE  lo.order_state = '4'                                                     ");
			if(null!=isBonded&&!"".equals(isBonded)){
	        	if("1".equals(isBonded)){
	        		sb.append(" AND stock.if_bonded='"+isBonded+"'");
	        	}else{
	        		sb.append(" AND (stock.if_bonded ='0' or stock.if_bonded is null)    ");
	        	}
	        }
			sb.append(" GROUP BY    ");
			sb.append(" loading_plan_num,");
			sb.append(" ai.rk_num, ");
			sb.append(" t.stock_id,");
			sb.append(" t.bill_num,");
			sb.append(" t.ctn_num, ");
			sb.append(" t.sku_id,  ");
			sb.append(" t.asn_id, ");
			sb.append(" t.enter_state, ");
			sb.append(" t.loading_time, ");
			sb.append(" t.loading_truck_num,");
			sb.append(" lo.stock_name,");
			sb.append(" ai.pro_time, ");
			sb.append(" t.out_link_id,");
			sb.append(" ba.order_num  ");
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
			sb.append(" ) aa                                                          ");
			sb.append(" LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id          ");
			sb.append(" LEFT JOIN (  ");
			sb.append(" 	SELECT               ");
			sb.append(" 		oin.out_link_id,   ");
			sb.append(" 		oin.bill_num,      ");
			sb.append(" 		oin.sku_id,        ");
			sb.append(" 		oin.ctn_num,       ");
			sb.append(" 		LISTAGG (oin.sales_num, ',') WITHIN GROUP (ORDER BY oin.sales_num) AS sales_num  ");
			sb.append(" 	FROM                      ");
			sb.append(" 		bis_out_stock_info oin  ");
			sb.append(" 	GROUP BY                  ");
			sb.append(" 		oin.out_link_id,        ");
			sb.append(" 		oin.bill_num,           ");
			sb.append(" 		oin.sku_id,             ");
			sb.append(" 		oin.ctn_num             ");
			sb.append(" ) osi ON (osi.out_link_id = aa.out_link_id                       ");
			sb.append(" AND osi.bill_num = aa.bill_num                                  ");
			sb.append(" AND osi.sku_id = aa.sku_id                                      ");
			sb.append(" AND osi.ctn_num = aa.ctn_num)                                    ");
			sb.append(" ORDER BY  ");
			sb.append(" aa.bill_num,  ");
			sb.append(" aa.ctn_num,  ");
			sb.append(" aa.loading_time ");
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		    return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 出库报告书--OTE客户，根据联系单号导出PDF（出库日期为计划出库日期 ）20160623 添加
	 * @author gzq
	 * @param linkId 联系单号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRepotOTEByOutlinkid(String linkId){
		List<Map<String,Object>> getList=null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		
//		sql.append( " select aa.stock_name, s.cargo_name, aa.bill_num, aa.ctn_num, aa.sku_id, aa.stock_id, aa.pro_time, bos.etd_warehouse as loading_time, aa.enter_state, aa.piece_sum, net_weight_sum, gross_weight_sum, s.type_size, s.pro_num, s.lot_num, s.msc_num, osi.sales_num ");
//		sql.append( " from (select ai.rk_num, t.stock_id, lo.stock_name, t.bill_num, t.ctn_num, t.sku_id, ai.pro_time, ");
//		sql.append( " (case t.enter_state when '0' then 'INTACT'  when '1' then 'BROKEN' when '2' then 'COVER TORN' end) as enter_state, ");
//		sql.append( "  sum(t.piece) as piece_sum, sum(t.net_weight) as net_weight_sum, sum(t.gross_weight) as gross_weight_sum, t.out_link_id ");
//		sql.append( "            from (select sku_id, loading_plan_num, bill_num, stock_id, ctn_num, enter_state, li.piece, li.net_weight, li.gross_weight, li.loading_time, li.out_link_id ");
//		sql.append("                     from bis_loading_info li ");
//		sql.append("                   where ");//--(已装车状态，非查询条件)
//		//sql.append("                   li.loading_state = '2'  and   ");//--(已装车状态，非查询条件)
//		if(linkId!=null && !"".equals(linkId)){//联系单号 
//		  sql.append("                      li.out_link_id =:outlinkid ");// --装车单号
//		  parme.put("outlinkid", linkId);
//		  }
//		  
//	 
//		sql.append("        ) t ");
//		sql.append("  left join (select blo.order_num, blo.stock_name, blo.order_state ");
//		sql.append("               from bis_loading_order blo ");
//		if(linkId!=null && !"".equals(linkId)){//--联系单号 
//			 sql.append("              where blo.out_link_id =:outlinkid1 "); 
//			  parme.put("outlinkid1", linkId);
//		}
//		
//		sql.append("          ) lo ");
//		sql.append("       on lo.order_num = t.loading_plan_num ");
//		sql.append("      left join bis_asn ba ");
//		sql.append("        on ba.bill_num = t.bill_num and ");
//		sql.append("       ba.ctn_num = t.ctn_num ");
//		sql.append("   left join bis_asn_info ai ");
//		sql.append("           on ai.asn_id = ba.asn and ");
//		sql.append("          ai.sku_id = t.sku_id ");
//		//sql.append("       where lo.order_state = '4'  ");//--(已出库状态，非查询条件)
//		sql.append( "    group by ai.rk_num, t.stock_id, t.bill_num, t.ctn_num, t.sku_id, t.enter_state, lo.stock_name, ai.pro_time, t.out_link_id ");
//		sql.append(" 	          ) aa ");
//		sql.append("   left join base_sku_base_info s ");
//		sql.append("    on s.sku_id = aa.sku_id ");
//		sql.append("   left join bis_out_stock bos ");
//		sql.append("    on bos.out_link_id=aa.out_link_id ");
//		sql.append("   left join bis_out_stock_info osi ");
//		sql.append("    on osi.out_link_id = aa.out_link_id and ");
//		sql.append("    osi.bill_num = aa.bill_num and ");
//		sql.append("      osi.sku_id = aa.sku_id and ");
//		sql.append("       osi.ctn_num = aa.ctn_num ");
//		sql.append(" 	 order by aa.bill_num, aa.ctn_num, bos.etd_warehouse ");
		
		sql.append( " select aa.stock_name, s.cargo_name, aa.bill_num, aa.ctn_num, aa.sku_id, aa.stock_id, aa.etd_warehouse as loading_time, aa.receiver_name,   ");
		sql.append( " (case aa.enter_state when '0' then 'INTACT'  when '1' then 'BROKEN' when '2' then 'COVER TORN' end) as enter_state, aa.piece_sum, aa.net_weight_sum, aa.gross_weight_sum, s.type_size, s.pro_num, s.lot_num, s.msc_num, aa.sales_num ");
		sql.append( " from (select  ");
		sql.append( "  bos.stock_in as stock_name,bos.stock_in_id as stock_id,bos.etd_warehouse,bos.receiver as receiver_name,  ");
		sql.append( "  bosi.sku_id, bosi.bill_num, bosi.cargo_name, bosi.ctn_num, bosi.enter_state,bosi.sales_num,sum(bosi.out_num) as piece_sum,  sum(bosi.net_weight) as net_weight_sum, sum(bosi.gross_weight) as gross_weight_sum, bosi.out_link_id ");
		sql.append( "            from bis_out_stock bos left join bis_out_stock_info bosi ");
		sql.append("                   on bos.out_link_id=bosi.out_link_id ");
		sql.append("                   where ");
		if(linkId!=null && !"".equals(linkId)){//联系单号 
		  sql.append("                      bos.out_link_id =:outlinkid ");
		  parme.put("outlinkid", linkId);
		  }
		  
		sql.append("  group by bos.stock_in_id, bosi.bill_num, bosi.ctn_num, bosi.sku_id, bosi.enter_state, bos.stock_in, bosi.out_link_id,bos.etd_warehouse,bos.receiver,bosi.cargo_name,bosi.sales_num ");
		sql.append("  ) aa ");
		sql.append("     left join base_sku_base_info s ");		
		sql.append("     on s.sku_id = aa.sku_id ");		
		sql.append(" 	 order by aa.bill_num, aa.ctn_num, aa.etd_warehouse ");
	 
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}


	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 出库报告书  接口信息（海路通系统）
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,Object>> outStockReportInfo(String itemNum, String ctnNum, String realClientName){

		List<Map<String,Object>> getList=null;
		StringBuffer sb=new StringBuffer();
		String trucknum = "";
		String billnum="";

		if (!StringUtils.isNull(itemNum)) {
			String[] itemNumList = itemNum.split(",");
			for (String truck : itemNumList) {
				billnum += "'" + truck + "'" + ",";
			}
			if (!billnum.equals("")) {
				billnum = billnum.substring(0, billnum.length() - 1);
			}
		}
		HashMap<String,Object> parme=new HashMap<String,Object>();
		sb.append("SELECT                                                          ");
		sb.append("	c.REAL_CLIENT_NAME AS client_name,                                                 ");
		sb.append("	s.cargo_name,                                                  ");
		sb.append("	aa.bill_num,                                                   ");
		sb.append("	aa.ctn_num,                                                    ");
		sb.append("	aa.sku_id,                                                     ");
		sb.append("	aa.loading_time,                                               ");
		sb.append("	aa.loading_truck_num,                                          ");
		sb.append("	aa.stock_id,                                                   ");
		sb.append("	aa.enter_state,                                                ");
		sb.append("	aa.piece_sum,                                                  ");
		sb.append("	round(net_weight_sum, 2) AS net_weight_sum,                    ");
		sb.append("	round(gross_weight_sum, 2) AS gross_weight_sum,                ");
		sb.append("	aa.RECEIVER_NAME                                               ");
		sb.append("FROM                                                            ");
		sb.append("	(                                                              ");
		sb.append("		SELECT                                                       ");
		sb.append("			t.loading_time,                                            ");
		sb.append("			t.loading_truck_num,                                       ");
		sb.append("			lo.stock_name,                                             ");
		sb.append("			t.stock_id,                                                ");
		sb.append("			t.bill_num,                                                ");
		sb.append("			t.ctn_num,                                                 ");
		sb.append("			t.sku_id,                                                  ");
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
		sb.append("			sum(t.piece) AS piece_sum,                                 ");
		sb.append("			sum(t.net_weight) AS net_weight_sum,                       ");
		sb.append("			sum(t.gross_weight) AS gross_weight_sum,                   ");
		sb.append("			MIN(lo.RECEIVER_NAME) AS RECEIVER_NAME                     ");
		sb.append("		FROM                                                         ");
		sb.append("			(                                                          ");
		sb.append("				SELECT                                                   ");
		sb.append("					sku_id,                                                ");
		sb.append("					loading_plan_num,                                      ");
		sb.append("					bill_num,                                              ");
		sb.append("					stock_id,                                              ");
		sb.append("					ctn_num,                                               ");
		sb.append("					enter_state,                                           ");
		sb.append("					li.out_link_id,                                        ");
		sb.append("					li.piece,                                              ");
		sb.append("					li.net_weight,                                         ");
		sb.append("					li.gross_weight,                                       ");
		sb.append("					to_char (li.loading_time, 'YY-MM-DD') AS loading_time, ");
		sb.append("					li.loading_time AS loading_real_time,                  ");
		sb.append("					li.loading_truck_num                                   ");
		sb.append("				FROM                                                       ");
		sb.append("					bis_loading_info li                                    ");
		sb.append("				WHERE                                                      ");
		sb.append("					li.loading_state = '2'                                 ");

		if(billnum!=null && !"".equals(billnum)){//提单号
			sb.append("             and  li.BILL_NUM in (" + billnum + ") ");// --提单号
		}
		if(ctnNum!=null && !"".equals(ctnNum)){//--箱号
			sb.append("             and   li.ctn_num =:ctnnum");//--箱号
			parme.put("ctnnum", ctnNum);
		}
		sb.append("			) t                                                            ");
		sb.append("		LEFT JOIN (                                                        ");
		sb.append("			SELECT                                                         ");
		sb.append("				out_link_id,                                               ");
		sb.append("				if_bonded                                                  ");
		sb.append("			FROM                                                           ");
		sb.append("				bis_out_stock                                              ");
		sb.append("		) stock ON t.out_link_id = stock.out_link_id                       ");
		sb.append("		LEFT JOIN (                                                        ");
		sb.append("			SELECT                                                     ");
		sb.append("				blo.order_num,                                           ");
		sb.append("				blo.stock_name,                                          ");
		sb.append("				blo.order_state,                                         ");
		sb.append("				RECEIVER_NAME                                            ");
		sb.append("			FROM                                                       ");
		sb.append("				bis_loading_order blo                                    ");
		sb.append("		) lo ON lo.order_num = t.loading_plan_num                    ");
		sb.append("		WHERE                                                        ");
		sb.append("			lo.order_state = '4'                                       ");
		sb.append("		GROUP BY                                                     ");
		sb.append("			t.stock_id,                                                ");
		sb.append("			t.bill_num,                                                ");
		sb.append("			t.ctn_num,                                                 ");
		sb.append("			t.sku_id,                                                  ");
		sb.append("			t.enter_state,                                             ");
		sb.append("			t.loading_time,                                            ");
		sb.append("			t.loading_truck_num,                                       ");
		sb.append("			lo.stock_name                                              ");
		sb.append("	) aa                                                           ");
		sb.append("LEFT JOIN base_sku_base_info s ON s.sku_id = aa.sku_id          ");
		sb.append(" LEFT JOIN base_client_info c ON c.ids = aa.stock_id     ");
		sb.append(" where 1=1 ");
		if (realClientName != null && !"".equals(realClientName)) {//--客户名称
			sb.append(" and c.REAL_CLIENT_NAME=:sockid  ");
			parme.put("sockid", realClientName);
		}
		sb.append("ORDER BY                                                        ");
		sb.append("	aa.bill_num,                                                   ");
		sb.append("	aa.ctn_num                                                     ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

}
