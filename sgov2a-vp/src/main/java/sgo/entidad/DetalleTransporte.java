package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class DetalleTransporte {
	private String id_dtransporte;
	private int id_transporte;
	private int id_producto;
	private float capacidad_volumetrica_compartimento;
	private String unidad_medida_volumen;
	private int numero_compartimento;
	private float volumen_temperatura_observada;
	private float temperatura_observada;
	private float api_temperatura_base;
	private float factor_correcion;
	private float volumen_temperatura_base;
	private int altura_compartimento;
	private String descripcionProducto;

	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_TEMPERATURA_VOLUMEN_OBSERVADO=10;
	static final int MAXIMA_LONGITUD_TEMPERATURA_OBSERVADA=4;
	static final int MAXIMA_LONGITUD_API_TEMPERATURA_BASE=4;
	static final int MAXIMA_LONGITUD_FACTOR_CORRECCION=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_TEMPERATURA_BASE=10;

	/**
	 * @return the descripcionProducto
	 */
	public String getDescripcionProducto() {
		return descripcionProducto;
	}

	/**
	 * @param descripcionProducto the descripcionProducto to set
	 */
	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}

	/**
	 * @return the altura_compartimento
	 */
	public int getAlturaCompartimento() {
		return altura_compartimento;
	}

	/**
	 * @param altura_compartimento the altura_compartimento to set
	 */
	public void setAlturaCompartimento(int alturaCompartimento) {
		this.altura_compartimento = alturaCompartimento;
	}

	public String getId() {
		return id_dtransporte;
	}

	public void setId(String Id) {
		this.id_dtransporte = Id;
	}

	public int getIdTransporte() {
		return id_transporte;
	}

	public void setIdTransporte(int IdTransporte) {
		this.id_transporte = IdTransporte;
	}

	public int getIdProducto() {
		return id_producto;
	}

	public void setIdProducto(int IdProducto) {
		this.id_producto = IdProducto;
	}

	public float getCapacidadVolumetricaCompartimento() {
		return capacidad_volumetrica_compartimento;
	}

	public void setCapacidadVolumetricaCompartimento(
			float CapacidadVolumetricaCompartimento) {
		this.capacidad_volumetrica_compartimento = CapacidadVolumetricaCompartimento;
	}

	public String getUnidadMedida() {
		return unidad_medida_volumen;
	}

	public void setUnidadMedida(String UnidadMedida) {
		this.unidad_medida_volumen = UnidadMedida;
	}

	public int getNumeroCompartimento() {
		return numero_compartimento;
	}

	public void setNumeroCompartimento(int NumeroCompartimento) {
		this.numero_compartimento = NumeroCompartimento;
	}

	public float getVolumenTemperaturaObservada() {
		return volumen_temperatura_observada;
	}

	public void setVolumenTemperaturaObservada(float VolumenTemperaturaObservada) {
		this.volumen_temperatura_observada = VolumenTemperaturaObservada;
	}

	public float getTemperaturaObservada() {
		return temperatura_observada;
	}

	public void setTemperaturaObservada(float TemperaturaObservada) {
		this.temperatura_observada = TemperaturaObservada;
	}

	public float getApiTemperaturaBase() {
		return api_temperatura_base;
	}

	public void setApiTemperaturaBase(float ApiTemperaturaBase) {
		this.api_temperatura_base = ApiTemperaturaBase;
	}

	public float getFactorCorrecion() {
		return factor_correcion;
	}

	public void setFactorCorrecion(float FactorCorrecion) {
		this.factor_correcion = FactorCorrecion;
	}

	public float getVolumenTemperaturaBase() {
		return volumen_temperatura_base;
	}

	public void setVolumenTemperaturaBase(float VolumenTemperaturaBase) {
		this.volumen_temperatura_base = VolumenTemperaturaBase;
	}

	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.id_producto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.volumen_temperatura_observada)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. T. Obs. (gal)" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.temperatura_observada)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura (F)" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.api_temperatura_base)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60 F" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.factor_correcion)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.volumen_temperatura_base)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. 60 F (gal)" }, locale);
			return respuesta;
		  }

		  if (Float.toString(this.volumen_temperatura_observada).length() > MAXIMA_LONGITUD_TEMPERATURA_VOLUMEN_OBSERVADO){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. T. Obs. (gal)", MAXIMA_LONGITUD_TEMPERATURA_VOLUMEN_OBSERVADO }, locale);
			return respuesta;
		  }
		  if (Float.toString(this.temperatura_observada).length() > MAXIMA_LONGITUD_TEMPERATURA_OBSERVADA){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura (F)", MAXIMA_LONGITUD_TEMPERATURA_OBSERVADA }, locale);
			return respuesta;
		  }
		  if (Float.toString(this.api_temperatura_base).length() > MAXIMA_LONGITUD_API_TEMPERATURA_BASE){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60 F", MAXIMA_LONGITUD_API_TEMPERATURA_BASE }, locale);
			return respuesta;
		  }
		  if (Float.toString(this.factor_correcion).length() > MAXIMA_LONGITUD_FACTOR_CORRECCION){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor", MAXIMA_LONGITUD_FACTOR_CORRECCION }, locale);
			return respuesta;
		  }
		  if (Float.toString(this.volumen_temperatura_base).length() > MAXIMA_LONGITUD_VOLUMEN_TEMPERATURA_BASE){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. 60 F (gal)", MAXIMA_LONGITUD_VOLUMEN_TEMPERATURA_BASE }, locale);
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
		if(!Utilidades.esValido(this.getVolumenTemperaturaObservada())){ this.setVolumenTemperaturaObservada(0); };
		if(!Utilidades.esValido(this.getTemperaturaObservada())){ this.setTemperaturaObservada(0); };
		if(!Utilidades.esValido(this.getApiTemperaturaBase())){ this.setApiTemperaturaBase(0); };
		if(!Utilidades.esValido(this.getFactorCorrecion())){ this.setFactorCorrecion(0); };
		if(!Utilidades.esValido(this.getVolumenTemperaturaBase())){ this.setVolumenTemperaturaBase(0); };

		cadena = Float.toString(this.getVolumenTemperaturaObservada()) + 
				 Float.toString(this.getTemperaturaObservada()) +
				 Float.toString(this.getApiTemperaturaBase()) + 
				 Float.toString(this.getFactorCorrecion())+ 
				 Float.toString(this.getVolumenTemperaturaBase());
		return cadena;
	}
	
}