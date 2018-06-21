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

import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tanque;
import sgo.utilidades.Constante;

@Repository
public class TanqueDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "tanque";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_tanque";
	public final static String NOMBRE_CAMPO_CLAVE = "id_tanque";
	public final static String NOMBRE_CAMPO_FILTRO = "descripcion";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "descripcion";	
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String NOMBRE_CAMPO_FILTRO_ESTACION = "id_estacion";
	
	public final static String NOMBRE_CAMPO_ID_ESTACION = "IdEstacion";
	public final static String CAMPO_ID_ESTACION = "id_estacion";
	
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
      if (propiedad.equals(NOMBRE_CAMPO_ID_ESTACION)){
      campoOrdenamiento=CAMPO_ID_ESTACION;
      }
			if (propiedad.equals("descripcion")){
				campoOrdenamiento="descripcion";
			}
			if (propiedad.equals("volumenTotal")){
				campoOrdenamiento="volumen_total";
			}
			if (propiedad.equals("volumenTrabajo")){
				campoOrdenamiento="volumen_trabajo";
			}
			if (propiedad.equals("estacion.nombre")){
				campoOrdenamiento="nombre_estacion";
			}
			if (propiedad.equals("producto.nombre")){
				campoOrdenamiento="nombre_producto";
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
		Contenido<Tanque> contenido = new Contenido<Tanque>();
		List<Tanque> listaRegistros = new ArrayList<Tanque>();
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
			
			if ((argumentosListar.getFiltroOperacion() != Constante.FILTRO_TODOS) && ((argumentosListar.getFiltroOperacion() != Constante.FILTRO_NINGUNO))){
		        filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
		      }
			if ((argumentosListar.getIdTanque() != Constante.FILTRO_TODOS) && ((argumentosListar.getIdTanque() != Constante.FILTRO_NINGUNO))){
		        filtrosWhere.add(" t1.id_tanque = " + argumentosListar.getIdTanque());
		      }
			
	    if ((argumentosListar.getFiltroEstacion() != Constante.FILTRO_TODOS) && ((argumentosListar.getFiltroEstacion() != Constante.FILTRO_NINGUNO))){
        filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTACION + "=" + argumentosListar.getFiltroEstacion());
      }
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_tanque,");
			consultaSQL.append("t1.volumen_total,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.volumen_trabajo,");
			consultaSQL.append("t1.tipo_tanque,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.nombre_producto,");
			consultaSQL.append("t1.abreviatura,");
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
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new TanqueMapper());
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
			List<Tanque> listaRegistros=new ArrayList<Tanque>();
			Contenido<Tanque> contenido = new Contenido<Tanque>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_tanque,");
				consultaSQL.append("t1.volumen_total,");
				consultaSQL.append("t1.descripcion,");
				consultaSQL.append("t1.volumen_trabajo,");
				consultaSQL.append("t1.id_estacion,");
				consultaSQL.append("t1.tipo_tanque,");
				consultaSQL.append("t1.id_operacion,");
				consultaSQL.append("t1.estado,");
				consultaSQL.append("t1.nombre_estacion,");
				consultaSQL.append("t1.id_producto,");
				consultaSQL.append("t1.nombre_producto,");
				consultaSQL.append("t1.abreviatura,");
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
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new TanqueMapper());
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
	
	public RespuestaCompuesta guardarRegistro(Tanque tanque){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (volumen_total,tipo_tanque,descripcion,volumen_trabajo,id_estacion,id_producto,estado,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:VolumenTotal,:TipoTanque,:Descripcion,:VolumenTrabajo,:IdEstacion,:IdProducto,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("VolumenTotal", tanque.getVolumenTotal());
			listaParametros.addValue("Descripcion", tanque.getDescripcion());
			listaParametros.addValue("VolumenTrabajo", tanque.getVolumenTrabajo());
			listaParametros.addValue("TipoTanque", tanque.getTipo());
			listaParametros.addValue("IdEstacion", tanque.getIdEstacion());
			listaParametros.addValue("IdProducto", tanque.getIdProducto());
			listaParametros.addValue("Estado", tanque.getEstado());
			listaParametros.addValue("CreadoEl", tanque.getCreadoEl());
			listaParametros.addValue("CreadoPor", tanque.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", tanque.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", tanque.getActualizadoEl());
			listaParametros.addValue("IpCreacion", tanque.getIpCreacion());
			listaParametros.addValue("IpActualizacion", tanque.getIpActualizacion());
			
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
	
	public RespuestaCompuesta actualizarRegistro(Tanque tanque){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("volumen_total=:VolumenTotal,");
			consultaSQL.append("descripcion=:Descripcion,");
			consultaSQL.append("volumen_trabajo=:VolumenTrabajo,");
			consultaSQL.append("id_estacion=:IdEstacion,");
			consultaSQL.append("id_producto=:IdProducto,");
			consultaSQL.append("tipo_tanque=:TipoTanque,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("VolumenTotal", tanque.getVolumenTotal());
			listaParametros.addValue("Descripcion", tanque.getDescripcion());
			listaParametros.addValue("VolumenTrabajo", tanque.getVolumenTrabajo());
			listaParametros.addValue("IdEstacion", tanque.getIdEstacion());
			listaParametros.addValue("IdProducto", tanque.getIdProducto());
			listaParametros.addValue("TipoTanque", tanque.getTipo());
			listaParametros.addValue("ActualizadoEl", tanque.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", tanque.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", tanque.getIpActualizacion());
			listaParametros.addValue("Id", tanque.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Tanque tanque){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
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
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", tanque.getEstado());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", tanque.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", tanque.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", tanque.getIpActualizacion());
			listaParametros.addValue("Id", tanque.getId());
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