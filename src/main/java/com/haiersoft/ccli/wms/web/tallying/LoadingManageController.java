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
	@Autowired
	private OutboundWebService outboundWebService;

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
		//依据装车单号获取全部的装车单
		List<BisLoadingInfo> loadings = loadingInfoService.getLoadingByNum(bisLoadingInfo.getLoadingTruckNum());
		if (null != loadings && loadings.size() > 0) {
			List<String> cargoNameList = new ArrayList<>();
			Integer piece = 0;
			for (BisLoadingInfo forBisLoadingInfo:loadings) {
				if (!cargoNameList.contains(forBisLoadingInfo.getCargoName())){
					cargoNameList.add(forBisLoadingInfo.getCargoName());
				}
				piece += forBisLoadingInfo.getPiece();
			}
			bisLoadingInfo.setCargoName(cargoNameList.toString());
			bisLoadingInfo.setPiece(piece);
		}
		model.addAttribute("trayInfo", bisLoadingInfo);
		model.addAttribute("action", "loadingSave");
		return "wms/tallying/loadingInfo";
	}
	@RequestMapping(value="loadingSave",method = RequestMethod.GET)
	@ResponseBody
	public String outboundSave(@Valid BisLoadingInfo bisLoadingInfo, Model model, HttpServletRequest request) {
		User user = UserUtil.getCurrentUser();
		//依据装车单号获取全部的装车单
		List<BisLoadingInfo> loadings = loadingInfoService.getLoadingByNum(bisLoadingInfo.getLoadingTruckNum());
		//判断扫描的托盘号是否存在
		if (null != loadings && loadings.size() > 0) {
			for (BisLoadingInfo forBisLoadingInfo:loadings) {
				String result = loadingService.outStorageLoadingCar(bisLoadingInfo.getLoadingTruckNum(),forBisLoadingInfo.getTrayId(),bisLoadingInfo.getPlatformNum(),bisLoadingInfo.getCarNo(),user.getName());
				if (!result.contains("操作成功")){
					return result;
				}
			}
		}
		return "success";
	}

	//回库
	@RequestMapping(value = "back/{ids}", method = RequestMethod.GET)
	@ResponseBody
	public String back(Model model, @PathVariable("ids") List<Integer> ids) {
		User user = UserUtil.getCurrentUser();
		//判断扫描的托盘号是否存在
		if (null != ids && ids.size() > 0) {
			for (Integer id:ids) {
				BisLoadingInfo bisLoadingInfo = loadingInfoService.get(id);
				String result = outboundWebService.comeBackStockConfirm(bisLoadingInfo.getLoadingTruckNum(),user.getName());
				if (!result.contains("操作成功")){
					return result;
				}
			}
		}
		return "success";
	}
}
