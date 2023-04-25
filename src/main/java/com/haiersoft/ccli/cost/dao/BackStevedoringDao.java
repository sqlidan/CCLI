package com.haiersoft.ccli.cost.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisBackSteveDoring;

@Repository
public class BackStevedoringDao  extends HibernateDao<BisBackSteveDoring, Integer> {
	
	/*
	 * 根据装车号获取出库装车单信息
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getLoadingInfoByNum(String loadingNum) {
		StringBuffer sb=new StringBuffer("select a.*,b.CLIENT_NAME as name,c.TYPE_SIZE as typeSize, ");
		sb.append(" from bis_loading_info a,base_client_info b,base_sku_base_info c,bis_out_stock d where 1 = 1 ");
		sb.append("and loadingTruckNum = :loadingTruckNum ");
		sb.append("and a.STOCK_ID = b.IDS ");
		sb.append("and a.SKU_ID = c.SKU_ID ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loadingTruckNum", loadingNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	//根据装车号获取出装卸单信息
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBackStevedoring(String billNum) {
		StringBuffer sb=new StringBuffer("select * ");
		sb.append(" from bis_back_stevedoring where 1 = 1 ");
		sb.append("and bill_num = :billNum ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billNum", billNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String billNum) {
		if(billNum!=null && !"".equals(billNum)){
			String sql="update bis_back_stevedoring  set if_ok = '1' where bill_num = :billNum";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("billNum", billNum);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	} 
	
	/*
	 * 更新 取消完成 状态
	 */
	public void updateState2(String billNum) {
		if(billNum!=null && !"".equals(billNum)){
			String sql="update bis_back_stevedoring  set if_ok = '0',STANDINGBOOKIDS='' where bill_num = :billNum";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("billNum", billNum);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	} 
}
