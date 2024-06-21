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
 * 拆托
 * @author 
 *
 */
@Repository
public class SplitTrayManageDao extends HibernateDao<TrayInfo, Integer>{
    /**
     * 取空货架框架
     * @param buildingNum 楼号
     * @param floorNum 楼层号
     * @param roomNum 房间号
     * @param areaNum 区号
     * @param actualStoreroomX X坐标
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMaxXZ(String buildingNum, String floorNum, String roomNum, String areaNum, String actualStoreroomX) {
        List<Map<String, Object>> getList = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" select DISTINCT  ");
//        sql.append(" BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_X,0)) as maxX, ");
//        sql.append(" max(nvl(ACTUAL_STOREROOM_Y,0)) as maxY, ");
        sql.append(" max(nvl(ACTUAL_STOREROOM_Z,0)) as maxZ ");
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
        if(!"".equals(actualStoreroomX) && null != actualStoreroomX){
            sql.append(" and ACTUAL_STOREROOM_X=:ACTUAL_STOREROOM_X ");
        }
//        sql.append(" group by BUILDING_NUM,FLOOR_NUM,ROOM_NUM,AREA_NUM,STOREROOM_NUM ");

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
        if(!"".equals(actualStoreroomX) && null != actualStoreroomX){
            parme.put("ACTUAL_STOREROOM_X", actualStoreroomX);
        }

        SQLQuery sqlQuery = createSQLQuery(sql.toString(), parme);
        getList = sqlQuery.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getList;
    }
}
