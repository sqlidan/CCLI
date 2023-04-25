/**
 * AuthReturnModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.haiersoft.ccli.supervision.tempuri;

public class AuthReturnModel  extends ReturnModel  implements java.io.Serializable {
    private java.lang.String responseObject;

    public AuthReturnModel() {
    }

    public AuthReturnModel(
           int returnCode,
           java.lang.String msg,
           java.lang.String obj,
           java.lang.String responseObject) {
        super(
            returnCode,
            msg,
            obj);
        this.responseObject = responseObject;
    }


    /**
     * Gets the responseObject value for this AuthReturnModel.
     * 
     * @return responseObject
     */
    public java.lang.String getResponseObject() {
        return responseObject;
    }


    /**
     * Sets the responseObject value for this AuthReturnModel.
     * 
     * @param responseObject
     */
    public void setResponseObject(java.lang.String responseObject) {
        this.responseObject = responseObject;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AuthReturnModel)) return false;
        AuthReturnModel other = (AuthReturnModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.responseObject==null && other.getResponseObject()==null) || 
             (this.responseObject!=null &&
              this.responseObject.equals(other.getResponseObject())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getResponseObject() != null) {
            _hashCode += getResponseObject().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AuthReturnModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "AuthReturnModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseObject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ResponseObject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
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
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
