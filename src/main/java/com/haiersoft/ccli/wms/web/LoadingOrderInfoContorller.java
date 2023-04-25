package com.haiersoft.ccli.wms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
/**
 * 出库订单明细
 * @author LZG
 *
 */
@Controller
@RequestMapping("bis/loadinginfo")
public class LoadingOrderInfoContorller extends BaseController {
	@Autowired
	private LoadingOrderInfoService loadingOrderInfoService;
	 
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisLoadingOrderInfo> page = getPage(request);
		page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = loadingOrderInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="alljson",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getJosnData(HttpServletRequest request) {
		List<Map<String,Object>> getList=null; 
		String getOrderId=request.getParameter("filter_EQS_loadingPlanNum");
		getList = loadingOrderInfoService.findInfoList(getOrderId);
		if(getList==null){
			getList=new ArrayList<Map<String,Object>>(); 
		}
		return getList;
	}
}
