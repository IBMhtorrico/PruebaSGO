$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='liquidacion';
  moduloActual.numeroObservados=0;
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_LISTARXESTACION= moduloActual.urlBase + '/listar-estacion';
  moduloActual.URL_LISTARXTANQUE= moduloActual.urlBase + '/listar-tanque';
  moduloActual.URL_LIQUIDAR_JORNADA = moduloActual.urlBase + '/liquidar-jornada';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'}); 
  moduloActual.columnasGrilla.push({ "data": 'nombreClienteOperacion'});
  moduloActual.columnasGrilla.push({ "data": 'idOperacion'});
  moduloActual.columnasGrilla.push({ "data": 'nombreProducto'});
  moduloActual.columnasGrilla.push({ "data": 'idProducto'});
  moduloActual.columnasGrilla.push({ "data": 'stockFinal'});
  moduloActual.columnasGrilla.push({ "data": 'stockFinalCalculado'});
  moduloActual.columnasGrilla.push({ "data": 'variacion'});
  moduloActual.columnasGrilla.push({ "data": 'tolerancia'});
  moduloActual.columnasGrilla.push({ "data": 'faltante'});
  moduloActual.columnasGrilla.push({ "data": null});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": false, "visible":true,"render" : utilitario.formatearFecha, "className": "text-center"});
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": false, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": false, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": false, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": false, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": false, "orderable": false, "visible":true, 
	  "render" : function ( data, type, full, meta ) {
      return utilitario.formatearDecimales(data,2);
  } , "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": false, "orderable": false, "visible":true, "render" : function ( data, type, full, meta ) {
      return utilitario.formatearDecimales(data,2);
  } , "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 8, "searchable": false, "orderable": false, "visible":true, 	  "render" : function ( data, type, full, meta ) {
      return utilitario.formatearDecimales(data,2);
  } , "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 9, "searchable": false, "orderable": false, "visible":true, 	  "render" : function ( data, type, full, meta ) {
      return utilitario.formatearDecimales(data,2);
  } , "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 10, "searchable": false, "orderable": false, "visible":true, 	  
	  "render" : function ( data, type, full, meta ) {
      return utilitario.formatearDecimales(data,2);
  } , "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 11, "searchable": false, "orderable": false, "visible":true,
	  "render" : function ( data, type, full, meta ) {
		  if (full.faltante < 0){
			  moduloActual.numeroObservados++;
		  }
		  return utilitario.formatearEstadoLiquidacion(full.faltante);
		  }, "className": "text-center"});

  moduloActual.reglasValidacionFormulario={
	cmpRazonSocial: { required: true, maxlength: 150 },
	cmpNombreCorto: { required: true, maxlength: 20   },
	cmpRuc: { required: true, rangelength: [11, 11], number: true },
	cmpNumContrato: { required: true  },
	cmpDesContrato: { required: true  }
  };

  
  moduloActual.mensajesValidacionFormulario={
	cmpRazonSocial: {
		required: "El campo es obligatorio",
		maxlength: "El campo Raz�n Social debe contener 150 caracteres como m&aacute;ximo."
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
	  var respuesta = true;
	  var ref=this; 
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
	
	moduloActual.iniciarVerDetallexEstacion= function (){
		console.log("iniciarVerDetallexEstacion");
		var ref = this;
		var parametros ={};
		console.log(ref.fechaOperativaSeleccionada);
		parametros.filtroOperacion= ref.idOperacionSeleccionada;
		parametros.filtroFechaDiaOperativo=ref.fechaOperativaSeleccionada;
		parametros.filtroProducto=ref.idProductoSeleccionado;
		console.log(parametros);
		ref.obj.ocultaContenedorDetallexEstacion.show();
		  try {
			  $.ajax({
				    type: constantes.PETICION_TIPO_GET,
				    url: ref.URL_LISTARXESTACION, 
				    contentType: ref.TIPO_CONTENIDO, 
				    data: parametros,	
				    success: function(respuesta) {
				      if (!respuesta.estado) {
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				      } else {  				    			    		
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);				    	  
				    	  ref.pintarDetallexEstacion(respuesta.contenido.carga);
				    	  ref.verDetallexEstacion();
				      }
				      ref.obj.ocultaContenedorDetallexEstacion.hide();
				    },			    		    
				    error: function() {
				    	ref.mostrarErrorServidor(xhr,estado,error);
				    	ref.obj.ocultaContenedorDetallexEstacion.hide();
				    }
			  });
		  }catch(error){
			  console.log(error);
			  ref.obj.ocultaContenedorDetallexEstacion.hide();
		  }
	};
	
	moduloActual.pintarDetallexEstacion=function(registros){
		  console.log("pintarDetallexEstacion");
		  var ref=this;
		  var numeroRegistros = registros.length;
		  var registro = null;
		  var clonDetalle=null;
		  try {
			  $("#tablaVistaDetalleEstacion tbody").off( "click", "**" );
			  $("#tablaVistaDetalleEstacion > tbody:first").children().remove();
			  ref.numeroRegistrosxEstacion= numeroRegistros;
			  console.log("ref.numeroRegistrosxEstacion");
			  console.log(ref.numeroRegistrosxEstacion);
			  if (numeroRegistros>0){
				  for(var contador=0;contador<numeroRegistros;contador++){
					  registro = registros[contador];
					  clonDetalle = ref.obj.filaDetalleEstacion.clone();
					  clonDetalle.attr('id', "filaDetalleEstacion" + contador);
					  clonDetalle.attr('data-fila',contador);
			  
					  var vistaDxEEstacion=clonDetalle.find("#vistaDxEEstacion");
					  vistaDxEEstacion.text(registro.nombreEstacion);
					  vistaDxEEstacion.attr('data-id-estacion', registro.idEstacion);	
					  vistaDxEEstacion.attr('id', "vistaDxEEstacion" + contador);			  
					  
					  var vistaDxEStockFinal=clonDetalle.find("#vistaDxEStockFinal");
					  vistaDxEStockFinal.text(utilitario.formatearDecimales(registro.stockFinal,2));
					  vistaDxEStockFinal.attr('id', "vistaDxEStockFinal" + contador);			 
					  
					  var vistaDxEStockCalculado=clonDetalle.find("#vistaDxEStockCalculado");
					  vistaDxEStockCalculado.text(utilitario.formatearDecimales(registro.stockFinalCalculado,2));
					  vistaDxEStockCalculado.attr('id', "vistaDxEStockCalculado" + contador);			  
					  
					  var vistaDxEVariacion=clonDetalle.find("#vistaDxEVariacion");
					  vistaDxEVariacion.text(registro.variacion.toFixed(2));
					  vistaDxEVariacion.attr('id', "vistaDxEVariacion" + contador);			  
					  
					  var vistaDxELimite=clonDetalle.find("#vistaDxELimite");
					  vistaDxELimite.text(registro.tolerancia.toFixed(2));
					  vistaDxELimite.attr('id', "vistaDxELimite" + contador);
					  
					  var vistaDxEFaltante=clonDetalle.find("#vistaDxEFaltante");
					  vistaDxEFaltante.text(registro.faltante.toFixed(2));
					  vistaDxEFaltante.attr('id', "vistaDxEFaltante" + contador);	
					  
					  var vistaDxEEstado=clonDetalle.find("#vistaDxEEstado");
					  vistaDxEEstado.text(utilitario.formatearEstadoLiquidacion(registro.faltante));
					  vistaDxEEstado.attr('id', "vistaDxEEstado" + contador);					  
					  $("#tablaVistaDetalleEstacion > tbody:last-child").append(clonDetalle);			  
				  }
				  
				  $("#tablaVistaDetalleEstacion > tbody:last-child").append(clonDetalle);			  
				  $('#tablaVistaDetalleEstacion tbody').on(ref.NOMBRE_EVENTO_CLICK, 'tr', function () {
					  console.log("on tablaVistaDetalleEstacion tr ");
					  console.log("ref.numeroRegistrosxEstacion");
					  console.log(ref.numeroRegistrosxEstacion);
					  var indice = $(this).attr("data-fila");
					  if (ref.numeroRegistrosxEstacion > 0){
						  if ( $(this).hasClass('selected') ) {
						    $(this).removeClass('selected');
						    ref.desactivarBotonesEstacion();
						  } else {
						    $('#tablaVistaDetalleEstacion tr.selected').removeClass('selected');
						    $(this).addClass('selected');
				            ref.idEstacionSeleccionada = $("#vistaDxEEstacion"+indice ).attr('data-id-estacion');
				            ref.nombreEstacionSeleccionada = $("#vistaDxEEstacion"+indice ).text();
				            ref.activarBotonesEstacion();
						  } 
					  }					  
				  });
			  } else {
				  clonDetalle=ref.obj.filaDetalleEstacionVacia;				  
				  $("#tablaVistaDetalleEstacion > tbody:last-child").append(clonDetalle);
			  }

		  }catch(error){
			  console.log(error);
		  }
	};
	
	moduloActual.activarBotonesEstacion=function(){
		  var ref=this;
		  ref.obj.btnVerDetallexTanque.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	};
	
	moduloActual.desactivarBotonesEstacion=function(){
		var ref=this;
		ref.obj.btnVerDetallexTanque.addClass(constantes.CSS_CLASE_DESHABILITADA);
	};
	
	moduloActual.pintarDetallexTanque=function(registros){
		  console.log("pintarDetallexTanque");
		  var ref=this;
		  var numeroRegistros = registros.length;
		  var registro = null;
		  var clonDetalle=null;
		  try {
			  $("#tablaVistaDetalleTanque > tbody:first").children().remove();
			  
			  if (numeroRegistros>0){
				  for(var contador=0;contador<numeroRegistros;contador++){
					  registro = registros[contador];
					  clonDetalle = ref.obj.filaDetalleTanque.clone();
					  clonDetalle.attr('id', "filaDetalleTanque" + contador);
					  clonDetalle.attr('data-id',contador);
			  
					  var vistaDxTTanque=clonDetalle.find("#vistaDxTTanque");
					  vistaDxTTanque.text(registro.nombreTanque);
					  vistaDxTTanque.attr('id', "vistaDxTTanque" + contador);			  
					  
					  var vistaDxTStockFinal=clonDetalle.find("#vistaDxTStockFinal");
					  vistaDxTStockFinal.text(utilitario.formatearDecimales(registro.stockFinal,2));
					  vistaDxTStockFinal.attr('id', "vistaDxTStockFinal" + contador);			 
					  
					  var vistaDxTStockCalculado=clonDetalle.find("#vistaDxTStockCalculado");
					  vistaDxTStockCalculado.text(utilitario.formatearDecimales(registro.stockFinalCalculado,2));
					  vistaDxTStockCalculado.attr('id', "vistaDxTStockCalculado" + contador);			  
					  
					  var vistaDxTVariacion=clonDetalle.find("#vistaDxTVariacion");
					  vistaDxTVariacion.text(registro.variacion.toFixed(2));
					  vistaDxTVariacion.attr('id', "vistaDxTVariacion" + contador);			  
					  
					  var vistaDxTLimite=clonDetalle.find("#vistaDxTLimite");
					  vistaDxTLimite.text(registro.tolerancia.toFixed(2));
					  vistaDxTLimite.attr('id', "vistaDxTLimite" + contador);
					  
					  var vistaDxTFaltante=clonDetalle.find("#vistaDxTFaltante");
					  vistaDxTFaltante.text(registro.faltante.toFixed(2));
					  vistaDxTFaltante.attr('id', "vistaDxTFaltante" + contador);	
					  
					  var vistaDxTEstado=clonDetalle.find("#vistaDxTEstado");
					  vistaDxTEstado.text(utilitario.formatearEstadoLiquidacion(registro.faltante));
					  vistaDxTEstado.attr('id', "vistaDxTEstado" + contador);					  
					  $("#tablaVistaDetalleTanque > tbody:last-child").append(clonDetalle);			  
				  }
				  
				  $("#tablaVistaDetalleTanque > tbody:last-child").append(clonDetalle);
				  /*$('#tablaVistaDetalleTanque tbody').on(ref.NOMBRE_EVENTO_CLICK, 'tr', function () {
					  console.log($(this).attr("id"));
					  console.log($(this).attr("data-id"));
				  });*/
			  } else {
				  clonDetalle=ref.obj.filaDetalleTanqueVacia;
				  $("#tablaVistaDetalleTanque > tbody:last-child").append(clonDetalle);
			  }

		  }catch(error){
			  console.log(error);
		  }
	  };
	
	moduloActual.verDetallexEstacion= function(){
		console.log("verDetallexEstacion");
		console.log("verDetallexEstacion2");
		var ref = this;
		try {
			ref.obj.tituloSeccion.text("Detalle de estaciones");
			ref.obj.cntTabla.hide();
			ref.obj.ocultaContenedorFormulario.hide();
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
			ref.obj.cmpClienteOperacion.val(ref.nombreOperacionSeleccionada);
			ref.obj.cmpDiaOperativo.val(utilitario.formatearFecha(ref.fechaOperativaSeleccionada));
			ref.obj.cmpProducto.val(ref.nombreProductoSeleccionado);
			ref.obj.cntDetalleEstacion.show();
			ref.obj.ocultaContenedorDetallexEstacion.hide();
			console.log("verDetallexEstacion3");
		} catch(error){
			console.log(error);
		}
	};
	
	moduloActual.cerrarDetallexEstacion= function(){
		console.log("cerrarDetallexEstacion");
		var ref = this;
		try {
			ref.obj.tituloSeccion.text("Lista de registros");
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
			ref.obj.cntDetalleEstacion.hide();
			ref.obj.ocultaContenedorDetallexEstacion.hide();
			ref.obj.ocultaContenedorFormulario.hide();
			ref.obj.cntTabla.show();			
		} catch(error){
			console.log(error);
		}
	};
	
	
	moduloActual.iniciarVerDetallexTanque= function (){
		console.log("iniciarVerDetallexTanque");
		var ref = this;
		var parametros ={};
		parametros.filtroOperacion= ref.idOperacionSeleccionada;
		parametros.filtroFechaDiaOperativo=(ref.fechaOperativaSeleccionada);
		parametros.filtroProducto=ref.idProductoSeleccionado;
		parametros.filtroEstacion=ref.idEstacionSeleccionada;
		console.log(parametros);
		ref.obj.ocultaContenedorDetallexTanque.show();
		  try {
			  $.ajax({
				    type: constantes.PETICION_TIPO_GET,
				    url: ref.URL_LISTARXTANQUE, 
				    contentType: ref.TIPO_CONTENIDO, 
				    data: parametros,	
				    success: function(respuesta) {
				      if (!respuesta.estado) {
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				      } else {  				    			    		
				    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);				    	  
				    	  ref.pintarDetallexTanque(respuesta.contenido.carga);
				    	  ref.verDetallexTanque();
				      }
				      ref.obj.ocultaContenedorDetallexTanque.hide();
				    },			    		    
				    error: function() {
				    	ref.obj.ocultaContenedorDetallexTanque.hide();
				    	ref.mostrarErrorServidor(xhr,estado,error); 
				    }
			  });
		  }catch(error){
			  console.log(error);
		  }
	};
		
	moduloActual.verDetallexTanque= function(){
		console.log("verDetallexTanque");
		var ref = this;
		try {
			ref.obj.tituloSeccion.text("Detalle de tanques");
			ref.obj.cmpClienteOperacionEstacion.val(ref.nombreOperacionSeleccionada);
			ref.obj.cmpDiaOperativoEstacion.val(utilitario.formatearFecha(ref.fechaOperativaSeleccionada));
			ref.obj.cmpProductoEstacion.val(ref.nombreProductoSeleccionado);
			ref.obj.cmpNombreEstacion.val(ref.nombreEstacionSeleccionada);
			ref.obj.cntTabla.hide();
			ref.obj.ocultaContenedorFormulario.hide();
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
			ref.obj.cntDetalleEstacion.hide();
			ref.obj.ocultaContenedorDetallexEstacion.hide();
			ref.obj.cntDetalleTanque.show();
			ref.obj.ocultaContenedorDetallexTanque.hide();
		} catch(error){
			console.log(error);
		}
	};
	
	moduloActual.cerrarDetallexTanque= function(){
		console.log("cerrarDetallexTanque");
		var ref = this;
		try {
			ref.obj.tituloSeccion.text("Detalle de estaciones");
			ref.obj.cntTabla.hide();
			ref.obj.ocultaContenedorFormulario.hide();
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
			ref.obj.ocultaContenedorDetallexTanque.hide();
			ref.obj.cntDetalleTanque.hide();			
			ref.obj.ocultaContenedorDetallexEstacion.hide();
			ref.obj.cntDetalleEstacion.show();			
		} catch(error){
			console.log(error);
		}
	};
	
	moduloActual.validarLiquidacion= function(){
		var ref = this;
		var respuesta = true;
		var longitudObservaciones =$("#cmpObservacionesLiquidacion").val().length;
		console.log("longitudObservaciones");
		console.log(longitudObservaciones);
		console.log("ref.numeroObservados");
		console.log(ref.numeroObservados);
		if ((ref.numeroObservados > 0) && (longitudObservaciones< 1)){
			respuesta = false;
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Debe ingresar observaciones");
		}
		return respuesta;
	};
	
	moduloActual.liquidarDiaOperativo= function(){		
		var ref= this;
			var parametros = {};
			parametros.fechaOperativa = utilitario.formatearFecha2Iso(ref.obj.cmpFiltroFechaOperativa.val());
			parametros.idOperacion=ref.obj.cmpFiltroOperacion.val();
			parametros.comentario = $("#cmpObservacionesLiquidacion").val();
			
			console.log(parametros);
			
			if (moduloActual.validarLiquidacion()){
				try {
					  $.ajax({
						    type: constantes.PETICION_TIPO_GET,
						    url: ref.URL_LIQUIDAR_JORNADA,
						    data: {idOperacion: ref.obj.cmpFiltroOperacion.val(),
						    	   fechaOperativa: utilitario.formatearFecha2Iso(ref.obj.cmpFiltroFechaOperativa.val()),
						    	   comentario: $("#cmpObservacionesLiquidacion").val()
						    },	
						    success: function(respuesta) {
						      if (!respuesta.estado) {
						    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
						      } else {  				    			    		
						    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
						      }
						      ref.obj.ocultaContenedorDetallexTanque.hide();
						    },			    		    
						    error: function() {
						    	ref.obj.ocultaContenedorDetallexTanque.hide();
						    	ref.mostrarErrorServidor(xhr,estado,error); 
						    }
					  });
				  }catch(error){
					  console.log(error);
					  ref.obj.ocultaContenedorDetallexTanque.hide();
				  }
				  
				  
				/*try {
					  $.ajax({
						    type: constantes.PETICION_TIPO_POST,
						    url: ref.URL_LIQUIDAR_JORNADA, 
						    data: parametros,	
						    success: function(respuesta) {
						      if (!respuesta.estado) {
						    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
						      } else {  				    			    		
						    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);				    	  
						      }
						      ref.obj.ocultaContenedorDetallexTanque.hide();
						    },			    		    
						    error: function() {
						    	ref.obj.ocultaContenedorDetallexTanque.hide();
						    	ref.mostrarErrorServidor(xhr,estado,error); 
						    }
					  });
				  }catch(error){
					  console.log(error);
				  }*/
			}
	};
	
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	var ref=this;	 
	
	//Botones
	ref.obj.cmpClienteOperacion=$("#cmpClienteOperacion");
	ref.obj.cmpDiaOperativo=$("#cmpDiaOperativo");
	ref.obj.cmpProducto=$("#cmpProducto");
	ref.obj.btnLiquidar=$("#btnLiquidar");
	ref.obj.btnLiquidar.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.descripcionPermiso = 'LIQUIDAR_DIA_OPERATIVO';
		ref.validaPermisos();
		//ref.liquidarDiaOperativo();
	});
	
	ref.obj.btnVerDetalle=$("#btnVerDetalle");
	ref.obj.btnVerDetalle.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.descripcionPermiso = 'DETALLE_POR_ESTACION';
		ref.validaPermisos();
		//ref.iniciarVerDetallexEstacion();
	});
	ref.obj.btnImprimir=$("#btnImprimir");
	ref.obj.btnImprimir.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.descripcionPermiso = 'EXPORTAR_PDF';
		ref.validaPermisos();
		//ref.exportarPDF();
	});
	//Formulario Detalle x Estacion
	ref.obj.filaDetalleEstacion= $("#filaDetalleEstacion");
	ref.obj.filaDetalleEstacionVacia= $("#filaDetalleEstacionVacia");
	ref.obj.tablaVistaDetalleEstacion=$("#tablaVistaDetalleEstacion");	
	$('#filaDetalleEstaci�n').remove();	
	ref.obj.cntDetalleEstacion=$("#cntDetalleEstacion");
	ref.obj.ocultaContenedorDetallexEstacion=$("#ocultaContenedorDetallexEstacion");
	ref.obj.btnRegresarDetalle=$("#btnRegresarDetalle");
	ref.obj.btnRegresarDetalle.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.cerrarDetallexEstacion();
	});

	//Formulario Detalle x Tanque
	ref.obj.cntDetalleTanque=$("#cntDetalleTanque");
	ref.obj.cmpClienteOperacionEstacion= $("#cmpClienteOperacionEstacion");
	ref.obj.cmpDiaOperativoEstacion= $("#cmpDiaOperativoEstacion");
	ref.obj.cmpProductoEstacion= $("#cmpProductoEstacion");
	ref.obj.cmpNombreEstacion= $("#cmpNombreEstacion");
	ref.obj.btnVerDetallexTanque=$("#btnVerDetallexTanque");
	ref.obj.btnVerDetallexTanque.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.descripcionPermiso = 'DETALLE_POR_TANQUE';
		ref.validaPermisos();
//		ref.iniciarVerDetallexTanque();
	});
	
	ref.obj.tablaVistaDetalleTanque= $("#tablaVistaDetalleTanque");
	ref.obj.filaDetalleTanque= $("#filaDetalleTanque");
	ref.obj.filaDetalleTanqueVacia= $("#filaDetalleTanqueVacia");
	ref.obj.ocultaContenedorDetallexTanque= $("#ocultaContenedorDetallexTanque");
	ref.obj.btnRegresarDetalleEstacion= $("#btnRegresarDetalleEstacion");
	ref.obj.btnRegresarDetalleEstacion.on(ref.NOMBRE_EVENTO_CLICK,function(){
		ref.cerrarDetallexTanque();
	});
	
	
	ref.obj.cmpFiltroFechaOperativa=$("#cmpFiltroFechaOperativa");
	var fechaActual = ref.obj.cmpFiltroFechaOperativa.attr('data-fecha-actual');
	ref.obj.cmpFiltroFechaOperativa.daterangepicker({
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

  };

  moduloActual.exportarPDF=function(){
		var ref = this;
		var filtroOperacion= ref.obj.cmpFiltroOperacion.val();
		var filtroFechaDiaOperativo=  utilitario.formatearFecha2Iso(ref.obj.cmpFiltroFechaOperativa.val());
		window.open("./liquidacion/reporte?filtroOperacion="+filtroOperacion+"&filtroFechaDiaOperativo="+filtroFechaDiaOperativo+"&formato=pdf");  
  };
  
  moduloActual.grillaDespuesSeleccionar= function(indice){
	var ref=this;
	ref.fechaOperativaSeleccionada=ref.obj.datClienteApi.cell(indice,1).data();
	ref.idOperacionSeleccionada = ref.obj.datClienteApi.cell(indice,3).data();
	ref.nombreOperacionSeleccionada = ref.obj.datClienteApi.cell(indice,2).data();
	ref.idProductoSeleccionado = ref.obj.datClienteApi.cell(indice,5).data();
	ref.nombreProductoSeleccionado= ref.obj.datClienteApi.cell(indice,4).data();
  };
    

  
  moduloActual.pintarDetalleRecuperado=function(registros){
	  console.log("pintarDetalleRecuperado");
	  var ref=this;
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
				  console.log("click");
			        if($(this).is(":checked")) {
			        	console.log("marcado");			        	
			        }
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
    
  moduloActual.actualizarEstadoRegistro= function(){
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
	  }  catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	  }

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
	    error: function() {
	      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	    }
	    });
		};
  
 
	moduloActual.mostrarFormulario= function(registro){
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
		ref.obj.cmpNumeroGuia.attr("data-numero-serie",registro.numeroSerie);
		ref.obj.cmpNumeroGuia.attr("data-numero-guia",registro.numeroGEC);
		ref.obj.cmpNumeroGuia.val(registro.numeroSerie+"-"+registro.numeroGEC);		
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
		
	}
  };
  
	moduloActual.resetearFormulario= function(){
		var referenciaModulo= this;
		referenciaModulo.obj.frmPrincipal[0].reset();
		referenciaModulo.obj.verificadorFormulario.resetForm();
		jQuery.each( this.obj, function( i, val ) {
			if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
				if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
					console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
					referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
				}
			}
		});
	};
  
	moduloActual.activarBotones=function(){
	  var ref=this;
	  ref.obj.btnVerDetalle.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  ref.obj.btnImprimir.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	};

	moduloActual.desactivarBotones=function(){
		var ref=this;
		ref.obj.btnVerDetalle.addClass(constantes.CSS_CLASE_DESHABILITADA);
		ref.obj.btnImprimir.addClass(constantes.CSS_CLASE_DESHABILITADA);
	};
	
	moduloActual.llamadaAjax=function(d){
		var referenciaModulo =this;
		referenciaModulo.numeroObservados=0;
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    d.filtroOperacion= referenciaModulo.obj.cmpFiltroOperacion.val();
        d.filtroFechaDiaOperativo=  utilitario.formatearFecha2Iso(referenciaModulo.obj.cmpFiltroFechaOperativa.val());
	};
 
	moduloActual.validaPermisos= function(){
	  var referenciaModulo = this;
	  try{
	  console.log("Validando permiso para: " + referenciaModulo.descripcionPermiso);
	  referenciaModulo.obj.ocultaContenedorTabla.show();
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.VERIFICANDO_PERMISOS);
		  $.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: './validaPermisos/validar',
		    contentType: referenciaModulo.TIPO_CONTENIDO, 
		    data: { permiso : referenciaModulo.descripcionPermiso, },	
		    success: function(respuesta) {
		      console.log("respuesta.estado " + respuesta.estado);
		      if(!respuesta.estado){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
			      referenciaModulo.obj.ocultaContenedorTabla.hide();
		      } else {
			      if (referenciaModulo.descripcionPermiso == 'DETALLE_POR_ESTACION'){
			    	  referenciaModulo.iniciarVerDetallexEstacion();
			      } else if (referenciaModulo.descripcionPermiso == 'EXPORTAR_PDF'){
			    	  referenciaModulo.exportarPDF();
			      } else if (referenciaModulo.descripcionPermiso == 'DETALLE_POR_TANQUE'){
			    	  referenciaModulo.iniciarVerDetallexTanque();
			      } else if (referenciaModulo.descripcionPermiso == 'LIQUIDAR_DIA_OPERATIVO'){
			    	  referenciaModulo.liquidarDiaOperativo();
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
  moduloActual.inicializar();
});