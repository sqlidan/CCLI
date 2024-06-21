package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.TrayInfoService;
import com.haiersoft.ccli.wms.service.tallying.RelocationManageService;
import com.haiersoft.ccli.wms.service.tallying.SplitTrayManageService;
import com.haiersoft.ccli.wms.service.tallying.WarehouseManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *移库
 */
@Controller
@RequestMapping(value = "wms/relocation")
public class RelocationManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(RelocationManageController.class);
	@Autowired
	private RelocationManageService relocationService;
	@Autowired
	private WarehouseManageService warehouseService;

	//移库
	@RequestMapping(value = "relocation", method = RequestMethod.GET)
	public String relocation() {
		return "wms/tallying/relocation";
	}
	//获取托盘数据
	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<TrayInfo> page = getPage(request);
		page.orderBy("enterStockTime").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		//默认为00 00已收货（理货确认）01已上架（库管确认）10出库中（装车单）11出库理货（库管确认）12已出库（理货确认）20待回库 21回库收货（理货确认）  99报损货物状态
		PropertyFilter filter = new PropertyFilter("NEQS_cargoState", "01");
		filters.add(filter);
		page = relocationService.search(page, filters);
		return getEasyUIData(page);
	}

	@Autowired
	TrayInfoService trayInfoService;
	//拆托
	@RequestMapping(value = "moveTray/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") Integer id) {
		TrayInfo trayInfo = relocationService.get(id);
		model.addAttribute("trayInfo", trayInfo);
		model.addAttribute("action", "moveTraySave");
		return "wms/tallying/relocationInfo";
	}
	@RequestMapping(value = "moveTraySave", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid TrayInfo trayInfo, Model model, HttpServletRequest request) {
		User user = UserUtil.getCurrentUser();
		//修改原托盘信息
		String result = relocationService.moveStorageConfirm(trayInfo.getTrayId(),trayInfo.getCargoLocation(), user.getName());
		if (!result.contains("操作成功")){
			return result;
		}
		return "success";
	}
}
