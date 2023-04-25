package com.haiersoft.ccli.cost.web;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.NumberToCN;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.cost.service.InspectionFeeInfoService;
import com.haiersoft.ccli.cost.entity.BisInspectionFee;
import com.haiersoft.ccli.cost.entity.BisInspectionFeeInfo;
import com.haiersoft.ccli.cost.service.InspectionFeeService;
/**
 * InspectionFeeContorller
 * @author PYL
 * @date 2016年3月23日
 */
@Controller
@RequestMapping("cost/inspecion")
public class InspectionFeeContorller extends BaseController {
	
	@Autowired
	private InspectionFeeInfoService inspectionFeeInfoService;
	@Autowired
	private InspectionFeeService inspectionFeeService;
	@Autowired
	private EnterStockService enterStockService;
	
	/*跳转查验费用列表*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "cost/inspectionFee/inspectionFee";
	}
	
	/*跳转查验费用报表*/
	@RequestMapping(value="reportList",method = RequestMethod.GET)
	public String reportList(){
		return "cost/inspectionFee/inspectionReport";
	}
	
	/**
	 * 导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportList")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BisInspectionFee inspectionFee = new BisInspectionFee();
        parameterReflect.reflectParameter(inspectionFee, request);// 转换对应实体类参数
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        List<BisInspectionFee> inspectionFeeList = inspectionFeeService.search(filters);
        ExportParams params = new ExportParams("查验总览", "查验总览", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, BisInspectionFee.class, inspectionFeeList);
        String formatFileNameP = "查验总览" + ".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
        os.close(); // 关闭流
    }
	
	/**
	 * 
	 * @author pyl
	 * @Description: 查验费用列表查询
	 * @date 2016年6月24日 下午2:20:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisInspectionFee> page = getPage(request);
		page.setOrderBy("checkDate");
        page.setOrder(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = inspectionFeeService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 查验费用列表查询
	 * @date 2016年6月24日 下午2:20:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="reportjson", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getReportData(HttpServletRequest request) {
		BisInspectionFee inspection = new BisInspectionFee();
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		parameterReflect.reflectParameter(inspection, request);//转换对应实体类参数
		String clientId = inspection.getClientId();
		String ifLx=inspection.getIfLx();
		Date startTime = inspection.getStartTime();
		Date endTime = inspection.getEndTime();
		return inspectionFeeService.findReportList(clientId,startTime,endTime,ifLx,sort,order);
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年6月24日  
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{feeId}")
	@ResponseBody
	public String deleteEnterStock(@PathVariable("feeId") String feeId) {
		inspectionFeeService.delete(feeId);
		return "success";
	}
	
	/**
	 * @author pyl 
	 * @Description: 查验需求管理页面跳转
	 * @date 2016年3月2日 下午2:42:15 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="add", method = RequestMethod.GET)
	public String createContractForm(Model model) {
		String feeId = inspectionFeeService.getNewFeeId();//查验单号
		BisInspectionFee bisInspectionFee=new BisInspectionFee();
		bisInspectionFee.setIfLx("0");
		bisInspectionFee.setIfPass(0);
		bisInspectionFee.setFeeId(feeId);
		bisInspectionFee.setBalanceWay("1");
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user",user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("inspection", bisInspectionFee);
		model.addAttribute("action", "create");
		return "cost/inspectionFee/inspectionFeeManager";
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 查验费用修改展示
	 * @date 2016年3月5日 上午10:35:02 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="update/{feeId}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("feeId") String feeId) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		BisInspectionFee bisInspectionFee = inspectionFeeService.get(feeId);
		model.addAttribute("inspection", bisInspectionFee);
		model.addAttribute("action", "update");
		return "cost/inspectionFee/inspectionFeeManager";
	}
	//复制功能
	@RequestMapping(value="copyIt/{feeId}", method = RequestMethod.GET)
	public String copyIt(Model model, @PathVariable("feeId") String feeId) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		BisInspectionFee bisInspectionFee = inspectionFeeService.get(feeId);
		BisInspectionFee newObj = new BisInspectionFee();
	    BeanUtils.copyProperties(bisInspectionFee, newObj);
	    newObj.setFeeId(inspectionFeeService.getNewFeeId());
	    newObj.setCheckDate(new Date());
	    newObj.setIfPass(0);
	    newObj.setIfLx("1");
	    newObj.setCtnNum(null);
	    newObj.setCtnType("40");
	    newObj.setBalanceWay("2");
	    newObj.setOperatePerson(user.getName());
	    newObj.setOperateTime(new Date());
		model.addAttribute("inspection", newObj);
		model.addAttribute("action", "update");
		return "cost/inspectionFee/inspectionFeeManager";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断提单号是否正确
	 * @date 2016年6月25日
	 * @return
	 * @throws
	 */
	@RequestMapping(value="checkbill/{billNum}/{clientId}/{ifLx}", method = RequestMethod.POST)
	@ResponseBody
	public String checkBillNum(@PathVariable("billNum") String billNum,@PathVariable("clientId") String clientId,@PathVariable("ifLx") String ifLx) {
		//如果是零星客户提单跟客户名称可以随便录入
		if("1".equals(ifLx)){
			return "true";
		}
		BisEnterStock stock = enterStockService.find("itemNum", billNum);
		if(null!=stock && !"".equals(stock)){
			if(!stock.getStockOrgId().equals(clientId)){
				return "false";
			}else{
				return "true";
			}
		}else{
			return "none";
		}
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 保存
	 * @date 2016年6月25日  
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String createContract(HttpServletRequest request, HttpServletResponse response) {
		BisInspectionFee inspectionFee = new BisInspectionFee();
		BisInspectionFee inspectionFeeOld = new BisInspectionFee();
		parameterReflect.reflectParameter(inspectionFee, request);//转换对应实体类参数
		if(!"2".equals(inspectionFee.getBalanceWay())){
			inspectionFee.setBalanceWay("1");
		}
		inspectionFeeOld=inspectionFeeService.get(inspectionFee.getFeeId());
		if(null!=inspectionFeeOld){
			inspectionFee.setCostAmount(inspectionFeeOld.getCostAmount());
		}
		inspectionFeeService.merge(inspectionFee);
		inspectionFeeInfoService.updateBillNum(inspectionFee.getFeeId(),inspectionFee.getBillNum());
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断查验费用是否已保存且无明细
	 * @date 2016年6月25日
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ifsave/{feeId}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("feeId") String feeId) {
		BisInspectionFee inspection = inspectionFeeService.get(feeId);
		if(null != inspection && !"".equals(inspection)){
			List<BisInspectionFeeInfo> infoList=inspectionFeeInfoService.findByFeeId(feeId);
			if(!infoList.isEmpty()){
				return "hasInfo";
			}else{
				return "success";
			}
		}else{
			return "false";
		}
	}

	/**
	 * 查验费用导出报表
	 * @param id
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	@ResponseBody
	public void exportInfo(@Valid @ModelAttribute @RequestBody BisInspectionFee obj,HttpServletRequest request, HttpServletResponse response) throws IOException {
		    Map<String, Object> map = new HashMap<String, Object>();
		    String sort=request.getParameter("sort");
			String order=request.getParameter("order");
	        List<Map<String, Object>> listMap=inspectionFeeService.findReportList(obj.getClientId(),obj.getStartTime(),obj.getEndTime(),obj.getIfLx(),sort,order);
			TemplateExportParams params = new TemplateExportParams("exceltemplate/inspectionReport.xls");
			String excelName = "查验费用报表.xls";
			User user = UserUtil.getCurrentUser();
			map.put("user", user.getName());
			map.put("now", new Date());
			if(null != listMap && listMap.size() > 0){
				map.put("maplist", listMap);
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
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 审核通过
	 * @param feeId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "okpass/{feeId}")
	@ResponseBody
	public String okPass(@PathVariable("feeId") String feeId) {
		BisInspectionFee obj = inspectionFeeService.get(feeId);
		obj.setIfPass(1);
		inspectionFeeService.update(obj);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 取消审核
	 * @param feeId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "nopass/{feeId}")
	@ResponseBody
	public String noPass(@PathVariable("feeId") String feeId) {
		BisInspectionFee obj = inspectionFeeService.get(feeId);
		obj.setIfPass(0);
		inspectionFeeService.update(obj);
		return "success";
	}
	
	/**
	 * 作业委托单打印跳转 jxk 20190128 添加
	 * @author jxk
	 * @date 2019-1-28
	 * @param payId
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "print/{feeId}", method = RequestMethod.GET)
	public String print(@PathVariable("feeId") String feeId, Model model) {
    	SimpleDateFormat format=new SimpleDateFormat ("yyyy.MM.dd");
    	BisInspectionFee inspectionFee=inspectionFeeService.get(feeId);
		List<BisInspectionFeeInfo> infoList = inspectionFeeInfoService.findByFeeId(inspectionFee.getFeeId());
		//1:水产 2：冻肉  3:水果 4:其他
		switch (inspectionFee.getCheckType()) {
			case "1":
				model.addAttribute("checkType","查验服务费（水产）");
				break;
			case "2":
				model.addAttribute("checkType","查验服务费（冻肉）");
				break;
			case "3":
				model.addAttribute("checkType","查验服务费（水果）");
				break;
			case "4":
				model.addAttribute("checkType","查验服务费（其他）");
				break;
		}
		model.addAttribute("inspectionFee", inspectionFee);
		model.addAttribute("infoList",infoList);
		model.addAttribute("size",null!=infoList?infoList.size():0);
		model.addAttribute("No",StringUtils.noToString());
		model.addAttribute("dateTime",format.format(inspectionFee.getOperateTime()));
		for (int i = 0;infoList!=null&&i <infoList.size(); i++) {
			BisInspectionFeeInfo feeInfo=infoList.get(i);
			if(null!=feeInfo.getIfField()&&1==feeInfo.getIfField()){
				//场地
				model.addAttribute("ifField",1);
			}
			if(null!=feeInfo.getIfHanding()&&1==feeInfo.getIfHanding()){
				//搬倒
				model.addAttribute("ifHanding",1);
			}
			if(null!=feeInfo.getIfHang()&&1==feeInfo.getIfHang()){
				//吊箱
				model.addAttribute("ifHang",1);
			}
			if(null!=feeInfo.getIfPlug()&&1==feeInfo.getIfPlug()){
				//插电
				model.addAttribute("ifPlug",1);
			}
		}
		Double money = inspectionFee.getCostAmount();
		BigDecimal numberOfMoney = new BigDecimal(money);
		BigDecimal setScale= numberOfMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
	    model.addAttribute("money",setScale);
		String strCnNumber = NumberToCN.number2CNMontrayUnit(setScale);
		model.addAttribute("CnNumber",strCnNumber);
		return "cost/inspectionFee/inspectionFeePrint";
	}
}
