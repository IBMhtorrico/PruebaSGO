package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;

public class DescargaCisterna extends EntidadBase {
  private int id_dcisterna;
  private int id_estacion;
  private int id_ctanque;
  private int tipo_tanque;//validaciones tipo tanque
  private int id_tanque;
  private int id_jornada;
  private int id_doperativo;  
  private int id_transporte;
  private Date fecha_arribo;
  private Date fecha_fiscalizacion;
  private int metodo_descarga;
  private String numero_comprobante;
  private String nombre_operacion;
  private String nombre_estacion;
  private String descripcion_tanque;
  private float merma_permisible;
  private float merma_porcentaje;
  private float lectura_inicial;
  private float lectura_final;
  private float pesaje_inicial;
  private float pesaje_final;
  private float factor_conversion;
  private float peso_neto;
  private float volumen_total_descargado_observado;
  private float volumen_total_descargado_corregido;
  private float variacion_volumen;
  private float volumen_excedente_observado;
  private float volumen_excedente_corregido;
  private int id_aejecutada=0;
  private ArrayList<DescargaCompartimento> compartimentos;
  private ArrayList<Evento> eventos;  
  private Transporte transporte;
  private int estado;
  
  public static final int ESTADO_ACTIVO=1;
  public static final int ESTADO_INACTIVO=2;
  public static final int ESTADO_OBSERVADO =3;  
  
  public static final int METODO_WINCHA=1;
  public static final int METODO_BALANZA=2;
  public static final int METODO_CONTOMETRO=3;

  public void agregarEvento (Evento elemento){
   if (this.eventos== null){
    this.eventos = new ArrayList<Evento>();
   }
   this.eventos.add(elemento);
  }

  public String getNombreOperacion() {
   return nombre_operacion;
  }

  public void setNombreOperacion(String nombre_operacion) {
   this.nombre_operacion = nombre_operacion;
  }

  public String getNombreEstacion() {
   return nombre_estacion;
  }

  public void setNombreEstacion(String nombre_estacion) {
   this.nombre_estacion = nombre_estacion;
  }

  public String getDescripcionTanque() {
   return descripcion_tanque;
  }

  public void setDescripcionTanque(String descripcion_tanque) {
   this.descripcion_tanque = descripcion_tanque;
  }

  public void agregarCompartimento (DescargaCompartimento elemento){
   if (this.compartimentos== null){
    this.compartimentos = new ArrayList<DescargaCompartimento>();
   }
   this.compartimentos.add(elemento);
  }
  
  public int getId(){
    return id_dcisterna;
  }

  public void setId(int Id ){
    this.id_dcisterna=Id;
  }
  
  public int getIdCargaTanque(){
    return id_ctanque;
  }

  public void setIdCargaTanque(int IdCargaTanque ){
    this.id_ctanque=IdCargaTanque;
  }
  
  public int getIdTransporte(){
    return id_transporte;
  }

  public void setIdTransporte(int IdTransporte ){
    this.id_transporte=IdTransporte;
  }
  
  public Date getFechaArribo(){
    return fecha_arribo;
  }

  public void setFechaArribo(Date FechaArribo ){
    this.fecha_arribo=FechaArribo;
  }
  
  public Date getFechaFiscalizacion(){
    return fecha_fiscalizacion;
  }

  public void setFechaFiscalizacion(Date FechaFiscalizacion ){
    this.fecha_fiscalizacion=FechaFiscalizacion;
  }
  
  public int getMetodoDescarga(){
    return metodo_descarga;
  }

  public void setMetodoDescarga(int MetodoDescarga ){
    this.metodo_descarga=MetodoDescarga;
  }
  
  public String getNumeroComprobante(){
    return numero_comprobante;
  }

  public void setNumeroComprobante(String NumeroComprobante ){
    this.numero_comprobante=NumeroComprobante;
  }
  
  public float getMermaPermisible(){
    return merma_permisible;
  }

  public void setMermaPermisible(float MermaPermisible ){
    this.merma_permisible=MermaPermisible;
  }
  
  public float getMermaPorcentaje(){
    return merma_porcentaje;
  }

  public void setMermaPorcentaje(float MermaPorcentaje ){
    this.merma_porcentaje=MermaPorcentaje;
  }
  
  public float getLecturaInicial(){
    return lectura_inicial;
  }

  public void setLecturaInicial(float LecturaInicial ){
    this.lectura_inicial=LecturaInicial;
  }
  
  public float getLecturaFinal(){
    return lectura_final;
  }

  public void setLecturaFinal(float LecturaFinal ){
    this.lectura_final=LecturaFinal;
  }
  
  public float getPesajeInicial(){
    return pesaje_inicial;
  }

  public void setPesajeInicial(float PesajeInicial ){
    this.pesaje_inicial=PesajeInicial;
  }
  
  public float getPesajeFinal(){
    return pesaje_final;
  }

  public void setPesajeFinal(float PesajeFinal ){
    this.pesaje_final=PesajeFinal;
  }
  
  public float getFactorConversion(){
    return factor_conversion;
  }

  public void setFactorConversion(float FactorConversion ){
    this.factor_conversion=FactorConversion;
  }
  
  public float getPesoNeto(){
    return peso_neto;
  }

  public void setPesoNeto(float PesoNeto ){
    this.peso_neto=PesoNeto;
  }
  
  public float getVolumenTotalDescargadoObservado(){
    return volumen_total_descargado_observado;
  }

  public void setVolumenTotalDescargadoObservado(float VolumenTotalDescargadoObservado ){
    this.volumen_total_descargado_observado=VolumenTotalDescargadoObservado;
  }
  
  public float getVolumenTotalDescargadoCorregido(){
    return volumen_total_descargado_corregido;
  }

  public void setVolumenTotalDescargadoCorregido(float VolumenTotalDescargadoCorregido ){
    this.volumen_total_descargado_corregido=VolumenTotalDescargadoCorregido;
  }
  
 /* public float getVariacionObservado(){
    return variacion_observado;
  }

  public void setVariacionObservado(float VariacionObservado ){
    this.variacion_observado=VariacionObservado;
  }
  
  public float getVariacionCorregido(){
    return variacion_corregido;
  }

  public void setVariacionCorregido(float VariacionCorregido ){
    this.variacion_corregido=VariacionCorregido;
  }*/
  
  public float getVolumenExcedenteObservado(){
    return volumen_excedente_observado;
  }

  public void setVolumenExcedenteObservado(float VolumenExcedenteObservado ){
    this.volumen_excedente_observado=VolumenExcedenteObservado;
  }
  
  public float getVolumenExcedenteCorregido(){
    return volumen_excedente_corregido;
  }

  public void setVolumenExcedenteCorregido(float VolumenExcedenteCorregido ){
    this.volumen_excedente_corregido=VolumenExcedenteCorregido;
  }

  /**
   * @return the variacion_volumen
   */
  public float getVariacionVolumen() {
   return variacion_volumen;
  }

  /**
   * @param variacion_volumen the variacion_volumen to set
   */
  public void setVariacionVolumen(float variacion_volumen) {
   this.variacion_volumen = variacion_volumen;
  }

  /**
   * @return the compartimentos
   */
  public ArrayList<DescargaCompartimento> getCompartimentos() {
   return compartimentos;
  }

  /**
   * @param compartimentos the compartimentos to set
   */
  public void setCompartimentos(ArrayList<DescargaCompartimento> compartimentos) {
   this.compartimentos = compartimentos;
  }

  /**
   * @return the transporte
   */
  public Transporte getTransporte() {
   return transporte;
  }

  /**
   * @param transporte the transporte to set
   */
  public void setTransporte(Transporte transporte) {
   this.transporte = transporte;
  }

  /**
   * @return the eventos
   */
  public ArrayList<Evento> getEventos() {
   return eventos;
  }

  /**
   * @param eventos the eventos to set
   */
  public void setEventos(ArrayList<Evento> eventos) {
   this.eventos = eventos;
  }

  /**
   * @return the id_aejecutada
   */
  public int getIdAutorizacionEjecutada() {
   return id_aejecutada;
  }

  /**
   * @param id_aejecutada the id_aejecutada to set
   */
  public void setIdAutorizacionEjecutada(int id_aejecutada) {
   this.id_aejecutada = id_aejecutada;
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

public int getTipoTanque() {
	return tipo_tanque;
}

public void setTipoTanque(int tipo_tanque) {
	this.tipo_tanque = tipo_tanque;
}

public int getIdJornada() {
	return id_jornada;
}

public void setIdJornada(int id_jornada) {
	this.id_jornada = id_jornada;
}

public int getIdTanque() {
	return id_tanque;
}

public void setIdTanque(int id_tanque) {
	this.id_tanque = id_tanque;
}

public int getIdDoperativo() {
	return id_doperativo;
}

public void setIdDoperativo(int id_doperativo) {
	this.id_doperativo = id_doperativo;
}

public int getIdEstacion() {
	return id_estacion;
}

public void setIdEstacion(int id_estacion) {
	this.id_estacion = id_estacion;
}

}