package sgo.servicio;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.GenericoDao;
import sgo.datos.OperacionDao;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.CabeceraReporte;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
import sgo.utilidades.Reporteador;
@Controller
public class ReporteGestorControlador {
 private static final String URL_GESTION_COMPLETA = "/admin/gestion-reporte";
 private static final String URL_GESTION_RELATIVA = "/gestion-reporte";
 
 private static final String URL_REPORTE_GENERAL_COMPLETA = "/admin/gestion-reporte/reporte-general";
 private static final String URL_REPORTE_GENERAL_RELATIVA = "/gestion-reporte/reporte-general";
 @Autowired
 private MenuGestor menu;
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private DiaOperativoDao dDiaOperativo;
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private GenericoDao dGenerico;
 @Autowired
 ServletContext servletContext;
 
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
   
   mapaValores.put("ETIQUETA_BOTON_EXPORTAR", gestorDiccionario.getMessage("sgo.etiquetaBotonExportar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
   mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));
   
   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));
  } catch (Exception ex) {

  }
  return mapaValores;
 }
 
 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  ArrayList<?> listaOperaciones = null;
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  HashMap<String, String> mapaValores = null;
  try {
   principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;

   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   respuesta = dOperacion.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "reporte_general/reporte_general.jsp");
   vista.addObject("vistaJS", "reporte_general/reporte_general.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("operaciones", listaOperaciones);
   vista.addObject("mapaValores", mapaValores);
   vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
  } catch (Exception ex) {

  }
  return vista;
 }
 
 @RequestMapping(value = URL_REPORTE_GENERAL_RELATIVA, method = RequestMethod.GET)
 public void mostrarReporteCierre(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  String tituloReporte ="";
  String formatoReporte="pdf";
  ArrayList<HashMap<?,?>> hmRegistros = null;
  String fechaInicial ="";
  String fechaFinal ="";
  String idOperacion ="";
  try {
   // Recuperar el usuario actual
   principal = (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_REPORTE_GENERAL_COMPLETA);
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
   
   if (httpRequest.getParameter("fechaInicial") != null) {
    fechaInicial=((httpRequest.getParameter("fechaInicial")));
   }
   
   if (httpRequest.getParameter("fechaFinal") != null) {
    fechaFinal=((httpRequest.getParameter("fechaFinal")));
   }
   if (httpRequest.getParameter("idOperacion") != null) {
    idOperacion=((httpRequest.getParameter("idOperacion")));
   }
   
   String where ="";
   ArrayList<String> filtros= new ArrayList<String>();
   if ((fechaInicial.length()==10) && (fechaFinal.length()==10)){
    filtros.add(" fecha_planificacion between '" + fechaInicial +"' AND '" + fechaFinal+"'");
   } else if ((fechaInicial.length()==10) && (fechaFinal.length()!=10)){
    filtros.add(" fecha_planificacion >= '" + fechaInicial+"'");
   }else if ((fechaInicial.length()!=10) && (fechaFinal.length()==10)){
    filtros.add(" fecha_planificacion <= '" + fechaFinal+"'");
   }   
   if (idOperacion.length()>0){
    filtros.add(" id_operacion='"+idOperacion+"'");
   }
   
   if (filtros.size()>0){
    where =" where " +  StringUtils.join(filtros," AND ");
   }   
   
   String sql ="select * from sgo.v_reporte_final_resumen" + where;
   
   respuesta = dGenerico.recuperarRegistros(sql);
   if (respuesta.estado==false){
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   hmRegistros = (ArrayList<HashMap<?, ?>>) respuesta.contenido.getCarga();

   ByteArrayOutputStream baos = null;
   Reporteador uReporteador = new Reporteador();
   uReporteador.setRutaServlet(servletContext.getRealPath("/"));
   ArrayList<Campo> listaCampos = this.generarCampos();
   ArrayList<CabeceraReporte> listaCamposCabecera = this.generarCabecera();
   if (formatoReporte.equals(Reporteador.FORMATO_PDF)){
    baos = uReporteador.generarReporteListado(tituloReporte, "", principal.getIdentidad(),hmRegistros, listaCampos,listaCamposCabecera);

    response.setHeader("Content-Disposition", "inline; charset=UTF-8; filename=\"reporte-cierre.pdf\"");
    response.setDateHeader("Expires", -1);
    response.setContentType("application/pdf");   
    response.setContentLength(baos.size());
    response.getOutputStream().write(baos.toByteArray());
    response.getOutputStream().flush();
   } else if (formatoReporte.equals(Reporteador.FORMATO_CSV)){    
    /*
	   
	   //response.setContentType("text/csv");
	//IA   
//	response.setContentType("text/csv; charset=iso-8859-1");
	response.setContentType("application/vnd.ms-excel");
   // response.setCharacterEncoding("UTF-8");
    //IA

    //response.setHeader("Content-Disposition", "charset=UTF-8; attachment;filename=\"reporte-general.csv\"");
    response.setHeader("Content-Disposition", "attachment; filename=\"reporte-general.csv\"");
    String contenidoCSV="";
    contenidoCSV=uReporteador.generarReporteListadoCSV( hmRegistros, listaCampos, listaCamposCabecera);
    response.getOutputStream().write(contenidoCSV.getBytes());
    response.getOutputStream().flush();
    
    */
	    baos=uReporteador.generarReporteListadoExcel(hmRegistros, listaCampos, listaCamposCabecera,"Reporte General Operaciones");
		try {
			
			byte[] bytes = baos.toByteArray();
			response.setContentType("application/vnd.ms-excel");
			response.addHeader ("Content-Disposition", "attachment; filename=\"reporte-general.xls\"");
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
 
 private ArrayList<Campo> generarCampos() {
  ArrayList<Campo> listaCampos = null;
  try {
   listaCampos = new ArrayList<Campo>();
   Campo eCampo = null;
   //1
   eCampo = new Campo();
   eCampo.setEtiqueta("Fecha Planificacion");
   eCampo.setNombre("fecha_planificacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //2
   eCampo = new Campo();
   eCampo.setEtiqueta("Fecha Asignacion");
   eCampo.setNombre("fecha_asignacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);  
   //3
   eCampo = new Campo();
   eCampo.setEtiqueta("Orden");
   eCampo.setNombre("numero_orden");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //4
   eCampo = new Campo();
   eCampo.setEtiqueta("Guia");
   eCampo.setNombre("guia_remision");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //5
   eCampo = new Campo();
   eCampo.setEtiqueta("factura");
   eCampo.setNombre("numero_factura");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
//6
   eCampo = new Campo();
   eCampo.setEtiqueta("Tracto / Cisterna");
   eCampo.setNombre("tracto_cisterna");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //7
   eCampo = new Campo();
   eCampo.setEtiqueta("Transportista");
   eCampo.setNombre("transportista");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(2);
   listaCampos.add(eCampo);
   //8
   eCampo = new Campo();
   eCampo.setEtiqueta("Conductor");
   eCampo.setNombre("conductor");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //9
   eCampo = new Campo();
   eCampo.setEtiqueta("Compartimento");
   eCampo.setNombre("numero_compartimento");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //10
   eCampo = new Campo();
   eCampo.setEtiqueta("Producto");
   eCampo.setNombre("producto");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //11
   eCampo = new Campo();
   eCampo.setEtiqueta("Vol Obs Despacho");
   eCampo.setNombre("volumen_observado_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //12
   eCampo = new Campo();
   eCampo.setEtiqueta("API Despacho");
   eCampo.setNombre("api_corregido_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //13
   eCampo = new Campo();
   eCampo.setEtiqueta("Temperatura Despacho");
   eCampo.setNombre("temperatura_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //14
   eCampo = new Campo();
   eCampo.setEtiqueta("Factor Despacho");
   eCampo.setNumeroDecimales(6);
   eCampo.setNombre("factor_correccion_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //15
   eCampo = new Campo();
   eCampo.setEtiqueta("Volumen 60F Despacho");
   eCampo.setNombre("volumen_corregido_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //16
   eCampo = new Campo();
   eCampo.setEtiqueta("Peso Salida");
   eCampo.setNombre("peso_despachado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //17
   eCampo = new Campo();
   eCampo.setEtiqueta("Fecha Descarga");
   eCampo.setNombre("fecha_descarga_formateada");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.7f);
   listaCampos.add(eCampo);
   //18
   eCampo = new Campo();
   eCampo.setEtiqueta("Operacion");
   eCampo.setNombre("operacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //19
   eCampo = new Campo();
   eCampo.setEtiqueta("Estacion");
   eCampo.setNombre("estacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //20
   eCampo = new Campo();
   eCampo.setEtiqueta("fecha_arribo");
   eCampo.setNombre("fecha_arribo");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //21
   eCampo = new Campo();
   eCampo.setEtiqueta("fecha_fiscalizacion");
   eCampo.setNombre("fecha_fiscalizacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //22
   eCampo = new Campo();
   eCampo.setEtiqueta("tanque");
   eCampo.setNombre("tanque");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //23
   eCampo = new Campo();
   eCampo.setEtiqueta("nivel");
   eCampo.setNombre("nivel");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //24
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_recibido_observado");
   eCampo.setNombre("volumen_recibido_observado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //25
   eCampo = new Campo();
   eCampo.setEtiqueta("api_descarga");
   eCampo.setNombre("api_descarga");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //26
   eCampo = new Campo();
   eCampo.setEtiqueta("temperatura_centro_cisterna");
   eCampo.setNombre("temperatura_centro_cisterna");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //27
   eCampo = new Campo();
   eCampo.setEtiqueta("temperatura_probeta_descarga");
   eCampo.setNombre("temperatura_probeta_descarga");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //28
   eCampo = new Campo();
   eCampo.setEtiqueta("factor_correccion_descarga");
   eCampo.setNombre("factor_correccion_descarga");
   eCampo.setNumeroDecimales(6);
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //29
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_recibido_corregido");
   eCampo.setNombre("volumen_recibido_corregido");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //30
   eCampo = new Campo();
   eCampo.setEtiqueta("variacion_observada");
   eCampo.setNombre("variacion_observada");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //31
   eCampo = new Campo();
   eCampo.setEtiqueta("variacion_corregida");
   eCampo.setNombre("variacion_corregida");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //32
   eCampo = new Campo();
   eCampo.setEtiqueta("pesaje_inicial_descarga");
   eCampo.setNombre("pesaje_inicial_descarga");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //33
   eCampo = new Campo();
   eCampo.setEtiqueta("pesaje_final_descarga");
   eCampo.setNombre("pesaje_final_descarga");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //34
   eCampo = new Campo();
   eCampo.setEtiqueta("peso_neto_descarga");
   eCampo.setNombre("peso_neto_descarga");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //35
   eCampo = new Campo();
   eCampo.setEtiqueta("factor_masa");
   eCampo.setNombre("factor_masa");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //36
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_peso_observado");
   eCampo.setNombre("volumen_peso_observado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //37
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_peso_corregido");
   eCampo.setNombre("volumen_peso_corregido");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //38
   eCampo = new Campo();
   eCampo.setEtiqueta("contometro_inicial");
   eCampo.setNombre("contometro_inicial");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //39
   eCampo = new Campo();
   eCampo.setEtiqueta("contometro_final");
   eCampo.setNombre("contometro_final");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //40
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_contometro_observado");
   eCampo.setNombre("volumen_contometro_observado");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
   //41
   eCampo = new Campo();
   eCampo.setEtiqueta("volumen_contometro_corregido");
   eCampo.setNombre("volumen_contometro_corregido");
   eCampo.setTipo(Campo.TIPO_DECIMAL);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_DERECHA);
   eCampo.setAncho(1);
   listaCampos.add(eCampo);
 //42
   eCampo = new Campo();
   eCampo.setEtiqueta("Estado");
   eCampo.setNombre("estado_descarga_texto");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.9f);
   listaCampos.add(eCampo); 
   //43
   eCampo = new Campo();
   eCampo.setEtiqueta("observacion_descarga");
   eCampo.setNombre("observacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   eCampo.setAlineacionHorizontal(Campo.ALINEACION_IZQUIERDA);
   eCampo.setAncho(0.9f);
   listaCampos.add(eCampo); 
  } catch (Exception ex) {

  }
  return listaCampos;
 }
 
 
 
 private ArrayList<CabeceraReporte> generarCabecera(){
  ArrayList<CabeceraReporte> listaCamposCabecera =null;
  CabeceraReporte cabeceraReporte = null;
  try {
   listaCamposCabecera = new ArrayList<CabeceraReporte>();
   //1
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Fecha Planificación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //2
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Fecha Asignación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //3
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Orden de entrega");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //4
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Guia");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //5
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Factura");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //6
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Tracto / Cisterna");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //7
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Transportista");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //8
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Conductor");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //9
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Compartimento");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //10
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Producto");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //11
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Obs. Despacho");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //12
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("API Despacho");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //13
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Temperatura Despacho");
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //14
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Factor Despacho");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //15
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen 60F Despacho");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //16
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Peso Salida");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //17
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Fecha Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //18
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Operación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //19
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estación");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //20
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Fecha Arribo");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //21
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Fecha Fiscalización");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //22
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Tanque");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //23
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Nivel");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //24
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Obs Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //25
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("API 60F Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //26
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Temperatura Centro");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //27
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Temperatura Probeta");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //28
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Factor Correccion Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //29
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen 60F Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //30
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Variacion a Observada");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //31
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Variacion a 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //32
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Peso Inicial Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //33
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Peso Final Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //34
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Peso Neto Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //35
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Conversión Masa");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //36
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Peso Obs");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //37
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Peso 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //38
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Contómetro Inicial");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //39
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Contómetro Final");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //40
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Contómetro Obs");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //41
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Volumen Contómetro 60F");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //42
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Estado Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   //43
   cabeceraReporte = new CabeceraReporte();
   cabeceraReporte.setEtiqueta("Observaciones Descarga");
   cabeceraReporte.setColspan(1);
   cabeceraReporte.setRowspan(1);
   listaCamposCabecera.add(cabeceraReporte);
   
  }catch(Exception ex){
   
  }
  return listaCamposCabecera;
 }
 
}
