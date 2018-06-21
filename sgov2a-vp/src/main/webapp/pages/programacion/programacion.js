$(document).ready(function(){
  var moduloActual = new moduloProgramacion();
  moduloActual.urlBase='programacion';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR_FACTOR="../admin/formula/recuperar-factor-correccion";
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_RECUPERAR_PLANIFICACION = moduloActual.urlBase +'/recuperar-planificacion';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_COMPLETAR = moduloActual.urlBase + '/completarProgramacion';
  moduloActual.URL_LISTAR_TRANSPORTES = moduloActual.urlBase + '/listarTransportes';
  moduloActual.URL_RECUPERAR_DETALLES_TRANSPORTE = moduloActual.urlBase + '/recuperarDetallesTransporte';
  moduloActual.URL_ACTUALIZAR_PESAJE = moduloActual.urlBase + '/actualizarPesaje';
  moduloActual.URL_LISTAR_ASIGNACION_TRANSPORTES = moduloActual.urlBase + '/listarAsignacionTransportes';
  moduloActual.URL_RECUPERAR_X_REGISTRO_Y_TIPO = './evento/recuperarXregistroYtipo';  
  moduloActual.URL_COMENTAR = moduloActual.urlBase + '/comentar';
//7000001924
  moduloActual.obj.cisterna= {
    listado:[],
    registrarTodo: function(lst){
        var __t=this;
        var szL = lst?lst.length:0;
        var ix=0;
        for(;ix<szL;ix++){
            __t.registrar(lst[ix]);
        }
    },
    registrar: function(item){
        var __t=this;
        if (item==null){
            return false;
        }
        var szL = __t.listado.length;
        var idx=0;
        for(;idx<szL;idx++){
            if(item.id == __t.listado[idx].id){
                return false;
            }
        }
        __t.listado.push(item);
        return true;
    },
    buscar: function(idCisterna){
        var __t=this;
        var szL = __t.listado.length;
        var idx=0;
        for(;idx<szL;idx++){
            if(__t.listado[idx].id==idCisterna){
                return __t.listado[idx];
            }
        }
        return null;
    },
    cargar_cisternas: function(vnum){
        var __t=this;
    	var respuesta= $.ajax({
    			url: "./cisterna/recuperarPorTransportista", 
    			dataType: 'json',
    			data: {idTransportista: vnum,txt: ''} ,
    			cache: false,
    			async: false
    		}).responseJSON;
		var resultados= respuesta.contenido.carga;
		__t.registrarTodo(resultados);
    },
    es_vacio: function(){
    	var __t=this;
    	if(__t.listado.length<=0){
    		return true;
    	}else{
    		return false;
    	}
    }
  };

  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'});  //Target1
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'}); //Target2
  moduloActual.columnasGrilla.push({ "data": 'fechaEstimadaCarga'}); //Target3
  moduloActual.columnasGrilla.push({ "data": 'totalCisternasPlanificados'}); //Target4
  moduloActual.columnasGrilla.push({ "data": 'totalCisternasProgramados'}); //Target5
  moduloActual.columnasGrilla.push({ "data": 'actualizadoEl'}); //Target6
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'}); //Target7
  moduloActual.columnasGrilla.push({ "data": 'estado'}); //Target8  
  moduloActual.columnasGrilla.push({ "data": 'operacion.nombre'});//Target9
  moduloActual.columnasGrilla.push({ "data": 'operacion.cliente.razonSocial'});//Target10
  moduloActual.columnasGrilla.push({ "data": 'operacion.cliente.id'});//Target11
  moduloActual.columnasGrilla.push({ "data": 'idOperacion'});//Target12
  moduloActual.columnasGrilla.push({ "data": 'operacion.plantaDespacho.descripcion'});//Target13
  
  //Columnas
  moduloActual.definicionColumnas.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "className": "text-right" });
  moduloActual.definicionColumnas.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearTimestampToString, "className": "text-center" });
  moduloActual.definicionColumnas.push({ "targets" : 7, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnas.push({ "targets" : 8, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo, "data-align":"left" });
  moduloActual.definicionColumnas.push({"targets" : 9, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 10, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 11, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 12, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 13, "searchable" : true, "orderable" : false, "visible" : false });
//esto para el dataTable secundario
  
  //grilla  programacion
  moduloActual.ordenGrillaDetalleProgramacion=[[ 1, 'asc' ]];  
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'transportista.razonSocial'});//Target2
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'diaOperativo.totalCisternasProgramados'});//Target3
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'actualizadoEl'});//Target4
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'usuarioActualizacion'});//Target5
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'estado'});//Target6
  moduloActual.columnasGrillaDetalleProgramacion.push({ "data": 'comentario'});//Target7
  
  //Columnas programacion
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 2, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 3, "searchable" : true, "orderable" : false, "visible" : true});
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearTimestampToString, "className": "text-center" });
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 5, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo, "data-align":"left" });
  moduloActual.definicionColumnasDetalleProgramacion.push({ "targets" : 7, "searchable" : true, "orderable" : false, "visible" : false });

  //data source combo producto
  
  moduloActual.dataProducto=[];
  moduloActual.flagHabilitaProducto=false;
  
  // Inicio: Atención Ticket 9000002608
  moduloActual.detalle_compartimento = null;
  moduloActual.detalle_volumen = null;
  moduloActual.detalleProgramacionConductorDisable = false;
  // Fin: Atención Ticket 9000002608
  
  
  
  moduloActual.reglasValidacionFormulario={
    cmpNumeroGuiaRemision:  {required: true, maxlength: 15 },
    cmpNumeroOrdenCompra:   {required: true, maxlength: 15 },
    cmpCodigoScop:      	{required: true, maxlength: 15 },
    cmpPlantaDespacho:    	{required: true },
    cmpTransportista:     	{required: true },
    cmpConductor:      		{required: true },
    cmpFemisionOE:      	{required: true },
  };

  moduloActual.mensajesValidacionFormulario={
  cmpNumeroGuiaRemision:  	{required: "El campo es obligatorio", maxlength: "El campo debe contener 15 caracteres como m&aacute;ximo." },
  cmpNumeroOrdenCompra:   	{required: "El campo es obligatorio", maxlength: "El campo debe contener 15 caracteres como m&aacute;ximo." },
    cmpCodigoScop:      	{required: "El campo es obligatorio", maxlength: "El campo debe contener 15 caracteres como m&aacute;ximo." },

    cmpPlantaDespacho:    	{required: "El campo es obligatorio" },
    cmpTransportista:     	{required: "El campo es obligatorio" },

    cmpConductor:       	{required: "El campo es obligatorio" },
    cmpFemisionOE:      	{required: "El campo es obligatorio" },
  };

  moduloActual.reglasValidacionFormularioEvento={
    cmpEventoTipoEvento:  {required: true },
    cmpEventoFechaHora:   {required: true },
    cmpEventoDescripcion:   {required: true, maxlength: 3000 },
  };

  moduloActual.mensajesValidacionFormularioEvento={
  cmpEventoTipoEvento:  {required: "El campo es obligatorio" },
  cmpEventoFechaHora:   {required: "El campo es obligatorio" },
    cmpEventoDescripcion: {required: "El campo es obligatorio", maxlength: "El campo debe contener 3000 caracteres como m&aacute;ximo." },
  };

  moduloActual.reglasValidacionFormularioPesaje={
  cmpPesajePesoBruto:   {required: true },
  cmpPesajePesoTara:    {required: true },
  };

  moduloActual.mensajesValidacionFormularioPesaje={
  cmpPesajePesoBruto:   {required: "El campo es obligatorio" },
  cmpPesajePesoTara:    {required: "El campo es obligatorio" },
  };

  moduloActual.inicializarCampos= function(){
  this.obj.filtroOperacion = $("#filtroOperacion");
  this.indiceFormulario=$("#indiceFormulario");
  this.obj.btnConfirmarEliminarRegistro=$("#btnConfirmarEliminarRegistro");
  
	this.obj.btnConfirmarEliminarRegistro.on("click",function(){
	      moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	      var id = parseInt(moduloActual.registrosEliminar);
	      // Inicio 9000002608
	      console.log( 'VALORES: ' + moduloActual.indicesEliminacion );
	      // Fin 9000002608
	      if(id>0){
		      //eRegistro.id=parseInt(moduloActual.registrosEliminar);
			  try {			  
			    $.ajax({
			    	//type: "DELETE",
			    	type: "POST",
				    //url: './programacion/eliminar/'+id, 
			    	url: './programacion/eliminar/',
				    contentType: moduloActual.TIPO_CONTENIDO, 
				    data: JSON.stringify(moduloActual.indicesEliminacion),
				    success: function(respuesta) {
				      if (!respuesta.estado) {
				    	  moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				      } else { 				    	
				    	moduloActual.recuperarRegistro();
				    	moduloActual.obj.frmConfirmarModificarEstado.modal("hide");
	                  	if(moduloActual.obj.cisternaProgramadoFormSeleccionado>0){
	                    	  moduloActual.obj.cisternaProgramadoFormSeleccionado--;    		  
	                    	  moduloActual.obj.cmpCisternasProgramado.text(moduloActual.obj.cisternaProgramadoFormSeleccionado);  
	                	}
				    	moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
				      };
				    },			    		    
				    error: function() {
				    	moduloActual.mostrarErrorServidor(xhr,estado,error); 
				    }
			    });
			  }  catch(error){
				  moduloActual.mostrarDepuracion(error.message);
			  }
	      }
	      

	  });
  
  
	
  
    this.obj.filtroOperacion.on('change', function(e){
      moduloActual.idOperacion=$(this).val();
      moduloActual.volumenPromedioCisterna=$(this).find("option:selected").attr('data-volumen-promedio-cisterna');
      moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
      moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');
      e.preventDefault(); 
    });   
    this.registrosEliminar=$("#registrosEliminar");
    this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
    //Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaPlanificada.attr('data-fecha-actual');  
    //SAR N� SGCO-OI-007-2016
    var rangoSemana = utilitario.retornarfechaInicialFinal(fechaActual);
    //var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
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

    //Campos de detalle de transporte
    this.obj.detalleIdDOperativo=$("#detalleIdDOperativo");
    this.obj.detalleClienteOpe=$("#detalleClienteOpe");    
    
    this.obj.detallePlanta=$("#detallePlanta");    
    this.obj.detalleOperacion=$("#detalleOperacion");
    this.obj.detalleFechaCarga=$("#detalleFechaCarga");
    this.obj.detalleFechaDescarga=$("#detalleFechaDescarga");
    //campos notificacion
    
   
    this.obj.notificarClienteOpe=$("#notificarClienteOpe");    
    this.obj.notificarPlanta=$("#notificarPlanta");   
    this.obj.notificarFechaCarga=$("#notificarFechaCarga");
    this.obj.notificarFechaDescarga=$("#notificarFechaDescarga");
    //
    
    //Campos de Formulario Principal     
    
    this.obj.cmpCisternasPlanificado=$("#cmpCisternasPlanificado");    
    this.obj.cmpCisternasProgramado=$("#cmpCisternasProgramado");
    this.obj.cmpFormularioIdDOperativo=$("#cmpFormularioIdDOperativo");
    this.obj.cmpFormularioClienteOperacion=$("#cmpFormularioClienteOperacion");//Cliente oper
    this.obj.cmpFormularioOrdenCompra=$("#cmpFormularioOrdenCompra");//Cliente oper
    this.obj.cmpFormularioPlantaDespacho=$("#cmpFormularioPlantaDespacho");
    this.obj.cmpFormularioFechaDescarga=$("#cmpFormularioFechaDescarga");
    this.obj.cmpFormularioFechaCarga=$("#cmpFormularioFechaCarga");
    //CAMPOS FORMULARIO COMPLETAR
    this.obj.cmpCompletarClienteOperacion=$("#cmpCompletarClienteOperacion");//Cliente oper
    this.obj.cmpCompletarPlantaDespacho=$("#cmpCompletarPlantaDespacho");
    this.obj.cmpCompletarFechaDescarga=$("#cmpCompletarFechaDescarga");
    this.obj.cmpCompletarFechaCarga=$("#cmpCompletarFechaCarga");
    this.obj.cmpCompletarTransportista=$("#cmpCompletarTransportista");
    this.obj.cmpCompletarOrdenCompra=$("#cmpCompletarOrdenCompra");
    //CAMPOS FORMULARIO COMPLETAR
    this.obj.cmpComentarComentario=$("#cmpComentarComentario");
    
    this.obj.cmpId=$("#cmpId");
    this.obj.formularioIdTransporte=$("#formularioIdTransporte");
    this.obj.cmpNumeroGuiaRemision=$("#cmpNumeroGuiaRemision");
    this.obj.cmpNumeroOrdenCompra=$("#cmpNumeroOrdenCompra");
    this.obj.cmpNumeroFactura=$("#cmpNumeroFactura");
    this.obj.cmpCodigoScop=$("#cmpCodigoScop");
    this.obj.cmpTransportista=$("#cmpTransportista");
    
    this.obj.cmpCisternasPlanificadoModal = $('#cmbCisterna');
    this.obj.cmpSelect2Cisterna_2 = $('#cmbCisterna').select2({
    	ajax: {
  		    url: "./cisterna/recuperarPorTransportista", 
  		    dataType: 'json',
  		    delay: 250,
  		    dropdownAutoWidth: true,
  		    "data": function (parametros) {
  		    	try{
  		    		var transportista = 0;  
  		    		if(moduloActual.idTransportista > 0){
  		    			transportista = moduloActual.idTransportista ;
  		    		}
	  		      return { 
	  		    	idTransportista:parseInt(transportista),
	  		    	txt: parametros.term // search term
	  		      };
  		    	} catch(error){
  		          console.log(error.message);
  		        };
  		    },
  		    processResults: function (respuesta, pagina) {
  		    	var resultados= respuesta.contenido.carga;
  		    	 //7000001924
  		    	if(moduloActual.obj.cisterna.es_vacio()){
      		    	moduloActual.obj.cisterna.registrarTodo(resultados);      		    		
  		    	}
  		        return { results: resultados};
  		    },
  		    cache: true
  		  },
  		"language": "es",
  		"escapeMarkup": function (markup) { return markup; },
  		"templateResult": function (registro) {
  			if (registro.loading) {
  				return registro.text;
  			}
  			moduloActual.placaCisternaTracto=registro.placaCisternaTracto; 
  	        return "<div class='select2-user-result' >" + registro.placaCisternaTracto + "</div>";},
  	        	"templateSelection": function (registro) {
  	    	return registro.placaCisternaTracto || registro.text;
  	    }
    });
    
    
    
    this.obj.cmpSelec2Conductor = $('#cmbConductor').select2({
    	ajax: {
  		    url: "./conductor/listar", 
  		    dataType: 'json',
  		    delay: 250,
  		    "data": function (parametros) {
  		    	try{
      		      return {
      		    	valorBuscado: parametros.term, // search term
      		    	filtroEstado: 1,
      		        page: parametros.page,
      		        paginacion:0,
      		        campoOrdenamiento: 'apellidos',
      		        sentidoOrdenamiento: 'asc'
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
  				return registro.text;
  			}
  	        return "<div class='select2-user-result'>" + registro.nombreCompleto + "</div>";},
  	    "templateSelection": function (registro) {
		    	moduloActual.cmpBreveteConductor = registro.brevete;
		        return  registro.nombreCompleto || registro.text;
  	    },
    });
    
    // Inicio Atención Ticket 9000002608
    
    $('#btnCancelarCisterna').on('click', function(e){
    	moduloActual.limpiarFormularioAgregarCisterna();
    	$('#frmAddCisterna').modal('hide');
    });
    
    $('#btnAddCisterna').on('click', function(e){
    	var referenciaModulo = this;
    	var cisternaTractoSeleccionado = $('#cmbCisterna').val();
    	var conductorSeleccionado = $('#cmbConductor').val();
    	if(cisternaTractoSeleccionado === '' || cisternaTractoSeleccionado === null || cisternaTractoSeleccionado === 'undefined' ||
    			cisternaTractoSeleccionado === '0'){
    		alert('Debe seleccionar un Tracto/Cisterna');
    	}else if(conductorSeleccionado === '' || conductorSeleccionado === null || conductorSeleccionado === 'undefined' || 
    			conductorSeleccionado === '0' ){
    		alert('Debe seleccionar un conductor');
    	}else{
    		cisternaTractoSeleccionadoText = $('#select2-cmbCisterna-container').text();
        	//moduloActual.agregarCisterna(); 
        	// Obtener Detalle de la Cisterna:
        	var idCisterna = $('#cmbCisterna').val();	
        	var itObjCisterna = null;
        	itObjCisterna = moduloActual.obj.cisterna.buscar(idCisterna); // jmatos
        	moduloActual.detalleProgramacionConductorDisable = false;
        	moduloActual.identificadorFilaProgramacion = moduloActual.generarRandomId();
        	for(var i = 0; i < itObjCisterna.compartimentos.length; i++){
        		moduloActual.detalle_compartimento = itObjCisterna.compartimentos[i].identificador;
        		moduloActual.detalle_volumen = itObjCisterna.compartimentos[i].capacidadVolumetrica;
        		moduloActual.agregarCisterna(); 
        		moduloActual.detalleProgramacionConductorDisable = true;
        	}
        	// Operacion
        	var totalCisternasProgramadas = 0;
        	totalCisternasProgramadas = $('#cmpCisternasProgramadas').val();
        	totalCisternasProgramadas = Number(totalCisternasProgramadas) + Number(itObjCisterna.compartimentos[0].identificador);
        	$('#cmpCisternasProgramadas').val(totalCisternasProgramadas);
        	
        	moduloActual.limpiarFormularioAgregarCisterna();
        	$('#frmAddCisterna').modal('hide');
    	}
    });
    
    $('#cmbCisterna').on('change', function(e){
    	var referenciaModulo = this;
    	var idCisterna = $('#cmbCisterna').val();
    	var itObjCisterna = null;
    	itObjCisterna = moduloActual.obj.cisterna.buscar(idCisterna);
    	if(itObjCisterna != null)
    		$('#txtCompartimento').val(itObjCisterna.cantidadCompartimentos);
    });
    // Fin Atención Ticket 9000002608
    
    this.obj.cmpSelect2Transportista=$("#cmpTransportista").select2({
        ajax: {
            url: "./transportista/listar",
            dataType: 'json',
            delay: 250,
            data: function (parametros) {
              return {
              valorBuscado: parametros.term, // search term
                page: parametros.page,
                paginacion:0,
                idOperacion: moduloActual.obj.filtroOperacion.val()
              };
            },
            processResults: function (respuesta, pagina) {
              moduloActual.obj.cisterna.listado = []; // Inicio Ticket 9000002608
              var resultados= respuesta.contenido.carga;
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
              return "<div class='select2-user-result'>" + (registro.razonSocial) + "</div>";
          },
          templateSelection: function (registro) {
            moduloActual.idTransportista = registro.id;
//            try{
//              var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
//              elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
//              elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "Seleccionar");
//              moduloActual.obj.cmpCisternaTracto.empty().append(elemento1).val(0).trigger('change');
//            } catch(error){
//                console.log(error.message);
//            }
            moduloActual.obj.contenedorDetalles.hide();
          return registro.razonSocial || registro.text;
          },
      });
    
   
    this.obj.cmpSelect2Transportista.on('change', function(e){	   	
	      var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
	      
	       var numeroFormularios = moduloActual.obj.grupoProgramacion.getForms().length;
	       for(var contador = 0; contador < numeroFormularios; contador++){                    	  
	         var fila = moduloActual.obj.grupoProgramacion.getForm(contador); 
         	 var cmpCisternaTracto = fila.find("select[elemento-grupo='cisterna']");
         	 cmpCisternaTracto.empty().append(elemento1).val(0).trigger('change');

         	// Inicio Atención Ticket 9000002608
	   	      $('#cmbCisterna').empty().append(elemento1).val(0).trigger('change');
	   	      $('#cmbConductor').empty().append(elemento1).val(0).trigger('change');
	   	      moduloActual.obj.cisterna.listado = [];
	   	      // Fin Atención Ticket 9000002608
	       }	   
	});
    
    
    moduloActual.reglasValidacionFormularioCorreo={
    		cmpPara:  {required: true }
    };

	  moduloActual.mensajesValidacionFormularioCorreo={
			  cmpPara:  {required: "El campo es obligatorio" }
	  };
    
    
    
    this.obj.frmCorreo.validate({
        rules: moduloActual.reglasValidacionFormularioCorreo,
        messages: moduloActual.mensajesValidacionFormularioCorreo,
        submitHandler: function(form) {
        // form.submit();
        }
      });
    
    


    this.obj.cmpIdTracto=$("#cmpIdTracto");
    this.obj.cmpPlacaCisterna=$("#cmpPlacaCisterna");
    this.obj.cmpPlacaTracto=$("#cmpPlacaTracto");
    this.obj.cmpIdConductor=$("#cmpIdConductor");
    this.obj.cmpFemisionOE=$("#cmpFemisionOE");
    this.obj.cmpFemisionOE.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
  
    this.obj.grupoProgramacion = $('#GrupoProgramacion').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: false,
        //maxFormsCount: 6,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpElementoProducto=$(formularioNuevo).find("select[elemento-grupo='producto']");
          cmpElementoProducto.select2();          
          cmpElementoProducto.tipoControl="select2";
          moduloActual.obj.cmpSelect2Producto=$(formularioNuevo).find("select[elemento-grupo='producto']").select2({
        	  data: moduloActual.dataProducto
          });
          cmpElementoProducto.on('change', function(){
        	 var hddIndicador = $(formularioNuevo).find("input[elemento-grupo='hidentificadorVolumen']");
        	 var cmpElementoVolumen = $(formularioNuevo).find("input[elemento-grupo='volumen']");
        	 var cmpVolumenProgramado = 0;
        	 cmpVolumenProgramado = $('#cmpVolumenProgramado').val();
        	 if(Number(hddIndicador.val()) == 0){
        		 cmpVolumenProgramado = Number(cmpVolumenProgramado) + Number(cmpElementoVolumen.val());
        		 hddIndicador.val('1');
        	 }
        	 $('#cmpVolumenProgramado').val(cmpVolumenProgramado);
          });
          
          //if(moduloActual.flagHabilitaProducto){ // jmatos rev
          cmpElementoProducto.attr('disabled',false); 
          /*}else{//seleccionar combo*/
    	      /*cmpElementoProducto.attr('disabled',true);
    		  var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,moduloActual.dataProducto[0].id);
    	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,moduloActual.dataProducto[0].text);
    	      cmpElementoProducto.empty().append(elemento1).val(moduloActual.dataProducto[0].id).trigger('change');
          }*/
          
          var cmpElementoCisterna=$(formularioNuevo).find("select[elemento-grupo='cisterna']");
          var cmpElementoConductor=$(formularioNuevo).find("select[elemento-grupo='conductor']");
          cmpElementoCisterna.select2();          
          cmpElementoCisterna.tipoControl="select2";
          
          cmpElementoConductor.select2();          
          cmpElementoConductor.tipoControl="select2";
           
          // Inicio Atención Ticket 9000002608
          var cisternaTractoSeleccionadoText = $('#select2-cmbCisterna-container').text();
          var cmpElementoTracto=$(formularioNuevo).find("input[elemento-grupo='tracto']");
          cmpElementoTracto.val(cisternaTractoSeleccionadoText);
          var hddTractoCisternaId = $(formularioNuevo).find("input[elemento-grupo='tracto-id']"); 
          hddTractoCisternaId.val($('#cmbCisterna').val());
          var conductorSeleccionadoText = $('#select2-cmbConductor-container').text();
          var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, $('#cmbConductor').val());
	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, conductorSeleccionadoText);	      
	      $(formularioNuevo).find("select[elemento-grupo='conductor']").empty().append(elemento1).val($('#cmbConductor').val()).trigger('change');
          
          // Compartimento && Volumen
          var cmpCompartimento = $(formularioNuevo).find("input[elemento-grupo='compartimento']");
          var cmpVolumen = $(formularioNuevo).find("input[elemento-grupo='volumen']");
          cmpCompartimento.val(moduloActual.detalle_compartimento);
          cmpVolumen.val(moduloActual.detalle_volumen);
          
          if(moduloActual.detalle_compartimento === 1){
        	  cmpCompartimento.attr('style',  'color:red');
        	  cmpVolumen.attr('style',  'color:red');
        	  cmpElementoTracto.attr('style',  'color:red');
        	  var cmpConductor = $(formularioNuevo).find("span[class='select2-selection__rendered']");
        	  cmpConductor.attr('style', 'color:red');
          }else{
        	  cmpCompartimento.removeAttr('style',  'color:red');
        	  cmpVolumen.removeAttr('style',  'color:red');
        	  cmpElementoTracto.removeAttr('style',  'color:red');
        	  var cmpConductor = $(formularioNuevo).find("span[class='select2-selection__rendered']");
        	  cmpConductor.removeAttr('style', 'color:red');
          }
          
          if(moduloActual.detalleProgramacionConductorDisable===true){
        	  $(formularioNuevo).find("select[elemento-grupo='conductor']").attr('disabled', 'disabled');
        	  $(formularioNuevo).find("a[elemento-grupo='botonElimina']").hide();
          }
          var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
          cmpRandomIdentificador.val(moduloActual.identificadorFilaProgramacion);
          console.log('Random: ' +cmpRandomIdentificador.val());
          // Fin Atención Ticket 9000002608
          
          moduloActual.obj.cmpSelect2Cisterna=$(formularioNuevo).find("select[elemento-grupo='cisterna']").select2({
      	  ajax: {
      		    url: "./cisterna/recuperarPorTransportista", 
      		    dataType: 'json',
      		    delay: 250,
      		    "data": function (parametros) {

      		    	try{
      		    		var transportista = 0;  
      		    		
      		    		if(moduloActual.idTransportista > 0){
      		    			transportista = moduloActual.idTransportista ;
      		    		}
    	  		      return { 
    	  		    	idTransportista:parseInt(transportista),
    	  		    	txt: parametros.term // search term
    	  		      };
      		    	} catch(error){
      		          console.log(error.message);
      		        };
      		    },
      		    processResults: function (respuesta, pagina) {
      		    	var resultados= respuesta.contenido.carga;
      		    	 //7000001924
      		    	if(moduloActual.obj.cisterna.es_vacio()){
          		    	moduloActual.obj.cisterna.registrarTodo(resultados);      		    		
      		    	}
      		        return { results: resultados};
      		    },
      		    cache: true
      		  },
      		"language": "es",
      		"escapeMarkup": function (markup) { return markup; },
      		"templateResult": function (registro) {
      			if (registro.loading) {
      				return registro.text;
      			}
      			moduloActual.placaCisternaTracto=registro.placaCisternaTracto; 
      	        return "<div class='select2-user-result' >" + registro.placaCisternaTracto + "</div>";},
      	    "templateSelection": function (registro) {
      	    	return registro.placaCisternaTracto || registro.text;
      	    },
        });
          
          // Atención Ticket 9000002608
          moduloActual.obj.cmpSelect2Conductor=$(formularioNuevo).find("select[elemento-grupo='conductor']").select2({
          	  ajax: {
          		    url: "./conductor/listar", 
          		    dataType: 'json',
          		    delay: 250,
          		    "data": function (parametros) {
          		    	try{
              		      return {
              		    	valorBuscado: parametros.term, // search term
              		    	filtroEstado: 1,
              		        page: parametros.page,
              		        paginacion:0,
              		        campoOrdenamiento: 'apellidos',
              		        sentidoOrdenamiento: 'asc'
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
          				return registro.text;
          			}
          	        return "<div class='select2-user-result'>" + registro.nombreCompleto + "</div>";},
          	    "templateSelection": function (registro) {
      		    	moduloActual.cmpBreveteConductor = registro.brevete;
      		        return  registro.nombreCompleto || registro.text;
          	    },
            });
          
           /*moduloActual.obj.cmpSelect2Conductor.on("select2:selecting", function(e) { 
        	   // what you would like to happen
        	   
        	   var data = $(this).select2('data');
               //$("#selectedS").text(data[0].text);
        	   console.log('data: ' + data[0].text);
        	   
        	   var nuevoConductor = $(formularioNuevo).find("select[elemento-grupo='conductor']");
        	   
        	   console.log('Actualiza items...');
        	   console.log('Conductor: ' + cmpElementoConductor.val());
        	   console.log('Cisterna: ' + hddTractoCisternaId.val());
        	   console.log('Nuevo Conductor: ' + nuevoConductor.val());
        	   var numeroFormularios = moduloActual.obj.grupoProgramacion.getForms().length;
        	   for(var contador = 0; contador < numeroFormularios; contador++){
        		   var elemento = moduloActual.obj.grupoProgramacion.getForm(contador).find("input[elemento-grupo='hrandomProgramacion']");
                   var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
                   
                   console.log('Elemento Seleccionado: ' + elemento.val());
                   console.log('Elemento Verificado: ' + cmpRandomIdentificador.val());
                   
                   if(cmpRandomIdentificador.val() == elemento.val()){
                	   console.log('Actualiza Conductor....');
                	   var fila = moduloActual.obj.grupoProgramacion.getForm(contador);
                  	   var cmpSelectorConductorHija = fila.find("select[elemento-grupo='conductor']");
                  	   
                  	   console.log('Elemento a sustituir: ' + cmpSelectorConductorHija.val());
                  	   var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
                  	   elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
                  	   elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,"SELECCIONE...");
                  	   cmpSelectorConductorHija.empty().append(elemento1).val(elemento1).trigger('change');
                   }
        	   }
        	});*/
          
          moduloActual.obj.cmpSelect2Conductor.on("select2:select", function(e){
        	  var valor = $(".select2 option:selected").text();
        	  var data = $(this).select2('data');
       	      var numeroFormularios = moduloActual.obj.grupoProgramacion.getForms().length;
	       	  for(var contador = 0; contador < numeroFormularios; contador++){
	       		var elemento = moduloActual.obj.grupoProgramacion.getForm(contador).find("input[elemento-grupo='hrandomProgramacion']");
                var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
                if(cmpRandomIdentificador.val() == elemento.val()){
                	var fila = moduloActual.obj.grupoProgramacion.getForm(contador);
               	    var cmpSelectorConductorHija = fila.find("select[elemento-grupo='conductor']");
	               	 var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	               	 elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, data[0].id);
	               	 elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, data[0].nombreCompleto);
	               	 cmpSelectorConductorHija.empty().append(elemento1).val(data[0].id).trigger('change');
                }
	       	  }
          }).trigger('change');
          
          
         // Fin Atención Ticket 9000002608
          
          var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");
          cmpElimina.on("click", function(){
        	  
              try{
            	  moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(26);              	  
                  var numeroFormularios = moduloActual.obj.grupoProgramacion.getForms().length;
                  var indexFormularios = [];
                  for(var contador = 0; contador < numeroFormularios; contador++){                     
                      //var formulario = moduloActual.obj.grupoProgramacion.getForm(contador); 
                      //var indice=(formulario.attr('id')).substring(26);//indice html
                      //if(moduloActual.indiceFormulario == indice){                    	  
                      var elemento = moduloActual.obj.grupoProgramacion.getForm(contador).find("input[elemento-grupo='hrandomProgramacion']");
                      var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
                      if(cmpRandomIdentificador.val() == elemento.val()){
                    	  var fila = moduloActual.obj.grupoProgramacion.getForm(contador);       
                    	  
                    	  if(fila.find("input[elemento-grupo='hidentificador']").val() > 0){
                    		 moduloActual.registrosEliminar = fila.find("input[elemento-grupo='hidentificador']").val();
                    		// Atenci�n Ticket 9000002608
                       	  	moduloActual.indicesEliminacion.push(fila.find("input[elemento-grupo='hidentificador']").val());
                       	  	// Fin Ticket 9000002608
              		    	 //moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
                    		 moduloActual.descripcionPermiso = 'ELIMINAR_PROGRAMACION';
                    		 moduloActual.validaPermisos();
              		    	 //moduloActual.obj.frmConfirmarModificarEstado.modal("show");
                    	  }else{
                    		  var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
                    		  indexFormularios.push(contador);
                        	  if(moduloActual.obj.cisternaProgramadoFormSeleccionado>0){
                            	  moduloActual.obj.cisternaProgramadoFormSeleccionado--;    		  
                            	  moduloActual.obj.cmpCisternasProgramado.text(moduloActual.obj.cisternaProgramadoFormSeleccionado);  
                        	  } 
                    	  }                    	  
                    	 //break; 
                      }
                  }
                  // Inicio: Atención Ticket 900002608
                  for( var i = 0; i<indexFormularios.length; i++){
                	  var fila = moduloActual.obj.grupoProgramacion.getForm(indexFormularios[0]);
                	  if(i === 0){
                		  var compartimento = fila.find("input[elemento-grupo='compartimento']").val();
                		  var cmpTotalCompartimentos = $('#cmpCisternasProgramadas').val();
                		  cmpTotalCompartimentos = Number(cmpTotalCompartimentos) - Number(compartimento);
                		  $('#cmpCisternasProgramadas').val(cmpTotalCompartimentos);
                	  }
                	  var hddIndicador = fila.find("input[elemento-grupo='hidentificadorVolumen']").val();
                	  if(Number(hddIndicador) === 1){
                		  var volumen = fila.find("input[elemento-grupo='volumen']").val();
                    	  var cmpTotalVolumen = $('#cmpVolumenProgramado').val();
                    	  cmpTotalVolumen = Number(cmpTotalVolumen) - Number(volumen);
                    	  $('#cmpVolumenProgramado').val(cmpTotalVolumen);
                	  }
                	  // Elimina Formulario
                	  moduloActual.obj.grupoProgramacion.removeForm(indexFormularios[0]);
                  }
                  // Fin: Atención Ticket 9000002608
              } catch(error){
                console.log(error.message);
              }
          });
          
          
          
        },
        afterRemoveCurrent: function(control) {
          if (control.hasForms()==false){
          	moduloActual.obj.cntPlanificaciones.hide();
            //control.addForm();          
          }
        }
      }); 
    
    this.obj.grupoCompletar = $('#GrupoCompletar').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: false,
        //maxFormsCount: 6,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {  
        	var cmpElementoCodigoScop     	= $(formularioNuevo).find("input[elemento-grupo='codigoScop']");
	        var cmpElementoCodigoSapPedido  = $(formularioNuevo).find("input[elemento-grupo='codigoSapPedido']");
	        var cmpElementoPlanta = $(formularioNuevo).find("select[elemento-grupo='planta']");
	        
	        cmpElementoCodigoScop.inputmask('decimal', {digits: 0});
	        cmpElementoCodigoSapPedido.inputmask('decimal', {digits: 0});
	        	     
	        cmpElementoPlanta.select2();          
	        cmpElementoPlanta.tipoControl="select2";
	                   	        
	        moduloActual.obj.cmpSelect2Planta=$(formularioNuevo).find("select[elemento-grupo='planta']").select2({
		    	  ajax: {
		  		    url: "./planta/listar",
		  		    dataType: 'json',
		  		    delay: 250,
		  		    data: function (parametros) {
		  		      return {
		  		    	valorBuscado: parametros.term, // search term
		  		        page: parametros.page,
		  		        paginacion:0
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
		  				return "Buscando..."; //registro.text;
		  			}	
		  				return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
				    },
				    templateSelection: function (registro) {
				        return registro.descripcion || registro.text;
				    },
		    });
        }
      });
    

    this.obj.cmpSumVolumenTempObservada=$("#cmpSumVolumenTempObservada");
    this.obj.cmpSumVolumenTempObservada.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.cmpSumVolumen60F=$("#cmpSumVolumen60F");
    this.obj.cmpSumVolumen60F.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});

    //campos Formulario vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaClienteOperacion=$("#vistaClienteOperacion");
    this.obj.vistaPlantaDespacho=$("#vistaPlantaDespacho");
    this.obj.vistaFechaPlanificada=$("#vistaFechaPlanificada");
    this.obj.vistaFechaCarga=$("#vistaFechaCarga");
    this.obj.vistaTransportista=$("#vistaTransportista");
	this.obj.vistaComentario=$("#vistaComentario");
    //Vista de auditoria
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    //Campos de formulario Evento
    this.obj.cmpEventoIdTransporteEvento=$("#cmpEventoIdTransporteEvento");
    this.obj.cmpEventoIdDOperativo=$("#cmpEventoIdDOperativo");
    this.obj.cmpEventoCliente=$("#cmpEventoCliente");
    this.obj.cmpEventoOperacion=$("#cmpEventoOperacion");
    this.obj.cmpEventoFechaPlanificacion=$("#cmpEventoFechaPlanificacion");

    this.obj.cmpEventoNumeroGuiaRemision=$("#cmpEventoNumeroGuiaRemision");
    this.obj.cmpEventoNumeroOrdenCompra=$("#cmpEventoNumeroOrdenCompra");
    this.obj.cmpEventoCisternaTracto=$("#cmpEventoCisternaTracto");

    this.obj.cmpEventoTipoEvento=$("#cmpEventoTipoEvento");
    this.obj.cmpEventoFechaHora=$("#cmpEventoFechaHora");
    this.obj.cmpEventoFechaHora.inputmask(constantes.FORMATO_FECHA, 
    { 
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpEventoDescripcion=$("#cmpEventoDescripcion");
  };

  
 
  moduloActual.verificaBotonCisterna = function(){	  
	  var moduloActual=this;	 
      try {
    	  
    	  var numeroProductos = moduloActual.obj.grupoProgramacion.getFormsCount();
    	  var cantidad_cisternas=0;
//    	  var cantidad = 0;
    	  for(var i=0; i < moduloActual.dataProducto.length; i++){
//    		  cantidad = 0;
    		  cantidad_cisternas=cantidad_cisternas+moduloActual.dataProducto[i].cantcisterna;
//        	  for(var contador=0; contador < numeroProductos; contador++){
//     	         var formulario = moduloActual.obj.grupoProgramacion.getForm(contador);
//     	         var cmpElementoProducto  = formulario.find("select[elemento-grupo='producto']");
//     	         
//     	         if(cmpElementoProducto != null && parseInt(cmpElementoProducto.val())>0){
//     	        	 if(moduloActual.dataProducto[i].id == parseInt(cmpElementoProducto.val())){
//     	        		cantidad++;
//     	        	 }
//     	        	 
//     	         }      			
//     	  	  }
        	  
//        	  if(cantidad<moduloActual.dataProducto[i].cantcisterna){
//        		  moduloActual.obj.btnAgregarCisterna.removeClass("disabled");
//        		  break;
//        	  }else{
//        		  moduloActual.obj.btnAgregarCisterna.addClass("disabled");
//        	  }

    	  }
    	  
    	  if(cantidad_cisternas == numeroProductos){
    		  moduloActual.obj.btnAgregarCisterna.addClass("disabled");
    	  }else{
    		  moduloActual.obj.btnAgregarCisterna.removeClass("disabled");
    	  }
    	  
      	
      } catch(error){
      	console.log(error.message);
      }      
  };
  
  
  
  moduloActual.agregarCisterna = function(registro){	  
	  var referenciaModulo=this;	 
      try {
    	  if(referenciaModulo.dataProducto.length>1){
    		  referenciaModulo.flagHabilitaProducto=true;    		  
    	  }else{
    		  referenciaModulo.flagHabilitaProducto=false;
    	  }    	  
    	  referenciaModulo.obj.cntFormulario.show();      	
    	  referenciaModulo.obj.grupoProgramacion.addForm();
    	  
  		  referenciaModulo.obj.cisternaProgramadoFormSeleccionado++;    		  
    	  referenciaModulo.obj.cmpCisternasProgramado.text(referenciaModulo.obj.cisternaProgramadoFormSeleccionado);
    	  //referenciaModulo.verificaBotonCisterna();
      	
      } catch(error){
      	console.log(error.message);
      }
  };
  
  moduloActual.agregarProductoCisterna = function(registro){
	    var referenciaModulo=this;
	    this.idRegistro = referenciaModulo.idDiaOperativo;
	    //identificadores para las busquedas
	    this.obj.idDiaOperativo = referenciaModulo.idDiaOperativo;
	    this.obj.idTransporte = referenciaModulo.idTransporte;
	    this.obj.idTransportista = registro.idTransportista;//

	    var numeroDetalles= registro.planificaciones.length;
	    this.obj.grupoProgramacion.removeAllForms();

	    var cantProd=0;
	    for(var contador=0; contador < numeroDetalles; contador++){      
	      var can_cisterna = registro.planificaciones[contador].cantidadCisternas;
	      for(var cant=0; cant < can_cisterna; cant++){    	  
	          moduloActual.obj.grupoProgramacion.addForm();
	          var formulario= moduloActual.obj.grupoProgramacion.getForm(cantProd);    	  
	    	  formulario.find("input[elemento-grupo='producto']").val(registro.planificaciones[contador].producto.nombre); // contador
	    	  formulario.find("input[elemento-grupo='producto']").attr("data-idProducto",registro.planificaciones[contador].producto.id); // contador
	    	  cantProd++;
	      }    

	    }
	  };
  
  


  moduloActual.datosCabecera= function(){
    var referenciaModulo=this;
    //referenciaModulo.resetearFormularioPrincipal();
    //para formulario principal
	  this.obj.cmpFormularioClienteOperacion.text(referenciaModulo.obj.clienteSeleccionado + '/'+referenciaModulo.obj.operacionSeleccionado);
	  this.obj.cmpFormularioPlantaDespacho.text(referenciaModulo.obj.plantaSeleccionado);
	  
	  this.obj.cmpFormularioFechaDescarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaDescargaSeleccionado));
	  this.obj.cmpFormularioFechaCarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaCargaSeleccionado));
	  this.obj.cmpCisternasPlanificado.text(referenciaModulo.obj.cisternaPlanificadoSeleccionado);
	  
	  if(referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_PROGRAMACION_NUEVO){		  
		  referenciaModulo.obj.cisternaProgramadoFormSeleccionado=referenciaModulo.obj.cisternaPlanificadoSeleccionado; 
		  try {
	      $.ajax({
	        type: constantes.PETICION_TIPO_GET,
	        url: "./transportista/listar", 
	        contentType: referenciaModulo.TIPO_CONTENIDO, 
	        data: {idOperacion: moduloActual.obj.filtroOperacion.val() },
	        success: function(respuesta) {
	          if (!respuesta.estado) {
	        	 
	          } else {
	        	  var contenido = respuesta.contenido.carga.length;
	        	  if(contenido == 1){
	        		var reg = respuesta.contenido.carga[0];
	        		var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	                elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, reg.id);
	                elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, reg.razonSocial);
	                moduloActual.obj.cmpTransportista.empty().append(elemento1).val(reg.id).trigger('change');
	                $(cmpTransportista).prop('disabled', true);
	        	  } 
	            }
	          },
	          error: function(xhr,estado,error) {
	            referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	          }
	        }); 
	      } catch(error){
	      referenciaModulo.mostrarDepuracion(error.message);
	      }
	  }
	  this.obj.cmpCisternasProgramado.text(referenciaModulo.obj.cisternaProgramadoFormSeleccionado);
	  
	  //para fomulario completar
	  this.obj.cmpCompletarClienteOperacion.text(referenciaModulo.obj.clienteSeleccionado + '/'+referenciaModulo.obj.operacionSeleccionado);
	  this.obj.cmpCompletarPlantaDespacho.text(referenciaModulo.obj.plantaSeleccionado);
	  this.obj.cmpCompletarFechaDescarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaDescargaSeleccionado));
	  this.obj.cmpCompletarFechaCarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaCargaSeleccionado));
	  //para fomulario comentar
	  this.obj.cmpComentarComentario.text(referenciaModulo.obj.cmpComentarComentario);
	  //para formulario vista
	  this.obj.vistaClienteOperacion.text(referenciaModulo.obj.clienteSeleccionado + '/'+referenciaModulo.obj.operacionSeleccionado);
	  this.obj.vistaPlantaDespacho.text(referenciaModulo.obj.plantaSeleccionado);
	  this.obj.vistaFechaPlanificada.text(utilitario.formatearFecha(referenciaModulo.obj.fechaDescargaSeleccionado));
	  this.obj.vistaFechaCarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaCargaSeleccionado));
      this.obj.vistaComentario.text(referenciaModulo.obj.cmpComentarComentario.val().toUpperCase());  
	  //para pantalla con dos grillas
	  moduloActual.obj.detalleClienteOpe.text(referenciaModulo.obj.clienteSeleccionado + '/'+referenciaModulo.obj.operacionSeleccionado);
	  moduloActual.obj.detallePlanta.text(referenciaModulo.obj.plantaSeleccionado);
	  moduloActual.obj.detalleFechaCarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaCargaSeleccionado));//abarri
	  moduloActual.obj.detalleFechaDescarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaDescargaSeleccionado));//abarri
	  //notificacion
//	  moduloActual.obj.notificarClienteOpe.text(referenciaModulo.obj.clienteSeleccionado + '/'+referenciaModulo.obj.operacionSeleccionado);
//	  moduloActual.obj.notificarPlanta.text(referenciaModulo.obj.plantaSeleccionado);
//	  moduloActual.obj.notificarFechaCarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaCargaSeleccionado));//abarri
//	  moduloActual.obj.notificarFechaDescarga.text(utilitario.formatearFecha(referenciaModulo.obj.fechaDescargaSeleccionado));//abarri
	  
	  
  
  };

  moduloActual.limpiarFormularioPrincipal = function(){
    var referenciaModulo=this;
    referenciaModulo.obj.frmPrincipal[0].reset();
    referenciaModulo.flagHabilitaProducto=false;
    var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
      
//      moduloActual.obj.cisternaProgramadoFormSeleccionado=0;
//      moduloActual.obj.cisternaPlanificadoSeleccionado=0;
      
//
//      moduloActual.idTransportista = 0;
//      moduloActual.obj.cmpCisternaTracto.select2("val", "null");
//      moduloActual.obj.cmpPlantaDespacho.select2("val", null);
//      moduloActual.obj.cmpTransportista.select2("val", null);
//      moduloActual.obj.cmpConductor.select2("val", null);
//
//      moduloActual.obj.cmpCisternaTracto.empty().append(elemento1).val(0).trigger('change');
//      moduloActual.obj.cmpPlantaDespacho.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpTransportista.empty().append(elemento1).val(0).trigger('change');
//      moduloActual.obj.cmpConductor.empty().append(elemento1).val(0).trigger('change');
  };

  moduloActual.llenarProductos = function(registro){	 
	  var referenciaModulo=this;	  
	  referenciaModulo.dataProducto=[];
	  if(registro!=null){
		  var numeroDetalles= registro.productos.length;
		  for(var contador=0; contador < numeroDetalles; contador++){ 
			  //var can_cisterna = registro.planificaciones[contador].cantidadCisternas;			  
			  var comboProducto={};
			  //comboProducto.id=registro.planificaciones[contador].producto.id;
			  //comboProducto.text=registro.planificaciones[contador].producto.nombre;
			  comboProducto.id=registro.productos[contador].id_producto;
			  comboProducto.text=registro.productos[contador].nombre;
			  
			  //comboProducto.cantcisterna=can_cisterna;
			  referenciaModulo.dataProducto.push(comboProducto);  
		  }  
	  }
	  
	  // Atención Ticket 9000002608
	  $('#cmpCisternasPlanificadas').val(registro.programacionPlanificada.cantidad_cisternas);  
	  $('#cmpVolumenPlanificado').val(registro.programacionPlanificada.volumen_solicitado);
	  $('#cmpCisternasPlanificadas').attr('readonly', 'readonly');
	  $('#cmpVolumenPlanificado').attr('readonly', 'readonly');
	  // Fin Ticket 9000002608
  };
  
  
  // Inicio: Atención Ticket 9000002608 - jmatos/ibm
  
  moduloActual.limpiarFormularioAgregarCisterna = function(){
	  var cmpCisternaTracto = $('#cmbCisterna');
	  var cmpConductor = $('#cmbConductor');
	  $('#txtCompartimento').val('');
	  cmpCisternaTracto.val("");
	  cmpConductor.val("");
	  
	 //$('#cmbCisterna').empty().append('Seleccionar...').val(0).trigger('change');
	  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

	  $('#cmbCisterna').empty().append(elemento1).val(0).trigger('change');
	  $('#cmbConductor').empty().append(elemento1).val(0).trigger('change');
	  
	  
  }
  
  moduloActual.llenarFormularioAgregar = function(registro){
	$('#cmpCisternasPlanificadas').val(registro.programacionPlanificada.cantidad_cisternas);  
	$('#cmpVolumenPlanificado').val(registro.programacionPlanificada.volumen_solicitado);
	$('#cmpCisternasPlanificadas').attr('readonly', 'readonly');
	$('#cmpVolumenPlanificado').attr('readonly', 'readonly');
	//$('#cmpVolumenPlanificado').inputmask('decimal', {digits: 2});
    // Inicio Ticket 9000002608
	var numeroDetalles = 0;
    if(registro.planificaciones != null && registro.planificaciones != 'undefined'){
    	numeroDetalle = registro.planificaciones.length;
    }
    // Fin Ticket 9000002608
    this.obj.grupoProgramacion.removeAllForms();
    var cantProd=0;
    for(var contador=0; contador < numeroDetalles; contador++){      
      var can_cisterna = registro.planificaciones[contador].cantidadCisternas;
      //cargar combo        
      for(var cant=0; cant < can_cisterna; cant++){    	  
          /*moduloActual.obj.grupoProgramacion.addForm();
          var formulario= moduloActual.obj.grupoProgramacion.getForm(cantProd);
		  var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.planificaciones[contador].producto.id);
	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.planificaciones[contador].producto.nombre);
	      formulario.find("select[elemento-grupo='producto']").empty().append(elemento1).val(registro.planificaciones[contador].producto.id).trigger('change');
	      cantProd++;*/
      } 
    }
  };
  // Fin: Atención Ticket 9000002608 - jmatos/ibm
  
  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
      //datos para la asignacion
	  if (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_PROGRAMACION_MODIFICAR){
	     eRegistro.id = parseInt(referenciaModulo.idProgramacion); 
	  }
      eRegistro.idDiaOperativo = parseInt(referenciaModulo.idDiaOperativo);           
      eRegistro.idTransportista = parseInt(referenciaModulo.idTransportista);
      eRegistro.programaciones=[];
      //datos para detalleTransporte
        var numeroFormularios = referenciaModulo.obj.grupoProgramacion.getForms().length;
        for(var contador = 0; contador < numeroFormularios; contador++){
          var detalles={};
          var formulario = referenciaModulo.obj.grupoProgramacion.getForm(contador);
          var cmpElementoProducto     = formulario.find("select[elemento-grupo='producto']");
          //var cmpElementoCisterna     = formulario.find("select[elemento-grupo='cisterna']");
          var cmpElementoCisterna     = formulario.find("input[elemento-grupo='tracto-id']"); // Atencion Ticket 9000002608
          var cmpElementoConductor     = formulario.find("select[elemento-grupo='conductor']");
          var cmpElementoPlanta       = formulario.find("select[elemento-grupo='planta']");
          var cmpFormularioOrdenCompra = referenciaModulo.obj.frmPrincipal[0].cmpFormularioOrdenCompra;
          var cmpElementoIdDetalle     = formulario.find("input[elemento-grupo='hidentificador']");
          var cmpElementoCodigoScop     = formulario.find("input[elemento-grupo='hcodigoScop']");
          var cmpElementoCodigoSapPedido     = formulario.find("input[elemento-grupo='hcodigoSapPedido']");
	       // Atención Ticket 9000002608
          var cmpCapacidadVolumetrica = formulario.find("input[elemento-grupo='volumen']");
          var cmpCantidadCompartimientos = formulario.find("input[elemento-grupo='compartimento']"); 
	       // Fin Ticket 9000002608
          	detalles.idProducto       = parseInt(cmpElementoProducto.val());            
            detalles.idCisterna     = parseInt(cmpElementoCisterna.val());            
            detalles.idConductor  = parseInt(cmpElementoConductor.val());
            detalles.idPlanta  = parseInt(cmpElementoPlanta.val());
            detalles.ordenCompra = cmpFormularioOrdenCompra.value;            
            detalles.codigoScop  = parseInt(cmpElementoCodigoScop.val());
            detalles.codigoSapPedido  = parseInt(cmpElementoCodigoSapPedido.val());
            detalles.id  = parseInt(cmpElementoIdDetalle.val());
            detalles.completar = 1; // flag que indica que NO es completar
            // Atención Ticket 9000002608
            detalles.capacidadCisternaTotal = parseInt(cmpCantidadCompartimientos.val());
            detalles.capacidadVolumetrica = parseInt(cmpCapacidadVolumetrica.val()); 
            // Fin Ticket 9000002608
            eRegistro.programaciones.push(detalles);
        }
        console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

  moduloActual.validarDetallesCompletar = function(retorno){
	  referenciaModulo = this;
	  retorno = true;
	  try{
	    var numeroFormularios = referenciaModulo.obj.grupoCompletar.getForms().length;	    
//	    var cmpFormularioOrdenCompra = referenciaModulo.obj.frmCompletar[0].cmpCompletarOrdenCompra;
//	    if(cmpFormularioOrdenCompra.value == ""){
//            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de ingresar el orden de compra");
//            return false;
//        }	    
	    
	    
	    for(var contador = 0; contador < numeroFormularios; contador++){
	          var formulario = referenciaModulo.obj.grupoCompletar.getForm(contador);
	          var cmpElementoCodigoScop     = formulario.find("input[elemento-grupo='codigoScop']");
	          var cmpElementoCodigoSapPedido   = formulario.find("input[elemento-grupo='codigoSapPedido']"); 
	       	  var cmpElementoProducto =     formulario.find("input[elemento-grupo='producto']");   
	       	  
	          // Inicio Ticket 9000002608
	          var cmpCantidadCompartimientos = formulario.find("input[elemento-grupo='compartimento']"); 
	          //if(cmpElementoCodigoScop.val().length < 1 && cmpCantidadCompartimientos.val() == 1){
	          if(cmpElementoCodigoScop.val().length < 1 && cmpElementoProducto.val().length > 0){
	            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Ingrese SCOP .");
	            return false;
	          } 
	          // Fin Ticket 9000002608
	          if(cmpElementoCodigoSapPedido.val() == 0 && cmpElementoProducto.val().length > 0){
	            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Ingrese Pedido .");
	            return false;
	          }	  
	          
	          console.log();
	      }
	    return retorno;
	    } catch(error){
	          console.log(error.message);
	      
	    };
	  };
	  
  moduloActual.validarDetallesProgramacion = function(retorno){
	  referenciaModulo = this;
	  retorno = true;
	  try{
	    var numeroFormularios = referenciaModulo.obj.grupoProgramacion.getForms().length;
	    
	    if(numeroFormularios>0){
		    var _listado = [];
		    var _map_cis ={};
			if(referenciaModulo.obj.cisterna.es_vacio()){
				var vnum=referenciaModulo.idTransportista;
				referenciaModulo.obj.cisterna.cargar_cisternas(vnum);
			}
			/*
			
			var numeroFormularios = moduloActual.obj.grupoProgramacion.getForms().length;
	       	  for(var contador = 0; contador < numeroFormularios; contador++){
	       		var elemento = moduloActual.obj.grupoProgramacion.getForm(contador).find("input[elemento-grupo='hrandomProgramacion']");
                var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
                if(cmpRandomIdentificador.val() == elemento.val()){
			
			*/
			
			var isTieneProducto = false;
			
		    for(var contador = 0; contador < numeroFormularios; contador++){
		          var formulario = referenciaModulo.obj.grupoProgramacion.getForm(contador);
		          var cmpElementoProducto     = formulario.find("select[elemento-grupo='producto']");
		          //var cmpElementoCisterna   = formulario.find("select[elemento-grupo='cisterna']");
		          var cmpElementoCisterna   = formulario.find("input[elemento-grupo='tracto-id']"); // Atención Ticket 9000002608
		          var cmpElementoConductor        = formulario.find("select[elemento-grupo='conductor']");	 
		          //var cmpElementoPlanta        = formulario.find("select[elemento-grupo='planta']");	 
		          // Inicio Ticket 9000002608
		          var cmpCantidadCompartimientos = formulario.find("input[elemento-grupo='compartimento']"); 
		          
		          
		          /** Validación de Productos: */
		          //var elemento = moduloActual.obj.grupoProgramacion.getForm(contador).find("input[elemento-grupo='hrandomProgramacion']");
	              //var cmpRandomIdentificador = $(formularioNuevo).find("input[elemento-grupo='hrandomProgramacion']");
	              
	              /*if(cmpRandomIdentificador.val() == elemento.val()){
	            	  if(cmpElementoProducto.val() != 0){
	            		  isTieneProducto = true;
	            	  }
	              }else{
	            	  if(!isTieneProducto){
	            		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar el Producto.");
	  		            return false;
	            	  }
	              }*/
		          
	              /*if(cmpElementoProducto.val() == 0 && cmpCantidadCompartimientos.val() == 1){
		            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar el Producto.");
		            return false;
		          }*/
		          // Fin Ticket 9000002608
		          if(cmpElementoCisterna.val() == 0){
		            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar la Cisterna.");
		            return false;
		          }
		          
		          if(cmpElementoConductor.val() == 0){
			            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar el conductor.");
			            return false;
			      }
		          var idProducto       = parseInt(cmpElementoProducto.val());            
		          var idCisterna     = parseInt(cmpElementoCisterna.val());            
		          var idConductor  = parseInt(cmpElementoConductor.val());
		          
		          // Inicio Ticket 9000002608
		          var idCompartimento = parseInt(cmpCantidadCompartimientos.val());
		          
		          // Fin Ticket 9000002608
		          if( ! (idCisterna in _map_cis ) ){
		                _map_cis[idCisterna] = {idCisterna:idCisterna, cantidadCompartimentos:0,cantidadUsada:0, conductores:[],productos:[]};
		          }
		          // Inicio Ticket 9000002608
		          _listado.push({idProducto: idProducto, idCisterna:idCisterna , idConductor:idConductor, compartimento: idCompartimento})
		          // Fin Ticket 9000002608
		          
		          /*if(cmpElementoPlanta.val() == 0){
			            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar la planta.");
			            return false;
			      }*/
		          /*i:7000001924
		          for(var i = 0; i < numeroFormularios; i++){
		        	  if(contador!=i){
		    	          var formularioAux = referenciaModulo.obj.grupoProgramacion.getForm(i);	    	          
		    	          var cmpElementoCisternaAux   = formularioAux.find("select[elemento-grupo='cisterna']");
		    	          var cmpElementoConductorAux  = formularioAux.find("select[elemento-grupo='conductor']");
		    	          
		    	          if(cmpElementoCisternaAux.val() == cmpElementoCisterna.val()){
		    	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Verifique que no exista alguna cisterna duplicada.");
		  		              return false;
		    	          }
		    	          if(cmpElementoConductorAux.val() == cmpElementoConductor.val()){
		    	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Verifique que no exista algun conductor duplicado.");
		  		              return false;
		    	          }	    	          
		        	  }
		          }e:7000001924*/
		          
		          
		          
		      }
		      //7000001924
		      var  ix = 0;
		      var szL = _listado.length;
		      var itObjCisterna=null;
		      var itRegistro = null;
		      var _map_cond ={};
		      var _existe= false;
		      
		      var y = 0;
		      for (var i in _map_cis){
		    	 /* var formulario = referenciaModulo.obj.grupoProgramacion.getForm(y);
		          var cmpElementoProducto     = formulario.find("select[elemento-grupo='producto']");
		    	  if(cmpElementoProducto.val() == 1){*/
		    	  itObjCisterna =referenciaModulo.obj.cisterna.buscar(i);
			      _map_cis[i].cantidadCompartimentos= itObjCisterna.cantidadCompartimentos;  
		    	  //}
		    	  y++;
		      }
		      for(;ix<szL;ix++){
		           itObjCisterna =_map_cis[_listado[ix].idCisterna];
		           itObjCisterna.cantidadUsada=itObjCisterna.cantidadUsada+1;
		           _existe= false;
		           
		           if(!_existe){
		                itObjCisterna.conductores.push(_listado[ix].idConductor);
		           }
		           _existe= false;
		           
		           for(var j =0;j<itObjCisterna.productos.length;j++){
		               if(itObjCisterna.productos[j]==_listado[ix].idProducto){
		                   _existe= true;
		                   break;
		               }
		           }
		           
		           if(!_existe){
		        	   itObjCisterna.productos.push(_listado[ix].idProducto);
		           }
		      }
		      // Inicio Ticket 9000002608
		      var isTieneProducto = false;
		      for(var i = 0; i<itObjCisterna.productos.length; i++){
		    	  if( !isNaN( itObjCisterna.productos[i])){
		    		  isTieneProducto = true;
		    	  }
		      }
		      if(!isTieneProducto){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de seleccionar el Producto. ");
		          return false;
		      }
		      
		      moduloActual.duplicidadConductores = false;
		      moduloActual.duplicidadCisterna = false;
		      
		      var conductorId = 0;
		      var cisternaId = 0;
		      
		      for(var i = 0; i<_listado.length; i++){
		         if(_listado[i].idConductor == conductorId && _listado[i].compartimento == 1){
		        	 moduloActual.duplicidadConductores = true;
		        	 //break;
		         }
		         conductorId = _listado[i].idConductor;
		         
		         if(_listado[i].idCisterna == cisternaId && _listado[i].compartimento == 1){
		        	 moduloActual.duplicidadCisterna = true;
		         }
		         cisternaId = _listado[i].idCisterna;
		      }
		      
		      // Fin Ticket 9000002608
		      //
		      var itObjCisterna2=null;
		      for (var i in _map_cis){
		          itObjCisterna =_map_cis[i];
		          itObjCisterna_2 =referenciaModulo.obj.cisterna.buscar(i);

		          // Inicio Atención de ticket 7000002071
		          /*if(!( itObjCisterna.cantidadUsada==itObjCisterna.cantidadCompartimentos)){
		    	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Cantidad usada de compartimentos para la cisterna no coincide con lo configurado en el sistema. Placa: "+ itObjCisterna2.placa+" compartimentos: "+itObjCisterna2.cantidadCompartimentos);
		  		              return false;
		          }*/
		          // Fin Atención de ticket 7000002071
		          /*if(itObjCisterna.conductores.length>1){
		    	        	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Verifique que cada cisterna tenga el mismo conductor. Placa:" + itObjCisterna2.placa);
		  		              return false;
		          }*/
		       // Inicio Atención de ticket 7000002071
		          /*if(itObjCisterna.productos.length>itObjCisterna.cantidadCompartimentos){
		        	  	console.log('Se detectadron cisternas duplicadas!');
		    	        	  //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Verifique que no exista alguna cisterna duplicada.");
		  		              //return false;
		          }*/
		       // Fin Atención de ticket 7000002071
		           _map_cond[itObjCisterna.conductores[0]] = _map_cond[itObjCisterna.conductores[0]]==null?1: (_map_cond[itObjCisterna.conductores[0]])+1;
		     }
		     for (var ix in _map_cond){
		           /*if( _map_cond[ix]>1){
		    	        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Verifique que no exista algun conductor duplicado.");
		  		        return false;
		           }*/
		     }

	    }else{
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe de ingresar al menos una cisterna.");
            return false;
	    }
	     
	    return retorno;
	    } catch(error){
	          console.log(error.message);
	      
	    };
	  };
  
  moduloActual.recuperarValoresCompletar = function(registro){
	    var eRegistro = {};
	    var referenciaModulo=this;
	    try {
	      //datos para la asignacion		  
		  eRegistro.id = parseInt(referenciaModulo.idProgramacion); 
		  
	      eRegistro.idDiaOperativo = parseInt(referenciaModulo.idDiaOperativo);           
	      eRegistro.idTransaportista = parseInt(referenciaModulo.idTransportista);
	      eRegistro.programaciones=[];
	      //datos para detalleTransporte
	        var numeroFormularios = referenciaModulo.obj.grupoCompletar.getForms().length;
	        for(var contador = 0; contador < numeroFormularios; contador++){
	          var detalles={};
	          var formulario = referenciaModulo.obj.grupoCompletar.getForm(contador);
	          var cmpElementoProducto     = formulario.find("input[elemento-grupo='producto']").attr("data-idProducto");
	          var cmpElementoCisterna     = formulario.find("input[elemento-grupo='cisterna']").attr("data-idCisterna");
	          var cmpElementoConductor     = formulario.find("input[elemento-grupo='conductor']").attr("data-idConductor");
	          var cmpElementoPlanta       = formulario.find("select[elemento-grupo='planta']");
	          
	          var cmpElementoCodigoScop     = formulario.find("input[elemento-grupo='codigoScop']");
	          var cmpElementoCodigoSapPedido     = formulario.find("input[elemento-grupo='codigoSapPedido']");
	          var cmpElementoIdDetalle     = formulario.find("input[elemento-grupo='codigoScop']").attr("data-iddprogramacion");
	          
	          var cmpFormularioOrdenCompra = referenciaModulo.obj.frmCompletar[0].cmpCompletarOrdenCompra;
	          
	        //if(cmpElementoProducto.val() > 0){
	          	detalles.idProducto       = parseInt(cmpElementoProducto);            
	            detalles.idCisterna     = parseInt(cmpElementoCisterna);            
	            detalles.idConductor  = parseInt(cmpElementoConductor);
	            detalles.idPlanta  = parseInt(cmpElementoPlanta.val());
	            detalles.ordenCompra = cmpFormularioOrdenCompra.value;
	            detalles.codigoScop= cmpElementoCodigoScop.val();
	            detalles.codigoSapPedido= cmpElementoCodigoSapPedido.val();
	            detalles.id = parseInt(cmpElementoIdDetalle);
	            detalles.completar = 2; // flag que indica que SI es completar

	            eRegistro.programaciones.push(detalles);
	        //}
	        }
	        console.log(eRegistro);
	    }  catch(error){
	      console.log(error.message);
	    }
	    return eRegistro;
	  };
  
  
  
//========================== cntFormularioCompletar ==============================================
  moduloActual.llenarFormularioCompletar = function(registro){
  this.obj.grupoCompletar.removeAllForms();
  if (registro != null && registro.length>0) {
	  var programacion = registro[0];
	  //this.obj.cmpTransportista.text(programacion.obj.guiaRemisionSeleccionado);
	  var referenciaModulo=this;
	  
	  referenciaModulo.obj.frmCompletar[0].cmpCompletarOrdenCompra.value=programacion.ordenCompra;
	  referenciaModulo.obj.frmCompletar[0].cmpCompletarTransportista.value=programacion.programacion.transportista.razonSocial;
	  var numeroDetalles= registro.length;
	  for(var contador=0; contador < numeroDetalles; contador++){     
	      moduloActual.obj.grupoCompletar.addForm();
	      var formulario= moduloActual.obj.grupoCompletar.getForm(contador);
	      
	      var descripcion = registro[contador].planta.descripcion;		
		  var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
		  elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro[contador].idPlanta);
		  elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,descripcion);
		  formulario.find("select[elemento-grupo='planta']").select2("val", registro[contador].idPlanta);
		  formulario.find("select[elemento-grupo='planta']").empty().append(elemento).val(registro[contador].idPlanta).trigger('change');		  
		  
		  formulario.find("input[elemento-grupo='producto']").val(registro[contador].producto.nombre); // contador
		  formulario.find("input[elemento-grupo='producto']").attr("data-idProducto",registro[contador].producto.id); // contador
		  
		  var cisternaTracto= registro[contador].cisterna.placa +'/'+registro[contador].cisterna.tracto.placa;
		  formulario.find("input[elemento-grupo='cisterna']").val(cisternaTracto); // contador
		  formulario.find("input[elemento-grupo='cisterna']").attr("data-idCisterna",registro[contador].cisterna.id); // contador

		  var nombres = registro[contador].conductor.apellidos + ','+registro[contador].conductor.nombres;
		  formulario.find("input[elemento-grupo='conductor']").val(nombres); // contador
		  formulario.find("input[elemento-grupo='conductor']").attr("data-idConductor",registro[contador].conductor.id); // contador
		 
		  formulario.find("input[elemento-grupo='codigoScop']").val(registro[contador].codigoScop);
		  formulario.find("input[elemento-grupo='codigoScop']").attr("data-iddprogramacion",registro[contador].id); // contador		  
		  
		  formulario.find("input[elemento-grupo='codigoSapPedido']").val(registro[contador].codigoSapPedido); 
		 
		  // Inicio Atención Ticket 9000002608
		  formulario.find("input[elemento-grupo='compartimento']").val(registro[contador].numeroCompartimiento);
	      formulario.find("input[elemento-grupo='volumen']").val(registro[contador].capacidadVolumetrica);
		  // Fin Atención Ticket 9000002608		  
		  
	    }
	  }
  };   
  
  
  
  moduloActual.llenarDetalles = function(registro){
		//var referenciaModulo=this;
		var programacion = registro[0].programacion;	   
	    this.obj.vistaTransportista.text(programacion.transportista.razonSocial);
	    //referenciaModulo.obj.datDetalleAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	    
	    //this.obj.vistaListaPlanificaciones=$("#vistaListaPlanificaciones");
	    var pedidoSAP="";
	    var ordenCompra="";
	    var scop="";
	    var planta="";
	    var producto="";
	    var filaNueva="";
	    var tractoCisterna="";
	    var conductor="";
	    if(registro != null){
		    var indice= registro.length;		    
		    $("#tablaVistaDetalle tbody").empty();
		    //$("#tablaVistaDetalle tr").remove(); 
		    for(var k = 0; k < indice; k++){ 
		    	planta=registro[k].planta.descripcion;
		    	pedidoSAP=registro[k].codigoSapPedido;
		    	scop=registro[k].codigoScop;
		    	if(registro[k].planta.descripcion==null){
		    		planta="";
		    	}
		    	if(registro[k].codigoSapPedido==null){
		    		pedidoSAP="";
		    	}
		    	if(registro[k].codigoScop==null){
		    		scop="";
		    	}
		    	ordenCompra=registro[k].ordenCompra;
		    	producto=registro[k].producto.nombre;		    
		    	tractoCisterna= registro[k].cisterna.placa +'/'+registro[k].cisterna.tracto.placa;
		    	conductor = registro[k].conductor.nombreCompleto;	
		    	filaNueva='<tr><td class="text-left">'+pedidoSAP+'</td>'
		    	+'<td class="text-left">'+ordenCompra+'</td>'
		    	+'<td class="text-left">'+scop+'</td>'
		    	+'<td class="text-left">'+planta+'</td>'
		    	+'<td class="text-left">'+producto+'</td>'
		    	+'<td class="text-left">'+tractoCisterna+'</td>'
		    	+'<td class="text-center">'+registro[k].numeroCompartimiento+'</td>'
		    	+'<td class="text-center">'+registro[k].capacidadVolumetrica+'</td>'
		    	+'<td class="text-left">'+conductor+'</td></tr>'; // jmatosp		    	
		    	$("#tablaVistaDetalle > tbody:last").append(filaNueva);
		    }
		    //Vista de auditoria
		    this.obj.vistaActualizadoEl.text(programacion.fechaActualizacion);
		    this.obj.vistaActualizadoPor.text(programacion.usuarioActualizacion);
		    this.obj.vistaIpActualizacion.text(programacion.ipActualizacion);
		    this.obj.vistaCreadoPor.text(programacion.usuarioCreacion);
		    this.obj.vistaCreadoEl.text(programacion.fechaCreacion);
		    this.obj.vistaIpCreacion.text(programacion.ipCreacion);
	    }
	    else{
	    	$("#tablaVistaDetalle tbody tr").remove();
	    	this.obj.vistaActualizadoEl.text("");
		    this.obj.vistaActualizadoPor.text("");
		    this.obj.vistaIpActualizacion.text("");
		    this.obj.vistaCreadoPor.text("");
		    this.obj.vistaCreadoEl.text("");
		    this.obj.vistaIpCreacion.text("");
	    }
	    
	  };
  
  
//========================== cntFormulario ==============================================
  moduloActual.llenarFormulario = function(registro){
  // Inicio Ticket 9000002608
  var acumuladorCompartimento = 0;
  var acumuladorProgramados = 0;
  //Fin Ticket 9000002608
  
  this.obj.grupoProgramacion.removeAllForms();
  var numeroDetalles= registro.length;
  if (registro != null && numeroDetalles>0) {	
  var programacion = registro[0];
  //this.obj.cmpTransportista.text(programacion.obj.guiaRemisionSeleccionado);
  var referenciaModulo=this;
  var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,programacion.programacion.idTransportista);
  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,programacion.programacion.transportista.razonSocial);
  this.obj.cmpTransportista.empty().append(elemento1).val(programacion.programacion.idTransportista).trigger('change');  
  
  referenciaModulo.obj.frmPrincipal[0].cmpFormularioOrdenCompra.value=programacion.ordenCompra;	  
	  for(var contador=0; contador < numeroDetalles; contador++){     
	      moduloActual.obj.grupoProgramacion.addForm();
	      var formulario= moduloActual.obj.grupoProgramacion.getForm(contador);	      
	      var elemento3=constantes.PLANTILLA_OPCION_SELECTBOX;
		  elemento3 = elemento3.replace(constantes.ID_OPCION_CONTENEDOR,registro[contador].producto.id);
		  elemento3 = elemento3.replace(constantes.VALOR_OPCION_CONTENEDOR,registro[contador].producto.nombre);
		  
		  // Inicio Atención Ticket 9000002608
	      //formulario.find("select[elemento-grupo='producto']").empty().append(elemento3).val(registro[contador].producto.id).trigger('change');	      
		  formulario.find("select[elemento-grupo='producto']").val(registro[contador].producto.id).trigger('change');
		  // Fin Atención Ticket 9000002608
		  
		  formulario.find("input[elemento-grupo='hidentificador']").val(registro[contador].id);
	      formulario.find("input[elemento-grupo='hcodigoScop']").val(registro[contador].codigoScop);
	      formulario.find("input[elemento-grupo='hcodigoSapPedido']").val(registro[contador].codigoSapPedido);	
	      //cisterna-tracto
		  var cisternaTracto= registro[contador].cisterna.placa +'/'+registro[contador].cisterna.tracto.placa;
		  var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
	      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro[contador].cisterna.id);
	      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,cisternaTracto);
	      formulario.find("select[elemento-grupo='cisterna']").empty().append(elemento1).val(registro[contador].cisterna.id).trigger('change');
	      // Inicio Ticket 9000002608
	      formulario.find("input[elemento-grupo='tracto']").val(cisternaTracto);
	      formulario.find("input[elemento-grupo='compartimento']").val(registro[contador].numeroCompartimiento);
	      formulario.find("input[elemento-grupo='volumen']").val(registro[contador].capacidadVolumetrica);
	      formulario.find("input[elemento-grupo='tracto-id']").val(registro[contador].idCisterna);
	      // Calcular Totales:
	      acumuladorProgramados += parseInt(registro[contador].capacidadVolumetrica);
	      
	      if(registro[contador].numeroCompartimiento === '1' || registro[contador].numeroCompartimiento === 1){
	    	  acumuladorCompartimento += parseInt(registro[contador].numeroCompartimiento);
	    	  formulario.find("input[elemento-grupo='compartimento']").attr('style',  'color:red');
	    	  formulario.find("input[elemento-grupo='volumen']").attr('style',  'color:red');
	    	  formulario.find("input[elemento-grupo='tracto']").attr('style',  'color:red');
	    	  var cmpConductor = formulario.find("span[class='select2-selection__rendered']");
        	  cmpConductor.attr('style', 'color:red');
        	  
        	  moduloActual.identificadorFilaProgramacion = moduloActual.generarRandomId();
	      }else{
	    	  formulario.find("select[elemento-grupo='conductor']").attr('disabled','disabled');
	    	  formulario.find("a[elemento-grupo='botonElimina']").hide();
	    	  
	    	  formulario.find("input[elemento-grupo='compartimento']").removeAttr('style',  'color:red');
	    	  formulario.find("input[elemento-grupo='volumen']").removeAttr('style',  'color:red');
	    	  formulario.find("input[elemento-grupo='tracto']").removeAttr('style',  'color:red');
	    	  var cmpConductor = formulario.find("span[class='select2-selection__rendered']");
        	  cmpConductor.removeAttr('style', 'color:red');
	    	  
	      }
	      $('#cmpCisternasProgramadas').val(acumuladorCompartimento);
	      $('#cmpVolumenProgramado').val(acumuladorProgramados);
	      
	      var cmpRandomIdentificador = formulario.find("input[elemento-grupo='hrandomProgramacion']");
          cmpRandomIdentificador.val(moduloActual.identificadorFilaProgramacion);
          console.log('Random actualizar: ' +cmpRandomIdentificador.val());
	      
	      // Fin Ticket 9000002608
	      //conductor 
		  var nombres = registro[contador].conductor.apellidos + ','+registro[contador].conductor.nombres;		
		  var elemento2=constantes.PLANTILLA_OPCION_SELECTBOX;
	      elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,registro[contador].conductor.id);
	      elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR,nombres);
		  formulario.find("select[elemento-grupo='conductor']").select2("val", registro[contador].conductor.id);
		  formulario.find("select[elemento-grupo='conductor']").empty().append(elemento2).val(registro[contador].conductor.id).trigger('change');
	    }
	  }
  };    

//========================== FIN cntPesajeTransporte ========================================================    

moduloActual.validarFechaEmision = function(retorno){
    referenciaModulo = this;
    retorno = true;
    try{
      var fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
      console.log(fechaEmisionGuia);
      var fechaPlanificada = utilitario.formatearStringToDate(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
      
      console.log(fechaPlanificada);
      
      if(fechaEmisionGuia > fechaPlanificada){
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La fecha de Emision O/E no puede ser mayor a la fecha planificada. ");
          referenciaModulo.obj.ocultaContenedorFormulario.hide();
          retorno = false;
        return retorno;
    }
    return retorno;
  }
  catch(error){
        console.log(error.message);
  }
};    
  
  //valida que almenos un registro este correctamente lleno
  moduloActual.validarUnDetalleTransporte = function(retorno){
    referenciaModulo = this;
    retorno = false;
    try{
      var numeroFormularios = referenciaModulo.obj.grupoProgramacion.getForms().length;
      for(var contador = 0; contador < numeroFormularios; contador++){
            var formulario = referenciaModulo.obj.grupoProgramacion.getForm(contador);
            var cmpElementoProducto     = formulario.find("select[elemento-grupo='producto']");
            var cmpVolumenTempObservada   = formulario.find("input[elemento-grupo='volumenTempObservada']");
            var cmpTemperatura        = formulario.find("input[elemento-grupo='temperatura']");
            var cmpAPI            = formulario.find("input[elemento-grupo='API']");
            var cmpFactor         = formulario.find("input[elemento-grupo='factor']");
            var cmpVolumen60F       = formulario.find("input[elemento-grupo='volumen60F']");

            if((cmpElementoProducto != null && parseInt(cmpElementoProducto.val())>0)&&
              (cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) > 0 )  &&
              (cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) > 0 )  &&
              (cmpTemperatura!=null   && parseFloat(cmpTemperatura.val()) > 0 ) &&
              (cmpAPI!=null     && parseFloat(cmpAPI.val()) > 0 ) &&
              (cmpFactor!=null    && parseFloat(cmpFactor.val()) > 0 ) &&
              (cmpVolumen60F!=null  && parseFloat(cmpVolumen60F.val()) > 0 )){
              return true;
            }
            else{
              referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Al menos un detalle de transporte debe estar correctamente informado.");
              referenciaModulo.obj.ocultaContenedorFormulario.hide();
            }
        }
      console.log(retorno);
      return retorno;
      } catch(error){
            console.log(error.message);
        
      };
    };
  
    
    
    $('#cmbCisterna').on('click', function(e){
    	console.log('Iniciando carga de datos....');
    });
    
    
  moduloActual.validarFechaEmision = function(retorno){
    referenciaModulo = this;
    retorno = true;
    try{
      var fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
      console.log(fechaEmisionGuia);
      var fechaPlanificada = utilitario.formatearStringToDate(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
      
      console.log(fechaPlanificada);
      
      if(fechaEmisionGuia > fechaPlanificada){
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La fecha de Emision O/E no puede ser mayor a la fecha planificada. ");
            referenciaModulo.obj.ocultaContenedorFormulario.hide();
            retorno = false;
            return retorno;
      }
      return retorno;
    }
    catch(error){
          console.log(error.message);
    }
  };  
  moduloActual.inicializar();
});

