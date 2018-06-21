package sgo.utilidades;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sgo.entidad.GuiaCombustible;
import sgo.entidad.Respuesta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Clase base para hacer las validaciones.
 * 
 * @author IBM DEL PERÚ / knavarro
 * @since 21/XIII/2015
 */
public class Utilidades extends BaseUtilidades{
	
	/** Instancia del log4j */
    protected static final Logger Log = Logger.getLogger(Utilidades.class);
    // Mensaje de errores.
    private String mError;
    private final static String LIT_ERROR = "Error: ";
    private final static String LIT_WARNING = "Warning: ";
    private final static String LIT_INFO = "Info: ";
    private final static String LIT_TRACE = "Traza: ";  
    
    /** Constante para retornos de error. */
    protected static final int ERROR = -1;

    /** Nombre de la clase. */
    private String nombreClase;
    
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
 /** Formato de fecha. */
 public static JdbcTemplate jdbcTemplate;
 public static NamedParameterJdbcTemplate namedJdbcTemplate;
 public static final String FORMATO_FECHA = "DD/MM/YYYY";
 public final static String DATOS_AUDITORIA = " t1.creado_el, t1.creado_por, t1.actualizado_por, t1.actualizado_el, t1.usuario_creacion, t1.usuario_actualizacion, t1.ip_creacion, t1.ip_actualizacion ";

 /** Constante para cadenas vacias. */
 public static final String STRVACIO = "";

 /**
  * Validate given email with regular expression.
  * 
  * @param email
  *            email for validation
  * @return true valid email, otherwise false
  */
 public static boolean validaEmail(String email) {
	 boolean retorno;
     // Compiles the given regular expression into a pattern.
     Pattern pattern = Pattern.compile(PATTERN_EMAIL);

     // Match the given input against this pattern
     Matcher matcher = pattern.matcher(email);
     retorno = matcher.matches();
     return retorno;

 }
 
 /**
  * Validate given email with regular expression.
  * 
  * @param email
  *            email for validation
  * @return true valid email, otherwise false
  */
 public static boolean validaEmail(ArrayList<String> listaEmail) {
	 String email = "";
	 boolean retorno = false;
	 for(int i = 0; i < listaEmail.size(); i++){
		 email = listaEmail.get(i);
		 // Compiles the given regular expression into a pattern.
	     Pattern pattern = Pattern.compile(PATTERN_EMAIL);

	     // Match the given input against this pattern
	     Matcher matcher = pattern.matcher(email);
	     retorno =  matcher.matches();
	     if (retorno == false){
	    	 return retorno;
	     }
	 }
	 return retorno;
 }
 
 /**que una llave foránea no sea nula y que su valor sea mayor a 0
  * 
  * @param entero El id de la llave foránea a validar
  * @return es correcto o no
  */
 public static boolean esValidoForingKey(Integer entero) {
  return entero != null && entero.intValue() > 0;
 }
 
 /**valida que el estado s{olo tenga valores 1 y 2
  * 
  * @param entero El valor del estado a validar
  * @return es correcto o no
  */
 public static boolean esValidoEstadoEntidad(Integer entero) {
  return entero != null && entero.intValue() >= 1 && entero.intValue() <= 2;
 }
 
 /**
  * Valida si un entero es correcto
  * 
  * @param entero
  *         El entero a validar
  * @return es correcto o no
  */
 public static boolean esValido(Integer entero) {
  return entero != null && entero.intValue() >= 0;
 }

 /**
  * Valida si un Long es correcto
  * 
  * @param largo
  *         El Long a validar
  * @return es correcto o no
  */
 public static boolean esValido(Long largo) {
  return largo != null && largo.longValue() >= 0;
 }

 /**
  * Valida si una cadena sea correcta
  * 
  * @param cadena
  *         La cadena a validar
  * @return es correcto o no
  */
 public static boolean esValido(String cadena) {
  return cadena != null && !cadena.equals(STRVACIO);
 }

 /**
  * Valida si un objeto esta instanciado.
  * 
  * @param objeto
  *         Objeto a validar
  * @return es correcto o no
  */
 public static boolean esValido(Object objeto) {
  return objeto != null;
 }

 /**
  * Comprueba si tiene contenido un String.
  * 
  * @param s
  *         String
  * @return boolean
  */
 public static boolean tieneContenido(String s) {
  return s != null && !s.trim().equals(STRVACIO);
 }

 /**
  * Comprueba si tiene contenido una lista.
  * 
  * @param l
  *         Lista
  * @return boolean
  */
 public static boolean tieneContenido(List l) {
  return l != null && !l.isEmpty();
 }

 /**
  * Comprueba si tiene contenido un ArrayList.
  * 
  * @param l
  *         Lista
  * @return boolean
  */
 public static boolean tieneContenido(ArrayList lista) {
  return lista != null && lista.size() > 0;
 }

 public static java.sql.Date convierteStringADate(String str) {
  java.sql.Date sql = null;
  SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
  try {
   java.util.Date resultado = format.parse(str);
   sql = new java.sql.Date(resultado.getTime());
  } catch (ParseException e) {
   e.printStackTrace();
  }
  return sql;
 }
 
 
 public static java.sql.Date convierteStringADate(String str,String patron) {
	  java.sql.Date sql = null;
	  SimpleDateFormat format = new SimpleDateFormat(patron);
	  try {
	   java.util.Date resultado = format.parse(str);
	   sql = new java.sql.Date(resultado.getTime());
	  } catch (ParseException e) {
	   e.printStackTrace();
	  }
	  return sql;
	 }
 public static String convierteDateAString(Date str,String patron) {
	  String sql = null;
	  SimpleDateFormat format = new SimpleDateFormat(patron);	
	  try {
		  sql=format.format(str);
		
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return sql;
 }
 
 public static int diferenciaEnDias2(Date fechaMayor, Date fechaMenor) {
	 long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
	 long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
	 return (int) dias;
 }

 public static String modificarFormatoFecha(String fecha) {
  String fechaModificada = null;
  String[] parametros = fecha.split("-");
  fechaModificada = new String(parametros[2] + "/" + parametros[1] + "/" + parametros[0]);
  System.out.println(fechaModificada);
  return fechaModificada;
 }
 
 public static String modificarFormatoFechaddmmaaaa(String fecha) {
  String fechaModificada = null;
  String[] parametros = fecha.split("/");
  fechaModificada = new String(parametros[2] + "-" + parametros[1] + "-" + parametros[0]);
  System.out.println(fechaModificada);
  return fechaModificada;
 }

 /**
  * Metodo para recuperar la fecha actual.
  * 
  * @return respuesta Fecha actual del sistema.
  */
 public static Respuesta recuperarFechaActual() {
  Date fechaActual = null;
  StringBuilder consultaSQL = new StringBuilder();
  Respuesta respuesta = new Respuesta();
  try {
   consultaSQL.append("SELECT now() WHERE 1=?");
   fechaActual = jdbcTemplate.queryForObject(consultaSQL.toString(), new Object[] { 1 }, Date.class);
   if (fechaActual == null) {
    respuesta.estado = true;
    respuesta.valor = null;
   } else {
    respuesta.estado = true;
    respuesta.valor = fechaActual.toString();
   }
  } catch (DataAccessException excepcionAccesoDatos) {
   excepcionAccesoDatos.printStackTrace();
   respuesta.error = Constante.EXCEPCION_ACCESO_DATOS;
   respuesta.estado = false;
  } catch (Exception excepcionGenerica) {
   excepcionGenerica.printStackTrace();
   respuesta.error = Constante.EXCEPCION_GENERICA;
   respuesta.estado = false;
  }
  return respuesta;
 }
 
 public static java.sql.Date sumarDias(int dias, Date fecha){
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.add(Calendar.DAY_OF_MONTH, +dias); 
		return  new java.sql.Date(cal.getTime().getTime());
	}
 
 public static java.sql.Date restarDias(int dias, Date fecha){
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.add(Calendar.DAY_OF_MONTH, -dias); 
		return  new java.sql.Date(cal.getTime().getTime());
	}

 public static boolean validarFecha(String fechaValidar, String formatoFecha) {
  if (fechaValidar == null) {
   return false;
  }
  SimpleDateFormat formateadorFecha = new SimpleDateFormat(formatoFecha);
  formateadorFecha.setLenient(false);
  try {
   // if not valid, it will throw ParseException
   Date fecha = formateadorFecha.parse(fechaValidar);
   System.out.println(fecha);
  } catch (ParseException e) {
   e.printStackTrace();
   return false;
  }
  return true;
 }
 
 public static boolean comparaTimestampConDate(Timestamp fecha1, Date fecha2) {
  if (fecha1 == null || fecha2 == null) {
   return false;
  }
  try {
	  Date date = Utilidades.convierteStringADate(Utilidades.convierteDateAString(fecha1, "dd/MM/yyyy"));
	  //Date date = new Date(fecha1.getYear(), fecha1.getMonth(), fecha1.getDay());	  
	  if(fecha2.compareTo(date)!=0){
		 return false;
	  }
  } catch (Exception e) {
   e.printStackTrace();
   return false;
  }
  return true;
 }

 @Autowired
 public void setDataSource(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
  this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
 }

 public DataSource getDataSource() {
  return this.jdbcTemplate.getDataSource();
 }

 /**
  * Gestiona el error de una Excepcion en codifica.
  * @param e1 Excepcion a procesar.
  * @param metodo Nombre del metodo que provoca la excepcion.
  */
 public static void gestionaError(Exception e1, String clase, String metodo) {
	 BasicConfigurator.configure();
	 Logger log = Logger.getLogger(" SGO: ");
     final String claseMetodo = clase + " / " + metodo + " - ";
     log.error(claseMetodo + LIT_ERROR + e1.toString());
    /* Log
     Log.addAppender(claseMetodo + LIT_ERROR + e1.toString());
     Log.org.apache.log4j.ConsoleAppender
      //(claseMetodo, e1);
*/ }
 
 /**
  * Gestiona el error de una Excepcion en codifica.
  * @param e1 Excepcion a procesar.
  * @param metodo Nombre del metodo que provoca la excepcion.
  */
 public static void gestionaWarning(Exception e1, String clase, String metodo, String querySQL) {
	 BasicConfigurator.configure();
	 Logger log = Logger.getLogger(" SGO: ");
     final String claseMetodo = clase + " / " + metodo + " - " + querySQL + " : ";
     log.warn(claseMetodo + LIT_WARNING + e1.getMessage());
}
 
 /**
  * Gestiona el error de una Excepcion en codifica.
  * @param e1 Excepcion a procesar.
  * @param metodo Nombre del metodo que provoca la excepcion.
  */
 public static void gestionaInfo(String clase, String metodo, String querySQL) {
	 BasicConfigurator.configure();
	 Logger log = Logger.getLogger(" SGO: ");
     final String claseMetodo = clase + " / " + metodo + " - " + querySQL;
     log.info(LIT_INFO + claseMetodo);
}
 
/**
  * Gestiona el error de una Excepcion en codifica.
  * @param e1 Excepcion a procesar.
  * @param metodo Nombre del metodo que provoca la excepcion.
  */
 public static void gestionaTrace(String clase, String metodo) {
	 BasicConfigurator.configure();
	 Logger log = Logger.getLogger(" SGO: ");
     final String claseMetodo = clase + " / " + metodo;
     log.trace(LIT_TRACE + claseMetodo);
}

public String getNombreClase() {
	return nombreClase;
}

public void setNombreClase(String nombreClase) {
	this.nombreClase = nombreClase;
}

/**
 * Con esto establecemos el error producido, para que otros métodos o la 
 * clase java (que no tiene por qué ser un EJB) que ha invocado a nuestro 
 * session bean pueda controlar la existencia de errores durante la ejecución 
 * del método del session bean
 * @param  mError La excepción producida por el error
 */
protected final void setError(String mError) {
    this.mError = mError;
}

/**
 * Método para grabar un error en el log
 * 
 * @param  objetoError El texto del error que queremos generar
 * @param  excepcion La excepción producida por el error
 */
protected final void error(Object objetoError, Throwable excepcion) {
    Log.error(objetoError, excepcion);
}

/**
 * Este método nos devuelve el último error que se ha guardado. Es útil para
 * compartir errores entre distintos métodos del session bean, e incluso para
 * obtener los errores producidos durante la ejecución de un método del session
 * bean desde otra clase java que haya efectuado la llamada a dicho método.
 *
 * @return      Devuelve el String con el error
 */
public String getError() {
    return mError;
}

public static Respuesta validaPassword(String password){
	Respuesta respuesta = new Respuesta();
	int len = 0;
	String cadMayusculas = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
	String cadMinusculas="abcdefghijklmnñopqrstuvwxyz";
	String cadnumeros = "0123456789";
	
	try {
		len = password.length();

		// Validamos el tamaÃ±o del password
	    if (len < 8) {
	    	respuesta.estado = false;
	    	respuesta.mensaje ="El password debe contener al menos 8 caracteres."; //FIXME vp.20170512 "El password debe contener como mínimo 8 caracteres.";
	    	respuesta.valor="sgo.password.minimocar";
	    	return respuesta;
	    };

	    //Validamos que la contraseña sea alfanumércia
	    boolean numerico = false;
	    for(int i = 0; i < password.length(); i++) {
	        if (cadnumeros.indexOf(password.charAt(i),0)!=-1){
	        	numerico = true;
	        }
	    };
	    if(!numerico) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = "El password debe ser alfanumérica.";
	    	respuesta.valor="sgo.password.alfa"; //FIXME vp.20170515
	    	return respuesta;
	    };
	    
	    boolean letra = false;
	    String passwordMinusculas = password.toLowerCase();
		for(int i=0; i<passwordMinusculas.length(); i++){
		   if (cadMinusculas.indexOf(passwordMinusculas.charAt(i),0)!=-1){
			   letra = true;
		   }
		}
		if(!letra) {
			respuesta.estado = false;
	    	respuesta.mensaje = "El password debe contener al menos una letra.";
	    	respuesta.valor="sgo.password.letra";//FIXME vp.20170515
	    	return respuesta;
		}
		
	    //Validamos que tenga al menos una mayuscula	    
	    boolean mayuscula = false;
	    for(int i=0; i<password.length(); i++){
	        if (cadMayusculas.indexOf(password.charAt(i),0)!=-1){
	        	mayuscula = true;
	        }
	     }
	    if(!mayuscula) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = "El password debe contener al menos una letra en mayúscula.";
	    	respuesta.valor="sgo.password.mayus";//FIXME vp.20170515
	    	return respuesta;
	    }

	    //Validamos que la contraseña no comience ni termine en un valor numérico
	    if ((cadnumeros.indexOf(password.charAt(0),0)!=-1) || (cadnumeros.indexOf(password.charAt(len-1),0)!=-1)){
	    	respuesta.estado = false;
	    	respuesta.mensaje = "El password no debe comenzar o terminar con valores numéricos.";
	    	respuesta.valor="sgo.password.inicio";//FIXME vp.20170515
	    	return respuesta;
	    }
	    
	    // Validamos que no haya 3 caracteres repetidos seguidos
	    for(int i=0; i< len - 2; i++){
	    	String subs1 = password.substring(i,i+1);
	    	String subs2 = password.substring(i+1,i+2);
	    	String subs3 = password.substring(i+2,i+3);
			if((subs1.equals(subs2)) && (subs2.equals(subs3))) {
				respuesta.estado = false;
		    	respuesta.mensaje = "El password no debe tener 3 caracteres iguales seguidos.";
		    	respuesta.valor="sgo.password.3iguales";//FIXME vp.20170515
		    	return respuesta;
			}

	    }
    respuesta.estado=true;
    respuesta.mensaje = "El password es correcto.";
    respuesta.valor="sgo.password.ok";//FIXME vp.20170515
	return respuesta;
} catch (Exception e) {
	respuesta.estado = false;
	respuesta.mensaje = e.getMessage();
	return respuesta;
}
}

public static String getGeneraPassword (){
	String cadenaAleatoria = "";
	long milis = new java.util.GregorianCalendar().getTimeInMillis();
	Random r = new Random(milis);
	int i = 0;
	while ( i < 8){
		char c = (char)r.nextInt(255);
		if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
		cadenaAleatoria += c;
		i ++;
		}
	}
	
	return cadenaAleatoria;
}

public static String retornaEstadoGEC(int idGEC) {
	  String estadoGECString = null;
	  if(idGEC==GuiaCombustible.ESTADO_REGISTRADO){
		  estadoGECString="Registrado";
	  }else if(idGEC==GuiaCombustible.ESTADO_EMITIDO){
		  estadoGECString="Emitido";
	  }else if(idGEC==GuiaCombustible.ESTADO_APROBADO){
		  estadoGECString="Aprobado";
	  }else if(idGEC==GuiaCombustible.ESTADO_OBSERVADO){
		  estadoGECString="Observado";
	  }
	  
	  return estadoGECString;
	 }

public static String generateCaptchaTextMethod1(){
    Random rdm=new Random();
    int rl=rdm.nextInt(); // Random numbers are generated.
    String hash1 = Integer.toHexString(rl); // Random numbers are converted to Hexa Decimal.

    return hash1;
}
 
public static String generateCaptchaTextMethod2(int captchaLength)   {
 
     String saltChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
     StringBuffer captchaStrBuffer = new StringBuffer();
     java.util.Random rnd = new java.util.Random();

     //build a random captchaLength chars salt
     while (captchaStrBuffer.length() < captchaLength){
        int index = (int) (rnd.nextFloat() * saltChars.length());
        captchaStrBuffer.append(saltChars.substring(index, index+1));
     }
     
     return captchaStrBuffer.toString();
}

public static String devolverStringDeListInteger(List<Integer> lstInput){
	String lstRet = "";
	
	if(lstInput == null){
		lstRet = "0";
	}else{
		for(Integer obj : lstInput){
			lstRet = lstRet + obj + ", ";
		}
		
		//borramos la ultima coma y espacio en blanco
		lstRet = lstRet.substring(0, lstRet.length()-2);
	}
	return lstRet;
}

}