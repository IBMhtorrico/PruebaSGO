<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Tracto"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); %>
<div class="content-wrapper">
	<section class="content-header">
		<h1> Aforo de Cisterna / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
	</section>
	<section class="content" id="cntInterface">
		<div class="row">
			<div class="col-xs-12">
				<div id="bandaInformacion" class="callout callout-success">
					<%=mapaValores.get("MENSAJE_CARGANDO_EXITOSO")%>
				</div>
			</div>
		</div>
    
		<div class="row" id="cntTabla">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-header">
          <form id="frmFiltroAforoCisterna" name="frmFiltroAforoCisterna" class="form" novalidate="novalidate">
            <table>
              <thead>
                <tr>
                  <td style="width:10%;">
                  <label id="lblFiltroTracto" class="etiqueta-titulo-horizontal">Tracto: </label>
                  </td>
                  <td style="width:15%;">
                      <select id="cmpFiltroTracto" class="form-control input-sm" >
                        <option></option>
                        <%
                        ArrayList<?> listaTractos = (ArrayList<?>) request.getAttribute("listaTractos");
                        int numeroRegistros = listaTractos.size();
                        Tracto eTracto = null;
                        for (int indice = 0; indice < numeroRegistros; indice++) {
                        eTracto = (Tracto) listaTractos.get(indice);
                        %>
                        <option value='<%=eTracto.getId()%>'><%=eTracto.getPlaca()%></option>
                        <%
                        }
                        %>
                    </select>
                 </td>
                  <td style="width:10%; padding-left:10px;">
                    <label id="lblFiltroCisterna" class="etiqueta-titulo-horizontal">Cisterna: </label>
                  </td>
                  <td style="width:15%;">
                     <select id="cmpFiltroCisterna"class="form-control input-sm" >
                        <option></option>
                    </select>
                  </td>
                  <td style="width:15%; padding-left:10px;">
                     <label id="lblFiltroCompartimento" class="etiqueta-titulo-horizontal">Compartimento: </label>
                  </td>
                  <td style="width:11%;">
                    <select id="cmpFiltroCompartimento" name="filtroCompartimento" class="form-control input-sm" >
                      <option></option>
                    </select>
                  </td>
                  <td style="width:9%;padding-left:10px;">
                  <label id="lblFiltroAltura" class="etiqueta-titulo-horizontal">Altura : </label>
                  </td>
                  <td style="width:10%;">
                    <input id="cmpFiltroAltura" type="text" class="form-control input-sm" value="" maxlength="4"/>
                  </td>
                  <td style="width:5%;padding-left:10px;">
                    <a id="btnFiltrarAforo" class="btn btn-sm btn-default espaciado col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
                  </td>
                </tr>
              </thead>
            </table>     
          </form>
 					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
						  <a id="btnAgregar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
			              <a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
			               <a id="btnImportar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  Importar</a>
			              <a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
						</div>
					</div>
					<div class="box-body">
						<table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
							<thead>
								<tr>
									<th>#</th>
									<th>ID</th>
									<th>Altura</th>
									<th>Volumen</th>
									<th>Variacion (mm)</th>
									<th>Variacion (gal)</th>
								</tr>
							</thead>
						</table>
						<div id="frmConfirmarModificarEstado" class="modal" data-keyboard="false" data-backdrop="static">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
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
		<div class="row" id="cntFormulario" style="display: none;">
			<div class="col-md-12">
				<div class="box box-default">
					<div class="box-body">
						<form id="frmPrincipal" role="form">
							<div class="form-group row">
                <div class="col-md-2">
								<label  class="control-label" for="cmpPlacaTracto">Tracto</label> 
								<input id="cmpPlacaTracto" type="text" class="form-control text-uppercase input-sm" readonly  />
                </div>
                <div class="col-md-2">
								<label  class="control-label" for="cmpPlacaTracto">Cisterna</label>  
                <input id="cmpPlacaCisterna" type="text" class="form-control text-uppercase input-sm" readonly />
                </div>
                <div class="col-md-2">
								<label  class="control-label" for="cmpCompartimento">Compartimento</label>  
                <input id="cmpCompartimento" type="text" class="form-control text-uppercase input-sm" readonly />
                </div>
                <div class="col-md-3">
								<label>Altura Flecha</label> 
								<input id="cmpAlturaFlecha" type="text" class="form-control text-uppercase input-sm" readonly/>
                </div>
                <div class="col-md-3">
								<label>Capacidad Flecha</label> 
								<input id="cmpCapacidad" type="text" class="form-control text-uppercase input-sm" readonly/>
                </div>
							</div>					
              <div class="form-group row">
                <div class="col-md-3">
                  <label>Variaci&oacute;n Altura</label> 
                  <input id="cmpVariacionAltura" name="cmpVariacionAltura" type="text" class="form-control text-uppercase input-sm" required  maxlength="4"/>
                </div>
                <div class="col-md-3">
                  <label>Variaci&oacute;n Volumen</label> 
                  <input id="cmpVariacionVolumen" name="cmpVariacionVolumen" type="text" class="form-control text-uppercase input-sm" required maxlength="5"/>
                </div>
                <div class="col-md-3">
                  <label>Altura</label> 
                  <input id="cmpAltura" type="text" class="form-control text-uppercase input-sm" readonly />
                </div>
                <div class="col-md-3">
                  <label>Volumen</label> 
                  <input id="cmpVolumen" type="text" class="form-control text-uppercase input-sm" readonly />
                </div>           
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
		
	<div class="row" id="cntFormularioImportar" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
           <div class="box-body">
            <form id="frmPrincipal">
              <div class="form-group" id="cntcmpRazonSocial">
                <label>Tracto / Cisterna</label>
                <input name="cmpTractoCisternaImportar" id="cmpTractoCisternaImportar" type="text" class="form-control text-uppercase input-sm" disabled maxlength="150" />
              </div>
				<div class="form-group" id="cntcmpRazonSocial">
                	<label>Compartimento</label>
                	<input name="cmpCompartimentoImportar" id="cmpCompartimentoImportar" type="text" class="form-control text-uppercase input-sm" disabled maxlength="150" />
              	</div>
              <div class="form-group" id="cntcmpRazonSocial">
                <label>Archivo:</label>
                <input name="cmpArchivo" id="cmpArchivo" type="file" />
              </div>
              <div class="checkbox">
                  <label>
                    <input id="chkBorrar" type="checkbox"> Borrar y Reemplazar
                  </label>
              </div>
            </form>
          </div>
          <div class="box-footer">
            <a id="btnCargar" class="btn btn-primary btn-sm"><i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
            <a id="btnCancelarCargar" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
          </div>
          <div class="overlay" id="ocultaContenedorFormularioImportar">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
		
		<div class="row" id="cntVistaRegistro" style="display: none;">
			<div class="col-md-12">
				<div class="box box-default">
					<div class="box-header with-border">
						<h3 class="box-title">Detalle del registro</h3>
					</div>
					<div class="box-body">          
						<table class="sgo-table table table-striped" style="width:100%;">
							<tbody>
								<tr>
									<td>Id:</td>
									<td><span id='vistaId'></span></td>
								</tr>
								<tr>
									<td>Placa Tracto:</td>
									<td><span id='vistaPlacaTracto'></span></td>
								</tr>
								<tr>
									<td>Placa Cisterna:</td>
									<td><span id='vistaPlacaCisterna'></span></td>
								</tr>
								<tr>
									<td>N° Compartimento:</td>
									<td><span id='vistaNumeroCompartimento'></span></td>
								</tr>
								<tr>
									<td>Altura Flecha:</td>
									<td><span id='vistaAlturaFlecha'></span></td>
								</tr>
								<tr>
									<td>Capacidad Volumetrica</td>
									<td><span id='vistaCapacidadVolumetrica'></span></td>
								</tr>
                <tr>
									<td>Variación Altura(mm):</td>
									<td><span id='vistaVariacionAltura'></span></td>
								</tr>
								<tr>
									<td>Variación Volumen(gal):</td>
									<td><span id='vistaVariacionVolumen'></span></td>
								</tr>
                <tr>
									<td>Altura Aforada (mm):</td>
									<td><span id='vistaAlturaAforada'></span></td>
								</tr>
								<tr>
									<td>Volumen Aforado(gal):</td>
									<td><span id='vistaVolumenAforado'></span></td>
								</tr>
								<tr>
									<td>Creado el:</td>
									<td><span id='vistaCreadoEl'></span></td>
								</tr>
								<tr>
									<td>Creado por:</td>
									<td><span id='vistaCreadoPor'></span></td>
								</tr>
								<tr>                
									<td>Actualizado por:</td>
									<td><span id='vistaActualizadoPor'></span></td>
								</tr>
								<tr>
									<td>Actualizado El:</td>
									<td><span id='vistaActualizadoEl'></span></td>
								</tr>
								<tr>
									<td>IP (Creación):</td>
									<td><span id='vistaIpCreacion'></span></td>
								</tr>
								<tr>
									<td>IP (Actualizacion):</td>
									<td><span id='vistaIpActualizacion'></span></td>
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