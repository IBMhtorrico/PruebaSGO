<%@page import="javax.xml.bind.DatatypeConverter"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Operacion"%>
<%@ page import="sgo.entidad.Producto"%>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper">
  <section class="content-header">
    <h1>Reportes</h1>
  </section>
  <section class="content" id="cntInterface">
  	<!--------------------------------------------------------------------- MENSAJE ----------------------------------------------------------------------->
  	<div class="row">
      <div class="col-xs-12">
      	<div id="bandaInformacion" class="callout callout-success">
          <%=mapaValores.get("MENSAJE_CARGANDO")%>
      	</div>
      </div>
    </div>
    <!--------------------------------------------------------------------- LISTADO ----------------------------------------------------------------------->
    <div class="row" id="cntTabla">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form" novalidate="novalidate">
              <table class="sgo-simple-table table table-condensed">
                <thead>
                  <tr>
                  	<td class="celda-detalle" style="width:5%;"><label class="etiqueta-titulo-horizontal">Reporte:</label></td>  
		            <td id="cntcmpReporte" class="celda-detalle" style="width:60%;"><select style="width:100%;" tipo-control="select2" id="cmpReporte" name="cmpReporte" class="form-control input-sm" >
                      <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
                	</select></td>
                </tr>
                </thead>
              </table>
            </form>
          </div>        
          <div class="box-header"></div>
          <div class="box-body"></div>
          <div class="overlay" id="ocultaContenedorTabla">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="cntReporte01">
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <form id="frmBuscar" class="form" novalidate="novalidate">
              <table class="sgo-simple-table table table-condensed">
                <thead>
                  <tr>
                    <td class="celda-detalle" style="width:12%;"><label class="etiqueta-titulo-horizontal">Operación / Cliente: </label></td>
        			<td class="celda-detalle" style="width:50%;"><select style="width:100%;" id="filtroOperacion" name="filtroOperacion" class="form-control espaciado input-sm" >
                 		<% 
                 		ArrayList<?> listaOperaciones = (ArrayList<?>) request.getAttribute("operaciones"); 
	                    String fechaActual = (String) request.getAttribute("fechaActual");   
	                    int numeroOperaciones = listaOperaciones.size();
	                    Operacion eOperacion=null;
	                    Cliente eCliente=null;
	                    String seleccionado="selected='selected'";
	                    seleccionado="";
	                    String operacion_razon="";
	                    for(int indiceOperaciones=0; indiceOperaciones < numeroOperaciones; indiceOperaciones++){ 
		                    eOperacion =(Operacion) listaOperaciones.get(indiceOperaciones);
		                    eCliente = eOperacion.getCliente();
		                    operacion_razon=eOperacion.getNombre().trim()+" / "+eOperacion.getCliente().getRazonSocial().trim();
		                    %>
		                    <option <%=seleccionado%> data-razon-social='<%=DatatypeConverter.printBase64Binary(operacion_razon.getBytes())%>' data-operacion-cliente='<%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %>' data-idOperacion='<%=eOperacion.getId()%>' data-fecha-actual='<%=fechaActual%>' data-nombre-operacion='<%=eOperacion.getNombre().trim()%>' data-nombre-cliente='<%=eCliente.getNombreCorto().trim()%>' value='<%=eOperacion.getId()%>'><%=eOperacion.getNombre().trim() + " / " + eCliente.getNombreCorto().trim() %></option>
		                    <%
	                    } %>
                    </select></td>
                    <td style="width:40%;"></td> 
        		</tr><tr>
        			<td class="celda-detalle" style="width:12%;"><label class="etiqueta-titulo-horizontal">Día Operativo: </label></td>
	                <td class="celda-detalle" style="width:80%;"><input id="filtroFechaPlanificada" type="text" style="width:100%" class="form-control espaciado input-sm" data-fecha-actual="<%=fechaActual%>" /></td>
            	</tr><tr>
            		<td class="celda-detalle" style="width:12%;"><label class="etiqueta-titulo-horizontal">Formato: </label></td>
                  	<td style="width:20%;">
                  		<input type="radio" name="filtroFormato" value="pdf" checked="checked" id="filtroFormatoPDF">PDF&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <input type="radio" name="filtroFormato" value="excel" id="filtroFormatoExcel">Excel -->
					</td>				 
                </tr><tr>  
                  	<td><a id="btnExportar" class="btn btn-default btn-sm col-md-12" style="width:60%;"><i class="fa fa-refresh"></i>  <%=mapaValores.get("ETIQUETA_BOTON_EXPORTAR")%></a></td>
                  	<td style="width:90%;"></td> 
                </tr>
                </thead>
              </table>
            </form>
          </div>   
          <div class="overlay" id="ocultaContenedorTabla01">
            <i class="fa fa-refresh fa-spin"></i>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>