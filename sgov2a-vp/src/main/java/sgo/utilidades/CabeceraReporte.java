package sgo.utilidades;

public class CabeceraReporte {
 private String etiqueta ;
 private int colspan;
 private int rowspan;
 private int alineacionHorizontal=ALINEACION_CENTRO;
 private int alineacionVertical = ALINEACION_MEDIA;
 
 public static final int ALINEACION_IZQUIERDA=0;
 public static final int ALINEACION_CENTRO=1;
 public static final int ALINEACION_DERECHA=2;
 public static final int ALINEACION_MEDIA=5;
 
 public String getEtiqueta() {
  return etiqueta;
 }
 public void setEtiqueta(String etiqueta) {
  this.etiqueta = etiqueta;
 }
 public int getColspan() {
  return colspan;
 }
 public void setColspan(int colspan) {
  this.colspan = colspan;
 }
 public int getRowspan() {
  return rowspan;
 }
 public void setRowspan(int rowspan) {
  this.rowspan = rowspan;
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
  * @return the alineacionVertical
  */
 public int getAlineacionVertical() {
  return alineacionVertical;
 }
 /**
  * @param alineacionVertical the alineacionVertical to set
  */
 public void setAlineacionVertical(int alineacionVertical) {
  this.alineacionVertical = alineacionVertical;
 }
 
}
