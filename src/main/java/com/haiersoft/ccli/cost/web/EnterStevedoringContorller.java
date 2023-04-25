package com.haiersoft.ccli.cost.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.TaskRemindService;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.cost.entity.BisEnterSteveDoring;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.cost.service.EnterStevedoringService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
/**
 * EnterStevedoringcontroller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/enterstevedoring")
public class EnterStevedoringContorller extends BaseController {
	
	@Autowired
	private ASNService asnService;
	@Autowired
	private ClientService clientService;
	//@Autowired
	//private ASNInfoService asnInfoService;//ans明细
	@Autowired
	private TrayInfoService trayInfoService;//ans明细
	@Autowired
	private EnterStevedoringService enterStevedoringService;//入库装卸
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//费用
	@Autowired
	private TaxRateService taxRateService;//汇率	
	@Autowired
	private EnterStevedoringService enterStevedroingService;//入库装卸队统计
	@Autowired
	private TaskRemindService taskRemindService;//任务提醒模块
	
	/*跳转入库装卸列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/enterStevedoring/enterStevedoring";
	}
	
	/*跳转入库装卸总览页面PH*/
	@RequestMapping(value="enterstevedoringList",method = RequestMethod.GET)
	public String getMenuList(){
		return "cost/enterStevedoring/enterstevedoringList";
	}
	
	/**
	 * 
	 * @author ph
	 * @Description: 入库装卸总览查询
	 * @date 2016年12月7日
	 * @return
	 * @throws
	 */
	@RequestMapping(value="jsonList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisEnterSteveDoring> page = getPage(request);
//		page.setOrder(Page.DESC);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = enterStevedoringService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author ph
	 * @Description: 入库装卸总览修改
	 * @date 2016年12月7日 
	 * @param model
	 * @throws
	 */
	@RequestMapping(value="update/{asnId}", method = RequestMethod.GET)
	public String update(Model model, @PathVariable("asnId") String asnId) {
//		BisEnterSteveDoring bisEnterSteveDoring =  enterStevedoringService.get(Integer.valueOf(asnId));
//		model.addAttribute("bisEnterSteveDoring", bisEnterSteveDoring);
		model.addAttribute("asnId", asnId);
		model.addAttribute("action", "update");
		return "cost/enterStevedoring/enterStevedoring";
	}
	
	/*
	 * 查询ASN提单信息
	 * update by slh 20160627 追加 收货方id，mr，当前入库明细最小时间 
	 */
	@RequestMapping(value="cxasn/{asn}",method = RequestMethod.POST)
	@ResponseBody
	public List<String> cxasn(@PathVariable("asn") String asn){
		BisAsn bisAsn = asnService.get(asn);
		List<String> result = new ArrayList<String>();
		if(bisAsn != null && !"".equals(bisAsn)){
			String billNum = bisAsn.getBillNum();
			String ctnNum = bisAsn.getCtnNum();//mr  
			String clientId =bisAsn.getStockIn(); //追加 收获方id，
			String clientName = bisAsn.getStockName();
			String asnState = bisAsn.getAsnState();
			String state ="";
			switch(asnState){ 
				case "1" : state = "在途"; break;
				case "2" : state = "收货中";break; 
				case "3" : state = "已上架"; break;
				case "4" : state = "已完成"; break;
			}
			String enterDate =""; //当前入库明细最小时间，   
			//货物明细
			List<TrayInfo> trayInfoList = trayInfoService.getASNInfoList(asn);
			Integer size = trayInfoList.size();
			Double piece = 0.00; 
			Double netWeight = 0.00;
			Date  enterStockTime=new Date() ; 
			if(!trayInfoList.isEmpty()){
				TrayInfo info =null;
				for(int i = 0;i<size;i++){
					info = new TrayInfo();
					info = trayInfoList.get(i); 
					if(null!=info.getEnterStockTime()){
						enterStockTime =(i==0||info.getEnterStockTime().before(enterStockTime))?info.getEnterStockTime():enterStockTime;
					}
					piece += info.getOriginalPiece()-info.getRemovePiece();
					netWeight += (info.getOriginalPiece()-info.getRemovePiece())*info.getNetSingle();
				}
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
			enterDate=sdf.format(enterStockTime); 
			result.add(state);
			result.add(asn);
			result.add(billNum);
			result.add(ctnNum);
			result.add(clientName);
			result.add(String.valueOf(piece));
			result.add(String.valueOf(netWeight));
			result.add(clientId);
			result.add(enterDate);
//			String tt = state+","+asn+","+billNum+","+ctnNum+","+clientName+","+String.valueOf(piece)+","+String.valueOf(netWeight)+","+clientId+","+enterDate;
			return result;
		}else{
			return result;
		}
	} 
	
	/*
	 * ASN入库货物明细
	 * @param asn
	 */
	@RequestMapping(value="json/{asn}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> json(HttpServletRequest request,@PathVariable("asn") String asn){
		List<Map<String,Object>> asnInfoList = trayInfoService.getAsnInfoByAsn(asn);
		return asnInfoList;
	}
		
	/*
	 * ASN装卸数量统计 展示
	 * @param asn
	 */
	@RequestMapping(value="zxjson/{asn}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> zxjson(HttpServletRequest request,@PathVariable("asn") String asn){
		List<Map<String,Object>> asnInfoList = enterStevedoringService.getEnterStevedoring(asn);
		return asnInfoList;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: ASN装卸单删除
	 * @param ids
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
//		enterStevedoringService.delete(id);
		for(int i = 0;i<ids.size();i++){
			enterStevedoringService.delete((ids.get(i)));
		}
		return "success";
	}
	
	
	/*
	 * 入库装卸单添加跳转
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("action", "add");
		return "cost/enterStevedoring/enterStevedoringAdd" ;
	}
	
	/*
	 * 入库装卸单添加
	 */
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid BisEnterSteveDoring bisEnterSteveDoring,Model model) {
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
		User user=UserUtil.getCurrentUser();
		bisEnterSteveDoring.setCreateUser(user.getName());
		bisEnterSteveDoring.setCreateTime(new Date());
		enterStevedoringService.save(bisEnterSteveDoring);
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
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("应付分拣费"); 
			}
		}
		if(manNum>0.00){
			params.put("feeType", "6");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("人工装卸费");
			}
		}
		if(wrapNum>0.00){
			params.put("feeType", "7");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("缠膜费"); 
			}
		}
		if(packNum>0.00){
			params.put("feeType", "8");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("打包费");
			}
		}
		if(sign == 1){
			return sb.toString() + "的费用方案缺失 ！是否继续添加？";
		}else{
			return "success";
		}
	}
	
	
	/*
	 * 添加台账信息
	 * update by slh 20160627 回写 入库装卸统计 台账单id 
	 */
	@RequestMapping(value="addstandbook/{asn}",method = RequestMethod.GET)
	@ResponseBody
	public String addStandBook(@PathVariable("asn") String asn){
		/*User user = UserUtil.getCurrentUser();
		List<BisEnterSteveDoring>  enterList=enterStevedoringService.getSteveByAsn(asn);
		for(int i=0;enterList!=null&&i<enterList.size();i++){
			BisEnterSteveDoring bisEnterSteveDoring=enterList.get(i);
			//计入台账信息
			BisAsn bisAsn = asnService.get(asn);
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum(bisEnterSteveDoring.getClientId());
			standingBook.setCustomsName(bisEnterSteveDoring.getClient());
			standingBook.setBillNum(bisAsn.getBillNum());
			standingBook.setLinkId(bisAsn.getLinkId());
			standingBook.setCrkSign(1);
			standingBook.setStorageDtae(bisAsn.getInboundTime());
			BaseClientInfo getClient = clientService.get(Integer.valueOf(bisEnterSteveDoring.getClientId())); 
			if(null != getClient){
				if(null!=getClient.getCheckDay()){
					standingBook.setBillDate(DateUtils.ifBillDay(bisAsn.getInboundTime(),getClient.getCheckDay()));
				}
			}
			standingBook.setFeePlan(bisEnterSteveDoring.getFeeId());
			standingBook.setAsn(asn);
			standingBook.setIfReceive(2);
			standingBook.setFillSign(0);
			standingBook.setChargeDate(new Date());
			standingBook.setCostDate(new Date());
			standingBook.setInputPerson(user.getName());
			standingBook.setInputDate(new Date());
			standingBook.setReconcileSign(0);
			standingBook.setSettleSign(0);
			standingBook.setSplitSign(0);
			standingBook.setContactType(1);
			standingBook.setBoxSign(0);
			standingBook.setShareSign(0);
			standingBook.setPaySign("0");
			standingBook.setChargeSign("0");
			standingBook.setExamineSign(1);
			//用于保存的新实体类
			BisStandingBook standingBookNew = null;
			//获取费用方案信息
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("schemeNum", bisEnterSteveDoring.getFeeId());
			List<ExpenseSchemeInfo> expenseList = null;
			ExpenseSchemeInfo expenseInfo  = null;
			BaseTaxRate taxRate = null;
			//生成分拣台账信息
			if(bisEnterSteveDoring.getSortingNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew); 
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "5");
				expenseList=expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisEnterSteveDoring.getSortingNum());
					standingBookNew.setReceiveAmount(bisEnterSteveDoring.getSortingNum() * expenseInfo.getUnit());
					taxRate=taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("5");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisEnterSteveDoring.setDroStanindBookIds(standingBookNew.getStandingNum().toString());
					standingBookService.save(standingBookNew);
				}
			}
			//生成人工台账信息
			if(bisEnterSteveDoring.getManNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "6");
				expenseList=expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisEnterSteveDoring.getManNum());
					standingBookNew.setReceiveAmount(bisEnterSteveDoring.getManNum() * expenseInfo.getUnit());
					taxRate=taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("6");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisEnterSteveDoring.setDroStanindBookIds(standingBookNew.getStandingNum().toString());
				} 
			}
			//生成缠膜台账信息
			if(bisEnterSteveDoring.getWrapNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "7");
				expenseList=expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisEnterSteveDoring.getWrapNum());
					standingBookNew.setReceiveAmount(bisEnterSteveDoring.getWrapNum() * expenseInfo.getUnit() );
					taxRate=taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("7");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisEnterSteveDoring.setDroStanindBookIds(standingBookNew.getStandingNum().toString());
					standingBookService.save(standingBookNew);
				}
			}
			//生成打包台账信息
			if(bisEnterSteveDoring.getPackNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				//获取sequence
				params.put("feeType", "8");
				expenseList=expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisEnterSteveDoring.getPackNum() );
					standingBookNew.setReceiveAmount(bisEnterSteveDoring.getPackNum() * expenseInfo.getUnit() );
					taxRate=taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("8");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisEnterSteveDoring.setDroStanindBookIds(standingBookNew.getStandingNum().toString());
					standingBookService.save(standingBookNew);
				}				
			}
			bisEnterSteveDoring.setIfOk(1);
		    enterStevedroingService.update(bisEnterSteveDoring);
		}
		//更新 是否完成 状态 
		//enterStevedoringService.updateState(asn); 
		return "success";*/
		return enterStevedoringService.addStandBook(asn);
	}
	
	
	
	/*
	 * 取消完成
	 * add by mzy 20161205
	 */
	@RequestMapping(value="concel/{asn}",method = RequestMethod.GET)
	@ResponseBody
	public String concelStandBook(@PathVariable("asn") String asn){
		List<BisEnterSteveDoring> enterList = enterStevedoringService.getSteveByAsn(asn);
		for(int i=0;enterList!=null&&i<enterList.size();i++){
			BisEnterSteveDoring bisEnterSteveDoring = enterList.get(i);
			String standingBookis=bisEnterSteveDoring.getDroStanindBookIds()!=null?bisEnterSteveDoring.getDroStanindBookIds():"";//台账IDs 
			if (standingBookis.trim().length()>0) {
				List<String> ids=Arrays.asList(standingBookis.split(","));//分隔取出台账ID
				standingBookService.deleteStandingBookBatch(ids);//删除对应台账
			}
		}
		//更新取消完成 状态 
		enterStevedoringService.updateState2(asn); 
		return "success";
	}
	
	
	/**
	 * ASN授权
	 * 
	 */
	@RequestMapping(value = "sqAsn", method = RequestMethod.GET)
	@ResponseBody
	public String sqAsn(HttpServletRequest request,String asn){
		BisAsn asnObj=asnService.get(asn);
		if(null!=asnObj){
			asnObj.setIfAllow(1);
			asnService.update(asnObj);
			taskRemindService.steveTask(asnObj,"1");
			return "success";
		}else{
			return "false";
		}
	}
}
