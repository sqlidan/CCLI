package com.haiersoft.ccli.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.util.OpenGateUtil;
import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.dao.ApiPledgeDao;
import com.haiersoft.ccli.api.dao.PledgeDynamicDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.api.entity.ApiPledgeDynamicTransferVO;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.HttpGo;

@Service
public class PledgeDynamicService extends BaseService<ApiPledge, Integer> {

    private static final Logger log = LoggerFactory.getLogger(PledgeDynamicService.class);

    //String url="http://211.97.194.78:10883/api/coldAndPort/sync-dynamic-result";
    String url = OpenGateConstant.OPEN_GATE_SYNC_PLEDGE_DYNAMIC_URL;

    @Autowired
    ApiPledgeDao apiPledgeDao;

    @Autowired
    private PledgeDynamicDao pledgeDynamicDao;
    @Autowired
    private HttpGo httpGo;


    @Override
    public HibernateDao<ApiPledge, Integer> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 按id值查询抵押表
     *
     * @param id id值
     * @return 抵押表对象
     */
    @Async
    public void getApiPledge(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        String params;
        ApiPledge apiPledge = apiPledgeDao.find(id);
        if (apiPledge != null) {
            params = this.passParam(apiPledge);

        } else {
            params = this.emptyParamBuilder();
        }
        log.error("同步动态质押监管 通过 :::{}",params);
        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), params);
        if (resultPost == null) {
            log.error("传输失败，没有返回值");
        } else {
            log.error(resultPost);
        }

    }

    /**
     * Fail时按id值查询抵押表
     *
     * @param id id值
     * @return 抵押表对象
     */
    @Async
    public void getApiPledgeFail(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }

        ApiPledge apiPledge = apiPledgeDao.find(id);
        String param;
        if (apiPledge != null) {
            param = this.refuseParam(apiPledge);

        } else {
            param = this.emptyParamBuilder();
        }
        log.error("同步动态质押监管 驳回 :::{}",param);
        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), param);
        if (resultPost == null) {
            log.error("传输失败，没有返回值");
        } else {
            log.error(resultPost);
        }

    }

    private String passParam(ApiPledge apiPledge) {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();
        Map<String, Object> body = new HashMap<>();
        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("lowAmount", apiPledge.getPledgeNumber().toString());
        body.put("lowWeight", apiPledge.getPledgeWeight().toString());
        body.put("trendId", apiPledge.getTrendId());
        if (apiPledge.getState().equals(3)) {
            body.put("order", "JCZYJG");
            //3 解除质押监管成功
            body.put("result", "3");
            body.put("reason", "success");
        } else {
            body.put("order", "ZYJG");
            //1 质押监管成功
            body.put("result", "1");
            body.put("reason", "success");
        }
        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }

    private String refuseParam(ApiPledge apiPledge) {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();
        Map<String, Object> body = new HashMap<>();
        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("lowAmount", apiPledge.getPledgeNumber().toString());
        body.put("lowWeight", apiPledge.getPledgeWeight().toString());
        body.put("trendId", apiPledge.getTrendId());
        if (apiPledge.getState().equals(0)) {
            body.put("order", "JCZYJG");
            //4 解除质押监管失败
            body.put("result", "4");
            body.put("reason", "error");
        } else {
            body.put("order", "ZYJG");
            //2 质押监管失败
            body.put("result", "2");
            body.put("reason", "error");
        }
        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }

    private String emptyParamBuilder() {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();

        Map<String, Object> body = new HashMap<>();
        body.put("traceId", header.get("requestId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("trendId", "");
        body.put("lowAmount", "00");
        body.put("lowWeight", "0.0");
        body.put("order", "");
        body.put("result", "error");
        body.put("reason", "no data");


        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }
}
