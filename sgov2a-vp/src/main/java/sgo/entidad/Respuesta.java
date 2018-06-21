package sgo.entidad;

public class Respuesta {
	//
	/**
	 * mensaje variable que permite almacenar el texto que se mostrara al usuario como resultado de la accion.
	 */
	public String mensaje;
	/**
	 * estado Valor Verdadero  o falso, que permite verificar si la accion se ejecuto con exito.
	 */
	public Boolean estado;
	/**
	 * error Valor numerico que permite conocer el codigo del error.
	 */
	public int error;
	/**
	 * valor Cadena , que es el valor de respuesta producto de la accion.
	 */
	public String valor;
}
