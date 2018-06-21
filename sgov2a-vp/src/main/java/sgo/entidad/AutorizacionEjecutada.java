package sgo.entidad;

import java.sql.Date;

public class AutorizacionEjecutada extends EntidadBase {
	private int id_aejecutada;
	private String descripcion;
	private int tipo_registro;
	private int id_registro;
	private long ejecutada_el;
	private int ejecutada_por;
	private int id_autorizacion;
	private int id_autorizador;
	private Date vigente_desde;
	private Date vigente_hasta;
	
	/**
	 * @return the id_aejecutada
	 */
	public int getId() {
		return id_aejecutada;
	}
	/**
	 * @param id_aejecutada the id_aejecutada to set
	 */
	public void setId(int Id) {
		this.id_aejecutada = Id;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the tipo_registro
	 */
	public int getTipoRegistro() {
		return tipo_registro;
	}
	/**
	 * @param tipo_registro the tipo_registro to set
	 */
	public void setTipoRegistro(int tipoRegistro) {
		this.tipo_registro = tipoRegistro;
	}
	/**
	 * @return the id_registro
	 */
	public int getIdRegistro() {
		return id_registro;
	}
	/**
	 * @param id_registro the id_registro to set
	 */
	public void setIdRegistro(int idRegistro) {
		this.id_registro = idRegistro;
	}
	/**
	 * @return the ejecutada_el
	 */
	public long getEjecutadaEl() {
		return ejecutada_el;
	}
	/**
	 * @param ejecutada_el the ejecutada_el to set
	 */
	public void setEjecutadaEl(long ejecutadaEl) {
		this.ejecutada_el = ejecutadaEl;
	}
	/**
	 * @return the ejecutada_por
	 */
	public int getEjecutadaPor() {
		return ejecutada_por;
	}
	/**
	 * @param ejecutada_por the ejecutada_por to set
	 */
	public void setEjecutadaPor(int ejecutadaPor) {
		this.ejecutada_por = ejecutadaPor;
	}
	/**
	 * @return the id_autorizacion
	 */
	public int getIdAutorizacion() {
		return id_autorizacion;
	}
	/**
	 * @param id_autorizacion the id_autorizacion to set
	 */
	public void setIdAutorizacion(int idAutorizacion) {
		this.id_autorizacion = idAutorizacion;
	}
	/**
	 * @return the id_autorizador
	 */
	public int getIdAutorizador() {
		return id_autorizador;
	}
	/**
	 * @param id_autorizador the id_autorizador to set
	 */
	public void setIdAutorizador(int idAutorizador) {
		this.id_autorizador = idAutorizador;
	}
	/**
	 * @return the vigente_desde
	 */
	public Date getVigenteDesde() {
		return vigente_desde;
	}
	/**
	 * @param vigente_desde the vigente_desde to set
	 */
	public void setVigenteDesde(Date vigenteDesde) {
		this.vigente_desde = vigenteDesde;
	}
	/**
	 * @return the vigente_hasta
	 */
	public Date getVigenteHasta() {
		return vigente_hasta;
	}
	/**
	 * @param vigente_hasta the vigente_hasta to set
	 */
	public void setVigenteHasta(Date vigenteHasta) {
		this.vigente_hasta = vigenteHasta;
	}
	

	

}