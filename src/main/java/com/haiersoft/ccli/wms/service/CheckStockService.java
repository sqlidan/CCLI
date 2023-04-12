package com.haiersoft.ccli.wms.service;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.dao.CheckStockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CheckStockService {

    @Autowired
    private CheckStockDAO checkStockDAO;

    public Page<Stock> checkStockByTrayId(Page<Stock> page, Stock stock) {
        return checkStockDAO.checkStockByTrayId(page, stock);
    }

    public List<Map<String, Object>> findStockByTrayId(Stock stock) {
        return checkStockDAO.findStockByTrayId(stock);
    }

    public Page<Stock> checkStockByCtn(Page<Stock> page, Stock stock) {
        return checkStockDAO.checkStockByCtn(page, stock);
    }

    public List<Map<String, Object>> findStockByCtn(Stock stock) {
        return checkStockDAO.findStockByCtn(stock);
    }

}
