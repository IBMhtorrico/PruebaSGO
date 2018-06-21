$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='planta';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
//Agregar columnas a la grilla
  moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
  moduloActual.columnasGrilla.push({ "data": 'descripcion'});//Target2
  moduloActual.columnasGrilla.push({ "data": 'estado'});//Target3
    
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
		"orderable": true,
		"visible":true,
		"render":  utilitario.formatearEstado
	});
  
  moduloActual.reglasValidacionFormulario={
	cmpDescripcion: {
		required: true,
		maxlength: 150
	},
	cmpEstado: {
		required: true
	},
	cmpCorreoPara: {
		required: true
	}
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpDescripcion: {
		required: "El campo Descripcion es obligatorio",
		maxlength: "El campo Descripcion debe contener 150 caracteres como m&aacute;ximo."
	},
	cmpEstado: {
		required: "El campo Estado es obligatorio",
	},
	cmpCorreoPara: { 
		required:  "El campo es obligatorio", 
	}
  };
  moduloActual.inicializarCampos= function(){
    //Campos de formulario
    this.obj.cmpDescripcion=$("#cmpDescripcion");
    this.obj.cmpEstado=$("#cmpEstado");
    this.obj.cmpSincronizadoEl=$("#cmpSincronizadoEl");
    this.obj.cmpFechaReferencia=$("#cmpFechaReferencia");
    this.obj.cmpCodigoReferencia=$("#cmpCodigoReferencia");
    this.obj.cmpCorreoPara=$("#cmpCorreoPara");
    this.obj.cmpCorreoCC=$("#cmpCorreoCC");
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaDescripcion=$("#vistaDescripcion");
    this.obj.vistaEstado=$("#vistaEstado");
    this.obj.vistaCorreoPara=$("#vistaCorreoPara");
    this.obj.vistaCorreoCC=$("#vistaCorreoCC");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
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
		var estadoRegistro = referenciaModulo.obj.datClienteApi.cell(indice,3).data();
		referenciaModulo.estadoRegistro=estadoRegistro;
		if (estadoRegistro == constantes.ESTADO_ACTIVO) {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-download"></i>'+constantes.TITULO_DESACTIVAR_REGISTRO);			
		} else {
			referenciaModulo.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);			
		}
  };
  
  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpDescripcion.val(registro.descripcion);
    this.obj.cmpEstado.val(registro.estado);
    this.obj.cmpSincronizadoEl.val(registro.sincronizadoEl);
    this.obj.cmpFechaReferencia.val(registro.fechaReferencia);
    this.obj.cmpCodigoReferencia.val(registro.codigoReferencia);
    this.obj.cmpCorreoPara.val(registro.correoPara);
    this.obj.cmpCorreoCC.val(registro.correoCC);
    
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaDescripcion.text(registro.descripcion);
    this.obj.vistaEstado.text(utilitario.formatearEstado(registro.estado));
    this.obj.vistaCorreoPara.text(registro.correoPara);
    this.obj.vistaCorreoCC.text(registro.correoCC);
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
    eRegistro.descripcion = referenciaModulo.obj.cmpDescripcion.val().toUpperCase();
    eRegistro.estado = referenciaModulo.obj.cmpEstado.val();
    eRegistro.correoPara = referenciaModulo.obj.cmpCorreoPara.val();
    console.log('referenciaModulo.obj.cmpCorreoPara.val() '+referenciaModulo.obj.cmpCorreoPara.val());
	eRegistro.correoCC = referenciaModulo.obj.cmpCorreoCC.val();
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