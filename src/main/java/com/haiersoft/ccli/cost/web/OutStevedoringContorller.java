package com.haiersoft.ccli.cost.web;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.cost.service.OutStevedoringService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.cost.entity.BisOutSteveDoring;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.TaskRemindService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
/**
 * OutStevedoringcontroller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/outstevedoring")
public class OutStevedoringContorller extends BaseController {
	
	@Autowired
	private OutStevedoringService outStevedoringService;//出库装卸
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//台账
	@Autowired
	private LoadingOrderService loadingOrderService;//出库订单
	@Autowired
	private LoadingInfoService loadingInfoService;//出库装车单
	@Autowired
	private TaskRemindService taskRemindService;//任务提醒模块
	
	
	/*跳转入库装卸列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/outStevedoring/outStevedoring";
	}
	
	/*跳转入库装卸总览页面PH*/
	@RequestMapping(value="outstevedoringList",method = RequestMethod.GET)
	public String getMenuList(){
		return "cost/outStevedoring/outStevedoringList";
	}
	
	/**
	 * 
	 * @author ph
	 * @Description: 出库装卸总览查询
	 * @date 2016年12月7日
	 * @return
	 * @throws
	 */
	@RequestMapping(value="jsonList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisOutSteveDoring> page = getPage(request);
//		page.setOrder(Page.DESC);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = outStevedoringService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author ph
	 * @Description: 出库装卸总览修改
	 * @date 2016年12月7日 
	 * @param model
	 * @throws
	 */
	@RequestMapping(value="update/{loadingNum}", method = RequestMethod.GET)
	public String update(Model model, @PathVariable("loadingNum") String loadingNum) {
//		BisEnterSteveDoring bisEnterSteveDoring =  enterStevedoringService.get(Integer.valueOf(asnId));
//		model.addAttribute("bisEnterSteveDoring", bisEnterSteveDoring);
		model.addAttribute("loadingNum", loadingNum);
		model.addAttribute("action", "update");
		return "cost/outStevedoring/outStevedoring";
	}
	
	/*
	 * 查询出库装车单信息
	 * update by slh 20160627 追加 收货方id，
	 */
	@RequestMapping(value="cxloadingnum/{loadingNum}",method = RequestMethod.POST)
	@ResponseBody
	public List<String> cxasn(@PathVariable("loadingNum") String loadingNum){
		//根据装车号获取已装车的出库装车信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loadingTruckNum", loadingNum);
		params.put("loadingState", "2");
		List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByNum(params);
		List<String> result = new ArrayList<String>();
		if(!loadingList.isEmpty()){
			//从出库订单表中获取装车状态
			String orderNum = loadingList.get(0).getLoadingPlanNum();
			String receiverName = loadingOrderService.get(orderNum).getReceiverName();
			String orderState = loadingOrderService.get(orderNum).getOrderState();
			String clientId =loadingOrderService.get(orderNum).getStockIn(); //追加 收获方id，
			String state = "";
			switch(orderState){ 
				case "1" : state = "创建"; break;
				case "2" : state = "分配出库中";break; 
				case "3" : state = "拣货中"; break;
				case "4" : state = "已出库"; break;
			}
			Integer size = loadingList.size();
			Integer piece = 0; 
			Double netWeight = 0.00;
			Date sj = null;
			for(int i = 0;i<size;i++){
				piece += loadingList.get(i).getPiece();
				netWeight += loadingList.get(i).getNetWeight();
				if(i == 0){
					sj = loadingList.get(i).getLoadingTime();
				}else{
					if(sj.before(loadingList.get(i).getLoadingTime())){
						sj = loadingList.get(i).getLoadingTime();
					}
				}
			}
			result.add(state);
			result.add(loadingNum);
			result.add(receiverName);
			result.add(String.valueOf(sj).substring(0, 19));
			result.add(String.valueOf(piece));
			result.add(String.valueOf(netWeight));
			result.add(clientId);
//			return state+","+loadingNum+","+receiverName+","+String.valueOf(sj).substring(0, 19)+","+String.valueOf(piece)+","+String.valueOf(netWeight)+","+clientId;
			return result;
		}else{
			return result;
		}
	} 
	
	/*
	 * 出库装车明细
	 * @param asn
	 */
	@RequestMapping(value="json/{loadingNum}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> json(HttpServletRequest request,@PathVariable("loadingNum") String loadingNum){
		List<Map<String,Object>> loadingInfoList = outStevedoringService.getLoadingInfoByNum(loadingNum);
		return loadingInfoList;
	}
		
	/*
	 * 装卸数量统计 展示
	 * @param asn
	 */
	@RequestMapping(value="zxjson/{loadingNum}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> zxjson(HttpServletRequest request,@PathVariable("loadingNum") String loadingNum){
		List<Map<String,Object>> loadingInfoList = outStevedoringService.getOutStevedoring(loadingNum);
		return loadingInfoList;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 出库装卸单删除
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		for(int i = 0;i<ids.size();i++){
			outStevedoringService.delete((ids.get(i)));
		}
		return "success";
	}
	
	
	/*
	 * 出库装卸单添加跳转
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("action", "add");
		return "cost/outStevedoring/outStevedoringAdd" ;
	}
	
	/*
	 * 出库装卸单添加
	 */
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid BisOutSteveDoring bisOutSteveDoring,Model model) {
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
		User user=UserUtil.getCurrentUser();
		bisOutSteveDoring.setCreateUser(user.getName());
		bisOutSteveDoring.setCreateTime(new Date());
		outStevedoringService.save(bisOutSteveDoring);
		
		
		return "success";
	}
	
	
	/**
	 * 判断是否有相对应的费用方案
	 * 
	 */
	@RequestMapping(value = "judgefee", method = RequestMethod.GET)
	@ResponseBody
	public String judgefee(HttpServletRequest request,Double sortingNum,Double manNum,Double wrapNum,Double packNum,String feeId){
		Map<String,Object> params  = new HashMap<String,Object>();
		params.put("schemeNum", feeId);
		List<ExpenseSchemeInfo> expenseList = null;
		StringBuffer sb = new StringBuffer();
		int sign = 0;
		if(sortingNum>0.00){
			params.put("feeType", "5");
			expenseList=expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("分拣  ");
			}
		}
		if(manNum>0.00){
			params.put("feeType", "6");
			expenseList=expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("人工  ");
			}
		}
		if(wrapNum>0.00){
			params.put("feeType", "7");
			expenseList=expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("缠膜  ");
			}
		}
		if(packNum>0.00){
			params.put("feeType", "8");
			expenseList=expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("打包  ");
			}
		}
		if(sign == 1){
			String str = sb.toString() + "的费用方案缺失 ！是否继续添加？";
			return str;
		}else{
			return "success";
		}
	}
	
	
	/*
	 * 添加台账信息
	 * update by  slh 20160627 回写台账信息
	 */
	@RequestMapping(value="addstandbook/{loadingNum}",method = RequestMethod.GET)
	@ResponseBody
	public String addStandBook(@PathVariable("loadingNum") String loadingNum){
		return outStevedoringService.addStandBook(loadingNum);
	}
	
	
	
	/*
	 * 取消完成
	 * add by mzy 20161205
	 */
	@RequestMapping(value="concel/{loadingNum}",method = RequestMethod.GET)
	@ResponseBody
	public String concelStandBook(@PathVariable("loadingNum") String loadingNum){
		List<BisOutSteveDoring>  outList =outStevedoringService.getSteveByLoading(loadingNum);	
		for(int i=0;outList!=null&&i<outList.size();i++){
			BisOutSteveDoring bisOutSteveDoring=outList.get(i);
			if(null!=bisOutSteveDoring.getDroStanindBookIds()&&!"".equals(bisOutSteveDoring.getDroStanindBookIds())){
				String standingBookis=bisOutSteveDoring.getDroStanindBookIds().toString();
				if (standingBookis.trim().length()>0) {
					List<String> ids=Arrays.asList(standingBookis.split(","));          //分隔取出台账ID
					standingBookService.deleteStandingBookBatch(ids);                   //删除对应台账
				}
			}
		}
		//更新取消完成 状态 
		outStevedoringService.updateState2(loadingNum); 
		return "success";
	}
	

	/**
	 * ASN授权
	 * 
	 */
	@RequestMapping(value = "sqLoading", method = RequestMethod.GET)
	@ResponseBody
	public String sqLoading(HttpServletRequest request,String loadingNum){
		List<BisLoadingInfo> objList=loadingInfoService.getLoadingByNum(loadingNum);
		if(!objList.isEmpty()){
			for(BisLoadingInfo obj:objList){
				obj.setIfAllow(1);
				loadingInfoService.update(obj);
			}
			taskRemindService.steveTask(objList.get(0).getOperator(), loadingNum, "2");
			return "success";
		}else{
			return "false";
		}
	}

}
