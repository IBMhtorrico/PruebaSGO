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

import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.Planta;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class PlantaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "planta";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_planta";
	public final static String NOMBRE_CAMPO_CLAVE = "id_planta";
	public final static String NOMBRE_CAMPO_FILTRO = "descripcion";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "descripcion";
	
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";	
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
			if (propiedad.equals("descripcion")){
				campoOrdenamiento="descripcion";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
			}
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
		Contenido<Planta> contenido = new Contenido<Planta>();
		List<Planta> listaRegistros = new ArrayList<Planta>();
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

			
			if (!argumentosListar.getValorBuscado().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_planta,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.correopara,");
			consultaSQL.append("t1.correocc,");
			
			//consultaSQL.append("t1.sincronizado_el,");
			//consultaSQL.append("t1.fecha_referencia,");
			//consultaSQL.append("t1.codigo_referencia,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.usuario_actualizacion,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.ip_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new PlantaMapper());
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
			List<Planta> listaRegistros=new ArrayList<Planta>();
			Contenido<Planta> contenido = new Contenido<Planta>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_planta,");
				consultaSQL.append("t1.descripcion,");
				consultaSQL.append("t1.estado,");
				consultaSQL.append("t1.correopara,");
				consultaSQL.append("t1.correocc,");
				//consultaSQL.append("t1.sincronizado_el,");
				//consultaSQL.append("t1.fecha_referencia,");
				//consultaSQL.append("t1.codigo_referencia,");
				//Campos de auditoria
				consultaSQL.append("t1.creado_el,");
				consultaSQL.append("t1.creado_por,");
				consultaSQL.append("t1.actualizado_por,");
				consultaSQL.append("t1.actualizado_el,");	
				consultaSQL.append("t1.usuario_creacion,");
				consultaSQL.append("t1.usuario_actualizacion,");
				consultaSQL.append("t1.ip_creacion,");
				consultaSQL.append("t1.ip_actualizacion");
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new PlantaMapper());
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
	
	public RespuestaCompuesta guardarRegistro(Planta planta){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (descripcion,estado,correopara,correocc,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Descripcion,:Estado,:CorreoPara,:CorreoCC,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("Descripcion", planta.getDescripcion());
			listaParametros.addValue("Estado", planta.getEstado());
			listaParametros.addValue("CorreoPara", planta.getCorreoPara());
			listaParametros.addValue("CorreoCC", planta.getCorreoCC());
			listaParametros.addValue("CreadoEl", planta.getCreadoEl());
			listaParametros.addValue("CreadoPor", planta.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", planta.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", planta.getActualizadoEl());
			listaParametros.addValue("IpCreacion", planta.getIpCreacion());
			listaParametros.addValue("IpActualizacion", planta.getIpActualizacion());
			//listaParametros.addValue("SincronizadoEl", planta.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", planta.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", planta.getCodigoReferencia());
			
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
	
	public RespuestaCompuesta actualizarRegistro(Planta planta){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("descripcion=:Descripcion,");
			consultaSQL.append("estado=:Estado,");
			consultaSQL.append("correopara=:CorreoPara,");
			consultaSQL.append("correocc=:CorreoCC,");
			//consultaSQL.append("sincronizado_el=:SincronizadoEl,");
			//consultaSQL.append("fecha_referencia=:FechaReferencia,");
			//consultaSQL.append("codigo_referencia=:CodigoReferencia,");

			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Descripcion", planta.getDescripcion());
			listaParametros.addValue("Estado", planta.getEstado());
			//listaParametros.addValue("SincronizadoEl", planta.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", planta.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", planta.getCodigoReferencia());
			listaParametros.addValue("CorreoPara", planta.getCorreoPara());
			listaParametros.addValue("CorreoCC", planta.getCorreoCC());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", planta.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", planta.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", planta.getIpActualizacion());
			listaParametros.addValue("Id", planta.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Planta planta){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("estado=:Estado,");
			//consultaSQL.append("sincronizado_el=:SincronizadoEl,");
			//consultaSQL.append("fecha_referencia=:FechaReferencia,");
			//consultaSQL.append("codigo_referencia=:CodigoReferencia,");
			 
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", planta.getEstado());
			//listaParametros.addValue("SincronizadoEl", planta.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", planta.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", planta.getCodigoReferencia());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", planta.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", planta.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", planta.getIpActualizacion());
			listaParametros.addValue("Id", planta.getId());
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

