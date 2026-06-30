package com.haiersoft.ccli.cost.web.finance;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
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

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "cost/receivableFeeStat/receivableFeeStatList";
    }

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

    @RequestMapping(value = "export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BisReceivableFeeStat> statList = bisReceivableFeeStatService.search(filters, "statDate", false);
        ExportParams params = new ExportParams("应收费用统计", "应收费用统计", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, BisReceivableFeeStat.class, statList);
        String fileName = new String("应收费用统计.xlsx".getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/msexcel");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
    }
}
