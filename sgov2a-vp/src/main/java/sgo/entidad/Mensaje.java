package sgo.entidad;

import sgo.utilidades.Utilidades;

public class Mensaje extends EntidadBase {
	private int id_mensaje;
	private String titulo;
	
	//variable para hacer las validaciones.
	static final int MAXIMA_LONGITUD_TITULO = 20;

	public int getId() {
		return id_mensaje;
	}

	public void setId(int Id) {
		this.id_mensaje = Id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String Titulo) {
		this.titulo = Titulo;
	}
	
	public boolean validar(){
		boolean resultado = true;

		if (this.titulo.length() > MAXIMA_LONGITUD_TITULO || !Utilidades.esValido(this.titulo)){			
			return false;
		}

		return resultado;
	}
}