package com.haiersoft.ccli.report.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.report.entity.PaymentDetailReportStock;

@Repository
public class PaymentDetailReportDao extends HibernateDao<PaymentDetailReportStock, String> {
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchStockReport(String payId,String clientName,String payee,String billNum,String searchStrTime,String searchEndTime) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer();
		HashMap<String,Object> params=new HashMap<String,Object>();
		sql.append(" select * from PaymentDetailReport t where 1=1 ");
		if(payId!=null && !"".equals(payId)){
			sql.append(" and t.pay_id in ( ");
			String[] payIds = payId.split(",");
			int size = payIds.length;
			for(int i=0;i<size;i++){
				sql.append(payIds[i]+",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(" ) ");
		}
		if(clientName!=null && !"".equals(clientName)){
			sql.append(" and t.client_name = :clientName ");
			params.put("clientName", clientName);	
		}
		if(payee!=null && !"".equals(payee)){
			sql.append(" and t.dl_client_name = :payee ");
			params.put("payee", payee);	
		}
		if(billNum!=null && !"".equals(billNum)){
			sql.append(" and t.bill_num = :billNum ");
			params.put("billNum", billNum);	
		}
		if(searchStrTime!=null && !"".equals(searchStrTime)){
			sql.append(" and trunc(t.ask_date,'mm')>= to_date(:searchStrTime, 'yyyy-mm') ");	
			params.put("searchStrTime", searchStrTime);
		}
		if(searchEndTime!=null && !"".equals(searchEndTime)){
			sql.append(" and trunc(t.ask_date,'mm')<= to_date(:searchEndTime, 'yyyy-mm')  ");	
			params.put("searchEndTime", searchEndTime);
		}
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
		getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
	/**
	 * 
	 * @author cuij
	 * @Description: 分页查询
	 * @date 2016年6月24日17:07:30 
	 * @param page
	 * @param expenseScheme
	 * @return
	 * @throws
	 
	public Page<PaymentDetailReportStock> searchStockReport(Page<PaymentDetailReportStock> page, PaymentDetailReportStock paymentDetailReportStock){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(""
				+ " select t.pay_id as payId, "
				+ " t.client_name as clientName,"
				+ " t.sell_man as sellMan,"
				+ " t.bill_num as billNum,"
				+ " t.dl_client_name as payee,"
				+ " t.ctn_amount as ctnAmount,"
				+ " t.ask_man as askMan,"
				+ " t.ask_date as askDate,"
				+ " t." 
				+ "\""
				+ "CFS　"
				+ "\""
				+ "as cfs,"
				+ " t."
				+ "\""
				+ "拆箱分拣卸货　"
				+ "\""
				+ "as cx,"
				+ " t."
				+ "\""
				+"服务　"
				+ "\""
				+"as fw,"
				+ " t."
				+ "\""
				+"港建　"
				+ "\""
				+"as gj,"
				+ " t."
				+ "\""
				+"外协拆、装箱　"
				+ "\""
				+"as wx,"
				+ " t.下毒 as xd"
                + " from tmp1 t"
				+ " where 1=1 "
				);
		if(!StringUtils.isNull(paymentDetailReportStock.getPayId())){
			sql.append(" and t.pay_id in ( ");
			String[] payIds = paymentDetailReportStock.getPayId().split(",");
			int size = payIds.length;
			for(int i=0;i<size;i++){
				sql.append(payIds[i]+",");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(" ) ");
// 			sql.append(" and t1.pay_id in (:payId) ");
//			params.put("payId", paymentReportStock.getPayId());
		}
		if(!StringUtils.isNull(paymentDetailReportStock.getClientName())){
			sql.append(" and t.client_name = :clientName ");
			params.put("clientName", paymentDetailReportStock.getClientName());	
		}
		if(!StringUtils.isNull(paymentDetailReportStock.getPayee())){
			sql.append(" and t.dl_client_name = :payee ");
			params.put("payee", paymentDetailReportStock.getPayee());	
		}
		if(!StringUtils.isNull(paymentDetailReportStock.getBillNum())){
			sql.append(" and t.bill_num = :billNum ");
			params.put("billNum", paymentDetailReportStock.getBillNum());	
		}
		if(null!=paymentDetailReportStock.getSearchStrTime() && !"".equals(paymentDetailReportStock.getSearchStrTime())){
			sql.append(" and t.ask_date >= :searchStrTime "); 
			params.put("searchStrTime", paymentDetailReportStock.getSearchStrTime());
		}
		if(null!=paymentDetailReportStock.getSearchEndTime() && !"".equals(paymentDetailReportStock.getSearchEndTime())){
			sql.append(" and t.ask_date <= :searchEndTime ");
			params.put("searchEndTime", DateUtils.addMonth(paymentDetailReportStock.getSearchEndTime(), 1));
		}
		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("payId", String.class);//业务付款单号
		parm.put("clientName", String.class);//客户名称
		parm.put("sellMan", String.class);//销售人员
		parm.put("billNum", String.class);//提单号
		parm.put("payee", String.class);//收款人
		parm.put("ctnAmount", Double.class);//箱量
		parm.put("askMan", String.class);//申请人
		parm.put("askDate", Date.class);//申请日期
		parm.put("cfs", Double.class);//CFS
		parm.put("cx", Double.class);//拆箱
		parm.put("fw", Double.class);//服务
		parm.put("gj", Double.class);//港建
		parm.put("wx", Double.class);//外协
		parm.put("xd", Double.class);//下毒
		return findPageSql(page, sql.toString(), parm, params);		
//		return findPageSql(page, sql.toString(), params);
	
           }
*/
	/**
	 * 在库明细--普通客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRepotPT(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getList=null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		
		sql.append(" select c.client_name, aa.stock_in, aa.bill_num, aa.ctn_num, aa.sku_id, s.cargo_name, aa.inbound_date, ");
		sql.append(" aa.cargo_location, aa.enter_state, aa.now_piece_sum, aa.net_weight_sum, aa.gross_weight_sum ");
		sql.append("   from (select t.stock_in, t.bill_num, t.ctn_num, t.sku_id, a.inbound_date,   ");
		sql.append( " (case  t.enter_state when '0' then '成品'  when '1' then '货损' when '2' then '包装破损' end) as enter_state,  ");
		sql.append( " 	   t.cargo_location, sum(t.now_piece) as now_piece_sum, sum(t.net_weight) as net_weight_sum, sum(t.gross_weight) as gross_weight_sum ");
		sql.append( "         from (select bt.stock_in, bt.bill_num, bt.ctn_num, bt.sku_id, bt.asn, bt.enter_state, bt.cargo_location, bt.now_piece, bt.net_weight, bt.gross_weight ");
		sql.append("                  from BIS_TRAY_INFO bt ");
		sql.append("                  where bt.cargo_state = '01'  ");
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and bt.bill_num=:billnum  ");
			parme.put("billnum", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and bt.stock_in=:sockid  ");
			parme.put("sockid", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and bt.ctn_nu=:ctnnum   ");
			parme.put("ctnnum", cunNum);
		}
		 
		sql.append("           ) t ");
		sql.append( "   inner join (select ba.stock_in, ba.bill_num, ba.ctn_num, trunc(ba.inbound_date) as inbound_date, ba.asn ");
		sql.append("                 from bis_asn ba  where 1=1 " );
		 
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and ba.bill_num=:billnum1  ");
			parme.put("billnum1", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and ba.stock_in=:sockid1  ");
			parme.put("sockid1", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and ba.ctn_nu=:ctnnum1   ");
			parme.put("ctnnum1", cunNum);
		}
		if(strTime!=null && !"".equals(strTime)){//--入库日期 
			sql.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
			parme.put("strTime", strTime);
		}
		if(endTime!=null && !"".equals(endTime)){//--入库日期 
			sql.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			parme.put("endTime", endTime);
		}
		sql.append("                ) a ");
		sql.append("        on a.asn = t.asn and ");
		sql.append("           a.ctn_num = t.ctn_num and ");
		sql.append("            a.stock_in = t.stock_in and ");
		sql.append("            a.bill_num = t.bill_num ");
		sql.append("     group by t.bill_num, t.ctn_num, t.sku_id, a.inbound_date, t.cargo_location, t.enter_state, t.stock_in) aa ");
		sql.append(" left join base_client_info c ");
		sql.append("   on c.ids = aa.stock_in ");
		sql.append(" left join base_sku_base_info s ");
		sql.append("   on s.sku_id = aa.sku_id ");
		sql.append("  order by aa.bill_num, aa.ctn_num, aa.inbound_date ");
		
		
		
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
		
		 
	}
	/**
	 * 在库明细--JP客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRepotJP(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getList=null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		
		sql.append("select c.client_name, aa.stock_in, aa.rk_num, aa.bill_num, aa.ctn_num, aa.sku_id, s.cargo_name, aa.cargo_location, ");
		sql.append(" aa.inbound_date, aa.enter_state, aa.now_piece_sum, aa.net_weight_sum, aa.gross_weight_sum ");
		sql.append("   from (select ai.rk_num, a.inbound_date, t.stock_in, t.bill_num, t.ctn_num, t.sku_id, t.cargo_location, ");
		sql.append(" (case  t.enter_state when '0' then '成品'  when '1' then '货损' when '2' then '包装破损' end) as enter_state,  ");
		sql.append("  sum(t.now_piece) as now_piece_sum, sum(t.net_weight) as net_weight_sum, sum(t.gross_weight) as gross_weight_sum ");
		sql.append("  from (select bt.sku_id, bt.asn, bt.bill_num, bt.stock_in, bt.ctn_num, bt.cargo_location, bt.enter_state, bt.now_piece, bt.net_weight, bt.gross_weight ");
		sql.append("         from BIS_TRAY_INFO bt ");
		sql.append("        where bt.cargo_state = '01' ");
		 
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and bt.bill_num=:billnum  ");
			parme.put("billnum", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and bt.stock_in=:sockid  ");
			parme.put("sockid", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and bt.ctn_nu=:ctnnum   ");
			parme.put("ctnnum", cunNum);
		}
		sql.append("         ) t ");
		sql.append("     inner join (select ba.asn, trunc(ba.inbound_date) as inbound_date, ba.ctn_num, ba.bill_num, ba.stock_in ");
		sql.append("                 from bis_asn ba where 1=1  ");
		 
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and ba.bill_num=:billnum1  ");
			parme.put("billnum1", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and ba.stock_in=:sockid1  ");
			parme.put("sockid1", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and ba.ctn_nu=:ctnnum1   ");
			parme.put("ctnnum1", cunNum);
		}
		if(strTime!=null && !"".equals(strTime)){//--入库日期 
			sql.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
			parme.put("strTime", strTime);
		}
		if(endTime!=null && !"".equals(endTime)){//--入库日期 
			sql.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			parme.put("endTime", endTime);
		}
		sql.append("             ) a ");
		sql.append("     on t.asn = a.asn and ");
		sql.append("        a.ctn_num = t.ctn_num and ");
		sql.append("        a.stock_in = t.stock_in and ");
		sql.append("        a.bill_num = t.bill_num ");
		sql.append("   left join bis_asn_info ai ");
		sql.append("  on ai.asn_id = a.asn and ");
		sql.append("  ai.sku_id = t.sku_id ");
		sql.append("   group by ai.rk_num, t.stock_in, t.bill_num, t.ctn_num, t.sku_id, t.cargo_location, a.inbound_date, t.enter_state) aa ");
		sql.append("   left join base_client_info c ");
		sql.append("    on c.ids = aa.stock_in ");
		sql.append("   left join base_sku_base_info s ");
		sql.append("     on s.sku_id = aa.sku_id ");
		sql.append("  order by aa.bill_num, aa.ctn_num, aa.inbound_date, aa.rk_num ");
    
		
		
		
		 
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	/**
	 * 在库明细--ote客户
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRepotOTE(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getList=null;
		 
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
		
		sql.append( "select c.client_name, aa.stock_in, aa.bill_num, aa.ctn_num, aa.sku_id, s.cargo_name, aa.cargo_location, aa.pro_time, aa.inbound_date,  ");
		sql.append( " aa.enter_state, aa.now_piece_sum, aa.net_weight_sum, aa.gross_weight_sum, s.type_size, s.pro_num, s.lot_num, s.msc_num  ");
		sql.append( "   from (select ai.pro_time, a.inbound_date, t.stock_in, t.bill_num, t.ctn_num, t.sku_id, t.cargo_location,   ");
		sql.append( " (case  t.enter_state when '0' then '成品'  when '1' then '货损' when '2' then '包装破损' end) as enter_state,  ");
		sql.append( " 		  sum(t.now_piece) as now_piece_sum, sum(t.net_weight) as net_weight_sum, sum(t.gross_weight) as gross_weight_sum  ");
		sql.append( "           from (select bt.bill_num, bt.ctn_num, bt.sku_id, bt.asn, bt.stock_in, bt.cargo_location, bt.enter_state, bt.now_piece, bt.net_weight, bt.gross_weight  ");
		sql.append("                from BIS_TRAY_INFO bt  ");
		sql.append("                 where bt.cargo_state = '01'   ");
		
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and bt.bill_num=:billnum  ");
			parme.put("billnum", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and bt.stock_in=:sockid  ");
			parme.put("sockid", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and bt.ctn_nu=:ctnnum   ");
			parme.put("ctnnum", cunNum);
		}  
		 
		
		sql.append("             ) t  ");
		sql.append( "     inner join (select ba.bill_num, ba.ctn_num, ba.asn, trunc(ba.inbound_date) as inbound_date, ba.stock_in  ");
		sql.append("                  from bis_asn ba  where 1=1 ");
		 
		if(itemNum!=null && !"".equals(itemNum)){//提单号 
			sql.append(" and ba.bill_num=:billnum1  ");
			parme.put("billnum1", itemNum);
		}
		if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
			sql.append(" and ba.stock_in=:sockid1  ");
			parme.put("sockid1", stockIn);
		}
		if(cunNum!=null && !"".equals(cunNum)){//--箱号
			sql.append(" and ba.ctn_nu=:ctnnum1   ");
			parme.put("ctnnum1", cunNum);
		}
		if(strTime!=null && !"".equals(strTime)){//--入库日期 
			sql.append(" and ba.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
			parme.put("strTime", strTime);
		}
		if(endTime!=null && !"".equals(endTime)){//--入库日期 
			sql.append(" and ba.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			parme.put("endTime", endTime);
		}
		sql.append("               ) a  ");
		sql.append("       on t.asn = a.asn and  ");
		sql.append("          a.ctn_num = t.ctn_num and  ");
		sql.append("          a.stock_in = t.stock_in and  ");
		sql.append("          a.bill_num = t.bill_num  ");
		sql.append("     left join bis_asn_info ai  ");
		sql.append("     on ai.asn_id = a.asn and  ");
		sql.append("      ai.sku_id = t.sku_id  ");
		sql.append( "      group by ai.pro_time, t.stock_in, t.bill_num, t.ctn_num, t.sku_id, t.cargo_location, a.inbound_date, t.enter_state) aa  ");
		sql.append("   left join base_client_info c  ");
		sql.append("     on c.ids = aa.stock_in  ");
		sql.append("   left join base_sku_base_info s  ");
		sql.append("     on s.sku_id = aa.sku_id  ");
		sql.append("  order by aa.bill_num, aa.ctn_num, aa.inbound_date  ");
		
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	/**
	 * 出入库明细 
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRepotInAndOut(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getList=null;
		/**
		  		create or replace view 出入库明细_带区位 as
				select
				--字段：存货方,存货方ID,类型,类型序号,收货方,收货方ID,提单号,箱号,SKU,品名,大类,大类号,小类,小类号,区位,出入库日期,货物状态,件数,总净重,总毛重
				--入库、分拣
				       c.client_name,aa.stock_in as stock_id,(case aa.if_second_enter when '1' then '入库' when '3' then '分拣' end) as leixing,
				        (case aa.if_second_enter when '1' then '1' when '3' then '3' end) as leixing_sort,
				       '' as receiver_name,'' as receiver_id,aa.bill_num,aa.ctn_num,
				       aa.sku_id,s.cargo_name,s.type_name,s.cargo_type,
				       s.class_name,s.class_type,aa.cargo_location,aa.inbound_date as churuku_date,
				       aa.enter_state,aa.piece_sum,aa.piece_sum * s.net_single as net_weight_sum,aa.piece_sum * s.gross_single as gross_weight_sum
				  from (select asnt.inbound_date,asnt.if_second_enter,t.stock_in,t.bill_num,
				               t.ctn_num,t.sku_id,t.cargo_location,
				               (case t.enter_state when '0' then '成品' when '1' then '货损' when '2' then '包装破损' end) as enter_state,
				               sum(t.original_piece - t.remove_piece) as piece_sum
				          from (select a.asn,a.inbound_date,a.ctn_num,a.bill_num,
				                       a.stock_in,a.if_second_enter
				                  from bis_asn a
				                 where (a.if_second_enter = '1' or a.if_second_enter = '3')
				                   and a.stock_in = '6940' and --客户ID
				                a.bill_num='20160422' and  --提单号
				                a.ctn_num='11' and --箱号 
				                a.inbound_date>=to_date('2016-01-20','yyyy-mm-dd') and a.inbound_date<to_date('2016-09-21','yyyy-mm-dd')+1  --入库日期
				                ) asnt
				         inner join (select sku_id,asn,bill_num,stock_in,
				                           ctn_num,enter_state,original_piece,remove_piece,ti.cargo_location
				                      from BIS_TRAY_INFO ti where
				                    bill_num='20160422'  and --提单号
				                    ctn_num='11' --箱号 
				                    ) t
				            on t.asn = asnt.asn
				           and asnt.bill_num = t.bill_num
				         group by t.stock_in,t.bill_num,t.ctn_num,t.sku_id,
				                  t.enter_state,asnt.inbound_date,t.cargo_location,asnt.if_second_enter) aa
				  left join base_client_info c
				    on c.ids = aa.stock_in
				  left join base_sku_base_info s
				    on s.sku_id = aa.sku_id
				
				union all
				
				--出库
				select aa.stock_name,aa.stock_id,'出库' as leixing,'4' as leixing_sort,
				       aa.receiver_name,aa.receiver_id,aa.bill_num,aa.ctn_num,
				       aa.sku_id,s.cargo_name,s.type_name,s.cargo_type,
				       s.class_name,s.class_type,aa.cargo_location,aa.loading_tiem as churuku_date,
				       aa.enter_state,aa.piece_sum,net_weight_sum,gross_weight_sum
				  from (select lor.loading_tiem,t.stock_id,lor.stock_name,t.bill_num,
				               t.ctn_num,t.sku_id,t.cargo_location,
				               (case t.enter_state when '0' then '成品' when '1' then '货损' when '2' then '包装破损' end) as enter_state,
				               sum(t.piece) as piece_sum,sum(t.net_weight) as net_weight_sum,sum(t.gross_weight) as gross_weight_sum,lo.receiver_name,lo.receiver_id
				          from (select a.loading_plan_num,a.bill_num,a.ctn_num,a.loading_tiem,
				                       a.stock_id,a.stock_name
				                  from bis_loading_order_info a
				                 where a.stock_id = '6940' and --客户ID
				                a.bill_num='20160422' and  --提单号
				                a.ctn_num='11' and --箱号 
				                a.loading_tiem>=to_date('2016-01-20','yyyy-mm-dd') and a.loading_tiem<to_date('2016-09-21','yyyy-mm-dd')+1  --入库日期
				                ) lor
				          left join (select sku_id,loading_plan_num,bill_num,stock_id,
				                           ctn_num,enter_state,li.piece,li.net_weight,
				                           li.gross_weight,li.cargo_location
				                      from bis_loading_info li
				                     where li.loading_state = '2' and
				                    bill_num='2016042110'  and --提单号
				                    ctn_num='11' --箱号 
				                    ) t
				            on t.loading_plan_num = lor.loading_plan_num
				          left join bis_loading_order lo
				            on lo.order_num = lor.loading_plan_num
				         where lo.order_state = '4' and--(已出库状态)
				         lo.stock_id='6940'--客户ID
				         group by t.stock_id,t.bill_num,t.ctn_num,t.sku_id,
				                  t.enter_state,lor.loading_tiem,lor.stock_name,lo.receiver_name,
				                  lo.receiver_id,t.cargo_location) aa
				  left join base_sku_base_info s
				    on s.sku_id = aa.sku_id
				 order by leixing_sort, bill_num, ctn_num;
		 */
		StringBuffer sql=new StringBuffer();
		HashMap<String,Object> parme=new HashMap<String,Object>();
				 
				sql.append(" select c.client_name,aa.stock_in as stock_id,(case aa.if_second_enter when '1' then '入库' when '3' then '分拣' end) as leixing, ");
				sql.append("        (case aa.if_second_enter when '1' then '1' when '3' then '3' end) as leixing_sort, ");
				sql.append("       '' as receiver_name,'' as receiver_id,aa.bill_num,aa.ctn_num, ");
				sql.append("       aa.sku_id,s.cargo_name,s.type_name,s.cargo_type, ");
				sql.append("       s.class_name,s.class_type,aa.cargo_location,aa.inbound_date as churuku_date, ");
				sql.append("       aa.enter_state,aa.piece_sum,aa.piece_sum * s.net_single as net_weight_sum,aa.piece_sum * s.gross_single as gross_weight_sum ");
				sql.append("  from (select asnt.inbound_date,asnt.if_second_enter,t.stock_in,t.bill_num, ");
				sql.append("               t.ctn_num,t.sku_id,t.cargo_location, ");
				sql.append("               (case t.enter_state when '0' then '成品' when '1' then '货损' when '2' then '包装破损' end) as enter_state, ");
				sql.append("               sum(t.original_piece - t.remove_piece) as piece_sum ");
				sql.append("          from (select a.asn,a.inbound_date,a.ctn_num,a.bill_num, ");
				sql.append("                       a.stock_in,a.if_second_enter ");
				sql.append("                  from bis_asn a ");
				sql.append("                 where (a.if_second_enter = '1' or a.if_second_enter = '3') ");
				if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
				sql.append("                   and a.stock_in =:sockid");
					parme.put("sockid", stockIn);
				}
				if(itemNum!=null && !"".equals(itemNum)){//提单号 
				sql.append("               and a.bill_num=:billnum ");
					parme.put("billnum", itemNum);
				}
				if(cunNum!=null && !"".equals(cunNum)){//--箱号
				sql.append("             and  a.ctn_num=:ctnnum  ");
					parme.put("ctnnum", cunNum);
				}
				if(strTime!=null && !"".equals(strTime)){//--入库日期 
					sql.append(" and a.inbound_date>=to_date(:strTime,'yyyy-mm-dd hh24:mi:ss')  ");
					parme.put("strTime", strTime);
				}
				if(endTime!=null && !"".equals(endTime)){//--入库日期 
					//sql.append(" and a.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')+1");
					sql.append(" and a.inbound_date<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
					parme.put("endTime", endTime);
				}
				sql.append("                ) asnt ");
				sql.append("         inner join (select sku_id,asn,bill_num,stock_in, ");
				sql.append("                           ctn_num,enter_state,original_piece,remove_piece,ti.cargo_location ");
				sql.append("                      from BIS_TRAY_INFO ti where 1=1 ");
				if(itemNum!=null && !"".equals(itemNum)){//提单号 
				sql.append("               and bill_num=:billnumo ");
					parme.put("billnumo", itemNum);
				}
				if(cunNum!=null && !"".equals(cunNum)){//--箱号
				sql.append("             and  ctn_num=:ctnnumo  ");
					parme.put("ctnnumo", cunNum);
				}
				sql.append("                    ) t ");
				sql.append("            on t.asn = asnt.asn ");
				sql.append("           and asnt.bill_num = t.bill_num ");
				sql.append("         group by t.stock_in,t.bill_num,t.ctn_num,t.sku_id, ");
				sql.append("                  t.enter_state,asnt.inbound_date,t.cargo_location,asnt.if_second_enter) aa ");
				sql.append("  left join base_client_info c ");
				sql.append("    on c.ids = aa.stock_in ");
				sql.append("  left join base_sku_base_info s ");
				sql.append("    on s.sku_id = aa.sku_id ");
				sql.append("union all ");
				sql.append("select aa.stock_name,aa.stock_id,'出库' as leixing,'4' as leixing_sort, ");
				sql.append("       aa.receiver_name,aa.receiver_id,aa.bill_num,aa.ctn_num, ");
				sql.append("       aa.sku_id,s.cargo_name,s.type_name,s.cargo_type, ");
				sql.append("       s.class_name,s.class_type,aa.cargo_location,aa.loading_tiem as churuku_date, ");
				sql.append("      aa.enter_state,aa.piece_sum,net_weight_sum,gross_weight_sum ");
				sql.append("  from (select lor.loading_tiem,t.stock_id,lor.stock_name,t.bill_num, ");
				sql.append("               t.ctn_num,t.sku_id,t.cargo_location, ");
				sql.append("               (case t.enter_state when '0' then '成品' when '1' then '货损' when '2' then '包装破损' end) as enter_state, ");
				sql.append("               sum(t.piece) as piece_sum,sum(t.net_weight) as net_weight_sum,sum(t.gross_weight) as gross_weight_sum,lo.receiver_name,lo.receiver_id ");
				sql.append("          from (select a.loading_plan_num,a.bill_num,a.ctn_num,a.loading_tiem, ");
				sql.append("                       a.stock_id,a.stock_name ");
				sql.append("                 from bis_loading_order_info a  where 1=1 ");
				if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
				sql.append("                   and a.stock_id =:sockid2");
					parme.put("sockid2", stockIn);
				}
				if(itemNum!=null && !"".equals(itemNum)){//提单号 
				sql.append("               and a.bill_num=:billnum2 ");
					parme.put("billnum2", itemNum);
				}
				if(cunNum!=null && !"".equals(cunNum)){//--箱号
				sql.append("             and  a.ctn_num=:ctnnum2  ");
					parme.put("ctnnum2", cunNum);
				}
				if(strTime!=null && !"".equals(strTime)){//--出库日期 
					sql.append(" and a.loading_tiem>=to_date(:strTime2,'yyyy-mm-dd hh24:mi:ss')  ");
					parme.put("strTime2", strTime);
				}
				if(endTime!=null && !"".equals(endTime)){//--出库日期 
					//sql.append(" and a.loading_tiem<to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')+1");
					sql.append(" and a.loading_tiem<to_date(:endTime2,'yyyy-mm-dd hh24:mi:ss')");
					parme.put("endTime2", endTime);
				}
				sql.append("                ) lor ");
				sql.append("          left join (select sku_id,loading_plan_num,bill_num,stock_id, ");
				sql.append("                           ctn_num,enter_state,li.piece,li.net_weight, ");
				sql.append("                           li.gross_weight,li.cargo_location ");
				sql.append("                      from bis_loading_info li ");
				sql.append("                     where li.loading_state = '2'  ");
				if(itemNum!=null && !"".equals(itemNum)){//提单号 
				sql.append("               and bill_num=:billnumt ");
					parme.put("billnumt", itemNum);
				}
				if(cunNum!=null && !"".equals(cunNum)){//--箱号
				sql.append("             and  ctn_num=:ctnnumt  ");
					parme.put("ctnnumt", cunNum);
				}
				sql.append("                    ) t ");
				sql.append("            on t.loading_plan_num = lor.loading_plan_num ");
				sql.append("          left join bis_loading_order lo ");
				sql.append("            on lo.order_num = lor.loading_plan_num ");
				sql.append("         where lo.order_state = '4'   ");//(已出库状态)
				if(stockIn!=null && !"".equals(stockIn)){//--客户ID 
				sql.append("                   and lo.stock_id =:sockidt");
					parme.put("sockidt", stockIn);
				}
				sql.append("         group by t.stock_id,t.bill_num,t.ctn_num,t.sku_id, ");
				sql.append("                  t.enter_state,lor.loading_tiem,lor.stock_name,lo.receiver_name, ");
				sql.append("                  lo.receiver_id,t.cargo_location) aa ");
				sql.append("  left join base_sku_base_info s ");
				sql.append("    on s.sku_id = aa.sku_id ");
				sql.append(" order by leixing_sort, bill_num, ctn_num");
		  
		SQLQuery sqlQuery=createSQLQuery(sql.toString(), parme);
		getList= sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
}