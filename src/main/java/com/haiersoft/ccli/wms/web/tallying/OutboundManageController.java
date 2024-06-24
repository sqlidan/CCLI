package com.haiersoft.ccli.wms.web.tallying;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.report.entity.Stock;
import com.haiersoft.ccli.report.service.StockReportService;
import com.haiersoft.ccli.wms.entity.PreEntryInvtQuery.BisPreEntryInvtQuery;
import com.haiersoft.ccli.wms.entity.apiEntity.InvtHeadTypeVo;
import com.haiersoft.ccli.wms.service.tallying.OutboundManageService;
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
 *出库拣货
 */
@Controller
@RequestMapping(value = "wms/outbound")
public class OutboundManageController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OutboundManageController.class);
	@Autowired
	private OutboundManageService outboundService;
	@Autowired
	private StockReportService stockReportService;

	//出库拣货
	@RequestMapping(value = "outbound", method = RequestMethod.GET)
	public String outbound() {
		return "wms/tallying/outbound";
	}

	@RequestMapping(value = "json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<Stock> page = getPage(request);
		Stock stock = new Stock();
		parameterReflect.reflectParameter(stock, request);//转换对应实体类参数
		page = stockReportService.searchStockReport(page, stock);
		return getEasyUIData(page);
	}
}
