package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.SortingReportDao;
import com.haiersoft.ccli.report.entity.Stock;

/**
 * 
 * @author Connor.M
 * @ClassName: SortingReportService
 * @Description: 分拣报告书 Service
 * @date 2016年4月25日 上午11:25:09
 */
@Service
@Transactional(readOnly = true)
public class SortingReportService {
	
	@Autowired
	private SortingReportDao sortingReportDao;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 分拣预报单--普通
	 * @date 2016年4月25日 下午1:52:48 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchSortingReport(Stock stock){
		return sortingReportDao.searchSortingReport(stock);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 分拣预报单--日本
	 * @date 2016年4月26日 上午11:03:02 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchSortingReportRB(Stock stock){
		return sortingReportDao.searchSortingReportRB(stock);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 分拣预报单--OTC
	 * @date 2016年4月26日 下午1:42:31 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchSortingReportOTE(Stock stock){
		return sortingReportDao.searchSortingReportOTE(stock);
	}
	
	public Page<Stock> getStocks(Page<Stock> page, Stock stock) {
        return sortingReportDao.getStocks(page, stock);
    }
}
