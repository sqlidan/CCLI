package com.haiersoft.ccli.outsidequery.service;

import com.haiersoft.ccli.api.dao.InStockInfoDao;
import com.haiersoft.ccli.api.entity.InStockInfo;
import com.haiersoft.ccli.api.entity.InStockInfoSort;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.outsidequery.dao.OutsideQueryDao;
import com.haiersoft.ccli.outsidequery.entity.OutsideQuery;
import com.haiersoft.ccli.report.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutsideQueryService extends BaseService<OutsideQuery, String> {

	@Autowired
	private OutsideQueryDao outsideQueryDao;
	
	@Override
	public HibernateDao<OutsideQuery, String> getEntityDao() {
		return outsideQueryDao;
	}


	public List<OutsideQuery> getStocks(Map map) {
		return outsideQueryDao.getStocks(map);
	}
	public List<OutsideQuery> getEnterStockStocks(Map map) {
		return outsideQueryDao.getEnterStockStocks(map);
	}

	public List<OutsideQuery> getOutStockStocks(Map map) {
		return outsideQueryDao.getOutStockStocks(map);
	}

}
