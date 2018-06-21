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

import sgo.entidad.Contometro;
import sgo.entidad.Contenido;
import sgo.entidad.ContometroJornada;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class ContometroJornadaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "contometro_jornada";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_contometro_jornada";
	public final static String NOMBRE_CAMPO_CLAVE = "id_cjornada";
	public final static String NOMBRE_CAMPO_CLAVE_JORNADA = "id_jornada";
	//public final static String NOMBRE_CAMPO_FILTRO = "alias";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_producto";
	
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado_servicio";
	public final static String O = "OR";
	public final static String Y = "AND";
	public final static String ENTRE = "BETWEEN";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}
	
	public String mapearCampoOrdenamiento(String propiedad){
		String campoOrdenamiento=NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
		try {
			if (propiedad.equals("id")){
				campoOrdenamiento="id_cjornada";
			}
			if (propiedad.equals("idjornada")){
				campoOrdenamiento="id_jornada";
			}
			if (propiedad.equals("lecturaInicial")){
				campoOrdenamiento="lectura_inicial";
			}
			if (propiedad.equals("lecturaFinal")){
				campoOrdenamiento="lectura_final";
			}
			if (propiedad.equals("estadoServicio")){
				campoOrdenamiento="estado_servicio";
			}
			if (propiedad.equals("idContometro")){
				campoOrdenamiento="id_contometro";
			}
			if (propiedad.equals("idProducto")){
				campoOrdenamiento="id_producto";
			}
			
			//Campos de auditoria
		}catch(Exception excepcion){
			
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
		Contenido<ContometroJornada> contenido = new Contenido<ContometroJornada>();
		List<ContometroJornada> listaRegistros = new ArrayList<ContometroJornada>();
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
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados=totalRegistros;
			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			
			if(argumentosListar.getIdJornada() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada());
			}
			
			if(argumentosListar.getFiltroProducto() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_producto = " + argumentosListar.getFiltroProducto());
			}
			
			if(argumentosListar.getFiltroEstacion() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_estacion = " + argumentosListar.getFiltroEstacion());
			}

			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cjornada,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.lectura_inicial,");
			consultaSQL.append("t1.lectura_final,");
			consultaSQL.append("t1.estado_servicio,");
			consultaSQL.append("t1.id_contometro,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.nombre_producto,");
			consultaSQL.append("t1.alias_contometro,");
			consultaSQL.append("t1.fecha_operativa,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.operario1,");
			consultaSQL.append("t1.operario2");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ContometroJornadaMapper());
			
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
	
	public RespuestaCompuesta recuperarRegistro(int ID){
		StringBuilder consultaSQL= new StringBuilder();		
		List<ContometroJornada> listaRegistros=new ArrayList<ContometroJornada>();
		Contenido<ContometroJornada> contenido = new Contenido<ContometroJornada>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cjornada,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.lectura_inicial,");
			consultaSQL.append("t1.lectura_final,");
			consultaSQL.append("t1.estado_servicio,");
			consultaSQL.append("t1.id_contometro,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.nombre_producto,");
			consultaSQL.append("t1.alias_contometro,");
			consultaSQL.append("t1.fecha_operativa,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.operario1,");
			consultaSQL.append("t1.operario2");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID}, new ContometroJornadaMapper());
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
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
	
	public RespuestaCompuesta guardarRegistro(ContometroJornada contometroJornada){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_jornada, lectura_inicial, lectura_final, estado_servicio, id_contometro, id_producto)");
			consultaSQL.append(" VALUES (:Jornada,:LecturaInicial,:LecturaFinal,:EstadoServicio,:Contometro,:Producto) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("Jornada", contometroJornada.getIdJornada());
			listaParametros.addValue("LecturaInicial", contometroJornada.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", contometroJornada.getLecturaFinal());
			listaParametros.addValue("EstadoServicio", contometroJornada.getEstadoServicio());
			listaParametros.addValue("Contometro", contometroJornada.getIdContometro());
			listaParametros.addValue("Producto", contometroJornada.getIdProducto());
			
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters,claveGenerada,new String[] {NOMBRE_CAMPO_CLAVE});		
			if (cantidadFilasAfectadas>1){
				respuesta.error=Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
			respuesta.valor= claveGenerada.getKey().toString();
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error=Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta actualizarRegistro(ContometroJornada contometroJornada){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			//consultaSQL.append("lectura_inicial=:LecturaInicial,");
			consultaSQL.append("lectura_final=:LecturaFinal,");
			consultaSQL.append("estado_servicio=:EstadoServicio");
			//consultaSQL.append("id_contometro=:Contometro,");
			//consultaSQL.append("id_producto=:Producto");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			//listaParametros.addValue("LecturaInicial", contometroJornada.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", contometroJornada.getLecturaFinal());
			listaParametros.addValue("EstadoServicio", contometroJornada.getEstadoServicio());
			//listaParametros.addValue("Contometro", contometroJornada.getIdContometro());
			//listaParametros.addValue("Producto", contometroJornada.getIdProducto());
			listaParametros.addValue("Id", contometroJornada.getId());
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (cantidadFilasAfectadas>1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta eliminarRegistro(int idRegistro){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int cantidadFilasAfectadas=0;	
		String consultaSQL="";
		Object[] parametros = {idRegistro};
		try {
			consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
        	cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
			if (cantidadFilasAfectadas > 1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){	
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	
	public RespuestaCompuesta ActualizarEstadoRegistro(ContometroJornada contometroJornada){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("estado_servicio=:Estado");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", contometroJornada.getEstadoServicio());
			listaParametros.addValue("Id", contometroJornada.getId());
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);		
			if (cantidadFilasAfectadas>1){
				respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.estado=true;
		} catch (DataIntegrityViolationException excepcionIntegridadDatos){
			excepcionIntegridadDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException excepcionAccesoDatos){
			excepcionAccesoDatos.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
}