package com.haiersoft.ccli.report.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.TrayHistoryReportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dxl584327830 on 16/8/15.
 */
@Controller
@RequestMapping("report/history")
public class TrayHistoryReportController extends BaseController {

    @Autowired
    private TrayHistoryReportService trayHistoryReportService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "report/trayHistoryList";
    }

    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {

        Page<Stock> page = getPage(request);

        Stock param = new Stock();
        parameterReflect.reflectParameter(param, request);

        page = trayHistoryReportService.searchTrayHistory(page, param);

        Map<String, Object> resultMap = getEasyUIData(page);

        return resultMap;
    }

    @RequestMapping(value = "exportReportExcel", method = RequestMethod.GET)
    @ResponseBody
    public void exportReportExcel(@Valid @ModelAttribute @RequestBody Stock stock,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        mapList = trayHistoryReportService.searchAllTrayHistoryByParams(stock);

        TemplateExportParams params = new TemplateExportParams("exceltemplate/trayHistoryExcel.xls");
        String excelName = "库存历史总览.xls";


//        Map<String, Object> rows = getData(request);
//
//        List<Stock> data = (List<Stock>) rows.get("rows");
//
//        List<Map<String, String>> listmap = new ArrayList<>();
//
//        for (Stock s : data) {
//            Map<String, String> tmp = new HashMap<>();
//
//            tmp.put("trayCode", s.getTrayCode());
//            tmp.put("billCode", s.getBillCode());
//            tmp.put("ctnNum", s.getCtnNum());
//            tmp.put("asn", s.getAsn());
//            tmp.put("sku", s.getSku());
//            tmp.put("contactCode", s.getContactCode());
//            tmp.put("clientName", s.getClientName());
//            tmp.put("warehouse", s.getWarehouse());
//            tmp.put("locationCode", s.getLocationCode());
//            tmp.put("cargoName", s.getCargoName());
//            tmp.put("nowNum", s.getNowNum().toString());
//            tmp.put("netWeight", s.getNetWeight().toString());
//            tmp.put("grossWeight", s.getGrossWeight().toString());
//            tmp.put("units", "1".equals(s.getUnits()) ? "千克" : "吨");
//            tmp.put("state", "已上架");
//            tmp.put("enterTime", s.getEnterTime().toString());
//            tmp.put("enterPerson", s.getEnterPerson());
//            tmp.put("enterOp", s.getEnterOp());
//            tmp.put("backupTime", s.getBackupTime().toString());
//
//            listmap.add(tmp);
//        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mapList", mapList);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式

        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流

    }

}
