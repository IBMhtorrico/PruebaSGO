package sgo.datos;

import java.sql.Timestamp;
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

import sgo.entidad.Muestreo;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class MuestreoDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+  "muestreo";
	public static final String NOMBRE_VISTA =  Constante.ESQUEMA_APLICACION	+ "v_muestreo";
	public final static String NOMBRE_CAMPO_CLAVE = "id_muestreo";
	public final static String NOMBRE_CAMPO_FILTRO = "hora_muestreo";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "hora_muestreo";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource() {
		return this.jdbcTemplate.getDataSource();
	}

	public String mapearCampoOrdenamiento(String propiedad) {
		String campoOrdenamiento=NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;;
		try {
			if (propiedad.equals("id")) {
				campoOrdenamiento = "id_muestreo";
			}
			if (propiedad.equals("horaMuestreo")) {
				campoOrdenamiento = "hora_muestreo";
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
	  Contenido<Muestreo> contenido = new Contenido<Muestreo>();
	  List<Muestreo> listaRegistros = new ArrayList<Muestreo>();
	  List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
	
			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
			
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;
			
			if (argumentosListar.getIdJornada() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada() + " ");
			}
			if (argumentosListar.getFiltroProducto() != Constante.FILTRO_TODOS) {
				filtrosWhere.add(" t1.producto_muestreado = " + argumentosListar.getFiltroProducto() + " ");
			}
			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}
		
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_muestreo,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.hora_muestreo,");
			consultaSQL.append("t1.api_muestreo,");
			consultaSQL.append("t1.temperatura_muestreo,");
			consultaSQL.append("t1.factor_muestreo,");
			consultaSQL.append("t1.producto_muestreado,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.abreviatura,");
			consultaSQL.append("t1.indicador_producto");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new MuestreoMapper());
			totalEncontrados =totalRegistros;
			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalEncontrados;
		} catch (DataAccessException daEx) {
			daEx.printStackTrace();
			respuesta.error=  Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.estado = false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta recuperarRegistro(int ID){
		StringBuilder consultaSQL= new StringBuilder();		
		List<Muestreo> listaRegistros=new ArrayList<Muestreo>();
		Contenido<Muestreo> contenido = new Contenido<Muestreo>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_muestreo,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.hora_muestreo,");
			consultaSQL.append("t1.api_muestreo,");
			consultaSQL.append("t1.temperatura_muestreo,");
			consultaSQL.append("t1.factor_muestreo,");
			consultaSQL.append("t1.producto_muestreado,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.abreviatura,");
			consultaSQL.append("t1.indicador_producto");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new MuestreoMapper());
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;			
		} catch (DataAccessException daEx) {
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta guardarRegistro(Muestreo muestreo){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder sbSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int rowsAffected=0;
		try {
			sbSQL.append("INSERT INTO ");
			sbSQL.append(NOMBRE_TABLA);
			sbSQL.append(" (id_jornada, hora_muestreo, producto_muestreado, api_muestreo, temperatura_muestreo, factor_muestreo) ");
			sbSQL.append(" VALUES (:Jornada,:Hora,:Producto,:Api,:Temperatura,:Factor) ");
			MapSqlParameterSource mapParameters= new MapSqlParameterSource();
			mapParameters.addValue("Jornada", muestreo.getIdJornada());
			mapParameters.addValue("Hora", muestreo.getHoraMuestreo());
			mapParameters.addValue("Producto", muestreo.getProductoMuestreado());
			mapParameters.addValue("Api", muestreo.getApiMuestreo());
			mapParameters.addValue("Temperatura", muestreo.getTemperaturaMuestreo());
			mapParameters.addValue("Factor", muestreo.getFactorMuestreo());
			
			SqlParameterSource namedParameters= mapParameters;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			rowsAffected= namedJdbcTemplate.update(sbSQL.toString(),namedParameters,claveGenerada,new String[] {NOMBRE_CAMPO_CLAVE});
			if (rowsAffected>1){
				respuesta.mensaje="Mas filas afectadas";
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.valor= claveGenerada.getKey().toString();

		} catch (DataIntegrityViolationException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta actualizarRegistro(Muestreo muestreo){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int rowsAffected=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("hora_muestreo=:Hora,");
			consultaSQL.append("producto_muestreado=:Producto,");
			consultaSQL.append("api_muestreo=:Api,");
			consultaSQL.append("temperatura_muestreo=:Temperatura,");
			consultaSQL.append("factor_muestreo=:Factor");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:id");
			MapSqlParameterSource mapParameters= new MapSqlParameterSource();
			mapParameters.addValue("Hora", muestreo.getHoraMuestreo());
			mapParameters.addValue("Producto", muestreo.getProductoMuestreado());
			mapParameters.addValue("Api", muestreo.getApiMuestreo());
			mapParameters.addValue("Temperatura", muestreo.getTemperaturaMuestreo());
			mapParameters.addValue("Factor", muestreo.getFactorMuestreo());
			
			mapParameters.addValue("id", muestreo.getId());
			SqlParameterSource namedParameters= mapParameters;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			rowsAffected= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (rowsAffected>1){
				respuesta.mensaje="Mas filas afectadas";
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta eliminarRegistro(int idRegistro){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int rowsAffected=0;	
		String sql="";
		Object[] params = {idRegistro};
		try {
			sql="DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
        	rowsAffected = jdbcTemplate.update(sql, params);
			if (rowsAffected==1){
				respuesta.estado=true;
			} else {
				respuesta.estado=false;
			}
		} catch (DataIntegrityViolationException daEx){	
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta eliminarRegistroPorHoraMuestreo(int idJornada, Timestamp horaMuestreo){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int rowsAffected=0;	
		String sql="";
		Object[] params = {idJornada, horaMuestreo};
		try {
			sql="DELETE FROM " + NOMBRE_TABLA + " WHERE id_jornada = ? and hora_muestreo = ? ";
        	rowsAffected = jdbcTemplate.update(sql, params);
        	respuesta.estado = true;
			/*if (rowsAffected==1){
				respuesta.estado=true;
			} else {
				respuesta.estado=false;
			}*/
		} catch (DataIntegrityViolationException daEx){	
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
}
