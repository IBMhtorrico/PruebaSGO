<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="sgo.entidad.Tolerancia"%>
<%@ page import="java.util.HashMap"%>
<%
 HashMap<?, ?> mapaValores = (HashMap<?, ?>) request.getAttribute("mapaValores");
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
	<!-- Cabecera de pagina -->
	<section class="content-header">
		<h1>
			Estaciones / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small>
		</h1>
	</section>
	<section class="content">
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
						<form id="frmBuscar" class="form">
							<table class="sgo-simple-table table table-condensed"  style="width:100%;">
				      			<thead>
				      				<tr>
				      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Nombre: </label></td>
				      				<td class="celda-detalle" style="width:20%;"><input id="txtFiltro" type="text" class="form-control input-sm espaciado text-uppercase" placeholder="Buscar..." maxlength="20">  </td>
				      				<td></td>
				      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Operación: </label></td>
				      				<td class="celda-detalle"  style="width:35%;">
					      				<select id="cmpFiltroOperacion" name="cmpFiltroOperacion" class="form-control input-sm">
											<option selected="selected" value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
											<%
											 ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("listadoOperaciones");
											 int numeroOperaciones = listaOperaciones.size();
											 int indiceOperaciones = 0;
											 Operacion eOperacion = null;
											 for (indiceOperaciones = 0; indiceOperaciones < numeroOperaciones; indiceOperaciones++) {
											  eOperacion = (Operacion) listaOperaciones.get(indiceOperaciones);
											%>
											<option value='<%=eOperacion.getId()%>'><%=eOperacion.getCliente().getNombreCorto() + " / " + eOperacion.getNombre().trim()%></option>
											<%
											 }
											%>
										</select>
									</td>
				      				<td></td>
				      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Estado: </label> </td>
				      				<td class="celda-detalle"  style="width:15%;">
				      					<select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control input-sm espaciado">
											<option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
											<option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
											<option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
										</select>
				      				</td>
				      				<td></td>
				      				<td class="celda-detalle"  style="width:15%;"> <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i> Filtrar</a> </td>
				      				</tr>
				      			</thead>
				      			<tbody>      				
				      			</tbody>
				      		</table>
						
							
							
						</form>
					</div>
					<div class="box-header">
						<div>
							<a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i
								class="fa fa-plus"></i> <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
							<a id="btnModificar"
								class="btn btn-default btn-sm disabled espaciado"><i
								class="fa fa-edit"></i> <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
							<a id="btnModificarEstado"
								class="btn btn-default btn-sm disabled espaciado"><i
								class="fa fa-cloud-upload"></i> <%=mapaValores.get("ETIQUETA_BOTON_ACTIVAR")%></a>
							<a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i
								class="fa fa-search"></i> <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
						</div>
					</div>
					<div class="box-body">
						<table id="tablaPrincipal" class="sgo-table table table-striped"
							style="width: 100%;">
							<thead>
								<tr>
									<th>N#</th>
									<th>ID</th>
									<th>Nombre</th>
									<th>Tipo</th>
									<th>Operaci&oacute;n</th>
									<th>Estado</th>
								</tr>
							</thead>
						</table>
						<div id="frmConfirmarModificarEstado" class="modal"
							data-keyboard="false" data-backdrop="static">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
									</div>
									<div class="modal-body">
										<p><%=mapaValores.get("MENSAJE_CAMBIAR_ESTADO")%></p>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default pull-left"
											data-dismiss="modal">Cerrar</button>
										<button id="btnConfirmarModificarEstado" type="button"
											class="btn btn-primary">Confirmar</button>
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
						<form id="frmPrincipal">
							<div class="form-group">
								<label>Nombre (Max. 20 caracteres)</label> <input
									name="cmpNombre" id="cmpNombre" type="text"
									class="form-control input-sm text-uppercase" maxlength="20"
									required placeholder="Ingresar Nombre" />
							</div>
							<div class="form-group">
								<label>Número de turnos por Jornada</label> <input
									name="cmpCantidadTurnos" id="cmpCantidadTurnos" type="text" class="form-control input-sm" maxlength="20"
									required placeholder="Ingresar Cantidad de turnos" />
							</div>
							<div class="form-group">
								<label>Tipo</label> <select id="cmpTipo" name="cmpTipo"
									class="form-control input-sm">
									<option value="<%=mapaValores.get("VALOR_TIPO_ESTANDAR")%>"><%=mapaValores.get("TEXTO_TIPO_ESTANDAR")%></option>
									<option value="<%=mapaValores.get("VALOR_TIPO_REPARTO")%>"><%=mapaValores.get("TEXTO_TIPO_REPARTO")%></option>
									<option value="<%=mapaValores.get("VALOR_TIPO_TUBERIA")%>"><%=mapaValores.get("TEXTO_TIPO_TUBERIA")%></option>
								</select>
							</div>
							
							<div class="form-group">
								<label>Operaci&oacute;n</label> <select tipo-control="select2"
									id="cmpIdOperacion" name="cmpIdOperacion"
									class="form-control input-sm" style="width: 100%">
									<%
									 numeroOperaciones = listaOperaciones.size();
									 indiceOperaciones = 0;
									 eOperacion = null;
									 for (indiceOperaciones = 0; indiceOperaciones < numeroOperaciones; indiceOperaciones++) {
									  eOperacion = (Operacion) listaOperaciones.get(indiceOperaciones);
									%>
									<option value='<%=eOperacion.getId()%>'><%=eOperacion.getCliente().getNombreCorto() + " / " + eOperacion.getNombre().trim()%></option>
									<%
									 }
									%>
								</select>
							</div>
							<div class="form-group">
								<label>Método Descarga</label> <select tipo-control="select2"
									id="cmpMetodoDescarga" name="cmpMetodoDescarga"
									class="form-control input-sm" style="width: 100%">
									<option value="<%=mapaValores.get("VALOR_METODO_WINCHA")%>"
										selected="selected"><%=mapaValores.get("TEXTO_METODO_WINCHA")%></option>
									<option value="<%=mapaValores.get("VALOR_METODO_BALANZA")%>"><%=mapaValores.get("TEXTO_METODO_BALANZA")%></option>
									<option value="<%=mapaValores.get("VALOR_METODO_CONTOMETRO")%>"><%=mapaValores.get("TEXTO_METODO_CONTOMETRO")%></option>
								</select>
							</div>
							<div class="form-group">
								<label>Estado</label> <select id="cmpEstado" name="cmpEstado"
									class="form-control input-sm">
									<option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
									<option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
								</select>
							</div>
						</form>
						<span>Tolerancias</span>
						<table class="sgo-simple-table table table-condensed"
							style="width: 100%;">
							<thead>
								<tr>
									<td style="width: 45%;" class="celda-cabecera-detalle">
										<label class="etiqueta-titulo-horizontal">Producto</label>
									</td>
									<td style="width: 25%;" class="celda-cabecera-detalle">
										<label	class="etiqueta-titulo-horizontal">Tolerancia</label>
									</td>
									<td style="width: 25%;" class="celda-cabecera-detalle">
										<label	class="etiqueta-titulo-horizontal">Tipo Volumen</label>
									</td>
									<td style="width: 5%;" class="celda-cabecera-detalle"></td>
								</tr>
							</thead>
							<tbody id="GrupoTolerancia">
								<tr id="GrupoTolerancia_template">
									<td class="celda-detalle">
										<select tipo-control="select2"
										elemento-grupo="producto"
										id="GrupoPlanificacion_#index#_Producto" style="width: 100%"
										name="tolerancia[detalle][#index#][producto]"
										class="form-control input-sm text-uppercase">
											<%
											 ArrayList<?> listaProductos = (ArrayList<?>) request.getAttribute("listaProductos");
											 Producto eProducto = null;
											 for (int i = 0; i < listaProductos.size(); i++) {
											  eProducto = (Producto) listaProductos.get(i);
											%>
											<option value='<%=eProducto.getId()%>'><%=eProducto.getNombre()%></option>
											<%
											 }
											%>
									</select></td>
									<td class="celda-detalle" >
										<input elemento-grupo="porcentajeActual"
										id="GrupoTolerancia_#index#_PorcentajeActual" name="tolerancia[detalle][#index#][porcentaje_actual]"
										type="text" class="form-control input-sm text-right" value="0.3" maxlength="3"/></td>
									<td class="celda-detalle">
										<select tipo-control="select2" elemento-grupo="tipoVolumen" id="GrupoPlanificacion_#index#_TipoVolumen" style="width: 100%"
										name="tolerancia[detalle][#index#][tipoVolumen]" class="form-control input-sm text-uppercase">
											<option value='<%=mapaValores.get("TIPO_VOLUMEN_OBSERVADO")%>'><%=mapaValores.get("NOMBRE_VOLUMEN_OBSERVADO")%></option>
											<option value='<%=mapaValores.get("TIPO_VOLUMEN_CORREGIDO")%>'><%=mapaValores.get("NOMBRE_VOLUMEN_CORREGIDO")%></option>
										</select>
									</td>
									<td class="celda-detalle" >
										<a id="GrupoTolerancia_remove_current"	class="btn btn-default btn-sm"
										style="width: 100%; display: inline-block">
										<i class="fa fa-remove"></i>
										</a>
									</td>
								</tr>
								<tr id="GrupoTolerancia_noforms_template">
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						<a id="btnGuardar" class="btn btn-primary btn-sm"><i
							class="fa fa-save"></i> <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
						<a id="btnAgregarTolerancia" class="btn bg-purple btn-sm"><i
							class="fa fa-plus-circle"></i> Agregar Tolerancia</a> <a
							id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i
							class="fa fa-close"></i> <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
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
					<div class="box-body">
						<table class="sgo-table table table-striped" style="width: 100%;">
							<tbody>
								<tr>
									<td>ID:</td>
									<td><span id='vistaId'></span></td>
								</tr>
								<tr>
									<td>Nombre:</td>
									<td><span id='vistaNombre'></span></td>
								</tr>
								<tr>
									<td>Número de turnos por Jornada:</td>
									<td><span id='vistaCantidadTurnos'></span></td>
								</tr>
								<tr>
									<td>Tipo:</td>
									<td><span id='vistaTipo'></span></td>
								</tr>
								<tr>
									<td>Metodo de descarga:</td>
									<td><span id='vistaMetodo'></span></td>
								</tr>
								<tr>
									<td>Operacion:</td>
									<td><span id='vistaIdOperacion'></span></td>
								</tr>
								<tr>
									<td>Estado:</td>
									<td><span id='vistaEstado'></span></td>
								</tr>
								<tr>
									<td>Creado el:</td>
									<td><span id="vistaCreadoEl"></span></td>
								</tr>
								<tr>
									<td>Creado por:</td>
									<td><span id="vistaCreadoPor"></span></td>
								</tr>
								<tr>
									<td>IP Creaci&oacute;n:</td>
									<td><span id="vistaIpCreacion"></span></td>
								</tr>
								<tr>
									<td>Actualizado el:</td>
									<td><span id="vistaActualizadoEl"></span></td>
								</tr>
								<tr>
									<td>Actualizado por:</td>
									<td><span id="vistaActualizadoPor"></span></td>
								</tr>
								<tr>
									<td>IP Actualizaci&oacute;n:</td>
									<td><span id="vistaIpActualizacion"></span></td>
								</tr>
							</tbody>
						</table>
						<table id="tablaVistaDetalle" class="sgo-table table table-striped" style="width: 100%;">
							<thead>
			      				<tr>
			      				<td>Producto</td>
			      				<td>Tolerancia</td>
			      				<td>Tipo Volumen</td>
			      				</tr>
			      			</thead>
							<tbody>							
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						<button id="btnCerrarVista" class="btn btn-danger btn-sm">
						<%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%>
						</button>
					</div>
					<div class="overlay" id="ocultaContenedorVista">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>
