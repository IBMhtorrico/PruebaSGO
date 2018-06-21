package sgo.entidad;

import java.util.Date;
import java.util.Locale;
import java.sql.Timestamp;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Muestreo extends EntidadBase {
  private int id_muestreo;
  private int id_jornada;
  private Timestamp hora_muestreo;
  private Date horaMuestreoDate;
  private int producto_muestreado;
  private float api_muestreo;
  private float temperatura_muestreo;
  private float factor_muestreo;
  private Producto producto;
  private int origen;
  
  private String descripcionProducto;
  
  public final static int ORIGEN_MUESTREO=1;
  public final static int ORIGEN_CIERRE=2;
  
static final int MAXIMA_LONGITUD_API=4;
static final int MAXIMA_LONGITUD_TEMPERATURA=4;
static final int MAXIMA_LONGITUD_FACTOR=10;
  
  public int getId(){
    return id_muestreo;
  }

  public void setId(int Id ){
    this.id_muestreo=Id;
  }

public int getIdJornada() {
	return id_jornada;
}

public void setIdJornada(int idJornada) {
	this.id_jornada = idJornada;
}

public Timestamp getHoraMuestreo() {
	return hora_muestreo;
}

public void setHoraMuestreo(Timestamp horaMuestreo) {
	this.hora_muestreo = horaMuestreo;
}

public int getProductoMuestreado() {
	return producto_muestreado;
}

public void setProductoMuestreado(int productoMuestreado) {
	this.producto_muestreado = productoMuestreado;
}

public float getApiMuestreo() {
	return api_muestreo;
}

public void setApiMuestreo(float apiMuestreo) {
	this.api_muestreo = apiMuestreo;
}

public float getTemperaturaMuestreo() {
	return temperatura_muestreo;
}

public void setTemperaturaMuestreo(float temperaturaMuestreo) {
	this.temperatura_muestreo = temperaturaMuestreo;
}

public float getFactorMuestreo() {
	return factor_muestreo;
}

public void setFactorMuestreo(float factorMuestreo) {
	this.factor_muestreo = factorMuestreo;
}

public Producto getProducto() {
	return producto;
}

public void setProducto(Producto producto) {
	this.producto = producto;
}

public int getOrigen() {
	return origen;
}

public void setOrigen(int origen) {
	this.origen = origen;
}

public Date getHoraMuestreoDate() {
	return horaMuestreoDate;
}

public void setHoraMuestreoDate(Date horaMuestreoDate) {
	this.horaMuestreoDate = horaMuestreoDate;
}
  
//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
	public String getCadena() {
		String cadena="";
		if(!Utilidades.esValido(this.getApiMuestreo())){ this.setApiMuestreo(0); };
		if(!Utilidades.esValido(this.getTemperaturaMuestreo())){ this.setTemperaturaMuestreo(0); };
		if(!Utilidades.esValido(this.getFactorMuestreo())){ this.setFactorMuestreo(0); };
		if(!Utilidades.esValido(this.getHoraMuestreo())){ this.setHoraMuestreo(new Timestamp(0)); };
		
		cadena = String.valueOf(this.getApiMuestreo()) + 
				 String.valueOf(this.getTemperaturaMuestreo()) + 
				 String.valueOf(this.getFactorMuestreo()) + 
				 String.valueOf(this.getHoraMuestreo());  

		return cadena;
	}	
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  
		  String texto = " del producto " + this.getDescripcionProducto();
			
		  if (!Utilidades.esValidoForingKey(this.producto_muestreado)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.api_muestreo) || this.api_muestreo == 0){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F" + texto }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.temperatura_muestreo) || this.temperatura_muestreo == 0){		
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura" + texto }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.factor_muestreo) || this.factor_muestreo == 0){		
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor" + texto }, locale);
			return respuesta;
		  }
		  
		  if (String.valueOf(this.api_muestreo).length() > MAXIMA_LONGITUD_API){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F", MAXIMA_LONGITUD_API }, locale);
			return respuesta;
		  }
		  
		  if (String.valueOf(this.temperatura_muestreo).length() > MAXIMA_LONGITUD_TEMPERATURA){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura", MAXIMA_LONGITUD_TEMPERATURA }, locale);
			return respuesta;
		  }
		  if (String.valueOf(this.factor_muestreo).length() > MAXIMA_LONGITUD_FACTOR){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor", MAXIMA_LONGITUD_FACTOR }, locale);
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

	public String getDescripcionProducto() {
		return descripcionProducto;
	}

	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}
  
}