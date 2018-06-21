<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Estacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>

<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Registro de D&iacute;a Operativo / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
		                    <option <%=seleccionado%> data-operacion-cliente='<%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %>' data-idCliente='<%=eOperacion.getIdCliente()%>' data-idOperacion='<%=eOperacion.getId()%>' data-fecha-actual='<%=fechaActual%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
		                    <%
	                    } %>
                    </select></td>  
                    <td></td>
                    <td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Estación: </label></td>
                    <td class="celda-detalle" ><select style="width:100%;" id="filtroEstacion" name="filtroEstacion" class="form-control espaciado input-sm" >
                 		<% 
                 		ArrayList<?> listaEstaciones = (ArrayList<?>) request.getAttribute("estaciones"); 
	                    int numeroEstaciones = listaEstaciones.size();
	                    Estacion eEstacion=null;
	                    String estacionSeleccionada="selected='selected'";
	                    estacionSeleccionada="";
	                    for(int indiceEstaciones=0; indiceEstaciones < numeroEstaciones; indiceEstaciones++){ 
	                    	eEstacion =(Estacion) listaEstaciones.get(indiceEstaciones);
		                    %>
		                    <option <%=estacionSeleccionada%> data-estacion='<%=eEstacion.getNombre().trim()%>' 
		                    								  data-idEstacion='<%=eEstacion.getId()%>' 
		                    								  value='<%=eEstacion.getId()%>'>
		                    								  <%=eEstacion.getNombre().trim()%></option>
		                    <%
	                    } %>
                    </select>
                    </td>
                    
                    <!-- <td  class="celda-detalle" style="width:20%;"><select style="width:100%;"  id="filtroEstacion" name="filtroEstacion" class="form-control input-sm">
                    	<option value="0" selected="selected">Seleccionar...</option>
                    </select></td> -->
        			<td></td>
        			 <td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Día Operativo: </label></td>
	                <td class="celda-detalle" style="width:15%;"><input id="filtroFechaJornada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" /></td>
            		<td></td> 
                  	<td><a id="btnFiltrar" class="btn btn-default btn-sm col-md-12"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a></td>
                </tr>
                </thead>
              </table>
          </form>
        </div>

        <div class="box-header">
          <div>
            <a id="btnApertura" class="btn btn-default btn-sm espaciado"><i class="fa fa-folder-open-o"></i>  Apertura</a>
            <a id="btnCierre" class="btn btn-default btn-sm espaciado"><i class="fa fa-folder-o"></i>  Cierre</a>
            <a id="btnReapertura" class="btn btn-default btn-sm espaciado"><i class="fa fa-mail-forward"></i>  Reapertura</a>
            <a id="btnVer" class="btn btn-default btn-sm espaciado"><i class="fa fa-search"></i>  Ver</a>
            <a id="btnCambioTanque" class="btn btn-default btn-sm espaciado"><i class="fa fa-exchange"></i>  Cambio Tanque</a>
            <a id="btnMuestreo" class="btn btn-default btn-sm espaciado"><i class="fa fa-eyedropper"></i>  Muestreo</a>
          </div>
        </div>
          <div class="box-body">
            <table id="tablaJornada" class="sgo-table table table-striped" width="100%">
              <thead>
                <tr>
 					<th>#</th>
 					<th>ID</th>
 					<th>ID Estacion</th>
 					<th>Estacion</th>
                 	<th>D&iacute;a Operativo</th>
                 	<th>Total Despachos</th>
                 	<th>Ult. Actualizaci&oacute;n</th>
                 	<th>Usuario</th>
                 	<th>Estado</th>
                 </tr>
              </thead>
            </table>
            <div id="frmConfirmarReapertura" class="modal" data-keyboard="false" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
						</div>
						<div class="modal-body">
							<p><%=mapaValores.get("MENSAJE_REAPERTURAR_JORNADA")%></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
							<button id="btnConfirmarReapertura" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
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

    <div class="row" id="cntFormCambioTanque" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmCambioTanque" role="form">
						<table class="sgo-simple-table table table-condensed">
		                  <thead>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:26%;"><span id='cmpCambioTanqueCliente'>	</span></td>
		        	 		  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:25%;"><span id='cmpCambioTanqueOperacion'>	</span></td>
		        			  <td></td>
		                      <td class="celda-detalle" style="width:10%;"><label>Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:25%;"><span id='cmpCambioTanqueEstacion'>	</span></td>
		                   </tr>
		                   <tr>
			                  <td class="celda-detalle" style="width:7%;"><label>Operador 1: </label></td>
			        		  <td class="celda-detalle" style="width:26%;">		<span id='cmpCambioTanqueOperador1'>	</span></td>
			                  <td></td>
			                  <td class="celda-detalle" style="width:7%;"><label>Operador 2: </label></td>
			                  <td class="celda-detalle" style="width:25%;">	<span id='cmpCambioTanqueOperador2'>	</span></td>
			                  <td></td>
		        			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:25%;"><FONT COLOR=red><B>	<span id='cmpCambioTanqueFechaJornada'></span></B></FONT> </td>
			                </tr>
		                  </thead>
		                </table>
						<label><B>Tanque a Cerrar (Medidas finales) </B></label>
						
						<table class="sgo-simple-table table table-condensed" >
							<thead> <tr> <td> </td></tr></thead>
							<tbody id="GrupoCambioTanquesFinal">
								<tr id="GrupoCambioTanquesFinal_template">
								<td>
									<table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
									<thead> <tr> <td> </td></tr></thead>
				      				<tr>
				      					<td style="width:14%;"><label class="text-center">Tanque:				</label></td>
				      					<td class="celda-detalle" colspan="2" style="width:28%;">
				      				   	  <select tipo-control="select2" elemento-grupo="idTanqueJornadaFinal" id="GrupoCambioTanquesFinal_#index#_IdTanqueJornadaFinal" style="width: 100%" name="cambioTanqueFinal[detalle][#index#][idTanqueJornadaFinal]" class="form-control input-sm text-uppercase">
											<option value="" selected="selected">SELECCIONAR...</option>
										  </select>
									 	</td>
									 	<td></td>
									 	<td style="width:14%;"><label class="text-center">Producto:				</label></td>
				      					<td class="celda-detalle" colspan="2" style="width:28%;">
				      						<input elemento-grupo="descripcionTanqueFinal" id="GrupoCambioTanquesFinal_#index#_DescripcionTanqueFinal" name="cambioTanqueFinal[detalle][#index#][descripcionTanqueFinal]" type="hidden" disabled class="form-control input-sm"/>
				      						<input elemento-grupo="idTanqueFinal" id="GrupoCambioTanquesFinal_#index#_IdTanqueFinal" name="cambioTanqueFinal[detalle][#index#][idTanqueFinal]" type="hidden" disabled class="form-control input-sm"/>
				      						<input elemento-grupo="idProductoFinal" id="GrupoCambioTanquesFinal_#index#_IdProductoFinal" name="cambioTanqueFinal[detalle][#index#][idProductoFinal]" type="hidden" disabled class="form-control input-sm"/>
				      						<input elemento-grupo="descripcionProductoFinal" id="GrupoCambioTanquesFinal_#index#_DescripcionProductoFinal" name="cambioTanqueFinal[detalle][#index#][descripcionProductoFinal]" type="text" disabled class="form-control input-sm"/>
										</td>
				      				</tr>
	
				      				<tr>
				      					<td class="celda-detalle text-center"><label>Hora 			</label></td>
				      					<td class="celda-detalle text-center"><label>Medida (mm)	</label></td>
										<td class="celda-detalle text-center"><label>Vol. Obs.		</label></td>
										<td class="celda-detalle text-center"><label>API 60F		</label></td>
										<td class="celda-detalle text-center"><label>Temperatura	</label></td>
										<td class="celda-detalle text-center"><label>Factor			</label></td>
				      					<td class="celda-detalle text-center"><label>Volumen 60F	</label></td>
				      					<td class="celda-detalle text-center"><label>Vol. Agua		</label></td>
				      					<!-- <td class="celda-detalle text-center"><label>F/S			</label></td>
				      					<td class="celda-detalle text-center"><label>Desp.			</label></td> -->
				      				</tr>
	
				      				<tr>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="horaFinal" id="GrupoCambioTanquesFinal_#index#_HoraFinal" name="cambioTanqueFinal[detalle][#index#][horaFinal]" type="text" class="form-control text-center input-sm" data-inputmask="'mask': 'd/m/y h:s:s'" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="medidaFinal" id="GrupoCambioTanquesFinal_#index#_MedidaFinal" name="cambioTanqueFinal[detalle][#index#][medidaFinal]" type="text" class="form-control text-right input-sm" maxlength="8" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="volObsFinal" id="GrupoCambioTanquesFinal_#index#_VolObsFinal" name="cambioTanqueFinal[detalle][#index#][volObsFinal]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="api60Final" id="GrupoCambioTanquesFinal_#index#_Api60Final" name="cambioTanqueFinal[detalle][#index#][api60Final]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="temperaturaFinal" id="GrupoCambioTanquesFinal_#index#_TemperaturaFinal" name="cambioTanqueFinal[detalle][#index#][temperaturaFinal]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="factorFinal" id="GrupoCambioTanquesFinal_#index#_FactorFinal" name="cambioTanqueFinal[detalle][#index#][factorFinal]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="vol60Final" id="GrupoCambioTanquesFinal_#index#_Vol60Final" name="cambioTanqueFinal[detalle][#index#][vol60Final]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
											<input elemento-grupo="volAguaFinal" id="GrupoCambioTanquesFinal_#index#_VolAguaFinal" name="cambioTanqueFinal[detalle][#index#][volAguaFinal]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<!-- <td class="celda-detalle"  style="width:3%;">
	      									<input elemento-grupo="fsInicialTanque" id="GrupoCambioTanquesFinal_#index#_FsInicialTanque" name="cambioTanqueFinal[detalle][#index#][fsInicialTanque]" type="checkbox" disabled class="form-control input-sm"/>
										</td>
										<td class="celda-detalle"  style="width:3%;">
	      									<input elemento-grupo="despInicialTanque" id="GrupoCambioTanquesFinal_#index#_DespInicialTanque" name="cambioTanqueFinal[detalle][#index#][despInicialTanque]" type="checkbox" disabled class="form-control input-sm"/>
										</td> -->
									</tr>
									
									</table>
								  </td>
								</tr>
								<tr id="GrupoCambioTanquesFinal_noforms_template">
			      				<td></td>
			      				</tr>  
							</tbody>
						</table>
						
						
						<label><B>Tanque a Aperturar (Medidas iniciales) </B></label>
						
						<table class="sgo-simple-table table table-condensed" >
							<thead> <tr> <td> </td></tr></thead>
							<tbody id="GrupoCambioTanquesInicial">
								<tr id="GrupoCambioTanquesInicial_template">
								<td>
									<table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
									<thead> <tr> <td> </td></tr></thead>
									<tr>
				      					<td style="width:14%;"><label class="text-center">Tanque:				</label></td>
				      					<td class="celda-detalle" colspan="2" style="width:28%;">
				      				   	  <select tipo-control="select2" elemento-grupo="idTanqueJornadaInicial" id="GrupoCambioTanquesInicial_#index#_IdTanqueJornadaInicial" style="width: 100%" name="cambioTanqueInicial[detalle][#index#][idTanqueJornadaInicial]" class="form-control input-sm text-uppercase">
											<option value="" selected="selected">SELECCIONAR...</option>
										  </select>
									 	</td>
									 	<td></td>
									 	<td style="width:14%;"><label class="text-center">Producto:				</label></td>
				      					<td class="celda-detalle" colspan="2" style="width:28%;">
				      						<input elemento-grupo="descripcionTanqueInicial" id="GrupoCambioTanquesFinal_#index#_DescripcionTanqueInicial" name="cambioTanqueFinal[detalle][#index#][descripcionTanqueInicial]" type="hidden" disabled class="form-control input-sm"/>
				      						<input elemento-grupo="idTanqueInicial" id="GrupoCambioTanquesInicial_#index#_IdTanqueInicial" name="cambioTanqueInicial[detalle][#index#][idTanqueInicial]" type="hidden" disabled class="form-control input-sm"/>
				      						<input elemento-grupo="idProductoInicial" id="GrupoCambioTanquesInicial_#index#_IdProductoInicial" name="cambioTanqueInicial[detalle][#index#][idProductoInicial]" type="hidden" disabled class="form-control input-sm" value="0" />
				      						<input elemento-grupo="descripcionProductoInicial" id="GrupoCambioTanquesInicial_#index#_DescripcionProductoInicial" name="cambioTanqueInicial[detalle][#index#][descripcionProductoInicial]" type="text" disabled class="form-control input-sm" />
										</td>
				      				</tr>
	
				      				<tr>
				      					<td class="celda-detalle text-center"><label>Hora 			</label></td>
				      					<td class="celda-detalle text-center"><label>Medida (mm)	</label></td>
										<td class="celda-detalle text-center"><label>Vol. Obs.		</label></td>
										<td class="celda-detalle text-center"><label>API 60F		</label></td>
										<td class="celda-detalle text-center"><label>Temperatura	</label></td>
										<td class="celda-detalle text-center"><label>Factor			</label></td>
				      					<td class="celda-detalle text-center"><label>Volumen 60F	</label></td>
				      					<td class="celda-detalle text-center"><label>		</label></td>
				      					<!-- <td class="celda-detalle text-center"><label>F/S			</label></td>
				      					<td class="celda-detalle text-center"><label>Desp.			</label></td> -->
				      				</tr>
									
									<tr>
										<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="horaInicial" id="GrupoCambioTanquesFinal_#index#_HoraInicial"  disabled name="cambioTanqueFinal[detalle][#index#][horaInicial]" type="text" class="form-control text-center input-sm" data-inputmask="'mask': 'd/m/y h:s:s'" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="medidaInicial" id="GrupoCambioTanquesInicial_#index#_MedidaInicial" name="cambioTanqueInicial[detalle][#index#][medidaInicial]" type="text" class="form-control text-right input-sm" maxlength="8" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="volObsInicial" id="GrupoCambioTanquesInicial_#index#_VolObsInicial" name="cambioTanqueInicial[detalle][#index#][volObsInicial]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="api60Inicial" id="GrupoCambioTanquesInicial_#index#_Api60Inicial" name="cambioTanqueInicial[detalle][#index#][api60Inicial]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="temperaturaInicial" id="GrupoCambioTanquesInicial_#index#_TemperaturaInicial" name="cambioTanqueInicial[detalle][#index#][temperaturaInicial]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:12%;">
	      									<input elemento-grupo="factorInicial" id="GrupoCambioTanquesInicial_#index#_FactorInicial" name="cambioTanqueInicial[detalle][#index#][factorInicial]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:13%;">
	      									<input elemento-grupo="vol60Inicial" id="GrupoCambioTanquesInicial_#index#_Vol60Inicial" name="cambioTanqueInicial[detalle][#index#][vol60Inicial]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td> </td>
										<!-- <td class="celda-detalle"  style="width:3%;">
	      									<input elemento-grupo="fsInicialTanque" id="GrupoCambioTanquesInicial_#index#_FsInicialTanque" name="cambioTanqueInicial[detalle][#index#][fsInicialTanque]" type="checkbox" disabled class="form-control input-sm"/>
										</td>
										<td class="celda-detalle"  style="width:3%;">
	      									<input elemento-grupo="despInicialTanque" id="GrupoCambioTanquesInicial_#index#_DespInicialTanque" name="cambioTanqueInicial[detalle][#index#][despInicialTanque]" type="checkbox" disabled class="form-control input-sm"/>
										</td> -->
									</tr>
									</table>
								  </td>
								</tr>
								<tr id="GrupoCambioTanquesInicial_noforms_template">
			      				<td></td>
			      				</tr>  
							</tbody>
						</table>
						
		      			
					</form>
				</div>
				<div class="box-footer">
		          <a id="btnGuardarCambioTanque" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
		          <a id="btnCancelarCambioTanque" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
		        </div>
				<div class="overlay" id="ocultaCntCambioTanque" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</div>
	</div>
    
    <!-- contenedor apertura jornada -->
    <div class="row" id="cntFormularioAperturaJornada" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmApertura" role="form">
						<table class="sgo-simple-table table table-condensed">
		                  <thead>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:18%;"><span id='cmpAperturaCliente'>	</span></td>
		        	 		  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:18%;"><span id='cmpAperturaOperacion'>	</span></td>
		        			  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:18%;"><span id='cmpAperturaEstacion'>	</span></td>
		                      <td></td>
		        			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='cmpAperturaFechaJornada'></span></B></FONT> </td>
			                  <td class="celda-detalle" ></td>
		                   </tr>
		                   <tr>
			                	<td class="celda-detalle" style="width:7%;"><label>Operador 1: </label></td>
			        			<td colspan="4" class="celda-detalle">
			        				<select style="width:100%;"  id="cmpOperador1" name="cmpOperador1" class="form-control input-sm">
				                    	<option value="0" selected="selected">Seleccionar...</option>
				                    </select>
								</td>
			                    <td></td>
			                    <td class="celda-detalle" style="width:7%;"><label>Operador 2: </label></td>
			                    <td colspan="4" class="celda-detalle">
			        				<select style="width:100%;"  id="cmpOperador2" name="cmpOperador2" class="form-control input-sm">
				                    	<option value="0" selected="selected">Seleccionar...</option>
				                    </select>
								</td>
			                </tr>
		                  </thead>
		                </table>
		                <label><B>Cont&oacute;metros </B></label>
		                <table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
							<thead>
							  <tr>
							  <td><label class="text-center">Cont&oacute;metro	</label></td>
							  <td><label class="text-center">Producto			</label></td>
							  <td><label class="text-center">Lectura Inicial	</label></td>
							  </tr>
							</thead>
							<tbody id="GrupoAperturaContometros">
			      				<tr id="GrupoAperturaContometros_template">
			      					<td class="celda-detalle"  style="width:10%;">
			      					    <input elemento-grupo="idContometros" id="GrupoAperturaContometros_#index#_IdContometro" name="aperturaContometro[detalle][#index#][idContometro]" type="hidden" disabled class="form-control text-right input-sm"/>
      									<input elemento-grupo="contometros" id="GrupoAperturaContometros_#index#_Contometro" name="aperturaContometro[detalle][#index#][contometro]" type="text" disabled class="form-control input-sm" />
									</td>
			      				   <td class="celda-detalle" style="width:70%;">
			      				   	  <select tipo-control="select2" elemento-grupo="productosContometros" id="GrupoAperturaContometros_#index#_ProductosContometros" style="width: 100%" name="aperturaContometro[detalle][#index#][productoContometro]" class="form-control input-sm text-uppercase" disabled>
										<option value="" selected="selected">SELECCIONAR...</option>
									  </select>
								 	</td>
      								<td class="celda-detalle" style="width:20%;">
      									<input elemento-grupo="lecturaInicial" id="GrupoAperturaContometros_#index#_LecturaInicial" name="aperturaContometro[detalle][#index#][lecturaInicial]" type="text"  maxlength="8" disabled class="form-control text-right input-sm" />
									</td>
								</tr>
								<tr>
			      				<td></td>
			      				</tr>
			      				<tr id="GrupoAperturaContometros_noforms_template">
			      				<td></td>
			      				</tr>    
							</tbody>
						</table>
		                <br />
		                <label><B>Tanques </B></label>
		                <table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
							<thead>
							  <tr>
								  <td><label class="text-center">Tanque				</label></td>

								  <td><label class="text-center">Producto			</label></td>
								  <td><label class="text-center">Medida				</label></td>
								  <td><label class="text-center">Vol. Obs. Inicial	</label></td>
								  <td><label class="text-center">API 60F			</label></td>
								  <td><label class="text-center">Temperatura		</label></td>
								  <td><label class="text-center">Factor				</label></td>
								  <td><label class="text-center">Vol. 60F			</label></td>
								  <td><label class="text-center">F/S				</label></td>
								  <td><label class="text-center">Desp				</label></td>
							  </tr>
							</thead>
							<tbody id="GrupoAperturaTanques">
			      				<tr id="GrupoAperturaTanques_template">
			      					<td class="celda-detalle"  style="width:8%;">
			      						<input elemento-grupo="idTanques" id="GrupoAperturaTanques_#index#_IdTanque" name="aperturaContometro[detalle][#index#][idTanques]" type="hidden" disabled class="form-control input-sm" value="0" />
      									<input elemento-grupo="tanques" id="GrupoAperturaTanques_#index#_Tanque" name="aperturaContometro[detalle][#index#][tanques]" type="text" disabled class="form-control input-sm" value="0" />
									</td>
									
			      				    <td class="celda-detalle" style="width:20%;">
			      				    	<input elemento-grupo="idProductosTanques" id="GrupoAperturaTanques_#index#_IdProductosTanques" name="aperturaContometro[detalle][#index#][idProductosTanques]" type="hidden" disabled class="form-control input-sm" value="0" />
      									<input elemento-grupo="productosTanques" id="GrupoAperturaTanques_#index#_ProductosTanques" name="aperturaContometro[detalle][#index#][productosTanques]" type="text" disabled class="form-control input-sm" value="0" />
			      				  		
			      				  		<!-- <select tipo-control="select2" elemento-grupo="productosTanques" id="GrupoAperturaTanques_#index#_ProductosTanque" style="width: 100%" name="aperturaContometro[detalle][#index#][productosTanques]" class="form-control input-sm text-uppercase" disabled>
											<option value="" selected="selected">SELECCIONAR...</option>
									  	</select> -->
									 <!--  <input elemento-grupo="productosTanques" id="GrupoAperturaTanques_#index#_ProductosTanque" name="aperturaContometro[detalle][#index#][productosTanques]" type="text" disabled class="form-control input-sm" value="0" /> -->
								 	</td>
      								<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="medidaInicial" id="GrupoAperturaTanques_#index#_MedidaInicial" name="aperturaContometro[detalle][#index#][medidaInicial]" type="text" disabled class="form-control text-right input-sm" value="0" />
									</td>
									<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="volObsInicial" id="GrupoAperturaTanques_#index#_VolObsInicial" name="aperturaContometro[detalle][#index#][volObsInicial]" type="text" disabled class="form-control text-right input-sm" value="0" />
									</td>
									<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="api60" id="GrupoAperturaTanques_#index#_Api60" name="aperturaContometro[detalle][#index#][api60]" type="text" disabled class="form-control text-right input-sm" value="" />
									</td>
									<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="temperatura" id="GrupoAperturaTanques_#index#_Temperatura" name="aperturaContometro[detalle][#index#][temperatura]" type="text" disabled class="form-control text-right input-sm" value="0" />
									</td>
									<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="factor" id="GrupoAperturaTanques_#index#_Factor" name="aperturaContometro[detalle][#index#][factor]" type="text" disabled class="form-control text-right input-sm" value="" readonly />
									</td>
									<td class="celda-detalle" style="width:9%;">
      									<input elemento-grupo="vol60" id="GrupoAperturaTanques_#index#_Vol60" name="aperturaContometro[detalle][#index#][vol60]" type="text" disabled class="form-control text-right input-sm" value="0" />
									</td>
									<td class="celda-detalle" style="width:3%; text-align:center";>
      									<input elemento-grupo="fs" id="GrupoAperturaTanques_#index#_Fs" name="aperturaContometro[detalle][#index#][fs]" type="checkbox" />
									</td>
									<td class="celda-detalle" style="width:3%; text-align:center";>
      									<input elemento-grupo="desp" id="GrupoAperturaTanques_#index#_Desp" name="aperturaContometro[detalle][#index#][desp]" type="checkbox" />
									</td>
								</tr>
								<tr id="GrupoAperturaTanques_noforms_template">
			      				<td></td>
			      				</tr>  
							</tbody>
						</table>
						<br>
      		            <label>Observaciones</label>
                          <table class="sgo-simple-table table table-condensed">
                            <thead>
                              <tr>
                                <td class="celda-detalle">
    					          <textarea id="cmpObservacionApertura" name="cmpObservacionApertura" type="text" class="form-control input-sm text-uppercase" maxlength="700" rows="1"></textarea>
    				            </td>
                              </tr>
                           </thead>
                         </table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarApertura" type="submit" class="btn btn-primary btn-sm">Guardar</button>
		            <button id="btnCancelarApertura" class="btn btn-danger btn-sm">Cancelar</button>
		            <br />
		    	</div>
				<div class="overlay" id="ocultaContenedorAperturaJornada">
			    	<i class="fa fa-refresh fa-spin"></i>
			    </div> 
	   		</div>
		</div>
	</div>
	
	
	
	<div class="row" id="cntFormularioCierreJornada" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmCierre" role="form">
						<table class="sgo-simple-table table table-condensed">
		                  <thead>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:18%;"><span id='cmpCierreCliente'>	</span></td>
		        	 		  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:18%;"><span id='cmpCierreOperacion'>	</span></td>
		        			  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:18%;"><span id='cmpCierreEstacion'>	</span></td>
		                      <td></td>
		        			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='cmpCierreFechaJornada'></span></B></FONT> </td>
			                  <td class="celda-detalle" ></td>
		                   </tr>
		                   <tr>
			                	<td class="celda-detalle" style="width:7%;"><label>Operador 1: </label></td>
			        			<td colspan="4" class="celda-detalle">		<span id='cmpCierreOperador1'>	</span></td>
			                    <td></td>
			                    <td class="celda-detalle" style="width:7%;"><label>Operador 2: </label></td>
			                    <td colspan="4" class="celda-detalle">		<span id='cmpCierreOperador2'>	</span></td>
			                </tr>
		                  </thead>
		                </table>
		                <!-- Sheppit de productos -->
		                <label><B>Productos </B></label>
		                <table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
							<thead>
							  <tr>
								  <td class="celda-detalle text-center"><label>Producto		</label></td>
								  <td class="celda-detalle text-center"><label>API 60F		</label></td>
								  <td class="celda-detalle text-center"><label>Temperatura	</label></td>
								  <td class="celda-detalle text-center"><label>Factor		</label></td>
							  </tr>
							</thead>
							<tbody id="GrupoCierreProducto">
			      				<tr id="GrupoCierreProducto_template">
			      					<td class="celda-detalle"  style="width:55%;">
			      						<input elemento-grupo="idMuestreo" id="GrupoCierreProducto_#index#_IdMuestreo" name="cierreContometro[detalle][#index#][idMuestreo]" type="hidden" disabled class="form-control text-right input-sm"/>
			      					    <input elemento-grupo="idProducto" id="GrupoCierreProducto_#index#_IdProducto" name="cierreProducto[detalle][#index#][idProducto]" type="hidden" disabled class="form-control input-sm"/>
      									<input elemento-grupo="producto" id="GrupoCierreProducto_#index#_Contometro" name="cierreProducto[detalle][#index#][producto]" type="text" disabled class="form-control input-sm" />
									</td>
      								<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="apiProducto" id="GrupoCierreProducto_#index#_ApiProducto" name="cierreProducto[detalle][#index#][apiProducto]" type="text" class="form-control text-right input-sm" />
									</td>
									<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="temperaturaProducto" id="GrupoCierreProducto_#index#_TemperaturaProducto" name="cierreProducto[detalle][#index#][temperaturaProducto]" type="text" class="form-control text-right input-sm" />
									</td>
									<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="factorProducto" id="GrupoCierreProducto_#index#_FactorProducto" name="cierreProducto[detalle][#index#][factorProducto]" type="text" disabled class="form-control text-right input-sm" />
									</td>
								</tr>
								<tr>
			      				<td></td>
			      				</tr>
			      				<tr id="GrupoCierreProducto_noforms_template">
			      				<td></td>
			      				</tr>    
							</tbody>
						</table>
						<br />
		                <label><B>Cont&oacute;metros </B></label>
		                <table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
							<thead>
							  <tr>
							  <td class="celda-detalle text-center"><label>Cont&oacute;metro	</label></td>
							  <td class="celda-detalle text-center"><label>Producto				</label></td>
							  <td class="celda-detalle text-center"><label>Lectura Inicial		</label></td>
							  <td class="celda-detalle text-center"><label>Lectura Final		</label></td>
							  <td class="celda-detalle text-center"><label>Dif. Vol. Encontrado	</label></td>
							  <td class="celda-detalle text-center"><label>F/S					</label></td>
							  </tr>
							</thead>
							<tbody id="GrupoCierreContometros">
			      				<tr id="GrupoCierreContometros_template">
			      					<td class="celda-detalle"  style="width:15%;">
			      						<input elemento-grupo="idContometroJornadaCierre" id="GrupoCierreContometros_#index#_IdContometroJornadaCierre" name="cierreContometro[detalle][#index#][idContometroJornadaCierre]" type="hidden" disabled class="form-control text-right input-sm"/>
			      					    <input elemento-grupo="idContometroCierre" id="GrupoCierreContometros_#index#_IdContometroCierre" name="cierreContometro[detalle][#index#][idContometroCierre]" type="hidden" disabled class="form-control text-right input-sm"/>
      									<input elemento-grupo="contometrosCierre" id="GrupoCierreContometros_#index#_ContometroCierre" name="cierreContometro[detalle][#index#][contometroCierre]" type="text" disabled class="form-control input-sm" />
									</td>
			      				   <td class="celda-detalle" style="width:40%;">
									 	<input elemento-grupo="idCierreProductoContometro" id="GrupoCierreContometros_#index#_IdCierreProductoContometro" name="cierreContometro[detalle][#index#][idCierreProductoContometro]" type="hidden" disabled class="form-control text-right input-sm"/>
								 		<input elemento-grupo="cierreProductoContometro" id="GrupoCierreContometros_#index#_CierreProductoContometro" name="cierreContometro[detalle][#index#][cierreProductoContometro]" type="text" disabled class="form-control input-sm" />
      								<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="lecturaInicial" id="GrupoCierreContometros_#index#_LecturaInicial" name="cierreContometro[detalle][#index#][lecturaInicial]" type="text" disabled class="form-control text-right input-sm" />
									</td>
									<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="lecturaFinal" id="GrupoCierreContometros_#index#_LecturaFinal" name="cierreContometro[detalle][#index#][lecturaFinal]" type="text" maxlength="8" disabled class="form-control text-right input-sm" />
									</td>
									<td class="celda-detalle" style="width:15%;">
      									<input elemento-grupo="diferencia" id="GrupoCierreContometros_#index#_Diferencia" name="cierreContometro[detalle][#index#][diferencia]" type="text" disabled class="form-control text-right input-sm" />
									</td>
									<td class="celda-detalle" style="width:2%; text-align:center";>
      									<input elemento-grupo="servicio" id="GrupoCierreContometros_#index#_Servicio" name="cierreContometro[detalle][#index#][servicio]" type="checkbox"/>
									</td>
								</tr>
								<tr>
			      				<td></td>
			      				</tr>
			      				<tr id="GrupoCierreContometros_noforms_template">
			      				<td></td>
			      				</tr>    
							</tbody>
						</table>
		                <br />
		                <label><B>Tanques </B></label>
		                <table class="sgo-simple-table table table-condensed" >
							<tbody id="GrupoCierreTanques">
								<tr id="GrupoCierreTanques_template">
								<td>
									<table class="sgo-simple-table table table-condensed" style="width:100%;  border:1px solid black;">
									<thead> <tr> <td> </td></tr></thead>
				      				<tr>
				      					<td style="width:6%;"><label class="text-center">Tanque:				</label></td>
				      					<td class="celda-detalle" colspan="2" style="width:12%;">
				      						<input elemento-grupo="idTanqueJornadaCierre" id="GrupoCierreTanques_#index#_IdTanqueJornadaCierre" name="cierreTanque[detalle][#index#][idTanqueJornadaCierre]" type="hidden" disabled class="form-control input-sm" value="0" />
				      						<input elemento-grupo="idTanqueCierre" id="GrupoCierreTanques_#index#_IdTanqueCierre" name="cierreTanque[detalle][#index#][idTanqueCierre]" type="hidden" disabled class="form-control input-sm" value="0" />
	      									<input elemento-grupo="tanqueCierre" id="GrupoCierreTanques_#index#_TanqueCierre" name="cierreTanque[detalle][#index#][tanqueCierre]" type="text" disabled class="form-control input-sm" value="0" />
										</td>
				      					<td style="width:10%;"><label class="text-center">Producto:				</label></td>
				      					<td class="celda-detalle"  colspan="3">
				      						<input elemento-grupo="idCierreProductoTanque" id="GrupoCierreTanques_#index#_IdCierreProductoTanque" name="cierreTanque[detalle][#index#][idCierreProductoTanque]" type="hidden" disabled class="form-control text-right input-sm"/>
									 		<input elemento-grupo="cierreProductoTanque" id="GrupoCierreTanques_#index#_CierreProductoTanque" name="cierreTanque[detalle][#index#][cierreProductoTanque]" type="text" disabled class="form-control input-sm" />
										</td>
										<td colspan="3"></td>
				      				</tr>
	
				      				<tr>
				      					<td class="celda-detalle"></td>
				      					<td class="celda-detalle text-center"><label>Medida (mm)	</label></td>
										<td class="celda-detalle text-center"><label>Vol. Obs.		</label></td>
										<td class="celda-detalle text-center"><label>API 60F		</label></td>
										<td class="celda-detalle text-center"><label>Temperatura	</label></td>
										<td class="celda-detalle text-center"><label>Factor			</label></td>
				      					<td class="celda-detalle text-center"><label>Volumen 60F	</label></td>
				      					<td class="celda-detalle text-center"><label>Vol. Agua		</label></td>
				      					<td class="celda-detalle text-center"><label>F/S			</label></td>
				      					<td class="celda-detalle text-center"><label>Desp.			</label></td>
				      				</tr>
	
				      				<tr>
				      					<td class="celda-detalle"  style="width:6%; font-weight:bold;"><label class="text-center">Inicial:				</label></td>
				      					
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="medidaInicialTanque" id="GrupoCierreTanques_#index#_MedidaInicialTanque" name="cierreTanque[detalle][#index#][medidaInicialTanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="volObsInicialTanque" id="GrupoCierreTanques_#index#_VolObsInicialTanque" name="cierreTanque[detalle][#index#][volObsInicialTanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="api60InicialTanque" id="GrupoCierreTanques_#index#_Api60InicialTanque" name="cierreTanque[detalle][#index#][api60InicialTanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="temperaturaInicialTanque" id="GrupoCierreTanques_#index#_Temperatura60InicialTanque" name="cierreTanque[detalle][#index#][temperaturaInicialTanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="factorInicialTanque" id="GrupoCierreTanques_#index#_Factor60InicialTanque" name="cierreTanque[detalle][#index#][factorInicial60Tanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="vol60InicialTanque" id="GrupoCierreTanques_#index#_Vol60InicialTanque" name="cierreTanque[detalle][#index#][vol60InicialTanque]" type="text" disabled class="form-control text-right input-sm" value="0" />
										</td>
										<td></td>
										<td class="celda-detalle" style="width:3%; text-align:center";>
	      									<input elemento-grupo="fsInicialTanque" id="GrupoCierreTanques_#index#_FsInicialTanque" name="cierreTanque[detalle][#index#][fsInicialTanque]" type="checkbox" disabled />
										</td>
										<td class="celda-detalle"  style="width:3%; text-align:center";>
	      									<input elemento-grupo="despInicialTanque" id="GrupoCierreTanques_#index#_DespInicialTanque" name="cierreTanque[detalle][#index#][despInicialTanque]" type="checkbox" disabled />
										</td>
									</tr>
									<tr>
				      					<td class="celda-detalle"  style="width:6%; font-weight:bold;"><label class="text-center">Final:				</label></td>
				      					
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="medidaFinalTanque" id="GrupoCierreTanques_#index#_MedidaFinalTanque" name="cierreTanque[detalle][#index#][medidaFinalTanque]" type="text" class="form-control text-right input-sm" maxlength="8" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="volObsFinalTanque" id="GrupoCierreTanques_#index#_VolObsFinalTanque" name="cierreTanque[detalle][#index#][volObsFinalTanque]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="api60FinalTanque" id="GrupoCierreTanques_#index#_Api60FinalTanque" name="cierreTanque[detalle][#index#][api60FinalTanque]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="temperaturaFinalTanque" id="GrupoCierreTanques_#index#_Temperatura60FinalTanque" name="cierreTanque[detalle][#index#][temperaturaFinalTanque]" type="text" class="form-control text-right input-sm" value="0" />
										</td>
				      					<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="factorFinalTanque" id="GrupoCierreTanques_#index#_Factor60FinalTanque" name="cierreTanque[detalle][#index#][factorFinal60Tanque]" type="text" class="form-control text-right input-sm" disabled value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="vol60FinalTanque" id="GrupoCierreTanques_#index#_Vol60FinalTanque" name="cierreTanque[detalle][#index#][vol60FinalTanque]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:10%;">
	      									<input elemento-grupo="volAguaFinalTanque" id="GrupoCierreTanques_#index#_VolAguaFinalTanque" name="cierreTanque[detalle][#index#][volAguaFinalTanque]" type="text" class="form-control text-right input-sm" maxlength="9" value="0" />
										</td>
										<td class="celda-detalle"  style="width:3%; text-align:center";>
	      									<input elemento-grupo="fsFinalTanque" id="GrupoCierreTanques_#index#_FsFinalTanque" name="cierreTanque[detalle][#index#][fsFinalTanque]" type="checkbox" />
										</td>
										<td class="celda-detalle"  style="width:3%; text-align:center";>
	      									<input elemento-grupo="despFinalTanque" id="GrupoCierreTanques_#index#_DespFinalTanque" name="cierreTanque[detalle][#index#][despFinalTanque]" type="checkbox" />
										</td>
									</tr>
									</table>
								  </td>
								</tr>
								<tr id="GrupoCierreTanques_noforms_template">
			      				<td></td>
			      				</tr>  
							</tbody>
						</table>
						<br>
      		            <label>Observaciones</label>
                          <table class="sgo-simple-table table table-condensed">
                            <thead>
                              <tr>
                                <td class="celda-detalle">
    					          <textarea id="cmpObservacionCierre" name="cmpObservacionCierre" type="text" class="form-control input-sm text-uppercase" maxlength="700" rows="1"></textarea>
    				            </td>
                              </tr>
                           </thead>
                         </table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarCierre" type="submit" class="btn btn-primary btn-sm">Guardar</button>
		            <button id="btnCancelarCierre" class="btn btn-danger btn-sm">Cancelar</button>
		            <br />
		    	</div>
		    	<!-- <div class="overlay" id="ocultaContenedorCierreJornada" style="display:none;">
		            <i class="fa fa-refresh fa-spin"></i>
		        </div> -->
			</div>
		</div>
	</div>
	
	<div class="row" id="cntVistaJornada" style="display: none;">
	  <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <table class="sgo-simple-table table table-condensed">
              <thead>
                <tr>
                  <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
    	 		  <td class="celda-detalle" style="width:18%;"><span id='cmpDetalleCliente'>	</span></td>
    	 		  <td></td>
                  <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
    			  <td class="celda-detalle" style="width:18%;"><span id='cmpDetalleOperacion'>	</span></td>
    			  <td></td>
                  <td class="celda-detalle" style="width:7%;"><label>Estaci&oacute;n: </label></td>
                  <td class="celda-detalle" style="width:18%;"><span id='cmpDetalleEstacion'>	</span></td>
                  <td></td>
    			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
               	  <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='cmpDetalleFechaJornada'></span></B></FONT> </td>
               </tr>
               <tr>
             	<td class="celda-detalle" style="width:7%;"><label>Operador 1: </label></td>
     			<td class="celda-detalle">		<span id='cmpDetalleOperador1'>	</span></td>
                 <td></td>
                 <td class="celda-detalle" style="width:7%;"><label>Operador 2: </label></td>
                 <td class="celda-detalle">		<span id='cmpDetalleOperador2'>	</span></td>
                 <td colspan="4"></td>
                 <td class="celda-detalle" style="width:7%;"><label>Estado: </label></td>
                 <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='cmpDetalleEstado'></span></B></FONT> </td>
             </tr>
              </thead>
            </table>
            <label><B>Cont&oacute;metros </B></label>
            <div class="box-body with-border">
				<table id="lista_contometros_jornada" class="sgo-table table table-striped"  style="width:100%;"></table>
			</div>
			<br />
			<label><B>Tanques </B></label>
            <div class="box-body with-border">
				<table id="lista_tanque_jornada" class="sgo-table table table-striped"  style="width:100%;"></table>
			</div>
			<br />
			<label>Observaciones</label>
                          <table class="sgo-simple-table table table-condensed">
                            <thead>
                              <tr>
                                <td class="celda-detalle">
                                  <textarea style="resize: none;" disabled="disabled" rows="3" cols="185" id='cmpDetalleObservacion'></textarea>
    				            </td>
                              </tr>
                           </thead>
                         </table>
			
			
			<!-- <td class="celda-detalle" style="width:7%;"><label>Observaciones: </label></td>
                 <td class="celda-detalle" style="width:15%;">
                 	<textarea style="resize: none;" disabled="disabled" rows="3" cols="60" id='cmpDetalleObservacion'></textarea>	
                 </td>	 -->
			
			<div class="box-body with-border">
				<table id="lista_bitacora" class="sgo-table table table-striped"  style="width:100%;"></table>
			</div>
			<div class="box-footer" align="right">
				<button id="btnCerrarVistaJornada" class="btn btn-danger btn-sm">Cerrar</button>
			</div>	
         	<div class="overlay" id="ocultaContenedorVistaJornada">
            	<i class="fa fa-refresh fa-spin"></i>
          	</div>
        	</div>
      	</div>
	  </div>
	</div>
	
	
	
	<div class="row" id="cntFormularioMuestreoJornada" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-header">
					<form id="frmMuestreo" class="form form-horizontal" role="form form-horizontal" novalidate="novalidate">
	                   <table class="sgo-simple-table table table-condensed">
		                  <thead>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:26%;"><span id='cmpMuestreoCliente'>	</span></td>
		        	 		  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:25%;"><span id='cmpMuestreoOperacion'>	</span></td>
		        			  <td></td>
		                      <td class="celda-detalle" style="width:10%;"><label>Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:25%;"><span id='cmpMuestreoEstacion'>	</span></td>
		                   </tr>
		                   <tr>
			                	<td class="celda-detalle" style="width:7%;"><label>Operador 1: </label></td>
			        			<td class="celda-detalle" style="width:26%;">		<span id='cmpMuestreoOperador1'>	</span></td>
			                    <td></td>
			                    <td class="celda-detalle" style="width:7%;"><label>Operador 2: </label></td>
			                    <td class="celda-detalle" style="width:25%;">		<span id='cmpMuestreoOperador2'>	</span></td>
			                    <td></td>
		        			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:25%;"><FONT COLOR=red><B>	<span id='cmpMuestreoFechaJornada'></span></B></FONT> </td>
			                  <td class="celda-detalle" ></td>
			                </tr>
		                  </thead>
		                </table>

						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      				<tr>
			      				<td class="celda-cabecera-detalle text-center"><label>Hora		</label></td>
			      				<td class="celda-cabecera-detalle text-center"><label>Producto	</label></td>
			      				<td class="celda-cabecera-detalle text-center"><label>API 60F	</label></td>
			      				<td class="celda-cabecera-detalle text-center"><label>Temperatura</label></td>
			      				<td class="celda-cabecera-detalle text-center"><label>Factor	</label></td>
								<td class="celda-cabecera-detalle"></td>
			      				<td class="celda-cabecera-detalle"></td>
			      				</tr>
			      			</thead>
			      			<tbody id="GrupoMuestreo">
			      				<tr id="GrupoMuestreo_template">
			      					<td class="celda-detalle">
			      						<input elemento-grupo="identificador" id="GrupoMuestreo_#index#_Identificador" name="muestra[detalle][#index#][identificador]" type="hidden" class="form-control input-sm text-right" />
			      						<input elemento-grupo="horaMuestra" id="GrupoMuestreo_#index#_HoraMuestra" name="muestra[detalle][#index#][horaMuestra]" type="text" class="form-control text-center input-sm" data-inputmask="'mask': 'd/m/y h:s:s'" />
			      					</td>
			      				   <td class="celda-detalle" style="width:40%;">
			                    		<select tipo-control="select2" elemento-grupo="producto" id="GrupoMuestreo_#index#_Producto" style="width: 100%" name="muestra[detalle][#index#][producto]" class="form-control input-sm text-uppercase">
			                    			<option value="" selected="selected">SELECCIONAR...</option>
			                    		</select>
			      				   </td>
			      					<td class="celda-detalle">
				      					<input elemento-grupo="api60" id="GrupoMuestreo_#index#_Api60" name="muestra[detalle][#index#][api60]" type="text" class="form-control text-right input-sm" />
			      					</td>
			      					<td class="celda-detalle">
			      					<input elemento-grupo="temperatura" id="GrupoMuestreo_#index#_Temperatura" name="muestra[detalle][#index#][temperatura]" type="text" class="form-control text-right input-sm" />
			      					</td>
			      					<td class="celda-detalle">
			      					<input elemento-grupo="factor" id="GrupoMuestreo_#index#_Factor" name="muestra[detalle][#index#][factor]" type="text" disabled class="form-control text-right input-sm" />
			      					</td>
			      					<td class="celda-detalle">
			      					 <a elemento-grupo="botonModifica" id="GrupoMuestreo_#index#_modifica" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-edit"></i></a>
			      					</td>
			      					<td class="celda-detalle">
			      					 <a elemento-grupo="botonElimina" id="GrupoMuestreo_#index#_elimina" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-remove"></i></a>
			      					</td>
			      				</tr>
			      				<tr id="GrupoMuestreo_noforms_template">
			      				<td></td>
			      				</tr>    			
			      			</tbody>
			      		</table>
					</form>
				</div>
				<div id="frmConfirmarEliminarMuestra" class="modal" data-keyboard="false" data-backdrop="static">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
							</div>
							<div class="modal-body">
								<p><%=mapaValores.get("MENSAJE_ELIMINAR_REGISTRO")%></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
								<button id="btnConfirmaEliminarMuestra" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
							</div>
						</div>
					</div>
	      		</div>
				<div class="box-footer">
		          <a id="btnGuardarMuestreo" class="btn btn-primary btn-sm"> <i class="fa fa-save"></i>  <%=mapaValores.get("ETIQUETA_BOTON_GUARDAR")%></a>
		          <a id="btnAgregarMuestra" class="btn bg-purple btn-sm"><i class="fa fa-plus-circle"></i>  Agregar Muestra</a>
		          <a id="btnCancelarMuestreo" class="btn btn-danger btn-sm"><i class="fa fa-close"></i>  <%=mapaValores.get("ETIQUETA_BOTON_CANCELAR")%></a>
		        </div>
				<div class="overlay" id="ocultaContenedorMuestreoJornada" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</div>
	</div>

	</section>
</div>