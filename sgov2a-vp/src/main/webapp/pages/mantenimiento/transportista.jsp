<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Transportistas / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
			              <div class="form-group">
			                <label for="txtFiltro" class="espaciado">Raz&oacute;n Social: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado input-sm text-uppercase"  placeholder="Buscar..." maxlength="150">
			              </div>
			              <div class="form-group">
			                <label for="cmpFiltroEstado" class="espaciado">Estado: </label>
			                <select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control espaciado input-sm">
			                  <option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
			                </select>
			              </div>
			              <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
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
 			 	<th>N#</th>
                <th>ID</th>
                 <th>Raz&oacute;n Social</th>
                 <th>Nombre Corto</th>
                 <th>RUC</th>
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
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <form id="frmPrincipal" role="form">
               <div class="form-group">
                <label>Raz&oacute;n Social</label>
                <input name="cmpRazonSocial" id="cmpRazonSocial" type="text" class="form-control input-sm text-uppercase" maxlength="150" required placeholder="Ingresar Raz&oacute;n Social"/>
                </div>
               <div class="form-group">
                <label>Nombre Corto</label>
                <input name="cmpNombreCorto" id="cmpNombreCorto" type="text" class="form-control input-sm text-uppercase" maxlength="20" required placeholder="Ingresar Nombre Corto"/>
                </div>
               <div class="form-group">
                <label>RUC</label>
                <input name="cmpRuc" id="cmpRuc" type="text" class="form-control input-sm" required placeholder="Ingresar RUC" maxlength="11"/>
                </div>
               <div class="form-group">
                <label>Estado</label>
                <select id="cmpEstado" name="cmpEstado" class="form-control input-sm">
                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
                </select>
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
                <td>Id:</td>
                <td>
                <span id='vistaId'></span>
                </td>
             </tr> 
              <tr>
                <td>Raz&oacute;n Social:</td>
                <td>
                <span id='vistaRazonSocial'></span>
                </td>
              </tr>              
              <tr>
                <td>Nombre Corto:</td>
                <td>
                <span id='vistaNombreCorto'></span>
                </td>
              </tr>              
              <tr>
                <td>RUC:</td>
                <td>
                <span id='vistaRuc'></span>
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