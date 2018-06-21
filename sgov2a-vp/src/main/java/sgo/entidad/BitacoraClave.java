package sgo.entidad;

import java.sql.Date;

import sgo.utilidades.Constante;

public class BitacoraClave {
	private int id_bitacora_clave;
	private int id_usuario;
	private String clave;
	private Date actualizacion_clave;
	
	protected int formatoFecha=Constante.TIPO_FORMATO_FECHA_DDMMYYYY;

	public int getId() {
		return id_bitacora_clave;
	}

	public void setId(int id_bitacora_clave) {
		this.id_bitacora_clave = id_bitacora_clave;
	}

	public int getIdUsuario() {
		return id_usuario;
	}

	public void setIdUsuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getActualizacionClave() {
		return actualizacion_clave;
	}

	public void setActualizacionClave(Date actualizacion_clave) {
		this.actualizacion_clave = actualizacion_clave;
	}

	public int getFormatoFecha() {
		return formatoFecha;
	}

	public void setFormatoFecha(int formatoFecha) {
		this.formatoFecha = formatoFecha;
	}

}