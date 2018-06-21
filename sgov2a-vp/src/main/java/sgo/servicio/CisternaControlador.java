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

import sgo.datos.AforoCisternaDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.DescargaCompartimentoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.TransporteDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Cisterna;
import sgo.entidad.Compartimento;
import sgo.entidad.Contenido;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class CisternaControlador {
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
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/cisterna";
 private static final String URL_GESTION_RELATIVA = "/cisterna";
 private static final String URL_GUARDAR_COMPLETA = "/admin/cisterna/crear";
 private static final String URL_GUARDAR_RELATIVA = "/cisterna/crear";
 private static final String URL_LISTAR_COMPLETA = "/admin/cisterna/listar";
 private static final String URL_LISTAR_RELATIVA = "/cisterna/listar";
 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/cisterna/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/cisterna/actualizar";
 private static final String URL_RECUPERAR_COMPLETA = "/admin/cisterna/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/cisterna/recuperar";
 private static final String URL_RECUPERAR_POR_TRANSPORTISTA_COMPLETA = "/admin/cisterna/recuperarPorTransportista";
 private static final String URL_RECUPERAR_POR_TRANSPORTISTA_RELATIVA = "/cisterna/recuperarPorTransportista";
 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/cisterna/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/cisterna/actualizarEstado";
 


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
   vista.addObject("vistaJSP", "mantenimiento/cisterna.jsp");
   vista.addObject("vistaJS", "mantenimiento/cisterna.js");
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

   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroEstado") != null) {
    parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
   }
   
   if (httpRequest.getParameter("filtroTracto") != null) {
    parametros.setFiltroTracto(Integer.parseInt(httpRequest.getParameter("filtroTracto")));
   }
   
   if (httpRequest.getParameter("idTransportista") != null) {
    parametros.setIdTransportista(Integer.parseInt(httpRequest.getParameter("idTransportista")));
   }

   // Recuperar registros
   respuesta = dCisterna.recuperarRegistros(parametros);
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
  Cisterna eCisterna = null;
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
   respuesta = dCisterna.recuperarRegistro(ID);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   eCisterna = (Cisterna) respuesta.contenido.carga.get(0);
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroCisterna(eCisterna.getId());
   parametros.setCampoOrdenamiento(CompartimentoDao.CAMPO_NUMERO_COMPARTIMENTO);
   parametros.setSentidoOrdenamiento("ASC");

   respuesta = dCompartimento.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   
   for(Object elemento: respuesta.contenido.carga){
	   Compartimento comp = (Compartimento) elemento;
	   
	   parametros = new ParametrosListar();
	   parametros.setPaginacion(Constante.SIN_PAGINACION);
	   parametros.setFiltroCisterna(comp.getIdCisterna());
	   parametros.setFiltroCompartimento(comp.getId());
	   parametros.setSentidoOrdenamiento("ASC");
	   RespuestaCompuesta respuestaAforo= respuesta = dAforoCisterna.recuperarRegistros(parametros);
	   
	   if (respuestaAforo.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   
	   if(respuestaAforo.contenido.carga.size() > 0){
		   comp.setCantidadAforos(respuestaAforo.contenido.carga.size());
	   }
	   else{
		   comp.setCantidadAforos(0);
	   }
	   
	   //esto para verificar si hay datos hostoricos en descarga
	   
	 //valido si el registro no está en datos históricos
	   parametros = new ParametrosListar();
	   parametros.setIdCisterna(eCisterna.getId());
	   parametros.setFiltroCompartimento(comp.getIdentificador());
	   
	   respuesta = dDescargaCompartimento.recuperarRegistros(parametros);
	   // Verifica si la accion se ejecuto de forma satisfactoria
	   if (respuesta.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
		   
	   if(respuesta.contenido.carga.size() > 0){
		 comp.setDescargas(respuesta.contenido.carga.size());
	   }
	   else{
		 comp.setDescargas(0);
	   }

	   eCisterna.agregarCompartimento(comp);
   }

   Contenido<Cisterna> contenido = new Contenido<Cisterna>();
   contenido.carga = new ArrayList<Cisterna>();
   contenido.carga.add(eCisterna);
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

 @RequestMapping(value = URL_RECUPERAR_POR_TRANSPORTISTA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperaRegistroPorTransportista(int idTransportista, String txt, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Cisterna eCisterna = null;
  ParametrosListar parametros = null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_POR_TRANSPORTISTA_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   respuesta = dCisterna.recuperarRegistroPorTransportista(idTransportista, txt);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   for(Object respuestaCisterna : respuesta.contenido.carga){
	   eCisterna = (Cisterna) respuestaCisterna;
	   parametros = new ParametrosListar();
	   parametros.setPaginacion(Constante.SIN_PAGINACION);
	   parametros.setFiltroCisterna(eCisterna.getId());
	   parametros.setCampoOrdenamiento(CompartimentoDao.CAMPO_NUMERO_COMPARTIMENTO);
	   parametros.setSentidoOrdenamiento("ASC");
	   RespuestaCompuesta respuestaCompartimentos = dCompartimento.recuperarRegistros(parametros);
	   if (respuestaCompartimentos.estado == false) {
	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	   }
	   for(Object elemento: respuestaCompartimentos.contenido.carga){
	    eCisterna.agregarCompartimento((Compartimento)elemento);
	   }
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

 @RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta guardarRegistro(@RequestBody Cisterna eCisterna, HttpServletRequest peticionHttp, Locale locale) {
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
   //valida los datos que vienen del formulario
   Respuesta validacion = Utilidades.validacionXSS(eCisterna, gestorDiccionario, locale);
   if (validacion.estado == false) {
     throw new Exception(validacion.valor);
   }
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dCisterna.getDataSource());
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
   //eCisterna.setCantidadCompartimentos(eCisterna.getCompartimentos().size());
   eCisterna.setEstado(Constante.ESTADO_ACTIVO);
   eCisterna.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eCisterna.setActualizadoPor(principal.getID());
   eCisterna.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eCisterna.setCreadoPor(principal.getID());
   eCisterna.setIpActualizacion(direccionIp);
   eCisterna.setIpCreacion(direccionIp);
   
   parametros = new ParametrosListar();
   parametros.setPlacaCisterna(eCisterna.getPlaca());
   
   //Valido primero si no hay otro registro igual en BD
   respuesta = dCisterna.validaRegistro(parametros);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   //Si existe el registro valido que no se encuentre activo y que no se encuentre duplicado
   if(respuesta.getContenido().getCarga().size() > 0){
	   Cisterna eCist = (Cisterna) respuesta.getContenido().getCarga().get(0);
	   if(eCist.getEstado() == Cisterna.ESTADO_ACTIVO){
		   throw new Exception(gestorDiccionario.getMessage("sgo.cisternaActiva", null, locale));
	   }
	   else{
		   parametros.setIdTracto(eCisterna.getIdTracto());
		   respuesta = dCisterna.validaRegistro(parametros);
		   // Verifica si la accion se ejecuto de forma satisfactoria
		   if (respuesta.estado == false) {
		     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
		   }
		   if(respuesta.getContenido().getCarga().size() > 0){
			 throw new Exception(gestorDiccionario.getMessage("sgo.cisternaDuplicada", null, locale));
		   }
	   }
   }

   respuesta = dCisterna.guardarRegistro(eCisterna);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper(); 
   ContenidoAuditoria = mapper.writeValueAsString(eCisterna);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(CisternaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eCisterna.getCreadoEl());
   eBitacora.setRealizadoPor(eCisterna.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   
   for(Compartimento compartimento : eCisterna.getCompartimentos()){
    compartimento.setIdCisterna(Integer.parseInt(ClaveGenerada));
    compartimento.setIdTracto(eCisterna.getIdTracto());
    
 	//valida los datos que vienen del formulario para los compartimentos
    validacion = Utilidades.validacionXSS(compartimento, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
    
    respuesta = dCompartimento.guardarRegistro(compartimento);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    }
   }
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eCisterna.getFechaCreacion().substring(0, 9), eCisterna.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
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
 RespuestaCompuesta actualizarRegistro(@RequestBody Cisterna eCisterna, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  ParametrosListar parametros = null;
  try {
	//valida los datos que vienen del formulario 
    Respuesta validacion = Utilidades.validacionXSS(eCisterna, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dCisterna.getDataSource());
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
   eCisterna.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eCisterna.setActualizadoPor(principal.getID());
   eCisterna.setIpActualizacion(direccionIp);
   //eCisterna.setCantidadCompartimentos(eCisterna.getCompartimentos().size());
   
   /*//Valido primero si no hay otro registro igual en BD
   ParametrosListar parametros1 = new ParametrosListar();
   parametros1.setPlacaCisterna(eCisterna.getPlaca());
   respuesta = dCisterna.validaRegistro(parametros1);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   //Si existe el registro valido que no se encuentre activo y que no se encuentre duplicado
   if(respuesta.getContenido().getCarga().size() > 0){
	   Cisterna eCist = (Cisterna) respuesta.getContenido().getCarga().get(0);
	   if(eCist.getEstado() == Cisterna.ESTADO_ACTIVO){
		   throw new Exception(gestorDiccionario.getMessage("sgo.cisternaActiva", null, locale));
	   }
	   else{
		   parametros1.setIdTracto(eCisterna.getIdTracto());
		   respuesta = dCisterna.validaRegistro(parametros1);
		   // Verifica si la accion se ejecuto de forma satisfactoria
		   if (respuesta.estado == false) {
		     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
		   }
		   if(respuesta.getContenido().getCarga().size() > 0){
			 throw new Exception(gestorDiccionario.getMessage("sgo.cisternaDuplicada", null, locale));
		   }
	   }
   }*/
   
   //recuperamos la cisterna original con sus respectivos compartimentos
   respuesta = dCisterna.recuperarRegistro(eCisterna.getId());
   if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }

   Cisterna eCisternaOriginal = (Cisterna) respuesta.contenido.carga.get(0);
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroCisterna(eCisternaOriginal.getId());
   parametros.setCampoOrdenamiento(CompartimentoDao.CAMPO_NUMERO_COMPARTIMENTO);
   parametros.setSentidoOrdenamiento("ASC");

   respuesta = dCompartimento.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }

   for(Object elemento: respuesta.contenido.carga){
	 Compartimento comp = (Compartimento) elemento;
	 eCisternaOriginal.agregarCompartimento(comp);
   }

   //valido si el registro no está en datos históricos de transporte
   parametros = new ParametrosListar();
   parametros.setIdCisterna(eCisterna.getId());
   respuesta = dTransporte.validaRegistrosExistentes(parametros);
   if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }

   if (respuesta.contenido.carga.size() > 0){
	 if(!(eCisternaOriginal.getPlaca().equals(eCisterna.getPlaca()))){
	   throw new Exception(gestorDiccionario.getMessage("sgo.noModificarPlacaCisterna", null, locale)); 
	 }

	 if(eCisternaOriginal.getIdTracto() != eCisterna.getIdTracto()){
	   throw new Exception(gestorDiccionario.getMessage("sgo.noModificarPlacaTracto", null, locale)); 
	 }
	  /* for(Object elemento: respuestaCompartimentos.contenido.carga){
		   Compartimento compartimentoCisternaOriginal = (Compartimento) elemento;

		   for(Compartimento compartimento : eCisterna.getCompartimentos()){
			 Compartimento compartimentoFormulario =(Compartimento) compartimento;
			 
			 if(compartimentoFormulario.getIdentificador() == compartimentoCisternaOriginal.getIdentificador()){
				 if(compartimentoFormulario.getAlturaFlecha() != compartimentoCisternaOriginal.getAlturaFlecha()){
					 throw new Exception(gestorDiccionario.getMessage("sgo.noModificarAlturaFlechaCompartimento", new Object[] { compartimentoCisternaOriginal.getIdentificador()}, locale)); 
				 }
				 
				 if(compartimentoFormulario.getCapacidadVolumetrica() != compartimentoCisternaOriginal.getCapacidadVolumetrica()){
					 throw new Exception(gestorDiccionario.getMessage("sgo.noModificarCapacidadVolumetricaCompartimento", new Object[] { compartimentoCisternaOriginal.getIdentificador()}, locale)); 
				 }
			 }
		   }
	   }*/
   }
   
   //si las validaciones son correctas permito hacer la actualización de la cisterna.
   respuesta = dCisterna.actualizarRegistro(eCisterna);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
   eBitacora.setTabla(CisternaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eCisterna.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eCisterna));
   eBitacora.setRealizadoEl(eCisterna.getActualizadoEl());
   eBitacora.setRealizadoPor(eCisterna.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   
   //esto para eliminar los compartimentos
    for(Compartimento compartimentoCisternaOriginal: eCisternaOriginal.getCompartimentos()){
     boolean comparaCompartimentos = false;
     for(Compartimento compartimentoFormulario : eCisterna.getCompartimentos()){
       if(compartimentoFormulario.getId() == compartimentoCisternaOriginal.getId()){
       comparaCompartimentos = true;
       break;
       }
     }
     if(comparaCompartimentos == false){
       parametros = new ParametrosListar();
       parametros.setFiltroCisterna(eCisternaOriginal.getId());
       parametros.setFiltroTracto(eCisternaOriginal.getIdTracto());
       parametros.setFiltroCompartimento(compartimentoCisternaOriginal.getId());
       //primero elimino los aforos
       respuesta = dAforoCisterna.eliminarRegistros(parametros);
       if (respuesta.estado == false) {
         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
       }
       //luego elimino el compartimento
       respuesta = dCompartimento.eliminarRegistro(compartimentoCisternaOriginal.getId());
       if (respuesta.estado == false) {
         throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
       }
     }
   }

   //esto para agregar compartimentos
   for(Compartimento compartimentoFormulario : eCisterna.getCompartimentos()){
     compartimentoFormulario.setIdCisterna(eCisterna.getId());
     compartimentoFormulario.setIdTracto(eCisterna.getIdTracto());
     //si el id es 0 entonces creamos un nuevo registro
     if(compartimentoFormulario.getId() == 0){
    	//valida los datos que vienen del formulario para el compartimento
	    validacion = Utilidades.validacionXSS(compartimentoFormulario, gestorDiccionario, locale);
	    if (validacion.estado == false) {
	      throw new Exception(validacion.valor);
	    }
 	    respuesta = dCompartimento.guardarRegistro(compartimentoFormulario);
 	    if (respuesta.estado == false) {
 	      throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
 	    }
     }
   }
   //Esto para actualizar compartimentos
   for(Compartimento compartimentoCisternaOriginal: eCisternaOriginal.getCompartimentos()){
     for(Compartimento compartimentoFormulario : eCisterna.getCompartimentos()){
       compartimentoFormulario.setIdCisterna(eCisterna.getId());
       compartimentoFormulario.setIdTracto(eCisterna.getIdTracto());
       if(compartimentoFormulario.getId() == compartimentoCisternaOriginal.getId()){
    	 //valida los datos que vienen del formulario para el compartimento
		  validacion = Utilidades.validacionXSS(compartimentoFormulario, gestorDiccionario, locale);
		  if (validacion.estado == false) {
		    throw new Exception(validacion.valor);
		  }
		  respuesta = dCompartimento.actualizarRegistro(compartimentoFormulario);
		  if (respuesta.estado == false) {
	        throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
	      }
       }
     }
   }

   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eCisterna.getFechaActualizacion().substring(0, 9), eCisterna.getFechaActualizacion().substring(10),principal.getIdentidad() }, locale);

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
 RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Cisterna eCisterna, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dCisterna.getDataSource());
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
   eCisterna.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eCisterna.setActualizadoPor(principal.getID());
   eCisterna.setIpActualizacion(direccionIp);
   respuesta = dCisterna.ActualizarEstadoRegistro(eCisterna);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(CisternaDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eCisterna.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eCisterna));
   eBitacora.setRealizadoEl(eCisterna.getActualizadoEl());
   eBitacora.setRealizadoPor(eCisterna.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eCisterna.getFechaActualizacion().substring(0, 9), eCisterna.getFechaActualizacion().substring(10),
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