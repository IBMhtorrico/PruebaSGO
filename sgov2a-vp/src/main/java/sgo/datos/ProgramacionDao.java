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
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DiaOperativo;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Programacion;
import sgo.entidad.ProgramacionPlanificada;
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
public class ProgramacionDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "programacion";
 public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_dia_operativo_programacion";
 public static final String NOMBRE_VISTA_REPORTE = Constante.ESQUEMA_APLICACION + "v_reporte_programacion";
 
 public static final String NOMBRE_VISTA_DETALLE = Constante.ESQUEMA_APLICACION + "v_programacion";
 public static final String NOMBRE_CAMPO_CLAVE = "id_programacion"; 
 public final static String NOMBRE_CAMPO_FILTRO = "id_doperativo";
 public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_doperativo";
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

public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy="";
		List<String> filtrosWhere= new ArrayList<String>();
		String sqlWhere="";
		int totalRegistros = 0, 
		totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<DiaOperativo> contenido = new Contenido<DiaOperativo>();
		List<DiaOperativo> listaRegistros = new ArrayList<DiaOperativo>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
			
			//sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento("id_doperativo") + " "  + argumentosListar.getSentidoOrdenamiento();
			sqlOrderBy = Constante.SQL_ORDEN + FECHA_OPERATIVA + " desc ";
			
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(id_doperativo) as total FROM " + NOMBRE_VISTA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados=totalRegistros;

			
			if (!argumentosListar.getValorBuscado().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
//			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
//				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
//			}
		   if (argumentosListar.getFiltroOperacion() != Constante.FILTRO_TODOS) {
			    filtrosWhere.add(" t1.id_operacion = "
			      + argumentosListar.getFiltroOperacion());
			   }
			
		   if (argumentosListar.getFiltroDiaOperativo() != Constante.FILTRO_TODOS) {
			    filtrosWhere.add(" t1.id_doperativo = " + argumentosListar.getFiltroDiaOperativo());
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
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1.id_doperativo) as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

		   consultaSQL.setLength(0);
		   consultaSQL.append("SELECT ");
		   consultaSQL.append("t1.id_doperativo,");
		   consultaSQL.append("t1.estado,");
		   consultaSQL.append("t1.creado_el,");
		   consultaSQL.append("t1.creado_por,");
		   consultaSQL.append("t1.actualizado_por,");
		   consultaSQL.append("t1.actualizado_el,");
		   consultaSQL.append("t1.ip_creacion,");
		   consultaSQL.append("t1.ip_actualizacion,");
		   consultaSQL.append("t1.fecha_operativa,");
		   consultaSQL.append("t1.id_operacion,");
		   consultaSQL.append("t1.nombre_usuario_creador,");   
		   consultaSQL.append("t1.nombre_usuario_actualizador,");
		//   consultaSQL.append("t1.identidad_usuario_creador");
		//   consultaSQL.append("t1.identidad_usuario_actualizador");
		   consultaSQL.append("t1.total_cisternas_pla,");
		   consultaSQL.append("t1.fecha_estimada_carga,");
		   consultaSQL.append("t1.total_cisternas_prog,");
		   consultaSQL.append("t1.nombre_operacion,");
		   consultaSQL.append("t1.id_cliente,");
		   consultaSQL.append("t1.razon_social_cliente,");
		   consultaSQL.append("t1.planta_despacho_defecto,");
		   consultaSQL.append("t1.descripcion_planta_despacho");
		   consultaSQL.append(" FROM ");
		   consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");	
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ProgramacionDiaOpeMapper());
		
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
 
 
public RespuestaCompuesta recuperaProgramacionDetalle(ParametrosListar argumentosListar) {
	 //modificar abarrios
 StringBuilder consultaSQL = new StringBuilder();
 RespuestaCompuesta respuesta = new RespuestaCompuesta();
 Contenido<Programacion> contenido = new Contenido<Programacion>();
 List<String> filtrosWhere= new ArrayList<String>();
 List<Programacion> listaRegistros = new ArrayList<Programacion>();
 List<Object> parametros = new ArrayList<Object>();
 String sqlWhere = "";
 try {	  
	 
	 
	   if (argumentosListar.getIdTransportista() > 0) {
		    filtrosWhere.add(" t1.id_transportista = "
		      + argumentosListar.getIdTransportista());
		}
	   
	   if (argumentosListar.getFiltroDiaOperativo() > 0) {
		    filtrosWhere.add(" t1.id_doperativo = "
		      + argumentosListar.getFiltroDiaOperativo());
		}
	   
	   
		if (!filtrosWhere.isEmpty()) {
			consultaSQL.setLength(0);
			sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
		}
	 
  consultaSQL.append("SELECT ");
  consultaSQL.append("t1.id_doperativo,");
  consultaSQL.append("t1.id_programacion,");
  consultaSQL.append("t1.id_transportista,");
  consultaSQL.append("t1.razon_social,");
  consultaSQL.append("t1.estado,");
  consultaSQL.append("t1.actualizado_el,");
  consultaSQL.append("t1.usuario_actualizacion,");
  consultaSQL.append("t1.candidadcisternas,");
  consultaSQL.append("t1.comentario");
  consultaSQL.append(" FROM ");
  consultaSQL.append(NOMBRE_VISTA_DETALLE);
  consultaSQL.append(" t1 ");
  consultaSQL.append(sqlWhere);

  listaRegistros = jdbcTemplate.query(consultaSQL.toString(), parametros.toArray(), new ProgramacionMapper());
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

 public RespuestaCompuesta recuperaProgramacionDetalle(int ID) {
	 //modificar abarrios
  StringBuilder consultaSQL = new StringBuilder();
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<Programacion> contenido = new Contenido<Programacion>();
  List<Programacion> listaRegistros = new ArrayList<Programacion>();
  if (!Utilidades.esValido(ID)) {
   respuesta.estado = false;
   respuesta.contenido = null;
   return respuesta;
  }
  try {	  
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id_doperativo,");
   consultaSQL.append("t1.id_programacion,");
   consultaSQL.append("t1.id_transportista,");
   consultaSQL.append("t1.razon_social,");
   consultaSQL.append("t1.estado,");
   consultaSQL.append("t1.actualizado_el,");
   consultaSQL.append("t1.usuario_actualizacion,");
   consultaSQL.append("t1.candidadcisternas,");
   consultaSQL.append("t1.comentario");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA_DETALLE);
   consultaSQL.append(" t1 ");
   consultaSQL.append(" WHERE t1.");
   consultaSQL.append(NOMBRE_CAMPO_FILTRO);
   consultaSQL.append("=?");
 
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new Object[] { ID }, new ProgramacionMapper());
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
 
 

 
 

 /**
  * Metodo para guardar la entidad Planificacion.
  * 
  * @param planificacion
  *         Entidad que se va a insertar.
  * @return respuesta resultado de la inserción.
  */
 public RespuestaCompuesta guardarRegistro(Programacion programacion ) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  KeyHolder claveGenerada = null;
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("INSERT INTO ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" (id_transportista,estado,id_doperativo,comentario,creado_el,creado_por,ip_creacion,actualizado_por,ip_actualizacion,actualizado_el) ");
   consultaSQL.append(" VALUES (:IdTransportista,:Estado,:IdDoperativo,:Comentario,:CreadoEl,:CreadoPor,:IpCreacion,:ActualizadoPor,:IpActualizacion,:ActualizadoEl) ");
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("IdTransportista", programacion.getIdTransportista());
   listaParametros.addValue("Estado", programacion.getEstado());
   listaParametros.addValue("IdDoperativo", programacion.getIdDiaOperativo());
   listaParametros.addValue("Comentario", programacion.getComentario());
   // datos auditoria
   listaParametros.addValue("CreadoEl", programacion.getCreadoEl());
   listaParametros.addValue("CreadoPor", programacion.getCreadoPor());
   listaParametros.addValue("IpCreacion", programacion.getIpCreacion());
   listaParametros.addValue("ActualizadoPor", programacion.getActualizadoPor());
   listaParametros.addValue("IpActualizacion", programacion.getIpActualizacion());
   listaParametros.addValue("ActualizadoEl", programacion.getActualizadoEl());
   
   
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

 /**
  * Metodo para actualizar la entidad Planificacion.
  * 
  * @param planificacion
  *         Entidad que se va a modificar.
  * @return respuesta resultado de la modificación.
  */
 public RespuestaCompuesta actualizarRegistro(Programacion programacion) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("estado=:Estado,");
   // Datos auditoria
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:idProgramacion ");
   
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("Estado", programacion.getEstado());
   
   // Valores Auditoria
   listaParametros.addValue("ActualizadoPor", programacion.getActualizadoPor());
   listaParametros.addValue("ActualizadoEl", programacion.getActualizadoEl());   
   listaParametros.addValue("IpActualizacion", programacion.getIpActualizacion());
   listaParametros.addValue("idProgramacion", programacion.getId());

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
 
 /** Inicio Ticket 9000002608**/
 public ProgramacionPlanificada recuperarCabeceraProgramacion(ParametrosListar argumentosListar){
	 StringBuilder consultaSQL = new StringBuilder();
	 
	 ProgramacionPlanificada programacionPlanificada = null;
	 
	 List<ProgramacionPlanificada> listaRegistros = new ArrayList<ProgramacionPlanificada>();
	 List<Object> parametros = new ArrayList<Object>();
	 List<String> filtrosWhere = new ArrayList<String>();
	 String sqlWhere="";
	 
	 if (argumentosListar.getFiltroDiaOperativo() > 0) {
			    filtrosWhere.add(" t1.id_doperativo='" + argumentosListar.getFiltroDiaOperativo() + "'");
	 }
	 
	 if (!filtrosWhere.isEmpty()) {
		    consultaSQL.setLength(0);
		    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
		}
	 try {
		 consultaSQL.append(" SELECT  t1.id_doperativo, ");
		 consultaSQL.append(" COALESCE(t3.volumen_solicitado,0) volumen_solicitado,");
		 consultaSQL.append(" t1.cantidad_cisternas_planificadas cantidad_cisternas, ");
		 consultaSQL.append(" COALESCE(t2.vol_asig,0) volumen_asignado, ");
		 consultaSQL.append(" COALESCE(t2.cist_asig,0) cisterna_asignada ");
		 consultaSQL.append(" FROM   sgo.dia_operativo t1 ");
		 consultaSQL.append(" LEFT JOIN (SELECT id_doperativo,estado,SUM(volumen_total_observado) vol_asig, COUNT(id_doperativo) cist_asig ");
		 consultaSQL.append(" FROM sgo.v_dia_operativo_base_calculo_asignacion_relacion WHERE estado <> 6 ");
		 consultaSQL.append(" GROUP BY id_doperativo,estado) t2 on (t2.id_doperativo = t1.id_doperativo) ");
		 consultaSQL.append(" LEFT JOIN (SELECT id_doperativo,SUM(volumen_solicitado) volumen_solicitado FROM sgo.planificacion ");
		 consultaSQL.append(" GROUP BY id_doperativo)  t3 on (t3.id_doperativo = t1.id_doperativo) ");
		 //consultaSQL.append("  WHERE t1.id_doperativo = ? ");
		 consultaSQL.append(sqlWhere);
		 listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new ProgramacionPlanificadaMapper());
		 programacionPlanificada = new ProgramacionPlanificada();
		 if(listaRegistros.size() > 0)
			 programacionPlanificada = listaRegistros.get(0);
	} catch (DataAccessException excepcionAccesoDatos) {
		excepcionAccesoDatos.printStackTrace();
	}
	 return programacionPlanificada;           
 }
 /** Fin Ticket 9000002608**/
 
 public RespuestaCompuesta recuperarRegistrosReporte(ParametrosListar argumentosListar) {
	  StringBuilder consultaSQL = new StringBuilder();
	  RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<DetalleProgramacion> contenido = new Contenido<DetalleProgramacion>();
	  List<DetalleProgramacion> listaRegistros = new ArrayList<DetalleProgramacion>();
	  List<Object> parametros = new ArrayList<Object>();
	  List<String> filtrosWhere = new ArrayList<String>();
	  String sqlWhere="";
	  String sqlOrderBy = "";
	  try {
		   if (argumentosListar.getFiltroDiaOperativo() > 0) {
			    filtrosWhere.add("id_doperativo='" + argumentosListar.getFiltroDiaOperativo() + "'");
			}

		   if (!filtrosWhere.isEmpty()) {
			    consultaSQL.setLength(0);
			    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
			}

		 //Comentado por req 9000002608 para Programacion=============================
		   sqlOrderBy = " ORDER BY t1.id_programacion, t1.id_dprogramacion";  
		 //==========================================================================
	   //Comentado por req 9000002608 para Programacion=============================
//	   sqlOrderBy = " ORDER BY t1.orden_compra,t1.nombre_producto,t1.razon_social";  
	 //==========================================================================
	   consultaSQL.append("SELECT ");
	   consultaSQL.append("t1.id_doperativo,");
	   consultaSQL.append("t1.id_programacion,");
	   consultaSQL.append("t1.orden_compra,");
	   consultaSQL.append("t1.nombre_producto,");
	   consultaSQL.append("t1.fecha_operativa,");
	   consultaSQL.append("t1.razon_social,");
	   consultaSQL.append("t1.fecha_estimada_carga,");
	   //Comentado por req 9000002608 para Programacion=============================
//	   consultaSQL.append("t1.total_capacidad_cisterna,");
//	   consultaSQL.append("t1.capacidad_cisterna,");	   
	   //==========================================================================
	   consultaSQL.append("t1.codigo_scop,");
	   consultaSQL.append("t1.placa_tracto,");
	   consultaSQL.append("t1.placa,");	   
	   consultaSQL.append("t1.tarjeta_cubicacion,");	   
	   consultaSQL.append("t1.apellidos,");
	   consultaSQL.append("t1.nombres,");
	   consultaSQL.append("t1.brevete,");
	   consultaSQL.append("t1.codigo_sap_pedido,");	
	   consultaSQL.append("t1.id_planta,");
	   consultaSQL.append("t1.descripcion_planta,");
	   consultaSQL.append("t1.codigo_sap_unidad");	
      //Agregado por req 9000002608 para Programacion=============================
	   consultaSQL.append(",t1.capacidad_volumetrica ");	
	   consultaSQL.append(",t1.numero_compartimento ");	
	   consultaSQL.append(",t1.id_dprogramacion ");
	   //==========================================================================
	   consultaSQL.append(" FROM ");
      //Agregado por req 9000002608 para Programacion=============================
	   consultaSQL.append("sgo.v_detalle_programacion");
	   //==========================================================================
	 //Comentado por req 9000002608 para Programacion=============================
//	   consultaSQL.append(NOMBRE_VISTA_REPORTE);
	   //==========================================================================
	   consultaSQL.append(" t1 ");
	   consultaSQL.append(sqlWhere);
	   consultaSQL.append(sqlOrderBy);

	   listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new ReporteProgramacionMapper());
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
 
 /**
  * Metodo para actualizar el comentario de la Programacion.
  * 
  * @param Programacion
  *         Entidad que se va a modificar.
  * @return respuesta resultado de la modificación.
  */
 public RespuestaCompuesta actualizarComentario(Programacion programacion) {
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  StringBuilder consultaSQL = new StringBuilder();
  int cantidadFilasAfectadas = 0;
  try {
   consultaSQL.append("UPDATE ");
   consultaSQL.append(NOMBRE_TABLA);
   consultaSQL.append(" SET ");
   consultaSQL.append("comentario=:Comentario,");
   // Datos auditoria
   consultaSQL.append("actualizado_por=:ActualizadoPor,");
   consultaSQL.append("actualizado_el=:ActualizadoEl,");
   consultaSQL.append("ip_actualizacion=:IpActualizacion");
   consultaSQL.append(" WHERE ");
   consultaSQL.append(NOMBRE_CAMPO_CLAVE);
   consultaSQL.append("=:idProgramacion ");
   
   MapSqlParameterSource listaParametros = new MapSqlParameterSource();
   listaParametros.addValue("Comentario", programacion.getComentario());
   
   // Valores Auditoria
   listaParametros.addValue("ActualizadoPor", programacion.getActualizadoPor());
   listaParametros.addValue("ActualizadoEl", programacion.getActualizadoEl());   
   listaParametros.addValue("IpActualizacion", programacion.getIpActualizacion());
   listaParametros.addValue("idProgramacion", programacion.getId());

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
 
}