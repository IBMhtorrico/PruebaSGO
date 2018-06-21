package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;


public class ContometroJornada {
	private int id_cjornada;
	private int id_jornada;
	private float lectura_inicial;
	private float lectura_final;
	private int estado_servicio;
	private int id_contometro;
	private String descripcionContometro;
	private int id_producto;
	
	private Producto producto;
	private Jornada jornada;
	private Contometro contometro;
	
	public static final int ESTADO_ABIERTO=1;
	public static final int ESTADO_CERRADO=2;
	
	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_LECTURA_INICIAL=10;
	static final int MAXIMA_LONGITUD_LECTURA_FINAL=10;
	
	public int getId() {
		return id_cjornada;
	}
	public void setId(int id_cjornada) {
		this.id_cjornada = id_cjornada;
	}
	public int getIdJornada() {
		return id_jornada;
	}
	public void setIdJornada(int id_jornada) {
		this.id_jornada = id_jornada;
	}
	public float getLecturaInicial() {
		return lectura_inicial;
	}
	public void setLecturaInicial(float lectura_inicial) {
		this.lectura_inicial = lectura_inicial;
	}
	public float getLecturaFinal() {
		return lectura_final;
	}
	public void setLecturaFinal(float lectura_final) {
		this.lectura_final = lectura_final;
	}
	public int getEstadoServicio() {
		return estado_servicio;
	}
	public void setEstadoServicio(int estado_servicio) {
		this.estado_servicio = estado_servicio;
	}
	public int getIdContometro() {
		return id_contometro;
	}
	public void setIdContometro(int id_contometro) {
		this.id_contometro = id_contometro;
	}
	public int getIdProducto() {
		return id_producto;
	}
	public void setIdProducto(int idProducto) {
		this.id_producto = idProducto;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Jornada getJornada() {
		return jornada;
	}
	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}
	public Contometro getContometro() {
		return contometro;
	}
	public void setContometro(Contometro contometro) {
		this.contometro = contometro;
	}
	
	//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
	public String getCadena() {
		String cadena="";
		if(!Utilidades.esValido(this.getLecturaInicial())){ this.setLecturaInicial(0); };
		if(!Utilidades.esValido(this.getLecturaFinal())){ this.setLecturaFinal(0); };
		
		cadena = String.valueOf(this.getLecturaInicial()) + 
				 String.valueOf(this.getLecturaFinal()); 

		return cadena;
	}	

	public Respuesta validarApertura(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
		  if (!Utilidades.esValidoForingKey(this.id_producto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_contometro)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Contómetro" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.lectura_inicial) || this.lectura_inicial == 0){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Lectura Inicial" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.estado_servicio)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
			return respuesta;
		  }

		  if (String.valueOf(this.lectura_inicial).length() > MAXIMA_LONGITUD_LECTURA_INICIAL){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Lectura Inicial", MAXIMA_LONGITUD_LECTURA_INICIAL }, locale);
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
	
	public Respuesta validarCierre(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
		  if (!Utilidades.esValidoForingKey(this.id_producto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_contometro)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Contómetro" }, locale);
			return respuesta;
		  }

		  if (!Utilidades.esValido(this.lectura_inicial) || this.lectura_inicial == 0){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Lectura Inicial" }, locale);
			return respuesta;
		  }
		  
		  if (!Utilidades.esValido(this.lectura_final) || this.lectura_final == 0){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Lectura Final" }, locale);
			return respuesta;
		  }
		  
		  if (!Utilidades.esValido(this.estado_servicio)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
			return respuesta;
		  }

		  if (String.valueOf(this.lectura_inicial).length() > MAXIMA_LONGITUD_LECTURA_INICIAL){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Lectura Inicial", MAXIMA_LONGITUD_LECTURA_INICIAL }, locale);
			return respuesta;
		  }
		  
		  if (String.valueOf(this.lectura_final).length() > MAXIMA_LONGITUD_LECTURA_FINAL){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Lectura Final", MAXIMA_LONGITUD_LECTURA_FINAL }, locale);
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
	public String getDescripcionContometro() {
		return descripcionContometro;
	}
	public void setDescripcionContometro(String descripcionContometro) {
		this.descripcionContometro = descripcionContometro;
	}
}