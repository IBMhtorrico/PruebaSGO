package sgo.servicio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
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

import sgo.datos.BitacoraDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.entidad.Bitacora;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DiaOperativoControlador {
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
 private DiaOperativoDao dDiaOperativo;
 @Autowired
 private PlanificacionDao dPlanificacion;
 @Autowired
 private OperacionDao dOperacion;

 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/dia_operativo";
 private static final String URL_GESTION_RELATIVA = "/dia_operativo";
 private static final String URL_GUARDAR_COMPLETA = "/admin/dia_operativo/crear";
 private static final String URL_GUARDAR_RELATIVA = "/dia_operativo/crear";
 private static final String URL_LISTAR_COMPLETA = "/admin/dia_operativo/listar";
 private static final String URL_LISTAR_RELATIVA = "/dia_operativo/listar";
 private static final String URL_ELIMINAR_COMPLETA = "/admin/dia_operativo/eliminar";
 private static final String URL_ELIMINAR_RELATIVA = "/dia_operativo/eliminar";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/dia_operativo/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/dia_operativo/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/dia_operativo/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/dia_operativo/recuperar";
 private static final String URL_RECUPERAR_ULTIMO_DIA_POR_OPERACION_RELATIVA = "/dia_operativo/recuperarUltimoDiaPorOperacion";
 private static final String URL_RECUPERAR_ULTIMO_DIA_COMPLETA = "/admin/dia_operativo/recuperar-ultimo-dia";
 private static final String URL_RECUPERAR_ULTIMO_DIA_RELATIVA = "/dia_operativo/recuperar-ultimo-dia";
 private static final String URL_VALIDA_PLANIFICACIONES_COMPLETA = "/admin/dia_operativo/valida-planificaciones-x-dia-operativo";
 private static final String URL_VALIDA_PLANIFICACIONES_RELATIVA = "/dia_operativo/valida-planificaciones-x-dia-operativo";
 private static final String URL_LISTAR_POR_DESCARGA_COMPLETA = "/admin/dia_operativo/listarxdescarga";
 private static final String URL_LISTAR_POR_DESCARGA_RELATIVA = "/dia_operativo/listarxdescarga";

 @RequestMapping("/dia_operativo")
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<Enlace> listaEnlaces = null;
  RespuestaCompuesta respuesta = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<Enlace>) respuesta.contenido.carga;
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "mantenimiento/dia_operativo.jsp");
   vista.addObject("vistaJS", "mantenimiento/dia_operativo.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
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

   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }

   if (httpRequest.getParameter("filtroFechaFinal") != null) {
    parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
   }

   if (httpRequest.getParameter("filtroFechaInicio") != null) {
    parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
   }
   parametros.setPaginacion(Constante.CON_PAGINACION);

   // Recuperar registros
   respuesta = dDiaOperativo.recuperarRegistros(parametros);
   
   //esto para buscar y asignar los datos de la operacion
   if(respuesta.contenido.carga.size() > 0){
	   for(int i = 0; i< respuesta.contenido.carga.size(); i++){
		   DiaOperativo entidad = (DiaOperativo) respuesta.contenido.carga.get(i);
		   RespuestaCompuesta respuestaOperacion = dOperacion.recuperarRegistro(entidad.getOperacion().getId());
		   if(respuestaOperacion.contenido.carga.size() > 0){
			   Operacion entidadOperacion = (Operacion) respuestaOperacion.contenido.carga.get(0);
			   entidad.setOperacion(entidadOperacion);
		   }
	   }
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_LISTAR_POR_DESCARGA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarDiasOperativosDescarga(HttpServletRequest httpRequest, Locale locale) {
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
    throw new Exception(mensajeRespuesta);
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   /*
    * if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
    * mensajeRespuesta =
    * gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale); throw new
    * Exception(mensajeRespuesta); }
    */
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

   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }

   if (httpRequest.getParameter("filtroFechaFinal") != null) {
    parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
   }

   if (httpRequest.getParameter("filtroFechaInicio") != null) {
    parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
   }

   // Recuperar registros
   // Recuperamos los dias operativos
   respuesta = dDiaOperativo.recuperarRegistros(parametros);
   if (!respuesta.estado) {
    respuesta.estado = false;
    respuesta.contenido = null;
    respuesta.mensaje = ("Error en la búsqueda de Días Operativos.");
    return respuesta;
   }

   // Si respuesta tiene registros, recuperamos todas sus planificaciones.
   if (!respuesta.contenido.carga.isEmpty()) {
    String descProductoVolRequerido = "";
    List<DiaOperativo> diasOperativos = new ArrayList<DiaOperativo>();
    Iterator itr = respuesta.contenido.carga.iterator();
    while (itr.hasNext()) {
     DiaOperativo eDiaOperativo = (DiaOperativo) itr.next();
     ParametrosListar parametros2 = new ParametrosListar();
     parametros2.setPaginacion(Constante.SIN_PAGINACION);
     parametros2.setCampoOrdenamiento("id");
     parametros2.setCampoOrdenamiento("asc");
     parametros2.setFiltroDiaOperativo(eDiaOperativo.getId());
     RespuestaCompuesta respuestaPlanificaciones = dPlanificacion.recuperarRegistros(parametros2);
     descProductoVolRequerido = "";
     if (!respuestaPlanificaciones.estado) {
      throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
     }

     if (!respuesta.contenido.carga.isEmpty()) {
      Iterator itrPlanifiacion = respuestaPlanificaciones.contenido.carga.iterator();
      List<Planificacion> listaPlanificaciones = new ArrayList<Planificacion>();
      while (itrPlanifiacion.hasNext()) {
       Planificacion ePlanificacion = (Planificacion) itrPlanifiacion.next();
       // descProductoVolRequerido = "";
       if (descProductoVolRequerido.isEmpty()) {
        descProductoVolRequerido = ePlanificacion.getDescProductoVolRequerido();
       } else {
        descProductoVolRequerido = descProductoVolRequerido + ", " + ePlanificacion.getDescProductoVolRequerido();
       }
       listaPlanificaciones.add(ePlanificacion);
      }
      // eDiaOperativo.setPlanificaciones(listaPlanificaciones);
      eDiaOperativo.setDetalleProductoSolicitado(descProductoVolRequerido);
     }
     diasOperativos.add(eDiaOperativo);
    }
    respuesta.contenido.carga = (List) diasOperativos;
   }
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
   respuesta = dDiaOperativo.recuperarRegistro(ID);
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

 @RequestMapping(value = URL_RECUPERAR_ULTIMO_DIA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 Respuesta recuperaUltimoDia(int idOperacion, Locale locale) {
  Respuesta respuesta = null;
  RespuestaCompuesta oRespuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_ULTIMO_DIA_COMPLETA);
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Recuperar el registro
 //además el último día operativo no se encuentre en estado anulado = 6
   String txtQuery = "  AND estado <> 6 ";
   respuesta = dDiaOperativo.recuperarUltimoDiaOperativo(idOperacion, txtQuery);
   // Verifica el resultado de la accion
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   String formatoFechaUsuario = "dd/MM/yyyy";
   SimpleDateFormat formateador = null;
   java.util.Date ultimoDia = null;

   if (respuesta.valor == null) {
	   //Esto para que busque la fecha de inicio de planificación de la operación
	   RespuestaCompuesta rpta = dOperacion.recuperarRegistro(idOperacion);
	   if (rpta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }

	   Operacion op = (Operacion) rpta.contenido.carga.get(0);
	   ultimoDia = op.getFechaInicioPlanificacion();
	   if(ultimoDia == null){
		   throw new Exception(gestorDiccionario.getMessage("sgo.fechaInicioPlanificacionNula", new Object[] {op.getNombre()}, locale));
	   }
    //ultimoDia = Calendar.getInstance().getTime();
    formateador = new SimpleDateFormat(formatoFechaUsuario);
    respuesta.valor = formateador.format(ultimoDia);
   } else {
	   
	  ParametrosListar parametros = new ParametrosListar();
	  parametros.setFiltroFechaInicio(respuesta.valor);
	  parametros.setFiltroFechaFinal(respuesta.valor);
	  parametros.setFiltroOperacion(idOperacion);
	  RespuestaCompuesta estadoDiaOperativo = dDiaOperativo.recuperarRegistros(parametros);
	  if (estadoDiaOperativo.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	  DiaOperativo eDiaOperativo = (DiaOperativo) estadoDiaOperativo.contenido.carga.get(0);
	  if (eDiaOperativo.getEstado() == DiaOperativo.ESTADO_ANULADO){
		  //para asignar como día operativo el día anulado
		  formateador = new SimpleDateFormat(Constante.FORMATO_FECHA_ESTANDAR);
		  Calendar calendario = Calendar.getInstance();
		  calendario.setTime(formateador.parse(eDiaOperativo.getFechaOperativa().toString()));
		  //calendario.add(Calendar.DATE, 1);
		  formateador = new SimpleDateFormat(formatoFechaUsuario);
		  respuesta.valor = formateador.format(calendario.getTime());
	  } else {
	  
	    formateador = new SimpleDateFormat(Constante.FORMATO_FECHA_ESTANDAR);
	    Calendar calendario = Calendar.getInstance();
	    calendario.setTime(formateador.parse(respuesta.valor));
	    calendario.add(Calendar.DATE, 1);
	    formateador = new SimpleDateFormat(formatoFechaUsuario);
	    respuesta.valor = formateador.format(calendario.getTime());
	  }
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody DiaOperativo eDiaOperativo, HttpServletRequest peticionHttp, Locale locale) {
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
   this.transaccion = new DataSourceTransactionManager(dDiaOperativo.getDataSource());
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
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setCreadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   eDiaOperativo.setIpCreacion(direccionIp);
   respuesta = dDiaOperativo.guardarRegistro(eDiaOperativo);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject
                                             // via @Autowired
   ContenidoAuditoria = mapper.writeValueAsString(eDiaOperativo);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eDiaOperativo.getCreadoEl());
   eBitacora.setRealizadoPor(eDiaOperativo.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso",
     new Object[] { eDiaOperativo.getFechaCreacion().substring(0, 9), eDiaOperativo.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
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
 RespuestaCompuesta actualizarRegistro(@RequestBody DiaOperativo eDiaOperativo, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dDiaOperativo.getDataSource());
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
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   respuesta = dDiaOperativo.actualizarRegistro(eDiaOperativo);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eDiaOperativo.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eDiaOperativo));
   eBitacora.setRealizadoEl(eDiaOperativo.getActualizadoEl());
   eBitacora.setRealizadoPor(eDiaOperativo.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eDiaOperativo.getFechaActualizacion().substring(0, 9), eDiaOperativo.getFechaActualizacion().substring(10),
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

 @RequestMapping(value = URL_ELIMINAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta eliminarRegistro(HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  int idRegistro = 0;
  Bitacora eBitacora = null;
  DiaOperativo eDiaOperativo = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dDiaOperativo.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_ELIMINAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Verificar si el id ha sido enviado
   if (peticionHttp.getParameter("ID") != null) {
    idRegistro = (Integer.parseInt(peticionHttp.getParameter("ID")));
   }
   if (idRegistro == 0) {
    throw new Exception(gestorDiccionario.getMessage("sgo.eliminarFallidoNoId", null, locale));
   }
   // Verificar la existencia del registro
   respuesta = dDiaOperativo.recuperarRegistro(idRegistro);
   if (respuesta.estado == false) {
    if (idRegistro == 0) {
     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
    }
   }
   // Asignar el registro
   eDiaOperativo = (DiaOperativo) respuesta.contenido.carga.get(0);
   // Eliminar el registro

   respuesta = dDiaOperativo.eliminarRegistro(idRegistro);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.eliminarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora = new Bitacora();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ELIMINAR_COMPLETA);
   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eDiaOperativo.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eDiaOperativo));
   eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
   eBitacora.setRealizadoPor(principal.getID());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   // Asignar el mensaje de confirmacion
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.eliminarExitoso", new Object[] { principal.getIdentidad(), eBitacora.getFechaRealizacion().substring(0, 9),
     eBitacora.getFechaRealizacion().substring(10) }, locale);
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
 
 @RequestMapping(value = URL_VALIDA_PLANIFICACIONES_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta validaPlanificacionesXDiaOperativo(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_VALIDA_PLANIFICACIONES_COMPLETA);
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
   if (httpRequest.getParameter("filtroFechaPlanificada") != null) {
    parametros.setFiltroFechaPlanificada((httpRequest.getParameter("filtroFechaPlanificada")));
   }
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }

   // Recuperar registros
   respuesta = dDiaOperativo.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   for (int contador = 0; contador < respuesta.contenido.carga.size(); contador++) {
	   DiaOperativo eDiaOperativo = (DiaOperativo) respuesta.contenido.carga.get(contador);
	   ParametrosListar parametros2 = new ParametrosListar();
	   parametros2.setPaginacion(Constante.SIN_PAGINACION);
	   parametros2.setCampoOrdenamiento("id");
	   parametros2.setCampoOrdenamiento("asc");
	   parametros2.setFiltroDiaOperativo(eDiaOperativo.getId());
	   RespuestaCompuesta respuestaPlanificaciones = dPlanificacion.recuperarRegistros(parametros2);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   if(respuestaPlanificaciones.contenido.carga.size() > 0){
		   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.fechaPlanificadaDuplicada", null, locale));
	   }
   }
   respuesta.estado = true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.planificacion.fechaPlanificadaCorrecta", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 //TODO
 @RequestMapping(value = URL_RECUPERAR_ULTIMO_DIA_POR_OPERACION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody Respuesta recuperaUltimoDiaPorOperacion(int idOperacion, Locale locale) {
  Respuesta respuesta = null;
  RespuestaCompuesta oRespuesta = null;
  AuthenticatedUserDetails principal = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_ULTIMO_DIA_COMPLETA);
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   // Recuperar el registro
   respuesta = dDiaOperativo.recuperarUltimoDiaOperativo(idOperacion, "");
   // Verifica el resultado de la accion
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   if (respuesta.valor == null) {
	   respuesta.valor = dDiaOperativo.recuperarFechaActual().valor;
	   respuesta.estado = true;
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
   ex.printStackTrace();
   respuesta.estado = false;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
}
