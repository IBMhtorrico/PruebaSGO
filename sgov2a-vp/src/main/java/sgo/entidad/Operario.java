package sgo.entidad;

import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Operario extends EntidadBase {
	private int id_operario;
	private String nombre_operario;
	private String apellido_paterno_operario;
	private String apellido_materno_operario;
	private String dni_operario;
	private int indicador_operario;
	private int estado;
	private int id_cliente;
	private Cliente cliente;
	
	static final int MAXIMA_LONGITUD_NOMBRES_APELLIDOS=80;
	static final int MAXIMA_LONGITUD_DNI=8;
	
	public int getId() {
		return id_operario;
	}
	
	public void setId(int id) {
		this.id_operario = id;
	}
	
	public String getNombreOperario() {
		return nombre_operario;
	}
	
	public void setNombreOperario(String nombreOperario) {
		this.nombre_operario = nombreOperario;
	}
	
	public String getApellidoPaternoOperario() {
		return apellido_paterno_operario;
	}
	
	public void setApellidoPaternoOperario(String apellidoPaternoOperario) {
		this.apellido_paterno_operario = apellidoPaternoOperario;
	}
	
	public String getApellidoMaternoOperario() {
		return apellido_materno_operario;
	}
	
	public void setApellidoMaternoOperario(String apellidoMaternoOperario) {
		this.apellido_materno_operario = apellidoMaternoOperario;
	}
	
	public String getDniOperario() {
		return dni_operario;
	}
	
	public void setDniOperario(String dniOperario) {
		this.dni_operario = dniOperario;
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getNombreCompletoOperario() {
		return this.nombre_operario +" " + this.apellido_paterno_operario + " " + this.apellido_materno_operario;
	}

	public int getIdCliente() {
		return id_cliente;
	}

	public void setIdCliente(int idCliente) {
		this.id_cliente = idCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public boolean validar(){
		boolean resultado = true;
		
		if (this.nombre_operario.length()> MAXIMA_LONGITUD_NOMBRES_APELLIDOS){			
			return false;
		}
		
		if (this.apellido_paterno_operario.length()> MAXIMA_LONGITUD_NOMBRES_APELLIDOS){
			return false;
		}
		
		if (this.apellido_materno_operario.length()> MAXIMA_LONGITUD_NOMBRES_APELLIDOS){
			return false;
		}
		
		if ((this.dni_operario.length() != MAXIMA_LONGITUD_DNI)){
			return false;
		}
		
		return resultado;
	}
	
public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
  Respuesta respuesta = new Respuesta();
	try {
	  if (!Utilidades.esValido(this.nombre_operario)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Nombre" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.apellido_paterno_operario)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Apellido Paterno" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.apellido_materno_operario)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Apellido Materno" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValido(this.dni_operario)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "DNI" }, locale);
		return respuesta;
	  }
	  if (!Utilidades.esValidoForingKey(this.id_cliente)){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cliente" }, locale);
		return respuesta;
	  }
	  if (this.nombre_operario.length() > MAXIMA_LONGITUD_NOMBRES_APELLIDOS){	
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Nombre", MAXIMA_LONGITUD_NOMBRES_APELLIDOS }, locale);
		return respuesta;
	  }
	  if (this.apellido_paterno_operario.length() > MAXIMA_LONGITUD_NOMBRES_APELLIDOS){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Apellido Paterno", MAXIMA_LONGITUD_NOMBRES_APELLIDOS }, locale);
		return respuesta;
	  }
	  if (this.apellido_materno_operario.length() > MAXIMA_LONGITUD_NOMBRES_APELLIDOS){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Apellido Materno", MAXIMA_LONGITUD_NOMBRES_APELLIDOS }, locale);
		return respuesta;
	  }
	  if (this.dni_operario.length() > MAXIMA_LONGITUD_DNI){
		respuesta.estado = false;
		respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "DNI", MAXIMA_LONGITUD_DNI }, locale);
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
	if(!Utilidades.esValido(this.getNombreOperario())){ this.setNombreOperario(""); };
	if(!Utilidades.esValido(this.getApellidoPaternoOperario())){ this.setApellidoPaternoOperario(""); };
	if(!Utilidades.esValido(this.getApellidoMaternoOperario())){ this.setApellidoMaternoOperario(""); };
	if(!Utilidades.esValido(this.getDniOperario())){ this.setDniOperario(""); };

	cadena = this.getNombreOperario().toString() + 
			 this.getApellidoPaternoOperario().toString() + 
			 this.getApellidoMaternoOperario().toString() +
			 this.getDniOperario().toString();
	return cadena;
}

public int getIndicadorOperario() {
	return indicador_operario;
}

public void setIndicadorOperario(int indicadorOperario) {
	this.indicador_operario = indicadorOperario;
}	
}