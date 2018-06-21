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

import sgo.entidad.Contenido;
import sgo.entidad.GecAprobacion;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class GecAprobacionDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "gec_aprobacion";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_guia_combustible_aprobacion";
	public final static String NOMBRE_CAMPO_CLAVE = "id_aprobacion_gec";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}

	/*public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		int totalRegistros = 0, totalEncontrados = 0;
		List<String> filtrosWhere= new ArrayList<String>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<GuiaCombustible> contenido = new Contenido<GuiaCombustible>();
		List<GuiaCombustible> listaRegistros = new ArrayList<GuiaCombustible>();
		List<Object> parametros = new ArrayList<Object>();

		String sqlWhere="";
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
			
	    if (argumentosListar.getFiltroOperacion() > 0){
       filtrosWhere.add(" ( t1.operacion = '"+ argumentosListar.getFiltroOperacion() +"' ) ");
     } 
     
     String fechaInicio = argumentosListar.getFiltroFechaInicio();
     String fechaFinal = argumentosListar.getFiltroFechaFinal();
     if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
      filtrosWhere.add(" t1.fecha_guia_combustible" +  Constante.SQL_ENTRE + ("'" + fechaInicio + "'" + Constante.SQL_Y + "'" + fechaFinal + "'"));
     } else {
      if (!fechaInicio.isEmpty()) {
       filtrosWhere.add(" t1.fecha_guia_combustible" +  " >= '" + fechaInicio + "'");
      }
      if (!fechaFinal.isEmpty()) {
       filtrosWhere.add(" t1.fecha_guia_combustible" +  " <= '" + fechaFinal + "'");
      }
     }
     
     if (argumentosListar.getFiltroEstado() > 0){
      filtrosWhere.add(" ( t1.estado = '"+ argumentosListar.getFiltroEstado() +"' ) ");
     }
     
     sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA+" t1 "+sqlWhere);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_gcombustible,");
			consultaSQL.append("t1.orden_compra,");
			consultaSQL.append("t1.fecha_guia_combustible,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.numero_gec,");
			consultaSQL.append("t1.numero_contrato,");
			consultaSQL.append("t1.descripcion_contrato,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.comentarios,");
			consultaSQL.append("t1.id_producto,");
	      consultaSQL.append("t1.cliente,");
	      consultaSQL.append("t1.operacion,");
	      consultaSQL.append("t1.serie_gec,");
	      consultaSQL.append("t1.nombre_operacion,");
	      consultaSQL.append("t1.nombre_cliente,");
	      consultaSQL.append("t1.nombre_producto,");
	      consultaSQL.append("t1.nombre_transportista,");
	      consultaSQL.append("t1.total_volumen_despachado,");
	      consultaSQL.append("t1.total_volumen_recibido,");
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
			consultaSQL.append(" ORDER BY CAST(serie_gec AS INTEGER)  desc, CAST(numero_gec AS INTEGER) desc ");
//			consultaSQL.append(" ORDER BY t1.fecha_guia_combustible desc ");
			consultaSQL.append(sqlLimit);
			//System.out.println(consultaSQL.toString());
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new GuiaCombustibleMapper());
			totalEncontrados =totalRegistros;
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
	}*/
	
	
	public RespuestaCompuesta recuperarRegistro(int ID){
		StringBuilder consultaSQL= new StringBuilder();		
		List<GecAprobacion> listaRegistros=new ArrayList<GecAprobacion>();
		Contenido<GecAprobacion> contenido = new Contenido<GecAprobacion>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_aprobacion_gec,");
			consultaSQL.append("t1.id_gcombustible,");
			consultaSQL.append("t1.id_usuario_registrado,");
			consultaSQL.append("t1.fecha_hora_registrado,");
			consultaSQL.append("t1.usuario_registrador,");
			consultaSQL.append("t1.correo_registrador,");
			consultaSQL.append("t1.id_usuario_emitido,");
			consultaSQL.append("t1.fecha_hora_emitido,");
			consultaSQL.append("t1.usuario_emisor,");
			consultaSQL.append("t1.correo_emisor,");
			consultaSQL.append("t1.id_usuario_aprobado,");
			consultaSQL.append("t1.fecha_hora_aprobado,");
			consultaSQL.append("t1.usuario_aprobador,");
			consultaSQL.append("t1.correo_aprobador,");
			consultaSQL.append("t1.observacion_cliente,");
			consultaSQL.append("t1.estado_usuario_aprobador");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new GecAprobacionMapper());
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
	
	public RespuestaCompuesta recuperarRegistroxGEC(int IdGEC){
		StringBuilder consultaSQL= new StringBuilder();		
		List<GecAprobacion> listaRegistros=new ArrayList<GecAprobacion>();
		Contenido<GecAprobacion> contenido = new Contenido<GecAprobacion>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_aprobacion_gec,");
			consultaSQL.append("t1.id_gcombustible,");
			consultaSQL.append("t1.id_usuario_registrado,");
			consultaSQL.append("t1.fecha_hora_registrado,");
			consultaSQL.append("t1.usuario_registrador,");
			consultaSQL.append("t1.correo_registrador,");
			consultaSQL.append("t1.id_usuario_emitido,");
			consultaSQL.append("t1.fecha_hora_emitido,");
			consultaSQL.append("t1.usuario_emisor,");
			consultaSQL.append("t1.correo_emisor,");
			consultaSQL.append("t1.id_usuario_aprobado,");
			consultaSQL.append("t1.fecha_hora_aprobado,");
			consultaSQL.append("t1.usuario_aprobador,");
			consultaSQL.append("t1.correo_aprobador,");
			consultaSQL.append("t1.observacion_cliente,");
			consultaSQL.append("t1.estado_usuario_aprobador");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(" t1.id_gcombustible = ?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {IdGEC},new GecAprobacionMapper());
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
	
	/*
	 * Cuando creamos una aprobacion de gec sólo se deben guardar los datos del usuario registrador.
	 */
	public RespuestaCompuesta registrarGec(GecAprobacion gec_aprobacion){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_gcombustible, id_usuario_registrado, fecha_hora_registrado) ");
			consultaSQL.append(" VALUES (:IdGuiaEntregaCombustible,:IdUsuarioRegistrador,:FechaHoraRegistro) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("IdGuiaEntregaCombustible", gec_aprobacion.getIdGcombustible());
			listaParametros.addValue("IdUsuarioRegistrador", gec_aprobacion.getIdUsuarioRegistrado());
			listaParametros.addValue("FechaHoraRegistro", gec_aprobacion.getFechaHoraRegistrado());
			
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
	
	public RespuestaCompuesta emitirGec(GecAprobacion gec_aprobacion){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_usuario_emitido=:IdUsuarioEmisor,");
			consultaSQL.append("fecha_hora_emitido=:FechaHoraEmitido");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("IdUsuarioEmisor", gec_aprobacion.getIdUsuarioEmitido());
			listaParametros.addValue("FechaHoraEmitido", gec_aprobacion.getFechaHoraEmitido());
			listaParametros.addValue("Id", gec_aprobacion.getId());
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
	
	public RespuestaCompuesta aprobarGec(GecAprobacion gec_aprobacion){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_usuario_aprobado=:IdUsuarioAprobador, ");
			consultaSQL.append("fecha_hora_aprobado=:FechaHoraAprobador, ");
			consultaSQL.append("observacion_cliente=:ObservacionCliente, ");
			consultaSQL.append("estado_usuario_aprobador=:Estado");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("IdUsuarioAprobador", gec_aprobacion.getIdUsuarioAprobado());
			listaParametros.addValue("FechaHoraAprobador", gec_aprobacion.getFechaHoraAprobado());
			listaParametros.addValue("ObservacionCliente", gec_aprobacion.getObservacionCliente());
			listaParametros.addValue("Estado", gec_aprobacion.getEstado());
			listaParametros.addValue("Id", gec_aprobacion.getId());
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
			consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + " = ?";
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
}