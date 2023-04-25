package com.haiersoft.ccli.report.web;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.service.CountTempleteService;
import com.haiersoft.ccli.wms.entity.CountTemplete;

/**
 * @Description: 财务仓储统计表模版
 */
@Controller
@RequestMapping("report/countTemplete")
public class CountTempleteController extends BaseController {

    @Autowired
    private CountTempleteService countTempleteService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String paymentReportList() {
        return "report/countTempleteReport";
    }

    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<CountTemplete> page = getPage(request);
        CountTemplete countTemplete = new CountTemplete();//实体累
        parameterReflect.reflectParameter(countTemplete, request);//转换对应实体类参数

        page = countTempleteService.searchStockReport(page, countTemplete);
        return getEasyUIData(page);
    }

    @RequestMapping("outport")
    @ResponseBody
    public void exportPaymentReportStockExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String BILL_NUM = request.getParameter("BILL_NUM");
        String STOCK_NAME = request.getParameter("STOCK_NAME");
        String CTN_NUM = request.getParameter("CTN_NUM");
        String starTime = request.getParameter("starTime");
        String endTime = request.getParameter("endTime");

        TemplateExportParams params = new TemplateExportParams("exceltemplate/countTempleteReport.xls");

        List<Map<String, Object>> mapList = countTempleteService.findReport(BILL_NUM, STOCK_NAME, CTN_NUM, starTime, endTime);

        String excelName = "财务仓储统计表模版.xls";

        Map<String, Object> map = new HashMap<>();
        map.put("mapList", mapList);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.getSheetAt(0).setForceFormulaRecalculation(true);// 强制执行公式

        String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");

        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型

        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流

    }

}
