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

import sgo.entidad.Cliente;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class ClienteDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "cliente";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_cliente";
	public final static String NOMBRE_CAMPO_CLAVE = "id_cliente";
	public final static String NOMBRE_CAMPO_FILTRO = "razon_social";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "razon_social";	
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	
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
				campoOrdenamiento="id_cliente";
			}
			if (propiedad.equals("nombreCorto")){
				campoOrdenamiento="nombre_corto";
			}
			if (propiedad.equals("razonSocial")){
				campoOrdenamiento="razon_social";
			}
			if (propiedad.equals("ruc")){
				campoOrdenamiento="ruc";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
			}
		} catch(Exception excepcion){
			
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
		Contenido<Cliente> contenido = new Contenido<Cliente>();
		List<Cliente> listaRegistros = new ArrayList<Cliente>();
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
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.numero_contrato,");
			consultaSQL.append("t1.descripcion_contrato,");
			consultaSQL.append("t1.ruc,");
			consultaSQL.append("t1.estado,");
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
			System.out.println(consultaSQL.toString());
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ClienteMapper());
		
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
			List<Cliente> listaClientes=new ArrayList<Cliente>();
			Contenido<Cliente> contenido = new Contenido<Cliente>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_cliente,");
				consultaSQL.append("t1.nombre_corto,");
				consultaSQL.append("t1.razon_social,");
				consultaSQL.append("t1.numero_contrato,");
				consultaSQL.append("t1.descripcion_contrato,");
				consultaSQL.append("t1.ruc,");
				consultaSQL.append("t1.estado,");
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
				listaClientes= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new ClienteMapper());
				contenido.totalRegistros=listaClientes.size();
				contenido.totalEncontrados=listaClientes.size();
				contenido.carga= listaClientes;
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
	
	public RespuestaCompuesta guardarRegistro(Cliente cliente){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			System.out.println("entra a guardarRegistro");
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (razon_social,nombre_corto,numero_contrato,descripcion_contrato,ruc,estado,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:razonSocial,:nombreCorto,:numeroContrato,:descripcionContrato,:ruc,:estado,:creadoEl,:creadoPor,:actualizadoPor,:actualizadoEl,:IpCreacion,:IpActualizacion)");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("razonSocial", cliente.getRazonSocial());
			listaParametros.addValue("nombreCorto", cliente.getNombreCorto());
			listaParametros.addValue("numeroContrato", cliente.getNumeroContrato());
			listaParametros.addValue("descripcionContrato", cliente.getDescripcionContrato());
			listaParametros.addValue("ruc", cliente.getRuc());			
			listaParametros.addValue("estado", cliente.getEstado());
			listaParametros.addValue("creadoEl", cliente.getCreadoEl());
			listaParametros.addValue("creadoPor", cliente.getCreadoPor());			
			listaParametros.addValue("actualizadoEl", cliente.getActualizadoEl());
			listaParametros.addValue("actualizadoPor", cliente.getActualizadoPor());
			listaParametros.addValue("IpCreacion", cliente.getIpCreacion());
			listaParametros.addValue("IpActualizacion", cliente.getIpActualizacion());
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
	
	public RespuestaCompuesta actualizarRegistro(Cliente cliente){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("razon_social=:razonSocial,");
			consultaSQL.append("nombre_corto=:nombreCorto,");
			consultaSQL.append("numero_contrato=:numeroContrato,");
			consultaSQL.append("descripcion_contrato=:descripcionContrato,");
			consultaSQL.append("ruc=:ruc,");
			//consultaSQL.append("estado=:estado,");
			consultaSQL.append("actualizado_por=:actualizadoPor,");
			consultaSQL.append("actualizado_el=:actualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("razonSocial", cliente.getRazonSocial());
			listaParametros.addValue("nombreCorto", cliente.getNombreCorto());
			listaParametros.addValue("numeroContrato", cliente.getNumeroContrato());
			listaParametros.addValue("descripcionContrato", cliente.getDescripcionContrato());
			listaParametros.addValue("ruc", cliente.getRuc());			
			//listaParametros.addValue("estado", cliente.getEstado());
			//listaParametros.addValue("creadoEl", cliente.getCreadoEl());
			//listaParametros.addValue("creadoPor", cliente.getCreadoPor());			
			listaParametros.addValue("actualizadoEl", cliente.getActualizadoEl());
			listaParametros.addValue("actualizadoPor", cliente.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", cliente.getIpActualizacion());
			listaParametros.addValue("id", cliente.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Cliente cliente){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("estado=:estado,");
			consultaSQL.append("actualizado_por=:actualizadoPor,");
			consultaSQL.append("actualizado_el=:actualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("razonSocial", cliente.getRazonSocial());
			listaParametros.addValue("nombreCorto", cliente.getNombreCorto());
			listaParametros.addValue("ruc", cliente.getRuc());			
			listaParametros.addValue("estado", cliente.getEstado());
			listaParametros.addValue("creadoEl", cliente.getCreadoEl());
			listaParametros.addValue("creadoPor", cliente.getCreadoPor());			
			listaParametros.addValue("actualizadoEl", cliente.getActualizadoEl());
			listaParametros.addValue("actualizadoPor", cliente.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", cliente.getIpActualizacion());
			listaParametros.addValue("id", cliente.getId());
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

/*
 * 
public Response getRecordByRazonSocial(String razonSocial){
	StringBuilder sbSQL= new StringBuilder();		
	List<Cliente> lClientes=new ArrayList<Cliente>();
	Payload<Cliente> payload = new Payload<Cliente>();
	Response response= new Response();
	try {
		sbSQL.append("SELECT ");
		sbSQL.append("c.cliente_id,c.razon_social,c.ruc,c.telefono,c.ubigeo,c.departamento,c.provincia,c.distrito,c.direccion,");
		sbSQL.append("c.ubigeo,c.correo_electronico,c.actividad as actividad_id,a.descripcion as actividad_descripcion,");
		sbSQL.append("c.actualizado_por,concat(u.apellido_real,', ',u.nombre_real) as usuario_actualizacion,c.actualizado_el,FROM_UNIXTIME((c.actualizado_el/1000),'%d/%m/%Y %H:%i') as fecha_actualizacion");
		sbSQL.append(" FROM ");
		sbSQL.append(TABLENAME);
		sbSQL.append(" c INNER JOIN actividad_economica a ON c.actividad= a.actividad_economica_id ");
		sbSQL.append("INNER JOIN usuario u ON c.actualizado_por = u.usuario_id ");
		sbSQL.append("WHERE c.razon_social=?");
		lClientes= jdbcTemplate.query(sbSQL.toString(),new Object[] {razonSocial},new ClienteMapper());
		payload.data=lClientes;
		payload.iTotalDisplayRecords=lClientes.size();
		payload.iTotalRecords=payload.iTotalDisplayRecords;
		response.message="OK";
		response.status=true;
		response.payload = payload;			
	} catch (DataAccessException daEx) {
		response.message="Error";
		response.status=false;
		response.payload.data = null;
	}
	return response;
}


public Response getRecords(querySelectOptions args) {		
	String sqlRecords="",sqlSort="",sqlRecordsFound="",sqlTotalRecords = "",sqlWhere="",sqlLimit="";
	int TotalRecords=0,recordsFound=0;
	List<Object> params = new ArrayList<Object>();
	List<Cliente> lClientes= new ArrayList<Cliente>();
	Response response = new Response();
	Payload<Cliente> payload = new Payload<Cliente>();

	try {
		sqlSort=" ORDER BY " + args.getSortColumn() + constants.SPACE + args.getSortDirection();			
		
		if (args.getPagination()==constants.WITH_PAGINATION){
			sqlLimit= " LIMIT ?,?";
			params.add(args.getOffset());
			params.add(args.getPageSize());
		}
		
		if (args.getFilters().size()>0){
			sqlWhere = " WHERE " ;				
			sqlWhere += StringUtils.join(args.getFilters().toArray(), " AND ");
		}			
		sqlTotalRecords = "SELECT COUNT("+FIELD_ID+") FROM " + TABLENAME;
		TotalRecords = jdbcTemplate.queryForInt(sqlTotalRecords);
		recordsFound= TotalRecords;
		
		if (!sqlWhere.equals("")){
			sqlRecordsFound = "SELECT COUNT("+FIELD_ID+") FROM " + TABLENAME +  sqlWhere +  sqlSort ;
			recordsFound=jdbcTemplate.queryForInt(sqlRecordsFound);
		}

		StringBuilder sbSQL= new StringBuilder();
		sbSQL.append("SELECT ");
		sbSQL.append("c.cliente_id,c.razon_social,c.ruc,c.telefono,c.ubigeo,c.departamento,c.provincia,c.distrito,c.direccion,");
		sbSQL.append("c.correo_electronico,c.actividad as actividad_id,a.descripcion as actividad_descripcion,");
		sbSQL.append("c.actualizado_por,concat(u.apellido_real,', ',u.nombre_real) as usuario_actualizacion,c.actualizado_el,FROM_UNIXTIME((c.actualizado_el/1000),'%d/%m/%Y %H:%i') as fecha_actualizacion FROM ");
		sbSQL.append(TABLENAME);
		sbSQL.append(" c INNER JOIN actividad_economica a ON c.actividad= a.actividad_economica_id ");
		sbSQL.append(" INNER JOIN usuario u ON c.actualizado_por = u.usuario_id ");
		sbSQL.append(sqlWhere);
		sbSQL.append(sqlSort);
		sbSQL.append(sqlLimit);
		sqlRecords= sbSQL.toString();

		lClientes= jdbcTemplate.query(sqlRecords,params.toArray(),new ClienteMapper());
		payload.data= lClientes;
		response.status=true;
		response.message="OK";
		response.payload= payload;
		response.payload.iTotalDisplayRecords=recordsFound;
		response.payload.iTotalRecords= TotalRecords;

	} catch (DataAccessException daEx){
		response.message=daEx.getMessage();
		response.status=false;
		response.payload = null;	
	} catch (Exception ex){
		response.message=ex.getMessage();
		response.status=false;
		response.payload = null;
	}
	return response;
}	

public Response getRecordsAsString(querySelectOptions args) {		
	String sqlRecords="",sqlSort="",sqlRecordsFound="",sqlTotalRecords = "",sqlWhere="",sqlLimit="";
	int TotalRecords=0,recordsFound=0;
	List<Object> params = new ArrayList<Object>();
	List<Cliente> lClientes= new ArrayList<Cliente>();
	Response response = new Response();
	Payload<Cliente> payload = new Payload<Cliente>();

	try {
		sqlSort=" ORDER BY " + args.getSortColumn() + constants.SPACE + args.getSortDirection();			
		
		if (args.getPagination()==constants.WITH_PAGINATION){
			sqlLimit= " LIMIT ?,?";
			params.add(args.getOffset());
			params.add(args.getPageSize());
		}
		
		if (args.getFilters().size()>0){
			sqlWhere = " WHERE " ;				
			sqlWhere += StringUtils.join(args.getFilters().toArray(), " AND ");
		}			
		sqlTotalRecords = "SELECT COUNT("+FIELD_ID+") FROM " + TABLENAME;
		TotalRecords = jdbcTemplate.queryForInt(sqlTotalRecords);
		recordsFound= TotalRecords;
		
		if (!sqlWhere.equals("")){
			sqlRecordsFound = "SELECT COUNT("+FIELD_ID+") FROM " + TABLENAME +  sqlWhere +  sqlSort + sqlLimit;
			recordsFound=jdbcTemplate.queryForInt(sqlRecordsFound,params.toArray());
		}

		StringBuilder sbSQL= new StringBuilder();
		sbSQL.append("SELECT ");
		sbSQL.append("c.cliente_id,c.razon_social,c.ruc,c.telefono,c.ubigeo,c.departamento,c.provincia,c.distrito,c.direccion,");
		sbSQL.append("c.correo_electronico,c.actividad,a.descripcion,");
		sbSQL.append("c.actualizado_por,c.actualizado_el FROM ");
		sbSQL.append(TABLENAME);
		sbSQL.append(" c INNER JOIN actividad_economica a ON c.actividad= a.actividad_economica_id ");
		sbSQL.append(sqlWhere);
		sbSQL.append(sqlSort);
		sbSQL.append(sqlLimit);
		sqlRecords= sbSQL.toString();

		lClientes= jdbcTemplate.query(sqlRecords,params.toArray(),new ClienteMapper());
		payload.data= lClientes;
		response.status=true;
		response.message="OK";
		response.payload= payload;
		response.payload.iTotalDisplayRecords=recordsFound;
		response.payload.iTotalRecords= TotalRecords;

	} catch (DataAccessException daEx){
		response.message=daEx.getMessage();
		response.status=false;
		response.payload = null;	
	} catch (Exception ex){
		response.message=ex.getMessage();
		response.status=false;
		response.payload = null;
	}
	return response;
}





public Response deleteRecordBatch(int[] ids){
	Response response = new Response();
	//int rowsAffected=0;	
	String sql="", razonSocial="";
	int counter=0, numElements=0, id=0, recordsDeleted=0;
	Object[] params = new Object[1];
	List<String> idList = new ArrayList<String>();
	try {
		if (ids.length>0){
			numElements= ids.length;
			for(counter=0; counter< numElements; counter++){
				try {
				id= ids[counter];
				params[0]=id;
				sql="SELECT " + FIELD_RAZON_SOCIAL +  " FROM " + TABLENAME + " WHERE " + FIELD_ID + "=?";
				razonSocial = jdbcTemplate.queryForObject(sql,params,String.class);					
				sql="DELETE FROM " + TABLENAME + " WHERE " + FIELD_ID + "=?";
	        	jdbcTemplate.update(sql, params);
	        	recordsDeleted++;
				} catch (DataIntegrityViolationException daEx){	
					idList.add(razonSocial);
				} catch (DataAccessException daEx){
					idList.add(razonSocial);
				}
			}
			if (recordsDeleted==ids.length){
				response.message="OK";
				response.status=true;
				response.payload = null;
			} else {
				String listFields= StringUtils.join(idList.toArray(), ',');
				response.returnedValue=listFields;
				response.message="[" + listFields + "] no fueron eliminados porque son utilizados por otros m�dulos (Propuesta, Contrato).";
				response.status=false;
				response.payload = null;
			}				
		} else {
			response.message="Datos insuficientes";
			response.status=false;
			response.payload = null;
		}
	} catch (Exception ex){
		response.message=ex.getMessage();
		response.status=false;
		response.payload = null;
	}
	return response;
}
 * */

