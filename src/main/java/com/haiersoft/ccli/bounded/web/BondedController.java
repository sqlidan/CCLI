package com.haiersoft.ccli.bounded.web;

import com.haiersoft.ccli.bounded.entity.BaseBoundedToNonBounded;
import com.haiersoft.ccli.bounded.service.BaseBoundedToNonBoundedService;
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
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "supervision/bonded")
public class BondedController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BondedController.class);
	@Autowired
	private BaseBoundedService baseBoundedService;
	@Autowired
	private BaseBoundedToNonBoundedService baseBoundedToNonBoundedService;


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "bounded/baseBoundedManage";
	}

	@RequestMapping(value = "listToNonBounded", method = RequestMethod.GET)
	public String listToNonBounded() {
		return "bounded/baseBoundedToNonBoundedManage";
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


		//2024-11-10 徐峥注释
//		Page<BaseBounded> page = getPage(request);
//		page.setOrder("asc");
//		page.setOrderBy("accountBook");
//		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
//		page = baseBoundedService.search(page, filters);
//		return getEasyUIData(page);

		Page<BaseBounded> page = getPage(request);
		BaseBounded baseBounded = new BaseBounded();
		parameterReflect.reflectParameter(baseBounded, request);//转换对应实体类参数
		page = baseBoundedService.searchBaseBounded(page, baseBounded);
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


//===================================================================================================================
	@RequestMapping(value = "nonBoundedJson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getNonBoundedData(HttpServletRequest request) {
		Page<BaseBoundedToNonBounded> page = getPage(request);
		page.setOrder("asc");
		page.setOrderBy("accountBook");
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = baseBoundedToNonBoundedService.search(page, filters);
		return getEasyUIData(page);
	}


	//删除
	@RequestMapping(value = "deleteNonBounded/{id}")
	@ResponseBody
	public String deleteNonBounded(@PathVariable("id") String id) {
		if (id == null && id.equals("")) {
			return "error";
		}

		baseBoundedToNonBoundedService.delete(id);

		return "success";
	}

	//开始转非保税
	@RequestMapping(value = "startTransfer/{id}", method = RequestMethod.GET)
	public String startTransfer(Model model, @PathVariable("id") String id) {
		BaseBounded baseBounded = baseBoundedService.get(id);
		List<BaseBoundedToNonBounded> baseBoundedToNonBoundedList = new ArrayList<>();
		List<PropertyFilter> filters = new ArrayList<>();
		filters.add(new PropertyFilter("EQS_orgId", id));
		filters.add(new PropertyFilter("NULLS_transferFinishTime", null));
		baseBoundedToNonBoundedList = baseBoundedToNonBoundedService.search(filters);
		if (baseBoundedToNonBoundedList!=null && baseBoundedToNonBoundedList.size() > 0){
			int allPrice = 0;
			double allNetWeight = 0;
			for (BaseBoundedToNonBounded forBaseBoundedToNonBounded:baseBoundedToNonBoundedList) {
				allPrice = allPrice + (forBaseBoundedToNonBounded.getNonPiece()==null?0:forBaseBoundedToNonBounded.getNonPiece());
				allNetWeight = allNetWeight + (forBaseBoundedToNonBounded.getNonNetWeight()==null?0:forBaseBoundedToNonBounded.getNonNetWeight());
			}
			int p = baseBounded.getPiece() - allPrice;
			double w = baseBounded.getNetWeight() - allNetWeight;
			baseBounded.setPiece(p);
			baseBounded.setNetWeight(w);
		}else{
			baseBounded.setPiece(baseBounded.getPiece());
			baseBounded.setNetWeight(baseBounded.getNetWeight());
		}
		model.addAttribute("baseBounded", baseBounded);
		model.addAttribute("action", "startTransfer");
		return "bounded/baseBoundedStartTransfer";
	}

	/**
	 * 开始转非保税
	 *
	 * @throws ParseException
	 */
	@RequestMapping(value = "startTransfer", method = RequestMethod.POST)
	@ResponseBody
	public String startTransfer(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		User user = UserUtil.getCurrentUser();

		BaseBoundedToNonBounded insertBaseBounded = new BaseBoundedToNonBounded();
		parameterReflect.reflectParameter(insertBaseBounded, request);//转换对应实体类参数

		BaseBounded queryBaseBounded = baseBoundedService.get(insertBaseBounded.getId());

		BaseBoundedToNonBounded baseBoundedToNonBounded = new BaseBoundedToNonBounded();
		BeanUtils.copyProperties(queryBaseBounded,baseBoundedToNonBounded);

		baseBoundedToNonBounded.setTransferStartBy(user.getLoginName());
		baseBoundedToNonBounded.setTransferStartTime(new Date());
		baseBoundedToNonBounded.setTransferFinishBy(null);
		baseBoundedToNonBounded.setTransferFinishTime(null);
		baseBoundedToNonBounded.setNonCargoLocation(null);
		baseBoundedToNonBounded.setNonCargoArea(null);
		baseBoundedToNonBounded.setOrgId(insertBaseBounded.getId());
		baseBoundedToNonBounded.setNonPiece(insertBaseBounded.getNonPiece());
		baseBoundedToNonBounded.setNonNetWeight(insertBaseBounded.getNonNetWeight());
		baseBoundedToNonBoundedService.save(baseBoundedToNonBounded);
		return "success";
	}

	//完成转非保税
	@RequestMapping(value = "finishTransfer/{id}", method = RequestMethod.GET)
	public String finishTransfer(Model model, @PathVariable("id") String id) {
		BaseBoundedToNonBounded baseBoundedToNonBounded = baseBoundedToNonBoundedService.get(id);
		model.addAttribute("baseBoundedToNonBounded", baseBoundedToNonBounded);
		model.addAttribute("action", "finishTransfer");
		return "bounded/baseBoundedFinishTransfer";
	}

	/**
	 * 完成转非保税
	 *
	 * @throws ParseException
	 */
	@RequestMapping(value = "finishTransfer", method = RequestMethod.POST)
	@ResponseBody
	public String finishTransfer(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		User user = UserUtil.getCurrentUser();

		BaseBoundedToNonBounded updateBaseBoundedToNonBounded = new BaseBoundedToNonBounded();
		parameterReflect.reflectParameter(updateBaseBoundedToNonBounded, request);//转换对应实体类参数

		//修改转出记录表的完成人和完成时间
		BaseBoundedToNonBounded baseBoundedToNonBounded = new BaseBoundedToNonBounded();
		baseBoundedToNonBounded = baseBoundedToNonBoundedService.get(updateBaseBoundedToNonBounded.getId());
		baseBoundedToNonBounded.setNonCargoLocation(updateBaseBoundedToNonBounded.getNonCargoLocation());
		baseBoundedToNonBounded.setNonCargoArea(updateBaseBoundedToNonBounded.getNonCargoArea());
		baseBoundedToNonBounded.setTransferFinishBy(user.getLoginName());
		baseBoundedToNonBounded.setTransferFinishTime(new Date());
		baseBoundedToNonBoundedService.merge(baseBoundedToNonBounded);

		//修改保税底账件数和净重
		BaseBounded queryBaseBounded = baseBoundedService.get(baseBoundedToNonBounded.getOrgId());
		queryBaseBounded.setPiece(queryBaseBounded.getPiece()-baseBoundedToNonBounded.getNonPiece());
		queryBaseBounded.setNetWeight(queryBaseBounded.getNetWeight()-baseBoundedToNonBounded.getNonNetWeight());
		queryBaseBounded.setUpdatedTime(new Date());
		baseBoundedService.merge(queryBaseBounded);
		return "success";
	}


}
