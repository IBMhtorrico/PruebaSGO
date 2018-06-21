$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='contometro';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'});
  moduloActual.columnasGrilla.push({ "data": 'alias'});
  moduloActual.columnasGrilla.push({ "data": 'tipoContometro'});
  moduloActual.columnasGrilla.push({ "data": 'estacion.nombre'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
  
  moduloActual.definicionColumnas.push({ "targets": 1, "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({ "targets": 2, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 3, "searchable": true, "orderable": true, "visible":true, "render":utilitario.formatearTipoContometro });
  moduloActual.definicionColumnas.push({ "targets": 4, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({ "targets": 5, "searchable": true, "orderable": true, "visible":true, "render": utilitario.formatearEstado });
  
  moduloActual.reglasValidacionFormulario = {
	cmpAlias: 			{ required: true, maxlength: 20 },
	cmpTipoContometro: 	{ required: true },
	cmpIdEstacion: 		{ required: true }
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpAlias: 			{ required: "El campo es obligatorio", 
						  maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo." },
	cmpTipoContometro: 	{ required: "El campo es obligatorio" },
	cmpIdEstacion: 		{ required: "El campo es obligatorio" }
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpAlias=$("#cmpAlias");
    this.obj.cmpTipoContometro=$("#cmpTipoContometro");
    this.obj.cmpIdEstacion=$("#cmpIdEstacion");
    this.obj.cmpIdEstacion.tipoControl="select2";
    this.obj.cmpSelect2Estacion=$("#cmpIdEstacion").select2({
  	  ajax: {
  		  url: "./estacion/listar",
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
		if (registro.loading) {
			return "Buscando...";
		}		    	
	        return "<div class='select2-user-result'>" + registro.nombre + "</div>";
	    },
	    templateSelection: function (registro) {
	        return registro.nombre || registro.text;
	    },
  });
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaAlias=$("#vistaAlias");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaTipoContometro=$("#vistaTipoContometro");
    this.obj.vistaIdEstacion=$("#vistaIdEstacion");
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
    this.obj.cmpAlias.val(registro.alias);    
    this.obj.cmpTipoContometro.val(registro.tipoContometro); 
    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.estacion.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.estacion.nombre);
    this.obj.cmpIdEstacion.empty().append(elemento).val(registro.estacion.id).trigger('change');    
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaAlias.text(registro.alias);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaTipoContometro.text(utilitario.formatearTipoContometro(registro.tipoContometro));
    this.obj.vistaIdEstacion.text(registro.estacion.nombre);
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
    eRegistro.alias = referenciaModulo.obj.cmpAlias.val().toUpperCase();
    eRegistro.tipoContometro = referenciaModulo.obj.cmpTipoContometro.val();
    eRegistro.idEstacion = referenciaModulo.obj.cmpIdEstacion.val();
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
