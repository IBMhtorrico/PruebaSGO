<%@ page  language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/admin/processlogin" var="loginUrl"/>
<% String mensajeError= (String) request.getAttribute("mensajeError");%>
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
        <!-- <a href="../../index2.html"><b>Sistema de Gestión de Operaciones SGO</b></a> -->
         <a><b>Sistema de Gestión de Operaciones SGO</b></a>
      </div><!-- /.login-logo -->
      <div class="login-box-body">
      	<label class="login-box-msg" id="mensaje" > <%=mensajeError%> </label>
        <%-- <p name="mensaje" id="mensaje" class="login-box-msg"><%=mensajeError%></p> --%>
        <form name="principal"  action="${loginUrl}" method="post"><%--FIXME vp.20170515 --%>
           <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <div class="form-group has-feedback">

             	<input name="username" type="text" class="form-control" placeholder="Usuario" style="width:105%" /> 
            	 <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">

            	 <input name="password" type="password" class="form-control" placeholder="Contraseña" AUTOCOMPLETE="off" style="width:105%"/> 
				<span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          
<!--           <div class="row"> 	     -->
		  <div> 
              <div class="col-md-6" style="background-color:#E4E4E4;">
                  <img id="captcha_id" name="imgCaptcha" src="captcha.jpg" style="display: block;position: relative;left:-30%;">                  
              </div>
			  <div class="col-md-6">
			 	  <input class="form-control" type="text" name="captcha" id="captcha" style="width: 80px;vertical-align: middle;height: 35px;" maxlength="5" placeholder="Captcha" AUTOCOMPLETE="off"/>
			  </div>
          </div> 
           &nbsp; &nbsp;	      
          <div class="row">
          	<div align="left" class="login-box-msg">
	          	<a href="javascript:;" title="Refrescar" onclick="document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();document.getElementById('captcha').value='';return false">
	            	Pruebe otro
	          	</a>
	          </div>
          </div> 
          
          <div class="row">
			<div class="col-md-12">
              <button class="btn btn-primary btn-block btn-flat" type="submit" value="login" >Ingresar</button>
            </div>
         </div>
         <br />
	     <!-- <div class="row">
			<div class="col-md-12">
	     	 <input class="btn btn-block btn-warning btn-xs"  value="Olvidé mi contraseña" onclick="reseteaPassword(event);" /> 
	     	</div>
         </div> -->
       </form>
       
       <form id="idFormPG"  action="/petrosecure/login.htm" method="post">
          <div class="row">
          	<div align="left" class="login-box-msg"> 
				   <input type="hidden" value="1" id="idSistema" name="idSistema"/>
				   <strong>	          	
	          			<a id="enviarPS" href="#" onclick="enviarDatosPS()">Olvidé mi contraseña.</a>	          	
	          		</strong>				       
	          </div>
          </div>  
         </form>	
       
      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->
    <!-- jQuery 2.1.3 -->
    <script src="tema/adminlte/plugins/jQuery/jQuery-2.1.3.min.js"></script>
    <!-- Bootstrap 3.3.2 JS -->
    <script src="tema/adminlte/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- iCheck -->
    <script src="tema/adminlte/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
    <script type="text/javascript">	 	
    	enviarDatosPS = function(){
	 		var form = document.getElementById("idFormPG");
	 		form.submit();	 		
	 		return null;
	 	};
  </script>
     
    
    
    <!-- <script>
    //$("#ocultaLogin").show();
/*     document.getElementById('ocultaLogin').modal = "show";
    console.log($("#ocultaLogin"));
    $("#ocultaLogin").show(); */
    
    //document.getElementById('ocultaLogin').modal("show");
		function reseteaPassword(e) {
			e.preventDefault();
		  	document.getElementById('mensaje').innerHTML = "";
			var form = $('form[name=principal]');
			$.ajax({
				type: "POST",
				 url: "./passwordNuevo",
				 dataType: "json",
				 data : { 
					username: form.find('input[name=username]').val(),
					_csrf  : form.find('input[name=_csrf]').val(),
				},
				success : function(json) {
					var data = json;
					document.getElementById('mensaje').innerHTML = data;
				},
				error: function(xhr) {
					var data = xhr;
					document.getElementById('mensaje').innerHTML = data;
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
      
      function refrescarCaptcha(id) { 
    		var img_cap = document.getElementById("img_captcha"+id);
    		img_cap.src = img_cap.src + "?" + new Date().getTime();
    		var rspt_cap = document.getElementById("i_captcha"+id);
    		rspt_cap.value = "";
    	}

    	function refrescarCaptchaGoogle() { 
    		console.log("captcha reseteado");
    		grecaptcha.reset();
    	}
    </script> -->
  </body>
</html>