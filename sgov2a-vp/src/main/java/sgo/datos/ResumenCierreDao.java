package sgo.datos;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.ResumenCierre;
import sgo.utilidades.Constante;

@Repository
public class ResumenCierreDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "cliente";
 public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_reporte_cierre_final";
 public static final String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "fecha_operativa";
 public static final String NOMBRE_CAMPO_CLAVE = "id";

 @Autowired
 public void setDataSource(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
  this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }

 public DataSource getDataSource() {
  return this.jdbcTemplate.getDataSource();
 }

 public String mapearCampoOrdenamiento(String propiedad) {
  String campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
  try {

  } catch (Exception excepcion) {

  }
  return campoOrdenamiento;
 }

 public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
  String sqlLimit = "";
  String sqlOrderBy = "";
  List<String> filtrosWhere = new ArrayList<String>();
  String sqlWhere = "";
  int totalRegistros = 0, totalEncontrados = 0;
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<ResumenCierre> contenido = new Contenido<ResumenCierre>();
  List<ResumenCierre> listaRegistros = new ArrayList<ResumenCierre>();
  List<Object> parametros = new ArrayList<Object>();
  try {
   if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
    sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
    parametros.add(argumentosListar.getInicioPaginacion());
    parametros.add(argumentosListar.getRegistrosxPagina());
   }

   sqlOrderBy = " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();

   StringBuilder consultaSQL = new StringBuilder();

   if (argumentosListar.getFiltroDiaOperativo() > 0) {
    filtrosWhere.add("dia_operativo_descarga='" + argumentosListar.getFiltroDiaOperativo() + "'");
   }

   if (!filtrosWhere.isEmpty()) {
    consultaSQL.setLength(0);
    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
    consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
    totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
   }
   totalEncontrados = totalRegistros;

   consultaSQL.setLength(0);
   consultaSQL.append("SELECT ");
   consultaSQL.append("t1.id,");
   consultaSQL.append("t1.id_tracto,");
   consultaSQL.append("t1.id_cisterna,");
   consultaSQL.append("t1.id_transportista,");
   consultaSQL.append("t1.id_transporte,");
   consultaSQL.append("t1.id_estacion,");
   consultaSQL.append("t1.id_operacion,");
   consultaSQL.append("t1.metodo_descarga,");
   consultaSQL.append("t1.entrada_total,");
   consultaSQL.append("t1.salida_total,");
   consultaSQL.append("t1.variacion,");
   consultaSQL.append("t1.limite_permisible,");
   consultaSQL.append("t1.faltante,");
   consultaSQL.append("t1.resultado,");
   consultaSQL.append("t1.placa_tracto,");
   consultaSQL.append("t1.placa_cisterna,");
   consultaSQL.append("t1.razon_social_transportista,");
   consultaSQL.append("t1.nombre_estacion,");
   consultaSQL.append("t1.dia_operativo_descarga,");
   consultaSQL.append("t1.fecha_operativa,");
   consultaSQL.append("t1.volumen_observado_despachado,");
   consultaSQL.append("t1.volumen_corregido_despachado,");
   consultaSQL.append("t1.volumen_observado_recibido,");
   consultaSQL.append("t1.volumen_corregido_recibido,");
   consultaSQL.append("t1.descripcion,");
   consultaSQL.append("t1.estado_descarga");
   consultaSQL.append(" FROM ");
   consultaSQL.append(NOMBRE_VISTA);
   consultaSQL.append(" t1 ");
   consultaSQL.append(sqlWhere);
   consultaSQL.append(sqlOrderBy);
   consultaSQL.append(sqlLimit);
   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), parametros.toArray(), new ResumenCierreMapper());
   contenido.carga = listaRegistros;
   respuesta.estado = true;
   respuesta.contenido = contenido;
   respuesta.contenido.totalRegistros = totalRegistros;
   respuesta.contenido.totalEncontrados = totalEncontrados;
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
   respuesta.contenido = null;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.contenido = null;
   respuesta.estado = false;
  }
  return respuesta;
 }
}
