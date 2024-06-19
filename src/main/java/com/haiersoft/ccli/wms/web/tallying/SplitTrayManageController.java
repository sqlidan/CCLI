package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.tallying.SplitTrayManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *拆托
 */
@Controller
@RequestMapping(value = "wms/splitTray")
public class SplitTrayManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SplitTrayManageController.class);
	@Autowired
	private SplitTrayManageService splitTrayService;

	//拆托
	@RequestMapping(value = "splitTray", method = RequestMethod.GET)
	public String splitTray() {
		return "wms/tallying/splitTray";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisPreEntryInvtQuery> page = getPage(request);
		//解析数据
		List<InvtHeadTypeVo> invtHeadTypeVoList = new ArrayList<>();
		//返回数据
		Page<InvtHeadTypeVo> page2 = getPage(request);
		page2.setResult(invtHeadTypeVoList);
		page2.setTotalCount(page.getTotalCount());
		return getEasyUIData(page2);
	}
}
