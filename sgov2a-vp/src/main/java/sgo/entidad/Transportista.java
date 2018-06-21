package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Transportista extends EntidadBase {
	private int id_transportista;
	private String razon_social;
	private String nombre_corto;
	private String ruc;
	private int estado;
	private String sincronizado_el;
	private String fecha_referencia;
	private String codigo_referencia;

	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_RAZON_SOCIAL = 150;
	static final int MAXIMA_LONGITUD_NOMBRE_CORTO = 20;
	static final int MAXIMA_LONGITUD_RUC = 11;

	public int getId() {
		return id_transportista;
	}

	public void setId(int Id) {
		this.id_transportista = Id;
	}

	public String getRazonSocial() {
		return razon_social;
	}

	public void setRazonSocial(String RazonSocial) {
		this.razon_social = RazonSocial;
	}

	public String getNombreCorto() {
		return nombre_corto;
	}

	public void setNombreCorto(String NombreCorto) {
		this.nombre_corto = NombreCorto;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String Ruc) {
		this.ruc = Ruc;
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
	
public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (this.razon_social.length()> MAXIMA_LONGITUD_RAZON_SOCIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Razón Social", MAXIMA_LONGITUD_RAZON_SOCIAL }, locale);
		return respuesta;
	  }

	  if (this.nombre_corto.length()> MAXIMA_LONGITUD_NOMBRE_CORTO){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre Corto", MAXIMA_LONGITUD_NOMBRE_CORTO }, locale);
		return respuesta;
	  }
	
	  if ((this.ruc.length() != MAXIMA_LONGITUD_RUC)){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "RUC", MAXIMA_LONGITUD_RUC }, locale);
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
	if(!Utilidades.esValido(this.getRazonSocial())){ this.setRazonSocial(""); };
	if(!Utilidades.esValido(this.getNombreCorto())){ this.setNombreCorto(""); };
	if(!Utilidades.esValido(this.getRuc())){ this.setRuc(""); };

	cadena = this.getRazonSocial().toString() + 
			 this.getNombreCorto().toString() + 
			 this.getRuc().toString(); 
	return cadena;
}
	
		
}