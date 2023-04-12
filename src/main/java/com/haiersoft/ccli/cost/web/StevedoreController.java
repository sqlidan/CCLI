package com.haiersoft.ccli.cost.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.Stevedore;
import com.haiersoft.ccli.cost.service.StevedoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户controller
 * @author test
 * @date 2022
 */
@Controller
@RequestMapping("cost/stevedore")
public class StevedoreController extends BaseController {

	@Autowired
	private StevedoreService stevedoreService;

	/**
	 * 默认页面
	 */
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list() {
		return "cost/stevedore/stevedoreList";
	}

	/**
	 * 获取用户json
	 */
	//@RequiresPermissions("sys:user:view")
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<Stevedore> page = getPage(request);
		page.orderBy("createDate").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = stevedoreService.search(page, filters);
		return getEasyUIData(page);
	}


	@RequestMapping(value = "page")
	@ResponseBody
	public Map<String, Object> pageStevedore(HttpServletRequest request) {

		Page<Stevedore> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = stevedoreService.pageStevedore(page,filters);
		return getEasyUIData(page);
	}


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("stevedore", new Stevedore());
		model.addAttribute("action", "create");
		return "cost/stevedore/stevedoreForm";
	}


	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Stevedore user, Model model) {
		stevedoreService.save(user);
		return "success";
	}


	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("stevedore", stevedoreService.get(id));
		model.addAttribute("action", "update");
		return "cost/stevedore/stevedoreForm";
	}


	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody Stevedore stevedore,Model model) {
		stevedoreService.update(stevedore);
		return "success";
	}

	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		stevedoreService.delete(id);
		return "success";
	}

	@RequestMapping(value = "checkName")
	@ResponseBody
	public String checkName(String name) {
		if (stevedoreService.getStevedore(name) == null) {
			return "true";
		} else {
			return "false";
		}
	}

}
