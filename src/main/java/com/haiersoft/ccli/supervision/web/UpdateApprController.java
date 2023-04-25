package com.haiersoft.ccli.supervision.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.supervision.entity.ApprHead;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.service.ApprHeadService;
import com.haiersoft.ccli.supervision.service.ApprInfoService;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;

//更新核放单详情controller
@Controller
@RequestMapping("supervision/appr")
public class UpdateApprController {
	@Autowired
	private ApprHeadService apprHeadService;
	@Autowired
	GetKeyService getKeyService;
	@Autowired
	FljgWsClient fljgWsClient;
	//申请单详情
	//@Transactional
	@RequestMapping(value = "applyDetail", method = RequestMethod.GET)
	@ResponseBody
	public String applyDetail(HttpServletRequest request) throws IOException, ServiceException {
		
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		//设置接口名
		String serviceName = "ApprGet";
		
		List<String> apprIdList = apprHeadService.findAllUpdateApprID();
		
		for(String apprId:apprIdList) {
			String apprget="{\"KeyModel\":{\"ApprId\":\""+apprId+"\"}}";
			
			String result = fljgWsClient.getResult(apprget, tickId, serviceName);
			System.out.println("result: " + result);
			
			JSONObject jsObject = JSON.parseObject(result);
			ApprHead ah = JSONObject.parseObject(jsObject.getString("ApprHead"), ApprHead.class);
			if (null != ah)
			apprHeadService.updateByApprId(ah);

		}		

		return "success";

	}
}
