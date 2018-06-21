$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='permiso';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  moduloActual.columnasGrilla.push({ "data": 'id'});
  moduloActual.columnasGrilla.push({ "data": 'nombre'});
  moduloActual.columnasGrilla.push({ "data": 'estado'});
    
  moduloActual.definicionColumnas.push({
    "targets": 1,
    "searchable": true,
    "orderable": true,
    "visible": false
  });
  moduloActual.definicionColumnas.push({
    "targets": 2,
    "searchable": true,
    "orderable": true,
    "visible": true
  });
  moduloActual.definicionColumnas.push({
    "targets": 3,
    "searchable": true,
    "orderable": true,
    "visible": true,
    "render": utilitario.formatearEstado
  });
  
  moduloActual.reglasValidacionFormulario={
   	cmpNombre: {
		required: true,
		maxlength: 40
	},
	cmpEstado: {
		required: true
	},
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpNombre: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 40 caracteres como m&aacute;ximo."
	},
	cmpEstado: {
		required: "El campo es obligatorio",
	}	  
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpEstado=$("#cmpEstado");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  };
  

	moduloActual.grillaDespuesSeleccionar = function(indice) {
		var referenciaModulo = this;
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice, 3).data();
		referenciaModulo.estadoRegistro = estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'	+ constantes.TITULO_DESACTIVAR_REGISTRO);
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>' + constantes.TITULO_ACTIVAR_REGISTRO);
		}
	};

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpNombre.val(registro.nombre);
    this.obj.cmpEstado.val(registro.estado);
  };

  moduloActual.llenarDetalles = function(registro){
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
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
    eRegistro.nombre = referenciaModulo.obj.cmpNombre.val().toUpperCase();
    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();  
    } catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
