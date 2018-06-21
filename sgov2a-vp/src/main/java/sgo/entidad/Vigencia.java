package sgo.entidad;

import java.sql.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Vigencia extends EntidadBase {
	private int id_vigencia;
	private int id_documento;
	private String numero_documento;
	private Date fecha_emision;
	private Date fecha_expiracion;
	private int id_entidad;
	private int pertenece_a;
	private Documento documento;
	private Cisterna cisterna;
	private Conductor conductor;
	
	//ESTO PARA EL VALOR DEL CAMPO entidad
	public final static int DOCUMENTO_CONDUCTOR=1;
	public final static int DOCUMENTO_CISTERNA=2;
	static final int MAXIMA_LONGITUD_NUMERO_DOCUMENTO=20;
	
	public int getId() {
		return id_vigencia;
	}
	
	public void setId(int id) {
		this.id_vigencia = id;
	}
	
	public int getIdDocumento() {
		return id_documento;
	}
	
	public void setIdDocumento(int idDocumento) {
		this.id_documento = idDocumento;
	}
	
	public String getNumeroDocumento() {
		return numero_documento;
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		this.numero_documento = numeroDocumento;
	}
	
	public Date getFechaEmision() {
		return fecha_emision;
	}
	
	public void setFechaEmision(Date fechaEmision) {
		this.fecha_emision = fechaEmision;
	}
	
	public Date getFechaExpiracion() {
		return fecha_expiracion;
	}
	
	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fecha_expiracion = fechaExpiracion;
	}
	
	public int getIdEntidad() {
		return id_entidad;
	}
	
	public void setIdEntidad(int idEntidad) {
		this.id_entidad = idEntidad;
	}
	
	public int getPerteneceA() {
		return pertenece_a;
	}
	
	public void setPerteneceA(int perteneceA) {
		this.pertenece_a = perteneceA;
	}
	
	public Cisterna getCisterna() {
		return cisterna;
	}
	
	public void setCisterna(Cisterna cisterna) {
		this.cisterna = cisterna;
	}
	
	public Conductor getConductor() {
		return conductor;
	}
	
	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		if (this.numero_documento.length() > MAXIMA_LONGITUD_NUMERO_DOCUMENTO){	
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Número de Documento", MAXIMA_LONGITUD_NUMERO_DOCUMENTO }, locale);
		  return respuesta;
		}
		  
		if (!Utilidades.esValidoForingKey(this.id_entidad)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Entidad" }, locale);
		  return respuesta;
		}
	
		if (!Utilidades.esValidoForingKey(this.id_documento)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Documento" }, locale);
		  return respuesta;
		}
		
		if (!Utilidades.validarFecha(this.fecha_emision.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Fecha Emisión" }, locale);
		  return respuesta;
		}
	
		if (!Utilidades.validarFecha(this.fecha_expiracion.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Fecha Expiración" }, locale);
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
		if(!Utilidades.esValido(this.getNumeroDocumento())){ this.setNumeroDocumento(""); };
		if(!Utilidades.esValido(this.getFechaEmision())){ this.setFechaEmision(new Date(0)); };
		if(!Utilidades.esValido(this.getFechaExpiracion())){ this.setFechaExpiracion(new Date(0)); };

		cadena = this.getNumeroDocumento().toString() + 
				 this.getFechaEmision().toString() + 
				 this.getFechaExpiracion().toString() ;
		return cadena;
	}
}