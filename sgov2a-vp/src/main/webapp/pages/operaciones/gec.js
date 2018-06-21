$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='guia-combustible';
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_LISTAR_GUIAS = moduloActual.urlBase + '/listar-guias';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_RECUPERAR_NUMERO_GEC="guia-combustible" + '/recuperar-numero-gec';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  //moduloActual.URL_GUARDAR = moduloActual.urlBase + 'guia-combustible/crear';
  moduloActual.URL_GUARDAR =  'guia-combustible/crear';
  moduloActual.URL_GUARDAR_APROBACION =  'guia-combustible/aprobar-gec';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_EMITIR = moduloActual.urlBase + '/emitir';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'numeroGuia'});
  moduloActual.columnasGrilla.push({ "data": 'fechaGuiaCombustible'});
  moduloActual.columnasGrilla.push({ "data": 'nombreOperacion'});
  moduloActual.columnasGrilla.push({ "data": 'totalVolumenDespachado'});
  moduloActual.columnasGrilla.push({ "data": 'totalVolumenRecibido'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": false, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": false, "visible":true  });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": false, "visible":true , "render" : utilitario.formatearFecha, "className": "text-center"});
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": false, "visible":true  });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": false, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": false, "orderable": false, "visible":true, "render": utilitario.formatearEstadoGuia });
  moduloActual.detalleRecuperado=[];
  moduloActual.reglasValidacionFormulario={
	cmpRazonSocial: { required: true, maxlength: 150 },
	cmpNombreCorto: { required: true, maxlength: 20   },
	cmpRuc: 	{ required: true, rangelength: [11, 11], number: true },
	cmpNumContrato: { required: true  },
	cmpDesContrato: { required: true  }
  };

  
  moduloActual.mensajesValidacionFormulario={
	cmpRazonSocial: {
		required: "El campo es obligatorio",
		maxlength: "El campo Razón Social debe contener 150 caracteres como m&aacute;ximo."
	},
	cmpNombreCorto: {
		required: "El campo es obligatorio",
		maxlength: "El campo Nombre Corto debe contener 20 caracteres como m&aacute;ximo."
	},
	cmpRuc: {
		required: "El campo es obligatorio",
		rangelength: "El campo RUC debe contener 11 caracteres",
		number: "El campo RUC solo debe contener caracteres num&eacute;ricos"
	},
	cmpNumContrato: {
		required: "El campo es obligatorio"
	},
	cmpDesContrato: {
		required: "El campo es obligatorio"
	}
  };
  
  
  moduloActual.validarFormulario = function(){
	  var respuesta = {estado:true,mensaje:"Los campos han sido validados con exito"};
	  var ref=this;
	  var numeroElementos = $('#tablaDetalleGec input:checked').length;
	  /*if (ref.obj.cmpOrdenCompra.val().length < 3){
		  respuesta.estado=false;
		  respuesta.mensaje="No ha ingresado la orden de compra";
		  return respuesta;
	  }*/
	  
	  if (ref.obj.cmpSelect2Transportista.val()<1){
		  respuesta.estado=false;
		  respuesta.mensaje="No ha seleccionado el transportista";
		  return respuesta;
	  }
	  if (ref.obj.cmpSelect2Producto.val()<1){
		  respuesta.estado=false;
		  respuesta.mensaje="No ha seleccionado el producto";
		  return respuesta;
	  }

	  if (ref.obj.cmpFiltroFechaGuia.val().length !=10){
		  respuesta.estado=false;
		  respuesta.mensaje="No ha seleccionado la fecha";
		  return respuesta;
	  }
	  if (numeroElementos<1){
		  respuesta.estado=false;
		  respuesta.mensaje="No ha seleccionado ninguna guia de remision";
		  return respuesta;
	  }	  
	  return respuesta;
  };
  
  moduloActual.validarObservacion = function(){
	  var respuesta = {estado:true,mensaje:"Los campos han sido validados con exito"};
	  var ref=this;	  
	  if (ref.obj.aprobacionObservacionCliente.val().length <21){
		  respuesta.estado=false;
		  respuesta.mensaje="Favor, verificar la observación.";
		  return respuesta;
	  }
	  return respuesta;
  };
  
  moduloActual.guardarRegistro= function(){
		var referenciaModulo = this;
		try {
			var respuestaLocal = moduloActual.validarFormulario();
			if (respuestaLocal.estado){
		    referenciaModulo.obj.ocultaContenedorFormulario.show();
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
				var eRegistro = referenciaModulo.recuperarValores();
				$.ajax({
		      type: constantes.PETICION_TIPO_POST,
		      url: referenciaModulo.URL_GUARDAR, 
		      contentType: referenciaModulo.TIPO_CONTENIDO, 
		      data: JSON.stringify(eRegistro),	
		      success: function(respuesta) {
		        if (!respuesta.estado) {
		          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		        } else {
		          referenciaModulo.iniciarListado(respuesta.mensaje);
		        }
		        referenciaModulo.obj.ocultaContenedorFormulario.hide();
		      },			    		    
		      error: function() {
		        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		      }
				});
			} else {
				referenciaModulo.obj.ocultaContenedorFormulario.hide();
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuestaLocal.mensaje);
			}
		} catch(error){
		    referenciaModulo.mostrarDepuracion(error.message);
		};
	};
	
	moduloBase.prototype.guardarAprobacion= function(){
		var referenciaModulo = this;
		var eRegistro = {};
		try {
			//var respuestaLocal = moduloActual.validarAprobacion();//crear validarAprobacion()
			//if (respuestaLocal.estado){ 
		    referenciaModulo.obj.ocultaContenedorAprobacion.show();
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
				
				if(referenciaModulo.obj.aprobacionCliente=="APROBADO"){
					eRegistro = referenciaModulo.recuperarAprobacion(); //avanzar validarAprobacion()
				} else if(referenciaModulo.obj.aprobacionCliente=="OBSERVADO"){
					eRegistro = referenciaModulo.recuperarObservacion(); //avanzar validarAprobacion()
				}
				
				
				$.ajax({
		      type: constantes.PETICION_TIPO_POST,
		      url: referenciaModulo.URL_GUARDAR_APROBACION, //"./guia-combustible/aprobar-gec", 
		      contentType: referenciaModulo.TIPO_CONTENIDO, 
		      data: JSON.stringify(eRegistro),	
		      success: function(respuesta) {  
		        if (!respuesta.estado) {
		          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		        } else {
		          referenciaModulo.iniciarListado(respuesta.mensaje);
		          referenciaModulo.obj.cntAprobacionGEC.hide();	
		          referenciaModulo.obj.ocultaContenedorAprobacion.hide();
		          //referenciaModulo.obj.cntFormulario.hide();
			      //referenciaModulo.obj.cntTabla.show();
		        }
		        referenciaModulo.obj.ocultaContenedorAprobacion.hide();
		      },			    		    
		      error: function() {
		        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		      }
				});
			//} 
			//else {
		    //referenciaModulo.obj.ocultaContenedorFormulario.hide();
		    //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuestaLocal.mensaje);
			//}
		} catch(error){
		    referenciaModulo.mostrarDepuracion(error.message);
		};
	};
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	var ref=this;
	
	ref.obj.cmpClienteOperacion=$("#cmpClienteOperacion");
	ref.obj.cmpNumeroGuia=$("#cmpNumeroGuia");
	ref.obj.cmpNumeroContrato=$("#cmpNumeroContrato");
	ref.obj.cmpDescripcionContrato=$("#cmpDescripcionContrato");
	ref.obj.cmpOrdenCompra=$("#cmpOrdenCompra");
	ref.obj.cmpIdTransportista=$("#cmpIdTransportista");
	ref.obj.cmpComentarios=$("#cmpComentarios");
	ref.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
	//
	ref.obj.plantillaDetalleGec= $("#plantillaDetalleGec");
	ref.obj.plantillaResumenDetalleGec= $("#plantillaResumenDetalleGec");	
	ref.obj.tablaDetalleGec= $("#tablaDetalleGec");  
	//
	ref.obj.tablaVistaDetalle=$("#tablaVistaDetalle");
	ref.obj.vistaDetalleGec=$("#vistaDetalleGec");
	ref.obj.vistaResumenDetalleGec=$("#vistaResumenDetalleGec");
	//
	ref.obj.aprobacionDetalleGec= $("#aprobacionDetalleGec");
	ref.obj.aprobacionResumenDetalleGec=$("#aprobacionResumenDetalleGec");
	ref.obj.tablaAprobacionDetalle=$("#tablaAprobacionDetalle");
	ref.obj.ocultaContenedorAprobacion=$("#ocultaContenedorAprobacion");	
	ref.obj.cntAprobacionGEC=$("#cntAprobacionGEC");
	ref.obj.aprobacionCliente=$("#aprobacionCliente");
	//Botones
	ref.obj.btnRecuperarGuiasRemision= $("#btnRecuperarGuiasRemision");
	ref.obj.btnRecuperarGuiasRemision.on(ref.NOMBRE_EVENTO_CLICK,function(){		
		ref.listarGuiasRemisionDisponibles();		
	});	
	
	ref.obj.ocultaContenedorAprobacion=$("#ocultaContenedorAprobacion");
	ref.obj.frmAprobacion=$("#frmAprobacion");
	ref.obj.btnAgregarGEC=$("#btnAgregarGEC");
	ref.obj.btnModificarGEC=$("#btnModificarGEC");
	ref.obj.btnVerGEC=$("#btnVerGEC");
	ref.obj.btnEmitir=$("#btnEmitir");
	ref.obj.btnAprobar=$("#btnAprobar");	
	ref.obj.btnAprobarAprobacion=$("#btnAprobarAprobacion");	
	ref.obj.btnObservarAprobacion=$("#btnObservarAprobacion");	
	ref.obj.btnCancelarAprobacion=$("#btnCancelarAprobacion");	
	ref.obj.btnNotificar=$("#btnNotificar");
	ref.obj.btnImprimir=$("#btnImprimir");
	ref.obj.btnConfirmarModificarEstado=$("#btnConfirmarModificarEstado");
	ref.obj.btnConfirmarModificarEstado.on(ref.NOMBRE_EVENTO_CLICK,function(){
	ref.actualizarEstadoRegistro();
		  });
	
    this.obj.cmpSelect2Transportista=$("#cmpIdTransportista").select2({
    	  ajax: {
    		    url: "./transportista/listar",
    		    dataType: 'json',
    		    delay: 250,
    		    data: function (parametros) {
    		      return {
    		    	valorBuscado: parametros.term, // search term
    		        page: parametros.page,
    		        paginacion:0,
    		        idOperacion: $('#cmpFiltroOperacion option:selected').val()
    		      };
    		    },
    		    processResults: function (respuesta, pagina) {
    		    	var resultados= respuesta.contenido.carga;
    		    	//console.log(resultados);
    		      return { results: resultados};
    		    },
    		    cache: true
    		  },
    		language: "es",
    		escapeMarkup: function (markup) { return markup; },
    		templateResult: function (registro) {
    			if (registro.loading) {
    				return registro.text;
    			}		    	
  		        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		        return registro.razonSocial || registro.text;
  		    },
    		//minimumInputLength: 3
      });
	
	
	ref.obj.cmpProducto=$("#cmpProducto");
	ref.obj.cmpSelect2Producto=ref.obj.cmpProducto.select2({
  	  ajax: {
  		    url: "./producto/listarPorOperacion",
  		    dataType: 'json',
  		    delay: 250,
  		    "data": function (parametros) {
  		    	try{
  		    	  var referenciaModulo=this;
  			      return {
  			    	filtroOperacion : moduloActual.obj.cmpClienteOperacion.attr("data-id-operacion"),
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

	ref.obj.cmpFiltroFechaGuia=$("#cmpFiltroFechaGuia");
	ref.obj.cmpFiltroFechaGuia.daterangepicker({
        singleDatePicker: true,        
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
	
	
	ref.obj.cmpFiltroOperacion=$("#cmpFiltroOperacion");
    ref.obj.cmpFiltroOperacion.select2();
	ref.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
	ref.obj.filtroIdRolUsuario = $("#filtroIdRolUsuario");
	ref.obj.filtroRolUsuario = $("#filtroRolUsuario");
	var fechaActual = ref.obj.filtroFechaPlanificada.attr('data-fecha-actual'); 
	//var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
	 var rangoSemana = utilitario.retornarfechaInicialFinal(fechaActual);
	ref.obj.filtroFechaPlanificada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
	ref.obj.filtroFechaPlanificada.daterangepicker({
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
    this.obj.vistaClienteOperacion=$("#vistaClienteOperacion");
    this.obj.vistaNumeroGuia=$("#vistaNumeroGuia");
    this.obj.vistaNumeroContrato=$("#vistaNumeroContrato");
    this.obj.vistaDescripcionContrato=$("#vistaDescripcionContrato");
    this.obj.vistaOrdenCompra=$("#vistaOrdenCompra");
    this.obj.vistaTransportista=$("#vistaTransportista");    
    this.obj.vistaProducto=$("#vistaProducto");
    this.obj.vistaFechaGuia=$("#vistaFechaGuia");
    this.obj.vistaComentarios=$("#vistaComentarios");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");	
  //Campos de aprobacion
    this.obj.aprobacionId=$("#aprobacionId");
    this.obj.aprobacionIdAprobacion=$("#aprobacionIdAprobacion");
    this.obj.aprobacionClienteOperacion=$("#aprobacionClienteOperacion");
    this.obj.aprobacionNumeroGuia=$("#aprobacionNumeroGuia");
    this.obj.aprobacionOrdenCompra=$("#aprobacionOrdenCompra");
    this.obj.aprobacionTransportista=$("#aprobacionTransportista");    
    this.obj.aprobacionProducto=$("#aprobacionProducto");
    this.obj.aprobacionFechaGuia=$("#aprobacionFechaGuia");   
    this.obj.aprobacionEstado=$("#aprobacionEstado");
    this.obj.aprobacionRegistradoEl=$("#aprobacionRegistradoEl");
    this.obj.aprobacionRegistradoPor=$("#aprobacionRegistradoPor");
    this.obj.aprobacionEmitidoEl=$("#aprobacionEmitidoEl");
    this.obj.aprobacionEmitidoPor=$("#aprobacionEmitidoPor");	
    this.obj.aprobacionObservacionCliente=$("#aprobacionObservacionCliente");
    //para notificar
    this.obj.btnNotificar=$("#btnNotificar");
    this.obj.btnEnviarCorreo=$("#btnEnviarCorreo");
    this.obj.frmNotificar=$("#frmNotificar");
    this.obj.frmConfirmarNotificar=$("#frmConfirmarNotificar");
    this.obj.frmCorreo=$("#frmCorreo");
    this.obj.cmpPara=$("#cmpPara");
    this.obj.cmpCC=$("#cmpCC");
    this.obj.cmpAsunto=$("#cmpAsunto");
   // this.obj.cmpNumeroGuia=$("#cmpNumeroGuia");
    this.obj.cmpEstadoGuia=$("#cmpEstadoGuia");
    
   /* if(ref.obj.filtroIdRolUsuario.val()==6){
    	ref.obj.btnAgregarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);	
    	ref.obj.btnModificarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);	
	}else{
		ref.obj.btnAgregarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);	
		ref.obj.btnModificarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);	
	} */
    ref.obj.btnAgregarGEC.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'CREAR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
  	  	//ref.iniciarAgregar();
    });
    
    ref.obj.btnModificarGEC.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'ACTUALIZAR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
  	  	//ref.iniciarModificar();
	});
    
    ref.obj.btnVerGEC.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'RECUPERAR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
    	//ref.iniciarVer();		
    });
    
	ref.obj.btnEmitir.on(ref.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'EMITIR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
		//ref.solicitarEmitirGec();
	});
	
	ref.obj.btnAprobar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'APROBAR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
		//ref.solicitarEmitirGec();
	});

	ref.obj.btnImprimir.on(ref.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'REPORTAR_GUIA_COMBUSTIBLE';
		moduloActual.validaPermisos();
		//ref.imprimirGec();
	});	
	
	ref.obj.btnNotificar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'NOTIFICAR_GUIA_COMBUSTIBLE';
    	moduloActual.validaPermisos();
		//ref.solicitarEmitirGec();
	});
	
	ref.obj.btnEnviarCorreo.on(ref.NOMBRE_EVENTO_CLICK,function(){
		  //referenciaModulo.enviarCorreo();
		  ref.enviarCorreo();
	  });
	
	ref.obj.btnCancelarAprobacion.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.iniciarCancelarAprobacion();
	});
	
	ref.obj.btnAprobarAprobacion.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.obj.aprobacionCliente="APROBADO";
		ref.guardarAprobacion();
	});
	
	ref.obj.btnObservarAprobacion.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.obj.aprobacionCliente="OBSERVADO";
		ref.guardarAprobacion();
	});
  };

  moduloActual.validaPermisos= function(){
  var referenciaModulo = this;
  try{
  //console.log("Validando permiso para: " + referenciaModulo.descripcionPermiso);
  referenciaModulo.obj.ocultaContenedorTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.VERIFICANDO_PERMISOS);
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: './validaPermisos/validar',
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: { permiso : referenciaModulo.descripcionPermiso, },	
    success: function(respuesta) {
      //console.log("respuesta.estado " + respuesta.estado);
      if(!respuesta.estado){
    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
    	  referenciaModulo.obj.ocultaContenedorTabla.hide();
      } else {
	      if (referenciaModulo.descripcionPermiso == 'CREAR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  moduloActual.iniciarAgregar();
	      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  referenciaModulo.iniciarModificar();
	      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  referenciaModulo.iniciarVer();
	      } else if (referenciaModulo.descripcionPermiso == 'EMITIR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  referenciaModulo.solicitarEmitirGec();
	    	  //referenciaModulo.actualizarEstadoRegistro();
	      } else if (referenciaModulo.descripcionPermiso == 'REPORTAR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  referenciaModulo.imprimirGec();
	      } else if (referenciaModulo.descripcionPermiso == 'APROBAR_GUIA_COMBUSTIBLE'){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  //console.log("APROBAR_GUIA_COMBUSTIBLE");
	    	  referenciaModulo.modoEdicion=constantes.MODO_APROBAR_GEC;
	    	  referenciaModulo.iniciarAprobacion();
	      } else if (referenciaModulo.descripcionPermiso == 'NOTIFICAR_GUIA_COMBUSTIBLE'){
	    	  //console.log("NOTIFICAR_GUIA_COMBUSTIBLE en gec.js");
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);	
	    	  referenciaModulo.abrirVentanaCorreo();	     
	      } 
	  }
	     referenciaModulo.obj.ocultaContenedorTabla.hide();
	  },
	    error: function() {
	    	referenciaModulo.obj.ocultaContenedorTabla.hide();
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	  }
	});
  } catch(error){
	  referenciaModulo.obj.ocultaContenedorTabla.hide();
	  referenciaModulo.mostrarDepuracion(error.message);
  };
};
	
  moduloActual.imprimirGec=function(){
		var ref = this;
		window.open("./guia-combustible/reporte-gec?idGuiaCombustible="+ref.idRegistro+"&formato=pdf");  
  };
  
  moduloActual.grillaDespuesSeleccionar= function(indice){
	var referenciaModulo=this;
	var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,7).data();
	referenciaModulo.estadoRegistro=estadoRegistro;	 
	//referenciaModulo.obj.cmpNumeroGuia = referenciaModulo.obj.datClienteApi.cell(indice,2).data();
	referenciaModulo.obj.cmpEstadoGuia = referenciaModulo.obj.datClienteApi.cell(indice,7).data();	 
  };
  moduloActual.listarGuiasRemisionDisponibles= function(){  
	 // console.log("listarGuiasRemisionDisponibles");
	  var ref= this;
	  var valorIdTransportista = ref.obj.cmpIdTransportista.val();
	  var valorIdProducto = ref.obj.cmpProducto.val();
	  var valorFecha = ref.obj.cmpFiltroFechaGuia.val();
	  if(valorFecha.length==0){
		  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Ingrese Fecha.");
	  }else{
		  try {
			  $.ajax({
				    type: constantes.PETICION_TIPO_GET,
				    url: ref.URL_LISTAR_GUIAS, 
				    contentType: ref.TIPO_CONTENIDO, 
				    data: {	idTransportista:valorIdTransportista,
				    		filtroProducto:valorIdProducto,
				    		filtroFechaDiaOperativo:valorFecha,
				    		filtroTipoConsulta:"agregar"
				    	  },	
				    success: function(respuesta) {
				      if (!respuesta.estado) {
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				      } else {  				    			    		
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
				    	  if(ref.descripcionPermiso == 'CREAR_GUIA_COMBUSTIBLE'){
				    		  ref.pintarDetalle(respuesta.contenido.carga);
				    	  }else{				    		  
				    		  ref.pintarDetalleModicar(respuesta.contenido.carga);
				    	  }
				    	  
				      }
				    },			    		    
				    error: function() {
				    	ref.mostrarErrorServidor(xhr,estado,error); 
				    }
			  });
		  }catch(error){
			  console.log(error);
		  } 
	  }

  };

  
  moduloActual.calcularTotal= function(){
	  var totalRecibido=0;
	    $('#tablaDetalleGec input:checked').each(function() {
	    	var fila = $(this).attr('data-fila');	        
	    	totalRecibido=totalRecibido +parseFloat($("#cmpVolumenRecibido"+fila).text()) ;
	    });
	    $("#cmpVolumenTotalRecibido").text(totalRecibido.toFixed(2));
  };
  
  moduloActual.pintarDetalleModicar=function(registros){
	  var ref=this;
	  var numeroRegistros = registros.length;
	  var registro = null;
	  var clonDetalle=null;
	  var registrosBD=ref.detalleRecuperado;
	  var numeroRegBD = registrosBD.length;
	  var totalRecibido = 0;
	  try {
		  //recupera de base datos
		  var index=0;
		  $("#tablaDetalleGec > tbody:first").children().remove();
		  for(var contador=0;contador<numeroRegBD;contador++){			  
			  registro = registrosBD[contador];
			  clonDetalle = ref.obj.plantillaDetalleGec.clone();
			  clonDetalle.attr('id', "plantillaDetalleGec" + index);
			  var selector=clonDetalle.find("#cmpSelectorDetalle");
			  selector.attr('data-fila', index);
			  selector.prop('checked', true);
			  selector.change(function() {
				  //console.log("click");
			        //if($(this).is(":checked")) {
			        	//console.log("marcado");			        	
			        //}
			        ref.calcularTotal();
			  });
			  selector.attr('id', "cmpSelectorDetalle" + index);
			  
			  var cmpDetalleNumeroGuia=clonDetalle.find("#cmpDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuia);
			  cmpDetalleNumeroGuia.attr('id', "cmpDetalleNumeroGuia" + index);			  
			  
			  var cmpFechaEmision=clonDetalle.find("#cmpFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "cmpFechaEmision" + index);			 
			  
			  var cmpFechaRecepcion=clonDetalle.find("#cmpFechaRecepcion");
			  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaRecepcion));
			  cmpFechaRecepcion.attr('id', "cmpFechaRecepcion" + index);			  
			  
			  var cmpVolumenDespachado=clonDetalle.find("#cmpVolumenDespachado");
			  cmpVolumenDespachado.text(registro.volumenDespachado.toFixed(2));
			  cmpVolumenDespachado.attr('id', "cmpVolumenDespachado" + index);			  
			  
			  var cmpVolumenRecibido=clonDetalle.find("#cmpVolumenRecibido");
			  cmpVolumenRecibido.text(registro.volumenRecibido.toFixed(2));
			  cmpVolumenRecibido.attr('id', "cmpVolumenRecibido" + index);			  
			  totalRecibido=totalRecibido+parseFloat(registro.volumenRecibido);
			  
			  var cmpEstadoDiaOperativo=clonDetalle.find("#cmpEstadoDiaOperativo");
			  cmpEstadoDiaOperativo.text(registro.estado);
			  cmpEstadoDiaOperativo.attr('id', "cmpEstadoDiaOperativo" + index);		  
			  
			  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);
			  index++;
		  }
		  
		  
		  //$("#tablaDetalleGec > tbody:first").children().remove();
		  for(var contador=0;contador<numeroRegistros;contador++){
			  registro = registros[contador];
			  clonDetalle = ref.obj.plantillaDetalleGec.clone();
			  clonDetalle.attr('id', "plantillaDetalleGec" + index);
			  var selector=clonDetalle.find("#cmpSelectorDetalle");
			  selector.attr('data-fila', index);			  
			  selector.change(function() {
				  //console.log("click");
			        //if($(this).is(":checked")) {
			        	//console.log("marcado");			        	
			        //}
			        ref.calcularTotal();
			  });
			  selector.attr('id', "cmpSelectorDetalle" + index);			  
			  var cmpDetalleNumeroGuia=clonDetalle.find("#cmpDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuiaRemision);
			  cmpDetalleNumeroGuia.attr('id', "cmpDetalleNumeroGuia" + index);			  
			  
			  var cmpFechaEmision=clonDetalle.find("#cmpFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "cmpFechaEmision" + index);			 
			  
			  var cmpFechaRecepcion=clonDetalle.find("#cmpFechaRecepcion");
			  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaArribo));
			  cmpFechaRecepcion.attr('id', "cmpFechaRecepcion" + index);			  
			  
			  var cmpVolumenDespachado=clonDetalle.find("#cmpVolumenDespachado");
			  cmpVolumenDespachado.text(registro.volumenDespachadoCorregido.toFixed(2));
			  cmpVolumenDespachado.attr('id', "cmpVolumenDespachado" + index);			  
			  
			  var cmpVolumenRecibido=clonDetalle.find("#cmpVolumenRecibido");
			  cmpVolumenRecibido.text(registro.volumenRecibidoCorregido.toFixed(2));
			  cmpVolumenRecibido.attr('id', "cmpVolumenRecibido" + index);			  
			  
			  var cmpEstadoDiaOperativo=clonDetalle.find("#cmpEstadoDiaOperativo");
			  //console.log(registro.estadoDiaOperativo);
			  cmpEstadoDiaOperativo.text(utilitario.formatearEstadoDiaOperativo(registro.estadoDiaOperativo));
			  cmpEstadoDiaOperativo.attr('id', "cmpEstadoDiaOperativo" + index);		  
			  
			  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);
			  index++;
		  }
		  clonDetalle = ref.obj.plantillaResumenDetalleGec.clone();
		  //var totalRecibido=0;
		  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);
		  $("#cmpVolumenTotalRecibido").text(totalRecibido.toFixed(2));
	  }catch(error){
		  
	  }
  };
  
  
  moduloActual.pintarDetalle=function(registros){
	  var ref=this;
	  var numeroRegistros = registros.length;
	  var registro = null;
	  var clonDetalle=null;
	  try {
		  $("#tablaDetalleGec > tbody:first").children().remove();
		  for(var contador=0;contador<numeroRegistros;contador++){
			  registro = registros[contador];
			  clonDetalle = ref.obj.plantillaDetalleGec.clone();
			  clonDetalle.attr('id', "plantillaDetalleGec" + contador);
			  var selector=clonDetalle.find("#cmpSelectorDetalle");
			  selector.attr('data-fila', contador);			  
			  selector.change(function() {
				  //console.log("click");
			        //if($(this).is(":checked")) {
			        	//console.log("marcado");			        	
			        //}
			        ref.calcularTotal();
			  });
			  selector.attr('id', "cmpSelectorDetalle" + contador);			  
			  var cmpDetalleNumeroGuia=clonDetalle.find("#cmpDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuiaRemision);
			  cmpDetalleNumeroGuia.attr('id', "cmpDetalleNumeroGuia" + contador);			  
			  
			  var cmpFechaEmision=clonDetalle.find("#cmpFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "cmpFechaEmision" + contador);			 
			  
			  var cmpFechaRecepcion=clonDetalle.find("#cmpFechaRecepcion");
			  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaArribo));
			  cmpFechaRecepcion.attr('id', "cmpFechaRecepcion" + contador);			  
			  
			  var cmpVolumenDespachado=clonDetalle.find("#cmpVolumenDespachado");
			  cmpVolumenDespachado.text(registro.volumenDespachadoCorregido.toFixed(2));
			  cmpVolumenDespachado.attr('id', "cmpVolumenDespachado" + contador);			  
			  
			  var cmpVolumenRecibido=clonDetalle.find("#cmpVolumenRecibido");
			  cmpVolumenRecibido.text(registro.volumenRecibidoCorregido.toFixed(2));
			  cmpVolumenRecibido.attr('id', "cmpVolumenRecibido" + contador);			  
			  
			  var cmpEstadoDiaOperativo=clonDetalle.find("#cmpEstadoDiaOperativo");
			  //console.log(registro.estadoDiaOperativo);
			  cmpEstadoDiaOperativo.text(utilitario.formatearEstadoDiaOperativo(registro.estadoDiaOperativo));
			  cmpEstadoDiaOperativo.attr('id', "cmpEstadoDiaOperativo" + contador);		  
			  
			  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);			  
		  }
		  clonDetalle = ref.obj.plantillaResumenDetalleGec.clone();
		  var totalRecibido=0;
		  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);
		  $("#cmpVolumenTotalRecibido").text(totalRecibido.toFixed(2));
	  }catch(error){
		  
	  }
  };
  
  moduloActual.pintarDetalleRecuperado=function(registros){	  
	  //console.log("pintarDetalleRecuperado");
	  var ref=this;
	  ref.detalleRecuperado=registros;//alamacenar array tempo
	  var numeroRegistros = registros.length;
	  var registro = null;
	  var clonDetalle=null;
	  var totalRecibido = 0;
	  try {		  
		  for(var contador=0;contador<numeroRegistros;contador++){
			  registro = registros[contador];
			  clonDetalle = ref.obj.plantillaDetalleGec.clone();
			  clonDetalle.attr('id', "plantillaDetalleGec" + contador);
			  var selector=clonDetalle.find("#cmpSelectorDetalle");
			  selector.attr('data-fila', contador);
			  selector.prop('checked', true);
			  selector.change(function() {
				  //console.log("click");
			        //if($(this).is(":checked")) {
			        //	console.log("marcado");			        	
			        //}
			        ref.calcularTotal();
			  });
			  selector.attr('id', "cmpSelectorDetalle" + contador);
			  
			  var cmpDetalleNumeroGuia=clonDetalle.find("#cmpDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuia);
			  cmpDetalleNumeroGuia.attr('id', "cmpDetalleNumeroGuia" + contador);			  
			  
			  var cmpFechaEmision=clonDetalle.find("#cmpFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "cmpFechaEmision" + contador);			 
			  
			  var cmpFechaRecepcion=clonDetalle.find("#cmpFechaRecepcion");
			  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaRecepcion));
			  cmpFechaRecepcion.attr('id', "cmpFechaRecepcion" + contador);			  
			  
			  var cmpVolumenDespachado=clonDetalle.find("#cmpVolumenDespachado");
			  cmpVolumenDespachado.text(registro.volumenDespachado.toFixed(2));
			  cmpVolumenDespachado.attr('id', "cmpVolumenDespachado" + contador);			  
			  
			  var cmpVolumenRecibido=clonDetalle.find("#cmpVolumenRecibido");
			  cmpVolumenRecibido.text(registro.volumenRecibido.toFixed(2));
			  cmpVolumenRecibido.attr('id', "cmpVolumenRecibido" + contador);			  
			  totalRecibido=totalRecibido+parseFloat(registro.volumenRecibido);
			  
			  var cmpEstadoDiaOperativo=clonDetalle.find("#cmpEstadoDiaOperativo");
			  cmpEstadoDiaOperativo.text(registro.estado);
			  cmpEstadoDiaOperativo.attr('id', "cmpEstadoDiaOperativo" + contador);		  
			  
			  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);			  
		  }
		  clonDetalle = ref.obj.plantillaResumenDetalleGec.clone();		 
		  $("#tablaDetalleGec > tbody:last-child").append(clonDetalle);	
		  $("#cmpVolumenTotalRecibido").text(totalRecibido.toFixed(2));
	  }catch(error){
		  console.log(error);
	  }
  };
  
  moduloActual.pintarDetalleVista=function(registros){
	  //console.log("pintarDetalleVista");
	  //console.log(registros);
	  var ref=this;
	  var numeroRegistros = registros.length;
	  var registro = null;
	  var clonDetalle=null;
	  var totalRecibido = 0;
	  try {		  
		  for(var contador=0;contador<numeroRegistros;contador++){
			  registro = registros[contador];
			  clonDetalle = ref.obj.vistaDetalleGec.clone();
			  clonDetalle.attr('id', "vistaDetalleGec" + contador);
			  
			  var cmpDetalleNumeroGuia=clonDetalle.find("#vistaDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuia);
			  cmpDetalleNumeroGuia.attr('id', "vistaDetalleNumeroGuia" + contador);			  
			  
			  var cmpFechaEmision=clonDetalle.find("#vistaFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "vistaFechaEmision" + contador);			 
			  
			  var cmpFechaRecepcion=clonDetalle.find("#vistaFechaRecepcion");
			  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaRecepcion));
			  cmpFechaRecepcion.attr('id', "vistaFechaRecepcion" + contador);			  
			  
			  var cmpVolumenDespachado=clonDetalle.find("#vistaVolumenDespachado");
			  cmpVolumenDespachado.text(registro.volumenDespachado.toFixed(2));
			  cmpVolumenDespachado.attr('id', "vistaVolumenDespachado" + contador);			  
			  
			  var cmpVolumenRecibido=clonDetalle.find("#vistaVolumenRecibido");
			  cmpVolumenRecibido.text(registro.volumenRecibido.toFixed(2));
			  cmpVolumenRecibido.attr('id', "vistaVolumenRecibido" + contador);			  
			  totalRecibido=totalRecibido+parseFloat(registro.volumenRecibido);
			  
			  var cmpEstadoDiaOperativo=clonDetalle.find("#vistaEstadoDiaOperativo");
			  cmpEstadoDiaOperativo.text(registro.estado);
			  cmpEstadoDiaOperativo.attr('id', "vistaEstadoDiaOperativo" + contador);		  
			  
			  $("#tablaVistaDetalle > tbody:last-child").append(clonDetalle);			  
		  }
		  clonDetalle = ref.obj.vistaResumenDetalleGec.clone();		 
		  $("#tablaVistaDetalle > tbody:last-child").append(clonDetalle);	
		  $("#vistaVolumenTotalRecibido").text(totalRecibido.toFixed(2));
	  }catch(error){
		  console.log(error);
	  }
  };
  
  
  
  moduloActual.actualizarEstadoRegistro= function(){
	  //console.log("actualizarEstadoRegistro..." );   
	  var eRegistro = {};
	  var referenciaModulo=this;
		referenciaModulo.obj.frmConfirmarModificarEstado.modal("hide");
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	  if (referenciaModulo.estadoRegistro==constantes.ESTADO_ACTIVO){
	    referenciaModulo.estadoRegistro=constantes.ESTADO_INACTIVO;
	  } else {
	    referenciaModulo.estadoRegistro=constantes.ESTADO_ACTIVO;
	  }

	  try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.estado = parseInt(referenciaModulo.estadoRegistro);
		  $.ajax({
		    type: constantes.PETICION_TIPO_POST,
		    url: referenciaModulo.URL_EMITIR, 
		    contentType: referenciaModulo.TIPO_CONTENIDO, 
		    data: JSON.stringify(eRegistro),	
		    success: function(respuesta) {
		      if (!respuesta.estado) {
		        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		      } else {		    				    			    		
		        referenciaModulo.listarRegistros();
		        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		        referenciaModulo.obj.cntFormulario.hide();	
		        referenciaModulo.obj.cntTabla.show();
		      }
		    },			    		    
		    error: function(xhr,estado,error) {
		      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		    }
		 });
	    
	  }  catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	  }


  };
  
  moduloActual.iniciarAgregar= function(){  
		var referenciaModulo=this;
		var idCliente = $('#cmpFiltroOperacion option:selected').attr('data-id-cliente');
		$("#tablaDetalleGec > tbody:first").children().remove();
		//console.log(idCliente);

		var elemento = constantes.PLANTILLA_OPCION_SELECTBOX;
		elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,"");
		elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR, "Seleccionar...");

		referenciaModulo.obj.cmpIdTransportista.empty().append(elemento).val("").trigger('change');
		referenciaModulo.obj.cmpProducto.empty().append(elemento).val("").trigger('change');
		$('#cmpFiltroFechaGuia').val();

		try {
			$.ajax({
			    type: constantes.PETICION_TIPO_GET,
			    url: referenciaModulo.URL_RECUPERAR_NUMERO_GEC, 
			    contentType: referenciaModulo.TIPO_CONTENIDO, 
			    data: {ID:idCliente},	
			    success: function(respuesta) {
			    	if (!respuesta.estado) {
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			    	} else {
			    		referenciaModulo.mostrarFormulario(respuesta.contenido.carga[0]);
		    		}
			    },			    		    
			    error: function(xhr,estado,error) {
			        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
			        if (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
			          referenciaModulo.obj.ocultaContenedorFormulario.hide();
			        } else if (referenciaModulo.modoEdicion == constantes.MODO_VER){
			          referenciaModulo.obj.ocultaContenedorVista.hide();
			        } 
			    }
			});	    
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		};
	};

	moduloActual.mostrarFormulario= function(registro){
		//console.log("entra en mostrarFormulario");
		var ref = this;
		try {
			ref.modoEdicion=constantes.MODO_NUEVO;
			ref.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
			ref.resetearFormulario();
			ref.obj.cntTabla.hide();
			ref.obj.cntVistaRegistro.hide();
			ref.obj.cntFormulario.show();
			ref.obj.ocultaContenedorFormulario.hide();
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
			var numeroGuiaGec=registro.numeroSerie+"-" + registro.numeroGEC;
			var ClienteOperacion = $('#cmpFiltroOperacion option:selected').text();
			var idCliente = $('#cmpFiltroOperacion option:selected').attr("data-id-cliente");
			var idOperacion = $('#cmpFiltroOperacion option:selected').val();
			ref.obj.cmpClienteOperacion.val(ClienteOperacion);
			ref.obj.cmpClienteOperacion.attr("data-id-cliente",idCliente);
			ref.obj.cmpClienteOperacion.attr("data-id-operacion",idOperacion);
			ref.obj.cmpNumeroGuia.val(numeroGuiaGec);
			ref.obj.cmpNumeroGuia.attr("data-numero-serie",registro.numeroSerie);
			ref.obj.cmpNumeroGuia.attr("data-numero-guia",registro.numeroGEC);
			ref.obj.cmpNumeroContrato.val(registro.numeroContrato);
			ref.obj.cmpDescripcionContrato.val(registro.descripcionContrato);
		} catch(error){
			
		}
	};

  moduloActual.llenarFormulario = function(registro){
	var ref = this;
	try {
		$("#tablaDetalleGec > tbody ").children().remove();
		var ClienteOperacion = $('#cmpFiltroOperacion option:selected').text();
		ref.idRegistro= registro.id;
		
		ref.obj.cmpClienteOperacion.val(ClienteOperacion);
		ref.obj.cmpClienteOperacion.attr("data-id-cliente",registro.idCliente);
		ref.obj.cmpClienteOperacion.attr("data-id-operacion",registro.idOperacion);
		
		ref.obj.cmpNumeroGuia.val(registro.numeroSerie+"-"+registro.numeroGEC);
		ref.obj.cmpNumeroGuia.attr("data-numero-serie",registro.numeroSerie);
		ref.obj.cmpNumeroGuia.attr("data-numero-guia",registro.numeroGEC);
		
		ref.obj.cmpNumeroContrato.val(registro.numeroContrato);		
		ref.obj.cmpDescripcionContrato.val(registro.descripcionContrato);		
		ref.obj.cmpOrdenCompra.val(registro.ordenCompra);
		ref.obj.cmpComentarios.val(registro.comentarios);
		ref.obj.cmpFiltroFechaGuia.val(utilitario.formatearFecha(registro.fechaGuiaCombustible));
		ref.obj.cmpProducto.children().remove();
		ref.obj.cmpProducto.append("<option></option>");
	    ref.obj.cmpProducto.append($('<option>', { 
	        value: registro.idProducto,
	        text : registro.nombreProducto
	    }));
	    ref.obj.cmpProducto.val(registro.idProducto).trigger('change');	    
		ref.obj.cmpIdTransportista.children().remove();
		ref.obj.cmpIdTransportista.append("<option></option>");
	    ref.obj.cmpIdTransportista.append($('<option>', { 
	        value: registro.idTransportista,
	        text : registro.nombreTransportista
	    }));
	    ref.obj.cmpIdTransportista.val(registro.idTransportista).trigger('change');
	    ref.pintarDetalleRecuperado(registro.detalle);
	} catch (error){
		console.log("error:"+error);
	}
  };
  
	moduloActual.resetearFormulario= function(){
		var referenciaModulo= this;
		referenciaModulo.obj.frmPrincipal[0].reset();
		referenciaModulo.obj.verificadorFormulario.resetForm();
		jQuery.each( this.obj, function( i, val ) {
			if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
				if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
					//console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
					referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
				}
			}
		});
	};
  
  moduloActual.inicializarFormularioPrincipal= function(){
	var referenciaModulo=this;
	referenciaModulo.obj.verificadorFormulario = referenciaModulo.obj.frmPrincipal.validate({
	rules: referenciaModulo.reglasValidacionFormulario,
	messages: referenciaModulo.mensajesValidacionFormulario,
	highlight: function(element, errorClass, validClass) {
      //$(element.form).find("label[for=" + element.id + "]").addClass(errorClass);
      $("#cnt" + $(element).attr("id")).removeClass(validClass).addClass(errorClass);
    },
    unhighlight: function(element, errorClass, validClass) {
      $("#cnt" + $(element).attr("id")).removeClass(errorClass).addClass(validClass);
      //$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
    },
    errorPlacement: function(error, element) {
      //console.log("errorPlacement");
      console.log(error);
      //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,mensaje);      
    },
    errorClass: "has-error",
    validClass: "has-success",
    showErrors: function(errorMap, errorList) {
      this.checkForm();
      //console.log("this.errorMap");
      //console.log(this.errorMap);
      //console.log("this.errorList");
      //console.log(this.errorList);
      this.defaultShowErrors();
      //console.log("this.errorList.length");
      //console.log(this.errorList.length);
      var numeroErrores = this.errorList.length;
      if (numeroErrores > 0) {
        var mensaje = numeroErrores == 1 ? 'Existe un campo con error.' : 'Existen ' + numeroErrores + ' campos con errores';
        for (var indice in this.errorMap){
          //console.log(indice);
          mensaje+=". " + this.errorMap[indice];
        }        
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,mensaje);
      } else {
        mensaje="Todos los campos son validos";
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,mensaje);
      }
    }
  });
};

	moduloActual.llenarDetalles = function(registro){
		var ref=this;
	    this.idRegistro= registro.id;
	    //var ClienteOperacion = $('#filtroOperacion option:selected').text();
	    var ClienteOperacion = registro.nombreCliente +'/'+registro.nombreOperacion;
	    //console.log($('#cmpFiltroOperacion option:selected').text());
	    this.obj.vistaId.text(registro.id);
	    this.obj.vistaClienteOperacion.text(ClienteOperacion);
	    //this.obj.vistaClienteOperacion.text(referenciaModulo.obj.cmpFiltroOperacion.val());
	    this.obj.vistaNumeroGuia.text(registro.numeroSerie+"-"+registro.numeroGEC);
	    this.obj.vistaNumeroContrato.text(registro.numeroContrato);
	    this.obj.vistaDescripcionContrato.text(registro.descripcionContrato);
	    this.obj.vistaOrdenCompra.text(registro.ordenCompra);
	    this.obj.vistaProducto.text(registro.nombreProducto);
	    this.obj.vistaTransportista.text(registro.nombreTransportista);
	    this.obj.vistaFechaGuia.text(utilitario.formatearFecha(registro.fechaGuiaCombustible));
	    this.obj.vistaComentarios.text(registro.comentarios);  
	    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
	    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
	    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
	    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
	    this.obj.vistaIPCreacion.text(registro.ipCreacion);
	    this.obj.vistaIPActualizacion.text(registro.ipActualizacion);
	    $("#tablaVistaDetalle > tbody ").children().remove();
	    ref.pintarDetalleVista(registro.detalle);
	    $('#listado_vista_aprobacionGec').html("");
	    if (registro.aprobacionGec != null){
		    var aprobacion = registro.aprobacionGec;
		    var fila = $('#listado_vista_aprobacionGec');
		    
		    g_tr = '<tbody><tr><td class="tabla-vista-titulo" style="width:10%;"> Registrado por: 				</td>' +
		    				  '<td class="text-left" style="width:23%;">' + aprobacion.registrador.identidad + '</td>' +
		    				  '<td class="tabla-vista-titulo" style="width:10%;"> Emitido por: 					</td>' +
		    				  '<td class="text-left" style="width:23%;">' + aprobacion.emisor.identidad + 	   '</td>' +
		    				  '<td class="tabla-vista-titulo" style="width:10%;"> Aprobado por: 				</td>' +
		    				  '<td class="text-left">' + aprobacion.aprobador.identidad +   '</td></tr>'; 
				
		    fila.append(g_tr);
		    
		    g_tr = '<tr><td class="tabla-vista-titulo" style="width:10%;" colspan="1"> Comentario: 				</td>' +
			  '<td class="text-left" style="width:100%;" colspan="5"><textarea style="resize: none; width:100%;" disabled="disabled" rows="6"> ' + aprobacion.observacionCliente + '</textarea></td></tr></tbody>'; 
		    
		    fila.append(g_tr);
	    }

	};
	
	//APROBACION GEC
	moduloBase.prototype.iniciarAprobacion= function(){
		  var referenciaModulo=this;
		  referenciaModulo.modoEdicion=constantes.MODO_VER;
		  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_GEC_APROBACION);		  
		  referenciaModulo.obj.cntTabla.hide();
		  referenciaModulo.obj.cntFormulario.hide();
		  referenciaModulo.obj.ocultaContenedorVista.hide();
		  referenciaModulo.obj.cntVistaRegistro.hide();
		  referenciaModulo.obj.ocultaContenedorAprobacion.show();
		  referenciaModulo.obj.cntAprobacionGEC.show();
		  referenciaModulo.recuperarRegistroAprobacion();
	};

	moduloBase.prototype.recuperarRegistroAprobacion= function(){
		var referenciaModulo = this;
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		$.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: referenciaModulo.URL_RECUPERAR, 
		    contentType: referenciaModulo.TIPO_CONTENIDO, 
		    data: {ID:referenciaModulo.idRegistro},	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    		referenciaModulo.obj.ocultaContenedorAprobacion.hide();
			        referenciaModulo.obj.cntAprobacionGEC.hide();
			        referenciaModulo.obj.cntTabla.show();
			        referenciaModulo.obj.ocultaContenedorTabla.hide(); 
		    	} else {		 
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		referenciaModulo.cargarAprobarGec(respuesta.contenido.carga[0]);
		    		referenciaModulo.obj.ocultaContenedorAprobacion.hide();
	    		}
		    },			    		    
		    error: function(xhr,estado,error) {
		    	console.log("error:"+error);
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
	        referenciaModulo.obj.ocultaContenedorAprobacion.hide();
	        referenciaModulo.obj.cntAprobacionGEC.hide();
	        referenciaModulo.obj.cntTabla.show();
	        referenciaModulo.obj.ocultaContenedorTabla.hide();  
	        
		    }
		});
	};
	
	moduloActual.cargarAprobarGec = function(registro){
		var ref=this;
		this.idRegistro= registro.id;
		var ClienteOperacion = registro.nombreCliente +'/'+registro.nombreOperacion;
		this.obj.aprobacionId.text(registro.id);
		this.obj.aprobacionClienteOperacion.text(ClienteOperacion);
		this.obj.aprobacionNumeroGuia.text(registro.numeroSerie+"-"+registro.numeroGEC);
		this.obj.aprobacionOrdenCompra.text(registro.ordenCompra); 
		this.obj.aprobacionProducto.text(registro.nombreProducto);
		this.obj.aprobacionTransportista.text(registro.nombreTransportista);
		this.obj.aprobacionFechaGuia.text(utilitario.formatearFecha(registro.fechaGuiaCombustible));
		this.obj.aprobacionRegistradoEl.text(utilitario.formatearTimestampToString(registro.aprobacionGec.fechaHoraRegistrado));
		this.obj.aprobacionRegistradoPor.text(registro.aprobacionGec.registrador.identidad);
		this.obj.aprobacionEmitidoEl.text(utilitario.formatearTimestampToString(registro.aprobacionGec.fechaHoraEmitido));
		this.obj.aprobacionEmitidoPor.text(registro.aprobacionGec.emisor.identidad); 
		this.obj.aprobacionObservacionCliente.text(registro.aprobacionGec.observacionCliente);  
		this.obj.aprobacionIdAprobacion = registro.aprobacionGec.id;  		  
		$("#tablaAprobacionDetalle > tbody ").children().remove();    
		ref.pintarDetalleAprobacion(registro.detalle);
	};
	
	moduloActual.pintarDetalleAprobacion=function(registros){
		  var ref=this;
		  var numeroRegistros = registros.length;
		  var registro = null;
		  var clonDetalle=null;
		  var totalRecibido = 0;
		  try {		  
			  for(var contador=0;contador<numeroRegistros;contador++){
				  registro = registros[contador];   
				  clonDetalle = ref.obj.aprobacionDetalleGec.clone();
				  clonDetalle.attr('id', "aprobacionDetalleGec" + contador);    
				  
				  var cmpDetalleNumeroGuia=clonDetalle.find("#aprobacionDetalleNumeroGuia");
				  cmpDetalleNumeroGuia.text(registro.numeroGuia);
				  cmpDetalleNumeroGuia.attr('id', "aprobacionDetalleNumeroGuia" + contador);			  
				  
				  var cmpFechaEmision=clonDetalle.find("#aprobacionFechaEmision");
				  cmpFechaEmision.text(utilitario.formatearFecha(registro.fechaEmision));
				  cmpFechaEmision.attr('id', "aprobacionFechaEmision" + contador);			 
				  
				  var cmpFechaRecepcion=clonDetalle.find("#aprobacionFechaRecepcion");
				  cmpFechaRecepcion.text(utilitario.formatearFecha(registro.fechaRecepcion));
				  cmpFechaRecepcion.attr('id', "aprobacionFechaRecepcion" + contador);			  
				  
				  var cmpVolumenDespachado=clonDetalle.find("#aprobacionVolumenDespachado");
				  cmpVolumenDespachado.text(registro.volumenDespachado.toFixed(2));
				  cmpVolumenDespachado.attr('id', "aprobacionVolumenDespachado" + contador);			  
				  
				  var cmpVolumenRecibido=clonDetalle.find("#aprobacionVolumenRecibido");
				  cmpVolumenRecibido.text(registro.volumenRecibido.toFixed(2));
				  cmpVolumenRecibido.attr('id', "aprobacionVolumenRecibido" + contador);			  
				  totalRecibido=totalRecibido+parseFloat(registro.volumenRecibido);
				  
				  var cmpEstadoDiaOperativo=clonDetalle.find("#aprobacionEstadoDiaOperativo");
				  cmpEstadoDiaOperativo.text(registro.estado);
				  cmpEstadoDiaOperativo.attr('id', "aprobacionEstadoDiaOperativo" + contador);		  
				  
				  $("#tablaAprobacionDetalle > tbody:last-child").append(clonDetalle);			  
			  }
			  clonDetalle = ref.obj.aprobacionResumenDetalleGec.clone();		 
			  $("#tablaAprobacionDetalle > tbody:last-child").append(clonDetalle);	
			  $("#aprobacionVolumenTotalRecibido").text(totalRecibido.toFixed(2));
		  }catch(error){
			  console.log(error);
		  }
	  };
	  
	  moduloBase.prototype.iniciarCancelarAprobacion=function(){
		  var referenciaModulo=this;
		  //referenciaModulo.modoEdicion=constantes.MODO_VER;
		  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);		  
		  referenciaModulo.obj.cntFormulario.hide();
		  referenciaModulo.obj.ocultaContenedorVista.hide();
		  referenciaModulo.obj.cntVistaRegistro.hide();
		  referenciaModulo.obj.cntAprobacionGEC.hide();
		  referenciaModulo.obj.ocultaContenedorAprobacion.show();
		  referenciaModulo.obj.cntTabla.show();		  
		  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CERRAR_VISTA);
	  };  	
  
	moduloActual.solicitarEmitirGec= function(){
		try {
			this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
			this.obj.frmConfirmarModificarEstado.modal("show");
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		}
	};
  
	moduloActual.activarBotones=function(){
	  var referenciaModulo=this;
	  if(referenciaModulo.obj.filtroIdRolUsuario.val()==1){	//admin
		  referenciaModulo.obj.btnAgregarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);  
		  referenciaModulo.obj.btnModificarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);		
		  referenciaModulo.obj.btnEmitir.removeClass(constantes.CSS_CLASE_DESHABILITADA); 
		  referenciaModulo.obj.btnAprobar.removeClass(constantes.CSS_CLASE_DESHABILITADA); 
		  referenciaModulo.obj.btnNotificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);				 
	  }
	  if(referenciaModulo.obj.filtroIdRolUsuario.val()==3){	  //supervisor
		  if (referenciaModulo.estadoRegistro == constantes.ESTADO_GEC_REGISTRADO) {
			  //console.log("USUARIO supervisor Y ESTADO_GEC_REGISTRADO");
			  referenciaModulo.obj.btnEmitir.removeClass(constantes.CSS_CLASE_DESHABILITADA); 
		  }else{
			  referenciaModulo.obj.btnEmitir.addClass(constantes.CSS_CLASE_DESHABILITADA); 
		  }		  
		  if (referenciaModulo.estadoRegistro == constantes.ESTADO_GEC_EMITIDO) {
			  //console.log("USUARIO supervisor Y ESTADO_GEC_EMITIDO");
			  referenciaModulo.obj.btnNotificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  }else{
			  referenciaModulo.obj.btnNotificar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  }
	  }	  
	  if(referenciaModulo.obj.filtroIdRolUsuario.val()==6){	 //unidad transporte
		  if (referenciaModulo.estadoRegistro == constantes.ESTADO_GEC_REGISTRADO) {		
			  	//console.log("USUARIO unidad transporte Y ESTADO_GEC_REGISTRADO");
			  	referenciaModulo.obj.btnModificarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);
			  	referenciaModulo.obj.btnNotificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);				  
			}else {
				referenciaModulo.obj.btnModificarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);
			  	referenciaModulo.obj.btnNotificar.addClass(constantes.CSS_CLASE_DESHABILITADA);	
			}
	  }	  
	  if(referenciaModulo.obj.filtroIdRolUsuario.val()==7){	//cliente
		  if (referenciaModulo.estadoRegistro == constantes.ESTADO_GEC_EMITIDO || referenciaModulo.estadoRegistro == constantes.ESTADO_GEC_OBSERVADO) {		
			  	//console.log("USUARIO clie|nte Y ESTADO_GEC_EMITIDO");
				referenciaModulo.obj.btnAprobar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
				referenciaModulo.obj.btnModificarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA); 
			}else{
				referenciaModulo.obj.btnAprobar.addClass(constantes.CSS_CLASE_DESHABILITADA); 
				referenciaModulo.obj.btnModificarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);
			} 
	  }
	  
	  /*this.obj.btnModificarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  this.obj.btnModificarEstado.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  this.obj.btnVerGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  ref.obj.btnEmitir.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  ref.obj.btnImprimir.removeClass(constantes.CSS_CLASE_DESHABILITADA);*/
	  
	  referenciaModulo.obj.btnVerGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  referenciaModulo.obj.btnImprimir.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	};

	moduloActual.desactivarBotones=function(){
		//console.log("desactivarBotones");
		var referenciaModulo=this;
		/*this.obj.btnModificarEstado.html(constantes.BOTON_ACTIVAR + constantes.TITULO_ACTIVAR_REGISTRO);
		this.obj.btnModificarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);
		this.obj.btnModificarEstado.addClass(constantes.CSS_CLASE_DESHABILITADA);
		this.obj.btnVerGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);
		ref.obj.btnEmitir.addClass(constantes.CSS_CLASE_DESHABILITADA);
		ref.obj.btnImprimir.addClass(constantes.CSS_CLASE_DESHABILITADA);*/
		
		referenciaModulo.obj.btnAgregarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);  
		referenciaModulo.obj.btnModificarGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);		
		referenciaModulo.obj.btnVerGEC.addClass(constantes.CSS_CLASE_DESHABILITADA);
		referenciaModulo.obj.btnEmitir.addClass(constantes.CSS_CLASE_DESHABILITADA); 
		referenciaModulo.obj.btnAprobar.addClass(constantes.CSS_CLASE_DESHABILITADA);  
		referenciaModulo.obj.btnImprimir.addClass(constantes.CSS_CLASE_DESHABILITADA);
		referenciaModulo.obj.btnNotificar.addClass(constantes.CSS_CLASE_DESHABILITADA);	
		
		
		 if(referenciaModulo.obj.filtroIdRolUsuario.val()==1 || referenciaModulo.obj.filtroIdRolUsuario.val()==6){	//admin
			  referenciaModulo.obj.btnAgregarGEC.removeClass(constantes.CSS_CLASE_DESHABILITADA); 			 
		  }
	};
	
	moduloActual.llamadaAjax=function(d){
		var referenciaModulo =this;
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    d.filtroEstado= referenciaModulo.obj.cmpFiltroEstado.val();
	    d.filtroOperacion= referenciaModulo.obj.cmpFiltroOperacion.val();
        var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
        var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
        var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
        d.filtroFechaInicio= fechaInicio;	
        d.filtroFechaFinal = fechaFinal;
	};

	moduloActual.recuperarValores = function(registro){  
		//TODO		
		var ref=this;
		var eRegistro = {};
		var totalRecibido=0;
		var totalDespachado=0;
		try {
	    eRegistro.id = parseInt(ref.idRegistro);	
	    eRegistro.ordenCompra =ref.obj.cmpOrdenCompra.val();
	    eRegistro.idOperacion =ref.obj.cmpClienteOperacion.attr("data-id-operacion");
	    eRegistro.idCliente =ref.obj.cmpClienteOperacion.attr("data-id-cliente");	    
	    eRegistro.numeroSerie = ref.obj.cmpNumeroGuia.attr("data-numero-serie");
	    eRegistro.numeroGEC = ref.obj.cmpNumeroGuia.attr("data-numero-guia");
	    eRegistro.comentarios = ref.obj.cmpComentarios.val();
	    eRegistro.numeroContrato = ref.obj.cmpNumeroContrato.val().toUpperCase();
	    eRegistro.descripcionContrato = ref.obj.cmpDescripcionContrato.val();
	    eRegistro.idProducto = ref.obj.cmpProducto.val();
	    eRegistro.idTransportista =  ref.obj.cmpIdTransportista.val();
	    eRegistro.fechaGuiaCombustible = utilitario.formatearStringToDate(ref.obj.cmpFiltroFechaGuia.val());
	    eRegistro.detalle=[];
	    $('#tablaDetalleGec input:checked').each(function() {
	    	var detalleGec={};
	    	var fila = $(this).attr('data-fila');
	    	//console.log("fila");
	        //console.log(fila);
	        var numeroGuiaRemision="";
	        var fechaEmision=null;
	        var fechaRecepcion=null;
	        var volumenDespachado=null;
	        var volumenRecibido=null;
	        numeroGuiaRemision=$("#cmpDetalleNumeroGuia"+fila).text();
	        fechaEmision=$("#cmpFechaEmision"+fila).text();
	        fechaRecepcion=$("#cmpFechaRecepcion"+fila).text();
	        volumenDespachado=$("#cmpVolumenDespachado"+fila).text();
	        volumenRecibido=$("#cmpVolumenRecibido"+fila).text();
	        estado=$("#cmpEstadoDiaOperativo"+fila).text();      
	        detalleGec.numeroGuia=numeroGuiaRemision;
	        detalleGec.fechaEmision=utilitario.formatearStringToDate(fechaEmision);
	        	//fechaEmision;
	        detalleGec.fechaRecepcion=utilitario.formatearStringToDate(fechaRecepcion);
	        	//;
	        detalleGec.volumenDespachado=parseFloat(volumenDespachado);
	        detalleGec.volumenRecibido=parseFloat(volumenRecibido);
	        detalleGec.estado=estado;
	        eRegistro.detalle.push(detalleGec);
	        totalRecibido=totalRecibido+parseFloat(volumenRecibido);
	        totalDespachado=totalDespachado+parseFloat(volumenDespachado);
	    });
	    eRegistro.totalVolumenRecibido=totalRecibido;
	    eRegistro.totalVolumenDespachado=totalDespachado;
		}  catch(error){
			console.log(error.message);
		}
		return eRegistro;
	};
	
	moduloActual.recuperarAprobacion = function(registro){  
		//TODO
		var ref=this;
		var eRegistro = {};
		try {			
			eRegistro.id = parseInt(ref.obj.aprobacionIdAprobacion);
			eRegistro.idGcombustible = ref.idRegistro;  
			eRegistro.observacionCliente =ref.obj.aprobacionObservacionCliente.val();
			eRegistro.estado = constantes.ESTADO_GEC_APROBADO;
			//console.log("eRegistro");
			//console.log(eRegistro);  
		}  catch(error){     
			console.log(error.message);   
		}
		return eRegistro;
	};
	
	moduloActual.recuperarObservacion = function(registro){  
		//TODO
		var ref=this;
		var eRegistro = {};
		try {			
			eRegistro.id = parseInt(ref.obj.aprobacionIdAprobacion);
			eRegistro.idGcombustible = ref.idRegistro;   
			eRegistro.observacionCliente =ref.obj.aprobacionObservacionCliente.val();
			eRegistro.estado = constantes.ESTADO_GEC_OBSERVADO;
			//console.log("eRegistro");
			//console.log(eRegistro);
		}  catch(error){     
			console.log(error.message);   
		}
		return eRegistro;
	};
	
	moduloActual.abrirVentanaCorreo= function(){  
		//console.log("abrirVentanaCorreo");
		var referenciaModulo=this;	
//		referenciaModulo.obj.cmpNumeroGuia = referenciaModulo.obj.datClienteApi.cell(indice,2).data();
//		referenciaModulo.obj.cmpEstadoGuia = referenciaModulo.obj.datClienteApi.cell(indice,7).data();	
		var estadoGuia = "";
		if(constantes.ESTADO_GEC_APROBADO == 1){
			estadoGuia = "Registrado";
		}else if(constantes.ESTADO_GEC_EMITIDO == 2){
			estadoGuia = "Emitido";
		}else if(constantes.ESTADO_GEC_APROBADO == 3){
			estadoGuia = "Aprobado";
		}else if(constantes.ESTADO_GEC_OBSERVADO == 4){
			estadoGuia = "Observado";
		}
		
		var asunto = "Envio GEC  Nro. "+ referenciaModulo.obj.cmpNumeroGuia + "  con estado " + estadoGuia;
		
		try {			
			referenciaModulo.obj.cmpAsunto.val(asunto);
			referenciaModulo.obj.frmNotificar.modal("show");
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);  
		}
	};
	
	moduloActual.enviarCorreo= function(){
		  //var eRegistro = {};
		  var ref=this;
		  ref.obj.frmNotificar.modal("hide");
		  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		  ref.obj.ocultaContenedorTabla.show();
	    //  eRegistro.id = parseInt(ref.idDiaOperativo);
	      try{
		  $.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: './guia-combustible/notificar-gec', 
		    contentType: ref.TIPO_CONTENIDO, 
		    data: {filtroMailPara : ref.obj.cmpPara.val(), 
		    	   filtroMailCC : ref.obj.cmpCC.val(),
		    	   filtroIdGuiaCombustible : ref.idRegistro		    
		    	},	
		    success: function(respuesta) {
		      if (!respuesta.estado) {
		    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		      } else {	
		    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje); 
		      }
		      ref.obj.ocultaContenedorTabla.hide();
		    },			    		    
		    error: function() {
		    	ref.mostrarErrorServidor(xhr,estado,error); 
		    	ref.obj.ocultaContenedorTabla.hide();
		    }
		    });
	      } catch(error){
	    	  ref.mostrarDepuracion(error.message);
	    	  ref.obj.ocultaContenedorTabla.hide();
		  }  
	};
  
  moduloActual.inicializar();
});