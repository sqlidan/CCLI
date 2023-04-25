package com.haiersoft.ccli.wms.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.api.service.ExchangeInfoService;
import com.haiersoft.ccli.api.service.ExchangeService;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BaseClientPledge;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ClientPledgeService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * 质押换货
 */
@Controller
@RequestMapping("exchange/comfirm/")
public class ExchangeComfirmController extends BaseController{
	@Autowired
	private ApiPledgeService apiPledgeService;
	
	@Autowired
	private ExchangeInfoService exchangeInfoService;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired
	TrayInfoService trayInfoService;
	
	@Autowired
	TrayInfoService trayInfoService2;
	@Autowired
	SkuInfoService skuInfoService;

	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "wms/pledge/exchangeInfo";
	}
	/**
	 * 
	 * @author 
	 * @Description: 
	 * @date 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {

        Page<ExchangeInfo> page = getPage(request);
        page.orderBy("id").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = exchangeInfoService.search(page, filters);
        return getEasyUIData(page);
		//return exchangeInfoService.getExchangeList();
	}

	/**
	 * 确认按钮操作
	 */
	@RequestMapping(value = "/exchange/confirm/{id}", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String exConfirm(@PathVariable("id") String id) {
		
		ExchangeInfo ex = exchangeInfoService.get(id);
		
		HashMap<String, Object> oRecordCounts = apiPledgeService.countRelatedTrendIdByRelatedTrendId(ex.getRelatedTrendId());
		//1 判断解押数量不足
		Integer pledgedNum = Integer.valueOf(oRecordCounts.get("sumNum").toString());
		Double pledgedWeight = Double.valueOf(oRecordCounts.get("sumWeight").toString());
		if(ex.getRelieveNumber() > pledgedNum) {
			return "解押数量大于当前质押数量！";
		}
		Date currDate = new Date();
		
		//生成解押记录
		ApiPledge releasedRecord = new ApiPledge();
		
		releasedRecord.setAccountId(ex.getAccountId());
		releasedRecord.setTrayInfoId(ex.getOriginalPledgeId());
		releasedRecord.setPledgeNumber(ex.getRelieveNumber());
		releasedRecord.setPledgeWeight(ex.getRelieveWeight());
		releasedRecord.setState(4); //设置为换货解押
		releasedRecord.setCreateDate(currDate);
		releasedRecord.setTrendId(ex.getSourceTrendId());
		releasedRecord.setRelatedTrendId(ex.getRelatedTrendId());
		ApiPledge oRecord = apiPledgeService.find("trendId", ex.getRelatedTrendId());
		//根据原质押记录填充信息
		releasedRecord.setBillNum(oRecord.getBillNum());
		releasedRecord.setCtnNum(oRecord.getCtnNum());
		releasedRecord.setCustomerName(oRecord.getCustomerName());
		releasedRecord.setCustomerId(oRecord.getCustomerId());
		releasedRecord.setCustomerCode(oRecord.getCustomerCode());
		releasedRecord.setSku(oRecord.getSku());
		releasedRecord.setItemClass(oRecord.getItemClass());
		releasedRecord.setpName(oRecord.getpName());
		releasedRecord.setEnterState(oRecord.getEnterState());
		releasedRecord.setWareHouse(oRecord.getWareHouse());
		releasedRecord.setWareHouseId(oRecord.getWareHouseId());
	
		releasedRecord.setConfirmStatus(1);
		
		
		
		
		//生成质押记录
		ApiPledge pledgeRecord = new ApiPledge();
		pledgeRecord.setAccountId(ex.getAccountId());
		pledgeRecord.setTrayInfoId(ex.getNewPledgeId());
		pledgeRecord.setPledgeNumber(ex.getPledgeNumber());
		pledgeRecord.setPledgeWeight(ex.getPledgeWeight());
		pledgeRecord.setState(5); //设置为换货质押
		pledgeRecord.setCreateDate(currDate);
		pledgeRecord.setTrendId(ex.getTrendId());
		
		TrayInfo info = trayInfoService.get(ex.getNewPledgeId());
		//判断要质押的库存货物情况
		if (null == info) {
			return "质押货物不存在！";			
		}
		if (!("0".equals(info.getIfTransfer())) && !("3".equals(info.getIfTransfer()) )) {
			return "该货物状态无法质押，请确认！";			
		}
		BaseSkuBaseInfo bs = skuInfoService.get(info.getSkuId());
		
		pledgeRecord.setBillNum(info.getBillNum());
		pledgeRecord.setCtnNum(info.getCtnNum());
		pledgeRecord.setCustomerName(info.getStockName());
		pledgeRecord.setCustomerId(Integer.valueOf(info.getStockIn()));
		pledgeRecord.setSku(info.getSkuId());
		pledgeRecord.setItemClass(Integer.valueOf(bs.getClassType()));
		pledgeRecord.setpName(info.getCargoName());
		pledgeRecord.setEnterState(info.getEnterState());
		pledgeRecord.setWareHouse(info.getWarehouse());
		pledgeRecord.setWareHouseId(info.getWarehouseId());
		pledgeRecord.setConfirmStatus(1);

		//判断质押数量不足
		// 检查质押数量或重量是否超出库存数量（单条检查）
		if (pledgeRecord.getPledgeNumber()>info.getNowPiece()) {
			return "质押数量超过本条库存的数量！";
		}
		// 检查质押数量或重量是否超出已静态质押数量（多条检查）
		HashMap<String, Object> pledgedMap = apiPledgeService.countPledgedByTrayInfoId(pledgeRecord.getTrayInfoId());
		Integer pNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
		if (pledgeRecord.getPledgeNumber()>(info.getNowPiece()-pNum)) {
			return "质押数量超过本条库存可质押的数量！";
		}
		
		// 检查质押数量或重量是否超出可质押数量（总量）
		if (!apiPledgeService.pledgeCountCheck(pledgeRecord)) {
			return "质押数量或质押重量超出可质押数量！";
		}
		
		apiPledgeService.save(releasedRecord);
		apiPledgeService.save(pledgeRecord);
		
		mkReleasedRecord(releasedRecord) ;
		mkPledgeRecord(pledgeRecord);
		ex.setStatus("1"); //修改换货记录状态
		ex.setConfirmDate(new Date());
		exchangeInfoService.save(ex); //保存换货记录
		exchangeService.getexchangeInfo(id); //回传确认通知
		
		
		
//		//修改原押品库存记录
//		TrayInfo trayInfo = trayInfoService.get(ex.getOriginalPledgeId());
//		trayInfo.setPledgePiece( trayInfo.getPledgePiece()-(pledgedNum - ex.getRelieveNumber()));
//		trayInfo.setPledgeGrossWeight(trayInfo.getPledgeGrossWeight()-(pledgedWeight.doubleValue()-ex.getRelieveWeight()));
//
//		// 如果库存表记录中的质押件数为0时，设置IfTransfer标志为0正常
//		if (0==trayInfo.getPledgePiece()) {
//			trayInfo.setIfTransfer("0");
//		}
//		trayInfoService.update(trayInfo);
		return "success";
		
	}
	/**
	 * 驳回按钮操作
	 */
	@RequestMapping(value = "/exchange/refuse/{id}", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String exRefuse(@PathVariable("id")String id) {
		ExchangeInfo ex = exchangeInfoService.get(id);
		ex.setStatus("2"); //修改换货记录状态
		ex.setConfirmDate(new Date());
		exchangeInfoService.save(ex); //保存换货记录	
		//调用同步接口
		//exchangeService.getexchangeInfo(id);
		return "success";
		
	}
	
	@Autowired 
	ClientPledgeService clientPledgeService;
	
	private void mkReleasedRecord(ApiPledge releasedRecord) {
		//增加解押记录
		BaseClientPledge releasedClientPledge = new BaseClientPledge();
		releasedClientPledge.setBillNum(releasedRecord.getBillNum());
		releasedClientPledge.setClientName(releasedRecord.getCustomerName());
		releasedClientPledge.setCtnNum(releasedRecord.getCtnNum());
		releasedClientPledge.setCuser(releasedRecord.getAccountId());
		releasedClientPledge.setSkuId(releasedRecord.getSku());
		
		releasedClientPledge.setCodeNum(releasedRecord.getTrendId());
		releasedClientPledge.setClient(releasedRecord.getCustomerId().toString());
		releasedClientPledge.setPname(releasedRecord.getpName());
		releasedClientPledge.setCtime(new Date());
		releasedClientPledge.setPtype(1);
		releasedClientPledge.setPclass(2);
		
		HashMap<String, Object> pledgedMap = apiPledgeService.countPledgedByTrayInfoId(releasedRecord.getTrayInfoId());
		Integer rNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
		Double rWeight = Double.valueOf(pledgedMap.get("sumWeight").toString());
		//计算本次解押数量
		//由于解押时接口传来的数量为解押后剩余的数量
		//所以这里解押数量的计算为 已质押数 - 剩余数量
		//BaseClientPledge中的解押数量为负值，所以要用0减解押数量
		Integer countNum = 0 - (rNum - releasedRecord.getPledgeNumber());
		Double countWeight = 0 - (rWeight-releasedRecord.getPledgeWeight());
		releasedClientPledge.setNum(countNum.doubleValue());
		releasedClientPledge.setNetWeight(countWeight);
		releasedClientPledge.setWarehouse(releasedRecord.getWareHouse());
		releasedClientPledge.setWarehouseId(releasedRecord.getWareHouseId());
		releasedClientPledge.setEnterState(releasedRecord.getEnterState());
		releasedClientPledge.setTrack("1");
		clientPledgeService.save(releasedClientPledge);
		//解押后修改库存表中的质押数量
		TrayInfo trayInfo = new TrayInfo();
		trayInfo = trayInfoService.get(releasedRecord.getTrayInfoId());
		trayInfo.setPledgePiece(releasedRecord.getPledgeNumber());
		trayInfo.setPledgeGrossWeight(releasedRecord.getPledgeWeight());
		// 如果库存表记录中的质押件数为0时，设置IfTransfer标志为0正常
		if (0==trayInfo.getPledgePiece()) {
			trayInfo.setIfTransfer("0");
		}
		
		trayInfoService.update(trayInfo);
	
	}
	
	private void mkPledgeRecord(ApiPledge pledgeRecord) {		
		//增加质押记录
		BaseClientPledge pledgedClientPledge = new BaseClientPledge();
		pledgedClientPledge.setBillNum(pledgeRecord.getBillNum());
		pledgedClientPledge.setClientName(pledgeRecord.getCustomerName());
		pledgedClientPledge.setCtnNum(pledgeRecord.getCtnNum());
		pledgedClientPledge.setCuser(pledgeRecord.getAccountId());
		pledgedClientPledge.setSkuId(pledgeRecord.getSku());
		pledgedClientPledge.setNum(pledgeRecord.getPledgeNumber().doubleValue());
		pledgedClientPledge.setCodeNum(pledgeRecord.getTrendId());
		pledgedClientPledge.setClient(pledgeRecord.getCustomerId().toString());
		pledgedClientPledge.setPname(pledgeRecord.getpName());
		pledgedClientPledge.setCtime(new Date());
		pledgedClientPledge.setPtype(1);
		pledgedClientPledge.setPclass(2);
		pledgedClientPledge.setNetWeight(pledgeRecord.getPledgeWeight());
		pledgedClientPledge.setWarehouse(pledgeRecord.getWareHouse());
		pledgedClientPledge.setWarehouseId(pledgeRecord.getWareHouseId());
		pledgedClientPledge.setEnterState(pledgeRecord.getEnterState());
		pledgedClientPledge.setTrack("1");		
		clientPledgeService.save(pledgedClientPledge);
		//质押后修改库存表中的质押数量
		TrayInfo trayInfoPledged = new TrayInfo();
		trayInfoPledged = trayInfoService.get(pledgeRecord.getTrayInfoId());
		// 修改库存表的可质押件数/重量字段
		// 如果当前库存表中的可质押件数/重量为空时，即之前没有做过供应链质押，则质押件数/重量为
		if (null == trayInfoPledged.getPledgePiece() || null == trayInfoPledged.getPledgeGrossWeight()) {
			trayInfoPledged.setPledgePiece(pledgeRecord.getPledgeNumber());
			trayInfoPledged.setPledgeGrossWeight(pledgeRecord.getPledgeWeight());
		} else {
			trayInfoPledged.setPledgePiece(trayInfoPledged.getPledgePiece() + pledgeRecord.getPledgeNumber());
			trayInfoPledged.setPledgeGrossWeight(trayInfoPledged.getPledgeGrossWeight() + pledgeRecord.getPledgeWeight());
		}
		
		trayInfoPledged.setIfTransfer("3");
		trayInfoService.update(trayInfoPledged);
	}
}
