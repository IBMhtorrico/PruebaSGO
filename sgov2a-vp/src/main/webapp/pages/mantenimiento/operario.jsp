<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Operarios / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
                  	<td class="celda-detalle" style="width:14%;"><label class="etiqueta-titulo-horizontal">Nombres y/o Apellidos: </label></td>
                  	<td class="celda-detalle" style="width:50%;"><input id="txtFiltro" type="text" class="form-control espaciado input-sm text-uppercase"  placeholder="<%=mapaValores.get("TEXTO_BUSCAR")%>" maxlength="80"></td>
                   	<td></td>
                    <td class="celda-detalle" style="width:6%;"><label class="etiqueta-titulo-horizontal">Estado: </label></td>
        			<td class="celda-detalle" style="width:10%;" >
	        			<select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control espaciado input-sm">
		                  <option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
		                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
		                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
		                </select>
                 	</td>
            		<td></td>
                  	<td><a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a></td>
                </tr>
                </thead>
              </table>
            </form>
          </div>        
          <div class="box-header">
            <div>
              <a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              <a id="btnModificar" class="btn btn-default btn-sm espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
              <a id="btnModificarEstado" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-cloud-upload"></i>  <%=mapaValores.get("ETIQUETA_BOTON_ACTIVAR")%></a>
              <a id="btnVer" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>N#</th>
                <th>ID</th>
                <th>Nombres</th>
                <th>Apellido Paterno</th>
                <th>Apellido Materno</th>
                <th>DNI</th>
                <th>Cliente</th>
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
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Nombres</label></td>
                	</tr>
                	<tr>
                	<td id="cntcmpNombres" class="celda-detalle"><input name="cmpNombres" id="cmpNombres" type="text" class="form-control text-uppercase input-sm" required placeholder="Ingresar Nombres" maxlength="80"/></td>
                	</tr>
                	<tr>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Apellido Paterno</label>
                	</tr>
                	<tr>
                	<td id="cntcmpApellidoPaterno" class="celda-detalle"><input name="cmpApellidoPaterno" id="cmpApellidoPaterno" type="text" class="form-control text-uppercase input-sm" required placeholder="Ingresar Apellido Paterno" maxlength="80"/>
                	</tr>
                	<tr>
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Apellido Materno</label>
                	</tr>
                	<tr>
                	<td id="cntcmpApellidoMaterno" class="celda-detalle"><input name="cmpApellidoMaterno" id="cmpApellidoMaterno" type="text" class="form-control text-uppercase input-sm" required placeholder="Ingresar Apellido Materno" maxlength="80"/>
              		</tr>
                	<tr>
              		<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">D.N.I.</label></td>
                	</tr>
                	<tr>
                	<td id="cntcmpDni" class="celda-detalle"><input name="cmpDni" id="cmpDni" type="text" class="form-control text-uppercase input-sm" required placeholder="Ingresar DNI" maxlength="8"/></td>
              		</tr>
              		<tr>  
                	<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Cliente</label></td>
                	</tr>
                	<tr>
                	<td id="cntcmpCliente" class="celda-detalle"><select style="width:100%;" tipo-control="select2" id="cmpCliente" name="cmpCliente" class="form-control input-sm" >
                		<option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option></select></td>
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
                  <td>Nombres:</td>
                  <td>
                    <span id="vistaNombres"></span>
                  </td>
                </tr>
                <tr>
                  <td>Apellido Paterno:</td>
                  <td>
                    <span id="vistaApellidoPaterno"></span>
                  </td>
                </tr>
                <tr>
                  <td>Apellido Materno:</td>
                  <td>
                    <span id="vistaApellidoMaterno"></span>
                  </td>
                </tr>
                <tr>
                  <td>D.N.I.:</td>
                  <td>
                    <span id="vistaDni"></span>
                  </td>
                </tr>
                <tr>
                  <td>Cliente:</td>
                  <td>
                    <span id="vistaCliente"></span>
                  </td>
                </tr>  
                <tr>
                  <td>Estado:</td>
                  <td>
                    <span id="vistaEstado"></span>
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