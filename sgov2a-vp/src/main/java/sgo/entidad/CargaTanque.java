package sgo.entidad;

import java.sql.Timestamp;

public class CargaTanque extends EntidadBase {
  private int id_ctanque;
  private int id_tanque;
  private int id_estacion;
  private int id_doperativo;
  private Timestamp fecha_hora_inicio;
  private Timestamp fecha_hora_fin;
  private int altura_inicial_producto;
  private int altura_final_producto;
  private float temperatura_inicial_centro;
  private float temperatura_final_centro;
  private float temperatura_inicial_probeta;
  private float temperatura_final_probeta;
  private float api_observado_inicial;
  private float api_observado_final;
  private float factor_correccion_inicial;
  private float factor_correccion_final;
  private float volumen_observado_inicial;
  private float volumen_observado_final;
  private float volumen_corregido_inicial;
  private float volumen_corregido_final;
  private Tanque tanque;
  private Estacion estacion;
  private DiaOperativo diaOperativo;
  

  public int getId(){
    return id_ctanque;
  }

  public void setId(int Id ){
    this.id_ctanque=Id;
  }
  
  public int getIdTanque(){
    return id_tanque;
  }

  public void setIdTanque(int idTanque ){
    this.id_tanque=idTanque;
  }
  
  public Timestamp getFechaHoraInicial(){
    return fecha_hora_inicio;
  }

  public void setFechaHoraInicial(Timestamp FechaHoraInicial ){
    this.fecha_hora_inicio=FechaHoraInicial;
  }
  
  public Timestamp getFechaHoraFinal(){
    return fecha_hora_fin;
  }

  public void setFechaHoraFinal(Timestamp FechaHoraFinal ){
    this.fecha_hora_fin=FechaHoraFinal;
  }
  
  public int getAlturaInicial(){
    return altura_inicial_producto;
  }

  public void setAlturaInicial(int AlturaInicial ){
    this.altura_inicial_producto=AlturaInicial;
  }
  
  public int getAlturaFinal(){
    return altura_final_producto;
  }

  public void setAlturaFinal(int AlturaFinal ){
    this.altura_final_producto=AlturaFinal;
  }
  
  public float getTemperaturaInicialCentro(){
    return temperatura_inicial_centro;
  }

  public void setTemperaturaInicialCentro(float TemperaturaInicialCentro ){
    this.temperatura_inicial_centro=TemperaturaInicialCentro;
  }
  
  public float getTemperaturaFinalCentro(){
    return temperatura_final_centro;
  }

  public void setTemperaturaFinalCentro(float TemperaturaFinalCentro ){
    this.temperatura_final_centro=TemperaturaFinalCentro;
  }
  
  public float getTemperaturaIniciaProbeta(){
    return temperatura_inicial_probeta;
  }

  public void setTemperaturaIniciaProbeta(float TemperaturaIniciaProbeta ){
    this.temperatura_inicial_probeta=TemperaturaIniciaProbeta;
  }
  
  public float getTemperaturaFinalProbeta(){
    return temperatura_final_probeta;
  }

  public void setTemperaturaFinalProbeta(float TemperaturaFinalProbeta ){
    this.temperatura_final_probeta=TemperaturaFinalProbeta;
  }
  
  public float getApiObservadoInicial(){
    return api_observado_inicial;
  }

  public void setApiObservadoInicial(float ApiObservadoInicial ){
    this.api_observado_inicial=ApiObservadoInicial;
  }
  
  public float getApiObservadoFinal(){
    return api_observado_final;
  }

  public void setApiObservadoFinal(float ApiObservadoFinal ){
    this.api_observado_final=ApiObservadoFinal;
  }
  
  public float getFactorCorreccionInicial(){
    return factor_correccion_inicial;
  }

  public void setFactorCorreccionInicial(float FactorCorreccionInicial ){
    this.factor_correccion_inicial=FactorCorreccionInicial;
  }
  
  public float getFactorCorreccionFinal(){
    return factor_correccion_final;
  }

  public void setFactorCorreccionFinal(float FactorCorreccionFinal ){
    this.factor_correccion_final=FactorCorreccionFinal;
  }
  
  public float getVolumenObservadoInicial(){
    return volumen_observado_inicial;
  }

  public void setVolumenObservadoInicial(float VolumenObservadoInicial ){
    this.volumen_observado_inicial=VolumenObservadoInicial;
  }
  
  public float getVolumenObservadoFinal(){
    return volumen_observado_final;
  }

  public void setVolumenObservadoFinal(float VolumenObservadoFinal ){
    this.volumen_observado_final=VolumenObservadoFinal;
  }
  
  public float getVolumenCorregidoInicial(){
    return volumen_corregido_inicial;
  }

  public void setVolumenCorregidoInicial(float VolumenCorregidoInicial ){
    this.volumen_corregido_inicial=VolumenCorregidoInicial;
  }
  
  public float getVolumenCorregidoFinal(){
    return volumen_corregido_final;
  }

  public void setVolumenCorregidoFinal(float VolumenCorregidoFinal ){
    this.volumen_corregido_final=VolumenCorregidoFinal;
  }

  /**
   * @return the tanque
   */
  public Tanque getTanque() {
   return tanque;
  }

  /**
   * @param tanque the tanque to set
   */
  public void setTanque(Tanque tanque) {
   this.tanque = tanque;
  }

  /**
   * @return the estacion
   */
  public Estacion getEstacion() {
   return estacion;
  }

  /**
   * @param estacion the estacion to set
   */
  public void setEstacion(Estacion estacion) {
   this.estacion = estacion;
  }

  /**
   * @return the id_estacion
   */
  public int getIdEstacion() {
   return id_estacion;
  }

  /**
   * @param id_estacion the id_estacion to set
   */
  public void setIdEstacion(int id_estacion) {
   this.id_estacion = id_estacion;
  }

  /**
   * @return the id_doperativo
   */
  public int getIdDiaOperativo() {
   return id_doperativo;
  }

  /**
   * @param id_doperativo the id_doperativo to set
   */
  public void setIdDiaOperativo(int id_doperativo) {
   this.id_doperativo = id_doperativo;
  }

  /**
   * @return the diaOperativo
   */
  public DiaOperativo getDiaOperativo() {
   return diaOperativo;
  }

  /**
   * @param diaOperativo the diaOperativo to set
   */
  public void setDiaOperativo(DiaOperativo diaOperativo) {
   this.diaOperativo = diaOperativo;
  }
  
  
}