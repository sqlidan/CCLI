package com.haiersoft.ccli.wms.web.tallying;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.remoting.hand.in.service.WarehousingWebService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.EnterTallyInfoToExcel;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.service.tallying.WarehouseManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *库管入库
 */
@Controller
@RequestMapping(value = "wms/warehouse")
public class WarehouseManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TrayInfo.class);
	@Autowired
	private WarehouseManageService warehouseService;

	//库管入库
	@RequestMapping(value = "warehouse", method = RequestMethod.GET)
	public String warehouse() {
		return "wms/tallying/warehouse";
	}

	//获取托盘数据
	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<TrayInfo> page = getPage(request);
		page.orderBy("enterStockTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		//默认为00 00已收货（理货确认）01已上架（库管确认）10出库中（装车单）11出库理货（库管确认）12已出库（理货确认）20待回库 21回库收货（理货确认）  99报损货物状态
		PropertyFilter filter = new PropertyFilter("EQS_cargoState", "00");
		filters.add(filter);
		page = warehouseService.search(page, filters);
		return getEasyUIData(page);
	}

	@RequestMapping(value="GetData",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetData(HttpServletRequest request) {
		String ids = request.getParameter("ids");//ids
		String buildingNum = request.getParameter("buildingNum")==null?"0":request.getParameter("buildingNum");// 楼号
		String floorNum = request.getParameter("floorNum");// 楼层号
		String roomNum = request.getParameter("roomNum");// 房间号
		String areaNum = request.getParameter("areaNum");// 区号
		String storeRoomNum = request.getParameter("storeRoomNum");// 库房号
		String layers = request.getParameter("layers");// 货架层数
		List<Map<String,Object>> trayFram = warehouseService.GetData(ids,buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers,null);
		return trayFram;
	}
	//获取框架数据
	@RequestMapping(value="GetFram",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFram(HttpServletRequest request) {
		String buildingNum = request.getParameter("buildingNum")==null?"0":request.getParameter("buildingNum");// 楼号
		String floorNum = request.getParameter("floorNum");// 楼层号
		String roomNum = request.getParameter("roomNum");// 房间号
		String areaNum = request.getParameter("areaNum");// 区号
		String storeRoomNum = request.getParameter("storeRoomNum");// 库房号
		String layers = request.getParameter("layers");// 货架层数
		List<Map<String,Object>> trayFram = warehouseService.GetFram(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
		return trayFram;
	}
	//获取框架内数据是否有货物（填充颜色）
	@RequestMapping(value="GetFramInfo",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String,Object>> GetFramInfo(HttpServletRequest request) {
		String buildingNum = request.getParameter("buildingNum")==null?"0":request.getParameter("buildingNum");// 楼号
		String floorNum = request.getParameter("floorNum");// 楼层号
		String roomNum = request.getParameter("roomNum");// 房间号
		String areaNum = request.getParameter("areaNum");// 区号
		String storeRoomNum = request.getParameter("storeRoomNum");// 库房号
		String layers = request.getParameter("layers");// 货架层数
		List<Map<String,Object>> trayFram = warehouseService.GetFramInfo(buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
		return trayFram;
	}


	@RequestMapping(value = "confirm/{location}", method = RequestMethod.POST)
	@ResponseBody
	public String confirm(@RequestBody List<TrayInfo> trayInfoList,@PathVariable("location") String location) {
		User user = UserUtil.getCurrentUser();
		System.out.println("trayInfoList"+ JSON.toJSONString(trayInfoList));
		System.out.println("location"+location);
		if (trayInfoList!=null && trayInfoList.size() > 0){
			for (TrayInfo forTrayInfo:trayInfoList){
				String result = warehouseService.inStorageConfirm(forTrayInfo.getTrayId(),location,user.getName(), forTrayInfo.getActualStoreroomX(), forTrayInfo.getActualStoreroomZ());
				if (!result.contains("操作成功")){
					return result;
				}
			}
		}
		return "success";
	}
}
