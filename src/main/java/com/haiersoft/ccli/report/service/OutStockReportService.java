package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.OutStockReportDao;
import com.haiersoft.ccli.report.entity.OutStock;

/**
 * 
 * @author Mazy
 * @ClassName: StockReportService
 * @Description: 库存报表Service
 * @date 2016年6月24日 上午9:41:09
 */
@Service
public class OutStockReportService {
	
	@Autowired
	private OutStockReportDao outStockReportDao;
	@Autowired
	private TaxRateService taxRateService;
	public Page<OutStock> searchStockReport(Page<OutStock> page, OutStock outStock){
		BaseTaxRate getObj=	taxRateService.getTaxByC("1");
		return outStockReportDao.searchStockReport(page, outStock,getObj!=null ?getObj.getExchangeRate():1d);
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
				getlist=outStockReportDao.findRepotPT(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else if(2==ntype){
				getlist=outStockReportDao.findRepotJP(itemNum, cunNum, stockIn, linkId, strTime, endTime);
			}else{
				getlist=outStockReportDao.findRepotOTE(itemNum, cunNum, stockIn, linkId, strTime, endTime);
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
		List<Map<String,Object>> getlist=outStockReportDao.findRepotInAndOut(itemNum, cunNum, stockIn, linkId, strTime, endTime);
		return getlist;
	}
}
