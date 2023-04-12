package com.haiersoft.ccli.cost.web;

import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.dao.PlatformWorkTicketDao;
import com.haiersoft.ccli.cost.entity.BisEnterSteveDoring;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import com.haiersoft.ccli.cost.service.EnterStevedoringService;
import com.haiersoft.ccli.cost.service.PlatformWorkTicketService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("cost/enterNotPaid")
public class EnterNotPaidController extends BaseController {

	@Autowired
	private PlatformWorkTicketService service;
	//入库装卸
	@Autowired
	private EnterStevedoringService enterStevedoringService;
	//费用
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;
	//入库没缴费
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String enterNotPaid(HttpServletRequest request) {
		return "cost/platformWorkTicket/enterNotPaidList";
	}

	@RequestMapping(value = "page")
	@ResponseBody
	public Map<String, Object> page(HttpServletRequest request) {

		Page<PlatformWorkTicket> pageData = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		String orderBy=" ORDER BY t1.INOUT_BOUND_FLAG ASC,\n" +
				" t1.CREATED_TIME DESC";
		pageData = service.page(pageData,filters,PlatformWorkTicketDao.QueryType.ENTER_STEVEDORING_NO_FEE_PAID,orderBy);
		return getEasyUIData(pageData);
	}

	/*
	 * 入库装卸单添加跳转
	 */
	@RequestMapping(value="add/{id}",method = RequestMethod.GET)
	public String add(Model model,@PathVariable("id") String id){
		PlatformWorkTicket platformWorkTicket = service.get(id);
		model.addAttribute("action", "add");
		model.addAttribute("platformWorkTicket",platformWorkTicket);
		return "cost/platformWorkTicket/enterStevedoringAdd" ;
	}

	/*
	 * 入库装卸单添加
	 */
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid BisEnterSteveDoring bisEnterSteveDoring, Model model) {
		// 2021 年 4月 增加 检查重量 等于 货物净重 乘以 重量系数
		Double netWeight = bisEnterSteveDoring.getNetWeight() * bisEnterSteveDoring.getNumPlus();
		//后台校验 四个数量是否小于（ ASN数量 - 已添加的装卸单数量）
		String asn = bisEnterSteveDoring.getAsnId();
		List<BisEnterSteveDoring> enterList = enterStevedoringService.getSteveByAsn(asn);
		//定义剩余未操作重量
		Double sortingNum = 0.00;
		Double manNum = 0.00;
		Double wrapNum = 0.00;
		Double packNum = 0.00;
		//遍历出已操作重量和
		if(!enterList.isEmpty()){
			int size = enterList.size();
			BisEnterSteveDoring enterSteveDoring = null;
			for(int i=1;i<size;i++){
				enterSteveDoring = new BisEnterSteveDoring();
				enterSteveDoring = enterList.get(i);
				sortingNum += enterSteveDoring.getSortingNum();
				manNum += enterSteveDoring.getManNum();
				wrapNum += enterSteveDoring.getWrapNum();
				packNum += enterSteveDoring.getPackNum();
			}
		}//end if
		sortingNum = netWeight - sortingNum;
		manNum     = netWeight - manNum;
		wrapNum    = netWeight - wrapNum;
		packNum    = netWeight - packNum;
		if(sortingNum < bisEnterSteveDoring.getSortingNum()){
			return "sorting";
		}
		if(manNum < bisEnterSteveDoring.getManNum()){
			return "man";
		}
		if(wrapNum < bisEnterSteveDoring.getWrapNum()){
			return "wrap";
		}
		if(packNum < bisEnterSteveDoring.getPackNum()){
			return "pack";
		}
		//droMR droStockInId     droEnterStockTime droStockInName
		//状态默认为未完成
		if(bisEnterSteveDoring.getIfAllMan() == null ){
			bisEnterSteveDoring.setIfAllMan(0);
		}
		bisEnterSteveDoring.setIfOk(0);
		User user= UserUtil.getCurrentUser();
		bisEnterSteveDoring.setCreateUser(user.getName());
		bisEnterSteveDoring.setCreateTime(new Date());
		enterStevedoringService.save(bisEnterSteveDoring);

		//生成费用
		enterStevedoringService.addStandBook(asn);
		return "success";
	}


}
