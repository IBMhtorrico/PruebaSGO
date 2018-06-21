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

import sgo.entidad.AforoTanque;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class AforoTanqueDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "aforo_tanque";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_aforo_tanque";
  public final static String NOMBRE_CAMPO_CLAVE = "id_atanque";
  public final static String CAMPO_CENTIMETROS  = "centimetros";
  public final static String CAMPO_ID_TANQUE = "id_tanque";
  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }
  
  public DataSource getDataSource(){
    return this.jdbcTemplate.getDataSource();
  }
  
  public String mapearCampoOrdenamiento(String propiedad){
    String campoOrdenamiento="id_atanque";
    try {
      if (propiedad.equals("id")){
        campoOrdenamiento="id_atanque";
      }
      if (propiedad.equals("centimetros")){
        campoOrdenamiento="centimetros";
      }
      if (propiedad.equals("idTanque")){
        campoOrdenamiento="id_tanque";
      }
      if (propiedad.equals("volumen")){
        campoOrdenamiento="volumen";
      }
      //Campos de auditoria
    }catch(Exception excepcion){
      
    }
    return campoOrdenamiento;
  }

  public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
    String sqlLimit = "";
    int totalRegistros = 0, totalEncontrados = 0;
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    Contenido<AforoTanque> contenido = new Contenido<AforoTanque>();
    List<AforoTanque> listaRegistros = new ArrayList<AforoTanque>();
    List<Object> parametros = new ArrayList<Object>();
    String sqlWhere ="";
    String sqlOrderBy="";
    List<String> filtrosWhere= new ArrayList<String>();
    try {
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }
      
      if (argumentosListar.getIdTanque() != Constante.FILTRO_TODOS){
          filtrosWhere.add(" "+CAMPO_ID_TANQUE+" = '"+ argumentosListar.getIdTanque() +"' ");
      }
      
      if(argumentosListar.getFiltroCentimetros()>0){
          filtrosWhere.add(" "+ CAMPO_CENTIMETROS + "=" + argumentosListar.getFiltroCentimetros());
       }
      
      if(!argumentosListar.getTxtQuery().isEmpty()){
          filtrosWhere.add(argumentosListar.getTxtQuery());
          //sqlLimit = "   OFFSET ? LIMIT 1 ";
       }
      
      
      if(!filtrosWhere.isEmpty()){
          sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
       }
      
      sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
      
      
      
      StringBuilder consultaSQL = new StringBuilder();
      consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA + " t1 "+sqlWhere);
      totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_atanque,");
      consultaSQL.append("t1.centimetros,");
      consultaSQL.append("t1.id_tanque,");
      consultaSQL.append("t1.volumen,");
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
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new AforoTanqueMapper());
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
      List<AforoTanque> listaRegistros=new ArrayList<AforoTanque>();
      Contenido<AforoTanque> contenido = new Contenido<AforoTanque>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_atanque,");
        consultaSQL.append("t1.centimetros,");
        consultaSQL.append("t1.id_tanque,");
        consultaSQL.append("t1.volumen,");
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
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new AforoTanqueMapper());
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
  
  public RespuestaCompuesta guardarRegistro(AforoTanque aforo_tanque){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (centimetros,id_tanque,volumen,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
      consultaSQL.append(" VALUES (:Centimetros,:IdTanque,:Volumen,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("Centimetros", aforo_tanque.getCentimetros());
      listaParametros.addValue("IdTanque", aforo_tanque.getIdTanque());
      listaParametros.addValue("Volumen", aforo_tanque.getVolumen());
      listaParametros.addValue("CreadoEl", aforo_tanque.getCreadoEl());
      listaParametros.addValue("CreadoPor", aforo_tanque.getCreadoPor());
      listaParametros.addValue("ActualizadoPor", aforo_tanque.getActualizadoPor());
      listaParametros.addValue("ActualizadoEl", aforo_tanque.getActualizadoEl());
      listaParametros.addValue("IpCreacion", aforo_tanque.getIpCreacion());
      listaParametros.addValue("IpActualizacion", aforo_tanque.getIpActualizacion());
      
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
  
  public RespuestaCompuesta actualizarRegistro(AforoTanque aforo_tanque){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("centimetros=:Centimetros,");
      consultaSQL.append("id_tanque=:IdTanque,");
      consultaSQL.append("volumen=:Volumen,");
       
      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("Centimetros", aforo_tanque.getCentimetros());
      listaParametros.addValue("IdTanque", aforo_tanque.getIdTanque());
      listaParametros.addValue("Volumen", aforo_tanque.getVolumen());
      //Valores Auditoria
      listaParametros.addValue("ActualizadoEl", aforo_tanque.getActualizadoEl());
      listaParametros.addValue("ActualizadoPor", aforo_tanque.getActualizadoPor());
      listaParametros.addValue("IpActualizacion", aforo_tanque.getIpActualizacion());
      listaParametros.addValue("Id", aforo_tanque.getId());
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
      if (cantidadFilasAfectadas > 1) {
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
  
  public RespuestaCompuesta eliminarRegistros(ArrayList<String> listaIds, ArrayList<String> listaAlturas,int idTanque){   
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   int cantidadFilasAfectadas=0; 
   String consultaSQL="";
   String cadenaIds = StringUtils.join(listaIds.toArray(),",");
   String cadenaAlturas = "";
   try {
    if (listaAlturas!=null ){
     cadenaAlturas=StringUtils.join(listaAlturas.toArray(),",");
    }
     consultaSQL="DELETE FROM " + NOMBRE_TABLA;
     consultaSQL+= " WHERE id_tanque='"+idTanque+"' and id_atanque NOT IN ("+cadenaIds+") ";
     if (cadenaAlturas.length()>0){
      consultaSQL+= " AND centimetros in ("+cadenaAlturas+") ";
     }
     cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL);
     if (cantidadFilasAfectadas > 1) {
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