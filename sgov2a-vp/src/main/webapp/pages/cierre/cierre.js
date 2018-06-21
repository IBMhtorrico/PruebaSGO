$(document).ready(function(){
  $.fn.select2.defaults.set( "theme", "bootstrap" );
  var moduloActual = new moduloCierre();  
  moduloActual.urlBase='cierre';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_CERRAR_DIA = moduloActual.urlBase + '/cerrar-dia';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];  
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'});
  moduloActual.columnasGrilla.push({ "data": 'totalCisternasAsignados'});
  moduloActual.columnasGrilla.push({ "data": 'totalCisternasDescargados'});
  moduloActual.columnasGrilla.push({ "data": 'fechaActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.nombre'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.cliente.razonSocial'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  moduloActual.columnasGrilla.push({ "data": 'operacion.id'});

  //Columnas
  moduloActual.definicionColumnas.push({ "targets" : 1, "searchable" : true, "orderable" : true, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 2, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnas.push({ "targets" : 3, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 4, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 5, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 6, "searchable" : true, "orderable" : true, "visible" : true });
  moduloActual.definicionColumnas.push({ "targets" : 7, "searchable" : true, "orderable" : true, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 8, "searchable" : true, "orderable" : true, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 9, "searchable" : true, "orderable" : true, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo });
  moduloActual.definicionColumnas.push({ "targets" : 10, "searchable" : true, "orderable" : true, "visible" : false });

  moduloActual.ordenGrillaResumen=[[ 1, 'asc' ]];  
  moduloActual.columnasGrillaResumen.push({ "data": 'id'}); 
  moduloActual.columnasGrillaResumen.push({"className":'details-control',"orderable":false,"data":null,"defaultContent": ''});   
  moduloActual.columnasGrillaResumen.push({ "data": 'nombreEstacion'});
  moduloActual.columnasGrillaResumen.push({ "data": 'tractoCisterna'});
  moduloActual.columnasGrillaResumen.push({ "data": 'razonSocialTransportista'});
  moduloActual.columnasGrillaResumen.push({ "data": 'metodoDescarga'});
  moduloActual.columnasGrillaResumen.push({ "data": 'volumenObservadoDespachado'});
  moduloActual.columnasGrillaResumen.push({ "data": 'volumenCorregidoDespachado'});
  moduloActual.columnasGrillaResumen.push({ "data": 'volumenObservadoRecibido'});
  moduloActual.columnasGrillaResumen.push({ "data": 'volumenCorregidoRecibido'});
  moduloActual.columnasGrillaResumen.push({ "data": 'entradaTotal'});
  moduloActual.columnasGrillaResumen.push({ "data": 'salidaTotal'});
  moduloActual.columnasGrillaResumen.push({ "data": 'variacion'});
  moduloActual.columnasGrillaResumen.push({ "data": 'limitePermisible'});
  moduloActual.columnasGrillaResumen.push({ "data": 'resultado'});
  moduloActual.columnasGrillaResumen.push({ "data": 'estadoDescarga'});
  
  moduloActual.definicionColumnasResumen.push({ "targets" : 1, "searchable" : false, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasResumen.push({ "targets" : 2, "searchable" : false, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnasResumen.push({ "targets" : 3, "searchable" : false, "orderable" : false, "visible" : true  , "class": "text-left"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 4, "searchable" : false, "orderable" : false, "visible" : true  , "class": "text-left" });
  moduloActual.definicionColumnasResumen.push({ "targets" : 5, "searchable" : false, "orderable" : false, "visible" : false  , "class": "text-left" });
  moduloActual.definicionColumnasResumen.push({ "targets" : 6, "searchable" : false, "orderable" : false, "visible" : true  , "class": "text-left",
    "render":function(valor){
      return utilitario.formatearMetodoDescarga(valor);
    }
  });
  moduloActual.definicionColumnasResumen.push({ "targets" : 7, "searchable" : false, "orderable" : false, "visible" : true  , "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 8, "searchable" : false, "orderable" : false, "visible" : true , "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 9, "searchable" : false, "orderable" : false, "visible" : true , "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 10, "searchable" : false, "orderable" : false, "visible" : true  , "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 11, "searchable" : false, "orderable" : false, "visible" : true , "class": "text-right" });
  moduloActual.definicionColumnasResumen.push({ "targets" : 12, "searchable" : false, "orderable" : false, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasResumen.push({ "targets" : 13, "searchable" : false, "orderable" : false, "visible" : true, "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 14, "searchable" : false, "orderable" : false, "visible" : true,  "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 15, "searchable" : false, "orderable" : false, "visible" : true,  "class": "text-right"});
  moduloActual.definicionColumnasResumen.push({ "targets" : 16, "searchable" : true, "orderable" : false, "visible" : true,  "class": "text-left",
    "render":function(valor){
      return utilitario.formatearValorEstadoDescarga(valor);
    }
  });

  moduloActual.inicializarCampos= function(){
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
    //campos de formulario principal
    this.clienteSeleccionado= $("#clienteSeleccionado");
    this.operacionSeleccionada= $("#operacionSeleccionada");
    this.fechaPlanificadaSeleccionada= $("#fechaPlanificadaSeleccionada");
    this.estadoSeleccionado= $("#estadoSeleccionado");
    //

    //campos de formulario autorizacion
    this.idAutorizacion =$("#idAutorizacion");
    this.idAutorizador =$("#idAutorizador");
    this.vigenteDesde =$("#vigenteDesde");
    this.vigenteHasta =$("#vigenteHasta");
    this.codigoAutorizacion =$("#codigoAutorizacion");

    this.obj.cmpDescAutorizacion=$("#cmpDescAutorizacion");
    this.obj.cmpAprobador=$("#cmpAprobador");
    this.obj.cmpCodigoValidacion=$("#cmpCodigoValidacion");
    this.obj.cmpVigenciaHastaValidacion=$("#cmpVigenciaHastaValidacion");
    this.obj.cmpVigenciaHastaValidacion.inputmask(constantes.FORMATO_FECHA, 
    { 
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpJustificacion=$("#cmpJustificacion");

    
  };  
  
  moduloActual.grillaDespuesSeleccionar= function(indice){
    var referenciaModulo=this;
    moduloActual.clienteSeleccionado.text(referenciaModulo.obj.datClienteApi.cell(indice,8).data());
    moduloActual.operacionSeleccionada.text(referenciaModulo.obj.datClienteApi.cell(indice,7).data());
    moduloActual.fechaPlanificadaSeleccionada.text(utilitario.formatearFecha(referenciaModulo.obj.datClienteApi.cell(indice,2).data()));
    moduloActual.estadoSeleccionado.text( utilitario.formatearEstadoDiaOperativo(referenciaModulo.obj.datClienteApi.cell(indice,9).data()));
    moduloActual.idEstadoSeleccionado =referenciaModulo.obj.datClienteApi.cell(indice,9).data();
  };

//-----------------------FORMULARIO INGRESO DE AUTORIZACION --------------------------
  moduloActual.recuperarAutorizacionesXcodigoInterno = function(){
	  //DATO QUE DEBE RECIBIR DEL MODULO CORRESPONDIENTE
	  var codInterno = 'REP';

	  var referenciaModulo = this;
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		console.log("recuperarAutorizacionesXcodigoInterno");
		$.ajax({
		    type: "GET",
		    url: "./autorizacion/recuperarPorCodigoInterno",
		    contentType: "application/json",
		    data: {codigoInterno:codInterno},	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    	} 	else {		 
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		moduloActual.llenarValidarAutorizacion(respuesta.contenido.carga[0]);
	    		}
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    }
		});
  };

  moduloActual.llenarValidarAutorizacion = function(registro){
	  moduloActual.idAutorizacion = registro.autorizacion[0].id;
	  moduloActual.obj.cmpDescAutorizacion.val(registro.autorizacion[0].nombre);
    
	  console.log("Entra en llenarValidarAutorizacion");
	  console.log(moduloActual.idAutorizacion);
	  //aqui hay que llenar el select2
	  //try{
		  $("#cmpAprobador").select2({
	    	  ajax: {
	    		    url: "./autorizacion/recuperarPorAutorizacion",
	    		    dataType: 'json',
	    		    delay: 250,
	    		    data: function (parametros) {
	    		      return {
	    		    	autorizacion: moduloActual.idAutorizacion,
	    		      };
	    		    },
	    		    processResults: function (respuesta, pagina) {
	    		    	var resultados = respuesta.contenido.carga;
	    		    	return { results: resultados};
	    		    },
	    		    cache: true
	    		  },
	    		language: "es",
	    		escapeMarkup: function (markup) {
	    			return markup; 
	    			},
	    		templateResult: function (registro) {
	    			if (registro.loading) {
	    				return "Buscando...";
	    			}
	  		        return "<div class='select2-user-result'>" + registro.identidad + "</div>";
	  		    },
	  		    templateSelection: function (registro) {
		  	    		moduloActual.vigenteDesde = registro.vigenteDesde;
		  	    		moduloActual.idAutorizador= registro.idUsuario;
		  	    		moduloActual.vigenteHasta = registro.vigenteHasta;
		  	    		moduloActual.codigoAutorizacion = registro.codigoAutorizacion;
		  	    	//	moduloActual.obj.cmpVigenciaHastaValidacion.val(utilitario.formatearFecha(registro.vigenteHasta));
		  	    	if(registro.vigenteHasta){	
		  	    		var fecha = registro.vigenteHasta;
		  	    			var parametros = fecha.split('-');
		  	    			var nuevaFecha =  new String(parametros[2]+ '/' + parametros[1] + '/' + parametros[0]);
		  	    			moduloActual.obj.cmpVigenciaHastaValidacion.val(nuevaFecha);
		  	    	}
		  	    	else{
		  	    		moduloActual.obj.cmpVigenciaHastaValidacion.val("");
		  	    	}
		  	        return registro.identidad || registro.text;
		  	    },
	      });
	  
	//  }  catch(error){
	//	  console.log(error.message);
	 // }
  };

  moduloActual.recuperarValoresValidacionAutorizacion = function(registro){
	var referenciaModulo = this;
	var eRegistro = {};
	try {
		eRegistro.idAutorizacion = parseInt(moduloActual.idAutorizacion);
		eRegistro.idAutorizador = parseInt(moduloActual.idAutorizador);
		eRegistro.vigenteDesde 	= new Date(moduloActual.vigenteDesde);
		eRegistro.vigenteHasta 	= utilitario.formatearStringToDate(moduloActual.obj.cmpVigenciaHastaValidacion.val());
		eRegistro.descripcion 	= moduloActual.obj.cmpJustificacion.val();
		eRegistro.idRegistro 	= parseInt(referenciaModulo.idRegistro);
		eRegistro.tipoRegistro 	= parseInt(constantes.TIPO_REGISTRO_CIERRE);
	}  catch(error){
	  console.log(error.message);
	}
	return eRegistro;
  };		 
  moduloActual.guardarValidacionAutorizacion= function(){
	console.log("entra en guardarValidacionAutorizacion");
	var referenciaModulo = this;
	var caracteres = referenciaModulo.obj.cmpJustificacion.val().length;
	if((caracteres < 60) || (caracteres > 3000)) {
		referenciaModulo.obj.mensajeError.text(cadenas.MENSAJE_ERROR_POR_LONGITUD);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.MENSAJE_ERROR_POR_LONGITUD);
		referenciaModulo.obj.cntTabla.show();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.frmValidarAutorizacion.modal("hide");
	    referenciaModulo.obj.frmAlertaValidacion.modal("show");
	    referenciaModulo.obj.ocultaContenedorTabla.show();
	} else {
		if(moduloActual.codigoAutorizacion != referenciaModulo.obj.cmpCodigoValidacion.val()){
			referenciaModulo.obj.mensajeError.text(cadenas.MENSAJE_ERROR_CODIGO_AUTORIZACION);
			//referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El código de autorización es incorrecto.");
			referenciaModulo.obj.cntTabla.show();
		    referenciaModulo.obj.cntFormulario.hide();
		    referenciaModulo.obj.frmValidarAutorizacion.modal("hide");
		    referenciaModulo.obj.frmAlertaValidacion.modal("show");
		    referenciaModulo.obj.ocultaContenedorTabla.show();
		}
		else if (referenciaModulo.obj.frmPrincipal.valid()){
			var eRegistro = referenciaModulo.recuperarValoresValidacionAutorizacion();
			$.ajax({
			    type: "POST",
			    url: "./autorizacion/crearAutorizacionEjecutada",
			    contentType: "application/json", 
			    data: JSON.stringify(eRegistro),	
			    success: function(respuesta) {
			    	if (!respuesta.estado) {
			    		referenciaModulo.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_ERROR );
			    		//referenciaModulo.obj.actualizarBandaInformacion.text("Hubo un error en la petici&oacute;n.");
			    		referenciaModulo.obj.frmValidarAutorizacion.modal("show");
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);			    		
			    	} 	else {		    		
			    		referenciaModulo.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_EXITO );
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "El registro se ha guardado correctamente.");
			    		referenciaModulo.obj.cntTabla.hide();
			    		referenciaModulo.obj.ocultaContenedorTabla.hide();
			    		referenciaModulo.obj.frmValidarAutorizacion.modal("hide");
			    		referenciaModulo.obj.cntFormulario.show();
			    		referenciaModulo.obj.ocultaContenedorFormulario.hide();
		    		}
			    },			    		    
			    error: function() {
			    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
			    }
			});
		}
	}
  };	
	
  moduloActual.inicializar();
});