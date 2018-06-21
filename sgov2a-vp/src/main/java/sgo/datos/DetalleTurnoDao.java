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
import sgo.entidad.DetalleGEC;
import sgo.entidad.DetalleGecVista;
import sgo.entidad.DetalleTurno;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class DetalleTurnoDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "detalle_turno";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_detalle_turno";
  public final static String NOMBRE_CAMPO_CLAVE = "id_dturno";
  public final static String NOMBRE_CAMPO_CLAVE_TURNO = "id_turno";
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }
  
  public DataSource getDataSource(){
    return this.jdbcTemplate.getDataSource();
  }
  
  public String mapearCampoOrdenamiento(String propiedad){
    String campoOrdenamiento="id_dturno";
    try {
      if (propiedad.equals("id")){
        campoOrdenamiento="id_dturno";
      }
      if (propiedad.equals("idTurno")){
        campoOrdenamiento="id_turno";
      }
      if (propiedad.equals("lecturaInicial")){
        campoOrdenamiento="lectura_inicial";
      }
      if (propiedad.equals("lecturaFinal")){
        campoOrdenamiento="lectura_final";
      }
      if (propiedad.equals("idProducto")){
        campoOrdenamiento="id_producto";
      }
      if (propiedad.equals("idContometro")){
        campoOrdenamiento="id_contometro";
      }
      if (propiedad.equals("fechaHoraCierre")){
          campoOrdenamiento="fecha_hora_cierre";
        }

    }catch(Exception excepcion){
      
    }
    return campoOrdenamiento;
  }

  public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
	  StringBuilder consultaSQL = new StringBuilder();
	  RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<DetalleTurno> contenido = new Contenido<DetalleTurno>();
	  List<String> filtrosWhere= new ArrayList<String>();
	  List<DetalleTurno> listaRegistros = new ArrayList<DetalleTurno>();
	  List<Object> parametros = new ArrayList<Object>();
	  String sqlWhere = "";
	  String sqlOrderBy="";
	  try {	  
	 	 
	 	 
	 	   if (argumentosListar.getIdJornada() > 0) {
	 		    filtrosWhere.add(" t1.id_jornada = "
	 		      + argumentosListar.getIdJornada());
	 		}
	 	   
	 	   if (argumentosListar.getIdTurno() > 0) {
	 		    filtrosWhere.add(" t1.id_turno = "
	 		      + argumentosListar.getIdTurno());
	 		}
	 	   
	 		if (!filtrosWhere.isEmpty()) {
	 			consultaSQL.setLength(0);
	 			sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
	 		}
	 		sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
	    consultaSQL.append("SELECT ");
	    consultaSQL.append("t1.id_turno,");
	    consultaSQL.append("t1.id_dturno,");
	    consultaSQL.append("t1.lectura_inicial,");
	    consultaSQL.append("t1.lectura_final,");
	    consultaSQL.append("t1.id_producto,");
	    consultaSQL.append("t1.nombre_producto,");
	    consultaSQL.append("t1.id_contometro,");
	    consultaSQL.append("t1.alias_contometro,");
	    consultaSQL.append("t1.id_jornada,");
	    consultaSQL.append("t1.estado,");
	    consultaSQL.append("t1.id_estacion,");
	    consultaSQL.append("t1.estacion,");
	    consultaSQL.append("t1.fecha_hora_apertura,");
	    consultaSQL.append("t1.fecha_hora_cierre,");
	    consultaSQL.append("t1.observacion,");
	    consultaSQL.append("t1.creado_el,");
	    consultaSQL.append("t1.creado_por,");
	    consultaSQL.append("t1.actualizado_por,");
	    consultaSQL.append("t1.actualizado_el,");
	    consultaSQL.append("t1.ip_creacion,");
	    consultaSQL.append("t1.ip_actualizacion,");
	    consultaSQL.append("t1.usuario_creacion,");
	    consultaSQL.append("t1.usuario_actualizacion,");
	    consultaSQL.append("t1.fecha_operativa");
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(sqlWhere);
      consultaSQL.append(sqlOrderBy);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(), parametros.toArray(), new DetalleTurnoMapper());
      contenido.totalRegistros = listaRegistros.size();
      contenido.totalEncontrados = listaRegistros.size();
      contenido.carga = listaRegistros;
      respuesta.mensaje = "OK";
      respuesta.estado = true;
      respuesta.contenido = contenido;
     } catch (DataAccessException excepcionAccesoDatos) {
      excepcionAccesoDatos.printStackTrace();
      respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado = false;
      respuesta.contenido = null;
     }
     return respuesta;
  }
  

  
  
  public RespuestaCompuesta recuperarRegistro(int ID){
      StringBuilder consultaSQL= new StringBuilder();   
      List<DetalleTurno> listaRegistros=new ArrayList<DetalleTurno>();
      Contenido<DetalleTurno> contenido = new Contenido<DetalleTurno>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
  	    consultaSQL.append("SELECT ");
  	    consultaSQL.append("t1.id_turno,");
  	    consultaSQL.append("t1.id_dturno,");
  	    consultaSQL.append("t1.lectura_inicial,");
  	    consultaSQL.append("t1.lectura_final,");
  	    consultaSQL.append("t1.id_producto,");
  	    consultaSQL.append("t1.nombre_producto,");
  	    consultaSQL.append("t1.id_contometro,");
  	    consultaSQL.append("t1.alias_contometro,");
  	    consultaSQL.append("t1.id_jornada,");
  	    consultaSQL.append("t1.estado,");
  	    consultaSQL.append("t1.id_estacion,");
  	    consultaSQL.append("t1.estacion,");
  	    consultaSQL.append("t1.fecha_hora_apertura,");
  	    consultaSQL.append("t1.fecha_hora_cierre,");
  	    consultaSQL.append("t1.observacion,");
  	    consultaSQL.append("t1.creado_el,");
  	    consultaSQL.append("t1.creado_por,");
  	    consultaSQL.append("t1.actualizado_por,");
  	    consultaSQL.append("t1.actualizado_el,");
  	    consultaSQL.append("t1.ip_creacion,");
  	    consultaSQL.append("t1.ip_actualizacion,");
  	    consultaSQL.append("t1.usuario_creacion,");
  	    consultaSQL.append("t1.usuario_actualizacion,");
  	    consultaSQL.append("t1.fecha_operativa");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DetalleTurnoMapper());
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
  
  public RespuestaCompuesta recuperarRegistroDetalleTurno(int idTurno){
      StringBuilder consultaSQL= new StringBuilder();   
      List<DetalleTurno> listaRegistros=new ArrayList<DetalleTurno>();
      Contenido<DetalleTurno> contenido = new Contenido<DetalleTurno>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
  	    consultaSQL.append("SELECT ");
  	    consultaSQL.append("t1.id_turno,");
  	    consultaSQL.append("t1.id_dturno,");
  	    consultaSQL.append("t1.lectura_inicial,");
  	    consultaSQL.append("t1.lectura_final,");
  	    consultaSQL.append("t1.id_producto,");
  	    consultaSQL.append("t1.nombre_producto,");
  	    consultaSQL.append("t1.id_contometro,");
  	    consultaSQL.append("t1.alias_contometro,");
  	    consultaSQL.append("t1.id_jornada,");
  	    consultaSQL.append("t1.estado,");
  	    consultaSQL.append("t1.id_estacion,");
  	    consultaSQL.append("t1.estacion,");
  	    consultaSQL.append("t1.fecha_hora_apertura,");
  	    consultaSQL.append("t1.fecha_hora_cierre,");
  	    consultaSQL.append("t1.observacion,");
  	    consultaSQL.append("t1.creado_el,");
  	    consultaSQL.append("t1.creado_por,");
  	    consultaSQL.append("t1.actualizado_por,");
  	    consultaSQL.append("t1.actualizado_el,");
  	    consultaSQL.append("t1.ip_creacion,");
  	    consultaSQL.append("t1.ip_actualizacion,");
  	    consultaSQL.append("t1.usuario_creacion,");
  	    consultaSQL.append("t1.usuario_actualizacion,");
        consultaSQL.append("t1.fecha_operativa");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE_TURNO);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {idTurno},new DetalleTurnoMapper());
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
  
  public RespuestaCompuesta guardarRegistro(DetalleTurno detalle_turno){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_turno, lectura_inicial, lectura_final, id_producto,id_contometro) ");
      consultaSQL.append(" VALUES (:IdTurno,:Lectura_inicial,:Lectura_final,:IdProducto,:IdContometro) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdTurno", detalle_turno.getIdTurno());
      listaParametros.addValue("Lectura_inicial", detalle_turno.getLecturaInicial());
      listaParametros.addValue("Lectura_final", detalle_turno.getLecturaFinal());
      listaParametros.addValue("IdProducto", detalle_turno.getIdProducto());
      listaParametros.addValue("IdContometro", detalle_turno.getIdContometro());
      
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
  
  public RespuestaCompuesta actualizarRegistro(DetalleTurno detalleTurno){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_turno=:IdTurno,");
      consultaSQL.append("lectura_inicial=:LecturaInicial,");
      consultaSQL.append("lectura_final=:LecturaFinal,");
      consultaSQL.append("id_producto=:IdProducto,");
      consultaSQL.append("id_contometro=:IdContometro");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("IdTurno", detalleTurno.getIdTurno());
      listaParametros.addValue("LecturaInicial", detalleTurno.getLecturaInicial());
      listaParametros.addValue("LecturaFinal", detalleTurno.getLecturaFinal());
      listaParametros.addValue("IdProducto", detalleTurno.getIdProducto());
      listaParametros.addValue("IdContometro", detalleTurno.getIdContometro());
      //Valores Auditoria
      listaParametros.addValue("Id", detalleTurno.getId());
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
}