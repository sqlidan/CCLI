package com.haiersoft.ccli.supervision.service;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.supervision.tempuri.AuthReturnModel;
import com.haiersoft.ccli.supervision.tempuri.RequestContext;
import com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoapProxy;
import com.haiersoft.ccli.supervision.tempuri.SecurityModel;

@Component
public class FljgWsClient {
	private static final Logger log =  LoggerFactory.getLogger(FljgWsClient.class);
	
	public String getResult(JSONObject jsonObject,String tickId,String serviceName) {
		System.out.println(jsonObject.toString());
		return this.getResult(jsonObject.toJSONString(), tickId, serviceName);		
	}
	

	public String getResult(String jsonString,String tickId,String serviceName) {
		
		SasV2ImportServiceSoapProxy sasV2ImportServiceSoapProxy = new SasV2ImportServiceSoapProxy();
        SecurityModel securityModel = new SecurityModel();
        securityModel = login(tickId);
        AuthReturnModel authReturnModel = new AuthReturnModel();
        RequestContext requestContext = new RequestContext();
        requestContext.setRequestObject(jsonString);
        requestContext.setServiceName(serviceName);
        requestContext.setSysName("QingGangFljg");
        requestContext.setApplicationName("fljg");
        
        try {
			authReturnModel = sasV2ImportServiceSoapProxy.sasCommonImport(requestContext, securityModel);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        log.error("msg: "+authReturnModel.getMsg());
        System.out.println("msg: "+authReturnModel.getMsg());
        String flg = authReturnModel.getResponseObject();
        System.out.println("flg： "+flg);
        log.error("flg： "+flg);
//        JSONObject jsonObject3 = JSON.parseObject(flg);
//        String state = jsonObject3.getString("state");
//        System.out.println("state： "+state);
		return flg;
		
	}
	
	public SecurityModel login(String tickId) {
		
		String applicationId = PropertiesUtil.getPropertiesByName("fljgApplicationId", "supervision-ws");
		String privateKey = PropertiesUtil.getPropertiesByName("fljgPrivateKey", "supervision-ws");
		String RelCusCode = PropertiesUtil.getPropertiesByName("fljgRelCusCode", "supervision-ws");
		String IcCode = PropertiesUtil.getPropertiesByName("fljgIcCode", "supervision-ws");
		

		SecurityModel securityModel = new SecurityModel();
		securityModel.setApplicationId(applicationId);
		securityModel.setPrivateKey(privateKey);
		securityModel.setRelCusCode(RelCusCode);// 客户diamante
		securityModel.setIcCode(IcCode);
		
		securityModel.setTicketId(tickId);

		return securityModel;
	}

}
