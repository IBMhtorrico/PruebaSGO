package sgo.servicio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.ClienteDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EventoDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.ResumenCierreDao;
import sgo.entidad.Bitacora;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.Evento;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.ResumenCierre;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.CabeceraReporte;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
import sgo.utilidades.Reporteador;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CierreControlador {
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
 private PlanificacionDao dPlanificacion;
 @Autowired
 private ClienteDao dCliente;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private ProductoDao dProducto;
 @Autowired
 private DiaOperativoControlador DiaOperativoControlador;
 @Autowired
 private ResumenCierreDao dResumenCierre;
 @Autowired
 private EventoDao dEvento;
 @Autowired
 ServletContext servletContext;
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/cierre";
 private static final String URL_GESTION_RELATIVA = "/cierre";
 private static final String URL_LISTAR_COMPLETA = "/admin/cierre/listar";
 private static final String URL_LISTAR_RELATIVA = "/cierre/listar";
 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/cierre/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/cierre/actualizarEstado";
 
 private static final String URL_CERRAR_ESTADO_COMPLETA = "/admin/cierre/cerrar-dia";
 private static final String URL_CERRAR_ESTADO_RELATIVA = "/cierre/cerrar-dia";
 
 private static final String URL_LISTAR_RESUMEN_COMPLETA = "/admin/dia_operativo/resumen";
 private static final String URL_LISTAR_RESUMEN_RELATIVA = "/dia_operativo/resumen";
 private static final String URL_REPORTE_CIERRE_COMPLETA = "/admin/cierre/reporte";
 private static final String URL_REPORTE_CIERRE_RELATIVA = "/cierre/reporte";
 
 private static final String URL_VERIFICA_DIA_OPERATIVO_RELATIVA = "/cierre/verificaDiaOperativo";

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
   mapaValores.put("MENSAJE_ERROR_CODIGO_AUTORIZACION", gestorDiccionario.getMessage("sgo.errorCodigoAutorizacion", null, locale));
   mapaValores.put("TEXTO_ELEMENTO_SELECCIONAR", gestorDiccionario.getMessage("sgo.seleccionarElemento", null, locale));
   mapaValores.put("MENSAJE_ERROR_POR_LONGITUD", gestorDiccionario.getMessage("sgo.errorLongitudJustificacion", null, locale));
   mapaValores.put("ERROR_FALTA_JUSTIFICACION", gestorDiccionario.getMessage("sgo.errorFaltaJustificacion", null, locale));
   mapaValores.put("TITULO_DETALLE_DIA_OPERATIVO", gestorDiccionario.getMessage("sgo.tituloDetalleDiaOperativo", null, locale));   
  } catch (Exception ex) {
   ex.printStackTrace();
  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  ArrayList<?> listaClientes = null;
  ArrayList<?> listaOperaciones = null;
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

   /*
    * parametros = new ParametrosListar();
    * parametros.setPaginacion(Constante.SIN_PAGINACION); respuesta =
    * dCliente.recuperarRegistros(parametros); if (respuesta.estado==false){
    * throw new
    * Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles"
    * ,null,locale)); }
    */
   // listaClientes = (ArrayList<Cliente>) respuesta.contenido.carga;

   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   parametros.setIdCliente(principal.getCliente().getId());
   parametros.setIdOperacion(principal.getOperacion().getId());
   respuesta = dOperacion.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "cierre/cierre.jsp");
   vista.addObject("vistaJS", "cierre/cierre.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("clientes", listaClientes);
   vista.addObject("operaciones", listaOperaciones);
   vista.addObject("mapaValores", mapaValores);
   vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
  } catch (Exception ex) {

  }
  return vista;
 }

 /*
  * @RequestMapping(value = URL_LISTAR_RELATIVA ,method = RequestMethod.GET)
  * public @ResponseBody RespuestaCompuesta
  * recuperarRegistros(HttpServletRequest httpRequest, Locale locale){
  * RespuestaCompuesta respuesta = null; ParametrosListar parametros= null;
  * AuthenticatedUserDetails principal = null; String mensajeRespuesta=""; try {
  * //Recuperar el usuario actual principal = this.getCurrentUser(); //Recuperar
  * el enlace de la accion respuesta =
  * dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA); if
  * (respuesta.estado==false){ mensajeRespuesta =
  * gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale); throw
  * new Exception(mensajeRespuesta); } Enlace eEnlace = (Enlace)
  * respuesta.getContenido().getCarga().get(0); //Verificar si cuenta con el
  * permiso necesario if
  * (!principal.getRol().searchPermiso(eEnlace.getPermiso())){ mensajeRespuesta
  * = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale); throw new
  * Exception(mensajeRespuesta); } //Recuperar parametros parametros = new
  * ParametrosListar(); if (httpRequest.getParameter("paginacion") != null) {
  * parametros.setPaginacion(Integer.parseInt(
  * httpRequest.getParameter("paginacion"))); }
  * 
  * if (httpRequest.getParameter("registrosxPagina") != null) {
  * parametros.setRegistrosxPagina
  * (Integer.parseInt(httpRequest.getParameter("registrosxPagina"))); }
  * 
  * if (httpRequest.getParameter("inicioPagina") != null) {
  * parametros.setInicioPaginacion
  * (Integer.parseInt(httpRequest.getParameter("inicioPagina"))); }
  * 
  * if (httpRequest.getParameter("campoOrdenamiento") != null) {
  * parametros.setCampoOrdenamiento
  * ((httpRequest.getParameter("campoOrdenamiento"))); }
  * 
  * if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
  * parametros.setSentidoOrdenamiento
  * ((httpRequest.getParameter("sentidoOrdenamiento"))); }
  * 
  * if (httpRequest.getParameter("valorBuscado") != null) {
  * parametros.setValorBuscado((httpRequest.getParameter("valorBuscado"))); }
  * 
  * if (httpRequest.getParameter("filtroOperacion") != null) {
  * parametros.setFiltroOperacion
  * (Integer.parseInt(httpRequest.getParameter("filtroOperacion"))); }
  * 
  * if (httpRequest.getParameter("filtroFechaPlanificada") != null) {
  * parametros.
  * setFiltroFechaPlanificada((httpRequest.getParameter("filtroFechaPlanificada"
  * ))); }
  * 
  * if (httpRequest.getParameter("filtroFechaInicio") != null) {
  * parametros.setFiltroFechaInicio
  * ((httpRequest.getParameter("filtroFechaInicio"))); }
  * 
  * if (httpRequest.getParameter("filtroFechaFinal") != null) {
  * parametros.setFiltroFechaFinal
  * ((httpRequest.getParameter("filtroFechaFinal"))); }
  * 
  * //Recuperar registros //Recuperamos los dias operativos respuesta =
  * dDiaOperativo.recuperarRegistrosParaCierre(parametros); respuesta.mensaje=
  * gestorDiccionario.getMessage("sgo.listarExitoso",null,locale); }
  * catch(Exception ex){ respuesta.estado=false; respuesta.contenido = null;
  * respuesta.mensaje=ex.getMessage(); } return respuesta; }
  */

  @RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta actualizarEstadoRegistro(@RequestBody DiaOperativo entidad, HttpServletRequest peticionHttp,Locale locale){
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora=null;
  String direccionIp="";
  try {
  //Inicia la transaccion
  this.transaccion = new DataSourceTransactionManager(dDiaOperativo.getDataSource());
  definicionTransaccion = new DefaultTransactionDefinition();
  estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
  eBitacora = new Bitacora();
  //Recuperar el usuario actual
  principal = this.getCurrentUser();
  //Recuperar el enlace de la accion
  respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_ESTADO_COMPLETA);
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
  
  entidad.setActualizadoEl(Calendar.getInstance().getTime().getTime());
  entidad.setActualizadoPor(principal.getID());
  entidad.setIpActualizacion(direccionIp);
  respuesta= dDiaOperativo.ActualizarEstadoRegistro(entidad);
  if (respuesta.estado==false){
   throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
  }
  //Guardar en la bitacora
  ObjectMapper mapper = new ObjectMapper();
  eBitacora.setUsuario(principal.getNombre());
  eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
  eBitacora.setTabla(CisternaDao.NOMBRE_TABLA);
  eBitacora.setIdentificador(String.valueOf( entidad.getId()));
  eBitacora.setContenido( mapper.writeValueAsString(entidad));
  eBitacora.setRealizadoEl(entidad.getActualizadoEl());
  eBitacora.setRealizadoPor(entidad.getActualizadoPor());
  respuesta= dBitacora.guardarRegistro(eBitacora);
  if (respuesta.estado==false){
  throw new
  Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
  }
  respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] { entidad.getFechaActualizacion().substring(0, 9),entidad.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
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

  @RequestMapping(value = URL_VERIFICA_DIA_OPERATIVO_RELATIVA, method = RequestMethod.GET)
  public @ResponseBody Respuesta verificaDiaOperativo(HttpServletRequest httpRequest, Locale locale) {
   Respuesta respuesta = new Respuesta();
   RespuestaCompuesta oRespuesta = null;
   AuthenticatedUserDetails principal = null;
   String mensajeRespuesta = "";
   DiaOperativo eDiaOperativo = null;
   int idDiaOperativo = 0;
   ParametrosListar parametros= null;
   try {
    // Recuperar el usuario actual
    principal = this.getCurrentUser();
    // Recuperar el enlace de la accion
    oRespuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_ESTADO_COMPLETA);
    if (oRespuesta.estado == false) {
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
    // Verificar si cuenta con el permiso necesario
    if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    parametros=new ParametrosListar();
    if (httpRequest.getParameter("idDiaOperativo") != null) {
     //idDiaOperativo = (Integer.parseInt(httpRequest.getParameter("idDiaOperativo")));
    	parametros.setFiltroDiaOperativo(Integer.parseInt(httpRequest.getParameter("idDiaOperativo")));
    } else {
     throw new Exception("No se ingreso el dia operativo a actualizar");
    }
    
    if (httpRequest.getParameter("idOperacion") != null) {
        //idDiaOperativo = (Integer.parseInt(httpRequest.getParameter("idOperacion")));
    	parametros.setIdOperacion(Integer.parseInt(httpRequest.getParameter("idOperacion")));
       } else {
        throw new Exception("Error al obtener la Operacion");
       }

    oRespuesta = dDiaOperativo.recuperarDiaOperativoAnterior(parametros);
    if (oRespuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
    }
    
    if (oRespuesta.contenido.carga.size()>0){
    eDiaOperativo = (DiaOperativo) oRespuesta.contenido.carga.get(0);
    if (eDiaOperativo.getEstado()!= DiaOperativo.ESTADO_CERRADO){
     throw new Exception(gestorDiccionario.getMessage("sgo.errorDiaOperativoAnteriorNoCerrado", null, locale));
    }
   }

    respuesta.estado = true;
    respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
   } catch (Exception ex) {
    ex.printStackTrace();
    respuesta.estado = false;
    respuesta.mensaje = ex.getMessage();
   }
   return respuesta;
  }
  
 @RequestMapping(value = URL_CERRAR_ESTADO_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta cerrarDiaOperativo(@RequestBody DiaOperativo eDiaOperativo, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  Evento eEvento = null;
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dDiaOperativo.getDataSource());
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

   //eDiaOperativo.setId(idDiaOperativo);
   eDiaOperativo.setEstado(DiaOperativo.ESTADO_CERRADO);
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   respuesta = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eDiaOperativo.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eDiaOperativo));
   eBitacora.setRealizadoEl(eDiaOperativo.getActualizadoEl());
   eBitacora.setRealizadoPor(eDiaOperativo.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }

   if (!eDiaOperativo.getJustificacionCierre().isEmpty()) {
    Calendar calendar = Calendar.getInstance();
    java.util.Date now = calendar.getTime();
    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

    eEvento = new Evento();
    eEvento.setIdRegistro(eDiaOperativo.getId());
    eEvento.setDescripcion(eDiaOperativo.getJustificacionCierre());
    eEvento.setTipoEvento(Evento.TIPO_EVENTO_OBSERVACION);
    eEvento.setTipoRegistro(Evento.TIPO_REGISTRO_CIERRE);
    eEvento.setCreadoEl(Calendar.getInstance().getTime().getTime());
    eEvento.setCreadoPor(principal.getID());
    eEvento.setIpCreacion(direccionIp);
    eEvento.setFechaHoraTimestamp(currentTimestamp);
	//valida los datos que vienen del formulario
    Respuesta validacion = Utilidades.validacionXSS(eEvento, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
    respuesta = dEvento.guardarRegistro(eEvento);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
    }
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eDiaOperativo.getFechaActualizacion().substring(0, 9), eDiaOperativo.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
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

 @RequestMapping(value = URL_LISTAR_RESUMEN_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarResumenCierre(HttpServletRequest httpRequest, Locale locale) {
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

   if (httpRequest.getParameter("idDiaOperativo") != null) {
    parametros.setFiltroDiaOperativo(Integer.parseInt((httpRequest.getParameter("idDiaOperativo"))));
   }

   // Recuperar registros
   respuesta = dResumenCierre.recuperarRegistros(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_REPORTE_CIERRE_RELATIVA, method = RequestMethod.GET)
 public void mostrarReporteCierre(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  String tituloReporte ="";
  String formatoReporte="pdf";
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
   if (httpRequest.getParameter("campoOrdenamiento") != null) {
    parametros.setCampoOrdenamiento((httpRequest.getParameter("campoOrdenamiento")));
   }

   if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
    parametros.setSentidoOrdenamiento((httpRequest.getParameter("sentidoOrdenamiento")));
   }
   
   if (httpRequest.getParameter("formato") != null) {
    formatoReporte=((httpRequest.getParameter("formato")));
   }

   if (httpRequest.getParameter("idDiaOperativo") != null) {
    parametros.setFiltroDiaOperativo(Integer.parseInt((httpRequest.getParameter("idDiaOperativo"))));
   } else {
    throw new Exception("No se ingreso el dia operativo a visualizar");
   }
   
   respuesta = dResumenCierre.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   ResumenCierre cierre =null;
   HashMap<String,String> hmValor = null;
   ArrayList<HashMap<?,?>> hmRegistros = null;
   hmRegistros= new  ArrayList<HashMap<?,?>>();
   ArrayList<?> elementos =(ArrayList<?>) respuesta.contenido.getCarga();
   for (int indice=0; indice <elementos.size();indice++){
    cierre= (ResumenCierre) elementos.get(indice);
    hmValor= new HashMap<String,String>();
    hmValor.put("nombre_estacion", cierre.getNombreEstacion());
    hmValor.put("tracto_cisterna", cierre.getTractoCisterna());
    hmValor.put("metodo", cierre.getNombreMetodo());
    hmValor.put("vol_obs_des", String.valueOf(cierre.getVolumenObservadoDespachado()) );
    hmValor.put("vol_cor_des", String.valueOf(cierre.getVolumenCorregidoDespachado()) );
    hmValor.put("vol_obs_rec", String.valueOf(cierre.getVolumenObservadoRecibido()) );
    hmValor.put("vol_cor_rec", String.valueOf(cierre.getVolumenCorregidoRecibido()) );
    hmValor.put("vol_entrada", String.valueOf(cierre.getEntradaTotal()) );
    hmValor.put("vol_salida", String.valueOf(cierre.getSalidaTotal()) );
    hmValor.put("variacion", String.valueOf(cierre.getVariacion()) );
    hmValor.put("limite", String.valueOf(cierre.getLimitePermisible()) );
    hmValor.put("faltante", String.valueOf(cierre.getResultado()) );
    hmValor.put("estado", cierre.getNombreEstado());
    hmValor.put("transportista", cierre.getRazonSocialTransportista());
    hmValor.put("observaciones", cierre.getDescripcion());
    hmRegistros.add(hmValor);
   }
   respuesta = dDiaOperativo.recuperarRegistro(parametros.getFiltroDiaOperativo());
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   if (respuesta.contenido.getCarga().size() < 1){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   DiaOperativo eDiaOperativo = (DiaOperativo) respuesta.contenido.getCarga().get(0);   
   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
   String fechaOperativa = sdf.format(eDiaOperativo.getFechaOperativa()); 
   
   respuesta = dOperacion.recuperarRegistro(eDiaOperativo.getIdOperacion());
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   if (respuesta.contenido.getCarga().size() < 1){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   Operacion operacion = (Operacion) respuesta.contenido.carga.get(0);
   String titulo4=   operacion.getCliente().getRazonSocial()+ " - " +operacion.getNombre();
   
   tituloReporte="CIERRE DE DÍA OPERATIVO " +fechaOperativa;
   ByteArrayOutputStream baos = null;
   Reporteador uReporteador = new Reporteador();
   uReporteador.setRutaServlet(servletContext.getRealPath("/"));
   ArrayList<Campo> listaCampos = this.generarCamposCierre();
   ArrayList<CabeceraReporte> listaCamposCabecera = this.generarCabeceraResumenCierre();
   
   if (formatoReporte.equals(Reporteador.FORMATO_PDF)){
    baos = uReporteador.generarReporteListado(titulo4,tituloReporte,  principal.getIdentidad(),hmRegistros, listaCampos,listaCamposCabecera,1);
    response.setHeader("Content-Disposition", "inline; filename=\"reporte-cierre.pdf\"");
    response.setDateHeader("Expires", -1);
    response.setContentType("application/pdf");
    response.setCharacterEncoding("UTF-8");
    response.setContentLength(baos.size());
    response.getOutputStream().write(baos.toByteArray());
    response.getOutputStream().flush();
   } else if (formatoReporte.equals(Reporteador.FORMATO_CSV)){
	   
    listaCamposCabecera = this.generarCabeceraResumenCierreCSV();
//    response.setContentType("text/csv");
//    response.setHeader("Content-Disposition", "attachment;filename=\"reporte-cierre.csv\"");
//    response.setCharacterEncoding("UTF-8");
//    String contenidoCSV="";
//    contenidoCSV=uReporteador.generarReporteListadoCSV( hmRegistros, listaCampos, listaCamposCabecera);
//    response.getOutputStream().write(contenidoCSV.getBytes());
//    response.getOutputStream().flush();	   
	   
	    baos=uReporteador.generarReporteListadoExcel(hmRegistros, listaCampos, listaCamposCabecera,"Reporte Cierre Día");
		try {
			
			byte[] bytes = baos.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.addHeader ("Content-Disposition", "attachment; filename=\"reporte-cierre.xls\"");
			response.setContentLength(bytes.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write( bytes, 0, bytes.length ); 
		    ouputStream.flush();    
		    ouputStream.close();  
			
		} catch (IOException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
	   
	   
   }

  } catch (Exception ex) {

  }
 }

 private ArrayList<Campo> generarCamposCierre() {
  ArrayList<Campo> listaCampos = null;
  try {
   listaCampos = new ArrayList<Campo>();
   Campo eCampo = null;
   eCampo = new Campo();
   eCampo.setEtiqueta("Estacion");
   eCampo.setNombre("nombre_estacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Transportista");
   eCampo.setNombre("transportista");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(2.1f);
   listaCampos.add(eCampo);  
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Tracto/Cisterna");
   eCampo.setNombre("tracto_cisterna");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1.2f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Método");
   eCampo.setNombre("metodo");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Obs");
   eCampo.setNombre("vol_obs_des");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);

   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Obs");
   eCampo.setNombre("vol_cor_des");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Obs");
   eCampo.setNombre("vol_obs_rec");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Obs");
   eCampo.setNombre("vol_cor_rec");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Entrada");
   eCampo.setNombre("vol_entrada");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);

   eCampo = new Campo();
   eCampo.setEtiqueta("Vol. Salida");
   eCampo.setNombre("vol_salida");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Variacion");
   eCampo.setNombre("variacion");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Limite");
   eCampo.setNombre("limite");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.6f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Faltante");
   eCampo.setNombre("faltante");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);

   eCampo = new Campo();
   eCampo.setEtiqueta("Estado");
   eCampo.setNombre("estado");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.9f);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Observaciones");
   eCampo.setNombre("observaciones");
   eCampo.setTipo(Campo.TIPO_MEMO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.9f);
   listaCampos.add(eCampo);  
   
   
  } catch (Exception ex) {

  }
  return listaCampos;
 }
 
 
 private ArrayList<CabeceraReporte> generarCabeceraResumenCierreCSV(){
  ArrayList<CabeceraReporte> listaCamposCabecera =null;
  CabeceraReporte cabeceraReporte = null;
  try {
   listaCamposCabecera = new ArrayList<CabeceraReporte>();

   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estacion");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Transportista");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Tracto/Cisterna");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Metodo");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);

   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Entrada");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Salida");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Variacion");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Limite");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Faltante");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estado");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   
  }catch(Exception ex){
   
  }
  return listaCamposCabecera;
 }
 
 private ArrayList<CabeceraReporte> generarCabeceraResumenCierre(){
  ArrayList<CabeceraReporte> listaCamposCabecera =null;
  CabeceraReporte cabeceraReporte = null;
  try {
   listaCamposCabecera = new ArrayList<CabeceraReporte>();
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("");
   cabeceraReporte.setColspan(4);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Despachado en Planta");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Descargado en Operación");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Referencia Control");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("");
   cabeceraReporte.setColspan(4);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);

   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Transportista");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Tracto/Cisterna");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Metodo");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);

   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Entrada");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Salida");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Variacion");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Limite");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Faltante");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estado");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
  }catch(Exception ex){
   
  }
  return listaCamposCabecera;
 }
 
/* private ArrayList<CabeceraReporte> generarCabeceraResumenCierre(){
  ArrayList<CabeceraReporte> listaCamposCabecera =null;
  CabeceraReporte cabeceraReporte = null;
  try {
   listaCamposCabecera = new ArrayList<CabeceraReporte>();
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(2);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Tracto/Cisterna");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(2);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Método");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(2);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Despachado en Planta");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Descargado en Operación");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Referencia Control");
   cabeceraReporte.setColspan(2);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. Obs.");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Vol. 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Entrada");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("V. Salida");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Variación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Limite");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Faltante");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estado");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
  }catch(Exception ex){
   
  }
  return listaCamposCabecera;
 }*/

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }

}
