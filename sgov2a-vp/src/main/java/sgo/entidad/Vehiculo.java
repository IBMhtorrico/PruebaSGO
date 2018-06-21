package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Vehiculo extends EntidadBase {
	private int id_vehiculo;
	private String nombre_corto;
	private String descripcion;
	private int id_propietario;
	private int estado;
	private String sincronizado_el;
	private String fecha_referencia;
	private String codigo_referencia;
	private Propietario propietario;
	private String razonSocialPropietario;
	
	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_NOMBRE_CORTO = 20;
	static final int MAXIMA_LONGITUD_DESCRIPCION = 80;
	
	

	public int getId() {
		return id_vehiculo;
	}

	public void setId(int Id) {
		this.id_vehiculo = Id;
	}

	public String getNombreCorto() {
		return nombre_corto;
	}

	public void setNombreCorto(String NombreCorto) {
		this.nombre_corto = NombreCorto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String Descripcion) {
		this.descripcion = Descripcion;
	}

	public int getIdPropietario() {
		return id_propietario;
	}

	public void setIdPropietario(int IdPropietario) {
		this.id_propietario = IdPropietario;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public String getSincronizadoEl() {
		return sincronizado_el;
	}

	public void setSincronizadoEl(String SincronizadoEl) {
		this.sincronizado_el = SincronizadoEl;
	}

	public String getFechaReferencia() {
		return fecha_referencia;
	}

	public void setFechaReferencia(String FechaReferencia) {
		this.fecha_referencia = FechaReferencia;
	}

	public String getCodigoReferencia() {
		return codigo_referencia;
	}

	public void setCodigoReferencia(String CodigoReferencia) {
		this.codigo_referencia = CodigoReferencia;
	}

	public Propietario getPropietario() {
		return propietario;
	}

	public void setPropietario(Propietario propietario) {
		this.propietario = propietario;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (this.nombre_corto.length()> MAXIMA_LONGITUD_NOMBRE_CORTO){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre Corto", MAXIMA_LONGITUD_NOMBRE_CORTO }, locale);
			return respuesta;
		  }
		  if (this.descripcion.length()> MAXIMA_LONGITUD_DESCRIPCION){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Descripción", MAXIMA_LONGITUD_DESCRIPCION }, locale);
			return respuesta;
		  }
		  if(!Utilidades.esValidoForingKey(this.id_propietario)){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Propietario" }, locale);
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
		if(!Utilidades.esValido(this.getNombreCorto())){ this.setNombreCorto(""); };
		if(!Utilidades.esValido(this.getDescripcion())){ this.setDescripcion(""); };

		cadena = this.getNombreCorto().toString() + 
				 this.getDescripcion().toString() ;
		return cadena;
	}

	public String getRazonSocialPropietario() {
		return razonSocialPropietario;
	}

	public void setRazonSocialPropietario(String razonSocialPropietario) {
		this.razonSocialPropietario = razonSocialPropietario;
	}
	
}