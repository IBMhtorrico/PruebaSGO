$(document).ready(function(){
  var moduloActual = new moduloBaseAutorizacion();
  
  moduloActual.urlBase='autorizacion';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crearAutorizaciones';
  moduloActual.URL_RECUPERAR_POR_CODIGO = moduloActual.urlBase + '/recuperarPorCodigoInterno';
  moduloActual.URL_GUARDAR_AUTORIZACION_EJECUTADA = moduloActual.urlBase + '/crearAutorizacionEjecutada';
  moduloActual.URL_RECUPERAR_AUTORIZACION = moduloActual.urlBase + '/recuperarAutorizacion';
  moduloActual.URL_RECUPERAR_POR_AUTORIZACION_COMPLETA = moduloActual.urlBase + '/recuperarPorAutorizacion';
  
  moduloActual.reglasValidacionFormulario={
	cmpCodigo: {
		required: true,
		maxlength: 16
	},
	cmpConfirmaCodigo: {
		required: true,
		maxlength: 64
	},
	cmpVigenciaDesde: {
		required: true
	},
	cmpVigenciaHasta: {
		required: true
	}
  };
	  
  moduloActual.mensajesValidacionFormulario={
    cmpCodigo: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 16 caracteres como m&aacute;ximo."
	},
	cmpConfirmaCodigo: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 64 caracteres como m&aacute;ximo."
	},
	cmpVigenciaDesde: {
		required: "El campo es obligatorio",
	},
	cmpVigenciaHasta: {
		required: "El campo es obligatorio",
	}
  };

  moduloActual.inicializarCampos= function(){
	this.obj.btnGuardarAutorizacion =$('#btnGuardarAutorizacion');
	this.obj.btnCancelarAutorizacion =$('#btnCancelarAutorizacion');
	this.obj.frmValidarCodigo =$("#frmValidarCodigo");
	this.obj.bandaInformacionVentana =$("#bandaInformacionVentana");
	this.obj.frmValidarAutorizacion=$("#frmValidarAutorizacion");
	this.obj.ocultaContenedorValidarAutorizacion=$("#ocultaContenedorValidarAutorizacion");
	this.obj.btnGuardarValidarAutorizacion=$("#btnGuardarValidarAutorizacion");
	this.obj.btnCancelarValidarAutorizacion=$("#btnCancelarValidarAutorizacion");
	this.idAutorizacion =$("#idAutorizacion");
	this.idAutorizador =$("#idAutorizador");
	this.vigenteDesde =$("#vigenteDesde");
	this.vigenteHasta =$("#vigenteHasta");
	this.codigoAutorizacion =$("#codigoAutorizacion");
	
	this.idAutorizacion=-1;

	this.obj.cntAutorizacion =$('#cntAutorizacion');	
	
	this.obj.btnLlamaPoppupAutorizacion=$("#btnLlamaPoppupAutorizacion");
	this.obj.btnCerrarValidarCodigo=$("#btnCerrarValidarCodigo");
	
	this.obj.idUsuario = $("#idUsuario");
	this.obj.usuario = $("#usuario");
	this.obj.RolUsuario = $("#RolUsuario");
	this.obj.OperacionUsuario = $("#OperacionUsuario");
	this.entidad = $("#entidad");
	
	this.obj.btnGuardarAutorizacion.on("click",function(){
	  var referenciaModulo=this;
	  try {
		  moduloActual.obj.cntAutorizacion.show();
		  moduloActual.guardarAutorizacion();
	  } catch(error){
		console.log(error.message);
	  };
	});

	this.obj.btnCancelarAutorizacion.on("click",function(){
	  var referenciaModulo=this;
	  try {
		  moduloActual.resetearFormulario();
		  moduloActual.obj.cntAutorizacion.show();
	  } catch(error){
		console.log(error.message);
	  };
	});

	moduloActual.obj.btnLlamaPoppupAutorizacion.on("click",function(){
	  var referenciaModulo=this;
	  try {
		  console.log("llamada al poppup.");
		  moduloActual.obj.ocultaContenedorTabla.hide(); 
		  moduloActual.recuperarAutorizacionesXcodigoInterno();
		  moduloActual.obj.frmValidarAutorizacion.modal("show");
	  } catch(error){
			console.log(error.message);
		  };
	});
	
	moduloActual.obj.btnCerrarValidarCodigo.on("click",function(){
		  var referenciaModulo=this;
		  try {
			  moduloActual.obj.frmValidarCodigo.modal("hide");
		  } catch(error){
				console.log(error.message);
			  };
		});
	
	
	moduloActual.obj.btnGuardarValidarAutorizacion.on("click",function(){
		var referenciaModulo=this;
	  try {
		  moduloActual.guardarValidacionAutorizacion();
	  } catch(error){
			console.log(error.message);
		  };
	});
	
	moduloActual.obj.btnCancelarValidarAutorizacion.on("click",function(){
		var referenciaModulo=this;
	  try {
		  moduloActual.obj.frmValidarAutorizacion.modal("hide");
	  } catch(error){
			console.log(error.message);
		  };
	});

	//Campos de formulario
    this.obj.cmpCodigo=$("#cmpCodigo");
    this.obj.cmpConfirmaCodigo=$("#cmpConfirmaCodigo");
    this.obj.cmpVigenciaDesde=$("#cmpVigenciaDesde");
    this.obj.cmpVigenciaDesde.inputmask(constantes.FORMATO_FECHA, 
    { 
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    this.obj.cmpVigenciaHasta=$("#cmpVigenciaHasta");
    this.obj.cmpVigenciaHasta.inputmask(constantes.FORMATO_FECHA, 
    { 
        "placeholder": constantes.FORMATO_FECHA,
        onincomplete: function(){
            $(this).val('');
        }
    });
    moduloActual.recuperarAutorizaciones();
    
  //Campos de formularioValidarAutorizaion
    this.obj.cmpDescAutorizacion=$("#cmpDescAutorizacion");
    this.obj.cmpAprobador=$("#cmpAprobador");
   /* this.obj.cmpSelect2Aprobador=$("#cmpAprobador").select2({
  	  ajax: {
  		    url: "./autorizacion/recuperarPorAutorizacion",
  		    dataType: 'json',
  		    delay: 250,
  		    data: function (parametros) {
  		    	console.log("entra en cmpSelect2Aprobador");
  		    	console.log(moduloActual.idAutorizacion);
  		      return {
  		    	txtFiltro: moduloActual.idAutorizacion, // search term
  		        page: parametros.page,
  		        paginacion:0
  		      };
  		    },
  		    processResults: function (respuesta, pagina) {
  		    	console.log("processResults");
  		    	var resultados= respuesta.contenido.carga;
  		      return { results: resultados};
  		    },
  		    cache: true
  		  },
  		language: "es",
  		escapeMarkup: function (markup) { return markup; },
  		templateResult: function (registro) {
  			console.log("templateResult");
  			if (registro.loading) {
  				return registro.text;
  			}
		        return "<div class='select2-user-result'>" + registro.identidad + "</div>";
		    },
	    templateSelection: function (registro) {
	    	console.log("templateSelection");
	    	console.log(registro.vigenteHasta);
	    	console.log(registro.vigenteHasta);
	    	console.log(registro.codigoAutorizacion);
	    	console.log(registro.idAutorizacion);
	    	console.log(registro.idUsuario);
	    	
	        return registro.identidad || registro.text;
	    },
    });*/
    
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

  moduloActual.recuperarAutorizaciones = function(){
	  var referenciaModulo = this;
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
//	  referenciaModulo.protegeFormulario(true);
		console.log("recuperarAutorizaciones");
		$.ajax({
		    type: "GET",
		    url: referenciaModulo.URL_RECUPERAR, 
		    contentType: "application/json", 
		    data: {ID:referenciaModulo.idRegistro},	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    	} 	else {		 
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		moduloActual.llenarAutorizaciones(respuesta.contenido.carga[0]);
		    		//referenciaModulo.protegeFormulario(false);
		    		moduloActual.resetearFormulario();
		    		referenciaModulo.obj.cntAutorizacion.show();
	    		}
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    	referenciaModulo.protegeFormulario(false);
		    }
		});
  };

  moduloActual.llenarAutorizaciones = function(registro){
	  console.log(registro);
	  moduloActual.idRegistro = registro.id;
	  moduloActual.entidad = registro;
	  var usuario = registro.usuario[0];
	  moduloActual.obj.idUsuario = usuario.id;
	  moduloActual.obj.usuario.text(usuario.identidad);
	  moduloActual.obj.RolUsuario.text(usuario.rol.nombre);
	  moduloActual.obj.OperacionUsuario.text(usuario.operacion.nombre);

	  //var indice= registro.permisos.length;
	  var grilla = $('#tablaAutorizaciones');
	  $('#tablaAutorizaciones').html("");
	  fila = '<thead><tr><th> # </th><th> Autorizaci&oacute;n</th></tr></thead>';
	  grilla.append(fila);
	
	  var indice = registro.autorizacion.length;
	  if(indice == 0){
		  moduloActual.obj.btnGuardarAutorizacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	  }
	  else{
		  moduloActual.obj.btnGuardarAutorizacion.remove(constantes.CSS_CLASE_DESHABILITADA);
	  }
	  
	  for(var k = 0; k < indice; k++){ 	
	      fila  = '<tr><td class="text-center">' + (k+1) + '</td>' + // #
	              '    <td>' +registro.autorizacion[k].nombre    + '</td></tr>'; // descripcion de la autorizacion
	  grilla.append(fila);
	}
  };
  
  moduloActual.recuperarValores = function(registro){
	var referenciaModulo = this;
	var eRegistro = {};
	
	try {
		console.log(moduloActual.entidad);
	    eRegistro.id = parseInt(moduloActual.entidad.id);
	    eRegistro.idUsuario = parseInt(moduloActual.entidad.idUsuario);
	    eRegistro.codigoAutorizacion = referenciaModulo.obj.cmpCodigo.val();
	    eRegistro.vigenteDesde = utilitario.formatearStringToDate(referenciaModulo.obj.cmpVigenciaDesde.val());
	    eRegistro.vigenteHasta = utilitario.formatearStringToDate(referenciaModulo.obj.cmpVigenciaHasta.val());
	    eRegistro.autorizacion=[];

	    var cantidadAutorizaciones = moduloActual.entidad.autorizacion.length;
	    for(var contador = 0; contador < cantidadAutorizaciones; contador++){
	    	var eAutorizacion = {};
	    	eAutorizacion.id = moduloActual.entidad.autorizacion[contador].id;
	    	console.log(moduloActual.entidad.autorizacion[contador].id);
	    	console.log(moduloActual.entidad.autorizacion[contador].nombre);
	    	eAutorizacion.nombre = moduloActual.entidad.autorizacion[contador].nombre;
	    	eRegistro.autorizacion.push(eAutorizacion);
	    }
	    
		console.log(eRegistro);    
	}  catch(error){
	  console.log(error.message);
	}
	return eRegistro;
  };
 
  moduloActual.guardarAutorizacion= function(){
	  var referenciaModulo = this;
		//Ocultar alertas de mensaje
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		console.log("empieza la validacion");
		console.log(referenciaModulo.obj.cmpCodigo.val());
		console.log(referenciaModulo.obj.cmpConfirmaCodigo.val());
		
		if (referenciaModulo.obj.frmPrincipal.valid()){
			//moduloBaseAutorizacion.showLoading();
			if(referenciaModulo.obj.cmpCodigo.val() != referenciaModulo.obj.cmpConfirmaCodigo.val()){
			  	console.log("No son iguales");
				this.obj.frmValidarCodigo.modal("show");
			}
			else{
				referenciaModulo.protegeFormulario(true);
				var eRegistro = referenciaModulo.recuperarValores();
				$.ajax({
				    type: "POST",
				    url: referenciaModulo.URL_ACTUALIZAR, 
				    contentType: "application/json", 
				    data: JSON.stringify(eRegistro),	
				    success: function(respuesta) {
				    	if (!respuesta.estado) {
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				    		referenciaModulo.protegeFormulario(false);
				    	} 	else {		    				    			    		
				    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
				    		moduloActual.resetearFormulario();
				    		referenciaModulo.protegeFormulario(false);
			    		}
				    },			    		    
				    error: function() {
				    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
				    	referenciaModulo.protegeFormulario(false);
				    }
				});
			}
		} else {
			console.log("No valido");
			//var containers=moduloBaseAutorizacion.obj.frmPrincipal.find('.control-group.control-element.error');
			//$(window).scrollTop($(containers[0]).offset().top-70);	
		}
  };	
	
//-----------------------FORMULARIO INGRESO DE AUTORIZACION --------------------------
  moduloActual.recuperarAutorizacionesXcodigoInterno = function(){
	  //DATO QUE DEBE RECIBIR DEL MODULO CORRESPONDIENTE
	  var codInterno = 'ACIFR';

	  var referenciaModulo = this;
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
//	  referenciaModulo.protegeFormulario(true);
		console.log("recuperarAutorizacionesXcodigoInterno");
		$.ajax({
		    type: "GET",
		    url: referenciaModulo.URL_RECUPERAR_POR_CODIGO, 
		    contentType: "application/json",
		    data: {codigoInterno:codInterno},	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    	} 	else {		 
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		
		    		moduloActual.llenarValidarAutorizacion(respuesta.contenido.carga[0]);
		    		//referenciaModulo.protegeFormulario(false);
		    		//moduloActual.resetearFormulario();
		    		//referenciaModulo.obj.cntAutorizacion.show();
	    		}
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    	referenciaModulo.protegeFormulario(false);
		    }
		});
  };
  
  //TODO
  moduloActual.llenarValidarAutorizacion = function(registro){
	  var referenciaModulo = this;
	  console.log(registro);
	  moduloActual.idAutorizacion = registro.autorizacion[0].id;
	  moduloActual.obj.cmpDescAutorizacion.val(registro.autorizacion[0].nombre);
    
	  console.log("Entra en llenarValidarAutorizacion");
	  console.log(moduloActual.idAutorizacion);
	  //var autorizacion = moduloActual.idAutorizacion;
    //TODO
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
	    		    	console.log("processResults");
	    		    	var resultados= respuesta.contenido.carga;
	    		      return { results: resultados};
	    		    },
	    		    cache: true
	    		  },
	    		language: "es",
	    		escapeMarkup: function (markup) {
	    			return markup; 
	    			},
	    		templateResult: function (registro) {
	    			console.log("templateResult");
	    			if (registro.loading) {
	    				return registro.text;
	    			}
	  		        return "<div class='select2-user-result'>" + registro.identidad + "</div>";
	  		    },
	  		    templateSelection: function (registro) {
	  		    	console.log(registro.vigenteHasta);
		  	    	console.log(registro.codigoAutorizacion);
		  	    	
		  	    		//moduloActual.vigenteDesde = utilitario.formatearFecha(registro.vigenteDesde);
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
		eRegistro.vigenteDesde = new Date(moduloActual.vigenteDesde);
		eRegistro.vigenteHasta = utilitario.formatearStringToDate(moduloActual.obj.cmpVigenciaHastaValidacion.val());
		eRegistro.descripcion = moduloActual.obj.cmpJustificacion.val();
		//TODO
		//Estos datos son propios de la pantalla
		eRegistro.tipoRegistro = parseInt(2);
		eRegistro.idRegistro = parseInt(1);

		console.log(eRegistro);    
	}  catch(error){
	  console.log(error.message);
	}
	return eRegistro;
  };
		 
  moduloActual.guardarValidacionAutorizacion= function(){
	  var referenciaModulo = this;
		//Ocultar alertas de mensaje
	 // referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		console.log("empieza a guardar la validacion.");
		
		var caracteres = referenciaModulo.obj.cmpJustificacion.val().length;
		if((caracteres < 60) || (caracteres > 3000)) {
			referenciaModulo.bandaInformacionVentana(constantes.TIPO_MENSAJE_ERROR, "La longitud de la justificacion debe ser mayor a 60 caracteres.");
		} else{
			/*if(moduloActual.codigoAutorizacion != referenciaModulo.obj.cmpCodigoValidacion.val()){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El código de autorización es incorrecto.");
			}*/
			//else{
				//if (referenciaModulo.obj.frmPrincipal.valid()){
					//moduloBaseAutorizacion.showLoading();
						var eRegistro = referenciaModulo.recuperarValoresValidacionAutorizacion();
						$.ajax({
						    type: "POST",
						    url: referenciaModulo.URL_GUARDAR_AUTORIZACION_EJECUTADA, 
						    contentType: "application/json", 
						    data: JSON.stringify(eRegistro),	
						    success: function(respuesta) {
						    	if (!respuesta.estado) {
						    		moduloActual.obj.cntInformacion.show();
						    		moduloActual.obj.bandaInformacionVentana.addClass(constantes.CLASE_MENSAJE_ERROR );
						    		moduloActual.obj.bandaInformacionVentana.text("Hubo un error en la petici&oacute;n.");
						    		moduloActual.obj.frmValidarAutorizacion.modal("show");
						    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
						    		
						    	} 	else {		    		
						    		moduloActual.obj.bandaInformacionVentana.addClass(constantes.CLASE_MENSAJE_EXITO );
						    		moduloActual.obj.bandaInformacionVentana.text("El registro se ha guardado correctamente.");
						    		moduloActual.obj.frmValidarAutorizacion.modal("hide");
						    		
						    	//	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
						    		moduloActual.obj.frmValidarAutorizacion.modal("hide");
						    		//referenciaModulo.obj.frmValidarAutorizacion[0].reset();
						    		//referenciaModulo.obj.select2("val", null);
						    		//referenciaModulo.protegeFormulario(false);
					    		}
						    },			    		    
						    error: function() {
						    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
						    }
						});
		
		}
	
  };	
	
  moduloActual.inicializar();
});
