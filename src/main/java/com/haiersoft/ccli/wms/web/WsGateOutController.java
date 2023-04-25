package com.haiersoft.ccli.wms.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisGateCar;
import com.haiersoft.ccli.wms.entity.BisInOut;
import com.haiersoft.ccli.wms.service.GateCarService;

/**
 * 闸口管理
 */
@Controller
@RequestMapping("wsgate/wms/gate")
public class WsGateOutController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WsGateOutController.class);

    @Autowired
    private GateCarService gateCarService;


    /**
     * 对外接口：接收出闸车号，记录出闸时间
     * 接收参数为：
     * carNum 车号
     * ctnNum1 箱号1
     * ctnNum2 箱号2
     * snFlag 南北闸标志 S:南闸,N:北闸
     *
     */
    @RequestMapping(value = "/out", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map<String, Object> outGate(HttpServletRequest request) {
        /*
         * String carNum= null; String ctnNum1= null; String ctnNum2= null; String
         * snFlag= null;
         *
         * try { BufferedReader streamReader = new BufferedReader( new
         * InputStreamReader(request.getInputStream(), "UTF-8")); StringBuilder
         * responseStrBuilder = new StringBuilder(); String inputStr; while ((inputStr =
         * streamReader.readLine()) != null) responseStrBuilder.append(inputStr);
         *
         * JSONObject jsonObject =
         * JSONObject.parseObject(responseStrBuilder.toString());
         *
         * logger.error( jsonObject.toJSONString()); carNum =
         * jsonObject.getString("carNum"); ctnNum1 = jsonObject.getString("ctnNum1");
         * ctnNum2 = jsonObject.getString("ctnNum2"); snFlag =
         * jsonObject.getString("snFlag");
         *
         * } catch (Exception e) { e.printStackTrace(); }
         */

        String carNum = request.getParameter("carNum");
        Map<String, Object> resultMap = new HashMap();
        // 车牌号为空，直接return null
        if (StringUtils.isEmpty(carNum)) {
            resultMap.put("code", 1);
            resultMap.put("success", false);
            resultMap.put("msg", "车牌号不允许为空！");
            return resultMap;
        }
        String ctnNum1 = request.getParameter("ctnNum1");
        String ctnNum2 = request.getParameter("ctnNum2");
        String snFlag = request.getParameter("snFlag");
        if (StringUtils.isEmpty(snFlag)) {
            resultMap.put("code", 1);
            resultMap.put("success", false);
            resultMap.put("msg", "南北闸标志不能为空");
            return resultMap;
        }


        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_carNum", carNum));
        filters.add(new PropertyFilter("NULLAD_carOutDate","NULL"));


        List<BisGateCar> bgcList = gateCarService.search(filters);
        Date date = new Date();
        for(BisGateCar bgc :bgcList) {
            bgc.setCarOutDate(date);
            bgc.setCarOutGate(snFlag);
            gateCarService.save(bgc);
        }
        return resultMap;
    }

}