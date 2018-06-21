package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Tolerancia  {
  private String id_tolerancia;
  private int id_estacion;
  private int id_producto;
  private float porcentaje_actual;
  private int tipo_volumen;
  private Producto producto;
  
  public final static int MAXIMA_LONGITUD_PORCENTAJE_ACTUAL=6;
  
  public static final int VOLUMEN_OBSERVADO=1;
  public static final int VOLUMEN_CORREGIDO=2;

  public int getTipoVolumen() {
   return tipo_volumen;
  }

  public void setTipoVolumen(int tipo_volumen) {
   this.tipo_volumen = tipo_volumen;
  }

  public String getId(){
    return id_tolerancia;
  }

  public void setId(String Id ){
    this.id_tolerancia=Id;
  }
  
  public int getIdEstacion(){
    return id_estacion;
  }

  public void setIdEstacion(int IdEstacion ){
    this.id_estacion=IdEstacion;
  }
  
  public int getIdProducto(){
    return id_producto;
  }

  public void setIdProducto(int IdProducto ){
    this.id_producto=IdProducto;
  }
  
  public float getPorcentajeActual(){
    return porcentaje_actual;
  }

  public void setPorcentajeActual(float PorcentajeActual ){
    this.porcentaje_actual=PorcentajeActual;
  }

  /**
   * @return the producto
   */
  public Producto getProducto() {
   return producto;
  }

  /**
   * @param producto the producto to set
   */
  public void setProducto(Producto producto) {
   this.producto = producto;
  }
  
  public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.id_producto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.porcentaje_actual)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tolerancia" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_estacion)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Estación" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.tipo_volumen)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo Volúmen" }, locale);
			return respuesta;
		  }
		  if (Float.toString(this.porcentaje_actual).length() > MAXIMA_LONGITUD_PORCENTAJE_ACTUAL){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Tolerancia", MAXIMA_LONGITUD_PORCENTAJE_ACTUAL }, locale);
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
		if(!Utilidades.esValido(this.getPorcentajeActual())){ this.setPorcentajeActual(0); };

		cadena = Float.toString(this.getPorcentajeActual()) ;
		return cadena;
	}
}