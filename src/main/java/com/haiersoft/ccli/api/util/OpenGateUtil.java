package com.haiersoft.ccli.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.service.ReceiveApplyResultService;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用于调用 质押 调用中台接口
 *
 * @Author 86185
 * @Date 2022/4/29 17:31
 * @Version 1.0
 */

public class OpenGateUtil {

    private static final Logger log = LoggerFactory.getLogger(OpenGateUtil.class);


    private static HttpGo httpGo = new HttpGo() ;

    private static String token = "";

    public static String getToken() {
//        if(StringUtils.isNotBlank(token)){
//            return token;
//        }
//        else{
//            setToken();
//            return token;
//        }
        setToken();
        return token;

    }

    public static void setToken() {

        //请求head部分
        Map<String, String> realheader = headerBuilder();

        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, String> header = new HashMap<>();
        header.put("appId", OpenGateConstant.APP_ID);
        header.put("appSecret", OpenGateConstant.APP_SECRET);
        header.put("charSet", OpenGateConstant.CHAR_SET);
        header.put("userId", OpenGateConstant.USER_ID);
        header.put("version", OpenGateConstant.VERSION);
        header.put("channel", OpenGateConstant.CHANNEL);

        String requestId = UUID.randomUUID().toString();
        header.put("requestId", requestId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        header.put("requestTime", dateStr);


        Map<String, String> body = new HashMap<>();
        body.put("traceId", requestId);
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("requestTime", dateStr);
        body.put("operationUserId", OpenGateConstant.USER_ID);

        Map<String, Map<String, String>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);

        log.error("调用中台接口获取Token()发送url："+OpenGateConstant.OPEN_GATE_TOKEN_URL);
        log.error("调用中台接口获取Token()发送参数："+paramJson);
        //调用构建 head的请求
        String message = httpGo.sendRequest(OpenGateConstant.OPEN_GATE_TOKEN_URL, paramJson);
        log.error("调用中台接口获取Token()返回参数："+message);
        JSONObject obj = JSONObject.parseObject(message);
        token = obj.getString("token");
        System.out.println(token);
    }

    public static Map<String, String> headerBuilder(){
        Map<String, String> header = new HashMap<>();
        header.put("apiType", OpenGateConstant.HEADER_APP_TYPE);
        return header;
    }

    public static Map<String, Object> headerOfBodyBuilder() {

        Map<String, Object> header = new HashMap<>();
        header.put("accessToken", OpenGateUtil.getToken());
        header.put("appId", OpenGateConstant.APP_ID);
        header.put("appSecret", OpenGateConstant.APP_SECRET);
        header.put("charSet", OpenGateConstant.CHAR_SET);
        String requestId = UUID.randomUUID().toString();
        header.put("requestId", requestId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        header.put("requestTime", dateStr);

        header.put("userId", OpenGateConstant.USER_ID);
        header.put("version", OpenGateConstant.VERSION);
        header.put("channel", OpenGateConstant.CHANNEL);

        return header;
    }
}
