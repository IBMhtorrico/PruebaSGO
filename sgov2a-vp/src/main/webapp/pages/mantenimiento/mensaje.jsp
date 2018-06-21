<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Mensaje<small> - Listado</small> </h1>
	</section>
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<div id="bandaInformacion" class="callout callout-success">
					Cargando...
				</div>
			</div>
		</div>
		<div class="row" id="cntTabla">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-header">
						<form id="frmBuscar" class="form-inline" role="form">
			              <div class="form-group">
			                <label for="txtFiltro" class="espaciado">T&iacute;tulo: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado text-uppercase"  placeholder="Buscar...">
			              </div>
			              <a id="btnFiltrar" class="btn btn-default"><i class="fa fa-refresh"></i>  Filtrar</a>
			            </form> 
					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
							<a id="btnAgregar" class="btn btn-default espaciado"><i class="fa fa-plus"></i> Agregar</a> 
							<a id="btnModificar" class="btn btn-default disabled espaciado"><i class="fa fa-edit"></i> Modificar</a> 
							<a id="btnVer" class="btn btn-default disabled espaciado"><i class="fa fa-search"></i> Ver</a>
						</div>
					</div>
          <div class="box-body">
            <table id="tablaPrincipal" class="table table-striped table-bordered responsive" width="100%">
              <thead>
                <tr>
                <th>#</th>
                <th>Id</th>
                <th>T&iacute;tulo</th>
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
          <div class="box-header">
             <h3 id="cmpTituloFormulario" class="box-title">uii</h3>
          </div>
          <div class="box-body">
            <form id="frmPrincipal" role="form">
             
              <div class="form-group">              
                <label>T&iacute;tulo</label>
                <input name="cmpTitulo" id="cmpTitulo" type="text" class="form-control text-uppercase" maxlength="20" required placeholder="Ingresar titulo"/>
              </div>
            </form>
          </div>
          <div class="box-footer">
            <button id="btnGuardar" type="submit" class="btn btn-primary">Guardar</button>
            <button id="btnCancelarGuardar"  class="btn btn-danger">Cancelar</button>
          </div>
          <div class="overlay" id="ocultaFormulario" style="display:none;">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="cntVistaRegistro" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
        	<div class="box-header with-border">
        		<h3 class="box-title">Detalle del registro</h3>
        	</div>
       <div class="box-body">
			<table class="table table-bordered table-striped">
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
            <button id="btnCerrarVista"  class="btn btn-danger">Cerrar</button>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>