$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='enlace';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
   moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
   moduloActual.columnasGrilla.push({ "data": 'urlCompleta'});//Target2
   moduloActual.columnasGrilla.push({ "data": 'urlRelativa'});//Target3
   moduloActual.columnasGrilla.push({ "data": 'orden'});//Target4
   moduloActual.columnasGrilla.push({ "data": 'padre'});//Target5
   moduloActual.columnasGrilla.push({ "data": 'tipo'});//Target6
   moduloActual.columnasGrilla.push({ "data": 'entidadPermiso.nombre'});//Target7

   moduloActual.definicionColumnas.push({
	   "targets" : 1,
		"searchable" : true,
		"orderable" : true,
		"visible" : false
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
	  	"searchable": true,
	    "orderable": true,
	    "visible":true
	});
	moduloActual.definicionColumnas.push({
	  	"targets": 6,
	  	"searchable": true,
	    "orderable": true,
	    "visible":true
	});
	moduloActual.definicionColumnas.push({
	  	"targets": 7,
	  	"searchable": true,
	    "orderable": true,
	    "visible":true
	});

	moduloActual.reglasValidacionFormulario={
		cmpTitulo: {
			required: true,
			maxlength: 80
		},
		cmpUrlCompleta: {
			required: true,
			maxlength: 100
		},
		cmpUrlRelativa: {
			required: true,
			maxlength: 100
		},
		cmpOrden: {
			required: true,
			number: true,
			maxlength: 4
		},

		cmpPadre: {
			required: true,
			number: true,
			maxlength: 4
			},
		cmpTipo: {
			required: true,
			number: true,
			maxlength: 1
			},
		cmpPermiso: "required",
  };

  moduloActual.mensajesValidacionFormulario={
	cmpTitulo: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 80 caracteres como maximo."
	},
	cmpUrlCompleta: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 100 caracteres como maximo."
	},
	cmpUrlRelativa: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 100 caracteres como maximo."
	},
	cmpOrden: {
		required: "El campo es obligatorio",
		number: "El campo solo debe contener caracteres numericos",
		maxlength: "El campo debe contener 4 caracteres como maximo."
	},
	cmpPadre: {
		required: "El campo es obligatorio",
		number: "El campo solo debe contener caracteres numericos",
			maxlength: "El campo debe contener 4 caracteres como maximo."
	},
	cmpTipo: {
		required:"El campo es obligatorio",
		number: "El campo solo debe contener caracteres numericos",
			maxlength: "El campo debe contener 1 caracter como maximo."
	},
	cmpPermiso: "El campo es obligatorio",
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpTitulo=$("#cmpTitulo");
    this.obj.cmpUrlCompleta=$("#cmpUrlCompleta");
    this.obj.cmpUrlRelativa=$("#cmpUrlRelativa");
    this.obj.cmpOrden=$("#cmpOrden");
    this.obj.cmpOrden.inputmask('integer');
    this.obj.cmpOrden.css("text-align", "left");
    this.obj.cmpPadre=$("#cmpPadre");
    this.obj.cmpPadre.inputmask('integer');
    this.obj.cmpPadre.css("text-align", "left");
    this.obj.cmpTipo=$("#cmpTipo");
    this.obj.cmpTipo.inputmask('integer');
    this.obj.cmpTipo.css("text-align", "left");
        
    this.obj.cmpPermiso=$("#cmpPermiso");
    this.obj.cmpPermiso.tipoControl="select2";
    this.obj.cmpSelect2Permiso=$("#cmpPermiso").select2({
    	  ajax: {
    		    url: "./permiso/listar",
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
  		        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
  		    },
  		    templateSelection: function (registro) {
  		    	console.log("templateSelection");
  		        return registro.nombre || registro.text;
  		    },
    		minimumInputLength: 3
      });
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaTitulo=$("#vistaTitulo");
    this.obj.vistaUrlCompleta=$("#vistaUrlCompleta");
    this.obj.vistaUrlRelativa=$("#vistaUrlRelativa");
    this.obj.vistaOrden=$("#vistaOrden");
    this.obj.vistaPadre=$("#vistaPadre");
    this.obj.vistaTipo=$("#vistaTipo");
    this.obj.vistaPermiso=$("#vistaPermiso");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    
   
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpTitulo.val(registro.titulo);
    this.obj.cmpUrlCompleta.val(registro.urlCompleta);
    this.obj.cmpUrlRelativa.val(registro.urlRelativa);
    this.obj.cmpOrden.val(registro.orden);
    this.obj.cmpPadre.val(registro.padre);
    this.obj.cmpTipo.val(registro.tipo);

    var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.permiso);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.entidadPermiso.nombre);
    this.obj.cmpPermiso.empty().append(elemento1).val(registro.permiso).trigger('change');
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaTitulo.text(registro.titulo);
    this.obj.vistaUrlCompleta.text(registro.urlCompleta);
    this.obj.vistaUrlRelativa.text(registro.urlRelativa);
    this.obj.vistaOrden.text(registro.orden);
    this.obj.vistaPadre.text(registro.padre);
    this.obj.vistaTipo.text(registro.tipo);
    this.obj.vistaPermiso.text(registro.entidadPermiso.nombre);
    this.obj.vistaCreadoEl.text(registro.fechaCreacion);
    this.obj.vistaCreadoPor.text(registro.usuarioCreacion);
    this.obj.vistaActualizadoEl.text(registro.fechaActualizacion);    
    this.obj.vistaActualizadoPor.text(registro.usuarioActualizacion);
  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
    try {
    eRegistro.id = parseInt(referenciaModulo.idRegistro);
    eRegistro.titulo = referenciaModulo.obj.cmpTitulo.val();
    eRegistro.urlCompleta = referenciaModulo.obj.cmpUrlCompleta.val();
    eRegistro.urlRelativa = referenciaModulo.obj.cmpUrlRelativa.val();
    eRegistro.orden = parseInt(referenciaModulo.obj.cmpOrden.val());
    eRegistro.padre = parseInt(referenciaModulo.obj.cmpPadre.val());
    eRegistro.tipo = parseInt(referenciaModulo.obj.cmpTipo.val());
    eRegistro.permiso = parseInt(referenciaModulo.obj.cmpPermiso.val());

    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
    moduloActual.inicializar();
  });