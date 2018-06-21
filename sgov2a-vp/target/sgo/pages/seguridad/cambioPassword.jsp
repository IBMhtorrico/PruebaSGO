<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Cliente"%>
<%@ page import="sgo.entidad.Usuario"%>
<%@ page import="sgo.entidad.Operacion"%>
<%
HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores"); 
%>
<!-- Contenedor de pagina-->
<div class="content-wrapper">
  <!-- Cabecera de pagina -->
  <section class="content-header">
    <h1>Actualización de Perfil </h1>
	</section>
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<div id="bandaInformacion" class="callout callout-success">El registro fue recuperado con exito</div>
			</div>
		</div>
	    <!-- Aqui empieza el formulario -->        
	    <div class="row" id="cntFormulario">
	      <div class="col-md-12">
	        <div class="box box-default">
	          <div class="box-body">
	            <form id="frmPrincipal" role="form">
					<div class="form-group">
						<label>Nombre</label> 
						<% ArrayList<Usuario> listaUsuario = (ArrayList<Usuario>) request.getAttribute("usuario");
						   Usuario eUsuario = listaUsuario.get(0);
						   int idUsuario = eUsuario.getId();
						   String nombre = eUsuario.getNombre();
						   String identidad = eUsuario.getIdentidad();
						   int tipo =  eUsuario.getTipo(); 
						   String email = eUsuario.getEmail();
						   String clave = eUsuario.getClave();
						   String zonaHoraria = eUsuario.getZonaHoraria();
						%> 
	            		<input data-id='<%=idUsuario%>' data-nombre='<%=nombre%>' data-tipo='<%=tipo%>' data-clave='<%=clave%>' data-zonaHoraria='<%=zonaHoraria%>' name="cmpNombre" id="cmpNombre" type="text" class="form-control input-sm" disabled required placeholder="" />
					</div>
					<div class="form-group">
						<label>Identidad</label> 
	            		<input data-identidad='<%=identidad%>' name="cmpIdentidad" id="cmpIdentidad" type="text" class="form-control text-uppercase input-sm" disabled required placeholder="" />
					</div>
					<div class="form-group">
						<label>Email</label>
						<div class="input-group">
							<span class="input-group-addon">
								<i class="fa fa-envelope"></i>
							</span>
							<input data-email='<%=email%>' name="cmpEmail" id="cmpEmail" class="form-control input-sm"  type="email" disabled placeholder="Email">
						</div>
					</div>
					<div class="form-group">
						<label>Contraseña Actual</label> 
	            		<input name="cmpClave" id="cmpClave" type="password" class="form-control input-sm" required placeholder="" />
					</div>
					<div class="form-group">
						<label>Nueva Contraseña </label>
						<input type="text" class="form-control alert-danger text-left espaciado input-sm text-uppercase" value="(Debe tener como m&iacute;nimo 8 caracteres alfanum&eacute;ricos, 1 letra en may&uacute;scula. No deber tener n&uacute;meros al inicio ni al final. Los caracteres no deben repetirse 3 veces seguidas.)" readonly /> 
	            		<input name="cmpNuevaClave" id="cmpNuevaClave" type="password" class="form-control input-sm" required placeholder="" maxlength="14" onkeypress="return validarClave(event);"/>
					</div>
					<div class="form-group">
						<label>Confirmar Contraseña</label> 
	            		<input name="cmpConfirmaClave" id="cmpConfirmaClave" type="password" class="form-control input-sm" required placeholder="" maxlength="14" onkeypress="return validarClave(event);"/>
					</div>
					
					<div class="form-group">
						<label>Zona Horaria</label> 
			            <select name="cmpZonaHoraria" id="cmpZonaHoraria" class="form-control input-sm" >
			              <option value=""><%=mapaValores.get("TITULO_SELECCIONAR_ELEMENTO")%></option>
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
			              <option value="+14">UTC +14</option>
			            </select>
					</div>
				</form>
	          </div>
	          <div class="box-footer">
	            <button id="btnGuardar" type="submit" class="btn btn-primary btn-sm">Guardar</button>
	            <a href="./panel" class="btn btn-danger btn-sm">Salir</a>
	            <!-- <button id="btnCancelarGuardar"  class="btn btn-danger btn-sm">Cancelar</button> -->
	          </div>
	          <div class="overlay" id="ocultaContenedorFormulario">
	            	<i class="fa fa-refresh fa-spin"></i>
	          	</div>
	        </div>
	      </div>
	    </div>
  </section>
</div>
<script type="text/javascript">
	function validarClave(event){
		var regex = new Regex("^[0-9a-zA-Z \b]+$");
		var key = String.fromCharCode(!event.charCode ? event.which: event.charCode);
		if(!regex.test(key)){
			event.preventDefault();
			return false;			
		}
	}

</script>