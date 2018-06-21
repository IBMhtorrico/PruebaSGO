package sgo.entidad;

import java.sql.Timestamp;

import sgo.utilidades.Utilidades;

public class Despacho extends EntidadBase {
	private int id_despacho;
	private int id_jornada;
	private int id_vehiculo;
	private float kilometro_horometro;
	private String numero_vale;
	private int tipo_registro;
	private Timestamp fecha_hora_inicio;
	private Timestamp fecha_hora_fin;
	
	private String clasificacion;
	private int id_producto;
	private float lectura_inicial;
	private float lectura_final;
	private float factor_correccion;
	private float api_corregido;
	private float temperatura;
	private float volumen_corregido;
	private int id_tanque;
	private int id_contometro;
	private int codigo_archivo_origen;
	private float volumen_observado;
	private int estado;
	private Jornada jornada;
	private Producto producto;
	private Vehiculo vehiculo;
	private Tanque tanque;
	private Contometro contometro;
	
	public final static int ESTADO_ACTIVO=1;
	public final static int ESTADO_INACTIVO=2;
	public final static int ESTADO_ASIGNADO=3;
	public final static int ESTADO_DESCARGADO=4;
	public final static int ESTADO_ANULADO=5;
	
	public final static int ORIGEN_MANUAL=1;
	public final static int ORIGEN_FICHERO=2;
	public final static int ORIGEN_MIXTO=3;
	
	public final static int CLASIFICACION_TRANSFERIDO=1;
	public final static int CLASIFICACION_RECIRCULADO=2;
	
	public final static String CLASIFICACION_TRANSFERIDO_TEXT="TRANSFERIDO";
	public final static String CLASIFICACION_RECIRCULADO_TEXT="RECIRCULADO";
	
	
	public int getId() {
		return id_despacho;
	}
	
	public void setId(int id) {
		this.id_despacho = id;
	}

	public int getIdJornada() {
		return id_jornada;
	}

	public void setIdJornada(int idJornada) {
		this.id_jornada = idJornada;
	}

	public int getIdVehiculo() {
		return id_vehiculo;
	}

	public void setIdVehiculo(int idVehiculo) {
		this.id_vehiculo = idVehiculo;
	}

	public float getKilometroHorometro() {
		return kilometro_horometro;
	}

	public void setKilometroHorometro(float kilometroHorometro) {
		this.kilometro_horometro = kilometroHorometro;
	}

	public String getNumeroVale() {
		return numero_vale;
	}

	public void setNumeroVale(String numeroVale) {
		this.numero_vale = numeroVale;
	}

	public int getTipoRegistro() {
		return tipo_registro;
	}

	public void setTipoRegistro(int tipoRegistro) {
		this.tipo_registro = tipoRegistro;
	}

	public Timestamp getFechaHoraInicio() {
		return fecha_hora_inicio;
	}

	public void setFechaHoraInicio(Timestamp fechaHoraInicio) {
		this.fecha_hora_inicio = fechaHoraInicio;
	}

	public Timestamp getFechaHoraFin() {
		return fecha_hora_fin;
	}

	public void setFechaHoraFin(Timestamp fechaHoraFin) {
		this.fecha_hora_fin = fechaHoraFin;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public int getIdProducto() {
		return id_producto;
	}

	public void setIdProducto(int idProducto) {
		this.id_producto = idProducto;
	}

	public float getLecturaInicial() {
		return lectura_inicial;
	}

	public void setLecturaInicial(float lecturaInicial) {
		this.lectura_inicial = lecturaInicial;
	}

	public float getLecturaFinal() {
		return lectura_final;
	}

	public void setLecturaFinal(float lecturaFinal) {
		this.lectura_final = lecturaFinal;
	}

	public float getFactorCorreccion() {
		return factor_correccion;
	}

	public void setFactorCorreccion(float factorCorreccion) {
		this.factor_correccion = factorCorreccion;
	}

	public float getApiCorregido() {
		return api_corregido;
	}

	public void setApiCorregido(float apiCorregido) {
		this.api_corregido = apiCorregido;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public float getVolumenCorregido() {
		return volumen_corregido;
	}

	public void setVolumenCorregido(float volumenCorregido) {
		this.volumen_corregido = volumenCorregido;
	}

	public int getIdTanque() {
		return id_tanque;
	}

	public void setIdTanque(int idTanque) {
		this.id_tanque = idTanque;
	}

	public int getIdContometro() {
		return id_contometro;
	}

	public void setIdContometro(int idContometro) {
		this.id_contometro = idContometro;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Jornada getJornada() {
		return jornada;
	}

	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Tanque getTanque() {
		return tanque;
	}

	public void setTanque(Tanque tanque) {
		this.tanque = tanque;
	}

	public Contometro getContometro() {
		return contometro;
	}

	public void setContometro(Contometro contometro) {
		this.contometro = contometro;
	}

	public int getCodigoArchivoOrigen() {
		return codigo_archivo_origen;
	}

	public void setCodigoArchivoOrigen(int codigoArchivoOrigen) {
		this.codigo_archivo_origen = codigoArchivoOrigen;
	}
	
	public String getOrigen(){
		if(Utilidades.esValido(this.codigo_archivo_origen)){
			return ("FICHERO");
		} else {
			return ("MANUAL");
		}
	}

	public float getVolumenObservado() {
		return volumen_observado;
	}

	public void setVolumenObservado(float volumenObservado) {
		this.volumen_observado = volumenObservado;
	}

}