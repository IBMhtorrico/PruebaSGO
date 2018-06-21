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

import sgo.entidad.Compartimento;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class CompartimentoDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "compartimento";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_compartimento";
  public final static String NOMBRE_CAMPO_CLAVE = "id_compartimento";
  public final static String CAMPO_NUMERO_COMPARTIMENTO="identificador";
  public final static String NOMBRE_CAMPO_NUMERO_COMPARTIMENTO="identificador";
  public final static String CAMPO_ALTURA_FLECHA="altura_flecha";
  public final static String NOMBRE_CAMPO_ALTURA_FLECHA="alturaFlecha";
  public final static String CAMPO_CISTERNA="id_cisterna";
  public final static String NOMBRE_CAMPO_CISTERNA="idCisterna";
  
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
        campoOrdenamiento="id_compartimento";
      }
      if (propiedad.equals(NOMBRE_CAMPO_NUMERO_COMPARTIMENTO)){
        campoOrdenamiento=CAMPO_NUMERO_COMPARTIMENTO;
      }
      if (propiedad.equals("capacidadVolumetrica")){
        campoOrdenamiento="capacidad_volumetrica";
      }
      if (propiedad.equals(NOMBRE_CAMPO_ALTURA_FLECHA)){
        campoOrdenamiento=CAMPO_ALTURA_FLECHA;
      }
      if (propiedad.equals(NOMBRE_CAMPO_CISTERNA)){
        campoOrdenamiento=CAMPO_CISTERNA;
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
    Contenido<Compartimento> contenido = new Contenido<Compartimento>();
    List<Compartimento> listaRegistros = new ArrayList<Compartimento>();
    List<Object> parametros = new ArrayList<Object>();
    List<String> filtrosWhere = new ArrayList<String>();
    String sqlWhere = "";
    String sqlOrderBy="";
    try {
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }
      
      if (argumentosListar.getFiltroCisterna()>0) {
       filtrosWhere.add(" t1." + CAMPO_CISTERNA + "=" + argumentosListar.getFiltroCisterna());
      }
      
      if (!filtrosWhere.isEmpty()) {
        sqlWhere = " WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
      }
      
      sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
      
      StringBuilder consultaSQL = new StringBuilder();
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_compartimento,");
      consultaSQL.append("t1.identificador,");
      consultaSQL.append("t1.capacidad_volumetrica,");
      consultaSQL.append("t1.altura_flecha,");
      consultaSQL.append("t1.id_cisterna,");
      consultaSQL.append("t1.id_tracto");
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(sqlWhere);
      consultaSQL.append(sqlOrderBy);      
      consultaSQL.append(sqlLimit);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new CompartimentoMapper());
      totalRegistros = listaRegistros.size();
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
      List<Compartimento> listaRegistros=new ArrayList<Compartimento>();
      Contenido<Compartimento> contenido = new Contenido<Compartimento>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_compartimento,");
        consultaSQL.append("t1.identificador,");
        consultaSQL.append("t1.capacidad_volumetrica,");
        consultaSQL.append("t1.altura_flecha,");
        consultaSQL.append("t1.id_cisterna,");
        consultaSQL.append("t1.id_tracto");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new CompartimentoMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Compartimento compartimento){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (identificador,capacidad_volumetrica,altura_flecha,id_cisterna,id_tracto) ");
      consultaSQL.append(" VALUES (:Identificador,:CapacidadVolumetrica,:AlturaFlecha,:IdCisterna,:IdTracto) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("Identificador", compartimento.getIdentificador());
      listaParametros.addValue("CapacidadVolumetrica", compartimento.getCapacidadVolumetrica());
      listaParametros.addValue("AlturaFlecha", compartimento.getAlturaFlecha());
      listaParametros.addValue("IdCisterna", compartimento.getIdCisterna());
      listaParametros.addValue("IdTracto", compartimento.getIdTracto());
      
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
  
  public RespuestaCompuesta actualizarRegistro(Compartimento compartimento){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("identificador=:Identificador,");
      consultaSQL.append("capacidad_volumetrica=:CapacidadVolumetrica,");
      consultaSQL.append("altura_flecha=:AlturaFlecha,");
      consultaSQL.append("id_cisterna=:IdCisterna,");
      consultaSQL.append("id_tracto=:IdTracto ");
//      consultaSQL.append("actualizado_por=:ActualizadoPor,");
//      consultaSQL.append("actualizado_el=:ActualizadoEl,");
//      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:Id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("Identificador", compartimento.getIdentificador());
      listaParametros.addValue("CapacidadVolumetrica", compartimento.getCapacidadVolumetrica());
      listaParametros.addValue("AlturaFlecha", compartimento.getAlturaFlecha());
      listaParametros.addValue("IdCisterna", compartimento.getIdCisterna());
      listaParametros.addValue("IdTracto", compartimento.getIdTracto());
      //Valores Auditoria
      listaParametros.addValue("Id", compartimento.getId());
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
	     consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + CAMPO_CISTERNA + "=?";
	     cantidadFilasAfectadas = jdbcTemplate.update(consultaSQL, parametros);
	     respuesta.estado=true;
	     respuesta.valor= String.valueOf(cantidadFilasAfectadas);
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
  
  public RespuestaCompuesta recuperarRegistroPorIdCisterna(int idCisterna){
      StringBuilder consultaSQL= new StringBuilder();   
      List<Compartimento> listaRegistros=new ArrayList<Compartimento>();
      Contenido<Compartimento> contenido = new Contenido<Compartimento>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_compartimento,");
        consultaSQL.append("t1.identificador,");
        consultaSQL.append("t1.capacidad_volumetrica,");
        consultaSQL.append("t1.altura_flecha,");
        consultaSQL.append("t1.id_cisterna,");
        consultaSQL.append("t1.id_tracto");
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(" t1.id_cisterna = ?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {idCisterna},new CompartimentoMapper());
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
}