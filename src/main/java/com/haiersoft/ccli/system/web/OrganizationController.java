package com.haiersoft.ccli.system.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.Organization;
import com.haiersoft.ccli.system.service.OrganizationService;

/**
 * 机构信息controller
 * @author ty
 * @date 2015年1月22日
 */
@Controller
@RequestMapping("system/organization")
public class OrganizationController extends BaseController{

	@Autowired
	private OrganizationService organizationService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "system/organization/orgList";
	}
	
	/**
	 * 获取机构信息json
	 */
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public List<Organization> areaInfoList(HttpServletRequest request) {
		List<Organization> organizations=organizationService.getAll();
		return organizations;
	}
	
	/**
	 * 添加机构信息跳转
	 * 
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("organization", new Organization());
		model.addAttribute("action", "create");
		return "system/organization/orgForm";
	}

	/**
	 * 添加机构信息
	 * 
	 * @param organization
	 * @param model
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Organization organization, Model model) {
		organizationService.save(organization);
		return "success";
	}

	/**
	 * 修改机构信息跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("organization", organizationService.get(id));
		model.addAttribute("action", "update");
		return "system/organization/orgForm";
	}

	/**
	 * 修改机构信息
	 * 
	 * @param goodsType
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody Organization organization,Model model) {
		organizationService.update(organization);
		return "success";
	}

	/**
	 * 删除机构信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		organizationService.delete(id);
		return "success";
	}
	
	/**
	 * 
	 * @author PYL
	 * @Description: 获得所有部门
	 * @date 2016年3月17日 下午4:01:14 
	 * @param code
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getOrgAll", method = RequestMethod.GET)
	@ResponseBody
	public List<Organization> getOrgAll(HttpServletRequest request, HttpServletResponse response) {
		List<Organization> org = new ArrayList<Organization>();
		String param = request.getParameter("q");//搜索值
		String setid = request.getParameter("setid");//原数据填充值
		if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
			List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
			if (param != null && !"".equals(param)) {
				PropertyFilter filterparam = new PropertyFilter("LIKES_orgName", String.valueOf(param));
				filters.add(filterparam);
				org = organizationService.search(filters);
			} else {
				//根据原值id获取对象
				Organization getObj = organizationService.get(Integer.valueOf(setid));
				if (getObj != null) {
					org.add(getObj);
				}
			}
		}
		return org;
	}
	
}
