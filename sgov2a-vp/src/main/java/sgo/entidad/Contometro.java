package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Contometro extends EntidadBase {
	private int id_contometro;
	private String alias;
	private int estado;
	private int tipo_contometro;
	private String sincronizado_el;
	private String fecha_referencia;
	private String codigo_referencia;
	private int id_estacion;
	private Estacion estacion;
	
	//variable para hacer las validaciones.
	static final int MAXIMA_LONGITUD_ALIAS = 20;

	public int getId() {
		return id_contometro;
	}

	public void setId(int Id) {
		this.id_contometro = Id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String Alias) {
		this.alias = Alias;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public int getTipoContometro() {
		return tipo_contometro;
	}

	public void setTipoContometro(int TipoContometro) {
		this.tipo_contometro = TipoContometro;
	}

	public String getSincronizadoEl() {
		return sincronizado_el;
	}

	public void setSincronizadoEl(String SincronizadoEl) {
		this.sincronizado_el = SincronizadoEl;
	}

	public String getFechaReferencia() {
		return fecha_referencia;
	}

	public void setFechaReferencia(String FechaReferencia) {
		this.fecha_referencia = FechaReferencia;
	}

	public String getCodigoReferencia() {
		return codigo_referencia;
	}

	public void setCodigoReferencia(String CodigoReferencia) {
		this.codigo_referencia = CodigoReferencia;
	}

	public int getIdEstacion() {
		return id_estacion;
	}

	public void setIdEstacion(int IdEstacion) {
		this.id_estacion = IdEstacion;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.alias.length() > MAXIMA_LONGITUD_ALIAS || !Utilidades.esValido(this.alias)){			
			return false;
		}
		
		if(!Utilidades.esValido(this.id_estacion)){
			return false;
		}
		
		if(!Utilidades.esValido(this.tipo_contometro)){
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
	  if (!Utilidades.esValido(this.alias)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Alias" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.tipo_contometro)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tipo" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_estacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Estación" }, locale);
		return respuesta;
	  }
	  if (this.alias.length() > MAXIMA_LONGITUD_ALIAS){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Alias", MAXIMA_LONGITUD_ALIAS }, locale);
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

	cadena = this.getAlias().toString() ;
	return cadena;
}

}