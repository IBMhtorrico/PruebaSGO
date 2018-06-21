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

import sgo.entidad.Tabla5B;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class Tabla5BDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "tabla5b";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_tabla5b";
  public final static String NOMBRE_CAMPO_CLAVE = "id_tabla5b";
  public final static String CAMPO_API_OBSERVADO = "api_observado";
  public final static String CAMPO_TEMPERATURA_OBSERVADA = "temperatura_observada";
  public final static String NOMBRE_CAMPO_API_OBSERVADO = "apiObservado";
  public final static String NOMBRE_CAMPO_TEMPERATURA_OBSERVADA = "temperaturaObservada";
  
  
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
        campoOrdenamiento="id_tabla5b";
      }
      if (propiedad.equals("temperaturaObservada")){
        campoOrdenamiento="temperatura_observada";
      }
      if (propiedad.equals("apiObservado")){
        campoOrdenamiento="api_observado";
      }
      if (propiedad.equals("apiCorregido")){
        campoOrdenamiento="api_corregido";
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
    Contenido<Tabla5B> contenido = new Contenido<Tabla5B>();
    List<Tabla5B> listaRegistros = new ArrayList<Tabla5B>();
    List<Object> parametros = new ArrayList<Object>();
    List<String> filtrosWhere= new ArrayList<String>();
    String sqlWhere="";
    String sqlOrderBy="";
    try {
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }
      
      if (argumentosListar.getFiltroTemperaturaObservada()>0){
       filtrosWhere.add(" "+CAMPO_TEMPERATURA_OBSERVADA+"  = '"+ argumentosListar.getFiltroTemperaturaObservada() +"' ");
      }
      
      if (argumentosListar.getFiltroApiObservado()>0){
       filtrosWhere.add(" "+CAMPO_API_OBSERVADO+" = '"+ argumentosListar.getFiltroApiObservado() +"' ");
      }
      
      if(!filtrosWhere.isEmpty()){
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
      } else {
       if (argumentosListar.getFiltroEspecialCombinacion()!= null){
        sqlWhere = "WHERE " +argumentosListar.getFiltroEspecialCombinacion();
       }
      }
      if ((argumentosListar.getFiltroEspecialOrdenamiento()!=null)&& (!argumentosListar.getFiltroEspecialOrdenamiento().isEmpty())){
       sqlOrderBy= " ORDER BY " + argumentosListar.getFiltroEspecialOrdenamiento();
      }else {
        if ((!argumentosListar.getCampoOrdenamiento().isEmpty()) && (!argumentosListar.getSentidoOrdenamiento().isEmpty())){
        sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
        }
       }
      
      StringBuilder consultaSQL = new StringBuilder();
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_tabla5b,");
      consultaSQL.append("t1.temperatura_observada,");
      consultaSQL.append("t1.api_observado,");
      consultaSQL.append("t1.api_corregido");
      //Campos de auditoria
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(sqlWhere);
      consultaSQL.append(sqlOrderBy);
      consultaSQL.append(sqlLimit);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new Tabla5BMapper());
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
      List<Tabla5B> listaRegistros=new ArrayList<Tabla5B>();
      Contenido<Tabla5B> contenido = new Contenido<Tabla5B>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_tabla5b,");
        consultaSQL.append("t1.temperatura_observada,");
        consultaSQL.append("t1.api_observado,");
        consultaSQL.append("t1.api_corregido,");
        //Campos de auditoria
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new Tabla5BMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Tabla5B tabla5b){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (temperatura_observada,api_observado,api_corregido) ");
      consultaSQL.append(" VALUES (:TemperaturaObservada,:ApiObservado,:ApiCorregido) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("TemperaturaObservada", tabla5b.getTemperaturaObservada());
      listaParametros.addValue("ApiObservado", tabla5b.getApiObservado());
      listaParametros.addValue("ApiCorregido", tabla5b.getApiCorregido());
      
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
  
  public RespuestaCompuesta actualizarRegistro(Tabla5B tabla5b){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("temperatura_observada=:TemperaturaObservada,");
      consultaSQL.append("api_observado=:ApiObservado,");
      consultaSQL.append("api_corregido=:ApiCorregido,");
       
      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("TemperaturaObservada", tabla5b.getTemperaturaObservada());
      listaParametros.addValue("ApiObservado", tabla5b.getApiObservado());
      listaParametros.addValue("ApiCorregido", tabla5b.getApiCorregido());
      //Valores Auditoria
      listaParametros.addValue("Id", tabla5b.getId());
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