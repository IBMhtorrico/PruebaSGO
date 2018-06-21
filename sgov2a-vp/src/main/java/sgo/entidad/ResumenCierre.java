package sgo.entidad;

import java.sql.Date;

public class ResumenCierre {
 private String id;
 private int idDiaOperativo;
 private int idTracto;
 private int idCisterna;
 private int idTransportista;
 private int idTransporte;
 private int idEstacion;
 private int metodoDescarga;
 private float entradaTotal;
 private float salidaTotal;
 private float variacion;
 private float limitePermisible;
 private float resultado;
 private String placaTracto;
 private String placaCisterna;
 private String razonSocialTransportista;
 private String nombreEstacion;
 private int idOperacion;
 private Date fechaOperativa;
 private int estadoDescarga;
 private float volumenObservadoDespachado;
 private float volumenCorregidoDespachado;
 private float volumenObservadoRecibido;
 private float volumenCorregidoRecibido;
 private String descripcion;
 
public String getNombreMetodo(){
 String nombreMetodo="";
 if (this.metodoDescarga ==  DescargaCisterna.METODO_WINCHA){
  nombreMetodo= "WINCHA";
 }
 if (this.metodoDescarga ==  DescargaCisterna.METODO_BALANZA){
  nombreMetodo= "BALANZA";
 }
 if (this.metodoDescarga ==  DescargaCisterna.METODO_CONTOMETRO){
  nombreMetodo= "CONTOMETRO";
 }
 return nombreMetodo;
}

public String getNombreEstado(){
 String nombreMetodo="";
 if (this.estadoDescarga ==  DescargaCisterna.ESTADO_ACTIVO){
  nombreMetodo= "ACTIVO";
 }
 if (this.estadoDescarga ==  DescargaCisterna.ESTADO_INACTIVO){
  nombreMetodo= "INACTIVO";
 }
 if (this.estadoDescarga ==  DescargaCisterna.ESTADO_OBSERVADO){
  nombreMetodo= "OBSERVADO";
 }
 return nombreMetodo;
}


 
 public float getVolumenObservadoDespachado() {
  return volumenObservadoDespachado;
 }

 public void setVolumenObservadoDespachado(float volumenObservadoDespachado) {
  this.volumenObservadoDespachado = volumenObservadoDespachado;
 }

 public float getVolumenCorregidoDespachado() {
  return volumenCorregidoDespachado;
 }

 public void setVolumenCorregidoDespachado(float volumenCorregidoDespachado) {
  this.volumenCorregidoDespachado = volumenCorregidoDespachado;
 }

 public float getVolumenObservadoRecibido() {
  return volumenObservadoRecibido;
 }

 public void setVolumenObservadoRecibido(float volumenObservadoRecibido) {
  this.volumenObservadoRecibido = volumenObservadoRecibido;
 }

 public float getVolumenCorregidoRecibido() {
  return volumenCorregidoRecibido;
 }

 public void setVolumenCorregidoRecibido(float volumenCorregidoRecibido) {
  this.volumenCorregidoRecibido = volumenCorregidoRecibido;
 }
 public String getTractoCisterna(){
  return this.placaTracto + " / " + this.placaCisterna;
 }
 
 public int getIdDiaOperativo() {
  return idDiaOperativo;
 }
 public void setIdDiaOperativo(int idDiaOperativo) {
  this.idDiaOperativo = idDiaOperativo;
 }
 public int getIdTracto() {
  return idTracto;
 }
 public void setIdTracto(int idTracto) {
  this.idTracto = idTracto;
 }
 public int getIdCisterna() {
  return idCisterna;
 }
 public void setIdCisterna(int idCisterna) {
  this.idCisterna = idCisterna;
 }
 public int getIdTransportista() {
  return idTransportista;
 }
 public void setIdTransportista(int idTransportista) {
  this.idTransportista = idTransportista;
 }
 public int getIdTransporte() {
  return idTransporte;
 }
 public void setIdTransporte(int idTransporte) {
  this.idTransporte = idTransporte;
 }
 public int getIdEstacion() {
  return idEstacion;
 }
 public void setIdEstacion(int idEstacion) {
  this.idEstacion = idEstacion;
 }
 public int getMetodoDescarga() {
  return metodoDescarga;
 }
 public void setMetodoDescarga(int metodoDescarga) {
  this.metodoDescarga = metodoDescarga;
 }
 public float getEntradaTotal() {
  return entradaTotal;
 }
 public void setEntradaTotal(float entradaTotal) {
  this.entradaTotal = entradaTotal;
 }
 public float getSalidaTotal() {
  return salidaTotal;
 }
 public void setSalidaTotal(float salidaTotal) {
  this.salidaTotal = salidaTotal;
 }
 public float getVariacion() {
  return variacion;
 }
 public void setVariacion(float variacion) {
  this.variacion = variacion;
 }
 public float getLimitePermisible() {
  return limitePermisible;
 }
 public void setLimitePermisible(float limitePermisible) {
  this.limitePermisible = limitePermisible;
 }
 public float getResultado() {
  return resultado;
 }
 public void setResultado(float resultado) {
  this.resultado = resultado;
 }
 public String getPlacaTracto() {
  return placaTracto;
 }
 public void setPlacaTracto(String placaTracto) {
  this.placaTracto = placaTracto;
 }
 public String getPlacaCisterna() {
  return placaCisterna;
 }
 public void setPlacaCisterna(String placaCisterna) {
  this.placaCisterna = placaCisterna;
 }
 public String getRazonSocialTransportista() {
  return razonSocialTransportista;
 }
 public void setRazonSocialTransportista(String razonSocialTransportista) {
  this.razonSocialTransportista = razonSocialTransportista;
 }
 public String getNombreEstacion() {
  return nombreEstacion;
 }
 public void setNombreEstacion(String nombreEstacion) {
  this.nombreEstacion = nombreEstacion;
 }
 /**
  * @return the idOperacion
  */
 public int getIdOperacion() {
  return idOperacion;
 }
 /**
  * @param idOperacion the idOperacion to set
  */
 public void setIdOperacion(int idOperacion) {
  this.idOperacion = idOperacion;
 }
 /**
  * @return the fechaOperativa
  */
 public Date getFechaOperativa() {
  return fechaOperativa;
 }
 /**
  * @param fechaOperativa the fechaOperativa to set
  */
 public void setFechaOperativa(Date fechaOperativa) {
  this.fechaOperativa = fechaOperativa;
 }
 /**
  * @return the id
  */
 public String getId() {
  return id;
 }
 /**
  * @param id the id to set
  */
 public void setId(String id) {
  this.id = id;
 }
 /**
  * @return the estadoDescarga
  */
 public int getEstadoDescarga() {
  return estadoDescarga;
 }
 /**
  * @param estadoDescarga the estadoDescarga to set
  */
 public void setEstadoDescarga(int estadoDescarga) {
  this.estadoDescarga = estadoDescarga;
 }

 /**
  * @return the descripcion
  */
 public String getDescripcion() {
  return descripcion;
 }

 /**
  * @param descripcion the descripcion to set
  */
 public void setDescripcion(String descripcion) {
  this.descripcion = descripcion;
 }

}
