/**
 * WSWSVoucherSoapBindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ormrpc.services.WSWSVoucher;
@SuppressWarnings("rawtypes")
public class WSWSVoucherSoapBindingStub extends org.apache.axis.client.Stub implements com.ormrpc.services.WSWSVoucher.WSWSVoucherSrvProxy {
    
	private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("importVoucher");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "voucherCols"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns1_WSWSVoucher"), com.ormrpc.wsvoucher.client.WSWSVoucher[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isTempSave"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isVerify"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "hasCashflow"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns2_WSWSRtnInfo"));
        oper.setReturnClass(com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "importVoucherReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "fault"),
                "wsvoucher.client.WSInvokeException",
                new javax.xml.namespace.QName("urn:client.wsvoucher", "WSInvokeException"),
                true
        ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("importVoucher");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "voucherCols"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns1_WSWSVoucher"), com.ormrpc.wsvoucher.client.WSWSVoucher[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isVerify"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isImpCashflow"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOfArrayOf_xsd_string"));
        oper.setReturnClass(java.lang.String[][].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "importVoucherReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "fault"),
                "wsvoucher.client.WSInvokeException",
                new javax.xml.namespace.QName("urn:client.wsvoucher", "WSInvokeException"),
                true
        ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("importVoucherOfReturnID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "voucherCols"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns1_WSWSVoucher"), com.ormrpc.wsvoucher.client.WSWSVoucher[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isVerify"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isImpCashflow"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOfArrayOf_xsd_string"));
        oper.setReturnClass(java.lang.String[][].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "importVoucherOfReturnIDReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "fault"),
                "wsvoucher.client.WSInvokeException",
                new javax.xml.namespace.QName("urn:client.wsvoucher", "WSInvokeException"),
                true
        ));
        _operations[2] = oper;

    }

    public WSWSVoucherSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public WSWSVoucherSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    @SuppressWarnings({ "unused", "unchecked" })
	public WSWSVoucherSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns1_WSWSVoucher");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.wsvoucher.client.WSWSVoucher[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:client.wsvoucher", "WSWSVoucher");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOf_tns2_WSWSRtnInfo");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://app.gl.fi.eas.kingdee.com", "WSWSRtnInfo");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://10.43.22.1:7888/ormrpc/services/WSWSVoucher", "ArrayOfArrayOf_xsd_string");
        cachedSerQNames.add(qName);
        cls = java.lang.String[][].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
        qName2 = null;
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://app.gl.fi.eas.kingdee.com", "WSWSRtnInfo");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:client.wsvoucher", "WSBean");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.wsvoucher.client.WSBean.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:client.wsvoucher", "WSInvokeException");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.wsvoucher.client.WSInvokeException.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:client.wsvoucher", "WSWSVoucher");
        cachedSerQNames.add(qName);
        cls = com.ormrpc.wsvoucher.client.WSWSVoucher.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, boolean isTempSave, boolean isVerify, boolean hasCashflow) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.app.gl.fi.eas.kingdee.com", "importVoucher"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{voucherCols, new java.lang.Boolean(isTempSave), new java.lang.Boolean(isVerify), new java.lang.Boolean(hasCashflow)});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ormrpc.kingdee.eas.fi.gl.app.WSWSRtnInfo[].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.ormrpc.wsvoucher.client.WSInvokeException) {
                    throw (com.ormrpc.wsvoucher.client.WSInvokeException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public java.lang.String[][] importVoucher(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.app.gl.fi.eas.kingdee.com", "importVoucher"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{voucherCols, new java.lang.Integer(isVerify), new java.lang.Integer(isImpCashflow)});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (java.lang.String[][]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (java.lang.String[][]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[][].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.ormrpc.wsvoucher.client.WSInvokeException) {
                    throw (com.ormrpc.wsvoucher.client.WSInvokeException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

    public java.lang.String[][] importVoucherOfReturnID(com.ormrpc.wsvoucher.client.WSWSVoucher[] voucherCols, int isVerify, int isImpCashflow) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.app.gl.fi.eas.kingdee.com", "importVoucherOfReturnID"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            java.lang.Object _resp = _call.invoke(new java.lang.Object[]{voucherCols, new java.lang.Integer(isVerify), new java.lang.Integer(isImpCashflow)});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (java.lang.String[][]) _resp;
                } catch (java.lang.Exception _exception) {
                    return (java.lang.String[][]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[][].class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            if (axisFaultException.detail != null) {
                if (axisFaultException.detail instanceof java.rmi.RemoteException) {
                    throw (java.rmi.RemoteException) axisFaultException.detail;
                }
                if (axisFaultException.detail instanceof com.ormrpc.wsvoucher.client.WSInvokeException) {
                    throw (com.ormrpc.wsvoucher.client.WSInvokeException) axisFaultException.detail;
                }
            }
            throw axisFaultException;
        }
    }

}
