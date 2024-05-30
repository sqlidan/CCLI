package com.haiersoft.ccli.system.web;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.haiersoft.ccli.common.utils.PropertiesUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.UserOrgService;
import com.haiersoft.ccli.system.service.UserRoleService;
import com.haiersoft.ccli.system.service.UserService;

/**
 * 用户controller
 * @author ty
 * @date 2015年1月13日
 */
@Controller
@RequestMapping("system/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private UserOrgService userOrgService;

	/**
	 * 默认页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "system/userList";
	}

	/**
	 * 获取用户json
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value="json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<User> page = getPage(request);
		page.orderBy("createDate").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = userService.search(page, filters);
		return getEasyUIData(page);
	}

	/**
	 * 添加用户跳转
	 * 
	 * @param model
	 */
	@RequiresPermissions("sys:user:add")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("action", "create");
		return "system/userForm";
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @param model
	 */
	@RequiresPermissions("sys:user:add")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid User user, Model model) {
		userService.save(user);
		return "success";
	}

	/**
	 * 修改用户跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("user", userService.get(id));
		model.addAttribute("action", "update");
		return "system/userForm";
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid @ModelAttribute @RequestBody User user,Model model) {
		userService.update(user);
		return "success";
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:user:delete")
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		userService.delete(id);
		return "success";
	}

	/**
	 * 弹窗页-用户拥有的角色
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:roleView")
	@RequestMapping(value = "{userId}/userRole")
	public String getUserRole(@PathVariable("userId") Integer id, Model model) {
		model.addAttribute("userId", id);
		return "system/userRoleList";
	}
	/**
	 * 弹窗页-用户所在机构
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:orgView")
	@RequestMapping(value = "{userId}/userOrg")
	public String getUserOrg(@PathVariable("userId") Integer id, Model model) {
		model.addAttribute("userId", id);
		return "system/userOrgList";
	}

	/**
	 * 获取用户拥有的角色ID集合
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:user:roleView")
	@RequestMapping(value = "{id}/role")
	@ResponseBody
	public List<Integer> getRoleIdList(@PathVariable("id") Integer id) {
		return userRoleService.getRoleIdList(id);
	}
	/**
	 * 获取用户拥有的机构ID集合
	 * @param id
	 * @return
	 */
	@RequiresPermissions("sys:user:orgView")
	@RequestMapping(value = "{id}/org")
	@ResponseBody
	public List<Integer> getOrgIdList(@PathVariable("id") Integer id) {
		return userOrgService.getOrgIdList(id);
	}

	/**
	 * 修改用户拥有的角色
	 * 
	 * @param id
	 * @param newRoleList
	 * @return
	 */
	@RequiresPermissions("sys:user:roleUpd")
	@RequestMapping(value = "{id}/updateRole")
	@ResponseBody
	public String updateUserRole(@PathVariable("id") Integer id,@RequestBody List<Integer> newRoleList) {
		userRoleService.updateUserRole(id, userRoleService.getRoleIdList(id),newRoleList);
		return "success";
	}
	/**
	 * 修改用户所在的部门
	 * 
	 * @param id
	 * @param newRoleList
	 * @return
	 */
	@RequiresPermissions("sys:user:orgUpd")
	@RequestMapping(value = "{id}/updateOrg")
	@ResponseBody
	public String updateUserOrg(@PathVariable("id") Integer id,@RequestBody List<Integer> newRoleList) {
		userOrgService.updateUserOrg(id,newRoleList);
		return "success";
	}

	/**
	 * 修改密码跳转
	 */
	@RequestMapping(value = "updatePwd", method = RequestMethod.GET)
	public String updatePwdForm(Model model, HttpSession session) {
		model.addAttribute("user", session.getAttribute("user"));
		return "system/updatePwd";
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "updatePwd", method = RequestMethod.POST)
	@ResponseBody
	public String updatePwd(String oldPassword,@Valid @ModelAttribute @RequestBody User user, HttpSession session) {
		if (userService.checkPassword((User) session.getAttribute("user"),oldPassword)) {
			Date date = new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			user.setPasswordUpdateDate(timestamp);
			userService.updatePwd(user);
			session.setAttribute("user", user);
//			return "success";
			//退出
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
			String ssoLogoutUrl = PropertiesUtil.getPropertiesByName("sso.ssoLogoutUrl", "sso");
			return "redirect:"+ssoLogoutUrl;
		} else {
			return "false";
		}

	}
	
	/**
	 * 用户管理页面修改密码跳转
	 */
	@RequestMapping(value = "updatePwd/{id}", method = RequestMethod.GET)
	public String updatePwdForm2(Model model,@PathVariable("id") String id) {
		model.addAttribute("user", userService.get(Integer.valueOf(id)));
		return "system/updatePwdByAdmin";
	}
	
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "updatePwdByAdmin", method = RequestMethod.POST)
	@ResponseBody
	public String updatePwdByAdmin(String oldPassword,@Valid @ModelAttribute @RequestBody User user) {
		if (userService.checkPassword(userService.get(user.getId()),oldPassword)) {
			Date date = new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			user.setPasswordUpdateDate(timestamp);
			userService.updatePwd(user);
			return "success";
		} else {
			return "false";
		}

	}
	
	/**
	 * 重置密码
	 */
	@RequestMapping(value = "resetPwd/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String resetPwd(@PathVariable("id") Integer id) {
		User user = userService.get(id);
		user.setPlainPassword("Ll!@#2024");
		userService.updatePwd(user);
		return "success";

	}
	

	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(String loginName) {
		if (userService.getUser(loginName) == null) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * ajax请求校验原密码是否正确
	 * 
	 * @param oldPassword
	 * @return
	 */
	@RequestMapping(value = "checkPwd")
	@ResponseBody
	public String checkPwd(String oldPassword, HttpSession session) {
		if (userService.checkPassword((User) session.getAttribute("user"),oldPassword)) {
			return "true";
		} else {
			return "false";
		}
	}
	
	
	/**
	 * ajax请求校验原密码是否正确(用户管理页面
	 * 
	 * @param oldPassword
	 * @return
	 */
	@RequestMapping(value = "checkPwd/{id}")
	@ResponseBody
	public String checkPwdByAdmin(String oldPassword,@PathVariable("id") Integer id) {
		if (userService.checkPassword(userService.get(id),oldPassword)) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getUser(@RequestParam(value = "id", defaultValue = "-1") Integer id,Model model) {
		if (id != -1) {
			model.addAttribute("user", userService.get(id));
		}
	}

	/**
	 * 导出excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		List<User> users = userService.search(filters);
		
        ExportParams params = new ExportParams("用户管理信息", "用户管理Sheet", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, User.class, users);
        
        String formatFileName = URLEncoder.encode("用户管理信息" +".xlsx","UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=\"" + formatFileName+"\"");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        OutputStream os = response.getOutputStream();
        workbook.write(os); // 写入文件
		os.close(); // 关闭流
	}
	
	
	/**
	 * 
	 * @author PYL
	 * @Description: 获得所有客服
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getUserAll", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUserAll(HttpServletRequest request,HttpServletResponse response) {
		List<User> objList = new ArrayList<User>();
		List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
		String param = request.getParameter("q");// 搜索值
		String setid = request.getParameter("setid");// 原数据填充值
		if ((param != null && !"".equals(param)) || (setid != null && !"".equals(setid))) {
			if (param != null && !"".equals(param)) {
				userList = userService.findUserByName(param);
			} else {
				// 根据原值id获取对象
				userList = userService.findUserByName(setid);
			}
		}
		
		for(Map<String,Object> obj:userList){
			User user=new User();
			user.setName(obj.get("NAME").toString());
			objList.add(user);
		}
		return objList;
	}
}
