package sgo.entidad;

import java.sql.Date;
import java.sql.Timestamp;

public class EtapaTransporte extends EntidadBase{
	
	private Integer id;
	private Integer idOperacionEtapaRuta;
	
	private String nombreEtapa;
	private Integer  idTransporte;
	
	private Timestamp fechaInicio;
	private Timestamp fechaFin;
	
	//Se utiliza para el reporte de tiempos por etapa
	private Date fechaArribo;
	private Integer orden;
	
	private String sFechaInicio;
	private String sFechaFin;
	
	private Integer tiempoEtapa;
	
	private String observacion;	

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getNombreEtapa() {
		return nombreEtapa;
	}

	public Integer getIdTransporte() {
		return idTransporte;
	}

	public Date getFechaArribo() {
		return fechaArribo;
	}

	public void setFechaArribo(Date fechaArribo) {
		this.fechaArribo = fechaArribo;
	}

	public void setIdTransporte(Integer idTransporte) {
		this.idTransporte = idTransporte;
	}



	public void setNombreEtapa(String nombreEtapa) {
		this.nombreEtapa = nombreEtapa;
	}

	public String getsFechaInicio() {
		return sFechaInicio;
	}

	public void setsFechaInicio(String sFechaInicio) {
		this.sFechaInicio = sFechaInicio;
	}

	public String getsFechaFin() {
		return sFechaFin;
	}

	public void setsFechaFin(String sFechaFin) {
		this.sFechaFin = sFechaFin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdOperacionEtapaRuta() {
		return idOperacionEtapaRuta;
	}

	public void setIdOperacionEtapaRuta(Integer idOperacionEtapaRuta) {
		this.idOperacionEtapaRuta = idOperacionEtapaRuta;
	}

	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Timestamp getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getTiempoEtapa() {
		return tiempoEtapa;
	}

	public void setTiempoEtapa(Integer tiempoEtapa) {
		this.tiempoEtapa = tiempoEtapa;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
