package sgo.entidad;

public class Autorizacion extends EntidadBase{
	private int id_autorizacion;
	private String nombre; //80
	private String codigo_interno; //20
	private int estado;
	private int tieneAutorizacion;
	
	// variables para hacer las validaciones.
	static final int MAXIMA_LONGITUD_CODIGO_INTERNO = 20;

	public static final String TIPO_INGRESAR_VOLUMEN_DIRECTO="IVD";
	public static final String TIPO_REVERTIR_ESTADO_PROCESO="REP";

	/**
	 * @return the id_autorizacion
	 */
	public int getId() {
		return id_autorizacion;
	}
	/**
	 * @param id_autorizacion the id_autorizacion to set
	 */
	public void setId(int Id) {
		this.id_autorizacion = Id;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the codigo_interno
	 */
	public String getCodigoInterno() {
		return codigo_interno;
	}
	/**
	 * @param codigo_interno the codigo_interno to set
	 */
	public void setCodigoInterno(String codigoInterno) {
		this.codigo_interno = codigoInterno;
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
	/**
	 * @return the tieneAutirozacion
	 */
	public int getTieneAutorizacion() {
		return tieneAutorizacion;
	}
	/**
	 * @param tieneAutirozacion the tieneAutirozacion to set
	 */
	public void setTieneAutorizacion(int tieneAutorizacion) {
		this.tieneAutorizacion = tieneAutorizacion;
	}	  

	
	
}
