$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='vehiculo';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'nombreCorto'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'descripcion'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'propietario.razonSocial'});//Target4
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
  
  moduloActual.reglasValidacionFormulario={
	cmpNombreCorto: {
		required: true,
		maxlength: 20
	},
	cmpDescripcion: {
		required: true,
		maxlength: 80
	},
	cmpIdPropietario: "required"
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpNombreCorto: {
		required: "El campo Nombre Corto es obligatorio",
		maxlength: "El campo Nombre Corto debe contener 20 caracteres como m&aacute;ximo."
	},
	cmpDescripcion: {
		required: "El campo Descripcion es obligatorio",
		maxlength: "El campo Descripcion debe contener 80 caracteres como m&aacute;ximo."
	},
	cmpIdPropietario: "El campo Propietario es obligatorio"
  };
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpNombreCorto=$("#cmpNombreCorto");
    this.obj.cmpDescripcion=$("#cmpDescripcion");   
    this.obj.cmpIdPropietario=$("#cmpIdPropietario");
    this.obj.cmpIdPropietario.tipoControl="select2";
    this.obj.cmpSelect2Propietario=$("#cmpIdPropietario").select2({
    	  ajax: {
    		    url: "./propietario/listar",
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
		        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
		    },
		    templateSelection: function (registro) {
		    	console.log("templateSelection");
		        return registro.razonSocial || registro.text;
		    },
    		//minimumInputLength: 3
    });
    
    this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpSincronizadoEl=$("#cmpSincronizadoEl");
    this.obj.cmpFechaReferencia=$("#cmpFechaReferencia");
    this.obj.cmpCodigoReferencia=$("#cmpCodigoReferencia");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombreCorto=$("#vistaNombreCorto");
    this.obj.vistaDescripcion=$("#vistaDescripcion");
    this.obj.vistaIdPropietario=$("#vistaIdPropietario");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    this.obj.vistaSincronizadoEl=$("#vistaSincronizadoEl");
    this.obj.vistaFechaReferencia=$("#vistaFechaReferencia");
    this.obj.vistaCodigoReferencia=$("#vistaCodigoReferencia");
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
    this.obj.cmpNombreCorto.val(registro.nombreCorto);
    this.obj.cmpDescripcion.val(registro.descripcion);
    this.obj.cmpEstado.val(registro.estado);
    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.propietario.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.propietario.razonSocial);
    this.obj.cmpIdPropietario.empty().append(elemento).val(registro.propietario.id).trigger('change');
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombreCorto.text(registro.nombreCorto);
    this.obj.vistaDescripcion.text(registro.descripcion);
    this.obj.vistaIdPropietario.text(registro.propietario.razonSocial);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaSincronizadoEl.text(registro.sincronizadoEl);
    this.obj.vistaFechaReferencia.text(registro.fechaReferencia);
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
    eRegistro.nombreCorto = referenciaModulo.obj.cmpNombreCorto.val().toUpperCase();
    eRegistro.descripcion = referenciaModulo.obj.cmpDescripcion.val().toUpperCase();
    eRegistro.idPropietario = referenciaModulo.obj.cmpIdPropietario.val();
    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();
    eRegistro.sincronizadoEl = referenciaModulo.obj.cmpSincronizadoEl.val();
    eRegistro.fechaReferencia = referenciaModulo.obj.cmpFechaReferencia.val();
    eRegistro.codigoReferencia = referenciaModulo.obj.cmpCodigoReferencia.val();
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
