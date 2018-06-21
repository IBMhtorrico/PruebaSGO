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
	<h1>Registro de Planificaci&oacute;n / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small>
    </h1>
  </section>
  <!-- Contenido principal -->
  <section class="content">
  <!-- El contenido debe incluirse aqui-->
    <div class="row">
      <div class="col-md-12">
        <div id="bandaInformacion" class="callout callout-info">
        <%=mapaValores.get("MENSAJE_CARGANDO")%>
        </div>
      </div>
    </div>
    <div class="row" id="cntTabla">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form" novalidate="novalidate">
              <div class="row">
                <div class="col-md-6">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">Operación / Cliente: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                    <select id="filtroOperacion" name="filtroOperacion" class="form-control espaciado input-sm" style="width:100%;">
                    <%
                    ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
                    String fechaActual = (String) request.getAttribute("fechaActual");   
                    String fechaHoy = (String) request.getAttribute("fechaHoy");     
                    int numeroOperaciones = listaOperaciones.size();
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    seleccionado="";
                    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();
                    %>
                    <option <%=seleccionado%> data-planta-despacho='<%=eOperacion.getPlantaDespacho().getDescripcion() %>' data-eta='<%=eOperacion.getEta() %>' data-fecha-hoy='<%=fechaHoy%>' data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
                    <%
                    //seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
                  <div class="col-md-4">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">F. Planificada: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" />
                  </div>
                </div>
                <div class="col-md-2">
                  <a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
                </div>
              </div>
            </form>
          </div>        
          <div class="box-header">
            <div>
              <a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              <a id="btnModificar" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a> 
              <!-- <a id="btnAnular" class="btn btn-default btn-sm espaciado"><i class="fa fa-remove"></i>  Anular</a> -->
              <a id="btnDetalle" class="btn btn-default btn-sm espaciado"><i class="fa fa-search"></i> Detalle</a>
              <a id="btnVer" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
              <a id="btnNotificarUnaPlanificacion" class="btn btn-default btn-sm espaciado"><i class="fa fa-envelope-square"></i> Notificar</a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>N#</th>
                <th>ID</th>
                <th>F. Planificada</th>
                 <th>F. Carga</th> 
                <th>Tot. Cisternas</th>
                <th>U. Actualizaci&oacute;n</th>
                <th>Usuario</th>
                <th>Estado</th>
                <th>Para</th>
                <th>CC</th>
                </tr>
              </thead>
            </table>
            <div id="frmNotificarUnaPlanificacion" class="modal" data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div class="modal-body">
							<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Para: </label>					</td>
			      				<td class="celda-detalle"> <input id="cmpParaUnaPlanificacion" name="cmpParaUnaPlanificacion" type="text" class="form-control input-sm"/> 	</td>
			      				</tr>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Cc: </label> 					</td>
			      				<td class="celda-detalle"> <input id="cmpCCUnaPlanificacion" name="cmpCCUnaPlanificacion" type="text" class="form-control input-sm"/> </td>
			      				</tr>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Asunto: </label> 					</td>
			      				<td class="celda-detalle"> <input id="cmpAsuntoUnaPlanificacion" name="cmpAsuntoUnaPlanificacion" type="text" class="form-control input-sm" readonly/> </td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
			      		<br />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></button>
							<button id="btnEnviarCorreoUnaPlanicacion" type="button" class="btn btn-primary"><%=mapaValores.get("MENSAJE_ENVIAR_CORREO")%></button>
						</div>
					</div>
				</div>
			</div>
            <%-- <div id="frmConfirmarAnularEstado" class="modal" data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div class="modal-body">
							<p><%=mapaValores.get("MENSAJE_ANULAR_REGISTRO")%></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
							<button id="btnConfirmarAnular" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
						</div>
					</div>
				</div>
			</div>  --%>
			
          </div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
  <!-- Aqui empieza el formulario -->
  <div class="row" id="cntFormulario" style="display: none;">
    <div class="col-md-12">
      <div class="box box-default">
      	<div class="box-body">
      		
      		<table class="sgo-simple-table table table-condensed">
      			<thead>
      				<tr>
      				<td>
      				 	<label class="etiqueta-titulo-horizontal">Cliente: </label>
      				</td>
      				<td>
      					<input id="cmpCliente" type="text" class="form-control espaciado input-sm text-uppercase" value="Las Bambas S.A" readonly />
      				</td>
      				<td>
      				 <label class="etiqueta-titulo-horizontal">Operación: </label>
      				</td>
      				<td>
      					<input id="cmpOperacion" type="text" class="form-control espaciado input-sm text-uppercase" value="Operación1 " readonly />
      				</td>
      				<td>
      					<label class="etiqueta-titulo-horizontal">Planta Despacho: </label>
      				</td>
      				<td>
      					<input id="cmpPlantaDespacho" type="text" class="form-control espaciado input-sm text-uppercase" readonly />
      				</td>
      				</tr>
      				
      				<tr>
      				<td>
      				 <label class="etiqueta-titulo-horizontal">F.Planificada: </label>
      				</td>
      				<td><B>
      					<input id="cmpFechaPlanificada" style="color: blue" type="text" class="form-control input-sm text-center" disabled placeholder="Ingresar fecha de planificaci&oacute;n" required data-inputmask="'alias': 'dd/mm/yyyy'"/>
      					</B>
      				</td>
      				<td>
      					<label class="etiqueta-titulo-horizontal">F. Carga:</label>
      				</td>
      				<td>
      					 <input id="cmpFechaCarga" type="text" class="form-control input-sm text-center" disabled placeholder="Ingresar fecha de carga" required data-inputmask="'alias': 'dd/mm/yyyy'"/>
      				</td>
      				<%-- <td>
      					<a id="btnRegresar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
      				</td> --%>
      				<%-- Inicio Agregado por 9000002608 --%>
      				<td>
      					<label class="etiqueta-titulo-horizontal">Cantidad Cisternas:</label>
      				</td>
      				<td>
      					<input id="cmpCantCisternas" type="text" class="form-control input-sm text-right" value="0"/>
      				</td>
      				<%-- Fin Agregado por 9000002608 --%>
      				</tr>
      			</thead>
      			<tbody>      				
      			</tbody>
      		</table>
      		
      		<div id="cntPlanificaciones" style="display: none;">
      		<form id="frmPrincipal" class="form form-horizontal" novalidate="novalidate">
      		<div class="col-md-12">
			  <div class="box box-default">
				<div class="box-header with-border">
					<div class="col-xs-12">
						<div class="row" style="padding: 3px;">
							<div class="col-md-2">	<label>Fecha &uacute;ltimo d&iacute;a cerrado:</label>						</div>
							<div class="col-md-2">	<input id="cmpFechaJornada" type="text" class="form-control alert-danger text-center espaciado input-sm text-uppercase" readonly /> 		</div>
						</div>
						<div  class="col-md-12">
							<table id="tablaFormulario" class="sgo-table table table-striped" style="width:100%;">
				              <thead>
				                <tr>
				                <th>N#</th>
				                <th>Estaci&oacute;n</th>
				                <th>Tanque</th>
				                <th>Producto</th>
				                <th>Capacidad Tanque</th>
				                <th>Capacidad de Trabajo</th>
				                <th>Ocupado</th>
				                <th>Libre</th>
				                </tr>
				              </thead>
				            </table>
						  </div>
					  </div>
				  </div>
			  </div>
		    </div>

      		
      		<div class="col-md-12">
			  <div class="box box-default">
				<div class="box-header with-border">
	      		<table class="sgo-simple-table table table-condensed">
	      			<thead>
	      				<tr>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Producto</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Sum. espacio disp.</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Promedio</label></td>
	      				<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Vol. Solicitado</label></td>
					<%-- 	Comentado por 9000002608
						<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">N# Cisternas</label></td>
					--%>
						<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Observaciones</label></td>
	      				<td class="celda-cabecera-detalle"></td>
	      				</tr>
	      			</thead>
	      			<tbody id="GrupoPlanificacion">
	      				<tr id="GrupoPlanificacion_template">
	      				   <td class="celda-detalle">
		                    <select tipo-control="select2" elemento-grupo="producto" id="GrupoPlanificacion_#index#_Producto" style="width: 100%" name="planificacion[detalle][#index#][producto]" class="form-control input-sm text-uppercase">
		                    	<option value="0" selected="selected">Seleccionar...</option>
		                    </select>
	      					</td>
	      					<td class="celda-detalle" style="width:10%;">
	      					<input elemento-grupo="idPlanificacion" id="GrupoPlanificacion_#index#_IdPlanificacion" name="planificacion[detalle][#index#][idPlanificacion]" type="hidden" class="form-control input-sm text-right" />
	      					<input elemento-grupo="volumenPropuesto" id="GrupoPlanificacion_#index#_VolumenPropuesto" name="planificacion[detalle][#index#][volumen_propuesto]" type="text" class="form-control input-sm text-right" disabled/>
	      					</td>
	      					<td class="celda-detalle" style="width:10%;">
	      					<input elemento-grupo="promedio" id="GrupoPlanificacion_#index#_Promedio" name="planificacion[detalle][#index#][promedio]" type="text" class="form-control input-sm text-right" disabled/>
	      					</td>
	      					<%-- 	Agregado por 9000002608	--%>
	      					<td class="celda-detalle" style="width:10%;">
	      					<input elemento-grupo="volumenSolicitado" id="GrupoPlanificacion_#index#_VolumenSolicitado" name="planificacion[detalle][#index#][volumen_solicitado]" type="text" class="form-control input-sm text-right" maxlength="6"/>
	      					</td>
	      					<%-- 	Comentado por 9000002608
	      					<td class="celda-detalle" style="width:10%;">
	      					<input elemento-grupo="volumenSolicitado" id="GrupoPlanificacion_#index#_VolumenSolicitado" name="planificacion[detalle][#index#][volumen_solicitado]" type="text" class="form-control input-sm text-right" disabled/>
	      					</td>
	      					<td class="celda-detalle" style="width:10%;">
	      					 <input elemento-grupo="numeroCisternas" id="GrupoPlanificacion_#index#_NumeroCisternas" name="planificacion[detalle][#index#][numero_cisternas]" type="text" class="form-control input-sm text-right" maxlength="7" value="0" />
	      					</td>
	      					--%>
	      					<td class="celda-detalle" style="width:20%;">
	      					 <textarea elemento-grupo="observacion" id="GrupoPlanificacion_#index#_Observacion" name="planificacion[detalle][#index#][observacion]" type="text" class="form-control input-sm text-uppercase" maxlength="700" rows="0"></textarea>
	      					</td>
	      					<td class="celda-detalle" style="width:3%;">
	      					 <a elemento-grupo="botonElimina" id="GrupoPlanificacion_#index#_elimina" class="btn btn-default btn-sm" style="width:100%; display:inline-block"><i class="fa fa-remove"></i></a>
	      					</td>
	      				</tr>
	      				<tr id="GrupoPlanificacion_noforms_template">
	      				<td></td>
	      				</tr>    			
	      			</tbody>
	      		</table>
	      		</div>
	      		</div>
	      		</div>
	      	</form>
	      		<div class="box-footer">
		          <a id="btnGuardar" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
		          <a id="btnAgregarProducto" class="btn bg-purple btn-sm"><i class="fa fa-plus-circle"></i>  Agregar Producto</a>
		          <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
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
			<div class="box-body">
				<table id="tablaVistaCabecera" class="sgo-table table" style="width:100%;">
					<tbody>
						<tr>
							<td class="tabla-vista-titulo" style="width:15%;">Cliente: </td>
							<td style="width:20%;"><span id='vistaCliente'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">Operaci&oacute;n: </td>
							<td style="width:15%;"><span id='vistaOperacion'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">Planta Despacho: </td>
							<td style="width:15%;"><span id='vistaPlantaDespacho'></span></td>
						</tr>
						<tr>
							<td class="tabla-vista-titulo" style="width:15%;">F. Planificaci&oacute;n: </td>
							<td style="width:15%;"><span id='vistaFechaPlanificacion'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">F. Carga: </td>
							<td style="width:15%;"><span id='vistaFechaCarga'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">Fecha &uacute;ltimo d&iacute;a cerrado:</td>
							<td style="width:15%;"><span id='vistaFechaJornada'></span></td>
						</tr>
						<%-- Agregado por req 9000002608--%>
						<tr>
							<td class="tabla-vista-titulo" style="width:15%;"></td>
							<td style="width:20%;"></td>
							<td class="tabla-vista-titulo" style="width:15%;"></td>
							<td style="width:15%;"></td>
							<td class="tabla-vista-titulo" style="width:15%;">Cantidad Cisternas: </td>
							<td style="width:15%;"><span id='vistaCantidadCisternas'></span></td>
						</tr>
						<%-- --%>
					</tbody>
				</table>
				<br>
				<div  class="col-md-12" class="box-header with-border">
					<table id="tablaDetalle" class="sgo-table table table-striped" style="width:100%;">
		              <thead>
		                <tr>
		                <th>N#</th>
		                <th>Tanque</th>
		                <th>Producto</th>
		                <th>Capacidad Tanque</th>
		                <th>Capacidad de Trabajo</th>
		                <th>Ocupado</th>
		                <th>Libre</th>
		                </tr>
		              </thead>
		            </table>
				</div>
				<br>
				<table id="tablaVistaDetalle" class="sgo-table table table-striped" style="width:100%;">
      			<thead>
      				<tr>
      				<td>Producto</td>
      				<td>Vol. Propuesto</td>
      				<td>Vol. Solicitado</td>
      				<%-- Comentado por 9000002608 
					<td>N# Cisternas</td>
					--%>
      				</tr>
      			</thead>			
				<tbody>					
				</tbody>
				</table>
				
				<table id="tablaVistaPie" class="sgo-table table" style="width:100%;">
					<tbody>
						<tr>
							<td class="tabla-vista-titulo" style="width:15%;">Creado por: </td>
							<td style="width:20%;"><span id='vistaCreadoPor'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">Creado el:</td>
							<td style="width:15%;"><span id='vistaCreadoEl'></span></td>							
							<td class="tabla-vista-titulo" style="width:15%;">IP (Creaci&oacute;n): </td>
							<td style="width:15%;"><span id='vistaIpCreacion'></span></td>
						</tr>
						<tr>
							<td class="tabla-vista-titulo" style="width:15%;">Actualizado por: </td>
							<td style="width:20%;"><span id='vistaActualizadoPor'></span></td>
							<td class="tabla-vista-titulo" style="width:15%;">Actualizado el:</td>
							<td style="width:15%;"><span id='vistaActualizadoEl'></span></td>							
							<td class="tabla-vista-titulo" style="width:15%;">IP (Actualizacion): </td>
							<td style="width:15%;"><span id='vistaIpActualizacion'></span></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="box-footer">
				<button id="btnCerrarVista" class="btn btn-sm btn-danger"><i class="fa fa-close"></i> <%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%> </button>
			</div>
		    <div class="overlay" id="ocultaContenedorVista">
		      <i class="fa fa-refresh fa-spin"></i>
		    </div>
    	</div>
    </div>
  </div>
  <div class="row" id="cntDetallePlanificacion" style="display: none;">
    <div class="col-md-12">
		<div class="box box-default">
			<div class="box-body">
				<table id="tablaDetallePlanificacionCabecera" class="sgo-table table" style="width:100%;">
					<tbody>
						<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Cliente: </td>
							<td style="width:20%;"><span id='detalleCliente'></span></td>
							<td class="tabla-vista-titulo" style="width:10%;">Operaci&oacute;n: </td>
							<td style="width:20%;"><span id='detalleOperacion'></span></td>
							<td class="tabla-vista-titulo" style="width:10%;">Periodo entre: </td>
							<td style="width:30%;"><span id='detalleFechaPlanificacion'></span></td>
						</tr>
					</tbody>
				</table>
				<br>
				<div class="box-footer">
					<button id="btnCerrarDetalle" class="btn btn-sm btn-default espaciado"><i class="fa fa-mail-reply"></i>  Regresar</button>
					<button id="btnNotificar" class="btn btn-default btn-sm espaciado"><i class="fa fa-envelope-square"></i> Notificar  </button>
				</div>
					<table id="tablaDetallePlanificacion" class="sgo-table table table-striped" style="width:100%;">
		              <thead>
		                <tr>
		                <th>Fecha Planificada</th>
		                <th>Fecha Carga</th>
		                <th>Producto</th>
		                <%-- Comentado por req 9000002608
		                <th>Cant. Cisternas</th>
		                --%>
		                <%-- Agregado por req 9000002608--%>
		                <th>Vol. Solicitado</th>
		                <%-- --%>
		                <th>Observación</th>
		                </tr>
		              </thead>
		            </table>
			</div>
			
			<div id="frmNotificar" class="modal" data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div class="modal-body">
							<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Para: </label>					</td>
			      				<td class="celda-detalle"> <input id="cmpPara" name="cmpPara" type="text" class="form-control input-sm"/> 	</td>
			      				</tr>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Cc: </label> 					</td>
			      				<td class="celda-detalle"> <input id="cmpCC" name="cmpCC" type="text" class="form-control input-sm"/> </td>
			      				</tr>
			      				<tr>
			      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Asunto: </label> 					</td>
			      				<td class="celda-detalle"> <input id="cmpAsunto" name="cmpAsunto" type="text" class="form-control input-sm" readonly/> </td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
			      		<br />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></button>
							<button id="btnEnviarCorreo" type="button" class="btn btn-primary"><%=mapaValores.get("MENSAJE_ENVIAR_CORREO")%></button>
						</div>
					</div>
				</div>
			</div>
		    <div class="overlay" id="ocultaContenedorDetalle">
		      <i class="fa fa-refresh fa-spin"></i>
		    </div>
    	</div>
    </div>
  </div>
  </section>
</div>