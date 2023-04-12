package com.haiersoft.ccli.base.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.service.TaxRateService;

/**
 * 税率controller
 * @author pyl
 * @date 2016年2月27日
 */
@Controller
@RequestMapping("base/taxrate")
public class TaxRateController extends BaseController {

	@Autowired
	private TaxRateService taxRateService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "base/taxRate";
	}
	
	/**
	 * 获取税率列表
	 */
	@RequiresPermissions("base:taxrate:view")
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseTaxRate> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = taxRateService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 添加费目代码跳转
	 * 
	 * @param model
	 */
	@RequiresPermissions("base:taxrate:add")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("BaseTaxRate", new BaseTaxRate());
		model.addAttribute("action", "create");
		return "base/taxRateForm";
	}

	/**
	 * 添加税率
	 * 
	 * @param user
	 * @param model
	 */
	@RequiresPermissions("base:taxrate:add")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid BaseTaxRate baseTaxRate,Model model, HttpServletRequest request) {
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		java.util.Date time=null;
//		try {
//		   time= sdf.parse(sdf.format(new Date()));
//		} catch (Exception e) {
//		   e.printStackTrace();
//		}
//		baseTaxRate.setTheDate(time);
		taxRateService.save(baseTaxRate);
		return "success";
	}

	/**
	 * 修改税率跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("base:taxrate:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("BaseTaxRate", taxRateService.get(id));
		model.addAttribute("action", "update");
		return "base/taxRateForm";
	}

	/**
	 * 修改税率
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("base:taxrate:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody BaseTaxRate baseTaxRate,Model model) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date time=null;
		try {
		   time= sdf.parse(sdf.format(new Date()));
		} catch (Exception e) {
		   e.printStackTrace();
		}
		baseTaxRate.setRepairDate(time);
		taxRateService.update(baseTaxRate);
		return "success";
	}

	/**
	 * 删除税率
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("base:taxrate:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		taxRateService.delete(id);
		return "success";
	}
	
	/**
	 * Ajax请求校验币种是否唯一。
	 */
    @RequestMapping(value = "checkCurrency")
	@ResponseBody
	public String checkCurrency(String currency) {
		List<BaseTaxRate> getList= taxRateService.getCurrency(currency);
		if(getList.isEmpty() ){
			return "true";
		}else{
			return "false";
		}
	}
	@RequestMapping(value = "getobj/{currency}")
	@ResponseBody
	public BaseTaxRate getobj(@PathVariable("currency") String currency) {
		BaseTaxRate getObj=taxRateService.getTaxByC(currency);
		if(getObj==null){
			getObj=new BaseTaxRate();
		}
		return getObj;
	}
}