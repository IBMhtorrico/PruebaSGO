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
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class ConductorDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "conductor";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_conductor";
	public final static String NOMBRE_CAMPO_CLAVE = "id_conductor";
	public final static String NOMBRE_CAMPO_FILTRO = "nombres";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "nombres";
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String NOMBRE_CAMPO_FILTRO_FECHA = "actualizado_por";
	
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
		String campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
		try {
			if (propiedad.equals("id")){
				campoOrdenamiento="id_conductor";
			}
			if (propiedad.equals("brevete")){
				campoOrdenamiento="brevete";
			}
			if (propiedad.equals("apellidos")){
				campoOrdenamiento="apellidos";
			}
			if (propiedad.equals("nombres")){
				campoOrdenamiento="nombres";
			}
			if (propiedad.equals("dni")){
				campoOrdenamiento="dni";
			}
			if (propiedad.equals("fechaNacimiento")){
				campoOrdenamiento="fecha_nacimiento";
			}
			
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
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
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Conductor> contenido = new Contenido<Conductor>();
		List<Conductor> listaRegistros = new ArrayList<Conductor>();
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
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados=totalRegistros;
			
			if (!argumentosListar.getValorBuscado().isEmpty()){
				filtrosWhere.add("lower(t1.nombres || t1.apellidos)  like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1.nombres || t1.apellidos) like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			
			if(argumentosListar.getFiltroBrevete().length()>3){
			  filtrosWhere.add(" t1.brevete='" + argumentosListar.getFiltroBrevete()+"'");
			}			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}
			
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_conductor,");
			consultaSQL.append("t1.brevete,");
			consultaSQL.append("t1.apellidos,");
			consultaSQL.append("t1.nombres,");
			consultaSQL.append("t1.dni,");
			consultaSQL.append("t1.fecha_nacimiento,");
			//consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.estado,");
			/*consultaSQL.append("t1.sincronizado_el,");
			consultaSQL.append("t1.fecha_referencia,");
			consultaSQL.append("t1.codigo_referencia,");*/
			//consultaSQL.append("t1.razon_social_transportista,");
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
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ConductorMapper());
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
			List<Conductor> listaRegistros=new ArrayList<Conductor>();
			Contenido<Conductor> contenido = new Contenido<Conductor>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_conductor,");
				consultaSQL.append("t1.brevete,");
				consultaSQL.append("t1.apellidos,");
				consultaSQL.append("t1.nombres,");
				consultaSQL.append("t1.dni,");
				consultaSQL.append("t1.fecha_nacimiento,");
				//consultaSQL.append("t1.id_transportista,");
				consultaSQL.append("t1.estado,");
//				consultaSQL.append("t1.sincronizado_el,");
//				consultaSQL.append("t1.fecha_referencia,");
//				consultaSQL.append("t1.codigo_referencia,");
				//consultaSQL.append("t1.razon_social_transportista,");
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
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new ConductorMapper());
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
	
	public RespuestaCompuesta guardarRegistro(Conductor conductor){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (brevete,apellidos,nombres,dni,fecha_nacimiento,estado,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Brevete,:Apellidos,:Nombres,:Dni,:FechaNacimiento,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("Brevete", conductor.getBrevete());
			listaParametros.addValue("Apellidos", conductor.getApellidos());
			listaParametros.addValue("Nombres", conductor.getNombres());
			listaParametros.addValue("Dni", conductor.getDni());
			listaParametros.addValue("FechaNacimiento", conductor.getFechaNacimiento());
			//listaParametros.addValue("IdTransportista", conductor.getIdTransportista());
			listaParametros.addValue("Estado", conductor.getEstado());
			//listaParametros.addValue("SincronizadoEl", conductor.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", conductor.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", conductor.getCodigoReferencia());
			listaParametros.addValue("CreadoEl", conductor.getCreadoEl());
			listaParametros.addValue("CreadoPor", conductor.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", conductor.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", conductor.getActualizadoEl());
			listaParametros.addValue("IpCreacion", conductor.getIpCreacion());
			listaParametros.addValue("IpActualizacion", conductor.getIpActualizacion());
			
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
	
	public RespuestaCompuesta actualizarRegistro(Conductor conductor){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("brevete=:Brevete,");
			consultaSQL.append("apellidos=:Apellidos,");
			consultaSQL.append("nombres=:Nombres,");
			consultaSQL.append("dni=:Dni,");
			consultaSQL.append("fecha_nacimiento=:FechaNacimiento,");
			//consultaSQL.append("id_transportista=:IdTransportista,");
			//consultaSQL.append("estado=:Estado,");
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
			listaParametros.addValue("Brevete", conductor.getBrevete());
			listaParametros.addValue("Apellidos", conductor.getApellidos());
			listaParametros.addValue("Nombres", conductor.getNombres());
			listaParametros.addValue("Dni", conductor.getDni());
			listaParametros.addValue("FechaNacimiento", conductor.getFechaNacimiento());
			//listaParametros.addValue("IdTransportista", conductor.getIdTransportista());
			//listaParametros.addValue("Estado", conductor.getEstado());
			//listaParametros.addValue("SincronizadoEl", conductor.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", conductor.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", conductor.getCodigoReferencia());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", conductor.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", conductor.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", conductor.getIpActualizacion());
			listaParametros.addValue("Id", conductor.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Conductor conductor){
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
			//Valores Auditoria
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", conductor.getEstado());
			//listaParametros.addValue("SincronizadoEl", conductor.getSincronizadoEl());
			//listaParametros.addValue("FechaReferencia", conductor.getFechaReferencia());
			//listaParametros.addValue("CodigoReferencia", conductor.getCodigoReferencia());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", conductor.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", conductor.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", conductor.getIpActualizacion());
			listaParametros.addValue("Id", conductor.getId());
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