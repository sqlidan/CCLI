/**
 * WSBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.ormrpc.wsvoucher.client;
import java.util.ArrayList;
import java.util.List;
public class WSBean  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8933737642829273486L;
	
	public WSBean() {
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WSBean)) return false;
        WSBean other = (WSBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true;
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WSBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:client.wsvoucher", "WSBean"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    @SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    @SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }
    
    public static void main(String[] args) {
    	List<String> list=new ArrayList<String>();
    	for (int i = 0; i <10; i++) {
    		list.add("PC");
		}
    	System.out.println(list.size());
    	System.out.println(list.contains("PC"));
    	System.out.println(list.contains("pc"));
    	//int a=157491/1000;
    	//int b=157491%1000;
    	//System.out.println(a);
    	//System.out.println(b);
    	//String str="{\"cmd\" : \"set_adminpass\",\"id\" : \"132156\",\"body\":{\"old_pass\":\"asgwe4AGSAD45\",\"new_pass\":\"fdas213asfdgad\"}}";
    	
    	/*String getsnCmd = "{\"cmd\" :\"getsn\"}";
    	 Socket socket;
		try {
			 socket = new Socket("127.0.0.1",8131);
			 sendCmd(socket, getsnCmd);
	     	 int sn_len = recvPacketSize(socket);
	         if(sn_len > 0)
	         {
	             //接收实际数据
	             byte[] data = new byte[sn_len];
	             int recvLen = recvBlock(socket, data, sn_len);
	             String sn = new String(data, 0, recvLen);
	             System.out.println(sn);
	         }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
