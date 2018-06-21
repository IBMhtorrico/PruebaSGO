<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Cierre de Día /<small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
  </section>
  <!-- Contenido principal -->
  <section class="content">
	<div id="cntBanda" class="row">
      <div class="col-md-12">
        <div id="bandaInformacion" class="callout callout-info">
        	<%=mapaValores.get("MENSAJE_CARGANDO")%>
        </div>
      </div>
    </div>
  <!-- El contenido debe incluirse aqui--> 
    <div class="row" id="cntTabla">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form" role="form"  novalidate="novalidate">
              <div class="row">
                <div class="col-md-6">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">Operación / Cliente: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                    <select id="filtroOperacion" name="filtroOperacion" class="form-control input-sm" style="width:100%;">
                    <%
                    ArrayList<Operacion> listaOperaciones = (ArrayList<Operacion>) request.getAttribute("operaciones"); 
                    String fechaActual = (String) request.getAttribute("fechaActual");                  
                    int numeroOperaciones = listaOperaciones.size();
                    int indiceOperaciones=0;
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    for(indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion = listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();
                    %>
                    <option <%=seleccionado%> data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
                    <%
                    seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">F. Planificada: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control input-sm" data-fecha-actual="<%=fechaActual%>" />
                  </div>
                </div>
                <div class="col-md-2">
                  <a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
                </div>
              </div>
            </form>
          </div>        
          <div class="box-header">
            <div>
              <a id="btnVerResumen" class="btn btn-default disabled espaciado btn-sm "><i class="fa fa-calendar-minus-o"></i>  Revisar Dia Operativo</a>              
              <a id="btnCambiarEstado" class="btn btn-default disabled espaciado btn-sm "><i class="fa fa-eye"></i>  Cambiar Estado</a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th class="text-center">N#</th>
                <th class="text-center">ID</th>
                <th class="text-center">F. Planificada</th>
                <th class="text-center">Tot. Asignados</th>
                <th class="text-center">Tot. Descargados</th>
                <th class="text-center">U. Actualizaci&oacute;n</th>
                <th class="text-center">Usuario</th>
                <th class="text-center">Operacion</th>
                <th class="text-center">Cliente</th>
                <th class="text-center">Estado</th>
                </tr>
              </thead>
            </table>
			<div id="frmValidarAutorizacion" class="modal" data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button  type="button" class="close" aria-label="Close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title">Ingreso de Autorizaci&oacute;n</h4>
						</div>
						
						<div class="modal-body">
							<table class="sgo-simple-table table table-condensed"  style="width:100%;">
				      			<tbody>
				      				<tr>
				      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Autorización: </label>					</td>
				      				<td class="celda-detalle"> <input name="cmpDescAutorizacion" id="cmpDescAutorizacion" type="text" class="form-control text-uppercase form-control espaciado input-sm" maxlength="15" disabled placeholder="" />	</td>
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
				      				<td class="celda-detalle" colspan="5"><textarea class="form-control text-uppercase espaciado input-sm" required id="cmpJustificacion" name="cmpJustificacion"  placeholder="Ingrese justificaci&oacute;n..." rows="5" resize="none"></textarea></td>
				      				</tr>
				      			</tbody>
				      		</table>
						</div>
						<div class="modal-footer">
				            <button id="btnGuardarValidarAutorizacion" class="btn btn-sm btn-default pull-left" data-dismiss="modal" type="button"><%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></button>
				            <button id="btnCancelarValidarAutorizacion"  class="btn btn-sm btn-default  pull-rigth" type="button"><%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></button>
				        </div>
					</div>
				</div>
			</div>

			<div id="frmAlertaValidacion" class="modal"  data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div id="mensajeError" class="modal-body">
							<p><%=mapaValores.get("MENSAJE_ERROR_CODIGO_AUTORIZACION")%></p>
						</div>
						<div class="modal-footer">
							<button id="btnCierraAlerta" type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><i class="fa fa-close"></i> <%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%> </button>
						</div>
					</div>
				</div>
			</div>
			
          </div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>    
  <!-- Resumen del cierre  -->
  <div class="row" id="cntResumenCierre" style="display:none">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-header">
            <table style="width:100%;">
              <thead>
                <tr>
                  <td style="width:15%;">
                  <label id="lblOperacionDetalleDiaOperativo" class="etiqueta-titulo-horizontal">Cliente / Operación: </label>
                  </td>
                  <td style="width:30%;">
                    <input id="cmpClienteOperacion" type="text" class="form-control input-sm" value="Cliente / Operación " readonly />
                  </td>
                  <td style="width:30%;padding-left:10px;">                   
                  </td>
                  <td style="width:15%; padding-left:10px;">
                    <label id="lblFechaPlanificacionDetalleDiaOperativo" class="etiqueta-titulo-horizontal">F. Operativa: </label>
                  </td>
                  <td style="width:10%;">
                    <input id="cmpFechaOperativa" type="text" class="form-control input-sm" value="12/12/2015" readonly />
                  </td>
                </tr>
              </thead>
            </table>     
        </div>
        <div class="box-header">
          <div>
              <a id="btnExportarPDF" class="btn btn-default espaciado btn-sm "><i class="fa fa-file-pdf-o"></i> Exportar a PDF</a>
              <a id="btnExportarCSV" class="btn btn-default espaciado btn-sm "><i class="fa fa-file-excel-o"></i> Exportar a Excel</a>              
          </div>
        </div>
        <div class="box-body">
           <table id="tablaResumenCierre" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                  <th colspan="7"></th>
                  <th colspan="2">Despachado en Planta</th>
                  <th colspan="2">Desgargado en Operación</th>
                  <th colspan="2">Referencia Control</th>
                  <th colspan="4"></th>
                </tr>
                <tr>
                <th class="text-center">N#</th>
                <th class="text-center">ID</th>
                <th class="text-center"></th>
                <th class="text-center">Estación</th>
                <th class="text-center">Tracto/Cisterna</th>
                <th class="text-center">Transportista</th>
                <th class="text-center">Método</th>
                <th class="text-center">Vol. Obs.</th>
                <th class="text-center">Vol. 60F</th>
                <th class="text-center">Vol. Obs.</th>
                <th class="text-center">Vol. 60F</th>
                <th class="text-center">V. Entrada</th>
                <th class="text-center">V. Salida</th>
                <th class="text-center">Variación</th>
                <th class="text-center">Limite</th>
                <th class="text-center">Faltante</th>
                <th class="text-center">Estado</th>
                </tr>
              </thead>
            </table>
			<table style="width:100%;">
              <tbody>
                <tr>
                  <td>
                    Observaciones
                  </td>
                </tr>
                  <tr>
                  <td>
                    <textarea id="cmpObservacionesCierre" class="form-control input-sm"></textarea>
                  </td>
                </tr>
              </tbody>
            </table>         
            <div id="frmCerrarDiaOperativo" class="modal" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
            </div>
            <div class="modal-body">
            <p><%=mapaValores.get("MENSAJE_CAMBIAR_ESTADO")%></p>
            </div>
            <div class="modal-footer">
            <button type="button" class="btn btn-danger btn-sm pull-left" data-dismiss="modal"> <i class="fa fa-times"></i>  Cancelar</button>
            <button id="btnConfirmarCerrarDiaOperativo" type="button" class="btn btn-sm btn-primary"> <i class="fa fa-check-circle"></i>  Confirmar</button>
            </div>
            </div>
            </div>
            </div>
        </div>
        <div class="box-footer">
          <div>
            <a id="btnSolicitarCerrarDiaOperativo" class="btn btn-primary espaciado btn-sm "><i class="fa fa-save"></i>  Cerrar Dia Operativo</a>
            <a id="btnCancelarCierre" class="btn btn-danger btn-sm espaciado"><i class="fa fa-arrow-circle-left"></i> Regresar </a>
          <div>
        </div>
        <div class="overlay" id="ocultaContenedorResumenCierre">
            <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>    
    </div>
  </div>
    </div>
    </div>
  
  
    <!-- Aqui empieza el formulario -->
  <div class="row" id="cntFormulario" style="display:none;">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-header">
          <h3 class="box-title">Cierre de D&iacute;a - Cambiar Estado</h3>
        </div>
        <div class="box-body">
        	<form id="frmPrincipal" class="form form-horizontal" role="form"  novalidate="novalidate">
        		<table class="sgo-simple-table table table-condensed"  style="width:100%;">
	      			<tbody>
	      				<tr>
	      				<td class="celda-detalle" style="width:15%;"> <label class="etiqueta-titulo-horizontal">Cliente: </label>					</td>
	      				<td class="celda-detalle" style="width:20%;"> <span id='clienteSeleccionado' class="espaciado input-sm text-uppercase">	</span> </td>
	      				<td style="width:30%;">
	      				<td class="celda-detalle" style="width:15%;"> <label class="etiqueta-titulo-horizontal">Operaci&oacute;n: </label>					</td>
	      				<td class="celda-detalle" style="width:20%;"> <span id='operacionSeleccionada' class="espaciado input-sm text-uppercase"></span>  </td>
	      				</tr>
	      				<tr>
	      				<td class="celda-detalle" style="width:15%;"> <label class="etiqueta-titulo-horizontal">F. Planificaci&oacute;n: </label>					</td>
	      				<td class="celda-detalle" style="width:20%;"> <span id='fechaPlanificadaSeleccionada' class="espaciado input-sm text-uppercase">	</span> </td>
	      				<td style="width:30%;">
	      				<td class="celda-detalle" style="width:15%;"> <label class="etiqueta-titulo-horizontal">Estado Actual: </label>					</td>
	      				<td class="celda-detalle" style="width:20%;"> <span id='estadoSeleccionado' class="espaciado input-sm text-uppercase"></span>  </td>
	      				</tr>
	      				<tr>
	      				<td class="celda-detalle" style="width:15%;"><label class="etiqueta-titulo-horizontal">Nuevo Estado:</label></td>
	      				<td class="celda-detalle" style="width:20%;"><select id="cmpEstado" name="cmpEstado" class="form-control input-sm text-uppercase">
																<option value="1">PLANIFICADO</option>
																<option value="2">ASIGNADO</option>
																<option value="3">DESCARGANDO</option>
															</select>
						</td>
						<td colspan="3"></td> 
	      				</tr>
	      			</tbody>
	      		</table>
          </form>
        </div>
        <div class="box-footer">
        	<button id="btnGuardarCambioEstado" type="submit" class="btn btn-sm btn-primary">Guardar </button>
            <button id="btnCancelarCambioEstado"  class="btn btn-sm btn-danger">Cancelar</button>
        </div>
			
			
			
        <div class="overlay" id="ocultaContenedorFormulario">
          <i class="fa fa-refresh fa-spin"></i>
        </div>
      </div>
    </div>
  </div>
  
  <!-- Aqui empieza el detalle  -->
  
  </section>
</div>