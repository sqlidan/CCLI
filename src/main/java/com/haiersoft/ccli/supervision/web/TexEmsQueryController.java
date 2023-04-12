package com.haiersoft.ccli.supervision.web;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseHscode;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.HscodeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ApprInfo;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;

/**
 * 分类监管 报税账册查询controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/taxEmsQuery")
public class TexEmsQueryController extends BaseController{	

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/taxEmsQuery";
	}
	
	@Autowired
	GetKeyService getKeyService;
	
	@Autowired
	FljgWsClient fljgWsClient;
	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,String BwlNo ,String TradeCode) throws RemoteException, ServiceException {		
		  //查询条件为空时返回空的列表 Map<String, Object> map = new HashMap<String, Object>();
		  String returnstr="{\r\n" + 
			  		"	\"state\": 1,\r\n" + 
			  		"	\"message\": null,\r\n" + 
			  		"	\"rows\": null,\r\n" + 
			  		"	\"TotalCount\": 0,\r\n" + 
			  		"	\"CheckInfos\": null\r\n" + 
			  		"}";
			  Map<String, Object> empMaps = (Map<String, Object>)JSON.parse(returnstr); 
		  if(StringUtils.isBlank(BwlNo) && StringUtils.isBlank(TradeCode)) {
			  
			/*
			 * map.put("state", ""); map.put("message", ""); map.put("rows", ""); return
			 * map;
			 */ 
			  BwlNo = "";
			  TradeCode = "";	
			  
			  return empMaps;
			  
		  }
		 
		
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BwlNo", BwlNo.trim());
        jsonObject.put("TradeCode", TradeCode.trim());        
		
		//1 获得key
		String tickId=getKeyService.builder();
		
		//如果获取tickId为空时返回空列表
		if((null ==tickId) || ("".equals(tickId)))
		{
			return empMaps;
		}
		//2 调用接口
		//先调用账册同步接口
		String serviceName = "TaxEmsSync";
		fljgWsClient.getResult(jsonObject, tickId, serviceName);
		//调用保税账册查询
		serviceName = "TaxEmsGoodsQuery";

		String result = fljgWsClient.getResult(jsonObject, tickId, serviceName);
		System.out.println("result: " + result);
		//if (result == null || result.equals(""))
		//	return null;


		String str="{\r\n" + 
				"    \"state\":1,\r\n" + 
				"    \"message\":null,\r\n" + 
				"    \"PageList\":[\r\n" + 
				"        {\r\n" + 
				"            \"EmsNo\":\"T4230W000033\",\r\n" + 
				"            \"GdsSeqno\":\"354\",\r\n" + 
				"            \"PreQty\":\"9590000\",\r\n" + 
				"            \"ActQty\":\"9765580\",\r\n" + 
				"            \"TbFlag\":\"0\",\r\n" + 
				"            \"DefendFlag\":\"0\",\r\n" + 
				"            \"InDate\":\"20200423 00:00:00\",\r\n" + 
				"            \"GdsMtno\":\"1\",\r\n" + 
				"            \"Gdecd\":\"2615100000\",\r\n" + 
				"            \"GdsNm\":\"锆英砂\",\r\n" + 
				"            \"GdsSpcfModelDesc\":\"黄褐色颗粒状\",\r\n" + 
				"            \"Natcd\":\"601\",\r\n" + 
				"            \"DclUnitcd\":\"035\",\r\n" + 
				"            \"LawfUnitcd\":\"035\",\r\n" + 
				"            \"SecdLawfUnitcd\":null,\r\n" + 
				"            \"DclUprcAmt\":\"1.6\",\r\n" + 
				"            \"DclCurrcd\":\"502\",\r\n" + 
				"            \"AvgPrice\":\"1.6\",\r\n" + 
				"            \"TotalAmt\":\"1\",\r\n" + 
				"            \"InQty\":\"9921580\",\r\n" + 
				"            \"InLawfQty\":\"9921580\",\r\n" + 
				"            \"InSecdLawfQty\":\"0\",\r\n" + 
				"            \"ActlIncQty\":\"0\",\r\n" + 
				"            \"ActlRedcQty\":null,\r\n" + 
				"            \"PrevdIncQty\":\"0\",\r\n" + 
				"            \"PrevdRedcQty\":\"175580\",\r\n" + 
				"            \"OutDate\":null,\r\n" + 
				"            \"LimitDate\":null,\r\n" + 
				"            \"InvtGNo\":\"1\",\r\n" + 
				"            \"CusmExeMarkcd\":\"1\",\r\n" + 
				"            \"InType\":null,\r\n" + 
				"            \"InvtNo\":\"QD423020I000000792\",\r\n" + 
				"            \"Rmk\":null\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"TotalCount\":0,\r\n" + 
				"    \"CheckInfos\":null\r\n" + 
				"}";
		
		 Map<String, Object> maps = (Map<String, Object>)JSON.parse(result);  
		 maps.put("rows",maps.remove("PageList"));
		return maps;
	}
	
}
