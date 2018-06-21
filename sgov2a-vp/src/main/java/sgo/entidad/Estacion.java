package sgo.entidad;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Estacion extends EntidadBase {
 private int id_estacion;
 private String nombre;
 private int cantidadTurnos;
 private int tipo;
 private int estado;
 private int id_operacion;
 /*
  * private String sincronizado_el; private String fecha_referencia; private
  * String codigo_referencia;
  */
 private Operacion operacion;
 private int metodoDescarga;
 private ArrayList<Tolerancia> tolerancias;

 public static final int TIPO_ESTANDAR = 1;
 public static final int TIPO_REPARTO = 2;
 public static final int TIPO_TUBERIA = 3;

 public ArrayList<Tolerancia> getTolerancias() {
  return tolerancias;
 }

 public void setTolerancias(ArrayList<Tolerancia> tolerancias) {
  this.tolerancias = tolerancias;
 }

 public void agregarTolerancia(Tolerancia elemento) {
  if (this.tolerancias == null) {
   this.tolerancias = new ArrayList<Tolerancia>();
  }
  this.tolerancias.add(elemento);
 }

 // variable para hacer las validaciones.
 static final int MAXIMA_LONGITUD_NOMBRE = 20;

 public int getId() {
  return id_estacion;
 }

 public void setId(int Id) {
  this.id_estacion = Id;
 }

 public String getNombre() {
  return nombre;
 }

 public void setNombre(String Nombre) {
  this.nombre = Nombre;
 }

 public int getTipo() {
  return tipo;
 }

 public void setTipo(int Tipo) {
  this.tipo = Tipo;
 }

 public int getEstado() {
  return estado;
 }

 public void setEstado(int Estado) {
  this.estado = Estado;
 }

 public int getIdOperacion() {
  return id_operacion;
 }

 public void setIdOperacion(int IdOperacion) {
  this.id_operacion = IdOperacion;
 }

 /*
  * public String getSincronizadoEl() { return sincronizado_el; }
  * 
  * public void setSincronizadoEl(String SincronizadoEl) { this.sincronizado_el
  * = SincronizadoEl; }
  * 
  * public String getFechaReferencia() { return fecha_referencia; }
  * 
  * public void setFechaReferencia(String FechaReferencia) {
  * this.fecha_referencia = FechaReferencia; }
  * 
  * public String getCodigoReferencia() { return codigo_referencia; }
  * 
  * public void setCodigoReferencia(String CodigoReferencia) {
  * this.codigo_referencia = CodigoReferencia; }
  */

 public Operacion getOperacion() {
  return operacion;
 }

 public void setOperacion(Operacion operacion) {
  this.operacion = operacion;
 }
 
 /**
  * @return the metodoDescarga
  */
 public int getMetodoDescarga() {
  return metodoDescarga;
 }

 /**
  * @param metodoDescarga
  *         the metodoDescarga to set
  */
 public void setMetodoDescarga(int metodoDescarga) {
  this.metodoDescarga = metodoDescarga;
 }

 public boolean validar() {
  boolean resultado = true;

  if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE || !Utilidades.esValido(this.nombre)) {
   return false;
  }

  if (!Utilidades.esValido(this.tipo)) {
   return false;
  }

  if (!Utilidades.esValido(this.id_operacion)) {
   return false;
  }

  if (!Utilidades.esValido(this.estado)) {
   return false;
  }

  return resultado;
 }
 
 public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.nombre)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Nombre" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.tipo)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_operacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Operación" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.metodoDescarga)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Método Descarga" }, locale);
		return respuesta;
	  }
	  if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre", MAXIMA_LONGITUD_NOMBRE }, locale);
		return respuesta;
	  }
	  respuesta.estado = true;
	  respuesta.valor = null;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
}

//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
public String getCadena() {
	String cadena="";
	if(!Utilidades.esValido(this.getNombre())){ this.setNombre(""); };

	cadena = this.getNombre().toString() ;
	return cadena;
}

public int getCantidadTurnos() {
	return cantidadTurnos;
}

public void setCantidadTurnos(int cantidadTurnos) {
	this.cantidadTurnos = cantidadTurnos;
}
	
}
