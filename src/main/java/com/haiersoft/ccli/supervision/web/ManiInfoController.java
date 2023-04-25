package com.haiersoft.ccli.supervision.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.entity.ManiInfo;
import com.haiersoft.ccli.supervision.service.ManiInfoService;


/**
 * 分类监管 核放单行controller
 * @author 
 *
 */

@Controller
@RequestMapping("supervision/maniinfo")
public class ManiInfoController extends BaseController{
    
	@Autowired
	private  ManiInfoService maniInfoService;

	
	@RequestMapping(value="json/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request,@PathVariable("id") String id) {
		Page<ManiInfo> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQS_headId", id);
		filters.add(filter);
		page = maniInfoService.search(page, filters);		
		return getEasyUIData(page);
	}
}
