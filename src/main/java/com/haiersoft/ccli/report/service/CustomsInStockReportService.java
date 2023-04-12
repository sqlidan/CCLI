package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.CustomsInStockReportDao;
import com.haiersoft.ccli.report.entity.CustomsInStock;

/**
 * 
 * @author cuij
 * @ClassName: CustomsInStockReportService
 * @Description: 海关入库报表Service
 * @date 2016年6月21日10:58:57
 */
@Service
public class CustomsInStockReportService {
	
	@Autowired
	private CustomsInStockReportDao customsInStockReportDao;
	@Autowired
	private TaxRateService taxRateService;
	
	public Page<CustomsInStock> searchStockReport(Page<CustomsInStock> page, CustomsInStock customsInStock){
		BaseTaxRate getObj=	taxRateService.getTaxByC("1");
		return customsInStockReportDao.searchStockReport(page, customsInStock,getObj!=null ?getObj.getExchangeRate():1d);
	}
	/**
	 * 在库明细
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String, Object>> findRepot(Integer ntype,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getlist=null;
		if(ntype!=null){
			if(1==ntype){
				getlist=customsInStockReportDao.findRepotPT(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else if(2==ntype){
				getlist=customsInStockReportDao.findRepotJP(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else{
				getlist=customsInStockReportDao.findRepotOTE(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}
		}
		return getlist;
	}
	/**
	 * 出入库明细
	 * @param itemNum 提单号
	 * @param cunNum 厢号
	 * @param stockIn 客户id
	 * @param linkId 联系单号
	 * @param strTime 入库时间开始
	 * @param endTime 入库时间结束
	 * @return
	 */
	public List<Map<String, Object>> findRepotInAndOut(String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime){
		List<Map<String,Object>> getlist=customsInStockReportDao.findRepotInAndOut(itemNum, cunNum, stockIn, linkId, strTime, endTime);
		return getlist;
	}
}
