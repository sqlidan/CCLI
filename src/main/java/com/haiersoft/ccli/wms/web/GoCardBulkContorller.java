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
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BaseGoCardBulk;
import com.haiersoft.ccli.wms.service.GoCardBulkService;
/**
 * GoCardBulkContorller
 * @author PYL
 * @date 2016年6月28日
 */
@Controller
@RequestMapping("gocard/bulk")
public class GoCardBulkContorller extends BaseController {
	
	@Autowired
	private GoCardBulkService goCardBulkService;
	
	/*跳转出门证散货列表*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String menuList(){
		return "wms/gocard/bulk";
	}
	
	
	/**
	 * 
	 * @author pyl
	 * @Description: 出门证散货列表查询
	 * @date 2016年6月24日 下午2:20:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseGoCardBulk> page = getPage(request);
		page.orderBy("goCard").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = goCardBulkService.search(page, filters);
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
		goCardBulkService.delete(gocard);
		return "success";
	}
	
	/**
	 * @author pyl 
	 * @Description: 管理页面跳转
	 * @date 2016年3月2日 下午2:42:15 
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
		String goCard=goCardBulkService.getNewGoCard();
		model.addAttribute("goCard", goCard);
		return "wms/gocard/bulkManager";
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
		BaseGoCardBulk obj = goCardBulkService.get(gocard);
		model.addAttribute("goCard", gocard);
		model.addAttribute("obj", obj);
		model.addAttribute("action", "update");
		return "wms/gocard/bulkManager";
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
		BaseGoCardBulk obj = new BaseGoCardBulk();
		parameterReflect.reflectParameter(obj, request);//转换对应实体类参数
		goCardBulkService.save(obj);
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
		BaseGoCardBulk obj = goCardBulkService.get(gocard);
		if(null != obj && !"".equals(obj)){
			return "success";
		}else{
			return "false";
		}
	}

	
	/**
	 * 散杂货出门证打印跳转
	 * 
	 * @param linkId
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "print/{gocard}", method = RequestMethod.GET)
	public String print(@PathVariable("gocard") String gocard, Model model) {
    	User user=UserUtil.getCurrentUser();
    	model.addAttribute("user", user.getName());
		BaseGoCardBulk obj = goCardBulkService.get(gocard);
		Date date = obj.getLeaveTime();
		model.addAttribute("year", DateUtils.getYear(date));
		model.addAttribute("month", DateUtils.getMonth(date));
		model.addAttribute("day", DateUtils.getDay(date));
		model.addAttribute("hour", DateUtils.getHour(date));
		model.addAttribute("minute", DateUtils.getMinute(date));
		model.addAttribute("obj", obj);
		model.addAttribute("action", "print");
		return "wms/gocard/bulkPrint";
	}
}
