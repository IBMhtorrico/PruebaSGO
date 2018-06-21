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

import sgo.datos.AutorizacionDao;
import sgo.datos.AutorizacionEjecutadaDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CargaTanqueDao;
import sgo.datos.ClienteDao;
import sgo.datos.DescargaCisternaDao;
import sgo.datos.DescargaCompartimentoDao;
import sgo.datos.DetalleTransporteDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EstacionDao;
import sgo.datos.EventoDao;
import sgo.datos.JornadaDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.TanqueDao;
import sgo.datos.TanqueJornadaDao;
import sgo.datos.TransporteDao;
import sgo.entidad.Autorizacion;
import sgo.entidad.AutorizacionEjecutada;
import sgo.entidad.Bitacora;
import sgo.entidad.CargaTanque;
import sgo.entidad.Contenido;
import sgo.entidad.DescargaCisterna;
import sgo.entidad.DescargaCompartimento;
import sgo.entidad.DescargaResumen;
import sgo.entidad.DetalleTransporte;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.Estacion;
import sgo.entidad.Evento;
import sgo.entidad.Jornada;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.entidad.Transporte;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DescargaControlador {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora; 
 @Autowired
 private EnlaceDao dEnlace;
@Autowired
private AutorizacionEjecutadaDao dAutorizacionEjecutada;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private DiaOperativoDao dDiaOperativo;
 @Autowired
 private PlanificacionDao dPlanificacion;
 @Autowired
 private ClienteDao dCliente;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private TanqueDao dTanque;
 @Autowired
 private EstacionDao dEstacion;
 @Autowired
 private ProductoDao dProducto;
 @Autowired
 private DiaOperativoControlador DiaOperativoControlador;
 @Autowired
 private CargaTanqueDao dCargaTanque;
 @Autowired
 private DescargaCisternaDao dDescarga;
 @Autowired
 private DescargaCompartimentoDao dDescargaCompartimentoDao;
 @Autowired
 private TransporteDao dTransporte;
 @Autowired
 private EventoDao dEvento;
 @Autowired
 private DetalleTransporteDao dDetalleTransporte;
 @Autowired
 private AutorizacionDao dAutorizacion;
 @Autowired
 private TanqueJornadaDao dTanqueJornadaDao;
 @Autowired
 private JornadaDao dJornadaDao;
 /** Nombre de la clase. */
 private static final String sNombreClase = "DescargaControlador";
 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/descarga";
 private static final String URL_GESTION_RELATIVA = "/descarga";

 private static final String URL_LISTAR_CARGA_COMPLETA = "/admin/descarga/listar-carga";
 private static final String URL_LISTAR_CARGA_RELATIVA = "/descarga/listar-carga";

 private static final String URL_RECUPERAR_CARGA_COMPLETA = "/admin/descarga/recuperar-carga";
 private static final String URL_RECUPERAR_CARGA_RELATIVA = "/descarga/recuperar-carga";

 private static final String URL_GUARDAR_CARGA_COMPLETA = "/admin/descarga/crear-carga";
 private static final String URL_GUARDAR_CARGA_RELATIVA = "/descarga/crear-carga";

 private static final String URL_ACTUALIZAR_CARGA_COMPLETA = "/admin/descarga/actualizar-carga";
 private static final String URL_ACTUALIZAR_CARGA_RELATIVA = "/descarga/actualizar-carga";
 
 private static final String URL_LISTAR_DESCARGA_COMPLETA = "/admin/descarga/listar";
 private static final String URL_LISTAR_DESCARGA_RELATIVA = "/descarga/listar";
 
 private static final String URL_CREAR_DESCARGA_COMPLETA = "/admin/descarga/crear";
 private static final String URL_CREAR_DESCARGA_RELATIVA = "/descarga/crear";

 private static final String URL_ACTUALIZAR_DESCARGA_COMPLETA = "/admin/descarga/actualizar";
 private static final String URL_ACTUALIZAR_DESCARGA_RELATIVA = "/descarga/actualizar";
 
 private static final String URL_RECUPERAR_DESCARGA_COMPLETA = "/admin/descarga/recuperar-descarga";
 private static final String URL_RECUPERAR_DESCARGA_RELATIVA = "/descarga/recuperar-descarga";
 


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
   mapaValores.put("TITULO_LISTADO_REGISTROS",gestorDiccionario.getMessage("sgo.tituloFormularioListado", null, locale));
   //
   mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar", null, locale));

   mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer", null, locale));
   mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar", null, locale));

   mapaValores.put("ETIQUETA_BOTON_CANCELAR",gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR",gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
   mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));

   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));

   mapaValores.put("TITULO_INICIAL_DESCARGA", gestorDiccionario.getMessage("sgo.tituloInicialDescarga", null, locale));
   mapaValores.put("TITULO_DETALLE_DESCARGA", gestorDiccionario.getMessage("sgo.tituloDetalleDescarga", null, locale));
   mapaValores.put("TITULO_AGREGAR_DESCARGA_TANQUE", gestorDiccionario.getMessage("sgo.tituloAgregarDescargaTanque", null, locale));
   mapaValores.put("TITULO_MODIFICAR_DESCARGA_TANQUE", gestorDiccionario.getMessage("sgo.tituloModificarDescargaTanque", null, locale));
   mapaValores.put("ETIQUETA_BOTON_VALIDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonValidar", null, locale));
   mapaValores.put("TEXTO_NO_ESTACIONES_OPERACION", gestorDiccionario.getMessage("sgo.operacionNoEstaciones", null, locale));
   mapaValores.put("ETIQUETA_BOTON_OBSERVACIONES", gestorDiccionario.getMessage("sgo.etiquetaBotonMostrarObservaciones", null, locale));
   
  } catch (Exception ex) {

  }
  return mapaValores;
 }

 @RequestMapping(value = URL_GESTION_RELATIVA, method = RequestMethod.GET)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  ArrayList<?> listaOperaciones = null;
  ArrayList<?> listaProductos = null;
  ArrayList<?> listaEstaciones = null;
  ArrayList<?> listadoTanques = null;
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  HashMap<String, String> mapaValores = null;
  String separador="";
  String separadorGrupo="";
  String elemento="";   
  String grupoOperacion="";
  Autorizacion eAutorizacion = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);   
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null,locale));
   }
   //Generar el menu
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado",  null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   parametros.setIdCliente(principal.getCliente().getId());
   parametros.setIdOperacion(principal.getOperacion().getId());
   respuesta = dOperacion.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noListadoOperaciones", null, locale));
   }
   listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   respuesta = dProducto.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noListadoProductos", null, locale));
   }
   listaProductos = (ArrayList<?>) respuesta.contenido.carga;
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   respuesta = dEstacion.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noListadoEstaciones", null, locale));
   }
   listaEstaciones = (ArrayList<?>) respuesta.contenido.carga;

   respuesta = dAutorizacion.recuperarAutorizacionesPorCodigoInterno(Autorizacion.TIPO_INGRESAR_VOLUMEN_DIRECTO);
   if (respuesta.estado ==false){
    
   }else {
    eAutorizacion= (Autorizacion) respuesta.contenido.carga.get(0);
   }

   String estaciones="";
   Estacion eEstacion=null;
   int idOperacion = 0;
   for(int contador=0;contador<listaEstaciones.size();contador++){
    eEstacion = (Estacion)listaEstaciones.get(contador);
    if (idOperacion != eEstacion.getIdOperacion()){
      grupoOperacion=separadorGrupo+eEstacion.getIdOperacion() + " : [";
      separadorGrupo="],";
      separador="";
    } else {
      grupoOperacion="";
    }
    idOperacion=eEstacion.getIdOperacion();
    elemento =  grupoOperacion+separador+ "{id:"+eEstacion.getId()+", nombre:'"+ eEstacion.getNombre() + "',metodo:'"+ eEstacion.getMetodoDescarga()+"'}";
    estaciones = estaciones+elemento;
    separador=",";
  }
   if (listaEstaciones.size()>0){
    estaciones+="]";
   }
   
   //
   parametros = new ParametrosListar();
   parametros.setCampoOrdenamiento(TanqueDao.NOMBRE_CAMPO_ID_ESTACION);
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   respuesta = dTanque.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noListadoTanques", null, locale));
   }
   listadoTanques = (ArrayList<?>) respuesta.contenido.carga;
   
   String tanques="";
   Tanque eTanque=null;
   separadorGrupo="";
   elemento="";
   separador="";
   int idEstacion = 0;
   for(int contador=0;contador<listadoTanques.size();contador++){
    eTanque = (Tanque)listadoTanques.get(contador);
    if (idEstacion != eTanque.getIdEstacion()){
      grupoOperacion=separadorGrupo+eTanque.getIdEstacion() + " : [";
      separadorGrupo="],";
      separador="";
    } else {
      grupoOperacion="";
    }
    idEstacion=eTanque.getIdEstacion();
    elemento =  grupoOperacion+separador+ "{id:"+eTanque.getId()+", nombre:'"+ eTanque.getDescripcion() + "'}";
    tanques = tanques+elemento;
    separador=",";
  }
   if (listadoTanques.size()>0){
    tanques+="]";
   }
   //
   mapaValores = recuperarMapaValores(locale);
   mapaValores.put("AUTORIZACION_DESCARGA", eAutorizacion.getNombre());
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "descarga/descarga.jsp");
   vista.addObject("vistaJS", "descarga/descarga.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("mapaValores", mapaValores);
   vista.addObject("operaciones", listaOperaciones);
   vista.addObject("productos", listaProductos);
   vista.addObject("estaciones", estaciones);
   vista.addObject("tanques", tanques);
   vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
  } catch (Exception ex) {

  }
  return vista;
 }

 @RequestMapping(value = URL_LISTAR_CARGA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest,
   Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_CARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);   
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null,locale));
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
   if (httpRequest.getParameter("filtroEstacion") != null) {
    parametros.setFiltroEstacion(Integer.valueOf(httpRequest.getParameter("filtroEstacion")));
   }
   if (httpRequest.getParameter("filtroFechaPlanificada") != null) {
    parametros.setFiltroFechaPlanificada((httpRequest.getParameter("filtroFechaPlanificada")));
   }
   // Recuperar registros
   respuesta = dCargaTanque.recuperarRegistros(parametros);
   for(int i = 0; i < respuesta.contenido.carga.size(); i++){
	   CargaTanque eCargaTanque = (CargaTanque) respuesta.contenido.carga.get(i);
	   RespuestaCompuesta respuestaTanque = dTanque.recuperarRegistro(eCargaTanque.getIdTanque());
	   if (respuestaTanque.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
	   }
	   Tanque eTanque = (Tanque) respuestaTanque.contenido.carga.get(0);
	   eCargaTanque.setTanque(eTanque);
   }
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_RECUPERAR_CARGA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistro(int ID, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_CARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null,locale));
   }
   respuesta = dCargaTanque.recuperarRegistro(ID);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   //ex.printStackTrace();
	  Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistro");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_GUARDAR_CARGA_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody CargaTanque eCargaTanque,
   HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager( dCargaTanque.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   principal = this.getCurrentUser();  
   respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_CARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   eCargaTanque.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eCargaTanque.setActualizadoPor(principal.getID());
   eCargaTanque.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eCargaTanque.setCreadoPor(principal.getID());
   eCargaTanque.setIpActualizacion(direccionIp);
   eCargaTanque.setIpCreacion(direccionIp);
   respuesta = dCargaTanque.guardarRegistro(eCargaTanque);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   ContenidoAuditoria = mapper.writeValueAsString(eCargaTanque);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_CARGA_COMPLETA);
   eBitacora.setTabla(CargaTanqueDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eCargaTanque.getCreadoEl());
   eBitacora.setRealizadoPor(eCargaTanque.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eCargaTanque.getFechaCreacion().substring(0, 9),eCargaTanque.getFechaCreacion().substring(10),principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   this.transaccion.rollback(estadoTransaccion);
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ACTUALIZAR_CARGA_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarRegistro(@RequestBody CargaTanque eCargaTanque,
   HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dCargaTanque.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_CARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",
      null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null,
      locale));
   }
   // Auditoria local (En el mismo registro)
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   eCargaTanque.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eCargaTanque.setActualizadoPor(principal.getID());
   eCargaTanque.setIpActualizacion(direccionIp);
   respuesta = dCargaTanque.actualizarRegistro(eCargaTanque);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_CARGA_COMPLETA);
   eBitacora.setTabla(CargaTanqueDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eCargaTanque.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eCargaTanque));
   eBitacora.setRealizadoEl(eCargaTanque.getActualizadoEl());
   eBitacora.setRealizadoPor(eCargaTanque.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eCargaTanque.getFechaActualizacion().substring(0, 9),eCargaTanque.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);
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
 
 @RequestMapping(value = URL_LISTAR_DESCARGA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarDescargas(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_CARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);   
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null,locale));
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
   if (httpRequest.getParameter("filtroCargaTanque") != null) {
    parametros.setFiltroCargaTanque(Integer.valueOf(httpRequest.getParameter("filtroCargaTanque")));
   }
   // Recuperar registros
   respuesta = dDescarga.recuperarRegistros(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   //ex.printStackTrace();
	  Utilidades.gestionaError(ex, sNombreClase, "recuperarDescargas");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_CREAR_DESCARGA_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody RespuestaCompuesta guardarDescarga(@RequestBody DescargaCisterna eDescarga, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager( dDescarga.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   principal = this.getCurrentUser();  
   respuesta = dEnlace.recuperarRegistro(URL_CREAR_DESCARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   //validacion segun tipo tanque
   ParametrosListar argumentosListar;
   int tipoTanque=eDescarga.getTipoTanque();
   argumentosListar=new ParametrosListar();
   DescargaCompartimento descargaCompartimento=null;
   TanqueJornada tanqueJornada=null;
   //if(tipoTanque==Tanque.TIPO_VIRTUAL){//valida que el producto en la tabla descarga_cisterna
	   //argumentosListar.setIdTransporte(eDescarga.getIdTransporte());
	   argumentosListar.setFiltroCargaTanque(eDescarga.getIdCargaTanque());
	   respuesta=dDescarga.recuperarRegistros(argumentosListar);
	   if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
		   DescargaResumen descargaResumen=(DescargaResumen)respuesta.contenido.getCarga().get(0);
		   if(descargaResumen!=null){
			   //obtener descarga_compartimiento
			   argumentosListar=new ParametrosListar();
			   argumentosListar.setFiltroDescargaCisterna(descargaResumen.getId());
			   respuesta=dDescargaCompartimentoDao.recuperarRegistros(argumentosListar);
			   if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
				   descargaCompartimento=(DescargaCompartimento)respuesta.contenido.getCarga().get(0);				   
			   }
		   }
	   }
   //se quita validacion de tanque administrado
   //kanb
   /*}else{//tanque administrativo	  
	   //obtener id_jornada
	   respuesta=dDiaOperativo.recuperarRegistro(eDescarga.getIdDoperativo());
	   if (respuesta.estado==false) {
		    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   DiaOperativo  diaOperativo= (DiaOperativo)respuesta.contenido.getCarga().get(0);	   
	   argumentosListar.setFiltroFechaJornada(Utilidades.convierteDateAString(diaOperativo.getFechaOperativa(),"yyyy-MM-dd"));
	   argumentosListar.setFiltroEstacion(eDescarga.getIdEstacion());
	   respuesta=dJornadaDao.recuperarRegistros(argumentosListar);
	   if (respuesta.estado==false) {
		    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   
	   if(respuesta.contenido.getCarga().size() > 0){
		   Jornada jornada=(Jornada)respuesta.contenido.getCarga().get(0);
		   if( jornada!=null){
			   argumentosListar=new ParametrosListar();
			   argumentosListar.setIdJornada(jornada.getId());
			   argumentosListar.setIdTanque(eDescarga.getIdTanque());
			   respuesta=dTanqueJornadaDao.recuperarRegistros(argumentosListar);
			   if (respuesta.estado==false) {
				    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			   }
			   if(respuesta.contenido.getCarga()!=null && respuesta.contenido.getCarga().size()>0){
				   tanqueJornada=(TanqueJornada)respuesta.contenido.getCarga().get(0);
				  if((tanqueJornada.getJornada().getEstado() != Jornada.ESTADO_ABIERTO) && (tanqueJornada.getJornada().getEstado() != Jornada.ESTADO_REGISTRADO)){
					  throw new Exception("Solo se permite la descarga cuando la jornada del dia se encuentra en estado abierto o registrado");
				  }
			   }else{
				   throw new Exception("No existe una jornada para el producto y tanque seleccionado ");
			   }
		   }
	   } else {
		   throw new Exception("Solo se permite la descarga cuando la jornada del dia se encuentra en estado abierto o registrado");
	   }
   }*/   
   eDescarga.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDescarga.setActualizadoPor(principal.getID());
   eDescarga.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eDescarga.setCreadoPor(principal.getID());
   eDescarga.setIpActualizacion(direccionIp);
   eDescarga.setIpCreacion(direccionIp);
   eDescarga.setEstado(DescargaCisterna.ESTADO_ACTIVO);
   for(DescargaCompartimento elemento: eDescarga.getCompartimentos()){
	   //validar tipo virtual el producto descargado es igual al producto de la primera cisterna
	   if(tipoTanque==Tanque.TIPO_VIRTUAL && descargaCompartimento!=null){
		   if(elemento.getIdProducto()!=descargaCompartimento.getIdProducto()){
			   throw new Exception("Solo se permite la descarga del producto: "+descargaCompartimento.getProducto().getNombre());
		   }
	   }else if(tipoTanque == Tanque.TIPO_ADMINISTRADO && tanqueJornada!=null){
		   if(elemento.getIdProducto()!=tanqueJornada.getIdProducto()){
			   throw new Exception("Solo se permite la descarga del producto: "+tanqueJornada.getProducto().getNombre()+ " de la jornada");
		   }
	   }	   
	   if(elemento.getTipoMetodo()==DescargaCisterna.METODO_WINCHA){
		   if(elemento.getAlturaCompartimento()!= elemento.getAlturaFlecha()){
			   eDescarga.setEstado(DescargaCisterna.ESTADO_OBSERVADO);
			   break;
			}else {
			   eDescarga.setEstado(DescargaCisterna.ESTADO_ACTIVO);
			}
	   }

   }
   if (eDescarga.getVolumenExcedenteCorregido()< 0){
    eDescarga.setEstado(DescargaCisterna.ESTADO_OBSERVADO);
   }
   respuesta = dDescarga.guardarRegistro(eDescarga);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   for(Evento elemento: eDescarga.getEventos()){
    elemento.setIdRegistro(Integer.parseInt(ClaveGenerada));
    elemento.setTipoRegistro(Constante.EVENTO_ORIGEN_DESCARGA);
    elemento.setCreadoEl(Calendar.getInstance().getTime().getTime());
    elemento.setCreadoPor(principal.getID());
    elemento.setIpCreacion(direccionIp);
    if (elemento.getId()>0){
     respuesta = dEvento.guardarRegistro(elemento);
    }else {
    respuesta = dEvento.actualizarRegistro(elemento);
    }
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   ContenidoAuditoria = mapper.writeValueAsString(eDescarga);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_CARGA_COMPLETA);
   eBitacora.setTabla(CargaTanqueDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eDescarga.getCreadoEl());
   eBitacora.setRealizadoPor(eDescarga.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   
   if (eDescarga.getIdAutorizacionEjecutada()>0) {
    AutorizacionEjecutada eAutorizacionEjecutada = new AutorizacionEjecutada();
    eAutorizacionEjecutada.setId(eDescarga.getIdAutorizacionEjecutada());
    eAutorizacionEjecutada.setIdRegistro(Integer.parseInt(ClaveGenerada));
    dAutorizacionEjecutada.actualizarRegistroVinculado(eAutorizacionEjecutada);
   }
   
   for(Evento elemento: eDescarga.getEventos()){
    elemento.setIdRegistro(Integer.parseInt(ClaveGenerada));
    elemento.setTipoRegistro(Constante.EVENTO_ORIGEN_DESCARGA);
    elemento.setCreadoEl(Calendar.getInstance().getTime().getTime());
    elemento.setCreadoPor(principal.getID());
    elemento.setIpCreacion(direccionIp);    
    respuesta = dEvento.guardarRegistro(elemento);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   
   
   for(DescargaCompartimento elemento: eDescarga.getCompartimentos()){
    elemento.setIdDescargaCisterna(Integer.parseInt(ClaveGenerada));
    respuesta = dDescargaCompartimentoDao.guardarRegistro(elemento);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   //Cambiar estado a transporte
   Transporte transporte = new Transporte();
   transporte.setId(eDescarga.getIdTransporte());
   transporte.setEstado(Transporte.ESTADO_DESCARGADO);
   respuesta = dTransporte.actualizarEstado(transporte);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   CargaTanque cargaTanque = null;
   respuesta = dCargaTanque.recuperarRegistro(eDescarga.getIdCargaTanque());
   if (respuesta.estado ==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   cargaTanque = (CargaTanque) respuesta.contenido.carga.get(0);
   
   DiaOperativo eDiaOperativo = new DiaOperativo();
   eDiaOperativo.setId(cargaTanque.getIdDiaOperativo());
   eDiaOperativo.setEstado(DiaOperativo.ESTADO_DESCARGANDO);
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());   
   respuesta = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
   if (respuesta.estado ==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eDescarga.getFechaCreacion().substring(0, 9),eDescarga.getFechaCreacion().substring(10),principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   this.transaccion.rollback(estadoTransaccion);
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "guardarDescarga");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_ACTUALIZAR_DESCARGA_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody RespuestaCompuesta actualizarDescarga(@RequestBody DescargaCisterna eDescarga, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager( dDescarga.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   principal = this.getCurrentUser();  
   respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_DESCARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   eDescarga.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDescarga.setActualizadoPor(principal.getID());
   eDescarga.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eDescarga.setCreadoPor(principal.getID());
   eDescarga.setIpActualizacion(direccionIp);
   eDescarga.setIpCreacion(direccionIp);
   eDescarga.setEstado(DescargaCisterna.ESTADO_ACTIVO);
   for(DescargaCompartimento elemento: eDescarga.getCompartimentos()){
	  
	if(elemento.getTipoMetodo()==DescargaCisterna.METODO_WINCHA){
	    if (elemento.getAlturaCompartimento()!= elemento.getAlturaFlecha()){
	        eDescarga.setEstado(DescargaCisterna.ESTADO_OBSERVADO);
	        break;
	       } else {
	        eDescarga.setEstado(DescargaCisterna.ESTADO_ACTIVO);
	       }
	}
    
   }
   if (eDescarga.getVolumenExcedenteCorregido()< 0){
    eDescarga.setEstado(DescargaCisterna.ESTADO_OBSERVADO);
   }
   respuesta = dDescarga.actualizarRegistro(eDescarga);

   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   //ClaveGenerada = ;
   
   for(Evento elemento: eDescarga.getEventos()){
    elemento.setIdRegistro(eDescarga.getId());
    elemento.setTipoRegistro(Constante.EVENTO_ORIGEN_DESCARGA);
    elemento.setCreadoEl(Calendar.getInstance().getTime().getTime());
    elemento.setCreadoPor(principal.getID());
    elemento.setIpCreacion(direccionIp);
    if (elemento.getId()>0){
     respuesta = dEvento.actualizarRegistro(elemento);
    }else {
    respuesta = dEvento.guardarRegistro(elemento);
    }
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   ContenidoAuditoria = mapper.writeValueAsString(eDescarga);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_CARGA_COMPLETA);
   eBitacora.setTabla(CargaTanqueDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eDescarga.getCreadoEl());
   eBitacora.setRealizadoPor(eDescarga.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   
   if (eDescarga.getIdAutorizacionEjecutada()>0) {
    AutorizacionEjecutada eAutorizacionEjecutada = new AutorizacionEjecutada();
    eAutorizacionEjecutada.setId(eDescarga.getIdAutorizacionEjecutada());
    eAutorizacionEjecutada.setIdRegistro(eDescarga.getId());
    dAutorizacionEjecutada.actualizarRegistroVinculado(eAutorizacionEjecutada);
   }
   
   respuesta= dDescargaCompartimentoDao.eliminarRegistros(eDescarga.getId());
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   for(DescargaCompartimento elemento: eDescarga.getCompartimentos()){
    elemento.setIdDescargaCisterna(eDescarga.getId());
    respuesta = dDescargaCompartimentoDao.guardarRegistro(elemento);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   //Cambiar estado a transporte
   Transporte transporte = new Transporte();
   transporte.setId(eDescarga.getIdTransporte());
   transporte.setEstado(Transporte.ESTADO_DESCARGADO);
   respuesta = dTransporte.actualizarEstado(transporte);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   CargaTanque cargaTanque = null;
   respuesta = dCargaTanque.recuperarRegistro(eDescarga.getIdCargaTanque());
   if (respuesta.estado ==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   cargaTanque = (CargaTanque) respuesta.contenido.carga.get(0);
   
   DiaOperativo eDiaOperativo = new DiaOperativo();
   eDiaOperativo.setId(cargaTanque.getIdDiaOperativo());
   eDiaOperativo.setEstado(DiaOperativo.ESTADO_DESCARGANDO);
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   respuesta = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
   if (respuesta.estado ==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eDescarga.getFechaCreacion().substring(0, 9),eDescarga.getFechaCreacion().substring(10),principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   this.transaccion.rollback(estadoTransaccion);
   //ex.printStackTrace();
   Utilidades.gestionaError(ex, sNombreClase, "actualizarDescarga");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_RECUPERAR_DESCARGA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody RespuestaCompuesta recuperarDescarga(int ID, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  DescargaCisterna eDescarga = null;
  ParametrosListar parametros = null;
  Transporte eTransporte = null;
  Contenido<DescargaCisterna> contenido=null;
  try {
   principal = this.getCurrentUser();  
   respuesta = dEnlace.recuperarRegistro(URL_CREAR_DESCARGA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   respuesta = dDescarga.recuperarRegistro(ID);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   eDescarga =  (DescargaCisterna) respuesta.contenido.carga.get(0);
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroDescargaCisterna(eDescarga.getId());
   parametros.setCampoOrdenamiento(DescargaCompartimentoDao.NOMBRE_CAMPO_NUMERO_COMPARTIMENTO);
   parametros.setSentidoOrdenamiento("ASC"); 
   respuesta = dDescargaCompartimentoDao.recuperarRegistros(parametros);
   if (!respuesta.estado) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   for(Object detalle : respuesta.contenido.carga){
    eDescarga.agregarCompartimento((DescargaCompartimento)detalle);
   }   
   
   respuesta = dTransporte.recuperarRegistro(eDescarga.getIdTransporte());
   if (!respuesta.estado) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   if (respuesta.contenido.carga.isEmpty()) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   eTransporte= (Transporte) respuesta.contenido.carga.get(0);   
   respuesta = dDetalleTransporte.recuperarDetalleTransporte(eTransporte.getId());
   
   if (!respuesta.estado) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   for(Object detalle : respuesta.contenido.carga){
    eTransporte.agregarDetalle((DetalleTransporte)detalle);
   }
   
   respuesta = dEvento.recuperarRegistroPorIdRegistroYTipoRegistro(eDescarga.getId(),Constante.EVENTO_ORIGEN_DESCARGA);
   if (!respuesta.estado) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   for(Object evento : respuesta.contenido.carga){
    eDescarga.agregarEvento((Evento)evento);
   }  
   
   eDescarga.setTransporte(eTransporte);   
   contenido= new Contenido<DescargaCisterna>();
   contenido.carga = new ArrayList<DescargaCisterna>();
   contenido.carga.add(eDescarga);
   respuesta.contenido= contenido;
   respuesta.estado=true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   //ex.printStackTrace();
	  Utilidades.gestionaError(ex, sNombreClase, "recuperarDescarga");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext()
    .getAuthentication().getPrincipal();
 }
}
