$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='tracto';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'placa'});//Target3
  //moduloActual.columnasGrilla.push({ "data": 'transportista.razonSocial'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'razonSocialTransportista'});  
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
		"orderable": true,
		"visible":true,
	    "render": utilitario.formatearEstado
  });
	  
  moduloActual.reglasValidacionFormulario={
	cmpPlaca: {
		required: true,
		maxlength: 15
	},
	cmpIdTransportista: "required",
	/*cmpCodigoReferencia:  {
		required: true,
		maxlength: 20
	},*/
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpPlaca: {
		required: "El campo Placa es obligatorio",
		maxlength: "El campo Placa debe contener 15 caracteres como m&aacute;ximo."
	},
	cmpIdTransportista: "El campo Transportista es obligatorio",
	/*cmpCodigoReferencia: {
		required: "El campo Placa es obligatorio",
		maxlength: "El campo Placa debe contener 20 caracteres como m&aacute;ximo."
	},*/
	cmpCodigoReferencia: "El campo Código Unidad SAP es obligatorio",
  };
  moduloActual.inicializarCampos= function(){
    //campos para el filtro de listado
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
			        page: parametros.page,
			        paginacion:0,
			        filtroEstado: constante.ESTADO_ACTIVO
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
	    
	//Campos de formulario
    this.obj.cmpPlaca=$("#cmpPlaca");
    //this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpIdTransportista=$("#cmpIdTransportista");
    this.obj.cmpIdTransportista.tipoControl="select2";
    this.obj.cmpSelect2Transportista=$("#cmpIdTransportista").select2({
  	  ajax: {
  		    url: "./transportista/listar",
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
		        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
		    },
		    templateSelection: function (registro) {
		        return registro.razonSocial || registro.text;
		    },
  		//minimumInputLength: 3
    });
    this.obj.cmpSincronizadoEl=$("#cmpSincronizadoEl");
    this.obj.cmpFechaReferencia=$("#cmpFechaReferencia");
    this.obj.cmpCodigoReferencia=$("#cmpCodigoReferencia");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaPlaca=$("#vistaPlaca");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaIdTransportista=$("#vistaIdTransportista");
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
  
  moduloActual.resetearFormulario= function(){
	  var referenciaModulo= this;
	  referenciaModulo.obj.cmpPlaca.val("");
	  
	  var elemento = constantes.PLANTILLA_OPCION_SELECTBOX;
	  elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR, "");
	  elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR, "Seleccionar...");
	  referenciaModulo.obj.cmpIdTransportista.empty().append(elemento).val("").trigger('change');
	  referenciaModulo.obj.cmpIdTransportista.val("");
	  referenciaModulo.obj.cmpCodigoReferencia.val("");
  };
  
  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,4).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpPlaca.val(registro.placa);
    //this.obj.cmpEstado.val(registro.estado);
    this.obj.cmpSincronizadoEl.val(registro.sincronizadoEl);
    this.obj.cmpFechaReferencia.val(registro.fechaReferencia);
    this.obj.cmpCodigoReferencia.val(registro.codigoReferencia);
    var elemento=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR,registro.transportista.id);
    elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.transportista.razonSocial);
    this.obj.cmpIdTransportista.empty().append(elemento).val(registro.transportista.id).trigger('change');
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaPlaca.text(registro.placa);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaIdTransportista.text(registro.transportista.razonSocial);
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
    eRegistro.placa = referenciaModulo.obj.cmpPlaca.val().toUpperCase();
    //eRegistro.estado = referenciaModulo.obj.cmpEstado.val();
    eRegistro.idTransportista = referenciaModulo.obj.cmpIdTransportista.val();
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