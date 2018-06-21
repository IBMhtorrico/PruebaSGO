package sgo.servicio;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sgo.entidad.Enlace;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.seguridad.AuthenticatedUserDetails;
import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class ResumenCierreControlador {
	/** Nombre de la clase. */
    private static final String sNombreClase = "ResumenCierreControlador";
 private static final String URL_LISTAR_COMPLETA = "/admin/cierre/listar";
 private static final String URL_LISTAR_RELATIVA = "/cierre/listar";
 
 @RequestMapping(value = URL_LISTAR_RELATIVA, method = RequestMethod.GET)
 public @ResponseBody
 RespuestaCompuesta recuperarRegistros(HttpServletRequest httpRequest, Locale locale) {
  RespuestaCompuesta respuesta = null;
  ParametrosListar parametros = null;
  AuthenticatedUserDetails principal = null;
  String mensajeRespuesta = "";
  try {
//   // Recuperar el usuario actual
//   principal = this.getCurrentUser();
//   // Recuperar el enlace de la accion
//   respuesta = dEnlace.recuperarRegistro(URL_LISTAR_COMPLETA);
//   if (respuesta.estado == false) {
//    mensajeRespuesta = gestorDiccionario.getMessage("sgo.accionNoHabilitada", null, locale);
//    throw new Exception(mensajeRespuesta);
//   }
//   Enlace eEnlace = (Enlace) respuesta.getContenido().getCarga().get(0);
//   // Verificar si cuenta con el permiso necesario
//   if (!principal.getRol().searchPermiso(eEnlace.getPermiso())) {
//    mensajeRespuesta = gestorDiccionario.getMessage("sgo.faltaPermiso", null, locale);
//    throw new Exception(mensajeRespuesta);
//   }
//   // Recuperar parametros
//   parametros = new ParametrosListar();
//   if (httpRequest.getParameter("paginacion") != null) {
//    parametros.setPaginacion(Integer.parseInt(httpRequest.getParameter("paginacion")));
//   }
//
//   if (httpRequest.getParameter("registrosxPagina") != null) {
//    parametros.setRegistrosxPagina(Integer.parseInt(httpRequest.getParameter("registrosxPagina")));
//   }
//
//   if (httpRequest.getParameter("inicioPagina") != null) {
//    parametros.setInicioPaginacion(Integer.parseInt(httpRequest.getParameter("inicioPagina")));
//   }
//
//   if (httpRequest.getParameter("campoOrdenamiento") != null) {
//    parametros.setCampoOrdenamiento((httpRequest.getParameter("campoOrdenamiento")));
//   }
//
//   if (httpRequest.getParameter("sentidoOrdenamiento") != null) {
//    parametros.setSentidoOrdenamiento((httpRequest.getParameter("sentidoOrdenamiento")));
//   }
//
//   if (httpRequest.getParameter("valorBuscado") != null) {
//    parametros.setValorBuscado((httpRequest.getParameter("valorBuscado")));
//   }
//
//   if (httpRequest.getParameter("txtFiltro") != null) {
//    parametros.setTxtFiltro((httpRequest.getParameter("txtFiltro")));
//   }
//
//   parametros.setFiltroEstado(Constante.FILTRO_TODOS);
//   if (httpRequest.getParameter("filtroEstado") != null) {
//    parametros.setFiltroEstado(Integer.parseInt(httpRequest.getParameter("filtroEstado")));
//   }
//
//   // Recuperar registros
//   respuesta = dCliente.recuperarRegistros(parametros);
//   respuesta.mensaje = gestorDiccionario.getMessage("sgo.listarExitoso", null, locale);
  } catch (Exception ex) {
   //ex.printStackTrace();
	  Utilidades.gestionaError(ex, sNombreClase, "recuperarRegistros");
   respuesta.estado = false;
   respuesta.contenido = null;
   respuesta.mensaje = ex.getMessage();
  }
  return respuesta;
 }
}
