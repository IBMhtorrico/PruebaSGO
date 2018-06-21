function moduloDespacho (){
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
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};
  this.TOPES_PAGINACION_DESPACHO = constantes.TOPES_PAGINACION_DESPACHO;
  this.NUMERO_REGISTROS_PAGINA_DESPACHO = constantes.NUMERO_REGISTROS_PAGINA_DESPACHO;
  this.TOPES_PAGINACION_DESPACHO_ARCHIVO = constantes.TOPES_PAGINACION_DESPACHO_ARCHIVO;
  this.NUMERO_REGISTROS_PAGINA_DESPACHO_ARCHIVO = constantes.NUMERO_REGISTROS_PAGINA_DESPACHO_ARCHIVO;
  
  //para la grilla de la jornada
  this.columnasGrillaJornada={};
  this.definicionColumnasJornada=[];
  this.ordenGrillaJornada=[[ 1, 'asc' ]];
  this.columnasGrillaJornada=[{ "data": null} ];//Target 0
  this.definicionColumnasJornada=[{ "targets": 0, 
	  								"searchable": false, 
	  								"orderable": false, 
	  								"visible":false, 
	  								"render": function ( datos, tipo, fila, meta ) {
											    var configuracion =meta.settings;
											    return configuracion._iDisplayStart + meta.row + 1;
											  }
  }];
  
  //para la grilla del despacho carga
  this.columnasGrillaDespachoCarga={};
  this.definicionColumnasDespachoCarga=[];
  this.ordenGrillaDespachoCarga=[[ 1, 'asc' ]];
  this.columnasGrillaDespachoCarga=[{ "data": null} ];//Target 0
  this.definicionColumnasDespachoCarga=[{ "targets": 0, 
	  									  "searchable": false, 
	  									  "orderable": false, 
	  									  "visible":false, 
	  									  "render": function ( datos, tipo, fila, meta ) {
													    var configuracion =meta.settings;
													    return configuracion._iDisplayStart + meta.row + 1;
												    }
  }];  
  
  //para la grilla del despacho
  this.columnasGrillaDespacho={};
  this.definicionColumnasDespacho=[];
  this.columnasGrillaDespacho=[{ "data": null} ];//Target 0
  this.definicionColumnasDespacho=[{  "targets": 0, 
	  								  "searchable": false, 
	  								  "orderable": false, 
	  								  "visible":false, 
	  								  "render": function ( datos, tipo, fila, meta ) {
													    var configuracion =meta.settings;
													    return configuracion._iDisplayStart + meta.row + 1;
												    }
  }];  
  
};

moduloDespacho.prototype.mostrarDepuracion=function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloDespacho.prototype.mostrarErrorServidor=function(xhr,estado,error){
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

moduloDespacho.prototype.inicializar=function(){
	this.configurarAjax();
  this.inicializarControlesGenericos();
  this.inicializarGrillaJornada();
  this.inicializarCampos();
  this.inicializarGrillaDespachoCarga();
  this.inicializarGrillaDespacho();
  this.inicializarFormularioPrincipal();
};

moduloDespacho.prototype.configurarAjax=function(){
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

moduloDespacho.prototype.resetearFormulario= function(){
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

moduloDespacho.prototype.validaFormularioXSS= function(formulario){
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

moduloDespacho.prototype.inicializarControlesGenericos=function(){
  this.mostrarDepuracion("inicializarControlesGenericos");
  var referenciaModulo=this; 
  //general
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  
  this.obj.tituloSeccion=$("#tituloSeccion");
  this.obj.frmConfirmarAnularRegistro=$("#frmConfirmarAnularRegistro");
  this.obj.bandaInformacion=$("#bandaInformacion");
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");
  //formularios
  this.obj.frmPrincipal = $("#frmPrincipal");
  this.obj.frmImportacion = $("#frmImportacion");
  this.obj.frmConfirmarAnularEstado = $("#frmConfirmarAnularEstado");
  //filtros de busqueda
  this.obj.filtroOperacion=$("#filtroOperacion");
  this.obj.filtroEstacion=$("#filtroEstacion");
  this.obj.filtroJornada=$("#filtroJornada");
  this.obj.filtroEstadoJornada=$("#filtroEstadoJornada");
  //tablas
  this.obj.tablaJornada=$('#tablaJornada');
  this.obj.tablaDespachoCarga=$('#tablaDespachoCarga');
  this.obj.tablaDespacho=$('#tablaDespacho');
  //contenedores
  this.obj.cntTabla=$("#cntTabla");
  this.obj.cntDetalleDespacho=$("#cntDetalleDespacho");
  this.obj.cntImportacion=$("#cntImportacion");
  this.obj.cntVistaImportacion=$("#cntVistaImportacion");  
  this.obj.cntFormulario=$("#cntFormulario");
  this.obj.cntVistaDespacho=$("#cntVistaDespacho");
  //oculta contenedores
  this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
  this.obj.ocultaContenedorDetalleDespacho=$("#ocultaContenedorDetalleDespacho");
  this.obj.ocultaContenedorImportacion=$("#ocultaContenedorImportacion");
  this.obj.ocultaContenedorVistaImportacion=$("#ocultaContenedorVistaImportacion");
  this.obj.ocultaContenedorFormulario=$("#ocultaContenedorFormulario");
  this.obj.ocultaContenedorVistaDespacho=$("#ocultaContenedorVistaDespacho");
  //Botones	
  this.obj.btnFiltrar=$("#btnFiltrar");
  this.obj.btnDetalle=$("#btnDetalle");
  this.obj.btnRegresar=$("#btnRegresar");
  this.obj.btnImportar=$("#btnImportar");
  this.obj.btnVerImportacion=$("#btnVerImportacion");
  this.obj.btnAgregarDespacho=$("#btnAgregarDespacho");
  this.obj.btnModificarDespacho=$("#btnModificarDespacho");
  this.obj.btnAnularDespacho=$("#btnAnularDespacho");
  this.obj.btnConfirmarAnular=$("#btnConfirmarAnular");
  this.obj.btnVerDespacho=$("#btnVerDespacho");
  this.obj.btnGuardarImportacion=$("#btnGuardarImportacion");
  this.obj.btnCancelarImportacion=$("#btnCancelarImportacion");
  this.obj.btnExaminar=$("#btnExaminar");
  this.obj.btnCerrarVistaImportacion=$("#btnCerrarVistaImportacion");
  this.obj.btnGuardarDespacho=$("#btnGuardarDespacho");
  this.obj.btnCancelarGuardarDespacho=$("#btnCancelarGuardarDespacho");
  this.obj.btnConfirmarAnularRegistro=$("#btnConfirmarAnularRegistro");
  this.obj.btnCerrarVistaDespacho=$("#btnCerrarVistaDespacho");
  //para guardar los datos de la jornada seleccionada
  this.obj.jornadaSeleccionado=$("#jornadaSeleccionado");
  this.obj.idClienteSeleccionado=$("#idClienteSeleccionado");
  this.obj.idOperacionSeleccionado =$("#idOperacionSeleccionado");
  this.obj.operacionSeleccionado=$("#operacionSeleccionado");
  this.obj.idEstacionSeleccionado =$("#idEstacionSeleccionado");
  this.obj.estacionSeleccionado =$("#estacionSeleccionado");
  this.obj.idJornadaSeleccionado =$("#idJornadaSeleccionado");
  this.obj.fechaJornadaSeleccionado=$("#fechaJornadaSeleccionado");  
  this.obj.idRegistroImportacion=$("#idRegistroImportacion");

  //estos valores para hacer los filtros de los listados	
  this.obj.txtFiltro=$("#txtFiltro");
  this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
  this.obj.cmpFechaInicial=$("#cmpFechaInicial");
  this.obj.cmpFechaFinal=$("#cmpFechaFinal");
  this.obj.filtroFechaJornada=$("#filtroFechaJornada");
  this.obj.detalleFechaJornada=$("#detalleFechaJornada");
  this.obj.cmpFiltroUsuario=$("#cmpFiltroUsuario");
  this.obj.cmpFiltroTabla=$("#cmpFiltroTabla");
  this.obj.cmpFiltroTipoFecha=$("#cmpFiltroTipoFecha");	
  this.obj.idJornada=$("#idJornada");
  //
  this.obj.btnFiltrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
	  referenciaModulo.listarRegistros();
  });
  
  this.obj.btnDetalle.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'LEER_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonDetalle();
  });
  
  this.obj.btnImportar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'IMPORTAR_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.validaImportar("importar");
	//referenciaModulo.botonImportar();
  });
  
  this.obj.btnVerImportacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'VER_IMPORTACION';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonVerImportacion();
  });
  
  this.obj.btnAgregarDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'CREAR_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.validaImportar("agregar"); 
	  //referenciaModulo.botonAgregarDespacho();
  });
  
  this.obj.btnModificarDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ACTUALIZAR_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonModificarDespacho();
  });
  
  this.obj.btnAnularDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ANULAR_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonAnularDespacho();		
  });
  
  this.obj.btnVerDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'RECUPERAR_DESPACHO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonVerDespacho();		
  });
  
  this.obj.btnRegresar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonRegresar();	
  });

  this.obj.btnGuardarImportacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonGuardarImportacion();		
  });
 
  this.obj.btnCancelarImportacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarImportacion();		
  });
  
  this.obj.btnExaminar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonExaminar();		
  });
  
  this.obj.btnCerrarVistaImportacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCerrarVistaImportacion();
  });

  this.obj.btnGuardarDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  if(referenciaModulo.validarFormulario()){
		  referenciaModulo.botonGuardarDespacho();  
	  } 
  });
  
  this.obj.btnCancelarGuardarDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarGuardarDespacho();		
  });
  
  this.obj.btnConfirmarAnularRegistro.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonConfirmarAnularRegistro();		
  });
  
  this.obj.btnCerrarVistaDespacho.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCerrarVistaDespacho();		
  });
};

moduloDespacho.prototype.botonDetalle = function(){
  var referenciaModulo = this;
  try {
   if(referenciaModulo.obj.filtroEstacion.val()>0){
	    referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.cntDetalleDespacho.show();
	    referenciaModulo.obj.ocultaContenedorDetalleDespacho.show();
	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    referenciaModulo.datosCabecera();//esto para llenar la cabecera
	    this.obj.dataDespachoCargaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	    this.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	    //this.inicializarGrillaDespachoCarga();
	    //this.inicializarGrillaDespacho();
	  	referenciaModulo.desactivarBotones();
	  	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
   }else{
	   referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Debe seleccionar la Estaci\u00f3n");
   }

  } catch(error){
    console.log(error.message);
  }
};

moduloDespacho.prototype.botonRegresar = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_LISTAR_DESPACHO;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_DETALLE_DESPACHO);
		referenciaModulo.obj.cntTabla.show();
		referenciaModulo.obj.cntDetalleDespacho.hide();
		referenciaModulo.obj.cntImportacion.hide();
		referenciaModulo.obj.cntVistaImportacion.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaDespacho.hide();
		referenciaModulo.obj.btnDetalle.removeClass("disabled");
		this.listarRegistros();
	} catch(error){
	  console.log(error.message);
	}
};
moduloDespacho.prototype.validaImportar = function(accion){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorDetalleDespacho.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './turno/listar', 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {idJornada:referenciaModulo.obj.idJornadaSeleccionado,
	    	   filtroEstado:constantes.TIPO_TURNO_ABIERTO},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {
	    		if(respuesta.contenido.carga==null || respuesta.contenido.carga.length==0){
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No existe un turno abierto.");
	    		}else{
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "Validacion de existencia de turno abierto exitosa.");
	    			if(accion == "importar"){
	    				referenciaModulo.botonImportar();
	    			}else if(accion == "agregar"){
	    				referenciaModulo.botonAgregarDespacho();
	    			}	    			
	    		}
	    		
    		}
	    	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
        	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
	   }
	});
};

moduloDespacho.prototype.recuperarTurno = function(accion){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorDetalleDespacho.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './turno/listar', 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {idJornada:referenciaModulo.obj.idJornadaSeleccionado,
	    	   filtroFechaJornada:referenciaModulo.obj.fechaJornadaSeleccionado,
	    	   filtroEstado:constantes.TIPO_TURNO_ABIERTO},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {
	    		if(respuesta.contenido.carga==null || respuesta.contenido.carga.length==0){
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "No existe un turno abierto.");
	    		}else{
	    			console.log(respuesta.contenido.carga[0]);
	    			var registro = respuesta.contenido.carga[0];
	    			console.log(registro.fechaHoraApertura);
	    			referenciaModulo.obj.cmpHoraAperturaTurno.text(utilitario.formatearTimestampToStringSoloHora(registro.fechaHoraApertura));
	    		}
	    		
    		}
	    	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
        	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
	   }
	});
};

moduloDespacho.prototype.botonImportar = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion=constantes.MODO_FORMULARIO_IMPORTACION;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_IMPORTA_DESPACHO);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleDespacho.hide();
		referenciaModulo.obj.cntImportacion.show();
		referenciaModulo.obj.cntVistaImportacion.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaDespacho.hide();
		referenciaModulo.obj.frmImportacion[0].reset();
	    var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
	    elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, 0);
	    elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, "SELECCIONAR...");
	    referenciaModulo.obj.cmpOperarioImportacion.empty().append(elemento1).val(0).trigger('change');	  
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonVerImportacion = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion=constantes.MODO_VER_IMPORTACION;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_VER_IMPORTACION_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleDespacho.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.show();
	    referenciaModulo.obj.ocultaContenedorVistaImportacion.show();
	    referenciaModulo.recuperarRegistroImportacion();
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonCerrarVistaImportacion = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.cntDetalleDespacho.show();
	    this.obj.dataDespachoCargaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	    this.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	  	referenciaModulo.desactivarBotones();
	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonAgregarDespacho = function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
	    referenciaModulo.modoEdicion = constantes.MODO_FORMULARIO_NUEVO_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.cntDetalleDespacho.hide();
	    this.limpiarFormularioPrincipal();
	    referenciaModulo.datosCabecera();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error) {
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonModificarDespacho = function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
	    referenciaModulo.modoEdicion=constantes.MODO_FORMULARIO_MODIFICAR_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleDespacho.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.ocultaContenedorFormulario.show();
	    referenciaModulo.obj.cntFormulario.show();
	    this.limpiarFormularioPrincipal();
	    referenciaModulo.datosCabecera();
	    referenciaModulo.recuperarRegistro();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
    } catch(error){
    	console.log(error.message);
    }
};

moduloDespacho.prototype.botonAnularDespacho = function(){
	var referenciaModulo = this;
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarAnularEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloDespacho.prototype.botonConfirmarAnularRegistro = function(){
  var eRegistro = {};
  var referenciaModulo=this;
  referenciaModulo.obj.frmConfirmarAnularEstado.modal("hide");
  referenciaModulo.obj.ocultaContenedorDetalleDespacho.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  eRegistro.id = parseInt(referenciaModulo.idDespacho);
  eRegistro.estado = parseInt(constantes.ESTADO_ANULADO);
  try{
  $.ajax({
    type: constantes.PETICION_TIPO_POST,
    url: referenciaModulo.URL_ACTUALIZAR_ESTADO, 
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: JSON.stringify(eRegistro),	
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {	
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
    	referenciaModulo.obj.dataDespachoCargaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
    	referenciaModulo.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
  	  	referenciaModulo.desactivarBotones();
  	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
      }
      referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
    },			    		    
    error: function() {
      referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
  } catch(error){
	  referenciaModulo.mostrarDepuracion(error.message);
  }  
};

moduloDespacho.prototype.botonVerDespacho = function(){
	var referenciaModulo = this;
	referenciaModulo.modoEdicion = constantes.MODO_VER_DESPACHO;
    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_VER_DESPACHO);
    referenciaModulo.obj.cntTabla.hide();
    referenciaModulo.obj.cntDetalleDespacho.hide();
    referenciaModulo.obj.cntImportacion.hide();
    referenciaModulo.obj.cntVistaImportacion.hide();
    referenciaModulo.obj.cntFormulario.hide();
    referenciaModulo.obj.ocultaContenedorVistaImportacion.show();
    referenciaModulo.obj.cntVistaDespacho.show();
    referenciaModulo.recuperarRegistro();
};

moduloDespacho.prototype.botonCerrarVistaDespacho = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.cntDetalleDespacho.show();
	    this.obj.dataDespachoCargaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	    this.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	  	referenciaModulo.desactivarBotones();
	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	} catch(error){
	  console.log(error.message);
	}
};
moduloDespacho.prototype.actualizarDetalle = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    //referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    //referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.cntDetalleDespacho.show();
	    this.obj.dataDespachoCargaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	    this.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	  	referenciaModulo.desactivarBotones();
	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonCancelarImportacion = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleDespacho.show();
		referenciaModulo.obj.cntImportacion.hide();
		referenciaModulo.obj.cntVistaImportacion.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaDespacho.hide();
		referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonGuardarDespacho = function(){
	var referenciaModulo = this;
	try {
		if (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_NUEVO_DESPACHO){
			referenciaModulo.guardarRegistro();
		} else if  (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_MODIFICAR_DESPACHO){
			referenciaModulo.actualizarRegistro();
		}  
	} catch(error){
	  console.log(error.message);
	}
};

moduloDespacho.prototype.botonCancelarGuardarDespacho = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.INICIAR_DETALLE_DESPACHO);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleDespacho.show();
		referenciaModulo.obj.cntImportacion.hide();
		referenciaModulo.obj.cntVistaImportacion.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaDespacho.hide();
		referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	} catch(error){
	  console.log(error.message);
	}
};

//para el listado de Jornadas
moduloDespacho.prototype.llamadaAjaxGrillaJornada=function(e,configuracion,json){
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

moduloDespacho.prototype.inicializarGrillaJornada=function(){
  //Nota no retornar el objeto solo manipular directamente
	//Establecer grilla y su configuracion
  var referenciaModulo=this;
  this.obj.tablaJornada.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
	   referenciaModulo.llamadaAjaxGrillaJornada(e,configuracion,json);
  });
  
  this.obj.tablaJornada.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
    //Se ejecuta antes de cualquier llamada ajax
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
    if (referenciaModulo.estaCargadaInterface==true){
    	referenciaModulo.obj.ocultaContenedorTabla.hide();
    }
  });

  this.obj.tablaJornada.on(constantes.DT_EVENTO_PAGINACION, function () {
  //Se ejecuta cuando se hace clic en boton de paginacion
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
  });

  this.obj.tablaJornada.on(constantes.DT_EVENTO_ORDENACION, function () {
  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
  });

  this.obj.datJornadaAPI= this.obj.tablaJornada.DataTable({
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
      "url": './jornada/listar',
      "type":constantes.PETICION_TIPO_GET,
      "data": function (d) {
    	  console.log("entra en datJornadaAPI");
        var indiceOrdenamiento = d.order[0].column;
        d.registrosxPagina =  d.length; 
        d.inicioPagina = d.start; 
        d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
        d.sentidoOrdenamiento=d.order[0].dir;
        d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
        d.filtroEstacion = referenciaModulo.obj.filtroEstacion.val();
        var rangoFecha = referenciaModulo.obj.filtroFechaJornada.val().split("-");
        var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
        var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
        d.filtroFechaInicio= fechaInicio;	
        d.filtroFechaFinal = fechaFinal;	
      }
    },
    "columns": referenciaModulo.columnasGrillaJornada,
    "columnDefs": referenciaModulo.definicionColumnasJornada
    //"order": referenciaModulo.ordenGrilla
	});	
	
	$('#tablaJornada tbody').on(referenciaModulo.NOMBRE_EVENTO_CLICK, 'tr', function () {
		if (referenciaModulo.obj.datJornadaAPI.data().length>0){
		    if ( $(this).hasClass('selected') ) {
		      $(this).removeClass('selected');
		    } else {
		      referenciaModulo.obj.datJornadaAPI.$('tr.selected').removeClass('selected');
		      $(this).addClass('selected');
		    }
		    var indiceFila = referenciaModulo.obj.datJornadaAPI.row( this ).index();
		  //	var idRegistro = referenciaModulo.obj.datJornadaAPI.cell(indiceFila,1).data();
		    referenciaModulo.obj.filtroJornada.val(referenciaModulo.obj.datJornadaAPI.cell(indiceFila,1).data());
		  	referenciaModulo.obj.idJornadaSeleccionado = referenciaModulo.obj.datJornadaAPI.cell(indiceFila,1).data();
		  	referenciaModulo.obj.idEstacionSeleccionado =referenciaModulo.obj.datJornadaAPI.cell(indiceFila,2).data();
		  	referenciaModulo.obj.estacionSeleccionado =referenciaModulo.obj.datJornadaAPI.cell(indiceFila,3).data();
		  	referenciaModulo.obj.fechaJornadaSeleccionado=referenciaModulo.obj.datJornadaAPI.cell(indiceFila,4).data();
		  	referenciaModulo.obj.filtroEstadoJornada = referenciaModulo.obj.datJornadaAPI.cell(indiceFila,8).data();
		  	if(referenciaModulo.obj.filtroEstadoJornada == constantes.TIPO_JORNADA_ABIERTO || referenciaModulo.obj.filtroEstadoJornada == constantes.TIPO_JORNADA_REGISTRADO){
		  		referenciaModulo.obj.btnImportar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  		referenciaModulo.obj.btnAgregarDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  	}else{
		  		referenciaModulo.obj.btnImportar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  		referenciaModulo.obj.btnAgregarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  	}
		  	console.log(referenciaModulo.obj.filtroEstadoJornada);
			console.log("referenciaModulo.obj.datJornadaAPI.cell(indiceFila,8).data()" + referenciaModulo.obj.datJornadaAPI.cell(indiceFila,8).data());
			referenciaModulo.obj.btnDetalle.removeClass("disabled");
		}
	});
};

//esto para la grilla de despacho carga
moduloDespacho.prototype.llamadaAjaxGrillaDespachoCarga=function(e,configuracion,json){
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

moduloDespacho.prototype.inicializarGrillaDespachoCarga=function(){
	  //Nota no retornar el objeto solo manipular directamente
		//Establecer grilla y su configuracion
	  var referenciaModulo=this;
	  this.obj.tablaDespachoCarga.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrillaDespachoCarga(e,configuracion,json);
	  });

	  this.obj.tablaDespachoCarga.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    if (referenciaModulo.estaCargadaInterface==true){
	    //referenciaModulo.ocultaContenedorDetalleDespacho.show();
	    }
	  });

	  this.obj.tablaDespachoCarga.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaDespachoCarga.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });

	  this.obj.dataDespachoCargaAPI= this.obj.tablaDespachoCarga.DataTable({
	    processing: true,
	    responsive: true,
	    dom: constantes.DT_ESTRUCTURA,
	    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA_DESPACHO_ARCHIVO,
	    lengthMenu: referenciaModulo.TOPES_PAGINACION_DESPACHO_ARCHIVO,
	    language: {
	      url: referenciaModulo.URL_LENGUAJE_GRILLA
	    },
	    "serverSide": true,
	    "ajax": {
	      "url": './despachoCarga/listar',
	      "type":constantes.PETICION_TIPO_GET,
	      "data": function (d) {
	    	  var indiceOrdenamiento = d.order[0].column;
	          d.registrosxPagina =  d.length; 
	          d.inicioPagina = d.start; 
	          d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	          d.sentidoOrdenamiento=d.order[0].dir;
	          d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
	          d.filtroEstacion = referenciaModulo.obj.filtroEstacion.val();	
	          var idJornada=0;
	          if(referenciaModulo.obj.idJornadaSeleccionado>0){
	        	  idJornada=referenciaModulo.obj.idJornadaSeleccionado;
	          }	          
	          d.idJornada = idJornada;
	          //d.idJornada = referenciaModulo.obj.idJornadaSeleccionado;	         
	      }
	    },
	    "columns": referenciaModulo.columnasGrillaDespachoCarga,
	    "columnDefs": referenciaModulo.definicionColumnasDespachoCarga
	    //"order": referenciaModulo.ordenGrilla
		});
	  
	  $('#tablaDespachoCarga tbody').on( 'click', 'tr', function () {
		  if (referenciaModulo.obj.dataDespachoCargaAPI.data().length > 0){
		  		 //var referenciaModulo = this;
			     if ( $(this).hasClass('selected') ) {
			            $(this).removeClass('selected');
			     } else {
			    	 referenciaModulo.obj.dataDespachoCargaAPI.$('tr.selected').removeClass('selected');
			     	$(this).addClass('selected');
			     }
				var indiceFilaDespachoCarga = referenciaModulo.obj.dataDespachoCargaAPI.row( this ).index();
				referenciaModulo.obj.idRegistroImportacion = referenciaModulo.obj.dataDespachoCargaAPI.cell(indiceFilaDespachoCarga, 1).data();
				referenciaModulo.obj.btnVerImportacion.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  }
	});
};


//esto para la grilla de despachos
moduloDespacho.prototype.llamadaAjaxGrillaDespacho=function(e,configuracion,json){
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

	moduloDespacho.prototype.inicializarGrillaDespacho=function(){
	  //Nota no retornar el objeto solo manipular directamente
		//Establecer grilla y su configuracion
	  var referenciaModulo=this;
	  this.obj.tablaDespacho.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrillaDespacho(e,configuracion,json);
	  });

	  this.obj.tablaDespacho.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    if (referenciaModulo.estaCargadaInterface==true){
	    //referenciaModulo.obj.ocultaContenedorTabla.show();
	    }
	  });

	  this.obj.tablaDespacho.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaDespacho.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });

	  this.obj.datDespachoAPI= this.obj.tablaDespacho.DataTable({
	    processing: true,
	    responsive: true,
	    dom: constantes.DT_ESTRUCTURA,
	    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA_DESPACHO,
	    lengthMenu: referenciaModulo.TOPES_PAGINACION_DESPACHO,
	    language: {
	      url: referenciaModulo.URL_LENGUAJE_GRILLA
	    },	    
	    "serverSide": true,
	    "ajax": {
	      "url": './despacho/listar',
	      "type":constantes.PETICION_TIPO_GET,
	      "data": function (d) {
	    	  var indiceOrdenamiento = d.order[0].column;
	          d.registrosxPagina =  d.length; 
	          d.inicioPagina = d.start; 
	          d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
	          d.sentidoOrdenamiento=d.order[0].dir;    	  
	          d.filtroOperacion = referenciaModulo.obj.filtroOperacion.val();
	          d.filtroEstacion = referenciaModulo.obj.filtroEstacion.val();
	          //d.idJornada = referenciaModulo.obj.filtroJornada.val();	          
	          var idJornada=0;
	          if(referenciaModulo.obj.idJornadaSeleccionado>0){
	        	  idJornada=referenciaModulo.obj.idJornadaSeleccionado;
	          }	          
	          d.idJornada = idJornada;	          
	          //d.filtroFechaJornada = referenciaModulo.obj.detalleFechaJornada.text();
	      }
	    },
	    "columns": referenciaModulo.columnasGrillaDespacho,
	    "columnDefs": referenciaModulo.definicionColumnasDespacho
		});	
	  
	  $('#tablaDespacho tbody').on( 'click', 'tr', function () {
		  if (referenciaModulo.obj.datDespachoAPI.data().length > 0){
			     if ( $(this).hasClass('selected') ) {
			            $(this).removeClass('selected');
			     } else {
			    	 referenciaModulo.obj.datDespachoAPI.$('tr.selected').removeClass('selected');
			     	$(this).addClass('selected');
			     }
				var indiceFilaDespacho = referenciaModulo.obj.datDespachoAPI.row( this ).index();
				referenciaModulo.idDespacho = referenciaModulo.obj.datDespachoAPI.cell(indiceFilaDespacho, 1).data();

				var estado = referenciaModulo.obj.datDespachoAPI.cell(indiceFilaDespacho, 3).data();

				referenciaModulo.obj.btnVerDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
				if(referenciaModulo.obj.filtroEstadoJornada ==3 || referenciaModulo.obj.filtroEstadoJornada == 4){
					referenciaModulo.obj.btnAgregarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
					referenciaModulo.obj.btnAnularDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
					referenciaModulo.obj.btnModificarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
				 } else {
					if(estado == constantes.ESTADO_ANULADO){
						referenciaModulo.obj.btnAnularDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
						referenciaModulo.obj.btnModificarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
					} else {
						referenciaModulo.obj.btnAnularDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
						referenciaModulo.obj.btnModificarDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
					}
				}
		  }

		});
	};

moduloDespacho.prototype.inicializarFormularioPrincipal= function(){  
  //Establecer validaciones del formulario    
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

moduloDespacho.prototype.activarBotones=function(){
  this.obj.btnModificarDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnAnularDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVerDespacho.removeClass(constantes.CSS_CLASE_DESHABILITADA);
};

moduloDespacho.prototype.desactivarBotones=function(){
  this.obj.btnDetalle.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnModificarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnAnularDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
  this.obj.btnVerDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
  if(this.obj.filtroEstadoJornada ==3 || this.obj.filtroEstadoJornada == 4){
	  this.obj.btnAgregarDespacho.addClass(constantes.CSS_CLASE_DESHABILITADA);
  }
 };

moduloDespacho.prototype.listarRegistros = function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("listarRegistros");  
  this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	 	
};

moduloDespacho.prototype.actualizarBandaInformacion=function(tipo, mensaje){
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

moduloDespacho.prototype.recuperarRegistroImportacion= function(){
	var referenciaModulo = this;
	console.log("referenciaModulo.obj.idRegistroImportacion " + referenciaModulo.obj.idRegistroImportacion);
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR_IMPORTACION, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.obj.idRegistroImportacion},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		console.log(respuesta.contenido.carga[0]);
	    		referenciaModulo.llenarDetallesImportacion(respuesta.contenido.carga[0]);
	    		referenciaModulo.obj.ocultaContenedorVistaImportacion.hide();
	    		//referenciaModulo.obj.ocultaContenedorFormulario.hide();
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
	    }
	});
};

moduloDespacho.prototype.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.idDespacho},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_MODIFICAR_DESPACHO){
	    			referenciaModulo.llenarFormulario(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_VER_DESPACHO){
	    			referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorVistaDespacho.hide();
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

moduloDespacho.prototype.verRegistro= function(){
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

moduloDespacho.prototype.guardarRegistro= function(){  
	var referenciaModulo = this;
   referenciaModulo.mostrarDepuracion("guardarRegistro");
   if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (referenciaModulo.obj.frmPrincipal.valid()){
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
	}
};

moduloDespacho.prototype.actualizarRegistro= function(){
  //Ocultar alertas de mensaje
  var referenciaModulo = this;
  if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (referenciaModulo.obj.frmPrincipal.valid()){
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

  }	
};

moduloDespacho.prototype.iniciarListado= function(mensaje){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_DESPACHO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESPACHO);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, mensaje);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntImportacion.hide();
	    referenciaModulo.obj.cntVistaImportacion.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.cntVistaDespacho.hide();
	    referenciaModulo.obj.cntDetalleDespacho.show();
	    referenciaModulo.obj.ocultaContenedorDetalleDespacho.show();
	    referenciaModulo.obj.btnVerImportacion.addClass(constantes.CSS_CLASE_DESHABILITADA);
	    referenciaModulo.datosCabecera();//esto para llenar la cabecera
	    referenciaModulo.obj.datDespachoAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	  	referenciaModulo.desactivarBotones();
	  	referenciaModulo.obj.ocultaContenedorDetalleDespacho.hide();
	} catch(error){
		console.log(error);
    referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloDespacho.prototype.iniciarContenedores = function(){
  var referenciaModulo = this;
  try {
   // referenciaModulo.obj.cntFormulario.hide();	
   // referenciaModulo.protegeFormulario(false);
    referenciaModulo.obj.cntTabla.show();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};

moduloDespacho.prototype.solicitarActualizarEstado= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloDespacho.prototype.actualizarEstadoRegistro= function(){
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
	
moduloDespacho.prototype.botonGuardarImportacion = function(){
//Implementar en cada caso
};	
moduloDespacho.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloDespacho.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloDespacho.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloDespacho.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloDespacho.prototype.grillaDespuesSeleccionar= function(indice){
//TODO implementar
};

moduloDespacho.prototype.despuesListarRegistros=function(){
//Implementar en cada caso
};

moduloDespacho.prototype.validarFormulario= function(){
	//Implementar en cada caso
};


moduloDespacho.prototype.validaPermisos= function(){
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
		      if (referenciaModulo.descripcionPermiso == 'LEER_DESPACHO'){
		    	  referenciaModulo.botonDetalle();
		      } else if (referenciaModulo.descripcionPermiso == 'IMPORTAR_DESPACHO'){
		    	  referenciaModulo.validaImportar("importar");
		      } else if (referenciaModulo.descripcionPermiso == 'VER_IMPORTACION'){
		    	  referenciaModulo.botonVerImportacion();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_DESPACHO'){
		    	  referenciaModulo.validaImportar("agregar");
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_DESPACHO'){
		    	  referenciaModulo.botonModificarDespacho();
		      } else if (referenciaModulo.descripcionPermiso == 'ANULAR_DESPACHO'){
		    	  referenciaModulo.botonAnularDespacho();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_DESPACHO'){
		    	  referenciaModulo.botonVerDespacho();
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