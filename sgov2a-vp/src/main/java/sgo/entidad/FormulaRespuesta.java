package sgo.entidad;

public class FormulaRespuesta {
 private double apiCorregido =0;
 private double factorCorreccion = 0;
 private  double volumenCorregido=0;
 private double tolerancia = 0 ;
 private int tipoVolumen = 0;
 public double getApiCorregido() {
  return apiCorregido;
 }
 public void setApiCorregido(double apiCorregido) {
  this.apiCorregido = apiCorregido;
 }
 public double getFactorCorreccion() {
  return factorCorreccion;
 }
 public void setFactorCorreccion(double factorCorreccion) {
  this.factorCorreccion = factorCorreccion;
 }
 public double getVolumenCorregido() {
  return volumenCorregido;
 }
 public void setVolumenCorregido(double volumenCorregido) {
  this.volumenCorregido = volumenCorregido;
 }
 /**
  * @return the tolerancia
  */
 public double getTolerancia() {
  return tolerancia;
 }
 /**
  * @param tolerancia the tolerancia to set
  */
 public void setTolerancia(double tolerancia) {
  this.tolerancia = tolerancia;
 }
 /**
  * @return the tipoVolumen
  */
 public int getTipoVolumen() {
  return tipoVolumen;
 }
 /**
  * @param tipoVolumen the tipoVolumen to set
  */
 public void setTipoVolumen(int tipoVolumen) {
  this.tipoVolumen = tipoVolumen;
 }
}
