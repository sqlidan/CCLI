package com.haiersoft.ccli.wms.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.wms.entity.BisCheckStock;
import com.haiersoft.ccli.wms.service.CheckStockService;
import com.haiersoft.ccli.wms.service.StockFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点模块（托盘盘点、箱号盘点）
 */
@Controller
@RequestMapping("stock/check")
public class CheckStockController extends BaseController {

    @Autowired
    private CheckStockService checkStockService;

    @Autowired
    private StockFeedbackService feedbackService;

    /**
     * 托盘号盘点
     */
    @RequestMapping("tray")
    public String tray() {
        return "wms/stock/check/tray";
    }

    /**
     * 箱号盘点
     */
    @RequestMapping("ctn")
    public String ctn() {
        return "wms/stock/check/ctn";
    }

    /**
     * 根据托盘号获取库存信息
     */
    @RequestMapping("tray/search")
    @ResponseBody
    public Map<String, Object> checkStockByTrayId(HttpServletRequest request) {

        Stock stock = reflectParameter(request, Stock.class);

        Page<Stock> page = getPage(request);

        checkStockService.checkStockByTrayId(page, stock);

        return getEasyUIData(page);
    }

    /**
     * 根据托盘号生成盘点报表
     */
    @RequestMapping("tray/excel")
    @ResponseBody
    public void exportExcelByTray(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Map<String, Object>> result = checkStockService.findStockByTrayId(stock);

        ExcelUtil excelUtil = new ExcelUtil();

        String fileName = ExcelUtil.encodeFileName("库存盘点");
        String filePath = ExcelUtil.getRootPath(request);
        //String srcPath = StringUtils.combineToPath(filePath, "exceltemplate", "stockCheckByTray.xls");//20180719修改
        String srcPath = filePath + "exceltemplate\\stockCheckByTray.xls";
        //String desPath = StringUtils.combineToPath(filePath, "WEB-INF", "classes", "excelpost", "stockCheckByTray.xls");
        String desPath = filePath + "excelpost\\stockCheckByTray.xls";
        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();

        int startRow = 10;
        int startColumn = 0;

        for (int i = 0; i < result.size(); i++) {

            Map<String, Object> row = result.get(i);

            excelUtil.setCellStrValue(startRow + i, startColumn, (row.get("LOCATIONCODE")==null?"":row.get("LOCATIONCODE")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 1, (row.get("TRAYCODE")==null?"":row.get("TRAYCODE")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 2, (row.get("BILLCODE")==null?"":row.get("BILLCODE")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 3, (row.get("CTNNUM")==null?"":row.get("CTNNUM")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 4, (row.get("CLIENTNAME")==null?"":row.get("CLIENTNAME")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 5, (row.get("SKU")==null?"":row.get("SKU")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 6, (row.get("CARGONAME")==null?"":row.get("CARGONAME")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 7, (row.get("NOWNUM")==null?"":row.get("NOWNUM")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 8, (row.get("NETWEIGHT")==null?"":row.get("NETWEIGHT")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 9, (row.get("GROSSWEIGHT")==null?"":row.get("GROSSWEIGHT")).toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 10, Integer.parseInt((row.get("UNITS")==null?"":row.get("UNITS")).toString()) == 1 ? "千克" : "吨");

        }

        excelUtil.exportToNewFile();

        transportExcel(response, desPath, fileName);

    }

    /**
     * 根据箱号获取库存信息
     */
    @RequestMapping("ctn/search")
    @ResponseBody
    public Map<String, Object> checkStockByCtn(HttpServletRequest request) {

        Stock stock = reflectParameter(request, Stock.class);

        Page<Stock> page = getPage(request);

        checkStockService.checkStockByCtn(page, stock);

        return getEasyUIData(page);

    }

    /**
     * 根据箱号生成盘点报表
     */
    @RequestMapping("ctn/excel")
    @ResponseBody
    public void exportExcelByCtn(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Map<String, Object>> result = checkStockService.findStockByCtn(stock);

        ExcelUtil excelUtil = new ExcelUtil();

        String fileName = ExcelUtil.encodeFileName("库存盘点");
        String filePath = ExcelUtil.getRootPath(request);
        String srcPath = StringUtils.combineToPath(filePath, "WEB-INF", "classes", "exceltemplate", "stockCheckByCtn.xls");
        String desPath = StringUtils.combineToPath(filePath, "WEB-INF", "classes", "excelpost", "stockCheckByCtn.xls");

        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();

        int startRow = 10;
        int startColumn = 0;

        for (int i = 0; i < result.size(); i++) {

            Map<String, Object> row = result.get(i);

            excelUtil.setCellStrValue(startRow + i, startColumn, row.get("CTNNUM").toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 1, row.get("ALLPIECE").toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 2, row.get("ALLNET").toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 3, row.get("ALLGROSS").toString());
            excelUtil.setCellStrValue(startRow + i, startColumn + 4, Integer.parseInt(row.get("UNITS").toString()) == 1 ? "千克" : "吨");
        }

        excelUtil.exportToNewFile();

        transportExcel(response, desPath, fileName);

    }

    @RequestMapping("record")
    public String stockRecord() {
        return "wms/stock/record";
    }

    @RequestMapping("feedback")
    public String stockCheckFeedback(Model model, String trayId, String ctnNum, String action) {

        List<Map<String, Object>> result;

        Stock stock = new Stock();

        if ("trayId".equals(action) && isNotNull(trayId)) {

            stock.setTrayCode(trayId);

            result = checkStockService.findStockByTrayId(stock);

            Map<String, Object> row = result.get(0);

            model.addAttribute("trayCode", row.get("TRAYCODE"));
            model.addAttribute("billCode", row.get("BILLCODE"));
            model.addAttribute("ctnNum", row.get("CTNNUM"));
            model.addAttribute("sku", row.get("SKU"));
            model.addAttribute("cargoName", row.get("CARGONAME"));
            model.addAttribute("nowNum", row.get("NOWNUM"));
            model.addAttribute("netWeight", row.get("NETWEIGHT"));
            model.addAttribute("grossWeight", row.get("GROSSWEIGHT"));
            model.addAttribute("units", row.get("UNITS"));

        } else if ("ctnNum".equals(action) && isNotNull(ctnNum)) {

            stock.setCtnNum(ctnNum);

            result = checkStockService.findStockByCtn(stock);

            Map<String, Object> row = result.get(0);

            model.addAttribute("ctnNum", row.get("CTNNUM"));
            model.addAttribute("cargoName", row.get("CARGONAME"));
            model.addAttribute("nowNum", row.get("ALLPIECE"));
            model.addAttribute("netWeight", row.get("ALLNET"));
            model.addAttribute("grossWeight", row.get("ALLGROSS"));
            model.addAttribute("units", row.get("UNITS"));

        }

        model.addAttribute("action", action);

        return "wms/stock/check/stockCheckFeedback";
    }

    /**
     * 根据箱号获取库存信息
     */
    @RequestMapping("record/search")
    @ResponseBody
    public Map<String, Object> pageRecordStock(HttpServletRequest request) {

        BisCheckStock checkStock = reflectParameter(request, BisCheckStock.class);

        Page<BisCheckStock> page = getPage(request);

        page = feedbackService.pageRecordStock(page, checkStock);

        return getEasyUIData(page);

    }

    @RequestMapping("feedback/add")
    @ResponseBody
    public String addFeekback(@ModelAttribute @RequestBody BisCheckStock checkStock) {

        feedbackService.merge(checkStock);

        return "success";
    }

}
