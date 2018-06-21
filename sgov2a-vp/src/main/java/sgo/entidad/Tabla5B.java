package sgo.entidad;

public class Tabla5B  {
  private int id_tabla5b;
  private float temperatura_observada;
  private float api_observado;
  private float api_corregido;
  

  public int getId(){
    return id_tabla5b;
  }

  public void setId(int Id ){
    this.id_tabla5b=Id;
  }
  
  public float getTemperaturaObservada(){
    return temperatura_observada;
  }

  public void setTemperaturaObservada(float TemperaturaObservada ){
    this.temperatura_observada=TemperaturaObservada;
  }
  
  public float getApiObservado(){
    return api_observado;
  }

  public void setApiObservado(float ApiObservado ){
    this.api_observado=ApiObservado;
  }
  
  public float getApiCorregido(){
    return api_corregido;
  }

  public void setApiCorregido(float ApiCorregido ){
    this.api_corregido=ApiCorregido;
  }
  
  
}