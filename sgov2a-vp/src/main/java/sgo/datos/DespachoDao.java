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

import sgo.entidad.Despacho;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class DespachoDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "despacho";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_despacho";
	public final static String NOMBRE_CAMPO_CLAVE = "id_despacho";
	public final static String NOMBRE_CAMPO_FILTRO = "fecha_operativa";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "fecha_operativa";
	
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
			if (propiedad.equals("jornada.fechaOperativa")){
				campoOrdenamiento="fecha_operativa";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
			}
			if (propiedad.equals("producto.nombre")){
				campoOrdenamiento="nombre_producto";
			}
			if (propiedad.equals("contometro.alias")){
				campoOrdenamiento="alias_contometro";
			}
			if (propiedad.equals("numeroVale")){
				campoOrdenamiento="numero_vale";
			}
			if (propiedad.equals("lecturaInicial")){
				campoOrdenamiento="lectura_inicial";
			}
			if (propiedad.equals("lecturaFinal")){
				campoOrdenamiento="lectura_final";
			}
			if (propiedad.equals("volumenObservado")){
				campoOrdenamiento="volumen_observado";
			}
			if (propiedad.equals("tipoRegistro")){
				campoOrdenamiento="codigo_archivo_origen";
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
		Contenido<Despacho> contenido = new Contenido<Despacho>();
		List<Despacho> listaRegistros = new ArrayList<Despacho>();
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

			if(!argumentosListar.getFiltroFechaJornada().isEmpty()){
				filtrosWhere.add(" t1.fecha_operativa ='" + argumentosListar.getFiltroFechaJornada() +"' ");
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
			
			if(argumentosListar.getFiltroCodigoArchivoOrigen() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.codigo_archivo_origen = " + argumentosListar.getFiltroCodigoArchivoOrigen() +" ");
			}

			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
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
			consultaSQL.append("t1.id_despacho,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.id_vehiculo,");
			consultaSQL.append("t1.kilometro_horometro,");
			consultaSQL.append("t1.numero_vale,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.fecha_hora_inicio,");
			consultaSQL.append("t1.fecha_hora_fin,");
			consultaSQL.append("t1.clasificacion,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.lectura_inicial,");
			consultaSQL.append("t1.lectura_final,");
			consultaSQL.append("t1.factor_correccion,");
			consultaSQL.append("t1.api_corregido,");
			consultaSQL.append("t1.temperatura,");
			consultaSQL.append("t1.volumen_corregido,");
			consultaSQL.append("t1.volumen_observado,");
			consultaSQL.append("t1.id_tanque,");
			consultaSQL.append("t1.id_contometro,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.fecha_operativa,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.nombre_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.id_propietario,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.nombre_corto_propietario,");
			consultaSQL.append("t1.descripcion_tanque,");
			consultaSQL.append("t1.alias_contometro,");
			consultaSQL.append("t1.tipo_contometro,");
			consultaSQL.append("t1.nombre_producto,");
			consultaSQL.append("t1.abreviatura,");
			consultaSQL.append("t1.codigo_archivo_origen,");
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
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DespachoMapper());
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
		List<Despacho> listaRegistros=new ArrayList<Despacho>();
		Contenido<Despacho> contenido = new Contenido<Despacho>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_despacho,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.id_vehiculo,");
			consultaSQL.append("t1.kilometro_horometro,");
			consultaSQL.append("t1.numero_vale,");
			consultaSQL.append("t1.tipo_registro,");
			consultaSQL.append("t1.fecha_hora_inicio,");
			consultaSQL.append("t1.fecha_hora_fin,");
			consultaSQL.append("t1.clasificacion,");
			consultaSQL.append("t1.id_producto,");
			consultaSQL.append("t1.lectura_inicial,");
			consultaSQL.append("t1.lectura_final,");
			consultaSQL.append("t1.factor_correccion,");
			consultaSQL.append("t1.api_corregido,");
			consultaSQL.append("t1.temperatura,");
			consultaSQL.append("t1.volumen_corregido,");
			consultaSQL.append("t1.volumen_observado,");
			consultaSQL.append("t1.id_tanque,");
			consultaSQL.append("t1.id_contometro,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.fecha_operativa,");
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.nombre_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.descripcion,");
			consultaSQL.append("t1.id_propietario,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.nombre_corto_propietario,");
			consultaSQL.append("t1.descripcion_tanque,");
			consultaSQL.append("t1.alias_contometro,");
			consultaSQL.append("t1.tipo_contometro,");
			consultaSQL.append("t1.nombre_producto,");
			consultaSQL.append("t1.abreviatura,");
			consultaSQL.append("t1.codigo_archivo_origen,");
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
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DespachoMapper());
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
	
	public RespuestaCompuesta guardarRegistro(Despacho despacho){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_jornada, id_vehiculo, kilometro_horometro, numero_vale, tipo_registro, fecha_hora_inicio, fecha_hora_fin, clasificacion, id_producto,  ");
			consultaSQL.append(" lectura_inicial, lectura_final, factor_correccion, api_corregido, temperatura, volumen_corregido, volumen_observado, id_tanque, id_contometro, estado, codigo_archivo_origen, ");
			consultaSQL.append(" creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ip_actualizacion ) ");

			consultaSQL.append(" VALUES (:IdJornada,:IdVehiculo,:KilometroHorometro,:NumeroVale,:TipoRegistro,:FechaHoraInicio,:FechaHoraFin,:Clasificacion,:IdProducto, ");
			consultaSQL.append(" :LecturaInicial, :LecturaFinal,:FactorCorreccion,:ApiCorregido, :Temperatura,:VolumenCorregido,:VolumenObservado,:IdTanque, :IdContometro, :Estado, :CodigoArchivoOrigen, ");
			consultaSQL.append(" :CreadoEl,:CreadoPor,:ActualizadoPor, :ActualizadoEl,:IpCreacion, :IpActualizacion )");
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("IdJornada", despacho.getIdJornada());
			listaParametros.addValue("IdVehiculo", despacho.getIdVehiculo());
			listaParametros.addValue("KilometroHorometro", despacho.getKilometroHorometro());
			listaParametros.addValue("NumeroVale", despacho.getNumeroVale());
			listaParametros.addValue("TipoRegistro", despacho.getTipoRegistro());
			listaParametros.addValue("FechaHoraInicio", despacho.getFechaHoraInicio());
			listaParametros.addValue("FechaHoraFin", despacho.getFechaHoraFin());
			listaParametros.addValue("Clasificacion", despacho.getClasificacion());
			listaParametros.addValue("IdProducto", despacho.getIdProducto());
			listaParametros.addValue("LecturaInicial", despacho.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", despacho.getLecturaFinal());
			listaParametros.addValue("FactorCorreccion", despacho.getFactorCorreccion());
			listaParametros.addValue("ApiCorregido", despacho.getApiCorregido());
			listaParametros.addValue("Temperatura", despacho.getTemperatura());
			listaParametros.addValue("VolumenCorregido", despacho.getVolumenCorregido());
			listaParametros.addValue("VolumenObservado", despacho.getVolumenObservado());
			listaParametros.addValue("IdTanque", despacho.getIdTanque());
			listaParametros.addValue("IdContometro", despacho.getIdContometro());
			listaParametros.addValue("Estado", despacho.getEstado());
			listaParametros.addValue("CodigoArchivoOrigen", despacho.getCodigoArchivoOrigen());
			//parametros para auditoria
			listaParametros.addValue("CreadoEl", despacho.getCreadoEl());
			listaParametros.addValue("CreadoPor", despacho.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", despacho.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", despacho.getActualizadoEl());
			listaParametros.addValue("IpCreacion", despacho.getIpCreacion());
			listaParametros.addValue("IpActualizacion", despacho.getIpActualizacion());
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
	
	public RespuestaCompuesta actualizarRegistro(Despacho despacho){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_jornada		=:IdJornada,");
			consultaSQL.append("id_vehiculo		=:IdVehiculo,");
			consultaSQL.append("kilometro_horometro=:KilometroHorometro,");
			consultaSQL.append("numero_vale		=:NumeroVale,");
			consultaSQL.append("tipo_registro	=:TipoRegistro,");
			consultaSQL.append("fecha_hora_inicio=:FechaHoraInicio,");
			consultaSQL.append("fecha_hora_fin	=:FechaHoraFin,");
			consultaSQL.append("clasificacion	=:Clasificacion,");
			consultaSQL.append("id_producto		=:IdProducto,");
			consultaSQL.append("lectura_inicial	=:LecturaInicial,");
			consultaSQL.append("lectura_final	=:LecturaFinal,");
			consultaSQL.append("factor_correccion=:FactorCorreccion,");
			consultaSQL.append("api_corregido	=:ApiCorregido,");
			consultaSQL.append("temperatura		=:Temperatura,");
			consultaSQL.append("volumen_corregido=:VolumenCorregido,");
			consultaSQL.append("volumen_observado=:VolumenObservado,");
			consultaSQL.append("id_tanque		=:IdTanque,");
			consultaSQL.append("id_contometro	=:IdContometro,");
			consultaSQL.append("estado			=:Estado,");
			consultaSQL.append("actualizado_por	=:ActualizadoPor,");
			consultaSQL.append("actualizado_el	=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("IdJornada", despacho.getIdJornada());
			listaParametros.addValue("IdVehiculo", despacho.getIdVehiculo());
			listaParametros.addValue("KilometroHorometro", despacho.getKilometroHorometro());
			listaParametros.addValue("NumeroVale", despacho.getNumeroVale());
			listaParametros.addValue("TipoRegistro", despacho.getTipoRegistro());
			listaParametros.addValue("FechaHoraInicio", despacho.getFechaHoraInicio());
			listaParametros.addValue("FechaHoraFin", despacho.getFechaHoraFin());
			listaParametros.addValue("Clasificacion", despacho.getClasificacion());
			listaParametros.addValue("IdProducto", despacho.getIdProducto());
			listaParametros.addValue("LecturaInicial", despacho.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", despacho.getLecturaFinal());
			listaParametros.addValue("FactorCorreccion", despacho.getFactorCorreccion());
			listaParametros.addValue("ApiCorregido", despacho.getApiCorregido());
			listaParametros.addValue("Temperatura", despacho.getTemperatura());
			listaParametros.addValue("VolumenCorregido", despacho.getVolumenCorregido());
			listaParametros.addValue("VolumenObservado", despacho.getVolumenObservado());
			listaParametros.addValue("IdTanque", despacho.getIdTanque());
			listaParametros.addValue("IdContometro", despacho.getIdContometro());
			listaParametros.addValue("Estado", despacho.getEstado());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", despacho.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", despacho.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", despacho.getIpActualizacion());
			listaParametros.addValue("Id", despacho.getId());
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
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Despacho despacho){
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
			listaParametros.addValue("Estado", despacho.getEstado());
			listaParametros.addValue("ActualizadoEl", despacho.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", despacho.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", despacho.getIpActualizacion());
			listaParametros.addValue("Id", despacho.getId());
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