$(document).ready(function(){
  $.fn.select2.defaults.set( "theme", "bootstrap" );
  var moduloActual = new moduloPlanificacion();  
  moduloActual.urlBase='planificacion';
  moduloActual.SEPARADOR_MILES = ",";
  //moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_LISTAR = './dia_operativo/listar';
  moduloActual.URL_VALIDA_FECHA_PLANIFICADA='./dia_operativo/valida-planificaciones-x-dia-operativo';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ANULAR = moduloActual.urlBase + '/anular';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_RECUPERAR_ULTIMO_DIA='../admin/dia_operativo/recuperar-ultimo-dia';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];  
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'});
  moduloActual.columnasGrilla.push({ "data": 'fechaEstimadaCarga'});
  moduloActual.columnasGrilla.push({ "data": 'totalCisternas'});
  moduloActual.columnasGrilla.push({ "data": 'fechaActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.correoPara'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.correoCC'});
  
  //Columnas
  moduloActual.definicionColumnas.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnas.push({ "targets" : 7, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo, "data-align":"left" });
  moduloActual.definicionColumnas.push({ "targets" : 8, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 9, "searchable" : true, "orderable" : false, "visible" : false });
  
  //grilla del formulario
  moduloActual.ordenGrillaFormulario=[[ 2, 'asc' ]];  
  moduloActual.columnasGrillaFormulario.push({ "data": 'jornada.estacion.nombre'}); 
  moduloActual.columnasGrillaFormulario.push({ "data": 'tanque.descripcion'}); 
  moduloActual.columnasGrillaFormulario.push({ "data": 'producto.nombre'});
  moduloActual.columnasGrillaFormulario.push({ "data": 'tanque.volumenTotal'});
  moduloActual.columnasGrillaFormulario.push({ "data": 'tanque.volumenTrabajo'});
  moduloActual.columnasGrillaFormulario.push({ "data": 'libre'});
  moduloActual.columnasGrillaFormulario.push({ "data": 'ocupado'});
 
  //Columnas de grilla del formulario
  moduloActual.definicionColumnasFormulario.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-left" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-left" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-left" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasFormulario.push({ "targets" : 7, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  
  //grilla del detalle
  moduloActual.ordenGrillaDetalle=[[ 2, 'asc' ]];  
  moduloActual.columnasGrillaDetalle.push({ "data": 'tanque.descripcion'}); 
  moduloActual.columnasGrillaDetalle.push({ "data": 'producto.nombre'});
  moduloActual.columnasGrillaDetalle.push({ "data": 'tanque.volumenTotal'});
  moduloActual.columnasGrillaDetalle.push({ "data": 'tanque.volumenTrabajo'});
  moduloActual.columnasGrillaDetalle.push({ "data": 'ocupado'});
  moduloActual.columnasGrillaDetalle.push({ "data": 'libre'});
  //Columnas de grilla del Detalle
  moduloActual.definicionColumnasDetalle.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-left" });
  moduloActual.definicionColumnasDetalle.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-left" });
  moduloActual.definicionColumnasDetalle.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasDetalle.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasDetalle.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });
  moduloActual.definicionColumnasDetalle.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-center" });

  moduloActual.inicializarCampos= function(){
	this.obj.cmpPlantaDespacho= $("#cmpPlantaDespacho");
	this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
	//Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaPlanificada.attr('data-fecha-actual');
    //var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
    //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 2
    var rangoSemana = utilitario.retornarfechaInicialFinal(fechaActual);
    this.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
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
          "daysOfWeek": [ "Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab" ],
          "monthNames": [ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" ]
        }
    });

    this.obj.filtroOperacion = $("#filtroOperacion");
    this.obj.filtroOperacion.select2();
 
    this.obj.filtroOperacion.on('change', function(e){
      moduloActual.idOperacion=$(this).val();
      moduloActual.volumenPromedioCisterna=$(this).find("option:selected").attr('data-volumen-promedio-cisterna');
      moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
      moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');
      moduloActual.plantaDespacho =$(this).find("option:selected").attr('data-planta-despacho');
      moduloActual.eta =$(this).find("option:selected").attr('data-eta');
      moduloActual.obj.ocultaContenedorTabla.show();
      try {
        $.ajax({
          type: constantes.PETICION_TIPO_GET,
          url: './dia_operativo/recuperarUltimoDiaPorOperacion', 
          contentType: moduloActual.TIPO_CONTENIDO, 
          data: {
          	idOperacion : parseInt(moduloActual.idOperacion) 
          },
          success: function(respuesta) {
            if (!respuesta.estado) {
          	  moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          	  moduloActual.obj.ocultaContenedorTabla.hide();
            } else {
          	  var fecha = respuesta.valor;
          	  console.log("fecha " + fecha);
          	  if(fecha != null){
  	        	  //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 2
  	        	  rangoSemana = utilitario.retornarfechaInicialFinal(fecha);
  	        	  //rangoSemana = utilitario.retornarRangoSemana(fecha);
  	        	  moduloActual.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
  	        	  //moduloActual.iniciarListado();
          	  } else {
          		 rangoSemana = utilitario.retornarfechaInicialFinal($(this).find("option:selected").attr('data-fecha-hoy'));
         		 moduloActual.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
          	  }
          	  moduloActual.iniciarListado();
          	  
            }
            moduloActual.obj.ocultaContenedorTabla.hide();
          },
          error: function(xhr,estado,error) {
            moduloActual.mostrarErrorServidor(xhr,estado,error); 
          }
        }); 
      } catch(error){
        moduloActual.mostrarDepuracion(error.message);
      }
      
      
      
      
     // moduloActual.recuperaUltimoDiaOperativoPorOperacion();
      e.preventDefault(); 
    });   


    
    //variables para correo
    this.obj.fechaParaNotificacion = $("#fechaParaNotificacion");
    this.obj.fechaCargaParaNotificar = $("#fechaCargaParaNotificar");
    this.obj.cantidadCisternasSolicitadas = $("#cantidadCisternasSolicitadas");
    
    
    //Campos formulario
    this.obj.espacioLibre = $("#espacioLibre");
    this.obj.valorParametro = $("#valorParametro");
    this.obj.cmpCliente = $("#cmpCliente");
    this.obj.cmpOperacion = $("#cmpOperacion");
    this.obj.cmpFechaPlanificada = $("#cmpFechaPlanificada");
    this.obj.cmpFechaPlanificada.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpFechaCarga= $("#cmpFechaCarga");
    this.obj.cmpFechaCarga.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    
    //Agregado por 9000002608================================
    this.obj.cmpCantCisternas = $("#cmpCantCisternas");
    this.obj.vistaCantidadCisternas = $("#vistaCantidadCisternas");
    //=======================================================
    this.obj.cmpFechaJornada= $("#cmpFechaJornada");
    this.obj.cmpIdOperacion = $("#cmpIdOperacion"); 
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaCliente=$("#vistaCliente");
    this.obj.vistaOperacion=$("#vistaOperacion");
    this.obj.vistaFechaPlanificacion=$("#vistaFechaPlanificacion");
    this.obj.vistaFechaCarga=$("#vistaFechaCarga");
    this.obj.vistaFechaJornada=$("#vistaFechaJornada");
    this.obj.vistaPlantaDespacho=$("#vistaPlantaDespacho");
    this.obj.vistaListaPlanificaciones=$("#vistaListaPlanificaciones");
    //Vista de auditoria
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");  
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");

    //cabecera detalle planificacion
    this.obj.detalleCliente=$("#detalleCliente");
    this.obj.detalleOperacion=$("#detalleOperacion");
    this.obj.detalleFechaPlanificacion=$("#detalleFechaPlanificacion");
    
    //botones Adicionales
    this.indiceFormulario=$("#indiceFormulario");
    this.obj.btnAgregarProducto=$("#btnAgregarProducto");    
    this.obj.grupoPlanificacion = $('#GrupoPlanificacion').sheepIt({
      separator: '',
      allowRemoveLast: true,
      allowRemoveCurrent: true,
      allowRemoveAll: true,
      allowAdd: true,
      allowAddN: false,
      maxFormsCount: 6,
      minFormsCount: 0,
      iniFormsCount: 0,
      afterAdd: function(origen, formularioNuevo) {
    	var cmpIdPlanificacion=$(formularioNuevo).find("input[elemento-grupo='idPlanificacion']");
        var cmpElementoProducto=$(formularioNuevo).find("select[elemento-grupo='producto']");
        var cmpVolumenPropuesto=$(formularioNuevo).find("input[elemento-grupo='volumenPropuesto']");
        var cmpPromedio=$(formularioNuevo).find("input[elemento-grupo='promedio']");
        var cmpVolumenSolicitado=$(formularioNuevo).find("input[elemento-grupo='volumenSolicitado']");
        var cmpNumeroCisternas=$(formularioNuevo).find("input[elemento-grupo='numeroCisternas']");
        var cmpObservacion=$(formularioNuevo).find("textarea[elemento-grupo='observacion']");
        var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");

        cmpElementoProducto.select2();
        cmpNumeroCisternas.val(0);
        cmpPromedio.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
        cmpVolumenPropuesto.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
        cmpVolumenSolicitado.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
        cmpNumeroCisternas.inputmask('decimal', {digits: 0});
        
        cmpElementoProducto.tipoControl="select2";
        moduloActual.obj.cmpSelect2Producto=$(formularioNuevo).find("select[elemento-grupo='producto']").select2({
    	  ajax: {
    		    url: "./producto/listarPorOperacion",
    		    dataType: 'json',
    		    delay: 250,
    		    "data": function (parametros) {
    		    	try{
    			      return {
    			    	filtroOperacion : moduloActual.idOperacion,
    			        page: parametros.page,
    			        paginacion:0
    			      };
    		    	} catch(error){
      		          console.log(error.message);
      		        };
    		    },
    		    processResults: function (respuesta, pagina) {
    		    	var resultados= respuesta.contenido.carga;
    		        return { results: resultados};
    		    },
    		    cache: true
    		  },
    		"language": "es",
    		"escapeMarkup": function (markup) { return markup; },
    		"templateResult": function (registro) {
    			if (registro.loading) {
    				return "Buscando...";
    			}
    	        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
    	    },
    	    "templateSelection": function (registro) {
               return registro.nombre || registro.text;
    	    },
      });
        
    cmpElimina.on("click", function(){
	    try{
	    	moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
	    	var numeroFormularios = moduloActual.obj.grupoPlanificacion.getForms().length;
    	   if (numeroFormularios == 1){
    		   moduloActual.obj.grupoPlanificacion.removeForm(0);
           	   $("#btnAgregarProducto").removeClass(constantes.CSS_CLASE_DESHABILITADA);
           } else {
        	   moduloActual.obj.grupoPlanificacion.removeForm(moduloActual.indiceFormulario);
           }
  	
	    } catch(error) {
	       console.log(error.message);
	    }
	  });
        
     cmpElementoProducto.on('change', function(e){
    	//var referenciaModulo=this;
    	var indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
    	console.log("indiceFormulario " + indiceFormulario);
    	if($(this).find("option:selected").val() > 0){
    		moduloActual.cambioProducto($(this).find("option:selected").val(), indiceFormulario);
	    /*try {
	      //Este ajax para el calculo del espacio disponible por producto
	      $.ajax({
	        type: constantes.PETICION_TIPO_GET,
	        url: './tanqueJornada/listar',
	        contentType: referenciaModulo.TIPO_CONTENIDO, 
	        data: {
	        	idOperacion : moduloActual.obj.filtroOperacion.val(),
	        	filtroEstado : constantes.TIPO_JORNADA_LIQUIDADO,
	        	filtroProducto: $(this).find("option:selected").val(),
	        	filtroFechaDiaOperativo : moduloActual.obj.cmpFechaJornada.val()
	        },
	        success: function(respuesta) {
	          if (!respuesta.estado) {
	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	          } else {
	        	  var suma = 0;
	        	  if(respuesta.contenido.carga.length > 0){
	        		  var itera = respuesta.contenido.carga.length;
		        	  for(var i=0; i < itera; i++){
		        		  var registro = respuesta.contenido.carga[i];
		        		  suma = suma +  parseInt(registro.libre);
		        	  }
	        	  }
	        	  cmpVolumenPropuesto.val(suma);
	          }
	        },
	      }); 
      	  //este ajax para el valor del promedio de descargas por producto
	      $.ajax({
		        type: constantes.PETICION_TIPO_GET,
		        url: './planificacion/recupera-promedio-producto',
		        contentType: referenciaModulo.TIPO_CONTENIDO, 
		        data: {
		        	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
		        	filtroProducto: $(this).find("option:selected").val(),
		        	filtroFechaInicio: utilitario.retornarSumaRestaFechas(-moduloActual.obj.valorParametro, moduloActual.obj.cmpFechaJornada.val()),
		        	filtroFechaFinal : moduloActual.obj.cmpFechaJornada.val()
		        },
		        success: function(respuesta) {
		          if (!respuesta.estado) {
		        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
		          } else {
		        	  if(respuesta.valor > 0){
		        		  cmpPromedio.val(respuesta.valor);
		        	  } else {
		        		  cmpPromedio.val(0);
		        	  }
		          }
		        },
		   }); 
		 } catch(error){
		   	console.log(error.message);
		 }*/
	   } else{
		   cmpVolumenPropuesto.val(0);
		   cmpPromedio.val(0);
	   }
       e.preventDefault(); 
     });
     
     //Agregado por 9000002608==========================================================================
     cmpVolumenSolicitado.on("keyup",function(){
         try{
           var volumenTotal = 0;
           var numeroFormularios = moduloActual.obj.grupoPlanificacion.getForms().length;
           for(var contador = 0; contador < numeroFormularios; contador++){  
        	   var fila = moduloActual.obj.grupoPlanificacion.getForm(contador);
        	   var volumenFila = fila.find("input[elemento-grupo='volumenSolicitado']").val();
        	   volumenFila = volumenFila.replace(',','');
        	   console.log('volumenFila: ' + volumenFila);
        	   if(volumenFila != null){
        		   volumenTotal = volumenTotal + parseInt(volumenFila);
        	   }
        	   console.log('volumenTotal: ' + volumenTotal);
           }
           
           var cantCisternas = Math.ceil(volumenTotal / moduloActual.volumenPromedioCisterna); 
           
           //Agregado por obs 9000002608============
           $("#cmpCantCisternas").prop('disabled', false);
           if(isNaN(cantCisternas)){
        	   cantCisternas = 0;
           }
           //=======================================
           $("#cmpCantCisternas").val(cantCisternas);
         } catch(error){
           console.log(error.message);
           }
         }); 
     //=================================================================================================
     /*Comentado por 9000002608
       cmpNumeroCisternas.on("input",function(){
        try{
          var totalVolumen = parseFloat(moduloActual.volumenPromedioCisterna) * parseInt(cmpNumeroCisternas.val()); 
          cmpVolumenSolicitado.val(totalVolumen);
        } catch(error){
          console.log(error.message);
          }
        });    */  
      },
    });   

    this.obj.btnAgregarProducto.on("click",function(){
      try {
    	moduloActual.obj.cntPlanificaciones.show();
        moduloActual.obj.grupoPlanificacion.addForm();
        //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 5
        moduloActual.validaUnicoProducto();
      } catch(error){
      console.log(error.message);
      }
    });    
  
    moduloActual.obj.cmpFechaPlanificada.on("change",function(e){
    	//var eta = moduloActual.obj.filtroOperacion.find("option:selected").attr('data-eta');
    	//moduloActual.obj.cmpFechaCarga.val(utilitario.retornarSumaRestaFechas(parseInt(eta * -1), moduloActual.obj.cmpFechaPlanificada.val()));
    	moduloActual.validaFechaPlanificada();
    	document.getElementById('cmpFechaCarga').focus();
    });
    
    moduloActual.obj.cmpFechaCarga.on("change", function(e){
    	//var eta = moduloActual.obj.filtroOperacion.find("option:selected").attr('data-eta');
     	//moduloActual.obj.cmpFechaPlanificada.val(utilitario.retornarSumaRestaFechas(eta, moduloActual.obj.cmpFechaCarga.val()));
     	moduloActual.validaFechaPlanificada();
     	document.getElementById('cmpFechaPlanificada').focus();
    });
    
    moduloActual.obj.cmpFechaPlanificada.on("keypress", function(e){
    	//var eta = moduloActual.obj.filtroOperacion.find("option:selected").attr('data-eta');
    	if (e.which == 13) { //valido que se presione la tecla enter
	      //moduloActual.obj.cmpFechaCarga.val(utilitario.retornarSumaRestaFechas(parseInt(eta * -1), moduloActual.obj.cmpFechaPlanificada.val()));
	      moduloActual.validaFechaPlanificada();
	      document.getElementById('cmpFechaCarga').focus();
    	}
    });
    
    moduloActual.obj.cmpFechaCarga.on("keypress", function(e){
    	//var eta = moduloActual.obj.filtroOperacion.find("option:selected").attr('data-eta');
   	  	if (e.which == 13) {  //valido que se presione la tecla enter
	    //moduloActual.obj.cmpFechaPlanificada.val(utilitario.retornarSumaRestaFechas(eta, moduloActual.obj.cmpFechaCarga.val()));
	    moduloActual.validaFechaPlanificada();
	    document.getElementById('cmpFechaPlanificada').focus();
      }
    });
  };  
  
  moduloActual.cambioProducto = function(productoSeleccionado, indiceFormulario){
	var referenciaModulo = this;
	referenciaModulo.obj.ocultaContenedorTabla.show();
	var formulario = moduloActual.obj.grupoPlanificacion.getForm(indiceFormulario);
	try {
	  //Este ajax para el calculo del espacio disponible por producto
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: './tanqueJornada/listar',
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {
        	idOperacion : moduloActual.obj.filtroOperacion.val(),
        	filtroEstado : constantes.TIPO_JORNADA_LIQUIDADO,
        	filtroProducto: productoSeleccionado,
        	filtroFechaDiaOperativo : moduloActual.obj.cmpFechaJornada.val()
        },
        success: function(respuesta) {
          if (!respuesta.estado) {
        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
          } else {
        	  var suma = 0;
        	  if(respuesta.contenido.carga.length > 0){
        		  var itera = respuesta.contenido.carga.length;
	        	  for(var i=0; i < itera; i++){
	        		  var registro = respuesta.contenido.carga[i];
	        		  suma = suma +  parseInt(registro.ocupado);
	        	  }
        	  }
        	  formulario.find("input[elemento-grupo='volumenPropuesto']").val(suma);
        	  //cmpVolumenPropuesto.val(suma);
          }
        },
      }); 
  	  //este ajax para el valor del promedio de descargas por producto
      $.ajax({
	        type: constantes.PETICION_TIPO_GET,
	        url: './planificacion/recupera-promedio-producto',
	        contentType: referenciaModulo.TIPO_CONTENIDO, 
	        data: {
	        	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
	        	filtroProducto: productoSeleccionado,
	        	filtroFechaInicio: utilitario.retornarSumaRestaFechas(-moduloActual.obj.valorParametro, moduloActual.obj.cmpFechaJornada.val()),
	        	filtroFechaFinal : moduloActual.obj.cmpFechaJornada.val()
	        },
	        success: function(respuesta) {
	          if (!respuesta.estado) {
	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	          } else {
	        	  if(respuesta.valor > 0){
	        		  formulario.find("input[elemento-grupo='promedio']").val(respuesta.valor);
	        		  //cmpPromedio.val(respuesta.valor);
	        	  } else {
	        		  formulario.find("input[elemento-grupo='promedio']").val(0);
	        		  //cmpPromedio.val(0);
	        	  }
	          }
	        },
	   }); 
      referenciaModulo.obj.ocultaContenedorTabla.hide();
	 } catch(error){
		 referenciaModulo.obj.ocultaContenedorTabla.hide();
	   	console.log(error.message);
	 }
  },
  
  //TODO
  moduloActual.recuperaUltimoDiaOperativoPorOperacion= function(){
    var referenciaModulo=this;
    referenciaModulo.obj.ocultaContenedorTabla.show();
    try {
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: './dia_operativo/recuperarUltimoDiaPorOperacion', 
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {
        	idOperacion : parseInt(referenciaModulo.idOperacion) 
        },
        success: function(respuesta) {
          if (!respuesta.estado) {
        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        	  referenciaModulo.obj.ocultaContenedorTabla.hide();
          } else {
        	  var fecha = respuesta.valor;
        	  console.log("fecha " + fecha);
        	  if(fecha != null){
	        	  //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 2
	        	  rangoSemana = utilitario.retornarfechaInicialFinal(fecha);
	        	  //rangoSemana = utilitario.retornarRangoSemana(fecha);
	        	  
	        	  referenciaModulo.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
	        	  referenciaModulo.iniciarListado();
        	  }
          }
          referenciaModulo.obj.ocultaContenedorTabla.hide();
        },
        error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      }); 
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    }
  };
	  
  moduloActual.validaFechaPlanificada= function(){
    var referenciaModulo=this;
    referenciaModulo.obj.ocultaContenedorFormulario.show();
    try {
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: referenciaModulo.URL_VALIDA_FECHA_PLANIFICADA, 
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {filtroFechaPlanificada : moduloActual.obj.cmpFechaPlanificada.val(), filtroOperacion : referenciaModulo.idOperacion },
        success: function(respuesta) {
          if (!respuesta.estado) {
        	  referenciaModulo.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
        	  referenciaModulo.obj.btnAgregarProducto.addClass(constantes.CSS_CLASE_DESHABILITADA);
        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
        	  referenciaModulo.obj.cntPlanificaciones.hide();
        	  referenciaModulo.obj.btnRegresar.show();
          } else {
        	  referenciaModulo.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
        	  referenciaModulo.obj.btnAgregarProducto.removeClass(constantes.CSS_CLASE_DESHABILITADA);
        	  referenciaModulo.obj.btnRegresar.hide();
        	  referenciaModulo.obj.grupoPlanificacion.removeAllForms();
        	  referenciaModulo.obj.cntPlanificaciones.show();
          }
          referenciaModulo.obj.ocultaContenedorFormulario.hide();
        },
        error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      }); 
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    }
  };
  
  moduloActual.validaUnicoProducto = function(){
	var referenciaModulo = this;
	referenciaModulo.obj.ocultaContenedorTabla.show();
	try {
	  //Este ajax para ver si la operación solo cuenta con un producto configurado
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: './producto/listarPorOperacion',
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {
        	filtroOperacion : moduloActual.idOperacion,
        },
        success: function(respuesta) {
          if (!respuesta.estado) {
        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
          } else {
        	  console.log("respuesta.contenido.carga.length " + respuesta.contenido.carga.length);
        	  if(respuesta.contenido.carga.length == 1){
        		  var formulario = moduloActual.obj.grupoPlanificacion.getForm(0);
        		  var registro = respuesta.contenido.carga[0];
        		  console.log("registro " + registro);
        	      var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
        	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.id);
        	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.nombre);
        	      formulario.find("select[elemento-grupo='producto']").empty().append(elemento1).val(registro.id).trigger('change');
        	      formulario.find("select[elemento-grupo='producto']").prop('disabled', true);
        	      $("#btnAgregarProducto").addClass(constantes.CSS_CLASE_DESHABILITADA);
        	  }
          }
          referenciaModulo.obj.ocultaContenedorTabla.hide();
        },
      error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      }); 
	 } catch(error){
		 referenciaModulo.obj.ocultaContenedorTabla.hide();
	   	console.log(error.message);
	 }
  },
  
  moduloActual.iniciarAgregar= function(){
    var referenciaModulo=this;
    var nombreOperacion="";
    var nombreCliente="";
    var plantaDespacho ="";
    var eta="";
    try {
      //moduloActual.obj.cmpTransportista.empty().append(elemento1).val(0).trigger('change');
      

	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_PLANIFICACION);
      referenciaModulo.obj.grupoPlanificacion.removeAllForms();
      moduloActual.obj.cntPlanificaciones.hide();
      //referenciaModulo.obj.grupoPlanificacion.addForm();  
      referenciaModulo.idOperacion = referenciaModulo.obj.filtroOperacion.val();
      moduloActual.volumenPromedioCisterna=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-volumen-promedio-cisterna');
      nombreOperacion=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-nombre-operacion');
      nombreCliente=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-nombre-cliente');
      plantaDespacho=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-planta-despacho');
      eta=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-eta');
	  referenciaModulo.obj.ocultaContenedorTabla.show();
	 // moduloActual.validaUnicoProducto();
	  $.ajax({
	        type: constantes.PETICION_TIPO_GET,
	        url: './planificacion/valida-operacion', 
	        contentType: referenciaModulo.TIPO_CONTENIDO, 
	        data: {idOperacion : referenciaModulo.idOperacion },
	        success: function(respuesta) {
	  	      if (!respuesta.estado) {
	  	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	  	    	  referenciaModulo.obj.ocultaContenedorTabla.hide();
	  	      } else {		
	  	    	$.ajax({
	  		        type: constantes.PETICION_TIPO_GET,
	  		        url: './jornada/recuperar-ultimo-dia', 
	  		        contentType: referenciaModulo.TIPO_CONTENIDO, 
	  		        data: {
	  		        	idOperacion:referenciaModulo.idOperacion,
	  		        	filtroEstado : constantes.TIPO_JORNADA_LIQUIDADO,
	  			    },
	  		        success: function(respuesta) {
	  		          if (!respuesta.estado) {
	  		            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	  		          } else {
	  		        	referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	  		            referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_AGREGAR_REGISTRO);
	  		            referenciaModulo.resetearFormulario();
	  		            if(respuesta.valor != null){
	  		            	referenciaModulo.obj.cmpFechaJornada.val(utilitario.formatearFecha(respuesta.valor));
	  		          	}
	  		          
	  		            referenciaModulo.obj.cmpCliente.val(nombreCliente);
	  		            referenciaModulo.obj.cmpOperacion.val(nombreOperacion);
	  		            referenciaModulo.obj.cmpPlantaDespacho.val(plantaDespacho);
	  		            referenciaModulo.obj.datFormularioAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	  		            moduloActual.recuperaFechaPlanificacion(eta);
//    	  		            moduloActual.obj.cmpFechaPlanificada.val("");
//    	  		            moduloActual.obj.cmpFechaCarga.val("");
	  		            referenciaModulo.obj.cntTabla.hide();
	  		            referenciaModulo.obj.cntVistaRegistro.hide();
	  		            referenciaModulo.obj.cntFormulario.show();
	  		            
		  		          referenciaModulo.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  	        	  referenciaModulo.obj.btnAgregarProducto.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  	        	  referenciaModulo.obj.btnRegresar.hide();
		  	        	  referenciaModulo.obj.grupoPlanificacion.removeAllForms();
		  	        	  referenciaModulo.obj.cntPlanificaciones.show();
	  		            
	  		            //referenciaModulo.obj.btnRegresar.show();
	  		            referenciaModulo.obj.ocultaContenedorTabla.hide();
	  		            referenciaModulo.recuperaParametro();
	  		            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	  		            referenciaModulo.obj.ocultaContenedorFormulario.hide();
	  		            document.getElementById('cmpFechaCarga').focus();
	  		          }
	  		          referenciaModulo.obj.ocultaContenedorTabla.hide();
	  		        },
	  		        error: function(xhr,estado,error) {
	  		          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	  		        }
	  		      }); 
	  	      }		
	        }
	      }); 
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    };
  };
  
  moduloActual.recuperaFechaPlanificacion = function(eta){
	  var referenciaModulo=this;
	  $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: referenciaModulo.URL_RECUPERAR_ULTIMO_DIA, 
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {idOperacion:referenciaModulo.idOperacion},
        success: function(respuesta) {
          if (!respuesta.estado) {
            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
          } else {
        	  referenciaModulo.obj.cmpFechaPlanificada.val(respuesta.valor);
              moduloActual.obj.cmpFechaCarga.val(utilitario.retornarSumaRestaFechas(-eta, respuesta.valor));
              referenciaModulo.obj.cmpFechaPlanificada.prop('disabled', true);
              referenciaModulo.obj.cmpFechaCarga.prop('disabled', true);
          }
          referenciaModulo.obj.ocultaContenedorFormulario.hide();
        },
        error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      });
  };

  moduloActual.llenarFormulario = function(registro){
    var referenciaModulo=this;
    var numeroPlanificaciones=0;
    this.idRegistro = registro.id;
    referenciaModulo.idOperacion = referenciaModulo.obj.filtroOperacion.val();
    moduloActual.volumenPromedioCisterna=$("#filtroOperacion option[value='"+referenciaModulo.idOperacion+"']").attr('data-volumen-promedio-cisterna');
    
    this.obj.cmpCliente.val(registro.operacion.cliente.razonSocial);
    this.obj.cmpOperacion.val(registro.operacion.nombre);	
    this.obj.cmpIdOperacion.val(registro.operacion.id);
    referenciaModulo.idOperacion = registro.operacion.id;	
    referenciaModulo.fechaOperativa = utilitario.formatearFecha(registro.fechaOperativa);
    referenciaModulo.obj.cmpFechaPlanificada.val(utilitario.formatearFecha(registro.fechaOperativa));	
    referenciaModulo.obj.cmpFechaCarga.val(utilitario.formatearFecha(registro.fechaEstimadaCarga));	
    referenciaModulo.obj.cmpPlantaDespacho.val(registro.operacion.plantaDespacho.descripcion);
    //moduloActual.volumenPromedioCisterna = registro.operacion.volumenPromedioCisterna;
    
    //Agregado por 9000002608===========================================================
    referenciaModulo.obj.cmpCantCisternas.val(registro.cantidadCisternasPlan);
    //================================================================================
    
    referenciaModulo.obj.grupoPlanificacion.removeAllForms();
    
    if (registro.planificaciones != null){
    	numeroPlanificaciones = registro.planificaciones.length;
    }
   /* if (numeroPlanificaciones > 0){
    	moduloActual.obj.cntPlanificaciones.show();
    } else{
    	moduloActual.obj.cntPlanificaciones.hide();
    }*/
    
    for(var contador=0; contador < numeroPlanificaciones;contador++){
      referenciaModulo.obj.grupoPlanificacion.addForm();
      var formulario= referenciaModulo.obj.grupoPlanificacion.getForm(contador);
      
      var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.planificaciones[contador].idProducto);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.planificaciones[contador].producto.nombre);
      formulario.find("select[elemento-grupo='producto']").empty().append(elemento1).val(registro.planificaciones[contador].idProducto).trigger('change');
      
      //formulario.find("select[elemento-grupo='producto']").select2("val", registro.planificaciones[contador].idProducto);
      formulario.find("input[elemento-grupo='idPlanificacion']").val(registro.planificaciones[contador].id);
      formulario.find("input[elemento-grupo='volumenPropuesto']").val(registro.planificaciones[contador].volumenPropuesto);
      formulario.find("input[elemento-grupo='volumenSolicitado']").val(registro.planificaciones[contador].volumenSolicitado);
      formulario.find("input[elemento-grupo='numeroCisternas']").val(registro.planificaciones[contador].cantidadCisternas);
      formulario.find("textarea[elemento-grupo='observacion']").val(registro.planificaciones[contador].observacion);  
      
      formulario.find("select[elemento-grupo='producto']").prop('disabled', true);
      formulario.find("[elemento-grupo='botonElimina']").addClass(constantes.CSS_CLASE_DESHABILITADA);
    }
    
    //SAR Nº SGCO-OI-004-2016 - REQUERIMIENTO 5
    if(numeroPlanificaciones > 0){
    	moduloActual.validaUnicoProducto();
    }
        
  };

  moduloActual.llenarDetalles = function(registro){
	var referenciaModulo=this;
    this.idRegistro = registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaCliente.text(registro.operacion.cliente.razonSocial);
    this.obj.vistaOperacion.text(registro.operacion.nombre);
    this.obj.vistaFechaPlanificacion.text(utilitario.formatearFecha(registro.fechaOperativa));
    this.obj.vistaFechaCarga.text(utilitario.formatearFecha(registro.fechaEstimadaCarga));
    this.obj.vistaFechaJornada.text(utilitario.formatearFecha(registro.ultimaJornadaLiquidada));
    if(registro.operacion.idPlantaDespacho > 0){
    	this.obj.vistaPlantaDespacho.text(registro.operacion.plantaDespacho.descripcion);
    }
    referenciaModulo.obj.datDetalleAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
    
    //Agregado por 9000002608=================================================
    this.obj.vistaCantidadCisternas.text(registro.cantidadCisternasPlan);
    //========================================================================
    
    this.obj.vistaListaPlanificaciones=$("#vistaListaPlanificaciones");
    var nombreProducto="";
    var volumenPropuesto=0;
    var volumenSolicitado=0;
    var cantidadCisternas=0;
    var observacion="";
    var bitacora="";
    var filaNueva="";
    if(registro.planificaciones != null){
	    var indice= registro.planificaciones.length;
	    
	    $("#tablaVistaDetalle tbody").empty();

	    //$("#tablaVistaDetalle tr").remove();
	    for(var k = 0; k < indice; k++){ 
	    	nombreProducto=registro.planificaciones[k].producto.nombre;
	    	volumenPropuesto=registro.planificaciones[k].volumenPropuesto;
	    	
	    	//Agregado por obs 9000002608=============================================
	    	volumenSolicitado=registro.planificaciones[k].volumenSolicitado.toLocaleString();
	        //========================================================================
	    	//Comentado por obs 9000002608=============================================
//	    	volumenSolicitado=registro.planificaciones[k].volumenSolicitado;
	        //========================================================================
	    	cantidadCisternas=registro.planificaciones[k].cantidadCisternas;
	    	observacion=registro.planificaciones[k].observacion;
	    	
	    	var partes = registro.planificaciones[k].bitacora.trim().split("|");
		   	bitacora = partes[0];
		   	if (partes[1] != null && partes[1].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[1];
		   	}
		   	if (partes[2] != null && partes[2].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[2];
		   	}
		   	if (partes[3] != null && partes[3].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[3];
		   	}
		   	if (partes[4] != null && partes[4].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[4];
		   	}
		   	if (partes[5] != null && partes[5].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[5];
		   	}
		   	if (partes[6] != null && partes[6].length > 0){
		   		bitacora = bitacora + ' <br> ' + partes[6];
		   	}
		   	
	    	//bitacora=registro.planificaciones[k].bitacora;
	    	filaNueva='<tr><td>'+nombreProducto+'</td>' +
	    			       '<td class="text-right">'+volumenPropuesto+'</td>' +
	    			       '<td class="text-right">'+volumenSolicitado+'</td>' +
	    			       //Agregado por 9000002608=================================
	    			       '</tr>';
	    			       //========================================================
	    					//Comentado por 9000002608=================================
//	    			       '<td class="text-right">'+cantidadCisternas+'</td></tr>';
	    			       //========================================================
	    	$("#tablaVistaDetalle > tbody:last").append(filaNueva);
	    	filaNueva='<tr><td colspan="4"><b> Observaciones: </b>'+observacion+'</td></tr>';
	    	$("#tablaVistaDetalle > tbody:last").append(filaNueva);
	    	filaNueva='<tr><td colspan="4"><b> Bitacora de Cambios: </b> <br>'+bitacora+'</td></tr>';
	    	$("#tablaVistaDetalle > tbody:last").append(filaNueva);
	    }
	    //Vista de auditoria
//	    this.obj.vistaActualizadoEl.text(registro.planificaciones[0].fechaActualizacion);
//	    this.obj.vistaActualizadoPor.text(registro.planificaciones[0].usuarioActualizacion);
//	    this.obj.vistaIpActualizacion.text(registro.planificaciones[0].ipActualizacion);
//	    this.obj.vistaCreadoPor.text(registro.planificaciones[0].usuarioCreacion);
//	    this.obj.vistaCreadoEl.text(registro.planificaciones[0].fechaCreacion);
//	    this.obj.vistaIpCreacion.text(registro.planificaciones[0].ipCreacion);
	    
    }
    else{
    	$("#tablaVistaDetalle tbody tr").remove();
    }
	this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    
  };
  
  moduloActual.llenarDetallesPlanificaciones = function(registros){
	var referenciaModulo=this;
	var bitacora = "";
    this.obj.detalleCliente.text(registros[0].diaOperativo.operacion.cliente.razonSocial);
    this.obj.detalleOperacion.text(registros[0].diaOperativo.operacion.nombre);
    this.obj.detalleFechaPlanificacion.text(referenciaModulo.obj.filtroFechaPlanificada.val());
    this.obj.cmpPara.val(registros[0].diaOperativo.operacion.correoPara);
    this.obj.cmpCC.val(registros[0].diaOperativo.operacion.correoCC);
    this.obj.cmpETA = registros[0].diaOperativo.operacion.eta;

    var filaNueva 	= $('#tablaDetallePlanificacion');
    $('#tablaDetallePlanificacion').html("");
    g_tr = '<thead><tr><th class="text-center">Fecha Planificada	</th>' +
    				  '<th class="text-center">Fecha Carga			</th>' + 
    				  '<th class="text-center">Producto		 		</th>' + 
    				  /* Comentado por 9000002608
    				  '<th class="text-center">Cant. Cisternas		</th>' +
    				  */
    				  //Agregado por 9000002608===================================
    				  '<th class="text-center">Vol. Solicitado		</th>' +
    				  //==========================================================
    				  '<th class="text-center">Observaci&oacute;n	</th>' + 
    				  '<th class="text-center">Bitacora de Cambios	</th></tr></thead>'; 
    filaNueva.append(g_tr);
    
    for(var k = 0; k < registros.length; k++){ 	
    	var planificacion = registros[k]; 
    	
   	var partes = planificacion.bitacora.trim().split("|");
   	bitacora = partes[0];
   	if (partes[1] != null && partes[1].length > 0){
   		bitacora = bitacora + ' <br> ' + partes[1];
   	}
   	if (partes[2] != null && partes[2].length > 0){
   		bitacora = bitacora + ' <br> ' + partes[2];
   	}
   	if (partes[3] != null && partes[3].length > 0){
   		bitacora = bitacora + ' <br> ' + partes[3];
   	}
   	if (partes[4] != null && partes[4].length > 0){
   		bitacora = bitacora + ' <br> ' + partes[4];
   	}
    	
     g_tr  = '<tr><td class="text-center" style="width:7%;">'   + planificacion.diaOperativo.fechaOperativa			+ '</td>' + // fceha planificada
    		'    <td class="text-center" style="width:7%;">'    + planificacion.diaOperativo.fechaEstimadaCarga  	+ '</td>' + // fecha carga
    		'    <td class="text-left" style="width:20%;">'     + planificacion.producto.nombre  					+ '</td>' + // nombre del producto
    		/* Comentado por 9000002608 
    		'    <td class="text-right"	style="width:6%;">' 	+ planificacion.cantidadCisternas 					+ '</td>' + // cantidad de cisternas
    		*/
    		// Agregado por 9000002608
    		'    <td class="text-right"	style="width:6%;">' 	+ planificacion.volumenSolicitado					+ '</td>' + // cantidad de cisternas
    		//================================================================================
    		'    <td class="text-left"	style="width:30%;">'    + planificacion.observacion 						+ '</td>' + // cantidad de cisternas
    		'    <td class="text-left" style="width:30%;">' 	+ bitacora			   								+ '</td></tr>'; // observaciones

      filaNueva.append(g_tr);
    }
  };

  moduloActual.recuperarValores = function(registro){
	var referenciaModulo=this;
    var eRegistro = null;

    try {
      eRegistro={};
      eRegistro.id = parseInt(referenciaModulo.idRegistro);
      //eRegistro.fechaOperativa = utilitario.formatearStringToDate(referenciaModulo.fechaOperativa);
      eRegistro.fechaOperativa = utilitario.formatearStringToDate(moduloActual.obj.cmpFechaPlanificada.val());
      eRegistro.idOperacion = parseInt(referenciaModulo.idOperacion);
      eRegistro.fechaEstimadaCarga = utilitario.formatearStringToDate(moduloActual.obj.cmpFechaCarga.val());
      eRegistro.ultimaJornadaLiquidada= utilitario.formatearStringToDate(referenciaModulo.obj.cmpFechaJornada.val());
      //Agregado por 9000002608============================================
      console.log('Cantidad Cisternas' + moduloActual.obj.cmpCantCisternas.val());
      eRegistro.cantidadCisternasPlan = parseInt(moduloActual.obj.cmpCantCisternas.val());
      //===================================================================
      eRegistro.planificaciones=[];
      var numeroFormularios = referenciaModulo.obj.grupoPlanificacion.getForms().length;
      for(var contador = 0;contador < numeroFormularios;contador++){
        var planificacion={};
        var formulario 			= referenciaModulo.obj.grupoPlanificacion.getForm(contador);        
        var cmpProducto 		= formulario.find("select[elemento-grupo='producto']");
        var cmpIdPlanificacion 	= formulario.find("input[elemento-grupo='idPlanificacion']");
        var cmpVolumenPropuesto = formulario.find("input[elemento-grupo='volumenPropuesto']");
        var cmpVolumenSolicitado= formulario.find("input[elemento-grupo='volumenSolicitado']");
        var cmpNumeroCisternas	= formulario.find("input[elemento-grupo='numeroCisternas']");
        var cmpObservacion		= formulario.find("textarea[elemento-grupo='observacion']");

        planificacion.idProducto= parseInt(cmpProducto.val());
        planificacion.id = parseInt(cmpIdPlanificacion.val());
        planificacion.volumenPropuesto= parseFloat(cmpVolumenPropuesto.val().replace(moduloActual.SEPARADOR_MILES,""));
        planificacion.volumenSolicitado= parseFloat(cmpVolumenSolicitado.val().replace(moduloActual.SEPARADOR_MILES,""));
        planificacion.cantidadCisternas= parseInt(cmpNumeroCisternas.val());
        planificacion.observacion = cmpObservacion.val().toUpperCase();
        if(planificacion.idProducto > 0){
        	eRegistro.planificaciones.push(planificacion);
        }
      }
    } catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  //Agregado por obs 9000002608===============================
  moduloActual.validarVolumenYCisternas = function(retorno){
	  referenciaModulo = this;
	  retorno = true;

	  var cantCisternas = $("#cmpCantCisternas").val();
	  var volumenTotal = 0;
	  var numeroFormularios = moduloActual.obj.grupoPlanificacion.getForms().length;
      for(var contador = 0; contador < numeroFormularios; contador++){  
	   	   var fila = moduloActual.obj.grupoPlanificacion.getForm(contador);
	   	   var volumenFila = fila.find("input[elemento-grupo='volumenSolicitado']").val();
	   	   volumenFila = volumenFila.replace(',','');
	   	   console.log('volumenFila: ' + volumenFila);
	   	   if(volumenFila != null){
	   		   volumenTotal = volumenTotal + parseInt(volumenFila);
	   	   }
	   	   console.log('volumenTotal: ' + volumenTotal);
      }
      
      if(cantCisternas == 0 && volumenTotal != 0){
    	  retorno = false;
    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El nro de cisternas a planificar no tiene correspondencia con el volumen solicitado. Por favor verifique.");
    	  return retorno;
      }
      
      if(cantCisternas != 0 && volumenTotal == 0){
    	  retorno = false;
    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El nro de cisternas a planificar no tiene correspondencia con el volumen solicitado. Por favor verifique.");
    	  return retorno;
      }
      
      if(cantCisternas > 0 && volumenTotal <= 0){
    	  retorno = false;
    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El nro de cisternas a planificar no tiene correspondencia con el volumen solicitado. Por favor verifique.");
    	  return retorno;
      }
      
      if(cantCisternas <= 0 && volumenTotal > 0){
    	  retorno = false;
    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El nro de cisternas a planificar no tiene correspondencia con el volumen solicitado. Por favor verifique.");
    	  return retorno;
      }
      
      return retorno;
	  
  };
  //==========================================================

  moduloActual.validarProductoPlanificacion = function(retorno){
		referenciaModulo = this;
		retorno = true;
		try{
			var numeroFormularios = referenciaModulo.obj.grupoPlanificacion.getForms().length;
			for(var contador = 0; contador < numeroFormularios;contador++){
		        var formulario = referenciaModulo.obj.grupoPlanificacion.getForm(contador);        
		        var cmpProducto = formulario.find("select[elemento-grupo='producto']");
		        //Comentado por 9000002608==================================================================
//		        var cmpNumeroCisternas = formulario.find("input[elemento-grupo='numeroCisternas']");
		        //==========================================================================================
		        //Agregado por 9000002608==================================================================
		        var cmpVolumenSolicitado = formulario.find("input[elemento-grupo='volumenSolicitado']");
		        console.log('cmpVolumenSolicitado' + cmpVolumenSolicitado);
		        //==========================================================================================
		        var idProducto = parseInt(cmpProducto.val());
		      //Comentado por 9000002608==================================================================
//		        var cantCisternas = parseInt(cmpNumeroCisternas.val());
		      //==========================================================================================
		        //Agregado por 9000002608==================================================================
		        var volumenSoli = parseInt(cmpVolumenSolicitado.val().replace(',',''));
		        //=========================================================================================
		        if(idProducto == 0){
		        	fila = contador + 1;
		        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe seleccionar un producto en la fila "+ fila +". Favor verifique.");
		        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
		        	retorno = false;
	        		return retorno;
		        }
		        /* Comentado por 9000002608
		        if(referenciaModulo.modoEdicion == constantes.MODO_NUEVO){
			        if(cantCisternas < 0 || cantCisternas == 0){
			        	fila = contador + 1;
			        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La cantidad de cisternas de la fila "+ fila +" no puede ser "+cantCisternas + ". Favor verifique.");
			        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
			        	retorno = false;
		        		return retorno;
			        }
		        }*/
		        
		      //Agregado por 9000002608==================================================================
		        console.log('volumenSoli' + volumenSoli);
		        if(volumenSoli < 0 || isNaN(volumenSoli)){
		        	fila = contador + 1;
		        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El volumen solicitado de la fila "+ fila +" es incorrecto. Favor verifique.");
		        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
		        	retorno = false;
	        		return retorno;
		        }
		      //=========================================================================================
		        
		       /* var cmpNumeroCisternas= formulario.find("input[elemento-grupo='numeroCisternas']");
		        if(parseInt(cmpNumeroCisternas.val()) > 0 ){
			        for(var comparador = 0; comparador < numeroFormularios; comparador++){
			        	if(comparador != contador){
				        	var comparacion = referenciaModulo.obj.grupoPlanificacion.getForm(comparador);
				        	var producto = comparacion.find("select[elemento-grupo='producto']");
				        	if(cmpProducto.val() != producto.val()){
				        		retorno = true;
				        	}
				        	else{
				        		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No puede planificar 2 productos iguales. ");
				        		referenciaModulo.obj.ocultaContenedorFormulario.hide();
				        		retorno = false;
				        		return retorno;
				        	}
			        	}
			        }
		        }
		        else{
		        	 referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El valor de N# Cisterna no puede ser 0. ");
		     		 referenciaModulo.obj.ocultaContenedorFormulario.hide();
		        	 retorno = false;
		        	 return retorno;
		        }*/
		    }
			return retorno;
		}
		catch(error){
		      console.log(error.message);
		}
	};

  moduloActual.inicializar();
});