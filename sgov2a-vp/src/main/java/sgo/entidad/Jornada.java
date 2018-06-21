package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Jornada extends EntidadBase {
	
	private int id_jornada;
	private int id_estacion;
	private int estado;
	private int id_operario1;
	private int id_operario2;
	private String comentario;
	private Date fecha_operativa;
	private Operario operario2; //operario de salida
	private Operario operario1; //operario de entrada
	private Estacion estacion;
	private String observacion;
	private int total_despachos;
	private List<ContometroJornada> contometroJornada;
	private List<TanqueJornada> tanqueJornada;
	private List<Muestreo> muestreo;
	//listado de tanque y contometros cuando no existen registro de jornadas anteriores
	private List<Contometro> contometro;
	private List<Tanque> tanque;
	private List<Producto> producto;
	private List<ContometroJornada> contometroTurno;
	private boolean registroNuevo;
	
	private List<TanqueJornada> tanqueJornadaApertura;
	
	private List<TanqueJornada> tanqueJornadaCierre;
	
	private List<TanqueJornada> tanqueJornadaFinal;
	private List<TanqueJornada> tanqueJornadaInicial;
	
	public static final int ESTADO_ABIERTO=1;
	public static final int ESTADO_REGISTRADO=2;
	public static final int ESTADO_CERRADO=3;
	public static final int ESTADO_LIQUIDADO=4;
	
	static final int MAXIMA_LONGITUD_OBSERVACION = 700;
	
	public void agregarContometroJornada(ContometroJornada elemento){
	 if (this.contometroJornada == null){
	  this.contometroJornada = new ArrayList<ContometroJornada>();
	 }
	 this.contometroJornada.add(elemento);
	}
	
	public void agregarTanqueJornada(TanqueJornada elemento){
	 if (this.tanqueJornada == null){
	  this.tanqueJornada = new ArrayList<TanqueJornada>();
	 }
	 this.tanqueJornada.add(elemento);
	}
	
	public int getId() {
		return id_jornada;
	}

	public void setId(int id) {
		this.id_jornada = id;
	}

	public int getIdEstacion() {
		return id_estacion;
	}

	public void setIdEstacion(int idEstacion) {
		this.id_estacion = idEstacion;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getIdOperario1() {
		return id_operario1;
	}

	public void setIdOperario1(int idOperario1) {
		this.id_operario1 = idOperario1;
	}

	public int getIdOperario2() {
		return id_operario2;
	}

	public void setIdOperario2(int idOperario2) {
		this.id_operario2 = idOperario2;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaOperativa() {
		return fecha_operativa;
	}

	public void setFechaOperativa(Date fechaOperativa) {
		this.fecha_operativa = fechaOperativa;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public int getTotalDespachos() {
		return total_despachos;
	}

	public void setTotalDespachos(int totalDespachos) {
		this.total_despachos = totalDespachos;
	}

	public List<ContometroJornada> getContometroJornada() {
		return contometroJornada;
	}

	public void setContometroJornada(List<ContometroJornada> contometroJornada) {
		this.contometroJornada = contometroJornada;
	}

	public List<TanqueJornada> getTanqueJornada() {
		return tanqueJornada;
	}

	public void setTanqueJornada(List<TanqueJornada> tanqueJornada) {
		this.tanqueJornada = tanqueJornada;
	}

	public List<Contometro> getContometro() {
		return contometro;
	}

	public void setContometro(List<Contometro> contometro) {
		this.contometro = contometro;
	}

	public List<Tanque> getTanque() {
		return tanque;
	}

	public void setTanque(List<Tanque> tanque) {
		this.tanque = tanque;
	}

	public boolean isRegistroNuevo() {
		return registroNuevo;
	}

	public void setRegistroNuevo(boolean registroNuevo) {
		this.registroNuevo = registroNuevo;
	}

	public Operario getOperario2() {
		return operario2;
	}

	public void setOperario2(Operario operario2) {
		this.operario2 = operario2;
	}

	public Operario getOperario1() {
		return operario1;
	}

	public void setOperario1(Operario operario1) {
		this.operario1 = operario1;
	}

	public List<Muestreo> getMuestreo() {
		return muestreo;
	}

	public void setMuestreo(List<Muestreo> muestreo) {
		this.muestreo = muestreo;
	}

	public List<Producto> getProducto() {
		return producto;
	}

	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}
	
	//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
	public String getCadena() {
		String cadena="";
		if(!Utilidades.esValido(this.getIdOperario1())){ this.setIdOperario1(0); };
		if(!Utilidades.esValido(this.getIdOperario2())){ this.setIdOperario2(0); };
		if(!Utilidades.esValido(this.getObservacion())){ this.setObservacion(""); };
		
		cadena = String.valueOf(this.getIdOperario1()) + 
				 String.valueOf(this.getIdOperario2()) +
				 this.getObservacion(); 

		return cadena;
	}	

	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
		  if (!Utilidades.esValidoForingKey(this.id_operario1)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Operador 1" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_operario2)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Operador 2" }, locale);
			return respuesta;
		  }
		  if (this.observacion.length() > MAXIMA_LONGITUD_OBSERVACION){			
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Observación de Apertura", MAXIMA_LONGITUD_OBSERVACION }, locale);
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

	public List<ContometroJornada> getContometroTurno() {
		return contometroTurno;
	}

	public void setContometroTurno(List<ContometroJornada> contometroTurno) {
		this.contometroTurno = contometroTurno;
	}

	public List<TanqueJornada> getTanqueJornadaFinal() {
		return tanqueJornadaFinal;
	}

	public void setTanqueJornadaFinal(List<TanqueJornada> tanqueJornadaFinal) {
		this.tanqueJornadaFinal = tanqueJornadaFinal;
	}

	public List<TanqueJornada> getTanqueJornadaInicial() {
		return tanqueJornadaInicial;
	}

	public void setTanqueJornadaInicial(List<TanqueJornada> tanqueJornadaInicial) {
		this.tanqueJornadaInicial = tanqueJornadaInicial;
	}

	public List<TanqueJornada> getTanqueJornadaCierre() {
		return tanqueJornadaCierre;
	}

	public void setTanqueJornadaCierre(List<TanqueJornada> tanqueJornadaCierre) {
		this.tanqueJornadaCierre = tanqueJornadaCierre;
	}

	public List<TanqueJornada> getTanqueJornadaApertura() {
		return tanqueJornadaApertura;
	}

	public void setTanqueJornadaApertura(List<TanqueJornada> tanqueJornadaApertura) {
		this.tanqueJornadaApertura = tanqueJornadaApertura;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}