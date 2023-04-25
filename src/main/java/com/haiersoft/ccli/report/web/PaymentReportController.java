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
import com.haiersoft.ccli.report.entity.PaymentReportStock;
import com.haiersoft.ccli.report.service.PaymentReportService;

/**
 * 
 * @author cuij
 * @ClassName: PaymentReportController
 * @Description: 业务付款申请单汇总表
 * @date 2016年6月23日19:58:57
 */
@Controller
@RequestMapping("report/paymentReportStock")
public class PaymentReportController extends BaseController{
	
	@Autowired
	private PaymentReportService paymentReportService;
	
	/**
	 * 
	 * @author cuij
	 * @Description:  业务付款申请单汇总表页面
	 * @date 2016年6月24日09:25:13 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="paymentReportList", method = RequestMethod.GET)
	public String paymentReportList() {
		return "report/paymentReportList";
	}
	
	/**
	 * 
	 * @author cuij
	 * @Description: 业务付款申请单汇总表查询
	 * @date 2016年6月24日09:26:00 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<PaymentReportStock> page = getPage(request);
		
		PaymentReportStock paymentReportStock = new PaymentReportStock();
		parameterReflect.reflectParameter(paymentReportStock, request);//转换对应实体类参数
		
		page = paymentReportService.searchStockReport(page, paymentReportStock);
		return getEasyUIData(page);
	}
	/**
	 * 
	 * @author cuij
	 * @Description:  导出业务付款申请单汇总表
	 * @date 2016年6月24日09:31:10 
	 * @param paymentReportStock
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportPaymentReportStockExcel")
	@ResponseBody
	public void exportPaymentReportStockExcel(@Valid @ModelAttribute @RequestBody PaymentReportStock paymentReportStock, HttpServletRequest request, HttpServletResponse response) throws Exception
	{		
		Map<String, Object> map = new HashMap<String, Object>();
        //List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		TemplateExportParams params = new TemplateExportParams("exceltemplate/paymentReport.xls");
		String excelName = "业务付款申请单汇总表.xls";
		Page<PaymentReportStock> page = getPage(request);
		page = paymentReportService.searchStockReport(page, paymentReportStock);
		params = new TemplateExportParams("exceltemplate/paymentReport.xls");

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
    
	
    

