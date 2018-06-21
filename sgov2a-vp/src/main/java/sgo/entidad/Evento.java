package sgo.entidad;

import java.util.Date;
import java.util.Locale;
import java.sql.Timestamp;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Evento extends EntidadBase {
	private int id_evento;
	private int tipo_evento;
	private Date fecha_hora;
	private Timestamp fechaHoraTimestamp;
	private String descripcion;
	private int tipo_registro;
	private int id_registro;
	
	public static final int TIPO_EVENTO_INCIDENCIA = 1;
	public static final int TIPO_EVENTO_ACCIDENTE = 2;
	public static final int TIPO_EVENTO_FALLA_OPERACIONAL = 3;
	public static final int TIPO_EVENTO_OBSERVACION = 4;
	
	public static final int TIPO_REGISTRO_TRANSPORTE=1;
	public static final int TIPO_REGISTRO_DESCARGA=2;
	public static final int TIPO_REGISTRO_CIERRE=3;

	// variable para hacer las validaciones.
	static final int MAXIMA_LONGITUD_DESCRIPCION = 3000;

	public int getId() {
		return id_evento;
	}

	public void setId(int Id) {
		this.id_evento = Id;
	}

	/**
	 * @return the tipo_evento
	 */
	public int getTipoEvento() {
		return tipo_evento;
	}

	/**
	 * @param tipo_evento the tipo_evento to set
	 */
	public void setTipoEvento(int tipoEvento) {
		this.tipo_evento = tipoEvento;
	}

	/**
	 * @return the fecha_hora
	 */
	public Date getFechaHora() {
		fecha_hora = new Date(this.getFechaHoraTimestamp().getTime());
		return fecha_hora;
	}

	/**
	 * @param fecha_hora the fecha_hora to set
	 */
	public void setFechaHora(Date fechaHora) {
		this.fecha_hora = fechaHora;
	}
	
	public Timestamp getFechaHoraTimestamp(){
	 if (this.fecha_hora!= null){
		this.fechaHoraTimestamp = new Timestamp(this.fecha_hora.getTime());
	 }
		return this.fechaHoraTimestamp;
	}
	
	public void setFechaHoraTimestamp(Timestamp fechaHora) {
		this.fechaHoraTimestamp = fechaHora;
	}
	
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the tipo_registro
	 */
	public int getTipoRegistro() {
		return tipo_registro;
	}

	/**
	 * @param tipo_registro the tipo_registro to set
	 */
	public void setTipoRegistro(int tipoRegistro) {
		this.tipo_registro = tipoRegistro;
	}

	/**
	 * @return the id_registro
	 */
	public int getIdRegistro() {
		return id_registro;
	}

	/**
	 * @param id_registro the id_registro to set
	 */
	public void setIdRegistro(int idRegistro) {
		this.id_registro = idRegistro;
	}

	public boolean validar(){
		boolean resultado = true;

		if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION || !Utilidades.esValido(this.descripcion)){			
			return false;
		}
		
		return resultado;
	}

	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.tipo_evento)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo de Evento" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.tipo_registro)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo de Registro" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.descripcion)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Descripción" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.fechaHoraTimestamp)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Fecha y hora" }, locale);
			return respuesta;
		  }

		  if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Descripción", MAXIMA_LONGITUD_DESCRIPCION }, locale);
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
		if(!Utilidades.esValido(this.getFechaHoraTimestamp())){ this.setFechaHoraTimestamp(new Timestamp(0)); }

		cadena = this.getDescripcion().toString() + 
				 this.getFechaHoraTimestamp().toString();
		return cadena;
	}
}