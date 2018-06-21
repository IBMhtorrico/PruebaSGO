package sgo.entidad;

import java.util.List;

public class Asignacion extends EntidadBase{
	private int id_asignacion;
	private int id_doperativo;
	private int id_transporte;
	private List<DiaOperativo> dia_operativo ;
	private List<Transporte> transportes;
	
	/**
	 * @return the id_asignacion
	 */
	public int getId() {
		return id_asignacion;
	}
	/**
	 * @param id_asignacion the id_asignacion to set
	 */
	public void setId(int Id) {
		this.id_asignacion = Id;
	}
	/**
	 * @return the id_dia_operativo
	 */
	public int getIdDoperativo() {
		return id_doperativo;
	}
	/**
	 * @param id_dia_operativo the id_dia_operativo to set
	 */
	public void setIdDoperativo(int IdDoperativo) {
		this.id_doperativo = IdDoperativo;
	}
	/**
	 * @return the id_transporte
	 */
	public int getIdTransporte() {
		return id_transporte;
	}
	/**
	 * @param id_transporte the id_transporte to set
	 */
	public void setIdTransporte(int idTransporte) {
		this.id_transporte = idTransporte;
	}
	/**
	 * @return the dia_operativo
	 */
	public List<DiaOperativo> getDiaOperativo() {
		return dia_operativo;
	}
	/**
	 * @param dia_operativo the dia_operativo to set
	 */
	public void setDiaOperativo(List<DiaOperativo> diaOperativo) {
		this.dia_operativo = diaOperativo;
	}
	/**
	 * @return the transportes
	 */
	public List<Transporte> getTransportes() {
		return transportes;
	}
	/**
	 * @param transportes the transportes to set
	 */
	public void setTransportes(List<Transporte> transportes) {
		this.transportes = transportes;
	}

	
}
