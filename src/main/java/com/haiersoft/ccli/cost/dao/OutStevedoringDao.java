package com.haiersoft.ccli.cost.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BisOutSteveDoring;

@Repository
public class OutStevedoringDao  extends HibernateDao<BisOutSteveDoring, Integer> {
	
	/*
	 * 根据装车号获取出库装车单信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLoadingInfoByNum(String loadingNum) {
		StringBuffer sb=new StringBuffer("select a.*,b.CLIENT_NAME as name,c.TYPE_SIZE as typeSize ");
		sb.append(" from bis_loading_info a,base_client_info b,base_sku_base_info c where 1 = 1 ");
		sb.append("and loading_truck_num = :loadingTruckNum ");
		sb.append("and loading_state = '2' ");
		sb.append("and a.STOCK_ID = b.IDS ");
		sb.append("and a.SKU_ID = c.SKU_ID ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loadingTruckNum", loadingNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	//根据装车号获取出装卸单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOutStevedoring(String loadingNum) {
		StringBuffer sb=new StringBuffer("select * ");
		sb.append(" from bis_out_stevedoring where 1 = 1 ");
		sb.append("and  loading_num = :loadingNum ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loadingNum", loadingNum);
		SQLQuery sqlQuery=createSQLQuery(sb.toString(), params);
		return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/*
	 * 更新 是否完成 跟台账id
	 */
    public void updateIfok(BisOutSteveDoring bisOutSteveDoring) {
    	if(bisOutSteveDoring!=null){
			String sql="update bis_out_stevedoring set if_ok = '1',drostanindbookids=:drostanindbookids where id=:id";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("id", bisOutSteveDoring.getId());
			parme.put("drostanindbookids",bisOutSteveDoring.getDroStanindBookIds());
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String loadingNum) {
		if(loadingNum!=null && !"".equals(loadingNum)){
			String sql="update bis_out_stevedoring set if_ok = '1' where loading_num = :loadingNum";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("loadingNum", loadingNum);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	} 
	
	/*
	 * 更新 取消完成 状态
	 */
	public void updateState2(String loadingNum) {
		if(loadingNum!=null && !"".equals(loadingNum)){
			String sql="update bis_out_stevedoring  set if_ok = '0',DROSTANINDBOOKIDS='' where loading_num = :loadingNum";
			Map<String,Object> parme=new HashMap<String,Object>();
			parme.put("loadingNum", loadingNum);
			SQLQuery sqlQuery=createSQLQuery(sql, parme);
			sqlQuery.executeUpdate();
		}
	}
 
}
