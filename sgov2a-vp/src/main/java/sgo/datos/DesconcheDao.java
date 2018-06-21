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

import sgo.entidad.ComboDesconche;
import sgo.entidad.Desconche;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.VistaDesconche;
import sgo.utilidades.Constante;

@Repository
public class DesconcheDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "desconche";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_desconche";
  public final static String NOMBRE_CAMPO_CLAVE = "id_desconche";
  
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
        campoOrdenamiento="id_desconche";
      }
      if (propiedad.equals("idCompartimento")){
        campoOrdenamiento="id_compartimento";
      }
      if (propiedad.equals("idTanque")){
        campoOrdenamiento="id_tanque";
      }
      if (propiedad.equals("numeroDesconche")){
        campoOrdenamiento="numero_desconche";
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
    List<String> filtrosWhere= new ArrayList<String>();
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    Contenido<VistaDesconche> contenido = new Contenido<VistaDesconche>();
    List<VistaDesconche> listaRegistros = new ArrayList<VistaDesconche>();
    List<Object> parametros = new ArrayList<Object>();
    StringBuilder consultaSQL=null;
    String sqlWhere="";
    try {
     consultaSQL = new StringBuilder();
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }

     if (argumentosListar.getFiltroOperacion() > 0){
       filtrosWhere.add(" ( t1.id_operacion = '"+ argumentosListar.getFiltroOperacion() +"' ) ");
     } 
     
     String fechaInicio = argumentosListar.getFiltroFechaInicio();
     String fechaFinal = argumentosListar.getFiltroFechaFinal();
     if (!fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
      filtrosWhere.add(" t1.fecha_operativa" +  Constante.SQL_ENTRE + ("'" + fechaInicio + "'" + Constante.SQL_Y + "'" + fechaFinal + "'"));
     } else {
      if (!fechaInicio.isEmpty()) {
       filtrosWhere.add(" t1.fecha_operativa" +  " >= '" + fechaInicio + "'");
      }
      if (!fechaFinal.isEmpty()) {
       filtrosWhere.add(" t1.fecha_operativa" +  " <= '" + fechaFinal + "'");
      }
     }
     
     sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM sgo.v_desconche_vista t1 " + sqlWhere);
     totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     totalEncontrados=totalRegistros;     
     
     if(!argumentosListar.getFiltroPlacaCisterna().isEmpty()){
       sqlWhere =sqlWhere+ " AND  t1.placa = '"+argumentosListar.getFiltroPlacaCisterna()+"'";
       consultaSQL.setLength(0);
       consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM sgo.v_desconche_vista t1 " + sqlWhere);
       
       totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     }

      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_desconche,");
      consultaSQL.append("t1.fecha_operativa,");
      consultaSQL.append("t1.placa,");
      consultaSQL.append("t1.placa_tracto,");
      consultaSQL.append("t1.numero_desconche,");
      consultaSQL.append("t1.numero_compartimento,");
      consultaSQL.append("t1.volumen,");     
      consultaSQL.append("t1.numero_compartimentos,");
      consultaSQL.append("t1.numero_maximo_desconche,");
      consultaSQL.append("t1.id_dcisterna,");     
      consultaSQL.append("t1.nombre_estacion,");
      consultaSQL.append("t1.descripcion_tanque,");
      consultaSQL.append("t1.id_operacion,");
      consultaSQL.append("t1.desconche_creado_el,");
      consultaSQL.append("t1.desconche_creado_por,");
      consultaSQL.append("t1.desconche_actualizado_por,");
      consultaSQL.append("t1.desconche_actualizado_el,");
      consultaSQL.append("t1.desconche_usuario_creacion,");
      consultaSQL.append("t1.desconche_usuario_actualizacion,");
      consultaSQL.append("t1.desconche_ip_creacion,");
      consultaSQL.append("t1.desconche_ip_actualizacion");
      consultaSQL.append(" FROM sgo.v_desconche_vista t1 ");
      consultaSQL.append(sqlWhere);
      consultaSQL.append(sqlLimit);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new VistaDesconcheMapper());
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
  
  public RespuestaCompuesta recuperarRegistroVista(int ID) {
   int totalRegistros = 0, totalEncontrados = 0;
   List<String> filtrosWhere= new ArrayList<String>();
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   Contenido<VistaDesconche> contenido = new Contenido<VistaDesconche>();
   List<VistaDesconche> listaRegistros = new ArrayList<VistaDesconche>();
   List<Object> parametros = new ArrayList<Object>();
   StringBuilder consultaSQL=null;
   String sqlWhere="";
   try {
    consultaSQL = new StringBuilder();
   
    sqlWhere = " WHERE " +" ( t1.id_desconche = '"+ ID +"' ) ";
    consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.id_desconche,");
     consultaSQL.append("t1.fecha_operativa,");
     consultaSQL.append("t1.placa,");
     consultaSQL.append("t1.placa_tracto,");
     consultaSQL.append("t1.numero_desconche,");
     consultaSQL.append("t1.numero_compartimento,");
     consultaSQL.append("t1.volumen,");     
     consultaSQL.append("t1.numero_compartimentos,");
     consultaSQL.append("t1.numero_maximo_desconche,");
     consultaSQL.append("t1.id_dcisterna,");     
     consultaSQL.append("t1.nombre_estacion,");
     consultaSQL.append("t1.descripcion_tanque,");
     consultaSQL.append("t1.id_operacion,");
     consultaSQL.append("t1.desconche_creado_el,");
     consultaSQL.append("t1.desconche_creado_por,");
     consultaSQL.append("t1.desconche_actualizado_por,");
     consultaSQL.append("t1.desconche_actualizado_el,");
     consultaSQL.append("t1.desconche_usuario_creacion,");
     consultaSQL.append("t1.desconche_usuario_actualizacion,");
     consultaSQL.append("t1.desconche_ip_creacion,");
     consultaSQL.append("t1.desconche_ip_actualizacion");
     consultaSQL.append(" FROM sgo.v_desconche_vista t1 ");
     consultaSQL.append(sqlWhere);
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new VistaDesconcheMapper());
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
  
  public RespuestaCompuesta listarComboDesconche(ParametrosListar argumentosListar) {
   int totalRegistros = 0, totalEncontrados = 0;
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   Contenido<ComboDesconche> contenido = new Contenido<ComboDesconche>();
   List<ComboDesconche> listaRegistros = new ArrayList<ComboDesconche>();
   List<Object> parametros = new ArrayList<Object>();
   String sqlWhere ="";
   try {   
     sqlWhere=" WHERE id_operacion='"+argumentosListar.getFiltroOperacion()+"'";
     StringBuilder consultaSQL = new StringBuilder();
     consultaSQL.append("SELECT ");
     consultaSQL.append("to_char(fecha_operativa, 'dd/mm/YYYY') as dia_operativa,");
     consultaSQL.append("placa_tracto,");
     consultaSQL.append("placa,");
     consultaSQL.append("nombre_estacion,");
     consultaSQL.append("descripcion_tanque as nombre_tanque,");
     consultaSQL.append("id_dcisterna,");
     consultaSQL.append("numero_compartimentos,");  
     consultaSQL.append("numero_maximo_desconche"); 
     consultaSQL.append(" FROM ");
     consultaSQL.append(" sgo.v_transporte_descarga_cisterna "+sqlWhere);
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new ComboDesconcheMapper());
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
      List<Desconche> listaRegistros=new ArrayList<Desconche>();
      Contenido<Desconche> contenido = new Contenido<Desconche>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_desconche,");
        consultaSQL.append("t1.id_compartimento,");
        consultaSQL.append("t1.id_tanque,");
        consultaSQL.append("t1.numero_desconche,");
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
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DesconcheMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Desconche desconche){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
     consultaSQL.append("INSERT INTO ");
     consultaSQL.append(NOMBRE_TABLA);
     consultaSQL.append(" (id_dcisterna,numero_desconche,volumen,numero_compartimento,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
     consultaSQL.append(" VALUES (:IdDescargaCisterna,:NumeroDesconche,:Volumen,:NumeroCompartimento,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
     MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
     listaParametros.addValue("IdDescargaCisterna", desconche.getIdDescargaCisterna());
     listaParametros.addValue("NumeroDesconche", desconche.getNumeroDesconche());
     listaParametros.addValue("Volumen", desconche.getVolumen());
     listaParametros.addValue("NumeroCompartimento", desconche.getNumeroCompartimento());
     listaParametros.addValue("CreadoEl", desconche.getCreadoEl());
     listaParametros.addValue("CreadoPor", desconche.getCreadoPor());
     listaParametros.addValue("ActualizadoPor", desconche.getActualizadoPor());
     listaParametros.addValue("ActualizadoEl", desconche.getActualizadoEl());
     listaParametros.addValue("IpCreacion", desconche.getIpCreacion());
     listaParametros.addValue("IpActualizacion", desconche.getIpActualizacion());
      
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
  
  public RespuestaCompuesta actualizarRegistro(Desconche desconche){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("numero_desconche=:NumeroDesconche,");
      consultaSQL.append("volumen=:Volumen,");      
      consultaSQL.append("numero_compartimento=:NumeroCompartimento,");
      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("NumeroDesconche", desconche.getNumeroDesconche());
      listaParametros.addValue("Volumen", desconche.getVolumen());
      listaParametros.addValue("NumeroCompartimento", desconche.getNumeroCompartimento());
      listaParametros.addValue("ActualizadoEl", desconche.getActualizadoEl());
      listaParametros.addValue("ActualizadoPor", desconche.getActualizadoPor());
      listaParametros.addValue("IpActualizacion", desconche.getIpActualizacion());
      listaParametros.addValue("Id", desconche.getId());
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