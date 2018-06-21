package sgo.servicio;

import java.sql.Date;
import java.text.SimpleDateFormat;
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

import antlr.collections.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import sgo.datos.BitacoraDao;
import sgo.datos.ClienteDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.OperacionDao;
import sgo.datos.EnlaceDao;
import sgo.datos.PlantaDao;
import sgo.datos.TransportistaDao;
import sgo.datos.TransportistaOperacionDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Compartimento;
import sgo.entidad.Operacion;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.OperacionEtapaRuta;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Transportista;
import sgo.entidad.TransportistaOperacion;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class OperacionControlador {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora; 
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private DiaOperativoDao dDiaOperativo;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private TransportistaDao dTransportistas; 
 @Autowired
 private PlantaDao dPlantas; 
 @Autowired
 private TransportistaOperacionDao dTransportistaOperacion;
 
 
 @Autowired
 private ClienteDao dCliente;
 //
 private DataSourceTransactionManager transaccion;
 /** Nombre de la clase. */
 private static final String sNombreClase = "OperacionControlador";
 private static final String URL_GESTION_COMPLETA = "/admin/operacion";
 private static final String URL_GESTION_RELATIVA = "/operacion";
 private static final String URL_GUARDAR_COMPLETA = "/admin/operacion/crear";
 private static final String URL_GUARDAR_RELATIVA = "/operacion/crear";
 private static final String URL_LISTAR_COMPLETA = "/admin/operacion/listar";
 private static final String URL_LISTAR_RELATIVA = "/operacion/listar";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/operacion/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/operacion/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/operacion/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/operacion/recuperar";
 
//Agregado por req 9000002570==================== 
 private static final String URL_RECUPERAR_ETAPAS_COMPLETA = "/admin/operacion/recuperarEtapas";
 private static final String URL_RECUPERAR_ETAPAS_RELATIVA = "/operacion/recuperarEtapas";
 
 private static final String URL_GUARDAR_ETAPAS_COMPLETA = "/admin/operacion/crearEtapas";
 private static final String URL_GUARDAR_ETAPAS_RELATIVA = "/operacion/crearEtapas";
 
 private static final String URL_ELIMINAR_ETAPA_COMPLETA = "/admin/operacion/eliminarEtapas";
 private static final String URL_ELIMINAR_ETAPA_RELATIVA = "/operacion/eliminarEtapas";
 
 private static final String URL_CAMBIAR_ESTADO_ETAPA_RELATIVA = "/operacion/cambiaEstadoEtapa";
 //==============================================
 
 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/operacion/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/operacion/actualizarEstado";

 private HashMap<String, String> recuperarMapaValores(Locale locale) {
  HashMap<String, String> mapaValores = new HashMap<String, String>();
  try {
   mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
   mapaValores.put("ESTADO_INACTIVO", String.valueOf(Constante.ESTADO_INACTIVO));
   mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
   mapaValores.put("TEXTO_INACTIVO", gestorDiccionario.getMessage("sgo.estadoInactivo", null, locale));
   mapaValores.put("TEXTO_ACTIVO", gestorDiccionario.getMessage("sgo.estadoActivo", null, locale));
   mapaValores.put("TEXTO_TODOS", gestorDiccionario.getMessage("sgo.filtroTodos", null, locale));
   mapaValores.put("TITULO_AGREGAR_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioAgregar", null, locale));
   mapaValores.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar", null, locale));
   mapaValores.put("TITULO_DETALLE_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioVer", null, locale));
   mapaValores.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer", null, locale));
   mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
   mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));
   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));
   
   //Agregado por req 9000002570==============================================================
   mapaValores.put("ETIQUETA_BOTON_ETAPAS", gestorDiccionario.getMessage("sgo.etiquetaBotonEtapas", null, locale));
   //=========================================================================================
   
  } catch (Exception ex) {

  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  ArrayList<?> listadoClientes = null;
  ArrayList<?> listadoTransportistas = null;
  ArrayList<?> listadoPlantas = null;
  HashMap<String, String> mapaValores = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;

   parametros = new ParametrosListar();
   respuesta = dCliente.recuperarRegistros(parametros);
   if( respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noClientes", null, locale));
   }
   listadoClientes = (ArrayList<?>) respuesta.contenido.carga;
   
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   respuesta = dTransportistas.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   listadoTransportistas = (ArrayList<?>) respuesta.contenido.carga;
   
   //para devolver el listado de plantas
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   respuesta = dPlantas.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   listadoPlantas = (ArrayList<?>) respuesta.contenido.carga;
   
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "mantenimiento/operacion.jsp");
   vista.addObject("vistaJS", "mantenimiento/operacion.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("listadoClientes", listadoClientes);
   vista.addObject("listadoTransportistas", listadoTransportistas);
   vista.addObject("listadoPlantas", listadoPlantas);
   
   vista.addObject("mapaValores", mapaValores);
  } catch (Exception ex) {

  }
  return vista;
 }

 @RequestMapping(value = URL_LISTAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Recuperar parametros
   parametros = new ParametrosListar();
   if (httpRequest.getParameter("paginacion") != null) {
    parametros.setPaginacion(Integer.parseInt(httpRequest.getParameter("paginacion")));
   }

   if (httpRequest.getParameter("registrosxPagina") != null) {
    parametros.setRegistrosxPagina(Integer.parseInt(httpRequest.getParameter("registrosxPagina")));
   }

   if (httpRequest.getParameter("inicioPagina") != null) {
    parametros.setInicioPaginacion(Integer.parseInt(httpRequest.getParameter("inicioPagina")));
   }

   if (httpRequest.getParameter("campoOrdenamiento") != null) {
    parametros.setCampoOrdenamiento((httpRequest.getParameter("campoOrdenamiento")));
   }

   if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
    parametros.setSentidoOrdenamiento((httpRequest.getParameter("sentidoOrdenamiento")));
   }

   if (httpRequest.getParameter("valorBuscado") != null) {
    parametros.setValorBuscado((httpRequest.getParameter("valorBuscado")));
   }
   if (httpRequest.getParameter("txtFiltro") != null) {
    parametros.setTxtFiltro((httpRequest.getParameter("txtFiltro")));
   }
   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroEstado") != null) {
    parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
   }

   if (httpRequest.getParameter("idCliente") != null) {
    parametros.setIdCliente(Integer.parseInt(httpRequest.getParameter("idCliente")));
   }
   // Recuperar registros
   respuesta = dOperacion.recuperarRegistros(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_RECUPERAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistro(int ID, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Recuperar el registro
   respuesta = dOperacion.recuperarRegistro(ID);
   // Verifica el resultado de la accion
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
//Agregado por req 9000002570======================================================
 @RequestMapping(value = URL_RECUPERAR_ETAPAS_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaEtapas(int ID, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_ETAPAS_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Recuperar el registro
   respuesta = dOperacion.recuperarEtapas(ID);
   // Verifica el resultado de la accion
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_GUARDAR_ETAPAS_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistrosEtapas(@RequestBody Operacion eOperacion, HttpServletRequest peticionHttp, Locale locale) {
	 RespuestaCompuesta respuesta = null;
	 AuthenticatedUserDetails principal = null;
	 Bitacora eBitacora = null;
	 TransactionDefinition definicionTransaccion = null;
	 TransactionStatus estadoTransaccion = null;
	 
	 try {
		 
		 // Inicia la transaccion
		 this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
		 definicionTransaccion = new DefaultTransactionDefinition();
		 estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		 eBitacora = new Bitacora();
		 // Recuperar el usuario actual
		 principal = this.getCurrentUser();
		 // Recuperar el enlace de la accion
		 respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_ETAPAS_COMPLETA);
		 if (respuesta.estado == false) {
			 throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
		 }
		 Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
		 // Verificar si cuenta con el permiso necesario
		 if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
			 throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
		 }
		 
//		 respuesta = dOperacion.eliminarEtapas(eOperacion.getId());
		 
		 ArrayList<OperacionEtapaRuta> etapas = eOperacion.getEtapas();
		 for(OperacionEtapaRuta etapa : etapas){
			 if(etapa.getId() == null || etapa.getId() == 0){
				 respuesta = dOperacion.guardarRegistroEtapa(etapa);
			 }else{
				 respuesta = dOperacion.actualizarRegistroEtapa(etapa);
			 }
		 }
		 
   	    String fechaActualizacionFormateada="";
		Date fechaActualizacion = new Date(Calendar.getInstance().getTime().getTime());
		SimpleDateFormat formateadorFecha = null;
		
		formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
		respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso",
				 	new Object[] { fechaActualizacionFormateada.substring(0, 10), fechaActualizacionFormateada.substring(10), principal.getIdentidad() }, locale);
       this.transaccion.commit(estadoTransaccion);
	 }catch (Exception ex) {
	   //ex.printStackTrace();
	   Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
	   respuesta.estado = false;
	   respuesta.contenido = null;
	   respuesta.mensaje = ex.getMessage();
	   this.transaccion.rollback(estadoTransaccion);
	 }
	 return respuesta;
 }
 
 @RequestMapping(value = URL_ELIMINAR_ETAPA_RELATIVA, method = RequestMethod.GET) 
	public @ResponseBody RespuestaCompuesta eliminarRegistro(Integer ID, HttpServletRequest peticionHttp, Locale locale){
	 
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		Bitacora eBitacora = null;
		
		try {
			
			 // Inicia la transaccion
			 this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
			 definicionTransaccion = new DefaultTransactionDefinition();
			 estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			 eBitacora = new Bitacora();
			 // Recuperar el usuario actual
			 principal = this.getCurrentUser();
			 // Recuperar el enlace de la accion
			 respuesta = dEnlace.recuperarRegistro(URL_ELIMINAR_ETAPA_COMPLETA);
			 if (respuesta.estado == false) {
				 throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
			 }
			 Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
			 // Verificar si cuenta con el permiso necesario
			 if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
				 throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
			 }
			 
			int idEtapaTrans = dOperacion.obtenerEtapaTransporte(ID);
			
			if(idEtapaTrans == 0){
				respuesta = dOperacion.eliminarEtapas(ID);
				
				String fechaActualizacionFormateada="";
				Date fechaActualizacion = new Date(Calendar.getInstance().getTime().getTime());
				SimpleDateFormat formateadorFecha = null;
				
				formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
			  	respuesta.mensaje=gestorDiccionario.getMessage("sgo.eliminarExitoso",new Object[] {fechaActualizacionFormateada.substring(0, 10),fechaActualizacionFormateada.substring(10),principal.getIdentidad() },locale);;
			  	this.transaccion.commit(estadoTransaccion);
			}else{
				respuesta.estado=false;
				respuesta.contenido = null;
				respuesta.mensaje= "YA_ASOCIADO";
			}
		} catch (Exception ex){
			//ex.printStackTrace();
			Utilidades.gestionaError(ex, sNombreClase, "eliminarRegistro");
			this.transaccion.rollback(estadoTransaccion);
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	 
 }
 
 @RequestMapping(value = URL_CAMBIAR_ESTADO_ETAPA_RELATIVA, method = RequestMethod.GET) 
	public @ResponseBody RespuestaCompuesta cambiarEstadoEtapa(Integer ID, Integer ESTADO, HttpServletRequest peticionHttp, Locale locale){
	 
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		Bitacora eBitacora = null;
		
		try {
			
			 // Inicia la transaccion
			 this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
			 definicionTransaccion = new DefaultTransactionDefinition();
			 estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			 eBitacora = new Bitacora();
			 // Recuperar el usuario actual
			 principal = this.getCurrentUser();
			 // Recuperar el enlace de la accion
			 respuesta = dEnlace.recuperarRegistro(URL_ELIMINAR_ETAPA_COMPLETA);
			 if (respuesta.estado == false) {
				 throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
			 }
			 Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
			 // Verificar si cuenta con el permiso necesario
			 if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
				 throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
			 }

			respuesta = dOperacion.cambiarEstadoEtapa(ID, ESTADO);
			
			String fechaActualizacionFormateada="";
			Date fechaActualizacion = new Date(Calendar.getInstance().getTime().getTime());
			SimpleDateFormat formateadorFecha = null;
			
			formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
		  	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {fechaActualizacionFormateada.substring(0, 10),fechaActualizacionFormateada.substring(10),principal.getIdentidad() },locale);
		  	this.transaccion.commit(estadoTransaccion);

		} catch (Exception ex){
			//ex.printStackTrace();
			Utilidades.gestionaError(ex, sNombreClase, "cambiarEstadoRegistro");
			this.transaccion.rollback(estadoTransaccion);
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	 
 }
 //===============================================================================

 @RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody Operacion eOperacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  ArrayList<String> para = new ArrayList<String>();
  ArrayList<String> cc = new ArrayList<String>();
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
 //valida los datos que vienen del formulario
   Respuesta validacion = Utilidades.validacionXSS(eOperacion, gestorDiccionario, locale);
   if (validacion.estado == false) {
	   respuesta = new RespuestaCompuesta();
	   throw new Exception(validacion.valor);
   }
   // Actualiza los datos de auditoria local
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   eOperacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eOperacion.setActualizadoPor(principal.getID());
   eOperacion.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eOperacion.setCreadoPor(principal.getID());
   eOperacion.setIpActualizacion(direccionIp);
   eOperacion.setIpCreacion(direccionIp);
   // Para asignar el estado activo cuando es un registro nuevo.
   eOperacion.setEstado(Constante.ESTADO_ACTIVO);
   
 //esto para armar el arraylist de los correos PARA
   if(!eOperacion.getCorreoPara().trim().isEmpty()){
	   String[] temp = eOperacion.getCorreoPara().split(";");
	   for (String correoPara : temp) {
			if(correoPara.trim().length() > 0){
				para.add(correoPara.trim());
				System.out.println("correoPara.trim():"+correoPara.trim());
			}
	   }
	   //Valido los correos PARA
	   if(!Utilidades.validaEmail(para)){
		   throw new Exception("El campo Para es incorrecto. Favor verifique.");
	   }
   } else {
		throw new Exception("El campo Para se encuentra vacío. Favor verifique.");
   }
   
   //esto para armar el arraylist de los correos CC
   if(!eOperacion.getCorreoCC().trim().isEmpty()){
		String[] temp = eOperacion.getCorreoCC().split(";");
		for (String correoCC : temp) {
			if(correoCC.trim().length() > 0){
				cc.add(correoCC.trim());
				System.out.println("correoCC.trim():"+correoCC.trim());
			}
	    }
		//Valido los correos CC
		if(!Utilidades.validaEmail(cc)){
		   throw new Exception("El campo CC es incorrecto. Favor verifique.");
		}
	}
   
 //validamos que los transportistas no se repitan
   if(!eOperacion.getTransportistas().isEmpty()){
	   for(int i= 0; i < eOperacion.getTransportistas().size(); i++){
		   for (int j = 0; j < eOperacion.getTransportistas().size(); j++){
			   if(i != j){
				   if(eOperacion.getTransportistas().get(i).getId() == eOperacion.getTransportistas().get(j).getId()){
					   throw new Exception(gestorDiccionario.getMessage("sgo.operacion.seleccionarUnRegistro", null, locale));
				   }
			   }
		   }
	   }
   } else {
		throw new Exception("No hay Transportistas para la Operación. Favor verifique.");
   }
   
   respuesta = dOperacion.guardarRegistro(eOperacion);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   int OperacionCreada = Integer.parseInt(respuesta.valor);
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject
                                             // via @Autowired
   ContenidoAuditoria = mapper.writeValueAsString(eOperacion);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(OperacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eOperacion.getCreadoEl());
   eBitacora.setRealizadoPor(eOperacion.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   TransportistaOperacion eTransportistaOperacion = null;
   for(Transportista transportista : eOperacion.getTransportistas()){
	   eTransportistaOperacion = new TransportistaOperacion();
	   eTransportistaOperacion.setIdTransportista(transportista.getId());
	   eTransportistaOperacion.setIdOperacion(OperacionCreada);
	   respuesta = dTransportistaOperacion.guardarRegistro(eTransportistaOperacion);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    }
   }
	   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso",
     new Object[] { eOperacion.getFechaCreacion().substring(0, 9), eOperacion.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
   this.transaccion.rollback(estadoTransaccion);
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ACTUALIZAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarRegistro(@RequestBody Operacion eOperacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  ParametrosListar parametros = null;
  String direccionIp = "";
  ArrayList<String> para = new ArrayList<String>();
  ArrayList<String> cc = new ArrayList<String>();
  try {
	// Inicia la transaccion
    this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
    definicionTransaccion = new DefaultTransactionDefinition();
    estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
    eBitacora = new Bitacora();
	//valida los datos que vienen del formulario
    Respuesta validacion = Utilidades.validacionXSS(eOperacion, gestorDiccionario, locale);
    if (validacion.estado == false) {
      respuesta = new RespuestaCompuesta();
      throw new Exception(validacion.valor);
    }
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_COMPLETA);
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
   eOperacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eOperacion.setActualizadoPor(principal.getID());
   eOperacion.setIpActualizacion(direccionIp);
   
   //esto para armar el arraylist de los correos PARA
   if(!eOperacion.getCorreoPara().trim().isEmpty()){
	   String[] temp = eOperacion.getCorreoPara().split(";");
	   for (String correoPara : temp) {
			if(correoPara.trim().length() > 0){
				para.add(correoPara.trim());
			}
	   }
	   //Valido los correos PARA
	   if(!Utilidades.validaEmail(para)){
		   throw new Exception("El campo Para es incorrecto. Favor verifique.");
	   }
   } else {
		throw new Exception("El campo Para se encuentra vacío. Favor verifique.");
   }
   
   //esto para armar el arraylist de los correos CC
   if(!eOperacion.getCorreoCC().trim().isEmpty()){
		String[] temp = eOperacion.getCorreoCC().split(";");
		for (String correoCC : temp) {
			if(correoCC.trim().length() > 0){
				cc.add(correoCC.trim());
			}
	    }
		//Valido los correos CC
		if(!Utilidades.validaEmail(cc)){
		   throw new Exception("El campo CC es incorrecto. Favor verifique.");
		}
	} 
   
 //validamos que los transportistas no se repitan
   if(!eOperacion.getTransportistas().isEmpty()){
	   for(int i= 0; i < eOperacion.getTransportistas().size(); i++){
		   for (int j = 0; j < eOperacion.getTransportistas().size(); j++){
			   if(i != j){
				   if(eOperacion.getTransportistas().get(i).getId() == eOperacion.getTransportistas().get(j).getId()){
					   throw new Exception(gestorDiccionario.getMessage("sgo.operacion.seleccionarUnRegistro", null, locale));
				   }
			   }
		   }
	   }
   } else {
		throw new Exception("No hay Transportistas para la Operación. Favor verifique.");
   }
   
   //valido si el registro no está en datos históricos
   //TODO 
   //CORREGIR ESTA VALIDACION BOTA ERRORES
   /*
   ParametrosListar parametros2 = new ParametrosListar();
   parametros2.setIdOperacion(eOperacion.getId());
   respuesta = dDiaOperativo.validaRegistrosExistentes(parametros2);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   if (respuesta.contenido.carga.size() > 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.noModificarDatosHistoricos", null, locale)); 
   }
   */

   respuesta = dOperacion.actualizarRegistro(eOperacion);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
   eBitacora.setTabla(OperacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eOperacion.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eOperacion));
   eBitacora.setRealizadoEl(eOperacion.getActualizadoEl());
   eBitacora.setRealizadoPor(eOperacion.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setIdOperacion(eOperacion.getId());
   respuesta = dTransportistaOperacion.recuperarRegistros(parametros);
   
   if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
  
   //esto para eliminar los registros
   for(Object elemento: respuesta.contenido.carga){
	   TransportistaOperacion eTransportistaOperacion = (TransportistaOperacion) elemento;
	   respuesta = dTransportistaOperacion.eliminarRegistro(eTransportistaOperacion.getId());
   }

  //esto para agregar los registros
   TransportistaOperacion eTransportistaOperacion = null;
   for(Transportista transportista : eOperacion.getTransportistas()){
	   eTransportistaOperacion = new TransportistaOperacion();
	   eTransportistaOperacion.setIdTransportista(transportista.getId());
	   eTransportistaOperacion.setIdOperacion(eOperacion.getId());
	   respuesta = dTransportistaOperacion.guardarRegistro(eTransportistaOperacion);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    }
   }
		   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eOperacion.getFechaActualizacion().substring(0, 9), eOperacion.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);;
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "actualizarRegistro");
   this.transaccion.rollback(estadoTransaccion);
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Operacion eEntidad, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dOperacion.getDataSource());
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
   eEntidad.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eEntidad.setActualizadoPor(principal.getID());
   eEntidad.setIpActualizacion(direccionIp);
   respuesta = dOperacion.ActualizarEstadoRegistro(eEntidad);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(OperacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eEntidad.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eEntidad));
   eBitacora.setRealizadoEl(eEntidad.getActualizadoEl());
   eBitacora.setRealizadoPor(eEntidad.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso",
     new Object[] { eEntidad.getFechaActualizacion().substring(0, 9), eEntidad.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
   ;
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "actualizarEstadoRegistro");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
   this.transaccion.rollback(estadoTransaccion);
  }
  return respuesta;
 }

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
}