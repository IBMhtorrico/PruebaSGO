package sgo.datos;

import org.apache.commons.lang.StringUtils;

import java.sql.Date;
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

import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Vigencia;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class VigenciaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "vigencia";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_vigencia";
	public final static String NOMBRE_CAMPO_CLAVE = "id_vigencia";

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public DataSource getDataSource() {
		return this.jdbcTemplate.getDataSource();
	}

	/*public String mapearCampoOrdenamiento(String propiedad) {
		String campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
		try {
			if (propiedad.equals("id")) {
				campoOrdenamiento = "id_vigencia";
			}
			if (propiedad.equals("placa")) {
				campoOrdenamiento = "placa";
			}
			if (propiedad.equals("idTracto")) {
				campoOrdenamiento = "id_tracto";
			}
			if (propiedad.equals("idTransportista")) {
				campoOrdenamiento = "id_transportista";
			}
			if (propiedad.equals("estado")) {
				campoOrdenamiento = "estado";
			}
			
			if (propiedad.equals("placaTracto")) {
				campoOrdenamiento = "placa_tracto";
			}
			if (propiedad.equals("razonSocial")) {
				campoOrdenamiento = "razon_social";
			}
		
			// Campos de auditoria
		} catch (Exception excepcion) {

		}
		return campoOrdenamiento;
	}*/

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Vigencia> contenido = new Contenido<Vigencia>();
		List<Vigencia> listaRegistros = new ArrayList<Vigencia>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			//sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();
			sqlOrderBy= " ORDER BY t1.fecha_expiracion ";
			
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;

			if (argumentosListar.getFiltroPerteneceA() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.pertenece_a = " + argumentosListar.getFiltroPerteneceA());
			}
			if (argumentosListar.getFiltroIdDocumento() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_documento = " + argumentosListar.getFiltroIdDocumento());
			}
			if (argumentosListar.getFiltroIdEntidad() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_entidad = " + argumentosListar.getFiltroIdEntidad());
			}

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_vigencia,");
			consultaSQL.append("t1.id_documento,");
			consultaSQL.append("t1.numero_documento,");
			consultaSQL.append("t1.fecha_emision,");
			consultaSQL.append("t1.fecha_expiracion,");
			consultaSQL.append("t1.pertenece_a, ");
			consultaSQL.append("t1.id_entidad, ");
			consultaSQL.append("t1.periodo_vigencia, ");
			consultaSQL.append("t1.tiempo_alerta, ");
			consultaSQL.append("t1.nombre_documento ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new VigenciaMapper());

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
		List<Vigencia> listaRegistros = new ArrayList<Vigencia>();
		Contenido<Vigencia> contenido = new Contenido<Vigencia>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_vigencia,");
			consultaSQL.append("t1.id_documento,");
			consultaSQL.append("t1.numero_documento,");
			consultaSQL.append("t1.fecha_emision,");
			consultaSQL.append("t1.fecha_expiracion,");
			consultaSQL.append("t1.pertenece_a, ");
			consultaSQL.append("t1.id_entidad, ");
			consultaSQL.append("t1.periodo_vigencia, ");
			consultaSQL.append("t1.tiempo_alerta, ");
			consultaSQL.append("t1.nombre_documento ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new VigenciaMapper());
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

	public RespuestaCompuesta guardarRegistro(Vigencia vigencia) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_documento, numero_documento, fecha_emision, fecha_expiracion, pertenece_a, id_entidad,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Documento, :NumeroDocumento, :FechaEmision, :FechaExpiracion, :PerteneceA, :IdEntidad,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Documento", vigencia.getIdDocumento());
			listaParametros.addValue("NumeroDocumento", vigencia.getNumeroDocumento());
			listaParametros.addValue("FechaEmision",	vigencia.getFechaEmision());
			listaParametros.addValue("FechaExpiracion", vigencia.getFechaExpiracion());
			listaParametros.addValue("PerteneceA", vigencia.getPerteneceA());
			listaParametros.addValue("IdEntidad", vigencia.getIdEntidad());
			listaParametros.addValue("CreadoEl", vigencia.getCreadoEl());
			listaParametros.addValue("CreadoPor", vigencia.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", vigencia.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", vigencia.getActualizadoEl());
			listaParametros.addValue("IpCreacion", vigencia.getIpCreacion());
			listaParametros.addValue("IpActualizacion", vigencia.getIpActualizacion());

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

	public RespuestaCompuesta actualizarRegistro(Vigencia vigencia) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_documento=:Documento,");
			consultaSQL.append("numero_documento=:NumeroDocumento,");
			consultaSQL.append("fecha_emision=:FechaEmision,");
			consultaSQL.append("fecha_expiracion=:FechaExpiracion,");
			consultaSQL.append("pertenece_a=:PerteneceA,");
			consultaSQL.append("id_entidad=:IdEntidad,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Documento", vigencia.getIdDocumento());
			listaParametros.addValue("NumeroDocumento", vigencia.getNumeroDocumento());
			listaParametros.addValue("FechaEmision",	vigencia.getFechaEmision());
			listaParametros.addValue("FechaExpiracion", vigencia.getFechaExpiracion());
			listaParametros.addValue("PerteneceA", vigencia.getPerteneceA());
			listaParametros.addValue("IdEntidad", vigencia.getIdEntidad());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoEl", vigencia.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", vigencia.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", vigencia.getIpActualizacion());
			listaParametros.addValue("Id", vigencia.getId());
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


}