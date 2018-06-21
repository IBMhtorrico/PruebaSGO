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
import sgo.entidad.Enlace;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planta;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.utilidades.Constante;

@Repository
public class EnlaceDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION	+ "enlace";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_enlace";
	public final static String NOMBRE_CAMPO_CLAVE = "id_enlace";
	public final static String NOMBRE_CAMPO_FILTRO = "url_completa";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "url_completa";
	public final static String DATOS_AUDITORIA = "t1.creado_el, t1.creado_por, t1.actualizado_por, t1.actualizado_el, t1.usuario_creacion, t1.usuario_actualizacion";

	public final static String O = " OR ";
	public final static String Y = " AND ";
	public final static String ENTRE = " BETWEEN ";

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
			if (propiedad.equals("id")) {
				campoOrdenamiento = "id_enlace";
			}
			if (propiedad.equals("urlCompleta")) {
				campoOrdenamiento = "url_completa";
			}
			if (propiedad.equals("urlRelativa")) {
				campoOrdenamiento = "url_relativa";
			}
			if (propiedad.equals("orden")) {
				campoOrdenamiento = "orden";
			}
			if (propiedad.equals("padre")) {
				campoOrdenamiento = "padre";
			}
			if (propiedad.equals("tipo")) {
				campoOrdenamiento = "tipo";
			}
			if (propiedad.equals("permiso.nombre")) {
				campoOrdenamiento = "desc_permiso";
			}
			// Campos de auditoria
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
		Contenido<Enlace> contenido = new Contenido<Enlace>();
		List<Enlace> listaRegistros = new ArrayList<Enlace>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(" + NOMBRE_CAMPO_CLAVE	+ ") as total FROM " + NOMBRE_TABLA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;
			
			if (!argumentosListar.getValorBuscado().isEmpty()) {
				filtrosWhere.add("lower(t1." + NOMBRE_CAMPO_FILTRO + ") like lower('%" + argumentosListar.getValorBuscado() + "%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
			
			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_enlace,");
			consultaSQL.append("t1.titulo,");
			consultaSQL.append("t1.url_completa,");
			consultaSQL.append("t1.url_relativa,");
			consultaSQL.append("t1.orden,");
			consultaSQL.append("t1.padre,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.id_permiso,");
			consultaSQL.append("t1.desc_permiso,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new EnlaceMapper());
			totalEncontrados =totalRegistros;
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
	
	
	public RespuestaCompuesta recuperarMenu(int tipoEnlace,int idRol) {
		int totalRegistros = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<Enlace> contenido = null;
		List<Enlace> listaRegistros = null;
		List<Object> parametros = null;
		StringBuilder consultaSQL =null;
		String vistaMenu="sgo.v_enlace_menu";
		
		try {			
			contenido = new Contenido<Enlace>();
			listaRegistros = new ArrayList<Enlace>();
			parametros = new ArrayList<Object>();
			consultaSQL = new StringBuilder();
			consultaSQL.append("SELECT ");			
			consultaSQL.append("t1.id_enlace,");
			consultaSQL.append("t1.titulo,");
			consultaSQL.append("t1.url_completa,");
			consultaSQL.append("t1.url_relativa,");
			consultaSQL.append("t1.orden,");
			consultaSQL.append("t1.padre,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.id_permiso,");
			consultaSQL.append("t1.desc_permiso,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			//Campos de auditoria
			consultaSQL.append(" FROM ");
			consultaSQL.append(vistaMenu);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE tipo=? and id_rol =?");
			consultaSQL.append(" ORDER BY t1.orden asc ");
			/*if(tipoEnlace == 1){
				consultaSQL.append(" ORDER BY t1.id_enlace asc ");
			}*/
			
			//	consultaSQL.append(" ORDER BY t1.t ");
			parametros.add(tipoEnlace);
			parametros.add(idRol);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new EnlaceMapper());	
			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.mensaje = "OK";
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = totalRegistros;
			respuesta.contenido.totalEncontrados = totalRegistros;
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
			StringBuilder consultaSQL= new StringBuilder();		
			List<Enlace> listaRegistros=new ArrayList<Enlace>();
			Contenido<Enlace> contenido = new Contenido<Enlace>();
			RespuestaCompuesta respuesta= new RespuestaCompuesta();
			try {
				consultaSQL.append("SELECT ");
				consultaSQL.append("t1.id_enlace,");
				consultaSQL.append("t1.titulo,");
				consultaSQL.append("t1.url_completa,");
				consultaSQL.append("t1.url_relativa,");
				consultaSQL.append("t1.orden,");
				consultaSQL.append("t1.padre,");
				consultaSQL.append("t1.tipo,");
				consultaSQL.append("t1.id_permiso,");
				consultaSQL.append("t1.desc_permiso,");
				//Campos de auditoria
				consultaSQL.append("t1.creado_el,");
				consultaSQL.append("t1.creado_por,");
				consultaSQL.append("t1.actualizado_por,");
				consultaSQL.append("t1.actualizado_el,");
				consultaSQL.append("t1.usuario_creacion,");
				consultaSQL.append("t1.usuario_actualizacion");
				consultaSQL.append(" FROM ");				
				consultaSQL.append(NOMBRE_VISTA);
				consultaSQL.append(" t1 ");
				consultaSQL.append(" WHERE ");
				consultaSQL.append(NOMBRE_CAMPO_CLAVE);
				consultaSQL.append("=?");
				listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new EnlaceMapper());
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
	
	public RespuestaCompuesta recuperarRegistro(String  urlCompleta){
		StringBuilder consultaSQL= new StringBuilder();		
		List<Enlace> listaRegistros=new ArrayList<Enlace>();
		Contenido<Enlace> contenido = new Contenido<Enlace>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_enlace,");
			consultaSQL.append("t1.titulo,");
			consultaSQL.append("t1.url_completa,");
			consultaSQL.append("t1.url_relativa,");
			consultaSQL.append("t1.orden,");
			consultaSQL.append("t1.padre,");
			consultaSQL.append("t1.tipo,");
			consultaSQL.append("t1.id_permiso,");
			consultaSQL.append("t1.desc_permiso,");
			//Campos de auditoria
			consultaSQL.append("t1.creado_el,");
			consultaSQL.append("t1.creado_por,");
			consultaSQL.append("t1.actualizado_por,");
			consultaSQL.append("t1.actualizado_el,");
			consultaSQL.append("t1.usuario_creacion,");
			consultaSQL.append("t1.usuario_actualizacion");
			consultaSQL.append(" FROM ");				
			consultaSQL.append(NOMBRE_VISTA);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE url_completa =?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {urlCompleta},new EnlaceMapper());
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.estado=true;
			respuesta.contenido = contenido;	
			
			//si no encuentra ni un enlace ponemos estado false
			if(respuesta.contenido.carga.size() == 0 ){
				respuesta.estado=false;
				respuesta.contenido=null;
			}
			
		} catch (DataAccessException daEx) {
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}
	
	public RespuestaCompuesta guardarRegistro(Enlace enlace){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" (titulo, url_completa, url_relativa, orden, padre, tipo, id_permiso, creado_el, creado_por, actualizado_por, actualizado_el, ip_creacion, ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Titulo, :UrlCompleta, :UrlRelativa, :Orden, :Padre, :Tipo, :Permiso, :CreadoEl, :CreadoPor, :ActualizadoPor, :ActualizadoEl, :IpCreacion, :IpActualizacion) ");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("Titulo", enlace.getTitulo());
			listaParametros.addValue("UrlCompleta", enlace.getUrlCompleta());
			listaParametros.addValue("UrlRelativa", enlace.getUrlRelativa());
			listaParametros.addValue("Orden", enlace.getOrden());
			listaParametros.addValue("Padre", enlace.getPadre());
			listaParametros.addValue("Tipo", enlace.getTipo());
			listaParametros.addValue("Permiso", enlace.getPermiso());
			listaParametros.addValue("CreadoEl", enlace.getCreadoEl());
			listaParametros.addValue("CreadoPor", enlace.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", enlace.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", enlace.getActualizadoEl());
			listaParametros.addValue("IpCreacion", enlace.getIpCreacion());
			listaParametros.addValue("IpActualizacion", enlace.getIpActualizacion());

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
	
	
	public RespuestaCompuesta actualizarRegistro(Enlace enlace){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
	//	int rowsAffected=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("titulo=:Titulo, ");
			consultaSQL.append("url_completa=:UrlCompleta, ");
			consultaSQL.append("url_relativa=:UrlRelativa, ");
			consultaSQL.append("orden=:Orden, ");
			consultaSQL.append("padre=:Padre, ");
			consultaSQL.append("tipo=:Tipo, ");
			consultaSQL.append("id_permiso=:Permiso, ");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			//MapSqlParameterSource mapParameters= new MapSqlParameterSource();
			listaParametros.addValue("Titulo", enlace.getTitulo());
			listaParametros.addValue("UrlCompleta", enlace.getUrlCompleta());
			listaParametros.addValue("UrlRelativa", enlace.getUrlRelativa());
			listaParametros.addValue("Orden", enlace.getOrden());
			listaParametros.addValue("Padre", enlace.getPadre());
			listaParametros.addValue("Tipo", enlace.getTipo());
			listaParametros.addValue("Permiso", enlace.getPermiso());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoPor", enlace.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", enlace.getActualizadoEl());
			listaParametros.addValue("id", enlace.getId());
			
			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(), namedParameters);
			if (cantidadFilasAfectadas>1){
				//respuesta.mensaje="Mas filas afectadas";
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
		
		/*} catch (DataIntegrityViolationException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.mensaje=daEx.getMessage();
			respuesta.estado=false;
		}
		return respuesta;*/
	}
	
	public RespuestaCompuesta eliminarRegistro(int idRegistro){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int rowsAffected=0;	
		String sql="";
		Object[] params = {idRegistro};
		try {
			sql="DELETE FROM " + NOMBRE_TABLA + " WHERE " + NOMBRE_CAMPO_CLAVE + "=?";
        	rowsAffected = jdbcTemplate.update(sql, params);
			if (rowsAffected==1){
				respuesta.estado=true;
			} else {
				respuesta.estado=false;
			}
		} catch (DataIntegrityViolationException daEx){	
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		} catch (DataAccessException daEx){
			respuesta.error= Constante.EXCEPCION_INTEGRIDAD_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
}
