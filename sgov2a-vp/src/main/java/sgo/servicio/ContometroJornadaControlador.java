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
import sgo.datos.ContometroJornadaDao;
import sgo.datos.EnlaceDao;
import sgo.entidad.Bitacora;
import sgo.entidad.ContometroJornada;
import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

@Controller
public class ContometroJornadaControlador {
	@Autowired
	private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
	@Autowired
	private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
	@Autowired
	private EnlaceDao dEnlace;
	@Autowired
	private MenuGestor menu;
	@Autowired
	private ContometroJornadaDao dContometroJornada;
	/** Nombre de la clase. */
    private static final String sNombreClase = "ContometroJornadaControlador";
	//
	//private DataSourceTransactionManager transaccion;//Gestor de la transaccion
	//urls generales
	private static final String URL_LISTAR_COMPLETA="/admin/contometroJornada/listar";
	private static final String URL_LISTAR_RELATIVA="/contometroJornada/listar";
	private static final String URL_RECUPERAR_COMPLETA="/admin/contometroJornada/recuperar";
	private static final String URL_RECUPERAR_RELATIVA="/contometroJornada/recuperar";
	
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
		
		if (httpRequest.getParameter("idJornada") != null) {
			parametros.setIdJornada(Integer.parseInt(httpRequest.getParameter("idJornada")));
		}
		
		if (httpRequest.getParameter("filtroProducto") != null) {
			parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
		}
		
		if (httpRequest.getParameter("filtroEstacion") != null) {
			parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
		}
		
		if (httpRequest.getParameter("txtFiltro") != null) {
			parametros.setTxtFiltro((httpRequest.getParameter("txtFiltro")));
		}
		
		parametros.setFiltroEstado(Constante.FILTRO_TODOS);
		if (httpRequest.getParameter("filtroEstado") != null) {
			parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
		}
		
		//Recuperar registros
		respuesta = dContometroJornada.recuperarRegistros(parametros);
		respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso",null,locale);
	} catch(Exception ex){
		ex.printStackTrace();
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
    	respuesta= dContometroJornada.recuperarRegistro(ID);
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
	

	private  AuthenticatedUserDetails getCurrentUser(){
		return  (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}