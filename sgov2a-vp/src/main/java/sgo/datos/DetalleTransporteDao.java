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

import sgo.entidad.ClonacionProgTrans;
import sgo.entidad.Contenido;
import sgo.entidad.DetalleTransporte;
import sgo.entidad.DetalleTransporteExtendido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class DetalleTransporteDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "detalle_transporte";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_detalle_transporte";
	public static final String NOMBRE_VISTA_EXTENDIDA = Constante.ESQUEMA_APLICACION + "v_detalle_transporte_ex_completo";
	public final static String NOMBRE_CAMPO_CLAVE = "id_dtransporte";
	public final static String NOMBRE_CAMPO_CLAVE_TRANSPORTE = "id_transporte";
	
	public final static int ID_SIN_PRODUCTO = 11;
	
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
				campoOrdenamiento="id_dtransporte";
			}
			if (propiedad.equals("idTransporte")){
				campoOrdenamiento="id_transporte";
			}
			if (propiedad.equals("idProducto")){
				campoOrdenamiento="id_producto";
			}
			if (propiedad.equals("capacidadVolumetricaCompartimento")){
				campoOrdenamiento="capacidad_volumetrica_compartimento";
			}
			if (propiedad.equals("unidadMedidaVolumen")){
				campoOrdenamiento="unidad_medida_volumen";
			}
			if (propiedad.equals("numeroCompartimento")){
				campoOrdenamiento="numero_compartimento";
			}
			if (propiedad.equals("volumenTemperaturaObservada")){
				campoOrdenamiento="volumen_temperatura_observada";
			}
			if (propiedad.equals("temperaturaObservada")){
				campoOrdenamiento="temperatura_observada";
			}
			if (propiedad.equals("apiTemperaturaBase")){
				campoOrdenamiento="api_temperatura_base";
			}
			if (propiedad.equals("factorCorrecion")){
				campoOrdenamiento="factor_correcion";
			}
			if (propiedad.equals("volumenTemperaturaBase")){
				campoOrdenamiento="volumen_temperatura_base";
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
		Contenido<DetalleTransporte> contenido = new Contenido<DetalleTransporte>();
		List<DetalleTransporte> listaRegistros = new ArrayList<DetalleTransporte>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			

			consultaSQL.append("t1.id_transporte,");
			consultaSQL.append("t1.numero_guia_remision,");
			consultaSQL.append("t1.numero_orden_entrega,");
			consultaSQL.append("t1.numero_factura,");
			consultaSQL.append("t1.codigo_scop,");
			consultaSQL.append("t1.fecha_emision,");
			consultaSQL.append("t1.planta_despacho,");
			consultaSQL.append("t1.planta_recepcion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.id_conductor,");
			consultaSQL.append("t1.brevete_conductor,");
			consultaSQL.append("t1.id_cisterna,");
			consultaSQL.append("t1.placa_cisterna,");
			consultaSQL.append("t1.tarjeta_cubicacion_cisterna,");
			consultaSQL.append("t1.id_tracto,");
			consultaSQL.append("t1.placa_tracto,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.volumen_total_observado,");
			consultaSQL.append("t1.volumen_total_corregido,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.sincronizado_el,");
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
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new DetalleTransporteMapper());
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
	
	/**
     * Metodo para recuperar el detalle de transporte por el ID transporte.
     * @param diasOperativos      Identificador del transporte.
     * @return respuesta		  Resgitro DetalleTransporte.
     */
	public RespuestaCompuesta recuperarRegistrosPorIdTransporte(int IdTransporte){
			StringBuilder consultaSQL= new StringBuilder();		
			List<DetalleTransporte> listaRegistros=new ArrayList<DetalleTransporte>();
			Contenido<DetalleTransporte> contenido = new Contenido<DetalleTransporte>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.setLength(0);
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_dtransporte, ");
				consultaSQL.append("t1.id_transporte, ");
				consultaSQL.append("t1.id_producto, ");
				consultaSQL.append("t1.capacidad_volumetrica_compartimento, ");
				consultaSQL.append("t1.unidad_medida_volumen, ");
				consultaSQL.append("t1.numero_compartimento, ");
				consultaSQL.append("t1.volumen_temperatura_observada, ");
				consultaSQL.append("t1.temperatura_observada, ");
				consultaSQL.append("t1.api_temperatura_base, ");
				consultaSQL.append("t1.factor_correcion, ");
				consultaSQL.append("t1.volumen_temperatura_base, ");
				consultaSQL.append("t1.descripcion_producto ");
				//consultaSQL.append("t1.altura_compartimento ");
				//Esta tabla no tienes campos de auditoria 
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE_TRANSPORTE);
				consultaSQL.append(" = ?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {IdTransporte},new DetalleTransporteMapper());
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
	
	
	
	 public RespuestaCompuesta recuperarDetalleTransporte(int IdTransporte){
    StringBuilder consultaSQL= new StringBuilder();   
    List<DetalleTransporteExtendido> listaRegistros=new ArrayList<DetalleTransporteExtendido>();
    Contenido<DetalleTransporteExtendido> contenido = new Contenido<DetalleTransporteExtendido>();
    RespuestaCompuesta respuesta= new RespuestaCompuesta();
    try {
      consultaSQL.setLength(0);
      consultaSQL.append("SELECT ");
      consultaSQL.append("t1.id_dtransporte, ");
      consultaSQL.append("t1.id_transporte, ");
      consultaSQL.append("t1.id_producto, ");
      consultaSQL.append("t1.capacidad_volumetrica_compartimento, ");
      consultaSQL.append("t1.unidad_medida_volumen, ");
      consultaSQL.append("t1.numero_compartimento, ");
      consultaSQL.append("t1.volumen_temperatura_observada, ");
      consultaSQL.append("t1.temperatura_observada, ");
      consultaSQL.append("t1.api_temperatura_base, ");
      consultaSQL.append("t1.factor_correcion, ");
      consultaSQL.append("t1.volumen_temperatura_base, ");
      consultaSQL.append("t1.descripcion_producto, ");
      consultaSQL.append("t1.id_compartimento ");
      //consultaSQL.append("t1.altura_compartimento ");
      //Esta tabla no tienes campos de auditoria 
      consultaSQL.append(" FROM ");       
      consultaSQL.append(NOMBRE_VISTA_EXTENDIDA);
      consultaSQL.append(" t1 ");
      consultaSQL.append(" WHERE ");
      consultaSQL.append(NOMBRE_CAMPO_CLAVE_TRANSPORTE);
      consultaSQL.append(" = ?");
      consultaSQL.append("ORDER BY t1.numero_compartimento");
      listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {IdTransporte},new DetalleTransporteMapperExtendido());
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
	
	
	/**
     * Metodo para recuperar el detalle de transporte por su identificador.
     * @param diasOperativos      Identificador del transporte.
     * @return respuesta		  Resgitro Transporte.
     */
	public RespuestaCompuesta recuperarRegistro(int ID){
			StringBuilder consultaSQL= new StringBuilder();		
			List<DetalleTransporte> listaRegistros=new ArrayList<DetalleTransporte>();
			Contenido<DetalleTransporte> contenido = new Contenido<DetalleTransporte>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_dtransporte, ");
				consultaSQL.append("t1.id_transporte, ");
				consultaSQL.append("t1.id_producto, ");
				consultaSQL.append("t1.capacidad_volumetrica_compartimento, ");
				consultaSQL.append("t1.unidad_medida_volumen, ");
				consultaSQL.append("t1.numero_compartimento, ");
				consultaSQL.append("t1.volumen_temperatura_observada, ");
				consultaSQL.append("t1.temperatura_observada, ");
				consultaSQL.append("t1.api_temperatura_base, ");
				consultaSQL.append("t1.factor_correcion, ");
				consultaSQL.append("t1.volumen_temperatura_base, ");
				consultaSQL.append("t1.descripcion_producto ");
				//consultaSQL.append("t1.altura_compartimento ");
				//Esta tabla no tienes campos de auditoria 
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new DetalleTransporteMapper());
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
	
	public RespuestaCompuesta guardarRegistro(DetalleTransporte eDetalleTransporte){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			//consultaSQL.append(" (id_producto, capacidad_volumetrica_compartimento,altura_compartimento, id_transporte, unidad_medida_volumen, numero_compartimento,volumen_temperatura_observada, volumen_temperatura_base, api_temperatura_base,factor_correcion, temperatura_observada) ");
			consultaSQL.append(" (capacidad_volumetrica_compartimento, unidad_medida_volumen, numero_compartimento, volumen_temperatura_observada, volumen_temperatura_base, ");
			consultaSQL.append(" api_temperatura_base, factor_correcion, temperatura_observada, id_producto, id_transporte) ");
			
			consultaSQL.append(" VALUES (:CapacidadVolumetricaCompartimento, :UnidadMedida, :NumeroCompartimento, :VolumenTemperaturaObservada, :VolumenTemperaturaBase, ");
			consultaSQL.append(" :ApiTemperaturaBase, :FactorCorrecion, :TemperaturaObservada, :IdProducto, :IdTransporte) ");
			
			//consultaSQL.append(" VALUES (:IdProducto,:CapacidadVolumetricaCompartimento,:AlturaCompartimento,:IdTransporte,:UnidadMedida,:NumeroCompartimento,:VolumenTemperaturaObservada,:VolumenTemperaturaBase,:ApiTemperaturaBase,:FactorCorrecion,:TemperaturaObservada) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   

			System.out.print("1 id_producto: " + eDetalleTransporte.getIdProducto());
			if(eDetalleTransporte.getIdProducto() <= 0){
				eDetalleTransporte.setIdProducto(ID_SIN_PRODUCTO);
			}
			System.out.print("2 id_producto: " + eDetalleTransporte.getIdProducto());
			listaParametros.addValue("IdProducto", eDetalleTransporte.getIdProducto());
			listaParametros.addValue("CapacidadVolumetricaCompartimento", eDetalleTransporte.getCapacidadVolumetricaCompartimento());
			listaParametros.addValue("AlturaCompartimento", eDetalleTransporte.getAlturaCompartimento());
			listaParametros.addValue("IdTransporte", eDetalleTransporte.getIdTransporte());
			listaParametros.addValue("UnidadMedida", eDetalleTransporte.getUnidadMedida());
			listaParametros.addValue("NumeroCompartimento", eDetalleTransporte.getNumeroCompartimento());
			listaParametros.addValue("VolumenTemperaturaObservada", eDetalleTransporte.getVolumenTemperaturaObservada());
			listaParametros.addValue("VolumenTemperaturaBase", eDetalleTransporte.getVolumenTemperaturaBase());
			listaParametros.addValue("ApiTemperaturaBase", eDetalleTransporte.getApiTemperaturaBase());
			listaParametros.addValue("FactorCorrecion", eDetalleTransporte.getFactorCorrecion());
			listaParametros.addValue("TemperaturaObservada", eDetalleTransporte.getTemperaturaObservada());
			
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
	
	public RespuestaCompuesta actualizarRegistro(DetalleTransporte eDetalleTransporte){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("id_producto=:IdProducto,");
			consultaSQL.append("capacidad_volumetrica_compartimento=:CapacidadVolumetricaCompartimento,");
			//consultaSQL.append("altura_compartimento	=:AlturaCompartimento,");
			consultaSQL.append("id_transporte			=:IdTransporte,");
			consultaSQL.append("unidad_medida_volumen	=:UnidadMedida,");
			consultaSQL.append("numero_compartimento	=:NumeroCompartimento,");
			consultaSQL.append("volumen_temperatura_observada=:VolumenTemperaturaObservada,");
			consultaSQL.append("volumen_temperatura_base=:VolumenTemperaturaBase,");
			consultaSQL.append("api_temperatura_base	=:ApiTemperaturaBase,");
			consultaSQL.append("factor_correcion		=:FactorCorrecion,");
			consultaSQL.append("temperatura_observada	=:TemperaturaObservada,");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();

			listaParametros.addValue("IdProducto", eDetalleTransporte.getIdProducto());
			listaParametros.addValue("CapacidadVolumetricaCompartimento", eDetalleTransporte.getCapacidadVolumetricaCompartimento());
			//listaParametros.addValue("AlturaCompartimento", eDetalleTransporte.getAlturaCompartimento());
			listaParametros.addValue("IdTransporte", eDetalleTransporte.getIdTransporte());
			listaParametros.addValue("UnidadMedida", eDetalleTransporte.getUnidadMedida());
			listaParametros.addValue("NumeroCompartimento", eDetalleTransporte.getNumeroCompartimento());
			listaParametros.addValue("VolumenTemperaturaObservada", eDetalleTransporte.getVolumenTemperaturaObservada());
			listaParametros.addValue("VolumenTemperaturaBase", eDetalleTransporte.getVolumenTemperaturaBase());
			listaParametros.addValue("ApiTemperaturaBase", eDetalleTransporte.getApiTemperaturaBase());
			listaParametros.addValue("FactorCorrecion", eDetalleTransporte.getFactorCorrecion());
			listaParametros.addValue("TemperaturaObservada", eDetalleTransporte.getTemperaturaObservada());
			listaParametros.addValue("Id", eDetalleTransporte.getId());
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
	
	/**
     * Metodo para cuenta todos los detalles de un transporte.
     * @param idTransporte      Identificador del transporte.
     * @return respuesta		Número de detalles de transporte que existen de un transporte.
     */
	public Respuesta numeroRegistrosPorTransporte(int idTransporte){		
		Respuesta respuesta = new Respuesta();
		StringBuilder consultaSQL= new StringBuilder();	
		int cantidadRegistros = 0;

		try {
			consultaSQL=new StringBuilder();
			consultaSQL.append("SELECT ");
			consultaSQL.append(" count(*) ");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(" sgo.detalle_transporte ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(" id_transporte = ");
			consultaSQL.append( idTransporte );
			
			cantidadRegistros = jdbcTemplate.queryForInt(consultaSQL.toString());
			respuesta.valor = String.valueOf(cantidadRegistros);
			respuesta.estado = true;
			
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
		} catch (Exception excepcionGenerica) {
			excepcionGenerica.printStackTrace();
			respuesta.error= Constante.EXCEPCION_GENERICA;
			respuesta.estado = false;
		}
		
		return respuesta;
	}
	
	/**
     * Metodo para eliminar todos los detalle de trasnportes de un transporte.
     * @param idTransporte  	Identificador del transporte.
     * @return respuesta		Contiene el valor de los registros eliminados.
     */
	public Respuesta eliminarRegistrosPorTransporte(int idTransporte){		
		Respuesta respuesta= new Respuesta();
		String consultaSQL="";
		Object[] parametros = {idTransporte};
		try {
			consultaSQL="DELETE FROM " + NOMBRE_TABLA + " WHERE id_transporte = ?";
			int registrosEliminados = jdbcTemplate.update(consultaSQL, parametros);
			if (!Utilidades.esValido(registrosEliminados)){
				respuesta.estado=true;
				respuesta.valor=null;
			} else {
				respuesta.estado = true;
				respuesta.valor= String.valueOf(registrosEliminados);
			}
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error=  Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
		} catch (Exception excepcionGenerica) {
			excepcionGenerica.printStackTrace();
			respuesta.error= Constante.EXCEPCION_GENERICA;
			respuesta.estado = false;
		}
		return respuesta;
	}
	
//Agregado por req 9000002608==================================================
	public RespuestaCompuesta recuperarTransYdetalleTrans(int idCisterna, int idTracto, int numCompartimento, int id_programacion){
		StringBuilder consultaSQL= new StringBuilder();   
	     List<ClonacionProgTrans> listaRegistros=new ArrayList<ClonacionProgTrans>();
	     Contenido<ClonacionProgTrans> contenido = new Contenido<ClonacionProgTrans>();
	     RespuestaCompuesta respuesta= new RespuestaCompuesta();
	     try {
	       consultaSQL.append(" SELECT ");
	       consultaSQL.append("	 t1.id_transporte, ");
	       consultaSQL.append("	 t2.id_dtransporte	");
	       consultaSQL.append(" FROM sgo.transporte t1	");
	       consultaSQL.append(" LEFT JOIN sgo.detalle_transporte t2 ON (t1.id_transporte = t2.id_transporte AND t2.numero_compartimento = " + numCompartimento+ ")	");
	       consultaSQL.append(" WHERE id_cisterna = " + idCisterna + " AND id_tracto = " + idTracto + "	AND programacion = " + id_programacion + " ");
	       consultaSQL.append("         AND t1.id_transporte = (	");
	       consultaSQL.append("	                SELECT MAX(t1.id_transporte)	");
	       consultaSQL.append("					FROM sgo.transporte t1	");
	       consultaSQL.append("					LEFT JOIN sgo.detalle_transporte t2 ON (t1.id_transporte = t2.id_transporte AND t2.numero_compartimento = " + numCompartimento+ ")	");
	       consultaSQL.append("					WHERE t1.id_cisterna = " + idCisterna + " AND t1.id_tracto = " + idTracto + "	AND programacion = " + id_programacion + " ");
	       consultaSQL.append(")	");
	       listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new ClonacionProgTransMapper());
	       contenido.totalRegistros=listaRegistros.size();
	       contenido.totalEncontrados=listaRegistros.size();
	       contenido.carga= listaRegistros;
	       respuesta.mensaje="OK";
	       respuesta.estado=true;
	       respuesta.contenido = contenido;    
	       Utilidades.gestionaInfo("clonarProgTrans: DetalleTransporteDao: recuperarTransYdetalleTrans ", "listaMaestros.getIdDiaOperativo() ", consultaSQL.toString());
	       System.out.println("clonarProgTrans: DetalleTransporteDao: recuperarTransporteProgramado: listaMaestros.getIdDiaOperativo() " + consultaSQL.toString());
	     } catch (DataAccessException excepcionAccesoDatos) {
	       excepcionAccesoDatos.printStackTrace();
	       respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
	       respuesta.estado=false;
	       respuesta.contenido=null;
	     }
	     return respuesta;
		}
	

	
}