$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='operario';
  //moduloActual.NUMERO_REGISTROS_PAGINA=15;
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  //Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); 
  moduloActual.columnasGrilla.push({ "data": 'nombreOperario'});
  moduloActual.columnasGrilla.push({ "data": 'apellidoPaternoOperario'});
  moduloActual.columnasGrilla.push({ "data": 'apellidoMaternoOperario'});
  moduloActual.columnasGrilla.push({ "data": 'dniOperario'});
  moduloActual.columnasGrilla.push({ "data": 'cliente.razonSocial'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": true, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": false, "orderable": true, "visible":true, "render": utilitario.formatearEstado });

  moduloActual.reglasValidacionFormulario={
	cmpNombres: { required: true, maxlength: 80},
	cmpApellidoPaterno: { required: true, maxlength: 80},
	cmpApellidoMaterno: { required: true, maxlength: 80},
	cmpDni: { required: true, number: true, rangelength: [8, 8]},
	cmpCliente: { required: true}
  };

  
  moduloActual.mensajesValidacionFormulario={
	cmpNombres: {
		required: "El campo 'Nombres' es obligatorio",
		maxlength: "El campo Nombres debe contener 80 caracteres como maximo."
	},
	cmpApellidoPaterno: {
		required: "El campo 'Apellido Paterno' es obligatorio",
		maxlength: "El campo Apellido Paterno debe contener 80 caracteres como maximo."
	},
	cmpApellidoMaterno: {
		required: "El campo 'Apellido Materno' es obligatorio",
		maxlength: "El campo Apellido Materno debe contener 80 caracteres como maximo."
	},
	cmpDni: {
		required: "El campo 'DNI' es obligatorio",
		number: "El campo DNI solo debe contener caracteres numericos",
		rangelength: "El campo DNI debe contener 8 caracteres"
	},
	cmpCliente: {
		required: "El campo 'Cliente' es obligatorio"
	}
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpNombres=$("#cmpNombres");
    this.obj.cmpApellidoPaterno=$("#cmpApellidoPaterno");
    this.obj.cmpApellidoMaterno=$("#cmpApellidoMaterno");
    this.obj.cmpDni=$("#cmpDni");
    this.obj.cmpDni.inputmask("99999999");
    this.obj.cmpCliente=$("#cmpCliente");
    this.obj.cmpCliente.tipoControl="select2";
    this.obj.cmpSelect2Cliente=$("#cmpCliente").select2({
  	  ajax: {
  		    url: "./cliente/listar",
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
  				return 'Buscando...';
  			}		    	
		        return "<div class='select2-user-result'>" + registro.razonSocial + "</div>";
		    },
		    templateSelection: function (registro) {
		    	console.log("templateSelection");
		        return registro.razonSocial || registro.text;
		    },
  		//minimumInputLength: 3
    });
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombres=$("#vistaNombres");
    this.obj.vistaApellidoPaterno=$("#vistaApellidoPaterno");
    this.obj.vistaApellidoMaterno=$("#vistaApellidoMaterno");
    this.obj.vistaDni=$("#vistaDni");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCliente=$("#vistaCliente");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");	
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
	  var referenciaModulo=this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,7).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>  '+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };

  moduloActual.llenarFormulario = function(registro){
	var referenciaModulo= this;
    this.idRegistro= registro.id;
    this.obj.cmpNombres.val(registro.nombreOperario);
    this.obj.cmpApellidoPaterno.val(registro.apellidoPaternoOperario);
    this.obj.cmpApellidoMaterno.val(registro.apellidoMaternoOperario);
    this.obj.cmpDni.val(registro.dniOperario);    
    var elemento1=constantes.PLANTILLA_OPCION_SELECTBOX;
    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR,registro.idCliente);
    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR,registro.cliente.razonSocial);
    referenciaModulo.obj.cmpCliente.empty().append(elemento1).val(registro.idCliente).trigger('change');
  };
  
  moduloActual.resetearFormulario= function(){
  var referenciaModulo= this;
  referenciaModulo.obj.frmPrincipal[0].reset();
  console.log("Verificador");
  referenciaModulo.obj.verificadorFormulario.resetForm();
  jQuery.each( this.obj, function( i, val ) {
    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
    	  console.log(referenciaModulo.obj[i].attr("data-valor-inicial"));
        referenciaModulo.obj[i].select2("val", referenciaModulo.obj[i].attr("data-valor-inicial"));
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
      // if (($.isEmptyObject(this.errorMap))) {
        // console.log("checkForm");
        // this.checkForm();
      // }
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
    this.obj.vistaNombres.text(registro.nombreOperario);
    this.obj.vistaApellidoPaterno.text(registro.apellidoPaternoOperario);
    this.obj.vistaApellidoMaterno.text(registro.apellidoMaternoOperario);
    this.obj.vistaDni.text(registro.dniOperario);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaCliente.text(registro.cliente.razonSocial);
    
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
	    eRegistro.nombreOperario = referenciaModulo.obj.cmpNombres.val().toUpperCase();
	    eRegistro.apellidoPaternoOperario = referenciaModulo.obj.cmpApellidoPaterno.val().toUpperCase();
	    eRegistro.apellidoMaternoOperario = referenciaModulo.obj.cmpApellidoMaterno.val().toUpperCase();
	    eRegistro.dniOperario = referenciaModulo.obj.cmpDni.val();
	    console.log('AQUI:'+referenciaModulo.obj.cmpCliente.val());
	    eRegistro.idCliente = parseInt(referenciaModulo.obj.cmpCliente.val());	    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});