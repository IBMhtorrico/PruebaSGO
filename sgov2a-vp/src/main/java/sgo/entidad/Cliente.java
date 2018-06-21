package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Cliente extends EntidadBase {
	/**
	 * 
	 */

	private int id_cliente;
	private String nombre_corto;
	private String razon_social;
	private String numero_contrato;
	private String descripcion_contrato;
	private String ruc;
	private int estado;

	static final int MAXIMA_LONGITUD_NOMBRE_CORTO=20;
	static final int MAXIMA_LONGITUD_RAZON_SOCIAL=150;
	static final int MAXIMA_LONGITUD_RUC=11;
	static final int MAXIMA_LONGITUD_NUMERO_CONTRATO=20;
	static final int MAXIMA_LONGITUD_DESCRIPCION_CONTRATO=200;
	
	public final static int ESTADO_ACTIVO=1;
	public final static int ESTADO_INACTIVO=2;

	public int getId(){
	  return id_cliente;
	}

	public void setId(int Id ){
	  this.id_cliente=Id;
	}

	public String getNombreCorto(){
	  return nombre_corto;
	}

	public void setNombreCorto(String nombreCorto ){
	  this.nombre_corto=nombreCorto;
	}

	public String getRazonSocial(){
	  return razon_social;
	}

	public void setRazonSocial(String razonSocial ){
	  this.razon_social=razonSocial;
	}
	
	/**
	 * @return the numero_contrato
	 */
	public String getNumeroContrato() {
		return numero_contrato;
	}

	/**
	 * @param numero_contrato the numero_contrato to set
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numero_contrato = numeroContrato;
	}

	/**
	 * @return the descripcion_contrato
	 */
	public String getDescripcionContrato() {
		return descripcion_contrato;
	}

	/**
	 * @param descripcion_contrato the descripcion_contrato to set
	 */
	public void setDescripcionContrato(String descripcionContrato) {
		this.descripcion_contrato = descripcionContrato;
	}

	public String getRuc(){
	  return ruc;
	}

	public void setRuc(String ruc ){
	  this.ruc=ruc;
	}

	public int getEstado(){
	  return estado;
	}

	public void setEstado(int estado ){
	  this.estado=estado;
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
		  if ((this.numero_contrato.length() > MAXIMA_LONGITUD_NUMERO_CONTRATO)){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Número de Contrato", MAXIMA_LONGITUD_NUMERO_CONTRATO }, locale);
			return respuesta;
		  }
		  if ((this.descripcion_contrato.length() > MAXIMA_LONGITUD_DESCRIPCION_CONTRATO)){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Descripción de Contrato", MAXIMA_LONGITUD_DESCRIPCION_CONTRATO }, locale);
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
		if(!Utilidades.esValido(this.getNumeroContrato())){ this.setNumeroContrato(""); };
		if(!Utilidades.esValido(this.getDescripcionContrato())){ this.setDescripcionContrato(""); };

		cadena = this.getRazonSocial().toString() + 
				 this.getNombreCorto().toString() + 
				 this.getRuc().toString() + 
				 this.getNumeroContrato().toString() + 
				 this.getDescripcionContrato().toString();
		return cadena;
	}
	
}