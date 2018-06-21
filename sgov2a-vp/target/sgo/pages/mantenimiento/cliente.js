$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='cliente';
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
  moduloActual.columnasGrilla.push({ "data": 'nombreCorto'});
  moduloActual.columnasGrilla.push({ "data": 'razonSocial'});  
  moduloActual.columnasGrilla.push({ "data": 'ruc'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": false, "orderable": true, "visible":true, "class": "text-right" });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": false, "orderable": true, "visible":true, "render": utilitario.formatearEstado });

  moduloActual.reglasValidacionFormulario={
	cmpRazonSocial: { required: true, maxlength: 150 },
	cmpNombreCorto: { required: true, maxlength: 20   },
	cmpRuc: { required: true, rangelength: [11, 11], number: true },
	cmpNumContrato: { required: true, maxlength: 20   },
	cmpDesContrato: { required: true, maxlength: 200   }
  };

  
  moduloActual.mensajesValidacionFormulario={
	cmpRazonSocial: {
		required: "El campo 'Razon Social' es obligatorio",
		maxlength: "El campo 'Razon Social' debe contener 150 caracteres como maximo"
	},
	cmpNombreCorto: {
		required: "El campo 'Nombre Corto' es obligatorio",
		maxlength: "El campo 'Nombre Corto' debe contener 20 caracteres como maximo"
	},
	cmpRuc: {
		required: "El campo 'RUC' es obligatorio",
		rangelength: "El campo 'RUC' debe contener 11 caracteres",
		number: "El campo 'RUC' solo debe contener caracteres numericos"
	},
	cmpNumContrato: {
		required: "El campo 'Nro. de Contrato' es obligatorio",
		maxlength: "El campo 'Nro. de Contrato' debe contener 20 caracteres como maximo"
	},
	cmpDesContrato: {
		required: "El campo 'Descripcion del Contrato' es obligatorio",
		maxlength: "El campo 'Descripcion del Contrato' debe contener 200 caracteres como maximo"
	}
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpRazonSocial=$("#cmpRazonSocial");
    this.obj.cmpNombreCorto=$("#cmpNombreCorto");
    this.obj.cmpRuc=$("#cmpRuc");
    this.obj.cmpRuc.inputmask("99999999999");
    this.obj.cmpNumContrato=$("#cmpNumContrato");
    this.obj.cmpDesContrato=$("#cmpDesContrato");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaRazonSocial=$("#vistaRazonSocial");
    this.obj.vistaNombreCorto=$("#vistaNombreCorto");
    this.obj.vistaRuc=$("#vistaRuc");
    this.obj.vistaNumContrato=$("#vistaNumContrato");
    this.obj.vistaDesContrato=$("#vistaDesContrato");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");	
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
	var referenciaModulo= this;
	referenciaModulo.idRegistro= registro.id;
	referenciaModulo.obj.cmpRazonSocial.val(registro.razonSocial);
	referenciaModulo.obj.cmpNombreCorto.val(registro.nombreCorto);
	referenciaModulo.obj.cmpRuc.val(registro.ruc);
	referenciaModulo.obj.cmpNumContrato.val(registro.numeroContrato);
	referenciaModulo.obj.cmpDesContrato.val(registro.descripcionContrato);
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
    this.obj.vistaRazonSocial.text(registro.razonSocial);
    this.obj.vistaNombreCorto.text(registro.nombreCorto);
    this.obj.vistaRuc.text(registro.ruc);
    this.obj.vistaNumContrato.text(registro.numeroContrato);
    this.obj.vistaDesContrato.text(registro.descripcionContrato);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
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
	    eRegistro.razonSocial = referenciaModulo.obj.cmpRazonSocial.val().toUpperCase();
	    eRegistro.nombreCorto = referenciaModulo.obj.cmpNombreCorto.val().toUpperCase();
	    eRegistro.numeroContrato = referenciaModulo.obj.cmpNumContrato.val().toUpperCase();
	    eRegistro.descripcionContrato = referenciaModulo.obj.cmpDesContrato.val().toUpperCase();
	    eRegistro.ruc = referenciaModulo.obj.cmpRuc.val();

	    console.log(eRegistro);
	    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});