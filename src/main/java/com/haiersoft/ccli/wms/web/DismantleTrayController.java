package com.haiersoft.ccli.wms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisDismantleTray;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.DismantleTrayService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * 
 * @author Connor.M
 * @ClassName: DismantleTrayController
 * @Description: 拆托Controller
 * @date 2016年3月17日 下午4:18:33
 */
@Controller
@RequestMapping("wms/dismantle")
public class DismantleTrayController extends BaseController {

	@Autowired
	private DismantleTrayService dismantleTrayService;
	@Autowired
	private TrayInfoService trayInfoService;

	/**
	 * 默认查询页面
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list() {
		return "wms/dismantleTray/dismantleTrayList";
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 拆托分页查询
	 * @date 2016年3月17日 下午4:26:36
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisDismantleTray> page = getPage(request);
		page.setOrder(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter
				.buildFromHttpRequest(request);
		page = dismantleTrayService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: 拆托添加界面
	 * @date 2016年3月17日 下午5:03:47
	 * @param model
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "dismantleForm", method = RequestMethod.GET)
	public String dismantleForm(Model model) {
		return "wms/dismantleTray/dismantleForm";
	}

	/**
	 * 
	 * @author Connor.M
	 * @Description: web 拆托确认
	 * @date 2016年3月18日 下午2:13:51
	 * @param id
	 * @param newTrayCode
	 * @param num
	 * @return
	 * @throws
	 */
	@RequestMapping("{id}/dismantleTrayWebConfirm")
	@ResponseBody
	public String dismantleTrayWebConfirm(@PathVariable("id") Integer id,
			@RequestParam String newTrayCode, @RequestParam String num) {
		String res = "success";
		TrayInfo trayInfo = trayInfoService.get(id);
		// 判断状态
		if ("01".equals(trayInfo.getCargoState())) {
			res = dismantleTrayService.dismantleTrayWebConfirm(trayInfo,
					newTrayCode, num);
		} else {
			res = "error";
		}
		return res;
	}

}
