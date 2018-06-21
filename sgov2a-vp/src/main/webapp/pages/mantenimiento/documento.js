$(document).ready(function(){
  
  var moduloActual = new moduloBase();  
  moduloActual.urlBase='documento';
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
  moduloActual.columnasGrilla.push({ "data": 'nombreDocumento'});
  moduloActual.columnasGrilla.push({ "data": 'perteneceA'});

  moduloActual.definicionColumnas.push({"targets": 1, "searchable": false, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": false, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": false, "orderable": true, "visible":true,"render" : utilitario.formatearAplicaPara });

  jQuery.validator.addMethod('selectcheck', function (value) {
      return (value != '0');
  }, "year required");
  
  
  moduloActual.reglasValidacionFormulario={
	cmpDocumento: { required: true, maxlength: 150 },
	cmpAplicaPara: { selectcheck: true}
  };

  
  moduloActual.mensajesValidacionFormulario={
	cmpDocumento: {
		required: "El campo 'Documento' es obligatorio",
		maxlength: "El campo Documento debe contener 150 caracteres como maximo."
	},
	cmpAplicaPara: {
		selectcheck: "El campo 'Aplica Para' es obligatorio"
	}
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpDocumento=$("#cmpDocumento");
    this.obj.cmpAplicaPara=$("#cmpAplicaPara");
    this.obj.cmpAplicaPara.val('0');
//    this.obj.cmpRuc=$("#cmpRuc");
//    this.obj.cmpRuc.inputmask("99999999999");
//    this.obj.cmpNumContrato=$("#cmpNumContrato");
//    this.obj.cmpDesContrato=$("#cmpDesContrato");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaRazonSocial=$("#vistaRazonSocial");
    this.obj.vistaDocumento=$("#vistaDocumento");
    this.obj.vistaAplicaPara=$("#vistaAplicaPara");   
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");	
    this.obj.vistaIPCreacion=$("#vistaIPCreacion");
    this.obj.vistaIPActualizacion=$("#vistaIPActualizacion");	
  };

  moduloActual.grillaDespuesSeleccionar= function(indice){
//	  var referenciaModulo=this;
//		var estadoRegistro = referenciaModulo.obj.datDocumentoApi.cell(indice,5).data();
//		referenciaModulo.estadoRegistro=estadoRegistro;
//		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
//			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i> '+constantes.TITULO_DESACTIVAR_REGISTRO);			
//		} else {
//			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>  '+constantes.TITULO_ACTIVAR_REGISTRO);			
//		}
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpDocumento.val(registro.nombreDocumento);
    this.obj.cmpAplicaPara.val(registro.perteneceA);

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
    this.obj.vistaDocumento.text(registro.nombreDocumento);
    this.obj.vistaAplicaPara.text(constantes.APLICA_PARA[registro.perteneceA]);     
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
	    eRegistro.nombreDocumento = referenciaModulo.obj.cmpDocumento.val().toUpperCase();
	    eRegistro.perteneceA = referenciaModulo.obj.cmpAplicaPara.val();

    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.iniciarModificar= function(){
	  var referenciaModulo=this;  
	  referenciaModulo.modoEdicion=constantes.MODO_ACTUALIZAR;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_REGISTRO);
	  referenciaModulo.obj.cntTabla.hide();
	  referenciaModulo.obj.cntVistaRegistro.hide();
	  referenciaModulo.obj.ocultaContenedorFormulario.show();
	  //referenciaModulo.obj.cmpDocumento.prop("readonly",true);
	  referenciaModulo.obj.cntFormulario.show();  
	  referenciaModulo.recuperarRegistro();	  
	};
	moduloActual.iniciarAgregar= function(){  
		var referenciaModulo=this;
		try {
	    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_REGISTRO);
	    referenciaModulo.resetearFormulario();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cmpDocumento.prop("readonly",false);
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
		} catch(error){
			referenciaModulo.mostrarDepuracion(error.message);
		};
	};
  
  moduloActual.inicializar();
});