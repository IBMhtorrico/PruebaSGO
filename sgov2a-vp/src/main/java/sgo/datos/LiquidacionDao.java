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

import sgo.entidad.Liquidacion;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class LiquidacionDao {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedJdbcTemplate;
  public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "liquidacion";
  public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_liquidacion";
  public final static String NOMBRE_CAMPO_CLAVE = "id_liquidacion";
  
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
        campoOrdenamiento="id_liquidacion";
      }
      if (propiedad.equals("idOperacion")){
        campoOrdenamiento="id_operacion";
      }
      if (propiedad.equals("fechaOperativa")){
        campoOrdenamiento="fecha_operativa";
      }
      if (propiedad.equals("idProducto")){
        campoOrdenamiento="id_producto";
      }
      if (propiedad.equals("porcentajeActual")){
        campoOrdenamiento="porcentaje_actual";
      }
      if (propiedad.equals("stockFinal")){
        campoOrdenamiento="stock_final";
      }
      if (propiedad.equals("stockInicial")){
        campoOrdenamiento="stock_inicial";
      }
      if (propiedad.equals("volumenDescargado")){
        campoOrdenamiento="volumen_descargado";
      }
      if (propiedad.equals("volumenDespacho")){
        campoOrdenamiento="volumen_despacho";
      }
      if (propiedad.equals("tolerancia")){
        campoOrdenamiento="tolerancia";
      }
      if (propiedad.equals("stockFinalCalculado")){
        campoOrdenamiento="stock_final_calculado";
      }
      if (propiedad.equals("variacion")){
        campoOrdenamiento="variacion";
      }
      if (propiedad.equals("variacionAbsoluta")){
        campoOrdenamiento="variacion_absoluta";
      }
      if (propiedad.equals("nombreProducto")){
        campoOrdenamiento="nombre_producto";
      }
      if (propiedad.equals("nombreOperacion")){
        campoOrdenamiento="nombre_operacion";
      }
      if (propiedad.equals("nombreCliente")){
        campoOrdenamiento="nombre_cliente";
      }
      if (propiedad.equals("faltante")){
        campoOrdenamiento="faltante";
      }
      if (propiedad.equals("idEstacion")){
        campoOrdenamiento="id_estacion";
      }
      if (propiedad.equals("idTanque")){
        campoOrdenamiento="id_tanque";
      }
      if (propiedad.equals("nombreEstacion")){
        campoOrdenamiento="nombre_estacion";
      }
      if (propiedad.equals("nombreTanque")){
        campoOrdenamiento="nombre_tanque";
      }
      //Campos de auditoria
    }catch(Exception excepcion){
      
    }
    return campoOrdenamiento;
  }

  public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
    String sqlLimit = "";
    List<String> filtrosWhere= new ArrayList<String>();
    String sqlWhere="";
    int totalRegistros = 0, totalEncontrados = 0;
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    Contenido<Liquidacion> contenido = new Contenido<Liquidacion>();
    List<Liquidacion> listaRegistros = new ArrayList<Liquidacion>();
    List<Object> parametros = new ArrayList<Object>();
    try {
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
        sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
        parametros.add(argumentosListar.getInicioPaginacion());
        parametros.add(argumentosListar.getRegistrosxPagina());
      }
      StringBuilder consultaSQL = new StringBuilder();
      //consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM sgo.v_liquidacion_inventario_a_resumen_completo_total t1" );
      //totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
      
      if (argumentosListar.getIdOperacion() > 0){
       filtrosWhere.add(" (t1.id_operacion ='"+ argumentosListar.getIdOperacion() +"') ");
     }
     if (!argumentosListar.getFiltroFechaDiaOperativo().isEmpty()){
       filtrosWhere.add(" ( t1.fecha_operativa='" + argumentosListar.getFiltroFechaDiaOperativo()+"') ");
     }
     
     if(!filtrosWhere.isEmpty()){
       consultaSQL.setLength(0);
       sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
       //consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
       //totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     }
      
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_operacion,");
      consultaSQL.append("t1.fecha_operativa,");
      consultaSQL.append("t1.id_producto,");
      consultaSQL.append("t1.porcentaje_actual,");
      consultaSQL.append("t1.stock_final,");
      consultaSQL.append("t1.stock_inicial,");
      consultaSQL.append("t1.volumen_descargado,");
      consultaSQL.append("t1.volumen_despacho,");
      consultaSQL.append("t1.tolerancia,");
      consultaSQL.append("t1.stock_final_calculado,");
      consultaSQL.append("t1.variacion,");
      consultaSQL.append("t1.variacion_absoluta,");
      consultaSQL.append("t1.nombre_producto,");
      consultaSQL.append("t1.nombre_operacion,");
      consultaSQL.append("t1.nombre_cliente,");
      consultaSQL.append("t1.faltante,");
      consultaSQL.append("t1.id_estacion,");
      consultaSQL.append("t1.id_tanque,");
      consultaSQL.append("t1.nombre_estacion,");
      consultaSQL.append("t1.nombre_tanque");
      consultaSQL.append(" FROM ");
      consultaSQL.append("sgo.v_liquidacion_inventario_a_resumen_completo_total t1 ");
      consultaSQL.append(sqlWhere);
      listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new LiquidacionMapper());
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
  
  
  public RespuestaCompuesta recuperarRegistrosxEstacion(ParametrosListar argumentosListar) {
   String sqlLimit = "";
   List<String> filtrosWhere= new ArrayList<String>();
   String sqlWhere="";
   int totalRegistros = 0, totalEncontrados = 0;
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   Contenido<Liquidacion> contenido = new Contenido<Liquidacion>();
   List<Liquidacion> listaRegistros = new ArrayList<Liquidacion>();
   List<Object> parametros = new ArrayList<Object>();
   try {
     if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
       sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
       parametros.add(argumentosListar.getInicioPaginacion());
       parametros.add(argumentosListar.getRegistrosxPagina());
     }
     StringBuilder consultaSQL = new StringBuilder();
     //consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM sgo.v_liquidacion_inventario_a_resumen_completo_total t1" );
     //totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
     
     if (argumentosListar.getIdOperacion() > 0){
      filtrosWhere.add(" (t1.id_operacion ='"+ argumentosListar.getIdOperacion() +"') ");
    }
     
     if (!argumentosListar.getFiltroFechaDiaOperativo().isEmpty()){
      filtrosWhere.add(" ( t1.fecha_operativa='" + argumentosListar.getFiltroFechaDiaOperativo()+"') ");
    }
     
     if (argumentosListar.getFiltroProducto() > 0){
      filtrosWhere.add(" (t1.id_producto ='"+ argumentosListar.getFiltroProducto() +"') ");
    }     
    
    if(!filtrosWhere.isEmpty()){
      consultaSQL.setLength(0);
      sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
      //consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
      //totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
    }
     
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.id_operacion,");
     consultaSQL.append("t1.fecha_operativa,");
     consultaSQL.append("t1.id_producto,");
     consultaSQL.append("t1.porcentaje_actual,");
     consultaSQL.append("t1.stock_final,");
     consultaSQL.append("t1.stock_inicial,");
     consultaSQL.append("t1.volumen_descargado,");
     consultaSQL.append("t1.volumen_despacho,");
     consultaSQL.append("t1.tolerancia,");
     consultaSQL.append("t1.stock_final_calculado,");
     consultaSQL.append("t1.variacion,");
     consultaSQL.append("t1.variacion_absoluta,");
     consultaSQL.append("t1.nombre_producto,");
     consultaSQL.append("t1.nombre_operacion,");
     consultaSQL.append("t1.nombre_cliente,");
     consultaSQL.append("t1.faltante,");
     consultaSQL.append("t1.id_estacion,");
     consultaSQL.append("t1.id_tanque,");
     consultaSQL.append("t1.nombre_estacion,");
     consultaSQL.append("t1.nombre_tanque");
     consultaSQL.append(" FROM ");
     consultaSQL.append("sgo.v_liquidacion_inventario_x_estacion_completo_total t1 ");
     consultaSQL.append(sqlWhere);
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new LiquidacionMapper());
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
  
  public RespuestaCompuesta recuperarRegistrosxTanque(ParametrosListar argumentosListar) {
   String sqlLimit = "";
   List<String> filtrosWhere= new ArrayList<String>();
   String sqlWhere="";
   int totalRegistros = 0, totalEncontrados = 0;
   RespuestaCompuesta respuesta = new RespuestaCompuesta();
   Contenido<Liquidacion> contenido = new Contenido<Liquidacion>();
   List<Liquidacion> listaRegistros = new ArrayList<Liquidacion>();
   List<Object> parametros = new ArrayList<Object>();
   try {
     if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
       sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
       parametros.add(argumentosListar.getInicioPaginacion());
       parametros.add(argumentosListar.getRegistrosxPagina());
     }
     StringBuilder consultaSQL = new StringBuilder();
     if (argumentosListar.getIdOperacion() > 0){
      filtrosWhere.add(" (t1.id_operacion ='"+ argumentosListar.getIdOperacion() +"') ");
    }
     
     if (!argumentosListar.getFiltroFechaDiaOperativo().isEmpty()){
      filtrosWhere.add(" ( t1.fecha_operativa='" + argumentosListar.getFiltroFechaDiaOperativo()+"') ");
    }
     
     if (argumentosListar.getFiltroProducto() > 0){
      filtrosWhere.add(" (t1.id_producto ='"+ argumentosListar.getFiltroProducto() +"') ");
    }
     
     if (argumentosListar.getFiltroEstacion() > 0){
      filtrosWhere.add(" (t1.id_estacion ='"+ argumentosListar.getFiltroEstacion() +"') ");
    }   
    
    if(!filtrosWhere.isEmpty()){
      consultaSQL.setLength(0);
      sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
    }
     
     consultaSQL.setLength(0);
     consultaSQL.append("SELECT ");
     consultaSQL.append("t1.id_operacion,");
     consultaSQL.append("t1.fecha_operativa,");
     consultaSQL.append("t1.id_producto,");
     consultaSQL.append("t1.porcentaje_actual,");
     consultaSQL.append("t1.stock_final,");
     consultaSQL.append("t1.stock_inicial,");
     consultaSQL.append("t1.volumen_descargado,");
     consultaSQL.append("t1.volumen_despacho,");
     consultaSQL.append("t1.tolerancia,");
     consultaSQL.append("t1.stock_final_calculado,");
     consultaSQL.append("t1.variacion,");
     consultaSQL.append("t1.variacion_absoluta,");
     consultaSQL.append("t1.nombre_producto,");
     consultaSQL.append("t1.nombre_operacion,");
     consultaSQL.append("t1.nombre_cliente,");
     consultaSQL.append("t1.faltante,");
     consultaSQL.append("t1.id_estacion,");
     consultaSQL.append("t1.id_tanque,");
     consultaSQL.append("t1.nombre_estacion,");
     consultaSQL.append("t1.nombre_tanque");
     consultaSQL.append(" FROM ");
     consultaSQL.append("sgo.v_liquidacion_inventario_x_tanque_total t1 ");
     consultaSQL.append(sqlWhere);
     listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new LiquidacionMapper());
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
      List<Liquidacion> listaRegistros=new ArrayList<Liquidacion>();
      Contenido<Liquidacion> contenido = new Contenido<Liquidacion>();
      RespuestaCompuesta respuesta= new RespuestaCompuesta();
      try {
        consultaSQL.append("SELECT ");
        consultaSQL.append("t1.id_liquidacion,");
        consultaSQL.append("t1.id_operacion,");
        consultaSQL.append("t1.fecha_operativa,");
        consultaSQL.append("t1.id_producto,");
        consultaSQL.append("t1.porcentaje_actual,");
        consultaSQL.append("t1.stock_final,");
        consultaSQL.append("t1.stock_inicial,");
        consultaSQL.append("t1.volumen_descargado,");
        consultaSQL.append("t1.volumen_despacho,");
        consultaSQL.append("t1.tolerancia,");
        consultaSQL.append("t1.stock_final_calculado,");
        consultaSQL.append("t1.variacion,");
        consultaSQL.append("t1.variacion_absoluta,");
        consultaSQL.append("t1.nombre_producto,");
        consultaSQL.append("t1.nombre_operacion,");
        consultaSQL.append("t1.nombre_cliente,");
        consultaSQL.append("t1.faltante,");
        consultaSQL.append("t1.id_estacion,");
        consultaSQL.append("t1.id_tanque,");
        consultaSQL.append("t1.nombre_estacion,");
        consultaSQL.append("t1.nombre_tanque,");
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
        listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new LiquidacionMapper());
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
  
  public RespuestaCompuesta guardarRegistro(Liquidacion liquidacion){
    RespuestaCompuesta respuesta = new RespuestaCompuesta();
    StringBuilder consultaSQL= new StringBuilder();
    KeyHolder claveGenerada = null;
    int cantidadFilasAfectadas=0;
    try {
      consultaSQL.append("INSERT INTO ");
      consultaSQL.append(NOMBRE_TABLA);
      consultaSQL.append(" (id_operacion,fecha_operativa,id_producto,porcentaje_actual,stock_final,stock_inicial,volumen_descargado,volumen_despacho,tolerancia,stock_final_calculado,variacion,variacion_absoluta,nombre_producto,nombre_operacion,nombre_cliente,faltante,id_estacion,id_tanque,nombre_estacion,nombre_tanque) ");
      consultaSQL.append(" VALUES (:IdOperacion,:FechaOperativa,:IdProducto,:PorcentajeActual,:StockFinal,:StockInicial,:VolumenDescargado,:VolumenDespacho,:Tolerancia,:StockFinalCalculado,:Variacion,:VariacionAbsoluta,:NombreProducto,:NombreOperacion,:NombreCliente,:Faltante,:IdEstacion,:IdTanque,:NombreEstacion,:NombreTanque) ");
      MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
      listaParametros.addValue("IdOperacion", liquidacion.getIdOperacion());
      listaParametros.addValue("FechaOperativa", liquidacion.getFechaOperativa());
      listaParametros.addValue("IdProducto", liquidacion.getIdProducto());
      listaParametros.addValue("PorcentajeActual", liquidacion.getPorcentajeActual());
      listaParametros.addValue("StockFinal", liquidacion.getStockFinal());
      listaParametros.addValue("StockInicial", liquidacion.getStockInicial());
      listaParametros.addValue("VolumenDescargado", liquidacion.getVolumenDescargado());
      listaParametros.addValue("VolumenDespacho", liquidacion.getVolumenDespacho());
      listaParametros.addValue("Tolerancia", liquidacion.getTolerancia());
      listaParametros.addValue("StockFinalCalculado", liquidacion.getStockFinalCalculado());
      listaParametros.addValue("Variacion", liquidacion.getVariacion());
      listaParametros.addValue("VariacionAbsoluta", liquidacion.getVariacionAbsoluta());
      listaParametros.addValue("NombreProducto", liquidacion.getNombreProducto());
      listaParametros.addValue("NombreOperacion", liquidacion.getNombreOperacion());
      listaParametros.addValue("NombreCliente", liquidacion.getNombreCliente());
      listaParametros.addValue("Faltante", liquidacion.getFaltante());
      listaParametros.addValue("IdEstacion", liquidacion.getIdEstacion());
      listaParametros.addValue("IdTanque", liquidacion.getIdTanque());
      listaParametros.addValue("NombreEstacion", liquidacion.getNombreEstacion());
      listaParametros.addValue("NombreTanque", liquidacion.getNombreTanque());
      
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

}