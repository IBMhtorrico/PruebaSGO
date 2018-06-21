package sgo.utilidades;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

public class ConstanteMail {
	public static String PATH_FILE_WIN      = "c:\\safc_files"+File.separator; 
	public static String PATH_FILE_LNX      = "/var/www/html/safc_files/"; 
	public static String PATH_FILE          = SystemUtils.IS_OS_WINDOWS ? PATH_FILE_WIN : PATH_FILE_LNX;
	public static String REPORT_PATH_SAFC   = "/pe/com/petroperu/safc/reportes/";
	public static String SYSTEM_HOST	    = "http://10.1.18.107:8080/safc";//"http://localhost:8081/safc";
	public static String SYSTEM_NAME	    = "safc";
	public static String CHARACTER_EMPTY    = "";
	public static String MENSAJE_SESION_OUT = "La sesión ha culminado, por favor vuelva a ingresar al aplicativo SGO.";
	public static String DOMINIO_CORREO		= "petroperu.com.pe";

	/***************************	MAIL	*************************************************/
	/*public static String MAIL_SERVIDOR = "smtp.gmail.com";//"mail.gmail.com";
	public static String MAIL_PUERTO   = "587";
	public static String MAIL_USUARIO  = "pruebasibm2017@gmail.com";
	public static String MAIL_PASSWORD = "P3tr0p3ru";*/ 
	
	public static String MAIL_SERVIDOR = "mailofp.petroperu.com.pe";//"mail.gmail.com";
	//public static String MAIL_SERVIDOR = "smtp.petroperu.com.pe";
	public static String MAIL_PUERTO = "25";
	public static String MAIL_USUARIO = "user";
	public static String MAIL_PASSWORD = "password";
	public static String RUTA_REPORTES = "/pe/com/ibm/bs/administracion/recursos/reportes/";
	
	public static String EMAIL_ORIGEN_CONSULTA = "clientesgo@petroperu.com.pe";//"clientesweb@petroperu.com.pe";
	
	public static String REPORT_PATH 		= "/pe/com/petroperu/clientes/saldo/recursos/reportes/";
	public static String REPORT_PATH_SIAC 	= "/pe/com/petroperu/combustible/control/recursos/reportes/";

	public static String REPORT_SALDO_NAME = "listadoClientes";
	public static String PATH_INITIAL = "//src";
	public static String MAIL = "";
	//public static String AS400_URL = "jdbc:as400://10.1.0.110;translate binary=true;";  //10.3.1.2
	//public static String AS400_USR = "INTRANET"; //"EINTRANET";
	//public static String AS400_PSW = "tenartni"; //"TENARTNIE";
//	public static String AS400_USR = "P202081";
//	public static String AS400_PSW = "p3tr001";

	//Conexion al AS400
	public static String AS400_URL = "jdbc:as400://10.3.1.2;translate binary=true;";
	//jdbc:as400://10.1.0.110;translate binary=true;
	public static String AS400_USR = "EINTRANET";
	public static String AS400_PSW = "TENARTNIE";

	//public static String AS400_USR = "P103022";
	//public static String AS400_PSW = "CADL1881";
	
	//ybp 09-11
	
	//Conexion al PostgreSQL Nativo
	//public static String POSTGRE_URL = "jdbc:postgresql://10.1.18.41:5432/petroperu"; //produccion 41
	//public static String POSTGRE_URL = "jdbc:postgresql://10.1.18.42:5432/sco_talara"; //desarrollo 42
	public static String POSTGRE_URL = "jdbc:postgresql://10.1.18.42:5432/sgo_desarrollo";
	public static String POSTGRE_CLASS = "org.postgresql.Driver";
	public static String POSTGRE_USR = "postgres";  // 42
	public static String POSTGRE_PSW = "p3tr0m1n"; // 42
	
	//public static String POSTGRE_URL = "jdbc:postgresql://localhost:5432/sco_talara"; //desarrollo 42
	//public static String POSTGRE_URL = "jdbc:postgresql://localhost:5432/pre_prod_db"; //localhost/*SAP*/
	//public static String POSTGRE_PSW = "postgres1"; 
	//public static String POSTGRE_PSW = "postgres";
	
	//public static String POSTGRE_USR = "adminrs"; // 41
	//public static String POSTGRE_PSW = "rosp1986"; // 41
	
	//Conexion al PostgreSQL Nativo
	public static String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
	//public static String ORACLE_CLASS = "org.oracle.Driver";
	public static String ORACLE_USR = "SYSTEM";
	public static String ORACLE_PSW = "123";
	
	
	public static String CONEXION_DS_01 = "java:/SCCDS";
	
	//Constantes a utilizar por la aplicacion
    public static final String USERSESION = "usuario";
    public static final String PERSONASESION = "v_persona";
    public static final String SOCIEDADSESION = "sociedad";
    public static String SISROLUSUARIO_SESION = "sisrolusuario";
    public static final String PARAMETROSSESION = "parametros";
    public static final String TREEMENUSESSION = "treemenu";
	
	
	public static Integer CAN_REG_CLIENTES = 50;
	/*Para generar en automatico el usuario y password*/
	public static String PARAM_USER_NEW = "SCCPAT";
	public static String PARAM_PASS_NEW = "SCCPAT";
	public static String PARAM_PASS_KEY = "����";
	public static String EMAIL_DESTINO_CONSULTA = "consultadesaldosviaweb@petroperu.com.pe"; //"cobranzas_comercio@petroperu.com.pe";
	
	///var/tmp
	
	//public static Long ID_SISTEMA = 44L;
	//public static Integer ID_ROL_ADMINISTRADOR = 23;
	//public static Integer ID_ROL_PETRO = 25;
	//public static Integer ID_ROL_CLIENTE = 43;
	
	
	public static Integer NUM_MEGAS_MAX = 10000;
	//public static String PATH_FILE_WIN = "c:\\tacho\\"; 
	//public static String PATH_FILE_LNX = "/tmp/"; 
	public static String PATH_FILE_TEMP = SystemUtils.IS_OS_WINDOWS? PATH_FILE_WIN : PATH_FILE_LNX;
	

	
	//public static String OPENEJB_FACTORY_INITIAL_LOCAL = "org.openejb.client.LocalInitialContextFactory";
	//public static String OPENEJB_FACTORY_INITIAL_REMOTE = "org.openejb.client.RemoteInitialContextFactory";
	//public static String OPENEJB_PROVIDER_URL = "http://localhost:8085/openejb/ejb";  //  8089 - 8085
	//public static String OPENEJB_PROVIDER_URL = "http://localhost:8089/openejb/ejb";  //  8089 - 8085
	//public static String OPENEJB_LOCAL = "Local";
	//public static String OPENEJB_REMOTE = "Remote";
	public static String EJB_LOCAL = "/local";
	public static String EJB_REMOTE = "/remote";
	public static String EJB_EAR = "PetroEAR/";
	

	public static String AUDITORIA_SESION = "auditoria";
	
	
	
	//BASE DE DATOS
	
	public static String BASE_DATOS =  "sgo_desarrollo"; //desarrollo 42
	
	//public static String BASE_DATOS =  "petroperu"; //produccion 41
	
	
	
	
	public static String ESQUEMA_LOG = "logger";
	
	public static String ESQUEMA_CONFIGURACION = "configuracion";
	public static String ESQUEMA_SEGURIDAD = "seguridad";
	public static String ESQUEMA_CLIENTES = "clientes";
	
//	public static String MAIL_SERVIDOR = "mailofp.petroperu.com.pe";//"mail.gmail.com";
//	public static String MAIL_PUERTO = "25";
//	public static String MAIL_USUARIO = "user";
//	public static String MAIL_PASSWORD = "password";
	
	
	public static String LOG_LEVEL = "DEBUG";
	public static String LOG_DIR = "DEBUG";
	public static String LOG_FILENAME = "c:\\defaultLog.log";
	
	
	public static String LOG_MAXLENGTH = "10000000";
	
	public static String Y = " AND ";
	public static String O = " OR ";
	public static String IS = " IS ";
	public static String IGUAL = " = ";
	public static String DIFERENTE = " <> ";
	public static String MAYOR = " > ";
	public static String MENOR = " < ";
	public static String MAYOR_IGUAL = " >= ";
	public static String MENOR_IGUAL = " <= ";
	public static String SUMA = " + ";
	public static String RESTA = " - ";
	public static String MULTIPLICACION = " * ";
	public static String DIVISION = " / ";
	
	public static String SELECT_FROM = " Select * from ";
	public static String LIKE = " like ";
	public static String UPPER = " UPPER";
	public static String WHERE = " WHERE ";
	
	
	/************************************* CODIGOS DEL CATALOGO SEGUN TABLAS *******************/
	public static String TABLA_SISTEMA = "01";  //Lista de los Nombres de los Sistemas
	public static String TABLA_ROLES_USUARIO = "02";  //Lista de los Roles de los Usuarios
	public static String TABLA_PERMISOS_USUARIO = "03";  //Lista de los Roles de los Usuarios
	public static String TABLA_ICONOS = "000350";  //Lista de iconos para los Sistemas
	public static String TABLA_CONVERSION_6B = "000354";  //Tabla de Conversion 6B
	public static String TABLA_CONVERSION_5B = "000355";  //Tabla de Conversion 5B
	public static String TABLA_CONVERSION_13 = "000356";  //Tabla de Factor de Conversion 13
	
	public static double RANGO_VALOR_TABLAS_6B5B = 0.5; 
	
	
	public static String ELEMENTO_SISTEMA_SALDOS = "01";  //Sistema Saldos
	
	public static String ABREVIATURA_TIPO_PERSONA = "TI_PERS";  //Sistema Saldos
	public static String ABREVIATURA_ESTADO = "EST";  //Sistema Saldos
	

	
	public static synchronized String getUPPER(String dato){
		return UPPER + "(" + dato+") ";
	}
	
	
	public static synchronized String getUPPERTEXTO(String dato, String longitud){
		return UPPER + "( cast(" + dato+" as varchar("+longitud+"))) ";
	}
	
	public static synchronized String getLIKE_P0(String dato){
		return LIKE + "'%" + dato.toUpperCase()+"' ";
	}
	
	public static synchronized String getLIKE_0P(String dato){
		return LIKE + "'" + dato.toUpperCase()+"%' ";
	}
	
	public static synchronized String getLIKE_PP(String dato){
		return LIKE + "'%" + dato.toUpperCase()+"%' ";
	}
	
	
	/*******************************************/
	//pe.com.petroperu.clientes.saldo.servlet;
	public static Long ID_SISTEMA = 44L;
	public static Integer ID_ROL_ADMINISTRADOR = 23;
	public static Integer ID_ROL_PETRO = 25;
	public static Integer ID_ROL_CLIENTE = 43;
	
	
	
	/*IAC*/
	/*------------------------PARA USARSE EN EL TENDER-----------------------------*/
	
	public static String REPORT_PATH_TENDER = "/pe/com/petroperu/tender/recursos/reportes/";
	
	public static String USERCAPTCHA = "captcha";
	
	public static String CARPETA_PROPUESTAS = "propuestas"; 
	public static String CARPETA_INVITACION = "invitaciones";
	public static String CARPETA_CARTAAWARD = "cartaaward";
	public static String CARPETA_AGRADECIMI = "agradecimientos";
	
	public static String VALOR_ELIMINACION = "E";
	public static String CAMPO_ELIMINACION = "estado";
	
	public static String PATH_FILE_TEN_PROP_WIN = "c:\\tacho\\"+CARPETA_PROPUESTAS+File.separator; 
	public static String PATH_FILE_TEN_PROP_LNX = "/var/www/html/tender/"+CARPETA_PROPUESTAS+"/"; 
	public static String PATH_FILE_TEN_PROP 	= SystemUtils.IS_OS_WINDOWS? PATH_FILE_TEN_PROP_WIN : PATH_FILE_TEN_PROP_LNX;
	
	public static String PATH_FILE_TEN_INVI_WIN = "c:\\tacho\\"+CARPETA_INVITACION+File.separator;; 
	public static String PATH_FILE_TEN_INVI_LNX = "/var/www/html/tender/"+CARPETA_INVITACION+"/"; 
	public static String PATH_FILE_TEN_INVI 	= SystemUtils.IS_OS_WINDOWS? PATH_FILE_TEN_INVI_WIN : PATH_FILE_TEN_INVI_LNX;
	
	public static String PATH_FILE_TEN_AWAR_WIN = "c:\\tacho\\"+CARPETA_CARTAAWARD+File.separator;; 
	public static String PATH_FILE_TEN_AWAR_LNX = "/var/www/html/tender/"+CARPETA_CARTAAWARD+"/"; 
	public static String PATH_FILE_TEN_AWAR 	= SystemUtils.IS_OS_WINDOWS? PATH_FILE_TEN_AWAR_WIN : PATH_FILE_TEN_AWAR_LNX;
	
	public static String PATH_FILE_TEN_AGRA_WIN = "c:\\tacho\\"+CARPETA_AGRADECIMI+File.separator;; 
	public static String PATH_FILE_TEN_AGRA_LNX = "/var/www/html/tender/"+CARPETA_AGRADECIMI+"/"; 
	public static String PATH_FILE_TEN_AGRA 	= SystemUtils.IS_OS_WINDOWS? PATH_FILE_TEN_AGRA_WIN : PATH_FILE_TEN_AGRA_LNX;
	
	public static String URL_CONTACTS = "http://aplicativosdev.petroperu.com.pe:8080/tender/";
	public static String URL_PROPOSAL = "http://aplicativosdev.petroperu.com.pe:8080/tender/";
	public static String URL_APACHE_TENDER = "http://aplicativosdev.petroperu.com.pe/tender/";
	public static String EMAIL_ORIGEN_TENDER = "tenderpetroperu@petroperu.com.pe";
	public static String EMAIL_PRUEBA_TENDER = "";
	
	public static String ARCHIVO_PDF_PRUEBA = "archivo1.pdf";
	/*IAC*/
}
