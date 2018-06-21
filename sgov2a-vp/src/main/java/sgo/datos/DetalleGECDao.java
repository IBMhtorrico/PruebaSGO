package sgo.datos;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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

import sgo.entidad.DetalleGEC;
import sgo.entidad.Contenido;
import sgo.entidad.DetalleGecVista;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class DetalleGECDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "detalle_gec";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_detalle_gec";
  public final static String NOMBRE_CAMPO_CLAVE = "id_dgec";
  /** Nombre de la clase. */
  private static final String sNombreClase = "DetalleGECDao";
  
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
        campoOrdenamiento="id_dgec";
      }
      if (propiedad.equals("idGuiaCombustible")){
        campoOrdenamiento="id_gcombustible";
      }
      if (propiedad.equals("numeroGuia")){
        campoOrdenamiento="numero_guia";
      }
      if (propiedad.equals("fechaEmision")){
        campoOrdenamiento="fecha_emision";
      }
      if (propiedad.equals("fechaRecepcion")){
        campoOrdenamiento="fecha_recepcion";
      }
      if (propiedad.equals("volumenDespachado")){
        campoOrdenamiento="volumen_despachado";
      }
      if (propiedad.equals("volumenRecibido")){
        campoOrdenamiento="volumen_recibido";
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
    int totalRegistros = 0, totalEncontrados = 0;
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    Contenido<DetalleGEC> contenido = new Contenido<DetalleGEC>();
    List<DetalleGEC> listaRegistros = new ArrayList<DetalleGEC>();
    List<Object> parametros = new ArrayList<Object>();
    StringBuilder consultaSQL = new StringBuilder();
    try {
      String sqlwhere = " WHERE id_gcombustible= '"+argumentosListar.getIdGuiaCombustible()+"' ";
      String sqlorder = " ORDER BY fecha_emision DESC ";
      consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
      totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_dgec,");
      consultaSQL.append("t1.id_gcombustible,");
      consultaSQL.append("t1.numero_guia,");
      consultaSQL.append("t1.fecha_emision,");
      consultaSQL.append("t1.fecha_recepcion,");
      consultaSQL.append("t1.volumen_despachado,");
      consultaSQL.append("t1.volumen_recibido,");
      consultaSQL.append("t1.estado");
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(sqlwhere);
      consultaSQL.append(sqlorder);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DetalleGECMapper());
      totalEncontrados =totalRegistros;
      contenido.carga = listaRegistros;
      respuesta.estado = true;
      respuesta.contenido = contenido;
      respuesta.contenido.totalRegistros = totalRegistros;
      respuesta.contenido.totalEncontrados = totalEncontrados;
      Utilidades.gestionaTrace(sNombreClase, "recuperarRegistros");
    } catch (DataAccessException excepcionAccesoDatos) {
      Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistros", consultaSQL.toString());
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
  
  public RespuestaCompuesta recuperarRegistrosVista(ParametrosListar argumentosListar) {
   String sqlLimit = "";
   int totalRegistros = 0, totalEncontrados = 0;
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   Contenido<DetalleGecVista> contenido = new Contenido<DetalleGecVista>();
   List<DetalleGecVista> listaRegistros = new ArrayList<DetalleGecVista>();
   List<Object> parametros = new ArrayList<Object>();
   String sqlWhere ="";
   StringBuilder consultaSQL = new StringBuilder();
   try {
    
    sqlWhere = "WHERE t1.id_transportista='"+argumentosListar.getIdTransportista()+"'";
    sqlWhere+=" AND t1.id_producto='"+argumentosListar.getFiltroProducto()+"'";
    sqlWhere+=" AND t1.fecha_operativa='"+Utilidades.modificarFormatoFechaddmmaaaa(argumentosListar.getFiltroFechaDiaOperativo())+"'";
    if (argumentosListar.getFiltroParametro() != null && argumentosListar.getFiltroParametro().equals("agregar") ) {
    	sqlWhere+=" AND t1.numero_guia_remision not in(select numero_guia from sgo.v_detalle_gec)";
    }
     consultaSQL.append("SELECT count(t1.id_dcisterna) as total FROM  sgo.v_guia_combustible_detalle_crear t1 "+ sqlWhere);
     totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.id_transporte,");
     consultaSQL.append("t1.id_dcisterna,");
     consultaSQL.append("t1.numero_guia_remision,");
     consultaSQL.append("t1.fecha_arribo,");
     consultaSQL.append("t1.fecha_fiscalizacion,");
     consultaSQL.append("t1.volumen_despachado_observado,");
     consultaSQL.append("t1.volumen_despachado_corregido,");
     consultaSQL.append("t1.estado_dia_operativo,");
     consultaSQL.append("t1.fecha_operativa,");
     consultaSQL.append("t1.fecha_emision,");
     consultaSQL.append("t1.volumen_recibido_observado,");
     consultaSQL.append("t1.volumen_recibido_corregido,");   
     consultaSQL.append("t1.id_producto,");
     consultaSQL.append("t1.id_transportista");
     consultaSQL.append(" FROM  sgo.v_guia_combustible_detalle_crear t1 ");
     consultaSQL.append(sqlWhere);
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DetalleGecVistaMapper());
     totalEncontrados =totalRegistros;
     contenido.carga = listaRegistros;
     respuesta.estado = true;
     respuesta.contenido = contenido;
     respuesta.contenido.totalRegistros = totalRegistros;
     respuesta.contenido.totalEncontrados = totalEncontrados;
     Utilidades.gestionaTrace(sNombreClase, "recuperarRegistrosVista");
   } catch (DataAccessException excepcionAccesoDatos) {
	 Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistrosVista", consultaSQL.toString());
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
      List<DetalleGEC> listaRegistros=new ArrayList<DetalleGEC>();
      Contenido<DetalleGEC> contenido = new Contenido<DetalleGEC>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_dgec,");
        consultaSQL.append("t1.id_gcombustible,");
        consultaSQL.append("t1.numero_guia,");
        consultaSQL.append("t1.fecha_emision,");
        consultaSQL.append("t1.fecha_recepcion,");
        consultaSQL.append("t1.volumen_despachado,");
        consultaSQL.append("t1.volumen_recibido,");
        consultaSQL.append("t1.estado");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DetalleGECMapper());
        contenido.totalRegistros=listaRegistros.size();
        contenido.totalEncontrados=listaRegistros.size();
        contenido.carga= listaRegistros;
        respuesta.mensaje="OK";
        respuesta.estado=true;
        respuesta.contenido = contenido;      
        Utilidades.gestionaTrace(sNombreClase, "recuperarRegistro");
      } catch (DataAccessException excepcionAccesoDatos) {
    	Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "recuperarRegistro", consultaSQL.toString());
        respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
        respuesta.estado=false;
        respuesta.contenido=null;
      }
      return respuesta;
    }
  
  public RespuestaCompuesta guardarRegistro(DetalleGEC detalle_gec){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_gcombustible,numero_guia,fecha_emision,fecha_recepcion,volumen_despachado,volumen_recibido,estado) ");
      consultaSQL.append(" VALUES (:IdGuiaCombustible,:NumeroGuia,:FechaEmision,:FechaRecepcion,:VolumenDespachado,:VolumenRecibido,:Estado) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdGuiaCombustible", detalle_gec.getIdGuiaCombustible());
      listaParametros.addValue("NumeroGuia", detalle_gec.getNumeroGuia());
      listaParametros.addValue("FechaEmision", detalle_gec.getFechaEmision());
      listaParametros.addValue("FechaRecepcion", detalle_gec.getFechaRecepcion());
      listaParametros.addValue("VolumenDespachado", detalle_gec.getVolumenDespachado());
      listaParametros.addValue("VolumenRecibido", detalle_gec.getVolumenRecibido());
      listaParametros.addValue("Estado", detalle_gec.getEstado());
      
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
      Utilidades.gestionaTrace(sNombreClase, "guardarRegistro");
    } catch (DataIntegrityViolationException excepcionIntegridadDatos){
      Utilidades.gestionaWarning(excepcionIntegridadDatos, sNombreClase, "guardarRegistro", consultaSQL.toString());
      respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
      respuesta.estado=false;
    } catch (DataAccessException excepcionAccesoDatos){
      Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "guardarRegistro", consultaSQL.toString());
      respuesta.error=Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado=false;
    }
    return respuesta;
  }
  
  public RespuestaCompuesta actualizarRegistro(DetalleGEC detalle_gec){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_gcombustible=:IdGuiaCombustible,");
      consultaSQL.append("numero_guia=:NumeroGuia,");
      consultaSQL.append("fecha_emision=:FechaEmision,");
      consultaSQL.append("fecha_recepcion=:FechaRecepcion,");
      consultaSQL.append("volumen_despachado=:VolumenDespachado,");
      consultaSQL.append("volumen_recibido=:VolumenRecibido,");
      consultaSQL.append("estado=:Estado");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("IdGuiaCombustible", detalle_gec.getIdGuiaCombustible());
      listaParametros.addValue("NumeroGuia", detalle_gec.getNumeroGuia());
      listaParametros.addValue("FechaEmision", detalle_gec.getFechaEmision());
      listaParametros.addValue("FechaRecepcion", detalle_gec.getFechaRecepcion());
      listaParametros.addValue("VolumenDespachado", detalle_gec.getVolumenDespachado());
      listaParametros.addValue("VolumenRecibido", detalle_gec.getVolumenRecibido());
      listaParametros.addValue("Estado", detalle_gec.getEstado());
      //Valores Auditoria
      listaParametros.addValue("Id", detalle_gec.getId());
      SqlParameterSource namedParameters= listaParametros;
      /*Ejecuta la consulta y retorna las filas afectadas*/
      cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters);   
      if (cantidadFilasAfectadas>1){
        respuesta.error= Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA;
        respuesta.estado=false;
        return respuesta;
      }
      respuesta.estado=true;
      Utilidades.gestionaTrace(sNombreClase, "actualizarRegistro");
    } catch (DataIntegrityViolationException excepcionIntegridadDatos){
      Utilidades.gestionaWarning(excepcionIntegridadDatos, sNombreClase, "actualizarRegistro", consultaSQL.toString());
      respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
      respuesta.estado=false;
    } catch (DataAccessException excepcionAccesoDatos){
    	Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "actualizarRegistro", consultaSQL.toString());
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
      Utilidades.gestionaTrace(sNombreClase, "eliminarRegistros");
    } catch (DataIntegrityViolationException excepcionIntegridadDatos){ 
      excepcionIntegridadDatos.printStackTrace();
      respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
      respuesta.estado=false;
    } catch (DataAccessException excepcionAccesoDatos){
    	Utilidades.gestionaWarning(excepcionAccesoDatos, sNombreClase, "eliminarRegistros", consultaSQL.toString());
      respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
      respuesta.estado=false;
    }
    return respuesta;
  }
}