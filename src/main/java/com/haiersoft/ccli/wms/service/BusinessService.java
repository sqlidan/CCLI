package com.haiersoft.ccli.wms.service;

import com.google.gson.Gson;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.remoting.hand.platform.service.CustomerService;
import com.haiersoft.ccli.wms.entity.BasePlanTime;
import com.haiersoft.ccli.wms.entity.BisPlanTransport;
import com.haiersoft.ccli.wms.entity.RemoteResultBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.stereotype.Service;


/**
 * 电商平台调用远程接口
 */
@Service
public class BusinessService {

    private static final String SUCCESS = "success";

    private static final String FAILED = "failed";

    private static String WSDL;

    private Gson gson = new Gson();

    static {
        WSDL = PropertiesUtil.getPropertiesByName("business_url", "remote-interface-url");
    }

    // -------------- 客户信息操作 --------------

    /**
     * 添加客户
     */
    public String clientAdd(BaseClientInfo clientInfo) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String json = service.addInfo(clientInfo.getIds(),
                clientInfo.getClientCode(),
                clientInfo.getClientName(),
                clientInfo.getClientSort(),
                clientInfo.getContactMan(),
                clientInfo.getTelNum(),
                clientInfo.getAddress(),
                clientInfo.getUserEMail(),
                clientInfo.getRmbBank(),
                clientInfo.getRmbAccount(),
                clientInfo.getUsdBank(),
                clientInfo.getUsdAccount(),
                clientInfo.getTaxAccount(),
                clientInfo.getCheckDay(),
                clientInfo.getTaxpayer(),
                clientInfo.getUseUnit(),
                clientInfo.getSaler(),
                clientInfo.getPledgeType(),
                clientInfo.getDelFlag(),
                clientInfo.getCwNum(),
                clientInfo.getGysNum(),
                clientInfo.getServiceCode(),
                clientInfo.getLimit(),
                clientInfo.getCustomerLevel(),
                clientInfo.getNote());

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;
    }

    /**
     * 修改客户
     *
     * @param clientInfo
     * @return
     */
    public String clientUpdate(BaseClientInfo clientInfo) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String json = service.editInfo(clientInfo.getIds(),
                clientInfo.getClientCode(),
                clientInfo.getClientName(),
                clientInfo.getClientSort(),
                clientInfo.getContactMan(),
                clientInfo.getTelNum(),
                clientInfo.getAddress(),
                clientInfo.getUserEMail(),
                clientInfo.getRmbBank(),
                clientInfo.getRmbAccount(),
                clientInfo.getUsdBank(),
                clientInfo.getUsdAccount(),
                clientInfo.getTaxAccount(),
                clientInfo.getCheckDay(),
                clientInfo.getTaxpayer(),
                clientInfo.getUseUnit(),
                clientInfo.getSaler(),
                clientInfo.getPledgeType(),
                clientInfo.getDelFlag(),
                clientInfo.getCwNum(),
                clientInfo.getGysNum(),
                clientInfo.getServiceCode(),
                clientInfo.getLimit(),
                clientInfo.getCustomerLevel(),
                clientInfo.getNote());

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;

    }

    /**
     * 删除客户
     */
    public String clientDelete(int clientId) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String json = service.deleteInfo(clientId);

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;
    }

    // -------------- 预约时间操作 --------------

    /**
     * 添加预约计划
     */
    public String planTimeAdd(BasePlanTime basePlanTime) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String id = String.valueOf(basePlanTime.getId());

        String json = service.addTime(
                id,
                basePlanTime.getPlanDate(),
                basePlanTime.getLargeNo(),
                basePlanTime.getDescription());

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;
    }

    /**
     * 更新预约计划
     */
    public String planTimeUpdate(BasePlanTime basePlanTime) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String id = String.valueOf(basePlanTime.getId());

        String json = service.editTime(
                id,
                basePlanTime.getPlanDate(),
                basePlanTime.getLargeNo(),
                basePlanTime.getDescription());

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;
    }

    /**
     * 删除预约计划
     */
    public String planTimeDelete(BasePlanTime basePlanTime) {

        if ("".equals(WSDL)) return FAILED;

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String id = String.valueOf(basePlanTime.getId());

        String json = service.deleteTime(id);

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? "success" : msg;
    }

    // -------------- 预约审核操作 --------------

    /**
     * 预约审核
     */
    public String planCheck(BisPlanTransport bisPlanTransport) {

        if ("".equals(WSDL)) return FAILED;

        String data = DateUtils.formatDateTime2(bisPlanTransport.getPlanDate());
        String time = bisPlanTransport.getPlanTime();
        String time1 = time.substring(0, 2);
        String time2 = time.substring(2, 4);
        String newData = data + " " + time1 + ":" + time2 + ":00";

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CustomerService.class);
        factory.setAddress(WSDL);

        CustomerService service = (CustomerService) factory.create();

        String json = service.updateExamine(bisPlanTransport.getBillNum(),
                bisPlanTransport.getCheckDate(),
                bisPlanTransport.getCheckUser(),
                bisPlanTransport.getCheckState(),
                newData);

        RemoteResultBean result = gson.fromJson(json, RemoteResultBean.class);

        String state = result.getState();
        String msg = result.getMsg();

        return "0".equals(state) ? SUCCESS : msg;
    }

}
