<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="cabecera.jsp" %>
<jsp:include page="${vistaJSP}" flush="false"/>
<!-- Main Footer -->
      <footer class="main-footer">
        <!-- To the right -->
        <div class="pull-right hidden-xs">
          POWERED BY IBM
        </div>
        <!-- Default to the left --> 
        <strong> <a target="_blank" href="http://www.petroperu.com.pe/">PETRÓLEOS DEL PERÚ - PETROPERÚ S.A.</a>  | </strong> © Copyright 2015
      </footer>
    </div>
    <!-- ./wrapper -->
    <!-- REQUIRED JS SCRIPTS -->    
    <!-- jQuery 2.1.3 -->    
     <!--
     Tiene problemas con internet explorer
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.js"></script>
    -->
    <script src="tema/adminlte/plugins/jQuery/jquery-2.1.3.js"></script>
    <script src="tema/jquery-validate-1.14.0/jquery.validate.js"></script>
    <!-- Bootstrap 3.3.2 JS -->
    <script src="tema/adminlte/bootstrap/js/bootstrap.js" type="text/javascript"></script>    
	<script src="tema/adminlte/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
	<script src="tema/adminlte/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
	<script src="tema/adminlte/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	<script src="tema/adminlte/plugins/input-mask/jquery.inputmask.numeric.extensions.js" type="text/javascript"></script>
	<script src="tema/adminlte/plugins/input-mask/jquery.inputmask.regex.extensions.js" type="text/javascript"></script>
	<script src="tema/adminlte/plugins/daterangepicker.2.0.8/moment.js" type="text/javascript"></script>
    <script src="tema/adminlte/plugins/daterangepicker.2.0.8/daterangepicker.js" type="text/javascript"></script>
	<!-- DateRangePicker
	<script type="text/javascript" src="tema/adminlte/plugins/daterangepicker/moment.js"></script>
	<script type="text/javascript" src="tema/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
	 -->
    <!-- DataTables -->
    <script src="tema/datatable/js/jquery.dataTables.js" type="text/javascript"></script>
    <!-- DataTables TableTools -->
    <script src="tema/datatable/extensions/TableTools/js/dataTables.tableTools.js" type="text/javascript"></script>
    <!-- DataTables Integracion con Bootstrap -->
    <script src="tema/datatable/js/dataTables.bootstrap.js" type="text/javascript"></script>
    <!-- Select2 control combobox -->
     <script src="tema/select2/js/select2.js" type="text/javascript"></script>
     <!-- Multiselect control  -->
	<script src="tema/multiselect/js/jquery.multi-select.js" type="text/javascript"></script>
     <!--  QuickSearch-->
	<script src="tema/jquery-quicksearch/js/jquery.quicksearch.js" type="text/javascript"></script>     
    <!--  sheepIt -->  
	<script type="text/javascript" src="tema/sheepIt/jquery.sheepItPlugin.js"></script>      
    <!-- Tema script -->
    <script src="tema/adminlte/dist/js/app.min.js" type="text/javascript"></script>    
    <!-- Optionally, you can add Slimscroll and FastClick plugins. 
          Both of these plugins are recommended to enhance the 
          user experience -->	
	<script src="./recursos.js" type="text/javascript"></script> 
	<script src="aplicacion/constantes.js" type="text/javascript"></script> 
    <script src="aplicacion/utilitario.js" type="text/javascript"></script> 
    <script src="aplicacion/baseautorizacion.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_base.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_usuario.js" type="text/javascript"></script>   
    <script src="aplicacion/modulo_planificacion.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_transporte.js" type="text/javascript"></script>  
    <script src="aplicacion/modulo_descarga.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_cierre.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_gestor_reporte.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_cambio_password.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_programacion.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_despacho.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_turno.js" type="text/javascript"></script>
    <script src="aplicacion/modulo_jornada.js" type="text/javascript"></script>       
	<script>
    <jsp:include page="${vistaJS}" flush="false"/>
    </script>
  </body>
</html>