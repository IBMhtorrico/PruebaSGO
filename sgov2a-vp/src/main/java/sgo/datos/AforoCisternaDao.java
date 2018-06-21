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

import sgo.entidad.AforoCisterna;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class AforoCisternaDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "aforo_cisterna";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_aforo_cisterna";
  public final static String NOMBRE_CAMPO_CLAVE = "id_acisterna";
  public final static String CAMPO_ID_TRACTO = "id_tracto";
  public final static String CAMPO_ID_CISTERNA = "id_cisterna";
  public final static String CAMPO_ID_COMPARTIMENTO = "id_compartimento";
  public final static String CAMPO_MILIMETROS  = "milimetros";
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }
  
  public DataSource getDataSource(){
    return this.jdbcTemplate.getDataSource();
  }
  
  public String mapearCampoOrdenamiento(String propiedad){
    String campoOrdenamiento=CAMPO_MILIMETROS;
    try {
      if (propiedad.equals("id")){
        campoOrdenamiento="id_acisterna";
      }
      if (propiedad.equals("idCisterna")){
        campoOrdenamiento="id_cisterna";
      }
      if (propiedad.equals("idTracto")){
        campoOrdenamiento="id_tracto";
      }
      if (propiedad.equals("idCompartimento")){
        campoOrdenamiento="id_compartimento";
      }
      if (propiedad.equals("milimetros")){
        campoOrdenamiento="milimetros";
      }
      if (propiedad.equals("volumen")){
        campoOrdenamiento="volumen";
      }
      if (propiedad.equals("variacionMilimetros")){
        campoOrdenamiento="variacion_milimetros";
      }
      if (propiedad.equals("variacionVolumen")){
        campoOrdenamiento="variacion_volumen";
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
    Contenido<AforoCisterna> contenido = new Contenido<AforoCisterna>();
    List<AforoCisterna> listaRegistros = new ArrayList<AforoCisterna>();
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
      
     if (argumentosListar.getFiltroTracto() > 0){
       filtrosWhere.add(" "+CAMPO_ID_TRACTO+" = '"+ argumentosListar.getFiltroTracto() +"' ");
     }
     if (argumentosListar.getFiltroCisterna()>0){
       filtrosWhere.add(" "+CAMPO_ID_CISTERNA+" = '"+ argumentosListar.getFiltroCisterna() +"' ");
     }
     if(argumentosListar.getFiltroCompartimento() != Constante.FILTRO_TODOS){
       filtrosWhere.add(" "+ CAMPO_ID_COMPARTIMENTO + "=" + argumentosListar.getFiltroCompartimento());
     }
     
     if(argumentosListar.getFiltroMilimetros()>0){
      filtrosWhere.add(" "+ CAMPO_MILIMETROS + "=" + argumentosListar.getFiltroMilimetros());
    }
     
     if(!filtrosWhere.isEmpty()){
       //consultaSQL.setLength(0);
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
       //consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
       //totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     }
     
     sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();

      StringBuilder consultaSQL = new StringBuilder();
      //consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
      //totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_acisterna,");
      consultaSQL.append("t1.id_cisterna,");
      consultaSQL.append("t1.id_tracto,");
      consultaSQL.append("t1.id_compartimento,");
      consultaSQL.append("t1.milimetros,");
      consultaSQL.append("t1.volumen,");
      consultaSQL.append("t1.variacion_milimetros,");
      consultaSQL.append("t1.variacion_volumen,");
      consultaSQL.append("t1.placa_tracto,");
      consultaSQL.append("t1.placa_cisterna,");
      consultaSQL.append("t1.altura_flecha,");
      consultaSQL.append("t1.capacidad_volumetrica,");
      consultaSQL.append("t1.numero_compartimento,");
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
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new AforoCisternaMapper());
      totalRegistros = listaRegistros.size();
      totalEncontrados = totalRegistros;
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
      List<AforoCisterna> listaRegistros=new ArrayList<AforoCisterna>();
      Contenido<AforoCisterna> contenido = new Contenido<AforoCisterna>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_acisterna,");
        consultaSQL.append("t1.id_cisterna,");
        consultaSQL.append("t1.id_tracto,");
        consultaSQL.append("t1.id_compartimento,");
        consultaSQL.append("t1.milimetros,");
        consultaSQL.append("t1.volumen,");
        consultaSQL.append("t1.variacion_milimetros,");
        consultaSQL.append("t1.variacion_volumen,");
        consultaSQL.append("t1.placa_tracto,");
        consultaSQL.append("t1.placa_cisterna,");
        consultaSQL.append("t1.altura_flecha,");
        consultaSQL.append("t1.capacidad_volumetrica,");
        consultaSQL.append("t1.numero_compartimento,");
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
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new AforoCisternaMapper());
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
  
  public RespuestaCompuesta guardarRegistro(AforoCisterna aforo_cisterna){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_cisterna,id_tracto,id_compartimento,milimetros,volumen,variacion_milimetros,variacion_volumen,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
      consultaSQL.append(" VALUES (:IdCisterna,:IdTracto,:IdCompartimento,:Milimetros,:Volumen,:VariacionMilimetros,:VariacionVolumen,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdCisterna", aforo_cisterna.getIdCisterna());
      listaParametros.addValue("IdTracto", aforo_cisterna.getIdTracto());
      listaParametros.addValue("IdCompartimento", aforo_cisterna.getIdCompartimento());
      listaParametros.addValue("Milimetros", aforo_cisterna.getMilimetros());
      listaParametros.addValue("Volumen", aforo_cisterna.getVolumen());
      listaParametros.addValue("VariacionMilimetros", aforo_cisterna.getVariacionMilimetros());
      listaParametros.addValue("VariacionVolumen", aforo_cisterna.getVariacionVolumen());
      listaParametros.addValue("CreadoEl", aforo_cisterna.getCreadoEl());
      listaParametros.addValue("CreadoPor", aforo_cisterna.getCreadoPor());
      listaParametros.addValue("ActualizadoPor", aforo_cisterna.getActualizadoPor());
      listaParametros.addValue("ActualizadoEl", aforo_cisterna.getActualizadoEl());
      listaParametros.addValue("IpCreacion", aforo_cisterna.getIpCreacion());
      listaParametros.addValue("IpActualizacion", aforo_cisterna.getIpActualizacion());
      
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

  public RespuestaCompuesta actualizarRegistro(AforoCisterna aforo_cisterna){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_cisterna=:IdCisterna,");
      consultaSQL.append("id_tracto=:IdTracto,");
      consultaSQL.append("id_compartimento=:IdCompartimento,");
      consultaSQL.append("milimetros=:Milimetros,");
      consultaSQL.append("volumen=:Volumen,");
      consultaSQL.append("variacion_milimetros=:VariacionMilimetros,");
      consultaSQL.append("variacion_volumen=:VariacionVolumen,");

      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("IdCisterna", aforo_cisterna.getIdCisterna());
      listaParametros.addValue("IdTracto", aforo_cisterna.getIdTracto());
      listaParametros.addValue("IdCompartimento", aforo_cisterna.getIdCompartimento());
      listaParametros.addValue("Milimetros", aforo_cisterna.getMilimetros());
      listaParametros.addValue("Volumen", aforo_cisterna.getVolumen());
      listaParametros.addValue("VariacionMilimetros", aforo_cisterna.getVariacionMilimetros());
      listaParametros.addValue("VariacionVolumen", aforo_cisterna.getVariacionVolumen());
      //Valores Auditoria
      listaParametros.addValue("ActualizadoEl", aforo_cisterna.getActualizadoEl());
      listaParametros.addValue("ActualizadoPor", aforo_cisterna.getActualizadoPor());
      listaParametros.addValue("IpActualizacion", aforo_cisterna.getIpActualizacion());
      listaParametros.addValue("Id", aforo_cisterna.getId());
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
  
  public RespuestaCompuesta eliminarRegistros(ArrayList<String> listaIds, ArrayList<String> listaAlturas, int idTracto,int idCisterna, int idCompartimento){   
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   int cantidadFilasAfectadas=0; 
   String consultaSQL="";
   //Object[] parametros = {idRegistro};
   String cadenaIds = StringUtils.join(listaIds.toArray(),",");
   String cadenaAlturas = "";
   //StringUtils.join(listaAlturas.toArray(),",");
   try {
     if (listaAlturas!=null ){
      cadenaAlturas=StringUtils.join(listaAlturas.toArray(),",");
     }
     consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE id_tracto='" + idTracto + "'  " ;
     consultaSQL+=" AND id_cisterna = '" + idCisterna + "' AND id_compartimento='"+ idCompartimento+"' ";
     consultaSQL+=" AND id_acisterna NOT IN ("+cadenaIds+")";
     if (cadenaAlturas.length()>0){
     consultaSQL+= " AND milimetros in ("+cadenaAlturas+") ";
     }
     System.out.println(consultaSQL);
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

  public RespuestaCompuesta eliminarRegistros(ParametrosListar argumentosListar) {
	RespuestaCompuesta respuesta = new RespuestaCompuesta();
    int cantidadFilasAfectadas=0; 
    String consultaSQL="";  
    List<Object> parametros = new ArrayList<Object>();
    List<String> filtrosWhere= new ArrayList<String>();
    String sqlWhere="";
    try {     
     if (argumentosListar.getFiltroTracto() > 0){
       filtrosWhere.add(" "+ CAMPO_ID_TRACTO + " = "+ argumentosListar.getFiltroTracto());
     }
     if (argumentosListar.getFiltroCisterna()>0){
       filtrosWhere.add(" "+ CAMPO_ID_CISTERNA + " = "+ argumentosListar.getFiltroCisterna());
     }
     if(argumentosListar.getFiltroCompartimento()>0){
       filtrosWhere.add(" "+ CAMPO_ID_COMPARTIMENTO + "=" + argumentosListar.getFiltroCompartimento());
     }

     if(!filtrosWhere.isEmpty()){
       sqlWhere = " WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     }

     consultaSQL="DELETE FROM " + NOMBRE_TABLA + sqlWhere;

     cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros.toArray());
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