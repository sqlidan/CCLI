package com.haiersoft.ccli.cost.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.StoreFeeDao;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.OutStockService;
import com.haiersoft.ccli.wms.service.TransferService;
/**
 * 存储费
 * @author LZG
 *
 */
@Service
public class StoreFeeService extends BaseService<Object, String> {

	@Autowired
	private  StoreFeeDao stortFeeDao;//存储费
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;//费用方案明细
	@Autowired
	private StandingBookService standingBookService;//台账明细
	@Autowired
	private EnterStockService enterStockService;//入库联系单
	@Autowired
	private OutStockService outStockService;//出库联系单
	@Autowired
	private TransferService transferService;//货转
	@Autowired
	private TaxRateService taxRateService;//汇率
	@Override
	public HibernateDao<Object, String> getEntityDao() {
		return stortFeeDao;
	}
	
	/**
	 * 查找需要进行存储费计算的记录
	 * @param nDay 日 当天 
	 * @return
	 */
	@Transactional
	public void creteStortFeeList(int nDay){
		if(nDay>0){
			List<Map<String,Object>> getList=stortFeeDao.findStortFeeList(nDay);
			if(getList!=null && getList.size()>0){
				String feeId=null;//费用方案
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("feeType","2");
				Double unit=0d;//单价 
				String billing=null;//计费方式：1按箱，2按毛重，3按净重，4按托
				double sumMoney=0d;
				BisStandingBook standingBook =null;//台账对象
				String linkNum=null;//联系单
				BisEnterStock enterStockObj=null;
				BisOutStock outStock=null;
				BisTransferStock transferStock=null;
				Date nowDate=new Date() ;
				BaseTaxRate taxRate=null;
				Map<String,Object> getUpMap=null;
				for(Map<String,Object> getMap:getList){
					feeId=getMap.get("FEE_PLAN_ID").toString();
					List<ExpenseSchemeInfo> exList =null;
					ExpenseSchemeInfo getSchemeInfo=null;//费用方案存储费明细对象
					
					if(feeId!=null && !"".equals(feeId)){
						params.put("schemeNum",feeId);
						//获取费用方案存储费明细
						exList=expenseSchemeInfoService.getFeeByName(params);
						if(exList!=null && exList.size()>0){
							getSchemeInfo=exList.get(0);
							if(getSchemeInfo!=null){
								unit=getSchemeInfo.getUnit();//价格
								billing=getSchemeInfo.getBilling();//计费方式
								if(unit>0 && billing!=null && !"".equals(billing)){
									//生成台账对象
									standingBook=new BisStandingBook();
									//计算总费用
									if("1".equals(billing.trim())){
										if(getMap.get("PICNUM")!=null){
											sumMoney=unit*Double.valueOf(getMap.get("PICNUM").toString());
											standingBook.setNum(Double.valueOf(getMap.get("PICNUM").toString()));
										}
										
									}else if("2".equals(billing.trim())){
										if(getMap.get("SUMGROSS")!=null){
											sumMoney=unit*Double.valueOf(getMap.get("SUMGROSS").toString())/1000;
											standingBook.setNum(Double.valueOf(getMap.get("SUMGROSS").toString())/1000);	
										}
										
									}else if("3".equals(billing.trim())){
										if(getMap.get("SUMNET")!=null){
											sumMoney=unit*Double.valueOf(getMap.get("SUMNET").toString())/1000;
											standingBook.setNum(Double.valueOf(getMap.get("SUMNET").toString())/1000);
										}
										
									}
									linkNum=getMap.get("LINKNUM").toString();
									if(sumMoney>0 && !"".equals(linkNum)){
										getUpMap=new HashMap<String,Object>();
										standingBook.setReceiveAmount(sumMoney);
										standingBook.setFeeCode(getSchemeInfo.getFeeCode());
										standingBook.setFeeName(getSchemeInfo.getFeeName());
										standingBook.setFeePlan(getSchemeInfo.getSchemeNum());
										standingBook.setLinkId(linkNum);
										getUpMap.put("linknum", linkNum);
										//根据联系单获取联系单对象
										if(linkNum.indexOf("E")==0){
											enterStockObj=enterStockService.get(linkNum);
											if(enterStockObj!=null && enterStockObj.getLinkId()!=null){
												standingBook.setCrkSign(1);
												standingBook.setContactType(1);
												standingBook.setBillNum(enterStockObj.getItemNum());
												standingBook.setCustomsNum(enterStockObj.getStockOrgId());
												standingBook.setCustomsName(enterStockObj.getStockOrg());
												getUpMap.put("type", 1);
											}
										}else if(linkNum.indexOf("D")==0){
											outStock=outStockService.get(linkNum);
											if(outStock!=null && outStock.getOutLinkId()!=null){
												standingBook.setCrkSign(2);
												standingBook.setContactType(2);
												standingBook.setBillNum("");
												standingBook.setCustomsNum(outStock.getSettleOrgId());
												standingBook.setCustomsName(outStock.getSettleOrg());
												getUpMap.put("type", 2);
											}
										}else if(linkNum.indexOf("T")==0){
											transferStock=transferService.get(linkNum);
											if(transferStock!=null && transferStock.getTransferId()!=null){
												standingBook.setCrkSign(3);
												standingBook.setContactType(3);
												standingBook.setBillNum("");
												standingBook.setCustomsNum(transferStock.getReceiverOrgId());
												standingBook.setCustomsName(transferStock.getReceiverOrg());
												getUpMap.put("type", 3);
											}
										}
										//根据币种获取汇率
										taxRate=taxRateService.getTaxByC(getSchemeInfo.getCurrency());
										if(taxRate!=null){
											standingBook.setExchangeRate(taxRate.getExchangeRate());
											standingBook.setShouldRmb(taxRate.getExchangeRate()*sumMoney);
										}
										
										standingBook.setIfReceive(1);
										standingBook.setPrice(unit);
										standingBook.setChargeDate(nowDate);
										standingBook.setCurrency(getSchemeInfo.getCurrency());
										standingBook.setBillDate(nowDate);
										standingBook.setCostDate(nowDate);
										standingBook.setRealAmount(0d);
										standingBook.setRealRmb(0d);
										standingBook.setRemark("系统自动生成"+DateUtils.getDate("yyyy-MM")+"存储费");
										standingBook.setInputPerson("SYSTEM");
										standingBook.setInputDate(nowDate);
										standingBook.setStandingNum(standingBookService.getSequenceId());
										standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
										standingBookService.save(standingBook);										
										stortFeeDao.upASNAction(getUpMap,nDay);
									}//end if
								}//end if 
							}//end if feeinfo
						}
					}//end if feeid
				}//end for
			}
		}
		
	}
	
}
