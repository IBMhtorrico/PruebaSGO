package sgo.entidad;

import java.sql.Date;

public class DespachoCarga extends EntidadBase {
	private int id_dcarga;
	private String nombre_archivo;
	private Date fecha_carga;
	private String comentario;
	private int id_operario;
	private int id_estacion;
	private int id_jornada;
	private Operario operario;
	private Estacion estacion;
	
	public int getId() {
		return id_dcarga;
	}
	
	public void setId(int id) {
		this.id_dcarga = id;
	}

	public String getNombreArchivo() {
		return nombre_archivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombre_archivo = nombreArchivo;
	}

	public Date getFechaCarga() {
		return fecha_carga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fecha_carga = fechaCarga;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getIdOperario() {
		return id_operario;
	}

	public void setIdOperario(int idOperario) {
		this.id_operario = idOperario;
	}

	public int getIdEstacion() {
		return id_estacion;
	}

	public void setIdEstacion(int idEstacion) {
		this.id_estacion = idEstacion;
	}

	public Operario getOperario() {
		return operario;
	}

	public void setOperario(Operario operario) {
		this.operario = operario;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public int getIdJornada() {
		return id_jornada;
	}

	public void setIdJornada(int idJornada) {
		this.id_jornada = idJornada;
	}
	
}