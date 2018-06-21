package sgo.entidad;

public class TransportistaOperacion extends EntidadBase {
	private int id;
	private int id_transportista;
	private int id_operacion;
	private Operacion eOperacion;
	private Transportista eTransportista;
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
	 * @return the id_transportista
	 */
	public int getIdTransportista() {
		return id_transportista;
	}
	/**
	 * @param id_transportista the id_transportista to set
	 */
	public void setIdTransportista(int idTransportista) {
		this.id_transportista = idTransportista;
	}
	/**
	 * @return the id_operacion
	 */
	public int getIdOperacion() {
		return id_operacion;
	}
	/**
	 * @param id_operacion the id_operacion to set
	 */
	public void setIdOperacion(int idOperacion) {
		this.id_operacion = idOperacion;
	}
	/**
	 * @return the eOperacion
	 */
	public Operacion geteOperacion() {
		return eOperacion;
	}
	/**
	 * @param eOperacion the eOperacion to set
	 */
	public void seteOperacion(Operacion eOperacion) {
		this.eOperacion = eOperacion;
	}
	/**
	 * @return the eTransportista
	 */
	public Transportista geteTransportista() {
		return eTransportista;
	}
	/**
	 * @param eTransportista the eTransportista to set
	 */
	public void seteTransportista(Transportista eTransportista) {
		this.eTransportista = eTransportista;
	}

	
	
	
}