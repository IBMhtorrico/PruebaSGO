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
import sgo.entidad.GuiaCombustible;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class GuiaCombustibleDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "guia_combustible";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_guia_combustible";
	public final static String NOMBRE_CAMPO_CLAVE = "id_gcombustible";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}
	
	public String mapearCampoOrdenamiento(String propiedad){
		String campoOrdenamiento="";
		try {
			if (propiedad.equals("id")){
				campoOrdenamiento="id_gcombustible";
			}
			if (propiedad.equals("ordenCompra")){
				campoOrdenamiento="orden_compra";
			}
			if (propiedad.equals("fechaGuiaCombustible")){
				campoOrdenamiento="fecha_guia_combustible";
			}
			if (propiedad.equals("idTransportista")){
				campoOrdenamiento="id_transportista";
			}
			if (propiedad.equals("numeroGEC")){
				campoOrdenamiento="numero_gec";
			}
			if (propiedad.equals("numeroContrato")){
				campoOrdenamiento="numero_contrato";
			}
			if (propiedad.equals("descripcionContrato")){
				campoOrdenamiento="descripcion_contrato";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
			}
			if (propiedad.equals("comentarios")){
				campoOrdenamiento="comentarios";
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
     
     if (!argumentosListar.getQueryRolGec().isEmpty()){
    	 filtrosWhere.add( argumentosListar.getQueryRolGec() );
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
			consultaSQL.append("t1.referencia_planta_recepcion,");
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
	}
	
	
	public RespuestaCompuesta recuperarRegistro(int ID){
			StringBuilder consultaSQL= new StringBuilder();		
			List<GuiaCombustible> listaRegistros=new ArrayList<GuiaCombustible>();
			Contenido<GuiaCombustible> contenido = new Contenido<GuiaCombustible>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_gcombustible,");
				consultaSQL.append("t1.orden_compra,");
				consultaSQL.append("t1.referencia_planta_recepcion,");
				consultaSQL.append("t1.fecha_guia_combustible,");
				consultaSQL.append("t1.id_transportista,");
				consultaSQL.append("t1.numero_gec,");
				consultaSQL.append("t1.numero_contrato,");
				consultaSQL.append("t1.descripcion_contrato,");
				consultaSQL.append("t1.estado,");
				consultaSQL.append("t1.comentarios,");
				consultaSQL.append("t1.id_producto,");
	      consultaSQL.append("t1.cliente,");
	      consultaSQL.append("t1.codigo_referencia,"); 
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
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new GuiaCombustibleMapper());
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
	
	 public RespuestaCompuesta recuperarNumeroGec(int idCliente){
    StringBuilder consultaSQL= new StringBuilder();   
    List<GuiaCombustible> listaRegistros=new ArrayList<GuiaCombustible>();
    Contenido<GuiaCombustible> contenido = new Contenido<GuiaCombustible>();
    RespuestaCompuesta respuesta= new RespuestaCompuesta();
    try {
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_gcombustible,");
      consultaSQL.append("t1.orden_compra,");
      consultaSQL.append("t1.referencia_planta_recepcion,");
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
      consultaSQL.append(" WHERE cliente");
      consultaSQL.append("=?");
      consultaSQL.append(" order by CAST(serie_gec AS INTEGER)  desc, CAST(numero_gec AS INTEGER) desc LIMIT 1;");
      listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {idCliente},new GuiaCombustibleMapper());
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
	
	
	public RespuestaCompuesta guardarRegistro(GuiaCombustible guia_combustible){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (total_volumen_despachado,total_volumen_recibido,serie_gec,operacion,cliente,orden_compra,fecha_guia_combustible,id_transportista,numero_gec,numero_contrato,descripcion_contrato,estado,comentarios,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion,id_producto) ");
			consultaSQL.append(" VALUES (:TotalVolumenDespachado,:TotalVolumenRecibido,:Serie,:Operacion,:Cliente,:OrdenCompra,:FechaGuiaCombustible,:IdTransportista,:NumeroGEC,:NumeroContrato,:DescripcionContrato,:Estado,:Comentarios,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion,:IdProducto) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Serie", guia_combustible.getNumeroSerie());
			listaParametros.addValue("Operacion", guia_combustible.getIdOperacion());
			listaParametros.addValue("Cliente", guia_combustible.getIdCliente());
			listaParametros.addValue("OrdenCompra", guia_combustible.getOrdenCompra());
			listaParametros.addValue("FechaGuiaCombustible", guia_combustible.getFechaGuiaCombustible());
			listaParametros.addValue("IdTransportista", guia_combustible.getIdTransportista());
			listaParametros.addValue("NumeroGEC", guia_combustible.getNumeroGEC());
			listaParametros.addValue("NumeroContrato", guia_combustible.getNumeroContrato());
			listaParametros.addValue("DescripcionContrato", guia_combustible.getDescripcionContrato());
			listaParametros.addValue("Estado", guia_combustible.getEstado());
			listaParametros.addValue("Comentarios", guia_combustible.getComentarios());
	     listaParametros.addValue("TotalVolumenDespachado", guia_combustible.getTotalVolumenDespachado());
	      listaParametros.addValue("TotalVolumenRecibido", guia_combustible.getTotalVolumenRecibido());
			listaParametros.addValue("CreadoEl", guia_combustible.getCreadoEl());
			listaParametros.addValue("CreadoPor", guia_combustible.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", guia_combustible.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", guia_combustible.getActualizadoEl());
			listaParametros.addValue("IpCreacion", guia_combustible.getIpCreacion());
			listaParametros.addValue("IpActualizacion", guia_combustible.getIpActualizacion());
			listaParametros.addValue("IdProducto", guia_combustible.getIdProducto());
			
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
	
	public RespuestaCompuesta actualizarRegistro(GuiaCombustible guia_combustible){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("orden_compra=:OrdenCompra,");
			consultaSQL.append("fecha_guia_combustible=:FechaGuiaCombustible,");
			consultaSQL.append("id_transportista=:IdTransportista,");
			consultaSQL.append("numero_gec=:NumeroGEC,");
			consultaSQL.append("numero_contrato=:NumeroContrato,");
			consultaSQL.append("descripcion_contrato=:DescripcionContrato,");
			if(guia_combustible.getEstado()>0)consultaSQL.append("estado=:Estado,");
			consultaSQL.append("comentarios=:Comentarios,");
			consultaSQL.append("id_producto=:IdProducto,");
			 
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("OrdenCompra", guia_combustible.getOrdenCompra());
			listaParametros.addValue("FechaGuiaCombustible", guia_combustible.getFechaGuiaCombustible());
			listaParametros.addValue("IdTransportista", guia_combustible.getIdTransportista());
			listaParametros.addValue("NumeroGEC", guia_combustible.getNumeroGEC());
			listaParametros.addValue("NumeroContrato", guia_combustible.getNumeroContrato());
			listaParametros.addValue("DescripcionContrato", guia_combustible.getDescripcionContrato());
			if(guia_combustible.getEstado()>0)listaParametros.addValue("Estado", guia_combustible.getEstado());
			listaParametros.addValue("Comentarios", guia_combustible.getComentarios());
			listaParametros.addValue("IdProducto", guia_combustible.getIdProducto());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", guia_combustible.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", guia_combustible.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", guia_combustible.getIpActualizacion());
			listaParametros.addValue("Id", guia_combustible.getId());
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
	
	 public RespuestaCompuesta actualizaEstado(GuiaCombustible guia_combustible){
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
      listaParametros.addValue("Estado", guia_combustible.getEstado());
      //Valores Auditoria      
      listaParametros.addValue("ActualizadoPor", guia_combustible.getActualizadoPor());
      listaParametros.addValue("ActualizadoEl", guia_combustible.getActualizadoEl());
      listaParametros.addValue("IpActualizacion", guia_combustible.getIpActualizacion());
      listaParametros.addValue("Id", guia_combustible.getId());
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