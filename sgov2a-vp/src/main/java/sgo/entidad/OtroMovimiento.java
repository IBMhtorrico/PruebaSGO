package sgo.entidad;

import java.sql.Date;

public class OtroMovimiento extends EntidadBase {
	private int id_omovimiento;
	private int tipo_movimiento;
	private int clasificacion;
	private int id_jornada;
	private int volumen;
	private String comentario;
	private int id_tanque_origen;
	private int id_tanque_destino;
	private int numero_movimiento;
	//sirve para obtener la ultima jornada: 
	private String fecha_jornada;
	
	private Jornada jornada;
	private Tanque tanqueOrigen;
	private Tanque tanqueDestino;
	
	public int getId() {
		return id_omovimiento;
	}
	public void setId(int Id) {
		this.id_omovimiento = Id;
	}
	public int getTipoMovimiento() {
		return tipo_movimiento;
	}
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipo_movimiento = tipoMovimiento;
	}
	public int getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(int clasificacion) {
		this.clasificacion = clasificacion;
	}
	public int getIdJornada() {
		return id_jornada;
	}
	public void setIdJornada(int idJornada) {
		this.id_jornada = idJornada;
	}
	public int getVolumen() {
		return volumen;
	}
	public void setVolumen(int volumen) {
		this.volumen = volumen;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getIdTanqueOrigen() {
		return id_tanque_origen;
	}
	public void setIdTanqueOrigen(int idTanqueOrigen) {
		this.id_tanque_origen = idTanqueOrigen;
	}
	public int getIdTanqueDestino() {
		return id_tanque_destino;
	}
	public void setIdTanqueDestino(int idTanqueDestino) {
		this.id_tanque_destino = idTanqueDestino;
	}
	public int getNumeroMovimiento() {
		return numero_movimiento;
	}
	public void setNumeroMovimiento(int numeroMovimiento) {
		this.numero_movimiento = numeroMovimiento;
	}
	public Jornada getJornada() {
		return jornada;
	}
	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}
	public Tanque getTanqueOrigen() {
		return tanqueOrigen;
	}
	public void setTanqueOrigen(Tanque tanqueOrigen) {
		this.tanqueOrigen = tanqueOrigen;
	}
	public Tanque getTanqueDestino() {
		return tanqueDestino;
	}
	public void setTanqueDestino(Tanque tanqueDestino) {
		this.tanqueDestino = tanqueDestino;
	}
	public String getFechaJornada() {
		return fecha_jornada;
	}
	public void setFechaJornada(String fechaJornada) {
		this.fecha_jornada = fechaJornada;
	}
		
	
}