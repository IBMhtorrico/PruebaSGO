function moduloCambioPassword (){
  this.obj={};
  this.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;
  this.NOMBRE_EVENTO_CLICK=constantes.NOMBRE_EVENTO_CLICK;
  this.modoEdicion=constantes.MODO_LISTAR;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  //Inicializar propiedades
  this.urlBase='';  
  this.mensajeEsMostrado=false;
  this.idRegistro = 0;
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};
};

moduloCambioPassword.prototype.mostrarDepuracion = function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloCambioPassword.prototype.mostrarErrorServidor=function(xhr,estado,error){
  var referenciaModulo=this;
  if (xhr.status === constantes.ERROR_SIN_CONEXION) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_NO_CONECTADO);
  } else if (xhr.status == constantes.ERROR_RECURSO_NO_DISPONIBLE) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_RECURSO_NO_DISPONIBLE);
  } else if (xhr.status == constantes.ERROR_INTERNO_SERVIDOR) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_INTERNO_SERVIDOR);
  } else if (estado === constantes.ERROR_INTERPRETACION_DATOS) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_GENERICO_SERVIDOR);
  } else if (estado === constantes.ERROR_TIEMPO_AGOTADO) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_TIEMPO_AGOTADO);
  } else if (estado === constantes.ERROR_CONEXION_ABORTADA) {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_GENERICO_SERVIDOR);
  } else {
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,constantes.ERROR_GENERICO_SERVIDOR);
  }
};

moduloCambioPassword.prototype.inicializar=function(){
  this.mostrarDepuracion("inicializar");
  this.configurarAjax();
  this.inicializarControlesGenericos();
  this.obj.ocultaContenedorFormulario.show();
  this.inicializarFormularioPrincipal();
  this.limpiarFormularioPrincipal();
  this.inicializarCampos();
  this.obj.ocultaContenedorFormulario.hide();
};

moduloCambioPassword.prototype.configurarAjax=function(){
	console.log("configurarAjax");
	var csrfConfiguracion = $("#csrf-token");
	var nombreParametro = csrfConfiguracion.attr("name");
	var valorParametro = csrfConfiguracion.val();
	var parametros = {};
	parametros[nombreParametro]=valorParametro;
	console.log(parametros);
	$.ajaxSetup({
        data: parametros,
        headers : {'X-CSRF-TOKEN' : valorParametro}
    });
};

moduloCambioPassword.prototype.resetearFormulario= function(){
  var referenciaModulo= this;
  referenciaModulo.obj.frmPrincipal[0].reset();
  jQuery.each( this.obj, function( i, val ) {
    if (typeof referenciaModulo.obj[i].tipoControl != constantes.CONTROL_NO_DEFINIDO ){
      if (referenciaModulo.obj[i].tipoControl == constantes.TIPO_CONTROL_SELECT2){
        referenciaModulo.obj[i].select2("val", null);
      }
    }
  });
};

moduloCambioPassword.prototype.validaFormularioXSS= function(formulario){
	//$(document).ready(function(){
	var retorno = true;
    $(formulario).find(':input').each(function() {
     var elemento= this;
     if(!utilitario.validaCaracteresFormulario(elemento.value)){
    	 retorno = false;
     }
    });
    return retorno;
  // });
};

moduloCambioPassword.prototype.inicializarControlesGenericos=function(){
  this.mostrarDepuracion("inicializarControlesGenericos");
  var referenciaModulo=this; 
  this.obj.frmPrincipal = $("#frmPrincipal");
  this.obj.cntFormulario=$("#cntFormulario");
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");
  //Inicializar controles	
  this.obj.ocultaContenedorFormulario=$("#ocultaContenedorFormulario");
  this.obj.bandaInformacion=$("#bandaInformacion");
  //Botones	
  this.obj.btnGuardar=$("#btnGuardar");

  this.obj.btnGuardar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.iniciarGuardar();
  });

};

//TODO
moduloCambioPassword.prototype.iniciarGuardar = function(){
  var referenciaModulo=this;
  try {
      referenciaModulo.actualizarRegistro();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};
	
moduloCambioPassword.prototype.inicializarFormularioPrincipal= function(){  
  //Establecer validaciones del formulario
  var referenciaModulo=this;
    this.obj.frmPrincipal.validate({
    rules: referenciaModulo.reglasValidacionFormulario,
    messages: referenciaModulo.mensajesValidacionFormulario,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloCambioPassword.prototype.actualizarBandaInformacion=function(tipo, mensaje){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("actualizarBandaInformacion");
	referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_ERROR);
	referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_EXITO);
	referenciaModulo.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_INFORMACION);
	if (tipo==constantes.TIPO_MENSAJE_INFO){
		referenciaModulo.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_INFORMACION );
	} else if (tipo==constantes.TIPO_MENSAJE_EXITO){
		referenciaModulo.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_EXITO );
	} else if (tipo==constantes.TIPO_MENSAJE_ERROR){
		referenciaModulo.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_ERROR );
	}	
	referenciaModulo.obj.bandaInformacion.text(mensaje);
};

moduloCambioPassword.prototype.actualizarRegistro = function(){
	var referenciaModulo = this;
	if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (referenciaModulo.obj.frmPrincipal.valid()){
		referenciaModulo.obj.ocultaContenedorFormulario.show();
	var eRegistro = referenciaModulo.recuperarValores();
	try{
	 $.ajax({
		 type: constantes.PETICION_TIPO_GET,
		 url: "./cambioPassword/validaCambioPassword",
	      contentType: referenciaModulo.TIPO_CONTENIDO, 
	      data: {
		    	  claveOld:	eRegistro.claveOld,
		    	  clave:	eRegistro.clave, 
		    	  nuevaClave: eRegistro.nuevaClave,
		    	  confirmaClave:	eRegistro.confirmaClave, 
		    	  nombre:	eRegistro.nombre, 
		      },
	          success: function(respuesta) {
	          if (!respuesta.estado) {
	            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	          } else {
	        	  
	            referenciaModulo.actualizarRegistroUsuario(eRegistro.id, eRegistro.nuevaClave, eRegistro.zonaHoraria);
	          }
	          referenciaModulo.obj.ocultaContenedorFormulario.hide();
	        },			    		    
	        error: function() {
	        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion"); 
	        }
	    });
	} catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	  };
	}
};

moduloCambioPassword.prototype.actualizarRegistroUsuario= function(id, clave, zonaHoraria){
  //Ocultar alertas de mensaje
  var referenciaModulo = this;
  var eRegistro  = {};
  eRegistro.id = id;
  eRegistro.clave = clave;
  eRegistro.zonaHoraria = zonaHoraria;
  if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
  } else {
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: "./usuario/actualizarPassword", 
      contentType: referenciaModulo.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),	
      success: function(respuesta) {
        if (!respuesta.estado) {
          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } 	else {		    				    			    		
          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
        }
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      },			    		    
      error: function() {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      }
    });
  }
};

moduloCambioPassword.prototype.limpiarFormularioPrincipal= function(){
//Implementar en cada caso
};
	
moduloCambioPassword.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloCambioPassword.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};
