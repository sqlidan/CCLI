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
import com.haiersoft.ccli.wms.entity.BisTransferStockTrayInfo;
import com.haiersoft.ccli.wms.service.TransferTrayInfoService;
/**
 * 货转托盘明细
 * @author LZG
 *
 */
@Controller
@RequestMapping("bis/ttrayinfo")
public class TransferTrayInfoContorller extends BaseController {
	@Autowired
	private TransferTrayInfoService transferTaryInfoService;
	 
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisTransferStockTrayInfo> page = getPage(request);
		page.orderBy("crTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = transferTaryInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	/*
	 * 列表页面table获取json
	 * */
	@RequestMapping(value="alljson",method = RequestMethod.GET)
	@ResponseBody
	public List<BisTransferStockTrayInfo> getJosnData(HttpServletRequest request) {
		String transferId=request.getParameter("transferId");
		List<BisTransferStockTrayInfo> getList = transferTaryInfoService.getTransferStockTrayInfoList(transferId);
		if(getList==null){
			getList=new ArrayList<BisTransferStockTrayInfo>(); 
		}
		return getList;
	}
}
