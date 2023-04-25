package com.haiersoft.ccli.wms.service;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.stereotype.Component;

import com.haiersoft.ccli.common.utils.PropertiesUtil;

@Component
public class CarInfoToQueue {
	
	public String send(String carNo,String cntNo,String GroupCode) throws RemoteException, ServiceException {
		
//		String code = "chehao08251002";
//		String name = "xianghao00002";

//		String groupCode ="M01G02";		
	
		String priorityCode = "A";
		System.out.println("调用排队接口，接收到的参数是：" + carNo +" "+cntNo +" "+ GroupCode);
		return sendtoWs(carNo,cntNo,priorityCode,GroupCode);
		
	}
	
	private String sendtoWs(String carNo,String cntNo,String priorityCode,String groupCode) throws ServiceException, RemoteException {
		
		String endpoint = PropertiesUtil.getPropertiesByName("carQueueWs", "application"); 
		String targetNamespace = "http://www.nqueue.com/";
		String operationName = "Enqueue";
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		call.setTargetEndpointAddress(endpoint);
		// 设置要返回的数据类型
		// call.setReturnType(new QName(targetNamespace,operationName), String.class);
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		call.setUseSOAPAction(true);
		
		// 设置要调用的方法
		call.setOperationName(new QName(targetNamespace, operationName));
		// 在创建QName对象时，QName类的构造方法的第一个参数表示WSDL文件的命名空间名，也就是<wsdl:definitions>元素的targetNamespace属性值
		call.setSOAPActionURI(targetNamespace + operationName);
		
		// 设置参数值
		call.addParameter(new QName(targetNamespace, "code"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter(new QName(targetNamespace, "name"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter(new QName(targetNamespace, "priorityCode"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter(new QName(targetNamespace, "groupCode"), org.apache.axis.encoding.XMLType.XSD_STRING,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数


		call.setTimeout(30000);
		

		Object[] params = new Object[] { carNo, cntNo, priorityCode, groupCode };// 设置要传递的参数(实参)

		String result = (String) call.invoke(params);
		
		return result;
	}
	

}
