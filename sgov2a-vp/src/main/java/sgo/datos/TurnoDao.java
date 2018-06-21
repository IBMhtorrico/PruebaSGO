package sgo.datos;

import java.sql.Timestamp;
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
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Turno;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class TurnoDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "turno";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_turno";
  public final static String NOMBRE_CAMPO_CLAVE = "id_turno";
  public final static String NOMBRE_CAMPO_FILTRO = "fecha_carga";
  public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "fecha_hora_apertura";
  
  public final static String O = "OR";
  public final static String Y = "AND";
  public final static String ENTRE = "BETWEEN";
  public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
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
      if (propiedad.equals("id")){
        campoOrdenamiento="id_turno";
      }
      if (propiedad.equals("idJornada")){
        campoOrdenamiento="id_jornada";
      }
     
      if (propiedad.equals("fechaHoraApertura")){
        campoOrdenamiento="fecha_hora_apertura";
      }
      if (propiedad.equals("fechaHoraCierre")){
        campoOrdenamiento="fecha_hora_cierre";
      }
      if (propiedad.equals("volumenDespachado")){
        campoOrdenamiento="volumen_despachado";
      }
      if (propiedad.equals("volumenRecibido")){
        campoOrdenamiento="volumen_recibido";
      }
      
      if (propiedad.equals("responsable")){
          campoOrdenamiento="responsable";
        }
      if (propiedad.equals("ayudante")){
          campoOrdenamiento="ayudante";
      }
      if (propiedad.equals("comentario")){
          campoOrdenamiento="comentario";
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
		Contenido<Turno> contenido = new Contenido<Turno>();
		List<Turno> listaRegistros = new ArrayList<Turno>();
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
		/*if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
			filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
		}*/
		if(!argumentosListar.getFiltroFechaJornada().isEmpty()){
			filtrosWhere.add(" t1."+ FECHA_OPERATIVA + " ='" + argumentosListar.getFiltroFechaJornada() + "' ");
		}
		
		if(argumentosListar.getIdJornada()!= Constante.FILTRO_TODOS){
			filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada() + " ");
		}
		
		if(argumentosListar.getFiltroOperacion() != Constante.FILTRO_TODOS){
			filtrosWhere.add(" t1.id_operacion = " + argumentosListar.getFiltroOperacion() + " ");
		}
		
		if(argumentosListar.getFiltroEstacion() != Constante.FILTRO_TODOS){
			filtrosWhere.add(" t1.id_estacion = " + argumentosListar.getFiltroEstacion() + " ");
		}

		if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
			filtrosWhere.add(" t1.estado = " + argumentosListar.getFiltroEstado() + " ");
		}
		// Esto es para un rango de fechas
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

		if(!filtrosWhere.isEmpty()){
			consultaSQL.setLength(0);
			sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
			
			consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
			totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
		}
    	
    	
    	
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_turno,");
      consultaSQL.append("t1.fecha_hora_apertura,");
      consultaSQL.append("t1.id_estacion,");
      consultaSQL.append("t1.id_jornada,");
      consultaSQL.append("t1.fecha_operativa,");
      consultaSQL.append("t1.estacion,");
      consultaSQL.append("t1.id_operacion,");
      consultaSQL.append("t1.cantidad_turnos,");
      consultaSQL.append("t1.nombre_operario,");
      consultaSQL.append("t1.apellido_paterno_operario,");
      consultaSQL.append("t1.apellido_materno_operario,");
      consultaSQL.append("t1.ayudante,");
      consultaSQL.append("t1.ayudante_paterno,");
      consultaSQL.append("t1.ayudante_materno,");
      consultaSQL.append("t1.estado,");
      consultaSQL.append("t1.comentario,");
      consultaSQL.append("t1.fecha_hora_cierre,");
      consultaSQL.append("t1.observacion,");
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
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new TurnoMapper());
      //totalEncontrados =totalRegistros;
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
      List<Turno> listaRegistros=new ArrayList<Turno>();
      Contenido<Turno> contenido = new Contenido<Turno>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
          consultaSQL.append("SELECT ");
          consultaSQL.append("t1.id_turno,");
          consultaSQL.append("t1.fecha_hora_apertura,");
          consultaSQL.append("t1.id_estacion,");
          consultaSQL.append("t1.id_jornada,");
          consultaSQL.append("t1.fecha_operativa,");
          consultaSQL.append("t1.estacion,");
          consultaSQL.append("t1.id_operacion,");
          consultaSQL.append("t1.nombre_operario,");
          consultaSQL.append("t1.cantidad_turnos,");
          consultaSQL.append("t1.apellido_paterno_operario,");
          consultaSQL.append("t1.apellido_materno_operario,");
          consultaSQL.append("t1.ayudante,");
          consultaSQL.append("t1.ayudante_paterno,");
          consultaSQL.append("t1.ayudante_materno,");
          consultaSQL.append("t1.estado,");
          consultaSQL.append("t1.comentario,");
          consultaSQL.append("t1.fecha_hora_cierre,");
          consultaSQL.append("t1.observacion,");
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
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new TurnoMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Turno turno){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" ( fecha_hora_apertura,id_jornada, responsable, ayudante,estado,comentario,observacion, creado_el, creado_por, ip_creacion, actualizado_el, actualizado_por, ip_actualizacion) ");
      consultaSQL.append(" VALUES (:FechaHoraApertura,:IdJornada,:Responsable,:Ayudante,:Estado,:Comentario,:Observacion,:CreadoEl,:CreadoPor,:IpCreacion,:ActualizadoEl,:ActualizadoPor,:IpActualizacion) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("FechaHoraApertura", turno.getFechaHoraApertura());
      listaParametros.addValue("IdJornada", turno.getIdJornada());
      listaParametros.addValue("Responsable", turno.getIdResponsable());
      listaParametros.addValue("Ayudante", turno.getIdAyudante());
      listaParametros.addValue("Estado", turno.getEstado());
      listaParametros.addValue("Comentario", turno.getComentario());
      listaParametros.addValue("Observacion", turno.getObservacion());
      listaParametros.addValue("CreadoEl", turno.getCreadoEl());
      listaParametros.addValue("ActualizadoEl", turno.getActualizadoEl());
      listaParametros.addValue("CreadoPor", turno.getCreadoPor());
      listaParametros.addValue("ActualizadoPor", turno.getActualizadoPor());
      listaParametros.addValue("IpCreacion", turno.getIpCreacion());
      listaParametros.addValue("IpActualizacion", turno.getIpActualizacion());
      listaParametros.addValue("FechaHoraCierre", turno.getFechaHoraCierre());
        
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
  
  public RespuestaCompuesta actualizarRegistro(Turno turno){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("estado=:Estado,");
      consultaSQL.append("observacion=:Observacion,");
      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion,");
      consultaSQL.append("fecha_hora_cierre=:FechaHoraCierre");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("Estado", turno.getEstado());
      listaParametros.addValue("Observacion", turno.getObservacion());
      
      //Valores Auditoria
      listaParametros.addValue("id", turno.getId());
      listaParametros.addValue("ActualizadoPor", turno.getActualizadoPor());
      listaParametros.addValue("ActualizadoEl", turno.getActualizadoEl());
      listaParametros.addValue("IpActualizacion", turno.getIpActualizacion());
      listaParametros.addValue("FechaHoraCierre", turno.getFechaHoraCierre());
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
  
  public RespuestaCompuesta eliminarRegistros(int idRegistro){   
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    int cantidadFilasAfectadas=0; 
    String consultaSQL="";
    Object[] parametros = {idRegistro};
    try {
      consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE id_gcombustible=?";
          cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
      if (cantidadFilasAfectadas <1){
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
	public Respuesta recuperarUltimoTurnoCerrado(ParametrosListar argumentosListar) {
		  Timestamp ultimoDia = null;
		  Respuesta respuesta = new Respuesta();
		  List<String> filtrosWhere= new ArrayList<String>();
		  String sqlWhere="";
	      List<Object> parametros = new ArrayList<Object>();

		  try {
		   StringBuilder consultaSQL = new StringBuilder();
		   
			if(argumentosListar.getIdJornada()!= Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.id_jornada = " + argumentosListar.getIdJornada() + " ");
			}
			if(argumentosListar.getFiltroEstado()!= Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1.estado = " + argumentosListar.getFiltroEstado() + " ");
			}			

			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = " WHERE " + StringUtils.join(filtrosWhere, Y);
			}

		   consultaSQL.append("select max(t1.fecha_hora_cierre) as fecha");
		   consultaSQL.append(" FROM sgo.turno t1 ");
		   consultaSQL.append(sqlWhere);
		   ultimoDia = jdbcTemplate.queryForObject(consultaSQL.toString(),parametros.toArray(), Timestamp.class);		   
		   if (ultimoDia == null) {
		    respuesta.estado = true;
		    respuesta.valor = null;
		   } else {
		    respuesta.estado = true;
		    respuesta.valor = Utilidades.convierteDateAString(ultimoDia, "yyyy/MM/dd HH:mm:s");
		   }
		  } catch (DataAccessException excepcionAccesoDatos) {
		   excepcionAccesoDatos.printStackTrace();
		   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
		   respuesta.estado = false;
		  } catch (Exception excepcionGenerica) {
		   excepcionGenerica.printStackTrace();
		   respuesta.error = Constante.EXCEPCION_GENERICA;
		   respuesta.estado = false;
		  }
		  return respuesta;
		 }
}