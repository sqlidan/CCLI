package com.haiersoft.ccli.wms.web;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import com.haiersoft.ccli.wms.entity.BisCustomsDeclarationInfo;
import com.haiersoft.ccli.wms.service.CustomsDeclarationInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 
 * @author pyl
 * @ClassName: CustomsDeclarationOutInfoController
 * @Description: 出库报关单明细Controller
 * @date 2016年3月3日 下午2:02:22
 */
@Controller
@RequestMapping("wms/customsoutinfo")
public class CustomsDeclarationOutInfoController extends BaseController{
	
	//@Autowired
	//private CustomsDeclarationService customsDeclarationService;
	@Autowired
	private CustomsDeclarationInfoService customsDeclarationInfoService;
	//@Autowired
	//private SkuInfoService skuInfoService;
	
	/**
	 * 
	 * @author pyl
	 * @Description: 出库报关单货物查询
	 * @date 2016年3月3日 下午2:06:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("id") String id) {
		Page<BisCustomsDeclarationInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		PropertyFilter filter = new PropertyFilter("EQS_cusId", id);
		filters.add(filter);
		page = customsDeclarationInfoService.search(page, filters);
		return getEasyUIData(page);
       
	}
	
	/**
	 * 添加出库联系单明细跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create/{cusId}/{cdNum}", method = RequestMethod.GET)
	public String createForm(@PathVariable("cusId") String cusId,@PathVariable("cdNum") String cdNum,Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user",user.getName());
		model.addAttribute("date",new Date());
		model.addAttribute("cusId",cusId);
		model.addAttribute("cdNum", cdNum);
		model.addAttribute("bisCustomsInfo", new BisCustomsDeclarationInfo());
		model.addAttribute("action", "create");
		return "wms/customsOut/customsOutAdd";
	}
	
	/**
	 * 添加出库报关单明细
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisCustomsDeclarationInfo bisCustomsDeclarationInfo,Model model, HttpServletRequest request) {
		//User user = UserUtil.getCurrentUser();
		customsDeclarationInfoService.save(bisCustomsDeclarationInfo);
		return "success";
	}
	
	/**
	 * 修改出库报关单明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisCustomsInfo", customsDeclarationInfoService.get(id));
		model.addAttribute("action", "update");
		return "wms/customsOut/customsOutAdd";
	}
	
	/**
	 * 修改出库报关单明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BisCustomsDeclarationInfo bisCustomsDeclarationInfo,Model model) {
		customsDeclarationInfoService.update(bisCustomsDeclarationInfo);
		return "success";
	}
	
	/**
	 * 复制出库报关单跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisCustomsInfo", customsDeclarationInfoService.get(id));
		model.addAttribute("action", "copy");
		return "wms/customsOut/customsOutAdd";
	}
	
	/**
	 * 复制后添加出库报关单
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	@ResponseBody
	public String createCopy(@Valid BisCustomsDeclarationInfo bisCustomsDeclarationInfo,Model model) {
		customsDeclarationInfoService.save(bisCustomsDeclarationInfo);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年3月3日 下午2:37:12 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteinfo/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		for(int i=0;i<ids.size();i++){
			customsDeclarationInfoService.delete(ids.get(i));
		}
		return "success";
	}
	
//	/**
//	 * 入库联系单明细excel跳转
//	 * 
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "into/{linkId}", method = RequestMethod.GET)
//	public String intoExcel( Model model,@PathVariable("linkId") String linkId) {
//		model.addAttribute("linkId", linkId);
//		model.addAttribute("action", "intot");
//		return "wms/enterStock/enterStockInfoInto";
//	}
//	
//	/**
//	 *入库联系单明细excel 
//	 * @param id
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "intot/{linkId}", method = RequestMethod.POST)
//	@ResponseBody
//	public String intoExcel2(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, Model model,@PathVariable("linkId") String linkId) {
//		try {
//			ImportParams params = new ImportParams();  
//			params.setTitleRows(1);
//			List<EnterStockInfoToExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), EnterStockInfoToExcel.class, params);
//			System.out.println(list.size());
//			BisEnterStockInfo newObj=null;
//			BisEnterStock jumpObj = null;
//			BaseSkuBaseInfo skuObj = null;
//			User user = UserUtil.getCurrentUser();
//			if(list!=null && list.size()>0){
//				for(EnterStockInfoToExcel getObj:list){
//					newObj=new BisEnterStockInfo();
//					jumpObj=new BisEnterStock();
//					skuObj = new BaseSkuBaseInfo();
//						newObj.setLinkId(linkId);
//						jumpObj=enterStockService.get(linkId);
//						if(jumpObj != null){
//						   newObj.setItemNum(jumpObj.getItemNum());
//						   }
//						newObj.setCtnNum(getObj.getCtnNum());
//						newObj.setProjectNum(getObj.getProjectNum());
//						if(getObj.getSku() != null && !"".equals(getObj.getSku())){
//							skuObj = skuInfoService.get(getObj.getSku());
//							    if(skuObj != null){
//							    	newObj.setSku(getObj.getSku());
//							    	newObj.setCargoName(skuObj.getCargoName());
//							    	newObj.setTypeSize(skuObj.getTypeSize());
//							    	newObj.setGrossSingle(skuObj.getGrossSingle());
//							    	newObj.setNetSingle(skuObj.getNetSingle());
//							    	newObj.setPiece(getObj.getPiece());
//							    	newObj.setGrossWeight(skuObj.getGrossSingle() * getObj.getPiece());
//							    	newObj.setNetWeight(skuObj.getNetSingle() * getObj.getPiece());
//							    	newObj.setUnits(skuObj.getUnits());
//							    }
//							}
//						enterStockInfoService.save(newObj);
//					}
//				}
//		}   catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "success";
//	}
}
