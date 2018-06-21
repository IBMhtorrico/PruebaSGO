$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='mensaje';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
   moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
   moduloActual.columnasGrilla.push({ "data": 'titulo'});//Target2

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
	 
  moduloActual.reglasValidacionFormulario={
  cmpTitulo: {
		required: true,
		maxlength: 20
	},
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpTitulo: {
		required: "El campo es obligatorio",
		maxlength: "El campo debe contener 20 caracteres como m&aacute;ximo."
	},
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpTitulo=$("#cmpTitulo");
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaTitulo=$("#vistaTitulo");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpTitulo.val(registro.titulo);
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaTitulo.text(registro.titulo);
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
    eRegistro.titulo = referenciaModulo.obj.cmpTitulo.val().toUpperCase();
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});