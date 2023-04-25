/**
 * SasV2ImportServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.haiersoft.ccli.supervision.tempuri;

public interface SasV2ImportServiceSoap extends java.rmi.Remote {
    public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel sasV2Import(com.haiersoft.ccli.supervision.tempuri.RequestContext request, com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException;
    public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel sasCommonImport(com.haiersoft.ccli.supervision.tempuri.RequestContext request, com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException;
    public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel loginTest(com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException;
    public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel importTest() throws java.rmi.RemoteException;
}
