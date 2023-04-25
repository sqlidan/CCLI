package com.haiersoft.ccli.supervision.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.BaseAccount;
import com.haiersoft.ccli.supervision.entity.BaseAccountExcel;
import com.haiersoft.ccli.supervision.service.BaseAccountService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 分类监管 底账明细controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/baseAccountStatistics")
public class BaseAccountStatisticsController extends BaseController{
    
	@Autowired
	private BaseAccountService baseAccountService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fljg/accountStatisticsList";
	}


	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseAccount> page = getPage(request);
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);

		page = baseAccountService.seachStatisticsSql(page, map);
		return getEasyUIData(page);
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 * @Description: 导出excel
	 * @date 2023年3月24日 下午2:22:55
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);


		List<BaseAccountExcel> baseAccountList = baseAccountService.seachStatisticsSql(map);

		ExportParams params = new ExportParams("底账汇总", "底账汇总Sheet", ExcelType.XSSF);

		Workbook workbook = ExcelExportUtil.exportExcel(params, BaseAccountExcel.class, baseAccountList);

		String formatFileNameP = "底账汇总" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

}
