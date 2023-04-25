package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.entity.BaseTrayRoom;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.GroupManage;
import com.haiersoft.ccli.platform.service.BaseTrayRoomService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 虚拟库controller
 * @author 
 */
@Controller
@RequestMapping("platform/base/trayroom")
public class BaseTrayRoomController extends BaseController{
	
	@Autowired
	private BaseTrayRoomService baseTrayRoomService ;



	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String list() {
		return "platform/base/trayRoomList";
	}


	
	@RequestMapping(value="/json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BaseTrayRoom> page = getPage(request);
		//page.orderBy("operateTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = baseTrayRoomService.search(page, filters);
		return getEasyUIData(page);
	}

}
