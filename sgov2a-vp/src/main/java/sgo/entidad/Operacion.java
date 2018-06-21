package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Operacion extends EntidadBase {
	private int id_operacion;
	private int id_cliente;
	private String nombre;
	private float volumen_promedio_cisterna;
	private int sincronizado_el;
	private String referencia_planta_recepcion;
	private String referencia_destinatario_mercaderia;
	private Date fecha_inicio_planificacion;
	private int estado;
	private Cliente cliente;
	private String alias;
	private int eta;
	private int idPlantaDespacho;
	private Planta plantaDespacho;
	private ArrayList<Transportista> transportistas;
	private String correoPara;
	private String correoCC;
	
	//Agregado por req 9000002570====================
	private ArrayList<OperacionEtapaRuta> etapas;
	
	public ArrayList<OperacionEtapaRuta> getEtapas() {
		return etapas;
	}

	public void setEtapas(ArrayList<OperacionEtapaRuta> etapas) {
		this.etapas = etapas;
	}
	//===============================================

	//variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_NOMBRE=150;
	static final int MAXIMA_LONGITUD_ALIAS=150;
	static final int MAXIMA_LONGITUD_REF_PLANTA_REFEPCION=20;
	static final int MAXIMA_LONGITUD_REF_PLANTA_MERCADERIA=20;
	static final int MAXIMA_LONGITUD_ETA=10;
	static final int MAXIMA_LONGITUD_VOLUMEN_PROM_CISTERNA=13;
	static final int MAXIMA_LONGITUD_CORREO_PARA=250;
	static final int MAXIMA_LONGITUD_CORREO_CC=250;

	/**
	 * @return the eta
	 */
	public int getEta() {
		return eta;
	}

	/**
	 * @param eta the eta to set
	 */
	public void setEta(int eta) {
		this.eta = eta;
	}

	/**
	 * @return the plantaDespacho
	 */
	public Planta getPlantaDespacho() {
		return plantaDespacho;
	}

	/**
	 * @param plantaDespacho the plantaDespacho to set
	 */
	public void setPlantaDespacho(Planta plantaDespacho) {
		this.plantaDespacho = plantaDespacho;
	}

	/**
	 * @return the plantaDespacho
	 */
	public int getIdPlantaDespacho() {
		return idPlantaDespacho;
	}

	/**
	 * @param plantaDespacho the plantaDespacho to set
	 */
	public void setIdPlantaDespacho(int idPlantaDespacho) {
		this.idPlantaDespacho = idPlantaDespacho;
	}

	public void agregarTransportista(Transportista transportista){
	 if (this.transportistas== null){
	  this.transportistas = new ArrayList<Transportista>();
	 }
	 this.transportistas.add(transportista);
	}
	
	 /**
	  * @return the transportistas
	  */
	 public ArrayList<Transportista> getTransportistas() {
	  return transportistas;
	 }

	 /**
	  * @param compartimentos the transportistas to set
	  */
	 public void setTransportistas(ArrayList<Transportista> transportistas) {
	  this.transportistas = transportistas;
	 }
	
	/**
	 * @return the fecha_inicio_planificacion
	 */
	public Date getFechaInicioPlanificacion() {
		return fecha_inicio_planificacion;
	}

	/**
	 * @param fecha_inicio_planificacion the fecha_inicio_planificacion to set
	 */
	public void setFechaInicioPlanificacion(Date fechaInicioPlanificacion) {
		this.fecha_inicio_planificacion = fechaInicioPlanificacion;
	}

	public String getReferenciaPlantaRecepcion() {
		return referencia_planta_recepcion;
	}

	public void setReferenciaPlantaRecepcion(String referencia_planta_recepcion) {
		this.referencia_planta_recepcion = referencia_planta_recepcion;
	}

	public String getReferenciaDestinatarioMercaderia() {
		return referencia_destinatario_mercaderia;
	}

	public void setReferenciaDestinatarioMercaderia(String referenciaDestinatarioMercaderia) {
		this.referencia_destinatario_mercaderia = referenciaDestinatarioMercaderia;
	}

	public int getId() {
		return id_operacion;
	}

	public void setId(int Id) {
		this.id_operacion = Id;
	}

	public float getVolumenPromedioCisterna() {
		return volumen_promedio_cisterna;
	}

	public void setVolumenPromedioCisterna(float VolumenPromedioCisterna) {
		this.volumen_promedio_cisterna = VolumenPromedioCisterna;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String Nombre) {
		this.nombre = Nombre;
	}

	public int getIdCliente() {
		return id_cliente;
	}

	public void setIdCliente(int IdCliente) {
		this.id_cliente = IdCliente;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public int getSincronizadoEl() {
		return sincronizado_el;
	}

	public void setSincronizadoEl(int SincronizadoEl) {
		this.sincronizado_el = SincronizadoEl;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	 public String getAlias() {
	  return alias;
	 }

	 public void setAlias(String alias) {
	  this.alias = alias;
	 }
	
	public boolean validar(){
		boolean resultado = true;

		if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE  || !Utilidades.esValido(this.nombre)){		
			return false;
		}
		
		if (this.referencia_destinatario_mercaderia.length() > MAXIMA_LONGITUD_REF_PLANTA_MERCADERIA  || !Utilidades.esValido(this.referencia_destinatario_mercaderia)){			
			return false;
		}
		
		if (this.referencia_planta_recepcion.length() > MAXIMA_LONGITUD_REF_PLANTA_REFEPCION  || !Utilidades.esValido(this.referencia_planta_recepcion)){			
			return false;
		}
		
		if(!Utilidades.esValido(this.id_cliente)){
			return false;
		}
		
		if(!Utilidades.esValido(this.volumen_promedio_cisterna)){
			return false;
		}

		/*if(!Utilidades.esValido(this.sincronizado_el)){
			return false;
		}*/
		if(!Utilidades.esValido(this.correoPara)){
			return false;
		}
		
		if(!Utilidades.esValido(this.correoCC)){
			return false;
		}
		
		return resultado;
	}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
  try {
  if (!Utilidades.esValido(this.nombre)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Nombre" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.alias)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Alias" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValidoForingKey(this.id_cliente)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cliente" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValidoForingKey(this.idPlantaDespacho)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Planta de Despacho" }, locale);
	return respuesta;
  }
  if (!Utilidades.esValido(this.volumen_promedio_cisterna)){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Volúmen Promedio Cisterna" }, locale);
	return respuesta;
  }
  if (!Utilidades.validarFecha(this.fecha_inicio_planificacion.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
    respuesta.estado = false;
    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Fecha de inicio de planificación" }, locale);
    return respuesta;
  }
  if (!Utilidades.esValido(this.eta)){
    respuesta.estado = false;
    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "ETA" }, locale);
    return respuesta;
  }
  if (!Utilidades.esValido(this.correoPara)){
    respuesta.estado = false;
    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "PARA" }, locale);
    return respuesta;
  }
  /*if (!Utilidades.esValido(this.correoCC)){
    respuesta.estado = false;
    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "CC" }, locale);
    return respuesta;
  }*/
	
  if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE){	
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre", MAXIMA_LONGITUD_NOMBRE }, locale);
	return respuesta;
  }
  if (this.alias.length() > MAXIMA_LONGITUD_ALIAS){
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Alias", MAXIMA_LONGITUD_ALIAS }, locale);
	return respuesta;
  }
  if (String.valueOf(this.eta).length() > MAXIMA_LONGITUD_ETA){
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Abreviatura", MAXIMA_LONGITUD_ETA }, locale);
	return respuesta;
  }
  if (String.valueOf(this.correoPara).length() > MAXIMA_LONGITUD_CORREO_PARA){
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Correo Para", MAXIMA_LONGITUD_CORREO_PARA }, locale);
	return respuesta;
  }
  if (String.valueOf(this.correoCC).length() > MAXIMA_LONGITUD_CORREO_CC){
	respuesta.estado = false;
	respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Correo CC", MAXIMA_LONGITUD_CORREO_CC }, locale);
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
if(!Utilidades.esValido(this.getNombre())){ this.setNombre(""); };
if(!Utilidades.esValido(this.getAlias())){ this.setAlias(""); };
if(!Utilidades.esValido(this.getReferenciaDestinatarioMercaderia())){ this.setReferenciaDestinatarioMercaderia(""); };
if(!Utilidades.esValido(this.getReferenciaPlantaRecepcion())){ this.setReferenciaPlantaRecepcion(""); };
if(!Utilidades.esValido(this.getVolumenPromedioCisterna())){ this.setVolumenPromedioCisterna(0); };
if(!Utilidades.esValido(this.getFechaInicioPlanificacion())){ this.setFechaInicioPlanificacion(new Date(0)); };
if(!Utilidades.esValido(this.getEta())){ this.setEta(0); };
if(!Utilidades.esValido(this.getCorreoPara())){ this.setCorreoPara(""); };
if(!Utilidades.esValido(this.getCorreoCC())){ this.setCorreoCC(""); };

	cadena = this.getNombre().toString() + 
			 this.getAlias().toString() +
			 this.getReferenciaDestinatarioMercaderia().toString() + 
			 this.getReferenciaPlantaRecepcion().toString() + 
			 Float.toString(this.getVolumenPromedioCisterna()) + 
			 this.getFechaInicioPlanificacion().toString() + 
			 String.valueOf(this.getEta() +
			 this.getCorreoPara().toString() +
			 this.getCorreoCC().toString() );
	return cadena;
}

public String getCorreoPara() {
	return correoPara;
}

public void setCorreoPara(String correoPara) {
	this.correoPara = correoPara;
}

public String getCorreoCC() {
	return correoCC;
}

public void setCorreoCC(String correoCC) {
	this.correoCC = correoCC;
}
 

}