package sgo.ws.sap.servicio;

public class ConsInformacionEntregas_OutProxy implements sgo.ws.sap.servicio.ConsInformacionEntregas_Out {
  private String _endpoint = null;
  private sgo.ws.sap.servicio.ConsInformacionEntregas_Out consInformacionEntregas_Out = null;
  
  public ConsInformacionEntregas_OutProxy() {
    _initConsInformacionEntregas_OutProxy();
  }
  
  public ConsInformacionEntregas_OutProxy(String endpoint) {
    _endpoint = endpoint;
    _initConsInformacionEntregas_OutProxy();
  }
  
  private void _initConsInformacionEntregas_OutProxy() {
    try {
      consInformacionEntregas_Out = (new sgo.ws.sap.servicio.Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator()).getHTTPS_Port();
      if (consInformacionEntregas_Out != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)consInformacionEntregas_Out)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)consInformacionEntregas_Out)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (consInformacionEntregas_Out != null)
      ((javax.xml.rpc.Stub)consInformacionEntregas_Out)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public sgo.ws.sap.servicio.ConsInformacionEntregas_Out getConsInformacionEntregas_Out() {
    if (consInformacionEntregas_Out == null)
      _initConsInformacionEntregas_OutProxy();
    return consInformacionEntregas_Out;
  }
  
  public sgo.ws.sap.parametros.Rpta_ConInfEntCabecera[] consInformacionEntregas_Out(sgo.ws.sap.parametros.ConInfEnt conInfEnt) throws java.rmi.RemoteException, sgo.ws.sap.comun.ZPI_Fault{
    if (consInformacionEntregas_Out == null)
      _initConsInformacionEntregas_OutProxy();
    return consInformacionEntregas_Out.consInformacionEntregas_Out(conInfEnt);
  }
  
  
}