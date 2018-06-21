package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Parametro extends EntidadBase {
	private int id_parametro;
	private String valor;
	private String alias;
	
	
	//variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_VALOR=80;
	static final int MAXIMA_LONGITUD_ALIAS=20;
	
	static final String ALIAS_ULTIMAS_DESCARGAS = "ULTIMAS DESCARGAS";
	static final String ALIAS_ROL_UT_DET_PLANIF = "ROL_UT_DEST_PLANIF";
	static final String ALIAS_ROL_TR_DEST_PLANIF = "ROL_TR_DEST_PLANIF";
	public static final String ALIAS_DIRECTORIO_ARCHIVOS = "DIRECTORIO_ARCHIVOS";
	public static final String ALIAS_PERIODO_NOTIFICACION = "PERIODO_NOTIFICACION";
	
	//SEGURIDAD LOGIN
	public static final String ALIAS_MAX_CAMBIO_CLAVE = "MAX_CAMBIO_CLAVE";
	public static final String ALIAS_INTENTOS_INVALIDOS = "INTENTOS_INVALIDOS";
	public static final String ALIAS_HIST_ITERA_CLAVE = "HIST_ITERA_CLAVE";
	//

	public int getId() {
		return id_parametro;
	}

	public void setId(int Id) {
		this.id_parametro = Id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String Valor) {
		this.valor = Valor;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String Alias) {
		this.alias = Alias;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.alias.length() > MAXIMA_LONGITUD_ALIAS  || !Utilidades.esValido(this.alias)){		
			return false;
		}
		
		if (this.valor.length() > MAXIMA_LONGITUD_VALOR  || !Utilidades.esValido(this.valor)){			
			return false;
		}
		
		return resultado;
	}
	
public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.alias)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Alias" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.valor)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Valor" }, locale);
		return respuesta;
	  }
	  if (this.alias.length() > MAXIMA_LONGITUD_ALIAS){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Alias", MAXIMA_LONGITUD_ALIAS }, locale);
		return respuesta;
	  }
	  if (this.valor.length() > MAXIMA_LONGITUD_VALOR){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Valor", MAXIMA_LONGITUD_VALOR }, locale);
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
	if(!Utilidades.esValido(this.getAlias())){ this.setAlias(""); };
	if(!Utilidades.esValido(this.getValor())){ this.setValor(""); };

	cadena = this.getAlias().toString() + 
			 this.getValor().toString();
	return cadena;
}	
}