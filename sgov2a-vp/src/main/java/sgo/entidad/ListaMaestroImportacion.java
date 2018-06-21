package sgo.entidad;

import java.util.ArrayList;

public class ListaMaestroImportacion {
 private int idDiaOperativo=0;
private ArrayList<MaestroImportacion> detalle;

/**
 * @return the detalle
 */
public ArrayList<MaestroImportacion> getDetalle() {
 return detalle;
}

/**
 * @param detalle the detalle to set
 */
public void setDetalle(ArrayList<MaestroImportacion> detalle) {
 this.detalle = detalle;
}

/**
 * @return the idDiaOperativo
 */
public int getIdDiaOperativo() {
 return idDiaOperativo;
}

/**
 * @param idDiaOperativo the idDiaOperativo to set
 */
public void setIdDiaOperativo(int idDiaOperativo) {
 this.idDiaOperativo = idDiaOperativo;
}
}
