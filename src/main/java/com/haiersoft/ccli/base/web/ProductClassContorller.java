package com.haiersoft.ccli.base.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

import com.haiersoft.ccli.api.service.GoodsService;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.base.service.ProductClassService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
/**
 * 货物类型controller
 * @author lzg
 * @date 2016年2月24日
 */
@Controller
@RequestMapping("base/product")
public class ProductClassContorller extends BaseController {
	@Autowired
	private ProductClassService productClassService;
	@Autowired
	private GoodsService goodsService;
	
	
	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String list(){
		return "base/productclassList";
	}
	
	
	/*
	 * 列表显示页面获取json
	 * */
	 
	@RequestMapping(value="listjsonInfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDataInfo(HttpServletRequest request) {
		Page<BaseProductClass> page = getPage(request);
		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page = productClassService.seachSql(page, map);
		return getEasyUIData(page);
	}		   
	
	/*
	 * 列表页面table获取json
	 * */
	 
	@RequestMapping(value="listjson",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseProductClass> page = getPage(request);
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		if(param!=null && !"".equals(param)){
			PropertyFilter filterparam = new PropertyFilter("LIKES_pName",String.valueOf(param));
			filters.add(filterparam);
		}
		page = productClassService.search(page, filters);
		return getEasyUIData(page);
	}
	/**
	 * 添加PRODUCT跳转
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("action", "create");
		return "base/productclassForm";
	}
	/**
	 * 添加PRODUCT
	 * 
	 * @param user
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String create(@Valid BaseProductClass baseProductClass,Model model, HttpServletRequest request) {
		productClassService.save(baseProductClass);
		if(baseProductClass.getId()!=null) {
			goodsService.getInfoById(baseProductClass.getId());
		}
		return "success";
	}

	/**
	 * 修改PRODUCT跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("product", productClassService.get(id));
		model.addAttribute("action", "update");
		return "base/productclassForm";
	}
	
	/**
	 * 修改PRODUCT目
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String update(@Valid @ModelAttribute @RequestBody BaseProductClass baseProductClass,Model model) {
		productClassService.update(baseProductClass);
		if(baseProductClass.getId()!=null) {
			goodsService.getInfoById(baseProductClass.getId());
		}
		return "success";
	}

	
	
	
	
	@RequestMapping(value="ajaxlist",method = RequestMethod.GET)
	@ResponseBody
	public List<BaseProductClass> getAjaxData(HttpServletRequest request) {
		Page<BaseProductClass> page = getPage(request);
		String param = request.getParameter("q");// 搜索值
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		if(param!=null && !"".equals(param)){
			PropertyFilter filterparam = new PropertyFilter("LIKES_pName",String.valueOf(param));
			PropertyFilter filterparam2 = new PropertyFilter("GTI_printId",0);
			filters.add(filterparam);
			filters.add(filterparam2);
			page = productClassService.search(page, filters);
		}
		if(page!=null){
			return page.getResult();
		}
		return null;
	}
	 
}
