package com.haiersoft.ccli.base.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.Warehouse;
import com.haiersoft.ccli.base.service.WarehouseService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 
 * @author Connor.M
 * @ClassName: WarehouseController
 * @Description: 仓库的Controller
 * @date 2016年3月3日 下午3:43:51
 */
@Controller
@RequestMapping("base/warehouse")
public class WarehouseController extends BaseController {
	
	@Autowired
	private WarehouseService warehouseService;
	
	/**
	 * @Description: 获得仓库集合
	 */
	@RequestMapping(value = "getWarehouse", method = RequestMethod.GET)
	@ResponseBody
	public List<Warehouse> getWarehouse(HttpServletRequest request, HttpServletResponse response) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		List<Warehouse> warehouse = warehouseService.search(filters);
		return warehouse;
	}
	
	
}
