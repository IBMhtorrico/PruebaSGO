package sgo.datos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class BitacoraDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public static final String NOMBRE_TABLA = Constante.ESQUEMA_SEGURIDAD + "bitacora";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_SEGURIDAD + "v_bitacora";
	public final static String NOMBRE_CAMPO_CLAVE = "id_bitacora";
	
	public final static String NOMBRE_CAMPO_FILTRO = "accion";
	public final static String NOMBRE_CAMPO_FILTRO_FECHA = "realizado_el";	
	public final static String NOMBRE_CAMPO_FILTRO_TABLA = "tabla";
	public final static String NOMBRE_CAMPO_FILTRO_USUARIO = "usuario";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "usuario";
	public final static String O = " OR ";
	public final static String Y = " AND ";
	public final static String ENTRE = " BETWEEN ";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}
	
	public String mapearCampoOrdenamiento(String propiedad){
		String campoOrdenamiento = NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
		try {
			if (propiedad.equals("tabla")){
				campoOrdenamiento="tabla";
			}
			if (propiedad.equals("usuario")){
				campoOrdenamiento="usuario";
			}
			if (propiedad.equals("fechaRealizacion")){
				campoOrdenamiento="realizado_el";
			}
			if (propiedad.equals("accion")){
				campoOrdenamiento="accion";
			}
		}catch(Exception excepcion){
			
		}
		return campoOrdenamiento;
	}

	public RespuestaCompuesta recuperarRegistros(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy="";
		List<String> filtrosWhere= new ArrayList<String>();
		String sqlWhere="";
		int totalRegistros = 0, 
		totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Bitacora> contenido = new Contenido<Bitacora>();
		List<Bitacora> listaRegistros = new ArrayList<Bitacora>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " "  + argumentosListar.getSentidoOrdenamiento();
			
			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados=totalRegistros;

			if(!argumentosListar.getFiltroUsuario().equals(Constante.FILTRO_STRING_TODOS)){
				filtrosWhere.add(" lower(t1."+ NOMBRE_CAMPO_FILTRO_USUARIO + ") = lower('" + argumentosListar.getFiltroUsuario() + "') ");
			}
			
			if(!argumentosListar.getFiltroTabla().equals(Constante.FILTRO_STRING_TODOS)){
				filtrosWhere.add(" lower(t1."+ NOMBRE_CAMPO_FILTRO_TABLA + ") = lower('" + argumentosListar.getFiltroTabla() + "') ");
			}

			//Esto para el filtro de fechas
			if(!argumentosListar.getFiltroFechaInicio().isEmpty() && !argumentosListar.getFiltroFechaFinal().isEmpty()){
				filtrosWhere.add(" t1."+ NOMBRE_CAMPO_FILTRO_FECHA + " " + ENTRE + " " +  (this.formatearStringToLong(argumentosListar.getFiltroFechaInicio().trim())) + Y +  (this.formatearStringToLong(argumentosListar.getFiltroFechaFinal().trim())));
			}
			else {
				if (!argumentosListar.getFiltroFechaInicio().isEmpty()) {
					filtrosWhere.add(" t1."+ NOMBRE_CAMPO_FILTRO_FECHA + " >= " + (this.formatearStringToLong(argumentosListar.getFiltroFechaInicio().trim())));
				}
				if (!argumentosListar.getFiltroFechaFinal().isEmpty()) {
					filtrosWhere.add(" t1."+ NOMBRE_CAMPO_FILTRO_FECHA + " <= " +  (this.formatearStringToLong(argumentosListar.getFiltroFechaFinal().trim())));
				}
			}
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_bitacora,");
			consultaSQL.append("t1.usuario,");
			consultaSQL.append("t1.accion,");
			consultaSQL.append("t1.tabla,");
			consultaSQL.append("t1.contenido,");
			consultaSQL.append("t1.realizado_por,");
			consultaSQL.append("t1.realizado_el,");
			consultaSQL.append("t1.identificador");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new BitacoraMapper());
			
			//totalEncontrados =listaRegistros.size();
			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalEncontrados;
		} catch (DataAccessException daEx) {
			daEx.printStackTrace();
			respuesta.error=  Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.estado = false;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta recuperarRegistro(int ID){
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			if(!Utilidades.esValido(ID)){
				respuesta.estado=false;
				respuesta.contenido=null;
				return respuesta;
			}
			StringBuilder consultaSQL= new StringBuilder();		
			List<Bitacora> listaRegistros=new ArrayList<Bitacora>();
			Contenido<Bitacora> contenido = new Contenido<Bitacora>();
			
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_bitacora,");
				consultaSQL.append("t1.usuario,");
				consultaSQL.append("t1.accion,");
				consultaSQL.append("t1.tabla,");
				consultaSQL.append("t1.contenido,");
				consultaSQL.append("t1.realizado_por,");
				consultaSQL.append("t1.realizado_el,");
				consultaSQL.append("t1.identificador");
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new BitacoraMapper());
				contenido.totalRegistros=listaRegistros.size();
				contenido.totalEncontrados=listaRegistros.size();
				contenido.carga= listaRegistros;
				respuesta.mensaje="OK";
				respuesta.estado=true;
				respuesta.contenido = contenido;			
			} catch (DataAccessException daEx) {
				respuesta.mensaje=daEx.getMessage();
				respuesta.estado=false;
				respuesta.contenido=null;
			}
			return respuesta;
		}	
	
	public RespuestaCompuesta guardarRegistro(Bitacora bitacora){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder sbSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int rowsAffected=0;
		try {
			sbSQL.append("INSERT INTO ");
			sbSQL.append(NOMBRE_TABLA);
			sbSQL.append(" (usuario,identificador,accion,tabla,realizado_el,realizado_por,contenido) ");
			sbSQL.append(" VALUES (:Usuario,:Identificador,:Accion,:Tabla,:RealizadoEl,:RealizadoPor,:Contenido) ");
			MapSqlParameterSource mapParameters= new MapSqlParameterSource();
			mapParameters.addValue("Usuario", bitacora.getUsuario());
			mapParameters.addValue("Accion", bitacora.getAccion());
			mapParameters.addValue("Tabla", bitacora.getTabla());
			mapParameters.addValue("Identificador", bitacora.getIdentificador());
			mapParameters.addValue("RealizadoEl", bitacora.getRealizadoEl());
			mapParameters.addValue("RealizadoPor", bitacora.getRealizadoPor());
			mapParameters.addValue("Contenido", bitacora.getContenido());
			
			SqlParameterSource namedParameters= mapParameters;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			rowsAffected= namedJdbcTemplate.update(sbSQL.toString(),namedParameters,claveGenerada,new String[] {NOMBRE_CAMPO_CLAVE});		
			if (rowsAffected>1){
				respuesta.mensaje="Mas filas afectadas";
				respuesta.estado=false;
				return respuesta;
			}
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.valor= claveGenerada.getKey().toString();

		} catch (DataIntegrityViolationException daEx){
			daEx.printStackTrace();
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			daEx.printStackTrace();
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		}
		return respuesta;
	}	
	
	public RespuestaCompuesta recuperarNombreTablas(){
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();		
		List<String> listaRegistros=new ArrayList<String>();
		Contenido<String> contenido = new Contenido<String>();
		try {
			consultaSQL.append("Select tablename from pg_tables where schemaname='sgo'  ");
			listaRegistros= jdbcTemplate.queryForList(consultaSQL.toString(), null, String.class);
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;			
		} catch (DataAccessException daEx) {
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}	
	
	public long formatearStringToLong(String strFecha){
		long retorno = new Long(0);
		try{
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = formatter.parse(strFecha);
		    retorno = date.getTime();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retorno;
	}
	
}