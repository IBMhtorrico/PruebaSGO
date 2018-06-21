$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='recorrido';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
   moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
   moduloActual.columnasGrilla.push({ "data": 'plantaOrigen.descripcion'});//Target2
   moduloActual.columnasGrilla.push({ "data": 'plantaDestino.descripcion'});//Target3
   moduloActual.columnasGrilla.push({ "data": 'numeroDias'});//Target4
   moduloActual.columnasGrilla.push({ "data": 'estado'});//Target5

   moduloActual.definicionColumnas.push({
	  	"targets": 1,
	    "searchable": true,
	    "orderable": true,
	    "visible":false
	  });
	  moduloActual.definicionColumnas.push({
	  	"targets": 2,
	    "searchable": true,
	    "orderable": true,
	    "visible":true
	  });
	  moduloActual.definicionColumnas.push({
	  	"targets": 3,
	    "searchable": true,
	    "orderable": true,
	    "visible":true
	});
	  moduloActual.definicionColumnas.push({
	  	"targets": 4,
	    "searchable": true,
	    "orderable": true,
	    "visible":true
	});
	  moduloActual.definicionColumnas.push({
	  	"targets": 5,
		"orderable": true,
		"visible":true,
	    "render": utilitario.formatearEstado
	});

	moduloActual.reglasValidacionFormulario = {
		cmpIdPlantaOrigen : {
			required : true
		},
		cmpIdPlantaDestino : {
			required : true
		},
		cmpNumeroDias : {
			required: true,
			number: true,
			maxlength: 3
		},
//		cmpEstado : {
//			required : true
//		},
	};
  
	moduloActual.mensajesValidacionFormulario={
		cmpIdPlantaOrigen: {
			required : "El campo Planta Origen es obligatorio",
		},
		cmpIdPlantaDestino: {
			required : "El campo Planta Destino es obligatorio",
		},
		cmpNumeroDias: {
			required: "El campo es obligatorio",
			maxlength: "El campo debe contener 3 caracteres como m&aacute;ximo.",
			number: "El campo solo debe contener caracteres num&eacute;ricos"
		},
//		cmpEstado: {
//			required : "El campo Planta Origen es obligatorio",
//		},
	};

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
	this.obj.cmpNumeroDias=$("#cmpNumeroDias");
	this.obj.cmpEstado=$("#cmpEstado");
	
	//Para el desplegable de la planta origen
    this.obj.cmpIdPlantaOrigen=$("#cmpIdPlantaOrigen");
    this.obj.cmpIdPlantaOrigen.tipoControl="select2";
    this.obj.cmpSelect2PlantaOrigen=$("#cmpIdPlantaOrigen").select2({
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
    				return registro.text;
    			}		    	
  		        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		    	console.log("templateSelection");
  		        return registro.descripcion || registro.text;
  		    },
    		minimumInputLength: 3
      });
    
    //Para el desplegable de la planta destino
    this.obj.cmpIdPlantaDestino=$("#cmpIdPlantaDestino");
    this.obj.cmpIdPlantaDestino.tipoControl="select2";
    this.obj.cmpSelect2PlantaDestino=$("#cmpIdPlantaDestino").select2({
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
    				return registro.text;
    			}		    	
  		        return "<div class='select2-user-result'>" + registro.descripcion + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		    	console.log("templateSelection");
  		        return registro.descripcion || registro.text;
  		    },
    		minimumInputLength: 3
      });
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaIdPlantaOrigen=$("#vistaIdPlantaOrigen");
    this.obj.vistaIdPlantaDestino=$("#vistaIdPlantaDestino");
    this.obj.vistaNumeroDias=$("#vistaNumeroDias");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,5).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  
  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpNumeroDias.val(registro.numeroDias);
    this.obj.cmpEstado.val(registro.estado);
    
    var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.plantaOrigen.id);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.plantaOrigen.descripcion);
    this.obj.cmpIdPlantaOrigen.empty().append(elemento1).val(registro.plantaOrigen.id).trigger('change');
    
    var elemento2=constantes.PLANTILLA_OPCION_SELECTBOX;
    
    elemento2 = elemento2.replace(constantes.ID_OPCION_CONTENEDOR,registro.plantaDestino.id);
    elemento2 = elemento2.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.plantaDestino.descripcion);
    this.obj.cmpIdPlantaDestino.empty().append(elemento2).val(registro.plantaDestino.id).trigger('change');
  };

  moduloActual.llenarDetalles = function(registro){
	
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaIdPlantaOrigen.text(registro.plantaOrigen.descripcion);
    this.obj.vistaIdPlantaDestino.text(registro.plantaDestino.descripcion);
    this.obj.vistaNumeroDias.text(registro.numeroDias);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
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
    eRegistro.idPlantaOrigen = referenciaModulo.obj.cmpIdPlantaOrigen.val();
    eRegistro.idPlantaDestino = referenciaModulo.obj.cmpIdPlantaDestino.val();
    eRegistro.numeroDias = referenciaModulo.obj.cmpNumeroDias.val();
    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();

    console.log(eRegistro);
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});