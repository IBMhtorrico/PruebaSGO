package sgo.servicio;

import java.io.File;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
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
import sgo.datos.ClienteDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EstacionDao;
import sgo.datos.OperacionDao;
import sgo.datos.ParametroDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.TanqueDao;
import sgo.datos.TransportistaOperacionDao;
import sgo.datos.UsuarioDao;
import sgo.datos.VigenciaDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.Parametro;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Planificacion;
import sgo.entidad.Producto;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Rol;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
import sgo.utilidades.MailNotifica;
import sgo.utilidades.Reporteador;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PlanificacionControlador {
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
 private ParametroDao dParametro;
 @Autowired
 private ClienteDao dCliente;
 @Autowired
 private OperacionDao dOperacion;
 @Autowired
 private ProductoDao dProducto;
 @Autowired
 private EstacionDao dEstacion;
 @Autowired
 private TanqueDao dTanque;
 @Autowired
 private TransportistaOperacionDao dTransportistaOperacion;
 @Autowired
 private MailNotifica dMailNotifica;
 @Autowired
 private UsuarioDao dUsuario;
 @Autowired
 private DiaOperativoControlador DiaOperativoControlador;
 @Autowired
 ServletContext servletContext;

 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 /** Nombre de la clase. */
 private static final String sNombreClase = "PlanificacionControlador";
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/planificacion";
 private static final String URL_GESTION_RELATIVA = "/planificacion";
 private static final String URL_GUARDAR_COMPLETA = "/admin/planificacion/crear";
 private static final String URL_GUARDAR_RELATIVA = "/planificacion/crear";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/planificacion/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/planificacion/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/planificacion/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/planificacion/recuperar";
 private static final String URL_ANULAR_COMPLETA = "/admin/planificacion/anular";
 private static final String URL_ANULAR_RELATIVA = "/planificacion/anular";
 private static final String URL_NOTIFICAR_COMPLETA = "/admin/planificacion/notificar";
 private static final String URL_NOTIFICAR_RELATIVA = "/planificacion/notificar";
 private static final String URL_NOTIFICAR_DETALLE_PLANIFICACIONES_RELATIVA = "/planificacion/notificarDetallePlanificaciones";
 private static final String URL_PROMEDIO_PRODUCTO_COMPLETA = "/admin/planificacion/recupera-promedio-producto";
 private static final String URL_PROMEDIO_PRODUCTO_RELATIVA = "/planificacion/recupera-promedio-producto";
 private static final String URL_VALIDA_OPERACION_COMPLETA = "/admin/planificacion/valida-operacion";
 private static final String URL_VALIDA_OPERACION_RELATIVA = "/planificacion/valida-operacion";
 private static final String URL_REPORTE_RELATIVA = "/planificacion/reporte";
 private static final String URL_RECUPERAR_PLANIFICACION_RELATIVA = "/planificacion/recuperarDetallePlanificacion";
 private static final String URL_RECUPERAR_PLANIFICACION_COMPLETA = "/admin/planificacion/recuperarDetallePlanificacion";

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

   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
   mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));
   mapaValores.put("MENSAJE_ENVIAR_CORREO", gestorDiccionario.getMessage("sgo.mensajeEnviarCorreo", null, locale));
   
   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
   mapaValores.put("MENSAJE_ANULAR_REGISTRO", gestorDiccionario.getMessage("sgo.mensajeAnularRegistro",null,locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));
  } catch (Exception ex) {
	  Utilidades.gestionaError(ex, sNombreClase, "recuperarMapaValores");
  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  ArrayList<?> listaOperaciones = null;
  ArrayList<?> listaProductos = null;

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
   
   Operacion opeTemp = (Operacion) respuesta.contenido.carga.get(0);
   int idOperacion = opeTemp.getId();
   String fecha = dDiaOperativo.recuperarFechaActual().valor;
   String fechaHoy = fecha;
   //esto para obtener el ultimo dia operativo 
   Respuesta oRespuesta = dDiaOperativo.recuperarUltimoDiaOperativo(idOperacion, "");
   // Verifica el resultado de la accion
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   if (oRespuesta.valor != null) {
	   fecha = oRespuesta.valor;
   }

   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   respuesta = dProducto.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
   }
   listaProductos = (ArrayList<?>) respuesta.contenido.carga;
   mapaValores = recuperarMapaValores(locale);
   vista = new ModelAndView("plantilla");
   vista.addObject("vistaJSP", "planificacion/planificacion.jsp");
   vista.addObject("vistaJS", "planificacion/planificacion.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("operaciones", listaOperaciones);
   vista.addObject("productos", listaProductos);
   vista.addObject("mapaValores", mapaValores);
   //vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
   //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 2
   vista.addObject("fechaActual", fecha);
   vista.addObject("fechaHoy", fechaHoy);
   vista.addObject("plantaDespacho", principal.getOperacion().getPlantaDespacho().getDescripcion());
   vista.addObject("eta", principal.getOperacion().getEta());
  } catch (Exception ex) {
	  //Utilidades.gestionaError(ex, sNombreClase, "mostrarFormulario");
  }
  return vista;
 }

 @RequestMapping(value = URL_RECUPERAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistro(int ID, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  DiaOperativo eDiaOperativo= null;
  Contenido<DiaOperativo> contenido=null;
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
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   eDiaOperativo = (DiaOperativo) respuesta.contenido.carga.get(0);

   ParametrosListar parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setCampoOrdenamiento("id");
   parametros.setSentidoOrdenamiento("asc");
   parametros.setFiltroDiaOperativo(eDiaOperativo.getId());

   respuesta = dPlanificacion.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   ArrayList<?> listaPlanificacion = (ArrayList<?>) respuesta.contenido.carga;
   //LA LISTA DE PLANIFICACIONES PUEDE ESTAR VACIA
   /*if (listaPlanificacion.size()==0) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }*/

   for(Object tPlanificacion : listaPlanificacion){
    eDiaOperativo.agregarPlanificacion((Planificacion)tPlanificacion);
   }

   contenido = new Contenido<DiaOperativo>();
   contenido.carga= new ArrayList<DiaOperativo>();
   contenido.carga.add(eDiaOperativo);   
   respuesta.contenido=contenido;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistro");
	//ex.printStackTrace();
	respuesta.estado=false;
	respuesta.contenido = null;
	respuesta.mensaje=ex.getMessage();
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
  Planificacion ePlanificacion = null;
  ObjectMapper mapper = null;
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dPlanificacion.getDataSource());
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
   
   for (int a = 0; a < eDiaOperativo.getPlanificaciones().size(); a++) {
		int cantidadProductos = 0;
		int idProd = eDiaOperativo.getPlanificaciones().get(a).getIdProducto();
		for (int b = 0; b < eDiaOperativo.getPlanificaciones().size(); b++) {
			if(idProd == eDiaOperativo.getPlanificaciones().get(b).getIdProducto()){
				cantidadProductos++;
			}
		}
		if(cantidadProductos > 1){
			RespuestaCompuesta producto = dProducto.recuperarRegistro(idProd);
			if (respuesta.estado==false){     	
             	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
           }
			Producto respuestaProducto = (Producto) producto.contenido.carga.get(0);
			throw new Exception("El producto " + respuestaProducto.getNombre() + " no puede repetirse. Favor verifique.");
		}
		
	}
   
   ParametrosListar parametros = new ParametrosListar();
   parametros.setFiltroFechaPlanificada(Utilidades.modificarFormatoFecha(eDiaOperativo.getFechaOperativa().toString()));
   parametros.setFiltroOperacion(eDiaOperativo.getIdOperacion());

   //esto es para saber cual es el último dia operativo
   Respuesta ultimoDiaOperativo = dDiaOperativo.recuperarUltimoDiaOperativo(eDiaOperativo.getIdOperacion(), "");
   if (ultimoDiaOperativo.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
  /* if(Utilidades.esValido(ultimoDiaOperativo.valor)){
	   Date ultimoDia = Utilidades.convierteStringADate(Utilidades.modificarFormatoFecha(ultimoDiaOperativo.valor));
	   
	   respuesta = dOperacion.recuperarRegistro(eDiaOperativo.getIdOperacion());
	   if (ultimoDiaOperativo.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   Operacion OpeTemporal = (Operacion) respuesta.contenido.carga.get(0);
	   
	   //esto para validar si existen dias operativos anteriores, si no existen inserto....
	   if(ultimoDia.compareTo(eDiaOperativo.getFechaOperativa()) < 0){
		   long diferencia = (eDiaOperativo.getFechaOperativa().getTime() - ultimoDia.getTime() );//MILLSECS_PER_DAY;
		   int dias = (int) Math.floor(diferencia / (1000 * 60 * 60 * 24));
		   for (int i = 1; i < dias+1; i++){
			   DiaOperativo diaTemporal = new DiaOperativo();
			   diaTemporal.setIdOperacion(eDiaOperativo.getIdOperacion());
			   diaTemporal.setFechaOperativa(Utilidades.sumarDias(i, ultimoDia));
			   diaTemporal.setFechaEstimadaCarga(Utilidades.restarDias(OpeTemporal.getEta(), diaTemporal.getFechaOperativa()));
			   diaTemporal.setEstado(DiaOperativo.ESTADO_PLANIFICADO);
			   diaTemporal.setCreadoEl(Calendar.getInstance().getTime().getTime());
			   diaTemporal.setCreadoPor(principal.getID());
			   diaTemporal.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			   diaTemporal.setActualizadoPor(principal.getID());
			   diaTemporal.setIpActualizacion(direccionIp);
			   diaTemporal.setIpCreacion(direccionIp);
	
			   respuesta = dDiaOperativo.guardarRegistro(diaTemporal);
			   if (respuesta.estado == false) {
			    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			   }
	
			   ClaveGenerada = respuesta.valor;
			   diaTemporal.setId(Integer.parseInt((ClaveGenerada)));
			   // Guardar en la bitacora
			   mapper = new ObjectMapper();
			   ContenidoAuditoria = mapper.writeValueAsString(diaTemporal);
			   eBitacora.setUsuario(principal.getNombre());
			   eBitacora.setAccion("/admin/dia_operativo/crear");
			   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
			   eBitacora.setIdentificador(ClaveGenerada);
			   eBitacora.setContenido(ContenidoAuditoria);
			   eBitacora.setRealizadoEl(eDiaOperativo.getCreadoEl());
			   eBitacora.setRealizadoPor(eDiaOperativo.getCreadoPor());
			   respuesta = dBitacora.guardarRegistro(eBitacora);
			   if (respuesta.estado == false) {
			    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
			   }
		   }
	   }
   }*/

   // Recuperar registros
   respuesta = dDiaOperativo.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   if(respuesta.contenido.carga.size() > 0){
	   DiaOperativo diaOpe = (DiaOperativo) respuesta.contenido.carga.get(0);
	   eDiaOperativo.setId(diaOpe.getId());
	   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
	   eDiaOperativo.setActualizadoPor(principal.getID());
	   eDiaOperativo.setIpActualizacion(direccionIp);

	   // Primero se actualiza el diaOperativo
	   eDiaOperativo.setEstado(DiaOperativo.ESTADO_PLANIFICADO);
	   respuesta = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
	   }
   } else {
	   eDiaOperativo.setEstado(DiaOperativo.ESTADO_PLANIFICADO);
	   eDiaOperativo.setCreadoEl(Calendar.getInstance().getTime().getTime());
	   eDiaOperativo.setCreadoPor(principal.getID());
	   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
	   eDiaOperativo.setActualizadoPor(principal.getID());
	   eDiaOperativo.setIpActualizacion(direccionIp);
	   eDiaOperativo.setIpCreacion(direccionIp);
	   
	   respuesta = dDiaOperativo.guardarRegistro(eDiaOperativo);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
	   }
	   ClaveGenerada = respuesta.valor;
	   eDiaOperativo.setId(Integer.parseInt((ClaveGenerada)));
	   // Guardar en la bitacora
	   mapper = new ObjectMapper();
	   ContenidoAuditoria = mapper.writeValueAsString(eDiaOperativo);
	   eBitacora.setUsuario(principal.getNombre());
	   eBitacora.setAccion("/admin/dia_operativo/crear");
	   eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
	   eBitacora.setIdentificador(ClaveGenerada);
	   eBitacora.setContenido(ContenidoAuditoria);
	   eBitacora.setRealizadoEl(eDiaOperativo.getCreadoEl());
	   eBitacora.setRealizadoPor(eDiaOperativo.getCreadoPor());
	   respuesta = dBitacora.guardarRegistro(eBitacora);
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
	   }
   }
// ahora el usuario pondrá la fecha de planificación y la fecha de carga
//   Respuesta diaOperativo = DiaOperativoControlador.recuperaUltimoDia(eDiaOperativo.getIdOperacion(), locale);
//   if (diaOperativo.estado == false) {
//    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
//   }
//
//   eDiaOperativo.setFechaOperativa(Utilidades.convierteStringADate(diaOperativo.valor));
//   int idDoperativo = Integer.parseInt(ClaveGenerada);

   Respuesta eRespuesta = dPlanificacion.eliminarRegistrosPorDiaOperativo(eDiaOperativo.getId());
   if (eRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   if(Utilidades.esValido(eDiaOperativo.getPlanificaciones())){
	   int numeroPlanificaciones = eDiaOperativo.getPlanificaciones().size();
	   for (int contador = 0; contador < numeroPlanificaciones; contador++) {
		String bitacora = "";
	    ePlanificacion = eDiaOperativo.getPlanificaciones().get(contador);
	    ePlanificacion.setIdDoperativo(eDiaOperativo.getId());
	    ePlanificacion.setActualizadoEl(eDiaOperativo.getActualizadoEl());
	    ePlanificacion.setActualizadoPor(eDiaOperativo.getActualizadoPor());
	    ePlanificacion.setIpActualizacion(eDiaOperativo.getIpActualizacion());
	    
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	    bitacora = format1.format(cal.getTime()) + " - " + principal.getIdentidad() + " - " + String.valueOf(ePlanificacion.getCantidadCisternas());
	    if(ePlanificacion.getObservacion().length() > 0){
	    	bitacora = bitacora + " - " + ePlanificacion.getObservacion();
	    } else {
	    	bitacora = bitacora + " - S/Obs.";
	    }
	    //bitacora = eDiaOperativo.getActualizadoEl()
	    ePlanificacion.setBitacora(bitacora);
	    
		//valida los datos que vienen del formulario
	    Respuesta validacion = Utilidades.validacionXSS(ePlanificacion, gestorDiccionario, locale);
	    if (validacion.estado == false) {
	      throw new Exception(validacion.valor);
	    }
	    respuesta = dPlanificacion.guardarRegistro(ePlanificacion);
	    if (respuesta.estado == false) {
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
	     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
	    }
	   }
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eDiaOperativo.getFechaActualizacion().substring(0, 9), eDiaOperativo.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);
  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
	//ex.printStackTrace();
	this.transaccion.rollback(estadoTransaccion);
	respuesta.estado=false;
	respuesta.contenido = null;
	respuesta.mensaje=ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ACTUALIZAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarRegistro(@RequestBody DiaOperativo eDiaOperativo, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
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
   
   //VALIDA QUE LOS PRODUCTOS NO SE REPITAN
   for (int a = 0; a < eDiaOperativo.getPlanificaciones().size(); a++) {
		int cantidadProductos = 0;
		int idProd = eDiaOperativo.getPlanificaciones().get(a).getIdProducto();
		for (int b = 0; b < eDiaOperativo.getPlanificaciones().size(); b++) {
			if(idProd == eDiaOperativo.getPlanificaciones().get(b).getIdProducto()){
				cantidadProductos++;
			}
		}
		if(cantidadProductos > 1){
			RespuestaCompuesta producto = dProducto.recuperarRegistro(idProd);
			if (respuesta.estado==false){     	
            	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
          }
			Producto respuestaProducto = (Producto) producto.contenido.carga.get(0);
			throw new Exception("El producto " + respuestaProducto.getNombre() + " no puede repetirse. Favor verifique.");
		}
		
	}

   respuesta = dDiaOperativo.recuperarRegistro(eDiaOperativo.getId());
   if (respuesta.estado == false) {
	   throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   DiaOperativo diaOperativoOriginal = (DiaOperativo) respuesta.getContenido().getCarga().get(0);
   if(diaOperativoOriginal.getEstado() == DiaOperativo.ESTADO_DESCARGANDO){
	   throw new Exception("No puede modificar una planificación en estado DESCARGANDO. Favor verifique.");
   }
   if(diaOperativoOriginal.getEstado() == DiaOperativo.ESTADO_LIQUIDADO){
	   throw new Exception("No puede modificar una planificación en estado LIQUIDADO. Favor verifique.");
   }
   if(diaOperativoOriginal.getEstado() == DiaOperativo.ESTADO_ANULADO){
	   throw new Exception("No puede modificar una planificación en estado ANULADO. Favor verifique.");
   }
   if(diaOperativoOriginal.getEstado() == DiaOperativo.ESTADO_CERRADO){
	   throw new Exception("No puede modificar una planificación en estado CERRADO. Favor verifique.");
   }
   
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);

   // Primero se actualiza el diaOperativo
   respuesta = dDiaOperativo.actualizarRegistro(eDiaOperativo);
   if (respuesta.estado == false) {
	   throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }

   for (int contador = 0; contador < eDiaOperativo.getPlanificaciones().size(); contador ++){
	   ePlanificacion = eDiaOperativo.getPlanificaciones().get(contador);
	   ePlanificacion.setIdDoperativo(eDiaOperativo.getId());
	   ePlanificacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
	   ePlanificacion.setActualizadoPor(principal.getID());
	   ePlanificacion.setIpActualizacion(direccionIp);

	   Respuesta validacion = Utilidades.validacionXSS(ePlanificacion, gestorDiccionario, locale);
	   if (validacion.estado == false) {
	     throw new Exception(validacion.valor);
	   }
	   
	   Calendar cal = Calendar.getInstance();
	   SimpleDateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    
	   if(Utilidades.esValido(ePlanificacion.getId()) && ePlanificacion.getId() > 0){
		   respuesta = dPlanificacion.recuperarRegistro(ePlanificacion.getId());
		   if (respuesta.estado == false) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
		   }
		   Planificacion planificacionOriginal = (Planificacion) respuesta.getContenido().getCarga().get(0);

		   //Agregado por obs 9000002608 se agrego validacion de volumenSolicitado===============================================================
		   if((!String.valueOf(ePlanificacion.getCantidadCisternas()).equals(String.valueOf(planificacionOriginal.getCantidadCisternas()))) || 
				   (!String.valueOf(ePlanificacion.getObservacion()).equals(String.valueOf(planificacionOriginal.getObservacion()))) ||
				   (!String.valueOf(ePlanificacion.getVolumenSolicitado()).equals(String.valueOf(planificacionOriginal.getVolumenSolicitado())))){
		   //==========================================================================================
		   //Comentado por obs 9000002608==============================================================
		   //Sólo se modificará la planificación si la cisterna o las observaciones son diferentes a la planificación original
//		   if((!String.valueOf(ePlanificacion.getCantidadCisternas()).equals(String.valueOf(planificacionOriginal.getCantidadCisternas()))) || 
//			   (!String.valueOf(ePlanificacion.getObservacion()).equals(String.valueOf(planificacionOriginal.getObservacion())))){
		   //=========================================================================================
			   ePlanificacion.setBitacora(planificacionOriginal.getBitacora());
	
			   ePlanificacion.setBitacora(planificacionOriginal.getBitacora() + " | " +  fechaHora.format(cal.getTime()) + " - " + principal.getIdentidad() + " - " + String.valueOf(ePlanificacion.getCantidadCisternas()));
			   if(ePlanificacion.getObservacion().length() > 0){
				   ePlanificacion.setBitacora(ePlanificacion.getBitacora() + " - " + ePlanificacion.getObservacion());
			   } else {
				   ePlanificacion.setBitacora(ePlanificacion.getBitacora() + " - S/Obs.");
			   }
			    
			    respuesta = dPlanificacion.actualizarRegistro(ePlanificacion);
			    if (respuesta.estado==false){     	
		          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
		        }
			    //Guardar en la bitacora
	            ObjectMapper mapper = new ObjectMapper();
	            eBitacora.setUsuario(principal.getNombre());
	            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
	            eBitacora.setTabla(PlanificacionDao.NOMBRE_TABLA);
	            eBitacora.setIdentificador(String.valueOf(ePlanificacion.getId()));
	            eBitacora.setContenido(mapper.writeValueAsString(ePlanificacion));
	            eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
	            eBitacora.setRealizadoPor(principal.getID());
		   }
	   } else{
		   ePlanificacion.setBitacora(fechaHora.format(cal.getTime()) + " - " + principal.getIdentidad() + " - " + String.valueOf(ePlanificacion.getCantidadCisternas()));
		   if(ePlanificacion.getObservacion().length() > 0){
			   ePlanificacion.setBitacora(ePlanificacion.getBitacora() + " - " + ePlanificacion.getObservacion());
		   } else {
			   ePlanificacion.setBitacora(ePlanificacion.getBitacora() + " - S/Obs.");
		   }
		   respuesta = dPlanificacion.guardarRegistro(ePlanificacion);
		   if (respuesta.estado == false) {
		       throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
		   }
		   ClaveGenerada = respuesta.valor;
		   // Guardar en la bitacora
		   ObjectMapper mapper = new ObjectMapper();
		   ContenidoAuditoria = mapper.writeValueAsString(ePlanificacion);
		   eBitacora.setUsuario(principal.getNombre());
		   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
		   eBitacora.setTabla(PlanificacionDao.NOMBRE_TABLA);
		   eBitacora.setIdentificador(ClaveGenerada);
		   eBitacora.setContenido(ContenidoAuditoria);
		   eBitacora.setRealizadoEl(ePlanificacion.getActualizadoEl());
		   eBitacora.setRealizadoPor(ePlanificacion.getActualizadoPor());
		   respuesta = dBitacora.guardarRegistro(eBitacora);
		   if (respuesta.estado == false) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
		   }
	   }
   }
   
/*
 *  Respuesta eRespuesta = dPlanificacion.eliminarRegistrosPorDiaOperativo(eDiaOperativo.getId());
   if (eRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   
   for (int contador = 0; contador < eDiaOperativo.getPlanificaciones().size(); contador++) {
    ePlanificacion = eDiaOperativo.getPlanificaciones().get(contador);
    ePlanificacion.setIdDoperativo(eDiaOperativo.getId());
    ePlanificacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
    ePlanificacion.setActualizadoPor(principal.getID());
    ePlanificacion.setIpActualizacion(direccionIp);
    //valida los datos que vienen del formulario
    Respuesta validacion = Utilidades.validacionXSS(ePlanificacion, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
    respuesta = dPlanificacion.guardarRegistro(ePlanificacion);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }

    ClaveGenerada = respuesta.valor;
    // Guardar en la bitacora
    ObjectMapper mapper = new ObjectMapper();
    ContenidoAuditoria = mapper.writeValueAsString(ePlanificacion);
    eBitacora.setUsuario(principal.getNombre());
    eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
    eBitacora.setTabla(PlanificacionDao.NOMBRE_TABLA);
    eBitacora.setIdentificador(ClaveGenerada);
    eBitacora.setContenido(ContenidoAuditoria);
    eBitacora.setRealizadoEl(ePlanificacion.getActualizadoEl());
    eBitacora.setRealizadoPor(ePlanificacion.getActualizadoPor());
    respuesta = dBitacora.guardarRegistro(eBitacora);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    }
   }*/
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eDiaOperativo.getFechaActualizacion().substring(0, 9),
		   eDiaOperativo.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);

  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "actualizarRegistro");
	//ex.printStackTrace();
	this.transaccion.rollback(estadoTransaccion);
	respuesta.estado=false;
	respuesta.contenido = null;
	respuesta.mensaje=ex.getMessage();
  }
  return respuesta;
 }

 @RequestMapping(value = URL_ANULAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta anularRegistro(@RequestBody DiaOperativo eDiaOperativo, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
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
   respuesta = dEnlace.recuperarRegistro(URL_ANULAR_COMPLETA);
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

   RespuestaCompuesta diaOperativo = dDiaOperativo.recuperarRegistro(eDiaOperativo.getId());
   if (diaOperativo.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   eDiaOperativo = (DiaOperativo) diaOperativo.contenido.carga.get(0);
   if(eDiaOperativo.getEstado() == 6){
	   //error porque el estado del registro es anulado
	   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.noPuedeAnularRegistroAnulado",  new Object[] { eDiaOperativo.getFechaOperativa().toString() } , locale));
   }
   
   //Primero hacemos las validaciones
   ParametrosListar parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setCampoOrdenamiento("fecha_operativa");
   parametros.setSentidoOrdenamiento("desc");
   parametros.setFiltroFechaDiaOperativo(eDiaOperativo.getFechaOperativa().toString());
   parametros.setIdOperacion(eDiaOperativo.getIdOperacion());

   RespuestaCompuesta respuestaValidacion = dDiaOperativo.validaRegistrosExistentes(parametros);
   if (respuestaValidacion.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   for(int i = 0; i < respuestaValidacion.contenido.carga.size(); i++){
	   DiaOperativo entidad = (DiaOperativo) respuestaValidacion.contenido.carga.get(i);
	   if(!entidad.getFechaOperativa().equals(eDiaOperativo.getFechaOperativa())){
		   if(entidad.getEstado() != 6){
			   //error porque el estado es diferente de anulado
			   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.noPuedeAnular",  new Object[] { eDiaOperativo.getFechaOperativa().toString() } , locale));
		   }
	   }
	   else{
		   if((entidad.getEstado() != 1) && (entidad.getEstado() != 6)) {
		   //error porque el estado es diferente de palnificado y de anulado
		   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.noPuedeAnular",  new Object[] { eDiaOperativo.getFechaOperativa().toString() } , locale));
		   }
	   }
   }

   //Actualizamos el estado del diaOperativo
   eDiaOperativo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eDiaOperativo.setActualizadoPor(principal.getID());
   eDiaOperativo.setIpActualizacion(direccionIp);
   eDiaOperativo.setEstado(DiaOperativo.ESTADO_ANULADO);

   respuesta = dDiaOperativo.ActualizarEstadoRegistro(eDiaOperativo);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   //Esto elimina las planificaciones del día operativo
   Respuesta eRespuesta = dPlanificacion.eliminarRegistrosPorDiaOperativo(eDiaOperativo.getId());
   	  if (eRespuesta.estado == false) {
	  throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   	  
   	ClaveGenerada = respuesta.valor;
    // Guardar en la bitacora
    ObjectMapper mapper = new ObjectMapper();
    ContenidoAuditoria = mapper.writeValueAsString(ePlanificacion);
    eBitacora.setUsuario(principal.getNombre());
    eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
    eBitacora.setTabla(DiaOperativoDao.NOMBRE_TABLA);
    eBitacora.setIdentificador(ClaveGenerada);
    eBitacora.setContenido(ContenidoAuditoria);
    respuesta = dBitacora.guardarRegistro(eBitacora);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eDiaOperativo.getFechaActualizacion().substring(0, 9), eDiaOperativo.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
   this.transaccion.commit(estadoTransaccion);

  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "anularRegistro");
	//ex.printStackTrace();
	this.transaccion.rollback(estadoTransaccion);
	respuesta.estado=false;
	respuesta.contenido = null;
	respuesta.mensaje=ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_NOTIFICAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody Respuesta enviarMail(HttpServletRequest httpRequest, Locale locale) {
  Respuesta respuesta = new Respuesta();
  RespuestaCompuesta oRespuesta = null;
  AuthenticatedUserDetails principal = null;
  ParametrosListar parametros = null;
  ArrayList<String> para = new ArrayList<String>();
  ArrayList<String> cc = new ArrayList<String>();
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_NOTIFICAR_COMPLETA);
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   // Recuperar parametros
   parametros = new ParametrosListar();
   if (httpRequest.getParameter("filtroMailPara") != null) {
    parametros.setFiltroMailPara((httpRequest.getParameter("filtroMailPara")));
   }

   if (httpRequest.getParameter("filtroMailCC") != null) {
    parametros.setFiltroMailCC((httpRequest.getParameter("filtroMailCC")));
   }
   
   if (httpRequest.getParameter("filtroFechaDiaOperativo") != null) {
    parametros.setFiltroFechaDiaOperativo((httpRequest.getParameter("filtroFechaDiaOperativo")));
   } 
   
   if (httpRequest.getParameter("filtroFechaCarga") != null) {
    parametros.setFiltroFechaCarga((httpRequest.getParameter("filtroFechaCarga")));
   } 
   
   if (httpRequest.getParameter("filtroNombreCliente") != null) {
    parametros.setFiltroNombreCliente((httpRequest.getParameter("filtroNombreCliente")));
   } 
   
   if (httpRequest.getParameter("filtroNombreOperacion") != null) {
    parametros.setFiltroNombreOperacion((httpRequest.getParameter("filtroNombreOperacion")));
   } 
   
   if (httpRequest.getParameter("filtroCisterna") != null) {
    parametros.setFiltroCisterna(Integer.parseInt(httpRequest.getParameter("filtroCisterna")));
   } 
   
   if (httpRequest.getParameter("filtroDiaOperativo") != null) {
    parametros.setFiltroDiaOperativo(Integer.parseInt(httpRequest.getParameter("filtroDiaOperativo")));
   } 
   
   //el filtro Operacion es para la busqueda de los usuarios
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }

   if(parametros.getFiltroCisterna() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.NoHayCisternasPlanificadas", null, locale));
   }
   
   //valida correos
 //esto para armar el arraylist de los correos PARA
   if(!parametros.getFiltroMailPara().trim().isEmpty()){
	   String[] temp = parametros.getFiltroMailPara().split(";");
	   for (String correoPara : temp) {
			if(correoPara.trim().length() > 0){
				para.add(correoPara.trim());
			}
	   }
	   //Valido los correos PARA
	   if(!Utilidades.validaEmail(para)){
		   throw new Exception("El campo Para es incorrecto. Favor verifique.");
	   }
   } else {
		throw new Exception("El campo Para se encuentra vacío. Favor verifique.");
   }
   
   //esto para armar el arraylist de los correos CC
   if(!parametros.getFiltroMailCC().trim().isEmpty()){
		String[] temp = parametros.getFiltroMailCC().split(";");
		for (String correoCC : temp) {
			if(correoCC.trim().length() > 0){
				cc.add(correoCC.trim());
			}
	    }
		//Valido los correos CC
		if(!Utilidades.validaEmail(cc)){
		   throw new Exception("El campo CC es incorrecto. Favor verifique.");
		}
	}
   
   ParametrosListar parametrosUsuario = new ParametrosListar();
   parametrosUsuario.setFiltroEstado(Constante.ESTADO_ACTIVO);
   parametrosUsuario.setFiltroRol(Rol.ROL_TRANSPORTISTA);
   parametrosUsuario.setTxtQuery("select id_transportista from sgo.transportista_operacion where id_operacion = " + parametros.getFiltroOperacion());

   //para buscar los usuarios con rol trasportista
//   RespuestaCompuesta respuestaUsuarioTransportista = dUsuario.recuperarRegistros(parametrosUsuario);
   
   ParametrosListar parametrosTransportista = new ParametrosListar();
   parametrosTransportista.setIdOperacion(parametros.getFiltroOperacion());
   
   RespuestaCompuesta respuestaUsuarioTransportista = dTransportistaOperacion.recuperarRegistros(parametrosTransportista);
   
   if (respuestaUsuarioTransportista.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   //para buscar los usuarios con rol Modulo Transporte
   parametrosUsuario.setFiltroRol(Rol.ROL_MODULO_TRANSPORTE);
   parametrosUsuario.setTxtQuery("");
   RespuestaCompuesta respuestaUsuarioModuloTransportista = dUsuario.recuperarRegistros(parametrosUsuario);
   if (respuestaUsuarioTransportista.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   ParametrosListar parametrosPlanificacion = new ParametrosListar();
   parametrosPlanificacion.setFiltroDiaOperativo(parametros.getFiltroDiaOperativo());
   RespuestaCompuesta planificaciones = dPlanificacion.recuperarRegistros(parametrosPlanificacion);
   if (respuestaUsuarioTransportista.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   //enviamos los parametros del correo, el listado de usuarios con rol transportista y el listado de usuarioscon rol modulo transporte
   respuesta.estado = dMailNotifica.enviarMailModuloPlanificacion(parametros, respuestaUsuarioTransportista, respuestaUsuarioModuloTransportista, planificaciones);
   if(respuesta.estado){
	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionExitosa", null, locale);
   }
   else{
	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
   }
  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "enviarMail");
	//ex.printStackTrace();
	respuesta.estado=false;
	if(!Utilidades.esValido(ex.getMessage())){
		respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
	} else {
		respuesta.mensaje=ex.getMessage();
	}
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_NOTIFICAR_DETALLE_PLANIFICACIONES_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody Respuesta enviarMailDetallePlanificaciones(HttpServletRequest httpRequest, Locale locale) {
  Respuesta respuesta = new Respuesta();
  RespuestaCompuesta oRespuesta = null;
  AuthenticatedUserDetails principal = null;
  ParametrosListar parametros = null;
  ArrayList<String> para = new ArrayList<String>();
  ArrayList<String> cc = new ArrayList<String>();
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_NOTIFICAR_COMPLETA);
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   // Recuperar parametros
   parametros = new ParametrosListar();
   if (httpRequest.getParameter("filtroMailPara") != null) {
    parametros.setFiltroMailPara((httpRequest.getParameter("filtroMailPara")));
   }

   if (httpRequest.getParameter("filtroMailCC") != null) {
    parametros.setFiltroMailCC((httpRequest.getParameter("filtroMailCC")));
   }
   
   if (httpRequest.getParameter("filtroFechaInicio") != null) {
    parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
   } 
   
   if (httpRequest.getParameter("filtroFechaFinal") != null) {
    parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
   } 
   
   if (httpRequest.getParameter("filtroEta") != null) {
    parametros.setFiltroEta(Integer.parseInt(httpRequest.getParameter("filtroEta")));
   }
   
   //el filtro Operacion es para la busqueda de los usuarios
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }
   
   if (httpRequest.getParameter("filtroNombreOperacion") != null) {
    parametros.setFiltroNombreOperacion((httpRequest.getParameter("filtroNombreOperacion")));
   } 
   
   if (httpRequest.getParameter("filtroNombreCliente") != null) {
    parametros.setFiltroNombreCliente((httpRequest.getParameter("filtroNombreCliente")));
   } 
   
 //valida correos
   //esto para armar el arraylist de los correos PARA
     if(!parametros.getFiltroMailPara().trim().isEmpty()){
  	   String[] temp = parametros.getFiltroMailPara().split(";");
  	   for (String correoPara : temp) {
  			if(correoPara.trim().length() > 0){
  				para.add(correoPara.trim());
  			}
  	   }
  	   //Valido los correos PARA
  	   if(!Utilidades.validaEmail(para)){
  		   throw new Exception("El campo Para es incorrecto. Favor verifique.");
  	   }
     } else {
  		throw new Exception("El campo Para se encuentra vacío. Favor verifique.");
     }
     
     //esto para armar el arraylist de los correos CC
     if(!parametros.getFiltroMailCC().trim().isEmpty()){
  		String[] temp = parametros.getFiltroMailCC().split(";");
  		for (String correoCC : temp) {
  			if(correoCC.trim().length() > 0){
  				cc.add(correoCC.trim());
  			}
  	    }
  		//Valido los correos CC
  		if(!Utilidades.validaEmail(cc)){
  		   throw new Exception("El campo CC es incorrecto. Favor verifique.");
  		}
  	}
     
   ParametrosListar parametrosUsuario = new ParametrosListar();
   parametrosUsuario.setFiltroEstado(Constante.ESTADO_ACTIVO);
   parametrosUsuario.setFiltroRol(Rol.ROL_TRANSPORTISTA);
   parametrosUsuario.setTxtQuery("select id_transportista from sgo.transportista_operacion where id_operacion = " + parametros.getFiltroOperacion());

   //para buscar los usuarios con rol trasportista
//   RespuestaCompuesta respuestaUsuarioTransportista = dUsuario.recuperarRegistros(parametrosUsuario);
   
   ParametrosListar parametrosTransportista = new ParametrosListar();
   parametrosTransportista.setIdOperacion(parametros.getFiltroOperacion());
   
   RespuestaCompuesta respuestaUsuarioTransportista = dTransportistaOperacion.recuperarRegistros(parametrosTransportista);
   
   if (respuestaUsuarioTransportista.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   //para buscar los usuarios con rol Modulo Transporte
   parametrosUsuario.setFiltroRol(Rol.ROL_MODULO_TRANSPORTE);
   parametrosUsuario.setTxtQuery("");
   RespuestaCompuesta respuestaUsuarioModuloTransportista = dUsuario.recuperarRegistros(parametrosUsuario);
   if (respuestaUsuarioTransportista.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   //enviamos los parametros del correo, el listado de usuarios con rol transportista y el listado de usuarioscon rol modulo transporte
   //respuesta.estado = dMailNotifica.enviarMailDetallePlanificaciones(parametros, respuestaUsuarioTransportista, respuestaUsuarioModuloTransportista);
   ArrayList<String> lfiles=new ArrayList<String>();  
   //inicio lectura los archivos
   ParametrosListar paramListar= new ParametrosListar();   
   paramListar.setFiltroParametro(Parametro.ALIAS_DIRECTORIO_ARCHIVOS);   
   oRespuesta=dParametro.recuperarRegistros(paramListar);
   if (oRespuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   Parametro eParametro = (Parametro)  oRespuesta.contenido.getCarga().get(0);
   if(eParametro==null || eParametro.getValor()==null || eParametro.getValor().length()==0){
	   throw new Exception("Falta configurar el directorio");
	   
   }else{
	   File validaDirectorio=new File(eParametro.getValor().toLowerCase());
	   if(!validaDirectorio.isDirectory()){
		   String mensaje=gestorDiccionario.getMessage("sgo.directorioArchivo.noExiste", null, locale)+ ": "+ eParametro.getValor().toLowerCase();
		   throw new Exception(mensaje);
	   }
   }   
   String directorio=eParametro.getValor().toLowerCase();   
  
   String path_archivo=null;	    
   path_archivo=generarArchivo(parametros,locale,directorio);
	   
    if (path_archivo != null && !path_archivo.isEmpty()) {
    	 File file = new File(path_archivo);
    	 path_archivo=file.getAbsolutePath();	
    } else {//error al generar archivo
		   throw new Exception("Error al generar el archivo adjunto.");
    }
   
    lfiles.add(path_archivo);
   //fin lectura archivo   
   respuesta.estado = dMailNotifica.enviarMailDetallePlanificaciones(parametros, respuestaUsuarioTransportista, respuestaUsuarioModuloTransportista, lfiles);
   
   if(respuesta.estado){
	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionExitosa", null, locale);
   }
   else{
	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
   }
  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "enviarMailDetallePlanificaciones");
	//ex.printStackTrace();
	respuesta.estado=false;
	//respuesta.mensaje=ex.getMessage();
	if(!Utilidades.esValido(ex.getMessage())){
		respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
	} else {
		respuesta.mensaje=ex.getMessage();
	}
  }
  return respuesta;
 }

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
 
 @RequestMapping(value = URL_PROMEDIO_PRODUCTO_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody Respuesta recuperaPromedioDescargaPorProducto(HttpServletRequest httpRequest, Locale locale) {
  Respuesta respuesta = null;
  RespuestaCompuesta oRespuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_PROMEDIO_PRODUCTO_COMPLETA);
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
   // Recuperar parametros
   parametros = new ParametrosListar();
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }
   
   if (httpRequest.getParameter("filtroProducto") != null) {
    parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
   }

   if (httpRequest.getParameter("filtroFechaFinal") != null) {
    parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
   }

   if (httpRequest.getParameter("filtroFechaInicio") != null) {
    parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
   }

   // Recuperar registros
   respuesta = dPlanificacion.recuperaPromedioDescargaPorProducto(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
     Utilidades.gestionaError(ex, sNombreClase, "recuperaPromedioDescargaPorProducto");
     respuesta.estado = false;
     respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_VALIDA_OPERACION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 Respuesta validacionAntesDeAgregar(int idOperacion, Locale locale) {
  Respuesta respuesta = new Respuesta();
  RespuestaCompuesta oRespuesta = null;
  AuthenticatedUserDetails principal = null;
  ParametrosListar parametros = null;
  try {
   // Recupera el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   oRespuesta = dEnlace.recuperarRegistro(URL_VALIDA_OPERACION_COMPLETA);
   if (oRespuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }

   //valido operacion
   oRespuesta = dOperacion.recuperarRegistro(idOperacion);
   if (oRespuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   Operacion eOperacion = (Operacion) oRespuesta.contenido.carga.get(0);
   if(!Utilidades.esValido(eOperacion.getIdPlantaDespacho()) || eOperacion.getIdPlantaDespacho() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.errorDatosOperacionPlanta", null, locale));
   }
   if(!Utilidades.esValido(eOperacion.getEta()) || eOperacion.getEta() < 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.errorDatosOperacionETA", null, locale));
   }
   
   //valido estacion
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setCampoOrdenamiento("id");
   parametros.setSentidoOrdenamiento("asc");
   parametros.setFiltroOperacion(idOperacion);
   
   oRespuesta = dEstacion.recuperarRegistros(parametros);
   if (oRespuesta.estado==false){
	   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if(oRespuesta.contenido.carga.size() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.operacionNoEstaciones", null, locale));
   }
   
   //valido tanques
   oRespuesta = dTanque.recuperarRegistros(parametros);
   if (oRespuesta.estado==false){
	   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if(oRespuesta.contenido.carga.size() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.errorOperacionSinTanques", null, locale));
   }
   
   //valido productos
   oRespuesta = dProducto.recuperarRegistrosPorOperacion(parametros);
   if (oRespuesta.estado==false){
	   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if(oRespuesta.contenido.carga.size() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.errorOperacionSinProductos", null, locale));
   }
   
  /* //valido transportistas
   oRespuesta = dTransportistaOperacion.recuperarRegistros(parametros);
   if (oRespuesta.estado==false){
	   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if(oRespuesta.contenido.carga.size() == 0){
	   throw new Exception(gestorDiccionario.getMessage("sgo.errorOperacionSinTransportista", null, locale));
   }
   for(int i = 0; i < oRespuesta.contenido.carga.size(); i++){
	   TransportistaOperacion eTransportistaOperacion = (TransportistaOperacion) oRespuesta.contenido.carga.get(i);
	 //valido usuarios para transportista
	   parametros.setFiltroRol(Rol.ROL_TRANSPORTISTA);
	   parametros.setIdTransportista(eTransportistaOperacion.geteTransportista().getId());
	   RespuestaCompuesta usuario = dUsuario.recuperarRegistros(parametros);
	   if (usuario.estado==false){
		   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   if(usuario.contenido.carga.size() == 0){
		   throw new Exception(gestorDiccionario.getMessage("sgo.errorOperacionSinUsuarioTransportista", new Object[] { eTransportistaOperacion.geteTransportista().getRazonSocial().toString() }, locale));
	   }
	   for(int k = 0; k < usuario.contenido.carga.size(); k++){
		   Usuario eUsuario = (Usuario) usuario.contenido.carga.get(k);
		   if(!Utilidades.esValido(eUsuario.getEmail())){
			   throw new Exception(gestorDiccionario.getMessage("sgo.errorUsuarioTransportistaSinCorreo", new Object[] { eUsuario.getIdentidad().toString(), eTransportistaOperacion.geteTransportista().getRazonSocial().toString() }, locale));
		   }

	   }
   }*/
   respuesta.estado = true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.validacionesCorrectas", null, locale);
  } catch (Exception ex) {
	Utilidades.gestionaError(ex, sNombreClase, "recuperaPromedioDescargaPorProducto");
	//ex.printStackTrace();
	respuesta.estado=false;
	respuesta.mensaje=ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_RECUPERAR_PLANIFICACION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody RespuestaCompuesta recuperaDetallePlanificaciones(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_PLANIFICACION_COMPLETA);
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
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }

   if (httpRequest.getParameter("filtroFechaFinal") != null) {
    parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
   }

   if (httpRequest.getParameter("filtroFechaInicio") != null) {
    parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
   }
   
   if (httpRequest.getParameter("filtroEta") != null) {
    parametros.setFiltroEta(Integer.parseInt(httpRequest.getParameter("filtroEta")));
   }
   
   parametros.setPaginacion(Constante.SIN_PAGINACION);

   int diferencia = Utilidades.diferenciaEnDias2(Utilidades.convierteStringADate(parametros.getFiltroFechaFinal(),"yyyy-MM-dd"), Utilidades.convierteStringADate(parametros.getFiltroFechaInicio(),"yyyy-MM-dd"));
   
   ParametrosListar paramListar= new ParametrosListar();   
   paramListar.setFiltroParametro(Parametro.ALIAS_PERIODO_NOTIFICACION);   
   respuesta = dParametro.recuperarRegistros(paramListar);
   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }

   Parametro eParametro = (Parametro)  respuesta.contenido.getCarga().get(0);
   if(diferencia > Integer.parseInt(eParametro.getValor())){
	   throw new Exception(gestorDiccionario.getMessage("sgo.planificacion.rangoFechas", new Object[] { eParametro.getValor() } , locale));
   }  
		   
   // Recuperar registros
   respuesta = dPlanificacion.recuperarRegistrosReporte(parametros);
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje= ex.getMessage();
   Utilidades.gestionaError(ex, sNombreClase, "recuperaDetallePlanificaciones");
  }
  return respuesta;
 }
 
 
 public String generarArchivo(ParametrosListar parametros,Locale locale,String directorio_archivo){
	 String path_archivo=null;
	 //ParametrosListar parametros = new ParametrosListar();	   
	 //parametros.setFiltroDiaOperativo(idDiaOperativo);
	  RespuestaCompuesta respuesta=null;
	  String mensajeRespuesta = "";
	   try{	   
		   respuesta = dPlanificacion.recuperarRegistrosReporte(parametros);
		   if (respuesta.estado==false){
		    mensajeRespuesta = gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale);
		    throw new Exception(mensajeRespuesta);
		   }		    
			   Planificacion planificacion =null;
			   HashMap<String,Object> hmValor = null;
			   ArrayList<HashMap<?,?>> hmRegistros = null;
			   hmRegistros= new  ArrayList<HashMap<?,?>>();
			   ArrayList<?> elementos =(ArrayList<?>) respuesta.contenido.getCarga();
			   
			   String fecha_operativa = "",fecha_carga="";
			   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			   
			   if(elementos.size()>0){
				   for (int indice=0; indice <elementos.size();indice++){
					planificacion= (Planificacion) elementos.get(indice);
				    hmValor= new HashMap<String,Object>();
				    Date fechaOperativa=planificacion.getDiaOperativo().getFechaOperativa();
				    Date fechaCarga=planificacion.getDiaOperativo().getFechaEstimadaCarga();
				    fecha_operativa = sdf.format(fechaOperativa);
				    fecha_carga = sdf.format(fechaCarga);
				    hmValor.put("fecha_operativa", fecha_operativa);
				    hmValor.put("fecha_carga", fecha_carga);
				    hmValor.put("nombre_producto", planificacion.getProducto().getNombre());
				    /* Comentado por req 9000002608
				    hmValor.put("cantidad_cisternas", planificacion.getCantidadCisternas());	
				    */
				    //Agregado por req 9000002608======================================================
					NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
					DecimalFormat df = (DecimalFormat)nf;
				    hmValor.put("volumen_cisternas", df.format(planificacion.getVolumenSolicitado()));
				    //=======================================================================
				    hmValor.put("observacion", planificacion.getObservacion());	
				    hmValor.put("bitacora", planificacion.getBitacora());	
				    hmRegistros.add(hmValor);
				   }	   
					   String tituloReporte= planificacion.getDiaOperativo().getOperacion().getCliente().getRazonSocial()+
							   " - OPERACIÓN:" + planificacion.getDiaOperativo().getOperacion().getNombre();
					 
					   Reporteador uReporteador = new Reporteador();
					   uReporteador.setRutaServlet(servletContext.getRealPath("/"));
					   ArrayList<Campo> listaCampos = this.generarCamposPlanificacion();				   
					   String rutaPlantilla="";				   
					   String fileName = "";				      
						   rutaPlantilla= servletContext.getRealPath("/")+"pages"+File.separator+"reportes"+File.separator+"planificacion_dia.xls";					   
						   fileName="Plan_"+planificacion.getDiaOperativo().getOperacion().getCliente().getNombreCorto().trim().toLowerCase()
								   +planificacion.getDiaOperativo().getOperacion().getNombre()+".xls";		  
						   path_archivo=uReporteador.generarReportePlantillaListadoExcel(rutaPlantilla ,
								   hmRegistros,listaCampos,tituloReporte,
								   directorio_archivo,
								   fileName
								   ,3);						
				   
			   }
			   

			   
		   
	   }catch(Exception ex){
		   //ex.printStackTrace();
		   Utilidades.gestionaError(ex, sNombreClase, "generarArchivo");
	   }
	 return path_archivo;
 }
 
 private ArrayList<Campo> generarCamposPlanificacion() {
  ArrayList<Campo> listaCampos = null;
  try {
   listaCampos = new ArrayList<Campo>();
   Campo eCampo = null;
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Fecha Planificada");
   eCampo.setNombre("fecha_operativa");
   eCampo.setTipo(Campo.TIPO_TEXTO);	   
   listaCampos.add(eCampo);
	   
   eCampo = new Campo();
   eCampo.setEtiqueta("Fecha Carga");
   eCampo.setNombre("fecha_carga");
   eCampo.setTipo(Campo.TIPO_TEXTO);	   
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Producto");
   eCampo.setNombre("nombre_producto");
   eCampo.setTipo(Campo.TIPO_TEXTO);	   
   listaCampos.add(eCampo);  
   
   /* Comentado por 9000002608
   eCampo = new Campo();
   eCampo.setEtiqueta("Cantidad de Cisternas");
   eCampo.setNombre("cantidad_cisternas");
   eCampo.setTipo(Campo.TIPO_ENTERO);	   
   listaCampos.add(eCampo);
   */
   
   //Agregado por 9000002608================================
   eCampo = new Campo();
   eCampo.setEtiqueta("Volumen Cisternas");
   eCampo.setNombre("volumen_cisternas");
   eCampo.setTipo(Campo.TIPO_TEXTO);	   
   listaCampos.add(eCampo);
   //========================================================
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Observación");
   eCampo.setNombre("observacion");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   listaCampos.add(eCampo);
   
   eCampo = new Campo();
   eCampo.setEtiqueta("Comentario");
   eCampo.setNombre("bitacora");
   eCampo.setTipo(Campo.TIPO_TEXTO);
   listaCampos.add(eCampo);
   
  } catch (Exception ex) {
	  Utilidades.gestionaError(ex, sNombreClase, "generarCamposPlanificacion");
  }
  return listaCampos;
 }
}
