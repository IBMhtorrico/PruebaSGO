package sgo.entidad;

public class DescargaResumen {
 private int id_dcisterna;
 private int id_ctanque;
 private int id_transporte;
 private String placa_tracto;
 private String placa_cisterna;
 private String numero_guia;
 private float despachado;
 private float recibido;
 private float variacion;
 private int estado;
 public int getId() {
  return id_dcisterna;
 }
 public void setId(int id_dcisterna) {
  this.id_dcisterna = id_dcisterna;
 }
 public int getIdCargaTanque() {
  return id_ctanque;
 }
 public void setIdCargaTanque(int id_ctanque) {
  this.id_ctanque = id_ctanque;
 }
 public int getIdTransporte() {
  return id_transporte;
 }
 public void setIdTransporte(int id_transporte) {
  this.id_transporte = id_transporte;
 }
 public String getPlacaTracto() {
  return placa_tracto;
 }
 public void setPlacaTracto(String placa_tracto) {
  this.placa_tracto = placa_tracto;
 }
 public String getPlacaCisterna() {
  return placa_cisterna;
 }
 public void setPlacaCisterna(String placa_cisterna) {
  this.placa_cisterna = placa_cisterna;
 }
 public String getNumeroGuia() {
  return numero_guia;
 }
 public void setNumeroGuia(String numero_guia) {
  this.numero_guia = numero_guia;
 }
 public float getDespachado() {
  return despachado;
 }
 public void setDespachado(float despachado) {
  this.despachado = despachado;
 }
 public float getRecibido() {
  return recibido;
 }
 public void setRecibido(float recibido) {
  this.recibido = recibido;
 }
 public float getVariacion() {
  return variacion;
 }
 public void setVariacion(float variacion) {
  this.variacion = variacion;
 }
 /**
  * @return the estado
  */
 public int getEstado() {
  return estado;
 }
 /**
  * @param estado the estado to set
  */
 public void setEstado(int estado) {
  this.estado = estado;
 }
}
