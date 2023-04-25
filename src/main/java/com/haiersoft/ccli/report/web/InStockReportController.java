package com.haiersoft.ccli.report.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.CreatPDFUtils;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.ExcelUtil;
import com.haiersoft.ccli.common.utils.MyFileUtils;
import com.haiersoft.ccli.common.utils.MyPDFUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.InStockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.itextpdf.text.PageSize;

/**
 * @author Connor.M
 * @ClassName: InStockReportController
 * @Description: 在库报告书
 * @date 2016年4月26日 下午2:52:38
 */
@Controller
@RequestMapping("report/inStock")
public class InStockReportController extends BaseController {

    @Autowired
    private InStockReportService inStockReportService;

    /**
     * 默认页面
     */
    @RequestMapping(value = "inStockList", method = RequestMethod.GET)
    public String inStockList() {
        return "report/inStock/inStockReportList";
    }

    /**
     * @param stock
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @author Connor.M
     * @Description: 导出 在库报告书
     * @date 2016年4月26日 下午3:23:50
     */
    @RequestMapping("exportInStockExcelbak")
    @ResponseBody
    public void exportInStockExcel(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        TemplateExportParams params = new TemplateExportParams("exceltemplate/inStockReportY.xls");
        String excelName = "在库报告书.xls";

        map.put("customer", "");

        if ("1".equals(stock.getReportType())) {//普通
            //在库数据
            listMap = inStockReportService.searchInStockReport(stock);

            if (stock.getLocationType().equals("1")) {//有库位
                excelName = "在库报告书-有库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportY.xls");
            } else if (stock.getLocationType().equals("2")) {//无库位
                excelName = "在库报告书-无库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportN.xls");
            }
        } else if ("2".equals(stock.getReportType())) {//日本
            //在库数据
            listMap = inStockReportService.searchInStockReportRB(stock);

            if (stock.getLocationType().equals("1")) {//有库位
                excelName = "在库报告书-日本-有库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportY-RB.xls");
            } else if (stock.getLocationType().equals("2")) {//无库位
                excelName = "在库报告书-日本-无库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportN-RB.xls");
            }
        } else if ("3".equals(stock.getReportType())) {//OTE
            //在库数据
            listMap = inStockReportService.searchInStockReportOTE(stock);

            if (stock.getLocationType().equals("1")) {//有库位
                excelName = "在库报告书-OTE-有库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportY-OTE.xls");
            } else if (stock.getLocationType().equals("2")) {//无库位
                excelName = "在库报告书-OTE-无库位.xls";
                params = new TemplateExportParams("exceltemplate/inStockReportN-OTE.xls");
            }
        }

        if (null != listMap && listMap.size() > 0) {
            String customer = listMap.get(0).get("CLIENT_NAME").toString();
            map.put("customer", customer);
            map.put("maplist", listMap);
        }

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式

        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }

    
    @RequestMapping("exportInStockExcel")
    @ResponseBody
    public void exportInStockExcelB(@Valid @ModelAttribute @RequestBody Stock stock,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        String formatFileName = URLEncoder.encode("在库报告书" + ".xls", "UTF-8");
        ExcelUtil excelUtil = new ExcelUtil();
        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");
        String srcPath = null;
        String desPath = null;
        List<Map<String, Object>> listMap=inStockReportService.searchInStockReport(stock);
        if (filePath == null || "".equals(filePath)) {
            filePath = request.getSession().getServletContext().getRealPath("/");
            if ("1".equals(stock.getReportType())) {
                // 普通客户
                //listMap = inStockReportService.searchInStockReport(stock);
                if ("1".equals(stock.getLocationType())) {//有库位
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportY.xls";
                } else {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportN.xls";
                }
            } else if ("2".equals(stock.getReportType())) {
                // 日本客户
                //listMap = inStockReportService.searchInStockReportRB(stock);
                if ("1".equals(stock.getLocationType())) {//有库位
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportY-RB.xls";
                } else {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportN-RB.xls";
                }
            } else if ("3".equals(stock.getReportType())) {
                // OTE
                //listMap = inStockReportService.searchInStockReportOTE(stock);
                if ("1".equals(stock.getLocationType())) {//有库位
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportY-OTE.xls";
                } else {
                    srcPath = filePath + "WEB-INF\\classes\\exceltemplate\\inStockReportN-OTE.xls";
                }
            }
            desPath = filePath + "WEB-INF\\classes\\excelpost\\inStockReportY.xls";
        } else {
            if ("1".equals(stock.getReportType())) {
                //listMap = inStockReportService.searchInStockReport(stock);
                if (stock.getLocationType().equals("1")) {//有库位
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportY.xls";
                } else {
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportN.xls";
                }
            } else if ("2".equals(stock.getReportType())) {
                //listMap = inStockReportService.searchInStockReportRB(stock);
                if (stock.getLocationType().equals("1")) {//有库位
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportY-RB.xls";
                } else {
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportN-RB.xls";
                }
            } else if ("3".equals(stock.getReportType())) {
                //listMap = inStockReportService.searchInStockReportOTE(stock);
                if (stock.getLocationType().equals("1")) {//有库位
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportY-OTE.xls";
                } else {
                    srcPath = filePath + "exceltemplate" + File.separator + "inStockReportN-OTE.xls";
                }
            }
            desPath = filePath + "excelpost" + File.separator + "igrouprmb.xls";
        }
        excelUtil.setSrcPath(srcPath);
        excelUtil.setDesPath(desPath);
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();

        int nTJNum = 0;
        int addRow = 0;
        int myNum = 9;

        if (listMap != null && listMap.size() > 0) {

            String getBillCode = null;
            Map<String, Object> map = null;
            int addNL = 0;
            Double sumPice = 0d;
            Double sumNet = 0d;
            Double sumGross = 0d;
            Double xjPice = 0d;
            Double xjNet = 0d;
            Double xjGross = 0d;
            String custom = null;

            for (int i = 0; i < listMap.size(); i++) {

                map = listMap.get(i);

                if (custom == null || "".equals(custom)) {
                    custom = map.get("CLIENT_NAME") != null ? map.get("CLIENT_NAME").toString() : "";
                    excelUtil.setCellStrValue(4, 8, custom);
                }

                if (getBillCode != null && !getBillCode.equals(map.get("BILL_NUM") != null ? map.get("BILL_NUM").toString() : "")) {
                    excelUtil.setCellStrValue(myNum + i + addRow, 0, "合计/Total");
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum, Double.parseDouble(new DecimalFormat("#.##").format(xjPice)));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum + 3, Double.parseDouble(new DecimalFormat("#.##").format(xjNet)));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum + 4, Double.parseDouble(new DecimalFormat("#.##").format(xjGross)));
                    xjPice = 0d;
                    xjNet = 0d;
                    xjGross = 0d;
                    addRow++;
                }

                getBillCode = map.get("BILL_NUM") != null ? map.get("BILL_NUM").toString() : "";

                excelUtil.setCellStrValue(myNum + i + addRow, 0, getBillCode);
                excelUtil.setCellStrValue(myNum + i + addRow, 2, map.get("SKU_ID") != null ? map.get("SKU_ID").toString() : "");
                excelUtil.setCellStrValue(myNum + i + addRow, 1, map.get("CTN_NUM") != null ? map.get("CTN_NUM").toString() : "");
                excelUtil.setCellStrValue(myNum + i + addRow, 3, map.get("CARGO_NAME") != null ? map.get("CARGO_NAME").toString() : "");
                excelUtil.setCellStrValue(myNum + i + addRow, 4, map.get("CLASS_NAME") != null ? map.get("CLASS_NAME").toString() : "");

                if ("1".equals(stock.getReportType())) {
                    // 普通客户
                    nTJNum = 8;

                    excelUtil.setCellStrValue(myNum + i + addRow, 5, map.get("ENTER_STATE") != null ? map.get("ENTER_STATE").toString() : "");

                    if (stock.getLocationType().equals("1")) {
                        excelUtil.setCellStrValue(myNum + i + addRow, 6, map.get("CARGO_LOCATION") != null ? map.get("CARGO_LOCATION").toString() : "");
                    } else {
                        addNL = -1;
                    }

                    excelUtil.setCellStrValue(myNum + i + addRow, 7 + addNL, map.get("INBOUND_DATE") != null ? map.get("INBOUND_DATE").toString() : "");
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 8 + addNL, Double.parseDouble(map.get("NOW_PIECE_SUM") != null ? map.get("NOW_PIECE_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 9 + addNL, Double.parseDouble(map.get("NET_SINGLE") != null ? map.get("NET_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 10 + addNL, Double.parseDouble(map.get("GROSS_SINGLE") != null ? map.get("GROSS_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 11 + addNL, Double.parseDouble(map.get("NET_WEIGHT_SUM") != null ? map.get("NET_WEIGHT_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 12 + addNL, Double.parseDouble(map.get("GROSS_WEIGHT_SUM") != null ? map.get("GROSS_WEIGHT_SUM").toString() : "0"));

                    nTJNum = nTJNum + addNL;

                } else if ("2".equals(stock.getReportType())) {
                    // 日本客户
                    nTJNum = 9;

//                    excelUtil.setCellStrValue(myNum + i + addRow, 2, map.get("RK_NUM") != null ? map.get("RK_NUM").toString() : "");

                    excelUtil.setCellStrValue(myNum + i + addRow, 5, map.get("RK_NUM") != null ? map.get("RK_NUM").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 6, map.get("ENTER_STATE") != null ? map.get("ENTER_STATE").toString() : "");

                    if (stock.getLocationType().equals("1")) {
                        excelUtil.setCellStrValue(myNum + i + addRow, 7, map.get("CARGO_LOCATION") != null ? map.get("CARGO_LOCATION").toString() : "");
                    } else {
                        addNL = -1;
                    }

                    excelUtil.setCellStrValue(myNum + i + addRow, 8 + addNL, map.get("INBOUND_DATE") != null ? map.get("INBOUND_DATE").toString() : "");
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 9 + addNL, Double.parseDouble(map.get("NOW_PIECE_SUM") != null ? map.get("NOW_PIECE_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 10 + addNL, Double.parseDouble(map.get("NET_SINGLE") != null ? map.get("NET_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 11 + addNL, Double.parseDouble(map.get("GROSS_SINGLE") != null ? map.get("GROSS_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 12 + addNL, Double.parseDouble(map.get("NET_WEIGHT_SUM") != null ? map.get("NET_WEIGHT_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 13 + addNL, Double.parseDouble(map.get("GROSS_WEIGHT_SUM") != null ? map.get("GROSS_WEIGHT_SUM").toString() : "0"));

                    nTJNum = nTJNum + addNL;

                } else if ("3".equals(stock.getReportType())) {
                    // OTE
                    nTJNum = 9;

                    excelUtil.setCellStrValue(myNum + i + addRow, 5, map.get("ENTER_STATE") != null ? map.get("ENTER_STATE").toString() : "");
                    if (stock.getLocationType().equals("1")) {
                        excelUtil.setCellStrValue(myNum + i + addRow, 6, map.get("CARGO_LOCATION") != null ? map.get("CARGO_LOCATION").toString() : "");
                    } else {
                        addNL = -1;
                    }

                    excelUtil.setCellStrValue(myNum + i + addRow, 7 + addNL, map.get("PRO_TIME") != null ? map.get("PRO_TIME").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 8 + addNL, map.get("INBOUND_DATE") != null ? map.get("INBOUND_DATE").toString() : "");
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 9 + addNL, Double.parseDouble(map.get("NOW_PIECE_SUM") != null ? map.get("NOW_PIECE_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 10 + addNL, Double.parseDouble(map.get("NET_SINGLE") != null ? map.get("NET_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 11 + addNL, Double.parseDouble(map.get("GROSS_SINGLE") != null ? map.get("GROSS_SINGLE").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 12 + addNL, Double.parseDouble(map.get("NET_WEIGHT_SUM") != null ? map.get("NET_WEIGHT_SUM").toString() : "0"));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, 13 + addNL, Double.parseDouble(map.get("GROSS_WEIGHT_SUM") != null ? map.get("GROSS_WEIGHT_SUM").toString() : "0"));
                    excelUtil.setCellStrValue(myNum + i + addRow, 14 + addNL, map.get("TYPE_SIZE") != null ? map.get("TYPE_SIZE").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 15 + addNL, map.get("PRO_NUM") != null ? map.get("PRO_NUM").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 16 + addNL, map.get("LOT_NUM") != null ? map.get("LOT_NUM").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 17 + addNL, map.get("MSC_NUM") != null ? map.get("MSC_NUM").toString() : "");
                    excelUtil.setCellStrValue(myNum + i + addRow, 18 + addNL, map.get("ORDER_NUM") != null ? map.get("ORDER_NUM").toString() : "");

                    nTJNum = nTJNum + addNL;
                }

                sumPice += Double.valueOf(map.get("NOW_PIECE_SUM") != null ? map.get("NOW_PIECE_SUM").toString() : "0");
                sumNet += Double.valueOf(map.get("NET_WEIGHT_SUM") != null ? map.get("NET_WEIGHT_SUM").toString() : "0");
                sumGross += Double.valueOf(map.get("GROSS_WEIGHT_SUM") != null ? map.get("GROSS_WEIGHT_SUM").toString() : "0");
                xjPice += Double.valueOf(map.get("NOW_PIECE_SUM") != null ? map.get("NOW_PIECE_SUM").toString() : "0");
                xjNet += Double.valueOf(map.get("NET_WEIGHT_SUM") != null ? map.get("NET_WEIGHT_SUM").toString() : "0");
                xjGross += Double.valueOf(map.get("GROSS_WEIGHT_SUM") != null ? map.get("GROSS_WEIGHT_SUM").toString() : "0");

                //最后一条添加合计
                if (i == listMap.size() - 1) {
                    addRow++;
                    excelUtil.setCellStrValue(myNum + i + addRow, 0, "合计/Total");
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum, Double.parseDouble(new DecimalFormat("#.##").format(xjPice)));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum + 3, Double.parseDouble(new DecimalFormat("#.##").format(xjNet)));
                    excelUtil.setCellDoubleValue(myNum + i + addRow, nTJNum + 4, Double.parseDouble(new DecimalFormat("#.##").format(xjGross)));
                }

            }

            excelUtil.setCellStrValue(myNum + listMap.size() + addRow, nTJNum - 1, "合计/Total");
            excelUtil.setCellDoubleValue(myNum + listMap.size() + addRow, nTJNum, Double.parseDouble(new DecimalFormat("#.##").format(sumPice)));
            excelUtil.setCellDoubleValue(myNum + listMap.size() + addRow, nTJNum + 3, Double.parseDouble(new DecimalFormat("#.##").format(sumNet)));
            excelUtil.setCellDoubleValue(myNum + listMap.size() + addRow, nTJNum + 4, Double.parseDouble(new DecimalFormat("#.##").format(sumGross)));

        }


        excelUtil.exportToNewFile();

        FileInputStream in = new FileInputStream(new File(desPath));

        int len = 0;

        byte[] buffer = new byte[1024];

        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\""); //设定输出文件头
        response.setContentType("application/msexcel"); //定义输出类型

        OutputStream out = response.getOutputStream();

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        if (null != in) {
            in.close();
        }

        if (null != out) {
            out.close();
        }

    }

    /**
     * @param stock
     * @param request
     * @param response
     * @throws Exception
     * @throws
     * @Description: 导出 在库报告书 PDF
     */
    @RequestMapping("exportInStockPDF")
    @ResponseBody
    public void exportInStockPDF(@Valid @ModelAttribute @RequestBody Stock stock, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (stock.getReportType() != null && !"".equals(stock.getReportType())) {
            List<Map<String, Object>> getlist = null;
            String[] headCN = {"提单号", "MR/集装箱号", "SKU", "货物描述", "小类", "货物状态", "入库日期", "件数", "单净重(KGS)", "单毛重(KGS)", "总净重(KGS)", "总毛重(KGS)"};
            String[] headEN = {"B/L NO.", "MR/CTN NO.", "SKU", "Description of cargo", "Sub Class", "State of cargo", "Inbound Date", "Qty", "Net Single", "Gross Single", "Net Weight", "Gross Weight"};
            String pdfTitle = "在库报告书";
            StringBuffer pdfCN = new StringBuffer();
            if ("1".equals(stock.getReportType())) {
                pdfCN.append("提单号"
                        + "                 "
                        + "MR/集装箱号" + "                             "
                        + "SKU" + "                                    "
                        + "货物描述" + "                                    "
                        + "小类" + "                    "
                        + "货物状态" + "                 "
                        + "区位" + "               "
                        + "入库日期" + "           "
                        + "件数" + "              "
                        + "单净重(KGS)" + "      "
                        + "单毛重(KGS)" + "        "
                        + "总净重(KGS)" + "      "
                        + "总毛重(KGS)" + "           "
                );
            }
            if ("2".equals(stock.getReportType())) {
                pdfCN.append("提单号"
                        + "            "
                        + "MR/集装箱号" + "                         "
                        + "SKU" + "                                     "
                        + "入库号" + "          "
                        + "货物描述" + "                                  "
                        + "小类" + "                   "
                        + "货物状态" + "                "
                        + "区位" + "                "
                        + "入库日期" + "          "
                        + "件数" + "           "
                        + "单净重(KGS)" + "    "
                        + "单毛重(KGS)" + "        "
                        + "总净重(KGS)" + "    "
                        + "总毛重(KGS)" + "        "
                );
            }
            if ("3".equals(stock.getReportType())) {
                pdfCN.append("提单号"
                        + "       "
                        + "MR/集装箱号" + "            "
                        + "SKU" + "                             "
                        + "货物描述" + "                      "
                        + "小类" + "           "
                        + "货物状态" + "          "
                        + "区位" + "        "
                        + "入库日期" + "     "
                        + "件数" + "     "
                        + "单净重(KGS)" + " "
                        + "单毛重(KGS)" + " "
                        + "总净重(KGS)" + " "
                        + "总毛重(KGS)" + " "
                        + "生产日期" + "          "
                        + "规格" + "             "
                        + "项目号" + "          "
                        + "船名批名" + "        "
                        + "MSC" + "        "
                        + "ORDER号" + "        "
                );
            }
            // String pdfCN = "提单号"+"                 "+ "MR/集装箱号"+"                       "+"SKU"+"                                           "+"货物描述"+"                                                            "+"小类"+"                          "+"货物状态"+"                    "+"入库日期"+"                          "+"件数"+"                                  "+"总净重(KGS)"+"                         "+"总毛重(KGS)"+"               ";
            //获取web项目的路径"d://exceltemplate//syshtm.html";
            String path = request.getSession().getServletContext().getRealPath("/");
          //  String path="D:/apache-tomcat-8.5.53-windows-x64/apache-tomcat-8.5.53/webapps/ccli";
            String pathHtml = path + "insyshtm.html";
            String pathPdf = path + "insyspdf.pdf";
            StringBuffer sbHtml = new StringBuffer();

            sbHtml.append("<div  style=\"height:5px;\"></div>");
            sbHtml.append("<table id=\"ctable\" style=\"border-spacing:0px;margin:none; text-align:left; border-collapse:collapse;font-family:宋体;font-size:17px;width:100%\">");
            //if ("1".equals(stock.getReportType())) {
            getlist = inStockReportService.searchInStockReport(stock);
            //}
            /*if ("2".equals(stock.getReportType())) {
                getlist = inStockReportService.searchInStockReportRB(stock);
            }
            if ("3".equals(stock.getReportType())) {
                getlist = inStockReportService.searchInStockReportOTE(stock);
            }*/
            String customer = null;
            //填充标题头
            sbHtml.append("<tr style=\"height:30px; \">");
          //  String    lab= URLDecoder.decode(headCN,"UTF-8");
            for ( String lab : headCN) {
            	System.out.println("lab"+lab);
                if ("货物描述".equals(lab)) {
                    sbHtml.append("<td  width=\"300px\">").append(lab).append("</td>");
                } else if ("MR/集装箱号".equals(lab) || "SKU".equals(lab)) {
                    sbHtml.append("<td  width=\"250px\">").append(lab).append("</td>");
                } else {
                    sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                }
                if ("2".equals(stock.getReportType()) && "SKU".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("入库号").append("</td>");
                }
                if ("1".equals(stock.getLocationType()) && "货物状态".equals(lab)) {//有库位
                    sbHtml.append("<td class=\"htd\">").append("区位").append("</td>");
                }
            }
            if ("3".equals(stock.getReportType())) {
                sbHtml.append("<td class=\"htd\">").append("生产日期").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("规格").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("项目号").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("船名批号").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("ORDER号").append("</td>");
            }
            sbHtml.append("</tr>");
            sbHtml.append("<tr style=\"height:30px; \">");
            for (String lab : headEN) {
                sbHtml.append("<td class=\"htd\">").append(lab).append("</td>");
                if ("2".equals(stock.getReportType()) && "SKU".equals(lab)) {
                    sbHtml.append("<td class=\"htd\">").append("Inbound NO.").append("</td>");
                }
                if ("1".equals(stock.getLocationType()) && "State of cargo".equals(lab)) {//有库位
                    sbHtml.append("<td class=\"htd\">").append("Cargo Location").append("</td>");
                }
            }
            if ("3".equals(stock.getReportType())) {
                sbHtml.append("<td class=\"htd\">").append("Produce Date").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("Standard").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("Pro No.").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("Lot No.").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("MSC").append("</td>");
                sbHtml.append("<td class=\"htd\">").append("ORDERNUM").append("</td>");
            }
            sbHtml.append("</tr>");
            int nTJ = 7;
            //填充内容
            if (getlist != null && getlist.size() > 0) {
                Double sumPice = 0d;
                Double sumNet = 0d;
                Double sumGross = 0d;
                Double xjPice = 0d;
                Double xjNet = 0d;
                Double xjGross = 0d;
                String getBillCode = null;
                Map<String, Object> getMap = null;
                for (int i = 0; i < getlist.size(); i++) {
                    getMap = getlist.get(i);
                    if (null == customer) {
                        customer = getMap.get("CLIENT_NAME").toString();
                    }
                    //添加小计
                    if (getBillCode != null && !getBillCode.equals(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "")) {
                        sbHtml.append("<tr><td class=\"ftd\"  style=\"border:0px;height:30px; \">合计/Total：</td>");
                        sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px; \"></td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(xjPice)).append("</td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjNet)).append("</td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjGross)).append("</td>");
                        sbHtml.append("</tr>");
                        xjPice = 0d;
                        xjNet = 0d;
                        xjGross = 0d;
                    }
                    getBillCode = getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "";
                    if (getMap != null && getMap.size() > 0) {
                        sbHtml.append("<tr>");

                        sbHtml.append("<td>").append(getMap.get("BILL_NUM") != null ? getMap.get("BILL_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CTN_NUM") != null ? getMap.get("CTN_NUM").toString() : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("SKU_ID") != null ? getMap.get("SKU_ID").toString() : "").append("</td>");
                        if ("2".equals(stock.getReportType())) {
                            sbHtml.append("<td>").append(getMap.get("RK_NUM") != null ? getMap.get("RK_NUM").toString() : "").append("</td>");
                            nTJ = 8;
                        }
                        sbHtml.append("<td >").append(getMap.get("CARGO_NAME") != null ? getMap.get("CARGO_NAME").toString().replaceAll("<", "《").replaceAll(">", "》").replaceAll("&", "&amp;") : "").append("</td>");
                        sbHtml.append("<td>").append(getMap.get("CLASS_NAME") != null ? getMap.get("CLASS_NAME").toString() : "").append("</td>");

                        sbHtml.append("<td>").append(getMap.get("ENTER_STATE") != null ? getMap.get("ENTER_STATE").toString() : "").append("</td>");
                        if ("1".equals(stock.getLocationType())) {//有库位
                            sbHtml.append("<td>").append(getMap.get("CARGO_LOCATION") != null ? getMap.get("CARGO_LOCATION").toString() : "").append("</td>");
                        }
                        sbHtml.append("<td>").append(getMap.get("INBOUND_DATE") != null ? getMap.get("INBOUND_DATE") : "").append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####").format(Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NET_SINGLE") != null ? getMap.get("NET_SINGLE").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("GROSS_SINGLE") != null ? getMap.get("GROSS_SINGLE").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0"))).append("</td>");
                        sbHtml.append("<td>").append(new DecimalFormat("####.00").format(Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0"))).append("</td>");
                        if ("3".equals(stock.getReportType())) {
                            sbHtml.append("<td>").append(getMap.get("PRO_TIME") != null ? getMap.get("PRO_TIME").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("TYPE_SIZE") != null ? getMap.get("TYPE_SIZE").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("PRO_NUM") != null ? getMap.get("PRO_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("LOT_NUM") != null ? getMap.get("LOT_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("MSC_NUM") != null ? getMap.get("MSC_NUM").toString() : "").append("</td>");
                            sbHtml.append("<td>").append(getMap.get("ORDER_NUM") != null ? getMap.get("ORDER_NUM").toString() : "").append("</td>");
                            nTJ = 8;//nTJ=7->8
                        }
                        if (!"1".equals(stock.getLocationType())) {//no库位
                            nTJ = nTJ - 1;
                        }
                        sbHtml.append("</tr>");
                        sumPice += Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0");
                        sumNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
                        sumGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
                        xjPice += Double.valueOf(getMap.get("NOW_PIECE_SUM") != null ? getMap.get("NOW_PIECE_SUM").toString() : "0");
                        xjNet += Double.valueOf(getMap.get("NET_WEIGHT_SUM") != null ? getMap.get("NET_WEIGHT_SUM").toString() : "0");
                        xjGross += Double.valueOf(getMap.get("GROSS_WEIGHT_SUM") != null ? getMap.get("GROSS_WEIGHT_SUM").toString() : "0");
                    }
                    //最后一条添加合计
                    if (i == getlist.size() - 1) {
                        sbHtml.append("<tr><td class=\"ftd\"  style=\"border:0px;height:30px; \">合计/Total：</td>");
                        sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px; \"></td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(xjPice)).append("</td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjNet)).append("</td>");
                        sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(xjGross)).append("</td>");
                        sbHtml.append("</tr>");
                    }
                }//end for

                //添加合计
                sbHtml.append("<tr><td class=\"ftd\" style=\"border:0px; \">合计/Total：</td>");
                sbHtml.append("<td class=\"ftd\" colspan=\"" + ("1".equals(stock.getReportType()) ? ("1".equals(stock.getLocationType()) ? 7 : 6) : nTJ) + "\" style=\"border:0px;height:30px; \"></td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####").format(sumPice)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \"></td> ");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumNet)).append("</td>");
                sbHtml.append("<td class=\"ftd\" style=\"border:0px; \">").append(new DecimalFormat("####.00").format(sumGross)).append("</td>");
                sbHtml.append("</tr>");
            }
            sbHtml.append("</table>");

            sbHtml.append("<table style=\"border-spacing:0px;text-align:center; border-collapse:collapse;font-family:宋体;font-size:12px;width:100%\"><tr>");
            @SuppressWarnings("unused")
            User user = UserUtil.getCurrentUser();
            sbHtml.append("<td style=\"text-align:right;margin-top:5px;\">").append(" PrintTime : &nbsp;").append(DateUtils.getDateTime()).append("</td>");
            sbHtml.append("</tr></table>");
            
            MyFileUtils html = new MyFileUtils();
            System.out.println("pathHtml"+pathHtml);
            html.setFilePath(pathHtml);
            
            html.saveStrToFile(CreatPDFUtils.createPdfHtmlInStock("在库报告书", "Inventory Report", "The Customer:", customer, sbHtml.toString()));
            MyPDFUtils.setsDEST(pathPdf);
            MyPDFUtils.setsHTML(pathHtml);
            MyPDFUtils.createPdf(PageSize.A3, pdfCN.toString(), pdfTitle);


            //下载操作
            FileInputStream in = new FileInputStream(new File(pathPdf));
            int len = 0;
            byte[] buffer = new byte[1024];
            String formatFileName = URLEncoder.encode("在库报告书" + ".pdf", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }
        }


    }
    
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
    	Page<Stock> page = getPage(request);
    	Stock stock = new Stock();
    	parameterReflect.reflectParameter(stock, request);
    	page = inStockReportService.getStocks(page, stock);
    	return getEasyUIData(page);
    }
}
