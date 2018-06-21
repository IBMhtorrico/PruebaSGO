package sgo.servicio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.AforoTanqueDao;
import sgo.datos.BitacoraDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.OperacionDao;
import sgo.datos.TanqueDao;
import sgo.datos.TanqueJornadaDao;
import sgo.entidad.AforoTanque;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.Enlace;
import sgo.entidad.Estacion;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Formula;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AforoTanqueControlador {
  @Autowired
  private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
  @Autowired
  private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
  @Autowired
  private EnlaceDao dEnlace;
  @Autowired
  private MenuGestor menu;
  @Autowired
  private AforoTanqueDao dAforoTanque;
  @Autowired
  private DiaOperativoDao dDiaOperativo;
  @Autowired
  private TanqueDao dTanque;
  @Autowired
  private OperacionDao dOperacion;
  @Autowired
  private TanqueJornadaDao dTanqueJornada;
  //
  private DataSourceTransactionManager transaccion;//Gestor de la transaccion
  //urls generales
  private static final String URL_INTERPOLAR_RELATIVA="/aforo-tanque/interpolacion";
  private static final String URL_GESTION_COMPLETA="/admin/aforo-tanque";
  private static final String URL_GESTION_RELATIVA="/aforo-tanque";
  private static final String URL_GUARDAR_COMPLETA="/admin/aforo-tanque/crear";
  private static final String URL_GUARDAR_RELATIVA="/aforo-tanque/crear";
  private static final String URL_LISTAR_COMPLETA="/admin/aforo-tanque/listar";
  private static final String URL_LISTAR_RELATIVA="/aforo-tanque/listar";
  private static final String URL_ELIMINAR_COMPLETA="/admin/aforo-tanque/eliminar";
  private static final String URL_ELIMINAR_RELATIVA="/aforo-tanque/eliminar";
  private static final String URL_ACTUALIZAR_COMPLETA="/admin/aforo-tanque/actualizar";
  private static final String URL_ACTUALIZAR_RELATIVA="/aforo-tanque/actualizar";
  private static final String URL_RECUPERAR_COMPLETA="/admin/aforo-tanque/recuperar";
  private static final String URL_RECUPERAR_RELATIVA="/aforo-tanque/recuperar";
  
  private static final String URL_CARGAR_ARCHIVO_COMPLETA="/admin/aforo-tanque/cargar-archivo";
  private static final String URL_CARGAR_ARCHIVO_RELATIVA="/aforo-tanque/cargar-archivo";
  private static final String SEPARADOR_CSV=",";
  
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
    mapaValores.put("MENSAJE_CARGANDO_EXITOSO", gestorDiccionario.getMessage("sgo.listarExitoso", null, locale));

   } catch (Exception ex) {

   }
   return mapaValores;
  }
  
  @RequestMapping("/aforo-tanque")
  public ModelAndView mostrarFormulario( Locale locale){
    ModelAndView vista =null;
    AuthenticatedUserDetails principal = null;
    ArrayList<Enlace> listaEnlaces = null;
    ArrayList<Tanque> listaTanques = null;
    RespuestaCompuesta respuesta = null;
    HashMap<String, String> mapaValores = null;
    ParametrosListar parametros=null;
    ArrayList<?> listaOperaciones = null;
    try {
      principal = this.getCurrentUser();
      respuesta = menu.Generar(principal.getRol().getId(),URL_GESTION_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado",null,locale));
      }      
      listaEnlaces = (ArrayList<Enlace>) respuesta.contenido.carga;
      
      parametros = new ParametrosListar();
      parametros.setPaginacion(Constante.SIN_PAGINACION);
      parametros.setFiltroEstado(Constante.FILTRO_TODOS);
      parametros.setIdCliente(principal.getCliente().getId());
      parametros.setIdOperacion(principal.getOperacion().getId());
      respuesta = dOperacion.recuperarRegistros(parametros);
      if (respuesta.estado == false) {
       throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
      }
      listaOperaciones = (ArrayList<?>) respuesta.contenido.carga;
      
      mapaValores = recuperarMapaValores(locale);
      vista = new ModelAndView("plantilla");
      vista.addObject("vistaJSP","mantenimiento/aforo_tanque.jsp");
      vista.addObject("vistaJS","mantenimiento/aforo_tanque.js");
      vista.addObject("identidadUsuario",principal.getIdentidad());
      vista.addObject("mapaValores", mapaValores);
      vista.addObject("operaciones", listaOperaciones);
 //     vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
      vista.addObject("menu",listaEnlaces);
    } catch(Exception ex){
      
    }
    return vista;
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
      
      if (httpRequest.getParameter("filtroTanque") != null && !httpRequest.getParameter("filtroTanque").isEmpty()) {
        parametros.setIdTanque(Integer.parseInt( httpRequest.getParameter("filtroTanque")));
      }
      if (httpRequest.getParameter("filtroCentimetros") != null && !httpRequest.getParameter("filtroCentimetros").isEmpty()) {
    	    parametros.setFiltroCentimetros(Integer.parseInt(httpRequest.getParameter("filtroCentimetros")));
      }

      //Recuperar registros
      respuesta = dAforoTanque.recuperarRegistros(parametros);
      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  } 
  
  @RequestMapping(value = URL_INTERPOLAR_RELATIVA ,method = RequestMethod.GET)
  public @ResponseBody RespuestaCompuesta recuperarInterpolacion(HttpServletRequest httpRequest, Locale locale){
    RespuestaCompuesta respuesta = null;
    ParametrosListar parametros= null;
    AuthenticatedUserDetails principal = null;
    String mensajeRespuesta="";
    int centimetros=0;
    AforoTanque inmediatoSuperior=new AforoTanque();
    AforoTanque inmediatoInferior=new AforoTanque();
    
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
      
      if (httpRequest.getParameter("filtroTanque") != null && !httpRequest.getParameter("filtroTanque").isEmpty()) {
    	  if (httpRequest.getParameter("filtroModulo") != null && !httpRequest.getParameter("filtroModulo").isEmpty() && httpRequest.getParameter("filtroModulo").equals("JORNADA")) { 
    		  respuesta = dTanqueJornada.recuperarRegistro(Integer.parseInt( httpRequest.getParameter("filtroTanque")));
    		  if (respuesta.estado == false) {
                  throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
              }
    		  TanqueJornada tanqueJornada = (TanqueJornada) respuesta.getContenido().getCarga().get(0);
    		  parametros.setIdTanque(tanqueJornada.getIdTanque());  
    	  }        
      }
      if (httpRequest.getParameter("filtroCentimetros") != null && !httpRequest.getParameter("filtroCentimetros").isEmpty()) {
    	    parametros.setFiltroCentimetros(Integer.parseInt(httpRequest.getParameter("filtroCentimetros")));
    	    centimetros = Integer.parseInt(httpRequest.getParameter("filtroCentimetros"));
      }

      //Recuperar registros
      respuesta = dAforoTanque.recuperarRegistros(parametros);
      // Verifica el resultado de la accion
      if (respuesta.estado == false) {
          throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
      } 
      if(respuesta.contenido.carga.size() == 0){
    	  parametros.setFiltroCentimetros(0);
    	  parametros.setCampoOrdenamiento("centimetros");
    	  parametros.setTxtQuery(" t1.centimetros > " + centimetros + " ");
    	  parametros.setSentidoOrdenamiento("ASC");
    	  //Recuperamos el registro inmediatoSuperior
          respuesta = dAforoTanque.recuperarRegistros(parametros);
          if (respuesta.estado == false) {
              throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
          }
          if(respuesta.contenido.carga.size() > 0){
        	  inmediatoSuperior = (AforoTanque) respuesta.getContenido().getCarga().get(0);
        	  
        	  //Recuperamos el registro imediatoInferior
        	  parametros.setFiltroCentimetros(0);
        	  parametros.setCampoOrdenamiento("centimetros");
        	  parametros.setTxtQuery(" t1.centimetros < " + centimetros + " ");
        	  parametros.setSentidoOrdenamiento("DESC");
              respuesta = dAforoTanque.recuperarRegistros(parametros);
              if (respuesta.estado == false) {
                  throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
              }
              if(respuesta.contenido.carga.size() > 0){
            	  inmediatoInferior = (AforoTanque) respuesta.getContenido().getCarga().get(0);
            	  
            	  double medida = centimetros;
            	  double medidaInferiorInmediata = inmediatoInferior.getCentimetros();
            	  double medidaSuperiorInmediata = inmediatoSuperior.getCentimetros();
            	  double volumenInferiorInmediata = inmediatoInferior.getVolumen();
            	  double volumenSuperiorInmediata = inmediatoSuperior.getVolumen();
            	  
            	  Formula formula = new Formula();
            	  double volumen = formula.calcularInterpolacion(medidaInferiorInmediata, medidaSuperiorInmediata, volumenInferiorInmediata, volumenSuperiorInmediata, medida);

            	  AforoTanque aforoFinal = new AforoTanque();
            	  aforoFinal.setCentimetros(new Float(medida));
            	  aforoFinal.setVolumen(new Float(volumen));
            	  
            	  Contenido<AforoTanque> contenido = new Contenido<AforoTanque>();
            	  List<AforoTanque> listaRegistros = new ArrayList<AforoTanque>();
            	  listaRegistros.add(aforoFinal);
            	  contenido.carga = listaRegistros;
            	  respuesta.estado = true;
                  respuesta.contenido = contenido;
              }
 
          }
      }

      respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
    } catch(Exception ex){
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  } 
  
  

  @RequestMapping(value = URL_CARGAR_ARCHIVO_RELATIVA+"/{idTanque}/{borrar}" ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta cargarArchivo(@RequestParam(value="file") MultipartFile file,
    @PathVariable("idTanque") String idTanque,
    @PathVariable("borrar") int borrar,
    HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    BufferedReader reader = null;
    AforoTanque aforo= null;
    String direccionIp="";
    String ClaveGenerada="";
    ArrayList<String> listaAlturas=null;
    ArrayList<String>  listaIds=null;
    ArrayList<String> listaAlturasNoProcesadas = null;
    try {
    	
      //Recupera el usuario actual
      principal = this.getCurrentUser(); 
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_CARGAR_ARCHIVO_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario      
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
          throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
      }
      
      InputStream inputStream = file.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String linea;
      direccionIp = peticionHttp.getHeader("X-FORWARDED-FOR");  
      if (direccionIp == null) {  
        direccionIp = peticionHttp.getRemoteAddr();  
      }
      String[] columnas= null;
      int altura=0;
      float volumen=0;
      int numeroLineas = 0;
      listaAlturas = new ArrayList<String>();
      listaIds = new ArrayList<String>();
      listaAlturasNoProcesadas= new ArrayList<String>();
      while ((linea = bufferedReader.readLine()) != null)
      {
       if (numeroLineas > 0){
        columnas= linea.split(SEPARADOR_CSV);
        altura = Integer.parseInt(columnas[1]);
        volumen= Float.parseFloat(columnas[2]);
        aforo = new AforoTanque();
        aforo.setIdTanque(Integer.parseInt(idTanque));
        aforo.setCentimetros(altura);
        aforo.setVolumen(volumen);
        aforo.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        aforo.setActualizadoPor(principal.getID()); 
        aforo.setCreadoEl(Calendar.getInstance().getTime().getTime());
        aforo.setCreadoPor(principal.getID());
        aforo.setIpActualizacion(direccionIp);
        aforo.setIpCreacion(direccionIp);
        respuesta= dAforoTanque.guardarRegistro(aforo);
        if (respuesta.estado==false){       
         //throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
         listaAlturasNoProcesadas.add(columnas[1]);
        }        
        ClaveGenerada = respuesta.valor;
        listaAlturas.add(columnas[1]);
        listaIds.add(ClaveGenerada);
       }
       numeroLineas++;
      }
      if (borrar==1){
       listaAlturas=null;
      }
      respuesta = dAforoTanque.eliminarRegistros(listaIds, listaAlturas,Integer.parseInt(idTanque));      
      respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
    } catch (Exception ex){
      ex.printStackTrace();
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
          respuesta= dAforoTanque.recuperarRegistro(ID);
          //Verifica el resultado de la accion
            if (respuesta.estado==false){         
              throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
    } catch (Exception ex){
      ex.printStackTrace();
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_GUARDAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody AforoTanque eAforoTanque,HttpServletRequest peticionHttp,Locale locale){    
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
      Respuesta validacion = Utilidades.validacionXSS(eAforoTanque, gestorDiccionario, locale);
      if (validacion.estado == false) {
        throw new Exception(validacion.valor);
      }
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dAforoTanque.getDataSource());
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
          eAforoTanque.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eAforoTanque.setActualizadoPor(principal.getID()); 
            eAforoTanque.setCreadoEl(Calendar.getInstance().getTime().getTime());
            eAforoTanque.setCreadoPor(principal.getID());
            eAforoTanque.setIpActualizacion(direccionIp);
            eAforoTanque.setIpCreacion(direccionIp);
/*            if (eAforoTanque.validar() == false) {
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
            }
*/            respuesta= dAforoTanque.guardarRegistro(eAforoTanque);
            //Verifica si la accion se ejecuto de forma satisfactoria
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
            }
            ClaveGenerada = respuesta.valor;
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
            ContenidoAuditoria =  mapper.writeValueAsString(eAforoTanque);
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_GUARDAR_COMPLETA);
            eBitacora.setTabla(AforoTanqueDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(ClaveGenerada);
            eBitacora.setContenido(ContenidoAuditoria);
            eBitacora.setRealizadoEl(eAforoTanque.getCreadoEl());
            eBitacora.setRealizadoPor(eAforoTanque.getCreadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }           
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eAforoTanque.getFechaCreacion().substring(0, 9),eAforoTanque.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
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
  
  @RequestMapping(value = URL_ACTUALIZAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta actualizarRegistro(@RequestBody AforoTanque eAforoTanque,HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    Bitacora eBitacora=null;
    String direccionIp="";
    try {
	  //valida los datos que vienen del formulario
      Respuesta validacion = Utilidades.validacionXSS(eAforoTanque, gestorDiccionario, locale);
      if (validacion.estado == false) {
        throw new Exception(validacion.valor);
      }
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dAforoTanque.getDataSource());
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
          eAforoTanque.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eAforoTanque.setActualizadoPor(principal.getID()); 
            eAforoTanque.setIpActualizacion(direccionIp);
           /* if (eAforoTanque.validar() == false) {
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
            }*/
            respuesta= dAforoTanque.actualizarRegistro(eAforoTanque);
            if (respuesta.estado==false){           
              throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(AforoTanqueDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eAforoTanque.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eAforoTanque));
            eBitacora.setRealizadoEl(eAforoTanque.getActualizadoEl());
            eBitacora.setRealizadoPor(eAforoTanque.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
          respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eAforoTanque.getFechaActualizacion().substring(0, 9),eAforoTanque.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
          this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      ex.printStackTrace();
      this.transaccion.rollback(estadoTransaccion);
      respuesta.estado=false;
      respuesta.contenido = null;
      respuesta.mensaje=ex.getMessage();
    }
    return respuesta;
  }
  
  @RequestMapping(value = URL_ELIMINAR_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta eliminarRegistro(HttpServletRequest peticionHttp,Locale locale){
    RespuestaCompuesta respuesta = null;
    AuthenticatedUserDetails principal = null;
    int idRegistro=0;
    Bitacora eBitacora=null;
    AforoTanque eAforoTanque= null;
    TransactionDefinition definicionTransaccion = null;
    TransactionStatus estadoTransaccion = null;
    try {   
      //Inicia la transaccion
      this.transaccion = new DataSourceTransactionManager(dAforoTanque.getDataSource());
      definicionTransaccion = new DefaultTransactionDefinition();
      estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);     
      //Recuperar el usuario actual
      principal = this.getCurrentUser();
      //Recuperar el enlace de la accion
      respuesta = dEnlace.recuperarRegistro(URL_ELIMINAR_COMPLETA);
      if (respuesta.estado==false){
        throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
      }
      Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
      //Verificar si cuenta con el permiso necesario      
      if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
        throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
        }     
      //Verificar si el id ha sido enviado
      if (peticionHttp.getParameter("ID") != null) {
        idRegistro=(Integer.parseInt( peticionHttp.getParameter("ID")));
      }
      if (idRegistro == 0 ){
        throw new Exception(gestorDiccionario.getMessage("sgo.eliminarFallidoNoId",null,locale));
      }
      //Verificar la existencia del registro
      respuesta= dAforoTanque.recuperarRegistro(idRegistro);
      if (respuesta.estado==false){
        if (idRegistro == 0 ){
          throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
      }
      //Asignar el registro
      eAforoTanque = (AforoTanque) respuesta.contenido.carga.get(0);      
          //Eliminar el registro
      
          respuesta= dAforoTanque.eliminarRegistro(idRegistro);
            if (respuesta.estado==false){             
              throw new Exception(gestorDiccionario.getMessage("sgo.eliminarFallido",null,locale));
            }   
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora = new Bitacora();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ELIMINAR_COMPLETA);
            eBitacora.setTabla(AforoTanqueDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eAforoTanque.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eAforoTanque));
            eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
            eBitacora.setRealizadoPor(principal.getID());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){       
                throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            } 
            //Asignar el mensaje de confirmacion
            respuesta.mensaje=gestorDiccionario.getMessage("sgo.eliminarExitoso",new Object[] { principal.getIdentidad(), eBitacora.getFechaRealizacion().substring(0, 9),eBitacora.getFechaRealizacion().substring(10) },locale);
            this.transaccion.commit(estadoTransaccion);
    } catch (Exception ex){
      ex.printStackTrace();
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
