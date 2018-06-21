package sgo.entidad;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Enlace extends EntidadBase {
	private int id_enlace;
	private String url_completa;
	private String url_relativa;
	private int orden;
	private int padre;
	private String titulo;
	private int tipo;
	private int id_permiso;
	private String claseCss = "";
	private Boolean enlaceActual = false;
	private List<Enlace> enlaces = null;
	private Permiso entidadPermiso;
	
	//variable para hacer las validaciones.
	static final int MAXIMA_LONGITUD_TITULO=80;
	static final int MAXIMA_LONGITUD_URL_COMPLETA=100;
	static final int MAXIMA_LONGITUD_URL_RELATIVA=100;
	static final int MAXIMA_LONGITUD_ORDEN=4;
	static final int MAXIMA_LONGITUD_PADRE=4;
	static final int MAXIMA_LONGITUD_TIPO=1;

	public Enlace() {
		this.enlaces = new ArrayList<Enlace>();
	}

	public void setEnlaces(List<Enlace> enlaces) {
		this.enlaces = enlaces;
	}

	public List<Enlace> getEnlaces() {
		return this.enlaces;
	}

	public void agregarEnlace(Enlace enlace) {
		this.enlaces.add(enlace);
	}

	public int getId() {
		return id_enlace;
	}

	public void setId(int Id) {
		this.id_enlace = Id;
	}

	public String getUrlCompleta() {
		return url_completa;
	}

	public void setUrlCompleta(String UrlCompleta) {
		this.url_completa = UrlCompleta;
	}

	public String getUrlRelativa() {
		return url_relativa;
	}

	public void setUrlRelativa(String UrlRelativa) {
		this.url_relativa = UrlRelativa;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int Orden) {
		this.orden = Orden;
	}

	public int getPadre() {
		return padre;
	}

	public void setPadre(int Padre) {
		this.padre = Padre;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int Tipo) {
		this.tipo = Tipo;
	}

	public int getPermiso() {
		return id_permiso;
	}

	public void setPermiso(int Permiso) {
		this.id_permiso = Permiso;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo
	 *            the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the claseCss
	 */
	public String getClaseCss() {
		return claseCss;
	}

	/**
	 * @param claseCss
	 *            the claseCss to set
	 */
	public void setClaseCss(String claseCss) {
		this.claseCss = claseCss;
	}

	/**
	 * @return the enlaceActual
	 */
	public Boolean getEnlaceActual() {
		return enlaceActual;
	}

	/**
	 * @param enlaceActual
	 *            the enlaceActual to set
	 */
	public void setEnlaceActual(Boolean enlaceActual) {
		this.enlaceActual = enlaceActual;
	}

	public Permiso getEntidadPermiso() {
		return entidadPermiso;
	}

	public void setEntidadPermiso(Permiso entidadPermiso) {
		this.entidadPermiso = entidadPermiso;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.titulo.length() > MAXIMA_LONGITUD_TITULO || !Utilidades.esValido(this.titulo)){			
			return false;
		}
		
		if (this.url_completa.length() > MAXIMA_LONGITUD_URL_COMPLETA || !Utilidades.esValido(this.url_completa)){
			return false;
		}
		
		if (this.url_relativa.length() > MAXIMA_LONGITUD_URL_RELATIVA || !Utilidades.esValido(this.url_relativa)){
			return false;
		}
		
		if(!Utilidades.esValido(this.orden)){
			return false;
		}
		
		if(!Utilidades.esValido(this.padre)){
			return false;
		}
		
		if(!Utilidades.esValido(this.tipo)){
			return false;
		}
		
		if(!Utilidades.esValido(this.id_permiso)){
			return false;
		}

		return resultado;
	}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.titulo)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Titulo" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.url_completa)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Url Completa" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.url_relativa)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Url Relativa" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.orden)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Orden" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.padre)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Padre" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.tipo)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_permiso)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Permiso" }, locale);
		return respuesta;
	  }

	  if (this.titulo.length() > MAXIMA_LONGITUD_TITULO){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Título", MAXIMA_LONGITUD_TITULO }, locale);
		return respuesta;
	  }
	  if (this.url_completa.length() > MAXIMA_LONGITUD_URL_COMPLETA){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Url Completa", MAXIMA_LONGITUD_URL_COMPLETA }, locale);
		return respuesta;
	  }
	  if (this.url_relativa.length() > MAXIMA_LONGITUD_URL_RELATIVA){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Url Relativa", MAXIMA_LONGITUD_URL_RELATIVA }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.orden).length() > MAXIMA_LONGITUD_ORDEN){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Orden", MAXIMA_LONGITUD_ORDEN }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.padre).length() > MAXIMA_LONGITUD_PADRE){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Padre", MAXIMA_LONGITUD_PADRE }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.tipo).length() > MAXIMA_LONGITUD_TIPO){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Tipo", MAXIMA_LONGITUD_TIPO }, locale);
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
	if(!Utilidades.esValido(this.getTitulo())){ this.setTitulo(""); };
	if(!Utilidades.esValido(this.getUrlCompleta())){ this.setUrlCompleta(""); };
	if(!Utilidades.esValido(this.getUrlRelativa())){ this.setUrlRelativa(""); };
	if(!Utilidades.esValido(this.getOrden())){ this.setOrden(0); };
	if(!Utilidades.esValido(this.getPadre())){ this.setPadre(0); };
	if(!Utilidades.esValido(this.getTipo())){ this.setTipo(0); };

	cadena = this.getTitulo().toString() + 
			 this.getUrlCompleta().toString() +
			 this.getUrlRelativa().toString() + 
			 String.valueOf(this.getOrden()) +
			 String.valueOf(this.getPadre()) +
			 String.valueOf(this.getTipo()) ;
	return cadena;
}
	

}