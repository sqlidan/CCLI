package com.haiersoft.ccli.report.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.InStockReportDao;
import com.haiersoft.ccli.report.entity.Stock;

/**
 * 
 * @author Connor.M
 * @ClassName: InStockReportService
 * @Description: 在库报告书 Service
 * @date 2016年4月26日 下午2:55:54
 */
@Service
@Transactional(readOnly = true)
public class InStockReportService {
	
	@Autowired
	private InStockReportDao inStockReportDao;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 在库报告书 -- 普通
	 * @date 2016年4月26日 下午3:37:32 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchInStockReport(Stock stock){
		return inStockReportDao.searchInStockReport(stock);
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 在库报告书 -- 日本
	 * @date 2016年4月27日 上午9:54:05 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchInStockReportRB(Stock stock){
		return inStockReportDao.searchInStockReportRB(stock);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 在库报告书 -- OTE 
	 * @date 2016年4月27日 上午11:27:01 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchInStockReportOTE(Stock stock){
		return inStockReportDao.searchInStockReportOTE(stock);
	}
	
    public Page<Stock> getStocks(Page<Stock> page, Stock stock) {
        return inStockReportDao.getStocks(page, stock);
    }
    
    public Page<Stock> getEnterStockStocks(Page<Stock> page,Integer ntype,String itemNum,String cunNum,String stockIn,String linkId,String sku,String strTime,String endTime,String isBonded) {
        return inStockReportDao.getEnterStockStocks(page, itemNum, cunNum, stockIn, linkId,sku,strTime, endTime,isBonded);
    }
    
    public Page<Stock> getOutStockStocks(Page<Stock> page,Integer ntype,String billNum,String itemNum,String cunNum,String stockIn,String linkId,String strTime,String endTime,String isBonded) {
        return inStockReportDao.getOutStockStocks(page,billNum,itemNum, cunNum, stockIn, linkId, strTime, endTime,isBonded);
    }
    
    public Page<Stock> getTransferStockStocks(Page<Stock> page,String isBonded,String transferNum) {
        return inStockReportDao.getTransferStockStocks(page,isBonded,transferNum);
    }
    
    public Page<Stock> getASNReportStocks(Page<Stock> page,String asnId) {
        return inStockReportDao.getASNReportStocks(page, asnId);
    }




	public List<Map<String, Object>> findInventoryList(Stock stock, String clientId) {
		return inStockReportDao.findInventoryList(stock,clientId);
	}


	/**
	 * @param itemNum  提单号
	 * @param ctnNum  箱号
	 * @param realClientName  客户名称
	 * @throws Exception
	 * @throws
	 * @Description: 在库报告书 接口信息（海路通系统）
	 */
	public List<Map<String, Object>> inStockReportInfo(String itemNum, String ctnNum, String realClientName){
		return inStockReportDao.inStockReportInfo(itemNum, ctnNum, realClientName);
	}
	
}
