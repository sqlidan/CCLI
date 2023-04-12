package com.ormrpc.services.WSWSVoucher;

public class WSWSVoucherSrvProxyProxy implements com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxy {
  private String _endpoint = null;
  private com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxy wSWSVoucherSrvProxy = null;
  
  public WSWSVoucherSrvProxyProxy() {
    _initWSWSVoucherSrvProxyProxy();
  }
  
  public WSWSVoucherSrvProxyProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSWSVoucherSrvProxyProxy();
  }
  
  private void _initWSWSVoucherSrvProxyProxy() {
    try {
      wSWSVoucherSrvProxy = (new com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxyServiceLocator()).getWSWSVoucher();
      if (wSWSVoucherSrvProxy != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSWSVoucherSrvProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSWSVoucherSrvProxy)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSWSVoucherSrvProxy != null)
      ((javax.xml.rpc.Stub)wSWSVoucherSrvProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxy getWSWSVoucherSrvProxy() {
    if (wSWSVoucherSrvProxy == null)
      _initWSWSVoucherSrvProxyProxy();
    return wSWSVoucherSrvProxy;
  }
  
  public com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, boolean isTempSave, boolean isVerify, boolean hasCashflow) throws java.rmi.RemoteException {
    if (wSWSVoucherSrvProxy == null)
      _initWSWSVoucherSrvProxyProxy();
    return wSWSVoucherSrvProxy.importVoucher(voucherCols, isTempSave, isVerify, hasCashflow);
  }
  
  public java.lang.String[][] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException {
    if (wSWSVoucherSrvProxy == null)
      _initWSWSVoucherSrvProxyProxy();
    return wSWSVoucherSrvProxy.importVoucher(voucherCols, isVerify, isImpCashflow);
  }
  
  public java.lang.String[][] importVoucherOfReturnID(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException {
    if (wSWSVoucherSrvProxy == null)
      _initWSWSVoucherSrvProxyProxy();
    return wSWSVoucherSrvProxy.importVoucherOfReturnID(voucherCols, isVerify, isImpCashflow);
  }
  
  
}