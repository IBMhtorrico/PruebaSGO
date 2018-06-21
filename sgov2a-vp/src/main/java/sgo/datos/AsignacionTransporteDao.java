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

import sgo.entidad.AsignacionTransporte;
import sgo.entidad.Cisterna;
import sgo.entidad.Contenido;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Producto;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Transporte;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Repository
public class AsignacionTransporteDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION	+ "v_asignacion_transporte";
	public final static String NOMBRE_CAMPO_CLAVE = "id_asignacion";
	public final static String NOMBRE_CAMPO_CLAVE_DOPERATIVO = "id_doperativo";
	public final static String NOMBRE_CAMPO_CLAVE_TRANSPORTE = "id_transporte";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "id_doperativo";

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
		String campoOrdenamiento = "nombre_producto";
		try {
			if (propiedad.equals("descripcionProducto")) {
				campoOrdenamiento = "nombre_producto";
			}
			if (propiedad.equals("volumenSolicitado")) {
				campoOrdenamiento = "volumen_solicitado";
			}
			if (propiedad.equals("cisternasSolicitadas")) {
				campoOrdenamiento = "cisternas_solicitadas";
			}
			if (propiedad.equals("volumenAsignado")) {
				campoOrdenamiento = "volumen_asignado";
			}
			if (propiedad.equals("cisternasAsignadas")) {
				campoOrdenamiento = "cisternas_asignadas";
			}
		} catch (Exception excepcion) {

		}
		return campoOrdenamiento;
	}
	
	public RespuestaCompuesta recuperarRegistrosPorDiaOperativo(ParametrosListar argumentosListar) {
		String sqlLimit = "";
		String sqlOrderBy = "";
		List<String> filtrosWhere = new ArrayList<String>();
		String sqlWhere = "";
		int totalRegistros = 0, totalEncontrados = 0;
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		Contenido<AsignacionTransporte> contenido = new Contenido<AsignacionTransporte>();
		List<AsignacionTransporte> listaRegistros = new ArrayList<AsignacionTransporte>();
		List<Object> parametros = new ArrayList<Object>();
		try {
			//Comentado por req 9000002608=============================================================
//			filtrosWhere.add(" pro.indicador_producto <> " + Producto.INDICADOR_PRODUCTO_SIN_DATOS);
			//=========================================================================================
			if (argumentosListar.getFiltroDiaOperativo() >= 0) {
				//Comentado por req 9000002608==================
//				filtrosWhere.add(" dpt.id_doperativo = ?");
				//==============================================
				//Agregado por req 9000002608
				filtrosWhere.add(" t1.id_doperativo = ?");
				//==============================================
				parametros.add(argumentosListar.getFiltroDiaOperativo());
		    }
			
			if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				parametros.add(argumentosListar.getInicioPaginacion());
				parametros.add(argumentosListar.getRegistrosxPagina());
			}

			sqlOrderBy= " ORDER BY " + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();

			StringBuilder consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			/*consultaSQL.append("SELECT count(DISTINCT(dpt.nombre_producto)) as total FROM " + NOMBRE_VISTA);
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;*/

			

			if (!filtrosWhere.isEmpty()) {
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere,Constante.SQL_Y);
				//consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE + ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				//totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

//			Comentado por req 9000002608
//		   consultaSQL.setLength(0);
//		   consultaSQL.append("SELECT ");
//		   consultaSQL.append("DISTINCT(dpt.nombre_producto),  ");
//		   consultaSQL.append("dpt.volumen_solicitado,  ");
//		   consultaSQL.append(" dpt.cantidad_cisternas, ");
//		   consultaSQL.append("ast.volumen_observado, ");
//		   consultaSQL.append("ast.total_cisternas ");
//		   consultaSQL.append(" FROM sgo.v_dia_planificado_transporte dpt ");				
//		   consultaSQL.append(" LEFT OUTER JOIN  sgo.v_asignacion_transporte_producto ast ");
//		   consultaSQL.append(" ON  dpt.id_doperativo = ast.id_doperativo ");
//		   consultaSQL.append(" AND dpt.id_producto = ast.id_producto ");
//		   consultaSQL.append(" INNER JOIN sgo.producto pro ON dpt.id_producto = pro.id_producto ");
//		   consultaSQL.append(sqlWhere);
//		   consultaSQL.append(sqlOrderBy);
//		   consultaSQL.append(sqlLimit);
//========================================================================================
			
			//Agregado por req 9000002608=============================================================
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT  																						");
			consultaSQL.append("	COALESCE(t3.volumen_solicitado,0) volumen_solicitado,																			");
			consultaSQL.append("	t1.cantidad_cisternas_planificadas cantidad_cisternas,														");
			consultaSQL.append("	COALESCE(t2.vol_asig,0) volumen_observado, 																					");
			consultaSQL.append("	COALESCE(t2.cist_asig,0) total_cisternas																					");
			consultaSQL.append(" FROM   sgo.dia_operativo t1 																					");
			consultaSQL.append(" LEFT JOIN (SELECT id_doperativo,estado,SUM(volumen_total_observado) vol_asig, COUNT(id_doperativo) cist_asig	");
			consultaSQL.append("		FROM sgo.v_dia_operativo_base_calculo_asignacion_relacion WHERE estado <> " + Constante.TRANSPORTE_ESTADO_PROGRAMADO  );
			consultaSQL.append("		GROUP BY id_doperativo,estado) t2 on (t2.id_doperativo = t1.id_doperativo)								");
			consultaSQL.append(" LEFT JOIN (SELECT id_doperativo,SUM(volumen_solicitado) volumen_solicitado FROM sgo.planificacion 				");
			consultaSQL.append("		GROUP BY id_doperativo)  t3 on (t3.id_doperativo = t1.id_doperativo)									");
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlLimit);
			//========================================================================================
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),	parametros.toArray(), new AsignacionTransporteMapper());

			contenido.carga = listaRegistros;
			respuesta.estado = true;
			respuesta.contenido = contenido;
			respuesta.contenido.totalRegistros = listaRegistros.size();
			respuesta.contenido.totalEncontrados = listaRegistros.size();
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

	
	
	/*public RespuestaCompuesta recuperarRegistrosPorDiaOperativo(ParametrosListar argumentosListar) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AsignacionTransporte> listaRegistros = new ArrayList<AsignacionTransporte>();
		Contenido<AsignacionTransporte> contenido = new Contenido<AsignacionTransporte>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idDoperativo)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
		  StringBuilder consultaSQL = new StringBuilder();
		  String sqlLimit = "";
		  List<AsignacionTransporte> listaRegistros = new ArrayList<AsignacionTransporte>();
		  Contenido<AsignacionTransporte> contenido = new Contenido<AsignacionTransporte>();
		  RespuestaCompuesta respuesta = new RespuestaCompuesta();
		  List<String> filtrosWhere = new ArrayList<String>();
		  String sqlOrderBy = "";
		  String sqlWhere = "";
		  List<Object> valores = new ArrayList<Object>();
		  int totalRegistros = 0, totalEncontrados = 0;
		  try {
			  
			 if (argumentosListar.getPaginacion() == Constante.CON_PAGINACION) {
				sqlLimit = Constante.SQL_LIMIT_CONFIGURADO;
				valores.add(argumentosListar.getInicioPaginacion());
				valores.add(argumentosListar.getRegistrosxPagina());
			}
			  
		   sqlOrderBy = Constante.SQL_ORDEN  + this.mapearCampoOrdenamiento(argumentosListar.getCampoOrdenamiento()) + " " + argumentosListar.getSentidoOrdenamiento();
		   
		    consultaSQL = new StringBuilder();
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT count(id_doperativo) as total FROM sgo.v_dia_planificado_transporte ");
			totalRegistros = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			totalEncontrados = totalRegistros;
			
		   if (argumentosListar.getFiltroDiaOperativo() > 0) {
			 filtrosWhere.add(" dpt.id_doperativo = ?");
		     valores.add(argumentosListar.getFiltroDiaOperativo());
		   }
		
		   if (!filtrosWhere.isEmpty()) {
		    consultaSQL.setLength(0);
		    sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Constante.SQL_Y);
		   }	

		   consultaSQL.setLength(0);
		   consultaSQL.append("SELECT ");
		   consultaSQL.append("DISTINCT(dpt.nombre_producto),  ");
		   consultaSQL.append("dpt.volumen_solicitado,  ");
		   consultaSQL.append(" dpt.cantidad_cisternas, ");
		   consultaSQL.append("ast.volumen_observado, ");
		   consultaSQL.append("ast.total_cisternas ");
		   consultaSQL.append(" FROM sgo.v_dia_planificado_transporte dpt ");				
		   consultaSQL.append(" LEFT OUTER JOIN  sgo.v_asignacion_transporte_producto ast ");
		   consultaSQL.append(" ON  dpt.id_doperativo = ast.id_doperativo ");
		   consultaSQL.append(" AND dpt.id_producto = ast.id_producto ");
		   consultaSQL.append(sqlWhere);
		   consultaSQL.append(sqlOrderBy);
		   consultaSQL.append(sqlLimit);
			//consultaSQL.append(" WHERE dpt.id_doperativo = ?");

			//listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idDoperativo}, new AsignacionTransporteMapper());
		   listaRegistros = jdbcTemplate.query(consultaSQL.toString(), valores.toArray(),new AsignacionTransporteMapper());

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
	}*/

	/*public RespuestaCompuesta recuperarRegistrosPorDiaOperativo(int idDoperativo) {
		StringBuilder consultaSQL = new StringBuilder();
		List<AsignacionTransporte> listaRegistros = new ArrayList<AsignacionTransporte>();
		Contenido<AsignacionTransporte> contenido = new Contenido<AsignacionTransporte>();
		RespuestaCompuesta respuesta = new RespuestaCompuesta();

		if(!Utilidades.esValido(idDoperativo)){
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado = false;
			respuesta.contenido = null;
			return respuesta;
		}
		try{
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("DISTINCT(dpt.nombre_producto),  ");
			consultaSQL.append("dpt.volumen_solicitado,  ");
			consultaSQL.append(" dpt.cantidad_cisternas, ");
			consultaSQL.append("ast.volumen_observado, ");
			consultaSQL.append("ast.total_cisternas ");
			consultaSQL.append(" FROM sgo.v_dia_planificado_transporte dpt ");				
			consultaSQL.append(" LEFT OUTER JOIN  sgo.v_asignacion_transporte_producto ast ");
			consultaSQL.append(" ON  dpt.id_doperativo = ast.id_doperativo ");
			consultaSQL.append(" AND dpt.id_producto = ast.id_producto ");
			consultaSQL.append(" WHERE dpt.id_doperativo = ?");

			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),new Object[] {idDoperativo}, new AsignacionTransporteMapper());

			contenido.totalRegistros = listaRegistros.size();
			contenido.totalEncontrados = listaRegistros.size();
			contenido.carga = listaRegistros;
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
	}*/

}