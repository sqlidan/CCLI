package com.haiersoft.ccli.report.web;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.OutStock;
import com.haiersoft.ccli.report.service.OutStockReportService;

/**
 * 
 * @author Mazy
 * @ClassName: OutStockReportController
 * @Description: 海关出库报表
 * @date 2016年6月24日 上午9:35:31
 */
@Controller
@RequestMapping("report/outStock")
public class OutStockReportController extends BaseController{
	
	@Autowired
	private OutStockReportService outStockReportService;
	
	/**
	 * 
	 * @author Mazy
	 * @Description:  海关出库报表页面
	 * @date 2016年3月9日 下午2:36:17 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="outlist", method = RequestMethod.GET)
	public String list() {
		return "report/outStockReportList";
	}
	
	/**
	 * 
	 * @author Mazy
	 * @Description:  海关出库报表查询
	 * @date 2016年3月9日 下午2:36:17 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<OutStock> page = getPage(request);
		
		OutStock outStock = new OutStock();
		parameterReflect.reflectParameter(outStock, request);//转换对应实体类参数
		
		page = outStockReportService.searchStockReport(page, outStock);
		return getEasyUIData(page);
	}
	
	/**
	 * @Description: 在库明细 导出 跳转
	 * @return 
	 * @throws
	 * @author lzg
	 */
	@RequestMapping(value="excel", method = RequestMethod.GET)
	public String toexcel(Model model) {
		Date now=new Date();
		model.addAttribute("strTime", DateUtils.getDateStart(now));
		model.addAttribute("endTime", DateUtils.getDateEnd(now));
		return "report/outStockReportService";
	}
	/***
     * 根据查询条件，导出海关出库报表
     * @param request
     * @param response
     * @throws Exception
     * @author mzy
     */
	@RequestMapping("exportCustomsOutStockExcel")
	@ResponseBody
	public void exportCustomsOutStockExcel(@Valid @ModelAttribute @RequestBody OutStock outStock, HttpServletRequest request, HttpServletResponse response) throws Exception
	{		
		Map<String, Object> map = new HashMap<String, Object>();
        //List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		TemplateExportParams params = new TemplateExportParams("exceltemplate/customsOutStockReport.xls");
		String excelName = "海关出库报表.xls";
		Page<OutStock> page = getPage(request);
		page = outStockReportService.searchStockReport(page, outStock);
		params = new TemplateExportParams("exceltemplate/customsOutStockReport.xls");

		if(null != page){
			map.put("maplist", page.getResult());
		}

		Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	    workbook.getSheetAt(0).setForceFormulaRecalculation(true);//强制执行公式
	    
		String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName +"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
    
    
}
