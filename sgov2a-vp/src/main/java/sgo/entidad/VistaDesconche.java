package sgo.entidad;

import java.sql.Date;

public class VistaDesconche extends EntidadBase{
 private int id;
 private Date fechaPlanificada;
 private String placaCisterna;
 private String placaTracto;
 private int numeroDesconche;
 private int numeroCompartimento;
 private float volumenDesconche;
 private String estacion;
 private String tanque;
 private int idOperacion;
 private int numeroMaximoDesconches;
 private int numeroCompartimentos;
 private int idDescargaCisterna;
 
 public Date getFechaPlanificada() {
  return fechaPlanificada;
 }
 public void setFechaPlanificada(Date fechaPlanificada) {
  this.fechaPlanificada = fechaPlanificada;
 }
 public String getPlacaCisterna() {
  return placaCisterna;
 }
 public void setPlacaCisterna(String placaCisterna) {
  this.placaCisterna = placaCisterna;
 }
 public int getNumeroDesconche() {
  return numeroDesconche;
 }
 public void setNumeroDesconche(int numeroDesconche) {
  this.numeroDesconche = numeroDesconche;
 }
 public int getNumeroCompartimento() {
  return numeroCompartimento;
 }
 public void setNumeroCompartimento(int numeroCompartimento) {
  this.numeroCompartimento = numeroCompartimento;
 }
 public float getVolumenDesconche() {
  return volumenDesconche;
 }
 public void setVolumenDesconche(float volumenDesconche) {
  this.volumenDesconche = volumenDesconche;
 }
 public String getEstacion() {
  return estacion;
 }
 public void setEstacion(String estacion) {
  this.estacion = estacion;
 }
 public String getTanque() {
  return tanque;
 }
 public void setTanque(String tanque) {
  this.tanque = tanque;
 }
 /**
  * @return the id
  */
 public int getId() {
  return id;
 }
 /**
  * @param id the id to set
  */
 public void setId(int id) {
  this.id = id;
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
  * @return the placaTracto
  */
 public String getPlacaTracto() {
  return placaTracto;
 }
 /**
  * @param placaTracto the placaTracto to set
  */
 public void setPlacaTracto(String placaTracto) {
  this.placaTracto = placaTracto;
 }
 /**
  * @return the numeroMaximoDesconches
  */
 public int getNumeroMaximoDesconches() {
  return numeroMaximoDesconches;
 }
 /**
  * @param numeroMaximoDesconches the numeroMaximoDesconches to set
  */
 public void setNumeroMaximoDesconches(int numeroMaximoDesconches) {
  this.numeroMaximoDesconches = numeroMaximoDesconches;
 }
 /**
  * @return the numeroCompartimentos
  */
 public int getNumeroCompartimentos() {
  return numeroCompartimentos;
 }
 /**
  * @param numeroCompartimentos the numeroCompartimentos to set
  */
 public void setNumeroCompartimentos(int numeroCompartimentos) {
  this.numeroCompartimentos = numeroCompartimentos;
 }
 /**
  * @return the idDescargaCisterna
  */
 public int getIdDescargaCisterna() {
  return idDescargaCisterna;
 }
 /**
  * @param idDescargaCisterna the idDescargaCisterna to set
  */
 public void setIdDescargaCisterna(int idDescargaCisterna) {
  this.idDescargaCisterna = idDescargaCisterna;
 }
 
 
}
