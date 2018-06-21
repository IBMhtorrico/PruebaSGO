package sgo.entidad;

import java.sql.Timestamp;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;
public class GecAprobacion  {
  private int id_aprobacion_gec;
  private int id_gcombustible;
  private int id_usuario_registrado;
  private Timestamp fecha_hora_registrado;
  private int id_usuario_emitido;
  private Timestamp fecha_hora_emitido;
  private int id_usuario_aprobado;
  private Timestamp fecha_hora_aprobado;
  private int estado;
  private String observacionCliente;
  
  private Usuario registrador;
  private Usuario emisor;
  private Usuario aprobador;
  
  static final int ESTADO_APROBADO = 3;
  static final int ESTADO_OBSERVADO = 4;
  
  static final int MAXIMA_LONGITUD_OBSERVACION=700;

  public int getId(){
    return id_aprobacion_gec;
  }

  public void setId(int Id ){
    this.id_aprobacion_gec=Id;
  }

public int getIdGcombustible() {
	return id_gcombustible;
}

public void setIdGcombustible(int idGcombustible) {
	this.id_gcombustible = idGcombustible;
}

public int getIdUsuarioRegistrado() {
	return id_usuario_registrado;
}

public void setIdUsuarioRegistrado(int idUsuarioRegistrado) {
	this.id_usuario_registrado = idUsuarioRegistrado;
}

public Timestamp getFechaHoraRegistrado() {
	return fecha_hora_registrado;
}

public void setFechaHoraRegistrado(Timestamp fechaHoraRegistrado) {
	this.fecha_hora_registrado = fechaHoraRegistrado;
}

public int getIdUsuarioEmitido() {
	return id_usuario_emitido;
}

public void setIdUsuarioEmitido(int idUsuarioEmitido) {
	this.id_usuario_emitido = idUsuarioEmitido;
}

public Timestamp getFechaHoraEmitido() {
	return fecha_hora_emitido;
}

public void setFechaHoraEmitido(Timestamp fechaHoraEmitido) {
	this.fecha_hora_emitido = fechaHoraEmitido;
}

public int getIdUsuarioAprobado() {
	return id_usuario_aprobado;
}

public void setIdUsuarioAprobado(int idUsuarioAprobado) {
	this.id_usuario_aprobado = idUsuarioAprobado;
}

public Timestamp getFechaHoraAprobado() {
	return fecha_hora_aprobado;
}

public void setFechaHoraAprobado(Timestamp fechaHoraAprobado) {
	this.fecha_hora_aprobado = fechaHoraAprobado;
}

public int getEstado() {
	return estado;
}

public void setEstado(int estado) {
	this.estado = estado;
}

public Usuario getRegistrador() {
	return registrador;
}

public void setRegistrador(Usuario registrador) {
	this.registrador = registrador;
}

public Usuario getEmisor() {
	return emisor;
}

public void setEmisor(Usuario emisor) {
	this.emisor = emisor;
}

public Usuario getAprobador() {
	return aprobador;
}

public void setAprobador(Usuario aprobador) {
	this.aprobador = aprobador;
}

public String getObservacionCliente() {
	return observacionCliente;
}

public void setObservacionCliente(String observacionCliente) {
	this.observacionCliente = observacionCliente;
}

public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (this.observacionCliente.length() > MAXIMA_LONGITUD_OBSERVACION){			
			  respuesta.estado = false;
			  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Observación del cliente", MAXIMA_LONGITUD_OBSERVACION }, locale);
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
		if(!Utilidades.esValido(this.getObservacionCliente())){ this.setObservacionCliente(""); };
		cadena = this.getObservacionCliente();
		return cadena;
	}

}