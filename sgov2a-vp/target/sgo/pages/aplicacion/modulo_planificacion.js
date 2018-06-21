function moduloPlanificacion (){
  this.obj={};
  this.NUMERO_REGISTROS_PAGINA = constantes.NUMERO_REGISTROS_PAGINA;
  this.TOPES_PAGINACION = constantes.TOPES_PAGINACION;
  this.URL_LENGUAJE_GRILLA = "tema/datatable/language/es-ES.json";
  this.LENGUAJE_GRILLA =  {
		    "sProcessing":cadenas.GRILLA_PROCESANDO,
		    "sLengthMenu":cadenas.GRILLA_LONGITUD_MENU,
		    "sZeroRecords":cadenas.GRILLA_SIN_REGISTROS,
		    "sEmptyTable": cadenas.GRILLA_TABLA_VACIA,
		    "sInfo":cadenas.GRILLA_INFO,
		    "sInfoEmpty": cadenas.GRILLA_INFO_VACIA,
		    "sInfoFiltered": cadenas.GRILLA_INFO_FILTRADA,
		    "sInfoPostFix":   cadenas.GRILLA_INFO_POST_FIX,
		    "sSearch": cadenas.GRILLA_BUSCAR,
		    "sUrl":          cadenas.GRILLA_URL,
		    "sInfoThousands":  cadenas.GRILLA_MILES,
		    "sLoadingRecords": cadenas.GRILLA_CARGANDO_REGISTROS,
		    "oPaginate": {
		      "sFirst":   cadenas.GRILLA_PAGINACION_PRIMERO,
		      "sLast":    cadenas.GRILLA_PAGINACION_ULTIMO,
		      "sNext":     cadenas.GRILLA_PAGINACION_SIGUIENTE,
		      "sPrevious": cadenas.GRILLA_PAGINACION_ANTERIOR,
		    },
		    "oAria": {
		      "sSortAscending":  cadenas.GRILLA_ORDEN_ASCENDENTE,
		      "sSortDescending": cadenas.GRILLA_ORDEN_DESCENDENTE,
		    }
		  };

  this.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;
  this.NOMBRE_EVENTO_CLICK=constantes.NOMBRE_EVENTO_CLICK;
  this.modoEdicion=constantes.MODO_LISTAR;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  //Inicializar propiedades
  this.urlBase='';  
  this.mensajeEsMostrado=false;
  this.idRegistro = 0;
  this.columnasGrilla={};
  this.definicionColumnas=[];
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};
  this.ordenGrilla=[[ 1, 'asc' ]];
  this.columnasGrilla=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnas=[{
    "targets": 0,
    "searchable": false,
    "orderable": false,      
    "visible":false,
    "render": function ( datos, tipo, fila, meta ) {
    var configuracion =meta.settings;
    return configuracion._iDisplayStart + meta.row + 1;
    }
  }];  
  
  //para la grilla del formulario
  this.columnasGrillaFormulario={};
  this.definicionColumnasFormulario=[];
//  this.ordenGrillaFormulario=[[ 1, 'asc' ]];
  this.columnasGrillaFormulario=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnasFormulario=[{ "targets": 0, "searchable": false, "orderable": false, "visible":false, "render": function ( datos, tipo, fila, meta ) {
    var configuracion =meta.settings;
    return configuracion._iDisplayStart + meta.row + 1;
    }
  }];  
  
  //para la grilla del detalle
  this.columnasGrillaDetalle={};
  this.definicionColumnasDetalle=[];
//  this.ordenGrillaDetalle=[[ 1, 'asc' ]];
  this.columnasGrillaDetalle=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnasDetalle=[{ "targets": 0, "searchable": false, "orderable": false, "visible":false, "render": function ( datos, tipo, fila, meta ) {
    var configuracion =meta.settings;
    return configuracion._iDisplayStart + meta.row + 1;
    }
  }];  
};

moduloPlanificacion.prototype.mostrarDepuracion=function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloPlanificacion.prototype.mostrarErrorServidor=function(xhr,estado,error){
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

moduloPlanificacion.prototype.inicializar=function(){
  this.configurarAjax();
  this.mostrarDepuracion("inicializarControlesGenericos");
  this.inicializarControlesGenericos();
  this.mostrarDepuracion("inicializarGrilla");
  this.inicializarGrilla();
  this.inicializarGrillaFormulario();
  this.inicializarGrillaDetalle();
  this.mostrarDepuracion("inicializarFormularioPrincipal");
  this.inicializarFormularioPrincipal();
  this.inicializarCampos();
};

moduloPlanificacion.prototype.configurarAjax=function(){
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

moduloPlanificacion.prototype.resetearFormulario= function(){
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

moduloPlanificacion.prototype.validaFormularioXSS= function(formulario){
	//$(document).ready(function(){
	var retorno = true;
    $(formulario).find(':input').each(function() {
     var elemento= this;
     console.log($(this));
     if(!utilitario.validaCaracteresFormulario(elemento.value)){
    	 retorno = false;
     }
    });
    return retorno;
  // });
};
	
moduloPlanificacion.prototype.inicializarControlesGenericos=function(){
  this.mostrarDepuracion("inicializarControlesGenericos");
  var referenciaModulo=this; 
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  //
  this.obj.tituloSeccion=$("#tituloSeccion");
  this.obj.tablaPrincipal=$('#tablaPrincipal');
  this.obj.tablaFormulario=$('#tablaFormulario');
  this.obj.tablaDetalle=$('#tablaDetalle');
  this.obj.frmPrincipal = $("#frmPrincipal");
  this.obj.cntTabla=$("#cntTabla");
  this.obj.cntFormulario=$("#cntFormulario");
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");
  this.obj.cntDetallePlanificacion=$("#cntDetallePlanificacion");
  this.obj.cntVistaRegistro=$("#cntVistaRegistro");
  this.obj.cntPlanificaciones=$("#cntPlanificaciones");
  //Inicializar controles	
  this.obj.frmConfirmarModificarEstado=$("#frmConfirmarModificarEstado");	
  this.obj.frmConfirmarEliminar=$("#frmConfirmarEliminar");
  this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
  this.obj.ocultaContenedorFormulario=$("#ocultaContenedorFormulario");
  this.obj.ocultaContenedorDetalle=$("#ocultaContenedorDetalle");
  this.obj.ocultaContenedorVista=$("#ocultaContenedorVista");
  this.obj.bandaInformacion=$("#bandaInformacion");
  //Botones	
  this.obj.btnListar=$("#btnListar");
  this.obj.btnAgregar=$("#btnAgregar");
  this.obj.btnModificar=$("#btnModificar");
  this.obj.btnModificarEstado=$("#btnModificarEstado");	
  this.obj.btnConfirmarModificarEstado=$("#btnConfirmarModificarEstado");
  this.obj.btnDetalle=$("#btnDetalle");
  this.obj.btnVer=$("#btnVer");
  this.obj.btnConfirmarEliminar=$("#btnConfirmarEliminar");	
  this.obj.btnGuardar=$("#btnGuardar");
  this.obj.btnArribo=$("#btnArribo");
  this.obj.btnCancelarGuardar=$("#btnCancelarGuardar");
  this.obj.btnRegresar=$("#btnRegresar");
  this.obj.btnCerrarVista=$("#btnCerrarVista");
  this.obj.btnCerrarDetalle=$("#btnCerrarDetalle");
  this.obj.btnFiltrar=$("#btnFiltrar");
  //para anular una planificacion
  this.obj.btnAnular=$("#btnAnular");
  this.obj.btnConfirmarAnular=$("#btnConfirmarAnular");
  this.obj.frmConfirmarAnularEstado=$("#frmConfirmarAnularEstado");
  //para notificar
  this.obj.btnNotificar=$("#btnNotificar");
  this.obj.btnEnviarCorreo=$("#btnEnviarCorreo");
  this.obj.frmNotificar=$("#frmNotificar");
  this.obj.cmpPara=$("#cmpPara");
  this.obj.cmpCC=$("#cmpCC");
  this.obj.cmpETA=$("#cmpETA");
  this.obj.cmpAsunto=$("#cmpAsunto");
  
  this.obj.btnNotificarUnaPlanificacion=$("#btnNotificarUnaPlanificacion");
  this.obj.btnEnviarCorreoUnaPlanicacion=$("#btnEnviarCorreoUnaPlanicacion");
  this.obj.frmNotificarUnaPlanificacion=$("#frmNotificarUnaPlanificacion");
  this.obj.cmpParaUnaPlanificacion=$("#cmpParaUnaPlanificacion");
  this.obj.cmpCCUnaPlanificacion=$("#cmpCCUnaPlanificacion");
  this.obj.cmpAsuntoUnaPlanificacion=$("#cmpAsuntoUnaPlanificacion");

  //estos valores para hacer los filtros de los listados	
  this.obj.txtFiltro=$("#txtFiltro");
  this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
  this.obj.cmpFechaInicial=$("#cmpFechaInicial");
  this.obj.cmpFechaFinal=$("#cmpFechaFinal");
  this.obj.filtroFechaPlanificada=$("#filtroFechaPlanificada");
  this.obj.cmpFiltroUsuario=$("#cmpFiltroUsuario");
  this.obj.cmpFiltroTabla=$("#cmpFiltroTabla");
  this.obj.cmpFiltroTipoFecha=$("#cmpFiltroTipoFecha");	
  //
  this.obj.filtroOperacion=$("#filtroOperacion");
  //
  this.obj.btnFiltrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
  referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
  referenciaModulo.listarRegistros();
  });

  this.obj.btnListar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
  referenciaModulo.listarRegistros();
  });

  this.obj.btnAgregar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'CREAR_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //Agregado por obs 9000002608==============
	  $("#cmpCantCisternas").prop('disabled', true);
	  $("#cmpCantCisternas").val(0);
	  //=========================================
  	//referenciaModulo.iniciarAgregar();
  });

  this.obj.btnModificar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ACTUALIZAR_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.iniciarModificar();
  });
  
  this.obj.btnAnular.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ANULAR_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.solicitarAnularEstado();
  });
  
  this.obj.btnConfirmarAnular.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.anularRegistro();
  });
  
  this.obj.btnVer.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'RECUPERAR_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.iniciarVer();
  });
  
  this.obj.btnDetalle.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'URL_RECUPERAR_PLANIFICACION_COMPLETA';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.iniciarDetalle();
  });

  this.obj.btnNotificar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  console.log("entra en btnNotificar");
	  referenciaModulo.descripcionPermiso = 'NOTIFICAR_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.abrirVentanaCorreo();
  });
  
  this.obj.btnNotificarUnaPlanificacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  console.log("entra en btnNotificar");
	  referenciaModulo.descripcionPermiso = 'NOTIFICAR_UNA_PLANIFICACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.abrirVentanaCorreo();
  });
  
  this.obj.btnEnviarCorreo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  //referenciaModulo.enviarCorreo();
	  referenciaModulo.enviarCorreoArchivo();
  });

  this.obj.btnEnviarCorreoUnaPlanicacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.enviarCorreo();
	  //referenciaModulo.enviarCorreoArchivo();
  });
  
 /* this.obj.btnModificarEstado.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.solicitarActualizarEstado();
  });

  this.obj.btnConfirmarModificarEstado.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.actualizarEstadoRegistro();
  });*/

  this.obj.btnCancelarGuardar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.iniciarCancelar();
  });
  
  this.obj.btnRegresar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.iniciarRegresar();
  });

  this.obj.btnCerrarVista.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.iniciarCerrarVista();
  });

  this.obj.btnCerrarDetalle.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  referenciaModulo.iniciarCerrarDetalle();
  });
  
  this.obj.btnGuardar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
  //Agregado por obs 9000002608===============================
	  
	  var iniciar = referenciaModulo.validarVolumenYCisternas();
      
      if(iniciar){
    	  referenciaModulo.iniciarGuardar();
      }
  //==========================================================
  //Comentado por obs 9000002608===============================
//  referenciaModulo.iniciarGuardar();
  //==========================================================
  });
};

moduloPlanificacion.prototype.iniciarGuardar = function(){
  var referenciaModulo=this;
  try {
	var validar = referenciaModulo.validarProductoPlanificacion();
	if(validar){
	    if (referenciaModulo.modoEdicion == constantes.MODO_NUEVO){
	      referenciaModulo.guardarRegistro();
	    } else if  (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
	      referenciaModulo.actualizarRegistro();
	    }
	}  
  } catch(error){
	  referenciaModulo.mostrarDepuracion(error.message);
  };
};

moduloPlanificacion.prototype.iniciarAgregar= function(){
	//metodo implementado en planificacion.js
	/*var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_PLANIFICACION);
	    referenciaModulo.resetearFormulario();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.cntPlanificaciones.hide();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};*/
};

moduloPlanificacion.prototype.iniciarModificar= function(){
  var referenciaModulo=this;
  try {
	  referenciaModulo.modoEdicion=constantes.MODO_ACTUALIZAR;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_PLANIFICACION);
	  referenciaModulo.obj.cntTabla.hide();
	  referenciaModulo.obj.cntVistaRegistro.hide();
	  referenciaModulo.obj.ocultaContenedorFormulario.show();
	  referenciaModulo.obj.cntFormulario.show();
	  referenciaModulo.obj.cntPlanificaciones.show();
	  referenciaModulo.recuperarRegistro();
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloPlanificacion.prototype.solicitarAnularEstado= function(){
	var referenciaModulo=this;
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarAnularEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloPlanificacion.prototype.anularRegistro= function(){
  var eRegistro = {};
  var referenciaModulo=this;
  referenciaModulo.obj.frmConfirmarAnularEstado.modal("hide");
  referenciaModulo.obj.ocultaContenedorTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  eRegistro.id = parseInt(referenciaModulo.idRegistro);
  try{
  $.ajax({
    type: constantes.PETICION_TIPO_POST,
    url: referenciaModulo.URL_ANULAR, 
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: JSON.stringify(eRegistro),	
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {		    				    			    		
        referenciaModulo.listarRegistros();
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        referenciaModulo.obj.cntFormulario.hide();	
        referenciaModulo.obj.cntTabla.show();
      }
      referenciaModulo.obj.ocultaContenedorTabla.hide();
    },			    		    
    error: function() {
      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
  } catch(error){
	  referenciaModulo.mostrarDepuracion(error.message);
  }  
};

moduloPlanificacion.prototype.abrirVentanaCorreo= function(){
	var ref=this;
	try {
		//if(ref.obj.cantidadCisternasSolicitadas > 0) {
			//ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
			ref.obj.cmpAsunto.val("Programaci\u00F3n de Planificaci\u00F3n");
			ref.obj.frmNotificar.modal("show");
	//	} else {
	//		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No se puede enviar una notificación sin planificación de cisternas, favor verifique.");
	//	}
	} catch(error){
		ref.mostrarDepuracion(error.message);
	}
};

moduloPlanificacion.prototype.abrirVentanaCorreoUnaPlanificacion = function(){
	var ref=this;
	try {
		if(ref.obj.cantidadCisternasSolicitadas > 0) {
			//ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
			ref.obj.cmpAsuntoUnaPlanificacion.val("Programaci\u00F3n de Planificaci\u00F3n");
			ref.obj.frmNotificarUnaPlanificacion.modal("show");
		} else {
			ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No se puede enviar una notificación sin planificación de cisternas, favor verifique.");
		}
	} catch(error){
		ref.mostrarDepuracion(error.message);
	}
};

moduloPlanificacion.prototype.enviarCorreoArchivo= function(){
	  var eRegistro = {};
	  var ref=this;
	  ref.obj.frmNotificar.modal("hide");
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	  ref.obj.ocultaContenedorDetalle.show();
	  eRegistro.id = parseInt(ref.idRegistro);
	  var rangoFecha = ref.obj.filtroFechaPlanificada.val().split("-");
	  var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
	  var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
    try{
	  $.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './planificacion/notificarDetallePlanificaciones', 
	    contentType: ref.TIPO_CONTENIDO, 
	    data: {
	    	   filtroMailPara : ref.obj.cmpPara.val(), 
	    	   filtroMailCC : ref.obj.cmpCC.val(),
	    	   filtroEta: ref.obj.cmpETA,
	    	   //filtroFechaDiaOperativo : utilitario.formatearFecha(ref.obj.fechaParaNotificacion),
	    	   //filtroFechaCarga : utilitario.formatearFecha(ref.obj.fechaCargaParaNotificar),
	    	   filtroNombreCliente : ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-cliente'),
	    	   filtroDiaOperativo : ref.idRegistro,
	    	   filtroNombreOperacion : ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-operacion'),
	    	   //filtroCisterna : ref.obj.cantidadCisternasSolicitadas,
	    	   filtroOperacion : ref.obj.filtroOperacion.val(),
	    	   filtroFechaInicio:fechaInicio,
	    	   filtroFechaFinal:fechaFinal
	    	},	
	    success: function(respuesta) {
	      if (!respuesta.estado) {
	    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	  ref.obj.ocultaContenedorDetalle.hide();
	      } else {		
	    	  //ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  ref.obj.cmpPara.val("");
	    	  ref.obj.cmpCC.val("");
	    	  ref.obj.ocultaContenedorDetalle.hide();
	    	  ref.obj.cntDetallePlanificacion.hide();
	    	  ref.iniciarListado(respuesta.mensaje);
	      }
	    },			    		    
	    error: function() {
	    	ref.mostrarErrorServidor(xhr,estado,error); 
	    }
	    });
    } catch(error){
  	  ref.mostrarDepuracion(error.message);
  	  ref.obj.ocultaContenedorTabla.hide();
	  }
};
moduloPlanificacion.prototype.enviarCorreo= function(){
	  var eRegistro = {};
	  var ref=this;
	  ref.obj.frmNotificarUnaPlanificacion.modal("hide");
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	  ref.obj.ocultaContenedorTabla.show();
      eRegistro.id = parseInt(ref.idRegistro);
      try{
	  $.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './planificacion/notificar', 
	    contentType: ref.TIPO_CONTENIDO, 
	    data: {filtroMailPara : ref.obj.cmpParaUnaPlanificacion.val(), 
	    	   filtroMailCC : ref.obj.cmpCCUnaPlanificacion.val(),
	    	   filtroFechaDiaOperativo : utilitario.formatearFecha(ref.obj.fechaParaNotificacion),
	    	   filtroFechaCarga : utilitario.formatearFecha(ref.obj.fechaCargaParaNotificar),
	    	   filtroNombreCliente : ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-cliente'),
	    	   filtroDiaOperativo : ref.idRegistro,
	    	   filtroNombreOperacion : ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-operacion'),
	    	   filtroCisterna : ref.obj.cantidadCisternasSolicitadas,
	    	   filtroOperacion : ref.obj.filtroOperacion.val()
	    	},	
	    success: function(respuesta) {
	      if (!respuesta.estado) {
	    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	  ref.obj.ocultaContenedorTabla.hide();
	      } else {		
	    	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    	  ref.obj.cmpParaUnaPlanificacion.val("");
	    	  ref.obj.cmpCCUnaPlanificacion.val("");
	    	  ref.obj.ocultaContenedorTabla.hide();
	      }
	    },			    		    
	    error: function() {
	    	ref.mostrarErrorServidor(xhr,estado,error); 
	    }
	    });
      } catch(error){
    	  ref.mostrarDepuracion(error.message);
    	  ref.obj.ocultaContenedorTabla.hide();
	  }
};

moduloPlanificacion.prototype.recuperaParametro = function(){
  var referenciaModulo=this;
  try{
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: './parametro/listar', 
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: {filtroParametro : "ULTIMAS DESCARGAS"},
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {		
    	  referenciaModulo.obj.valorParametro = respuesta.contenido.carga[0].valor;
      }
      referenciaModulo.obj.ocultaContenedorTabla.hide();
    },			    		    
    error: function() {
      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
  } catch(error){
	  referenciaModulo.mostrarDepuracion(error.message);
  }  
};

moduloPlanificacion.prototype.iniciarVer= function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.ocultaContenedorTabla.hide();
		referenciaModulo.modoEdicion=constantes.MODO_VER;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_VER_PLANIFICACION);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.ocultaContenedorVista.show();
		referenciaModulo.obj.cntVistaRegistro.show();
		referenciaModulo.recuperarRegistro();
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloPlanificacion.prototype.iniciarDetalle= function(){
	var referenciaModulo=this;
	try {
		referenciaModulo.modoEdicion= constantes.MODO_DETALLE_PLANIFICACION;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_PLANIFICACION);
		referenciaModulo.obj.cntTabla.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.ocultaContenedorTabla.show();
		referenciaModulo.recuperarDetallePlanificacion();
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};		  
};

moduloPlanificacion.prototype.iniciarCerrarDetalle=function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("iniciarCerrarDetalle");
  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
  referenciaModulo.obj.cntFormulario.hide();
  referenciaModulo.obj.cntVistaRegistro.hide();
  //referenciaModulo.obj.ocultaContenedorVista.show();
  referenciaModulo.obj.cntDetallePlanificacion.hide();
  referenciaModulo.obj.cntTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CERRAR_VISTA);
  referenciaModulo.obj.ocultaContenedorTabla.hide();
  referenciaModulo.obj.cntDetallePlanificacion.hide();
};

moduloPlanificacion.prototype.iniciarCancelar=function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("iniciarCancelar");
  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
  referenciaModulo.resetearFormulario();
  referenciaModulo.obj.cntFormulario.hide();
  referenciaModulo.obj.ocultaContenedorFormulario.show();
  referenciaModulo.obj.cntVistaRegistro.hide();
  referenciaModulo.obj.cntTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
};

moduloPlanificacion.prototype.iniciarRegresar=function(){
  var referenciaModulo=this;
  referenciaModulo.iniciarCancelar();
};

moduloPlanificacion.prototype.iniciarCerrarVista=function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("iniciarCerrarVista");
  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
  referenciaModulo.obj.cntFormulario.hide();
  referenciaModulo.obj.cntVistaRegistro.hide();
  //referenciaModulo.obj.ocultaContenedorVista.show();
  referenciaModulo.obj.cntTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CERRAR_VISTA);
  referenciaModulo.obj.ocultaContenedorTabla.hide();
  referenciaModulo.obj.ocultaContenedorVista.hide();
};

moduloPlanificacion.prototype.llamadaAjaxGrilla=function(e,configuracion,json){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
  referenciaModulo.desactivarBotones();
  if (json.estado==true){
    json.recordsTotal=json.contenido.totalRegistros;
    json.recordsFiltered=json.contenido.totalEncontrados;
    json.data= json.contenido.carga;
    if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    }
  } else {
    json.recordsTotal=0;
    json.recordsFiltered=0;
    json.data= {};
    if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    } else {

    }
  }
  if (referenciaModulo.estaCargadaInterface==false){        
    referenciaModulo.estaCargadaInterface=true;
  }
  referenciaModulo.obj.ocultaContenedorTabla.hide();  
};

moduloPlanificacion.prototype.inicializarGrilla=function(){
  //Nota no retornar el objeto solo manipular directamente
	//Establecer grilla y su configuracion
  var referenciaModulo=this;
  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
	   referenciaModulo.llamadaAjaxGrilla(e,configuracion,json);
  });
  
  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
    //Se ejecuta antes de cualquier llamada ajax
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
    if (referenciaModulo.estaCargadaInterface==true){
    referenciaModulo.obj.ocultaContenedorTabla.show();
    }
  });

  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_PAGINACION, function () {
  //Se ejecuta cuando se hace clic en boton de paginacion
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
  });

  this.obj.tablaPrincipal.on(constantes.DT_EVENTO_ORDENACION, function () {
  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
  });

  this.obj.datDiaOperativoAPI= this.obj.tablaPrincipal.DataTable({
	"processing": true,
	"responsive": true,
	"dom": '<"row" <"col-sm-12" t> ><"row" <"col-sm-3"l> <"col-sm-4"i> <"col-sm-5"p>>',
	"iDisplayLength": referenciaModulo.NUMERO_REGISTROS_PAGINA,
	"lengthMenu": referenciaModulo.TOPES_PAGINACION,
	"language": {
	"url": referenciaModulo.URL_LENGUAJE_GRILLA
	},
    "serverSide": true,
    "ajax": {
      "url": referenciaModulo.URL_LISTAR,
      "type":constantes.PETICION_TIPO_GET,
      "data": function (d) {
        var indiceOrdenamiento = d.order[0].column;
        d.registrosxPagina =  d.length; 
        d.inicioPagina = d.start; 
        d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
        d.sentidoOrdenamiento=d.order[0].dir;
        d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
        var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
        var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
        var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
        d.filtroFechaInicio= fechaInicio;	
        d.filtroFechaFinal = fechaFinal;	
      }
    },
    "columns": referenciaModulo.columnasGrilla,
    "columnDefs": referenciaModulo.definicionColumnas
    //"order": referenciaModulo.ordenGrilla
	});	
	
	$('#tablaPrincipal tbody').on(referenciaModulo.NOMBRE_EVENTO_CLICK, 'tr', function () {
		if (referenciaModulo.obj.datDiaOperativoAPI.data().length>0){
		    if ( $(this).hasClass('selected') ) {
		        $(this).removeClass('selected');
		    } else {
		        referenciaModulo.obj.datDiaOperativoAPI.$('tr.selected').removeClass('selected');
		        $(this).addClass('selected');
		    }
		  	var indiceFila = referenciaModulo.obj.datDiaOperativoAPI.row( this ).index();
		  	var idRegistro = referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,1).data();
		  	referenciaModulo.idRegistro=idRegistro;		
		  	referenciaModulo.grillaDespuesSeleccionar(indiceFila);
		  	referenciaModulo.activarBotones();
		  	var estadoRegistro = parseInt(referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,7).data());

		  	referenciaModulo.obj.fechaParaNotificacion = (referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,2).data());
		  	referenciaModulo.obj.cantidadCisternasSolicitadas = (referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,4).data());
		  	referenciaModulo.obj.fechaCargaParaNotificar = (referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,3).data());
		  	referenciaModulo.obj.cmpParaUnaPlanificacion.val(referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,8).data());
		  	referenciaModulo.obj.cmpCCUnaPlanificacion.val(referenciaModulo.obj.datDiaOperativoAPI.cell(indiceFila,9).data());

		  	if(estadoRegistro == constantes.ESTADO_PLANIFICADO || estadoRegistro == constantes.ESTADO_PROGRAMADO || estadoRegistro == constantes.ESTADO_ASIGNADO){
		  		referenciaModulo.obj.btnModificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  	} else {
		  		referenciaModulo.obj.btnModificar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  	}
		  	
		  	if(estadoRegistro == constantes.ESTADO_PLANIFICADO){
		  		
		  		referenciaModulo.obj.btnAnular.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  		referenciaModulo.obj.btnNotificarUnaPlanificacion.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  	} else {
		  		referenciaModulo.obj.btnAnular.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  		referenciaModulo.obj.btnNotificarUnaPlanificacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  	}
		}
	});
};

//TODO
//esto para la grilla del formulario
moduloPlanificacion.prototype.llamadaAjaxGrillaFormulario=function(e,configuracion,json){
	  var ref=this;
	  try {
		  ref.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
		  if (json.estado==true){
		    json.recordsTotal=json.contenido.totalRegistros;
		    json.recordsFiltered=json.contenido.totalEncontrados;
		    json.data= json.contenido.carga;
		    if (ref.modoEdicion==constantes.MODO_LISTAR){
		    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
		    }
		  } else {
		    json.recordsTotal=0;
		    json.recordsFiltered=0;
		    json.data= {};
		    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
		  }
		  if (ref.estaCargadaInterface==false){        
			  ref.estaCargadaInterface=true;
		  }
	   } catch(error){
		    ref.mostrarDepuracion(error.message);
	   }  
	};

	moduloPlanificacion.prototype.inicializarGrillaFormulario=function(){
	  //Nota no retornar el objeto solo manipular directamente
		//Establecer grilla y su configuracion
	  var referenciaModulo=this;
	  this.obj.tablaFormulario.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrillaFormulario(e,configuracion,json);
	  });

	  this.obj.tablaFormulario.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    if (referenciaModulo.estaCargadaInterface==true){
	    referenciaModulo.obj.ocultaContenedorTabla.show();
	    }
	  });

	  this.obj.tablaFormulario.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaFormulario.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });

	  this.obj.datFormularioAPI= this.obj.tablaFormulario.DataTable({
	    processing: true,
	    responsive: true,
	    dom: constantes.DT_ESTRUCTURA,
	    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA,
	    lengthMenu: referenciaModulo.TOPES_PAGINACION,
	    language: {
	      url: referenciaModulo.URL_LENGUAJE_GRILLA
	    },
	    "serverSide": true,
	    "ajax": {
	      "url": './tanqueJornada/listar',
	      "type":constantes.PETICION_TIPO_GET,
	      "data": function (d) {
	          d.idOperacion = referenciaModulo.idOperacion;
	          d.filtroEstado = constantes.TIPO_JORNADA_LIQUIDADO,
	          d.filtroFechaDiaOperativo = referenciaModulo.obj.cmpFechaJornada.val();
	      }
	    },
	    "columns": referenciaModulo.columnasGrillaFormulario,
	    "columnDefs": referenciaModulo.definicionColumnasFormulario
	    //"order": referenciaModulo.ordenGrilla
		});	
	};
	
//esto para la grilla del detalle del registro
moduloPlanificacion.prototype.llamadaAjaxGrillaDetalle=function(e,configuracion,json){
	  var ref=this;
	  try {
		  ref.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
		  if (json.estado==true){
		    json.recordsTotal=json.contenido.totalRegistros;
		    json.recordsFiltered=json.contenido.totalEncontrados;
		    json.data= json.contenido.carga;
		    if (ref.modoEdicion==constantes.MODO_LISTAR){
		    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
		    }
		  } else {
		    json.recordsTotal=0;
		    json.recordsFiltered=0;
		    json.data= {};
		    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
		  }
		  if (ref.estaCargadaInterface==false){        
			  ref.estaCargadaInterface=true;
		  }
	   } catch(error){
		    ref.mostrarDepuracion(error.message);
	   }  
	};

	moduloPlanificacion.prototype.inicializarGrillaDetalle=function(){
	  //Nota no retornar el objeto solo manipular directamente
		//Establecer grilla y su configuracion
	  var referenciaModulo=this;
	  this.obj.tablaDetalle.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrillaDetalle(e,configuracion,json);
	  });

	  this.obj.tablaDetalle.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    if (referenciaModulo.estaCargadaInterface==true){
	    referenciaModulo.obj.ocultaContenedorTabla.show();
	    }
	  });

	  this.obj.tablaDetalle.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaDetalle.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });

	  this.obj.datDetalleAPI= this.obj.tablaDetalle.DataTable({
	    processing: true,
	    responsive: true,
	    dom: constantes.DT_ESTRUCTURA,
	    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA,
	    lengthMenu: referenciaModulo.TOPES_PAGINACION,
	    language: {
	      url: referenciaModulo.URL_LENGUAJE_GRILLA
	    },
	    "serverSide": true,
	    "ajax": {
	      "url": './tanqueJornada/listar',
	      "type":constantes.PETICION_TIPO_GET,
	      "data": function (d) {
	          d.idOperacion = referenciaModulo.idOperacion;
	          d.filtroEstado = constantes.TIPO_JORNADA_LIQUIDADO
	          if(referenciaModulo.obj.vistaFechaJornada.text().length > 0){
	        	  d.filtroFechaDiaOperativo = referenciaModulo.obj.vistaFechaJornada.text();
	          }else{
	        	  d.filtroFechaDiaOperativo = '01/01/2090';
	          }
	      }
	    },
	    "columns": referenciaModulo.columnasGrillaDetalle,
	    "columnDefs": referenciaModulo.definicionColumnasDetalle
		});	
	};

moduloPlanificacion.prototype.inicializarFormularioPrincipal= function(){  
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

moduloPlanificacion.prototype.activarBotones=function(){
  this.obj.btnModificar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnModificarEstado.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVer.removeClass(constantes.CSS_CLASE_DESHABILITADA);
};

moduloPlanificacion.prototype.desactivarBotones=function(){
  this.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);
  this.obj.btnModificar.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnAnular.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnNotificarUnaPlanificacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnModificarEstado.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVer.addClass(constantes.CSS_CLASE_DESHABILITADA);
};

moduloPlanificacion.prototype.listarRegistros = function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("listarRegistros");  
  this.obj.datDiaOperativoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	 	
};

moduloPlanificacion.prototype.actualizarBandaInformacion=function(tipo, mensaje){
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

moduloPlanificacion.prototype.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.idRegistro},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
	    			referenciaModulo.llenarFormulario(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_VER){
	    			referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorVista.hide();
	    		}          
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
        if (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
          referenciaModulo.obj.ocultaContenedorFormulario.hide();
        } else if (referenciaModulo.modoEdicion == constantes.MODO_VER){
          referenciaModulo.obj.ocultaContenedorVista.hide();
        } 
	    }
	});
};

moduloPlanificacion.prototype.recuperarDetallePlanificacion= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);

     var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
     var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
     var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);

	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './planificacion/recuperarDetallePlanificacion', 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {
	    	filtroOperacion: referenciaModulo.obj.filtroOperacion.val(),
	    	filtroFechaInicio: fechaInicio,
	    	filtroFechaFinal:	fechaFinal
	    	},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    		referenciaModulo.obj.ocultaContenedorTabla.hide();
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if (referenciaModulo.modoEdicion == constantes.MODO_DETALLE_PLANIFICACION){
	    			if(respuesta.contenido.carga.length > 0){
	    				referenciaModulo.obj.ocultaContenedorTabla.hide();
	    				referenciaModulo.obj.cntTabla.hide();
	    				referenciaModulo.obj.ocultaContenedorDetalle.show();
	    				referenciaModulo.obj.cntDetallePlanificacion.show();
	    				referenciaModulo.llenarDetallesPlanificaciones(respuesta.contenido.carga);
	    			} else {
	    				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"No se encontraron resultados." );
	    				referenciaModulo.obj.ocultaContenedorTabla.hide();
	    			}
	    			referenciaModulo.obj.ocultaContenedorDetalle.hide();
	    		}
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
        referenciaModulo.obj.ocultaContenedorDetalle.hide();
        referenciaModulo.obj.ocultaContenedorTabla.hide();
	    }
	});
};

moduloPlanificacion.prototype.verRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.idRegistro},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} 	else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    		referenciaModulo.obj.ocultaContenedorVista.show();
    		}
	    },			    		    
	    error: function() {
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	    	referenciaModulo.obj.ocultaContenedorVista.show();
	    }
	});
};

moduloPlanificacion.prototype.guardarRegistro= function(){  
	var referenciaModulo = this;
   referenciaModulo.mostrarDepuracion("guardarRegistro");
	if (referenciaModulo.obj.frmPrincipal.valid()){
    referenciaModulo.obj.ocultaContenedorFormulario.show();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		var eRegistro = referenciaModulo.recuperarValores();
		$.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: referenciaModulo.URL_GUARDAR, 
      contentType: referenciaModulo.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),	
      success: function(respuesta) {
        if (!respuesta.estado) {
          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          referenciaModulo.iniciarListado(respuesta.mensaje);
        }
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      },			    		    
      error: function() {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
      }
		});
	} else {
    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	};
};

moduloPlanificacion.prototype.iniciarListado= function(mensaje){
	var referenciaModulo = this;
	try {
		referenciaModulo.listarRegistros();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,mensaje);
		referenciaModulo.obj.cntFormulario.hide();	
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
		console.log(error);
    referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloPlanificacion.prototype.actualizarRegistro= function(){
  //Ocultar alertas de mensaje
  var referenciaModulo = this;
  if (referenciaModulo.obj.frmPrincipal.valid()){
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    referenciaModulo.obj.ocultaContenedorFormulario.show();
    var eRegistro = referenciaModulo.recuperarValores();
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: referenciaModulo.URL_ACTUALIZAR, 
      contentType: referenciaModulo.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),	
      success: function(respuesta) {
        if (!respuesta.estado) {
          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } 	else {		    				    			    		
          referenciaModulo.iniciarListado(respuesta.mensaje);
        }
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      },			    		    
      error: function() {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      }
    });
  } else {

  };
};

moduloPlanificacion.prototype.iniciarContenedores = function(){
  var referenciaModulo = this;
  try {
    referenciaModulo.obj.cntFormulario.hide();	
    referenciaModulo.protegeFormulario(false);
    referenciaModulo.obj.cntTabla.show();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};

moduloPlanificacion.prototype.solicitarActualizarEstado= function(){
	var referenciaModulo = this;
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};	
};

moduloPlanificacion.prototype.actualizarEstadoRegistro= function(){
  var eRegistro = {};
  var referenciaModulo=this;
	referenciaModulo.obj.frmConfirmarModificarEstado.modal("hide");
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  if (referenciaModulo.estadoRegistro==constantes.ESTADO_ACTIVO){
    referenciaModulo.estadoRegistro=constantes.ESTADO_INACTIVO;
  } else {
    referenciaModulo.estadoRegistro=constantes.ESTADO_ACTIVO;
  }

  try {
    eRegistro.id = parseInt(referenciaModulo.idRegistro);
    eRegistro.estado = parseInt(referenciaModulo.estadoRegistro);
  }  catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  }

  $.ajax({
    type: constantes.PETICION_TIPO_POST,
    url: referenciaModulo.URL_ACTUALIZAR_ESTADO, 
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: JSON.stringify(eRegistro),	
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {		    				    			    		
        referenciaModulo.listarRegistros();
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        referenciaModulo.obj.cntFormulario.hide();	
        referenciaModulo.obj.cntTabla.show();
      }
    },			    		    
    error: function() {
      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
	};
	
moduloPlanificacion.prototype.recuperaUltimoDiaOperativoPorOperacion= function(){
//Implementar en cada caso
};

moduloPlanificacion.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloPlanificacion.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloPlanificacion.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloPlanificacion.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloPlanificacion.prototype.grillaDespuesSeleccionar= function(indice){
//TODO implementar
};

moduloPlanificacion.prototype.despuesListarRegistros=function(){
//Implementar en cada caso
};

moduloPlanificacion.prototype.validaPermisos= function(){
  var referenciaModulo = this;
  try{
  console.log("Validando permiso para: " + referenciaModulo.descripcionPermiso);
  referenciaModulo.obj.ocultaContenedorTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.VERIFICANDO_PERMISOS);
	  $.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './validaPermisos/validar',
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: { permiso : referenciaModulo.descripcionPermiso, },	
	    success: function(respuesta) {
	      console.log("respuesta.estado " + respuesta.estado);
	      if(!respuesta.estado){
	    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
		      referenciaModulo.obj.ocultaContenedorTabla.hide();
	      } else {
		      if (referenciaModulo.descripcionPermiso == 'CREAR_PLANIFICACION'){
		    	  referenciaModulo.iniciarAgregar();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_PLANIFICACION'){
		    	  referenciaModulo.iniciarModificar();
		      } else if (referenciaModulo.descripcionPermiso == 'ANULAR_PLANIFICACION'){
		    	  referenciaModulo.solicitarAnularEstado();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_PLANIFICACION'){
		    	  referenciaModulo.iniciarVer();
		      } else if (referenciaModulo.descripcionPermiso == 'URL_RECUPERAR_PLANIFICACION_COMPLETA'){
		    	  referenciaModulo.iniciarDetalle();
		      } else if (referenciaModulo.descripcionPermiso == 'NOTIFICAR_UNA_PLANIFICACION'){
		    	  referenciaModulo.abrirVentanaCorreoUnaPlanificacion();
		      } else if (referenciaModulo.descripcionPermiso == 'NOTIFICAR_PLANIFICACION'){
		    	  referenciaModulo.abrirVentanaCorreo();
		      } 
	      }
	      referenciaModulo.obj.ocultaContenedorTabla.hide();
	    },
	    error: function() {
	    	referenciaModulo.obj.ocultaContenedorTabla.hide();
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	    }
	  });
  } catch(error){
	  referenciaModulo.obj.ocultaContenedorTabla.hide();
	  referenciaModulo.mostrarDepuracion(error.message);
  };
};