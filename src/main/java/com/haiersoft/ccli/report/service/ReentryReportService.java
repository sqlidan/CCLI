package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.ReentryReportDao;
import com.haiersoft.ccli.report.entity.Stock;

/**
 * 
 * @author yaohn
 * @ClassName: ReentryReportService
 * @Description: 重收报告书 Service
 * @date 
 */
@Service
@Transactional(readOnly = true)
public class ReentryReportService {
	
	@Autowired
	private ReentryReportDao reentryReportDao;
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收预报单--普通
	 * @date 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchReentryReport(Stock stock){
		return reentryReportDao.searchReentryReport(stock);
	}
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收预报单--日本
	 * @date 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchReentryReportRB(Stock stock){
		return reentryReportDao.searchReentryReportRB(stock);
	}
	
	/**
	 * 
	 * @author yaohn
	 * @Description: 重收预报单--OTC
	 * @date 
	 * @param stock
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> searchReentryReportOTE(Stock stock){
		return reentryReportDao.searchReentryReportOTE(stock);
	}
	
	public Page<Stock> getStocks(Page<Stock> page, Stock stock) {
        return reentryReportDao.getStocks(page, stock);
    }
}
