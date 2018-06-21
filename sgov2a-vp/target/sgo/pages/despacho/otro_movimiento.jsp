<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Otros Movimientos / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
  </section>
  <section class="content" id="cntInterface">
  	<!--------------------------------------------------------------------- MENSAJE ----------------------------------------------------------------------->
  	<div class="row">
      <div class="col-xs-12">
      	<div id="bandaInformacion" class="callout callout-success">
          <%=mapaValores.get("MENSAJE_CARGANDO")%>
      	</div>
      </div>
    </div>
    <!--------------------------------------------------------------------- LISTADO ----------------------------------------------------------------------->
    <div class="row" id="cntTabla">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form" novalidate="novalidate">
              <table class="sgo-simple-table table table-condensed">
                <thead>
                  <tr>
                    <td class="celda-detalle" style="width:12%;"><label class="etiqueta-titulo-horizontal">Operación / Cliente: </label></td>
        			<td class="celda-detalle" ><select style="width:100%;" id="filtroOperacion" name="filtroOperacion" class="form-control espaciado input-sm" >
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
		                    <option <%=seleccionado%> data-operacion-cliente='<%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %>' data-idOperacion='<%=eOperacion.getId()%>' data-fecha-actual='<%=fechaActual%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
		                    <%
	                    } %>
                    </select></td> 
                    <td></td>
                    <td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Estación: </label></td>
                    <td  class="celda-detalle" style="width:20%;"><select style="width:100%;"  id="cmpFiltroEstacion" name="cmpFiltroEstacion" class="form-control input-sm">
                    	<option value="0" selected="selected">Seleccionar...</option>
                    </select></td>
        			<td></td>
        			<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Día Operativo: </label></td>
	                <td class="celda-detalle" style="width:15%;"><input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" /></td>
            		<td></td>
                  	<td><a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a></td>
                </tr>
                </thead>
              </table>
            </form>
          </div>        
          <div class="box-header">
            <div>
              <a id="btnAgregarOtroMovimiento" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              <a id="btnModificarOtroMovimiento" class="btn btn-default btn-sm espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
              <a id="btnVerOtroMovimiento" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>target0</th>
                <th>id</th>
                <th>Estación</th>
                <th>Día Operativo</th>
                <th>Estado Día Operativo</th>
                <th>Nro Movimiento</th>
                <th>Clasificación</th>
                <th>Volumen</th>                
                </tr>
              </thead>
            </table>			
          </div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <!--------------------------------------------------------------------- FORMULARIO ----------------------------------------------------------------------->
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
           <div class="box-body">
            <form id="frmPrincipal">
              <table class="sgo-simple-table table table-condensed">
	              <thead>
				  </thead>
				  <tbody>
				  	<tr>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Operaci&oacute;n/Cliente</label></td>
                	<td id="cntcmpOperacionCliente" class="celda-detalle" style="width:20%;"><input name="cmpOperacionCliente" id="cmpOperacionCliente" type="text" class="form-control text-uppercase input-sm" readonly/></td>
                	<td></td>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Estaci&oacute;n</label>
                	<td id="cntcmpEstacion" class="celda-detalle" style="width:25%;"><input name="cmpEstacion" id="cmpEstacion" type="text" class="form-control text-uppercase input-sm" readonly />
                	<td></td>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">D&iacute;a Operativo</label>
                	<td id="cntcmpDiaOperativo" class="celda-detalle" style="width:25%;"><input name="cmpDiaOperativo" id="cmpDiaOperativo" type="text" class="form-control input-sm" readonly/>
              		</tr>
              		<tr>
              		<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Tipo</label></td>
              		<td id="cntcmpTipo" class="celda-detalle" ><select name="cmpTipo" id="cmpTipo" class="form-control input-sm" >
		              <option value="1">ENTRADA</option>
		              <option value="2">SALIDA</option>
		            </select></td>
		            <td colspan="6"></td>
              		</tr>
                	<tr>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Clasificaci&oacute;n</label></td>
                	<td id="cntcmpClasificacion" class="celda-detalle" ><select style="width:100%;" name="cmpClasificacion" id="cmpClasificacion" class="form-control input-sm" >
		              <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
		              <option value="1">RECIRCULACIÓN</option>
		              <option value="2">CALIBRACIÓN</option>
		              <option value="3">TRASIEGO</option>
		              <option value="4">OTROS MOVIMIENTOS</option>
		            </select></td>
		            <td></td>
		            <td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Tanque Origen</label></td>  
		            <td id="cntcmpTanqueOrigen" class="celda-detalle" style="width:25%;"><select style="width:100%;" tipo-control="select2" id="cmpTanqueOrigen" name="cmpTanqueOrigen" class="form-control input-sm" >
                      <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
                	</select></td>
                	<td></td>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Tanque Destino</label></td>
                	<td id="cntcmpTanqueDestino" class="celda-detalle" style="width:25%;"><select style="width:100%;" tipo-control="select2" id="cmpTanqueDestino" name="cmpTanqueDestino" class="form-control input-sm" >
                      <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
                	</select></td>
              		</tr>
              		<tr>
              		<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Volumen</label></td>
                	<td id="cntcmpVolumen" class="celda-detalle"><input name="cmpVolumen" id="cmpVolumen" type="text" class="form-control text-uppercase input-sm" required placeholder="Ingresar Volumen" maxlength="6"/></td>
              		<td colspan="6"></td>
              		</tr>
              		<tr>  
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Comentario</label></td>
                	<td colspan="7" id="cntcmpComentario" class="celda-detalle"><textarea class="form-control input-sm text-uppercase" required id="cmpComentario" name="cmpComentario" placeholder="Ingrese Comentario..." rows="3" maxlength="500"></textarea></td>
             		</tr>
              </table>
            </form>
          </div>
          <div class="box-footer">
            <a id="btnGuardar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i><%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i><%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
          </div>
          <div class="overlay" id="ocultaContenedorFormulario">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <!--------------------------------------------------------------------- VISTA ----------------------------------------------------------------------->
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
                  <td>Operaci&oacute;n/Cliente:</td>
                  <td>
                    <span id="vistaOperacionCliente"></span>
                  </td>
                </tr>
                <tr>
                  <td>Estaci&oacute;n:</td>
                  <td>
                    <span id="vistaEstacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>D&iacute;a Operativo:</td>
                  <td>
                    <span id="vistaDiaOperativo"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tipo:</td>
                  <td>
                    <span id="vistaTipo"></span>
                  </td>
                </tr>
                <tr>
                  <td>Clasificaci&oacute;n:</td>
                  <td>
                    <span id="vistaClasificacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tanque Origen:</td>
                  <td>
                    <span id="vistaTanqueOrigen"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tanque Destino:</td>
                  <td>
                    <span id="vistaTanqueDestino"></span>
                  </td>
                </tr>
                <tr>
                  <td>Volumen:</td>
                  <td>
                    <span id="vistaVolumen"></span>
                  </td>
                </tr>
                <tr>
                  <td>Comentario:</td>
                  <td>
                    <span id="vistaComentario"></span>
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