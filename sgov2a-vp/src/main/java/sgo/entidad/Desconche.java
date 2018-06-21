package sgo.entidad;

import java.sql.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Desconche extends EntidadBase {
  private int id_desconche;
  private int id_compartimento;
  private String tracto_cisterna;
  private int id_tanque;
  private int numeroCompartimento;
  private int numero_desconche;
  private float volumen;
  private int idDescargaCisterna;
  
//variables para hacer las validaciones
static final int MAXIMA_LONGITUD_VOLUMEN=12;

  public int getId(){
    return id_desconche;
  }

  public void setId(int Id ){
    this.id_desconche=Id;
  }
  
  public int getIdCompartimento(){
    return id_compartimento;
  }

  public void setIdCompartimento(int IdCompartimento ){
    this.id_compartimento=IdCompartimento;
  }
  
  public int getIdTanque(){
    return id_tanque;
  }

  public void setIdTanque(int IdTanque ){
    this.id_tanque=IdTanque;
  }
  
  public int getNumeroDesconche(){
    return numero_desconche;
  }

  public void setNumeroDesconche(int NumeroDesconche ){
    this.numero_desconche=NumeroDesconche;
  }
  
  public float getVolumen(){
    return volumen;
  }

  public void setVolumen(float Volumen ){
    this.volumen=Volumen;
  }

  /**
   * @return the tracto_cisterna
   */
  public String getTractoCisterna() {
   return tracto_cisterna;
  }

  /**
   * @param tracto_cisterna the tracto_cisterna to set
   */
  public void setTractoCisterna(String tracto_cisterna) {
   this.tracto_cisterna = tracto_cisterna;
  }

  /**
   * @return the idDescargaCisterna
   */
  public int getIdDescargaCisterna() {
   return idDescargaCisterna;
  }

  /**
   * @param idDescargaCisterna the idDescargaCisterna to set
   */
  public void setIdDescargaCisterna(int idDescargaCisterna) {
   this.idDescargaCisterna = idDescargaCisterna;
  }

  /**
   * @return the numeroCompartimento
   */
  public int getNumeroCompartimento() {
   return numeroCompartimento;
  }

  /**
   * @param numeroCompartimento the numeroCompartimento to set
   */
  public void setNumeroCompartimento(int numeroCompartimento) {
   this.numeroCompartimento = numeroCompartimento;
  }  
  
  public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.volumen)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.numeroCompartimento)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "N# Compartimento" }, locale);
			return respuesta;
		  }

		  if (Float.toString(this.volumen).length() > MAXIMA_LONGITUD_VOLUMEN){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen", MAXIMA_LONGITUD_VOLUMEN }, locale);
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
		if(!Utilidades.esValido(this.getVolumen())){ this.setVolumen(0); };

		cadena = Float.toString(this.getVolumen()) ;
		return cadena;
	}
}