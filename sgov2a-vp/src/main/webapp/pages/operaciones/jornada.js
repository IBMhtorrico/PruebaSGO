$(document).ready(function(){
  var moduloActual = new moduloJornada();
  moduloActual.urlBase='jornada';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR_JORNADA = './jornada/listar';
  moduloActual.URL_RECUPERAR_IMPORTACION = './despachoCarga/recuperar';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_APERTURA = moduloActual.urlBase + '/recuperar-apertura';
  moduloActual.URL_RECUPERAR_ULTIMA_JORNADA = moduloActual.urlBase + '/recuperar-ultimo-dia';
  moduloActual.URL_GUARDAR_CAMBIO_TANQUE = moduloActual.urlBase + '/registrarCambioTanque';

  // listado de jornadaf
  moduloActual.ordenGrillaJornada=[[ 2, 'asc' ]];
  moduloActual.columnasGrillaJornada.push({ "data": 'id'}); // Target1
  moduloActual.columnasGrillaJornada.push({ "data": 'estacion.id'});// Target2
  moduloActual.columnasGrillaJornada.push({ "data": 'estacion.nombre'});// Target3
  moduloActual.columnasGrillaJornada.push({ "data": 'fechaOperativa'});// Target4
  moduloActual.columnasGrillaJornada.push({ "data": 'totalDespachos'});// Target5
  moduloActual.columnasGrillaJornada.push({ "data": 'fechaActualizacion'});// Target6
  moduloActual.columnasGrillaJornada.push({ "data": 'usuarioActualizacion'});// Target7
  moduloActual.columnasGrillaJornada.push({ "data": 'estado'});// Target8
  
  // Columnas jornada
  moduloActual.definicionColumnasJornada.push({"targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasJornada.push({"targets" : 2, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnasJornada.push({"targets" : 3, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnasJornada.push({"targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnasJornada.push({"targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnasJornada.push({"targets" : 6, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center" });
  moduloActual.definicionColumnasJornada.push({"targets" : 7, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-rigth" });
  moduloActual.definicionColumnasJornada.push({"targets" : 8, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearEstadoJornada });

  moduloActual.reglasValidacionFormulario={
	cmpIdVehiculo:			{required: true },
	cmpIdClasificacion: 	{required: true },
	cmpNumeroVale: 			{required: true },
	cmpFechaInicio: 		{required: true },
	cmpFechaFin: 			{required: true },
	cmpIdProducto: 			{required: true },
	cmpIdContometro: 		{required: true },
	cmpIdTanque: 			{required: true },
  };

  moduloActual.mensajesValidacionFormulario={
    cmpIdVehiculo:  		{required: "El campo es obligatorio" },
    cmpIdClasificacion: 	{required: "El campo es obligatorio" },
    cmpNumeroVale: 			{required: "El campo es obligatorio" },
    cmpFechaInicio: 		{required: "El campo es obligatorio" },
    cmpFechaFin: 			{required: "El campo es obligatorio" },
	cmpIdProducto: 			{required: "El campo es obligatorio" },
	cmpIdContometro: 		{required: "El campo es obligatorio" },
	cmpIdTanque: 			{required: "El campo es obligatorio" },
  };

  moduloActual.inicializarCampos= function(){
	this.obj.cantTurnosEstacion =$("#cantTurnosEstacion");
    this.obj.idOperacionSeleccionado =$("#idOperacionSeleccionado");
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
    
    // campos para formulario apertura
    this.obj.cmpAperturaCliente=$("#cmpAperturaCliente");
    this.obj.cmpAperturaOperacion=$("#cmpAperturaOperacion");
    this.obj.cmpAperturaEstacion=$("#cmpAperturaEstacion");
    this.obj.cmpAperturaFechaJornada=$("#cmpAperturaFechaJornada");
    this.obj.cmpObservacionApertura=$("#cmpObservacionApertura");
    this.obj.cmpObservacionCierre=$("#cmpObservacionCierre");
    this.obj.cmpHoraCierre=$("#cmpHoraCierre");
    this.obj.cmpHoraCierre.inputmask("h:s:s"); 
    this.obj.cmpHoraApertura=$("#cmpHoraApertura");
    this.obj.cmpHoraApertura.inputmask("h:s:s"); 
    
    this.obj.cmpOperador1=$("#cmpOperador1");
    this.obj.cmpOperador1.tipoControl="select2";
    this.obj.cmpSelect2Operario1=$("#cmpOperador1").select2({
  	  ajax: {
  		  
  		    url: "./operario/listar",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		      return {
  		    	valorBuscado: parametros.term, // search term
  		        page: parametros.page,
  		        paginacion:0,
  		        indicadorOperario: constantes.INDICADOR_OPERARIO_RESPONSABLE,
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
    
    this.obj.cmpOperador2=$("#cmpOperador2");
    this.obj.cmpOperador2.tipoControl="select2";
    this.obj.cmpSelect2Operario2=$("#cmpOperador2").select2({
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
    this.obj.GrupoAperturaContometros = $('#GrupoAperturaContometros').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdContometros=$(formularioNuevo).find("input[elemento-grupo='idContometros']");
          var cmpContometros=$(formularioNuevo).find("input[elemento-grupo='contometros']");
          var cmpProductoContometro=$(formularioNuevo).find("select[elemento-grupo='productosContometros']");
          var cmpLecturaInicial=$(formularioNuevo).find("input[elemento-grupo='lecturaInicial']");

          cmpLecturaInicial.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpProductoContometro.tipoControl="select2";
          moduloActual.obj.cmpSelect2Producto=$(formularioNuevo).find("select[elemento-grupo='productosContometros']").select2({
    	  ajax: {
    		    url: "./producto/listarPorOperacion",
    		    dataType: 'json',
    		    delay: 250,
    		    "data": function (parametros) {
    		    	try{
    			      return {
    			    	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
    			    	filtroEstacion: moduloActual.obj.filtroEstacion.val(),
    			    	filtroEstado: constantes.ESTADO_ACTIVO,
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
        
        cmpProductoContometro.on('change', function(e){
          var formulario = moduloActual.obj.GrupoAperturaContometros.getForm(($(formularioNuevo).attr('id')).substring(33));  
          formulario.find("input[elemento-grupo='lecturaInicial']").val(""); // lecturaInicial
        });
 
      },
    }); 

    this.obj.grupoAperturaTanques = $('#GrupoAperturaTanques').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdTanques=$(formularioNuevo).find("input[elemento-grupo='idTanques']");
          var cmpTanques=$(formularioNuevo).find("input[elemento-grupo='tanques']");
          var cmpIdProductosTanques=$(formularioNuevo).find("input[elemento-grupo='idProductosTanques']");
          var cmpProductosTanques=$(formularioNuevo).find("input[elemento-grupo='productosTanques']");
          //var cmpProductosTanques=$(formularioNuevo).find("select[elemento-grupo='productosTanques']");
          var cmpMedidaInicial=$(formularioNuevo).find("input[elemento-grupo='medidaInicial']");
          var cmpVolObsInicial=$(formularioNuevo).find("input[elemento-grupo='volObsInicial']");
          var cmpApi60=$(formularioNuevo).find("input[elemento-grupo='api60']");
          var cmpTemperatura=$(formularioNuevo).find("input[elemento-grupo='temperatura']");
          var cmpFactor=$(formularioNuevo).find("input[elemento-grupo='factor']");
          var cmpVol60=$(formularioNuevo).find("input[elemento-grupo='vol60']");
          var cmpFs=$(formularioNuevo).find("input[elemento-grupo='fs']");
          var cmpDesp=$(formularioNuevo).find("input[elemento-grupo='desp']");
          
          cmpIdTanques.inputmask('decimal', {digits: 0});
          cmpMedidaInicial.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolObsInicial.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          
          cmpTemperatura.inputmask("99.9");
          cmpApi60.inputmask("99.9");

          cmpVol60.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
         
          /*cmpProductosTanques.tipoControl="select2";
          moduloActual.obj.cmpSelect2ProductoTanque=$(formularioNuevo).find("select[elemento-grupo='productosTanques']").select2({
    	  ajax: {
    		    url: "./producto/listarPorOperacion",
    		    dataType: 'json',
    		    delay: 250,
    		    "data": function (parametros) {
    		    	try{
    			      return {
    			    	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
    			    	// indicadorProducto:
						// constantes.INDICADOR_PRODUCTO_SIN_DATOS,
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
          });*/
          
          cmpApi60.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(29);
        	  moduloActual.calcularFactorCorreccion(cmpApi60.val(), cmpTemperatura.val(), "grupoAperturaTanques", indiceFormulario);

          });
         
          cmpTemperatura.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(29);
        	  moduloActual.calcularFactorCorreccion(cmpApi60.val(), cmpTemperatura.val(), "grupoAperturaTanques", indiceFormulario);
          });
          
         /* cmpTemperatura.on("change",function(){
        	  console.log("entra en cmpTemperatura change");
            var idElemento = $(this).attr("id");
            moduloActual.calcularFactor(idElemento);
          });
         
          cmpApi60.on("change",function(){
        	  console.log("entra en cmpApi60 change");
            var idElemento = $(this).attr("id");
            moduloActual.calcularFactor(idElemento);
          });*/
          
          cmpProductosTanques.on('change', function(e){
        	var formulario = moduloActual.obj.grupoAperturaTanques.getForm(($(formularioNuevo).attr('id')).substring(29));  
        	formulario.find("input[elemento-grupo='medidaInicial']").val(""); // volumen_temperatura_observada
        	formulario.find("input[elemento-grupo='volObsInicial']").val(""); // volumen_temperatura_observada
        	formulario.find("input[elemento-grupo='api60']").val(""); // volumen_temperatura_observada
        	formulario.find("input[elemento-grupo='temperatura']").val(""); // volumen_temperatura_observada
        	formulario.find("input[elemento-grupo='factor']").val(""); // volumen_temperatura_observada
        	formulario.find("input[elemento-grupo='vol60']").val(""); // volumen_temperatura_observada
          });
        },
      });
    
    // campos para formulario cierre
    this.obj.cmpCierreCliente=$("#cmpCierreCliente");
    this.obj.cmpCierreOperacion=$("#cmpCierreOperacion");
    this.obj.cmpCierreEstacion=$("#cmpCierreEstacion");
    this.obj.cmpCierreFechaJornada=$("#cmpCierreFechaJornada");
    this.obj.cmpCierreOperador1=$("#cmpCierreOperador1");
    this.obj.cmpCierreOperador2=$("#cmpCierreOperador2");
    
    this.obj.GrupoCierreProducto = $('#GrupoCierreProducto').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdMuestreo=$(formularioNuevo).find("input[elemento-grupo='idMuestreo']");
          var cmpIdProducto=$(formularioNuevo).find("input[elemento-grupo='idProducto']");
          var cmpProducto=$(formularioNuevo).find("input[elemento-grupo='producto']");
          var cmpApiProducto=$(formularioNuevo).find("input[elemento-grupo='apiProducto']");
          var cmpTemperaturaProducto=$(formularioNuevo).find("input[elemento-grupo='temperaturaProducto']");
          var cmpFactorProducto=$(formularioNuevo).find("input[elemento-grupo='factorProducto']");

          cmpApiProducto.inputmask("99.9");
          cmpTemperaturaProducto.inputmask("99.9");

          cmpApiProducto.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(28);
        	  moduloActual.calcularFactorCorreccion(cmpApiProducto.val(), cmpTemperaturaProducto.val(), "GrupoCierreProducto", indiceFormulario);

          });
         
          cmpTemperaturaProducto.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(28);
        	  moduloActual.calcularFactorCorreccion(cmpApiProducto.val(), cmpTemperaturaProducto.val(), "GrupoCierreProducto", indiceFormulario);
          });
      },
    }); 
    
    this.obj.GrupoCierreContometros = $('#GrupoCierreContometros').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdContometroJornadaCierre=$(formularioNuevo).find("input[elemento-grupo='idContometroJornadaCierre']");
          var cmpIdContometroCierre=$(formularioNuevo).find("input[elemento-grupo='idContometroCierre']");
          var cmpContometrosCierre=$(formularioNuevo).find("input[elemento-grupo='contometrosCierre']");
          var cmpIdCierreProductoContometro=$(formularioNuevo).find("input[elemento-grupo='idCierreProductoContometro']");
          var cmpCierreProductoContometro=$(formularioNuevo).find("input[elemento-grupo='cierreProductoContometro']");
          var cmpLecturaInicial=$(formularioNuevo).find("input[elemento-grupo='lecturaInicial']");
          var cmpLecturaFinal=$(formularioNuevo).find("input[elemento-grupo='lecturaFinal']");
          var cmpDiferencia=$(formularioNuevo).find("input[elemento-grupo='diferencia']");
          var cmpServicio=$(formularioNuevo).find("input[elemento-grupo='servicio']");

          cmpLecturaFinal.inputmask('decimal', {digits: 0});
          
          cmpLecturaFinal.on("input",function(){
            try{
              var diferencia = parseFloat(cmpLecturaFinal.val()) - parseFloat(cmpLecturaInicial.val()); 
              cmpDiferencia.val(diferencia);
            } catch(error){
              console.log(error.message);
            }
          }); 
      },
    });
    
    this.obj.GrupoCierreTanques = $('#GrupoCierreTanques').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
       	  var cmpIdTanqueJornadaCierre=$(formularioNuevo).find("input[elemento-grupo='idTanqueJornadaCierre']");
          var cmpIdTanqueCierre=$(formularioNuevo).find("input[elemento-grupo='idTanqueCierre']");
          var cmpTanqueCierre=$(formularioNuevo).find("input[elemento-grupo='tanqueCierre']");
          var cmpIdCierreProductoTanque=$(formularioNuevo).find("input[elemento-grupo='idCierreProductoTanque']");
          var cmpCierreProductoTanque=$(formularioNuevo).find("input[elemento-grupo='cierreProductoTanque']");
          // medidas iniciales
          var cmpMedidaInicialTanque=$(formularioNuevo).find("input[elemento-grupo='medidaInicialTanque']");
          var cmpVolObsInicialTanque=$(formularioNuevo).find("input[elemento-grupo='volObsInicialTanque']");
          var cmpApi60InicialTanque=$(formularioNuevo).find("input[elemento-grupo='api60InicialTanque']");
          var cmpFactorInicialTanque=$(formularioNuevo).find("input[elemento-grupo='factorInicialTanque']");
          var cmpTemperaturaInicialTanque=$(formularioNuevo).find("input[elemento-grupo='temperaturaInicialTanque']");
          var cmpVol60InicialTanque=$(formularioNuevo).find("input[elemento-grupo='vol60InicialTanque']");
          var cmpFsInicialTanque=$(formularioNuevo).find("input[elemento-grupo='fsInicialTanque']");
          var cmpDespInicialTanque=$(formularioNuevo).find("input[elemento-grupo='despInicialTanque']");
          // medidas finales
          var cmpMedidaFinalTanque=$(formularioNuevo).find("input[elemento-grupo='medidaFinalTanque']");
          var cmpVolObsFinalTanque=$(formularioNuevo).find("input[elemento-grupo='volObsFinalTanque']");
          var cmpApi60FinalTanque=$(formularioNuevo).find("input[elemento-grupo='api60FinalTanque']");
          var cmpFactorFinalTanque=$(formularioNuevo).find("input[elemento-grupo='factorFinalTanque']");
          var cmpTemperaturaFinalTanque=$(formularioNuevo).find("input[elemento-grupo='temperaturaFinalTanque']");
          var cmpVol60FinalTanque=$(formularioNuevo).find("input[elemento-grupo='vol60FinalTanque']");
          var cmpVolAguaFinalTanque=$(formularioNuevo).find("input[elemento-grupo='volAguaFinalTanque']");
          var cmpFsFinalTanque=$(formularioNuevo).find("input[elemento-grupo='fsFinalTanque']");
          var cmpDespFinalTanque=$(formularioNuevo).find("input[elemento-grupo='despFinalTanque']");
          
          cmpMedidaInicialTanque.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          //cmpMedidaFinalTanque.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolObsInicialTanque.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolObsFinalTanque.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          
          cmpTemperaturaFinalTanque.inputmask("99.9");
          cmpApi60FinalTanque.inputmask("99.9");
          
          cmpVol60InicialTanque.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVol60FinalTanque.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolAguaFinalTanque.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          
          cmpMedidaFinalTanque.on("change",function(){
        	  console.log("cmpMedidaFinalTanque......on(change,function()");
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
        	  moduloActual.calcularVolumenObservadoFinal(cmpMedidaFinalTanque.val(), "GrupoCierreTanques", indiceFormulario);
          });
          
          cmpTemperaturaFinalTanque.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
        	  moduloActual.calcularFactorCorreccion(cmpApi60FinalTanque.val(), cmpTemperaturaFinalTanque.val(), "GrupoCierreTanques", indiceFormulario);
          });
         
          cmpApi60FinalTanque.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
        	  moduloActual.calcularFactorCorreccion(cmpApi60FinalTanque.val(), cmpTemperaturaFinalTanque.val(), "GrupoCierreTanques", indiceFormulario);
          });

      },
    });

    // campos para detalle de jornada
    this.obj.cmpDetalleCliente=$("#cmpDetalleCliente");
    this.obj.cmpDetalleOperacion=$("#cmpDetalleOperacion");
    this.obj.cmpDetalleEstacion=$("#cmpDetalleEstacion");
    this.obj.cmpDetalleFechaJornada=$("#cmpDetalleFechaJornada");
    this.obj.cmpDetalleOperador1=$("#cmpDetalleOperador1");
    this.obj.cmpDetalleOperador2=$("#cmpDetalleOperador2");
    this.obj.cmpDetalleEstado=$("#cmpDetalleEstado");
    this.obj.cmpDetalleObservacion=$("#cmpDetalleObservacion");
    
    // campos para formulario cambio de tanque
    this.obj.cmpCambioTanqueCliente=$("#cmpCambioTanqueCliente");
    this.obj.cmpCambioTanqueOperacion=$("#cmpCambioTanqueOperacion");
    this.obj.cmpCambioTanqueEstacion=$("#cmpCambioTanqueEstacion");
    this.obj.cmpCambioTanqueFechaJornada=$("#cmpCambioTanqueFechaJornada");
    this.obj.cmpCambioTanqueOperador1=$("#cmpCambioTanqueOperador1");
    this.obj.cmpCambioTanqueOperador2=$("#cmpCambioTanqueOperador2");
    this.obj.cmpCambioTanqueEstado==$("#cmpCambioTanqueEstado");
    // tanque a finalizar
    this.obj.GrupoCambioTanquesFinal = $('#GrupoCambioTanquesFinal').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 1,
        iniFormsCount: 1,
        afterAdd: function(origen, formularioNuevo) {
          //tanque a cerrar
       	  var cmpIdTanqueJornadaFinal = $(formularioNuevo).find("select[elemento-grupo='idTanqueJornadaFinal']");
       	  var cmpIdTanqueFinal=$(formularioNuevo).find("input[elemento-grupo='idTanqueFinal']");
       	  var cmpDescripcionTanqueFinal=$(formularioNuevo).find("input[elemento-grupo='descripcionTanqueFinal']");
          var cmpIdProductoFinal=$(formularioNuevo).find("input[elemento-grupo='idProductoFinal']");
          var cmpDescripcionProductoFinal=$(formularioNuevo).find("input[elemento-grupo='descripcionProductoFinal']");
          //medidas del tanque a cerrar
          var cmpHoraFinal=$(formularioNuevo).find("input[elemento-grupo='horaFinal']");
          var cmpMedidaFinal=$(formularioNuevo).find("input[elemento-grupo='medidaFinal']");
          var cmpVolObsFinal=$(formularioNuevo).find("input[elemento-grupo='volObsFinal']");
          var cmpApi60Final=$(formularioNuevo).find("input[elemento-grupo='api60Final']");
          var cmpTemperaturaFinal=$(formularioNuevo).find("input[elemento-grupo='temperaturaFinal']");
          var cmpFactorFinal=$(formularioNuevo).find("input[elemento-grupo='factorFinal']");
          var cmpVol60Final=$(formularioNuevo).find("input[elemento-grupo='vol60Final']");
          var cmpVolAguaFinal=$(formularioNuevo).find("input[elemento-grupo='volAguaFinal']");
          var cmpFsFinal=$(formularioNuevo).find("input[elemento-grupo='fsFinal']");
          var cmpDespFinal=$(formularioNuevo).find("input[elemento-grupo='despFinal']");

          cmpHoraFinal.inputmask("d/m/y h:s:s");

          cmpHoraFinal.on("input",function(){
         	  var formularioInicial = moduloActual.obj.GrupoCambioTanquesInicial.getForm(0);
          	  formularioInicial.find("input[elemento-grupo='horaInicial']").val(cmpHoraFinal.val());
          });

          cmpIdTanqueJornadaFinal.tipoControl="select2";
          moduloActual.obj.cmpSelect2TanqueJornadaFinal=$(formularioNuevo).find("select[elemento-grupo='idTanqueJornadaFinal']").select2({
	    	  ajax: {
	    		url: "./tanqueJornada/listar",
	  		    dataType: 'json',
	  		    delay: 250,
	  		    "data": function (parametros) {
	  		    	try{
	  			      return {
	  			    	valorBuscado: parametros.term,
  				        page: parametros.page,
  				        paginacion:0,
  				        idOperacion: moduloActual.obj.filtroOperacion.val(),
  				        idJornada: moduloActual.obj.idJornada,
  				        estadoDespachando: constantes.ESTADO_DESPACHANDO
	  			    };
	  		      } catch(error){
	  		    		console.log(error.message);
	    		  };
	  		    },
	  		    processResults: function (respuesta, pagina) {
	  		      var resultados= respuesta.contenido.carga;
	  		      return { 
	  		    	  results: resultados
	  		      };
	  		    },
	  		    cache: true
	  		  },
	  		"language": "es",
	  		"escapeMarkup": function (markup) { return markup; },
	  		"templateResult": function (registro) {
	  			if (registro.loading) {
	  				return "Buscando...";
	  			}
	  	        return "<div class='select2-user-result'>" + registro.descripcionTanque + "</div>";
	  	    },
	  	    "templateSelection": function (registro) {
	  	    	try{
	  	    		cmpDescripcionTanqueFinal.val(registro.descripcionTanque);
	  	    		cmpIdTanqueFinal.val(registro.idTanque);
	  	    		cmpIdProductoFinal.val(registro.producto.id);
	                cmpDescripcionProductoFinal.val(registro.producto.nombre);
	  	    	 } catch(error){
 		    		console.log(error.message);
	  	    	 };
	  	    	
	             return registro.descripcionTanque || registro.text;
	  	    },
	      });

          cmpMedidaFinal.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolObsFinal.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpApi60Final.inputmask("99.9");
          cmpTemperaturaFinal.inputmask("99.9");
          cmpVol60Final.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolAguaFinal.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          
          cmpApi60Final.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(32);
        	  moduloActual.calcularFactorCorreccion(cmpApi60Final.val(), cmpTemperaturaFinal.val(), "GrupoCambioTanquesFinal", indiceFormulario);
          });
         
          cmpTemperaturaFinal.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(32);
        	  moduloActual.calcularFactorCorreccion(cmpApi60Final.val(), cmpTemperaturaFinal.val(), "GrupoCambioTanquesFinal", indiceFormulario);
          });
          
          cmpMedidaFinal.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(32);
        	  cmpVolObsFinal.val(0);
        	  moduloActual.recuperarVolumen(cmpIdTanqueFinal.val(), cmpMedidaFinal.val(), "GrupoCambioTanquesFinal", indiceFormulario);
          });
       },
    });
    // tanque a aperturar
    this.obj.GrupoCambioTanquesInicial = $('#GrupoCambioTanquesInicial').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 1,
        iniFormsCount: 1,
        afterAdd: function(origen, formularioNuevo) {
          //tanque a aperturar
          var cmpIdTanqueJornadaInicial = $(formularioNuevo).find("select[elemento-grupo='idTanqueJornadaInicial']");
          var cmpIdTanqueInicial=$(formularioNuevo).find("input[elemento-grupo='idTanqueInicial']");
          var cmpDescripcionTanqueInicial=$(formularioNuevo).find("input[elemento-grupo='descripcionTanqueInicial']");
          var cmpIdProductoInicial=$(formularioNuevo).find("input[elemento-grupo='idProductoInicial']");
          var cmpDescripcionProductoInicial=$(formularioNuevo).find("input[elemento-grupo='descripcionProductoInicial']");
          //medidas del tanque a cerrar
          var cmpHoraInicial=$(formularioNuevo).find("input[elemento-grupo='horaInicial']");
          var cmpMedidaInicial=$(formularioNuevo).find("input[elemento-grupo='medidaInicial']");
          var cmpVolObsInicial=$(formularioNuevo).find("input[elemento-grupo='volObsInicial']");
          var cmpApi60Inicial=$(formularioNuevo).find("input[elemento-grupo='api60Inicial']");
          var cmpTemperaturaInicial=$(formularioNuevo).find("input[elemento-grupo='temperaturaInicial']");
          var cmpFactorInicial=$(formularioNuevo).find("input[elemento-grupo='factorInicial']");
          var cmpVol60Inicial=$(formularioNuevo).find("input[elemento-grupo='vol60Inicial']");
          var cmpFsInicial=$(formularioNuevo).find("input[elemento-grupo='fsInicial']");
          var cmpDespInicial=$(formularioNuevo).find("input[elemento-grupo='despInicial']");

          cmpHoraInicial.inputmask("d/m/y h:s:s");
          cmpIdTanqueJornadaInicial.tipoControl="select2";
          moduloActual.obj.cmpSelect2TanqueJornadaInicial=$(formularioNuevo).find("select[elemento-grupo='idTanqueJornadaInicial']").select2({
	    	  ajax: {
	    		url: "./tanqueJornada/listar",
	  		    dataType: 'json',
	  		    delay: 250,
	  		    "data": function (parametros) {
	  		    	try{
	  			      return {
	  			    	valorBuscado: parametros.term,
  				        page: parametros.page,
  				        paginacion:0,
  				        idOperacion: moduloActual.obj.filtroOperacion.val(),
  				        idJornada: moduloActual.obj.idJornada,
  				        estadoDespachando:  constantes.ESTADO_NO_DESPACHANDO,
  				        tanqueDeApertura: 1, // que estÃ© esn el listado de los tanques de apertura
  				        txtFiltro: " id_tanque not in (SELECT ID_tanque FROM sgo.v_tanque_jornada where en_linea = 1) "
	  			    };
	  		      } catch(error){
	  		    		console.log(error.message);
	    		  };
	  		    },
	  		    processResults: function (respuesta, pagina) {
	  		      var resultados= respuesta.contenido.carga;
	  		      return { 
	  		    	  results: resultados
	  		      };
	  		    },
	  		    cache: true
	  		  },
	  		"language": "es",
	  		"escapeMarkup": function (markup) { return markup; },
	  		"templateResult": function (registro) {
	  			if (registro.loading) {
	  				return "Buscando...";
	  			}
	  	        return "<div class='select2-user-result'>" + registro.descripcionTanque + "</div>";
	  	    },
	  	    "templateSelection": function (registro) {
	  	    	try{
	  	    		cmpDescripcionTanqueInicial.val(registro.descripcionTanque);
	  	    		cmpIdTanqueInicial.val(registro.idTanque);
	  	    		cmpIdProductoInicial.val(registro.producto.id);
	                cmpDescripcionProductoInicial.val(registro.producto.nombre);
	  	    	 } catch(error){
 		    		console.log(error.message);
	  	    	 };
	             return registro.descripcionTanque || registro.text;
	  	    },
	      });

          cmpMedidaInicial.inputmask('decimal', {digits: 0, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpVolObsInicial.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpApi60Inicial.inputmask("99.9");
          cmpTemperaturaInicial.inputmask("99.9");
          cmpVol60Inicial.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          
          cmpApi60Inicial.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(34);
        	  moduloActual.calcularFactorCorreccion(cmpApi60Inicial.val(), cmpTemperaturaInicial.val(), "GrupoCambioTanquesInicial", indiceFormulario);
          });
         
          cmpTemperaturaInicial.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(34);
        	  moduloActual.calcularFactorCorreccion(cmpApi60Inicial.val(), cmpTemperaturaInicial.val(), "GrupoCambioTanquesInicial", indiceFormulario);
          });
          
          cmpMedidaInicial.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(34);
        	  cmpVolObsInicial.val(0);
        	  moduloActual.recuperarVolumen(cmpIdTanqueInicial.val(), cmpMedidaInicial.val(), "GrupoCambioTanquesInicial", indiceFormulario);
          });
      },
    });

    // campos para formulario muestreo
    this.obj.cmpMuestreoCliente=$("#cmpMuestreoCliente");
    this.obj.cmpMuestreoOperacion=$("#cmpMuestreoOperacion");
    this.obj.cmpMuestreoEstacion=$("#cmpMuestreoEstacion");
    this.obj.cmpMuestreoFechaJornada=$("#cmpMuestreoFechaJornada");
    this.obj.cmpMuestreoOperador1=$("#cmpMuestreoOperador1");
    this.obj.cmpMuestreoOperador2=$("#cmpMuestreoOperador2");
    this.obj.cmpMuestreoEstado=$("#cmpMuestreoEstado");
    
    this.indiceFormulario=$("#indiceFormulario");
    this.registrosEliminar=$("#registrosEliminar");
    
    this.obj.GrupoMuestreo = $('#GrupoMuestreo').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpIdRegistro=$(formularioNuevo).find("input[elemento-grupo='identificador']");
          var cmpProducto=$(formularioNuevo).find("select[elemento-grupo='producto']");
          var cmpApi60=$(formularioNuevo).find("input[elemento-grupo='api60']");
          var cmpTemperatura=$(formularioNuevo).find("input[elemento-grupo='temperatura']");
          var cmpFactor=$(formularioNuevo).find("input[elemento-grupo='factor']");
          var cmpHoraMuestra=$(formularioNuevo).find("input[elemento-grupo='horaMuestra']");
          var cmpModifica=$(formularioNuevo).find("[elemento-grupo='botonModifica']");
  	      var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");

  	    cmpProducto.tipoControl="select2";
        moduloActual.obj.cmpSelect2Producto=$(formularioNuevo).find("select[elemento-grupo='producto']").select2({
      	  ajax: {
    		    url: "./producto/listarPorOperacion",
    		    dataType: 'json',
    		    delay: 250,
    		    "data": function (parametros) {
    		    	try{
    			      return {
    			    	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
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
  	      
          cmpIdRegistro = null;
          cmpHoraMuestra.inputmask("d/m/y h:s:s");  

          cmpApi60.inputmask("99.9");
          cmpTemperatura.inputmask("99.9");

          cmpModifica.on("click", function(){
      	    try{
      	    	moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);
      	   	    var fila = moduloActual.obj.GrupoMuestreo.getForm(moduloActual.indiceFormulario);  
        			fila.find("input[elemento-grupo='horaMuestra']").prop('disabled', false);
        			fila.find("select[elemento-grupo='producto']").prop('disabled', false);
        			fila.find("input[elemento-grupo='api60']").prop('disabled', false);
        			fila.find("input[elemento-grupo='temperatura']").prop('disabled', false);
      	    } catch(error){
      	       console.log(error.message);
      	    }
      	  });
          
          cmpElimina.on("click", function(){
      	    try{
      	    	moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);
      	    	var fila = moduloActual.obj.GrupoMuestreo.getForm(moduloActual.indiceFormulario);
      	    	if(fila.find("input[elemento-grupo='identificador']").val() > 0){
      		    	moduloActual.registrosEliminar = fila.find("input[elemento-grupo='identificador']").val();
      		    	moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
      		    	moduloActual.obj.frmConfirmarEliminarMuestra.modal("show");
      	    	} else {
      	    		moduloActual.obj.GrupoMuestreo.removeForm(moduloActual.indiceFormulario);
      		    }
      	    } catch(error) {
      	       console.log(error.message);
      	    }
      	  }); 
          
          cmpTemperatura.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);
        	  moduloActual.calcularFactorCorreccion(cmpApi60.val(), cmpTemperatura.val(), "GrupoMuestreo", indiceFormulario);
          });
         
          cmpApi60.on("change",function(){
        	  var indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);
        	  moduloActual.calcularFactorCorreccion(cmpApi60.val(), cmpTemperatura.val(), "GrupoMuestreo", indiceFormulario);
          });
      },
    }); 
    
    

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
	  
  moduloActual.calcularFactorCorreccion= function(apiCorregido, temperatura, nombreSheepit, indiceFormulario){
	var ref=this;
	var parametros={};
	var camposInvalidos = 0;
	var factorCorreccion = 0;
	var volumenCorregido = 0;
	try {
		if ((typeof temperatura == "undefined") || (temperatura == null) || (temperatura == '')){
		  camposInvalidos++;
		}
		if ((typeof apiCorregido == "undefined") || (apiCorregido == null) || (apiCorregido == '')){
		  camposInvalidos++;
		}
		if (camposInvalidos == 0){
		  parametros.apiCorregido=apiCorregido;
		  parametros.temperatura=temperatura;
		  parametros.volumenObservado = 0;
		  console.log(parametros);
		  $.ajax({
		    type: constantes.PETICION_TIPO_GET,
		    url: '../admin/formula/recuperar-factor-correccion',
		    contentType: ref.TIPO_CONTENIDO,
		    data: parametros,
		    success: function(respuesta) {
		      if (!respuesta.estado) {
		        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No se puede calcular el factor");
		      } else {
		        var registro = respuesta.contenido.carga[0];
		        console.log(registro);
		        factorCorreccion = registro.factorCorreccion;
		        
		        console.log("nombre de Sheppit  " + nombreSheepit);
		        console.log("factorCorreccion  " + factorCorreccion);

		        if (nombreSheepit == "grupoAperturaTanques"){
		        	var formulario = moduloActual.obj.grupoAperturaTanques.getForm(indiceFormulario);
		        	var volObs = formulario.find("input[elemento-grupo='volObsInicial']").val();
		        	volumenCorregido = parseFloat(moduloActual.eliminaSeparadorComa(volObs)) * parseFloat(factorCorreccion);
		  	      	formulario.find("input[elemento-grupo='factor']").val(factorCorreccion.toFixed(6));
		  	        formulario.find("input[elemento-grupo='vol60']").val(volumenCorregido.toFixed(2));

		        } else if (nombreSheepit == "GrupoCierreProducto"){
		        	var formulario = moduloActual.obj.GrupoCierreProducto.getForm(indiceFormulario); 
		  	      	formulario.find("input[elemento-grupo='factorProducto']").val(factorCorreccion.toFixed(6));
		  	      	
		        } else if (nombreSheepit == "GrupoCierreTanques"){
		        	var formulario = moduloActual.obj.GrupoCierreTanques.getForm(indiceFormulario);
		        	var volObs = formulario.find("input[elemento-grupo='volObsFinalTanque']").val();
		        	volumenCorregido = parseFloat(moduloActual.eliminaSeparadorComa(volObs)) * parseFloat(factorCorreccion);
		  	      	formulario.find("input[elemento-grupo='factorFinalTanque']").val(factorCorreccion.toFixed(6));
		  	        formulario.find("input[elemento-grupo='vol60FinalTanque']").val(volumenCorregido.toFixed(2));

		        } else if (nombreSheepit == "GrupoMuestreo"){
		        	var formulario = moduloActual.obj.GrupoMuestreo.getForm(indiceFormulario);
		  	      	formulario.find("input[elemento-grupo='factor']").val(factorCorreccion.toFixed(6));
		  	        
 		        } else if (nombreSheepit == "GrupoCambioTanquesFinal"){
		        	var formulario = moduloActual.obj.GrupoCambioTanquesFinal.getForm(indiceFormulario);
		        	var volObs = formulario.find("input[elemento-grupo='volObsFinal']").val();
		        	volumenCorregido = parseFloat(moduloActual.eliminaSeparadorComa(volObs)) * parseFloat(factorCorreccion);
		  	      	formulario.find("input[elemento-grupo='factorFinal']").val(factorCorreccion.toFixed(6));
		  	        formulario.find("input[elemento-grupo='vol60Final']").val(volumenCorregido.toFixed(2));

		        } else if (nombreSheepit == "GrupoCambioTanquesInicial"){
		        	var formulario = moduloActual.obj.GrupoCambioTanquesInicial.getForm(indiceFormulario);
		        	var volObs = formulario.find("input[elemento-grupo='volObsInicial']").val();
		        	volumenCorregido = parseFloat(moduloActual.eliminaSeparadorComa(volObs)) * parseFloat(factorCorreccion);
		  	      	formulario.find("input[elemento-grupo='factorInicial']").val(factorCorreccion.toFixed(6));
		  	      	formulario.find("input[elemento-grupo='vol60Inicial']").val(volumenCorregido.toFixed(2));

		        }
		      }
		    },
		    error: function(xhr,estado,error) {
		      ref.mostrarErrorServidor(xhr,estado,error);
		    }
		  });
	  } 
	} catch(error){
  	  moduloActual.mostrarDepuracion(error.message);
    };	
};

	moduloActual.calcularVolumenObservadoFinal= function(medida, nombreSheepit, indiceFormulario){
		console.log("medida:" + medida + "-nombreSheepit:" + nombreSheepit + "-indiceFormulario:" + indiceFormulario);
		var ref=this;
		var parametros={};
		var camposInvalidos = 0;
		var volumenObservado = 0;
		//volumenObservado = volumenObservado.toFixed(ref.NUMERO_DECIMALES);
		try {
			if ((typeof medida == "undefined") || (medida == null) || (medida == '')){
			  camposInvalidos++;
			}
			if (camposInvalidos == 0){
				
				parametros={filtroTanque:0,filtroCentimetros:0};
				var formulario = moduloActual.obj.GrupoCierreTanques.getForm(indiceFormulario);
				//var formulario2 = moduloActual.obj.GrupoAperturaTanques.getForm(indiceFormulario);
				
				//formulario2.find("input[elemento-grupo='tanqueCierre']").val(volumenObservado);
				parametros.filtroModulo = "JORNADA";
			    parametros.filtroTanque = parseInt(formulario.find("input[elemento-grupo='idTanqueJornadaCierre']").val());
			    parametros.filtroCentimetros = parseInt(formulario.find("input[elemento-grupo='medidaFinalTanque']").val()); 
			    console.log(parametros);
			    $.ajax({
			      type: constantes.PETICION_TIPO_GET,
			      url:'./aforo-tanque/interpolacion', 
			      contentType: ref.TIPO_CONTENIDO, 
			      data: parametros, 
			      success: function(respuesta) {
			        if (!respuesta.estado) {
			        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			        } else {
			          if (respuesta.contenido.carga.length > 0 ) {
			        	  console.log("If...");
			            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
			            var registro = respuesta.contenido.carga[0];
			            volumenObservado = parseInt(registro.volumen);
			            //volumenObservado = volumenObservado.toFixed(ref.NUMERO_DECIMALES);			            
			          } else {
			        	  console.log("Else...");
			        	  volumenObservado = 0;
			            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.MENSAJE_ALTURA_NO_AFORADA);
			          }
			          
			          console.log("volumenObservado:" + volumenObservado);
			          //var formulario = moduloActual.obj.GrupoCierreTanques.getForm(indiceFormulario);
			  	        formulario.find("input[elemento-grupo='volObsFinalTanque']").val(volumenObservado);
			        }
			        //ref.obj.ocultaFormularioDescarga.hide();
			      },                  
			      error: function(xhr,estado,error) {
			        ref.mostrarErrorServidor(xhr,estado,error);
			        //ref.obj.ocultaFormularioDescarga.hide();
			      }
			    }); // fin ajax
		  }// fin IF  
		} catch(error){
	  	  moduloActual.mostrarDepuracion(error.message);
	    };	
	};

  moduloActual.botonAgregarMuestra= function(){
	  var referenciaModulo=this;
	try {
		moduloActual.obj.GrupoMuestreo.addForm();
		var contador = moduloActual.obj.GrupoMuestreo.getForms().length;
		var formulario = moduloActual.obj.GrupoMuestreo.getForm(contador-1);
		formulario.find("input[elemento-grupo='horaMuestra']").val(referenciaModulo.obj.cmpMuestreoFechaJornada.text());
    } catch(error){
  	  moduloActual.mostrarDepuracion(error.message);
    };
  };

 /* moduloActual.calcularFactor= function(idElemento){
	var ref=this;
	var temp = idElemento.split("_");
	var indice = temp[1];
	var parametros={};
	var temperatura = $("#GrupoAperturaTanques_"+indice+"_Temperatura").val();
	var apiCorregido = $("#GrupoAperturaTanques_"+indice+"_Api60").val();
	var volumenObs = $("#GrupoAperturaTanques_"+indice+"_VolObsInicial").val();
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
	  parametros.volumenObservado=0;
	  $("#ocultaContenedorAperturaJornada").show();
	  $.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: '../admin/formula/recuperar-factor-correccion',  
	    contentType: ref.TIPO_CONTENIDO, 
	    data: parametros, 
	    success: function(respuesta) {
	      if (!respuesta.estado) {
	        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	      } else {
	        var registro = respuesta.contenido.carga[0];
	        var factorCorrecion = parseFloat(registro.factorCorreccion);
	        $("#GrupoAperturaTanques_"+indice+"_Factor").val(factorCorrecion.toFixed(6));
	        if(factorCorrecion!=null && volumenObs!=null){
	        	volumenObs=volumenObs.replaceAll(moduloActual.SEPARADOR_MILES,"");
	        	var volumne60=factorCorrecion*parseFloat(volumenObs);
	        	$("#GrupoAperturaTanques_"+indice+"_Vol60").val(volumne60.toFixed(6));	
	        }
	        
	        
	        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);         
	      }
	      $("#ocultaContenedorAperturaJornada").hide();
	    },                  
	    error: function(xhr,estado,error) {
	      ref.mostrarErrorServidor(xhr,estado,error);
	      $("#ocultaContenedorAperturaJornada").hide();   
	    }
	  });
	}   
  };*/
		  
  moduloActual.datosCabecera= function(){
    var referenciaModulo=this;
	referenciaModulo.obj.idOperacionSeleccionado = referenciaModulo.obj.filtroOperacion.val();
	referenciaModulo.obj.idCliente = $(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-idCliente');
    referenciaModulo.obj.idEstacionSeleccionado = referenciaModulo.obj.filtroEstacion.val();
    referenciaModulo.obj.idJornadaSeleccionado = referenciaModulo.obj.idJornadaSeleccionado;

    // para apertura
    this.obj.cmpAperturaCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
    this.obj.cmpAperturaOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));

    // para cierre
    this.obj.cmpCierreCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
    this.obj.cmpCierreOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
  
    // para detalle
    this.obj.cmpDetalleCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
    this.obj.cmpDetalleOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
    
    // para cambio tanque
    this.obj.cmpCambioTanqueCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
    this.obj.cmpCambioTanqueOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
    
    // para cambio muestreo
    this.obj.cmpMuestreoCliente.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-cliente'));
    this.obj.cmpMuestreoOperacion.text($(referenciaModulo.obj.filtroOperacion).find("option:selected").attr('data-nombre-operacion'));
  };
	  
// =========================================================== Formulario Apertura ================================================================
  moduloActual.llenarApertura = function(registro){
	var referenciaModulo = this;
	var numeroContometros = 0;
	var numeroTanques = 0;
	if(registro.registroNuevo == false){
		referenciaModulo.obj.cmpAperturaEstacion.text(registro.estacion.nombre);
		referenciaModulo.obj.cmpAperturaFechaJornada.text(utilitario.retornarSumaRestaFechas(1, utilitario.formatearFecha(registro.fechaOperativa)));
		referenciaModulo.obj.cmpObservacionApertura.val("");
		if (registro.contometroJornada != null){
	    	numeroContometros = registro.contometroJornada.length;
	    }
	
	    referenciaModulo.obj.GrupoAperturaContometros.removeAllForms();
	    for(var contador = 0; contador < numeroContometros; contador++){
	      referenciaModulo.obj.GrupoAperturaContometros.addForm();
	      var formulario= referenciaModulo.obj.GrupoAperturaContometros.getForm(contador);

	      formulario.find("input[elemento-grupo='idContometros']").val(registro.contometroJornada[contador].contometro.id);
	      formulario.find("input[elemento-grupo='contometros']").val(registro.contometroJornada[contador].contometro.alias);

	      var productoContometro=constantes.PLANTILLA_OPCION_SELECTBOX;
	      productoContometro = productoContometro.replace(constantes.ID_OPCION_CONTENEDOR,registro.contometroJornada[contador].producto.id);
	      productoContometro = productoContometro.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.contometroJornada[contador].producto.nombre);
	      formulario.find("select[elemento-grupo='productosContometros']").empty().append(productoContometro).val(registro.contometroJornada[contador].producto.id).trigger('change');
	      
	      // formulario.find("input[elemento-grupo='productosContometros']").val(registro.contometroJornada[contador].producto.nombre);
	      formulario.find("input[elemento-grupo='lecturaInicial']").val(registro.contometroJornada[contador].lecturaFinal);
	      console.log(formulario);
	    }
	    
	    if (registro.tanqueJornada != null){
	    	numeroTanques = registro.tanqueJornada.length;
	    }
	    referenciaModulo.obj.grupoAperturaTanques.removeAllForms();
	    for(var cont = 0; cont < numeroTanques; cont++){
	      referenciaModulo.obj.grupoAperturaTanques.addForm();
	      var formularioTanque= referenciaModulo.obj.grupoAperturaTanques.getForm(cont);
	      
	      formularioTanque.find("input[elemento-grupo='idTanques']").val(registro.tanqueJornada[cont].tanque.id);
	      formularioTanque.find("input[elemento-grupo='tanques']").val(registro.tanqueJornada[cont].tanque.descripcion);
	      
	      
	      formularioTanque.find("input[elemento-grupo='idProductosTanques']").val(registro.tanqueJornada[cont].producto.id);
	      formularioTanque.find("input[elemento-grupo='productosTanques']").val(registro.tanqueJornada[cont].producto.nombre);
	      
	      
	      /*var productoTanque=constantes.PLANTILLA_OPCION_SELECTBOX;
	      productoTanque = productoTanque.replace(constantes.ID_OPCION_CONTENEDOR,registro.tanqueJornada[cont].producto.id);
	      productoTanque = productoTanque.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.tanqueJornada[cont].producto.nombre);
	      formularioTanque.find("select[elemento-grupo='productosTanques']").empty().append(productoTanque).val(registro.tanqueJornada[cont].producto.id).trigger('change');*/
	      
	      // formularioTanque.find("select[elemento-grupo='productosTanques']").val(registro.tanqueJornada[cont].producto.nombre);
	      formularioTanque.find("input[elemento-grupo='medidaInicial']").val(registro.tanqueJornada[cont].medidaFinal);
	      formularioTanque.find("input[elemento-grupo='volObsInicial']").val(registro.tanqueJornada[cont].volumenObservadoFinal.toFixed(2));
	      formularioTanque.find("input[elemento-grupo='api60']").val(registro.tanqueJornada[cont].apiCorregidoFinal.toFixed(2));
	      formularioTanque.find("input[elemento-grupo='temperatura']").val(registro.tanqueJornada[cont].temperaturaFinal.toFixed(2));
	      formularioTanque.find("input[elemento-grupo='factor']").val(registro.tanqueJornada[cont].factorCorreccionFinal.toFixed(6));
	      formularioTanque.find("input[elemento-grupo='vol60']").val(registro.tanqueJornada[cont].volumenCorregidoFinal.toFixed(2));
	      if(registro.tanqueJornada[cont].estadoServicio == 1) { formularioTanque.find("input[elemento-grupo='fs']").prop('checked', true);  }
	      												  else { formularioTanque.find("input[elemento-grupo='fs']").prop('checked', false); }
	      
	      if(registro.tanqueJornada[cont].enLinea == 1) 	   { formularioTanque.find("input[elemento-grupo='desp']").prop('checked', true);  }
	      												  else { formularioTanque.find("input[elemento-grupo='desp']").prop('checked', false); }
	    }
	} else {
		referenciaModulo.obj.cmpAperturaEstacion.text(registro.estacion.nombre);
		referenciaModulo.obj.cmpAperturaFechaJornada.text(utilitario.formatearFecha(registro.fechaOperativa));
		if (registro.contometro != null){
	    	numeroContometros = registro.contometro.length;
	    }

	    referenciaModulo.obj.GrupoAperturaContometros.removeAllForms();
	    for(var contador = 0; contador < numeroContometros; contador++){
	      referenciaModulo.obj.GrupoAperturaContometros.addForm();
	      var formulario= referenciaModulo.obj.GrupoAperturaContometros.getForm(contador);
	      formulario.find("input[elemento-grupo='idContometros']").val(registro.contometro[contador].id);
	      formulario.find("input[elemento-grupo='contometros']").val(registro.contometro[contador].alias);
	      formulario.find("input[elemento-grupo='lecturaInicial']").val("");

	      formulario.find("select[elemento-grupo='productosContometros']").prop('disabled', false);
	      formulario.find("input[elemento-grupo='lecturaInicial']").prop('disabled', false);

	      console.log(formulario);
	    }

	    if (registro.tanque != null){
	    	numeroTanques = registro.tanque.length;
	    }
	    referenciaModulo.obj.grupoAperturaTanques.removeAllForms();
	    for(var cont = 0; cont < numeroTanques; cont++){
	      referenciaModulo.obj.grupoAperturaTanques.addForm();
	      var formularioTanque= referenciaModulo.obj.grupoAperturaTanques.getForm(cont);
	      formularioTanque.find("input[elemento-grupo='idTanques']").val(registro.tanque[cont].id);
	      formularioTanque.find("input[elemento-grupo='tanques']").val(registro.tanque[cont].descripcion);
	      
	      formularioTanque.find("input[elemento-grupo='idProductosTanques']").val(registro.tanque[cont].producto.id);
	      formularioTanque.find("input[elemento-grupo='productosTanques']").val(registro.tanque[cont].producto.nombre);
	      
	      formularioTanque.find("input[elemento-grupo='medidaInicial']").val("");
	      formularioTanque.find("input[elemento-grupo='volObsInicial']").val("");
	      formularioTanque.find("input[elemento-grupo='api60']").val("");
	      formularioTanque.find("input[elemento-grupo='temperatura']").val("");
	      formularioTanque.find("input[elemento-grupo='factor']").val("");
	      formularioTanque.find("input[elemento-grupo='vol60']").val("");
	      formularioTanque.find("input[elemento-grupo='fs']").prop('checked', false);
	      formularioTanque.find("input[elemento-grupo='desp']").prop('checked', false);
	      
	      //formularioTanque.find("select[elemento-grupo='productosTanques']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='medidaInicial']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='volObsInicial']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='api60']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='temperatura']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='factor']").prop('disabled', false);
	      formularioTanque.find("input[elemento-grupo='vol60']").prop('disabled', false);
	    }
	}
  };
  
  moduloActual.recuperarValoresApertura = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
    	console.log(referenciaModulo.obj.cmpAperturaFechaJornada.text());
	    // datos para la jornada
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.idEstacion = parseInt(referenciaModulo.obj.filtroEstacion.val());
	    eRegistro.estado = parseInt(constantes.TIPO_JORNADA_ABIERTO);
	    eRegistro.idOperario1 = parseInt(referenciaModulo.obj.cmpOperador1.val());
	    eRegistro.idOperario2 = parseInt(referenciaModulo.obj.cmpOperador2.val());
	    eRegistro.fechaOperativa =  utilitario.formatearStringToDate(referenciaModulo.obj.cmpAperturaFechaJornada.text());
	    eRegistro.observacion = referenciaModulo.obj.cmpObservacionApertura.val().toUpperCase();
	    
	    eRegistro.contometroJornada = [];
	    // datos para los contometros de la jornada
	    var numeroContometros = referenciaModulo.obj.GrupoAperturaContometros.getForms().length;
	      for(var contador = 0; contador < numeroContometros; contador++){
	        var contometroJornada = {};
	        var formularioContometro = referenciaModulo.obj.GrupoAperturaContometros.getForm(contador);
	        
	        var cmpIdContometros		= formularioContometro.find("input[elemento-grupo='idContometros']");
	        var cmpContometros			= formularioContometro.find("input[elemento-grupo='contometros']");
	        var cmpProductoContometro	= formularioContometro.find("select[elemento-grupo='productosContometros']");
	        var cmpLecturaInicial		= formularioContometro.find("input[elemento-grupo='lecturaInicial']");
	        
	        console.log(cmpLecturaInicial.val());
	        
	        contometroJornada.lecturaInicial = parseFloat(cmpLecturaInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
	        contometroJornada.idContometro = parseInt(cmpIdContometros.val());
	        contometroJornada.idProducto = parseInt(cmpProductoContometro.val());
	        eRegistro.contometroJornada.push(contometroJornada);
	      }
	      
	      eRegistro.tanqueJornada = [];
	      // datos para los contometros de la jornada
	      var numeroTanques = referenciaModulo.obj.grupoAperturaTanques.getForms().length;
	      console.log("numeroTanques " + numeroTanques);
	      for(var contador = 0; contador < numeroTanques; contador++){
	        var tanqueJornada = {};
	        var formularioTanque = referenciaModulo.obj.grupoAperturaTanques.getForm(contador);

	        var cmpIdTanques=$(formularioTanque).find("input[elemento-grupo='idTanques']");
            var cmpTanques=$(formularioTanque).find("input[elemento-grupo='tanques']");
            var cmpIdProductosTanques=$(formularioTanque).find("input[elemento-grupo='idProductosTanques']");
            var cmpProductosTanques=$(formularioTanque).find("input[elemento-grupo='productosTanques']");
            var cmpMedidaInicial=$(formularioTanque).find("input[elemento-grupo='medidaInicial']");
            var cmpVolObsInicial=$(formularioTanque).find("input[elemento-grupo='volObsInicial']");
            var cmpApi60=$(formularioTanque).find("input[elemento-grupo='api60']");
            var cmpTemperatura=$(formularioTanque).find("input[elemento-grupo='temperatura']");
            var cmpFactor=$(formularioTanque).find("input[elemento-grupo='factor']");
            var cmpVol60=$(formularioTanque).find("input[elemento-grupo='vol60']");
            var cmpFs=$(formularioTanque).find("input[elemento-grupo='fs']");
            var cmpDesp=$(formularioTanque).find("input[elemento-grupo='desp']");

	        if(cmpFs.prop('checked') == true)	{ tanqueJornada.estadoServicio = 1; } else { contometroJornada.estadoServicio = 0; }
	        if(cmpDesp.prop('checked') == true) { tanqueJornada.enLinea = 1; }		  else { contometroJornada.enLinea = 0; }

            tanqueJornada.idTanque = parseInt(cmpIdTanques.val());
            tanqueJornada.idProducto = parseInt(cmpIdProductosTanques.val());
            tanqueJornada.medidaInicial = parseFloat(cmpMedidaInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenObservadoInicial = parseFloat(cmpVolObsInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.apiCorregidoInicial = parseFloat(cmpApi60.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.temperaturaInicial = parseFloat(cmpTemperatura.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.factorCorreccionInicial = parseFloat(cmpFactor.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenCorregidoInicial = parseFloat(cmpVol60.val().replace(moduloActual.SEPARADOR_MILES,""));

	        eRegistro.tanqueJornada.push(tanqueJornada);
	      }
	console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

  // ==============================================Formulario Cierre ============================================
  moduloActual.llenarFormularioCierre = function(registro){
	  var referenciaModulo = this;
	  console.log("==============================================Formulario Cierre ============================================");
	  console.log(registro);
	var numeroContometros = 0;
	var numeroTanques = 0;
	var numeroProductos = 0;
		referenciaModulo.obj.cmpCierreEstacion.text(registro.estacion.nombre);
		referenciaModulo.obj.cmpCierreFechaJornada.text(utilitario.formatearFecha(registro.fechaOperativa));
		referenciaModulo.obj.cmpCierreOperador1.text(registro.operario1.nombreCompletoOperario);
		referenciaModulo.obj.cmpCierreOperador2.text(registro.operario2.nombreCompletoOperario);
		referenciaModulo.obj.cmpObservacionCierre.val(registro.observacion);

		if (registro.contometroJornada != null){
	    	numeroContometros = registro.contometroJornada.length;
	    }
	    referenciaModulo.obj.GrupoCierreContometros.removeAllForms();
	    for(var contador = 0; contador < numeroContometros; contador++){
	      referenciaModulo.obj.GrupoCierreContometros.addForm();
	      var formulario= referenciaModulo.obj.GrupoCierreContometros.getForm(contador);
	      formulario.find("input[elemento-grupo='idContometroJornadaCierre']").val(registro.contometroJornada[contador].id);
	      formulario.find("input[elemento-grupo='idContometroCierre']").val(registro.contometroJornada[contador].contometro.id);
	      formulario.find("input[elemento-grupo='contometrosCierre']").val(registro.contometroJornada[contador].contometro.alias);
	      formulario.find("input[elemento-grupo='idCierreProductoContometro']").val(registro.contometroJornada[contador].producto.id);
	      console.log("idCierreProductoContometro " + registro.contometroJornada[contador].producto.id);
	      formulario.find("input[elemento-grupo='cierreProductoContometro']").val(registro.contometroJornada[contador].producto.nombre);
	      formulario.find("input[elemento-grupo='lecturaInicial']").val(registro.contometroJornada[contador].lecturaInicial);
	      formulario.find("input[elemento-grupo='lecturaFinal']").val(registro.contometroJornada[contador].lecturaFinal);
	      var diferencia = parseFloat(registro.contometroJornada[contador].lecturaFinal) - parseFloat(registro.contometroJornada[contador].lecturaInicial);
	      formulario.find("input[elemento-grupo='diferencia']").val(diferencia);
	      formulario.find("checkbox[elemento-grupo='servicio']").val(registro.contometroJornada[contador].estadoServicio);
	    }
	    
	    if (registro.tanqueJornadaCierre != null){
	    	numeroTanques = registro.tanqueJornadaCierre.length;
	    }
	    referenciaModulo.obj.GrupoCierreTanques.removeAllForms();
	    //referenciaModulo.obj.GrupoCierreProducto.removeAllForms();
	    for(var cont = 0; cont < numeroTanques; cont++){
	      referenciaModulo.obj.GrupoCierreTanques.addForm();
	      var formularioCierreTanque= referenciaModulo.obj.GrupoCierreTanques.getForm(cont);
	      
	      formularioCierreTanque.find("input[elemento-grupo='idTanqueJornadaCierre']").val(registro.tanqueJornadaCierre[cont].idTjornada);
	      formularioCierreTanque.find("input[elemento-grupo='idTanqueCierre']").val(registro.tanqueJornadaCierre[cont].tanque.id);
	      formularioCierreTanque.find("input[elemento-grupo='tanqueCierre']").val(registro.tanqueJornadaCierre[cont].tanque.descripcion);
	      
	      formularioCierreTanque.find("input[elemento-grupo='idCierreProductoTanque']").val(registro.tanqueJornadaCierre[cont].producto.id);
	      formularioCierreTanque.find("input[elemento-grupo='cierreProductoTanque']").val(registro.tanqueJornadaCierre[cont].producto.nombre);
	      // medidas iniciales
	      formularioCierreTanque.find("input[elemento-grupo='horaInicial']").val(utilitario.formatearTimestampToString(registro.tanqueJornadaCierre[cont].horaInicial));
	      formularioCierreTanque.find("input[elemento-grupo='medidaInicialTanque']").val(registro.tanqueJornadaCierre[cont].medidaInicial);
	      formularioCierreTanque.find("input[elemento-grupo='volObsInicialTanque']").val(registro.tanqueJornadaCierre[cont].volumenObservadoInicial.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='api60InicialTanque']").val(registro.tanqueJornadaCierre[cont].apiCorregidoInicial.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='factorInicialTanque']").val(registro.tanqueJornadaCierre[cont].factorCorreccionInicial.toFixed(6));
	      formularioCierreTanque.find("input[elemento-grupo='temperaturaInicialTanque']").val(registro.tanqueJornadaCierre[cont].temperaturaInicial.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='vol60InicialTanque']").val(registro.tanqueJornadaCierre[cont].volumenCorregidoInicial.toFixed(2));
	    	  
          if(registro.tanqueJornadaCierre[cont].estadoServicio == 1) { formularioCierreTanque.find("input[elemento-grupo='fsInicialTanque']").prop('checked', true);  }
			  													else { formularioCierreTanque.find("input[elemento-grupo='fsInicialTanque']").prop('checked', false); }

          if(registro.tanqueJornadaCierre[cont].enLinea == 1) 	   	 { formularioCierreTanque.find("input[elemento-grupo='despInicialTanque']").prop('checked', true);  }
			  													else { formularioCierreTanque.find("input[elemento-grupo='despInicialTanque']").prop('checked', false); }
          // medidas finales
          if(registro.tanqueJornadaCierre[cont].horaFinal != null){
        	  formularioCierreTanque.find("input[elemento-grupo='horaFinal']").val(utilitario.asignarHoraFinalTimestampToString(registro.tanqueJornadaCierre[cont].horaFinal));
          } else {
        	  formularioCierreTanque.find("input[elemento-grupo='horaFinal']").val(referenciaModulo.obj.cmpCierreFechaJornada.text());
          }
	      formularioCierreTanque.find("input[elemento-grupo='medidaFinalTanque']").val(registro.tanqueJornadaCierre[cont].medidaFinal);
	      formularioCierreTanque.find("input[elemento-grupo='volObsFinalTanque']").val(registro.tanqueJornadaCierre[cont].volumenObservadoFinal.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='api60FinalTanque']").val(registro.tanqueJornadaCierre[cont].apiCorregidoFinal.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='factorFinalTanque']").val(registro.tanqueJornadaCierre[cont].factorCorreccionFinal.toFixed(6));
	      formularioCierreTanque.find("input[elemento-grupo='temperaturaFinalTanque']").val(registro.tanqueJornadaCierre[cont].temperaturaFinal.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='vol60FinalTanque']").val(registro.tanqueJornadaCierre[cont].volumenCorregidoFinal.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='volAguaFinalTanque']").val(registro.tanqueJornadaCierre[cont].volumenAguaFinal.toFixed(2));
	      formularioCierreTanque.find("input[elemento-grupo='fsFinalTanque']").prop('checked', false);
	      formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', false);
	      
	      console.log(registro.tanqueJornadaCierre[cont].medidaFinal);
	       
	      if(registro.tanqueJornadaCierre[cont].horaFinal == null){
	    	  formularioCierreTanque.find("input[elemento-grupo='horaFinal']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='medidaFinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='volObsFinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='api60FinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='factorFinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='temperaturaFinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='vol60FinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='volAguaFinalTanque']").prop('disabled', false);
	    	  formularioCierreTanque.find("input[elemento-grupo='fsFinalTanque']").prop('disabled', false);
	    	  
	    	  if(registro.tanqueJornadaCierre[cont].enLinea == 1) 	   	 { formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', true);  }
																	else { formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', false);
  			  }
	    	  
	    	  //formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('disabled', true);
	      } else {
	    	  //el estado de servicio si es 0 esta activo;
	    	  //si es 1 esta inactivo
	    	  if(registro.tanqueJornadaCierre[cont].estadoServicio == 1) { formularioCierreTanque.find("input[elemento-grupo='fsFinalTanque']").prop('checked', true);  }//inactivo
																	else { formularioCierreTanque.find("input[elemento-grupo='fsFinalTanque']").prop('checked', false); }  //activo

	    	  if(registro.tanqueJornadaCierre[cont].enLinea == 1) 	   	 { formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', true);  }
																	else { formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('checked', false); }
	    	  
	    	  
	    	  formularioCierreTanque.find("input[elemento-grupo='horaFinal']").prop('disabled', true);
	    	  /*formularioCierreTanque.find("input[elemento-grupo='medidaFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='volObsFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='api60FinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='factorFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='temperaturaFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='vol60FinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='volAguaFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='fsFinalTanque']").prop('disabled', true);
	    	  formularioCierreTanque.find("input[elemento-grupo='despFinalTanque']").prop('disabled', true);*/
	      }

	    }
	    
	    if (registro.producto != null){
	    	numeroProductos = registro.producto.length;
	    }
	    
	    console.log("numeros de productos " + numeroProductos);
	    referenciaModulo.obj.GrupoCierreProducto.removeAllForms();

	    for(var indice = 0; indice < numeroProductos; indice++){

	      referenciaModulo.obj.GrupoCierreProducto.addForm();
		  var formularioMuestreo= referenciaModulo.obj.GrupoCierreProducto.getForm(indice);
		  formularioMuestreo.find("input[elemento-grupo='idProducto']").val(registro.producto[indice].id);
		  formularioMuestreo.find("input[elemento-grupo='producto']").val(registro.producto[indice].nombre);
	    }

  };
  
  
  moduloActual.recuperarValoresCierre = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
    	console.log("cmpCierreFechaJornada " + referenciaModulo.obj.cmpCierreFechaJornada.text());
	    // datos para la jornada
	    eRegistro.id = parseInt(referenciaModulo.obj.idJornada);
	    eRegistro.estado = parseInt(constantes.TIPO_JORNADA_CERRADO);
	    eRegistro.fechaOperativa = utilitario.formatearStringToDate(referenciaModulo.obj.cmpCierreFechaJornada.text());
	    eRegistro.observacion = referenciaModulo.obj.cmpObservacionCierre.val().toUpperCase();

	    eRegistro.contometroJornada = [];
	    // datos para los contometros de la jornada
	    var numeroContometros = referenciaModulo.obj.GrupoCierreContometros.getForms().length;
	    console.log("numeroContometros " + numeroContometros);
	      for(var contador = 0; contador < numeroContometros; contador++){
	        var contometroJornada = {};
	        var formularioContometro = referenciaModulo.obj.GrupoCierreContometros.getForm(contador);
	        var cmpIdContometroJornadaCierre	= formularioContometro.find("input[elemento-grupo='idContometroJornadaCierre']");
	        var cmpIdProductoJornadaCierre	= formularioContometro.find("input[elemento-grupo='idCierreProductoContometro']");
	        var cmpLecturaFinal					= formularioContometro.find("input[elemento-grupo='lecturaFinal']");
	        var cmpLecturaInicial					= formularioContometro.find("input[elemento-grupo='lecturaInicial']");
	        var cmpIdContometroCierre					= formularioContometro.find("input[elemento-grupo='idContometroCierre']");
	        var cmpServicio						= formularioContometro.find("input[elemento-grupo='servicio']");  
	        
	        if(cmpServicio.prop('checked') == true){
	        	contometroJornada.estadoServicio = 1;
	        } else { 
	        	contometroJornada.estadoServicio = 0;
	        }

	        contometroJornada.id = parseInt(cmpIdContometroJornadaCierre.val());
	        contometroJornada.idProducto = parseInt(cmpIdProductoJornadaCierre.val());
	        contometroJornada.idContometro = parseInt(cmpIdContometroCierre.val());
	        contometroJornada.lecturaInicial = parseFloat(cmpLecturaInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
	        contometroJornada.lecturaFinal = parseFloat(cmpLecturaFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
	        eRegistro.contometroJornada.push(contometroJornada);
	      }
	      
	      eRegistro.tanqueJornada = [];
	      // datos para los contometros de la jornada
	      var numeroTanques = referenciaModulo.obj.GrupoCierreTanques.getForms().length;
	      console.log("numeroTanques " + numeroTanques)
	      for(var contador = 0; contador < numeroTanques; contador++){
	        var tanqueJornada = {};
	        var formularioTanque = referenciaModulo.obj.GrupoCierreTanques.getForm(contador);

	        var cmpIdTanqueJornadaCierre=$(formularioTanque).find("input[elemento-grupo='idTanqueJornadaCierre']");
	        var cmpIdTanque=$(formularioTanque).find("input[elemento-grupo='idTanqueCierre']");
	        var cmpTanqueCierre=$(formularioTanque).find("input[elemento-grupo='tanqueCierre']");
	        
	        var cmpIdProducto=$(formularioTanque).find("input[elemento-grupo='idCierreProductoTanque']");
	        
	        var cmpMedidaInicialTanque=$(formularioTanque).find("input[elemento-grupo='medidaInicialTanque']");
            var cmpVolObsInicialTanque=$(formularioTanque).find("input[elemento-grupo='volObsInicialTanque']");
            var cmpApi60InicialTanque=$(formularioTanque).find("input[elemento-grupo='api60InicialTanque']");
            var cmpFactorInicialTanque=$(formularioTanque).find("input[elemento-grupo='factorInicialTanque']");
            var cmpTemperaturaInicialTanque=$(formularioTanque).find("input[elemento-grupo='temperaturaInicialTanque']");
            var cmpVol60InicialTanque=$(formularioTanque).find("input[elemento-grupo='vol60InicialTanque']");
            
            var cmpMedidaFinalTanque=$(formularioTanque).find("input[elemento-grupo='medidaFinalTanque']");
            var cmpVolObsFinalTanque=$(formularioTanque).find("input[elemento-grupo='volObsFinalTanque']");
            var cmpApi60FinalTanque=$(formularioTanque).find("input[elemento-grupo='api60FinalTanque']");
            var cmpFactorFinalTanque=$(formularioTanque).find("input[elemento-grupo='factorFinalTanque']");
            var cmpTemperaturaFinalTanque=$(formularioTanque).find("input[elemento-grupo='temperaturaFinalTanque']");
            var cmpVol60FinalTanque=$(formularioTanque).find("input[elemento-grupo='vol60FinalTanque']");
            var cmpVolAguaFinalTanque=$(formularioTanque).find("input[elemento-grupo='volAguaFinalTanque']");
            var cmpFsFinalTanque=$(formularioTanque).find("input[elemento-grupo='fsFinalTanque']");
            var cmpDespFinalTanque=$(formularioTanque).find("input[elemento-grupo='despFinalTanque']");

            tanqueJornada.idTjornada = parseInt(cmpIdTanqueJornadaCierre.val());
            tanqueJornada.idTanque = parseInt(cmpIdTanque.val());
            tanqueJornada.descripcionTanque = cmpTanqueCierre.val();
            tanqueJornada.idProducto = parseInt(cmpIdProducto.val());
           
            //------MEDIDAS INICIALES -----
            tanqueJornada.medidaInicial = parseInt(cmpMedidaInicialTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenObservadoInicial = parseFloat(cmpVolObsInicialTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.apiCorregidoInicial = parseFloat(cmpApi60InicialTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.factorCorreccionInicial = parseFloat(cmpFactorInicialTanque.val());
            tanqueJornada.temperaturaInicial = parseFloat(cmpTemperaturaInicialTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenCorregidoInicial = parseFloat(cmpVol60InicialTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            
            //-------MEDIDAS FINALES--------
            tanqueJornada.medidaFinal = parseInt(cmpMedidaFinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenObservadoFinal = parseFloat(cmpVolObsFinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.apiCorregidoFinal = parseFloat(cmpApi60FinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.factorCorreccionFinal = parseFloat(cmpFactorFinalTanque.val());
            tanqueJornada.temperaturaFinal = parseFloat(cmpTemperaturaFinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenCorregidoFinal = parseFloat(cmpVol60FinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));
            tanqueJornada.volumenAguaFinal = parseFloat(cmpVolAguaFinalTanque.val().replace(moduloActual.SEPARADOR_MILES,""));

            if(cmpFsFinalTanque.prop('checked') == true){ tanqueJornada.estadoServicio = 1; 
	        									 } else { tanqueJornada.estadoServicio = 0; }
            
            if(cmpDespFinalTanque.prop('checked') == true){ tanqueJornada.enLinea = 1; 
			 									   } else { tanqueJornada.enLinea = 0; }
            
	        eRegistro.tanqueJornada.push(tanqueJornada);
	      }
	      
	      eRegistro.muestreo = [];
	      // datos para los contometros de la jornada
	      var numeroMuestreos = referenciaModulo.obj.GrupoCierreProducto.getForms().length;
	      console.log("numeroMuestreos " + numeroMuestreos);
	      for(var i = 0; i < numeroMuestreos; i++){
	        var muestreo = {};
	        var formularioTanque = referenciaModulo.obj.GrupoCierreProducto.getForm(i);

	        var cmpIdProducto=$(formularioTanque).find("input[elemento-grupo='idProducto']");
	        var cmpProducto=$(formularioTanque).find("input[elemento-grupo='producto']");
	        var cmpApiProducto=$(formularioTanque).find("input[elemento-grupo='apiProducto']");
	        var cmpTemperaturaProducto=$(formularioTanque).find("input[elemento-grupo='temperaturaProducto']");
	        var cmpFactorProducto=$(formularioTanque).find("input[elemento-grupo='factorProducto']");
	        
	        muestreo.idJornada = parseInt(referenciaModulo.obj.idJornada);
	        muestreo.descripcionProducto= cmpProducto.val();
	        muestreo.productoMuestreado = parseInt(cmpIdProducto.val());
	        muestreo.apiMuestreo = parseFloat(cmpApiProducto.val().replace(moduloActual.SEPARADOR_MILES,""));
	        muestreo.temperaturaMuestreo = parseFloat(cmpTemperaturaProducto.val().replace(moduloActual.SEPARADOR_MILES,""));
	        muestreo.factorMuestreo = parseFloat(cmpFactorProducto.val().replace(moduloActual.SEPARADOR_MILES,""));
	        muestreo.origen = 2; // ORIGEN_CIERRE
	        eRegistro.muestreo.push(muestreo);
	      }
	console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

//============================================== Detalle de Jornada ============================================
  moduloActual.llenarDetalleJornada = function(registro){
	var referenciaModulo = this;
	var indiceContometros = 0;
	var indiceTanque 	= 0;
	var filaContometro 	= $('#lista_contometros_jornada');
    var filaTanque 	   	= $('#lista_tanque_jornada');
    var filaBitacora 	= $('#lista_bitacora');
    
	referenciaModulo.obj.cmpDetalleEstacion.text(registro.estacion.nombre);
	referenciaModulo.obj.cmpDetalleFechaJornada.text(utilitario.formatearFecha(registro.fechaOperativa));
	referenciaModulo.obj.cmpDetalleOperador1.text(registro.operario1.nombreCompletoOperario);
	referenciaModulo.obj.cmpDetalleOperador2.text(registro.operario2.nombreCompletoOperario);
	referenciaModulo.obj.cmpDetalleEstado.text(utilitario.formatearEstadoJornada(registro.estado));
	referenciaModulo.obj.cmpDetalleObservacion.text(registro.observacion);
	
	// detalle de contometros
    if (registro.contometroJornada != null){
    	indiceContometros = registro.contometroJornada.length;
    }
    $('#lista_contometros_jornada').html("");
    g_tr = '<thead><tr><th class="text-center">Cont&oacute;metro</th>' +
    				  '<th class="text-center">Producto			</th>' + 
    				  '<th class="text-center">Lect. Inicial	</th>' + 
    				  '<th class="text-center">Lect. Final		</th>' + 
    				  '<th class="text-center">Dif. Vol. Obs.	</th></tr></thead>'; 
    filaContometro.append(g_tr);
    for(var k = 0; k < indiceContometros; k++){ 	

      var diferencia = parseFloat(registro.contometroJornada[k].lecturaFinal) - parseFloat(registro.contometroJornada[k].lecturaInicial);
      g_tr  = '<tr><td class="text-left">'+registro.contometroJornada[k].contometro.alias  	+ '</td>' + // contometro
    		'    <td class="text-left">'  +registro.contometroJornada[k].producto.nombre   	+ '</td>' + // producto
    		'    <td class="text-right">' +registro.contometroJornada[k].lecturaInicial  	+ '</td>' + // lectura inicial
    		'    <td class="text-right">' +registro.contometroJornada[k].lecturaFinal 		+ '</td>' + // lectura final
    		'    <td class="text-right">' +diferencia										+ '</td></tr>'; // diferencia

      filaContometro.append(g_tr);
    }

    // detalle de tanques
    if (registro.tanqueJornada != null){
    	indiceTanque = registro.tanqueJornada.length;
    }
    $('#lista_tanque_jornada').html("");
    console.log(registro.tanqueJornada.length);
    if(indiceTanque == 0){
    	g_tr  = '<tr><td class="text-left">'  + "No se encontraron Tanques" + '</td></tr>'; 
    	filaTanque.append(g_tr);
    }

    for(var r = 0; r < indiceTanque; r++){ 
	  var textoEstado = "";
      if(registro.tanqueJornada[r].estadoServicio == 1){
  	    textoEstado = '<input type="checkbox" disabled checked  value="1"/>';
      } else {
      	textoEstado='<input type="checkbox" disabled value="0"/>';
      }
      
      var textoEnLinea = "";
      if(registro.tanqueJornada[r].enLinea == 1){
    	 textoEnLinea = '<input type="checkbox" disabled checked  value="1"/>';
      } else {
    	 textoEnLinea='<input type="checkbox" disabled value="0"/>';
      }
    	
      g_tr  = '<tr><th class="text-left" style="background-color: #FFC05A; color: #fbf9f6;"> Tanque: </th>      <td class="text-left" colspan="2">' +registro.tanqueJornada[r].tanque.descripcion 	+ '</td>' + // tanque
 			  '    <th class="text-left" style="background-color: #FFC05A; color: #fbf9f6;"> Producto: </th>	<td class="text-left" colspan="6">' +registro.tanqueJornada[r].producto.nombre 		+ '</td></tr>'; // producto
 	  filaTanque.append(g_tr);

      g_tr = '<tr><td> </td>	' +
      				  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Hora	 		</th>	' +
      				  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Medida (mm) 	</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Vol. Obs. 	</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> API 60F 		</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Factor		</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Temperatura 	</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Volumen 60F 	</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Vol. Agua	</th>	' +
					  '<th class="text-center" style="background-color: #FFC05A; color: #fbf9f6;"> Desp	 		</th></tr>';
      filaTanque.append(g_tr);

      g_tr  = '<tr><th class="text-left" style="font-weight:bold;"> Inicial: </th>' + // etiqueta
     		  '    <td class="text-right">' +utilitario.formatearTimestampToString(registro.tanqueJornada[r].horaInicial) + '</td>' + // horaInicial
		 	  '    <td class="text-right">' +registro.tanqueJornada[r].medidaInicial  						+ '</td>' + // medidaInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].volumenObservadoInicial.toFixed(2)   + '</td>' + // volumenObservadoInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].apiCorregidoInicial.toFixed(2) 		+ '</td>' + // apiCorregidoInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].factorCorreccionInicial.toFixed(6)  	+ '</td>' + // factorCorreccionInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].temperaturaInicial.toFixed(2)  		+ '</td>' + // temperaturaInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].volumenCorregidoInicial.toFixed(2)  	+ '</td>' + // volumenCorregidoInicial
			  '    <td class="text-right">' +					" "							 				+ '</td>' +
      		  '    <td class="text-right">' +					" "							 				+ '</td></tr>';
	  filaTanque.append(g_tr);
      
      g_tr  = '<tr><th class="text-left" style="font-weight:bold;"> Final: </th>' + // etiqueta
      		  '    <td class="text-right">' +utilitario.formatearTimestampToString(registro.tanqueJornada[r].horaFinal)	+ '</td>' + // horaFinal
		 	  '    <td class="text-right">' +registro.tanqueJornada[r].medidaFinal  						+ '</td>' + // medidaInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].volumenObservadoFinal.toFixed(2)  	+ '</td>' + // volumenObservadoInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].apiCorregidoFinal.toFixed(2)  		+ '</td>' + // apiCorregidoInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].factorCorreccionFinal.toFixed(6)  	+ '</td>' + // factorCorreccionInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].temperaturaFinal.toFixed(2)  		+ '</td>' + // temperaturaInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].volumenCorregidoFinal.toFixed(2)  	+ '</td>' + // volumenCorregidoInicial
			  '    <td class="text-right">' +registro.tanqueJornada[r].volumenAguaFinal.toFixed(2)  		+ '</td>' +// f/s
      		  '    <td class="text-right">' +textoEnLinea  		+ 											  '</td></tr>';// f/s
	  filaTanque.append(g_tr);
	  
	  g_tr  = '<tr> <td colspan ="10"> </td> </tr>';
	  filaTanque.append(g_tr);
    }
    
    // detalle de bitacora
    $('#lista_bitacora').html("");
    g_tr  = '<tr><td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> Creado Por: 			</td>   <td class="text-left">' +registro.usuarioCreacion 	+ '</td>' + 
    		'    <td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> Creado el: 			</td>	<td class="text-left">' +registro.fechaCreacion		+ '</td>'  + 
      		'    <td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> IP Creaci&oacute;n: 	</td>	<td class="text-left">' +registro.ipCreacion 		+ '</td></tr>'; 
    filaBitacora.append(g_tr);
    g_tr  = '<tr><td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> Actualizado Por: 		  </td> <td class="text-left">' +registro.usuarioActualizacion	+ '</td>' + 
    		'    <td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> Actualizado el: 		  </td>	<td class="text-left">' +registro.fechaActualizacion 		+ '</td>'  + 
    		'    <td class="text-left" style="background-color: #FFC05A; color: #fbf9f6; font-weight:bold;"> IP Actualizaci&oacute;n: </td>	<td class="text-left">' +registro.ipActualizacion 		+ '</td></tr>'; 
    filaBitacora.append(g_tr);
  };
  
// ==============================================Formulario Cambio Tanque  ============================================
  moduloActual.llenarFormularioCambioTanque = function(registro){
	var referenciaModulo = this;
	console.log("=================================== Formulario Cambio de Tanque =======================================");
//    console.log(registro);
//	var numeroTanques = 0;
    referenciaModulo.obj.cmpCambioTanqueEstacion.text(registro.estacion.nombre);
    referenciaModulo.obj.cmpCambioTanqueFechaJornada.text(utilitario.formatearFecha(registro.fechaOperativa));
    referenciaModulo.obj.cmpCambioTanqueOperador1.text(registro.operario1.nombreCompletoOperario);
    referenciaModulo.obj.cmpCambioTanqueOperador2.text(registro.operario2.nombreCompletoOperario);
    //moduloActual.obj.cmpCambioTanqueEstado = registro.estado;
  };
  
  moduloActual.recuperarValoresCambioTanque = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    console.log("entra en recuperarValoresCambioTanqueFinal");
    eRegistro.idJornada = parseInt(referenciaModulo.obj.idJornada);
    eRegistro.tanqueJornadaFinal = [];
	eRegistro.tanqueJornadaInicial = [];
    try {
		var numeroTanqueFinales = referenciaModulo.obj.GrupoCambioTanquesFinal.getForms().length;
		for(var i = 0; i < numeroTanqueFinales; i++){
		  var tanqueJornadaFinal = {};
		  var formulario 		= referenciaModulo.obj.GrupoCambioTanquesFinal.getForm(i);
		
		  //tanque a cerrar
		  var cmpIdTanqueJornadaFinal 		= formulario.find("select[elemento-grupo='idTanqueJornadaFinal']");
          var cmpIdTanqueFinal				= formulario.find("input[elemento-grupo='idTanqueFinal']");
          var cmpDescripcionTanqueFinal		= formulario.find("input[elemento-grupo='descripcionTanqueFinal']");
          var cmpIdProductoFinal 			= formulario.find("input[elemento-grupo='idProductoFinal']");
          var cmpDescripcionProductoFinal 	= formulario.find("input[elemento-grupo='descripcionProductoFinal']");
          //medidas del tanque a cerrar
          
          var cmpHoraFinal 					= formulario.find("input[elemento-grupo='horaFinal']");
          var cmpMedidaFinal 				= formulario.find("input[elemento-grupo='medidaFinal']");
          var cmpVolObsFinal 				= formulario.find("input[elemento-grupo='volObsFinal']");
          var cmpApi60Final 				= formulario.find("input[elemento-grupo='api60Final']");
          var cmpTemperaturaFinal 			= formulario.find("input[elemento-grupo='temperaturaFinal']");
          var cmpFactorFinal 				= formulario.find("input[elemento-grupo='factorFinal']");
          var cmpVol60Final 				= formulario.find("input[elemento-grupo='vol60Final']");
          var cmpVolAguaFinal 				= formulario.find("input[elemento-grupo='volAguaFinal']");
      //    var cmpFsFinal = formulario.find("input[elemento-grupo='fsFinal']");
      //    var cmpDespFinal = formulario.find("input[elemento-grupo='despFinal']");
          

          tanqueJornadaFinal.idTjornada 	= parseInt(cmpIdTanqueJornadaFinal.val());
          tanqueJornadaFinal.idTanque 		= parseInt(cmpIdTanqueFinal.val());
          tanqueJornadaFinal.descripcionTanque	= cmpDescripcionTanqueFinal.val();
          tanqueJornadaFinal.idJornada 		= parseInt(referenciaModulo.obj.idJornada);
          tanqueJornadaFinal.idProducto 	= parseInt(cmpIdProductoFinal.val());

          
  		console.log("cmpDescripcionTanqueFinal " + cmpDescripcionTanqueFinal.val());
  		console.log("cmpHoraFinal " + cmpHoraFinal.val());
  		
          tanqueJornadaFinal.horaFinal=utilitario.formatearStringToDateHour(cmpHoraFinal.val());
          tanqueJornadaFinal.medidaFinal = parseFloat(cmpMedidaFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.volumenObservadoFinal = parseFloat(cmpVolObsFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.apiCorregidoFinal = parseFloat(cmpApi60Final.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.temperaturaFinal = parseFloat(cmpTemperaturaFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.factorCorreccionFinal = parseFloat(cmpFactorFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.volumenCorregidoFinal = parseFloat(cmpVol60Final.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaFinal.volumenAguaFinal = parseFloat(cmpVolAguaFinal.val().replace(moduloActual.SEPARADOR_MILES,""));
          
          eRegistro.tanqueJornadaFinal.push(tanqueJornadaFinal);
		}
		
		var numeroTanqueIniciales = referenciaModulo.obj.GrupoCambioTanquesInicial.getForms().length;
		for(var k = 0; k < numeroTanqueIniciales; k++){
		  var tanqueJornadaInicial = {};
		  var formulario 		= referenciaModulo.obj.GrupoCambioTanquesInicial.getForm(k);
		
		  //tanque a cerrar
          var cmpIdTanqueJornadaInicial 	= formulario.find("select[elemento-grupo='idTanqueJornadaInicial']");
          var cmpIdTanqueInicial			= formulario.find("input[elemento-grupo='idTanqueInicial']");
          var cmpDescripcionTanqueInicial	= formulario.find("input[elemento-grupo='descripcionTanqueInicial']");
          var cmpIdProductoInicial 			= formulario.find("input[elemento-grupo='idProductoInicial']");
          var cmpDescripcionProductoInicial = formulario.find("input[elemento-grupo='descripcionProductoInicial']");
          //medidas del tanque a cerrar
          var cmpHoraInicial 				= formulario.find("input[elemento-grupo='horaInicial']");
          var cmpMedidaInicial 				= formulario.find("input[elemento-grupo='medidaInicial']");
          var cmpVolObsInicial 				= formulario.find("input[elemento-grupo='volObsInicial']");
          var cmpApi60Inicial 				= formulario.find("input[elemento-grupo='api60Inicial']");
          var cmpTemperaturaInicial 		= formulario.find("input[elemento-grupo='temperaturaInicial']");
          var cmpFactorInicial 				= formulario.find("input[elemento-grupo='factorInicial']");
          var cmpVol60Inicial 				= formulario.find("input[elemento-grupo='vol60Inicial']");
      //    var cmpFsInicial = formulario.find("input[elemento-grupo='fsInicial']");
      //    var cmpDespInicial = formulario.find("input[elemento-grupo='despInicial']");
          
          tanqueJornadaInicial.idTjornada 			= parseInt(cmpIdTanqueJornadaInicial.val());
          tanqueJornadaInicial.idTanque 			= parseInt(cmpIdTanqueInicial.val());
          tanqueJornadaInicial.descripcionTanque 	= cmpDescripcionTanqueInicial.val();
          tanqueJornadaInicial.idJornada			= parseInt(referenciaModulo.obj.idJornada);
          tanqueJornadaInicial.idProducto			= parseInt(cmpIdProductoInicial.val());

		console.log("cmpDescripcionTanqueInicial " + cmpDescripcionTanqueInicial.val());
		console.log("cmpHoraInicial " + cmpHoraInicial.val());
		
		  tanqueJornadaInicial.horaInicial   = utilitario.formatearStringToDateHour(cmpHoraInicial.val());
            
          tanqueJornadaInicial.medidaInicial = parseFloat(cmpMedidaInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaInicial.volumenObservadoInicial = parseFloat(cmpVolObsInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaInicial.apiCorregidoInicial = parseFloat(cmpApi60Inicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaInicial.temperaturaInicial = parseFloat(cmpTemperaturaInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaInicial.factorCorreccionInicial = parseFloat(cmpFactorInicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          tanqueJornadaInicial.volumenCorregidoInicial = parseFloat(cmpVol60Inicial.val().replace(moduloActual.SEPARADOR_MILES,""));
          eRegistro.tanqueJornadaInicial.push(tanqueJornadaInicial);
		}
		console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    } 
    return eRegistro;
  };
  
  // ==============================================Formulario Muestreo ============================================
  moduloActual.llenarFormularioMuestreo = function(registro){
    var referenciaModulo = this;
    var numeroMuestreo = 0;
    console.log("==============================================Formulario Muestreo ============================================");
    
	referenciaModulo.obj.cmpMuestreoEstacion.text(registro.estacion.nombre);
	referenciaModulo.obj.cmpMuestreoFechaJornada.text(utilitario.formatearFecha(registro.fechaOperativa));
	referenciaModulo.obj.cmpMuestreoOperador1.text(registro.operario1.nombreCompletoOperario);
	referenciaModulo.obj.cmpMuestreoOperador2.text(registro.operario2.nombreCompletoOperario);
	moduloActual.obj.cmpMuestreoEstado = registro.estado;
	
	if (registro.muestreo != null){
    	numeroMuestreo = registro.muestreo.length;
    }
    referenciaModulo.obj.GrupoMuestreo.removeAllForms();
    for(var indice = 0; indice < numeroMuestreo; indice++){
      referenciaModulo.obj.GrupoMuestreo.addForm();
	  var formulario= referenciaModulo.obj.GrupoMuestreo.getForm(indice);
	  formulario.find("input[elemento-grupo='identificador']").val(registro.muestreo[indice].id);
      formulario.find("input[elemento-grupo='horaMuestra']").val(utilitario.formatearTimestampToString(registro.muestreo[indice].horaMuestreo));
      
      var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.muestreo[indice].producto.id);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.muestreo[indice].producto.nombre);
      formulario.find("select[elemento-grupo='producto']").empty().append(elemento1).val(registro.muestreo[indice].producto.id).trigger('change');

      formulario.find("input[elemento-grupo='api60']").val(registro.muestreo[indice].apiMuestreo.toFixed(2));
      formulario.find("input[elemento-grupo='temperatura']").val(registro.muestreo[indice].temperaturaMuestreo.toFixed(2));
      formulario.find("input[elemento-grupo='factor']").val(registro.muestreo[indice].factorMuestreo.toFixed(6));
      
      formulario.find("input[elemento-grupo='horaMuestra']").prop('disabled', true);
      formulario.find("select[elemento-grupo='producto']").prop('disabled', true);
      formulario.find("input[elemento-grupo='api60']").prop('disabled', true);
      formulario.find("input[elemento-grupo='temperatura']").prop('disabled', true);
      formulario.find("input[elemento-grupo='factor']").prop('disabled', true);
    }
  };

  moduloActual.recuperarValoresMuestreo = function(registro){
    var eRegistro = {};
    console.log("entra en recuperarValoresMuestreo");
    var referenciaModulo=this;
    try {
	  // datos para la jornada
	  eRegistro.id = parseInt(referenciaModulo.obj.idJornada);
	  eRegistro.estado = parseInt(moduloActual.obj.cmpMuestreoEstado);
	  eRegistro.fechaOperativa = utilitario.formatearStringToDate(referenciaModulo.obj.cmpMuestreoFechaJornada.text());
      eRegistro.contometroJornada = [];
	  eRegistro.tanqueJornada = [];
	  eRegistro.muestreo = [];
      // datos para los contometros de la jornada
      var numeroMuestreos = referenciaModulo.obj.GrupoMuestreo.getForms().length;
      for(var i = 0; i < numeroMuestreos; i++){
        var muestreo = {};
        var formulario 		= referenciaModulo.obj.GrupoMuestreo.getForm(i);
        
        console.log(formulario.find("input[elemento-grupo='horaMuestra']").val());

        var id 				= formulario.find("input[elemento-grupo='identificador']");
        var cmpHoraMuestra	= formulario.find("input[elemento-grupo='horaMuestra']");
		var cmpIdProducto	= formulario.find("select[elemento-grupo='producto']");
		var cmpApi			= formulario.find("input[elemento-grupo='api60']");
		var cmpTemperatura	= formulario.find("input[elemento-grupo='temperatura']");
		var cmpFactor		= formulario.find("input[elemento-grupo='factor']");

		muestreo.id = parseInt(id.val());
		muestreo.idJornada = parseInt(referenciaModulo.obj.idJornada);
		
		console.log("cmpHoraMuestra " + cmpHoraMuestra.val());
		console.log("cmpIdProducto " + cmpIdProducto.val());
		console.log("cmpApi " + cmpApi.val());
		
		

        muestreo.productoMuestreado = parseInt(cmpIdProducto.val());
        muestreo.apiMuestreo = parseFloat(cmpApi.val().replace(moduloActual.SEPARADOR_MILES,""));
        muestreo.temperaturaMuestreo = parseFloat(cmpTemperatura.val().replace(moduloActual.SEPARADOR_MILES,""));
        muestreo.factorMuestreo = parseFloat(cmpFactor.val().replace(moduloActual.SEPARADOR_MILES,""));
        muestreo.horaMuestreo=utilitario.formatearStringToDateHour(cmpHoraMuestra.val());
        //muestreo.horaMuestreoDate = utilitario.formatearStringToDateHour(cmpHoraMuestra.val());
        muestreo.origen = 1; // ORIGEN_MUESTREO
        eRegistro.muestreo.push(muestreo);
      }
      console.log(eRegistro);
	}  catch(error){
	  console.log(error.message);
	}
	return eRegistro;
  };

moduloActual.resetearFormularioApertura= function(){
  console.log("entra resetearFormularioApertura");
  var referenciaModulo= this;
  referenciaModulo.obj.frmApertura[0].reset();

  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

  moduloActual.obj.cmpOperador1.select2("val", null);
  moduloActual.obj.cmpOperador2.select2("val", null);
  moduloActual.obj.cmpOperador1.empty().append(elemento1).val(0).trigger('change');
  moduloActual.obj.cmpOperador2.empty().append(elemento1).val(0).trigger('change');
};

moduloActual.resetearFormularioCierre= function(){
  console.log("entra resetearFormularioApertura");
  var referenciaModulo= this;
  referenciaModulo.obj.frmCierre[0].reset();

  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

  moduloActual.obj.cmpOperador1.select2("val", null);
  moduloActual.obj.cmpOperador2.select2("val", null);
  moduloActual.obj.cmpOperador1.empty().append(elemento1).val(0).trigger('change');
  moduloActual.obj.cmpOperador2.empty().append(elemento1).val(0).trigger('change');
};

moduloActual.resetearFormularioCambioTanque= function(){
  console.log("entra resetearFormularioApertura");
  var referenciaModulo= this;
  referenciaModulo.obj.frmCambioTanque[0].reset();

  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

  moduloActual.obj.cmpOperador1.select2("val", null);
  moduloActual.obj.cmpOperador2.select2("val", null);
  moduloActual.obj.cmpOperador1.empty().append(elemento1).val(0).trigger('change');
  moduloActual.obj.cmpOperador2.empty().append(elemento1).val(0).trigger('change');
};

moduloActual.resetearFormularioMuestreo= function(){
  console.log("entra resetearFormularioApertura");
  var referenciaModulo= this;
  referenciaModulo.obj.frmMuestreo[0].reset();

  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

  moduloActual.obj.cmpOperador1.select2("val", null);
  moduloActual.obj.cmpOperador2.select2("val", null);
  moduloActual.obj.cmpOperador1.empty().append(elemento1).val(0).trigger('change');
  moduloActual.obj.cmpOperador2.empty().append(elemento1).val(0).trigger('change');
};
	
  moduloActual.inicializar();
  
});

