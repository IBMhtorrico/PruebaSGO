package sgo.servicio;

import java.util.HashMap;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RecursoControlador {
 @Autowired 
 private MessageSource gestorDiccionario;
 
 private static final String URL_GESTION_COMPLETA="/admin/recursos.js";
 private static final String URL_GESTION_RELATIVA="/recursos.js";
 
 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario( Locale locale){
  ModelAndView vista =null;
  HashMap<String, String> hmRecursos = new HashMap<String,String>();
  try {
   //Recursos
   hmRecursos.put("CERRAR_VISTA", gestorDiccionario.getMessage("sgo.cerrarVista",null,locale));
   hmRecursos.put("INICIAR_OPERACION", gestorDiccionario.getMessage("sgo.iniciarOperacion",null,locale));
   hmRecursos.put("CANCELAR_OPERACION", gestorDiccionario.getMessage("sgo.cancelarOperacion",null,locale));
   hmRecursos.put("PROCESANDO_PETICION", gestorDiccionario.getMessage("sgo.procesandoPeticion",null,locale));
   hmRecursos.put("VERIFICANDO_PERMISOS", gestorDiccionario.getMessage("sgo.verificandoPermisos",null,locale));
   hmRecursos.put("ERROR_NO_CONECTADO", gestorDiccionario.getMessage("sgo.errorNoConectado",null,locale));
   hmRecursos.put("ERROR_RECURSO_NO_DISPONIBLE", gestorDiccionario.getMessage("sgo.errorRecursoNoDisponible",null,locale));
   hmRecursos.put("ERROR_TIEMPO_AGOTADO", gestorDiccionario.getMessage("sgo.errorTiempoAgotado",null,locale));
   hmRecursos.put("ERROR_GENERICO_SERVIDOR", gestorDiccionario.getMessage("sgo.errorGenericoServidor",null,locale));
   hmRecursos.put("ERROR_INTERNO_SERVIDOR", gestorDiccionario.getMessage("sgo.errorInternoServidor",null,locale));
   hmRecursos.put("LISTADO_OPERACIONES_ERRADO", gestorDiccionario.getMessage("sgo.noListadoOperaciones",null,locale));
   hmRecursos.put("LISTADO_ESTACIONES_ERRADO", gestorDiccionario.getMessage("sgo.noListadoEstaciones",null,locale));
   hmRecursos.put("LISTADO_ESTACIONES_EXITOSO", gestorDiccionario.getMessage("sgo.listadoEstacionesExitoso",null,locale));
   hmRecursos.put("LISTADO_TANQUE_EXITOSO", gestorDiccionario.getMessage("sgo.listadoTanquesExitoso",null,locale));
   hmRecursos.put("SELECCIONAR_ELEMENTO", gestorDiccionario.getMessage("sgo.seleccionarElemento",null,locale));
   hmRecursos.put("ERROR_DATOS_OPERACION", gestorDiccionario.getMessage("sgo.errorDatosOperacion",null,locale));
   hmRecursos.put("ERROR_VALORES_FORMULARIO", gestorDiccionario.getMessage("sgo.erroValoresFormulario",null,locale));
   hmRecursos.put("LISTADO_RECUPERA_EXITOSO", gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale));

   
   //
   hmRecursos.put("TITULO_AGREGAR_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioAgregar",null,locale));
   hmRecursos.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar",null,locale));
   hmRecursos.put("TITULO_DETALLE_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioVer",null,locale));
   hmRecursos.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado",null,locale));
   
   
   hmRecursos.put("MENSAJE_CAMBIAR_REGISTRO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado",null,locale));
   hmRecursos.put("MENSAJE_ERROR_POR_LONGITUD", gestorDiccionario.getMessage("sgo.errorLongitudJustificacion",null,locale));
   hmRecursos.put("MENSAJE_ERROR_CODIGO_AUTORIZACION", gestorDiccionario.getMessage("sgo.errorCodigoAutorizacion",null,locale));
   
   hmRecursos.put("TITULO_AGREGAR_PLANIFICACION", gestorDiccionario.getMessage("sgo.tituloAgregarPlanificacion",null,locale));
   hmRecursos.put("TITULO_MODIFICA_PLANIFICACION", gestorDiccionario.getMessage("sgo.tituloModificarPlanificacion",null,locale));
   hmRecursos.put("TITULO_VER_PLANIFICACION", gestorDiccionario.getMessage("sgo.tituloVerPlanificacion",null,locale));
   hmRecursos.put("TITULO_DETALLE_PLANIFICACION", gestorDiccionario.getMessage("sgo.tituloDetallePlanificacion",null,locale));
   
   hmRecursos.put("TITULO_DETALLE_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloDetalleTransporte",null,locale));
   hmRecursos.put("TITULO_AGREGAR_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloAgregarTransporte",null,locale));
   hmRecursos.put("TITULO_MODIFICA_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloModificarTransporte",null,locale));
   hmRecursos.put("TITULO_IMPORTA_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloImportarTransporte",null,locale));
   hmRecursos.put("TITULO_VER_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloVerTransporte",null,locale));
   hmRecursos.put("TITULO_EVENTO_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloEventoTransporte",null,locale));
   hmRecursos.put("TITULO_PESAJE_TRANSPORTE", gestorDiccionario.getMessage("sgo.tituloPesajeTransporte",null,locale));
   hmRecursos.put("CERRAR_DETALLE_TRANSPORTE", gestorDiccionario.getMessage("sgo.cerrarDetalleTransporte",null,locale));
   hmRecursos.put("INICIAR_DETALLE_TRANSPORTE", gestorDiccionario.getMessage("sgo.iniciarDetalleTransporte",null,locale));
   
   hmRecursos.put("TITULO_MODIFICA_CIERRE", gestorDiccionario.getMessage("sgo.tituloModificarCIerre",null,locale));
   
   hmRecursos.put("TEXTO_METODO_WINCHA", gestorDiccionario.getMessage("sgo.metodoWincha",null,locale));
   hmRecursos.put("TEXTO_METODO_BALANZA", gestorDiccionario.getMessage("sgo.metodoBalanza",null,locale));
   hmRecursos.put("TEXTO_METODO_CONTOMETRO", gestorDiccionario.getMessage("sgo.metodoContometro",null,locale));
   hmRecursos.put("TITULO_INICIAL_DESCARGA", gestorDiccionario.getMessage("sgo.tituloInicialDescarga", null, locale));
   hmRecursos.put("TITULO_DETALLE_DESCARGA", gestorDiccionario.getMessage("sgo.tituloDetalleDescarga", null, locale));
   hmRecursos.put("TITULO_AGREGAR_DESCARGA_TANQUE", gestorDiccionario.getMessage("sgo.tituloAgregarDescargaTanque", null, locale));
   hmRecursos.put("TITULO_MODIFICAR_DESCARGA_TANQUE", gestorDiccionario.getMessage("sgo.tituloModificarDescargaTanque", null, locale));
   hmRecursos.put("TITULO_AGREGAR_DESCARGA", gestorDiccionario.getMessage("sgo.tituloAgregarDescargaCisterna", null, locale));
   hmRecursos.put("TITULO_MODIFICAR_DESCARGA", gestorDiccionario.getMessage("sgo.tituloModificarDescargaCisterna", null, locale));
   hmRecursos.put("MENSAJE_ALTURA_NO_AFORADA", gestorDiccionario.getMessage("sgo.alturaNoAforada", null, locale));
   hmRecursos.put("TEXTO_NO_ESTACIONES_OPERACION", gestorDiccionario.getMessage("sgo.operacionNoEstaciones", null, locale));
   hmRecursos.put("TEXTO_NO_TANQUES_ESTACION", gestorDiccionario.getMessage("sgo.estacionNoTanques", null, locale));
   hmRecursos.put("GRILLA_PROCESANDO", gestorDiccionario.getMessage("sgo.grilla.Processing", null, locale));   
   hmRecursos.put("GRILLA_LONGITUD_MENU", gestorDiccionario.getMessage("sgo.grilla.LengthMenu", null, locale)); 
   hmRecursos.put("GRILLA_SIN_REGISTROS", gestorDiccionario.getMessage("sgo.grilla.ZeroRecords", null, locale));   
   hmRecursos.put("GRILLA_TABLA_VACIA", gestorDiccionario.getMessage("sgo.grilla.EmptyTable", null, locale));   
   hmRecursos.put("GRILLA_INFO", gestorDiccionario.getMessage("sgo.grilla.Info", null, locale)); 
   hmRecursos.put("GRILLA_INFO_VACIA", gestorDiccionario.getMessage("sgo.grilla.InfoEmpty", null, locale));
   hmRecursos.put("GRILLA_INFO_FILTRADA", gestorDiccionario.getMessage("sgo.grilla.InfoFiltered", null, locale));   
   hmRecursos.put("GRILLA_INFO_POST_FIX", gestorDiccionario.getMessage("sgo.grilla.InfoPostFix", null, locale)); 
   hmRecursos.put("GRILLA_BUSCAR", gestorDiccionario.getMessage("sgo.grilla.Search", null, locale));
   hmRecursos.put("GRILLA_URL", gestorDiccionario.getMessage("sgo.grilla.Url", null, locale));   
   hmRecursos.put("GRILLA_MILES", gestorDiccionario.getMessage("sgo.grilla.InfoThousands", null, locale));   
   hmRecursos.put("GRILLA_CARGANDO_REGISTROS", gestorDiccionario.getMessage("sgo.grilla.LoadingRecords", null, locale));
   hmRecursos.put("GRILLA_PAGINACION_PRIMERO", gestorDiccionario.getMessage("sgo.grilla.Paginacion.First", null, locale)); 
   hmRecursos.put("GRILLA_PAGINACION_ULTIMO", gestorDiccionario.getMessage("sgo.grilla.Paginacion.Last", null, locale));
   hmRecursos.put("GRILLA_PAGINACION_SIGUIENTE", gestorDiccionario.getMessage("sgo.grilla.Paginacion.Next", null, locale));
   hmRecursos.put("GRILLA_PAGINACION_ANTERIOR", gestorDiccionario.getMessage("sgo.grilla.Paginacion.Previous", null, locale));  
   hmRecursos.put("GRILLA_ORDEN_ASCENDENTE", gestorDiccionario.getMessage("sgo.grilla.SortAscending", null, locale));
   hmRecursos.put("GRILLA_ORDEN_DESCENDENTE", gestorDiccionario.getMessage("sgo.grilla.SortDescending", null, locale));
   hmRecursos.put("TEXTO_NO_CLIENTES", gestorDiccionario.getMessage("sgo.noClientes", null, locale));
   
   hmRecursos.put("TITULO_VER_DETALLE_PROGRAMACION", gestorDiccionario.getMessage("sgo.tituloVerDetProgramacion",null,locale));
   hmRecursos.put("TITULO_VER_PROGRAMACION", gestorDiccionario.getMessage("sgo.tituloVerProgramacion",null,locale));
   
   hmRecursos.put("TITULO_DETALLE_DESPACHO", gestorDiccionario.getMessage("sgo.tituloDetalleDespacho",null,locale));
   hmRecursos.put("TITULO_AGREGAR_DESPACHO", gestorDiccionario.getMessage("sgo.tituloAgregarDespacho",null,locale));
   hmRecursos.put("TITULO_MODIFICA_DESPACHO", gestorDiccionario.getMessage("sgo.tituloModificarDespacho",null,locale));
   hmRecursos.put("TITULO_IMPORTA_DESPACHO", gestorDiccionario.getMessage("sgo.tituloImportarDespacho",null,locale));
   hmRecursos.put("TITULO_VER_IMPORTACION_DESPACHO", gestorDiccionario.getMessage("sgo.tituloVerImportacionDespacho",null,locale));
   hmRecursos.put("TITULO_VER_DESPACHO", gestorDiccionario.getMessage("sgo.tituloVerDespacho",null,locale));
   hmRecursos.put("INICIAR_DETALLE_DESPACHO", gestorDiccionario.getMessage("sgo.iniciarDetalleDespacho",null,locale));
   
   hmRecursos.put("TITULO_APERTURA_JORNADA", gestorDiccionario.getMessage("sgo.tituloAperturaJornada",null,locale));
   hmRecursos.put("TITULO_CIERRE_JORNADA", gestorDiccionario.getMessage("sgo.tituloCierreJornada",null,locale));
   hmRecursos.put("TITULO_REAPERTURA_JORNADA", gestorDiccionario.getMessage("sgo.tituloReaperturaJornada",null,locale));
   hmRecursos.put("TITULO_DETALLE_JORNADA", gestorDiccionario.getMessage("sgo.tituloDetalleJornada",null,locale));
   hmRecursos.put("TITULO_CAMBIO_TANQUE_JORNADA", gestorDiccionario.getMessage("sgo.tituloCambioTanqueJornada",null,locale));
   hmRecursos.put("TITULO_MUESTREO_JORNADA", gestorDiccionario.getMessage("sgo.tituloMuestreoJornada",null,locale));
   hmRecursos.put("TITULO_DETALLE_JORNADA", gestorDiccionario.getMessage("sgo.iniciarDetalleJornada",null,locale));

   hmRecursos.put("INICIAR_DETALLE_PROGRAMACION", gestorDiccionario.getMessage("sgo.iniciarDetalleProgramacion",null,locale));
   hmRecursos.put("TITULO_AGREGAR_PROGRAMACION", gestorDiccionario.getMessage("sgo.tituloAgregarProgramacion",null,locale));
   hmRecursos.put("CERRAR_DETALLE_PROGRAMACION", gestorDiccionario.getMessage("sgo.cerrarDetalleProgramacion",null,locale));
   hmRecursos.put("TITULO_LISTADO_DIA_PLANIFICADO", gestorDiccionario.getMessage("sgo.tituloFormularioListadoDiaPlanificado",null,locale));
   
   hmRecursos.put("TITULO_TURNO_APERTURA", gestorDiccionario.getMessage("sgo.tituloTurnoApertura",null,locale));
   hmRecursos.put("TITULO_TURNO_CERRAR", gestorDiccionario.getMessage("sgo.tituloTurnoCierre",null,locale));
   hmRecursos.put("INICIAR_LISTADO_TURNO", gestorDiccionario.getMessage("sgo.iniciarListadoTurno",null,locale));
   
   hmRecursos.put("TITULO_GEC_APROBACION", gestorDiccionario.getMessage("sgo.tituloGecAprobacion",null,locale));
   
   //Agregado por req 9000002570==============================================================
   hmRecursos.put("TITULO_ETAPAS_REGISTRO", gestorDiccionario.getMessage("sgo.tituloEtapasRegistro", null, locale));
   hmRecursos.put("TITULO_TIEMPOS_ETAPAS_REGISTRO", gestorDiccionario.getMessage("sgo.tituloTiemposEtapasRegistro", null, locale));
   //=========================================================================================
   
   vista = new ModelAndView("recursos");
   vista.addObject("recursos",hmRecursos);
 } catch(Exception ex){
   ex.printStackTrace();
 }
 return vista;
 }
 
}
