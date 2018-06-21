<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>

<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Registro de Transporte / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
  </section>
  <!-- Contenido principal -->
  <section class="content">
  <!-- El contenido debe incluirse aqui-->
    <div class="row">
      <div class="col-md-12">
        <div id="bandaInformacion" class="callout callout-info"><%=mapaValores.get("MENSAJE_CARGANDO")%></div>
      </div>
    </div>
  <div class="row" id="cntTabla">
    <div class="col-md-12">
      <div class="box">
        <div class="box-header">
          <form id="frmBuscar" class="form" role="form"  novalidate="novalidate">
            <div class="row">
              <div class="col-md-6">
                <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                  <label class="etiqueta-titulo-horizontal">Operación / Cliente: </label>
                </div>
                <div class="col-md-8" style="padding-left: 4px;padding-right: 4px" >
                  <select id="filtroOperacion" name="filtroOperacion" class="form-control" style="width:100%;">
                  <%
                  ArrayList<Operacion> listaOperaciones = (ArrayList<Operacion>) request.getAttribute("operaciones"); 
                  String fechaActual = (String) request.getAttribute("fechaActual"); 
                  
                  int numeroOperaciones = listaOperaciones.size();
                  int indiceOperaciones=0;
                  Operacion eOperacion=null;
                  Cliente eCliente=null;
                  String seleccionado="selected='selected'";
                  for(indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
                  eOperacion = listaOperaciones.get(indiceOperaciones);
                  eCliente = eOperacion.getCliente();
                  %>
                  <option data-id-cliente="<%=eCliente.getId()%>" <%=seleccionado%> data-fecha-actual='<%=fechaActual%>' data-volumen-promedio-cisterna='<%=eOperacion.getVolumenPromedioCisterna()%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
                  <%
                  seleccionado="";
                  } %>
                  </select>
                </div>
              </div>
              <div class="col-md-4">
                <div class="col-md-4" style="padding-left: 4px;padding-right: 4px">
                  <label class="etiqueta-titulo-horizontal">F. Planificada: </label>
                </div>
                <div class="col-md-8" style="padding-left: 4px;padding-right: 4px">
                  <input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" />
                </div>
              </div>
              <div class="col-md-2">
                  <a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
              </div>
            </div>
          </form>
        </div>

        <div class="box-header">
          <div>
            <a id="btnDetalle" class="btn btn-default btn-sm espaciado"><i class="fa fa-search"></i>  Detalle</a>
          </div>
        </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" width="100%">
              <thead>
                <tr>
 					<th>#</th>
 					<th>ID</th>
                 	<th>F. Planificaci&oacute;n</th>
                 	<th>Tot. Cis. Planificadas</th>
                 	<th>Tot Cis. Asignadas</th>
                 	<th>U.  Actualizaci&oacute;n</th>
                 	<th>Usuario</th>
					<th>Estado</th>
					<th>operacion</th>
					<th>cliente</th>
					<th>idCliente</th>
					<th>idOperacion</th>
					<th>fechaEstimadaCarga</th>
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

	<div class="row" id="cntDetalleTransporte" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-header with-border">
					<h3  id="cmpTituloFormulario" class="box-title"></h3>
				</div>
				<div class="box-header with-border">
					<div class="col-xs-12">
						<div class="row" style="padding: 4px;">
							<div class="col-md-1">	<label>Cliente:</label>						</div>
							<div class="col-md-3">	<span id='detalleCliente'>	</span> 		</div>
							<div class="col-md-1">	<label>Operaci&oacute;n:</label>			</div>
							<div class="col-md-3">	<span id='detalleOperacion'></span> 		</div>
							<div class="col-md-2">	<label> F. Planificaci&oacute;n:</label>	</div>
							<div class="col-md-2"><FONT COLOR=blue><B>	<span id='detalleFechaPlanificacion'></span></B></FONT></div>
						</div>
						<div  class="col-md-12">
							<table id="tablaDetalleDiaOperativo" class="sgo-table table table-striped" width="100%">
			       				<thead>
			          					<tr>
										<th class="text-center">#</th>
										<%-- Comentado por 9000002608
										<th class="text-center">Producto</th>
										--%>
										<th class="text-center">Vol. Solicitado </th>
								    	<th class="text-center">Cist. Solicitados</th>
								    	<th class="text-center">Vol. Asignados</th>
								    	<th class="text-center">Cist. Asignados</th>
			          					</tr>
			        				</thead>
       						</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-12">
			<div class="box box-default">
	   			<div class="box-header">
          			<div>
           				<a id="btnAgregarTransporte" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  Agregar</a>
           				<a id="btnModificarTransporte" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-edit"></i>  Modificar</a>
			            <a id="btnImportar" class="btn btn-default btn-sm espaciado"><i class="fa fa-download"></i>  Importar</a>
			            <a id="btnVer" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  Ver</a>
			            <a id="btnEvento" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-eye"></i> Evento</a>
						<a id="btnPesaje" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-truck"></i> Pesaje</a>
						<%--Inicio Agregado por 9000002570 --%>
						<a id="btnTiempoEtapa" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-truck"></i> Tiempos por Etapa</a>
						<%--Fin Agregado por 9000002570 --%>
          			</div>
        		</div>
          		<div class="box-body">
	   				<!-- <table id="listado_transportes" class="table table-bordered table-striped"></table> -->
	   				
	   				<table id="tablaAsignacionTransporte" class="sgo-table table table-striped" width="100%">
        				<thead>
           					<tr>
								<th class="text-center">#</th>
								<th class="text-center">ID</th>
								<th class="text-center">G/R</th>
						    	<th class="text-center">F. Emisi&oacute;n</th>
						    	<th class="text-center">Cisterna / Tracto</th>
						    	<th class="text-center">SCOP</th>
						    	<th class="text-center">Vol. [T. Obs.]</th>
						    	<th class="text-center">Vol. [60F]</th>
						    	<th class="text-center">Origen</th>
								<th class="text-center">Estado</th>
								<th class="text-center">NumeroOrdenCompra</th>
           					</tr>
         				</thead>
       				</table>
	   			</div>
	   			<div class="box-footer" align="right">
					<button id="btnCerrarDetalleTransporte" class="btn btn-sm espaciado btn-danger">Cerrar</button>
				</div>
				<div class="overlay" id="ocultaContenedorDetalleTransporte">
			    	<i class="fa fa-refresh fa-spin"></i>
			    </div>
	   		</div>
		</div>
	</div>

  <div class="row" id="cntFormularioImportar" style="display: none;">
    <div class="col-md-12">
      <div class="box box-default">
        <div class="box-header">
          <table style="width:100%;">
            <tr>
              <td> 
              Cliente / Operación 
              </td>
              <td>
                <span id="lblNombreCliente" style="font-weight:bold;">Cliente</span>/ <span id="lblNombreOperacion" style="font-weight:bold;">Operación</span>
              </td>
              <td> 
              F. Planificación
              </td>
              <td>
                <span id="lblFechaPlanificacion" style="font-weight:bold;">12/12/2016</span>
              </td>
				<td> 
              F.Emisión Guia
              </td>
              <td style="padding-right:10px;">
               <input style="width:60%;" name="cmpFiltroFechaInicialImportacion" id="cmpFiltroFechaInicialImportacion" type="text" class="form-control input-sm text-right"/>
              </td>
 				<td colspan="1" style="text-align:right;">
                	<a id="btnFiltrarSap" class="btn btn-default btn-sm"><i class="fa fa-search"></i> Consultar SAP</a>
              </td>
            </tr>
          </table>
        </div>
        <div class="box-body">
          <table id="tablaListaSap" class="sgo-table table table-striped" width="100%">
          <thead>
            <tr>
            <td> </td>
            <th>G/R</th>
            <th>F. Emisión </th>
            <th>Tracto/Cisterna</th>
            <th>Vol [T. Obs.]</th>
            <th>Vol [60F]</th>
            <th>SCOP</th>
            <th>Situacion</th>
            </tr>
          </thead>
          <tbody>
            <tr id="plantillaNoRegistro">
              <td colspan="8">
              Sin registros
              </td>
            </tr>
            <tr id="plantillaDetalleSap" data-fila="0">
            		<td style="padding-left:10px;"><input name="cmpSelectorDetalle" id="cmpSelectorDetalle" type="checkbox" name="selector" value="0" style="margin:0;"></td>
            		<td style="padding-left:10px;"><span style="line-height:10px;" id="cmpDetalleNumeroGuia"></span></td>
            		<td style="padding-left:10px;"><span id="cmpFechaEmision"></span></td>
            		<td style="padding-left:10px;"><span id="cmpTractoCisterna"></span></td>                	
            		<td style="text-align:right;padding-right:10px;"><span id="cmpVolumenObservado"></span></td>
            		<td style="text-align:right;padding-right:10px;"><span id="cmpVolumenCorregido"></span></td>
            		<td style="padding-left:10px;"><span id="cmpSCOP"></span></td>
            		<td style="padding-left:10px;"><span id="cmpSituacion"></span></td>
            	</tr>
          </tbody>
          </table>
        </div>
        <div class="box-footer">
						<button id="btnImportarTransporte" type="submit" class="btn btn-primary btn-sm">Guardar</button>
						<button id="btnCancelarImportarTransporte" class="btn btn-danger btn-sm">Regresar</button>
				</div>
				<div class="overlay" id="ocultaContenedorFormularioImportar" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
      </div>
    </div>
  </div>
  
	<div class="row" id="cntFormulario" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-header">
					<form id="frmPrincipal" class="form form-horizontal" role="form form-horizontal" novalidate="novalidate">
						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Cliente: </label></td>
			      				<td class="celda-detalle" ><span id="cmpFormularioCliente" class="espaciado input-sm text-uppercase" readonly></span> </td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Operación: </label></td>
			      				<td class="celda-detalle" ><span id="cmpFormularioOperacion" class="espaciado input-sm text-uppercase" readonly /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">F.Planificada: </label> </td>
			      				<td class="celda-detalle"  style="width:15%;"><span id="cmpFormularioFechaPlanificacion" type="text" class="form-control alert-danger text-center espaciado input-sm text-uppercase" readonly /></td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
			      			
		      			<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      			</thead>
			      			<tbody>
			      				<tr>
			      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">G/R: </label> </td>
			      				<td class="celda-detalle" style="width:18%;"><input name="cmpNumeroGuiaRemision" id="cmpNumeroGuiaRemision" maxlength="20" type="text" class="form-control espaciado input-sm" required placeholder="" /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">O/E: </label> </td>
			      				<td class="celda-detalle"  style="width:13%;"><input name="cmpNumeroOrdenCompra" id="cmpNumeroOrdenCompra" maxlength="20" type="text" class="form-control espaciado input-sm" required placeholder="" /></td>
			      				<td></td>
			      				
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Factura: </label></td>
			      				<td class="celda-detalle"  style="width:13%;"><input name="cmpNumeroFactura" id="cmpNumeroFactura" maxlength="15" type="text" class="form-control espaciado input-sm" placeholder="" /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">SCOP: </label></td>
			      				<td class="celda-detalle" style="width:19%;"><input name="cmpCodigoScop" id="cmpCodigoScop" maxlength="20" type="text" class="form-control espaciado input-sm"  placeholder="" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Planta: </label> </td>
			      				<td class="celda-detalle" style="width:18%;"><select tipo-control="select2" id="cmpPlantaDespacho" name="cmpPlantaDespacho" class="form-control espaciado text-uppercase input-sm" required style="width: 100%">
										<option value="" selected="selected">Seleccionar</option>
									</select>
								</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Transportista: </label></td>
			      				<td colspan="4" class="celda-detalle"><select tipo-control="select2" id="cmpTransportista" name="cmpTransportista" class="form-control espaciado text-uppercase input-sm" required style="width: 100%">
																				<option value="" selected="selected">Seleccionar</option>
																			</select> 
								</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Cisterna/Tracto: </label></td>
			      				<td class="celda-detalle" style="width:15%;"><select tipo-control="select2" id="cmpCisternaTracto" name="cmpCisternaTracto" class="form-control espaciado input-sm" required style="width: 100%">
																				<option value="" selected="selected">Seleccionar</option>
																			</select>
								</td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Conductor: </label> </td>
			      				<td class="celda-detalle" colspan="7"><select tipo-control="select2" id="cmpConductor" name="cmpConductor" class="form-control espaciado text-uppercase input-sm" required style="width: 100%">
																				<option value="" selected="selected">Seleccionar</option>
																			</select>
								</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">F. Emisi&oacute;n O/E: </label></td>
			      				<td class="celda-detalle" style="width:15%;"><input name="cmpFemisionOE" id="cmpFemisionOE" type="text" class="form-control espaciado input-sm"  required data-inputmask="'alias': 'dd/mm/yyyy'" placeholder="" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Precintos: </label> </td>
			      				<td class="celda-detalle" style="width:18%;"colspan="10"><input name="cmpPrecintos" id="cmpPrecintos" maxlength="180" type="text" class="form-control espaciado input-sm text-uppercase text-uppercase" placeholder="" />
			      				</tr>
			      				
			      		</tbody>
			      		</table>
						<div class="col-xs-12" id="contenedorDetalles">
							<table class="sgo-simple-table table table-condensed" style="width:100%;">
								<thead>
								  <tr>
								  <td><label class="text-center">Compartimentos</label></td>
								  <td><label class="text-center">Producto</label></td>
								  <td><label class="text-center">Vol. T. Obs. (gal)</label></td>
								  <td><label class="text-center">Temperatura (F)</label></td>
								  <td><label class="text-center">API 60 F</label></td>
								  <td><label class="text-center">Factor</label></td>
								  <td><label class="text-center">Vol. 60 F (gal)</label></td>
								  </tr>
								</thead>
								<tbody id="GrupoTransporte">
				      				<tr id="GrupoTransporte_template">
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="compartimentos" id="GrupoTransporte_#index#_Compartimentos" name="transporte[detalle][#index#][compartimentos]" type="text" disabled class="form-control text-right input-sm" value="0" />
	      									<input elemento-grupo="capVolCompartimento" id="GrupoTransporte_#index#_CapVolCompartimento" name="transporte[detalle][#index#][capVolCompartimento]" type="hidden" disabled class="form-control text-right input-sm" />
										</td>
				      				   <td class="celda-detalle">
										  <select tipo-control="select2" elemento-grupo="producto" id="GrupoTransporte_#index#_Producto" style="width: 100%" name="transporte[detalle][#index#][producto]" class="form-control input-sm text-uppercase">
											<option value="" selected="selected">SELECCIONAR...</option>
											<%-- <option value="0">SELECCIONAR...</option>
											<%ArrayList<Producto> listaProductos = (ArrayList<Producto>) request.getAttribute("productos");
												Producto eProducto = null;
													
												for (int i = 0; i < listaProductos.size(); i++) {
													eProducto = listaProductos.get(i);
											%>
											<option data-indicador-producto='<%=eProducto.getIndicadorProducto()%>' value='<%=eProducto.getId()%>'><%=eProducto.getNombre()%></option>
											<%	} %> --%>
										  </select>
									 	</td>
	      								<td class="celda-detalle" style="width:10%;">
	      									<input elemento-grupo="volumenTempObservada" id="GrupoTransporte_#index#_VolumenTempObservada" name="transporte[detalle][#index#][volumenTempObservada]" type="text" class="form-control text-right input-sm"  maxlength="7" value="0" />
										</td>
										<td class="celda-detalle" style="width:10%;">
											<input elemento-grupo="temperatura" id="GrupoTransporte_#index#_Temperatura" name="transporte[detalle][#index#][temperatura]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle" style="width:10%;">
											<input elemento-grupo="API" id="GrupoTransporte_#index#_API" name="transporte[detalle][#index#][API]" type="text" class="form-control text-right input-sm" value="" />
										</td>
										<td class="celda-detalle" style="width:10%;">
											<input elemento-grupo="factor" id="GrupoTransporte_#index#_Factor" name="transporte[detalle][#index#][factor]" type="text" class="form-control text-right input-sm" value="" readonly />
										</td>
										<td class="celda-detalle" style="width:10%;">
											<input elemento-grupo="volumen60F" id="GrupoTransporte_#index#_Volumen60F" name="transporte[detalle][#index#][volumen60F]" type="text" class="form-control text-right input-sm" maxlength="7" value="0" />
										</td>
									</tr>
									<tr id="GrupoTransporte_noforms_template">
										<td></td>
									</tr>
									<tr>
										<td class="celda-detalle"></td>
										<td class="celda-detalle"></td>
										<td class="celda-detalle">									
									  		<input  name="cmpSumVolumenTempObservada" id="cmpSumVolumenTempObservada" type="text" class="form-control input-sm text-right" disabled />		
									  	</td>
									  	<td class="celda-detalle"></td>
									  	<td class="celda-detalle"></td>
									  	<td class="celda-detalle"></td>
									  	<td class="celda-detalle">	
									  		<input name="cmpSumVolumen60F" id="cmpSumVolumen60F" type="text" class="form-control input-sm text-right" disabled />			
									  	</td>
									</tr>
								</tbody>
							</table>
						</div>
					</form>
				</div>
				<div class="box-footer col-xs-12">
					<div class="col-md-1">
						<button id="btnGuardar" type="submit" class="btn btn-primary btn-sm">Guardar</button>
					</div>
					<div class="col-md-10"></div>
					<div class="col-md-1">
						<button id="btnCancelarGuardarFormulario" class="btn btn-danger btn-sm">Cancelar</button>
					</div>
				</div>
				<div class="overlay" id="ocultaContenedorFormulario" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</div>
	</div>


	<div class="row" id="cntVistaRegistro" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<table class="sgo-table table" style="width:100%;">
						<tbody>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Cliente:						</td>
							<td> <span class="text-uppercase" id='vistaCliente'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">Operaci&oacute;n:			</td>		
							<td style="width:25%;"> <span id='vistaOperacion'></span> 						</td>
							<td class="tabla-vista-titulo" style="width:15%;">F. Planificaci&oacute;n:		</td>
							<td style="width:15%;"> <span id='vistaFechaPlanificacion'></span>				</td>
							</tr>
						</tbody>
					</table>
					
					<table class="sgo-table table" style="width:100%;">
						<tbody>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">G/R:	</td>
							<td style="width:15%;"> <span id='vistaNumeroGuiaRemision'></span>	</td>
							<td class="tabla-vista-titulo" style="width:5%;">O/E:	</td>		
							<td style="width:15%;"> <span id='vistaNumeroOrdenCompra'></span> 	</td>
							<td class="tabla-vista-titulo" style="width:10%;">Factura:			</td>
							<td style="width:15%;"> <span id='vistaNumeroFactura'></span>		</td>
							<td class="tabla-vista-titulo" style="width:15%;">SCOP:			</td>
							<td style="width:15%;"> <span id='vistaCodigoScop'></span>			</td>
							</tr>
						
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Planta:			</td>
							<td colspan="2"> <span id='vistaPlantaDespacho'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">Transportista:	</td>
							<td colspan="2" style="width:25%;"> <span id='vistaIdTransportista'></span>	</td>
							<td class="tabla-vista-titulo" style="width:15%;">Cisterna/Tracto:	</td>
							<td style="width:15%;"> <span id='vistaCisternaTracto'></span>		</td>
							</tr>
						
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Conductor:				</td>
							<td colspan="5"> <span id='vistaIdConductor'></span>				</td>
							<td class="tabla-vista-titulo" style="width:15%;">F. Emisi&oacute;n O/E:	</td>
							<td style="width:15%;"> <span id='vistaFechaEmisionGuia'></span>			</td>
							</tr>
						
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Precintos:	</td>
							<td colspan="7"> <span id='vistaPrecintos'></span>		</td>
							</tr>
						</tbody>
					</table>
					
					<table class="sgo-table table" style="width:100%;">
						<tbody>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Peso Bruto:			</td>
							<td class="text-right" style="width:23%;"> <span id='vistaPesoBruto'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">Peso Tara:	</td>
							<td class="text-right" style="width:23%;"> <span id='vistaPesoTara'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">Peso Neto:	</td>
							<td class="text-right"> <span id='vistaPesoNeto'></span>		</td>
							</tr>
						</tbody>
					</table>
					
					<table class="sgo-table table" style="width:100%;">
						<tbody>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Creado el:			</td>
							<td class="text-center" style="width:23%;"> <span id='vistaCreadoEl'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">Creado por:	</td>
							<td class="text-left" style="width:23%;"> <span id='vistaCreadoPor'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">IP Creaci&oacute;n:	</td>
							<td class="text-left"> <span id='vistaIpCreacion'></span>		</td>
							</tr>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Actualizado el:			</td>
							<td class="text-center" style="width:23%;"> <span id='vistaActualizadoEl'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">Actualizado por:	</td>
							<td class="text-left" style="width:23%;"> <span id='vistaActualizadoPor'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">IP Actualizaci&oacute;n:	</td>
							<td class="text-left"> <span id='vistaIpActualizacion'></span>		</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="box-body with-border">
					<table id="listado_vista_detalles" class="sgo-table table table-striped"  style="width:100%;"></table>
				</div>

				<div class="box-body with-border">
					<div>	<label class="etiqueta-titulo-horizontal"> Eventos:</label>				</div>
					<table id="listado_vista_eventos" class="sgo-table table" style="width:100%;"></table>
				</div>
				<div class="box-footer" align="right">
					<button id="btnCerrarVista" class="btn btn-danger btn-sm">Cerrar</button>
				</div>
				<div class="overlay" id="ocultaContenedorVista">
			      <i class="fa fa-refresh fa-spin"></i>
			    </div>
			</div>
		</div>
	</div>

	<div class="row" id="cntEventoTransporte" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmEvento" role="form">
						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cliente: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoCliente'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Operación: </label> 					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoOperacion'></span></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">F.Planificada: </label>				</td>
			      				<td class="celda-detalle" style="width:10%;"> <span id="cmpEventoFechaPlanificacion" type="text" class="form-control alert-danger text-center input-sm text-uppercase" readonly /> </td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">G/R: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoNumeroGuiaRemision'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">O/E: </label> 					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpEventoNumeroOrdenCompra'></span></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cisterna/Tracto: </label>				</td>
			      				<td class="celda-detalle" style="width:10%;"> <span class="input-sm text-uppercase" id='cmpEventoCisternaTracto'></span></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Tipo: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;">  <input class="hide" name="cmpEventoIdTransporteEvento" id='cmpEventoIdTransporteEvento' />
										<select id="cmpEventoTipoEvento" name="cmpEventoTipoEvento" class="form-control input-sm text-uppercase">
											<option value="1">Incidencia</option>
											<option value="2">Accidente</option>
											<option value="3">Falla Operacional</option>
										</select> 
								</td>
								<td class="celda-detalle" colspan="2"> </td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Fecha y hora: </label>				</td>
			      				<td class="celda-detalle" style="width:20%;"> <input name="cmpEventoFechaHora" id="cmpEventoFechaHora" type="text" class="form-control input-sm" required data-bind="value: dateTime" placeholder="__/__/____ __:__:__" data-inputmask="'mask': 'd/m/y h:s:s'" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Observaciones: </label>					</td>
			      				<td class="celda-detalle" colspan="5">  <input class="hide" name="cmpEventoIdTransporteEvento" id='cmpEventoIdTransporteEvento' />
									<textarea class="form-control input-sm text-uppercase" required id="cmpEventoDescripcion" maxlength="3000" name="cmpEventoDescripcion" placeholder="Ingrese observaci&oacute;n..." rows="3" ></textarea>
								</td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarEvento" type="submit" class="btn btn-primary btn-sm">Guardar</button>
		            <button id="btnCancelarGuardarEvento" class="btn btn-danger btn-sm">Cancelar</button>
		            <br />
		    	</div>
		    	<div class="overlay" id="ocultaContenedorFormularioEvento" style="display:none;">
		            <i class="fa fa-refresh fa-spin"></i>
		        </div>
			</div>
		</div>
	</div>
	
	
	<div class="row" id="cntPesajeTransporte" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmPesaje" role="form">
						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cliente: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpPesajeCliente'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Operación: </label> 					</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpPesajeOperacion'></span></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">F.Planificada: </label>				</td>
			      				<td class="celda-detalle" style="width:20%;"> <span id="cmpPesajeFechaPlanificacion" type="text" class="form-control alert-danger text-center input-sm text-uppercase" readonly /> </td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">G/R: </label>			</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpPesajeNumeroGuiaRemision'></span>	</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">O/E: </label> 				</td>
			      				<td class="celda-detalle" style="width:25%;"> <span class="input-sm text-uppercase" id='cmpPesajeNumeroOrdenCompra'></span>						</td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Cisterna/Tracto: </label>				</td>
			      				<td class="celda-detalle" style="width:20%;"> <span class="input-sm text-uppercase" id='cmpPesajeCisternaTracto'></span>		</td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Peso Bruto: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <input name="cmpPesajePesoBruto" id="cmpPesajePesoBruto" type="text" class="form-control text-right input-sm" maxlength="9" value="" /></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Peso Tara: </label>					</td>
			      				<td class="celda-detalle" style="width:25%;"> <input name="cmpPesajePesoTara" id="cmpPesajePesoTara" type="text" class="form-control text-right input-sm" maxlength="9" value="" /></td>
			      				<td class="celda-detalle" style="width:10%;"> <label class="etiqueta-titulo-horizontal">Peso Neto: </label>					</td>
			      				<td class="celda-detalle" style="width:20%;"> <input name="cmpPesajePesoNeto" id="cmpPesajePesoNeto" type="text" class="form-control text-right input-sm" disabled maxlength="9" value="" /></td>
			      				</tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarPesaje" type="submit" class="btn btn-primary btn-sm">Guardar</button>
		            <button id="btnCancelarGuardarPesaje" class="btn btn-danger btn-sm">Cancelar</button>
		    	</div>
		    	<div class="overlay" id="ocultaContenedorFormularioPesaje" style="display:none;">
		            <i class="fa fa-refresh fa-spin"></i>
		          </div>
			</div>
		</div>
	</div>	
	
	<%-- Inicio Agregado por 9000002570 --%>
    <div class="row" id="cntFrmTiempos" style="display:none;">
    	<div class="col-md-12">
    		<div class="box box-default">
    			<div class="box-body">
    				<table class="sgo-simple-table table table-condensed">
    					<thead>
    						<tr>
			      				<td>
			      				 	<label class="etiqueta-titulo-horizontal">Cliente: </label>
			      				</td>
			      				<td>
			      					<span id="lblCliente" >Prueba</span>
			      				</td>
			      				<td>
			      				 <label class="etiqueta-titulo-horizontal">Operación: </label>
			      				</td>
			      				<td>
			      					<span id="lblOperacion" >Prueba</span>
			      				</td>
			      				<td>
			      					<label class="etiqueta-titulo-horizontal">Fecha Planificada: </label>
			      				</td>
			      				<td>
			      					<span id="lblFechaPlanificada" >Prueba</span>
			      				</td>
      						</tr>
    						<tr>
			      				<td>
			      				 	<label class="etiqueta-titulo-horizontal">Cisterna: </label>
			      				</td>
			      				<td>
			      					<span id="lblCisterna" >Prueba</span>
			      				</td>
			      				<td>
			      				 <label class="etiqueta-titulo-horizontal">Nro G / R: </label>
			      				</td>
			      				<td>
			      					<span id="lblNroGR" >Prueba</span>
			      				</td>
			      				<td>
			      					<label class="etiqueta-titulo-horizontal">Fecha G / R: </label>
			      				</td>
			      				<td>
			      					<span id="lblFechaGR" >Prueba</span>
			      				</td>
      						</tr>
    						<tr>
			      				<td>
			      				 	<label class="etiqueta-titulo-horizontal">Conductor: </label>
			      				</td>
			      				<td>
			      					<span id="lblConductor" >Prueba</span>
			      				</td>
			      				<td>
			      				 <label class="etiqueta-titulo-horizontal">Planta: </label>
			      				</td>
			      				<td>
			      					<span id="lblPlanta" >Prueba</span>
			      				</td>
      						</tr>
    					</thead>
    				</table>
    				<div id="cntTiempos">
    					<form id="frmTiempos" class="form form-horizontal" novalidate="novalidate">
    						<div class="col-md-12">
			  					<div class="box box-default">
			  					<div><span id="lblPlanta" style="font-weight:bold;">Especifique los tiempos transcurridos para las siguientes etapas (obligatorio):</span></div>
									<div class="box-header with-border">
										<div class="col-xs-12">
											<div  class="col-md-12">
											<table class="sgo-simple-table table table-condensed">
								      			<thead>
								      				<tr>
								      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Etapa</label></td>
								      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Inicio</label></td>
								      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Fin</label></td>
								      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Min.</label></td>
								      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Observación</label></td>
								                	</tr>
								              	</thead>
								              	<tbody id="GrupoTiempos">
	      											<tr id="GrupoTiempos_template">
	      												<td class="celda-detalle">
	      													<input elemento-grupo="idEtapaTrans" id="GrupoTiempos_#index#_IdEtapaTrans" name="tiempo[detalle][#index#][idEtapa]" type="hidden" class="form-control input-sm text-right" />
	      													<input elemento-grupo="idEtapaOPerRuta" id="GrupoTiempos_#index#_IdEtapaOPerRuta" name="tiempo[detalle][#index#][idEtapaOPerRuta]" type="hidden" class="form-control input-sm text-right" />
	      													<input elemento-grupo="nombreEtapa" id="GrupoTiempos_#index#_NombreEtapa" name="tiempo[detalle][#index#][nombreEtapa]" type="text" class="form-control input-sm text-left text-uppercase" disabled/>
	      												</td>
	      												<td class="celda-detalle" style="width:20%;">
	      													<input elemento-grupo="inicioEtapa" id="GrupoTiempos_#index#_InicioEtapa" name="tiempo[detalle][#index#][inicioEtapa]" type="text" class="form-control input-sm text-center" placeholder="dd/mm/aaaa hh:mm"/>
	      												</td>
	      												<td class="celda-detalle" style="width:20%;">
	      													<input elemento-grupo="finEtapa" id="GrupoTiempos_#index#_FinEtapa" name="tiempo[detalle][#index#][finEtapa]" type="text" class="form-control input-sm text-center" placeholder="dd/mm/aaaa hh:mm"/>
	      												</td>
	      												<td class="celda-detalle" style="width:5%;">
	      													<input elemento-grupo="minutos" id="GrupoTiempos_#index#_Minutos" name="tiempo[detalle][#index#][minutos]" type="text" class="form-control input-sm text-center" disabled/>
	      												</td>	      												
	      												<td class="celda-detalle" style="width:25%;">
	      													<input elemento-grupo="observ" id="GrupoTiempos_#index#_Observ" name="tiempo[detalle][#index#][observ]" type="text" class="form-control input-sm text-left text-uppercase" />
	      												</td>
	      											</tr>
	      											<tr id="GrupoTiempos_noforms_template">
								      					<td></td>
								      				</tr>  
	      										</tbody>
								            </table>
								            <table>
								            	<tr>
								            		<td></td>
								            		<td style="width:20%;"></td>
								            		<td style="width:20%; text-align:right;">
      													<label class="etiqueta-titulo-horizontal">TIEMPO TRANSPORTE (hrs): </label>
      												</td>
      												<td style="width:5%;">
      													<input id="tiempoTotal" type="text" class="form-control input-sm text-center" disabled/>
      												</td>
      												<td style="width:25%;"></td>
      											</tr>
								            </table>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div><span id="lblPlanta" style="font-weight:bold;">NOTA: En caso de la ocurrencia de algún Evento durante el transporte de material, utilizar la opción "Eventos" del Sistema para informarlo </span></div>
    					</form>
    					<div class="box-footer">
			    			<a id="btnGuardarTiempos" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
					        <a id="btnCancelarTiempos" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
		    			</div>
    				</div>
    			</div>
    			<div class="overlay" id="ocultaContenedorTiempos">
	            	<i class="fa fa-refresh fa-spin"></i>
	          	</div>
    		</div>
    	</div>
    </div>
    <%-- Fin Agregado por 9000002570 --%>
	</section>
</div>