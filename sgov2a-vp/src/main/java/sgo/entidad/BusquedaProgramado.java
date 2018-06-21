package sgo.entidad;

import java.sql.Date;


public class BusquedaProgramado {
  private int idDiaOperativo;
  private int idOperacion;
  private Date fechaOperativa;
  private int idTransportista;
  private int idConductor;
  private int id_cisterna;
  private int estado;
  private int programacion;
  private int id_transporte;

  public int getIdDiaOperativo(){
    return idDiaOperativo;
  }

  public void setIdDiaOperativo(int IdDiaOperativo ){
    this.idDiaOperativo=IdDiaOperativo;
  }
  
  public int getIdOperacion(){
    return idOperacion;
  }

  public void setIdOperacion(int IdOperacion ){
    this.idOperacion=IdOperacion;
  }
  
  public Date getFechaOperativa(){
    return fechaOperativa;
  }

  public void setFechaOperativa(Date FechaOperativa ){
    this.fechaOperativa=FechaOperativa;
  }
  
  public int getIdTransportista(){
    return idTransportista;
  }

  public void setIdTransportista(int IdTransportista ){
    this.idTransportista=IdTransportista;
  }
  
  public int getIdConductor(){
    return idConductor;
  }

  public void setIdConductor(int IdConductor ){
    this.idConductor=IdConductor;
  }
  
  public int getEstado(){
    return estado;
  }

  public void setEstado(int Estado ){
    this.estado=Estado;
  }

  /**
   * @return the id_cisterna
   */
  public int getIdCisterna() {
   return id_cisterna;
  }

  /**
   * @param id_cisterna the id_cisterna to set
   */
  public void setIdCisterna(int id_cisterna) {
   this.id_cisterna = id_cisterna;
  }

  /**
   * @return the programacion
   */
  public int getProgramacion() {
   return programacion;
  }

  /**
   * @param programacion the programacion to set
   */
  public void setProgramacion(int programacion) {
   this.programacion = programacion;
  }

  /**
   * @return the id_transporte
   */
  public int getIdTransporte() {
   return id_transporte;
  }

  /**
   * @param id_transporte the id_transporte to set
   */
  public void setIdTransporte(int id_transporte) {
   this.id_transporte = id_transporte;
  }
  
  
}