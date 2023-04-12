package com.haiersoft.ccli.platform.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.entity.BasePlatform;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.dao.PlatformReservationInboundDao;
import com.haiersoft.ccli.platform.dao.ReservationOutboundDao;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.platform.service.BasePlatformService;
import com.haiersoft.ccli.platform.service.ReservationInboundService;
import com.haiersoft.ccli.platform.service.ReservationOutboundService;
import com.haiersoft.ccli.platform.service.VehicleQueueService;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述 来车列表
 */
@Controller
@RequestMapping(value = "platform/vehicle/queue")
public class VehicleQueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleQueueController.class);

    @Autowired
    VehicleQueueService vehicleQueueService;
    @Autowired
    BasePlatformService platformService;
    @Autowired
    ReservationInboundService inboundService;
    @Autowired
    ReservationOutboundService outboundService;

    @Autowired
    private HttpUtil httpUtil;
//    @Autowired
//    PlatformAllocationLogService platformAllocationLogService;
    /**
     * @Description: 来车列表
     * @param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "platform/vehicle/queue";
    }

    @RequestMapping(value = "/serach", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> serach(HttpServletRequest request){
      /*  Page<VehicleQueue> page=getPage(request);
        List<VehicleQueue> queue = vehicleQueueService.getTodayQueue();
        page.setResult(queue);
        return getEasyUIData(page);*/

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

        end.add(Calendar.DATE, 1);
        // 时
        end.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        end.set(Calendar.MINUTE, 0);
        // 秒
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        Page<VehicleQueue> page=getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        filters.add(new PropertyFilter("GED_queueTime", start.getTime()));
        filters.add(new PropertyFilter("LTD_queueTime", end.getTime()));

        page.orderBy("statusFlag,queueTime").order("asc,asc");
        page= vehicleQueueService.search(page,filters);
       List<VehicleQueue> list= page.getResult();
    /*   for(VehicleQueue vehicleQueue:list){
        if(vehicleQueue.getStatusFlag().equals("0")){
            long t =System.currentTimeMillis() - vehicleQueue.getQueueTime().getTime();
            long hour=t / ( 60 * 60 * 1000);
           if( hour>=4){
               vehicleQueue.setColorType("2");
           }else if(hour>=2){
               vehicleQueue.setColorType("1");
           }
        }
       }*/
        return getEasyUIData(page);
    }

    /**
     * @Description: 手动指派月台跳转
     * @param
     * @return
     */
    @RequestMapping(value = "/assignment/{id}", method = RequestMethod.GET)
    public String assignment(@PathVariable("id") String id, Model model) {

      VehicleQueue vehicleQueue=  vehicleQueueService.get(id);
        model.addAttribute("queueId", id);
        model.addAttribute("inoutBoundFlag",vehicleQueue.getInoutBoundFlag());
        model.addAttribute("trayRoomNum",vehicleQueue.getWarehouseNo());
        model.addAttribute("action", "assignPlat");
        return "platform/vehicle/assignment";
    }

    /**
     * @Description: 指派月台
     * @param
     * @return
     */
    @RequestMapping(value = "/assignPlat", method = RequestMethod.POST)
    @ResponseBody
    public String update(@ModelAttribute @RequestBody VehicleQueue vehicleQueue, Model model) {
        VehicleQueue queue = vehicleQueueService.find("id",vehicleQueue.getId());
      //  VehicleQueue queueTime= vehicleQueueService.getMinQueueTime();
       // BasePlatform platform = platformService.findByPlatformNo(vehicleQueue.getPlatformId());

        if(!"0".equals(queue.getStatusFlag())){
            return "车辆目前非排队状态";
        }
        BasePlatform platform = platformService.get(vehicleQueue.getPlatformId());
        queue.setPlatformId(platform.getId());
        queue.setPlatformNo(String.valueOf(platform.getPlatformNo()));
        queue.setPlatformName(platform.getPlatform());
        queue.setWarehouseNo(platform.getTrayRoomNum());
        queue.setAutoManualFlag("2");
        // 添加优先队列，不使用最早时间，注：最早时间的sql写的不对
      //  queue.setQueueTime(DateUtils.addSecond(queueTime.getQueueTime(),-1));
        vehicleQueueService.update(queue);


        //添加优先队列
        Map<String,String> sendMap = new HashMap<>();
        sendMap.put("id",vehicleQueue.getId());



        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_UPDATE_PLATROM_PRIORQUEUE, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*//生成指派记录
        PlatformAllocationLog log = new PlatformAllocationLog();
        BeanUtils.copyProperties(queue,log);
        log.setQueueId(Integer.parseInt(queue.getId()));
        log.setBusinessDate(new Date());
        platformAllocationLogService.save(log);*/
        return "success";
    }



    //车辆取消作业  取消预约
    @RequestMapping(value = "cancel/{id}")
    @ResponseBody
    public String cancel(@PathVariable("id") String id) {
        if(id==null&&id.equals("")) {
            return "error";
        }
        //功能  platform 修改
      //VehicleQueue queue = vehicleQueueService.find("id",id);
     VehicleQueue queue = vehicleQueueService.get(id);

        if("1".equals(queue.getInoutBoundFlag())){
            PlatformReservationInbound platformReservationInbound = inboundService.find("yyid", queue.getYyid());
            platformReservationInbound.setStatus("3");
            platformReservationInbound.setUpdatedTime(new Date());
            inboundService.update(platformReservationInbound);
        }else if("2".equals(queue.getInoutBoundFlag())) {

            PlatformReservationOutbound platformReservationOutbound = outboundService.find("yyid", queue.getYyid());
            platformReservationOutbound.setStatus("3");
            platformReservationOutbound.setUpdatedTime(new Date());
            outboundService.update(platformReservationOutbound);
        }
        //取消靠口
      //  queue.setStatusFlag("6");
       // vehicleQueueService.update(queue);

        User user = UserUtil.getCurrentUser();

        //取消靠口
        Map<String,String> sendMap = new HashMap<>();
        sendMap.put("id",id);
        sendMap.put("operator",user.getName());


        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_CANCEL_RESERVATION, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";

    }



    //重新靠口
    @RequestMapping(value = "resertQueue/{id}")
    @ResponseBody
    public String resertQueue(@PathVariable("id") String id) {
        if(id==null&&id.equals("")) {
            return "error";
        }
        // VehicleQueue queue = vehicleQueueService.find("id",id);
       // VehicleQueue queue = vehicleQueueService.get(id);
      //  PlatformReservationOutbound outbound =outboundDao.findUniqueBy("yyid", queue.getId());
        //取消靠口
      //  queue.setStatusFlag("0");

      //  vehicleQueueService.update(queue);

        //User user = UserUtil.getCurrentUser();

        //重新靠口
        Map<String,String> sendMap = new HashMap<>();
        sendMap.put("id",id);

        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_RESERT_QUEUE, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";

    }
}
