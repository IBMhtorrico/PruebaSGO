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

import sgo.entidad.Contenido;
import sgo.entidad.DescargaCisterna;
import sgo.entidad.DescargaResumen;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class DescargaCisternaDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "descarga_cisterna";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_descarga_base";
	public static final String NOMBRE_VISTA_RESUMEN = Constante.ESQUEMA_APLICACION + "v_descarga_resumen";
	public final static String NOMBRE_CAMPO_CLAVE = "id_dcisterna";
	public final static String CAMPO_CARGA_TANQUE="id_ctanque";
	
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
				campoOrdenamiento="id_dcisterna";
			}
			if (propiedad.equals("idCargaTanque")){
				campoOrdenamiento="id_ctanque";
			}
			if (propiedad.equals("idTransporte")){
				campoOrdenamiento="id_transporte";
			}
			if (propiedad.equals("fechaArribo")){
				campoOrdenamiento="fecha_arribo";
			}
			if (propiedad.equals("fechaFiscalizacion")){
				campoOrdenamiento="fecha_fiscalizacion";
			}
			if (propiedad.equals("metodoDescarga")){
				campoOrdenamiento="metodo_descarga";
			}
			if (propiedad.equals("numeroComprobante")){
				campoOrdenamiento="numero_comprobante";
			}
			if (propiedad.equals("lecturaInicial")){
				campoOrdenamiento="lectura_inicial";
			}
			if (propiedad.equals("lecturaFinal")){
				campoOrdenamiento="lectura_final";
			}
			if (propiedad.equals("pesajeInicial")){
				campoOrdenamiento="pesaje_inicial";
			}
			if (propiedad.equals("pesajeFinal")){
				campoOrdenamiento="pesaje_final";
			}
			if (propiedad.equals("factorConversion")){
				campoOrdenamiento="factor_conversion";
			}
			if (propiedad.equals("pesoNeto")){
				campoOrdenamiento="peso_neto";
			}
			if (propiedad.equals("volumenTotalDescargadoObservado")){
				campoOrdenamiento="volumen_total_descargado_observado";
			}
			if (propiedad.equals("volumenTotalDescargadoCorregido")){
				campoOrdenamiento="volumen_total_descargado_corregido";
			}
			if (propiedad.equals("variacionCorregido")){
				campoOrdenamiento="variacion_volumen";
			}
			if (propiedad.equals("mermaPorcentaje")){
				campoOrdenamiento="merma_porcentaje";
			}
			if (propiedad.equals("mermaPermisible")){
				campoOrdenamiento="merma_permisible";
			}
			if (propiedad.equals("volumenExcedenteCorregido")){
				campoOrdenamiento="excedente_temperatura_base";
			}
			if (propiedad.equals("volumenExcedenteObservado")){
				campoOrdenamiento="excedente_temperatura_observada";
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
		Contenido<DescargaResumen> contenido = new Contenido<DescargaResumen>();
		List<DescargaResumen> listaRegistros = new ArrayList<DescargaResumen>();
		List<Object> parametros = new ArrayList<Object>();
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere="";
		StringBuilder consultaSQL= null;
		try {
		 consultaSQL = new StringBuilder();
			
	  if(argumentosListar.getFiltroCargaTanque()!= 0){
        filtrosWhere.add(" t1."+CAMPO_CARGA_TANQUE + "=" + argumentosListar.getFiltroCargaTanque());
      }
	  
	  if(argumentosListar.getIdTransporte()!= 0){
	        filtrosWhere.add(" t1.id_transporte =" + argumentosListar.getIdTransporte());
	  }
      
      if(!filtrosWhere.isEmpty()){
        sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
      }
      
      if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
       sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
       parametros.add(argumentosListar.getInicioPaginacion());
       parametros.add(argumentosListar.getRegistrosxPagina());
       
       consultaSQL.setLength(0);
       consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA+ " t1 " + sqlWhere);
       totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
       totalEncontrados=totalRegistros;
     }
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_dcisterna,");
			consultaSQL.append("t1.id_ctanque,");
			consultaSQL.append("t1.id_transporte,");
			consultaSQL.append("t1.placa_cisterna,");
			consultaSQL.append("t1.placa_tracto,");
			consultaSQL.append("t1.numero_guia_remision,");
			consultaSQL.append("t1.despachado,");
			consultaSQL.append("t1.volumen_total_descargado_corregido as recibido,");
			consultaSQL.append("t1.variacion_volumen as variacion,");
			consultaSQL.append("t1.estado");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA_RESUMEN);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlLimit);
			System.out.println(consultaSQL.toString());
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DescargaResumenMapper());
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
			List<DescargaCisterna> listaRegistros=new ArrayList<DescargaCisterna>();
			Contenido<DescargaCisterna> contenido = new Contenido<DescargaCisterna>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_dcisterna,");
				consultaSQL.append("t1.id_ctanque,");
				consultaSQL.append("t1.id_transporte,");
				consultaSQL.append("t1.fecha_arribo,");
				consultaSQL.append("t1.fecha_fiscalizacion,");
				consultaSQL.append("t1.metodo_descarga,");
				consultaSQL.append("t1.numero_comprobante,");
				consultaSQL.append("t1.lectura_inicial,");
				consultaSQL.append("t1.lectura_final,");
				consultaSQL.append("t1.pesaje_inicial,");
				consultaSQL.append("t1.pesaje_final,");
				consultaSQL.append("t1.factor_conversion,");
				consultaSQL.append("t1.peso_neto,");
				consultaSQL.append("t1.volumen_total_descargado_observado,");
				consultaSQL.append("t1.volumen_total_descargado_corregido,");
		    consultaSQL.append("t1.excedente_temperatura_base,");
	      consultaSQL.append("t1.excedente_temperatura_observada,");
				consultaSQL.append("t1.variacion_volumen,");
				consultaSQL.append("t1.merma_porcentaje,");
				consultaSQL.append("t1.merma_permisible,");
				consultaSQL.append("t1.nombre_operacion,");
				consultaSQL.append("t1.nombre_estacion,");
				consultaSQL.append("t1.descripcion_tanque,");
				consultaSQL.append("t1.fecha_operativa,");
				consultaSQL.append("t1.estado_descarga as estado,");
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
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DescargaCisternaMapper());
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
	
	public RespuestaCompuesta guardarRegistro(DescargaCisterna descarga_cisterna){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (id_ctanque,estado,id_transporte,fecha_arribo,fecha_fiscalizacion,metodo_descarga,numero_comprobante,lectura_inicial,lectura_final,pesaje_inicial,pesaje_final,factor_conversion,peso_neto,volumen_total_descargado_observado,volumen_total_descargado_corregido,variacion_volumen,merma_porcentaje,merma_permisible,excedente_temperatura_base,excedente_temperatura_observada,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:IdCargaTanque,:Estado,:IdTransporte,:FechaArribo,:FechaFiscalizacion,:MetodoDescarga,:NumeroComprobante,:LecturaInicial,:LecturaFinal,:PesajeInicial,:PesajeFinal,:FactorConversion,:PesoNeto,:VolumenTotalDescargadoObservado,:VolumenTotalDescargadoCorregido,:VariacionVolumen,:MermaPorcentaje,:MermaPermisible,:VolumenExcedenteCorregido,:VolumenExcedenteObservado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("IdCargaTanque", descarga_cisterna.getIdCargaTanque());
			listaParametros.addValue("Estado", descarga_cisterna.getEstado());
			listaParametros.addValue("IdTransporte", descarga_cisterna.getIdTransporte());
			listaParametros.addValue("FechaArribo", descarga_cisterna.getFechaArribo());
			listaParametros.addValue("FechaFiscalizacion", descarga_cisterna.getFechaFiscalizacion());
			listaParametros.addValue("MetodoDescarga", descarga_cisterna.getMetodoDescarga());
			listaParametros.addValue("NumeroComprobante", descarga_cisterna.getNumeroComprobante());
			listaParametros.addValue("LecturaInicial", descarga_cisterna.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", descarga_cisterna.getLecturaFinal());
			listaParametros.addValue("PesajeInicial", descarga_cisterna.getPesajeInicial());
			listaParametros.addValue("PesajeFinal", descarga_cisterna.getPesajeFinal());
			listaParametros.addValue("FactorConversion", descarga_cisterna.getFactorConversion());
			listaParametros.addValue("PesoNeto", descarga_cisterna.getPesoNeto());
			listaParametros.addValue("VolumenTotalDescargadoObservado", descarga_cisterna.getVolumenTotalDescargadoObservado());
			listaParametros.addValue("VolumenTotalDescargadoCorregido", descarga_cisterna.getVolumenTotalDescargadoCorregido());
			listaParametros.addValue("VariacionVolumen", descarga_cisterna.getVariacionVolumen());
			listaParametros.addValue("MermaPorcentaje", descarga_cisterna.getMermaPorcentaje());
			listaParametros.addValue("MermaPermisible", descarga_cisterna.getMermaPermisible());
			listaParametros.addValue("VolumenExcedenteCorregido", descarga_cisterna.getVolumenExcedenteCorregido());
			listaParametros.addValue("VolumenExcedenteObservado", descarga_cisterna.getVolumenExcedenteObservado());
			listaParametros.addValue("CreadoEl", descarga_cisterna.getCreadoEl());
			listaParametros.addValue("CreadoPor", descarga_cisterna.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", descarga_cisterna.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", descarga_cisterna.getActualizadoEl());
			listaParametros.addValue("IpCreacion", descarga_cisterna.getIpCreacion());
			listaParametros.addValue("IpActualizacion", descarga_cisterna.getIpActualizacion());
			
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
	
	public RespuestaCompuesta actualizarRegistro(DescargaCisterna descarga_cisterna){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_ctanque=:IdCargaTanque,");
			consultaSQL.append("id_transporte=:IdTransporte,");
			consultaSQL.append("fecha_arribo=:FechaArribo,");
			consultaSQL.append("fecha_fiscalizacion=:FechaFiscalizacion,");
			consultaSQL.append("metodo_descarga=:MetodoDescarga,");
			consultaSQL.append("numero_comprobante=:NumeroComprobante,");
			consultaSQL.append("lectura_inicial=:LecturaInicial,");
			consultaSQL.append("lectura_final=:LecturaFinal,");
			consultaSQL.append("estado=:Estado,");
			consultaSQL.append("pesaje_inicial=:PesajeInicial,");
			consultaSQL.append("pesaje_final=:PesajeFinal,");
			consultaSQL.append("factor_conversion=:FactorConversion,");
			consultaSQL.append("peso_neto=:PesoNeto,");
			consultaSQL.append("volumen_total_descargado_observado=:VolumenTotalDescargadoObservado,");
			consultaSQL.append("volumen_total_descargado_corregido=:VolumenTotalDescargadoCorregido,");
			consultaSQL.append("variacion_volumen=:VariacionCorregido,");
			consultaSQL.append("merma_porcentaje=:MermaPorcentaje,");
			consultaSQL.append("merma_permisible=:MermaPermisible,");
			consultaSQL.append("excedente_temperatura_base=:VolumenExcedenteCorregido,");
			consultaSQL.append("excedente_temperatura_observada=:VolumenExcedenteObservado,");			 
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("IdCargaTanque", descarga_cisterna.getIdCargaTanque());
			listaParametros.addValue("IdTransporte", descarga_cisterna.getIdTransporte());
			listaParametros.addValue("Estado", descarga_cisterna.getEstado());
			listaParametros.addValue("FechaArribo", descarga_cisterna.getFechaArribo());
			listaParametros.addValue("FechaFiscalizacion", descarga_cisterna.getFechaFiscalizacion());
			listaParametros.addValue("MetodoDescarga", descarga_cisterna.getMetodoDescarga());
			listaParametros.addValue("NumeroComprobante", descarga_cisterna.getNumeroComprobante());
			listaParametros.addValue("LecturaInicial", descarga_cisterna.getLecturaInicial());
			listaParametros.addValue("LecturaFinal", descarga_cisterna.getLecturaFinal());
			listaParametros.addValue("PesajeInicial", descarga_cisterna.getPesajeInicial());
			listaParametros.addValue("PesajeFinal", descarga_cisterna.getPesajeFinal());
			listaParametros.addValue("FactorConversion", descarga_cisterna.getFactorConversion());
			listaParametros.addValue("PesoNeto", descarga_cisterna.getPesoNeto());
			listaParametros.addValue("VolumenTotalDescargadoObservado", descarga_cisterna.getVolumenTotalDescargadoObservado());
			listaParametros.addValue("VolumenTotalDescargadoCorregido", descarga_cisterna.getVolumenTotalDescargadoCorregido());
			listaParametros.addValue("VariacionCorregido", descarga_cisterna.getVariacionVolumen());
			listaParametros.addValue("MermaPorcentaje", descarga_cisterna.getMermaPorcentaje());
			listaParametros.addValue("MermaPermisible", descarga_cisterna.getMermaPermisible());
			listaParametros.addValue("VolumenExcedenteCorregido", descarga_cisterna.getVolumenExcedenteCorregido());
			listaParametros.addValue("VolumenExcedenteObservado", descarga_cisterna.getVolumenExcedenteObservado());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", descarga_cisterna.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", descarga_cisterna.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", descarga_cisterna.getIpActualizacion());
			listaParametros.addValue("Id", descarga_cisterna.getId());
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