<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
	<section class="content-header">
		<h1> Cisterna / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
	</section>
	<section class="content" id="cntInterface">
		<div class="row">
			<div class="col-xs-12">
				<div id="bandaInformacion" class="callout callout-success">
					<%=mapaValores.get("MENSAJE_CARGANDO")%>
				</div>
			</div>
		</div>
		<div class="row" id="cntTabla">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-header">
						<form id="frmBuscar" class="form-inline" role="form">
						 <table style="width:100%;">
						 	<tr>
						 		<td class="celda-detalle" style="width:3%;"><label class="espaciado">Placa: </label> </td>
						 		<td class="celda-detalle" style="width:5%;"><input id="txtFiltro" name="txtFiltro" type="text" class="form-control espaciado input-sm text-uppercase"  placeholder="Buscar..." maxlength="15"> </td>
							 	<td class="celda-detalle" style="width:3%;"><label class="espaciado">Transportista: </label> </td>
						 		<td class="celda-detalle" style="width:30%;"><select tipo-control="select2" id="cmpFiltroTransportista" name="cmpFiltroTransportista" class="form-control input-sm" style="width: 100%">
																				<option value="0" selected="selected">Seleccionar...</option>
																			</select>
							 	</td>
							 	<td style="width:1%;"></td>
						 		<td class="celda-detalle" style="width:3%;"><label class="espaciado">Estado: </label> </td>
						 		<td class="celda-detalle" style="width:5%;">	
						 			<select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control espaciado input-sm">
			                  			<option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
			                  			<option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
			                  			<option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
			                		</select>
			                	</td>
			                	<td style="width:5%;">
			                		<a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i><%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
			                	</td>
			                	<td style="width:15%;"> </td>
			               	</tr>
			              </table>
			            </form>
					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
						  <a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
			              <a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
			              <a id="btnModificarEstado" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-cloud-upload"></i>  <%=mapaValores.get("ETIQUETA_BOTON_ACTIVAR")%></a>
			              <a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
						</div>
					</div>
					<div class="box-body">
						<table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
							<thead>
								<tr>
									<th>#</th>
									<th>ID</th>
									<th>Placa</th>
									<th>Tracto</th>
									<th>Transportista</th>
									<th>Estado</th>
								</tr>
							</thead>
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
										<p><%=mapaValores.get("MENSAJE_CAMBIAR_ESTADO")%></p>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
										<button id="btnConfirmarModificarEstado" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
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
		<div class="row" id="cntFormulario" style="display: none;">
			<div class="col-md-12">
				<div class="box box-default">
					<div class="box-body">
						<form id="frmPrincipal" role="form">
							<div class="form-group">
								<label  class="control-label" for="cmpPlaca">Placa</label> 
								<input name="cmpPlaca" id="cmpPlaca" type="text" class="form-control text-uppercase input-sm" maxlength="15" required placeholder="Placa" />
							</div>		
							<div class="form-group">
								<label>Tarjeta de Cubicaci&oacute;n</label> 
								<input name="cmpTarjetaCubicacion" id="cmpTarjetaCubicacion" type="text" class="form-control text-uppercase input-sm" maxlength="20" required placeholder="Tarjeta de Cubicaci&oacute;n" />
							</div>		
							<div class="form-group">
								<label>Fecha vigencia de la tarjeta de cubicaci&oacute;n</label> 
								<input name="cmpFechaVigenciaTarjetaCubicacion" id="cmpFechaVigenciaTarjetaCubicacion" type="text" class="form-control text-uppercase input-sm" maxlength="20" required data-inputmask="'alias': 'dd/mm/yyyy'" placeholder="" />
							</div>		
							<div class="form-group">
								<label>Tracto</label> <select tipo-control="select2" id="cmpIdTracto" name="cmpIdTracto" class="form-control input-sm" style="width: 100%">
									<option value="" selected="selected">Seleccionar</option>
								</select>
							</div>
							<div class="form-group">
								<label>Transportista</label> 
								<input name="cmpNombreTransportista" id="cmpNombreTransportista" type="text" class="form-control text-uppercase input-sm" disabled placeholder="" />
							</div>
						</form>
			<label>En caso de modificar la altura de la flecha o la capacidad volumetrica, debe revisar y corregir (de ser necesario) la Tabla de Aforo.</label> 
            <table class="sgo-simple-table table table-condensed" style="width:100%;">
      			<thead>
      				<tr>
      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">N# Compartimento</label></td>
      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Altura Flecha</label></td>
      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Capacidad Volum&eacute;trica</label></td>
      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Reg. en T. Aforo</label></td>
      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">¿Tiene descargas?</label></td>
      				<td class="celda-cabecera-detalle"></td>
      				</tr>
      			</thead>
      			<tbody id="GrupoCompartimento">
      				<tr id="GrupoCompartimento_template">
      				<td class="celda-detalle">
      					<input elemento-grupo="idCompartimento" id="GrupoCompartimento_#index#_IdCompartimento" name="compartimento[detalle][#index#][idCompartimento]" type="hidden" class="form-control input-sm text-right" readonly value="1" />
      					<input elemento-grupo="numeroCompartimento" id="GrupoCompartimento_#index#_NumeroCompartimento" name="compartimento[detalle][#index#][numero_compartimento]" type="text" class="form-control input-sm text-right" readonly value="1" />
      					</td>
      					<td class="celda-detalle">
      					<input elemento-grupo="alturaFlecha" id="GrupoCompartimento_#index#_AlturaFlecha" name="compartimento[detalle][#index#][altura_flecha]" type="text" class="form-control input-sm text-right" maxlength="4" value="0" />
      					</td>
      					<td class="celda-detalle">
      					 <input elemento-grupo="capacidadVolumetrica" id="GrupoCompartimento_#index#_CapacidadVolumetrica" name="compartimento[detalle][#index#][capacidad_volumetrica]" type="text" class="form-control input-sm text-right" value="0" maxlength="8" />
      					</td>
      					<td class="celda-detalle">
      					 <input elemento-grupo="aforos" id="GrupoCompartimento_#index#_Aforos" name="compartimento[detalle][#index#][aforos]" type="text" class="form-control input-sm text-right" readonly value="0" />
      					</td>
      					<td class="celda-detalle">
      					 <input elemento-grupo="descargas" id="GrupoCompartimento_#index#_Descargas" name="compartimento[detalle][#index#][descargas]" type="text" class="form-control input-sm text-left" readonly value="0" />
      					</td>
      					<td class="celda-detalle">
      					 <a elemento-grupo="botonElimina" id="GrupoCompartimento_#index#_elimina" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-remove"></i></a>
      					</td>
      				</tr>
      				<tr id="GrupoCompartimento_noforms_template">
      				<td></td>
      				</tr>    			
      			</tbody>
      		</table>
					</div>
					<div class="box-footer">
						<a id="btnGuardar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnAgregarCompartimento" class="btn bg-purple btn-sm"><i class="fa fa-plus-circle"></i>  Agregar Compartimento</a>
            <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
					</div>
					
					<div id="frmConfirmarModificarRegistro" class="modal" data-keyboard="false" data-backdrop="static">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
									</div>
									<div class="modal-body">
										<p><%=mapaValores.get("MENSAJE_ELIMINAR_COMPARTIMENTO")%></p>
										<p><%=mapaValores.get("MENSAJE_DESEA_CONTINUAR")%></p>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
										<button id="btnConfirmarModificarRegistro" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
									</div>
								</div>
							</div>
						</div>
						
					
					<div class="overlay" id="ocultaContenedorFormulario">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
				</div>
			</div>
		</div>
		<div class="row" id="cntVistaRegistro" style="display: none;">
			<div class="col-md-12">
				<div class="box box-default">
					<div class="box-header with-border">
						<h3 class="box-title">Detalle del registro</h3>
					</div>
					<div class="box-body">
						<table class="sgo-table table table-striped" style="width:100%;">
							<tbody>
								<tr>
									<td>Id:</td>
									<td><span id='vistaId'></span></td>
								</tr>
								<tr>
									<td>Placa:</td>
									<td><span id='vistaPlaca'></span></td>
								</tr>
								<tr>
									<td>Tarjeta de Cubicaci&oacute;n:</td>
									<td><span id='vistaTarjetaCubicacion'></span></td>
								</tr>
								<tr>
									<td>Fecha de vigencia de la tarjeta de Cubicaci&oacute;n:</td>
									<td><span id='vistaFechaVigenciaTarjetaCubicacion'></span></td>
								</tr>
								<tr>
									<td>Cantidad compartimentos:</td>
									<td><span id='vistaCantCompartimentos'></span></td>
								</tr>
								<tr>
									<td>Tracto:</td>
									<td><span id='vistaIdTracto'></span></td>
								</tr>
								<tr>
									<td>Transportista:</td>
									<td><span id='vistaIdTransportista'></span></td>
								</tr>
								<tr>
									<td>Estado:</td>
									<td><span id='vistaEstado'></span></td>
								</tr>
								<tr>
									<td>Creado el:</td>
									<td><span id='vistaCreadoEl'></span></td>
								</tr>
								<tr>
									<td>Creado por:</td>
									<td><span id='vistaCreadoPor'></span></td>
								</tr>
								<tr>
									<td>Actualizado por:</td>
									<td><span id='vistaActualizadoPor'></span></td>
								</tr>
								<tr>
									<td>Actualizado El:</td>
									<td><span id='vistaActualizadoEl'></span></td>
								</tr>
								<tr>
									<td>IP (Creación):</td>
									<td><span id='vistaIpCreacion'></span></td>
								</tr>
								<tr>
									<td>IP (Actualizacion):</td>
									<td><span id='vistaIpActualizacion'></span></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						<button id="btnCerrarVista"  class="btn btn-danger btn-sm"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
					</div>
					<div class="overlay" id="ocultaContenedorVista">
			            <i class="fa fa-refresh fa-spin"></i>
			        </div>
				</div>
			</div>
		</div>
	</section>
</div>