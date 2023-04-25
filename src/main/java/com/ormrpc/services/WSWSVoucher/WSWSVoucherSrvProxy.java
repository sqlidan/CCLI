/**
 * WSWSVoucherSrvProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ormrpc.services.WSWSVoucher;

public interface WSWSVoucherSrvProxy extends java.rmi.Remote {
    com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, boolean isTempSave, boolean isVerify, boolean hasCashflow) throws java.rmi.RemoteException;
    java.lang.String[][] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException;
    java.lang.String[][] importVoucherOfReturnID(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException;
}
