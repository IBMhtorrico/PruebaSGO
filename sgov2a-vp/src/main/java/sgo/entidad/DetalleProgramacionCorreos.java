package sgo.entidad;

public class DetalleProgramacionCorreos {
  private int id_doperativo;
  private int id_programacion;
  private int id_dprogramacion;
  private int id_planta;
  private String comentario;
  
  private Planta planta;

public int getIdDoperativo() {
	return id_doperativo;
}

public void setIdDoperativo(int idDoperativo) {
	this.id_doperativo = idDoperativo;
}

public int getIdProgramacion() {
	return id_programacion;
}

public void setIdProgramacion(int idProgramacion) {
	this.id_programacion = idProgramacion;
}

public int getIdDprogramacion() {
	return id_dprogramacion;
}

public void setIdDprogramacion(int idDprogramacion) {
	this.id_dprogramacion = idDprogramacion;
}

public int getIdPlanta() {
	return id_planta;
}

public void setIdPlanta(int idPlanta) {
	this.id_planta = idPlanta;
}

public String getComentario() {
	return comentario;
}

public void setComentario(String comentario) {
	this.comentario = comentario;
}

public Planta getPlanta() {
	return planta;
}

public void setPlanta(Planta planta) {
	this.planta = planta;
}


}