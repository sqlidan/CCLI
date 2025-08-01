package com.haiersoft.ccli.wms.web.passPort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPort;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfo;
import com.haiersoft.ccli.wms.entity.passPort.BisPassPortInfoDJ;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryDictData;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoDJService;
import com.haiersoft.ccli.wms.service.passPort.PassPortInfoService;
import com.haiersoft.ccli.wms.service.passPort.PassPortService;
import com.haiersoft.ccli.wms.web.preEntry.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("wms/passPortApi")
public class PassPortApiController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PassPortApiController.class);

    @Autowired
    private PassPortService passPortService;

    /**
     * 获取单条核放单信息中的总重量
     */
    @RequestMapping(value = "checkTotalWt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkTotalWt(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        String PLATE_NO = request.getParameter("PLATE_NO");
        System.out.println("PLATE_NO："+PLATE_NO);
        if(PLATE_NO != null && PLATE_NO.trim().length() > 0){

        }else{
            result.put("code", "500");
            result.put("msg", "承运车牌号为必填参数!");
            return result;
        }
        String FLAG = request.getParameter("FLAG");
        System.out.println("FLAG："+FLAG);
        if(FLAG != null && FLAG.trim().length() > 0){
            if ("O".equals(FLAG.trim())){
                FLAG = "E";
            }
        }else{
            result.put("code", "500");
            result.put("msg", "进出区标识为必填参数!");
            return result;
        }
        return passPortService.getDataByVehicleNo(PLATE_NO,FLAG);
    }

    /**
     * 校验核放单审核通过但未过闸数据
     */
    @RequestMapping(value = "passButNotPassGate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> passButNotPassGate(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        String PLATE_NO = request.getParameter("PLATE_NO");
        System.out.println("PLATE_NO："+PLATE_NO);
        if(PLATE_NO != null && PLATE_NO.trim().length() > 0){

        }else{
            result.put("code", "500");
            result.put("msg", "承运车牌号为必填参数!");
            return result;
        }
        String FLAG = request.getParameter("FLAG");
        System.out.println("FLAG："+FLAG);
        if(FLAG != null && FLAG.trim().length() > 0){
            if ("O".equals(FLAG.trim())){
                FLAG = "E";
            }
        }else{
            result.put("code", "500");
            result.put("msg", "进出区标识为必填参数!");
            return result;
        }
        return passPortService.passButNotPassGate(PLATE_NO,FLAG);
    }

//====================2.2.2.4全量查询实时库存================================================================
    @RequestMapping(value = "queryFullInventoryData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryFullInventoryData(HttpServletRequest request) {
        SimpleDateFormat startTemp = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endTemp = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String startTime = startTemp.format(new Date());
        String endTime = endTemp.format(new Date());
        return passPortService.queryFullInventoryData(startTime,endTime);
    }
}
