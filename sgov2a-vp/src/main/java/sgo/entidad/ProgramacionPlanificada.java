package sgo.entidad;

public class ProgramacionPlanificada extends EntidadBase {

	private int id_doperativo;
	private float volumen_solicitado;
	private int cantidad_cisternas;
	private float volumen_asignado;
	private int cisternas_asignadas;
	
	public int getId_doperativo() {
		return id_doperativo;
	}
	public void setId_doperativo(int id_doperativo) {
		this.id_doperativo = id_doperativo;
	}
	public float getVolumen_solicitado() {
		return volumen_solicitado;
	}
	public void setVolumen_solicitado(float volumen_solicitado) {
		this.volumen_solicitado = volumen_solicitado;
	}
	public int getCantidad_cisternas() {
		return cantidad_cisternas;
	}
	public void setCantidad_cisternas(int cantidad_cisternas) {
		this.cantidad_cisternas = cantidad_cisternas;
	}
	public float getVolumen_asignado() {
		return volumen_asignado;
	}
	public void setVolumen_asignado(float volumen_asignado) {
		this.volumen_asignado = volumen_asignado;
	}
	public int getCisternas_asignadas() {
		return cisternas_asignadas;
	}
	public void setCisternas_asignadas(int cisternas_asignadas) {
		this.cisternas_asignadas = cisternas_asignadas;
	}

}
