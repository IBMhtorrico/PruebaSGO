package sgo.entidad;

import java.sql.Timestamp;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class TanqueJornada extends EntidadBase {
	
	private int id_tjornada;
	private int  id_tanque;
	private int  id_producto;
	private int  medida_inicial;
	private int  medida_final;
	private float volumen_observado_inicial;
	private float volumen_observado_final;
	private float api_corregido_inicial;
	private float api_corregido_final;
	private float temperatura_inicial;
	private float temperatura_final;
	private float factor_correccion_inicial;
	private float factor_correccion_final;
	private float volumen_corregido_inicial;
	private float volumen_corregido_final;
	private int  estado_servicio;
	private int  en_linea;
	private float volumen_agua_final;
	private int id_jornada;
	private Producto producto;
	private Tanque tanque;
	private Jornada jornada;
	private float ocupado;
	private float libre;
	private String descripcionTanque;
	private Timestamp hora_inicial;
	private Timestamp hora_final;
	private int apertura;
	private int cierre;
	
	public final static int ESTADO_SERVICIO_ACTIVO = 0;
	public final static int ESTADO_SERVICICO_INACTIVO = 1;
	
	public final static int TANQUE_APERTURA = 1;
	public final static int TANQUE_NO_APERTURA = 2;
	
	public final static int TANQUE_CIERRE = 1;
	public final static int TANQUE_NO_CIERRE = 2;
	
	public final static int ESTADO_NO_DESPACHANDO = 0;
	public final static int ESTADO_DESPACHANDO = 1;
	
	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_MEDIDA_INICIAL=10;
	static final int MAXIMA_LONGITUD_MEDIDA_FINAL=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_FINAL=10;
	static final int MAXIMA_LONGITUD_API_INICIAL=4;
	static final int MAXIMA_LONGITUD_API_FINAL=4;
	static final int MAXIMA_LONGITUD_TEMPERATURA_INICIAL=4;
	static final int MAXIMA_LONGITUD_TEMPERATURA_FINAL=4;
	static final int MAXIMA_LONGITUD_FACTOR_INICIAL=10;
	static final int MAXIMA_LONGITUD_FACTOR_FINAL=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_FINAL=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_AGUA_FINAL=10;

	public void setOcupado(float ocupado) {
		this.ocupado = ocupado;
	}

	public void setLibre(float libre) {
		this.libre = libre;
	}
	
	public float getOcupado(){
		if(Utilidades.esValido(this.getTanque())){
			return (this.getTanque().getVolumenTrabajo() - this.getVolumenObservadoFinal());
		} else {
			return 0;
		}
	}
	
	public float getLibre(){
		if(Utilidades.esValido(this.getTanque())){
			float ocupado = this.getTanque().getVolumenTrabajo() - this.getVolumenObservadoFinal();
			return (this.getTanque().getVolumenTrabajo() - ocupado);
		} else {
			return 0;
		}
	}
	
	public int getId() {
		return id_tjornada;
	}
	
	public int getIdTjornada() {
		return id_tjornada;
	}
	
	public void setIdTjornada(int idTjornada) {
		this.id_tjornada = idTjornada;
	}
	
	public int getIdTanque() {
		return id_tanque;
	}
	
	public void setIdTanque(int idTanque) {
		this.id_tanque = idTanque;
	}
	
	public int getIdProducto() {
		return id_producto;
	}
	
	public void setIdProducto(int idProducto) {
		this.id_producto = idProducto;
	}
	
	public int getMedidaInicial() {
		return medida_inicial;
	}
	
	public void setMedidaInicial(int medidaInicial) {
		this.medida_inicial = medidaInicial;
	}
	
	public int getMedidaFinal() {
		return medida_final;
	}
	
	public void setMedidaFinal(int medidaFinal) {
		this.medida_final = medidaFinal;
	}
	
	public float getVolumenObservadoInicial() {
		return volumen_observado_inicial;
	}
	
	public void setVolumenObservadoInicial(float volumenObservadoInicial) {
		this.volumen_observado_inicial = volumenObservadoInicial;
	}
	
	public float getVolumenObservadoFinal() {
		return volumen_observado_final;
	}
	
	public void setVolumenObservadoFinal(float volumenObservadoFinal) {
		this.volumen_observado_final = volumenObservadoFinal;
	}
	
	public float getApiCorregidoInicial() {
		return api_corregido_inicial;
	}
	
	public void setApiCorregidoInicial(float apiCorregidoInicial) {
		this.api_corregido_inicial = apiCorregidoInicial;
	}
	
	public float getApiCorregidoFinal() {
		return api_corregido_final;
	}
	
	public void setApiCorregidoFinal(float apiCorregidoFinal) {
		this.api_corregido_final = apiCorregidoFinal;
	}
	
	public float getTemperaturaInicial() {
		return temperatura_inicial;
	}
	
	public void setTemperaturaInicial(float temperaturaInicial) {
		this.temperatura_inicial = temperaturaInicial;
	}
	
	public float getTemperaturaFinal() {
		return temperatura_final;
	}
	
	public void setTemperaturaFinal(float temperaturaFinal) {
		this.temperatura_final = temperaturaFinal;
	}
	
	public float getFactorCorreccionInicial() {
		return factor_correccion_inicial;
	}
	
	public void setFactorCorreccionInicial(float factorCorreccionInicial) {
		this.factor_correccion_inicial = factorCorreccionInicial;
	}
	
	public float getFactorCorreccionFinal() {
		return factor_correccion_final;
	}
	
	public void setFactorCorreccionFinal(float factorCorreccionFinal) {
		this.factor_correccion_final = factorCorreccionFinal;
	}
	
	public float getVolumenCorregidoInicial() {
		return volumen_corregido_inicial;
	}
	
	public void setVolumenCorregidoInicial(float volumenCorregidoInicial) {
		this.volumen_corregido_inicial = volumenCorregidoInicial;
	}
	
	public float getVolumenCorregidoFinal() {
		return volumen_corregido_final;
	}
	
	public void setVolumenCorregidoFinal(float volumenCorregidoFinal) {
		this.volumen_corregido_final = volumenCorregidoFinal;
	}
	
	public int getEstadoServicio() {
		return estado_servicio;
	}
	
	public void setEstadoServicio(int estadoServicio) {
		this.estado_servicio = estadoServicio;
	}
	
	public int getEnLinea() {
		return en_linea;
	}
	
	public void setEnLinea(int enLinea) {
		this.en_linea = enLinea;
	}
	
	public float getVolumenAguaFinal() {
		return volumen_agua_final;
	}
	
	public void setVolumenAguaFinal(float volumenAguaFinal) {
		this.volumen_agua_final = volumenAguaFinal;
	}
	
	public int getIdJornada() {
		return id_jornada;
	}
	
	public void setIdJornada(int idJornada) {
		this.id_jornada = idJornada;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public Tanque getTanque() {
		return tanque;
	}
	
	public void setTanque(Tanque tanque) {
		this.tanque = tanque;
	}
	
	public Jornada getJornada() {
		return jornada;
	}
	
	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}
	
	
	//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
public String getCadena() {
	String cadena="";
	if(!Utilidades.esValido(this.getMedidaInicial())){ this.setMedidaInicial(0); };
	if(!Utilidades.esValido(this.getMedidaFinal())){ this.setMedidaFinal(0); };
	if(!Utilidades.esValido(this.getVolumenObservadoInicial())){ this.setVolumenObservadoInicial(0); };
	if(!Utilidades.esValido(this.getVolumenObservadoFinal())){ this.setVolumenObservadoFinal(0); };
	if(!Utilidades.esValido(this.getApiCorregidoInicial())){ this.setApiCorregidoInicial(0); };
	if(!Utilidades.esValido(this.getApiCorregidoFinal())){ this.setApiCorregidoFinal(0); };
	if(!Utilidades.esValido(this.getTemperaturaInicial())){ this.setTemperaturaInicial(0); };
	if(!Utilidades.esValido(this.getTemperaturaFinal())){ this.setTemperaturaFinal(0); };
	if(!Utilidades.esValido(this.getFactorCorreccionInicial())){ this.setFactorCorreccionInicial(0); };
	if(!Utilidades.esValido(this.getFactorCorreccionFinal())){ this.setFactorCorreccionFinal(0); };
	if(!Utilidades.esValido(this.getVolumenCorregidoInicial())){ this.setVolumenCorregidoInicial(0); };
	if(!Utilidades.esValido(this.getVolumenCorregidoFinal())){ this.setVolumenCorregidoFinal(0); };
	if(!Utilidades.esValido(this.getVolumenAguaFinal())){ this.setVolumenAguaFinal(0); };

	cadena = String.valueOf(this.getMedidaInicial()) + 
			 String.valueOf(this.getMedidaFinal())   +
			 String.valueOf(this.getVolumenObservadoInicial())   + 
			 String.valueOf(this.getVolumenObservadoFinal())   + 
			 String.valueOf(this.getApiCorregidoInicial())   + 
			 String.valueOf(this.getApiCorregidoFinal())   + 
			 String.valueOf(this.getTemperaturaInicial())   + 
			 String.valueOf(this.getTemperaturaFinal())   + 
			 String.valueOf(this.getFactorCorreccionInicial())   + 
			 String.valueOf(this.getFactorCorreccionFinal())   + 
			 String.valueOf(this.getVolumenCorregidoInicial())   + 
			 String.valueOf(this.getVolumenCorregidoFinal())   + 
			 String.valueOf(this.getVolumenAguaFinal()); 
	return cadena;
}	

public Respuesta validarApertura(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
		
	  if (!Utilidades.esValidoForingKey(this.id_producto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_tanque)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tanque" }, locale);
		return respuesta;
	  }
	  
	  if (!Utilidades.esValido(this.hora_inicial)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Hora Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.medida_inicial) || this.medida_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Medida Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_observado_inicial) || this.volumen_observado_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. Obs. Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.api_corregido_inicial) || this.api_corregido_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F Inicial " }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.temperatura_inicial) || this.temperatura_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.factor_correccion_inicial) || this.factor_correccion_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_corregido_inicial) || this.volumen_corregido_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen 60F Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.estado_servicio)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.en_linea)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Desp" }, locale);
		return respuesta;
	  }
		
	  if (String.valueOf(this.medida_inicial).length() > MAXIMA_LONGITUD_MEDIDA_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Medida Inicial", MAXIMA_LONGITUD_MEDIDA_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_observado_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. Obs. Inicial", MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.api_corregido_inicial).length() > MAXIMA_LONGITUD_API_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F Inicial", MAXIMA_LONGITUD_API_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.temperatura_inicial).length() > MAXIMA_LONGITUD_TEMPERATURA_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura Inicial", MAXIMA_LONGITUD_TEMPERATURA_INICIAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.factor_correccion_inicial).length() > MAXIMA_LONGITUD_FACTOR_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor Inicial", MAXIMA_LONGITUD_FACTOR_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_corregido_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen 60F Inicial", MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL }, locale);
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

public Respuesta validarCierre(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
		
  if (!Utilidades.esValidoForingKey(this.id_producto)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValidoForingKey(this.id_tanque)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tanque" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.hora_inicial)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Hora Inicial" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.medida_inicial) || this.medida_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Medida Inicial" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.volumen_observado_inicial) || this.volumen_observado_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. Obs. Inicial" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.api_corregido_inicial) || this.api_corregido_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F Inicial " }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.temperatura_inicial) || this.temperatura_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura Inicial" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.factor_correccion_inicial) || this.factor_correccion_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor Inicial" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.volumen_corregido_inicial) || this.volumen_corregido_inicial == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen 60F Inicial" }, locale);
	return respuesta;
  }
  
  String texto = " de " + this.getDescripcionTanque();
  if (!Utilidades.esValido(this.hora_final)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Hora Final" + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.medida_final) || this.medida_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Medida Final" + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.volumen_observado_final) || this.volumen_observado_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. Obs. Final" + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.api_corregido_final) || this.api_corregido_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F Final " + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.temperatura_final) || this.temperatura_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura Final" + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.factor_correccion_final) || this.factor_correccion_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor Final" + texto }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.volumen_corregido_final) || this.volumen_corregido_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen 60F Final" + texto }, locale);
	return respuesta;
  }
  //se quita la validacion de registro de volumen de agua final
/*  if (!Utilidades.esValido(this.volumen_agua_final) || this.volumen_agua_final == 0){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen de Agua" + texto }, locale);
	return respuesta;
  }*/
  if (!Utilidades.esValido(this.estado_servicio)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.en_linea)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Desp" }, locale);
	return respuesta;
  }
	
  if (String.valueOf(this.medida_inicial).length() > MAXIMA_LONGITUD_MEDIDA_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Medida Inicial", MAXIMA_LONGITUD_MEDIDA_INICIAL }, locale);
	return respuesta;
  }
  
  if (String.valueOf(this.volumen_observado_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. Obs. Inicial", MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL }, locale);
	return respuesta;
  }
  
  if (String.valueOf(this.api_corregido_inicial).length() > MAXIMA_LONGITUD_API_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F Inicial", MAXIMA_LONGITUD_API_INICIAL }, locale);
	return respuesta;
  }
  
  if (String.valueOf(this.temperatura_inicial).length() > MAXIMA_LONGITUD_TEMPERATURA_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura Inicial", MAXIMA_LONGITUD_TEMPERATURA_INICIAL }, locale);
	return respuesta;
  }
  if (String.valueOf(this.factor_correccion_inicial).length() > MAXIMA_LONGITUD_FACTOR_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor Inicial", MAXIMA_LONGITUD_FACTOR_INICIAL }, locale);
	return respuesta;
  }
  
  if (String.valueOf(this.volumen_corregido_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen 60F Inicial", MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL }, locale);
	return respuesta;
  }
  
  if (String.valueOf(this.medida_final).length() > MAXIMA_LONGITUD_MEDIDA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Medida Final", MAXIMA_LONGITUD_MEDIDA_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_observado_final).length() > MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. Obs. Final", MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.api_corregido_final).length() > MAXIMA_LONGITUD_API_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F Final", MAXIMA_LONGITUD_API_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.temperatura_final).length() > MAXIMA_LONGITUD_TEMPERATURA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura Final", MAXIMA_LONGITUD_TEMPERATURA_FINAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.factor_correccion_final).length() > MAXIMA_LONGITUD_FACTOR_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor Final", MAXIMA_LONGITUD_FACTOR_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_corregido_final).length() > MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen 60F Final", MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_FINAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.volumen_agua_final).length() > MAXIMA_LONGITUD_VOLUMEN_AGUA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen de Agua", MAXIMA_LONGITUD_VOLUMEN_AGUA_FINAL }, locale);
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

public Respuesta validarCambioTanqueFinal(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
	  if (!Utilidades.esValidoForingKey(this.id_producto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_tanque)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tanque" }, locale);
		return respuesta;
	  }

	  String texto = " de " + this.getDescripcionTanque();
	  if (!Utilidades.esValido(this.hora_final)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Hora Final" + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.medida_final) || this.medida_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Medida Final" + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_observado_final) || this.volumen_observado_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. Obs. Final" + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.api_corregido_final) || this.api_corregido_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F Final " + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.temperatura_final) || this.temperatura_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura Final" + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.factor_correccion_final) || this.factor_correccion_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor Final" + texto }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_corregido_final) || this.volumen_corregido_final == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen 60F Final" + texto }, locale);
		return respuesta;
	  }
	  /*if (!Utilidades.esValido(this.volumen_agua_final) || this.volumen_agua_final == 0){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen de Agua" + texto }, locale);
			return respuesta;
		  }*/
	  /*if (!Utilidades.esValido(this.estado_servicio)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.en_linea)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Desp" }, locale);
		return respuesta;
	  }
		*/
	  
	  if (String.valueOf(this.medida_final).length() > MAXIMA_LONGITUD_MEDIDA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Medida Final", MAXIMA_LONGITUD_MEDIDA_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_observado_final).length() > MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. Obs. Final", MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.api_corregido_final).length() > MAXIMA_LONGITUD_API_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F Final", MAXIMA_LONGITUD_API_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.temperatura_final).length() > MAXIMA_LONGITUD_TEMPERATURA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura Final", MAXIMA_LONGITUD_TEMPERATURA_FINAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.factor_correccion_final).length() > MAXIMA_LONGITUD_FACTOR_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor Final", MAXIMA_LONGITUD_FACTOR_FINAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_corregido_final).length() > MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen 60F Final", MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_FINAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.volumen_agua_final).length() > MAXIMA_LONGITUD_VOLUMEN_AGUA_FINAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen de Agua", MAXIMA_LONGITUD_VOLUMEN_AGUA_FINAL }, locale);
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

public Respuesta validarCambioTanqueInicial(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
	  if (!Utilidades.esValidoForingKey(this.id_producto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_tanque)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Tanque" }, locale);
		return respuesta;
	  }

	  if (!Utilidades.esValido(this.hora_inicial)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Hora Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.medida_inicial) || this.medida_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Medida Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_observado_inicial) || this.volumen_observado_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Vol. Obs. Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.api_corregido_inicial) || this.api_corregido_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "API 60F Inicial " }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.temperatura_inicial) || this.temperatura_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Temperatura Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.factor_correccion_inicial) || this.factor_correccion_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Factor Inicial" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_corregido_inicial) || this.volumen_corregido_inicial == 0){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen 60F Inicial" }, locale);
		return respuesta;
	  }
	  
	 /* if (!Utilidades.esValido(this.estado_servicio)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "F/S" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.en_linea)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Desp" }, locale);
		return respuesta;
	  }*/
		
	  if (String.valueOf(this.medida_inicial).length() > MAXIMA_LONGITUD_MEDIDA_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Medida Inicial", MAXIMA_LONGITUD_MEDIDA_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_observado_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Vol. Obs. Inicial", MAXIMA_LONGITUD_VOLUMEN_OBSERVADO_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.api_corregido_inicial).length() > MAXIMA_LONGITUD_API_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "API 60F Inicial", MAXIMA_LONGITUD_API_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.temperatura_inicial).length() > MAXIMA_LONGITUD_TEMPERATURA_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Temperatura Inicial", MAXIMA_LONGITUD_TEMPERATURA_INICIAL }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.factor_correccion_inicial).length() > MAXIMA_LONGITUD_FACTOR_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factor Inicial", MAXIMA_LONGITUD_FACTOR_INICIAL }, locale);
		return respuesta;
	  }
	  
	  if (String.valueOf(this.volumen_corregido_inicial).length() > MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen 60F Inicial", MAXIMA_LONGITUD_VOLUMEN_CORREGIDO_INICIAL }, locale);
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

public String getDescripcionTanque() {
	return descripcionTanque;
}

public void setDescripcionTanque(String descripcionTanque) {
	this.descripcionTanque = descripcionTanque;
}

public Timestamp getHoraInicial() {
	return hora_inicial;
}

public void setHoraInicial(Timestamp horaInicial) {
	this.hora_inicial = horaInicial;
}

public Timestamp getHoraFinal() {
	return hora_final;
}

public void setHoraFinal(Timestamp horaFinal) {
	this.hora_final = horaFinal;
}

public int getApertura() {
	return apertura;
}

public void setApertura(int apertura) {
	this.apertura = apertura;
}

public int getCierre() {
	return cierre;
}

public void setCierre(int cierre) {
	this.cierre = cierre;
}
	

}