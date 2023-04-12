package com.haiersoft.ccli.api.service;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.dao.ExchangeDao;
import com.haiersoft.ccli.api.entity.ExchangeInfo;
import com.haiersoft.ccli.api.util.OpenGateUtil;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeService extends BaseService<ExchangeInfo, String> {

    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);
    @Autowired
    private ExchangeDao exchangeDao;
    @Autowired
    private HttpGo httpGo;

    @Override
    public HibernateDao<ExchangeInfo, String> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 按id值查询交换表
     *
     * @param id id值
     * @return 交换表对象
     */
    @Async
    public void getexchangeInfo(String id) {

        String url = OpenGateConstant.OPEN_GATE_SYNC_EXCHANGE_URL;

        if (StringUtils.isEmpty(id)) {
            return;
        }

        ExchangeInfo exchangeInfo = exchangeDao.Query(id);
        String param;
        if (exchangeInfo != null) {

            param = this.bulidParam(exchangeInfo);

        } else {
            param = this.bulidEmptyParam();
        }
        log.error("同步换货指令 :::{}",param);

        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), param);
        if (resultPost == null) {
            log.error("传输失败，没有返回值");
        } else {
            log.error(resultPost);
        }

    }

    private String bulidParam(ExchangeInfo exchangeInfo) {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();
        Map<String, Object> body = new HashMap<>();

        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("trendId", exchangeInfo.getTrendId());
        body.put("order", "HH");
        body.put("sourceTrendId", exchangeInfo.getSourceTrendId());

        if (exchangeInfo.getStatus().equals("0")) {
            //6 换货失败
            body.put("result", "6");
            body.put("reason", "error");
        } else {
            //5 换货成功
            body.put("result", "5");
            body.put("reason", "success");

        }

        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }

    private String bulidEmptyParam() {
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();
        Map<String, Object> body = new HashMap<>();

        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("trendId", "");
        body.put("order", "HH");
        body.put("result", "6");
        body.put("reason", "nodata");
        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }
}
