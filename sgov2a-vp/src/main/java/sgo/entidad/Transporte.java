package sgo.entidad;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Transporte extends EntidadBase {
	private int id_transporte;
	private String numero_guia_remision;
	private String numero_orden_entrega;
	private String numero_factura;
	private String codigo_scop;
	private Date fecha_emision;
	private int id_planta_despacho;
	private int id_planta_recepcion;
	private int id_cliente;
	private int id_conductor;
	private String brevete_conductor;
	private int id_cisterna;
	private String placa_cisterna;
	private String tarjeta_cubicacion_cisterna;
	private int id_tracto;
	private String placa_tracto;
	private int id_transportista;
	private float volumen_total_observado;
	private float volumen_total_corregido;
	private int estado;
	private long sincronizado_el;
	private String cisterna_tracto;
	private String precintos_seguridad;
	private String tipo_registro;
	private float peso_bruto;
	private float peso_tara;
	private float peso_neto;
	private Planta plantaDespacho;
	private Operacion plantaRecepcion;
	private Conductor conductor;
	private Cisterna cisterna;
	private Transportista transportista;
	private List<DetalleTransporte> detalles;
	private List<Evento> eventos;
	private int programacion;
	
	//agregado por req 9000002570=================
	private ArrayList<EtapaTransporte> etapasTransporte;
	
	public ArrayList<EtapaTransporte> getEtapasTransporte() {
		return etapasTransporte;
	}

	public void setEtapasTransporte(ArrayList<EtapaTransporte> etapasTransporte) {
		this.etapasTransporte = etapasTransporte;
	}
	
	//===========================================

	public final static int ESTADO_ACTIVO=1;
	public final static int ESTADO_INACTIVO=2;
	public final static int ESTADO_ASIGNADO=3;
	public final static int ESTADO_DESCARGADO=4;
	public final static int ESTADO_ANULADO=5;
	public final static int ESTADO_PROGRAMADO=6;
	
	public final static String ORIGEN_MANUAL="M";
	public final static String ORIGEN_AUTOMATICO="A";
	public final static String ORIGEN_SEMI_AUTOMATICO="P";
	
	//variables para hacer las validaciones
	static final int MAXIMA_LONGITUD_GUIA_REMISION=20;
	static final int MAXIMA_LONGITUD_ORDEN_COMPRA=20;
	static final int MAXIMA_LONGITUD_FACTURA=15;
	static final int MAXIMA_LONGITUD_SCOP=20;
	static final int MAXIMA_LONGITUD_PRECINTOS=180;
	
	public int getProgramacion() {
		return programacion;
	}

	public void setProgramacion(int programacion) {
		this.programacion = programacion;
	}

	public void agregarDetalle(DetalleTransporte elemento){
	 if (this.detalles == null){
	  this.detalles = new ArrayList<DetalleTransporte>();
	 }
	 this.detalles.add(elemento);
	}
	
	 public void agregarEvento(Evento elemento){
	   if (this.eventos == null){
	    this.eventos = new ArrayList<Evento>();
	   }
	   this.eventos.add(elemento);
	  }
	
	public int getId() {
		return id_transporte;
	}
	

	public void setId(int Id) {
		this.id_transporte = Id;
	}

	/**
	 * @return the precintos_seguridad
	 */
	public String getPrecintosSeguridad() {
		return precintos_seguridad;
	}

	/**
	 * @param precintos_seguridad
	 *            the precintos_seguridad to set
	 */
	public void setPrecintosSeguridad(String precintos_seguridad) {
		this.precintos_seguridad = precintos_seguridad;
	}

	/**
	 * @return the origen
	 */
	public String getOrigen() {
		return tipo_registro;
	}

	/**
	 * @param origen the origen to set
	 */
	public void setOrigen(String origen) {
		this.tipo_registro = origen;
	}

	public String getCisternaTracto() {
		return this.cisterna_tracto;
		// return this.placa_cisterna + " / " + this.placa_tracto;
	}

	public void setCisternaTracto(String CisternaTracto) {
		this.cisterna_tracto = CisternaTracto;
	}

	public String getNumeroGuiaRemision() {
		return numero_guia_remision;
	}

	public void setNumeroGuiaRemision(String NumeroGuiaRemision) {
		this.numero_guia_remision = NumeroGuiaRemision;
	}

	public String getNumeroOrdenCompra() {
		return numero_orden_entrega;
	}

	public void setNumeroOrdenCompra(String NumeroOrdenCompra) {
		this.numero_orden_entrega = NumeroOrdenCompra;
	}

	public String getNumeroFactura() {
		return numero_factura;
	}

	public void setNumeroFactura(String NumeroFactura) {
		this.numero_factura = NumeroFactura;
	}

	public String getCodigoScop() {
		return codigo_scop;
	}

	public void setCodigoScop(String CodigoScop) {
		this.codigo_scop = CodigoScop;
	}

	public Date getFechaEmisionGuia() {
		return fecha_emision;
	}

	public void setFechaEmisionGuia(Date FechaEmisionGuia) {
		this.fecha_emision = FechaEmisionGuia;
	}

	public int getIdPlantaDespacho() {
		return id_planta_despacho;
	}

	public void setIdPlantaDespacho(int IdPlantaDespacho) {
		this.id_planta_despacho = IdPlantaDespacho;
	}

	public int getIdPlantaRecepcion() {
		return id_planta_recepcion;
	}

	public void setIdPlantaRecepcion(int IdPlantaRecepcion) {
		this.id_planta_recepcion = IdPlantaRecepcion;
	}

	public int getIdCliente() {
		return id_cliente;
	}

	public void setIdCliente(int IdCliente) {
		this.id_cliente = IdCliente;
	}

	public int getIdConductor() {
		return id_conductor;
	}

	public void setIdConductor(int IdConductor) {
		this.id_conductor = IdConductor;
	}
	
	/**
	 * @return the cisterna
	 */
	public Cisterna getCisterna() {
		return cisterna;
	}

	/**
	 * @param cisterna the cisterna to set
	 */
	public void setCisterna(Cisterna cisterna) {
		this.cisterna = cisterna;
	}

	public String getBreveteConductor() {
		return brevete_conductor;
	}

	public void setBreveteConductor(String BreveteConductor) {
		this.brevete_conductor = BreveteConductor;
	}

	public int getIdCisterna() {
		return id_cisterna;
	}

	public void setIdCisterna(int IdCisterna) {
		this.id_cisterna = IdCisterna;
	}

	public String getPlacaCisterna() {
		return placa_cisterna;
	}

	public void setPlacaCisterna(String PlacaCisterna) {
		this.placa_cisterna = PlacaCisterna;
	}

	public String getTarjetaCubicacionCompartimento() {
		return tarjeta_cubicacion_cisterna;
	}

	public void setTarjetaCubicacionCompartimento(
			String TarjetaCubicacionCompartimento) {
		this.tarjeta_cubicacion_cisterna = TarjetaCubicacionCompartimento;
	}

	public int getIdTracto() {
		return id_tracto;
	}

	public void setIdTracto(int IdTracto) {
		this.id_tracto = IdTracto;
	}

	public String getPlacaTracto() {
		return placa_tracto;
	}

	public void setPlacaTracto(String PlacaTracto) {
		this.placa_tracto = PlacaTracto;
	}

	public int getIdTransportista() {
		return id_transportista;
	}

	public void setIdTransportista(int IdTransportista) {
		this.id_transportista = IdTransportista;
	}

	public float getVolumenTotalObservado() {
		return volumen_total_observado;
	}

	public void setVolumenTotalObservado(float VolumenTotalObservado) {
		this.volumen_total_observado = VolumenTotalObservado;
	}

	public float getVolumenTotalCorregido() {
		return volumen_total_corregido;
	}

	public void setVolumenTotalCorregido(float VolumenTotalCorregido) {
		this.volumen_total_corregido = VolumenTotalCorregido;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int Estado) {
		this.estado = Estado;
	}

	public long getSincronizadoEl() {
		return sincronizado_el;
	}

	public void setSincronizadoEl(long SincronizadoEl) {
		this.sincronizado_el = SincronizadoEl;
	}

	/**
	 * @return the peso_bruto
	 */
	public float getPesoBruto() {
		return peso_bruto;
	}

	/**
	 * @param peso_bruto
	 *            the peso_bruto to set
	 */
	public void setPesoBruto(float pesoBruto) {
		this.peso_bruto = pesoBruto;
	}

	/**
	 * @return the peso_tara
	 */
	public float getPesoTara() {
		return peso_tara;
	}

	/**
	 * @param peso_tara
	 *            the peso_tara to set
	 */
	public void setPesoTara(float pesoTara) {
		this.peso_tara = pesoTara;
	}

	/**
	 * @return the peso_neto
	 */
	public float getPesoNeto() {
		return peso_neto;
	}

	/**
	 * @param peso_neto
	 *            the peso_neto to set
	 */
	public void setPesoNeto(float pesoNeto) {
		this.peso_neto = pesoNeto;
	}

	/**
	 * @return the plantaDespacho
	 */
	public Planta getPlantaDespacho() {
		return plantaDespacho;
	}

	/**
	 * @param plantaDespacho
	 *            the plantaDespacho to set
	 */
	public void setPlantaDespacho(Planta plantaDespacho) {
		this.plantaDespacho = plantaDespacho;
	}

	/**
	 * @return the plantaRecepcion
	 */
	public Operacion getPlantaRecepcion() {
		return plantaRecepcion;
	}

	/**
	 * @param plantaRecepcion
	 *            the plantaRecepcion to set
	 */
	public void setPlantaRecepcion(Operacion plantaRecepcion) {
		this.plantaRecepcion = plantaRecepcion;
	}

	/**
	 * @return the conductor
	 */
	public Conductor getConductor() {
		return conductor;
	}

	/**
	 * @param conductor
	 *            the conductor to set
	 */
	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	/**
	 * @return the transportista
	 */
	public Transportista getTransportista() {
		return transportista;
	}

	/**
	 * @param transportista
	 *            the transportista to set
	 */
	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}

	/**
	 * @return the detalles
	 */
	public List<DetalleTransporte> getDetalles() {
		return detalles;
	}

	/**
	 * @param detalles
	 *            the detalles to set
	 */
	public void setDetalles(List<DetalleTransporte> detalles) {
		this.detalles = detalles;
	}

	/**
	 * @return the eventos
	 */
	public List<Evento> getEventos() {
		return eventos;
	}

	/**
	 * @param eventos the eventos to set
	 */
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}


	public Respuesta validarTransporte(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
			
		  if (!Utilidades.esValidoForingKey(this.id_planta_despacho)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Planta Despacho" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_transportista)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Transportista" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_cisterna)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Cisterna" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValidoForingKey(this.id_conductor)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Conductor" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.fecha_emision)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Fecha Emisión" }, locale);
			return respuesta;
		  }

		  if (this.numero_guia_remision.length() > MAXIMA_LONGITUD_GUIA_REMISION){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Guía de Remisión", MAXIMA_LONGITUD_GUIA_REMISION }, locale);
			return respuesta;
		  }
		  if (this.numero_orden_entrega.length() > MAXIMA_LONGITUD_ORDEN_COMPRA){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Orden de Entrega", MAXIMA_LONGITUD_ORDEN_COMPRA }, locale);
			return respuesta;
		  }
		  if (this.numero_factura.length() > MAXIMA_LONGITUD_ORDEN_COMPRA){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Factura", MAXIMA_LONGITUD_FACTURA }, locale);
			return respuesta;
		  }
		  if (this.codigo_scop.length() > MAXIMA_LONGITUD_SCOP){
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresEntidad", new Object[] { "Código SCOP", MAXIMA_LONGITUD_SCOP }, locale);
			return respuesta;
		  }
		  if (!Utilidades.validarFecha(this.fecha_emision.toString(), Constante.FORMATO_FECHA_ESTANDAR)) {
		    respuesta.estado = false;
		    respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresIncorrectosEntidad", new Object[] { "Fecha de Emisión" }, locale);
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
	public String getCadenaTransporte() {
		String cadena="";
		if(!Utilidades.esValido(this.getNumeroGuiaRemision())){ this.setNumeroGuiaRemision(""); };
		if(!Utilidades.esValido(this.getNumeroOrdenCompra())){ this.setNumeroOrdenCompra(""); };
		if(!Utilidades.esValido(this.getNumeroFactura())){ this.setNumeroFactura(""); };
		if(!Utilidades.esValido(this.getCodigoScop())){ this.setCodigoScop(""); };
		if(!Utilidades.esValido(this.getPrecintosSeguridad())){ this.setPrecintosSeguridad(""); };
		if(!Utilidades.esValido(this.getFechaEmisionGuia())){ this.setFechaEmisionGuia(new Date(0)); };
		
		cadena = this.getNumeroGuiaRemision() + 
				 this.getNumeroOrdenCompra() +
				 this.getNumeroFactura() +
				 this.getCodigoScop() +
				 this.getPrecintosSeguridad() +
				 this.getFechaEmisionGuia().toString() ; 

		return cadena;
	}	
	
	//validaciones para el pesaje
	public Respuesta validarPesajeTransporte(MessageSource gestorDiccionario, Locale locale){
	  Respuesta respuesta = new Respuesta();
		try {
		  if (!Utilidades.esValido(this.peso_bruto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Peso Bruto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.peso_neto)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Peso Neto" }, locale);
			return respuesta;
		  }
		  if (!Utilidades.esValido(this.peso_tara)){	
			respuesta.estado = false;
			respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Peso Tara" }, locale);
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
	public String getCadenaPesaje() {
		String cadena="";
		if(!Utilidades.esValido(this.getPesoBruto())){ this.setPesoBruto(0); };
		if(!Utilidades.esValido(this.getPesoNeto())){ this.setPesoNeto(0); };
		if(!Utilidades.esValido(this.getPesoTara())){ this.setPesoTara(0); };
		
		cadena = Float.toString(this.getPesoBruto()) + 
				 Float.toString(this.getPesoNeto()) +
				 Float.toString(this.getPesoTara()) ; 

		return cadena;
	}	
	
	
}
