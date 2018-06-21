package sgo.entidad;

import java.sql.Date;
import java.text.SimpleDateFormat;

import sgo.utilidades.Constante;

public class Bitacora {
private int id_bitacora;
private String usuario;
private String accion;
private String tabla;
private String contenido;
private long realizado_el;
private int realizado_por;
private String identificador;
protected int formatoFecha=Constante.TIPO_FORMATO_FECHA_DDMMYYYY;

public int getId(){
  return id_bitacora;
}

public void setId(int Id ){
  this.id_bitacora=Id;
}

public String getUsuario(){
  return usuario;
}

public void setUsuario(String Usuario ){
  this.usuario=Usuario;
}

public String getAccion(){
  return accion;
}

public void setAccion(String Accion ){
  this.accion=Accion;
}

public String getTabla(){
  return tabla;
}

public void setTabla(String Tabla ){
  this.tabla=Tabla;
}

public long getRealizadoEl(){
  return realizado_el;
}

public void setRealizadoEl(long RealizadoEl ){
  this.realizado_el=RealizadoEl;
}

public String getFechaRealizacion() {
	String fechaRealizacionFormateada="";
	Date fechaActualizacion = new Date(realizado_el);
	SimpleDateFormat formateadorFecha = null;
	
	formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_DDMMYYYY){
		fechaRealizacionFormateada = formateadorFecha.format(fechaActualizacion); 
	}
	
	formateadorFecha=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_MMDDYYYY){
		fechaRealizacionFormateada = formateadorFecha.format(fechaActualizacion); 
	}
	
	formateadorFecha=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	if(this.formatoFecha==Constante.TIPO_FORMATO_FECHA_ESTANDAR){
		fechaRealizacionFormateada = formateadorFecha.format(fechaActualizacion); 
	}
	return fechaRealizacionFormateada;
}

public String getContenido(){
  return contenido;
}

public void setContenido(String Contenido ){
  this.contenido=Contenido;
  
}

/**
 * @return the realizado_por
 */
public int getRealizadoPor() {
	return realizado_por;
}

/**
 * @param realizado_por the realizado_por to set
 */
public void setRealizadoPor(int realizado_por) {
	this.realizado_por = realizado_por;
}

/**
 * @return the identificador
 */
public String getIdentificador() {
	return identificador;
}

/**
 * @param identificador the identificador to set
 */
public void setIdentificador(String identificador) {
	this.identificador = identificador;
}


}