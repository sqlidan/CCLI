package com.haiersoft.ccli.api.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.api.aspect.ApiPledgedCheck;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.api.entity.ExchangeRequestVo;
import com.haiersoft.ccli.api.entity.PledgeRequestVo;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.api.service.ExchangeInfoService;
import com.haiersoft.ccli.api.service.ExchangeService;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * 换货
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("api/users")
public class ExchangeInfoController {

	@Autowired
	private ApiPledgeService apiPledgeService;
	@Autowired
	private ExchangeInfoService exchangeInfoService;
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired
	TrayInfoService trayInfoService;
	@Autowired
	SkuInfoService skuInfoService;

	/**
	 * 接收参数,存入数据库
	 * 
	 * @param params
	 * @return
	 */
	//@ApiPledgedCheck
	@RequestMapping(value = "exchangeGoods", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo exchange(@RequestBody ExchangeRequestVo vo) {
		ExchangeInfo exchangeInfo = new ExchangeInfo();
//		String strAccountId = vo.getAccountId(); // 
		exchangeInfo.setAccountId(vo.getAccountId()); // 账户ID
		// 原押品信息
//		Integer intPledgeId =  Integer.valueOf(vo.getPledgeId());
//		Integer intRelieveNumber = vo.getRelieveNumber();
//		Double doubleRelieveWeight = vo.getRelieveWeight();
//		String strRelieveOrder = vo.getRelieveOrder();

		// 如果原押品主键 pledgeId 无质押,返回错误信息
		TrayInfo info = trayInfoService.get(Integer.valueOf(vo.getPledgeId().trim()));
		if (null == info) {
			return ResponseVo.error("原押品主键货物不存在！");
		}

		// 如果原质押唯一标识 relatedTrendId 不存在,返回错误信息
		ApiPledge apiPledge = apiPledgeService.findUniqueByTrendId(vo.getRelatedTrendId());
		if (null == apiPledge) {
			return ResponseVo.error("原质押记录不存在！");
		}
		// 如果原押品主键的可解押数量不足 返回错误信息
		// 根据库存主键查询已静态质押的数量
		HashMap<String, Object> oRecordCounts = apiPledgeService
				.countRelatedTrendIdByRelatedTrendId(vo.getRelatedTrendId());
		// 判断解押数量不足
		Integer pledgedNum = Integer.valueOf(oRecordCounts.get("sumNum").toString());
		if (vo.getRelieveNumber() > pledgedNum) {
			return ResponseVo.error("解押数量大于当前质押数量！");
		}

		BaseSkuBaseInfo bs = skuInfoService.get(info.getSkuId());
		exchangeInfo.setOriginalPledgeId(Integer.valueOf(vo.getPledgeId()));
		exchangeInfo.setRelieveNumber(vo.getRelieveNumber());
		exchangeInfo.setRelieveWeight(vo.getRelieveWeight());
		exchangeInfo.setRelieveOrder(vo.getRelieveOrder());

		// 填充解押货物信息
		exchangeInfo.setReleasedBillNum(info.getBillNum());
		exchangeInfo.setReleasedCtnNum(info.getCtnNum());
		exchangeInfo.setReleasedCustomerName(info.getStockName());
		exchangeInfo.setReleasedCustomerId(Integer.valueOf(info.getStockIn()));
		exchangeInfo.setReleasedSku(info.getSkuId());
		exchangeInfo.setReleasedItemClass(Integer.valueOf(bs.getClassType()));
		exchangeInfo.setReleasedPName(info.getCargoName());
		exchangeInfo.setReleasedEnterState(info.getEnterState());
		exchangeInfo.setReleasedWareHouse(info.getWarehouse());
		exchangeInfo.setReleasedWareHouseId(info.getWarehouseId());

		// 新押品信息判断，同静态质押时的判断
//		Integer intId = Integer.valueOf(vo.getId());
//		Integer intPledgeNumber = vo.getPledgeNumber();
//		Double doublePledgeWeight = vo.getPledgeWeight();
//		String strOrder = vo.getOrder();

		// 如果在库主键 id 数量不足,返回错误信息
		TrayInfo pledgedInfo = trayInfoService.get(Integer.valueOf(vo.getId().trim()));

		if (null == pledgedInfo) {
			return ResponseVo.error("质押货物不存在！");
		}
		// 判断当前处于什么状态 如果不是0正常 3静态质押的不可以质押
		if (!("0".equals(pledgedInfo.getIfTransfer())) && !("3".equals(pledgedInfo.getIfTransfer()))) {
			return ResponseVo.error("该货物状态无法质押，请确认！");
		}
		// 检查质押数量或重量是否超出库存数量（检查单条库存）
		if (vo.getPledgeNumber() > pledgedInfo.getNowPiece()) {
			return ResponseVo.error("质押数量超过本条库存的数量！");
		}
		// 检查质押数量或重量是否超出已静态质押数量（多条质押、解押记录时检查）
		HashMap<String, Object> pledgedMap = apiPledgeService.countPledgedByTrayInfoId(Integer.valueOf(vo.getId()));
		Integer newPledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
		if (vo.getPledgeNumber() > (info.getNowPiece() - newPledgedNum)) {
			return ResponseVo.error("质押数量超过本条库存可质押的数量！");
		}

		exchangeInfo.setNewPledgeId(Integer.valueOf(vo.getId()));
		exchangeInfo.setPledgeNumber(vo.getPledgeNumber());
		exchangeInfo.setPledgeWeight(vo.getPledgeWeight());
		exchangeInfo.setPledgeOrder(vo.getOrder());

		BaseSkuBaseInfo bsp = skuInfoService.get(pledgedInfo.getSkuId());
		ApiPledge temp = new ApiPledge();
		temp.setItemClass(Integer.valueOf(bsp.getClassType()));
		temp.setCustomerId(Integer.valueOf(pledgedInfo.getStockIn()));
		temp.setPledgeNumber(vo.getPledgeNumber());
		// 检查质押数量或重量是否超出可质押数量（总量）
		if (!apiPledgeService.pledgeCountCheck(temp)) {
			return ResponseVo.error("质押数量或质押重量超出可质押数量！");
		}
		// 填充质押货物信息
		exchangeInfo.setPledgedBillNum(pledgedInfo.getBillNum());
		exchangeInfo.setPledgedCtnNum(pledgedInfo.getCtnNum());
		exchangeInfo.setPledgedCustomerName(pledgedInfo.getStockName());
		exchangeInfo.setPledgedCustomerId(Integer.valueOf(pledgedInfo.getStockIn()));
		exchangeInfo.setPledgedSku(pledgedInfo.getSkuId());
		exchangeInfo.setPledgedItemClass(Integer.valueOf(bsp.getClassType()));
		exchangeInfo.setPledgedPName(pledgedInfo.getCargoName());
		exchangeInfo.setPledgedEnterState(pledgedInfo.getEnterState());
		exchangeInfo.setPledgedWareHouse(pledgedInfo.getWarehouse());
		exchangeInfo.setPledgedWareHouseId(pledgedInfo.getWarehouseId());

		// 新增字段
//		String strSourceTrendId = vo.getSourceTrendId();
//		String strRelatedTrendId = vo.getRelatedTrendId();
//		String strTrendId = vo.getTrendId();
		exchangeInfo.setSourceTrendId(vo.getSourceTrendId());
		exchangeInfo.setRelatedTrendId(vo.getRelatedTrendId());
		exchangeInfo.setTrendId(vo.getTrendId());

		// 存入数据库
		Date date = new Date();

		exchangeInfo.setCreateDate(date);
		exchangeInfo.setStatus("0"); // 换货生效状态 0/未生效

		exchangeInfoService.save(exchangeInfo);

		return ResponseVo.ok("已接收换货申请,等待确认中");
	}
}

	/**
	 * 查询全部换货记录
	 */
//
//	@RequestMapping(value = "exchangeList", method = RequestMethod.GET)
//	@ResponseBody
//	public List<Map<String, Object>> getExchangeList() {
//		return exchangeInfoService.getExchangeList();
//	}
//
//	/**
//	 * 跳转列表页面
//	 */
//	@RequestMapping(value = "exchangePage", method = RequestMethod.GET)
//	public String returnExchangePage() {
//		return "wms/pledge/exchangeInfo";
//	}

//	/**
//	 * 确认按钮操作
//	 */
//	@RequestMapping(value = "confirm", method = RequestMethod.POST)
//	@ResponseBody
//	@Transactional
//	public String confirm(@RequestBody Map<String, String> params) {
//
//		String id = params.get("ID");// 换货表id
//		String accountId = params.get("ACCOUNT_ID");
//		// 新押品信息
//		String newPledgeId = params.get("NEW_PLEDGE_ID");// 新押品质押监管主键
//		String pledgeNumber = params.get("PLEDGE_NUMBER");// 新押品质押监管数量
//		String pledgeWeight = params.get("PLEDGE_WEIGHT");// 新押品质押监管重量
//		String pledgeStatus = params.get("PLEDGE_STATUS");// 新押品质押监管指令
//		// 原押品信息
//		String originalPledgeId = params.get("ORIGINAL_PLEDGE_ID");// 原押品在库主键
//		String relieveNumber = params.get("RELIEVE_NUMBER");// 解除后仍质押数量
//		Integer relieveNumber1 = Integer.parseInt(relieveNumber);
//		String relieveWeight = params.get("RELIEVE_WEIGHT");// 解除后仍质押重量
//		Integer relieveWeight1 = Integer.parseInt(relieveWeight);
//		String relieveStatus = params.get("RELIEVE_STATUS");// 解除质押指令
//		// 新增字段
//		String sourceTrendId = params.get("SOURCE_TREND_ID");// 解除质押唯一标识
//		String relatedId = params.get("RELATED_ID");// 解除质押时,原质押唯一标识
//		String trendId = params.get("TREND_ID");// 新质押唯一标识
//		
//		if(StringUtils.isEmpty(relatedId)) {
//			exchangeService.getexchangeInfo(id);
//			return "relatedTrendId不存在,无法进行换货";
//		}
//		
//		if(StringUtils.isEmpty(trendId)) {
//			exchangeService.getexchangeInfo(id);
//			return "trendId不存在,无法进行换货";
//		}
//
//		List<ApiPledge> apiPledgeInfoList = exchangeInfoService.getPledgeInfo(originalPledgeId, relatedId);
//
//		Integer newPledgeNumber = null;
//		Double newPledgeWeight = null;
//		PledgeRequestVo requestVo = new PledgeRequestVo();
//		if (apiPledgeInfoList.size() > 0) {
//			ApiPledge apiPledge = apiPledgeInfoList.get(0);
//			
//			Integer state = apiPledge.getState();
//			Integer itemClass = apiPledge.getItemClass();
//			Integer pledgeNumber1 = apiPledge.getPledgeNumber();// 原质押数量
//			Double pledgeWeight1 = apiPledge.getPledgeWeight();// 原质押重量
//			if (state != 1) {
//				exchangeService.getexchangeInfo(id);
//				return "货品不是静态质押状态";
//			}
//			// 如果传过来的质押数量小于原质押数量
//			if (pledgeNumber1 >= relieveNumber1) {
//				newPledgeNumber = pledgeNumber1 - relieveNumber1;
//			}else {
//				exchangeService.getexchangeInfo(id);
//				return "换货失败";
//			}
//			if (pledgeWeight1 >= relieveWeight1) {
//				newPledgeWeight = pledgeWeight1 - relieveWeight1;
//			}else {
//				exchangeService.getexchangeInfo(id);
//				return "换货失败";
//			}
//
//			requestVo.setAccountId(accountId);
//			requestVo.setId(originalPledgeId);
//			requestVo.setPledgeNumber(newPledgeNumber);
//			requestVo.setPledgeWeight(newPledgeWeight);
//			requestVo.setItemClass(itemClass);
//			requestVo.setTrendId(sourceTrendId);
//			requestVo.setRelatedTrendId(relatedId);
//			// 静态解押
//			ResponseVo rv1 = apiPledgeService.staticRelease(requestVo);
//			if ((boolean) rv1.get("success")) {
//				ApiPledge api = new ApiPledge();
//				api.setAccountId(requestVo.getAccountId());
//				api.setTrayInfoId(requestVo.getId());
//				api.setPledgeNumber(requestVo.getPledgeNumber());
//				api.setPledgeWeight(requestVo.getPledgeWeight());
//				api.setState(0);
//				api.setCreateDate(new Date());
//				api.setTrendId(sourceTrendId);
//				api.setRelatedTrendId(relatedId);
//				api.setItemClass(itemClass);
//				api.setConfirmStatus(1);
//				
//				apiPledgeService.save(api);
//			} else {
//				exchangeService.getexchangeInfo(id);
//				return "静态解押失败";
//			}
//
//			requestVo.setAccountId(accountId);
//			requestVo.setId(newPledgeId);
//			requestVo.setPledgeNumber(Integer.parseInt(pledgeNumber));
//			requestVo.setPledgeWeight(Double.parseDouble(pledgeWeight));
//			requestVo.setItemClass(itemClass);
//			requestVo.setTrendId(trendId);
//			// 静态质押
//			ResponseVo rv2 = apiPledgeService.staticSve(requestVo);
//			if ((boolean) rv2.get("success")) {
//				ApiPledge api = new ApiPledge();
//				api.setAccountId(requestVo.getAccountId());
//				api.setTrayInfoId(Integer.valueOf(requestVo.getId()));
//				api.setPledgeNumber(requestVo.getPledgeNumber());
//				api.setPledgeWeight(requestVo.getPledgeWeight());
//				api.setState(1);
//				api.setCreateDate(new Date());
//				api.setTrendId(trendId);
//				api.setItemClass(itemClass);
//				api.setConfirmStatus(1);
//				
//				apiPledgeService.save(api);
//			} else {
//				exchangeService.getexchangeInfo(id);
//				return "静态质押失败";
//			}
//			
//		}else {
//			exchangeService.getexchangeInfo(id);
//			return "换货失败";
//		}
//		
//		//换货表状态改为生效
//		Integer status = exchangeInfoService.updStatus(1, id);
//		String result  = "";
//		if(status == 1) {
//			result = "success";
//		}else {
//			result = "fail";
//		}
//		//调用同步接口
//		exchangeService.getexchangeInfo(id);
//		return result;
//	}
//	
//}
