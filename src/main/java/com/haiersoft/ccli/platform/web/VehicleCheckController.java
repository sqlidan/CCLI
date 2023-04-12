package com.haiersoft.ccli.platform.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.VehicleCheck;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.platform.service.ReservationInboundService;
import com.haiersoft.ccli.platform.service.VehicleCheckService;
import com.haiersoft.ccli.platform.service.VehicleQueueService;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Controller
@RequestMapping(value = "platform/vehicle/check")
public class VehicleCheckController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleCheckController.class);

    @Autowired
    private ReservationInboundService reservationInboundService;
    @Autowired
    private VehicleCheckService vehicleCheckService;
    @Autowired
    private VehicleQueueService vehicleQueueService;

    @Autowired
    private HttpUtil httpUtil;

    @RequestMapping(value = "/list")
    public String list(){
        return "platform/vehicle/check";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public Map<String,Object> search(HttpServletRequest request){
        Page<VehicleCheck> page=getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page.orderBy("checkStatus").order("asc");
        page = vehicleCheckService.search(page,filters);
        return getEasyUIData(page);

    }


    @RequestMapping(value = "/updateStatus/{id}",method = RequestMethod.GET)
    public String updateStatus(@PathVariable("id") String id, Model model){
       model.addAttribute("check", vehicleCheckService.getbyId(id));
        model.addAttribute("action", "edit/{id}");
        return "platform/vehicle/updatecheck";
    }


    @RequestMapping(value = "/edit/{id}",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String editStatus(@RequestParam("id")String id, @RequestParam("actualVehicleNo")String actualVehicleNo ){

        vehicleCheckService.updateStatus(id,actualVehicleNo);
        VehicleCheck vehicleCheck = vehicleCheckService.getbyId(id);
        VehicleQueue vehicleQueue = new VehicleQueue();
        vehicleQueue.setQueueTime(new Date());
        vehicleQueue.setStatusFlag("0");
        vehicleQueue.setInoutBoundFlag("1");
        vehicleQueue.setPlatNo(StringUtils.isEmpty(actualVehicleNo)?vehicleCheck.getVehicleNo():actualVehicleNo);
        vehicleQueue.setContainerNo(vehicleCheck.getContainerNo());
        vehicleQueue.setAutoManualFlag("1");
        vehicleQueue.setDeletedFlag("0");
        vehicleQueue.setYyid(vehicleCheck.getYyid());
       // vehicleQueue.setActualVehicleNo(StringUtils.isEmpty(actualVehicleNo)?vehicleCheck.getVehicleNo():actualVehicleNo);
        vehicleQueueService.save(vehicleQueue);
/*
        // 不更新预约信息  2022/1/4
        if (!StringUtils.isEmpty(actualVehicleNo)){
            reservationInboundService.updateByYyid(vehicleCheck.getYyid(),actualVehicleNo);
        }*/
        //更新预约表排队时间
        reservationInboundService.updateQueueingTimeByYyid(vehicleCheck.getYyid());
       //加入普通排队队列
        Map<String,String> sendMap = new HashMap<>();
        sendMap.put("id",vehicleQueue.getId());



        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_UPDATE_PLATROM_QUEUE_CHECK, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

         return "success";
    }
}
