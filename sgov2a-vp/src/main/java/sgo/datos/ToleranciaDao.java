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
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tolerancia;
import sgo.utilidades.Constante;

@Repository
public class ToleranciaDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "tolerancia";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_tolerancia";
  public final static String NOMBRE_CAMPO_CLAVE = "id_tolerancia";
  public final static String CAMPO_ESTACION = "id_estacion";
  public final static String CAMPO_ID_PRODUCTO = "id_producto";
  
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
        campoOrdenamiento="id_tolerancia";
      }
      if (propiedad.equals("idEstacion")){
        campoOrdenamiento="id_estacion";
      }
      if (propiedad.equals("idProducto")){
        campoOrdenamiento="id_producto";
      }
      if (propiedad.equals("porcentajeActual")){
        campoOrdenamiento="porcentaje_actual";
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
    Contenido<Tolerancia> contenido = new Contenido<Tolerancia>();
    List<Tolerancia> listaRegistros = new ArrayList<Tolerancia>();
    List<Object> parametros = new ArrayList<Object>();
    List<String> filtrosWhere = new ArrayList<String>();
    String sqlWhere="";
    try {
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }
      
      if (argumentosListar.getFiltroEstacion()>0){
       filtrosWhere.add(" t1."+CAMPO_ESTACION+"= '"+ argumentosListar.getFiltroEstacion() +"' ");
     }
      
      if (argumentosListar.getFiltroProducto()>0){
       filtrosWhere.add(" t1."+CAMPO_ID_PRODUCTO+"= '"+ argumentosListar.getFiltroProducto() +"' ");
     }
      
      if (!argumentosListar.getAbreviaturaProducto().isEmpty()){
		filtrosWhere.add("lower(t2.abreviatura) like lower('%"+ argumentosListar.getAbreviaturaProducto() +"%') ");
      }
		
     if (argumentosListar.getFiltroOperacion()>0){
       filtrosWhere.add(" t1."+ CAMPO_ESTACION +" IN (SELECT ID_ESTACION FROM SGO.ESTACION WHERE ID_OPERACION = '" + argumentosListar.getFiltroOperacion() + "')' ");
     }
     
     if (!filtrosWhere.isEmpty()) {
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     }
      
      StringBuilder consultaSQL = new StringBuilder();
      consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
      totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_tolerancia,");
      consultaSQL.append("t1.id_estacion,");
      consultaSQL.append("t1.id_producto,");
      consultaSQL.append("t1.tipo_volumen,");
      consultaSQL.append("t2.nombre as nombre_producto,");
      consultaSQL.append("t2.abreviatura as abreviatura_producto,");
      consultaSQL.append("t1.porcentaje_actual");
      //Campos de auditoria
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 INNER JOIN sgo.producto t2 ON t1.id_producto=t2.id_producto ");
      consultaSQL.append(sqlWhere);
      consultaSQL.append(sqlLimit);
      System.out.println(consultaSQL.toString());
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ToleranciaMapper());
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
      List<Tolerancia> listaRegistros=new ArrayList<Tolerancia>();
      Contenido<Tolerancia> contenido = new Contenido<Tolerancia>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_tolerancia,");
        consultaSQL.append("t1.id_estacion,");
        consultaSQL.append("t1.id_producto,");
        consultaSQL.append("t1.porcentaje_actual,");
        consultaSQL.append("t1.tipo_volumen");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new ToleranciaMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Tolerancia tolerancia){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_estacion,id_producto,porcentaje_actual,tipo_volumen) ");
      consultaSQL.append(" VALUES (:IdEstacion,:IdProducto,:PorcentajeActual,:TipoVolumen) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdEstacion", tolerancia.getIdEstacion());
      listaParametros.addValue("IdProducto", tolerancia.getIdProducto());
      listaParametros.addValue("TipoVolumen", tolerancia.getTipoVolumen());
      listaParametros.addValue("PorcentajeActual", tolerancia.getPorcentajeActual());
      
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
  
  public RespuestaCompuesta actualizarRegistro(Tolerancia tolerancia){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_estacion=:IdEstacion,");
      consultaSQL.append("id_producto=:IdProducto,");
      consultaSQL.append("tipo_volumen=:TipoVolumen,");
      consultaSQL.append("porcentaje_actual=:PorcentajeActual,");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("IdEstacion", tolerancia.getIdEstacion());
      listaParametros.addValue("IdProducto", tolerancia.getIdProducto());
      listaParametros.addValue("TipoVolumen", tolerancia.getTipoVolumen());
      listaParametros.addValue("PorcentajeActual", tolerancia.getPorcentajeActual());
      //Valores Auditoria
      listaParametros.addValue("Id", tolerancia.getId());
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
  
  public RespuestaCompuesta eliminarRegistros(int idRegistro){   
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   int cantidadFilasAfectadas=0; 
   String consultaSQL="";
   Object[] parametros = {idRegistro};
   try {
     consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + CAMPO_ESTACION + "=?";
     cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
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