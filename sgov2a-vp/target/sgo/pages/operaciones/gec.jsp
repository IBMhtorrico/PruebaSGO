<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Guía de entrega de combustible / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
            <form id="frmBuscar" class="form" novalidate="novalidate">
              <div class="row">
                <div class="col-md-5">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal"> Cliente / Operación : </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                    <select id="cmpFiltroOperacion" name="cmpFiltroOperacion" class="form-control espaciado input-sm" style="width:100%;">
                    <%
                    ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
                    String fechaActual = (String) request.getAttribute("fechaActual"); 
                    int rolIdUsuario = (Integer) request.getAttribute("rolIdUsuario");
                    String rolUsuario = (String) request.getAttribute("rolUsuario");                     
                    int numeroOperaciones = listaOperaciones.size();
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    seleccionado="";
                    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();  
                    %>
                    <option  <%=seleccionado%> data-id-rol='<%=rolIdUsuario%>' data-rol-usuario='<%=rolUsuario%>' data-id-cliente='<%=eOperacion.getIdCliente() %>'  data-planta-despacho='<%=eOperacion.getPlantaDespacho().getDescripcion() %>' data-eta='<%=eOperacion.getEta() %>' data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=  eCliente.getNombreCorto().trim()+ " / " +eOperacion.getNombre().trim() %></option>
                    <%
                    //seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
				<div class="col-md-2">
					<div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">Estado : </label>
					</div>
					<div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
					<select id="cmpFiltroEstado" class="form-control espaciado input-sm" style="width:100%;">
						<option value="0">Todos</option>
						<option value="1">Registrado</option>
						<option value="2">Emitido</option>
						<option value="3">Aprobado</option>
						<option value="4">Observado</option>
					</select>
					</div>
                </div>
				<div class="col-md-3">
					<div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">F. Planificada: </label>
					</div>
					<div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" />
                    <input id="filtroIdRolUsuario" type="hidden" style="width:100%" class="form-control espaciado input-sm" value="<%=rolIdUsuario%>" />
                    <input id="filtroRolUsuario" type="hidden" style="width:100%" class="form-control espaciado input-sm" value="<%=rolUsuario%>" />
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
              	<a id="btnAgregarGEC" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              	<a id="btnModificarGEC" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
              	<a id="btnVerGEC" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
            	<a id="btnEmitir" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_EMITIR")%></a>
            	<a id="btnAprobar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_APROBAR")%></a>
            	<a id="btnImprimir" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_IMPRIMIR")%></a>
            	<a id="btnNotificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_NOTIFICAR")%></a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>N#</th>
                <th>ID</th>
                <th>N° GEC</th>
                <th>Fecha</th>
                <th>Operacion</th>
                <th>T. Volumen Despachado</th>
                <th>T. Volumen Recibido</th>
                <th>Estado</th>
                </tr>
              </thead>
            </table>
            <div id="frmConfirmarModificarEstado" class="modal" data-keyboard="false" data-backdrop="static">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
                  </div>
                  <div class="modal-body">
                    <p>¿Desea emitir la guia de entrega de combustible seleccionada?</p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
                    <button id="btnConfirmarModificarEstado" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- NOTIFICAR -->
            <div id="frmConfirmarNotificar" class="modal" data-keyboard="false" tabindex="-1" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>							
						</div>
						<div class="modal-body">
<%-- 							<p><%=mapaValores.get("MENSAJE_NOTIFICAR_PROGRAMACION")%></p> --%>
							<table class="sgo-simple-table table table-condensed">
			                <thead>
			                <tr>
			                	<td class="celda-detalle" costyle="width:36%;" colspan="2"> <label class="etiqueta-titulo-horizontal"><%=mapaValores.get("MENSAJE_NOTIFICAR_PROGRAMACION")%></label></td>
			                	
			                </tr>
			                  <tr>
				                    <td class="celda-detalle" style="width:10%;"><label>Cliente/Operaci&oacute;n: </label></td>
				        			<td class="celda-detalle" style="width:26%;"><span id='notificarClienteOpe'>	</span></td>
			        		   </tr>
			        		   <tr>
			        		   		<td class="celda-detalle" style="width:10%;"><label>Planta Despacho: </label></td>
				        			<td class="celda-detalle" style="width:20%;"><span id='notificarPlanta'>	</span></td>
			        		   </tr>
			                   <tr>
				                    <td class="celda-detalle" style="width:10%;"><label>F. Planificada: </label></td>
				                    <td class="celda-detalle" style="width:7%;"><FONT COLOR=blue><B>	<span id='notificarFechaDescarga'>	</span></B></FONT></td>
				        		</tr>
				        		<tr>
				        			<td class="celda-detalle" style="width:10%;"><label>F. Carga: </label></td>
					                <td class="celda-detalle" style="width:7%;"><FONT COLOR=blue><B>	<span id='notificarFechaCarga'>		</span></B></FONT> </td>
			                	</tr>
			                </thead>
			              </table>
							
							
							
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
							<button id="btnConfirmarNotificar" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
						</div>
					</div>
				</div>
			</div>
            
            <div id="frmNotificar" class="modal" data-keyboard="false" tabindex="-1" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div class="modal-body">						
							<form id="frmCorreo" method="post" class="form-horizontal">
								<table class="sgo-simple-table table table-condensed">
					      			<thead>
					      				<tr>
					      				<td class="celda-detalle" style="width:20%;"> <label class="etiqueta-titulo-horizontal">Para: </label></td>
					      				<td class="celda-detalle"> <input id="cmpPara" name="cmpPara" type="text"  class="form-control input-sm"/> 	</td>
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
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></button>
							<button id="btnEnviarCorreo" type="button" class="btn btn-primary"><%=mapaValores.get("MENSAJE_ENVIAR_CORREO")%></button>
						</div>
					</div>
				</div>
			</div>
            <!-- FIN NOTIFICAR -->
            
          </div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
       <form id="frmPrincipal">
        <div class="box box-default">
           <div class="box-body">
           
            <table style="width:100%">
            	<tr>
            		<td style=""><strong>Cliente / Operación</strong></td>
            		<td style="width:70%;">
            		<input name="cmpClienteOperacion" id="cmpClienteOperacion" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled/>
            		</td>
            	</tr>
            	<tr>
            		<td><strong>Guia de entrega de combustible</strong></td>
            		<td >
            		<input name="cmpNumeroGuia" id="cmpNumeroGuia" type="text" class="form-control text-uppercase input-sm" maxlength="20" disabled/>
            		</td>
            	</tr>
              <tr>
            		<td><strong>Nº de Contrato</strong></td>
            		<td>
            		<input name="cmpNumeroContrato" id="cmpNumeroContrato" type="text" class="form-control text-uppercase input-sm" maxlength="20" disabled/>
            		</td>
            	</tr>
            	<tr>
            		<td><strong>Descripción del contrato</strong></td>
            		<td>
            		<input name="cmpDescripcionContrato" id="cmpDescripcionContrato" type="text" class="form-control text-uppercase input-sm" maxlength="500" disabled/>
            		</td>
            	</tr>
				<tr>
            		<td><strong>Orden de Compra</strong></td>
            		<td>
            		<input name="cmpOrdenCompra" id="cmpOrdenCompra" type="text" class="form-control text-uppercase input-sm" maxlength="20" />
            		</td>
            	</tr>
            </table>

          </div>
        </div>
        <div class="box box-default">
           <div class="box-body">
            <table>
              <tr>
                <td style="width:15%;">
                  <strong>Transportista:</strong>
                </td>
                <td style="width:25%;">
                  <select id="cmpIdTransportista" name="cmpIdTransportista" class="form-control input-sm" style="width: 100%">
                  <option value="" selected="selected">Seleccionar...</option>
                  </select>
                </td>
                <td style="padding-left:10px;width:10%;">
                <strong>Producto:</strong>
                </td>
                <td style="width:18%;">
                <select name="cmpProducto" id="cmpProducto"  style="width: 100%"  class="form-control input-sm text-uppercase">
                <option value="" selected="selected">Seleccionar...</option>
                </select>
                </td>
                <td style="padding-left:10px;width:10%;">
                <strong>Fecha :</strong>
                </td>
                <td style="width:6%;">
                <input name="cmpFiltroFechaGuia"  id="cmpFiltroFechaGuia" type="text" class="form-control text-uppercase input-sm" maxlength="150" />
                </td>
                <td style="width:6%;">
                <a id="btnRecuperarGuiasRemision" class="btn btn-primary btn-sm"><i class="fa fa-save"></i> Filtrar</a>
                </td>
              </tr>
            </table>
            <table id="tablaDetalleGec" style="width:100%;margin-top:10px;">
            	<thead>
            	<tr>
            		<td> </td>
            		<td><strong>Nº Guia Remisión</strong></td>
            		<td><strong>Fecha de Emisión</strong></td>
            		<td><strong>Fecha de Recepción</strong></td>
            		<td><strong>Volumen Despachado en Planta</strong></td>
            		<td><strong>Volumen Recibido en Mina</strong></td>
            		<td><strong>Estado</strong></td>
            	</tr>
            	</thead>
            	<tbody>
            	<tr id="plantillaDetalleGec" data-fila="0">
            		<td><input name="cmpSelectorDetalle" id="cmpSelectorDetalle" type="checkbox" name="selector" value="0"></td>
            		<td><span  id="cmpDetalleNumeroGuia"></span></td>
            		<td><span id="cmpFechaEmision"></span></td>
            		<td><span id="cmpFechaRecepcion"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="cmpVolumenDespachado"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="cmpVolumenRecibido"></span></td>
            		<td><span id="cmpEstadoDiaOperativo"></span></td>
            	</tr>
				<tr id="plantillaResumenDetalleGec">
            		<td></td>
            		<td></td>
            		<td></td>
            		<td></td>
            		<td style="text-align:right;padding-right:10px;"><span id="cmpTituloTotal">Total a Facturar</span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="cmpVolumenTotalRecibido"></span></td>
            		<td></td>
            	</tr>
            	</tbody>
            </table>
            <table style="width:100%">
              <tr>
                <td>
                Observaciones:
                </td>
              </tr>
              <tr>
                <td>
                <textarea id="cmpComentarios" name="cmpComentarios" style="width:100%" class="form-control input-sm text-uppercase"  rows="3" ></textarea>
                </td>
              </tr>
            </table>
           </div>
            <div class="overlay" id="ocultaContenedorFormulario">
            <i class="fa fa-refresh fa-spin"></i>
            </div>
            <div class="box-footer">
            <a id="btnGuardar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
            </div>
           </div>
           </form>
      </div>
    </div>
    <div class="row" id="cntVistaRegistro" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <table class="sgo-table table table-striped" style="width:100%;">
              <tbody>
                <tr>
                  <td>ID:</td>
                  <td>
                    <span id='vistaId'></span>
                  </td>
                </tr>                
                <tr>
                  <td>Cliente / Operación:</td>
                  <td>
                    <span id="vistaClienteOperacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Nº GEC:</td>
                  <td>
                    <span id="vistaNumeroGuia"></span>
                  </td>
                </tr>
                <tr>
                  <td>Nº de Contrato</td>
                  <td>
                    <span id="vistaNumeroContrato"></span>
                  </td>
                </tr>
                <tr>
                  <td>Descripci&oacute;n del Contrato:</td>
                  <td>
                    <span id="vistaDescripcionContrato"></span>
                  </td>
                </tr>
                <tr>
                  <td>Orden de Compra:</td>
                  <td>
                    <span id="vistaOrdenCompra"></span>
                  </td>
                </tr>
                <tr>
                  <td>Transportista:</td>
                  <td>
                    <span id="vistaTransportista"></span>
                  </td>
                </tr>
                <tr>
                  <td>Producto:</td>
                  <td>
                    <span id="vistaProducto"></span>
                  </td>
                </tr>
				<tr>
                  <td>Fecha GEC:</td>
                  <td>
                    <span id="vistaFechaGuia"></span>
                  </td>
                </tr>
				<tr>
                  <td>Comentarios:</td>
                  <td>
                    <span id="vistaComentarios"></span>
                  </td>
                </tr>
              </tbody>
            </table>
            <table id="tablaVistaDetalle" class="sgo-table table table-striped" style="width:100%;">
                <thead>
            	<tr>
            		<td>N# Guia Remisión</td>
            		<td>Fecha de Emisión</td>
            		<td>Fecha de Recepción</td>
            		<td>Volumen Despachado en Planta</td>
            		<td>Volumen Recibido en Mina</td>
            		<td>Estado</td>
            	</tr>
            	</thead>
            	<tbody>
            	<tr id="vistaDetalleGec" data-fila="0">
            		<td><span  id="vistaDetalleNumeroGuia"></span></td>
            		<td><span id="vistaFechaEmision"></span></td>
            		<td><span id="vistaFechaRecepcion"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaVolumenDespachado"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaVolumenRecibido"></span></td>
            		<td><span id="vistaEstadoDiaOperativo"></span></td>
            	</tr>
				<tr id="vistaResumenDetalleGec">
            		<td></td>
            		<td></td>
            		<td></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaTituloTotal">Total a Facturar</span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaVolumenTotalRecibido"></span></td>
            		<td></td>
            	</tr>
            	</tbody>
            </table>
            
            <div class="sgo-table table" style="width:100%;">
				<table id="listado_vista_aprobacionGec" class="sgo-table table"  style="width:100%;"></table>
			</div>
				
            <table class="sgo-table table" style="width:100%;">
				<tbody>
					<tr>
					<td class="tabla-vista-titulo" style="width:10%;">Creado el:			</td>
					<td class="text-center" style="width:23%;"> <span id='vistaCreadoEl'></span>		</td>
					<td class="tabla-vista-titulo" style="width:10%;">Creado por:	</td>
					<td class="text-left" style="width:23%;"> <span id='vistaCreadoPor'></span>	</td>
					<td class="tabla-vista-titulo" style="width:10%;">IP Creaci&oacute;n:	</td>
					<td class="text-left"> <span id='vistaIPCreacion'></span>		</td>
					</tr>
					<tr>
					<td class="tabla-vista-titulo" style="width:10%;">Actualizado el:			</td>
					<td class="text-center" style="width:23%;"> <span id='vistaActualizadoEl'></span>		</td>
					<td class="tabla-vista-titulo" style="width:10%;">Actualizado por:	</td>
					<td class="text-left" style="width:23%;"> <span id='vistaActualizadoPor'></span>	</td>
					<td class="tabla-vista-titulo" style="width:10%;">IP Actualizaci&oacute;n:	</td>
					<td class="text-left"> <span id='vistaIPActualizacion'></span>		</td>
					</tr>
				</tbody>
			</table>
					
           <!--  <table class="sgo-table table table-striped" style="width:100%;">
            	<tr>
                 	<td>Creado el:</td>
                  	<td>
                    <span id="vistaCreadoEl"></span>
                  	</td>
                  	<td>Creado por:</td>
                  	<td>
                    <span id="vistaCreadoPor"></span>
                  	</td>
					<td>IP Creaci&oacute;n:</td>
                  	<td>
                    <span id="vistaIPCreacion"></span>
                  	</td>
            	</tr>
				<tr>
					<td>Actualizado el:</td>
					<td>
                    <span id="vistaActualizadoEl"></span>
					</td>
					<td>Actualizado por:</td>
					<td>
                    <span id="vistaActualizadoPor"></span>
                  	</td>
					<td>IP Actualizaci&oacute;n:</td>
                  	<td>
                    <span id="vistaIPActualizacion"></span>
                  	</td>
            	</tr>
            </table> -->
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
    <div class="row" id="cntAprobacionGEC" style="display:none;">
      <div class="col-md-12">
      	<form id="frmAprobacion">
	        <div class="box box-default">
	          <div class="box-body">
	            <table class="sgo-table table" style="width:100%;">
	              <tbody>               
					  <tr>
					    <td class="tabla-vista-titulo">Cliente / Operación:</td>
					    <td class="text-left">
					      <span id="aprobacionClienteOperacion"></span>
					    </td>
					    <td class="tabla-vista-titulo">Nº GEC:</td>
					    <td class="text-left">
					      <span id="aprobacionNumeroGuia"></span>
					    </td>
					    <td class="tabla-vista-titulo">Orden de Compra:</td>
					    <td class="text-left">
					      <span id="aprobacionOrdenCompra"></span>
					    </td>
					  </tr>
					  <tr>
					    <td class="tabla-vista-titulo">Transportista:</td>
					    <td class="text-left">
					      <span id="aprobacionTransportista"></span>
					    </td>
					    <td class="tabla-vista-titulo">Producto:</td>
					    <td class="text-left">
					      <span id="aprobacionProducto"></span>
					    </td>
					    <td class="tabla-vista-titulo">Fecha GEC:</td>
					    <td class="text-center">
					      <span id="aprobacionFechaGuia"></span>
					    </td>
					  </tr>
					</tbody>
	            </table>
	            <table id="tablaAprobacionDetalle" class="sgo-table table table-striped" style="width:100%;">
	                <thead>
	            	<tr>
	            		<td>N# Guia Remisión</td>
	            		<td>Fecha de Emisión</td>
	            		<td>Fecha de Recepción</td>
	            		<td>Volumen Despachado en Planta</td>
	            		<td>Volumen Recibido en Mina</td>
	            		<td>Estado</td>
	            	</tr>
	            	</thead>
	            	<tbody>
	            	<tr id="aprobacionDetalleGec" data-fila="0">
	            		<td><span  id="aprobacionDetalleNumeroGuia"></span></td>
	            		<td><span id="aprobacionFechaEmision"></span></td>
	            		<td><span id="aprobacionFechaRecepcion"></span></td>
	            		<td style="text-align:right;padding-right:10px;"><span id="aprobacionVolumenDespachado"></span></td>
	            		<td style="text-align:right;padding-right:10px;"><span id="aprobacionVolumenRecibido"></span></td>
	            		<td><span id="aprobacionEstadoDiaOperativo"></span></td>
	            	</tr>
					<tr id="aprobacionResumenDetalleGec">
	            		<td></td>
	            		<td></td>
	            		<td></td>
	            		<td style="text-align:right;padding-right:10px;"><span id="aprobacionTituloTotal">Total a Facturar</span></td>
	            		<td style="text-align:right;padding-right:10px;"><span id="aprobacionVolumenTotalRecibido"></span></td>
	            		<td></td>
	            	</tr>
	            	</tbody>
	            </table>
	            <table class="sgo-table table" style="width:100%;">
					<tbody>
						<tr>
						<td class="tabla-vista-titulo" style="width:10%;">Registrado el:			</td>
						<td class="text-center" style="width:15%;"> <span id='aprobacionRegistradoEl'></span>		</td>
						<td class="tabla-vista-titulo" style="width:10%;">Registrado por:	</td>
						<td class="text-left" style="width:15%;"> <span id='aprobacionRegistradoPor'></span>	</td>
						<td class="tabla-vista-titulo" style="width:10%;">Emitido el:			</td>
						<td class="text-center" style="width:15%;"> <span id='aprobacionEmitidoEl'></span>		</td>
						<td class="tabla-vista-titulo" style="width:10%;">Emitido por:	</td>
						<td class="text-left" style="width:15%;"> <span id='aprobacionEmitidoPor'></span>	</td>
						</tr>
					</tbody>
				</table> 
				<table class="sgo-table table" style="width:100%;">
					<tbody>
						<tr>
							<td>
								<label>Comentarios:</label>		
							</td>
						</tr>
						<tr>
							<td>
			                	<textarea id="aprobacionObservacionCliente" class="form-control input-sm" rows="6"></textarea>
			                </td>
						</tr>
					</tbody>
				</table>   
	          </div>
	          <div class="box-footer">
	            <a id="btnAprobarAprobacion" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_APROBAR")%></a>
	            <a id="btnObservarAprobacion" class="btn btn-primary btn-sm"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_OBSERVAR")%></a>
	            <a id="btnCancelarAprobacion" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
	          </div>
	          <div class="overlay" id="ocultaContenedorAprobacion">
	            <i class="fa fa-refresh fa-spin"></i>
	          </div>
	        </div>
	  	</form>
      </div>
    </div>
  </section>
</div>