package sgo.utilidades;

public class Constante {
 public static final String FILTRO_STRING_TODOS="todos";
 public static final int TRANSPORTE_ESTADO_ASIGNADO=3;
 public static final int TRANSPORTE_ESTADO_DESCARGADO=4;
 public static final int TRANSPORTE_ESTADO_ANULADO=5;
 public static final int TRANSPORTE_ESTADO_PROGRAMADO=6;

 public static final int SITUACION_NO_IMPORTADO=1;
 public static final int SITUACION_IMPORTADO=2;
 
 public static final int ESTADO_ACTIVO=1;
 public static final int ESTADO_INACTIVO=2;
 public static final int FILTRO_TODOS=0;
 public static final int FILTRO_NINGUNO=-1;
 public final static int ESTADO_REGISTRADO =1;
 public final static int ESTADO_EMITIDO =2;
 public final static int JORNADA_ESTADO_LIQUIDADO =4;
 public static final int SIN_VALOR=0;
 public static final int ORIGEN_AUTORIZACION_PLANIFICACION=1;
 public static final int ORIGEN_AUTORIZACION_ASIGNACION=2;
 public static final int ORIGEN_AUTORIZACION_DESCARGA=3;
 public static final int ORIGEN_AUTORIZACION_CIERRE=4;
 public static final String ESQUEMA_APLICACION="sgo.";
 public static final String ESQUEMA_SEGURIDAD="seguridad.";	
 public static final String SQL_LIMIT_MYSQL=" LIMIT ?,? ";
 public static final String SQL_LIMIT_POSTGRES="  OFFSET ? LIMIT ? ";
 public static final String SQL_LIMIT_CONFIGURADO=SQL_LIMIT_POSTGRES;	
 public static final int CON_PAGINACION=1;
 public static final int SIN_PAGINACION=0;
 public static final int CANTIDAD_PAGINACION=20;
 public static final int CANTIDAD_PAGINACION_TRANSPORTE=5;
 public static final String FORMATO_FECHA_ESTANDAR="yyyy-MM-dd";
 public static final String FORMATO_FECHA_DDMMYYYY="dd/MM/yyyy";
 public static final int TIPO_FORMATO_FECHA_ESTANDAR=1;
 public static final int TIPO_FORMATO_FECHA_DDMMYYYY=2;
 public static final int TIPO_FORMATO_FECHA_MMDDYYYY=3;	
 public static final int EXCEPCION_GENERICA = 10;
 public static final int EXCEPCION_DATOS = 1000;
 public static final int EXCEPCION_INTEGRIDAD_DATOS = EXCEPCION_DATOS +1;
 public static final int EXCEPCION_ACCESO_DATOS = EXCEPCION_DATOS + 2;
 public static final int EXCEPCION_CANTIDAD_REGISTROS_INCORRECTA = EXCEPCION_DATOS + 3;
 public static final int ENLACE_TIPO_GRUPO=1;
 public static final int ENLACE_TIPO_MENU=2;
 public static final int ENLACE_TIPO_ACCION=3;
 public static final String LDAP_URL_PROVEEDOR ="ldap://ldap.petroperu.com.pe";
 public static final String LDAP_CONTEXTO_FACTORIA="com.sun.jndi.ldap.LdapCtxFactory";
 public static final String LDAP_SECURITY_SSL="simple";
 public static final String LDAP_SECURITY_SASL="simple";
 public static final String LDAP_SECURITY_SIMPLE="simple";
 public static final String LDAP_METODO_AUTENTICACION=LDAP_SECURITY_SIMPLE;
 public static final String LDAP_PUERTO="389";
 public static final String LDAP_BASE_DN="dc=petroperu,dc=com,dc=pe";
 public static final String LDAP_CORREO="@petroperu.com.pe";
 public static final String SQL_Y=" AND ";
 public static final String SQL_O=" OR ";
 public static final String SQL_ENTRE=" BETWEEN ";
 public static final String SQL_ORDEN=" ORDER BY ";
 public static final String ORIGEN_MANUAL="M";
 public static final String ORIGEN_AUTOMATICO="A";
 public static final int EVENTO_ORIGEN_TRANSPORTE=1;
 public static final int EVENTO_ORIGEN_DESCARGA=2;
 public static final String PREFIJO_ARCHIVO_PROGRAMACION="prog";
	
	/*
	#Administrador del Directorio
	SECURITY_PRINCIPAL=Directory Manager
	#Nombre del dominio base del directorio
	BASE_DN=dc=petroperu,dc=com,dc=pe
	#Autenticacion
	#-------------
	#Puede ser simple, por SSL o via SASL
	#SSL: Autentica con encripcion SSL a traves de la red.
	#SASL: Usa el mecanismo MD5/Kerberos Uses MD5/Kerberos mechanisms. SASL is a simple authentication and security layer-based scheme
	SECURITY_AUTHENTICATION=simple/*/
}
