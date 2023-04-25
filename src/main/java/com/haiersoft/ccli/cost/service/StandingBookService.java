package com.haiersoft.ccli.cost.service;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeDao;
import com.haiersoft.ccli.base.dao.ExpenseSchemeInfoDao;
import com.haiersoft.ccli.base.dao.FeeCodeDao;
import com.haiersoft.ccli.base.dao.TaxRateDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.entity.FeeCode;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.FeeCodeService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.StandingBookDao;
import com.haiersoft.ccli.cost.entity.BisPayInfo;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.dao.ASNDao;
import com.haiersoft.ccli.wms.dao.EnterStockDao;
import com.haiersoft.ccli.wms.dao.EnterStockInfoDao;
import com.haiersoft.ccli.wms.dao.LoadingInfoDao;
import com.haiersoft.ccli.wms.dao.OutStockDao;
import com.haiersoft.ccli.wms.dao.TrayInfoDao;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.service.EnterStockService;
/**
 * 
 * @author Connor.M
 * @ClassName: StandingBookService
 * @Description: 费目台账 Service
 * @date 2016年4月7日 下午6:51:31
 */
@Service
@Transactional(readOnly = true)
public class StandingBookService extends BaseService<BisStandingBook, Integer> {
	@Autowired
	private TaxRateService taxRateService;//汇率
	@Autowired
	private StandingBookDao standingBookDao;
	@Autowired
	private ClientService clientService;
	@Autowired
	private EnterStockDao enterStockDao;
	@Autowired
	private EnterStockInfoDao enterStockInfoDao;
	@Autowired
	private OutStockDao outStockDao;
	@Autowired
	private ExpenseSchemeDao expenseSchemeDao;
	@Autowired
	private ExpenseSchemeInfoDao expenseSchemeInfoDao;
	@Autowired
	private TaxRateDao taxRateDao;
	@Autowired
	private FeeCodeDao feeCodeDao;
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private TrayInfoDao trayInfoDao;
	@Autowired
	private LoadingInfoDao loadingInfoDao;
	@Autowired
	private BisPayInfoService bisPayInfoService;//业务付款单明细
	@Autowired
	private EnterStockService enterStockService;//入库
	@Autowired
	private FeeCodeService feeCodeService;//费目
	@Autowired
	private ASNDao asnDao;
	
	
	@Override
	public HibernateDao<BisStandingBook, Integer> getEntityDao() {
		return standingBookDao;
	}
	
	/**
	 * 根据提单返回台账
	 * @param billNum
	 * @return
	 */
	public List<BisStandingBook> getBisStandingBooks(String billNum,String[] feeCodes){
		return standingBookDao.getBisStandingBooks(billNum,feeCodes);
	}
	/**
	 * 
	 * @author jxk
	 * @Description:判断费用方案是否引入
	 * @date 2018年12月6日 上午10:00:35 
	 * @param customsNum
	 * @param billNum
	 * @param linkId
	 * @param feePlan
	 * @param ifReceive
	 * @param feeCode
	 * @return
	 * @throws
	 */
	public BisStandingBook getBisStandingBook(String customsNum,String billNum,String linkId,String feePlan,Integer ifReceive,String feeCode){
		return standingBookDao.getBisStandingBook(customsNum, billNum, linkId, feePlan, ifReceive, feeCode);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 添加客服费用入库  的方案费目
	 * @date 2019年1月15日 上午10:00:35 
	 * @param linkId
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String addInStandingScheme(String linkId, List<String> ids) throws Exception {
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		//获得入库联系单对象如果费用完成的不需要引入新的费用
		List<BisEnterStock> enterStockList = enterStockDao.find(Restrictions.and(Restrictions.eq("delFlag","0"),Restrictions.eq("linkId",linkId)));
		if(null==enterStockList){
			return "success";
		}
		BisEnterStock enterStock=enterStockList.get(0);
		
		if("1".equals(enterStock.getFinishFeeState())){
			return "success";
		}
		//获得客户信息
		BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(enterStock.getStockId()));
		if(null != enterStock){
			Map<String, Double> rateMap = new HashMap<String, Double>();
			//获取计算列表
			List<String> feeCodeS=feeCode(enterStock);
			//获得汇率数据
			List<BaseTaxRate> taxRates = taxRateDao.findAll();
			if(null != taxRates && taxRates.size() > 0){
				for (BaseTaxRate taxRate : taxRates){
					rateMap.put(taxRate.getCurrencyType(), taxRate.getExchangeRate());
				}
			}
			//获的库存计算量
			List<Map<String,Object>> trayList=trayInfoDao.getSum(linkId);
			//获取箱数
			List<Map<String,Object>> ctnList;
			for(String id : ids){
				//获得 费目对象
				ExpenseScheme expenseScheme = expenseSchemeDao.find(id);
				if(null != expenseScheme){
			        //根据条件进行费用的计算处理去掉仓储跟分拣以及出入库一次性费用
					List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
					filters.add(new PropertyFilter("EQS_schemeNum", id));
					filters.add(new PropertyFilter("NEQS_feeType", "2"));
					filters.add(new PropertyFilter("NEQS_feeType", "3"));
					filters.add(new PropertyFilter("NEQS_feeType", "4"));
					filters.add(new PropertyFilter("NEQS_feeType", "5"));
					List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoDao.find(filters);
					//用于判断计费方式是否是按重量收费
					if(null != schemeInfos && schemeInfos.size() > 0&&null!=feeCodeS&&feeCodeS.size()>0){
						for (ExpenseSchemeInfo schemeInfo : schemeInfos){
							//判断该费目是否已经引入
							BisStandingBook oldBook=standingBookDao.getBisStandingBook(enterStock.getStockOrgId(),enterStock.getItemNum(),linkId,id,1,schemeInfo.getFeeCode());
							if(oldBook!=null){
								//处理调整后的费用
								updateFee(enterStock,oldBook,schemeInfo,clientInfo);
								continue;
							}
							if (feeCodeS.contains(schemeInfo.getFeeCode())) {
								Double num = 0D;// 费用调整 引入方案时，数量为1
								Map<String, Object> jsmap = (null != trayList && trayList.size() > 0 ? trayList.get(0)
										: new HashMap<String, Object>());
									switch (schemeInfo.getBilling()) {
									case "1":// 按件数
										num = BigDecimalUtil.add(num, Integer
												.parseInt(jsmap.get("SL") != null ? jsmap.get("SL").toString() : "0"));
										break;
									case "2":// 按毛重吨
										num = BigDecimalUtil.add(num, Double
												.parseDouble(jsmap.get("ZMZ") != null ? jsmap.get("ZMZ").toString() : "0"));
										break;
									case "3":// 按净重吨
										num = BigDecimalUtil.add(num, Double
												.parseDouble(jsmap.get("ZJZ") != null ? jsmap.get("ZJZ").toString() : "0"));
										break;
									case "4":// 按托数
										num = Double
												.parseDouble(jsmap.get("TS") != null ? jsmap.get("TS").toString() : "0");
										break;
									case "5":// 净重千克
										num = BigDecimalUtil.add(num, Double.parseDouble(
												jsmap.get("JZKG") != null ? jsmap.get("JZKG").toString() : "0"));
										break;
									case "6":// 毛重千克
										num = BigDecimalUtil.add(num, Double.parseDouble(
												jsmap.get("MZKG") != null ? jsmap.get("MZKG").toString() : "0"));
										break;
									case "8":// 按票算（提单号）
										num = 1d;
										break;
									case "9":// 按箱量算
										// 如果是派车时候要根据费目进行获取，其他按箱号进行获取
										ctnList = enterStockInfoDao.getCtnNumList(enterStock.getLinkId(),schemeInfo.getFeeCode());
										num = (null != ctnList ? ctnList.size() : 0d);
										break;
									case "17":// 按张进行计算
										switch (schemeInfo.getFeeCode()) {
											case "dybq":// 打印标签
												num = enterStock.getPrintLableAmount() != null
														? enterStock.getPrintLableAmount() : 0;
												break;
											case "nbq":// 内标签
												num = enterStock.getPrintNLableAmount() != null
														? enterStock.getPrintNLableAmount() : 0;
												break;
											case "wbq":// 外标签
												num = enterStock.getPrintWLableAmount() != null
														? enterStock.getPrintWLableAmount() : 0;
												break;
										}
										break;
									case "21":// 按个进行计算
										switch (schemeInfo.getFeeCode()) {
										case "ztj":// 装铁架
											num = enterStock.getOutFitAmount() != null ? enterStock.getOutFitAmount() : 0;
											break;
										case "ctj":// 拆铁架
											num = enterStock.getTakeFitAmount() != null ? enterStock.getTakeFitAmount() : 0;
											break;
										}
										break;
									default:
										num = 0D;
										break;
									}
								if (num.compareTo(0d)==0) {
									continue;
								}
								BisStandingBook standingBook = new BisStandingBook();
								standingBook.setCustomsNum("1".equals(expenseScheme.getIfGet())
										? enterStock.getStockOrgId() : expenseScheme.getCustomsId());
								standingBook.setCustomsName("1".equals(expenseScheme.getIfGet())
										? enterStock.getStockOrg() : expenseScheme.getCustomsName());
								standingBook.setBillNum(enterStock.getItemNum());
								standingBook.setLinkId(enterStock.getLinkId());
								standingBook.setCrkSign(1);
								standingBook.setStorageDtae(enterStock.getRkTime() != null
										? enterStockService.getFirstTime(enterStock.getRkTime()) : null);
								standingBook.setFeeCode(schemeInfo.getFeeCode());
								standingBook.setFeeName(schemeInfo.getFeeName());
								standingBook.setFeePlan(expenseScheme.getSchemeNum());
								standingBook.setIfReceive(Integer.parseInt(expenseScheme.getIfGet()));
								standingBook.setNum(num);
								standingBook.setPrice(schemeInfo.getUnit());
								standingBook.setReceiveAmount(BigDecimalUtil.mul(num, schemeInfo.getUnit()));
								standingBook.setRealAmount(0D);
								standingBook.setCurrency(schemeInfo.getCurrency() != null ? schemeInfo.getCurrency() : "0");

								Double exchangeRate = Double.parseDouble(
										rateMap.get(schemeInfo.getCurrency() != null ? schemeInfo.getCurrency() : "0")
												.toString());
								standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
								standingBook.setShouldRmb(BigDecimalUtil.mul(standingBook.getReceiveAmount(),
										standingBook.getExchangeRate()));

								standingBook.setBisType(expenseScheme.getBisType());
								standingBook.setTaxRate(0D);
								standingBook.setFillSign(0);

								standingBook.setInputPersonId(user.getId().toString());
								standingBook.setInputPerson(user.getName());
								standingBook.setInputDate(now);

								standingBook.setChargeDate(now);
								standingBook.setCostDate(now);

								standingBook.setExamineSign(0);
								standingBook.setShareSign(0);
								standingBook.setReconcileSign(0);
								standingBook.setSettleSign(0);
								standingBook.setSplitSign(0);
								standingBook.setContactType(1);
								standingBook.setChargeSign("0");
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
								if(null !=enterStock.getRkTime()&&!"".equals(enterStock.getRkTime())){
									//获取客户信息，用于获取对账单日
									standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()));
									standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterStock.getRkTime()),clientInfo.getCheckDay()));
								}else{
									 Integer nowDate = Integer.parseInt(DateUtils.getDay());// 获得当前日
									 if (nowDate != null && nowDate > clientInfo.getCheckDay()) {
									    standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)),"yyyy-MM"));
								     } else {
										standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
									 }
								}
								standingBook.setRemark(format.format(standingBook.getBillDate())+"引入"+standingBook.getFeeName().trim());
								standingBook.setStandingNum(this.getSequenceId());
								standingBook.setStandingCode(StringUtils
										.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
								if (null != schemeInfo.getIfPay() && schemeInfo.getIfPay() == 1) {
									standingBook.setPaySign("1");
									BisStandingBook standingBookNew = new BisStandingBook();
									BeanUtils.copyProperties(standingBook, standingBookNew);
									standingBookNew.setIfReceive(3 - Integer.parseInt(expenseScheme.getIfGet()));
									standingBookNew.setCustomsNum(enterStock.getStockOrgId());
									standingBookNew.setCustomsName(enterStock.getStockOrg());
									standingBookNew.setStandingNum(this.getSequenceId());
									standingBookNew.setStandingCode(StringUtils
											.numToCode(String.valueOf(standingBookNew.getStandingNum()), new Date()));
									standingBookDao.merge(standingBookNew);
									//standingBookDao.insertStandingBook(standingBookNew);
								} else {
									standingBook.setPaySign("0");
								}
								standingBookDao.merge(standingBook);
								//standingBookDao.insertStandingBook(standingBook);
							}
						}
					}
				}
			}//end for
			//计算分拣费
			List<BisAsn> asnList=asnDao.findBy("linkId", linkId);
			for(BisAsn asnObj:asnList){
				List<Map<String, Object>> numList = new ArrayList<Map<String, Object>>();
				if ("3".equals(asnObj.getIfSecondEnter())||"1".equals(enterStock.getIfSorting())) {
					Map<String,Object> fj = new HashMap<String,Object>();
					for(String fjFeeId:ids){
						fj.put("schemeNum", fjFeeId);
						fj.put("feeType", "4");
						//获取费目方案明细
						List<ExpenseSchemeInfo> fjList = expenseSchemeInfoDao.findBy(fj);
						if(fjList.isEmpty()){
							continue;
						}else{
							//一个费用方案中不会存在既有上下限的也有不按sku个数的，如果有就是录入错误
							//处理按sku个数分拣的
							if(null!=fjList.get(0).getTermAttribute() &&"1".equals(fjList.get(0).getTermAttribute())){
								int size = fjList.size();
								Double max = 0.00;
								Double min = 0.00;
								ExpenseSchemeInfo exInfo = null;
								//获取有几个SKU
								Map<String, Object> asnparam = new HashMap<String, Object>();
								asnparam.put("asn", asnObj.getAsn());
								numList = asnDao.fjFee(asnparam);
								int num = numList.size();
								for (int i = 0; i < size; i++) {
									max = fjList.get(i).getMaxPrice();//上限
									min = fjList.get(i).getMinPrice();//下限
									if (null != max && !"".equals(max) && null != min && !"".equals(min)) {
										if (num >= min && num < max) {
											exInfo = fjList.get(i);
											break;
										}
									}
								}
								if (exInfo == null) {
									continue;
								}else{
									//判断该费目是否已经引入
									BisStandingBook oldBook=standingBookDao.getBisStandingBook(enterStock.getStockOrgId(),enterStock.getItemNum(),linkId,fjFeeId,1,exInfo.getFeeCode(),asnObj.getAsn());
									if(oldBook!=null){
										continue;
									}
									this.fjFee(asnObj,exInfo,numList,now,enterStock);
							   }
							}else if(null!=fjList.get(0).getTermAttribute() && !"1".equals(fjList.get(0).getTermAttribute())){//处理不按sku分拣的
								    //判断该费目是否已经引入
									BisStandingBook oldBook=standingBookDao.getBisStandingBook(enterStock.getStockOrgId(),enterStock.getItemNum(),linkId,fjFeeId,1,fjList.get(0).getFeeCode(),asnObj.getAsn());
									if(oldBook!=null){
										continue;
									}
									this.fjFeeByW(asnObj,fjList.get(0),now,enterStock);
							}
						}
					}//end for feeId
				}//end if 分拣
			}//end for
		}
		return "success";
	}
	
    private String updateFee(BisEnterStock enterStock,BisStandingBook standingBook,ExpenseSchemeInfo schemeInfo,BaseClientInfo clientInfo) throws Exception{
    	User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间 
		if(null !=enterStock.getRkTime()&&!"".equals(enterStock.getRkTime())){
			//获取客户信息，用于获取对账单日
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()));
			standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterStock.getRkTime()),clientInfo.getCheckDay()));
			standingBook.setRemark(format.format(standingBook.getBillDate())+"引入"+standingBook.getFeeName().trim());
		}
    	switch (schemeInfo.getFeeCode()) {
			case "dybq":// 打印标签
				if(enterStock.getPrintLableAmount().compareTo(standingBook.getNum())!=0){
					standingBook.setNum(enterStock.getPrintLableAmount());
					standingBook.setPrice(schemeInfo.getUnit());
					standingBook.setReceiveAmount(BigDecimalUtil.mul(enterStock.getPrintLableAmount(), schemeInfo.getUnit()));
					standingBook.setRealAmount(0D);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.merge(standingBook);
				}
				break;
			case "nbq":// 内标签
				if(enterStock.getPrintNLableAmount().compareTo(standingBook.getNum())!=0){
					standingBook.setNum(enterStock.getPrintNLableAmount());
				    standingBook.setPrice(schemeInfo.getUnit());
					standingBook.setReceiveAmount(BigDecimalUtil.mul(enterStock.getPrintNLableAmount(), schemeInfo.getUnit()));
					standingBook.setRealAmount(0D);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.merge(standingBook);
				}
				break;
			case "wbq":// 外标签
				if(enterStock.getPrintWLableAmount().compareTo(standingBook.getNum())!=0){
					standingBook.setNum(enterStock.getPrintWLableAmount());
				    standingBook.setPrice(schemeInfo.getUnit());
					standingBook.setReceiveAmount(BigDecimalUtil.mul(enterStock.getPrintWLableAmount(), schemeInfo.getUnit()));
					standingBook.setRealAmount(0D);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.merge(standingBook);
				}
				break;
			case "ztj":// 装铁架
				if(enterStock.getOutFitAmount().compareTo(standingBook.getNum())!=0){
					standingBook.setNum(enterStock.getOutFitAmount());
				    standingBook.setPrice(schemeInfo.getUnit());
					standingBook.setReceiveAmount(BigDecimalUtil.mul(enterStock.getOutFitAmount(), schemeInfo.getUnit()));
					standingBook.setRealAmount(0D);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.merge(standingBook);
				}
				break;
			case "ctj":// 拆铁架
				if(enterStock.getTakeFitAmount().compareTo(standingBook.getNum())!=0){
					standingBook.setNum(enterStock.getTakeFitAmount());
				    standingBook.setPrice(schemeInfo.getUnit());
					standingBook.setReceiveAmount(BigDecimalUtil.mul(enterStock.getTakeFitAmount(), schemeInfo.getUnit()));
					standingBook.setRealAmount(0D);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.merge(standingBook);
				}
				break;
			default:
				break;
		   }
       return "success";
    }
	
	private List<String> feeCode(BisEnterStock enterStock){
		List<String> feeCodes=new ArrayList<String>();
		/*if(null!=enterStock.getIfRecord()&&"1".equals(enterStock.getIfRecord())){
			feeCodes.add("sp");
		}*/
		/*if(null!=enterStock.getIfUseTruck()&&"1".equals(enterStock.getIfUseTruck())){
			feeCodes.add("tc");
			feeCodes.add("tcwq");
			feeCodes.add("tcsq");
			feeCodes.add("tssiqi");
		}*/
		if(null!=enterStock.getIfCheck()&&"1".equals(enterStock.getIfCheck())){
			feeCodes.add("cyzy");
		}
		if(null!=enterStock.getIfWeigh()&&"1".equals(enterStock.getIfWeigh())){
			feeCodes.add("cz");
		}
		if(null!=enterStock.getIfPrintLable()&&"1".equals(enterStock.getIfPrintLable())){
			feeCodes.add("dybq");
		}
		if(null!=enterStock.getIfPrintNLable()&&"1".equals(enterStock.getIfPrintNLable())){
			feeCodes.add("nbq");
		}
		if(null!=enterStock.getIfPrintWLable()&&"1".equals(enterStock.getIfPrintWLable())){
			feeCodes.add("wbq");
		}
		if(null!=enterStock.getIfOutFit()&&"1".equals(enterStock.getIfOutFit())){
			feeCodes.add("ztj");
		}
		if(null!=enterStock.getIfTakeFit()&&"1".equals(enterStock.getIfTakeFit())){
			feeCodes.add("ctj");
		}
		if(null!=enterStock.getIfWrap()&&"1".equals(enterStock.getIfWrap())){
			feeCodes.add("cm");
		}
		if(null!=enterStock.getIfBagging()&&"1".equals(enterStock.getIfBagging())){
			feeCodes.add("td");
		}
		/*if(null!=enterStock.getIfToCustoms()&&"1".equals(enterStock.getIfToCustoms())){
			feeCodes.add("bgf");
		}*/
		/*if(null!=enterStock.getIfToCiq()&&"1".equals(enterStock.getIfToCiq())){
			feeCodes.add("bj");
		}*/
		if((null!=enterStock.getIfBack()&&"1".equals(enterStock.getIfBack()))||null!=enterStock.getBackDate()){
			feeCodes.add("daox");
			feeCodes.add("dx");
		}
		return feeCodes;
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 添加财务入库费用的方案费目
	 * @date 2016年4月5日 上午10:00:35 
	 * @param linkId
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String addInStandingSchemeBatch(String linkId, List<String> ids) throws Exception {
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		//获得入库联系单对象如果费用完成的不需要引入新的费用
		List<BisEnterStock> enterStockList = enterStockDao.find(Restrictions.and(Restrictions.eq("delFlag","0"),Restrictions.eq("linkId",linkId)));
		if(null==enterStockList){
			return "success";
		}
		BisEnterStock enterStock=enterStockList.get(0);
		
		if("1".equals(enterStock.getFinishFeeState())){
			return "success";
		}
		//如果倒箱不要引入出入库一次性费用
		if("1".equals(enterStock.getIfBack())||null!=enterStock.getBackDate()){
			return "success";
		}
		//获得客户信息
		BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(enterStock.getStockId()));
		if(null != enterStock){
			Map<String, Double> rateMap = new HashMap<String, Double>();
			//获得汇率数据
			List<BaseTaxRate> taxRates = taxRateDao.findAll();
			if(null != taxRates && taxRates.size() > 0){
				for (BaseTaxRate taxRate : taxRates){
					rateMap.put(taxRate.getCurrencyType(), taxRate.getExchangeRate());
				}
			}
			//获取箱数
			List<Map<String,Object>> ctnList;
			//获的库存计算量
			List<Map<String,Object>> trayList=trayInfoDao.getSum(linkId);
			for(String id : ids){
				//获得 费目对象
				ExpenseScheme expenseScheme = expenseSchemeDao.find(id);
				if(null != expenseScheme){
					//查询方案 的费目(过滤仓储费跟分拣)
					//2019-1-14新加入调整处理，财务费用按钮跟客服的区分开来
					//财务引入处理出入库一次性降温、进口操作费、换单代理、押箱代理
					List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoDao.find(Restrictions.eq("schemeNum",id),Restrictions.or(Restrictions.eq("feeCode","crk"),Restrictions.eq("feeCode","jkcz"),Restrictions.eq("feeCode","hddl"),Restrictions.eq("feeCode","yxdl")));
					//用于判断计费方式是否是按重量收费
					if(null != schemeInfos && schemeInfos.size() > 0){
						for (ExpenseSchemeInfo schemeInfo : schemeInfos){
							//判断该费目是否已经引入
							BisStandingBook oldBook=standingBookDao.getBisStandingBook(enterStock.getStockOrgId(),enterStock.getItemNum(),linkId,id,1,schemeInfo.getFeeCode());
							if(oldBook!=null){
								continue;
							}
							BisStandingBook standingBook = new BisStandingBook();
						    standingBook.setCustomsNum("1".equals(expenseScheme.getIfGet())?enterStock.getStockOrgId():expenseScheme.getCustomsId());
						    standingBook.setCustomsName("1".equals(expenseScheme.getIfGet())?enterStock.getStockOrg():expenseScheme.getCustomsName());
							standingBook.setBillNum(enterStock.getItemNum());
							standingBook.setLinkId(enterStock.getLinkId());
							standingBook.setCrkSign(1);
						    standingBook.setStorageDtae(enterStock.getRkTime()!=null?enterStockService.getFirstTime(enterStock.getRkTime()):null);
							standingBook.setFeeCode(schemeInfo.getFeeCode());
							standingBook.setFeeName(schemeInfo.getFeeName());
							standingBook.setFeePlan(expenseScheme.getSchemeNum());
							standingBook.setIfReceive(Integer.parseInt(expenseScheme.getIfGet()));
							Double num = 0D;//费用调整 引入方案时，数量为1
							String unit=schemeInfo.getBilling().toString();                              //获取计费方式
							String[] billing={"2","3","5","6","11","12","13","14"};                      //计费方式为按重量的费目编码
							List<String> unitList=Arrays.asList(billing); 
							
							if(null != trayList && trayList.size() > 0){
								Map<String,Object> jsmap=trayList.get(0);
								switch(schemeInfo.getBilling()){
								  case "1"://按件数
									 num = BigDecimalUtil.add(num,Integer.parseInt(jsmap.get("SL")!=null?jsmap.get("SL").toString():"0"));
									 break;
								  case "2"://按毛重吨
									 num = BigDecimalUtil.add(num,Double.parseDouble(jsmap.get("ZMZ")!=null?jsmap.get("ZMZ").toString():"0"));
									 break;
								  case "3"://按净重吨
									 num = BigDecimalUtil.add(num,Double.parseDouble(jsmap.get("ZJZ")!=null?jsmap.get("ZJZ").toString():"0"));
									 break;
								  case "4"://按托数
								     num = Double.parseDouble(jsmap.get("TS")!=null?jsmap.get("TS").toString():"0");
								     break;
								  case "5"://净重千克
									 num = BigDecimalUtil.add(num,Double.parseDouble(jsmap.get("JZKG")!=null?jsmap.get("JZKG").toString():"0"));
                                     break;
								  case "6"://毛重千克
									 num = BigDecimalUtil.add(num,Double.parseDouble(jsmap.get("MZKG")!=null?jsmap.get("MZKG").toString():"0"));
	                                 break;
								  case "8":// 按票算（提单号）
										num = 1d;
										break;
								  case "9":// 按箱量算
										ctnList = enterStockInfoDao.getCtnNumList(enterStock.getLinkId(),null);
										num = (null != ctnList ? ctnList.size() : 0d);
										break;
								  default:
									 num=1D;
							         break;
								}
							}else if(null == trayList&& unitList.contains(unit)){          
								//如果没有入库，则按重量计费的费目num为0
								num=0D;
								//return "当前联系单没有入库数量,无法添加费用方案";
								continue;
							}else{
								num=1D;
								//return "当前联系单没有入库数量,无法添加费用方案";
							}
							standingBook.setNum(num);
							standingBook.setPrice(schemeInfo.getUnit());
							standingBook.setReceiveAmount(BigDecimalUtil.mul(num, schemeInfo.getUnit()));
							standingBook.setRealAmount(0D);
							standingBook.setCurrency(schemeInfo.getCurrency()!=null?schemeInfo.getCurrency():"0");
							
							Double exchangeRate = Double.parseDouble(rateMap.get(schemeInfo.getCurrency()!=null?schemeInfo.getCurrency():"0").toString());
							standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
							standingBook.setShouldRmb(BigDecimalUtil.mul(standingBook.getReceiveAmount(), standingBook.getExchangeRate()));
							
							standingBook.setBisType(expenseScheme.getBisType());
							standingBook.setTaxRate(0D);
							standingBook.setFillSign(0);
							
							standingBook.setInputPersonId(user.getId().toString());
							standingBook.setInputPerson("SYSTEM");
							standingBook.setInputDate(now);
							
							standingBook.setChargeDate(now);
							standingBook.setCostDate(now);
							
							standingBook.setExamineSign(0);
							standingBook.setShareSign(0);
							standingBook.setReconcileSign(0);
							standingBook.setSettleSign(0);
							standingBook.setSplitSign(0);
							standingBook.setContactType(1);
							standingBook.setChargeSign("0");
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
							if(null !=enterStock.getRkTime()&&!"".equals(enterStock.getRkTime())){
								//获取客户信息，用于获取对账单日
								standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()));
								standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterStock.getRkTime()),clientInfo.getCheckDay()));
							}else{
								 Integer nowDate = Integer.parseInt(DateUtils.getDay());// 获得当前日
								 if (nowDate != null && nowDate > clientInfo.getCheckDay()) {
								    standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)),"yyyy-MM"));
							     } else {
									standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
								 }
							}
							standingBook.setRemark("系统自动生成"+format.format(standingBook.getBillDate())+standingBook.getFeeName()+"费");
							standingBook.setStandingNum(this.getSequenceId());
							standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
							if(null!=schemeInfo.getIfPay() && schemeInfo.getIfPay()==1){
								standingBook.setPaySign("1");
								BisStandingBook standingBookNew = new BisStandingBook();
								BeanUtils.copyProperties(standingBook, standingBookNew); 
								standingBookNew.setIfReceive(3-Integer.parseInt(expenseScheme.getIfGet()));
								standingBookNew.setCustomsNum(enterStock.getStockOrgId()); 
								standingBookNew.setCustomsName(enterStock.getStockOrg());
								standingBookNew.setStandingNum(this.getSequenceId());
								standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
								standingBookDao.merge(standingBookNew);
								//standingBookDao.insertStandingBook(standingBookNew);
							}else{
								standingBook.setPaySign("0");
							}
							//standingBookDao.insertStandingBook(standingBook);
							standingBookDao.merge(standingBook);
						}
					}
				}
			}
		}
		return "success";
	}
	
	//生成分拣费(按SKU分拣的)
	private void fjFee(BisAsn getObj, ExpenseSchemeInfo exInfo, List<Map<String, Object>> numList,Date now,BisEnterStock enterStock) throws Exception {
		User user = UserUtil.getCurrentUser();
		String asn = getObj.getAsn();
		String linkId = getObj.getLinkId();
		int num = numList.size();
		Integer piece = 0;
		Double netWeight = 0.00;
		Double grossWeight = 0.00;
		for (int i = 0; i < num; i++) {
			piece += ((BigDecimal) numList.get(i).get("PIECE")).intValue();
			netWeight += ((BigDecimal) numList.get(i).get("NETWEIGHT")).doubleValue();
			grossWeight += ((BigDecimal) numList.get(i).get("GROSSWEIGHT")).doubleValue();
		}
		Double price = 0.00;
		Double unit = exInfo.getUnit();
		Double jsNum = 0.00;
		switch (exInfo.getBilling()) {
		   case "1":
				price = unit * piece;
				jsNum = piece.doubleValue();
				break;
			case "2":
				price = unit * grossWeight;
				jsNum = grossWeight / 1000;
				break;
			case "3":
				price = unit * netWeight;
				jsNum = netWeight / 1000;
				break;
		}
		// 将费用加入台账表
		BisStandingBook standingBook = new BisStandingBook();
 		standingBook.setStandingNum(this.getSequenceId());
 		//yhn 20180208 分拣费客户 由之前ASN取 改为从入库联系单的结算单位取
 		standingBook.setCustomsNum(enterStock.getStockOrgId());
 		standingBook.setCustomsName(enterStock.getStockOrg());
		standingBook.setBillNum(getObj.getBillNum());
		standingBook.setAsn(asn);
		standingBook.setLinkId(linkId);
		standingBook.setFeePlan(exInfo.getSchemeNum());
		standingBook.setFeeCode(exInfo.getFeeCode());
		standingBook.setFeeName(exInfo.getFeeName());
		standingBook.setCrkSign(1);
		standingBook.setInputPersonId(user.getId().toString());
		standingBook.setInputPerson(user.getName());
		standingBook.setInputDate(now);
		BaseClientInfo getClient = clientDao.find(Integer.valueOf(getObj.getStockIn())); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		if(null !=enterStock.getRkTime()&&!"".equals(enterStock.getRkTime())){
			//获取客户信息，用于获取对账单日
			standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()));
			standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterStock.getRkTime()),getClient.getCheckDay()));
		}else{
			 Integer nowDate = Integer.parseInt(DateUtils.getDay());// 获得当前日
			 if (nowDate != null && nowDate > getClient.getCheckDay()) {
			    standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)),"yyyy-MM"));
		     } else {
				standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
			 }
		}
		standingBook.setIfReceive(1);
		standingBook.setNum(jsNum);
		standingBook.setPrice(unit);
		standingBook.setChargeDate(new Date());
		standingBook.setCostDate(new Date());
		standingBook.setFillSign(0);
		standingBook.setCurrency(exInfo.getCurrency());
		BaseTaxRate taxRate = taxRateDao.findUniqueBy("currencyType",exInfo.getCurrency());
		standingBook.setExchangeRate(taxRate.getExchangeRate());
		standingBook.setExamineSign(0);
		standingBook.setBisType("4");
		if(exInfo.getBilling().equals("1")){
			standingBook.setReceiveAmount(price);
			standingBook.setShouldRmb(taxRate.getExchangeRate() * price);
		}else{
			standingBook.setReceiveAmount(price / 1000);
			standingBook.setShouldRmb(taxRate.getExchangeRate() * price / 1000);
		}
		standingBook.setReconcileSign(0);
		standingBook.setSettleSign(0);
		standingBook.setSplitSign(0);
		standingBook.setRemark("引入方案生成asn的分拣费(按SKU分拣)");
		standingBook.setContactType(1);
		standingBook.setBoxSign(0);
		standingBook.setShareSign(0);
		standingBook.setPaySign("0");
		standingBook.setChargeSign("0");
		standingBook.setRealAmount(0.00);
		standingBook.setRealRmb(0.00);
 		standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
		standingBookDao.save(standingBook);
	}
	//生成分拣费(不按SKU分拣的)
	private void fjFeeByW(BisAsn getObj, ExpenseSchemeInfo exInfo, Date now, BisEnterStock enterStock) throws Exception {
		User user = UserUtil.getCurrentUser();
		String asn = getObj.getAsn();
		String linkId = getObj.getLinkId();
		Double price = 0.00;
		Double unit = exInfo.getUnit();
		Double jsNum = 0.00;
		//获的库存计算量
		List<Map<String,Object>> trayList=trayInfoDao.getSum(linkId);
		if (null != trayList && trayList.size() > 0) {
			Map<String,Object> jsmap=trayList.get(0);
			switch(exInfo.getBilling()){
			  case "1"://按件数
				 jsNum = BigDecimalUtil.add(jsNum,Integer.parseInt(jsmap.get("SL")!=null?jsmap.get("SL").toString():"0"));
				 break;
			  case "2"://按毛重吨
				 jsNum = BigDecimalUtil.add(jsNum,Double.parseDouble(jsmap.get("ZMZ")!=null?jsmap.get("ZMZ").toString():"0"));
				 break;
			  case "3"://按净重吨
				 jsNum = BigDecimalUtil.add(jsNum,Double.parseDouble(jsmap.get("ZJZ")!=null?jsmap.get("ZJZ").toString():"0"));
				 break;
			  case "4"://按托数
				 jsNum = Double.parseDouble(jsmap.get("TS")!=null?jsmap.get("TS").toString():"0");
			     break;
			  case "5"://净重千克
				 jsNum = BigDecimalUtil.add(jsNum,Double.parseDouble(jsmap.get("JZKG")!=null?jsmap.get("JZKG").toString():"0"));
                 break;
			  case "6"://毛重千克
				 jsNum = BigDecimalUtil.add(jsNum,Double.parseDouble(jsmap.get("MZKG")!=null?jsmap.get("MZKG").toString():"0"));
                 break;
			  default:
				 jsNum=1D;
		         break;
			}
		} else {
			jsNum = 1D;
		}
		price = BigDecimalUtil.mul(jsNum, exInfo.getUnit());
		// 将费用加入台账表
		BisStandingBook standingBook = new BisStandingBook();
		standingBook.setStandingNum(this.getSequenceId());
		standingBook.setCustomsNum(enterStock.getStockOrgId());
		standingBook.setCustomsName(enterStock.getStockOrg());
		standingBook.setBillNum(getObj.getBillNum());
		standingBook.setAsn(asn);
		standingBook.setLinkId(linkId);
		standingBook.setFeePlan(exInfo.getSchemeNum());
		standingBook.setFeeCode(exInfo.getFeeCode());
		standingBook.setFeeName(exInfo.getFeeName());
		standingBook.setCrkSign(1);
		standingBook.setInputPersonId(user.getId().toString());
		standingBook.setInputPerson(user.getName());
		standingBook.setInputDate(now);
		BaseClientInfo getClient = clientDao.find(Integer.valueOf(getObj.getStockIn()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		if(null !=enterStock.getRkTime()&&!"".equals(enterStock.getRkTime())){
			//获取客户信息，用于获取对账单日
			standingBook.setStorageDtae(enterStockService.getFirstTime(enterStock.getRkTime()));
			standingBook.setBillDate(DateUtils.ifBillDay(enterStockService.getFirstTime(enterStock.getRkTime()),getClient.getCheckDay()));
		}else{
			 Integer nowDate = Integer.parseInt(DateUtils.getDay());// 获得当前日
			 if (nowDate != null && nowDate > getClient.getCheckDay()) {
			    standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)),"yyyy-MM"));
		     } else {
				standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
			 }
		}
		standingBook.setIfReceive(1);
		standingBook.setNum(jsNum);
		standingBook.setPrice(unit);
		standingBook.setChargeDate(new Date());
		standingBook.setCostDate(new Date());
		standingBook.setFillSign(0);
		standingBook.setCurrency(exInfo.getCurrency());
		BaseTaxRate taxRate = taxRateDao.findUniqueBy("currencyType", exInfo.getCurrency());
		standingBook.setExchangeRate(taxRate.getExchangeRate() != null ? taxRate.getExchangeRate() : 1D);
		standingBook.setExamineSign(0);
		standingBook.setBisType("4");
		standingBook.setReceiveAmount(price);
		standingBook.setShouldRmb(standingBook.getExchangeRate() * price);
		standingBook.setReconcileSign(0);
		standingBook.setSettleSign(0);
		standingBook.setSplitSign(0);
		standingBook.setRemark("引入方案生成asn的分拣费(不按SKU分拣)");
		standingBook.setContactType(1);
		standingBook.setBoxSign(0);
		standingBook.setShareSign(0);
		standingBook.setPaySign("0");
		standingBook.setChargeSign("0");
		standingBook.setRealAmount(0.00);
		standingBook.setRealRmb(0.00);
		standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
		standingBookDao.save(standingBook);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 出库客服费目调整 添加方案  费目
	 * @date 2019年1月30日 下午2:14:57 
	 * @param linkId
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String addOutStandingScheme(String linkId) throws Exception {
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		//获得出库库联系单对象
		BisOutStock outStock = outStockDao.find(linkId);
		if(null!=outStock.getFinishFeeState()&&"1".equals(outStock.getFinishFeeState())){
			return "success";
		}
		//获得客户
		BaseClientInfo clientInfo=clientDao.find(Integer.parseInt(outStock.getStockInId()));
		//按提单计算抄码费用
		List<Map<String,Object>> list=outStockDao.getFeeCodeList(linkId);
		if(null==list||list.size()==0){
			return "success";
		}
		//获得汇率数据
		Map<String, Double> rateMap = new HashMap<String, Double>();
		List<BaseTaxRate> taxRates = taxRateDao.findAll();
		if(null != taxRates && taxRates.size() > 0){
			for (BaseTaxRate taxRate : taxRates){
				rateMap.put(taxRate.getCurrencyType(), taxRate.getExchangeRate());
			}
		}
		
		for (int i = 0;i<list.size();i++) {
			Map<String,Object> map=list.get(i);
			String billNum=map.get("BILL_NUM")!=null?map.get("BILL_NUM").toString():"";
			String feeId=map.get("FEE_ID")!=null?map.get("FEE_ID").toString():"";
			if(null==map.get("CODE_NUM")||"".equals(map.get("CODE_NUM"))){
				continue;
			}
			if(null==map.get("UNIT")||"".equals(map.get("UNIT"))){
				continue;
			}
			//判断该费目是否已经引入
			BisStandingBook oldBook=standingBookDao.getBisStandingBook(outStock.getSettleOrgId(),billNum,linkId,feeId,1,"cmf");
			if(oldBook!=null){
				if(Double.valueOf(map.get("CODE_NUM").toString()).compareTo(oldBook.getNum())!=0){
					oldBook.setNum(Double.valueOf(map.get("CODE_NUM").toString()));
					oldBook.setPrice(Double.valueOf(map.get("UNIT").toString()));
					oldBook.setReceiveAmount(BigDecimalUtil.mul(oldBook.getNum(),oldBook.getPrice()));
					oldBook.setRealAmount(0D);
					oldBook.setUpdatePerson(user.getName());
					oldBook.setUpdateDate(now);
					standingBookDao.merge(oldBook);
				}
				continue;
			}
			
			//获得 方案对象
			ExpenseScheme expenseScheme = expenseSchemeDao.find(feeId);
			if(null==expenseScheme){
				continue;
			}
			//查询方案 的费目
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_schemeNum",feeId));
			filters.add(new PropertyFilter("EQS_feeCode", "cmf"));
			List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoDao.find(filters);
			if(null==schemeInfos){
				continue;
			}
			ExpenseSchemeInfo schemeInfo=schemeInfos.get(0);
			if(null==schemeInfo){
				continue;
			}
			BisStandingBook standingBook = new BisStandingBook();
			standingBook.setCustomsNum("1".equals(expenseScheme.getIfGet())?outStock.getSettleOrgId():expenseScheme.getCustomsId());
			standingBook.setCustomsName("1".equals(expenseScheme.getIfGet())?outStock.getSettleOrg():expenseScheme.getCustomsName());
			standingBook.setLinkId(outStock.getOutLinkId());
			standingBook.setBillNum(billNum);
			standingBook.setCrkSign(2);
			standingBook.setFeeCode(schemeInfo.getFeeCode());
			standingBook.setFeeName(schemeInfo.getFeeName());
			standingBook.setFeePlan(expenseScheme.getSchemeNum());
			standingBook.setIfReceive(Integer.parseInt(expenseScheme.getIfGet()));
			standingBook.setNum(Double.valueOf(map.get("CODE_NUM").toString()));
			standingBook.setPrice(Double.valueOf(map.get("UNIT").toString()));
			standingBook.setReceiveAmount(BigDecimalUtil.mul(standingBook.getNum(),standingBook.getPrice()));
			standingBook.setRealAmount(0D);
			standingBook.setCurrency(schemeInfo.getCurrency());
			
			Double exchangeRate = Double.parseDouble(rateMap.get(schemeInfo.getCurrency()).toString());
			standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
			standingBook.setShouldRmb(BigDecimalUtil.mul(standingBook.getReceiveAmount(), standingBook.getExchangeRate()));

			standingBook.setBisType(expenseScheme.getBisType());
			
			standingBook.setFillSign(0);
			standingBook.setInputPersonId(user.getId().toString());
			standingBook.setInputPerson(user.getName());
			standingBook.setInputDate(now);
			
			standingBook.setTaxRate(0D);
			standingBook.setChargeDate(now);
			standingBook.setCostDate(now);
			
			standingBook.setExamineSign(0);
			standingBook.setShareSign(0);
			standingBook.setReconcileSign(0);
			standingBook.setSettleSign(0);
			standingBook.setSplitSign(0);
			standingBook.setContactType(1);
			standingBook.setChargeSign("0");
			
			Integer nowDate = Integer.parseInt(DateUtils.getDay());//获得当前日
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			
			if(nowDate != null && nowDate > clientInfo.getCheckDay()){
				standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)), "yyyy-MM"));
			}else{
				standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
			}
			
			standingBook.setUpdatePerson(user.getName());
			standingBook.setUpdateDate(now);
			standingBook.setStandingNum(this.getSequenceId());
			standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
			if(schemeInfo.getIfPay()==1){
				standingBook.setPaySign("1");
				BisStandingBook standingBookNew = new BisStandingBook();
				BeanUtils.copyProperties(standingBook, standingBookNew); 
				standingBookNew.setIfReceive(3-Integer.parseInt(expenseScheme.getIfGet()));
				standingBookNew.setCustomsNum(outStock.getSettleOrgId());
				standingBookNew.setCustomsName(outStock.getSettleOrg());
				standingBookNew.setStandingNum(this.getSequenceId());
				standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
				standingBookDao.merge(standingBookNew);
			}else{
				standingBook.setPaySign("0");
			}
			standingBookDao.merge(standingBook);
		}
		return null;
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 出库财务费目调整 添加方案  费目
	 * @date 2016年4月14日 下午2:14:57 
	 * @param linkId
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String addOutStandingSchemeBatch(String linkId, List<String> ids) throws Exception {
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		//获得出库库联系单对象
		BisOutStock outStock = outStockDao.find(linkId);
		if(null!=outStock.getFinishFeeState()&&"1".equals(outStock.getFinishFeeState())){
			return "success";
		}
		//获得客户
		BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(outStock.getStockInId()));
//		//获得该联系单的   出库时间
//		BisLoadingOrderInfo loadingOrderInfo = loadingOrderInfoDao.getOrderInfoByLinkIdForMaxDate(linkId);

		if(null != outStock){
			Map<String, Double> rateMap = new HashMap<String, Double>();
			//获得汇率数据
			List<BaseTaxRate> taxRates = taxRateDao.findAll();
			if(null != taxRates && taxRates.size() > 0){
				for (BaseTaxRate taxRate : taxRates){
					rateMap.put(taxRate.getCurrencyType(), taxRate.getExchangeRate());
				}
			}
			//查询装车单数据
			List<PropertyFilter> loadPfs = new ArrayList<PropertyFilter>();
			loadPfs.add(new PropertyFilter("EQS_outLinkId", linkId));
			loadPfs.add(new PropertyFilter("EQS_loadingState", "2"));
			List<BisLoadingInfo> loadingInfos = loadingInfoDao.find(loadPfs);
			for(String id : ids){
				//获得 方案对象
				ExpenseScheme expenseScheme = expenseSchemeDao.find(id);
				
				if(null != expenseScheme){
					//查询方案 的费目
					List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
					filters.add(new PropertyFilter("EQS_schemeNum", id));
					filters.add(new PropertyFilter("NEQS_feeType", "2"));
					List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoDao.find(filters);
					
					if(null != schemeInfos && schemeInfos.size() > 0){
						for (ExpenseSchemeInfo schemeInfo : schemeInfos){
							//判断该费目是否已经引入
							/*BisStandingBook oldBook=standingBookDao.getBisStandingBook(outStock.getSettleOrgId(),billNum,linkId,feeId,1,"cmf");
							if(oldBook!=null){
								continue;
							}*/
							BisStandingBook standingBook = new BisStandingBook();
							if(expenseScheme.getIfGet().equals("1")){
								standingBook.setCustomsNum(outStock.getSettleOrgId());
								standingBook.setCustomsName(outStock.getSettleOrg());
							}else{
								standingBook.setCustomsNum(expenseScheme.getCustomsId());
								standingBook.setCustomsName(expenseScheme.getCustomsName());
							}
							standingBook.setLinkId(outStock.getOutLinkId());
							standingBook.setCrkSign(2);							
							standingBook.setFeeCode(schemeInfo.getFeeCode());
							standingBook.setFeeName(schemeInfo.getFeeName());
							standingBook.setFeePlan(expenseScheme.getSchemeNum());
							standingBook.setIfReceive(Integer.parseInt(expenseScheme.getIfGet()));
							Double num = 0D;
							if(null != loadingInfos && loadingInfos.size() > 0){
								if("1".equals(schemeInfo.getBilling())){//按件数
									for(BisLoadingInfo loadingInfo : loadingInfos){
										num = BigDecimalUtil.add(num, loadingInfo.getPiece());
									}
								}else if("2".equals(schemeInfo.getBilling())){//按毛重（吨）
									for(BisLoadingInfo loadingInfo : loadingInfos){
										num = BigDecimalUtil.add(num, BigDecimalUtil.div(loadingInfo.getGrossWeight(), 1000));
									}
								}else if("3".equals(schemeInfo.getBilling())){//按净重（吨）
									for(BisLoadingInfo loadingInfo : loadingInfos){
										num = BigDecimalUtil.add(num, BigDecimalUtil.div(loadingInfo.getNetWeight(), 1000));
									}
								}else if("4".equals(schemeInfo.getBilling())){//按托
									
									num = Double.parseDouble(String.valueOf(loadingInfos.size()));
									
								}else if("5".equals(schemeInfo.getBilling())){//净重（千克）
									for(BisLoadingInfo loadingInfo : loadingInfos){
										num = BigDecimalUtil.add(num, loadingInfo.getNetWeight());
									}
								}else if("6".equals(schemeInfo.getBilling())){//毛重（千克）
									for(BisLoadingInfo loadingInfo : loadingInfos){
										num = BigDecimalUtil.add(num, loadingInfo.getGrossWeight());
									}
								}
							}
							if("16".equals(schemeInfo.getFeeType())){
								num = outStock.getOutCustomsCount().doubleValue();
							}else if("17".equals(schemeInfo.getFeeType())){
								num = outStock.getOutCiqCount().doubleValue();
							}
							standingBook.setNum(num);
							standingBook.setPrice(schemeInfo.getUnit());
							standingBook.setReceiveAmount(BigDecimalUtil.mul(num, schemeInfo.getUnit()));
							standingBook.setRealAmount(0D);
							standingBook.setCurrency(schemeInfo.getCurrency());
							
							Double exchangeRate = Double.parseDouble(rateMap.get(schemeInfo.getCurrency()).toString());
							standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
							standingBook.setShouldRmb(BigDecimalUtil.mul(standingBook.getReceiveAmount(), standingBook.getExchangeRate()));

							standingBook.setBisType(expenseScheme.getBisType());
							
							standingBook.setFillSign(0);
							standingBook.setInputPersonId(user.getId().toString());
							standingBook.setInputPerson(user.getName());
							standingBook.setInputDate(now);
							
							standingBook.setTaxRate(0D);
							standingBook.setChargeDate(now);
							standingBook.setCostDate(now);
							
							standingBook.setExamineSign(0);
							standingBook.setShareSign(0);
							standingBook.setReconcileSign(0);
							standingBook.setSettleSign(0);
							standingBook.setSplitSign(0);
							standingBook.setContactType(1);
							standingBook.setChargeSign("0");
							
							Integer nowDate = Integer.parseInt(DateUtils.getDay());//获得当前日
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
							
							if(nowDate != null && nowDate > clientInfo.getCheckDay()){
								standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 1)), "yyyy-MM"));
							}else{
								standingBook.setBillDate(DateUtils.parseDate(format.format(DateUtils.addMonth(now, 0)), "yyyy-MM"));
							}
							
							standingBook.setUpdatePerson(user.getName());
							standingBook.setUpdateDate(now);
							standingBook.setStandingNum(this.getSequenceId());
							standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()),new Date()));
							if(schemeInfo.getIfPay()==1){
								standingBook.setPaySign("1");
								BisStandingBook standingBookNew = new BisStandingBook();
								BeanUtils.copyProperties(standingBook, standingBookNew); 
								standingBookNew.setIfReceive(3-Integer.parseInt(expenseScheme.getIfGet()));
								standingBookNew.setCustomsNum(outStock.getSettleOrgId());
								standingBookNew.setCustomsName(outStock.getSettleOrg());
								standingBookNew.setStandingNum(this.getSequenceId());
								standingBookNew.setStandingCode(StringUtils.numToCode(String.valueOf(standingBookNew.getStandingNum()),new Date()));
								standingBookDao.merge(standingBookNew);
							}else{
								standingBook.setPaySign("0");
							}
							standingBookDao.merge(standingBook);
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量删除
	 * @date 2016年4月6日 上午11:40:13 
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String deleteStandingBookBatch(List<String> ids){
		if(null != ids && ids.size() > 0){
			for(String id : ids){
				if(null==standingBookDao.find(Integer.parseInt(id)))
					continue;
				standingBookDao.delete(Integer.parseInt(id));
			}
		}
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 批量审核通过
	 * @date 2016年4月6日 下午1:52:05 
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String isOkStandingBookBatch(List<String> ids){
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		for(String id : ids){
			BisStandingBook standingBook = standingBookDao.find(Integer.parseInt(id));
			standingBook.setExamineSign(1);
			standingBook.setExaminePerson(user.getName());
			standingBook.setExamineDate(now);
			
			standingBook.setUpdatePerson(user.getName());
			standingBook.setUpdateDate(now);
			//判断是不是业务付款单的数据，若是，则此业务付款单明细改为已审核状态
			if(!"".equals(standingBook.getPayId()) && null!=standingBook.getPayId() && 0!=standingBook.getPayId()){
				BisPayInfo payInfo=bisPayInfoService.get(standingBook.getPayId());
				if(payInfo!=null&&payInfo.getCheckSign()!=null&&payInfo.getCheckSign()!=1){
					payInfo.setCheckSign(1);
					bisPayInfoService.update(payInfo);
				}
			}
			standingBookDao.save(standingBook);
		}
		return null;
	}
	
	/**
	 * 
	 * @author PYL
	 * @Description: 批量取消审核
	 * @date 2016年5月18日  
	 * @param ids
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String isNotOkStandingBookBatch(List<String> ids){
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		BisPayInfo payInfo = null;
		for(String id : ids){
			BisStandingBook standingBook = standingBookDao.find(Integer.parseInt(id));
			standingBook.setExamineSign(0);
			standingBook.setExaminePerson(user.getName());
			standingBook.setExamineDate(now);
			
			standingBook.setUpdatePerson(user.getName());
			standingBook.setUpdateDate(now);
			//判断是不是业务付款单的数据，若是，则判断是否对应的两个台账都改为了未审核状态，若是，修改业务付款单明细状态为未审核
			if(!"".equals(standingBook.getPayId()) && null!=standingBook.getPayId() && 0!=standingBook.getPayId()){
				//获得payId与这个相同的list
				List<BisStandingBook> bookList = standingBookDao.findBy("payId", standingBook.getPayId());
				int tt = 0;
				for(int i=0;i<bookList.size();i++){
					if(bookList.get(i).getExamineSign()==0){
						tt = tt+1;
					}
				}
				//判断是否都取消审核了
				if(tt==2){
					payInfo = new BisPayInfo();
					payInfo = bisPayInfoService.get(standingBook.getPayId());
					if(payInfo.getCheckSign()!=0){
						payInfo.setCheckSign(0);
						bisPayInfoService.update(payInfo);
					}
				}
			}
			standingBookDao.save(standingBook);
		}
		return null;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 关于费目的修改
	 * @date 2016年4月7日 下午6:40:28 
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String editStandingBookByFee(BisStandingBook standingBook, String feeCode, String currency){
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		
		Map<String, Double> rateMap = new HashMap<String, Double>();
		//获得汇率数据
		List<BaseTaxRate> taxRates = taxRateDao.findAll();
		if(null != taxRates && taxRates.size() > 0){
			for (BaseTaxRate taxRate : taxRates){
				rateMap.put(taxRate.getCurrencyType(), taxRate.getExchangeRate());
			}
		}
		
		//判断费目代码  是否一样   置换 费目内容
		if(feeCode!=null&&!"".equals(feeCode)&&!feeCode.equals(standingBook.getFeeCode())){
			//查询  费目 数据
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>(); 
			filters.add(new PropertyFilter("EQS_code", feeCode));
			List<FeeCode> feeCodes = feeCodeDao.find(filters);
			
			if(null != feeCodes && feeCodes.size() > 0){
				FeeCode fee = feeCodes.get(0);
				standingBook.setFeeCode(fee.getCode());
				standingBook.setFeeName(fee.getNameC());
				standingBook.setCurrency(fee.getCurrencyType().toString());
				Double exchangeRate = Double.parseDouble(rateMap.get(fee.getCurrencyType().toString()).toString());
				standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
			}
		}
		
		//判断币种 是否一样   置换币种相关内容
		if(currency!=null&&!"".equals(currency)&&!currency.equals(standingBook.getCurrency())){
			standingBook.setCurrency(currency);
			Double exchangeRate = Double.parseDouble(rateMap.get(currency).toString());
			standingBook.setExchangeRate(exchangeRate == null ? 1D : exchangeRate);
		}
		
		//计算该费目费用
		if(standingBook.getNum() != null){
			Double price = standingBook.getPrice() == null ? 0D : standingBook.getPrice();//单价
			standingBook.setReceiveAmount(BigDecimalUtil.mul(price, standingBook.getNum()));
			standingBook.setShouldRmb(BigDecimalUtil.mul(standingBook.getReceiveAmount(), standingBook.getExchangeRate()));
		}
		
		standingBook.setUpdatePerson(user.getName());
		standingBook.setUpdateDate(now);
		standingBookDao.merge(standingBook);
		return null;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据联系单Id统计  应收应付费用
	 * @date 2016年4月9日 上午11:45:11 
	 * @param linkId
	 * @return
	 * @throws
	 */
	public Map<Object, String> sumStandingBookByLinkId(String linkId){
		Map<Object, String> map = new HashMap<Object, String>();
		map.put("ys", "0");//应收
		map.put("yf", "0");//应付
		map.put("lr", "0");//利润

		List<BisStandingBook> lists = standingBookDao.sumStandingBookByLinkId(linkId);
		if(null != lists && lists.size() > 0){
			for(BisStandingBook book : lists){
				if(book.getIfReceive() == 1){//应收
//					map.put("ys", book.getReceiveAmount().toString());
					map.put("ys", book.getShouldRmb().toString());
				}else if (book.getIfReceive() == 2){//应付
//					map.put("yf", book.getReceiveAmount().toString());
					map.put("yf", book.getShouldRmb().toString());
				}
			}
			//计算利润
			Double d = BigDecimalUtil.sub(Double.parseDouble(map.get("ys").toString()), Double.parseDouble(map.get("yf").toString()));
			map.put("lr", d.toString());//利润
		}
		return map; 
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 根据台账Id  垫付操作
	 * @date 2016年4月12日 下午4:42:06 
	 * @param standingBook
	 * @param stockId
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String updatePaySignByStandingNum(BisStandingBook standingBook, String stockId){
		
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		
		standingBook.setPaySign("1");
		standingBook.setUpdatePerson(user.getName());
		standingBook.setInputPerson(user.getName());
		standingBook.setInputPersonId(String.valueOf(user.getId()));
		standingBook.setUpdateDate(now);
		standingBook.setPayStandingNum(standingBook.getStandingNum());
		
		BisStandingBook newStandingBook = new BisStandingBook();
		BeanUtils.copyProperties(standingBook, newStandingBook);//复制
		
		//替换客户
		BaseClientInfo clientInfo = clientDao.find(Integer.parseInt(stockId));
		newStandingBook.setStandingNum(this.getSequenceId());
		newStandingBook.setStandingCode(StringUtils.numToCode(String.valueOf(newStandingBook.getStandingNum()),new Date()));
		newStandingBook.setCustomsNum(stockId);
		newStandingBook.setCustomsName(clientInfo.getClientName());
		newStandingBook.setIfReceive(1);
		/**
		 * 新加入拖车的自动关联应收里面价格
		 */
		BisEnterStock enterStock = enterStockDao.find(standingBook.getLinkId());
		if(null!=enterStock){
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			filters.add(new PropertyFilter("EQS_schemeNum",enterStock.getFeeId()!=null?enterStock.getFeeId():null));
			filters.add(new PropertyFilter("EQS_feeCode",standingBook.getFeeCode()));
			List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoDao.find(filters);
			if(null != schemeInfos && schemeInfos.size() > 0){
				ExpenseSchemeInfo schemeInfo=schemeInfos.get(0);
				newStandingBook.setPrice(schemeInfo.getUnit());
				newStandingBook.setReceiveAmount(BigDecimalUtil.mul(newStandingBook.getNum(), schemeInfo.getUnit()));
				newStandingBook.setShouldRmb(BigDecimalUtil.mul(newStandingBook.getReceiveAmount(),newStandingBook.getExchangeRate()));
			}
		}
		standingBookDao.save(standingBook);
		standingBookDao.save(newStandingBook);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 取消垫付操作
	 * @date 2016年5月6日 下午2:52:43 
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String deletePaySignByStandingNum(Integer standingNum){
		String back = "error";
		
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		
		//查询台账信息
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>(); 
		filters.add(new PropertyFilter("EQI_payStandingNum", standingNum));
		filters.add(new PropertyFilter("EQI_examineSign", 1));
		List<BisStandingBook> standingBooks = standingBookDao.find(filters);
		
		//存在  已审核的数据不允许  取消垫付
		if(null != standingBooks && standingBooks.size() > 0){
			back = "error";
		}else{
			//查询台账信息
	    	List<PropertyFilter> pfs = new ArrayList<PropertyFilter>(); 
	    	pfs.add(new PropertyFilter("EQI_payStandingNum", standingNum));
	    	List<BisStandingBook> books = standingBookDao.find(pfs);
			if(null != books && books.size() > 0){
				for(BisStandingBook book : books){
					if(1==book.getIfReceive()){
						standingBookDao.delete(book.getStandingNum());
					}else if(2==book.getIfReceive()){
						book.setPaySign("0");
						book.setPayStandingNum(null);
						book.setUpdatePerson(user.getName());
						book.setUpdateDate(now);
						standingBookDao.save(book);
					}
				}
				back = "success";
			}else{
				BisStandingBook oldbook=standingBookDao.find(standingNum);
				if(null!=oldbook){
					oldbook.setPaySign("0");
					standingBookDao.merge(oldbook);
				}
				back = "success";
			}
		}
		return back;
	}
	
	
	/**
	 * @author GLZ
	 * @Description: 根据多个条件匹配台账表中的数据
	 * @param params
	 * @return list
	 */
	public List<BisStandingBook> getFee(Map<String, Object> params) {
		return standingBookDao.findBy(params);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 统一修改结账日期
	 * @date 2016年4月13日 上午10:10:08 
	 * @param linkId
	 * @param editDate
	 * @return
	 * @throws
	 */
	@Transactional(readOnly = false)
	public void updateDateByLinkId(String linkId, String editDate) throws Exception {
		User user = UserUtil.getCurrentUser();//用户信息
		Date now = new Date();//统一时间
		
		//获得台账数据
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>(); 
		filters.add(new PropertyFilter("EQS_linkId", linkId));
		List<BisStandingBook> standingBooks = standingBookDao.find(filters);
		
		if(null != standingBooks && standingBooks.size() > 0){
			//结账日期
			Date date = DateUtils.parseDate(editDate, "yyyy-MM");
			
			for (BisStandingBook standingBook : standingBooks){
				if(standingBook.getExamineSign() == 0){//未审核的才可统一修改
					standingBook.setBillDate(date);
					standingBook.setUpdatePerson(user.getName());
					standingBook.setUpdateDate(now);
					standingBookDao.save(standingBook);
				}
			}
		}
	}
	/**
	 * 分组获取台账明细
	 * @param sDZ 对账单号
	 * @param ntype 1入，2出、3在库
	 * @param linkNum 联系单
	 * @param ibillNum 入提单
	 * @param obillNum 出提单
	 * @param sBaoGuan 报关单
	 * @param custom  客户
	 * @param inCarNUm 费目
	 * @param strTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public  List<Map<String, Object>> getStandingBookGroupList(String sDZ,int ntype,String linkNum,String ibillNum,String obillNum,String sBaoGuan,String custom,String inCarNUm,String strTime,String endTime) {
		return standingBookDao.getStandingBookGroupList(sDZ,ntype, linkNum, ibillNum, obillNum, sBaoGuan, custom, inCarNUm, strTime, endTime);
	}
	/**
	 * 根据对账单号获取对账单明细
	 * @param ntype
	 * @param checkingBookCode
	 * @return
	 */
	public  List<Map<String, Object>> getCheckingBookInfoList(int ntype,String checkingBookCode) {
		return standingBookDao.getCheckingBookInfoList(ntype, checkingBookCode);
	}
	public Integer getSequenceId() {
		return standingBookDao.getSequenceId("SEQ_STANDING_BOOK");
	}
	
	/**
	 * 根据主键集合，更新对账单号
	 * @param ordCode 对账id
	 *  @param ids 账单集合ids
	 */
	public Map<String, Object> updateStingBookCodeList(String sCode,String ids){
		Map<String, Object> retMap=new HashMap<String, Object>();
		retMap.put("endStr", "error");
		if(sCode!=null && !"".equals(sCode) && ids!=null && !"".equals(ids)){
		  standingBookDao.upStingBookCodeList(sCode, ids);
		  retMap.put("endStr", "success");
		}
		return retMap;
	}
	/**
	 * 根据主键集合，更新对账单号
	 * @param ordCode 对账id
	 *  @param ids 账单集合ids
	 */
	public Map<String, Object> delStingBookCodeList(String sCode,String ids){
		Map<String, Object> retMap=new HashMap<String, Object>();
		retMap.put("endStr", "error");
		if(sCode!=null && !"".equals(sCode) && ids!=null && !"".equals(ids)){
		  standingBookDao.delStingBookCodeList(sCode, ids);
		  retMap.put("endStr", "success");
		}
		return retMap;
	}

	public List<BisStandingBook> getList(Map<String, Object> params) {
		return standingBookDao.findBy(params);
	}
	
	public List<AsnAction> getTray(Map<String, Object> params) {
		return standingBookDao.getTray(params);
	}
	
	/**
	 * 核销台账总览
	 * @param customsId 客户id
	 * @param strTime 开始时间
	 * @param endTime 结束时间 台账期
	 * @param ntype 应收应付
	 * @param ncx 查询类型
	 * @return  Map<String, Object>
	 */
	public Map<String, Object> getCancelParameterList(int customsId,int ntype,String strTime,String endTime,int ncx,int nPage,int nPageSize){
		return standingBookDao.getCancelParameterList(customsId, ntype, strTime, endTime,ncx, nPage, nPageSize);
	}
	/**
	 * 核销台账明细
	 * @param customsId
	 * @param ymTime
	 * @param ntype
	 * @return
	 */
	public  Map<String, Object> getCancelParameterInfoList(int customsId,String ymTime ,int ntype,int nPage,int nPageSize) {
		return standingBookDao.getCancelParameterInfoList(customsId, ymTime, ntype, nPage, nPageSize);
	}
	/**
	 * 统计选中的核销金额
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> countSumMoney(String ids){
		return standingBookDao.countSumMoney(ids);
	}
	
	//获得数据库连接
	public Connection getConnect() throws SQLException{
		return standingBookDao.getConnection();
	}
	
	/**
	 *  落地插电模块结束时生成费用
	 * 
	 */
	public void luoDiMoney(String ctnNum,String billNum,String clientId,String yfClientId){
		List<FeeCode> feeCodeYSList = feeCodeService.findByType(19);
		List<FeeCode> feeCodeYFList = feeCodeService.findByType(20);
		User user=UserUtil.getCurrentUser();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("itemNum", billNum);
		params.put("delFlag", "0");
		List<BisEnterStock> enterList= enterStockService.findEnterList(params);
		BisEnterStock enterObj=enterList.get(0);
		BaseClientInfo clientYS=clientService.get(Integer.valueOf(clientId));
		BaseClientInfo clientYF=clientService.get(Integer.valueOf(yfClientId));
		Date now = new Date();
		if(!feeCodeYSList.isEmpty()){
			BisStandingBook  ysObj = new BisStandingBook();
			ysObj.setBillDate(DateUtils.ifBillDay(now,clientYS.getCheckDay()));
			ysObj.setLinkId(enterObj.getLinkId());
			ysObj.setCrkSign(1);
			ysObj.setContactType(1);
			ysObj.setCustomsNum(clientId);
			ysObj.setCustomsName(clientYS.getClientName());
			ysObj.setBillNum(billNum);
			ysObj.setIfReceive(1);
			ysObj.setInputPerson(user.getName());
			ysObj.setInputDate(now);
			ysObj.setStandingNum(this.getSequenceId());
			FeeCode feeCodeYS = feeCodeYSList.get(0);
			ysObj.setFeeCode(feeCodeYS.getCode());
			ysObj.setFeeName(feeCodeYS.getNameC());
			ysObj.setNum(1.00);
			ysObj.setPrice(feeCodeYS.getPriceBase()!=null?feeCodeYS.getPriceBase():70.00);
			ysObj.setReceiveAmount(feeCodeYS.getPriceBase()!=null?feeCodeYS.getPriceBase():70.00);
			ysObj.setCurrency(feeCodeYS.getCurrencyType().toString());
			ysObj.setExchangeRate(taxRateService.getCurrency(ysObj.getCurrency()).get(0).getExchangeRate());
			ysObj.setBisType("19");
			ysObj.setShouldRmb(ysObj.getReceiveAmount() * ysObj.getExchangeRate());
			ysObj.setStandingCode(StringUtils.numToCode(String.valueOf(ysObj.getStandingNum()),now));
//			ysObj.setExamineSign(1);
		}
		if(!feeCodeYFList.isEmpty()){
			BisStandingBook  yfObj = new BisStandingBook();
			yfObj.setBillDate(DateUtils.ifBillDay(now,clientYF.getCheckDay()));
			yfObj.setLinkId(enterObj.getLinkId());
			yfObj.setCrkSign(1);
			yfObj.setContactType(1);
			yfObj.setCustomsNum(yfClientId);
			yfObj.setCustomsName(clientYF.getClientName());
			yfObj.setBillNum(billNum);
			yfObj.setIfReceive(2);
			yfObj.setInputPerson(user.getName());
			yfObj.setInputDate(now);
			yfObj.setStandingNum(this.getSequenceId());
			FeeCode feeCodeYF = feeCodeYFList.get(0);
			yfObj.setFeeCode(feeCodeYF.getCode());
			yfObj.setFeeName(feeCodeYF.getNameC());
			yfObj.setNum(1.00);
			yfObj.setPrice(feeCodeYF.getPriceBase()!=null?feeCodeYF.getPriceBase():70.00);
			yfObj.setReceiveAmount(feeCodeYF.getPriceBase()!=null?feeCodeYF.getPriceBase():70.00);
			yfObj.setCurrency(feeCodeYF.getCurrencyType().toString());
			yfObj.setExchangeRate(taxRateService.getCurrency(yfObj.getCurrency()).get(0).getExchangeRate());
			yfObj.setBisType("20");
			yfObj.setShouldRmb(yfObj.getReceiveAmount() * yfObj.getExchangeRate());
			yfObj.setStandingCode(StringUtils.numToCode(String.valueOf(yfObj.getStandingNum()),now));
		}
	}

	public List<BisStandingBook> checkIfJf( BisStandingBook params) {
        //return standingBookDao.checkIfJf(params);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		PropertyFilter filter = new PropertyFilter("LIKES_remark", params.getRemark());
		PropertyFilter filter1 = new PropertyFilter("EQS_billNum", params.getBillNum());
		filters.add(filter);
		filters.add(filter1);
		List<BisStandingBook> list = standingBookDao.find(filters);
		return list;
    }
	
	public List<Map<String,Object>> findStandingBookListForCheck(String linkId){
		return standingBookDao.findStandingBookListForCheck(linkId);
	}
	
	public Page<BisStandingBook> getStocks(Page<BisStandingBook> page, BisStandingBook stock) {
        return standingBookDao.getStocks(page, stock);
    }
	
	public Page<BisStandingBook> getCustomerStocks(Page<BisStandingBook> page, BisStandingBook stock) {
        return standingBookDao.getCustomerStocks(page, stock);
    }
	
	public Page<BisStandingBook> getSupplierStocks(Page<BisStandingBook> page, BisStandingBook stock) {
        return standingBookDao.getSupplierStocks(page, stock);
    }
	/**
	 * 导出excel
	 * @param stock
	 * @return
	 */
	public List<Map<String, Object>> findRepot(BisStandingBook stock){
		return standingBookDao.findRepot(stock);
	}
	/**
	 * 导出客户费目明细账单
	 * @param stock
	 * @return
	 */
	public List<Map<String, Object>> findCustomerRepot(BisStandingBook stock){
		return standingBookDao.findCustomerRepot(stock);
	}
	/**
	 * 冷链月代理费用查询
	 * @param stock
	 * @return
	 */
	public List<Map<String, Object>> findSupplierRepot(BisStandingBook stock){
		return standingBookDao.findSupplierRepot(stock);
	}
	/**
	 * 汇总
	 * @param stock
	 * @return
	 */
	public List<Map<String, Object>> sum(BisStandingBook stock){
		return standingBookDao.sum(stock);
	}
	/**
	 * 
	 * @param stock
	 * @return
	 */
	public List<Map<String, Object>> customersum(BisStandingBook stock){
		return standingBookDao.customersum(stock);
	}
	
	public List<Map<String, Object>> suppliersum(BisStandingBook stock){
		return standingBookDao.suppliersum(stock);
	}
}
