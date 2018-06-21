$(document).ready(function(){
  var moduloActual = new moduloBase();
  moduloActual.indiceCompartimento=0;
  moduloActual.urlBase='cisterna';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];

  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'placa'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'tracto.placa'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'transportista.razonSocial'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target5

  moduloActual.definicionColumnas.push({ "targets": 1,  "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({ "targets": 2,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 3,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 4,  "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 5,  "searchable": true, "orderable": true, "visible":true, "render": utilitario.formatearEstado });
	 
  moduloActual.reglasValidacionFormulario={ 
    cmpPlaca: 				{ required: true, maxlength: 15,},
    cmpIdTracto: 			{ required: true,},
    cmpTarjetaCubicacion:	{ required: true, maxlength: 20,},
    cmpFechaVigenciaTarjetaCubicacion: { required: true },
    cmpCantCompartimentos: 	{ required: true, rangelength: [1, 1], number: true, },
    txtFiltro:{ maxlength: 15}
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpPlaca: 				{ required: "El campo Placa es obligatorio", maxlength: "El campo Placa debe contener 15 caracteres como maximo." },
	cmpIdTracto: 			{ required: "El campo Tracto es obligatorio" },
	cmpTarjetaCubicacion:	{ required: "El campo Tarjeta Cubicacion es obligatorio", maxlength: "El campo Tarjeta Cubicacion debe contener 20 caracteres como m&aacute;ximo." },
	cmpFechaVigenciaTarjetaCubicacion:	{ required: "El campo es obligatorio"},
	cmpCantCompartimentos: 	{ required: "El campo Fecha Vigencia es obligatorio",
	  						  rangelength: "El campo Fecha Vigencia debe contener 1 caracter",
	   						  number: "El campo Fecha Vigencia solo debe contener caracteres numericos" }
  };

  moduloActual.inicializarCampos= function(){
	this.obj.btnConfirmarModificarRegistro=$("#btnConfirmarModificarRegistro");
	this.obj.frmConfirmarModificarRegistro=$("#frmConfirmarModificarRegistro");
	this.idCisterna=$("#idCisterna");
	this.idCompartimento=$("#idCompartimento");
	this.indiceFormulario=$("#indiceFormulario");
	
    //Campos de formulario
	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
	this.obj.cmpFiltroTransportista=$("#cmpFiltroTransportista");
	
	this.obj.cmpFiltroTransportista.tipoControl="select2";
    this.obj.cmpSelect2FiltroTransportista=$("#cmpFiltroTransportista").select2({
	  ajax: {
		    url: "./transportista/listar",
		    dataType: 'json',
		    delay: 250,
		    "data": function (parametros) {
		    	try{
			      return {
			    	valorBuscado: parametros.term,
			        filtroEstado: constantes.ESTADO_ACTIVO
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
	        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
	    },
	    "templateSelection": function (registro) {
	        return registro.razonSocial || registro.text;
	    },
  });
    
    
	this.obj.cmpPlaca=$("#cmpPlaca");
    this.obj.cmpIdTracto=$("#cmpIdTracto");
    this.obj.cmpIdTransportista=$("#cmpIdTransportista");
    this.obj.cmpNombreTransportista=$("#cmpNombreTransportista");
    this.obj.cmpTarjetaCubicacion=$("#cmpTarjetaCubicacion");
    this.obj.cmpFechaVigenciaTarjetaCubicacion=$("#cmpFechaVigenciaTarjetaCubicacion");
    this.obj.cmpFechaVigenciaTarjetaCubicacion.inputmask(constantes.FORMATO_FECHA, 
    {
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpIdTracto.tipoControl="select2";
    this.obj.cmpSelect2Tracto=$("#cmpIdTracto").select2({
	  ajax: {
		    url: "./tracto/listar",
		    dataType: 'json',
		    delay: 250,
		    "data": function (parametros) {
		    	try{
			      return {
			    	valorBuscado: parametros.term,
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
	        return "<div class='select2-user-result'>" + registro.placa + "</div>";
	    },
	    "templateSelection": function (registro) {
	    	try{
	    		moduloActual.obj.cmpIdTransportista = registro.idTransportista;
	    		moduloActual.obj.cmpNombreTransportista.val(registro.razonSocialTransportista);
	    	} catch(error){
	        	moduloActual.mostrarDepuracion(error.message);
	        };
	        return registro.placa || registro.text;
	    },
  });
  this.obj.btnAgregarCompartimento=$("#btnAgregarCompartimento");

  this.obj.GrupoCompartimento = $('#GrupoCompartimento').sheepIt({
    separator: '',
    allowRemoveLast: true,
    allowRemoveCurrent: true,
    allowRemoveAll: true,
    allowAdd: true,
    allowAddN: false,
    //maxFormsCount: 3, Atencion Ticket 7000002025 jmatos
    minFormsCount: 0,
    iniFormsCount: 0,
    afterAdd: function(origen, formularioNuevo) {
      var cmpIdCompartimento=$(formularioNuevo).find("input[elemento-grupo='idCompartimento']");
      var cmpNumeroCompartimento=$(formularioNuevo).find("input[elemento-grupo='numeroCompartimento']");
      var cmpAlturaFlecha=$(formularioNuevo).find("input[elemento-grupo='alturaFlecha']");
      var cmpCapacidadVolumetrica=$(formularioNuevo).find("input[elemento-grupo='capacidadVolumetrica']");      
      var cmpAforos=$(formularioNuevo).find("input[elemento-grupo='aforos']");
      var cmpDescargas=$(formularioNuevo).find("input[elemento-grupo='descargas']");
      var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");

      cmpNumeroCompartimento.val(moduloActual.obj.GrupoCompartimento.getFormsCount());
      cmpAlturaFlecha.inputmask('integer');
      cmpCapacidadVolumetrica.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
      cmpDescargas.val("NO");
      cmpIdCompartimento.val(0);

      cmpElimina.on("click", function(){
      try{
    	moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(27);
    	if(cmpAforos.val() > 0){
    	  moduloActual.obj.frmConfirmarModificarRegistro.modal("show");
    	  moduloActual.idCompartimento = cmpIdCompartimento.val();
    	} else{
    	  moduloActual.obj.GrupoCompartimento.removeForm(moduloActual.indiceFormulario);
    	  var numeroCompartimentos = moduloActual.obj.GrupoCompartimento.getFormsCount();
    	  console.log("numeroCompartimentos " + numeroCompartimentos);
    	  for(var contador=0; contador < numeroCompartimentos; contador++){
			var fila= moduloActual.obj.GrupoCompartimento.getForm(contador);
			fila.find("input[elemento-grupo='numeroCompartimento']").val(contador+1); // contador
	  	  }
    	}
      } catch(error){
        console.log(error.message);
      }
    });
  }
    
/*  afterRemoveCurrent: function(control) {
    var numeroCompartimentos = moduloActual.obj.GrupoCompartimento.getFormsCount();
    for(var indice=0;indice < numeroCompartimentos;indice++){
      $("#GrupoCompartimento_"+indice+"_NumeroCompartimento").val(indice+1);
    }
    if (control.hasForms()==false){
      control.addForm();
    };
  }*/
});
  
  moduloActual.obj.btnConfirmarModificarRegistro.on(constantes.NOMBRE_EVENTO_CLICK,function(){
    try {
      moduloActual.obj.frmConfirmarModificarRegistro.modal("hide");
  	  moduloActual.obj.GrupoCompartimento.removeForm(moduloActual.indiceFormulario);
  	  var numeroCompartimentos = moduloActual.obj.GrupoCompartimento.getFormsCount();
	  console.log("numeroCompartimentos " + numeroCompartimentos);
	  for(var contador=0; contador < numeroCompartimentos; contador++){
		var fila= moduloActual.obj.GrupoCompartimento.getForm(contador);
		fila.find("input[elemento-grupo='numeroCompartimento']").val(contador+1); // contador
 	  }
    } catch(error){
    	moduloActual.mostrarDepuracion(error.message);
    };
  });
  
  this.obj.GrupoCompartimento.addForm();

  this.obj.btnAgregarCompartimento.on("click",function(){
    try {
      moduloActual.obj.GrupoCompartimento.addForm();
    } catch(error){
    	moduloActual.mostrarDepuracion(error.message);
    };
  });
  
  //Campos de vista
  this.obj.vistaId=$("#vistaId");
  this.obj.vistaPlaca=$("#vistaPlaca");
  this.obj.vistaIdTracto=$("#vistaIdTracto");
  this.obj.vistaIdTransportista=$("#vistaIdTransportista");
  this.obj.vistaTarjetaCubicacion=$("#vistaTarjetaCubicacion");
  this.obj.vistaFechaVigenciaTarjetaCubicacion=$("#vistaFechaVigenciaTarjetaCubicacion");
  this.obj.vistaCantCompartimentos=$("#vistaCantCompartimentos");
  this.obj.vistaEstado=$("#vistaEstado");
  this.obj.vistaSincronizadoEl=$("#vistaSincronizadoEl");
  this.obj.vistaFechaReferencia=$("#vistaFechaReferencia");
  this.obj.vistaCodigoReferencia=$("#vistaCodigoReferencia");
  this.obj.vistaCreadoEl=$("#vistaCreadoEl");
  this.obj.vistaCreadoPor=$("#vistaCreadoPor");
  this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
  this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
  this.obj.vistaIpCreacion=$("#vistaIpCreacion");
  this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  };

  moduloActual.inicializaCamposFormulario = function(){
  	try {
  		$('#cmpPlaca').removeClass("error");
  		$('#cmpCantCompartimentos').removeClass("error");  		
  		$('#cmpPlaca-error').text(null);
  		$('#cmpIdTracto-error').text(null);
  	}  catch(error){
       this.mostrarDepuracion(error.message);
    }
  };
  
//  moduloActual.resetearFormulario= function(){
//	  var referenciaModulo= this;
//	  referenciaModulo.obj.frmPrincipal[0].reset();
//	  jQuery.each( this.obj, function( i, val ) {
//	    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
//	      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
//	        referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
//	      }
//	    }
//	  });
//	  this.obj.GrupoCompartimento.removeAllForms();
//	  this.obj.GrupoCompartimento.addForm();
//	};

  /*moduloActual.despuesDeEliminarCompartimento = function() {
    var numeroCompartimentos = moduloActual.obj.GrupoCompartimento.getFormsCount();
    console.log("numero compartimentos "  + numeroCompartimentos);
    
    for(var indice=0; indice < numeroCompartimentos; indice++){
      $("#GrupoCompartimento_"+indice+"_NumeroCompartimento").val(indice+1);
    }
    if (numeroCompartimentos == 0){
	  moduloActual.obj.GrupoCompartimento.addForm();
    };
  };*/
  
  moduloActual.llamadaAjax=function(d){
	var referenciaModulo =this;
    var indiceOrdenamiento = d.order[0].column;
    d.registrosxPagina =  d.length; 
    d.inicioPagina = d.start; 
    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
    d.sentidoOrdenamiento=d.order[0].dir;
    d.valorBuscado=d.search.value;
    d.txtFiltro = referenciaModulo.obj.txtFiltro.val();
    d.filtroEstado= referenciaModulo.obj.cmpFiltroEstado.val();
    d.idTransportista = referenciaModulo.obj.cmpFiltroTransportista.val();
  };
  
  moduloActual.resetearFormulario = function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.frmPrincipal[0].reset();

	  var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");

      moduloActual.obj.cmpIdTracto.select2("val", null);
      moduloActual.obj.cmpIdTransportista = 0;
	  moduloActual.obj.cmpNombreTransportista.val("");
      moduloActual.obj.cmpIdTracto.empty().append(elemento1).val(0).trigger('change');

      this.obj.GrupoCompartimento.removeAllForms();
      this.obj.GrupoCompartimento.addForm();
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
	var referenciaModulo=this;
	var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,5).data();
	referenciaModulo.estadoRegistro=estadoRegistro;
	if (estadoRegistro == constantes.ESTADO_ACTIVO) {
		referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
	} else {
		referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i> '+constantes.TITULO_ACTIVAR_REGISTRO);			
    }
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.idCisterna = registro.id;
    this.obj.cmpPlaca.val(registro.placa);
    this.obj.cmpIdTracto.val(registro.idTracto);

    this.obj.cmpTarjetaCubicacion.val(registro.tarjetaCubicacion);
    this.obj.cmpFechaVigenciaTarjetaCubicacion.val(utilitario.formatearFecha(registro.fechaVigenciaTarjetaCubicacion));

    var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.tracto.id);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.tracto.placa);
    this.obj.cmpIdTracto.empty().append(elemento1).val(registro.tracto.id).trigger('change');

    this.obj.cmpIdTransportista = registro.idTransportista;
    this.obj.cmpNombreTransportista.val(registro.transportista.razonSocial);

    if (registro.compartimentos != null) {
        var numeroCompartimentos = registro.compartimentos.length;
        this.obj.GrupoCompartimento.removeAllForms();
        for(var contador=0; contador < numeroCompartimentos;contador++){
          this.obj.GrupoCompartimento.addForm();
          var formulario= this.obj.GrupoCompartimento.getForm(contador);
          formulario.find("input[elemento-grupo='idCompartimento']").val(registro.compartimentos[contador].id);
          formulario.find("input[elemento-grupo='numeroCompartimento']").val(registro.compartimentos[contador].identificador);
          formulario.find("input[elemento-grupo='alturaFlecha']").val(registro.compartimentos[contador].alturaFlecha);
          formulario.find("input[elemento-grupo='capacidadVolumetrica']").val(registro.compartimentos[contador].capacidadVolumetrica);  
          formulario.find("input[elemento-grupo='aforos']").val(registro.compartimentos[contador].cantidadAforos); 
          var cantidad_descarga = registro.compartimentos[contador].descargas;

          //TODO
          if (cantidad_descarga > 0){
        	  formulario.find("input[elemento-grupo='descargas']").val("SI");
        	  formulario.find("[elemento-grupo='botonElimina']").addClass(constantes.CSS_CLASE_DESHABILITADA);
          }else{
        	  formulario.find("input[elemento-grupo='descargas']").val("NO");
        	  formulario.find("[elemento-grupo='botonElimina']").removeClass(constantes.CSS_CLASE_DESHABILITADA);
          }
        }
    } else {
    	this.obj.GrupoCompartimento.removeAllForms();
    }
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaPlaca.text(registro.placa);
    this.obj.vistaIdTracto.text(registro.tracto.placa);
    this.obj.vistaIdTransportista.text(registro.transportista.razonSocial);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaSincronizadoEl.text(registro.sincronizadoEl);
    this.obj.vistaFechaReferencia.text(registro.fechaReferencia);
    this.obj.vistaTarjetaCubicacion.text(registro.tarjetaCubicacion);
    this.obj.vistaFechaVigenciaTarjetaCubicacion.text(utilitario.formatearFecha(registro.fechaVigenciaTarjetaCubicacion));
    this.obj.vistaCantCompartimentos.text(registro.cantidadCompartimentos);
    this.obj.vistaCodigoReferencia.text(registro.codigoReferencia);
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIpCreacion.text(registro.ipCreacion);
    this.obj.vistaIpActualizacion.text(registro.ipActualizacion);
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
      eRegistro.id = parseInt(referenciaModulo.idRegistro);
      eRegistro.placa = referenciaModulo.obj.cmpPlaca.val().toUpperCase();
      eRegistro.idTracto = parseInt(referenciaModulo.obj.cmpIdTracto.val());
      console.log("moduloActual.obj.cmpIdTransportista " + moduloActual.obj.cmpIdTransportista);
      eRegistro.idTransportista = moduloActual.obj.cmpIdTransportista;
      eRegistro.tarjetaCubicacion = referenciaModulo.obj.cmpTarjetaCubicacion.val().toUpperCase();
      eRegistro.fechaVigenciaTarjetaCubicacion = utilitario.formatearStringToDate(referenciaModulo.obj.cmpFechaVigenciaTarjetaCubicacion.val());
      eRegistro.compartimentos=[];   
      var numeroFormularios = referenciaModulo.obj.GrupoCompartimento.getForms().length;
      eRegistro.cantidadCompartimentos = numeroFormularios;
      for(var contador = 0;contador < numeroFormularios; contador++){
	      var compartimento={};
	      var formulario = referenciaModulo.obj.GrupoCompartimento.getForm(contador);
	      var cmpIdCompartimento = formulario.find("input[elemento-grupo='idCompartimento']");
	      var cmpNumeroCompartimento = formulario.find("input[elemento-grupo='numeroCompartimento']");
	      var cmpAlturaFlecha = formulario.find("input[elemento-grupo='alturaFlecha']");
	      var cmpCapacidadVolumetrica= formulario.find("input[elemento-grupo='capacidadVolumetrica']");
	      compartimento.idCisterna= parseInt(eRegistro.id);
	      compartimento.id = parseInt(cmpIdCompartimento.val());
	      compartimento.identificador = parseInt(cmpNumeroCompartimento.val());
	      compartimento.alturaFlecha = parseInt(cmpAlturaFlecha.val());
	      compartimento.capacidadVolumetrica= parseFloat(cmpCapacidadVolumetrica.val().replace(moduloActual.SEPARADOR_MILES,""));
	      eRegistro.compartimentos.push(compartimento);
      }
      console.log(eRegistro);
    } catch(error){
      referenciaModulo.mostrarDepuracion(error.message);
    }
    return eRegistro;
  };

  moduloActual.inicializar();
});