package com.haiersoft.ccli.base.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haiersoft.ccli.base.entity.BaseLoadingStrategy;
import com.haiersoft.ccli.base.service.LoadingStrategyService;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingOrder;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderInfoService;
import com.haiersoft.ccli.wms.service.LoadingOrderService;
import com.haiersoft.ccli.wms.service.TransferService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 楼层选择
 */
@Controller
@RequestMapping("base/floor")
public class FloorContorller extends BaseController {

    @Autowired
    private LoadingOrderService loadingOrderService;

    //装车单
    @Autowired
    private LoadingInfoService loadingInfoService;

    //订单明细
    @Autowired
    private LoadingOrderInfoService loadingOrderInfoService;

    /**
     * 跳转楼层选择
     */
    @RequestMapping(value = "choose/{ordId}", method = RequestMethod.GET)
    public String checkForm(@PathVariable("ordId") String ordId, Model model) {
        model.addAttribute("ordId", ordId);
        return "wms/loadingorder/chooseFloorList";
    }

    /**
     * 根据策略编码获取策略详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "listFloorTray", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> listFloorTray(HttpServletRequest request) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (request.getParameter("ordId") != null) {
            // 出库单号
            String ordId = request.getParameter("ordId").toString();
            //获取出库联系单对象
            BisLoadingOrder loadingOrder = loadingOrderService.get(ordId);
            //获取订单明细
            List<Map<String, Object>> getOrdList = loadingOrderInfoService.findInfoList(ordId);

            List<String> numList = Lists.newArrayList();
            for (Map<String, Object> getMap : getOrdList) {

                String billNum = getMap.get("BILL_NUM").toString();
                String ctnNum = getMap.get("CTN_NUM").toString();
                String skuNum = getMap.get("SKU_ID").toString();

                String numCode = billNum + "_" + ctnNum + "_" + skuNum;
                if (numList.contains(numCode)) {
                    continue;
                }
                numList.add(numCode);

                // 获取目前库存
                List<Map<String, Object>> trayList = loadingInfoService.listFloorTray(billNum, ctnNum, skuNum);
                list.addAll(trayList);
            }
        }
        return list;
    }

    /**
     * 刷新楼层
     *
     * @return
     */
    @RequestMapping(value = "refFloor/{ordId}/{floorNum}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> refFloor(@PathVariable("ordId") String ordId, @PathVariable("floorNum") String floorNum) {
        Map<String, Object> returnInfo = Maps.newHashMap();
        returnInfo.put("code", "200");

        //获取出库联系单对象
        BisLoadingOrder loadingOrder = loadingOrderService.get(ordId);
        if (Objects.isNull(loadingOrder)) {
            returnInfo.put("code", "400");
            returnInfo.put("msg", "出库单不存在或已删除!");
            return returnInfo;
        }
        // 装车单校验
        List<BisLoadingInfo> truckList = loadingInfoService.getLoadingInfoIfOrd(ordId);
        if (!CollectionUtils.isEmpty(truckList)) {
            for (BisLoadingInfo loadInfo : truckList) {
                if (loadInfo.getFloorNum().equals(floorNum)) {
                    returnInfo.put("code", "400");
                    returnInfo.put("msg", "当前楼层和已分配楼层一致!");
                    return returnInfo;
                }
            }
        }
        // 是否可删除
        boolean isClear = loadingInfoService.deleteClearTruck(ordId);
        if (isClear) {
            // 删除装车单
            BisLoadingOrder ordObj = loadingOrderService.get(ordId);
            ordObj.setOrderState("1");
            loadingOrderService.update(ordObj);
            // 重新生成装车单
            Map<String, Object> truckMap = loadingInfoService.createTruck(ordId, "0", floorNum);

            if (truckMap.containsKey("splitTray")) {
                // 需要拆托
                returnInfo.put("needSplit", "Y");
                returnInfo.put("trayIds", truckMap.get("trayIds"));
                returnInfo.put("splitTray", truckMap.get("splitTray"));
            } else {
                // 分配完成
                String tNum = truckMap.get("tNum").toString();
                // 装车单备注
                loadingInfoService.insertRemark(ordId, tNum);
            }
        } else {
            returnInfo.put("code", "400");
            returnInfo.put("msg", "装车单不能生成!");
            return returnInfo;
        }
        return returnInfo;
    }


}
