package com.haiersoft.ccli.supervision.tempuri;

public class SasV2ImportServiceSoapProxy implements com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap {
  private String _endpoint = null;
  private com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap sasV2ImportServiceSoap = null;
  
  public SasV2ImportServiceSoapProxy() {
    _initSasV2ImportServiceSoapProxy();
  }
  
  public SasV2ImportServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSasV2ImportServiceSoapProxy();
  }
  
  private void _initSasV2ImportServiceSoapProxy() {
    try {
      sasV2ImportServiceSoap = (new com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceLocator()).getSasV2ImportServiceSoap();
      if (sasV2ImportServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sasV2ImportServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sasV2ImportServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sasV2ImportServiceSoap != null)
      ((javax.xml.rpc.Stub)sasV2ImportServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.haiersoft.ccli.supervision.tempuri.SasV2ImportServiceSoap getSasV2ImportServiceSoap() {
    if (sasV2ImportServiceSoap == null)
      _initSasV2ImportServiceSoapProxy();
    return sasV2ImportServiceSoap;
  }
  
  public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel sasV2Import(com.haiersoft.ccli.supervision.tempuri.RequestContext request, com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException{
    if (sasV2ImportServiceSoap == null)
      _initSasV2ImportServiceSoapProxy();
    return sasV2ImportServiceSoap.sasV2Import(request, securityModel);
  }
  
  public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel sasCommonImport(com.haiersoft.ccli.supervision.tempuri.RequestContext request, com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException{
    if (sasV2ImportServiceSoap == null)
      _initSasV2ImportServiceSoapProxy();
    return sasV2ImportServiceSoap.sasCommonImport(request, securityModel);
  }
  
  public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel loginTest(com.haiersoft.ccli.supervision.tempuri.SecurityModel securityModel) throws java.rmi.RemoteException{
    if (sasV2ImportServiceSoap == null)
      _initSasV2ImportServiceSoapProxy();
    return sasV2ImportServiceSoap.loginTest(securityModel);
  }
  
  public com.haiersoft.ccli.supervision.tempuri.AuthReturnModel importTest() throws java.rmi.RemoteException{
    if (sasV2ImportServiceSoap == null)
      _initSasV2ImportServiceSoapProxy();
    return sasV2ImportServiceSoap.importTest();
  }
  
  
}