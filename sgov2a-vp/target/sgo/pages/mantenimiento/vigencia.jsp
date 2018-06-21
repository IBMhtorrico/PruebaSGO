}<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Vigencias/ <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
 </section>
	<section class="content" id="cntInterface">
		<div class="row">
			<div class="col-xs-12">
				<div id="bandaInformacion" class="callout callout-success">
					<%=mapaValores.get("MENSAJE_RECUPERAR_EXITOSO")%> 
				</div>
			</div>
		</div>
	<div class="row" id="cntTabla">
    	<div class="col-xs-12">
			<div class="box">
				<div class="box-header with-border">
				<form id="frmBuscar" class="form" novalidate="novalidate">
				  <div class="row">
                	<div class="col-md-14">
                		<div class="col-md-1"></div>
                		<div class="col-md-1" style="padding-left: 4px;padding-right: 4px">
		                	<label class="etiqueta-titulo-horizontal">Entidad: </label>
		                </div>
		                <div class="col-md-3" style="padding-left: 4px;padding-right: 4px">
		                 	<% String fechaActual = (String) request.getAttribute("fechaActual"); %>
		                	<input id="fechaActual" type="hidden" style="width:100%" class="form-control espaciado input-sm" value="<%=fechaActual%>" />
			                <select name="cmpEntidad" id="cmpEntidad" class="form-control input-sm" >
				              <option value="1">CONDUCTOR</option>
				              <option value="2">CISTERNA</option>
				            </select>
		              	</div>
						<div class="col-md-1" style="padding-left: 4px;padding-right: 4px">
		                  <label class="etiqueta-titulo-horizontal">Descripci&oacute;n: </label>
		                </div>
		              	<div id="cntIdCisterna" class="col-md-4" style="padding-left: 4px;padding-right: 4px">
		                  <select name="cmpIdCisterna" id="cmpIdCisterna" class="form-control input-sm" style="width:100%">
			                <option selected="selected" value="0">SELECCIONAR...</option>
			              </select>
		              	</div>
		              	<div id="cntIdConductor" class="col-md-4" style="padding-left: 4px;padding-right: 4px">
			              	<select name="cmpIdConductor" id="cmpIdConductor" class="form-control input-sm" style="width:100%">
				             <option selected="selected" value="0">SELECCIONAR...</option>
				            </select>
		              	</div>
		              	<div class="col-md-1" style="padding-left: 4px;padding-right: 4px">
	                  	  <a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
	                	</div>
	                 </div>
	                 </div>
		            </form> 
				</div>
				 <div class="overlay" id="ocultaContenedorTabla" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</div>
    </div>
    
    <div id="cntFormulario" style="display: none;">
    	<div class="box">
	     	<form id="frmPrincipal" class="form form-horizontal" novalidate="novalidate">
	     	<div class="col-md-12">
	      		<table class="sgo-simple-table table table-condensed">
	      			<thead>
	      				<tr>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Documento</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">N&uacute;mero</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Fecha Emisión</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Fecha Expiraci&oacute;n</label></td>
						<td class="celda-cabecera-detalle"></td>
	      				<td class="celda-cabecera-detalle"></td>
	      				</tr>
	      			</thead>
	      			<tbody id="GrupoVigencia">
	      				<tr id="GrupoVigencia_template">
	      				   <td class="celda-detalle" style="width:40%;">
	                    		<select tipo-control="select2" elemento-grupo="documento" id="GrupoVigencia_#index#_Documento" style="width: 100%" name="vigencia[detalle][#index#][documento]"  class="form-control input-sm text-uppercase">
	                    			<option value="" selected="selected">Seleccionar...</option>
	                    		</select>
	      				   </td>
	      					<td class="celda-detalle">
	      					<input elemento-grupo="identificador" id="GrupoVigencia_#index#_Identificador" name="vigencia[detalle][#index#][identificador]" type="hidden" class="form-control input-sm text-right" />
	      					<input elemento-grupo="numeroDocumento" id="GrupoVigencia_#index#_NumeroDocumento" name="vigencia[detalle][#index#][numero_documento]" type="text" class="form-control input-sm text-uppercase text-right" maxlength="20" />
	      					</td>
	      					<td class="celda-detalle">
	      					<input elemento-grupo="fechaEmision" id="GrupoVigencia_#index#_FechaEmision" name="vigencia[detalle][#index#][fecha_emision]" type="text" class="form-control input-sm text-center"  required data-inputmask="'alias': 'dd/mm/yyyy'"/>
	      					</td>
	      					<td class="celda-detalle">
	      					<input elemento-grupo="fechaExpiracion" id="GrupoVigencia_#index#_FechaExpiracion" name="vigencia[detalle][#index#][fecha_expiracion]" type="text" class="form-control input-sm text-center"  required data-inputmask="'alias': 'dd/mm/yyyy'"/>
	      					</td>
	      					<td class="celda-detalle">
	      					 <a elemento-grupo="botonModifica" id="GrupoVigencia_#index#_modifica" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-edit"></i></a>
	      					</td>
	      					<td class="celda-detalle">
	      					 <a elemento-grupo="botonElimina" id="GrupoVigencia_#index#_elimina" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-remove"></i></a>
	      					</td>
	      				</tr>
	      				<tr id="GrupoVigencia_noforms_template">
	      				<td></td>
	      				</tr>    			
	      			</tbody>
	      		</table>
	      		<div id="frmConfirmarModificarEstado" class="modal" data-keyboard="false" data-backdrop="static">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
							</div>
							<div class="modal-body">
								<p><%=mapaValores.get("MENSAJE_ELIMINAR_REGISTRO")%></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
								<button id="btnConfirmarEliminarRegistro" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
							</div>
						</div>
					</div>
	      		</div>
	      		</div>
		      	</form>
      		<div class="box-footer">
	          <a id="btnGuardar" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
	          <a id="btnAgregarDocumento" class="btn bg-purple btn-sm"><i class="fa fa-plus-circle"></i>  Agregar Documento</a>
	          <%-- <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a> --%>
	        </div>
	        <div class="overlay" id="ocultaContenedorFormulario" style="display: none;">
				<i class="fa fa-refresh fa-spin"></i>
			</div>
			
			</div>
      	</div>
  </section>
</div>