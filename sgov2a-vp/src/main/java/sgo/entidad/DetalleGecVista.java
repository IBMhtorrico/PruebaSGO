package sgo.entidad;

import java.sql.Date;

public class DetalleGecVista {
 private int id_transporte;
 private int id_dcisterna;
 private String numero_guia_remision;
 private Date fecha_arribo;
 private Date fecha_fiscalizacion;
 private float volumen_despachado_observado;
 private float volumen_despachado_corregido;
 private int estado_dia_operativo;
 private Date fecha_operativa;
 private Date fecha_emision;
 private float volumen_recibido_observado;
 private float volumen_recibido_corregido;
 private int id_producto;
 private int id_transportista;
 /**
  * @return the id_transporte
  */
 public int getIdTransporte() {
  return id_transporte;
 }
 /**
  * @param id_transporte the id_transporte to set
  */
 public void setIdTransporte(int id_transporte) {
  this.id_transporte = id_transporte;
 }
 /**
  * @return the id_dcisterna
  */
 public int getIdDcisterna() {
  return id_dcisterna;
 }
 /**
  * @param id_dcisterna the id_dcisterna to set
  */
 public void setIdDcisterna(int id_dcisterna) {
  this.id_dcisterna = id_dcisterna;
 }
 /**
  * @return the numero_guia_remision
  */
 public String getNumeroGuiaRemision() {
  return numero_guia_remision;
 }
 /**
  * @param numero_guia_remision the numero_guia_remision to set
  */
 public void setNumeroGuiaRemision(String numero_guia_remision) {
  this.numero_guia_remision = numero_guia_remision;
 }
 /**
  * @return the fecha_arribo
  */
 public Date getFechaArribo() {
  return fecha_arribo;
 }
 /**
  * @param fecha_arribo the fecha_arribo to set
  */
 public void setFechaArribo(Date fecha_arribo) {
  this.fecha_arribo = fecha_arribo;
 }
 /**
  * @return the fecha_fiscalizacion
  */
 public Date getFechaFiscalizacion() {
  return fecha_fiscalizacion;
 }
 /**
  * @param fecha_fiscalizacion the fecha_fiscalizacion to set
  */
 public void setFechaFiscalizacion(Date fecha_fiscalizacion) {
  this.fecha_fiscalizacion = fecha_fiscalizacion;
 }
 /**
  * @return the volumen_despachado_observado
  */
 public float getVolumenDespachadoObservado() {
  return volumen_despachado_observado;
 }
 /**
  * @param volumen_despachado_observado the volumen_despachado_observado to set
  */
 public void setVolumenDespachadoObservado(float volumen_despachado_observado) {
  this.volumen_despachado_observado = volumen_despachado_observado;
 }
 /**
  * @return the volumen_despachado_corregido
  */
 public float getVolumenDespachadoCorregido() {
  return volumen_despachado_corregido;
 }
 /**
  * @param volumen_despachado_corregido the volumen_despachado_corregido to set
  */
 public void setVolumenDespachadoCorregido(float volumen_despachado_corregido) {
  this.volumen_despachado_corregido = volumen_despachado_corregido;
 }
 /**
  * @return the estado_dia_operativo
  */
 public int getEstadoDiaOperativo() {
  return estado_dia_operativo;
 }
 /**
  * @param estado_dia_operativo the estado_dia_operativo to set
  */
 public void setEstadoDiaOperativo(int estado_dia_operativo) {
  this.estado_dia_operativo = estado_dia_operativo;
 }
 /**
  * @return the fecha_operativa
  */
 public Date getFechaOperativa() {
  return fecha_operativa;
 }
 /**
  * @param fecha_operativa the fecha_operativa to set
  */
 public void setFechaOperativa(Date fecha_operativa) {
  this.fecha_operativa = fecha_operativa;
 }
 /**
  * @return the fecha_emision
  */
 public Date getFechaEmision() {
  return fecha_emision;
 }
 /**
  * @param fecha_emision the fecha_emision to set
  */
 public void setFechaEmision(Date fecha_emision) {
  this.fecha_emision = fecha_emision;
 }
 /**
  * @return the volumen_recibido_observado
  */
 public float getVolumenRecibidoObservado() {
  return volumen_recibido_observado;
 }
 /**
  * @param volumen_recibido_observado the volumen_recibido_observado to set
  */
 public void setVolumenRecibidoObservado(float volumen_recibido_observado) {
  this.volumen_recibido_observado = volumen_recibido_observado;
 }
 /**
  * @return the volumen_recibido_corregido
  */
 public float getVolumenRecibidoCorregido() {
  return volumen_recibido_corregido;
 }
 /**
  * @param volumen_recibido_corregido the volumen_recibido_corregido to set
  */
 public void setVolumenRecibidoCorregido(float volumen_recibido_corregido) {
  this.volumen_recibido_corregido = volumen_recibido_corregido;
 }
 /**
  * @return the id_producto
  */
 public int getIdProducto() {
  return id_producto;
 }
 /**
  * @param id_producto the id_producto to set
  */
 public void setIdProducto(int id_producto) {
  this.id_producto = id_producto;
 }
 /**
  * @return the id_transportista
  */
 public int getIdTransportista() {
  return id_transportista;
 }
 /**
  * @param id_transportista the id_transportista to set
  */
 public void setIdTransportista(int id_transportista) {
  this.id_transportista = id_transportista;
 }
}
