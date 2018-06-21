package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;

public class MaestroImportacion extends EntidadBase {
  private int situacion;
  private String id_mimportacion;
  private String numero_guia_remision;
  private String numero_factura;
  private String numero_orden_entrega;
  private String codigo_scop;
  private Date fecha_emision_guia_remision;
  private String codigo_referencia_planta_despacho;
  private String nombre_planta_despacho;
  private String codigo_referencia_planta_recepcion;
  private String nombre_planta_recepcion;
  private String codigo_referencia_cliente;
  private String razon_social_cliente;
  private String codigo_referencia_conductor;
  private String nombre_conductor;
  private String apellido_conductor;
  private String brevete_conductor;
  private String codigo_referencia_cisterna;
  private String tarjeta_cubicacion_cisterna;
  private String placa_cisterna;
  private String codigo_referencia_tracto;
  private String placa_tracto;
  private float volumen_observado_guia;
  private float volumen_corregido_guia;
  private String codigo_referencia_transportista;
  private String razon_social_transportista;
  private String tipo_movimiento;
  private int es_anulada;
  private Date ultima_fecha_actualizacion;
  private ArrayList<DetalleImportacion> detalle;
  private String precintosSeguridad;

  public String getId(){
    return id_mimportacion;
  }

  public void setId(String Id ){
    this.id_mimportacion=Id;
  }
  
  public String getNumeroGuiaRemision(){
    return numero_guia_remision;
  }

  public void setNumeroGuiaRemision(String NumeroGuiaRemision ){
    this.numero_guia_remision=NumeroGuiaRemision;
  }
  
  public String getNumeroFactura(){
    return numero_factura;
  }

  public void setNumeroFactura(String NumeroFactura ){
    this.numero_factura=NumeroFactura;
  }
  
  public String getNumeroOrdenEntrega(){
    return numero_orden_entrega;
  }

  public void setNumeroOrdenEntrega(String NumeroOrdenEntrega ){
    this.numero_orden_entrega=NumeroOrdenEntrega;
  }
  
  public String getCodigoScop(){
    return codigo_scop;
  }

  public void setCodigoScop(String CodigoScop ){
    this.codigo_scop=CodigoScop;
  }
  
  public Date getFechaEmision(){
    return fecha_emision_guia_remision;
  }

  public void setFechaEmision(Date FechaEmision ){
    this.fecha_emision_guia_remision=FechaEmision;
  }
  
  public String getCodigoReferenciaPlantaDespacho(){
    return codigo_referencia_planta_despacho;
  }

  public void setCodigoReferenciaPlantaDespacho(String CodigoReferenciaPlantaDespacho ){
    this.codigo_referencia_planta_despacho=CodigoReferenciaPlantaDespacho;
  }
  
  public String getNombrePlantaDespacho(){
    return nombre_planta_despacho;
  }

  public void setNombrePlantaDespacho(String NombrePlantaDespacho ){
    this.nombre_planta_despacho=NombrePlantaDespacho;
  }
  
  public String getCodigoReferenciaPlantaRecepcion(){
    return codigo_referencia_planta_recepcion;
  }

  public void setCodigoReferenciaPlantaRecepcion(String CodigoReferenciaPlantaRecepcion ){
    this.codigo_referencia_planta_recepcion=CodigoReferenciaPlantaRecepcion;
  }
  
  public String getNombrePlantaRecepcion(){
    return nombre_planta_recepcion;
  }

  public void setNombrePlantaRecepcion(String NombrePlantaRecepcion ){
    this.nombre_planta_recepcion=NombrePlantaRecepcion;
  }
  
  public String getCodigoReferenciaCliente(){
    return codigo_referencia_cliente;
  }

  public void setCodigoReferenciaCliente(String CodigoReferenciaCliente ){
    this.codigo_referencia_cliente=CodigoReferenciaCliente;
  }
  
  public String getRazonSocialCliente(){
    return razon_social_cliente;
  }

  public void setRazonSocialCliente(String RazonSocialCliente ){
    this.razon_social_cliente=RazonSocialCliente;
  }
  
  public String getCodigoReferenciaConductor(){
    return codigo_referencia_conductor;
  }

  public void setCodigoReferenciaConductor(String CodigoReferenciaConductor ){
    this.codigo_referencia_conductor=CodigoReferenciaConductor;
  }
  
  public String getNombreConductor(){
    return nombre_conductor;
  }

  public void setNombreConductor(String NombreConductor ){
    this.nombre_conductor=NombreConductor;
  }
  
  public String getApellidoConductor(){
    return apellido_conductor;
  }

  public void setApellidoConductor(String ApellidoConductor ){
    this.apellido_conductor=ApellidoConductor;
  }
  
  public String getBreveteConductor(){
    return brevete_conductor;
  }

  public void setBreveteConductor(String BreveteConductor ){
    this.brevete_conductor=BreveteConductor;
  }
  
  public String getCodigoReferenciaCisterna(){
    return codigo_referencia_cisterna;
  }

  public void setCodigoReferenciaCisterna(String CodigoReferenciaCisterna ){
    this.codigo_referencia_cisterna=CodigoReferenciaCisterna;
  }
  
  public String getTarjetaCubicacionCisterna(){
    return tarjeta_cubicacion_cisterna;
  }

  public void setTarjetaCubicacionCisterna(String TarjetaCubicacionCisterna ){
    this.tarjeta_cubicacion_cisterna=TarjetaCubicacionCisterna;
  }
  
  public String getPlacaCisterna(){
    return placa_cisterna;
  }

  public void setPlacaCisterna(String PlacaCisterna ){
    this.placa_cisterna=PlacaCisterna;
  }
  
  public String getCodigoReferenciaTracto(){
    return codigo_referencia_tracto;
  }

  public void setCodigoReferenciaTracto(String CodigoReferenciaTracto ){
    this.codigo_referencia_tracto=CodigoReferenciaTracto;
  }
  
  public String getPlacaTracto(){
    return placa_tracto;
  }

  public void setPlacaTracto(String PlacaTracto ){
    this.placa_tracto=PlacaTracto;
  }
  
  public float getVolumenObservadoGuia(){
    return volumen_observado_guia;
  }

  public void setVolumenObservadoGuia(float VolumenObservadoGuia ){
    this.volumen_observado_guia=VolumenObservadoGuia;
  }
  
  public float getVolumenCorregidoGuia(){
    return volumen_corregido_guia;
  }

  public void setVolumenCorregidoGuia(float VolumenCorregidoGuia ){
    this.volumen_corregido_guia=VolumenCorregidoGuia;
  }
  
  public String getCodigoReferenciaTransportista(){
    return codigo_referencia_transportista;
  }

  public void setCodigoReferenciaTransportista(String CodigoReferenciaTransportista ){
    this.codigo_referencia_transportista=CodigoReferenciaTransportista;
  }
  
  public String getRazonSocialTransportista(){
    return razon_social_transportista;
  }

  public void setRazonSocialTransportista(String RazonSocialTransportista ){
    this.razon_social_transportista=RazonSocialTransportista;
  }
  
  public String getTipoMovimiento(){
    return tipo_movimiento;
  }

  public void setTipoMovimiento(String TipoMovimiento ){
    this.tipo_movimiento=TipoMovimiento;
  }
  
  public int getEsAnulada(){
    return es_anulada;
  }

  public void setEsAnulada(int EsAnulada ){
    this.es_anulada=EsAnulada;
  }
  
  public Date getUltimaFechaActualizacion(){
    return ultima_fecha_actualizacion;
  }

  public void setUltimaFechaActualizacion(Date UltimaFechaActualizacion ){
    this.ultima_fecha_actualizacion=UltimaFechaActualizacion;
  }

  /**
   * @return the detalle
   */
  public ArrayList<DetalleImportacion> getDetalle() {
   return detalle;
  }

  /**
   * @param detalle the detalle to set
   */
  public void setDetalle(ArrayList<DetalleImportacion> detalle) {
   this.detalle = detalle;
  }
  
  public void agregarDetalle(DetalleImportacion elemento){
   if (this.detalle==null){
    this.detalle = new ArrayList<DetalleImportacion>();
   }
   this.detalle.add(elemento);
  }

  /**
   * @return the precintosSeguridad
   */
  public String getPrecintosSeguridad() {
   return precintosSeguridad;
  }

  /**
   * @param precintosSeguridad the precintosSeguridad to set
   */
  public void setPrecintosSeguridad(String precintosSeguridad) {
   this.precintosSeguridad = precintosSeguridad;
  }

  /**
   * @return the situacion
   */
  public int getSituacion() {
   return situacion;
  }

  /**
   * @param situacion the situacion to set
   */
  public void setSituacion(int situacion) {
   this.situacion = situacion;
  }
  
  
}