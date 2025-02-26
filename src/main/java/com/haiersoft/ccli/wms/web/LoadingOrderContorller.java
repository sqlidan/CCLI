package com.haiersoft.ccli.wms.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.service.LoadingStrategyService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.entity.BisLoadingOrderInfo;
import com.haiersoft.ccli.wms.entity.BisOutStock;
import com.haiersoft.ccli.wms.entity.BisOutStockInfo;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.OutStockInfoService;
import com.haiersoft.ccli.wms.service.OutStockService;

/**
 * 出库订单总览
 *
 * @author LZG
 */
@Controller
@RequestMapping("bis/loading")
public class LoadingOrderContorller extends BaseController {

    @Autowired
    private LoadingOrderService loadingOrderService;
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;
    @Autowired
    private OutStockService outStockService;//出库联系单
    @Autowired
    private OutStockInfoService outStockInfoService;
    @Autowired
    private LoadingStrategyService loadingStartegyService;//分拣策略

    /*跳转出库订单列表页面*/
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String menuList() {
        return "wms/loadingorder/orderList";
    }

    /*
     * 列表页面table获取json
     * */
    @RequiresPermissions("bis:loading:view")
    @RequestMapping(value = "listjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisLoadingOrder> page = getPage(request);
        page.orderBy("createTime").order(Page.DESC);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = loadingOrderService.search(page, filters);
        List<BisLoadingOrder> bisLoadingOrderList = new ArrayList<>();
        for (BisLoadingOrder forBisLoadingOrder:page.getResult()) {
            BisOutStock bisOutStock = outStockService.find("outLinkId",forBisLoadingOrder.getOutLinkId());
            if (bisOutStock!=null && bisOutStock.getIfBonded()!=null && "1".equals(bisOutStock.getIfBonded().trim().toString())){
                forBisLoadingOrder.setIfBonded("1");//保税
            }else{
                forBisLoadingOrder.setIfBonded("0");//非保税
            }
            bisLoadingOrderList.add(forBisLoadingOrder);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", bisLoadingOrderList);
        map.put("total", page.getTotalCount());
        return map;
    }

    /**
     * 添加出库订单跳转
     */
    @RequiresPermissions("bis:loading:add")
    @RequestMapping(value = "create/{linkId}", method = RequestMethod.GET)
    public String createForm(@PathVariable("linkId") String linkId, Model model) {
        BisLoadingOrder newObj = new BisLoadingOrder();
        newObj.setIslock("0");
        if (linkId != null && !"000000".equals(linkId)) {
            // 获取出库联系单信息，填充出库订单
            // 根据出库联系单id获取出库联系单对象
        	BisOutStock outStock = outStockService.get(linkId);
            if (outStock != null) {
                newObj.setOutLinkId(outStock.getOutLinkId());//联系单号
                newObj.setReceiverId(outStock.getReceiverId());//收货方id
                //标示清库
                if (outStock.getIfClearStore() != null && "1".equals(outStock.getIfClearStore())) {
                    newObj.setIsClear(1);
                }
            }
        }
        newObj.setOrderNum(loadingOrderService.getOrderId());//生成主键
        model.addAttribute("order", newObj);
        model.addAttribute("action", "create");
        return "wms/loadingorder/orderForm";
    }


    @RequiresPermissions("bis:loading:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisLoadingOrder order, Model model) {
        return loadingOrderService.saveOrder(order);
    }


    /**
     * 删除出库订单
     *
     * @param id
     * @return
     */
    @RequiresPermissions("bis:loading:delete")
    @RequestMapping(value = "delete/{orderid}")
    @ResponseBody
    public String delete(@PathVariable("orderid") String orderId) {
        String retStr = "success";
        BisLoadingOrder getObj = loadingOrderService.get(orderId);
        //创建过程中可以删除
        if (getObj != null && "1".equals(getObj.getOrderState())) {
            retStr = loadingOrderService.deleteOder(orderId);
        } else {
            retStr = "state";
        }
        return retStr;
    }
    /**
     * 控车出库单操作对应车辆不能进行出闸操作
     *
     * @param id
     * @return
     */
    @RequiresPermissions("bis:loading:lock")
    @RequestMapping(value = "lock/{orderid}")
    @ResponseBody
    public String lock(@PathVariable("orderid") String orderId) {
        String retStr = "success";
        BisLoadingOrder getObj = loadingOrderService.get(orderId);
        //创建过程中可以删除
        if (getObj != null) {
        	getObj.setIslock("1");
        	loadingOrderService.updateOrder(getObj);
        } else {
            retStr = "state";
        }
        return retStr;
    }
    
    /**
     * 解控车出库单操作对应车辆进行出闸操作
     *
     * @param id
     * @return
     */
    @RequiresPermissions("bis:loading:unlock")
    @RequestMapping(value = "unlock/{orderid}")
    @ResponseBody
    public String unlock(@PathVariable("orderid") String orderId) {
        String retStr = "success";
        BisLoadingOrder getObj = loadingOrderService.get(orderId);
        //创建过程中可以删除
        if (getObj != null) {
        	getObj.setIslock("0");
        	loadingOrderService.updateOrder(getObj);
        } else {
            retStr = "state";
        }
        return retStr;
    }
    

    /**
     * 修改订单跳转
     *
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "update/{orderid}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("orderid") String orderId, Model model) {
        // 获取装车单
        BisLoadingOrder getObj = loadingOrderService.get(orderId);
        if (getObj != null && 1 < Integer.valueOf(getObj.getOrderState())) {
            getObj.setIsEdite(1); // 出库订单变为不可编辑
            if(Integer.valueOf(getObj.getOrderState())==4){
            	getObj.setIsEditeTwo(1);
            }
        }
        //标示清库
        if (getObj.getOutLinkId() != null && !"".equals(getObj.getOutLinkId())) {
            // 根据出库联系单id获取出库联系单对象
            BisOutStock outStock = outStockService.get(getObj.getOutLinkId());
            if (outStock != null) {
                //标示清库
                if (outStock.getIfClearStore() != null && "1".equals(outStock.getIfClearStore())) {
                    getObj.setIsClear(1);
                    getObj.setLastCar(1);
                }
            }

        }
        model.addAttribute("order", getObj);
        model.addAttribute("action", "update");
        return "wms/loadingorder/orderForm";
    }

    /**
     * 修改出库订单
     *
     * @param order
     * @param model
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BisLoadingOrder order, Model model, HttpServletRequest request) {
        return loadingOrderService.updateOrder(order);
    }

    /**
     * 打开订单添加明细跳转
     *
     * @param linkId 出库联系单id
     */
    @RequestMapping(value = "openinfos/{linkId}/{orderNum}", method = RequestMethod.GET)
    public String openInfosForm(@PathVariable("linkId") String linkId, @PathVariable("orderNum") String orderNum, Model model) {
        BisLoadingOrder newObj = new BisLoadingOrder();
        if (linkId != null && !"000000".equals(linkId)) {
            // 获取出库联系单信息，填充出库订单
            // 根据出库联系单id获取出库联系单对象
            BisOutStock outStock = outStockService.get(linkId);
            if (outStock != null) {
                newObj.setStockIn(outStock.getStockInId());//存货方id
                newObj.setStockName(outStock.getStockIn());//存货方名称
                newObj.setWarehouseId(outStock.getWarehouseId());//仓库id
            }
            newObj.setOutLinkId(linkId);
        }
        newObj.setRemark(orderNum);//临时放置出库订单编号
        model.addAttribute("order", newObj);
        return "wms/loadingorder/addEnterInfoList";
    }

    /**
     * 查询联系单跳转
     *
     * @return
     */
    @RequestMapping(value = "outLinklist", method = RequestMethod.GET)
    public String enterList() {
        return "wms/loadingorder/searchList";
    }

    /**
     * 校验库存明细SKU出库数量校验，出库数量>=库存量-质押量
     *
     * @param order
     * @param model
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkSKUNum(@Valid BisLoadingOrder order, Model model) {
        Map<String, Object> retMap = loadingOrderService.checkSKUNum(order);
        return retMap;
    }

    /**
     * 列表页面选择拣货策略
     *
     * @param ordId
     * @param model
     * @return
     */
    @RequiresPermissions("bis:loading:addzcd")
    @RequestMapping(value = "opencl/{ordId}", method = RequestMethod.GET)
    public String opencl(@PathVariable("ordId") String ordId, Model model) {
        List<Map<String, Object>> list = loadingStartegyService.findList();
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("NAME", "默认分拣策略");
        map.put("STRATEGYCODE", 0);
        list.add(map);
        model.addAttribute("retList", JSON.toJSONString(list));
        model.addAttribute("ordId", ordId);
        model.addAttribute("action", "create");
        return "wms/loadingorder/checkStrategy";
    }

    /**
     * 拣货拆托界面
     *
     * @param ordId
     * @param model
     * @return
     */
    @RequiresPermissions("bis:loading:addzcd")
    @RequestMapping(value = "openct/{ordId}", method = RequestMethod.GET)
    public String openct(@PathVariable("ordId") String ordId, Model model) {
        model.addAttribute("ordId", ordId);
        return "wms/loadingorder/splitStock";
    }

    /**
     * 导入出库联系单明细
     *
     * @param ordId
     * @param outLinkId
     * @return
     */
    @RequestMapping(value = "importinfo", method = RequestMethod.GET)
    @ResponseBody
    public String importinfo(HttpServletRequest request, String orderId, String outLinkId) {
        BisLoadingOrder order = loadingOrderService.get(orderId);
        List<BisOutStockInfo> listObj = outStockInfoService.getList(outLinkId);
        int size = listObj.size();
        if (size != 0) {
            User user = UserUtil.getCurrentUser();
            BisLoadingOrderInfo info = null;
            BisOutStockInfo oldObj = null;
            for (int i = 0; i < size; i++) {
                info = new BisLoadingOrderInfo();
                oldObj = new BisOutStockInfo();
                oldObj = listObj.get(i);
                info.setLoadingPlanNum(orderId);
                info.setSkuId(oldObj.getSkuId());
                info.setBillNum(oldObj.getBillNum());
                info.setCtnNum(oldObj.getCtnNum());
                info.setOutLinkId(outLinkId);
                info.setEnterState(oldObj.getEnterState());
                info.setPiece(oldObj.getOutNum());
                info.setNetSingle(oldObj.getNetSingle());
                info.setGrossSingle(oldObj.getGrossSingle());
                info.setNetWeight(oldObj.getNetWeight());
                info.setGrossWeight(oldObj.getGrossWeight());
                info.setUnits("1");
                info.setOperator(user.getName());
                info.setOperateTime(new Date());
                info.setStockIn(order.getStockIn());
                info.setStockName(order.getStockName());
                info.setCatgoName(oldObj.getCargoName());
                loadingOrderInfoService.save(info);
            }
            return "success";
        } else {
            return "blank";
        }
    }

    /*
     * 判断出库订单是否保存
     * */
    @RequestMapping(value = "ifsave/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifsave(@PathVariable("orderId") String orderId) {
        BisLoadingOrder order = loadingOrderService.get(orderId);
        if (order == null) {
            return "success";
        } else {
            return "false";
        }
    }

    /*
     * 判断托盘号是否存在
     * */
    @RequestMapping("confirm")
    @ResponseBody
    public String ifConfirm(HttpServletRequest request) {
        String ifConfirm = "";
        String ss = request.getParameter("ss");
        String ss1[] = ss.trim().split(" ");
        for (int i = 0; i < ss1.length; i++) {
            System.out.println(ss1[i]);
            if (!outStockInfoService.ifConfirm(ss1[i])) {
                ifConfirm += ss1[i] + ",";
            }
        }
        if (!"".equals(ifConfirm)) {
            ifConfirm = ifConfirm.substring(0, ifConfirm.length() - 1);
        }
        return ifConfirm;
    }
    
    
	@RequestMapping(value = "mani/{orderNums}")
	public String maniForm(HttpServletRequest request, Model model, @PathVariable("orderNums") List<String> orderNums) {

		Double totalGrosswt = 0.0;
		String orderNumsStr = "";
		String contaId = "";
		String vehicleId = "";
		for (String orderNum : orderNums) {
			BisLoadingOrder order = loadingOrderService.get(orderNum);
			// 此处车号由前端控制，只能为同一个车号
			vehicleId = order.getCarNum();
			// 拼接订单号
			orderNumsStr = orderNumsStr + orderNum + ",";
			List<BisLoadingOrderInfo> infos = loadingOrderInfoService.getInfoList(orderNum);
			for (BisLoadingOrderInfo info : infos) {
				// 拼接箱号
				contaId = contaId + info.getCtnNum() + ",";
				// 计算总重量
				totalGrosswt = totalGrosswt + info.getGrossWeight();
			}
		}

		String grossWeightTotal = String.format("%.2f",totalGrosswt);

		model.addAttribute("ContaId", contaId);
		model.addAttribute("GrossWt", grossWeightTotal);
		model.addAttribute("VehicleId", vehicleId);
		model.addAttribute("OrderNum", orderNumsStr);
		return "wms/loadingorder/maniForm";
	}

    @Autowired
    private PassPortService passPortService;
	//生成保税核放单
    @RequestMapping("createPassport")
    @ResponseBody
    public String createPassport(@Valid @ModelAttribute @RequestBody BisLoadingOrder order, Model model, HttpServletRequest request) {
        User user = UserUtil.getCurrentUser();
        BisPassPort bisPassPort = new BisPassPort();
        //添加数据
        bisPassPort.setId(getNewPassPortId());
        bisPassPort.setMasterCuscd("4230");//主管关区
        bisPassPort.setAreainEtpsno("3702631016");//区内企业编码
        bisPassPort.setAreainEtpsSccd("91370220395949850B");//区内企业社会信用代码
        bisPassPort.setAreainEtpsNm("青岛港怡之航冷链物流有限公司");//区内企业名称
        bisPassPort.setDclEtpsno("3702631016");//申报单位编码
        bisPassPort.setDclEtpsSccd("91370220395949850B");//申报单位社会信用代码
        bisPassPort.setDclEtpsNm("青岛港怡之航冷链物流有限公司");//申报单位名称
        bisPassPort.setInputCode("3702631016");//录入单位编码
        bisPassPort.setInputSccd("91370220395949850B");//录入单位社会信用代码
        bisPassPort.setInputName("青岛港怡之航冷链物流有限公司");//录入单位名称
        bisPassPort.setEtpsPreentNo("3702631016");//企业内部编号
        bisPassPort.setDclTypecd("1");//申报类型
        bisPassPort.setDclBy(user.getName());
        //TODO 添加表头信息

        bisPassPort.setState("0");//状态
        bisPassPort.setLockage("0");//过卡状态
        bisPassPort.setCreateBy(user.getName());//创建人
        bisPassPort.setCreateTime(new Date());//创建时间
        passPortService.save(bisPassPort);
        return "success";
    }
    public String getNewPassPortId() {
        User user = UserUtil.getCurrentUser();
        String userCode = user.getUserCode();
        //判断用户码是否为空
        if (StringUtils.isNull(user.getUserCode())) {
            userCode = "YZH";
        } else {//判断用户码 的长度
            if (userCode.length() > 3) {
                userCode = userCode.substring(0, 3);
            } else if (userCode.length() < 3) {
                userCode = StringUtils.lpadStringLeft(3, userCode);
            }
        }
        String linkId = "P" + userCode + StringUtils.timeToString();
        return linkId;
    }
}
