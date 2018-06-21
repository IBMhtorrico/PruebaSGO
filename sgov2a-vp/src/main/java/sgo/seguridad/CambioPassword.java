package sgo.seguridad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sgo.datos.BitacoraDao;
import sgo.datos.EnlaceDao;
import sgo.datos.UsuarioDao;
//import sgo.entidad.Contenido;
//import sgo.entidad.Enlace;
import sgo.entidad.MenuGestor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.Respuesta;
import sgo.entidad.RespuestaCompuesta;
//import sgo.entidad.Usuario;
import sgo.utilidades.Constante;

//import com.petroperu.accesodatos.FedoraDirectoryConnection;
//import com.petroperu.accesodatos.FedoraDirectoryConnectionFactory;

/*
import java.util.Hashtable;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import sgo.datos.EventoDao;
*/

@Controller
public class CambioPassword {
	@Autowired
	private BitacoraDao dBitacora; // Clase para registrar en la bitacora
	                                // (auditoria por accion)
	@Autowired
	private EnlaceDao dEnlace;
	@Autowired
	private MenuGestor menu;
	@Autowired
	private UsuarioDao dUsuario;
	@Autowired 
	private MessageSource gestorDiccionario;
	@Autowired
    private UserDetailsServiceImpl userService;
	//
    private DataSourceTransactionManager transaccion;// Gestor de la transaccion
	private static final String URL_GESTION_COMPLETA="/admin/cambioPassword";
	private static final String URL_GESTION_RELATIVA="/cambioPassword";
	private static final String URL_VALIDA_PASSWORD_RELATIVA="/cambioPassword/validaPassword";
	private static final String URL_VALIDA_PASSWORD_COMPLETA="/admin/cambioPassword/validaPassword";
	private static final String URL_VALIDA_CAMBIO_PASSWORD_RELATIVA="/cambioPassword/validaCambioPassword";
	private static final String URL_VALIDA_CAMBIO_PASSWORD_COMPLETA="/admin/cambioPassword/validaCambioPassword";
	private static final String URL_LISTAR_RELATIVA="/cambioPassword/actualizar";
	private static final String URL_LISTAR_COMPLETA="/admin/cambioPassword/actualizar";

	private HashMap<String, String> recuperarMapaValores(Locale locale) {
	  HashMap<String, String> mapaValores = new HashMap<String, String>();
	  try {
	   mapaValores.put("TITULO_MODIFICA_REGISTRO", gestorDiccionario.getMessage("sgo.tituloFormularioEditar", null, locale));
	   //
	   mapaValores.put("ETIQUETA_BOTON_CERRAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCerrar", null, locale));
	   mapaValores.put("ETIQUETA_BOTON_GUARDAR", gestorDiccionario.getMessage("sgo.etiquetaBotonGuardar", null, locale));

	   mapaValores.put("ETIQUETA_BOTON_CANCELAR", gestorDiccionario.getMessage("sgo.etiquetaBotonCancelar", null, locale));
	   mapaValores.put("ETIQUETA_BOTON_CONFIRMAR", gestorDiccionario.getMessage("sgo.etiquetaBotonConfirmar", null, locale));

	   mapaValores.put("MENSAJE_CARGANDO", gestorDiccionario.getMessage("sgo.mensajeCargando", null, locale));
	   
	   mapaValores.put("TITULO_VENTANA_MODAL", gestorDiccionario.getMessage("sgo.tituloVentanaModal", null, locale));
	  } catch (Exception ex) {

	  }
	  return mapaValores;
	 }
	
    // @SuppressWarnings("unchecked")
	@RequestMapping(URL_GESTION_RELATIVA)
	 public ModelAndView mostrarFormulario( Locale locale){
		ModelAndView vista = null;
		  AuthenticatedUserDetails principal = null;
		  ArrayList<?> listaUsuario = null;
		  ArrayList<?> listaEnlaces = null;
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
		   
		   mapaValores = recuperarMapaValores(locale);
		   vista = new ModelAndView("plantilla");
		   vista.addObject("vistaJSP", "seguridad/cambioPassword.jsp");
		   vista.addObject("vistaJS", "seguridad/cambioPassword.js");

		   respuesta = dUsuario.recuperarRegistro(principal.getID());
		   if (respuesta.estado == false) {
		    throw new Exception(gestorDiccionario.getMessage("sgo.noPermisosDisponibles", null, locale));
		   }
		   listaUsuario = (ArrayList<?>) respuesta.contenido.carga;
		   
		   vista.addObject("identidadUsuario", principal.getIdentidad());
		   
		   vista.addObject("usuario", listaUsuario);
		   vista.addObject("menu", listaEnlaces);
		   vista.addObject("mapaValores", mapaValores);
		} catch(Exception ex){
			
		}
		return vista;
	 }

	public Boolean comparaPasswords(String passActual, String passNueva){
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		return passwordEncoder.matches(passActual, passNueva);

	}
	
	@RequestMapping(value = URL_VALIDA_CAMBIO_PASSWORD_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta validaCambioPassword(HttpServletRequest httpRequest, Locale locale){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		ParametrosListar parametros = null;
		AuthenticatedUserDetails principal = null;
		int ide = 0;
		String newpwd = "";
		String newcon = "";
		String pwd = "";
		String user = "";
		int len = 0;
		String cadMayusculas = "ABCDEFGHIKLMNÑOPQRSTUVWXYZ";
		String cadMinusculas="abcdefghijklmnñopqrstuvwxyz";
		String cadnumeros = "0123456789";
		try {
//			//Recuperar el usuario actual
//			principal = this.getCurrentUser(); 
//			//Recuperar el enlace de la accion
//			respuesta = dEnlace.recuperarRegistro(URL_VALIDA_CAMBIO_PASSWORD_COMPLETA);
//			if (respuesta.estado==false){
//				mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale);
//				throw new Exception(mensajeRespuesta);
//			}
//			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
//			//Verificar si cuenta con el permiso necesario			
//			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
//				mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale);
//				throw new Exception(mensajeRespuesta);
//    		}
			
			if (httpRequest.getParameter("clave") != null) {
				pwd = ( httpRequest.getParameter("clave"));
			}
			
			if (httpRequest.getParameter("nuevaClave") != null) {
				newpwd = ( httpRequest.getParameter("nuevaClave"));
				len = newpwd.length();
			}
			
			if (httpRequest.getParameter("confirmaClave") != null) {
				newcon = ( httpRequest.getParameter("confirmaClave"));
			}
			
			if (httpRequest.getParameter("nombre") != null) {
				user = ( httpRequest.getParameter("nombre"));
			}

		AuthenticatedUserDetails usuario =  (AuthenticatedUserDetails) userService.loadUserByUsername(user);
		
		if(usuario.getTipo() == 1 ){
			throw new Exception(gestorDiccionario.getMessage("sgo.password.PasswordUserInterno",null,locale));
		}
		
		if (!newcon.equals(newpwd)) {
			throw new Exception(gestorDiccionario.getMessage("sgo.password.PasswordsNoIguales",null,locale));
		}

		// Validamos el tamaÃ±o del password
	    if (len < 8) {
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.DebeContenerMinimo8Caracteres",null,locale));
	    }

	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    //validamos que la contraseña no sea igual a la que se encuentra en BD
	    if (!passwordEncoder.matches(pwd, usuario.getPassword())) {
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoCoincidePWDconPWDOLD",null,locale));
	    } 
	    
	    //validamos que la nueva contraseña no sea igual a la que se encuentra en BD
	    if (passwordEncoder.matches(newpwd, usuario.getPassword())) {
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoIgualAnterior",null,locale));
	    }
	    
	    //Validamos que la contraseña sea alfanumércia
	    boolean numerico = false;
	    for(int i = 0; i < newpwd.length(); i++) {
	        if (cadnumeros.indexOf(newpwd.charAt(i),0)!=-1){
	        	numerico = true;
	        }
	    }
	    if(!numerico) {
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.Numerico",null,locale));
	    }
	    
	    boolean letra = false;
	    String newpwdMinusculas = newpwd.toLowerCase();
		for(int i=0; i<newpwdMinusculas.length(); i++){
		   if (cadMinusculas.indexOf(newpwdMinusculas.charAt(i),0)!=-1){
			   letra = true;
		   }
		}
		if(!letra) {
		    	throw new Exception(gestorDiccionario.getMessage("sgo.password.Caracter",null,locale));
		}
		
	    //Validamos que tenga al menos una mayuscula	    
	    boolean mayuscula = false;
	    for(int i=0; i<newpwd.length(); i++){
	        if (cadMayusculas.indexOf(newpwd.charAt(i),0)!=-1){
	        	mayuscula = true;
	        }
	     }
	    if(!mayuscula) {
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.ContenerMayuscula",null,locale));
	    }

	    //Validamos que la contraseña no comience ni termine en un valor numérico
	    if ((cadnumeros.indexOf(newpwd.charAt(0),0)!=-1) || (cadnumeros.indexOf(newpwd.charAt(len-1),0)!=-1)){
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoNumerosIniFin",null,locale));
	    }

	    if(usuario.getNombre().toLowerCase().contains(newpwd.toLowerCase())){
	    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoIdentificador",null,locale));
	    }

	    // Validamos que no haya 3 caracteres repetidos seguidos
	    for(int i=0; i< len - 2; i++){
	    	String subs1 = newpwd.substring(i,i+1);
	    	String subs2 = newpwd.substring(i+1,i+2);
	    	String subs3 = newpwd.substring(i+2,i+3);
	    	//if(newpwd[i]==newpwd[i+1] & newpwd[i]==newpwd[i+2]){
			if((subs1.equals(subs2)) && (subs2.equals(subs3))) {
				throw new Exception(gestorDiccionario.getMessage("sgo.password.CaracetresIguales",null,locale));
			}

	    }

	    respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso", null,locale);
	    respuesta.estado=true;
		respuesta.contenido = null;
	
		} catch(Exception ex){
			ex.printStackTrace();
			respuesta.estado=false;
			respuesta.contenido = null;
			respuesta.mensaje=ex.getMessage();
		}
		return respuesta;
	}	
	

	@RequestMapping(value = URL_VALIDA_PASSWORD_RELATIVA ,method = RequestMethod.GET)
	public @ResponseBody RespuestaCompuesta validaPassword(HttpServletRequest httpRequest, Locale locale){
		RespuestaCompuesta respuesta = new RespuestaCompuesta();
		ParametrosListar parametros = null;
		AuthenticatedUserDetails principal = null;
		int tipo = 0; 
		String newpwd = null;
		String confirmapwd = null;
		String pwd = "";
		String user = "";
		int len = 0;
		String cadMayusculas = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
		String cadMinusculas="abcdefghijklmnñopqrstuvwxyz";
		String cadnumeros = "0123456789";
		try {
//			//Recuperar el usuario actual
//			principal = this.getCurrentUser(); 
//			//Recuperar el enlace de la accion
//			respuesta = dEnlace.recuperarRegistro(URL_VALIDA_PASSWORD_COMPLETA);
//			if (respuesta.estado==false){
//				mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada",null,locale);
//				throw new Exception(mensajeRespuesta);
//			}
//			Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
//			//Verificar si cuenta con el permiso necesario			
//			if (!principal.getRol().searchPermiso(eEnlace.getPermiso())){
//				mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso",null,locale);
//				throw new Exception(mensajeRespuesta);
//    		}
			
			if (httpRequest.getParameter("clave") != null) {
				newpwd = ( httpRequest.getParameter("clave"));
				len = newpwd.length();
			}

			if (httpRequest.getParameter("confirmaClave") != null) {
				confirmapwd = ( httpRequest.getParameter("confirmaClave"));
			}
			
			if (httpRequest.getParameter("nombre") != null) {
				user = ( httpRequest.getParameter("nombre"));
			};
			
			if (httpRequest.getParameter("tipo") != null) {
				tipo = (Integer.parseInt(httpRequest.getParameter("tipo")));
			};
			
			if(user == null){
				throw new Exception(gestorDiccionario.getMessage("sgo.password.NoUser",null,locale));
			}
			
			parametros= new ParametrosListar();
			parametros.setPaginacion(Constante.SIN_PAGINACION);			
			parametros.setFiltroEstado(Constante.ESTADO_ACTIVO);
			parametros.setFiltroUsuario(user);
			respuesta = dUsuario.recuperarRegistros(parametros);
			if(respuesta.contenido.carga.size()  > 0) {
				throw new Exception(gestorDiccionario.getMessage("sgo.password.ExisteUsuario", null, locale));
			} 
		
			if(tipo == 2 ){
				if(newpwd == null){
					throw new Exception(gestorDiccionario.getMessage("sgo.password.NoDatos",null,locale));
				}
				
				if(confirmapwd == null){
					throw new Exception(gestorDiccionario.getMessage("sgo.password.NoConfirmaDatos",null,locale));
				}
		
				if (!newpwd.equals(confirmapwd)) {
					throw new Exception(gestorDiccionario.getMessage("sgo.password.PasswordsNoIguales",null,locale));
				};
	
				// Validamos el tamaÃ±o del password
			    if (len < 8) {
			    	throw new Exception(gestorDiccionario.getMessage("sgo.password.DebeContenerMinimo8Caracteres",null,locale));
			    };
	
			    //Validamos que la contraseña sea alfanumércia
			    boolean numerico = false;
			    for(int i = 0; i < newpwd.length(); i++) {
			        if (cadnumeros.indexOf(newpwd.charAt(i),0)!=-1){
			        	numerico = true;
			        }
			    };
			    if(!numerico) {
			    	throw new Exception(gestorDiccionario.getMessage("sgo.password.Numerico",null,locale));
			    };
			    
			    boolean letra = false;
			    String newpwdMinusculas = newpwd;
				for(int i=0; i<newpwdMinusculas.length(); i++){
				   if (cadMinusculas.indexOf(newpwdMinusculas.charAt(i),0)!=-1){
					   letra = true;
				   }
				}
				if(!letra) {
				    	throw new Exception(gestorDiccionario.getMessage("sgo.password.Caracter",null,locale));
				}
				
			    //Validamos que tenga al menos una mayuscula	    
			    boolean mayuscula = false;
			    for(int i=0; i<newpwd.length(); i++){
			        if (cadMayusculas.indexOf(newpwd.charAt(i),0)!=-1){
			        	mayuscula = true;
			        }
			     }
			    if(!mayuscula) {
			    	throw new Exception(gestorDiccionario.getMessage("sgo.password.ContenerMayuscula",null,locale));
			    }
	
			    //Validamos que la contraseña no comience ni termine en un valor numérico
			    if ((cadnumeros.indexOf(newpwd.charAt(0),0)!=-1) || (cadnumeros.indexOf(newpwd.charAt(len-1),0)!=-1)){
			    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoNumerosIniFin",null,locale));
			    }
			    
			    //Validamos que la contraseña no contenga el nombre 
			    if(user.toLowerCase().contains(newpwd.toLowerCase())){
			    	throw new Exception(gestorDiccionario.getMessage("sgo.password.NoIdentificador",null,locale));
			    }
	
			    // Validamos que no haya 3 caracteres repetidos seguidos
			    for(int i=0; i< len - 2; i++){
			    	String subs1 = newpwd.substring(i,i+1);
			    	String subs2 = newpwd.substring(i+1,i+2);
			    	String subs3 = newpwd.substring(i+2,i+3);
					if((subs1.equals(subs2)) && (subs2.equals(subs3))) {
						throw new Exception(gestorDiccionario.getMessage("sgo.password.CaracetresIguales",null,locale));
					}
	
			    }
		    respuesta.mensaje= gestorDiccionario.getMessage("sgo.listarExitoso", null,locale);
		    respuesta.estado=true;
			respuesta.contenido = null;
			}
		} catch(Exception ex){
			ex.printStackTrace();
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
