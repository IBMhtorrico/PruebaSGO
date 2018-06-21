package sgo.utilidades;

import org.springframework.context.MessageSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import java.util.Locale;

import sgo.entidad.AforoCisterna;
import sgo.entidad.AforoTanque;
import sgo.entidad.AutorizacionUsuario;
import sgo.entidad.Cisterna;
import sgo.entidad.Cliente;
import sgo.entidad.Compartimento;
import sgo.entidad.Conductor;
import sgo.entidad.Contometro;
import sgo.entidad.ContometroJornada;
import sgo.entidad.Desconche;
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DetalleTransporte;
import sgo.entidad.Documento;
import sgo.entidad.Enlace;
import sgo.entidad.Estacion;
import sgo.entidad.Evento;
import sgo.entidad.Jornada;
import sgo.entidad.Muestreo;
import sgo.entidad.Operacion;
import sgo.entidad.Operario;
import sgo.entidad.Parametro;
import sgo.entidad.Permiso;
import sgo.entidad.Planificacion;
import sgo.entidad.Planta;
import sgo.entidad.Producto;
import sgo.entidad.Propietario;
import sgo.entidad.Respuesta;
import sgo.entidad.Rol;
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.entidad.Tolerancia;
import sgo.entidad.Tracto;
import sgo.entidad.Transporte;
import sgo.entidad.Transportista;
import sgo.entidad.Usuario;
import sgo.entidad.Vehiculo;
import sgo.entidad.Vigencia;
import sgo.filtros.RequestWrapper;

/**
 * Clase base para hacer las validaciones.
 * 
 * @author IBM DEL PERÚ / knavarro
 * @since 18/III/2016
 */
public class BaseUtilidades {

 private static Logger logger = Logger.getLogger(RequestWrapper.class);
	
 public static Respuesta validacionXSS(Cliente eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }

 public static Respuesta validacionXSS(Transportista eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	respuesta = validacionXSS(eEntidad.getCadena());
	if (respuesta.estado == false){
	 respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
	 return respuesta;
	} 
	respuesta = eEntidad.validar(gestorDiccionario, locale);
	if (respuesta.estado == false){
	 return respuesta;
	} 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Planta eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	respuesta = validacionXSS(eEntidad.getCadena());
	if (respuesta.estado == false){
	 respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
	 return respuesta;
	} 
	respuesta = eEntidad.validar(gestorDiccionario, locale);
	if (respuesta.estado == false){
	 return respuesta;
	} 
	
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	   respuesta.estado = false;
	  }
	  return respuesta;
	 }
 
 public static Respuesta validacionXSS(Usuario eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	respuesta = validacionXSS(eEntidad.getCadena());
	if (respuesta.estado == false){
	 respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
	 return respuesta;
	} 
	respuesta = eEntidad.validar(gestorDiccionario, locale);
	if (respuesta.estado == false){
	 return respuesta;
	} 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Vehiculo eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	respuesta = validacionXSS(eEntidad.getCadena());
	if (respuesta.estado == false){
	 respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
	 return respuesta;
	} 
	respuesta = eEntidad.validar(gestorDiccionario, locale);
	if (respuesta.estado == false){
	 return respuesta;
	} 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Vigencia eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	respuesta = validacionXSS(eEntidad.getCadena());
	if (respuesta.estado == false){
	 respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
	 return respuesta;
	} 
	respuesta = eEntidad.validar(gestorDiccionario, locale);
	if (respuesta.estado == false){
	 return respuesta;
	} 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Tracto eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Tanque eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static Respuesta validacionXSS(Propietario eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 } 
 
 public static Respuesta validacionXSS(AforoCisterna eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 } 
 
 public static Respuesta validacionXSS(Producto eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 } 

 public static Respuesta validacionXSS(Parametro eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Operario eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Operacion eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Estacion eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Tolerancia eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Enlace eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Documento eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Contometro eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Conductor eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Cisterna eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(AforoTanque eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Compartimento eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Permiso eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Rol eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(AutorizacionUsuario eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  

 //MODULO PLANIFICACION
 public static Respuesta validacionXSS(Planificacion eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 //MODULO PROGRAMACION
 public static Respuesta validacionProgramacionXSS(DetalleProgramacion eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validarProgramacion(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }   
 
 public static Respuesta validacionCompletarProgramacionXSS(DetalleProgramacion eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validarCompletarProgramacion(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }   
 
//MODULO TRANSPORTE
 public static Respuesta validacionTransporteXSS(Transporte eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadenaTransporte());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validarTransporte(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 } 

 public static Respuesta validacionPesajeTransporteXSS(Transporte eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadenaPesaje());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validarPesajeTransporte(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(DetalleTransporte eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
//Modulo_desconche
 public static Respuesta validacionXSS(Desconche eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 }  
 
 public static Respuesta validacionXSS(Evento eEntidad, MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
    respuesta.estado = true;
    respuesta.valor = "";
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
   respuesta.estado = false;
  }
  return respuesta;
 } 
 
//MODULO JORNADA
public static Respuesta validacionXSS(Jornada eEntidad, MessageSource gestorDiccionario, Locale locale){
 Respuesta respuesta = new Respuesta();
 try {
	  respuesta = validacionXSS(eEntidad.getCadena());
	  if (respuesta.estado == false){
		  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
		  return respuesta;
	  } 
	  respuesta = eEntidad.validar(gestorDiccionario, locale);
	  if (respuesta.estado == false){
		  return respuesta;
	  } 
   respuesta.estado = true;
   respuesta.valor = "";
 } catch (Exception ex) {
  ex.printStackTrace();
  respuesta.error = Constante.EXCEPCION_GENERICA;
  respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
  respuesta.estado = false;
 }
 return respuesta;
} 

public static Respuesta validacionContometroAperturaXSS(ContometroJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarApertura(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
} 

public static Respuesta validacionContometroCierreXSS(ContometroJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarCierre(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
} 

public static Respuesta validacionTanqueAperturaXSS(TanqueJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarApertura(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
} 

public static Respuesta validacionTanqueCierreXSS(TanqueJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarCierre(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
}

public static Respuesta validacionCambioTanqueFinalXSS(TanqueJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarCambioTanqueFinal(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
}

public static Respuesta validacionCambioTanqueInicialXSS(TanqueJornada eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validarCambioTanqueInicial(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
}

public static Respuesta validacionXSS(Muestreo eEntidad, MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
		  respuesta = validacionXSS(eEntidad.getCadena());
		  if (respuesta.estado == false){
			  respuesta.valor = gestorDiccionario.getMessage("sgo.erroValoresFormulario", null, locale);
			  return respuesta;
		  } 
		  respuesta = eEntidad.validar(gestorDiccionario, locale);
		  if (respuesta.estado == false){
			  return respuesta;
		  } 
	 respuesta.estado = true;
	 respuesta.valor = "";
	} catch (Exception ex) {
	ex.printStackTrace();
	respuesta.error = Constante.EXCEPCION_GENERICA;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	respuesta.estado = false;
	}
	return respuesta;
}
 
 private static Respuesta validacionXSS(String cadena){
  Respuesta respuesta = new Respuesta();
  try {
	  //busqueda de caracteres en el texto
		if(cadena.indexOf('<') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	    } 
		if(cadena.indexOf('>') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('!') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('{') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('}') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf(']') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('[') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('?') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf('%') != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }

		//busqueda de cadenas en el texto
		if(cadena.indexOf("www.") != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf("http") != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf("https") != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	     }
		if(cadena.indexOf("://") != -1){
			respuesta.estado = false;
			respuesta.valor = null;
			return respuesta;
	    }
    respuesta.estado = true;
    respuesta.valor = cadena;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static String cleanXSS(String value) {
	 if (!Utilidades.esValido(value)){
		 return "";
	 }
	// You'll need to remove the spaces from the html entities below
	//logger.info("InnXSS RequestWrapper ..............." + value);
	//value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
	//value = value.replaceAll("'", "& #39;");
	value = value.replaceAll("eval\\((.*)\\)", "");
	value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

	value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
	value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
	value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
	value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
	value = value.replaceAll("<script>", "");
	value = value.replaceAll("</script>", "");
	value = value.replaceAll("<", "").replaceAll(">", "");
	value = value.replaceAll("://", "").replaceAll("https", "");
	value = value.replaceAll("www", "").replaceAll("http", "");

	
	//logger.info("OutnXSS RequestWrapper ........ value ......." + value);
	return value;
}
 
}
