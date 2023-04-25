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

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.cost.service.BackStevedoringService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.cost.entity.BisBackSteveDoring;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
/**
 * OutStevedoringcontroller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/backstevedoring")
public class BackStevedoringContorller extends BaseController {
	
	@Autowired
	private ClientService clientService;
	@Autowired
	private BackStevedoringService backStevedoringService;//入库装卸
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//台账
	@Autowired
	private TaxRateService taxRateService;//汇率
	@Autowired
	private EnterStockService enterStockService;//入库联系单
	@Autowired
	private EnterStockInfoService enterStockInfoService;//入库联系单明细
	@Autowired
	private ASNService asnService;
	
	@Autowired
	private BackStevedoringService backStevedoringservice;//倒箱装卸队统计
	
	/*跳转倒箱装卸列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/backStevedoring/backStevedoring";
	}
	
	/*跳转倒箱装卸总览页面PH*/
	@RequestMapping(value="backstevedoringList",method = RequestMethod.GET)
	public String getMenuList(){
		return "cost/backStevedoring/backstevedoringList";
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
		Page<BisBackSteveDoring> page = getPage(request);
//		page.setOrder(Page.DESC);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = backStevedoringService.search(page, filters);
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
	@RequestMapping(value="update/{billNum}", method = RequestMethod.GET)
	public String update(Model model, @PathVariable("billNum") String billNum) {
		model.addAttribute("billNum", billNum);
		model.addAttribute("action", "update");
		return "cost/backStevedoring/backStevedoring";
	}
	
	/*
	 * 查询出库装车单信息
	 */
	@RequestMapping(value="cx/{billNum}",method = RequestMethod.POST)
	@ResponseBody
	public List<String> cxasn(@PathVariable("billNum") String billNum){
		//根据装车号获取出库装车信息
		List<String> result = new ArrayList<String>();
		if(null==billNum||"".equals(billNum)){
			return result;
		}
		List<BisEnterStock> enterStockList = enterStockService.getEnterStockByBillNum(billNum.replace("笑脸","/"));
		if(!enterStockList.isEmpty()){
			//从出库订单表中获取装车状态
			BisEnterStock enterStock = enterStockList.get(0);
			String linkId = enterStock.getLinkId();
			String client = enterStock.getStockIn();
			Double weight = 0.00;
			List<BisEnterStockInfo> enterStockInfoList = enterStockInfoService.getList(linkId);
			if(!enterStockInfoList.isEmpty()){
				int size = enterStockInfoList.size();
				for(int i =0;i<size;i++){
					if(null==enterStockInfoList.get(i).getNetWeight()){
						weight = 0.00;
					}else{
						weight += enterStockInfoList.get(i).getNetWeight();
					}
				}
			}
			String sbackdate = "";//enterStock.getBackDate();
			if(enterStock.getBackDate()!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
				sbackdate=sdf.format(enterStock.getBackDate()); 
			}  
			result.add(linkId);
			result.add(client);
			result.add(enterStock.getItemNum());
			result.add(String.valueOf(weight));
			result.add(enterStock.getStockId());
			result.add(sbackdate);
			return result;
		}else{
			return result;
		}
	} 
	
	/*
	 * 倒箱装卸数量统计 展示
	 * @param asn
	 */
	@RequestMapping(value="zxjson/{billNum}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> zxjson(HttpServletRequest request,@PathVariable("billNum") String billNum){
		if(null==billNum){
			return null;
		}
		List<Map<String,Object>> backList= backStevedoringService.getBackStevedoring(billNum.replace("笑脸","/"));
		return backList;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 倒库装卸单删除
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		for(int i = 0;i<ids.size();i++){
			backStevedoringService.delete((ids.get(i)));
		}
		return "success";
	}
	
	
	/*
	 * 出库装卸单添加跳转
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("action", "add");
		return "cost/backStevedoring/backStevedoringAdd" ;
	}
	
	/*
	 * 倒箱装卸单添加
	 */
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid BisBackSteveDoring bisBackSteveDoring,Model model) {
		Double netWeight = bisBackSteveDoring.getWeight();
		//后台校验 四个数量是否小于（ 装车订单数量 - 已添加的装卸单数量）
		String billNum = bisBackSteveDoring.getBillNum();
		List<BisBackSteveDoring> backList = backStevedoringService.getSteveByBill(billNum);
		//定义剩余未操作重量
		Double sortingNum = 0.00;
		Double manNum = 0.00;
		//Double wrapNum = 0.00;
		Double packNum = 0.00;
		
		//遍历出已操作重量和
		if(!backList.isEmpty()){
			int size = backList.size();
			BisBackSteveDoring backSteveDoring = null;
			for(int i=1;i<size;i++){
				backSteveDoring = new BisBackSteveDoring();
				backSteveDoring = backList.get(i);
				sortingNum += backSteveDoring.getSortingNum();
				manNum += backSteveDoring.getManNum();
				//wrapNum += backSteveDoring.getWrapNum();
				packNum += backSteveDoring.getPackNum();
			}
		}//end if
		sortingNum = netWeight - sortingNum;
		manNum     = netWeight - manNum;
		//wrapNum    = netWeight - wrapNum;
		packNum    = netWeight - packNum;
		if(sortingNum < bisBackSteveDoring.getSortingNum()){
			return "sorting";
		}
		if(manNum < bisBackSteveDoring.getManNum()){
			return "man";
		}
		if(packNum < bisBackSteveDoring.getPackNum()){
			return "pack";
		}
		if(null==bisBackSteveDoring.getDrobackdate()&&null==bisBackSteveDoring.getDate()){
			return "data";
		}
		bisBackSteveDoring.setDrobackdate(bisBackSteveDoring.getDrobackdate()!=null?bisBackSteveDoring.getDrobackdate():bisBackSteveDoring.getDate());		
		//状态默认为未完成
		if(bisBackSteveDoring.getIfAllMan() == null ){
			bisBackSteveDoring.setIfAllMan(0);
		}
		bisBackSteveDoring.setIfOk(0);
		backStevedoringService.save(bisBackSteveDoring);
		return "success";
	}
	
	/**
	 * 判断是否有相对应的费用方案
	 * 
	 */
	@RequestMapping(value = "judgefee", method = RequestMethod.GET)
	@ResponseBody
	public String judgefee(HttpServletRequest request,Double sortingNum,Double manNum,Double wrapNum,Double packNum,String feeId,Double nbqNum,Double wbqNum,Double mtNum,Double ztjNum,Double ctjNum){
		Map<String,Object> params  = new HashMap<String,Object>();
		params.put("schemeNum", feeId);
		List<ExpenseSchemeInfo> expenseList = null;
		StringBuffer sb = new StringBuffer();
		int sign = 0;
		if(null!=sortingNum&&sortingNum>0.00){
			params.put("feeType", "5");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("分拣  ");
			}
		}
		if(null!=manNum&&manNum>0.00){
			params.put("feeType", "6");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("人工  ");
			}
		}
		if(null!=wrapNum&&wrapNum>0.00){
			params.put("feeType", "7");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("缠膜  ");
			}
		}
		if(null!=packNum&&packNum>0.00){
			params.put("feeType", "8");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("打包  ");
			}
		}
		if(null!=nbqNum&&nbqNum>0.00){
			params.put("feeType", "21");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("内标签  ");
			}
		}
		if(null!=wbqNum&&wbqNum>0.00){
			params.put("feeType", "22");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("外标签  ");
			}
		}
		if(null!=mtNum&&mtNum>0.00){
			params.put("feeType", "23");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("码托  ");
			}
		}
		if(null!=ztjNum&&ztjNum>0.00){
			params.put("feeType", "24");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("装铁架  ");
			}
		}
		if(null!=ctjNum&&ctjNum>0.00){
			params.put("feeType", "25");
			expenseList = new ArrayList<ExpenseSchemeInfo>();
			expenseList = expenseSchemeInfoService.getFeeByName(params);
			if(expenseList.isEmpty()){
				sign = 1;
				sb.append("拆铁架  ");
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
	 */
	@RequestMapping(value="addstandbook/{billNum}",method = RequestMethod.GET)
	@ResponseBody
	public String addStandBook(@PathVariable("billNum") String billNum){
		User user = UserUtil.getCurrentUser();
		billNum=billNum.replace("笑脸","/");
		List<BisBackSteveDoring> backList = backStevedoringService.getSteveByBill(billNum);
		for (int i = 0;backList!=null&&i < backList.size(); i++) {
			BisBackSteveDoring bisBackSteveDoring=backList.get(i);
			//计入台账信息
			BisEnterStock enterStock = enterStockService.getEnterStockByBillNum(billNum).get(0);
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum(bisBackSteveDoring.getClientId());
			standingBook.setCustomsName(bisBackSteveDoring.getClient());
			standingBook.setBillNum(billNum);
			standingBook.setLinkId(enterStock.getLinkId());
			standingBook.setCrkSign(1);
			Date enterDate = enterStock.getEnterTime();
			List<BisAsn> bisAsn = asnService.ifasn(enterStock.getLinkId());
			if(!bisAsn.isEmpty()){
				standingBook.setStorageDtae(bisAsn.get(0).getInboundTime());
			}else{
				standingBook.setStorageDtae(enterStock.getOperateTime());
			}
			standingBook.setFeePlan(bisBackSteveDoring.getFeeId());
			standingBook.setIfReceive(2);
			standingBook.setFillSign(0);
			standingBook.setChargeDate(enterDate);
			BaseClientInfo getClient = clientService.get(Integer.valueOf(bisBackSteveDoring.getClientId())); 
			//账单日期(客户的结账日跟入库联系单实际入库时间是否要计入下个月)
			if(null != getClient){
				if(null!=getClient.getCheckDay() && enterDate!=null){
					standingBook.setBillDate(DateUtils.ifBillDay(enterDate,getClient.getCheckDay()));
				}
			}
			standingBook.setCostDate(new Date());
			standingBook.setInputPerson(user.getName());
			standingBook.setInputDate(new Date());
			standingBook.setSplitSign(0);
			standingBook.setSettleSign(0);
			standingBook.setReconcileSign(0);
			standingBook.setContactType(1);
			standingBook.setBoxSign(1);
			standingBook.setShareSign(0);
			standingBook.setPaySign("0");
			standingBook.setChargeSign("0");
			standingBook.setExamineSign(1);
			//用于保存的新实体类
			BisStandingBook standingBookNew = null;
			//获取费用方案信息
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("schemeNum", bisBackSteveDoring.getFeeId());
			List<ExpenseSchemeInfo> expenseList = null;
			ExpenseSchemeInfo expenseInfo  = null;
			BaseTaxRate taxRate = null;
			//生成在库分拣台账信息
			if(null!=bisBackSteveDoring.getSortingNum()&&bisBackSteveDoring.getSortingNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "5");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getSortingNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getSortingNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("5");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成人工台账信息
			if(null!=bisBackSteveDoring.getManNum()&&bisBackSteveDoring.getManNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "6");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getManNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getManNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("6");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成缠膜台账信息
			if(null!=bisBackSteveDoring.getWrapNum()&&bisBackSteveDoring.getWrapNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "7");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getWrapNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getWrapNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("7");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}				
		   }
			//生成打包台账信息
			if(null!=bisBackSteveDoring.getPackNum()&&bisBackSteveDoring.getPackNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				//获取sequence
				params.put("feeType", "8");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getPackNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getPackNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("8");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}		
			}
			//生成内标签台账
			if(null!=bisBackSteveDoring.getNbqNum()&&bisBackSteveDoring.getNbqNum()!= 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "21");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getNbqNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getNbqNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("21");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成外标签台账
			if(null!=bisBackSteveDoring.getWbqNum()&&bisBackSteveDoring.getWbqNum()!= 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "22");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getWbqNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getWbqNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("22");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成码托台账
			if(null!=bisBackSteveDoring.getMtNum()&&bisBackSteveDoring.getMtNum()!= 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "23");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getMtNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getMtNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("23");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成装铁架台账
			if(null!=bisBackSteveDoring.getZtjNum()&&bisBackSteveDoring.getZtjNum()!= 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "24");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getZtjNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getZtjNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("24");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			//生成拆铁架台账
			if(null!=bisBackSteveDoring.getCtjNum()&&bisBackSteveDoring.getCtjNum()!= 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
 				standingBookNew.setStandingNum(standingBookService.getSequenceId());
				params.put("feeType", "25");
				expenseList = new ArrayList<ExpenseSchemeInfo>();
				expenseList = expenseSchemeInfoService.getFeeByName(params);
				if(!expenseList.isEmpty()){
					expenseInfo = new ExpenseSchemeInfo();
					expenseInfo = expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisBackSteveDoring.getCtjNum() );
					standingBookNew.setReceiveAmount(bisBackSteveDoring.getCtjNum() * expenseInfo.getUnit() );
					taxRate = new BaseTaxRate();
					taxRate = taxRateService.getTaxByC(expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("25");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBookService.save(standingBookNew);
					bisBackSteveDoring.setStandingbookids(standingBookNew.getStandingNum()!=null?standingBookNew.getStandingNum().toString():null);
				}
			}
			backStevedoringservice.update(bisBackSteveDoring);
		}
		//更新 是否完成 状态
		backStevedoringService.updateState(billNum);
		return "success";
	}
	
	
	
	/*
	 * 取消完成
	 * add by mzy 20161205
	 */
	@RequestMapping(value="concel/{billNum}",method = RequestMethod.GET)
	@ResponseBody
	public String concelStandBook(@PathVariable("billNum") String billNum){
		billNum=billNum.replace("笑脸","/");
		List<BisBackSteveDoring> backList= backStevedoringService.getSteveByBill(billNum);
		for (int i = 0;backList!=null&&i < backList.size(); i++) {
			BisBackSteveDoring bisBackSteveDoring=backList.get(i);
			String standingBookis=bisBackSteveDoring.getStandingbookids()!=null?bisBackSteveDoring.getStandingbookids().toString():"";
			if (standingBookis.trim().length()>0) {
				List<String> ids=Arrays.asList(standingBookis.split(","));//分隔取出台账ID
				standingBookService.deleteStandingBookBatch(ids);//删除对应台账
			}
		}
		//更新取消完成 状态 
		backStevedoringservice.updateState2(billNum); 
		return "success";
	}
	

}
