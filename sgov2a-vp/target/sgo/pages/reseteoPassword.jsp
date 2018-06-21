<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/reseteoPassword" var="loginUrl"/>
<% String mensajeError= (String) request.getAttribute("mensajeError");%>
<% String username= (String) request.getAttribute("username");%>
<% String password= (String) request.getAttribute("password");%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Sistema de Gestión de Operaciones - Acceso al Sistema</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <!-- Bootstrap 3.3.2 -->
    <link href="tema/adminlte/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome Icons -->
    <link href="tema/font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="tema/adminlte/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <!-- iCheck -->
    <link href="tema/adminlte/plugins/iCheck/square/blue.css" rel="stylesheet" type="text/css" />
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <link href="tema/app/css/app.login.css" rel="stylesheet" type="text/css" />
  </head>
  <body class="login-page">
    <div class="login-box">
      <div class="login-logo">
         <a><b>Sistema de Gestión de Operaciones SGO</b></a>
      </div><!-- /.login-logo -->
      <div class="login-box-body">
	    <label class="login-box-msg" id="mensaje" > <%=mensajeError%> </label>
        <form name="principal"  method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <div class="form-group has-feedback">
            <input name="username" type="text" disabled value="<%=username%>" class="form-control" readonly />
            <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input name="password" type="password" disabled value="<%=password%>" class="form-control" readonly />
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>

          <div>
            <p class="login-box-msg">Ingrese nueva contraseña</p>      
	      </div>
          <div class="form-group has-feedback">
            <input name="nuevoPassword" type="password" class="form-control" placeholder="Nueva Contraseña" AUTOCOMPLETE="off"/>
          </div>
          <div class="form-group has-feedback">
            <input name="confirmaPaswword" type="password" class="form-control" placeholder="Confirme Contraseña" AUTOCOMPLETE="off"/>
          </div>

          <div class="row">
			<div class="col-md-12">
              <!-- <button class="btn btn-primary btn-block btn-flat" onclick="ingresar()" type="submit">Ingresar</button> -->
              <input class="btn btn-primary btn-block btn-flat" value="Ingresar" onclick="ingresar()" />
            </div>
         </div>
         <%--FIXME vp.20170512 --%>
         <div class="row">
	         <div class="col-md-12">
	         	<a href="javascript:politicas_password_mostrar ();"  >Políticas para password</a>
	         </div>
         </div>
        <div class="form-group has-feedback">
        <div class="col-md-12">
        </div>
        </div>
       </form>
      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->
    <%--FIXME vp.20170512 ............................................................ --%>
    <div class="login-box" id="politicas_password" style="display: none;z-index: 500;position: absolute;left:5%; top:10px;background-color: #e2e2d6;">
      <div class="login-logo">
         <a><b>Políticas de características del password</b></a>
      </div>
      <div class="login-box-body" style="width:360px;">
          <div>
                  
            <p >El password debe contener como mínimo 8 caracteres.</p>
            <p >El password debe ser alfanumérica.</p>
            <p >El password debe contener al menos una letra.</p>
            <p >El password debe contener al menos una letra en mayúscula.</p>
            <p >El password no debe comenzar o terminar con valores numéricos.</p>
            <p >El password no debe tener 3 caracteres iguales seguidos.</p>
            
	      </div>
          <div class="row">
			<div class="col-md-12">
              <input class="btn btn-primary btn-block btn-flat" value="Aceptar" onclick="politicas_password_cerrar()" />
            </div>
         </div>
        <div class="form-group has-feedback">
        <div class="col-md-12">
        </div>
        </div>
      </div>
    </div>

<%--................................................................................. --%>    
    <!-- jQuery 2.1.3 -->
    <script src="tema/adminlte/plugins/jQuery/jQuery-2.1.3.min.js"></script>
    <!-- Bootstrap 3.3.2 JS -->
    <script src="tema/adminlte/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- iCheck -->
    <script src="tema/adminlte/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
    <script>
//FIXME vp.20170512
function politicas_password_mostrar(){
	document.getElementById( 'politicas_password').style.display='block';
}
function politicas_password_cerrar(){
	document.getElementById( 'politicas_password').style.display='none';
}
//function recargaCaptcha(){
//	document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();  return false;
//}
	function ingresar() {
		document.getElementById('mensaje').innerHTML = "";
		var form = $('form[name=principal]');
		console.log("user " + form.find('input[name=username]').val());
		console.log("_csrf " + form.find('input[name=_csrf]').val());
		$.ajax({
			type: "POST",
			 url: "./reseteoPassword",
			 dataType: "json",
			 data : { 
				username: 		  form.find('input[name=username]').val(),
				password: 		  form.find('input[name=password]').val(),
				nuevoPassword: 	  form.find('input[name=nuevoPassword]').val(),
				confirmaPaswword: form.find('input[name=confirmaPaswword]').val(),
				_csrf  			: form.find('input[name=_csrf]').val(),
			},
			success : function(json) {
				var data = json;
				document.getElementById('mensaje').innerHTML = data;
				if(data.length == 0){
					//alert("mensaje vacio");
					location.reload();
				};
				//FIXME vp.20170512 location.reload();
				//if(data.length>0){
				//	recargaCaptcha();
				//}
			},
			error: function(xhr) {
				var data = xhr;
				document.getElementById('mensaje').innerHTML = data;
				if(data.length == 0){
					alert("mensaje vacio");
					location.reload();
				};
				location.reload();

	      },
		});
 	}

      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
  	});
    </script>
  </body>
</html>