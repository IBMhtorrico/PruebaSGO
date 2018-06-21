package sgo.entidad;

import java.sql.Date;
public class DetalleGEC  {
  private int id_dgec;
  private int id_gcombustible;
  private String numero_guia;
  private Date fecha_emision;
  private Date fecha_recepcion;
  private float volumen_despachado;
  private float volumen_recibido;
  private String estado;
  

  public int getId(){
    return id_dgec;
  }

  public void setId(int Id ){
    this.id_dgec=Id;
  }
  
  public int getIdGuiaCombustible(){
    return id_gcombustible;
  }

  public void setIdGuiaCombustible(int IdGuiaCombustible ){
    this.id_gcombustible=IdGuiaCombustible;
  }
  
  public String getNumeroGuia(){
    return numero_guia;
  }

  public void setNumeroGuia(String NumeroGuia ){
    this.numero_guia=NumeroGuia;
  }
  
  public Date getFechaEmision(){
    return fecha_emision;
  }

  public void setFechaEmision(Date FechaEmision ){
    this.fecha_emision=FechaEmision;
  }
  
  public Date getFechaRecepcion(){
    return fecha_recepcion;
  }

  public void setFechaRecepcion(Date FechaRecepcion ){
    this.fecha_recepcion=FechaRecepcion;
  }
  
  public float getVolumenDespachado(){
    return volumen_despachado;
  }

  public void setVolumenDespachado(float VolumenDespachado ){
    this.volumen_despachado=VolumenDespachado;
  }
  
  public float getVolumenRecibido(){
    return volumen_recibido;
  }

  public void setVolumenRecibido(float VolumenRecibido ){
    this.volumen_recibido=VolumenRecibido;
  }
  
  public String getEstado(){
    return estado;
  }

  public void setEstado(String Estado ){
    this.estado=Estado;
  }
  
  
}