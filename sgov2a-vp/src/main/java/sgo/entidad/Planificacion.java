package sgo.entidad;

import java.util.Locale;
import org.springframework.context.MessageSource;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Planificacion extends EntidadBase {
	private int id_doperativo;
	private int id_planificacion;
	private int id_producto;
	private float volumen_propuesto;
	private float volumen_solicitado;
	private int cantidad_cisternas;
	private Producto producto;
	private String descProductoVolRequerido;
	private int estado;
	private String observacion;
	private String bitacora;
	private DiaOperativo diaOperativo;

	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_VOLUMEN_PROPUESTO=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_SOLICITADO=10;
	static final int MAXIMA_LONGITUD_CANTIDAD_CISTERNAS=5;
	static final int MAXIMA_LONGITUD_OBSERVACION=700;
	static final int MAXIMA_LONGITUD_BITACORA=1000;
	
	public final static int ESTADO_ACTIVO=1;
	public final static int ESTADO_INACTIVO=2;
	public final static int ESTADO_PLANIFICADO=3;
	public final static int ESTADO_PROGRAMADO=4;
	public final static int ESTADO_ANULADO=5;

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getDescProductoVolRequerido() {
		if(this.getProducto() != null && this.getProducto().getNombre() != null){
		descProductoVolRequerido = this.getProducto().getNombre() + "(" + String.valueOf((int)this.getVolumenSolicitado()) + ")";
		} else {
			descProductoVolRequerido = "";
		}			
		return descProductoVolRequerido;
	}

	public void setDescProductoVolRequerido(String descProductoVolRequerido) {
		this.descProductoVolRequerido = descProductoVolRequerido;
	}

	public int getIdDoperativo() {
		return id_doperativo;
	}

	public void setIdDoperativo(int id_doperativo) {
		this.id_doperativo = id_doperativo;
	}

	public int getId() {
		return id_planificacion;
	}

	public void setId(int Id) {
		this.id_planificacion = Id;
	}

	public int getIdProducto() {
		return id_producto;
	}

	public void setIdProducto(int IdProducto) {
		this.id_producto = IdProducto;
	}

	public float getVolumenPropuesto() {
		return volumen_propuesto;
	}

	public void setVolumenPropuesto(float VolumenPropuesto) {
		this.volumen_propuesto = VolumenPropuesto;
	}

	public float getVolumenSolicitado() {
		return volumen_solicitado;
	}

	public void setVolumenSolicitado(float VolumenSolicitado) {
		this.volumen_solicitado = VolumenSolicitado;
	}

	public int getCantidadCisternas() {
		return cantidad_cisternas;
	}

	public void setCantidadCisternas(int CantidadCisternas) {
		this.cantidad_cisternas = CantidadCisternas;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValidoForingKey(this.id_producto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_propuesto)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Promedio" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.volumen_solicitado)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volumen Solicitado" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.cantidad_cisternas)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cantidad de Cisternas" }, locale);
		return respuesta;
	  }
	  if (this.observacion.length() > MAXIMA_LONGITUD_OBSERVACION){			
		  respuesta.estado = false;
		  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Observación" }, locale);
		  return respuesta;
	  }

	  if (Float.toString(this.volumen_propuesto).length() > MAXIMA_LONGITUD_VOLUMEN_PROPUESTO){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Promedio", MAXIMA_LONGITUD_VOLUMEN_PROPUESTO }, locale);
		return respuesta;
	  }
	  if (Float.toString(this.volumen_solicitado).length() > MAXIMA_LONGITUD_VOLUMEN_SOLICITADO){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Volumen Solicitado", MAXIMA_LONGITUD_VOLUMEN_SOLICITADO }, locale);
		return respuesta;
	  }
	  if (String.valueOf(this.cantidad_cisternas).length() > MAXIMA_LONGITUD_CANTIDAD_CISTERNAS){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Cantidad de Cisternas", MAXIMA_LONGITUD_CANTIDAD_CISTERNAS }, locale);
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
	if(!Utilidades.esValido(this.getVolumenPropuesto())){ this.setVolumenPropuesto(0); };
	if(!Utilidades.esValido(this.getVolumenSolicitado())){ this.setVolumenSolicitado(0); };
	if(!Utilidades.esValido(this.getCantidadCisternas())){ this.setCantidadCisternas(0); };
	if(!Utilidades.esValido(this.getObservacion())){ this.setObservacion(""); };
	
	cadena = Float.toString(this.getVolumenPropuesto()) + 
			 Float.toString(this.getVolumenSolicitado()) +
			 String.valueOf(this.getCantidadCisternas()) +
			 this.getObservacion();

	return cadena;
}

public String getObservacion() {
	return observacion;
}

public void setObservacion(String observacion) {
	this.observacion = observacion;
}

public DiaOperativo getDiaOperativo() {
	return diaOperativo;
}

public void setDiaOperativo(DiaOperativo diaOperativo) {
	this.diaOperativo = diaOperativo;
}

public String getBitacora() {
	return bitacora;
}

public void setBitacora(String bitacora) {
	this.bitacora = bitacora;
}

	
}