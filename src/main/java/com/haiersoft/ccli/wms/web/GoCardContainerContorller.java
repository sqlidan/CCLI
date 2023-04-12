package com.haiersoft.ccli.wms.web;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseGoCardContainer;
import com.haiersoft.ccli.wms.service.GoCardContainerService;
/**
 * GoCardContainerContorller
 * @author PYL
 * @date 2016年6月28日
 */
@Controller
@RequestMapping("gocard/container")
public class GoCardContainerContorller extends BaseController {
	
	@Autowired
	private GoCardContainerService goCardContainerService;
	
	/*跳转出门证散货列表*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "wms/gocard/container";
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 出门证集装箱列表查询
	 * @date 2016年6月24日 下午2:20:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseGoCardContainer> page = getPage(request);
		page.orderBy("goCard").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = goCardContainerService.search(page, filters);
		return getEasyUIData(page);
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
	@RequestMapping(value = "delete/{gocard}")
	@ResponseBody
	public String deleteEnterStock(@PathVariable("gocard") String gocard) {
		goCardContainerService.delete(gocard);
		return "success";
	}
	
	/**
	 * @author pyl 
	 * @Description: 管理页面跳转
	 * @date 2016年6月29日 下午2:42:15 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value="add", method = RequestMethod.GET)
	public String createContractForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user",user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		String goCard = goCardContainerService.getNewGoCard();
		model.addAttribute("goCard", goCard);
		return "wms/gocard/containerManager";
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 修改展示
	 * @return
	 * @throws
	 */
	@RequestMapping(value="update/{gocard}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("gocard") String gocard) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		BaseGoCardContainer obj = goCardContainerService.get(gocard);
		model.addAttribute("goCard", gocard);
		model.addAttribute("obj", obj);
		model.addAttribute("action", "update");
		return "wms/gocard/containerManager";
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
		BaseGoCardContainer obj = new BaseGoCardContainer();
		parameterReflect.reflectParameter(obj, request);//转换对应实体类参数
		goCardContainerService.save(obj);
		return "success";
	}
	
	/**
	 * 
	 * @author pyl
	 * @Description: 判断是否已保存
	 * @date 2016年6月25日
	 * @return
	 * @throws
	 */
	@RequestMapping(value="ifsave/{gocard}", method = RequestMethod.GET)
	@ResponseBody
	public String ifsave(@PathVariable("gocard") String gocard) {
		BaseGoCardContainer obj = goCardContainerService.get(gocard);
		if(null != obj && !"".equals(obj)){
			return "success";
		}else{
			return "false";
		}
	}

	
	/**
	 * 集装箱出门证打印跳转
	 * 
	 * @param linkId
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "print/{gocard}", method = RequestMethod.GET)
	public String print(@PathVariable("gocard") String gocard, Model model) {
		BaseGoCardContainer obj = goCardContainerService.get(gocard);
		model.addAttribute("obj", obj);
		String xianghao = "";
		if("1".equals(obj.getIfEmpty())){
			xianghao += "E";
		}else{
			xianghao += "F";
		}
		xianghao += obj.getTheSize()+"X";
		if(StringUtils.isNull(obj.getCtnNumTwo())){
			xianghao += "1";
		}else{
			xianghao += "2";
		}
		if(obj.getAppearTime().toString().length()>10){
			model.addAttribute("appearTime", 
					obj.getAppearTime().toString().substring(0, 11).replaceAll("-", ""));
		}
		
		model.addAttribute("xianghao", xianghao);
		model.addAttribute("action", "print");
		return "wms/gocard/containerPrint";
	}
}
