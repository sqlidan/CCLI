package com.haiersoft.ccli.wms.dao.tallying;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库管入库
 * @author 
 *
 */
@Repository
public class WarehouseManageDao extends HibernateDao<TrayInfo, Integer>{
    /**
     * 取空货架框架
     * @param buildingNum 楼号
     * @param floorNum 楼层号
     * @param roomNum 房间号
     * @param areaNum 区号
     * @param storeRoomNum 库房号
     * @param layers 货架层数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> GetData(String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers, String actualStoreroomX) {
        List<Map<String, Object>> getList = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" select DISTINCT a.ID,a.BUILDING_NUM,a.FLOOR_NUM,a.ROOM_NUM,a.AREA_NUM,a.STOREROOM_NUM, ");
        sql.append(" a.TRAY_ID,a.BILL_NUM,a.ENTER_STOCK_TIME, ");
        sql.append(" nvl(a.ACTUAL_STOREROOM_X,0) as ACTUAL_STOREROOM_X,nvl(a.ACTUAL_STOREROOM_Y,0) as ACTUAL_STOREROOM_Y,nvl(a.ACTUAL_STOREROOM_Z,0) as ACTUAL_STOREROOM_Z,nvl( ACTUAL_STOREROOM_X, 0 ) || '_' || nvl( ACTUAL_STOREROOM_Z, 0 ) AS XZ, ");
        sql.append(" b.maxX,b.maxY,b.maxZ,b.num ");
        sql.append(" from bis_Tray_info a ");
        sql.append(" left join ( ");
        sql.append("  select DISTINCT BILL_NUM,BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_X,0)) as maxX, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_Y,0)) as maxY, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_Z,0)) as maxZ, ");
        sql.append(" count(0) as num ");
        sql.append(" from bis_Tray_info ");
        sql.append(" where CARGO_STATE='01' ");
        if(!"".equals(buildingNum) && null != buildingNum){
            sql.append(" and BUILDING_NUM=:BUILDING_NUM ");
        }
        if(!"".equals(floorNum) && null != floorNum){
            sql.append(" and FLOOR_NUM=:FLOOR_NUM ");
        }
        if(!"".equals(roomNum) && null != roomNum){
            sql.append(" and ROOM_NUM=:ROOM_NUM ");
        }
        if(!"".equals(areaNum) && null != areaNum){
            sql.append(" and AREA_NUM=:AREA_NUM ");
        }
        if(!"".equals(storeRoomNum) && null != storeRoomNum){
            sql.append(" and STOREROOM_NUM=:STOREROOM_NUM ");
        }
        if(!"".equals(actualStoreroomX) && null != actualStoreroomX){
            sql.append(" and ACTUAL_STOREROOM_X=:ACTUAL_STOREROOM_X ");
        }
        sql.append(" group by BILL_NUM,BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM ");
        sql.append(" ) b on a.BILL_NUM = b.BILL_NUM and a.BUILDING_NUM = b.BUILDING_NUM and a.FLOOR_NUM = b.FLOOR_NUM and a.ROOM_NUM = b.ROOM_NUM and a.AREA_NUM = b.AREA_NUM and a.STOREROOM_NUM = b.STOREROOM_NUM ");
        sql.append(" where a.CARGO_STATE='01' ");
        if(!"".equals(buildingNum) && null != buildingNum){
            sql.append(" and a.BUILDING_NUM=:BUILDING_NUM ");
        }
        if(!"".equals(floorNum) && null != floorNum){
            sql.append(" and a.FLOOR_NUM=:FLOOR_NUM ");
        }
        if(!"".equals(roomNum) && null != roomNum){
            sql.append(" and a.ROOM_NUM=:ROOM_NUM ");
        }
        if(!"".equals(areaNum) && null != areaNum){
            sql.append(" and a.AREA_NUM=:AREA_NUM ");
        }
        if(!"".equals(storeRoomNum) && null != storeRoomNum){
            sql.append(" and a.STOREROOM_NUM=:STOREROOM_NUM ");
        }
        if(!"".equals(actualStoreroomX) && null != actualStoreroomX){
            sql.append(" and a.ACTUAL_STOREROOM_X=:ACTUAL_STOREROOM_X ");
        }
        sql.append(" order by a.ENTER_STOCK_TIME asc ");

        HashMap<String, Object> parme = new HashMap<String, Object>();
        if(!"".equals(buildingNum) && null != buildingNum){
            parme.put("BUILDING_NUM", buildingNum);
        }
        if(!"".equals(floorNum) && null != floorNum){
            parme.put("FLOOR_NUM", floorNum);
        }
        if(!"".equals(roomNum) && null != roomNum){
            parme.put("ROOM_NUM", roomNum);
        }
        if(!"".equals(areaNum) && null != areaNum){
            parme.put("AREA_NUM", areaNum);
        }
        if(!"".equals(storeRoomNum) && null != storeRoomNum){
            parme.put("STOREROOM_NUM", storeRoomNum);
        }
        if(!"".equals(actualStoreroomX) && null != actualStoreroomX){
            parme.put("ACTUAL_STOREROOM_X", actualStoreroomX);
        }

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        getList = sqlQuery.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 取空货架框架
     * @param id id
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> GetOneData(String id) {
        List<Map<String, Object>> getList = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" select DISTINCT a.ID,a.BUILDING_NUM,a.FLOOR_NUM,a.ROOM_NUM,a.AREA_NUM,a.STOREROOM_NUM, ");
        sql.append(" a.TRAY_ID,a.BILL_NUM,a.ENTER_STOCK_TIME, ");
        sql.append(" nvl(a.ACTUAL_STOREROOM_X,0) as ACTUAL_STOREROOM_X,nvl(a.ACTUAL_STOREROOM_Y,0) as ACTUAL_STOREROOM_Y,nvl(a.ACTUAL_STOREROOM_Z,0) as ACTUAL_STOREROOM_Z, ");
        sql.append(" nvl( ACTUAL_STOREROOM_X, 0 ) || '_' || nvl( ACTUAL_STOREROOM_Z, 0 ) AS XZ, ");
        sql.append(" 0 as maxX,0 as maxY,0 as maxZ ");
        sql.append(" from bis_Tray_info a ");
        sql.append(" where a.ID=:ID ");

        HashMap<String, Object> parme = new HashMap<String, Object>();
        parme.put("ID", id);

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        getList = sqlQuery.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList.get(0);
    }

    /**
     * 取空货架框架
     * @param buildingNum 楼号
     * @param floorNum 楼层号
     * @param roomNum 房间号
     * @param areaNum 区号
     * @param storeRoomNum 库房号
     * @param layers 货架层数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> GetFram(String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();

        sql.append(" select DISTINCT BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_X,0)) as maxX, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_Y,0)) as maxY, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_Z,0)) as maxZ ");
        sql.append(" from bis_Tray_info ");
        sql.append(" where CARGO_STATE = '01' ");
        if(!"".equals(buildingNum) && null != buildingNum){
            sql.append(" and BUILDING_NUM=:BUILDING_NUM ");
            parme.put("BUILDING_NUM", buildingNum);
        }
        if(!"".equals(floorNum) && null != floorNum){
            sql.append(" and FLOOR_NUM=:FLOOR_NUM ");
            parme.put("FLOOR_NUM", floorNum);
        }
        if(!"".equals(roomNum) && null != roomNum){
            sql.append(" and ROOM_NUM=:ROOM_NUM ");
            parme.put("ROOM_NUM", roomNum);
        }
        if(!"".equals(areaNum) && null != areaNum){
            sql.append(" and AREA_NUM=:AREA_NUM ");
            parme.put("AREA_NUM", areaNum);
        }
//        if(!"".equals(storeRoomNum) && null != storeRoomNum){
//            sql.append(" and STOREROOM_NUM=:STOREROOM_NUM ");
//            parme.put("STOREROOM_NUM", storeRoomNum);
//        }
        sql.append(" group by BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM ");

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        getList = sqlQuery.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }

    /**
     * 取空货架框架
     * @param buildingNum 楼号
     * @param floorNum 楼层号
     * @param roomNum 房间号
     * @param areaNum 区号
     * @param storeRoomNum 库房号
     * @param layers 货架层数
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> GetFramInfo(String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers) {
        List<Map<String, Object>> getList = null;
        HashMap<String, Object> parme = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();

        sql.append(" select DISTINCT BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM ");
        sql.append(" , nvl(ACTUAL_STOREROOM_X,0) as ACTUAL_STOREROOM_X,nvl(ACTUAL_STOREROOM_Y,0) as ACTUAL_STOREROOM_Y,nvl(ACTUAL_STOREROOM_Z,0) as ACTUAL_STOREROOM_Z ");
        sql.append(" , nvl(ACTUAL_STOREROOM_X,0)||'_'||nvl(ACTUAL_STOREROOM_Z,0) as XZ ");
        sql.append(" from bis_Tray_info ");
        sql.append(" where CARGO_STATE='01' ");
        if(!"".equals(buildingNum) && null != buildingNum){
            sql.append(" and BUILDING_NUM=:BUILDING_NUM ");
            parme.put("BUILDING_NUM", buildingNum);
        }
        if(!"".equals(floorNum) && null != floorNum){
            sql.append(" and FLOOR_NUM=:FLOOR_NUM ");
            parme.put("FLOOR_NUM", floorNum);
        }
        if(!"".equals(roomNum) && null != roomNum){
            sql.append(" and ROOM_NUM=:ROOM_NUM ");
            parme.put("ROOM_NUM", roomNum);
        }
        if(!"".equals(areaNum) && null != areaNum){
            sql.append(" and AREA_NUM=:AREA_NUM ");
            parme.put("AREA_NUM", areaNum);
        }

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        getList = sqlQuery.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }



}
