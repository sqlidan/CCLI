package com.haiersoft.ccli.supervision.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.supervision.entity.ManiHead;
import com.haiersoft.ccli.supervision.service.FljgWsClient;
import com.haiersoft.ccli.supervision.service.GetKeyService;
import com.haiersoft.ccli.supervision.service.ManiHeadService;
import com.haiersoft.ccli.supervision.service.ManiInfoService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;

//更新核放单详情controller
@Controller
@RequestMapping("supervision/mani")
public class UpdateManiController {
	private static Logger logger = LoggerFactory.getLogger(UpdateManiController.class);
	
	@Autowired
	ManiHeadService maniHeadService;
	
	@Autowired
	ManiInfoService maniInfoService;
	
    @Autowired
    EnterStockInfoService enterStockInfoService;
    
	@Autowired
	GetKeyService getKeyService;
	
	@Autowired
	FljgWsClient fljgWsClient;


	//更新核放单详情
	@RequestMapping(value = "applyDetail", method = RequestMethod.GET)
	@ResponseBody
	public String applyDetail(HttpServletRequest request) throws IOException, ServiceException {
		
		//入库核放单更新的核放单中不包括已提交到货确认的记录
			//更新入库核放单 Status为空的记录 
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();	
		filters.add(new PropertyFilter("EQS_IeFlag", "I"));	
		filters.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));	
		filters.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		filters.add(new PropertyFilter("NULLS_Status","NULL"));		
		List<ManiHead> iheadList = maniHeadService.search(filters);
		if(iheadList.size() !=0) {
			update(iheadList);
		}
			//更新入库核放单 Status为0 暂存的记录
		List<PropertyFilter> filtersIn0 = new ArrayList<PropertyFilter>();	
		filtersIn0.add(new PropertyFilter("EQS_IeFlag", "I"));	
		filtersIn0.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));	
		filtersIn0.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		filtersIn0.add(new PropertyFilter("EQS_Status","0"));		
		List<ManiHead> iheadList0 = maniHeadService.search(filtersIn0);
		if(iheadList0.size() !=0) {
			update(iheadList0);
		}

			//更新入库核放单 Status为 1 申报的记录
		List<PropertyFilter> filtersIn1 = new ArrayList<PropertyFilter>();	
		filtersIn1.add(new PropertyFilter("EQS_IeFlag", "I"));	
		filtersIn1.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));	
		filtersIn1.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		filtersIn1.add(new PropertyFilter("EQS_Status","1"));		
		List<ManiHead> iheadList1 = maniHeadService.search(filtersIn1);
		if(iheadList1.size() !=0) {
			update(iheadList1);
		}
			//更新入库核放单 Status为 2 审批通过的记录
		List<PropertyFilter> filtersIn2 = new ArrayList<PropertyFilter>();	
		filtersIn2.add(new PropertyFilter("EQS_IeFlag", "I"));	
		filtersIn2.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));	
		filtersIn2.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		filtersIn2.add(new PropertyFilter("EQS_Status","2"));		
		List<ManiHead> iheadList2 = maniHeadService.search(filtersIn2);
		if(iheadList2.size() !=0) {
			update(iheadList2);
		}	

			//更新入库核放单 Status为 3 审批退回的记录
		List<PropertyFilter> filtersIn3 = new ArrayList<PropertyFilter>();	
		filtersIn3.add(new PropertyFilter("EQS_IeFlag", "I"));	
		filtersIn3.add(new PropertyFilter("EQS_ManiConfirmStatus", "N"));	
		filtersIn3.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		filtersIn3.add(new PropertyFilter("EQS_Status","2"));		
		List<ManiHead> iheadList3 = maniHeadService.search(filtersIn3);
		if(iheadList3.size() !=0) {
			update(iheadList3);
		}	
		
		//出库核放单更新 PassStatus 为空的
		List<PropertyFilter> filtersOut = new ArrayList<PropertyFilter>();
		filtersOut.add(new PropertyFilter("EQS_IeFlag", "E"));	
		filtersOut.add(new PropertyFilter("NULLS_PassStatus","NULL"));
		filtersOut.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		List<ManiHead> eheadList = maniHeadService.search(filtersOut);

		if(eheadList.size() !=0) {
			update(eheadList);
		}

		//出库核放单状态 PassStatus 为1 已过闸的
		List<PropertyFilter> filtersOut1 = new ArrayList<PropertyFilter>();
		filtersOut1.add(new PropertyFilter("EQS_IeFlag", "E"));	
		filtersOut1.add(new PropertyFilter("EQS_PassStatus","1"));
		filtersOut1.add(new PropertyFilter("NNULLS_ManifestId","NULL"));
		List<ManiHead> eheadListOut1 = maniHeadService.search(filtersOut1);

		if(eheadListOut1.size() !=0) {
			update(eheadListOut1);
		}

		
		return "success";

	}
	
	private void update(List<ManiHead> headlist) throws RemoteException, ServiceException {
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		//设置接口名
		String serviceName = "ManiGet";		
		
		for(ManiHead maniHead:headlist) {
			
			String maniGet="{\"KeyModel\":{\"ManifestId\":\""+maniHead.getManifestId()+"\"}}";
			logger.error(">>>>>>>>>>>>>>>>>调用核放单更新核放json： "+maniGet);				
			
			String result = fljgWsClient.getResult(maniGet, tickId, serviceName);
			System.out.println("result: " + result);
			logger.error(">>>>>>>>>>>>>>>>>调用核放单更新核放result： "+result);					
			JSONObject jsObject = JSON.parseObject(result);
			ManiHead mh = JSONObject.parseObject(jsObject.getString("Manibase"), ManiHead.class);
			if (null != mh)
			maniHeadService.updateByManiId(mh);

		}	
	}
	
	
	//单条更新核放单详情
	@RequestMapping(value = "applyDetail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String applyDetail(@PathVariable("id") String id) throws IOException, ServiceException {
		
		ManiHead ihead = maniHeadService.get(id);
		updateSingle(ihead);
		return "success";
	}
	private void updateSingle(ManiHead maniHead) throws RemoteException, ServiceException {
		// 1 获得key
		String tickId = getKeyService.builder();
		System.out.println(tickId);
		// 2 调用接口
		// 设置接口名
		String serviceName = "ManiGet";

		String maniGet = "{\"KeyModel\":{\"ManifestId\":\"" + maniHead.getManifestId() + "\"}}";
		logger.error(">>>>>>>>>>>>>>>>>调用核放单更新核放json： " + maniGet);

		String result = fljgWsClient.getResult(maniGet, tickId, serviceName);
		System.out.println("result: " + result);
		logger.error(">>>>>>>>>>>>>>>>>调用核放单更新核放result： " + result);
		JSONObject jsObject = JSON.parseObject(result);
		ManiHead mh = JSONObject.parseObject(jsObject.getString("Manibase"), ManiHead.class);
		if (null != mh)
			maniHeadService.updateByManiId(mh);

	}
}
