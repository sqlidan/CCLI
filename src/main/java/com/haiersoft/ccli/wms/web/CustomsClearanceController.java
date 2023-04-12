package com.haiersoft.ccli.wms.web;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceToExcel;
import com.haiersoft.ccli.wms.service.CustomsClearanceInfoService;
import com.haiersoft.ccli.wms.service.CustomsClearanceService;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.ExcelStyleUtil;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisInspectionFee;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * 
 * @author 
 */
@Controller
@RequestMapping("wms/customs/clearance")
public class CustomsClearanceController extends BaseController{
	
	@Autowired
	private CustomsClearanceService customsClearanceService;
	@Autowired
	private CustomsClearanceInfoService customsClearanceInfoService;



	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/customs/customsClearanceManager";
	}
	

	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "wms/customs/customsClearance";
	}
  
	
	@RequestMapping(value="getNewNumber", method = RequestMethod.POST)
	@ResponseBody
	public String getNewNumber() {
		String number = customsClearanceService.getNewNumber();//报关单号
		return number;
	}
	
	
	@RequestMapping(value="getNumber", method = RequestMethod.POST)
	@ResponseBody
	public String getNumber() {
		String number = customsClearanceService.getLinkIdToString();//报关单号
		return number;
	}
	
	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisCustomsClearance> page = getPage(request);
//		page.orderBy("id").order(Page.DESC); 
		BisCustomsClearance customsClearance = new BisCustomsClearance();
        parameterReflect.reflectParameter(customsClearance, request);
//		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page = customsClearanceService.seachCustomsClearanceSql(page, customsClearance);
		return getEasyUIData(page);
	}

	
	/**
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("bisCustoms", new BisCustomsDeclaration());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "wms/customs/customsClearance";
	}
	
	
	/**
	 * 新增保存报关单
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response) {
		BisCustomsClearance bisCustoms = new BisCustomsClearance();
		parameterReflect.reflectParameter(bisCustoms, request);//转换对应实体类参数
		User user = UserUtil.getCurrentUser();
		bisCustoms.setOperator(user.getName());
		bisCustoms.setOperateTime(new Date());
		bisCustoms.setAuditingState("0");
		try {
			customsClearanceService.save(bisCustoms);
		}
		catch(DataIntegrityViolationException ex){
			return "duplicate";
		}
		
		return "success";
	}
	
	//修改跳转
	@RequestMapping(value="update/{cdNum}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("cdNum") String cdNum) {
		BisCustomsClearance bisCustoms = customsClearanceService.get(cdNum);
		model.addAttribute("bisCustoms", bisCustoms);
		model.addAttribute("action", "update");
		return "wms/customs/customsClearance";
	}
	
	/**
	 * 修改保存报关单
	 * @throws ParseException 
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);
		BisCustomsClearance bisCustoms = new BisCustomsClearance();
		parameterReflect.reflectParameter(bisCustoms, request);//转换对应实体类参数
		//已审核的不能修改
		if(bisCustoms.getAuditingState().equals("3"))
			return "passed";
		bisCustoms.setUpdateTime(new Date());	
		bisCustoms.setOperateTime(date);
		customsClearanceService.update(bisCustoms);
		return "success";
	}
	//提交
	@RequestMapping(value = "submitIt/{cdNum}")
	@ResponseBody
	public String submitIt(@PathVariable("cdNum") String cdNum) {
		
		BisCustomsClearance obj = customsClearanceService.get(cdNum);		
		obj.setAuditingState("1");
		obj.setUpdateTime(new Date());		
		customsClearanceService.update(obj);
		return "success";
	}
	
	//驳回
	@RequestMapping(value = "reject/{cdNum}")
	@ResponseBody
	public String reject(@PathVariable("cdNum") String cdNum) {		
		BisCustomsClearance obj = customsClearanceService.get(cdNum);		
		obj.setAuditingState("2");
		obj.setUpdateTime(new Date());		
		customsClearanceService.update(obj);
		return "success";
	}	
	
	//审核通过
	@RequestMapping(value = "okpass/{cdNum}")
	@ResponseBody
	public String okPass(@PathVariable("cdNum") String cdNum) {
		User user = UserUtil.getCurrentUser();
		BisCustomsClearance obj = customsClearanceService.get(cdNum);
		//如果审核人为保存人时，审核失败
		if(obj.getOperator().equals(user.getName()))
			return "error";
		obj.setAuditingState("3");
		obj.setUpdateTime(new Date());
		obj.setExaminePerson(user.getName());
		obj.setExamineTime(new Date());
		customsClearanceService.update(obj);
		return "success";
	}
	
	//取消审核
	@RequestMapping(value = "nopass/{cdNum}")
	@ResponseBody
	public String noPass(@PathVariable("cdNum") String cdNum) {
		User user = UserUtil.getCurrentUser();
		BisCustomsClearance obj = customsClearanceService.get(cdNum);
		//如果审核人为保存人时，审核失败
		if(obj.getOperator().equals(user.getName()))
			return "error";
		obj.setAuditingState("0");
		obj.setUpdateTime(new Date());
		obj.setExaminePerson(user.getName());
		obj.setExamineTime(new Date());
		customsClearanceService.update(obj);
		return "success";
	}
	
	//判断是否保存
	@RequestMapping(value = "ifsave/{id}")
	@ResponseBody
	public String ifsave(@PathVariable("id") String id) {
		BisCustomsClearance bisCustoms = customsClearanceService.get(id);
			if(bisCustoms == null || bisCustoms.equals("")){
				return "success";
			}else{
				return "false";
			}
	}
	


	
	//删除
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		if(id==null&&id.equals("")) {
			return "error";
		}
		BisCustomsClearance bisCustoms = customsClearanceService.get(id);
		if(bisCustoms.getAuditingState().equals("3")) {
			return "此记录已审核，不能删除";
		}
		customsClearanceService.delete(id);
		BisCustomsClearanceInfo  bisCustomsClearanceInfo=new BisCustomsClearanceInfo();
		bisCustomsClearanceInfo.setCusId(id);
		customsClearanceInfoService.delete(bisCustomsClearanceInfo);
		return "success";
		
	}
	
	
	@RequestMapping(value="getSum", method = RequestMethod.POST)
	@ResponseBody
	public String getSum(String dNumber) {
		
		String result = customsClearanceService.countNum(dNumber);
		if(null == result) {
			return "error";
		}
		
		else return result;

	}
	
	@RequestMapping(value="getMaxSum", method = RequestMethod.POST)
	@ResponseBody
	public String getMaxSum(String dNumber) {
		
		String result = customsClearanceService.countMaxNum(dNumber);
		if(null == result) {
			return "error";
		}
		
		else return result;

	}
	
	/**
	 * Ajax请求校验提单号是否唯一。
	 */
//	@RequestMapping(value = "checkbillnum/{id}")
//	@ResponseBody
//	public String checkBillNum(@PathVariable("id") String id,String billNum) {
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("billNum", billNum);
//		params.put("inOutSign", "1");
//		List<BisCustomsClearance> customs = customsClearanceService.findList(params);
//		if (customs.isEmpty()) {
//			return "true";
//		} else {
//			if(customs.get(0).getCdNum().equals(id)){
//				return "true";
//			}else{
//				return "false";
//			}
//		}
//	}
	
	
	/**
	 *
	 * @Description: 导出excel
	 * @date 2016年3月15日 下午5:22:55
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Page<BisCustomsClearance> page = getPage(request);
		BisCustomsClearance customsClearance = new BisCustomsClearance();
		parameterReflect.reflectParameter(customsClearance, request);// 转换对应实体类参数

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		// PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		// filters.add(filter);

		List<Map<String, String>> outStockList1 =customsClearanceService.seachCustomsClearanceSql(customsClearance);
	//	List<BisCustomsClearance> outStockList =customsClearanceService.seachCustomsClearanceSql1( customsClearance);
		List<BisCustomsClearanceToExcel> outStockList=new ArrayList<>();
		for(Map map :outStockList1){
			BisCustomsClearanceToExcel user = JSON.parseObject(JSON.toJSONString(map), BisCustomsClearanceToExcel.class);
			//不处理  null 会默认为 -1
			if(null==user.getNUM()){
				user.setNUM(new BigDecimal(0));
			}
			if(null==user.getNET_WEIGHT()){
				user.setNET_WEIGHT(new BigDecimal(0));

			}
			if(null==user.getGROSS_WEIGHT()){
				user.setGROSS_WEIGHT(new BigDecimal(0));

			}
			if(null==user.getMONEY()){
				user.setMONEY(new BigDecimal(0));

			}

			outStockList.add(user);
		}


	/*	for(   BisCustomsClearance a: outStockList){

			a.getClientName();
			System.out.println(a.getClientName());
		}*/
		/*Collections.sort(outStockList, new Comparator<BisCustomsClearance>(){
			@Override
			public int compare(BisCustomsClearance b1, BisCustomsClearance b2) {
				return b2.getOperateTime().compareTo(b1.getOperateTime());
			}

		});*/
		ExportParams params = new ExportParams("通关台账", "通关台账Sheet", ExcelType.XSSF);
       params.setStyle(ExcelStyleUtil.class);
		Workbook workbook = ExcelExportUtil.exportExcel(params,BisCustomsClearanceToExcel.class, outStockList);

		// String formatFileName = URLEncoder.encode("出库联系单" +".xlsx","UTF-8");
		String formatFileNameP = "通关台账" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	/**
	 *
	 * @Description: 导出excel
	 * @date 2016年3月15日 下午5:22:55
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "export/{cdNum}")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response, @PathVariable("cdNum") String cdNum) throws Exception {
		Page<BisCustomsClearance> page = getPage(request);
		BisCustomsClearance customsClearance = new BisCustomsClearance();
		//parameterReflect.reflectParameter(customsClearance, request);// 转换对应实体类参数

		//List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		// PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		// filters.add(filter);
		customsClearance.setCdNum(cdNum);

		List<Map<String, String>> outStockList1 =customsClearanceService.seachCustomsClearanceSql(customsClearance);
		//	List<BisCustomsClearance> outStockList =customsClearanceService.seachCustomsClearanceSql1( customsClearance);
		List<BisCustomsClearanceToExcel> outStockList=new ArrayList<>();
		for(Map map :outStockList1){
			BisCustomsClearanceToExcel user = JSON.parseObject(JSON.toJSONString(map), BisCustomsClearanceToExcel.class);
			//不处理  null 会默认为 -1
			if(null==user.getNUM()){
				user.setNUM(new BigDecimal(0));
			}
			if(null==user.getNET_WEIGHT()){
				user.setNET_WEIGHT(new BigDecimal(0));

			}
			if(null==user.getGROSS_WEIGHT()){
				user.setGROSS_WEIGHT(new BigDecimal(0));

			}
			if(null==user.getMONEY()){
				user.setMONEY(new BigDecimal(0));

			}

			outStockList.add(user);
		}


	/*	for(   BisCustomsClearance a: outStockList){

			a.getClientName();
			System.out.println(a.getClientName());
		}*/
		/*Collections.sort(outStockList, new Comparator<BisCustomsClearance>(){
			@Override
			public int compare(BisCustomsClearance b1, BisCustomsClearance b2) {
				return b2.getOperateTime().compareTo(b1.getOperateTime());
			}

		});*/
		ExportParams params = new ExportParams("通关台账", "通关台账Sheet", ExcelType.XSSF);
		params.setStyle(ExcelStyleUtil.class);
		Workbook workbook = ExcelExportUtil.exportExcel(params,BisCustomsClearanceToExcel.class, outStockList);

		// String formatFileName = URLEncoder.encode("出库联系单" +".xlsx","UTF-8");
		String formatFileNameP = "通关台账" + ".xlsx";
		String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	
}
