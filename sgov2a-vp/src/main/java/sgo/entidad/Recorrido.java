package sgo.entidad;

import sgo.utilidades.Utilidades;

public class Recorrido extends EntidadBase {
	private int id_recorrido;
	private int planta_origen;
	private int planta_destino;
	private int numero_dias;
	private int estado;
	private Planta plantaOrigen;
	private Planta plantaDestino;
	
	static final int MAXIMA_LONGITUD_NUMERO_DIAS=3;

	public int getId() {
		return id_recorrido;
	}

	public void setId(int Id) {
		this.id_recorrido = Id;
	}

	public int getIdPlantaOrigen() {
		return planta_origen;
	}

	public void setIdPlantaOrigen(int idPlantaOrigen) {
		this.planta_origen = idPlantaOrigen;
	}

	public int getIdPlantaDestino() {
		return planta_destino;
	}

	public void setIdPlantaDestino(int IdPlantaDestino) {
		this.planta_destino = IdPlantaDestino;
	}

	public int getNumeroDias() {
		return numero_dias;
	}

	public void setNumeroDias(int NumeroDias) {
		this.numero_dias = NumeroDias;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public Planta getPlantaOrigen() {
		return plantaOrigen;
	}

	public void setPlantaOrigen(Planta plantaOrigen) {
		this.plantaOrigen = plantaOrigen;
	}

	public Planta getPlantaDestino() {
		return plantaDestino;
	}

	public void setPlantaDestino(Planta plantaDestino) {
		this.plantaDestino = plantaDestino;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if(!Utilidades.esValido(this.planta_origen)){
			return false;
		}
		
		if(!Utilidades.esValido(this.plantaDestino)){
			return false;
		}
		
		if(!Utilidades.esValido(this.numero_dias) || this.numero_dias > MAXIMA_LONGITUD_NUMERO_DIAS){
			return false;
		}
		
		if(!Utilidades.esValido(this.estado)){
			return false;
		}

		return resultado;
	}

}