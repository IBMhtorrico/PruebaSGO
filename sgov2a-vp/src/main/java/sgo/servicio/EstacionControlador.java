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
import sgo.datos.BitacoraDao;
import sgo.datos.EstacionDao;
import sgo.datos.EnlaceDao;
import sgo.datos.OperacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.ToleranciaDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.DescargaCisterna;
import sgo.entidad.Estacion;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Producto;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tolerancia;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class EstacionControlador {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora;
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private ProductoDao dProducto;
 @Autowired
 private MenuGestor menu;
 @Autowired
 private EstacionDao dEstacion;
 @Autowired
 private ToleranciaDao dTolerancia;
 @Autowired
 private OperacionDao dOperacion;
 //
 private DataSourceTransactionManager transaccion;// Gestor de la transaccion
 // urls generales
 private static final String URL_GESTION_COMPLETA = "/admin/estacion";
 private static final String URL_GESTION_RELATIVA = "/estacion";

 private static final String URL_GUARDAR_COMPLETA = "/admin/estacion/crear";
 private static final String URL_GUARDAR_RELATIVA = "/estacion/crear";

 private static final String URL_LISTAR_COMPLETA = "/admin/estacion/listar";
 private static final String URL_LISTAR_RELATIVA = "/estacion/listar";

 private static final String URL_ACTUALIZAR_COMPLETA = "/admin/estacion/actualizar";
 private static final String URL_ACTUALIZAR_RELATIVA = "/estacion/actualizar";

 private static final String URL_RECUPERAR_COMPLETA = "/admin/estacion/recuperar";
 private static final String URL_RECUPERAR_RELATIVA = "/estacion/recuperar";

 private static final String URL_ACTUALIZAR_ESTADO_COMPLETA = "/admin/estacion/actualizarEstado";
 private static final String URL_ACTUALIZAR_ESTADO_RELATIVA = "/estacion/actualizarEstado";

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

   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));

   mapaValores.put("TEXTO_METODO_WINCHA", gestorDiccionario.getMessage("sgo.metodoWincha", null, locale));
   mapaValores.put("TEXTO_METODO_BALANZA", gestorDiccionario.getMessage("sgo.metodoBalanza", null, locale));
   mapaValores.put("TEXTO_METODO_CONTOMETRO", gestorDiccionario.getMessage("sgo.metodoContometro", null, locale));

   mapaValores.put("VALOR_METODO_WINCHA", String.valueOf(DescargaCisterna.METODO_WINCHA));
   mapaValores.put("VALOR_METODO_BALANZA", String.valueOf(DescargaCisterna.METODO_BALANZA));
   mapaValores.put("VALOR_METODO_CONTOMETRO", String.valueOf(DescargaCisterna.METODO_CONTOMETRO));

   mapaValores.put("TEXTO_TIPO_ESTANDAR", gestorDiccionario.getMessage("sgo.tipoEstacionEstandar", null, locale));
   mapaValores.put("TEXTO_TIPO_REPARTO", gestorDiccionario.getMessage("sgo.tipoEstacionReparto", null, locale));
   mapaValores.put("TEXTO_TIPO_TUBERIA", gestorDiccionario.getMessage("sgo.tipoEstacionTuberia", null, locale));

   mapaValores.put("VALOR_TIPO_ESTANDAR", String.valueOf(Estacion.TIPO_ESTANDAR));
   mapaValores.put("VALOR_TIPO_REPARTO", String.valueOf(Estacion.TIPO_REPARTO));
   mapaValores.put("VALOR_TIPO_TUBERIA", String.valueOf(Estacion.TIPO_TUBERIA));
   
   mapaValores.put("TIPO_VOLUMEN_OBSERVADO", String.valueOf(Tolerancia.VOLUMEN_OBSERVADO));
   mapaValores.put("TIPO_VOLUMEN_CORREGIDO", String.valueOf(Tolerancia.VOLUMEN_CORREGIDO));
   mapaValores.put("NOMBRE_VOLUMEN_CORREGIDO",  gestorDiccionario.getMessage("sgo.tituloVolumenCorregido", null, locale));
   mapaValores.put("NOMBRE_VOLUMEN_OBSERVADO",  gestorDiccionario.getMessage("sgo.tituloVolumenObservado", null, locale));

  } catch (Exception ex) {

  }
  return mapaValores;
 }

 @RequestMapping(URL_GESTION_RELATIVA)
 public ModelAndView mostrarFormulario(Locale locale) {
  ModelAndView vista = null;
  AuthenticatedUserDetails principal = null;
  ArrayList<?> listaEnlaces = null;
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  ArrayList<?> listaOperaciones = null;
  HashMap<String, String> mapaValores = null;
  ArrayList<?> listaProductos = null;
  try {
   principal = this.getCurrentUser();
   respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado", null, locale));
   }
   listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;

   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
   respuesta = dOperacion.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.noListadoOperaciones", null, locale));
   }
   listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;

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
   vista.addObject("vistaJSP", "mantenimiento/estacion.jsp");
   vista.addObject("vistaJS", "mantenimiento/estacion.js");
   vista.addObject("identidadUsuario", principal.getIdentidad());
   vista.addObject("menu", listaEnlaces);
   vista.addObject("listadoOperaciones", listaOperaciones);
   vista.addObject("listaProductos", listaProductos);
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
  try {
   // Recuperar el usuario actual
   principal = this.getCurrentUser();
   // Recuperar el enlace de la accion
   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   // Verificar si cuenta con el permiso necesario
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
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
   parametros.setFiltroOperacion(Constante.FILTRO_TODOS);
   if (httpRequest.getParameter("filtroOperacion") != null) {
    parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
   }
   // Recuperar registros
   respuesta = dEstacion.recuperarRegistros(parametros);
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
  ParametrosListar parametros =null;
  Estacion eEstacion = null;
  Contenido<Estacion> contenido =  null;
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
   respuesta = dEstacion.recuperarRegistro(ID);
   // Verifica el resultado de la accion
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }   
   eEstacion = (Estacion) respuesta.contenido.carga.get(0);
   parametros = new ParametrosListar();
   parametros.setFiltroEstacion(eEstacion.getId());
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   respuesta = dTolerancia.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   for(Object tolerancia: respuesta.contenido.carga){
    eEstacion.agregarTolerancia((Tolerancia)tolerancia);
   }
   contenido  = new Contenido<Estacion>();
   contenido.carga =  new ArrayList<Estacion>();
   contenido.carga.add(eEstacion);
   respuesta.contenido=contenido;   
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
 RespuestaCompuesta guardarRegistro(@RequestBody Estacion eEstacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  Bitacora eBitacora = null;
  String ContenidoAuditoria = "";
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  String direccionIp = "";
  String ClaveGenerada = "";
  try {
	//valida los datos que vienen del formulario
    Respuesta validacion = Utilidades.validacionXSS(eEstacion, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
   this.transaccion = new DataSourceTransactionManager(dEstacion.getDataSource());
   definicionTransaccion = new DefaultTransactionDefinition();
   estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
   eBitacora = new Bitacora();
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_COMPLETA);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
   }
   direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");
   if (direccionIp == null) {
    direccionIp = peticionHttp.getRemoteAddr();
   }
   eEstacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eEstacion.setActualizadoPor(principal.getID());
   eEstacion.setCreadoEl(Calendar.getInstance().getTime().getTime());
   eEstacion.setCreadoPor(principal.getID());
   eEstacion.setIpActualizacion(direccionIp);
   eEstacion.setIpCreacion(direccionIp);
   eEstacion.setEstado(Constante.ESTADO_ACTIVO);
   respuesta = dEstacion.guardarRegistro(eEstacion);
   // Verifica si la accion se ejecuto de forma satisfactoria
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }
   ClaveGenerada = respuesta.valor;
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   ContenidoAuditoria = mapper.writeValueAsString(eEstacion);
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
   eBitacora.setTabla(EstacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(ClaveGenerada);
   eBitacora.setContenido(ContenidoAuditoria);
   eBitacora.setRealizadoEl(eEstacion.getCreadoEl());
   eBitacora.setRealizadoPor(eEstacion.getCreadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }

   for(Tolerancia tolerancia: eEstacion.getTolerancias()){
    tolerancia.setIdEstacion(Integer.parseInt(ClaveGenerada));
    System.out.println("tolerancia.getPorcentajeActual()");
    System.out.println(tolerancia.getPorcentajeActual());
    //valida los datos que vienen del formulario para la tolerancia
    validacion = Utilidades.validacionXSS(tolerancia, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
    respuesta = dTolerancia.guardarRegistro(tolerancia);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   }
   
   /*//ESTO PARA GUARDAR EL SIN PRODUCTO EN LA TOLERANCIA
   ParametrosListar parametros = new ParametrosListar();
   parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
   parametros.setIndicadorProducto(Producto.INDICADOR_PRODUCTO_SIN_DATOS);
   respuesta = dProducto.recuperarRegistros(parametros);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   Producto eProducto = (Producto) respuesta.contenido.carga.get(0);
   Tolerancia tolerancia = new Tolerancia();
   tolerancia.setIdEstacion(Integer.parseInt(ClaveGenerada));
   tolerancia.setIdProducto(eProducto.getId());
   tolerancia.setPorcentajeActual(Constante.SIN_VALOR);
   tolerancia.setTipoVolumen(Constante.SIN_VALOR);
   respuesta = dTolerancia.guardarRegistro(tolerancia);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
   }*/
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.guardarExitoso", new Object[] { eEstacion.getFechaCreacion().substring(0, 9), eEstacion.getFechaCreacion().substring(10), principal.getIdentidad() }, locale);
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
 RespuestaCompuesta actualizarRegistro(@RequestBody Estacion eEstacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
	//valida los datos que vienen del formulario 
    Respuesta validacion = Utilidades.validacionXSS(eEstacion, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dEstacion.getDataSource());
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
   eEstacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eEstacion.setActualizadoPor(principal.getID());
   eEstacion.setIpActualizacion(direccionIp);
   respuesta = dEstacion.actualizarRegistro(eEstacion);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
   eBitacora.setTabla(EstacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eEstacion.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eEstacion));
   eBitacora.setRealizadoEl(eEstacion.getActualizadoEl());
   eBitacora.setRealizadoPor(eEstacion.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta= dTolerancia.eliminarRegistros(eEstacion.getId());
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   
   for(Tolerancia tolerancia: eEstacion.getTolerancias()){
    tolerancia.setIdEstacion(eEstacion.getId());
    //valida los datos que vienen del formulario para la tolerancia
    validacion = Utilidades.validacionXSS(tolerancia, gestorDiccionario, locale);
    if (validacion.estado == false) {
      throw new Exception(validacion.valor);
    }
    respuesta = dTolerancia.guardarRegistro(tolerancia);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    }
   } 
   
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eEstacion.getFechaActualizacion().substring(0, 9), eEstacion.getFechaActualizacion().substring(10), principal.getIdentidad() }, locale);
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

 @RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA, method = RequestMethod.POST)
 public @ResponseBody
 RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Estacion eEstacion, HttpServletRequest peticionHttp, Locale locale) {
  RespuestaCompuesta respuesta = null;
  AuthenticatedUserDetails principal = null;
  TransactionDefinition definicionTransaccion = null;
  TransactionStatus estadoTransaccion = null;
  Bitacora eBitacora = null;
  String direccionIp = "";
  try {
   // Inicia la transaccion
   this.transaccion = new DataSourceTransactionManager(dEstacion.getDataSource());
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
   eEstacion.setActualizadoEl(Calendar.getInstance().getTime().getTime());
   eEstacion.setActualizadoPor(principal.getID());
   eEstacion.setIpActualizacion(direccionIp);
   respuesta = dEstacion.ActualizarEstadoRegistro(eEstacion);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido", null, locale));
   }
   // Guardar en la bitacora
   ObjectMapper mapper = new ObjectMapper();
   eBitacora.setUsuario(principal.getNombre());
   eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
   eBitacora.setTabla(EstacionDao.NOMBRE_TABLA);
   eBitacora.setIdentificador(String.valueOf(eEstacion.getId()));
   eBitacora.setContenido(mapper.writeValueAsString(eEstacion));
   eBitacora.setRealizadoEl(eEstacion.getActualizadoEl());
   eBitacora.setRealizadoPor(eEstacion.getActualizadoPor());
   respuesta = dBitacora.guardarRegistro(eBitacora);
   if (respuesta.estado == false) {
    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
   }
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.actualizarExitoso", new Object[] { eEstacion.getFechaActualizacion().substring(0, 9), eEstacion.getFechaActualizacion().substring(10),
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

 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
}
