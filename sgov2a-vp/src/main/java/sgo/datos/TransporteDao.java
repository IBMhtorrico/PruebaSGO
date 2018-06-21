package sgo.datos;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sgo.entidad.BusquedaDetalleProgramado;
import sgo.entidad.BusquedaProgramado;
import sgo.entidad.Cliente;
import sgo.entidad.Conductor;
import sgo.entidad.Contenido;
import sgo.entidad.EtapaTransporte;
import sgo.entidad.OperacionEtapaRuta;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planta;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Transporte;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class TransporteDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "transporte";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_transporte";
	public static final String NOMBRE_VISTA_ASIGNADOS_DIA = Constante.ESQUEMA_APLICACION + "v_transporte_asignados_dia";
	public final static String NOMBRE_CAMPO_CLAVE = "id_transporte";
	public final static String CAMPO_PLACA_CISTERNA ="placa_cisterna";
	public final static String CAMPO_FECHA_OPERATIVA ="fecha_operativa";
	public final static String CAMPO_FECHA_EMISION ="fecha_emision";
	public final static String CAMPO_ESTADO_TRANPORTE="estado_transporte";
	public final static String CAMPO_OPERACION = "id_operacion";

	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}

	public String mapearCampoOrdenamiento(String propiedad){
		String campoOrdenamiento="";
		try {
			if (propiedad.equals("id")){
				campoOrdenamiento="id_transporte";
			}
			if (propiedad.equals("numeroGuiaRemision")){
				campoOrdenamiento="numero_guia_remision";
			}
			if (propiedad.equals("numeroOrdenCompra")){
				campoOrdenamiento="numero_orden_entrega";
			}
			if (propiedad.equals("numeroFactura")){
				campoOrdenamiento="numero_factura";
			}
			if (propiedad.equals("codigoScop")){
				campoOrdenamiento="codigo_scop";
			}
			if (propiedad.equals("fechaEmisionGuia")){
				campoOrdenamiento="fecha_emision";
			}
			if (propiedad.equals("plantaDespacho")){
				campoOrdenamiento="planta_despacho";
			}
			if (propiedad.equals("plantaRecepcion")){
				campoOrdenamiento="planta_recepcion";
			}
			if (propiedad.equals("idCliente")){
				campoOrdenamiento="id_cliente";
			}
			if (propiedad.equals("idConductor")){
				campoOrdenamiento="id_conductor";
			}
			if (propiedad.equals("breveteConductor")){
				campoOrdenamiento="brevete_conductor";
			}
			if (propiedad.equals("idCisterna")){
				campoOrdenamiento="id_cisterna";
			}
			if (propiedad.equals("placaCisterna")){
				campoOrdenamiento="placa_cisterna";
			}
			if (propiedad.equals("tarjetaCubicacionCompartimento")){
				campoOrdenamiento="tarjeta_cubicacion_cisterna";
			}
			if (propiedad.equals("idTracto")){
				campoOrdenamiento="id_tracto";
			}
			if (propiedad.equals("placaTracto")){
				campoOrdenamiento="placa_tracto";
			}
			if (propiedad.equals("idTransportista")){
				campoOrdenamiento="id_transportista";
			}
			if (propiedad.equals("volumenTotalObservado")){
				campoOrdenamiento="volumen_total_observado";
			}
			if (propiedad.equals("volumenTotalCorregido")){
				campoOrdenamiento="volumen_total_corregido";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
			}
			
			//Campos de auditoria
		}catch(Exception excepcion){
			
		}
		return campoOrdenamiento;
	}
	
	//Agregado por req 9000002570====================
		public RespuestaCompuesta recuperarTiempoEtapas(int idTransporte, int idOperacion){
			StringBuilder consultaSQL= new StringBuilder();	
			List<Transporte> listaRegistros=new ArrayList<Transporte>();
			Contenido<Transporte> contenido = new Contenido<Transporte>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			Transporte eTransporte = null;
			Conductor eConductor= null;
			Planta ePlanta = null;
			try {
				
				consultaSQL.append("SELECT  t1.planta_despacho, t1.id_transporte, t6.id_operacion_etapa_ruta, ");
				consultaSQL.append("	t3.placa cisterna, t4.placa tracto, t1.numero_guia_remision, t1.fecha_emision, ");
				consultaSQL.append("	t5.nombres, t5.apellidos, t2.descripcion, ");
				consultaSQL.append("	t6.id_etapa_transporte, t7.nombre_etapa, t6.fecha_inicio_etapa, t6.fecha_fin_etapa, t6.tiempo_etapa, t6.observacion_etapa ");
				consultaSQL.append("FROM sgo.transporte t1 ");
				consultaSQL.append("INNER JOIN sgo.planta t2 on (t1.planta_despacho = t2.id_planta) ");
				consultaSQL.append("INNER JOIN sgo.cisterna t3 on (t1.id_cisterna = t3.id_cisterna) ");
				consultaSQL.append("INNER JOIN sgo.tracto t4 on (t1.id_tracto = t4.id_tracto) ");
				consultaSQL.append("INNER JOIN sgo.conductor t5 on (t1.id_conductor = t5.id_conductor) ");
				consultaSQL.append("LEFT  JOIN sgo.etapa_transporte t6 on (t1.id_transporte = t6.id_transporte) ");
				consultaSQL.append("LEFT  JOIN sgo.operacion_etapa_ruta t7 on (t6.id_operacion_etapa_ruta = t7.id_operacion_etapa_ruta) ");
				consultaSQL.append("WHERE t1.id_transporte = " + idTransporte + " ");
				
				List<Map<String,Object>> mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
				Map<String, Object> map = mapRegistros.get(0);
				
				eTransporte = new Transporte();
				eTransporte.setId((Integer) map.get("id_transporte"));
				
				eTransporte.setPlacaCisterna((String) map.get("cisterna"));
				eTransporte.setPlacaTracto((String) map.get("tracto"));
				
				eTransporte.setNumeroGuiaRemision((String) map.get("numero_guia_remision"));
				eTransporte.setFechaEmisionGuia((Date) map.get("fecha_emision"));
				
				eConductor = new Conductor();
				eConductor.setNombres((String) map.get("nombres"));
				eConductor.setApellidos((String) map.get("apellidos"));
				
				eTransporte.setConductor(eConductor);
				
				ePlanta = new Planta();
				ePlanta.setDescripcion((String) map.get("descripcion"));
				eTransporte.setPlantaDespacho(ePlanta);
				
				eTransporte.setEtapasTransporte(null);
				
				Planta plantaOperacion = obtenerPlantaOperacion(idOperacion);
				Integer idPlantaCisterna = (Integer) map.get("planta_despacho");
				
				EtapaTransporte etapa;
				ArrayList<EtapaTransporte> etapas = new ArrayList<EtapaTransporte>();
				Integer tiempos = (Integer) map.get("id_etapa_transporte");
				if(tiempos != null && tiempos > 0){
					
					for(Map<String,Object> obj : mapRegistros){
						etapa = new EtapaTransporte();
						etapa.setId((Integer) obj.get("id_etapa_transporte"));
						etapa.setIdOperacionEtapaRuta((Integer) obj.get("id_operacion_etapa_ruta"));
						etapa.setNombreEtapa((String) obj.get("nombre_etapa"));
						etapa.setFechaInicio((Timestamp) obj.get("fecha_inicio_etapa"));
						etapa.setFechaFin((Timestamp) obj.get("fecha_fin_etapa"));
						etapa.setTiempoEtapa((Integer) obj.get("tiempo_etapa"));
						etapa.setObservacion((String) obj.get("observacion_etapa"));
						
						etapas.add(etapa);
					}
					
					eTransporte.setEtapasTransporte(etapas);
				}else{
					
					ArrayList<OperacionEtapaRuta> lstOer = obtenerOperEtapRutaPorIdOper(idOperacion);
					
					if(lstOer != null){
						
						for(OperacionEtapaRuta obj : lstOer){
							etapa = new EtapaTransporte();
							etapa.setId(0);
							etapa.setIdOperacionEtapaRuta(obj.getId());
							etapa.setNombreEtapa(obj.getNombreEtapa());
							etapa.setFechaInicio(new Timestamp(System.currentTimeMillis()));
							etapa.setFechaFin(new Timestamp(System.currentTimeMillis()));
							etapa.setTiempoEtapa(0);
							etapa.setObservacion("");
							
							etapas.add(etapa);
						}
						eTransporte.setEtapasTransporte(etapas);
					}
				}
				
				listaRegistros.add(eTransporte);
				contenido.totalRegistros=listaRegistros.size();
				contenido.totalEncontrados=listaRegistros.size();
				contenido.carga= listaRegistros;
				respuesta.mensaje="OK";
				respuesta.estado=true;
				respuesta.contenido = contenido;	
				
				if(plantaOperacion == null){
					respuesta.mensaje = "El transporte no tiene planta asociada";
				}else{
					Integer idPlantaOperacion = plantaOperacion.getId();
					
					if(idPlantaOperacion.intValue() != idPlantaCisterna.intValue()){
						respuesta.mensaje = "Cisterna no ha partido de planta " + plantaOperacion.getDescripcion() + " (especificada en el mantenimiento de operaciones), por lo tanto, las etapas definidas aquí no son válidas para este vehículo";
					}
				}
				
				
			    System.out.println("recuperarEtapas: OperacionDao: " + consultaSQL.toString());
			} catch (DataAccessException excepcionAccesoDatos) {
				excepcionAccesoDatos.printStackTrace();
				respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
				respuesta.estado=false;
				respuesta.contenido=null;
			}
			return respuesta;
		}
		
		private ArrayList<OperacionEtapaRuta> obtenerOperEtapRutaPorIdOper(int idOperacion){
			
			StringBuilder consultaSQL= new StringBuilder();	
			OperacionEtapaRuta etapa;
			ArrayList<OperacionEtapaRuta> valorRet = new ArrayList<OperacionEtapaRuta>();
			
			consultaSQL.append("SELECT nombre_etapa, id_operacion_etapa_ruta 	");
			consultaSQL.append("FROM sgo.operacion_etapa_ruta ");
			consultaSQL.append("WHERE id_operacion = " + idOperacion + " AND estado = 1");
			consultaSQL.append("ORDER BY orden 	");
			
			List<Map<String,Object>> mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
			
			if(mapRegistros != null){
				for(Map<String,Object> obj : mapRegistros){
					etapa = new OperacionEtapaRuta();
					etapa.setId((Integer) obj.get("id_operacion_etapa_ruta"));
					etapa.setNombreEtapa((String) obj.get("nombre_etapa"));
					valorRet.add(etapa);
				}
			}
			
			return valorRet;
		}
		
		private Planta obtenerPlantaOperacion(int idOperacion){
			
			StringBuilder consultaSQL= new StringBuilder();	
			
			consultaSQL.append("SELECT t1.planta_despacho_defecto, t2.descripcion ");
			consultaSQL.append("FROM sgo.operacion t1 ");
			consultaSQL.append("INNER JOIN sgo.planta t2 on (t1.planta_despacho_defecto = t2.id_planta) ");
			consultaSQL.append("WHERE id_operacion = " + idOperacion);
			
			Planta planta = null;
			
			try{
				
				List<Map<String,Object>> mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
				
				if(mapRegistros != null && mapRegistros.size() > 0){
					Map<String,Object> map = mapRegistros.get(0);
					planta = new Planta();
					planta.setId((Integer) map.get("planta_despacho_defecto"));
					planta.setDescripcion((String) map.get("descripcion"));
				}	
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			return planta;
		}
		//==========================================================================================0

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Transporte> contenido = new Contenido<Transporte>();
		List<Transporte> listaRegistros = new ArrayList<Transporte>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_transporte, ");
			consultaSQL.append("t1.numero_guia_remision, ");
			consultaSQL.append("t1.planta_despacho, ");
			consultaSQL.append("t1.descripcionPlantaDespacho, ");
			consultaSQL.append("t1.cliente_transporte, ");
			consultaSQL.append("t1.id_conductor, ");
			consultaSQL.append("t1.apellidosconductor, ");
			consultaSQL.append("t1.nombresconductor, ");
			consultaSQL.append("t1.fecha_emision, ");
			consultaSQL.append("t1.id_cisterna, ");
			consultaSQL.append("t1.placa_cisterna, ");
			consultaSQL.append("t1.volumen_total_observado, ");
			consultaSQL.append("t1.volumen_total_corregido, ");
			consultaSQL.append("t1.id_transportista, ");
			consultaSQL.append("t1.razonsocialtransportista, ");
			consultaSQL.append("t1.brevete_conductor, ");
			consultaSQL.append("t1.numero_orden_entrega, ");
			consultaSQL.append("t1.codigo_scop, ");
			consultaSQL.append("t1.estado_transporte, ");
			//consultaSQL.append("t1.sincronizado_el, ");
			consultaSQL.append("t1.numero_factura, ");
			consultaSQL.append("t1.planta_recepcion, ");
			consultaSQL.append("t1.nombre_planta_recepcion, ");
			consultaSQL.append("t1.alias_planta_recepcion, ");
			//consultaSQL.append("t1.descripcionPlantaRecepcion, ");
			consultaSQL.append("t1.id_tracto, ");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
			consultaSQL.append("t1.cisterna_tracto,");
			consultaSQL.append("t1.precintos_seguridad,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.peso_bruto,");
			consultaSQL.append("t1.peso_tara,");
			consultaSQL.append("t1.peso_neto,");
			//Campos de auditoria 
			consultaSQL.append("t1.creado_el_transporte, ");
			consultaSQL.append("t1.creado_por_transporte, ");
			consultaSQL.append("t1.usuario_creacion_transporte, ");
			consultaSQL.append("t1.actualizado_por_transporte, ");
			consultaSQL.append("t1.usuario_actualizacion_transporte, ");
			consultaSQL.append("t1.actualizado_el_transporte, ");
			consultaSQL.append("t1.ip_creacion_transporte,");
			consultaSQL.append("t1.ip_actualizacion_transporte ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new TransporteMapper());
			totalEncontrados =totalRegistros;
			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalEncontrados;
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error=  Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido=null;
		} catch (Exception excepcionGenerica) {
			excepcionGenerica.printStackTrace();
			respuesta.error= Constante.EXCEPCION_GENERICA;
			respuesta.contenido=null;
			respuesta.estado = false;
		}
		return respuesta;
	}
	
	/**
	 * Metodo para recuperar trnsportes por su identificador.
	 * @param diasOperativos      Identificador del transporte.
	 * @return respuesta		  Resgitro Transporte.
	 */
	public RespuestaCompuesta recuperarRegistro(int ID){
		StringBuilder consultaSQL= new StringBuilder();		
		List<Transporte> listaRegistros=new ArrayList<Transporte>();
		Contenido<Transporte> contenido = new Contenido<Transporte>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_transporte, ");
			consultaSQL.append("t1.numero_guia_remision, ");
			consultaSQL.append("t1.planta_despacho, ");
			consultaSQL.append("t1.descripcionPlantaDespacho, ");
			consultaSQL.append("t1.cliente_transporte, ");
			consultaSQL.append("t1.id_conductor, ");
			consultaSQL.append("t1.apellidosconductor, ");
			consultaSQL.append("t1.nombresconductor, ");
			consultaSQL.append("t1.fecha_emision, ");
			consultaSQL.append("t1.id_cisterna, ");
			consultaSQL.append("t1.placa_cisterna, ");
			consultaSQL.append("t1.volumen_total_observado, ");
			consultaSQL.append("t1.volumen_total_corregido, ");
			consultaSQL.append("t1.id_transportista, ");
			consultaSQL.append("t1.razonsocialtransportista, ");
			consultaSQL.append("t1.brevete_conductor, ");
			consultaSQL.append("t1.numero_orden_entrega, ");
			consultaSQL.append("t1.codigo_scop, ");
			consultaSQL.append("t1.estado_transporte, ");
			//consultaSQL.append("t1.sincronizado_el, ");
			consultaSQL.append("t1.numero_factura, ");
			consultaSQL.append("t1.planta_recepcion, ");
			consultaSQL.append("t1.nombre_planta_recepcion, ");
			consultaSQL.append("t1.alias_planta_recepcion, ");
			//consultaSQL.append("t1.descripcionPlantaRecepcion, ");
			consultaSQL.append("t1.id_tracto, ");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
			consultaSQL.append("t1.cisterna_tracto,");
			consultaSQL.append("t1.precintos_seguridad,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.peso_bruto,");
			consultaSQL.append("t1.peso_tara,");
			consultaSQL.append("t1.peso_neto,");
			//Campos de auditoria 
			consultaSQL.append("t1.creado_el_transporte, ");
			consultaSQL.append("t1.creado_por_transporte, ");
			consultaSQL.append("t1.usuario_creacion_transporte, ");
			consultaSQL.append("t1.actualizado_por_transporte, ");
			consultaSQL.append("t1.usuario_actualizacion_transporte, ");
			consultaSQL.append("t1.actualizado_el_transporte, ");
			consultaSQL.append("t1.ip_creacion_transporte,");
			consultaSQL.append("t1.ip_actualizacion_transporte ");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new TransporteMapper());
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;			
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}

	 public RespuestaCompuesta recuperarRegistroxGuia(String numeroGuiaRemision){
    StringBuilder consultaSQL= new StringBuilder();   
    List<Transporte> listaRegistros=new ArrayList<Transporte>();
    Contenido<Transporte> contenido = new Contenido<Transporte>();
    RespuestaCompuesta respuesta= new RespuestaCompuesta();
    try {
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_transporte, ");
      consultaSQL.append("t1.numero_guia_remision, ");
      consultaSQL.append("t1.planta_despacho, ");
      consultaSQL.append("t1.descripcionPlantaDespacho, ");
      consultaSQL.append("t1.cliente_transporte, ");
      consultaSQL.append("t1.id_conductor, ");
      consultaSQL.append("t1.apellidosconductor, ");
      consultaSQL.append("t1.nombresconductor, ");
      consultaSQL.append("t1.fecha_emision, ");
      consultaSQL.append("t1.id_cisterna, ");
      consultaSQL.append("t1.placa_cisterna, ");
      consultaSQL.append("t1.volumen_total_observado, ");
      consultaSQL.append("t1.volumen_total_corregido, ");
      consultaSQL.append("t1.id_transportista, ");
      consultaSQL.append("t1.razonsocialtransportista, ");
      consultaSQL.append("t1.brevete_conductor, ");
      consultaSQL.append("t1.numero_orden_entrega, ");
      consultaSQL.append("t1.codigo_scop, ");
      consultaSQL.append("t1.estado_transporte, ");
      //consultaSQL.append("t1.sincronizado_el, ");
      consultaSQL.append("t1.numero_factura, ");
      consultaSQL.append("t1.planta_recepcion, ");
      consultaSQL.append("t1.nombre_planta_recepcion, ");
      consultaSQL.append("t1.alias_planta_recepcion, ");
      //consultaSQL.append("t1.descripcionPlantaRecepcion, ");
      consultaSQL.append("t1.id_tracto, ");
      consultaSQL.append("t1.placa_tracto, ");
      consultaSQL.append("t1.cantidad_compartimentos, ");
      consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
      consultaSQL.append("t1.cisterna_tracto,");
      consultaSQL.append("t1.precintos_seguridad,");
      consultaSQL.append("t1.tipo_registro,");
      consultaSQL.append("t1.peso_bruto,");
      consultaSQL.append("t1.peso_tara,");
      consultaSQL.append("t1.peso_neto,");
      //Campos de auditoria 
      consultaSQL.append("t1.creado_el_transporte, ");
      consultaSQL.append("t1.creado_por_transporte, ");
      consultaSQL.append("t1.usuario_creacion_transporte, ");
      consultaSQL.append("t1.actualizado_por_transporte, ");
      consultaSQL.append("t1.usuario_actualizacion_transporte, ");
      consultaSQL.append("t1.actualizado_el_transporte, ");
      consultaSQL.append("t1.ip_creacion_transporte,");
      consultaSQL.append("t1.ip_actualizacion_transporte ");
      consultaSQL.append(" FROM ");       
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 WHERE numero_guia_remision =? ");
      listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {numeroGuiaRemision},new TransporteMapper());
      contenido.totalRegistros=listaRegistros.size();
      contenido.totalEncontrados=listaRegistros.size();
      contenido.carga= listaRegistros;
      respuesta.mensaje="OK";
      respuesta.estado=true;
      respuesta.contenido = contenido;      
    } catch (DataAccessException excepcionAccesoDatos) {
      excepcionAccesoDatos.printStackTrace();
      respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado=false;
      respuesta.contenido=null;
    }
    return respuesta;
  }
	
	  public RespuestaCompuesta recuperarTransporteProgramado(int idCisterna, int idConductor, int idOperacion){
     StringBuilder consultaSQL= new StringBuilder();   
     List<BusquedaProgramado> listaRegistros=new ArrayList<BusquedaProgramado>();
     Contenido<BusquedaProgramado> contenido = new Contenido<BusquedaProgramado>();
     RespuestaCompuesta respuesta= new RespuestaCompuesta();
     try {
       consultaSQL.append("SELECT ");
       consultaSQL.append("t1.id_doperativo,");
       consultaSQL.append("t1.id_operacion,");
       consultaSQL.append("t1.fecha_operativa,");
       consultaSQL.append("t1.id_transportista,");
       consultaSQL.append("t1.id_conductor,");
       consultaSQL.append("t1.programacion,");
       consultaSQL.append("t1.id_transporte,");
       consultaSQL.append("t1.id_cisterna,");
       consultaSQL.append("t1.estado");
       consultaSQL.append(" FROM sgo.v_busqueda_programados_completo ");       
       consultaSQL.append(" t1 ");
       consultaSQL.append(" WHERE id_cisterna='"+idCisterna+"' AND id_conductor='"+idConductor+"' AND id_operacion='"+idOperacion+"' ");
       listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new BusquedaProgramadoMapper());
       contenido.totalRegistros=listaRegistros.size();
       contenido.totalEncontrados=listaRegistros.size();
       contenido.carga= listaRegistros;
       respuesta.mensaje="OK";
       respuesta.estado=true;
       respuesta.contenido = contenido;      
     } catch (DataAccessException excepcionAccesoDatos) {
       excepcionAccesoDatos.printStackTrace();
       respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
       respuesta.estado=false;
       respuesta.contenido=null;
     }
     return respuesta;
   }
	
	//Buscar las planificacione sprogramadas por cisterna, conductor, operacion y dia operativo.
  //KANB
  public RespuestaCompuesta recuperarTransporteProgramado(int idCisterna, int idConductor, int idOperacion, int idDIaperativo , int idTracto){
	     StringBuilder consultaSQL= new StringBuilder();   
	     List<BusquedaProgramado> listaRegistros=new ArrayList<BusquedaProgramado>();
	     Contenido<BusquedaProgramado> contenido = new Contenido<BusquedaProgramado>();
	     RespuestaCompuesta respuesta= new RespuestaCompuesta();
	     try {
	       consultaSQL.append("SELECT ");
	       consultaSQL.append("t1.id_doperativo,");
	       consultaSQL.append("t1.id_operacion,");
	       consultaSQL.append("t1.fecha_operativa,");
	       consultaSQL.append("t1.id_transportista,");
	       consultaSQL.append("t1.id_conductor,");
	       consultaSQL.append("t1.programacion,");
	       consultaSQL.append("t1.id_transporte,");
	       consultaSQL.append("t1.id_cisterna,");
	       consultaSQL.append("t1.estado");
	       consultaSQL.append(",t1.id_tracto");
	       consultaSQL.append(" FROM sgo.v_busqueda_programados_completo ");       
	       consultaSQL.append(" t1 ");
	       consultaSQL.append(" WHERE id_cisterna ='"+ idCisterna +"' AND id_conductor ='"+ idConductor +"' AND id_operacion ='"+ idOperacion +"' AND id_doperativo ='"+ idDIaperativo +"' " +" AND id_tracto= '"+idTracto+"' ");
	       listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new BusquedaProgramadoMapper());
	       contenido.totalRegistros=listaRegistros.size();
	       contenido.totalEncontrados=listaRegistros.size();
	       contenido.carga= listaRegistros;
	       respuesta.mensaje="OK";
	       respuesta.estado=true;
	       respuesta.contenido = contenido;    
	       Utilidades.gestionaInfo("guardarSAP: TransporteDao: recuperarTransporteProgramado ", "listaMaestros.getIdDiaOperativo() ", consultaSQL.toString());
	       System.out.println("guardarSAP: TransporteDao: recuperarTransporteProgramado: listaMaestros.getIdDiaOperativo() " + consultaSQL.toString());
	     } catch (DataAccessException excepcionAccesoDatos) {
	       excepcionAccesoDatos.printStackTrace();
	       respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
	       respuesta.estado=false;
	       respuesta.contenido=null;
	     }
	     return respuesta;
	   }
  
//Agregado por 9000002608==================================================================================================
	//Buscar las planificaciones programadas por idTransporte
  //KANB
  public RespuestaCompuesta recuperarDetalleTransporteProgramado(int idTransporte){
	     StringBuilder consultaSQL= new StringBuilder();   
	     List<BusquedaDetalleProgramado> listaRegistros=new ArrayList<BusquedaDetalleProgramado>();
	     Contenido<BusquedaDetalleProgramado> contenido = new Contenido<BusquedaDetalleProgramado>();
	     RespuestaCompuesta respuesta= new RespuestaCompuesta();
	     try {
	       consultaSQL.append("SELECT ");
	       consultaSQL.append("t1.numero_compartimento,");
	       consultaSQL.append("t1.id_producto, ");
	       consultaSQL.append("t2.codigo_referencia ");
	       consultaSQL.append(" FROM sgo.detalle_transporte t1 ");   
	       consultaSQL.append(" INNER JOIN sgo.producto t2 on (t1.id_producto = t2.id_producto) ");
	       consultaSQL.append(" WHERE id_transporte = " + idTransporte);
	       listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new BusquedaDetalleProgramadoMapper());
	       contenido.totalRegistros=listaRegistros.size();
	       contenido.totalEncontrados=listaRegistros.size();
	       contenido.carga= listaRegistros;
	       respuesta.mensaje="OK";
	       respuesta.estado=true;
	       respuesta.contenido = contenido;    
	       Utilidades.gestionaInfo("guardarSAP: TransporteDao: recuperarDetalleTransporteProgramado ", "listaMaestros.getIdDiaOperativo() ", consultaSQL.toString());
	       System.out.println("guardarSAP: TransporteDao: recuperarDetalleTransporteProgramado: listaMaestros.getIdDiaOperativo() " + consultaSQL.toString());
	     } catch (DataAccessException excepcionAccesoDatos) {
	       excepcionAccesoDatos.printStackTrace();
	       respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
	       respuesta.estado=false;
	       respuesta.contenido=null;
	     }
	     return respuesta;
	   }
//================================================================================================================================
	  
	public String mapearCampoOrdenamientoTransportesAsignados(String propiedad) {
	  String campoOrdenamiento = "";
	  try {
	   if (propiedad.equals("id")) {
	    campoOrdenamiento = "id_transporte";
	   }
	   if (propiedad.equals("numeroGuiaRemision")) {
	    campoOrdenamiento = "numero_guia_remision";
	   }
	   if (propiedad.equals("cisternaTracto")) {
	    campoOrdenamiento = "cisterna_tracto";
	   }
	   if (propiedad.equals("codigoScop")) {
	    campoOrdenamiento = "codigo_scop";
	   }
	   if (propiedad.equals("volumenTotalObservado")) {
	    campoOrdenamiento = "volumen_total_observado";
	   }
	   if (propiedad.equals("volumenTotalCorregido")) {
	    campoOrdenamiento = "volumen_total_corregido";
	   }
	   if (propiedad.equals("origen")) {
	    campoOrdenamiento = "tipo_registro";
	   }
	   if (propiedad.equals("estado")) {
	    campoOrdenamiento = "estado";
	   }
	   // Campos de auditoria
	  } catch (Exception excepcion) {

	  }
	  return campoOrdenamiento;
	 }
	/**
     * Metodo para recuperar trnsportes por su identificador.
     * @param diasOperativos      Identificador del transporte.
     * @return respuesta		  Resgitro Transporte.
     */
	public RespuestaCompuesta recuperaTransportesAsignados(ParametrosListar parametros) {
		String sqlLimit = "";
		  //StringBuilder consultaSQL = new StringBuilder();
		  List<Transporte> listaRegistros = new ArrayList<Transporte>();
		  Contenido<Transporte> contenido = new Contenido<Transporte>();
		  RespuestaCompuesta respuesta = new RespuestaCompuesta();
		  List<String> filtrosWhere = new ArrayList<String>();
		  int totalRegistros = 0, totalEncontrados = 0;
		  String sqlOrderBy = "";
		  String sqlWhere = "";
		  List<Object> valores = new ArrayList<Object>();
		  try {
		  if (parametros.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				valores.add(parametros.getInicioPaginacion());
				valores.add(parametros.getRegistrosPaginaTransporte());
			}
		  StringBuilder consultaSQL = new StringBuilder();
		  consultaSQL.setLength(0);
		  consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_TABLA);
		  totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null,Integer.class);
		  totalEncontrados = totalRegistros;
		  
		  sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(parametros.getCampoOrdenamiento()) + " " + parametros.getSentidoOrdenamiento();
			   
		   if (parametros.getFiltroDiaOperativo() >= 0) {
			 filtrosWhere.add(" t1.id_transporte = t2.id_transporte ");
			 filtrosWhere.add(" t2.id_doperativo = " + parametros.getFiltroDiaOperativo());
		   }
		
		   if (!filtrosWhere.isEmpty()) {
			  consultaSQL.setLength(0);
			  sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
			  consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1, sgo.v_asignacion t2  " + sqlWhere);
			  totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(),null, Integer.class);
		    }	

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_transporte, ");
			consultaSQL.append("t1.numero_guia_remision, ");
			consultaSQL.append("t1.planta_despacho, ");
			consultaSQL.append("t1.descripcionPlantaDespacho, ");
			consultaSQL.append("t1.cliente_transporte, ");
			consultaSQL.append("t1.id_conductor, ");
			consultaSQL.append("t1.apellidosconductor, ");
			consultaSQL.append("t1.nombresconductor, ");
			consultaSQL.append("t1.fecha_emision, ");
			consultaSQL.append("t1.id_cisterna, ");
			consultaSQL.append("t1.placa_cisterna, ");
			consultaSQL.append("t1.volumen_total_observado, ");
			consultaSQL.append("t1.volumen_total_corregido, ");
			consultaSQL.append("t1.id_transportista, ");
			consultaSQL.append("t1.razonsocialtransportista, ");
			consultaSQL.append("t1.brevete_conductor, ");
			consultaSQL.append("t1.numero_orden_entrega, ");
			consultaSQL.append("t1.codigo_scop, ");
			consultaSQL.append("t1.estado_transporte, ");
			consultaSQL.append("t1.numero_factura, ");
			consultaSQL.append("t1.planta_recepcion, ");
			consultaSQL.append("t1.nombre_planta_recepcion, ");
			consultaSQL.append("t1.alias_planta_recepcion, ");
			consultaSQL.append("t1.id_tracto, ");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
			consultaSQL.append("t1.cisterna_tracto,");
			consultaSQL.append("t1.precintos_seguridad,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.peso_bruto,");
			consultaSQL.append("t1.peso_tara,");
			consultaSQL.append("t1.peso_neto,");
			//Campos de auditoria 
			consultaSQL.append("t1.creado_el_transporte, ");
			consultaSQL.append("t1.creado_por_transporte, ");
			consultaSQL.append("t1.usuario_creacion_transporte, ");
			consultaSQL.append("t1.actualizado_por_transporte, ");
			consultaSQL.append("t1.usuario_actualizacion_transporte, ");
			consultaSQL.append("t1.actualizado_el_transporte, ");
			consultaSQL.append("t1.ip_creacion_transporte,");
			consultaSQL.append("t1.ip_actualizacion_transporte ");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1, sgo.v_asignacion t2 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(), valores.toArray(),new TransporteMapper());
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalEncontrados;
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}
	
	
	public RespuestaCompuesta recuperaTransportesDescarga(ParametrosListar argumentosListar){
    StringBuilder consultaSQL= new StringBuilder();   
    List<Transporte> listaRegistros=new ArrayList<Transporte>();
    Contenido<Transporte> contenido = new Contenido<Transporte>();
    RespuestaCompuesta respuesta= new RespuestaCompuesta();
    List<String> filtrosWhere= new ArrayList<String>();
    String sqlWhere ="";
    //List<Object> parametros = new ArrayList<Object>();
    try {
    	
       if (argumentosListar.getIdTransporte()>0) {
    		filtrosWhere.add(" t1.id_transporte ='"+ argumentosListar.getIdTransporte() +"' ");
       }
      if (argumentosListar.getFiltroOperacion()>0) {
       filtrosWhere.add(" t1."+CAMPO_OPERACION+"  ='"+ argumentosListar.getFiltroOperacion() +"' ");
      }
      
      if (argumentosListar.getFiltroEstado()>0) {
       filtrosWhere.add(" t1."+CAMPO_ESTADO_TRANPORTE+"  ="+ argumentosListar.getFiltroEstado() +" ");
      }
      
      if(!filtrosWhere.isEmpty()){
       sqlWhere = " WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
      }
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_transporte,");
      consultaSQL.append("t1.numero_guia_remision, ");
      consultaSQL.append("t1.planta_despacho, ");
      consultaSQL.append("t1.cliente_transporte, ");
      consultaSQL.append("t1.id_conductor, ");
      consultaSQL.append("t1.nombre_planta_recepcion, ");
      consultaSQL.append("t1.alias_planta_recepcion, ");
      consultaSQL.append("t1.apellidosconductor, ");
      consultaSQL.append("t1.nombresconductor, ");
      consultaSQL.append("t1.fecha_emision, ");
      consultaSQL.append("t1.id_cisterna,");
      consultaSQL.append("t1.placa_cisterna, ");
      consultaSQL.append("t1.volumen_total_observado, ");
      consultaSQL.append("t1.volumen_total_corregido, ");
      consultaSQL.append("t1.id_transportista, ");
      consultaSQL.append("t1.razonsocialtransportista, ");
      consultaSQL.append("t1.descripcionplantadespacho, ");
      consultaSQL.append("t1.brevete_conductor, ");
      consultaSQL.append("t1.numero_orden_entrega, ");
      consultaSQL.append("t1.codigo_scop, ");
      consultaSQL.append("t1.estado_transporte, ");
      consultaSQL.append("t1.numero_factura, ");
      consultaSQL.append("t1.fecha_operativa, ");
      consultaSQL.append("t1.planta_recepcion, ");
      consultaSQL.append("t1.id_tracto, ");
      consultaSQL.append("t1.placa_tracto, ");
      //consultaSQL.append("t1.cantidad_compartimentos, ");
      consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
      consultaSQL.append("t1.cisterna_tracto,");
      consultaSQL.append("t1.cantidad_compartimentos,");      
      consultaSQL.append("t1.precintos_seguridad,");
      consultaSQL.append("t1.tipo_registro,");
      consultaSQL.append("t1.peso_bruto,");
      consultaSQL.append("t1.peso_tara,");
      consultaSQL.append("t1.peso_neto,");
      //Campos de auditoria 
      consultaSQL.append("t1.creado_el_transporte, ");
      consultaSQL.append("t1.creado_por_transporte, ");
      consultaSQL.append("t1.usuario_creacion_transporte, ");
      consultaSQL.append("t1.actualizado_por_transporte, ");
      consultaSQL.append("t1.usuario_actualizacion_transporte, ");
      consultaSQL.append("t1.actualizado_el_transporte, ");
      consultaSQL.append("t1.ip_creacion_transporte,");
      consultaSQL.append("t1.ip_actualizacion_transporte ");
      consultaSQL.append(" FROM ");       
      consultaSQL.append(NOMBRE_VISTA_ASIGNADOS_DIA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(sqlWhere);
      System.out.println(consultaSQL.toString());
      listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new TransporteMapper());
      contenido.totalRegistros=listaRegistros.size();
      contenido.totalEncontrados=listaRegistros.size();
      contenido.carga= listaRegistros;
      respuesta.mensaje="OK";
      respuesta.estado=true;
      respuesta.contenido = contenido;      
    } catch (DataAccessException excepcionAccesoDatos) {
      excepcionAccesoDatos.printStackTrace();
      respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado=false;
      respuesta.contenido=null;
    }
    return respuesta;
  }
	 
	
	  public RespuestaCompuesta recuperaTransporteDescarga(int ID){
	    StringBuilder consultaSQL= new StringBuilder();   
	    List<Transporte> listaRegistros=new ArrayList<Transporte>();
	    Contenido<Transporte> contenido = new Contenido<Transporte>();
	    RespuestaCompuesta respuesta= new RespuestaCompuesta();
	    List<String> filtrosWhere= new ArrayList<String>();
	    String sqlWhere ="";
	    //List<Object> parametros = new ArrayList<Object>();
	    try {
	      consultaSQL.setLength(0);
	      consultaSQL.append("SELECT ");
	      consultaSQL.append("t1.id_transporte,");
	      consultaSQL.append("t1.numero_guia_remision, ");
	      consultaSQL.append("t1.planta_despacho, ");
	      consultaSQL.append("t1.cliente_transporte, ");
	      consultaSQL.append("t1.id_conductor, ");
	      consultaSQL.append("t1.apellidosconductor, ");
	      consultaSQL.append("t1.nombresconductor, ");
	      consultaSQL.append("t1.fecha_emision, ");
	      consultaSQL.append("t1.id_cisterna,");
	      consultaSQL.append("t1.placa_cisterna, ");
	      consultaSQL.append("t1.volumen_total_observado, ");
	      consultaSQL.append("t1.volumen_total_corregido, ");
	      consultaSQL.append("t1.id_transportista, ");
	      consultaSQL.append("t1.razonsocialtransportista, ");
	      consultaSQL.append("t1.descripcionplantadespacho, ");
	      consultaSQL.append("t1.brevete_conductor, ");
	      consultaSQL.append("t1.numero_orden_entrega, ");
	      consultaSQL.append("t1.codigo_scop, ");
	      consultaSQL.append("t1.estado_transporte, ");
	      consultaSQL.append("t1.numero_factura, ");
	      consultaSQL.append("t1.fecha_operativa, ");
	      consultaSQL.append("t1.planta_recepcion, ");
	      consultaSQL.append("t1.id_tracto, ");
	      consultaSQL.append("t1.placa_tracto, ");
	      consultaSQL.append("t1.cantidad_compartimentos, ");
	      consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
	      consultaSQL.append("t1.cisterna_tracto,");
	      consultaSQL.append("t1.precintos_seguridad,");
	      consultaSQL.append("t1.tipo_registro,");
	      consultaSQL.append("t1.peso_bruto,");
	      consultaSQL.append("t1.peso_tara,");
	      consultaSQL.append("t1.peso_neto,");
	      //Campos de auditoria 
	      consultaSQL.append("t1.creado_el_transporte, ");
	      consultaSQL.append("t1.creado_por_transporte, ");
	      consultaSQL.append("t1.usuario_creacion_transporte, ");
	      consultaSQL.append("t1.actualizado_por_transporte, ");
	      consultaSQL.append("t1.usuario_actualizacion_transporte, ");
	      consultaSQL.append("t1.actualizado_el_transporte, ");
	      consultaSQL.append("t1.ip_creacion_transporte,");
	      consultaSQL.append("t1.ip_actualizacion_transporte ");
	      consultaSQL.append(" FROM ");       
	      consultaSQL.append(NOMBRE_VISTA_ASIGNADOS_DIA);
	      consultaSQL.append(" t1 ");
	      consultaSQL.append(sqlWhere);
	      System.out.println(consultaSQL.toString());
	      listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new TransporteMapper());
	      contenido.totalRegistros=listaRegistros.size();
	      contenido.totalEncontrados=listaRegistros.size();
	      contenido.carga= listaRegistros;
	      respuesta.mensaje="OK";
	      respuesta.estado=true;
	      respuesta.contenido = contenido;      
	    } catch (DataAccessException excepcionAccesoDatos) {
	      excepcionAccesoDatos.printStackTrace();
	      respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
	      respuesta.estado=false;
	      respuesta.contenido=null;
	    }
	    return respuesta;
	  }
	
	public RespuestaCompuesta guardarRegistro(Transporte transporte){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			//consultaSQL.append(" (numero_guia_remision,numero_orden_entrega,numero_factura,codigo_scop,fecha_emision,planta_despacho,id_cliente,id_conductor,brevete_conductor,id_cisterna,placa_cisterna,tarjeta_cubicacion_cisterna,id_tracto,placa_tracto,id_transportista,volumen_total_observado,volumen_total_corregido,estado,precintos_seguridad,tipo_registro,peso_bruto,peso_tara,peso_neto,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" (numero_guia_remision, planta_despacho, id_cliente, fecha_emision, volumen_total_observado, volumen_total_corregido, ");
			consultaSQL.append(" brevete_conductor, numero_orden_entrega, codigo_scop, creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ");
			consultaSQL.append(" ip_actualizacion, estado, numero_factura, planta_recepcion, id_tracto, tarjeta_cubicacion_cisterna, precintos_seguridad, ");
			consultaSQL.append(" tipo_registro, peso_neto, peso_bruto, peso_tara, id_transportista, id_conductor, id_cisterna, programacion) ");

			consultaSQL.append(" VALUES (:NumeroGuiaRemision,:PlantaDespacho,:IdCliente,:FechaEmisionGuia,:VolumenTotalObservado,:VolumenTotalCorregido, ");
			consultaSQL.append(" :BreveteConductor, :NumeroOrdenCompra,:CodigoScop,:CreadoEl,:CreadoPor,:ActualizadoPor, :ActualizadoEl,:IpCreacion, ");
			consultaSQL.append(" :IpActualizacion, :Estado, :NumeroFactura, :PlantaRecepcion, :IdTracto, :TarjetaCubicacionCompartimento, :PrecintosSeguridad, ");
			consultaSQL.append(" :TipoRegistro, :PesoNeto, :PesoBruto, :PesoTara, :IdTransportista,:IdConductor, :IdCisterna, :Programacion)");
			
			//consultaSQL.append(" VALUES (:NumeroGuiaRemision,:NumeroOrdenCompra,:NumeroFactura,:CodigoScop,:FechaEmisionGuia,:PlantaDespacho,:IdCliente,:IdConductor,:BreveteConductor,:IdCisterna,:PlacaCisterna,:TarjetaCubicacionCompartimento,:IdTracto,:PlacaTracto,:IdTransportista,:VolumenTotalObservado,:VolumenTotalCorregido,:Estado,:PrecintosSeguridad,:TipoRegistro,:PesoBruto,:PesoTara,:PesoNeto,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("NumeroGuiaRemision", transporte.getNumeroGuiaRemision());
			listaParametros.addValue("NumeroOrdenCompra", transporte.getNumeroOrdenCompra());
			listaParametros.addValue("NumeroFactura", transporte.getNumeroFactura());
			listaParametros.addValue("CodigoScop", transporte.getCodigoScop());
			listaParametros.addValue("FechaEmisionGuia", transporte.getFechaEmisionGuia());
			listaParametros.addValue("PlantaDespacho", transporte.getIdPlantaDespacho());
			listaParametros.addValue("PlantaRecepcion", transporte.getIdPlantaRecepcion());
			listaParametros.addValue("IdCliente", transporte.getIdCliente());
			listaParametros.addValue("IdConductor", transporte.getIdConductor());
			listaParametros.addValue("BreveteConductor", transporte.getBreveteConductor());
			listaParametros.addValue("IdCisterna", transporte.getIdCisterna());
			listaParametros.addValue("PlacaCisterna", transporte.getPlacaCisterna());
			listaParametros.addValue("TarjetaCubicacionCompartimento", transporte.getTarjetaCubicacionCompartimento());
			listaParametros.addValue("IdTracto", transporte.getIdTracto());
			listaParametros.addValue("PlacaTracto", transporte.getPlacaTracto());
			listaParametros.addValue("IdTransportista", transporte.getIdTransportista());
			listaParametros.addValue("VolumenTotalObservado", transporte.getVolumenTotalObservado());
			listaParametros.addValue("VolumenTotalCorregido", transporte.getVolumenTotalCorregido());
			listaParametros.addValue("Estado", transporte.getEstado());
			listaParametros.addValue("PrecintosSeguridad", transporte.getPrecintosSeguridad());
			listaParametros.addValue("TipoRegistro", transporte.getOrigen());
			listaParametros.addValue("PesoBruto", transporte.getPesoBruto());
			listaParametros.addValue("PesoTara", transporte.getPesoTara());
			listaParametros.addValue("PesoNeto", transporte.getPesoNeto());
			listaParametros.addValue("Programacion", transporte.getProgramacion());
			//listaParametros.addValue("SincronizadoEl", transporte.getSincronizadoEl());
			listaParametros.addValue("CreadoEl", transporte.getCreadoEl());
			listaParametros.addValue("CreadoPor", transporte.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", transporte.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", transporte.getActualizadoEl());
			listaParametros.addValue("IpCreacion", transporte.getIpCreacion());
			listaParametros.addValue("IpActualizacion", transporte.getIpActualizacion());
			
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters,claveGenerada,new String[] {NOMBRE_CAMPO_CLAVE});		
			if (cantidadFilasAfectadas>1){
				respuesta.error=Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
			respuesta.valor= claveGenerada.getKey().toString();
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error=Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta actualizarRegistro(Transporte transporte){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("numero_guia_remision	=:NumeroGuiaRemision,");
			consultaSQL.append("planta_despacho			=:PlantaDespacho,");
			consultaSQL.append("id_cliente				=:IdCliente,");
			consultaSQL.append("fecha_emision			=:FechaEmisionGuia,");
			consultaSQL.append("volumen_total_observado	=:VolumenTotalObservado,");
			consultaSQL.append("volumen_total_corregido	=:VolumenTotalCorregido,");
			consultaSQL.append("brevete_conductor		=:BreveteConductor,");
			consultaSQL.append("numero_orden_entrega	=:NumeroOrdenCompra,");
			consultaSQL.append("codigo_scop				=:CodigoScop,");
			consultaSQL.append("numero_factura			=:NumeroFactura,");
			consultaSQL.append("planta_recepcion		=:PlantaRecepcion,");
			consultaSQL.append("id_tracto				=:IdTracto,");
			consultaSQL.append("tipo_registro        =:TipoRegistro,");
			consultaSQL.append("estado					=:Estado,");
			//consultaSQL.append("tarjeta_cubicacion_cisterna=:TarjetaCubicacionCompartimento,");
			consultaSQL.append("precintos_seguridad		=:PrecintosSeguridad,");
			consultaSQL.append("id_transportista		=:IdTransportista,");
			consultaSQL.append("id_conductor			=:IdConductor,");
			consultaSQL.append("id_cisterna				=:IdCisterna,");
			consultaSQL.append("actualizado_por			=:ActualizadoPor,");
			consultaSQL.append("actualizado_el			=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion		=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("NumeroGuiaRemision", transporte.getNumeroGuiaRemision());
			listaParametros.addValue("PlantaDespacho", transporte.getIdPlantaDespacho());
			listaParametros.addValue("IdCliente", transporte.getIdCliente());
			listaParametros.addValue("FechaEmisionGuia", transporte.getFechaEmisionGuia());
			listaParametros.addValue("VolumenTotalObservado", transporte.getVolumenTotalObservado());
			listaParametros.addValue("VolumenTotalCorregido", transporte.getVolumenTotalCorregido());
			listaParametros.addValue("BreveteConductor", transporte.getBreveteConductor());
			listaParametros.addValue("NumeroOrdenCompra", transporte.getNumeroOrdenCompra());
			listaParametros.addValue("CodigoScop", transporte.getCodigoScop());
			listaParametros.addValue("NumeroFactura", transporte.getNumeroFactura());
			listaParametros.addValue("PlantaRecepcion", transporte.getIdPlantaRecepcion());
			listaParametros.addValue("IdTracto", transporte.getIdTracto());
			listaParametros.addValue("TipoRegistro", transporte.getOrigen());
			listaParametros.addValue("Estado", transporte.getEstado());
			//listaParametros.addValue("TarjetaCubicacionCompartimento", transporte.getTarjetaCubicacionCompartimento());
			listaParametros.addValue("PrecintosSeguridad", transporte.getPrecintosSeguridad());
			listaParametros.addValue("IdTransportista", transporte.getIdTransportista());
			listaParametros.addValue("IdConductor", transporte.getIdConductor());
			listaParametros.addValue("IdCisterna", transporte.getIdCisterna());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", transporte.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", transporte.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", transporte.getIpActualizacion());
			listaParametros.addValue("Id", transporte.getId());
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (cantidadFilasAfectadas>1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta eliminarRegistro(int idRegistro){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int cantidadFilasAfectadas=0;	
		String consultaSQL="";
		Object[] parametros = {idRegistro};
		try {
			consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
        	cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
			if (cantidadFilasAfectadas > 1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){	
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta actualizarPesajes(Transporte transporte){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("peso_bruto=:PesoBruto,");
			consultaSQL.append("peso_tara=:PesoTara,");
			consultaSQL.append("peso_neto=:PesoNeto,");
			//datos auditoria
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("PesoBruto", transporte.getPesoBruto());
			listaParametros.addValue("PesoTara", transporte.getPesoTara());
			listaParametros.addValue("PesoNeto", transporte.getPesoNeto());
			//datos auditoria
			listaParametros.addValue("ActualizadoEl", transporte.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", transporte.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", transporte.getIpActualizacion());
			listaParametros.addValue("Id", transporte.getId());
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (cantidadFilasAfectadas>1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	
	public RespuestaCompuesta actualizarEstado(Transporte transporte){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("estado=:Estado");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("Estado", transporte.getEstado());
      listaParametros.addValue("Id", transporte.getId());
      SqlParameterSource namedParameters= listaParametros;
      /*Ejecuta la consulta y retorna las filas afectadas*/
      cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);   
      if (cantidadFilasAfectadas>1){
        respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
        respuesta.estado=false;
        return respuesta;
      }
      respuesta.estado=true;
    } catch (DataIntegrityViolationException excepcionIntegridadDatos){
      excepcionIntegridadDatos.printStackTrace();
      respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
      respuesta.estado=false;
    } catch (DataAccessException excepcionAccesoDatos){
      excepcionAccesoDatos.printStackTrace();
      respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado=false;
    }
    return respuesta;
  }
	
	/**
	  * Metodo para validar que la cistera no se encuentre asignada
	  * 
	  * @return respuesta cantidad de final afectadas.
	  */
	 public Respuesta validaCisternaPorEstadoDeTransporte(int idTransporte, int idCisterna) {
	  int registros= 0;
	  StringBuilder consultaSQL = new StringBuilder();
	  Respuesta respuesta = new Respuesta();
	  try {
	   consultaSQL.append(" SELECT count(id_transporte) ");
	   consultaSQL.append(" FROM ");
	   consultaSQL.append(NOMBRE_VISTA);
	   consultaSQL.append(" WHERE ");
	  /* consultaSQL.append(NOMBRE_CAMPO_CLAVE);
	   consultaSQL.append(" = ? ");*/
	   consultaSQL.append(" id_cisterna = ? ");
	   consultaSQL.append(" AND estado_transporte = ");
	   consultaSQL.append(Transporte.ESTADO_ASIGNADO);
	   registros = jdbcTemplate.queryForObject(consultaSQL.toString(), new Object[] { idCisterna }, Integer.class);
	   
	   if (registros > 0) {
	    respuesta.estado = true;
	    respuesta.valor = String.valueOf(registros);
	   } else {
	    respuesta.estado = true;
	    respuesta.valor = null;
	   }
	  } catch (DataAccessException excepcionAccesoDatos) {
	   excepcionAccesoDatos.printStackTrace();
	   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
	   respuesta.estado = false;
	  } catch (Exception excepcionGenerica) {
	   excepcionGenerica.printStackTrace();
	   respuesta.error = Constante.EXCEPCION_GENERICA;
	   respuesta.estado = false;
	  }
	  return respuesta;
	 }
	 
	 public RespuestaCompuesta validaRegistrosExistentes(ParametrosListar argumentosListar) {
		String sqlLimit = " limit 1 ";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Transporte> contenido = new Contenido<Transporte>();
		List<Transporte> listaRegistros = new ArrayList<Transporte>();
		StringBuilder consultaSQL = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>();
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		try {
			if (argumentosListar.getIdCisterna() > 0){
				filtrosWhere.add(" t1.id_cisterna = " + argumentosListar.getIdCisterna());
			}
			if (argumentosListar.getIdTracto() > 0) {
				filtrosWhere.add(" t1.id_tracto = " + argumentosListar.getIdTracto());
			}
			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
			}
			
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_transporte, ");
			consultaSQL.append("t1.numero_guia_remision, ");
			consultaSQL.append("t1.planta_despacho, ");
			consultaSQL.append("t1.descripcionPlantaDespacho, ");
			consultaSQL.append("t1.cliente_transporte, ");
			consultaSQL.append("t1.id_conductor, ");
			consultaSQL.append("t1.apellidosconductor, ");
			consultaSQL.append("t1.nombresconductor, ");
			consultaSQL.append("t1.fecha_emision, ");
			consultaSQL.append("t1.id_cisterna, ");
			consultaSQL.append("t1.placa_cisterna, ");
			consultaSQL.append("t1.volumen_total_observado, ");
			consultaSQL.append("t1.volumen_total_corregido, ");
			consultaSQL.append("t1.id_transportista, ");
			consultaSQL.append("t1.razonsocialtransportista, ");
			consultaSQL.append("t1.brevete_conductor, ");
			consultaSQL.append("t1.numero_orden_entrega, ");
			consultaSQL.append("t1.codigo_scop, ");
			consultaSQL.append("t1.estado_transporte, ");
			consultaSQL.append("t1.numero_factura, ");
			consultaSQL.append("t1.planta_recepcion, ");
			consultaSQL.append("t1.nombre_planta_recepcion, ");
			consultaSQL.append("t1.alias_planta_recepcion, ");
			consultaSQL.append("t1.id_tracto, ");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			consultaSQL.append("t1.tarjeta_cubicacion_cisterna, ");
			consultaSQL.append("t1.cisterna_tracto,");
			consultaSQL.append("t1.precintos_seguridad,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.peso_bruto,");
			consultaSQL.append("t1.peso_tara,");
			consultaSQL.append("t1.peso_neto,");
			consultaSQL.append("t1.creado_el_transporte, ");
			consultaSQL.append("t1.creado_por_transporte, ");
			consultaSQL.append("t1.usuario_creacion_transporte, ");
			consultaSQL.append("t1.actualizado_por_transporte, ");
			consultaSQL.append("t1.usuario_actualizacion_transporte, ");
			consultaSQL.append("t1.actualizado_el_transporte, ");
			consultaSQL.append("t1.ip_creacion_transporte,");
			consultaSQL.append("t1.ip_actualizacion_transporte ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new TransporteMapper());
			totalEncontrados =totalRegistros;
			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalEncontrados;
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error=  Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido=null;
		} catch (Exception excepcionGenerica) {
			excepcionGenerica.printStackTrace();
			respuesta.error= Constante.EXCEPCION_GENERICA;
			respuesta.contenido=null;
			respuesta.estado = false;
		}
		return respuesta;
	}
	 
	//Agregado por 9000002570============================================
	 
	 public RespuestaCompuesta guardarRegistroTiempo(EtapaTransporte etapa){
			RespuestaCompuesta respuesta = new RespuestaCompuesta();
			StringBuilder consultaSQL= new StringBuilder();
			KeyHolder claveGenerada = null;
			int cantidadFilasAfectadas=0;
			
			try {
				
				consultaSQL.append("INSERT INTO ");
				consultaSQL.append(" SGO.ETAPA_TRANSPORTE ");
				consultaSQL.append(" (id_operacion_etapa_ruta, id_transporte, fecha_inicio_etapa, fecha_fin_etapa, tiempo_etapa, ");
				consultaSQL.append(" observacion_etapa, creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ip_actualizacion )");
				consultaSQL.append(" VALUES (:IdOperacionEtapaRuta,:IdTransporte,:FechaInicioEtapa,:FechaFinEtapa, :TiempoEtapa, ");
				consultaSQL.append(" :ObservacionEtapa, :CreadoEl, :CreadoPor, :ActualizadoPor, :ActualizadoEl, :IpCreacion, :ipActualizacion  )");
				
				MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
				listaParametros.addValue("IdOperacionEtapaRuta", etapa.getIdOperacionEtapaRuta());
				listaParametros.addValue("IdTransporte", etapa.getIdTransporte());
				listaParametros.addValue("FechaInicioEtapa", etapa.getFechaInicio());
				listaParametros.addValue("FechaFinEtapa", etapa.getFechaFin());
				listaParametros.addValue("TiempoEtapa", etapa.getTiempoEtapa());
				listaParametros.addValue("ObservacionEtapa", etapa.getObservacion().toUpperCase());
				listaParametros.addValue("CreadoEl", etapa.getCreadoEl());
				listaParametros.addValue("CreadoPor", etapa.getCreadoPor());
				listaParametros.addValue("ActualizadoPor", etapa.getActualizadoPor());
				listaParametros.addValue("ActualizadoEl", etapa.getActualizadoEl());
				listaParametros.addValue("IpCreacion", etapa.getIpCreacion());
				listaParametros.addValue("ipActualizacion", etapa.getIpActualizacion());

				SqlParameterSource namedParameters= listaParametros;
				/*Ejecuta la consulta y retorna las filas afectadas*/
				claveGenerada = new GeneratedKeyHolder();
				cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters,claveGenerada,new String[] {"id_operacion_etapa_ruta"});		
				if (cantidadFilasAfectadas>1){
					respuesta.error=Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
					respuesta.estado=false;
					return respuesta;
				}
				respuesta.estado=true;
				respuesta.valor= claveGenerada.getKey().toString();
				
		
			} catch (DataIntegrityViolationException excepcionIntegridadDatos){
				excepcionIntegridadDatos.printStackTrace();
				respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
				respuesta.estado=false;
			} catch (DataAccessException excepcionAccesoDatos){
				excepcionAccesoDatos.printStackTrace();
				respuesta.error=Constante.EXCEPCION_ACCESO_DATOS;
				respuesta.estado=false;
			}
			return respuesta;
	 }
	 
	 public RespuestaCompuesta actualizarRegistroTiempo(EtapaTransporte etapa){
		 
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {		
			
			consultaSQL.append("UPDATE ");
			consultaSQL.append(" SGO.ETAPA_TRANSPORTE ");
			consultaSQL.append(" SET ");
			consultaSQL.append("fecha_inicio_etapa=:FechaInicioEtapa,");
			consultaSQL.append("fecha_fin_etapa=:FechaFinEtapa,");
			consultaSQL.append("tiempo_etapa=:TiempoEtapa, ");
			consultaSQL.append("observacion_etapa=:ObservacionEtapa, ");
			consultaSQL.append("actualizado_por=:ActualizadoPor, ");
			consultaSQL.append("actualizado_el=:ActualizadoEl, ");
			consultaSQL.append("ip_actualizacion=:IpActualizacion ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(" id_etapa_transporte ");
			consultaSQL.append("=:Id");
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("FechaInicioEtapa", etapa.getFechaInicio());
			listaParametros.addValue("FechaFinEtapa", etapa.getFechaFin());
			listaParametros.addValue("TiempoEtapa", etapa.getTiempoEtapa());
			listaParametros.addValue("ObservacionEtapa", etapa.getObservacion().toUpperCase());
			listaParametros.addValue("ActualizadoPor", etapa.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", etapa.getActualizadoEl());
			listaParametros.addValue("IpActualizacion", etapa.getIpActualizacion());
			listaParametros.addValue("Id", etapa.getId());
			SqlParameterSource namedParameters= listaParametros;
			
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (cantidadFilasAfectadas>1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	//===================================================================
		
	
}