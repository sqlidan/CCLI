package com.haiersoft.ccli.api.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.haiersoft.ccli.api.service.CustomerQueryConfirmService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.api.entity.InStockInfo;
import com.haiersoft.ccli.api.entity.InStockInfoResponseVo;
import com.haiersoft.ccli.api.entity.InStockInfoSort;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.InStockInfoService;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;

/**
 * 在库信息、历史库存、动态质押查询
 * @author 
 *
 */
@Controller
@RequestMapping("api/users")
public class InStockInfoController {

	@Autowired
	private InStockInfoService inStockInfoService;
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private CustomerQueryConfirmService customerQueryConfirmService;
	
	/**
	 * 历史业务量查询
	 */
	//@ApiPledgedCheck
	@RequestMapping(value = "historyStockInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo historyStockInfo(@RequestBody Map<String,String> params) {
		Long start = System.currentTimeMillis();
		
		String accountId = params.get("accountId");
		if (StringUtils.isBlank(accountId)) {
			return ResponseVo.error("账户ID不允许为空");
		}

		String clientCode = params.get("customerNumber");
		String clientName = params.get("custName");
		if (StringUtils.isBlank(clientCode) && StringUtils.isBlank(clientName)) {
			return ResponseVo.error("货主账号和货主名称不能都为空");
		}
		//货主账号和货主名称不能都为空
		//货主名称为空时，按货主帐号查询client的id
		List<Integer> clientIds  = null;
		if(StringUtils.isEmpty(clientName)) {
			List<String> codeList = Arrays.asList(clientCode.split(",")); 
			clientIds=clientService.getClientIds("clientCode", codeList);
			if(codeList.size()!=clientIds.size()) {
				return ResponseVo.error("查不到的货主账号，请确认！");
			}
		}
		//货主名称不为空时，按货主名称查询client的id
		else {
			List<String> nameList = Arrays.asList(clientName.split(",")); 
			clientIds = clientService.getClientIds("clientName", nameList);
			if(nameList.size()!=clientIds.size()) {
				return ResponseVo.error("查不到的货主名称，请确认！");
			}
		}

		//2022-06 增加检查是否有申请查询权限
		clientIds = customerQueryConfirmService.checkCLients(clientIds);
		if(CollectionUtils.isEmpty(clientIds)){
			return ResponseVo.error("没有可查的货主，请确认！");
		}

		String strItemClass = params.get("itemClass");
		if (StringUtils.isEmpty(strItemClass)) {
			return ResponseVo.error("货种代码不允许为空");
		}
		List<String> classList = Arrays.asList(strItemClass.split(",")); 
		
		//入库时间段判断
		String strInstorageDate = params.get("instorageDate");
		String startDate = null;
		String endDate = null;
		if (!StringUtils.isEmpty(strInstorageDate) && strInstorageDate.length() != 16) {
			return ResponseVo.error("请按规定日期格式输入");
		}
		if (!StringUtils.isEmpty(strInstorageDate)){
			startDate = strInstorageDate.substring(0, 8);
			endDate= strInstorageDate.substring(8);
		}


		
//		//当接口数据中的clientName为空时,根据clientCode查询,判断client是否存在
//		if (StringUtils.isBlank(clientName)) {
//	        List<Object> getList = clientService.getClient("clientCode", clientCode);
//	        if (getList == null || getList.size() > 1 || getList.size()==0) {
//	            return ResponseVo.error("客户信息有误");
//	        } 
//	        BaseClientInfo client = (BaseClientInfo)getList.get(0);
//	        clientName = client.getClientName();
//		}
//		//当接口数据中的clientName不为空时,判断client是否存在
//		else {
//			List<Object> getList = clientService.getClient("clientName", clientName);
//	        if (getList == null || getList.size() > 1 || getList.size()==0) {
//	            return ResponseVo.error("客户信息有误");
//	        } 
//		}
		System.out.println("Start:---------"+ (start));
		List<?> list = inStockInfoService.getStockInPeriod(clientIds,classList, startDate, endDate);
		//List<?> list = null;
		System.out.println("End:---------"+ (System.currentTimeMillis()-start));
		return ResponseVo.ok(list);

	}
	
	/**
	 * 查询在库信息
	 */
	//@ApiPledgedCheck
	@RequestMapping(value = "inStockInfo", method = RequestMethod.POST)
	@ResponseBody
	public InStockInfoResponseVo sendInStockInfo(@RequestBody Map<String,String> params) {
		Long start = System.currentTimeMillis();
		String strAccountId = params.get("accountId"); // 账户ID 必填
		String clientCode = params.get("customerNumber"); // 货主账号
		String clientName = params.get("custName"); // 货主名称 货主帐号/货主名称都填时以货主名称进行查询

		String strItemClass = params.get("itemClass"); // 货种
		String strSeachtype = params.get("seachtype"); // 查询类型
		String strId = params.get("id");
	

		if (StringUtils.isBlank(strAccountId)) {
			return InStockInfoResponseVo.error("账户ID不允许为空");
		}

		if (StringUtils.isBlank(clientCode) && StringUtils.isBlank(clientName)) {
			return InStockInfoResponseVo.error("货主账号和货主名称不能都为空");
		}
		
		String pageNumStr = params.get("pageNum"); // 货种
		String pageSizeStr = params.get("pageSize"); // 查询类型
		if (StringUtils.isBlank(pageNumStr) || StringUtils.isBlank(pageSizeStr)) {
			return InStockInfoResponseVo.error("查询页和查询每页数量都不允许为空");
		}
		Integer pageNum = Integer.parseInt(params.get("pageNum"));//查询页
		Integer pageSize = Integer.parseInt(params.get("pageSize"));//查询每页数量
		
		InStockInfoSort inStockInfoSort = new InStockInfoSort();
		inStockInfoSort.setPageNum(pageNum);
		inStockInfoSort.setPageSize(pageSize);
		
		String sortField = params.get("sortField");//排序字段
		String sortOrder = params.get("sortOrder");//排序顺序（降序 升序）
		if(StringUtils.isNotBlank(sortField)) {
			if(!"tdh".equals(sortField)&&!"xh".equals(sortField)&&!"instorageDate".equals(sortField)&&!"location".equals(sortField)) {
				return InStockInfoResponseVo.error("排序字段不正确");
			}
			if("tdh".equals(sortField)) {
				inStockInfoSort.setSortField("bill_num");
			}else if("xh".equals(sortField)) {
				inStockInfoSort.setSortField("ctn_num");
			}else if("instorageDate".equals(sortField)) {
				inStockInfoSort.setSortField("enter_stock_time");
			}else if("location".equals(sortField)) {
				inStockInfoSort.setSortField("area_num");
			}
		
		}
	
		if(StringUtils.isNotBlank(sortOrder)) {
			if(!"asc".equals(sortOrder)&&!"desc".equals(sortOrder)) {
				return InStockInfoResponseVo.error("排序顺序不正确");
			}
			inStockInfoSort.setSortOrder(sortOrder);
		}

		//货主账号和货主名称不能都为空
		//货主名称为空时，按货主帐号查询client的id
		List<Integer> clientIds  = null;
		if(StringUtils.isEmpty(clientName)) {
			List<String> codeList = Arrays.asList(clientCode.split(",")); 
			clientIds=clientService.getClientIds("clientCode", codeList);
			if(codeList.size()!=clientIds.size()) {
				return InStockInfoResponseVo.error("查不到的货主账号，请确认！");
			}
		}
		//货主名称不为空时，按货主名称查询client的id
		else {
			List<String> nameList = Arrays.asList(clientName.split(",")); 
			clientIds = clientService.getClientIds("clientName", nameList);
			if(nameList.size()!=clientIds.size()) {
				return InStockInfoResponseVo.error("查不到的货主名称，请确认！");
			}
		}

		//2022-06 增加检查是否有申请查询权限
		clientIds = customerQueryConfirmService.checkCLients(clientIds);
		if(CollectionUtils.isEmpty(clientIds)){
			return InStockInfoResponseVo.error("没有可查的货主，请确认！");
		}

		List<String> itemClassList = null;
		if (StringUtils.isNotEmpty(strItemClass)) {
			itemClassList = Arrays.asList(strItemClass.split(","));
		}

		List<InStockInfo> staticInStockInfo = inStockInfoService.getInStockInfo(itemClassList, clientIds, strId,inStockInfoSort);
		Integer total = inStockInfoService.getTotal(itemClassList, clientIds, strId,inStockInfoSort);
		inStockInfoSort.setTotal(total);
		System.out.println("End:---------"+ (System.currentTimeMillis()-start));
		return InStockInfoResponseVo.ok(staticInStockInfo,inStockInfoSort);
	}
	
	/**
	 * 动态质押数据查询
	 */
	@RequestMapping(value = "dynamicPledge", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo dynamicPledgeQuery(@RequestBody Map<String, String> params) {
		String clientCode = params.get("customerNumber"); // 1个
		String strItemClass = params.get("itemClass"); //多个
		String strAccountId = params.get("accountId"); 
		
		if(StringUtils.isBlank(strAccountId)) {
			return ResponseVo.error("账户ID不允许为空");
		}
		if(StringUtils.isBlank(clientCode)) {
			return ResponseVo.error("货主账号不允许为空");
		}
		if(StringUtils.isBlank(strItemClass)) {
			return ResponseVo.error("货种小类代码不允许为空");
		}
		
		BaseClientInfo client = clientService.find("clientCode", clientCode);

		if (null == client) {
			return ResponseVo.error("客户信息未查到");
		}
		
		List<String> itemClassList = Arrays.asList(strItemClass.split(","));
		
		List<Map<String, Object>> dynamicPledge = inStockInfoService.dynamicQuery(client.getIds(), strItemClass);
		if(dynamicPledge == null || dynamicPledge.size() <= 0) {
			return ResponseVo.error("未查询到动态质押信息");
		}
		return ResponseVo.ok(dynamicPledge);
	}
}
