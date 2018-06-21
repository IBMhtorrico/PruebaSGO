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

import sgo.entidad.Cisterna;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class CisternaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "cisterna";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_cisterna";
	public final static String NOMBRE_CAMPO_CLAVE = "id_cisterna";
	public final static String NOMBRE_CAMPO_FILTRO = "placa";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "placa";
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String NOMBRE_CAMPO_FILTRO_FECHA = "actualizado_por";
	public final static String CAMPO_TRACTO="id_tracto";

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
				campoOrdenamiento = "id_cisterna";
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
	}

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Cisterna> contenido = new Contenido<Cisterna>();
		List<Cisterna> listaRegistros = new ArrayList<Cisterna>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;

			if (!argumentosListar.getValorBuscado().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getTxtFiltro() + "%') ");
			}
			
			if (argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1." + NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			
		    if (argumentosListar.getFiltroTracto()>0) {
		    	filtrosWhere.add(" t1." + CAMPO_TRACTO + "=" + argumentosListar.getFiltroTracto());
		    }
	    
		    if (argumentosListar.getIdTransportista() > 0) {
		        filtrosWhere.add(" t1.id_transportista =" + argumentosListar.getIdTransportista());
		    }
	    
		    if (argumentosListar.getFiltroPlacaCisterna().length()>0) {
		    	filtrosWhere.add(" t1.placa='" + argumentosListar.getFiltroPlacaCisterna()+"'");
	        }
		  //7000001925
		    if (argumentosListar.getFiltroPlacaTracto().length()>0) {
		    	filtrosWhere.add(" t1.placa_tracto='" + argumentosListar.getFiltroPlacaTracto()+"'");
	        }

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cisterna,");
			consultaSQL.append("t1.placa,");
			consultaSQL.append("t1.id_tracto,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.razon_social, ");
			consultaSQL.append("t1.cisternatracto, ");
			consultaSQL.append("t1.tarjeta_cubicacion, ");
			consultaSQL.append("t1.fecha_vigencia_tarjeta_cubicacion, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			// Campos de auditoria
			consultaSQL.append(" t1.creado_el, ");
			consultaSQL.append(" t1.creado_por, ");
			consultaSQL.append(" t1.actualizado_por," );
			consultaSQL.append(" t1.actualizado_el, "); 
			consultaSQL.append(" t1.usuario_creacion, "); 
			consultaSQL.append(" t1.usuario_actualizacion, ");
			consultaSQL.append(" t1.ip_creacion, ");
			consultaSQL.append(" t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new CisternaMapper());

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
		List<Cisterna> listaRegistros = new ArrayList<Cisterna>();
		Contenido<Cisterna> contenido = new Contenido<Cisterna>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cisterna,");
			consultaSQL.append("t1.placa,");
			consultaSQL.append("t1.id_tracto,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.razon_social, ");
			consultaSQL.append("t1.cisternatracto, ");
			consultaSQL.append("t1.tarjeta_cubicacion, ");
			consultaSQL.append("t1.fecha_vigencia_tarjeta_cubicacion, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			// Campos de auditoria
			consultaSQL.append(" t1.creado_el, ");
			consultaSQL.append(" t1.creado_por, ");
			consultaSQL.append(" t1.actualizado_por," );
			consultaSQL.append(" t1.actualizado_el, "); 
			consultaSQL.append(" t1.usuario_creacion, "); 
			consultaSQL.append(" t1.usuario_actualizacion, ");
			consultaSQL.append(" t1.ip_creacion, ");
			consultaSQL.append(" t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new CisternaMapper());
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
	
	public RespuestaCompuesta validaRegistro(ParametrosListar argumentosListar) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Cisterna> listaRegistros = new ArrayList<Cisterna>();
		Contenido<Cisterna> contenido = new Contenido<Cisterna>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		List<Object> parametros = new ArrayList<Object>();
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		try {

			if (!argumentosListar.getPlacaCisterna().isEmpty()){
				filtrosWhere.add(" lower(t1.placa) = lower('"+ argumentosListar.getPlacaCisterna() +"') ");
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
			consultaSQL.append("t1.id_cisterna,");
			consultaSQL.append("t1.placa,");
			consultaSQL.append("t1.id_tracto,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.razon_social, ");
			consultaSQL.append("t1.cisternatracto, ");
			consultaSQL.append("t1.tarjeta_cubicacion, ");
			consultaSQL.append("t1.fecha_vigencia_tarjeta_cubicacion, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			// Campos de auditoria
			consultaSQL.append(" t1.creado_el, ");
			consultaSQL.append(" t1.creado_por, ");
			consultaSQL.append(" t1.actualizado_por," );
			consultaSQL.append(" t1.actualizado_el, "); 
			consultaSQL.append(" t1.usuario_creacion, "); 
			consultaSQL.append(" t1.usuario_actualizacion, ");
			consultaSQL.append(" t1.ip_creacion, ");
			consultaSQL.append(" t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new CisternaMapper());
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

	public RespuestaCompuesta guardarRegistro(Cisterna cisterna) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (placa, id_tracto, id_transportista,estado,cantidad_compartimentos,tarjeta_cubicacion,fecha_vigencia_tarjeta_cubicacion,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Placa, :IdTracto, :IdTransportista, :Estado,:CantidadCompartimentos,:TarjetaCubicacion,:FechaVigenciaTarjetaCubicacion,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Placa", cisterna.getPlaca());
			listaParametros.addValue("IdTracto", cisterna.getIdTracto());
			listaParametros.addValue("IdTransportista",	cisterna.getIdTransportista());
			listaParametros.addValue("Estado", cisterna.getEstado());
			listaParametros.addValue("CantidadCompartimentos", cisterna.getCantidadCompartimentos());
			listaParametros.addValue("TarjetaCubicacion", cisterna.getTarjetaCubicacion());
			listaParametros.addValue("FechaVigenciaTarjetaCubicacion", cisterna.getFechaVigenciaTarjetaCubicacion());
			listaParametros.addValue("CreadoEl", cisterna.getCreadoEl());
			listaParametros.addValue("CreadoPor", cisterna.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", cisterna.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", cisterna.getActualizadoEl());
			listaParametros.addValue("IpCreacion", cisterna.getIpCreacion());
			listaParametros.addValue("IpActualizacion", cisterna.getIpActualizacion());

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

	public RespuestaCompuesta actualizarRegistro(Cisterna cisterna) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("placa=:Placa,");
			consultaSQL.append("id_tracto=:IdTracto,");
			consultaSQL.append("id_transportista=:IdTransportista,");
			consultaSQL.append("tarjeta_cubicacion=:TarjetaCubicacion,");
			consultaSQL.append("fecha_vigencia_tarjeta_cubicacion=:FechaVigenciaTarjetaCubicacion,");
			consultaSQL.append("cantidad_compartimentos=:CantidadCompartimentos,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Placa", cisterna.getPlaca());
			listaParametros.addValue("IdTracto", cisterna.getIdTracto());
			listaParametros.addValue("IdTransportista", cisterna.getIdTransportista());
			listaParametros.addValue("TarjetaCubicacion", cisterna.getTarjetaCubicacion());
			listaParametros.addValue("FechaVigenciaTarjetaCubicacion", cisterna.getFechaVigenciaTarjetaCubicacion());
			listaParametros.addValue("CantidadCompartimentos", cisterna.getCantidadCompartimentos());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoEl", cisterna.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", cisterna.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", cisterna.getIpActualizacion());
			listaParametros.addValue("Id", cisterna.getId());
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

	public RespuestaCompuesta ActualizarEstadoRegistro(Cisterna cisterna) {
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
			listaParametros.addValue("Estado", cisterna.getEstado());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoEl", cisterna.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", cisterna.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", cisterna.getIpActualizacion());
			listaParametros.addValue("Id", cisterna.getId());
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
     * Método para recuperar una entidad Cisterna.
     * @param idTransportista     	Identificador del transportista .
     * @return RespuestaCompuesta	Resultado de la búsqueda de la entidad.
     */
	public RespuestaCompuesta recuperarRegistroPorTransportista(int idTransportista, String txt) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Cisterna> listaRegistros = new ArrayList<Cisterna>();
		Contenido<Cisterna> contenido = new Contenido<Cisterna>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cisterna,");
			consultaSQL.append("t1.placa,");
			consultaSQL.append("t1.id_tracto,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.placa_tracto, ");
			consultaSQL.append("t1.razon_social, ");
			consultaSQL.append("t1.cisternatracto, ");
			consultaSQL.append("t1.tarjeta_cubicacion, ");
			consultaSQL.append("t1.fecha_vigencia_tarjeta_cubicacion, ");
			consultaSQL.append("t1.cantidad_compartimentos, ");
			// Campos de auditoria
			consultaSQL.append(" t1.creado_el, ");
			consultaSQL.append(" t1.creado_por, ");
			consultaSQL.append(" t1.actualizado_por," );
			consultaSQL.append(" t1.actualizado_el, "); 
			consultaSQL.append(" t1.usuario_creacion, "); 
			consultaSQL.append(" t1.usuario_actualizacion, ");
			consultaSQL.append(" t1.ip_creacion, ");
			consultaSQL.append(" t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE id_transportista = ? ");
			if (Utilidades.esValido(txt)){
				consultaSQL.append("and lower(t1.cisternatracto) like lower('%" + txt + "%') ");
			}
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { idTransportista }, new CisternaMapper());
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