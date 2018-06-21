$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='producto';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
  moduloActual.columnasGrilla.push({ "data": 'id'});  //Target1
  moduloActual.columnasGrilla.push({ "data": 'nombre'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'codigoOsinerg'});//Target3
  moduloActual.columnasGrilla.push({ "data": 'abreviatura'});//Target4
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
		 cmpNombre: {
			required: true,
			maxlength: 80
		},
		cmpCodigoOsinerg: {
			required: true,
			maxlength: 5
		},
		cmpAbreviatura: {
			required: true,
			maxlength: 20
		},
	    cmpEstado: "required",
	    cmpCodigoReferencia: {//7000001924
	    	maxlength: 20
	    }
  };
  
  moduloActual.mensajesValidacionFormulario={
	  cmpNombre: {
			required: "El campo Nombre es obligatorio",
			maxlength: "El campo Nombre debe contener 80 caracteres como m&aacute;ximo."
		},
		
		cmpCodigoOsinerg: {
			required: "El campo Codigo Osinerg es obligatorio",
			maxlength: "El campo Codigo Osinerg debe contener 5 caracteres como m&aacute;ximo."
		},
		cmpAbreviatura: {
			required: "El campo Abreviatura es obligatorio",
			maxlength: "El campo Abreviatura debe contener 20 caracteres como m&aacute;ximo."
		},
		cmpEstado: {
			required: "El campo Estado es obligatorio",
		},
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
    this.obj.cmpNombre=$("#cmpNombre");
    this.obj.cmpCodigoOsinerg=$("#cmpCodigoOsinerg");
    this.obj.cmpAbreviatura=$("#cmpAbreviatura");
    this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpCodigoReferencia=$("#cmpCodigoReferencia");//7000001924
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaNombre=$("#vistaNombre");
    this.obj.vistaCodigoOsinerg=$("#vistaCodigoOsinerg");
    this.obj.vistaAbreviatura=$("#vistaAbreviatura");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaSincronizadoEl=$("#vistaSincronizadoEl");
    this.obj.vistaFechaReferencia=$("#vistaFechaReferencia");
    this.obj.vistaCodigoReferencia=$("#vistaCodigoReferencia");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
    this.obj.vistaCodigoReferencia=$("#vistaCodigoReferencia");//7000001924
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
    this.obj.cmpNombre.val(registro.nombre);
    this.obj.cmpCodigoOsinerg.val(registro.codigoOsinerg);
    this.obj.cmpAbreviatura.val(registro.abreviatura);
    this.obj.cmpEstado.val(registro.estado);
    this.obj.cmpCodigoReferencia.val(registro.codigoReferencia);//7000001924
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaNombre.text(registro.nombre);
    this.obj.vistaCodigoOsinerg.text(registro.codigoOsinerg);
    this.obj.vistaAbreviatura.text(registro.abreviatura);
    this.obj.vistaCodigoReferencia.text(registro.codigoReferencia);//7000001924
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
    eRegistro.codigoOsinerg = referenciaModulo.obj.cmpCodigoOsinerg.val().toUpperCase();
    eRegistro.abreviatura = referenciaModulo.obj.cmpAbreviatura.val().toUpperCase();
    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();
    eRegistro.codigoReferencia = referenciaModulo.obj.cmpCodigoReferencia.val().toUpperCase();//7000001924
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
