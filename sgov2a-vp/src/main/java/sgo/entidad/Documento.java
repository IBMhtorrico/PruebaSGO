package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Documento extends EntidadBase {
	private int id_documento;
	private int pertenece_a;
	private int periodo_vigencia;
	private int tiempo_alerta;
	private String nombre_documento;

	//ESTO PARA EL VALOR DEL CAMPO pertenece_a
	public final static int DOCUMENTO_CONDUCTOR=1;
	public final static int DOCUMENTO_CISTERNA=2;
	
	public final static int MAXIMA_LONGITUD_NOMBRE_DOCUMENTO=20;
	
	public int getId() {
		return id_documento;
	}
	
	public void setId(int id) {
		this.id_documento = id;
	}
	
	public int getPerteneceA() {
		return pertenece_a;
	}
	
	public void setPerteneceA(int perteneceA) {
		this.pertenece_a = perteneceA;
	}
	
	public int getPeriodoVigencia() {
		return periodo_vigencia;
	}
	
	public void setPeriodoVigencia(int periodoVigencia) {
		this.periodo_vigencia = periodoVigencia;
	}
	
	public int getTiempoAlerta() {
		return tiempo_alerta;
	}
	
	public void setTiempoAlerta(int tiempoAlerta) {
		this.tiempo_alerta = tiempoAlerta;
	}
	
	public String getNombreDocumento() {
		return nombre_documento;
	}
	
	public void setNombreDocumento(String nombreDocumento) {
		this.nombre_documento = nombreDocumento;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.nombre_documento)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Documento" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.pertenece_a)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Aplica Para" }, locale);
			return respuesta;
		  }
		  if (this.nombre_documento.length() > MAXIMA_LONGITUD_NOMBRE_DOCUMENTO){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Documento", MAXIMA_LONGITUD_NOMBRE_DOCUMENTO }, locale);
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
		if(!Utilidades.esValido(this.getNombreDocumento())){ this.setNombreDocumento(""); };

		cadena = this.getNombreDocumento().toString() ;
		return cadena;
	}
	
}