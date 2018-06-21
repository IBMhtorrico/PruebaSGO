$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='conductor';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'brevete'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'apellidos'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'nombres'});//Target4
  moduloActual.columnasGrilla.push({ "data": 'dni'});//Target5
  moduloActual.columnasGrilla.push({ "data": 'fechaNacimiento'});//Target6
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target7
  
  moduloActual.definicionColumnas.push({"targets": 1, "searchable": true, "orderable": true, "visible":false });
  moduloActual.definicionColumnas.push({"targets": 2, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 3, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 4, "searchable": true, "orderable": true, "visible":true });
  moduloActual.definicionColumnas.push({"targets": 5, "searchable": true, "orderable": true, "visible":true, "class": "text-right"  });
  moduloActual.definicionColumnas.push({"targets": 6, "searchable": true, "orderable": true, "visible":true, "class": "text-center", "render" : utilitario.formatearFecha });
  moduloActual.definicionColumnas.push({"targets": 7, "searchable": true, "orderable": true, "visible":true, "render": utilitario.formatearEstado });

  moduloActual.reglasValidacionFormulario={
	cmpBrevete: 	{ required: true, maxlength: 15 },
	cmpApellidos: 	{ required: true, maxlength: 150 },
	cmpNombres: 	{ required: true, maxlength: 150 },
	cmpDni: 		{ required: true, rangelength: [8, 8], number: true},
	cmpFechaNacimiento: { required: true }
	//cmpEstado: 		{ required: true }
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpBrevete: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 15 caracteres como m&aacute;ximo.",
		text:"red"
	},
	cmpApellidos: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 150 caracteres como m&aacute;ximo."			
	},
	cmpNombres: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 150 caracteres como m&aacute;ximo."
	},
	cmpDni: {
		required: "El campo es obligatorio",
		rangelength: "El campo debe contener 8 caracteres como m&aacute;ximo.",
		number: "El campo solo debe contener caracteres num&eacute;ricos"
	},
	cmpFechaNacimiento: {
		required: "El campo es obligatorio"
	},
	/*cmpEstado: {
		required: "El campo es obligatorio"
	}*/
  };
  
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpBrevete=$("#cmpBrevete");
    this.obj.cmpApellidos=$("#cmpApellidos");
    this.obj.cmpNombres=$("#cmpNombres");
    this.obj.cmpDni=$("#cmpDni");
    this.obj.cmpDni.inputmask('integer');
    this.obj.cmpDni.inputmask("99999999");
    this.obj.cmpDni.css("text-align", "left");
    
    this.obj.cmpFechaNacimiento=$("#cmpFechaNacimiento");
    this.obj.cmpFechaNacimiento.inputmask(constantes.FORMATO_FECHA, 
            { 
                "placeholder": constantes.FORMATO_FECHA,
                onincomplete: function(){
                    $(this).val('');
                }
            }
        );
    
    this.obj.cmpEstado=$("#cmpEstado");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaBrevete=$("#vistaBrevete");
    this.obj.vistaApellidos=$("#vistaApellidos");
    this.obj.vistaNombres=$("#vistaNombres");
    this.obj.vistaDni=$("#vistaDni");
    this.obj.vistaFechaNacimiento=$("#vistaFechaNacimiento");
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
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,7).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpBrevete.val(registro.brevete);
    this.obj.cmpApellidos.val(registro.apellidos);
    this.obj.cmpNombres.val(registro.nombres);
    this.obj.cmpDni.val(registro.dni);
    this.obj.cmpFechaNacimiento.val(utilitario.formatearFecha(registro.fechaNacimiento));
    this.obj.cmpEstado.val(registro.estado);
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaBrevete.text(registro.brevete);
    this.obj.vistaApellidos.text(registro.apellidos);
    this.obj.vistaNombres.text(registro.nombres);
    this.obj.vistaDni.text(registro.dni);
    this.obj.vistaFechaNacimiento.text(utilitario.formatearFecha(registro.fechaNacimiento));
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
    eRegistro.brevete = referenciaModulo.obj.cmpBrevete.val().toUpperCase();
    eRegistro.apellidos = referenciaModulo.obj.cmpApellidos.val().toUpperCase();
    eRegistro.nombres = referenciaModulo.obj.cmpNombres.val().toUpperCase();
    eRegistro.dni = referenciaModulo.obj.cmpDni.val();
    console.log(referenciaModulo.obj.cmpFechaNacimiento.val());
    console.log(utilitario.formatearStringToDate(referenciaModulo.obj.cmpFechaNacimiento.val()));
    eRegistro.fechaNacimiento= utilitario.formatearStringToDate(referenciaModulo.obj.cmpFechaNacimiento.val());
    
    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
