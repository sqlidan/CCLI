package com.haiersoft.ccli.cost.service;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.cost.dao.BisVerifiBookDao;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.entity.BisVerifiBook;
import com.haiersoft.ccli.cost.entity.BisVerifiBookInfo;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxyProxy;
import com.ormrpc.wsvoucher.client.WSInvokeException;
import com.ormrpc.wsvoucher.client.WSWSVoucher;

/**
 * 核销
 * @author LZG
 *
 */
@Service
public class BisVerifiBookService extends BaseService<BisVerifiBook, String> {

	@Autowired
	private  BisVerifiBookDao bisVerifiBookDao;//核销
	@Autowired
	private BisVerifiBookInfoService  bisVerifiBookInfoService;//核销明细
	@Autowired
	private StandingBookService standingBookService;//台账
	@Autowired
	private TaxRateService taxRateService;//汇率
	//日志
	private static Logger logger = LoggerFactory.getLogger(BisVerifiBookService.class);
	@Override
	public HibernateDao<BisVerifiBook, String> getEntityDao() {
		return bisVerifiBookDao;
	}
	/**
	 * 检索核销银行信息列表
	 */
	 public List<Map<String, Object>> getBanklist(){
		 return bisVerifiBookDao.getBanklist();
	 }
	 /**
	  * 添加挂账信息
	  * @param obj
	  * @return
	  */
	 public String saveVerifiBook(BisVerifiBook obj){
		 String retStr = "error"; 
			if(obj.getIds()!=null && !"".equals(obj.getIds()) && obj.getSumMoney()!=null && obj.getSumMoney()>0){
				BaseTaxRate taxrate=taxRateService.getTaxByC(obj.getCurrency());
				if(taxrate!=null){
					obj.setTaxNum(taxrate.getExchangeRate());
				}else{
					//货币汇率没找到
					return "taxerror";
				}
				Double dsumMoneyRMB=obj.getSumMoney();
				Date getNowDate=new Date();
				dsumMoneyRMB=dsumMoneyRMB*obj.getTaxNum();//金额*汇率==RMB
				obj.setMoneyRmb(dsumMoneyRMB);
				String keys="HX"+DateUtils.getDateRandom();
				obj.setVerifiNum(keys);
				obj.setCrTime(getNowDate);
				User user = UserUtil.getCurrentUser();
				if(user!=null){
					obj.setCrUser(user.getName());
				}
				//添加主表信息
				this.save(obj);
				//添加详细表信息
				String[] idsList=obj.getIds().split(",");
				BisStandingBook getBookObj=null;//台账对象
				if(idsList!=null && idsList.length>0){
					BisVerifiBookInfo verifiBookInfo=null;//核销明细
					for(String getId:idsList){
						if(!"".equals(getId)){
							Double dwsRMB=0d;//账单未付款金额RMB
							getBookObj=standingBookService.get(Integer.valueOf(getId));
							if(getBookObj!=null){
								//未付款金额
								dwsRMB=getBookObj.getShouldRmb()-(getBookObj.getRealRmb()!=null?getBookObj.getRealRmb():0d);
								verifiBookInfo= new BisVerifiBookInfo();
								verifiBookInfo.setSumMoney(dwsRMB);
								verifiBookInfo.setStandingNum(getBookObj.getStandingNum());
								verifiBookInfo.setVerifiNum(obj.getVerifiNum());
								verifiBookInfo.setNowMoney(dwsRMB);
								verifiBookInfo.setFeedCode(getBookObj.getFeeCode());
								getBookObj.setHxUser(user!=null?user.getName():"");
								getBookObj.setHxDate(getNowDate);
								getBookObj.setHxKey(keys);
								getBookObj.setHxBz(Integer.valueOf(obj.getCurrency()));
								getBookObj.setHxState(1);
								//保存台账信息
								standingBookService.update(getBookObj);
								//保存核销明细
								bisVerifiBookInfoService.save(verifiBookInfo); 
							}
						}
					}//end for
				}
				retStr = "success"; 
				try {
					saveSFToJDGX(keys,1,obj.getnType(),1);
				} catch (Exception e) {
				}
			}
			return retStr;
	 }
	 
	 
	 
	 /**
	  * 添加核销信息 --现结，冲账
	  * @param obj
	  * @return
	  */
	 public String saveHx(BisVerifiBook obj){
		 String retStr = "error"; 
			if(obj.getIds()!=null && !"".equals(obj.getIds()) && obj.getSumMoney()!=null && obj.getSumMoney()>0){
				String getMoneyType=obj.getCurrency();
				 Map<String,Double> getTaxMap=taxRateService.getTaxMap();
				if(getTaxMap==null || getTaxMap.get(getMoneyType)==null){
					//货币汇率没找到
					return "taxerror";
				}else{
					obj.setTaxNum(getTaxMap.get(getMoneyType));
				}
				Double dsumMoneyRMB=obj.getSumMoney();
				dsumMoneyRMB=dsumMoneyRMB*getTaxMap.get(getMoneyType);//金额*汇率==RMB
				obj.setMoneyRmb(dsumMoneyRMB);
				String keys="HX"+DateUtils.getDateRandom();
				obj.setVerifiNum(keys);
				obj.setCrTime(new Date());
				User user = UserUtil.getCurrentUser();
				if(user!=null){
					obj.setCrUser(user.getName());
				}
				//添加主表信息
				this.save(obj);
				
				
				//添加详细表信息
				String[] idsList=obj.getIds().split(",");
				BisStandingBook getBookObj=null;//台账对象
				Double dwsRMB=0d;//账单未付款金额RMB
				Double bookTax=0d;//台账汇率
				if(idsList!=null && idsList.length>0){
					BisVerifiBookInfo verifiBookInfo=null;//核销明细
					for(String getId:idsList){
						if(!"".equals(getId)){
							getBookObj=standingBookService.get(Integer.valueOf(getId));
							if(getBookObj!=null){
								//未付款金额要大于0
								dwsRMB=getBookObj.getShouldRmb()-(getBookObj.getRealRmb()!=null?getBookObj.getRealRmb():0d);
								if(dwsRMB>0){
									verifiBookInfo= new BisVerifiBookInfo();
									verifiBookInfo.setSumMoney(dwsRMB);
									verifiBookInfo.setStandingNum(getBookObj.getStandingNum());
									verifiBookInfo.setVerifiNum(obj.getVerifiNum());
									dsumMoneyRMB=dsumMoneyRMB-dwsRMB;
									//获取台账汇率
									bookTax=getTaxMap.get(getBookObj.getCurrency());
									if(bookTax==null ){
										bookTax=1d;
									}
									verifiBookInfo.setTaxNum(bookTax);
									//当核销金额减去未付款金额大于等于0时则代表该台账完成已付款
									if(dsumMoneyRMB>=0){
										getBookObj.setRealRmb(getBookObj.getShouldRmb());
										getBookObj.setRealAmount((getBookObj.getRealAmount()!=null?getBookObj.getRealAmount():0d)+dwsRMB/bookTax);
										verifiBookInfo.setNowMoney(dwsRMB);
										verifiBookInfo.setFeedCode(getBookObj.getFeeCode());
										//保存台账信息
										standingBookService.update(getBookObj);
										//保存核销明细
										bisVerifiBookInfoService.save(verifiBookInfo);
										if(dsumMoneyRMB==0){
											break;
										}
									}else{
										//如果核销金额减去未付款金额小于0时则代表该台账完成已付款
										getBookObj.setRealRmb((getBookObj.getRealRmb()!=null?getBookObj.getRealRmb():0d)+(dwsRMB+dsumMoneyRMB));
										verifiBookInfo.setNowMoney(dwsRMB+dsumMoneyRMB);
										getBookObj.setRealAmount((getBookObj.getRealAmount()!=null?getBookObj.getRealAmount():0d)+(dwsRMB+dsumMoneyRMB)/bookTax);
										verifiBookInfo.setFeedCode(getBookObj.getFeeCode());
										//保存台账信息
										standingBookService.update(getBookObj);
										//保存核销明细
										bisVerifiBookInfoService.save(verifiBookInfo);
										break;
									}
								}
							}
						}
					}//end for
					 
				}
				retStr = "success"; 
				try{
					saveSFToJDGX(keys,1,obj.getnType(),obj.getcType());
				} catch (Exception e) {
				}
			}
			return retStr;
	 }
	 /**
	  * 取消核销
	  * @param num
	  * @return
	  */
	 public String delHx(String num){
		 String retStr = "error";
		 if(num!=null && !"".equals(num)){
			 List<BisVerifiBookInfo> getList= bisVerifiBookInfoService.getVerifiBookInfoList(num);
			 if(getList!=null && getList.size()>0){
				 BisStandingBook getBookObj=null;//台账对象
				 for(BisVerifiBookInfo getObj:getList){
					 getBookObj=standingBookService.get(getObj.getStandingNum());
					 if(getBookObj!=null){
						 getBookObj.setRealRmb(getBookObj.getRealRmb()-getObj.getNowMoney());
						 getBookObj.setRealAmount(getBookObj.getRealAmount()-(getObj.getNowMoney()/getObj.getTaxNum()));
						//保存台账信息
						 standingBookService.update(getBookObj);
					 }
				 }//end for
			 }//end if
			 
			 //更新主表状态
			 BisVerifiBook getObj=this.get(num);
			 if(getObj!=null && !"".equals(getObj.getVerifiNum())){
				 getObj.setUpTime(new Date());
				 User user = UserUtil.getCurrentUser();
					if(user!=null){
						getObj.setUpUser(user.getName());
					}
				 getObj.setnState(2);
				 this.update(getObj);
			 }
			 retStr = "success"; 
		 }
		 return retStr; 
	 }
	
	 /**
	  * 
	  * @param code 核销编码
	  * @param ntype 凭证类型 1，挂账，2，现结
	  * @param nsfType 核销类型  1应收，2应付
	  * @param cType 操作类型 1,挂账，2现结，3冲账
	  */
	 public String  saveSFToJDGX(String code,int ntype,int nsfType,int cType){
		 String endStr="error";
		 List<Map<String, Object>> getList=null ;
		 if(1==nsfType){//应收
			 if(1==cType || 2==cType){//挂账，现结
				 getList= bisVerifiBookDao.getYSToJDGJDataList(code,cType);
			 }else{//冲现
				 getList= bisVerifiBookDao.getYSToJDCDataList(code,cType);
			 }
		 }else{ //应付
			 if(1==cType || 2==cType){//挂账，现结
				 getList= bisVerifiBookDao.getYFToJDGJDataList(code,cType);
			 }else{//冲现
				 getList= bisVerifiBookDao.getYFToJDCDataList(code,cType);
			 }
		 }
		 if(getList!=null && getList.size()>0){
			 WSWSVoucher[] voucherList=this.getPostWSWSVoucher(getList, code,cType);
			 //发送金蝶接口
			 WSWSVoucherSrvProxyProxy proxyJD=new WSWSVoucherSrvProxyProxy();
			 try {
				String[][] getStrList=proxyJD.importVoucher(voucherList, 0, 0);
				System.out.println(getStrList.toString());
				logger.error("接口error："+getStrList.toString());
				int nstate=1;
				String pzNum="";
				if(getStrList.length==1){
					if("0000".equals(getStrList[0][4])){
						nstate=3;
						pzNum=getStrList[0][6];
					}
				}else{
					nstate=2;
				}
				bisVerifiBookDao.updateVerifiBook(code,nstate,pzNum);
				endStr="success";
			} catch (WSInvokeException e) {
				 
			} catch (RemoteException e) {
				 
			}
		 }
		return endStr;
	 }
	
	 /**
	  * 组成接口需要发送的对象集合
	  * @param getList 发送查询结果集
	  * @param code 
	  * @return
	  */
	 private WSWSVoucher[]  getPostWSWSVoucher(List<Map<String, Object>> getList,String code,int cType){
		//获取客户财务码
		 Map<String, Object> clientMap=bisVerifiBookDao.getClientJDNum(code);
		 String khClient=null;
		 String gysClient=null;
		 if(clientMap==null || clientMap.size()==0 || (clientMap.get("GYSNUM")==null && clientMap.get("CWNUM")==null)){
			 logger.error("接口error："+code+"客户财务编码未查询到！");
			 return null;
		 }else{
			 khClient=clientMap.get("CWNUM")!=null?clientMap.get("CWNUM").toString():"";
			 gysClient=clientMap.get("GYSNUM")!=null?clientMap.get("GYSNUM").toString():"";
		 }
		 WSWSVoucher addObj=null;
		 WSWSVoucher[] voucherList=new WSWSVoucher[getList.size()];
		 int i=0;
		 String hsXMNum=null;
		 String pzNum=DateUtils.getDateRandom();
		 for(Map<String, Object> map:getList){
			 addObj=new WSWSVoucher();
			 addObj.setCompanyNumber("A0040");//公司码
			 String nowDate=map.get("DNOW")!=null?map.get("DNOW").toString():"";
			 addObj.setBookedDate(nowDate);//DateUtils.getDateStr(DateUtils.parseDate(nowDate),"yyyy-MM")
			 addObj.setBizDate(nowDate);
			 addObj.setPeriodYear(Integer.valueOf(DateUtils.getYear()));
			 addObj.setPeriodNumber(Integer.valueOf(DateUtils.getMonth()));
			 addObj.setVoucherType(map.get("PZTYPE").toString());
			 addObj.setVoucherNumber(pzNum);
			 addObj.setEntrySeq(i+1);
			 addObj.setAccountNumber(map.get("YSCODE")!=null?map.get("YSCODE").toString():"");
			 addObj.setCurrencyNumber("BB01"); 
			 addObj.setEntryDC(Integer.valueOf(map.get("FANGXIANG").toString()));
			 addObj.setOriginalAmount(Double.valueOf(map.get("YMONEY")!=null?map.get("YMONEY").toString():"0d"));
			 addObj.setDebitAmount(Double.valueOf(map.get("JFMONEY")!=null?map.get("JFMONEY").toString():"0d"));
			 addObj.setCreditAmount(Double.valueOf(map.get("DFMONEY")!=null?map.get("DFMONEY").toString():"0d"));
			 addObj.setVoucherAbstract(map.get("INFO")!=null?map.get("INFO").toString():"");
			 User user = UserUtil.getCurrentUser();
			 if(user!=null){
				 addObj.setCreator(user.getName());
			 }
//			 addObj.setCreator("李鲁璐");//发布需去掉
			 addObj.setAsstSeq(i+1);//4003);
			 //addObj.setCussent(1);
			 if("1122".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("客户");
				 addObj.setAsstActNumber1(khClient);
				 addObj.setAsstActType2("应收类型");
				 if(1==cType){
					 addObj.setAsstActNumber2("40-0002"); 
				 }else{
					 addObj.setAsstActNumber2("40-0001"); 
				 }
			 }else if("600118".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("客户");
				 addObj.setAsstActNumber1(khClient);
				 addObj.setAsstActType2("项目");
				 hsXMNum=map.get("JDNUM")!=null?map.get("JDNUM").toString():"";
				 if(hsXMNum==null || "".equals(hsXMNum)){
					 logger.error("接口error："+(i+1)+"行核销项目代码未查询到！");
					 return null;
				 }
//				 addObj.setAsstActNumber2("002818"); 
				 addObj.setAsstActNumber2(hsXMNum); 
			 }else if("60010501".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("客户");
				 addObj.setAsstActNumber1(khClient);
			 }else if("22210105".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("税率");
				 addObj.setAsstActNumber1("000004");
			 }else if("100218".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("银行账户");
				 addObj.setAsstActNumber1("4003");
			 }else if("22210101".equals(addObj.getAccountNumber())){//应付
				 addObj.setAsstActType1("进项税额类型");
				 addObj.setAsstActNumber1("001");
				 addObj.setAsstActType2("税率");
				 addObj.setAsstActNumber2("000004");
			 }else if("2202".equals(addObj.getAccountNumber())){//应付
				 addObj.setAsstActType1("供应商");
				 addObj.setAsstActNumber1(gysClient);
			 }else if("6401050118".equals(addObj.getAccountNumber())){
				 addObj.setAsstActType1("客户");
				 addObj.setAsstActNumber1(khClient);
			 }else{
				 addObj.setAsstActType1("客户");
				 addObj.setAsstActNumber1(khClient);
			 }
			 
			 voucherList[i]=addObj;
			 i++;
		 }
		return voucherList;
	 }
	 
}
