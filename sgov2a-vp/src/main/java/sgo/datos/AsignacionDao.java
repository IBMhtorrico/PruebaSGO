package sgo.datos;

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

import sgo.entidad.Asignacion;
import sgo.entidad.Contenido;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class AsignacionDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "asignacion";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_asignacion";
	public final static String NOMBRE_CAMPO_CLAVE = "id_asignacion";
	public final static String NOMBRE_CAMPO_CLAVE_DOPERATIVO = "id_doperativo";
	public final static String NOMBRE_CAMPO_CLAVE_TRANSPORTE = "id_transporte";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_doperativo";

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
				campoOrdenamiento = "id_asignacion";
			}
			if (propiedad.equals("idDoperativo")) {
				campoOrdenamiento = "id_doperativo";
			}
			if (propiedad.equals("idTransporte")) {
				campoOrdenamiento = "id_transporte";
			}
			// Campos de auditoria
		} catch (Exception excepcion) {

		}
		return campoOrdenamiento;
	}

	public RespuestaCompuesta recuperarRegistrosPorDiaOperativo(int idDoperativo) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Asignacion> listaRegistros = new ArrayList<Asignacion>();
		Contenido<Asignacion> contenido = new Contenido<Asignacion>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idDoperativo)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			//DATOS DE DIA OPERATIVO
			consultaSQL.append("t1.id_doperativo, ");
			consultaSQL.append("t1.id_transporte, ");
			//Datos de auditoria
			consultaSQL.append("t1.actualizado_por, ");
			consultaSQL.append("t1.actualizado_el, ");
			consultaSQL.append("t1.usuario_actualizacion, ");
			consultaSQL.append("t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.id_doperativo = ?");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idDoperativo}, new AsignacionMapper());

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
	
	public RespuestaCompuesta recuperarRegistro(int idDiaOperativo, int idTransporte) {
		StringBuilder consultaSQL = new StringBuilder();
		List<Asignacion> listaRegistros = new ArrayList<Asignacion>();
		Contenido<Asignacion> contenido = new Contenido<Asignacion>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		List<Object> parametros = new ArrayList<Object>();
		if(!Utilidades.esValido(idDiaOperativo) || !Utilidades.esValido(idTransporte)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		parametros.add(idDiaOperativo);
		parametros.add(idTransporte);
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			//DATOS DE DIA OPERATIVO
			consultaSQL.append("t1.id_doperativo, ");
			consultaSQL.append("t1.id_transporte, ");
			//Datos de auditoria
			consultaSQL.append("t1.actualizado_por, ");
			consultaSQL.append("t1.actualizado_el, ");
			consultaSQL.append("t1.usuario_actualizacion, ");
			consultaSQL.append("t1.ip_actualizacion ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE_DOPERATIVO);
			consultaSQL.append("= ? ");
			consultaSQL.append( Y );
			consultaSQL.append(NOMBRE_CAMPO_CLAVE_TRANSPORTE);
			consultaSQL.append("= ? ");
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new AsignacionMapper());
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

	public RespuestaCompuesta guardarRegistro(Asignacion eAsignacion) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (actualizado_por, actualizado_el, ip_actualizacion,id_doperativo, id_transporte) ");
			consultaSQL.append(" VALUES (:ActualizadoPor,:ActualizadoEl,:IpActualizacion,:IdDoperativo,:IdTransporte) ");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
			listaParametros.addValue("ActualizadoPor", eAsignacion.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", eAsignacion.getActualizadoEl());
			listaParametros.addValue("IpActualizacion",	eAsignacion.getIpActualizacion());
			listaParametros.addValue("IdDoperativo", eAsignacion.getIdDoperativo());
			listaParametros.addValue("IdTransporte", eAsignacion.getIdTransporte());

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

	public RespuestaCompuesta actualizarRegistro(Asignacion eAsignacion) {
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL = new StringBuilder();
		int cantidadFilasAfectadas = 0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
//			consultaSQL.append("id_doperativo=:IdDoperativo,");
//			consultaSQL.append("id_transporte=:IdTransporte,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros = new MapSqlParameterSource();
//			listaParametros.addValue("IdDoperativo", eAsignacion.getIdDoperativo());
//			listaParametros.addValue("IdTransporte", eAsignacion.getIdTransporte());
			// Valores Auditoria
			listaParametros.addValue("ActualizadoEl", eAsignacion.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", eAsignacion.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", eAsignacion.getIpActualizacion());
			listaParametros.addValue("Id", eAsignacion.getId());
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
			consultaSQL = "DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "= ?";
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