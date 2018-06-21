package sgo.entidad;

import java.sql.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Compartimento  {
 private int id_compartimento;
 private int identificador;
 private int altura_flecha;
 private int id_cisterna;
 private float capacidad_volumetrica;
 private int id_tracto;
 private int cantidad_aforos;
 private int descargas;
 
 public final static int MAXIMA_LONGITUD_CAPACIDAD_VOLUMETRICA=7;
 public final static int MAXIMA_LONGITUD_ALTURA_FLECHA=4;
 
 public int getId() {
  return id_compartimento;
 }
 public void setId(int id_compartimento) {
  this.id_compartimento = id_compartimento;
 }
 public int getIdentificador() {
  return identificador;
 }
 public void setIdentificador(int identificador) {
  this.identificador = identificador;
 }
 public int getAlturaFlecha() {
  return altura_flecha;
 }
 public void setAlturaFlecha(int altura_flecha) {
  this.altura_flecha = altura_flecha;
 }
 public int getIdCisterna() {
  return id_cisterna;
 }
 public void setIdCisterna(int id_cisterna) {
  this.id_cisterna = id_cisterna;
 }
 public float getCapacidadVolumetrica() {
  return capacidad_volumetrica;
 }
 public void setCapacidadVolumetrica(float capacidad_volumetrica) {
  this.capacidad_volumetrica = capacidad_volumetrica;
 }
 /**
  * @return the id_tracto
  */
 public int getIdTracto() {
  return id_tracto;
 }
 /**
  * @param id_tracto the id_tracto to set
  */
 public void setIdTracto(int id_tracto) {
  this.id_tracto = id_tracto;
 }
/**
 * @return the cantidad_aforos
 */
public int getCantidadAforos() {
	return cantidad_aforos;
}
/**
 * @param cantidad_aforos the cantidad_aforos to set
 */
public void setCantidadAforos(int cantidadAforos) {
	this.cantidad_aforos = cantidadAforos;
}
/**
 * @return the descargas
 */
public int getDescargas() {
	return descargas;
}
/**
 * @param descargas the descargas to set
 */
public void setDescargas(int descargas) {
	this.descargas = descargas;
}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.altura_flecha)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Altura Flecha" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.capacidad_volumetrica)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Capacidad Volumétrica" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_cisterna)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cisterna" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_tracto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tracto" }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.altura_flecha).length() > MAXIMA_LONGITUD_ALTURA_FLECHA){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Altura Flecha", MAXIMA_LONGITUD_ALTURA_FLECHA }, locale);
		return respuesta;
	  }
	  if (Float.toString(this.capacidad_volumetrica).length() > MAXIMA_LONGITUD_CAPACIDAD_VOLUMETRICA){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Capacidad Volumétrica", MAXIMA_LONGITUD_CAPACIDAD_VOLUMETRICA }, locale);
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
	if(!Utilidades.esValido(this.getAlturaFlecha())){ this.setAlturaFlecha(0); };
	if(!Utilidades.esValido(this.getCapacidadVolumetrica())){ this.setCapacidadVolumetrica(0); };

	cadena = Float.toString(this.getAlturaFlecha()) + 
			Float.toString(this.getCapacidadVolumetrica());
	return cadena;
}
 
 
}
