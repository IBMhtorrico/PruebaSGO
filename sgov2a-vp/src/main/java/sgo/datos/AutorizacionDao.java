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

import sgo.entidad.Autorizacion;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class AutorizacionDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_SEGURIDAD	+ "autorizacion";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_SEGURIDAD	+ "v_autorizacion";
	public final static String NOMBRE_CAMPO_CLAVE = "id_autorizacion";
	public final static String NOMBRE_CAMPO_FILTRO = "id_usuario";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "nombre";
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String NOMBRE_CAMPO_FILTRO_FECHA = "actualizado_por";

	public final static String O = " OR ";
	public final static String Y = " AND ";
	public final static String ENTRE = " BETWEEN ";

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
				campoOrdenamiento = "id_autorizacion";
			}
			if (propiedad.equals("nombre")) {
				campoOrdenamiento = "nombre";
			}
			if (propiedad.equals("codigoInterno")) {
				campoOrdenamiento = "codigo_interno";
			}
			if (propiedad.equals("estado")) {
				campoOrdenamiento = "estado";
			}
		// Campos de auditoria
		} catch (Exception excepcion) {

		}
		return campoOrdenamiento;
	}

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Autorizacion> contenido = new Contenido<Autorizacion>();
		List<Autorizacion> listaRegistros = new ArrayList<Autorizacion>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			StringBuilder consultaSQL = new StringBuilder();
			if(Utilidades.esValido(argumentosListar)){
				if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
					sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
					parametros.add(argumentosListar.getInicioPaginacion());
					parametros.add(argumentosListar.getRegistrosxPagina());
				}
	
				// sqlOrderBy= " ORDER BY " +
				// this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento())
				// + " " + argumentosListar.getSentidoOrdenamiento();
	
				
				consultaSQL.setLength(0);
				consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
				totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
				totalEncontrados = totalRegistros;
	
				if (!argumentosListar.getTxtFiltro().isEmpty()) {
					filtrosWhere.add("t1." + NOMBRE_CAMPO_FILTRO + " = "+ argumentosListar.getTxtFiltro());
				}

				if (!filtrosWhere.isEmpty()) {
					consultaSQL.setLength(0);
					sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
					consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
					totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
				}
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno_autorizacion,");
			consultaSQL.append("t1.estado_autorizacion,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el_autorizacion,");
			consultaSQL.append("t1.creado_por_autorizacion,");
			consultaSQL.append("t1.ip_creacion_autorizacion,");
			consultaSQL.append("t1.usuario_creacion_autorizacion,");
			consultaSQL.append("t1.actualizado_por_autorizacion,");
			consultaSQL.append("t1.actualizado_el_autorizacion,");
			consultaSQL.append("t1.usuario_actualizacion_autorizacion,");
			consultaSQL.append("t1.ip_actualizacion_autorizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new AutorizacionMapper());

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
		List<Autorizacion> listaRegistros = new ArrayList<Autorizacion>();
		Contenido<Autorizacion> contenido = new Contenido<Autorizacion>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno_autorizacion,");
			consultaSQL.append("t1.estado_autorizacion,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el_autorizacion,");
			consultaSQL.append("t1.creado_por_autorizacion,");
			consultaSQL.append("t1.ip_creacion_autorizacion,");
			consultaSQL.append("t1.usuario_creacion_autorizacion,");
			consultaSQL.append("t1.actualizado_por_autorizacion,");
			consultaSQL.append("t1.actualizado_el_autorizacion,");
			consultaSQL.append("t1.usuario_actualizacion_autorizacion,");
			consultaSQL.append("t1.ip_actualizacion_autorizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new AutorizacionMapper());
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

	public RespuestaCompuesta guardarRegistro(Autorizacion entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (nombre, codigo_interno, estado, creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Nombre, :CodigoInterno,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Nombre", entidad.getNombre());
			listaParametros.addValue("CodigoInterno", entidad.getCodigoInterno());
			listaParametros.addValue("Estado",	entidad.getEstado());
			listaParametros.addValue("CreadoEl", entidad.getCreadoEl());
			listaParametros.addValue("CreadoPor", entidad.getCreadoPor());
			listaParametros.addValue("ActualizadoPor",	entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
			listaParametros.addValue("IpCreacion", entidad.getIpCreacion());
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

	public RespuestaCompuesta actualizarRegistro(Autorizacion entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("nombre=:Nombre,");
			consultaSQL.append("codigo_interno=:CodigoInterno,");
			consultaSQL.append("estado=:Estado,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion,");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Nombre", entidad.getNombre());
			listaParametros.addValue("CodigoInterno", entidad.getCodigoInterno());
			listaParametros.addValue("Estado",	entidad.getEstado());
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

	public RespuestaCompuesta ActualizarEstadoRegistro(Autorizacion entidad) {
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
			listaParametros.addValue("ActualizadoPor", entidad.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
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
	
	public RespuestaCompuesta recuperarAutorizacionesPorCodigoInterno(String codigoInterno) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Autorizacion> listaRegistros = new ArrayList<Autorizacion>();
		Contenido<Autorizacion> contenido = new Contenido<Autorizacion>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(codigoInterno)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.nombre_autorizacion,");
			consultaSQL.append("t1.codigo_interno_autorizacion,");
			consultaSQL.append("t1.estado_autorizacion,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el_autorizacion,");
			consultaSQL.append("t1.creado_por_autorizacion,");
			consultaSQL.append("t1.ip_creacion_autorizacion,");
			consultaSQL.append("t1.usuario_creacion_autorizacion,");
			consultaSQL.append("t1.actualizado_por_autorizacion,");
			consultaSQL.append("t1.actualizado_el_autorizacion,");
			consultaSQL.append("t1.usuario_actualizacion_autorizacion,");
			consultaSQL.append("t1.ip_actualizacion_autorizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE codigo_interno_autorizacion = ? ");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {codigoInterno}, new AutorizacionMapper());

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
