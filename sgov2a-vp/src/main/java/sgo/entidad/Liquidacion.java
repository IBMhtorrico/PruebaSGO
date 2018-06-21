/*package sgo.entidad;

import java.sql.Date;

public class Liquidacion {

 private int id;
 private int idOperacion;
 private Date fechaOperativa;
 private String nombreOperacion;
 private int idProducto;
 private String nombreProducto;
 private float porcentajeActual;
 private float stockInicial;
 private float stockFinal;
 private float volumenDescargado;
 private float volumenDespachado;
 private float stockFinalFisico;
 private float stockFinalCalculado;
 public int getId() {
  return id;
 }
 public void setId(int id) {
  this.id = id;
 }
 public int getIdOperacion() {
  return idOperacion;
 }
 public void setIdOperacion(int idOperacion) {
  this.idOperacion = idOperacion;
 }
 public Date getFechaOperativa() {
  return fechaOperativa;
 }
 public void setFechaOperativa(Date fechaOperativa) {
  this.fechaOperativa = fechaOperativa;
 }
 public String getNombreOperacion() {
  return nombreOperacion;
 }
 public void setNombreOperacion(String nombreOperacion) {
  this.nombreOperacion = nombreOperacion;
 }
 public int getIdProducto() {
  return idProducto;
 }
 public void setIdProducto(int idProducto) {
  this.idProducto = idProducto;
 }
 public String getNombreProducto() {
  return nombreProducto;
 }
 public void setNombreProducto(String nombreProducto) {
  this.nombreProducto = nombreProducto;
 }
 public float getPorcentajeActual() {
  return porcentajeActual;
 }
 public void setPorcentajeActual(float porcentajeActual) {
  this.porcentajeActual = porcentajeActual;
 }
 public float getStockInicial() {
  return stockInicial;
 }
 public void setStockInicial(float stockInicial) {
  this.stockInicial = stockInicial;
 }
 public float getStockFinal() {
  return stockFinal;
 }
 public void setStockFinal(float stockFinal) {
  this.stockFinal = stockFinal;
 }
 public float getVolumenDescargado() {
  return volumenDescargado;
 }
 public void setVolumenDescargado(float volumenDescargado) {
  this.volumenDescargado = volumenDescargado;
 }
 public float getVolumenDespachado() {
  return volumenDespachado;
 }
 public void setVolumenDespachado(float volumenDespachado) {
  this.volumenDespachado = volumenDespachado;
 }
 public float getStockFinalFisico() {
  return stockFinalFisico;
 }
 public void setStockFinalFisico(float stockFinalFisico) {
  this.stockFinalFisico = stockFinalFisico;
 }
 public float getStockFinalCalculado() {
  return stockFinalCalculado;
 }
 public void setStockFinalCalculado(float stockFinalCalculado) {
  this.stockFinalCalculado = stockFinalCalculado;
 }
}*/
package sgo.entidad;

import java.sql.Date;

public class Liquidacion  {
  private int id_liquidacion;
  private int id_operacion;
  private Date fecha_operativa;
  private int id_producto;
  private float porcentaje_actual;
  private float stock_final;
  private float stock_inicial;
  private float volumen_descargado;
  private float volumen_despacho;
  private float tolerancia;
  private float stock_final_calculado;
  private float variacion;
  private float variacion_absoluta;
  private String nombre_producto;
  private String nombre_operacion;
  private String nombre_cliente;
  private float faltante;
  private int id_estacion;
  private int id_tanque;
  private String nombre_estacion;
  private String nombre_tanque;
  private String estado;

  public int getId(){
    return id_liquidacion;
  }

  public void setId(int Id ){
    this.id_liquidacion=Id;
  }
  
  public int getIdOperacion(){
    return id_operacion;
  }

  public void setIdOperacion(int IdOperacion ){
    this.id_operacion=IdOperacion;
  }
  
  public Date getFechaOperativa(){
    return fecha_operativa;
  }

  public void setFechaOperativa(Date FechaOperativa ){
    this.fecha_operativa=FechaOperativa;
  }
  
  public int getIdProducto(){
    return id_producto;
  }

  public void setIdProducto(int IdProducto ){
    this.id_producto=IdProducto;
  }
  
  public float getPorcentajeActual(){
    return porcentaje_actual;
  }

  public void setPorcentajeActual(float PorcentajeActual ){
    this.porcentaje_actual=PorcentajeActual;
  }
  
  public float getStockFinal(){
    return stock_final;
  }

  public void setStockFinal(float StockFinal ){
    this.stock_final=StockFinal;
  }
  
  public float getStockInicial(){
    return stock_inicial;
  }

  public void setStockInicial(float StockInicial ){
    this.stock_inicial=StockInicial;
  }
  
  public float getVolumenDescargado(){
    return volumen_descargado;
  }

  public void setVolumenDescargado(float VolumenDescargado ){
    this.volumen_descargado=VolumenDescargado;
  }
  
  public float getVolumenDespacho(){
    return volumen_despacho;
  }

  public void setVolumenDespacho(float VolumenDespacho ){
    this.volumen_despacho=VolumenDespacho;
  }
  
  public float getTolerancia(){
    return tolerancia;
  }

  public void setTolerancia(float Tolerancia ){
    this.tolerancia=Tolerancia;
  }
  
  public float getStockFinalCalculado(){
    return stock_final_calculado;
  }

  public void setStockFinalCalculado(float StockFinalCalculado ){
    this.stock_final_calculado=StockFinalCalculado;
  }
  
  public float getVariacion(){
    return variacion;
  }

  public void setVariacion(float Variacion ){
    this.variacion=Variacion;
  }
  
  public float getVariacionAbsoluta(){
    return variacion_absoluta;
  }

  public void setVariacionAbsoluta(float VariacionAbsoluta ){
    this.variacion_absoluta=VariacionAbsoluta;
  }
  
  public String getNombreProducto(){
    return nombre_producto;
  }

  public void setNombreProducto(String NombreProducto ){
    this.nombre_producto=NombreProducto;
  }
  
  public String getNombreOperacion(){
    return nombre_operacion;
  }

  public void setNombreOperacion(String NombreOperacion ){
    this.nombre_operacion=NombreOperacion;
  }
  
  public String getNombreCliente(){
    return nombre_cliente;
  }

  public void setNombreCliente(String NombreCliente ){
    this.nombre_cliente=NombreCliente;
  }
  
  public float getFaltante(){
    return faltante;
  }

  public void setFaltante(float Faltante ){
    this.faltante=Faltante;
  }
  
  public int getIdEstacion(){
    return id_estacion;
  }

  public void setIdEstacion(int IdEstacion ){
    this.id_estacion=IdEstacion;
  }
  
  public int getIdTanque(){
    return id_tanque;
  }

  public void setIdTanque(int IdTanque ){
    this.id_tanque=IdTanque;
  }
  
  public String getNombreEstacion(){
    return nombre_estacion;
  }

  public void setNombreEstacion(String NombreEstacion ){
    this.nombre_estacion=NombreEstacion;
  }
  
  public String getNombreTanque(){
    return nombre_tanque;
  }

  public void setNombreTanque(String NombreTanque ){
    this.nombre_tanque=NombreTanque;
  }
  
  public String getNombreClienteOperacion (){
   return this.nombre_cliente + " / " + this.nombre_operacion;
  }

  /**
   * @return the estado
   */
  public String getEstado() {
   return estado;
  }

  /**
   * @param estado the estado to set
   */
  public void setEstado(String estado) {
   this.estado = estado;
  }
  
  
}
