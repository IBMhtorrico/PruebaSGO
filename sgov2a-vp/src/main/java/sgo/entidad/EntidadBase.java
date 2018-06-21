package sgo.entidad;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import sgo.utilidades.Constante;

public class EntidadBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int actualizadoPor;
	protected long actualizadoEl;
	protected int creadoPor;
	protected long creadoEl;
	protected String usuarioActualizacion;
	protected String usuarioCreacion;	
	protected String ipCreacion;
	protected String ipActualizacion;	

	protected int formatoFecha=Constante.TIPO_FORMATO_FECHA_DDMMYYYY;
	
	public void setFormatoFecha(int formatoFecha){
		this.formatoFecha=formatoFecha;
	}
	
	public String getIpCreacion() {
		return ipCreacion;
	}

	public void setIpCreacion(String direccionIpCreacion) {
		this.ipCreacion = direccionIpCreacion;
	}
	
	public String getIpActualizacion() {
		return ipActualizacion;
	}

	public void setIpActualizacion(String direccionIpActualizacion) {
		this.ipActualizacion = direccionIpActualizacion;
	}
	
	
	public String getFechaActualizacion() {
		String fechaActualizacionFormateada="";
		Date fechaActualizacion = new Date(actualizadoEl );
		SimpleDateFormat formateadorFecha = null;
		
		formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_DDMMYYYY){
			fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
		}
		
		formateadorFecha=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_MMDDYYYY){
			fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
		}
		
		formateadorFecha=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_ESTANDAR){			
			fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
		}
		return fechaActualizacionFormateada;
	}
	
	public String getFechaCreacion() {
		String fechaCreacionFormateada="";
		Date fechaCreacion = new Date(creadoEl);
		SimpleDateFormat formateadorFecha = null;
		
		formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_DDMMYYYY){
			fechaCreacionFormateada = formateadorFecha.format(fechaCreacion); 
		}
		formateadorFecha=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (this.formatoFecha== Constante.TIPO_FORMATO_FECHA_MMDDYYYY){
			fechaCreacionFormateada = formateadorFecha.format(fechaCreacion); 
		}
		formateadorFecha=new SimpleDateFormat("yyyy-M<-dd HH:mm:ss");
		if (this.formatoFecha == Constante.TIPO_FORMATO_FECHA_ESTANDAR){			
			fechaCreacionFormateada = formateadorFecha.format(fechaCreacion); 
		}
		return fechaCreacionFormateada;
	}
	
	public String getUsuarioActualizacion() {
		return usuarioActualizacion;
	}
	public void setUsuarioActualizacion(String usuarioActualizacion) {
		this.usuarioActualizacion = usuarioActualizacion;
	}
	
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	
	/**
	 * @return the _actualizadoPor
	 */
	public int getActualizadoPor() {
		return actualizadoPor;
	}
	/**
	 * @param _actualizadoPor the _actualizadoPor to set
	 */
	public void setActualizadoPor(int _actualizadoPor) {
		this.actualizadoPor = _actualizadoPor;
	}
	/**
	 * @return the _actualizadoEl
	 */
	public long getActualizadoEl() {
		return actualizadoEl;
	}
	/**
	 * @param _actualizadoEl the _actualizadoEl to set
	 */
	public void setActualizadoEl(long _actualizadoEl) {
		this.actualizadoEl = _actualizadoEl;
	}
	
	public int getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(int creadoPor) {
		this.creadoPor = creadoPor;
	}
	public long getCreadoEl() {
		return creadoEl;
	}
	public void setCreadoEl(long creadoEl) {
		this.creadoEl = creadoEl;
	}
}
