<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Usuario"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Bitacora / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
  </section>
  <!-- Contenido principal -->
  <section class="content" id="cntInterface">    
    <!-- El contenido debe incluirse aqui-->
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
          		<table class="sgo-simple-table table table-condensed">
	      			<thead>
	      				<tr>
		      				<td class="celda-detalle" style="width:10%;"><label class="etiqueta-titulo-horizontal">Rango de fecha: </label></td>
		      				<td class="celda-detalle" style="width:15%;"><% String fechaActual = (String) request.getAttribute("fechaActual"); %>
	                   								   <input id="filtroFecha" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" /> </td>
		      				
		      				<td style="width:5%;"></td>
		      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Tabla: </label></td>
		      				<td class="celda-detalle" style="width:20%;">
		      						<select id="cmpFiltroTabla" name="cmpFiltroTabla" class="form-control espaciado input-sm" style="width:100%;">
		      							<option value="todos">Todos</option>
					                    <% ArrayList<?> listaNombreTablas = (ArrayList<?>) request.getAttribute("tablas");
					                    int numeroTablas = listaNombreTablas.size();
					                    String nombreTabla = null;
					                    for(int indice=0; indice < numeroTablas; indice++){ 
					                    	nombreTabla =(String) listaNombreTablas.get(indice);
					                    %>
					                    <option value='<%=nombreTabla.trim()%>'><%=nombreTabla.trim()%></option>
					                    <% } %>
				                    </select></td>
		      				
		      				<td style="width:5%;"></td>
		      				<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Usuario: </label> </td>
		      				<td class="celda-detalle"  style="width:20%;">
		      						<select id="cmpFiltroUsuario" name="cmpFiltroUsuario" class="form-control espaciado input-sm" style="width:100%;">
		      							<option value="todos">Todos</option>
					                    <% ArrayList<?> listaUsuarios = (ArrayList<?>) request.getAttribute("usuarios"); 
					                    int numeroUsuarios = listaUsuarios.size();
					                    Usuario eUsuario=null;
					                    String seleccionado="selected='selected'";
					                    seleccionado="";
					                    for(int indice=0; indice < numeroUsuarios; indice++){ 
					                      eUsuario =(Usuario) listaUsuarios.get(indice);
					                    %>
					                    <option <%=seleccionado%> value='<%=eUsuario.getNombre().trim()%>'><%=eUsuario.getNombre().trim()%></option>
					                    <% } %>
				                    </select>
				            </td>
	      				</tr>
	      			</thead>
	      			<tbody>      				
	      			</tbody>
	      		</table>

			<!-- BOTONES PARA EL MANTENIMIENTO -->
			<div>
				 <a id="btnFiltrar" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_FILTRAR")%></a>
				 <a id="btnVer" class="btn btn-default btn-sm disabled"><i class="fa fa-search"></i>  <%=mapaValores.get("ETIQUETA_BOTON_VER")%></a>
            </div>
          </div>
          <div class="box-body">
            <table id="tablaPrincipal" class="sgo-table table table-striped" style="width:100%;">
              <thead>
                <tr>
				<th>#</th>
				<th>ID</th>
				<th>Tabla</th>
				<th>Usuario</th>
				<th>Fecha</th>
				<th>Acci&oacute;n</th>         
                </tr>
              </thead>
            </table>
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
					  <td style="width:10%;">ID:						</td>
					  <td style="width:90%;"> <span id='vistaId'></span></td>
					</tr>
					<tr>
					  <td>Usuario:									</td>		
					  <td> <span id='vistaUsuario'></span> 			</td>
					</tr>
					<tr>
					  <td>Acci&oacute;n: 							</td>
					  <td> <span id='vistaAccion'></span>			</td>
					</tr>
					<tr>  
					  <td>Tabla:									</td>
					  <td> <span id='vistaTabla'></span>			</td>
					</tr>
					<tr>  
					  <td>Contenido:								</td>
					  <td> <span id='vistaContenido'></span>		</td>
					</tr>
					<tr>  
					  <td>Realizado el:								</td>
					  <td> <span id='vistaRealizadoEl'></span>		</td>
					</tr>
					<tr>  
					  <td>Realizado por:							</td>
					  <td> <span id='vistaRealizadoPor'></span>		</td>
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