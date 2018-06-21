package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class AforoCisterna extends EntidadBase {
  private int id_acisterna;
  private int id_cisterna;
  private int id_tracto;
  private int id_compartimento;
  private float milimetros;
  private float volumen;
  private float variacion_milimetros;
  private float variacion_volumen;
  private Tracto tracto;
  private Cisterna cisterna;
  private Compartimento compartimento;

  static final int MAXIMA_LONGITUD_MILIMETROS=10;  
  static final int MAXIMA_LONGITUD_VOLUMEN=10;
  static final int MAXIMA_LONGITUD_VARIACION_VOLUMEN=10;
  static final int MAXIMA_LONGITUD_VARIACION_MILIMETROS=10;
  
  
  public int getId(){
    return id_acisterna;
  }

  public void setId(int Id ){
    this.id_acisterna=Id;
  }
  
  public int getIdCisterna(){
    return id_cisterna;
  }

  public void setIdCisterna(int IdCisterna ){
    this.id_cisterna=IdCisterna;
  }
  
  public int getIdTracto(){
    return id_tracto;
  }

  public void setIdTracto(int IdTracto ){
    this.id_tracto=IdTracto;
  }
  
  public int getIdCompartimento(){
    return id_compartimento;
  }

  public void setIdCompartimento(int IdCompartimento ){
    this.id_compartimento=IdCompartimento;
  }
  
  public float getMilimetros(){
    return milimetros;
  }

  public void setMilimetros(float Milimetros ){
    this.milimetros=Milimetros;
  }
  
  public float getVolumen(){
    return volumen;
  }

  public void setVolumen(float Volumen ){
    this.volumen=Volumen;
  }
  
  public float getVariacionMilimetros(){
    return variacion_milimetros;
  }

  public void setVariacionMilimetros(float VariacionMilimetros ){
    this.variacion_milimetros=VariacionMilimetros;
  }
  
  public float getVariacionVolumen(){
    return variacion_volumen;
  }

  public void setVariacionVolumen(float VariacionVolumen ){
    this.variacion_volumen=VariacionVolumen;
  }

  /**
   * @return the tracto
   */
  public Tracto getTracto() {
   return tracto;
  }

  /**
   * @param tracto the tracto to set
   */
  public void setTracto(Tracto tracto) {
   this.tracto = tracto;
  }

  /**
   * @return the cisterna
   */
  public Cisterna getCisterna() {
   return cisterna;
  }

  /**
   * @param cisterna the cisterna to set
   */
  public void setCisterna(Cisterna cisterna) {
   this.cisterna = cisterna;
  }

  /**
   * @return the compartimento
   */
  public Compartimento getCompartimento() {
   return compartimento;
  }
 
   
  /**
   * @param compartimento the compartimento to set
   */
  public void setCompartimento(Compartimento compartimento) {
   this.compartimento = compartimento;
  }
	public boolean validar(){
		boolean resultado = true;
		if (String.valueOf(this.milimetros).length()> MAXIMA_LONGITUD_MILIMETROS){			
			return false;
		}
		if (String.valueOf(this.volumen).length()> MAXIMA_LONGITUD_VOLUMEN){
			return false;
		}
		
		if (String.valueOf(this.variacion_volumen).length() > MAXIMA_LONGITUD_VARIACION_VOLUMEN){
			return false;
		}
		if (String.valueOf(this.variacion_milimetros).length() > MAXIMA_LONGITUD_VARIACION_MILIMETROS){
			return false;
		}		
		return resultado;
	}  
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if(!Utilidades.esValido(this.variacion_milimetros)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Variación Altura" }, locale);
			  return respuesta;
		  }
		  if(!Utilidades.esValido(this.variacion_volumen)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Variación Volumen" }, locale);
			  return respuesta;
		  }
		  if(!Utilidades.esValido(this.milimetros)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Altura" }, locale);
			  return respuesta;
		  }
		  if(!Utilidades.esValido(this.volumen)){
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen" }, locale);
			  return respuesta;
		  }
		  if (String.valueOf(this.variacion_milimetros).length()> MAXIMA_LONGITUD_VARIACION_MILIMETROS){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Variación Altura", MAXIMA_LONGITUD_VARIACION_MILIMETROS }, locale);
			return respuesta;
		  }
		  if (String.valueOf(this.variacion_volumen).length()> MAXIMA_LONGITUD_VARIACION_VOLUMEN){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Variación Volumen", MAXIMA_LONGITUD_VARIACION_VOLUMEN }, locale);
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
		if(!Utilidades.esValido(this.getMilimetros())){ this.setMilimetros(0);};
		if(!Utilidades.esValido(this.getVolumen())){ this.setVolumen(0); };
		if(!Utilidades.esValido(this.getVariacionVolumen())){ this.setVariacionVolumen(0); };
		if(!Utilidades.esValido(this.getVariacionMilimetros())){ this.setVariacionMilimetros(0); };

		cadena = String.valueOf(this.getMilimetros()) + 
				 String.valueOf(this.getVolumen()) + 
				 String.valueOf(this.getVariacionVolumen()) + 
				 String.valueOf(this.getVariacionMilimetros()); 
										
		return cadena;
	}
}