package com.haiersoft.ccli.report.service;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.report.dao.TrayHistoryReportDao;
import com.haiersoft.ccli.report.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * Created by dxl584327830 on 16/8/15.
 */
@Service
public class TrayHistoryReportService {

    @Autowired
    TrayHistoryReportDao trayHistoryReportDao;

    public Page<Stock> searchTrayHistory(Page<Stock> page, Stock param) {
        return trayHistoryReportDao.searchTrayHistory(page, param);
    }

    public void exportReportExcel(HttpServletRequest request) {


    }

    public List<Map<String, Object>> searchAllTrayHistoryByParams(Stock params) {
        return trayHistoryReportDao.searchAllTrayHistoryByParams(params);
    }

}
