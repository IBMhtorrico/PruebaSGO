<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Aforo de Tanques / <small id="tituloSeccion"><%=mapaValores.get("TITULO_LISTADO_REGISTROS")%></small></h1>
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
<table>
  <thead>
  <tr>
  <td style="width:9%;">
  <label id="lblFiltroOperacion" class="etiqueta-titulo-horizontal">Cliente / Operacion: </label>
  </td>
  <td style="width:25%;">
    <select id="filtroOperacion" name="filtroOperacion" class="form-control espaciado input-sm" style="width:100%;">
   	<option value="-1" selected="selected">Seleccionar...</option>
    <%
    ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
    int numeroOperaciones = listaOperaciones.size();
    Operacion eOperacion=null;
    Cliente eCliente=null;
    String seleccionado="selected='selected'";
    seleccionado="";
    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
    eCliente = eOperacion.getCliente();
    %>
    <option <%=seleccionado%> data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
    <%}%>
    </select>
  </td> 
  <td style="width:5%;padding-left:10px;">
  <label id="lblFiltroEstacion" class="etiqueta-titulo-horizontal">Estación : </label>
  </td>
  <td style="width:15%;">
    <select id="filtroEstacion" name="filtroEstacion" class="form-control espaciado input-sm" style="width:100%;">
      <option value="-1">Seleccionar...</option>
    </select>
  </td>
  <td style="width:5%;padding-left:10px;">
  <label id="lblFiltroTanque" class="etiqueta-titulo-horizontal">Tanque : </label>
  </td>
  <td style="width:15%;">
    <select id="filtroTanque" name="filtroTanque" class="form-control espaciado input-sm" style="width:100%;">
      <option value="-1">Seleccionar...</option>
    </select>
  </td>
  <td style="width:5%;padding-left:10px;">
  <label id="lblFiltroAltura" class="etiqueta-titulo-horizontal">Altura : </label>
  </td>
  <td style="width:3%;"> 
  <input id="cmpFiltroAltura" name="cmpFiltroAltura" type="text" class="form-control input-sm" value="" maxlength="4" />
  </td>
  <td style="width:4%;padding-left:10px;">
  <a id="btnFiltrarAforo" class="btn btn-sm btn-default espaciado col-md-12"><i class="fa fa-refresh"></i>  Filtrar</a>
  </td>
  </tr>
  </thead>
</table>              
          </div>
          <div class="box-header">
            <div>
              <a id="btnAgregar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-plus"></i>  <%=mapaValores.get("ETIQUETA_BOTON_AGREGAR")%></a>
              <a id="btnImportar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-plus"></i>  Importar</a>
              <a id="btnModificar" class="btn btn-default btn-sm disabled espaciado"><i class="fa fa-edit"></i>  <%=mapaValores.get("ETIQUETA_BOTON_MODIFICAR")%></a>
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
					<th>F. Actualizacion</th>
					<th>Usuario</th>
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
        <!--   <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
 -->        </div>
      </div>
    </div>
    <div class="row" id="cntFormulario" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
           <div class="box-body">
            <form id="frmPrincipal">
              <div class="form-group" id="cntcmpRazonSocial">
                <label>Cliente / Operacion</label>
                <input name="cmpClienteOperacion" id="cmpClienteOperacion" type="text" class="form-control text-uppercase input-sm" disabled />
              </div>
				<div class="form-group" id="cntcmpRazonSocial">
                <label>Estacion</label>
                <input name="cmpEstacion" id="cmpEstacion" type="text" class="form-control text-uppercase input-sm" disabled />
              </div>
				<div class="form-group" id="cntcmpRazonSocial">
                <label>Tanque</label>
                <input name="cmpTanque" id="cmpTanque" type="text" class="form-control text-uppercase input-sm" disabled />
              </div>
              <div class="form-group" id="cntcmpRazonSocial">
                <label>Altura (Centimetros)</label>
                <input name="cmpAltura" id="cmpAltura" type="text" class="form-control text-uppercase input-sm" maxlength="4" required />
              </div>
              <div class="form-group" id="cntcmpNombreCorto">
                <label>Volumen:</label>
                <input name="cmpVolumen" id="cmpVolumen" type="text" class="form-control text-uppercase input-sm" maxlength="6" required />
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
                <label>Cliente / Operacion</label>
                <input name="cmpClienteOperacionImportar" id="cmpClienteOperacionImportar" type="text" class="form-control text-uppercase input-sm" disabled maxlength="150" />
              </div>
				<div class="form-group" id="cntcmpRazonSocial">
                <label>Estacion</label>
                <input name="cmpEstacionImportar" id="cmpEstacionImportar" type="text" class="form-control text-uppercase input-sm" disabled maxlength="150" />
              </div>
				<div class="form-group" id="cntcmpRazonSocial">
                <label>Tanque</label>
                <input name="cmpTanqueImportar" id="cmpTanqueImportar" type="text" class="form-control text-uppercase input-sm" disabled maxlength="150" />
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
    <div class="row" id="cntVistaRegistro" style="display:none;">
      <div class="col-md-12">
        <div class="box box-default">
          <div class="box-body">
            <table class="sgo-table table table-striped" style="width:100%;">
              <tbody>
                <tr>
                  <td>ID:</td>
                  <td>
                    <span id='vistaId'></span>
                  </td>
                </tr>                
                <tr>
                  <td>Cliente / Operacion:</td>
                  <td>
                    <span id="vistaClienteOperacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Estación:</td>
                  <td>
                    <span id="vistaEstacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Tanque:</td>
                  <td>
                    <span id="vistaTanque"></span>
                  </td>
                </tr>
                <tr>
                  <td>Altura:</td>
                  <td>
                    <span id="vistaAltura"></span>
                  </td>
                </tr>
                <tr>
                  <td>Volumen:</td>
                  <td>
                    <span id="vistaVolumen"></span>
                  </td>
                </tr>
                <tr>
                  <td>Creado el:</td>
                  <td>
                    <span id="vistaCreadoEl"></span>
                  </td>
                </tr>
                <tr>
                  <td>Creado por:</td>
                  <td>
                    <span id="vistaCreadoPor"></span>
                  </td>
                </tr>
                <tr>
                  <td>IP Creaci&oacute;n:</td>
                  <td>
                    <span id="vistaIPCreacion"></span>
                  </td>
                </tr>
                <tr>
                  <td>Actualizado el:</td>
                  <td>
                    <span id="vistaActualizadoEl"></span>
                  </td>
                </tr>
                <tr>
                  <td>Actualizado por:</td>
                  <td>
                    <span id="vistaActualizadoPor"></span>
                  </td>
                </tr>
                <tr>
                  <td>IP Actualizaci&oacute;n:</td>
                  <td>
                    <span id="vistaIPActualizacion"></span>
                  </td>
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