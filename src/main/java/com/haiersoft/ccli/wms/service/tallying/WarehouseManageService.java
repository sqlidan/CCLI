package com.haiersoft.ccli.wms.service.tallying;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.tallying.WarehouseManageDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库管入库
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class WarehouseManageService extends BaseService<TrayInfo, String> {
	
	@Autowired
	private WarehouseManageDao warehouseDao  ;

	@Override
	public HibernateDao<TrayInfo, String> getEntityDao() {
		return warehouseDao;
	}

	public List<Map<String, Object>> GetData(String ids,String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers) {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		List<Map<String, Object>> mapList = new ArrayList<>();
		mapList = warehouseDao.GetData(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
		if (mapList!=null && mapList.size() > 0){
			resultMap = createXZ(ids,mapList,layers);
		}
		return resultMap;
	}

	//生成货架位的算法
	public List<Map<String, Object>> createXZ(String ids,List<Map<String, Object>> mapList, String layers) {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		//区分该区位配置和没有配置货架位的数据
		List<Map<String, Object>> createXZList = new ArrayList<>();
		List<Map<String, Object>> alreadyXZList = new ArrayList<>();
		for (Map<String, Object> forMap:mapList) {
			if ("0_0".equals(forMap.get("XZ").toString())){
				createXZList.add(forMap);
			}else{
				alreadyXZList.add(forMap);
			}
		}
		int createNum = createXZList.size();
		int cg = 10;//没配置的货架位的托盘默认层高为3
		int maxX = 0;//计算预计列数
		int l = 0;//新增托盘列数
		int g = 0;//新增托盘列数
		if (layers != null && layers.trim().length() > 0){
			g = Integer.parseInt(layers);
		}
		if (createNum == 0 && (ids == null || ids.trim().length() == 0)){
			return mapList;
		}else{
			//获取已配置货架位的X坐标,因为已配置的X坐标不再配置
			List<Integer> integerList = new ArrayList<>();
			for (Map<String, Object> forMap:alreadyXZList) {
				if (!integerList.contains(Integer.parseInt(forMap.get("ACTUAL_STOREROOM_X").toString()))){
					integerList.add(Integer.parseInt(forMap.get("ACTUAL_STOREROOM_X").toString()));
				}
			}

			if (createNum%cg == 0){
				maxX = createNum/cg;
			}else{
				maxX = createNum/cg+1;
			}
			//判断有几个已配置X坐标值的大于预计列数
			int count = 0;
			for (Integer forInteger:integerList) {
				if (forInteger<maxX){
					count++;
				}
			}
			maxX = maxX+count;
			//校验预计列数是否是最大的X坐标
			for (Integer forInteger:integerList) {
				if (forInteger>maxX){
					maxX = forInteger;
				}
			}
			//如果选中了托盘就再增加几列
			String[] strings = null;
			if (ids != null && ids.trim().length() > 0){
				strings = ids.split(",");
				if (strings!=null && strings.length > 0){
					if (strings.length%g == 0){
						l = strings.length/g;
					}else{
						l = strings.length/g+1;
					}
				}
			}
			for (int x = 1; x <= maxX; x++) {
				//如果i坐标值在已配置坐标列中，则跳过
				if (integerList.contains(x)){
					continue;
				}else{
					//动态生成货架位
					for (int z = 1; z <= cg; z++) {
						for (Map<String, Object> forMap:createXZList) {
							if ("0_0".equals(forMap.get("XZ").toString())){
								forMap.put("XZ",x+"_"+z);
								resultMap.add(forMap);
								break;
							}
						}
					}
				}
			}
			for (int x = maxX+1; x <= maxX+l; x++) {
				for (int z = 1; z <= g; z++) {
					for (int i = 0; i < strings.length; i++) {
						Map<String, Object> map = new HashMap<>();
						map =  warehouseDao.GetOneData(strings[i]);
						map.put("XZ",x+"_"+z);
						resultMap.add(map);
						break;
					}
				}
			}
		}
		maxX = maxX + l;
		resultMap.addAll(alreadyXZList);
		for (Map<String, Object> forMap:resultMap) {
			forMap.put("MAXX",maxX);
			int maxZ =Integer.parseInt(forMap.get("MAXZ")==null?"0":forMap.get("MAXZ").toString());
			if (cg>=maxZ ){
				if (g>=cg){
					forMap.put("MAXZ",g);
				}else{
					forMap.put("MAXZ",cg);
				}
			}else{
				if (g>=maxZ){
					forMap.put("MAXZ",g);
				}else{
					forMap.put("MAXZ",maxZ);
				}
			}
		}
		return resultMap;
	}


	public List<Map<String, Object>> GetFram(String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers) {
		return warehouseDao.GetFram(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
	}

	public List<Map<String, Object>> GetFramInfo(String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers) {
		return warehouseDao.GetFramInfo(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
	}

}
