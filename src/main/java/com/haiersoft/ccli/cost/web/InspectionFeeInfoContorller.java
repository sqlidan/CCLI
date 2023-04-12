package com.haiersoft.ccli.cost.web;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.cost.service.InspectionFeeInfoService;
import com.haiersoft.ccli.cost.service.InspectionFeeService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.cost.entity.BisInspectionFee;
import com.haiersoft.ccli.cost.entity.BisInspectionFeeInfo;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.FeeCode;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.FeeCodeService;
import com.haiersoft.ccli.base.service.TaxRateService;
/**
 * EnterStevedoringcontroller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/inspecioninfo")
public class InspectionFeeInfoContorller extends BaseController {
	
	@Autowired
	private InspectionFeeService inspectionFeeService;
	@Autowired
	private InspectionFeeInfoService inspectionFeeInfoService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private TaxRateService taxRateService;//汇率
	@Autowired
	private FeeCodeService feeCodeService;//费目
	@Autowired
	private EnterStockService enterStockService;//费目
	
	/**
	 * 
	 * @author pyl
	 * @Description: 费用查验明细查询
	 * @date 2016年6月25日 下午2:06:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{feeId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("feeId") String feeId) {
		Page<BisInspectionFeeInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_feeId", feeId);
		filters.add(filter);
		page = inspectionFeeInfoService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 添加费用查验单明细跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create/{feeId}/{billNum}", method = RequestMethod.GET)
	public String createForm(@PathVariable("feeId") String feeId,@PathVariable("billNum") String billNum,Model model) {
		billNum = billNum.replaceAll("\\*", "/");//20170817 解决前台提单号带斜杠的问题。yhn
		model.addAttribute("feeId",feeId);
		model.addAttribute("billNum",billNum);
		model.addAttribute("info", new BisInspectionFeeInfo());
		model.addAttribute("actions", "create");
		return "cost/inspectionFee/inspectionFeeInfo";
	}
	
	/**
	 * 添加费用查验单明细
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisInspectionFeeInfo infoObj) {
		User user = UserUtil.getCurrentUser();
		BisInspectionFee inspection = inspectionFeeService.get(infoObj.getFeeId());
		String standingIds="";
		String bills = "";
		Double amount = 0.00;
		Integer ctn = 0;
		if("1".equals(inspection.getIfLx())){
			//如果零星客户
			infoObj.setIfPlug(null != infoObj.getIfPlug()?1:0);
			infoObj.setIfHang(null !=  infoObj.getIfHang()?1:0);
			infoObj.setIfField(null !=  infoObj.getIfField()?1:0);
			infoObj.setIfHanding(null !=  infoObj.getIfHanding()?1:0);
		}else{
			Map<String,Object> parme = new HashMap<String,Object>();
			parme.put("itemNum", infoObj.getBillNum());
			parme.put("delFlag", "0");
			List<BisEnterStock> stockList=enterStockService.findEnterList(parme);
			if(null==stockList){
				return "success";
			}
			BisEnterStock stock = enterStockService.findEnterList(parme).get(0);
			BisStandingBook standing = new BisStandingBook();
			BaseClientInfo client = clientService.get(Integer.valueOf(stock.getStockOrgId()));
			if(null!=inspection.getCheckDate() && null!=client.getCheckDay()){
				standing.setBillDate(DateUtils.ifBillDay(inspection.getCheckDate(),client.getCheckDay()));
			}
			standing.setLinkId(stock.getLinkId());
			standing.setCrkSign(1);
			standing.setContactType(1);
			standing.setCustomsNum(inspection.getClientId());
			standing.setCustomsName(inspection.getClientName());
			standing.setBillNum(infoObj.getBillNum());
			standing.setIfReceive(1);
			standing.setInputPerson(user.getName());
			standing.setInputDate(new Date());
			standing.setExamineSign(1);
			//水产平台服务费或冻肉平台服务费或水果平台服务费
			if(infoObj.getCheckStandard() > 0.00){
				BisStandingBook standingA = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingA);
				standingA.setStandingNum(standingBookService.getSequenceId());
				FeeCode feeCode=feeCodeService.getFeeCode("cyzy");
				//1:水产 2：冻肉  3:水果 4:其他
				//2019-1-22统一使用查验作业费
				switch (inspection.getCheckType()) {
					case "1":
						standingA.setBisType("9");
						break;
					case "2":
						standingA.setBisType("10");
						break;
					case "3":
						standingA.setBisType("18");
						break;
					case "4":
						standingA.setBisType("26");
						break;
				}
				standingA.setFeeCode(feeCode.getCode());
				standingA.setFeeName(feeCode.getNameC());
				standingA.setNum(1.00);
				standingA.setPrice(infoObj.getCheckStandard());
				standingA.setReceiveAmount(infoObj.getCheckStandard());
				standingA.setCurrency(feeCode.getCurrencyType().toString());
				standingA.setExchangeRate(taxRateService.getCurrency(standingA.getCurrency()).get(0).getExchangeRate());
				standingA.setShouldRmb(standingA.getReceiveAmount() * standingA.getExchangeRate());
				standingA.setStandingCode(StringUtils.numToCode(String.valueOf(standingA.getStandingNum()),new Date()));
				standingIds += standingA.getStandingNum().toString()+",";
				standingBookService.save(standingA);
			}
			//插电费目
			if(null != infoObj.getIfPlug()){
				BisStandingBook standingC = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingC);
				standingC.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(11);
				FeeCode feeCode = feeCodeList.get(0);
				standingC.setFeeCode(feeCode.getCode());
				standingC.setFeeName(feeCode.getNameC());
				standingC.setNum(infoObj.getPlugDays().doubleValue());
				standingC.setPrice(infoObj.getPlugUnit());
				standingC.setReceiveAmount(infoObj.getPlugPrice());
				standingC.setCurrency(feeCode.getCurrencyType().toString());
				standingC.setExchangeRate(taxRateService.getCurrency(standingC.getCurrency()).get(0).getExchangeRate());
				standingC.setBisType("11");
				standingC.setShouldRmb(standingC.getReceiveAmount() * standingC.getExchangeRate());
				standingC.setStandingCode(StringUtils.numToCode(String.valueOf(standingC.getStandingNum()),new Date()));
				standingIds += standingC.getStandingNum().toString()+",";
				standingBookService.save(standingC);
			}else{
				infoObj.setIfPlug(0);
				infoObj.setPlugPrice(0.00);
			}
			
			//吊箱费目
			if( null !=  infoObj.getIfHang() ){
				BisStandingBook standingD = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingD);
				standingD.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(12);
				FeeCode feeCode = feeCodeList.get(0);
				standingD.setFeeCode(feeCode.getCode());
				standingD.setFeeName(feeCode.getNameC());
				standingD.setNum(infoObj.getHangTimes().doubleValue());
				standingD.setPrice(infoObj.getHangUnit());
				standingD.setReceiveAmount(infoObj.getHangPrice());
				standingD.setCurrency(feeCode.getCurrencyType().toString());
				standingD.setExchangeRate(taxRateService.getCurrency(standingD.getCurrency()).get(0).getExchangeRate());
				standingD.setBisType("12");
				standingD.setShouldRmb(standingD.getReceiveAmount() * standingD.getExchangeRate());
				standingD.setStandingCode(StringUtils.numToCode(String.valueOf(standingD.getStandingNum()),new Date()));
				standingIds += standingD.getStandingNum().toString()+",";
				standingBookService.save(standingD);
			}else{
				infoObj.setIfHang(0);
				infoObj.setHangPrice(0.00);
			}
			
			//场地费目
			if( null !=  infoObj.getIfField()){
				BisStandingBook standingE = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingE);
				standingE.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(13);
				FeeCode feeCode = feeCodeList.get(0);
				standingE.setFeeCode(feeCode.getCode());
				standingE.setFeeName(feeCode.getNameC());
				standingE.setNum(infoObj.getFieldDays().doubleValue());
				standingE.setPrice(infoObj.getFieldUnit());
				standingE.setReceiveAmount(infoObj.getFieldPrice());
				standingE.setCurrency(feeCode.getCurrencyType().toString());
				standingE.setExchangeRate(taxRateService.getCurrency(standingE.getCurrency()).get(0).getExchangeRate());
				standingE.setBisType("13");
				standingE.setShouldRmb(standingE.getReceiveAmount() * standingE.getExchangeRate());
				standingE.setStandingCode(StringUtils.numToCode(String.valueOf(standingE.getStandingNum()),new Date()));
				standingIds += standingE.getStandingNum().toString()+",";
				standingBookService.save(standingE);
			}else{
				infoObj.setIfField(0);
				infoObj.setFieldPrice(0.00);
			}
			
			//搬倒费目
			if(null !=  infoObj.getIfHanding()){
				BisStandingBook standingF = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingF);
				standingF.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(14);
				FeeCode feeCode = feeCodeList.get(0);
				standingF.setFeeCode(feeCode.getCode());
				standingF.setFeeName(feeCode.getNameC());
				standingF.setNum(1.00);
				standingF.setPrice(infoObj.getHandingUnit());
				standingF.setReceiveAmount(infoObj.getHandingPrice());
				standingF.setCurrency(feeCode.getCurrencyType().toString());
				standingF.setExchangeRate(taxRateService.getCurrency(standingF.getCurrency()).get(0).getExchangeRate());
				standingF.setBisType("14");
				standingF.setShouldRmb(standingF.getReceiveAmount() * standingF.getExchangeRate());
				standingF.setStandingCode(StringUtils.numToCode(String.valueOf(standingF.getStandingNum()),new Date()));
				standingIds += standingF.getStandingNum().toString()+",";
				standingBookService.save(standingF);
			}else{
				infoObj.setIfHanding(0);
				infoObj.setHandingPrice(0.00);
			}
			
			if(!"".equals(standingIds)){
				standingIds=standingIds.substring(0, standingIds.length()-1);
			}
		}
		infoObj.setStandingNum(standingIds);
		inspectionFeeInfoService.save(infoObj);
		List<BisInspectionFeeInfo> infoList = inspectionFeeInfoService.findByFeeId(infoObj.getFeeId());
		for(BisInspectionFeeInfo info:infoList){
			bills += info.getCtnNum()+",";
			ctn = ctn+1;
			amount += info.getCheckStandard();
		    amount +=(1==infoObj.getIfPlug()?info.getPlugPrice():0);
		    amount +=(1==infoObj.getIfHang()?info.getHangPrice():0);
		    amount +=(1==infoObj.getIfField()?info.getFieldPrice():0);
		    amount +=(1==infoObj.getIfHanding()?info.getHandingPrice():0);
		}
		if(!"".equals(bills)){
			bills=bills.substring(0, bills.length()-1);
		}
		inspection.setCtnNum(bills);
		inspection.setCtnAmount(ctn);
		inspection.setCostAmount(amount);
		inspectionFeeService.merge(inspection);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除查验明细
	 * @date 2016年6月26日 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteinfo/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		Double amount = 0.00;
		for(int i = 0;i<ids.size();i++){
			BisInspectionFeeInfo info = inspectionFeeInfoService.get(ids.get(i));
			BisInspectionFee inspectionFee=inspectionFeeService.get(info.getFeeId());
            if(!"1".equals(inspectionFee.getIfLx())){
				if(!StringUtils.isNull(info.getStandingNum())){
					String[] standingIds= info.getStandingNum().split(",");
					int size=standingIds.length;
					for(int j=0;j<size;j++){
						BisStandingBook obj=standingBookService.get(Integer.valueOf(standingIds[j]));
						if(obj!=null){
							standingBookService.delete(Integer.valueOf(standingIds[j]));
						}
					}
				}
            }
    	    amount+=info.getCheckStandard();
    		amount+=(null!=info.getIfPlug()&&1==info.getIfPlug()?info.getPlugPrice():0);
    		amount+=(null!=info.getIfHang()&&1==info.getIfHang()?info.getHangPrice():0);
    		amount+=(null!=info.getIfField()&&1==info.getIfField()?info.getFieldPrice():0);
    		amount+=(null!=info.getIfHanding()&&1==info.getIfHanding()?info.getHandingPrice():0);
    		//更新主表的总金额
            if(inspectionFee.getCtnNum().indexOf(info.getCtnNum()+",")!=-1){
            	inspectionFee.setCtnNum(inspectionFee.getCtnNum().replace(info.getCtnNum()+",",""));
            }
            if(inspectionFee.getCtnNum().indexOf(","+info.getCtnNum())!=-1){
            	inspectionFee.setCtnNum(inspectionFee.getCtnNum().replace(","+info.getCtnNum(),""));
            }
            if(inspectionFee.getCtnNum().indexOf(info.getCtnNum())!=-1){
            	inspectionFee.setCtnNum(inspectionFee.getCtnNum().replace(info.getCtnNum(),""));
            }
    		inspectionFee.setCtnAmount(inspectionFee.getCtnAmount()-1);
    		inspectionFee.setCostAmount(inspectionFee.getCostAmount()-amount);
            inspectionFeeInfoService.delete((ids.get(i)));
    		inspectionFeeService.merge(inspectionFee);
		}
		return "success";
	}
	
	/**
	 * 修改查验明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		BisInspectionFeeInfo info = inspectionFeeInfoService.get(id);
		model.addAttribute("info", info);
		model.addAttribute("feeId", info.getFeeId());
		model.addAttribute("billNum",info.getBillNum());
		model.addAttribute("actions", "update");
		return "cost/inspectionFee/inspectionFeeInfo";
	}

	/**
	 * 修改查验明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BisInspectionFeeInfo info,Model model) {
		BisInspectionFee inspectionFee=inspectionFeeService.get(info.getFeeId());
		if(!"1".equals(inspectionFee.getIfLx())){
			String[] standingIds= info.getStandingNum().split(",");
			int size=standingIds.length;
			for(int j=0;j<size;j++){
				if(null==standingIds[j]||"".equals(standingIds[j])){
					continue;
				}
				BisStandingBook standingBook=standingBookService.get(Integer.valueOf(standingIds[j]));
				if(null!=standingBook){
				  standingBookService.delete(standingBook);
				}
			}
		}
		this.updateNew(info);
		return "success";
	}

	private void updateNew(BisInspectionFeeInfo infoObj) {
		User user = UserUtil.getCurrentUser();
		BisInspectionFee inspection = inspectionFeeService.get(infoObj.getFeeId());
		String standingIds="";
		String bills = "";
		Double amount = 0.00;
		Integer ctn = 0;
		if("1".equals(inspection.getIfLx())){
			//如果零星客户
			infoObj.setIfPlug((null != infoObj.getIfPlug()&&1==infoObj.getIfPlug())?1:0);
			infoObj.setIfHang((null !=  infoObj.getIfHang()&&1==infoObj.getIfHang())?1:0);
			infoObj.setIfField((null !=  infoObj.getIfField()&&1==infoObj.getIfField())?1:0);
			infoObj.setIfHanding((null !=  infoObj.getIfHanding()&&1==infoObj.getIfHanding())?1:0);
		}else{
			BisStandingBook standing = new BisStandingBook();
			Map<String,Object> parme = new HashMap<String,Object>();
			parme.put("itemNum", infoObj.getBillNum());
			parme.put("delFlag", "0");
			BisEnterStock stock = enterStockService.findEnterList(parme).get(0);
			BaseClientInfo client = clientService.get(Integer.valueOf(stock.getStockOrgId()));
			if(null!=inspection.getCheckDate() && null!=client.getCheckDay()){
				standing.setBillDate(DateUtils.ifBillDay(inspection.getCheckDate(),client.getCheckDay()));
			}
			standing.setLinkId(stock.getLinkId());
			standing.setCrkSign(1);
			standing.setContactType(1);
			standing.setCustomsNum(inspection.getClientId());
			standing.setCustomsName(inspection.getClientName());
			standing.setBillNum(infoObj.getBillNum());
			standing.setIfReceive(1);
			standing.setInputPersonId(user.getId()+"");
			standing.setInputPerson(user.getName());
			standing.setInputDate(new Date());
			//水产平台服务费或冻肉平台服务费
			if(infoObj.getCheckStandard() > 0.00){
				BisStandingBook standingA = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingA);
				standingA.setStandingNum(standingBookService.getSequenceId());
				//List<FeeCode> feeCodeList=null;
				//FeeCode feeCode=null;
				FeeCode feeCode=feeCodeService.getFeeCode("cyzy");
				//1:水产 2：冻肉  3:水果 4:其他
				switch (inspection.getCheckType()) {
					case "1":
						//feeCodeList = feeCodeService.findByType(9);
						//feeCode = feeCodeList.get(0);
						standingA.setBisType("9");
						break;
					case "2":
						//feeCodeList = feeCodeService.findByType(10);
						//feeCode = feeCodeList.get(0);
						standingA.setBisType("10");
						break;
					case "3":
						//feeCodeList = feeCodeService.findByType(18);
						//feeCode = feeCodeList.get(0);
						standingA.setBisType("18");
						break;
					case "4":
						//feeCodeList = feeCodeService.findByType(26);
						//feeCode = feeCodeList.get(0);
						standingA.setBisType("26");
						break;
				}
				standingA.setFeeCode(feeCode.getCode());
				standingA.setFeeName(feeCode.getNameC());
				standingA.setNum(1.00);
				standingA.setPrice(infoObj.getCheckStandard());
				standingA.setReceiveAmount(infoObj.getCheckStandard());
				standingA.setCurrency(feeCode.getCurrencyType().toString());
				standingA.setExchangeRate(taxRateService.getCurrency(standingA.getCurrency()).get(0).getExchangeRate());
				standingA.setShouldRmb(standingA.getReceiveAmount() * standingA.getExchangeRate());
				standingA.setStandingCode(StringUtils.numToCode(String.valueOf(standingA.getStandingNum()),new Date()));
				standingIds += standingA.getStandingNum().toString()+",";
				standingBookService.merge(standingA);
			}
			//插电费目
			if(null != infoObj.getIfPlug()&&0!=infoObj.getIfPlug()){
				BisStandingBook standingC = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingC);
				standingC.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(11);
				FeeCode feeCode = feeCodeList.get(0);
				standingC.setFeeCode(feeCode.getCode());
				standingC.setFeeName(feeCode.getNameC());
				standingC.setNum(infoObj.getPlugDays()!=null?infoObj.getPlugDays().doubleValue():null);
				standingC.setPrice(infoObj.getPlugUnit());
				standingC.setReceiveAmount(infoObj.getPlugPrice());
				standingC.setCurrency(feeCode.getCurrencyType().toString());
				standingC.setExchangeRate(taxRateService.getCurrency(standingC.getCurrency()).get(0).getExchangeRate());
				standingC.setBisType("11");
				standingC.setShouldRmb(standingC.getReceiveAmount() * standingC.getExchangeRate());
				standingC.setStandingCode(StringUtils.numToCode(String.valueOf(standingC.getStandingNum()),new Date()));
				standingIds += standingC.getStandingNum().toString()+",";
				standingBookService.merge(standingC);
			}else{
				infoObj.setIfPlug(0);
				infoObj.setPlugPrice(0.00);
			}
			
			//吊箱费目
			if( null !=  infoObj.getIfHang()&&0!=infoObj.getIfHang()){
				BisStandingBook standingD = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingD);
				standingD.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(12);
				FeeCode feeCode = feeCodeList.get(0);
				standingD.setFeeCode(feeCode.getCode());
				standingD.setFeeName(feeCode.getNameC());
				standingD.setNum(null!=infoObj.getHangTimes()?infoObj.getHangTimes().doubleValue():0);
				standingD.setPrice(infoObj.getHangUnit());
				standingD.setReceiveAmount(infoObj.getHangPrice());
				standingD.setCurrency(feeCode.getCurrencyType().toString());
				standingD.setExchangeRate(taxRateService.getCurrency(standingD.getCurrency()).get(0).getExchangeRate());
				standingD.setBisType("12");
				standingD.setShouldRmb(standingD.getReceiveAmount() * standingD.getExchangeRate());
				standingD.setStandingCode(StringUtils.numToCode(String.valueOf(standingD.getStandingNum()),new Date()));
				standingIds += standingD.getStandingNum().toString()+",";
				standingBookService.merge(standingD);
			}else{
				infoObj.setIfHang(0);
				infoObj.setHangPrice(0.00);
			}
			
			//场地费目
			if( null !=  infoObj.getIfField()&&0!=infoObj.getIfField()){
				BisStandingBook standingE = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingE);
				standingE.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(13);
				FeeCode feeCode = feeCodeList.get(0);
				standingE.setFeeCode(feeCode.getCode());
				standingE.setFeeName(feeCode.getNameC());
				standingE.setNum(infoObj.getFieldDays().doubleValue());
				standingE.setPrice(infoObj.getFieldUnit());
				standingE.setReceiveAmount(infoObj.getFieldPrice());
				standingE.setCurrency(feeCode.getCurrencyType().toString());
				standingE.setExchangeRate(taxRateService.getCurrency(standingE.getCurrency()).get(0).getExchangeRate());
				standingE.setBisType("13");
				standingE.setShouldRmb(standingE.getReceiveAmount() * standingE.getExchangeRate());
				standingE.setStandingCode(StringUtils.numToCode(String.valueOf(standingE.getStandingNum()),new Date()));
				standingIds += standingE.getStandingNum().toString()+",";
				standingBookService.merge(standingE);
			}else{
				infoObj.setIfField(0);
				infoObj.setFieldPrice(0.00);
			}
			
			//搬倒费目
			if(null !=  infoObj.getIfHanding()&&0!=infoObj.getIfHanding()){
				BisStandingBook standingF = new BisStandingBook();
				BeanUtils.copyProperties(standing, standingF);
				standingF.setStandingNum(standingBookService.getSequenceId());
				List<FeeCode> feeCodeList = feeCodeService.findByType(14);
				FeeCode feeCode = feeCodeList.get(0);
				standingF.setFeeCode(feeCode.getCode());
				standingF.setFeeName(feeCode.getNameC());
				standingF.setNum(1.00);
				standingF.setPrice(infoObj.getHandingUnit());
				standingF.setReceiveAmount(infoObj.getHandingPrice());
				standingF.setCurrency(feeCode.getCurrencyType().toString());
				standingF.setExchangeRate(taxRateService.getCurrency(standingF.getCurrency()).get(0).getExchangeRate());
				standingF.setBisType("14");
				standingF.setShouldRmb(standingF.getReceiveAmount() * standingF.getExchangeRate());
				standingF.setStandingCode(StringUtils.numToCode(String.valueOf(standingF.getStandingNum()),new Date()));
				standingIds += standingF.getStandingNum().toString()+",";
				standingBookService.merge(standingF);
			}else{
				infoObj.setIfHanding(0);
				infoObj.setHandingPrice(0.00);
			}
			
			if(!"".equals(standingIds)){
				standingIds=standingIds.substring(0, standingIds.length()-1);
			}
		}
		infoObj.setStandingNum(standingIds);
		inspectionFeeInfoService.merge(infoObj);
		List<BisInspectionFeeInfo> infoList = inspectionFeeInfoService.findByFeeId(infoObj.getFeeId());
		for(BisInspectionFeeInfo info:infoList){
			bills += info.getCtnNum()+",";
			ctn = ctn+1;
			amount+=info.getCheckStandard();
			amount+=(null!=info.getIfPlug()&&1==info.getIfPlug()?info.getPlugPrice():0);
			amount+=(null!=info.getIfHang()&&1==info.getIfHang()?info.getHangPrice():0);
			amount+=(null!=info.getIfField()&&1==info.getIfField()?info.getFieldPrice():0);
			amount+=(null!=info.getIfHanding()&&1==info.getIfHanding()?info.getHandingPrice():0);
		}
		if(!"".equals(bills)){
			bills=bills.substring(0, bills.length()-1);
		}
		inspection.setCtnNum(bills);
		inspection.setCtnAmount(ctn);
		inspection.setCostAmount(amount);
		inspectionFeeService.merge(inspection);
	}
}
