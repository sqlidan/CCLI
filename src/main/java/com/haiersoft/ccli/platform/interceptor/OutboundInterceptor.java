package com.haiersoft.ccli.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import com.haiersoft.ccli.platform.utils.PlatformConsts;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author
 * @Date
 * @Version
 */

public class OutboundInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OutboundInterceptor.class);

    @Autowired
    private HttpUtil httpUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Out preHandle~~~~");
        Map<String,String[]> pramMap = request.getParameterMap();
        Map<String,String> sendMap = new HashMap<>();
        for(String str :pramMap.keySet()){
            sendMap.put(str,pramMap.get(str)[0]);
        }
        try{
            httpUtil.doPost(PlatformConsts.PLATFORM_URL_OUT, JSON.toJSONString(sendMap));
        }catch (HttpHostConnectException ex){
            logger.info(ex.getMessage());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
