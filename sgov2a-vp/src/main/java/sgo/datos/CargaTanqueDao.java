package sgo.datos;

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

import sgo.entidad.CargaTanque;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class CargaTanqueDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "carga_tanque";
 public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_carga_tanque";
 public final static String NOMBRE_CAMPO_CLAVE = "id_ctanque";
 public final static String FILTRO_ESTACION = "id_estacion";
 public final static String FILTRO_DIA_OPERATIVO = "id_doperativo";

 @Autowired
 public void setDataSource(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
  this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }

 public DataSource getDataSource() {
  return this.jdbcTemplate.getDataSource();
 }

 public String mapearCampoOrdenamiento(String propiedad) {
  String campoOrdenamiento = "id_ctanque";
  try {
   if (propiedad.equals("id")) {
    campoOrdenamiento = "id_ctanque";
   }
   if (propiedad.equals("idTanque")) {
    campoOrdenamiento = "id_tanque";
   }
   if (propiedad.equals("fechaHoraInicial")) {
    campoOrdenamiento = "fecha_hora_inicio";
   }
   if (propiedad.equals("fechaHoraFinal")) {
    campoOrdenamiento = "fecha_hora_fin";
   }
   if (propiedad.equals("alturaInicial")) {
    campoOrdenamiento = "altura_inicial_producto";
   }
   if (propiedad.equals("alturaFinal")) {
    campoOrdenamiento = "altura_final_producto";
   }
   if (propiedad.equals("temperaturaInicialCentro")) {
    campoOrdenamiento = "temperatura_inicial_centro";
   }
   if (propiedad.equals("temperaturaFinalCentro")) {
    campoOrdenamiento = "temperatura_final_centro";
   }
   if (propiedad.equals("temperaturaIniciaProbeta")) {
    campoOrdenamiento = "temperatura_inicial_probeta";
   }
   if (propiedad.equals("temperaturaFinalProbeta")) {
    campoOrdenamiento = "temperatura_final_probeta";
   }
   if (propiedad.equals("apiObservadoInicial")) {
    campoOrdenamiento = "api_observado_inicial";
   }
   if (propiedad.equals("apiObservadoFinal")) {
    campoOrdenamiento = "api_observado_final";
   }
   if (propiedad.equals("factorCorreccionInicial")) {
    campoOrdenamiento = "factor_correccion_inicial";
   }
   if (propiedad.equals("factorCorreccionFinal")) {
    campoOrdenamiento = "factor_correccion_final";
   }
   if (propiedad.equals("volumenObservadoInicial")) {
    campoOrdenamiento = "volumen_observado_inicial";
   }
   if (propiedad.equals("volumenObservadoFinal")) {
    campoOrdenamiento = "volumen_observado_final";
   }
   if (propiedad.equals("volumenCorregidoInicial")) {
    campoOrdenamiento = "volumen_corregido_inicial";
   }
   if (propiedad.equals("volumenCorregidoFinal")) {
    campoOrdenamiento = "volumen_corregido_final";
   }
   // Campos de auditoria
  } catch (Exception excepcion) {

  }
  return campoOrdenamiento;
 }

 public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
  String sqlLimit = "";
  int totalRegistros = 0, totalEncontrados = 0;
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<CargaTanque> contenido = new Contenido<CargaTanque>();
  List<CargaTanque> listaRegistros = new ArrayList<CargaTanque>();
  List<Object> parametros = new ArrayList<Object>();
  String sqlOrderBy = "";
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlWhere = "";
  try {
   StringBuilder consultaSQL = new StringBuilder();
   
   if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
    sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
    parametros.add(argumentosListar.getInicioPaginacion());
    parametros.add(argumentosListar.getRegistrosxPagina());
   }

   sqlOrderBy = " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();

   if ((argumentosListar.getFiltroEstacion()) > Constante.FILTRO_TODOS) {
    filtrosWhere.add(" t1." + FILTRO_ESTACION + "=" + argumentosListar.getFiltroEstacion());
   }

   if (Integer.parseInt(argumentosListar.getFiltroFechaPlanificada()) > Constante.FILTRO_TODOS) {
    filtrosWhere.add(" t1." + FILTRO_DIA_OPERATIVO + "=" + Integer.parseInt(argumentosListar.getFiltroFechaPlanificada()));
   }

   if (!filtrosWhere.isEmpty()) {    
    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
   }
   
   consultaSQL.setLength(0);
   consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
   totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
   totalRegistros = totalEncontrados;

   consultaSQL.setLength(0);
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_ctanque,");
   consultaSQL.append("t1.id_tanque,");
   consultaSQL.append("t1.nombre_tanque,");
//   consultaSQL.append("t1.tipo_tanque,");   
   consultaSQL.append("t1.nombre_estacion,");
   consultaSQL.append("t1.id_estacion,");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.fecha_hora_inicio,");
   consultaSQL.append("t1.fecha_hora_fin,");
   consultaSQL.append("t1.altura_inicial_producto,");
   consultaSQL.append("t1.altura_final_producto,");
   consultaSQL.append("t1.temperatura_inicial_centro,");
   consultaSQL.append("t1.temperatura_final_centro,");
   consultaSQL.append("t1.temperatura_inicial_probeta,");
   consultaSQL.append("t1.temperatura_final_probeta,");
   consultaSQL.append("t1.api_observado_inicial,");
   consultaSQL.append("t1.api_observado_final,");
   consultaSQL.append("t1.factor_correccion_inicial,");
   consultaSQL.append("t1.factor_correccion_final,");
   consultaSQL.append("t1.volumen_observado_inicial,");
   consultaSQL.append("t1.volumen_observado_final,");
   consultaSQL.append("t1.volumen_corregido_inicial,");
   consultaSQL.append("t1.volumen_corregido_final,");   
   // Campos de auditoria
   consultaSQL.append("t1.creado_el,");
   consultaSQL.append("t1.creado_por,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.usuario_creacion,");
   consultaSQL.append("t1.usuario_actualizacion,");
   consultaSQL.append("t1.ip_creacion,");
   consultaSQL.append("t1.ip_actualizacion");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA);
   consultaSQL.append(" t1 ");
   consultaSQL.append(sqlWhere);
   consultaSQL.append(sqlOrderBy);
   consultaSQL.append(sqlLimit);
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), parametros.toArray(), new CargaTanqueMapper());
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

 public RespuestaCompuesta recuperarRegistro(int ID) {
  StringBuilder consultaSQL = new StringBuilder();
  List<CargaTanque> listaRegistros = new ArrayList<CargaTanque>();
  Contenido<CargaTanque> contenido = new Contenido<CargaTanque>();
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  try {
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_ctanque,");
   consultaSQL.append("t1.id_tanque,");
   consultaSQL.append("t1.nombre_tanque,");
//   consultaSQL.append("t1.tipo_tanque,");  
   consultaSQL.append("t1.id_estacion,");
   consultaSQL.append("t1.nombre_estacion,");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.fecha_hora_inicio,");
   consultaSQL.append("t1.fecha_hora_fin,");
   consultaSQL.append("t1.altura_inicial_producto,");
   consultaSQL.append("t1.altura_final_producto,");
   consultaSQL.append("t1.temperatura_inicial_centro,");
   consultaSQL.append("t1.temperatura_final_centro,");
   consultaSQL.append("t1.temperatura_inicial_probeta,");
   consultaSQL.append("t1.temperatura_final_probeta,");
   consultaSQL.append("t1.api_observado_inicial,");
   consultaSQL.append("t1.api_observado_final,");
   consultaSQL.append("t1.factor_correccion_inicial,");
   consultaSQL.append("t1.factor_correccion_final,");
   consultaSQL.append("t1.volumen_observado_inicial,");
   consultaSQL.append("t1.volumen_observado_final,");
   consultaSQL.append("t1.volumen_corregido_inicial,");
   consultaSQL.append("t1.volumen_corregido_final,");
   // Campos de auditoria
   consultaSQL.append("t1.creado_el,");
   consultaSQL.append("t1.creado_por,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.usuario_creacion,");
   consultaSQL.append("t1.usuario_actualizacion,");
   consultaSQL.append("t1.ip_creacion,");
   consultaSQL.append("t1.ip_actualizacion");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA);
   consultaSQL.append(" t1 ");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=?");
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new Object[] { ID }, new CargaTanqueMapper());
   contenido.totalRegistros = listaRegistros.size();
   contenido.totalEncontrados = listaRegistros.size();
   contenido.carga = listaRegistros;
   respuesta.mensaje = "OK";
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

 public RespuestaCompuesta guardarRegistro(CargaTanque carga_tanque) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  KeyHolder claveGenerada = null;
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("INSERT INTO ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL
     .append(" (id_tanque,id_doperativo,id_estacion,fecha_hora_inicio,fecha_hora_fin,altura_inicial_producto,altura_final_producto,temperatura_inicial_centro,temperatura_final_centro,temperatura_inicial_probeta,temperatura_final_probeta,api_observado_inicial,api_observado_final,factor_correccion_inicial,factor_correccion_final,volumen_observado_inicial,volumen_observado_final,volumen_corregido_inicial,volumen_corregido_final,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
   consultaSQL
     .append(" VALUES (:IdTanque,:IdDiaOperativo,:IdEstacion,:FechaHoraInicial,:FechaHoraFinal,:AlturaInicial,:AlturaFinal,:TemperaturaInicialCentro,:TemperaturaFinalCentro,:TemperaturaIniciaProbeta,:TemperaturaFinalProbeta,:ApiObservadoInicial,:ApiObservadoFinal,:FactorCorreccionInicial,:FactorCorreccionFinal,:VolumenObservadoInicial,:VolumenObservadoFinal,:VolumenCorregidoInicial,:VolumenCorregidoFinal,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("IdTanque", carga_tanque.getIdTanque());
   listaParametros.addValue("IdDiaOperativo", carga_tanque.getIdDiaOperativo());
   listaParametros.addValue("IdEstacion", carga_tanque.getIdEstacion());
   listaParametros.addValue("FechaHoraInicial", carga_tanque.getFechaHoraInicial());
   listaParametros.addValue("FechaHoraFinal", carga_tanque.getFechaHoraFinal());
   listaParametros.addValue("AlturaInicial", carga_tanque.getAlturaInicial());
   listaParametros.addValue("AlturaFinal", carga_tanque.getAlturaFinal());
   listaParametros.addValue("TemperaturaInicialCentro", carga_tanque.getTemperaturaInicialCentro());
   listaParametros.addValue("TemperaturaFinalCentro", carga_tanque.getTemperaturaFinalCentro());
   listaParametros.addValue("TemperaturaIniciaProbeta", carga_tanque.getTemperaturaIniciaProbeta());
   listaParametros.addValue("TemperaturaFinalProbeta", carga_tanque.getTemperaturaFinalProbeta());
   listaParametros.addValue("ApiObservadoInicial", carga_tanque.getApiObservadoInicial());
   listaParametros.addValue("ApiObservadoFinal", carga_tanque.getApiObservadoFinal());
   listaParametros.addValue("FactorCorreccionInicial", carga_tanque.getFactorCorreccionInicial());
   listaParametros.addValue("FactorCorreccionFinal", carga_tanque.getFactorCorreccionFinal());
   listaParametros.addValue("VolumenObservadoInicial", carga_tanque.getVolumenObservadoInicial());
   listaParametros.addValue("VolumenObservadoFinal", carga_tanque.getVolumenObservadoFinal());
   listaParametros.addValue("VolumenCorregidoInicial", carga_tanque.getVolumenCorregidoInicial());
   listaParametros.addValue("VolumenCorregidoFinal", carga_tanque.getVolumenCorregidoFinal());
   listaParametros.addValue("CreadoEl", carga_tanque.getCreadoEl());
   listaParametros.addValue("CreadoPor", carga_tanque.getCreadoPor());
   listaParametros.addValue("ActualizadoPor", carga_tanque.getActualizadoPor());
   listaParametros.addValue("ActualizadoEl", carga_tanque.getActualizadoEl());
   listaParametros.addValue("IpCreacion", carga_tanque.getIpCreacion());
   listaParametros.addValue("IpActualizacion", carga_tanque.getIpActualizacion());

   SqlParameterSource namedParameters = listaParametros;
   /* Ejecuta la consulta y retorna las filas afectadas */
   claveGenerada = new GeneratedKeyHolder();
   System.out.println(consultaSQL.toString());
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

 public RespuestaCompuesta actualizarRegistro(CargaTanque carga_tanque) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("id_tanque=:IdTanque,");
   consultaSQL.append("fecha_hora_inicio=:FechaHoraInicial,");
   consultaSQL.append("fecha_hora_fin=:FechaHoraFinal,");
   consultaSQL.append("altura_inicial_producto=:AlturaInicial,");
   consultaSQL.append("altura_final_producto=:AlturaFinal,");
   consultaSQL.append("temperatura_inicial_centro=:TemperaturaInicialCentro,");
   consultaSQL.append("temperatura_final_centro=:TemperaturaFinalCentro,");
   consultaSQL.append("temperatura_inicial_probeta=:TemperaturaIniciaProbeta,");
   consultaSQL.append("temperatura_final_probeta=:TemperaturaFinalProbeta,");
   consultaSQL.append("api_observado_inicial=:ApiObservadoInicial,");
   consultaSQL.append("api_observado_final=:ApiObservadoFinal,");
   consultaSQL.append("factor_correccion_inicial=:FactorCorreccionInicial,");
   consultaSQL.append("factor_correccion_final=:FactorCorreccionFinal,");
   consultaSQL.append("volumen_observado_inicial=:VolumenObservadoInicial,");
   consultaSQL.append("volumen_observado_final=:VolumenObservadoFinal,");
   consultaSQL.append("volumen_corregido_inicial=:VolumenCorregidoInicial,");
   consultaSQL.append("volumen_corregido_final=:VolumenCorregidoFinal,");
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:Id");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("IdTanque", carga_tanque.getIdTanque());
   listaParametros.addValue("FechaHoraInicial", carga_tanque.getFechaHoraInicial());
   listaParametros.addValue("FechaHoraFinal", carga_tanque.getFechaHoraFinal());
   listaParametros.addValue("AlturaInicial", carga_tanque.getAlturaInicial());
   listaParametros.addValue("AlturaFinal", carga_tanque.getAlturaFinal());
   listaParametros.addValue("TemperaturaInicialCentro", carga_tanque.getTemperaturaInicialCentro());
   listaParametros.addValue("TemperaturaFinalCentro", carga_tanque.getTemperaturaFinalCentro());
   listaParametros.addValue("TemperaturaIniciaProbeta", carga_tanque.getTemperaturaIniciaProbeta());
   listaParametros.addValue("TemperaturaFinalProbeta", carga_tanque.getTemperaturaFinalProbeta());
   listaParametros.addValue("ApiObservadoInicial", carga_tanque.getApiObservadoInicial());
   listaParametros.addValue("ApiObservadoFinal", carga_tanque.getApiObservadoFinal());
   listaParametros.addValue("FactorCorreccionInicial", carga_tanque.getFactorCorreccionInicial());
   listaParametros.addValue("FactorCorreccionFinal", carga_tanque.getFactorCorreccionFinal());
   listaParametros.addValue("VolumenObservadoInicial", carga_tanque.getVolumenObservadoInicial());
   listaParametros.addValue("VolumenObservadoFinal", carga_tanque.getVolumenObservadoFinal());
   listaParametros.addValue("VolumenCorregidoInicial", carga_tanque.getVolumenCorregidoInicial());
   listaParametros.addValue("VolumenCorregidoFinal", carga_tanque.getVolumenCorregidoFinal());
   // Valores Auditoria
   listaParametros.addValue("ActualizadoEl", carga_tanque.getActualizadoEl());
   listaParametros.addValue("ActualizadoPor", carga_tanque.getActualizadoPor());
   listaParametros.addValue("IpActualizacion", carga_tanque.getIpActualizacion());
   listaParametros.addValue("Id", carga_tanque.getId());
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

 public RespuestaCompuesta eliminarRegistro(int idRegistro) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  int cantidadFilasAfectadas = 0;
  String consultaSQL = "";
  Object[] parametros = { idRegistro };
  try {
   consultaSQL = "DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
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
}