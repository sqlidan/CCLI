package com.haiersoft.ccli.platform.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.PlatformReservationInbound;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.platform.service.ReservationOutboundService;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Controller
@RequestMapping(value = "platform/reservation/outbound")
public class ReservationOutboundController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationOutboundController.class);

    @Autowired
    private ReservationOutboundService reservationOutboundService;

    @Autowired
    private HttpUtil httpUtil;
    /**
     * @Description: 预约出库记录
     * @param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String outboundList(){
        return "platform/reservation/outbound";
    }

    @RequestMapping(value = "/selectList",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> outboundList(HttpServletRequest request){
        Page<PlatformReservationOutbound> page=getPage(request);
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


            end.add(Calendar.DATE, 1);
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
       // page.orderBy("status,appointDate").order("asc,desc");
        //page.orderBy("status,appointDate,queuingTime").order("asc,desc,asc").setOrderNulls("false,false,true");
        page.orderBy("appointDate,queuingTime").order("desc,asc").setOrderNulls("false,true");

        page= reservationOutboundService.search(page,filters);
        return getEasyUIData(page);
    }


    //车辆取消靠口
    @RequestMapping(value = "cancel/{id}")
    @ResponseBody
    public String cancel(@PathVariable("id") String id) {
        if(id==null&&id.equals("")) {
            return "error";
        }
        PlatformReservationOutbound platformReservationOutbound= reservationOutboundService.get(id);
        //取消
        platformReservationOutbound.setStatus("3");
        reservationOutboundService.save(platformReservationOutbound);

        User user = UserUtil.getCurrentUser();

        //取消靠口
        Map<String,String> sendMap = new HashMap<>();

        sendMap.put("operator",user.getName());
        sendMap.put("inoutBoundFlag","2");
        sendMap.put("yyid",platformReservationOutbound.getYyid());

        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_CANCEL_RESERVATION_NO_GATEIN, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
    //散货同步信息修改客户信息
    @RequestMapping(value = "synchronousInformation/{id}")
    @ResponseBody
    public String synchronousInformation(@PathVariable("id") String id) {
        if(id==null&&id.equals("")) {
            return "error";
        }
        PlatformReservationOutbound platformReservationOutbound= reservationOutboundService.get(id);


        Map<String,String> sendMap = new HashMap<>();

        sendMap.put("inoutBoundFlag","2");
        sendMap.put("yyid",platformReservationOutbound.getYyid());

        try{
          String result=  httpUtil.doPostSync(PlatformConsts.PLATFORM_URL_BULKCARGO_UPDATE_OUTRESERVATION, JSON.toJSONString(sendMap));

            JSONObject jsonObject = JSON.parseObject(result);
            String code =jsonObject.getString("code");
            if(!"200".equals(code)){
                return jsonObject.getString("msg");
            }

        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}
