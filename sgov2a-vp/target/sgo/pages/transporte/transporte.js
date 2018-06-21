$(document).ready(function(){
  var moduloActual = new moduloTransporte();
  moduloActual.urlBase='transporte';
  moduloActual.SEPARADOR_MILES = ",";
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR_FACTOR="../admin/formula/recuperar-factor-correccion";
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar-transporte';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_LISTAR_TRANSPORTES = moduloActual.urlBase + '/listarTransportes';
  moduloActual.URL_RECUPERAR_DETALLES_TRANSPORTE = moduloActual.urlBase + '/recuperarDetallesTransporte';
  moduloActual.URL_ACTUALIZAR_PESAJE = moduloActual.urlBase + '/actualizarPesaje';
  moduloActual.URL_LISTAR_ASIGNACION_TRANSPORTES = moduloActual.urlBase + '/listarAsignacionTransportes';
  moduloActual.URL_RECUPERAR_X_REGISTRO_Y_TIPO = './evento/recuperarXregistroYtipo';  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'fechaOperativa'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'totalCisternas'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'totalCisternasAsignados'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'fechaActualizacion'});//Target5
  moduloActual.columnasGrilla.push({ "data": 'usuarioActualizacion'});//Target6
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target7
  moduloActual.columnasGrilla.push({ "data": 'operacion.nombre'});//Target8
  moduloActual.columnasGrilla.push({ "data": 'operacion.cliente.razonSocial'});//Target9
  moduloActual.columnasGrilla.push({ "data": 'operacion.cliente.id'});//Target10
  moduloActual.columnasGrilla.push({ "data": 'idOperacion'});//Target11
  moduloActual.columnasGrilla.push({ "data": 'fechaEstimadaCarga'});//Target12
  
  //Columnas
  moduloActual.definicionColumnas.push({"targets" : 1, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 2, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnas.push({"targets" : 3, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets" : 4, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets" : 5, "searchable" : true, "orderable" : false, "visible" : true, "class": "text-center" });
  moduloActual.definicionColumnas.push({"targets" : 6, "searchable" : true, "orderable" : false, "visible" : true });
  moduloActual.definicionColumnas.push({"targets" : 7, "searchable" : true, "orderable" : false, "visible" : true, "render" : utilitario.formatearEstadoDiaOperativo});
  moduloActual.definicionColumnas.push({"targets" : 8, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 9, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 10, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 11, "searchable" : true, "orderable" : false, "visible" : false });
  moduloActual.definicionColumnas.push({"targets" : 12, "searchable" : true, "orderable" : false, "visible" : false, "class": "text-center", "render" : utilitario.formatearFecha });
//esto para el dataTable secundario
  moduloActual.ordenGrillaAsignacionTransporte=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'id'});					//Target1
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'numeroGuiaRemision'});	//Target2
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'fechaEmisionGuia'});	//Target3
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'cisternaTracto'});		//Target4
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'codigoScop'});			//Target5
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'volumenTotalObservado'});//Target6
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'volumenTotalCorregido'});//Target7
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'origen'});				//Target8
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'estado'});				//Target9
  moduloActual.columnasGrillaAsignacionTransporte.push({ "data": 'numeroOrdenCompra'});	//Target10
  //
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 1, "searchable": false, "orderable": false, "visible":false });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 2, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 3, "searchable": true, "orderable": false, "visible":true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 4, "searchable": true, "orderable": false, "visible":true });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 5, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 6, "searchable": true, "orderable": false, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 7, "searchable": true, "orderable": false, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 8, "searchable": true, "orderable": false, "visible":true, "render" : utilitario.formatearOrigenTransporte });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 9, "searchable": true, "orderable": false, "visible":true, "render" : utilitario.formatearEstadoTransporte });
  moduloActual.definicionColumnasAsignacionTransporte.push({ "targets": 10, "searchable": true, "orderable": false, "visible":false});
  //esto para la tercera dataTable
  moduloActual.ordenDetalleDiaOperativo=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  //Comentado por 9000002608======================================================================
//  moduloActual.columnasDetalleDiaOperativo.push({ "data": 'descripcionProducto'});	//Target1
  //==============================================================================================
  moduloActual.columnasDetalleDiaOperativo.push({ "data": 'volumenSolicitado'});		//Target2
  moduloActual.columnasDetalleDiaOperativo.push({ "data": 'cisternasSolicitadas'});	//Target3
  moduloActual.columnasDetalleDiaOperativo.push({ "data": 'volumenAsignado'});		//Target4
  moduloActual.columnasDetalleDiaOperativo.push({ "data": 'cisternasAsignadas'});		//Target5  
  moduloActual.definicionColumnasDetalleDiaOperativo.push({ "targets": 1, "searchable": true, "orderable": false, "visible":true });
  moduloActual.definicionColumnasDetalleDiaOperativo.push({ "targets": 2, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnasDetalleDiaOperativo.push({ "targets": 3, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnasDetalleDiaOperativo.push({ "targets": 4, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  //Comentado por 9000002608======================================================================
//  moduloActual.definicionColumnasDetalleDiaOperativo.push({ "targets": 5, "searchable": true, "orderable": false, "visible":true, "class": "text-right" });
  //===============================================================================================
  moduloActual.reglasValidacionFormulario={
    cmpNumeroGuiaRemision:	{required: true, maxlength: 20 },
    cmpNumeroOrdenCompra: 	{required: true, maxlength: 20 },
    cmpCodigoScop: 			{required: true,maxlength: 20 },
    cmpPlantaDespacho: 		{required: true },
    cmpTransportista: 		{required: true },
    cmpCisternaTracto: 		{required: true },
    cmpConductor: 			{required: true },
    cmpFemisionOE: 			{required: true },
  };

  moduloActual.mensajesValidacionFormulario={
	cmpNumeroGuiaRemision:  {required: "El campo es obligatorio", maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo." },
	cmpNumeroOrdenCompra: 	{required: "El campo es obligatorio", maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo." },
    cmpCodigoScop: 			{maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo." },

    cmpPlantaDespacho: 		{required: "El campo es obligatorio" },
    cmpTransportista: 		{required: "El campo es obligatorio" },
    cmpCisternaTracto: 		{required: "El campo es obligatorio" },

    cmpConductor: 			{required: "El campo es obligatorio" },
    cmpFemisionOE: 			{required: "El campo es obligatorio" },
  };

  moduloActual.reglasValidacionFormularioEvento={
    cmpEventoTipoEvento:	{required: true },
    cmpEventoFechaHora: 	{required: true },
    cmpEventoDescripcion: 	{required: true, maxlength: 3000 },
  };

  moduloActual.mensajesValidacionFormularioEvento={
	cmpEventoTipoEvento:  {required: "El campo es obligatorio" },
	cmpEventoFechaHora:   {required: "El campo es obligatorio" },
    cmpEventoDescripcion: {required: "El campo es obligatorio", maxlength: "El campo debe contener 3000 caracteres como m&aacute;ximo." },
  };

  moduloActual.reglasValidacionFormularioPesaje={
	cmpPesajePesoBruto:	  {required: true },
	cmpPesajePesoTara: 	  {required: true },
  };

  moduloActual.mensajesValidacionFormularioPesaje={
	cmpPesajePesoBruto:	  {required: "El campo es obligatorio" },
	cmpPesajePesoTara:    {required: "El campo es obligatorio" },
  };

  moduloActual.inicializarCampos= function(){
	this.obj.filtroOperacion = $("#filtroOperacion");
    this.obj.filtroOperacion.on('change', function(e){
      moduloActual.idOperacion=$(this).val();
      moduloActual.volumenPromedioCisterna=$(this).find("option:selected").attr('data-volumen-promedio-cisterna');
      moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
      moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');
      e.preventDefault(); 
    });   
    
    this.cmpOrigen = $("#cmpOrigen");
    this.obj.indicadorProducto = $("#indicadorProducto");
    this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
    this.cmpFechaCarga = $("#cmpFechaCarga");
    //Recupera la fecha actual enviada por el servidor
    var fechaActual = this.obj.filtroFechaPlanificada.attr('data-fecha-actual');
    console.log(" fechaActual -------> " + fechaActual);
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
    //Campos formulario Importa
	this.obj.lblNombreCliente=$("#lblNombreCliente");
    this.obj.lblNombreOperacion=$("#lblNombreOperacion");
    this.obj.lblFechaPlanificacion=$("#lblFechaPlanificacion");
    
    
    var ref = this;
    ref.obj.cmpFiltroFechaInicialImportacion=$("#cmpFiltroFechaInicialImportacion");
	ref.obj.cmpFiltroFechaInicialImportacion.daterangepicker({
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
	
	ref.obj.cmpFiltroFechaFinalImportacion=$("#cmpFiltroFechaFinalImportacion");
	ref.obj.cmpFiltroFechaFinalImportacion.daterangepicker({
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
    
	this.obj.cmpFiltroFechaInicialImportacion=$("#cmpFiltroFechaInicialImportacion");
    this.obj.cmpFiltroFechaFinalImportacion=$("#cmpFiltroFechaFinalImportacion");
    this.obj.cmpFiltroNumeroGuiaRemision=$("#cmpFiltroNumeroGuiaRemision");
    //Campos de detalle de transporte
	this.obj.detalleIdDOperativo=$("#detalleIdDOperativo");
    this.obj.detalleCliente=$("#detalleCliente");
    this.obj.detalleOperacion=$("#detalleOperacion");
    this.obj.detalleFechaPlanificacion=$("#detalleFechaPlanificacion");
    this.obj.detalleIdTransporte=$("#detalleIdTransporte");
    //Campos de Formulario Principal
    this.cmpTarjetaCubicacionCompartimento=$("#cmpTarjetaCubicacionCompartimento");
    this.cmpFechaVigenciaTarjetaCubicacion=$("#cmpFechaVigenciaTarjetaCubicacion");
    this.cmpCapVolCompartimento=$("#cmpCapVolCompartimento");
    this.obj.cmpFormularioIdDOperativo=$("#cmpFormularioIdDOperativo");
    this.obj.cmpFormularioCliente=$("#cmpFormularioCliente");
    this.obj.cmpFormularioOperacion=$("#cmpFormularioOperacion");
    this.obj.cmpFormularioFechaPlanificacion=$("#cmpFormularioFechaPlanificacion");
    this.obj.cmpId=$("#cmpId");
    this.obj.formularioIdTransporte=$("#formularioIdTransporte");
    this.obj.cmpNumeroGuiaRemision=$("#cmpNumeroGuiaRemision");
    this.obj.cmpNumeroOrdenCompra=$("#cmpNumeroOrdenCompra");
    this.obj.cmpNumeroFactura=$("#cmpNumeroFactura");
    this.obj.cmpCodigoScop=$("#cmpCodigoScop");
    this.obj.cmpPlantaDespacho=$("#cmpPlantaDespacho");
    this.obj.cmpSelect2Planta=$("#cmpPlantaDespacho").select2({
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
  				return registro.text;
  			}		    	
		        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
		    },
		    templateSelection: function (registro) {
		        return registro.descripcion || registro.text;
		    },
    });
    this.obj.cmpTransportista=$("#cmpTransportista");
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
    		        idOperacion : moduloActual.obj.filtroOperacion.val()
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
    				return registro.text;
    			}	    	
  		        return "<div class='select2-user-result'>" + (registro.razonSocial) + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		    	moduloActual.idTransportista = registro.id;
  		    	try{
	  		      var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	  		      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
	  		      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "Seleccionar");
	  		      moduloActual.obj.cmpCisternaTracto.empty().append(elemento1).val(0).trigger('change');
  		    	} catch(error){
  		          console.log(error.message);
  		    	}
  		    	moduloActual.obj.contenedorDetalles.hide();
	  		  return registro.razonSocial || registro.text;
  		    },
      });

    this.obj.cmpIdTracto=$("#cmpIdTracto");
    this.obj.cmpPlacaCisterna=$("#cmpPlacaCisterna");
    this.obj.cmpPlacaTracto=$("#cmpPlacaTracto");
    this.obj.cmpCisternaTracto=$("#cmpCisternaTracto");
    this.obj.cmpSelect2CisternaTracto=$("#cmpCisternaTracto").select2({
	  ajax: {
  		    url: "./cisterna/recuperarPorTransportista",
  		    dataType: 'json',
  		    delay: 250,
  		    "data": function (parametros) {
  		    	try{
  		    		var transportista = 0;  		    		
  		    		if(moduloActual.idTransportista > 0){
  		    			transportista = moduloActual.idTransportista;
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
  			  var resultados = respuesta.contenido.carga;
  			  return { results: resultados};
  		    },
  		    cache: true
  		  },
	  		"language": "es",
	  		"escapeMarkup": function (markup) { 
	  			return markup; 
	  		},
	  		"templateResult": function (registro) {
	  			if (registro.loading) {
	  				return registro.text;
	  			}	
		        return "<div class='select2-user-result'>" + registro.placaCisternaTracto + "</div>";
		    },
		    "templateSelection": function (registro) {
		    	try{
		    		console.log(registro);
		    	moduloActual.cmpIdTracto = registro.idTracto;
		    	moduloActual.cmpPlacaCisterna = registro.placa;
		    	moduloActual.cmpPlacaTracto = registro.placaTracto;
		    	moduloActual.cmpTarjetaCubicacionCompartimento = registro.tarjetaCubicacion;
		    	moduloActual.cmpFechaVigenciaTarjetaCubicacion = registro.fechaVigenciaTarjetaCubicacion;
		    	var retorno = moduloActual.validarFechaVigenciaTarjetaCubicacion();
		    	console.log(retorno);
		    	moduloActual.agregarFilas(registro);
		    	} catch(error){
  		          console.log(error.message);
  		        };
		        return registro.placaCisternaTracto || registro.text;
		    },
	 });
    this.obj.cmpBreveteConductor=$("#cmpBreveteConductor");
    this.obj.cmpConductor=$("#cmpConductor");
    this.obj.cmpSelect2Conductor=$("#cmpConductor").select2({
    	  ajax: {
    		    url: "./conductor/listar",
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
    				return registro.text;
    			}		
  		        return "<div class='select2-user-result'>" + registro.nombreCompleto + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		    	moduloActual.cmpBreveteConductor = registro.brevete;
  		        return  registro.nombreCompleto || registro.text;
  		    },
      });

    this.obj.cmpIdConductor=$("#cmpIdConductor");
    this.obj.cmpFemisionOE=$("#cmpFemisionOE");
    this.obj.cmpFemisionOE.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpPrecintos=$("#cmpPrecintos");
    this.obj.grupoTransporte = $('#GrupoTransporte').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        maxFormsCount: 6,
        minFormsCount: 0,
        iniFormsCount: 0,
        afterAdd: function(origen, formularioNuevo) {
          var cmpCompartimentos=$(formularioNuevo).find("input[elemento-grupo='compartimentos']");
          var cmpCapVolCompartimento=$(formularioNuevo).find("input[elemento-grupo='capVolCompartimento']");
          var cmpElementoProducto=$(formularioNuevo).find("select[elemento-grupo='producto']");
          var cmpVolumenTempObservada=$(formularioNuevo).find("input[elemento-grupo='volumenTempObservada']");
          var cmpTemperatura=$(formularioNuevo).find("input[elemento-grupo='temperatura']");
          var cmpAPI=$(formularioNuevo).find("input[elemento-grupo='API']");
          var cmpFactor=$(formularioNuevo).find("input[elemento-grupo='factor']");
          var cmpVolumen60F=$(formularioNuevo).find("input[elemento-grupo='volumen60F']");
          cmpElementoProducto.select2();

          cmpCompartimentos.inputmask('decimal', {digits: 0});
          cmpElementoProducto.select2();
          cmpVolumenTempObservada.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          //cmpVolumenTempObservada.inputmask("99,999.99");
          
          //cmpTemperatura.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          //cmpAPI.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
          //cmpFactor.inputmask('decimal', {digits: 4, groupSeparator:',',autoGroup:true,groupSize:3});
          cmpTemperatura.inputmask("99.9");
          cmpAPI.inputmask("99.9");
          //cmpFactor.inputmask("0.999999");
          
          cmpVolumenTempObservada.on("change",function(){
           //var idElemento = $(this).attr("id");
           //moduloActual.calcularFactor(idElemento);
          });
          
          cmpTemperatura.on("change",function(){
           var idElemento = $(this).attr("id");
           moduloActual.calcularFactor(idElemento);
          });
          
          cmpAPI.on("change",function(){
           var idElemento = $(this).attr("id");
           moduloActual.calcularFactor(idElemento);
          });
         
          cmpVolumen60F.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});

          cmpVolumenTempObservada.on("input",function(){
        	  moduloActual.sumaVolumenTempObservada();
          });  

          cmpVolumen60F.on("input",function(){
        	  moduloActual.sumaVolumen60F();
          });
          
          cmpElementoProducto.tipoControl="select2";
          moduloActual.obj.cmpSelect2Producto=$(formularioNuevo).find("select[elemento-grupo='producto']").select2({
      	  ajax: {
      		    url: "./producto/listarPorOperacion",
      		    //url: "./producto/listar",
      		    dataType: 'json',
      		    delay: 250,
      		    "data": function (parametros) {
      		    	console.log(moduloActual.obj.filtroOperacion.val());
      		    	try{
      		    		console.log(moduloActual.obj.filtroOperacion.val());
      			      return {
      			    	filtroOperacion : moduloActual.obj.filtroOperacion.val(),
      			    	indicadorProducto: constantes.INDICADOR_PRODUCTO_SIN_DATOS,
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
      	    //TODO
      	    "templateSelection": function (registro) {
      	    	try{
      	    		moduloActual.obj.indicadorProducto = registro.indicadorProducto;
      	    	} catch (error){
      	    		
      	    	}
                 return registro.nombre || registro.text;
      	    },
        });
          
          cmpElementoProducto.on('change', function(e){
            var indiceFormulario = ($(formularioNuevo).attr('id')).substring(24);
         	//var indicadorProducto = $('option:selected', this).attr('data-indicador-producto');
         	var indicadorProducto = moduloActual.obj.indicadorProducto;
         	console.log(indicadorProducto);
        	moduloActual.desactivaValores(indiceFormulario, indicadorProducto);
          });  
    
        },
        afterRemoveCurrent: function(control) {
          if (control.hasForms()==false){
            control.addForm();          
          }
        }
      }); 

    this.obj.cmpSumVolumenTempObservada=$("#cmpSumVolumenTempObservada");
    this.obj.cmpSumVolumenTempObservada.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.cmpSumVolumen60F=$("#cmpSumVolumen60F");
    this.obj.cmpSumVolumen60F.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});

    //Campos de vista
    this.obj.vistaIdDOperativo=$("#vistaIdDOperativo");
    this.obj.vistaCliente=$("#vistaCliente");
    this.obj.vistaOperacion=$("#vistaOperacion");
    this.obj.vistaFechaPlanificacion=$("#vistaFechaPlanificacion");
    this.obj.vistaIdTransporte=$("#vistaIdTransporte");

    //campos Formulario
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNumeroGuiaRemision=$("#vistaNumeroGuiaRemision");
    this.obj.vistaNumeroOrdenCompra=$("#vistaNumeroOrdenCompra");
    this.obj.vistaNumeroFactura=$("#vistaNumeroFactura");
    this.obj.vistaCodigoScop=$("#vistaCodigoScop");
    this.obj.vistaFechaEmisionGuia=$("#vistaFechaEmisionGuia");
    this.obj.vistaPlantaDespacho=$("#vistaPlantaDespacho");
    this.obj.vistaPlantaRecepcion=$("#vistaPlantaRecepcion");
    this.obj.vistaIdCliente=$("#vistaIdCliente");
    this.obj.vistaIdConductor=$("#vistaIdConductor");
    this.obj.vistaBreveteConductor=$("#vistaBreveteConductor");
    this.obj.vistaIdCisterna=$("#vistaIdCisterna");
    this.obj.vistaPlacaCisterna=$("#vistaPlacaCisterna");
    this.obj.vistaTarjetaCubicacionCompartimento=$("#vistaTarjetaCubicacionCompartimento");
    this.obj.vistaIdTracto=$("#vistaIdTracto");
    this.obj.vistaPlacaTracto=$("#vistaPlacaTracto");
    this.obj.vistaIdTransportista=$("#vistaIdTransportista");
    this.obj.vistaVolumenTotalObservado=$("#vistaVolumenTotalObservado");
    this.obj.vistaVolumenTotalObservado.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.vistaVolumenTotalCorregido=$("#vistaVolumenTotalCorregido");
    this.obj.vistaVolumenTotalCorregido.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
    this.obj.vistaPrecintos=$("#vistaPrecintos");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaSincronizadoEl=$("#vistaSincronizadoEl");
    this.obj.vistaCisternaTracto=$("#vistaCisternaTracto");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    this.obj.vistaPesoBruto=$("#vistaPesoBruto");
    this.obj.vistaPesoTara=$("#vistaPesoTara");
    this.obj.vistaPesoNeto=$("#vistaPesoNeto");

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

    //Campos de formulario Pesaje
    this.obj.cmpPesajeIdTransporte=$("#cmpPesajeIdTransporte");
    this.obj.cmpPesajeCliente=$("#cmpPesajeCliente");
    this.obj.cmpPesajeOperacion=$("#cmpPesajeOperacion");
    this.obj.cmpPesajeFechaPlanificacion=$("#cmpPesajeFechaPlanificacion");

    this.obj.cmpPesajeNumeroGuiaRemision=$("#cmpPesajeNumeroGuiaRemision");
    this.obj.cmpPesajeNumeroOrdenCompra=$("#cmpPesajeNumeroOrdenCompra");
    this.obj.cmpPesajeCisternaTracto=$("#cmpPesajeCisternaTracto");

    this.obj.cmpPesajePesoBruto=$("#cmpPesajePesoBruto");
    this.obj.cmpPesajePesoTara=$("#cmpPesajePesoTara");
    this.obj.cmpPesajePesoNeto=$("#cmpPesajePesoNeto");

    //('decimal',  {digits: 1});
    this.obj.cmpPesajePesoBruto.inputmask('decimal', {digits: 3, groupSeparator:',',autoGroup:true, groupSize:3});
    //this.obj.cmpPesajePesoBruto.inputmask("999999.999");
    this.obj.cmpPesajePesoTara.inputmask('decimal', {digits: 3, groupSeparator:',',autoGroup:true,groupSize:3});
    //this.obj.cmpPesajePesoTara.inputmask("9999.99");
	this.obj.cmpPesajePesoNeto.inputmask('decimal', {digits: 3, groupSeparator:',',autoGroup:true,groupSize:3});

    this.obj.cmpPesajePesoBruto.on("keypress",function(){
    	var pesoBruto = moduloActual.eliminaSeparadorComa(document.forms["frmPesaje"].cmpPesajePesoBruto.value);
    	var pesoTara = moduloActual.eliminaSeparadorComa(document.forms["frmPesaje"].cmpPesajePesoTara.value);
    	var pesoNeto = parseFloat(pesoBruto) - parseFloat(pesoTara);
        document.forms["frmPesaje"].cmpPesajePesoNeto.value = pesoNeto;
    }); 

    this.obj.cmpPesajePesoTara.on("keypress",function(){
    	var pesoBruto = moduloActual.eliminaSeparadorComa(document.forms["frmPesaje"].cmpPesajePesoBruto.value);
    	var pesoTara = moduloActual.eliminaSeparadorComa(document.forms["frmPesaje"].cmpPesajePesoTara.value);
    	var pesoNeto = parseFloat(pesoBruto) - parseFloat(pesoTara);
        document.forms["frmPesaje"].cmpPesajePesoNeto.value = pesoNeto;
    });
  };

  
  moduloActual.calcularFactor= function(idElemento){
   var ref=this;
   var temp = idElemento.split("_");
   var indice = temp[1];
   var parametros={};
   var temperatura = $("#GrupoTransporte_"+indice+"_Temperatura").val();
   var apiCorregido = $("#GrupoTransporte_"+indice+"_API").val();
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
    $("#ocultaContenedorFormulario").show();
     $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_FACTOR, 
      contentType: ref.TIPO_CONTENIDO, 
      data: parametros, 
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          var registro = respuesta.contenido.carga[0];
          console.log(registro);
          var factorCorrecion = parseFloat(registro.factorCorreccion);
          $("#GrupoTransporte_"+indice+"_Factor").val(factorCorrecion);
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);         
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
  
  moduloActual.agregarFilas = function(registro){
    var compartimento;
    var cantidad = 0;
  	try{
  		moduloActual.obj.contenedorDetalles.hide();
  		moduloActual.obj.grupoTransporte.removeAllForms();
  		if(registro.compartimentos != null){
  		moduloActual.obj.contenedorDetalles.show();
  		cantidad = registro.compartimentos.length;
  	  	this.obj.grupoTransporte.addNForms(cantidad);
  	  	for(var contador=0; contador < cantidad; contador++){
  		  compartimento = registro.compartimentos[contador];
		  var formulario= moduloActual.obj.grupoTransporte.getForm(contador);
		  formulario.find("input[elemento-grupo='compartimentos']").val(compartimento.identificador); // contador
		  formulario.find("input[elemento-grupo='capVolCompartimento']").val(compartimento.capacidadVolumetrica); // contador
		  console.log("compartimento.capacidadVolumetrica " + compartimento.capacidadVolumetrica);
  	  	}
	  	moduloActual.obj.cmpSumVolumenTempObservada.val(0);
	  	moduloActual.obj.cmpSumVolumen60F.val(0);
  		}
  	} catch(error){
      console.log(error.message);
    };
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

  moduloActual.datosCabecera= function(){
	  var referenciaModulo=this;
	  //referenciaModulo.resetearFormularioPrincipal();

	  //para pantalla con dos grillas
	  moduloActual.obj.detalleCliente.text(referenciaModulo.obj.clienteSeleccionado);
	  moduloActual.obj.detalleOperacion.text(referenciaModulo.obj.operacionSeleccionado);
	  moduloActual.obj.detalleFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
	  if(referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_TRANSPORTE_NUEVO){
		  moduloActual.cmpOrigen = constantes.ORIGEN_TRANSPORTE_MANUAL;
	  }

	  //para formulario principal
  	  this.obj.cmpFormularioCliente.text(referenciaModulo.obj.clienteSeleccionado);
  	  this.obj.cmpFormularioOperacion.text(referenciaModulo.obj.operacionSeleccionado);
  	  this.obj.cmpFormularioFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
  	  moduloActual.idCliente = referenciaModulo.idCliente;

  	  //para ver
	  this.obj.vistaCliente.text(referenciaModulo.obj.clienteSeleccionado);
	  this.obj.vistaOperacion.text(referenciaModulo.obj.operacionSeleccionado);
	  this.obj.vistaFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));

  	  //para formulario Evento
  	  this.obj.cmpEventoCliente.text(referenciaModulo.obj.clienteSeleccionado);
	  this.obj.cmpEventoOperacion.text(referenciaModulo.obj.operacionSeleccionado);
	  this.obj.cmpEventoFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));

  	  //para formulario Pesaje
	  this.obj.cmpPesajeCliente.text(referenciaModulo.obj.clienteSeleccionado);
	  this.obj.cmpPesajeOperacion.text(referenciaModulo.obj.operacionSeleccionado);
	  this.obj.cmpPesajeFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
  };

  moduloActual.limpiarFormularioPrincipal = function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.frmPrincipal[0].reset();

	  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

      moduloActual.idTransportista = 0;
      moduloActual.obj.cmpCisternaTracto.select2("val", "null");
      moduloActual.obj.cmpPlantaDespacho.select2("val", null);
      moduloActual.obj.cmpTransportista.select2("val", null);
      moduloActual.obj.cmpConductor.select2("val", null);

      moduloActual.obj.cmpCisternaTracto.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpPlantaDespacho.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpTransportista.empty().append(elemento1).val(0).trigger('change');
      moduloActual.obj.cmpConductor.empty().append(elemento1).val(0).trigger('change');
  };

  moduloActual.llenarFormulario = function(registro){
	  var referenciaModulo=this;
	  this.idRegistro = referenciaModulo.idDiaOperativo;

	  //identificadores para las busquedas
	  this.obj.idDiaOperativo = referenciaModulo.idDiaOperativo;
	  this.obj.idTransporte = referenciaModulo.idTransporte;

	//  var transporte = registro.transportes[0];


	  var TipoRegistro = registro.origen;
	  console.log("Origen " + registro.origen);

	  this.obj.formularioIdTransporte.val(registro.idTransporte);
	  this.obj.cmpNumeroGuiaRemision.val(registro.numeroGuiaRemision);
	  this.obj.cmpNumeroOrdenCompra.val(registro.numeroOrdenCompra);
	  this.obj.cmpNumeroFactura.val(registro.numeroFactura);
	  this.obj.cmpCodigoScop.val(registro.codigoScop);

	  this.obj.cmpCisternaTracto.val(registro.cisternaTracto);
	  this.obj.cmpFemisionOE.val(utilitario.formatearFecha(registro.fechaEmisionGuia));
	  this.obj.cmpPrecintos.val(registro.precintosSeguridad);

	  var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.idTransportista);
	  elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.transportista.razonSocial);
	  this.obj.cmpTransportista.empty().append(elemento1).val(registro.idTransportista).trigger('change');

	  var elemento2=constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,registro.idPlantaDespacho);
	  elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.plantaDespacho.descripcion);
	  this.obj.cmpPlantaDespacho.empty().append(elemento2).val(registro.idPlantaDespacho).trigger('change');

	  var elemento3=constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento3 = elemento3.replace(constantes.ID_OPCION_CONTENEDOR, registro.idConductor);
	  elemento3 = elemento3.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.conductor.nombreCompleto);
	  this.obj.cmpConductor.empty().append(elemento3).val(registro.idConductor).trigger('change');

	  this.obj.idTransportista = registro.idTransportista;

	  var elemento3 =constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento3 = elemento3.replace(constantes.ID_OPCION_CONTENEDOR, registro.idCisterna);
	  elemento3 = elemento3.replace(constantes.VALOR_OPCION_CONTENEDOR, registro.cisternaTracto);
	  this.obj.cmpCisternaTracto.empty().append(elemento3).val(registro.idCisterna).trigger('change');

	  moduloActual.cmpIdTracto = registro.idTracto;
  	  moduloActual.cmpPlacaCisterna = registro.placaCisterna;
  	  moduloActual.cmpPlacaTracto = registro.placaTracto;
  	  moduloActual.cmpBreveteConductor = registro.breveteConductor;
  	  moduloActual.cmpTarjetaCubicacionCompartimento = registro.tarjetaCubicacionCompartimento;
  	  moduloActual.cmpFechaVigenciaTarjetaCubicacion = registro.cisterna.fechaVigenciaTarjetaCubicacion;
  	  console.log("registro.origen " + registro.origen);
  	  moduloActual.cmpOrigen = registro.origen;
  	
  	  var numeroDetalles= registro.detalles.length;
	  var sumaVolumenTempObs = 0;
	  var sumaVol60F = 0;

	  this.obj.grupoTransporte.removeAllForms();
	  moduloActual.obj.contenedorDetalles.show();
	  for(var contador=0; contador < numeroDetalles; contador++){
		  moduloActual.obj.grupoTransporte.addForm();
		  var formulario= moduloActual.obj.grupoTransporte.getForm(contador);
		  formulario.find("input[elemento-grupo='compartimentos']").val(registro.detalles[contador].numeroCompartimento); // contador
		  formulario.find("input[elemento-grupo='capVolCompartimento']").val(registro.detalles[contador].capacidadVolumetricaCompartimento); // CapacidadVolumerica del compartimento
		  
		  var elementoProducto=constantes.PLANTILLA_OPCION_SELECTBOX;
		  console.log(registro.detalles[contador].idProducto);
		  console.log(registro.detalles[contador].descripcionProducto);
		  
		  elementoProducto = elementoProducto.replace(constantes.ID_OPCION_CONTENEDOR,registro.detalles[contador].idProducto);
		  elementoProducto = elementoProducto.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.detalles[contador].descripcionProducto);
	      formulario.find("select[elemento-grupo='producto']").empty().append(elementoProducto).val(registro.detalles[contador].idProducto).trigger('change');
		  
		  //formulario.find("select[elemento-grupo='producto']").select2("val", registro.detalles[contador].idProducto); // id_producto
		  formulario.find("input[elemento-grupo='volumenTempObservada']").val(registro.detalles[contador].volumenTemperaturaObservada.toFixed(2)); // volumen_temperatura_observada
		  formulario.find("input[elemento-grupo='temperatura']").val(registro.detalles[contador].temperaturaObservada.toFixed(1)); // temperatura_observada
		  formulario.find("input[elemento-grupo='API']").val(registro.detalles[contador].apiTemperaturaBase.toFixed(1)); // api_temperatura_base
		  formulario.find("input[elemento-grupo='factor']").val(registro.detalles[contador].factorCorrecion.toFixed(6)); // factor_correcion
		  formulario.find("input[elemento-grupo='volumen60F']").val(registro.detalles[contador].volumenTemperaturaBase.toFixed(2)); // volumen_temperatura_base
		  

		  sumaVolumenTempObs = sumaVolumenTempObs + parseFloat(registro.detalles[contador].volumenTemperaturaObservada);      
		  sumaVol60F = sumaVol60F + parseFloat(registro.detalles[contador].volumenTemperaturaBase);
	  }
	  /*console.log("compartimentos " + registro.cisterna.compartimentos.length);
	  var compartimentos = registro.cisterna.cantidadCompartimentos;
	  if(compartimentos > numeroDetalles){
		  var insertarSheepet = parseInt(compartimentos) - parseInt(numeroDetalles);
		  console.log("insertarSheepet " + insertarSheepet);
		  this.obj.grupoTransporte.addNForms(insertarSheepet);
		  console.log("los compartimentos son mayores que el detalle de transporte");
	  }*/

	  this.obj.cmpSumVolumenTempObservada.val(sumaVolumenTempObs.toFixed(2));
	  this.obj.cmpSumVolumen60F.val(sumaVol60F.toFixed(2));
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var eTransporte={};
    var referenciaModulo=this;
    try {

	    //datos para la asignacion
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.idDoperativo = parseInt(referenciaModulo.idDiaOperativo);
	    eRegistro.idTransporte = parseInt(referenciaModulo.idTransporte);
	    //eRegistro.id = referenciaModulo.obj.cmpId.val();
	    //datos para transporte
	    eRegistro.transportes=[];
	    eTransporte.id = parseInt(referenciaModulo.idTransporte);
	    eTransporte.numeroGuiaRemision = referenciaModulo.obj.cmpNumeroGuiaRemision.val();
	    eTransporte.numeroOrdenCompra = referenciaModulo.obj.cmpNumeroOrdenCompra.val();
	    eTransporte.numeroFactura = referenciaModulo.obj.cmpNumeroFactura.val();
	    eTransporte.codigoScop = referenciaModulo.obj.cmpCodigoScop.val();
	    
	    eTransporte.idPlantaDespacho = parseInt(referenciaModulo.obj.cmpPlantaDespacho.val());
	    eTransporte.idTransportista = parseInt(referenciaModulo.obj.cmpTransportista.val());
	    eTransporte.idCisterna = parseInt(referenciaModulo.obj.cmpCisternaTracto.val());
	    eTransporte.idTracto = parseInt(moduloActual.cmpIdTracto);
	    eTransporte.placaCisterna = moduloActual.cmpPlacaCisterna;
	    eTransporte.placaTracto = moduloActual.cmpPlacaTracto;
	    
	    eTransporte.idConductor = parseInt(referenciaModulo.obj.cmpConductor.val());
	    eTransporte.breveteConductor = moduloActual.cmpBreveteConductor;
	    eTransporte.fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
	    eTransporte.precintosSeguridad = referenciaModulo.obj.cmpPrecintos.val();
	    
	    eTransporte.volumenTotalObservado = parseFloat(referenciaModulo.obj.cmpSumVolumenTempObservada.val().replace(moduloActual.SEPARADOR_MILES,""));
	    eTransporte.volumenTotalCorregido = parseFloat(referenciaModulo.obj.cmpSumVolumen60F.val().replace(moduloActual.SEPARADOR_MILES,""));
	    
	    eTransporte.idCliente = parseInt(moduloActual.idCliente);
	    console.log(moduloActual.cmpTarjetaCubicacionCompartimento);
	    eTransporte.tarjetaCubicacionCompartimento = moduloActual.cmpTarjetaCubicacionCompartimento;
	    eTransporte.idPlantaRecepcion = parseInt(referenciaModulo.obj.idOperacionSeleccionado); 
        eTransporte.origen = moduloActual.cmpOrigen;

	    eTransporte.detalles=[];
	    //datos para detalleTransporte
	      var numeroFormularios = referenciaModulo.obj.grupoTransporte.getForms().length;
	      for(var contador = 0; contador < numeroFormularios; contador++){
	        var detalles={};
	        var formulario = referenciaModulo.obj.grupoTransporte.getForm(contador);
	        var cmpCompartimentos			= formulario.find("input[elemento-grupo='compartimentos']");
	        var cmpCapVolCompartimento		= formulario.find("input[elemento-grupo='capVolCompartimento']");
	        var cmpElementoProducto			= formulario.find("select[elemento-grupo='producto']");
	        var cmpVolumenTempObservada		= formulario.find("input[elemento-grupo='volumenTempObservada']");
	        var cmpTemperatura				= formulario.find("input[elemento-grupo='temperatura']");
	        var cmpAPI						= formulario.find("input[elemento-grupo='API']");
	        var cmpFactor					= formulario.find("input[elemento-grupo='factor']");
	        var cmpVolumen60F				= formulario.find("input[elemento-grupo='volumen60F']");
	        
		    if(cmpElementoProducto.val() > 0){
		        detalles.idTransporte			= parseInt(referenciaModulo.idTransporte);
		        detalles.numeroCompartimento	= parseInt(cmpCompartimentos.val());
		        detalles.capacidadVolumetricaCompartimento = parseFloat(cmpCapVolCompartimento.val());
		        detalles.idProducto				= parseInt(cmpElementoProducto.val());
		        detalles.volumenTemperaturaObservada = parseFloat(cmpVolumenTempObservada.val().replace(moduloActual.SEPARADOR_MILES,""));
		        detalles.temperaturaObservada 	= parseFloat(cmpTemperatura.val().replace(moduloActual.SEPARADOR_MILES,""));
		        detalles.apiTemperaturaBase 	= parseFloat(cmpAPI.val().replace(moduloActual.SEPARADOR_MILES,""));
		        detalles.factorCorrecion 		= parseFloat(cmpFactor.val().replace(moduloActual.SEPARADOR_MILES,""));
		        detalles.volumenTemperaturaBase = parseFloat(cmpVolumen60F.val().replace(moduloActual.SEPARADOR_MILES,""));
		        eTransporte.detalles.push(detalles);
		    }
	      }
	      eRegistro.transportes.push(eTransporte);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };

//========================== cntVistaRegistro ============================================================ 
  moduloActual.llenarDetalles = function(registro){
	this.obj.vistaNumeroGuiaRemision.text(registro.numeroGuiaRemision);
	this.obj.vistaNumeroOrdenCompra.text(registro.numeroOrdenCompra);
	this.obj.vistaNumeroFactura.text(registro.numeroFactura);
	this.obj.vistaCodigoScop.text(registro.codigoScop);
	this.obj.vistaFechaEmisionGuia.text(utilitario.formatearFecha(registro.fechaEmisionGuia));
	this.obj.vistaPlantaDespacho.text(registro.plantaDespacho.descripcion);
	this.obj.vistaIdCliente.text(registro.idCliente);
	this.obj.vistaIdConductor.text(registro.conductor.apellidos + ", " + registro.conductor.nombres);
	this.obj.vistaBreveteConductor.text(registro.breveteConductor);
	this.obj.vistaIdCisterna.text(registro.idCisterna);
	this.obj.vistaPlacaCisterna.text(registro.placaCisterna);
	this.obj.vistaTarjetaCubicacionCompartimento.text(registro.tarjetaCubicacionCompartimento);
	this.obj.vistaIdTracto.text(registro.idTracto);
	this.obj.vistaPlacaTracto.text(registro.placaTracto);
	this.obj.vistaIdTransportista.text(registro.transportista.razonSocial);
	this.obj.vistaVolumenTotalObservado.text(registro.volumenTotalObservado);
	this.obj.vistaVolumenTotalCorregido.text(registro.volumenTotalCorregido);
	this.obj.vistaEstado.text(registro.estado);
	this.obj.vistaCisternaTracto.text(registro.cisternaTracto);
	this.obj.vistaPrecintos.text(registro.precintosSeguridad);
	this.obj.vistaPesoBruto.text(registro.pesoBruto);
    this.obj.vistaPesoTara.text(registro.pesoTara);
    this.obj.vistaPesoNeto.text(registro.pesoNeto);
    
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
    
    //detalle de transportes
    var indice= registro.detalles.length;
    var fila = $('#listado_vista_detalles');
    $('#listado_vista_detalles').html("");
    g_tr = '<thead><tr><th class="text-center">Compartimentos</th>' +
    				  '<th class="text-center">Producto</th>' + 
    				  '<th class="text-center">Vol. T. Obs. (gal)</th>' + 
    				  '<th class="text-center">Temperatura (F)</th>' + 
    				  '<th class="text-center">API 60 F</th>' + 
    				  '<th class="text-center">Factor</th>' + 
    				  '<th class="text-center">Vol 60F (gal)</th></tr></thead>'; 
    fila.append(g_tr);
    for(var k = 0; k < indice; k++){ 	
    g_tr  = '<tr><td class="text-right">' +registro.detalles[k].numeroCompartimento   + // compartimiento
    '    <td>' +registro.detalles[k].descripcionProducto   + '</td>' + // fecha_emision
    '    <td class="text-right">' +registro.detalles[k].volumenTemperaturaObservada  + 	'</td>' + // volumen_temperatura_observada
    '    <td class="text-right">' +registro.detalles[k].temperaturaObservada + 			'</td>' + // temperatura_observada
    '    <td class="text-right">' +registro.detalles[k].apiTemperaturaBase + 			'</td>' + // api_temperatura_base
    '    <td class="text-right">' +registro.detalles[k].factorCorrecion + 				'</td>' + // factor_correcion
    '    <td class="text-right">' +registro.detalles[k].volumenTemperaturaBase + 		'</td></tr>'; // volumen_temperatura_base
    fila.append(g_tr);
    }
    g_tr = '<tr><th></td>' +
    	   	   '<th class="text-right">Vol. T. Obs. Total (gal):</th>' +
    	   	   '<th class="text-right">' + registro.volumenTotalObservado + '</th>' +
    	   	   '<th></th>' +
    	   	   '<th></th>' +
    	   	   '<th class="text-right">Vol. 60 F Total (gal):</th>' +
    	   	   '<th class="text-right">' + registro.volumenTotalCorregido + '</th></tr>'; 
    fila.append(g_tr);

    $('#listado_vista_eventos').html("");
    //detalle de eventos
    if(registro.eventos != null){
	    var indiceEvento= registro.eventos.length;
	    var filaEvento = $('#listado_vista_eventos');
	    //$('#listado_vista_eventos').html("");
	
	    for(var r = 0; r < indiceEvento; r++){ 	
	    	g_tr = '<tr><td class="tabla-vista-titulo" style="width:10%;">Tipo:</td><td>' +utilitario.formatearTipoEvento(registro.eventos[r].tipoEvento)   + ' </td>'+
	    			   '<td class="tabla-vista-titulo" style="width:10%;">Fecha y hora:</td><td>' +utilitario.formatearTimestampToString(registro.eventos[r].fechaHoraTimestamp) + '</td><tr>';
	    	filaEvento.append(g_tr);
		    g_tr = '<tr><td class="tabla-vista-titulo" colspan="5">Observaciones:</td><tr>';
		    filaEvento.append(g_tr);
		    g_tr = '<tr><td colspan="5">' + registro.eventos[r].descripcion   + '</td><tr>';
		    filaEvento.append(g_tr);
	
	    }
    }
  };		

//========================== cntEventoTransporte ============================================================  
  moduloActual.llenarEventoTransporte = function(registro){
	  var referenciaModulo = this;
	  this.obj.cmpEventoNumeroGuiaRemision.text(referenciaModulo.obj.guiaRemisionSeleccionado);
	  this.obj.cmpEventoNumeroOrdenCompra.text(referenciaModulo.obj.ordenEntregaSeleccionado);
	  this.obj.cmpEventoCisternaTracto.text(referenciaModulo.obj.cisternaTractoSeleccionado);
  };

  moduloActual.guardarEvento = function(){
		var referenciaModulo = this;
		//Ocultar alertas de mensaje
		if (!referenciaModulo.validaFormularioXSS("#frmEvento")){
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
		} else if (referenciaModulo.obj.frmEvento.valid()){
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
			//referenciaModulo.protegeFormulario(true);
			if(this.obj.cmpEventoFechaHora.val() == ""){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La fecha y hora debe estar informada.");
		    	//referenciaModulo.protegeFormulario(false);
	    	} else if (this.obj.cmpEventoDescripcion.val() == ""){
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La observaci&oacute;n debe ser informada.");
	    		//referenciaModulo.protegeFormulario(false);
			} else {
				var eRegistro = referenciaModulo.recuperarValoresEvento();
				$.ajax({
				    type: "POST",
				    url: "./evento/crear", 
				    contentType: "application/json", 
				    data: JSON.stringify(eRegistro),	
				    success: function(respuesta) {
				    	if (!respuesta.estado) {
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				    		//referenciaModulo.protegeFormulario(false);
				    	} 	else {		    	
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
				    		//referenciaModulo.protegeFormulario(false);
				    		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
				        	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
					    	moduloActual.obj.cntDetalleTransporte.show();
					    	moduloActual.obj.cntEventoTransporte.hide();
			    		}
				    },			    		    
				    error: function() {
				    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
				    	//referenciaModulo.protegeFormulario(false);
				    }
				});
			}
		} else {
			console.log("No valido");
		}
	};
	
	moduloActual.recuperarValoresEvento = function(registro){
	    var eRegistro = {};
	    var referenciaModulo=this;
	    try {
		    eRegistro.tipoRegistro = constantes.TIPO_REGISTRO_TRANSPORTE; //1 porque el tipo de registro es transporte
		    eRegistro.tipoEvento = parseInt(this.obj.cmpEventoTipoEvento.val());
		    eRegistro.fechaHora  = utilitario.formatearStringToDateHour(this.obj.cmpEventoFechaHora.val());
		    eRegistro.descripcion = this.obj.cmpEventoDescripcion.val().toUpperCase();
		    eRegistro.idRegistro = parseInt(referenciaModulo.idTransporte);
	    } catch(error){
	      console.log(error.message);
	    }
	    return eRegistro;
	  };

//========================== FIN cntEventoTransporte ========================================================  	  

//========================== cntPesajeTransporte ============================================================  
  moduloActual.llenarPesajeTransporte = function(registro){
	  this.obj.cmpPesajeIdTransporte.val(registro.idTransporte);

	  this.obj.cmpPesajeNumeroGuiaRemision.text(registro.numeroGuiaRemision);
	  this.obj.cmpPesajeNumeroOrdenCompra.text(registro.numeroOrdenCompra);
	  this.obj.cmpPesajeCisternaTracto.text(registro.cisternaTracto);

	  this.obj.cmpPesajePesoBruto.val(registro.pesoBruto);
	  this.obj.cmpPesajePesoTara.val(registro.pesoTara);
	  this.obj.cmpPesajePesoNeto.val(registro.pesoNeto);
  };

  moduloActual.guardarPesaje= function(){
		var referenciaModulo = this;
		if (!referenciaModulo.validaFormularioXSS("#frmPesaje")){
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
		} else if (referenciaModulo.obj.frmPesaje.valid()){
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
			//referenciaModulo.protegeFormulario(true);
			if(this.obj.cmpPesajePesoBruto.val() == 0){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"El Peso Bruto no puede ser 0.");
		    	//referenciaModulo.protegeFormulario(false);
	    	} else if (this.obj.cmpPesajePesoTara.val() == 0){
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"El Peso Tara no puede ser 0.");
	    		//referenciaModulo.protegeFormulario(false);
			}
			else{
				var eRegistro = referenciaModulo.recuperarValoresPesaje();
				$.ajax({
				    type: "POST",
				    url: referenciaModulo.URL_ACTUALIZAR_PESAJE, 
				    contentType: "application/json", 
				    data: JSON.stringify(eRegistro),	
				    success: function(respuesta) {
				    	if (!respuesta.estado) {
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				    		//referenciaModulo.protegeFormulario(false);
				    	} 	else {		    	
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
				    		//referenciaModulo.protegeFormulario(false);
				    		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
				        	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
					    	moduloActual.obj.cntDetalleTransporte.show();
					    	moduloActual.obj.cntPesajeTransporte.hide();
			    		}
				    },			    		    
				    error: function() {
				    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
				    	//referenciaModulo.protegeFormulario(false);
				    }
				});
			}
		} else {
			console.log("No valido");
		}	
	};
	
	moduloActual.recuperarValoresPesaje = function(registro){
	    var eRegistro = {};
	    var referenciaModulo=this;
	    try {
    		eRegistro.id = parseInt(referenciaModulo.idTransporte);
	    	eRegistro.pesoBruto = parseFloat(this.obj.cmpPesajePesoBruto.val().replace(moduloActual.SEPARADOR_MILES,""));
	    	eRegistro.pesoTara = parseFloat(this.obj.cmpPesajePesoTara.val().replace(moduloActual.SEPARADOR_MILES,""));
	    	eRegistro.pesoNeto = parseFloat(this.obj.cmpPesajePesoNeto.val().replace(moduloActual.SEPARADOR_MILES,""));
	    }  catch(error){
	      console.log(error.message);
	    }
	    return eRegistro;
	  };

//========================== FIN cntPesajeTransporte ========================================================  	  

  moduloActual.sumaVolumenTempObservada = function(){
    var referenciaModulo=this;
    var suma = 0;
    try {
      var numeroFormularios = referenciaModulo.obj.grupoTransporte.getForms().length;
      for(var contador = 0;contador < numeroFormularios; contador++){
        var formulario = referenciaModulo.obj.grupoTransporte.getForm(contador);   
        var cmpVolumenTempObservada = formulario.find("input[elemento-grupo='volumenTempObservada']");
        suma = suma + parseFloat(moduloActual.eliminaSeparadorComa(cmpVolumenTempObservada.val()));
      }
      this.obj.cmpSumVolumenTempObservada.val(suma);
    }  catch(error){
      console.log(error.message);
    }
  };
  
  moduloActual.sumaVolumen60F = function(){
	    var referenciaModulo=this;
	    var suma = 0;
	    try {
	      var numeroFormularios = referenciaModulo.obj.grupoTransporte.getForms().length;
	      for(var contador = 0;contador < numeroFormularios; contador++){
	        var formulario = referenciaModulo.obj.grupoTransporte.getForm(contador);   
	        var cmpVolumen60F = formulario.find("input[elemento-grupo='volumen60F']");
	        suma = suma + parseFloat(moduloActual.eliminaSeparadorComa(cmpVolumen60F.val()));
	      }
	      this.obj.cmpSumVolumen60F.val(suma);
	    }  catch(error){
	      console.log(error.message);
	    }
	  };
	  
  moduloActual.desactivaValores = function(indiceFormulario, indicadorProducto){
	    var referenciaModulo=this;
	    try {
	    	 var formulario = referenciaModulo.obj.grupoTransporte.getForm(indiceFormulario);  
	         var valor = 00;
	         if(indicadorProducto == constantes.PRODUCTO_SIN_DATOS){
	        	  formulario.find("input[elemento-grupo='volumenTempObservada']").val(valor.toFixed(2)); // volumen_temperatura_observada
				  formulario.find("input[elemento-grupo='temperatura']").val(valor.toFixed(1)); // temperatura_observada
				  formulario.find("input[elemento-grupo='API']").val(valor.toFixed(1)); // api_temperatura_base
				  formulario.find("input[elemento-grupo='factor']").val(valor.toFixed(6)); // factor_correcion
				  formulario.find("input[elemento-grupo='volumen60F']").val(valor.toFixed(2)); // volumen_temperatura_base
				  
				  formulario.find("input[elemento-grupo='volumenTempObservada']").prop('disabled', true);
				  formulario.find("input[elemento-grupo='temperatura']").prop('disabled', true);
				  formulario.find("input[elemento-grupo='API']").prop('disabled', true);
				  formulario.find("input[elemento-grupo='factor']").prop('disabled', true);
				  formulario.find("input[elemento-grupo='volumen60F']").prop('disabled', true);
	         }  else {
	        	  formulario.find("input[elemento-grupo='volumenTempObservada']").prop('disabled', false);
				  formulario.find("input[elemento-grupo='temperatura']").prop('disabled', false);
				  formulario.find("input[elemento-grupo='API']").prop('disabled', false);
				  formulario.find("input[elemento-grupo='factor']").prop('disabled', false);
				  formulario.find("input[elemento-grupo='volumen60F']").prop('disabled', false);
	         }
	    }  catch(error){
	      console.log(error.message);
	    }
	  };
  
//========================== FIN cntPesajeTransporte ========================================================  	 

moduloActual.validarFechaEmision = function(retorno){
		referenciaModulo = this;
		retorno = true;
		try{
			var fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
			var fechaPlanificada = utilitario.formatearStringToDate(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
			
			if(fechaEmisionGuia.getTime() > fechaPlanificada.getTime()){
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

moduloActual.validarFormulario = function(retorno){
	console.log("entra en validarFormulario");
referenciaModulo = this;
retorno = "";
try{
	
	var guiaRemision = $("#cmpNumeroGuiaRemision").val();
	var ordenCompra = $("#cmpNumeroOrdenCompra").val();
	var scop = $("#cmpCodigoScop").val();
	
	var planta = $("#cmpPlantaDespacho").val();
	var transportista = $("#cmpTransportista").val();
	var cisterna = $("#cmpCisternaTracto").val();
	var conductor = $("#cmpConductor").val();
	var fEmision = $("#cmpFemisionOE").val();
	
	if(guiaRemision == null || guiaRemision.length == 0){
		return retorno = "El campo Guia de Remision es incorrecto, favor verifique.";
	}
	if(ordenCompra == null || ordenCompra.length == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Orden de Entrega es incorrecto, favor verifique.";
	}
	if(scop == null || scop.length == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo SCOP es incorrecto, favor verifique.";
	}
	if(planta == null || planta == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Planta es incorrecto, favor verifique.";
	}
	if(transportista == null || transportista == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Transportista es incorrecto, favor verifique.";
	}
	if(cisterna == null || cisterna == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Cisterna/Tracto es incorrecto, favor verifique.";
	}
	if(conductor == null || conductor == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Conductor es incorrecto, favor verifique.";
	}
	if(fEmision == null || fEmision == 0){
		return retorno = constantes.TIPO_MENSAJE_ERROR, "El campo Fecha emision O/E es incorrecto, favor verifique.";
	} else {
		var fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
		var fechaPlanificada = utilitario.formatearStringToDate(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
		if(fechaEmisionGuia.getTime() > fechaPlanificada.getTime()){
			return retorno = "La fecha de Emision O/E no puede ser mayor a la fecha planificada, favor verifique.";
		}
	}
	console.log(retorno);
	return retorno;
	} catch(error){
	      console.log(error.message);
		
	};
};

//TODO
moduloActual.validarDetallesTransporte = function(retorno){
	referenciaModulo = this;
	retorno = true;
	try{
		var numeroFormularios = referenciaModulo.obj.grupoTransporte.getForms().length;
		for(var contador = 0; contador < numeroFormularios; contador++){
	        var formulario = referenciaModulo.obj.grupoTransporte.getForm(contador);
	        var cmpCompartimentos			= formulario.find("input[elemento-grupo='compartimentos']");
	        var cmpElementoProducto			= formulario.find("select[elemento-grupo='producto']");
	        var cmpVolumenTempObservada		= formulario.find("input[elemento-grupo='volumenTempObservada']");
	        var cmpTemperatura				= formulario.find("input[elemento-grupo='temperatura']");
	        var cmpAPI						= formulario.find("input[elemento-grupo='API']");
	        var cmpFactor					= formulario.find("input[elemento-grupo='factor']");
	        var cmpVolumen60F				= formulario.find("input[elemento-grupo='volumen60F']");

	        if(cmpElementoProducto.val() == 0){
	        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El compartimento " + cmpCompartimentos.val() + " no tiene Producto seleccionado.");
	        	return false;
	        }
	        
	        var indicadorProducto = $('option:selected', cmpElementoProducto).attr('data-indicador-producto');
	        if(indicadorProducto != constantes.PRODUCTO_SIN_DATOS){
	        	/*if((cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) > 0 )  ||
	        			(cmpTemperatura!=null 	&& parseFloat(cmpTemperatura.val()) > 0 ) ||
	    	        	(cmpAPI!=null		 	&& parseFloat(cmpAPI.val()) > 0 ) ||
	    	        	(cmpFactor!=null 		&& parseFloat(cmpFactor.val()) > 0 ) ||
	    	        	(cmpVolumen60F!=null 	&& parseFloat(cmpVolumen60F.val()) > 0 )){
		        		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El compartimento " + cmpCompartimentos.val() + " no tiene Producto seleccionado.");
			        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
			        	return false;
	    	        }
		        } else */
		        	if((cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) <= 0 )  ||
	        			(cmpTemperatura!=null 	&& parseFloat(cmpTemperatura.val()) <= 0 ) ||
	    	        	(cmpAPI!=null		 	&& parseFloat(cmpAPI.val()) <= 0 ) ||
	    	        	(cmpFactor!=null 		&& parseFloat(cmpFactor.val()) <= 0 ) ||
	    	        	(cmpVolumen60F!=null 	&& parseFloat(cmpVolumen60F.val()) <= 0 )){
		        		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,  "Verifique que los datos del compartimento " + cmpCompartimentos.val() + " sean correctos.");
			        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
			        	return false;
		    	     }
		       // }
	        }
	    }
		console.log(retorno);
		return retorno;
		} catch(error){
		      console.log(error.message);
			
		};
	};
	
	//valida que almenos un registro este correctamente lleno
	moduloActual.validarUnDetalleTransporte = function(retorno){
		referenciaModulo = this;
		retorno = false;
		try{
			var numeroFormularios = referenciaModulo.obj.grupoTransporte.getForms().length;
			for(var contador = 0; contador < numeroFormularios; contador++){
		        var formulario = referenciaModulo.obj.grupoTransporte.getForm(contador);
		        var cmpElementoProducto			= formulario.find("select[elemento-grupo='producto']");
		        var cmpVolumenTempObservada		= formulario.find("input[elemento-grupo='volumenTempObservada']");
		        var cmpTemperatura				= formulario.find("input[elemento-grupo='temperatura']");
		        var cmpAPI						= formulario.find("input[elemento-grupo='API']");
		        var cmpFactor					= formulario.find("input[elemento-grupo='factor']");
		        var cmpVolumen60F				= formulario.find("input[elemento-grupo='volumen60F']");

		        if((cmpElementoProducto != null && parseInt(cmpElementoProducto.val())>0)&&
		        	(cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) > 0 )  &&
		        	(cmpVolumenTempObservada!=null && parseFloat(cmpVolumenTempObservada.val()) > 0 )  &&
		        	(cmpTemperatura!=null 	&& parseFloat(cmpTemperatura.val()) > 0 ) &&
		        	(cmpAPI!=null		 	&& parseFloat(cmpAPI.val()) > 0 ) &&
		        	(cmpFactor!=null 		&& parseFloat(cmpFactor.val()) > 0 ) &&
		        	(cmpVolumen60F!=null 	&& parseFloat(cmpVolumen60F.val()) > 0 )){
		        	return true;
		        }
		        else{
		        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Al menos un detalle de transporte debe estar correctamente informado.");
		        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
		        }
		    }
			return retorno;
			} catch(error){
			      console.log(error.message);
				
			};
		};
	
	moduloActual.validarFechaEmision = function(retorno){
		referenciaModulo = this;
		retorno = true;
		try{
			var fechaEmisionGuia = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFemisionOE.val());
			var fechaPlanificada = utilitario.formatearStringToDate(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
			
			console.log(fechaPlanificada);
			
			if(fechaEmisionGuia.getTime() > fechaPlanificada.getTime()){
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

	moduloActual.validarFechaVigenciaTarjetaCubicacion = function(retorno){
		referenciaModulo = this;
		retorno = true;
		try{
			var fechaVigenciaTarjetaCubicacion = utilitario.formatearStringToDate(utilitario.formatearFecha(moduloActual.cmpFechaVigenciaTarjetaCubicacion));
			var fechaActual = utilitario.formatearStringToDate(utilitario.formatearFecha(this.obj.filtroFechaPlanificada.attr('data-fecha-actual')));
			var cisterna_tracto = moduloActual.cmpPlacaCisterna + '/' + moduloActual.cmpPlacaTracto;
			if(fechaVigenciaTarjetaCubicacion.getTime() < fechaActual.getTime()){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La tarjeta de cubicacion de la Cisterna/Tracto " + cisterna_tracto +" se encuentra caducada (" + utilitario.formatearFecha(moduloActual.cmpFechaVigenciaTarjetaCubicacion) +"). No se puede registrar datos.");
	        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
	        	retorno = false;
        		return retorno;
			}
			else{
				referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_ERROR);
				referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_EXITO);
				referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_INFORMACION);
				referenciaModulo.obj.bandaInformacion.text("");
	        	referenciaModulo.obj.ocultaContenedorFormulario.hide();
	        	retorno = true;
			}
		}
		catch(error){
		      console.log(error.message);
		}
		return retorno;
	};
	
	
  moduloActual.inicializar();
});

