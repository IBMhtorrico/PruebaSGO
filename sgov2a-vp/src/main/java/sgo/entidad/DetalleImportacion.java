package sgo.entidad;

public class DetalleImportacion {
  private String id_dimportacion;
  private String id_mimportacion;
  private String codigo_referencia_producto;
  private String codigo_osinerg_producto;
  private String nombre_producto;
  private float capacidad_volumetrica_compartimento;
  private int numero_compartimento;
  private String unidad_medida_volumen;
  private float temperatura_observada;
  private float volumen_temperatura_observada;
  private float api_temperatura_base;
  private float factor_correccion;
  private float volumen_corregido_temperatura_base;
  

  public String getId(){
    return id_dimportacion;
  }

  public void setId(String Id ){
    this.id_dimportacion=Id;
  }
  
  public String getIdImportacion(){
    return id_mimportacion;
  }

  public void setIdImportacion(String IdImportacion ){
    this.id_mimportacion=IdImportacion;
  }
  
  public String getCodigoReferenciaProducto(){
    return codigo_referencia_producto;
  }

  public void setCodigoReferenciaProducto(String CodigoReferenciaProducto ){
    this.codigo_referencia_producto=CodigoReferenciaProducto;
  }
  
  public String getCodigoOsinergProducto(){
    return codigo_osinerg_producto;
  }

  public void setCodigoOsinergProducto(String CodigoOsinergProducto ){
    this.codigo_osinerg_producto=CodigoOsinergProducto;
  }
  
  public String getNombreProducto(){
    return nombre_producto;
  }

  public void setNombreProducto(String NombreProducto ){
    this.nombre_producto=NombreProducto;
  }
  
  public float getCapacidadVolumetricaCompartimento(){
    return capacidad_volumetrica_compartimento;
  }

  public void setCapacidadVolumetricaCompartimento(float CapacidadVolumetricaCompartimento ){
    this.capacidad_volumetrica_compartimento=CapacidadVolumetricaCompartimento;
  }
  
  public int getNumeroCompartimento(){
    return numero_compartimento;
  }

  public void setNumeroCompartimento(int NumeroCompartimento ){
    this.numero_compartimento=NumeroCompartimento;
  }
  
  public String getUnidadMedidaVolumen(){
    return unidad_medida_volumen;
  }

  public void setUnidadMedidaVolumen(String UnidadMedidaVolumen ){
    this.unidad_medida_volumen=UnidadMedidaVolumen;
  }
  
  public float getTemperaturaObservada(){
    return temperatura_observada;
  }

  public void setTemperaturaObservada(float TemperaturaObservada ){
    this.temperatura_observada=TemperaturaObservada;
  }
  
  public float getVolumenTemperaturaObservada(){
    return volumen_temperatura_observada;
  }

  public void setVolumenTemperaturaObservada(float VolumenTemperaturaObservada ){
    this.volumen_temperatura_observada=VolumenTemperaturaObservada;
  }
  
  public float getApiTemperaturaBase(){
    return api_temperatura_base;
  }

  public void setApiTemperaturaBase(float ApiTemperaturaBase ){
    this.api_temperatura_base=ApiTemperaturaBase;
  }
  
  public float getFactorCorreccion(){
    return factor_correccion;
  }

  public void setFactorCorreccion(float FactorCorreccion ){
    this.factor_correccion=FactorCorreccion;
  }
  
  public float getVolumenCorregidoTemperaturaBase(){
    return volumen_corregido_temperatura_base;
  }

  public void setVolumenCorregidoTemperaturaBase(float VolumenCorregidoTemperaturaBase ){
    this.volumen_corregido_temperatura_base=VolumenCorregidoTemperaturaBase;
  }
  
  
}