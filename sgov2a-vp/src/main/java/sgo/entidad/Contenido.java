package sgo.entidad;

import java.util.List;

public class Contenido<T> {
	public List<T> carga;
	public int totalEncontrados;
	public int totalRegistros;
	public String sEcho;
	
	public  List<T> getCarga(){
		return this.carga;
	}
}
