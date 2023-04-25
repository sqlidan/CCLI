package com.haiersoft.ccli.report.web;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.CustomsInStock;
import com.haiersoft.ccli.report.service.CustomsInStockReportService;
/**
 * 
 * @author cuij
 * @ClassName: CustomsInStockReportController
 * @Description: 海关入库报表
 * @date 2016年6月21日10:55:54
 */
@Controller
@RequestMapping("report/customsInStock")
public class CustomsInStockReportController extends BaseController{
	
	@Autowired
	private CustomsInStockReportService customsInStockReportService;
	
	/**
	 * 
	 * @author cuij
	 * @Description:  海关入库报表页面
	 * @date 2016年6月21日10:56:57 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="customsInList", method = RequestMethod.GET)
	public String customsInList() {
		return "report/customsInStockReportList";
	}
	
	/**
	 * 
	 * @author cuij
	 * @Description: 海关入库报表查询
	 * @date 2016年6月21日10:57:35 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<CustomsInStock> page = getPage(request);
		
		CustomsInStock customsInStock = new CustomsInStock();
		parameterReflect.reflectParameter(customsInStock, request);//转换对应实体类参数
		
		page = customsInStockReportService.searchStockReport(page, customsInStock);
		return getEasyUIData(page);
	}
	/**
	 * 
	 * @author cuij
	 * @Description:  导出海关入库报表
	 * @date 2016年6月23日09:24:28 
	 * @param customsStock
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportCustomsInStockExcel")
	@ResponseBody
	public void exportCustomsInStockExcel(@Valid @ModelAttribute @RequestBody CustomsInStock customsInStock, HttpServletRequest request, HttpServletResponse response) throws Exception
	{		
		Map<String, Object> map = new HashMap<String, Object>();
        //List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		TemplateExportParams params = new TemplateExportParams("exceltemplate/customsInStockReport.xls");
		String excelName = "海关入库报表.xls";
		Page<CustomsInStock> page = getPage(request);
		page = customsInStockReportService.searchStockReport(page, customsInStock);
		params = new TemplateExportParams("exceltemplate/customsInStockReport.xls");

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
    
	
    

