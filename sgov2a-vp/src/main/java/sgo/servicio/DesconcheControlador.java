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
import sgo.datos.DesconcheDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.OperacionDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Desconche;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class DesconcheControlador {
  @Autowired
  private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
  @Autowired
  private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
  @Autowired
  private EnlaceDao dEnlace;
  @Autowired
  private MenuGestor menu;
  @Autowired
  private DesconcheDao dDesconche;
  @Autowired
  private OperacionDao dOperacion;
  @Autowired
  private DiaOperativoDao dDiaOperativo;
  //
  private DataSourceTransactionManager transaccion;//Gestor de la transaccion
  /** Nombre de la clase. */
  private static final String sNombreClase = "DesconcheControlador";
  //urls generales
  private static final String URL_GESTION_COMPLETA="/admin/desconche";
  private static final String URL_GESTION_RELATIVA="/desconche";
  private static final String URL_GUARDAR_COMPLETA="/admin/desconche/crear";
  private static final String URL_GUARDAR_RELATIVA="/desconche/crear";
  private static final String URL_LISTAR_COMPLETA="/admin/desconche/listar";
  private static final String URL_LISTAR_RELATIVA="/desconche/listar";
  
  private static final String URL_LISTAR_COMBO_COMPLETA="/admin/desconche/listar-combo";
  private static final String URL_LISTAR_COMBO_RELATIVA="/desconche/listar-combo";
  
  private static final String URL_ELIMINAR_COMPLETA="/admin/desconche/eliminar";
  private static final String URL_ELIMINAR_RELATIVA="/desconche/eliminar";
  private static final String URL_ACTUALIZAR_COMPLETA="/admin/desconche/actualizar";
  private static final String URL_ACTUALIZAR_RELATIVA="/desconche/actualizar";
  private static final String URL_RECUPERAR_COMPLETA="/admin/desconche/recuperar";
  private static final String URL_RECUPERAR_RELATIVA="/desconche/recuperar";
  
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

   } catch (Exception ex) {
	   Utilidades.gestionaError(ex, sNombreClase, "recuperarMapaValores");
   }
   return mapaValores;
  }
  
  @RequestMapping(URL_GESTION_RELATIVA)
  public ModelAndView mostrarFormulario( Locale locale){
    ModelAndView vista =null;
    AuthenticatedUserDetails principal = null;
    ArrayList<Enlace> listaEnlaces = null;
    RespuestaCompuesta respuesta = null;
    HashMap<String, String> mapaValores = null;
    ArrayList<?> listaOperaciones = null;
    ParametrosListar parametros = null;
    try {
      principal = this.getCurrentUser();
      respuesta = menu.Generar(principal.getRol().getId(),URL_GESTION_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado",null,locale));
      }
      listaEnlaces = (ArrayList<Enlace>) respuesta.contenido.carga;     
      
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
      
      vista = new ModelAndView("plantilla");
      mapaValores = recuperarMapaValores(locale);
      vista.addObject("vistaJSP","descarga/desconche.jsp");
      vista.addObject("vistaJS","descarga/desconche.js");
      vista.addObject("identidadUsuario",principal.getIdentidad());
      vista.addObject("menu",listaEnlaces);
      vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
      vista.addObject("operaciones", listaOperaciones);
      vista.addObject("mapaValores", mapaValores);
    } catch(Exception ex){
      Utilidades.gestionaError(ex, sNombreClase, "mostrarFormulario");
    }
    return vista;
  }
  
  @RequestMapping(value = URL_LISTAR_COMBO_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta listarComboDesconche(HttpServletRequest httpRequest, Locale locale){
    RespuestaCompuesta respuesta = null;
    ParametrosListar parametros= null;
    AuthenticatedUserDetails principal = null;
    String mensajeRespuesta="";
    try {
      //Recuperar el usuario actual
      principal = this.getCurrentUser();
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMBO_COMPLETA);
      if (respuesta.estado==false){
        mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale);
        throw new Exception(mensajeRespuesta);
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
        mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale);
        throw new Exception(mensajeRespuesta);
        }
      //Recuperar parametros
       parametros = new ParametrosListar();   
      if (httpRequest.getParameter("valorBuscado") != null) {
        parametros.setValorBuscado(( httpRequest.getParameter("valorBuscado")));
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
      //Recuperar registros
      respuesta = dDesconche.listarComboDesconche(parametros);
      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      respuesta.estado=false;
      respuesta.contenido = null;
      Utilidades.gestionaError(ex, sNombreClase, "listarComboDesconche");
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_LISTAR_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest, Locale locale){
    RespuestaCompuesta respuesta = null;
    ParametrosListar parametros= null;
    AuthenticatedUserDetails principal = null;
    String mensajeRespuesta="";
    try {
      //Recuperar el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
      if (respuesta.estado==false){
        mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale);
        throw new Exception(mensajeRespuesta);
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario      
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
        mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale);
        throw new Exception(mensajeRespuesta);
        }
      //Recuperar parametros
       parametros = new ParametrosListar();
      if (httpRequest.getParameter("paginacion") != null) {
        parametros.setPaginacion(Integer.parseInt( httpRequest.getParameter("paginacion")));
      }
      
      if (httpRequest.getParameter("registrosxPagina") != null) {
        parametros.setRegistrosxPagina(Integer.parseInt( httpRequest.getParameter("registrosxPagina")));
      }
      
      if (httpRequest.getParameter("inicioPagina") != null) {
        parametros.setInicioPaginacion(Integer.parseInt( httpRequest.getParameter("inicioPagina")));
      }
      
      if (httpRequest.getParameter("campoOrdenamiento") != null) {
        parametros.setCampoOrdenamiento(( httpRequest.getParameter("campoOrdenamiento")));
      }
      
      if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
        parametros.setSentidoOrdenamiento(( httpRequest.getParameter("sentidoOrdenamiento")));
      }
      
      if (httpRequest.getParameter("paginacion") != null) {
       parametros.setPaginacion(Integer.parseInt( httpRequest.getParameter("paginacion")));
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
      
      if (httpRequest.getParameter("filtroPlacaCisterna") != null) {
       parametros.setFiltroPlacaCisterna((httpRequest.getParameter("filtroPlacaCisterna")));
      }

      //Recuperar registros
      respuesta = dDesconche.recuperarRegistros(parametros);
      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      respuesta.estado=false;
      respuesta.contenido = null;
      Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  } 

  @RequestMapping(value = URL_RECUPERAR_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperaRegistro(int ID,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    try {     
      //Recupera el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario      
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
          throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
        }
      //Recuperar el registro
         // respuesta= dDesconche.recuperarRegistro(ID);
      respuesta = dDesconche.recuperarRegistroVista(ID);
          //Verifica el resultado de la accion
            if (respuesta.estado==false){         
              throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
    } catch (Exception ex){
      //ex.printStackTrace();
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
      Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistro");
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_GUARDAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody Desconche eDesconche,HttpServletRequest peticionHttp,Locale locale){    
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    Bitacora eBitacora= null;
    String ContenidoAuditoria ="";
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    String direccionIp="";
    String ClaveGenerada="";
    try {
      //valida los datos que vienen del formulario
      Respuesta validacion = Utilidades.validacionXSS(eDesconche, gestorDiccionario, locale);
      if (validacion.estado == false) {
        throw new Exception(validacion.valor);
      }
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dDesconche.getDataSource());
      definicionTransaccion = new DefaultTransactionDefinition();
      estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
      eBitacora = new Bitacora();
      //Recuperar el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_GUARDAR_COMPLETA);
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
          eDesconche.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eDesconche.setActualizadoPor(principal.getID()); 
            eDesconche.setCreadoEl(Calendar.getInstance().getTime().getTime());
            eDesconche.setCreadoPor(principal.getID());
            eDesconche.setIpActualizacion(direccionIp);
            eDesconche.setIpCreacion(direccionIp);
            respuesta= dDesconche.guardarRegistro(eDesconche);
            //Verifica si la accion se ejecuto de forma satisfactoria
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
            }
            ClaveGenerada = respuesta.valor;
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
            ContenidoAuditoria =  mapper.writeValueAsString(eDesconche);
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_GUARDAR_COMPLETA);
            eBitacora.setTabla(DesconcheDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(ClaveGenerada);
            eBitacora.setContenido(ContenidoAuditoria);
            eBitacora.setRealizadoEl(eDesconche.getCreadoEl());
            eBitacora.setRealizadoPor(eDesconche.getCreadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }           
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eDesconche.getFechaCreacion().substring(0, 9),eDesconche.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
          this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      this.transaccion.rollback(estadoTransaccion);
      //ex.printStackTrace();
      respuesta.estado=false;
      respuesta.contenido = null;
      Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_ACTUALIZAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta actualizarRegistro(@RequestBody Desconche eDesconche,HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    Bitacora eBitacora=null;
    String direccionIp="";
    try {
	  //valida los datos que vienen del formulario
      Respuesta validacion = Utilidades.validacionXSS(eDesconche, gestorDiccionario, locale);
      if (validacion.estado == false) {
        throw new Exception(validacion.valor);
      }
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dDesconche.getDataSource());
      definicionTransaccion = new DefaultTransactionDefinition();
      estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
      eBitacora = new Bitacora();
      //Recuperar el usuario actual
      principal = this.getCurrentUser();
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_COMPLETA);
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
          eDesconche.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eDesconche.setActualizadoPor(principal.getID()); 
            eDesconche.setIpActualizacion(direccionIp);
            respuesta= dDesconche.actualizarRegistro(eDesconche);
            if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(DesconcheDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eDesconche.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eDesconche));
            eBitacora.setRealizadoEl(eDesconche.getActualizadoEl());
            eBitacora.setRealizadoPor(eDesconche.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eDesconche.getFechaActualizacion().substring(0, 9),eDesconche.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
          this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      //ex.printStackTrace();
      Utilidades.gestionaError(ex, sNombreClase, "actualizarRegistro");
      this.transaccion.rollback(estadoTransaccion);
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  private  AuthenticatedUserDetails getCurrentUser(){
    return  (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
