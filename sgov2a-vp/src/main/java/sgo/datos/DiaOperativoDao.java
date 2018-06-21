package sgo.datos;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import sgo.entidad.Cierre;
import sgo.entidad.Contenido;
import sgo.entidad.DiaOperativo;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class DiaOperativoDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION   + "dia_operativo";
 public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION   + "v_dia_operativo";
 public static final String NOMBRE_VISTA_RESUMEN = Constante.ESQUEMA_APLICACION   + "v_dia_operativo_resumen";
 public final static String NOMBRE_CAMPO_CLAVE = "id_doperativo";
 public final static String FECHA_OPERATIVA = "fecha_operativa";

 @Autowired
 public void setDataSource(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
  this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }

 public DataSource getDataSource() {
  return this.jdbcTemplate.getDataSource();
 }

 public String mapearCampoOrdenamiento(String propiedad) {
  String campoOrdenamiento = "";
  try {
   if (propiedad.equals("id")) {
    campoOrdenamiento = "id_doperativo";
   }
   if (propiedad.equals("fechaOperativa")) {
    campoOrdenamiento = "fecha_operativa";
   }
   if (propiedad.equals("operacion")) {
    campoOrdenamiento = "id_operacion";
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
  * Metodo para recuperar los registros según los filtros.
  * 
  * @param argumentosListar
  *         Contiene los filtros para el where de la consulta.
  * @return respuesta Resultado de la consulta.
  */
 public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
  String sqlLimit = "";
  String sqlOrderBy = "";
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlWhere = "";
  int totalRegistros = 0, totalEncontrados = 0;
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<DiaOperativo> contenido = new Contenido<DiaOperativo>();
  List<DiaOperativo> listaRegistros = new ArrayList<DiaOperativo>();
  String detalleProductoSolicitado = "";
  List<Object> parametros = new ArrayList<Object>();
  try {
   if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
    sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
    parametros.add(argumentosListar.getInicioPaginacion());
    parametros.add(argumentosListar.getRegistrosxPagina());
   }

   StringBuilder consultaSQL = new StringBuilder();
   consultaSQL.setLength(0);
   consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA_RESUMEN);
   totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null,Integer.class);
   totalEncontrados = totalRegistros;

   sqlOrderBy = Constante.SQL_ORDEN + FECHA_OPERATIVA + " desc ";

   if (!argumentosListar.getValorBuscado().isEmpty()) {
    filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_CLAVE + ") like lower('%" + argumentosListar.getValorBuscado() + "%') ");
   }
   if (!argumentosListar.getFiltroFechaPlanificada().isEmpty()) {
    filtrosWhere.add(" t1.fecha_operativa ='" + argumentosListar.getFiltroFechaPlanificada() + "' ");
   }

   if (Utilidades.esValido(argumentosListar.getFiltroOperacion())) {
    filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
   }
   String fechaInicio = argumentosListar.getFiltroFechaInicio();
   String fechaFinal = argumentosListar.getFiltroFechaFinal();

   // Esto para el filtro de fechas
   if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
    filtrosWhere.add(" t1." + FECHA_OPERATIVA + Constante.SQL_ENTRE + ("'" + fechaInicio + "'" + Constante.SQL_Y + "'" + fechaFinal + "'"));
   } else {
    if (!fechaInicio.isEmpty()) {
     filtrosWhere.add(" t1." + FECHA_OPERATIVA + " >= '" + fechaInicio + "'");
    }
    if (!fechaFinal.isEmpty()) {
     filtrosWhere.add(" t1." + FECHA_OPERATIVA + " <= '" + fechaFinal + "'");
    }
   }

   if (!filtrosWhere.isEmpty()) {
    consultaSQL.setLength(0);
    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
    consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA_RESUMEN + " t1 " + sqlWhere);
    totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(),null, Integer.class);
   }

   consultaSQL.setLength(0);
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.estado, ");
   consultaSQL.append("t1.creado_el,");
   consultaSQL.append("t1.creado_por,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.ip_creacion,");
   consultaSQL.append("t1.ip_actualizacion,");
   consultaSQL.append("t1.fecha_operativa,");
   consultaSQL.append("t1.fecha_estimada_carga,");
   consultaSQL.append("t1.ultima_jornada_liquidada,");
   consultaSQL.append("t1.planta_despacho_defecto,");
   consultaSQL.append("t1.descripcion_planta_despacho,");
   consultaSQL.append("t1.id_operacion,");
   consultaSQL.append("t1.nombre_usuario_creador as usuario_creacion,");
   consultaSQL.append("t1.nombre_usuario_actualizador as usuario_actualizacion,");
   consultaSQL.append("t1.identidad_usuario_creador,");
   consultaSQL.append("t1.identidad_usuario_actualizador,");
   consultaSQL.append("t1.nombre_operacion,");
   consultaSQL.append("t1.id_cliente,");
   consultaSQL.append("t1.nombre_corto_cliente,");
   consultaSQL.append("t1.razon_social_cliente,");
   consultaSQL.append("t1.total_cisternas,");   
   consultaSQL.append("t1.total_planificados,");
   consultaSQL.append("t1.total_asignados,");
   consultaSQL.append("t1.total_descargados");
   //9000002608==========================================
   consultaSQL.append(", t1.cantidad_cisternas_planificadas");
   //====================================================
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA_RESUMEN);
   consultaSQL.append(" t1 ");
   consultaSQL.append(sqlWhere);
   consultaSQL.append(sqlOrderBy);
   consultaSQL.append(sqlLimit);
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DiaOperativoMapper());
   //System.out.println(consultaSQL.toString());
     
   contenido.carga = listaRegistros;
   respuesta.estado = true;
   respuesta.contenido = contenido;
   respuesta.contenido.totalRegistros = totalRegistros;
   respuesta.contenido.totalEncontrados = totalEncontrados;

  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.contenido = null;
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public RespuestaCompuesta validaRegistrosExistentes(ParametrosListar argumentosListar) {
    String sqlLimit = " limit 1 ";
	int totalRegistros = 0, totalEncontrados = 0;
	RespuestaCompuesta respuesta = new RespuestaCompuesta();
	Contenido<DiaOperativo> contenido = new Contenido<DiaOperativo>();
	List<DiaOperativo> listaRegistros = new ArrayList<DiaOperativo>();
	StringBuilder consultaSQL = new StringBuilder();
	List<Object> parametros = new ArrayList<Object>();
	List<String> filtrosWhere = new ArrayList<String>();
	String sqlWhere = "";
	String sqlOrderBy = "";
	try {
		if (argumentosListar.getIdOperacion() > 0){
			filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getIdOperacion());
		}
		if (!argumentosListar.getFiltroFechaDiaOperativo().isEmpty() ){
			filtrosWhere.add(" t1.fecha_operativa >= '" + argumentosListar.getFiltroFechaDiaOperativo() +"' ");
			sqlLimit = "";
		}
		if (!argumentosListar.getCampoOrdenamiento().isEmpty() ){
			sqlOrderBy = Constante.SQL_ORDEN + FECHA_OPERATIVA + " desc ";
		}
		if (!filtrosWhere.isEmpty()) {
			consultaSQL.setLength(0);
			sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
		}

	   consultaSQL.setLength(0);
	   consultaSQL.append("SELECT ");
	   consultaSQL.append("t1.id_doperativo,");
	   consultaSQL.append("t1.estado, ");
	   consultaSQL.append("t1.creado_el,");
	   consultaSQL.append("t1.creado_por,");
	   consultaSQL.append("t1.actualizado_el,");
	   consultaSQL.append("t1.actualizado_por,");
	   consultaSQL.append("t1.ip_creacion,");
	   consultaSQL.append("t1.ip_actualizacion,");
	   consultaSQL.append("t1.fecha_operativa,");
	   consultaSQL.append("t1.fecha_estimada_carga,");
	   consultaSQL.append("t1.ultima_jornada_liquidada,");
	   consultaSQL.append("t1.planta_despacho_defecto,");
	   consultaSQL.append("t1.descripcion_planta_despacho,");
	   consultaSQL.append("t1.id_operacion,");
	   consultaSQL.append("t1.nombre_usuario_creador as usuario_creacion,");
	   consultaSQL.append("t1.nombre_usuario_actualizador as usuario_actualizacion,");
	   consultaSQL.append("t1.identidad_usuario_creador,");
	   consultaSQL.append("t1.identidad_usuario_actualizador,");
	   consultaSQL.append("t1.nombre_operacion,");
	   consultaSQL.append("t1.id_cliente,");
	   consultaSQL.append("t1.nombre_corto_cliente,");
	   consultaSQL.append("t1.razon_social_cliente,");
	   consultaSQL.append("t1.total_cisternas,");   
	   consultaSQL.append("t1.total_planificados,");
	   consultaSQL.append("t1.total_asignados,");
	   consultaSQL.append("t1.total_descargados");
	   //9000002608==========================================
	   consultaSQL.append(", t1.cantidad_cisternas_planificadas");
	   //====================================================
	   consultaSQL.append(" FROM ");
	   consultaSQL.append(NOMBRE_VISTA_RESUMEN);
	   consultaSQL.append(" t1 ");
	   consultaSQL.append(sqlWhere);
	   consultaSQL.append(sqlOrderBy);
	   consultaSQL.append(sqlLimit);
	   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DiaOperativoMapper());
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
 

 public RespuestaCompuesta recuperarRegistro(int ID) {
  StringBuilder consultaSQL = new StringBuilder();
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<DiaOperativo> contenido = new Contenido<DiaOperativo>();
  List<DiaOperativo> listaRegistros = new ArrayList<DiaOperativo>();
  try {
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.estado, ");
   consultaSQL.append("t1.creado_el,");
   consultaSQL.append("t1.creado_por,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.ip_creacion,");
   consultaSQL.append("t1.ip_actualizacion,");
   consultaSQL.append("t1.fecha_operativa,");
   consultaSQL.append("t1.fecha_estimada_carga,");
   consultaSQL.append("t1.ultima_jornada_liquidada,");
   consultaSQL.append("t1.planta_despacho_defecto,");
   consultaSQL.append("t1.descripcion_planta_despacho,");
   consultaSQL.append("t1.id_operacion,");
   consultaSQL.append("t1.nombre_usuario_creador as usuario_creacion,");
   consultaSQL.append("t1.nombre_usuario_actualizador as usuario_actualizacion,");
   consultaSQL.append("t1.identidad_usuario_creador,");
   consultaSQL.append("t1.identidad_usuario_actualizador,");
   consultaSQL.append("t1.nombre_operacion,");
   consultaSQL.append("t1.id_cliente,");
   consultaSQL.append("t1.nombre_corto_cliente,");
   consultaSQL.append("t1.razon_social_cliente,");
   consultaSQL.append("t1.total_cisternas,");   
   consultaSQL.append("t1.total_planificados,");
   consultaSQL.append("t1.total_asignados,");
   consultaSQL.append("t1.total_descargados");
   //9000002608==========================================
   consultaSQL.append(", t1.cantidad_cisternas_planificadas");
   //====================================================
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA_RESUMEN);
   consultaSQL.append(" t1 ");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=?");
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new Object[] { ID }, new DiaOperativoMapper());
   contenido.totalRegistros = listaRegistros.size();
   contenido.totalEncontrados = listaRegistros.size();
   contenido.carga = listaRegistros;
   respuesta.estado = true;
   respuesta.contenido = contenido;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  }
  return respuesta;
 }

 public Respuesta recuperarUltimoDiaOperativo(int idOperacion, String txtQuery) {
  Date ultimoDia = null;
  Respuesta respuesta = new Respuesta();
  try {
   StringBuilder consultaSQL = new StringBuilder();
   consultaSQL.append("select max(fecha_operativa) as fecha");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" WHERE id_operacion=? ");
   if(!txtQuery.isEmpty()){
	  consultaSQL.append(txtQuery);
   }
   ultimoDia = jdbcTemplate.queryForObject(consultaSQL.toString(),
     new Object[] { idOperacion }, Date.class);
   if (ultimoDia == null) {
    respuesta.estado = true;
    respuesta.valor = null;
   } else {
    respuesta.estado = true;
    respuesta.valor = ultimoDia.toString();
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
 
 public RespuestaCompuesta recuperarDiaOperativoAnterior(ParametrosListar argumentosListar ) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
 Contenido<DiaOperativo> contenido = new Contenido<DiaOperativo>();
 List<DiaOperativo> listaRegistros = new ArrayList<DiaOperativo>();
 List<String> filtrosWhere = new ArrayList<String>();
 String sqlWhere="";
 List<Object> parametros = new ArrayList<Object>();
  try {
	  
	  if (argumentosListar.getIdOperacion() > 0){
			filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getIdOperacion());
	  }
	  if (argumentosListar.getFiltroDiaOperativo() > 0){
			filtrosWhere.add(" t1.id_doperativo < " + argumentosListar.getFiltroDiaOperativo());
	  }
	  
	   if (!filtrosWhere.isEmpty()) {	
		    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
		   
		}
   StringBuilder consultaSQL = new StringBuilder();
   consultaSQL.append("SELECT t1.* ");
   //consultaSQL.append(" ,0::integer as total_cisternas,0::integer as total_descargados,0::integer as total_planificados,0::integer as total_asignados");
   consultaSQL.append(" FROM sgo.v_dia_operativo_resumen t1 "); //antes v_dia_operativo
   consultaSQL.append(sqlWhere);
   consultaSQL.append(" order by fecha_operativa desc limit 1; ");   
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DiaOperativoMapper());   
   //listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new Object[] { idDiaOperativo,idDiaOperativo}, new DiaOperativoMapper());
   contenido.carga = listaRegistros;
   respuesta.estado = true;
   respuesta.contenido = contenido;
   respuesta.contenido.totalRegistros = listaRegistros.size();
   respuesta.contenido.totalEncontrados = listaRegistros.size();   
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

 public RespuestaCompuesta guardarRegistro(DiaOperativo diaOperativo) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  KeyHolder claveGenerada = null;
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("INSERT INTO ");
   consultaSQL.append(NOMBRE_TABLA);
   //Se agrega el campo cantidad_cisternas_planificadas al insert por requerimiento 9000002608
   consultaSQL.append(" (cantidad_cisternas_planificadas, fecha_operativa,ultima_jornada_liquidada,id_operacion,fecha_estimada_carga,estado,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
   consultaSQL.append(" VALUES (:CantidadCisternasPlan, :FechaOperativa,:UltimaJornadaLiquidada,:idOperacion,:FechaEstimadaCarga,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   //Se agrega por requerimiento 9000002608====================================================
   listaParametros.addValue("CantidadCisternasPlan", diaOperativo.getCantidadCisternasPlan());
   //==========================================================================================
   listaParametros.addValue("FechaOperativa", diaOperativo.getFechaOperativa());
   listaParametros.addValue("UltimaJornadaLiquidada", diaOperativo.getUltimaJornadaLiquidada());
   listaParametros.addValue("idOperacion", diaOperativo.getIdOperacion());
   listaParametros.addValue("FechaEstimadaCarga", diaOperativo.getFechaEstimadaCarga());
   listaParametros.addValue("Estado", diaOperativo.getEstado());
   listaParametros.addValue("CreadoEl", diaOperativo.getCreadoEl());
   listaParametros.addValue("CreadoPor", diaOperativo.getCreadoPor());
   listaParametros.addValue("ActualizadoPor", diaOperativo.getActualizadoPor());
   listaParametros.addValue("ActualizadoEl", diaOperativo.getActualizadoEl());
   listaParametros.addValue("IpCreacion", diaOperativo.getIpCreacion());
   listaParametros.addValue("IpActualizacion",diaOperativo.getIpActualizacion());

   SqlParameterSource namedParameters = listaParametros;
   /* Ejecuta la consulta y retorna las filas afectadas */
   claveGenerada = new GeneratedKeyHolder();
   cantidadFilasAfectadas = namedJdbcTemplate.update(consultaSQL.toString(), namedParameters, claveGenerada, new String[] { NOMBRE_CAMPO_CLAVE });
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
   respuesta.valor = claveGenerada.getKey().toString();
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   excepcionIntegridadDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 public RespuestaCompuesta actualizarRegistro(DiaOperativo dia_operativo) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   
   //Agregado por req 9000002608==============================================
   consultaSQL.append("cantidad_cisternas_planificadas=:CantCisterPlan,");
   //=========================================================================
   
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:Id");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   // Valores Auditoria
   listaParametros.addValue("ActualizadoEl", dia_operativo.getActualizadoEl());
   
   //Agregado por req 9000002608========================================================
   listaParametros.addValue("CantCisterPlan", dia_operativo.getCantidadCisternasPlan());
   //===================================================================================
   
   listaParametros.addValue("ActualizadoPor", dia_operativo.getActualizadoPor());
   listaParametros.addValue("IpActualizacion", dia_operativo.getIpActualizacion());
   listaParametros.addValue("Id", dia_operativo.getId());
   SqlParameterSource namedParameters = listaParametros;
   /* Ejecuta la consulta y retorna las filas afectadas */
   cantidadFilasAfectadas = namedJdbcTemplate.update(consultaSQL.toString(),
     namedParameters);
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   excepcionIntegridadDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 public RespuestaCompuesta eliminarRegistro(int idRegistro) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  int cantidadFilasAfectadas = 0;
  String consultaSQL = "";
  Object[] parametros = { idRegistro };
  try {
   consultaSQL = "DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE
     + "=?";
   cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   excepcionIntegridadDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 public RespuestaCompuesta ActualizarEstadoRegistro(DiaOperativo entidad) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("estado=:Estado,");
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:Id");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("Estado", entidad.getEstado());
   // Valores Auditoria
   listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
   listaParametros.addValue("ActualizadoPor", entidad.getActualizadoPor());
   listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());
   listaParametros.addValue("Id", entidad.getId());
   SqlParameterSource namedParameters = listaParametros;
   /* Ejecuta la consulta y retorna las filas afectadas */
   cantidadFilasAfectadas = namedJdbcTemplate.update(consultaSQL.toString(), namedParameters);
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   excepcionIntegridadDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 /**
  * Metodo para recuperar la fecha actual.
  * 
  * @return respuesta Fecha actual del sistema.
  */
 public Respuesta recuperarFechaActual() {
  Date fechaActual = null;
  StringBuilder consultaSQL = new StringBuilder();
  Respuesta respuesta = new Respuesta();
  try {
   consultaSQL.append("SELECT now() WHERE 1=?");
   fechaActual = jdbcTemplate.queryForObject(consultaSQL.toString(),
     new Object[] { 1 }, Date.class);
   if (fechaActual == null) {
    respuesta.estado = true;
    respuesta.valor = null;
   } else {
    respuesta.estado = true;
    respuesta.valor = fechaActual.toString();
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
 
 /**
  * Metodo para recuperar la fecha actual formato fecha date.sql.
  * 
  * @return respuesta Fecha actual del sistema.
  */
 public Date recuperarFechaActualDateSql(String formato) {
  Date fechaActual = null;
  StringBuilder consultaSQL = new StringBuilder();
  Respuesta respuesta = new Respuesta();
  java.sql.Date sqlDateFechaActual=null;
  try {
   consultaSQL.append("SELECT now() WHERE 1=?");
   fechaActual = jdbcTemplate.queryForObject(consultaSQL.toString(),
     new Object[] { 1 }, Date.class);
   if (fechaActual == null) {
    respuesta.estado = true;
    respuesta.valor = null;
   } else {
    respuesta.estado = true;
    respuesta.valor = fechaActual.toString();
   }
   
   if(respuesta.valor!=null){
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
	    java.util.Date date = sdf.parse(respuesta.valor);	               
	    sqlDateFechaActual = new Date(date.getTime()); 
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
  
  return sqlDateFechaActual;
 }
 
 /**
  * Metodo para recuperar la fecha actual formato fecha date.sql.
  * 
  * @return respuesta Fecha actual del sistema.
  */
 public Timestamp recuperarFechaActualTimeStamp() {
  Timestamp fechaActual = null;
  StringBuilder consultaSQL = new StringBuilder();
  Respuesta respuesta = new Respuesta();
  try {
   consultaSQL.append("SELECT now() WHERE 1=?");
   fechaActual = jdbcTemplate.queryForObject(consultaSQL.toString(), new Object[] { 1 }, Timestamp.class);
   if (fechaActual == null) {
    respuesta.estado = true;
    respuesta.valor = null;
   } else {
    respuesta.estado = true;
    respuesta.valor = fechaActual.toString();
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
  
  return fechaActual;
 }
 

 
 

 public String mapearCampoOrdenamientoParaCierre(String propiedad) {
  String campoOrdenamiento = "fecha_operativa";
  try {
   if (propiedad.equals("fechaOperativa")) {
    campoOrdenamiento = "fecha_operativa";
   }
   if (propiedad.equals("totalAsignado")) {
    campoOrdenamiento = "total_asignado";
   }
   if (propiedad.equals("totalDescargado")) {
    campoOrdenamiento = "total_descargado";
   }
   if (propiedad.equals("ultimaActualizacion")) {
    campoOrdenamiento = "ultima_actualizacion";
   }
   if (propiedad.equals("nombreUsuario")) {
    campoOrdenamiento = "nombre_usuario";
   }
   if (propiedad.equals("estado")) {
    campoOrdenamiento = "estado";
   }
  } catch (Exception excepcion) {

  }
  return campoOrdenamiento;
 }

 /**
  * Metodo para recuperar los registros según los filtros.
  * 
  * @param argumentosListar
  *         Contiene los filtros para el where de la consulta.
  * @return respuesta Resultado de la consulta.
  */
 public RespuestaCompuesta recuperarRegistrosParaCierre(ParametrosListar argumentosListar) {
  String sqlLimit = "";
  String sqlOrderBy = "";
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlWhere = "";
  int totalRegistros = 0, totalEncontrados = 0;
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<Cierre> contenido = new Contenido<Cierre>();
  List<Cierre> listaRegistros = new ArrayList<Cierre>();

  List<Object> parametros = new ArrayList<Object>();
  try {
   if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
    sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
    parametros.add(argumentosListar.getInicioPaginacion());
    parametros.add(argumentosListar.getRegistrosxPagina());
   }

   sqlOrderBy = " ORDER BY " + this.mapearCampoOrdenamientoParaCierre(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();
   StringBuilder consultaSQL = new StringBuilder();
   consultaSQL.setLength(0);
   consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_TABLA);
   totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
   totalEncontrados = totalRegistros;
   
   if (Utilidades.esValido(argumentosListar.getFiltroOperacion())) {
    filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
   }

   if (!argumentosListar.getFiltroFechaPlanificada().isEmpty()) {
    String[] fecha = argumentosListar.getFiltroFechaPlanificada().split("-");
    String fechaInicio = new String(fecha[0]);
    String fechaFinal = new String(fecha[1]);

    // Esto para el filtro de fechas
    if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
     filtrosWhere.add(" t1." + FECHA_OPERATIVA + Constante.SQL_ENTRE + ("'" + fechaInicio + "'" + Constante.SQL_Y + "'" + fechaFinal + "'"));
    } else {
     if (!fechaInicio.isEmpty()) {
      filtrosWhere.add(" t1." + FECHA_OPERATIVA + " >= '" + fechaInicio + "'");
     }
     if (!fechaFinal.isEmpty()) {
      filtrosWhere.add(" t1." + FECHA_OPERATIVA + " <= '" + fechaFinal + "'");
     }
    }
   }
   if (!filtrosWhere.isEmpty()) {
    consultaSQL.setLength(0);
    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
    consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM sgo.v_cierre t1 " + sqlWhere);
    totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
   }

   consultaSQL.setLength(0);
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.fecha_operativa,");
   consultaSQL.append("t1.total_asignado, ");
   consultaSQL.append("t1.total_descargado, ");
   consultaSQL.append("t1.ultima_actualizacion, ");
   consultaSQL.append("t1.id_usuario, ");
   consultaSQL.append("t1.nombre_usuario, ");
   consultaSQL.append("t1.nombre_operacion, ");
   consultaSQL.append("t1.nombre_cliente, ");
   consultaSQL.append("t1.estado ");
   consultaSQL.append(" FROM sgo.v_cierre t1 ");
   consultaSQL.append(sqlWhere);
   consultaSQL.append(sqlOrderBy);
   consultaSQL.append(sqlLimit);
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), parametros.toArray(), new CierreMapper());

   contenido.carga = listaRegistros;
   respuesta.estado = true;
   respuesta.contenido = contenido;
   respuesta.contenido.totalRegistros = totalRegistros;
   respuesta.contenido.totalEncontrados = totalEncontrados;

  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.contenido = null;
   respuesta.estado = false;
  }
  return respuesta;
 }

}