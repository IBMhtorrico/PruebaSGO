package sgo.datos;

import java.sql.Date;
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

import sgo.entidad.OtroMovimiento;
import sgo.entidad.Contenido;
import sgo.entidad.OtroMovimiento;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class OtroMovimientoDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "otro_movimiento";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_otro_movimiento";
	public final static String NOMBRE_CAMPO_CLAVE = "id_omovimiento";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_omovimiento";	
	public final static String FECHA_OPERATIVA = "fecha_operativa";
	
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
			if (propiedad.equals("estacion")){
				campoOrdenamiento="estacion";
			}
			if (propiedad.equals("dia_operativo")){
				campoOrdenamiento="dia_operativo";
			}
			if (propiedad.equals("estado_dia_operativo")){
				campoOrdenamiento="estado_dia_operativo";
			}
			if (propiedad.equals("nro_movimiento")){
				campoOrdenamiento="nro_movimiento";
			}
			if (propiedad.equals("clasificacion")){
				campoOrdenamiento="clasificacion";
			}
			if (propiedad.equals("volumen")){
				campoOrdenamiento="volumen";
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
		Contenido<OtroMovimiento> contenido = new Contenido<OtroMovimiento>();
		List<OtroMovimiento> listaRegistros = new ArrayList<OtroMovimiento>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
			
			sqlOrderBy= Constante.SQL_ORDEN + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
			
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados=totalRegistros;

			if (Utilidades.esValido(argumentosListar.getFiltroOperacion())) {
				filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion());
			}
			if (Utilidades.esValido(argumentosListar.getFiltroEstacion())) {
				filtrosWhere.add(" t1.id_estacion = " + argumentosListar.getFiltroEstacion());
			}
			
			// Esto para el filtro de fechas
			if (!argumentosListar.getFiltroFechaInicio().isEmpty() && !argumentosListar.getFiltroFechaFinal().isEmpty()) {
			 filtrosWhere.add(" t1." + FECHA_OPERATIVA + Constante.SQL_ENTRE + ("'" + argumentosListar.getFiltroFechaInicio() + "'" + Constante.SQL_Y + "'" + argumentosListar.getFiltroFechaFinal() + "'"));
			} else {
				if (!argumentosListar.getFiltroFechaInicio().isEmpty()) {
				filtrosWhere.add(" t1." + FECHA_OPERATIVA + " >= '" + argumentosListar.getFiltroFechaInicio() + "'");
				}
				if (!argumentosListar.getFiltroFechaFinal().isEmpty()) {
				filtrosWhere.add(" t1." + FECHA_OPERATIVA + " <= '" + argumentosListar.getFiltroFechaFinal() + "'");
				}
			}

		   if (!filtrosWhere.isEmpty()) {
		    consultaSQL.setLength(0);
		    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
		    consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
		    totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(),null, Integer.class);
		   }
		   
			//tabla otro_movimiento
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_omovimiento,");
			consultaSQL.append("t1.tipo_movimiento,");
			consultaSQL.append("t1.clasificacion,");
			consultaSQL.append("t1.id_jornada,");
			consultaSQL.append("t1.volumen,");
			consultaSQL.append("t1.comentario,");
			consultaSQL.append("t1.id_tanque_origen,");
			consultaSQL.append("t1.id_tanque_destino,");
			consultaSQL.append("t1.numero_movimiento,");
			//tabla jornada
			consultaSQL.append("t1.id_estacion,");
			consultaSQL.append("t1.estado_jornada,");
			consultaSQL.append("t1.operario1,");
			consultaSQL.append("t1.operario2,");
			consultaSQL.append("t1.comentario_jornada,");
			consultaSQL.append("t1.fecha_operativa,");
			//tabla estacion
			consultaSQL.append("t1.nombre_estacion,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.estado_estacion,");
			consultaSQL.append("t1.metodo_descarga,");
			//tabla operacion
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.nombre_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.referencia_planta_recepcion,");
			consultaSQL.append("t1.volumen_promedio_cisterna,");
			consultaSQL.append("t1.referencia_destinatario_mercaderia,");
			consultaSQL.append("t1.estado_operacion,");
			consultaSQL.append("t1.alias,");
			consultaSQL.append("t1.fecha_inicio_planificacion,");			
			consultaSQL.append("t1.eta_origen,");
			consultaSQL.append("t1.planta_despacho_defecto,");
			//tabla tanque ORIGEN 
			consultaSQL.append("t1.descripcion_torigen,");
			consultaSQL.append("t1.vtotal_torigen,");
			consultaSQL.append("t1.vtrabajo_torigen,");
			consultaSQL.append("t1.estado_torigen,");
			consultaSQL.append("t1.ttanque_torigen,");
			consultaSQL.append("t1.ccalibracion_torigen,");
			consultaSQL.append("t1.fecalibracion_torigen,");
			//tabla tanque DESTINO
			consultaSQL.append("t1.descripcion_tdestino,");
			consultaSQL.append("t1.vtotal_tdestino,");
			consultaSQL.append("t1.vtrabajo_tdestino,");
			consultaSQL.append("t1.estado_tdestino,");
			consultaSQL.append("t1.ttanque_tdestino,");
			consultaSQL.append("t1.ccalibracion_tdestino,");
			consultaSQL.append("t1.fecalibracion_tdestino,");
			//auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.ip_creacion,");
			consultaSQL.append("t1.ip_actualizacion,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");	
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new OtroMovimientoMapper());
		
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
			List<OtroMovimiento> listaOtroMovimientos=new ArrayList<OtroMovimiento>();
			Contenido<OtroMovimiento> contenido = new Contenido<OtroMovimiento>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				//tabla otro_movimiento
				consultaSQL.setLength(0);
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_omovimiento,");
				consultaSQL.append("t1.tipo_movimiento,");
				consultaSQL.append("t1.clasificacion,");
				consultaSQL.append("t1.id_jornada,");
				consultaSQL.append("t1.volumen,");
				consultaSQL.append("t1.comentario,");
				consultaSQL.append("t1.id_tanque_origen,");
				consultaSQL.append("t1.id_tanque_destino,");
				consultaSQL.append("t1.numero_movimiento,");
				//tabla jornada
				consultaSQL.append("t1.id_estacion,");
				consultaSQL.append("t1.estado_jornada,");
				consultaSQL.append("t1.operario1,");
				consultaSQL.append("t1.operario2,");
				consultaSQL.append("t1.comentario_jornada,");
				consultaSQL.append("t1.fecha_operativa,");
				//tabla estacion
				consultaSQL.append("t1.nombre_estacion,");
				consultaSQL.append("t1.tipo,");
				consultaSQL.append("t1.estado_estacion,");
				consultaSQL.append("t1.metodo_descarga,");
				//tabla operacion
				consultaSQL.append("t1.id_operacion,");
				consultaSQL.append("t1.nombre_operacion,");
				consultaSQL.append("t1.id_cliente,");
				consultaSQL.append("t1.referencia_planta_recepcion,");
				consultaSQL.append("t1.volumen_promedio_cisterna,");
				consultaSQL.append("t1.referencia_destinatario_mercaderia,");
				consultaSQL.append("t1.estado_operacion,");
				consultaSQL.append("t1.alias,");
				consultaSQL.append("t1.fecha_inicio_planificacion,");			
				consultaSQL.append("t1.eta_origen,");
				consultaSQL.append("t1.planta_despacho_defecto,");
				//tabla tanque ORIGEN 
				consultaSQL.append("t1.descripcion_torigen,");
				consultaSQL.append("t1.vtotal_torigen,");
				consultaSQL.append("t1.vtrabajo_torigen,");
				consultaSQL.append("t1.estado_torigen,");
				consultaSQL.append("t1.ttanque_torigen,");
				consultaSQL.append("t1.ccalibracion_torigen,");
				consultaSQL.append("t1.fecalibracion_torigen,");
				//tabla tanque DESTINO
				consultaSQL.append("t1.descripcion_tdestino,");
				consultaSQL.append("t1.vtotal_tdestino,");
				consultaSQL.append("t1.vtrabajo_tdestino,");
				consultaSQL.append("t1.estado_tdestino,");
				consultaSQL.append("t1.ttanque_tdestino,");
				consultaSQL.append("t1.ccalibracion_tdestino,");
				consultaSQL.append("t1.fecalibracion_tdestino,");
				//auditoria
				consultaSQL.append("t1.creado_el,");
				consultaSQL.append("t1.creado_por,");
				consultaSQL.append("t1.actualizado_por,");
				consultaSQL.append("t1.actualizado_el,");
				consultaSQL.append("t1.ip_creacion,");
				consultaSQL.append("t1.ip_actualizacion,");
				consultaSQL.append("t1.usuario_creacion,");
				consultaSQL.append("t1.usuario_actualizacion");
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaOtroMovimientos= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new OtroMovimientoMapper());
				contenido.totalRegistros=listaOtroMovimientos.size();
				contenido.totalEncontrados=listaOtroMovimientos.size();
				contenido.carga= listaOtroMovimientos;
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
	
	public RespuestaCompuesta guardarRegistro(OtroMovimiento otro_movimiento){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int numero_movimiento = 0;
		int cantidadFilasAfectadas=0;
		try {
			
			consultaSQL.append("SELECT COALESCE(MAX(numero_movimiento),0)+1 FROM sgo.otro_movimiento");
			numero_movimiento = jdbcTemplate.queryForInt(consultaSQL.toString());
			consultaSQL.setLength(0);			
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (tipo_movimiento, clasificacion, id_jornada, volumen, comentario, id_tanque_origen, id_tanque_destino, numero_movimiento, creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:tipoMovimiento,:clasificacion,:idJornada,:volumen,:comentario,:tanqueOrigen,:tanqueDestino,:numeroMovimiento,:creadoEl,:creadoPor,:actualizadoPor,:actualizadoEl,:IpCreacion,:IpActualizacion)");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("tipoMovimiento", otro_movimiento.getTipoMovimiento());
			listaParametros.addValue("clasificacion", otro_movimiento.getClasificacion());
			listaParametros.addValue("idJornada", otro_movimiento.getIdJornada());
			listaParametros.addValue("volumen", otro_movimiento.getVolumen());
			listaParametros.addValue("comentario", otro_movimiento.getComentario());			
			listaParametros.addValue("tanqueOrigen", otro_movimiento.getIdTanqueOrigen());
			listaParametros.addValue("tanqueDestino", otro_movimiento.getIdTanqueDestino());
			listaParametros.addValue("numeroMovimiento", numero_movimiento);			
			listaParametros.addValue("creadoEl", otro_movimiento.getCreadoEl());
			listaParametros.addValue("creadoPor", otro_movimiento.getCreadoPor());			
			listaParametros.addValue("actualizadoEl", otro_movimiento.getActualizadoEl());
			listaParametros.addValue("actualizadoPor", otro_movimiento.getActualizadoPor());
			listaParametros.addValue("IpCreacion", otro_movimiento.getIpCreacion());
			listaParametros.addValue("IpActualizacion", otro_movimiento.getIpActualizacion());
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
	
	public RespuestaCompuesta actualizarRegistro(OtroMovimiento otro_movimiento){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("tipo_movimiento=:tipoMovimiento,");
			consultaSQL.append("clasificacion=:clasificacion,");
			consultaSQL.append("volumen=:volumen,");
			consultaSQL.append("comentario=:comentario,");
			consultaSQL.append("id_tanque_origen=:tanqueOrigen,");
			consultaSQL.append("id_tanque_destino=:tanqueDestino,");
			consultaSQL.append("actualizado_por=:actualizadoPor,");
			consultaSQL.append("actualizado_el=:actualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("tipoMovimiento", otro_movimiento.getTipoMovimiento());
			listaParametros.addValue("clasificacion", otro_movimiento.getClasificacion());
			listaParametros.addValue("volumen", otro_movimiento.getVolumen());
			listaParametros.addValue("comentario", otro_movimiento.getComentario());			
			listaParametros.addValue("tanqueOrigen", otro_movimiento.getIdTanqueOrigen());
			listaParametros.addValue("tanqueDestino", otro_movimiento.getIdTanqueDestino());
			listaParametros.addValue("actualizadoEl", otro_movimiento.getActualizadoEl());
			listaParametros.addValue("actualizadoPor", otro_movimiento.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", otro_movimiento.getIpActualizacion());
			listaParametros.addValue("id", otro_movimiento.getId());
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