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

import sgo.entidad.DescargaCompartimento;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class DescargaCompartimentoDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "descarga_compartimento";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_descarga_compartimento";
  public final static String NOMBRE_CAMPO_CLAVE = "id_dcompartimento";
  public final static String NOMBRE_CAMPO_DESCARGA_CISTERNA = "idCisterna";
  public final static String CAMPO_DESCARGA_CISTERNA = "id_dcisterna";
  public final static String NOMBRE_CAMPO_NUMERO_COMPARTIMENTO = "numeroCompartimento";
  public final static String CAMPO_NUMERO_COMPARTIMENTO = "numero_compartimento";
  public final static String CAMPO_ID_CISTERNA = "id_cisterna";

  
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }
  
  public DataSource getDataSource(){
    return this.jdbcTemplate.getDataSource();
  }
  
  public String mapearCampoOrdenamiento(String propiedad){
    String campoOrdenamiento="id_dcompartimento";
    try {
      if (propiedad.equals("id")){
        campoOrdenamiento="id_dcompartimento";
      }
      if (propiedad.equals("idCisterna")){
        campoOrdenamiento="id_dcisterna";
      }
      if (propiedad.equals("idProducto")){
        campoOrdenamiento="id_producto";
      }
      if (propiedad.equals("capacidadVolumetricaCompartimento")){
        campoOrdenamiento="capacidad_volumetrica_compartimento";
      }
      if (propiedad.equals("alturaCompartimento")){
        campoOrdenamiento="altura_compartimento";
      }
      if (propiedad.equals("alturaProducto")){
        campoOrdenamiento="altura_producto";
      }
      if (propiedad.equals("unidadMedida")){
        campoOrdenamiento="unidad_medida_volumen";
      }
      if (propiedad.equals("numeroCompartimento")){
        campoOrdenamiento="numero_compartimento";
      }
      if (propiedad.equals("temperaturaObservada")){
        campoOrdenamiento="temperatura_centro_cisterna";
      }
      if (propiedad.equals("temperaturaProbeta")){
        campoOrdenamiento="temperatura_probeta";
      }
      if (propiedad.equals("apiTemperaturaObservada")){
        campoOrdenamiento="api_temperatura_observada";
      }
      if (propiedad.equals("apiTemperaturaBase")){
        campoOrdenamiento="api_temperatura_base";
      }
      if (propiedad.equals("factorCorrecion")){
        campoOrdenamiento="factor_correccion";
      }
      if (propiedad.equals("volumenTemperaturaObservada")){
        campoOrdenamiento="volumen_recibido_observado";
      }
      if (propiedad.equals("volumenTemperaturaBase")){
        campoOrdenamiento="volumen_recibido_corregido";
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
    Contenido<DescargaCompartimento> contenido = new Contenido<DescargaCompartimento>();
    List<DescargaCompartimento> listaRegistros = new ArrayList<DescargaCompartimento>();
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
      
      if (argumentosListar.getFiltroDescargaCisterna()>0){
       filtrosWhere.add(" "+CAMPO_DESCARGA_CISTERNA+" ='"+ argumentosListar.getFiltroDescargaCisterna() +"' ");
      }
     
      if (argumentosListar.getFiltroCisterna()>0){
        filtrosWhere.add(" "+CAMPO_ID_CISTERNA+" = '"+ argumentosListar.getFiltroCisterna() +"' ");
      }
      if(argumentosListar.getFiltroCompartimento()>0){
        filtrosWhere.add(" "+ CAMPO_NUMERO_COMPARTIMENTO + "=" + argumentosListar.getFiltroCompartimento());
    }
      
     if (!filtrosWhere.isEmpty()) {
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
     }
     
     sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
      
      StringBuilder consultaSQL = new StringBuilder();
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_dcompartimento,");
      consultaSQL.append("t1.id_dcisterna,");
      consultaSQL.append("t1.id_producto,");
      consultaSQL.append("t1.capacidad_volumetrica_compartimento,");
      consultaSQL.append("t1.altura_compartimento,");
      consultaSQL.append("t1.altura_producto,");
      consultaSQL.append("t1.altura_flecha,");
      consultaSQL.append("t1.unidad_medida_volumen,");
      consultaSQL.append("t1.numero_compartimento,");
      consultaSQL.append("t1.id_compartimento,");
      consultaSQL.append("t1.temperatura_centro_cisterna,");
      consultaSQL.append("t1.temperatura_probeta,");
      consultaSQL.append("t1.api_temperatura_observada,");
      consultaSQL.append("t1.api_temperatura_base,");
      consultaSQL.append("t1.factor_correccion,");
      consultaSQL.append("t1.volumen_recibido_observado,");
      consultaSQL.append("t1.volumen_recibido_corregido,");
      consultaSQL.append("t1.nombre_producto,");
      consultaSQL.append("t1.tipo_volumen,");
      consultaSQL.append("t1.merma_porcentaje,");
      consultaSQL.append("t1.abreviatura_producto");
      consultaSQL.append(" FROM ");
      consultaSQL.append(NOMBRE_VISTA);
      consultaSQL.append(" t1 ");     
      consultaSQL.append(sqlWhere);      
      consultaSQL.append(sqlOrderBy);
      consultaSQL.append(sqlLimit);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DescargaCompartimentoMapper());
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
      List<DescargaCompartimento> listaRegistros=new ArrayList<DescargaCompartimento>();
      Contenido<DescargaCompartimento> contenido = new Contenido<DescargaCompartimento>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_dcompartimento,");
        consultaSQL.append("t1.id_dcisterna,");
        consultaSQL.append("t1.id_producto,");
        consultaSQL.append("t1.capacidad_volumetrica_compartimento,");
        consultaSQL.append("t1.altura_compartimento,");
        consultaSQL.append("t1.altura_flecha,");
        consultaSQL.append("t1.altura_producto,");
        consultaSQL.append("t1.unidad_medida_volumen,");
        consultaSQL.append("t1.id_compartimento,");
        consultaSQL.append("t1.numero_compartimento,");
        consultaSQL.append("t1.temperatura_centro_cisterna,");
        consultaSQL.append("t1.temperatura_probeta,");
        consultaSQL.append("t1.api_temperatura_observada,");
        consultaSQL.append("t1.api_temperatura_base,");
        consultaSQL.append("t1.factor_correccion,");
        consultaSQL.append("t1.tipo_volumen,");
        consultaSQL.append("t1.merma_porcentaje,");
        consultaSQL.append("t1.volumen_recibido_observado,");
        consultaSQL.append("t1.volumen_recibido_corregido");
        //Campos de auditoria
        consultaSQL.append(" FROM ");       
        consultaSQL.append(NOMBRE_VISTA);
        consultaSQL.append(" t1 ");
        consultaSQL.append(" WHERE ");
        consultaSQL.append(NOMBRE_CAMPO_CLAVE);
        consultaSQL.append("=?");
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DescargaCompartimentoMapper());
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
  
  public RespuestaCompuesta guardarRegistro(DescargaCompartimento descargaCompartimento){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_dcisterna,id_producto,capacidad_volumetrica_compartimento,altura_compartimento,altura_flecha,altura_producto,unidad_medida_volumen,numero_compartimento,id_compartimento,temperatura_centro_cisterna,temperatura_probeta,api_temperatura_observada,api_temperatura_base,factor_correccion,volumen_recibido_observado,volumen_recibido_corregido,tipo_volumen,merma_porcentaje) ");
      consultaSQL.append(" VALUES (:IdCisterna,:IdProducto,:CapacidadVolumetricaCompartimento,:AlturaCompartimento,:AlturaFlecha,:AlturaProducto,:UnidadMedida,:NumeroCompartimento,:IdCompartimento,:TemperaturaObservada,:TemperaturaProbeta,:ApiTemperaturaObservada,:ApiTemperaturaBase,:FactorCorrecion,:VolumenTemperaturaObservada,:VolumenTemperaturaBase,:TipoVolumen,:MermaPorcentaje) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdCisterna", descargaCompartimento.getIdDescargaCisterna());
      listaParametros.addValue("IdProducto", descargaCompartimento.getIdProducto());
      listaParametros.addValue("CapacidadVolumetricaCompartimento", descargaCompartimento.getCapacidadVolumetricaCompartimento());
      listaParametros.addValue("AlturaCompartimento", descargaCompartimento.getAlturaCompartimento());
      listaParametros.addValue("AlturaFlecha", descargaCompartimento.getAlturaFlecha());
      listaParametros.addValue("AlturaProducto", descargaCompartimento.getAlturaProducto());
      listaParametros.addValue("UnidadMedida", descargaCompartimento.getUnidadMedida());
      listaParametros.addValue("NumeroCompartimento", descargaCompartimento.getNumeroCompartimento());
      listaParametros.addValue("IdCompartimento", descargaCompartimento.getIdCompartimento());
      listaParametros.addValue("TemperaturaObservada", descargaCompartimento.getTemperaturaObservada());
      listaParametros.addValue("TemperaturaProbeta", descargaCompartimento.getTemperaturaProbeta());
      listaParametros.addValue("ApiTemperaturaObservada", descargaCompartimento.getApiTemperaturaObservada());
      listaParametros.addValue("ApiTemperaturaBase", descargaCompartimento.getApiTemperaturaBase());
      listaParametros.addValue("FactorCorrecion", descargaCompartimento.getFactorCorreccion());
      listaParametros.addValue("VolumenTemperaturaObservada", descargaCompartimento.getVolumenRecibidoObservado());
      listaParametros.addValue("VolumenTemperaturaBase", descargaCompartimento.getVolumenRecibidoCorregido());
      listaParametros.addValue("TipoVolumen", descargaCompartimento.getTipoVolumen());
      listaParametros.addValue("MermaPorcentaje", descargaCompartimento.getMermaPorcentaje());
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
  
  public RespuestaCompuesta actualizarRegistro(DescargaCompartimento descarga_compartimento){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("UPDATE ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" SET ");
      consultaSQL.append("id_dcisterna=:IdCisterna,");
      consultaSQL.append("id_producto=:IdProducto,");
      consultaSQL.append("capacidad_volumetrica_compartimento=:CapacidadVolumetricaCompartimento,");
      consultaSQL.append("altura_compartimento=:AlturaCompartimento,");
      consultaSQL.append("altura_producto=:AlturaProducto,");
      consultaSQL.append("unidad_medida_volumen=:UnidadMedida,");
      consultaSQL.append("numero_compartimento=:NumeroCompartimento,");
      consultaSQL.append("id_compartimento=:IdCompartimento,");
      consultaSQL.append("temperatura_centro_cisterna=:TemperaturaObservada,");
      consultaSQL.append("temperatura_probeta=:TemperaturaProbeta,");
      consultaSQL.append("api_temperatura_observada=:ApiTemperaturaObservada,");
      consultaSQL.append("api_temperatura_base=:ApiTemperaturaBase,");
      consultaSQL.append("factor_correccion=:FactorCorrecion,");
      consultaSQL.append("tipo_volumen=:TipoVolumen,");
      consultaSQL.append("merma_porcentaje=:MermaPorcentaje,");
      consultaSQL.append("volumen_recibido_observado=:VolumenTemperaturaObservada,");
      consultaSQL.append("volumen_recibido_corregido=:VolumenTemperaturaBase,");       
      consultaSQL.append("actualizado_por=:ActualizadoPor,");
      consultaSQL.append("actualizado_el=:ActualizadoEl,");
      consultaSQL.append("ip_actualizacion=:IpActualizacion");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE);
      consultaSQL.append("=:id");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();
      listaParametros.addValue("IdCisterna", descarga_compartimento.getIdDescargaCisterna());
      listaParametros.addValue("IdProducto", descarga_compartimento.getIdProducto());
      listaParametros.addValue("CapacidadVolumetricaCompartimento", descarga_compartimento.getCapacidadVolumetricaCompartimento());
      listaParametros.addValue("AlturaCompartimento", descarga_compartimento.getAlturaCompartimento());
      listaParametros.addValue("AlturaProducto", descarga_compartimento.getAlturaProducto());
      listaParametros.addValue("UnidadMedida", descarga_compartimento.getUnidadMedida());
      listaParametros.addValue("NumeroCompartimento", descarga_compartimento.getNumeroCompartimento());
      listaParametros.addValue("IdCompartimento", descarga_compartimento.getIdCompartimento());
      listaParametros.addValue("TemperaturaObservada", descarga_compartimento.getTemperaturaObservada());
      listaParametros.addValue("TemperaturaProbeta", descarga_compartimento.getTemperaturaProbeta());
      listaParametros.addValue("ApiTemperaturaObservada", descarga_compartimento.getApiTemperaturaObservada());
      listaParametros.addValue("ApiTemperaturaBase", descarga_compartimento.getApiTemperaturaBase());
      listaParametros.addValue("FactorCorrecion", descarga_compartimento.getFactorCorreccion());
      listaParametros.addValue("VolumenTemperaturaObservada", descarga_compartimento.getVolumenRecibidoObservado());
      listaParametros.addValue("VolumenTemperaturaBase", descarga_compartimento.getVolumenRecibidoCorregido());
      listaParametros.addValue("TipoVolumen", descarga_compartimento.getTipoVolumen());
      listaParametros.addValue("MermaPorcentaje", descarga_compartimento.getMermaPorcentaje());
      //Valores Auditoria
      listaParametros.addValue("Id", descarga_compartimento.getId());
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
   String consultaSQL="";
   int cantidadFilasAfectadas=0;
   Object[] parametros = {idRegistro};
   try {
     consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE " + CAMPO_DESCARGA_CISTERNA + "=?";
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