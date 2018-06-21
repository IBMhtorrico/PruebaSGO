package sgo.datos;

import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
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

import sgo.entidad.Aprobador;
import sgo.entidad.Autorizacion;
import sgo.entidad.AutorizacionUsuario;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class AutorizacionUsuarioDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_SEGURIDAD	+ "autorizacion_usuario";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_SEGURIDAD	+ "v_autorizacion_usuario";
	public final static String NOMBRE_CAMPO_CLAVE = "id_ausuario";
	public final static String ID_USUARIO = "id_usuario";
	public final static String NOMBRE_CAMPO_FILTRO = "vigente_desde";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "vigente_desde";

	public final static String CAMPO_FECHA_INICIO="vigente_desde";
	public final static String CAMPO_FECHA_FIN="vigente_hasta";
	public final static String CAMPO_CODIGO_INTERNO="codigo_interno";
	public final static String CAMPO_CODIGO_AUTORIZACION="codigo_autorizacion";

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
				campoOrdenamiento = "id_ausuario";
			}
			if (propiedad.equals("vigenteDesde")) {
				campoOrdenamiento = "vigente_desde";
			}
			if (propiedad.equals("vigenteHasta")) {
				campoOrdenamiento = "vigente_hasta";
			}
		// Campos de auditoria
		} catch (Exception excepcion) {

		}
		return campoOrdenamiento;
	}

  public RespuestaCompuesta recuperarAprobadores(ParametrosListar argumentos) {
   StringBuilder consultaSQL = new StringBuilder();
   List<Aprobador> listaRegistros = new ArrayList<Aprobador>();
   Contenido<Aprobador> contenido = new Contenido<Aprobador>();
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   List<String> filtrosWhere = new ArrayList<String>();
   String sqlWhere = "";
   try {
      if (!argumentos.getFiltroCodigoInternoAutorizacion().isEmpty()) {
       filtrosWhere.add("t1." + CAMPO_CODIGO_INTERNO + "='" + argumentos.getFiltroCodigoInternoAutorizacion() + "' ");
     }
      
      if (argumentos.getFiltroFechaInicio() != null) {
       filtrosWhere.add( "'" + argumentos.getFiltroFechaInicio() + "' >= t1." + CAMPO_FECHA_INICIO );
     }
      
     if (argumentos.getFiltroFechaFinal() != null) {
       filtrosWhere.add("'" +argumentos.getFiltroFechaFinal() + "' <= t1." + CAMPO_FECHA_FIN);
     }     

     if (!filtrosWhere.isEmpty()) {
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     }
     
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.nombre,");
     consultaSQL.append("t1.identidad,");
     consultaSQL.append("t1.vigente_desde,");
     consultaSQL.append("t1.vigente_hasta");
     consultaSQL.append(" FROM ");
     consultaSQL.append(NOMBRE_VISTA);
     consultaSQL.append(" t1 ");
     consultaSQL.append(sqlWhere);
     System.out.println(consultaSQL.toString());
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new AprobadorMapper());
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
  
  public RespuestaCompuesta recuperarAutorizacionUsuario(ParametrosListar argumentos) {
   StringBuilder consultaSQL = new StringBuilder();
   List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
   Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   List<String> filtrosWhere = new ArrayList<String>();
   String sqlWhere = "";
   try {
      if (!argumentos.getFiltroCodigoInternoAutorizacion().isEmpty()) {
       filtrosWhere.add("t1." + CAMPO_CODIGO_INTERNO + "='" + argumentos.getFiltroCodigoInternoAutorizacion() + "' ");
     }
      
      if (argumentos.getFiltroFechaInicio() != null) {
       filtrosWhere.add( "'" + argumentos.getFiltroFechaInicio() + "' >= t1." + CAMPO_FECHA_INICIO );
     }
      
     if (argumentos.getFiltroFechaFinal() != null) {
       filtrosWhere.add("'" +argumentos.getFiltroFechaFinal() + "' <= t1." + CAMPO_FECHA_FIN);
     }
     
     if (argumentos.getFiltroIdUsuario() > 0) {
      filtrosWhere.add(" t1." +ID_USUARIO+ "=" + String.valueOf(argumentos.getFiltroIdUsuario()));
     }
     
     if (argumentos.getFiltroCodigoAutorizacion()!=null) {
      filtrosWhere.add(" t1." +CAMPO_CODIGO_AUTORIZACION+ "='" + argumentos.getFiltroCodigoAutorizacion()+"'");
     } 

     if (!filtrosWhere.isEmpty()) {
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     }
     
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.id_ausuario,");
     consultaSQL.append("t1.id_usuario,");
     consultaSQL.append("t1.identidad,");
     consultaSQL.append("t1.nombre,");
     consultaSQL.append("t1.id_autorizacion,");
     consultaSQL.append("t1.nombre_autorizacion,");
     consultaSQL.append("t1.codigo_interno,");
     consultaSQL.append("t1.estado_autorizacion,");
     consultaSQL.append("t1.codigo_autorizacion,");
     consultaSQL.append("t1.vigente_desde,");
     consultaSQL.append("t1.vigente_hasta,");
     consultaSQL.append("t1.estado,");
     //Campos de auditoria
     consultaSQL.append("t1.creado_el,");
     consultaSQL.append("t1.creado_por,");
     consultaSQL.append("t1.ip_creacion,");
     consultaSQL.append("t1.usuario_creacion,");
     consultaSQL.append("t1.actualizado_por,");
     consultaSQL.append("t1.actualizado_el,");
     consultaSQL.append("t1.ip_actualizacion,");
     consultaSQL.append("t1.usuario_actualizacion");
     consultaSQL.append(" FROM ");
     consultaSQL.append(NOMBRE_VISTA);
     consultaSQL.append(" t1 ");
     consultaSQL.append(sqlWhere);
     System.out.println(consultaSQL.toString());
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(), new AutorizacionUsuarioMapper());
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
	
	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			// sqlOrderBy= " ORDER BY " +
			// this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento())
			// + " " + argumentosListar.getSentidoOrdenamiento();

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;

			if (!argumentosListar.getTxtFiltro().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getTxtFiltro() + "%') ");
			}
			
			if (!argumentosListar.getValorBuscado().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getValorBuscado() + "%') ");
			}

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new AutorizacionUsuarioMapper());

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
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new AutorizacionUsuarioMapper());
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

	public RespuestaCompuesta guardarRegistro(AutorizacionUsuario entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_usuario, id_autorizacion, codigo_autorizacion, vigente_desde, vigente_hasta, estado, creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ip_actualizacion) ");
			consultaSQL.append(" VALUES (:IdUsuario,:IdAutorizacion,:CodigoAutorizacion,:VigenteDesde,:VigenteHasta,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("IdUsuario", entidad.getIdUsuario());
			listaParametros.addValue("IdAutorizacion", entidad.getIdAutorizacion());
			listaParametros.addValue("CodigoAutorizacion",	entidad.getCodigoAutorizacion());
			listaParametros.addValue("VigenteDesde",	entidad.getVigenteDesde());
			listaParametros.addValue("VigenteHasta",	entidad.getVigenteHasta());
			listaParametros.addValue("Estado",			entidad.getEstado());
			listaParametros.addValue("CreadoEl", 		entidad.getCreadoEl());
			listaParametros.addValue("CreadoPor", 		entidad.getCreadoPor());
			listaParametros.addValue("ActualizadoPor",	entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", 	entidad.getActualizadoEl());
			listaParametros.addValue("IpCreacion", 		entidad.getIpCreacion());
			listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());

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

	public RespuestaCompuesta actualizarRegistro(AutorizacionUsuario entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_usuario=:IdUsuario,");
			consultaSQL.append("id_autorizacion=:IdAutorizacion,");
			consultaSQL.append("codigo_autorizacion=:CodigoAutorizacion,");
			consultaSQL.append("vigente_desde=:VigenteDesde,");
			consultaSQL.append("vigente_hasta=:VigenteHasta,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion,");
			consultaSQL.append("estado=:Estado");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("IdUsuario", entidad.getIdUsuario());
			listaParametros.addValue("IdAutorizacion", entidad.getIdAutorizacion());
			listaParametros.addValue("CodigoAutorizacion", entidad.getCodigoAutorizacion());
			listaParametros.addValue("VigenteDesde",	entidad.getVigenteDesde());
			listaParametros.addValue("VigenteHasta",	entidad.getVigenteHasta());
			listaParametros.addValue("Estado",			entidad.getEstado());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoPor",	entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
			listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());
			listaParametros.addValue("Id", entidad.getId());
			SqlParameterSource namedParameters = listaParametros;
			/* Ejecuta la consulta y retorna las filas afectadas */
			cantidadFilasAfectadas = namedJdbcTemplate.update(
					consultaSQL.toString(), namedParameters);
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
	
	public RespuestaCompuesta actualizarEstadoRegistro(AutorizacionUsuario entidad) {
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
			listaParametros.addValue("ActualizadoPor",	entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
			listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());
			listaParametros.addValue("Id", entidad.getId());
			SqlParameterSource namedParameters = listaParametros;
			/* Ejecuta la consulta y retorna las filas afectadas */
			cantidadFilasAfectadas = namedJdbcTemplate.update(
					consultaSQL.toString(), namedParameters);
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
	
	public RespuestaCompuesta actualizarRegistroPorIdUsuarioIdAutorizacion(AutorizacionUsuario entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("codigo_autorizacion=:CodigoAutorizacion,");
			consultaSQL.append("vigente_desde=:VigenteDesde,");
			consultaSQL.append("vigente_hasta=:VigenteHasta,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE id_usuario =:IdUsuario");
			consultaSQL.append(" AND   id_autorizacion =:IdAutorizacion");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("CodigoAutorizacion", entidad.getCodigoAutorizacion());
			listaParametros.addValue("VigenteDesde",	entidad.getVigenteDesde());
			listaParametros.addValue("VigenteHasta",	entidad.getVigenteHasta());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoPor",	entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
			listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());
			listaParametros.addValue("IdUsuario", entidad.getIdUsuario());
			listaParametros.addValue("IdAutorizacion", entidad.getIdAutorizacion());
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

	public RespuestaCompuesta recuperarAutorizacionesPorUsuario(int idUsuario) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idUsuario)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.id_usuario = ?");
			consultaSQL.append(" AND t1.estado = 1");//que todos los registros esten activos

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idUsuario}, new AutorizacionUsuarioMapper());
			
			contenido.totalRegistros = listaRegistros.size();
			contenido.totalEncontrados = listaRegistros.size();
			contenido.carga = listaRegistros;
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
	
	public RespuestaCompuesta recuperarAutorizacionesPorUsuarioYAutorizacion(int idUsuario, int idAutorizacion) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idUsuario)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;

		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.id_usuario = ?");
			consultaSQL.append(" AND t1.id_autorizacion = ?");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idUsuario, idAutorizacion}, new AutorizacionUsuarioMapper());

			contenido.totalRegistros = listaRegistros.size();
			contenido.totalEncontrados = listaRegistros.size();
			contenido.carga = listaRegistros;
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

	public RespuestaCompuesta recuperarAutorizacionesPorAutorizacion(int idAutorizacion) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idAutorizacion)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.id_autorizacion = ?");
			consultaSQL.append(" AND t1.estado = 1");//que todos los registros esten activos

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idAutorizacion}, new AutorizacionUsuarioMapper());
			
			contenido.totalRegistros = listaRegistros.size();
			contenido.totalEncontrados = listaRegistros.size();
			contenido.carga = listaRegistros;
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

	public RespuestaCompuesta recuperarAutorizacionesPorAutorizacionYFecha(int idAutorizacion) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AutorizacionUsuario> listaRegistros = new ArrayList<AutorizacionUsuario>();
		Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idAutorizacion)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_ausuario,");
			consultaSQL.append("t1.id_usuario,");
			consultaSQL.append("t1.identidad,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno,");
			consultaSQL.append("t1.estado_autorizacion,");
			consultaSQL.append("t1.codigo_autorizacion,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta,");
			consultaSQL.append("t1.estado,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.id_autorizacion = ?");
			consultaSQL.append(" AND t1.estado = 1");//que todos los registros esten activos
			consultaSQL.append(" AND t1.codigo_autorizacion is not null");
			consultaSQL.append(" AND t1.vigente_hasta >= (SELECT now()::date)");
			consultaSQL.append(" AND t1.vigente_desde <= (SELECT now()::date)");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idAutorizacion}, new AutorizacionUsuarioMapper());
			
			contenido.totalRegistros = listaRegistros.size();
			contenido.totalEncontrados = listaRegistros.size();
			contenido.carga = listaRegistros;
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
	
}
