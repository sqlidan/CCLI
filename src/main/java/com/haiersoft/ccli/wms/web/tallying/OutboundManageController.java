package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.remoting.hand.goBack.service.GoBackStockWebService;
import com.haiersoft.ccli.remoting.hand.out.service.OutboundWebService;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.tallying.OutboundManageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *出库拣货
 */
@Controller
@RequestMapping(value = "wms/outbound")
public class OutboundManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OutboundManageController.class);
	@Autowired
	private OutboundManageService outboundService;
	@Autowired
	private LoadingInfoService loadingInfoService;
	@Autowired
	private OutboundWebService outboundWebService;
	@Autowired
	private GoBackStockWebService goBackStockWebService;

	//出库拣货
	@RequestMapping(value = "outbound", method = RequestMethod.GET)
	public String outbound() {
		return "wms/tallying/outbound";
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

	@RequestMapping(value = "outbound/{id}", method = RequestMethod.GET)
	public String updateContractForm(Model model, @PathVariable("id") Integer id) {
		BisLoadingInfo bisLoadingInfo = loadingInfoService.get(id);
		model.addAttribute("trayInfo", bisLoadingInfo);
		model.addAttribute("action", "outboundSave");
		return "wms/tallying/outboundInfo";
	}
	@RequestMapping(value="outboundSave",method = RequestMethod.GET)
	@ResponseBody
	public String outboundSave(@Valid BisLoadingInfo bisLoadingInfo, Model model, HttpServletRequest request) {
		User user = UserUtil.getCurrentUser();
		//依据装车单号获取全部的装车单
		List<BisLoadingInfo> loadings = loadingInfoService.getLoadingByNum(bisLoadingInfo.getLoadingTruckNum());
		//判断扫描的托盘号是否存在
		if (null != loadings && loadings.size() > 0) {
			for (BisLoadingInfo forBisLoadingInfo:loadings) {
//				String result = outboundWebService.outSortingConfirm(bisLoadingInfo.getLoadingTruckNum(),forBisLoadingInfo.getTrayId(),forBisLoadingInfo.getChangeTrayId(),user.getName());
				String result = outboundWebService.outSortingConfirm(bisLoadingInfo.getLoadingTruckNum(),forBisLoadingInfo.getTrayId(),forBisLoadingInfo.getTrayId(),user.getName());
				if (!result.contains("操作成功")){
					return result;
				}
			}
		}
		return "success";
	}

	//跳转回库上架弹窗
	@RequestMapping(value = "backUp/{id}", method = RequestMethod.GET)
	public String backUp(Model model, @PathVariable("id") Integer id) {
		BisLoadingInfo bisLoadingInfo = loadingInfoService.get(id);
		model.addAttribute("trayInfo", bisLoadingInfo);
		model.addAttribute("action", "backUpSave");
		return "wms/tallying/outboundBackUpInfo";
	}

	@RequestMapping(value="backUpSave",method = RequestMethod.GET)
	@ResponseBody
	public String backUpSave(@Valid BisLoadingInfo bisLoadingInfo, Model model, HttpServletRequest request) throws ParseException {
		User user = UserUtil.getCurrentUser();
		String result = goBackStockWebService.getBackStockUpConfirm(bisLoadingInfo.getTrayId(),bisLoadingInfo.getCargoLocation(),user.getName());
		if (!result.contains("操作成功")){
			return result;
		}
		return "success";
	}
}
