package sgo.entidad;

public class ProductoProgramacion extends EntidadBase {

	private int id_operacion;
	private int id_cliente;
	private int id_producto;
	private String nombre;
	private String razon_social;
	private String abreviatura;
	private String actualizado_por;
	private String usuario_actualizacion;
	private String ip_actualizacion;
	public int getId_operacion() {
		return id_operacion;
	}
	public void setId_operacion(int id_operacion) {
		this.id_operacion = id_operacion;
	}
	public int getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	public int getId_producto() {
		return id_producto;
	}
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRazon_social() {
		return razon_social;
	}
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getActualizado_por() {
		return actualizado_por;
	}
	public void setActualizado_por(String actualizado_por) {
		this.actualizado_por = actualizado_por;
	}
	public String getUsuario_actualizacion() {
		return usuario_actualizacion;
	}
	public void setUsuario_actualizacion(String usuario_actualizacion) {
		this.usuario_actualizacion = usuario_actualizacion;
	}
	public String getIp_actualizacion() {
		return ip_actualizacion;
	}
	public void setIp_actualizacion(String ip_actualizacion) {
		this.ip_actualizacion = ip_actualizacion;
	}
}
