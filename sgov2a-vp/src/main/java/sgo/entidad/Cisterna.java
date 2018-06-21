package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Cisterna extends EntidadBase {
	private int id_cisterna;
	private String placa;
	private int id_tracto;
	private int id_transportista;
	private int estado;
	private String sincronizado_el;
	private String fecha_referencia;
	private String codigo_referencia;
	private String placa_tracto;
	private String tarjeta_cubicacion;
	private Date fecha_vigencia_tarjeta_cubicacion;
	private int cantidad_compartimentos;
	private Tracto tracto;
	private Transportista transportista;
	private String placaCisternaTracto;
	private ArrayList<Compartimento> compartimentos;
	
	public final static int ESTADO_ACTIVO=1;
	public final static int ESTADO_INACTIVO=2;
	
	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_PLACA=15;
	static final int MAXIMA_LONGITUD_TARJETA_CUBICACION=20;
	
	public void agregarCompartimento(Compartimento compartimento){
	 if (this.compartimentos== null){
	  this.compartimentos = new ArrayList<Compartimento>();
	 }
	 this.compartimentos.add(compartimento);
	}
	
	/**
	 * @return the fecha_vigencia_tarjeta_cubicacion
	 */
	public Date getFechaVigenciaTarjetaCubicacion() {
		return fecha_vigencia_tarjeta_cubicacion;
	}

	/**
	 * @param fecha_vigencia_tarjeta_cubicacion the fecha_vigencia_tarjeta_cubicacion to set
	 */
	public void setFechaVigenciaTarjetaCubicacion(Date fechaVigenciaTarjetaCubicacion) {
		this.fecha_vigencia_tarjeta_cubicacion = fechaVigenciaTarjetaCubicacion;
	}

	/**
	 * @return the tarjeta_cubicacion
	 */
	public String getTarjetaCubicacion() {
		return tarjeta_cubicacion;
	}

	/**
	 * @param tarjeta_cubicacion the tarjeta_cubicacion to set
	 */
	public void setTarjetaCubicacion(String tarjetaCubicacion) {
		this.tarjeta_cubicacion = tarjetaCubicacion;
	}



	public int getCantidadCompartimentos() {
		return cantidad_compartimentos;
	}

	public void setCantidadCompartimentos(int cantidadCompartimentos) {
		this.cantidad_compartimentos = cantidadCompartimentos;
	}
	
	public int getId() {
		return id_cisterna;
	}

	public void setId(int Id) {
		this.id_cisterna = Id;
	}

	public String getPlaca() {
		return placa;
	}
	

	public void setPlaca(String Placa) {
		this.placa = Placa;
	}

	public int getIdTracto() {
		return id_tracto;
	}

	public void setIdTracto(int idTracto) {
		this.id_tracto = idTracto;
	}

	/**
	 * @return the placa_tracto
	 */
	public String getPlacaTracto() {
		return placa_tracto;
	}

	/**
	 * @param placa_tracto the placa_tracto to set
	 */
	public void setPlacaTracto(String placaTracto) {
		this.placa_tracto = placaTracto;
	}

	public int getIdTransportista() {
		return id_transportista;
	}

	public void setIdTransportista(int IdTransportista) {
		this.id_transportista = IdTransportista;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
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

	public Tracto getTracto() {
		return tracto;
	}

	public void setTracto(Tracto tracto) {
		this.tracto = tracto;
	}

	public Transportista getTransportista() {
		return transportista;
	}

	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}
	
	public String getPlacaCisternaTracto() {
		return this.placaCisternaTracto;
	}

	/**
	 * @param placaCisternaTracto the placaCisternaTracto to set
	 */
	public void setPlacaCisternaTracto(String placaCisternaTracto) {
		this.placaCisternaTracto = placaCisternaTracto;
	}

	 /**
	  * @return the compartimentos
	  */
	 public ArrayList<Compartimento> getCompartimentos() {
	  return compartimentos;
	 }

	 /**
	  * @param compartimentos the compartimentos to set
	  */
	 public void setCompartimentos(ArrayList<Compartimento> compartimentos) {
	  this.compartimentos = compartimentos;
	 }
	 
	public boolean validar(){
		boolean resultado = true;

		if (this.placa.length() > MAXIMA_LONGITUD_PLACA || !Utilidades.esValido(this.placa)){			
			return false;
		}
		
		if(!Utilidades.esValido(this.id_tracto)){
			return false;
		}
		
		if(!Utilidades.esValido(this.id_transportista)){
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
	  if (!Utilidades.esValido(this.placa)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Placa" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.tarjeta_cubicacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tarjeta de Cubicación" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.fecha_vigencia_tarjeta_cubicacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Fecha vigencia de la tarjeta de cubicación" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_tracto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tracto" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_transportista)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Transportista" }, locale);
		return respuesta;
	  }
	  
	  if (this.placa.length() > MAXIMA_LONGITUD_PLACA){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Placa", MAXIMA_LONGITUD_PLACA }, locale);
		return respuesta;
	  }
	  if (this.tarjeta_cubicacion.length() > MAXIMA_LONGITUD_TARJETA_CUBICACION){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Tarjeta de Cubicación", MAXIMA_LONGITUD_TARJETA_CUBICACION }, locale);
		return respuesta;
	  }
	  if (!Utilidades.validarFecha(this.fecha_vigencia_tarjeta_cubicacion.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
	    respuesta.estado = false;
	    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Fecha vigencia de la tarjeta de cubicación" }, locale);
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
	if(!Utilidades.esValido(this.getPlaca())){ this.setPlaca(""); };
	if(!Utilidades.esValido(this.getTarjetaCubicacion())){ this.setTarjetaCubicacion(""); };
	if(!Utilidades.esValido(this.getFechaVigenciaTarjetaCubicacion())){ this.setFechaVigenciaTarjetaCubicacion(new Date(0)); };

	cadena = this.getPlaca().toString() + 
			 this.getTarjetaCubicacion().toString() + 
			 this.getFechaVigenciaTarjetaCubicacion().toString();
	return cadena;
}
	
}