package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class AforoTanque extends EntidadBase {
  private int id_atanque;
  private float centimetros;
  private int id_tanque;
  private float volumen;
  private String nombreTanque;
  
  
  static final int MAXIMA_LONGITUD_CENTIMETROS=10;  
  static final int MAXIMA_LONGITUD_VOLUMEN=10;

  public int getId(){
    return id_atanque;
  }

  public void setId(int Id ){
    this.id_atanque=Id;
  }
  
  public float getCentimetros(){
    return centimetros;
  }

  public void setCentimetros(float Centimetros ){
    this.centimetros=Centimetros;
  }
  
  public int getIdTanque(){
    return id_tanque;
  }

  public void setIdTanque(int IdTanque ){
    this.id_tanque=IdTanque;
  }
  
  public float getVolumen(){
    return volumen;
  }

  public void setVolumen(float Volumen ){
    this.volumen=Volumen;
  }

  /**
   * @return the nombreTanque
   */
  public String getNombreTanque() {
   return nombreTanque;
  }

  /**
   * @param nombreTanque the nombreTanque to set
   */
  public void setNombreTanque(String nombreTanque) {
   this.nombreTanque = nombreTanque;
  }
  
	public boolean validar(){
		boolean resultado = true;
		if (String.valueOf(this.centimetros).length()> MAXIMA_LONGITUD_CENTIMETROS){			
			return false;
		}
		if (String.valueOf(this.volumen).length()> MAXIMA_LONGITUD_VOLUMEN){
			return false;
		}
		
		
		return resultado;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if(!Utilidades.esValido(this.volumen)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen" }, locale);
			  return respuesta;
		  }
		  if(!Utilidades.esValido(this.centimetros)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Altura" }, locale);
			  return respuesta;
		  }
		  if (String.valueOf(this.volumen).length()> MAXIMA_LONGITUD_VOLUMEN){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen", MAXIMA_LONGITUD_VOLUMEN }, locale);
			return respuesta;
		  }
		  if (String.valueOf(this.centimetros).length()> MAXIMA_LONGITUD_CENTIMETROS){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Altura", MAXIMA_LONGITUD_CENTIMETROS }, locale);
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
		if(!Utilidades.esValido(this.getCentimetros())){ this.setCentimetros(0);};
		if(!Utilidades.esValido(this.getVolumen())){ this.setVolumen(0); };

		cadena = String.valueOf(this.getCentimetros()) + 
				 String.valueOf(this.getVolumen()); 
										
		return cadena;
	}
}