package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.service.ReservationInboundService;
import com.haiersoft.ccli.platform.service.ReservationOutboundService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.*;
import com.haiersoft.ccli.wms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

/**
 * 生成预约信息Controller
 */
@Controller
@RequestMapping("platform/reservationData")
public class PlatformReservationDataController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(PlatformReservationDataController.class);
	@Autowired
	private ReservationInboundService reservationInboundService;
	@Autowired
	private ASNService asnService;
	@Autowired
	private ASNInfoService asnInfoService;
	@Autowired
	private EnterStockService enterStockService;
	@Autowired
	private EnterStockInfoService enterStockInfoService;

	/**
	 * 跳转修改预约入库记录信息
	 */
	@RequestMapping(value = "updateInbound/{id}", method = RequestMethod.GET)
	public String updateInbound(@PathVariable("id") String id, Model model) {
		model.addAttribute("reservationData", reservationInboundService.get(id));
		model.addAttribute("action", "updateInbound");
		return "platform/reservation/inboundedit";
	}

	/**
	 * 修改预约入库记录信息
	 */
	@RequestMapping(value = "updateInbound", method = RequestMethod.POST)
	@ResponseBody
	public String updateInbound(@Valid @ModelAttribute @RequestBody PlatformReservationInbound platformReservationInbound, Model model) {
		PlatformReservationInbound updatelatformReservationInbound = reservationInboundService.get(platformReservationInbound.getId());
		updatelatformReservationInbound.setCarNumber(platformReservationInbound.getCarNumber());
		updatelatformReservationInbound.setDriverMobile(platformReservationInbound.getDriverMobile());
		updatelatformReservationInbound.setUpdatedTime(new Date());
		reservationInboundService.update(updatelatformReservationInbound);
		return "success";
	}

	/**
	 * 删除预约入库记录信息
	 */
	@RequestMapping(value = "deleteInbound/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteInbound(@PathVariable("id") String id, Model model) {
		reservationInboundService.delete(id);
		return "success";
	}


	/**
	 * 入库联系单生成预约入库记录
	 */
	@RequestMapping(value = "/inbound/{asn}", method = RequestMethod.POST)
	@ResponseBody
	public String inbound(@PathVariable("asn") String asn) {
		User user = UserUtil.getCurrentUser();
		List<PropertyFilter> filtersOrder = new ArrayList<>();
		filtersOrder.add(new PropertyFilter("EQS_asn", asn));
		List<BisAsn> bisAsnList = asnService.search(filtersOrder);
		if (bisAsnList != null && bisAsnList.size() == 1) {
			//订单信息
			BisAsn bisAsn = bisAsnList.get(0);
			List<PropertyFilter> filtersOrder2 = new ArrayList<>();
			filtersOrder2.add(new PropertyFilter("EQS_linkId", bisAsn.getLinkId()));
			List<BisEnterStock> bisEnterStockList = enterStockService.search(filtersOrder2);
			BisEnterStock bisEnterStock = new BisEnterStock();
			if (bisEnterStockList != null && bisEnterStockList.size() == 1) {
				bisEnterStock = bisEnterStockList.get(0);
			}
			//订单明细信息
			List<PropertyFilter> filtersOrderInfo = new ArrayList<>();
			filtersOrderInfo.add(new PropertyFilter("EQS_asnId", asn));
			List<BisAsnInfo> bisAsnInfoList = asnInfoService.search(filtersOrderInfo);
			if (bisAsnInfoList != null && bisAsnInfoList.size() > 0) {
				for (BisAsnInfo forBisAsnInfo : bisAsnInfoList) {
					PlatformReservationInbound insertPlatformReservationInbound = new PlatformReservationInbound();
					Random random = new Random();
					int randomNumber = random.nextInt(1000000) + 1; // 生成一个[1, 999999]之间的随机数
					insertPlatformReservationInbound.setId(System.currentTimeMillis()+""+String.format("%06d", randomNumber));
					insertPlatformReservationInbound.setCustomerService(bisAsn.getCreateUser()==null?user.getName():bisAsn.getCreateUser());//客服
					insertPlatformReservationInbound.setConsumeCompany(bisAsn.getStockIn());//客户名称
					insertPlatformReservationInbound.setBillNo(bisAsn.getBillNum());//提单号
					insertPlatformReservationInbound.setStatus("0");//状态 0：已保存，1：已入闸，2：已出闸；
					insertPlatformReservationInbound.setContainerNo(bisAsn.getCtnNum());//箱号
					insertPlatformReservationInbound.setQueuingTime(new Date());//排队时间
					insertPlatformReservationInbound.setProductName(forBisAsnInfo.getCargoName());//品名
					insertPlatformReservationInbound.setProductType("3");//货类  1 水产 2 肉类 3 其他
					insertPlatformReservationInbound.setNum(forBisAsnInfo.getPiece().toString());//件数
					insertPlatformReservationInbound.setNetWeight(forBisAsnInfo.getNetWeight());//总净重
					insertPlatformReservationInbound.setCarNumber("");//车牌号
					insertPlatformReservationInbound.setDriverMobile("");//手机号
					insertPlatformReservationInbound.setAppointDate(new Date());//预约时间
					insertPlatformReservationInbound.setStorageTemperature(bisEnterStock.getTemperature());//存放温度
					insertPlatformReservationInbound.setCheckInstructions(bisEnterStock.getIfCheck());//是否查验
					insertPlatformReservationInbound.setIsFreetax(bisEnterStock.getIfBonded());//是否保税
					insertPlatformReservationInbound.setIsZdmt(bisEnterStock.getIfWithWooden());//是否带木托
					insertPlatformReservationInbound.setSealNo("");//铅封号
					insertPlatformReservationInbound.setOriginCountry(forBisAsnInfo.getProducingArea());//原产国(产地)
					insertPlatformReservationInbound.setFactoryNo(forBisAsnInfo.getRkNum());//厂号
					insertPlatformReservationInbound.setReportNumber(bisEnterStock.getBgdh());//报关单号
					insertPlatformReservationInbound.setCreatedTime(new Date());//预约日期
					insertPlatformReservationInbound.setType("1");//区分同步和系统生成
					reservationInboundService.save(insertPlatformReservationInbound);
				}
			}
		}else{
			return "error";
		}
		return "success";
	}

	@Autowired
	private ReservationOutboundService reservationOutboundService;
	@Autowired
	private LoadingOrderService loadingOrderService;
	@Autowired
	private LoadingOrderInfoService loadingOrderInfoService;

	/**
	 * 跳转修改预约出库记录信息
	 */
	@RequestMapping(value = "updateOutbound/{id}", method = RequestMethod.GET)
	public String updateOutbound(@PathVariable("id") String id, Model model) {
		model.addAttribute("reservationData", reservationOutboundService.get(id));
		model.addAttribute("action", "updateOutbound");
		return "platform/reservation/outboundedit";
	}

	/**
	 * 修改预约出库记录信息
	 */
	@RequestMapping(value = "updateOutbound", method = RequestMethod.POST)
	@ResponseBody
	public String updateOutbound(@Valid @ModelAttribute @RequestBody PlatformReservationOutbound platformReservationOutbound, Model model) {
		PlatformReservationOutbound updatePlatformReservationOutbound = reservationOutboundService.get(platformReservationOutbound.getId());
		updatePlatformReservationOutbound.setCarNumber(platformReservationOutbound.getCarNumber());
		updatePlatformReservationOutbound.setDriverMobile(platformReservationOutbound.getDriverMobile());
		updatePlatformReservationOutbound.setUpdatedTime(new Date());
		reservationOutboundService.update(updatePlatformReservationOutbound);
		return "success";
	}

	/**
	 * 删除预约出库记录信息
	 */
	@RequestMapping(value = "deleteOutbound/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteOutbound(@PathVariable("id") String id, Model model) {
		reservationOutboundService.delete(id);
		return "success";
	}

	/**
	 * 出库订单生成预约出库记录
	 */
	@RequestMapping(value = "/outbound/{orderNum}", method = RequestMethod.POST)
	@ResponseBody
	public String outbound(@PathVariable("orderNum") String orderNum) {
		User user = UserUtil.getCurrentUser();
		List<PropertyFilter> filtersOrder = new ArrayList<>();
		filtersOrder.add(new PropertyFilter("EQS_orderNum", orderNum));
		List<BisLoadingOrder> bisLoadingOrderList = loadingOrderService.search(filtersOrder);
		if (bisLoadingOrderList!=null && bisLoadingOrderList.size() ==1){
			//订单信息
			BisLoadingOrder bisLoadingOrder = bisLoadingOrderList.get(0);
			//订单明细信息
			List<PropertyFilter> filtersOrderInfo = new ArrayList<>();
			filtersOrderInfo.add(new PropertyFilter("EQS_loadingPlanNum", orderNum));
			List<BisLoadingOrderInfo> bisLoadingOrderInfoList = loadingOrderInfoService.search(filtersOrderInfo);
			if (bisLoadingOrderInfoList!=null && bisLoadingOrderInfoList.size() > 0){
				for (BisLoadingOrderInfo forBisLoadingOrderInfo:bisLoadingOrderInfoList) {
					PlatformReservationOutbound insertPlatformReservationOutbound = new PlatformReservationOutbound();
					Random random = new Random();
					int randomNumber = random.nextInt(1000000) + 1; // 生成一个[1, 999999]之间的随机数
					insertPlatformReservationOutbound.setId(System.currentTimeMillis()+""+String.format("%06d", randomNumber));
					insertPlatformReservationOutbound.setYyid("100000"+(new Date()).getTime());//预约id
					insertPlatformReservationOutbound.setCustomerService(bisLoadingOrder.getCreatePerson()==null?user.getName():bisLoadingOrder.getCreatePerson());//客服
					insertPlatformReservationOutbound.setAppointDate(new Date());//预约出库日期
					insertPlatformReservationOutbound.setConsumeCompany(bisLoadingOrder.getStockName());//客户名称
					insertPlatformReservationOutbound.setBillNo(forBisLoadingOrderInfo.getBillNum());//提单号
					insertPlatformReservationOutbound.setContainerNo(forBisLoadingOrderInfo.getCtnNum());//箱号
					insertPlatformReservationOutbound.setOriginCountry("");//原产国
					insertPlatformReservationOutbound.setProductType("3");//货类  1 水产 2 肉类 3 其他
					insertPlatformReservationOutbound.setProductName(forBisLoadingOrderInfo.getCatgoName());//品名
					insertPlatformReservationOutbound.setStatus("0");//状态 0：已保存，1：已入闸，2：已出闸；
					insertPlatformReservationOutbound.setNum(forBisLoadingOrderInfo.getPiece().toString());//件数
					insertPlatformReservationOutbound.setWeight(forBisLoadingOrderInfo.getPiece()*forBisLoadingOrderInfo.getNetWeight());//重量
					insertPlatformReservationOutbound.setQueuingTime(new Date());//排队时间
					insertPlatformReservationOutbound.setCarNumber(bisLoadingOrder.getCarNum());//车牌号
					insertPlatformReservationOutbound.setDriverMobile("");//手机号
					insertPlatformReservationOutbound.setLocationNo("");//库号
					insertPlatformReservationOutbound.setRoomNum("");//房间号
					insertPlatformReservationOutbound.setDeletedFlag(0);
					insertPlatformReservationOutbound.setCreatedTime(new Date());
					insertPlatformReservationOutbound.setIsBulkCargo(null);//是否散货
					insertPlatformReservationOutbound.setType("1");//区分同步和系统生成
					reservationOutboundService.save(insertPlatformReservationOutbound);
				}
				return "success";
			}
			return "当前订单没有明细信息";
		}else{
			return "未找到对应的出库订单信息";
		}
	}

}
