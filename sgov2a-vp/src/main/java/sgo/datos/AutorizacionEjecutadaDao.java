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

import sgo.entidad.AutorizacionEjecutada;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class AutorizacionEjecutadaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_SEGURIDAD	+ "autorizacion_ejecutada";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_SEGURIDAD	+ "v_autorizacion_ejecutada";
	public final static String NOMBRE_CAMPO_CLAVE = "id_aejecutada";
	public final static String NOMBRE_CAMPO_FILTRO = "vigente_hasta";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "vigente_hasta";

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

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<AutorizacionEjecutada> contenido = new Contenido<AutorizacionEjecutada>();
		List<AutorizacionEjecutada> listaRegistros = new ArrayList<AutorizacionEjecutada>();
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
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_aejecutada,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.id_registro,");
			consultaSQL.append("t1.ejecutada_el,");
			consultaSQL.append("t1.ejecutada_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.id_autorizador,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new AutorizacionEjecutadaMapper());

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
		List<AutorizacionEjecutada> listaRegistros = new ArrayList<AutorizacionEjecutada>();
		Contenido<AutorizacionEjecutada> contenido = new Contenido<AutorizacionEjecutada>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_aejecutada,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.id_registro,");
			consultaSQL.append("t1.ejecutada_el,");
			consultaSQL.append("t1.ejecutada_por,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.id_autorizacion,");
			consultaSQL.append("t1.id_autorizador,");
			consultaSQL.append("t1.vigente_desde,");
			consultaSQL.append("t1.vigente_hasta");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new AutorizacionEjecutadaMapper());
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

	public RespuestaCompuesta guardarRegistro(AutorizacionEjecutada entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (descripcion, tipo_registro, id_registro, ejecutada_el, ejecutada_por, ip_creacion, id_autorizacion, id_autorizador, vigente_desde, vigente_hasta) ");
			consultaSQL.append(" VALUES (:Descripcion, :TipoRegistro, :IdRegistro, :EjecutadaEl, :EjecutadaPor, :IpCreacion, :IdAutorizacion, :IdAutorizador, :VigenteDesde, :VigenteHasta) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Descripcion",		entidad.getDescripcion());
			listaParametros.addValue("TipoRegistro",	entidad.getTipoRegistro());
			listaParametros.addValue("IdRegistro",		entidad.getIdRegistro());
			listaParametros.addValue("EjecutadaEl",		entidad.getEjecutadaEl());
			listaParametros.addValue("EjecutadaPor",	entidad.getEjecutadaPor());
			listaParametros.addValue("IpCreacion",		entidad.getIpCreacion());
			listaParametros.addValue("IdAutorizacion",	entidad.getIdAutorizacion());
			listaParametros.addValue("IdAutorizador",	entidad.getIdAutorizador());
			listaParametros.addValue("VigenteDesde",	entidad.getVigenteDesde());
			listaParametros.addValue("VigenteHasta",	entidad.getVigenteHasta());

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

	public RespuestaCompuesta actualizarRegistro(AutorizacionEjecutada entidad) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("descripcion=:Descripcion, ");
			consultaSQL.append("tipo_registro=:TipoRegistro, ");
			consultaSQL.append("id_registro=:IdRegistro, ");
			consultaSQL.append("ejecutada_el=:EjecutadaEl, ");
			consultaSQL.append("ejecutada_por=:EjecutadaPor, ");
			consultaSQL.append("ip_creacion=:IpCreacion, ");
			consultaSQL.append("id_autorizacion=:IdAutorizacion, ");
			consultaSQL.append("id_autorizador=:IdAutorizador, ");
			consultaSQL.append("vigente_desde=:VigenteDesde, ");
			consultaSQL.append("vigente_hasta=:VigenteHasta ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Descripcion",		entidad.getDescripcion());
			listaParametros.addValue("TipoRegistro",	entidad.getTipoRegistro());
			listaParametros.addValue("IdRegistro",		entidad.getIdRegistro());
			listaParametros.addValue("EjecutadaEl",		entidad.getEjecutadaEl());
			listaParametros.addValue("EjecutadaPor",	entidad.getEjecutadaPor());
			listaParametros.addValue("IpCreacion",		entidad.getIpCreacion());
			listaParametros.addValue("IdAutorizacion",	entidad.getIdAutorizacion());
			listaParametros.addValue("IdAutorizador",	entidad.getIdAutorizador());
			listaParametros.addValue("VigenteDesde",	entidad.getVigenteDesde());
			listaParametros.addValue("VigenteHasta",	entidad.getVigenteHasta());

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
	
	 public RespuestaCompuesta actualizarRegistroVinculado(AutorizacionEjecutada entidad) {
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL = new StringBuilder();
    int cantidadFilasAfectadas = 0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_registro=:IdRegistro ");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros = new MapSqlParameterSource();
      listaParametros.addValue("IdRegistro",    entidad.getIdRegistro());
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
