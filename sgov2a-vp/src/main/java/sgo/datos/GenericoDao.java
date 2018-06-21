package sgo.datos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sgo.entidad.Contenido;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
@Repository
public class GenericoDao {
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedJdbcTemplate;
 @Autowired
 public void setDataSource(DataSource dataSource) {
   this.jdbcTemplate = new JdbcTemplate(dataSource);
   this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }
 
 public DataSource getDataSource(){
   return this.jdbcTemplate.getDataSource();
 }
 
 public RespuestaCompuesta recuperarRegistros(String consultaSQL ){
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<HashMap<String,String>> contenido = new Contenido<HashMap<String,String>>();
  List<HashMap<String,String>> listaRegistros = new ArrayList<HashMap<String,String>>();
  List<Object> parametros = new ArrayList<Object>();
  try {
    listaRegistros = jdbcTemplate.query(consultaSQL,parametros.toArray(), new GenericoMapper());
    contenido.carga = listaRegistros;
    respuesta.estado = true;
    respuesta.contenido = contenido;
    respuesta.contenido.totalRegistros = listaRegistros.size();
    respuesta.contenido.totalEncontrados =listaRegistros.size();
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
 
 public RespuestaCompuesta recuperarRegistros(ArrayList<Campo> listaCampos, String tabla ){
  RespuestaCompuesta respuesta = new RespuestaCompuesta();
  Contenido<HashMap<String,String>> contenido = new Contenido<HashMap<String,String>>();
  List<HashMap<String,String>> listaRegistros = new ArrayList<HashMap<String,String>>();
  List<Object> parametros = new ArrayList<Object>();
  String consultaSQL="";
  String separador="";
  try {
   for(Campo campo : listaCampos){
    consultaSQL = consultaSQL+separador+ campo.getNombre();
    separador=",";
   }
   consultaSQL= "SELECT " + consultaSQL + " FROM " + tabla;
    listaRegistros = jdbcTemplate.query(consultaSQL,parametros.toArray(), new GenericoMapper());
    contenido.carga = listaRegistros;
    respuesta.estado = true;
    respuesta.contenido = contenido;
    respuesta.contenido.totalRegistros = listaRegistros.size();
    respuesta.contenido.totalEncontrados =listaRegistros.size();
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
}
