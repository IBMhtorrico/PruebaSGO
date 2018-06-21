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

import sgo.entidad.DespachoCarga;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class DespachoCargaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "despacho_carga";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_despacho_carga";
	public final static String NOMBRE_CAMPO_CLAVE = "id_dcarga";
	public final static String NOMBRE_CAMPO_FILTRO = "fecha_carga";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "fecha_carga";
	
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
			if (propiedad.equals("nombreArchivo")){
				campoOrdenamiento="nombre_archivo";
			}
			if (propiedad.equals("estacion.nombre")){
				campoOrdenamiento="nombre";
			}
			if (propiedad.equals("fechaCarga")){
				campoOrdenamiento="fecha_carga";
			}
			if (propiedad.equals("comentario")){
				campoOrdenamiento="comentario";
			}
			if (propiedad.equals("operario.nombreOperario")){
				campoOrdenamiento="nombre_operario";
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
		Contenido<DespachoCarga> contenido = new Contenido<DespachoCarga>();
		List<DespachoCarga> listaRegistros = new ArrayList<DespachoCarga>();
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
			if (!argumentosListar.getNombreArchivoDespacho().isEmpty()){
				filtrosWhere.add("lower(t1.nombre_archivo) = lower('"+argumentosListar.getNombreArchivoDespacho()+"')");
			}
			
			if (!argumentosListar.getFiltroFechaJornada().isEmpty()){
				filtrosWhere.add("t1.fecha_carga = '"+ argumentosListar.getFiltroFechaJornada() +"' ");
			}

			if(argumentosListar.getIdCliente() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_cliente = " + argumentosListar.getIdCliente() +" ");
			}
			
			if(argumentosListar.getFiltroOperacion() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion() +" ");
			}
			
			if(argumentosListar.getFiltroEstacion() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_estacion = " + argumentosListar.getFiltroEstacion() +" ");
			}
			
			if(argumentosListar.getFiltroIdOperario() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_operario = " + argumentosListar.getFiltroIdOperario() +" ");
			}
			
			if(argumentosListar.getIdJornada() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada() +" ");
			}
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}
			
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_dcarga,");
			consultaSQL.append("t1.nombre_archivo,");
			consultaSQL.append("t1.fecha_carga,");
			consultaSQL.append("t1.comentario,");
			consultaSQL.append("t1.id_operario,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_operario,");
			consultaSQL.append("t1.apellido_paterno_operario,");
			consultaSQL.append("t1.apellido_materno_operario,");
			consultaSQL.append("t1.dni_operario,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_jornada");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DespachoCargaMapper());
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
		List<DespachoCarga> listaRegistros=new ArrayList<DespachoCarga>();
		Contenido<DespachoCarga> contenido = new Contenido<DespachoCarga>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_dcarga,");
			consultaSQL.append("t1.nombre_archivo,");
			consultaSQL.append("t1.fecha_carga,");
			consultaSQL.append("t1.comentario,");
			consultaSQL.append("t1.id_operario,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_operario,");
			consultaSQL.append("t1.apellido_paterno_operario,");
			consultaSQL.append("t1.apellido_materno_operario,");
			consultaSQL.append("t1.dni_operario,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_jornada");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DespachoCargaMapper());
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
	
	public RespuestaCompuesta guardarRegistro(DespachoCarga despachoCarga){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (nombre_archivo, fecha_carga, comentario, id_operario, id_estacion, id_jornada)");
			consultaSQL.append(" VALUES (:NombreArchivo,:FechaCarga,:Comentario,:IdOperario,:IdEstacion,:IdJornada )");
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("NombreArchivo", despachoCarga.getNombreArchivo());
			listaParametros.addValue("FechaCarga", despachoCarga.getFechaCarga());
			listaParametros.addValue("Comentario", despachoCarga.getComentario());
			listaParametros.addValue("IdOperario", despachoCarga.getIdOperario());
			listaParametros.addValue("IdEstacion", despachoCarga.getIdEstacion());
			listaParametros.addValue("IdJornada", despachoCarga.getIdJornada());
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
	
	public RespuestaCompuesta actualizarRegistro(DespachoCarga despachoCarga){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("nombre_archivo	=:NombreArchivo,");
			consultaSQL.append("fecha_carga		=:FechaCarga,");
			consultaSQL.append("comentario		=:Comentario,");
			consultaSQL.append("id_operario		=:IdOperario");
			//consultaSQL.append("id_estacion		=:IdEstacion,");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("NombreArchivo", despachoCarga.getNombreArchivo());
			listaParametros.addValue("FechaCarga", despachoCarga.getFechaCarga());
			listaParametros.addValue("Comentario", despachoCarga.getComentario());
			listaParametros.addValue("IdOperario", despachoCarga.getIdOperario());
			//listaParametros.addValue("IdEstacion", despachoCarga.getIdEstacion());
			listaParametros.addValue("Id", despachoCarga.getId());
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

}