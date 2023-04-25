package com.haiersoft.ccli.wms.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.wms.dao.BackHistoryInfoDao;
import com.haiersoft.ccli.wms.entity.BackHistoryInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
/**
 * 
 * @ClassName: BackHistoryInfoService
 */
@Service
public class BackHistoryInfoService extends BaseService<BackHistoryInfo, Integer> {

	@Autowired
	private BackHistoryInfoDao backHistoryInfoDao;
	
	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;
	@Autowired
	private StandingBookService standingBookService;
	@Autowired
	private TaxRateService taxRateService;
	@Autowired
	private ClientService clientService;
	
	@Override
    public HibernateDao<BackHistoryInfo, Integer> getEntityDao() {
	    return backHistoryInfoDao;
    }
	
	
	public Page<BackHistoryInfo> findBackPage(Page<BackHistoryInfo> page, BackHistoryInfo params) {
        return backHistoryInfoDao.findBackPage(page, params);
    }
   	/**
     * @param getObj
     * @param crExpense
     * @return
     * @Description 回库记录，点击收费生成装车单的出入库费
     * @author PYL
	 * @throws ParseException 
     */
    @ResponseBody
    public String crFeeForBack(Page<BackHistoryInfo> page, String loadingNum,String userName) throws ParseException {
    	Date now = new Date();
       
        
        /**查询装车单的回库数量
        select lod.BILL_NUM,tr.CONTACT_NUM,tr.STOCK_IN,tr.STOCK_NAME,sum(lod.PIECE),sum(lod.NET_WEIGHT),sum(lod.GROSS_WEIGHT) 
        from bis_loading_info lod 
        left join bis_tray_info tr on tr.TRAY_ID=lod.TRAY_ID
        where LOADING_TRUCK_NUM=loadingNum and loading_state='6'
        group by lod.BILL_NUM,tr.CONTACT_NUM,tr.STOCK_IN,tr.STOCK_NAME
         **/
        //结果集循环生成费用
    	BackHistoryInfo bhi = new BackHistoryInfo();
    	bhi.setLoadingNum(loadingNum);
    	
    	
    	Page<BackHistoryInfo> page1 = findBackPage(page, bhi);
    	List<BackHistoryInfo> list = page1.getResult();
    	for(BackHistoryInfo getObj : list){
    		 //生成费用明细
            BisEnterStock enterStock = enterStockService.get(getObj.getCtnNum());//
            Map<String, Object> cr = new HashMap<String, Object>();
            String crFeeId = enterStock.getFeeId();
            cr.put("schemeNum", crFeeId);
            cr.put("feeType", "3");
            List<ExpenseSchemeInfo> crList = expenseSchemeInfoService.getFeeByName(cr);
            if (!crList.isEmpty()) {
            	ExpenseSchemeInfo exInfo = crList.get(0);
    	        String linkId = getObj.getCtnNum();//
    	        //Map<String, Object> params = new HashMap<String, Object>();
    	        //params.put("asn", asn);
    	        Double unit = exInfo.getUnit();
    	        Double price = 0d;
    	        Double jsNum = 0.00;
    	        switch (exInfo.getBilling()) {
    	            case "1":
    	                price = unit * getObj.getNowPiece();
    	                jsNum = getObj.getNowPiece().doubleValue();
    	                break;
    	            case "2":
    	                price = unit * getObj.getGrossWeight();
    	                jsNum = getObj.getGrossWeight() / 1000;
    	                break;
    	            case "3":
    	                price = unit * getObj.getNetWeight();
    	                jsNum = getObj.getNetWeight() / 1000;
    	                break;
    	        }
    	        // 将费用加入台账表
    	        BisStandingBook standingBook = new BisStandingBook();
    	        standingBook.setStandingNum(standingBookService.getSequenceId());
    	        standingBook.setCustomsNum(getObj.getStockIn());
    	        standingBook.setCustomsName(getObj.getStockName());
    	        standingBook.setBillNum(getObj.getBillNum());
    	        /*standingBook.setAsn(asn);*/
    	        standingBook.setLinkId(linkId);
    	        standingBook.setFeePlan(exInfo.getSchemeNum());
    	        standingBook.setFeeCode(exInfo.getFeeCode());
    	        standingBook.setFeeName(exInfo.getFeeName());
    	        standingBook.setCrkSign(1);
    	        standingBook.setStorageDtae(DateUtils.dateToDate(now));
    	        BaseClientInfo getClient = clientService.get(Integer.valueOf(getObj.getStockIn()));
    	        if (null != getClient) {
    	            if (null != getClient.getCheckDay()) {
    	                standingBook.setBillDate(DateUtils.ifBillDay(now, getClient.getCheckDay()));
    	            }
    	        }
    	        standingBook.setIfReceive(1);
    	        standingBook.setNum(jsNum);
    	        standingBook.setPrice(unit);
    	        standingBook.setReceiveAmount(price / 1000);
    	        standingBook.setChargeDate(now);
    	        standingBook.setCostDate(now);
    	        standingBook.setInputPerson(userName);
    	        standingBook.setInputDate(now);
    	        standingBook.setFillSign(0);
    	        standingBook.setCurrency(exInfo.getCurrency());
    	        BaseTaxRate taxRate = taxRateService.getTaxByC(exInfo.getCurrency());
    	        standingBook.setExchangeRate(taxRate.getExchangeRate());
    	        standingBook.setExamineSign(0);
    	        standingBook.setBisType("3");
    	        standingBook.setShouldRmb(taxRate.getExchangeRate() * price / 1000);
    	        standingBook.setReconcileSign(0);
    	        standingBook.setSettleSign(0);
    	        standingBook.setSplitSign(0);
    	        standingBook.setRemark("因装车单"+loadingNum+"回库生成出入库费");
    	        standingBook.setContactType(1);
    	        standingBook.setBoxSign(0);
    	        standingBook.setShareSign(0);
    	        standingBook.setPaySign("0");
    	        standingBook.setChargeSign("0");
    	        standingBook.setRealAmount(0.00);
    	        standingBook.setRealRmb(0.00);
    	        standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), now));
    	        standingBookService.save(standingBook);
    	        return SUCCESS;
            }else{
            	return "false";
            }
    	}
    	
       return "false";
    }
}
