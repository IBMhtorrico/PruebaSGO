package sgo.entidad;

import java.sql.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Tanque extends EntidadBase {
  private int id_tanque;
  private float volumen_total;
  private String descripcion;
  private float volumen_trabajo;
  private int id_estacion;
  private int estado;
  private String sincronizado_el;
  private String fecha_referencia;
  private String codigo_referencia;
  private Estacion estacion;
  private int tipo;
  private int certificado_calibracion;
  private Date fecha_emision_calibracion;
  private int id_producto;
  public final static int TIPO_VIRTUAL=1;
  public final static int TIPO_ADMINISTRADO=2;  
  public Producto producto;

  //variables para hacer las validaciones.
  static final int MAXIMA_LONGITUD_DESCRIPCION=20;
	
  public int getId(){
    return id_tanque;
  }

  public void setId(int Id ){
    this.id_tanque=Id;
  }
  
  public float getVolumenTotal(){
    return volumen_total;
  }

  public void setVolumenTotal(float VolumenTotal ){
    this.volumen_total=VolumenTotal;
  }
  
  public String getDescripcion(){
    return descripcion;
  }

  public void setDescripcion(String Descripcion ){
    this.descripcion=Descripcion;
  }
  
  public float getVolumenTrabajo(){
    return volumen_trabajo;
  }

  public void setVolumenTrabajo(float VolumenTrabajo ){
    this.volumen_trabajo=VolumenTrabajo;
  }
  
  public int getIdEstacion(){
    return id_estacion;
  }

  public void setIdEstacion(int IdEstacion ){
    this.id_estacion=IdEstacion;
  }
  
  public int getEstado(){
    return estado;
  }

  public void setEstado(int Estado ){
    this.estado=Estado;
  }
  
  public String getSincronizadoEl(){
    return sincronizado_el;
  }

  public void setSincronizadoEl(String SincronizadoEl ){
    this.sincronizado_el=SincronizadoEl;
  }
  
  public String getFechaReferencia(){
    return fecha_referencia;
  }

  public void setFechaReferencia(String FechaReferencia ){
    this.fecha_referencia=FechaReferencia;
  }
  
  public String getCodigoReferencia(){
    return codigo_referencia;
  }

  public void setCodigoReferencia(String CodigoReferencia ){
    this.codigo_referencia=CodigoReferencia;
  }

  public Estacion getEstacion() {
	return estacion;
  }

  public void setEstacion(Estacion estacion) {
	this.estacion = estacion;
  }
  
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getCertificadoCalibracion() {
		return certificado_calibracion;
	}
	
	public void setCertificadoCalibracion(int certificadoCalibracion) {
		this.certificado_calibracion = certificadoCalibracion;
	}
	
	public Date getFechaEmisionCalibracion() {
		return fecha_emision_calibracion;
	}
	
	public void setFecha_emision_calibracion(Date fechaEmisionCalibracion) {
		this.fecha_emision_calibracion = fechaEmisionCalibracion;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION  || !Utilidades.esValido(this.descripcion)){		
			return false;
		}
		
		if(!Utilidades.esValido(this.volumen_trabajo)){
			return false;
		}
		
		if(!Utilidades.esValido(this.volumen_total)){
			return false;
		}

		if(!Utilidades.esValido(this.estacion)){
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
		  if (this.descripcion.length() > MAXIMA_LONGITUD_DESCRIPCION){	
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Descripción", MAXIMA_LONGITUD_DESCRIPCION }, locale);
		  return respuesta;
		}

		if(!Utilidades.esValido(this.volumen_total)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen Total" }, locale);
		  return respuesta;
		}
		
		if(!Utilidades.esValido(this.volumen_trabajo)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen Trabajo" }, locale);
		  return respuesta;
		}
		
		if(!Utilidades.esValidoForingKey(this.id_estacion)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Estacion" }, locale);
		  return respuesta;
		}
		
		if(!Utilidades.esValidoForingKey(this.tipo)){
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo" }, locale);
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
		if(!Utilidades.esValido(this.getVolumenTotal())){ this.setVolumenTotal(0); };
		if(!Utilidades.esValido(this.getVolumenTrabajo())){ this.setVolumenTrabajo(0); };
		
		cadena = this.getDescripcion().toString() +
				 String.valueOf(this.getVolumenTotal()) +
				 String.valueOf(this.getVolumenTrabajo()) ;
		return cadena;
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

}