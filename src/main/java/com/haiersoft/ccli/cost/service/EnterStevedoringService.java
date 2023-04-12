package com.haiersoft.ccli.cost.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.service.ASNService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.cost.dao.EnterStevedoringDao;
import com.haiersoft.ccli.cost.entity.BisEnterSteveDoring;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnterStevedoringService extends BaseService<BisEnterSteveDoring, Integer> {
	@Autowired
	private EnterStevedoringDao enterStevedoringDao;
	//@Autowired
	//private ClientDao clientDao;
	//@Autowired
	//private AsnActionDao asnActionDao;
	//@Autowired
	//private ASNInfoService asnInfoService;//ans明细
	//@Autowired
	//private SkuInfoService skuInfoService;//sku
	//@Autowired
	//private AsnActionService asnActionService;//ASN计费区间
	//@Autowired
	//private EnterStockService enterStockService;//入库联系单
	//@Autowired
	//private ClientService clientService;//客户
	@Autowired
	private ASNService asnService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TaxRateService taxRateService;//汇率

	@Autowired
	private StandingBookService standingBookService;//台账

	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//费用

	@Override
	public HibernateDao<BisEnterSteveDoring, Integer> getEntityDao() {
		return enterStevedoringDao;
}

	/*
	 *  根据ASN获取入库装卸单信息
	 */
	public List<Map<String, Object>> getEnterStevedoring(String asn) {
		return enterStevedoringDao.getEnterStevedoring(asn);
	}
	
	/*
	 *  根据ASN获取入库装卸单信息
	 */
	public List<BisEnterSteveDoring> getSteveByAsn(String asn) {
		return enterStevedoringDao.findBy("asnId", asn);
	}
	
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String asn) {
		enterStevedoringDao.updateState(asn);
	}
	
	/*
	 * 更新 取消完成 状态
	 */
	public void updateState2(String asn) {
		enterStevedoringDao.updateState2(asn);
	}
 
	/*
	 * 获得sequence
	 */
	public Integer getSequenceId() {
		return enterStevedoringDao.getSequenceId("SEQ_STANDING_BOOK");
		
	}

	@Transactional(readOnly = false)
	public String addStandBook(String asn){

		User user = UserUtil.getCurrentUser();
		List<BisEnterSteveDoring>  enterList=this.getSteveByAsn(asn);
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
			this.update(bisEnterSteveDoring);
		}
		//更新 是否完成 状态
		//enterStevedoringService.updateState(asn);
		return "success";
	}
}
