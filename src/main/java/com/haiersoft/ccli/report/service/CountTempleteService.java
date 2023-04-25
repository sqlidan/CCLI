package com.haiersoft.ccli.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.dao.CountTempleteDao;
import com.haiersoft.ccli.wms.entity.CountTemplete;

/**
 */
@Service
public class CountTempleteService {

    @Autowired
    private CountTempleteDao countTempleteDao;

    public Page<CountTemplete> searchStockReport(Page<CountTemplete> page, CountTemplete countTemplete) {
        return countTempleteDao.searchStockReport(page, countTemplete);
    }

    public List<Map<String, Object>> findReport(String BILL_NUM, String STOCK_NAME, String CTN_NUM, String starTime, String endTime) {
        return countTempleteDao.findReport(BILL_NUM, STOCK_NAME, CTN_NUM, starTime, endTime);
    }

}
