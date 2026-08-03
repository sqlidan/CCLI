package com.haiersoft.ccli.cost.web.finance;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.finance.BisReceivableFeeStat;
import com.haiersoft.ccli.cost.service.finance.BisReceivableFeeStatService;

@Controller
@RequestMapping("cost/receivableFeeStat")
public class ReceivableFeeStatController extends BaseController {

    @Autowired
    private BisReceivableFeeStatService bisReceivableFeeStatService;

    /**
     * 进入应收费用统计列表页面。
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "cost/receivableFeeStat/receivableFeeStatList";
    }

    /**
     * 按当前页面筛选条件分页查询应收费用统计数据。
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisReceivableFeeStat> page = getPage(request);
        page.setOrderBy("statDate");
        page.setOrder(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = bisReceivableFeeStatService.search(page, filters);
        return getEasyUIData(page);
    }

    /**
     * 按当前页面传入的筛选条件导出应收费用统计数据。
     */
    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String statDateStart = request.getParameter("filter_GED_statDate");
        String statDateEnd = request.getParameter("filter_LED_statDate");
        if (!hasText(statDateStart) || !hasText(statDateEnd)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("请选择统计日期开始和结束后再导出！");
            return;
        }
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BisReceivableFeeStat> statList = bisReceivableFeeStatService.search(filters, "statDate", false);
        Workbook workbook = buildExportWorkbook(statList, statDateStart, statDateEnd);
        String fileName = new String("应收费用统计.xlsx".getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/msexcel");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
    }

    /**
     * 生成应收费用统计导出文件，包含统计区间、格式化表头和金额格式。
     */
    private Workbook buildExportWorkbook(List<BisReceivableFeeStat> statList, String statDateStart, String statDateEnd) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("应收费用统计");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
        sheet.setColumnWidth(0, 32 * 256);
        sheet.setColumnWidth(1, 14 * 256);
        sheet.setColumnWidth(2, 14 * 256);
        sheet.setColumnWidth(3, 14 * 256);
        sheet.setColumnWidth(4, 24 * 256);
        sheet.setColumnWidth(5, 16 * 256);
        sheet.createFreezePane(0, 3);

        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle rangeStyle = createRangeStyle(workbook);
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle textStyle = createTextStyle(workbook);
        CellStyle amountStyle = createAmountStyle(workbook);

        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(28);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("应收费用统计");
        titleCell.setCellStyle(titleStyle);

        Row rangeRow = sheet.createRow(1);
        rangeRow.setHeightInPoints(22);
        Cell rangeCell = rangeRow.createCell(0);
        rangeCell.setCellValue("统计区间：" + statDateStart + " 至 " + statDateEnd);
        rangeCell.setCellStyle(rangeStyle);

        String[] headers = { "客户名称", "账期", "统计日期", "费目代码", "费目", "金额" };
        Row headerRow = sheet.createRow(2);
        headerRow.setHeightInPoints(22);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int rowIndex = 3;
        for (BisReceivableFeeStat stat : statList) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeightInPoints(20);
            createTextCell(row, 0, stat.getCustomerName(), textStyle);
            createTextCell(row, 1, stat.getAccountPeriod(), textStyle);
            createTextCell(row, 2, stat.getStatDate() == null ? "" : dateFormat.format(stat.getStatDate()), textStyle);
            createTextCell(row, 3, stat.getFeeCode(), textStyle);
            createTextCell(row, 4, stat.getFeeName(), textStyle);
            Cell amountCell = row.createCell(5);
            amountCell.setCellValue(stat.getAmount() == null ? 0D : stat.getAmount().doubleValue());
            amountCell.setCellStyle(amountStyle);
        }
        sheet.setAutoFilter(new CellRangeAddress(2, Math.max(2, rowIndex - 1), 0, 5));
        return workbook;
    }

    /**
     * 创建导出标题样式。
     */
    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    /**
     * 创建统计区间样式。
     */
    private CellStyle createRangeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    /**
     * 创建表头样式。
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        return style;
    }

    /**
     * 创建文本单元格样式。
     */
    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        return style;
    }

    /**
     * 创建金额单元格样式，金额统一保留两位小数。
     */
    private CellStyle createAmountStyle(Workbook workbook) {
        CellStyle style = createTextStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("#,##0.00"));
        return style;
    }

    /**
     * 写入文本单元格。
     */
    private void createTextCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    /**
     * 判断导出参数是否已填写。
     */
    private boolean hasText(String value) {
        return value != null && value.trim().length() > 0;
    }
}
