$(document).ready(function(){
  $.fn.select2.defaults.set( "theme", "bootstrap" );
  var moduloActual = new moduloGestorReporte();  
  moduloActual.urlBase='gestion-reporte';
  moduloActual.SEPARADOR_MILES = ",";
  //moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_LISTAR = './dia_operativo/listar';
  moduloActual.URL_EXPORTAR="./gestion-reporte/reporte-general";
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];  
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'});
  moduloActual.columnasGrilla.push({ "data": 'totalCisternas'});
  moduloActual.columnasGrilla.push({ "data": 'fechaActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  //Columnas
  moduloActual.definicionColumnas.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnas.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo, "data-align":"left" });

  moduloActual.inicializarCampos= function(){
	var referenciaModulo =this;
    this.obj.filtroOperacion = $("#filtroOperacion");
    this.obj.filtroOperacion.on('change', function(e){
      moduloActual.idOperacion=$(this).val();
      moduloActual.volumenPromedioCisterna=$(this).find("option:selected").attr('data-volumen-promedio-cisterna');
      moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
      moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');
      e.preventDefault(); 
    });   
    this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
    //Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaPlanificada.attr('data-fecha-actual');
    var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
    this.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
    //Controles de filtro
    this.obj.filtroOperacion.select2();
    this.obj.filtroFechaPlanificada.daterangepicker({
        singleDatePicker: false,        
        showDropdowns: false,
        locale: { 
          "format": 'DD/MM/YYYY',
          "applyLabel": "Aceptar",
          "cancelLabel": "Cancelar",
          "fromLabel": "Desde",
          "toLabel": "Hasta",
          "customRangeLabel": "Seleccionar",
          "daysOfWeek": [
          "Dom",
          "Lun",
          "Mar",
          "Mie",
          "Jue",
          "Vie",
          "Sab"
          ],
          "monthNames": [
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
          ]
        }
    });
    //Campos formulario
    this.obj.cmpCliente = $("#cmpCliente");
    this.obj.cmpOperacion = $("#cmpOperacion");
    this.obj.cmpFechaPlanificada = $("#cmpFechaPlanificada");
    this.obj.cmpIdOperacion = $("#cmpIdOperacion"); 
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaCliente=$("#vistaCliente");
    this.obj.vistaOperacion=$("#vistaOperacion");
    this.obj.vistaFechaPlanificacion=$("#vistaFechaPlanificacion");
    this.obj.vistaListaPlanificaciones=$("#vistaListaPlanificaciones");
    
    //Vista de auditoria
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");    
    //botones Adicionales
    this.obj.btnExportar=$("#btnExportar");
    this.obj.btnExportar.on("click", function(e){
    	e.preventDefault();
    	var fechaInicial = "2015-01-01";
    	var fechaFinal="2015-02-01";
    	var idOperacion = referenciaModulo.obj.filtroOperacion.val();
        var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
        fechaInicial = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
        fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
        console.log(fechaInicial);
        console.log(fechaFinal);
    	var url=moduloActual.URL_EXPORTAR+'?formato='+constantes.FORMATO_CSV;
    	url=url+"&fechaInicial="+fechaInicial + "&fechaFinal=" +fechaFinal + "&idOperacion="+idOperacion;
    	window.open(url);
    });	
  };  
  
  moduloActual.inicializar();
});