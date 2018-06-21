package sgo.entidad;

import java.sql.Date;
import java.text.SimpleDateFormat;

import sgo.utilidades.Constante;

public class Cierre extends EntidadBase {
	private int id;
	private Date fechaOperativa;
	private float totalAsignados;
	private float totalDescargados;
	private long ultimaActualizacion;
	private int idUsuario;
	private String nombreUsuario;
	private String nombreOperacion;
	private String nombreCliente;
	private int estado;
	
	public static final int ESTADO_PLANIFICADO=1;
	public static final int ESTADO_ASIGNADO=2;
	public static final int ESTADO_DESCARGANDO=3;
	public static final int ESTADO_CERRADO=4;
	public static final int ESTADO_LIQUIDADO=5;
	
	protected int formatoFecha=Constante.TIPO_FORMATO_FECHA_DDMMYYYY;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the fechaOperativa
	 */
	public Date getFechaOperativa() {
		return fechaOperativa;
	}
	


	/**
	 * @param fechaOperativa the fechaOperativa to set
	 */
	public void setFechaOperativa(Date fechaOperativa) {
		this.fechaOperativa = fechaOperativa;
	}

	/**
	 * @return the totalAsignados
	 */
	public float getTotalAsignados() {
		return totalAsignados;
	}

	/**
	 * @param totalAsignados the totalAsignados to set
	 */
	public void setTotalAsignados(float totalAsignados) {
		this.totalAsignados = totalAsignados;
	}

	/**
	 * @return the totalDescargados
	 */
	public float getTotalDescargados() {
		return totalDescargados;
	}

	/**
	 * @param totalDescargados the totalDescargados to set
	 */
	public void setTotalDescargados(float totalDescargados) {
		this.totalDescargados = totalDescargados;
	}

	/**
	 * @return the ultimaActualizacion
	 */
	public String getUltimaActualizacion() {
		String fechaActualizacionFormateada="";
		Date fechaActualizacion = new Date(ultimaActualizacion );
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

	/**
	 * @param ultimaActualizacion the ultimaActualizacion to set
	 */
	public void setUltimaActualizacion(long ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}

	/**
	 * @return the idUsuario
	 */
	public int getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param nombreUsuario the nombreUsuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * @return the nombreOperacion
	 */
	public String getNombreOperacion() {
		return nombreOperacion;
	}

	/**
	 * @param nombreOperacion the nombreOperacion to set
	 */
	public void setNombreOperacion(String nombreOperacion) {
		this.nombreOperacion = nombreOperacion;
	}

	/**
	 * @return the nombreCliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * @param nombreCliente the nombreCliente to set
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}
}