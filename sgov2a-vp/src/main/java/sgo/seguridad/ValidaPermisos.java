package sgo.seguridad;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sgo.datos.AforoCisternaDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.DescargaCompartimentoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.TransporteDao;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Utilidades;

@Controller
public class ValidaPermisos {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora; 
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private CisternaDao dCisterna;
 @Autowired
 private TransporteDao dTransporte;
 @Autowired
 private CompartimentoDao dCompartimento;
 @Autowired
 private AforoCisternaDao dAforoCisterna;
 @Autowired
 private DescargaCompartimentoDao dDescargaCompartimento;

 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 /** Nombre de la clase. */
 private static final String sNombreClase = "ValidaPermisos";
 // urls generales
 private static final String VERIFICA_PERMISOS_RELATIVA = "/validaPermisos/validar";
 //PLANIFICACIÓN
 private static final String CREAR_PLANIFICACION = "/admin/planificacion/crear";
 private static final String ACTUALIZAR_PLANIFICACION =  "/admin/planificacion/actualizar";
 private static final String ANULAR_PLANIFICACION = "/admin/planificacion/anular";
 private static final String NOTIFICAR_PLANIFICACION = "/admin/planificacion/notificar";
 private static final String RECUPERAR_PLANIFICACION = "/admin/planificacion/recuperar";
 private static final String URL_RECUPERAR_PLANIFICACION_COMPLETA = "/admin/planificacion/recuperarDetallePlanificacion";
 //PROGRAMACION
 private static final String LEER_PROGRAMACION = "/admin/programacion/listar";
 private static final String REPORTAR_PROGRAMACION = "/admin/programacion/reporte";
 private static final String NOTIFICAR_PROGRAMACION = "/admin/programacion/notificar";
 private static final String CREAR_PROGRAMACION = "/admin/programacion/crear";
 private static final String ACTUALIZAR_PROGRAMACION = "/admin/programacion/actualizar";
 private static final String COMPLETAR_PROGRAMACION = "/admin/programacion/completarProgramacion";
 private static final String RECUPERAR_PROGRAMACION = "/admin/programacion/recuperar";
 private static final String ELIMINAR_PROGRAMACION="/admin/programacion/eliminar";
 //TRANSPORTE
 private static final String LEER_TRANSPORTE = "/admin/transporte/listar";
 private static final String CREAR_TRANSPORTE = "/admin/transporte/crear";
 private static final String ACTUALIZAR_TRANSPORTE = "/admin/transporte/actualizar";
 private static final String IMPORTAR_TRANSPORTE = "/admin/transporte/guardar-sap";
 private static final String RECUPERAR_TRANSPORTE = "/admin/transporte/recuperar";
 private static final String CREAR_EVENTO_TRANSPORTE = "/admin/evento/crear";
 //DESCARGA
 private static final String LEER_DESCARGA = "/admin/descarga/listar-carga";
 private static final String CREAR_DESCARGA = "/admin/descarga/crear";
 private static final String ACTUALIZAR_DESCARGA = "/admin/descarga/actualizar";
 private static final String CREAR_EVENTO_DESCARGA = "/admin/evento/crear";
 private static final String RECUPERAR_DESCARGA = "/admin/descarga/recuperar-carga";
 //DESCONCHE
 private static final String CREAR_DESCONCHE = "/admin/desconche/crear";
 private static final String ACTUALIZAR_DESCONCHE = "/admin/desconche/actualizar";
 private static final String RECUPERAR_DESCONCHE = "/admin/desconche/recuperar";
 //CIERRE
 private static final String LEER_CIERRE = "/admin/cierre/listar";
 private static final String ACTUALIZAR_DIA_OPERATIVO = "/admin/cierre/actualizarEstado";
 //GEC
 private static final String CREAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/crear";
 private static final String ACTUALIZAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/actualizar";
 private static final String RECUPERAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/recuperar";
 private static final String EMITIR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/emitir";
 private static final String APROBAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/aprobar-gec";
 private static final String REPORTAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/reporte-gec";
 private static final String NOTIFICAR_GUIA_COMBUSTIBLE = "/admin/guia-combustible/notificar-gec";
 //DESPACHO
 private static final String LEER_DESPACHO = "/admin/despacho/listar";
 private static final String CREAR_DESPACHO = "/admin/despacho/crear";
 private static final String RECUPERAR_DESPACHO = "/admin/despacho/recuperar";
 private static final String ACTUALIZAR_DESPACHO = "/admin/despacho/actualizar";
 private static final String ACTUALIZAR_ESTADO_DESPACHO = "/admin/despacho/actualizarEstado";
 
 //OTROS MOVIMIENTOS
 private static final String CREAR_OTRO_MOVIMIENTO = "/admin/otroMovimiento/crear";
 private static final String LEER_OTRO_MOVIMIENTO = "/admin/otroMovimiento/listar";
 private static final String ACTUALIZAR_OTRO_MOVIMIENTO = "/admin/otroMovimiento/actualizar";
 private static final String RECUPERAR_OTRO_MOVIMIENTO = "/admin/otroMovimiento/recuperar";
 // DIA OPERATIVO
 private static final String CREAR_JORNADA="/admin/jornada/crear";
 private static final String LEER_JORNADA="/admin/jornada/listar";
 private static final String ACTUALIZAR_JORNADA="/admin/jornada/actualizar";
 private static final String RECUPERAR_JORNADA="/admin/jornada/recuperar";
 private static final String ACTUALIZAR_ESTADO_JORNADA="/admin/jornada/actualizarEstado";
 private static final String URL_RECUPERAR_APERTURA_COMPLETA="/admin/jornada/recuperar-apertura";
 private static final String URL_ELIMINAR_MUESTREO_JORNADA_COMPLETA="/admin/jornada/eliminarMuestreoJornadaa";
 private static final String URL_GUARDAR_CAMBIO_TANQUE_COMPLETA="/admin/jornada/registrarCambioTanque";
 
 //TURNO
 private static final String CREAR_TURNO = "/admin/turno/crear";
 private static final String LEER_TURNO = "/admin/turno/listar";
 private static final String ACTUALIZAR_TURNO = "/admin/turno/actualizar";
 private static final String RECUPERAR_TURNO = "/admin/turno/recuperar";
 
 //LIQUIDACION
 private static final String LEER_LIQUIDACION = "/admin/liquidacion/listar";
 private static final String LIQUIDAR_DIA_OPERATIVO = "/admin/liquidacion/liquidar-jornada";

 //REPORTE GENERAL
 private static final String LEER_REPORTE_GENERAL = "/admin/gestion-reporte/reporte-general";
 
 //OTROS REPORTES
 private static final String LEER_REPORTE_LIQ_CISTERNA = "/admin/reporte/liquidacion-cisterna";
 private static final String LEER_REPORTE_CONC_VOLUMETRICA = "/admin/reporte/conciliacion-volumetrica";
 private static final String LEER_REPORTE_CONC_VOL_ESTACION = "/admin/reporte/conciliacion-volumetrica-estacion";
 
 	
 @RequestMapping(value = VERIFICA_PERMISOS_RELATIVA, method = RequestMethod.GET) 
 public @ResponseBody RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  String descripcionPermiso = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   
   if (httpRequest.getParameter("permiso") != null) {
	   descripcionPermiso = ((httpRequest.getParameter("permiso")));
   } 
   if(descripcionPermiso.length() > 0){
   // Recuperar el enlace de la accion
	   respuesta = dEnlace.recuperarRegistro(this.getPermiso(descripcionPermiso));
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	   }
	   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	   // Verificar si cuenta con el permiso necesario
	   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	   }
   } else {
	   throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   respuesta.estado = true;
   respuesta.mensaje = "Permisos verificados";
  } catch (Exception ex) {
	  Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
	//  ex.printStackTrace();
	  respuesta.estado = false;
	  respuesta.contenido = null;
	  respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
 
 private String getPermiso(String permiso) {
	  String url_completa = "";
	  try {
		  //PLANIFICACION
		  if(permiso.equals("CREAR_PLANIFICACION"))				{ return url_completa = CREAR_PLANIFICACION; }
		  else if(permiso.equals("ACTUALIZAR_PLANIFICACION")) 	{ return url_completa = ACTUALIZAR_PLANIFICACION; }
		  else if(permiso.equals("ANULAR_PLANIFICACION")) 		{ return url_completa = ANULAR_PLANIFICACION; }
		  else if(permiso.equals("RECUPERAR_PLANIFICACION")) 	{ return url_completa = RECUPERAR_PLANIFICACION; }
		  else if(permiso.equals("NOTIFICAR_PLANIFICACION"))	{ return url_completa = NOTIFICAR_PLANIFICACION; }
		  else if(permiso.equals("NOTIFICAR_UNA_PLANIFICACION")){ return url_completa = NOTIFICAR_PLANIFICACION; }
		  else if(permiso.equals("URL_RECUPERAR_PLANIFICACION_COMPLETA"))	{ return url_completa = URL_RECUPERAR_PLANIFICACION_COMPLETA; }
		  //PROGRAMACION
		  else if(permiso.equals("LEER_PROGRAMACION")) 			{ return url_completa = LEER_PROGRAMACION; }
		  else if(permiso.equals("REPORTAR_PROGRAMACION")) 		{ return url_completa = REPORTAR_PROGRAMACION; }
		  else if(permiso.equals("NOTIFICAR_PROGRAMACION")) 	{ return url_completa = NOTIFICAR_PROGRAMACION; }
		  else if(permiso.equals("CREAR_PROGRAMACION")) 		{ return url_completa = CREAR_PROGRAMACION; }
		  else if(permiso.equals("ACTUALIZAR_PROGRAMACION")) 	{ return url_completa = ACTUALIZAR_PROGRAMACION; }
		  else if(permiso.equals("COMPLETAR_PROGRAMACION")) 	{ return url_completa = COMPLETAR_PROGRAMACION; }
		  else if(permiso.equals("RECUPERAR_PROGRAMACION")) 	{ return url_completa = RECUPERAR_PROGRAMACION; }
		  else if(permiso.equals("ELIMINAR_PROGRAMACION")) 		{ return url_completa = ELIMINAR_PROGRAMACION; }
		  else if(permiso.equals("COMENTAR_PROGRAMACION")) 		{ return url_completa = COMPLETAR_PROGRAMACION; } 		  
		  //TRANSPORTE
		  else if(permiso.equals("LEER_TRANSPORTE")) 			{ return url_completa = LEER_TRANSPORTE; }
		  else if(permiso.equals("CREAR_TRANSPORTE")) 			{ return url_completa = CREAR_TRANSPORTE; }
		  else if(permiso.equals("ACTUALIZAR_TRANSPORTE")) 		{ return url_completa = ACTUALIZAR_TRANSPORTE; }
		  else if(permiso.equals("IMPORTAR_TRANSPORTE")) 		{ return url_completa = IMPORTAR_TRANSPORTE; }
		  else if(permiso.equals("RECUPERAR_TRANSPORTE")) 		{ return url_completa = RECUPERAR_TRANSPORTE; }
		  else if(permiso.equals("CREAR_EVENTO_TRANSPORTE")) 	{ return url_completa = CREAR_EVENTO_TRANSPORTE; }
		  else if(permiso.equals("ACTUALIZAR_PESAJE")) 			{ return url_completa = ACTUALIZAR_TRANSPORTE; }
		  //DESCARGA
		  else if(permiso.equals("LEER_DESCARGA")) 				{ return url_completa = LEER_DESCARGA; }
		  else if(permiso.equals("CREAR_CARGA")) 				{ return url_completa = CREAR_DESCARGA; }
		  else if(permiso.equals("ACTUALIZAR_CARGA")) 			{ return url_completa = ACTUALIZAR_DESCARGA; }
		  else if(permiso.equals("CREAR_DESCARGA")) 			{ return url_completa = CREAR_DESCARGA; }
		  else if(permiso.equals("ACTUALIZAR_DESCARGA")) 		{ return url_completa = ACTUALIZAR_DESCARGA; }
		  else if(permiso.equals("CREAR_EVENTO_DESCARGA")) 		{ return url_completa = CREAR_EVENTO_DESCARGA; }
		  else if(permiso.equals("RECUPERAR_DESCARGA")) 		{ return url_completa = RECUPERAR_DESCARGA; }
		  //DESCONCHE
		  else if(permiso.equals("CREAR_DESCONCHE")) 			{ return url_completa = CREAR_DESCONCHE; }
		  else if(permiso.equals("ACTUALIZAR_DESCONCHE")) 		{ return url_completa = ACTUALIZAR_DESCONCHE; }
		  else if(permiso.equals("RECUPERAR_DESCONCHE"))		{ return url_completa = RECUPERAR_DESCONCHE; }
		  //CIERRE
		  else if(permiso.equals("LEER_CIERRE")) 				{ return url_completa = LEER_CIERRE; }
		  else if(permiso.equals("EXPORTAR_PDF")) 				{ return url_completa = LEER_CIERRE; }
		  else if(permiso.equals("EXPORTAR_XLS")) 				{ return url_completa = LEER_CIERRE; }
		  else if(permiso.equals("ACTUALIZAR_DIA_OPERATIVO")) 	{ return url_completa = ACTUALIZAR_DIA_OPERATIVO; }
		  else if(permiso.equals("CERRAR_DIA_OPERATIVO")) 		{ return url_completa = ACTUALIZAR_DIA_OPERATIVO; }
		  
		  //GEC
		  else if(permiso.equals("CREAR_GUIA_COMBUSTIBLE")) 	{ return url_completa = CREAR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("ACTUALIZAR_GUIA_COMBUSTIBLE")) { return url_completa = ACTUALIZAR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("RECUPERAR_GUIA_COMBUSTIBLE")) { return url_completa = RECUPERAR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("EMITIR_GUIA_COMBUSTIBLE")) 	{ return url_completa = EMITIR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("APROBAR_GUIA_COMBUSTIBLE")) 	{ return url_completa = APROBAR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("REPORTAR_GUIA_COMBUSTIBLE")) 	{ return url_completa = REPORTAR_GUIA_COMBUSTIBLE; }
		  else if(permiso.equals("NOTIFICAR_GUIA_COMBUSTIBLE")) { return url_completa = NOTIFICAR_GUIA_COMBUSTIBLE; }
		  
		  //DESPACHO
		  else if(permiso.equals("LEER_DESPACHO")) 				{ return url_completa = LEER_DESPACHO; }
		  else if(permiso.equals("IMPORTAR_DESPACHO")) 			{ return url_completa = CREAR_DESPACHO; }
		  else if(permiso.equals("VER_IMPORTACION")) 			{ return url_completa = RECUPERAR_DESPACHO; }
		  else if(permiso.equals("CREAR_DESPACHO")) 			{ return url_completa = CREAR_DESPACHO; }
		  else if(permiso.equals("RECUPERAR_DESPACHO")) 		{ return url_completa = RECUPERAR_DESPACHO; }
		  else if(permiso.equals("ACTUALIZAR_DESPACHO")) 		{ return url_completa = ACTUALIZAR_DESPACHO; }
		  else if(permiso.equals("ANULAR_DESPACHO")) 			{ return url_completa = ACTUALIZAR_ESTADO_DESPACHO; }
		  
		  //OTROS MOVIMIENTOS
		  else if(permiso.equals("LEER_OTRO_MOVIMIENTO")) 		{ return url_completa = LEER_OTRO_MOVIMIENTO; }
		  else if(permiso.equals("CREAR_OTRO_MOVIMIENTO")) 		{ return url_completa = CREAR_OTRO_MOVIMIENTO; }
		  else if(permiso.equals("ACTUALIZAR_OTRO_MOVIMIENTO")) { return url_completa = ACTUALIZAR_OTRO_MOVIMIENTO; }
		  else if(permiso.equals("RECUPERAR_OTRO_MOVIMIENTO")) 	{ return url_completa = RECUPERAR_OTRO_MOVIMIENTO; }

		  //DIA OPERATIVO
		  else if(permiso.equals("CREAR_JORNADA")) 				{ return url_completa = CREAR_JORNADA; }
		  else if(permiso.equals("LEER_JORNADA")) 				{ return url_completa = LEER_JORNADA; }
		  else if(permiso.equals("ACTUALIZAR_ESTADO_JORNADA")) 	{ return url_completa = ACTUALIZAR_ESTADO_JORNADA; }
		  else if(permiso.equals("ACTUALIZAR_JORNADA")) 		{ return url_completa = ACTUALIZAR_JORNADA; }
		  else if(permiso.equals("RECUPERAR_JORNADA")) 			{ return url_completa = RECUPERAR_JORNADA; }
		  else if(permiso.equals("CAMBIO_TANQUE_JORNADA")) 		{ return url_completa = ACTUALIZAR_JORNADA; }
		  else if(permiso.equals("MUESTREO_JORNADA")) 			{ return url_completa = ACTUALIZAR_JORNADA; }

		  //TURNOS
		  else if(permiso.equals("CREAR_TURNO")) 				{ return url_completa = CREAR_TURNO; }
		  else if(permiso.equals("LEER_TURNO")) 				{ return url_completa = LEER_TURNO; }
		  else if(permiso.equals("ACTUALIZAR_TURNO")) 			{ return url_completa = ACTUALIZAR_TURNO; }
		  else if(permiso.equals("RECUPERAR_TURNO")) 			{ return url_completa = RECUPERAR_TURNO; }
		  
		  //LIQUIDACION
		  else if(permiso.equals("LEER_LIQUIDACION")) 			{ return url_completa = LEER_LIQUIDACION; }
		  else if(permiso.equals("EXPORTAR_PDF")) 				{ return url_completa = LEER_LIQUIDACION; }
		  else if(permiso.equals("DETALLE_POR_ESTACION")) 		{ return url_completa = LEER_LIQUIDACION; }
		  else if(permiso.equals("DETALLE_POR_TANQUE")) 		{ return url_completa = LEER_LIQUIDACION; }
		  else if(permiso.equals("LIQUIDAR_DIA_OPERATIVO")) 	{ return url_completa = LIQUIDAR_DIA_OPERATIVO; }
  
		  //REPORTE GENERAL
		  else if(permiso.equals("LEER_REPORTE_GENERAL")) { return url_completa = LEER_REPORTE_GENERAL; }
		  
		  //OTROS REPORTES
		  else if(permiso.equals("LEER_REPORTE_LIQ_CISTERNA")) 		{ return url_completa = LEER_REPORTE_LIQ_CISTERNA; }
		  else if(permiso.equals("LEER_REPORTE_CONC_VOLUMETRICA")) 	{ return url_completa = LEER_REPORTE_CONC_VOLUMETRICA; }
		  else if(permiso.equals("LEER_REPORTE_CONC_VOL_ESTACION")) { return url_completa = LEER_REPORTE_CONC_VOL_ESTACION; }

		  //MANTENIMIENTOS
		  //AFORO CISTERNA
		  
		  //AFORO TANQUE
		  
		  //.....
		  
		  //SEGURIDAD - BITACORA
		  
		  //SEGURIDAD - PERMISOS
		  
		  //SEGURIDAD - ROLES
		  
		  //SEGURIDAD - USUARIOS
		  
		  //SEGURIDAD - AUTORIZACION
		  
		  
		  
		  
		  
		  
	  } catch (Exception ex) {

	  }
	  return url_completa;
	 }


}