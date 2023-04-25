package com.haiersoft.ccli.wms.web;
import java.io.OutputStream;
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
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;
import com.haiersoft.ccli.wms.entity.BisCiqDeclarationInfo;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclaration;
import com.haiersoft.ccli.wms.entity.BisCustomsDeclarationInfo;
import com.haiersoft.ccli.wms.entity.BisForecast;
import com.haiersoft.ccli.wms.entity.BisForecastInfo;
import com.haiersoft.ccli.wms.service.CiqDeclarationInfoService;
import com.haiersoft.ccli.wms.service.CiqDeclarationService;
import com.haiersoft.ccli.wms.service.CustomsDeclarationInfoService;
import com.haiersoft.ccli.wms.service.CustomsDeclarationService;
import com.haiersoft.ccli.wms.service.ForecastInfoService;
import com.haiersoft.ccli.wms.service.ForecastService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: ForecastController
 * @Description: 入库预报单总览Control1ler
 * @date 2016年3月1日 下午5:05:33
 */
@Controller
@RequestMapping("wms/forecast")
public class ForecastController extends BaseController{
	
	@Autowired
	private ForecastService forecastService;
	@Autowired
	private ForecastInfoService forecastInfoService;
	//@Autowired
	//private ClientService clientService;
	//@Autowired
	//private ExpenseSchemeService expenseSchemeService;
	//@Autowired
	//private ExpenseSchemeInfoService expenseSchemeInfoService;
	@Autowired
	private CustomsDeclarationService customsDeclarationService;
	@Autowired
	private CustomsDeclarationInfoService customsDeclarationInfoService;
	@Autowired
	private CiqDeclarationService ciqDeclarationService;
	@Autowired
	private CiqDeclarationInfoService ciqDeclarationInfoService;
	
	/**
	 * 入库预报单总览默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/forecast/forecast";
	}
	
	/**
	 * 入库预报单管理默认页面
	 */
	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("bisForecast", new BisForecast());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "wms/forecast/forecastManager";
	}
    /*
     * 获取新的预报单号
     */
	@RequestMapping(value="getNewForId", method = RequestMethod.POST)
	@ResponseBody
	public String getNewForId() {
		String forId = forecastService.getLinkIdToString();//预报单号
		return forId;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库预报单
	 * @date 2016年4月14日 下午4:02:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisForecast> page = getPage(request);
//		page.setOrder(Page.DESC);
		page.orderBy("forId").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = forecastService.search(page, filters);
		return getEasyUIData(page);
	}

	
	/**
	 * 添加入库预报单跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("bisForecast", new BisForecast());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "wms/forecast/forecastManager";
	}
	
	
	/**
	 * 添加出入库预报单
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisForecast bisForecast,Model model, HttpServletRequest request) {
		bisForecast.setCiqSign(0);
		bisForecast.setCdSign(0);
 		bisForecast.setUpdateTime(new Date());
		forecastService.save(bisForecast);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库预报单修改展示
	 * @date 2016年4月20日 上午10:35:02 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="update/{forId}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("forId") String forId) {
		BisForecast bisForecast = forecastService.get(forId);
		model.addAttribute("bisForecast", bisForecast);
		model.addAttribute("action", "update");
		return "wms/forecast/forecastManager";
	}

	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断入库预报单是否已保存
	 * @date 2016年4月20日 下午9:16:02 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ifsave/{forId}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("forId") String forId) {
		BisForecast bisForecast = forecastService.get(forId);
		if(bisForecast != null && !"".equals(bisForecast)){
			return "success";
		}else{
			return "false";
		}
		
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年4月14日 上午16:36
	 * @param forId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{forId}")
	@ResponseBody
	public String deleteEnterStock(@PathVariable("forId") String forId) {
		forecastService.delete(forId);
		return "success";
 	}

	/**
	 * 入库预报单打印跳转
	 * 
	 * @param linkId
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "print/{forId}", method = RequestMethod.GET)
	public String print(@PathVariable("forId") String forId, Model model) {
		BisForecast forecast = forecastService.get(forId);
		List<BisForecastInfo> forecastInfoList = forecastInfoService.getList(forId);
		model.addAttribute("bisForecast", forecast);
		if(!forecastInfoList.isEmpty()){
				model.addAttribute("forecastInfo", forecastInfoList);
		}
//		else{
//			BisEnterStockInfo enterStockInfoN = new BisEnterStockInfo();
//			model.addAttribute("bisEnterStockInfo", enterStockInfoN);
//		}
		model.addAttribute("action", "print");
		return "wms/forecast/forecastPrint";
	}
	
	
	/**
	 * 
	 * @Description: 导出excel
	 * @date 2016年4月15日 下午2:29:55 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
		BisForecast forecast = new BisForecast();
		parameterReflect.reflectParameter(forecast, request);//转换对应实体类参数
		
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		List<BisForecast> forecastList = forecastService.search(filters);
		
        ExportParams params = new ExportParams("入库预报单", "入库预报单Sheet", ExcelType.XSSF);
        
        Workbook workbook = ExcelExportUtil.exportExcel(params, BisForecast.class, forecastList);
        
//        String formatFileName = URLEncoder.encode("入库预报单" +".xlsx","UTF-8");
        String formatFileNameP ="入库预报单" +".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	/**
	 * 
	 * @author pyl
	 * @Description: 入库预报单生成报关数据
	 * @date 2016年5月24日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="customs/{forId}", method = RequestMethod.GET)
	@ResponseBody
	public String customs(@PathVariable("forId") String forId) {
		User user = UserUtil.getCurrentUser();
		BisForecast forecast = forecastService.get(forId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billNum", forecast.getBillNum());
		params.put("inOutSign", "1");
		List<BisCustomsDeclaration> customsList = customsDeclarationService.findList(params);
		if(customsList.isEmpty()){
			BisCustomsDeclaration cusObj = new BisCustomsDeclaration();
			cusObj.setId(customsDeclarationService.getNewNumber());
			cusObj.setCdNum(forecast.getCdNum());
			cusObj.setImportPort("青开发区");
			cusObj.setInOutSign("1");
			cusObj.setManagementUnit("怡之航");
			cusObj.setConsignee("怡之航");
			cusObj.setBillNum(forecast.getBillNum());
			cusObj.setContainerNum(forecast.getCtnCont());
			cusObj.setContainerLoadType("02");
			cusObj.setTradeType(forecast.getTradeMode());
			cusObj.setForId(forecast.getForId());
			cusObj.setClientId(forecast.getClientId());
			cusObj.setClientName(forecast.getClientName());
			cusObj.setRecordMan(user.getName());
			cusObj.setRecordTime(new Date());
			customsDeclarationService.save(cusObj);
			List<BisForecastInfo> infoList = forecastInfoService.getList(forId);
			if(!infoList.isEmpty()){
				for(BisForecastInfo info : infoList){
					BisCustomsDeclarationInfo cdInfo = new BisCustomsDeclarationInfo();
					cdInfo.setCusId(cusObj.getId());
					cdInfo.setCdNum(info.getHscode());
					cdInfo.setItemNum(info.getItemNum());
					cdInfo.setCargoName(info.getCargoName());
					cdInfo.setSpec(info.getSpace());
					cdInfo.setScalar(info.getPiece());
					cdInfo.setNetWeight(info.getNetWeight());
					cdInfo.setUnits("KG");
					cdInfo.setFreeLavy(0);
					cdInfo.setCurrencyType("1");
					cdInfo.setDuty(0.06);
					cdInfo.setRecordMan(user.getName());
					cdInfo.setRecordTime(new Date());
					customsDeclarationInfoService.merge(cdInfo);
				}
			}
			return "success";
		}else{
			return "false";
		}
		
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库预报单生成报检数据
	 * @date 2016年5月24日 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ciq/{forId}", method = RequestMethod.GET)
	@ResponseBody
	public String ciq(@PathVariable("forId") String forId) {
		User user = UserUtil.getCurrentUser();
		BisForecast forecast = forecastService.get(forId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billNum", forecast.getBillNum());
		params.put("inOutSign", "1");
		List<BisCiqDeclaration> ciqList = ciqDeclarationService.findList(params);
		if(ciqList.isEmpty()){
			BisCiqDeclaration ciqObj = new BisCiqDeclaration();
			ciqObj.setId(ciqDeclarationService.getNewNumber());
			ciqObj.setCiqCode(forecast.getCiqNum());
			ciqObj.setInOutSign("1");
			ciqObj.setBillNum(forecast.getBillNum());
			ciqObj.setForId(forecast.getForId());
			ciqObj.setClientId(forecast.getClientId());
			ciqObj.setClientName(forecast.getClientName());
			ciqObj.setTradeType(forecast.getTradeMode());
			ciqObj.setRecordMan(user.getName());
			ciqObj.setRecordTime(new Date());
			ciqDeclarationService.save(ciqObj);
			List<BisForecastInfo> infoList = forecastInfoService.getList(forId);
			if(!infoList.isEmpty()){
				for(BisForecastInfo info : infoList){
					BisCiqDeclarationInfo ciqInfo = new BisCiqDeclarationInfo();
					ciqInfo.setCiqId(ciqObj.getId());
					ciqInfo.setCiqNum(info.getHscode());
					ciqInfo.setCargoName(info.getCargoName());
					ciqInfo.setScalar(info.getPiece());
					ciqInfo.setNetWeight(info.getNetWeight());
					ciqInfo.setRecordMan(user.getName());
					ciqInfo.setRecordTime(new Date());
					ciqInfo.setRemark1(info.getRemark());
					ciqDeclarationInfoService.merge(ciqInfo);
				}
			}
			return "success";
		}else{
			return "false";
		}
		
	}
}
