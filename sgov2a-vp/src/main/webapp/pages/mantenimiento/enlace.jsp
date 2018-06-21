<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Enlace / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
			                <label for="txtFiltro" class="espaciado">Url Completa: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado text-uppercase input-sm"  placeholder="Buscar..." maxlength="100">
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
                <th>Url Completa</th>
                <th>Url Relativa</th>
                <th>Orden</th>
                <th>Padre</th>
                <th>Tipo</th>
                <th>Permiso</th>                
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
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <form id="frmPrincipal" role="form">
             
              <div class="form-group">              
                <label>Titulo</label>
                <input name="cmpTitulo" id="cmpTitulo" type="text" class="form-control input-sm" maxlength="80"  required placeholder="Ingresar titulo"/>
              </div>
              <div class="form-group">              
                <label>Url Completa</label>
				<input name="cmpUrlCompleta" id="cmpUrlCompleta" type="text" class="form-control input-sm"  maxlength="100" required placeholder="Ingresar url completa"/>
              </div>
              <div class="form-group">              
                <label>Url Relativa</label>
                <input name="cmpUrlRelativa" id="cmpUrlRelativa" type="text" class="form-control input-sm"  maxlength="100" required placeholder="Ingresar url relativa"/>
              </div>
              <div class="form-group">              
                <label>Orden</label>
                <input name="cmpOrden" id="cmpOrden" type="text" class="form-control input-sm" required placeholder="Ingresar orden" maxlength="4"/>
              </div>
              <div class="form-group">              
                <label>Padre</label>
                <input name="cmpPadre" id="cmpPadre" type="text" class="form-control input-sm" required placeholder="Ingresar padre" maxlength="4"/>
              </div>
              <div class="form-group">              
                <label>Tipo</label>
                <input name="cmpTipo" id="cmpTipo" type="text" class="form-control input-sm" required placeholder="Ingresar tipo" maxlength="1"/>
              </div>
              <div class="form-group">
                <label>Permiso</label>
                <select tipo-control="select2" id="cmpPermiso" name="cmpPermiso" class="form-control input-sm" style="width: 100%">
                  <option value="" selected="selected">Seleccionar</option>
                </select>
              </div>

            </form>
          </div>
         <div class="box-footer">
            <a id="btnGuardar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnCancelarGuardar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
          </div>
          <div class="overlay" id="ocultaContenedorFormulario" style="display:none;">
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
						<td>Titulo:</td>
						<td><span id='vistaTitulo'></span></td>
					</tr>
					<tr>
						<td>Url Completa:</td>
						<td><span id='vistaUrlCompleta'></span></td>
					</tr>
					<tr>
						<td>Url Relativa:</td>
						<td><span id='vistaUrlRelativa'></span></td>
					</tr>
					<tr>
						<td>Orden:</td>
						<td><span id='vistaOrden'></span></td>
					</tr>
					<tr>
						<td>Padre:</td>
						<td><span id='vistaPadre'></span></td>
					</tr>
					<tr>
						<td>Tipo:</td>
						<td><span id='vistaTipo'></span></td>
					</tr>
					<tr>
						<td>Permiso:</td>
						<td><span id='vistaPermiso'></span></td>
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