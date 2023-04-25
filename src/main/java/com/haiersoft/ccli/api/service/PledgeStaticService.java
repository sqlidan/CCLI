package com.haiersoft.ccli.api.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.util.OpenGateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.dao.ApiPledgeDao;
import com.haiersoft.ccli.api.dao.PledgeStaticDao;
import com.haiersoft.ccli.api.entity.ApiPledge;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.utils.StringUtils;

@Service
public class PledgeStaticService extends BaseService<ApiPledge, Integer> {
    private static final Logger log = LoggerFactory.getLogger(PledgeStaticService.class);

    //String url="http://211.97.194.78:10883/api/coldAndPort/sync-static-result";
    //String url="http://10.199.18.10:10883/api/coldAndPort/sync-static-result";
    String url = OpenGateConstant.OPEN_GATE_SYNC_PLEDGE_STATIC_URL;
    @Autowired
    private PledgeStaticDao pledgeStaticDao;

    @Autowired
    ApiPledgeDao apiPledgeDao;

    @Autowired
    private HttpGo httpGo;

    @Override
    public HibernateDao<ApiPledge, Integer> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 同步静态质押监管（解除）指令结果
     *
     * @param id id值
     * @return 抵押表对象
     */
    @Async
    public void getApiPledge(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }

        ApiPledge apiPledge = apiPledgeDao.find(id);
        String param;
        if (apiPledge != null) {
            param = this.passParam(apiPledge);
        } else {
            param = this.emptyParamBuilder();

        }
        log.error("同步静态质押监管 通过:::{}",param);

        String resultPost = httpGo.sendRequest(url, OpenGateUtil.headerBuilder(), param);
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
        log.error("同步静态质押监管 驳回 :::{}",param);
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

        Map<String, Object> body = this.bodyBuilder(header, apiPledge);
        // 3 解除质押监管成功
        if (apiPledge.getState().equals(0)) {
            body.put("order", "JCZYJG");
            body.put("result", "3");
        }
        // 1 质押监管成功
        else {
            body.put("order", "ZYJG");
            body.put("result", "1");

        }
        body.put("reason", "success");
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

        Map<String, Object> body = this.bodyBuilder(header, apiPledge);
        // 4 解除质押监管失败
        if (apiPledge.getState().equals(0)) {
            body.put("order", "JCZYJG");
            body.put("result", "4");
        }
        // 2 质押监管失败
        else {
            body.put("order", "ZYJG");
            body.put("result", "2");

        }
        body.put("reason", "refuse");
        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);

        String paramJson = JSON.toJSONString(param);

        return paramJson;
    }

    private Map<String, Object> bodyBuilder(Map<String, Object> header, ApiPledge apiPledge) {

        Map<String, Object> body = new HashMap<>();

        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("trendId", apiPledge.getTrendId());

        if (apiPledge.getCustomerId() != null) {
            BaseClientInfo baseClientInfo = pledgeStaticDao.QueryClientInfo(apiPledge.getCustomerId());
            if (baseClientInfo != null) {
                body.put("userNo", baseClientInfo.getClientCode());
                body.put("userName", baseClientInfo.getClientName());
            } else {
                body.put("userNo", "");
                body.put("userName", "");
            }
        }
        body.put("watchAmount", apiPledge.getPledgeNumber());
        body.put("watchWeight", apiPledge.getPledgeWeight());

        return body;

    }

    private String emptyParamBuilder() {
        //请求body部分
        //注意这里的body部分分为head和body 两段
        Map<String, Object> header = OpenGateUtil.headerOfBodyBuilder();

        Map<String, Object> body = new HashMap<>();
        body.put("traceId", header.get("traceId"));
        body.put("channel", OpenGateConstant.CHANNEL);
        body.put("operationUserId", OpenGateConstant.USER_ID);
        body.put("requestTime", header.get("requestTime"));
        body.put("chanType", OpenGateConstant.CHAN_TYPE);
        body.put("trendId", "");
        body.put("userNo", "");
        body.put("userName", "");
        body.put("watchAmount", 00);
        body.put("watchWeight", 0.0);
        body.put("reason", "no data");
        body.put("result", "error");

        Map<String, Map<String, Object>> param = new HashMap<>();
        param.put("header", header);
        param.put("body", body);
        String paramJson = JSON.toJSONString(param);
        return paramJson;
    }


}
