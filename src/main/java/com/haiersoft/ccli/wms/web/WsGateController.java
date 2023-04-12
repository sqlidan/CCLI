package com.haiersoft.ccli.wms.web;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import com.haiersoft.ccli.wms.entity.BisGateCarParams;
import com.haiersoft.ccli.wms.entity.BisGateCarWrongInfo;
import com.haiersoft.ccli.wms.entity.BisInOut;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisPresenceBox;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.CarInfoToQueue;
import com.haiersoft.ccli.wms.service.GateCarService;
import com.haiersoft.ccli.wms.service.GateCarWrongInfoService;
import com.haiersoft.ccli.wms.service.InOutService;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.PresenceBoxService;

/**
 * 闸口管理
 */
@Controller
@RequestMapping("wsgate/wms/gate")
public class WsGateController extends BaseController {

	@Autowired
	private InOutService inOutService;

	@Autowired
	private PresenceBoxService presenceBoxService;

	@Autowired
	private GateCarService gateCarService;
	@Autowired
	private GateCarWrongInfoService gateCarWrongInfoService;

	@Autowired
	private ASNService asnService;
	@Autowired
	private ASNInfoService asnInfoService;
	@Autowired
	private LoadingInfoService loadingInfoService;
	@Autowired
	private LoadingOrderService loadingOrderService;
	@Autowired
	private LoadingOrderInfoService loadingOrderInfoService;
	@Autowired
	private CarInfoToQueue carInfoToQueue;

	/**
	 * 对外接口：直接调用并将数据存入数据库
	 */
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> mergeForm1(HttpServletRequest request) {
		String carNo = request.getParameter("carNum");
		// 车牌号为空，直接return null
		if (null == carNo) {
			return null;
		}
		String ctnNum1 = request.getParameter("ctnNum1");
		String ctnNum2 = request.getParameter("ctnNum2");

		BisGateCarParams bisGateCarParams = new BisGateCarParams();
		bisGateCarParams.setCarNum(carNo);
		List<String> list = new ArrayList();
		if (null != ctnNum1) {
			list.add(ctnNum1);
		}
		if (null != ctnNum2) {
			list.add(ctnNum2);
		}
		bisGateCarParams.setCtnNum(list);

		// 判断进出场记录表中是否有重复车牌号
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		PropertyFilter filter = new PropertyFilter("EQS_carNum", bisGateCarParams.getCarNum());
		filters.add(filter);
		List<BisInOut> inOutList = inOutService.search(filters);

		Map<String, Object> resultMap = new HashMap();

		for (String ctnNum : bisGateCarParams.getCtnNum()) {
			if (ctnNum != null) {
				BisGateCar bisGateCar = new BisGateCar();
				Map<String, Object> searchBises1 = searchBis1(bisGateCarParams.getCarNum(), ctnNum);
				// 判断在场箱表中是否有重复箱号
				// String ctnNo=gateEntity.getCtnNum();
				List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
				PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", ctnNum);
				filters2.add(filter2);
				List<BisPresenceBox> boxList = presenceBoxService.search(filters2);

				bisGateCar = getBisGateCar(searchBises1, bisGateCar);
				setProperties(bisGateCarParams.getCarNum(), ctnNum, inOutList, boxList, resultMap, bisGateCar);
			}
		}
		//参数只有车牌号
		if(bisGateCarParams.getCtnNum() == null || bisGateCarParams.getCtnNum().size() <= 0) {
			BisGateCar bisGateCar = new BisGateCar();
				Map<String, Object> searchBises1 = searchBis1(bisGateCarParams.getCarNum(), null);
				
				// 判断在场箱表中是否有重复箱号
				// String ctnNo=gateEntity.getCtnNum();
				List<PropertyFilter> filters2 = new ArrayList<PropertyFilter>();
				PropertyFilter filter2 = new PropertyFilter("EQS_ctnNum", null);
				filters2.add(filter2);
				List<BisPresenceBox> boxList = presenceBoxService.search(filters2);				
				bisGateCar = getBisGateCar(searchBises1, bisGateCar);
				setProperties(bisGateCarParams.getCarNum(), null, inOutList, boxList, resultMap, bisGateCar);

		}
		
		return resultMap;
	}

	public void setProperties(String carNo, String ctnNo, List<BisInOut> inOutList, List<BisPresenceBox> boxList,
			Map<String, Object> resultMap, BisGateCar bisGateCar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BisGateCarWrongInfo wrongInfo = new BisGateCarWrongInfo();
		Date now = DateUtils.getNow();
		String date = sdf.format(now);
		wrongInfo.setCreateDate(date);
		wrongInfo.setCarNum(carNo);
		if(ctnNo != null) {
			wrongInfo.setCtnNum(ctnNo);
		}

		if (!inOutList.isEmpty()) {
			resultMap.put("code", 0);
			resultMap.put("success", true);
			resultMap.put("msg", "success");

			wrongInfo.setReason("overCar");
			gateCarWrongInfoService.save(wrongInfo);
			return;
		}

		if (!boxList.isEmpty()) {
			resultMap.put("code", 0);
			resultMap.put("success", true);
			resultMap.put("msg", "success");

			wrongInfo.setReason("overBox");
			gateCarWrongInfoService.save(wrongInfo);
			return;
		}

		// 车辆信息
		bisGateCar.setCreateDate(now);
		bisGateCar.setCreateUser("接口调用");
		bisGateCar.setJobType("1");
		if(ctnNo != null) {
			bisGateCar.setCtnNum(ctnNo);
		}
		bisGateCar.setCarNum(carNo);
		bisGateCar.setCtnSize("40");
		bisGateCar.setCtnType("RF");

		// 进出场信息
		BisInOut inOut = new BisInOut();
		BeanUtils.copyProperties(bisGateCar, inOut);// 复制
		// 在场箱信息
		BisPresenceBox presenceBox = new BisPresenceBox();
		BeanUtils.copyProperties(bisGateCar, presenceBox);// 复制
		presenceBox.setState("1");

		gateCarService.save(bisGateCar);
		inOutService.save(inOut);
		presenceBoxService.save(presenceBox);
		
		String bisType = bisGateCar.getBisType();
		try {
			if (bisType != null) {
				if (bisType.equals("1")) {
					carInfoToQueue.send(carNo, ctnNo, "M01G01");
				}
				if (bisType.equals("2")) {
					// 出库
					carInfoToQueue.send(carNo, ctnNo, "M01G02");
				}
				//业务类型为 3无作业任务4特殊作业 时，不调用叫号机接口
				if (bisType.equals("3")) {
					//carInfoToQueue.send(carNo, ctnNo, "M01G01");
				}
				if (bisType.equals("4")) {
					//carInfoToQueue.send(carNo, ctnNo, "M01G01");
				}
			}
		} catch (RemoteException | ServiceException e) {
			e.printStackTrace();
		}
		resultMap.put("code", 0);
		resultMap.put("success", true);
		resultMap.put("msg", "success");
	}

	public BisGateCar getBisGateCar(Map<String, Object> searchBises, BisGateCar bisGateCar) {

		// 取出查询的值
		for (String key : searchBises.keySet()) {
			Object value = searchBises.get(key);
			if ("empty".equals(searchBises.get("str"))) {
				bisGateCar.setBisType("3");
			}
			if ("ent".equals(searchBises.get("type"))) {
				bisGateCar.setBisType("1"); // 1:入库
			} else if ("out".equals(searchBises.get("type"))) {
				bisGateCar.setBisType("2"); // 出库
			} else {
				bisGateCar.setBisType("4");
			}
			if ("billNum".equals(key)) {
				String billNum = (String) searchBises.get("billNum");
				bisGateCar.setBillNum(billNum);
			}
			if ("asn".equals(key)) {
				String asn = (String) searchBises.get("asn");
				bisGateCar.setAsn(asn);
			}
			if ("clientId".equals(key)) {
				String clientId = (String) searchBises.get("clientId");
				bisGateCar.setStockId(clientId);
			}
			if ("clientName".equals(key)) {
				String clientName = (String) searchBises.get("clientName");
				bisGateCar.setStockName(clientName);
			}
			if ("entNum".equals(key)) {
				Double entNum = (Double) searchBises.get("entNum");
				bisGateCar.setEnterNum(entNum);
			}
			if ("outNum".equals(key)) {
				Double outNum = (Double) searchBises.get("outNum");
				bisGateCar.setOutNum(outNum);
			}
			if ("loadingNum".equals(key)) {
				String loadingNum = (String) searchBises.get("loadingNum");
				bisGateCar.setLoadingNum(loadingNum);
			}
			// resultMap.put(key, value);
		}
		return bisGateCar;
	}

	/**
	 * 寻找业务类型
	 * 
	 * @return
	 */
	public Map<String, Object> searchBis1(String carNo, String ctnNum) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("str", "empty");
		List<PropertyFilter> asnF = new ArrayList<PropertyFilter>();
		asnF.add(new PropertyFilter("EQS_ctnNum", ctnNum));
		asnF.add(new PropertyFilter("INAS_asnState", new String[] { "1", "2" }));
		List<BisAsn> asnList = asnService.findByF(asnF);
		if (!asnList.isEmpty()) {
			BisAsn asn = asnList.get(0);
			List<BisAsnInfo> infoList = asnInfoService.getASNInfoList(asn.getAsn());
			Double entNum = 0d;
			for (BisAsnInfo info : infoList) {
				entNum += info.getPiece();
			}
			resultMap.put("str", "success");
			resultMap.put("type", "ent");
			resultMap.put("billNum", asn.getBillNum());
			resultMap.put("asn", asn.getAsn());
			resultMap.put("clientId", asn.getStockIn());
			resultMap.put("clientName", asn.getStockName());
			resultMap.put("entNum", entNum);
			return resultMap;
		} else {
			List<PropertyFilter> orderF = new ArrayList<PropertyFilter>();
			orderF.add(new PropertyFilter("EQS_carNum", carNo));
			orderF.add(new PropertyFilter("INAS_orderState", new String[] { "1", "2", "3" }));
			List<BisLoadingOrder> orderList = loadingOrderService.search(orderF);
			if (!orderList.isEmpty()) {
				BisLoadingOrder order = orderList.get(0);
				List<BisLoadingOrderInfo> infoList = loadingOrderInfoService.getInfoList(order.getOrderNum());
				Double outNum = 0d;
				for (BisLoadingOrderInfo info : infoList) {
					outNum += info.getPiece();
				}
				resultMap.put("str", "success");
				resultMap.put("type", "out");
				resultMap.put("outNum", outNum);
				resultMap.put("clientId", order.getStockIn());
				resultMap.put("clientName", order.getStockName());
				List<BisLoadingInfo> loadingList = loadingInfoService.getLoadingByOrder(order.getOrderNum());
				if (loadingList.isEmpty()) {
					resultMap.put("loadingNum", "");
				} else {
					resultMap.put("loadingNum", loadingList.get(0).getLoadingTruckNum());
				}
				return resultMap;
			} else {
				return resultMap;
			}
		}
	}

}
