<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Estacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<script>
var cacheDescarga={
	estaciones: {<%=(String)request.getAttribute("estaciones")%>},
	tanques:{<%=(String)request.getAttribute("tanques")%>}
};
</script>
<div class="content-wrapper">
  <section class="content-header">
    <h1 id="tituloPantalla"> 
    Registro de Descarga / 
    <small id="tituloSeccion">
		<%=mapaValores.get("TITULO_LISTADO_REGISTROS")%>
    </small>
    </h1>
  </section>
  <!-- Contenido principal -->
  <section class="content">
  <!-- El contenido debe incluirse aqui-->
    <div id="cntBanda" class="row">
      <div class="col-md-12">
        <div id="bandaInformacion" class="callout callout-info">
        	<%=mapaValores.get("MENSAJE_CARGANDO")%>
        </div>
      </div>
    </div>
    <div class="row" id="cntTablaDiaOperativo">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form">
              <div class="row">
                <div class="col-md-6">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label id="lblFiltroOperacion" class="etiqueta-titulo-horizontal">Cliente / Operación: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                    <select id="filtroOperacion" name="filtroOperacion" class="form-control input-sm" style="width:100%;">
                    <%
                    ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
                    String fechaActual = (String) request.getAttribute("fechaActual");                  
                    int numeroOperaciones = listaOperaciones.size();
                    int indiceOperaciones=0;
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    for(indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion = (Operacion)listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();
                    %>
                    <option <%=seleccionado%> data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=  eCliente.getNombreCorto().trim() + " / "+ eOperacion.getNombre().trim() %></option>
                    <%
                    seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label id="lblFiltroFechaPlanificada" class="etiqueta-titulo-horizontal">F. Planificada: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control input-sm" data-fecha-actual="<%=fechaActual%>" />
                  </div>
                </div>
                <div class="col-md-2">
                  <a id="btnFiltrarDiaOperativo" class="btn btn-sm btn-default col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
                </div>
              </div>
            </form>
          </div>        
          <div class="box-header">
            <div>
              <a id="btnDetalleDiaOperativo" class="btn btn-sm btn-default espaciado"><i class="fa fa-search"></i>  Detalle</a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaDiaOperativo" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>N#</th>
                <th>ID</th>
                <th>F. Planificada</th>
                <th>Tot. Asignados</th>
                <th>Tot. Descargados</th>
                <th>U. Actualizaci&oacute;n</th>
                <th>Usuario</th>
                <th>Estado</th>
                </tr>
              </thead>
            </table>
          </div>
          <div class="overlay" id="ocultaContenedorTablaDiaOperativo">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    
  <div class="row" id="cntDetalleDiaOperativo" style="display:none">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-header">
          <form id="frmFiltrarxEstacion" name="frmFiltrarxEstacion" class="form">
            <table>
              <thead>
                <tr>
                  <td style="width:15%;">
                  <label id="lblOperacionDetalleDiaOperativo" class="etiqueta-titulo-horizontal">Operación / Cliente : </label>
                  </td>
                  <td style="width:25%;">
                    <input id="cmpOperacionDetalleDiaOperativo" name="cmpOperacionDetalleDiaOperativo" type="text" class="form-control input-sm" value="" readonly />
                  </td>
                  <td style="width:15%; padding-left:10px;">
                    <label id="lblFechaPlanificacionDetalleDiaOperativo" class="etiqueta-titulo-horizontal">F.Planificación: </label>
                  </td>
                  <td style="width:15%;">
                    <input id="cmpFechaPlanificacionDetalleDiaOperativo" name="cmpFechaPlanificacionDetalleDiaOperativo" 
                    type="text" class="form-control input-sm" value="" readonly />
                  </td>
                  <td style="width:10%; padding-left:10px;">
                     <label id="lblFiltroEstacionDetalleDiaOperativo" class="etiqueta-titulo-horizontal">Estación: </label>
                  </td>
                  <td style="width:15%;">
                      <select id="filtroEstacionDetalleDiaOperativo" name="filtroEstacionDetalleDiaOperativo" class="form-control input-sm" >
                      <option value="1">No seleccionado</option>
                    </select>
                  </td>
                  <td style="width:5%;padding-left:10px;">
                    <a id="btnFiltrarCargaTanque" class="btn btn-sm btn-default espaciado col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
                  </td>
                </tr>
              </thead>
            </table>     
          </form>
        </div>
        <div class="box-header">
          <div>
          <a id="btnAgregarCarga" class="btn btn-sm btn-default espaciado"><i class="fa fa-plus"></i>  Agregar</a>
          <a id="btnModificarCarga" class="btn btn-sm btn-default disabled espaciado"><i class="fa fa-edit"></i>  Modificar</a>
          <a id="btnRegresarPrincipal" class="btn btn-sm btn-default espaciado"><i class="fa fa-edit"></i>  Regresar</a>
          </div>
        </div>
        <div class="box-body">
          <FONT COLOR=red><B><label>Listado de carga de tanques.</label></B></FONT>
          <table id="tablaCargaTanque" class="sgo-table table table-striped" style="width:100%;">
            <thead>
              <tr>
              <th>N#</th>
              <th>ID</th>
              <th style="width:15%;">Tanque</th>
              <th style="width:10%;">F.Inicial</th>
              <th style="width:7%;">A.Inicial</th>
              <th style="width:7%;">V.Inicial</th>
              <th style="width:10%;">F.Final</th>
              <th style="width:7%;">A.Final</th>
              <th style="width:7%;">V.Final</th>
              <th style="width:7%;">V.Recibido</th>
              <th style="width:10%;">Tipo.Tanque</th>
              <th style="width:0%;">idTanque</th>
              <th style="width:15%;">Producto</th>
              <th style="width:5%;">IdProducto</th>
              </tr>
            </thead>
          </table>
        </div>
        <div class="overlay" id="ocultaContenedorTablaCargaTanque">
            <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>
      <div class="box box-default">
        <div class="box-header">
        </div>
        <div class="box-header">
          <div>
          <a id="btnAgregarDescarga" class="btn btn-sm btn-default disabled espaciado"><i class="fa fa-plus"></i>  Agregar</a>
          <a id="btnModificarDescarga" class="btn btn-sm btn-default disabled espaciado"><i class="fa fa-edit"></i>  Modificar</a>
          <a id="btnAgregarEvento" class="btn btn-sm btn-default disabled espaciado"><i class="fa fa-eye"></i>  Evento</a>
          <a id="btnVerDescarga" class="btn btn-sm btn-default disabled espaciado"><i class="fa fa-search"></i>  Ver</a>
          </div>
        </div>
        <div class="box-body">
          <FONT COLOR=red><B><label>Listado de descarga de cisternas.</label></B></FONT>
          <table id="tablaDescargaCisterna" class="sgo-table table table-striped" style="width:100%;">
            <thead>
              <tr>
              <th>N#</th>
              <th>ID</th>
              <th>Cisterna</th>
              <th>Tracto</th>
              <th>N# Guia</th>
              <th>Despachado (60F)</th>
              <th>Recibido (60F)</th>
              <th>Variacion (60F)</th>
              <th>Estado</th>
              <th>IDTransporte</th>
              </tr>
            </thead>
          </table>
        </div>
        <div class="overlay" id="ocultaContenedorTablaDescarga">
            <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>      
    </div>
  </div>
  <!-- Aqui empieza el formulario -->
  <div class="row" id="cntFormularioCargaTanque" style="display:none">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-header">
        	<table>
        		<thead>
                <tr>
                	<td style="width:15%;">
                	<label id="lblOperacionCargaTanque" class="etiqueta-titulo-horizontal"> Cliente / Operación  : </label>
                	</td>
                	<td style="width:25%;">
                	<input id="cmpOperacionFormularioCargaTanque" name="cmpOperacionFormularioCargaTanque" type="text" class="form-control input-sm" value="Cliente / Operación " readonly />
                	</td>
                	<td style="width:5%; padding-left:10px;">
                		<label id="lblEstacionCargaTanque" class="etiqueta-titulo-horizontal">Estación: </label>
                	</td>
                	<td style="width:15%;">
                	 	<input id="cmpEstacionFormularioCargaTanque" name="cmpEstacionFormularioCargaTanque" type="text" class="form-control input-sm" value="" readonly />
                	</td>
                	<td style="width:5%;padding-left:10px;">
                		<label id="4" class="etiqueta-titulo-horizontal">Tanque: </label>
                	</td>
                	<td style="width:15%;">
						<select id="cmpTanqueFormularioCargaTanque" name="cmpTanqueFormularioCargaTanque" class="form-control input-sm" >
                      	<option value="1">No seleccionado</option>
                    	</select>
                	</td>
                	<td style="width:20%;padding-left:10px;">
                		<input id="cmpProductoFormularioCargaTanque" name="cmpProductoFormularioCargaTanque" type="text" class="form-control input-sm" disabled readonly />
                	</td>
                </tr>
                </thead>
        	</table>
        </div>
        <div class="box-body">
          <form id="frmAgregarCargaTanque" class="form form-horizontal">
          <table class="table table-condensed">
            <thead>
              <tr>
              <th class="text-center">Medida</th>
              <th class="text-center">Fecha/Hora</th>
              <th class="text-center">Altura</th>
              <th class="text-center">T.Centro</th>
              <th class="text-center">T. Probeta</th>
              <th class="text-center">API T. Obs.</th>
              <th class="text-center">Factor</th>
              <th class="text-center">Vol. T. Obs.</th>
              <th class="text-center">Vol. 60F</th>
              </tr>
            </thead>
            <tbody>
              <tr>
              <td class="celda-detalle" style="width: 8%"><input id="cmpMedidaInicial" name="cmpMedidaInicial" type="text" class="form-control text-center input-sm" value="Inicial" readonly="readonly" /></td>
              <td class="celda-detalle" style="width: 15%"><input id="cmpFechaHoraInicial" name="cmpFechaHoraInicial" type="text" class="form-control input-sm text-center" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpAlturaInicial" name="cmpAlturaInicial" type="text" class="form-control input-sm text-right" maxlength="5" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpTemperaturaInicialCentro" name="cmpTemperaturaInicialCentro" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpTemperaturaInicialProbeta" name="cmpTemperaturaInicialProbeta" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpAPIObservadoInicial" name="cmpAPIObservadoInicial" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpFactorInicial" name="cmpFactorInicial" readonly="readonly" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpVolumenInicialObservado" name="cmpVolumenInicialObservado"  type="text" class="form-control input-sm text-right"  maxlength="9" value="" /></td>
              <td class="celda-detalle" style="width: 10%"><input id="cmpVolumenInicialCorregido" name="cmpVolumenInicialCorregido" readonly="readonly" type="text" class="form-control input-sm text-right"  maxlength="9" value="" /></td>
              </tr>
              <tr>
              <td class="celda-detalle"><input id="cmpMedidaFinal" name="cmpMedidaFinal" type="text" class="form-control input-sm text-center" value="Final" readonly="readonly" /></td>
              <td class="celda-detalle"><input id="cmpFechaHoraFinal" name="cmpFechaHoraFinal" type="text" class="form-control input-sm text-center" value="" /></td>
              <td class="celda-detalle"><input id="cmpAlturaFinal" name="cmpAlturaFinal" type="text" class="form-control input-sm text-right" maxlength="5" value="" /></td>
              <td class="celda-detalle"><input id="cmpTemperaturaFinalCentro" name="cmpTemperaturaFinalCentro" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle"><input id="cmpTemperaturaFinalProbeta" name="cmpTemperaturaFinalProbeta" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle"><input id="cmpAPIObservadoFinal" name="cmpAPIObservadoFinal" type="text" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle"><input id="cmpFactorFinal" name="cmpFactorFinal" type="text" readonly="readonly" class="form-control input-sm text-right" value="" /></td>
              <td class="celda-detalle"><input id="cmpVolumenFinalObservado" name="cmpVolumenFinalObservado" type="text" class="form-control input-sm text-right" maxlength="9" value="" /></td>
              <td class="celda-detalle"><input id="cmpVolumenFinalCorregido" name="cmpVolumenFinalCorregido" readonly="readonly" type="text" class="form-control input-sm text-right" maxlength="9" value="" /></td>
              </tr>
            </tbody>
          </table>
          </form>
        </div>
        <div class="box-footer">
          <a id="btnGuardarCarga" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>  Guardar</a>
          <a id="btnCancelarGuardarCarga" class="btn btn-sm btn-danger"><i class="fa fa-close"></i>  Cancelar</a>
        </div>
        <div class="overlay" id="ocultaContenedorFormularioCargaTanque" style="display:none">
          <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>
    </div>
  </div>
  <div class="row" id="cntFormularioDescarga" style="display:none">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-body">
        
      <div id="cntCabeceraDescarga" class="box box-default">
        <div class="box-body">
        <form id="frmAgregarDescarga" class="form form-horizontal">
          <table style="width: 100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <tr>           
              <td style="width: 9%" class="celda-titulo"> Operación 	</td>
              <td style="width: 20%">
                <input id="cmpOperacionFormularioDescarga" name="cmpOperacionFormularioDescarga" type="text" class="form-control input-sm" value="OPERACION " readonly />
              </td>
              <td style="width: 6%;padding-left:10px;" class="celda-titulo"> Estación </td>
              <td style="width: 12%">
                <input id="cmpEstacionFormularioDescarga" name="cmpEstacionFormularioDescarga" type="text" class="form-control input-sm" value="ESTACION " readonly />
              </td>
              <td style="width: 5%;padding-left:10px;" class="celda-titulo"> Tanque  </td>                
              <td style="width: 13%">
                <input id="cmpTanqueFormularioDescarga" name="cmpTanqueFormularioDescarga" type="text" class="form-control input-sm" value="TANQUE 1 " readonly />
                </td>
              <td style="width: 20%">  
                <input id="cmpProductoTanqueFormularioDescarga" name="cmpProductoTanqueFormularioDescarga" type="text" class="form-control input-sm" value="PRODUCTO XXX " readonly />
              </td>
              <td style="width: 5%;padding-left:10px;" class="celda-titulo"> Método </td>
              <td style="width: 10%">
                <input id="cmpMetodoFormularioDescarga" name="cmpMetodoFormularioDescarga" type="text" class="form-control input-sm" value="WINCHA " readonly />
              </td> 
            </tr>
            <tr style="margin-top:6px;">
              <td class="celda-titulo"> Seleccionar Cisterna </td>
              <td colspan="8">
                <select tipo-control="select2" id="cmpBuscarCisternaFormularioDescarga" name="cmpBuscarCisternaFormularioDescarga" class="form-control input-sm" style="width:100%" >
                 <option value="0" selected="selected">Seleccionar</option>
                </select>
              </td>
            </tr>
          </table>
        </form>
        </div>
      </div>
      
      <div id="cntDetalleTransporte" class="box box-default">
        <div class="box-body">
          <table style="width: 100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <tr style="margin-top:6px;">
              <td style="width: 14%" class="celda-titulo"> 
              G/R
              </td>
              <td style="width: 15%">
                <input id="cmpGRFormularioDescarga" name="cmpGRFormularioDescarga" type="text" class="form-control input-sm" value="007-0021697 " readonly />
              </td>
              <td style="width: 9%;padding-left:10px;" class="celda-titulo">
                O/E
              </td>
              <td style="width: 12%">
                <input id="cmpOEFormularioDescarga" name="cmpOEFormularioDescarga" type="text" class="form-control input-sm" value="007-0021697" readonly />
              </td>
              <td style="width: 14%;padding-left:10px;" class="celda-titulo">
                Cisterna/Tracto
              </td>                
              <td style="width: 12%">
                <input id="cmpCisternaFormularioDescarga" name="cmpCisternaFormularioDescarga" type="text" class="form-control input-sm" value="ZD-2867/V2I-871" readonly />
              </td>
              <td style="width: 10%;padding-left:10px;" class="celda-titulo">
                Vol. Obs.
              </td>
              <td style="width: 12%">
                <input id="cmpVolumenObservadoDespachado" name="cmpVolumenObservadoDespachado" type="text" class="form-control input-sm text-right" value="9000 " readonly />
              </td> 
            </tr>
            <tr style="margin-top:6px;">
              <td class="celda-titulo"> F. Emisión O/E
              </td>
              <td>
                <input id="cmpFEmisionOEFormularioDescarga" name="cmpFEmisionOEFormularioDescarga" type="text" class="form-control input-sm text-center" value="12/12/2015 " readonly />
              </td>
              <td style="padding-left:10px;" class="celda-titulo">
                F. Arribo
              </td>
              <td>
                <input id="cmpFArriboFormularioDescarga" name="cmpFArriboFormularioDescarga" type="text" class="form-control input-sm text-center" value="12/12/2015"  />
              </td>
              <td style="padding-left:10px;" class="celda-titulo">
                F. Fiscalización
              </td>                
              <td>
                <input id="cmpFFiscalizacionFormularioDescarga" name="cmpFFiscalizacionFormularioDescarga" type="text" class="form-control input-sm text-center" value="12/12/2015"  />
              </td>
              <td style="padding-left:10px;" class="celda-titulo">
                F. Descarga
              </td>
              <td>
                <input id="cmpFDescargaFormularioDescarga" name="cmpFDescargaFormularioDescarga" type="text" class="form-control input-sm text-center" value="12/12/2015" readonly />
              </td> 
            </tr>
          </table>
          <table id="tablaCompartimentoDespacho" style="width:100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <thead>
            <tr> 
              <td colspan="2" style="width:10%;" class="celda-titulo-vertical text-center">Compartimento</td>
              <td style="width:30%;" class="celda-titulo-vertical text-center">Producto</td>
              <td style="width:15%;" class="celda-titulo-vertical text-center">Vol. T Obs</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">Temperatura</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">API 60° F</td>
              <td style="width:15%;" class="celda-titulo-vertical text-center">Factor</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">Vol. 60° F</td>
            </tr>
            </thead>
            <tbody>
            <tr id="plantillaDetalleTransporte">
              <td style="width:5%;">
                <a id="copiarDetalle" data-fila="0" class="btn btn-default btn-sm" style="width:100%;">
                <i class="glyphicon glyphicon-plus"></i>
                </a>
              </td>
              <td>
              <input id="cmpComDespacho" name="cmpComDespacho" type="text" class="form-control input-sm text-center" value="1" readonly />
              </td>
              <td>
              <input id="cmpProDespacho" name="cmpProDespacho" type="text" class="form-control input-sm text-left" value="BIODIESEL 100" readonly />
              </td>
              <td>
              <input id="cmpVolObsDespacho" name="cmpProDespacho" type="text" class="form-control input-sm text-right" value="9000" readonly />
              </td>
              <td>
              <input id="cmpTemDespacho" name="cmpTemDespacho" type="text" class="form-control input-sm text-right" value="45.7" readonly />
              </td>
              <td>
              <input id="cmpAPIDespacho" name="cmpAPIDespacho" type="text" class="form-control input-sm text-right" value="45.7" readonly />
              </td>
              <td>
              <input id="cmpFacDespacho" name="cmpFacDespacho" type="text" class="form-control input-sm text-right" value="0.789456" readonly />
              </td>
              <td>
              <input id="cmpVolCorDespacho" name="cmpVolCorDespacho" type="text" class="form-control input-sm text-right" value="8700" readonly />
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      
      <div id="cntDetalleDescarga" class="box box-default">
        <div class="box-body">
          <table id="tablaCompartimentoDescarga" style="width:100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <thead>
            <tr > 
              <td colspan="2" style="width:10%;" class="celda-titulo-vertical text-center">Compartimento</td>
              <td style="width:24%;" class="celda-titulo-vertical text-center">Producto</td>
              <td style="width:6%;" class="celda-titulo-vertical text-center">Flecha T.C.</td>
              <td style="width:6%;" class="celda-titulo-vertical text-center">Flecha Medida</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">Altura</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">T. Centro</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">T. Probeta</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">API T. Obs.</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">Factor</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">Vol. T. Obs.</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">Vol. 60° F.</td>
            </tr>
            </thead>
            <tbody>
            <tr id="plantillaDetalleDescarga">
              <td style="width:5%;">
                <a id="eliminarDetalle" data-fila="0" class="btn btn-default btn-sm" style="width:100%;">
                <i class="glyphicon glyphicon-remove"></i>
                </a>
              </td>
              <td>
              <input id="cmpComRecepcion" name="cmpComRecepcion" type="text" class="form-control input-sm text-center" value="1" readonly/>
              </td>
              <td>
              <input id="cmpProRecepcion" name="cmpProRecepcion" type="text" class="form-control input-sm text-left" value="BIODIESEL 100" readonly />
              </td>
              <td>
              <input id="cmpNivelFlechaDespacho" name="cmpNivelFlechaDespacho" type="text" class="form-control input-sm text-right" readonly value="0" />
              </td>
              <td>
              <input id="cmpNivelFlechaDescarga" name="cmpNivelFlechaDescarga" type="text" class="form-control input-sm text-right" maxlength="5" value="0" />
              </td>
              <td>
              <input id="cmpAlturaRecepcion" name="cmpAlturaRecepcion" type="text" class="form-control input-sm text-right" maxlength="5" value="9000" />
              </td>
              <td>
              <input id="cmpTemCentroRecepcion" name="cmpTemCentroRecepcion" type="text" class="form-control input-sm text-right" maxlength="4" value="45.7" />
              </td>
              <td>
              <input id="cmpTempProbetaRecepcion" name="cmpTempProbetaRecepcion" type="text" class="form-control input-sm text-right" maxlength="4" value="45.7" />
              </td>
              <td>
              <input id="cmpAPIObsRecepcion" name="cmpAPIObsRecepcion" type="text" class="form-control input-sm text-right" maxlength="4" value="45.89" />
              </td>
              <td>
              <input id="cmpFactorRecepcion" name="cmpFactorRecepcion" type="text" class="form-control input-sm text-right" value="0.789456" readonly/>
              </td>
              <td>
              <input id="cmpVolObsRecepcion" name="cmpVolObsRecepcion" type="text" class="form-control input-sm text-right" value="0.789456" readonly/>
              </td>
              <td>
              <input id="cmpVolCorRecepcion" name="cmpVolCorRecepcion" type="text" class="form-control input-sm text-right" value="0.789456" readonly/>
              </td>
            </tr>
            </tbody> 
          </table>
          <table id="tablaTotalDescarga" style="width:100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <tbody>
              <tr> 
              <td colspan="8" style="width:80%;padding-right:10px;" class="celda-titulo-vertical text-right">Vol. Total Recibido Observado y Corregido (gal)</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">
              <input id="cmpVolTotObsRecepcion" name="cmpVolTotObsRecepcion" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">
              <input id="cmpVolTotCorRecepcion" name="cmpVolTotCorRecepcion" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
              </tr>  
            </tbody>
          </table>
        </div>
      </div>
      
      <div class="box box-default" id="cntMetodoPesaje" style="display:block">
        <div class="box-body">
          <table id="tablaPesaje" style="width: 100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <tr>           
              <td style="width: 9%" class="celda-titulo"> Pesaje Inicial
              </td>
              <td style="width: 8%">
                <input id="cmpPesajeInicial" name="cmpPesajeInicial" type="text" class="form-control input-sm text-right" maxlength="10" value="0" />
              </td>
              <td style="width: 9%;padding-left:10px;" class="celda-titulo">
                Pesaje Final
              </td>
              <td style="width: 8%">
                <input id="cmpPesajeFinal" name="cmpPesajeFinal" type="text" class="form-control input-sm text-right" maxlength="10" value="0 " />
              </td>
              <td style="width: 9%;padding-left:10px;" class="celda-titulo">
                Peso Neto
              </td>                
              <td style="width: 8%">
                <input id="cmpPesoNeto" name="cmpPesoNeto" type="text" class="form-control input-sm text-right" value="0" readonly />
              </td>       
               <td style="width: 10%;padding-left:10px;" class="celda-titulo">
                Factor (Tabla 13)
              </td>
              <td style="width: 8%">
                <input id="cmpFactorPesaje" name="cmpFactorPesaje" type="text" class="form-control input-sm text-right" value="0" readonly />
              </td>
              <td style="width: 10%;padding-left:10px;" class="celda-titulo">
                Vol. Recibido 60F
              </td>                
              <td style="width: 8%">
                <input id="cmpVolPesaje" name="cmpVolPesaje" type="text" class="form-control input-sm text-right" value="0" readonly />
              </td>
            </tr>
          </table>
        </div>
      </div>
      
      <div class="box box-default" id="cntMetodoContometro" style="display:block">
        <div class="box-body">
          <table id="tablaContometro" style="width: 100%;border:0px;border-spacing: 2px;border-collapse:separate;">
            <tr>           
              <td style="width: 12%;padding-left:10px;" class="celda-titulo">
                L. Inicial :
              </td>
              <td style="width: 12%">
                <input id="cmpLecturaInicial" name="cmpLecturaInicial" type="text" class="form-control input-sm text-right" value="0" />
              </td>
              <td style="width: 12%;padding-left:10px;" class="celda-titulo">
                L. Final :
              </td>                
              <td style="width: 12%" >
                <input id="cmpLecturaFinal" name="cmpLecturaFinal" type="text" class="form-control input-sm text-right" value="0" />
              </td>
              <td style="width: 12%;padding-left:10px;" class="celda-titulo">
               Vol. Obs. 
              </td>                
              <td style="width: 12%">
                <input id="cmpVolumenContometro" name="cmpVolumenContometro" type="text" class="form-control input-sm text-right" value="0" readonly />
              </td>
            </tr>
          </table>
        </div>
      </div>
      
       <div class="box box-default" id="cntDescargaComparacion">
        <div class="box-body">
          <table id="tablaDescargaComparacion" style="border:0px;border-spacing: 2px;border-collapse:separate;">
            <tr>           
              <td style="width: 20%" class="celda-titulo"> 
              <span id="etiquetaVolDespachadoTotal">Vol. Despachado Corregido (gal)</span>
              </td>
              <td style="width: 10%">
                <input id="cmpVolumenDespachadoCorregido" name="cmpVolumenDespachadoCorregido" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
              <td style="width: 20%;padding-left:10px;" class="celda-titulo">                
                <span id="etiquetaVolRecibidoTotal">Vol. Recibido Corregido (gal)</span>
              </td>
              <td style="width: 10%;">
                <input id="cmpVolumenRecibidoCorregido" name="cmpVolumenRecibidoCorregido" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
              <td style="width: 20%;padding-left:10px;" class="celda-titulo">
                Variación 60F (gal)
              </td>                
              <td style="width: 10%">
                <input id="cmpVariacionFinal" name="(gal)" type="text" class="form-control input-sm text-right" value="0" readonly />
              </td>
            </tr>
            <tr>           
              <td class="celda-titulo"> 
              Limite Permisible (gal)
              </td>
              <td>
                <input id="cmpMermaPermisible" name="cmpMermaPermisible" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
              <td style="padding-left:10px;" class="celda-titulo">
                Vol. Faltante T. Obs. (gal)
              </td>
              <td>
                <input id="cmpVolumenExcedenteObservado" name="cmpVolumenExcedenteObservado" type="text" class="form-control input-sm text-right" value="0 " readonly/>
              </td>
              <td style="padding-left:10px;" class="celda-titulo">
                Vol. Faltante 60 F (gal)
              </td>                
              <td>
                <input  id="cmpVolumenExcedenteCorregido" name="cmpVolumenExcedenteCorregido" type="text" class="form-control input-sm text-right" value="0" readonly/>
              </td>
            </tr>
          </table>
        </div>
      </div>
        <div class="box box-default" data-visible="0" id="cntObservacionesDescarga" style="display:none">
          <div class="box-body">
            <textarea data-id="0" id="cmpDescargaObservacion" name="cmpDescargaObservacion" class="form-control" rows="3" placeholder="Ingresar observaciones"></textarea>
          </div>
        </div>
      </div>
        <div class="box-footer">
            <a id="btnGuardarDescarga" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnMostrarObservaciones" class="btn bg-yellow btn-sm"><i class="fa fa-eye"></i>  <%=mapaValores.get("ETIQUETA_BOTON_OBSERVACIONES")%></a>
            <button id="btnAutorizarCambioVolumen" type="submit" class="btn bg-purple btn-sm"> <i class="fa fa-save"></i> Autorizar</button>
            <a id="btnCancelarGuardarDescarga" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
        </div>       
        
        <div id="frmValidarAutorizacion" class="modal" data-keyboard="false" data-backdrop="static">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
              <h4 class="modal-title">Ingreso de Autorizaci&oacute;n</h4>
              </div>
              <div class="modal-body">
                <table class="sgo-simple-table table table-condensed"  style="width:100%;">
                <tbody>
                <tr>
                <td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Autorización: </label>					</td>
                <td class="celda-detalle"> <input name="cmpDescAutorizacion" id="cmpDescAutorizacion" type="text" class="form-control text-uppercase form-control espaciado input-sm" maxlength="15" disabled placeholder="" value="<%=mapaValores.get("AUTORIZACION_DESCARGA")%>"/>	</td>
                </tr>
                <tr>
                <td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Aprobador: </label>					</td>
                <td class="celda-detalle"> <select tipo-control="select2" id="cmpAprobador" name="cmpAprobador" class="form-control text-uppercase form-control espaciado input-sm" style="width: 100%">
                <option value="" selected="selected"><%=mapaValores.get("TEXTO_ELEMENTO_SELECCIONAR")%></option>
                </select>
                </td>
                </tr>
                </tbody>
                </table>
                <table class="sgo-simple-table table table-condensed"  style="width:100%;">
                <tbody>
                <tr>
                <td class="celda-detalle" style="width:25%;"> <label class="etiqueta-titulo-horizontal">C&oacute;digo de Autorizacion: </label>					</td>
                <td class="celda-detalle"> <input id="cmpCodigoValidacion" name="cmpCodigoValidacion" type="password" class="form-control espaciado input-sm"/> </td>
                <td style="width:5%;">
                <td class="celda-detalle" style="width:15%;"> <label class="etiqueta-titulo-horizontal">Vigencia hasta: </label>					</td>
                <td class="celda-detalle"> <input id="cmpVigenciaHastaValidacion" name="cmpVigenciaHastaValidacion" type="text" class="form-control espaciado input-sm" disabled required data-inputmask="'alias': 'dd/mm/yyyy'" placeholder="" /> </td>
                </tr>
                <tr>
                <td class="celda-detalle" colspan="5"><label class="etiqueta-titulo-horizontal">Justificaci&oacute;n:</label></td>
                </tr>
                <tr>
                <td class="celda-detalle" colspan="5"><textarea class="form-control text-uppercase espaciado input-sm" required id="cmpJustificacion" name="cmpJustificacion"  placeholder="Ingrese justificaci&oacute;n..." rows="5"></textarea></td>
                </tr>
                </tbody>
                </table>
              </div>
              <div class="modal-footer">
                <button id="btnGuardarValidarAutorizacion" class="btn btn-sm btn-default pull-left" type="button"><%=mapaValores.get("ETIQUETA_BOTON_VALIDAR")%></button>
                <button id="btnCancelarValidarAutorizacion"  class="btn btn-sm btn-default  pull-rigth" type="button"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
              </div>
            </div>
          </div>
        </div>
        <div class="overlay" id="ocultaFormularioDescarga" style="display:none">
          <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>
    </div>
  </div>
  
  <div class="row" id="cntVistaFormularioDescarga" style="display:none">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-body">        
          <div id="cntVistaCabeceraDescarga">
              <table style="width: 100%;border:1px solid;border-spacing: 0px;border-collapse:collapse;" class="sgo-table table">
                <tr>           
                  <td class="tabla-vista-titulo" style="width: 14%"> Operación
                  </td>
                  <td style="width: 15%">
                    <span id="vistaOperacionFormularioDescarga" ></span>
                  </td>
                  <td class="tabla-vista-titulo" style="width: 9%;padding-left:10px;">
                    Estación
                  </td>
                  <td style="width: 12%">
                    <span id="vistaEstacionFormularioDescarga" ></span>
                  </td>
                  <td class="tabla-vista-titulo" style="width: 14%;padding-left:10px;">
                    Tanque
                  </td>                
                  <td style="width: 12%">
                    <span id="vistaTanqueFormularioDescarga" ></span>
                  </td>
                  <td class="tabla-vista-titulo" style="width: 10%;padding-left:10px;">
                    Método
                  </td>
                  <td style="width: 12%">
                    <span id="vistaMetodoFormularioDescarga" ></span>
                  </td> 
                </tr>
              </table>
          </div>      
          <div id="cntVistaDetalleTransporte">
              <table style="width: 100%;border:1px solid;border-spacing: 0px;border-collapse:collapse;" class="sgo-table table">
                <tr style="margin-top:6px;">
                  <td style="width: 14%" class="tabla-vista-titulo"> 
                  G/R
                  </td>
                  <td style="width: 15%">
                    <span id="vistaGRFormularioDescarga"></span>
                  </td>
                  <td style="width: 9%;padding-left:10px;" class="tabla-vista-titulo">
                    O/E
                  </td>
                  <td style="width: 12%">
                    <span id="vistaOEFormularioDescarga"></span>
                  </td>
                  <td style="width: 14%;padding-left:10px;" class="tabla-vista-titulo">
                    Cisterna/Tracto
                  </td>                
                  <td style="width: 12%">
                    <span id="vistaCisternaFormularioDescarga"></span>
                  </td>
                  <td style="width: 10%;padding-left:10px;" class="tabla-vista-titulo">
                    Vol. Obs.
                  </td>
                  <td style="width: 12%">
                    <span id="vistaVolumenObservadoDespachado" ></span>
                  </td> 
                </tr>
                <tr style="margin-top:6px;">
                  <td class="tabla-vista-titulo"> 
                  F. Emisión O/E
                  </td>
                  <td>
                    <span id="vistaFEmisionOEFormularioDescarga" ></span>
                  </td>
                  <td style="padding-left:10px;" class="tabla-vista-titulo">
                    F. Arribo
                  </td>
                  <td>
                    <span id="vistaFArriboFormularioDescarga"></span>
                  </td>
                  <td style="padding-left:10px;" class="tabla-vista-titulo">
                    F. Fiscalización
                  </td>                
                  <td>
                    <span id="vistaFFiscalizacionFormularioDescarga"></span>
                  </td>
                  <td style="padding-left:10px;" class="tabla-vista-titulo">
                    F. Descarga
                  </td>
                  <td>
                    <span id="vistaFDescargaFormularioDescarga" ></span>
                  </td> 
                </tr>
              </table>
              
          
	         <table style="width: 100%;border:1px solid;border-spacing: 0px;border-collapse:collapse;" class="sgo-table table">
                <tr style="margin-top:6px;">
					<td class="tabla-vista-titulo" style="width:10%;">Creado el:			</td>
					<td class="text-center" style="width:23%;"> <span id='vistaCreadoEl'></span>		</td>
					<td class="tabla-vista-titulo" style="width:10%;">Creado por:	</td>
					<td class="text-left" style="width:23%;"> <span id='vistaCreadoPor'></span>	</td>
					<td class="tabla-vista-titulo" style="width:10%;">IP Creaci&oacute;n:	</td>
					<td class="text-left"> <span id='vistaIpCreacion'></span>		</td>
				</tr>
				<tr style="margin-top:6px;">
					<td class="tabla-vista-titulo" style="width:10%;">Actualizado el:			</td>
					<td class="text-center" style="width:23%;"> <span id='vistaActualizadoEl'></span>		</td>
					<td class="tabla-vista-titulo" style="width:10%;">Actualizado por:	</td>
					<td class="text-left" style="width:23%;"> <span id='vistaActualizadoPor'></span>	</td>
					<td class="tabla-vista-titulo" style="width:10%;">IP Actualizaci&oacute;n:	</td>
					<td class="text-left"> <span id='vistaIpActualizacion'></span>		</td>
				</tr>
			  </table>
          
              <table id="tablaVistaCompartimentoDespacho" class="sgo-table table table-striped" style="display:none;margin-top:5px;width:100%;border:1px solid;border-spacing: 0px;border-collapse:collapse;">
                <thead>
                <tr> 
                  <td style="width:10%;" class="celda-titulo-vertical text-center">Compartimento</td>
                  <td style="width:30%;" class="celda-titulo-vertical text-center">Producto</td>
                  <td style="width:15%;" class="celda-titulo-vertical text-center">Vol. T Obs</td>
                  <td style="width:10%;" class="celda-titulo-vertical text-center">Temperatura</td>
                  <td style="width:10%;" class="celda-titulo-vertical text-center">API 60° F</td>
                  <td style="width:15%;" class="celda-titulo-vertical text-center">Factor</td>
                  <td style="width:10%;" class="celda-titulo-vertical text-center">Vol. 60° F</td>
                </tr>
                </thead>
                <tbody>
                <tr id="plantillaVistaDetalleTransporte">
                  <td class="text-center">
                  <span id="vistaComDespacho"  ></span>
                  </td>
                  <td class="text-left">
                  <span id="vistaProDespacho"  ></span>
                  </td>
                  <td class="text-right">
                  <span id="vistaVolObsDespacho"  ></span>
                  </td>
                  <td class="text-right">
                  <span id="vistaTemDespacho" ></span>
                  </td>
                  <td class="text-right">
                  <span id="vistaAPIDespacho"  ></span>
                  </td>
                  <td class="text-right">
                  <span id="vistaFacDespacho"  ></span>
                  </td>
                  <td class="text-right">
                  <span id="vistaVolCorDespacho"  ></span>
                  </td>
                </tr>
                </tbody>
              </table>
          </div>      
      <div id="cntVistaDetalleDescarga" >
          <table id="tablaVistaCompartimentoDescarga" class="sgo-table table table-striped" style="width:100%;border:1px solid;border-spacing: 0px;border-collapse:collapse;">
            <thead>
            <tr> 
              <td style="width:5%;" class="celda-titulo-vertical text-center">Comp.</td>
              <td style="width:23%;" class="celda-titulo-vertical text-center">Producto</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">Flecha T.C</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">Flecha Med.</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">Altura</td>
              <td style="width:7%;" class="celda-titulo-vertical text-center">T. Centro</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">T. Probeta</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">API T. Obs.</td>
              <td style="width:8%;" class="celda-titulo-vertical text-center">Factor</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">Vol. T. Obs.</td>
              <td style="width:10%;" class="celda-titulo-vertical text-center">Vol. 60° F.</td>
            </tr>
            </thead>
            <tfoot>
              <tr> 
              <td colspan="7" style="width:84%;padding-right:10px;" class="celda-titulo-vertical text-right">
              Vol. Total Recibido Observado y 60°F (gal)
              </td>
              <td style="width:8%;" class="celda-titulo-vertical text-right">
              <span id="vistaVolTotObsRecepcion"  ></span>
              </td>
              <td style="width:8%;" class="celda-titulo-vertical text-right">
              <span id="vistaVolTotCorRecepcion"></span>
              </td>
              </tr> 
            </tfoot>
            <tbody>
            <tr id="plantillaVistaDetalleDescarga">
              <td class="text-center">
              <span id="vistaComRecepcion"  ></span>
              </td>
              <td class="text-left">
              <span id="vistaProRecepcion"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaAlturaTC"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaAlturaFlecha"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaAlturaRecepcion"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaTemCentroRecepcion"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaTempProbetaRecepcion"  ></span>
              </td>
              <td class="text-right">
              <span id="vistaAPIObsRecepcion"></span>
              </td>
              <td class="text-right">
              <span id="vistaFactorRecepcion"  ></span>
              </td>
              <td  class="text-right">
              <span id="vistaVolObsRecepcion" ></span>
              </td>
              <td class="text-right">
              <span id="vistaVolCorRecepcion"  ></span>
              </td>
            </tr>
            </tbody>

          </table>
      </div>      
      <table id="cntVistaMetodoPesaje" class="sgo-table table" style="border:1px solid;border-spacing: 0px;border-collapse:collapse;">
        <tr>           
          <td style="width: 10%" class="tabla-vista-titulo"> 
          Pesaje Inicial
          </td>
          <td style="width: 9%"  class="text-right">
            <span id="vistaPesajeInicial" ></span>
          </td>
          <td style="width: 12%;padding-left:10px;" class="tabla-vista-titulo">
            Pesaje Final
          </td>
          <td style="width: 9%" class="text-right">
            <span id="vistaPesajeFinal"  ></span>
          </td>
          <td style="width: 10%;padding-left:10px;" class="tabla-vista-titulo">
            Peso Neto
          </td>                
          <td style="width: 8%" class="text-right">
            <span id="vistaPesoNeto"  ></span>
          </td>         
          <td style="width: 14%;padding-left:10px;" class="tabla-vista-titulo">
            Factor (Tabla 13)
          </td>
          <td style="width: 8%" class="text-right">
            <span id="vistaFactorPesaje"></span>
          </td>
          <td style="width: 14%;padding-left:10px;" class="tabla-vista-titulo">
            Vol. Recibido 60F
          </td>                
          <td style="width: 10%" class="text-right">
            <span id="vistaVolPesaje"></span>
          </td>
        </tr>
      </table>
     
      <table id="cntVistaMetodoContometro" class="sgo-table table" style="border:1px solid;border-spacing: 0px;border-collapse:collapse;">
        <tr>           
          <td style="width: 12%;padding-left:10px;" class="tabla-vista-titulo">
          L. Inicial :
          </td>
          <td style="width: 12%" class="text-right">
          <span id="vistaLecturaInicial"  ></span>
          </td>
          <td style="width: 12%;padding-left:10px;" class="tabla-vista-titulo">
          L. Final :
          </td>                
          <td style="width: 12%" class="text-right" >
          <span id="vistaLecturaFinal"  ></span>
          </td>
          <td style="width: 12%;padding-left:10px;" class="tabla-vista-titulo">
          Vol. Obs. 
          </td>                
          <td style="width: 12%" class="text-right">
          <span id="vistaVolumenContometro"  ></span>
          </td>
        </tr>
      </table>
      
      <table id="cntVistaDescargaComparacion" class="sgo-table table" style="border:1px solid;border-spacing: 0px;border-collapse:collapse;">
        <tr>           
          <td style="width: 20%" class="tabla-vista-titulo"> 
            Vol. Despachado 60°F (gal)
          </td>
          <td style="width: 10%" class="text-right">
            <span id="vistaVolumenDespachadoCorregido"></span>
          </td>
          <td style="width: 20%;padding-left:10px;" class="tabla-vista-titulo">
            Vol. Recibido 60°F (gal)
          </td>
          <td style="width: 10%;" class="text-right" >
            <span id="vistaVolumenRecibidoCorregido"></span>
          </td>
          <td style="width: 20%;padding-left:10px;" class="tabla-vista-titulo">
            Variación 60°F (gal)
          </td>                
          <td style="width: 10%" class="text-right">
            <span id="vistaVariacionFinal"></span>
          </td>
        </tr>
        <tr>           
          <td class="tabla-vista-titulo"> 
          Merma Permisible (gal)
          </td>
          <td class="text-right">
            <span id="vistaMermaPermisible"></span>
          </td>
          <td style="padding-left:10px;" class="tabla-vista-titulo">
            Vol. Faltante T. Obs. (gal)
          </td>
          <td class="text-right">
            <span id="vistaVolumenExcedenteObservado"  ></span>
          </td>
          <td style="padding-left:10px;" class="tabla-vista-titulo">
            Vol. Faltante 60°F (gal)
          </td>                
          <td class="text-right">
            <span id="vistaVolumenExcedenteCorregido"  ></span>
          </td>
        </tr>
      </table>
      
      <table style="width:100%;" class="sgo-table table" id="cntVistaObservacionesDescarga">
        <tbody id="plantillaObservacionesDescarga">
          <tr>
            <td colspan="4" class="tabla-vista-titulo">
              Observaciones:
            </td>
          </tr>
          <tr>
            <td colspan="5"><span id="vistaObservacionesTexto"></span></td>
          </tr>
        </tbody>
      </table> 
      
      <table style="width:100%;">
        <tbody>
          <tr>
          <td>Eventos</td>          
          </tr>
        </tbody>
      </table>
      <table style="width:100%;" class="sgo-table table" id="cntVistaEventosDescarga">
        <tbody id="plantillaEventoDescarga">
          <tr >
            <td style="width:10%;" class="tabla-vista-titulo">
            Tipo:
            </td>
            <td>
            <span id="vistaTipoEvento"></span>
            </td>
            <td style="width:10%;" class="tabla-vista-titulo">
            Fecha y hora:
            </td>
            <td>
              <span id="vistaFechaHoraEvento"></span>
            </td>
          </tr>
          <tr>
            <td colspan="4" class="tabla-vista-titulo">
              Detalle del evento:
            </td>
          </tr>
          <tr>
            <td colspan="5"><span id="vistaObservacionesEvento"></span></td>
          </tr>
        </tbody>
      </table>


      
      </div>
        <div class="box-footer">
          <a id="btnCerrarVistaDescarga" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></a>
        </div>
        <div class="overlay" id="ocultaVistaDescarga" style="display:none">
          <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>
    </div>
  </div>
  
  <div class="row" id="cntEventoDescarga" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmEvento">
						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cliente: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoCliente'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Estación: </label> 					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoOperacion'></span></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">F.Planificada: </label>				</td>
			      				<td class="celda-detalle" style="width:10%;"> <span id="cmpEventoFechaPlanificacion" class="form-control alert-danger text-center input-sm text-uppercase" readonly /> </td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">G/R: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoNumeroGuiaRemision'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal"> </label> 					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoNumeroOrdenCompra'></span></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cisterna/Tracto: </label>				</td>
			      				<td class="celda-detalle" style="width:10%;"> <span class="input-sm text-uppercase" id='cmpEventoCisternaTracto'></span></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Tipo: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;">  <input class="hide" name="cmpEventoIdTransporteEvento" id='cmpEventoIdTransporteEvento' />
										<select id="cmpEventoTipoEvento" name="cmpEventoTipoEvento" class="form-control input-sm text-uppercase">
											<option value="1">Incidencia</option>
											<option value="2">Accidente</option>
											<option value="3">Falla Operacional</option>
                      <option value="4">Otros</option>
										</select> 
								</td>
								<td class="celda-detalle" colspan="2"> </td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Fecha y hora: </label>				</td>
			      				<td class="celda-detalle" style="width:20%;"> <input name="cmpEventoFechaHora" id="cmpEventoFechaHora" type="text" class="form-control input-sm" required data-bind="value: dateTime" placeholder="__/__/____ __:__:__" data-inputmask="'mask': 'd/m/y h:s:s'" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Observaciones: </label>					</td>
			      				<td class="celda-detalle" colspan="5">  <input class="hide" name="cmpEventoIdTransporteEvento" id='cmpEventoIdTransporteEvento' />
									<textarea class="form-control input-sm text-uppercase" required id="cmpEventoDescripcion" name="cmpEventoDescripcion" placeholder="Ingrese observaci&oacute;n..." rows="3" ></textarea>
								</td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarEvento" type="submit" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i> Guardar</button>
                
		            <button id="btnCancelarGuardarEvento" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  Cancelar</button>
		            <br />
		    	</div>
		    	<div class="overlay" id="ocultaFormulario" style="display:none;">
		            <i class="fa fa-refresh fa-spin"></i>
		        </div>
			</div>
		</div>
	</div>
  
  </section>
</div>