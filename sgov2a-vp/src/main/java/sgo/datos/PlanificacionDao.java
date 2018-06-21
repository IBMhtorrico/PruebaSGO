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

import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Producto;
import sgo.entidad.ProductoProgramacion;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

/**
 * Funcionalidades para el modulo de Planificación.
 * 
 * @author I.B.M. DEL PERÚ - knavarro
 * @since 13/XIII/2015 Modificado por Rafael Reyna Camones
 */
@Repository
public class PlanificacionDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "planificacion";
 public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_planificacion";
 public static final String NOMBRE_CAMPO_CLAVE = "id_planificacion";
 public final static String NOMBRE_CAMPO_FILTRO = "id_planificacion";
 public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_planificacion";
 public static final String NOMBRE_VISTA_REPORTE = Constante.ESQUEMA_APLICACION + "v_planificacion_dia_operativo";
 /** Nombre de la clase. */
 private static final String sNombreClase = "PlanificacionDao";
 @Autowired
 public void setDataSource(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
  this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }

 public DataSource getDataSource() {
  return this.jdbcTemplate.getDataSource();
 }

 public String mapearCampoOrdenamiento(String propiedad) {
  String campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
  try {
   if (propiedad.equals("id")) {
    campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
   }
   if (propiedad.equals("idProducto")) {
    campoOrdenamiento = "id_producto";
   }
   if (propiedad.equals("idDoperativo")) {
    campoOrdenamiento = "id_doperativo";
   }
   if (propiedad.equals("volumenPropuesto")) {
    campoOrdenamiento = "volumen_propuesto";
   }
   if (propiedad.equals("volumenSolicitado")) {
    campoOrdenamiento = "volumen_solicitado";
   }
   if (propiedad.equals("cantidadCisternas")) {
    campoOrdenamiento = "cantidad_cisternas";
   }
   // Campos de auditoria
  } catch (Exception excepcion) {

  }
  return campoOrdenamiento;
 }

 // jmatos
 public List<ProductoProgramacion> recuperarRegistrosPorProgramacion(ParametrosListar parametros){
	 StringBuilder consultaSQL = new StringBuilder();
	 List<ProductoProgramacion> listaRegistros = new ArrayList<ProductoProgramacion>();
	 List<Object> valores = new ArrayList<Object>();
	 if(parametros.getIdOperacion() > 0){
		 valores.add(parametros.getIdOperacion());
	 }
	 try {
		 consultaSQL.append("SELECT ");
		 consultaSQL.append("distinct t1.id_producto, ");
		 consultaSQL.append("t1.id_operacion, ");
		 consultaSQL.append("t1.id_cliente, ");
		 consultaSQL.append("t1.nombre, ");
		 consultaSQL.append("t1.razon_social, ");
		 consultaSQL.append("t1.abreviatura, ");
		 consultaSQL.append("t1.actualizado_por, ");
		 consultaSQL.append("t1.usuario_actualizacion, ");
		 consultaSQL.append("t1.ip_actualizacion ");
		 consultaSQL.append(" from sgo.v_productos_programacion t1 ");
		 consultaSQL.append(" where ");
		 consultaSQL.append(" t1.id_operacion = ?");
		 consultaSQL.append(" order by t1.id_cliente, t1.id_producto ");
		 listaRegistros = jdbcTemplate.query(consultaSQL.toString(), valores.toArray(), new ProductoProgramacionMapper());
		 Utilidades.gestionaTrace(sNombreClase, "recuperarRegistros");
	} catch (DataAccessException excepcionAccesoDatos) {
		Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistros", consultaSQL.toString());
	}
	 return listaRegistros;
 }
 
 // jmatos
 
 
 public RespuestaCompuesta recuperarRegistros(ParametrosListar parametros) {
  StringBuilder consultaSQL = new StringBuilder();
  List<Planificacion> listaRegistros = new ArrayList<Planificacion>();
  Contenido<Planificacion> contenido = new Contenido<Planificacion>();
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlOrderBy = "";
  String sqlWhere = "";
  List<Object> valores = new ArrayList<Object>();
  try {
   sqlOrderBy = Constante.SQL_ORDEN + this.mapearCampoOrdenamiento(parametros.getCampoOrdenamiento()) + " " + parametros.getSentidoOrdenamiento();

   filtrosWhere.add(" t1.id_producto = pro.id_producto ");
   filtrosWhere.add("  pro.indicador_producto <> " + Producto.INDICADOR_PRODUCTO_SIN_DATOS); 

   if (parametros.getFiltroDiaOperativo() > 0) {
    filtrosWhere.add(" t1.id_doperativo = ? ");
    valores.add(parametros.getFiltroDiaOperativo());
   }

   if (!parametros.getFiltroFechaPlanificada().isEmpty()) {
    filtrosWhere.add(" t1.fecha_operativa ='" + parametros.getFiltroFechaPlanificada() + "' ");
   }

   if (!filtrosWhere.isEmpty()) {
    consultaSQL.setLength(0);
    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
   }

   consultaSQL.setLength(0);
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.id_planificacion,");
   consultaSQL.append("t1.id_producto,");
   consultaSQL.append("t1.nombre,");
   consultaSQL.append("t1.volumen_propuesto,");
   consultaSQL.append("t1.volumen_solicitado,");
   consultaSQL.append("t1.cantidad_cisternas,");
   consultaSQL.append("t1.observacion,");
   consultaSQL.append("t1.bitacora,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.usuario_actualizacion,");
   consultaSQL.append("t1.ip_actualizacion");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA);
   consultaSQL.append(" t1, sgo.producto pro ");
   consultaSQL.append(sqlWhere);
   consultaSQL.append(sqlOrderBy);
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), valores.toArray(), new PlanificacionMapper());
   contenido.totalRegistros = listaRegistros.size();
   contenido.totalEncontrados = listaRegistros.size();
   contenido.carga = listaRegistros;
   respuesta.mensaje = "OK";
   respuesta.estado = true;
   respuesta.contenido = contenido;
   Utilidades.gestionaTrace(sNombreClase, "recuperarRegistros");
  } catch (DataAccessException excepcionAccesoDatos) {
   //excepcionAccesoDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistros", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  }
  return respuesta;
 }

 public RespuestaCompuesta recuperarRegistro(int ID) {
  StringBuilder consultaSQL = new StringBuilder();
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<Planificacion> contenido = new Contenido<Planificacion>();
  List<Planificacion> listaRegistros = new ArrayList<Planificacion>();
  if (!Utilidades.esValido(ID)) {
   respuesta.estado = false;
   respuesta.contenido = null;
   return respuesta;
  }
  try {
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.id_planificacion,");
   consultaSQL.append("t1.id_producto,");
   consultaSQL.append("t1.nombre,");
   consultaSQL.append("t1.volumen_propuesto,");
   consultaSQL.append("t1.volumen_solicitado,");
   consultaSQL.append("t1.cantidad_cisternas,");
   consultaSQL.append("t1.observacion,");
   consultaSQL.append("t1.bitacora,");
   // Campos de auditoria
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.actualizado_por,");
   consultaSQL.append("t1.usuario_actualizacion,");
   consultaSQL.append("t1.ip_actualizacion");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA);
   consultaSQL.append(" t1, sgo.producto pro ");
   consultaSQL.append(" WHERE t1.");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append(" = ? ");
   consultaSQL.append(" and t1.id_producto = pro.id_producto ");
   consultaSQL.append(" and pro.indicador_producto <> " + Producto.INDICADOR_PRODUCTO_SIN_DATOS);   
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new Object[] { ID }, new PlanificacionMapper());
   contenido.totalRegistros = listaRegistros.size();
   contenido.totalEncontrados = listaRegistros.size();
   contenido.carga = listaRegistros;
   respuesta.mensaje = "OK";
   respuesta.estado = true;
   respuesta.contenido = contenido;
   Utilidades.gestionaTrace(sNombreClase, "recuperarRegistro");
  } catch (DataAccessException excepcionAccesoDatos) {
   //excepcionAccesoDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  }
  return respuesta;
 }

 /**
  * Metodo para guardar la entidad Planificacion.
  * 
  * @param planificacion
  *         Entidad que se va a insertar.
  * @return respuesta resultado de la inserción.
  */
 public RespuestaCompuesta guardarRegistro(Planificacion planificacion) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  KeyHolder claveGenerada = null;
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("INSERT INTO ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" (id_doperativo,id_producto,volumen_propuesto,volumen_solicitado,cantidad_cisternas,observacion,bitacora,actualizado_por,actualizado_el,ip_actualizacion) ");
   consultaSQL.append(" VALUES (:IdDoperativo,:IdProducto,:VolumenPropuesto,:VolumenSolicitado,:CantidadCisternas,:Observacion,:Bitacora,:ActualizadoPor,:ActualizadoEl,:IpActualizacion) ");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("IdDoperativo", planificacion.getIdDoperativo());
   listaParametros.addValue("IdProducto", planificacion.getIdProducto());
   listaParametros.addValue("VolumenPropuesto", planificacion.getVolumenPropuesto());
   listaParametros.addValue("VolumenSolicitado", planificacion.getVolumenSolicitado());
   listaParametros.addValue("CantidadCisternas", planificacion.getCantidadCisternas());
   listaParametros.addValue("Observacion", planificacion.getObservacion());
   listaParametros.addValue("Bitacora", planificacion.getBitacora());
   // datos auditoria
   listaParametros.addValue("ActualizadoPor", planificacion.getActualizadoPor());
   listaParametros.addValue("ActualizadoEl", planificacion.getActualizadoEl());
   listaParametros.addValue("IpActualizacion", planificacion.getIpActualizacion());
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
   Utilidades.gestionaTrace(sNombreClase, "guardarRegistro");
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
 //excepcionIntegridadDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionIntegridadDatos, sNombreClase, "guardarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  
  } catch (DataAccessException excepcionAccesoDatos) {
   //excepcionAccesoDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "guardarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 /**
  * Metodo para actualizar la entidad Planificacion.
  * 
  * @param planificacion
  *         Entidad que se va a modificar.
  * @return respuesta resultado de la modificación.
  */
 public RespuestaCompuesta actualizarRegistro(Planificacion planificacion) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("id_producto=:IdProducto,");
   consultaSQL.append("volumen_propuesto=:VolumenPropuesto,");
   consultaSQL.append("cantidad_cisternas=:CantidadCisternas,");
   consultaSQL.append("volumen_solicitado=:VolumenSolicitado,");
   consultaSQL.append("observacion=:Observacion,");
   consultaSQL.append("bitacora=:Bitacora,");
   // Datos auditoria
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:idPlanificacion ");
   consultaSQL.append(Constante.SQL_Y);
   consultaSQL.append(" id_doperativo ");
   consultaSQL.append("=:idDoperacion ");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("IdProducto", planificacion.getIdProducto());
   listaParametros.addValue("VolumenPropuesto", planificacion.getVolumenPropuesto());
   listaParametros.addValue("CantidadCisternas", planificacion.getCantidadCisternas());
   listaParametros.addValue("VolumenSolicitado", planificacion.getVolumenSolicitado());
   listaParametros.addValue("Observacion", planificacion.getObservacion());
   listaParametros.addValue("Bitacora", planificacion.getBitacora());
   // Valores Auditoria
   listaParametros.addValue("ActualizadoEl", planificacion.getActualizadoEl());
   listaParametros.addValue("ActualizadoPor", planificacion.getActualizadoPor());
   listaParametros.addValue("IpActualizacion", planificacion.getIpActualizacion());
   listaParametros.addValue("idPlanificacion", planificacion.getId());
   listaParametros.addValue("idDoperacion", planificacion.getIdDoperativo());

   SqlParameterSource namedParameters = listaParametros;
   /* Ejecuta la consulta y retorna las filas afectadas */
   cantidadFilasAfectadas = namedJdbcTemplate.update(consultaSQL.toString(), namedParameters);
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
   Utilidades.gestionaTrace(sNombreClase, "actualizarRegistro");
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   //excepcionIntegridadDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionIntegridadDatos, sNombreClase, "actualizarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   //excepcionAccesoDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "actualizarRegistro", consultaSQL.toString());
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

  if (!Utilidades.esValido(idRegistro)) {
   respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
   respuesta.estado = false;
   return respuesta;
  }
  try {
   consultaSQL = "DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
   cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
   if (cantidadFilasAfectadas > 1) {
    respuesta.error = Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
    respuesta.estado = false;
    return respuesta;
   }
   respuesta.estado = true;
   Utilidades.gestionaTrace(sNombreClase, "eliminarRegistro");
  } catch (DataIntegrityViolationException excepcionIntegridadDatos) {
   //excepcionIntegridadDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionIntegridadDatos, sNombreClase, "eliminarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_INTEGRIDAD_DATOS;
   respuesta.estado = false;
  } catch (DataAccessException excepcionAccesoDatos) {
   //excepcionAccesoDatos.printStackTrace();
   Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "eliminarRegistro", consultaSQL.toString());
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  }
  return respuesta;
 }

 /**
  * Metodo para eliminar todas las planificaciones de un día operativo.
  * 
  * @param idDiaOperativo
  *         Identificador del día operativo.
  * @return respuesta Contiene el valor de los registros eliminados.
  */
 public Respuesta eliminarRegistrosPorDiaOperativo(int idDiaOperativo) {
  Respuesta respuesta = new Respuesta();
  String consultaSQL = "";
  Object[] parametros = { idDiaOperativo };
  try {
   consultaSQL = "DELETE FROM " + NOMBRE_TABLA + " WHERE id_doperativo = ?";
   int registrosEliminados = jdbcTemplate.update(consultaSQL, parametros);
    respuesta.estado = true;
    respuesta.valor = String.valueOf(registrosEliminados);
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
  * Metodo para cuenta todas las planificaciones de un día operativo.
  * 
  * @param idDiaOperativo
  *         Identificador del día operativo.
  * @return respuesta Número de planificaciones que existen del día operativo. .
  */
 public Respuesta numeroRegistrosPorDiaOperativo(int idDiaOperativo) {
  Respuesta respuesta = new Respuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadRegistros = 0;

  try {
   consultaSQL = new StringBuilder();
   consultaSQL.append("SELECT ");
   consultaSQL.append(" count(*) ");
   consultaSQL.append(" FROM ");
   consultaSQL.append(" sgo.planificacion ");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(" id_doperativo = ");
   consultaSQL.append(idDiaOperativo);

   cantidadRegistros = jdbcTemplate.queryForInt(consultaSQL.toString());
   respuesta.valor = String.valueOf(cantidadRegistros);
   respuesta.estado = true;

  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.estado = false;
  }

  return respuesta;
 }
 
 public RespuestaCompuesta recuperarRegistroXDiaOperativoYProducto(int IdDiaOperativo, int idProducto) {
	  StringBuilder consultaSQL = new StringBuilder();
	  RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<Planificacion> contenido = new Contenido<Planificacion>();
	  List<Planificacion> listaRegistros = new ArrayList<Planificacion>();
	  List<Object> parametros = new ArrayList<Object>();

	  try {
	   parametros.add(IdDiaOperativo);
	   parametros.add(idProducto);
	   
	   consultaSQL.append("SELECT ");
	   consultaSQL.append("t1.id_doperativo,");
	   consultaSQL.append("t1.id_planificacion,");
	   consultaSQL.append("t1.id_producto,");
	   consultaSQL.append("t1.nombre,");
	   consultaSQL.append("t1.volumen_propuesto,");
	   consultaSQL.append("t1.volumen_solicitado,");
	   consultaSQL.append("t1.cantidad_cisternas,");
	   consultaSQL.append("t1.observacion,");
	   consultaSQL.append("t1.bitacora,");
	   // Campos de auditoria
	   consultaSQL.append("t1.actualizado_el,");
	   consultaSQL.append("t1.actualizado_por,");
	   consultaSQL.append("t1.usuario_actualizacion,");
	   consultaSQL.append("t1.ip_actualizacion");
	   consultaSQL.append(" FROM ");
	   consultaSQL.append(NOMBRE_VISTA);
	   consultaSQL.append(" t1 ");
	   consultaSQL.append(" WHERE t1.id_doperativo = ?");
	   consultaSQL.append(" AND id_producto = ? ");
	   
	   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new PlanificacionMapper());
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
 
 public RespuestaCompuesta ActualizarEstadoRegistro(Planificacion entidad) {
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
 public Respuesta recuperaPromedioDescargaPorProducto(ParametrosListar argumentosListar) {
  Long promedio = null;
  StringBuilder consultaSQL = new StringBuilder();
  Respuesta respuesta = new Respuesta();
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlWhere = "";
  List<Object> parametros = new ArrayList<Object>();
  try {
		if (argumentosListar.getFiltroOperacion() > 0){
			filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
		}
		if (argumentosListar.getFiltroProducto() > 0){
			filtrosWhere.add(" t2.id_producto = " + argumentosListar.getFiltroProducto() +" ");
		}
	    if (!argumentosListar.getFiltroFechaInicio().isEmpty() && !argumentosListar.getFiltroFechaFinal().isEmpty()) {
	     filtrosWhere.add(" t1.fecha_operativa " + Constante.SQL_ENTRE + ("'" + argumentosListar.getFiltroFechaInicio() + "'" + Constante.SQL_Y + "'" + argumentosListar.getFiltroFechaFinal() + "'"));
	    }
		
		if (!filtrosWhere.isEmpty()) {
			consultaSQL.setLength(0);
			sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
		}

	   consultaSQL.setLength(0);
	   consultaSQL.append("SELECT ");
	   consultaSQL.append(" avg(t1.volumen_total_descargado_observado) ");
	   consultaSQL.append(" from sgo.v_descarga_base t1 ");
	   consultaSQL.append(" JOIN sgo.v_descarga_compartimento t2 on t1.id_dcisterna = t2.id_dcisterna ");
	   consultaSQL.append(sqlWhere);

	   promedio = jdbcTemplate.queryForObject(consultaSQL.toString(), parametros.toArray() , Long.class);
	   
	   if (promedio == null) {
		   respuesta.estado = true;
		   respuesta.valor = null;
	   } else {
		   respuesta.estado = true;
		   respuesta.valor = promedio.toString();
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

public RespuestaCompuesta recuperarRegistrosReporte(ParametrosListar argumentosListar) {
	  StringBuilder consultaSQL = new StringBuilder();
	  RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<Planificacion> contenido = new Contenido<Planificacion>();
	  List<Planificacion> listaRegistros = new ArrayList<Planificacion>();
	  List<Object> parametros = new ArrayList<Object>();
	  List<String> filtrosWhere = new ArrayList<String>();
	  String sqlWhere="";
	  String sqlOrderBy = "";
	  try {
		    sqlOrderBy = Constante.SQL_ORDEN + "t1.fecha_planificada desc ";
		  
		    if (!argumentosListar.getFiltroFechaInicio().isEmpty() && !argumentosListar.getFiltroFechaFinal().isEmpty()) {
			     filtrosWhere.add(" t1.fecha_planificada " + Constante.SQL_ENTRE + ("'" + argumentosListar.getFiltroFechaInicio() + "'" + Constante.SQL_Y + "'" + argumentosListar.getFiltroFechaFinal() + "'"));
			}
			if (argumentosListar.getFiltroOperacion() > 0){
				filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
				//parametros.add(argumentosListar.getFiltroOperacion());
			}
	   
		   if (!filtrosWhere.isEmpty()) {
			    consultaSQL.setLength(0);
			    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
			}
		   
	 //  sqlOrderBy = " ORDER BY t1.fecha_planificada, t1.id_operacion, t1.razon_social DESC";  
	   
	   
	   consultaSQL.append("SELECT ");
	   consultaSQL.append("t1.id_doperativo,");
	   consultaSQL.append("t1.fecha_planificada,");
	   consultaSQL.append("t1.fecha_carga,");
	   consultaSQL.append("t1.id_cliente,");
	   consultaSQL.append("t1.cliente_nombre,");
	   consultaSQL.append("t1.razon_social,");
	   consultaSQL.append("t1.id_operacion,");
	   consultaSQL.append("t1.nombre_operacion,");
	   consultaSQL.append("t1.id_planificacion,");
	   consultaSQL.append("t1.id_producto,");
	   consultaSQL.append("t1.nombre_producto,");	   
	   consultaSQL.append("t1.volumen_propuesto,");
	   consultaSQL.append("t1.volumen_solicitado,");
	   consultaSQL.append("t1.cantidad_cisternas,");	   
	   consultaSQL.append("t1.actualizado_el,");	   
	   consultaSQL.append("t1.actualizado_por,");
	   consultaSQL.append("t1.observacion,");
	   consultaSQL.append("t1.bitacora,");   
	   consultaSQL.append("t1.correoPara,");
	   consultaSQL.append("t1.correoCC,");
	   consultaSQL.append("t1.eta_origen");
	   consultaSQL.append(" from ");
	   consultaSQL.append(NOMBRE_VISTA_REPORTE);
	   consultaSQL.append(" t1 ");
	   consultaSQL.append(sqlWhere);
	   consultaSQL.append(sqlOrderBy);
	   
	   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new ReportePlanificacionMapper());
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
 
}