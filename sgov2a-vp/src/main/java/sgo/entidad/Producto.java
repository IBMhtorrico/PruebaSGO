 	

package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Producto extends EntidadBase {
  private int id_producto;
  private String nombre;
  private String codigo_osinerg;
  private String abreviatura;
  private int estado;
  private String sincronizado_el;
  private String fecha_referencia;
  private String codigo_referencia;
  private int indicador_producto;
  
	//variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_NOMBRE=80;
	static final int MAXIMA_LONGITUD_CODIGO_OSINERG=5;
	static final int MAXIMA_LONGITUD_ABREVIATURA=20;
	
	public final static int INDICADOR_PRODUCTO_SIN_DATOS=2;
	public final static int INDICADOR_PRODUCTO_CON_DATOS=1;
  
  public int getId(){
    return id_producto;
  }

  public void setId(int Id ){
    this.id_producto=Id;
  }
  
  public String getNombre(){
    return nombre;
  }

  public void setNombre(String Nombre ){
    this.nombre=Nombre;
  }
  
  public String getCodigoOsinerg(){
    return codigo_osinerg;
  }

  public void setCodigoOsinerg(String CodigoOsinerg ){
    this.codigo_osinerg=CodigoOsinerg;
  }
  
  public String getAbreviatura(){
    return abreviatura;
  }

  public void setAbreviatura(String Abreviatura ){
    this.abreviatura=Abreviatura;
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
  
  /**
 * @return the indicador_producto
 */
public int getIndicadorProducto() {
	return indicador_producto;
}

/**
 * @param indicador_producto the indicador_producto to set
 */
public void setIndicadorProducto(int indicadorProducto) {
	this.indicador_producto = indicadorProducto;
}

public boolean validar(){
		boolean resultado = true;

		if (this.abreviatura.length() > MAXIMA_LONGITUD_ABREVIATURA  || !Utilidades.esValido(this.abreviatura)){		
			return false;
		}
		
		if (this.codigo_osinerg.length() > MAXIMA_LONGITUD_CODIGO_OSINERG  || !Utilidades.esValido(this.codigo_osinerg)){			
			return false;
		}
		
		if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE  || !Utilidades.esValido(this.nombre)){			
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
	  if (!Utilidades.esValido(this.nombre)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Nombre" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.codigo_osinerg)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Código Osinerg" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.abreviatura)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Abreviatura" }, locale);
		return respuesta;
	  }
	  if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre", MAXIMA_LONGITUD_NOMBRE }, locale);
		return respuesta;
	  }
	  if (this.codigo_osinerg.length() > MAXIMA_LONGITUD_CODIGO_OSINERG){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Código Osinerg", MAXIMA_LONGITUD_CODIGO_OSINERG }, locale);
		return respuesta;
	  }
	  if (this.abreviatura.length() > MAXIMA_LONGITUD_ABREVIATURA){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Abreviatura", MAXIMA_LONGITUD_ABREVIATURA }, locale);
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
	if(!Utilidades.esValido(this.getNombre())){ this.setNombre(""); };
	if(!Utilidades.esValido(this.getCodigoOsinerg())){ this.setCodigoOsinerg(""); };
	if(!Utilidades.esValido(this.getAbreviatura())){ this.setAbreviatura(""); };

	cadena = this.getNombre().toString() + 
			 this.getCodigoOsinerg().toString() + 
			 this.getAbreviatura().toString();
	return cadena;
}


}