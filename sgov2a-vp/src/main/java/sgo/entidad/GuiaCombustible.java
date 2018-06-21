package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class GuiaCombustible extends EntidadBase {
  private int id_gcombustible;
  private String orden_compra;
  private Date fecha_guia_combustible;
  private int id_transportista;
  private String numero_gec;
  private String numero_contrato;
  private String descripcion_contrato;
  private int estado;
  private String comentarios;
  private int id_producto;
  private int idOperacion;
  private String numeroSerie;
  private int idCliente;
  private String nombreOperacion;
  private String nombreCliente;
  private String nombreProducto;
  private String nombreTransportista;
  private String codigo_referencia;
  private String referencia_planta_recepcion;
  private ArrayList<DetalleGEC> detalle;
  private float totalVolumenRecibido;
  private float totalVolumenDespachado;
  private GecAprobacion aprobacionGec;
  private Cliente cliente;
  private Operacion operacion;
  
  //variables para hacer las validaciones
  static final int MAXIMA_LONGITUD_ORDEN_COMPRA=20;
  static final int MAXIMA_LONGITUD_COMENTARIO=500;
  
  public static final int ESTADO_REGISTRADO=1;
  public static final int ESTADO_EMITIDO=2;
  public static final int ESTADO_APROBADO=3;
  public static final int ESTADO_OBSERVADO=4;

  public int getId(){
    return id_gcombustible;
  }

  public void setId(int Id ){
    this.id_gcombustible=Id;
  }
  
  public String getOrdenCompra(){
    return orden_compra;
  }

  public void setOrdenCompra(String OrdenCompra ){
    this.orden_compra=OrdenCompra;
  }
  
  public Date getFechaGuiaCombustible(){
    return fecha_guia_combustible;
  }

  public void setFechaGuiaCombustible(Date FechaGuiaCombustible ){
    this.fecha_guia_combustible=FechaGuiaCombustible;
  }
  
  public int getIdTransportista(){
    return id_transportista;
  }

  public void setIdTransportista(int IdTransportista ){
    this.id_transportista=IdTransportista;
  }
  
  public String getNumeroGEC(){
    return numero_gec;
  }

  public void setNumeroGEC(String NumeroGEC ){
    this.numero_gec=NumeroGEC;
  }
  
  public String getNumeroGuia(){
   return numeroSerie + "-" +  this.numero_gec;
  }
  
  public String getNumeroContrato(){
    return numero_contrato;
  }

  public void setNumeroContrato(String NumeroContrato ){
    this.numero_contrato=NumeroContrato;
  }
  
  public String getDescripcionContrato(){
    return descripcion_contrato;
  }

  public void setDescripcionContrato(String DescripcionContrato ){
    this.descripcion_contrato=DescripcionContrato;
  }
  
  public int getEstado(){
    return estado;
  }

  public void setEstado(int Estado ){
    this.estado=Estado;
  }
  
  public String getComentarios(){
    return comentarios;
  }

  public void setComentarios(String Comentarios ){
    this.comentarios=Comentarios;
  }
  
  public int getIdProducto(){
    return id_producto;
  }

  public void setIdProducto(int IdProducto ){
    this.id_producto=IdProducto;
  }



  /**
   * @return the numeroSerie
   */
  public String getNumeroSerie() {
   return numeroSerie;
  }

  /**
   * @param numeroSerie the numeroSerie to set
   */
  public void setNumeroSerie(String numeroSerie) {
   this.numeroSerie = numeroSerie;
  }

  /**
   * @return the idOperacion
   */
  public int getIdOperacion() {
   return idOperacion;
  }

  /**
   * @param idOperacion the idOperacion to set
   */
  public void setIdOperacion(int idOperacion) {
   this.idOperacion = idOperacion;
  }

  /**
   * @return the idCliente
   */
  public int getIdCliente() {
   return idCliente;
  }

  /**
   * @param idCliente the idCliente to set
   */
  public void setIdCliente(int idCliente) {
   this.idCliente = idCliente;
  }

  /**
   * @return the nombreOperacion
   */
  public String getNombreOperacion() {
   return nombreOperacion;
  }

  /**
   * @param nombreOperacion the nombreOperacion to set
   */
  public void setNombreOperacion(String nombreOperacion) {
   this.nombreOperacion = nombreOperacion;
  }

  /**
   * @return the nombreCliente
   */
  public String getNombreCliente() {
   return nombreCliente;
  }

  /**
   * @param nombreCliente the nombreCliente to set
   */
  public void setNombreCliente(String nombreCliente) {
   this.nombreCliente = nombreCliente;
  }

  /**
   * @return the nombreProducto
   */
  public String getNombreProducto() {
   return nombreProducto;
  }

  /**
   * @param nombreProducto the nombreProducto to set
   */
  public void setNombreProducto(String nombreProducto) {
   this.nombreProducto = nombreProducto;
  }

  /**
   * @return the nombreTransportista
   */
  public String getNombreTransportista() {
   return nombreTransportista;
  }

  /**
   * @param nombreTransportista the nombreTransportista to set
   */
  public void setNombreTransportista(String nombreTransportista) {
   this.nombreTransportista = nombreTransportista;
  }
  
  public void agregarDetalle (DetalleGEC elemento){
   if (this.detalle== null){
    this.detalle = new ArrayList<DetalleGEC>();
   }
   this.detalle.add(elemento);
  }

  /**
   * @return the detalle
   */
  public ArrayList<DetalleGEC> getDetalle() {
   return detalle;
  }

  /**
   * @param detalle the detalle to set
   */
  public void setDetalle(ArrayList<DetalleGEC> detalle) {
   this.detalle = detalle;
  }

  /**
   * @return the totalVolumenRecibido
   */
  public float getTotalVolumenRecibido() {
   return totalVolumenRecibido;
  }

  /**
   * @param totalVolumenRecibido the totalVolumenRecibido to set
   */
  public void setTotalVolumenRecibido(float totalVolumenRecibido) {
   this.totalVolumenRecibido = totalVolumenRecibido;
  }

  /**
   * @return the totalVolumenDespachado
   */
  public float getTotalVolumenDespachado() {
   return totalVolumenDespachado;
  }

  /**
   * @param totalVolumenDespachado the totalVolumenDespachado to set
   */
  public void setTotalVolumenDespachado(float totalVolumenDespachado) {
   this.totalVolumenDespachado = totalVolumenDespachado;
  }
  
  public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.orden_compra)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Orden de Compra" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.id_producto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Producto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.id_transportista)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Transportista" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.idOperacion)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Operacion" }, locale);
			return respuesta;
		  }

		  if (this.orden_compra.length() > MAXIMA_LONGITUD_ORDEN_COMPRA){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Orden de Compra", MAXIMA_LONGITUD_ORDEN_COMPRA }, locale);
			return respuesta;
		  }

		  respuesta.estado = true;
		  respuesta.valor = null;
	  } catch (Exception excepcionGenerica) {
	   excepcionGenerica.printStackTrace();
	   respuesta.error = Constante.EXCEPCION_GENERICA;
	   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
	   respuesta.estado = false;
	  }
	  return respuesta;
	}
	
	//CADENAS SEGUN LOS CAMPOS QUE VIENEN DEL FORMULARIO
	public String getCadena() {
		String cadena="";
		if(!Utilidades.esValido(this.getOrdenCompra())){ this.setOrdenCompra(""); };
		if(!Utilidades.esValido(this.getComentarios())){ this.setComentarios(""); };
	
		cadena = this.getOrdenCompra().toString() + 
				 this.getComentarios().toString() ;
		return cadena;
	}

	public GecAprobacion getAprobacionGec() {
		return aprobacionGec;
	}

	public void setAprobacionGec(GecAprobacion aprobacionGec) {
		this.aprobacionGec = aprobacionGec;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Operacion getOperacion() {
		return operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	public String getCodigo_referencia() {
		return codigo_referencia;
	}

	public void setCodigo_referencia(String codigo_referencia) {
		this.codigo_referencia = codigo_referencia;
	}

	public String getReferencia_planta_recepcion() {
		return referencia_planta_recepcion;
	}

	public void setReferencia_planta_recepcion(String referencia_planta_recepcion) {
		this.referencia_planta_recepcion = referencia_planta_recepcion;
	}



	
}