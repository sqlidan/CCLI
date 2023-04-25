package com.haiersoft.ccli.wms.web;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.parameterReflect;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.Monitor;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.MonitorService;

/**
 * 
 * @author pyl
 * @ClassName: monitorController
 * @Description: 作业监控Control1ler
 * @date 2016年3月1日 下午5:05:33
 */
@Controller
@RequestMapping("wms/monitor")
public class MonitorController extends BaseController{
	
	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ExpenseSchemeInfoService expenseSchemeInfoService;
	
	/**
	 * 入库作业监控默认页面
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public String list() {
		return "wms/monitor/enterMonitor";
	}
	
	/**
	 * 出库作业监控默认页面
	 */
	@RequestMapping(value="list2", method = RequestMethod.GET)
	public String manager(Model model) {
		return "wms/monitor/outMonitor";
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * @author pyl
	 * @Description: 入库作业监控查询
	 * @date 2017年4月27日 下午16:00:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="enterJson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getData(HttpServletRequest request) {
		Page<Monitor> page = getPage(request);
		Monitor monitor = new Monitor();
		parameterReflect.reflectParameter(monitor, request);//转换对应实体类参数
		if(null==monitor.getCcTimeS() && null==monitor.getCcTimeE()){
			String aa = request.getParameter("ccTimeS");
			Date bb = DateUtils.parseDate(aa);
			monitor.setCcTimeS(bb);
			String cc = request.getParameter("ccTimeE");
			Date dd = DateUtils.addDay(DateUtils.parseDate(cc), 1) ;
			monitor.setCcTimeE(dd);
		}else{
			if(null!=monitor.getCcTimeE()){
				monitor.setCcTimeE(DateUtils.addDay(monitor.getCcTimeE(),1));
			}
		}
		page = monitorService.searchMonitor(page, monitor);
		return getEasyUIData(page);
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * @author pyl
	 * @Description: 出库作业监控查询
	 * @date 2017年4月27日 下午16:00:10 
	 * @return
	 * @throws
	 */
	@RequestMapping(value="outJson", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getOutData(HttpServletRequest request) {
		Page<Monitor> page = getPage(request);
		Monitor monitor = new Monitor();
		parameterReflect.reflectParameter(monitor, request);//转换对应实体类参数
		if(null==monitor.getCcTimeS() && null==monitor.getCcTimeE()){
			String aa = request.getParameter("ccTimeS");
			Date bb = DateUtils.parseDate(aa);
			monitor.setCcTimeS(bb);
			
			String cc = request.getParameter("ccTimeE");
			Date dd = DateUtils.addDay(DateUtils.parseDate(cc), 1) ;
			monitor.setCcTimeE(dd);
		}else{
			if(null!=monitor.getCcTimeE()){
				monitor.setCcTimeE(DateUtils.addDay(monitor.getCcTimeE(),1));
			}
		}
		page = monitorService.searchOutMonitor(page, monitor);
		return getEasyUIData(page);
	}
	
	
	
	/**
	 * 获取客户字典
	 */
	@RequestMapping(value="selClient",method = RequestMethod.GET)
	@ResponseBody
	public List<BaseClientInfo> selClient(HttpServletRequest request) {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		PropertyFilter filter = new PropertyFilter("EQI_delFlag", String.valueOf(0));
		filters.add(filter);
		PropertyFilter filter2 = new PropertyFilter("EQS_clientSort", String.valueOf(0));
		filters.add(filter2);
		List<BaseClientInfo> clientInfo = clientService.search(filters);
		return clientInfo;
	}


	
	
	
	/**
	 * Ajax请求校验提单号是否唯一。
	 */
	@RequestMapping(value = "checkItemNum/{linkId}")
	@ResponseBody
	public String checkItemNum(@PathVariable("linkId") String linkId,String itemNum) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("itemNum", itemNum);
		params.put("delFlag", "0");
		List<BisEnterStock> enterStock = enterStockService.findEnterList(params);
		if(itemNum.contains(",")||itemNum.contains("，")){
			return "false";
		}else{
			if (enterStock.isEmpty()) {
				return "true";
			} else {
				if(enterStock.get(0).getLinkId().equals(linkId)){
					return "true";
				}else{
					return "false";
				}
			}
		}
	}
	
	/**
	 * @Description: 通过费用ID获得费用方案名称
	 */
	public String getFeeName(String id){
		ExpenseSchemeInfo expenseSchemeInfo = expenseSchemeInfoService.get(id);
		String schemeName = expenseSchemeInfo.getSchemeNum();
		return schemeName;
	}
	
}
