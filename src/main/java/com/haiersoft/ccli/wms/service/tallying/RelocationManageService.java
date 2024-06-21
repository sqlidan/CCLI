package com.haiersoft.ccli.wms.service.tallying;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.dao.tallying.RelocationManageDao;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Override
	public HibernateDao<TrayInfo, Integer> getEntityDao() {
		return relocationDao;
	}

	/**
	 *
	 * @author Connor.M
	 * @Description: 移库确认 操作
	 * @date 2016年3月16日 上午10:28:06
	 * @param trayCode 托盘号
	 * @param cargoLocation 库位号
	 * @param userName 用户名
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String moveStorageConfirm(String trayCode, String cargoLocation, String userName){
		Result<TrayInfo> result = new Result<TrayInfo>();

		//查询库存 托盘数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_trayId", trayCode));
		filters.add(new PropertyFilter("EQS_cargoState", "01"));
		List<TrayInfo> trayInfos = trayInfoDao.find(filters);

		//判断是否存在
		if(null != trayInfos && trayInfos.size() == 1){
			TrayInfo orgTrayInfo = trayInfos.get(0);
			TrayInfo trayInfo = trayInfos.get(0);
			//移库时更新货品存放信息 gzq 20160630 添加
			if(StringUtils.isNull(cargoLocation)){
				cargoLocation = "00-00-000";
			}
			String[] num = cargoLocation.split("-");//库位号截取 注：库位号为三段式如：A1-01-18
			//判断是否为三段式   若非三段式补足两位
			if(num.length < 3){
				num = ArrayUtils.addAll(num, "00", "00");
			}
			String buildingNum = StringUtils.lpadStringLeft(2, num[0]);
			//修改库存
			trayInfo.setBuildingNum(buildingNum.substring(0, 1));//楼
			trayInfo.setFloorNum(buildingNum.substring(1, 2));//层
			trayInfo.setRoomNum(num[1]);
			trayInfo.setAreaNum(StringUtils.lpadStringLeft(3, num[2]));//三位补足
			trayInfo.setStoreroomNum(num[0] + num[1]);//库房号
			//End 移库后更新库存信息 gzq 20160630 添加
			trayInfo.setCargoLocation(cargoLocation);
			trayInfo.setUpdateTime(new Date());
			List<Map<String,Object>> mapList = warehouseService.GetData(null,orgTrayInfo.getBuildingNum(),orgTrayInfo.getFloorNum(),orgTrayInfo.getRoomNum()
					,orgTrayInfo.getAreaNum(),null,null,null);
			if (mapList!=null && mapList.size() > 0) {
				Map<String, Object> maxXZ = mapList.get(0);
				if (maxXZ == null || maxXZ.get("MAXX") == null){
					return "未获取到原托盘X轴坐标数";
				}
				if (maxXZ == null || maxXZ.get("MAXZ") == null){
					return "未获取到原托盘层高数";
				}
				trayInfo.setActualStoreroomX((Integer.parseInt(maxXZ.get("MAXX").toString())+1)+"");//X坐标
			}else{
				trayInfo.setActualStoreroomX("1");//X坐标
			}
			trayInfo.setActualStoreroomZ("1");//Z坐标
			trayInfoDao.save(trayInfo);
			result.setCode(0);
			result.setMsg("移库成功！");
		}else{
			result.setCode(1);
			result.setMsg("数据错误，该托盘数据状态有误！");
		}
		return JSON.toJSONString(result);
	}
}
