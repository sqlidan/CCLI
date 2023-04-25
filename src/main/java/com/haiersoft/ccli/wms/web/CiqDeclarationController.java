package com.haiersoft.ccli.wms.web;
import java.io.OutputStream;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;
import com.haiersoft.ccli.wms.entity.BisCiqDeclarationInfo;
import com.haiersoft.ccli.wms.service.CiqDeclarationInfoService;
import com.haiersoft.ccli.wms.service.CiqDeclarationService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;

/**
 * 
 * @author pyl
 * @ClassName: CiqDeclarationController
 * @Description: 入库报检单总览Control1ler
 * @date 2016年4月16日 下午3:42:33
 */
@Controller
@RequestMapping("wms/ciq")
public class CiqDeclarationController extends BaseController{
	
	@Autowired
	private CiqDeclarationService ciqDeclarationService;
	@Autowired
	private CiqDeclarationInfoService ciqDeclarationInfoService;
	//@Autowired
	//private ClientService clientService;
	
	/**
	 * 入库报检单总览默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/ciq/ciq";
	}
	
	/**
	 * 入库报检单管理默认页面
	 */
	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "wms/ciq/ciqManager";
	}
    /*
     * 获取新的报关单号
     */
	@RequestMapping(value="getNewNumber", method = RequestMethod.POST)
	@ResponseBody
	public String getNewNumber() {
		String number = ciqDeclarationService.getNewNumber();//报关单号
		return number;
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库报检单
	 * @date 2016年4月14日 下午4:02:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisCiqDeclaration> page = getPage(request);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_inOutSign", "1");
		filters.add(filter);
		page = ciqDeclarationService.search(page, filters);
		return getEasyUIData(page);
	}

	
	/**
	 * 添加入库报检单跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("bisCiq", new BisCiqDeclaration());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "wms/ciq/ciqManager";
	}
	
	
	/**
	 * 添加出入库预报单
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response) {
		BisCiqDeclaration bisCiq = new BisCiqDeclaration();
		parameterReflect.reflectParameter(bisCiq, request);//转换对应实体类参数
		bisCiq.setUpdateTime(new Date());
		ciqDeclarationService.save(bisCiq);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断是否保存
	 * @date 2016年4月18日 14:02:50 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "ifsave/{id}")
	@ResponseBody
	public String ifsave(@PathVariable("id") String id) {
		BisCiqDeclaration bisCiq = ciqDeclarationService.get(id);
			if(bisCiq == null || bisCiq.equals("")){
				return "success";
			}else{
				return "false";
			}
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库报检单修改跳转
	 * @date 2016年4月18日 14:06:02 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") String id) {
		BisCiqDeclaration bisCiq = ciqDeclarationService.get(id);
		model.addAttribute("bisCiq", bisCiq);
		model.addAttribute("action", "update");
		return "wms/ciq/ciqManager";
	}

	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年4月18日 上午11:29:50 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		ciqDeclarationService.delete(id);
			return "success";
	}
	
	/**
	 * 入库报检单打印跳转
	 * 
	 * @param model
	 * @return
	 */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "print/{ids}")
	public String print(Model model,@PathVariable("ids") List ids) {
		List<BisCiqDeclaration> bisCiqList = new ArrayList<BisCiqDeclaration>();
		String idit="";
		for(int i=0;i<ids.size();i++){
		    idit=ids.get(i).toString();
		    bisCiqList.add(ciqDeclarationService.get(idit));
		}
		if(!bisCiqList.isEmpty()){
				model.addAttribute("bisCiq", bisCiqList);
		}
		else{
			List<BisCiqDeclaration> bisCiqNull = new ArrayList<BisCiqDeclaration>();
			model.addAttribute("bisCiq", bisCiqNull);
		}
		model.addAttribute("action", "print");
		return "wms/ciq/ciqPrint";
	}
	
	/**
	 * 入库报检单(货物明细)打印跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "printInfo/{id}", method = RequestMethod.GET)
	public String print(@PathVariable("id") String id, Model model) {
    	BisCiqDeclaration bisCiq = ciqDeclarationService.get(id);
		List<BisCiqDeclarationInfo> ciqInfoList = ciqDeclarationInfoService.getByCiqId(id);
		model.addAttribute("bisCiq", bisCiq);
		if(!ciqInfoList.isEmpty()){
				model.addAttribute("ciqInfo", ciqInfoList);
		}
		else{
			List<BisCiqDeclarationInfo> ciqInfoN = new ArrayList<BisCiqDeclarationInfo>();
			model.addAttribute("ciqInfo", ciqInfoN);
		}
		model.addAttribute("action", "print");
		return "wms/ciq/ciqInfoPrint";
	}
	
	
	/**
	 * 
	 * @Description: 导出excel(不带货物）
	 * @date 2016年4月18日 14:09:55 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
		BisCiqDeclaration bisCiq = new BisCiqDeclaration();
		parameterReflect.reflectParameter(bisCiq, request);//转换对应实体类参数
		
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_inOutSign", "1");
		filters.add(filter);
		List<BisCiqDeclaration> bisCiqList = ciqDeclarationService.search(filters);
		
        ExportParams params = new ExportParams("入库报检单", "入库报检单sheet", ExcelType.XSSF);
        
        Workbook workbook = ExcelExportUtil.exportExcel(params, BisCiqDeclaration.class, bisCiqList);
        
        String formatFileNameP ="入库报检单" +".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}

	/**
	 * 
	 * @Description: 导出excel(带货物）
	 * @date 2016年4月18日 15:54:55 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping(value = "exportwith/{id}")
	@ResponseBody
	public void exportWith(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id) throws Exception{
		List<BisCiqDeclaration> bisCiqList = new ArrayList<BisCiqDeclaration>();
		bisCiqList.add(ciqDeclarationService.get(id));
		List<BisCiqDeclarationInfo> bisCiqInfoList =  new ArrayList<BisCiqDeclarationInfo>();
        bisCiqInfoList = ciqDeclarationInfoService.getByCiqId(id);
        ExportParams params = new ExportParams("入库报检单", "入库报检单sheet", ExcelType.XSSF);
        ExportParams params2 = new ExportParams("入库报检单货物信息", "入库报检单货物信息sheet", ExcelType.XSSF);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> p1 = new HashMap<String,Object>();
        p1.put("title", params);
        p1.put("entity", BisCiqDeclaration.class);
        p1.put("data", bisCiqList);
        list.add(p1);
        Map<String,Object> p2 = new HashMap<String,Object>();
        p2.put("title", params2);
        p2.put("entity", BisCiqDeclarationInfo.class);
        p2.put("data", bisCiqInfoList);
        list.add(p2);
        Workbook workbook =  ExcelExportUtil.exportExcel(list, "abc");
        String formatFileNameP ="入库报检单（带货物）" +".xlsx";
        String formatFileName = new String(formatFileNameP.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	
	/**
	 * Ajax请求校验提单号是否唯一。
	 */
	@RequestMapping(value = "checkbillnum/{id}")
	@ResponseBody
	public String checkBillNum(@PathVariable("id") String id,String billNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billNum", billNum);
		params.put("inOutSign", "1");
		List<BisCiqDeclaration> customs = ciqDeclarationService.findList(params);
		if (customs.isEmpty()) {
			return "true";
		} else {
			if(customs.get(0).getId().equals(id)){
				return "true";
			}else{
				return "false";
			}
		}
	}
	
}
