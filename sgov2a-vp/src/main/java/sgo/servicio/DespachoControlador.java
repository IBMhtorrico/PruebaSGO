package sgo.servicio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.jaxen.function.SubstringAfterFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.AsignacionDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.ClienteDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.ContometroDao;
import sgo.datos.ContometroJornadaDao;
import sgo.datos.DespachoCargaDao;
import sgo.datos.DespachoDao;
import sgo.datos.DetalleProgramacionDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EstacionDao;
import sgo.datos.EventoDao;
import sgo.datos.JornadaDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.PropietarioDao;
import sgo.datos.TanqueDao;
import sgo.datos.TanqueJornadaDao;
import sgo.datos.ToleranciaDao;
import sgo.datos.TransportistaDao;
import sgo.datos.VehiculoDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contometro;
import sgo.entidad.ContometroJornada;
import sgo.entidad.Despacho;
import sgo.entidad.DespachoCarga;
import sgo.entidad.Enlace;
import sgo.entidad.Jornada;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.entidad.Tolerancia;
import sgo.entidad.Vehiculo;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Formula;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DespachoControlador {
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
private DespachoDao dDespacho;
@Autowired
private DiaOperativoControlador DiaOperativoControlador;
@Autowired
private DetalleProgramacionDao dDetalleProgramacion;
@Autowired
private PropietarioDao dPropietarioDao;
@Autowired
private VehiculoDao dVehiculoDao;
@Autowired
private ContometroDao dContometroDao;
@Autowired
private ContometroJornadaDao dContometroJornadaDao;
@Autowired
private TanqueDao dTanqueDao;
@Autowired
private EstacionDao dEstacion;
@Autowired
private TanqueJornadaDao dTanqueJornadaDao;
@Autowired
private DespachoCargaDao dDespachoCargaDao;
@Autowired
private JornadaDao dJornadaDao;
@Autowired
private ToleranciaDao dToleranciaDao;
@Autowired
private JornadaDao dJornada;
//
private DataSourceTransactionManager transaccion;// Gestor de la transaccion
// urls generales
private static final String URL_GESTION_COMPLETA = "/admin/despacho";
private static final String URL_GESTION_RELATIVA = "/despacho";
private static final String URL_GUARDAR_COMPLETA = "/admin/despacho/crear";
private static final String URL_GUARDAR_RELATIVA = "/despacho/crear";
private static final String URL_LISTAR_COMPLETA = "/admin/despacho/listar";
private static final String URL_LISTAR_RELATIVA = "/despacho/listar";
private static final String URL_ACTUALIZAR_COMPLETA = "/admin/despacho/actualizar";
private static final String URL_ACTUALIZAR_RELATIVA = "/despacho/actualizar";
private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/despacho/actualizarEstado";
private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/despacho/actualizarEstado";
private static final String URL_RECUPERAR_COMPLETA = "/admin/despacho/recuperar";
private static final String URL_RECUPERAR_RELATIVA = "/despacho/recuperar";
private static final String URL_CARGAR_ARCHIVO_COMPLETA="/admin/despacho/cargar-archivo";
private static final String URL_CARGAR_ARCHIVO_RELATIVA="/despacho/cargar-archivo";
private static final String SEPARADOR_CSV=",";

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
	    mapaValores.put("MENSAJE_ANULAR_REGISTRO", gestorDiccionario.getMessage("sgo.mensajeAnularRegistro",null,locale));
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
	ArrayList<?> listaEstaciones = null;
	ArrayList<?> listaProductos = null;
	RespuestaCompuesta respuesta = null;
	ParametrosListar parametros = null;
	HashMap<String, String> mapaValores = null;
	try {
		principal = this.getCurrentUser();
		respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
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
		// Para que retorne sÃ³lo los productos que se encuentren activos
		parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
		respuesta = dProducto.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaProductos = (ArrayList<?>) respuesta.contenido.carga;
		
		String fecha = dDiaOperativo.recuperarFechaActual().valor;
		Operacion op = (Operacion) listaOperaciones.get(0);
		parametros.setIdOperacion(op.getId());
	    //esto para obtener la última jornada cargada 
	    Respuesta oRespuesta = dJornada.recuperarUltimaJornada(parametros);
	    // Verifica el resultado de la accion
	    if (oRespuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
	    }
	    if (oRespuesta.valor != null) {
	 	   fecha = oRespuesta.valor;
	    }
	    
	    parametros = new ParametrosListar();
		parametros.setPaginacion(Constante.SIN_PAGINACION);
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		parametros.setIdCliente(principal.getCliente().getId());
		parametros.setFiltroOperacion(op.getId());
		respuesta = dEstacion.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaEstaciones = (ArrayList<?>) respuesta.contenido.carga;
	    
		mapaValores = recuperarMapaValores(locale);
		vista = new ModelAndView("plantilla");
		vista.addObject("vistaJSP", "despacho/despacho.jsp");
		vista.addObject("vistaJS", "despacho/despacho.js");
		vista.addObject("identidadUsuario", principal.getIdentidad());
		vista.addObject("menu", listaEnlaces);
		vista.addObject("clientes", listaClientes);
		vista.addObject("operaciones", listaOperaciones);
		vista.addObject("estaciones", listaEstaciones);
		vista.addObject("productos", listaProductos);
		vista.addObject("mapaValores", mapaValores);
		//vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
		vista.addObject("fechaActual", fecha);
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

		if (httpRequest.getParameter("filtroCodigoArchivoOrigen") != null) {
			parametros.setFiltroCodigoArchivoOrigen(Integer.parseInt(httpRequest.getParameter("filtroCodigoArchivoOrigen")));
		}
		
		//Recuperar registros
		respuesta = dDespacho.recuperarRegistros(parametros);
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
    	respuesta= dDespacho.recuperarRegistro(ID);
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
RespuestaCompuesta guardarRegistro(@RequestBody Despacho eDespacho,
		HttpServletRequest peticionHttp, Locale locale) {
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
		this.transaccion = new DataSourceTransactionManager(dDespacho.getDataSource());
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
		
		if(!Utilidades.esValido(eDespacho.getFechaHoraInicio())){
			throw new Exception(gestorDiccionario.getMessage("sgo.errorFechaInicio",null,locale));
		}
		if(!Utilidades.esValido(eDespacho.getFechaHoraFin())){
			throw new Exception(gestorDiccionario.getMessage("sgo.errorFechaFin",null,locale));
		}
		
//		//para recuperar el id del contometro
		respuesta= dContometroJornadaDao.recuperarRegistro(eDespacho.getIdContometro());
		if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
		ContometroJornada contometroJornada = (ContometroJornada) respuesta.contenido.carga.get(0);
		eDespacho.setIdContometro(contometroJornada.getIdContometro());
		
//		//para recuperar el id del tanque
//		respuesta= dTanqueJornadaDao.recuperarRegistro(eDespacho.getIdTanque());
//		if (respuesta.estado==false){        	
//        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
//        }
//		TanqueJornada tanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(0);
//		eDespacho.setIdTanque(tanqueJornada.getIdTanque());
		
    	eDespacho.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eDespacho.setActualizadoPor(principal.getID()); 
       	eDespacho.setCreadoEl(Calendar.getInstance().getTime().getTime());
        eDespacho.setCreadoPor(principal.getID());
        eDespacho.setIpActualizacion(direccionIp);
        eDespacho.setIpCreacion(direccionIp);
        eDespacho.setTipoRegistro(Despacho.ORIGEN_MANUAL);
        respuesta= dDespacho.guardarRegistro(eDespacho);
        //Verifica si la accion se ejecuto de forma satisfactoria
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
        }
        ClaveGenerada = respuesta.valor;
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
        ContenidoAuditoria =  mapper.writeValueAsString(eDespacho);
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_GUARDAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(ClaveGenerada);
        eBitacora.setContenido(ContenidoAuditoria);
        eBitacora.setRealizadoEl(eDespacho.getCreadoEl());
        eBitacora.setRealizadoPor(eDespacho.getCreadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }           
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eDespacho.getFechaCreacion().substring(0, 9),eDespacho.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
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
RespuestaCompuesta actualizarRegistro(@RequestBody Despacho eDespacho, HttpServletRequest peticionHttp, Locale locale) {
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dDespacho.getDataSource());
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
    	eDespacho.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eDespacho.setActualizadoPor(principal.getID()); 
        eDespacho.setIpActualizacion(direccionIp);
        
        //RECUPERAR DESPACHO ANTES DE LA MODIFICACION
        respuesta= dDespacho.recuperarRegistro(eDespacho.getId());
	    if (respuesta.estado == false) {
		     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
		}
	    Despacho despachoAnterior=(Despacho)respuesta.contenido.getCarga().get(0);
	    int estadoDespacho = despachoAnterior.getTipoRegistro();
	    if(estadoDespacho==Despacho.ORIGEN_FICHERO){
	    	eDespacho.setTipoRegistro(Despacho.ORIGEN_MIXTO);
	    }
        respuesta= dDespacho.actualizarRegistro(eDespacho);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eDespacho.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eDespacho));
        eBitacora.setRealizadoEl(eDespacho.getActualizadoEl());
        eBitacora.setRealizadoPor(eDespacho.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eDespacho.getFechaActualizacion().substring(0, 9),eDespacho.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);
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

@RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA, method = RequestMethod.POST)
public @ResponseBody
RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Despacho eDespacho, HttpServletRequest peticionHttp, Locale locale) {
   RespuestaCompuesta respuesta = null;
   AuthenticatedUserDetails principal = null;
   TransactionDefinition definicionTransaccion = null;
   TransactionStatus estadoTransaccion = null;
   Bitacora eBitacora = null;
   String direccionIp = "";
   try {
	    // Inicia la transaccion
	    this.transaccion = new DataSourceTransactionManager(dDespacho.getDataSource());
	    definicionTransaccion = new DefaultTransactionDefinition();
	    estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
	    eBitacora = new Bitacora();
	    // Recuperar el usuario actual
	    principal = this.getCurrentUser();
	    // Recuperar el enlace de la accion
	    respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_ESTADO_COMPLETA);
	    if (respuesta.estado == false) {
	       throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
	    }
	    Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	    // Verificar si cuenta con el permiso necesario
	    if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	    }
	    // Auditoria local (En el mismo registro)
	    direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
	    if (direccionIp == null) {
	       direccionIp = peticionHttp.getRemoteAddr();
	    }
	    eDespacho.setActualizadoEl(Calendar.getInstance().getTime().getTime());
	    eDespacho.setActualizadoPor(principal.getID());
	    eDespacho.setIpActualizacion(direccionIp);
	    respuesta = dDespacho.ActualizarEstadoRegistro(eDespacho);
	    if (respuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
	    }
	    // Guardar en la bitacora
	    ObjectMapper mapper = new ObjectMapper();
	    eBitacora.setUsuario(principal.getNombre());
	    eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
	    eBitacora.setTabla(CisternaDao.NOMBRE_TABLA);
	    eBitacora.setIdentificador(String.valueOf(eDespacho.getId()));
	    eBitacora.setContenido(mapper.writeValueAsString(eDespacho));
	    eBitacora.setRealizadoEl(eDespacho.getActualizadoEl());
	    eBitacora.setRealizadoPor(eDespacho.getActualizadoPor());
	    respuesta = dBitacora.guardarRegistro(eBitacora);
	    if (respuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
	    }
	    respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eDespacho.getFechaActualizacion().substring(0, 9), eDespacho.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);
	    this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex) {
      ex.printStackTrace();
      this.transaccion.rollback(estadoTransaccion);
      respuesta.estado = false;
      respuesta.contenido = null;
      respuesta.mensaje = ex.getMessage();
    }
 return respuesta;
}
	
@RequestMapping(value = URL_CARGAR_ARCHIVO_RELATIVA+"/{idJornada}/{idOperario}/{comentario}" ,method = RequestMethod.POST)
public @ResponseBody RespuestaCompuesta cargarArchivo(@RequestParam(value="file") MultipartFile file,
  @PathVariable("idJornada") String idJornada,
  @PathVariable("idOperario") String idOperario,
  @PathVariable("comentario") String comentario,
//  @PathVariable("borrar") int borrar,
  HttpServletRequest peticionHttp,Locale locale){
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Despacho despacho= null;
  String direccionIp="";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  try {     
	//Inicia la transaccion
	this.transaccion = new DataSourceTransactionManager(dDespacho.getDataSource());
	definicionTransaccion = new DefaultTransactionDefinition();
	estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);

	
    //Recupera el usuario actual
    principal = this.getCurrentUser(); 
    //Recuperar el enlace de la accion
    respuesta = dEnlace.recuperarRegistro(URL_CARGAR_ARCHIVO_COMPLETA);
    if (respuesta.estado==false) {
      throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
    }
    Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
    //Verificar si cuenta con el permiso necesario      
    if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
        throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
    }
    ParametrosListar argumentosListar=null;
    //valido si existe una carga con el mismo archivo
    argumentosListar=new ParametrosListar();
    argumentosListar.setNombreArchivoDespacho(file.getOriginalFilename());
    argumentosListar.setIdJornada(Integer.parseInt(idJornada));
    respuesta=dDespachoCargaDao.recuperarRegistros(argumentosListar);
    if (respuesta.estado==false){  
    	throw new Exception("Error al validar la existencia del archivo de carga");
    }
    if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
    	throw new Exception("Ya existe una carga del archivo "+file.getOriginalFilename());
    }    
    
    InputStream inputStream = file.getInputStream();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    String linea;
    direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");  
    if (direccionIp == null) {  
      direccionIp = peticionHttp.getRemoteAddr();  
    }
    String[] columnas= null;
    
    //Propietario propietario=null;
    Vehiculo vehiculo=null;
    Tolerancia tolerancia_producto=null;
    Contometro contometro=null;
    Tanque tanque=null;
    String sHoraInicio;
    String sHoraFin;
    String clasificacion=null;
    float volumen_observado=0;
    String sVolumen_observado=null;
    String ClaveGenerada="";
    String numero_vale=null;
    float kilometro_horometro=0;
    float factor_correccion=0;//Valor del factor.
    float lectura_inicial=0;
    float lectura_final=0;
    int numeroLineas = 0;
    float api_60=0;
    float temperatura=0;
    float volumen_corregido=0;
    
    String nombre_corto_vehiculo=null;//Nombre corto del vehï¿½culo o maquina al que se le abastece combustible.
    String abreviatura_producto=null;//Abreviatura del producto (material).
    String contrometro_alias=null;
    String descripcion_tanque=null;
    String sLectura_inicial=null;
    String sLectura_final=null;
    Jornada jornada=null;
    double factorCorreccionVolumen=0; 
    
    respuesta = dJornadaDao.recuperarRegistro(Integer.parseInt(idJornada));
    if (respuesta.estado == false) {
  	  throw new Exception("Error al obtener jornada");        	  
    }
    if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
    	jornada=(Jornada)respuesta.contenido.getCarga().get(0);  	 
    }
    
    //guardar cabecera
    DespachoCarga despachoCarga=new DespachoCarga();
    despachoCarga.setNombreArchivo(file.getOriginalFilename());
    despachoCarga.setIdOperario(Integer.parseInt(idOperario));
    despachoCarga.setIdEstacion(jornada.getEstacion().getId());
    despachoCarga.setIdJornada(jornada.getId());
    despachoCarga.setFechaCarga(dDiaOperativo.recuperarFechaActualDateSql("yyyy-MM-dd"));
    despachoCarga.setComentario(comentario);
    despachoCarga.setActualizadoEl(Calendar.getInstance().getTime().getTime());
    despachoCarga.setActualizadoPor(principal.getID()); 
    despachoCarga.setCreadoEl(Calendar.getInstance().getTime().getTime());
    despachoCarga.setCreadoPor(principal.getID());
    despachoCarga.setIpActualizacion(direccionIp);
    despachoCarga.setIpCreacion(direccionIp);
    //buscar jornada 
    respuesta=dDespachoCargaDao.guardarRegistro(despachoCarga);
    if (respuesta.estado==false){       
      //listaAlturasNoProcesadas.add(columnas[1]);
      throw new Exception("No se pudo registrar Despacho Carga.");
    }
    ClaveGenerada = respuesta.valor;
    
    int numero_columna=0; 
    
    
  //  int cantidad_columnas_csv = bufferedReader.readLine().split(SEPARADOR_CSV).length;
    
    while ((linea = bufferedReader.readLine()) != null)
    {
     if (numeroLineas > 0){
      columnas= linea.split(SEPARADOR_CSV,-1);
      numero_columna=columnas.length;
      despacho = new Despacho();
      nombre_corto_vehiculo=columnas[0];
      if(nombre_corto_vehiculo!=null && !nombre_corto_vehiculo.isEmpty()){
          argumentosListar=new ParametrosListar();
          argumentosListar.setVehiculoNombreCorto(nombre_corto_vehiculo);   
          respuesta=dVehiculoDao.recuperarRegistros(argumentosListar);
          if (respuesta.estado == false) {
        	  throw new Exception("Error al obtener el identificador del vehiculo :"+nombre_corto_vehiculo);        	  
          }else{
              if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
            	  vehiculo=(Vehiculo)respuesta.contenido.getCarga().get(0);
            	  despacho.setIdVehiculo(vehiculo.getId());
              }else{
            	  //errorCSV.append("No se encontro el vehiculo con el nombre :"+nombre_corto_vehiculo);
            	  throw new Exception("No se encontro el vehiculo con el nombre :"+nombre_corto_vehiculo);            	 
              }
          }
      }else{    	  
    	  throw new Exception("Nombre Corto Vehiculo es requerido. Fila:"+numeroLineas);    	 
      }


      if(columnas[1]!=null && !columnas[1].isEmpty()){
    	kilometro_horometro= Float.parseFloat(columnas[1]); 
    	despacho.setKilometroHorometro(kilometro_horometro);
      }      
      
      numero_vale=columnas[2];   
      if(numero_vale!=null && !numero_vale.isEmpty()){
    	despacho.setNumeroVale(numero_vale);    	 
      }else{
    	throw new Exception("Numero Vale es requerido. Fila:"+numeroLineas);    	
      }      
      sHoraInicio = columnas[3];      
      sHoraFin = columnas[4];  
      String fechaOperativa=Utilidades.convierteDateAString(jornada.getFechaOperativa(), "yyyyMMdd");
      if(sHoraInicio!=null && !sHoraInicio.isEmpty()){     	  
          Date fechaInicio=Utilidades.convierteStringADate(fechaOperativa+sHoraInicio, "yyyyMMddHH:mm");      
          java.sql.Timestamp currentTimestampIni = new java.sql.Timestamp(fechaInicio.getTime());
          despacho.setFechaHoraInicio(currentTimestampIni);
      }else{
    	throw new Exception("Hora de inicio es requerido. Fila:"+numeroLineas);    	
      }
      if(sHoraFin!=null && !sHoraFin.isEmpty()){
    	  Date fechaFin=Utilidades.convierteStringADate(fechaOperativa+sHoraFin, "yyyyMMddHH:mm");      
          java.sql.Timestamp currentTimestampFin = new java.sql.Timestamp(fechaFin.getTime());
          despacho.setFechaHoraFin(currentTimestampFin);   	 
      }else{
    	throw new Exception("Hora de Fin es requerido. Fila:"+numeroLineas);    	
      }
      
      clasificacion= columnas[5];     
      if(clasificacion!=null && !clasificacion.isEmpty()){
    	  if(Despacho.CLASIFICACION_RECIRCULADO_TEXT.equals(clasificacion.trim().toUpperCase()) || 
    			  Despacho.CLASIFICACION_TRANSFERIDO_TEXT.equals(clasificacion.trim().toUpperCase())){
    		  if(Despacho.CLASIFICACION_RECIRCULADO_TEXT.equals(clasificacion.trim().toUpperCase())){
    			  despacho.setClasificacion(String.valueOf(Despacho.CLASIFICACION_RECIRCULADO));
    		  }else{
    			  despacho.setClasificacion(String.valueOf(Despacho.CLASIFICACION_TRANSFERIDO));
    		  }    		      	 
    	  }else{
    		  throw new Exception("Ingreso un valor correcto para la Clasificacion :"+Despacho.CLASIFICACION_RECIRCULADO_TEXT +
    				  " o "+ Despacho.CLASIFICACION_TRANSFERIDO_TEXT+". Fila:"+numeroLineas);    	
    	  }
    	  
      }else{
    	throw new Exception("Clasificacion es requerido. Fila:"+numeroLineas);    	
      }  
      abreviatura_producto=columnas[6];
      if(abreviatura_producto!=null && !abreviatura_producto.isEmpty()){
          argumentosListar=new ParametrosListar();
          argumentosListar.setAbreviaturaProducto(abreviatura_producto);
          argumentosListar.setFiltroEstacion(jornada.getEstacion().getId());
          respuesta=dToleranciaDao.recuperarRegistros(argumentosListar);
          if (respuesta.estado == false) {
        	  throw new Exception("Error al obtener el identificador del producto :"+abreviatura_producto);
          }else{
        	  if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
        		  tolerancia_producto=(Tolerancia)respuesta.contenido.getCarga().get(0); 
        		  despacho.setIdProducto(tolerancia_producto.getProducto().getId());
              }else{            	  
            	  throw new Exception("No se encontro en la estacion  " + jornada.getEstacion().getNombre()+" el Producto con la abreviatura "+abreviatura_producto);            	  
              } 
          }
      }else{
    	  throw new Exception("Abreviatura Producto es requerido Fila:"+numeroLineas);
    	  
      }
      contrometro_alias=columnas[7];
      if(contrometro_alias!=null && !contrometro_alias.isEmpty()){
          argumentosListar=new ParametrosListar();
          argumentosListar.setTxtFiltro(contrometro_alias);
          argumentosListar.setFiltroEstacion(jornada.getEstacion().getId());
          respuesta=dContometroDao.recuperarRegistros(argumentosListar);
          if (respuesta.estado == false) {
        	  throw new Exception("Error al obtener el identificador del contometros :"+contrometro_alias);
          }else{
        	  if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
        		  contometro=(Contometro)respuesta.contenido.getCarga().get(0);
        		  despacho.setIdContometro(contometro.getId());
              }else{
            	  throw new Exception("No se encontro en la estacion  " + jornada.getEstacion().getNombre()+" el Contometro con el alias "+contrometro_alias);            	 
              } 
          }
      }else{
    	  throw new Exception("Alias contometro es requerido.Fila:"+numeroLineas);    	  
      }
      
      //tanque
      Date fechaInicioImportacion=Utilidades.convierteStringADate(fechaOperativa+sHoraInicio, "yyyyMMddHH:mm");
      Date fechaFinImportacion=Utilidades.convierteStringADate(fechaOperativa+sHoraFin, "yyyyMMddHH:mm");  
      
      if(fechaInicioImportacion.after(fechaFinImportacion)){
    	  throw new Exception("la Hora de Inicio no puede ser posterior a la Hora Fin. Fila:"+numeroLineas); 
      }      
      
      if(sHoraInicio!=null && !sHoraInicio.isEmpty()){
          Date fechaInicio=Utilidades.convierteStringADate(fechaOperativa+sHoraInicio, "yyyyMMddHH:mm");      
          java.sql.Timestamp currentTimestampIni = new java.sql.Timestamp(fechaInicio.getTime());
          despacho.setFechaHoraInicio(currentTimestampIni);
      }else{
    	throw new Exception("Hora de inicio es requerido. Fila:"+numeroLineas);    	
      }
      if(sHoraFin!=null && !sHoraFin.isEmpty()){
    	  Date fechaFin=Utilidades.convierteStringADate(fechaOperativa+sHoraFin, "yyyyMMddHH:mm");      
          java.sql.Timestamp currentTimestampFin = new java.sql.Timestamp(fechaFin.getTime());
          despacho.setFechaHoraFin(currentTimestampFin);   	 
      }else{
    	throw new Exception("Hora de Fin es requerido. Fila:"+numeroLineas);    	
      }
      
      //buscamos el tanque que se encuentre despachando
      ParametrosListar parametros = new ParametrosListar();
      parametros.setIdJornada(despachoCarga.getIdJornada());
      parametros.setFiltroProducto(despacho.getIdProducto());
      String fecha =(fechaOperativa.substring(0, 4))+"-"+(fechaOperativa.substring(4,6))+"-"+(fechaOperativa.substring(6,8));
      parametros.setFiltroInicioDespacho(fecha+" "+sHoraInicio);
      
      respuesta = dTanqueJornadaDao.recuperarRegistros(parametros);
      if (respuesta.estado == false) {
    	  throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
      } else {
    	  if(respuesta.contenido.carga.size() > 0){
    		  TanqueJornada eTanque = (TanqueJornada) respuesta.contenido.carga.get(0);
    		  despacho.setIdTanque(eTanque.getIdTanque());
    	  } else {
    		  parametros.setFiltroInicioDespacho("");
    		  parametros.setEstadoDespachando(TanqueJornada.ESTADO_DESPACHANDO);
    		  respuesta = dTanqueJornadaDao.recuperarRegistros(parametros);
    		  if (respuesta.estado == false) {
    	    	  throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
    	      } else {
    	    		  TanqueJornada eTanque = (TanqueJornada) respuesta.contenido.carga.get(0);
    	    		  despacho.setIdTanque(eTanque.getIdTanque());
    	      }
    	  }
      }

      descripcion_tanque=columnas[8];
      /*if(descripcion_tanque!=null && !descripcion_tanque.isEmpty()){
          argumentosListar=new ParametrosListar();
          argumentosListar.setValorBuscado(descripcion_tanque);
          argumentosListar.setFiltroEstacion(jornada.getEstacion().getId());
          respuesta=dTanqueDao.recuperarRegistros(argumentosListar);
          if (respuesta.estado == false) {
        	  throw new Exception("Error al obtener el identificador del tanque :"+descripcion_tanque);
          }else{
        	  if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
        		  tanque=(Tanque)respuesta.contenido.getCarga().get(0); 
        		  despacho.setIdTanque(tanque.getId());
              }else{
            	  throw new Exception("No se encontro en la estacion " + jornada.getEstacion().getNombre()+" el tanque con la descripcion "+descripcion_tanque);            	  
              } 
          }
      }else{
    	  throw new Exception("Descripcion del tanque es requerido. Fila:"+numeroLineas);    	  
      }*/
      //volumen observado
      
      sVolumen_observado=(columnas[9]==null)?"":columnas[9];
      sLectura_inicial=(columnas[10]==null)?"":columnas[10];
      sLectura_final=(columnas[11]==null)?"":columnas[11];
      
      if(sVolumen_observado.isEmpty() && (!sLectura_inicial.isEmpty() && !sLectura_final.isEmpty())){
    	  lectura_inicial=Float.parseFloat(sLectura_inicial);
    	  lectura_final=Float.parseFloat(sLectura_final);
    	  volumen_observado=lectura_final-lectura_inicial;
    	  despacho.setLecturaInicial(lectura_inicial);
    	  despacho.setLecturaFinal(lectura_final);
    	  despacho.setVolumenObservado(volumen_observado);    	  
      }      
      else if(!sVolumen_observado.isEmpty() && (sLectura_inicial.isEmpty() || sLectura_final.isEmpty())){    	   
    	  volumen_observado=Float.parseFloat(sVolumen_observado);
    	  lectura_inicial=0;
    	  lectura_final=volumen_observado;
    	  volumen_observado=lectura_final-lectura_inicial;
    	  despacho.setLecturaInicial(lectura_inicial);
    	  despacho.setLecturaFinal(lectura_final);
    	  despacho.setVolumenObservado(volumen_observado);
      }else if(sVolumen_observado.isEmpty() && (sLectura_inicial.isEmpty() || sLectura_final.isEmpty())){
    	  throw new Exception("El volumen Observado o las lecturas inicial y final son requeridas. Fila:"+numeroLineas);
      }else if(!sVolumen_observado.isEmpty() && (!sLectura_inicial.isEmpty() && !sLectura_final.isEmpty())){
    	  lectura_inicial=Float.parseFloat(sLectura_inicial);
    	  lectura_final=Float.parseFloat(sLectura_final);
    	  volumen_observado=lectura_final-lectura_inicial;
    	  despacho.setLecturaInicial(lectura_inicial);
    	  despacho.setLecturaFinal(lectura_final);
    	  despacho.setVolumenObservado(volumen_observado); 
      }   
      
      if(columnas[13]==null || columnas[13].isEmpty()){
    	  api_60 = 0;  
      }else{
    	  api_60= Float.parseFloat(columnas[13]);
      }      
      despacho.setApiCorregido(api_60);
      
      
      if(columnas[14]==null || columnas[14].isEmpty()){
    	  temperatura= 0;
      }else{
    	  temperatura= Float.parseFloat(columnas[14]);    	  
      }
      despacho.setTemperatura(temperatura);
      
      if(despacho.getApiCorregido()>0 && despacho.getTemperatura()>0) {
    	  factorCorreccionVolumen = Formula.calcularFactorCorreccion( despacho.getApiCorregido(), despacho.getTemperatura());
      }           
      
      if(columnas[12]!=null && !columnas[12].isEmpty()){
    	  factor_correccion= Float.parseFloat(columnas[12]);
    	  despacho.setFactorCorreccion(factor_correccion);
      }else {
    	  if(factorCorreccionVolumen>0){
    		  despacho.setFactorCorreccion((float) factorCorreccionVolumen);
    	  }
    	 
      }
      
      if(numero_columna>15){
          if(columnas[15]!=null && !columnas[15].isEmpty()){
        	  volumen_corregido= Float.parseFloat(columnas[15]);
        	  despacho.setVolumenCorregido(volumen_corregido);
          }else{
        	  if(despacho.getFactorCorreccion()>0){
            	  volumen_corregido=despacho.getFactorCorreccion()*despacho.getVolumenObservado();
            	  //volumen_corregido = (Math.round(volumen_corregido* 1000)/1000);
            	  volumen_corregido = (volumen_corregido* 1000)/1000;
            	  despacho.setVolumenCorregido(volumen_corregido);
        	  }
          }
      }else{
    	  if(despacho.getFactorCorreccion()>0){
        	  volumen_corregido=despacho.getFactorCorreccion()*despacho.getVolumenObservado();
        	  //volumen_corregido = Math.round(volumen_corregido* 1000.00)/1000;
        	  volumen_corregido = (volumen_corregido* 1000)/1000;
        	  despacho.setVolumenCorregido(volumen_corregido);
    	  }
      }
      
      despacho.setIdJornada(jornada.getId());
      despacho.setTipoRegistro(Despacho.ORIGEN_FICHERO);
      despacho.setEstado(Despacho.ESTADO_ACTIVO);
      despacho.setCodigoArchivoOrigen(Integer.parseInt(ClaveGenerada));
      //auditoria
      despacho.setActualizadoEl(Calendar.getInstance().getTime().getTime());
      despacho.setActualizadoPor(principal.getID()); 
      despacho.setCreadoEl(Calendar.getInstance().getTime().getTime());
      despacho.setCreadoPor(principal.getID());
      despacho.setIpActualizacion(direccionIp);
      despacho.setIpCreacion(direccionIp);
      respuesta= dDespacho.guardarRegistro(despacho);
      if (respuesta.estado==false){       
       //listaAlturasNoProcesadas.add(columnas[1]);
    	  throw new Exception("No se pudo registrar el despacho.");
      }       
      
     }
     numeroLineas++;
    }
//    if (borrar==1){
//     listaAlturas=null;
//    }
    //respuesta = dAforoCisterna.eliminarRegistros(listaIds, listaAlturas,Integer.parseInt(idTracto),Integer.parseInt(idCisterna),Integer.parseInt(idCompartimento));      
    //respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);    
    
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

private AuthenticatedUserDetails getCurrentUser() {
	return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
}

}
