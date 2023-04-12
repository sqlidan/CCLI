package com.haiersoft.ccli.base.dao;    

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;

@Repository
public class BaseTrayDao extends HibernateDao<BaseClientInfo, Integer> {

	/**
	 * 获取空库位框架 by slh 20160623 
	 * @param BuildingNum  楼号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayFByBuildingNum(String BuildingNum) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" select A.BUILDING_NUM ,A.Floor_Num,A.STOREROOM_NUM,B.MaxAre,B.MinAre,nvl(C.AccNum,0)  as AccNum ,B.Iwith ,E.Ihegiht from (");
		sql.append(" SELECT BUILDING_NUM, Floor_Num,ROOM_NUM,STOREROOM_NUM FROM base_tray where BUILDING_NUM=:BUILDING_NUM and GROUP_ID  is not null and AREA_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by BUILDING_NUM ,Floor_Num ,STOREROOM_NUM ) A left join (");
		sql.append(" SELECT BUILDING_NUM,COUNT(DISTINCT STOREROOM_NUM) as Ihegiht  FROM base_tray where BUILDING_NUM=:BUILDING_NUM and GROUP_ID is not null and AREA_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by BUILDING_NUM )E  on E.BUILDING_NUM =A.BUILDING_NUM left join (");
		sql.append(" select max(AREA_NUM) as MaxAre,min(AREA_NUM) as MinAre,BUILDING_NUM,(TO_NUMBER(max(AREA_NUM))-TO_NUMBER(min(AREA_NUM))+1) as Iwith from base_tray where BUILDING_NUM=:BUILDING_NUM and group_id is not null and AREA_NUM is not null");
		sql.append(" group by BUILDING_NUM ) B on A.BUILDING_NUM= B.BUILDING_NUM left join (");
		sql.append(" select Count(*) as AccNum,BUILDING_NUM , FLOOR_NUM,ROOM_NUM from bis_Tray_info  where CARGO_STATE='01' and BUILDING_NUM=:BUILDING_NUM and FLOOR_NUM is not null and ROOM_NUM is not null and AREA_NUM is not null  ");
		sql.append(" and BUILDING_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by  BUILDING_NUM,FLOOR_NUM,ROOM_NUM )C on A.BUILDING_NUM= C.BUILDING_NUM and A.FLOOR_NUM =C.FLOOR_NUM and C.ROOM_NUM =A.ROOM_NUM   ");
		sql.append(" order by  A.BUILDING_NUM,A.STOREROOM_NUM ");

		HashMap<String, Object> parme = new HashMap<String, Object>();
		parme.put("BUILDING_NUM", BuildingNum);

		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
	/*if(!"".equals(BuildingNum) && null != BuildingNum){
		sql.append(" and  t.BUILDING_NUM=:BUILDING_NUM ");
	    parme.put("BUILDING_NUM", BuildingNum);
	}
	*/
	

	/**
	 * 获取空区位框架 by slh 20160623 
	 * @param BuildingNum  楼号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayF(String BuildingNum,String housid) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer();
		HashMap<String, Object> parme = new HashMap<String, Object>();
        String strsql="";
        if(!"".equals(BuildingNum) && null != BuildingNum){
        	strsql=" BUILDING_NUM=:BUILDING_NUM  and";
    	    parme.put("BUILDING_NUM", BuildingNum);
        }
        if(!"".equals(housid) && null != housid){
        	strsql= strsql.trim().length()>0?(strsql+"   warehouse_id=:WAREHOUSE_ID  and ") : " warehouse_id=:WAREHOUSE_ID and ";
    	    parme.put("WAREHOUSE_ID", housid);
        }
		sql.append(" select A.BUILDING_NUM ,A.Floor_Num,A.STOREROOM_NUM,B.MaxAre,B.MinAre,nvl(C.AccNum,0) as AccNum,B.Iwith ,E.Ihegiht from (");
		sql.append(" SELECT BUILDING_NUM ,FLOOR_NUM,ROOM_NUM,STOREROOM_NUM FROM base_tray where ");
		
		sql.append(strsql);
		
		sql.append("  GROUP_ID  is not null and AREA_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by BUILDING_NUM ,Floor_Num ,ROOM_NUM,STOREROOM_NUM ) A left join (");
		sql.append(" SELECT BUILDING_NUM,COUNT(DISTINCT STOREROOM_NUM) as Ihegiht  FROM base_tray where ");
		
		sql.append(strsql);
		
		sql.append("  GROUP_ID is not null and AREA_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by BUILDING_NUM )E  on E.BUILDING_NUM =A.BUILDING_NUM left join (");
		sql.append(" select max(AREA_NUM) as MaxAre,min(AREA_NUM) as MinAre,BUILDING_NUM,(TO_NUMBER(max(AREA_NUM))-TO_NUMBER(min(AREA_NUM))+1) as Iwith from base_tray where ");
		
		sql.append(strsql);
		
		sql.append("  group_id is not null and AREA_NUM is not null");
		sql.append(" group by BUILDING_NUM ) B on A.BUILDING_NUM= B.BUILDING_NUM left join (");
		sql.append(" select Count(distinct AREA_NUM) as AccNum,BUILDING_NUM , FLOOR_NUM,ROOM_NUM from bis_Tray_info  where ");
		
		sql.append(strsql);
		
		sql.append(" CARGO_STATE='01' and FLOOR_NUM is not null and ROOM_NUM is not null and AREA_NUM is not null  ");
		sql.append(" and BUILDING_NUM is not null and STOREROOM_NUM is not null ");
		sql.append(" group by  BUILDING_NUM ,FLOOR_NUM,ROOM_NUM )C on A.BUILDING_NUM= C.BUILDING_NUM and A.FLOOR_NUM =C.FLOOR_NUM and C.ROOM_NUM =A.ROOM_NUM    ");
		sql.append(" order by  A.BUILDING_NUM,A.STOREROOM_NUM "); 

		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
	
	/**
	 * 获取空库位框架 by slh 20160625
	 * @param BuildingNum  楼号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> FindTrayStoreF(String BuildingNum,String housid) {
		List<Map<String, Object>> getList = null;
		StringBuffer sql = new StringBuffer();
		HashMap<String, Object> parme = new HashMap<String, Object>();
        String strsql="";
        if(!"".equals(BuildingNum) && null != BuildingNum){
        	strsql=" BUILDING_NUM=:BUILDING_NUM and ";
        	parme.put("BUILDING_NUM",BuildingNum);
        }
        if(!"".equals(housid) && null != housid){
        	strsql= strsql.trim().length()>0?(strsql+"  warehouse_id=:WAREHOUSE_ID and ") : " warehouse_id=:WAREHOUSE_ID  and ";
        	parme.put("WAREHOUSE_ID",housid);
        }
        
        sql.append(" select a.BUILDING_NUM, a.Floor_Num,a.room_num ,a.STOREROOM_NUM,b.fwidth,c.mfwidth   from ("); //,f.classCou as classCou
        sql.append(" SELECT BUILDING_NUM, Floor_Num,room_num ,STOREROOM_NUM FROM base_tray where ");
        
        sql.append(strsql);
        
        sql.append("  GROUP_ID  is not null and AREA_NUM is not null and STOREROOM_NUM is not null ");         
        sql.append(" group by BUILDING_NUM ,Floor_Num ,room_num,STOREROOM_NUM) A left join  (");
        sql.append(" SELECT BUILDING_NUM,Floor_Num,count(DISTINCT room_num) as fwidth  FROM base_tray where");
        
        sql.append(strsql);
        
        sql.append("  GROUP_ID is not null and AREA_NUM is not null and STOREROOM_NUM is not null  group by BUILDING_NUM ,Floor_Num)B on  A.BUILDING_NUM=b.BUILDING_NUM and a.Floor_Num=b.Floor_Num");
        sql.append(" left join ( select BUILDING_NUM,max(fwidth) as mfwidth from (");
        sql.append(" SELECT BUILDING_NUM,Floor_Num,count(DISTINCT room_num) as fwidth  FROM base_tray where  ");
        
        sql.append(strsql);
        
        sql.append("  GROUP_ID is not null and AREA_NUM is not null and STOREROOM_NUM is not null  group by BUILDING_NUM ,Floor_Num)");
        sql.append(" group by BUILDING_NUM  ) C on c.BUILDING_NUM=a.BUILDING_NUM  "); 
        
        sql.append("  order by  a.BUILDING_NUM, a.Floor_Num,a.room_num ,a.STOREROOM_NUM ");

		SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
		getList = sqlQuery.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getList;
	}
	
} 
