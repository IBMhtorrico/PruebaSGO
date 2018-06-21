package sgo.datos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import sgo.entidad.Cliente;
import sgo.entidad.Contenido;
import sgo.entidad.Operacion;
import sgo.entidad.OperacionEtapaRuta;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Transportista;
import sgo.entidad.TransportistaOperacion;
import sgo.utilidades.Constante;

/**
 * Funcionalidades para el modulo de Operacion.
 *
 * @author I.B.M. DEL PERÚ 
 * @since  XIII/2015
 */
@Repository
public class OperacionDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public static final String NOMBRE_TABLA = Constante.ESQUEMA_APLICACION + "operacion";
	public static final String NOMBRE_VISTA = Constante.ESQUEMA_APLICACION + "v_operacion";
	public static final String NOMBRE_VISTA_TRANSPORTISTA_OPERACION = Constante.ESQUEMA_APLICACION + "v_transportista_operacion";
	public final static String NOMBRE_CAMPO_CLAVE = "id_operacion";
	public final static String NOMBRE_CAMPO_FILTRO = "nombre";
	public final static String NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO = "nombre";
	
	public final static String NOMBRE_CAMPO_FILTRO_ESTADO = "estado";
	public final static String O = "OR";
	public final static String Y = "AND";
	public final static String ENTRE = "BETWEEN";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public DataSource getDataSource(){
		return this.jdbcTemplate.getDataSource();
	}
	
	public String mapearCampoOrdenamiento(String propiedad){
		String campoOrdenamiento=NOMBRE_CAMPO_ORDENAMIENTO_DEFECTO;
		try {
			if (propiedad.equals("id")){
				campoOrdenamiento="id_operacion";
			}
			if (propiedad.equals("nombre")){
				campoOrdenamiento="nombre";
			}
			if (propiedad.equals("cliente.razonSocial")){
				campoOrdenamiento="razon_social_cliente";
			}
			if (propiedad.equals("referenciaPlantaRecepcion")){
				campoOrdenamiento="referencia_planta_recepcion";
			}
			if (propiedad.equals("referenciaDestinatarioMercaderia")){
				campoOrdenamiento="referencia_destinatario_mercaderia";
			}
			if (propiedad.equals("estado")){
				campoOrdenamiento="estado";
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
		Contenido<Operacion> contenido = new Contenido<Operacion>();
		List<Operacion> listaRegistros = new ArrayList<Operacion>();
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

			
			if (!argumentosListar.getValorBuscado().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getValorBuscado() +"%') ");
			}
			if (!argumentosListar.getTxtFiltro().isEmpty()){
				filtrosWhere.add("lower(t1."+NOMBRE_CAMPO_FILTRO+") like lower('%"+ argumentosListar.getTxtFiltro() +"%') ");
			}
			if(argumentosListar.getFiltroEstado() != Constante.FILTRO_TODOS){
				filtrosWhere.add(" t1."+NOMBRE_CAMPO_FILTRO_ESTADO + "=" + argumentosListar.getFiltroEstado());
			}
			if(argumentosListar.getIdCliente() > 0)     {
				filtrosWhere.add(" t1.id_cliente =" + argumentosListar.getIdCliente());
			}
			if(argumentosListar.getIdOperacion() > 0)     {
				filtrosWhere.add(" t1.id_operacion =" + argumentosListar.getIdOperacion());
			}
	     if(argumentosListar.getFiltroCentroCliente().length()>2)     {
        filtrosWhere.add(" t1.referencia_planta_recepcion ='" + argumentosListar.getFiltroCentroCliente()+"'");
      }
       if(argumentosListar.getFiltroDestinarioMercaderia().length()>2)     {
        filtrosWhere.add(" t1.referencia_destinatario_mercaderia ='" + argumentosListar.getFiltroDestinarioMercaderia()+"'");
      }
			
			
			if(!filtrosWhere.isEmpty()){
				consultaSQL.setLength(0);
				sqlWhere = "WHERE " + StringUtils.join(filtrosWhere, Y);
				
				consultaSQL.append("SELECT count(t1." + NOMBRE_CAMPO_CLAVE+ ") as total FROM " + NOMBRE_VISTA + " t1 " + sqlWhere);
				totalEncontrados = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);
			}

			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.razon_social_cliente,");
			consultaSQL.append("t1.nombre_corto_cliente,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.alias,");
			consultaSQL.append("t1.referencia_planta_recepcion,");
			consultaSQL.append("t1.referencia_destinatario_mercaderia,");
			consultaSQL.append("t1.fecha_inicio_planificacion,");
			consultaSQL.append("t1.volumen_promedio_cisterna,");
			consultaSQL.append("t1.eta_origen,");
			consultaSQL.append("t1.planta_despacho_defecto, ");
			consultaSQL.append("t1.descripcion_planta_despacho, ");
			//consultaSQL.append("t1.sincronizado_el,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.correopara,");
			consultaSQL.append("t1.correocc,");
			
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
			consultaSQL.append(sqlWhere);
			consultaSQL.append(sqlOrderBy);
			consultaSQL.append(sqlLimit);
			listaRegistros = jdbcTemplate.query(consultaSQL.toString(),parametros.toArray(), new OperacionMapper());
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

	//Agregado por req 9000002570====================
	public RespuestaCompuesta recuperarEtapas(int ID){
		StringBuilder consultaSQL= new StringBuilder();	
		List<Operacion> listaRegistros=new ArrayList<Operacion>();
		Contenido<Operacion> contenido = new Contenido<Operacion>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		Operacion eOperacion = null;
		Cliente eCliente= null;
		Planta ePlanta = null;
		try {
			
			consultaSQL.append("SELECT t1.id_operacion, t1.nombre, t1.eta_origen, t2.nombre_corto, t3.descripcion, ");
			consultaSQL.append("		t4.id_operacion_etapa_ruta, t4.nombre_etapa, t4.estado, t4.orden ");
			consultaSQL.append("FROM sgo.operacion t1 ");
			consultaSQL.append("INNER JOIN sgo.cliente t2 on (t1.id_cliente = t2.id_cliente) ");
			consultaSQL.append("INNER JOIN sgo.planta t3 on (t1.planta_despacho_defecto = t3.id_planta) ");
			consultaSQL.append("LEFT  JOIN sgo.operacion_etapa_ruta t4 on (t4.id_operacion = t1.id_operacion) ");
			consultaSQL.append("WHERE t1.id_operacion = " + ID + " ");
			consultaSQL.append("ORDER BY t4.orden");
			
			List<Map<String,Object>> mapRegistros= jdbcTemplate.queryForList(consultaSQL.toString(),new Object[]{});
			Map<String, Object> map = mapRegistros.get(0);
			
			eOperacion = new Operacion();
			eOperacion.setId((Integer) map.get("id_operacion"));
			eOperacion.setNombre((String) map.get("nombre"));
			eOperacion.setEta((Integer) map.get("eta_origen"));
			
			eCliente = new Cliente();
			eCliente.setNombreCorto((String) map.get("nombre_corto"));
			eOperacion.setCliente(eCliente);
			
			ePlanta = new Planta();
			ePlanta.setDescripcion((String) map.get("descripcion"));
			eOperacion.setPlantaDespacho(ePlanta);
			
			Integer idEtapa = (Integer) map.get("id_operacion_etapa_ruta");
			if(idEtapa != null && idEtapa > 0){
				OperacionEtapaRuta etapa;
				ArrayList<OperacionEtapaRuta> etapas = new ArrayList<OperacionEtapaRuta>();
				for(Map<String,Object> obj : mapRegistros){
					etapa = new OperacionEtapaRuta();
					etapa.setId((Integer) obj.get("id_operacion_etapa_ruta"));
					etapa.setNombreEtapa((String) obj.get("nombre_etapa"));
					etapa.setEstado((Integer) obj.get("estado"));
					etapa.setOrden((Integer) obj.get("orden"));
					
					etapas.add(etapa);
				}
				
				eOperacion.setEtapas(etapas);
			}
			
			listaRegistros.add(eOperacion);
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;			
			
		    System.out.println("recuperarEtapas: OperacionDao: " + consultaSQL.toString());
		} catch (DataAccessException excepcionAccesoDatos) {
			excepcionAccesoDatos.printStackTrace();
			respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}
	
	
	public int obtenerEtapaTransporte(int ID){
		StringBuilder consultaSQL= new StringBuilder();	
		int respuesta = 0;
		try {
			
			consultaSQL.append("SELECT count(id_etapa_transporte) ");
			consultaSQL.append("FROM sgo.etapa_transporte ");
			consultaSQL.append("WHERE id_operacion_etapa_ruta = " + ID );
			
			Integer filas = jdbcTemplate.queryForObject(consultaSQL.toString(), null, Integer.class);			
			
			if(filas == null){
				respuesta = 0;
			}else{
				respuesta = filas;
			}

		    System.out.println("recuperarEtapas: OperacionDao: " + consultaSQL.toString());
		} catch (DataAccessException excepcionAccesoDatos) {
			respuesta = 0;
		}
		return respuesta;
	}
	//==================================================
	
	public RespuestaCompuesta recuperarRegistro(int ID){
		StringBuilder consultaSQL= new StringBuilder();	
		List<TransportistaOperacion> listaTransportistaOpe=new ArrayList<TransportistaOperacion>();
		ArrayList<Transportista> listaTransportistas = new ArrayList<Transportista>();
		List<Operacion> listaRegistros=new ArrayList<Operacion>();
		Contenido<Operacion> contenido = new Contenido<Operacion>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		Operacion eOperacion= null;
		try {
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.razon_social_cliente,");
			consultaSQL.append("t1.nombre_corto_cliente,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.alias,");
			consultaSQL.append("t1.referencia_planta_recepcion,");
			consultaSQL.append("t1.referencia_destinatario_mercaderia,");
			consultaSQL.append("t1.fecha_inicio_planificacion,");
			consultaSQL.append("t1.volumen_promedio_cisterna,");
			consultaSQL.append("t1.eta_origen,");
			consultaSQL.append("t1.planta_despacho_defecto,");
			consultaSQL.append("t1.descripcion_planta_despacho, ");
			//consultaSQL.append("t1.sincronizado_el,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.correopara,");
			consultaSQL.append("t1.correocc,");
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
			eOperacion = (Operacion) jdbcTemplate.queryForObject(consultaSQL.toString(), new OperacionMapper(),new Object[] {ID});
			
		//	listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new OperacionMapper());
			
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_toperacion,");
			consultaSQL.append("t1.id_transportista,");
			consultaSQL.append("t1.razon_social,");
			consultaSQL.append("t1.nombre_corto,");
			consultaSQL.append("t1.ruc,");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.id_operacion, ");
			consultaSQL.append("t1.nombre, ");			
			consultaSQL.append("t1.id_cliente, ");
			consultaSQL.append("t1.eta_origen ");
			consultaSQL.append(" FROM ");
			consultaSQL.append(NOMBRE_VISTA_TRANSPORTISTA_OPERACION);
			consultaSQL.append(" t1 ");
			consultaSQL.append(" WHERE t1.");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=?");
			listaTransportistaOpe = jdbcTemplate.query(consultaSQL.toString(),new Object[] {eOperacion.getId()}, new TransportistaOperacionMapper());
			
			for( int i = 0 ; i < listaTransportistaOpe.size(); i++){
				listaTransportistas.add(listaTransportistaOpe.get(i).geteTransportista());
			}

			eOperacion.setTransportistas(listaTransportistas);
			listaRegistros.add(eOperacion);
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
	
	public RespuestaCompuesta guardarRegistro(Operacion operacion){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(NOMBRE_TABLA);
			//consultaSQL.append(" (nombre,alias,id_cliente,referencia_planta_recepcion,referencia_destinatario_mercaderia,fecha_inicio_planificacion,volumen_promedio_cisterna,eta_origen,planta_despacho_defecto,estado,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			//consultaSQL.append(" VALUES (:Nombre,:Alias,:IdCliente,:Referencia_planta_recepcion,:Referencia_destinatario_mercaderia,:FechaInicioPlanificacion,:Volumen_promedio_cisterna,:ETA,:PlantaDespacho,:Estado,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			
			consultaSQL.append(" (nombre,alias,id_cliente,referencia_planta_recepcion,referencia_destinatario_mercaderia,fecha_inicio_planificacion,volumen_promedio_cisterna,eta_origen,planta_despacho_defecto,estado,correopara,correocc,creado_el,creado_por,actualizado_por,actualizado_el,ip_creacion,ip_actualizacion) ");
			consultaSQL.append(" VALUES (:Nombre,:Alias,:IdCliente,:Referencia_planta_recepcion,:Referencia_destinatario_mercaderia,:FechaInicioPlanificacion,:Volumen_promedio_cisterna,:ETA,:PlantaDespacho,:Estado,:CorreoPara,:CorreoCC,:CreadoEl,:CreadoPor,:ActualizadoPor,:ActualizadoEl,:IpCreacion,:IpActualizacion) ");
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("Nombre", operacion.getNombre());
			listaParametros.addValue("Alias", operacion.getAlias());
			listaParametros.addValue("IdCliente", operacion.getIdCliente());
			//listaParametros.addValue("SincronizadoEl", operacion.getSincronizadoEl());
			listaParametros.addValue("Referencia_planta_recepcion", operacion.getReferenciaPlantaRecepcion());
			listaParametros.addValue("Referencia_destinatario_mercaderia", operacion.getReferenciaDestinatarioMercaderia());
			listaParametros.addValue("FechaInicioPlanificacion", operacion.getFechaInicioPlanificacion());
			listaParametros.addValue("Volumen_promedio_cisterna", operacion.getVolumenPromedioCisterna());
			listaParametros.addValue("ETA", operacion.getEta());
			listaParametros.addValue("PlantaDespacho", operacion.getIdPlantaDespacho());
			listaParametros.addValue("Estado", operacion.getEstado());
			listaParametros.addValue("CorreoPara", operacion.getCorreoPara());
			listaParametros.addValue("CorreoCC", operacion.getCorreoCC());
			listaParametros.addValue("CreadoEl", operacion.getCreadoEl());
			listaParametros.addValue("CreadoPor", operacion.getCreadoPor());
			listaParametros.addValue("ActualizadoPor", operacion.getActualizadoPor());
			listaParametros.addValue("ActualizadoEl", operacion.getActualizadoEl());
			listaParametros.addValue("IpCreacion", operacion.getIpCreacion());
			listaParametros.addValue("IpActualizacion", operacion.getIpActualizacion());

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
	
	public RespuestaCompuesta actualizarRegistro(Operacion operacion){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("nombre=:Nombre,");
			consultaSQL.append("alias=:Alias,");
			consultaSQL.append("id_cliente=:IdCliente,");
			consultaSQL.append("referencia_planta_recepcion=:Referencia_planta_recepcion,");
			consultaSQL.append("referencia_destinatario_mercaderia=:Referencia_destinatario_mercaderia,");
			consultaSQL.append("fecha_inicio_planificacion=:FechaInicioPlanificacion,");
			consultaSQL.append("volumen_promedio_cisterna=:Volumen_promedio_cisterna,");
			consultaSQL.append("eta_origen=:ETA,");
			consultaSQL.append("planta_despacho_defecto=:PlantaDespacho,");
			consultaSQL.append("correoPara=:CorreoPara,");
			consultaSQL.append("correoCC=:CorreoCC,");
			//Valores Auditoria
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Nombre", operacion.getNombre());
			listaParametros.addValue("IdCliente", operacion.getIdCliente());
			listaParametros.addValue("Alias", operacion.getAlias());
			listaParametros.addValue("Referencia_planta_recepcion", operacion.getReferenciaPlantaRecepcion());
			listaParametros.addValue("Referencia_destinatario_mercaderia", operacion.getReferenciaDestinatarioMercaderia());
			listaParametros.addValue("FechaInicioPlanificacion", operacion.getFechaInicioPlanificacion());
			listaParametros.addValue("Volumen_promedio_cisterna", operacion.getVolumenPromedioCisterna());
			listaParametros.addValue("ETA", operacion.getEta());
			listaParametros.addValue("PlantaDespacho", operacion.getIdPlantaDespacho());
			listaParametros.addValue("CorreoPara", operacion.getCorreoPara());
			listaParametros.addValue("CorreoCC", operacion.getCorreoCC());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", operacion.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", operacion.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", operacion.getIpActualizacion());
			listaParametros.addValue("Id", operacion.getId());
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
	
	//Agregado por 9000002570============================================
	
	public RespuestaCompuesta eliminarEtapas(int idRegistro){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int cantidadFilasAfectadas=0;	
		String consultaSQL="";
		Object[] parametros = {idRegistro};
		try {
			consultaSQL="DELETE FROM SGO.OPERACION_ETAPA_RUTA WHERE ID_OPERACION_ETAPA_RUTA = ?";
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
	
	public RespuestaCompuesta cambiarEstadoEtapa(int idRegistro, int estado){		
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		int cantidadFilasAfectadas=0;	
		String consultaSQL="";

		try {
			consultaSQL="UPDATE SGO.OPERACION_ETAPA_RUTA SET ESTADO = :Estado WHERE ID_OPERACION_ETAPA_RUTA = :IdOperacionEtapaRuta";
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", estado);
			listaParametros.addValue("IdOperacionEtapaRuta", idRegistro);
			SqlParameterSource namedParameters= listaParametros;
        	cantidadFilasAfectadas = namedJdbcTemplate.update(consultaSQL, namedParameters);
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
	
	public RespuestaCompuesta guardarRegistroEtapa(OperacionEtapaRuta etapa){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		KeyHolder claveGenerada = null;
		int cantidadFilasAfectadas=0;
		try {
			consultaSQL.append("INSERT INTO ");
			consultaSQL.append(" SGO.OPERACION_ETAPA_RUTA ");
			consultaSQL.append(" (id_operacion, nombre_etapa, orden, estado) ");
			consultaSQL.append(" VALUES (:IdOperacion,:NombreEtapa,:Orden,:Estado) ");
			
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();   
			listaParametros.addValue("IdOperacion", etapa.getIdOperacion());
			listaParametros.addValue("NombreEtapa", etapa.getNombreEtapa().toUpperCase());
			listaParametros.addValue("Orden", etapa.getOrden());
			listaParametros.addValue("Estado", etapa.getEstado());

			SqlParameterSource namedParameters= listaParametros;
			/*Ejecuta la consulta y retorna las filas afectadas*/
			claveGenerada = new GeneratedKeyHolder();
			cantidadFilasAfectadas= namedJdbcTemplate.update(consultaSQL.toString(),namedParameters,claveGenerada,new String[] {"id_operacion_etapa_ruta"});		
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
	
	public RespuestaCompuesta actualizarRegistroEtapa(OperacionEtapaRuta etapa){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {			
			consultaSQL.append("UPDATE ");
			consultaSQL.append(" SGO.OPERACION_ETAPA_RUTA ");
			consultaSQL.append(" SET ");
			consultaSQL.append("nombre_etapa=:NombreEtapa,");
			consultaSQL.append("orden=:Orden,");
			consultaSQL.append("estado=:Estado ");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(" id_operacion_etapa_ruta ");
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("NombreEtapa", etapa.getNombreEtapa());
			listaParametros.addValue("Orden", etapa.getOrden());
			listaParametros.addValue("Estado", etapa.getEstado());
			listaParametros.addValue("Id", etapa.getId());
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
		} catch (Exception ex){
			ex.printStackTrace();
			respuesta.error= Constante.EXCEPCION_ACCESO_DATOS;
			respuesta.estado=false;
		}
		return respuesta;
	}
	
	//===================================================================
	
	
	public RespuestaCompuesta ActualizarEstadoRegistro(Operacion entidad){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		StringBuilder consultaSQL= new StringBuilder();
		int cantidadFilasAfectadas=0;
		try {			
			consultaSQL.append("UPDATE ");
			consultaSQL.append(NOMBRE_TABLA);
			consultaSQL.append(" SET ");
			consultaSQL.append("estado=:Estado,");
			consultaSQL.append("actualizado_por=:ActualizadoPor,");
			consultaSQL.append("actualizado_el=:ActualizadoEl,");
			consultaSQL.append("ip_actualizacion=:IpActualizacion");
			consultaSQL.append(" WHERE ");
			consultaSQL.append(NOMBRE_CAMPO_CLAVE);
			consultaSQL.append("=:Id");
			MapSqlParameterSource listaParametros= new MapSqlParameterSource();
			listaParametros.addValue("Estado", entidad.getEstado());
			//Valores Auditoria
			listaParametros.addValue("ActualizadoEl", entidad.getActualizadoEl());
			listaParametros.addValue("ActualizadoPor", entidad.getActualizadoPor());
			listaParametros.addValue("IpActualizacion", entidad.getIpActualizacion());
			listaParametros.addValue("Id", entidad.getId());
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
	
	/**
     * Método que retorna el el listado de operaciones filtrado por cliente.
     * @param ID         	Identificador del cliente.
     * @return respuesta	Listado de operaciones.
     */
	public RespuestaCompuesta recuperarRegistrosxCliente(int ID) {
		StringBuilder consultaSQL= new StringBuilder();		
		List<Operacion> listaRegistros=new ArrayList<Operacion>();
		Contenido<Operacion> contenido = new Contenido<Operacion>();
		RespuestaCompuesta respuesta= new RespuestaCompuesta();
		try {
			consultaSQL.setLength(0);
			consultaSQL.append("SELECT ");
			consultaSQL.append("t1.id_operacion,");
			consultaSQL.append("t1.id_cliente,");
			consultaSQL.append("t1.razon_social_cliente,");
			consultaSQL.append("t1.nombre,");
			consultaSQL.append("t1.alias,");
			consultaSQL.append("t1.referencia_planta_recepcion,");
			consultaSQL.append("t1.referencia_destinatario_mercaderia,");
			consultaSQL.append("t1.fecha_inicio_planificacion,");
			consultaSQL.append("t1.volumen_promedio_cisterna,");
			consultaSQL.append("t1.eta_origen,");
			consultaSQL.append("t1.planta_despacho_defecto,");
			consultaSQL.append("t1.descripcion_planta_despacho, ");
			consultaSQL.append("t1.estado,");
			consultaSQL.append("t1.correopara,");
			consultaSQL.append("t1.correocc,");
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
			consultaSQL.append("where id_cliente = ?");
			listaRegistros= jdbcTemplate.query(consultaSQL.toString(),new Object[] {ID},new OperacionMapper());
			contenido.totalRegistros=listaRegistros.size();
			contenido.totalEncontrados=listaRegistros.size();
			contenido.carga= listaRegistros;
			respuesta.mensaje="OK";
			respuesta.estado=true;
			respuesta.contenido = contenido;
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