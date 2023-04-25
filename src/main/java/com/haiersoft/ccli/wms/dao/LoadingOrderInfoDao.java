package com.haiersoft.ccli.wms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;

@Repository
public class LoadingOrderInfoDao  extends HibernateDao<BisLoadingOrderInfo, String> {
	/**
	 * 根据orderId删除对应所有出库订单明细
	 * @param asnId
	 * @return
	 */
	public void deleteOrderInfos(String orderId){
		if(orderId!=null && !"".equals(orderId)){
			String hql="delete BisLoadingOrderInfo a where a.loadingPlanNum=?0";
			batchExecute(hql, orderId);
		}
	}
	
	/**
	 * 根据订单号获取订单明细信息
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findInfoList(String orderId){
		if(orderId!=null && !"".equals(orderId)){
			StringBuffer sb=new StringBuffer();
			HashMap<String,Object> parme=new HashMap<String,Object>();
			//,s.net_single,s.gross_single 
			sb.append(" select o.TYPE_SIZE,o.LOADING_PLAN_NUM,o.OUT_LINK_ID,o.CTN_NUM,o.ASN,o.SKU_ID,o.PIECE,o.LOADING_TIEM,o.LOADING_TRUCK_NUM,o.GROSS_WEIGHT,o.LOT_NUM,o.MSC_NUM,o.NET_WEIGHT,o.UNITS,o.OPERATOR,o.BILL_NUM,o.STOCK_ID,o.STOCK_NAME,o.NET_SINGLE,o.GROSS_SINGLE,o.CARGO_NAME,o.ENTER_STATE,o.RK_NUM,o.REMARK1,s.producing_area,o.ENTER_STATE || '$$' || o.bill_num || '$$' ||o.ctn_num || '$$' ||o.SKU_ID||nvl2(o.asn, '$$'||o.asn,'')  as lab from ");
			sb.append(" (select TYPE_SIZE,LOADING_PLAN_NUM,OUT_LINK_ID,CTN_NUM,ASN,SKU_ID,PIECE,LOADING_TIEM,LOADING_TRUCK_NUM,GROSS_WEIGHT,NET_WEIGHT,UNITS,OPERATOR,BILL_NUM,STOCK_ID,STOCK_NAME,NET_SINGLE,GROSS_SINGLE,CARGO_NAME,LOT_NUM,MSC_NUM,ENTER_STATE,RK_NUM,REMARK1 from bis_loading_order_info  l where l.loading_plan_num=:orderid ) o ");
			sb.append(" left join base_sku_base_info   s on o.sku_id=s.sku_id where s.DEL_FLAG=0 ");
			parme.put("orderid", orderId);
			SQLQuery sqlQuery=createSQLQuery(sb.toString(), parme);
			return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
		return null;
	}
	/**
	 * 根据出库订单id，提单号，厢号，SKU，入库类型获取明细对象
	 * @param orderNum //出库订单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return BisLoadingOrderInfo
	 */
	public BisLoadingOrderInfo findOrderInfoObj(String orderNum,String billNum,String cnNum,String skuNum,String isLab,String asn){
		BisLoadingOrderInfo retObj=null;
		if(orderNum!=null && !"".equals(orderNum) && billNum!=null &&  !"".equals(billNum) && cnNum!=null &&!"".equals(cnNum) && skuNum!=null && !"".equals(skuNum) && isLab!=null && !"".equals(isLab)){
			String hql="from BisLoadingOrderInfo where loadingPlanNum=:order and billNum=:billNum and ctnNum=:ctnNum and skuId=:skuId and enterState=:isLab";
			
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("order", orderNum);
			parme.put("billNum", billNum);
			parme.put("ctnNum", cnNum);
			parme.put("skuId", skuNum);
			parme.put("isLab", isLab);
			if(asn!=null&&!"".equals(asn)  ){
				hql = hql + " and asn=:asn";
				parme.put("asn", asn);
			}
			List<BisLoadingOrderInfo> getList=this.find(hql, parme);
			if(getList!=null && getList.size()>0){
				retObj=getList.get(0);
			}
		}
		return retObj;
	}
	/**
	 * 根据出库订单id，提单号，厢号，SKU，入库类型删除明细对象
	 * @param orderNum //出库订单id
	 * @param billNum  //提单号
	 * @param cnNum  //厢号
	 * @param skuNum //SKU
	 * @param isLab //入库类型
	 * @return void
	 */
	public void delOrderInfoObj(String orderNum,String billNum,String cnNum,String skuNum,String isLab){
		if(orderNum!=null && !"".equals(orderNum) && billNum!=null &&  !"".equals(billNum) && cnNum!=null &&!"".equals(cnNum) && skuNum!=null && !"".equals(skuNum) && isLab!=null && !"".equals(isLab)){
			String hql="delete BisLoadingOrderInfo where loadingPlanNum=:order and billNum=:billNum and ctnNum=:ctnNum and skuId=:skuId and enterState=:isLab";
			HashMap<String,Object> parme=new HashMap<String,Object>();
			parme.put("order", orderNum);
			parme.put("billNum", billNum);
			parme.put("ctnNum", cnNum);
			parme.put("skuId", skuNum);
			parme.put("isLab", isLab);
			batchExecute(hql, parme);
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据 出库联系单  获得最大出库时间   的对象数据
	 * @date 2016年4月14日 下午3:12:33 
	 * @param linkId
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unchecked")
    public BisLoadingOrderInfo getOrderInfoByLinkIdForMaxDate(String linkId){
		BisLoadingOrderInfo loadingOrderInfo = new BisLoadingOrderInfo();
		
		StringBuffer sql = new StringBuffer(""
				+ " SELECT "
				+ " l.out_link_id AS outLinkId, "
				+ " MAX(l.loading_tiem) AS loadingTiem "
				+ " FROM bis_loading_order_info l "
				+ " WHERE 1=1 " );
		sql.append(" AND l.out_link_id = ?0 ");
		sql.append(" GROUP BY l.out_link_id ");		
		
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), linkId);	
		sqlQuery.addScalar("outLinkId", StandardBasicTypes.STRING);// 出库联系单ID
		sqlQuery.addScalar("loadingTiem", StandardBasicTypes.DATE);//装车时间
		sqlQuery.setResultTransformer(Transformers.aliasToBean(BisLoadingOrderInfo.class));
		
		List<BisLoadingOrderInfo> loadingOrderInfos = sqlQuery.list();
		if(null != loadingOrderInfos && loadingOrderInfos.size() > 0){
			loadingOrderInfo = loadingOrderInfos.get(0);
		}
		return loadingOrderInfo;
	}
	
}
