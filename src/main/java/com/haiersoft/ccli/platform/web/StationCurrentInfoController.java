package com.haiersoft.ccli.platform.web;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.platform.entity.StationCurrentInfo;
import com.haiersoft.ccli.platform.service.BasePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/12/9
 * @描述
 */
@Controller
@RequestMapping(value = "platform/station/current")
public class StationCurrentInfoController extends BaseController {

    @Autowired
    private BasePlatformService basePlatformService;
    /**
     * @Description: 月台实时信息
     * @param
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String currentInfo(){
        return "platform/station/currentStatus";
    }

//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String,Object> search(HttpServletRequest request) {
//        Page<StationCurrentInfo> page = getPage(request);
//        List<StationCurrentInfo> result = new ArrayList<>();
//        List<Map<String,Object>> platformList = platformService.getActivePlatform();
//        for (Map entity : platformList) {
//            StationCurrentInfo stationCurrentInfo = new StationCurrentInfo();
//            stationCurrentInfo.setPlatform((entity.get("PLATFORM")!=null?entity.get("PLATFORM"):"").toString());
//            stationCurrentInfo.setWarehouseNo((entity.get("TRAY_ROOM_NUM")!=null?entity.get("TRAY_ROOM_NUM"):"").toString());
//            stationCurrentInfo.setPlatformNo((entity.get("PLATFORM_NO")!=null?Integer.parseInt(String.valueOf(entity.get("PLATFORM_NO"))):null));
//            stationCurrentInfo.setPlatformStatus((entity.get("PLATFORM_STATUS")!=null?entity.get("PLATFORM_STATUS"):"").toString());
//            stationCurrentInfo.setPlatformType((entity.get("PLATFORM_TYPE")!=null?entity.get("PLATFORM_TYPE"):"").toString());
//           //作业中或者车辆驶入月台状态  展示车牌号
//            if ("1".equals((entity.get("PLATFORM_STATUS")!=null?entity.get("PLATFORM_STATUS"):"").toString())||"4".equals((entity.get("PLATFORM_STATUS")!=null?entity.get("PLATFORM_STATUS"):"").toString())) {
//               //这个查询  只有点击开始之后 才能显示车牌号
//                StationOperationRecord stationOperationRecord = stationOperationRecordService.getRecord( Integer.parseInt(String.valueOf(entity.get("ID"))));
//                if (stationOperationRecord!=null){
//                    stationCurrentInfo.setPlatNo(stationOperationRecord.getPlatNo());
//
//                    //入库   通过预约id关联预约入库表 展示箱号
//                    if(stationOperationRecord.getInoutBoundFlag().equals("1")){
//
//                        PlatformReservationInbound platformReservationInbound = reservationInboundService.find("yyid", stationOperationRecord.getYyid());
//
//                        if(null!=platformReservationInbound){
//                          stationCurrentInfo.setCtnNum(platformReservationInbound.getContainerNo());
//
//                        }
//                    }
//                }
//
//            }
//            result.add(stationCurrentInfo);
//
//        }
//        page.setResult(result);
//        return getEasyUIData(page);
//    }



    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> search(HttpServletRequest request) {
        Page<StationCurrentInfo> page = getPage(request);
        //List<StationCurrentInfo> result = new ArrayList<>();
        List result = basePlatformService.getCurrentInfo();
        page.setResult(result);
        return getEasyUIData(page);
    }

}
