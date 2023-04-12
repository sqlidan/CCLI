package com.haiersoft.ccli.cost.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.dao.PlatformWorkTicketDao;
import com.haiersoft.ccli.cost.entity.BisOutSteveDoring;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import com.haiersoft.ccli.cost.service.OutStevedoringService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("cost/outNotPaid")
public class OutNotPaidController extends BaseController {

	@Autowired
	private PlatformWorkTicketService service;
	//出库装卸
	@Autowired
	private OutStevedoringService outStevedoringService;

	//出库没缴费
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String outNotPaid(HttpServletRequest request) {
		return "cost/platformWorkTicket/outNotPaidList";
	}

	@RequestMapping(value = "page")
	@ResponseBody
	public Map<String, Object> page(HttpServletRequest request) {

		Page<PlatformWorkTicket> pageData = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		String orderBy=" ORDER BY t1.INOUT_BOUND_FLAG ASC,\n" +
				" t1.CREATED_TIME DESC";
		pageData = service.page(pageData,filters,PlatformWorkTicketDao.QueryType.OUT_STEVEDORING_NO_FEE_PAID,orderBy);
		return getEasyUIData(pageData);
	}


	/*
	 * 出库装卸单添加跳转
	 */
	@RequestMapping(value="add/{id}",method = RequestMethod.GET)
	public String add(Model model,@PathVariable("id") String id){
		PlatformWorkTicket platformWorkTicket = service.get(id);
		model.addAttribute("action", "add");
		model.addAttribute("platformWorkTicket",platformWorkTicket);
		return "cost/platformWorkTicket/outStevedoringAdd" ;
	}

	/*
	 * 出库装卸单添加
	 */
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid BisOutSteveDoring bisOutSteveDoring, Model model) {
		Double netWeight = bisOutSteveDoring.getNetWeight()* bisOutSteveDoring.getNumPlus();
		//后台校验 四个数量是否小于（ 装车订单数量 - 已添加的装卸单数量）
		String loadingNum = bisOutSteveDoring.getLoadingNum();
		List<BisOutSteveDoring> outList = outStevedoringService.getSteveByLoading(loadingNum);
		//定义剩余未操作重量
		Double sortingNum = 0.00;
		Double manNum = 0.00;
		Double wrapNum = 0.00;
		Double packNum = 0.00;
		//遍历出已操作重量和
		if(!outList.isEmpty()){
			int size = outList.size();
			BisOutSteveDoring outSteveDoring = null;
			for(int i=1;i<size;i++){
				outSteveDoring = new BisOutSteveDoring();
				outSteveDoring = outList.get(i);
				sortingNum += outSteveDoring.getSortingNum();
				manNum += outSteveDoring.getManNum();
				wrapNum += outSteveDoring.getWrapNum();
				packNum += outSteveDoring.getPackNum();
			}
		}//end if
		sortingNum = netWeight - sortingNum;
		manNum     = netWeight - manNum;
		wrapNum    = netWeight - wrapNum;
		packNum    = netWeight - packNum;
		if(sortingNum < bisOutSteveDoring.getSortingNum()){
			return "sorting";
		}
		if(manNum < bisOutSteveDoring.getManNum()){
			return "man";
		}
		if(wrapNum < bisOutSteveDoring.getWrapNum()){
			return "wrap";
		}
		if(packNum < bisOutSteveDoring.getPackNum()){
			return "pack";
		}

		//状态默认为未完成
		if(bisOutSteveDoring.getIfAllMan() == null ){
			bisOutSteveDoring.setIfAllMan(0);
		}
		bisOutSteveDoring.setIfOk(0);
		User user= UserUtil.getCurrentUser();
		bisOutSteveDoring.setCreateUser(user.getName());
		bisOutSteveDoring.setCreateTime(new Date());
		outStevedoringService.save(bisOutSteveDoring);

		//生成费用
		outStevedoringService.addStandBook(loadingNum);
		return "success";
	}

}
