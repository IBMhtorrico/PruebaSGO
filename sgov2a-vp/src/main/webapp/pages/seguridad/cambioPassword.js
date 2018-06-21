$(document).ready(function(){
  var moduloActual = new moduloCambioPassword();
  
  moduloActual.urlBase='cambioPassword';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  
  moduloActual.reglasValidacionFormulario={ 
    cmpClave: 			{ required: true, maxlength: 16   },
	cmpNuevaClave: 		{ required: true, maxlength: 120  },
	cmpConfirmaClave: 	{ required: true, maxlength: 20   },
	cmpZonaHoraria: 	{ required: true, maxlength: 120  },
  };
	  
  moduloActual.mensajesValidacionFormulario={
    cmpClave:  		  { required: "El campo es obligatorio", maxlength: "El campo debe contener 64 caracteres como m&aacute;ximo."},
    cmpNuevaClave:    { required: "El campo es obligatorio", maxlength: "El campo debe contener 64 caracteres como m&aacute;ximo."},
    cmpConfirmaClave: { required: "El campo es obligatorio", maxlength: "El campo debe contener 64 caracteres como m&aacute;ximo."},
    cmpZonaHoraria:   { required: "El campo es obligatorio", maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo."}
  };

  moduloActual.inicializarCampos= function(){
	//Campos de formulario
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpIdentidad=$("#cmpIdentidad");
    this.obj.cmpEmail=$("#cmpEmail");
    this.obj.cmpZonaHoraria=$("#cmpZonaHoraria");
    this.obj.cmpTipo=$("#cmpTipo");
    this.obj.cmpClaveOld=$("#cmpClaveOld");
    this.obj.cmpClave=$("#cmpClave");
    this.obj.cmpNuevaClave=$("#cmpNuevaClave");
    this.obj.cmpConfirmaClave=$("#cmpConfirmaClave");
    
    moduloActual.obj.cmpNombre.val($(cmpNombre).attr('data-nombre'));
    moduloActual.obj.cmpIdentidad.val($(cmpIdentidad).attr('data-identidad'));
    moduloActual.obj.cmpEmail.val($(cmpEmail).attr('data-email'));
    moduloActual.obj.cmpZonaHoraria.val($(cmpNombre).attr('data-zonaHoraria'));
    moduloActual.obj.cmpTipo.val($(cmpNombre).attr('data-tipo'));
    moduloActual.obj.cmpClaveOld.val($(cmpNombre).attr('data-clave'));
    
    
  };

  moduloActual.limpiarFormularioPrincipal = function(){
	var referenciaModulo=this;
	referenciaModulo.obj.frmPrincipal[0].reset();
	
    $(cmpNombre).prop('disabled', true);
	$(cmpIdentidad).prop('disabled', true);
	$(cmpEmail).prop('disabled', true);
	
	if($(cmpNombre).attr('data-tipo') == 1){
		$(cmpClave).prop('disabled', true);
		$(cmpNuevaClave).prop('disabled', true);
		$(cmpConfirmaClave).prop('disabled', true);
		referenciaModulo.obj.btnGuardar.addClass("disabled");
	}
	else{
		$(cmpClave).prop('disabled', false);
		$(cmpNuevaClave).prop('disabled', false);
		$(cmpConfirmaClave).prop('disabled', false);
		referenciaModulo.obj.btnGuardar.removeClass("disabled");
	}
	
  };

//  moduloActual.llenarFormulario = function(registro){
//    this.idRegistro= registro.id;
//    this.obj.cmpNombre.val(registro.nombre);
//    this.obj.cmpIdentidad.val(registro.identidad);
//    this.obj.cmpEmail.val(registro.email);
//    this.obj.cmpZonaHoraria.val(registro.zonaHoraria);
//  };

  moduloActual.recuperarValores = function(registro){
    var eRegistro = {};
    var referenciaModulo=this;
   
    
    console.log("tipo " + $(cmpNombre).attr('data-tipo'));
    console.log("clave " + $(cmpNombre).attr('data-clave'));
    
    
    try {
    eRegistro.id = $(cmpNombre).attr('data-id');
    eRegistro.nombre = $(cmpNombre).attr('data-nombre');
    eRegistro.claveOld = $(cmpNombre).attr('data-clave');
    eRegistro.clave = referenciaModulo.obj.cmpClave.val();
    eRegistro.nuevaClave = referenciaModulo.obj.cmpNuevaClave.val();
    eRegistro.confirmaClave = referenciaModulo.obj.cmpConfirmaClave.val();
    eRegistro.zonaHoraria = referenciaModulo.obj.cmpZonaHoraria.val();
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
	
  moduloActual.inicializar();

});
