package com.haiersoft.ccli.api.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.api.aspect.ApiPledgedCheck;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.PledgeRequestVo;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.ApiPledgeService;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.TrayInfoService;

@Controller
public class ApiPledgeController {
	@Autowired
	private ApiPledgeService apiPledgeService;

	@Autowired
	private TrayInfoService trayInfoService;
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private ProductClassService productClassService;

	@Autowired
	private SkuInfoService skuInfoService;
	
	// 静态质押/解压
	//@ApiPledgedCheck
	@RequestMapping("/api/users/staticSave")
	@ResponseBody
	public ResponseVo staticSave(@RequestBody PledgeRequestVo requestVo) {
		ResponseVo rv = new ResponseVo();
		if (StringUtils.isEmpty(requestVo.getId())) {
			rv = ResponseVo.error("缺少在库主键");
			return rv;
		}
		TrayInfo trayInfo = trayInfoService.get(Integer.valueOf(requestVo.getId().trim()));
		if (null == trayInfo) {
			return ResponseVo.error("在库主键id不存在");
		}
		if (StringUtils.isEmpty(requestVo.getAccountId())) {

			return ResponseVo.error("缺少账户ID");
		}

		if (requestVo.getPledgeNumber() == null) {

			return ResponseVo.error("请传质押数量");
		}

		if (requestVo.getPledgeWeight() == null) {

			return ResponseVo.error("请传质押重量");
		}
		// 件数重量匹配交验
		if (requestVo.getPledgeWeight() != requestVo.getPledgeNumber() * trayInfo.getGrossSingle()) {
			return ResponseVo.error("件数与重量不匹配，请确认");
		}
		if (StringUtils.isEmpty(requestVo.getOrder())) {

			return ResponseVo.error("请传指令");
		}
		if (StringUtils.isEmpty(requestVo.getTrendId())) {

			return ResponseVo.error("请传质押/解除质押唯一标识");
		}

		List<ApiPledge> list = apiPledgeService.checkTrendId(requestVo.getTrendId());
		if (list.size() > 0) {

			return ResponseVo.error("trendId已存在");
		}
		if ("JCZYJG".equals(requestVo.getOrder())) {
			if (StringUtils.isEmpty(requestVo.getRelatedTrendId())) {
				return ResponseVo.error("请传原质押唯一标识");
			}
		}

		// 质押 /解押 操作
		if ("ZYJG".equals(requestVo.getOrder().toUpperCase())) {// 质押
			
			return this.staticPledge(requestVo);

		} else if ("JCZYJG".equals(requestVo.getOrder().toUpperCase())) {// 解压

			
			return this.staticRelease(requestVo);

		}
		return rv;

	}
	
	//静态质押
	private ResponseVo staticPledge(PledgeRequestVo requestVo) {
		TrayInfo info = trayInfoService.get(Integer.valueOf(requestVo.getId().trim()));
		// 判断当前处于什么状态 如果不是0正常 3静态质押的不可以质押
		if (null == info) {
			return ResponseVo.error("质押货物不存在！");			
		}
		if (!("0".equals(info.getIfTransfer())) && !("3".equals(info.getIfTransfer()) )) {
			return ResponseVo.error("该货物状态无法质押，请确认！");			
		}
		
		// 检查质押数量或重量是否超出库存数量（单条检查）
		if (requestVo.getPledgeNumber()>info.getNowPiece()) {
			return ResponseVo.error("质押数量超过本条库存的数量！");
		}
		
		// 检查质押数量或重量是否超出已静态质押数量（多条检查）
		//HashMap<String, Object> pledgedMap = apiPledgeService.countTotalPledgedByTrayInfoId(Integer.valueOf(requestVo.getId()));
		HashMap<String, Object> pledgedMap = apiPledgeService.sumStaticPledgedNum(Integer.valueOf(requestVo.getId()));
		
		Integer pledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
		if (requestVo.getPledgeNumber()>(info.getNowPiece()-pledgedNum)) {
			return ResponseVo.error("质押数量超过本条库存可质押的数量！");
		}
		BaseSkuBaseInfo bs = skuInfoService.get(info.getSkuId());
		
		ApiPledge api = new ApiPledge();
		api.setAccountId(requestVo.getAccountId());
		api.setTrayInfoId(Integer.valueOf(requestVo.getId()));
		api.setPledgeNumber(requestVo.getPledgeNumber());
		api.setPledgeWeight(requestVo.getPledgeWeight());
		api.setState(1);
		api.setCreateDate(new Date());
		api.setTrendId(requestVo.getTrendId());
		api.setRelatedTrendId(requestVo.getTrendId());
		api.setBillNum(info.getBillNum());
		api.setCtnNum(info.getCtnNum());
		api.setCustomerName(info.getStockName());
		api.setCustomerId(Integer.valueOf(info.getStockIn()));
		api.setSku(info.getSkuId());
		api.setItemClass(Integer.valueOf(bs.getClassType()));
		api.setpName(info.getCargoName());
		api.setEnterState(info.getEnterState());
		api.setWareHouse(info.getWarehouse());
		api.setWareHouseId(info.getWarehouseId());
		
		// 检查质押数量或重量是否超出可质押数量（总量）
		if (!apiPledgeService.pledgeCountCheck(api)) {
			return ResponseVo.error("质押数量或质押重量超出可质押数量！");
		}
		apiPledgeService.save(api);	
		return ResponseVo.pledgeOk();
		
	}
	//静态解押
	private ResponseVo staticRelease(PledgeRequestVo requestVo) {
		// 根据原质押唯一标识查询货物质押情况
		List<ApiPledge> pledgedRecord = apiPledgeService.findPledgedRecordByTrendId(requestVo.getRelatedTrendId());
		if(0 ==pledgedRecord.size() || null == pledgedRecord ) {
			return ResponseVo.error("找不到原质押TrendId！");
		}
		// 解押数量不能超出该库存质押数量
		//HashMap<String, Object> pledgedMap= apiPledgeService.countStaticPledgedNumByTrendId(requestVo.getRelatedTrendId());
//		HashMap<String, Object> pledgedMap= apiPledgeService.countTotalPledgedByTrayInfoId(Integer.valueOf(requestVo.getId()));
//		Integer pledgedNum = Integer.valueOf(pledgedMap.get("sumNum").toString());
//
//		if (requestVo.getPledgeNumber() > pledgedNum ) {
//			return ResponseVo.error("本次解押数量大于该库存总质押数量！");
//		} 
		ApiPledge forRelease = pledgedRecord.get(0);
		Integer getTrayInfoId = forRelease.getTrayInfoId();
		Integer voTrayInfoId = Integer.valueOf(requestVo.getId());
		if(!getTrayInfoId.equals(voTrayInfoId)) {
			return ResponseVo.error("在库明细的主键与原质押单中不一致！");
		}
		
		
		
		//解押数量不能超出trend_id的数量
		HashMap<String, Object> pledgedRelatedMap= apiPledgeService.countRelatedTrendIdByRelatedTrendId(requestVo.getRelatedTrendId());
		Integer pledgedRelatedNum = Integer.valueOf(pledgedRelatedMap.get("sumNum").toString());
		if (requestVo.getPledgeNumber() > pledgedRelatedNum ) {
			return ResponseVo.error("本次解押数量超出限制！");
		} 
			ApiPledge api = new ApiPledge();
			api.setAccountId(requestVo.getAccountId());
			api.setTrayInfoId(Integer.valueOf(requestVo.getId()));
			api.setPledgeNumber(requestVo.getPledgeNumber());
			api.setPledgeWeight(requestVo.getPledgeWeight());
			api.setState(0);
			api.setCreateDate(new Date());
			api.setTrendId(requestVo.getTrendId());
			api.setRelatedTrendId(requestVo.getRelatedTrendId());
			
			//根据原质押记录填充信息
			//ApiPledge forRelease = apiPledgeService.find("trendId", requestVo.getRelatedTrendId());
			
			api.setBillNum(forRelease.getBillNum());
			api.setCtnNum(forRelease.getCtnNum());
			api.setCustomerName(forRelease.getCustomerName());
			api.setCustomerId(forRelease.getCustomerId());
			api.setSku(forRelease.getSku());
			api.setpName(forRelease.getpName());
			api.setEnterState(forRelease.getEnterState());
			api.setWareHouse(forRelease.getWareHouse());
			api.setWareHouseId(forRelease.getWareHouseId());
			api.setItemClass(forRelease.getItemClass());
			apiPledgeService.save(api);
			
			return ResponseVo.pledgeOk();
		
	}





	// 动态质押/解压
	//@ApiPledgedCheck
	@RequestMapping("/api/users/activeSave")
	@ResponseBody
	public ResponseVo activeSave(@RequestBody PledgeRequestVo requestVo) {
		ResponseVo rv = new ResponseVo();
		if (StringUtils.isEmpty(requestVo.getCustomerNumber())) {
			rv = ResponseVo.error("请传货主账号");
			return rv;
		}
		if (StringUtils.isEmpty(requestVo.getId())) {
			rv = ResponseVo.error("请传业务主键ID");
			return rv;
		}

		if (StringUtils.isEmpty(requestVo.getAccountId())) {
			rv = ResponseVo.error("缺少账户ID");
			return rv;
		}

		if (requestVo.getPledgeNumber() == null) {
			rv = ResponseVo.error("请传质押数量");
			return rv;
		}

		if (requestVo.getPledgeWeight() == null) {
			rv = ResponseVo.error("请传质押重量");
			return rv;
		}
		if (StringUtils.isEmpty(requestVo.getOrder())) {
			rv = ResponseVo.error("请传指令");
			return rv;
		}
		if (StringUtils.isEmpty(requestVo.getTrendId())) {
			rv = ResponseVo.error("请传质押/解除质押唯一标识trendId");
			return rv;
		}
		List<ApiPledge> list = apiPledgeService.checkTrendIdActive(requestVo.getTrendId());
		if (list.size() > 0) {
			rv = ResponseVo.error("唯一质押ID已存在");
			return rv;
		}

		if ("JCZYJG".equals(requestVo.getOrder())) {
			if (StringUtils.isEmpty(requestVo.getRelatedTrendId())) {
				rv = ResponseVo.error("请传原质押唯一标识");
				return rv;
			}
		}
		if (requestVo.getItemClass() == null) {
			rv = ResponseVo.error("请传货品小类");
			return rv;
		}

		if (requestVo.getCustomerNumber() == null) {
			rv = ResponseVo.error("请传货主账号");
			return rv;
		}

		BaseClientInfo client = clientService.find("clientCode", requestVo.getCustomerNumber());
		if (null == client) {
			return ResponseVo.error("货主账号不存在");
		}
		
		Integer baseProductClassId = productClassService.getProductClassId(requestVo.getItemClass(), false);
		if(baseProductClassId==0) {//说明货种小类Id不存在
			return ResponseVo.error("该货种小类不存在！！");
		}
		BaseProductClass pc = productClassService.get(baseProductClassId);
		
		// 先判断是质押还是解压
		if ("ZYJG".equals(requestVo.getOrder().toUpperCase())) {// 质押

			//BaseSkuBaseInfo bs = skuInfoService.find("classType", requestVo.getItemClass().toString());
			ApiPledge api = new ApiPledge();
			api.setAccountId(requestVo.getAccountId());
			api.setCustomerCode(requestVo.getCustomerNumber());
			api.setCustomerId(client.getIds());
			api.setCustomerName(client.getClientName());
			api.setpName(pc.getpName());
			api.setItemClass(pc.getId());
			api.setItemClassName(requestVo.getItemClass());
			api.setPledgeNumber(requestVo.getPledgeNumber());
			api.setPledgeWeight(requestVo.getPledgeWeight());
			api.setState(2); // 2 动态质押中
			api.setTrendId(requestVo.getTrendId());
			api.setRelatedTrendId(requestVo.getTrendId());
			api.setCreateDate(new Date());
			api.setBusinessId(requestVo.getId());
		//	api.setSku(bs.getSkuId());
			// 检查质押数量或重量是否超出
			if (!apiPledgeService.pledgeCountCheck(api)) {
				return ResponseVo.error("质押数量或质押重量超出可质押数量！");
			}
			HashMap<String, String> map = apiPledgeService.getSKUandWarehouse(api);
			api.setSku(map.get("SKU_ID"));
			api.setWareHouse(map.get("WAREHOUSE"));
			api.setWareHouseId(map.get("WAREHOUSE_ID"));
			apiPledgeService.save(api);
			return ResponseVo.pledgeOk();
			
		} else if ("JCZYJG".equals(requestVo.getOrder().toUpperCase())) {// 解压

			List<ApiPledge> records = apiPledgeService.findOrderedListByRelatedTrendId(requestVo.getRelatedTrendId());
			if (null == records  || records.size() == 0 ) {
				return ResponseVo.error("质押唯一标识为：" + requestVo.getRelatedTrendId() + "的质押记录未找到");
			}

			Integer pledgeNum =requestVo.getPledgeNumber();// 本次质押数量
			Double pledgeWeight = requestVo.getPledgeWeight();// 本次质押重量
			// 判断当前是否进行过解押
			
			
			// 判断解押数量或重量是否超出
			if (pledgeNum > records.get(0).getPledgeNumber()) {
				return ResponseVo.error("解除质押的重量或数量超出当前质押重量或数量！");
			}
			ApiPledge api = new ApiPledge();
			api.setAccountId(requestVo.getAccountId());
			api.setCustomerCode(requestVo.getCustomerNumber());
			api.setCustomerId(client.getIds());
			api.setCustomerName(client.getClientName());
			api.setpName(pc.getpName());
			api.setItemClass(pc.getId());
			api.setItemClassName(requestVo.getItemClass());
			api.setPledgeNumber(requestVo.getPledgeNumber());
			api.setPledgeWeight(requestVo.getPledgeWeight());
			api.setState(3); // 3 动态解押
			api.setCreateDate(new Date());
			api.setTrendId(requestVo.getTrendId());
			api.setRelatedTrendId(requestVo.getRelatedTrendId());
			api.setBusinessId(requestVo.getId());
			
			ApiPledge pledgedRecord = apiPledgeService.find("trendId", requestVo.getRelatedTrendId());
			api.setSku(pledgedRecord.getSku());
			api.setWareHouse(pledgedRecord.getWareHouse());
			api.setWareHouseId(pledgedRecord.getWareHouseId());
			apiPledgeService.save(api);
			return ResponseVo.pledgeOk();
			// rv = apiPledgeService.activeRelease(requestVo);
		}
		return rv;
	}
}
