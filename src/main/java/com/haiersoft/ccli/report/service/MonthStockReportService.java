package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.MonthStockReportDao;

@Service
@Transactional(readOnly = true)
public class MonthStockReportService {
	
	@Autowired
	MonthStockReportDao monthStockReportDao;
	

	public Page<Object> getPage(Page<Object> page, String month,String names) {
		
		return monthStockReportDao.getPage(page,month, names);
	}


	public List<Map<String, Object>>  getAllMonth() {
		// TODO Auto-generated method stub
		return monthStockReportDao.getMonths();
	}
}
