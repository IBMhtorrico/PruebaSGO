<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Desconche / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
                    <label class="etiqueta-titulo-horizontal">Operación / Cliente: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                    <select id="filtroOperacion" name="filtroOperacion" class="form-control espaciado input-sm" style="width:100%;">
                    <%
                    ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
                    String fechaActual = (String) request.getAttribute("fechaActual");   
                    int numeroOperaciones = listaOperaciones.size();
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    seleccionado="";
                    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();
                    %>
                    <option <%=seleccionado%> data-planta-despacho='<%=eOperacion.getPlantaDespacho().getDescripcion() %>' data-eta='<%=eOperacion.getEta() %>' data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
                    <%
                    //seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
                <div class="col-md-2">
                  <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">Cisterna: </label>
                  </div>
                  <div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="filtroCisterna" type="text" style="width:100%" class="form-control espaciado input-sm" />
                  </div>
                </div>
                <div class="col-md-3">
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
              <a id="btnAgregarDesconche" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              <a id="btnModificarDesconche" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
              <a id="btnVerDesconche" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>N#</th>
                <th>ID</th>
                <th>F.Planificación</th>
                <th>Cisterna</th>
				<th>N# Desconche</th>
                <th>Compartimento</th>
                <th>Vol. Desconche</th>
				<th>Estación</th>
                <th>Tanque</th>
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
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
           <div class="box-body">
            <form id="frmPrincipal">
            <table style="width:100%">
            	<tr>
            		<td style="padding-bottom: 5px;">
            		Cisterna:
            		</td>
					<td colspan="8">
	           		<select name="cmpDescarga" id="cmpDescarga" class="form-control text-uppercase input-sm" style="width: 100%">
	           			<option>Seleccionar...</option>
            		</select>
            		</td>
            	</tr>
            	<tr>
            		<td>F. Planificada : </td>
					<td>
					<input name="cmpFechaPlanificada" id="cmpFechaPlanificada" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled />
            		</td>
					<td style="padding-left:10px;">Tracto / Cisterna :</td>
					<td>
					<input name="cmpTractoCisterna" id="cmpTractoCisterna" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled />
            		</td>
					<td style="padding-left:10px;">Estación: </td>
					<td>
					<input name="cmpEstacion" id="cmpEstacion" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled />
            		</td>
					<td style="padding-left:10px;">Tanque: </td>
					<td>
					<input name="cmpTanque" id="cmpTanque" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled />
            		</td>
            	</tr>
            	<tr>
            		<td>N# Desconche: </td>
					<td>
					<input name="cmpNumeroDesconche" id="cmpNumeroDesconche" type="text" class="form-control text-uppercase input-sm" maxlength="150" disabled value="1" />
            		</td>
					<td style="padding-left:10px;">Volumen: </td>
					<td>
					<input name="cmpVolumenDesconche" id="cmpVolumenDesconche" type="text" class="form-control text-right input-sm" maxlength="9" />
            		</td>
					<td style="padding-left:10px;">N# Compartimento: </td>
					<td>
						<select name="cmpNumeroCompartimento" id="cmpNumeroCompartimento" class="form-control text-uppercase input-sm">
							<option value="1">1</option>
							<option value="2">2</option>
						</select>
            		</td>
					<td colspan="2"></td>
            	</tr>
            </table>
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
                  <td>ID:</td>
                  <td>
                    <span id='vistaId'></span>
                  </td>
                </tr>                
                <tr>
                  <td>Cliente / Operacion:</td>
                  <td>
                    <span id="vistaClienteOperacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Fecha Planificada:</td>
                  <td>
                    <span id="vistaFechaPlanificada"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tracto / Cisterna :</td>
                  <td>
                    <span id="vistaTractoCisterna"></span>
                  </td>
                </tr>
                <tr>
                  <td>Estación:</td>
                  <td>
                    <span id="vistaEstacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tanque:</td>
                  <td>
                    <span id="vistaTanque"></span>
                  </td>
                </tr>
                <tr>
                  <td>N# Desconche:</td>
                  <td>
                    <span id="vistaNumeroDesconche"></span>
                  </td>
                </tr>
				<tr>
                  <td>Volumen:</td>
                  <td>
                    <span id="vistaVolumen"></span>
                  </td>
                </tr>
				<tr>
                  <td>Compartimento:</td>
                  <td>
                    <span id="vistaCompartimento"></span>
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
                    <span id="vistaIPCreacion"></span>
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
                    <span id="vistaIPActualizacion"></span>
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