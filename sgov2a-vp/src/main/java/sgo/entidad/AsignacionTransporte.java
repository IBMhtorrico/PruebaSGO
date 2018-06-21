package sgo.entidad;

public class AsignacionTransporte extends EntidadBase{
	//Comentado por 9000002608===============================
//	private String descripcionProducto;
	private float volumenSolicitado;
	private float cisternasSolicitadas;
	private float volumenAsignado;
	private float cisternasAsignadas;
	//=======================================================
	//Comentado por 9000002608
//	/**
//	 * @return the descripcionProducto
//	 */
//	public String getDescripcionProducto() {
//		return descripcionProducto;
//	}
//	/**
//	 * @param descripcionProducto the descripcionProducto to set
//	 */
//	public void setDescripcionProducto(String descripcionProducto) {
//		this.descripcionProducto = descripcionProducto;
//	}
	//========================================================
	/**
	 * @return the volumesSolicitado
	 */
	public float getVolumenSolicitado() {
		return volumenSolicitado;
	}
	/**
	 * @param volumesSolicitado the volumesSolicitado to set
	 */
	public void setVolumenSolicitado(float volumenSolicitado) {
		this.volumenSolicitado = volumenSolicitado;
	}
	/**
	 * @return the cisternasSolicitadas
	 */
	public float getCisternasSolicitadas() {
		return cisternasSolicitadas;
	}
	/**
	 * @param cisternasSolicitadas the cisternasSolicitadas to set
	 */
	public void setCisternasSolicitadas(float cisternasSolicitadas) {
		this.cisternasSolicitadas = cisternasSolicitadas;
	}
	/**
	 * @return the volumenAsignado
	 */
	public float getVolumenAsignado() {
		return volumenAsignado;
	}
	/**
	 * @param volumenAsignado the volumenAsignado to set
	 */
	public void setVolumenAsignado(float volumenAsignado) {
		this.volumenAsignado = volumenAsignado;
	}
	/**
	 * @return the cisternasAsignadas
	 */
	public float getCisternasAsignadas() {
		return cisternasAsignadas;
	}
	/**
	 * @param cisternasAsignadas the cisternasAsignadas to set
	 */
	public void setCisternasAsignadas(float cisternasAsignadas) {
		this.cisternasAsignadas = cisternasAsignadas;
	}
	
}
