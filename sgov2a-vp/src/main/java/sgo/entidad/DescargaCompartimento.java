package sgo.entidad;

public class DescargaCompartimento  {
  private int id_dcompartimento;
  private int id_dcisterna;
  private int id_producto;
  private float capacidad_volumetrica_compartimento;
  private int altura_compartimento;
  private int altura_producto;
  private String unidad_medida_volumen;
  private int numero_compartimento;
  private int id_compartimento;
  private float temperatura_centro_cisterna;
  private float temperatura_probeta;
  private float api_temperatura_observada;
  private float api_temperatura_base;
  private float factor_correccion;
  private float volumen_recibido_observado;
  private float volumen_recibido_corregido;
  private Producto producto;
  private int altura_flecha;
  private int tipoVolumen;
  private float merma_porcentaje;
  private int tipo_metodo;
  
  
  
  
  public float getMermaPorcentaje() {
   return merma_porcentaje;
  }

  public void setMermaPorcentaje(float merma_porcentaje) {
   this.merma_porcentaje = merma_porcentaje;
  }

  public int getAlturaFlecha() {
   return altura_flecha;
  }

  public void setAlturaFlecha(int altura_flecha) {
   this.altura_flecha = altura_flecha;
  }

  public int getId(){
    return id_dcompartimento;
  }

  public void setId(int Id ){
    this.id_dcompartimento=Id;
  }
  
  public int getIdDescargaCisterna(){
    return id_dcisterna;
  }

  public void setIdDescargaCisterna(int IdDCisterna ){
    this.id_dcisterna=IdDCisterna;
  }
  
  public int getIdProducto(){
    return id_producto;
  }

  public void setIdProducto(int IdProducto ){
    this.id_producto=IdProducto;
  }
  
  public float getCapacidadVolumetricaCompartimento(){
    return capacidad_volumetrica_compartimento;
  }

  public void setCapacidadVolumetricaCompartimento(float CapacidadVolumetricaCompartimento ){
    this.capacidad_volumetrica_compartimento=CapacidadVolumetricaCompartimento;
  }
  
  public int getAlturaCompartimento(){
    return altura_compartimento;
  }

  public void setAlturaCompartimento(int AlturaCompartimento ){
    this.altura_compartimento=AlturaCompartimento;
  }
  
  public int getAlturaProducto(){
    return altura_producto;
  }

  public void setAlturaProducto(int AlturaProducto ){
    this.altura_producto=AlturaProducto;
  }
  
  public String getUnidadMedida(){
    return unidad_medida_volumen;
  }

  public void setUnidadMedida(String UnidadMedida ){
    this.unidad_medida_volumen=UnidadMedida;
  }
  
  public int getNumeroCompartimento(){
    return numero_compartimento;
  }

  public void setNumeroCompartimento(int NumeroCompartimento ){
    this.numero_compartimento=NumeroCompartimento;
  }
  
  public float getTemperaturaObservada(){
    return temperatura_centro_cisterna;
  }

  public void setTemperaturaObservada(float TemperaturaObservada ){
    this.temperatura_centro_cisterna=TemperaturaObservada;
  }
  
  public float getTemperaturaProbeta(){
    return temperatura_probeta;
  }

  public void setTemperaturaProbeta(float TemperaturaProbeta ){
    this.temperatura_probeta=TemperaturaProbeta;
  }
  
  public float getApiTemperaturaObservada(){
    return api_temperatura_observada;
  }

  public void setApiTemperaturaObservada(float ApiTemperaturaObservada ){
    this.api_temperatura_observada=ApiTemperaturaObservada;
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

  public void setFactorCorreccion(float FactorCorrecion ){
    this.factor_correccion=FactorCorrecion;
  }
  


  /**
   * @return the producto
   */
  public Producto getProducto() {
   return producto;
  }

  /**
   * @param producto the producto to set
   */
  public void setProducto(Producto producto) {
   this.producto = producto;
  }

  /**
   * @return the volumen_recibido_observado
   */
  public float getVolumenRecibidoObservado() {
   return volumen_recibido_observado;
  }

  /**
   * @param volumen_recibido_observado the volumen_recibido_observado to set
   */
  public void setVolumenRecibidoObservado(float volumen_recibido_observado) {
   this.volumen_recibido_observado = volumen_recibido_observado;
  }

  /**
   * @return the volumen_recibido_corregido
   */
  public float getVolumenRecibidoCorregido() {
   return volumen_recibido_corregido;
  }

  /**
   * @param volumen_recibido_corregido the volumen_recibido_corregido to set
   */
  public void setVolumenRecibidoCorregido(float volumen_recibido_corregido) {
   this.volumen_recibido_corregido = volumen_recibido_corregido;
  }

  /**
   * @return the id_compartimento
   */
  public int getIdCompartimento() {
   return id_compartimento;
  }

  /**
   * @param id_compartimento the id_compartimento to set
   */
  public void setIdCompartimento(int id_compartimento) {
   this.id_compartimento = id_compartimento;
  }

  /**
   * @return the tipoVolumen
   */
  public int getTipoVolumen() {
   return tipoVolumen;
  }

  /**
   * @param tipoVolumen the tipoVolumen to set
   */
  public void setTipoVolumen(int tipoVolumen) {
   this.tipoVolumen = tipoVolumen;
  }

  public int getTipoMetodo() {
		return tipo_metodo;
  }
	
  public void setTipoMetodo(int tipo_metodo) {
		this.tipo_metodo = tipo_metodo;
  }
  
  
}