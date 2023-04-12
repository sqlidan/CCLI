package com.haiersoft.ccli.cost.web;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.entity.FeeCode;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.cost.service.BisPayInfoService;
import com.haiersoft.ccli.cost.service.BisPayService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.cost.entity.BisPay;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
import com.haiersoft.ccli.cost.entity.BisPayInfoExcel;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.FeeCodeService;
/**
 * BisPayContorller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/bispayinfo")
public class BisPayInfoContorller extends BaseController {
	
	@Autowired
	private BisPayInfoService bisPayInfoService;//业务付款单明细
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//费目方案明细
	@Autowired
	private BisPayService bisPayService;//业务付款单
	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private FeeCodeService feeCodeService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private StandingBookService standingBookService;// 台账表
	/**
	 * 
	 * @author pyl
	 * @Description: 业务付款单单明细查询
	 * @date 2016年5月13日  
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{payId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("payId") String payId) {
		Page<BisPayInfo> page = getPage(request);
		page.setPageSize(9999);
		page.setOrderBy("id");
		page.setOrder("desc");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		PropertyFilter filter = new PropertyFilter("EQS_payId", payId);
		filters.add(filter);
		page = bisPayInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	

	/**
	 * 添加业务付款单明细跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create/{payId}", method = RequestMethod.GET)
	public String createForm(@PathVariable("payId") String payId,Model model) {
		model.addAttribute("payId",payId);
		model.addAttribute("bisPay",bisPayService.get(payId));
		model.addAttribute("bisPayInfo", new BisPayInfo());
		model.addAttribute("action", "create");
		return "cost/bisPay/bisPayInfo";
	}
	
	
	/**
	 * 添加业务付款单明细
	 * 
	 * @param user
	 * @param model
	 * @throws ParseException 
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisPayInfo bisPayInfo) throws ParseException {
		String code = bisPayInfo.getFeeCode();
		FeeCode feeCode = feeCodeService.find("code", code); //获得费用方案明细
		bisPayInfo.setCheckSign(0);
		//根据提单号获得对应入库联系单
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("itemNum",bisPayInfo.getBillNum());
		param.put("delFlag","0");
		List<BisEnterStock> enterStock = enterStockService.findEnterList(param);	
		BisEnterStock enterObj=enterStock.get(0);
		//判断入库联系单是不是已经费用完成如果完成不能进行操作，需要财务人员放开权限
		if(enterObj!=null&&"1".equals(enterObj.getFinishFeeState())){
			return bisPayInfo.getBillNum()+"提单号已经费用完成，不允许此操作!"	;	
		}
		//用于查找台账中是否有重复的费目
		/*
		 * 根据是否冲账，是否垫付两个条件进行判断，
		 * （1）冲账时： 不判断重复，并且垫付 生成应收应付两条信息；并且不垫付 生成应付信息
		 * （2）不冲账：并且垫付，判断台账费用是否有重复，生成应收应付两条；并且不垫付，判断应付是否重复，生成一条应付*/
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_schemeNum",enterObj.getFeeId()!=null?enterObj.getFeeId():null));
		filters.add(new PropertyFilter("EQS_feeCode",bisPayInfo.getFeeCode()));
		List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoService.search(filters);
		
		int repeat = 0;//判断台账中是否有重复的标志位 0为不重复，1为重复
		if(1==bisPayInfo.getIfReverse()){//冲账
			BisPay bisPay=bisPayService.get(bisPayInfo.getPayId());
			//生成主表的台账
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum(bisPay.getClientId());
			standingBook.setCustomsName(bisPay.getClientName());
			standingBook.setBillNum(bisPayInfo.getBillNum());
			standingBook.setLinkId(enterObj.getLinkId());
			standingBook.setCrkSign(1);
			standingBook.setFeePlan(enterObj.getFeeId()!=null?enterObj.getFeeId():null);
			standingBook.setFeeCode(bisPayInfo.getFeeCode());
			standingBook.setFeeName(bisPayInfo.getFeeName());
			standingBook.setIfReceive(2);
			standingBook.setNum(bisPayInfo.getNum());
			standingBook.setPrice(bisPayInfo.getUnitPrice());
			standingBook.setCostDate(new Date());
			standingBook.setFillSign(0);
			standingBook.setCurrency(null!=feeCode.getCurrencyType()?feeCode.getCurrencyType().toString():"0"); 
			standingBook.setSalesman(null!=bisPayInfo.getSellMan()?bisPayInfo.getSellMan():null);
			standingBook.setExchangeRate(1.00);
			standingBook.setShareSign(1);
			standingBook.setBisType(null!=feeCode.getFeeType()?feeCode.getFeeType().toString():"1");
			standingBook.setReceiveAmount(bisPayInfo.getTotelPrice());
			standingBook.setShouldRmb(bisPayInfo.getTotelPrice());
			standingBook.setTaxRate(bisPayInfo.getTaxRate());
			standingBook.setRemark("业务付款单明细: "+bisPayInfo.getRemark());
			standingBook.setContactType(1);
			if(null != enterObj.getRkTime()&& !"".equals(enterObj.getRkTime())){
				//获取客户信息，用于获取对账单日
				standingBook.setStorageDtae(enterStockService.getFirstTime(enterObj.getRkTime()));
				BaseClientInfo clientObj = clientService.get(Integer.valueOf(bisPayInfo.getClientId()));
				standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterObj.getRkTime()),clientObj.getCheckDay()));
			}
			standingBook.setStandingNum(standingBookService.getSequenceId());
			standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
			standingBook.setPaySign(1==bisPayInfo.getHelpPay()?"1":"0");
			User user = UserUtil.getCurrentUser();
			standingBook.setInputPerson(user.getName());
			standingBook.setInputPersonId(String.valueOf(user.getId()));
			standingBook.setInputDate(new Date());
			bisPayInfo.setStandingNum(standingBook.getStandingNum());
			bisPayInfo.setShareState("1");
			bisPayInfoService.save(bisPayInfo);
			standingBook.setPayId(bisPayInfo.getId());
			if(1==bisPayInfo.getHelpPay()){
				//生成明细垫付的台账
				BisStandingBook standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
				standingBookNew.setCustomsNum(bisPayInfo.getClientId());
				standingBookNew.setCustomsName(bisPayInfo.getClientName());
				standingBookNew.setPayStandingNum(standingBook.getStandingNum());//应付台账的关联台账ID为应收台账的Id
				standingBookNew.setStandingNum(standingBookService.getSequenceId()); //生成新的应付台账的ID
				standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
				standingBook.setPayStandingNum(standingBookNew.getStandingNum());//应收台账的关联台账ID为应付台账的ID
				standingBookNew.setIfReceive(1);
				standingBookNew.setPaySign("1");
				//如果对应的费目方案中有拖车费价钱计算要以这个为准
				if(null != schemeInfos && schemeInfos.size() > 0){
					ExpenseSchemeInfo schemeInfo=schemeInfos.get(0);
					standingBookNew.setPrice(schemeInfo.getUnit());
					standingBookNew.setReceiveAmount(BigDecimalUtil.mul(standingBookNew.getNum(), schemeInfo.getUnit()));
					standingBookNew.setShouldRmb(BigDecimalUtil.mul(standingBookNew.getReceiveAmount(),standingBookNew.getExchangeRate()));
				}
				//判断是否是属于业务付款单的费目
				if(0==bisPayInfo.getTaxRate()){//判断是否是属于业务付款单的费目
					standingBookNew.setNum(standingBookNew.getNum()*1.0683);
					standingBookNew.setPrice(standingBookNew.getPrice());
					standingBookNew.setReceiveAmount(BigDecimalUtil.mul(standingBookNew.getNum(),standingBookNew.getReceiveAmount()));
					standingBookNew.setShouldRmb(BigDecimalUtil.mul(standingBookNew.getReceiveAmount(),standingBookNew.getExchangeRate()));
				}
				standingBookService.merge(standingBookNew);
			}else{
				standingBook.setPayStandingNum(0);
			}
			standingBookService.merge(standingBook);
		}else{//不冲账
			BisPay bisPay=bisPayService.get(bisPayInfo.getPayId());
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("linkId", enterObj.getLinkId());
			params.put("feeCode",code);
			params.put("ifReceive",2);//应付
			params.put("customsNum",bisPay.getClientId());//同一订单不同客户要进行判断处理
			if(1==bisPayInfo.getHelpPay()){//不冲账，垫付
				//判断是否重复
				List<BisStandingBook> bookList = standingBookService.getList(params);
				if(!bookList.isEmpty()){
					repeat = 1;
				}
				if("1".equals(enterObj.getFinishFeeState())&& repeat==0){
					repeat = 2;
				}
				//不重复
				if(repeat == 0){
					//生成主表的台账
					BisStandingBook standingBook = new BisStandingBook();
					standingBook.setCustomsNum(bisPay.getClientId());
					standingBook.setCustomsName(bisPay.getClientName());
					standingBook.setBillNum(bisPayInfo.getBillNum());
					standingBook.setLinkId(enterObj.getLinkId());
					standingBook.setCrkSign(1);
					standingBook.setFeePlan(enterObj.getFeeId()!=null?enterObj.getFeeId():null);
					standingBook.setFeeCode(bisPayInfo.getFeeCode());
					standingBook.setFeeName(bisPayInfo.getFeeName());
					standingBook.setIfReceive(2);
					standingBook.setNum(bisPayInfo.getNum());
					standingBook.setPrice(bisPayInfo.getUnitPrice());
					standingBook.setCostDate(new Date());
					standingBook.setFillSign(0);
					standingBook.setCurrency(null!=feeCode.getCurrencyType()?feeCode.getCurrencyType().toString():"0"); 
					standingBook.setSalesman(bisPayInfo.getSellMan());
					standingBook.setExchangeRate(1.00);
					standingBook.setShareSign(1);
					standingBook.setBisType(null!=feeCode.getFeeType()?feeCode.getFeeType().toString():"1");
					standingBook.setReceiveAmount(bisPayInfo.getTotelPrice());
					standingBook.setShouldRmb(bisPayInfo.getTotelPrice());
					standingBook.setTaxRate(bisPayInfo.getTaxRate());
					standingBook.setRemark("业务付款单明细: "+bisPayInfo.getRemark());
					standingBook.setContactType(1);
					if(null != enterObj.getRkTime()&& !"".equals(enterObj.getRkTime())){
						//获取客户信息，用于获取对账单日
						standingBook.setStorageDtae(enterStockService.getFirstTime(enterObj.getRkTime()));
						BaseClientInfo clientObj = clientService.get(Integer.valueOf(bisPayInfo.getClientId()));
						standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterObj.getRkTime()),clientObj.getCheckDay()));
					}
					standingBook.setStandingNum(standingBookService.getSequenceId());
					standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
				    standingBook.setPaySign(1==bisPayInfo.getHelpPay()?"1":"0");
					User user = UserUtil.getCurrentUser();
					standingBook.setInputPerson(user.getName());
					standingBook.setInputDate(new Date());
					bisPayInfo.setStandingNum(standingBook.getStandingNum());
					bisPayInfo.setShareState("1");
					bisPayInfoService.save(bisPayInfo);
					standingBook.setPayId(bisPayInfo.getId());
					standingBookService.merge(standingBook);
					//生成明细垫付的台账首先要判断自动引入的有没有记录
					BisStandingBook standingBookNew = new BisStandingBook();
					BeanUtils.copyProperties(standingBook, standingBookNew);
					standingBookNew.setCustomsNum(bisPayInfo.getClientId());
					standingBookNew.setCustomsName(bisPayInfo.getClientName());
					standingBookNew.setPayStandingNum(standingBook.getStandingNum());//应付台账的关联台账ID为应收台账的Id
					standingBookNew.setStandingNum(standingBookService.getSequenceId()); //生成新的应付台账的ID
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					standingBook.setPayStandingNum(standingBookNew.getStandingNum());//应收台账的关联台账ID为应付台账的ID
					standingBookNew.setIfReceive(1);
					standingBookNew.setPaySign("1");
					//如果对应的费目方案中有拖车费价钱计算要以这个为准
					if(null != schemeInfos && schemeInfos.size() > 0){
						ExpenseSchemeInfo schemeInfo=schemeInfos.get(0);
						standingBookNew.setPrice(schemeInfo.getUnit());
						standingBookNew.setReceiveAmount(BigDecimalUtil.mul(standingBookNew.getNum(), schemeInfo.getUnit()));
						standingBookNew.setShouldRmb(BigDecimalUtil.mul(standingBookNew.getReceiveAmount(),standingBookNew.getExchangeRate()));
					}
					//判断是否是属于业务付款单的费目
					if(0==bisPayInfo.getTaxRate()){//判断是否是属于业务付款单的费目
						standingBookNew.setNum(standingBookNew.getNum()*1.0683);
						standingBookNew.setPrice(standingBookNew.getPrice());
						standingBookNew.setReceiveAmount(BigDecimalUtil.mul(standingBookNew.getNum(),standingBookNew.getReceiveAmount()));
						standingBookNew.setShouldRmb(BigDecimalUtil.mul(standingBookNew.getReceiveAmount(),standingBookNew.getExchangeRate()));
					}
					standingBookService.merge(standingBookNew);
				}else if(repeat==2){
					bisPayInfo.setShareState("3");
					bisPayInfoService.save(bisPayInfo);
				}else{
					bisPayInfo.setShareState("2");
					bisPayInfoService.save(bisPayInfo);
				}
			}else{//不冲账 不垫付
				List<BisStandingBook> bookList = standingBookService.getList(params);
				if(!bookList.isEmpty()){
					repeat = 1;
				}
				if("1".equals(enterObj.getFinishFeeState())&& repeat==0){
					repeat = 2;
				}
				if(repeat == 0){
					//生成主表的台账
					BisStandingBook standingBook = new BisStandingBook();
					standingBook.setCustomsNum(bisPay.getClientId());
					standingBook.setCustomsName(bisPay.getClientName());
					standingBook.setBillNum(bisPayInfo.getBillNum());
					standingBook.setLinkId(enterObj.getLinkId());
					standingBook.setCrkSign(1);
					standingBook.setFeePlan(enterObj.getFeeId()!=null?enterObj.getFeeId():null);
					standingBook.setFeeCode(bisPayInfo.getFeeCode());
					standingBook.setFeeName(bisPayInfo.getFeeName());
					standingBook.setIfReceive(2);
					standingBook.setNum(bisPayInfo.getNum());
					standingBook.setPrice(bisPayInfo.getUnitPrice());
					standingBook.setCostDate(new Date());
					standingBook.setFillSign(0);
					standingBook.setCurrency(null!=feeCode.getCurrencyType()?feeCode.getCurrencyType().toString():"0"); 
					standingBook.setSalesman(bisPayInfo.getSellMan());
					standingBook.setExchangeRate(1.00);
					standingBook.setShareSign(1);
					standingBook.setBisType(null!=feeCode.getFeeType()?feeCode.getFeeType().toString():"1");
					standingBook.setReceiveAmount(bisPayInfo.getTotelPrice());
					standingBook.setShouldRmb(bisPayInfo.getTotelPrice());
					standingBook.setTaxRate(bisPayInfo.getTaxRate());
					standingBook.setRemark("业务付款单明细: "+bisPayInfo.getRemark());
					standingBook.setContactType(1);
					if(null != enterObj.getRkTime()&& !"".equals(enterObj.getRkTime())){
						//获取客户信息，用于获取对账单日
						standingBook.setStorageDtae(enterStockService.getFirstTime(enterObj.getRkTime()));
						BaseClientInfo clientObj = clientService.get(Integer.valueOf(bisPayInfo.getClientId()));
						standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterObj.getRkTime()),clientObj.getCheckDay()));
					}
					standingBook.setStandingNum(standingBookService.getSequenceId());
					standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
					standingBook.setPaySign(1==bisPayInfo.getHelpPay()?"1":"0");
					User user = UserUtil.getCurrentUser();
					standingBook.setInputPerson(user.getName());
					standingBook.setInputDate(new Date());
					bisPayInfo.setStandingNum(standingBook.getStandingNum());
					bisPayInfo.setShareState("1");
					bisPayInfoService.save(bisPayInfo);
					standingBook.setPayId(bisPayInfo.getId());
					standingBook.setPayStandingNum(0);
					standingBookService.merge(standingBook);
				}else if(repeat==2){
					bisPayInfo.setShareState("3");
					bisPayInfoService.save(bisPayInfo);
				}else{
					bisPayInfo.setShareState("2");
					bisPayInfoService.save(bisPayInfo);
				}
			}//end 不冲账 不垫付
		}//end 不冲账
		return "success";
	}
	
	/**
	 * 修改业务付款单明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		BisPayInfo bisPayInfo=bisPayInfoService.get(id);
		model.addAttribute("bisPayInfo",bisPayInfo);
		model.addAttribute("bisPay",null!=bisPayInfo.getPayId()?bisPayService.get(bisPayInfo.getPayId()):new BisPay());
		model.addAttribute("action", "update");
		return "cost/bisPay/bisPayInfo";
	}
	
	/**
	 * 修改业务付款单明细
	 * @param user
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid BisPayInfo bisPayInfo) throws ParseException {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(bisPayInfo.getId());
		this.delete(ids);
 		bisPayInfo.setId(null);
		this.create(bisPayInfo);
		return "success";
	}
	

	/**
	 * 
	 * @author pyl
	 * @Description: 删除明细
	 * @date 2016年3月3日 下午2:37:12 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{ids}")
	@ResponseBody
	public String delete(@PathVariable("ids") List<Integer> ids) {
		for(Integer id :ids){
			BisPayInfo payInfo=bisPayInfoService.get(id);
			//根据提单号获得对应入库联系单
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("itemNum",payInfo.getBillNum());
			param.put("delFlag","0");
			List<BisEnterStock> enterStock = enterStockService.findEnterList(param);	
			BisEnterStock enterObj=enterStock.get(0);
			//判断入库联系单是不是已经费用完成如果完成不能进行操作，需要财务人员放开权限
			if(enterObj!=null&&"1".equals(enterObj.getFinishFeeState())){
				return payInfo.getBillNum()+"提单号已经费用完成，不允许此操作!";	
			}
			//删除台账的处理
			bisPayService.deletePayInfos(id);
			/*if("1".equals(payInfo.getShareState())){
				//首先删除的是应付的对账单跟已经垫付的应收对账单
				BisStandingBook standingBook=standingBookService.get(payInfo.getStandingNum());
				if(standingBook!=null){
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("payStandingNum",standingBook.getStandingNum());
					params.put("payId",standingBook.getPayId());
					params.put("ifReceive",1);
					List<BisStandingBook> bsb=standingBookService.getList(params);
					for (int i = 0; bsb!=null&&i < bsb.size(); i++) {
						standingBookService.delete(bsb.get(i));
					}
					Integer fBook  = standingBook.getPayStandingNum();
					if(fBook!=null&&0!=fBook){
						BisStandingBook book=standingBookService.get(standingBook.getPayStandingNum());
						if(book!=null){
						   standingBookService.delete(book);
						}
					}
					standingBookService.delete(payInfo.getStandingNum());
				}
			}*/
	 		bisPayInfoService.delete(id);
		}
		return "success";
	}
	
	
	/**
	 * Ajax请求校验提单号是否存在。
	 */
	@RequestMapping(value = "checkbillnum")
	@ResponseBody
	public String checkbillnum(HttpServletRequest request,String billNum,String clientId,String feeCode,Integer id,String stockId) {
		StringBuffer sb=new StringBuffer();
		if(null==billNum||"".equals(billNum)){
			sb.append("提单号不存在！");
			return sb.toString();
		}
		//判断入库联系单里面提单号是否费用完成
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itemNum", billNum);
		params.put("delFlag","0");
		List<BisEnterStock> enterStock = enterStockService.findEnterList(params);
		if(enterStock!=null&&enterStock.size()>0){
			BisEnterStock stock=enterStock.get(0);
			if("1".equals(stock.getFinishFeeState())){
				sb.append("入库联系单费用已完成，请联系财务人员！");
				return sb.toString();
			}
		}
		//判断结算方+客户+费目+提单+匹配成功存不存存在给出提示
		//同一个提单可以找2个装卸队来干
		//存在同一个提单号分2家客户做的问题
	    /*Map<String,Object> paramInfos = new HashMap<String,Object>();
		paramInfos.put("clientId", clientId);
		paramInfos.put("billNum",billNum);
		paramInfos.put("feeCode",feeCode);
		paramInfos.put("shareState","1");
		if(null!=id){
			BisPayInfo bisPayInfo=bisPayInfoService.get(id);
			if(billNum.equals(bisPayInfo.getBillNum())){
				return "success";
			}
		}
		List<BisPayInfo> list=bisPayInfoService.findby(paramInfos);
		if(list!=null&&list.size()>0){
			BisPayInfo info=list.get(0);
			sb.append("业务单号【");
			sb.append(info.getPayId());
			sb.append("】提单号为【");
			sb.append(billNum);
			sb.append("】的付款明细已经匹配【");
			sb.append(feeCode);
			sb.append("】费目代码");
			return sb.toString();
		}*/
		return "success";
	}
	
	
	/**
     * 业务付款单明细导入模板下载
     * @param request
     * @param response
     * @throws ServletException
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ResponseBody
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String fileName = "业务付款单明细导入模板.xls";
        String filePath = PropertiesUtil.getPropertiesByName("downloadexcel", "application");
        String realpath = filePath + "WEB-INF\\classes\\importExcel\\业务付款单明细导入模板.xls";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            String formatFileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=\"" + formatFileName + "\"");
            fis = new FileInputStream(realpath);
            bis = new BufferedInputStream(fis);
            fos = response.getOutputStream();
            bos = new BufferedOutputStream(fos);
            int bytesRead = 0;
            byte[] buffer = new byte[5 * 1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
            }
            bos.close();
            bis.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            response.reset();
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                System.err.print(e);
            }
        }
    }
    
    
    
    /**
     * 业务付款单明细excel跳转
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "into/{payId}", method = RequestMethod.GET)
    public String intoExcel(Model model, @PathVariable("payId") String payId) {
        model.addAttribute("payId", payId);
        model.addAttribute("action", "intot");
        return "cost/bisPay/bisPayInfoInto";
    }

    /**
     * 业务付款单明细excel导入
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "intot/{payId}", method = RequestMethod.POST)
    @ResponseBody
    public String intoExcel2(@RequestParam(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request,Model model,@PathVariable("payId") String payId) {
        StringBuffer noBill=new StringBuffer();
        StringBuffer noFee=new StringBuffer();
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            List<BisPayInfoExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), BisPayInfoExcel.class, params);
            Map<String, Object> param= new HashMap<String, Object>();
            if (list != null && list.size() > 0) {
                int i = 2;
                for (BisPayInfoExcel getObj : list) {
            		i++;
            		param.put("itemNum",getObj.getBillNum()!=null?getObj.getBillNum():"0");
            		param.put("delFlag","0");
            		List<BisEnterStock> enterList=enterStockService.findEnterList(param);
            		BisEnterStock enter=null!=enterList?enterList.get(0):null;
            		String nameC = getObj.getFeeName()!=null?getObj.getFeeName().trim():"";
            		List<Map<String,Object>> list1 = feeCodeService.findFeeCodeByName(nameC);
            		if(null==enter){
            			noBill.append(String.valueOf(i)+",");
            		}else if(list1.size() == 0){
            			noFee.append(String.valueOf(i)+",");
            		}
                }//end for
                String noFee1 = noFee.toString();
            	String noBill1 = noBill.toString();
            	if ("".equals(noBill1) && "".equals(noFee1)) {
                    //验证合格可以导入
            		for (BisPayInfoExcel getObj : list) {
                		i++;
                		param.put("itemNum",getObj.getBillNum()!=null?getObj.getBillNum():"0");
                		param.put("delFlag","0");
                		List<BisEnterStock> enterList=enterStockService.findEnterList(param);
                		BisEnterStock enter=null!=enterList?enterList.get(0):null;
                		String nameC = getObj.getFeeName()!=null?getObj.getFeeName().trim():"";
                		List<Map<String,Object>> list1 = feeCodeService.findFeeCodeByName(nameC);
                		Map<String,Object> feeMap = list1.size()>0?list1.get(0):null;
                		
                		if(null==enter){
                			noBill.append(String.valueOf(i)+",");
                		}else if(list1.size() == 0){
                			noFee.append(String.valueOf(i)+",");
                		}else{
	                    	BisPayInfo newObj = new BisPayInfo();
	                    	newObj.setPayId(payId);
	                        newObj.setCheckSign(0);
	                        newObj.setClientId(enter.getStockOrgId());
	                        newObj.setClientName(enter.getStockOrg());
	                        newObj.setBillNum(getObj.getBillNum());
	                        newObj.setFeeId(feeMap.get("ID").toString());//fee.getId().toString()
	                        newObj.setFeeCode(feeMap.get("CODE").toString());//fee.getCode()
	                        newObj.setFeeName(feeMap.get("NAME_C").toString());//fee.getNameC()
	                        newObj.setUnitPrice(getObj.getUnitPrice()!=null?getObj.getUnitPrice():1d);
	                        newObj.setTotelPrice(getObj.getTotelPrice()!=null?getObj.getTotelPrice():1d);
	                        newObj.setNum(BigDecimalUtil.div(newObj.getTotelPrice(), newObj.getUnitPrice()));
	                        newObj.setTaxRate(getObj.getTaxRate());
	                        newObj.setHelpPay("否".equals(getObj.getHelpPay())? 0 : 1 );
	                        newObj.setRemark(getObj.getRemark());
	                        newObj.setIfReverse("否".equals(getObj.getIfReverse())?0:1);
	                        //bisPayInfoService.save(newObj);
	                        this.create(newObj);
                		}
                    }//end for
            		return "success";
                } else {
                	//验证不合格
                   if(noBill1.equals("")){
                	   noFee1 = noFee1.substring(0, noFee1.length() - 1);
                	   return noFee1 + "a";
                   }else if(noFee1.equals("")){
                	   noBill1 = noBill1.substring(0,noBill1.length() - 1);
                	   return noBill1 + "b";
                   }else{
                	   noFee1 = noFee1.substring(0, noFee1.length() - 1);
                	   noBill1 = noBill1.substring(0,noBill1.length() - 1);
                	   return noFee1 +"/"+noBill1;
                   }
                }
	        }else{
	        	//list为空
	        	return "f";
	        }//end if list
        } catch (Exception e) {
            e.printStackTrace();
            return "f";
        }

        

    }
}
