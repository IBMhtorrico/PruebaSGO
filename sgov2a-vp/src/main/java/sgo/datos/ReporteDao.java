package sgo.datos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sgo.entidad.Contenido;
import sgo.entidad.EtapaTransporte;
import sgo.entidad.ReporteTiempoEtapas;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;
@Repository
public class ReporteDao {
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
 
 public RespuestaCompuesta recuperarRegistrosJasper(String consultaSQL ){
	  RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<HashMap<String,Object>> contenido = new Contenido<HashMap<String,Object>>();
	  List<HashMap<String,Object>> listaRegistros = new ArrayList<HashMap<String,Object>>();
	  List<Object> parametros = new ArrayList<Object>();
	  try {
	    listaRegistros = jdbcTemplate.query(consultaSQL,parametros.toArray(), new GenericoJasperMapper());
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
 
 //Agregado por 9000002570===================================
 public RespuestaCompuesta recuperarRegistrosTiempoEtapas(Map<String,Object> parametrosReporte, String where){
	 RespuestaCompuesta respuesta = new RespuestaCompuesta();
	  Contenido<HashMap<String,Object>> contenido = new Contenido<HashMap<String,Object>>();
	  List<HashMap<String,Object>> listaRegistros = new ArrayList<HashMap<String,Object>>();
	  
	  List<ReporteTiempoEtapas> lstData;
	  
	  try {
		  
		  lstData = obtenerDataTiempoEtapas(parametrosReporte, where);
		  listaRegistros = mapearTiempoEtapas(lstData);
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
 
 private List<ReporteTiempoEtapas> obtenerDataTiempoEtapas(Map<String,Object> parametrosReporte, String where){
	 
	 int size;
	 Integer idTransporte;
	 StringBuilder consultaSQL= new StringBuilder();
	 ReporteTiempoEtapas data;
	 List<ReporteTiempoEtapas> lstData;
	 List<Integer> idsTransporte;
	 
	 EtapaTransporte etapa;
	 List<EtapaTransporte> lstEtapas = new ArrayList<EtapaTransporte>();
	 Map<Integer, List<EtapaTransporte>> mapEtapas = new HashMap<Integer, List<EtapaTransporte>>();
	 
	 try {
		 
		 consultaSQL.append("SELECT  t1.id_transporte, 	");
		 consultaSQL.append("	t3.fecha_operativa,		");
		 consultaSQL.append("	t4.placa || ' / ' || t5.placa cisterna,	");
		 consultaSQL.append("	t6.nombre_corto		");
		 consultaSQL.append("FROM sgo.transporte t1	");
		 consultaSQL.append("INNER JOIN sgo.asignacion t2 on (t1.id_transporte = t2.id_transporte)	");
		 consultaSQL.append("INNER JOIN sgo.dia_operativo t3 on (t2.id_doperativo = t3.id_doperativo)	");
		 consultaSQL.append("INNER JOIN sgo.cisterna t4 on (t1.id_cisterna = t4.id_cisterna) 			");
		 consultaSQL.append("INNER JOIN sgo.tracto t5 on (t1.id_tracto = t5.id_tracto)					");
		 consultaSQL.append("INNER JOIN sgo.transportista t6 on (t1.id_transportista = t6.id_transportista)	");
		 consultaSQL.append(where);
		 consultaSQL.append(" ORDER BY t3.fecha_operativa DESC");
		 
		 System.out.println(consultaSQL.toString());
		 List<Map<String,Object>> mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
		 
		 lstData = new ArrayList<ReporteTiempoEtapas>();
		 idsTransporte = new ArrayList<Integer>();
		 if(mapRegistros != null && mapRegistros.size() > 0){
			
			 for(Map<String,Object> obj : mapRegistros){
				 data = new ReporteTiempoEtapas();
				 idTransporte = (Integer) obj.get("id_transporte");
				 data.setIdTransporte(idTransporte);
				 idsTransporte.add(idTransporte);
				 
				 data.setFechaPlanificada((Date) obj.get("fecha_operativa"));
				 data.setCisterna((String) obj.get("cisterna"));
				 data.setTransportista((String) obj.get("nombre_corto"));
				 
				 lstData.add(data);
			 }
			 
			 consultaSQL= new StringBuilder();
			 consultaSQL.append("SELECT t1.id_transporte, t2.nombre_etapa, tiempo_etapa, fecha_fin_etapa::timestamp::date, t2.orden ");
			 consultaSQL.append("FROM sgo.etapa_transporte t1	");
			 consultaSQL.append("INNER JOIN sgo.operacion_etapa_ruta t2 on (t1.id_operacion_etapa_ruta = t2.id_operacion_etapa_ruta) ");
			 consultaSQL.append("WHERE id_transporte in (" + Utilidades.devolverStringDeListInteger(idsTransporte)+ ")	");
			 consultaSQL.append("AND t2.estado = 1 ");
			 consultaSQL.append("ORDER BY id_transporte, t2.orden	");
			 
			 System.out.println(consultaSQL.toString());
			 mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
			 
			 if(mapRegistros != null && mapRegistros.size() > 0){
				 for(Map<String,Object> obj : mapRegistros){
					 idTransporte = (Integer) obj.get("id_transporte");
					 lstEtapas = mapEtapas.get(idTransporte);
					 
					 if(lstEtapas == null){
						 lstEtapas = new ArrayList<EtapaTransporte>();
					 }
					 
					 etapa = new EtapaTransporte();
					 etapa.setNombreEtapa((String) obj.get("nombre_etapa"));
					 etapa.setTiempoEtapa((Integer) obj.get("tiempo_etapa"));
					 etapa.setFechaArribo((Date) obj.get("fecha_fin_etapa"));
					 etapa.setOrden((Integer) obj.get("orden"));
					 
					 lstEtapas.add(etapa);
					 
					 mapEtapas.put(idTransporte, lstEtapas);
				 }
			 }
			 
			 EtapaTransporte et;
			 for(ReporteTiempoEtapas obj : lstData){
				 idTransporte = obj.getIdTransporte();
				 lstEtapas = mapEtapas.get(idTransporte);
				 
				 if(lstEtapas != null){
					 
					 size = lstEtapas.size();
					 if(size > 0){
						 obj.setFechaArribo(lstEtapas.get(size-1).getFechaArribo());
					 }

					 if(size>0){
						 et = lstEtapas.get(0);
						 parametrosReporte.put("colEtapa1", et.getNombreEtapa());
						 obj.setEtapa1(et.getTiempoEtapa());
					 }
					 
					 if(size>1){
						 et = lstEtapas.get(1);
						 parametrosReporte.put("colEtapa2", et.getNombreEtapa());
						 obj.setEtapa2(et.getTiempoEtapa());
					 }
					 
					 if(size>2){
						 et = lstEtapas.get(2);
						 parametrosReporte.put("colEtapa3", et.getNombreEtapa());
						 obj.setEtapa3(et.getTiempoEtapa());
					 }
					 
					 if(size>3){
						 et = lstEtapas.get(3);
						 parametrosReporte.put("colEtapa4", et.getNombreEtapa());
						 obj.setEtapa4(et.getTiempoEtapa());
					 }
					 
					 if(size>4){
						 et = lstEtapas.get(4);
						 parametrosReporte.put("colEtapa5", et.getNombreEtapa());
						 obj.setEtapa5(et.getTiempoEtapa());
					 }
					 
					 if(size>5){
						 et = lstEtapas.get(5);
						 parametrosReporte.put("colEtapa6", et.getNombreEtapa());
						 obj.setEtapa6(et.getTiempoEtapa());
					 }
					 
					 if(size>6){
						 et = lstEtapas.get(6);
						 parametrosReporte.put("colEtapa7", et.getNombreEtapa());
						 obj.setEtapa7(et.getTiempoEtapa());
					 }
					 
					 if(size>7){
						 et = lstEtapas.get(7);
						 parametrosReporte.put("colEtapa8", et.getNombreEtapa());
						 obj.setEtapa8(et.getTiempoEtapa());
					 }
					 
					 if(size>8){
						 et = lstEtapas.get(8);
						 parametrosReporte.put("colEtapa9", et.getNombreEtapa());
						 obj.setEtapa9(et.getTiempoEtapa());
					 }
					 
					 if(size>9){
						 et = lstEtapas.get(9);
						 parametrosReporte.put("colEtapa10", et.getNombreEtapa());
						 obj.setEtapa10(et.getTiempoEtapa());
					 }
					 
					 if(size>10){
						 et = lstEtapas.get(10);
						 parametrosReporte.put("colEtapa11", et.getNombreEtapa());
						 obj.setEtapa11(et.getTiempoEtapa());
					 }
					 
					 if(size>11){
						 et = lstEtapas.get(11);
						 parametrosReporte.put("colEtapa12", et.getNombreEtapa());
						 obj.setEtapa12(et.getTiempoEtapa());
					 }
					 
					 if(size>12){
						 et = lstEtapas.get(12);
						 parametrosReporte.put("colEtapa13", et.getNombreEtapa());
						 obj.setEtapa13(et.getTiempoEtapa());
					 }
					 
					 if(size>13){
						 et = lstEtapas.get(13);
						 parametrosReporte.put("colEtapa14", et.getNombreEtapa());
						 obj.setEtapa14(et.getTiempoEtapa());
					 }
				 }
				 
			 }

		 }

	 } catch (DataAccessException excepcionAccesoDatos) {
		 excepcionAccesoDatos.printStackTrace();
		 return new ArrayList<ReporteTiempoEtapas>();
	 }
	 
	 return lstData;
 }
 
 private List<HashMap<String,Object>> mapearTiempoEtapas(List<ReporteTiempoEtapas> lstData){
	 
	 List<HashMap<String,Object>> listaRegistros = new ArrayList<HashMap<String,Object>>();
	 
	 HashMap<String,Object> hmRegistro;
	 
	 ReporteTiempoEtapas data;
	 
	 try{
		 
		 int size = lstData.size();
		 for(int i = 0; i < size; i++){
			 data = lstData.get(i);
			 
			 hmRegistro = new HashMap<String,Object>();
					 
			 hmRegistro.put("fechaArribo", data.getFechaArribo());
			 hmRegistro.put("fechaPlanificada", data.getFechaPlanificada());
			 
			 hmRegistro.put("cisterna", data.getCisterna());
			 hmRegistro.put("transportista", data.getTransportista());
			 
			 
			 hmRegistro.put("etapa1", data.getEtapa1());
			 hmRegistro.put("etapa2", data.getEtapa2());
			 hmRegistro.put("etapa3", data.getEtapa3());
			 hmRegistro.put("etapa4", data.getEtapa4());
			 hmRegistro.put("etapa5", data.getEtapa5());
			 hmRegistro.put("etapa6", data.getEtapa6());
			 hmRegistro.put("etapa7", data.getEtapa7());
			 hmRegistro.put("etapa8", data.getEtapa8());
			 hmRegistro.put("etapa9", data.getEtapa9());
			 hmRegistro.put("etapa10", data.getEtapa10());
			 
			 listaRegistros.add(hmRegistro);
			 
		 }
	 
//	 SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
//	 String startDate="23-10-2017";
//	 java.util.Date date = sdf1.parse(startDate);
//	 
//	 hmRegistro.put("fechaArribo", new java.sql.Date(date.getTime()));
//	 
//	 startDate="24-10-2017";
//	 date = sdf1.parse(startDate);
//	 hmRegistro.put("fechaPlanificada", new java.sql.Date(date.getTime()));
//	 
//	 hmRegistro.put("cisterna", "PEAAX995/PEAPZ766");
//	 hmRegistro.put("transportista", "SERVOZA");
//	 
//	 
//	 hmRegistro.put("etapa1", 59);
//	 hmRegistro.put("etapa2", 15);
//	 hmRegistro.put("etapa3", 13);
//	 hmRegistro.put("etapa4", 213);
//	 hmRegistro.put("etapa5", 13);
//	 hmRegistro.put("etapa6", null);
//	 hmRegistro.put("etapa7", 46);
//	 hmRegistro.put("etapa8", 5);
//	 hmRegistro.put("etapa9", 53);
//	 hmRegistro.put("etapa10", 53);
	 
	 }catch(Exception ex){
		 ex.printStackTrace();
	 }
	 
	 return listaRegistros;
 }
 //==========================================================
}
