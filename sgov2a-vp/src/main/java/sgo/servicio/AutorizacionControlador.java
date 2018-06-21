package sgo.servicio;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import sgo.datos.AutorizacionDao;
import sgo.datos.AutorizacionEjecutadaDao;
import sgo.datos.AutorizacionUsuarioDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.EnlaceDao;
import sgo.datos.UsuarioDao;
import sgo.entidad.Autorizacion;
import sgo.entidad.AutorizacionEjecutada;
import sgo.entidad.AutorizacionUsuario;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Usuario;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class AutorizacionControlador {
 @Autowired
 private MessageSource gestorDiccionario;// Gestor del diccionario de mensajes
                                         // para la internacionalizacion
 @Autowired
 private BitacoraDao dBitacora; // Clase para registrar en la bitacora
                                // (auditoria por accion)
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private CisternaDao dCisterna;
 @Autowired
 private AutorizacionDao dAutorizacion;
 @Autowired
 private UsuarioDao dUsuario;
 @Autowired
 private AutorizacionUsuarioDao dAutorizacionUsuario;
 @Autowired
 private AutorizacionEjecutadaDao dAutorizacionEjecutada;

 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/autorizacion";
 private static final String URL_GESTION_RELATIVA = "/autorizacion";
 
 private static final String URL_LISTAR_APROBADOR_COMPLETA = "/admin/autorizacion/listar-aprobador";
 private static final String URL_LISTAR_APROBADOR_RELATIVA = "/autorizacion/listar-aprobador";
 
 private static final String URL_VALIDAR_AUTORIZACION_COMPLETA = "/admin/autorizacion/validar";
 private static final String URL_VALIDAR_AUTORIZACION_RELATIVA = "/autorizacion/validar";
 
 
 private static final String URL_GUARDAR_COMPLETA = "/admin/autorizacion/crear";
 private static final String URL_GUARDAR_RELATIVA = "/autorizacion/crear";
 private static final String URL_LISTAR_COMPLETA = "/admin/autorizacion/listar";
 private static final String URL_LISTAR_RELATIVA = "/autorizacion/listar";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/autorizacion/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/autorizacion/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/autorizacion/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/autorizacion/recuperar";
 private static final String URL_RECUPERAR_POR_CODIGO_COMPLETA = "/admin/autorizacion/recuperarPorCodigoInterno";
 private static final String URL_RECUPERAR_POR_CODIGO_RELATIVA = "/autorizacion/recuperarPorCodigoInterno";
 private static final String URL_RECUPERAR_AUTORIZACION_COMPLETA = "/admin/autorizacion/recuperarAutorizacion";
 private static final String URL_RECUPERAR_AUTORIZACION_RELATIVA = "/autorizacion/recuperarAutorizacion";
 private static final String URL_RECUPERAR_POR_AUTORIZACION_COMPLETA = "/admin/autorizacion/recuperarPorAutorizacion";
 private static final String URL_RECUPERAR_POR_AUTORIZACION_RELATIVA = "/autorizacion/recuperarPorAutorizacion";
 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/autorizacion/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/autorizacion/actualizarEstado";
 private static final String URL_GUARDAR_AUTORIZACION_EJECUTADA_COMPLETA = "/admin/autorizacion/crearAutorizacionEjecutada";
 private static final String URL_GUARDAR_AUTORIZACION_EJECUTADA_RELATIVA = "/autorizacion/crearAutorizacionEjecutada";

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
   //
   mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar", null, locale));

   mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer", null, locale));
   mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_AUTORIZAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAutorizar", null, locale));

   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
   mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));

   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));

   mapaValores.put("TITULO_SELECCIONAR_ELEMENTO", gestorDiccionario.getMessage("sgo.seleccionarElemento", null, locale));

  } catch (Exception ex) {

  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  // verificar si esto existe
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<Enlace> listaEnlaces = null;
  RespuestaCompuesta respuesta = null;
  HashMap<String, String> mapaValores = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<Enlace>) respuesta.contenido.carga;
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "seguridad/autorizacion.jsp");
   vista.addObject("vistaJS", "seguridad/autorizacion.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("mapaValores", mapaValores);
  } catch (Exception ex) {

  }
  return vista;
 }

 
 @RequestMapping(value = URL_LISTAR_APROBADOR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarAprobadores(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_APROBADOR_COMPLETA);
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

   if (httpRequest.getParameter("CodigoInterno") != null) {
    parametros.setFiltroCodigoInternoAutorizacion((httpRequest.getParameter("CodigoInterno")));
   }
   
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
   String fechaHoy = sdf.format(new java.util.Date()); 
   parametros.setFiltroFechaInicio(fechaHoy);
   parametros.setFiltroFechaFinal(fechaHoy);

   // Recuperar registros
   respuesta = dAutorizacionUsuario.recuperarAprobadores(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_VALIDAR_AUTORIZACION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta validarAutorizacion(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   //respuesta = dEnlace.recuperarRegistro(URL_VALIDAR_AUTORIZACION_COMPLETA);
   //if (respuesta.estado == false) {
   // mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
    //throw new Exception(mensajeRespuesta);
  // }
   //Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   //if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
   // mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
   // throw new Exception(mensajeRespuesta);
   //}
   // Recuperar parametros
   parametros = new ParametrosListar();

   if (httpRequest.getParameter("nombreUsuario") == null) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }   
   String nombreUsuario = (httpRequest.getParameter("nombreUsuario"));
   
   if (httpRequest.getParameter("justificacion") == null) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }   
   String justificacion = (httpRequest.getParameter("justificacion"));
   
   if (httpRequest.getParameter("codigoInterno") == null) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   String codigoInterno =(httpRequest.getParameter("codigoInterno"));
   
   if (httpRequest.getParameter("codigoAutorizacion") == null) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   String codigoAutorizacion = ((httpRequest.getParameter("codigoAutorizacion")));
   
   
   Usuario mUsuario = dUsuario.getRecord(nombreUsuario);
   if (mUsuario==null){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noExisteUsuario", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   parametros.setFiltroCodigoAutorizacion(codigoAutorizacion);
   parametros.setFiltroIdUsuario(mUsuario.getId());
   parametros.setFiltroCodigoInternoAutorizacion(codigoInterno);
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
   String fechaHoy = sdf.format(new java.util.Date()); 
   parametros.setFiltroFechaInicio(fechaHoy);
   parametros.setFiltroFechaFinal(fechaHoy);
   // Recuperar registros
   respuesta = dAutorizacionUsuario.recuperarAutorizacionUsuario(parametros);
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.autorizacionInvalida", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   if (respuesta.contenido.carga.size()!=1){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.autorizacionInvalida", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   AutorizacionUsuario eAutorizacion = (AutorizacionUsuario) respuesta.contenido.carga.get(0);
   
   int OrigenAutorizacion =0;
   if (codigoInterno.equals(Autorizacion.TIPO_INGRESAR_VOLUMEN_DIRECTO)){
    OrigenAutorizacion = Constante.ORIGEN_AUTORIZACION_DESCARGA;
   }
   
   AutorizacionEjecutada eAutorizacionEjecutada = new AutorizacionEjecutada();
   eAutorizacionEjecutada.setIdAutorizacion(eAutorizacion.getIdAutorizacion());
   eAutorizacionEjecutada.setIdAutorizador(mUsuario.getId());
   eAutorizacionEjecutada.setDescripcion(justificacion);
   eAutorizacionEjecutada.setVigenteDesde(eAutorizacion.getVigenteDesde());
   eAutorizacionEjecutada.setVigenteHasta(eAutorizacion.getVigenteHasta());
   eAutorizacionEjecutada.setEjecutadaPor(principal.getID());
   eAutorizacionEjecutada.setEjecutadaEl(Calendar.getInstance().getTime().getTime());
   eAutorizacionEjecutada.setIdRegistro(0);
   eAutorizacionEjecutada.setTipoRegistro(OrigenAutorizacion);
   
   respuesta = dAutorizacionEjecutada.guardarRegistro(eAutorizacionEjecutada);
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.autorizacionInvalida", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   respuesta.estado=true;
   respuesta.mensaje=gestorDiccionario.getMessage("sgo.autorizacionValida", null, locale);   
   
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
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

   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroEstado") != null) {
    parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
   }

   // Recuperar registros
   respuesta = dAutorizacion.recuperarRegistros(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_RECUPERAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistro(int ID, Locale locale) {
  // TODO
  ID = 2;
  System.out.println("entra en recuperaRegistro");
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

   AutorizacionUsuario eAutorizacionUsuario = new AutorizacionUsuario();
   List<Usuario> listaUsuario = new ArrayList<Usuario>();
   List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();

   eAutorizacionUsuario.setIdUsuario(principal.getID());
   RespuestaCompuesta buscaUsuario = dUsuario.recuperarRegistro(principal.getID());
   if (!buscaUsuario.estado) {
    respuesta.estado = false;
    respuesta.contenido = null;
    respuesta.mensaje = ("Error en la búsqueda de las usuario.");
    return respuesta;
   }
   if (!buscaUsuario.contenido.carga.isEmpty()) {
    listaUsuario = new ArrayList<Usuario>();
    Iterator itrUsuario = buscaUsuario.contenido.carga.iterator();
    while (itrUsuario.hasNext()) {
     Usuario eUsuario = (Usuario) itrUsuario.next();
     listaUsuario.add(eUsuario);
     eAutorizacionUsuario.setUsuario(listaUsuario);
    }
   }

   // Recuperar el registro
   RespuestaCompuesta recuperaAutorizacionesPorUsuario = dAutorizacionUsuario.recuperarAutorizacionesPorUsuario(principal.getID());
   // Verifica el resultado de la accion
   if (recuperaAutorizacionesPorUsuario.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   if (!recuperaAutorizacionesPorUsuario.contenido.carga.isEmpty()) {
    Iterator itrRespuesta = recuperaAutorizacionesPorUsuario.contenido.carga.iterator();

    while (itrRespuesta.hasNext()) {
     AutorizacionUsuario autUsuario = (AutorizacionUsuario) itrRespuesta.next();
     RespuestaCompuesta buscaAutorizacion = dAutorizacion.recuperarRegistro(autUsuario.getIdAutorizacion());
     if (!buscaAutorizacion.estado) {
      respuesta.estado = false;
      respuesta.contenido = null;
      respuesta.mensaje = ("Error en la búsqueda de las autorizaciones.");
      return respuesta;
     }
     if (!buscaAutorizacion.contenido.carga.isEmpty()) {
      Iterator itrAutorizacion = buscaAutorizacion.contenido.carga.iterator();
      while (itrAutorizacion.hasNext()) {
       Autorizacion eAutorizacion = (Autorizacion) itrAutorizacion.next();
       listaAutorizacion.add(eAutorizacion);
      }
     }
    }

   }
   eAutorizacionUsuario.setAutorizacion(listaAutorizacion);
   List<AutorizacionUsuario> resp = new ArrayList<AutorizacionUsuario>();
   Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
   resp.add(eAutorizacionUsuario);
   contenido.carga = resp;
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

 @RequestMapping(value = URL_RECUPERAR_POR_CODIGO_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistrosPorCodigoInterno(String codigoInterno, Locale locale) {
  System.out.println("entra en recuperaRegistrosPorCodigoInterno");
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_POR_CODIGO_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   AutorizacionUsuario eAutorizacionUsuario = new AutorizacionUsuario();
   List<Usuario> listaUsuario = new ArrayList<Usuario>();
   List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();

   RespuestaCompuesta buscaPorCodigo = dAutorizacion.recuperarAutorizacionesPorCodigoInterno(codigoInterno);
   if (!buscaPorCodigo.estado) {
    respuesta.estado = false;
    respuesta.contenido = null;
    respuesta.mensaje = ("Error en la búsqueda de la autorizacion.");
    return respuesta;
   }
   if (!buscaPorCodigo.contenido.carga.isEmpty()) {
    Iterator itrBuscaPorCodigo = buscaPorCodigo.contenido.carga.iterator();

    while (itrBuscaPorCodigo.hasNext()) {
     Autorizacion eAutorizacion = (Autorizacion) itrBuscaPorCodigo.next();
     RespuestaCompuesta buscaUsuarios = dAutorizacionUsuario.recuperarAutorizacionesPorAutorizacionYFecha(eAutorizacion.getId());
     if (!buscaUsuarios.estado) {
      respuesta.estado = false;
      respuesta.contenido = null;
      respuesta.mensaje = ("Error en la búsqueda de las usuarios.");
      return respuesta;
     }
     if (!buscaUsuarios.contenido.carga.isEmpty()) {
      Iterator itrUsuario = buscaUsuarios.contenido.carga.iterator();
      while (itrUsuario.hasNext()) {
       AutorizacionUsuario eAutorizacionUsuarioRecuperados = (AutorizacionUsuario) itrUsuario.next();
       RespuestaCompuesta buscaDatosUsuario = dUsuario.recuperarRegistro(eAutorizacionUsuarioRecuperados.getIdUsuario());
       if (!buscaDatosUsuario.estado) {
        respuesta.estado = false;
        respuesta.contenido = null;
        respuesta.mensaje = ("Error en la búsqueda de los datos del usuario.");
        return respuesta;
       }
       if (!buscaDatosUsuario.contenido.carga.isEmpty()) {
        Iterator itrBuscaDatosUsuario = buscaDatosUsuario.contenido.carga.iterator();
        while (itrBuscaDatosUsuario.hasNext()) {
         Usuario eUsuario = (Usuario) itrBuscaDatosUsuario.next();
         listaUsuario.add(eUsuario);
        }
       }
      }
     }
     listaAutorizacion.add(eAutorizacion);
    }
   }
   eAutorizacionUsuario.setUsuario(listaUsuario);
   eAutorizacionUsuario.setAutorizacion(listaAutorizacion);

   List<AutorizacionUsuario> resp = new ArrayList<AutorizacionUsuario>();
   Contenido<AutorizacionUsuario> contenido = new Contenido<AutorizacionUsuario>();
   resp.add(eAutorizacionUsuario);
   contenido.carga = resp;
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

 // TODO
 @RequestMapping(value = URL_RECUPERAR_POR_AUTORIZACION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistrosPorAutorizacion(HttpServletRequest httpRequest, Locale locale) {
  System.out.println("entra en recuperaRegistrosPorAutorizacion");
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  int IdAutorizacion = -1;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_POR_AUTORIZACION_COMPLETA);
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
   if (httpRequest.getParameter("autorizacion") != null) {
    System.out.println(httpRequest.getParameter("autorizacion"));
    IdAutorizacion = Integer.parseInt(httpRequest.getParameter("autorizacion"));
   }

   // Recuperar registros
   respuesta = dAutorizacionUsuario.recuperarAutorizacionesPorAutorizacionYFecha(IdAutorizacion);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 // TODO
 @RequestMapping(value = URL_RECUPERAR_AUTORIZACION_COMPLETA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaAutorizacion(int idUsuario, int idAutorizacion, Locale locale) {
  System.out.println("entra en recuperaRegistrosPorCodigoInterno");
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_AUTORIZACION_RELATIVA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   AutorizacionUsuario eAutorizacionUsuario = new AutorizacionUsuario();
   List<Usuario> listaUsuario = new ArrayList<Usuario>();
   List<Autorizacion> listaAutorizacion = new ArrayList<Autorizacion>();

   RespuestaCompuesta buscaPorCodigo = dAutorizacionUsuario.recuperarAutorizacionesPorUsuarioYAutorizacion(idUsuario, idAutorizacion);
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

 @RequestMapping(value = URL_GUARDAR_AUTORIZACION_EJECUTADA_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarValidarAutorizacion(@RequestBody AutorizacionEjecutada eAutorizacionEjecutada, HttpServletRequest peticionHttp, Locale locale) {
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
   this.transaccion = new DataSourceTransactionManager(dAutorizacion.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_AUTORIZACION_EJECUTADA_COMPLETA);
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

   eAutorizacionEjecutada.setEjecutadaEl(Calendar.getInstance().getTime().getTime());
   eAutorizacionEjecutada.setEjecutadaPor(principal.getID());
   eAutorizacionEjecutada.setIpCreacion(direccionIp);

   respuesta = dAutorizacionEjecutada.guardarRegistro(eAutorizacionEjecutada);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject
                                             // via @Autowired
   ContenidoAuditoria = mapper.writeValueAsString(eAutorizacionEjecutada);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_AUTORIZACION_EJECUTADA_COMPLETA);
   eBitacora.setTabla(AutorizacionEjecutadaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eAutorizacionEjecutada.getEjecutadaEl());
   eBitacora.setRealizadoPor(eAutorizacionEjecutada.getEjecutadaPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eAutorizacionEjecutada.getFechaCreacion().substring(0, 9),
     eAutorizacionEjecutada.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
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

 @RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody Autorizacion eAutorizacion, HttpServletRequest peticionHttp, Locale locale) {
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
   this.transaccion = new DataSourceTransactionManager(dAutorizacion.getDataSource());
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
   //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
   
   //eAutorizacion.setCodigoInterno(passwordEncoder.encode(eAutorizacion.getCodigoInterno()));
   eAutorizacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eAutorizacion.setActualizadoPor(principal.getID());
   eAutorizacion.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eAutorizacion.setCreadoPor(principal.getID());
   eAutorizacion.setIpActualizacion(direccionIp);
   eAutorizacion.setIpCreacion(direccionIp);
   respuesta = dAutorizacion.guardarRegistro(eAutorizacion);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject
                                             // via @Autowired
   ContenidoAuditoria = mapper.writeValueAsString(eAutorizacion);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(AutorizacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eAutorizacion.getCreadoEl());
   eBitacora.setRealizadoPor(eAutorizacion.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso",
     new Object[] { eAutorizacion.getFechaCreacion().substring(0, 9), eAutorizacion.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
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
 RespuestaCompuesta actualizarRegistro(@RequestBody AutorizacionUsuario eAutorizacionUsuario, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dAutorizacion.getDataSource());
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

   AutorizacionUsuario entidad = new AutorizacionUsuario();
   if (!eAutorizacionUsuario.getAutorizacion().isEmpty()) {
    entidad = new AutorizacionUsuario();
    for (int i = 0; i < eAutorizacionUsuario.getAutorizacion().size(); i++) {
     entidad.setIdUsuario(eAutorizacionUsuario.getIdUsuario());
     entidad.setIdAutorizacion(eAutorizacionUsuario.getAutorizacion().get(i).getId());
     entidad.setCodigoAutorizacion(eAutorizacionUsuario.getCodigoAutorizacion());
     entidad.setVigenteDesde(eAutorizacionUsuario.getVigenteDesde());
     entidad.setVigenteHasta(eAutorizacionUsuario.getVigenteHasta());
     entidad.setActualizadoEl(Calendar.getInstance().getTime().getTime());
     entidad.setActualizadoPor(principal.getID());
     entidad.setIpActualizacion(direccionIp);
	 //valida los datos que vienen del formulario
	 Respuesta validacion = Utilidades.validacionXSS(entidad, gestorDiccionario, locale);
	 if (validacion.estado == false) {
	   throw new Exception(validacion.valor);
	 }
     respuesta = dAutorizacionUsuario.actualizarRegistroPorIdUsuarioIdAutorizacion(entidad);
     if (respuesta.estado == false) {
      throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
     }
     // Guardar en la bitacora
     ObjectMapper mapper = new ObjectMapper();
     eBitacora.setUsuario(principal.getNombre());
     eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
     eBitacora.setTabla(AutorizacionUsuarioDao.NOMBRE_TABLA);
     eBitacora.setIdentificador(String.valueOf(entidad.getIdAutorizacion()));
     eBitacora.setContenido(mapper.writeValueAsString(entidad));
     eBitacora.setRealizadoEl(entidad.getActualizadoEl());
     eBitacora.setRealizadoPor(entidad.getActualizadoPor());
     respuesta = dBitacora.guardarRegistro(eBitacora);
     if (respuesta.estado == false) {
      throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
     }
     respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso",
       new Object[] { entidad.getFechaActualizacion().substring(0, 9), entidad.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
     ;
    }
   }

   // if (respuesta.estado==false){
   // throw new
   // Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
   // }
   // respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new
   // Object[] { entidad.getFechaActualizacion().substring(0,
   // 9),entidad.getFechaActualizacion().substring(10),principal.getIdentidad()
   // },locale);;
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
 RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Autorizacion eAutorizacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dAutorizacion.getDataSource());
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
   eAutorizacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eAutorizacion.setActualizadoPor(principal.getID());
   eAutorizacion.setIpActualizacion(direccionIp);
   respuesta = dAutorizacion.ActualizarEstadoRegistro(eAutorizacion);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(AutorizacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eAutorizacion.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eAutorizacion));
   eBitacora.setRealizadoEl(eAutorizacion.getActualizadoEl());
   eBitacora.setRealizadoPor(eAutorizacion.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eAutorizacion.getFechaActualizacion().substring(0, 9), eAutorizacion.getFechaActualizacion().substring(10),
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