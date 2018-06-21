package sgo.entidad;

import java.util.ArrayList;

public class ListaContenedor {
private int id;
private ArrayList<ElementoContenedor> detalle;
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
 * @return the detalle
 */
public ArrayList<ElementoContenedor> getDetalle() {
 return detalle;
}
/**
 * @param detalle the detalle to set
 */
public void setDetalle(ArrayList<ElementoContenedor> detalle) {
 this.detalle = detalle;
}
}
