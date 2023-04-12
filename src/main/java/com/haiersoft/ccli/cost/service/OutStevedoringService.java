package com.haiersoft.ccli.cost.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeInfoDao;
import com.haiersoft.ccli.base.dao.TaxRateDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.OutStevedoringDao;
import com.haiersoft.ccli.cost.dao.StandingBookDao;
import com.haiersoft.ccli.cost.entity.BisOutSteveDoring;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.LoadingOrderInfoDao;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
 
@Service
public class OutStevedoringService extends BaseService<BisOutSteveDoring, Integer> {
	@Autowired
	private OutStevedoringDao outStevedoringDao;
	@Autowired
	private LoadingInfoDao loadingInfofDao;
	@Autowired
	private LoadingOrderInfoDao loadingOrderInfoDao;
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private StandingBookDao standingBookDao;
	@Autowired
	private ExpenseSchemeInfoDao expenseSchemeInfoDao;
	@Autowired
	private TaxRateDao taxRateDao;
	@Override
	public HibernateDao<BisOutSteveDoring, Integer> getEntityDao() {
		return outStevedoringDao;
	}
	
	/**
	 * 点击完成时候出现的关于生成多条一样数据调整
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public String addStandBook(String loadingNum){
		User user = UserUtil.getCurrentUser();
		List<BisOutSteveDoring>  outList =this.getSteveByLoading(loadingNum);
		for(int i=0;outList!=null&&i<outList.size();i++){
			BisOutSteveDoring bisOutSteveDoring=outList.get(i);
			//计入台账信息
			Map<String,Object> params2 = new HashMap<String,Object>();
			params2.put("loadingTruckNum", loadingNum);
			params2.put("loadingState", "2");
			BisLoadingInfo loadingInfo = loadingInfofDao.findBy(params2).get(0);
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum(bisOutSteveDoring.getClientId());
			standingBook.setCustomsName(bisOutSteveDoring.getClient());
			standingBook.setBillNum(loadingInfo.getBillNum());
			standingBook.setLinkId(loadingInfo.getOutLinkId());
			standingBook.setCrkSign(2);
			Date outDate =loadingOrderInfoDao.findBy("loadingPlanNum", loadingInfo.getLoadingPlanNum()).get(0).getLoadingTiem();
 			standingBook.setStorageDtae(outDate);
 			BaseClientInfo getClient = clientDao.find(Integer.valueOf(bisOutSteveDoring.getClientId())); 
			if(null != getClient){
				if(null!=getClient.getCheckDay()){
					standingBook.setBillDate(DateUtils.ifBillDay(loadingInfo.getLoadingTime(),getClient.getCheckDay()));
				}
			}
			standingBook.setFeePlan(bisOutSteveDoring.getFeeId());
			standingBook.setIfReceive(2);
			standingBook.setFillSign(0);
			standingBook.setChargeDate(new Date());
			standingBook.setCostDate(new Date());
			standingBook.setInputPerson(user.getName());
			standingBook.setInputPersonId(String.valueOf(user.getId()));
			standingBook.setInputDate(new Date());
			standingBook.setSplitSign(0);
			standingBook.setSettleSign(0);
			standingBook.setReconcileSign(0);
			standingBook.setContactType(2);
			standingBook.setBoxSign(0);
			standingBook.setShareSign(0);
			standingBook.setPaySign("0");
			standingBook.setChargeSign("0");
			standingBook.setExamineSign(1);
			//用于保存的新实体类
			BisStandingBook standingBookNew = null;
			//获取费用方案信息
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("schemeNum", bisOutSteveDoring.getFeeId());
			List<ExpenseSchemeInfo> expenseList = null;
			ExpenseSchemeInfo expenseInfo  = null;
			BaseTaxRate taxRate = null;
			//生成分拣台账信息
			if(bisOutSteveDoring.getSortingNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
				Integer num=standingBookDao.getSequenceId("SEQ_STANDING_BOOK");
 				standingBookNew.setStandingNum(num);
				params.put("feeType", "5");
				expenseList=expenseSchemeInfoDao.findBy(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisOutSteveDoring.getSortingNum() );
					standingBookNew.setReceiveAmount(bisOutSteveDoring.getSortingNum() * expenseInfo.getUnit() );
					taxRate=taxRateDao.findUniqueBy("currencyType", expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("5");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisOutSteveDoring.setDroStanindBookIds(num.toString());
					standingBookDao.merge(standingBookNew);
				}			
			}
			//生成人工台账信息
			if(bisOutSteveDoring.getManNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
				Integer num=standingBookDao.getSequenceId("SEQ_STANDING_BOOK");
 				standingBookNew.setStandingNum(num);
				params.put("feeType", "6");
				expenseList=expenseSchemeInfoDao.findBy(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisOutSteveDoring.getManNum() );
					standingBookNew.setReceiveAmount(bisOutSteveDoring.getManNum() * expenseInfo.getUnit() );
					taxRate=taxRateDao.findUniqueBy("currencyType", expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("6");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisOutSteveDoring.setDroStanindBookIds(num.toString());
					standingBookDao.merge(standingBookNew);
				}			
			}
			//生成缠膜台账信息
			if(bisOutSteveDoring.getWrapNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
				Integer num=standingBookDao.getSequenceId("SEQ_STANDING_BOOK");
 				standingBookNew.setStandingNum(num);
				params.put("feeType", "7");
				expenseList=expenseSchemeInfoDao.findBy(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisOutSteveDoring.getWrapNum() );
					standingBookNew.setReceiveAmount(bisOutSteveDoring.getWrapNum() * expenseInfo.getUnit() );
					taxRate=taxRateDao.findUniqueBy("currencyType", expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("7");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisOutSteveDoring.setDroStanindBookIds(num.toString());
					standingBookDao.merge(standingBookNew);
				}
             }
			//生成打包台账信息
			if(bisOutSteveDoring.getPackNum() != 0){
				standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew);
				Integer num=standingBookDao.getSequenceId("SEQ_STANDING_BOOK");
 				standingBookNew.setStandingNum(num);
				//获取sequence
				params.put("feeType", "8");
				expenseList=expenseSchemeInfoDao.findBy(params);
				if(!expenseList.isEmpty()){
					expenseInfo=expenseList.get(0);
					standingBookNew.setFeeCode(expenseInfo.getFeeCode());
					standingBookNew.setPrice(expenseInfo.getUnit());
					standingBookNew.setNum(bisOutSteveDoring.getPackNum() );
					standingBookNew.setReceiveAmount(bisOutSteveDoring.getPackNum() * expenseInfo.getUnit() );
					taxRate=taxRateDao.findUniqueBy("currencyType", expenseInfo.getCurrency());
					standingBookNew.setExchangeRate(taxRate.getExchangeRate());
					standingBookNew.setCurrency(expenseInfo.getCurrency());
					standingBookNew.setShouldRmb(standingBookNew.getReceiveAmount() * taxRate.getExchangeRate());
					standingBookNew.setBisType("8");
					standingBookNew.setFeeName(expenseInfo.getFeeName());
					standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
					bisOutSteveDoring.setDroStanindBookIds(num.toString());
					standingBookDao.merge(standingBookNew);
				}
	        }
			bisOutSteveDoring.setIfOk(1);
			outStevedoringDao.merge(bisOutSteveDoring);
		}
		return "success";
	}
	/*
	 * 根据装车号获取出库装车单信息
	 */
	public List<Map<String, Object>> getLoadingInfoByNum(String loadingNum) {
		return outStevedoringDao.getLoadingInfoByNum(loadingNum);
	}

	/*
	 *  根据装车号获取出装卸单信息
	 */
	public List<Map<String, Object>> getOutStevedoring(String loadingNum) {
		return outStevedoringDao.getOutStevedoring(loadingNum);
	}

	/*
	 *  根据ASN获取入库装卸单信息
	 */
	public List<BisOutSteveDoring> getSteveByLoading(String loadingNum) {
		return outStevedoringDao.findBy("loadingNum", loadingNum);
	}

	/**
	 * 统一更新完成状态跟台账id
	 * @param updateIfok
	 */
	public void updateIfok(BisOutSteveDoring bisOutSteveDoring) {
		outStevedoringDao.updateIfok(bisOutSteveDoring);
	}
	/*
	 * 更新 是否完成 状态
	 */
	public void updateState(String loadingNum) {
		outStevedoringDao.updateState(loadingNum);
	}
	
	/*
	 * 更新 取消完成 状态
	 */
	public void updateState2(String loadingNum) {
		outStevedoringDao.updateState2(loadingNum);
	}
 
}
