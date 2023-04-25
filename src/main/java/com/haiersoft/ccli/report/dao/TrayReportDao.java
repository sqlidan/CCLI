package com.haiersoft.ccli.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.hibernate.SQLQuery; 
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao; 
import com.haiersoft.ccli.report.entity.Stock;

@Repository
public class TrayReportDao extends HibernateDao<Stock, String> {

	/**
	 * 获取已使用的库房号 by slh 20160623 
	 * @param BuildingNum 楼号  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayInfoByBuiNum(String BuildingNum) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.CARGO_LOCATION,t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num from bis_Tray_info t where t. CARGO_STATE='01' and  t.BUILDING_NUM=:BUILDING_NUM and t.STOREROOM_NUM is not null   and t.AREA_NUM is not null ");
		sql.append(" group by t.CARGO_LOCATION,t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num");
		sql.append(" order by t.building_num ,t.storeroom_num");

		HashMap<String, Object> parme = new HashMap<String, Object>();
		parme.put("BUILDING_NUM", BuildingNum);

		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
	/**
	 * 获取已使用的库房号 by slh 20160623 
	 * @param BuildingNum 楼号   + 楼号 housid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayInfo(String BuildingNum,String housid) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer(); //warehouse_id
		HashMap<String, Object> parme = new HashMap<String, Object>();
		
		sql.append(" select t.CARGO_LOCATION,t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num from bis_Tray_info t where ");
		
		if(!"".equals(BuildingNum) && null != BuildingNum){
			sql.append("   t.BUILDING_NUM=:BUILDING_NUM and");
		    parme.put("BUILDING_NUM", BuildingNum);
		}
		
		if(!"".equals(housid) && null != housid){
			 sql.append("  t.warehouse_id=:warehouse_id and ");
			 parme.put("warehouse_id", housid);
		}
		sql.append(" t. CARGO_STATE='01'  and t.STOREROOM_NUM is not null   and t.AREA_NUM is not null ");
		sql.append(" group by t.CARGO_LOCATION,t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num");
		sql.append(" order by t.building_num ,t.storeroom_num"); 
		 
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
	
	/**
	 * 获取库房货物统计  by slh 20160625 
	 * @param BuildingNum 楼号   + 楼号 housid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayStoreInfo(String BuildingNum,String housid) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer(); //warehouse_id
		HashMap<String, Object> parme = new HashMap<String, Object>();
		String strsql="";
		if(!"".equals(BuildingNum) && null != BuildingNum){
			strsql=" BUILDING_NUM=:BUILDING_NUM  and ";
		    parme.put("BUILDING_NUM", BuildingNum);
		}
		
		if(!"".equals(housid) && null != housid){
			 strsql=strsql.trim().length()<=0?" warehouse_id=:warehouse_id and ":(strsql+" warehouse_id=:warehouse_id and ") ;
			 parme.put("warehouse_id", housid);
		}
	
		sql.append(" select A.building_num,A.storeroom_num,A.cargo_type,A.type_name,A.class_type,A.class_name,round(sum(A.dm),3) as dm ,round(sum(A.dj),3) as dj ,'1' as isxj from (");
		sql.append(" select bsku.class_type,bsku.cargo_type,bsku.type_name,decode(bsku.class_name,'其他', (bsku.type_name||'-'||bsku.class_name),bsku.class_name) as class_name,t.SKU_ID,t.NET_SINGLE,t.GROSS_SINGLE,t.NOW_PIECE,nvl(t.NOW_PIECE,0)*nvl(t.NET_SINGLE,0)/1000 as dj ,nvl(t.NOW_PIECE,0)*nvl(t.GROSS_SINGLE,0)/1000 as dm,");
		sql.append(" t.CARGO_LOCATION, t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num from ( ");
		sql.append(" select SKU_ID,GROSS_SINGLE,NOW_PIECE,NET_SINGLE, CARGO_LOCATION,building_num,floor_num,room_num,area_num ,storeroom_num from bis_tray_info where");
		sql.append(strsql);
		sql.append(" FLOOR_NUM <> 7 AND FLOOR_NUM <> 9 AND (CARGO_STATE = '01' or CARGO_STATE = '10' ) and STOREROOM_NUM is not null and AREA_NUM is not null) t  ");
		sql.append(" left join base_sku_base_info bsku on t.SKU_ID= bsku.sku_id ) A "); 
		sql.append(" group by  building_num,storeroom_num,class_type,class_name, type_name ,cargo_type ");
		sql.append(" union all select A.building_num,A.storeroom_num,A.cargo_type,A.type_name,'' as class_type,(A.type_name||'-合计') as class_name  ,round(sum(A.dm),3) as dm ,round(sum(A.dj),3) as dj ,'2' as isxj  from ( "); 
		sql.append(" select bsku.cargo_type,bsku.type_name,t.SKU_ID,nvl(t.NOW_PIECE,0)*nvl(t.NET_SINGLE,0)/1000 as dj ,nvl(t.NOW_PIECE,0)*nvl(t.GROSS_SINGLE,0)/1000 as dm,");
		sql.append(" t.CARGO_LOCATION, t.building_num,t.floor_num,t.room_num,t.area_num ,t.storeroom_num from ("); 
		sql.append(" select SKU_ID,GROSS_SINGLE,NOW_PIECE,NET_SINGLE, CARGO_LOCATION,building_num,floor_num,room_num,area_num ,storeroom_num from bis_tray_info where "); 
		sql.append(strsql);
		sql.append(" FLOOR_NUM <> 7 AND FLOOR_NUM <> 9 AND (CARGO_STATE = '01' or CARGO_STATE = '10' )  and STOREROOM_NUM is not null and AREA_NUM is not null) t ");
		sql.append(" left join base_sku_base_info bsku on t.SKU_ID= bsku.sku_id ) A   ");
		sql.append(" group by  building_num,storeroom_num,cargo_type,type_name ");
		sql.append(" order by  building_num,storeroom_num,cargo_type,class_type asc ");
		
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	/**
	 * 获取现有个类产品的库存  by slh 20160625 
	 * @param BuildingNum 楼号   + 楼号 housid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayProductSum(String BuildingNum,String housid) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer(); //warehouse_id
		HashMap<String, Object> parme = new HashMap<String, Object>();
		String strsql="";
		if(!"".equals(BuildingNum) && null != BuildingNum){
			strsql=" BUILDING_NUM=:BUILDING_NUM  and ";
		    parme.put("BUILDING_NUM", BuildingNum);
		}
		
		if(!"".equals(housid) && null != housid){
			 strsql=strsql.trim().length()<=0?" warehouse_id=:warehouse_id and ":(strsql+" warehouse_id=:warehouse_id and ") ;
			 parme.put("warehouse_id", housid);
		}

		sql.append(" select b.printid,b.id ,to_char(decode(b.pname,'其他', '其他',b.pname)) as pname,nvl(a.dm,0) as dm,nvl(a.dj,0) as dj  ");        
		sql.append(" from (select id, pname,printid from base_product_calss where printid != 0) B  "); 
		sql.append(" left join ( "); 
		sql.append("   select bsku.cargo_type,bsku.type_name,bsku.class_type,bsku.class_name,round(sum(dm), 3) as dm,round(sum(dj), 3) as dj "); 
		sql.append("   from (select SKU_ID, nvl(NOW_PIECE, 0) * nvl(NET_SINGLE, 0) / 1000 as dj,nvl(NOW_PIECE, 0) * nvl(GROSS_SINGLE, 0) / 1000 as dm "); 
		sql.append("      from bis_tray_info where "); 
		sql.append(strsql);
		sql.append("   FLOOR_NUM <> 7 AND FLOOR_NUM <> 9 AND (CARGO_STATE = '01' or CARGO_STATE = '10' ) and STOREROOM_NUM is not null  and AREA_NUM is not null) t "); 
		sql.append("   left join base_sku_base_info bsku  on t.SKU_ID = bsku.sku_id  "); 
		sql.append("   group by bsku.cargo_type,bsku.type_name,bsku.class_type,bsku.class_name )A "); 
		sql.append(" on a.class_type = b.id  "); 
		sql.append(" union all  "); 
		sql.append(" select b.id as printid,b.printid as id,to_char((b.pname|| '-合计')) as pname,nvl(a.dm,0) as dm,nvl(a.dj,0) as dj     ");     
		sql.append(" from (select id, pname,printid from base_product_calss where printid = 0) B  "); 
		sql.append(" left join ( "); 
		sql.append("  select bsku.cargo_type,(nvl(bsku.type_name,'')|| '-合计') as type_name,round(sum(dm), 3) as dm,round(sum(dj), 3) as dj "); 
		sql.append(" from (select SKU_ID, nvl(NOW_PIECE, 0) * nvl(NET_SINGLE, 0) / 1000 as dj,nvl(NOW_PIECE, 0) * nvl(GROSS_SINGLE, 0) / 1000 as dm "); 
		sql.append("    from bis_tray_info where  "); 
		sql.append(strsql); 
		sql.append("  FLOOR_NUM <> 7 AND FLOOR_NUM <> 9 AND (CARGO_STATE = '01' or CARGO_STATE = '10' ) and STOREROOM_NUM is not null  and AREA_NUM is not null) t "); 
		sql.append("  left join base_sku_base_info bsku  on t.SKU_ID = bsku.sku_id  "); 
		sql.append("   group by bsku.cargo_type,bsku.type_name)A "); 
		sql.append(" on a.cargo_type = b.id  "); 
		sql.append(" order by printid ,id  asc  ");
		
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
}
