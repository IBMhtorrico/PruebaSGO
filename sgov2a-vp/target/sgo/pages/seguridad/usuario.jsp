<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Transportista"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Usuarios / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
			                <label for="txtFiltro" class="espaciado">Nombre: </label>
			                <input id="txtFiltro" type="text" class="form-control espaciado text-uppercase input-sm"  placeholder="Buscar..." maxlength="16">
			              </div>
			              <div class="form-group">
			                <label for="cmpFiltroEstado" class="espaciado">Estado: </label>
			                <select id="cmpFiltroEstado" name="cmpFiltroEstado" class="form-control espaciado input-sm">
			                  <option value="<%=mapaValores.get("FILTRO_TODOS")%>"><%=mapaValores.get("TEXTO_TODOS")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_ACTIVO")%>"><%=mapaValores.get("TEXTO_ACTIVO")%></option>
			                  <option value="<%=mapaValores.get("ESTADO_INACTIVO")%>"><%=mapaValores.get("TEXTO_INACTIVO")%></option>
			                </select>
			              </div>
			              <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  Filtrar</a>
			            </form> 
					</div>
					<!-- BOTONES PARA EL MANTENIMIENTO -->
					<div class="box-header">
						<div>
							<a id="btnAgregar" class="btn btn-default btn-sm espaciado"><i class="fa fa-plus"></i> <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a> 
							<a id="btnBuscarUsuarios" class="btn btn-default btn-sm espaciado"><i class="fa fa-binoculars"></i> Buscar Usuario Corporativo</a> 
							<a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a> 
							<a id="btnModificarEstado" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-cloud-upload"></i>  <%=mapaValores.get("ETIQUETA_BOTON_ACTIVAR")%></a> 
							<a id="btnAutorizar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-key"></i> <%=mapaValores.get("ETIQUETA_BOTON_AUTORIZAR")%></a> 
							<a id="btnVer" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
							<!-- <a id="btnResetearClave" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-search"></i>  Resetear Contrase&#241a</a> -->
						</div>
					</div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" width="100%">
              <thead>
					<tr>
						<th>N#</th>
						<th>ID</th>
						<th>Nombre</th>
						<th>Identidad</th>
						<th>Rol</th>
						<th>Operacion</th>
						<th>Estado</th>
					</tr>
				</thead>
            </table>
            <!-- Esto para la confirmación de cambio de estado del registro -->
		            <div id="frmConfirmarModificarEstado" class="modal">
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
	
	
	<div class="row" id="cntUsuarioLDAP" style="display:none;">
		<div class="col-xs-12">
			<div class="box">
				<div class="box-header">
					<form class="form-inline" role="form">
		              <div class="form-group">
		                <label for="txtBuscarUsuarioLDAP" class="espaciado">Buscar: </label>
		                <input id="txtBuscarUsuarioLDAP" type="text" class="form-control espaciado text-uppercase input-sm"  placeholder="Buscar...">
		              </div>
		              <a id="btnFiltrarUsuarioLDAP" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  Filtrar</a>
		            </form> 
				</div>
				<!-- BOTONES PARA EL MANTENIMIENTO -->
				<div class="box-header">
					<div>
					  <button id="btnSeleccionar" type="submit" class="btn btn-primary btn-sm">Seleccionar</button>
            		  <button id="btnCancelarUsuarioLDAP"  class="btn btn-danger btn-sm">Cancelar</button>
					</div>
				</div>
          <div class="box-body">
            <table id="tablaUsuarioLDAP" class="sgo-table table table-striped" width="100%">
              <thead>
					<tr>
						<th>N#</th>
						<th>Nombre</th>
						<th>Identidad</th>
						<th>Email</th>
						<th>Tipo</th>
					</tr>
				</thead>
            </table>
           	</div>
           	<div class="overlay" id="ocultaContenedorTablaUsuarioLDAP">
	            	<i class="fa fa-refresh fa-spin"></i>
	          	</div>
        </div>
    	</div>
	</div>
	
    <!-- Aqui empieza el formulario -->        
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <form id="frmPrincipal" role="form">
				<div class="form-group">
					<label>Usuario</label> 
            		<input name="cmpNombre" id="cmpNombre" type="text" class="form-control input-sm" maxlength="16" required placeholder="" />
				</div>
				<div class="form-group">
					<label>Contraseña</label>
					<FONT COLOR=red><B><label>(Debe contener como mínimo 8 caracteres alfanum&eacute;ricos, 1 letra en may&uacute;scula. No debe tener n&uacute;meros al inicio ni al final. Los caracteres no deben repetirse 3 veces seguidas.)</label></B></FONT> 
            		<input name="cmpClave" id="cmpClave" type="password" class="form-control input-sm" required placeholder="" maxlength="64"/>
				</div>
				<div class="form-group">
					<label>Confirmar Contraseña</label> 
            		<input name="cmpConfirmaClave" id="cmpConfirmaClave" type="password" class="form-control input-sm" required placeholder="" maxlength="64"/>
				</div>
				<div class="form-group">
					<label>Nombre</label> 
            		<input name="cmpIdentidad" id="cmpIdentidad" type="text" class="form-control input-sm" maxlength="120" required placeholder="" />
				</div>
				<div class="form-group">
					<label>Email</label>
					<div class="input-group">
						<span class="input-group-addon">
							<i class="fa fa-envelope"></i>
						</span>
						<input  name="cmpEmail" id="cmpEmail" class="form-control input-sm"  type="email" maxlength="50" placeholder="Email">
					</div>
				</div>
				<div class="form-group">
					<label>Zona Horaria</label> 
		            <select tipo-control="select2" name="cmpZonaHoraria" id="cmpZonaHoraria" class="form-control input-sm" style="width: 100%">
		              <%-- <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
		              <option value="-12">UTC -12</option>
		              <option value="-11">UTC -11</option>
		              <option value="-10">UTC -10</option>
		              <option value="-09">UTC -9</option>
		              <option value="-08">UTC -8</option>
		              <option value="-07">UTC -7</option>
		              <option value="-06">UTC -6</option>
		              <option value="-05">UTC -5</option>
		              <option value="-04">UTC -4</option>
		              <option value="-03">UTC -3</option>
		              <option value="-02">UTC -2</option>
		              <option value="-01">UTC -1</option>
		              <option value="+00">UTC +0</option>
		              <option value="+01">UTC +1</option>
		              <option value="+02">UTC +2</option>
		              <option value="+03">UTC +3</option>
		              <option value="+04">UTC +4</option>
		              <option value="+05">UTC +5</option>
		              <option value="+06">UTC +6</option>
		              <option value="+07">UTC +7</option>
		              <option value="+08">UTC +8</option>
		              <option value="+09">UTC +9</option>
		              <option value="+10">UTC +10</option>
		              <option value="+11">UTC +11</option>
		              <option value="+12">UTC +12</option>
		              <option value="+13">UTC +13</option>
		              <option value="+14">UTC +14</option> --%>
		            </select>
				</div>
				<div class="form-group">
					<label>Rol</label> 
            		<select tipo-control="select2" id="cmpIdRol" name="cmpIdRol" class="form-control input-sm" style="width: 100%">
						<option value="" selected="selected">SELECCIONAR...</option>
					</select>
				</div>
          		<div class="form-group">
					<label>Cliente</label>
					<select id="cmpIdCliente" name="cmpIdCliente" class="form-control input-sm" style="width: 100%">
						<option data-idCliente="0" value="0" selected="selected">TODOS</option>
						<%
	                    HashMap<String,String> listaClientes ;
						ArrayList<?> listadoClientes;
						listadoClientes = (ArrayList<?>) request.getAttribute("listadoClientes"); 
	                    Cliente eCliente=null;
	                    for(int contador=0; contador < listadoClientes.size(); contador++){ 
	                     eCliente =(Cliente) listadoClientes.get(contador);
	                    %>
	                    <option selected="selected" data-idCliente='<%=eCliente.getId()%>' value='<%=eCliente.getId()%>'><%=eCliente.getNombreCorto().trim() %></option>
	                    <%} %>
					</select>

				</div>
          		<div class="form-group">
					<label>Operacion</label>
            		<select tipo-control="select2" id="cmpIdOperacion" name="cmpIdOperacion" class="form-control input-sm" style="width: 100%">
            			<option value="0">TODOS</option>
					</select>
				</div>
				<div class="form-group">
					<label>Transportista</label>
					<select tipo-control="select2" id="cmpIdTransportista" name="cmpIdTransportista" class="form-control input-sm" style="width: 100%">
            			<option value="0">SELECCIONAR...</option>
					</select>
					
						
					
				</div>
			</form>
          </div>
          <div class="box-footer">
            <button id="btnGuardar" type="submit" class="btn btn-primary btn-sm">Guardar</button>
            <button id="btnCancelarGuardar"  class="btn btn-danger btn-sm">Cancelar</button>
          </div>
          <div class="overlay" id="ocultaFormulario" style="display:none;">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
  
  
  <div class="row" id="cntAutorizacion" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-header">
            <h3 class="box-title">Asignar Autorizaci&oacute;n a Usuario</h3>
          </div>
          <div class="box-body">
            <form id="frmAutorizacion" role="form">
            	<div class="col-xs-12">
					<div class="row">
					<br />
						<div class="col-md-1">	<label>Usuario:</label>						</div>
						<div class="col-md-3">	<span id='usuario'>	</span> 		</div>
						<div class="col-md-1">	<label>Rol:</label>			</div>
						<div class="col-md-3">	<span id='RolUsuario'></span> 		</div>
						<div class="col-md-2">	<label> Operaci&oacute;n:</label>	</div>
						<div class="col-md-2">	<span id='OperacionUsuario'></span></div>
					</div>
					<div class="col-md-12">
						 <table id="tablaSecundaria" class="sgo-table table table-striped" cellspacing="0" width="100%">
						   <thead>
						      <tr>
						         <th></th>
						         <th>ID</th>
						         <th>Autorizaci&oacute;n</th>
						   	  </tr>
						   </thead>
						</table>
					</div>
				</div>
			</form>
          </div>
          <div class="box-footer">
            <button id="btnGuardarAutorizacion" type="submit" class="btn btn-sm btn-primary">Guardar</button>
            <button id="btnCancelarAutorizacion"  class="btn btn-sm btn-danger">Cancelar</button>
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
            <table class="sgo-table table table-striped" style="width:100%;">
            <tbody>
						<tr>
							<td>Id:</td>
							<td><span id='vistaId'></span></td>
						</tr>
						<tr>
							<td>Nombre:</td>
							<td><span id='vistaNombre'></span></td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td>Clave:</td> -->
<!-- 							<td><span id='vistaClave'></span></td> -->
<!-- 						</tr> -->
						<tr>
							<td>Identidad:</td>
							<td><span id='vistaIdentidad'></span></td>
						</tr>
						<tr>
							<td>Zona Horaria:</td>
							<td><span id='vistaZonaHoraria'></span></td>
						</tr>
						<tr>
							<td>Email:</td>
							<td><span id='vistaEmail'></span></td>
						</tr>
						<tr>
							<td>Rol:</td>
							<td><span id='vistaIdRol'></span></td>
						</tr>
						<tr>
							<td>Transportista:</td>
							<td><span id='vistaIdTransportista'></span></td>
						</tr>
						<tr>
							<td>Tipo:</td>
							<td><span id='vistaTipo'></span></td>
						</tr>
						<tr>
							<td>Operacion:</td>
							<td><span id='vistaIdOperacion'></span></td>
						</tr>
						<tr>
							<td>Estado:</td>
							<td><span id='vistaEstado'></span></td>
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