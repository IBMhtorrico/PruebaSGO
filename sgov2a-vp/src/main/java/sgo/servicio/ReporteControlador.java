package sgo.servicio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.BitacoraDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.GenericoDao;
import sgo.datos.JornadaDao;
import sgo.datos.OperacionDao;
import sgo.datos.OperarioDao;
import sgo.datos.ReporteDao;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Reporteador;

@Controller
public class ReporteControlador {
	
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora;
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private OperarioDao dOperario;
 @Autowired
 private DiaOperativoDao dDiaOperativo;
 @Autowired
 private JornadaDao dJornada;
 @Autowired
 private GenericoDao dGenerico;
 @Autowired
 private ReporteDao dReporte;
 @Autowired
 ServletContext servletContext;
 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales 
 private static final String URL_REPORTES_COMPLETA = "/admin/reporte";
 private static final String URL_REPORTES_RELATIVA = "/reporte"; 
 private static final String URL_LIQUIDACION_CISTERNA_COMPLETA = "/admin/reporte/liquidacion-cisterna";
 private static final String URL_LIQUIDACION_CISTERNA_RELATIVA = "/reporte/liquidacion-cisterna"; 
 
 //Agregado por 9000002570================================
 private static final String URL_TIEMPO_ETAPA_COMPLETA = "/admin/reporte/tiempo_etapa-reporte";
 private static final String URL_TIEMPO_ETAPA_RELATIVA = "/reporte/tiempo_etapa-reporte"; 
 
 private static final String URL_ATENCION_PEDIDO_COMPLETA = "/admin/reporte/atencion_pedido-reporte";
 private static final String URL_ATENCION_PEDIDO_RELATIVA = "/reporte/atencion_pedido-reporte"; 
 //==================================================
 private static final String URL_CONCILIACION_VOLUMETRICA_COMPLETA = "/admin/reporte/conciliacion-volumetrica";
 private static final String URL_CONCILIACION_VOLUMETRICA_RELATIVA = "/reporte/conciliacion-volumetrica"; 
 private static final String URL_CONCILIACION_VOLUMETRICA_ESTACION_COMPLETA = "/admin/reporte/conciliacion-volumetrica-estacion";
 private static final String URL_CONCILIACION_VOLUMETRICA_ESTACION_RELATIVA = "/reporte/conciliacion-volumetrica-estacion"; 
 private static final String URL_REPORTE4_COMPLETA = "/admin/reportes/reporte4";
 private static final String URL_REPORTE4_RELATIVA = "/reportes/reporte4"; 
 private static final String URL_VALIDA_PERMISO_REPORTE = "/reporte/valida-permiso-reporte";
 private List<Object> data;
 //private Map<String,Object> parametros = new HashMap<String,Object>();
 //private Map<String,Object> listaData = new HashMap<String,Object>();
 //private String nombreArchivoDownload;
 private static String RUTA_REPORTE = null ;
 //private String jasperFileName;
 private Class clase;
 
 
 private HashMap<String, String> recuperarMapaValores(Locale locale) {
  HashMap<String, String> mapaValores = new HashMap<String, String>();
  try {
   mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
   mapaValores.put("ESTADO_INACTIVO", String.valueOf(Constante.ESTADO_INACTIVO));
   mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
   mapaValores.put("TEXTO_INACTIVO", gestorDiccionario.getMessage("sgo.estadoInactivo", null, locale));
   mapaValores.put("TEXTO_ACTIVO", gestorDiccionario.getMessage("sgo.estadoActivo", null, locale));
   mapaValores.put("TEXTO_TODOS", gestorDiccionario.getMessage("sgo.filtroTodos", null, locale));
   mapaValores.put("TEXTO_BUSCAR", gestorDiccionario.getMessage("sgo.buscarElemento", null, locale));

   mapaValores.put("TITULO_AGREGAR_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioAgregar", null, locale));
   mapaValores.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar", null, locale));
   mapaValores.put("TITULO_DETALLE_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioVer", null, locale));
   mapaValores.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado", null, locale));
  
   mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));

   mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer", null, locale));
   mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_EXPORTAR", gestorDiccionario.getMessage("sgo.etiquetaBotonExportar", null, locale));

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

 @RequestMapping(URL_REPORTES_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  ArrayList<?> listaOperaciones = null;
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  HashMap<String, String> mapaValores = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_REPORTES_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;
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
   vista.addObject("vistaJSP", "otros_reportes/reportes.jsp");
   vista.addObject("vistaJS", "otros_reportes/reportes.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("operaciones", listaOperaciones);
   vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
   vista.addObject("mapaValores", mapaValores);
  } catch (Exception ex) {

  }
  return vista;
 }


@RequestMapping(value = URL_LIQUIDACION_CISTERNA_RELATIVA, method = RequestMethod.GET)
 public void mostrarReporteLiquidacionCisterna(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  String formatoReporte="";
  String fechaInicial ="";
  String fechaFinal ="";
  String idOperacion ="";
  String txtOperacion = "";
  try {
   // Recuperar el usuario actual
   principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_LIQUIDACION_CISTERNA_COMPLETA);
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
   //PARAMETROS DEL REPORTE   
   if (httpRequest.getParameter("formato") != null) {
	   formatoReporte=httpRequest.getParameter("formato");
   }   
   if (httpRequest.getParameter("fechaInicial") != null) {
	   fechaInicial=httpRequest.getParameter("fechaInicial");
   }   
   if (httpRequest.getParameter("fechaFinal") != null) {
	   fechaFinal=httpRequest.getParameter("fechaFinal");
   }   
   if (httpRequest.getParameter("idOperacion") != null) {
	   idOperacion=httpRequest.getParameter("idOperacion");
   }
   if (httpRequest.getParameter("txtOperacion") != null) {
	   txtOperacion=httpRequest.getParameter("txtOperacion");
   }
   
   String where ="";
   ArrayList<String> filtros= new ArrayList<String>();   
   Map<String,Object> parametrosReporte = new HashMap<String,Object>();   
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   Date fechaIni=sdf.parse(fechaInicial);
   Date fechaFin=sdf.parse(fechaFinal);

   if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
    filtros.add(" fechaOperativa between '" + fechaInicial +"' AND '" + fechaFinal+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
	   parametrosReporte.put("fechaFinal", fechaFin);
   } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
    filtros.add(" fechaOperativa >= '" + fechaInicial+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
   }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
    filtros.add(" fechaOperativa <= '" + fechaFinal+"'");
	   parametrosReporte.put("fechaFinal", fechaFin);
   }   
   if (idOperacion.length()>0){
    filtros.add(" idOperacion='"+idOperacion+"'");
	   parametrosReporte.put("id_operacion", idOperacion);
   }
   if (txtOperacion.length()>0){
	   byte[] operacion = DatatypeConverter.parseBase64Binary(txtOperacion);
	   parametrosReporte.put("operacion",new String(operacion).toUpperCase());
   }
   if (formatoReporte.length()>0){
	   parametrosReporte.put("formato", formatoReporte);
   }   
   if (filtros.size()>0){
	    where =" where " +  StringUtils.join(filtros," AND ");
   }   
   
   String sql ="select * from sgo.v_reporte_liquidacion_cisterna " + where;
   respuesta = dReporte.recuperarRegistrosJasper(sql);
   if (respuesta.estado==false){
	    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
	    throw new Exception(mensajeRespuesta);
   }
   
   String jasperFileName= "";
   String logoFileName= servletContext.getRealPath("/")+"pages"+File.separator+"tema"+File.separator+"app"+File.separator+"imagen"+File.separator+"logo.jpg";
   parametrosReporte.put("rutaImagen", logoFileName);
   parametrosReporte.put("usuario",principal.getIdentidad());
   data=(List<Object>) respuesta.contenido.getCarga();   
   ByteArrayOutputStream baos = null; 
   Reporteador uReporteador = new Reporteador();
   parametrosReporte.put("REPORT_LOCALE", new Locale("en", "US")); 
   
   if(formatoReporte.equals(Reporteador.FORMATO_PDF)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Liquidacion_Cisterna.jasper";
	   baos = uReporteador.generarPDF(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.pdf\"");
	   response.setDateHeader("Expires", -1);
	   response.setContentType("application/pdf");   
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
   }else if(formatoReporte.equals(Reporteador.FORMATO_EXCEL)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Liquidacion_Cisterna_Excel.jasper";
	   baos = uReporteador.generarEXCEL(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.xls\"");
	   response.setContentType("application/xls");  
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
   }
   

  } catch (Exception ex) {
	  ex.printStackTrace();
  }
 }

// Agregado por req 9000002570 =====================
@RequestMapping(value = URL_TIEMPO_ETAPA_RELATIVA, method = RequestMethod.GET)
public void mostrarReporteTiempoEtapa(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
 RespuestaCompuesta respuesta = null;
 AuthenticatedUserDetails principal = null;
 String mensajeRespuesta = "";
 String formatoReporte="";
 String fechaInicial ="";
 String fechaFinal ="";
 String idOperacion ="";
 String txtOperacion = "";
 try {
  // Recuperar el usuario actual
  principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  // Recuperar el enlace de la accion
  respuesta = dEnlace.recuperarRegistro(URL_TIEMPO_ETAPA_COMPLETA);
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
  //PARAMETROS DEL REPORTE   
  if (httpRequest.getParameter("formato") != null) {
	   formatoReporte=httpRequest.getParameter("formato");
  }   
  if (httpRequest.getParameter("fechaInicial") != null) {
	   fechaInicial=httpRequest.getParameter("fechaInicial");
  }   
  if (httpRequest.getParameter("fechaFinal") != null) {
	   fechaFinal=httpRequest.getParameter("fechaFinal");
  }   
  if (httpRequest.getParameter("idOperacion") != null) {
	   idOperacion=httpRequest.getParameter("idOperacion");
  }
  if (httpRequest.getParameter("txtOperacion") != null) {
	   txtOperacion=httpRequest.getParameter("txtOperacion");
  }
  
  String where ="";
  ArrayList<String> filtros= new ArrayList<String>();   
  Map<String,Object> parametrosReporte = new HashMap<String,Object>();   
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  Date fechaIni=sdf.parse(fechaInicial);
  Date fechaFin=sdf.parse(fechaFinal);

  if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
   filtros.add(" fecha_operativa between '" + fechaInicial +"' AND '" + fechaFinal+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
	   parametrosReporte.put("fechaFinal", fechaFin);
  } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
   filtros.add(" fecha_operativa >= '" + fechaInicial+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
  }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
   filtros.add(" fecha_operativa <= '" + fechaFinal+"'");
	   parametrosReporte.put("fechaFinal", fechaFin);
  }   
  if (idOperacion.length()>0){
   filtros.add(" t3.id_operacion="+idOperacion+" ");
	   parametrosReporte.put("idOperacion", idOperacion);
  }
  if (txtOperacion.length()>0){
	   byte[] operacion = DatatypeConverter.parseBase64Binary(txtOperacion);
	   parametrosReporte.put("operacion",new String(operacion).toUpperCase());
  }
  if (formatoReporte.length()>0){
	   parametrosReporte.put("formato", formatoReporte);
  }   
  if (filtros.size()>0){
	    where =" where " +  StringUtils.join(filtros," AND ");
  }   
  
  respuesta = dReporte.recuperarRegistrosTiempoEtapas(parametrosReporte, where);
  if (respuesta.estado==false){
	    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
	    throw new Exception(mensajeRespuesta);
  }
  
  String jasperFileName= "";
  String logoFileName= servletContext.getRealPath("/")+"pages"+File.separator+"tema"+File.separator+"app"+File.separator+"imagen"+File.separator+"logo.jpg";
  parametrosReporte.put("rutaImagen", logoFileName);
  parametrosReporte.put("usuario",principal.getIdentidad());
  data=(List<Object>) respuesta.contenido.getCarga();   
  ByteArrayOutputStream baos = null; 
  Reporteador uReporteador = new Reporteador();
  parametrosReporte.put("REPORT_LOCALE", new Locale("en", "US")); 
  
  if(formatoReporte.equals(Reporteador.FORMATO_PDF)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Tiempo_Etapa.jasper";
	   baos = uReporteador.generarPDFColDinamicas(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.pdf\"");
	   response.setDateHeader("Expires", -1);
	   response.setContentType("application/pdf");   
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }else if(formatoReporte.equals(Reporteador.FORMATO_EXCEL)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Liquidacion_Cisterna_Excel.jasper";
	   baos = uReporteador.generarEXCEL(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.xls\"");
	   response.setContentType("application/xls");  
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }
  

 } catch (Exception ex) {
	  ex.printStackTrace();
 }
}

@RequestMapping(value = URL_ATENCION_PEDIDO_RELATIVA, method = RequestMethod.GET)
public void mostrarReporteAtencionPedido(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
 RespuestaCompuesta respuesta = null;
 AuthenticatedUserDetails principal = null;
 String mensajeRespuesta = "";
 String formatoReporte="";
 String fechaInicial ="";
 String fechaFinal ="";
 String idOperacion ="";
 String txtOperacion = "";
 try {
  // Recuperar el usuario actual
  principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  // Recuperar el enlace de la accion
  respuesta = dEnlace.recuperarRegistro(URL_ATENCION_PEDIDO_COMPLETA);
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
  //PARAMETROS DEL REPORTE   
  if (httpRequest.getParameter("formato") != null) {
	   formatoReporte=httpRequest.getParameter("formato");
  }   
  if (httpRequest.getParameter("fechaInicial") != null) {
	   fechaInicial=httpRequest.getParameter("fechaInicial");
  }   
  if (httpRequest.getParameter("fechaFinal") != null) {
	   fechaFinal=httpRequest.getParameter("fechaFinal");
  }   
  if (httpRequest.getParameter("idOperacion") != null) {
	   idOperacion=httpRequest.getParameter("idOperacion");
  }
  if (httpRequest.getParameter("txtOperacion") != null) {
	   txtOperacion=httpRequest.getParameter("txtOperacion");
  }
  
  String where ="";
  ArrayList<String> filtros= new ArrayList<String>();   
  Map<String,Object> parametrosReporte = new HashMap<String,Object>();   
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  Date fechaIni=sdf.parse(fechaInicial);
  Date fechaFin=sdf.parse(fechaFinal);

  if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
   filtros.add(" fechaplanificada between '" + fechaInicial +"' AND '" + fechaFinal+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
	   parametrosReporte.put("fechaFinal", fechaFin);
  } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
   filtros.add(" fechaplanificada >= '" + fechaInicial+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
  }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
   filtros.add(" fechaplanificada <= '" + fechaFinal+"'");
	   parametrosReporte.put("fechaFinal", fechaFin);
  }   
  if (idOperacion.length()>0){
   filtros.add(" idOperacion='"+idOperacion+"'");
	   parametrosReporte.put("id_operacion", idOperacion);
  }
  if (txtOperacion.length()>0){
	   byte[] operacion = DatatypeConverter.parseBase64Binary(txtOperacion);
	   parametrosReporte.put("operacion",new String(operacion).toUpperCase());
  }
  if (formatoReporte.length()>0){
	   parametrosReporte.put("formato", formatoReporte);
  }   
  if (filtros.size()>0){
	    where =" where " +  StringUtils.join(filtros," AND ");
  }   
  
  String sql ="select * from sgo.v_reporte_atencion_pedido " + where;
  respuesta = dReporte.recuperarRegistrosJasper(sql);
  if (respuesta.estado==false){
	    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
	    throw new Exception(mensajeRespuesta);
  }
  
  String jasperFileName= "";
  String logoFileName= servletContext.getRealPath("/")+"pages"+File.separator+"tema"+File.separator+"app"+File.separator+"imagen"+File.separator+"logo.jpg";
  parametrosReporte.put("rutaImagen", logoFileName);
  parametrosReporte.put("usuario",principal.getIdentidad());
  data=(List<Object>) respuesta.contenido.getCarga();   
  ByteArrayOutputStream baos = null; 
  Reporteador uReporteador = new Reporteador();
  parametrosReporte.put("REPORT_LOCALE", new Locale("en", "US")); 
  
  if(formatoReporte.equals(Reporteador.FORMATO_PDF)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Atencion_Pedido.jasper";
	   baos = uReporteador.generarPDF(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.pdf\"");
	   response.setDateHeader("Expires", -1);
	   response.setContentType("application/pdf");   
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }else if(formatoReporte.equals(Reporteador.FORMATO_EXCEL)){
	   jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Atencion_Pedido.jasper";
	   baos = uReporteador.generarEXCEL(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteLiquidacionCisterna.xls\"");
	   response.setContentType("application/xls");  
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }
  

 } catch (Exception ex) {
	  ex.printStackTrace();
 }
}
//=======================================

@RequestMapping(value = URL_CONCILIACION_VOLUMETRICA_RELATIVA, method = RequestMethod.GET)
public void mostrarReporteConciliacionVolumetrica(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
 RespuestaCompuesta respuesta = null;
 AuthenticatedUserDetails principal = null;
 String mensajeRespuesta = "";
 String formatoReporte="";
 String fechaInicial ="";
 String fechaFinal ="";
 String idOperacion ="";
 String txtOperacion="";
 try {
  // Recuperar el usuario actual
  principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  // Recuperar el enlace de la accion
  respuesta = dEnlace.recuperarRegistro(URL_CONCILIACION_VOLUMETRICA_COMPLETA);
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
  //PARAMETROS DEL REPORTE   
  if (httpRequest.getParameter("formato") != null) {
	  formatoReporte=httpRequest.getParameter("formato");
  }   
  if (httpRequest.getParameter("fechaInicial") != null) {
	  fechaInicial=httpRequest.getParameter("fechaInicial");
  }   
  if (httpRequest.getParameter("fechaFinal") != null) {
	  fechaFinal=httpRequest.getParameter("fechaFinal");
  }   
  if (httpRequest.getParameter("idOperacion") != null) {
	  idOperacion=httpRequest.getParameter("idOperacion");
  }
  if (httpRequest.getParameter("txtOperacion") != null) {
	  txtOperacion=httpRequest.getParameter("txtOperacion");
  }
   
  String where ="";
  ArrayList<String> filtros= new ArrayList<String>();   
  Map<String,Object> parametrosReporte = new HashMap<String,Object>();  
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  Date fechaIni=sdf.parse(fechaInicial);
  Date fechaFin=sdf.parse(fechaFinal);	

  if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
   filtros.add(" diaoperativo between '" + fechaInicial +"' AND '" + fechaFinal+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
	   parametrosReporte.put("fechaFinal", fechaFin);
  } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
   filtros.add(" diaoperativo >= '" + fechaInicial+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
  }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
   filtros.add(" diaoperativo <= '" + fechaFinal+"'");
	   parametrosReporte.put("fechaFinal", fechaFin);
  }   
  if (idOperacion.length()>0){
   filtros.add(" idoperacion='"+idOperacion+"'");
	   parametrosReporte.put("id_operacion", idOperacion);
  }
  if (txtOperacion.length()>0){
	   byte[] operacion = DatatypeConverter.parseBase64Binary(txtOperacion);
	   parametrosReporte.put("operacion",new String(operacion).toUpperCase());
  }
  if (formatoReporte.length()>0){
	   parametrosReporte.put("formato", formatoReporte);
  }
  if (filtros.size()>0){
	    where =" where " +  StringUtils.join(filtros," AND ");
  }   
  
  String sql ="select * from sgo.v_reporte_conciliacion_volumetrica " + where;
  respuesta = dReporte.recuperarRegistrosJasper(sql);
  if (respuesta.estado==false){
	    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
	    throw new Exception(mensajeRespuesta);
  }
  
  String jasperFileName= "";
  String logoFileName= servletContext.getRealPath("/")+"pages"+File.separator+"tema"+File.separator+"app"+File.separator+"imagen"+File.separator+"logo.jpg";
  parametrosReporte.put("rutaImagen", logoFileName);
  parametrosReporte.put("usuario",principal.getIdentidad());
  data=(List<Object>) respuesta.contenido.getCarga(); 
  ByteArrayOutputStream baos = null; 
  Reporteador uReporteador = new Reporteador();	
  parametrosReporte.put("REPORT_LOCALE", new Locale("en", "US")); 
  
  if(formatoReporte.equals(Reporteador.FORMATO_PDF)){
	  jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Conciliacion_Volumetrica.jasper";
	   baos = uReporteador.generarPDF(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteConciliacionVolumetrica.pdf\"");
	   response.setDateHeader("Expires", -1);
	   response.setContentType("application/pdf");   
	   response.setContentLength(baos.size()); 
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }else if(formatoReporte.equals(Reporteador.FORMATO_EXCEL)){
	  jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Conciliacion_Volumetrica_Excel.jasper";
	   baos = uReporteador.generarEXCEL(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteConciliacionVolumetrica.xls\"");
	   response.setContentType("application/xls");  
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }

 } catch (Exception ex) {
	  ex.printStackTrace();
 }
}
 

@RequestMapping(value = URL_CONCILIACION_VOLUMETRICA_ESTACION_RELATIVA, method = RequestMethod.GET)
public void mostrarReporteConciliacionVolumetricaEstacion(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
 RespuestaCompuesta respuesta = null;
 ParametrosListar parametros = null;
 AuthenticatedUserDetails principal = null;
 String mensajeRespuesta = "";
 //String tituloReporte ="";
 String formatoReporte="pdf";
 //ArrayList<HashMap<?,?>> hmRegistros = null;
 String fechaInicial ="";
 String fechaFinal ="";
 String idOperacion ="";
 String txtOperacion="";
 try {
  // Recuperar el usuario actual
  principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  // Recuperar el enlace de la accion
  respuesta = dEnlace.recuperarRegistro(URL_CONCILIACION_VOLUMETRICA_ESTACION_COMPLETA);
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
   parametros.setCampoOrdenamiento(httpRequest.getParameter("campoOrdenamiento"));
  }
  if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
   parametros.setSentidoOrdenamiento(httpRequest.getParameter("sentidoOrdenamiento"));
  }
  
  //PARAMETROS DEL REPORTE   
  
  if (httpRequest.getParameter("formato") != null) {
   formatoReporte=httpRequest.getParameter("formato");
  }   
  if (httpRequest.getParameter("fechaInicial") != null) {
   fechaInicial=httpRequest.getParameter("fechaInicial");
  }   
  if (httpRequest.getParameter("fechaFinal") != null) {
   fechaFinal=httpRequest.getParameter("fechaFinal");
  }   
  if (httpRequest.getParameter("idOperacion") != null) {
   idOperacion=httpRequest.getParameter("idOperacion");
  }
  if (httpRequest.getParameter("txtOperacion") != null) {
	   txtOperacion=httpRequest.getParameter("txtOperacion");
  }
  String where ="";
  ArrayList<String> filtros= new ArrayList<String>();   
  Map<String,Object> parametrosReporte = new HashMap<String,Object>();
  
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  Date fechaIni=sdf.parse(fechaInicial);

  sdf = new SimpleDateFormat("yyyy-MM-dd");
  Date fechaFin=sdf.parse(fechaFinal);

  if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
   filtros.add(" fecha_operativa between '" + fechaInicial +"' AND '" + fechaFinal+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
	   parametrosReporte.put("fechaFinal", fechaFin);
  } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
   filtros.add(" fecha_operativa >= '" + fechaInicial+"'");
	   parametrosReporte.put("fechaInicial", fechaIni);
  }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
   filtros.add(" fecha_operativa <= '" + fechaFinal+"'");
	   parametrosReporte.put("fechaFinal", fechaFin);
  }   
  if (idOperacion.length()>0){
   filtros.add(" id_operacion='"+idOperacion+"'");
	   parametrosReporte.put("id_operacion", idOperacion);
  }
  if (formatoReporte.length()>0){
	   parametrosReporte.put("formato", formatoReporte);
 }
  if (txtOperacion.length()>0){	 
	  byte[] operacion = DatatypeConverter.parseBase64Binary(txtOperacion);
	parametrosReporte.put("operacion",new String(operacion).toUpperCase());
  }
  if (filtros.size()>0){
	    where =" where " +  StringUtils.join(filtros," AND ");
  }   
  
  String sql ="select * from sgo.v_reporte_conciliacion_estacion " + where+" order by fecha_operativa";
  respuesta = dReporte.recuperarRegistrosJasper(sql);

  if (respuesta.estado==false){
	    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
	    throw new Exception(mensajeRespuesta);
  }

  
  String jasperFileName= "";
  String logoFileName= servletContext.getRealPath("/")+"pages"+File.separator+"tema"+File.separator+"app"+File.separator+"imagen"+File.separator+"logo.jpg";
  parametrosReporte.put("rutaImagen", logoFileName);
  parametrosReporte.put("usuario",principal.getIdentidad());
  data=(List<Object>) respuesta.contenido.getCarga(); 


  ByteArrayOutputStream baos = null; 
  Reporteador uReporteador = new Reporteador();	
  parametrosReporte.put("REPORT_LOCALE", new Locale("en", "US"));
  
  if(formatoReporte.equals(Reporteador.FORMATO_PDF)){
	  jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Conciliacion_Volumetrica_Estacion.jasper";
	   baos = uReporteador.generarPDF(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteConciliacionVolumetricaEstacion.pdf\"");
	   response.setDateHeader("Expires", -1);
	   response.setContentType("application/pdf");
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }else if(formatoReporte.equals(Reporteador.FORMATO_EXCEL)){
	  jasperFileName= servletContext.getRealPath("/")+"pages"+File.separator+"otros_reportes"+File.separator+"Reporte_Conciliacion_Volumetrica_Estacion_Excel.jasper";
	  baos = uReporteador.generarEXCEL(parametrosReporte, clase, data, jasperFileName);
	   response.setHeader("Content-Disposition", "inline; filename=\"reporteConciliacionVolumetricaEstacion.xls\"");
	   response.setContentType("application/xls");  
	   response.setContentLength(baos.size());
	   response.getOutputStream().write(baos.toByteArray());
	   response.getOutputStream().flush();
  }

 } catch (Exception ex) {
	  ex.printStackTrace();
 }
}
@RequestMapping(value = URL_VALIDA_PERMISO_REPORTE, method = RequestMethod.GET)
public @ResponseBody
RespuestaCompuesta validaPermisoReporte(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
 RespuestaCompuesta respuesta = null;
 AuthenticatedUserDetails principal = null;
 String mensajeRespuesta = "";
 try {
  // Recuperar el usuario actual
  principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
  // Recuperar el enlace de la accion
  String url_completa="/admin/"+httpRequest.getParameter("url_reporte");
  respuesta = dEnlace.recuperarRegistro(url_completa);
  if (respuesta.estado == false) {
   mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
   respuesta.mensaje=mensajeRespuesta;
   //throw new Exception(mensajeRespuesta);
  }else{
	  Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	  // Verificar si cuenta con el permiso necesario
	  if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	   mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
	   respuesta.estado = false;
	   respuesta.mensaje=mensajeRespuesta;
	  }
  }
 } catch (Exception ex) {
	   ex.printStackTrace();
	   respuesta.estado = false;
	   respuesta.contenido = null;
	   respuesta.mensaje = ex.getMessage();
 }
 return respuesta;
}
 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }

 
}
