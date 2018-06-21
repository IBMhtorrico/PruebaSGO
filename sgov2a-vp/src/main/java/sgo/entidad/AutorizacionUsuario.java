package sgo.entidad;

import java.sql.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class AutorizacionUsuario extends EntidadBase{
	private int id_ausuario;
	private int id_usuario;
	private int id_autorizacion;
	private String codigo_autorizacion;
	private Date vigente_desde;
	private Date vigente_hasta;
	private int estado;
	private String identidad;
	private Usuario eUsuario;
	private Autorizacion eAutorizacion;
	private List<Usuario> usuario;
	private List<Autorizacion> autorizacion;
	
	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_CODIGO_AUTORIZACION = 16;
	
	/**
	 * @return the id_ausuario
	 */
	public int getId() {
		return id_ausuario;
	}
	/**
	 * @param id_ausuario the id_ausuario to set
	 */
	public void setId(int Id) {
		this.id_ausuario = Id;
	}
	/**
	 * @return the id_usuario
	 */
	public int getIdUsuario() {
		return id_usuario;
	}
	/**
	 * @param id_usuario the id_usuario to set
	 */
	public void setIdUsuario(int IdUsuario) {
		this.id_usuario = IdUsuario;
	}
	/**
	 * @return the id_autorizacion
	 */
	public int getIdAutorizacion() {
		return id_autorizacion;
	}
	/**
	 * @param id_autorizacion the id_autorizacion to set
	 */
	public void setIdAutorizacion(int IdAutorizacion) {
		this.id_autorizacion = IdAutorizacion;
	}
	
	/**
	 * @return the codigo_autorizacion
	 */
	public String getCodigoAutorizacion() {
		return codigo_autorizacion;
	}
	/**
	 * @param codigo_autorizacion the codigo_autorizacion to set
	 */
	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigo_autorizacion = codigoAutorizacion;
	}
	/**
	 * @return the vigente_desde
	 */
	public Date getVigenteDesde() {
		return vigente_desde;
	}
	/**
	 * @param vigente_desde the vigente_desde to set
	 */
	public void setVigenteDesde(Date vigenteDesde) {
		this.vigente_desde = vigenteDesde;
	}
	/**
	 * @return the vigente_hasta
	 */
	public Date getVigenteHasta() {
		return vigente_hasta;
	}
	/**
	 * @param vigente_hasta the vigente_hasta to set
	 */
	public void setVigenteHasta(Date vigenteHasta) {
		this.vigente_hasta = vigenteHasta;
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
	/**
	 * @return the usuario
	 */
	public List<Usuario> getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(List<Usuario> usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the autorizacion
	 */
	public List<Autorizacion> getAutorizacion() {
		return autorizacion;
	}
	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(List<Autorizacion> autorizacion) {
		this.autorizacion = autorizacion;
	}
	/**
	 * @return the eUsuario
	 */
	public Usuario geteUsuario() {
		return eUsuario;
	}
	/**
	 * @param eUsuario the eUsuario to set
	 */
	public void seteUsuario(Usuario eUsuario) {
		this.eUsuario = eUsuario;
	}
	/**
	 * @return the eAutorizacion
	 */
	public Autorizacion geteAutorizacion() {
		return eAutorizacion;
	}
	/**
	 * @param eAutorizacion the eAutorizacion to set
	 */
	public void seteAutorizacion(Autorizacion eAutorizacion) {
		this.eAutorizacion = eAutorizacion;
	}
	/**
	 * @return the identidad
	 */
	public String getIdentidad() {
		return identidad;
	}
	/**
	 * @param identidad the identidad to set
	 */
	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){	
	Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.codigo_autorizacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Código" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.vigente_desde)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vigencia Desde" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.vigente_hasta)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vigencia Hasta" }, locale);
		return respuesta;
	  }

	  if (this.codigo_autorizacion.length() > MAXIMA_LONGITUD_CODIGO_AUTORIZACION){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Código", MAXIMA_LONGITUD_CODIGO_AUTORIZACION }, locale);
		return respuesta;
	  }
	  if (!Utilidades.validarFecha(this.vigente_desde.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
	    respuesta.estado = false;
	    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Vigencia Desde" }, locale);
	    return respuesta;
	  }
	  if (!Utilidades.validarFecha(this.vigente_hasta.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
	    respuesta.estado = false;
	    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Vigencia Hasta" }, locale);
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
	if(!Utilidades.esValido(this.getCodigoAutorizacion())){ this.setCodigoAutorizacion(""); };
	if(!Utilidades.esValido(this.getVigenteDesde())){ this.setVigenteDesde(new Date(0)); };
	if(!Utilidades.esValido(this.getVigenteHasta())){ this.setVigenteHasta(new Date(0)); };

	cadena = this.getCodigoAutorizacion().toString() + 
			 this.getVigenteDesde().toString() +
			 this.getVigenteHasta().toString() ;
	return cadena;
}

}
