$(document).ready(function(){
  var moduloActual = new moduloBase();
  
  moduloActual.urlBase='parametro';
  moduloActual.URL_LISTAR = moduloActual.urlBase + '/listar';
  moduloActual.URL_RECUPERAR = moduloActual.urlBase + '/recuperar';
  moduloActual.URL_ELIMINAR = moduloActual.urlBase + '/eliminar';
  moduloActual.URL_GUARDAR = moduloActual.urlBase + '/crear';
  moduloActual.URL_ACTUALIZAR = moduloActual.urlBase + '/actualizar';
  moduloActual.URL_ACTUALIZAR_ESTADO = moduloActual.urlBase + '/actualizarEstado';
  moduloActual.ordenGrilla=[[ 2, 'asc' ]];
  
   moduloActual.columnasGrilla.push({ "data": 'id'}); //Target1
   moduloActual.columnasGrilla.push({ "data": 'valor'});//Target2
   moduloActual.columnasGrilla.push({ "data": 'alias'});//Target3

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
	 
  moduloActual.reglasValidacionFormulario={
  cmpValor: {
		required: true,
		maxlength: 80
	},
	cmpAlias: {
		required: true,
		maxlength: 20
	},
  };
  
  moduloActual.mensajesValidacionFormulario={
	cmpValor: {
		required: "El campo Valor es obligatorio",
		maxlength: "El campo Valor debe contener 80 caracteres como m&aacute;ximo."
	},
	cmpAlias: {
		required: "El campo Alias es obligatorio",
		maxlength: "El campo Alias debe contener 20 caracteres como m&aacute;ximo."
	},
  };

  moduloActual.inicializarCampos= function(){
    //Campos de formulario
	this.obj.cmpValor=$("#cmpValor");
	this.obj.cmpAlias=$("#cmpAlias");
    
    //Campos de vista
    this.obj.vistaId=$("#vistaId");
    this.obj.vistaValor=$("#vistaValor");
    this.obj.vistaAlias=$("#vistaAlias");
    this.obj.vistaCreadoEl=$("#vistaCreadoEl");
    this.obj.vistaCreadoPor=$("#vistaCreadoPor");
    this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
    this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
    this.obj.vistaIpCreacion=$("#vistaIpCreacion");
    this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  };

  moduloActual.llenarFormulario = function(registro){
    this.idRegistro= registro.id;
    this.obj.cmpValor.val(registro.valor);
    this.obj.cmpAlias.val(registro.alias);
  };

  moduloActual.llenarDetalles = function(registro){
    this.idRegistro= registro.id;
    this.obj.vistaId.text(registro.id);
    this.obj.vistaValor.text(registro.valor);
    this.obj.vistaAlias.text(registro.alias);
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
    eRegistro.valor = referenciaModulo.obj.cmpValor.val().toUpperCase();
    eRegistro.alias = referenciaModulo.obj.cmpAlias.val().toUpperCase();
    
    }  catch(error){
      console.log(error.message);
    }
    return eRegistro;
  };
  
  moduloActual.inicializar();
});
