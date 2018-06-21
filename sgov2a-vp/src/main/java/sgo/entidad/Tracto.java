package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Tracto extends EntidadBase {
	private int id_tracto;
	private String placa;
	private int estado;
	private int id_transportista;
	private String razon_social_transportista;
	private String sincronizado_el;
	private String fecha_referencia;
	private String codigo_referencia;
	private Transportista transportista;

	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_PLACA = 15;
	static final int MAXIMA_LONGITUD_CODIGO_REFERENCIA = 20;
	
	/**
	 * @return the razon_social_transportista
	 */
	public String getRazonSocialTransportista() {
		return razon_social_transportista;
	}

	/**
	 * @param razon_social_transportista the razon_social_transportista to set
	 */
	public void setRazonSocialTransportista(String razonSocialTransportista) {
		this.razon_social_transportista = razonSocialTransportista;
	}

	public int getId() {
		return id_tracto;
	}

	public void setId(int Id) {
		this.id_tracto = Id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String Placa) {
		this.placa = Placa;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public int getIdTransportista() {
		return id_transportista;
	}

	public void setIdTransportista(int IdTransportista) {
		this.id_transportista = IdTransportista;
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

	public Transportista getTransportista() {
		return transportista;
	}

	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}

	@Override
	public String toString() {
		return "Tracto [id_tracto=" + id_tracto + ", placa=" + placa
				+ ", estado=" + estado + ", id_transportista="
				+ id_transportista + ", sincronizado_el=" + sincronizado_el
				+ ", fecha_referencia=" + fecha_referencia
				+ ", codigo_referencia=" + codigo_referencia
				+ ", transportista=" + transportista + "]";
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.placa.length() > MAXIMA_LONGITUD_PLACA  || !Utilidades.esValido(this.placa)){		
			return false;
		}
		
		if (this.codigo_referencia.length() > MAXIMA_LONGITUD_CODIGO_REFERENCIA  || !Utilidades.esValido(this.codigo_referencia)){		
			return false;
		}
				
		if(!Utilidades.esValido(this.id_transportista)){
			return false;
		}
		
		if(!Utilidades.esValido(this.estado)){
			return false;
		}

		return resultado;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		 if (this.placa.length() > MAXIMA_LONGITUD_PLACA){	
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Placa", MAXIMA_LONGITUD_PLACA }, locale);
		  return respuesta;
		}
		
		if (this.codigo_referencia.length() > MAXIMA_LONGITUD_CODIGO_REFERENCIA){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Placa", MAXIMA_LONGITUD_CODIGO_REFERENCIA }, locale);
			return respuesta;
		}		 

		if(!Utilidades.esValidoForingKey(this.id_transportista)){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Transportista" }, locale);
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
		if(!Utilidades.esValido(this.getPlaca())){ this.setPlaca(""); };

		cadena = this.getPlaca().toString();
		return cadena;
	}

}