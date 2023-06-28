package com.haiersoft.ccli.bounded.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;

import com.haiersoft.ccli.bounded.entity.BaseBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "supervision/bonded")
public class BondedController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BondedController.class);
	@Autowired
	private BaseBoundedService baseBoundedService;


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "bounded/baseBoundedManage";
	}


/*	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "platform/user/baseBoundedInfo";
	}
  */


	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
/*		Page<baseBounded> page = getPage(request);
//		page.orderBy("id").order(Page.DESC);
		baseBounded baseBounded = new baseBounded();
        parameterReflect.reflectParameter(baseBounded, request);
//		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page = baseBoundedService.seachCustomsClearanceSql(page, baseBounded);
		return getEasyUIData(page);*/


		Page<BaseBounded> page = getPage(request);
		page.setOrder("asc");
		page.setOrderBy("accountBook");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = baseBoundedService.search(page, filters);
		return getEasyUIData(page);
	}


	/**
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("baseBounded", new BaseBounded());
		model.addAttribute("action", "create");
		return "bounded/baseBoundedInfo";
	}


	/**
	 * 新增保存报关单
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response) {
		BaseBounded baseBounded = new BaseBounded();
		parameterReflect.reflectParameter(baseBounded, request);//转换对应实体类参数


		User user = UserUtil.getCurrentUser();
		//baseBounded.setOperator(user.getName());
		baseBounded.setCreatedTime(new Date());
		// baseBounded.setDeletedFlag("0");
		List<Map<String, Object>> list = baseBoundedService.getLoactionsByBillNum(baseBounded);
		Map map=list.get(0);
		String loactions = map.get("LOCATIONCODE")!= null ? map.get("LOCATIONCODE").toString():"";
		String area =  map.get("AREANUM")!= null ? map.get("AREANUM").toString():"";

		baseBounded.setCargoLocation(loactions);
		baseBounded.setCargoArea(area);
		try {
			baseBoundedService.save(baseBounded);
		} catch (DataIntegrityViolationException ex) {
			return "duplicate";
		}

		return "success";
	}


	//修改跳转
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") String id) {
		BaseBounded baseBounded = baseBoundedService.get(id);
		model.addAttribute("baseBounded", baseBounded);
		model.addAttribute("action", "update");
		return "bounded/baseBoundedInfo";
	}

	/**
	 * 修改保存报关单
	 *
	 * @throws ParseException
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);*/
		BaseBounded baseBounded = new BaseBounded();
		parameterReflect.reflectParameter(baseBounded, request);//转换对应实体类参数

		baseBounded.setUpdatedTime(new Date());
      List<Map<String, Object>> list = baseBoundedService.getLoactionsByBillNum(baseBounded);

		Map map=list.get(0);
        String loactions = map.get("LOCATIONCODE")!= null ? map.get("LOCATIONCODE").toString():"";
		String area =  map.get("AREANUM")!= null ? map.get("AREANUM").toString():"";
		baseBounded.setCargoLocation(loactions);
		baseBounded.setCargoArea(area);

		baseBoundedService.update(baseBounded);
		return "success";
	}


	//删除
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		if (id == null && id.equals("")) {
			return "error";
		}

		baseBoundedService.delete(id);

		return "success";

	}

	@RequestMapping(value = "exportExcel")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		Page<PlatformWorkTicket> pageData = getPage(request);

		// clientList.getResult();
		List<PlatformWorkTicket> result = null;

		List<BaseBounded> resultExcelList = baseBoundedService.search(filters, "accountBook", true);


		ExportParams params = new ExportParams("保税货物底账导出", "保税货物底账Sheet", ExcelType.XSSF);

		Workbook workbook = ExcelExportUtil.exportExcel(params, BaseBounded.class, resultExcelList);

		String formatFileNameP = "保税货物底账" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
}
