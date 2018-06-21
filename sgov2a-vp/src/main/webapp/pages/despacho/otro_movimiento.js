$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='otroMovimiento';
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'jornada.estacion.nombre'}); 
  moduloActual.columnasGrilla.push({ "data": 'jornada.fechaOperativa'});
  moduloActual.columnasGrilla.push({ "data": 'jornada.estado'});
  moduloActual.columnasGrilla.push({ "data": 'numeroMovimiento'});
  moduloActual.columnasGrilla.push({ "data": 'clasificacion'});
  moduloActual.columnasGrilla.push({ "data": 'volumen'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true, "render" : utilitario.formatearFecha ,"className": "text-center"});
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": true, "visible":true, "render" : utilitario.formatearEstadoJornada});
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": true, "visible":true, "className": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": false, "orderable": true, "visible":true, "render" : utilitario.formatearClasificacionOtroMovimiento});
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": false, "orderable": true, "visible":true, "className": "text-right"  });
    
  moduloActual.reglasValidacionFormulario={
	cmpTipo: { required: true },
	cmpClasificacion: { required: true },
	cmpTanqueOrigen: { required: true },
	cmpTanqueDestino: { required: true  },
	cmpVolumen: { required: true, number: true , rangelength: [1,6]},
	cmpComentario: { required: true  }	
  };

  moduloActual.mensajesValidacionFormulario={
    cmpTipo: {
		required: "El campo 'Tipo' es obligatorio"
	},
	cmpClasificacion: {
		required: "El campo 'Clasificacion' es obligatorio"
	},
	cmpTanqueOrigen: {
		required: "El campo 'Tanque Origen' es obligatorio"
	},
	cmpTanqueDestino: {
		required: "El campo 'Tanque Destino' es obligatorio"
	},
	cmpVolumen: {
		required: "El campo 'Volumen' es obligatorio",
		number: "El campo Volumen solo debe contener caracteres numericos",
		rangelength: "El campo Volumen debe contener entre 1 y 6 caracteres numericos"
	},
	cmpComentario: {
		required: "El campo 'Comentario' es obligatorio"
	}	
  };
  
  moduloActual.inicializarCampos= function(){	
	this.obj.nombreEstacion=$("#nombreEstacion");
	this.obj.idEstacion=$("#idEstacion");
	this.obj.idJornada=$("#idJornada");
	this.obj.nombreOperacionCliente=$("#nombreOperacionCliente");
          
    //Campos de formulario
	this.obj.cmpOperacionCliente=$("#cmpOperacionCliente");
    this.obj.cmpEstacion=$("#cmpEstacion");
    this.obj.cmpDiaOperativo=$("#cmpDiaOperativo");
    this.obj.cmpTipo=$("#cmpTipo");
    this.obj.cmpClasificacion=$("#cmpClasificacion");
    this.obj.cmpTanqueOrigen=$("#cmpTanqueOrigen");
    this.obj.cmpTanqueDestino=$("#cmpTanqueDestino");
    this.obj.cmpVolumen=$("#cmpVolumen");
    this.obj.cmpComentario=$("#cmpComentario");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaOperacionCliente=$("#vistaOperacionCliente");
    this.obj.vistaEstacion=$("#vistaEstacion");
    this.obj.vistaDiaOperativo=$("#vistaDiaOperativo");
    this.obj.vistaTipo=$("#vistaTipo");
    this.obj.vistaClasificacion=$("#vistaClasificacion");
    this.obj.vistaTanqueOrigen=$("#vistaTanqueOrigen");
    this.obj.vistaTanqueDestino=$("#vistaTanqueDestino");
    this.obj.vistaVolumen=$("#vistaVolumen");
    this.obj.vistaComentario=$("#vistaComentario");
    //-auditoria
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");	
      
	//FILTRO OPERACION/CLIENTE  
    this.obj.filtroOperacion=$("#filtroOperacion");
    this.obj.filtroOperacion.on('change', function(e){
        moduloActual.idOperacion=$(this).val();
        moduloActual.nombreOperacion=$(this).find("option:selected").attr('data-nombre-operacion');
        moduloActual.nombreCliente=$(this).find("option:selected").attr('data-nombre-cliente');   
        moduloActual.obj.cmpFiltroEstacion.select2("val", moduloActual.obj.cmpFiltroEstacion.attr("data-valor-inicial"));        
        
        //$("#cmpFiltroEstacion").find("option:selected").val(0);        
        //$("#cmpFiltroEstacion").val(0).trigger('change');
        //moduloActual.obj.cmpEstacion.val(0);
        		
     
        e.preventDefault(); 
    }); 
    this.obj.filtroOperacion.select2();
    //FILTRO ESTACION
    this.obj.cmpFiltroEstacion=$("#cmpFiltroEstacion");
    this.obj.cmpFiltroEstacion.tipoControl="select2";
    this.obj.cmpSelect2Estacion=$("#cmpFiltroEstacion").select2({
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
    //FILTRO DIA OPERATIVO
    this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");
    var fechaActual = this.obj.filtroFechaPlanificada.attr('data-fecha-actual');
    var rangoSemana = utilitario.retornarRangoSemana(fechaActual);
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
  //FILTRO TANQUE ORIGEN
    this.obj.cmpTanqueOrigen=$("#cmpTanqueOrigen");
    this.obj.cmpTanqueOrigen.tipoControl="select2";
    this.obj.cmpSelect2TanqueOrigen=$("#cmpTanqueOrigen").select2({
  	  ajax: {
  		    url: "./tanque/listar",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		      return {
  		    	valorBuscado: parametros.term, // search term
  		        page: parametros.page,
  		        paginacion:0,
  		        filtroEstacion:moduloActual.obj.idEstacion
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
		        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
		    },
		    templateSelection: function (registro) {	    	
		        return registro.descripcion || registro.text;
		    },
    });
  //FILTRO TANQUE DESTINO
    this.obj.cmpTanqueDestino=$("#cmpTanqueDestino");
    this.obj.cmpTanqueDestino.tipoControl="select2";
    this.obj.cmpSelect2TanqueDestino=$("#cmpTanqueDestino").select2({
  	  ajax: {
  		    url: "./tanque/listar",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		      return {
  		    	valorBuscado: parametros.term, // search term
  		        page: parametros.page,
  		        paginacion:0,
  		        filtroEstacion:moduloActual.obj.idEstacion
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
		        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
		    },
		    templateSelection: function (registro) {    	
		        return registro.descripcion || registro.text;
		    },
    });
 /*   //TANQUE ORIGEN Y DESTINO - VALIDACIONES
    this.obj.cmpTanqueOrigen.on('change', function(e){
    	try {
    		 if($("#cmpTanqueOrigen").val()!=0 && $("#cmpTanqueDestino").val()!=0 && ($("#cmpTanqueOrigen").val()==$("#cmpTanqueDestino").val())){
    	        	moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Los tanques origen y destino no deben ser iguales");
    	        }
		} catch (e) { 
			 console.log('e:'+e.message);
		}       
       
    }); 
    this.obj.cmpTanqueDestino.on('change', function(e){
    	try {
    		 if($("#cmpTanqueOrigen").val()!=0 && $("#cmpTanqueDestino").val()!=0 && ($("#cmpTanqueOrigen").val()==$("#cmpTanqueDestino").val())){
    	        	moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Los tanques origen y destino no deben ser iguales");
    	        }
		} catch (e) {
			 console.log('e:'+e.message);
		}
       
    });*/
    this.obj.btnAgregarOtroMovimiento=$("#btnAgregarOtroMovimiento");
    this.obj.btnModificarOtroMovimiento=$("#btnModificarOtroMovimiento");
    this.obj.btnVerOtroMovimiento=$("#btnVerOtroMovimiento");
    
    moduloActual.obj.btnAgregarOtroMovimiento.on(constantes.NOMBRE_EVENTO_CLICK,function(){
    	moduloActual.descripcionPermiso = 'CREAR_OTRO_MOVIMIENTO';
    	moduloActual.validaPermisos();
	    //referenciaModulo.iniciarAgregar();
	});
	
    moduloActual.obj.btnModificarOtroMovimiento.on(constantes.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'ACTUALIZAR_OTRO_MOVIMIENTO';
    	moduloActual.validaPermisos();
	    //referenciaModulo.iniciarModificar();
	});
	
    moduloActual.obj.btnVerOtroMovimiento.on(constantes.NOMBRE_EVENTO_CLICK,function(){
		moduloActual.descripcionPermiso = 'RECUPERAR_OTRO_MOVIMIENTO';
    	moduloActual.validaPermisos();
		//referenciaModulo.iniciarVer();		
	});
  };
  
  moduloActual.activarBotones=function(){
	  moduloActual.obj.btnModificarOtroMovimiento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	  moduloActual.obj.btnVerOtroMovimiento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  };
	
  moduloActual.desactivarBotones=function(){
	  moduloActual.obj.btnModificarOtroMovimiento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	  moduloActual.obj.btnVerOtroMovimiento.addClass(constantes.CSS_CLASE_DESHABILITADA);
  };
  
  moduloActual.llamadaAjax=function(d){
		var referenciaModulo =this;
		
		$("#cmpFiltroEstacion").find("option:selected").val(moduloActual.obj.idEstacion);
		referenciaModulo.obj.cmpFiltroEstacion.val(moduloActual.obj.idEstacion);

//	    $("#cmpFiltroEstacion").val(moduloActual.obj.idEstacion).trigger('change');
//		referenciaModulo.obj.cmpFiltroEstacion.val(moduloActual.obj.idEstacion);
		  
	    var indiceOrdenamiento = d.order[0].column;
	    d.registrosxPagina =  d.length; 
	    d.inicioPagina = d.start; 
	    d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	    d.sentidoOrdenamiento=d.order[0].dir;
	    
	    d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
	    d.filtroEstacion = referenciaModulo.obj.cmpFiltroEstacion.val();
	    
	    var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
      var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
      var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
      d.filtroFechaInicio= fechaInicio;	
      d.filtroFechaFinal = fechaFinal;	 
	};
	
  moduloActual.resetearFormulario= function(){
	  var referenciaModulo= this;
	  referenciaModulo.obj.frmPrincipal[0].reset();
	  console.log("Verificador");
	  referenciaModulo.obj.verificadorFormulario.resetForm();
	  jQuery.each( this.obj, function( i, val ) {
	    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
	    	if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
	            referenciaModulo.obj[i].select2("val", null);
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
		      $("#cnt" + $(element).attr("id")).removeClass(validClass).addClass(errorClass);
		    },
		    unhighlight: function(element, errorClass, validClass) {
		      $("#cnt" + $(element).attr("id")).removeClass(errorClass).addClass(validClass);
		    },
		    errorPlacement: function(error, element) {
		      console.log("errorPlacement");
		      console.log(error);     
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
	
	moduloActual.iniciarCancelar=function(){
		  var referenciaModulo=this;
		  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
		  referenciaModulo.resetearFormulario();
		  referenciaModulo.obj.cntFormulario.hide();
		  referenciaModulo.obj.ocultaContenedorFormulario.show();
		  referenciaModulo.obj.cntVistaRegistro.hide();
		  referenciaModulo.obj.cntTabla.show();
		  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
		  //PARA PONERLE VALORES AL FILTRO ESTACION
		  $("#cmpFiltroEstacion").find("option:selected").val(moduloActual.obj.idEstacion);        
	      $("#cmpFiltroEstacion").val(moduloActual.obj.idEstacion).trigger('change');
		  referenciaModulo.obj.cmpFiltroEstacion.val(moduloActual.obj.idEstacion);

		};
		
	moduloActual.iniciarListado= function(mensaje){
		var referenciaModulo = this;
		try {
			referenciaModulo.listarRegistros();
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,mensaje);
			referenciaModulo.obj.cntFormulario.hide();	
			referenciaModulo.obj.cntTabla.show();
			//PARA PONERLE VALORES AL FILTRO ESTACION
			  $("#cmpFiltroEstacion").find("option:selected").val(moduloActual.obj.idEstacion);        
		      $("#cmpFiltroEstacion").val(moduloActual.obj.idEstacion).trigger('change');
			  referenciaModulo.obj.cmpFiltroEstacion.val(moduloActual.obj.idEstacion);
			
		} catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
		};
	};

  moduloActual.iniciarAgregar= function(){  
	var referenciaModulo=this; 
	try {
    	$.ajax({
	        type: constantes.PETICION_TIPO_GET,
	        url: './jornada/recuperar-ultimo-dia', 
	        contentType: referenciaModulo.TIPO_CONTENIDO, 
	        data: {
	        	idOperacion:referenciaModulo.filtroOperacion,
	        	filtroEstacion:referenciaModulo.obj.cmpFiltroEstacion.val(),
	        	filtroEstado : constantes.TIPO_JORNADA_ABIERTO,
		      },
		      success: function(respuesta) {
	          if (!respuesta.estado) {
	            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	          } else {            
	            if(respuesta.valor == null){
	            	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No existe una jornada activa para la operacion.");
	          	} else {	
	          		referenciaModulo.resetearFormulario();	          		
		            referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
		    	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
		    	    referenciaModulo.obj.cmpDiaOperativo.val(utilitario.formatearFecha(respuesta.valor));
		    	    referenciaModulo.obj.cmpOperacionCliente.val(referenciaModulo.obj.filtroOperacion.find("option:selected").attr('data-operacion-cliente'));
		    	    referenciaModulo.obj.cmpEstacion.val(moduloActual.obj.nombreEstacion);
		    	    referenciaModulo.obj.cntTabla.hide();
		    	    referenciaModulo.obj.cntVistaRegistro.hide();
		    	    referenciaModulo.obj.cntFormulario.show();
		    	    referenciaModulo.obj.ocultaContenedorFormulario.hide();  
		    	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.INICIAR_OPERACION);
	          	}
	          }
	          referenciaModulo.obj.ocultaContenedorFormulario.hide();
	        },
	        error: function(xhr,estado,error) {
	          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	        }
	      }); 
    	
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		};
	};
	
  moduloActual.llenarFormulario = function(registro){
	var referenciaModulo = this;  
    this.idRegistro= registro.id;
    
    referenciaModulo.obj.cmpDiaOperativo.val(utilitario.formatearFecha(registro.jornada.fechaOperativa));
    referenciaModulo.obj.cmpOperacionCliente.val(referenciaModulo.obj.filtroOperacion.find("option:selected").attr('data-operacion-cliente'));
    referenciaModulo.obj.cmpEstacion.val(referenciaModulo.obj.nombreEstacion);
    moduloActual.obj.idJornada = registro.idJornada;
    
    this.obj.cmpVolumen.val(registro.volumen);
    this.obj.cmpComentario.val(registro.comentario);  
    this.obj.cmpTipo.val(registro.tipoMovimiento);  
    this.obj.cmpClasificacion.val(registro.clasificacion);  
    
    var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.idTanqueOrigen);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.tanqueOrigen.descripcion);
    referenciaModulo.obj.cmpTanqueOrigen.empty().append(elemento1).val(registro.idTanqueOrigen).trigger('change');
    
    var elemento2=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,registro.idTanqueDestino);
    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.tanqueDestino.descripcion);
    referenciaModulo.obj.cmpTanqueDestino.empty().append(elemento2).val(registro.idTanqueDestino).trigger('change');      
  };

  moduloActual.llenarDetalles = function(registro){
	var referenciaModulo = this; 
    var txt="";
	this.idRegistro= registro.id;    
    this.obj.vistaId.text(registro.id);
    
    this.obj.vistaOperacionCliente.text(referenciaModulo.obj.filtroOperacion.find("option:selected").attr('data-operacion-cliente'));
    this.obj.vistaEstacion.text(referenciaModulo.obj.nombreEstacion);
    this.obj.vistaDiaOperativo.text(utilitario.formatearFecha(registro.jornada.fechaOperativa));
    if(registro.tipoMovimiento==1){
    	txt="ENTRADA";
    }else if(registro.tipoMovimiento==2){
    	txt="SALIDA";
    }
    this.obj.vistaTipo.text(txt);
    this.obj.vistaClasificacion.text(utilitario.formatearClasificacionOtroMovimiento(registro.clasificacion));
    this.obj.vistaTanqueOrigen.text(registro.tanqueOrigen.descripcion);
    this.obj.vistaTanqueDestino.text(registro.tanqueDestino.descripcion);
    this.obj.vistaVolumen.text(registro.volumen);
    this.obj.vistaComentario.text(registro.comentario);
    //-auditoria  
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
    this.obj.vistaIPCreacion.text(registro.ipCreacion);
    this.obj.vistaIPActualizacion.text(registro.ipActualizacion);    
  };
//PARA REGISTRAR
  moduloActual.recuperarValores = function(registro){    
    var referenciaModulo=this;
    var eRegistro = {};
    try {
	    eRegistro.id = parseInt(referenciaModulo.idRegistro);				
	    eRegistro.idJornada = parseInt(moduloActual.obj.idJornada);
	    eRegistro.fechaJornada = referenciaModulo.obj.cmpDiaOperativo.val();	    
	    eRegistro.numeroMovimiento = 1;															
	    eRegistro.tipoMovimiento = parseInt(referenciaModulo.obj.cmpTipo.val());
	    eRegistro.clasificacion = parseInt(referenciaModulo.obj.cmpClasificacion.val());
	    eRegistro.idTanqueOrigen = parseInt(referenciaModulo.obj.cmpTanqueOrigen.val());
	    eRegistro.idTanqueDestino = parseInt(referenciaModulo.obj.cmpTanqueDestino.val());
	    eRegistro.volumen = parseInt(referenciaModulo.obj.cmpVolumen.val());
	    eRegistro.comentario = referenciaModulo.obj.cmpComentario.val().toUpperCase(); 
	    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
	  
  };  
  
  moduloActual.validaTurnoAbierto = function(){
		var referenciaModulo = this;
		console.log("referenciaModulo.obj.moduloActual.obj.cmpFiltroEstacion.val() " + referenciaModulo.obj.cmpFiltroEstacion.val());
		referenciaModulo.obj.ocultaContenedorTabla.show();
		if(referenciaModulo.obj.cmpFiltroEstacion.val() > 0){
			//referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
			$.ajax({
			    type: constantes.PETICION_TIPO_GET,
			    url: './turno/obtieneUltimaJornada', 
			    contentType: referenciaModulo.TIPO_CONTENIDO, 
			    data: { filtroEstacion : referenciaModulo.obj.cmpFiltroEstacion.val(),
			        	filtroEstado : constantes.TIPO_JORNADA_ABIERTO},	 
			    success: function(respuesta) {
			    	if (!respuesta.estado) {
			    		console.log("respuesta.mensaje " + respuesta.mensaje);
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
			    		referenciaModulo.obj.ocultaContenedorTabla.hide();
			    	} else {
			    		var valor = respuesta.valor;
			    		moduloActual.obj.idJornada = parseInt(respuesta.valor);
			    		if(valor != 0){
			    			$.ajax({
			    			    type: constantes.PETICION_TIPO_GET,
			    			    url: './turno/listar', 
			    			    contentType: referenciaModulo.TIPO_CONTENIDO, 
			    			    data: {idJornada:moduloActual.obj.idJornada,
			    			    	   filtroEstado:constantes.TIPO_TURNO_ABIERTO},	
			    			    success: function(respuesta) {
			    			    	if (!respuesta.estado) {
			    			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
			    			    		referenciaModulo.obj.ocultaContenedorTabla.hide();
			    			    	} else {
			    			    		if(respuesta.contenido.carga==null || respuesta.contenido.carga.length==0){
			    			    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No existe un turno abierto para la  Estaci\u00f3n seleccionada.");
			    			    			referenciaModulo.obj.ocultaContenedorTabla.hide();
			    			    		} else {
			    			    			//inicia agregar otro movimiento
			    			    			referenciaModulo.iniciarAgregar();   			
			    			    		}
			    		    		}
			    			    	referenciaModulo.obj.ocultaContenedorTabla.hide();
			    			    },			    		    
			    			    	error: function(xhr,estado,error) {
			    			    	referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
			    		        	referenciaModulo.obj.ocultaContenedorTabla.hide();
			    			   }
			    			});
			    		} else {
			    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			    			referenciaModulo.obj.ocultaContenedorTabla.hide();
			    		}
		    		}
			    },			    		    
			    error: function(xhr,estado,error) {
		        referenciaModulo.mostrarErrorServidor(xhr,estado,error);  
		        referenciaModulo.obj.ocultaContenedorTabla.hide();
			    }
			});
		}else{
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Debe seleccionar la Estaci\u00f3n.");
			referenciaModulo.obj.ocultaContenedorTabla.hide();
		}
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
			      if (referenciaModulo.descripcionPermiso == 'CREAR_OTRO_MOVIMIENTO'){
			    	  moduloActual.validaTurnoAbierto();
//			    	  referenciaModulo.iniciarAgregar();
			      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_OTRO_MOVIMIENTO'){
			    	  referenciaModulo.iniciarModificar();
			      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_OTRO_MOVIMIENTO'){
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
	
  moduloActual.inicializar();
});

