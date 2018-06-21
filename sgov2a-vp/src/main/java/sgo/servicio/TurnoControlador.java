package sgo.servicio;


import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.AsignacionDao;
import sgo.datos.BitacoraDao;
import sgo.datos.CisternaDao;
import sgo.datos.ClienteDao;
import sgo.datos.CompartimentoDao;
import sgo.datos.ContometroJornadaDao;
import sgo.datos.DetalleProgramacionDao;
import sgo.datos.DetalleTurnoDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EstacionDao;
import sgo.datos.EventoDao;
import sgo.datos.JornadaDao;
import sgo.datos.OperacionDao;
import sgo.datos.PlanificacionDao;
import sgo.datos.ProductoDao;
import sgo.datos.TanqueJornadaDao;
import sgo.datos.TransportistaDao;
import sgo.datos.TurnoDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.ContometroJornada;
import sgo.entidad.DetalleTurno;
import sgo.entidad.Enlace;
import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.MenuGestor;
import sgo.entidad.Muestreo;
import sgo.entidad.Operacion;
import sgo.entidad.Operario;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Producto;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.TanqueJornada;
import sgo.entidad.Turno;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TurnoControlador {
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
private TransportistaDao dTransportista;
@Autowired
private ClienteDao dCliente;
@Autowired
private CisternaDao dCisterna;
@Autowired
private CompartimentoDao dCompartimento;
@Autowired
private ContometroJornadaDao dContometroJornada;
@Autowired
private EstacionDao dEstacion;
@Autowired
private OperacionDao dOperacion;
@Autowired
private ProductoDao dProducto;
@Autowired
private TurnoDao dTurno;
@Autowired
private DetalleTurnoDao dDetalleTurnoDao;
@Autowired
private JornadaDao dJornada;
@Autowired
private DiaOperativoControlador DiaOperativoControlador;
@Autowired
private DetalleProgramacionDao dDetalleProgramacion;
@Autowired
private ContometroJornadaDao dContometroJornadaDao;
@Autowired
private TanqueJornadaDao dTanqueJornadaDao;
/** Nombre de la clase. */
private static final String sNombreClase = "TurnoControlador";
//
private DataSourceTransactionManager transaccion;// Gestor de la transaccion
// urls generales
private static final String URL_GESTION_COMPLETA = "/admin/turno";
private static final String URL_GESTION_RELATIVA = "/turno";
private static final String URL_GUARDAR_COMPLETA = "/admin/turno/crear";
private static final String URL_GUARDAR_RELATIVA = "/turno/crear";
private static final String URL_LISTAR_COMPLETA = "/admin/turno/listar";
private static final String URL_LISTAR_RELATIVA = "/turno/listar";
private static final String URL_ACTUALIZAR_COMPLETA = "/admin/turno/actualizar";
private static final String URL_ACTUALIZAR_RELATIVA = "/turno/actualizar";

private static final String URL_RECUPERAR_COMPLETA = "/admin/turno/recuperar";
private static final String URL_RECUPERAR_RELATIVA = "/turno/recuperar";

private static final String URL_RECUPERAR_TANQUES_RELATIVA = "/turno/recuperarTanquesDespachando";

private static final String URL_OBTIENE_ULTIMA_JORNADA_COMPLETA = "/admin/turno/obtieneUltimaJornada";
private static final String URL_OBTIENE_ULTIMA_JORNADA_RELATIVA = "/turno/obtieneUltimaJornada";

private static final String URL_RECUPERAR_APERTURA_COMPLETA = "/admin/turno/recuperarApertura";
private static final String URL_RECUPERAR_APERTURA_RELATIVA = "/turno/recuperarApertura";



private HashMap<String, String> recuperarMapaValores(Locale locale) {
	HashMap<String, String> mapaValores = new HashMap<String, String>();
	try {
		mapaValores.put("ESTADO_ACTIVO", String.valueOf(Constante.ESTADO_ACTIVO));
	    mapaValores.put("ESTADO_INACTIVO", String.valueOf(Constante.ESTADO_INACTIVO));
	    mapaValores.put("FILTRO_TODOS", String.valueOf(Constante.FILTRO_TODOS));
	    mapaValores.put("TEXTO_INACTIVO", gestorDiccionario.getMessage("sgo.estadoInactivo",null,locale));
	    mapaValores.put("TEXTO_ACTIVO", gestorDiccionario.getMessage("sgo.estadoActivo",null,locale));
	    mapaValores.put("TEXTO_TODOS", gestorDiccionario.getMessage("sgo.filtroTodos",null,locale));
	    
	    mapaValores.put("TITULO_AGREGAR_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioAgregar",null,locale));
	    mapaValores.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar",null,locale));
	    mapaValores.put("TITULO_DETALLE_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioVer",null,locale));
	    mapaValores.put("TITULO_LISTADO_REGISTROS", gestorDiccionario.getMessage("sgo.tituloFormularioListado",null,locale));
	    //
	    mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar",null,locale));

	    mapaValores.put("ETIQUETA_BOTON_AGREGAR", gestorDiccionario.getMessage("sgo.etiquetaBotonAgregar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_MODIFICAR", gestorDiccionario.getMessage("sgo.etiquetaBotonModificar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_VER", gestorDiccionario.getMessage("sgo.etiquetaBotonVer",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_FILTRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonFiltrar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_ACTIVAR", gestorDiccionario.getMessage("sgo.etiquetaBotonActivar",null,locale));
	    
	    mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar",null,locale));
	    mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar",null,locale));
	    mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado",null,locale));
	    
	    mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
	    mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));
	} catch (Exception ex) {

	}
	return mapaValores;
}

// @SuppressWarnings("unchecked")
@RequestMapping(URL_GESTION_RELATIVA)
public ModelAndView mostrarFormulario(Locale locale) {
	ModelAndView vista = null;
	AuthenticatedUserDetails principal = null;
	ArrayList<?> listaEnlaces = null;
	ArrayList<?> listaClientes = null;
	ArrayList<?> listaOperaciones = null;
	ArrayList<?> listaEstaciones = null;
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
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		respuesta = dCliente.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}

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

		parametros = new ParametrosListar();
		parametros.setPaginacion(Constante.SIN_PAGINACION);
		// Para que retorne solo los productos que se encuentren activos
		parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
		respuesta = dProducto.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaProductos = (ArrayList<?>) respuesta.contenido.carga;

		String fecha = dDiaOperativo.recuperarFechaActual().valor;
		Operacion op = (Operacion) listaOperaciones.get(0);
		parametros.setIdOperacion(op.getId());
	    //esto para obtener la última jornada cargada 
	    Respuesta oRespuesta = dJornada.recuperarUltimaJornada(parametros);
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
		parametros.setIdCliente(principal.getCliente().getId());
		parametros.setFiltroOperacion(op.getId());
		respuesta = dEstacion.recuperarRegistros(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		}
		listaEstaciones = (ArrayList<?>) respuesta.contenido.carga;
		
		mapaValores = recuperarMapaValores(locale);
		vista = new ModelAndView("plantilla");
		vista.addObject("vistaJSP", "operaciones/turno.jsp");
		vista.addObject("vistaJS", "operaciones/turno.js");
		vista.addObject("identidadUsuario", principal.getIdentidad());
		vista.addObject("menu", listaEnlaces);
		vista.addObject("clientes", listaClientes);
		vista.addObject("operaciones", listaOperaciones);
		vista.addObject("estaciones", listaEstaciones);
		vista.addObject("productos", listaProductos);
		vista.addObject("mapaValores", mapaValores);
		//vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
		vista.addObject("fechaActual", fecha);
	} catch (Exception ex) {

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
		
//		if (httpRequest.getParameter("campoOrdenamiento") != null) {
//			parametros.setCampoOrdenamiento(( httpRequest.getParameter("campoOrdenamiento")));
//		}
//		
//		if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
//			parametros.setSentidoOrdenamiento(( httpRequest.getParameter("sentidoOrdenamiento")));
//		}
		parametros.setCampoOrdenamiento("fechaHoraApertura");
		parametros.setSentidoOrdenamiento("DESC");
		
		
		if (httpRequest.getParameter("valorBuscado") != null) {
			parametros.setValorBuscado(( httpRequest.getParameter("valorBuscado")));
		}
		if (httpRequest.getParameter("txtFiltro") != null) {
			parametros.setTxtFiltro((httpRequest.getParameter("txtFiltro")));
		}
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("filtroEstado") != null) {
			parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
		}
		
		if (httpRequest.getParameter("filtroOperacion") != null) {
			parametros.setFiltroOperacion(Integer.parseInt(httpRequest.getParameter("filtroOperacion")));
		}
		
		if (httpRequest.getParameter("idJornada") != null) {
			parametros.setIdJornada(Integer.parseInt(httpRequest.getParameter("idJornada")));
		}
		
		if (httpRequest.getParameter("filtroEstacion") != null) {
			parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
		}
		
		if (httpRequest.getParameter("filtroFechaJornada") != null) {
			parametros.setFiltroFechaJornada((httpRequest.getParameter("filtroFechaJornada")));
		}
		
		//esto para buscar jornadas por un rango de fechas
	    if (httpRequest.getParameter("filtroFechaInicio") != null) {
		   parametros.setFiltroFechaInicio((httpRequest.getParameter("filtroFechaInicio")));
	    }
	   
	   if (httpRequest.getParameter("filtroFechaFinal") != null) {
		  parametros.setFiltroFechaFinal((httpRequest.getParameter("filtroFechaFinal")));
	   }

		//Recuperar registros
		respuesta = dTurno.recuperarRegistros(parametros);
		respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
	} catch(Exception ex){
		//ex.printStackTrace();
		Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}	


@RequestMapping(value = URL_RECUPERAR_APERTURA_RELATIVA ,method = RequestMethod.GET)
public @ResponseBody RespuestaCompuesta recuperarApertura(HttpServletRequest httpRequest, Locale locale){
	RespuestaCompuesta oRespuesta = null;
	AuthenticatedUserDetails principal = null;
	ParametrosListar parametros= null;
	int cantidadTurnos = 0;
	String MensajeCantidadTurnos="";
	try {			
		//Recupera el usuario actual
		principal = this.getCurrentUser(); 
		//Recuperar el enlace de la accion
		oRespuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_APERTURA_COMPLETA);
		if (oRespuesta.estado==false){
			throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
		}
		Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
		}
		//Recuperar el registro por id Estacion de la ultima jornada
		
		
		//Recuperar parametros
		parametros = new ParametrosListar();
		parametros.setIdJornada(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("idJornada") != null && httpRequest.getParameter("idJornada") != "") {
			parametros.setIdJornada(Integer.parseInt( httpRequest.getParameter("idJornada")));
		}
		if (httpRequest.getParameter("cantidadTurnos") != null && httpRequest.getParameter("cantidadTurnos") != "") {
			cantidadTurnos = Integer.parseInt( httpRequest.getParameter("cantidadTurnos"));
		}
		
		//verifica que no exista un turno abierto
		parametros.setFiltroEstado(Turno.ESTADO_ABIERTO);
		oRespuesta=dTurno.recuperarRegistros(parametros);		
        if (oRespuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
        if(oRespuesta.getContenido().getCarga()!=null && oRespuesta.getContenido().getCarga().size()>0){
        	oRespuesta.valor="0";
        	oRespuesta.mensaje="Existe un turno abierto";
        	//throw new Exception("Existe un turno abierto");
        } else {
        	parametros=new ParametrosListar();
    		//ordena por la ultima hora de cierre
        	parametros.setIdJornada(Integer.parseInt( httpRequest.getParameter("idJornada")));
    		parametros.setCampoOrdenamiento("fechaHoraCierre");
    		parametros.setSentidoOrdenamiento("DESC");  
    		//obtiene cabecera del turno.
    		oRespuesta=dTurno.recuperarRegistros(parametros);		
            if (oRespuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            parametros=new ParametrosListar();
            if(oRespuesta.getContenido().getCarga()!=null && oRespuesta.getContenido().getCarga().size()>0){
            	Turno eTurno = (Turno)oRespuesta.getContenido().getCarga().get(0);    
            	
            	//recuperamos la estacion del jornada del turno
                RespuestaCompuesta registroEstacion = dEstacion.recuperarRegistro(eTurno.getJornada().getEstacion().getId());
                if (registroEstacion.estado==false){        	
                	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
                }
                
                Estacion eEstacion = (Estacion) registroEstacion.getContenido().getCarga().get(0);
                
                if(eEstacion.getCantidadTurnos() == 0){
                	throw new Exception("Debe ingresar la cantidad de Turnos con las que cuenta la Estación. Favor verifique.");
                }
                
                if(eEstacion.getCantidadTurnos() > cantidadTurnos){
                	cantidadTurnos = cantidadTurnos + 1;
                	MensajeCantidadTurnos = "Se está aperturando el turno " + cantidadTurnos + " de " + eEstacion.getCantidadTurnos() + " turnos.";
                }
                else{
                	throw new Exception("No se pueden abrir más turnos para la estación " + eEstacion.getNombre() + ". Favor verifique.");
                	
                }
                
            	eTurno.getJornada().setEstacion(eEstacion);
            	
            	//extrae detalle del ultimo turno de la jornada            	
            	parametros.setIdTurno(eTurno.getId());
            	oRespuesta= dDetalleTurnoDao.recuperarRegistros(parametros);
                if (oRespuesta.estado==false){        	
                	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
                }
                oRespuesta.valor="1"; 
                if(MensajeCantidadTurnos.length() != 0){
                	oRespuesta.mensaje = MensajeCantidadTurnos;
                } else {
                	oRespuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
                }
            	
            }else{//obtener lista de contometro jornada  
            	parametros.setIdJornada(Integer.parseInt(httpRequest.getParameter("idJornada")));
            	oRespuesta=dContometroJornadaDao.recuperarRegistros(parametros);
           	 	if (oRespuesta.estado==false){        	
                	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
                } 
           	 	ContometroJornada eContometroJornada = (ContometroJornada)oRespuesta.getContenido().getCarga().get(0);
           	//recuperamos la estacion del jornada del turno
                RespuestaCompuesta registroEstacion = dEstacion.recuperarRegistro(eContometroJornada. getJornada().getEstacion().getId());
                if (registroEstacion.estado==false){        	
                	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
                }
                
                Estacion eEstacion = (Estacion) registroEstacion.getContenido().getCarga().get(0);
                
                if(eEstacion.getCantidadTurnos() == 0){
                	throw new Exception("Debe ingresar la cantidad de Turnos con las que cuenta la Estación. Favor verifique.");
                }
                
                if(eEstacion.getCantidadTurnos() > cantidadTurnos){
                	cantidadTurnos = cantidadTurnos + 1;
                	MensajeCantidadTurnos = "Se está aperturando el turno " + cantidadTurnos + " de " + eEstacion.getCantidadTurnos() + " turnos.";
                }
                else{
                	throw new Exception("No se pueden abrir más turnos para la estación " + eEstacion.getNombre() + ". Favor verifique.");
                }

           	 	 oRespuesta.valor="2"; 
           	 	if(MensajeCantidadTurnos.length() != 0){
                	oRespuesta.mensaje = MensajeCantidadTurnos;
                } else {
                	oRespuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
                }
           	 	
           	 	// oRespuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
	           	 if(oRespuesta.getContenido().getCarga().isEmpty()){
	           		oRespuesta.valor="0"; 
	           		oRespuesta.mensaje="No existe Contómetros para la jornada";
	           	 }
            }
            //oRespuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
        }
       
	} catch (Exception ex){
  		//ex.printStackTrace();
		Utilidades.gestionaError(ex, sNombreClase, "recuperarApertura");
  		oRespuesta.estado = false;
  		oRespuesta.mensaje = ex.getMessage();
	}
	return oRespuesta;
}


@RequestMapping(value = URL_RECUPERAR_TANQUES_RELATIVA ,method = RequestMethod.GET)
public @ResponseBody RespuestaCompuesta recuperaTanquesDespachando(int idJornada, Locale locale){
	RespuestaCompuesta respuesta = null;
	ParametrosListar parametros = null;
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
    	respuesta= dJornada.recuperarRegistro(idJornada);
    	//Verifica el resultado de la accion
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
        Contenido<Jornada> contenido = new Contenido<Jornada>();
        List<TanqueJornada> listaTanqueJornada = new ArrayList<TanqueJornada>();
        List<Jornada> listaRegistros = new ArrayList<Jornada>();

        Jornada eJornada = (Jornada) respuesta.contenido.carga.get(0);

        parametros = new ParametrosListar();
        parametros.setIdJornada(idJornada);
        parametros.setPaginacion(Constante.SIN_PAGINACION);
        parametros.setEstadoDespachando(TanqueJornada.ESTADO_DESPACHANDO);
        //recuperamos los tanques de la jornada
        respuesta = dTanqueJornadaDao.recuperarRegistros(parametros);
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
        for (int a = 0; a < respuesta.contenido.carga.size(); a++){
        	TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(a);
        	listaTanqueJornada.add(eTanqueJornada);
		}

        //le asignamos los contometros y los tanque a la jornada
        eJornada.setTanqueJornada(listaTanqueJornada);
        listaRegistros.add(eJornada);

        contenido.carga = listaRegistros;
		respuesta.contenido = contenido;
        
     	respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
	} catch (Exception ex){
		Utilidades.gestionaError(ex, sNombreClase, "recuperaRegistro");
		//ex.printStackTrace();
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}


@RequestMapping(value = URL_OBTIENE_ULTIMA_JORNADA_RELATIVA ,method = RequestMethod.GET)
public @ResponseBody Respuesta obtieneUltimaJornada(HttpServletRequest httpRequest, Locale locale){
	RespuestaCompuesta oRespuesta = null;
	Respuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	ParametrosListar parametros= null;
	try {			
		//Recupera el usuario actual
		principal = this.getCurrentUser(); 
		//Recuperar el enlace de la accion
		oRespuesta = dEnlace.recuperarRegistro(URL_OBTIENE_ULTIMA_JORNADA_COMPLETA);
		if (oRespuesta.estado==false){
			throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale));
		}
		Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
		//Verificar si cuenta con el permiso necesario			
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
			throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale));
		}
		//Recuperar el registro por id Estacion de la ultima jornada
		
		
		//Recuperar parametros
		parametros = new ParametrosListar();
		parametros.setFiltroEstacion(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("filtroEstacion") != null && httpRequest.getParameter("filtroEstacion") != "") {
			parametros.setFiltroEstacion(Integer.parseInt( httpRequest.getParameter("filtroEstacion")));
		}
		
		parametros.setIdOperacion(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("idOperacion") != null) {
			parametros.setIdOperacion(Integer.parseInt( httpRequest.getParameter("idOperacion")));
		}
		
		int estados[]={Jornada.ESTADO_ABIERTO,Jornada.ESTADO_REGISTRADO};		
		parametros.setFiltroEstados(estados);		
		
    	respuesta= dJornada.recuperarUltimaJornadaEstado(parametros);
    	//Verifica el resultado de la accion
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
        
        String ultimaFechaOperativa=respuesta.valor;
        if(ultimaFechaOperativa==null){//mensaje de error
        	respuesta.valor="0";
        	respuesta.mensaje="No existe una jornada abierta para la estación seleccionada.";
        }else{
    		parametros=new ParametrosListar();
    		parametros.setFiltroFechaJornada(ultimaFechaOperativa);
    		parametros.setFiltroEstacion(Integer.parseInt( httpRequest.getParameter("filtroEstacion")));
    		oRespuesta= dJornada.recuperarRegistros(parametros);
        	//Verifica el resultado de la accion
            if (oRespuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            //retornar el ID JORNADA
            Jornada eJornada = (Jornada) oRespuesta.getContenido().getCarga().get(0);
            respuesta.valor=String.valueOf(eJornada.getId()); 
        }
     	//respuesta.mensaje=gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale);
	} catch (Exception ex){
  		//ex.printStackTrace();
		Utilidades.gestionaError(ex, sNombreClase, "obtieneUltimaJornada");
  		respuesta.estado = false;
  		respuesta.mensaje = ex.getMessage();
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
		//Recuperar el registro ID TURNO
    	respuesta= dDetalleTurnoDao.recuperarRegistroDetalleTurno(ID);
    	//Verifica el resultado de la accion
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
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

@RequestMapping(value = URL_GUARDAR_RELATIVA, method = RequestMethod.POST)
public @ResponseBody
RespuestaCompuesta guardarRegistro(@RequestBody Turno eTurno, HttpServletRequest peticionHttp, Locale locale) {
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	Bitacora eBitacora= null;
	String ContenidoAuditoria ="";
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	String direccionIp="";
	String ClaveGenerada="";
	ParametrosListar parametrosListar=null;
	Respuesta respuestaSimple=null;
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dTurno.getDataSource());
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
    	eTurno.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eTurno.setActualizadoPor(principal.getID()); 
       	eTurno.setCreadoEl(Calendar.getInstance().getTime().getTime());
        eTurno.setCreadoPor(principal.getID());
        eTurno.setIpActualizacion(direccionIp);
        eTurno.setIpCreacion(direccionIp);        
        
        //valida responsable ayudante
		if(!Utilidades.esValido(eTurno.getIdResponsable())){
			throw new Exception(gestorDiccionario.getMessage("sgo.noOperario1", null, locale));
		}
		/*if(!Utilidades.esValido(eTurno.getIdAyudante())){
			throw new Exception(gestorDiccionario.getMessage("sgo.noOperario2", null, locale));
		}*/
		if(eTurno.getIdResponsable() == eTurno.getIdAyudante()){
			throw new Exception(gestorDiccionario.getMessage("sgo.operadoreIguales", null, locale));
		}
        //valida que fecha del dia operativo sea la misma la fecha de apertura
		//obtener fecha operativa por el id_jornada
		respuesta = dJornada.recuperarRegistro(eTurno.getIdJornada());
		if (respuesta.estado==false){
			throw new Exception("Error al obtener la Jornada");
		}
		//Jornada eJornada=(Jornada)respuesta.contenido.getCarga().get(0);		
		/*boolean validaFechas = Utilidades.comparaTimestampConDate(eTurno.getFechaHoraApertura(), eJornada.getFechaOperativa());
        if (validaFechas == false) {
            throw new Exception("La fecha de Inicio de Apertura debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
        }*/
		//valida que la hora de apertura sea mayor de la hora de cierre anterior
		parametrosListar=new ParametrosListar();
		parametrosListar.setIdJornada(eTurno.getIdJornada());
		parametrosListar.setFiltroEstado(Turno.ESTADO_CERRADO);
		respuestaSimple = dTurno.recuperarUltimoTurnoCerrado(parametrosListar);
		if (respuestaSimple.estado==false){
			throw new Exception("Error al obtener la hora del ultimo cierre");
		}
		if(respuestaSimple.valor!=null){
			Timestamp horaCierre=new java.sql.Timestamp(Utilidades.convierteStringADate(respuestaSimple.valor,"yyyy/MM/dd HH:mm:ss").getTime());
			if(eTurno.getFechaHoraApertura().compareTo(horaCierre)<0){
				 throw new Exception("La Hora de Apertura debe ser mayor que la hora Cierre : " + respuestaSimple.valor);
			}
		}

        respuesta= dTurno.guardarRegistro(eTurno);
        //Verifica si la accion se ejecuto de forma satisfactoria
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
        }
        ClaveGenerada = respuesta.valor;
        //guardar Detalle
        List<DetalleTurno> listaTurno=eTurno.getTurnoDetalles();
        if(!listaTurno.isEmpty()){        	
        	for(DetalleTurno detalleTurno:listaTurno ){
        		detalleTurno.setIdTurno(Integer.parseInt(ClaveGenerada));
        		respuesta=dDetalleTurnoDao.guardarRegistro(detalleTurno);
                if (respuesta.estado==false){     	
                  	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
                }        	
        	}         	
        }
         //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
        ContenidoAuditoria =  mapper.writeValueAsString(eTurno);
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_GUARDAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(ClaveGenerada);
        eBitacora.setContenido(ContenidoAuditoria);
        eBitacora.setRealizadoEl(eTurno.getCreadoEl());
        eBitacora.setRealizadoPor(eTurno.getCreadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }           
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eTurno.getFechaCreacion().substring(0, 9),eTurno.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
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

@RequestMapping(value = URL_ACTUALIZAR_RELATIVA, method = RequestMethod.POST)
public @ResponseBody
RespuestaCompuesta actualizarRegistro(@RequestBody Turno eTurno, HttpServletRequest peticionHttp, Locale locale) {
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dTurno.getDataSource());
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
    	eTurno.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        eTurno.setActualizadoPor(principal.getID()); 
        eTurno.setIpActualizacion(direccionIp);
        
        //valida que la hora de cierre debe ser mayor a la hora de apertura
        //recupera turno a cerrar
		respuesta = dTurno.recuperarRegistro(eTurno.getId());
		if (respuesta.estado==false){
			throw new Exception("Error al obtener el Turno");
		}
		Turno turnoOld=(Turno)respuesta.contenido.getCarga().get(0);
		if(eTurno.getFechaHoraCierre().compareTo(turnoOld.getFechaHoraApertura())<0){
			throw new Exception("La Hora de Cierre debe ser mayor a: " + Utilidades.convierteDateAString(turnoOld.getFechaHoraApertura(), "dd/MM/yyyy HH:mm:ss" ));
		}
        
        //valida que fecha del dia operativo sea la misma la fecha de cierre
		//obtener fecha operativa por el id_jornada
		respuesta = dJornada.recuperarRegistro(turnoOld.getIdJornada());
		if (respuesta.estado==false){
			throw new Exception("Error al obtener la Jornada");
		}
		Jornada eJornada=(Jornada)respuesta.contenido.getCarga().get(0);		
		/*boolean validaFechas = Utilidades.comparaTimestampConDate(eTurno.getFechaHoraCierre(), eJornada.getFechaOperativa());
        if (validaFechas == false) {
            throw new Exception("La fecha de Cierre debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
        }*/
        
        respuesta= dTurno.actualizarRegistro(eTurno);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar detalle
        List<DetalleTurno> listaTurno=eTurno.getTurnoDetalles();
        for(DetalleTurno detalleTurno:listaTurno){
        	respuesta= dDetalleTurnoDao.actualizarRegistro(detalleTurno);
            if (respuesta.estado==false){          	
            	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
        }
        
        
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
        eBitacora.setTabla(EventoDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eTurno.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eTurno));
        eBitacora.setRealizadoEl(eTurno.getActualizadoEl());
        eBitacora.setRealizadoPor(eTurno.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eTurno.getFechaActualizacion().substring(0, 9),eTurno.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
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

private AuthenticatedUserDetails getCurrentUser() {
	return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
}

}
