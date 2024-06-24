package com.haiersoft.ccli.wms.service.tallying;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.dao.tallying.RelocationManageDao;
import com.haiersoft.ccli.wms.dao.tallying.WarehouseManageDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 移库
 * @author 
 */
@Service
@Transactional(readOnly = true)
public class RelocationManageService extends BaseService<TrayInfo, Integer> {

	@Autowired
	private WarehouseManageService warehouseService;
	@Autowired
	private RelocationManageDao relocationDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private WarehouseManageDao warehouseDao;

	@Override
	public HibernateDao<TrayInfo, Integer> getEntityDao() {
		return relocationDao;
	}

	public List<Map<String, Object>> GetData(String ids,String buildingNum, String floorNum, String roomNum, String areaNum, String storeRoomNum, String layers, String actualStoreroomX) {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		List<Map<String, Object>> mapList = new ArrayList<>();
		mapList = warehouseDao.GetData(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers,actualStoreroomX);
		if (mapList!=null && mapList.size() > 0){
			resultMap = createXZ(ids,mapList,layers);
		}
		return resultMap;
	}

	//生成货架位的算法二
	public List<Map<String, Object>> createXZ(String ids,List<Map<String, Object>> mapList, String layers) {
		/*
		 * 徐峥 2024-06-24
		 * 当前规则
		 * ✔ 1.当前有值的X坐标列判断同提单可以填补，如果超过默认层高则增加X坐标
		 * ✔ 2.当前默认未设置货架位的数据层高为4
		 * ✔ 3.需要按照同一提单号和默认层高值增加X坐标列值
		 * ✔ 4.第一次排列X坐标和Z坐标值时，不保存X和Z坐标字段值
		 * ✔ 5.默认显示蓝色。预览显示绿色
		 * ✔ 6.已有提单托盘继续入库，在对应的X坐标上继续向上叠放（放在一起）
		 */
		List<Map<String, Object>> resultMapList = new ArrayList<>();
		//区分该区位配置和没有配置货架位的数据
		List<Map<String, Object>> billNumMapList = new ArrayList<>();
		for (Map<String, Object> forMap:mapList) {
			int count = 0;
			for (Map<String, Object> billNumMap:billNumMapList) {
				if (billNumMap.get("BILL_NUM").toString().equals(forMap.get("BILL_NUM").toString())){//相同提单号存在
					count++;
				}
			}
			if (count == 0){//相同提单号不存在
				Map<String, Object> billNumMap = new HashMap<>();
				billNumMap.put("BILL_NUM",forMap.get("BILL_NUM").toString());
				billNumMap.put("NUM",forMap.get("NUM").toString());
				//获取同一提单号里的托盘数据
				List<Map<String, Object>> sameBillNumMapList = new ArrayList<>();
				for (Map<String, Object> forMap2:mapList) {
					if (billNumMap.get("BILL_NUM").toString().equals(forMap2.get("BILL_NUM").toString())){
						sameBillNumMapList.add(forMap2);
					}
				}
				billNumMap.put("sameBillNumMapList",sameBillNumMapList);
				//加入到billNumMapLsit
				billNumMapList.add(billNumMap);
			}
		}

		int cg = 4;//没配置的货架位的托盘默认层高为4
		int maxX = 1;//预计最大列数
		int maxZ = cg;//预计最大层数
		//循环提单托盘数据
		for (Map<String, Object> billNumMap:billNumMapList) {
//			if (!"YMLUN755174288".equals(billNumMap.get("BILL_NUM").toString())
//					&& ! "YMLUW192258600".equals(billNumMap.get("BILL_NUM").toString())
//					&& ! "TIG1802CH003".equals(billNumMap.get("BILL_NUM").toString())
//					&& ! "MEDUGR007428".equals(billNumMap.get("BILL_NUM").toString())
//			){
//				continue;
//			}
			int NUM = Integer.parseInt(billNumMap.get("NUM").toString());//提单下的托盘数量
			//判断是否存在已指定的货架位
			List<Map<String, Object>> sameBillNumMapList = (List<Map<String, Object>>) billNumMap.get("sameBillNumMapList");
			int count = 0;
			int MAXX = 0;//有货架位的托盘的X坐标标记
			int MAXZ = cg;//有货架位的托盘的层高标记
			for (Map<String, Object> sameBillNumMap:sameBillNumMapList) {
				if (!"0_0".equals(sameBillNumMap.get("XZ").toString())){
					int MAXXTemp = Integer.parseInt(sameBillNumMap.get("ACTUAL_STOREROOM_X").toString());
					//同一提单号里可能存在多个指定货架位的托盘,获取最小的MAXX
					if (MAXX == 0){
						MAXX = MAXXTemp;
					}else{
						if (MAXX > MAXXTemp){
							MAXX = MAXXTemp;
						}
					}
					MAXZ = Integer.parseInt(sameBillNumMap.get("MAXZ").toString());
					//筛选默认层高和指定层高的较大值
					if (MAXZ > maxZ){
						maxZ = MAXZ;
					}
					count++;
				}
			}
			int num = 0;
			if (count > 0){//存在，把提单下的托盘数按指定层高计算列数
				//如果之前提单动态生成的列数已大于有货架位的托盘的X坐标值
				if (maxX >= MAXX){
					//数据异常
					return new ArrayList<>();
				}else{
					//将有货架位的托盘的X坐标值左侧的X坐标值赋值为当前的最大X坐标值
					maxX = MAXX;
				}
				if (NUM%MAXZ == 0){
					num = NUM/MAXZ;
				}else{
					num = NUM/MAXZ+1;
				}

				List<String> stringList = new ArrayList<>();
				for (int x = maxX; x <= maxX+num; x++) {
					//动态生成货架位
					for (int z = 1; z <= MAXZ; z++) {
						for (Map<String, Object> sameBillNumMap:sameBillNumMapList) {
							if ("0_0".equals(sameBillNumMap.get("XZ").toString())) {
								if (stringList.contains(x + "_" + z)){
									break;
								}
								sameBillNumMap.put("XZ", x + "_" + z);
								sameBillNumMap.put("COL", "1");
								resultMapList.add(sameBillNumMap);
								//将动态生成的X坐标和Z坐标值保存到表中
								TrayInfo updateTrayInfo = new TrayInfo();
								updateTrayInfo.setId(Integer.parseInt(sameBillNumMap.get("ID").toString()));
								updateTrayInfo.setActualStoreroomX(x + "");
								updateTrayInfo.setActualStoreroomZ(z + "");
								updateTrayInfo.setUpdateTime(new Date());
//								warehouseDao.merge(updateTrayInfo);
								break;
							}else{
								if (!stringList.contains(sameBillNumMap.get("XZ").toString())){
									sameBillNumMap.put("COL", "1");
									if (!resultMapList.contains(sameBillNumMap)){
										resultMapList.add(sameBillNumMap);
									}
									stringList.add(sameBillNumMap.get("XZ").toString());
								}
							}
						}
					}
				}
			}else{//不存在，把提单下的托盘数按默认层高计算列数
				if (NUM%cg == 0){
					num = NUM/cg;
				}else{
					num = NUM/cg+1;
				}
				List<String> stringList = new ArrayList<>();
				for (int x = maxX; x <= maxX+num; x++) {
					//动态生成货架位
					for (int z = 1; z <= cg; z++) {
						for (Map<String, Object> sameBillNumMap:sameBillNumMapList) {
							if ("0_0".equals(sameBillNumMap.get("XZ").toString())) {
								if (stringList.contains(x + "_" + z)){
									break;
								}
								sameBillNumMap.put("XZ", x + "_" + z);
								sameBillNumMap.put("COL", "1");
								resultMapList.add(sameBillNumMap);
								//将动态生成的X坐标和Z坐标值保存到表中
								TrayInfo updateTrayInfo = new TrayInfo();
								updateTrayInfo.setId(Integer.parseInt(sameBillNumMap.get("ID").toString()));
								updateTrayInfo.setActualStoreroomX(x + "");
								updateTrayInfo.setActualStoreroomZ(z + "");
								updateTrayInfo.setUpdateTime(new Date());
//								warehouseDao.merge(updateTrayInfo);
								break;
							}else{
								if (!stringList.contains(sameBillNumMap.get("XZ").toString())){
									sameBillNumMap.put("COL", "1");
									if (!resultMapList.contains(sameBillNumMap)){
										resultMapList.add(sameBillNumMap);
									}
									stringList.add(sameBillNumMap.get("XZ").toString());
								}
							}
						}
					}
				}
			}
			maxX += num;
		}

		//依据id获取预计确认的数据
		String[] strings = null;
		if (ids != null && ids.trim().length() > 0) {
			List<Map<String, Object>> inMapList = new ArrayList<>();
			strings = ids.split(",");
			if (strings != null && strings.length > 0) {
				for (int i = 0; i < strings.length; i++) {
					Map<String, Object> map = new HashMap<>();
					map =  warehouseDao.GetOneData(strings[i]);
					inMapList.add(map);
				}
			}
			//预计数据按提单号分组
			List<Map<String, Object>> inBillNumMapList = new ArrayList<>();
			for (Map<String, Object> forMap:inMapList) {
				int count = 0;
				for (Map<String, Object> billNumMap:inBillNumMapList) {
					if (billNumMap.get("BILL_NUM").toString().equals(forMap.get("BILL_NUM").toString())){//相同提单号存在
						count++;
					}
				}
				if (count == 0) {//相同提单号不存在
					Map<String, Object> inBillNumMap = new HashMap<>();
					inBillNumMap.put("BILL_NUM", forMap.get("BILL_NUM").toString());
					//获取同一提单号里的托盘数据
					List<Map<String, Object>> sameBillNumMapList = new ArrayList<>();
					for (Map<String, Object> forMap2 : inMapList) {
						if (inBillNumMap.get("BILL_NUM").toString().equals(forMap2.get("BILL_NUM").toString())) {
							sameBillNumMapList.add(forMap2);
						}
					}
					inBillNumMap.put("sameBillNumMapList", sameBillNumMapList);
					//加入到billNumMapLsit
					inBillNumMapList.add(inBillNumMap);
				}
			}
			//校验预计确认的托盘号对应的提单是否已存在
			for (Map<String, Object> inBillNumMap:inBillNumMapList) {
				//获取相同提单号的所有货架位
				List<String> allXZList = new ArrayList<>();
				for (Map<String, Object> resultMap:resultMapList) {
					if (inBillNumMap.get("BILL_NUM").toString().equals(resultMap.get("BILL_NUM").toString())){//相同提单号存在
						if (!allXZList.contains(resultMap.get("XZ").toString())){
							allXZList.add(resultMap.get("XZ").toString());
						}
					}
				}
				if (allXZList.size() > 0){//相同提单号存在
					//获取每列最高的货架位
					List<String> xList = new ArrayList<>();
					for (String string:allXZList) {
						String[] strings1 = string.split("_");
						if (!xList.contains(strings1[0])){
							xList.add(strings1[0]);
						}
					}
					int[] xAry = new int[xList.size()];
					int[] zAry = new int[xList.size()];
					for (int i = 0; i < xList.size(); i++) {
						xAry[i] = Integer.parseInt(xList.get(i));
						int maxz = 0;
						for (String string:allXZList) {
							String[] strings1 = string.split("_");
							if (xAry[i] == Integer.parseInt(strings1[0])){
								if (maxz < Integer.parseInt(strings1[1])){
									maxz = Integer.parseInt(strings1[1]);
								}
							}
						}
						zAry[i] = maxz;
					}
					//获取同提单下的托盘数据
					List<Map<String, Object>> sameBillNumMapList = (List<Map<String, Object>>) inBillNumMap.get("sameBillNumMapList");
					for (Map<String, Object> forMap:sameBillNumMapList) {
						int minz = Arrays.stream(zAry).min().getAsInt();
						int iValue = 0;
						for (int i = 0; i < zAry.length; i++) {
							if (zAry[i] == minz){
								iValue = i;
								break;
							}
						}
						forMap.put("XZ",xAry[iValue]+"_"+(zAry[iValue]+1));
						forMap.put("COL","2");
						resultMapList.add(forMap);
						zAry[iValue] = zAry[iValue] +1;
					}
					//获取层数最大值
					int maxz = Arrays.stream(zAry).max().getAsInt();
					int[] getMaxZ = new int[3];
					getMaxZ[0] = cg;
					getMaxZ[1] = maxz;
					getMaxZ[2] = maxZ;
					maxZ = Arrays.stream(getMaxZ).max().getAsInt();
				}else{//不存在
					int l = 0;//新增托盘列数
					int g = 0;//新增托盘层数
					if (layers != null && layers.trim().length() > 0){
						g = Integer.parseInt(layers);
						if(g>maxZ){
							maxZ = g;
						}
					}
					List<Map<String, Object>> sameBillNumMapList = (List<Map<String, Object>>) inBillNumMap.get("sameBillNumMapList");
					int NUM = sameBillNumMapList.size();
					if (NUM%g == 0){
						l = NUM/g;
					}else{
						l = NUM/g+1;
					}
					for (int x = maxX; x < maxX+l; x++) {
						for (int z = 1; z <= g; z++) {
							Map<String, Object> map = new HashMap<>();
							int index = ((x-maxX)*g+z);
							if (index <= sameBillNumMapList.size()){
								map =  sameBillNumMapList.get(index-1);
								map.put("XZ",x+"_"+z);
								map.put("COL","2");
								resultMapList.add(map);
							}
						}
					}
					//累加X坐标值
					maxX = maxX + l;
				}
			}
		}

		//赋值最大的X坐标和层高
		for (Map<String, Object> forMap:resultMapList) {
			forMap.put("MAXX",maxX-1);
			forMap.put("MAXZ",maxZ);
		}
		return resultMapList;
	}






////===================原移库逻辑 Abandon=====================================================================================================
//	/**
//	 *
//	 * @author Connor.M
//	 * @Description: 移库确认 操作
//	 * @date 2016年3月16日 上午10:28:06
//	 * @param trayCode 托盘号
//	 * @param cargoLocation 库位号
//	 * @param userName 用户名
//	 * @return
//	 * @throws
//	 */
//	@Transactional(readOnly = false)
//	public String moveStorageConfirm(String trayCode, String cargoLocation, String userName){
//		Result<TrayInfo> result = new Result<TrayInfo>();
//
//		//查询库存 托盘数据
//		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//		filters.add(new PropertyFilter("EQS_trayId", trayCode));
//		filters.add(new PropertyFilter("EQS_cargoState", "01"));
//		List<TrayInfo> trayInfos = trayInfoDao.find(filters);
//
//		//判断是否存在
//		if(null != trayInfos && trayInfos.size() == 1){
//			TrayInfo orgTrayInfo = trayInfos.get(0);
//			TrayInfo trayInfo = trayInfos.get(0);
//			//移库时更新货品存放信息 gzq 20160630 添加
//			if(StringUtils.isNull(cargoLocation)){
//				cargoLocation = "00-00-000";
//			}
//			String[] num = cargoLocation.split("-");//库位号截取 注：库位号为三段式如：A1-01-18
//			//判断是否为三段式   若非三段式补足两位
//			if(num.length < 3){
//				num = ArrayUtils.addAll(num, "00", "00");
//			}
//			String buildingNum = StringUtils.lpadStringLeft(2, num[0]);
//			//修改库存
//			trayInfo.setBuildingNum(buildingNum.substring(0, 1));//楼
//			trayInfo.setFloorNum(buildingNum.substring(1, 2));//层
//			trayInfo.setRoomNum(num[1]);
//			trayInfo.setAreaNum(StringUtils.lpadStringLeft(3, num[2]));//三位补足
//			trayInfo.setStoreroomNum(num[0] + num[1]);//库房号
//			//End 移库后更新库存信息 gzq 20160630 添加
//			trayInfo.setCargoLocation(cargoLocation);
//			trayInfo.setUpdateTime(new Date());
//			List<Map<String,Object>> mapList = warehouseService.GetData(null,orgTrayInfo.getBuildingNum(),orgTrayInfo.getFloorNum(),orgTrayInfo.getRoomNum()
//					,orgTrayInfo.getAreaNum(),null,null,null);
//			if (mapList!=null && mapList.size() > 0) {
//				Map<String, Object> maxXZ = mapList.get(0);
//				if (maxXZ == null || maxXZ.get("MAXX") == null){
//					return "未获取到原托盘X轴坐标数";
//				}
//				if (maxXZ == null || maxXZ.get("MAXZ") == null){
//					return "未获取到原托盘层高数";
//				}
//				trayInfo.setActualStoreroomX((Integer.parseInt(maxXZ.get("MAXX").toString())+1)+"");//X坐标
//			}else{
//				trayInfo.setActualStoreroomX("1");//X坐标
//			}
//			trayInfo.setActualStoreroomZ("1");//Z坐标
//			trayInfoDao.save(trayInfo);
//			result.setCode(0);
//			result.setMsg("移库成功！");
//		}else{
//			result.setCode(1);
//			result.setMsg("数据错误，该托盘数据状态有误！");
//		}
//		return JSON.toJSONString(result);
//	}
}
