package sgo.entidad;

import java.sql.Date;
import java.util.Locale;
import org.springframework.context.MessageSource;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Usuario extends EntidadBase {
	private int id_usuario;
	private String nombre;
	private String clave;
	private String identidad;
	private String zona_horaria;
	private int estado;
	private String email;
	private int cambio_clave;
	private int id_rol;
	private int id_operacion;
	private int id_cliente;
	private Rol rol;
	private int tipo;
	private Operacion operacion;
	private Cliente cliente;
	private int id_transportista;
	private Transportista transportista;
	
	private Date actualizacion_clave;
	private int intentos;
	private int clave_temporal;

	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_NOMBRE = 16;
	static final int MAXIMA_LONGITUD_IDENTIDAD = 120;
	static final int MAXIMA_LONGITUD_CLAVE = 64;
	static final int MAXIMA_LONGITUD_ZONA_HORARIA = 20;
	static final int MAXIMA_LONGITUD_EMAIL = 250;
	
	static final int TIPO_INTERNO = 1;
	static final int TIPO_EXTERNO = 2;

	public int getId_rol() {
		return id_rol;
	}

	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	
	public String getAutogeneraClave(){
		return "P3tr0"+this.getNombre()+"p3ru";
	}

	public int getId() {
		return id_usuario;
	}

	public void setId(int Id) {
		this.id_usuario = Id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String Nombre) {
		this.nombre = Nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String Clave) {
		this.clave = Clave;
	}

	public String getIdentidad() {
		return identidad;
	}

	public void setIdentidad(String Identidad) {
		this.identidad = Identidad;
	}

	public String getZonaHoraria() {
		return zona_horaria;
	}

	public void setZonaHoraria(String ZonaHoraria) {
		this.zona_horaria = ZonaHoraria;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String Email) {
		this.email = Email;
	}

	public int getCambioClave() {
		return cambio_clave;
	}

	public void setCambioClave(int CambioClave) {
		this.cambio_clave = CambioClave;
	}

	public Rol getRol() {
		return rol;
	}

	/**
	 * @param _rol the _rol to set
	 */
	public void setRol(Rol _rol) {
		this.rol = _rol;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int Tipo) {
		this.tipo = Tipo;
	}

	public int getId_operacion() {
		return id_operacion;
	}

	public void setId_operacion(int id_operacion) {
		this.id_operacion = id_operacion;
	}

	public Operacion getOperacion() {
		return operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}
	
	
	
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	/**
	 * @return the id_transportista
	 */
	public int getIdTransportista() {
		return id_transportista;
	}

	/**
	 * @param id_transportista the id_transportista to set
	 */
	public void setIdTransportista(int idTransportista) {
		this.id_transportista = idTransportista;
	}

	/**
	 * @return the transportista
	 */
	public Transportista getTransportista() {
		return transportista;
	}

	/**
	 * @param transportista the transportista to set
	 */
	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}

 /**
  * @return the id_cliente
  */
 public int getIdCliente() {
  return id_cliente;
 }

 /**
  * @param id_cliente the id_cliente to set
  */
 public void setIdCliente(int id_cliente) {
  this.id_cliente = id_cliente;
 }
 
public boolean validar(){
	boolean resultado = true;

	if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE|| !Utilidades.esValido(this.nombre)){	return false; }
	
	if (this.identidad.length() > MAXIMA_LONGITUD_IDENTIDAD  || !Utilidades.esValido(this.identidad)){	return false; }
	
	if (this.zona_horaria.length() > MAXIMA_LONGITUD_ZONA_HORARIA  || !Utilidades.esValido(this.zona_horaria)){	return false; }		
	
	if (this.clave.length() > MAXIMA_LONGITUD_CLAVE || !Utilidades.esValido(this.clave)){	return false; }		

	if (this.email.length() > MAXIMA_LONGITUD_EMAIL || !Utilidades.esValido(this.email)){	return false; }			
	
	if(!Utilidades.esValido(this.id_rol)){	return false; }
	
	if(!Utilidades.esValido(this.id_operacion)){	return false; }

	if(!Utilidades.esValido(this.tipo)){	return false; }
	
	if(!Utilidades.esValido(this.estado)){	return false; }
	return resultado;
}
 
 public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.nombre)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Usuario" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.identidad)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Nombre" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.clave)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Contraseña" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.email)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Email" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.zona_horaria)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Zona Horaria" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_rol)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Rol" }, locale);
		return respuesta;
	  }
	  /*if (!Utilidades.esValidoForingKey(this.id_cliente)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cliente" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_operacion)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Operación" }, locale);
		return respuesta;
	  }*/

	  if (this.nombre.length() > MAXIMA_LONGITUD_NOMBRE){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Usuario", MAXIMA_LONGITUD_NOMBRE }, locale);
		return respuesta;
	  }
	  if (this.identidad.length() > MAXIMA_LONGITUD_IDENTIDAD){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre", MAXIMA_LONGITUD_IDENTIDAD }, locale);
		return respuesta;
	  }
	  if (this.clave.length() > MAXIMA_LONGITUD_CLAVE){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Clave", MAXIMA_LONGITUD_CLAVE }, locale);
		return respuesta;
	  }
	  if (this.email.length() > MAXIMA_LONGITUD_EMAIL){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Email", MAXIMA_LONGITUD_EMAIL }, locale);
		return respuesta;
	  }
	  if (this.zona_horaria.length() > MAXIMA_LONGITUD_ZONA_HORARIA){
	    respuesta.estado = false;
	    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Zona Horaria", MAXIMA_LONGITUD_ZONA_HORARIA }, locale);
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
	if(!Utilidades.esValido(this.getIdentidad())){ this.setIdentidad(""); };
	if(!Utilidades.esValido(this.getClave())){ this.setClave(""); };
	if(!Utilidades.esValido(this.getEmail())){ this.setEmail(""); };

	cadena = this.getNombre().toString() + 
			 this.getIdentidad().toString() +
			 this.getClave().toString() + 
			 this.getEmail().toString() ;
	return cadena;
}

public Date getActualizacionClave() {
	return actualizacion_clave;
}

public void setActualizacionClave(Date actualizacionClave) {
	this.actualizacion_clave = actualizacionClave;
}

public int getIntentos() {
	return intentos;
}

public void setIntentos(int intentos) {
	this.intentos = intentos;
}

public int getClaveTemporal() {
	return clave_temporal;
}

public void setClaveTemporal(int claveTemporal) {
	this.clave_temporal = claveTemporal;
}

}