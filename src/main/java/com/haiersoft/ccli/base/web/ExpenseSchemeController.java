package com.haiersoft.ccli.base.web;
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
import org.jeecgframework.poi.pdf.PdfExportUtil;
import org.jeecgframework.poi.pdf.entity.PdfExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseClientRelation;
import com.haiersoft.ccli.base.entity.BaseExpenseShare;
import com.haiersoft.ccli.base.entity.ExpenseContract;
import com.haiersoft.ccli.base.entity.ExpenseScheme;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientRelationService;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseContractService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.ExpenseSchemeService;
import com.haiersoft.ccli.base.service.ExpenseShareService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.Permission;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.PermissionService;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Connor.M
 * @ClassName: ExpenseSchemeController
 * @Description: 方案controller
 * @date 2016年2月24日 下午5:21:47
 */
@Controller
@RequestMapping("base/scheme")
public class ExpenseSchemeController extends BaseController{
	
	@Autowired
	private ExpenseSchemeService expenseSchemeService;
	@Autowired
	private ClientRelationService clientRelationService;
	@Autowired
	private ExpenseShareService expenseShareService;
	@Autowired
	private ExpenseContractService expenseContractService;
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "base/scheme/expenseSchemeList";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案查询
	 * @date 2016年2月24日 下午3:42:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<ExpenseScheme> page = getPage(request);
		page.setOrder(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		filters.add(filter);
		String lanhuo = request.getParameter("lanhuo");
		List<ExpenseContract> objList = expenseContractService.findList(lanhuo);
		ArrayList<Object> sList = new ArrayList<Object>();
		int size = objList.size();
		for(int i=0; i<size;i++){
			sList.add(objList.get(i).getContractNum());
		}
		if(!StringUtils.isNull(lanhuo)){
			if(!sList.isEmpty()){
				String[] aa = sList.toArray(new String[sList.size()]);
				PropertyFilter filter2 = new PropertyFilter("INAS_contractId", aa);
				filters.add(filter2);
			}else{
				PropertyFilter filter2 = new PropertyFilter("EQS_contractId", "0000");
				filters.add(filter2);
			}
		}
		page = expenseSchemeService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案查询(包含共享合同)
	 * @date 2016年2月24日 下午3:42:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json2",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getData2(HttpServletRequest request) {
		String clientId=request.getParameter("filter_EQS_customsId");
		String ifGet= request.getParameter("filter_EQS_ifGet");
		String contractId=request.getParameter("filter_LIKES_contractId");
		List<Map<String,Object>> expenseList = expenseSchemeService.getShare(contractId,clientId,ifGet);
		return expenseList;
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案录入展示
	 * @date 2016年2月29日 下午1:50:27 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="createSchemeForm", method = RequestMethod.GET)
	public String createSchemeForm(Model model) {
		String schemeNum = expenseSchemeService.getSchemeIdToString();//方案Id
		User user = UserUtil.getCurrentUser();
		ExpenseScheme scheme = new ExpenseScheme();
		scheme.setSchemeNum(schemeNum);
		scheme.setOperatorPerson(user.getName());
		scheme.setOperateTime(new Date());
		model.addAttribute("scheme", scheme);
		model.addAttribute("controller", "createScheme");
		return "base/scheme/editSchemeForm";
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案修改展示
	 * @date 2016年3月1日 下午4:30:41 
	 * @param model
	 * @param contractNum
	 * @return
	 * @throws
	 */
	@RequestMapping(value="updateSchemeForm/{schemeNum}", method = RequestMethod.GET)
	public String updateSchemeForm(Model model, @PathVariable("schemeNum") String schemeNum) {
		ExpenseScheme scheme = expenseSchemeService.get(schemeNum);
		model.addAttribute("scheme", scheme);
		model.addAttribute("controller", "updateScheme");
		return "base/scheme/editSchemeForm";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 方案查看
	 * @date 2016年3月2日 下午2:16:06 
	 * @param model
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value="checkSchemeForm/{schemeNum}", method = RequestMethod.GET)
	public String checkSchemeForm(Model model, @PathVariable("schemeNum") String schemeNum) {
		ExpenseScheme scheme = expenseSchemeService.get(schemeNum);
		model.addAttribute("scheme", scheme);
		return "base/scheme/checkSchemeForm";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 获取合同的客户名称
	 * @date 2016年5月30日  
	 * @param contractNum
	 */
	@RequestMapping(value = "getContractClient/{val}", method = RequestMethod.GET)
	@ResponseBody
	public String createScheme(@PathVariable("val") String contractNum) {
		ExpenseContract contract = expenseContractService.get(contractNum);
		if(!"".equals(contract) && null != contract){
			return contract.getClientName();
		}else{
			String aa = "";
			return aa;
		}
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 保存  ajax的form
	 * @date 2016年2月29日 下午3:36:31 
	 * @param request
	 * @param response
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createScheme", method = RequestMethod.POST)
	@ResponseBody
	public String createScheme(HttpServletRequest request, HttpServletResponse response) {
		ExpenseScheme scheme = new ExpenseScheme();
		parameterReflect.reflectParameter(scheme, request);//转换对应实体类参数
		this.saveShareIds(scheme);
		scheme.setIsDel("0");//删除标志，正常
		expenseSchemeService.merge(scheme);
		return "success";
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 删除
	 * @date 2016年2月29日 下午7:04:05 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteScheme/{id}")
	@ResponseBody
	public String deleteScheme(@PathVariable("id") String id) {
		User user = UserUtil.getCurrentUser();
		ExpenseScheme scheme = expenseSchemeService.get(id);
		scheme.setIsDel("1");//删除
		scheme.setUpdatePerson(user.getName());
		scheme.setUpdateTime(new Date());
		expenseSchemeService.update(scheme);
		expenseShareService.delSchemeNum(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 审核通过
	 * @date 2016年2月29日 下午7:10:28 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "passOkScheme/{id}")
	@ResponseBody
	public String passOkScheme(@PathVariable("id") String id) {
		User user = UserUtil.getCurrentUser();
		ExpenseScheme scheme = expenseSchemeService.get(id);
		scheme.setProgramState("1");
		scheme.setExaminePerson(user.getName());
		scheme.setExamineTime(new Date());
		expenseSchemeService.update(scheme);
		expenseShareService.okpass(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 取消审核
	 * @date 2016年3月1日 下午5:17:33 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "passNoScheme/{id}")
	@ResponseBody
	public String passNoScheme(@PathVariable("id") String id) {
		User user = UserUtil.getCurrentUser();
		ExpenseScheme scheme = expenseSchemeService.get(id);
		scheme.setProgramState("0");
		scheme.setUpdatePerson(user.getName());
		scheme.setUpdateTime(new Date());
		scheme.setExaminePerson(user.getName());
		scheme.setExamineTime(new Date());
		expenseSchemeService.update(scheme);
		expenseShareService.nopass(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 复制方案
	 * @date 2016年2月29日 下午7:14:10 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "copyScheme/{id}")
	@ResponseBody
	public String copyScheme(@PathVariable("id") String id) {
		expenseSchemeService.copyScheme(id);
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 保存并复制
	 * @date 2016年3月1日 下午5:23:31 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "createCopyScheme", method = RequestMethod.POST)
	@ResponseBody
	public String createCopyScheme(HttpServletRequest request, HttpServletResponse response, Model model) {
		ExpenseScheme scheme = new ExpenseScheme();
		parameterReflect.reflectParameter(scheme, request);//转换对应实体类参数
		ExpenseContract contract = expenseContractService.get(scheme.getContractId());
		scheme.setCustomsId(contract.getClientId());
		scheme.setCustomsName(contract.getClientName());
		scheme.setIsDel("0");//删除标志，正常
		expenseSchemeService.save(scheme);
		//复制
		ExpenseScheme newScheme = expenseSchemeService.copyScheme(scheme.getSchemeNum());
		model.addAttribute("contract", newScheme);
		model.addAttribute("controller", "createContract");
		return newScheme.getSchemeNum();
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 导出excel
	 * @date 2016年3月1日 下午7:18:55 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportSchemeExcel")
	@ResponseBody
	public void exportSchemeExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ExpenseScheme scheme = new ExpenseScheme();
		parameterReflect.reflectParameter(scheme, request);//转换对应实体类参数
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		filters.add(filter);
		List<ExpenseScheme> schemes = expenseSchemeService.search(filters);
        ExportParams params = new ExportParams("费用方案信息", "费用方案Sheet", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ExpenseScheme.class, schemes);
        String excelName = "费用方案信息.xlsx";
        String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 导出PDF
	 * @date 2016年4月22日 下午7:06:32 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportSchemePDF")
	@ResponseBody
	public void exportSchemePDF(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String excelName = "费用方案信息.pdf";
	    String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
		response.setContentType("application/pdf");// 定义输出类型
		OutputStream os = response.getOutputStream();
		ExpenseScheme scheme = new ExpenseScheme();
		parameterReflect.reflectParameter(scheme, request);//转换对应实体类参数
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_isDel", "0");
		filters.add(filter);
		List<ExpenseScheme> schemes = expenseSchemeService.search(filters);
        PdfExportParams params = new PdfExportParams("费用方案信息", null);
        Document document = PdfExportUtil.exportPdf(params, ExpenseScheme.class, schemes, os);
        PdfWriter.getInstance(document, os);
		os.close(); // 关闭流
	}
	
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: excel 明细导出
	 * @date 2016年3月2日 上午10:46:23 
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws
	 */
	@RequestMapping("exportSchemeInfoExcel/{id}")
	@ResponseBody
	public void exportSchemeInfoExcel(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception{
	    List<PropertyFilter> filtersT = new ArrayList<PropertyFilter>();
		filtersT.add(new PropertyFilter("EQS_schemeNum", id));
		List<ExpenseSchemeInfo> schemeInfos = expenseSchemeInfoService.search(filtersT);
        ExportParams params = new ExportParams("费用方案"+ id +"明细", "费用方案明细Sheet", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ExpenseSchemeInfo.class, schemeInfos);
        String excelName = "费用方案" + id +"明细.xlsx";
        String formatFileName = new String(excelName.getBytes("GB2312"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	

	/**
	 * 
	 * @author Connor.M
	 * @Description: 入库  费用调整  简版 费用方案 
	 * @date 2016年4月1日 上午10:23:34 
	 * @param model
	 * @param stockId 客户Id
	 * @return
	 * @throws
	 */
	@RequestMapping(value="inEasySchemeList/{stockId}/{linkId}/{planFeeState}", method = RequestMethod.GET)
	public String inEasySchemeList(Model model, @PathVariable("stockId") String stockId, @PathVariable("linkId") String linkId, @PathVariable("planFeeState") String planFeeState) {
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(stockId));
		model.addAttribute("client", clientInfo);
		model.addAttribute("linkId", linkId);
		model.addAttribute("planFeeState", planFeeState);
		return "cost/standingBook/inEasySchemeList";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 出库  费用调整  简版 费用方案 
	 * @date 2016年4月14日 下午1:56:52 
	 * @param model
	 * @param stockId
	 * @param linkId
	 * @return
	 * @throws
	 */
	@RequestMapping(value="outEasySchemeList/{stockId}/{linkId}/{planFeeState}", method = RequestMethod.GET)
	public String outEasySchemeList(Model model, @PathVariable("stockId") String stockId, @PathVariable("linkId") String linkId, @PathVariable("planFeeState") String planFeeState) {
		BaseClientInfo clientInfo = clientService.get(Integer.valueOf(stockId));
		model.addAttribute("client", clientInfo);
		model.addAttribute("linkId", linkId);
		model.addAttribute("planFeeState", planFeeState);
		return "cost/standingBook/outEasySchemeList";
	}
	
	/**
	 * 根据费用方案ID获取费用方案名称
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getname/{uid}")
	@ResponseBody
	public String getname(@PathVariable("uid") String uid,Model model) {
		ExpenseScheme  getObj=new ExpenseScheme();
		if (null!=uid && !uid.equals("")) {
			getObj=expenseSchemeService.get(uid);
		}
		return getObj.getSchemeName();
	}
	
	/**
	 * 获取可以共享合同的客户
	 * 
	 */
	@RequestMapping(value = "getclient/{customsId}", method = RequestMethod.GET)
	public String getFee(Model model, @PathVariable("customsId") String clientId) {
		List<BaseClientRelation> clientList = new ArrayList<BaseClientRelation>();
		clientList = clientRelationService.getList(clientId);
		int size = clientList.size();
		model.addAttribute("num", size);
		model.addAttribute("clientList", clientList);
		return "base/scheme/getClient";
	}
	
	/**
	 * 费用方案回显共享客户名称
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "hxname", method = RequestMethod.GET)
	@ResponseBody
	public String hxname(HttpServletRequest request, String shareIds) {
		String shareClient = "";
		if (null != shareIds && !"".equals(shareIds)) {
			String[] ids = shareIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				shareClient += clientService.get(Integer.valueOf(ids[i])).getClientName()+",";
			}
			shareClient = shareClient.substring(0, shareClient.length() - 1);
		}
		return shareClient;
	}
	
	/**
	 * 获取共享客户的回显
	 * 
	 * @return list
	 */
	@RequestMapping(value = "getshare/{schemeNum}", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getshare(@PathVariable("schemeNum") String schemeNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("schemeNum", schemeNum);
		params.put("ifParent", "0");
		List<BaseExpenseShare> shareList = expenseShareService.getList(params);
		String clientIds = "";
		String clientNames = "";
		for (BaseExpenseShare share : shareList) {
			clientIds += share.getClientId()+",";
			clientNames += share.getClientName()+",";
		}
		if(!"".equals(clientIds)){
			clientIds = clientIds.substring(0, clientIds.length()-1);
		}
		if(!"".equals(clientNames)){
			clientNames = clientNames.substring(0, clientNames.length()-1);
		}
		List<String> tt = new ArrayList<String>();
		tt.add(clientIds);
		tt.add(clientNames);
		return tt;
	}
	
	//将共享客户保存
	public void saveShareIds(ExpenseScheme scheme){
		String[] shareId = scheme.getShareIds().split(",");
		int size = shareId.length;
		expenseShareService.delSchemeNum(scheme.getSchemeNum());
		if(!"".equals(scheme.getShareIds())){
			for(int i=0;i<size;i++){
				BaseExpenseShare share = new BaseExpenseShare();
				share.setClientId(shareId[i]);
				share.setClientName(clientService.get(Integer.valueOf(share.getClientId())).getClientName());
				share.setSchemeNum(scheme.getSchemeNum());
				share.setSchemeName(scheme.getSchemeName());
				share.setProgramState("0");
				share.setIfParent("0");
				expenseShareService.save(share);
			}
		}
		BaseExpenseShare shareParent = new BaseExpenseShare();
		shareParent.setClientId(scheme.getCustomsId());
		shareParent.setClientName(scheme.getCustomsName());
		shareParent.setSchemeNum(scheme.getSchemeNum());
		shareParent.setSchemeName(scheme.getSchemeName());
		shareParent.setProgramState("0");
		shareParent.setIfParent("1");
		expenseShareService.save(shareParent);
	}
	
	/**
	 * 暂时保存全部的操作
	 * 
	 * @return list
	 */
	@RequestMapping(value = "saveall", method = RequestMethod.GET)
	@ResponseBody
	public String saveall(){
		List<ExpenseScheme> schemeList = expenseSchemeService.getAll();
		for(ExpenseScheme scheme : schemeList){
			expenseShareService.delSchemeNum(scheme.getSchemeNum());
			BaseExpenseShare shareParent = new BaseExpenseShare();
			shareParent.setClientId(scheme.getCustomsId());
			shareParent.setClientName(scheme.getCustomsName());
			shareParent.setSchemeNum(scheme.getSchemeNum());
			shareParent.setSchemeName(scheme.getSchemeName());
			shareParent.setProgramState("1");
			shareParent.setIfParent("1");
			expenseShareService.save(shareParent);
		}
		return "success";
	}
	
	
	/**
	 * 校验权限
	 * 
	 * @return list
	 */
	@RequestMapping(value = "checkPermission/{name}", method = RequestMethod.POST)
	@ResponseBody
	public String checkPermission(@PathVariable("name") String name) {
		User user =UserUtil.getCurrentUser();
		if(name.equals(user.getName())){
			return "success";
		}else{
			List<Permission> perList=permissionService.getPermissionsCW(user.getId());
			int cw=0;
			for(Permission obj:perList){
				if(obj.getId()==490||obj.getId().equals(490)){
					cw=1;
					break;
				}
			}
			if(cw==1){
				return "success";
			}else{
				return "false";
			}
		}
	}
}
