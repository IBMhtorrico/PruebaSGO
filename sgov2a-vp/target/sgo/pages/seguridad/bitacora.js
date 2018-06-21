$(document).ready(function(){
  var moduloActual = new moduloBase();
  moduloActual.urlBase='bitacora';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  
  moduloActual.ordenGrilla=[[ 2, 'desc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'});   //Target1
  moduloActual.columnasGrilla.push({ "data": 'tabla'}); //Target2
  moduloActual.columnasGrilla.push({ "data": 'usuario'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'fechaRealizacion'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'accion'});//Target5
    
  moduloActual.definicionColumnas.push({"targets": 1, "searchable": true, "orderable": true, "visible": false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": true, "orderable": true, "visible": true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": true, "orderable": true, "visible": true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": true, "orderable": true, "visible": true, "class": "text-center" });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": true, "orderable": true, "visible": true });

  moduloActual.inicializarCampos= function(){
	this.obj.filtroFecha = $("#filtroFecha");
	var fechaActual = this.obj.filtroFecha.attr('data-fecha-actual');
    var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
    this.obj.filtroFecha.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
    //Controles de filtro
    this.obj.filtroFecha.daterangepicker({
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
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaUsuario=$("#vistaUsuario");
    this.obj.vistaAccion=$("#vistaAccion");
    this.obj.vistaTabla=$("#vistaTabla");
    this.obj.vistaContenido=$("#vistaContenido");
    this.obj.vistaRealizadoEl=$("#vistaRealizadoEl");
    this.obj.vistaRealizadoPor=$("#vistaRealizadoPor");
    this.obj.vistaIdentificador=$("#vistaIdentificador");
    this.obj.vistaIpUsuario=$("#vistaIpUsuario");
  };
  
  moduloActual.llamadaAjax=function(d){
		var referenciaModulo =this;
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    var rangoFecha = referenciaModulo.obj.filtroFecha.val().split("-");
	    var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
	    var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
	    d.filtroFechaInicio= fechaInicio;	
	    d.filtroFechaFinal = fechaFinal;
	    d.filtroUsuario= referenciaModulo.obj.cmpFiltroUsuario.val();
	    d.filtroTabla= referenciaModulo.obj.cmpFiltroTabla.val();
	};
  
  moduloActual.llenarDetalles = function(registro){
	this.obj.vistaId.text(registro.id);
    this.obj.vistaUsuario.text(registro.usuario);
    this.obj.vistaAccion.text(registro.accion);
    this.obj.vistaTabla.text(registro.tabla);
    console.log(this.formatearStringToStringConSaltoLinea(registro.contenido));
    this.obj.vistaContenido.text(this.formatearStringToStringConSaltoLinea(registro.contenido));
    this.obj.vistaRealizadoEl.text(registro.fechaRealizacion);
    this.obj.vistaRealizadoPor.text(registro.realizadoPor);
    this.obj.vistaIdentificador.text(registro.identificador);
    this.obj.vistaIpUsuario.text(registro.ipUsuario);
  };
  
  moduloActual.formatearStringToStringConSaltoLinea = function(str){
	return str.replace(/,/gi, ', ');
  },
  
  moduloActual.inicializar();
});
