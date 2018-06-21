package sgo.servicio;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sgo.datos.BitacoraDao;
import sgo.datos.EnlaceDao;
import sgo.datos.Tabla13Dao;
import sgo.datos.Tabla5BDao;
import sgo.datos.ToleranciaDao;
import sgo.entidad.Contenido;
import sgo.entidad.Enlace;
import sgo.entidad.FormulaRespuesta;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Tabla13;
import sgo.entidad.Tabla5B;
import sgo.entidad.Tolerancia;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Formula;

@Controller
public class FormulaControlador {
 @Autowired
 private MessageSource gestorDiccionario;
 @Autowired
 private BitacoraDao dBitacora; 
 @Autowired
 private EnlaceDao dEnlace;
 @Autowired
 private ToleranciaDao dTolerancia;
 @Autowired
 private Tabla5BDao dTabla5B;
 @Autowired
 private Tabla13Dao dTabla13;
 private static final String URL_RECUPERAR_VOLUMEN_CORREGIDO_COMPLETA = "/admin/formula/recuperar-volumen-corregido";
 private static final String URL_RECUPERAR_VOLUMEN_CORREGIDO_RELATIVA = "/formula/recuperar-volumen-corregido";
 
 private static final String URL_RECUPERAR_FACTOR_MASA_COMPLETA = "/admin/formula/recuperar-factor-masa";
 private static final String URL_RECUPERAR_FACTOR_MASA_RELATIVA = "/formula/recuperar-factor-masa";
 
 private static final String URL_RECUPERAR_TOLERANCIA_COMPLETA = "/admin/formula/recuperar-tolerancia";
 private static final String URL_RECUPERAR_TOLERANCIA_RELATIVA = "/formula/recuperar-tolerancia";
 
 private static final String URL_RECUPERAR_FACTOR_CORRECCION_COMPLETA = "/admin/formula/recuperar-factor-correccion";
 private static final String URL_RECUPERAR_FACTOR_CORRECCION_RELATIVA = "/formula/recuperar-factor-correccion";
 
 @RequestMapping(value = URL_RECUPERAR_TOLERANCIA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody RespuestaCompuesta recuperarTolerancia(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_TOLERANCIA_COMPLETA);
   if (respuesta.estado == false) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   if (httpRequest.getParameter("filtroProducto") != null) {
    parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
   }   
   if (httpRequest.getParameter("filtroEstacion") != null) {
    parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
   } 
   respuesta =  dTolerancia.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   respuesta.estado=true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarFormulaExitoso", null, locale);   
   } catch(Exception ex){
    
   }
  return respuesta;
 }
 
 @RequestMapping(value = URL_RECUPERAR_FACTOR_MASA_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody RespuestaCompuesta recuperarFactorConversionMasa(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  Tabla13 eTabla13=null;
  Tolerancia eTolerancia=null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_FACTOR_MASA_COMPLETA);
   if (respuesta.estado == false) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   parametros = new ParametrosListar();
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   if (httpRequest.getParameter("apiObservado") != null) {
    parametros.setFiltroApiObservado( (Float.parseFloat(httpRequest.getParameter("apiObservado"))));
   } else {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.errorFaltaValorApi", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   if (httpRequest.getParameter("filtroProducto") != null) {
    parametros.setFiltroProducto(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
   }else {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.errorFaltaProducto", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   
   if (httpRequest.getParameter("filtroEstacion") != null) {
    parametros.setFiltroEstacion(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
   }else {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.errorFaltaProducto", null, locale);
    throw new Exception(mensajeRespuesta);
   }
     
   respuesta =  dTabla13.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   eTabla13 = (Tabla13) respuesta.contenido.carga.get(0);
   
   respuesta =  dTolerancia.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   if (respuesta.contenido.carga.size()<1){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   eTolerancia = (Tolerancia) respuesta.contenido.carga.get(0);
   
   Contenido<FormulaRespuesta> contenido = new Contenido<FormulaRespuesta>();
   FormulaRespuesta eFormula = new FormulaRespuesta();
   eFormula.setTolerancia(Formula.redondearDouble(eTolerancia.getPorcentajeActual(), 2));
   eFormula.setFactorCorreccion(eTabla13.getFactor());
   eFormula.setTipoVolumen(eTolerancia.getTipoVolumen());
   contenido.carga= new ArrayList<FormulaRespuesta>();
   contenido.carga.add(eFormula);
   respuesta.contenido = contenido;   
   respuesta.estado=true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarFormulaExitoso", null, locale);   
   } catch(Exception ex){
    respuesta.contenido = null;   
    respuesta.estado=false;
    respuesta.mensaje = gestorDiccionario.getMessage("sgo.errorRecuperarFactorMasa", null, locale);  
   }
  return respuesta;
 }
 
 @RequestMapping(value = URL_RECUPERAR_VOLUMEN_CORREGIDO_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarVolumenCorregido(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  double volumenObservado=0;
  double volumenCorregido=0;
  double factorCorreccionVolumen=0; 
  double apiObservado=0;  
  double apiCorregido=0;
  double temperaturaCentro=0; 
  double temperaturaProbeta=0;  
  Contenido<FormulaRespuesta> contenido =null;
  FormulaRespuesta eFormulaRespuesta=null;
  Tabla5B eTabla5B1=null;
  Tabla5B eTabla5B2=null;
  int idProducto=0;
  int idEstacion =0;
  Tolerancia eTolerancia=null;
  try {
   principal = this.getCurrentUser();
   respuesta = dEnlace.recuperarRegistro(URL_RECUPERAR_VOLUMEN_CORREGIDO_COMPLETA);
   if (respuesta.estado == false) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
    throw new Exception(mensajeRespuesta);
   }
   parametros = new ParametrosListar();
   if (httpRequest.getParameter("apiObservado") != null && !httpRequest.getParameter("apiObservado").isEmpty()) {
    apiObservado = (Double.parseDouble(httpRequest.getParameter("apiObservado")));
   }
   
   if (httpRequest.getParameter("apiCorregido") != null && !httpRequest.getParameter("apiCorregido").isEmpty()) {
    apiCorregido = (Double.parseDouble(httpRequest.getParameter("apiCorregido")));
   }
   
   if (httpRequest.getParameter("temperaturaCentro") != null && !httpRequest.getParameter("temperaturaCentro").isEmpty()) {
    temperaturaCentro=(Double.parseDouble(httpRequest.getParameter("temperaturaCentro")));
   }
   if (httpRequest.getParameter("temperaturaProbeta") != null && !httpRequest.getParameter("temperaturaProbeta").isEmpty()) {
    temperaturaProbeta= (Double.parseDouble(httpRequest.getParameter("temperaturaProbeta")));
   }
   if (httpRequest.getParameter("volumenObservado") != null && !httpRequest.getParameter("volumenObservado").isEmpty()) {
    volumenObservado=(Double.parseDouble(httpRequest.getParameter("volumenObservado")));
   }   
   if (httpRequest.getParameter("filtroProducto") != null && !httpRequest.getParameter("filtroProducto").isEmpty()) {
    idProducto=(Integer.parseInt(httpRequest.getParameter("filtroProducto")));
    parametros.setFiltroProducto(idProducto);
   }   
   if (httpRequest.getParameter("filtroEstacion") != null && !httpRequest.getParameter("filtroEstacion").isEmpty()) {
    idEstacion=(Integer.parseInt(httpRequest.getParameter("filtroEstacion")));
    parametros.setFiltroEstacion(idEstacion);
   }   
   respuesta =  dTolerancia.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   if (respuesta.contenido.carga.size()<1){
    throw new Exception(gestorDiccionario.getMessage("sgo.noToleranciaConfigurada", null, locale));
   }
   eTolerancia = (Tolerancia) respuesta.contenido.carga.get(0);
   
   long parteEntera;
   double decimalApiObservado;
   double decimalTemperatura;
   boolean apiExacto=false;
   boolean temperaturaExacto=false;
   double valorX1 =0 ;
   double valorX2 =0 ;
   double valorXA1 =0 ;
   double valorXA2 =0 ;
   double valorYA1 =0 ;
   double valorYA2 =0 ;
   double Y1 =0 ;
   double Y2 =0 ;
   String filtroEspecial ="";
   String filtroEspecialOrdenamiento="";
   parteEntera = (long) apiObservado;
   decimalApiObservado = apiObservado - parteEntera;
   if ((decimalApiObservado ==0 )||(decimalApiObservado ==0.5)) {
    apiExacto =true;
   }
   
   parteEntera = (long) temperaturaProbeta;
   decimalTemperatura = temperaturaProbeta - parteEntera;
   if ((decimalTemperatura ==0 )||(decimalTemperatura ==0.5)) {
    temperaturaExacto =true;
   }
   
   if ((apiExacto==true)&&(temperaturaExacto==true)){
    parametros.setPaginacion(Constante.SIN_PAGINACION);   
    parametros.setFiltroApiObservado((float) apiObservado);
    parametros.setFiltroTemperaturaObservada((float) temperaturaProbeta);
    respuesta = dTabla5B.recuperarRegistros(parametros);
    if (respuesta.estado==false){
     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
    }
    if (respuesta.contenido.carga.size()==0){
     throw new Exception(gestorDiccionario.getMessage("sgo.errorValoresFueraRango", null, locale));
    }    
    eTabla5B1 = (Tabla5B) respuesta.contenido.carga.get(0);
    apiCorregido = eTabla5B1.getApiCorregido();
   } else if ((apiExacto==true)&&(temperaturaExacto==false)) {
     if (decimalTemperatura > 0.5){
      valorX1 = (long) temperaturaProbeta + 0.5;
      valorX2 =(long) temperaturaProbeta +1 ;
     }else {
      valorX1 =(long) temperaturaProbeta;
      valorX2 =(long) temperaturaProbeta + 0.5 ;
     }
   
     filtroEspecial = " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " +apiObservado  ;
     filtroEspecial = filtroEspecial + " AND ";
     filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX1+") ";    
     filtroEspecial = filtroEspecial + " OR ";
     filtroEspecial = filtroEspecial + " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " + apiObservado;
     filtroEspecial = filtroEspecial + " AND ";
     filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX2+") ";
     
     parametros.setPaginacion(Constante.SIN_PAGINACION);
     parametros.setFiltroEspecialCombinacion(filtroEspecial);
     filtroEspecialOrdenamiento=Tabla5BDao.CAMPO_API_OBSERVADO + " ASC ," + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA+ " ASC";
     parametros.setFiltroEspecialOrdenamiento(filtroEspecialOrdenamiento);
     respuesta = dTabla5B.recuperarRegistros(parametros);
     if (respuesta.estado==false){
      throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
     }
     if (respuesta.contenido.carga.size()<2){
      throw new Exception(gestorDiccionario.getMessage("sgo.errorValoresFueraRango", null, locale));
     }
     eTabla5B1 = (Tabla5B) respuesta.contenido.carga.get(0);
     eTabla5B2 = (Tabla5B) respuesta.contenido.carga.get(1);
     Y1 =  eTabla5B1.getApiCorregido();
     Y2 =  eTabla5B2.getApiCorregido();
     apiCorregido = Formula.calcularInterpolacion(valorX1, valorX2, Y1, Y2,temperaturaProbeta);   
   } else if ((apiExacto==false)&&(temperaturaExacto==true)) {
    if (decimalApiObservado > 0.5){
     valorX1 = (long) apiObservado + 0.5;
     valorX2 =(long) apiObservado +1 ;
    }else {
     valorX1 =(long) apiObservado;
     valorX2 =(long) apiObservado + 0.5 ;
    }
    filtroEspecial = " ("+   Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA + "= " +temperaturaProbeta  ;
    filtroEspecial = filtroEspecial + " AND ";
    filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_API_OBSERVADO +  "=" + valorX1+") ";    
    filtroEspecial = filtroEspecial + " OR ";
    filtroEspecial = filtroEspecial + " ("+   Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA + "= " + temperaturaProbeta;
    filtroEspecial = filtroEspecial + " AND ";
    filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_API_OBSERVADO +  "=" + valorX2+") ";
    parametros.setPaginacion(Constante.SIN_PAGINACION);
    parametros.setFiltroEspecialCombinacion(filtroEspecial);
    filtroEspecialOrdenamiento=Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA+ " ASC, "+Tabla5BDao.CAMPO_API_OBSERVADO + " ASC ";
    parametros.setFiltroEspecialOrdenamiento(filtroEspecialOrdenamiento);
    respuesta = dTabla5B.recuperarRegistros(parametros);
    if (respuesta.estado==false){
     throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
    }
    if (respuesta.contenido.carga.size()<2){
     throw new Exception(gestorDiccionario.getMessage("sgo.errorValoresFueraRango", null, locale));
    }
    eTabla5B1 = (Tabla5B) respuesta.contenido.carga.get(0);
    eTabla5B2 = (Tabla5B) respuesta.contenido.carga.get(1);
    Y1 =  eTabla5B1.getApiCorregido();
    Y2 =  eTabla5B2.getApiCorregido();
    apiCorregido = Formula.calcularInterpolacion(valorX1, valorX2, Y1, Y2,apiObservado); 
  }else if ((apiExacto==false)&&(temperaturaExacto==false)) {
   if (decimalApiObservado > 0.5){
    valorXA1 = (long) apiObservado + 0.5;
    valorXA2 =(long) apiObservado +1 ;
   }else {
    valorXA1 =(long) apiObservado;
    valorXA2 =(long) apiObservado + 0.5 ;
   }
   //Se interpola la temperatura proque se utiliza el valor valorXA1
   if (decimalTemperatura > 0.5){
    valorX1 = (long) temperaturaProbeta + 0.5;
    valorX2 =(long) temperaturaProbeta +1 ;
   }else {
    valorX1 =(long) temperaturaProbeta;
    valorX2 =(long) temperaturaProbeta + 0.5 ;
   }
   
   filtroEspecial = " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " +valorXA1  ;
   filtroEspecial = filtroEspecial + " AND ";
   filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX1+") ";    
   filtroEspecial = filtroEspecial + " OR ";
   filtroEspecial = filtroEspecial + " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " + valorXA1;
   filtroEspecial = filtroEspecial + " AND ";
   filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX2+") ";
   
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEspecialCombinacion(filtroEspecial);
   filtroEspecialOrdenamiento=Tabla5BDao.CAMPO_API_OBSERVADO + " ASC ," + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA+ " ASC";
   parametros.setFiltroEspecialOrdenamiento(filtroEspecialOrdenamiento);
   respuesta = dTabla5B.recuperarRegistros(parametros);
   if (respuesta.estado==false){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if (respuesta.contenido.carga.size()<2){
    throw new Exception(gestorDiccionario.getMessage("sgo.errorValoresFueraRango", null, locale));
   }
   eTabla5B1 = (Tabla5B) respuesta.contenido.carga.get(0);
   eTabla5B2 = (Tabla5B) respuesta.contenido.carga.get(1);
   Y1 =  eTabla5B1.getApiCorregido();
   Y2 =  eTabla5B2.getApiCorregido();
   valorYA1 = Formula.calcularInterpolacion(valorX1, valorX2, Y1, Y2,temperaturaProbeta);
   //
   if (decimalTemperatura > 0.5){
    valorX1 = (long) temperaturaProbeta + 0.5;
    valorX2 =(long) temperaturaProbeta +1 ;
   }else {
    valorX1 =(long) temperaturaProbeta;
    valorX2 =(long) temperaturaProbeta + 0.5 ;
   }   
   filtroEspecial = " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " +valorXA2  ;
   filtroEspecial = filtroEspecial + " AND ";
   filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX1+") ";    
   filtroEspecial = filtroEspecial + " OR ";
   filtroEspecial = filtroEspecial + " ("+   Tabla5BDao.CAMPO_API_OBSERVADO + "= " + valorXA2;
   filtroEspecial = filtroEspecial + " AND ";
   filtroEspecial = filtroEspecial + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA +  "=" + valorX2+") ";   
   parametros.setPaginacion(Constante.SIN_PAGINACION);
   parametros.setFiltroEspecialCombinacion(filtroEspecial);
   filtroEspecialOrdenamiento=Tabla5BDao.CAMPO_API_OBSERVADO + " ASC ," + Tabla5BDao.CAMPO_TEMPERATURA_OBSERVADA+ " ASC";
   parametros.setFiltroEspecialOrdenamiento(filtroEspecialOrdenamiento);
   respuesta = dTabla5B.recuperarRegistros(parametros);
   if ((respuesta.estado==false) ){
    throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido", null, locale));
   }
   if (respuesta.contenido.carga.size()!=2){
    throw new Exception(gestorDiccionario.getMessage("sgo.errorValoresFueraRango", null, locale));
   }
   eTabla5B1 = (Tabla5B) respuesta.contenido.carga.get(0);
   eTabla5B2 = (Tabla5B) respuesta.contenido.carga.get(1);
   Y1 =  eTabla5B1.getApiCorregido();
   Y2 =  eTabla5B2.getApiCorregido();
   valorYA2 = Formula.calcularInterpolacion(valorX1, valorX2, Y1, Y2,temperaturaProbeta);  
   apiCorregido= Formula.calcularInterpolacion(valorXA1, valorXA2, valorYA1, valorYA2,apiObservado);
 }
   
   apiCorregido = Formula.redondearDouble(apiCorregido,1);
   factorCorreccionVolumen = Formula.calcularFactorCorreccion( apiCorregido, temperaturaCentro);
   volumenCorregido = factorCorreccionVolumen * volumenObservado;
   
   volumenCorregido = (double)Math.round(volumenCorregido* 1000)/1000;
   eFormulaRespuesta =  new FormulaRespuesta();
   eFormulaRespuesta.setApiCorregido(apiCorregido);
   eFormulaRespuesta.setFactorCorreccion(factorCorreccionVolumen);
   eFormulaRespuesta.setVolumenCorregido(volumenCorregido);
   eFormulaRespuesta.setTolerancia(Formula.redondearDouble(eTolerancia.getPorcentajeActual(),2));
   eFormulaRespuesta.setTipoVolumen(eTolerancia.getTipoVolumen());
   contenido = new Contenido<FormulaRespuesta>();
   contenido.carga =  new ArrayList<FormulaRespuesta>();
   contenido.carga.add(eFormulaRespuesta);
   
   respuesta.contenido = contenido;
   respuesta.estado=true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarFormulaExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 @RequestMapping(value = URL_RECUPERAR_FACTOR_CORRECCION_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarFactorCorreccion(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  double volumenObservado=0;
  double volumenCorregido=0;
  double factorCorreccionVolumen=0; 
  double apiObservado=0;  
  double apiCorregido=0;
  double temperaturaCentro=0; 
  double temperaturaProbeta=0;  
  Contenido<FormulaRespuesta> contenido =null;
  FormulaRespuesta eFormulaRespuesta=null;
  try {
   principal = this.getCurrentUser();
   respuesta = new RespuestaCompuesta();
   parametros = new ParametrosListar();   
    if ((httpRequest.getParameter("apiCorregido") != null) && (esDouble(httpRequest.getParameter("apiCorregido")))) {
     apiCorregido = (Double.parseDouble(httpRequest.getParameter("apiCorregido")));
    } else {
     throw new Exception(gestorDiccionario.getMessage("sgo.errorApiCorregidoInvalido",null,locale));
    }
    
    if ((httpRequest.getParameter("temperatura") != null) && (esDouble(httpRequest.getParameter("temperatura")))) {
     temperaturaCentro = (Double.parseDouble(httpRequest.getParameter("temperatura")));
    } else {
     throw new Exception(gestorDiccionario.getMessage("sgo.errorTemperaturaInvalida",null,locale));
    }
   
   if (httpRequest.getParameter("volumenObservado") != null) {
    volumenObservado=(Double.parseDouble(httpRequest.getParameter("volumenObservado")));
   }   

   factorCorreccionVolumen = Formula.calcularFactorCorreccion( apiCorregido, temperaturaCentro);
   volumenCorregido = factorCorreccionVolumen * volumenObservado;
   volumenCorregido = (double)Math.round(volumenCorregido* 1000)/1000;
   eFormulaRespuesta =  new FormulaRespuesta();
   eFormulaRespuesta.setApiCorregido(apiCorregido);
   eFormulaRespuesta.setFactorCorreccion(factorCorreccionVolumen);
   eFormulaRespuesta.setVolumenCorregido(volumenCorregido);
   contenido = new Contenido<FormulaRespuesta>();
   contenido.carga =  new ArrayList<FormulaRespuesta>();
   contenido.carga.add(eFormulaRespuesta);   
   respuesta.contenido = contenido;
   respuesta.estado=true;
   respuesta.mensaje = gestorDiccionario.getMessage("sgo.recuperarFormulaExitoso", null, locale);
  } catch (Exception ex) {
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
 
 private AuthenticatedUserDetails getCurrentUser() {
  return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
 
 boolean esDouble(String str) {
  try {
      Double.parseDouble(str);
      return true;
  } catch (NumberFormatException e) {
      return false;
  }
}
 
}
