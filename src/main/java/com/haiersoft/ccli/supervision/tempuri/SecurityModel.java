/**
 * SecurityModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.haiersoft.ccli.supervision.tempuri;

public class SecurityModel  implements java.io.Serializable {
    private java.lang.String icCode;

    private java.lang.String ticketId;

    private java.lang.String applicationId;

    private java.lang.String signature;

    private java.lang.String privateKey;

    private java.lang.String relCusCode;

    public SecurityModel() {
    }

    public SecurityModel(
           java.lang.String icCode,
           java.lang.String ticketId,
           java.lang.String applicationId,
           java.lang.String signature,
           java.lang.String privateKey,
           java.lang.String relCusCode) {
           this.icCode = icCode;
           this.ticketId = ticketId;
           this.applicationId = applicationId;
           this.signature = signature;
           this.privateKey = privateKey;
           this.relCusCode = relCusCode;
    }


    /**
     * Gets the icCode value for this SecurityModel.
     * 
     * @return icCode
     */
    public java.lang.String getIcCode() {
        return icCode;
    }


    /**
     * Sets the icCode value for this SecurityModel.
     * 
     * @param icCode
     */
    public void setIcCode(java.lang.String icCode) {
        this.icCode = icCode;
    }


    /**
     * Gets the ticketId value for this SecurityModel.
     * 
     * @return ticketId
     */
    public java.lang.String getTicketId() {
        return ticketId;
    }


    /**
     * Sets the ticketId value for this SecurityModel.
     * 
     * @param ticketId
     */
    public void setTicketId(java.lang.String ticketId) {
        this.ticketId = ticketId;
    }


    /**
     * Gets the applicationId value for this SecurityModel.
     * 
     * @return applicationId
     */
    public java.lang.String getApplicationId() {
        return applicationId;
    }


    /**
     * Sets the applicationId value for this SecurityModel.
     * 
     * @param applicationId
     */
    public void setApplicationId(java.lang.String applicationId) {
        this.applicationId = applicationId;
    }


    /**
     * Gets the signature value for this SecurityModel.
     * 
     * @return signature
     */
    public java.lang.String getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this SecurityModel.
     * 
     * @param signature
     */
    public void setSignature(java.lang.String signature) {
        this.signature = signature;
    }


    /**
     * Gets the privateKey value for this SecurityModel.
     * 
     * @return privateKey
     */
    public java.lang.String getPrivateKey() {
        return privateKey;
    }


    /**
     * Sets the privateKey value for this SecurityModel.
     * 
     * @param privateKey
     */
    public void setPrivateKey(java.lang.String privateKey) {
        this.privateKey = privateKey;
    }


    /**
     * Gets the relCusCode value for this SecurityModel.
     * 
     * @return relCusCode
     */
    public java.lang.String getRelCusCode() {
        return relCusCode;
    }


    /**
     * Sets the relCusCode value for this SecurityModel.
     * 
     * @param relCusCode
     */
    public void setRelCusCode(java.lang.String relCusCode) {
        this.relCusCode = relCusCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SecurityModel)) return false;
        SecurityModel other = (SecurityModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.icCode==null && other.getIcCode()==null) || 
             (this.icCode!=null &&
              this.icCode.equals(other.getIcCode()))) &&
            ((this.ticketId==null && other.getTicketId()==null) || 
             (this.ticketId!=null &&
              this.ticketId.equals(other.getTicketId()))) &&
            ((this.applicationId==null && other.getApplicationId()==null) || 
             (this.applicationId!=null &&
              this.applicationId.equals(other.getApplicationId()))) &&
            ((this.signature==null && other.getSignature()==null) || 
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            ((this.privateKey==null && other.getPrivateKey()==null) || 
             (this.privateKey!=null &&
              this.privateKey.equals(other.getPrivateKey()))) &&
            ((this.relCusCode==null && other.getRelCusCode()==null) || 
             (this.relCusCode!=null &&
              this.relCusCode.equals(other.getRelCusCode())));
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
        if (getIcCode() != null) {
            _hashCode += getIcCode().hashCode();
        }
        if (getTicketId() != null) {
            _hashCode += getTicketId().hashCode();
        }
        if (getApplicationId() != null) {
            _hashCode += getApplicationId().hashCode();
        }
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        if (getPrivateKey() != null) {
            _hashCode += getPrivateKey().hashCode();
        }
        if (getRelCusCode() != null) {
            _hashCode += getRelCusCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SecurityModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "SecurityModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IcCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ticketId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "TicketId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ApplicationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("privateKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "PrivateKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relCusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "RelCusCode"));
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
