package sgo.servicio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import sgo.datos.ContometroDao;
import sgo.datos.ContometroJornadaDao;
import sgo.datos.DetalleTurnoDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.EstacionDao;
import sgo.datos.JornadaDao;
import sgo.datos.MuestreoDao;
import sgo.datos.OperacionDao;
import sgo.datos.OperarioDao;
import sgo.datos.ProductoDao;
import sgo.datos.TanqueDao;
import sgo.datos.TanqueJornadaDao;
import sgo.datos.TurnoDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Contenido;
import sgo.entidad.Contometro;
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
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.entidad.Turno;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class JornadaControlador {
	@Autowired
	private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
	@Autowired
	private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
	@Autowired
	private EnlaceDao dEnlace;
	@Autowired
	private MenuGestor menu;
	@Autowired
	private JornadaDao dJornada;
	@Autowired
	private ProductoDao dProducto;
	@Autowired
	private ContometroJornadaDao dContometroJornada;
	@Autowired
	private TanqueJornadaDao dTanqueJornada;
	@Autowired
	private MuestreoDao dMuestreo;
	@Autowired
	private TurnoDao dTurno;
	@Autowired
	private DetalleTurnoDao dDetalleTurno;
	@Autowired
	private TanqueDao dTanque;
	@Autowired
	private EstacionDao dEstacion;
	@Autowired
	private ContometroDao dContometro;
	@Autowired
	private OperacionDao dOperacion;
	@Autowired
	private OperarioDao dOperario;
	@Autowired
	private DiaOperativoDao dDiaOperativo;
	//
	private DataSourceTransactionManager transaccion;//Gestor de la transaccion
	/** Nombre de la clase. */
    private static final String sNombreClase = "JornadaControlador";
	//urls generales
	private static final String URL_GESTION_COMPLETA="/admin/jornada";
	private static final String URL_GESTION_RELATIVA="/jornada";
	private static final String URL_GUARDAR_COMPLETA="/admin/jornada/crear";
	private static final String URL_GUARDAR_RELATIVA="/jornada/crear";
	private static final String URL_LISTAR_COMPLETA="/admin/jornada/listar";
	private static final String URL_LISTAR_RELATIVA="/jornada/listar";
	private static final String URL_ACTUALIZAR_COMPLETA="/admin/jornada/actualizar";
	private static final String URL_ACTUALIZAR_RELATIVA="/jornada/actualizar";
	private static final String URL_RECUPERAR_COMPLETA="/admin/jornada/recuperar";
	private static final String URL_RECUPERAR_RELATIVA="/jornada/recuperar";
	private static final String URL_ACTUALIZAR_ESTADO_COMPLETA="/admin/jornada/actualizarEstado";
	private static final String URL_ACTUALIZAR_ESTADO_RELATIVA="/jornada/actualizarEstado";
	private static final String URL_RECUPERAR_ULTIMO_DIA_COMPLETA="/admin/jornada/recuperar-ultimo-dia";
	private static final String URL_RECUPERAR_ULTIMO_DIA_RELATIVA="/jornada/recuperar-ultimo-dia";
	private static final String URL_RECUPERAR_APERTURA_COMPLETA="/admin/jornada/recuperar-apertura";
	private static final String URL_RECUPERAR_APERTURA_RELATIVA="/jornada/recuperar-apertura";
	private static final String URL_ELIMINAR_MUESTREO_JORNADA_COMPLETA="/admin/jornada/eliminarMuestreoJornadaa";
	private static final String URL_ELIMINAR_MUESTREO_JORNADA_RELATIVA="/jornada/eliminarMuestreoJornada";
	private static final String URL_GUARDAR_CAMBIO_TANQUE_COMPLETA="/admin/jornada/registrarCambioTanque";
	private static final String URL_GUARDAR_CAMBIO_TANQUE_RELATIVA="/jornada/registrarCambioTanque";
	private static final String URL_PROCESO_CIERRE_RELATIVA="/jornada/procesoCierre";
	
	private HashMap<String,String> recuperarMapaValores(Locale locale){
	 HashMap<String,String> mapaValores = new HashMap<String,String>();
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
    mapaValores.put("MENSAJE_REAPERTURAR_JORNADA", gestorDiccionario.getMessage("sgo.mensajeReaperturarJornada",null,locale));
    mapaValores.put("MENSAJE_ELIMINAR_REGISTRO", gestorDiccionario.getMessage("sgo.mensajeEliminarRegistro",null,locale));
    
    mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
    mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));

	 }catch(Exception ex){
	  
	 }
	 return mapaValores;
	}
	
	@RequestMapping(URL_GESTION_RELATIVA)
	public ModelAndView mostrarFormulario(Locale locale){
		ModelAndView vista =null;
		AuthenticatedUserDetails principal = null;
		ArrayList<Enlace> listaEnlaces = null;
		RespuestaCompuesta respuesta = null;
		ParametrosListar parametros = null;
		ArrayList<?> listaOperaciones = null;
		ArrayList<?> listaEstaciones = null;
		HashMap<String,String> mapaValores= null;
		try {
			principal = this.getCurrentUser();
			respuesta = menu.Generar(principal.getRol().getId(), URL_GESTION_COMPLETA);
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
			vista.addObject("vistaJSP","operaciones/jornada.jsp");
			vista.addObject("vistaJS","operaciones/jornada.js");
			vista.addObject("identidadUsuario",principal.getIdentidad());
			vista.addObject("menu",listaEnlaces);
			vista.addObject("operaciones", listaOperaciones);
			vista.addObject("estaciones", listaEstaciones);
			vista.addObject("mapaValores",mapaValores);
			//vista.addObject("fechaActual",dDiaOperativo.recuperarFechaActual().valor);
			 vista.addObject("fechaActual", fecha);
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
			
			if (httpRequest.getParameter("filtroEstacion") != null && !httpRequest.getParameter("filtroEstacion").isEmpty()) {
				parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
			} else {
				throw new Exception(gestorDiccionario.getMessage("sgo.estacionVacia",null,locale));
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
			respuesta = dJornada.recuperarRegistros(parametros);
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

	@RequestMapping(value = URL_RECUPERAR_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta recuperaRegistro(int ID, Locale locale){
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
        	respuesta= dJornada.recuperarRegistro(ID);
        	//Verifica el resultado de la accion
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            Contenido<Jornada> contenido = new Contenido<Jornada>();
            List<ContometroJornada> listaContometroJornada = new ArrayList<ContometroJornada>();
            List<TanqueJornada> listaTanqueJornada = new ArrayList<TanqueJornada>();
            List<Jornada> listaRegistros = new ArrayList<Jornada>();
            List<Muestreo> listaMuestreo = new ArrayList<Muestreo>();
            List<Producto> listaProducto = new ArrayList<Producto>();
            List<ContometroJornada> listaContometroTurno = new ArrayList<ContometroJornada>();
            List<TanqueJornada> listaTanqueJornadaApertura = new ArrayList<TanqueJornada>();
            List<TanqueJornada> listaTanqueJornadaCierre = new ArrayList<TanqueJornada>();

            Jornada eJornada = (Jornada) respuesta.contenido.carga.get(0);

            parametros = new ParametrosListar();
            parametros.setIdJornada(ID);
            parametros.setPaginacion(Constante.SIN_PAGINACION);
            parametros.setCampoOrdenamiento("id");
			parametros.setSentidoOrdenamiento("asc");
            //recuperamos los contometros de la jornada
			respuesta = dContometroJornada.recuperarRegistros(parametros);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int i = 0; i < respuesta.contenido.carga.size(); i++){
            	ContometroJornada eContometroJornada = (ContometroJornada) respuesta.contenido.carga.get(i);
				listaContometroJornada.add(eContometroJornada);
			}

            //recuperamos los contometros del turno de la jornada
            parametros.setFiltroEstado(Turno.ESTADO_CERRADO);
            parametros.setCampoOrdenamiento("nombre_tanque");
            parametros.setSentidoOrdenamiento("DESC");
			respuesta = dTurno.recuperarRegistros(parametros);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            if(respuesta.contenido.carga.size() > 0) {
	            Turno eTurno = (Turno) respuesta.contenido.carga.get(0);
	
	            respuesta = dDetalleTurno.recuperarRegistroDetalleTurno(eTurno.getId());
	            if (respuesta.estado==false){        	
	            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
	            }
	
	            for (int b = 0; b < respuesta.contenido.carga.size(); b++){
	            	DetalleTurno eDetalleTurno = (DetalleTurno) respuesta.contenido.carga.get(b);
	
	                for(int c = 0; c < listaContometroJornada.size(); c++ ){
	                	ContometroJornada eContometroJornada = listaContometroJornada.get(c);
	                	if(eContometroJornada.getIdContometro() == eDetalleTurno.getContometro().getId()){
	                		eContometroJornada.setLecturaFinal(eDetalleTurno.getLecturaFinal());
	                	}
	                }
				}
            }

            parametros.setFiltroEstado(Constante.FILTRO_TODOS);
            parametros.setCampoOrdenamiento("fecha_hora_cierre");
            parametros.setSentidoOrdenamiento("DESC");
            //recuperamos los tanques de la jornada
            respuesta = dTanqueJornada.recuperarRegistros(parametros);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int k = 0; k < respuesta.contenido.carga.size(); k++){
            	TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(k);
            	listaTanqueJornada.add(eTanqueJornada);
				
            	if(listaProducto.size() > 0){
            		boolean existeProducto = false;
            		for(int r = 0; r < listaProducto.size(); r++){
            			if(eTanqueJornada.getProducto().getId() == listaProducto.get(r).getId()){
            				existeProducto = true;
            			}
            		}
            		if(!existeProducto){
            			listaProducto.add(eTanqueJornada.getProducto());	
            		}
            	} else {
            		listaProducto.add(eTanqueJornada.getProducto());
            	}
			}
            
            //recuperamos los muestreos de la jornada
            respuesta = dMuestreo.recuperarRegistros(parametros);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int r = 0; r < respuesta.contenido.carga.size(); r++){
            	Muestreo eMuestreo = (Muestreo) respuesta.contenido.carga.get(r);
				listaMuestreo.add(eMuestreo);
			}
            
            ParametrosListar parametrosApertura = new ParametrosListar();
            parametrosApertura.setTanqueDeCierre(TanqueJornada.TANQUE_CIERRE);
            //recuperamos los tanques de la jornada
            respuesta = dTanqueJornada.recuperarRegistros(parametrosApertura);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int k = 0; k < respuesta.contenido.carga.size(); k++){
            	TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(k);
            	listaTanqueJornadaApertura.add(eTanqueJornada);
			}
            
            //TODO
            //recuperamos los tanque para el cierre de la jornada
            //primero los tanques que se encuentra despachando
            ParametrosListar parametrosCierre = new ParametrosListar();
            parametrosCierre.setIdJornada(ID);
            parametrosCierre.setPaginacion(Constante.SIN_PAGINACION);
            parametrosCierre.setEstadoDespachando(TanqueJornada.ESTADO_DESPACHANDO);
            //recuperamos los tanques de la jornada
            respuesta = dTanqueJornada.recuperarRegistros(parametrosCierre);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int a = 0; a < respuesta.contenido.carga.size(); a++){
            	TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(a);
            	listaTanqueJornadaCierre.add(eTanqueJornada);
			}
            
            
            
            //recuperamos los tanque para el cierre de la jornada
            //luego los tanque que no estan despachando, que sean tanques de apertura y que tengan medida_final = 0 --es decir no ha sido cerrada
            parametrosCierre.setIdJornada(ID);
            parametrosCierre.setPaginacion(Constante.SIN_PAGINACION);
            parametrosCierre.setEstadoDespachando(TanqueJornada.ESTADO_NO_DESPACHANDO);
            parametrosCierre.setTanqueDeApertura(TanqueJornada.TANQUE_APERTURA);
            //parametrosCierre.setTxtFiltro(" hora_final is null ");
            parametrosCierre.setTxtFiltro(" id_tanque not in (select id_tanque from sgo.v_tanque_jornada where en_linea = 1 and id_jornada = " + ID + " ) and medida_final = 0 ");
            //parametrosCierre.setTxtFiltro(" id_tanque not in (select id_tanque from sgo.v_tanque_jornada where en_linea = 1 and id_jornada = " + ID + " )  and cierre = 2 ");
            //recuperamos los tanques de la jornada
            respuesta = dTanqueJornada.recuperarRegistros(parametrosCierre);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int a = 0; a < respuesta.contenido.carga.size(); a++){
            	TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(a);
            	listaTanqueJornadaCierre.add(eTanqueJornada);
			}
            
            //recuperamos los tanque para el cierre de la jornada
            //luego los tanque que no estan despachando, 
            //que no son de apertura
            //
            parametrosCierre.setIdJornada(ID);
            parametrosCierre.setPaginacion(Constante.SIN_PAGINACION);
            parametrosCierre.setEstadoDespachando(TanqueJornada.ESTADO_NO_DESPACHANDO);
            //parametrosCierre.setTanqueDeApertura(TanqueJornada.TANQUE_NO_APERTURA);
            parametrosCierre.setTanqueDeApertura(Constante.FILTRO_NINGUNO);
            //parametrosCierre.setTxtFiltro(" id_tanque not in (SELECT id_tanque FROM sgo.v_tanque_jornada where en_linea = 1) and hora_final not in(select hora_final from sgo.v_tanque_jornada where hora_final is not null and id_jornada = " + ID + ") ");
            parametrosCierre.setTxtFiltro("");
            //recuperamos los tanques de la jornada
            respuesta = dTanqueJornada.recuperarRegistros(parametrosCierre);
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            TanqueJornada otroTanqueJornada = new TanqueJornada();
            boolean existe = false;

            for(int h = 0; h < respuesta.contenido.carga.size(); h++){
            	otroTanqueJornada = new TanqueJornada();
            	TanqueJornada tanqueJornadaTemp = (TanqueJornada) respuesta.contenido.carga.get(h);
            	otroTanqueJornada = tanqueJornadaTemp;
            	int idTanqueTemp = tanqueJornadaTemp.getIdTanque();
            	existe = false;
            	
            	//verificamos si ya existe ese tanque en el listado de tanques para el cierre
            	/*for(int c = 0; c < listaTanqueJornadaCierre.size(); c++) {
            		TanqueJornada tanqueJornadaCierreTemp = listaTanqueJornadaCierre.get(c);
            		if(tanqueJornadaTemp.getIdTanque() == tanqueJornadaCierreTemp.getIdTanque()){
            			existe = true;
            		}
            	}*/
            	
            	//si no existe
            	//if(!existe){
	            	for(int a = 0; a < respuesta.contenido.carga.size(); a++){
	                	TanqueJornada tanqueJornadaTemp2 = (TanqueJornada) respuesta.contenido.carga.get(a);
	                	int idTanqueTemp2 = tanqueJornadaTemp2.getIdTanque();
	                	if(tanqueJornadaTemp.getId() != tanqueJornadaTemp2.getId()){
	                		if(idTanqueTemp == idTanqueTemp2){
	                			if(tanqueJornadaTemp2.getHoraFinal().after(tanqueJornadaTemp.getHoraFinal())) {
	                				otroTanqueJornada = tanqueJornadaTemp2;
	                			}
	                		}
	                	}
	                }
	            	//verificamos si ya existe ese tanque en el listado de tanques para el cierre
	            	for(int c = 0; c < listaTanqueJornadaCierre.size(); c++) {
	            		TanqueJornada tanqueJornadaCierreTemp = listaTanqueJornadaCierre.get(c);
	            		if(tanqueJornadaCierreTemp.getIdTanque() == otroTanqueJornada.getIdTanque()){
	            			existe = true;
	            		}
	            	}
	            	if(!existe){
	            		listaTanqueJornadaCierre.add(otroTanqueJornada);
	            	}
            	//}
            	
            }
            
            
            /*if(respuesta.contenido.carga.size() > 0){
	            otroTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(respuesta.contenido.carga.size() - 1);
	            listaTanqueJornadaCierre.add(otroTanqueJornada);
            }*/
            
            //buscamos el operario de entrada y de salida
            respuesta = dOperario.recuperarRegistro(eJornada.getIdOperario1());
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            eJornada.setOperario1((Operario) respuesta.contenido.carga.get(0));

            respuesta = dOperario.recuperarRegistro(eJornada.getIdOperario2());
            if (respuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            eJornada.setOperario2((Operario) respuesta.contenido.carga.get(0));

            //le asignamos los contometros y los tanque a la jornada
            eJornada.setContometroJornada(listaContometroJornada);
            eJornada.setTanqueJornada(listaTanqueJornada);
            eJornada.setMuestreo(listaMuestreo);
            eJornada.setProducto(listaProducto);
            eJornada.setContometroTurno(listaContometroTurno);
            eJornada.setTanqueJornadaCierre(listaTanqueJornadaCierre);
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
	
	@RequestMapping(value = URL_RECUPERAR_ULTIMO_DIA_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody Respuesta recuperaUltimoDia(HttpServletRequest httpRequest, Locale locale){
	Respuesta respuesta = null;
	RespuestaCompuesta oRespuesta = null;
	ParametrosListar parametros= null;
	AuthenticatedUserDetails principal = null;
	try {
		// Recupera el usuario actual
		principal = this.getCurrentUser();
		// Recuperar el enlace de la accion
		oRespuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_ULTIMO_DIA_COMPLETA);
		if (oRespuesta.estado == false) {
		  throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
		}
		Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
		// Verificar si cuenta con el permiso necesario
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
		  throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
		}

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

		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("filtroEstado") != null) {
			parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
		}
		
		respuesta = dJornada.recuperarUltimaJornada(parametros);
	    
		if (respuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	    }

	    respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
	  	} catch (Exception ex) {
	  		Utilidades.gestionaError(ex, sNombreClase, "recuperaUltimoDia");
	  		//ex.printStackTrace();
	  		respuesta.estado = false;
	  		respuesta.mensaje = ex.getMessage();
	  	}
	  return respuesta;
	 }

	@RequestMapping(value = URL_RECUPERAR_APERTURA_RELATIVA, method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta recuperarApertura(HttpServletRequest httpRequest, Locale locale){
	Respuesta respuesta = null;
	RespuestaCompuesta oRespuesta = null;
	ParametrosListar parametros= null;
	AuthenticatedUserDetails principal = null;
	java.util.Date ultimoDia = null;
	try {
		// Recupera el usuario actual
		principal = this.getCurrentUser();
		// Recuperar el enlace de la accion
		oRespuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_APERTURA_COMPLETA);
		if (oRespuesta.estado == false) {
		  throw new Exception(gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale));
		}
		Enlace eEnlace = (Enlace) oRespuesta.getContenido().getCarga().get(0);
		// Verificar si cuenta con el permiso necesario
		if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
		  throw new Exception(gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale));
		}

		//Recuperar parametros
		parametros = new ParametrosListar();
		if (httpRequest.getParameter("filtroEstacion") != null) {
			parametros.setFiltroEstacion(Integer.parseInt( httpRequest.getParameter("filtroEstacion")));
			if(parametros.getFiltroEstacion() == 0){
				throw new Exception(gestorDiccionario.getMessage("sgo.estacionVacia",null,locale));
			}
		} else {
			throw new Exception(gestorDiccionario.getMessage("sgo.estacionVacia",null,locale));
		}
		
		parametros.setIdOperacion(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("idOperacion") != null) {
			parametros.setIdOperacion(Integer.parseInt( httpRequest.getParameter("idOperacion")));
		}

		if (httpRequest.getParameter("idCliente") != null) {
			parametros.setIdCliente(Integer.parseInt(httpRequest.getParameter("idCliente")));
		}
		if (httpRequest.getParameter("filtroEstacion") != null) {
			parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
		}
		
		parametros.setFiltroEstado(Jornada.ESTADO_ABIERTO);
		oRespuesta = dJornada.recuperarRegistros(parametros);
		if (oRespuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	    }
		
		if(oRespuesta.contenido.carga.size() > 0){
			//throw new Exception(gestorDiccionario.getMessage("Hay un dÃƒÂ­a operativo con estado abierto, favor verifique.", null, locale));
			throw new Exception(gestorDiccionario.getMessage("sgo.jornada.existeJornadaAbierta", null, locale));
		}
		
		parametros.setFiltroEstado(Jornada.ESTADO_REGISTRADO);
		oRespuesta = dJornada.recuperarRegistros(parametros);
		if (oRespuesta.estado == false) {
	     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	    }
		
		if(oRespuesta.contenido.carga.size() > 0){
			//throw new Exception(gestorDiccionario.getMessage("Hay un dÃƒÂ­a operativo con estado abierto, favor verifique.", null, locale));
			throw new Exception(gestorDiccionario.getMessage("sgo.jornada.existeJornadaRegistrado", null, locale));
		}
		
		parametros.setTxtFiltro(" (t1.estado = 3 or t1.estado = 4) ");
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		//verifica si existe alguna jornada cerrada o liquidada
		respuesta = dJornada.recuperarUltimaJornada(parametros);
		if (respuesta.estado == false) {
			throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
	    }
		
		Contenido<Jornada> contenido = new Contenido<Jornada>();
        List<ContometroJornada> listaContometroJornada = new ArrayList<ContometroJornada>();
        List<TanqueJornada> listaTanqueJornada = new ArrayList<TanqueJornada>();
        List<Contometro> listaContometro = new ArrayList<Contometro>();
        List<Tanque> listaTanque = new ArrayList<Tanque>();
        List<Jornada> listaRegistros = new ArrayList<Jornada>();
		
		if(Utilidades.esValido(respuesta.valor)){
			ParametrosListar parametros2 = new ParametrosListar();
			parametros2.setFiltroFechaJornada(respuesta.valor.toString());
			parametros2.setFiltroEstacion(parametros.getFiltroEstacion());
			oRespuesta = dJornada.recuperarRegistros(parametros2);
			if (respuesta.estado == false) {
		     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
		    }

            Jornada eJornada = (Jornada) oRespuesta.contenido.carga.get(0);
            if((eJornada.getEstado() == Jornada.ESTADO_ABIERTO) || (eJornada.getEstado() == Jornada.ESTADO_REGISTRADO)){
            	throw new Exception(gestorDiccionario.getMessage("sgo.jornadaNoCerrada",null,locale));
            }
            
            parametros = new ParametrosListar();
            parametros.setIdJornada(eJornada.getId());
            parametros.setPaginacion(Constante.SIN_PAGINACION);
            parametros.setCampoOrdenamiento("id");
			parametros.setSentidoOrdenamiento("asc");
            //recuperamos los contometros de la jornada
			oRespuesta = dContometroJornada.recuperarRegistros(parametros);
            if (oRespuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int i = 0; i < oRespuesta.contenido.carga.size(); i++){
            	ContometroJornada eContometroJornada = (ContometroJornada) oRespuesta.contenido.carga.get(i);
				listaContometroJornada.add(eContometroJornada);
			}
            //recuperamos los tanques de la jornada
            parametros.setTanqueDeCierre(TanqueJornada.TANQUE_CIERRE);
            oRespuesta = dTanqueJornada.recuperarRegistros(parametros);
            if (oRespuesta.estado==false){        	
            	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            }
            for (int k = 0; k < oRespuesta.contenido.carga.size(); k++){
            	TanqueJornada eTanqueJornada = (TanqueJornada) oRespuesta.contenido.carga.get(k);
				listaTanqueJornada.add(eTanqueJornada);
			}
            //le asingamos los contometros y los tanque a la jornada
            eJornada.setRegistroNuevo(false);
            eJornada.setContometroJornada(listaContometroJornada);
            eJornada.setTanqueJornada(listaTanqueJornada);
            listaRegistros.add(eJornada);

            contenido.carga = listaRegistros;
            oRespuesta.contenido = contenido;
		} else {
			 RespuestaCompuesta rpta = dOperacion.recuperarRegistro(parametros.getIdOperacion());
			 if (rpta.estado == false) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			 }
			 Operacion op = (Operacion) rpta.contenido.carga.get(0);
			 ultimoDia = op.getFechaInicioPlanificacion();
			 if(ultimoDia == null){
			   throw new Exception(gestorDiccionario.getMessage("sgo.fechaInicioPlanificacionNula", new Object[] {op.getNombre()}, locale));
			 }
			 
			 Jornada eJornada = new Jornada();
			 eJornada.setFechaOperativa(op.getFechaInicioPlanificacion());
			 
			 ParametrosListar parametros3 = new ParametrosListar();
			 parametros3.setFiltroEstacion(parametros.getFiltroEstacion());
			 parametros3.setFiltroEstado(Constante.ESTADO_ACTIVO);
			 parametros3.setPaginacion(Constante.SIN_PAGINACION);
			 parametros3.setCampoOrdenamiento("id");
			 parametros3.setSentidoOrdenamiento("asc");
			 //para asignar la estaciÃ¯Â¿Â½n a la jornada
			 oRespuesta = dEstacion.recuperarRegistro(parametros.getFiltroEstacion());
			 if (oRespuesta.estado == false || oRespuesta.contenido.carga.size() == 0) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
			 }
			 eJornada.setEstacion((Estacion) oRespuesta.contenido.carga.get(0));
			 
			 oRespuesta = dContometro.recuperarRegistros(parametros3);
			 if (oRespuesta.estado == false || oRespuesta.contenido.carga.size() == 0) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.EstacionSinContometro",new Object[] {  eJornada.getEstacion().getNombre() },locale));
			 }
			 for (int k = 0; k < oRespuesta.contenido.carga.size(); k++){
			 Contometro eContometro = (Contometro) oRespuesta.contenido.carga.get(k);
            	listaContometro.add(eContometro);
			 }
			 oRespuesta = dTanque.recuperarRegistros(parametros3);
			 if (oRespuesta.estado == false || oRespuesta.contenido.carga.size() == 0) {
			   throw new Exception(gestorDiccionario.getMessage("sgo.EstacionSinTanque",new Object[] {  eJornada.getEstacion().getNombre() },locale));
			 }
			 for (int k = 0; k < oRespuesta.contenido.carga.size(); k++){
			 Tanque eTanque = (Tanque) oRespuesta.contenido.carga.get(k);
            	listaTanque.add(eTanque);
			 }
			 eJornada.setTotalDespachos(0);
			 eJornada.setRegistroNuevo(true);
			 eJornada.setTanque(listaTanque);
			 eJornada.setContometro(listaContometro);

			 listaRegistros.add(eJornada);
	         contenido.carga = listaRegistros;
	         oRespuesta.contenido = contenido;
			 
		}

		oRespuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarExitoso", null, locale);
	  	} catch (Exception ex) {
	  		Utilidades.gestionaError(ex, sNombreClase, "recuperarApertura");
	  		//ex.printStackTrace();
	  		oRespuesta.estado = false;
	  		oRespuesta.mensaje = ex.getMessage();
	  	}
	  return oRespuesta;
	 }

	
	@RequestMapping(value = URL_GUARDAR_RELATIVA ,method = RequestMethod.POST)
	public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody Jornada eJornada,HttpServletRequest peticionHttp,Locale locale){		 
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		Bitacora eBitacora= null;
		String ContenidoAuditoria ="";
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		String direccionIp="";
		String ClaveGenerada="";
		ContometroJornada eContometroJornada = null;
		TanqueJornada eTanqueJornada = null;
		ObjectMapper mapper = null;
		int idJornadaGenerada;
		try {
			//Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
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

			if(!Utilidades.esValido(eJornada.getIdOperario1()) || eJornada.getIdOperario1() <= 0){
				throw new Exception(gestorDiccionario.getMessage("sgo.noOperario1", null, locale));
			}
			if(!Utilidades.esValido(eJornada.getIdOperario2())){
				throw new Exception(gestorDiccionario.getMessage("sgo.noOperario2", null, locale));
			}
			if(eJornada.getIdOperario1() == eJornada.getIdOperario2()){
				throw new Exception(gestorDiccionario.getMessage("sgo.operadoreIguales", null, locale));
			}
			
			
			ArrayList<TanqueJornada> tanqueApertura = new ArrayList<TanqueJornada>();

			for (int cont = 0; cont < eJornada.getTanqueJornada().size(); cont++) {
				if(eJornada.getTanqueJornada().get(cont).getEnLinea() == 1){
					tanqueApertura.add(eJornada.getTanqueJornada().get(cont));
				}
			}
			
			if(tanqueApertura.size() == 0){
				throw new Exception(gestorDiccionario.getMessage("sgo.jornada.noExisteSeleccionProducto", null, locale));
			}
			
			for (int a = 0; a < tanqueApertura.size(); a++) {
				int cantidadProductos = 0;
				int idProd = tanqueApertura.get(a).getIdProducto();
				for (int b = 0; b < tanqueApertura.size(); b++) {
					if(idProd == tanqueApertura.get(b).getIdProducto()){
						cantidadProductos++;
					}
				}
				if(cantidadProductos > 1){
					RespuestaCompuesta producto = dProducto.recuperarRegistro(idProd);
					if (respuesta.estado==false){     	
		              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
		            }
					Producto respuestaProducto = (Producto) producto.contenido.carga.get(0);
					throw new Exception(gestorDiccionario.getMessage("sgo.jornada.soloUnTanqueDespachando",new Object[] {  respuestaProducto.getNombre() },locale));
				}
				
			}
			
			//TODO
			//validacion de producto por tanque
			ParametrosListar argumentosProd = new ParametrosListar();
			//argumentosProd.setFiltroOperacion(eJornada.get)
			argumentosProd.setFiltroEstacion(eJornada.getIdEstacion());
			RespuestaCompuesta listaProdPorEstacion = dProducto.recuperarRegistrosPorOperacion(argumentosProd);
			for (int b = 0; b < listaProdPorEstacion.contenido.carga.size(); b++) {
				Producto prodEstacion =(Producto) listaProdPorEstacion.contenido.carga.get(b);
				int atencionProducto = 0;
				
				for (int a = 0; a < tanqueApertura.size(); a++) {
					int idProd = tanqueApertura.get(a).getIdProducto();
					
					if(idProd == prodEstacion.getId()){
						atencionProducto++;
					}
				}

				if(atencionProducto == 0){
					throw new Exception(gestorDiccionario.getMessage("sgo.jornada.soloUnTanquePorProducto", new Object[] {  prodEstacion.getNombre() }, locale));
				}
			}
		
        	eJornada.setActualizadoEl(Calendar.getInstance().getTime().getTime());
            eJornada.setActualizadoPor(principal.getID()); 
           	eJornada.setCreadoEl(Calendar.getInstance().getTime().getTime());
            eJornada.setCreadoPor(principal.getID());
            eJornada.setIpActualizacion(direccionIp);
            eJornada.setIpCreacion(direccionIp);
            eJornada.setEstado(Jornada.ESTADO_ABIERTO);
            
            Respuesta validacion = Utilidades.validacionXSS(eJornada, gestorDiccionario, locale);
		    if (validacion.estado == false) {
		      throw new Exception(validacion.valor);
		    }
            
            respuesta= dJornada.guardarRegistro(eJornada);
            //Verifica si la accion se ejecuto de forma satisfactoria
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
            }

            ClaveGenerada = respuesta.valor;
            idJornadaGenerada = Integer.parseInt(respuesta.valor);
            //Guardar en la bitacora
            mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
            ContenidoAuditoria =  mapper.writeValueAsString(eJornada);
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_GUARDAR_COMPLETA);
            eBitacora.setTabla(JornadaDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(ClaveGenerada);
            eBitacora.setContenido(ContenidoAuditoria);
            eBitacora.setRealizadoEl(eJornada.getCreadoEl());
            eBitacora.setRealizadoPor(eJornada.getCreadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }    
            
            //esto para guardar los contometros de la jornada
            for (int contador = 0; contador < eJornada.getContometroJornada().size(); contador++) {
            	eContometroJornada = eJornada.getContometroJornada().get(contador);
            	eContometroJornada.setIdJornada(idJornadaGenerada);
            	eContometroJornada.setEstadoServicio(ContometroJornada.ESTADO_ABIERTO);

            	validacion = Utilidades.validacionContometroAperturaXSS(eContometroJornada, gestorDiccionario, locale);
    		    if (validacion.estado == false) {
    		      throw new Exception(validacion.valor);
    		    }
            	RespuestaCompuesta respuestaContJornada = dContometroJornada.guardarRegistro(eContometroJornada);
				if (respuestaContJornada.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}
				
				// Guardar en la bitacora
				mapper = new ObjectMapper();
				ContenidoAuditoria = mapper.writeValueAsString(eContometroJornada);
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion("admin/jornadaContometro/crear");
				eBitacora.setTabla(ContometroJornadaDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(respuestaContJornada.valor);
				eBitacora.setContenido(ContenidoAuditoria);
				eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				respuesta= dBitacora.guardarRegistro(eBitacora);
				if (respuesta.estado==false){     	
	              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
	            }
			}

            //esto para guardar los tanques de la jornada
            for (int cont = 0; cont < eJornada.getTanqueJornada().size(); cont++) {
            	eTanqueJornada = eJornada.getTanqueJornada().get(cont);
            	eTanqueJornada.setIdJornada(idJornadaGenerada);
            	eTanqueJornada.setApertura(TanqueJornada.TANQUE_APERTURA);
            	
            	 //Asignamos la hora inicial
    		    long time = eJornada.getFechaOperativa().getTime();
            	Timestamp diaHoradeApertura = new Timestamp(time);
            	diaHoradeApertura.setHours(00);
            	diaHoradeApertura.setMinutes(00);
            	diaHoradeApertura.setSeconds(00);
            	eTanqueJornada.setHoraInicial(diaHoradeApertura);
            	
            	
            	validacion = Utilidades.validacionTanqueAperturaXSS(eTanqueJornada, gestorDiccionario, locale);
    		    if (validacion.estado == false) {
    		      throw new Exception(validacion.valor);
    		    }
    		    
    		   /* boolean validaFechas = Utilidades.comparaTimestampConDate(eTanqueJornada.getHoraInicial(), eJornada.getFechaOperativa());
    		    if (validaFechas == false) {
					throw new Exception("La Fecha de Hora Inicial del tanque " + eTanqueJornada.getDescripcionTanque() + " debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
				}*/
     		   
    		    eTanqueJornada.setHoraFinal(null);
    		    
            	RespuestaCompuesta respuestaTanqueJornada = dTanqueJornada.guardarRegistro(eTanqueJornada);
				if (respuestaTanqueJornada.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}

				// Guardar en la bitacora
				mapper = new ObjectMapper();
				ContenidoAuditoria = mapper.writeValueAsString(eTanqueJornada);
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion("admin/tanqueJornada/crear");
				eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(respuestaTanqueJornada.valor);
				eBitacora.setContenido(ContenidoAuditoria);
				eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				respuesta= dBitacora.guardarRegistro(eBitacora);
				if (respuesta.estado==false){     	
	              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
	            }
			}
            
        	respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eJornada.getFechaCreacion().substring(0, 9),eJornada.getFechaCreacion().substring(10),principal.getIdentidad() },locale);
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
	public @ResponseBody RespuestaCompuesta actualizarJornada(@RequestBody Jornada eJornada, HttpServletRequest peticionHttp,Locale locale){
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		ContometroJornada eContometroJornada = null;
		TanqueJornada eTanqueJornada = null;
		Muestreo eMuestreoJornada = null;
		Bitacora eBitacora=null;
		String direccionIp="";
		try {
			//Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
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
			eJornada.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eJornada.setActualizadoPor(principal.getID()); 
			eJornada.setIpActualizacion(direccionIp);
            respuesta= dJornada.actualizarRegistro(eJornada);
            if (respuesta.estado==false){          	
            	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(JornadaDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf(eJornada.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eJornada));
            eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
            eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  

            //esto para actualizar los contometros de la jornada
            for (int contador = 0; contador < eJornada.getContometroJornada().size(); contador++) {
            	eContometroJornada = eJornada.getContometroJornada().get(contador);
            	eContometroJornada.setIdJornada(eJornada.getId());
            	
            	Respuesta validacion = Utilidades.validacionContometroCierreXSS(eContometroJornada, gestorDiccionario, locale);
    		    if (validacion.estado == false) {
    		      throw new Exception(validacion.valor);
    		    }
    		    
            	RespuestaCompuesta respuestaContJornada = dContometroJornada.actualizarRegistro(eContometroJornada);
				if (respuestaContJornada.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}
				// Guardar en la bitacora
				mapper = new ObjectMapper();
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion("admin/jornadaContometro/actualizar");
				eBitacora.setTabla(ContometroJornadaDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(respuestaContJornada.valor);
				eBitacora.setContenido(mapper.writeValueAsString(eContometroJornada));
				eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				respuesta= dBitacora.guardarRegistro(eBitacora);
				if (respuesta.estado==false){     	
	              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
	            }
			}

            //esto para actualizar los tanques de la jornada
            for (int cont = 0; cont < eJornada.getTanqueJornada().size(); cont++) {
            	eTanqueJornada = eJornada.getTanqueJornada().get(cont);
            	eTanqueJornada.setIdJornada(eJornada.getId());
            	eTanqueJornada.setCierre(TanqueJornada.TANQUE_CIERRE);
            	
            	long timeInicial = eJornada.getFechaOperativa().getTime();
            	Timestamp diaHoradeApertura = new Timestamp(timeInicial);
            	diaHoradeApertura.setHours(00);
            	diaHoradeApertura.setMinutes(00);
            	diaHoradeApertura.setSeconds(00);
            	eTanqueJornada.setHoraInicial(diaHoradeApertura);
            	
            	 //Asignamos la hora final de la jornada
    		    long time = eJornada.getFechaOperativa().getTime();
            	Timestamp diaHoradeCierre = new Timestamp(time);
            	diaHoradeCierre.setHours(23);
            	diaHoradeCierre.setMinutes(59);
            	diaHoradeCierre.setSeconds(59);
            	eTanqueJornada.setHoraFinal(diaHoradeCierre);
            	
            	Respuesta validacion = Utilidades.validacionTanqueCierreXSS(eTanqueJornada, gestorDiccionario, locale);
    		    if (validacion.estado == false) {
    		      throw new Exception(validacion.valor);
    		    }

    		    
    		   /* boolean validaFechas = Utilidades.comparaTimestampConDate(eTanqueJornada.getHoraFinal(), eJornada.getFechaOperativa());
    		    if (validaFechas == false) {
					throw new Exception("La Fecha de Hora Final del tanque " + eTanqueJornada.getDescripcionTanque() + " debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
				}*/
    		    
            	RespuestaCompuesta respuestaTanqueJornada = dTanqueJornada.actualizarRegistro(eTanqueJornada);
				if (respuestaTanqueJornada.estado == false) {
					throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
				}
				// Guardar en la bitacora
				mapper = new ObjectMapper();
				eBitacora.setUsuario(principal.getNombre());
				eBitacora.setAccion("admin/tanqueJornada/actualizar");
				eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
				eBitacora.setIdentificador(respuestaTanqueJornada.valor);
				eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
				eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
				eBitacora.setRealizadoPor(principal.getID());
				respuesta= dBitacora.guardarRegistro(eBitacora);
				if (respuesta.estado==false){     	
	              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
	            }
			}

            RespuestaCompuesta respuestaMuestreoJornada = null;
            //esto para actualizar los tanques de la jornada
            for (int indice = 0; indice < eJornada.getMuestreo().size(); indice++) {
            	eMuestreoJornada = eJornada.getMuestreo().get(indice);
            	eMuestreoJornada.setIdJornada(eJornada.getId());

            	if(eMuestreoJornada.getOrigen() == Muestreo.ORIGEN_CIERRE){
            		
            		 //primero eliminamos los registros que hayan sido muestreados al cerrar la jornada (si los hubiera)
                    long time = eJornada.getFechaOperativa().getTime();
                	Timestamp diaHoradeCierre = new Timestamp(time);
                	diaHoradeCierre.setHours(23);
                	diaHoradeCierre.setMinutes(59);
                	diaHoradeCierre.setSeconds(59);
                	eMuestreoJornada.setHoraMuestreo(diaHoradeCierre);
                	
	            	Respuesta validacion = Utilidades.validacionXSS(eMuestreoJornada, gestorDiccionario, locale);
	    		    if (validacion.estado == false) {
	    		      throw new Exception(validacion.valor);
	    		    }
                	
                	RespuestaCompuesta respuestaEliminaMuestreo = dMuestreo.eliminarRegistroPorHoraMuestreo(eJornada.getId(), diaHoradeCierre);
                	if (respuestaEliminaMuestreo.estado == false) {
        				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
        			}

//	    		    boolean validaFechas = Utilidades.comparaTimestampConDate(eMuestreoJornada.getHoraMuestreo(), eJornada.getFechaOperativa());
//	    		    if (validaFechas == false) {
//						throw new Exception("La Fecha de Hora Final del muestreo debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
//					}
	    		    
	            	respuestaMuestreoJornada = dMuestreo.guardarRegistro(eMuestreoJornada);
					if (respuestaMuestreoJornada.estado == false) {
						throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
					}
					
					// Guardar en la bitacora
					mapper = new ObjectMapper();
					eBitacora.setUsuario(principal.getNombre());
					eBitacora.setAccion("admin/muestreo/crear");
					eBitacora.setTabla(MuestreoDao.NOMBRE_TABLA);
					eBitacora.setIdentificador(respuestaMuestreoJornada.valor);
					eBitacora.setContenido(mapper.writeValueAsString(eMuestreoJornada));
					eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
					eBitacora.setRealizadoPor(principal.getID());
					respuesta= dBitacora.guardarRegistro(eBitacora);
					if (respuesta.estado==false){     	
		              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
		            }
            	} else {
            		if(Utilidades.esValido(eMuestreoJornada.getId()) && eMuestreoJornada.getId() > 0){
            			Respuesta validacion = Utilidades.validacionXSS(eMuestreoJornada, gestorDiccionario, locale);
    	    		    if (validacion.estado == false) {
    	    		      throw new Exception(validacion.valor);
    	    		    }
    	    		    /*boolean validaFechas = Utilidades.comparaTimestampConDate(eMuestreoJornada.getHoraMuestreo(), eJornada.getFechaOperativa());
    	    		    if (validaFechas == false) {
    						throw new Exception("La Fecha del muestreo debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
    					}*/
            			respuestaMuestreoJornada= dMuestreo.actualizarRegistro(eMuestreoJornada);
            	        //Verifica si la accion se ejecuto de forma satisfactoria
            	        if (respuestaMuestreoJornada.estado==false){     	
            	          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
            	        }
            	        //Guardar en la bitacora
                        mapper = new ObjectMapper();
                        eBitacora.setUsuario(principal.getNombre());
                        eBitacora.setAccion("admin/muestreo/actualizar");
                        eBitacora.setTabla(MuestreoDao.NOMBRE_TABLA);
                        eBitacora.setIdentificador(String.valueOf( eMuestreoJornada.getId()));
                        eBitacora.setContenido(mapper.writeValueAsString(eMuestreoJornada));
                        eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
                        eBitacora.setRealizadoPor(principal.getID());
            		} else {
            			/*//valido que no exista un tegistro con los mismos valores
            			parametros = new ParametrosListar();
            			parametros.setFiltroPerteneceA(eVigencia.getPerteneceA());
            			parametros.setFiltroIdDocumento(eVigencia.getIdEntidad());
            			respuesta = dVigencia.recuperarRegistros(parametros);
            			if (respuesta.estado==false){     	
            	          	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            	        }
            			for(int i = 0; i < respuesta.contenido.carga.size(); i++){
            				Vigencia temp = (Vigencia) respuesta.contenido.carga.get(i);
            				if(temp.getIdDocumento() == eVigencia.getIdDocumento() && temp.getFechaEmision() == eVigencia.getFechaEmision() && temp.getFechaExpiracion() == eVigencia.getFechaExpiracion()){
            					throw new Exception(gestorDiccionario.getMessage("Ya existe un registro con los mismos valores",null,locale));
            				}
            			}*/
            			Respuesta validacion = Utilidades.validacionXSS(eMuestreoJornada, gestorDiccionario, locale);
    	    		    if (validacion.estado == false) {
    	    		      throw new Exception(validacion.valor);
    	    		    }
    	    		    /*boolean validaFechas = Utilidades.comparaTimestampConDate(eMuestreoJornada.getHoraMuestreo(), eJornada.getFechaOperativa());
    	    		    if (validaFechas == false) {
    						throw new Exception("La Fecha de Hora Final del muestreo debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
    					}*/
            			respuestaMuestreoJornada = dMuestreo.guardarRegistro(eMuestreoJornada);
    					if (respuestaMuestreoJornada.estado == false) {
    						throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    					}
    					
    					// Guardar en la bitacora
    					mapper = new ObjectMapper();
    					eBitacora.setUsuario(principal.getNombre());
    					eBitacora.setAccion("admin/muestreo/crear");
    					eBitacora.setTabla(MuestreoDao.NOMBRE_TABLA);
    					eBitacora.setIdentificador(respuestaMuestreoJornada.valor);
    					eBitacora.setContenido(mapper.writeValueAsString(eMuestreoJornada));
    					eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
    					eBitacora.setRealizadoPor(principal.getID());
            		}

                    respuesta= dBitacora.guardarRegistro(eBitacora);
                    if (respuesta.estado==false){     	
                      	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
                    }     
            	}
			}

         	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eJornada.getFechaActualizacion().substring(0, 9),eJornada.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
        	this.transaccion.commit(estadoTransaccion);
		} catch (Exception ex){
			Utilidades.gestionaError(ex, sNombreClase, "actualizarJornada");
			//ex.printStackTrace();
			this.transaccion.rollback(estadoTransaccion);
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	}

	@RequestMapping(value = URL_ELIMINAR_MUESTREO_JORNADA_RELATIVA ,method = RequestMethod.POST)
	public @ResponseBody RespuestaCompuesta eliminarMuestreo(@RequestBody Muestreo eMuestreo, HttpServletRequest peticionHttp,Locale locale){
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		Bitacora eBitacora=null;
		String direccionIp="";
		Jornada eJornada = new Jornada();
		try {
			//Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
			//Recuperar el usuario actual
			principal = this.getCurrentUser();
			//Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_ELIMINAR_MUESTREO_JORNADA_COMPLETA);
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
        	eJornada.setActualizadoEl(Calendar.getInstance().getTime().getTime());
        	eJornada.setActualizadoPor(principal.getID()); 
            eJornada.setIpActualizacion(direccionIp);
            eJornada.setId(eMuestreo.getIdJornada());

           // buscar la jornada por ID para pillar el estado
            respuesta= dJornada.ActualizarEstadoRegistro(eJornada);
            if (respuesta.estado==false){          	
            	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(JornadaDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eJornada.getId()));
            eBitacora.setContenido( mapper.writeValueAsString(eJornada));
            eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
            eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
            
            //Eliminamos el muestreo
        	respuesta= dMuestreo.eliminarRegistro(eMuestreo.getId());
            if (respuesta.estado==false){          	
            	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion("/admin/muestreo/crear");
            eBitacora.setTabla(MuestreoDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(respuesta.valor);
            eBitacora.setContenido(mapper.writeValueAsString(eMuestreo));
            eBitacora.setRealizadoEl(eJornada.getCreadoEl());
            eBitacora.setRealizadoPor(eJornada.getCreadoPor());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }
        	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eJornada.getFechaActualizacion().substring(0, 9),eJornada.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
        	this.transaccion.commit(estadoTransaccion);
		} catch (Exception ex){
			//ex.printStackTrace();
			Utilidades.gestionaError(ex, sNombreClase, "eliminarMuestreo");
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

@RequestMapping(value = URL_GUARDAR_CAMBIO_TANQUE_RELATIVA ,method = RequestMethod.POST)
public @ResponseBody RespuestaCompuesta registrarCambioTanqueJornada(@RequestBody Jornada eJornada, HttpServletRequest peticionHttp, Locale locale){
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	TanqueJornada eTanqueJornada = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	ObjectMapper mapper = null;
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
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

		if(!Utilidades.esValido(eJornada.getTanqueJornadaFinal().get(0).getHoraFinal())){
			throw new Exception("Debe informar la hora del tanque a cerrar.");
		}
		
		if(!Utilidades.esValido(eJornada.getTanqueJornadaInicial().get(0).getHoraInicial())){
			throw new Exception("Debe informar la hora del tanque a aperturar.");
		}
		
		if(!Utilidades.esValido(eJornada.getTanqueJornadaFinal().get(0).getIdTanque())){
			throw new Exception("Debe informar el tanque a cerrar.");
		}

		if(!Utilidades.esValido(eJornada.getTanqueJornadaInicial().get(0).getIdTanque())){
			throw new Exception("Debe informar el tanque a aperturar.");
		}

		if(eJornada.getTanqueJornadaFinal().get(0).getIdTanque() == eJornada.getTanqueJornadaInicial().get(0).getIdTanque()){
			throw new Exception("sgo.jornada.tanquesIguales");
		}
		
		if(eJornada.getTanqueJornadaFinal().get(0).getIdProducto() != eJornada.getTanqueJornadaInicial().get(0).getIdProducto()){
			throw new Exception(gestorDiccionario.getMessage("sgo.jornada.productosIguales",null,locale));
		}
		
        //esto para actualizar el tanque
        for (int cont = 0; cont < eJornada.getTanqueJornadaFinal().size(); cont++) {
        	eTanqueJornada = eJornada.getTanqueJornadaFinal().get(cont);
        	eTanqueJornada.setEnLinea(TanqueJornada.ESTADO_NO_DESPACHANDO);
        	eTanqueJornada.setCierre(TanqueJornada.TANQUE_NO_CIERRE);
        	eTanqueJornada.setEstadoServicio(TanqueJornada.ESTADO_SERVICIO_ACTIVO);

        	Respuesta validacion = Utilidades.validacionCambioTanqueFinalXSS(eTanqueJornada, gestorDiccionario, locale);
        	if (validacion.estado == false) {
		      throw new Exception(validacion.valor);
		    }

        	//boolean validaFechas = Utilidades.comparaTimestampConDate(eTanqueJornada.getHoraFinal(), eJornada.getFechaOperativa());
        	//if (validaFechas == false) {
			//	throw new Exception("La Fecha de Hora Final del tanque " + eTanqueJornada.getDescripcionTanque() + " debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
			//}
        	respuesta = dTanqueJornada.actualizarRegistro(eTanqueJornada);
			if (respuesta.estado == false) {
				throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
			}
			// Guardar en la bitacora
			mapper = new ObjectMapper();
			eBitacora.setUsuario(principal.getNombre());
			eBitacora.setAccion("admin/tanqueJornada/actualizar");
			eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
			eBitacora.setIdentificador(String.valueOf(eTanqueJornada.getIdTjornada()));
			eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
			eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
			eBitacora.setRealizadoPor(principal.getID());
			respuesta= dBitacora.guardarRegistro(eBitacora);
			if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }
		}
        
      //esto para guardar el tanque
        for (int cont = 0; cont < eJornada.getTanqueJornadaInicial().size(); cont++) {
        	eTanqueJornada = eJornada.getTanqueJornadaInicial().get(cont);
        	
        	eTanqueJornada.setEnLinea(TanqueJornada.ESTADO_DESPACHANDO);
        	eTanqueJornada.setApertura(TanqueJornada.TANQUE_NO_APERTURA);
        	eTanqueJornada.setCierre(TanqueJornada.TANQUE_NO_CIERRE);
        	eTanqueJornada.setEstadoServicio(TanqueJornada.ESTADO_SERVICIO_ACTIVO);

        	Respuesta validacion = Utilidades.validacionCambioTanqueInicialXSS(eTanqueJornada, gestorDiccionario, locale);
        	if (validacion.estado == false) {
  		      throw new Exception(validacion.valor);
  		    }

//        	boolean validaFechas = Utilidades.comparaTimestampConDate(eTanqueJornada.getHoraInicial(), eJornada.getFechaOperativa());
//        	if (validaFechas == false) {
//				throw new Exception("La Fecha de Hora Inicial del tanque " + eTanqueJornada.getDescripcionTanque() + " debe ser igual a la fecha del dÃƒÂ­a operativo: " + Utilidades.convierteDateAString(eJornada.getFechaOperativa(), Constante.FORMATO_FECHA_DDMMYYYY) );
//			}
        	respuesta = dTanqueJornada.guardarRegistro(eTanqueJornada);
    	   // Verifica si la accion se ejecuto de forma satisfactoria
    	   if (respuesta.estado == false) {
    	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
    	   }
    	   // Guardar en la bitacora
    	   mapper = new ObjectMapper(); 
    	   eBitacora.setUsuario(principal.getNombre());
    	   eBitacora.setAccion(URL_GUARDAR_COMPLETA);
    	   eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
    	   eBitacora.setIdentificador(respuesta.valor);
    	   eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
    	   eBitacora.setRealizadoEl(eTanqueJornada.getCreadoEl());
    	   eBitacora.setRealizadoPor(eTanqueJornada.getCreadoPor());
    	   respuesta = dBitacora.guardarRegistro(eBitacora);
    	   if (respuesta.estado == false) {
    	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido", null, locale));
    	   }
    	   
    	   //valido si el tanque de la jornada tiene valores finales.
    	   respuesta = dTanqueJornada.recuperarRegistro(eTanqueJornada.getIdTjornada());
    	   if (respuesta.estado == false) {
       	    throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
       	   }
    	   
    	   TanqueJornada eActualizarTanque = (TanqueJornada) respuesta.contenido.carga.get(0);
    	   
    	   if(eActualizarTanque.getMedidaFinal() == 0){
    	     eActualizarTanque.setMedidaFinal(eTanqueJornada.getMedidaInicial());
    		 eActualizarTanque.setVolumenObservadoFinal(eTanqueJornada.getVolumenObservadoInicial());
    		 eActualizarTanque.setApiCorregidoFinal(eTanqueJornada.getApiCorregidoInicial());
    		 eActualizarTanque.setTemperaturaFinal(eTanqueJornada.getTemperaturaInicial());
    		 eActualizarTanque.setFactorCorreccionFinal(eTanqueJornada.getFactorCorreccionInicial());
    		 eActualizarTanque.setVolumenCorregidoFinal(eTanqueJornada.getVolumenCorregidoInicial());
    		 eActualizarTanque.setEstadoServicio(TanqueJornada.ESTADO_SERVICIO_ACTIVO);
    		 eActualizarTanque.setEnLinea(TanqueJornada.ESTADO_NO_DESPACHANDO);
    		 eActualizarTanque.setVolumenAguaFinal(0);
    		 eActualizarTanque.setHoraFinal(eTanqueJornada.getHoraInicial());
    		 eActualizarTanque.setCierre(TanqueJornada.TANQUE_NO_CIERRE);
    		 
    		 respuesta = dTanqueJornada.actualizarRegistro(eActualizarTanque);
	   		 if (respuesta.estado == false) {
	   		   throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
	   		 }
	   		 // Guardar en la bitacora
	   		 mapper = new ObjectMapper();
	   		 eBitacora.setUsuario(principal.getNombre());
	   		 eBitacora.setAccion("admin/tanqueJornada/actualizar");
	   		 eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
	   		 eBitacora.setIdentificador(String.valueOf(eTanqueJornada.getIdTjornada()));
	   		 eBitacora.setContenido(mapper.writeValueAsString(eTanqueJornada));
	   		 eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
	   		 eBitacora.setRealizadoPor(principal.getID());
	   		 respuesta= dBitacora.guardarRegistro(eBitacora);
	   		 if (respuesta.estado==false){     	
	           throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
	         }
    		   
    	   }
    	   
    	   
		}
        respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eJornada.getFechaActualizacion().substring(0, 9),eJornada.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
    	this.transaccion.commit(estadoTransaccion);
	} catch (Exception ex){
		Utilidades.gestionaError(ex, sNombreClase, "actualizarJornada");
		//ex.printStackTrace();
		this.transaccion.rollback(estadoTransaccion);
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
}

  @RequestMapping(value = URL_ACTUALIZAR_ESTADO_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody RespuestaCompuesta actualizarEstadoRegistro(@RequestBody Jornada eJornada,HttpServletRequest peticionHttp,Locale locale){
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
		definicionTransaccion = new DefaultTransactionDefinition();
		estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		eBitacora = new Bitacora();
		//Recuperar el usuario actual
		principal = this.getCurrentUser();
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_ESTADO_COMPLETA);
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
		eJornada.setActualizadoEl(Calendar.getInstance().getTime().getTime());
		eJornada.setActualizadoPor(principal.getID()); 
		eJornada.setIpActualizacion(direccionIp);
        respuesta= dJornada.ActualizarEstadoRegistro(eJornada);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
        eBitacora.setTabla(JornadaDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eJornada.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eJornada));
        eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
        eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
        
        ParametrosListar parametrosTanqueJornada = new ParametrosListar();
        parametrosTanqueJornada.setTanqueDeCierre(TanqueJornada.TANQUE_CIERRE);
        parametrosTanqueJornada.setIdJornada(eJornada.getId());
        
        respuesta = dTanqueJornada.recuperarRegistros(parametrosTanqueJornada);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        
        for (int a = 0; a < respuesta.contenido.carga.size(); a++){
    	  TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(a);
    	  eTanqueJornada.setCierre(TanqueJornada.TANQUE_NO_CIERRE);
    	  if(eTanqueJornada.getEnLinea() == 1){
    		  eTanqueJornada.setHoraFinal(null);
    	  }

    	  RespuestaCompuesta respuestaReaperturaTanque= dTanqueJornada.actualizarReaperturaJornada(eTanqueJornada);
          if (respuestaReaperturaTanque.estado==false){          	
          	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
          }
          //Guardar en la bitacora
          mapper = new ObjectMapper();
          eBitacora.setUsuario(principal.getNombre());
          eBitacora.setAccion("/admin/tanqueJornada/actualizarReaperturaJornada");
          eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
          eBitacora.setIdentificador(String.valueOf( eTanqueJornada.getId()));
          eBitacora.setContenido( mapper.writeValueAsString(eTanqueJornada));
          eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
          eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
          RespuestaCompuesta respuestaBitacora= dBitacora.guardarRegistro(eBitacora);
          if (respuestaBitacora.estado==false){     	
           	 throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
          }
        }

    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eJornada.getFechaActualizacion().substring(0, 9),eJornada.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
    	this.transaccion.commit(estadoTransaccion);
	} catch (Exception ex){
		//ex.printStackTrace();
		Utilidades.gestionaError(ex, sNombreClase, "actualizarEstadoRegistro");
		this.transaccion.rollback(estadoTransaccion);
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
  }
  
  @RequestMapping(value = URL_PROCESO_CIERRE_RELATIVA ,method = RequestMethod.POST)
  public @ResponseBody Respuesta procesoCierre(@RequestBody Jornada eJornada, HttpServletRequest peticionHttp,Locale locale){
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dJornada.getDataSource());
		definicionTransaccion = new DefaultTransactionDefinition();
		estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		eBitacora = new Bitacora();
		//Recuperar el usuario actual
		principal = this.getCurrentUser();
		//Recuperar el enlace de la accion
		respuesta = dEnlace.recuperarRegistro(URL_ACTUALIZAR_ESTADO_COMPLETA);
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
		eJornada.setActualizadoEl(Calendar.getInstance().getTime().getTime());
		eJornada.setActualizadoPor(principal.getID()); 
		eJornada.setIpActualizacion(direccionIp);
        respuesta= dJornada.ActualizarEstadoRegistro(eJornada);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_ESTADO_COMPLETA);
        eBitacora.setTabla(JornadaDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eJornada.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eJornada));
        eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
        eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
        
        ParametrosListar parametrosTanqueJornada = new ParametrosListar();
        parametrosTanqueJornada.setTanqueDeCierre(TanqueJornada.TANQUE_CIERRE);
        parametrosTanqueJornada.setIdJornada(eJornada.getId());
        
        respuesta = dTanqueJornada.recuperarRegistros(parametrosTanqueJornada);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        
        for (int a = 0; a < respuesta.contenido.carga.size(); a++){
    	  TanqueJornada eTanqueJornada = (TanqueJornada) respuesta.contenido.carga.get(a);
    	  eTanqueJornada.setCierre(TanqueJornada.TANQUE_NO_CIERRE);
    	  if(eTanqueJornada.getEnLinea() == 1){
    		  eTanqueJornada.setHoraFinal(null);
    	  }

    	  RespuestaCompuesta respuestaReaperturaTanque= dTanqueJornada.actualizarReaperturaJornada(eTanqueJornada);
          if (respuestaReaperturaTanque.estado==false){          	
          	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
          }
          //Guardar en la bitacora
          mapper = new ObjectMapper();
          eBitacora.setUsuario(principal.getNombre());
          eBitacora.setAccion("/admin/tanqueJornada/actualizarReaperturaJornada");
          eBitacora.setTabla(TanqueJornadaDao.NOMBRE_TABLA);
          eBitacora.setIdentificador(String.valueOf( eTanqueJornada.getId()));
          eBitacora.setContenido( mapper.writeValueAsString(eTanqueJornada));
          eBitacora.setRealizadoEl(eJornada.getActualizadoEl());
          eBitacora.setRealizadoPor(eJornada.getActualizadoPor());
          RespuestaCompuesta respuestaBitacora= dBitacora.guardarRegistro(eBitacora);
          if (respuestaBitacora.estado==false){     	
           	 throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
          }
        }

    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eJornada.getFechaActualizacion().substring(0, 9),eJornada.getFechaActualizacion().substring(10),principal.getIdentidad() },locale);;
    	this.transaccion.commit(estadoTransaccion);
	} catch (Exception ex){
		//ex.printStackTrace();
		Utilidades.gestionaError(ex, sNombreClase, "actualizarEstadoRegistro");
		this.transaccion.rollback(estadoTransaccion);
		respuesta.estado=false;
		respuesta.contenido = null;
		respuesta.mensaje=ex.getMessage();
	}
	return respuesta;
  }
  
}