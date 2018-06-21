package sgo.servicio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import sgo.datos.AsignacionDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.ClienteDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.DetalleProgramacionDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EventoDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.DespachoCargaDao;
import sgo.datos.EnlaceDao;
import sgo.datos.TransportistaDao;
import sgo.entidad.Bitacora;
import sgo.entidad.DespachoCarga;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;

@Controller
public class DespachoCargaControlador {
@Autowired
private MessageSource gestorDiccionario;// Gestor del diccionario de
										// mensajes
										// para la internacionalizacion
@Autowired
private BitacoraDao dBitacora; // Clase para registrar en la bitacora
								// (auditoria por accion)
@Autowired
private EnlaceDao dEnlace;
@Autowired
private MenuGestor menu;
@Autowired
private DiaOperativoDao dDiaOperativo;
@Autowired
private AsignacionDao dAsignacion;
@Autowired
private PlanificacionDao dPlanificacion;
@Autowired
private TransportistaDao dTransportista;
@Autowired
private ClienteDao dCliente;
@Autowired
private CisternaDao dCisterna;
@Autowired
private CompartimentoDao dCompartimento;
@Autowired
private OperacionDao dOperacion;
@Autowired
private ProductoDao dProducto;
@Autowired
private DespachoCargaDao dDespachoCarga;
@Autowired
private DiaOperativoControlador DiaOperativoControlador;
@Autowired
private DetalleProgramacionDao dDetalleProgramacion;

//
private DataSourceTransactionManager transaccion;// Gestor de la transaccion
// urls generales
private static final String URL_GESTION_COMPLETA = "/admin/despachoCarga";
private static final String URL_GESTION_RELATIVA = "/despachoCarga";
private static final String URL_GUARDAR_COMPLETA = "/admin/despachoCarga/crear";
private static final String URL_GUARDAR_RELATIVA = "/despachoCarga/crear";
private static final String URL_LISTAR_COMPLETA = "/admin/despachoCarga/listar";
private static final String URL_LISTAR_RELATIVA = "/despachoCarga/listar";
private static final String URL_ACTUALIZAR_COMPLETA = "/admin/despachoCarga/actualizar";
private static final String URL_ACTUALIZAR_RELATIVA = "/despachoCarga/actualizar";
private static final String URL_RECUPERAR_COMPLETA = "/admin/despachoCarga/recuperar";
private static final String URL_RECUPERAR_RELATIVA = "/despachoCarga/recuperar";

private HashMap<String, String> recuperarMapaValores(Locale locale) {
	HashMap<String, String> mapaValores = new HashMap<String, String>();
	try {
		mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
	    mapaValores.put("ESTADO_INACTIVO", String.valueOf(Constante.ESTADO_INACTIVO));
	    mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
	    mapaValores.put("TEXTO_INACTIVO", gestorDiccionario.getMessage("sgo.estadoInactivo",null,locale));
	    mapaValores.put("TEXTO_ACTIVO", gestorDiccionario.getMessage("sgo.estadoActivo",null,locale));
	    mapaValores.put("TEXTO_TODOS", gestorDiccionario.getMessage("sgo.filtroTodos",null,locale));
	    
	    mapaValores.put("TITULO_AGREGAR_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioAgregar",null,locale));
	    mapaValores.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar",null,locale));
	    mapaValores.put("TITULO_DETALLE_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioVer",null,locale));
	    mapaValores.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado",null,locale));
	    //
	    mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar",null,locale));

	    mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar",null,locale));
	    
	    mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar",null,locale));
	    mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado",null,locale));
	    
	    mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
	    mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));
	} catch (Exception ex) {

	}
	return mapaValores;
}

// @SuppressWarnings("unchecked")
@RequestMapping(URL_GESTION_RELATIVA)
public ModelAndView mostrarFormulario(Locale locale) {
	ModelAndView vista = null;
	AuthenticatedUserDetails principal = null;
	ArrayList<?> listaEnlaces = null;
	ArrayList<?> listaClientes = null;
	ArrayList<?> listaOperaciones = null;
	ArrayList<?> listaProductos = null;
	RespuestaCompuesta respuesta = null;
	ParametrosListar parametros = null;
	HashMap<String, String> mapaValores = null;
	try {
		principal = this.getCurrentUser();
		respuesta = menu.Generar(principal.getRol().getId(),URL_GESTION_COMPLETA);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
		}
		listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;

		parametros = new ParametrosListar();
		parametros.setPaginacion(Constante.SIN_PAGINACION);
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		respuesta = dCliente.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}

		parametros = new ParametrosListar();
		parametros.setPaginacion(Constante.SIN_PAGINACION);
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		parametros.setIdCliente(principal.getCliente().getId());
		parametros.setIdOperacion(principal.getOperacion().getId());
		respuesta = dOperacion.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;

		parametros = new ParametrosListar();
		parametros.setPaginacion(Constante.SIN_PAGINACION);
		// Para que retorne sólo los productos que se encuentren activos
		parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
		respuesta = dProducto.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaProductos = (ArrayList<?>) respuesta.contenido.carga;
		mapaValores = recuperarMapaValores(locale);
		vista = new ModelAndView("plantilla");
		vista.addObject("vistaJSP", "despachoCarga/despachoCarga.jsp");
		vista.addObject("vistaJS", "despachoCarga/despachoCarga.js");
		vista.addObject("identidadUsuario", principal.getIdentidad());
		vista.addObject("menu", listaEnlaces);
		vista.addObject("clientes", listaClientes);
		vista.addObject("operaciones", listaOperaciones);
		vista.addObject("productos", listaProductos);
		vista.addObject("mapaValores", mapaValores);
		vista.addObject("fechaActual",dDiaOperativo.recuperarFechaActual().valor);
	} catch (Exception ex) {

	}
	return vista;
}

@RequestMapping(value = URL_LISTAR_RELATIVA ,method = RequestMethod.GET)
public @ResponseBody RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest, Locale locale){
	RespuestaCompuesta respuesta = null;
	ParametrosListar parametros= null;
	AuthenticatedUserDetails principal = null;
	String mensajeRespuesta="";
	try {
		//Recuperar el usuario actual
		principal = this.getCurrentUser(); 
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
		if (respuesta.estado==false){
			mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale);
			throw new Exception(mensajeRespuesta);
		}
		Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale);
			throw new Exception(mensajeRespuesta);
		}
		//Recuperar parametros
		 parametros = new ParametrosListar();
		if (httpRequest.getParameter("registrosxPagina") != null) {
			parametros.setRegistrosxPagina(Integer.parseInt( httpRequest.getParameter("registrosxPagina")));
		}
		
		if (httpRequest.getParameter("inicioPagina") != null) {
			parametros.setInicioPaginacion(Integer.parseInt( httpRequest.getParameter("inicioPagina")));
		}
		
		if (httpRequest.getParameter("campoOrdenamiento") != null) {
			parametros.setCampoOrdenamiento(( httpRequest.getParameter("campoOrdenamiento")));
		}
		
		if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
			parametros.setSentidoOrdenamiento(( httpRequest.getParameter("sentidoOrdenamiento")));
		}
		
		if (httpRequest.getParameter("valorBuscado") != null) {
			parametros.setValorBuscado(( httpRequest.getParameter("valorBuscado")));
		}
		
		if (httpRequest.getParameter("txtFiltro") != null) {
			parametros.setTxtFiltro((httpRequest.getParameter("txtFiltro")));
		}
		
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("filtroEstado") != null) {
			parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
		}
		
		if (httpRequest.getParameter("filtroFechaJornada") != null) {
			parametros.setFiltroFechaJornada((httpRequest.getParameter("filtroFechaJornada")));
		}
		
		if (httpRequest.getParameter("idJornada") != null) {
			parametros.setIdJornada(Integer.parseInt(httpRequest.getParameter("idJornada")));
		}
		
		if (httpRequest.getParameter("idCliente") != null) {
			parametros.setIdCliente(Integer.parseInt(httpRequest.getParameter("idCliente")));
		}
		
		if (httpRequest.getParameter("filtroOperacion") != null) {
			parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
		}

		if (httpRequest.getParameter("filtroEstacion") != null) {
			parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
		}
		
		if (httpRequest.getParameter("filtroIdOperario") != null) {
			parametros.setFiltroIdOperario(Integer.parseInt(httpRequest.getParameter("filtroIdOperario")));
		}

		//Recuperar registros
		respuesta = dDespachoCarga.recuperarRegistros(parametros);
		respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
	} catch(Exception ex){
		ex.printStackTrace();
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}	

@RequestMapping(value = URL_RECUPERAR_RELATIVA ,method = RequestMethod.GET)
public @ResponseBody RespuestaCompuesta recuperaRegistro(int ID,Locale locale){
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	try {			
		//Recupera el usuario actual
		principal = this.getCurrentUser(); 
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA);
		if (respuesta.estado==false){
			throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
		}
		Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
		}
		//Recuperar el registro
    	respuesta= dDespachoCarga.recuperarRegistro(ID);
    	//Verifica el resultado de la accion
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
     	respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
	} catch (Exception ex){
		ex.printStackTrace();
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}

@RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
public @ResponseBody
RespuestaCompuesta guardarRegistro(@RequestBody DespachoCarga eDespachoCarga, HttpServletRequest peticionHttp, Locale locale) {
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	Bitacora eBitacora= null;
	String ContenidoAuditoria ="";
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	String direccionIp="";
	String ClaveGenerada="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dDespachoCarga.getDataSource());
		definicionTransaccion = new DefaultTransactionDefinition();
		estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		eBitacora = new Bitacora();
		//Recuperar el usuario actual
		principal = this.getCurrentUser(); 
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_COMPLETA);
		if (respuesta.estado==false){
			throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
		}
		Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
		}			
		//Actualiza los datos de auditoria local
		direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");  
		if (direccionIp == null) {  
			direccionIp = peticionHttp.getRemoteAddr();  
		}
    	eDespachoCarga.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eDespachoCarga.setActualizadoPor(principal.getID()); 
       	eDespachoCarga.setCreadoEl(Calendar.getInstance().getTime().getTime());
        eDespachoCarga.setCreadoPor(principal.getID());
        eDespachoCarga.setIpActualizacion(direccionIp);
        eDespachoCarga.setIpCreacion(direccionIp);
        respuesta= dDespachoCarga.guardarRegistro(eDespachoCarga);
        //Verifica si la accion se ejecuto de forma satisfactoria
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
        }
        ClaveGenerada = respuesta.valor;
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
        ContenidoAuditoria =  mapper.writeValueAsString(eDespachoCarga);
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_GUARDAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(ClaveGenerada);
        eBitacora.setContenido(ContenidoAuditoria);
        eBitacora.setRealizadoEl(eDespachoCarga.getCreadoEl());
        eBitacora.setRealizadoPor(eDespachoCarga.getCreadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }           
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eDespachoCarga.getFechaCreacion().substring(0, 9),eDespachoCarga.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
    	this.transaccion.commit(estadoTransaccion);
	} catch (Exception ex){
		this.transaccion.rollback(estadoTransaccion);
		ex.printStackTrace();
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}

@RequestMapping(value = URL_ACTUALIZAR_RELATIVA, method = RequestMethod.POST)
public @ResponseBody
RespuestaCompuesta actualizarRegistro(@RequestBody DespachoCarga eDespachoCarga, HttpServletRequest peticionHttp, Locale locale) {
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dDespachoCarga.getDataSource());
		definicionTransaccion = new DefaultTransactionDefinition();
		estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		eBitacora = new Bitacora();
		//Recuperar el usuario actual
		principal = this.getCurrentUser();
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_COMPLETA);
		if (respuesta.estado==false){
			throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
		}
		Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
		}			
		//Auditoria local (En el mismo registro)
		direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");  
		if (direccionIp == null) {  
			direccionIp = peticionHttp.getRemoteAddr();  
		}
    	eDespachoCarga.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eDespachoCarga.setActualizadoPor(principal.getID()); 
        eDespachoCarga.setIpActualizacion(direccionIp);
        respuesta= dDespachoCarga.actualizarRegistro(eDespachoCarga);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eDespachoCarga.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eDespachoCarga));
        eBitacora.setRealizadoEl(eDespachoCarga.getActualizadoEl());
        eBitacora.setRealizadoPor(eDespachoCarga.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eDespachoCarga.getFechaActualizacion().substring(0, 9),eDespachoCarga.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
    	this.transaccion.commit(estadoTransaccion);
	} catch (Exception ex){
		ex.printStackTrace();
		this.transaccion.rollback(estadoTransaccion);
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}

private AuthenticatedUserDetails getCurrentUser() {
	return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
}

}
