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
    <h1>Registro de Despacho / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
                    <!-- <td></td>
                    <td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Estación: </label></td>
                    <td  class="celda-detalle" style="width:20%;"><select style="width:100%;"  id="filtroEstacion" name="filtroEstacion" class="form-control input-sm">
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
            <a id="btnDetalle" class="btn btn-default btn-sm espaciado"><i class="fa fa-search"></i>  Detalle</a>
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
          </div>
		 	<div class="overlay" id="ocultaContenedorTabla">
	            <i class="fa fa-refresh fa-spin"></i>
	        </div>   
        </div>
      </div>
    </div>
    
    <!-- contenedor del detalle del despacho -->
    <div class="row" id="cntDetalleDespacho" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-header with-border">
					<h3  id="cmpTituloFormulario" class="box-title"></h3>
				</div>
				<table class="sgo-simple-table table table-condensed">
	                <thead>
	                  <tr>
	                    <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
	        			<td class="celda-detalle" style="width:18%;"><span id='detalleCliente'>	</span></td>
	                    <td></td>
	                    <td class="celda-detalle" style="width:7%;"><label>Operaci&oacute;n: </label></td>
	        			<td class="celda-detalle" style="width:18%;"><span id='detalleOperacion'>	</span></td>
	                    <td></td>
	                    <td class="celda-detalle" style="width:7%;"><label>Estaci&oacute;n: </label></td>
	                    <td class="celda-detalle" style="width:18%;"><span id='detalleEstacion'>	</span></td>
	        			<td></td>
	        			<td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
		                <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='detalleFechaJornada'></span></B></FONT> </td>
	                </tr>
	                </thead>
	              </table>
				
				
				<div class="box-header with-border">
					<div class="col-xs-12">
						<div  class="col-md-12">
							<div>
		           				<a id="btnImportar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  Importar</a>
					            <a id="btnVerImportacion" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  Ver</a>
		          			</div>
							 <table id="tablaDespachoCarga" class="sgo-table table table-striped" width="100%">
			       				<thead>
			          					<tr>
										<th class="text-center">#</th>
										<th class="text-center">ID</th>
										<th class="text-center">Archivo </th>
								    	<th class="text-center">Fecha Creacci&oacute;n</th>
								    	<th class="text-center">Comentario</th>
								    	<th class="text-center">Operario</th>
			          					</tr>
			        				</thead>
       						</table> 
						</div>
					</div>
          			<div>
           				<a id="btnAgregarDespacho" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i>  Agregar</a>
           				<a id="btnModificarDespacho" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-edit"></i>  Modificar</a>
			            <a id="btnAnularDespacho" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-download"></i>  Anular</a>
			            <a id="btnVerDespacho" class="btn btn-default disabled btn-sm espaciado"><i class="fa fa-search"></i>  Ver</a>
          			</div>
        		</div>
          		<div class="box-body">
	   				<table id="tablaDespacho" class="sgo-table table table-striped" width="100%">
        				<thead>
           					<tr>
			 					<th>#</th>
			 					<th>ID</th>
			                 	<th>D&iacute;a Operativo</th>
			                 	<th>Estado</th>
			                 	<th>Nro Despacho</th>
			                 	<th>Producto</th>
			                 	<th>Cont&oacute;metro</th>
								<th>Inicial</th>
								<th>Final</th>
								<th>Vol. Observado</th>
								<th>Origen</th>
			                </tr>
         				</thead>
       				</table>
       				<div id="frmConfirmarAnularEstado" class="modal" data-keyboard="false" data-backdrop="static">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<h4 class="modal-title"><%=mapaValores.get("TITULO_VENTANA_MODAL")%></h4>
								</div>
								<div class="modal-body">
									<p><%=mapaValores.get("MENSAJE_ANULAR_REGISTRO")%></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default pull-left" data-dismiss="modal"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
									<button id="btnConfirmarAnularRegistro" type="button" class="btn btn-primary"><%=mapaValores.get("ETIQUETA_BOTON_CONFIRMAR")%></button>
								</div>
							</div>
						</div>
					</div>
	   			</div>
	   			<div class="box-footer" align="right">
					<button id="btnRegresar" class="btn btn-sm espaciado btn-danger">Regresar</button>
				</div>
				<div class="overlay" id="ocultaContenedorDetalleDespacho">
			    	<i class="fa fa-refresh fa-spin"></i>
			    </div>
	   		</div>
		</div>
	</div>
	
	<div class="row" id="cntImportacion" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					<form id="frmImportacion" role="form">
						<table class="sgo-simple-table table table-condensed">
		                  <thead>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label>Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:18%;"><span id='cmpImportacionCliente'>	</span></td>
		                      <td class="celda-detalle" style="width:5%;"><label>Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:18%;"><span id='cmpImportacionOperacion'>	</span></td>
		                      <td class="celda-detalle" style="width:5%;"><label>Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:18%;"><span id='cmpImportacionEstacion'>	</span></td>
		        			  <td class="celda-detalle" style="width:10%;"><label>D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:15%;"><FONT COLOR=red><B>	<span id='cmpImportacionFechaJornada'></span></B></FONT> </td>
			                  <td class="celda-detalle" ></td>
		                   </tr>
		                  </thead>
		                </table>
						<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      			  <tr>
			      				<td class="celda-detalle" style="width:5%;"> 
			      					<label class="etiqueta-titulo-horizontal">Archivo: </label> 
			      				</td>
			      				<td class="celda-detalle" style="width:67%;"> 
<!-- 			      					<input name="cmpArchivoImportacion" id="cmpArchivoImportacion" type="text" class="form-control espaciado input-sm text-uppercase" placeholder="" /> -->
			      						<input name="cmpArchivo" id="cmpArchivo" type="file" />
			      				</td>
<!-- 			      				<td class="celda-detalle" style="width:8%;">  -->
<!-- 			      					<a id="btnExaminar" class="btn btn-default btn-sm espaciado"> Examinar...</a>  -->
<!-- 			      				</td> -->
			      				<td class="celda-detalle"></td>
			      			  </tr>
			      		      <tr>
			      				<td class="celda-detalle" style="width:5%;"> <label class="etiqueta-titulo-horizontal">Operario: </label>				</td>
			      				<td colspan="2" class="celda-detalle" style="width:75%;"><select style="width:100%;"  id="cmpOperarioImportacion" name="cmpOperarioImportacion" class="form-control input-sm">
				                    	<option value="0" selected="selected">Seleccionar...</option>
				                    </select>
				                </td>
			      			  </tr>
							  <tr>
			      				<td class="celda-detalle" style="width:5%;"> <label class="etiqueta-titulo-horizontal">Comentario: </label> 					</td>
			      				<td colspan="2" class="celda-detalle" style="width:75%;"> 
									<textarea class="form-control input-sm text-uppercase" required id="cmpComentarioImportacion" name="cmpComentarioImportacion" placeholder="Ingrese comentario..." rows="3" ></textarea>
								</td>
			      			   </tr>
			      			</thead>
			      			<tbody>      				
			      			</tbody>
			      		</table>
					</form>
				</div>
				<div class="box-footer">
		            <button id="btnGuardarImportacion" type="submit" class="btn btn-primary btn-sm">Guardar</button>
		            <button id="btnCancelarImportacion" class="btn btn-danger btn-sm">Cancelar</button>
		            <br />
		    	</div>
		    	<div class="overlay" id="ocultaContenedorImportacion" style="display:none;">
		            <i class="fa fa-refresh fa-spin"></i>
		        </div>
			</div>
		</div>
	</div>
	
	<div class="row" id="cntVistaImportacion" style="display: none;">
	  <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <table class="sgo-table table table-striped" style="width:100%;">
              <tbody>
                <tr>
                  <td>Cliente:</td>				<td> <span id='vistaImportacionCliente'></span> </td>
                </tr>                
                <tr>
                  <td>Operaci&oacute;n:</td>	<td> <span id="vistaImportacionOperacion"></span> </td>
                </tr>
                <tr>
                  <td>Estaci&oacute;n:</td>		<td> <span id="vistaImportacionEstacion"></span> </td>
                </tr>
                <tr>
                  <td>D&iacute;a Operativo:</td> <td> <span id="vistaImportacionFechaJornada"></span> </td>
                </tr>
                <tr>
                  <td>Archivo:</td>				<td> <span id="vistaArchivoImportacion"></span> </td>
                </tr>
                <tr>
                  <td>Operario:</td>			<td> <span id="vistaOperarioImportacion"></span> </td>
                </tr>
                <tr>
                  <td>Comentario:</td>			<td> <span id="vistaComentarioImportacion"></span> </td>
                </tr>
                <tr>
                  <td>Creado el:</td>			<td> <span id="vistaImportacionCreadoEl"></span> </td>
                </tr>
                <tr> 
                  <td>Creado por:</td>			<td> <span id="vistaImportacionCreadoPor"></span> </td>
                </tr>
                <tr>
                  <td>IP Creaci&oacute;n:</td>	<td> <span id="vistaImportacionIpCreacion"></span> </td>
                </tr>
                <tr>
                  <td>Actualizado el:</td>		<td> <span id="vistaImportacionActualizadoEl"></span> </td>
                </tr>
                <tr>
                  <td>Actualizado por:</td>		<td> <span id="vistaImportacionActualizadoPor"></span> </td>
                </tr>
                <tr>
                  <td>IP Actualizaci&oacute;n:</td>  <td> <span id="vistaImportacionIpActualizacion"></span> </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="box-footer">
            <button id="btnCerrarVistaImportacion"  class="btn btn-danger btn-sm"><%=mapaValores.get("ETIQUETA_BOTON_CERRAR")%></button>
          </div>
          <div class="overlay" id="ocultaContenedorVistaImportacion">
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
		                  <thead> </thead>
		                  <tbody>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Cliente: </label></td>
		        	 		  <td class="celda-detalle" style="width:25%;"><span id='cmpFormularioCliente'>	</span></td>
		        	 		  <td></td>
		                      <td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Operaci&oacute;n: </label></td>
		        			  <td class="celda-detalle" style="width:25%;"><span id='cmpFormularioOperacion'>	</span></td>
		        			  <td></td>
		                      <td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Estaci&oacute;n: </label></td>
		                      <td class="celda-detalle" style="width:25%;"><span id='cmpFormularioEstacion'>	</span></td>
		                    </tr>
		                    <tr>
		                      <td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">D&iacute;a Operativo: </label></td>
			                  <td class="celda-detalle" style="width:25%;"><FONT COLOR=red><B>	<span id='cmpFormularioFechaJornada'></span></B></FONT> </td>
			                  <td></td>
		                      <td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Hora apertura turno: </label></td>
			                  <td class="celda-detalle" style="width:25%;"><FONT COLOR=red><B>	<span id='cmpHoraAperturaTurno'></span></B></FONT> </td>
			                  <td class="celda-detalle" ></td>
		                   </tr>
		                  </tbody>
		                </table>
			      			
		      			<table class="sgo-simple-table table table-condensed">
			      			<thead>
			      			</thead>
			      			<tbody>
			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Veh&iacute;culo: </label> </td>
			      				<td class="celda-detalle" style="width:25%;"><select tipo-control="select2" id="cmpIdVehiculo" name="cmpIdVehiculo" class="form-control text-uppercase input-sm"  style="width: 100%">
										<option value="" selected="selected">Seleccionar</option>
									</select>
								</td>
								<td></td>
								<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Propietario: </label> </td>
			      				<td class="celda-detalle" style="width:25%;"><input name="cmpIdPropietario" id="cmpIdPropietario" maxlength="50" disabled type="text" class="form-control input-sm" required placeholder="" />
								</td>
								<td></td>
								<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Clasificaci&oacute;n: </label></td>
			      				<td class="celda-detalle" style="width:25%;"><select tipo-control="select2" id="cmpIdClasificacion" name="cmpIdClasificacion" class="form-control input-sm" style="width: 100%">
									  <option value="">SELECCIONAR...</option>
						              <option value="1">TRANSFERIDO</option>
						              <option value="2">RECIRCULADO</option>
									</select>
								</td>
								</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">K.M. / Hor&oacute;metro: </label> </td>
			      				<td class="celda-detalle" style="width:25%;"><input name="cmpKmHorometro" id="cmpKmHorometro" maxlength="15" type="text" class="form-control input-sm" placeholder="" /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Nro Vale: </label> </td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpNumeroVale" id="cmpNumeroVale" maxlength="15" type="text" class="form-control input-sm" required placeholder="" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Hora Inicio: </label> </td>
			      				<td class="celda-detalle" style="width:25%;"><input id="cmpHoraInicio" name="cmpHoraInicio" type="text" class="form-control input-sm" value="" placeholder="HH:mm:ss"/>
			      				</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Hora Fin: </label></td>
			      				<td class="celda-detalle" style="width:25%;"><input id="cmpHoraFin" name="cmpHoraFin" type="text" class="form-control input-sm" value="" placeholder="HH:mm:ss"/>
			      				</td>
			      				<td></td>
			      				</tr>

			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Producto: </label> </td>
			      				<td class="celda-detalle" colspan="4"><select tipo-control="select2" id="cmpIdProducto" name="cmpIdProducto" class="form-control espaciado text-uppercase input-sm" required style="width: 100%">
										<option value="" selected="selected">Seleccionar</option>
									</select>
								</td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Cont&oacute;metro: </label> </td>
			      				<td class="celda-detalle" style="width:25%;"><select tipo-control="select2" id="cmpIdContometro" name="cmpIdContometro" disabled class="form-control espaciado text-uppercase input-sm" style="width: 100%">
										<option value="" selected="selected">Seleccionar</option>
									</select>
								</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Tanque: </label></td>
			      				<td class="celda-detalle" style="width:25%;"><select tipo-control="select2" id="cmpIdTanque" name="cmpIdTanque" disabled class="form-control espaciado text-uppercase input-sm"  style="width: 100%">
										<option value="" selected="selected">Seleccionar</option>
									</select>
								</td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Factor: </label></td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpFactor" id="cmpFactor" maxlength="10" type="text" readonly="readonly" class="form-control text-right input-sm" placeholder="" /></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Lec. Inicial: </label> </td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpLecturaInicial" id="cmpLecturaInicial" maxlength="8" type="text" class="form-control text-right input-sm" placeholder="" value="0"/></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Lec. Final: </label></td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpLecturaFinal" id="cmpLecturaFinal" maxlength="8" type="text" class="form-control text-right input-sm" placeholder="" value="0"/></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Vol. Obs.: </label></td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpVolObservado" id="cmpVolObservado" maxlength="8" type="text" class="form-control text-right input-sm" placeholder="" value="0"/></td>
			      				</tr>
			      				
			      				<tr>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">API 60F: </label> </td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpAPI60" id="cmpAPI60" maxlength="15" type="text" class="form-control text-right input-sm" placeholder="" /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Temp (F): </label></td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpTemperatura" id="cmpTemperatura" maxlength="15" type="text" class="form-control text-right input-sm" placeholder="" /></td>
			      				<td></td>
			      				<td class="celda-detalle" style="width:7%;"><label class="etiqueta-titulo-horizontal">Volumen 60F: </label></td>
			      				<td class="celda-detalle"  style="width:25%;"><input name="cmpVolumen60" id="cmpVolumen60" maxlength="8" type="text" class="form-control text-right input-sm" placeholder="" /></td>
			      				</tr>
			      		</tbody>
			      	  </table>
					</form>
				</div>
				<div class="box-footer col-xs-12">
					<div class="col-md-1">
						<button id="btnGuardarDespacho" type="submit" class="btn btn-primary btn-sm">Guardar</button>
					</div>
					<div class="col-md-10"></div>
					<div class="col-md-1">
						<button id="btnCancelarGuardarDespacho" class="btn btn-danger btn-sm">Cancelar</button>
					</div>
				</div>
				<div class="overlay" id="ocultaContenedorFormulario" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row" id="cntVistaDespacho" style="display: none;">
		<div class="col-md-12">
			<div class="box box-default">
				<div class="box-body">
					   <table class="sgo-table table"  style="width:100%;">
		                  <thead>
		                  </thead>
		                  <tbody>
		                    <tr>
		                      <td class="tabla-vista-titulo" style="width:10%;">Cliente: </td>
		        	 		  <td style="width:18%;"><span class="text-uppercase" id='vistaFormularioCliente'>		</span></td>
		                      <td class="tabla-vista-titulo" style="width:5%;">Operaci&oacute;n: </td>
		        			  <td style="width:18%;"><span class="text-uppercase" id='vistaFormularioOperacion'>	</span></td>
		                      <td class="tabla-vista-titulo" style="width:5%;">Estaci&oacute;n: </td>
		                      <td style="width:18%;"><span class="text-uppercase" id='vistaFormularioEstacion'>		</span></td>
		        			  <td class="tabla-vista-titulo" style="width:10%;">D&iacute;a Operativo: </td>
			                  <td style="width:15%;"><span class="text-uppercase" id='vistaFormularioFechaJornada'>	</span></td>
		                   </tr>
		                  </tbody>
		                </table>
			      			
		      			<table class="sgo-table table"  style="width:100%;">
			      			<thead>
			      			</thead>
			      			<tbody>
			      				<tr>
			      				<td class="tabla-vista-titulo" style="width:10%;">Propietario:			</td>
			      				<td style="width:23%;"> <span id='vistaIdPropietario'></span>			</td>

								<td class="tabla-vista-titulo" style="width:10%;">Veh&iacute;culo:		</td>
			      				<td style="width:23%;"> <span id='vistaIdVehiculo'></span>									</td>

								<td class="tabla-vista-titulo" style="width:10%;">Clasificaci&oacute;n:	</td>
			      				<td style="width:23%;"> <span id='vistaIdClasificacion'></span>	</td>
								</tr>
			      				
			      				<tr>
			      				<td class="tabla-vista-titulo" style="width:10%;">K.M. / Hor&oacute;metro:	</td>
			      				<td style="width:15%;"> <span id='vistaKmHorometro'></span>				</td>

								<td class="tabla-vista-titulo" style="width:10%;">Nro Vale:				</td>
			      				<td style="width:15%;"> <span id='vistaNumeroVale'></span>				</td>
			      				
			      				<td colspan="2"></td>
			      				</tr>

			      				<tr>
			      				<td class="tabla-vista-titulo" style="width:10%;">Fecha Inicio:			</td>
			      				<td style="width:15%;"> <span id='vistaFechaInicio'></span>				</td>
			  
							  	<td class="tabla-vista-titulo" style="width:10%;">Fecha Fin:			</td>    				
			      				<td style="width:15%;"> <span id='vistaFechaFin'></span>				</td>
			      				
			      				<td colspan="2"></td>
			      				</tr>

			      				<tr>
								<td class="tabla-vista-titulo" style="width:10%;">Producto:				</td>			      				
			      				<td colspan="5" style="width:15%;"> <span id='vistaIdProducto'></span>				</td>
			      				</tr>
			      				
			      				<tr>
								<td class="tabla-vista-titulo" style="width:10%;">Cont&oacute;metro:	</td>			      				
			      				<td style="width:15%;"> <span id='vistaIdContometro'></span>			</td>
			      				
								<td class="tabla-vista-titulo" style="width:10%;">Tanque:				</td>				      				
			      				<td style="width:15%;"> <span id='vistaIdTanque'></span>				</td>

								<td class="tabla-vista-titulo" style="width:10%;">Vol. Observado:		</td>				      							      				
			      				<td  class="text-right" style="width:15%;"> <span id='vistaVolObservado'></span>			</td>
			      				</tr>
			      				
			      				<tr>
						      	<td class="tabla-vista-titulo" style="width:10%;">Lec. Inicial:			</td>			
			      				<td class="text-right" style="width:15%;"> <span id='vistaLecturaInicial'></span>			</td>
			      				
			      				<td class="tabla-vista-titulo" style="width:10%;">Lec. Final:			</td>			
			      				<td class="text-right" style="width:15%;"> <span id='vistaLecturaFinal'></span>			</td>
			      				
			      				<td class="tabla-vista-titulo" style="width:10%;">Factor:				</td>			
			      				<td class="text-right" style="width:15%;"> <span id='vistaFactor'></span>					</td>
			      				</tr>
			      				
			      				<tr>
								<td class="tabla-vista-titulo" style="width:10%;">API 60F:				</td>			      				
			      				<td class="text-right" style="width:15%;"> <span id='vistaAPI60'></span>					</td>

								<td class="tabla-vista-titulo" style="width:10%;">Temp (F):				</td>			      				
			      				<td class="text-right" style="width:15%;"> <span id='vistaTemperatura'></span>				</td>

								<td class="tabla-vista-titulo" style="width:10%;">Volumen 60F:			</td>			      				
			      				<td class="text-right" style="width:15%;"> <span id='vistaVolumen60'></span>				</td>
			      				</tr>
			      		</tbody>
			      	  </table>
					
					<table class="sgo-table table" style="width:100%;">
						<thead>
			      		</thead>
						<tbody>
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Creado el:					</td>
							<td style="width:23%;"> <span id='vistaCreadoEl'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">Creado Por:					</td>
							<td style="width:23%;"> <span id='vistaCreadoPor'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">IP Creaci&oacute;n:			</td>
							<td style="width:23%;"> <span id='vistaIpCreacion'></span>						</td>
							</tr>
							
							<tr>
							<td class="tabla-vista-titulo" style="width:10%;">Actualizado el:			</td>
							<td style="width:23%;"> <span id='vistaActualizadoEl'></span>		</td>
							<td class="tabla-vista-titulo" style="width:10%;">Actualizado Por:	</td>
							<td style="width:23%;"> <span id='vistaActualizadoPor'></span>	</td>
							<td class="tabla-vista-titulo" style="width:10%;">IP Actualizaci&oacute;n:	</td>
							<td> <span id='vistaIpActualizacion'></span>		</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="box-footer" align="right">
					<button id="btnCerrarVistaDespacho" class="btn btn-danger btn-sm">Cerrar</button>
				</div>
				<div class="overlay" id="ocultaContenedorVistaDespacho">
			      <i class="fa fa-refresh fa-spin"></i>
			    </div>
			</div>
		</div>
	</div>

	</section>
</div>