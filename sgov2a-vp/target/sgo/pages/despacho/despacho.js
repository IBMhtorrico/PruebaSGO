$(document).ready(function(){
  var moduloActual = new moduloDespacho();
  moduloActual.urlBase='despacho';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR_JORNADA = './jornada/listar';
  moduloActual.URL_RECUPERAR_IMPORTACION = './despachoCarga/recuperar';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_CARGAR_ARCHIVO = moduloActual.urlBase +'/cargar-archivo';
 
  moduloActual.MEGABYTE= 1048576;
  moduloActual.TAMANO_MAXIMO_ARCHIVO=2*moduloActual.MEGABYTE;
  //listado de jornada
  moduloActual.ordenGrillaJornada=[[ 2, 'asc' ]];
  moduloActual.columnasGrillaJornada.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrillaJornada.push({ "data": 'estacion.id'});//Target2
  moduloActual.columnasGrillaJornada.push({ "data": 'estacion.nombre'});//Target3
  moduloActual.columnasGrillaJornada.push({ "data": 'fechaOperativa'});//Target4
  moduloActual.columnasGrillaJornada.push({ "data": 'totalDespachos'});//Target5
  moduloActual.columnasGrillaJornada.push({ "data": 'fechaActualizacion'});//Target6
  moduloActual.columnasGrillaJornada.push({ "data": 'usuarioActualizacion'});//Target7
  moduloActual.columnasGrillaJornada.push({ "data": 'estado'});//Target8

    
  //Columnas jornada
  moduloActual.definicionColumnasJornada.push({"targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasJornada.push({"targets" : 2, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasJornada.push({"targets" : 3, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnasJornada.push({"targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnasJornada.push({"targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasJornada.push({"targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center" });
  moduloActual.definicionColumnasJornada.push({"targets" : 7, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-rigth" });
  moduloActual.definicionColumnasJornada.push({"targets" : 8, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearEstadoJornada });


  //listado de despacho carga
  moduloActual.ordenGrillaDespachoCarga=[[ 2, 'asc' ]];
  moduloActual.columnasGrillaDespachoCarga.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrillaDespachoCarga.push({ "data": 'nombreArchivo'});//Target2
  moduloActual.columnasGrillaDespachoCarga.push({ "data": 'fechaCarga'});//Target3
  moduloActual.columnasGrillaDespachoCarga.push({ "data": 'comentario'});//Target4
  moduloActual.columnasGrillaDespachoCarga.push({ "data": 'operario.nombreCompletoOperario'});//Target5
  
  //Columnas despacho carga
  moduloActual.definicionColumnasDespachoCarga.push({"targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasDespachoCarga.push({"targets" : 2, "searchable" : true, "orderable" : false, "visible" : true  });
  moduloActual.definicionColumnasDespachoCarga.push({"targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnasDespachoCarga.push({"targets" : 4, "searchable" : true, "orderable" : false, "visible" : true  });
  moduloActual.definicionColumnasDespachoCarga.push({"targets" : 5, "searchable" : true, "orderable" : false, "visible" : true  });
  
  //listado de despachos
  moduloActual.ordenGrillaDespacho=[[ 2, 'asc' ]];
  moduloActual.columnasGrillaDespacho.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrillaDespacho.push({ "data": 'jornada.fechaOperativa'});//Target2
  moduloActual.columnasGrillaDespacho.push({ "data": 'estado'});//Target3
  moduloActual.columnasGrillaDespacho.push({ "data": 'numeroVale'});//Target4
  moduloActual.columnasGrillaDespacho.push({ "data": 'producto.nombre'});//Target5
  moduloActual.columnasGrillaDespacho.push({ "data": 'contometro.alias'});//Target6
  moduloActual.columnasGrillaDespacho.push({ "data": 'lecturaInicial'});//Target7
  moduloActual.columnasGrillaDespacho.push({ "data": 'lecturaFinal'});//Target8
  moduloActual.columnasGrillaDespacho.push({ "data": 'volumenObservado'});//Target9
  moduloActual.columnasGrillaDespacho.push({ "data": 'tipoRegistro'});//Target10
  
  //Columnas Despacho
  moduloActual.definicionColumnasDespacho.push({"targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasDespacho.push({"targets" : 2, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha});
  moduloActual.definicionColumnasDespacho.push({"targets" : 3, "searchable" : true, "orderable" : true, "visible" : true, "render"  : utilitario.formatearEstadoJornada });
  moduloActual.definicionColumnasDespacho.push({"targets" : 4, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasDespacho.push({"targets" : 5, "searchable" : true, "orderable" : true, "visible" : true });
  moduloActual.definicionColumnasDespacho.push({"targets" : 6, "searchable" : true, "orderable" : true, "visible" : true });
  moduloActual.definicionColumnasDespacho.push({"targets" : 7, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasDespacho.push({"targets" : 8, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasDespacho.push({"targets" : 9, "searchable" : true, "orderable" : true, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasDespacho.push({"targets" : 10, "searchable" : true, "orderable" : true, "visible" : true, "render"  : utilitario.formatearOrigenDespacho });
  
  jQuery.validator.addMethod('fechahora', function (value) {
	  return utilitario.validaformatoStringToDateHour(value);	  
  }, "Fecha Hora Valida");  
  jQuery.validator.addMethod('selectcheck', function (value) {
      return (value != '0' && value != '');
  }, "year required");
  moduloActual.reglasValidacionFormulario={
	cmpIdVehiculo:			{selectcheck: true },
	cmpIdClasificacion: 	{selectcheck: true },
	cmpNumeroVale: 			{required: true },
	cmpHoraInicio: 			{required: true },
	cmpHoraFin: 			{required: true },
	cmpIdProducto: 			{selectcheck: true },
	cmpIdContometro: 		{selectcheck: true },
	cmpIdTanque: 			{selectcheck: true },
  };

  moduloActual.mensajesValidacionFormulario={
    cmpIdVehiculo:  		{selectcheck: "El campo 'Vehiculo' es obligatorio" },
    cmpIdClasificacion: 	{selectcheck: "El campo 'Clasificacion' es obligatorio" },
    cmpNumeroVale: 			{required: "El campo 'Nro Vale' es obligatorio" },
    cmpHoraInicio: 			{required: "El campo 'Hora Inicio' es obligatorio" },
    cmpHoraFin: 			{required: "El campo 'Hora Fin' es obligatorio" },
	cmpIdProducto: 			{selectcheck: "El campo 'Producto' es obligatorio" },
	cmpIdContometro: 		{selectcheck: "El campo 'Contometro' es obligatorio" },
	cmpIdTanque: 			{selectcheck: "El campo 'Tanque' es obligatorio" },
  };

  moduloActual.inicializarCampos= function(){
	var ref=this;
	ref.archivosCargados=[];	
    ref.obj.cmpArchivo=$("#cmpArchivo");
    ref.obj.cmpArchivo.on("change",function(event){
    	ref.archivosCargados=event.target.files;
    	console.log(ref.archivosCargados);
    });
	
    this.obj.idOperacionSeleccionado =$("#idOperacionSeleccionado");
    this.obj.idClienteSeleccionado =$("#idClienteSeleccionado");
    this.obj.idEstacionSeleccionado =$("#idEstacionSeleccionado");
    this.obj.idJornadaSeleccionado =$("#idJornadaSeleccionado");
	  
    this.obj.clienteSeleccionado=$("#clienteSeleccionado");
    this.obj.operacionSeleccionado=$("#operacionSeleccionado");

    
    this.obj.filtroFechaJornada = $("#filtroFechaJornada");
    // Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaJornada.attr('data-fecha-actual');
    //PARA QUE COJA LAS ULTIMAS FECHAS CARGADAS
    var rangoSemana = utilitario.retornarfechaInicialFinal(fechaActual);
    this.obj.filtroFechaJornada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
    this.obj.filtroFechaJornada.daterangepicker({
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
    
    this.obj.filtroEstacion = $("#filtroEstacion");
	this.obj.filtroEstacion.select2();

	this.obj.idCliente = $("#idCliente");
	this.obj.filtroOperacion = $("#filtroOperacion");
	this.obj.filtroOperacion.select2();
	
	this.obj.filtroOperacion.on('change', function(e){
	   moduloActual.obj.idOperacionSeleccionado=$(this).val();
	   moduloActual.obj.operacionSeleccionado=$(this).find("option:selected").attr('data-nombre-operacion');
	   moduloActual.obj.clienteSeleccionado=$(this).find("option:selected").attr('data-nombre-cliente');
	   moduloActual.obj.filtroEstacion.select2("val", moduloActual.obj.filtroEstacion.attr("data-valor-inicial"));		
	   moduloActual.obj.ocultaContenedorTabla.show();
	   $.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: "./estacion/listar", 
		    dataType: 'json',
		    data: {filtroOperacion: moduloActual.obj.filtroOperacion.val()},	
		    success: function (respuesta) {
		    	if(respuesta.contenido.carga.length > 0){
		    		document.getElementById("filtroEstacion").innerHTML = "";
		    		for(var cont = 0; cont < respuesta.contenido.carga.length; cont++){
		    			var registro = respuesta.contenido.carga[cont];
		    			$('#filtroEstacion').append("<option value="+ registro.id +"> " + registro.nombre + "</option>");
		    			
		    		}
		    		moduloActual.obj.filtroEstacion.select2("val", respuesta.contenido.carga[0].id);
		    		$('#filtroEstacion').val(respuesta.contenido.carga[0].id);
		    		//moduloActual.obj.filtroEstacion.val(respuesta.contenido.carga[0].id);
		    		console.log(moduloActual.obj.filtroEstacion.val());
 		    	} else {
		    		var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
		    	    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, -1);
		    	    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
		  	        moduloActual.obj.filtroEstacion.empty().append(elemento2).val(-1).trigger('change');
		  	        $('#filtroEstacion').find("option:selected").val(-1);
		    	    moduloActual.obj.filtroEstacion.val(-1);
		    	}
		    },			    		    
		    error: function(xhr,estado,error) {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
		    }
		});
	   console.log(moduloActual.obj.filtroEstacion.val());
	   moduloActual.obj.ocultaContenedorTabla.hide();
	   e.preventDefault(); 
	});
	
	
	this.obj.filtroEstacion.on('change', function(e){
	   moduloActual.obj.idEstacionSeleccionado=$(this).val();
	   $.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: "./jornada/recuperar-ultimo-dia", 
		    dataType: 'json',
		    data: {
		    	idOperacion: moduloActual.obj.filtroOperacion.val(),
		    	filtroEstacion: moduloActual.obj.filtroEstacion.val()
		    },	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion("La estación no cuenta con jornadas.");
		    	} else {
		    		var valor = respuesta.valor;
		    		if(valor != null){
		    			var rangoSemana = utilitario.retornarfechaInicialFinal(valor);
			    	    moduloActual.obj.filtroFechaJornada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
		    		}
	    		}
		    },
		    error: function(xhr,estado,error) {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
		    }
		});
	   
	   moduloActual.modoEdicion=constantes.MODO_LISTAR;
	   moduloActual.listarRegistros();
	   e.preventDefault(); 
	});
    
	/*this.obj.clienteSeleccionado=$("#clienteSeleccionado");
    this.obj.operacionSeleccionado=$("#operacionSeleccionado");

	this.obj.filtroOperacion = $("#filtroOperacion");
	this.obj.filtroOperacion.on('change', function(e){
	   moduloActual.obj.idOperacionSeleccionado=$(this).val();
	   moduloActual.obj.operacionSeleccionado=$(this).find("option:selected").attr('data-nombre-operacion');
	   moduloActual.obj.clienteSeleccionado=$(this).find("option:selected").attr('data-nombre-cliente');
	   moduloActual.obj.filtroEstacion.select2("val", moduloActual.obj.filtroEstacion.attr("data-valor-inicial"));	
	   
	   e.preventDefault(); 
	});   
	 
	this.obj.filtroEstacion = $("#filtroEstacion");
	this.obj.filtroEstacion.tipoControl="select2";
    this.obj.cmpSelect2Estacion=$("#filtroEstacion").select2({
  	  ajax: {
  		    url: "./estacion/listar",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		      return {
  		    	valorBuscado: parametros.term, // search term
  		        page: parametros.page,
  		        paginacion:0,
  		        filtroOperacion: moduloActual.obj.filtroOperacion.find("option:selected").attr('data-idOperacion')
  		      };
  		    },
  		    processResults: function (respuesta, pagina) {
  		    	console.log("processResults");
  		    	var resultados= respuesta.contenido.carga;
  		    	console.log(resultados);
  		      return { results: resultados};
  		    },
  		    cache: true
  		  },
  		language: "es",
  		escapeMarkup: function (markup) { return markup; },
  		templateResult: function (registro) {
  			console.log("templateResult");
  			if (registro.loading) {
  				return "Buscando...";
  			}		    	
		        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
		    },
		    templateSelection: function (registro) {
		    	try {
		    		if(registro.nombre!=null){
		    			moduloActual.obj.nombreEstacion = registro.nombre;
		    			moduloActual.obj.idEstacion= registro.id;
		    		}
				} catch (e) {
					console.log(e.message);
				}		    	
		        return registro.nombre || registro.text;
		    },
    });

    this.obj.filtroFechaJornada = $("#filtroFechaJornada");
    //Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaJornada.attr('data-fecha-actual');
    console.log(" fechaActual -------> " + fechaActual);
  //PARA QUE COJA LAS ULTIMAS FECHAS CARGADAS
    var rangoSemana = utilitario.retornarfechaInicialFinal(fechaActual);
    //var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
    this.obj.filtroFechaJornada.val(utilitario.formatearFecha2Cadena(rangoSemana.fechaInicial) + " - " +utilitario.formatearFecha2Cadena(rangoSemana.fechaFinal));
    //Controles de filtro
    this.obj.filtroOperacion.select2();
    this.obj.filtroFechaJornada.daterangepicker({
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
    });*/

    //Campos de detalle de transporte
	this.obj.detalleIdDOperativo=$("#detalleIdDOperativo");
    this.obj.detalleCliente=$("#detalleCliente");
    this.obj.detalleOperacion=$("#detalleOperacion");
    this.obj.detalleEstacion=$("#detalleEstacion");
    this.obj.detalleFechaJornada=$("#detalleFechaJornada");
    
    //campos para formulario de importacion
    this.obj.cmpImportacionCliente=$("#cmpImportacionCliente");
	this.obj.cmpImportacionOperacion=$("#cmpImportacionOperacion");
	this.obj.cmpImportacionEstacion=$("#cmpImportacionEstacion");
	this.obj.cmpHoraAperturaTurno=$("#cmpHoraAperturaTurno");
	this.obj.cmpImportacionFechaJornada=$("#cmpImportacionFechaJornada");
    this.obj.cmpArchivoImportacion=$("#cmpArchivoImportacion");
    this.obj.cmpOperarioImportacion=$("#cmpOperarioImportacion");
    this.obj.cmpOperarioImportacion.tipoControl="select2";
    this.obj.cmpSelect2OperarioImportacion=$("#cmpOperarioImportacion").select2({
  	  ajax: {
  		    url: "./operario/listar",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		      return {
  		    	valorBuscado: parametros.term, // search term
  		        page: parametros.page,
  		        paginacion:0,
  		        filtroEstado: constantes.ESTADO_ACTIVO
  		      };
  		    },
  		    processResults: function (respuesta, pagina) {
  		      var resultados= respuesta.contenido.carga;
  		      return { results: resultados};
  		    },
  		    cache: true
  		  },
  		language: "es",
  		escapeMarkup: function (markup) { return markup; },
  		templateResult: function (registro) {
  			if (registro.loading) {
  				return "Buscando...";
  			}		    	
		        return "<div class='select2-user-result'>" + registro.nombreCompletoOperario + "</div>";
		    },
		    templateSelection: function (registro) {
		        return registro.nombreCompletoOperario || registro.text;
		    },
    });
    this.obj.cmpComentarioImportacion=$("#cmpComentarioImportacion");
    
    //campos para vista de importacion
    this.obj.vistaImportacionCliente=$("#vistaImportacionCliente");
	this.obj.vistaImportacionOperacion=$("#vistaImportacionOperacion");
	this.obj.vistaImportacionEstacion=$("#vistaImportacionEstacion");
	this.obj.vistaImportacionFechaJornada=$("#vistaImportacionFechaJornada");
    this.obj.vistaArchivoImportacion=$("#vistaArchivoImportacion");
    this.obj.vistaOperarioImportacion=$("#vistaOperarioImportacion");
    this.obj.vistaComentarioImportacion=$("#vistaComentarioImportacion");
    this.obj.vistaImportacionCreadoEl=$("#vistaImportacionCreadoEl");
    this.obj.vistaImportacionCreadoPor=$("#vistaImportacionCreadoPor");
    this.obj.vistaImportacionActualizadoPor=$("#vistaImportacionActualizadoPor");
    this.obj.vistaImportacionActualizadoEl=$("#vistaImportacionActualizadoEl");
    this.obj.vistaImportacionIpCreacion=$("#vistaImportacionIpCreacion");
    this.obj.vistaImportacionIpActualizacion=$("#vistaImportacionIpActualizacion");
    
    //Campos de Formulario Principal
    this.obj.cmpFormularioCliente=$("#cmpFormularioCliente");
    this.obj.cmpFormularioOperacion=$("#cmpFormularioOperacion");
    this.obj.cmpFormularioEstacion=$("#cmpFormularioEstacion");
    this.obj.cmpFormularioFechaJornada=$("#cmpFormularioFechaJornada");
    this.obj.cmpIdPropietario=$("#cmpIdPropietario");
    this.obj.cmpIdVehiculo=$("#cmpIdVehiculo");
    this.obj.cmpIdVehiculo.tipoControl="select2";
    this.obj.cmpSelect2Vehiculo=$("#cmpIdVehiculo").select2({
    	  ajax: {
  	    url: "./vehiculo/listar",
  	    dataType: 'json',
  	    delay: 250,
  	    data: function (parametros) {
  	      return {
  	    	valorBuscado: parametros.term, // search term
  	        page: parametros.page,
  	        paginacion:0,
  	        filtroEstado: constantes.ESTADO_ACTIVO
  	      };
  	    },
  	    processResults: function (respuesta, pagina) {
  	      var resultados= respuesta.contenido.carga;
  	      return { results: resultados};
  	    },
  	    cache: true
  	  },
  	language: "es",
  	escapeMarkup: function (markup) { return markup; },
  	templateResult: function (registro) {
  		if (registro.loading) {
  			return "Buscando...";
  		}		    	
  	        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
  	    },
  	    templateSelection: function (registro) {
  	    	try{
  	    		moduloActual.obj.cmpIdPropietario.val(registro.propietario.razonSocial);
  	    	} catch(error) {
  	          console.log(error.message);
  	        }
  	        return registro.descripcion || registro.text;
  	    },	
      });
    this.obj.cmpKmHorometro=$("#cmpKmHorometro");
    this.obj.cmpNumeroVale=$("#cmpNumeroVale");

    this.obj.cmpHoraInicio=$("#cmpHoraInicio");
    this.obj.cmpHoraInicio.inputmask("h:s:s");
    
    this.obj.cmpHoraFin=$("#cmpHoraFin");
    this.obj.cmpHoraFin.inputmask("h:s:s");
    
    this.obj.cmpIdClasificacion=$("#cmpIdClasificacion");
    this.obj.cmpIdProducto=$("#cmpIdProducto");
    this.obj.cmpIdProducto.tipoControl="select2";
    this.obj.cmpSelect2Producto=$("#cmpIdProducto").select2({
    	  ajax: {
  	    url: "./producto/listarPorOperacion",
  	    dataType: 'json',
  	    delay: 250,
  	    data: function (parametros) {
  	      return {
  	    	valorBuscado: parametros.term, // search term
  	        page: parametros.page,
  	        paginacion:0,
  	        filtroEstado: constantes.ESTADO_ACTIVO,
  	        filtroOperacion: moduloActual.obj.filtroOperacion.val(),
  	        filtroEstacion: moduloActual.obj.filtroEstacion.val()
  	      };
  	    },
  	    processResults: function (respuesta, pagina) {
  	      var resultados= respuesta.contenido.carga;
  	      return { results: resultados};
  	    },
  	    cache: true
  	  },
  	language: "es",
  	escapeMarkup: function (markup) { return markup; },
  	templateResult: function (registro) {
  		if (registro.loading) {
  			return "Buscando...";
  		}		    	
  	        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
  	    },
  	    templateSelection: function (registro) {
  	    	try{
  	    		$("#cmpIdContometro").prop('disabled', false);
  	    		$("#cmpIdTanque").prop('disabled', false);
  	    	   var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  	           elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
  	           elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

  	           moduloActual.obj.cmpIdContometro.select2("val", null);
  	           moduloActual.obj.cmpIdContometro.empty().append(elemento1).val(0).trigger('change');
  	           
  	           if($(cmpIdProducto).val() > 0){
  	        	   moduloActual.seleccionarTanque();
  	           }

  	    	} catch (error){
  	    		
  	    	}
  	        return registro.nombre || registro.text;
  	    },	
      });
    this.obj.cmpIdContometro=$("#cmpIdContometro");
    this.obj.cmpIdContometro.tipoControl="select2";
    this.obj.cmpSelect2Contometro=$("#cmpIdContometro").select2({
    	  ajax: {
  	    url: "./contometroJornada/listar",
  	    dataType: 'json',
  	    delay: 250,
  	    data: function (parametros) {
  	      return {
  	    	valorBuscado: parametros.term, // search term
  	        page: parametros.page,
  	        paginacion:0,
  	        filtroEstado: constantes.ESTADO_ACTIVO,
  	        idJornada: parseInt(moduloActual.obj.idJornadaSeleccionado),
  	        filtroEstacion:  moduloActual.obj.filtroEstacion.val(),
  	        filtroProducto: moduloActual.obj.cmpIdProducto.val()
  	      };
  	    },
  	    processResults: function (respuesta, pagina) {
  	      var resultados= respuesta.contenido.carga;
  	      return { results: resultados};
  	    },
  	    cache: true
  	  },
  	language: "es",
  	escapeMarkup: function (markup) { return markup; },
  	templateResult: function (registro) {
  		if (registro.loading) {
  			return "Buscando...";
  		}		    	
  	        return "<div class='select2-user-result'>" + registro.descripcionContometro + "</div>";
  	    },
  	    templateSelection: function (registro) {
  	        return registro.descripcionContometro || registro.text;
  	    },	
      });
    this.obj.cmpIdTanque=$("#cmpIdTanque");
    this.obj.cmpIdTanque.tipoControl="select2";
    this.obj.cmpSelect2Tanque=$("#cmpIdTanque").select2({
    	  ajax: {
  	    url: "./tanqueJornada/listar",
  	    dataType: 'json',
  	    delay: 250,
  	    data: function (parametros) {
  	      return {
  	    	valorBuscado: parametros.term, // search term
  	        page: parametros.page,
  	        paginacion:0,
  	        filtroEstado: constantes.ESTADO_ACTIVO,
  	        filtroEstacion:  moduloActual.obj.filtroEstacion.val(),
  	        idJornada: parseInt(moduloActual.obj.idJornadaSeleccionado),
  	        filtroProducto: moduloActual.obj.cmpIdProducto.val(),
  	        estadoDespachando:constantes.ESTADO_DESPACHANDO
  	      };
  	    },
  	    processResults: function (respuesta, pagina) {
  	      var resultados= respuesta.contenido.carga;
  	      return { results: resultados};
  	    },
  	    cache: true
  	  },
  	language: "es",
  	escapeMarkup: function (markup) { return markup; },
  	templateResult: function (registro) {
  		if (registro.loading) {
  			return "Buscando...";
  		}		    	
  	        return "<div class='select2-user-result'>" + registro.descripcionTanque + "</div>";
  	    },
  	    templateSelection: function (registro) {
  	        return registro.descripcionTanque || registro.text;
  	    },	
      });
    this.obj.cmpVolObservado=$("#cmpVolObservado");
    //this.obj.cmpVolObservado.inputmask("99999999.99");
    this.obj.cmpVolObservado.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.cmpLecturaInicial=$("#cmpLecturaInicial");
    this.obj.cmpLecturaFinal=$("#cmpLecturaFinal");
    moduloActual.obj.cmpLecturaInicial.val(0);
    moduloActual.obj.cmpLecturaFinal.val(0);
    this.obj.cmpLecturaInicial.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.cmpLecturaFinal.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
    
    
    moduloActual.obj.cmpLecturaInicial.on("input",function(e){
    	var lecturaFinal =  moduloActual.eliminaSeparadorComa(moduloActual.obj.cmpLecturaFinal.val());
    	var lecturaInicial = moduloActual.eliminaSeparadorComa(moduloActual.obj.cmpLecturaInicial.val());
    	var totalVolumen = parseFloat(lecturaFinal - lecturaInicial); 
    	moduloActual.obj.cmpVolObservado.val(totalVolumen);
    });
    
    moduloActual.obj.cmpLecturaFinal.on("input", function(e){
    	var lecturaFinal =  moduloActual.eliminaSeparadorComa(moduloActual.obj.cmpLecturaFinal.val());
    	var lecturaInicial = moduloActual.eliminaSeparadorComa(moduloActual.obj.cmpLecturaInicial.val());
    	var totalVolumen = parseFloat(lecturaFinal - lecturaInicial); 
    	moduloActual.obj.cmpVolObservado.val(totalVolumen);
    });
    
    /*moduloActual.obj.cmpLecturaInicial.on("keypress", function(e){
    	if (e.which == 13) { //valido que se presione la tecla enter
    		var totalVolumen = parseFloat(moduloActual.obj.cmpLecturaFinal.val()) - parseInt(moduloActual.obj.cmpLecturaInicial.val());  
        	moduloActual.obj.cmpVolObservado.val(totalVolumen);
    	}
    });
    
    moduloActual.obj.cmpLecturaFinal.on("keypress", function(e){
   	  	if (e.which == 13) {  //valido que se presione la tecla enter
   	  	var totalVolumen = parseFloat(moduloActual.obj.cmpLecturaFinal.val()) - parseInt(moduloActual.obj.cmpLecturaInicial.val()); 
	    	moduloActual.obj.cmpVolObservado.val(totalVolumen);
      }
    });*/
    
    moduloActual.obj.cmpHoraInicio.on("change",function(e){
    	if(moduloActual.obj.cmpHoraFin.val().length == 0){
    		moduloActual.obj.cmpHoraFin.val(moduloActual.obj.cmpHoraInicio.val());
    	}
    });
    
    this.obj.cmpFactor=$("#cmpFactor");
    //this.obj.cmpFactor.inputmask('decimal', {digits: 6,integerDigits:4, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.cmpAPI60=$("#cmpAPI60");
    this.obj.cmpAPI60.inputmask("99.9");
    this.obj.cmpAPI60.on("change",function(){
      moduloActual.calcularFactor();
    });
    
    this.obj.cmpTemperatura=$("#cmpTemperatura");
    this.obj.cmpTemperatura.inputmask("99.9");
    this.obj.cmpTemperatura.on("change",function(){
        moduloActual.calcularFactor();
      });
    
    this.obj.cmpVolumen60=$("#cmpVolumen60");
    this.obj.cmpVolumen60.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    //this.obj.cmpVolumen60.inputmask("99999999.99");
    
    this.obj.cmpTipoRegistro=$("#cmpTipoRegistro");

    //Campos vista Despacho
    this.obj.vistaFormularioCliente=$("#vistaFormularioCliente");
    this.obj.vistaFormularioOperacion=$("#vistaFormularioOperacion");
    this.obj.vistaFormularioEstacion=$("#vistaFormularioEstacion");
    this.obj.vistaFormularioFechaJornada=$("#vistaFormularioFechaJornada");
    this.obj.vistaIdPropietario=$("#vistaIdPropietario");
    this.obj.vistaIdVehiculo=$("#vistaIdVehiculo");
    this.obj.vistaKmHorometro=$("#vistaKmHorometro");
    this.obj.vistaNumeroVale=$("#vistaNumeroVale");
    this.obj.vistaFechaInicio=$("#vistaFechaInicio");
    this.obj.vistaFechaFin=$("#vistaFechaFin");
    this.obj.vistaIdClasificacion=$("#vistaIdClasificacion");
    this.obj.vistaIdProducto=$("#vistaIdProducto");
    this.obj.vistaIdContometro=$("#vistaIdContometro");
    this.obj.vistaIdTanque=$("#vistaIdTanque");
    this.obj.vistaVolObservado=$("#vistaVolObservado");
    this.obj.vistaLecturaInicial=$("#vistaLecturaInicial");
    this.obj.vistaLecturaFinal=$("#vistaLecturaFinal");
    this.obj.vistaFactor=$("#vistaFactor");
    this.obj.vistaAPI60=$("#vistaAPI60");
    this.obj.vistaTemperatura=$("#vistaTemperatura");
    this.obj.vistaVolumen60=$("#vistaVolumen60");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
	this.obj.vistaCreadoPor=$("#vistaCreadoPor");
	this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
	this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
	this.obj.vistaIpCreacion=$("#vistaIpCreacion");
	this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
	
	
  };
  
  moduloActual.eliminaSeparadorComa = function(numeroFloat){
	var parametros = numeroFloat.split(',');
  	var retorno =  new String(parametros[0]);
  	if(parametros[1] != null){
  		retorno =  new String(parametros[0] + parametros[1]);
  	}
  	if(parametros[2] != null){
  		retorno =  new String(parametros[0] + parametros[1] + parametros[2]);
  	}
  	if(parametros[3] != null){
  		retorno =  new String(parametros[0] + parametros[1] + parametros[2] + parametros[3]);
  	}
  	if(parametros[4] != null){
  		retorno =  new String(parametros[0] + parametros[1] + parametros[2] + parametros[3] + parametros[4]);
  	}
  	return retorno;
  };
  
  moduloActual.seleccionarTanque = function(){
	  var referenciaModulo=this;
	  try {
       	$.ajax({
  		type: constantes.PETICION_TIPO_GET,
  	    url: "./tanqueJornada/listar",
  	    contentType: referenciaModulo.TIPO_CONTENIDO, 
  	    data: {
  	        filtroEstado: constantes.ESTADO_ACTIVO,
  	        filtroEstacion:  referenciaModulo.obj.filtroEstacion.val(),
  	        idJornada: parseInt(referenciaModulo.obj.idJornadaSeleccionado),
  	        filtroProducto: $(cmpIdProducto).val(),
  	        estadoDespachando:constantes.ESTADO_DESPACHANDO
  	    },
  	    success: function(respuesta) {
          if (!respuesta.estado) {
        	 
          } else {
        	  var tanquejornada = respuesta.contenido.carga.length;
        	  if(tanquejornada == 1){
        		var registro = respuesta.contenido.carga[0];
        		var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
        			elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, registro.tanque.id);
        			elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.descripcionTanque);
                moduloActual.obj.cmpIdTanque.empty().append(elemento2).val(registro.tanque.id).trigger('change');
                $(cmpIdTanque).prop('disabled', true);
        	  };
            };
          },
            error: function(xhr,estado,error) {
            referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
          }
        }); 
      } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
      };
  }
  
  moduloActual.calcularFactor= function(){
   var ref=this;
   //var temp = idElemento.split("_");
   var parametros={};
   var temperatura =  ref.obj.cmpTemperatura.val();
   var apiCorregido = ref.obj.cmpAPI60.val();
   var volObs = moduloActual.eliminaSeparadorComa(ref.obj.cmpVolObservado.val());
   var camposInvalidos = 0;
   if ((typeof temperatura == "undefined") || (temperatura == null) || (temperatura == '')){
    camposInvalidos++;
   }
   
   if ((typeof apiCorregido == "undefined") || (apiCorregido == null) || (apiCorregido == '')){
    camposInvalidos++;
   }
     
   if (camposInvalidos ==0){
    parametros.apiCorregido=apiCorregido;
    parametros.temperatura=temperatura;
    parametros.volumenObservado=volObs;
    $("#ocultaContenedorFormulario").show();
     $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: "../admin/formula/recuperar-factor-correccion", 
      contentType: ref.TIPO_CONTENIDO, 
      data: parametros, 
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          var registro = respuesta.contenido.carga[0];
          console.log(registro);
          var factorCorrecion = parseFloat(registro.factorCorreccion);
          ref.obj.cmpFactor.val(factorCorrecion);
          ref.obj.cmpVolumen60.val(registro.volumenCorregido);
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);         
        }
        $("#ocultaContenedorFormulario").hide();
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error);
        $("#ocultaContenedorFormulario").hide();   
      }
    });
   }   
  };
	  
  moduloActual.datosCabecera= function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.idOperacionSeleccionado = referenciaModulo.obj.filtroOperacion.val();
	  referenciaModulo.obj.idClienteSeleccionado = $(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-idCliente');
	  referenciaModulo.obj.idEstacionSeleccionado = referenciaModulo.obj.filtroEstacion.val();
	  referenciaModulo.obj.idJornadaSeleccionado = referenciaModulo.obj.idJornadaSeleccionado;
	  if(referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_NUEVO_DESPACHO){
		  moduloActual.obj.cmpTipoRegistro = "";
	  }

	  //para pantalla de detalle de despacho
	  moduloActual.obj.detalleCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
	  moduloActual.obj.detalleOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
	  moduloActual.obj.detalleEstacion.text(referenciaModulo.obj.estacionSeleccionado);
	  moduloActual.obj.detalleFechaJornada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));

  	  //para importar
	  this.obj.cmpImportacionCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
	  this.obj.cmpImportacionOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
	  this.obj.cmpImportacionEstacion.text(moduloActual.obj.estacionSeleccionado);
	  this.obj.cmpImportacionFechaJornada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));

	  //para formulario
  	  this.obj.cmpFormularioCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
  	  this.obj.cmpFormularioOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
  	  this.obj.cmpFormularioEstacion.text(moduloActual.obj.estacionSeleccionado);
  	  this.obj.cmpFormularioFechaJornada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));
      
  	  referenciaModulo.recuperarTurno();
  	  
  	  if(referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_NUEVO_DESPACHO){
  		  
  		try {
  		  	$.ajax({
  		  		type: constantes.PETICION_TIPO_GET,
  		  	    url: "./producto/listarPorOperacion",
  		  	    contentType: referenciaModulo.TIPO_CONTENIDO, 
  		  	    data: {
  		  	        filtroEstado: constantes.ESTADO_ACTIVO,
  		  	        filtroOperacion: moduloActual.obj.filtroOperacion.val(),
  		  	        filtroEstacion: moduloActual.obj.filtroEstacion.val()
  		  	    },
  		  	    success: function(respuesta) {
  		          if (!respuesta.estado) {
  		        	 
  		          } else {
  		        	  var contenido = respuesta.contenido.carga.length;
  		        	  if(contenido == 1){
  		        		var reg = respuesta.contenido.carga[0];
  		        		var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  		                elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, reg.id);
  		                elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, reg.nombre);
  		                moduloActual.obj.cmpIdProducto.empty().append(elemento1).val(reg.id).trigger('change');
  		                $(cmpIdProducto).val(reg.id);
  		                $(cmpIdProducto).prop('disabled', true);

  		                if($(cmpIdProducto).val() > 0){
  		                	moduloActual.seleccionarTanque();
  		                }
  		                moduloActual.obj.cmpIdContometro.select2("val", null);

		        	  };
		            }
		          },

		          error: function(xhr,estado,error) {
		            referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		          }
	        	}); 
		      } catch(error){
		      referenciaModulo.mostrarDepuracion(error.message);
		      }

  			      
  			  	/*try {
  		           	$.ajax({
  	   	  	  		type: constantes.PETICION_TIPO_GET,
  	   	  	  	    url: "./tanqueJornada/listar",
  	   	  	  	    contentType: referenciaModulo.TIPO_CONTENIDO, 
  	   	  	  	    data: {
  	   		  	        filtroEstado: constantes.ESTADO_ACTIVO,
  	   		  	        filtroEstacion:  moduloActual.obj.filtroEstacion.val(),
  	   		  	        idJornada: parseInt(moduloActual.obj.idJornadaSeleccionado),
  	   		  	        filtroProducto: $(cmpIdProducto).val(),
  	   		  	        estadoDespachando:constantes.ESTADO_DESPACHANDO
  	   	  	  	    },
  	   	  	  	    success: function(respuesta) {
  	   	  	          if (!respuesta.estado) {
  	   	  	        	 
  	   	  	          } else {
  	   	  	        	  var tanquejornada = respuesta.contenido.carga.length;
  	   	  	        	  if(tanquejornada == 1){
  	   	  	        		  console.log(respuesta.contenido.carga[0]);
  	   	  	        		var registro = respuesta.contenido.carga[0];
  	   	  	        		var elemento2 =constantes.PLANTILLA_OPCION_SELECTBOX;
  	   	  	        			elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR, registro.id);
  	   	  	        			elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.descripcionTanque);
  	   	  	                moduloActual.obj.cmpIdTanque.empty().append(elemento2).val(registro.id).trigger('change');
  	   	  	                $(cmpIdTanque).prop('disabled', true);
  	   	  	        	  } 
  	   	  	            }
  	   	  	          },
  	   	  	            error: function(xhr,estado,error) {
  	   	  	            referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
  	   	  	          }
  	   	  	        }); 
  	   	  	      } catch(error){
  	   	  	      referenciaModulo.mostrarDepuracion(error.message);
  	   	  	      }*/
  		  
  		  
  		//formulario.find("input[elemento-grupo='horaMuestra']").val(utilitario.formatearTimestampToString(registro.muestreo[indice].horaMuestreo));
  		//referenciaModulo.obj.cmpFechaInicio.val(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));
  		//referenciaModulo.obj.cmpFechaFin.val(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));
  		
  		moduloActual.obj.cmpHoraInicio.val("");
  		moduloActual.obj.cmpHoraFin.val("");
  		
  	  }
  };

  moduloActual.limpiarFormularioPrincipal = function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.frmPrincipal[0].reset();

	  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

      moduloActual.obj.cmpIdVehiculo.select2("val", null);
      moduloActual.obj.cmpIdProducto.select2("val", null);
      moduloActual.obj.cmpIdContometro.select2("val", null);
      moduloActual.obj.cmpIdTanque.select2("val", null);

      moduloActual.obj.cmpIdVehiculo.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpIdProducto.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpIdContometro.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpIdTanque.empty().append(elemento1).val(0).trigger('change');
      
      $("#cmpIdContometro").prop('disabled', true);
	  $("#cmpIdTanque").prop('disabled', true);
  };

  moduloActual.llenarFormulario = function(registro){
	  var referenciaModulo=this;
	  this.idRegistro = referenciaModulo.idDespacho;

	  var elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.vehiculo.id);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.vehiculo.descripcion);
	  this.obj.cmpIdVehiculo.empty().append(elemento1).val(registro.vehiculo.id).trigger('change');
	 
	  elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.producto.id);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.producto.nombre);
	  this.obj.cmpIdProducto.empty().append(elemento1).val(registro.producto.id).trigger('change');
	  
	  elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.contometro.id);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.contometro.alias);
	  this.obj.cmpIdContometro.empty().append(elemento1).val(registro.contometro.id).trigger('change');
	  
	  elemento1 = constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, registro.tanque.id);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.tanque.descripcion);
	  this.obj.cmpIdTanque.empty().append(elemento1).val(registro.tanque.id).trigger('change');
	  this.obj.cmpTipoRegistro = registro.tipoRegistro;
	  this.obj.cmpIdClasificacion.val(registro.clasificacion);
	  this.obj.cmpIdPropietario.val(registro.vehiculo.propietario.razonSocial);
	  this.obj.cmpKmHorometro.val(registro.kilometroHorometro);
	  this.obj.cmpNumeroVale.val(registro.numeroVale);
	  //this.obj.cmpFechaInicio.val(utilitario.formatearTimestampToString(registro.fechaHoraInicio));
	  //this.obj.cmpFechaFin.val(utilitario.formatearTimestampToString(registro.fechaHoraFin));
	  this.obj.cmpHoraInicio.val(utilitario.formatearTimestampToStringSoloHora(registro.fechaHoraInicio));
	  this.obj.cmpHoraFin.val(utilitario.formatearTimestampToStringSoloHora(registro.fechaHoraFin));
	  this.obj.cmpVolObservado.val(registro.volumenObservado.toFixed(2));
	  this.obj.cmpVolumen60.val(registro.volumenCorregido.toFixed(2));
	
	  this.obj.cmpLecturaInicial.val(registro.lecturaInicial);
	  this.obj.cmpLecturaFinal.val(registro.lecturaFinal);
	  this.obj.cmpFactor.val(registro.factorCorreccion.toFixed(6));	  
	  this.obj.cmpAPI60.val(registro.apiCorregido.toFixed(1));
  	  this.obj.cmpTemperatura.val(registro.temperatura.toFixed(2));
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
	    //datos para el despacho
	    eRegistro.id = parseInt(referenciaModulo.idDespacho);
	    eRegistro.idJornada = parseInt(referenciaModulo.obj.idJornadaSeleccionado);
	    eRegistro.idVehiculo = parseInt(referenciaModulo.obj.cmpIdVehiculo.val());
	    eRegistro.kilometroHorometro = parseInt(referenciaModulo.obj.cmpKmHorometro.val());
	    eRegistro.numeroVale = parseInt(referenciaModulo.obj.cmpNumeroVale.val());
	    eRegistro.clasificacion = referenciaModulo.obj.cmpIdClasificacion.val();
	    eRegistro.idProducto = parseInt(referenciaModulo.obj.cmpIdProducto.val());
	    eRegistro.idTanque = parseInt(referenciaModulo.obj.cmpIdTanque.val());
	    eRegistro.idContometro = parseInt(referenciaModulo.obj.cmpIdContometro.val());
	    eRegistro.estado = parseInt(constantes.ESTADO_ACTIVO);
	    eRegistro.tipoRegistro = moduloActual.obj.cmpTipoRegistro;
	    
	    eRegistro.fechaHoraInicio = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFormularioFechaJornada.text() + " " + referenciaModulo.obj.cmpHoraInicio.val());
	    eRegistro.fechaHoraFin = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFormularioFechaJornada.text() + " " + referenciaModulo.obj.cmpHoraFin.val());
	    
	    //eRegistro.fechaHoraInicio = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFechaInicio.val());
	    //eRegistro.fechaHoraFin = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFechaFin.val());
	    eRegistro.factorCorreccion = parseFloat(referenciaModulo.obj.cmpFactor.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.apiCorregido = parseFloat(referenciaModulo.obj.cmpAPI60.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.temperatura = parseFloat(referenciaModulo.obj.cmpTemperatura.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.volumenObservado = parseFloat(referenciaModulo.obj.cmpVolObservado.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.volumenCorregido = parseFloat(referenciaModulo.obj.cmpVolumen60.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.lecturaInicial = parseFloat(referenciaModulo.obj.cmpLecturaInicial.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.lecturaFinal = parseFloat(referenciaModulo.obj.cmpLecturaFinal.val().replaceAll(moduloActual.SEPARADOR_MILES,""));
        console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

//========================== cntVistaImportacion ============================================================ 
  moduloActual.llenarDetallesImportacion = function(registro){
	var referenciaModulo=this;
	this.obj.vistaImportacionCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
	this.obj.vistaImportacionOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
	this.obj.vistaImportacionEstacion.text(moduloActual.obj.nombreEstacion);
	this.obj.vistaImportacionFechaJornada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));
	this.obj.vistaArchivoImportacion.text(registro.nombreArchivo);
	this.obj.vistaOperarioImportacion.text(registro.operario.nombreCompletoOperario);
	this.obj.vistaComentarioImportacion.text(registro.comentario);
	this.obj.vistaImportacionCreadoEl.text(registro.fechaCreacion);
	this.obj.vistaImportacionCreadoPor.text(registro.usuarioCreacion);
	this.obj.vistaImportacionActualizadoPor.text(registro.usuarioActualizacion);
	this.obj.vistaImportacionActualizadoEl.text(registro.fechaActualizacion);
	this.obj.vistaImportacionIpCreacion.text(registro.ipCreacion);
	this.obj.vistaImportacionIpActualizacion.text(registro.ipActualizacion);
  };		

//========================== cntVistaFormulario ============================================================ 
  moduloActual.llenarDetalles = function(registro){
	var referenciaModulo=this;
	this.obj.vistaFormularioCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
	this.obj.vistaFormularioOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
	this.obj.vistaFormularioEstacion.text(moduloActual.obj.nombreEstacion);
	this.obj.vistaFormularioFechaJornada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaJornadaSeleccionado));
	this.obj.vistaIdPropietario.text(registro.vehiculo.propietario.razonSocial);
	this.obj.vistaIdVehiculo.text(registro.vehiculo.descripcion);
	this.obj.vistaKmHorometro.text(registro.kilometroHorometro);
	this.obj.vistaNumeroVale.text(registro.numeroVale);
	this.obj.vistaFechaInicio.text(utilitario.formatearTimestampToString(registro.fechaHoraInicio));
    this.obj.vistaFechaFin.text(utilitario.formatearTimestampToString(registro.fechaHoraFin));
    if(registro.clasificacion == constantes.CLASIFICACION_TRANSFERIDO){
    	this.obj.vistaIdClasificacion.text(constantes.CLASIFICACION_TRANSFERIDO_TEXT);
    } else{
    	this.obj.vistaIdClasificacion.text(constantes.CLASIFICACION_RECIRCULADO_TEXT);
    }
	this.obj.vistaIdProducto.text(registro.producto.nombre);
	this.obj.vistaIdContometro.text(registro.contometro.alias);
	this.obj.vistaIdTanque.text(registro.tanque.descripcion);
	this.obj.vistaVolObservado.text(registro.volumenObservado);
	this.obj.vistaLecturaInicial.text(registro.lecturaInicial);
	this.obj.vistaLecturaFinal.text(registro.lecturaFinal);
	this.obj.vistaFactor.text(registro.factorCorreccion);
	this.obj.vistaAPI60.text(registro.apiCorregido);
	this.obj.vistaTemperatura.text(registro.temperatura);
	this.obj.vistaVolumen60.text(registro.volumenCorregido.toFixed(2));
	this.obj.vistaCreadoEl.text(registro.fechaCreacion);
	this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
	this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
	this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
	this.obj.vistaIpCreacion.text(registro.ipCreacion);
	this.obj.vistaIpActualizacion.text(registro.ipActualizacion);

	
  };
 moduloActual.recuperaExtension = function(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
 };
  moduloActual.botonGuardarImportacion = function(){	  
		var ref=this;		
		//var idTanque =$("#cmpTanqueImportar").attr("data-id");
		var idJornada =ref.obj.idJornadaSeleccionado;
		var idOperario =ref.obj.cmpOperarioImportacion.val();
		var comentario =ref.obj.cmpComentarioImportacion.val();
		try {	
			if (ref.validarCargaArchivo()){
				$("#ocultaContenedorImportacion").show();
			   	var formularioDatos = new FormData();  
			   	formularioDatos.append('file',ref.archivosCargados[0]);		
				$.ajax({
				    type: "post",
				    enctype: 'multipart/form-data',
				    url: moduloActual.URL_CARGAR_ARCHIVO+"/"+idJornada+"/"+idOperario+"/"+comentario, 
		            data: formularioDatos,
		            cache: false,
		            contentType: false,
		            processData: false,		                
				    success: function(respuesta) {
				    	$("#ocultaContenedorImportacion").hide();
				    	if(!respuesta.estado){
				    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);				    		
				    	}else{
				    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,"La importaci\u00f3n de registros fue exitosa");
				    		moduloActual.actualizarDetalle();
				    	}				    					    	
				    },			    
				    error: function() {
				    }
				});
			}
		} catch(error){
			$("#ocultaContenedorImportacion").hide();
			console.log(error);
		}
  };
	moduloActual.validarCargaArchivo = function(){
		console.log("validarCargaArchivo");
		var respuesta = true;
		var ref=this;		
		if (typeof ref.archivosCargados[0] == 'undefined'){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"No ha seleccionado el archivo a importar");
			respuesta = false;
			return respuesta;
		}
		var nombreArchivo = ref.archivosCargados[0].name.toLowerCase();
		if (!ref.recuperaExtension(nombreArchivo,'csv')) {
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Solo se puede subir archivos en formato CSV");
			respuesta = false;
			return respuesta;
		}
		
		if (ref.archivosCargados[0].size >= moduloActual.TAMANO_MAXIMO_ARCHIVO){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"El limite por archivo es de 2 MB");
			respuesta = false;
			return respuesta;
		}
		if (ref.obj.cmpOperarioImportacion.val()<1){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"No ha seleccionado el operario");
			respuesta = false;
			return respuesta;
		}
		if (ref.obj.cmpComentarioImportacion.val().length == 0){
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Ingrese un comentario a la carga");
			respuesta = false;
			return respuesta;
		}
		console.log("termina de validarCargaArchivo");
		return respuesta;
	};

	moduloActual.validarFormulario = function(retorno){
		referenciaModulo = this;
		retorno = true;
		try{
			var inicio = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFormularioFechaJornada.text() + " " + referenciaModulo.obj.cmpHoraInicio.val());
			var fin = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFormularioFechaJornada.text() + " " + referenciaModulo.obj.cmpHoraFin.val());
			var turno = utilitario.formatearStringToDateHour(referenciaModulo.obj.cmpFormularioFechaJornada.text() + " " + referenciaModulo.obj.cmpHoraAperturaTurno.text());
			if(fin.getTime() < inicio.getTime()){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La hora Fin no puede ser menor a la Hora inicio. Favor verifique.");
		    	referenciaModulo.obj.ocultaContenedorFormulario.hide();
		    	retorno = false;
		    	return retorno;
			}
			if(inicio.getTime() < turno.getTime()){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La hora Inicio no puede ser menor a la Hora de apertura del turno. Favor verifique.");
		    	referenciaModulo.obj.ocultaContenedorFormulario.hide();
		    	retorno = false;
		    	return retorno;
			}
		}
		catch(error){
		      console.log(error.message);
		}
		return retorno;
	};
	
  moduloActual.inicializar();
});

