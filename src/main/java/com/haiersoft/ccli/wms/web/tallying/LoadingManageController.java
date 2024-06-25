package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.remoting.hand.out.service.OutboundWebService;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.tallying.LoadingManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Map;

/**
 *装车理货
 */
@Controller
@RequestMapping(value = "wms/loading")
public class LoadingManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoadingManageController.class);
	@Autowired
	private LoadingManageService loadingService;
	@Autowired
	private LoadingInfoService loadingInfoService;

	//装车理货
	@RequestMapping(value = "loading", method = RequestMethod.GET)
	public String loading() {
		return "wms/tallying/loading";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<BisLoadingInfo> page = getPage(request);
		page.orderBy("id").order(Page.DESC);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		page = loadingInfoService.search(page, filters);
		return getEasyUIData(page);
	}

	@RequestMapping(value = "loading/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") Integer id) {
		BisLoadingInfo bisLoadingInfo = loadingInfoService.get(id);
		model.addAttribute("trayInfo", bisLoadingInfo);
		model.addAttribute("action", "loadingSave");
		return "wms/tallying/loadingInfo";
	}
	@RequestMapping(value="loadingSave",method = RequestMethod.GET)
	@ResponseBody
	public String outboundSave(@Valid BisLoadingInfo bisLoadingInfo, Model model, HttpServletRequest request) {
		User user = UserUtil.getCurrentUser();
		String result = loadingService.outStorageLoadingCar(bisLoadingInfo.getLoadingTruckNum(),bisLoadingInfo.getTrayId(),bisLoadingInfo.getPlatformNum(),bisLoadingInfo.getCarNo(),user.getName());
		if (!result.contains("操作成功")){
			return result;
		}
		return "success";
	}
}
