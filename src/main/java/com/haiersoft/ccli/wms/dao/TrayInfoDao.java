package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Connor.M
 * @ClassName: TrayInfoDao
 * @Description: 库存DAO
 * @date 2016年3月4日 下午2:11:23
 */

@Repository
public class TrayInfoDao extends HibernateDao<TrayInfo, Integer> {
	/**
	 * 根据库存算出总的计算量
	 * @param contact_num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSum(String contact_num){
		HashMap<String,Object> parme=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT                                             ");
		sb.append(" 	T .CONTACT_NUM,                                ");
		sb.append(" 	COUNT (T .CONTACT_NUM) AS TS,                  ");
		sb.append(" 	SUM (T .sl) AS SL,                             ");
		sb.append(" 	SUM (ROUND(T .mz, 4)) AS MZ,                   ");
		sb.append(" 	SUM (ROUND(T .jz, 4)) AS JZ,                   ");
		sb.append(" 	SUM (ROUND(T .zmz, 4)) AS ZMZ,                 ");
		sb.append(" 	SUM (ROUND(T .zjz, 4)) AS ZJZ,                 ");
		sb.append(" 	SUM (ROUND(T .jzkg, 4)) AS JZKG,               ");
		sb.append(" 	SUM (ROUND(T .mzkg, 4)) AS MZKG                ");
		sb.append(" FROM                                               ");
		sb.append(" 	(                                              ");
		sb.append(" 		SELECT                                     ");
		sb.append(" 			T .CONTACT_NUM,                        ");
		sb.append(" 			(                                       ");
		sb.append(" 				T .ORIGINAL_PIECE - T .REMOVE_PIECE ");
		sb.append(" 			) AS sl,                                ");
		sb.append(" 			CASE T .UNITS                           ");
		sb.append(" 		WHEN '2' THEN                               ");
		sb.append(" 			(T .GROSS_SINGLE)                       ");
		sb.append(" 		ELSE                                        ");
		sb.append(" 			(T .GROSS_SINGLE / 1000)                ");
		sb.append(" 		END AS mz,                                  ");
		sb.append(" 		CASE T .UNITS                               ");
		sb.append(" 	WHEN '2' THEN                                   ");
		sb.append(" 		(T .NET_SINGLE)                             ");
		sb.append(" 	ELSE                                            ");
		sb.append(" 		(T .NET_SINGLE / 1000)                      ");
		sb.append(" 	END AS jz,                                      ");
		sb.append(" 	CASE T .UNITS                                   ");
		sb.append(" WHEN '2' THEN                                       ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .GROSS_SINGLE)                           ");
		sb.append(" ELSE                                                ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .GROSS_SINGLE / 1000)                    ");
		sb.append(" END AS zmz,                                         ");
		sb.append("  CASE T .UNITS                                      ");
		sb.append(" WHEN '2' THEN                                       ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .NET_SINGLE)                             ");
		sb.append(" ELSE                                                ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .NET_SINGLE / 1000)                      ");
		sb.append(" END AS zjz,                                         ");
		sb.append("  CASE T .UNITS                                      ");
		sb.append(" WHEN '2' THEN                                       ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .NET_SINGLE * 1000)                      ");
		sb.append(" ELSE                                                ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .NET_SINGLE)                             ");
		sb.append(" END AS jzkg,                                        ");
		sb.append("  CASE T .UNITS                                      ");
		sb.append(" WHEN '2' THEN                                       ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .GROSS_SINGLE * 1000)                    ");
		sb.append(" ELSE                                                ");
		sb.append(" 	(                                               ");
		sb.append(" 		T .ORIGINAL_PIECE - T .REMOVE_PIECE         ");
		sb.append(" 	) * (T .GROSS_SINGLE)                           ");
		sb.append(" END AS mzkg                                         ");
		sb.append(" FROM                                                ");
		sb.append(" 	BIS_TRAY_INFO T                                 ");
		sb.append(" WHERE   T .CARGO_STATE <> '99'                      ");
		if(contact_num!=null && !"".equals(contact_num)){
	        sb.append(" AND	T .CONTACT_NUM =:contact_num                ");
			parme.put("contact_num", contact_num);
	    }
		sb.append(" 	) T                                             ");
		sb.append(" WHERE                                               ");
		sb.append(" 	1 = 1                                           ");
		sb.append(" GROUP BY                                            ");
		sb.append(" 	T .CONTACT_NUM                                  ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/**
	 * 按客户，提单，厢号，sKU,入库类型  分组获取库存数量
	 * @param clientId //客户
	 * @param ckId //仓库
	 * @param getMr 
	 * @param getSku 
	 * @param getBill 
	 * @param getRk 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findClientTrayList(String outLinkId,String clientId,String ckId, String getRk, String getBill, String getSku, String getMr){
		if(clientId!=null){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select t.*,nvl(t.pnum,0)as tnum,nvl(t.ppnum,0) as ttnum  from ( select c.*,s.rkdh,s.net_single,s.gross_single,s.cargo_name,c.ENTER_STATE||'$$'||c.bill_num||'$$'||c.ctn_num||'$$'||c.SKU_ID||'$$'||c.asn as lab,");
			sb.append(" (select sum(p.num)  from base_client_pledge p where p.client=:pclientId and PTYPE=1 and p.bill_num=c.bill_num and p.ctn_num=c.ctn_num and p.sku_id=c.SKU_ID and p.enter_state=c.ENTER_STATE  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  p.WAREHOUSE_ID=:pckid");
				parme.put("pckid", ckId);
			}
			sb.append(" ) as pnum, ");
			sb.append(" (select sum(pp.num)  from base_client_pledge pp where pp.client=:pclientId and PTYPE=2 and pp.sku_id=c.SKU_ID and pp.enter_state=c.ENTER_STATE  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  pp.WAREHOUSE_ID=:pwarehouseid");
				parme.put("pwarehouseid", ckId);
			}
			sb.append(" ) as ppnum, ");
			sb.append(" nvl((select sum(tt.out_num)  from bis_out_stock_info tt where tt.out_link_id =:outLinkId  and tt.ctn_num=c.ctn_num and tt.sku_id=c.sku_id and tt.enter_state=c.enter_state and tt.asn=c.asn),0) as has ");
			parme.put("outLinkId", outLinkId);
			sb.append(" from ( ");
			sb.append(" select t.PLEDGE_PIECE,sum(t.now_piece) as now_piece,t.ctn_num,t.bill_num,t.SKU_ID,t.ENTER_STATE,t.ASN,max(t.stock_in ) as stock_in,max(t.STOCK_NAME) as STOCK_NAME,max(t.BEFORE_STOCK_IN) as BEFORE_STOCK_IN from bis_tray_info t ");
//			sb.append(" left join BASE_SKU_BASE_INFO ss on ss.SKU_ID = t.SKU_ID ");
			sb.append(" where t.NOW_PIECE>0 and  t.ISTRUCK='0' and t.stock_in=:clientId and t.CARGO_STATE='01' ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  t.WAREHOUSE_ID=:ckid");
				parme.put("ckid", ckId);
			}
			if(getBill!=null && !"".equals(getBill)){
				sb.append(" and  lower(t.BILL_NUM) like lower(:getBill)");
				parme.put("getBill", "%"+getBill+"%");
			}
			if(getSku!=null && !"".equals(getSku)){
				sb.append(" and  lower(t.SKU_ID) like lower(:getSku)");
				parme.put("getSku", "%"+getSku+"%");
			}
			if(getMr!=null && !"".equals(getMr)){
				sb.append(" and  lower(t.CTN_NUM) like lower(:getMr)");
				parme.put("getMr", "%"+getMr+"%");
			}
			sb.append(" group by t.BILL_NUM,t.ASN,t.CTN_NUM,t.SKU_ID,t.ENTER_STATE,t.PLEDGE_PIECE ");
			sb.append(" ) c left join base_sku_base_info s on s.sku_id=c.SKU_ID where s.del_flag=0 ");
			if(getRk!=null && !"".equals(getRk)){
				sb.append(" and  lower(s.RKDH) like lower(:getRk) ");
				parme.put("getRk", "%"+getRk+"%");
			}
			sb.append(" ) t");
			
			parme.put("pclientId", clientId);
			parme.put("clientId", clientId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	
	/**
	 * 按客户，提单，厢号，sKU 分组获取库存数量(原货主和现货主一致)
	 * @param clientId //客户
	 * @param getMr 
	 * @param getSku 
	 * @param getBill 
	 * @param getRk 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findClientTrayList2(String outLinkId,String clientId,String ckId, String getRk, String getBill, String getSku, String getMr){
		if(clientId!=null){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select t.*,nvl(t.pnum,0) as tnum,nvl(t.ppnum,0) as ttnum  from ( select c.*,s.rkdh,s.net_single,s.gross_single,s.cargo_name,c.ENTER_STATE||'$$'||c.bill_num||'$$'||c.ctn_num||'$$'||c.SKU_ID as lab,");
			sb.append(" (select sum(p.num)  from base_client_pledge p where p.client=:pclientId and PTYPE=1 and p.bill_num=c.bill_num and p.ctn_num=c.ctn_num and p.sku_id=c.SKU_ID and p.enter_state=c.ENTER_STATE ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  p.WAREHOUSE_ID=:pckid");
				parme.put("pckid", ckId);
			}
			sb.append(" ) as pnum, ");
			sb.append(" (select sum(pp.num)  from base_client_pledge pp where pp.client=:pclientId  and PTYPE=2 and pp.sku_id=c.SKU_ID and pp.enter_state=c.ENTER_STATE  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  pp.WAREHOUSE_ID=:pwarehouseid");
				parme.put("pwarehouseid", ckId);
			}
			sb.append(" ) as ppnum, ");
			sb.append(" nvl((select sum(tt.out_num)  from bis_out_stock_info tt where tt.out_link_id =:outLinkId  and tt.ctn_num=c.ctn_num and tt.sku_id=c.sku_id and tt.enter_state=c.enter_state),0) as has ");
			parme.put("outLinkId", outLinkId);
			sb.append(" from ( ");
			sb.append(" select t.PLEDGE_PIECE,sum(t.now_piece) as now_piece,t.ctn_num,t.bill_num,t.SKU_ID,t.ENTER_STATE,max(t.stock_in ) as stock_in,max(t.STOCK_NAME) as STOCK_NAME,max(t.BEFORE_STOCK_IN) as BEFORE_STOCK_IN from bis_tray_info t ");
//			sb.append(" left join BIS_ENTER_STOCK e on e.LINK_ID = t.CONTACT_NUM ");
			sb.append(" where NOW_PIECE>0 and ISTRUCK='0' and t.stock_in=:clientId and CARGO_STATE='01' and t.before_stock_in = t.stock_in  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  t.WAREHOUSE_ID=:ckid");
				parme.put("ckid", ckId);
			}
			if(getBill!=null && !"".equals(getBill)){
				sb.append(" and  lower(t.BILL_NUM) like lower(:getBill)");
				parme.put("getBill", "%"+getBill+"%");
			}
			if(getSku!=null && !"".equals(getSku)){
				sb.append(" and  lower(t.SKU_ID) like lower(:getSku)");
				parme.put("getSku", "%"+getSku+"%");
			}
			if(getMr!=null && !"".equals(getMr)){
				sb.append(" and  lower(t.CTN_NUM) like lower(:getMr)");
				parme.put("getMr", "%"+getMr+"%");
			}
			sb.append(" group by t.BILL_NUM,t.CTN_NUM,t.SKU_ID,t.ENTER_STATE,t.PLEDGE_PIECE ");
			sb.append(" ) c left join base_sku_base_info s on s.sku_id=c.SKU_ID where s.del_flag=0 ");
			if(getRk!=null && !"".equals(getRk)){
				sb.append(" and  lower(s.RKDH) like lower(:getRk) ");
				parme.put("getRk", "%"+getRk+"%");
			}
			sb.append(" ) t");
			parme.put("pclientId", clientId);
			parme.put("clientId", clientId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	
	/**
	 * 按客户，提单，厢号，sKU,入库类型  分组获取库存数量
	 * @param clientId //客户
	 * @param ckId //仓库
	 * @param outCode //出库联系单号
	 * @param ordCode 出库订单号
	 * @param getRk 
	 * @param getName 
	 * @return
	 */
	//@Autowired
	//private OutStockDao outStockDao;
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findOutOrderClientTrayList(String clientId,String ckId,String outCode,String ordCode, String getRk, String getBill, String getSku, String getMr, String getName){
		//查询bis_out_stock表本方法outCode对应的记录asn为空的数量 用于判断后面lnum是否拼接asn相等条件 
		int count = this.selectIfASNOfBisOutStockInfo(outCode);
		
		if(clientId!=null){
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select t.*,nvl(t.pnum,0)as tnum,nvl(t.ppnum,0) as ttnum   ");
			if(outCode!=null && !"".equals(outCode)){
				sb.append(", (znum-nvl(lnum,0)) as gnum ");
			}
			sb.append(" from ( select c.*,s.net_single,s.gross_single,s.cargo_name,s.msc_num,s.lot_num,nvl(s.type_size,' ') as type_size,c.ENTER_STATE||'$$'||c.bill_num||'$$'||c.ctn_num||'$$'||c.SKU_ID||nvl2(c.asn, '$$'||c.asn,'') as lab, ");
			sb.append(" (select sum(p.num)  from base_client_pledge p where p.client=:pclientId and PTYPE=1 and p.bill_num=c.bill_num and p.ctn_num=c.ctn_num and p.sku_id=c.SKU_ID and p.enter_state=c.ENTER_STATE  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  p.WAREHOUSE_ID=:pckid");
				parme.put("pckid", ckId);
			}
			sb.append(" ) as pnum, ");
			sb.append(" (select sum(pp.num)  from base_client_pledge pp where pp.client=:pclientId and PTYPE=2 and pp.sku_id=c.SKU_ID and pp.enter_state=c.ENTER_STATE  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  pp.WAREHOUSE_ID=:pwarehouseid");
				parme.put("pwarehouseid", ckId);
			}
			sb.append(" ) as ppnum ");
			if(outCode!=null && !"".equals(outCode)){
				sb.append(" , nvl2( lodinfo.asn, (select sum(OUT_NUM) from bis_out_stock_info oui where oui.out_link_id=:outCode and oui.sku_id=c.SKU_ID and oui.enter_state=c.ENTER_STATE AND oui.ctn_num = c.ctn_num And oui.asn=c.asn AND oui.bill_num = c.bill_num "); 
				//bis_out_stock   IF_BUYER_PAY 条件out_link_id=:outCode 
				/*BisOutStock bos = outStockDao.find(outCode);
				if("1".equals(bos.getIfBuyerPay())){
					sb.append(" AND oui.asn = c.asn ");
				}*/
				
				sb.append(" ),lodinfo.outnum) as znum, "); 
				String inSQL="";
				if(ordCode!=null && !"".equals(ordCode)){
					inSQL="and ordi.loading_plan_num !=:ordCode";
					parme.put("ordCode", ordCode);
				}
				//yhn 20180111
				String inSQL1="";
				if(count == 0){
					inSQL1 = " and ordi.asn = c.asn ";
				}
				
				sb.append("(select sum(PIECE)  from bis_loading_order_info ordi where ordi.out_link_id=:outCode and ordi.sku_id=c.SKU_ID and ordi.enter_state=c.ENTER_STATE AND ordi.ctn_num = c.ctn_num AND ordi.BILL_NUM = c.bill_num ").append(inSQL).append(inSQL1).append(" ) as lnum ");
				parme.put("outCode", outCode);
			}
			sb.append(" from ( ");
			sb.append(" select sum(t.now_piece) as now_piece,ctn_num,bill_num,SKU_ID,ENTER_STATE,ASN,max(t.stock_in ) as stock_in,max(STOCK_NAME) as STOCK_NAME,max(BEFORE_STOCK_IN) as BEFORE_STOCK_IN from bis_tray_info t where NOW_PIECE>0 and  ISTRUCK='0' and t.stock_in=:clientId and CARGO_STATE='01' ");
			
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  WAREHOUSE_ID=:ckid");
				parme.put("ckid", ckId);
			}
			if(getBill!=null && !"".equals(getBill)){
				sb.append(" and  lower(t.BILL_NUM) like lower(:getBill)");
				parme.put("getBill", "%"+getBill+"%");
			}
			if(getSku!=null && !"".equals(getSku)){
				sb.append(" and  lower(t.SKU_ID) like lower(:getSku)");
				parme.put("getSku", "%"+getSku+"%");
			}
			if(getMr!=null && !"".equals(getMr)){
				sb.append(" and  lower(t.CTN_NUM) like lower(:getMr)");
				parme.put("getMr", "%"+getMr+"%");
			}
			if(getName!=null && !"".equals(getName)){
				sb.append(" and  lower(t.CARGO_NAME) like lower(:getName)");
				parme.put("getName", "%"+getName+"%");
			}
			if(outCode!=null && !"".equals(outCode)){
				sb.append(" and  exists( select 1 from bis_out_stock_info s where s.out_link_id=:orderCode and s.sku_id=t.sku_id and s.bill_num=t.bill_num and s.ctn_num=t.ctn_num and s.enter_state=t.enter_state ");
//				if(null!=getRk && !"".equals(getRk)){
//					sb.append(" and s.RK_NUM=:rkNum ");
//					parme.put("rkNum", getRk);
//				}
				sb.append(" ) ");
				parme.put("orderCode", outCode);
			}
			sb.append(" group by BILL_NUM,ASN,CTN_NUM,SKU_ID,ENTER_STATE ");
			sb.append(" ) c ");
			if(outCode!=null && !"".equals(outCode)){
				sb.append(" left join (select  lod.id,sku_id,enter_state,ctn_num,bill_num,sum(lod.OUT_NUM) outnum,max(lod.asn) asn from bis_out_stock_info lod where lod.out_link_id = :outCode group by lod.id,sku_id,enter_state,ctn_num,bill_num ORDER BY lod.id");
				sb.append("  ) lodinfo on  lodinfo.sku_id = c.SKU_ID  and lodinfo.enter_state = c.ENTER_STATE AND lodinfo.ctn_num = c.ctn_num  AND lodinfo.bill_num = c.bill_num  ");
			}
			sb.append(" left join base_sku_base_info s on s.sku_id=c.SKU_ID ");
			if(null!=getRk && !"".equals(getRk)){
				sb.append(" and s.RKDH like :rkNum  ");
				parme.put("rkNum", "%"+getRk+"%");
			}
			sb.append("  where s.del_flag=0 ");
			sb.append(" ) t");
			parme.put("pclientId", clientId);
			parme.put("clientId", clientId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	/**
	 * 按客户，sKU 分组获取库存数量(成品) 入库类型货损不统计
	 * @param clientId //客户
	 * @param ckId //仓库
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findClientSKUTrayList(String clientId,String ckId){
		if(clientId!=null){
//			StringBuffer sb=new StringBuffer("select t.*,nvl(t.pnum,0)as tnum  from ( select c.*, ");
//			sb.append(" (select p.num  from base_client_pledge p where p.client=:pclientId and PTYPE=2 and p.sku_id=c.SKU_ID and p.enter_state=c.ENTER_STATE ) as pnum ");
//			sb.append(" from ( ");
//			sb.append(" select sum(t.now_piece) as now_piece,SKU_ID,max(ENTER_STATE),max(t.stock_in ) as stock_in,max(STOCK_NAME) as STOCK_NAME,max(BEFORE_STOCK_IN) as BEFORE_STOCK_IN from bis_tray_info t where t.stock_in=:clientId and CARGO_STATE='01' and ENTER_STATE='0' group by SKU_ID ");
//			sb.append(" ) c ) t");
			HashMap<String,Object> parme=new HashMap<String,Object>();
			StringBuffer sb=new StringBuffer("select t.*,nvl(t.pnum,0)as tnum  from ( select c.*, ");//and PTYPE=2
			sb.append(" (select sum(p.num)  from base_client_pledge p where p.client=:pclientId  and p.sku_id=c.SKU_ID  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  p.WAREHOUSE_ID=:pckid");
				parme.put("pckid", ckId);
			}
			sb.append("   ) as pnum ");
			sb.append(" from ( ");
			sb.append(" select sum(t.now_piece) as now_piece,SKU_ID,max(t.stock_in ) as stock_in,max(STOCK_NAME) as STOCK_NAME,max(BEFORE_STOCK_IN) as BEFORE_STOCK_IN from bis_tray_info t where NOW_PIECE>0 and  ISTRUCK='0' and t.stock_in=:clientId and CARGO_STATE='01'  ");
			if(ckId!=null && !"".equals(ckId)){
				sb.append(" and  WAREHOUSE_ID=:ckid");
				parme.put("ckid", ckId);
			}
			sb.append("  group by SKU_ID ) c ) t");
			parme.put("pclientId", clientId);
			parme.put("clientId", clientId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	
	/**
	 * 获取托盘信息
	 * @param ckId 仓库id
	 * @param userid 客户id
	 * @param billNum 提单号
	 * @param ctnNum 厢号
	 * @param sku 
	 * @param state 入库类型
	 * @param sqlIf 策略sql条件
	 * @param sqlOrd  策略排序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTrayList(String ckId, String userid, String billNum, String ctnNum, String asn, String sku, String state, String sqlIf, String sqlOrd, Integer pickNum, String chooseFloor, String chooseRoomNum) {
		if (userid != null && billNum != null && ctnNum != null && sku != null && state != null) {
			if (!"".equals(userid) && !"".equals(billNum) && !"".equals(ctnNum) && !"".equals(sku) && !"".equals(state)) {

				String floorNum = "";
				String roomNum = "";
				if (StringUtils.isNotEmpty(chooseFloor) && StringUtils.isNotEmpty(chooseRoomNum)) {
					floorNum = chooseFloor;
					roomNum = chooseRoomNum;
				} else {
					// 查找所有可拣货的仓库楼层
					List<Map<String, Object>> pickFloorList = listCanPickFloor(ckId, userid, billNum, ctnNum, asn, sku, state, sqlIf, pickNum);
					if (!CollectionUtils.isEmpty(pickFloorList) && pickFloorList.size() > 1) {
						// 货物存放于多楼层的,采用轮训取货的方式
						// 获取该提单最后一车配载的楼层
						Map<String, Object> lastFloorRoom = getLastFloor(billNum, ctnNum, sku);
						int startNum = 0;
						if (Objects.nonNull(lastFloorRoom) && lastFloorRoom.containsKey("FLOORNUM")) {
							String floorNumLast = (String) lastFloorRoom.get("FLOORNUM");
							String roomNumLast = (String) lastFloorRoom.get("ROOMNUM");

							for (int i = 0; i < pickFloorList.size(); i++) {
								Map<String, Object> floorRoom = pickFloorList.get(i);
								String floorNumNow = (String) floorRoom.get("FLOORNUM");
								String roomNumNow = (String) floorRoom.get("ROOMNUM");
								if (floorNumLast.equals(floorNumNow) && roomNumLast.equals(roomNumNow)) {
									startNum = i + 1;
									break;
								}
								int floorNumLastInt = Integer.parseInt(floorNumLast);
								int roomNumLastInt = Integer.parseInt(roomNumLast);
								int floorNumNowInt = Integer.parseInt(floorNumNow);
								int roomNumNowInt = Integer.parseInt(roomNumNow);

								if (floorNumNowInt > floorNumLastInt
										|| (floorNumNowInt == floorNumLastInt && roomNumNowInt > roomNumLastInt)) {
									startNum = i;
									break;
								}
							}
						}
						if (startNum >= pickFloorList.size()) {
							startNum = 0;
						}
						Map<String, Object> chooseFloorRoom = pickFloorList.get(startNum);
						floorNum = (String) chooseFloorRoom.get("FLOORNUM");
						roomNum = (String) chooseFloorRoom.get("ROOMNUM");
					}
				}

				HashMap<String, Object> parme = new HashMap<String, Object>();
				StringBuffer sbSQL = new StringBuffer("select t.ID,t.TRAY_ID,t.NOW_PIECE,t.NET_WEIGHT,t.GROSS_WEIGHT from bis_tray_info t where t.NOW_PIECE>0 and t.ISTRUCK='0' and ");
				sbSQL.append(" t.stock_in=:userid and t.bill_num=:billNum and t.ctn_num=:ctnNum ");
				if (!StringUtils.isNull(asn) && !"".equals(asn)) {
					sbSQL.append(" and t.asn=:asn ");
					parme.put("asn", asn);
				}
				sbSQL.append(" and t.sku_id=:sku and t.enter_state=:state ");

				parme.put("userid", userid);
				parme.put("billNum", billNum);
				parme.put("ctnNum", ctnNum);
				parme.put("sku", sku);
				parme.put("state", state);
				if (ckId != null && !"".equals(ckId)) {
					sbSQL.append(" and t.WAREHOUSE_ID=:ckId ");
					parme.put("ckId", ckId);
				}
				if (StringUtils.isNotEmpty(floorNum)) {
					sbSQL.append(" and t.floor_num = :floorNum ");
					sbSQL.append(" and t.room_num = :roomNum ");
					parme.put("floorNum", floorNum);
					parme.put("roomNum", roomNum);
				}
				if (sqlIf != null && !"".equals(sqlIf)) {
					sbSQL.append(" and ").append(sqlIf);
				}
				sbSQL.append(" order by ").append(sqlOrd);
				SQLQuery sqlQuery = createSQLQuery(sbSQL.toString(), parme);
				return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}
		}
		return null;
	}


	/**
	 * 根据提单号和箱号查询最后一车装车仓库
	 *
	 * @param billNum
	 * @param ctnNum
	 * @param sku
	 * @return
	 */
	private Map<String, Object> getLastFloor(String billNum, String ctnNum, String sku) {

		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append(" select t.floor_num floorNum,t.room_num roomNum ");
		sbSQL.append("   from bis_loading_info t ");
		sbSQL.append("  where t.bill_num = :billNum ");
		sbSQL.append("    and t.ctn_num = :ctnNum ");
		sbSQL.append("    and t.sku_id = :sku ");
		sbSQL.append("  order by t.id desc ");

		HashMap<String, Object> parme = Maps.newHashMap();
		parme.put("billNum", billNum);
		parme.put("ctnNum", ctnNum);
		parme.put("sku", sku);

		SQLQuery sqlQuery = createSQLQuery(sbSQL.toString(), parme);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> flootList = sqlQuery.list();
		if (!CollectionUtils.isEmpty(flootList)) {
			return flootList.get(0);
		}
		return Maps.newHashMap();
	}

	/**
	 * 获取所有可拣货的楼层
	 *
	 * @param ckId    仓库id
	 * @param userid  客户id
	 * @param billNum 提单号
	 * @param ctnNum  厢号
	 * @param sku
	 * @param state   入库类型
	 * @param sqlIf   策略sql条件
	 * @return
	 */
	private List<Map<String, Object>> listCanPickFloor(String ckId, String userid, String billNum, String ctnNum, String asn, String sku, String state, String sqlIf, Integer pickNum) {
		if (userid != null && billNum != null && ctnNum != null && sku != null && state != null) {
			if (!"".equals(userid) && !"".equals(billNum) && !"".equals(ctnNum) && !"".equals(sku) && !"".equals(state)) {
				HashMap<String, Object> parme = Maps.newHashMap();
				StringBuffer sbSQL = new StringBuffer();
				sbSQL.append(" select a.floor_num floorNum, a.room_num roomNum ");
				sbSQL.append("	 from ( 					");
				sbSQL.append(" select t.floor_num, t.room_num, sum(t.now_piece) allNum ");
				sbSQL.append("   from bis_tray_info t ");
				sbSQL.append("  where t.now_piece > 0 ");
				sbSQL.append("    and t.istruck = '0'       ");
				sbSQL.append("    and t.cargo_state = '01'   ");
				sbSQL.append("    and t.stock_in = :userid ");
				sbSQL.append("    and t.bill_num = :billNum ");
				sbSQL.append("    and t.ctn_num = :ctnNum ");
				if (!StringUtils.isNull(asn) && !"".equals(asn)) {
					sbSQL.append(" and t.asn=:asn ");
					parme.put("asn", asn);
				}
				sbSQL.append("    and t.sku_id = :sku ");
				sbSQL.append("    and t.enter_state = :state ");

				parme.put("userid", userid);
				parme.put("billNum", billNum);
				parme.put("ctnNum", ctnNum);
				parme.put("sku", sku);
				parme.put("state", state);
				if (ckId != null && !"".equals(ckId)) {
					sbSQL.append(" and t.WAREHOUSE_ID = :ckId ");
					parme.put("ckId", ckId);
				}
				if (sqlIf != null && !"".equals(sqlIf)) {
					sbSQL.append(" and ").append(sqlIf);

				}
				sbSQL.append("  group by t.floor_num, t.room_num ) a where a.allNum > :allNum order by a.floor_num, a.room_num ");
				parme.put("allNum", pickNum);

				SQLQuery sqlQuery = createSQLQuery(sbSQL.toString(), parme);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> list = sqlQuery.list();
				if (!CollectionUtils.isEmpty(list)) {
					return list;
				}
				return list;
			}
		}
		return null;
	}

	/*
	 * yhn20170822
	 * 
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String,Object>> findTrayListForSample(String stockName,String billNum,String ctnNum,String asn,String skuId,String trayId){
		if(stockName!=null && billNum!=null && ctnNum!=null && skuId!=null && asn!=null){
			if(!"".equals(stockName) && !"".equals(billNum) && !"".equals(ctnNum)  && !"".equals(skuId)  && !"".equals(asn)){
				StringBuffer sbSQL=new StringBuffer("select t.ID from bis_tray_info t where t.NOW_PIECE>0 and t.ISTRUCK='0' and ");
				sbSQL.append(" t.cargo_state='01' and t.stock_name=:stockName and t.bill_num=:billNum and t.ctn_num=:ctnNum and t.sku_id=:skuId ");
				sbSQL.append(" and t.asn=:asn and t.tray_id=:trayId ");
				HashMap<String,Object> parme=new HashMap<String,Object>();
				parme.put("stockName", stockName);
				parme.put("billNum", billNum);
				parme.put("ctnNum", ctnNum);
				parme.put("skuId", skuId);
				parme.put("asn", asn);
				parme.put("trayId", trayId);
				sbSQL.append(" order by t.tray_id desc");
				SQLQuery sqlQuery=createSQLQuery(sbSQL.toString(), parme);
				return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}
		}
		return null;
	}
	
	/*
	 * yhn20171027
	 * 
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String,Object>> findTrayListForCheck(String contactNum){
		if(contactNum!=null && !"".equals(contactNum)){
			StringBuffer sbSQL=new StringBuffer("select t.ID from bis_tray_info t where t.CONTACT_NUM =:contactNum");
			sbSQL.append(" and (t.ISTRUCK='1' or(t.cargo_state >'01' and t.cargo_state < '99') or (t.stock_in <> t.before_stock_in)) ");
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("contactNum", contactNum);
			SQLQuery sqlQuery=createSQLQuery(sbSQL.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	
	
	/**
	 * 排除策略拣货获取托盘信息
	 * @param ckId 仓库id
	 * @param userid 客户id
	 * @param billNum 提单号
	 * @param ctnNum 厢号
	 * @param sku 
	 * @param state 入库类型
	 * @param sqlOrd  策略排序
	 * @param notIds  要排除的主键集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String,Object>> findNoIfTrayList(String ckId,String userid,String billNum,String ctnNum,String sku,String state,String sqlOrd,String notIds){
		if(userid!=null && billNum!=null && ctnNum!=null && sku!=null && state!=null){
			if(!"".equals(userid) && !"".equals(billNum) && !"".equals(ctnNum)  && !"".equals(sku)  && !"".equals(state) ){
				StringBuffer sbSQL=new StringBuffer("select ti.ID,ti.TRAY_ID,ti.NOW_PIECE,ti.NET_WEIGHT,ti.GROSS_WEIGHT from bis_tray_info ti where  ti.NOW_PIECE>0 and ti.ISTRUCK='0' and ");
				sbSQL.append(" ti.stock_in=:userid and ti.bill_num=:billNum and ti.ctn_num=:ctnNum and ti.sku_id=:sku and ti.enter_state=:state ");
				HashMap<String,Object> parme=new HashMap<String,Object>();
				parme.put("userid", userid);
				parme.put("billNum", billNum);
				parme.put("ctnNum", ctnNum);
				parme.put("sku", sku);
				parme.put("state", state);
				if(ckId!=null && !"".equals(ckId)){
					sbSQL.append(" and ti.WAREHOUSE_ID=:ckId ");
					parme.put("ckId", ckId);
				}
				if(notIds!=null && !"".equals(notIds)){
					sbSQL.append(" and ti.ID not in ("+notIds.substring(0,notIds.length()-1)+") ");
				}
				sbSQL.append(" order by ").append(sqlOrd);
				SQLQuery sqlQuery=createSQLQuery(sbSQL.toString(), parme);
				return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}
		}
		return null;
	}		
	
	/**
	 * 根据id集合批量锁定库存明细--出库
	 * @param ids 最后一位为,
	 */
	@Transactional(rollbackFor =Exception.class)
	public void lockTrayList(String ids){
		if(ids!=null && !"".equals(ids)){
			/*String sql="update bis_tray_info t set t.istruck='1',t.CARGO_STATE='10' where t.id in ("+ids.substring(0,ids.length()-1)+")";
			SQLQuery sqlQuery=createSQLQuery(sql, new HashMap<String,Object>());
			sqlQuery.executeUpdate();*/
			String idstring = ids.substring(0, ids.length() - 1);

			String[]idlist=  idstring.split(",");
			for(int i=0;i<idlist.length;i++){
				String sql="update bis_tray_info t set t.istruck='1',t.CARGO_STATE='10' where t.id = "+idlist[i]+" ";
				SQLQuery sqlQuery=createSQLQuery(sql, new HashMap<String,Object>());
				sqlQuery.executeUpdate();

			}

		}
	}
	
	/**
	 * 根据id集合批量锁定库存明细--货转
	 * @param ids 最后一位为,
	 * @param userId 存货方id
	 * @param userName 存放方
	 */
	@Transactional(rollbackFor =Exception.class)
	public void UsrTrayList(String ids,String userId,String userName){
		if(ids!=null && !"".equals(ids) && userId!=null && !"".equals(userId) && userName!=null && !"".equals(userName)){
			/*String sql="update bis_tray_info t set t.before_stock_name=t.stock_name,t.before_stock_in=t.stock_in,t.stock_in=:userid,t.stock_name=:username where t.id in ("+ids.substring(0,ids.length()-1)+")";
			HashMap<String,Object> parm=new HashMap<String,Object>();
			parm.put("userid", userId);
			parm.put("username", userName);
			SQLQuery sqlQuery=createSQLQuery(sql,parm );
			sqlQuery.executeUpdate();*/

			String idstring = ids.substring(0, ids.length() - 1);

			String[]idlist=  idstring.split(",");
			for(int i=0;i<idlist.length;i++){
				String sql="update bis_tray_info t set t.before_stock_name=t.stock_name,t.before_stock_in=t.stock_in,t.stock_in=:userid,t.stock_name=:username where t.id = "+idlist[i]+" ";
				HashMap<String,Object> parm=new HashMap<String,Object>();
				parm.put("userid", userId);
				parm.put("username", userName);
				SQLQuery sqlQuery=createSQLQuery(sql,parm );
				sqlQuery.executeUpdate();

			}

		}
	}
	
	/**
	 * 根据订单id解锁锁定库存明细
	 * @param ordCode 订单id
	 */
	public void unlockTrayList(String ordCode){
		if(ordCode!=null && !"".equals(ordCode)){
			String sql="update bis_tray_info ti set ti.istruck='0',ti.CARGO_STATE='01' where exists (select * from bis_loading_info t where t.loading_plan_num=:ordCode and t.tray_id=ti.tray_id)";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("ordCode", ordCode);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
	/**
	 * @author GLZ
	 * 通过SQL语句，根据ASNid获取库存对象列表
	 * @author PYL
	 * @param ans
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAsnInfoByAsn(String asn) {
		StringBuffer sb=new StringBuffer("select a.*,(a.ORIGINAL_PIECE-a.REMOVE_PIECE) as rkNum,b.INBOUND_DATE as inboundTime,c.TYPE_SIZE as typesize,c.CARGO_NAME as cargoname");
		sb.append(" from bis_asn b , bis_tray_info a,base_sku_base_info c where 1 = 1 ");
		sb.append(" and b.asn = :asn ");
		sb.append(" and a.asn = b.asn ");
		sb.append(" and a.sku_id = c.sku_id ");
//		sb.append("GROUP BY client,client_name,bill_num,ctn_num,sku_id,pname,ptype");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("asn", asn);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据 提单号   获得  总数量，总净重，总毛重
	 * @date 2016年4月19日 下午4:57:48 
	 * @param trayInfo
	 * @return
	 * @throws
	 */
	public List<TrayInfo> getSumNumByBillNum(TrayInfo trayInfo){
		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " t.bill_num AS billNum, "
                + " SUM(NVL(t.Original_Piece,0)) AS originalPiece,  "
                + " SUM(NVL(t.remove_piece,0)) AS removePiece, "
                + " SUM(NVL(t.Original_Piece,0) - NVL(t.remove_piece,0)) AS nowPiece, "
                + " SUM((NVL(t.Original_Piece,0) - NVL(t.remove_piece,0)) * NVL(NET_SINGLE,0)) AS netWeight,  "
                + " SUM((NVL(t.Original_Piece,0) - NVL(t.remove_piece,0)) * NVL(GROSS_SINGLE,0)) AS grossWeight  "
                + " FROM BIS_TRAY_INFO t "
                + " WHERE 1=1 ");
		sql.append(" AND t.bill_num = :billNum ");
		params.put("billNum", trayInfo.getBillNum());
		
		sql.append(" GROUP BY t.bill_num ");
		
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("billNum", String.class);//提单号
		parm.put("originalPiece", Integer.class);//原始  总件数
		parm.put("removePiece", Integer.class);//拆托  总件数
		parm.put("nowPiece", Integer.class);//现有 总件数
		parm.put("netWeight", Double.class);//总净重
		parm.put("grossWeight", Double.class);//总毛重
		
		return findSql(sql.toString(), parm, params);
	}


	//根据箱号获得现有件数
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCtnList(String ctnNum) {
		StringBuffer sb=new StringBuffer("select ctn_num , sum(NOW_PIECE) as piece ");
		sb.append(" from bis_tray_info  where 1 = 1 ");
		sb.append(" and ctn_num = ?0 ");
		sb.append(" group by ctn_num ");
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), ctnNum);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * 修改库存件数
	 */
	public void changeTrayNum(String trayId,Integer newNum, Double net, Double gross){
		if(trayId!=null && !"".equals(trayId) && newNum !=null){
			String sql="update bis_tray_info ti set ti.now_piece=:newNum,ti.original_piece=:newNum,ti.NET_WEIGHT=:net,ti.GROSS_WEIGHT=:gross where ti.tray_id = :trayId";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("newNum", newNum);
			parme.put("net", net);
			parme.put("gross", gross);
			parme.put("trayId", trayId);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	
	 //根据SKU获取库存
	 @SuppressWarnings("unchecked")
	public List<Map<String, Object>> GetTrayInfoBySku(String skuid) {
			StringBuffer sb=new StringBuffer("select  * from bis_tray_info where sku_id=?0 "); 
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), skuid);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}

	 //出库装车、完结是更新一遍asnaction的库存
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateAsnAction(BisLoadingInfo info) {
		Map<String,Object> params=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select sum(t.now_piece) as piece,sum(t.gross_weight) as gross,sum(t.net_weight) as net from bis_tray_info t ");
		sb.append(" where t.cargo_state!='99' "); 
		if(StringUtils.isNull(info.getAsnId())){
			sb.append(" and t.asn is null ");
		}else{
			sb.append(" and t.asn = :asn ");
			params.put("asn", info.getAsnId());
		}
		sb.append(" and t.sku_id=:sku and t.stock_in=:clientId ");
		params.put("sku", info.getSkuId());
		params.put("clientId", info.getStockId());
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	//货转时更新一遍asnaction的数量(暂时弃用)
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> updateAsnAction(TrayInfo obj, String trayIds, String stockIn) {
		Map<String,Object> params=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select sum(t.now_piece) as piece,sum(t.gross_weight) as gross,sum(t.net_weight) as net from bis_tray_info t ");
		sb.append(" where t.cargo_state!='99' and t.id not in ("+trayIds.substring(0,trayIds.length()-1)+")"); 
//		params.put("trayId", obj.getTrayId());
		if(StringUtils.isNull(obj.getAsn())){
			sb.append(" and t.asn is null ");
		}else{
			sb.append(" and t.asn = :asn ");
			params.put("asn", obj.getAsn());
		}
		sb.append(" and t.sku_id=:sku and t.stock_in=:clientId ");
		params.put("sku", obj.getSkuId());
		params.put("clientId", stockIn);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	/**
	 * 关联修改库存的箱号 20171029 yhn
	 * @param ctnNumNew
	 * @param ctnNumOld
	 * @param linkId
	 * @return
	 */
	public String updateTrayInfoforNewCtnNum(String ctnNumNew,String ctnNumOld,String linkId) {

        Map<String, String> params = new HashMap<>();
        String sql = "update BIS_TRAY_INFO t set t.ctn_num =:ctnNumNew where t.ctn_num=:ctnNumOld and t.contact_num=:linkId";

        if (StringUtils.nonNull(ctnNumNew)) {
            
            params.put("ctnNumNew", ctnNumNew);
        }else{
        	params.put("ctnNumNew", "");
        }
        
        if (StringUtils.nonNull(ctnNumOld)) {
            
            params.put("ctnNumOld", ctnNumOld);
        }else{
        	params.put("ctnNumOld", "");
        }

        
        if (StringUtils.nonNull(linkId)) {
            
            params.put("linkId", linkId);
        }else{
        	params.put("linkId", "");
        }
        
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        int rows = sqlQuery.executeUpdate();

        return rows > 0 ? "success" : "";
    }
	/**
	 * 查询bis_out_socket中outCode条件 的asn为空的记录数
	 * @param outCode 
	 * @return
	 */
	public int selectIfASNOfBisOutStockInfo(String outCode){
		Map<String,String> params = new HashMap<String,String>();
		String sql = "select id from BIS_OUT_STOCK_INFO t where t.asn is null and t.out_link_id=:outCode";
        if (StringUtils.nonNull(outCode)) {
            params.put("outCode", outCode);
        }else{
        	params.put("outCode", "");
        }
        SQLQuery sqlQuery = createSQLQuery(sql,params);
        int count =  sqlQuery.executeUpdate();
        return count;
	}
}
