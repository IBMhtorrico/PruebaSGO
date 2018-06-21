package sgo.servicio;
import java.io.BufferedReader;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sgo.datos.DiaOperativoDao;
import sgo.datos.ParametroDao;
import sgo.datos.UsuarioDao;
import sgo.entidad.LoginEntity;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.seguridad.CustomAuthenticationProvider;


@Controller
public class Login {
 @Autowired
 private MessageSource gestorDiccionario;
@Autowired
private UsuarioDao dUsuario;
@Autowired
private ParametroDao dParametro;
@Autowired
private DiaOperativoDao dDiaOperativo;

	@RequestMapping("/login")
	public ModelAndView mostrarLogin(@ModelAttribute("loginEntity") LoginEntity loginEntity, HttpServletRequest peticionHTTP, Locale locale){
		ModelAndView vista = null;
		boolean error=false;
		String mensajeError="";
		try{
		 if (peticionHTTP.getParameter("error")!= null){
		  error = Boolean.parseBoolean(peticionHTTP.getParameter("error")) ;
		 }
		 if (error==true){
			Exception exception = (Exception) peticionHTTP.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			mensajeError = exception.getMessage();
//		  mensajeError = gestorDiccionario.getMessage("sgo.errorInicioSesion", null, locale);
		 }
		vista= new ModelAndView("login");
		vista.addObject("mensajeError",mensajeError);
		}catch(Exception ex){
			
		}
		return vista;
	}
	
	@RequestMapping("/processlogin")
	public ModelAndView processlogin(@ModelAttribute("loginEntity") LoginEntity loginEntity, HttpSession session, HttpServletRequest peticionHttp, Locale locale, Class<?> authentication) {
		ModelAndView vista = null;
		boolean error=false;
		String mensajeError="";
		StringBuffer jb = new StringBuffer();
		String line = null;
		String valores = null;
		try{
			vista= new ModelAndView("/panel");
			vista.addObject("mensajeError", "");
			if (peticionHttp.getParameter("error")!= null){
		      error = Boolean.parseBoolean(peticionHttp.getParameter("error")) ;
		    } else {
				BufferedReader reader = peticionHttp.getReader();
			    while ((line = reader.readLine()) != null) {
			      jb.append(line);
			      valores = line;
			    }
			    String[] parts = valores.split("captcha=");
			    String valorCaptcha = parts[1];
			    
	
				String captcha=(String)session.getAttribute("CAPTCHA");
		        if(captcha==null || (captcha!=null && !captcha.equals(valorCaptcha))){
		        	vista= new ModelAndView("login");
					vista.addObject("mensajeError", "El código Captcha es incorrecto.");
					return vista;
		        }
		    }
			
			peticionHttp.getSession().getAttribute("FORM_LOGIN_FILTER");
			
			
			
			if (error==true){
				Exception exception = (Exception) peticionHttp.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
				vista= new ModelAndView("login");
				vista.addObject("mensajeError", exception.getMessage());
				return vista;
			}
			
			}catch(Exception ex){
				
			}
		return vista;
	}
	

	@RequestMapping("/invalida")
	public ModelAndView mostrarSesionInvalida(HttpServletRequest peticionHTTP,Locale locale){
		ModelAndView vista = null;
    boolean error=false;
    String mensajeError="";
		try{
	    if (peticionHTTP.getParameter("error")!= null){
	      error = Boolean.parseBoolean(peticionHTTP.getParameter("error")) ;
	     }
	     if (error==true){
	      mensajeError=gestorDiccionario.getMessage("sgo.errorInicioSesion", null, locale);
	     }
			vista= new ModelAndView("login");
			vista.addObject("mensajeError",mensajeError);
		}catch(Exception ex){
			
		}
		return vista;
	}
	
	 public boolean supports(Class<?> authentication) {
		 
	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	    }
	 
	 private AuthenticatedUserDetails getCurrentUser() {
		  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 }
	 
}
