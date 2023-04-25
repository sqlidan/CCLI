package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.GroupManage;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.platform.service.GroupManageService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 */
@Controller
@RequestMapping("platform/group/manage")
public class GroupManageController extends BaseController{
	
	@Autowired
	private GroupManageService groupManageService ;



	
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "platform/groupManage";
	}
	

/*	@RequestMapping(value="manager", method = RequestMethod.GET)
	public String manager(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("user", user.getName());
		model.addAttribute("date", new Date());
		model.addAttribute("action", "create");
		return "platform/groupManageInfo";
	}
  
	*/

	

	
	@RequestMapping(value="json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
/*		Page<GroupManage> page = getPage(request);
//		page.orderBy("id").order(Page.DESC); 
		GroupManage customsClearance = new GroupManage();
        parameterReflect.reflectParameter(customsClearance, request);
//		Map<String, Object> map=PropertyFilter.buildFromHttpRequestMap(request);
		page = groupManageService.seachCustomsClearanceSql(page, customsClearance);*/

		Page<GroupManage> page=getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

		page = groupManageService.search(page,filters);
		return getEasyUIData(page);

	}

	
	/**
	 * @param model
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model) {
		User user = UserUtil.getCurrentUser();
		model.addAttribute("groupManage", new GroupManage());
		model.addAttribute("date", new Date());
		model.addAttribute("user",user.getName());
		model.addAttribute("action", "create");
		return "platform/groupManageInfo";
	}
	
	
	/**
	 * 新增保存报关单
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response) {
		GroupManage groupManage = new GroupManage();
		parameterReflect.reflectParameter(groupManage, request);//转换对应实体类参数
		User user = UserUtil.getCurrentUser();
		groupManage.setOperator(user.getName());
		groupManage.setCreatedTime(new Date());
		try {
			groupManageService.save(groupManage);
		}
		catch(DataIntegrityViolationException ex){
			return "duplicate";
		}
		
		return "success";
	}
	
	//修改跳转
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") Integer id) {
		GroupManage groupManage = groupManageService.get(id);
		model.addAttribute("groupManage", groupManage);
		model.addAttribute("action", "update");
		return "platform/groupManageInfo";
	}
	
	/**
	 * 修改保存报关单
	 * @throws ParseException 
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String operateTime = request.getParameter("operateTime");
		Date date = simpleDateFormat.parse(operateTime);*/
		GroupManage groupManage = new GroupManage();
		parameterReflect.reflectParameter(groupManage, request);//转换对应实体类参数
		//已审核的不能修改

		groupManage.setUpdatedTime(new Date());
		//groupManage.setCreateTime(date);
		groupManageService.update(groupManage);
		return "success";
	}



	



	
	//删除
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id") Integer id) {
		if(id==null&&id.equals("")) {
			return "error";
		}

		groupManageService.delete(id);

		return "success";
		
	}
	



	
}
