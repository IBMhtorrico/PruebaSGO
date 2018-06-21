package sgo.servicio;


import java.io.ByteArrayInputStream;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import sgo.datos.ClienteDao;
import sgo.datos.DescargaCompartimentoDao;
import sgo.datos.DetalleGECDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.GecAprobacionDao;
import sgo.datos.GuiaCombustibleDao;
import sgo.datos.EnlaceDao;
import sgo.datos.OperacionDao;
import sgo.datos.ParametroDao;
import sgo.datos.UsuarioDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Cisterna;
import sgo.entidad.Cliente;
import sgo.entidad.Compartimento;
import sgo.entidad.Contenido;
import sgo.entidad.DescargaCisterna;
import sgo.entidad.DescargaCompartimento;
import sgo.entidad.DetalleGEC;
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DiaOperativo;
import sgo.entidad.GecAprobacion;
import sgo.entidad.GuiaCombustible;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.Operacion;
import sgo.entidad.Parametro;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.ResumenCierre;
import sgo.entidad.Rol;
import sgo.entidad.Usuario;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.CabeceraReporte;
import sgo.utilidades.Campo;
import sgo.utilidades.Constante;
import sgo.utilidades.MailNotifica;
import sgo.utilidades.ReporteGec;
import sgo.utilidades.Reporteador;
import sgo.utilidades.Utilidades;

@Controller
public class GuiaCombustibleControlador {
  @Autowired
  private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
  @Autowired
  private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
  @Autowired
  private EnlaceDao dEnlace;
  @Autowired
  private MailNotifica dMailNotifica;
  @Autowired
  private MenuGestor menu;
  @Autowired
  private GuiaCombustibleDao dGuiaCombustible;
  @Autowired
  private GecAprobacionDao dGecAprobacion;
  @Autowired
  private DetalleGECDao dDetalleGec;
  @Autowired
  private DiaOperativoDao dDiaOperativo;
  @Autowired
  private ClienteDao dCliente;
  @Autowired
  private ParametroDao dParametro;
  @Autowired
  ServletContext servletContext;
  //
  @Autowired
  private OperacionDao dOperacion;
  @Autowired
  private UsuarioDao dUsuario;
  private DataSourceTransactionManager transaccion;//Gestor de la transaccion
  /** Nombre de la clase. */
  private static final String sNombreClase = "GuiaCombustibleControlador";
  //urls generales
  private static final String URL_GESTION_COMPLETA="/admin/guia-combustible";
  private static final String URL_GESTION_RELATIVA="/guia-combustible";
  private static final String URL_GUARDAR_COMPLETA="/admin/guia-combustible/crear";
  private static final String URL_GUARDAR_RELATIVA="/guia-combustible/crear";
  private static final String URL_LISTAR_COMPLETA="/admin/guia-combustible/listar";
  private static final String URL_LISTAR_RELATIVA="/guia-combustible/listar";
  
  private static final String URL_LISTAR_GUIAS_COMPLETA="/admin/guia-combustible/listar-guias";
  private static final String URL_LISTAR_GUIAS_RELATIVA="/guia-combustible/listar-guias";
  
  private static final String URL_ELIMINAR_COMPLETA="/admin/guia-combustible/eliminar";
  private static final String URL_ELIMINAR_RELATIVA="/guia-combustible/eliminar";
  private static final String URL_ACTUALIZAR_COMPLETA="/admin/guia-combustible/actualizar";
  private static final String URL_ACTUALIZAR_RELATIVA="/guia-combustible/actualizar";
  private static final String URL_RECUPERAR_COMPLETA="/admin/guia-combustible/recuperar";
  private static final String URL_RECUPERAR_RELATIVA="/guia-combustible/recuperar";
  private static final String URL_EMITIR_COMPLETA="/admin/guia-combustible/emitir";
  private static final String URL_EMITIR_RELATIVA="/guia-combustible/emitir";
  private static final String URL_APROBAR_COMPLETA="/admin/guia-combustible/aprobar-gec";
  private static final String URL_APROBAR_RELATIVA="/guia-combustible/aprobar-gec";
  private static final String URL_RECUPERAR_NUMERO_GEC_COMPLETA="/admin/guia-combustible/recuperar-numero-gec";
  private static final String URL_RECUPERAR_NUMERO_GEC_RELATIVA="/guia-combustible/recuperar-numero-gec";  
  private static final String URL_VERIFICA_NOTIFICAR_COMPLETA = "/admin/guia-combustible/verifica-notificacion";
  private static final String URL_VERIFICA_NOTIFICAR_RELATIVA = "/guia-combustible/verifica-notificacion";
  private static final String URL_NOTIFICAR_COMPLETA="/admin/guia-combustible/notificar-gec";
  private static final String URL_NOTIFICAR_RELATIVA="/guia-combustible/notificar-gec";
  private static final String URL_REPORTE_GEC_COMPLETA="/admin/guia-combustible/reporte-gec";
  private static final String URL_REPORTE_GEC_RELATIVA="/guia-combustible/reporte-gec";
  
  
  private HashMap<String, String> recuperarMapaValores(Locale locale) {
   HashMap<String, String> mapaValores = new HashMap<String, String>();
   try {
    mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
    mapaValores.put("ESTADO_INACTIVO", String.valueOf(Constante.ESTADO_INACTIVO));
    
    mapaValores.put("ESTADO_REGISTRADO", String.valueOf(Constante.ESTADO_REGISTRADO));
    mapaValores.put("ESTADO_EMITIDO", String.valueOf(Constante.ESTADO_EMITIDO));
    mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
    mapaValores.put("TEXTO_ESTADO_REGISTRADO", gestorDiccionario.getMessage("sgo.estadoRegistrado", null, locale));
    mapaValores.put("TEXTO_ESTADO_EMITIDO", gestorDiccionario.getMessage("sgo.estadoEmitido", null, locale));
    mapaValores.put("TEXTO_ESTADO_APROBADO", gestorDiccionario.getMessage("sgo.estadoAprobado", null, locale)); 
    mapaValores.put("TEXTO_ESTADO_OBSERVADO", gestorDiccionario.getMessage("sgo.estadoObservado", null, locale));
    
    mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
    mapaValores.put("TEXTO_INACTIVO", gestorDiccionario.getMessage("sgo.estadoInactivo", null, locale));
    mapaValores.put("TEXTO_ACTIVO", gestorDiccionario.getMessage("sgo.estadoActivo", null, locale));
    mapaValores.put("TEXTO_TODOS", gestorDiccionario.getMessage("sgo.filtroTodos", null, locale));
    mapaValores.put("TEXTO_BUSCAR", gestorDiccionario.getMessage("sgo.buscarElemento", null, locale));

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
    
    mapaValores.put("ETIQUETA_BOTON_EMITIR", gestorDiccionario.getMessage("sgo.etiquetaBotonEmitirGec", null, locale));
    mapaValores.put("ETIQUETA_BOTON_APROBAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAprobarGec", null, locale));
    mapaValores.put("ETIQUETA_BOTON_OBSERVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonObservarGec", null, locale));
    mapaValores.put("ETIQUETA_BOTON_IMPRIMIR", gestorDiccionario.getMessage("sgo.etiquetaBotonImprimirPDF", null, locale));
    mapaValores.put("ETIQUETA_BOTON_NOTIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonNotificarGec", null, locale));
    mapaValores.put("MENSAJE_ENVIAR_CORREO", gestorDiccionario.getMessage("sgo.mensajeEnviarCorreo", null, locale));

    mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
    mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));
    mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado", null, locale));

    mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
    mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));

   } catch (Exception ex) {

   }
   return mapaValores;
  }
  
  @RequestMapping("/guia-combustible")
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
      
      Operacion opeTemp = (Operacion) respuesta.contenido.carga.get(0);
      int idOperacion = opeTemp.getId();
      String fecha = dDiaOperativo.recuperarFechaActual().valor;
      //esto para obtener el ultimo dia operativo 
      Respuesta oRespuesta = dDiaOperativo.recuperarUltimoDiaOperativo(idOperacion, "");
      // Verifica el resultado de la accion
      if (oRespuesta.estado == false) {
       throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
      }
      if (oRespuesta.valor != null) {
   	   fecha = oRespuesta.valor;
      }
      
      
      
      mapaValores = recuperarMapaValores(locale);
      vista = new ModelAndView("plantilla");
      vista.addObject("vistaJSP","operaciones/gec.jsp");
      vista.addObject("vistaJS","operaciones/gec.js");
      vista.addObject("identidadUsuario",principal.getIdentidad());
      vista.addObject("mapaValores", mapaValores);
      vista.addObject("fechaActual", fecha);
      //vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
      vista.addObject("rolIdUsuario", principal.getRol().getId());
      vista.addObject("rolUsuario", principal.getRol().getNombre());
      vista.addObject("operaciones", listaOperaciones);
      vista.addObject("menu",listaEnlaces);
    } catch(Exception ex){
      
    }
    return vista;
  }
  
  @RequestMapping(value = URL_LISTAR_GUIAS_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperarGuias(HttpServletRequest httpRequest, Locale locale){
    RespuestaCompuesta respuesta = null;
    ParametrosListar parametros= null;
    AuthenticatedUserDetails principal = null;
    String mensajeRespuesta="";
    try {
      //Recuperar el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_LISTAR_GUIAS_COMPLETA);
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
      if ((httpRequest.getParameter("idTransportista") != null) && (Integer.parseInt(httpRequest.getParameter("idTransportista")))>0) {
       parametros.setIdTransportista(Integer.parseInt(httpRequest.getParameter("idTransportista")));
      }

      if (httpRequest.getParameter("filtroProducto") != null) {
       parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
      }
      
      if (httpRequest.getParameter("filtroFechaDiaOperativo") != null) {
       parametros.setFiltroFechaDiaOperativo((httpRequest.getParameter("filtroFechaDiaOperativo")));
      }
      if (httpRequest.getParameter("filtroTipoConsulta") != null && !httpRequest.getParameter("filtroTipoConsulta").isEmpty() ) {
          parametros.setFiltroParametro((httpRequest.getParameter("filtroTipoConsulta")));
      }
      //Recuperar registros
      respuesta = dDetalleGec.recuperarRegistrosVista(parametros);
      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      Utilidades.gestionaError(ex, sNombreClase, "recuperarGuias");
      respuesta.estado=false;
      respuesta.contenido = null;
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
    int rolPrincipal;
    try {
      //Recuperar el usuario actual
      principal = this.getCurrentUser(); 
      rolPrincipal = principal.getRol().getId();
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
      
      if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
    	  parametros.setSentidoOrdenamiento(( httpRequest.getParameter("sentidoOrdenamiento")));
      }
      
      if (httpRequest.getParameter("campoOrdenamiento") != null && !httpRequest.getParameter("campoOrdenamiento").isEmpty()) {
        parametros.setCampoOrdenamiento(( httpRequest.getParameter("campoOrdenamiento")));
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
      
      if (httpRequest.getParameter("filtroEstado") != null) {
       parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
      }
      
      parametros.setQueryRolGec("");
      if (rolPrincipal == Rol.ROL_CLIENTE) {
    	  parametros.setQueryRolGec(" t1.estado <> 1 AND t1.id_gcombustible IN (select id_gcombustible from sgo.gec_aprobacion where id_gcombustible = t1.id_gcombustible) ");
      } else if (rolPrincipal == Rol.ROL_MODULO_TRANSPORTE){
    	  parametros.setQueryRolGec("");
      } else if (rolPrincipal == Rol.ROL_SUPERVISOR){
    	  parametros.setQueryRolGec("");
      }
      
      //Recuperar registros
      respuesta = dGuiaCombustible.recuperarRegistros(parametros);
      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  } 

  @RequestMapping(value = URL_RECUPERAR_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperaRegistro(int ID,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    GuiaCombustible guia = null;
    Contenido<GuiaCombustible> contenido=null;
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
          respuesta= dGuiaCombustible.recuperarRegistro(ID); 
          //Verifica el resultado de la accion
            if (respuesta.estado==false){         
              throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            guia = (GuiaCombustible) respuesta.contenido.carga.get(0);
            
            //Esto para recuperar la aprobación de la gec
            respuesta = dGecAprobacion.recuperarRegistroxGEC(guia.getId());
            if (respuesta.estado==false){         
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            
            if(respuesta.contenido.carga.size()>0){
            	guia.setAprobacionGec((GecAprobacion) respuesta.contenido.carga.get(0));
                //Terminamos de recuperar la aprobación para la GEC
            }
            /*else{
            	throw new Exception(gestorDiccionario.getMessage("sgo.mensajeGecSinAprobacion",null,locale));
            }*/
            
            ParametrosListar parametros = new ParametrosListar();          
            parametros.setIdTransportista(guia.getIdTransportista());
            parametros.setFiltroProducto(guia.getIdProducto());
            
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

//          Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();        
//          Using DateFormat format method we can create a string 
//          representation of a date with the defined format.
            String fechaFormateada = df.format(guia.getFechaGuiaCombustible());
            //System.out.println(fechaFormateada);
            parametros.setFiltroFechaDiaOperativo(fechaFormateada);
            parametros.setPaginacion(Constante.SIN_PAGINACION);
            parametros.setSentidoOrdenamiento("ASC"); 
            parametros.setIdGuiaCombustible(guia.getId());
            respuesta = dDetalleGec.recuperarRegistros(parametros);
            if (!respuesta.estado) {
             throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
            for(Object detalle : respuesta.contenido.carga){
             guia.agregarDetalle((DetalleGEC)detalle);
            }
           
            contenido= new Contenido<GuiaCombustible>();
            contenido.carga = new ArrayList<GuiaCombustible>();
            contenido.carga.add(guia);
            respuesta.contenido= contenido;
            respuesta.estado=true;
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
    } catch (Exception ex){
      //ex.printStackTrace();
      Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistro");
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_RECUPERAR_NUMERO_GEC_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperarNumeroGec(int ID,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    int numeroCaracteresGuia =3;
    int numeroCaracteresSerie=2;
    int numeroSerie=0;    
    int numeroGuia =0;
    String numeroGuiaCadena ="";
    String numeroSerieCadena ="";
    GuiaCombustible guia = new GuiaCombustible();
    Cliente cliente = new Cliente();
    int limiteGuia =0;
    Contenido<GuiaCombustible> contenido = new Contenido<GuiaCombustible>();
    List<GuiaCombustible> listaRegistros = new ArrayList<GuiaCombustible>();
    try {
      limiteGuia = (int) Math.pow (10,(double)numeroCaracteresGuia);
      //Recupera el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_NUMERO_GEC_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario      
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
          throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
      }
      //Recuperar el registro
      respuesta = dGuiaCombustible.recuperarNumeroGec(ID);
      if (respuesta.estado==false){         
       throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
      }      
      if (respuesta.contenido.carga.size()==0){
       numeroGuia=1;
       numeroSerie=1;
       numeroGuiaCadena=StringUtils.repeat("0", numeroCaracteresGuia) + numeroGuia;
       numeroSerieCadena = StringUtils.repeat("0", numeroCaracteresSerie) + numeroSerie;
       guia.setNumeroGEC(numeroGuiaCadena.substring(1));
       guia.setNumeroSerie(numeroSerieCadena.substring(1));
      } else {
       guia =  (GuiaCombustible) respuesta.contenido.carga.get(0);
       numeroGuia = Integer.parseInt(guia.getNumeroGEC()) ;
       numeroSerie = Integer.parseInt(guia.getNumeroSerie());
       numeroGuia ++;
       if (numeroGuia >= limiteGuia){
        numeroSerie++;
        numeroGuia=1;
       }
       numeroGuiaCadena=StringUtils.repeat("0", numeroCaracteresGuia) + numeroGuia;
       numeroSerieCadena = StringUtils.repeat("0", numeroCaracteresSerie) + numeroSerie;
       guia.setNumeroGEC(numeroGuiaCadena.substring(1));
       guia.setNumeroSerie(numeroSerieCadena.substring(1));
      }      
      //
      respuesta = dCliente.recuperarRegistro(ID);
      if (respuesta.estado==false){         
       throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
      }
      cliente = (Cliente) respuesta.contenido.carga.get(0);
      guia.setNumeroContrato(cliente.getNumeroContrato());
      guia.setDescripcionContrato(cliente.getDescripcionContrato());
      guia.setIdCliente(cliente.getId());
     //Verifica el resultado de la accion
      listaRegistros.add(guia);
      contenido.carga =  listaRegistros;
      respuesta.contenido = contenido;
     respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
    } catch (Exception ex){
      Utilidades.gestionaError(ex, sNombreClase, "recuperarNumeroGec");
      //ex.printStackTrace();
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_GUARDAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody GuiaCombustible eGuiaCombustible,HttpServletRequest peticionHttp,Locale locale){    
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    Bitacora eBitacora= null;
    String ContenidoAuditoria ="";
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    String direccionIp="";
    String ClaveGenerada="";
    try {
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dGuiaCombustible.getDataSource());
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
          eGuiaCombustible.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eGuiaCombustible.setActualizadoPor(principal.getID()); 
            eGuiaCombustible.setCreadoEl(Calendar.getInstance().getTime().getTime());
            eGuiaCombustible.setCreadoPor(principal.getID());
            eGuiaCombustible.setIpActualizacion(direccionIp);
            eGuiaCombustible.setIpCreacion(direccionIp);
            eGuiaCombustible.setEstado(Constante.ESTADO_REGISTRADO);
            respuesta= dGuiaCombustible.guardarRegistro(eGuiaCombustible);
            //Verifica si la accion se ejecuto de forma satisfactoria
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
            }
            ClaveGenerada = respuesta.valor;    
            
            //Cuando se crea una gec se debe registrar en la tabla gec_aprobacion
            GecAprobacion aprobacion = new GecAprobacion();
            aprobacion.setIdGcombustible(Integer.parseInt(ClaveGenerada));
            aprobacion.setIdUsuarioRegistrado(principal.getID());
            aprobacion.setFechaHoraRegistrado(dDiaOperativo.recuperarFechaActualTimeStamp());
            respuesta = dGecAprobacion.registrarGec(aprobacion);
            if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
            }
            
            //Luego guardamos el detalle de la GEC
            for(DetalleGEC detalleGec : eGuiaCombustible.getDetalle()){
             detalleGec.setIdGuiaCombustible(Integer.parseInt(ClaveGenerada));
             respuesta = dDetalleGec.guardarRegistro(detalleGec);
             if (respuesta.estado == false) {
              throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
             }
            }            
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
            ContenidoAuditoria =  mapper.writeValueAsString(eGuiaCombustible);
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_GUARDAR_COMPLETA);
            eBitacora.setTabla(GuiaCombustibleDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(ClaveGenerada);
            eBitacora.setContenido(ContenidoAuditoria);
            eBitacora.setRealizadoEl(eGuiaCombustible.getCreadoEl());
            eBitacora.setRealizadoPor(eGuiaCombustible.getCreadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }           
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eGuiaCombustible.getFechaCreacion().substring(0, 9),eGuiaCombustible.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
          this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      this.transaccion.rollback(estadoTransaccion);
      //ex.printStackTrace();
      Utilidades.gestionaError(ex, sNombreClase, "guardarRegistro");
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_ACTUALIZAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta actualizarRegistro(@RequestBody GuiaCombustible eGuiaCombustible,HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    Bitacora eBitacora=null;
    String direccionIp="";
    try {
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dGuiaCombustible.getDataSource());
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
          eGuiaCombustible.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eGuiaCombustible.setActualizadoPor(principal.getID()); 
            eGuiaCombustible.setIpActualizacion(direccionIp);            
            //eGuiaCombustible.setEstado(Constante.ESTADO_REGISTRADO);
            respuesta= dGuiaCombustible.actualizarRegistro(eGuiaCombustible);
            if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            
            respuesta = dDetalleGec.eliminarRegistros(eGuiaCombustible.getId());
            if (respuesta.estado==false && respuesta.error!=Constante.EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA){           
             throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            
            for(DetalleGEC detalleGec : eGuiaCombustible.getDetalle()){
             detalleGec.setIdGuiaCombustible(eGuiaCombustible.getId());

             respuesta = dDetalleGec.guardarRegistro(detalleGec);
             if (respuesta.estado == false) {
              throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
             }
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(GuiaCombustibleDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eGuiaCombustible.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eGuiaCombustible));
            eBitacora.setRealizadoEl(eGuiaCombustible.getActualizadoEl());
            eBitacora.setRealizadoPor(eGuiaCombustible.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eGuiaCombustible.getFechaActualizacion().substring(0, 9),eGuiaCombustible.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
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
  
  @RequestMapping(value = URL_EMITIR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta emitirGuia(@RequestBody GuiaCombustible eGuiaCombustible,HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    ParametrosListar parametros = null;
    ParametrosListar parametros2 = null;
    Bitacora eBitacora=null;
    String direccionIp="";
    ArrayList<String> file=new ArrayList<String>();
    
    try {
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dGuiaCombustible.getDataSource());
      definicionTransaccion = new DefaultTransactionDefinition();
      estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
      eBitacora = new Bitacora();
      //Recuperar el usuario actual
      principal = this.getCurrentUser();
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_EMITIR_COMPLETA);
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
          eGuiaCombustible.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eGuiaCombustible.setActualizadoPor(principal.getID()); 
            eGuiaCombustible.setIpActualizacion(direccionIp);
            eGuiaCombustible.setEstado(Constante.ESTADO_EMITIDO);
            respuesta= dGuiaCombustible.actualizaEstado(eGuiaCombustible);
            if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(GuiaCombustibleDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eGuiaCombustible.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eGuiaCombustible));
            eBitacora.setRealizadoEl(eGuiaCombustible.getActualizadoEl());
            eBitacora.setRealizadoPor(eGuiaCombustible.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eGuiaCombustible.getFechaActualizacion().substring(0, 9),eGuiaCombustible.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
          
          //Emitimos la gec en la tabla gec_aprobacion
          GecAprobacion aprobacion = new GecAprobacion();
          respuesta= dGecAprobacion.recuperarRegistroxGEC(eGuiaCombustible.getId());
          if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
          }
          if(respuesta.getContenido().getCarga().size() > 0){
        	  aprobacion = (GecAprobacion) respuesta.getContenido().getCarga().get(0);          
              //aprobacion.setIdGcombustible(eGuiaCombustible.getId());
              //aprobacion.setId(eGuiaCombustible.getAprobacionGec().getId());
              aprobacion.setIdUsuarioEmitido(principal.getID());
              aprobacion.setFechaHoraEmitido(dDiaOperativo.recuperarFechaActualTimeStamp());
              respuesta = dGecAprobacion.emitirGec(aprobacion);
          } else {
        	  throw new Exception("No se encontró registro de ésta GEC para su emisión.");
          }
          
          if (respuesta.estado == false) {
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
          }
         
          respuesta = dGuiaCombustible.recuperarRegistro(eGuiaCombustible.getId());
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
          
          eGuiaCombustible = (GuiaCombustible) respuesta.getContenido().getCarga().get(0);  
          
          parametros = new ParametrosListar();
          parametros.setIdGuiaCombustible(eGuiaCombustible.getId()); 
          respuesta = dDetalleGec.recuperarRegistros(parametros);
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
          
          DetalleGEC eDetalleGEC = (DetalleGEC) respuesta.getContenido().getCarga().get(0); 
          
          respuesta = dCliente.recuperarRegistro(eGuiaCombustible.getIdCliente());
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
          
          eGuiaCombustible.setCliente((Cliente) respuesta.getContenido().getCarga().get(0)); 
          
          respuesta = dOperacion.recuperarRegistro(eGuiaCombustible.getIdOperacion());
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
          
          eGuiaCombustible.setOperacion((Operacion) respuesta.getContenido().getCarga().get(0));   
          
          //inicio
          
          String correos = "";
          parametros2 = new ParametrosListar();
          parametros2.setIdCliente(eGuiaCombustible.getCliente().getId());
          parametros2.setFiltroRol(7);
          respuesta = dUsuario.recuperarRegistros(parametros2);          
          if (respuesta.estado == false) {
          	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
          }
          
          List<Usuario> listausuarios = new ArrayList<Usuario>();
          listausuarios = (ArrayList<Usuario>) respuesta.getContenido().getCarga();
          
          if(!listausuarios.isEmpty()){
  			for (Usuario usuario : listausuarios) {
  				if (!usuario.getEmail().trim().isEmpty()) { 
  					//System.out.println("correos inicio:" + correos);
  					correos = correos + usuario.getEmail() + ";";
  					//System.out.println("correos final:" + correos);
				}  				
			 }
	  	  }	  
          
          System.out.println("correos:" + correos);
          eGuiaCombustible.getOperacion().setCorreoPara(correos);
          
          //fin
          
          
          parametros.setFiltroUsuario(principal.getNombre() + " - " + principal.getIdentidad()); 
          respuesta.estado = dMailNotifica.enviarMailNotificacionGECEmitido(parametros,eGuiaCombustible,eDetalleGEC);          
          if(respuesta.estado){
       	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionExitosa", null, locale);
          } else{
       	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
       	   throw new Exception(gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale));
          }
          
          this.transaccion.commit(estadoTransaccion);
          
       
    } catch (Exception ex){
      //ex.printStackTrace();
      Utilidades.gestionaError(ex, sNombreClase, "emitirGuia");
      this.transaccion.rollback(estadoTransaccion);
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_APROBAR_RELATIVA, method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta aprobarGuia(@RequestBody GecAprobacion eGecAprobacion,HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    ParametrosListar parametros = null;
    Bitacora eBitacora=null;
    String direccionIp="";
    try {
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dGuiaCombustible.getDataSource());
      definicionTransaccion = new DefaultTransactionDefinition();
      estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
      eBitacora = new Bitacora();
      //Recuperar el usuario actual
      principal = this.getCurrentUser();
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_APROBAR_COMPLETA);
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
      
      if (eGecAprobacion.getEstado() == GuiaCombustible.ESTADO_OBSERVADO && eGecAprobacion.getObservacionCliente().isEmpty()){           
        throw new Exception("Debe especificar en el campo Comentarios la razón por la cual se rechaza la GEC. Favor, verifique.");
      }
      
      if (eGecAprobacion.getEstado() == GuiaCombustible.ESTADO_OBSERVADO && eGecAprobacion.getObservacionCliente().length()<6){           
          throw new Exception("El campo observación debe tener como mínimo 06 caracteres. Favor, verifique.");
        }
      
      		GuiaCombustible eGuiaCombustible = new GuiaCombustible();
      		eGuiaCombustible.setId(eGecAprobacion.getIdGcombustible());
	      	eGuiaCombustible.setEstado(eGecAprobacion.getEstado());
            eGuiaCombustible.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eGuiaCombustible.setActualizadoPor(principal.getID()); 
            eGuiaCombustible.setIpActualizacion(direccionIp);
            respuesta= dGuiaCombustible.actualizaEstado(eGuiaCombustible);
            if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(GuiaCombustibleDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eGuiaCombustible.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eGuiaCombustible));
            eBitacora.setRealizadoEl(eGuiaCombustible.getActualizadoEl());
            eBitacora.setRealizadoPor(eGuiaCombustible.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eGuiaCombustible.getFechaActualizacion().substring(0, 9),eGuiaCombustible.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
          
          //Emitimos la gec en la tabla gec_aprobacion
         // GecAprobacion aprobacion = new GecAprobacion();
         // aprobacion.setIdGcombustible(eGuiaCombustible.getId());
         // aprobacion.setId(eGuiaCombustible.getAprobacionGec().getId());
          eGecAprobacion.setIdUsuarioAprobado(principal.getID());
          eGecAprobacion.setFechaHoraAprobado(dDiaOperativo.recuperarFechaActualTimeStamp());
         // aprobacion.setEstado(eGuiaCombustible.getEstado());
         // aprobacion.setObservacionCliente(eGuiaCombustible.getAprobacionGec().getObservacionCliente());
          respuesta = dGecAprobacion.aprobarGec(eGecAprobacion);
          if (respuesta.estado == false) {
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
          }
          
          respuesta = dGuiaCombustible.recuperarRegistro(eGecAprobacion.getIdGcombustible());
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }
          
          eGuiaCombustible = (GuiaCombustible) respuesta.getContenido().getCarga().get(0);  
          
          parametros = new ParametrosListar();
          parametros.setIdGuiaCombustible(eGecAprobacion.getIdGcombustible()); 
          respuesta = dDetalleGec.recuperarRegistros(parametros);
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }          
          DetalleGEC eDetalleGEC = (DetalleGEC) respuesta.getContenido().getCarga().get(0); 
          
          //para recuperar GEC Aprobacion
          respuesta = dGecAprobacion.recuperarRegistro(eGecAprobacion.getId());
          if (respuesta.estado == false) {
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
            }          
          eGecAprobacion = (GecAprobacion) respuesta.getContenido().getCarga().get(0);
          
          eGuiaCombustible.setAprobacionGec(eGecAprobacion);
          parametros = new ParametrosListar();
          parametros.setFiltroUsuario(principal.getNombre() + " - " + principal.getIdentidad()); 
          
          if (eGecAprobacion.getEstado()==GuiaCombustible.ESTADO_APROBADO) {
        	  respuesta.estado = dMailNotifica.enviarMailNotificacionGECAprobado(parametros,eGuiaCombustible,eDetalleGEC);     
          }else if (eGecAprobacion.getEstado()==GuiaCombustible.ESTADO_OBSERVADO) {
        	  respuesta.estado = dMailNotifica.enviarMailNotificacionGECObservado(parametros,eGuiaCombustible,eDetalleGEC);      
          }
          
          if(respuesta.estado){
       	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionExitosa", null, locale);
          } else{
       	   respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
       	   throw new Exception(gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale));
          }
          
          this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      //ex.printStackTrace();
      Utilidades.gestionaError(ex, sNombreClase, "emitirGuia");
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
   String direccionIp = "";
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
    
    if (httpRequest.getParameter("filtroIdGuiaCombustible") != null) {
 	    parametros.setIdGuiaCombustible(Integer.parseInt((httpRequest.getParameter("filtroIdGuiaCombustible"))));
    }
   
    oRespuesta=dGuiaCombustible.recuperarRegistro(parametros.getIdGuiaCombustible());  
    if (oRespuesta.estado == false) {
 	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
    }
    GuiaCombustible eGuiaCombustible = (GuiaCombustible)  oRespuesta.contenido.getCarga().get(0);
    
    oRespuesta = dDetalleGec.recuperarRegistros(parametros);
    if (oRespuesta.estado == false) {
      	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
      }
    
    DetalleGEC eDetalleGEC = (DetalleGEC) oRespuesta.getContenido().getCarga().get(0);
    
    ArrayList<DetalleGEC> aDetalleGEC = (ArrayList<DetalleGEC>) oRespuesta.getContenido().getCarga();
    
    eGuiaCombustible.setDetalle(aDetalleGEC);
   
    /* 
     RespuestaCompuesta respuestaC = dDiaOperativo.recuperarRegistro(parametros.getFiltroDiaOperativo());
    DiaOperativo eDiaOperativoRep = (DiaOperativo) respuestaC.contenido.getCarga().get(0);  
    String razonSocial= eDiaOperativoRep.getOperacion().getCliente().getRazonSocial(); 
     parametros.setFiltroNombreCliente(razonSocial);//razon social*/
     ArrayList<String> file=new ArrayList<String>();   
    /*SimpleDateFormat sdfArchivo = new SimpleDateFormat("ddMMyyyy");
     String fileName=Constante.PREFIJO_ARCHIVO_PROGRAMACION+"_"+eDiaOperativoRep.getOperacion().getCliente().getNombreCorto().trim().toLowerCase()
 		   +eDiaOperativoRep.getOperacion().getId()
 		   +sdfArchivo.format(eDiaOperativoRep.getFechaEstimadaCarga())+".xls";
    
    
      ParametrosListar paramListar= new ParametrosListar();   
    paramListar.setFiltroParametro(Parametro.ALIAS_DIRECTORIO_ARCHIVOS);
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
    File fileXLS=new File(directorio+File.separator+fileName);
    String path_archivo=null;
    if(!fileXLS.exists()){	  
 	    path_archivo=generarArchivo(parametros.getFiltroDiaOperativo(),locale,directorio);		   
 	   //throw new Exception(gestorDiccionario.getMessage("sgo.programacion.NoEncontroArchivo", null, locale));
    }else{
 	   path_archivo=fileXLS.getAbsolutePath();	   
    }
    file.add(path_archivo);
*/
    //enviamos los parametros del correo, el listado de usuarios con rol transportista y el listado de usuarioscon rol modulo transporte
    //respuesta.estado = dMailNotifica.enviarMailModuloProgramacion(parametros,file);
     
     ParametrosListar paramListar= new ParametrosListar();   
     paramListar.setFiltroParametro(Parametro.ALIAS_DIRECTORIO_ARCHIVOS);   
     oRespuesta=dParametro.recuperarRegistros(paramListar);
     if (oRespuesta.estado == false) {
  	    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
     }
     Parametro eParametro = (Parametro)  oRespuesta.contenido.getCarga().get(0);  
     if(eParametro==null || eParametro.getValor()==null || eParametro.getValor().length()==0){
  	   throw new Exception(gestorDiccionario.getMessage("sgo.directorioArchivo.noExiste", null, locale));
     }   
     String directorio_archivo=eParametro.getValor().trim();//.toLowerCase(); 
     
     ByteArrayOutputStream baos = null;
     ReporteGec uReporteador = new ReporteGec();
     uReporteador.setRutaServlet(servletContext.getRealPath("/"));
     String titulo4= eGuiaCombustible.getNumeroGuia();   
     String tituloReporte="GUIA DE ENTREGA DE COMBUSTIBLES" ;
     baos = uReporteador.generarReporteGec(titulo4,tituloReporte,  principal.getIdentidad(),eGuiaCombustible, locale);
    
     InputStream is = new ByteArrayInputStream(baos.toByteArray());
     File tempFile = new File(directorio_archivo+"/GuiaCombustible"+ eGuiaCombustible.getNumeroSerie() + "-" + eGuiaCombustible.getNumeroGEC() +".pdf"); 
     tempFile.deleteOnExit();
    
    	 FileOutputStream out = new FileOutputStream(tempFile);
         IOUtils.copy(is, out);
         out.flush();
         is.close();
         out.close();
         
         String path = tempFile.getAbsolutePath();
         file.add(path);
     
    //TODO
    parametros.setFiltroUsuario(principal.getIdentidad()); 
    respuesta.estado = dMailNotifica.enviarMailNotificacionGEC(parametros,eGuiaCombustible,eDetalleGEC,file);
    
    if(oRespuesta.estado){
    	respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionExitosa", null, locale); 	  
    }
    else{
    	respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
 	   throw new Exception(gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale));
    }
   } catch (Exception ex) {
    //ex.printStackTrace();
 	  Utilidades.gestionaError(ex, sNombreClase, "enviarMailNotificacionGEC");
    respuesta.estado = false;
    respuesta.mensaje = gestorDiccionario.getMessage("sgo.NotificacionNOExitosa", null, locale);
   }
   return respuesta;
  }
  
  


  
  @RequestMapping(value = URL_REPORTE_GEC_RELATIVA, method = RequestMethod.GET)
  public void mostrarReporteGec(HttpServletRequest httpRequest, HttpServletResponse response, Locale locale) {
   RespuestaCompuesta respuesta = null;
   ParametrosListar parametros = null;
   AuthenticatedUserDetails principal = null;
   String mensajeRespuesta = "";
   String tituloReporte ="";
   String formatoReporte="pdf";
   try {
    // Recuperar el usuario actual
    principal = this.getCurrentUser();
    // Recuperar el enlace de la accion
    respuesta = dEnlace.recuperarRegistro(URL_REPORTE_GEC_COMPLETA  );
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
   
    if (httpRequest.getParameter("formato") != null) {
     formatoReporte=((httpRequest.getParameter("formato")));
    }

    if (httpRequest.getParameter("idGuiaCombustible") != null) {
     parametros.setIdGuiaCombustible(Integer.parseInt((httpRequest.getParameter("idGuiaCombustible"))));
    } else {
     throw new Exception("No se ingreso el id de Guia de Combustible");
    }
    
    respuesta=dGuiaCombustible.recuperarRegistro(parametros.getIdGuiaCombustible());    
    if (respuesta.estado==false){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    GuiaCombustible guia = (GuiaCombustible) respuesta.contenido.carga.get(0);
    
    //Esto para recuperar la aprobación de la gec
    respuesta = dGecAprobacion.recuperarRegistroxGEC(guia.getId());
    if (respuesta.estado==false){         
    	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
    }
    guia.setAprobacionGec((GecAprobacion) respuesta.contenido.carga.get(0));
    //Terminamos de recuperar la aprobación para la GEC
    
    parametros.setIdGuiaCombustible(guia.getId());
    respuesta = dDetalleGec.recuperarRegistros(parametros);
    if (respuesta.estado==false){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    int numeroElementos = respuesta.contenido.carga.size();
    DetalleGEC detalle=null;
    for (int contador=0;contador<numeroElementos; contador++){
     detalle = (DetalleGEC) respuesta.contenido.carga.get(contador);
     guia.agregarDetalle(detalle);
    }

   /* respuesta = dDiaOperativo.recuperarRegistro(parametros.getFiltroDiaOperativo());
    if (respuesta.estado==false){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    if (respuesta.contenido.getCarga().size() < 1){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    DiaOperativo eDiaOperativo = (DiaOperativo) respuesta.contenido.getCarga().get(0);   
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fechaOperativa = sdf.format(eDiaOperativo.getFechaOperativa()); 
    
    respuesta = dOperacion.recuperarRegistro(eDiaOperativo.getIdOperacion());
    if (respuesta.estado==false){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }
    if (respuesta.contenido.getCarga().size() < 1){
     mensajeRespuesta = gestorDiccionario.getMessage("sgo.noListadoRegistros", null, locale);
     throw new Exception(mensajeRespuesta);
    }*/
    //Operacion operacion = (Operacion) respuesta.contenido.carga.get(0);
    String titulo4= guia.getNumeroGuia();
      //operacion.getCliente().getRazonSocial()+ " - " +operacion.getNombre();
    
    tituloReporte="GUIA DE ENTREGA DE COMBUSTIBLES" ;
    ByteArrayOutputStream baos = null;
    ReporteGec uReporteador = new ReporteGec();
    uReporteador.setRutaServlet(servletContext.getRealPath("/"));
    
    if (formatoReporte.equals(Reporteador.FORMATO_PDF)){
    	//TODO
     baos = uReporteador.generarReporteGec(titulo4,tituloReporte,  principal.getIdentidad(),guia, locale);
     response.setHeader("Content-Disposition", "inline; filename=\"reporte-gec.pdf\"");
     response.setDateHeader("Expires", -1);
     response.setContentType("application/pdf");   
     response.setContentLength(baos.size());
     response.getOutputStream().write(baos.toByteArray());
     response.getOutputStream().flush();
    } else if (formatoReporte.equals(Reporteador.FORMATO_CSV)){
     
     response.setContentType("text/csv");
     response.setHeader("Content-Disposition", "attachment;filename=\"reporte-cierre.csv\"");
     String contenidoCSV="";
     //contenidoCSV=uReporteador.generarReporteListadoCSV( hmRegistros, listaCampos, listaCamposCabecera);
     response.getOutputStream().write(contenidoCSV.getBytes());
     response.getOutputStream().flush();
    }

   } catch (Exception ex) {
	   Utilidades.gestionaError(ex, sNombreClase, "mostrarReporteGec");
   }
  }
  
  private  AuthenticatedUserDetails getCurrentUser(){
    return  (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  
  @RequestMapping(value = URL_VERIFICA_NOTIFICAR_RELATIVA, method = RequestMethod.GET)
  public @ResponseBody
  RespuestaCompuesta verificarPermisoNotificacion(int ID, Locale locale) {
   RespuestaCompuesta respuesta = null;
   AuthenticatedUserDetails principal = null;

   try {
    principal = this.getCurrentUser();
    respuesta = dEnlace.recuperarRegistro(URL_VERIFICA_NOTIFICAR_COMPLETA);
    if (respuesta.estado == false) {
     throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
    }
    Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
    if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
     throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
    }    
     
   } catch (Exception ex) {
    //ex.printStackTrace();
 	  Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistroNotificacion");
    respuesta.estado = false;
    respuesta.contenido = null;
    respuesta.mensaje = ex.getMessage();
   }
   return respuesta;
  }
  
}
