<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sgo.entidad.Estacion"%>
<%@ page import="java.util.HashMap"%>
<%HashMap<?,?> mapaValores = (HashMap<?,?>) request.getAttribute("mapaValores");%>
<div class="content-wrapper"
style="background: url('./tema/app/imagen/fondo.login.jpg') no-repeat center center">
  <section class="content-header text-center">
    <h1 class="titulo-bienvenida"><%=mapaValores.get("MENSAJE_BIENVENIDA")%></h1>
  </section>
	<section class="content" id="cntInterface" >
		<div class="row">
			<div class="col-xs-12">

			</div>
		</div>
  </section>
</div>