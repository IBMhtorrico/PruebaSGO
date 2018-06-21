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

import sgo.entidad.Contenido;
import sgo.entidad.Evento;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class EventoDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "evento";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_evento";
	public final static String NOMBRE_CAMPO_CLAVE = "id_evento";
	public final static String NOMBRE_CAMPO_CLAVE_ID_REGISTRO = "id_registro";
	public final static String NOMBRE_CAMPO_CLAVE_TIPO_REGISTRO = "tipo_registro";

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
				campoOrdenamiento = "id_evento";
			}
			if (propiedad.equals("tipoEvento")) {
				campoOrdenamiento = "tipo_evento";
			}
			if (propiedad.equals("fechaHora")) {
				campoOrdenamiento = "fecha_hora";
			}
			if (propiedad.equals("descripcion")) {
				campoOrdenamiento = "descripcion";
			}
			if (propiedad.equals("tipoRegistro")) {
				campoOrdenamiento = "tipo_registro";
			}
			if (propiedad.equals("idRegistro")) {
				campoOrdenamiento = "id_registro";
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
		Contenido<Evento> contenido = new Contenido<Evento>();
		List<Evento> listaRegistros = new ArrayList<Evento>();
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

/*			if (!argumentosListar.getTxtFiltro().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getTxtFiltro() + "%') ");
			}
			
			if (!argumentosListar.getValorBuscado().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getValorBuscado() + "%') ");
			}

			if (argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1." + NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}*/

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_evento,");
			consultaSQL.append("t1.tipo_evento,");
			consultaSQL.append("t1.fecha_hora,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.id_registro,");
			//Datos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.ip_creacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new EventoMapper());

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
		List<Evento> listaRegistros = new ArrayList<Evento>();
		Contenido<Evento> contenido = new Contenido<Evento>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_evento,");
			consultaSQL.append("t1.tipo_evento,");
			consultaSQL.append("t1.fecha_hora,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.id_registro,");
			//Datos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.ip_creacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new EventoMapper());
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
	
	public RespuestaCompuesta recuperarRegistroPorIdRegistroYTipoRegistro(int idRegistro, int tipoRegistro) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Evento> listaRegistros = new ArrayList<Evento>();
		Contenido<Evento> contenido = new Contenido<Evento>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		List<Object> parametros = new ArrayList<Object>();
		
		if(!Utilidades.esValido(idRegistro) || !Utilidades.esValido(tipoRegistro)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		else {
			//El tipoRegistro sólo puede ser 1:transporte || 2:descarga. || 3: diaoperativo
			if(tipoRegistro <1 || tipoRegistro > 3 ){
				respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
				respuesta.estado = false;
				respuesta.contenido = null;
				return respuesta;
			}
		}

		try {
			parametros.add(idRegistro);
			parametros.add(tipoRegistro);
			
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_evento,");
			consultaSQL.append("t1.tipo_evento,");
			consultaSQL.append("t1.fecha_hora,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.id_registro,");
			//Datos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.ip_creacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE_ID_REGISTRO);
			consultaSQL.append("= ?");
			consultaSQL.append( Y );
			consultaSQL.append(NOMBRE_CAMPO_CLAVE_TIPO_REGISTRO);
			consultaSQL.append("= ?");
			consultaSQL.append(" order by t1.fecha_hora desc   ");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new EventoMapper());

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

	public RespuestaCompuesta guardarRegistro(Evento eEvento) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" ( tipo_evento, fecha_hora, descripcion, tipo_registro, id_registro, creado_el, creado_por, ip_creacion) ");
			consultaSQL.append(" VALUES (:TipoEvento, :FechaHora, :Descripcion, :TipoRegistro,:IdRegistro,:CreadoEl,:CreadoPor,:IpCreacion) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("TipoEvento", eEvento.getTipoEvento());
			listaParametros.addValue("FechaHora", eEvento.getFechaHoraTimestamp());
			listaParametros.addValue("Descripcion",	eEvento.getDescripcion());
			listaParametros.addValue("TipoRegistro", eEvento.getTipoRegistro());
			listaParametros.addValue("IdRegistro", eEvento.getIdRegistro());
			listaParametros.addValue("CreadoEl", eEvento.getCreadoEl());
			listaParametros.addValue("CreadoPor", eEvento.getCreadoPor());
			listaParametros.addValue("IpCreacion", eEvento.getIpCreacion());

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

	public RespuestaCompuesta actualizarRegistro(Evento eEvento) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("tipo_evento=:TipoEvento,");
			consultaSQL.append("fecha_hora=:FechaHora,");
			consultaSQL.append("descripcion=:Descripcion,");
			consultaSQL.append("tipo_registro=:TipoRegistro,");
			consultaSQL.append("id_registro=:IdRegistro");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("TipoEvento", eEvento.getTipoEvento());
			listaParametros.addValue("FechaHora", eEvento.getFechaHora());
			listaParametros.addValue("Descripcion",	eEvento.getDescripcion());
			listaParametros.addValue("TipoRegistro", eEvento.getTipoRegistro());
			listaParametros.addValue("IdRegistro", eEvento.getIdRegistro());
			listaParametros.addValue("Id", eEvento.getId());
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