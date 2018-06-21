package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Planta extends EntidadBase {
  private int id_planta;
  private String descripcion;
  //private String abreviatura;
  private int estado;
  private String sincronizado_el;
  private String fecha_referencia;
  private String codigo_referencia;
  private String correoPara;
  private String correoCC;
  
  //variables para hacer las validaciones.
  static final int MAXIMA_LONGITUD_DESCRIPCION=150;
  static final int MAXIMA_LONGITUD_CORREO_PARA=250;
  static final int MAXIMA_LONGITUD_CORREO_CC=250;

  public int getId(){
    return id_planta;
  }

  public void setId(int Id ){
    this.id_planta=Id;
  }
  
  public String getDescripcion(){
    return descripcion;
  }

  public void setDescripcion(String Descripcion ){
    this.descripcion=Descripcion;
  }
  
  public int getEstado(){
    return estado;
  }

  public void setEstado(int Estado ){
    this.estado=Estado;
  }
  
  public String getSincronizadoEl(){
    return sincronizado_el;
  }

  public void setSincronizadoEl(String SincronizadoEl ){
    this.sincronizado_el=SincronizadoEl;
  }
  
  public String getFechaReferencia(){
    return fecha_referencia;
  }

  public void setFechaReferencia(String FechaReferencia ){
    this.fecha_referencia=FechaReferencia;
  }
  
  public String getCodigoReferencia(){
    return codigo_referencia;
  }

  public void setCodigoReferencia(String CodigoReferencia ){
    this.codigo_referencia=CodigoReferencia;
  }
  
  public boolean validar(){
		boolean resultado = true;

		if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION  || !Utilidades.esValido(this.descripcion)){		
			return false;
		}
		
		if(!Utilidades.esValido(this.estado)){
			return false;
		}
		
		if(!Utilidades.esValido(this.correoPara)){
			return false;
		}
		
		if(!Utilidades.esValido(this.correoCC)){
			return false;
		}

		return resultado;
	}
  
  public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		
			if (!Utilidades.esValido(this.correoPara)) {
				respuesta.estado = false;
				respuesta.valor = gestorDiccionario.getMessage(
						"sgo.errorValoresNulosEntidad",
						new Object[] { "PARA" }, locale);
				return respuesta;
			}
			/*
			 * if (!Utilidades.esValido(this.correoCC)){ respuesta.estado =
			 * false; respuesta.valor =
			 * gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new
			 * Object[] { "CC" }, locale); return respuesta; }
			 */		
		if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION  || !Utilidades.esValido(this.descripcion)){			
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Descripcion", MAXIMA_LONGITUD_DESCRIPCION }, locale);
		  return respuesta;
		}
		if (String.valueOf(this.correoPara).length() > MAXIMA_LONGITUD_CORREO_PARA) {
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage(
					"sgo.errorValoresEntidad", new Object[] {
							"Correo Para", MAXIMA_LONGITUD_CORREO_PARA },
					locale);
			return respuesta;
		}
		if (String.valueOf(this.correoCC).length() > MAXIMA_LONGITUD_CORREO_CC) {
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage(
					"sgo.errorValoresEntidad", new Object[] { "Correo CC",
							MAXIMA_LONGITUD_CORREO_CC }, locale);
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
		if(!Utilidades.esValido(this.getDescripcion())){ this.setDescripcion(""); };
		if(!Utilidades.esValido(this.getCorreoPara())){ this.setCorreoPara(""); };
		if(!Utilidades.esValido(this.getCorreoCC())){ this.setCorreoCC(""); };
		
		cadena = this.getDescripcion().toString() +
				 this.getCorreoPara().toString() +
				 this.getCorreoCC().toString() ;
		return cadena;
	}
	
	public String getCorreoPara() {
		return correoPara;
	}

	public void setCorreoPara(String correoPara) {
		this.correoPara = correoPara;
	}

	public String getCorreoCC() {
		return correoCC;
	}

	public void setCorreoCC(String correoCC) {
		this.correoCC = correoCC;
	}
  
}