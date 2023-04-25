package com.haiersoft.ccli.wms.web;
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
import com.haiersoft.ccli.wms.entity.BisForecastInfo;
import com.haiersoft.ccli.wms.service.ForecastInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;

/**
 * 
 * @author pyl
 * @ClassName: ForecastInfoController
 * @Description: 入库预报单货物明细Controller
 * @date 2016年4月15日 下午2:02:22
 */
@Controller
@RequestMapping("wms/forecastinfo")
public class ForecastInfoController extends BaseController{
	
	//@Autowired
	//private ForecastService forecastService;
	@Autowired
	private ForecastInfoService forecastInfoService;
	//@Autowired
	//private SkuInfoService skuInfoService;
	
	/**
	 * 
	 * @author pyl
	 * @Description: 入库预报单货物明细
	 * @date 2016年4月14日 下午7:38:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json/{forId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("forId") String forId) {
		Page<BisForecastInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		
		PropertyFilter filter = new PropertyFilter("EQS_forId", forId);
		filters.add(filter);
		page = forecastInfoService.search(page, filters);
		return getEasyUIData(page);
       
	}
	
	/**
	 * 添加预报单货物信息跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create/{forId}", method = RequestMethod.GET)
	public String createForm(@PathVariable("forId") String forId,Model model) {
		model.addAttribute("forId",forId);
		model.addAttribute("bisForecastInfo", new BisForecastInfo());
		model.addAttribute("action", "create");
		return "wms/forecast/forecastInfoAdd";
	}
	
	/**
	 * 添加预报单货物信息
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BisForecastInfo bisForecastInfo,Model model, HttpServletRequest request) {
		forecastInfoService.save(bisForecastInfo);
		return "success";
	}
	
	/**
	 * 修改入库联系单明细跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisForecastInfo", forecastInfoService.get(id));
		model.addAttribute("action", "update");
		return "wms/forecast/forecastInfoAdd";
	}
	
	/**
	 * 修改入库联系单明细
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BisForecastInfo bisForecastInfo,Model model) {
		forecastInfoService.update(bisForecastInfo);
		return "success";
	}
	

	/**
	 * 
	 * @author pyl
	 * @Description: 删除
	 * @date 2016年4月15日 下午1:57:12 
	 * @param id
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteinfo/{ids}")
	@ResponseBody
	public String deleteContractInfo(@PathVariable("ids") List<Integer> ids) {
		for(int i=0;i<ids.size();i++){
			forecastInfoService.delete(ids.get(i));
		}
		return "success";
	}
	
	/**
	 * 复制入库联系单跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("bisForecastInfo", forecastInfoService.get(id));
		model.addAttribute("action", "copy");
		return "wms/forecast/forecastInfoAdd";
	}
	
	/**
	 * 复制后添加入库联系单
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	@ResponseBody
	public String createCopy(@Valid BisForecastInfo bisForecastInfo,Model model) {
		forecastInfoService.save(bisForecastInfo);
		return "success";
	}
}
