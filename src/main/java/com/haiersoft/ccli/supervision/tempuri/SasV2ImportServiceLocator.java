/**
 * SasV2ImportServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.haiersoft.ccli.supervision.tempuri;

public class SasV2ImportServiceLocator extends org.apache.axis.client.Service implements com.haiersoft.ccli.supervision.tempuri.SasV2ImportService {

    public SasV2ImportServiceLocator() {
    }


    public SasV2ImportServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SasV2ImportServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SasV2ImportServiceSoap
    private java.lang.String SasV2ImportServiceSoap_address = "http://hg.qdcdc.com/Qdcdc.SuperVisionAreaSystemV2.ImportService/SasV2ImportService.asmx";

    public java.lang.String getSasV2ImportServiceSoapAddress() {
        return SasV2ImportServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SasV2ImportServiceSoapWSDDServiceName = "SasV2ImportServiceSoap";

    public java.lang.String getSasV2ImportServiceSoapWSDDServiceName() {
        return SasV2ImportServiceSoapWSDDServiceName;
    }

    public void setSasV2ImportServiceSoapWSDDServiceName(java.lang.String name) {
        SasV2ImportServiceSoapWSDDServiceName = name;
    }

    public com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap getSasV2ImportServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SasV2ImportServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSasV2ImportServiceSoap(endpoint);
    }

    public com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap getSasV2ImportServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoapStub _stub = new com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoapStub(portAddress, this);
            _stub.setPortName(getSasV2ImportServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSasV2ImportServiceSoapEndpointAddress(java.lang.String address) {
        SasV2ImportServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoapStub _stub = new com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoapStub(new java.net.URL(SasV2ImportServiceSoap_address), this);
                _stub.setPortName(getSasV2ImportServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SasV2ImportServiceSoap".equals(inputPortName)) {
            return getSasV2ImportServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "SasV2ImportService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SasV2ImportServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SasV2ImportServiceSoap".equals(portName)) {
            setSasV2ImportServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
