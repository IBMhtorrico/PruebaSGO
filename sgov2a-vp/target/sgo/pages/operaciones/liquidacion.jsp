<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Liquidación / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
                    int numeroOperaciones = listaOperaciones.size();
                    Operacion eOperacion=null;
                    Cliente eCliente=null;
                    String seleccionado="selected='selected'";
                    seleccionado="";
                    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
                    eCliente = eOperacion.getCliente();
                    %>
                    <option  <%=seleccionado%> data-id-cliente='<%=eOperacion.getIdCliente() %>'  data-planta-despacho='<%=eOperacion.getPlantaDespacho().getDescripcion() %>' data-eta='<%=eOperacion.getEta() %>' data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=  eCliente.getNombreCorto().trim()+ " / " +eOperacion.getNombre().trim() %></option>
                    <%
                    //seleccionado="";
                    } %>
                    </select>
                  </div>
                </div>
				<div class="col-md-2">

                </div>
				<div class="col-md-3">
					<div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                    <label class="etiqueta-titulo-horizontal">F. Planificada: </label>
					</div>
					<div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                    <input id="cmpFiltroFechaOperativa" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" />
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
              	<a id="btnVerDetalle" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  Detalle</a>
            	<a id="btnImprimir" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-file-pdf-o"></i> Exportar PDF</a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
	                <th>N#</th>
	                <th style="width:10%;">Día Operativo</th>
	                <th style="width:20%;">Operacion</th>
	                <th style="width:20%;">IdOperacion</th>
	                <th style="width:20%;">Producto</th>
	                <th style="width:20%;">idProducto</th>
	                <th style="width:8%;">Inventario Final Fisico</th>
	                <th style="width:8%;">Inventario Final Calculado</th>
	                <th style="width:5%;">Variación</th>
	                <th style="width:5%;">Limite Permisible</th>
	                <th style="width:5%;">Faltante</th>
	                <th style="width:10%;">Estado</th>
                </tr>
              </thead>
            </table>
            <table style="width:100%;">
               <tr>
                  <td>
                    Observaciones
                  </td>
                </tr>
                <tr>
                  <td>
                    <textarea id="cmpObservacionesLiquidacion" class="form-control input-sm"></textarea>
                  </td>
                </tr>
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
          </div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
		<div class="box-footer">
			<div>
            <a id="btnLiquidar" class="btn btn-primary espaciado btn-sm "><i class="fa fa-save"></i> Liquidar</a>
        	</div>
      	</div>
    </div>
    </div>
    </div>
    <div class="row" id="cntDetalleEstacion" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
			<div class="box-header">
          	<table style="width:100%;">
          		<tr>
          			<td>Cliente / Operación
          			</td>
					<td>
						<input name="cmpClienteOperacion" id="cmpClienteOperacion" type="text" class="form-control text-uppercase input-sm" disabled/>
          			</td>
					<td style="padding-left:10px;">
						Día Operativo
          			</td>
					<td>
						<input name="cmpDiaOperativo" id="cmpDiaOperativo" type="text" class="form-control text-uppercase input-sm"  disabled/>
          			</td>
					<td style="padding-left:10px;">
						Producto
          			</td>
					<td>
						<input name="cmpProducto" id="cmpProducto" type="text" class="form-control text-uppercase input-sm"  disabled/>
          			</td>
          		</tr>
  			</table>
  			</div>
			<div class="box-header">
            <div>
              	<a id="btnVerDetallexTanque" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  Detalle </a>
            	<a id="btnRegresarDetalle" class="btn btn-default btn-sm espaciado"><i class="fa fa-mail-reply"></i>  Regresar</a>
            </div>
          	</div>
          <div class="box-body">
            <table id="tablaVistaDetalleEstacion" class="sgo-table table table-striped" style="width:100%;">
                <thead>
                <tr>
	                <th style="width:20%;">Estación</th>
	                <th style="width:8%;">Inventario Final Fisico</th>
	                <th style="width:8%;">Inventario Final Calculado</th>
	                <th style="width:5%;">Variación</th>
	                <th style="width:5%;">Limite Permisible</th>
	                <th style="width:5%;">Faltante</th>
	                <th style="width:10%;">Estado</th>
                </tr>
            	</thead>
            	<tbody>
            	<tr id="filaDetalleEstacion" data-fila="0">
            		<td><span  id="vistaDxEEstacion"></span></td>
            		<td><span id="vistaDxEStockFinal"></span></td>
            		<td><span id="vistaDxEStockCalculado"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxEVariacion"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxELimite"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxEFaltante"></span></td>
            		<td><span id="vistaDxEEstado"></span></td>
            	</tr>
				<tr id="filaDetalleEstacionVacia">
            		<td colspan="7">No existen registros disponibles</td>
            	</tr>
            	</tbody>
            </table>
          </div>
			<div class="overlay" id="ocultaContenedorDetallexEstacion">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
	<div class="row" id="cntDetalleTanque" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
			<div class="box-header">
          	<table style="width:100%;">
          		<tr>
          			<td style="width:10%;">
          			Cliente / Operación
          			</td>
					<td style="width:20%;">
						<input name="cmpClienteOperacionEstacion" id="cmpClienteOperacionEstacion" type="text" class="form-control text-uppercase input-sm" disabled/>
          			</td>
					<td style="padding-left:10px;width:10%;">
						Día Operativo
          			</td>
					<td style="width:8%;">
						<input name="cmpDiaOperativoEstacion" id="cmpDiaOperativoEstacion" type="text" class="form-control text-uppercase input-sm"  disabled/>
          			</td>
					<td style="padding-left:10px;width:10%;">
						Producto
          			</td>
					<td style="width:20%;">
						<input name="cmpProductoEstacion" id="cmpProductoEstacion" type="text" class="form-control text-uppercase input-sm"  disabled/>
          			</td>
					<td style="padding-left:10px;width:8%;">
						Estación
          			</td>
					<td style="width:14%;">
						<input name="cmpNombreEstacion" id="cmpNombreEstacion" type="text" class="form-control text-uppercase input-sm"  disabled/>
          			</td>
          		</tr>
  			</table>
  			</div>
			<div class="box-header">
            <div>
            	<a id="btnRegresarDetalleEstacion" class="btn btn-default btn-sm espaciado"><i class="fa fa-mail-reply"></i> Regresar </a>
            </div>
          	</div>
          <div class="box-body">
            <table id="tablaVistaDetalleTanque" class="sgo-table table table-striped" style="width:100%;">
                <thead>
                <tr>
	                <th style="width:20%;">Tanque</th>
	                <th style="width:8%;">Inventario Final Fisico</th>
	                <th style="width:8%;">Inventario Final Calculado</th>
	                <th style="width:5%;">Variación</th>
	                <th style="width:5%;">Limite Permisible</th>
	                <th style="width:5%;">Faltante</th>
	                <th style="width:10%;">Estado</th>
                </tr>
            	</thead>
            	<tbody>
            	<tr id="filaDetalleTanque" data-fila="0">
            		<td><span  id="vistaDxTTanque"></span></td>
            		<td><span id="vistaDxTStockFinal"></span></td>
            		<td><span id="vistaDxTStockCalculado"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxTVariacion"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxTLimite"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="vistaDxTFaltante"></span></td>
            		<td><span id="vistaDxTEstado"></span></td>
            	</tr>
				<tr id="filaDetalleTanqueVacia">
            		<td colspan="7">No existen registros disponibles</td>
            	</tr>
            	</tbody>
            </table>
          </div>
			<div class="overlay" id="ocultaContenedorDetallexTanque">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>