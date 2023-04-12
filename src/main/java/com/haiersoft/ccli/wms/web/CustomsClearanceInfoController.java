package com.haiersoft.ccli.wms.web;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.haiersoft.ccli.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisCustomsClearance;
import com.haiersoft.ccli.wms.entity.BisCustomsClearanceInfo;
import com.haiersoft.ccli.wms.service.CustomsClearanceInfoService;
import com.haiersoft.ccli.wms.service.CustomsClearanceService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.ExcelStyleUtil;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 
 * @author 
 */
@Controller
@RequestMapping("wms/customs/clearance/info")
public class CustomsClearanceInfoController extends BaseController{
	
	@Autowired
	private CustomsClearanceService customsClearanceService;
	
	@Autowired
	private CustomsClearanceInfoService customsClearanceInfoService;
	
	
	
	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/customs/customsManagerClearance";
	}
	
	
	
	/**
	 * 
	 * @author 
	 * @Description: 货物查询
	 * @date 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{cdNum}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("cdNum") String cdNum) {
		Page<BisCustomsClearanceInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		PropertyFilter filter = new PropertyFilter("EQS_cusId", cdNum);
		filters.add(filter);
		page = customsClearanceInfoService.search(page, filters);
		return getEasyUIData(page);
       
	}
	
	/**
	 * 添加入库联系单明细跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create/{cdNum}", method = RequestMethod.GET)
	public String createForm(@PathVariable("cdNum") String cdNum,Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user",user.getName());
		model.addAttribute("date",new Date());
		model.addAttribute("idd",cdNum);
		//model.addAttribute("cdNum", cdNum);
		model.addAttribute("bisCustomsInfo", new BisCustomsClearanceInfo());
		model.addAttribute("action", "create");
		return "wms/customs/customsClearanceAdd";
	}
	
	/**
	 * 添加入库联系单明细
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisCustomsClearanceInfo info,Model model, HttpServletRequest request) {
		//User user = UserUtil.getCurrentUser();
		customsClearanceInfoService.save(info);
		return "success";
	}
	
	
	/**
	 * 添加入库联系单明细
	 * 
	 * @param 
	 * @param 
	 */
	@RequestMapping(value = "saveInfo", method = RequestMethod.POST)
	@ResponseBody
	public String saveAll(@RequestBody List<BisCustomsClearanceInfo> info) {
		//User user = UserUtil.getCurrentUser();
		List<BisCustomsClearanceInfo> a = info;
		for(BisCustomsClearanceInfo i:info) {
			if(i.getId()==null)
				customsClearanceInfoService.save(i);
			customsClearanceInfoService.update(i);
		}
		//customsTestInfoService.save(bisCustomsTestInfo);
		return "success";
	}
	
	/**
	 * 修改入库报关单明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisCustomsInfo", customsClearanceInfoService.get(id));
		model.addAttribute("action", "update");
		return "wms/customs/customsClearanceAdd";
	}
	
	/**
	 * 修改入库报关单明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BisCustomsClearanceInfo info,Model model) {
		customsClearanceInfoService.update(info);
		return "success";
	}
	
	/**
	 * 复制入库报关单跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisCustomsInfo", customsClearanceInfoService.get(id));
		model.addAttribute("action", "copy");
		return "wms/customs/customsAddClearance";
	}
	
	/**
	 * 复制后添加入库联系单
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	@ResponseBody
	public String createCopy(@Valid BisCustomsClearanceInfo info,Model model) {
		customsClearanceInfoService.save(info);
		return "success";
	}
	
	/**
	 * 
	 * @author 
	 * @Description: 删除
	 * @date 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteinfo/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		for(int i=0;i<ids.size();i++){
			customsClearanceInfoService.delete(ids.get(i));
		}
		return "success";
	}
	
	//根据报关单号查询info	
	@RequestMapping(value = "getRecordByDNumber")
	@ResponseBody
	public List<Map<String, Object>> getRecordByDNumber(String dNumber) {
		
		if(StringUtils.isNotEmpty(dNumber)) {
			List<Map<String, Object>> infos = customsClearanceInfoService.getRecordByDNumber(dNumber);
			return infos;
		}
		return null;		
		
	}
	
	
	/**
	 * 出库联系单明细导出报表
	 */
	@RequestMapping(value = "exportinfo/{cdNum}", method = RequestMethod.POST)
	@ResponseBody
	public void exportInfo(HttpServletRequest request,
						   HttpServletResponse response,
						   Model model,
						   @PathVariable("cdNum") String cdNum) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		TemplateExportParams params = new TemplateExportParams("exceltemplate/customsClearanceInfo.xls");

		String excelName = "通关管理.xls";

		BisCustomsClearance bisCustoms = customsClearanceService.get(cdNum);

		//BisOutStock outStock = customsTestInfoService.get(outLinkId);
//		map.put("receiver", outStock.getReceiver());
//		map.put("stockIn", outStock.getStockIn());
//		map.put("outLinkId", outLinkId);
//		map.put("OutCustomsCount", outStock.getOutCustomsCount()!=null?outStock.getOutCustomsCount():0);
//		map.put("OutCiqCount", outStock.getOutCiqCount()!=null?outStock.getOutCiqCount():0);//
//		if (!StringUtils.isNull(outStock.getSellFee())) {
//			map.put("sellFee", outStock.getSellFee());
//		} else {
//			map.put("sellFee", "");
//		}
//
//		if ("0".equals(outStock.getIfCustomsClearance())) {
//			map.put("clearance", "否");
//		} else {
//			map.put("clearance", "是");
//		}
//		if ("0".equals(outStock.getIfBonded())) {
//			map.put("bonded", "否");
//		} else {
//			map.put("bonded", "是");
//		}
//		if ("0".equals(outStock.getIfClearStore())) {
//			map.put("clear", "否");
//		} else {
//			map.put("clear", "是");
//		}
		User user = UserUtil.getCurrentUser();


		ExcelStyleUtil.entityToMap(map,bisCustoms);
		if(map.get("modeTrade").equals("0")){
			map.put("modeTrade","保税");

		}else if(map.get("modeTrade").equals("1")){
			map.put("modeTrade","来料加工");

		}else if(map.get("modeTrade").equals("2")){
			map.put("modeTrade","进料加工");

		}else if(map.get("modeTrade").equals("3")){
			map.put("modeTrade","一般贸易");
		}


		if(map.get("auditingState").equals("0")){
			map.put("auditingState","未审核");

		}else if(map.get("auditingState").equals("1")){
			map.put("auditingState","已提交");

		}else if(map.get("auditingState").equals("2")){
			map.put("auditingState","已驳回");

		}else if(map.get("auditingState").equals("3")){
			map.put("auditingState","已审核");

		}

		if(map.get("serviceProject").equals("0")){
			map.put("serviceProject","报进");

		}else if(map.get("serviceProject").equals("1")){
			map.put("serviceProject","报出");

		}


		//	listMap = outStockInfoService.searchInfo(outLinkId);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		PropertyFilter filter = new PropertyFilter("EQS_cusId", cdNum);
		filters.add(filter);
		List<BisCustomsClearanceInfo> outStock = customsClearanceInfoService.search( filters);

		if (null != outStock && outStock.size() > 0) {

			for(BisCustomsClearanceInfo a :outStock){
				Map map1=new HashMap();

				map1=ExcelStyleUtil.entityToMap(a);
       // 有无木制包装  转义
//				if (!StringUtils.isNull(a.getIfWoodenPacking())) {
//					if(a.getIfWoodenPacking().equals("0")){
//						map1.put("ifWoodenPacking", "无");
//
//					}else if(a.getIfWoodenPacking().equals("1")){
//						map1.put("ifWoodenPacking", "有");
//					}else{
//						map1.put("ifWoodenPacking", "");
//
//					}
//				} else {
//					map1.put("ifWoodenPacking", "");
//				}
				// 有无木制包装  转义
				if(map1.get("ifWoodenPacking").equals("0")){

						map1.put("ifWoodenPacking", "无");


					}else if(map1.get("ifWoodenPacking").equals("1")){

						map1.put("ifWoodenPacking", "有");

					}

				// 币制  转义
				if(map1.get("currencyValue").equals("0")){
					map1.put("currencyValue","人民币");

				}else if(map1.get("currencyValue").equals("1")){
					map1.put("currencyValue","美元");

				}else if(map1.get("currencyValue").equals("2")){
					map1.put("currencyValue","日元");

				}else if(map1.get("currencyValue").equals("3")){
					map1.put("currencyValue","欧元");
				}else if(map1.get("currencyValue").equals("4")){
					map1.put("currencyValue","英镑");
				}

				listMap.add(map1)	;
			//	listMap.add(ExcelStyleUtil.entityToMap(a))	;
			}
			map.put("maplist", listMap);
		}

		Workbook workbook = ExcelExportUtil.exportExcel(params, map);
		String formatFileName = new String(excelName.getBytes("GB2312"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName + "\"");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		OutputStream os = response.getOutputStream();
		workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
}
