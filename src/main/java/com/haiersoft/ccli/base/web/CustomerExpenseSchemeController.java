package com.haiersoft.ccli.base.web;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeInfoView;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeQuery;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeView;
import com.haiersoft.ccli.base.service.CustomerExpenseSchemeService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 客户费用方案明细页面控制器。
 */
@Controller
@RequestMapping("base/customerExpenseScheme")
public class CustomerExpenseSchemeController extends BaseController {

    @Autowired
    private CustomerExpenseSchemeService customerExpenseSchemeService;

    /**
     * 打开客户费用方案明细列表页面。
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list() {
        return "base/scheme/customerExpenseSchemeList";
    }

    /**
     * 分页查询费用方案主表及关联合同主表数据，供客户费用方案明细列表使用。
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> json(HttpServletRequest request, CustomerExpenseSchemeQuery query) {
        Page<CustomerExpenseSchemeView> page = getPage(request);
        page = customerExpenseSchemeService.findPage(page, query);
        return getEasyUIData(page);
    }

    /**
     * 打开客户费用方案详情页面，展示费用方案明细及关联合同费目。
     */
    @RequestMapping(value = "detail/{schemeNum}", method = RequestMethod.GET)
    public String detail(@PathVariable("schemeNum") String schemeNum, Model model) {
        model.addAttribute("scheme", customerExpenseSchemeService.findBySchemeNum(schemeNum));
        return "base/scheme/customerExpenseSchemeDetail";
    }

    /**
     * 按当前筛选条件导出费用方案子表数据。
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    @ResponseBody
    public void export(CustomerExpenseSchemeQuery query, HttpServletResponse response) throws Exception {
        List<CustomerExpenseSchemeInfoView> schemeInfoList = customerExpenseSchemeService.findInfoList(query);

        ExportParams exportParams = new ExportParams("客户费用方案明细", "费用方案明细", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CustomerExpenseSchemeInfoView.class, schemeInfoList);
        String fileName = new String("客户费用方案明细.xlsx".getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/msexcel");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
