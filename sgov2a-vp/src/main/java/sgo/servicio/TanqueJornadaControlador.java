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

import sgo.datos.BitacoraDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.DescargaCompartimentoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.TanqueJornadaDao;
import sgo.datos.TransporteDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.TanqueJornada;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;

@Controller
public class TanqueJornadaControlador {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora; 
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private TanqueJornadaDao dTanqueJornada;
 @Autowired
 private TransporteDao dTransporte;
 @Autowired
 private CompartimentoDao dCompartimento;
 @Autowired
 private DescargaCompartimentoDao dDescargaCompartimento;

 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/tanqueJornada";
 private static final String URL_GESTION_RELATIVA = "/tanqueJornada";
 private static final String URL_GUARDAR_COMPLETA = "/admin/tanqueJornada/crear";
 private static final String URL_GUARDAR_RELATIVA = "/tanqueJornada/crear";
 private static final String URL_LISTAR_COMPLETA = "/admin/tanqueJornada/listar";
 private static final String URL_LISTAR_RELATIVA = "/tanqueJornada/listar";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/tanqueJornada/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/tanqueJornada/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/tanqueJornada/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/tanqueJornada/recuperar";
 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/tanqueJornada/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/tanqueJornada/actualizarEstado";
 


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
   mapaValores.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado", null, locale));   //
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
   mapaValores.put("MENSAJE_ELIMINAR_COMPARTIMENTO", gestorDiccionario.getMessage("sgo.mensajeEliminarCompartimento",null,locale));
   mapaValores.put("MENSAJE_DESEA_CONTINUAR", gestorDiccionario.getMessage("sgo.deseaContinuar",null,locale));
  } catch (Exception ex) {

  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  // verificar si esto existe
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  RespuestaCompuesta respuesta = null;
  HashMap<String, String> mapaValores = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "mantenimiento/tanqueJornada.jsp");
   vista.addObject("vistaJS", "mantenimiento/tanqueJornada.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
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
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
   if (respuesta.estado == false) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   // Recuperar parametros
   parametros = new ParametrosListar();
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

   parametros.setFiltroFechaDiaOperativo("");
   if (httpRequest.getParameter("filtroFechaDiaOperativo") != null) {
	   parametros.setFiltroFechaDiaOperativo((httpRequest.getParameter("filtroFechaDiaOperativo")));
   }

   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroEstado") != null) {
    parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
   }
   
   parametros.setIdOperacion(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("idOperacion") != null) {
    parametros.setIdOperacion(Integer.parseInt(httpRequest.getParameter("idOperacion")));
   }
   
   parametros.setFiltroProducto(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroProducto") != null) {
    parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
   }
   
   if (httpRequest.getParameter("idJornada") != null) {
    parametros.setIdJornada(Integer.parseInt(httpRequest.getParameter("idJornada")));
   }

   if (httpRequest.getParameter("filtroEstacion") != null) {
    parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
   }

   if (httpRequest.getParameter("estadoServicio") != null) {
    parametros.setEstadoServicio(Integer.parseInt(httpRequest.getParameter("estadoServicio")));
   }
   
   if (httpRequest.getParameter("estadoDespachando") != null) {
    parametros.setEstadoDespachando(Integer.parseInt(httpRequest.getParameter("estadoDespachando")));
   }
   
   if (httpRequest.getParameter("tanqueDeApertura") != null) {
    parametros.setTanqueDeApertura(Integer.parseInt(httpRequest.getParameter("tanqueDeApertura")));
   }
   
   if (httpRequest.getParameter("tanqueDeCierre") != null) {
    parametros.setTanqueDeCierre(Integer.parseInt(httpRequest.getParameter("tanqueDeCierre")));
   }

   // Recuperar registros
   respuesta = dTanqueJornada.recuperarRegistros(parametros);
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
  TanqueJornada eTanqueJornada = null;
  ParametrosListar parametros = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   respuesta = dTanqueJornada.recuperarRegistro(ID);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(0);
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setIdJornada(eTanqueJornada.getIdTjornada());
   parametros.setCampoOrdenamiento(CompartimentoDao.CAMPO_NUMERO_COMPARTIMENTO);
   parametros.setSentidoOrdenamiento("ASC");

   respuesta = dCompartimento.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   Contenido<TanqueJornada> contenido = new Contenido<TanqueJornada>();
   contenido.carga = new ArrayList<TanqueJornada>();
   contenido.carga.add(eTanqueJornada);
   respuesta.contenido = contenido;  
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody TanqueJornada eTanqueJornada, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  ParametrosListar parametros = null;
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dTanqueJornada.getDataSource());
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
   
   // Actualiza los datos de auditoria local
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   //eTanqueJornada.setCantidadCompartimentos(eTanqueJornada.getCompartimentos().size());
   eTanqueJornada.setEstadoServicio(Constante.ESTADO_ACTIVO);
   
   parametros = new ParametrosListar();
   
   respuesta = dTanqueJornada.guardarRegistro(eTanqueJornada);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); 
   ContenidoAuditoria = mapper.writeValueAsString(eTanqueJornada);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eTanqueJornada.getCreadoEl());
   eBitacora.setRealizadoPor(eTanqueJornada.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eTanqueJornada.getFechaCreacion().substring(0, 9), eTanqueJornada.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
   this.transaccion.rollback(estadoTransaccion);
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ACTUALIZAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarRegistro(@RequestBody TanqueJornada eTanqueJornada, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  ParametrosListar parametros = null;
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dTanqueJornada.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
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
 
   //si las validaciones son correctas permito hacer la actualizaciÃƒÂ³n de la TanqueJornada.
   respuesta = dTanqueJornada.actualizarRegistro(eTanqueJornada);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
   eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eTanqueJornada.getIdTjornada()));
   eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
   eBitacora.setRealizadoEl(eTanqueJornada.getActualizadoEl());
   eBitacora.setRealizadoPor(eTanqueJornada.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
  
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eTanqueJornada.getFechaActualizacion().substring(0, 9), eTanqueJornada.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);

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

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }

 @RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarEstadoRegistro(@RequestBody TanqueJornada eTanqueJornada, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dTanqueJornada.getDataSource());
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
   respuesta = dTanqueJornada.ActualizarEstadoRegistro(eTanqueJornada);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eTanqueJornada.getIdTjornada()));
   eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
   eBitacora.setRealizadoEl(eTanqueJornada.getActualizadoEl());
   eBitacora.setRealizadoPor(eTanqueJornada.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eTanqueJornada.getFechaActualizacion().substring(0, 9), eTanqueJornada.getFechaActualizacion().substring(10),
     principal.getIdentidad() }, locale);
   ;
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
}