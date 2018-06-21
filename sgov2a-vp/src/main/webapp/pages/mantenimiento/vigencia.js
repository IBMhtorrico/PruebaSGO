$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='vigencia';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;

  moduloActual.inicializarCampos= function(){
	//
	this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
	this.indiceFormulario=$("#indiceFormulario");
	this.registrosEliminar=$("#registrosEliminar");
	this.fechaActual=$("#fechaActual");
	//botones
	this.obj.btnFiltrar=$("#btnFiltrar");
	this.obj.btnAgregarDocumento=$("#btnAgregarDocumento");
	this.obj.cntIdConductor=$("#cntIdConductor");
	this.obj.cntIdCisterna=$("#cntIdCisterna");
	
	$("#cntIdConductor").removeClass('hidden');
	$("#cntIdCisterna").addClass('hidden');
	
	this.obj.btnConfirmarEliminarRegistro=$("#btnConfirmarEliminarRegistro");
	
	this.obj.btnConfirmarEliminarRegistro.on("click",function(){
	  //var referenciaModulo = this;
	  var eRegistro = {};
	  eRegistro.id = parseInt(moduloActual.registrosEliminar);
      moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	  try {
		  
	    $.ajax({
	    	type: constantes.PETICION_TIPO_POST,
		    url: './vigencia/eliminar', 
		    contentType: moduloActual.TIPO_CONTENIDO, 
		    data: JSON.stringify(eRegistro),
		    success: function(respuesta) {
		      if (!respuesta.estado) {
		    	  moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		      } else {		    				    			    		
		    	moduloActual.listarRegistros();
		    	moduloActual.obj.frmConfirmarModificarEstado.modal("hide");
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
  });
	
	//campos formulario
	this.obj.cmpEntidad=$("#cmpEntidad");
	this.obj.cmpEntidad.on('change', function(e){
		if($(this).val() == 1){
			$('#cmpIdCisterna').find("option:selected").val(0);
	    	$('#cmpIdCisterna').val(0).trigger('change');
	    	moduloActual.obj.cmpIdCisterna.val();
	    	
			$("#cntIdConductor").removeClass('hidden');
			$("#cntIdCisterna").addClass('hidden');
		} else {
			$('#cmpIdConductor').find("option:selected").val(0);
	    	$('#cmpIdConductor').val(0).trigger('change');
	    	moduloActual.obj.cmpIdConductor.val();
	    	
			$('#cntIdConductor').addClass('hidden');
			$("#cntIdCisterna").removeClass('hidden');
		}
	      e.preventDefault(); 
    });  

	this.obj.cmpPerteneceA=$("#cmpPerteneceA");
	this.obj.cmpIdConductor=$("#cmpIdConductor");
	this.obj.cmpIdConductor.tipoControl="select2";
	this.obj.cmpSelect2Conductor=$("#cmpIdConductor").select2({
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
  		      return { results: resultados}
  		    },
  		    cache: true,
  		  },
  		language: "es",
  		escapeMarkup: function (markup) { return markup; },
  		templateResult: function (registro) {
  			if (registro.loading) {
  				return "Buscando...";
  			}		    	
		        return "<div class='select2-user-result'>" + registro.nombreCompleto +" - " +  registro.brevete +"</div>";
		    },
		    templateSelection: function (registro) {
		    	try{
		    		if(registro.brevete != null){
		    			return registro.nombreCompleto +" - " +  registro.brevete || registro.text;
		    		}
		    		else{
		    			return registro.nombreCompleto || registro.text;
		    		}
		    	} catch(error){
		        	moduloActual.mostrarDepuracion(error.message);
		        };
		    },
    });

  	this.obj.cmpIdCisterna=$("#cmpIdCisterna");
	this.obj.cmpIdCisterna.tipoControl="select2";
	this.obj.cmpSelect2Cisterna=$("#cmpIdCisterna").select2({
	  ajax: {
	    url: "./cisterna/listar",
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
	      cache: true,
	  	},
		language: "es",
		escapeMarkup: function (markup) { return markup; },
		templateResult: function (registro) {
		if (registro.loading) {
			return "Buscando...";
		}		    	
	        return "<div class='select2-user-result'>" + registro.placaCisternaTracto +" - " +  registro.transportista.razonSocial +"</div>";
	    },
	    templateSelection: function (registro) {
	    	try{
	    		if(registro.idTransportista > 0){
	    			return registro.placaCisternaTracto +" - " +  registro.transportista.razonSocial || registro.text;
	    		}
	    		else{
	    			return registro.placaCisternaTracto|| registro.text;
	    		}
	    	} catch(error){
	        	moduloActual.mostrarDepuracion(error.message);
	        };
	        
	    },
	  });

	this.obj.GrupoVigencia = $('#GrupoVigencia').sheepIt({
	  separator: '',
	  allowRemoveLast: true,
	  allowRemoveCurrent: true,
	  allowRemoveAll: false,
	  allowAdd: true,
	  allowAddN: false,
	  minFormsCount: 0,
	  iniFormsCount: 1,
	  afterAdd: function(origen, formularioNuevo) {
		var cmpIdRegistro=$(formularioNuevo).find("input[elemento-grupo='identificador']");
	    var cmpElementoDocumento=$(formularioNuevo).find("select[elemento-grupo='documento']");
	    var cmpNumeroDocumento=$(formularioNuevo).find("input[elemento-grupo='numeroDocumento']");
	    var cmpFechaEmision=$(formularioNuevo).find("input[elemento-grupo='fechaEmision']");
	    var cmpFechaExpiracion=$(formularioNuevo).find("input[elemento-grupo='fechaExpiracion']");      
	    var cmpModifica=$(formularioNuevo).find("[elemento-grupo='botonModifica']");
	    var cmpElimina=$(formularioNuevo).find("[elemento-grupo='botonElimina']");

	    cmpIdRegistro = null;
	    //cmpNumeroDocumento = "";

	    cmpFechaEmision.inputmask(constantes.FORMATO_FECHA, 
	    {
	        "placeholder": constantes.FORMATO_FECHA,
	        onincomplete: function(){
	            $(this).val('');
	        }
	    });

	    cmpFechaExpiracion.inputmask(constantes.FORMATO_FECHA, 
	    {
	        "placeholder": constantes.FORMATO_FECHA,
	        onincomplete: function(){
	            $(this).val('');
	        }
	    });

	    cmpNumeroDocumento.on("change",function(){
	    	if(cmpNumeroDocumento.val().length > 20){
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La longitud del numero de documento no puede ser mayor de 20 caracteres");
	    		moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    	} else {
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "El registro fue recuperado con exito ");
	    		moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    	}
        }); 

	    cmpFechaExpiracion.on("change",function(){
	    	var fActual = utilitario.formatearStringToDate(utilitario.formatearFecha(moduloActual.fechaActual.val()));
	    	var fExpiracion = utilitario.formatearStringToDate(cmpFechaExpiracion.val());
	    	if(fExpiracion < fActual){
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de expiracion no puede ser inferior a la fecha actual");
	    		moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    	} else {
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
	    		moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    	}
	    	
	    	if(cmpFechaEmision.val().length > 0 ){
	    		var fEmision = utilitario.formatearStringToDate(cmpFechaEmision.val());
	    		if(fEmision > fExpiracion){
	    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de expiracion no puede ser inferior a la fecha de emision");
	    			moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		} else{
	    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
	    			moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    		}
	    	} 
        });  
	    
	    cmpFechaEmision.on("change",function(){
	    	var fActual = utilitario.formatearStringToDate(utilitario.formatearFecha(moduloActual.fechaActual.val()));
	    	var fEmision = utilitario.formatearStringToDate(cmpFechaEmision.val());
	    	if(fEmision > fActual){
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de emision no puede ser superior a la fecha actual");
	    		moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    	} else {
	    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
	    		moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    	}
	    	
	    	
	    	if(cmpFechaExpiracion.val().length > 0 ){
	    		var fExpiracion = utilitario.formatearStringToDate(cmpFechaExpiracion.val());
	    		if(fEmision > fExpiracion){
	    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de emision no puede ser superior a la fecha de expiracion");
	    			moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		} else{
	    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
	    			moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    		}
	    	} 
        }); 
	    
	    cmpFechaExpiracion.on("keypress",function(e){
	    	if (e.which == 13) { //valido que se presione la tecla enter
		    	var fActual = utilitario.formatearStringToDate(utilitario.formatearFecha(moduloActual.fechaActual.val()));
		    	var fExpiracion = utilitario.formatearStringToDate(cmpFechaExpiracion.val());
		    	if(fExpiracion < fActual){
		    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de expiracion no puede ser inferior a la fecha actual");
		    		moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    	} else {
		    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
		    		moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    	}
		    	
		    	if(cmpFechaEmision.val().length > 0 ){
		    		var fEmision = utilitario.formatearStringToDate(cmpFechaEmision.val());
		    		if(fEmision > fExpiracion){
		    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de expiracion no puede ser inferior a la fecha de emision");
		    			moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
			    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		} else{
		    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
		    			moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
			    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		}
		    	}
	    	}
        });  
	    
	    cmpFechaEmision.on("keypress",function(e){
	    	if (e.which == 13) { //valido que se presione la tecla enter
		    	var fActual = utilitario.formatearStringToDate(utilitario.formatearFecha(moduloActual.fechaActual.val()));
		    	var fEmision = utilitario.formatearStringToDate(cmpFechaEmision.val());
		    	if(fEmision > fActual){
		    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de emision no puede ser superior a la fecha actual");
		    		moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    	} else {
		    		moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
		    		moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    	}
		    	
		    	if(cmpFechaExpiracion.val().length > 0 ){
		    		var fExpiracion = utilitario.formatearStringToDate(cmpFechaExpiracion.val());
		    		if(fEmision > fExpiracion){
		    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Las fecha de emision no puede ser superior a la fecha de expiracion");
		    			moduloActual.obj.btnGuardar.addClass(constantes.CSS_CLASE_DESHABILITADA);
			    		moduloActual.obj.btnAgregarDocumento.addClass(constantes.CSS_CLASE_DESHABILITADA);
		    		} else{
		    			moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Las fechas son validas");
		    			moduloActual.obj.btnGuardar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
			    		moduloActual.obj.btnAgregarDocumento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    		}
		    	} 
	    	}
        }); 
	    
	    cmpElementoDocumento.tipoControl="select2";
        moduloActual.obj.cmpSelect2Documento=$(formularioNuevo).find("select[elemento-grupo='documento']").select2({
    	  ajax: {
    		    url: "./documento/listar",
    		    dataType: 'json',
    		    delay: 250,
    		    "data": function (parametros) {
    		    	try{
    			      return {
    			        page: parametros.page,
    			        paginacion:0,
    			        filtroPerteneceA : moduloActual.obj.cmpEntidad.val()
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
    	      return "<div class='select2-user-result'>" + registro.nombreDocumento + "</div>";
    	    },
    	    "templateSelection": function (registro) {
               return registro.nombreDocumento || registro.text;
    	    },
      });
	    
     cmpModifica.on("click", function(){
	    try{
	    	moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);
	   	    var fila = moduloActual.obj.GrupoVigencia.getForm(moduloActual.indiceFormulario);  
  			fila.find("select[elemento-grupo='documento']").prop('disabled', false);
			fila.find("input[elemento-grupo='numeroDocumento']").prop('disabled', false);
			fila.find("input[elemento-grupo='fechaEmision']").prop('disabled', false);
			fila.find("input[elemento-grupo='fechaExpiracion']").prop('disabled', false);
	    } catch(error){
	       console.log(error.message);
	    }
	  });
        
	 cmpElimina.on("click", function(){
	    try{	    	
      	  moduloActual.indiceFormulario = ($(formularioNuevo).attr('id')).substring(22);              	  
          var numeroFormularios = moduloActual.obj.GrupoVigencia.getForms().length;
          for(var contador = 0; contador < numeroFormularios; contador++){                     
              var formulario = moduloActual.obj.GrupoVigencia.getForm(contador); 
              var indice=(formulario.attr('id')).substring(22);//indice html
              if(moduloActual.indiceFormulario == indice){                    	  
            	  var fila = moduloActual.obj.GrupoVigencia.getForm(contador);                    	  
            	  if(fila.find("input[elemento-grupo='identificador']").val() > 0){
            		 moduloActual.registrosEliminar = fila.find("input[elemento-grupo='identificador']").val();
      		    	 moduloActual.obj.frmConfirmarModificarEstado.modal("show");
            	  }else{
            		  moduloActual.obj.GrupoVigencia.removeForm(contador);
            	  }                    	  
            	 break; 
              }
              
          }	    	
	    } catch(error) {
	       console.log(error.message);
	    }
	  });
	}
	});
	
	moduloActual.obj.btnAgregarDocumento.on("click",function(){
      try {
        moduloActual.obj.GrupoVigencia.addForm();
      } catch(error){
    	moduloActual.mostrarDepuracion(error.message);
      };
	});
  };
  

moduloActual.inicializar=function(){
	this.configurarAjax();
  this.inicializarControlesGenericos();
  this.inicializarCampos();
  this.inicializarFormularioPrincipal();
};

moduloActual.listarRegistros = function(){
	var ref=this;
	  try {
		  if(ref.obj.cmpIdConductor.val() > 0 || ref.obj.cmpIdCisterna.val() > 0 ){
		  ref.obj.cntTabla.show();
		  ref.obj.cntFormulario.show();
		  	if(ref.obj.cmpIdConductor.val() > 0){
		  		moduloActual.obj.cmpPerteneceA = ref.obj.cmpIdConductor.val();
		  	} else {
		  		moduloActual.obj.cmpPerteneceA = ref.obj.cmpIdCisterna.val();
		  	};
		  	//ajax para recuperar los registros
		  	moduloActual.recuperarRegistro();
		  } else {
			  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Debe seleccionar una descripcion");
		  };
	  } catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	  };
};
	
moduloActual.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorFormulario.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_LISTAR, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {
	    	filtroPerteneceA: moduloActual.obj.cmpPerteneceA,
	    	filtroIdEntidad: moduloActual.obj.cmpEntidad.val()
	    	},	
	    success: function(respuesta) {
	    	console.log(respuesta);
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		referenciaModulo.llenarFormulario(respuesta.contenido);
			}
	    },			    		    
	    error: function(xhr,estado,error) {
	    referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
	    }
	});
};  

moduloActual.llenarFormulario = function(registros){
	console.log("Entra en llenarFormulario");
  var referenciaModulo = this;
  if (registros.carga != null){
	var numeroRegistros = registros.carga.length;
	

	referenciaModulo.obj.GrupoVigencia.removeAllForms();
    for(var contador=0; contador < numeroRegistros; contador++){
      referenciaModulo.obj.GrupoVigencia.addForm();
      var formulario= referenciaModulo.obj.GrupoVigencia.getForm(contador);
      formulario.find("input[elemento-grupo='identificador']").val(registros.carga[contador].id);
      
      var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
      elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registros.carga[contador].idDocumento);
      elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registros.carga[contador].documento.nombreDocumento);
      formulario.find("select[elemento-grupo='documento']").empty().append(elemento1).val(registros.carga[contador].idDocumento).trigger('change');
      formulario.find("input[elemento-grupo='numeroDocumento']").val(registros.carga[contador].numeroDocumento);
      formulario.find("input[elemento-grupo='fechaEmision']").val(utilitario.formatearFecha(registros.carga[contador].fechaEmision));
      formulario.find("input[elemento-grupo='fechaExpiracion']").val(utilitario.formatearFecha(registros.carga[contador].fechaExpiracion));  

      formulario.find("select[elemento-grupo='documento']").prop('disabled', true);
      formulario.find("input[elemento-grupo='numeroDocumento']").prop('disabled', true);
      formulario.find("input[elemento-grupo='fechaEmision']").prop('disabled', true);
      formulario.find("input[elemento-grupo='fechaExpiracion']").prop('disabled', true);

    } 
  }
  referenciaModulo.obj.ocultaContenedorFormulario.hide();
};

moduloActual.iniciarGuardar = function(){
  var referenciaModulo=this;
  try {
	  referenciaModulo.obj.ocultaContenedorFormulario.show();
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	  moduloActual.recuperarValores();
	  moduloActual.listarRegistros();
	  moduloActual.recuperarRegistro();
  	  moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
	  referenciaModulo.obj.ocultaContenedorFormulario.hide();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};

moduloActual.guardarRegistro= function(eRegistro){
	var referenciaModulo = this;
	try {
		$.ajax({
			type: constantes.PETICION_TIPO_POST,
			url: referenciaModulo.URL_GUARDAR, 
			contentType: referenciaModulo.TIPO_CONTENIDO, 
			data: JSON.stringify(eRegistro),	
			success: function(respuesta) {
	        if (!respuesta.estado) {
	          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	          //referenciaModulo.obj.ocultaContenedorFormulario.hide();
	        } else {
	        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
	        	referenciaModulo.listarRegistros();
	        } 
	      },			    		    
	      error: function() {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	      }
		});
	} catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	};
};
	
  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
	    var numeroFormularios = referenciaModulo.obj.GrupoVigencia.getForms().length;
	    for(var contador = 0; contador < numeroFormularios; contador++){
	        var formulario 				= referenciaModulo.obj.GrupoVigencia.getForm(contador);
	        var id 						= formulario.find("input[elemento-grupo='identificador']");
	        var cmpElementoDocumento	= formulario.find("select[elemento-grupo='documento']");
		    var cmpNumeroDocumento		= formulario.find("input[elemento-grupo='numeroDocumento']");
		    var cmpFechaEmision			= formulario.find("input[elemento-grupo='fechaEmision']");
		    var cmpFechaExpiracion		= formulario.find("input[elemento-grupo='fechaExpiracion']");  
		    
		    if((cmpElementoDocumento.val().length > 0) && (cmpNumeroDocumento.val().length > 0) && (cmpFechaEmision.val().length > 0) && (cmpFechaExpiracion.val().length > 0)){
			    eRegistro.id				= id.val();
			    eRegistro.idDocumento		= parseInt(cmpElementoDocumento.val());
			    eRegistro.numeroDocumento	= cmpNumeroDocumento.val().toUpperCase();
			    eRegistro.fechaEmision 		= utilitario.formatearStringToDate(cmpFechaEmision.val());
			    eRegistro.fechaExpiracion	= utilitario.formatearStringToDate(cmpFechaExpiracion.val());
			    eRegistro.idEntidad 		= parseInt(referenciaModulo.obj.cmpEntidad.val());
			    eRegistro.perteneceA		= parseInt(moduloActual.obj.cmpPerteneceA);
			    moduloActual.guardarRegistro(eRegistro);
		    } else{
		    	console.log("no guarda porque algun dato se encuentra vacío");
		    }

	      }
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});