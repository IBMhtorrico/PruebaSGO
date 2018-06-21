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

import sgo.entidad.Operario;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class OperarioDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "operario";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_operario";
	public final static String NOMBRE_CAMPO_CLAVE = "id_operario";
	public final static String NOMBRE_CAMPO_FILTRO = "nombre_operario";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "nombre_operario";
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String O = " OR ";
	public final static String Y = " AND ";
	public final static String ENTRE = " BETWEEN ";
	
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
			if (propiedad.equals("nombreOperario")){
				campoOrdenamiento="nombre_operario";
			}
			if (propiedad.equals("apellidoPaternoOperario")){
				campoOrdenamiento="apellido_paterno_operario";
			}
			if (propiedad.equals("apellidoMaternoOperario")){
				campoOrdenamiento="apellido_materno_operario";
			}
			if (propiedad.equals("dniOperario")){
				campoOrdenamiento="dni_operario";
			}
			if (propiedad.equals("cliente.razonSocial")){
				campoOrdenamiento="razon_social";
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
		int totalRegistros = 0, 
		totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Operario> contenido = new Contenido<Operario>();
		List<Operario> listaRegistros = new ArrayList<Operario>();
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
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getValorBuscado() +"%') "
						+ " OR lower(t1.apellido_paterno_operario) like lower('%"+ argumentosListar.getValorBuscado() +"%') "
						+ " OR lower(t1.apellido_materno_operario) like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getTxtFiltro() +"%') "
						+ " OR lower(t1.apellido_paterno_operario) like lower('%"+ argumentosListar.getTxtFiltro() +"%') "
						+ " OR lower(t1.apellido_materno_operario) like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			if(argumentosListar.getIdCliente() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_cliente = " + argumentosListar.getIdCliente() + " ");
			}
			//para recuperar el registro "SIN AYUDANTE"
			if (argumentosListar.getIndicadorOperador() != Constante.FILTRO_TODOS){
				 filtrosWhere.add(" t1.indicador_operario =  '"+argumentosListar.getIndicadorOperador()+"'");
				}
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}
			
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_operario,");
			consultaSQL.append("t1.nombre_operario,");
			consultaSQL.append("t1.apellido_paterno_operario,");
			consultaSQL.append("t1.apellido_materno_operario,");
			consultaSQL.append("t1.dni_operario,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.ruc,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.indicador_operario,");
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
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new OperarioMapper());
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
		List<Operario> listaRegistros=new ArrayList<Operario>();
		Contenido<Operario> contenido = new Contenido<Operario>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_operario,");
			consultaSQL.append("t1.nombre_operario,");
			consultaSQL.append("t1.apellido_paterno_operario,");
			consultaSQL.append("t1.apellido_materno_operario,");
			consultaSQL.append("t1.dni_operario,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.ruc,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.indicador_operario,");
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
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new OperarioMapper());
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
	
	public RespuestaCompuesta guardarRegistro(Operario operario){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (nombre_operario, apellido_paterno_operario, apellido_materno_operario, dni_operario, estado, id_cliente,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:NombreOperario,:ApellidoPaternoOperario,:ApellidoMaternoOperario,:DniOperario,:Estado,:IdCliente,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("NombreOperario", operario.getNombreOperario());
			listaParametros.addValue("ApellidoPaternoOperario", operario.getApellidoPaternoOperario());
			listaParametros.addValue("ApellidoMaternoOperario", operario.getApellidoMaternoOperario());
			listaParametros.addValue("DniOperario", operario.getDniOperario());
			listaParametros.addValue("Estado", operario.getEstado());
			System.out.println("operario.getEstado():" + operario.getEstado()); 
			listaParametros.addValue("IdCliente", operario.getIdCliente());
			listaParametros.addValue("CreadoEl", operario.getCreadoEl());
			listaParametros.addValue("CreadoPor", operario.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", operario.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", operario.getActualizadoEl());
			listaParametros.addValue("IpCreacion", operario.getIpCreacion());
			listaParametros.addValue("IpActualizacion", operario.getIpActualizacion());
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			System.out.println("QUERY REGISTRAR:"+consultaSQL.toString() );
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
	
	public RespuestaCompuesta actualizarRegistro(Operario operario){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("nombre_operario=:NombreOperario,");
			consultaSQL.append("apellido_paterno_operario=:ApellidoPaternoOperario,");
			consultaSQL.append("apellido_materno_operario=:ApellidoMaternoOperario,");
			consultaSQL.append("dni_operario=:DniOperario,");
			consultaSQL.append("id_cliente=:IdCliente,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("NombreOperario", operario.getNombreOperario());
			listaParametros.addValue("ApellidoPaternoOperario", operario.getApellidoPaternoOperario());
			listaParametros.addValue("ApellidoMaternoOperario", operario.getApellidoMaternoOperario());
			listaParametros.addValue("DniOperario", operario.getDniOperario());
			listaParametros.addValue("Estado", operario.getEstado());
			listaParametros.addValue("IdCliente", operario.getIdCliente());
			listaParametros.addValue("ActualizadoPor", operario.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", operario.getActualizadoEl());
			listaParametros.addValue("IpActualizacion", operario.getIpActualizacion());
			listaParametros.addValue("Id", operario.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Operario operario){
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
			listaParametros.addValue("Estado", operario.getEstado());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", operario.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", operario.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", operario.getIpActualizacion());
			listaParametros.addValue("Id", operario.getId());
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