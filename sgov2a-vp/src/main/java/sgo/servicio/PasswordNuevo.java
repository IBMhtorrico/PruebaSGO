package sgo.servicio;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sgo.datos.BitacoraClaveDao;
import sgo.datos.BitacoraDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.ParametroDao;
import sgo.datos.UsuarioDao;
import sgo.entidad.Bitacora;
import sgo.entidad.BitacoraClave;
import sgo.entidad.Respuesta;
import sgo.entidad.Usuario;
import sgo.seguridad.UserDetailsServiceImpl;
import sgo.utilidades.MailNotifica;
import sgo.utilidades.Utilidades;

@Controller
public class PasswordNuevo {
 @Autowired
 private MessageSource gestorDiccionario;
@Autowired
private UsuarioDao dUsuario;
@Autowired
private BitacoraDao dBitacora; //Clase para registrar en la bitacora (auditoria por accion)
@Autowired
private ParametroDao dParametro;
@Autowired
private DiaOperativoDao dDiaOperativo;
@Autowired
private BitacoraClaveDao dBitacoraClave;
@Autowired
private MailNotifica dMailNotifica;

@Autowired
private UserDetailsServiceImpl userService;
//
private DataSourceTransactionManager transaccion;//Gestor de la transaccion

/** Nombre de la clase. */
private static final String sNombreClase = "PasswordNuevo";


@RequestMapping("/passwordNuevo")
public void mensajeError(HttpServletRequest peticionHTTP, HttpServletResponse response, Locale locale){
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	String direccionIp="";
	boolean error = false;
	Respuesta respuesta = new Respuesta();
	String username = "";
	String claveGenerada="";
	Bitacora eBitacora=null;
	Usuario eUsuario = null;
	String mensajeError="";
	
	try{
		if (peticionHTTP.getParameter("username")!= null){
			username = peticionHTTP.getParameter("username") ;
	    }
	
		System.out.println("username " + username);
		
		if(username.length() == 0){
			mensajeError="Debe llenar el campo Usuario.";
	    	error = true;
		} 
		else {
			eUsuario = dUsuario.getRecord(username);
			if(eUsuario == null){
				mensajeError="El usuario no se encuentra dado de alta.";
		    	error = true;
			} else if(eUsuario.getTipo() == 2) {
				respuesta.estado = false;
				while (!respuesta.estado){
					claveGenerada = Utilidades.getGeneraPassword();
					respuesta = Utilidades.validaPassword(claveGenerada);
					respuesta.mensaje = gestorDiccionario.getMessage(respuesta.valor, null, locale); //FIXME vp.20170515
				}
				try{
					//Inicia la transaccion
					this.transaccion = new DataSourceTransactionManager(dUsuario.getDataSource());
					definicionTransaccion = new DefaultTransactionDefinition();
					estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
					eBitacora = new Bitacora();
					//Auditoria local (En el mismo registro)
					direccionIp = peticionHTTP.getHeader("X-FORWARDED-FOR");  
					if (direccionIp == null) {  
						direccionIp = peticionHTTP.getRemoteAddr();  
					}
		
					PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
					//TODO
					//eUsuario.setClave(passwordEncoder.encode("12345678"));
					System.out.println("claveGenerada "+ claveGenerada);
					eUsuario.setClave(passwordEncoder.encode(claveGenerada));
					eUsuario.setActualizadoEl(Calendar.getInstance().getTime().getTime());
					eUsuario.setActualizadoPor(eUsuario.getId()); 
					eUsuario.setIpActualizacion(direccionIp);
					eUsuario.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
					eUsuario.setIntentos(0);
					eUsuario.setClaveTemporal(1);
		System.out.println("antes de resetPassword ");
			        respuesta= dUsuario.resetPassword(eUsuario);
		System.out.println("despues de resetPassword ");     
			        if (respuesta.estado==false){          	
			        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
			        }
			        //Guardar en la bitacora
			        ObjectMapper mapper = new ObjectMapper();
			        eBitacora.setUsuario(eUsuario.getNombre());
			        eBitacora.setAccion("/admin/bitacoraClave/resetPassword");
			        eBitacora.setTabla("seguridad.usuario");
			        eBitacora.setIdentificador(String.valueOf(eUsuario.getId()));
			        eBitacora.setContenido(mapper.writeValueAsString(eUsuario));
			        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
			        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
	    System.out.println("antes de guardar bitacora ");
			        respuesta= dBitacora.guardarRegistro(eBitacora);
			        if (respuesta.estado==false){     	
			          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
			        }  
			        
			        BitacoraClave eBitacoraClave = new BitacoraClave();
			        eBitacoraClave.setIdUsuario(eUsuario.getId());
			        eBitacoraClave.setClave(passwordEncoder.encode(eUsuario.getClave()));
			        eBitacoraClave.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
System.out.println("antes de guardar en bitacora clave ");     
			        respuesta= dBitacoraClave.guardarRegistro(eBitacoraClave);
			        if (respuesta.estado==false){          	
			        	throw new Exception(gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale));
			        }
			        //Guardar en la bitacora
			        mapper = new ObjectMapper();
			        eBitacora.setUsuario(eUsuario.getNombre());
			        eBitacora.setAccion("/admin/bitacoraClave/crear");
			        eBitacora.setTabla(BitacoraClaveDao.NOMBRE_TABLA);
			        eBitacora.setIdentificador(respuesta.valor);
			        eBitacora.setContenido(mapper.writeValueAsString(eBitacoraClave));
			        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
			        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
			        respuesta= dBitacora.guardarRegistro(eBitacora);
			        if (respuesta.estado==false){     	
			          	throw new Exception(gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale));
			        }

			        //enviamos correo al usuario con la nueva contraseña temporal.
					eUsuario.setClave(claveGenerada);
					respuesta.estado = dMailNotifica.enviarMailReseteoPassword(eUsuario);
					if(respuesta.estado){
						mensajeError = "Se ha enviado correo a " + eUsuario.getEmail() + " con un password temporal";
					}
					else{
						mensajeError = "Contacte con el administrador.";
					}
			    	this.transaccion.commit(estadoTransaccion);
				} catch (Exception ex){
					//ex.printStackTrace();
					Utilidades.gestionaError(ex, sNombreClase, "mostrarSesionOlvidoPassword");
					this.transaccion.rollback(estadoTransaccion);
					mensajeError = ex.getMessage();
					
					String data = "{}";
					Gson gson = new GsonBuilder().create();
					
					data = gson.toJson(mensajeError);
					PrintWriter out = response.getWriter();
					out.print(data);
					System.out.println("mensajeError " + ex.getMessage());
					
					respuesta.estado=false;
					respuesta.mensaje=ex.getMessage();
				}
			} else if (eUsuario.getTipo() == 1){
				mensajeError="Debe usar el aplicativo Cambio de Contraseña.";
		    	error = true;
			}
		}
		String data = "{}";
		Gson gson = new GsonBuilder().create();
		
		data = gson.toJson(mensajeError);
		PrintWriter out = response.getWriter();
		out.print(data);
		System.out.println("mensajeError " + mensajeError);
	}catch(Exception ex){
		
	}
}

@RequestMapping(value = "/reseteoPassword", method = RequestMethod.POST)
public void confirmaPasswordNuevo(HttpServletRequest peticionHTTP, HttpServletResponse response, Locale locale){
	ModelAndView vista = null;
	TransactionDefinition definicionTransaccion = null;
	TransactionStatus estadoTransaccion = null;
	boolean error = false;
	String direccionIp="";
	Respuesta respuesta = new Respuesta();
	String username = "";
	String password = "";
	String nuevoPassword = "";
	String confirmaPaswword = "";
	Bitacora eBitacora=null;
	Usuario eUsuario = null;
	String mensajeError="";
	
	try{
		if (peticionHTTP.getParameter("username")!= null){
			username = peticionHTTP.getParameter("username") ;
	    }
		if (peticionHTTP.getParameter("password")!= null){
			password = peticionHTTP.getParameter("password") ;
		}
		if (peticionHTTP.getParameter("nuevoPassword")!= null){
			nuevoPassword = peticionHTTP.getParameter("nuevoPassword") ;
		}
		if (peticionHTTP.getParameter("confirmaPaswword")!= null){
			confirmaPaswword = peticionHTTP.getParameter("confirmaPaswword") ;
	    }

		System.out.println("username "+ username);
		System.out.println("password "+ password);
		System.out.println("nuevoPassword "+ nuevoPassword);
		System.out.println("confirmaPaswword "+ confirmaPaswword);

		if(username.length() == 0){
			error = true;
			mensajeError = "Debe llenar el campo Usuario";
		}
		
		if(nuevoPassword.length() == 0){
			error = true;
			mensajeError = "Debe llenar el campo Nueva Contraseña";
		}
		
		if(confirmaPaswword.length() == 0){
			error = true;
			mensajeError = "Debe llenar el campo Confirme Contraseña.";
		}
		
		if (!nuevoPassword.equals(confirmaPaswword)) {
			error = true;
			mensajeError = gestorDiccionario.getMessage("sgo.password.PasswordsNoIguales",null,locale);
		}
		
		eUsuario = dUsuario.getRecord(username);
	    
		if(eUsuario.getTipo() == 1 ){
			error = true;
			mensajeError = gestorDiccionario.getMessage("sgo.password.PasswordUserInterno",null,locale);
		}

		// Validamos el tamaÃ±o del password
	    if (nuevoPassword.length() < 8) {
	    	error = true;
	    	mensajeError = gestorDiccionario.getMessage("sgo.password.DebeContenerMinimo8Caracteres",null,locale);
	    }

	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	    //validamos que la nueva contraseña no sea igual a la que se encuentra en BD
	    if (passwordEncoder.matches(nuevoPassword, eUsuario.getClave())) {
	    	error = true;
	    	mensajeError = gestorDiccionario.getMessage("sgo.password.NoIgualAnterior",null,locale);
	    }

	    Respuesta validaPassword = Utilidades.validaPassword(nuevoPassword);
	    validaPassword.mensaje = gestorDiccionario.getMessage(validaPassword.valor, null, locale); //FIXME vp.20170515
	    
	    if(!validaPassword.estado){
	    	error = true;
	    	mensajeError = validaPassword.mensaje;
	    }

	    //Inicia la transaccion
		this.transaccion = new DataSourceTransactionManager(dUsuario.getDataSource());
		definicionTransaccion = new DefaultTransactionDefinition();
		estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
		eBitacora = new Bitacora();
		//Auditoria local (En el mismo registro)
		direccionIp = peticionHTTP.getHeader("X-FORWARDED-FOR");  
		if (direccionIp == null) {  
			direccionIp = peticionHTTP.getRemoteAddr();  
		}

		eUsuario.setClave(passwordEncoder.encode(nuevoPassword));
		eUsuario.setActualizadoEl(Calendar.getInstance().getTime().getTime());
		eUsuario.setActualizadoPor(eUsuario.getId()); 
		eUsuario.setIpActualizacion(direccionIp);
		eUsuario.setClaveTemporal(0);
		eUsuario.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
		eUsuario.setIntentos(0);

        respuesta= dUsuario.ActualizarPassword(eUsuario);
        if (respuesta.estado==false){         
        	error = true;
        	mensajeError = gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale);
        }
        //Guardar en la bitacora
        ObjectMapper mapper = new ObjectMapper();
        eBitacora.setUsuario(eUsuario.getNombre());
        eBitacora.setAccion("/reseteoPassword");
        eBitacora.setTabla(UsuarioDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(String.valueOf(eUsuario.getId()));
        eBitacora.setContenido( mapper.writeValueAsString(eUsuario));
        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){  
        	error = true;
        	mensajeError = gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale);
        }  
        
        BitacoraClave eBitacoraClave = new BitacoraClave();
        eBitacoraClave.setIdUsuario(eUsuario.getId());
        eBitacoraClave.setClave(passwordEncoder.encode(eUsuario.getClave()));
        eBitacoraClave.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
        
        respuesta= dBitacoraClave.guardarRegistro(eBitacoraClave);
        if (respuesta.estado==false){ 
        	error = true;
        	mensajeError = gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale);
        }
        //Guardar en la bitacora
        mapper = new ObjectMapper();
        eBitacora.setUsuario(eUsuario.getNombre());
        eBitacora.setAccion("/admin/bitacoraClave/crear");
        eBitacora.setTabla(BitacoraClaveDao.NOMBRE_TABLA);
        eBitacora.setIdentificador(respuesta.valor);
        eBitacora.setContenido(mapper.writeValueAsString(eBitacoraClave));
        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
        respuesta= dBitacora.guardarRegistro(eBitacora);
        if (respuesta.estado==false){   
        	error = true;
        	mensajeError = gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale);
        }
    	
    	if(!error){
	    	this.transaccion.commit(estadoTransaccion);
			//vista= new ModelAndView("panel");
			//vista.addObject("mensajeError", mensajeError);
		} else {
			this.transaccion.rollback(estadoTransaccion);
			//vista= new ModelAndView("reseteoPassword");
			//vista.addObject("mensajeError", respuesta.mensaje);
			//vista.addObject("username", username);
			//vista.addObject("password", password);
			//System.out.println("mensajeError " + mensajeError);
		}
    	
		String data = "{}";
		Gson gson = new GsonBuilder().create();
		
		data = gson.toJson(mensajeError);
		PrintWriter out = response.getWriter();
		out.print(data);
		System.out.println("mensajeError " + mensajeError);
    	
	} catch (Exception ex){

	}
}
















	/*@RequestMapping(value = "/reseteoPassword", method = RequestMethod.POST)
	public @ResponseBody ModelAndView confirmaPasswordNuevo(HttpServletRequest peticionHTTP, Locale locale){
		ModelAndView vista = null;
		TransactionDefinition definicionTransaccion = null;
		TransactionStatus estadoTransaccion = null;
		boolean error = false;
		String direccionIp="";
		Respuesta respuesta = new Respuesta();
		String username = "";
		String password = "";
		String nuevoPassword = "";
		String confirmaPaswword = "";
		Bitacora eBitacora=null;
		Usuario eUsuario = null;
		String mensajeError="";
		
		try{
			if (peticionHTTP.getParameter("username")!= null){
				username = peticionHTTP.getParameter("username") ;
		    }
			if (peticionHTTP.getParameter("password")!= null){
				password = peticionHTTP.getParameter("password") ;
			}
			if (peticionHTTP.getParameter("nuevoPassword")!= null){
				nuevoPassword = peticionHTTP.getParameter("nuevoPassword") ;
			}
			if (peticionHTTP.getParameter("confirmaPaswword")!= null){
				confirmaPaswword = peticionHTTP.getParameter("confirmaPaswword") ;
		    }
	
			System.out.println("username "+ username);
			System.out.println("password "+ password);
			System.out.println("nuevoPassword "+ nuevoPassword);
			System.out.println("confirmaPaswword "+ confirmaPaswword);
	
			if(username.length() == 0){
				error = true;
				mensajeError = "Debe llenar el campo Usuario";
			}
			
			if(nuevoPassword.length() == 0){
				error = true;
				mensajeError = "Debe llenar el campo Nueva Contraseña";
			}
			
			if(confirmaPaswword.length() == 0){
				error = true;
				mensajeError = "Debe llenar el campo Confirme Contraseña.";
			}
			
			if (!nuevoPassword.equals(confirmaPaswword)) {
				error = true;
				mensajeError = gestorDiccionario.getMessage("sgo.password.PasswordsNoIguales",null,locale);
			}
			
			eUsuario = dUsuario.getRecord(username);
		    
			if(eUsuario.getTipo() == 1 ){
				error = true;
				mensajeError = gestorDiccionario.getMessage("sgo.password.PasswordUserInterno",null,locale);
			}
	
			// Validamos el tamaÃ±o del password
		    if (nuevoPassword.length() < 8) {
		    	error = true;
		    	mensajeError = gestorDiccionario.getMessage("sgo.password.DebeContenerMinimo8Caracteres",null,locale);
		    }
	
		    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
		    //validamos que la nueva contraseña no sea igual a la que se encuentra en BD
		    if (passwordEncoder.matches(nuevoPassword, eUsuario.getClave())) {
		    	error = true;
		    	mensajeError = gestorDiccionario.getMessage("sgo.password.NoIgualAnterior",null,locale);
		    }
	
		    Respuesta validaPassword = Utilidades.validaPassword(nuevoPassword);
		    
		    if(!validaPassword.estado){
		    	error = true;
		    	mensajeError = validaPassword.mensaje;
		    }
	
		    //Inicia la transaccion
			this.transaccion = new DataSourceTransactionManager(dUsuario.getDataSource());
			definicionTransaccion = new DefaultTransactionDefinition();
			estadoTransaccion = this.transaccion.getTransaction(definicionTransaccion);
			eBitacora = new Bitacora();
			//Auditoria local (En el mismo registro)
			direccionIp = peticionHTTP.getHeader("X-FORWARDED-FOR");  
			if (direccionIp == null) {  
				direccionIp = peticionHTTP.getRemoteAddr();  
			}
	
			eUsuario.setClave(passwordEncoder.encode(nuevoPassword));
			eUsuario.setActualizadoEl(Calendar.getInstance().getTime().getTime());
			eUsuario.setActualizadoPor(eUsuario.getId()); 
			eUsuario.setIpActualizacion(direccionIp);
			eUsuario.setClaveTemporal(0);
			eUsuario.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
			eUsuario.setIntentos(0);
	
	        respuesta= dUsuario.ActualizarPassword(eUsuario);
	        if (respuesta.estado==false){         
	        	error = true;
	        	mensajeError = gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale);
	        }
	        //Guardar en la bitacora
	        ObjectMapper mapper = new ObjectMapper();
	        eBitacora.setUsuario(eUsuario.getNombre());
	        eBitacora.setAccion("/reseteoPassword");
	        eBitacora.setTabla(UsuarioDao.NOMBRE_TABLA);
	        eBitacora.setIdentificador(String.valueOf(eUsuario.getId()));
	        eBitacora.setContenido( mapper.writeValueAsString(eUsuario));
	        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
	        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
	        respuesta= dBitacora.guardarRegistro(eBitacora);
	        if (respuesta.estado==false){  
	        	error = true;
	        	mensajeError = gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale);
	        }  
	        
	        BitacoraClave eBitacoraClave = new BitacoraClave();
	        eBitacoraClave.setIdUsuario(eUsuario.getId());
	        eBitacoraClave.setClave(passwordEncoder.encode(eUsuario.getClave()));
	        eBitacoraClave.setActualizacionClave(new Date(Calendar.getInstance().getTime().getTime()));
	        
	        respuesta= dBitacoraClave.guardarRegistro(eBitacoraClave);
	        if (respuesta.estado==false){ 
	        	error = true;
	        	mensajeError = gestorDiccionario.getMessage("sgo.actualizarFallido",null,locale);
	        }
	        //Guardar en la bitacora
	        mapper = new ObjectMapper();
	        eBitacora.setUsuario(eUsuario.getNombre());
	        eBitacora.setAccion("/admin/bitacoraClave/crear");
	        eBitacora.setTabla(BitacoraClaveDao.NOMBRE_TABLA);
	        eBitacora.setIdentificador(respuesta.valor);
	        eBitacora.setContenido(mapper.writeValueAsString(eBitacoraClave));
	        eBitacora.setRealizadoEl(eUsuario.getActualizadoEl());
	        eBitacora.setRealizadoPor(eUsuario.getActualizadoPor());
	        respuesta= dBitacora.guardarRegistro(eBitacora);
	        if (respuesta.estado==false){   
	        	error = true;
	        	mensajeError = gestorDiccionario.getMessage("sgo.guardarBitacoraFallido",null,locale);
	        }
	    	System.out.println("mensajeError " + mensajeError);
	    	System.out.println("error " + error);
	    	if(!error){
		    	this.transaccion.commit(estadoTransaccion);
	    		vista= new ModelAndView("panel");
				vista.addObject("mensajeError", mensajeError);
	    	} else {
	    		this.transaccion.rollback(estadoTransaccion);
	    		vista= new ModelAndView("reseteoPassword");
				vista.addObject("mensajeError", respuesta.mensaje);
				vista.addObject("username", username);
				vista.addObject("password", password);
				System.out.println("mensajeError " + mensajeError);
	    	}
		} catch (Exception ex){
			//ex.printStackTrace();
			Utilidades.gestionaError(ex, sNombreClase, "confirmaPasswordNuevo");
			this.transaccion.rollback(estadoTransaccion);
			respuesta.estado=false;
			respuesta.mensaje=ex.getMessage();
			vista= new ModelAndView("reseteoPassword");
			vista.addObject("mensajeError", respuesta.mensaje);
			vista.addObject("username", username);
			vista.addObject("password", password);
			System.out.println("mensajeError " + mensajeError);
		}

		return vista;
	}*/

}
