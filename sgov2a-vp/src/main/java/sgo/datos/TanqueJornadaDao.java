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
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.TanqueJornada;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class TanqueJornadaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "tanque_jornada";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_tanque_jornada";
	public final static String NOMBRE_CAMPO_CLAVE = "id_tjornada";
	public final static String NOMBRE_CAMPO_FILTRO = "id_tjornada";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_tjornada";
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado_servicio";

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
				campoOrdenamiento = "id_tjornada";
			}
			if (propiedad.equals("idTanque")) {
				campoOrdenamiento = "id_tanque";
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
		Contenido<TanqueJornada> contenido = new Contenido<TanqueJornada>();
		List<TanqueJornada> listaRegistros = new ArrayList<TanqueJornada>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

//			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();
			sqlOrderBy = " ORDER BY hora_inicial, hora_final ASC";

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_VISTA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;
			
			if (Utilidades.esValido(argumentosListar.getTxtFiltro())) {
				filtrosWhere.add(argumentosListar.getTxtFiltro());
			}
			
			if (argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.estado_jornada =" + argumentosListar.getFiltroEstado());
			}
			
			if (!argumentosListar.getFiltroFechaDiaOperativo().isEmpty()) {
				filtrosWhere.add(" t1.fecha_operativa = '" + argumentosListar.getFiltroFechaDiaOperativo() +"' ");
			}
			
			if (argumentosListar.getIdJornada() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada() +" ");
			}
			
			if (argumentosListar.getFiltroEstacion() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_estacion = " + argumentosListar.getFiltroEstacion() +" ");
			}
			
			if (argumentosListar.getIdOperacion() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getIdOperacion() +" ");
			}

			if (argumentosListar.getFiltroProducto() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_producto = " + argumentosListar.getFiltroProducto() +" ");
			}

			if (argumentosListar.getIdTanque() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_tanque = " + argumentosListar.getIdTanque() +" ");
			}

			if (argumentosListar.getEstadoDespachando() != Constante.FILTRO_NINGUNO) {
				filtrosWhere.add(" t1.en_linea = " + argumentosListar.getEstadoDespachando() +" ");
			}

			if (argumentosListar.getTanqueDeApertura() != Constante.FILTRO_NINGUNO) {
				filtrosWhere.add(" t1.apertura = " + argumentosListar.getTanqueDeApertura() +" ");
			}

			if (argumentosListar.getTanqueDeCierre() != Constante.FILTRO_NINGUNO) {
				filtrosWhere.add(" t1.cierre = " + argumentosListar.getTanqueDeCierre() +" ");
			}

			if (!argumentosListar.getFiltroInicioDespacho().isEmpty()) {
				filtrosWhere.add(" t1.hora_inicial >= '" + argumentosListar.getFiltroInicioDespacho() +"' ");
			}

			if (!argumentosListar.getFiltroFinDespacho().isEmpty()) {
				filtrosWhere.add(" t1.hora_final   >= " + argumentosListar.getFiltroFinDespacho() +" ");
			}

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_tjornada,");
			consultaSQL.append("t1.id_tanque,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.medida_inicial,");
			consultaSQL.append("t1.medida_final,");
			consultaSQL.append("t1.volumen_observado_inicial, ");
			consultaSQL.append("t1.volumen_observado_final, ");
			consultaSQL.append("t1.api_corregido_inicial, ");
			consultaSQL.append("t1.api_corregido_final, ");
			consultaSQL.append("t1.temperatura_inicial, ");
			consultaSQL.append("t1.temperatura_final, ");
			consultaSQL.append("t1.factor_correccion_inicial, ");
			consultaSQL.append("t1.factor_correccion_final, ");
			consultaSQL.append("t1.volumen_corregido_inicial, ");
			consultaSQL.append("t1.volumen_corregido_final, ");
			consultaSQL.append("t1.estado_servicio, ");
			consultaSQL.append("t1.en_linea, ");
			consultaSQL.append("t1.volumen_agua_final, ");
			consultaSQL.append("t1.id_jornada, "); 
			consultaSQL.append("t1.nombre_producto, "); 
			consultaSQL.append("t1.abreviatura, ");
			consultaSQL.append("t1.indicador_producto, ");
			consultaSQL.append("t1.nombre_tanque, ");
			consultaSQL.append("t1.hora_inicial, ");
			consultaSQL.append("t1.hora_final, ");
			consultaSQL.append("t1.apertura, ");
			consultaSQL.append("t1.cierre, ");
			consultaSQL.append("t1.volumen_total, ");
			consultaSQL.append("t1.volumen_trabajo, ");
			consultaSQL.append("t1.id_estacion, ");
			consultaSQL.append("t1.fecha_operativa, ");
			consultaSQL.append("t1.estado_jornada, ");
			consultaSQL.append("t1.nombre_estacion, ");
			consultaSQL.append("t1.id_operacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new TanqueJornadaMapper());

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
		List<TanqueJornada> listaRegistros = new ArrayList<TanqueJornada>();
		Contenido<TanqueJornada> contenido = new Contenido<TanqueJornada>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_tjornada,");
			consultaSQL.append("t1.id_tanque,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.medida_inicial,");
			consultaSQL.append("t1.medida_final,");
			consultaSQL.append("t1.volumen_observado_inicial, ");
			consultaSQL.append("t1.volumen_observado_final, ");
			consultaSQL.append("t1.api_corregido_inicial, ");
			consultaSQL.append("t1.api_corregido_final, ");
			consultaSQL.append("t1.temperatura_inicial, ");
			consultaSQL.append("t1.temperatura_final, ");
			consultaSQL.append("t1.factor_correccion_inicial, ");
			consultaSQL.append("t1.factor_correccion_final, ");
			consultaSQL.append("t1.volumen_corregido_inicial, ");
			consultaSQL.append("t1.volumen_corregido_final, ");
			consultaSQL.append("t1.estado_servicio, ");
			consultaSQL.append("t1.en_linea, ");
			consultaSQL.append("t1.volumen_agua_final," );
			consultaSQL.append("t1.id_jornada, "); 
			consultaSQL.append("t1.nombre_producto, "); 
			consultaSQL.append("t1.abreviatura, ");
			consultaSQL.append("t1.indicador_producto, ");
			consultaSQL.append("t1.nombre_tanque, ");
			consultaSQL.append("t1.hora_inicial, ");
			consultaSQL.append("t1.hora_final, ");
			consultaSQL.append("t1.apertura, ");
			consultaSQL.append("t1.cierre, ");
			consultaSQL.append("t1.volumen_total, ");
			consultaSQL.append("t1.volumen_trabajo, ");
			consultaSQL.append("t1.id_estacion, ");
			consultaSQL.append("t1.fecha_operativa, ");
			consultaSQL.append("t1.estado_jornada, ");
			consultaSQL.append("t1.nombre_estacion, ");
			consultaSQL.append("t1.id_operacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	new Object[] { ID }, new TanqueJornadaMapper());
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

	public RespuestaCompuesta guardarRegistro(TanqueJornada tanqueJornada) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_tanque, id_producto, medida_inicial, medida_final, volumen_observado_inicial, volumen_observado_final, ");
			consultaSQL.append(" api_corregido_inicial, api_corregido_final, temperatura_inicial, temperatura_final, factor_correccion_inicial, ");
			consultaSQL.append(" factor_correccion_final, volumen_corregido_inicial, volumen_corregido_final, estado_servicio, en_linea, volumen_agua_final, ");
			consultaSQL.append(" id_jornada, hora_inicial, hora_final, apertura, cierre) ");			
			
			consultaSQL.append(" VALUES (:Tanque, :Producto, :MedidaInicial, :MedidaFinal,:VolObsInicial,:VolObsFinal, ");
			consultaSQL.append(" :ApiCorregidoInicial,:ApiCorregidoFinal,:TempInicial,:TempFinal,:FactorInicial, ");
			consultaSQL.append(" :FactorFinal,:VolCorregidoInicial,:VolCorregidoFinal,:Estado,:EnLinea,:VolAguaFinal, ");
			consultaSQL.append(" :IdJornada,:HoraInicial,:HoraFinal,:Apertura,:Cierre) ");
			
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Tanque", tanqueJornada.getIdTanque());
			listaParametros.addValue("Producto", tanqueJornada.getIdProducto());
			listaParametros.addValue("MedidaInicial",	tanqueJornada.getMedidaInicial());
			listaParametros.addValue("MedidaFinal", tanqueJornada.getMedidaFinal());
			listaParametros.addValue("VolObsInicial", tanqueJornada.getVolumenObservadoInicial());
			listaParametros.addValue("VolObsFinal", tanqueJornada.getVolumenObservadoFinal());
			listaParametros.addValue("ApiCorregidoInicial", tanqueJornada.getApiCorregidoInicial());
			listaParametros.addValue("ApiCorregidoFinal", tanqueJornada.getApiCorregidoFinal());
			listaParametros.addValue("TempInicial", tanqueJornada.getTemperaturaInicial());
			listaParametros.addValue("TempFinal", tanqueJornada.getTemperaturaFinal());
			listaParametros.addValue("FactorInicial", tanqueJornada.getFactorCorreccionInicial());
			listaParametros.addValue("FactorFinal", tanqueJornada.getFactorCorreccionFinal());
			listaParametros.addValue("VolCorregidoInicial", tanqueJornada.getVolumenCorregidoInicial());
			listaParametros.addValue("VolCorregidoFinal", tanqueJornada.getVolumenCorregidoFinal());
			listaParametros.addValue("Estado", tanqueJornada.getEstadoServicio());
			listaParametros.addValue("EnLinea", tanqueJornada.getEnLinea());
			listaParametros.addValue("VolAguaFinal", tanqueJornada.getVolumenAguaFinal());
			listaParametros.addValue("IdJornada", tanqueJornada.getIdJornada());
			listaParametros.addValue("HoraInicial", tanqueJornada.getHoraInicial());
			listaParametros.addValue("HoraFinal", tanqueJornada.getHoraFinal());
			listaParametros.addValue("Apertura", tanqueJornada.getApertura());
			listaParametros.addValue("Cierre", tanqueJornada.getCierre());

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

	public RespuestaCompuesta actualizarRegistro(TanqueJornada tanqueJornada) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("medida_final=:MedidaFinal,");
			consultaSQL.append("volumen_observado_final=:VolObsFinal,");
			consultaSQL.append("api_corregido_final=:ApiCorregidoFinal,");
			consultaSQL.append("temperatura_final=:TempFinal,");
			consultaSQL.append("factor_correccion_final=:FactorFinal,");
			consultaSQL.append("volumen_corregido_final=:VolCorregidoFinal,");
			consultaSQL.append("estado_servicio=:Estado,");
			consultaSQL.append("en_linea=:EnLinea,");
			consultaSQL.append("volumen_agua_final=:VolAguaFinal,");
			consultaSQL.append("hora_final=:HoraFinal,");
			consultaSQL.append("cierre=:Cierre");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("MedidaFinal", tanqueJornada.getMedidaFinal());
			listaParametros.addValue("VolObsFinal", tanqueJornada.getVolumenObservadoFinal());
			listaParametros.addValue("ApiCorregidoFinal", tanqueJornada.getApiCorregidoFinal());
			listaParametros.addValue("TempFinal", tanqueJornada.getTemperaturaFinal());
			listaParametros.addValue("FactorFinal", tanqueJornada.getFactorCorreccionFinal());
			listaParametros.addValue("VolCorregidoFinal", tanqueJornada.getVolumenCorregidoFinal());
			listaParametros.addValue("Estado", tanqueJornada.getEstadoServicio());
			listaParametros.addValue("EnLinea", tanqueJornada.getEnLinea());
			listaParametros.addValue("VolAguaFinal", tanqueJornada.getVolumenAguaFinal());
			listaParametros.addValue("HoraFinal", tanqueJornada.getHoraFinal());
			listaParametros.addValue("Cierre", tanqueJornada.getCierre());

			listaParametros.addValue("Id", tanqueJornada.getIdTjornada());
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

	public RespuestaCompuesta ActualizarEstadoRegistro(TanqueJornada tanqueJornada) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {

			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("estado_servicio=:Estado");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Estado", tanqueJornada.getEstadoServicio());
			// Valores Auditoria
			listaParametros.addValue("Id", tanqueJornada.getIdTjornada());
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
	
	public RespuestaCompuesta RegistrarAperturaCierre(TanqueJornada tanqueJornada) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append(" apertura=:Apertura, ");
			consultaSQL.append("cierre=:Cierre ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("Apertura", tanqueJornada.getApertura());
			listaParametros.addValue("Cierre", tanqueJornada.getCierre());
			// Valores Auditoria
			listaParametros.addValue("Id", tanqueJornada.getIdTjornada());
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
	
	public RespuestaCompuesta actualizarReaperturaJornada(TanqueJornada tanqueJornada) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("hora_final=:HoraFinal,");
			consultaSQL.append("cierre=:Cierre");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("HoraFinal", tanqueJornada.getHoraFinal());
			listaParametros.addValue("Cierre", tanqueJornada.getCierre());

			listaParametros.addValue("Id", tanqueJornada.getIdTjornada());
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