/**
 * Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.servicio;

public class Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator extends org.apache.axis.client.Service implements sgo.ws.sap.servicio.Petroperu_BC_SWCC_ConsInformacionEntregas_Out {

    public static final String USUARIO="PISGOUSER";
    //IA
     //public static final String CLAVE="P3tr0p3ru!";
    //IA public static final String CLAVE="P3tr0p3ru";
    public static final String CLAVE="P3tr0p3ru042016";
    //IA
     
     
    public Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator() {
    }


    public Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for HTTPS_Port
    //IA
    //private java.lang.String HTTPS_Port_address = "https://petpidqc:50201/XISOAPAdapter/MessageServlet?senderParty=Petroperu&senderService=BC_SWCC&receiverParty=&receiverService=&interface=ConsInformacionEntregas_Out&interfaceNamespace=urn%3Apetroperu.com.pe%3Apmerp%3Aswcc%3Asgo_informacion_entregas";
                                                     
    //IA
    private java.lang.String HTTPS_Port_address = "https://pepinprc.petroperu.com.pe/XISOAPAdapter/MessageServlet?senderParty=Petroperu&senderService=BC_SWCC&receiverParty=&receiverService=&interface=ConsInformacionEntregas_Out&interfaceNamespace=urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas";    


    public java.lang.String getHTTPS_PortAddress() {
        return HTTPS_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String HTTPS_PortWSDDServiceName = "HTTPS_Port";

    public java.lang.String getHTTPS_PortWSDDServiceName() {
        return HTTPS_PortWSDDServiceName;
    }

    public void setHTTPS_PortWSDDServiceName(java.lang.String name) {
        HTTPS_PortWSDDServiceName = name;
    }

    public sgo.ws.sap.servicio.ConsInformacionEntregas_Out getHTTPS_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(HTTPS_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getHTTPS_Port(endpoint);
    }

    public sgo.ws.sap.servicio.ConsInformacionEntregas_Out getHTTPS_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub _stub = new sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub(portAddress, this);
            _stub.setPortName(getHTTPS_PortWSDDServiceName());
            _stub.setUsername(USUARIO);
            _stub.setPassword(CLAVE);
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setHTTPS_PortEndpointAddress(java.lang.String address) {
        HTTPS_Port_address = address;
    }


    // Use to get a proxy class for HTTP_Port
    //IA
    //private java.lang.String HTTP_Port_address = "http://petpidqc:50200/XISOAPAdapter/MessageServlet?senderParty=Petroperu&senderService=BC_SWCC&receiverParty=&receiverService=&interface=ConsInformacionEntregas_Out&interfaceNamespace=urn%3Apetroperu.com.pe%3Apmerp%3Aswcc%3Asgo_informacion_entregas";
    //private java.lang.String HTTP_Port_address = "http://PEPINPRC:50600/XISOAPAdapter/MessageServlet?senderParty=Petroperu&senderService=BC_SWCC&receiverParty=&receiverService=&interface=ConsInformacionEntregas_Out&interfaceNamespace=urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas";
    private java.lang.String HTTP_Port_address =   "http://PEPINPRC:50600/XISOAPAdapter/MessageServlet?senderParty=Petroperu&senderService=BC_SWCC&receiverParty=&receiverService=&interface=ConsInformacionEntregas_Out&interfaceNamespace=urn%3Apetroperu.com.pe%3Apmerp%3Aswcc%3Asgo_informacion_entregas";
    //IA
    
    public java.lang.String getHTTP_PortAddress() {
        return HTTP_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String HTTP_PortWSDDServiceName = "HTTP_Port";

    public java.lang.String getHTTP_PortWSDDServiceName() {
        return HTTP_PortWSDDServiceName;
    }

    public void setHTTP_PortWSDDServiceName(java.lang.String name) {
        HTTP_PortWSDDServiceName = name;
    }

    public sgo.ws.sap.servicio.ConsInformacionEntregas_Out getHTTP_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(HTTP_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getHTTP_Port(endpoint);
    }

    public sgo.ws.sap.servicio.ConsInformacionEntregas_Out getHTTP_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub _stub = new sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub(portAddress, this);
            _stub.setPortName(getHTTP_PortWSDDServiceName());
            _stub.setUsername(USUARIO);
            _stub.setPassword(CLAVE);
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setHTTP_PortEndpointAddress(java.lang.String address) {
        HTTP_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (sgo.ws.sap.servicio.ConsInformacionEntregas_Out.class.isAssignableFrom(serviceEndpointInterface)) {
                sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub _stub = new sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub(new java.net.URL(HTTPS_Port_address), this);
                _stub.setPortName(getHTTPS_PortWSDDServiceName());
                _stub.setUsername(USUARIO);
                _stub.setPassword(CLAVE);
                return _stub;
            }
            if (sgo.ws.sap.servicio.ConsInformacionEntregas_Out.class.isAssignableFrom(serviceEndpointInterface)) {
                sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub _stub = new sgo.ws.sap.servicio.ConsInformacionEntregas_OutBindingStub(new java.net.URL(HTTP_Port_address), this);
                _stub.setPortName(getHTTP_PortWSDDServiceName());
                _stub.setUsername(USUARIO);
                _stub.setPassword(CLAVE);
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
        if ("HTTPS_Port".equals(inputPortName)) {
            return getHTTPS_Port();
        }
        else if ("HTTP_Port".equals(inputPortName)) {
            return getHTTP_Port();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", "Petroperu_BC_SWCC_ConsInformacionEntregas_Out");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", "HTTPS_Port"));
            ports.add(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:swcc:sgo_informacion_entregas", "HTTP_Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("HTTPS_Port".equals(portName)) {
            setHTTPS_PortEndpointAddress(address);
        }
        else 
if ("HTTP_Port".equals(portName)) {
            setHTTP_PortEndpointAddress(address);
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
