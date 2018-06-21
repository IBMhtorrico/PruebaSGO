package sgo.utilidades;

public class Campo {
 private String etiqueta;
 private String nombre;
 private int tipo;
 private float ancho;
 private int numeroDecimales=2;
 private int alineacionHorizontal=ALINEACION_CENTRO;

 
 public static final int ALINEACION_IZQUIERDA=0;
 public static final int ALINEACION_CENTRO=1;
 public static final int ALINEACION_DERECHA=2;
 
 public static final int TIPO_NUMERICO=1;
 public static final int TIPO_TEXTO=2;
 public static final int TIPO_MEMO=3;
 public static final int TIPO_FECHA=4;
 public static final int TIPO_ENTERO=5;
 public static final int TIPO_DECIMAL=6;

 
 public String getEtiqueta() {
  return etiqueta;
 }
 public void setEtiqueta(String etiqueta) {
  this.etiqueta = etiqueta;
 }
 public String getNombre() {
  return nombre;
 }
 public void setNombre(String nombre) {
  this.nombre = nombre;
 }
 public int getTipo() {
  return tipo;
 }
 public void setTipo(int tipo) {
  this.tipo = tipo;
 }
 /**
  * @return the ancho
  */
 public float getAncho() {
  return ancho;
 }
 /**
  * @param ancho the ancho to set
  */
 public void setAncho(float ancho) {
  this.ancho = ancho;
 }
 /**
  * @return the alineacionHorizontal
  */
 public int getAlineacionHorizontal() {
  return alineacionHorizontal;
 }
 /**
  * @param alineacionHorizontal the alineacionHorizontal to set
  */
 public void setAlineacionHorizontal(int alineacionHorizontal) {
  this.alineacionHorizontal = alineacionHorizontal;
 }
 /**
  * @return the numeroDecimales
  */
 public int getNumeroDecimales() {
  return numeroDecimales;
 }
 /**
  * @param numeroDecimales the numeroDecimales to set
  */
 public void setNumeroDecimales(int numeroDecimales) {
  this.numeroDecimales = numeroDecimales;
 }

}
