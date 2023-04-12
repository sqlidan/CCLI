package com.haiersoft.ccli.supervision.service;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.stereotype.Component;

import com.haiersoft.ccli.common.utils.PropertiesUtil;

@Component
public class GetKeyService {

	private String key=null;	
	private long lastRunTime=0;
	long timeInterval = 5*1000*60;
	
	//根据参数timeInterval,超出时间间隔或者key为空的时候调用接口生成key
	//减少频繁调用
	public String builder() throws RemoteException, ServiceException {
		long thisTime=System.currentTimeMillis();
		if(key == null||key.equals("") || ((thisTime-lastRunTime)>timeInterval)) {
			key = getKeyWs();
			lastRunTime = System.currentTimeMillis();
		}
		return key;
	}

	private String getKeyWs() throws ServiceException, RemoteException {

		String endpoint = PropertiesUtil.getPropertiesByName("ticketEndpoint", "supervision-ws");
		String targetNamespace = PropertiesUtil.getPropertiesByName("ticketTargetNamespace", "supervision-ws");
		String operationName = PropertiesUtil.getPropertiesByName("ticketOperationName", "supervision-ws");

		System.out.println("endpoint:" + endpoint);
		System.out.println("targetNamespace:" + targetNamespace);
		System.out.println("operationName:" + operationName);
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		call.setTargetEndpointAddress(endpoint);
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
		call.setUseSOAPAction(true);

        //设置要调用的方法
		call.setOperationName(new QName(targetNamespace,operationName));// WSDL里面描述的接口名称
	    // 在创建QName对象时，QName类的构造方法的第一个参数表示WSDL文件的命名空间名，也就是<wsdl:definitions>元素的targetNamespace属性值        
		call.setSOAPActionURI(targetNamespace + operationName);
	
		// 设置参数值
		call.addParameter(new QName(targetNamespace, "serviceUrl"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter(new QName(targetNamespace, "pass"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter(new QName(targetNamespace, "id"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		
		String serviceUrl = PropertiesUtil.getPropertiesByName("ticketServiceUrl", "supervision-ws");
		String pass = PropertiesUtil.getPropertiesByName("ticketPass", "supervision-ws");
		String id = PropertiesUtil.getPropertiesByName("ticketId", "supervision-ws");

		Object[] params = new Object[] { serviceUrl, pass, id };// 设置要传递的参数(实参)

		String result = (String) call.invoke(params);

		// 给方法传递参数，并且调用方法
		System.out.println("result is " + result);

		return result;
	}

	public String builderTest() {
		// String str= RandomStringUtils.randomAlphanumeric(10);
		String str = "889a0525-92e3-4fbb-b7d2-a5b5081054ce";
		return str;
	}

}
