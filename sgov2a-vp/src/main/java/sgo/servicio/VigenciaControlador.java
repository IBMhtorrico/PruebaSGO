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

import sgo.datos.BitacoraDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.EnlaceDao;
import sgo.datos.TransporteDao;
import sgo.datos.VigenciaDao;
import sgo.entidad.Bitacora;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Vigencia;
import sgo.negocio.VigenciaBusiness;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class VigenciaControlador {
	@Autowired
	private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
	@Autowired
	private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
	@Autowired
	private EnlaceDao dEnlace;
	@Autowired
	private TransporteDao dTransporte;
	@Autowired
	private MenuGestor menu;
	@Autowired
	private DiaOperativoDao dDiaOperativo;
	@Autowired
	private VigenciaDao dVigencia;
	@Autowired
	private VigenciaBusiness vigenciaBusiness;
	
	//
	private DataSourceTransactionManager transaccion;//Gestor de la transaccion
	//urls generales
	private static final String URL_GESTION_COMPLETA="/admin/vigencia";
	private static final String URL_GESTION_RELATIVA="/vigencia";
	private static final String URL_GUARDAR_COMPLETA="/admin/vigencia/crear";
	private static final String URL_GUARDAR_RELATIVA="/vigencia/crear";
	private static final String URL_LISTAR_COMPLETA="/admin/vigencia/listar";
	private static final String URL_LISTAR_RELATIVA="/vigencia/listar";
	private static final String URL_ACTUALIZAR_COMPLETA="/admin/vigencia/actualizar";
	private static final String URL_ACTUALIZAR_RELATIVA="/vigencia/actualizar";
	private static final String URL_RECUPERAR_COMPLETA="/admin/vigencia/recuperar";
	private static final String URL_RECUPERAR_RELATIVA="/vigencia/recuperar";
	private static final String URL_ELIMINAR_COMPLETA="/admin/vigencia/eliminar";
	private static final String URL_ELIMINAR_RELATIVA="/vigencia/eliminar";
	private static final String URL_ACTUALIZAR_ESTADO_COMPLETA="/admin/vigencia/actualizarEstado";
	private static final String URL_ACTUALIZAR_ESTADO_RELATIVA="/vigencia/actualizarEstado";
	private static final String URL_VERIFICA_COMPLETA="/admin/vigencia/verifica";
	private static final String URL_VERIFICA_RELATIVA="/vigencia/verifica";
	
	
	
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
    mapaValores.put("MENSAJE_CAMBIAR_ESTADO", gestorDiccionario.getMessage("sgo.mensajeCambiarEstado",null,locale));
    mapaValores.put("MENSAJE_ELIMINAR_REGISTRO", gestorDiccionario.getMessage("sgo.mensajeEliminarRegistro",null,locale));

    mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando",null,locale));
    mapaValores.put("MENSAJE_RECUPERAR_EXITOSO", gestorDiccionario.getMessage("sgo.recuperarExitoso",null,locale));
    mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal",null,locale));

	 }catch(Exception ex){
	  
	 }
	 return mapaValores;
	}
	
	@RequestMapping(URL_GESTION_RELATIVA)
	public ModelAndView mostrarFormulario( Locale locale){
		ModelAndView vista =null;
		AuthenticatedUserDetails principal = null;
		ArrayList<?> listaEnlaces = null;
		RespuestaCompuesta respuesta = null;
		HashMap<String,String> mapaValores= null;
		try {
			principal = this.getCurrentUser();
			respuesta = menu.Generar(principal.getRol().getId(),URL_GESTION_COMPLETA);
			if (respuesta.estado==false){
				throw new Exception(gestorDiccionario.getMessage("sgo.menuNoGenerado",null,locale));
			}

			listaEnlaces = (ArrayList<?>) respuesta.contenido.carga;
			mapaValores = recuperarMapaValores(locale);
			vista = new ModelAndView("plantilla");
			vista.addObject("vistaJSP","mantenimiento/vigencia.jsp");
			vista.addObject("vistaJS","mantenimiento/vigencia.js");
			vista.addObject("identidadUsuario",principal.getIdentidad());
			vista.addObject("menu",listaEnlaces);
			vista.addObject("mapaValores",mapaValores);
			vista.addObject("fechaActual", dDiaOperativo.recuperarFechaActual().valor);
		} catch(Exception ex){
			
		}
		return vista;
	}
	
	
	
	
	@RequestMapping(value = URL_VERIFICA_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta verificaVigencia(HttpServletRequest httpRequest, Locale locale){
		RespuestaCompuesta respuesta = null;
		ParametrosListar parametros= null;
		AuthenticatedUserDetails principal = null;
		String mensajeRespuesta="";
		try {
			//Recuperar el usuario actual
			principal = this.getCurrentUser(); 
			//Recuperar el enlace de la accion
			respuesta = dEnlace.recuperarRegistro(URL_VERIFICA_COMPLETA);
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
			if (httpRequest.getParameter("filtroIdDocumento") != null) {
				parametros.setFiltroIdDocumento(Integer.parseInt(httpRequest.getParameter("filtroIdDocumento")));
			}

			if (httpRequest.getParameter("filtroPerteneceA") != null) {
				parametros.setFiltroPerteneceA(Integer.parseInt(httpRequest.getParameter("filtroPerteneceA")));
			}
			
			if (httpRequest.getParameter("filtroIdEntidad") != null) {//DOCUMENTO_CONDUCTOR=1;DOCUMENTO_CISTERNA=2;		
				parametros.setFiltroIdEntidad(Integer.parseInt(httpRequest.getParameter("filtroIdEntidad")));
			}
			//Recuperar registros
			
			String validacion=vigenciaBusiness.verificaVigenciaDocumento(parametros, locale);
			
//			respuesta = dVigencia.recuperarRegistros(parametros);
//	        if (respuesta.estado==false){        	
//	        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
//	        }
//	        StringBuilder documentos=new StringBuilder();
//	        if(respuesta.getContenido().getCarga()!=null && respuesta.getContenido().getCarga().size()>0){
//	        	for(Vigencia vigencia:(ArrayList<Vigencia>)respuesta.getContenido().getCarga()){	        	       
//	                Date fechaActual=dDiaOperativo.recuperarFechaActualDateSql("yyyy-mm-dd");
//	                if(vigencia.getFechaExpiracion().compareTo(fechaActual)<0){//documento caducado
//	                	if(vigencia.getIdEntidad()==Vigencia.DOCUMENTO_CISTERNA){
//	                		RespuestaCompuesta respuestaCisterna=dCisternaDao.recuperarRegistro(vigencia.getPerteneceA());
//	            	        if (respuestaCisterna.estado==false){        	
//	            	        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
//	            	        }
//	                		Cisterna cisterna=(Cisterna)respuestaCisterna.contenido.getCarga().get(0);
//	                		documentos.append("El Documento :"+vigencia.getDocumento().getNombreDocumento()+" de la Cisterna "+cisterna.getPlacaCisternaTracto()+" se encuentra caducado, fecha de expiración "+vigencia.getFechaExpiracion());
//	                	}else if(vigencia.getIdEntidad()==Vigencia.DOCUMENTO_CONDUCTOR){
//	                		RespuestaCompuesta respuestaConductor= dConductorDao.recuperarRegistro(vigencia.getPerteneceA());
//	            	        if (respuestaConductor.estado==false){        	
//	            	        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
//	            	        }
//	            	        Conductor conductor=(Conductor)respuestaConductor.contenido.getCarga().get(0);
//	                		documentos.append("El Documento :"+vigencia.getDocumento().getNombreDocumento()+" del Conductor "+conductor.getNombreCompleto()+" se encuentra caducado, fecha de expiración "+vigencia.getFechaExpiracion());
//
//	                	}
//	                	
//	                	
//	                }
//	        	}
//	        }	
	        respuesta.valor=validacion;			
			respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
		} catch(Exception ex){
			ex.printStackTrace();
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

			
			if (httpRequest.getParameter("filtroIdDocumento") != null) {
				parametros.setFiltroIdDocumento(Integer.parseInt(httpRequest.getParameter("filtroIdDocumento")));
			}

			if (httpRequest.getParameter("filtroPerteneceA") != null) {
				parametros.setFiltroPerteneceA(Integer.parseInt(httpRequest.getParameter("filtroPerteneceA")));
			}
			
			if (httpRequest.getParameter("filtroIdEntidad") != null) {
				parametros.setFiltroIdEntidad(Integer.parseInt(httpRequest.getParameter("filtroIdEntidad")));
			}
			//Recuperar registros
			respuesta = dVigencia.recuperarRegistros(parametros);
			respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
		} catch(Exception ex){
			ex.printStackTrace();
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	}

	@RequestMapping(value = URL_RECUPERAR_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta recuperaRegistro(int ID, Locale locale){
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
        	respuesta= dVigencia.recuperarRegistro(ID);
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
  public @ResponseBody RespuestaCompuesta guardarRegistro(@RequestBody Vigencia eVigencia, HttpServletRequest peticionHttp,Locale locale){		 
 	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	Bitacora eBitacora= null;
	String ContenidoAuditoria ="";
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	String direccionIp="";
	String ClaveGenerada="";
	ParametrosListar parametros = null;
	try {
		//valida los datos que vienen del formulario
	    Respuesta validacion = Utilidades.validacionXSS(eVigencia, gestorDiccionario, locale);
	    if (validacion.estado == false) {
	      throw new Exception(validacion.valor);
	    }
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dVigencia.getDataSource());
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
		
		eVigencia.setActualizadoEl(Calendar.getInstance().getTime().getTime());
		eVigencia.setIpActualizacion(direccionIp);
		eVigencia.setActualizadoPor(principal.getID());

        //TODO
		//definir validaciones con Ivan
        /*//Valido primero si no hay otro registro igual en BD
        respuesta = dVigencia.validaRegistro(eVigencia.getPlaca());
        // Verifica si la accion se ejecuto de forma satisfactoria
        if (respuesta.estado == false) {
         throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido", null, locale));
        }
        if (respuesta.contenido.carga.size() > 0){
     	throw new Exception(gestorDiccionario.getMessage("sgo.registroDuplicado", null, locale));
        }*/
		if(Utilidades.esValido(eVigencia.getId()) && eVigencia.getId() > 0){
			respuesta= dVigencia.actualizarRegistro(eVigencia);
	        //Verifica si la accion se ejecuto de forma satisfactoria
	        if (respuesta.estado==false){     	
	          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
	        }
	        //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(VigenciaDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf( eVigencia.getId()));
            eBitacora.setContenido(mapper.writeValueAsString(eVigencia));
            eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
            eBitacora.setRealizadoPor(principal.getID());
		} else {
			//valido que no exista un tegistro con los mismos valores
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
			}

			eVigencia.setCreadoEl(Calendar.getInstance().getTime().getTime());
			eVigencia.setCreadoPor(principal.getID());
			eVigencia.setIpCreacion(direccionIp);
			
	        respuesta= dVigencia.guardarRegistro(eVigencia);
	        //Verifica si la accion se ejecuto de forma satisfactoria
	        if (respuesta.estado==false){     	
	          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarFallido",null,locale));
	        }
	        ClaveGenerada = respuesta.valor;
	        //Guardar en la bitacora
	        ObjectMapper mapper = new ObjectMapper(); // no need to do this if you inject via @Autowired
	        ContenidoAuditoria =  mapper.writeValueAsString(eVigencia);
	        eBitacora.setUsuario(principal.getNombre());
	        eBitacora.setAccion(URL_GUARDAR_COMPLETA);
	        eBitacora.setTabla(VigenciaDao.NOMBRE_TABLA);
	        eBitacora.setIdentificador(ClaveGenerada);
	        eBitacora.setContenido(ContenidoAuditoria);
	        eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
	        eBitacora.setRealizadoPor(principal.getID());
		}

        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }           
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.guardarExitoso",new Object[] {  eBitacora.getFechaRealizacion().substring(0, 9),eBitacora.getFechaRealizacion().substring(10),principal.getIdentidad() },locale);
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
public @ResponseBody RespuestaCompuesta actualizarRegistro(@RequestBody Vigencia eVigencia,HttpServletRequest peticionHttp,Locale locale){
	RespuestaCompuesta respuesta = null;
	AuthenticatedUserDetails principal = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	Bitacora eBitacora=null;
	String direccionIp="";
	try {
		//valida los datos que vienen del formulario
	    Respuesta validacion = Utilidades.validacionXSS(eVigencia, gestorDiccionario, locale);
	    if (validacion.estado == false) {
	      throw new Exception(validacion.valor);
	    }
		//Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dVigencia.getDataSource());
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
        
		eVigencia.setActualizadoEl(Calendar.getInstance().getTime().getTime());
		eVigencia.setIpActualizacion(direccionIp);
		eVigencia.setActualizadoPor(principal.getID());
		
        //si las validaciones son correctas permito hacer la actualización del registro.
        respuesta= dVigencia.actualizarRegistro(eVigencia);
        if (respuesta.estado==false){          	
        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(principal.getNombre());
        eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
        eBitacora.setTabla(VigenciaDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf( eVigencia.getId()));
        eBitacora.setContenido(mapper.writeValueAsString(eVigencia));
        eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
        eBitacora.setRealizadoPor(principal.getID());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){     	
          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
        }  
    	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {  eBitacora.getFechaRealizacion().substring(0, 9),eBitacora.getFechaRealizacion().substring(10),principal.getIdentidad() },locale);;
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
	public @ResponseBody RespuestaCompuesta eliminarRegistro(@RequestBody Vigencia eVigencia, HttpServletRequest peticionHttp, Locale locale){
		RespuestaCompuesta respuesta = null;
		AuthenticatedUserDetails principal = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		Bitacora eBitacora=null;
		//String direccionIp="";
		try {
			//Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dVigencia.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
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

            respuesta= dVigencia.eliminarRegistro(eVigencia.getId());
            if (respuesta.estado==false){          	
            	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
            }
            //Guardar en la bitacora
            ObjectMapper mapper = new ObjectMapper();
            eBitacora.setUsuario(principal.getNombre());
            eBitacora.setAccion(URL_ACTUALIZAR_COMPLETA);
            eBitacora.setTabla(VigenciaDao.NOMBRE_TABLA);
            eBitacora.setIdentificador(String.valueOf(eVigencia.getId()));
           // eBitacora.setContenido(mapper.writeValueAsString(eVigencia));
            eBitacora.setRealizadoEl(Calendar.getInstance().getTime().getTime());
            eBitacora.setRealizadoPor(principal.getID());
            respuesta= dBitacora.guardarRegistro(eBitacora);
            if (respuesta.estado==false){     	
              	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
            }  
        	respuesta.mensaje=gestorDiccionario.getMessage("sgo.actualizarExitoso",new Object[] {eBitacora.getFechaRealizacion().substring(0, 9),eBitacora.getFechaRealizacion().substring(10),principal.getIdentidad() },locale);;
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