package com.haiersoft.ccli.platform.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.platform.service.ReservationInboundService;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 预约Controller
 *
 * @author
 * @date
 */
@Controller
@RequestMapping("platform/reservation/inbound")
public class PlatformReservationController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(PlatformReservationController.class);
	@Autowired
	private ReservationInboundService reservationInboundService;
	@Autowired
	private HttpUtil httpUtil;

	/**
	 * 预约入库记录
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {

		return "platform/reservation/inbound";
	}

	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> selectList(HttpServletRequest request) {
		Page<PlatformReservationInbound> page = getPage(request);
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);


		boolean appointDateHave = false;
		for (PropertyFilter propertyFilter : filters) {

			if (propertyFilter.getPropertyName().equals("appointDate")) {
				appointDateHave = true;
				break;
			}
		}
		//筛选条件没有日期  默认为今天
		if (!appointDateHave) {
			Calendar start = Calendar.getInstance();
			//结束时间
			Calendar end = Calendar.getInstance();
			start.add(Calendar.DATE, 0);
			// 时
			start.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			start.set(Calendar.MINUTE, 0);
			// 秒
			start.set(Calendar.SECOND, 0);

			start.set(Calendar.MILLISECOND, 0);


			end.add(Calendar.DATE, 30);
			// 时
			end.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			end.set(Calendar.MINUTE, 0);
			// 秒
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);

			filters.add(new PropertyFilter("GED_appointDate", start.getTime()));
			filters.add(new PropertyFilter("LTD_appointDate", end.getTime()));
		}

		//page.orderBy("appointDate").order("desc");
		//page.orderBy("status,appointDate,queuingTime").order("asc,desc,asc").setOrderNulls("false,false,true");
		page.orderBy("appointDate,queuingTime").order("desc,asc").setOrderNulls("false,true");

		//page.setNullsLast(true);
		page = reservationInboundService.search(page, filters);
		return getEasyUIData(page);
	}


	//车辆取消靠口
	@RequestMapping(value = "cancel/{id}")
	@ResponseBody
	public String cancel(@PathVariable("id") String id) {
		if (id == null && id.equals("")) {
			return "error";
		}
		PlatformReservationInbound platformReservationInbound = reservationInboundService.get(id);
		//取消
		platformReservationInbound.setStatus("3");
		reservationInboundService.save(platformReservationInbound);
		User user = UserUtil.getCurrentUser();

		//取消靠口
		Map<String,String> sendMap = new HashMap<>();

		sendMap.put("operator",user.getName());
		sendMap.put("inoutBoundFlag","1");
		sendMap.put("yyid",platformReservationInbound.getYyid());

		try{
			httpUtil.doPost(PlatformConsts.PLATFORM_URL_CANCEL_RESERVATION_NO_GATEIN, JSON.toJSONString(sendMap));
		}catch (HttpHostConnectException ex){
			log.info(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
