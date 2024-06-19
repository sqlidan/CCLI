package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
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
		PropertyFilter filter = new PropertyFilter("NEQS_cargoState", "01");//已删除
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
		List<Map<String,Object>> trayFram = warehouseService.GetData(ids,buildingNum,floorNum,roomNum,areaNum,storeRoomNum,layers);
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


	@RequestMapping(value = "confirm/{ids}/{target}", method = RequestMethod.GET)
	@ResponseBody
	public String confirm(@PathVariable("ids") String ids,@PathVariable("target") String target) {
		System.out.println("ids"+ids.toString());
		System.out.println("target"+target.toString());
		return "success";
	}
}
