<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Parametro / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
						<form id="frmBuscar" class="form-inline" role="form">
			              <div class="form-group">
			                <label for="txtFiltro" class="espaciado">Valor: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado input-sm text-uppercase"  placeholder="Buscar..." maxlength="80">
			              </div>
			              <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
			            </form> 
					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
						  <a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
			              <a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
			              <a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
						</div>
					</div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
                <th>#</th>
                <th>Id</th>
                <th>Valor</th>
                <th>Alias</th>
                </tr>
              </thead>
            </table>
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
                <label>Valor</label>
                <input name="cmpValor" id="cmpValor" type="text" class="form-control input-sm text-uppercase" maxlength="80"  required placeholder="Ingresar Valor"/>
              </div>
              <div class="form-group">              
                <label>Alias</label>
                <input name="cmpAlias" id="cmpAlias" type="text" class="form-control input-sm text-uppercase" maxlength="20"  required placeholder="Ingresar Alias"/>
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
						<td><span id='vistaId'></span></td>
					</tr>
					<tr>
						<td>Valor:</td>
						<td><span id='vistaValor'></span></td>
					</tr>
					<tr>
						<td>Alias:</td>
						<td><span id='vistaAlias'></span></td>
					</tr>
					<tr>
						<td>Creado el :</td>
						<td><span id="vistaCreadoEl"></span></td>
					</tr>
					<tr>
						<td>Creado Por :</td>
						<td><span id="vistaCreadoPor"></span></td>
					</tr>
					<tr>
						<td>Actualizado el :</td>
						<td><span id="vistaActualizadoEl"></span></td>
					</tr>
					<tr>
						<td>Actualizado Por :</td>
						<td><span id="vistaActualizadoPor"></span></td>
					</tr>
					<tr>
						<td>IP (creaci&oacute;n) :</td>
						<td><span id="vistaIpCreacion"></span></td>
					</tr>
					<tr>
						<td>IP (Actualizaci&oacute;n) :</td>
						<td><span id="vistaIpActualizacion"></span></td>
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
