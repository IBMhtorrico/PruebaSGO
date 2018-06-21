$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='desconche';
  moduloActual.SEPARADOR_MILES = ",";
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'});
  moduloActual.columnasGrilla.push({ "data": 'fechaPlanificada'});
  moduloActual.columnasGrilla.push({ "data": 'placaCisterna'});
  moduloActual.columnasGrilla.push({ "data": 'numeroDesconche'});
  moduloActual.columnasGrilla.push({ "data": 'numeroCompartimento'});
  moduloActual.columnasGrilla.push({ "data": 'volumenDesconche'});
  moduloActual.columnasGrilla.push({ "data": 'estacion'});
  moduloActual.columnasGrilla.push({ "data": 'tanque'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": true, "orderable" : false,"visible":true, "render" : utilitario.formatearFecha, "className": "text-center" });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": true, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": true, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": false, "orderable": true, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 8, "searchable": false, "orderable": true, "visible":true });

  moduloActual.reglasValidacionFormulario={
	cmpRazonSocial: { required: true, maxlength: 150 },
	cmpNombreCorto: { required: true, maxlength: 20   },
	cmpRuc: { required: true, rangelength: [11, 11], number: true },
	cmpNumContrato: { required: true  },
	cmpDesContrato: { required: true  }
  };

  moduloActual.listaDescargas={};
  moduloActual.mensajesValidacionFormulario={
	cmpRazonSocial: { required:  "El campo es obligatorio",
					  maxlength: "El campo Raz&oacute;n Social debe contener 150 caracteres como m&aacute;ximo." },
	cmpNombreCorto: { required:  "El campo es obligatorio",
					  maxlength: "El campo Nombre Corto debe contener 20 caracteres como m&aacute;ximo." },
	cmpRuc: 		{ required:  "El campo es obligatorio",
					  rangelength: "El campo RUC debe contener 11 caracteres",
					  number:    "El campo RUC solo debe contener caracteres num&eacute;ricos" },
	cmpNumContrato: { required:  "El campo es obligatorio" },
	cmpDesContrato: { required:  "El campo es obligatorio" }
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	  var ref=this;
	  ref.obj.cmpFechaPlanificada=$("#cmpFechaPlanificada");
	  ref.obj.cmpTractoCisterna=$("#cmpTractoCisterna");
	  ref.obj.cmpEstacion=$("#cmpEstacion");
	  ref.obj.cmpTanque=$("#cmpTanque");
	  ref.obj.cmpNumeroDesconche=$("#cmpNumeroDesconche");
	  ref.obj.cmpVolumenDesconche=$("#cmpVolumenDesconche");
	  ref.obj.cmpVolumenDesconche.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
	  ref.obj.cmpNumeroCompartimento=$("#cmpNumeroCompartimento");
	  ref.obj.filtroOperacion=$("#filtroOperacion");
	  ref.obj.filtroCisterna=$("#filtroCisterna");
	  ref.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
	    var fechaActual = ref.obj.filtroFechaPlanificada.attr('data-fecha-actual');
	    var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
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
	    
	    ref.obj.cmpDescarga=$("#cmpDescarga");
	  ref.obj.filtroOperacion.select2();
	  ref.obj.cmpSelect2Descarga=$("#cmpDescarga").select2({
    	  ajax: {
    		    url: "./desconche/listar-combo",
    		    dataType: 'json',
    		    placeholder: "Seleccionar...",
    		    delay: 250,
    		    data: function (parametros) {
    		      return {
    		    	valorBuscado: parametros.term, // search term
    		        page: parametros.page,
    		        paginacion:0,
    		        filtroOperacion: ref.obj.filtroOperacion.val()
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
    			moduloActual.listaDescargas[registro.id]={
    					numeroMaximoDesconches:registro.numeroMaximoDesconches,
    					numeroCompartimentos:registro.numeroCompartimentos,
    					tanque:registro.tanque,
    					estacion:registro.estacion,
    					placaCisterna:registro.placaCisterna,
    					placaTracto:registro.placaTracto,
    					fechaPlanificada:registro.fechaPlanificada    					
    			};    			
    			console.log(moduloActual.listaDescargas[registro.id]);    			
    			return "<div id='select-descarga-" + registro.id + "' class='select2-user-result'>" + registro.descripcion + "</div>";
  		        //return "<div id='select-descarga-" + registro.id + "' data-numero-maximo-desconches='"+registro.numeroMaximoDesconches+"' data-numero-compartimentos='"+registro.numeroCompartimentos+"' data-tanque='"+registro.tanque+"' data-estacion='"+registro.estacion+"' data-placa-cisterna='"+registro.placaCisterna+"'  data-placa-tracto='"+registro.placaTracto+"' data-fecha-planificada='"+registro.fechaPlanificada+"' class='select2-user-result'>" + registro.descripcion + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		        return registro.descripcion || registro.text;
  		    }
      });
    
	  ref.obj.cmpDescarga.on("change", function (e) {
        var opcionSeleccionada =$(this).val();
        console.log(opcionSeleccionada);
        console.log("cmp descarga change");
         console.log(moduloActual.listaDescargas);
         if (opcionSeleccionada != null){
             var elemento =moduloActual.listaDescargas[opcionSeleccionada];        
             var fechaPlanificada = elemento.fechaPlanificada;
             var placaTracto = elemento.placaTracto;
             var placaCisterna =elemento.placaCisterna;
             var estacion = elemento.estacion;
             var tanque = elemento.tanque;
             var numeroCompartimentos = parseInt( elemento.numeroCompartimentos);
             var numeroMaximoDesconches = parseInt( elemento.numeroMaximoDesconches);
             ref.obj.cmpFechaPlanificada.val(fechaPlanificada);
             ref.obj.cmpTractoCisterna.val(placaTracto+" / " + placaCisterna);
             ref.obj.cmpEstacion.val(estacion);
             ref.obj.cmpTanque.val(tanque);
             if (ref.modoEdicion==constantes.MODO_NUEVO){
             	ref.obj.cmpNumeroDesconche.val(numeroMaximoDesconches+1);
             } else {
             	//ref.obj.cmpNumeroDesconche.val(numeroMaximoDesconches);
             }        
             ref.obj.cmpNumeroCompartimento.children().remove();
             for(var contador = 0;contador < numeroCompartimentos;contador++){
               ref.obj.cmpNumeroCompartimento.append($('<option>', { 
                 value: contador+1,
                 text : contador+1
               })); 
             } 
         }       
      });

    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaClienteOperacion=$("#vistaClienteOperacion");
    this.obj.vistaFechaPlanificada=$("#vistaFechaPlanificada");
    this.obj.vistaTractoCisterna=$("#vistaTractoCisterna");
    this.obj.vistaEstacion=$("#vistaEstacion");
    this.obj.vistaTanque=$("#vistaTanque");
    this.obj.vistaNumeroDesconche=$("#vistaNumeroDesconche");    
    this.obj.vistaVolumen=$("#vistaVolumen");
    this.obj.vistaCompartimento=$("#vistaCompartimento");    
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");
    this.obj.btnAgregarDesconche=$("#btnAgregarDesconche");
    this.obj.btnModificarDesconche=$("#btnModificarDesconche");
    this.obj.btnVerDesconche=$("#btnVerDesconche");
    
    this.obj.btnAgregarDesconche.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'CREAR_DESCONCHE';
    	moduloActual.validaPermisos();
  	  	//ref.iniciarAgregar();
    });
    
    this.obj.btnModificarDesconche.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'ACTUALIZAR_DESCONCHE';
    	moduloActual.validaPermisos();
  	  	//ref.iniciarModificar();
	});
    
    this.obj.btnVerDesconche.on(ref.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'RECUPERAR_DESCONCHE';
    	moduloActual.validaPermisos();
    	//ref.iniciarVer();		
    });
    
  };
  

	moduloActual.activarBotones = function() {
		this.obj.btnModificarDesconche.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		this.obj.btnVerDesconche.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	};

	moduloActual.desactivarBotones = function() {
		this.obj.btnModificarDesconche.addClass(constantes.CSS_CLASE_DESHABILITADA);
		this.obj.btnVerDesconche.addClass(constantes.CSS_CLASE_DESHABILITADA);
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
			      if (referenciaModulo.descripcionPermiso == 'CREAR_DESCONCHE'){
			    	  referenciaModulo.iniciarAgregar();
			      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_DESCONCHE'){
			    	  referenciaModulo.iniciarModificar();
			      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_DESCONCHE'){
			    	  referenciaModulo.iniciarVer();
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
  
  
  moduloActual.iniciarAgregar= function(){  
		var referenciaModulo=this;
		try {
	    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
	    referenciaModulo.resetearFormulario();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	    
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		};
	};

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,5).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>  '+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };

  moduloActual.llenarFormulario = function(registro){
	var ref = this;
    this.idRegistro= registro.id;
    ref.listaDescargas[registro.idDescargaCisterna]={
		numeroMaximoDesconches:registro.numeroMaximoDesconches,
		numeroCompartimentos:registro.numeroCompartimentos,
		tanque:registro.tanque,
		estacion:registro.estacion,
		placaCisterna:registro.placaCisterna,
		placaTracto:registro.placaTracto,
		fechaPlanificada:utilitario.formatearFecha(registro.fechaPlanificada)    					
	};
    
    var opcionElemento = "<option value='"+registro.idDescargaCisterna+"'>"+"F. Planificada:"+utilitario.formatearFecha(registro.fechaPlanificada)+" Tracto / Cisterna: "+registro.placaTracto+" / " + registro.placaCisterna+" Estaci�n: "+registro.estacion+" Tanque: "+registro.tanque+"</option>";
    ref.obj.cmpDescarga.empty().append(opcionElemento).val(registro.idDescargaCisterna).trigger('change');
    ref.obj.cmpFechaPlanificada.val(utilitario.formatearFecha(registro.fechaPlanificada));
    ref.obj.cmpTractoCisterna.val(registro.placaTracto+" / " + registro.placaCisterna);
    ref.obj.cmpEstacion.val(registro.estacion);
    ref.obj.cmpTanque.val(registro.tanque);	
    ref.obj.cmpNumeroDesconche.val(registro.numeroDesconche);
    ref.obj.cmpNumeroCompartimento.val(registro.numeroCompartimento);
    ref.obj.cmpVolumenDesconche.val(registro.volumenDesconche);
    
  };
  
  moduloActual.resetearFormulario= function(){
  var referenciaModulo= this;
  referenciaModulo.obj.frmPrincipal[0].reset();
  referenciaModulo.obj.verificadorFormulario.resetForm();
  jQuery.each( this.obj, function( i, val ) {
    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
    	  console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
        //referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
      }
    }
  });  
  referenciaModulo.obj.cmpSelect2Descarga.val(null).trigger("change");
  var elemento ="<option>Seleccionar...</option>";
  //referenciaModulo.obj.cmpDescarga.empty().append(elemento).val(registro.cliente.id).trigger('change');
  
};

moduloActual.llamadaAjax=function(d){
	var referenciaModulo =this;
    var indiceOrdenamiento = d.order[0].column;
    d.registrosxPagina =  d.length; 
    d.inicioPagina = d.start; 
    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
    d.sentidoOrdenamiento=d.order[0].dir;
    d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
    var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
    var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
    var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
    d.filtroFechaInicio= fechaInicio;	
    d.filtroFechaFinal = fechaFinal;
    d.filtroPlacaCisterna= referenciaModulo.obj.filtroCisterna.val();
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
      console.log("errorPlacement");
      console.log(error);
      //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,mensaje);      
    },
    errorClass: "has-error",
    validClass: "has-success",
    showErrors: function(errorMap, errorList) {
      console.log("Custom showErrors");
      console.log("checkForm");
      this.checkForm();
      console.log("this.errorMap");
      console.log(this.errorMap);
      console.log("this.errorList");
      console.log(this.errorList);
      this.defaultShowErrors();
      console.log("this.errorList.length");
      console.log(this.errorList.length);
      var numeroErrores = this.errorList.length;
      if (numeroErrores > 0) {
        var mensaje = numeroErrores == 1 ? 'Existe un campo con error.' : 'Existen ' + numeroErrores + ' campos con errores';
        for (var indice in this.errorMap){
          console.log(indice);
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
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    
    this.obj.vistaClienteOperacion.text($("#filtroOperacion option:selected").text());
    this.obj.vistaFechaPlanificada.text(utilitario.formatearFecha(registro.fechaPlanificada));
    this.obj.vistaTractoCisterna.text(registro.placaTracto + "/" +registro.placaCisterna);
    this.obj.vistaEstacion.text(registro.estacion);
    this.obj.vistaTanque.text(registro.tanque);
    this.obj.vistaNumeroDesconche.text((registro.numeroDesconche));
    this.obj.vistaVolumen.text(registro.volumenDesconche);
    this.obj.vistaCompartimento.text((registro.numeroCompartimento));
    
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIPCreacion.text(registro.ipCreacion);
    this.obj.vistaIPActualizacion.text(registro.ipActualizacion);
  };

  moduloActual.recuperarValores = function(registro){    
    var referenciaModulo=this;
    var eRegistro = {};   
    try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);
	    eRegistro.numeroCompartimento =parseInt(referenciaModulo.obj.cmpNumeroCompartimento.val()) ;
	    eRegistro.numeroDesconche = parseInt(referenciaModulo.obj.cmpNumeroDesconche.val()) ;
	    eRegistro.volumen =parseFloat(referenciaModulo.obj.cmpVolumenDesconche.val().replace(moduloActual.SEPARADOR_MILES,""));
	    eRegistro.idDescargaCisterna= parseInt(referenciaModulo.obj.cmpDescarga.val());   
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});