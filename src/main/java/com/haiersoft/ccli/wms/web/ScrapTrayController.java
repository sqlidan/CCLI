package com.haiersoft.ccli.wms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisScrapTray;
import com.haiersoft.ccli.wms.service.ScrapTrayService;

/**
 * 
 * @author Connor.M
 * @ClassName: ScrapTrayController
 * @Description: 报损Controller
 * @date 2016年3月21日 下午4:07:09
 */
@Controller
@RequestMapping("wms/scrap")
public class ScrapTrayController extends BaseController {
	
	@Autowired
	private ScrapTrayService scrapTrayService;
	
	
	/**
	 * 默认查询页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/scrapTray/scrapTrayList";
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 报损  分页查询
	 * @date 2016年3月21日 下午4:09:47 
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisScrapTray> page = getPage(request);
		page.setOrder(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = scrapTrayService.search(page, filters);
		return getEasyUIData(page);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 报损操作  界面展示
	 * @date 2016年3月21日 下午4:38:52 
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "scrapTrayForm", method = RequestMethod.GET)
	public String scrapTrayForm(Model model) {
		String scrapCode = scrapTrayService.getTrayCode();
		model.addAttribute("scrapCode", scrapCode);
		return "wms/scrapTray/scrapTrayForm";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 报损确认
	 * @date 2016年3月21日 下午6:57:43 
	 * @param newIdsList
	 * @return
	 * @throws
	 */
	@RequestMapping(value="{scrapCode}/{scrapType}/scrapTrayConfirm")
	@ResponseBody
	public String scrapTrayConfirm(@PathVariable("scrapCode") String scrapCode, @PathVariable("scrapType") String scrapType, @RequestBody List<Integer> newIdsList) {
		return scrapTrayService.scrapTrayConfirm(scrapCode, scrapType, newIdsList);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 查看货损信息
	 * @date 2016年5月10日 下午6:36:04 
	 * @param scrapCode
	 * @param scrapType
	 * @param newIdsList
	 * @return
	 * @throws
	 */
	@RequestMapping(value="showScrapTray/{trayCode}")
	@ResponseBody
	public List<BisScrapTray> showScrapTray(@PathVariable("trayCode") String trayCode) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
//		filters.add(new PropertyFilter("EQS_scrapCode", scrapCode));
		filters.add(new PropertyFilter("EQS_trayCode", trayCode));
		List<BisScrapTray> scrapTrays = scrapTrayService.search(filters);
		return scrapTrays;
	}
	
	/**
	 * 打印装车清单
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "print/{scrapCode}", method = RequestMethod.GET)
	public String printInfo(@PathVariable("scrapCode") String scrapCode,Model model) {
		if(scrapCode!=null && !"".equals(scrapCode)){
			List<BisScrapTray> theList = scrapTrayService.findListByScrapCode(scrapCode);
			if(theList!=null && theList.size()>0){
				model.addAttribute("tnum",scrapCode);
				model.addAttribute("date",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
				model.addAttribute("isok", 1);
			}else{
				model.addAttribute("isok", 0);
				model.addAttribute("user", "货损报表未查询到");
				model.addAttribute("adress","货损报表未查询到");
			}
				model.addAttribute("infolist",  theList);
			}
		return "wms/scrapTray/scrapTrayPrint";
	}
	
}
