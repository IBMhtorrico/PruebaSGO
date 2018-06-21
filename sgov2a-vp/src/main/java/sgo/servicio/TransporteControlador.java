package sgo.servicio;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.LogFactory;
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

import sgo.datos.AsignacionDao;
import sgo.datos.AsignacionTransporteDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.ClienteDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.ConductorDao;
import sgo.datos.DetalleProgramacionDao;
import sgo.datos.DetalleTransporteDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EventoDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.ProgramacionDao;
import sgo.datos.TransporteDao;
import sgo.datos.TransportistaDao;
import sgo.entidad.Asignacion;
import sgo.entidad.Bitacora;
import sgo.entidad.BusquedaDetalleProgramado;
import sgo.entidad.BusquedaProgramado;
import sgo.entidad.Cisterna;
import sgo.entidad.ClonacionProgTrans;
import sgo.entidad.Compartimento;
import sgo.entidad.Conductor;
import sgo.entidad.Contenido;
import sgo.entidad.DetalleImportacion;
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DetalleTransporte;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.EtapaTransporte;
import sgo.entidad.Evento;
import sgo.entidad.ListaMaestroImportacion;
import sgo.entidad.MaestroImportacion;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Producto;
import sgo.entidad.Programacion;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Transporte;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Formula;
import sgo.utilidades.Utilidades;
import sgo.ws.sap.comun.ZPI_Fault;
import sgo.ws.sap.parametros.ConInfEnt;
import sgo.ws.sap.parametros.Rpta_ConInfEntCabecera;
import sgo.ws.sap.parametros.Rpta_ConInfEntCabeceraDetalle;
import sgo.ws.sap.servicio.ConsInformacionEntregas_Out;
import sgo.ws.sap.servicio.Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TransporteControlador {
	@Autowired
	private MessageSource gestorDiccionario;// Gestor del diccionario de
											// mensajes
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
	private AsignacionDao dAsignacion;
	@Autowired
	private PlanificacionDao dPlanificacion;
	@Autowired
	private ProgramacionDao dProgramacion;
	@Autowired
	private TransportistaDao dTransportista;
	@Autowired
	private ClienteDao dCliente;
	@Autowired
	private CisternaDao dCisterna;
	@Autowired
	private ConductorDao dConductor;
	@Autowired
	private CompartimentoDao dCompartimento;
	@Autowired
	private OperacionDao dOperacion;
	@Autowired
	private ProductoDao dProducto;
	@Autowired
	private DetalleTransporteDao dDetalleTransporte;
	@Autowired
	private TransporteDao dTransporte;
	@Autowired
	private EventoDao dEvento;
	@Autowired
	private AsignacionTransporteDao dAsignacionTransporteDao;
	@Autowired
	private DiaOperativoControlador DiaOperativoControlador;
	@Autowired
	private DetalleProgramacionDao dDetalleProgramacion;

	//
	private DataSourceTransactionManager transaccion;// Gestor de la transaccion
	// urls generales
	private static final String URL_GESTION_COMPLETA = "/admin/transporte";
	private static final String URL_GESTION_RELATIVA = "/transporte";
	private static final String URL_GUARDAR_COMPLETA = "/admin/transporte/crear";
	private static final String URL_GUARDAR_RELATIVA = "/transporte/crear";
	private static final String URL_GUARDAR_PROGRAMACION_COMPLETA = "/admin/transporte/crearPorProgramacion";
	private static final String URL_GUARDAR_PROGRAMACION_RELATIVA = "/transporte/crearPorProgramacion";
	private static final String URL_LISTAR_COMPLETA = "/admin/transporte/listar";
	private static final String URL_LISTAR_RELATIVA = "/transporte/listar";
	
	//Agregado por req 9000002570==================== 
	private static final String URL_RECUPERAR_TIEMPO_ETAPAS_COMPLETA = "/admin/transporte/recuperarTiemposEtapa";
	private static final String URL_RECUPERAR_TIEMPO_ETAPAS_RELATIVA = "/transporte/recuperarTiemposEtapa";
	
	private static final String URL_GUARDAR_TIEMPO_ETAPAS_COMPLETA = "/admin/transporte/crearTiempos";
	private static final String URL_GUARDAR_TIEMPO_ETAPAS_RELATIVA = "/transporte/crearTiempos";
	//===============================================

	private static final String URL_LISTAR_FILTRAR_TRANSPORTE_COMPLETA = "/admin/transporte/listar-asignados";
	private static final String URL_LISTAR_FILTRAR_TRANSPORTE_RELATIVA = "/transporte/listar-asignados";

	private static final String URL_ACTUALIZAR_COMPLETA = "/admin/transporte/actualizar";
	private static final String URL_ACTUALIZAR_RELATIVA = "/transporte/actualizar";
	private static final String URL_RECUPERAR_COMPLETA = "/admin/transporte/recuperar";
	private static final String URL_RECUPERAR_RELATIVA = "/transporte/recuperar";
	private static final String URL_LISTAR_TRANSPORTES_COMPLETA = "/admin/transporte/listarTransportes";
	private static final String URL_LISTAR_TRANSPORTES_RELATIVA = "/transporte/listarTransportes";

	private static final String URL_RECUPERAR_TRANSPORTE_COMPLETA = "/admin/transporte/recuperar-transporte";
	private static final String URL_RECUPERAR_TRANSPORTE_RELATIVA = "/transporte/recuperar-transporte";

	private static final String URL_LISTAR_ASIGNACION_TRANSPORTES_COMPLETA = "/admin/transporte/listarAsignacionTransportes";
	private static final String URL_LISTAR_ASIGNACION_TRANSPORTES_RELATIVA = "/transporte/listarAsignacionTransportes";
	private static final String URL_ACTUALIZAR_PESAJE_COMPLETA = "/admin/transporte/actualizarPesaje";
	private static final String URL_ACTUALIZAR_PESAJE_RELATIVA = "/transporte/actualizarPesaje";
	private static final String URL_GUARDAR_EVENTO_COMPLETA = "/admin/transporte/guardarEventoTransporte";
	
	 private static final String URL_CONSULTAR_SAP_COMPLETA = "/admin/transporte/consultar-sap";
   private static final String URL_CONSULTAR_SAP_RELATIVA = "/transporte/consultar-sap";
   
   private static final String URL_GUARDAR_SAP_COMPLETA = "/admin/transporte/guardar-sap";
   private static final String URL_GUARDAR_SAP_RELATIVA = "/transporte/guardar-sap";

	private HashMap<String, String> recuperarMapaValores(Locale locale) {
		HashMap<String, String> mapaValores = new HashMap<String, String>();
		try {
			mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
			mapaValores.put("ESTADO_INACTIVO",String.valueOf(Constante.ESTADO_INACTIVO));
			mapaValores.put("FILTRO_TODOS",String.valueOf(Constante.FILTRO_TODOS));
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

			mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
			mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
			mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));

			mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
			mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));
		} catch (Exception ex) {

		}
		return mapaValores;
	}
	
	//Agregado por req 9000002570======================================================
	
	@RequestMapping(value = URL_GUARDAR_TIEMPO_ETAPAS_RELATIVA, method = RequestMethod.POST)
	 public @ResponseBody
	 RespuestaCompuesta guardarRegistrosTiempos(@RequestBody Transporte eTransporte, HttpServletRequest peticionHttp, Locale locale) {
		 RespuestaCompuesta respuesta = null;
		 AuthenticatedUserDetails principal = null;
		 Bitacora eBitacora = null;
		 TransactionDefinition definicionTransaccion = null;
		 TransactionStatus estadoTransaccion = null;
		 String direccionIp = "";
		 
		 try {
			 
			// Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dTransporte.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
			// Recuperar el usuario actual
			principal = this.getCurrentUser();
			// Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_TIEMPO_ETAPAS_COMPLETA);
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
			
			ArrayList<EtapaTransporte> etapas = eTransporte.getEtapasTransporte();
			 for(EtapaTransporte etapa : etapas){
				 
				 etapa.setCreadoEl(Calendar.getInstance().getTime().getTime());
				 etapa.setCreadoPor(principal.getID());
				 etapa.setIpCreacion(direccionIp);
				 etapa.setActualizadoEl(Calendar.getInstance().getTime().getTime());
				 etapa.setActualizadoPor(principal.getID());
				 etapa.setIpActualizacion(direccionIp);

				if(etapa.getId() == null || etapa.getId() == 0){
					respuesta = dTransporte.guardarRegistroTiempo(etapa);
				}else{
					respuesta = dTransporte.actualizarRegistroTiempo(etapa);
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
			   ex.printStackTrace();
			   respuesta.estado = false;
			   respuesta.contenido = null;
			   respuesta.mensaje = ex.getMessage();
			   this.transaccion.rollback(estadoTransaccion);
			 }
			 return respuesta;
	}
	
	 @RequestMapping(value = URL_RECUPERAR_TIEMPO_ETAPAS_RELATIVA, method = RequestMethod.GET)
	 public @ResponseBody
	 RespuestaCompuesta recuperaTiemposEtapa(int idTransporte, int idOperacion, Locale locale) {
	  RespuestaCompuesta respuesta = null;
	  AuthenticatedUserDetails principal = null;
	  try {
	   // Recupera el usuario actual
	   principal = this.getCurrentUser();
	   // Recuperar el enlace de la accion
	   //TODO cambiar URL_RECUPERAR_COMPLETA
	   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_TIEMPO_ETAPAS_COMPLETA);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
	   }
	   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	   // Verificar si cuenta con el permiso necesario
	   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	   }
	   // Recuperar el registro
	   respuesta = dTransporte.recuperarTiempoEtapas(idTransporte, idOperacion);
	   // Verifica el resultado de la accion
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   
	   if(respuesta.mensaje.equals("OK")){
		   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
	   }
	   
	  } catch (Exception ex) {
	   ex.printStackTrace();
	   respuesta.estado = false;
	   respuesta.contenido = null;
	   respuesta.mensaje = ex.getMessage();
	  }
	  return respuesta;
	 }

	// @SuppressWarnings("unchecked")
	@RequestMapping(URL_GESTION_RELATIVA)
	public ModelAndView mostrarFormulario(Locale locale) {
		ModelAndView vista = null;
		AuthenticatedUserDetails principal = null;
		ArrayList<?> listaEnlaces = null;
		ArrayList<?> listaClientes = null;
		ArrayList<?> listaOperaciones = null;
		ArrayList<?> listaProductos = null;
		RespuestaCompuesta respuesta = null;
		ParametrosListar parametros = null;
		HashMap<String, String> mapaValores = null;
		try {
			principal = this.getCurrentUser();
			respuesta = menu.Generar(principal.getRol().getId(),URL_GESTION_COMPLETA);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
			}
			listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;

			parametros = new ParametrosListar();
			parametros.setPaginacion(Constante.SIN_PAGINACION);
			parametros.setFiltroEstado(Constante.FILTRO_TODOS);
			respuesta = dCliente.recuperarRegistros(parametros);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
			}

			parametros = new ParametrosListar();
			parametros.setPaginacion(Constante.SIN_PAGINACION);
			parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
			parametros.setIdCliente(principal.getCliente().getId());
			parametros.setIdOperacion(principal.getOperacion().getId());
			respuesta = dOperacion.recuperarRegistros(parametros);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.noPermisosDisponibles", null, locale));
			}
			listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;

			parametros = new ParametrosListar();
			parametros.setPaginacion(Constante.SIN_PAGINACION);
			// Para que retorne sólo los productos que se encuentren activos
			parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
			respuesta = dProducto.recuperarRegistros(parametros);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
			}
			listaProductos = (ArrayList<?>) respuesta.contenido.carga;
			mapaValores = recuperarMapaValores(locale);
			vista = new ModelAndView("plantilla");
			vista.addObject("vistaJSP", "transporte/transporte.jsp");
			vista.addObject("vistaJS", "transporte/transporte.js");
			vista.addObject("identidadUsuario", principal.getIdentidad());
			vista.addObject("menu", listaEnlaces);
			vista.addObject("clientes", listaClientes);
			vista.addObject("operaciones", listaOperaciones);
			vista.addObject("productos", listaProductos);
			vista.addObject("mapaValores", mapaValores);
			vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
		} catch (Exception ex) {

		}
		return vista;
	}
	
	

	/*
	 * @RequestMapping(value = URL_RECUPERAR_RELATIVA ,method =
	 * RequestMethod.GET) public @ResponseBody RespuestaCompuesta
	 * recuperaRegistro(int ID, int IDTransporte, Locale locale){
	 * RespuestaCompuesta respuesta = null; AuthenticatedUserDetails principal =
	 * null; try { //Recupera el usuario actual principal =
	 * this.getCurrentUser(); //Recuperar el enlace de la accion respuesta =
	 * dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA); if
	 * (respuesta.estado==false){ throw new
	 * Exception(gestorDiccionario.getMessage
	 * ("sgo.accionNoHabilitada",null,locale)); } Enlace eEnlace = (Enlace)
	 * respuesta.getContenido().getCarga().get(0); //Verificar si cuenta con el
	 * permiso necesario if
	 * (!principal.getRol().searchPermiso(eEnlace.getPermiso())){ throw new
	 * Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
	 * }
	 * 
	 * if(!Utilidades.esValido(ID)){ throw new
	 * Exception(gestorDiccionario.getMessage
	 * ("sgo.accionNoHabilitada",null,locale)); }
	 * 
	 * respuesta = dAsignacion.recuperarRegistro(ID, IDTransporte);
	 * 
	 * if(!respuesta.estado){ throw new
	 * Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada"
	 * ,null,locale)); }
	 * 
	 * //Si respuesta tiene registros, recuperamos los transportes.
	 * if(!respuesta.contenido.carga.isEmpty()){ List<Asignacion>
	 * listaAsignaciones = new ArrayList<Asignacion>(); List<Transporte>
	 * listaTransportes = new ArrayList<Transporte>(); Iterator iteraAsignacion
	 * = respuesta.contenido.carga.iterator(); while (iteraAsignacion.hasNext())
	 * { Asignacion eAsignacion = (Asignacion)iteraAsignacion.next();
	 * 
	 * //buscamos los transportes del día operativo RespuestaCompuesta
	 * respuestaTransportes =
	 * dTransporte.recuperarRegistro(eAsignacion.getIdTransporte());
	 * if(!respuestaTransportes.estado){ throw new
	 * Exception(gestorDiccionario.getMessage
	 * ("sgo.accionNoHabilitada",null,locale)); }
	 * 
	 * if(!respuestaTransportes.contenido.carga.isEmpty()){
	 * List<DetalleTransporte> listaDetalleTransporte = new
	 * ArrayList<DetalleTransporte>(); List<Evento> listaEventos = new
	 * ArrayList<Evento>(); Iterator iteraTransportes =
	 * respuestaTransportes.contenido.carga.iterator(); while
	 * (iteraTransportes.hasNext()) { Transporte eTransporte =
	 * (Transporte)iteraTransportes.next();
	 * 
	 * //buscamos los detalles de los transportes de día operativo
	 * RespuestaCompuesta respuestaDetalleTransporte =
	 * dDetalleTransporte.recuperarRegistrosPorIdTransporte
	 * (eTransporte.getId()); if(!respuestaDetalleTransporte.estado){ throw new
	 * Exception
	 * (gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale)); }
	 * 
	 * if(!respuestaDetalleTransporte.contenido.carga.isEmpty()){ Iterator
	 * iteraDetalleTransportes =
	 * respuestaDetalleTransporte.contenido.carga.iterator(); while
	 * (iteraDetalleTransportes.hasNext()) { DetalleTransporte
	 * eDetalleTransporte = (DetalleTransporte)iteraDetalleTransportes.next();
	 * listaDetalleTransporte.add(eDetalleTransporte); } }
	 * 
	 * //buscamos los eventos del transporte RespuestaCompuesta respuestaEvento
	 * =
	 * dEvento.recuperarRegistroPorIdRegistroYTipoRegistro(eTransporte.getId(),
	 * 1); //se envía 1 porque el tipo de registro es transporte
	 * if(!respuestaEvento.estado){ throw new
	 * Exception(gestorDiccionario.getMessage
	 * ("sgo.accionNoHabilitada",null,locale)); }
	 * 
	 * if(!respuestaEvento.contenido.carga.isEmpty()){ Iterator iteraEvento =
	 * respuestaEvento.contenido.carga.iterator(); while (iteraEvento.hasNext())
	 * { Evento eEvento = (Evento)iteraEvento.next(); listaEventos.add(eEvento);
	 * } }
	 * 
	 * eTransporte.setEventos(listaEventos);
	 * eTransporte.setDetalles(listaDetalleTransporte);
	 * listaTransportes.add(eTransporte); } }
	 * eAsignacion.setTransportes(listaTransportes);
	 * listaAsignaciones.add(eAsignacion); } respuesta.contenido.carga = (List)
	 * listaAsignaciones; } respuesta.mensaje =
	 * gestorDiccionario.getMessage("sgo.listarExitoso",null,locale); } catch
	 * (Exception ex){ ex.printStackTrace(); respuesta.estado=false;
	 * respuesta.contenido = null; respuesta.mensaje=ex.getMessage(); } return
	 * respuesta; }
	 */

	 @RequestMapping(value = URL_CONSULTAR_SAP_RELATIVA, method = RequestMethod.GET)
	  public @ResponseBody RespuestaCompuesta consultarSAP(HttpServletRequest httpRequest, Locale locale) {
	    RespuestaCompuesta respuesta = null;
	    AuthenticatedUserDetails principal = null;
      ConsInformacionEntregas_Out puerto;
      Rpta_ConInfEntCabecera[] resultado= null;
      Rpta_ConInfEntCabecera elemento=null;
      MaestroImportacion maestroImportacion = null;
      DetalleImportacion detalleImportacion= null;
      Contenido<MaestroImportacion> contenido = null;
      ArrayList<MaestroImportacion> listaImportacion = null;
      Rpta_ConInfEntCabeceraDetalle[] compartimentos=null;
      String destinatario="";
      int esCentro=0;
      ConInfEnt parametrosSAP=null;
	    try {
	     System.setProperty(LogFactory.FACTORY_PROPERTY, LogFactory.FACTORY_DEFAULT);
	      // Recupera el usuario actual
	      principal = this.getCurrentUser();
	      // Recuperar el enlace de la accion
	      respuesta = dEnlace.recuperarRegistro(URL_LISTAR_TRANSPORTES_COMPLETA);
	      if (respuesta.estado == false) {
	        throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
	      }
	      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	      // Verificar si cuenta con el permiso necesario
	      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	        throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	      }
	      // Recuperar el registro	      
	      String fechaInicial="";
	      String fechaFinal="";
	      int idOperacion =0;
	      String centro="";
	      ParametrosListar parametros = new ParametrosListar();
	      //parametros.setFiltroFechaFinal(filtroFechaFinal);      
	      if (httpRequest.getParameter("idOperacion") != null) {
	       idOperacion = Integer.parseInt(httpRequest.getParameter("idOperacion") );	       
        }else {
         throw new Exception(gestorDiccionario.getMessage("sgo.noIdOperacion", null, locale));
        }
	      respuesta = dOperacion.recuperarRegistro(idOperacion);
	      if (respuesta.estado==false){
	       throw new Exception(gestorDiccionario.getMessage("sgo.noIdOperacion", null, locale));
	      }
	      Operacion operacion=null;
	      operacion = (Operacion) respuesta.contenido.carga.get(0);
	      centro= operacion.getReferenciaPlantaRecepcion();
	      destinatario = operacion.getReferenciaDestinatarioMercaderia();
	      
	      if(centro != null){
	    	  if (centro.length()>0){
	   	       esCentro=1;
	    	  } 
	      } else if(destinatario != null){
	   	   	if (destinatario.length()>0){
	 	       esCentro=0;
	 	    }
	   	 } else{
	   		throw new Exception(gestorDiccionario.getMessage("sgo.noIdOperacion", null, locale));
	   	 }

	      if ((centro.length()==0)&&(destinatario.length()==0)){
	       throw new Exception(gestorDiccionario.getMessage("sgo.noIdOperacion", null, locale));
	      }
	      
	      if (httpRequest.getParameter("filtroFechaInicio") != null) {
	        fechaInicial = httpRequest.getParameter("filtroFechaInicio");
	        String[] arrFechaInicial = fechaInicial.split("/");
	        fechaInicial = arrFechaInicial[0]+"."+arrFechaInicial[1]+"."+arrFechaInicial[2];
        }else {
         throw new Exception(gestorDiccionario.getMessage("sgo.noFechaInicial", null, locale));
        }
	      
	      /*
	      if (httpRequest.getParameter("filtroFechaFinal") != null) {
	        fechaFinal = httpRequest.getParameter("filtroFechaFinal");
          String[] arrFechaFinal = fechaFinal.split("/");
          fechaFinal = arrFechaFinal[0]+"."+arrFechaFinal[1]+"."+arrFechaFinal[2];
	      } else {
	       throw new Exception(gestorDiccionario.getMessage("sgo.noFechaFinal", null, locale));
	      }    */
	      
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String[] centros = new String[1]  ;
	      String[] destinatarios = new String[1];

	      if (esCentro ==1) {
	       centros[0]=centro;
	       parametrosSAP= new ConInfEnt(fechaInicial, "00:00:01", fechaInicial, "23:59:59", centros, null);
	      } else if (esCentro ==0) {
	       destinatarios[0]= destinatario;
	       parametrosSAP= new ConInfEnt(fechaInicial, "00:00:01", fechaInicial, "23:59:59", null, destinatarios);
	      }
	      
	      Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator servicio = new Petroperu_BC_SWCC_ConsInformacionEntregas_OutLocator();     
	       puerto = servicio.getHTTP_Port();
	       resultado = puerto.consInformacionEntregas_Out(parametrosSAP);
	       int numeroRegistros = resultado.length;
	       listaImportacion= new ArrayList<MaestroImportacion>();
	       contenido= new Contenido<MaestroImportacion>();
	       contenido.carga = new ArrayList<MaestroImportacion>();
	       int numeroDetalle = 0;
	       for (int contador=0;contador<numeroRegistros;contador++){
	          elemento = resultado[contador];
          
            maestroImportacion = new MaestroImportacion();
            maestroImportacion.setApellidoConductor(elemento.getApellidoConductor()) ;
            maestroImportacion.setBreveteConductor(elemento.getBreveteConductor()) ;
            maestroImportacion.setCodigoScop(elemento.getCodigoScop());
            maestroImportacion.setCodigoReferenciaConductor(elemento.getCodRefConductor());
            maestroImportacion.setCodigoReferenciaCliente(elemento.getCodRefDestinatario());
            maestroImportacion.setCodigoReferenciaCisterna(elemento.getCodReferenciaCisterna());
            maestroImportacion.setCodigoReferenciaPlantaDespacho(elemento.getCodRefPlantaDespacho());
            maestroImportacion.setCodigoReferenciaPlantaRecepcion(elemento.getCodRefPlantaRecepcion()) ;
            maestroImportacion.setCodigoReferenciaTracto(elemento.getCodRefTracto());
            maestroImportacion.setCodigoReferenciaTransportista(elemento.getCodRefTransportista());
            //maestroImportacion.setEsAnulada(Integer.parseInt(elemento.getEsAnulada()));         
            maestroImportacion.setNombreConductor(elemento.getNomConductor());
            maestroImportacion.setNombrePlantaDespacho(elemento.getNomPlantaDespacho());
            maestroImportacion.setNombrePlantaRecepcion(elemento.getNomPlantaRecepcion());
            maestroImportacion.setNumeroFactura(elemento.getNumeroFac());
            maestroImportacion.setNumeroGuiaRemision(elemento.getNumeroGR());
            maestroImportacion.setNumeroOrdenEntrega(elemento.getNumeroOC());
            maestroImportacion.setPlacaCisterna(elemento.getPlacaCisterna());
            maestroImportacion.setPlacaTracto(elemento.getPlacaTracto());
            maestroImportacion.setPrecintosSeguridad(elemento.getPrecintosSeguridadCisterna());
            maestroImportacion.setRazonSocialCliente(elemento.getRazonSocDestinatario());
            maestroImportacion.setRazonSocialTransportista(elemento.getRazonSocialTransportista());
            maestroImportacion.setTipoMovimiento(elemento.getTipoGR());           
            maestroImportacion.setVolumenCorregidoGuia(Float.parseFloat(elemento.getVolCorregidoGuia()));
            maestroImportacion.setVolumenObservadoGuia(Float.parseFloat(elemento.getVolObservadoGuia()));
           maestroImportacion.setFechaEmision(new java.sql.Date(sdf.parse(elemento.getFecEmiGR()).getTime()));
           compartimentos = elemento.getDetalle();
           numeroDetalle = compartimentos.length;
           for (int contadorDetalle=0;contadorDetalle<numeroDetalle;contadorDetalle++){
            detalleImportacion = new DetalleImportacion();
            
            detalleImportacion.setApiTemperaturaBase(Float.parseFloat(compartimentos[contadorDetalle].getApiTemperaturaBase()));
            detalleImportacion.setCapacidadVolumetricaCompartimento(Float.parseFloat( compartimentos[contadorDetalle].getCapVolCompartimento()));
            detalleImportacion.setCodigoOsinergProducto(compartimentos[contadorDetalle].getCodOsinergProducto());
            detalleImportacion.setCodigoReferenciaProducto(compartimentos[contadorDetalle].getCodRefProducto());
            detalleImportacion.setFactorCorreccion(Float.parseFloat(compartimentos[contadorDetalle].getFactorCorrecion()));
            detalleImportacion.setNombreProducto(compartimentos[contadorDetalle].getNomProducto());
            detalleImportacion.setNumeroCompartimento(Integer.parseInt(compartimentos[contadorDetalle].getNumCompartimento()));
            detalleImportacion.setTemperaturaObservada(Float.parseFloat(compartimentos[contadorDetalle].getTemperaturaObservada()));
            detalleImportacion.setUnidadMedidaVolumen(compartimentos[contadorDetalle].getUnidadMedVolumen());
            detalleImportacion.setVolumenCorregidoTemperaturaBase(Float.parseFloat(compartimentos[contadorDetalle].getVolCorregidoTemperaturaBase()));
            detalleImportacion.setVolumenTemperaturaObservada(Float.parseFloat(compartimentos[contadorDetalle].getVolTemperaturaObservada()));
             maestroImportacion.agregarDetalle(detalleImportacion);
           }           
           respuesta = dTransporte.recuperarRegistroxGuia(elemento.getNumeroGR());
           if (respuesta.contenido.carga.size() == 0) {
            maestroImportacion.setSituacion(Constante.SITUACION_NO_IMPORTADO);
           }else {
            maestroImportacion.setSituacion(Constante.SITUACION_IMPORTADO);
           }          
            contenido.carga.add(maestroImportacion);          
	       }
	      respuesta.contenido = contenido;
	      respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
	    } 
      catch (ServiceException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (ZPI_Fault e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      } catch (RemoteException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
      }  catch (Exception ex) {
	      ex.printStackTrace();
	      respuesta.estado = false;
	      respuesta.contenido = null;
	      respuesta.mensaje = ex.getMessage();
	    }
	    return respuesta;
	 }
	 
	 @RequestMapping(value = URL_GUARDAR_SAP_RELATIVA, method = RequestMethod.POST)
	 public @ResponseBody
	 RespuestaCompuesta guardarSAP(@RequestBody ListaMaestroImportacion listaMaestros, HttpServletRequest peticionHttp, Locale locale) {
	  RespuestaCompuesta respuesta = null;
	  AuthenticatedUserDetails principal = null;
	  Bitacora eBitacora = null;
	  String ContenidoAuditoria = "";
	  TransactionDefinition definicionTransaccion = null;
	  TransactionStatus estadoTransaccion = null;
	  String direccionIp = "";
	  int numeroMaestros=0;
	  MaestroImportacion maestroImportacion=null;
	  DetalleImportacion detalleImportacion = null;
	  Transporte transporte = null;
	  DetalleTransporte detalleTransporte = null;
	  Conductor conductor = null;
	  Operacion operacion = null;
	  int numeroDetalles=0;
	  Producto producto = null;
	  Asignacion eAsignacion = null;
	  String GuiasImportadas = "";
	  String separador = "";
	  int claveTransporte=0;
	  int claveDetalleTransporte=0;
	  BusquedaProgramado busquedaProgramado = null;
	  int numeroErrores = 0;
	  String mensajeErrores ="";
	  String mensajeError="";
	  String listaGuiasErradas="";
	  try {
	   // Inicia la transaccion
	   eBitacora = new Bitacora();
	   principal = this.getCurrentUser();
	   respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_SAP_COMPLETA);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
	   }
	   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
	   // Verificar si cuenta con el permiso necesario
	   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
	   }
     direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
     if (direccionIp == null) {
      direccionIp = peticionHttp.getRemoteAddr();
     }
     numeroMaestros = listaMaestros.getDetalle().size();
     ParametrosListar argumentosListar= new ParametrosListar();
     for(int contador =0 ; contador<numeroMaestros; contador++){      
      try {
       System.out.println("contador Principal:");
       System.out.println(contador);
       //Iniciar transaccion
       this.transaccion = new DataSourceTransactionManager(dCliente.getDataSource());
       definicionTransaccion = new DefaultTransactionDefinition();
       estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
       maestroImportacion= listaMaestros.getDetalle().get(contador);
       transporte= new Transporte();
       //Recuperar Operacion
       argumentosListar= new ParametrosListar();
       argumentosListar.setPaginacion(Constante.SIN_PAGINACION);
       String centroCliente ="";
       String destinatario="";
       centroCliente=maestroImportacion.getCodigoReferenciaPlantaRecepcion();
       destinatario = maestroImportacion.getCodigoReferenciaCliente();
       if (centroCliente.length()>0){
        argumentosListar.setFiltroCentroCliente(maestroImportacion.getCodigoReferenciaPlantaRecepcion());
       } else if (destinatario.length()>0){
        argumentosListar.setFiltroDestinarioMercaderia(maestroImportacion.getCodigoReferenciaCliente());
       }   
       respuesta = dOperacion.recuperarRegistros(argumentosListar);
       if (respuesta.estado==false){
        mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + " no se pudo encontrar la operación";
        throw new Exception(mensajeError);
       }
       if (respuesta.contenido.carga.size()==0){
        mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + " no se pudo encontrar la operación";
        throw new Exception(mensajeError);
       }
       operacion = (Operacion) respuesta.contenido.carga.get(0);
       //Recuperar Cisterna
       argumentosListar= new ParametrosListar();
       argumentosListar.setPaginacion(Constante.SIN_PAGINACION);
       argumentosListar.setFiltroPlacaCisterna(maestroImportacion.getPlacaCisterna());
       argumentosListar.setFiltroPlacaTracto(maestroImportacion.getPlacaTracto());//7000001925
       respuesta = dCisterna.recuperarRegistros(argumentosListar);
       if (respuesta.estado==false){
        mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + " no se pudo encontrar la cisterna "+ maestroImportacion.getPlacaCisterna()+" / " + maestroImportacion.getPlacaTracto();//7000001925 +tracto 
        throw new Exception(mensajeError);
       }
       if (respuesta.contenido.carga.size()==0){
        mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + " no se pudo encontrar la cisterna "+ maestroImportacion.getPlacaCisterna()+" / " + maestroImportacion.getPlacaTracto();//7000001925 +tracto 
        throw new Exception(mensajeError);
       }
       Cisterna cisterna = (Cisterna) respuesta.contenido.carga.get(0);
       transporte.setIdCisterna(cisterna.getId());
       transporte.setIdTracto(cisterna.getIdTracto()); //7000001925
       //TODO
       GuiasImportadas= GuiasImportadas + separador + maestroImportacion.getNumeroGuiaRemision();
       transporte.setNumeroGuiaRemision(maestroImportacion.getNumeroGuiaRemision());
       transporte.setNumeroOrdenCompra(maestroImportacion.getNumeroOrdenEntrega());
       transporte.setNumeroFactura(maestroImportacion.getNumeroFactura());
       transporte.setCodigoScop(maestroImportacion.getCodigoScop());
       transporte.setFechaEmisionGuia(maestroImportacion.getFechaEmision());
       transporte.setIdPlantaDespacho(operacion.getIdPlantaDespacho());
       transporte.setIdPlantaRecepcion(operacion.getId());
       transporte.setIdCliente(operacion.getIdCliente());
       //Recuperar Conductor
       argumentosListar.setPaginacion(Constante.SIN_PAGINACION);
       argumentosListar.setFiltroBrevete(maestroImportacion.getBreveteConductor());
       respuesta = dConductor.recuperarRegistros(argumentosListar);
       Boolean estadoRespuesta = respuesta.estado;
       int numeroConductores=0;
       if (estadoRespuesta==true){
        numeroConductores = respuesta.contenido.carga.size();
       }
       int claveGenerada= 0;
       if ((numeroConductores==0)){
        conductor = new Conductor();
        conductor.setId(0);
        conductor.setApellidos(maestroImportacion.getApellidoConductor());
        conductor.setBrevete(maestroImportacion.getBreveteConductor());
        conductor.setNombres(maestroImportacion.getNombreConductor());
        conductor.setDni("");
        conductor.setEstado(Constante.ESTADO_ACTIVO);
        conductor.setIdTransportista(cisterna.getIdTransportista());
        conductor.setCodigoReferencia("");
        conductor.setFechaNacimiento(null);       
        conductor.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        conductor.setActualizadoPor(principal.getID());
        conductor.setCreadoEl(Calendar.getInstance().getTime().getTime());
        conductor.setCreadoPor(principal.getID());
        conductor.setIpActualizacion(direccionIp);
        conductor.setIpCreacion(direccionIp);
        respuesta = dConductor.guardarRegistro(conductor);
        if (respuesta.estado==false){
         mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + " no se pudo agregar al conductor con brevete "+ maestroImportacion.getBreveteConductor();
         throw new Exception(mensajeError);
        }
        claveGenerada =Integer.parseInt(respuesta.valor) ;
        //
       } else {
        conductor = (Conductor) respuesta.contenido.carga.get(0);
        claveGenerada= conductor.getId();
       }
       transporte.setIdConductor(claveGenerada);
       transporte.setBreveteConductor(maestroImportacion.getBreveteConductor());
       transporte.setTarjetaCubicacionCompartimento(maestroImportacion.getTarjetaCubicacionCisterna());
       transporte.setIdTracto(cisterna.getIdTracto());
       transporte.setIdTransportista(cisterna.getIdTransportista());
       transporte.setVolumenTotalCorregido(maestroImportacion.getVolumenCorregidoGuia());
       transporte.setVolumenTotalObservado(maestroImportacion.getVolumenObservadoGuia());
       transporte.setEstado(Constante.TRANSPORTE_ESTADO_ASIGNADO);
       transporte.setPrecintosSeguridad(maestroImportacion.getPrecintosSeguridad());
       transporte.setOrigen(Constante.ORIGEN_AUTOMATICO);
       transporte.setPesoBruto(0);
       transporte.setPesoTara(0);
       transporte.setPesoNeto(0);
       transporte.setActualizadoEl(Calendar.getInstance().getTime().getTime());
       transporte.setActualizadoPor(principal.getID());
       transporte.setCreadoEl(Calendar.getInstance().getTime().getTime());
       transporte.setCreadoPor(principal.getID());
       transporte.setIpActualizacion(direccionIp);
       transporte.setIpCreacion(direccionIp);
       transporte.setProgramacion(0);
       transporte.setId(0);
       
       System.out.println(" listaMaestros.getIdDiaOperativo() -------> "  +  listaMaestros.getIdDiaOperativo());
       //respuesta = dTransporte.recuperarTransporteProgramado(transporte.getIdCisterna(), transporte.getIdConductor(), operacion.getId());
       //KANB
       respuesta = dTransporte.recuperarTransporteProgramado(transporte.getIdCisterna(), transporte.getIdConductor(), operacion.getId(), listaMaestros.getIdDiaOperativo(),transporte.getIdTracto());

       if (respuesta.estado==true){
         if (respuesta.contenido.carga.size()>0){
          busquedaProgramado =  (BusquedaProgramado) respuesta.contenido.carga.get(0);
          transporte.setId(busquedaProgramado.getIdTransporte());
          transporte.setProgramacion(busquedaProgramado.getProgramacion());
         }
       }
       
       if (transporte.getId()==0){
        respuesta = dTransporte.guardarRegistro(transporte);
        if (respuesta.estado == false) {
         mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + "no se pudo crear el transporte";
         throw new Exception(mensajeError);
       }
        claveGenerada = Integer.valueOf(respuesta.valor);       
        claveTransporte = claveGenerada;
        
        // ASIGNACION
        eAsignacion =  new Asignacion();
        eAsignacion.setIdTransporte(claveTransporte);
        eAsignacion.setIdDoperativo(listaMaestros.getIdDiaOperativo());
        eAsignacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eAsignacion.setActualizadoPor(principal.getID());
        eAsignacion.setIpActualizacion(direccionIp);
        RespuestaCompuesta respuestaAsignacion = dAsignacion.guardarRegistro(eAsignacion);
        if (respuestaAsignacion.estado == false) {
          throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
        }       
       }else {
    	   
    	//Agregado por obs 9000002608========================================================================
    	   boolean actualizar = false;
    	   respuesta = dTransporte.recuperarDetalleTransporteProgramado(transporte.getId());
    	   if (respuesta.contenido.carga.size()>0){
    		   List<BusquedaDetalleProgramado> lstBusquedaDetalleProgramadoTabla =  (List<BusquedaDetalleProgramado>) respuesta.contenido.carga;
    		   
    		   BusquedaDetalleProgramado detalleBusquedaTabla;
    		   DetalleImportacion detalleImportacionSap;

    		   int numeroDetallesSap = maestroImportacion.getDetalle().size();
    		   int tempBusqueda = lstBusquedaDetalleProgramadoTabla.size();
    		   for(int tempDetalles=0;tempDetalles<tempBusqueda;tempDetalles++){
    			   detalleBusquedaTabla = lstBusquedaDetalleProgramadoTabla.get(tempDetalles);
    			   
    			   int numCompartTabla = detalleBusquedaTabla.getNumeroCompartimento();
    			   String codRefProdTabla = detalleBusquedaTabla.getCodigoReferencia();

    			   actualizar = false;
    			   for (int contadorDetalles=0;contadorDetalles<numeroDetallesSap;contadorDetalles++){
    				   detalleImportacionSap = maestroImportacion.getDetalle().get(contadorDetalles);
    				   int numCompartSap = detalleImportacionSap.getNumeroCompartimento();
    				   
    				   if(numCompartTabla == numCompartSap){
    					   Integer tempCodRefProdSap = Integer.parseInt(detalleImportacionSap.getCodigoReferenciaProducto());
    					   String codRefProdSap = tempCodRefProdSap.toString();
    					   if(codRefProdTabla.equals(codRefProdSap)){
    						   actualizar = true;
    					   }else{
    						   actualizar = false;
    					   }
    					   break;
    				   }
    			   }
    			   
    			   if(!actualizar){
    	    		   break;
    	    	   }
    		   }
    	       
    	       if(actualizar){
    		        respuesta = dTransporte.actualizarRegistro(transporte);
    		        if (respuesta.estado == false) {
    		         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
    		       }
    		       // Respuesta numeroRegistros = dDetalleTransporte.numeroRegistrosPorTransporte(transporte.getId());
    		        Respuesta registrosEliminados = dDetalleTransporte.eliminarRegistrosPorTransporte(transporte.getId());
    		        if (registrosEliminados.estado == false) {
    		         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
    		       }

    		        claveTransporte= transporte.getId();
    	       }else{
    	    	   respuesta = dTransporte.guardarRegistro(transporte);
    	           if (respuesta.estado == false) {
    	            mensajeError = "Para la guia " + maestroImportacion.getNumeroGuiaRemision() + "no se pudo crear el transporte";
    	            throw new Exception(mensajeError);
    	          }
    	           claveGenerada = Integer.valueOf(respuesta.valor);       
    	           claveTransporte = claveGenerada;
    	           
    	           // ASIGNACION
    	           eAsignacion =  new Asignacion();
    	           eAsignacion.setIdTransporte(claveTransporte);
    	           eAsignacion.setIdDoperativo(listaMaestros.getIdDiaOperativo());
    	           eAsignacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
    	           eAsignacion.setActualizadoPor(principal.getID());
    	           eAsignacion.setIpActualizacion(direccionIp);
    	           RespuestaCompuesta respuestaAsignacion = dAsignacion.guardarRegistro(eAsignacion);
    	           if (respuestaAsignacion.estado == false) {
    	             throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    	           }
    	       }
    		   
    	   }
    	//===============================================================================================
    	   
    	//Comentado por 9000002608==================================================================
//	        respuesta = dTransporte.actualizarRegistro(transporte);
//	        if (respuesta.estado == false) {
//	         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
//	       }
//	       // Respuesta numeroRegistros = dDetalleTransporte.numeroRegistrosPorTransporte(transporte.getId());
//	        Respuesta registrosEliminados = dDetalleTransporte.eliminarRegistrosPorTransporte(transporte.getId());
//	        if (registrosEliminados.estado == false) {
//	         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
//	       }
//	        /*
//	        if (!numeroRegistros.valor.equals(registrosEliminados.valor)) {
//	          throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
//	        }*/
//	        claveTransporte= transporte.getId();
    	//==========================================================================================
       }
       //Actualizar Dia Operativo
       DiaOperativo  diaOperativo = new DiaOperativo();
       diaOperativo.setId(listaMaestros.getIdDiaOperativo());
       diaOperativo.setEstado(DiaOperativo.ESTADO_ASIGNADO); 
       diaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
       diaOperativo.setActualizadoPor(principal.getID());
       diaOperativo.setIpActualizacion(direccionIp);
       respuesta = dDiaOperativo.ActualizarEstadoRegistro(diaOperativo);
       if (respuesta.estado == false) {
        throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
       }
       //
       System.out.println("claveTransporte");
       System.out.println(claveTransporte);
       numeroDetalles = maestroImportacion.getDetalle().size();
       for (int contadorDetalles=0;contadorDetalles<numeroDetalles;contadorDetalles++){
        detalleTransporte = new DetalleTransporte();
        detalleImportacion = maestroImportacion.getDetalle().get(contadorDetalles);
        //Recuperar producto
        argumentosListar = new ParametrosListar();
        argumentosListar.setPaginacion(Constante.SIN_PAGINACION);
        argumentosListar.setFiltroCodigoReferencia(detalleImportacion.getCodigoReferenciaProducto().substring(detalleImportacion.getCodigoReferenciaProducto().length() - 5));
        respuesta = dProducto.recuperarRegistros(argumentosListar);
        if (respuesta.estado==false){
         throw new Exception(gestorDiccionario.getMessage("sgo.cisternaNoEncontrada", null, locale));
        }
        producto = (Producto) respuesta.contenido.carga.get(0);       
        detalleTransporte.setIdProducto(producto.getId());       
        detalleTransporte.setAlturaCompartimento(0);
        detalleTransporte.setApiTemperaturaBase(detalleImportacion.getApiTemperaturaBase());
        detalleTransporte.setCapacidadVolumetricaCompartimento(detalleImportacion.getCapacidadVolumetricaCompartimento());
        detalleTransporte.setDescripcionProducto("");
        detalleTransporte.setFactorCorrecion(detalleImportacion.getFactorCorreccion());
        detalleTransporte.setIdTransporte(claveTransporte);
        detalleTransporte.setNumeroCompartimento(detalleImportacion.getNumeroCompartimento());
        detalleTransporte.setTemperaturaObservada(detalleImportacion.getTemperaturaObservada());
        detalleTransporte.setUnidadMedida(detalleImportacion.getUnidadMedidaVolumen());
        detalleTransporte.setVolumenTemperaturaBase(detalleImportacion.getVolumenCorregidoTemperaturaBase());
        detalleTransporte.setVolumenTemperaturaObservada(detalleImportacion.getVolumenTemperaturaObservada());
        
        respuesta = dDetalleTransporte.guardarRegistro(detalleTransporte);
        if (respuesta.estado == false) {
         throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
        }       
       } 
       this.transaccion.commit(estadoTransaccion);
      } catch (Exception ex){
       this.transaccion.rollback(estadoTransaccion);
       numeroErrores++;
       mensajeErrores= mensajeErrores+" "+ex.getMessage();
       listaGuiasErradas=listaGuiasErradas+separador+maestroImportacion.getNumeroGuiaRemision();
      }
      separador=",";
     }
     respuesta.estado=true;
     respuesta.valor=GuiasImportadas;
     respuesta.mensaje=gestorDiccionario.getMessage("sgo.transporteImportadoExito", null, locale);
     respuesta.mensaje+=". Las guias ("+GuiasImportadas+")"+"fueron importadas con éxito";
     if (numeroErrores > 0){
      respuesta.estado =false;
      respuesta.mensaje=mensajeErrores;      
     }     	   
	  } catch (Exception ex) {	   
	   ex.printStackTrace();
	   respuesta.estado = false;
	   respuesta.contenido = null;
	   respuesta.mensaje = ex.getMessage();
	  }
	  return respuesta;
	 }
	
	@RequestMapping(value = URL_LISTAR_TRANSPORTES_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody
	RespuestaCompuesta recuperaTransportesAsignados(int ID, HttpServletRequest httpRequest, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		try {
			// Recupera el usuario actual
			principal = this.getCurrentUser();
			// Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_LISTAR_TRANSPORTES_COMPLETA);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
			}
			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
			// Verificar si cuenta con el permiso necesario
			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
				throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
			}
			// Recuperar el registro
			ParametrosListar parametros = new ParametrosListar();
			parametros.setPaginacion(Constante.CON_PAGINACION);
			if (httpRequest.getParameter("registrosxPagina") != null) {
				parametros.setRegistrosPaginaTransporte(Integer.parseInt(httpRequest.getParameter("registrosxPagina")));
			}
			if (httpRequest.getParameter("inicioPagina") != null) {
				parametros.setInicioPaginacion(Integer.parseInt(httpRequest.getParameter("inicioPagina")));
			}
			parametros.setCampoOrdenamiento("id");
			parametros.setSentidoOrdenamiento("asc");
			parametros.setFiltroDiaOperativo(ID);
			respuesta = dTransporte.recuperaTransportesAsignados(parametros);
			// respuesta= dTransporte.recuperaTransportesAsignados(ID);
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

	@RequestMapping(value = URL_LISTAR_ASIGNACION_TRANSPORTES_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody
	RespuestaCompuesta recuperaAsignacionDeTransportesPorDiaOperativo(HttpServletRequest httpRequest, Locale locale) {
		RespuestaCompuesta respuesta = null;
		ParametrosListar parametros = null;
		AuthenticatedUserDetails principal = null;
		String mensajeRespuesta = "";
		try {
			// Recuperar el usuario actual
			principal = this.getCurrentUser();
			// Recuperar el enlace de la accion
			respuesta = dEnlace
					.recuperarRegistro(URL_LISTAR_ASIGNACION_TRANSPORTES_COMPLETA);
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
			if (httpRequest.getParameter("ID") != null) {
				parametros.setFiltroDiaOperativo(Integer.parseInt(httpRequest.getParameter("ID")));
			}

			// Recuperamos los dias operativos
			respuesta = dAsignacionTransporteDao.recuperarRegistrosPorDiaOperativo(parametros);
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage("sgo.sgo.recuperarRegistrosFallido", null, locale));
			}

			respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.estado = false;
			respuesta.contenido = null;
			respuesta.mensaje = ex.getMessage();
		}
		return respuesta;
	}

	/*
	 * @RequestMapping(value = URL_LISTAR_ASIGNACION_TRANSPORTES_RELATIVA
	 * ,method = RequestMethod.GET) public @ResponseBody RespuestaCompuesta
	 * recuperaAsignacionDeTransportesPorDiaOperativo(int ID, Locale locale){
	 * RespuestaCompuesta respuesta = null; AuthenticatedUserDetails principal =
	 * null; try { //Recupera el usuario actual principal =
	 * this.getCurrentUser(); //Recuperar el enlace de la accion respuesta =
	 * dEnlace.recuperarRegistro(URL_LISTAR_ASIGNACION_TRANSPORTES_COMPLETA); if
	 * (respuesta.estado==false){ throw new
	 * Exception(gestorDiccionario.getMessage
	 * ("sgo.accionNoHabilitada",null,locale)); } Enlace eEnlace = (Enlace)
	 * respuesta.getContenido().getCarga().get(0); //Verificar si cuenta con el
	 * permiso necesario if
	 * (!principal.getRol().searchPermiso(eEnlace.getPermiso())){ throw new
	 * Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
	 * } //Recuperar el registro ParametrosListar parametros = new
	 * ParametrosListar(); parametros.setPaginacion(Constante.SIN_PAGINACION);
	 * parametros.setCampoOrdenamiento("descripcionProducto");
	 * parametros.setSentidoOrdenamiento("asc");
	 * parametros.setFiltroDiaOperativo(ID); //respuesta=
	 * dAsignacionTransporteDao.recuperarRegistrosPorDiaOperativo(ID);
	 * respuesta=
	 * dAsignacionTransporteDao.recuperarRegistrosPorDiaOperativo(parametros);
	 * //Verifica el resultado de la accion if (respuesta.estado==false){ throw
	 * new
	 * Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale
	 * )); }
	 * respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso"
	 * ,null,locale); } catch (Exception ex){ ex.printStackTrace();
	 * respuesta.estado=false; respuesta.contenido = null;
	 * respuesta.mensaje=ex.getMessage(); } return respuesta; }
	 */

	@RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
	public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody Asignacion eAsignacion,HttpServletRequest peticionHttp, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		Bitacora eBitacora = null;
		String ContenidoAuditoria = "";
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		String direccionIp = "";
		String ClaveGenerada = "";
		DetalleTransporte eDetalleTransporte = null;
		int idTransporteCreado;
		Planificacion ePlanificacion = null;
		try {
			// Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dTransporte.getDataSource());
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
			// TRANSPORTE
			Transporte eTransporte = (Transporte) eAsignacion.getTransportes().get(0);
			eTransporte.setCreadoEl(Calendar.getInstance().getTime().getTime());
			eTransporte.setCreadoPor(principal.getID());
			eTransporte.setIpCreacion(direccionIp);
			eTransporte.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eTransporte.setActualizadoPor(principal.getID());
			eTransporte.setIpActualizacion(direccionIp);
			eTransporte.setPesoBruto(0);
			eTransporte.setPesoNeto(0);
			eTransporte.setPesoTara(0);
			eTransporte.setEstado(Transporte.ESTADO_ASIGNADO);
			eTransporte.setOrigen(Transporte.ORIGEN_MANUAL);

			// verifica que la cisterna no este asignada con otro transporte
			/*Respuesta respuestaValidacion = dTransporte.validaCisternaPorEstadoDeTransporte(eTransporte.getId(), eTransporte.getIdCisterna());
			if (respuestaValidacion.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			}
			if (respuestaValidacion.valor != null) {
				throw new Exception(gestorDiccionario.getMessage("sgo.validaCisternaAsignada", null, locale));
			}*/
			//valida los datos que vienen del formulario
		    Respuesta validacion = Utilidades.validacionTransporteXSS(eTransporte, gestorDiccionario, locale);
		    if (validacion.estado == false) {
		      throw new Exception(validacion.valor);
		    }
			respuesta = dTransporte.guardarRegistro(eTransporte);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			}
			ClaveGenerada = respuesta.valor;
			idTransporteCreado = Integer.valueOf(respuesta.valor);
			// Guardar en la bitacora para transporte
			ObjectMapper mapper = new ObjectMapper(); // no need to do this if
														// you inject via
														// @Autowired
			ContenidoAuditoria = mapper.writeValueAsString(eTransporte);
			eBitacora.setUsuario(principal.getNombre());
			eBitacora.setAccion(URL_GUARDAR_EVENTO_COMPLETA);
			eBitacora.setTabla(TransporteDao.NOMBRE_TABLA);
			eBitacora.setIdentificador(ClaveGenerada);
			eBitacora.setContenido(ContenidoAuditoria);
			eBitacora.setRealizadoEl(eTransporte.getCreadoEl());
			eBitacora.setRealizadoPor(eTransporte.getCreadoPor());

			RespuestaCompuesta respuestaBitacoraTransporte = dBitacora.guardarRegistro(eBitacora);
			if (respuestaBitacoraTransporte.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
			}

			// DETALLE DE TRANSPORTE
			for (int contador = 0; contador < eTransporte.getDetalles().size(); contador++) {
				eDetalleTransporte = eTransporte.getDetalles().get(contador);
				eDetalleTransporte.setIdTransporte(idTransporteCreado);
				//valida los datos que vienen del formulario
		        validacion = Utilidades.validacionXSS(eDetalleTransporte, gestorDiccionario, locale);
		        if (validacion.estado == false) {
		          throw new Exception(validacion.valor);
		        }
				RespuestaCompuesta respuestaDetalleTransporte = dDetalleTransporte.guardarRegistro(eDetalleTransporte);
				if (respuestaDetalleTransporte.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}

				ClaveGenerada = respuestaDetalleTransporte.valor;
				// Guardar en la bitacora
				mapper = new ObjectMapper();
				ContenidoAuditoria = mapper.writeValueAsString(eDetalleTransporte);
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
				eBitacora.setTabla(DetalleTransporteDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(ClaveGenerada);
				eBitacora.setContenido(ContenidoAuditoria);
				eBitacora.setRealizadoEl(eTransporte.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				RespuestaCompuesta respuestaBitacoraDetalleTransporte = dBitacora.guardarRegistro(eBitacora);
				if (respuestaBitacoraDetalleTransporte.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
				}

				// VERIFICAMOS QUE LOS TRANSPORTES ASIGNADOS SE ENCUENTRAN
				// PLANIFICADOS
				RespuestaCompuesta verificaProductoPlanificacion = dPlanificacion.recuperarRegistroXDiaOperativoYProducto(eAsignacion.getIdDoperativo(), eDetalleTransporte.getIdProducto());
				if (!verificaProductoPlanificacion.estado) {
					throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
				}

				// Si respuesta tiene registros, recuperamos los transportes.
				if (verificaProductoPlanificacion.contenido.carga.isEmpty()) {
					System.out.print("el producto no existe");
					ePlanificacion = new Planificacion();
					ePlanificacion.setIdDoperativo(eAsignacion.getIdDoperativo());
					ePlanificacion.setIdProducto(eDetalleTransporte.getIdProducto());
					ePlanificacion.setVolumenPropuesto(0);
					ePlanificacion.setVolumenSolicitado(0);
					ePlanificacion.setCantidadCisternas(0);

					ePlanificacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
					ePlanificacion.setActualizadoPor(principal.getID());
					ePlanificacion.setIpActualizacion(direccionIp);
					RespuestaCompuesta agregaPlanificacion = dPlanificacion.guardarRegistro(ePlanificacion);
					if (agregaPlanificacion.estado == false) {
						throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
					}

					ClaveGenerada = respuesta.valor;
					// Guardar en la bitacora
					mapper = new ObjectMapper();
					ContenidoAuditoria = mapper.writeValueAsString(ePlanificacion);
					eBitacora.setUsuario(principal.getNombre());
					eBitacora.setAccion(URL_GUARDAR_COMPLETA);
					eBitacora.setTabla(PlanificacionDao.NOMBRE_TABLA);
					eBitacora.setIdentificador(ClaveGenerada);
					eBitacora.setContenido(ContenidoAuditoria);
					eBitacora.setRealizadoEl(ePlanificacion.getActualizadoEl());
					eBitacora.setRealizadoPor(ePlanificacion.getActualizadoPor());
					respuesta = dBitacora.guardarRegistro(eBitacora);
					if (respuesta.estado == false) {
						throw new Exception(gestorDiccionario.getMessage(
								"sgo.guardarBitacoraFallido", null, locale));
					}
				}
				// TERMINAMOS DE VERIFICAR QUE LOS TRANSPORTES ASIGNADOS SE
				// ENCUENTRAN PLANIFICADOS
			}

			// ASIGNACION
			eAsignacion.setIdTransporte(idTransporteCreado);
			eAsignacion.setActualizadoEl(Calendar.getInstance().getTime()
					.getTime());
			eAsignacion.setActualizadoPor(principal.getID());
			eAsignacion.setIpActualizacion(direccionIp);

			RespuestaCompuesta respuestaAsignacion = dAsignacion
					.guardarRegistro(eAsignacion);

			if (respuestaAsignacion.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.guardarFallido", null, locale));
			}
			ClaveGenerada = respuestaAsignacion.valor;
			// Guardar en la bitacora para asignacion
			mapper = new ObjectMapper(); // no need to do this if you inject via
											// @Autowired
			ContenidoAuditoria = mapper.writeValueAsString(eAsignacion);
			eBitacora.setUsuario(principal.getNombre());
			eBitacora.setAccion(URL_GUARDAR_EVENTO_COMPLETA);
			eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
			eBitacora.setIdentificador(ClaveGenerada);
			eBitacora.setContenido(ContenidoAuditoria);
			eBitacora.setRealizadoEl(eAsignacion.getCreadoEl());
			eBitacora.setRealizadoPor(eAsignacion.getCreadoPor());
			RespuestaCompuesta respuestaBitacoraAsignacion = dBitacora
					.guardarRegistro(eBitacora);
			if (respuestaBitacoraAsignacion.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.guardarBitacoraFallido", null, locale));
			}

			// CAMBIO A ESTADO ASIGNADO AL DIA OPERATIVO
			DiaOperativo eDiaOperativo = new DiaOperativo();
			eDiaOperativo.setId(eAsignacion.getIdDoperativo());
			eDiaOperativo.setEstado(DiaOperativo.ESTADO_ASIGNADO);
			eDiaOperativo.setActualizadoPor(principal.getID());
			eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime()
					.getTime());
			eDiaOperativo.setIpActualizacion(direccionIp);

			RespuestaCompuesta respuestaDiaOperativo = dDiaOperativo
					.ActualizarEstadoRegistro(eDiaOperativo);
			if (respuestaDiaOperativo.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.actualizarFallido", null, locale));
			}
			// Guardar en la bitacora
			eBitacora.setUsuario(principal.getNombre());
			eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
			eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
			eBitacora.setIdentificador(String.valueOf(eDiaOperativo.getId()));
			eBitacora.setContenido(mapper.writeValueAsString(eDiaOperativo));
			eBitacora.setRealizadoEl(eDiaOperativo.getActualizadoEl());
			eBitacora.setRealizadoPor(eDiaOperativo.getActualizadoPor());
			respuesta = dBitacora.guardarRegistro(eBitacora);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.guardarBitacoraFallido", null, locale));
			}

			respuesta.mensaje = gestorDiccionario.getMessage(
					"sgo.guardarExitoso", new Object[] {
							eAsignacion.getFechaCreacion().substring(0, 9),
							eAsignacion.getFechaCreacion().substring(10),
							principal.getIdentidad() }, locale);
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
	RespuestaCompuesta actualizarRegistro(@RequestBody Asignacion eAsignacion, HttpServletRequest peticionHttp, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		String ContenidoAuditoria = "";
		Bitacora eBitacora = null;
		String direccionIp = "";
		String ClaveGenerada = "";
		DetalleTransporte eDetalleTransporte = null;
		Planificacion ePlanificacion = null;
		try {
			// Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dPlanificacion.getDataSource());
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

			// eAsignacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			// eAsignacion.setActualizadoPor(principal.getID());
			// eAsignacion.setIpActualizacion(direccionIp);
			//
			// Primero se actualiza la asignacion
			// respuesta= dAsignacion.actualizarRegistro(eAsignacion);
			//
			// if (respuesta.estado==false){
			// throw new
			// Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
			// }
			Transporte eTransporte = (Transporte) eAsignacion.getTransportes().get(0);
			eTransporte.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eTransporte.setActualizadoPor(principal.getID());
			eTransporte.setIpActualizacion(direccionIp);
			//eTransporte.setOrigen(Transporte.ORIGEN_MANUAL);
			eTransporte.setEstado(Transporte.ESTADO_ASIGNADO);
			// eTransporte.setIdPlantaRecepcion(Integer.valueOf(principal.getOperacion().getReferenciaPlantaRecepcion()));
			//valida los datos que vienen del formulario
		    Respuesta validacion = Utilidades.validacionTransporteXSS(eTransporte, gestorDiccionario, locale);
		    if (validacion.estado == false) {
		      throw new Exception(validacion.valor);
		    }
			RespuestaCompuesta respuestaTransporte = dTransporte.actualizarRegistro(eTransporte);

			if (respuestaTransporte.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			}

			Respuesta numeroRegistros = dDetalleTransporte.numeroRegistrosPorTransporte(eTransporte.getId());
			Respuesta registrosEliminados = dDetalleTransporte.eliminarRegistrosPorTransporte(eTransporte.getId());

			if (!numeroRegistros.valor.equals(registrosEliminados.valor)) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			}

			for (int contador = 0; contador < eTransporte.getDetalles().size(); contador++) {
				eDetalleTransporte = eTransporte.getDetalles().get(contador);
				float factorCorrecion = (float) Formula.calcularFactorCorreccion(
																	eDetalleTransporte.getApiTemperaturaBase(),
																	eDetalleTransporte.getTemperaturaObservada());
				eDetalleTransporte.setFactorCorrecion(factorCorrecion);
				
				//valida los datos que vienen del formulario
			    validacion = Utilidades.validacionXSS(eDetalleTransporte, gestorDiccionario, locale);
			    if (validacion.estado == false) {
			      throw new Exception(validacion.valor);
			    }
				RespuestaCompuesta respuestaDetalleTransporte = dDetalleTransporte.guardarRegistro(eDetalleTransporte);
				if (respuestaDetalleTransporte.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}
				ClaveGenerada = respuestaDetalleTransporte.valor;
				// Guardar en la bitacora
				ObjectMapper mapper = new ObjectMapper();
				ContenidoAuditoria = mapper.writeValueAsString(eDetalleTransporte);
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
				eBitacora.setTabla(DetalleTransporteDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(ClaveGenerada);
				eBitacora.setContenido(ContenidoAuditoria);
				eBitacora.setRealizadoEl(eTransporte.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				RespuestaCompuesta respuestaBitacora = dBitacora.guardarRegistro(eBitacora);
				if (respuestaBitacora.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
				}

				// VERIFICAMOS QUE LOS TRANSPORTES ASIGNADOS SE ENCUENTRAN
				// PLANIFICADOS
				RespuestaCompuesta verificaProductoPlanificacion = dPlanificacion.recuperarRegistroXDiaOperativoYProducto(eAsignacion.getIdDoperativo(), eDetalleTransporte.getIdProducto());
				if (!verificaProductoPlanificacion.estado) {
					throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
				}

				// Si respuesta tiene registros, recuperamos los transportes.
				if (verificaProductoPlanificacion.contenido.carga.isEmpty()) {
					System.out.print("el producto no existe");
					ePlanificacion = new Planificacion();
					ePlanificacion.setIdDoperativo(eAsignacion.getIdDoperativo());
					ePlanificacion.setIdProducto(eDetalleTransporte.getIdProducto());
					ePlanificacion.setVolumenPropuesto(0);
					ePlanificacion.setVolumenSolicitado(0);
					ePlanificacion.setCantidadCisternas(0);

					ePlanificacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
					ePlanificacion.setActualizadoPor(principal.getID());
					ePlanificacion.setIpActualizacion(direccionIp);
					RespuestaCompuesta agregaPlanificacion = dPlanificacion.guardarRegistro(ePlanificacion);
					if (agregaPlanificacion.estado == false) {
						throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
					}

					ClaveGenerada = respuesta.valor;
					// Guardar en la bitacora
					mapper = new ObjectMapper();
					ContenidoAuditoria = mapper
							.writeValueAsString(ePlanificacion);
					eBitacora.setUsuario(principal.getNombre());
					eBitacora.setAccion(URL_GUARDAR_COMPLETA);
					eBitacora.setTabla(PlanificacionDao.NOMBRE_TABLA);
					eBitacora.setIdentificador(ClaveGenerada);
					eBitacora.setContenido(ContenidoAuditoria);
					eBitacora.setRealizadoEl(ePlanificacion.getActualizadoEl());
					eBitacora.setRealizadoPor(ePlanificacion
							.getActualizadoPor());
					respuesta = dBitacora.guardarRegistro(eBitacora);
					if (respuesta.estado == false) {
						throw new Exception(gestorDiccionario.getMessage(
								"sgo.guardarBitacoraFallido", null, locale));
					}
				}
				// TERMINAMOS DE VERIFICAR QUE LOS TRANSPORTES ASIGNADOS SE
				// ENCUENTRAN PLANIFICADOS
			}

			// CAMBIO A ESTADO ASIGNADO AL DIA OPERATIVO
			DiaOperativo eDiaOperativo = new DiaOperativo();
			eDiaOperativo.setId(eAsignacion.getIdDoperativo());
			eDiaOperativo.setEstado(DiaOperativo.ESTADO_ASIGNADO);
			eDiaOperativo.setActualizadoPor(principal.getID());
			eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eDiaOperativo.setIpActualizacion(direccionIp);

			RespuestaCompuesta respuestaDiaOperativo = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
			if (respuestaDiaOperativo.estado == false) {
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
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.guardarBitacoraFallido", null, locale));
			}

			respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {eTransporte.getFechaActualizacion().substring(0, 9),eTransporte.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);
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

	@RequestMapping(value = URL_ACTUALIZAR_PESAJE_RELATIVA, method = RequestMethod.POST)
	public @ResponseBody
	RespuestaCompuesta actualizarPesosDelRegistro(@RequestBody Transporte eTransporte, HttpServletRequest peticionHttp, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		Bitacora eBitacora = null;
		String direccionIp = "";
		try {
			// Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dTransporte.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
			// Recuperar el usuario actual
			principal = this.getCurrentUser();
			// Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_PESAJE_COMPLETA);
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
			eTransporte.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eTransporte.setActualizadoPor(principal.getID());
			eTransporte.setIpActualizacion(direccionIp);
			//valida los datos que vienen del formulario
		    Respuesta validacion = Utilidades.validacionPesajeTransporteXSS(eTransporte, gestorDiccionario, locale);
		    if (validacion.estado == false) {
		      throw new Exception(validacion.valor);
		    }
			respuesta = dTransporte.actualizarPesajes(eTransporte);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
			}
			// Guardar en la bitacora
			ObjectMapper mapper = new ObjectMapper();
			eBitacora.setUsuario(principal.getNombre());
			eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
			eBitacora.setTabla(TransporteDao.NOMBRE_TABLA);
			eBitacora.setIdentificador(String.valueOf(eTransporte.getId()));
			eBitacora.setContenido(mapper.writeValueAsString(eTransporte));
			eBitacora.setRealizadoEl(eTransporte.getActualizadoEl());
			eBitacora.setRealizadoPor(eTransporte.getActualizadoPor());
			respuesta = dBitacora.guardarRegistro(eBitacora);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
			}
			respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {eTransporte.getFechaActualizacion().substring(0, 9),eTransporte.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);;
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

	//
	@RequestMapping(value = URL_LISTAR_FILTRAR_TRANSPORTE_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody
	RespuestaCompuesta recuperarDiasOperativosTransporte(HttpServletRequest httpRequest, Locale locale) {
		RespuestaCompuesta respuesta = null;
		ParametrosListar parametros = null;
		AuthenticatedUserDetails principal = null;
		String mensajeRespuesta = "";
		try {
			// Recuperar el usuario actual
			principal = this.getCurrentUser();
			// Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_LISTAR_FILTRAR_TRANSPORTE_COMPLETA);
			if (respuesta.estado == false) {
				mensajeRespuesta = gestorDiccionario.getMessage(
						"sgo.accionNoHabilitada", null, locale);
				throw new Exception(mensajeRespuesta);
			}
			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga()
					.get(0);
			// Verificar si cuenta con el permiso necesario
			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
				mensajeRespuesta = gestorDiccionario.getMessage(
						"sgo.faltaPermiso", null, locale);
				throw new Exception(mensajeRespuesta);
			}
			// Recuperar parametros
			parametros = new ParametrosListar();
			if (httpRequest.getParameter("paginacion") != null) {
				parametros.setPaginacion(Integer.parseInt(httpRequest
						.getParameter("paginacion")));
			}

			if (httpRequest.getParameter("filtroOperacion") != null) {
				parametros.setFiltroOperacion(Integer.parseInt(httpRequest
						.getParameter("filtroOperacion")));
			}
			if (httpRequest.getParameter("filtroEstado") != null && !httpRequest.getParameter("filtroEstado").isEmpty()) {
				parametros.setFiltroEstado(Integer.parseInt(httpRequest
						.getParameter("filtroEstado")));
			}
			if (httpRequest.getParameter("filtroIdTransporte") != null && !httpRequest.getParameter("filtroIdTransporte").isEmpty()) {
				parametros.setIdTransporte(Integer.parseInt(httpRequest
						.getParameter("filtroIdTransporte")));
			}
			//parametros.setFiltroEstado(Transporte.ESTADO_ASIGNADO);
			// Recuperamos los transportes
			respuesta = dTransporte.recuperaTransportesDescarga(parametros);
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage("sgo.sgo.recuperarRegistrosFallido", null, locale));
			}

			respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.estado = false;
			respuesta.contenido = null;
			respuesta.mensaje = ex.getMessage();
		}
		return respuesta;
	}

	@RequestMapping(value = URL_RECUPERAR_TRANSPORTE_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody
	RespuestaCompuesta recuperaRegistro(int ID, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		Transporte eTransporte = null;
		Cisterna eCisterna = null;
		Contenido<Transporte> contenido = null;
		try {
			principal = this.getCurrentUser();
			respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.accionNoHabilitada", null, locale));
			}
			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga()
					.get(0);
			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.faltaPermiso", null, locale));
			}
			respuesta = dTransporte.recuperarRegistro(ID);
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.recuperarFallido", null, locale));
			}

			if (respuesta.contenido.carga.isEmpty()) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.recuperarFallido", null, locale));
			}

			eTransporte = (Transporte) respuesta.contenido.carga.get(0);

			respuesta = dCisterna
					.recuperarRegistro(eTransporte.getIdCisterna());
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage(
						"sgo.recuperarFallido", null, locale));
			}
			eTransporte
					.setCisterna((Cisterna) respuesta.contenido.carga.get(0));

			respuesta = dDetalleTransporte.recuperarDetalleTransporte(eTransporte.getId());
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			}
			for (Object detalle : respuesta.contenido.carga) {
				eTransporte.agregarDetalle((DetalleTransporte) detalle);
			}

			respuesta = dEvento.recuperarRegistroPorIdRegistroYTipoRegistro(eTransporte.getId(), 1);
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			}

			for (Object evento : respuesta.contenido.carga) {
				eTransporte.agregarEvento((Evento) evento);
			}

			eCisterna = (Cisterna) eTransporte.getCisterna();
			respuesta = dCompartimento.recuperarRegistroPorIdCisterna(eCisterna.getId());
			if (!respuesta.estado) {
				throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			}

			for (Object compartimento : respuesta.contenido.carga) {
				eCisterna.agregarCompartimento((Compartimento) compartimento);
			}

			contenido = new Contenido<Transporte>();
			contenido.carga = new ArrayList<Transporte>();
			contenido.carga.add(eTransporte);
			respuesta.contenido = contenido;
			respuesta.estado = true;
			respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.estado = false;
			respuesta.contenido = null;
			respuesta.mensaje = ex.getMessage();
		}
		return respuesta;
	}

	@RequestMapping(value = URL_GUARDAR_PROGRAMACION_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta guardarRegistro(int ID, HttpServletRequest peticionHttp, Locale locale) {
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		Bitacora eBitacora= null;
		String ContenidoAuditoria ="";
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		String direccionIp="";
		String ClaveGenerada="";
		DetalleTransporte eDetalleTransporte = null;
		int idTransporteCreado;
		Transporte eTransporte = null;
		DetalleProgramacion eDetalleProgramacion = null;
		ParametrosListar parametros = null; 
		
		try {
			//Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dTransporte.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
			//Recuperar el usuario actual
			principal = this.getCurrentUser(); 
			//Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_PROGRAMACION_COMPLETA);
			if (respuesta.estado==false){
				throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
			}
			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
			//Verificar si cuenta con el permiso necesario			
			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
				throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
			}
			//Actualiza los datos de auditoria local
			direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
			if (direccionIp == null) {
				direccionIp = peticionHttp.getRemoteAddr();
			}
			
			
			
			//Recuperamos el detalle de la programaci�n
			parametros = new ParametrosListar();
			parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
			parametros.setFiltroDiaOperativo(ID);
			RespuestaCompuesta respuestaProgramaciones = dProgramacion.recuperaProgramacionDetalle(ID);
			if (respuestaProgramaciones.estado==false){     	
	            throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
	        }
			System.out.println(respuestaProgramaciones.contenido.carga.size());
			
			
			for(int i = 0; i < respuestaProgramaciones.contenido.carga.size(); i++){
				Programacion eProgramacion = (Programacion) respuestaProgramaciones.contenido.carga.get(i);

				RespuestaCompuesta respuestaDiaOperativo = dDiaOperativo.recuperarRegistro(eProgramacion.getIdDiaOperativo());
				if (respuestaDiaOperativo.estado==false){     	
					throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
		        }
				//recuperamos el día operativo para saber la fecha de carga de la planificacion
				DiaOperativo eDiaOperativo = (DiaOperativo) respuestaDiaOperativo.contenido.carga.get(0);
				
				//7000001924 RespuestaCompuesta respuestaDetalleProgramacion = dDetalleProgramacion.recuperarDetalleProgramacion(eProgramacion.getId());
				RespuestaCompuesta respuestaDetalleProgramacion = dDetalleProgramacion.recuperarDetalleProgramacion2Transportista(eProgramacion.getId());
				if (respuestaDetalleProgramacion.estado==false){     	
		            throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
		         }
				
				//Cambio nueva logica para clonacion req ======================================
				
				System.out.println("size: " + respuestaDetalleProgramacion.contenido.carga.size());
				eDetalleProgramacion = new DetalleProgramacion();
				for(int k = 0; k < respuestaDetalleProgramacion.contenido.carga.size(); k++) {
					eDetalleProgramacion = (DetalleProgramacion) respuestaDetalleProgramacion.contenido.carga.get(k);
					
					System.out.println("k: " + k);
					System.out.println("eDetalleProgramacion.getIdCisterna(): " + eDetalleProgramacion.getIdCisterna());
					System.out.println("eDetalleProgramacion.getCisterna().getIdTracto(): " + eDetalleProgramacion.getCisterna().getIdTracto());
					System.out.println("eDetalleProgramacion.getNumeroCompartimiento(): " + eDetalleProgramacion.getNumeroCompartimiento());
					RespuestaCompuesta respuestaDatosClonacion = dDetalleTransporte.recuperarTransYdetalleTrans(eDetalleProgramacion.getIdCisterna(), 
																													eDetalleProgramacion.getCisterna().getIdTracto(),
																													eDetalleProgramacion.getNumeroCompartimiento(),
																													eDetalleProgramacion.getIdProgramacion());
			         if (respuestaDatosClonacion.estado==false){     	
			           	throw new Exception(gestorDiccionario.getMessage("sgo.transporte",null,locale));
			         }
			         
			         int cantidadRegistros = respuestaDatosClonacion.contenido.carga.size();
			         
			         System.out.println("cantidadRegistros: " + cantidadRegistros);
			         if(cantidadRegistros == 0){
			        	 
			        	 idTransporteCreado = clonarTransporteYDetalle(eTransporte, eDetalleProgramacion, eDiaOperativo,
			        			 				ContenidoAuditoria, eBitacora, respuesta, principal, direccionIp, ClaveGenerada, locale);
			        	 
			             // ASIGNACION
							Asignacion eAsignacion = new Asignacion();
							eAsignacion.setIdDoperativo(ID);
							eAsignacion.setIdTransporte(idTransporteCreado);
							eAsignacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
							eAsignacion.setActualizadoPor(principal.getID());
							eAsignacion.setIpActualizacion(direccionIp);

							RespuestaCompuesta respuestaAsignacion = dAsignacion.guardarRegistro(eAsignacion);

							if (respuestaAsignacion.estado == false) {
								throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
							}
							ClaveGenerada = respuestaAsignacion.valor;
							// Guardar en la bitacora para asignacion
							ObjectMapper mapper = new ObjectMapper(); 
							ContenidoAuditoria = mapper.writeValueAsString(eAsignacion);
							eBitacora.setUsuario(principal.getNombre());
							eBitacora.setAccion(URL_GUARDAR_EVENTO_COMPLETA);
							eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
							eBitacora.setIdentificador(ClaveGenerada);
							eBitacora.setContenido(ContenidoAuditoria);
							eBitacora.setRealizadoEl(eAsignacion.getCreadoEl());
							eBitacora.setRealizadoPor(eAsignacion.getCreadoPor());
							RespuestaCompuesta respuestaBitacoraAsignacion = dBitacora.guardarRegistro(eBitacora);
							if (respuestaBitacoraAsignacion.estado == false) {
								throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
							}
			        	 
			        	 System.out.println("1 idTransporteCreado: " + idTransporteCreado);
			        	 
			         }else{
			        	 
			        	 ClonacionProgTrans clonacionProgTrans = (ClonacionProgTrans)respuestaDatosClonacion.contenido.carga.get(0);
			        	 
			        	 System.out.println("clonacionProgTrans.getIdTransporte(): " + clonacionProgTrans.getIdTransporte());
			        	 System.out.println("clonacionProgTrans.getIdDetalleTransporte(): " + clonacionProgTrans.getIdDetalleTransporte());
			        	 if(clonacionProgTrans.getIdDetalleTransporte() == null || clonacionProgTrans.getIdDetalleTransporte() == 0){
			        		 
			        		 idTransporteCreado = clonacionProgTrans.getIdTransporte();
			        		 
			        		 clonarSoloDetalleTransporte(idTransporteCreado, eDetalleProgramacion.getIdProducto(), 
	     		 						eDetalleProgramacion.getNumeroCompartimiento(), eDetalleProgramacion.getCapacidadVolumetrica(),
	     		 						principal, ContenidoAuditoria, eBitacora, ClaveGenerada, locale);
			        		 
			        		 System.out.println("solo detalle");

			        	 }else{
			                 
			        		 idTransporteCreado = clonarTransporteYDetalle(eTransporte, eDetalleProgramacion, eDiaOperativo,
	        			 				ContenidoAuditoria, eBitacora, respuesta, principal, direccionIp, ClaveGenerada, locale);
			        		 
				             // ASIGNACION
								Asignacion eAsignacion = new Asignacion();
								eAsignacion.setIdDoperativo(ID);
								eAsignacion.setIdTransporte(idTransporteCreado);
								eAsignacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
								eAsignacion.setActualizadoPor(principal.getID());
								eAsignacion.setIpActualizacion(direccionIp);

								RespuestaCompuesta respuestaAsignacion = dAsignacion.guardarRegistro(eAsignacion);

								if (respuestaAsignacion.estado == false) {
									throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
								}
								ClaveGenerada = respuestaAsignacion.valor;
								// Guardar en la bitacora para asignacion
								ObjectMapper mapper = new ObjectMapper(); 
								ContenidoAuditoria = mapper.writeValueAsString(eAsignacion);
								eBitacora.setUsuario(principal.getNombre());
								eBitacora.setAccion(URL_GUARDAR_EVENTO_COMPLETA);
								eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
								eBitacora.setIdentificador(ClaveGenerada);
								eBitacora.setContenido(ContenidoAuditoria);
								eBitacora.setRealizadoEl(eAsignacion.getCreadoEl());
								eBitacora.setRealizadoPor(eAsignacion.getCreadoPor());
								RespuestaCompuesta respuestaBitacoraAsignacion = dBitacora.guardarRegistro(eBitacora);
								if (respuestaBitacoraAsignacion.estado == false) {
									throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
								}
			        		 
			        		 System.out.println("2 idTransporteCreado: " + idTransporteCreado);
			        		 
			        	 }
			        	 
			         }
			         
				}
				
				//=============================================================================
		    }
			//esto para cambio de estado si fuera necesario.
			//CAMBIO A ESTADO PROGRAMACION AL DIA OPERATIVO
	        DiaOperativo eDiaOperativo = new DiaOperativo();
	        eDiaOperativo.setId(ID);
	        eDiaOperativo.setEstado(DiaOperativo.ESTADO_PROGRAMACION);
	        eDiaOperativo.setActualizadoPor(principal.getID());
	        eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
	        eDiaOperativo.setIpActualizacion(direccionIp);
	         
	        RespuestaCompuesta respuestaDiaOperativo = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
	        if (respuestaDiaOperativo.estado == false) {
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
	   	    
	   	    String fechaActualizacionFormateada="";
			Date fechaActualizacion = new Date(Calendar.getInstance().getTime().getTime());
			SimpleDateFormat formateadorFecha = null;
			
			formateadorFecha=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaActualizacionFormateada = formateadorFecha.format(fechaActualizacion); 
			
			respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {fechaActualizacionFormateada.substring(0, 9),fechaActualizacionFormateada.substring(10),principal.getIdentidad() }, locale);
	   	    if (respuesta.estado == false) {
	   	      throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
	   	    }
   	     	this.transaccion.commit(estadoTransaccion);
			
		} catch (Exception ex){
			this.transaccion.rollback(estadoTransaccion);
			ex.printStackTrace();
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	}

	private AuthenticatedUserDetails getCurrentUser() {
		return (AuthenticatedUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	}
	
	private Integer clonarTransporteYDetalle(Transporte eTransporte, DetalleProgramacion eDetalleProgramacion, DiaOperativo eDiaOperativo,
											String ContenidoAuditoria, Bitacora eBitacora, RespuestaCompuesta respuesta,
											AuthenticatedUserDetails principal, String direccionIp, String ClaveGenerada, Locale locale) throws Exception{
		int idTransporteCreado;
		
		eTransporte = new Transporte();
		eTransporte.setProgramacion(eDetalleProgramacion.getIdProgramacion());
		eTransporte.setIdPlantaDespacho(eDetalleProgramacion.getProgramacion().getDiaOperativo().getOperacion().getIdPlantaDespacho());
		eTransporte.setIdPlantaRecepcion(eDetalleProgramacion.getProgramacion().getDiaOperativo().getIdOperacion());
		eTransporte.setIdCliente(eDetalleProgramacion.getProgramacion().getDiaOperativo().getOperacion().getIdCliente());
		eTransporte.setBreveteConductor(eDetalleProgramacion.getConductor().getBrevete());
		eTransporte.setVolumenTotalCorregido(0);
		eTransporte.setVolumenTotalObservado(0);
		eTransporte.setCodigoScop(eDetalleProgramacion.getCodigoScop());
		eTransporte.setEstado(Transporte.ESTADO_PROGRAMADO);
		eTransporte.setIdTracto(eDetalleProgramacion.getCisterna().getIdTracto());
		eTransporte.setTarjetaCubicacionCompartimento(eDetalleProgramacion.getCisterna().getTarjetaCubicacion());
		eTransporte.setOrigen(Transporte.ORIGEN_SEMI_AUTOMATICO);
		eTransporte.setPesoBruto(0);
        eTransporte.setPesoNeto(0);
        eTransporte.setPesoTara(0);
		eTransporte.setIdTransportista(eDetalleProgramacion.getProgramacion().getIdTransportista());
		eTransporte.setIdConductor(eDetalleProgramacion.getIdConductor());
		eTransporte.setIdCisterna(eDetalleProgramacion.getIdCisterna());
		//Asignamos a la fecha de guia de emisión la fecha de la carga de la planificación.
		eTransporte.setFechaEmisionGuia(eDiaOperativo.getFechaEstimadaCarga());
		//datos bitacora
		eTransporte.setCreadoEl(Calendar.getInstance().getTime().getTime());
        eTransporte.setCreadoPor(principal.getID()); 
        eTransporte.setIpCreacion(direccionIp);
        eTransporte.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eTransporte.setActualizadoPor(principal.getID()); 
        eTransporte.setIpActualizacion(direccionIp);
        
        /*//verifica que la cisterna no este asignada con otro transporte
         Respuesta respuestaValidacion = dTransporte.validaCisternaPorEstadoDeTransporte(0, eTransporte.getIdCisterna());
         if (respuestaValidacion.estado==false){     	
            throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
         }
         if (respuestaValidacion.valor != null){
        	 throw new Exception(gestorDiccionario.getMessage("sgo.validaCisternaAsignada",null,locale));
         }*/
         
         respuesta = dTransporte.guardarRegistro(eTransporte);
         if (respuesta.estado==false){     	
           	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
         }
         ClaveGenerada = respuesta.valor;
 		 idTransporteCreado = Integer.valueOf(respuesta.valor);
         //Guardar en la bitacora para transporte
 		 ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
         ContenidoAuditoria =  mapper.writeValueAsString(eTransporte);
         eBitacora.setUsuario(principal.getNombre());
         eBitacora.setAccion(URL_GUARDAR_EVENTO_COMPLETA);
         eBitacora.setTabla(TransporteDao.NOMBRE_TABLA);
         eBitacora.setIdentificador(ClaveGenerada);
         eBitacora.setContenido(ContenidoAuditoria);
         eBitacora.setRealizadoEl(eTransporte.getCreadoEl());
         eBitacora.setRealizadoPor(eTransporte.getCreadoPor());

         RespuestaCompuesta respuestaBitacoraTransporte = dBitacora.guardarRegistro(eBitacora);
         if (respuestaBitacoraTransporte.estado==false){     	
           	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
         }
         
         clonarSoloDetalleTransporte(idTransporteCreado, eDetalleProgramacion.getIdProducto(), 
        		 						eDetalleProgramacion.getNumeroCompartimiento(), eDetalleProgramacion.getCapacidadVolumetrica(),
        		 						principal, ContenidoAuditoria, eBitacora, ClaveGenerada, locale);
         
         return idTransporteCreado;
	}
	
	private void clonarSoloDetalleTransporte(Integer idTransporteCreado, Integer idProducto, 
												Integer numeroCompartimento, Integer capacidadVolumetrica, 
												AuthenticatedUserDetails principal, String ContenidoAuditoria,
												Bitacora eBitacora, String ClaveGenerada, Locale locale) throws Exception{
		
		DetalleTransporte eDetalleTransporte = new DetalleTransporte();
		eDetalleTransporte.setIdProducto(idProducto);
        eDetalleTransporte.setNumeroCompartimento(numeroCompartimento);
        eDetalleTransporte.setCapacidadVolumetricaCompartimento(capacidadVolumetrica);
        eDetalleTransporte.setVolumenTemperaturaObservada(capacidadVolumetrica);
        
        eDetalleTransporte.setIdTransporte(idTransporteCreado);
        RespuestaCompuesta respuestaDetalleTransporte = dDetalleTransporte.guardarRegistro(eDetalleTransporte);
        if (respuestaDetalleTransporte.estado==false){     	
       	 throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
        }

        ClaveGenerada = respuestaDetalleTransporte.valor;
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper(); 
        ContenidoAuditoria =  mapper.writeValueAsString(eDetalleTransporte);
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
        eBitacora.setTabla(DetalleTransporteDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(ClaveGenerada);
        eBitacora.setContenido(ContenidoAuditoria);
        eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
        eBitacora.setRealizadoPor(principal.getID());
        RespuestaCompuesta respuestaBitacoraDetalleTransporte = dBitacora.guardarRegistro(eBitacora);
        if (respuestaBitacoraDetalleTransporte.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }
	}

}
