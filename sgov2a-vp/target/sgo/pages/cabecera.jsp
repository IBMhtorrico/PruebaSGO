<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.List"  %>
<%@ page import="sgo.entidad.Enlace"  %>
<% ArrayList<Enlace> menu = (ArrayList<Enlace>) request.getAttribute("menu"); %>
<% String identidadUsuario = (String) request.getAttribute("identidadUsuario"); %>
<% String tituloSeccionDetalle = (String) request.getAttribute("tituloSeccionDetalle"); %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>SGO</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <!-- Bootstrap 3.3.2 -->
    <link href="tema/adminlte/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <!-- Font Awesome Icons -->
    <link type="text/css" rel="stylesheet" href="tema/font-awesome-4.3.0/css/font-awesome.css"/>
    <!-- Ionicons -->
    <link href="tema/ionicons/css/ionicons.min.css" rel="stylesheet" type="text/css"/>
    <!-- 
    <link type="text/css" rel="stylesheet" href="tema/adminlte/plugins/daterangepicker/daterangepicker-bs3.css"/>    
     -->
     <link type="text/css" rel="stylesheet" href="tema/adminlte/plugins/daterangepicker.2.0.8/daterangepicker.css"/>	
     <link href="tema/datatable/css/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>     
    <!-- Theme style -->
    <link href="tema/adminlte/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css"/>
    <link href="tema/adminlte/dist/css/skins/skin-blue.min.css" rel="stylesheet" type="text/css"/>
    <link href="tema/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
    <link href="tema/app/css/select2-bootstrap.css" rel="stylesheet" type="text/css" />    
     <link href="tema/multiselect/css/multi-select.css" media="screen" rel="stylesheet" type="text/css">
    <link href="tema/app/css/app.css" rel="stylesheet" type="text/css" />    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="tema/html5shiv.3.7.0/html5shiv.js"></script>
        <script src="tema/respond.1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="skin-blue">
	  <form id="frmCerrarSesion" action="./processlogout" method="post">
	  <input id="csrf-token" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	  </form>
    <div class="wrapper">
      <header class="main-header">
        <!-- Logo -->
        <a class="logo">
        	<h3 class="titulo-petro"><b>PETROPERÚ S.A.</b></h3>
			</a>
        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
              <!-- Messages: style can be found in dropdown.less-->
              <!-- Notifications Menu -->
              <!-- Tasks Menu -->
              <!-- User Account Menu -->
              <li class="dropdown user user-menu">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <!-- The user image in the navbar-->
                  <!-- <img src="./tema/adminlte/dist/img/user2-160x160.jpg" class="user-image" alt="User Image"/> -->
                  <!-- hidden-xs hides the username on small devices so only the image appears. -->
                  <span class="hidden-xs"><%=identidadUsuario%></span>
                </a>
                <ul class="dropdown-menu">
                  <!-- The user image in the menu -->
                  <!-- <li class="user-header">
                    <img src="./tema/adminlte/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />
                    <p>
                      Alexander Pierce - Web Developer
                      <small>Member since Nov. 2012</small>
                    </p>
                  </li> -->
                  <!-- Menu Footer-->
                  <li class="user-footer">
                    <!-- <div class="pull-left">
                      <a href="#" class="btn btn-default btn-flat">Perfil</a>
                    </div> -->
                    <div class="pull-right">
                      <a href="./cambioPassword" class="btn btn-default btn-flat">Ver Perfil</a>
                      <a id="btnCerrarSesion" href="#" class="btn btn-default btn-flat">Salir</a>
                    </div>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
      </header>
      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
          <!-- Sidebar user panel (optional) -->
          <!-- Sidebar Menu -->
          <ul class="sidebar-menu">
            <!-- <li class="header">Menu</li> -->
            <!-- Optionally, you can add icons to the links -->
			<%
			int numeroElementos = menu.size();
			int numeroOpciones =0;
			Enlace eEnlace = null;
			Enlace eOpcion = null;
			String claseGrupo="treeview";
			String claseOpcion="";
			for(int indice=0;indice<numeroElementos;indice++) {
			eEnlace = menu.get(indice);	
			List<Enlace> opciones = eEnlace.getEnlaces();
			numeroOpciones = opciones.size();
			claseGrupo="treeview";
			if (eEnlace.getEnlaceActual()){
				claseGrupo="active treeview";
			}
			
			%>
            <li class="<%=claseGrupo%>">
              <a href="<%=eEnlace.getUrlCompleta()%>"><span><%=eEnlace.getTitulo()%></span> <i class="fa fa-angle-left pull-right"></i></a>
              <ul class="treeview-menu">
              	<% for(int subindice=0;subindice<numeroOpciones;subindice++) {
              	eOpcion = opciones.get(subindice);
              	claseOpcion="";
    			if (eOpcion.getEnlaceActual()){
    				claseOpcion="active";
    			}
              	%>
                <li class="<%=claseOpcion%>"><a href=".<%=eOpcion.getUrlRelativa()%>"><%=eOpcion.getTitulo() %></a></li>
				<%} %>
              </ul>
            </li>
			<%
			}
			%>
          </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>