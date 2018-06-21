<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Transportista"%>
<%@ page import="sgo.entidad.Planta"%>
<%@ page import="java.util.HashMap"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Operaciones / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
						<form id="frmBuscar" class="form-inline">
			              <div class="form-group">
			                <label for="txtFiltro" class="espaciado">Nombre: </label>
			                <input id="txtFiltro" type="text" class="form-control input-sm espaciado text-uppercase"  placeholder="Buscar..." maxlength="150">
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
                 <th>Nombre</th>
                 <th>Cliente</th>
                 <th>Ref. planta recepci&oacute;n</th>
                 <th>Ref. destino mercader&iacute;a</th>
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
                <label>Nombre (Max. 150 caracteres)</label>
                <input name="cmpNombre" id="cmpNombre" type="text" class="form-control input-sm text-uppercase" maxlength="150" required placeholder="Ingresar Nombre"/>
                </div>
               <div class="form-group">
                <label>Alias (Max. 150 caracteres)</label>
                <input name="cmpAlias" id="cmpAlias" type="text" class="form-control input-sm text-uppercase" maxlength="150" required placeholder="Ingresar Alias"/>
                </div>
              <div class="form-group">
                <label>Cliente</label>                
                    <%
                    String seleccionado="selected='selected'";
                    ArrayList<?> listadoClientes = (ArrayList<?>) request.getAttribute("listadoClientes");               
                    int numeroClientes = listadoClientes.size();
                    Cliente eCliente=null;                    
                    %>
                    <select id="cmpIdCliente" name="cmpIdCliente" class="form-control input-sm" style="width: 100%">
                    <option></option>
                    <%
                    for(int contador=0; contador < numeroClientes; contador++){ 
                     eCliente =(Cliente) listadoClientes.get(contador);
                    %>
                    <option <%=seleccionado%> value='<%=eCliente.getId()%>'><%=eCliente.getRazonSocial().trim()  %></option>
                    <%
                    seleccionado="";
                    }%>
                </select>
              </div>
              <div class="form-group">
                <label>Referencia Planta Recepci&oacute;n</label>
                 <input name="cmpIdRefPlantaRecepcion" id="cmpIdRefPlantaRecepcion" type="text" class="form-control input-sm" maxlength="20" placeholder="Ingresar Referencia Planta Recepci&oacute;n"/>
              </div>
              <div class="form-group">
                <label>Referencia Destinatario Mercader&iacute;a</label>
                <input name="cmpIdRefDestMercaderia" id="cmpIdRefDestMercaderia" type="text" class="form-control input-sm" maxlength="20" placeholder="Ingresar Referencia Destinatario Mercader&iacute;a"/>
              </div>
              <div class="form-group">
                <label>Vol&uacute;men Promedio Cisterna</label> 
                <input name="cmpVolumenPromedioCisterna" id="cmpVolumenPromedioCisterna" type="text" class="form-control input-sm"  placeholder="Ingresar Vol&uacute;men Promedio Cisterna" maxlength="8" required/>
              </div>
              <div class="form-group">
                <label>Fecha de inicio de planificaci&oacute;n</label>
                <input name="cmpFechaInicioPlanificacion" id="cmpFechaInicioPlanificacion" type="text" class="form-control input-sm" placeholder="Ingresar fecha de inicio de planificación" required data-inputmask="'alias': 'dd/mm/yyyy'" />
              </div>
              <div class="form-group">
                <label>Planta de Despacho</label>
                <select id="cmpPlantaDespacho" name="cmpPlantaDespacho" class="form-control input-sm" style="width: 100%">
					<option  selected="selected" value="0">SELECCIONAR...</option>
					<%  ArrayList<Planta> listadoPlantas = (ArrayList<Planta>) request.getAttribute("listadoPlantas");
						Planta eplanta = null;
						for (int i = 0; i < listadoPlantas.size(); i++) {
							eplanta = listadoPlantas.get(i);
					%>
					<option selected='selected' value='<%=eplanta.getId()%>'><%=eplanta.getDescripcion()%></option>
					<%	} %>
				  </select>
              </div>
              <div class="form-group">
                <label>ETA</label>
                <input name="cmpETA" id="cmpETA" type="text" class="form-control input-sm" placeholder="Ingresar el tiempo estimado de llegada" required maxlength="2"/>
              </div>
              
               <div class="form-group">
                <label>Para:</label>
                <FONT COLOR=red><B><label>(Max. 250 caracteres). Los correos deben ser separadaos por ";".</label></B></FONT>
                <input name="cmpCorreoPara" id="cmpCorreoPara" type="text" class="form-control input-sm" maxlength="250" placeholder="Para..."/>
              </div>
              
               <div class="form-group">
                <label>CC:</label>
                <FONT COLOR=red><B><label>(Max. 250 caracteres). Los correos deben ser separdaos por ";".</label></B></FONT>
                <input name="cmpCorreoCC" id="cmpCorreoCC" type="text" class="form-control input-sm" maxlength="250" placeholder="CC..."/>
              </div>
              
              <table class="sgo-simple-table table table-condensed" style="width:100%;">
      			<thead>
      				<tr>
      					<td class="celda-cabecera-detalle"><label class="etiqueta-titulo-horizontal">Transportistas</label></td>
      				</tr>
      			</thead>
      			<tbody id="GrupoTransportista">
      				<tr id="GrupoTransportista_template">
      				<td class="celda-detalle">
					  <select tipo-control="select2" elemento-grupo="idTransportista" id="GrupoTransportista_#index#_Transportista" style="width: 100%" name="transportista[detalle][#index#][transportista]" class="form-control input-sm text-uppercase">
						<option value="0">SELECCIONAR...</option>
						<%ArrayList<Transportista> listaTransportistas = (ArrayList<Transportista>) request.getAttribute("listadoTransportistas");
							Transportista eTransportista = null;
							for (int i = 0; i < listaTransportistas.size(); i++) {
								eTransportista = listaTransportistas.get(i);
						%>
						<option value='<%=eTransportista.getId()%>'><%=eTransportista.getRazonSocial()%></option>
						<%	} %>
					  </select>
				 	</td>
				 	<td class="celda-detalle">
    					<a elemento-grupo="botonElimina" id="GrupoTransportista_#index#_elimina" class="btn btn-default btn-sm" style="width:100%;display:inline-block"><i class="fa fa-remove"></i></a>
    				</td>
      				</tr>
      				<tr id="GrupoTransportista_noforms_template">
      				<td></td>
      				</tr>    			
      			</tbody>
      		</table>
      		
            </form>
          </div>
          <div class="box-footer">
            <button id="btnGuardar" type="submit" class="btn btn-sm btn-primary"><i class="fa fa-save"></i> Guardar</button>
             <a id="btnAgregarTransportista" class="btn bg-purple btn-sm"><i class="fa fa-plus-circle"></i>  Agregar Transportista</a>
            <button id="btnCancelarGuardar"  class="btn btn-sm btn-danger"><i class="fa fa-close"></i>  Cancelar</button>
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
            <table id="grilla_x" class="sgo-table table table-striped" style="width:100%;">
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