<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Tractos/ <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
						 <table>
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
						
						<%-- 
						
			              <div class="form-group">
			                <label for="txtFiltro" class="espaciado">Placa: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado input-sm text-uppercase"  placeholder="Buscar..." maxlength="15">
			              </div>
			              <div class="form-group">
			                <label for="cmpFiltroEstado" class="espaciado">Estado: </label>
			                <select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control espaciado input-sm">
			                  <option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
			                </select>
			              </div>
			             <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a> --%>
			            </form> 
					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
							<a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i> Agregar</a> 
							<a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i> Modificar</a> 
							<a id="btnModificarEstado" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-cloud-upload"></i> Activar</a> 
							<a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i> Ver</a>
						</div>
					</div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" width="100%">
              <thead>
                <tr>
 				<th>N#</th>
                <th>ID</th>
                 <th>Placa</th>
                 <th>Transportista</th>
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
                    <p><%=mapaValores.get("MENSAJE_CAMBIAR_ESTADO")%></p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Cerrar</button>
                    <button id="btnConfirmarModificarEstado" type="button" class="btn btn-primary">Confirmar</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <form id="frmPrincipal">
               <div class="form-group">
                <label>Placa (Max. 15 caracteres)</label>
                <input name="cmpPlaca" id="cmpPlaca" type="text" class="form-control text-uppercase input-sm" maxlength="15" required placeholder="Ingrese Placa"/>
               </div>
               <div class="form-group">
                <label>Transportista</label>
                <select tipo-control="select2" id="cmpIdTransportista" name="cmpIdTransportista" class="form-control input-sm" style="width: 100%">
                  <option value="" selected="selected">Seleccionar</option>
                </select>
               </div>
              <%--  <div class="form-group">
                <label>Estado</label>
               <select id="cmpEstado" name="cmpEstado" class="form-control input-sm">
                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
                </select>
               </div> --%>
               <div class="form-group">
                <label>Código Unidad SAP (Max. 20 caracteres)</label>
                <input name="cmpCodigoReferencia" id="cmpCodigoReferencia" type="text" class="form-control text-uppercase input-sm" maxlength="20" required placeholder="Ingrese Código Unidad SAP"/>
               </div>
            </form>
          </div>
           <div class="box-footer">
            <a id="btnGuardar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
          </div>
          <div class="overlay" id="ocultaContenedorFormulario">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="cntVistaRegistro" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <table class="sgo-table table table-striped" style="width:100%;">
            <tbody>
              <tr>
                <td> ID:</td>
                <td>
                <span id='vistaId'></span>
                </td>
              </tr>
              <tr>
                <td>Placa:</td>
                <td>
                <span id='vistaPlaca'></span>
                </td>
              </tr>     
               <tr>
              <td>Transportista:</td>
                <td>
                <span id='vistaIdTransportista'></span>
                </td>
              </tr>  
               <tr>
              <td>Código Unidad SAP:</td>
                <td>
                <span id='vistaCodigoReferencia'></span>
                </td>
              </tr>                            
              <tr>
                <td>Estado:</td>
                <td>
                <span id='vistaEstado'></span>
                </td>
              </tr>              
             <tr>
                <td>Creado el:</td>
                <td>
					<span id="vistaCreadoEl"></span>
                </td>
              </tr>
			   <tr>
              	<td>Creado por:</td>
                <td>
					<span id="vistaCreadoPor"></span>
                </td>
			</tr>
			<tr>
              	<td>IP Creaci&oacute;n:</td>
                <td>
					<span id="vistaIpCreacion"></span>
                </td>
			</tr> 
             <tr>
                <td>Actualizado el:</td>
                <td>
					<span id="vistaActualizadoEl"></span>
                </td>
              </tr>
				<tr>
                <td>Actualizado por:</td>
                <td>
					<span id="vistaActualizadoPor"></span>
                </td>
              </tr>
 				<tr>
              	<td>IP Actualizaci&oacute;n:</td>
                <td>
					<span id="vistaIpActualizacion"></span>
                </td>
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