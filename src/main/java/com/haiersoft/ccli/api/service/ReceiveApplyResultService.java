package com.haiersoft.ccli.api.service;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.util.OpenGateUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.HttpGo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 86185
 * @Date 2022/6/15 22:34
 * @Version 1.0
 */

@Service
public class ReceiveApplyResultService {
    private static final Logger log = LoggerFactory.getLogger(ReceiveApplyResultService.class);

    @Autowired
    private HttpGo httpGo;

    String url = OpenGateConstant.RECEIVE_APPLY_RESULT_URL;

    @Async
    public void sendPassInfo(ApiCustomerQueryApply apply){
        String param = this.bulidParam(apply);
        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), param);
        if (resultPost == null) {
            log.error("传输失败，没有返回值");
        } else {
            log.error(resultPost);
        }
    }

    @Async
    public void sendRefuseInfo(ApiCustomerQueryApply apply){
        String param = this.bulidParam(apply);
        log.error("ReceiveApplyResult 申请推送接口url::: "+url);
        log.error("ReceiveApplyResult 申请推送接口参数::: "+param);

        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), param);
        log.error("ReceiveApplyResult 申请推送接口返回::: "+resultPost);
        if (resultPost == null) {
            log.error("传输失败，没有返回值");
        } else {
            log.error(resultPost);
        }
    }

    private String bulidParam(ApiCustomerQueryApply apply) {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();
        Map<String, Object> body = new HashMap<>();

        body.put("traceId", header.get("requestId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));

        body.put("supplyId", apply.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = sdf.format(apply.getComfirmTime());
        body.put("procTime", dateStr);
        body.put("status", apply.getConfirmStatus().toString());

        body.put("customerNumber", apply.getCustomerNumber());
        body.put("customerName", apply.getCustomerName());
        body.put("taxNumber", apply.getTaxNumber());
        body.put("remark", apply.getRemark());


        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);

        return paramJson;
    }

}
