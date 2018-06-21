function moduloCierre (){
  this.obj={};
  this.NUMERO_REGISTROS_PAGINA = constantes.NUMERO_REGISTROS_PAGINA;
  this.TOPES_PAGINACION = constantes.TOPES_PAGINACION;
  this.URL_LENGUAJE_GRILLA = "tema/datatable/language/es-ES.json";
  this.URL_LISTAR_RESUMEN= '../admin/dia_operativo/resumen';
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
  this.idOperacionSeleccionada=0;
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
  
  this.ordenGrillaResumen=[[ 1, 'asc' ]];
  this.columnasGrillaResumen=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnasResumen=[{
    "targets": 0,
    "searchable": false,
    "orderable": false,      
    "visible":false,
    "render": function ( datos, tipo, fila, meta ) {
    var configuracion =meta.settings;
    return configuracion._iDisplayStart + meta.row + 1;
    }
  }]; 
};

moduloCierre.prototype.mostrarDepuracion=function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloCierre.prototype.mostrarErrorServidor=function(xhr,estado,error){
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

moduloCierre.prototype.inicializar=function(){
	this.configurarAjax();
  this.inicializarControlesGenericos();
  this.inicializarGrilla();
  this.inicializarGrillaResumen();
  this.inicializarFormularioPrincipal();
  this.inicializarCampos();
};

moduloCierre.prototype.configurarAjax=function(){
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

moduloCierre.prototype.resetearFormulario= function(){
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

moduloCierre.prototype.validaFormularioXSS= function(formulario){
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

moduloCierre.prototype.inicializarControlesGenericos=function(){
  var referenciaModulo=this; 
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  //CONTENEDORES
  this.obj.tituloSeccion=$("#tituloSeccion");
  this.obj.mensajeError=$("#mensajeError");
  this.obj.cntTabla=$("#cntTabla");
  this.obj.cntFormulario=$("#cntFormulario");
  this.obj.cntResumenCierre=$("#cntResumenCierre");
  this.obj.ocultaContenedorResumenCierre = $("#ocultaContenedorResumenCierre");
  //tabla principal
  this.obj.tablaPrincipal=$('#tablaPrincipal');
  this.obj.tablaResumenCierre=$("#tablaResumenCierre");
  //formularios
  this.obj.frmPrincipal = $("#frmPrincipal");
  this.obj.frmValidarAutorizacion = $("#frmValidarAutorizacion");
  this.obj.frmCerrarDiaOperativo = $("#frmCerrarDiaOperativo");
  this.obj.frmConfirmarModificarEstado=$("#frmConfirmarModificarEstado");	
  this.obj.frmAlertaValidacion=$("#frmAlertaValidacion");
  //botones
  this.obj.btnFiltrar=$("#btnFiltrar");
  this.obj.btnCerrar=$("#btnCerrar");
  this.obj.btnCambiarEstado=$("#btnCambiarEstado");
  this.obj.btnGuardarValidarAutorizacion=$("#btnGuardarValidarAutorizacion");
  this.obj.btnCancelarValidarAutorizacion=$("#btnCancelarValidarAutorizacion");
  this.obj.btnGuardarCambioEstado=$("#btnGuardarCambioEstado");
  this.obj.btnCancelarCambioEstado=$("#btnCancelarCambioEstado");  
  this.obj.btnSolicitarCerrarDiaOperativo=$("#btnSolicitarCerrarDiaOperativo");
  this.obj.btnConfirmarCerrarDiaOperativo=$("#btnConfirmarCerrarDiaOperativo");
  this.obj.btnCierraAlerta=$("#btnCierraAlerta");
  this.obj.btnExportarPDF = $("#btnExportarPDF");
  this.obj.btnExportarCSV = $("#btnExportarCSV");
  //titulo de las pantallas
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");	
  this.obj.cmpEstado=$("#cmpEstado");
  //Inicializar controles
  this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
  this.obj.ocultaContenedorFormulario=$("#ocultaContenedorFormulario");
  this.obj.ocultaContenedorVista=$("#ocultaContenedorVista");
  this.obj.bandaInformacion=$("#bandaInformacion");
  //Botones	
  this.obj.btnListar=$("#btnListar");
  this.obj.btnAgregar=$("#btnAgregar");
  this.obj.btnModificar=$("#btnModificar");
  this.obj.btnModificarEstado=$("#btnModificarEstado");	
  this.obj.btnConfirmarModificarEstado=$("#btnConfirmarModificarEstado");
  this.obj.btnVer=$("#btnVer");
  this.obj.btnConfirmarEliminar=$("#btnConfirmarEliminar");	
  this.obj.btnGuardar=$("#btnGuardar");
  this.obj.btnArribo=$("#btnArribo");
  this.obj.btnCancelarGuardar=$("#btnCancelarGuardar");
  this.obj.btnCerrarVista=$("#btnCerrarVista");
  this.obj.btnCancelarCierre=$("#btnCancelarCierre");
  this.obj.cmpEstado=$("#cmpEstado");
  //
  this.obj.cmpClienteOperacion= $("#cmpClienteOperacion");
  this.obj.cmpFechaOperativa= $("#cmpFechaOperativa");
  //estos valores para hacer los filtros de los listados	
  this.obj.filtroOperacion=$("#filtroOperacion");
  this.obj.filtroFechaPlanificada=$("#filtroFechaPlanificada");
  this.obj.btnCerrarDiaOperativo=$("#btnCerrarDiaOperativo");
  this.obj.btnVerResumen=$("#btnVerResumen");

  this.obj.btnVerResumen.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'LEER_CIERRE';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.mostrarResumenCierre();
  });  
  
  this.obj.btnCambiarEstado.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.descripcionPermiso = 'ACTUALIZAR_DIA_OPERATIVO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonCambiarEstado();
  });
  
  this.obj.btnSolicitarCerrarDiaOperativo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.descripcionPermiso = 'CERRAR_DIA_OPERATIVO';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.solicitarCierreDia();
  });
  
  this.obj.btnExportarPDF.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.descripcionPermiso = 'EXPORTAR_PDF';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.mostrarReporte(constantes.FORMATO_PDF);
  });
  
  this.obj.btnExportarCSV.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.descripcionPermiso = 'EXPORTAR_XLS';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.mostrarReporte(constantes.FORMATO_CSV);
  });

  this.obj.btnConfirmarCerrarDiaOperativo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
    referenciaModulo.confirmarCierreDia();
  });
  
  this.obj.btnCancelarCierre.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.mostrarDiaOperativo();
  });
  
  this.obj.btnFiltrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
	  referenciaModulo.listarRegistros();
  });
 
  this.obj.btnCerrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.solicitarActualizarEstado();
  });

  this.obj.btnCierraAlerta.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.botonCierraAlerta();
  });
  
  this.obj.btnGuardarValidarAutorizacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.botonGuardarValidarAutorizacion();
  });

  this.obj.btnCancelarValidarAutorizacion.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.botonCancelarValidarAutorizacion();
  });

  this.obj.btnGuardarCambioEstado.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.botonGuardarCambioEstado();
  });
  
  this.obj.btnCancelarCambioEstado.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.botonCancelarCambioEstado();
  });
  
};

moduloCierre.prototype.mostrarReporte= function(formato){
	var ref = this;
	window.open("./cierre/reporte?idDiaOperativo="+ref.idRegistro+"&formato="+formato);
};

moduloCierre.prototype.mostrarResumenCierre = function(){
 console.log("mostrarResumenCierre");
	var ref = this;
	ref.obj.cntTabla.hide();
	ref.obj.cntResumenCierre.show();
	ref.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DIA_OPERATIVO);  
	ref.obj.tituloSeccion.text("Detalle de Día Operativo");
	ref.nombreOperacion = ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-operacion');
	ref.nombreCliente = ref.obj.filtroOperacion.find("option:selected").attr('data-nombre-cliente');
	ref.obj.cmpClienteOperacion.val(ref.nombreCliente +" / "+ref.nombreOperacion);
	ref.obj.cmpFechaOperativa.val(utilitario.formatearFecha(ref.fechaOperativaSeleccionada)); 	
	ref.listarResumenCierre();
	ref.obtenerObservacion();
	if (ref.estadoDiaOperativo==constantes.ESTADO_CERRADO){
	  $("#btnCerrarDiaOperativo").addClass(constantes.CSS_CLASE_DESHABILITADA);
	} else {
	  $("#btnCerrarDiaOperativo").removeClass(constantes.CSS_CLASE_DESHABILITADA);
	}
};

moduloCierre.prototype.mostrarDiaOperativo= function(){
  var ref = this;	
  ref.obj.cntResumenCierre.hide();
  ref.obj.cntTabla.show();
};

moduloCierre.prototype.solicitarCierreDia= function(){
	var referenciaModulo=this;
	try {
		if (referenciaModulo.idEstadoSeleccionado!=constantes.DIA_OPERATIVO_ESTADO_CERRADO){
			if(!utilitario.validaCaracteresFormulario($("#cmpObservacionesCierre").val())){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
			} else if((referenciaModulo.existeObservacionesDescarga==true) && ($("#cmpObservacionesCierre").val().length==0)) {
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Falta ingresar una justificación.");
			} else {
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
				referenciaModulo.verificaCierreDia();
				//this.obj.frmCerrarDiaOperativo.modal("show");
			}
		} else if (referenciaModulo.idEstadoSeleccionado == constantes.DIA_OPERATIVO_ESTADO_CERRADO) {
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia ya ha sido cerrado previamente.");
		}		
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};
//
moduloCierre.prototype.confirmarCierreDia=function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.frmCerrarDiaOperativo.modal("hide");
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);  	  
    	  var eRegistroDiaOperativo = {};
    	  eRegistroDiaOperativo.id = parseInt(referenciaModulo.idRegistro);
    	  eRegistroDiaOperativo.justificacionCierre = $("#cmpObservacionesCierre").val();
    	  $.ajax({
		    type: constantes.PETICION_TIPO_POST,
		    url: referenciaModulo.URL_CERRAR_DIA, 
		    contentType: referenciaModulo.TIPO_CONTENIDO, 
		    data: JSON.stringify(eRegistroDiaOperativo),	
		    success: function(respuesta) {
		      if (!respuesta.estado) {
		        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		      } else {		    				    			    		
		        referenciaModulo.listarRegistros();
		        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		        referenciaModulo.obj.cntFormulario.hide();
		        referenciaModulo.obj.cntResumenCierre.hide();
		        referenciaModulo.obj.cntTabla.show();
		      }
		    },			    		    
		    error: function() {
		    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		    }
		    });	    
	};	


moduloCierre.prototype.verificaCierreDia=function(){
  var referenciaModulo=this;
  referenciaModulo.obj.frmCerrarDiaOperativo.modal("hide");
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);  
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: './cierre/verificaDiaOperativo', 
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: {idDiaOperativo:referenciaModulo.idRegistro,
    	   idOperacion:	referenciaModulo.idOperacionSeleccionada},	
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
        referenciaModulo.obj.ocultaContenedorFormulario.hide();
      } else {		  
    	  referenciaModulo.obj.frmCerrarDiaOperativo.modal("show");
      }
    },			    		    
    error: function() {
    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
};	

/*moduloCierre.prototype.cierraElDiaOperativo=function(){
  var referenciaModulo=this;
  var justificacion ="";
  var idDiaOperativo = 0;
  referenciaModulo.obj.frmCerrarDiaOperativo.modal("hide");
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);  
  try {
	  console.log("parseInt(referenciaModulo.idRegistro);   " + parseInt(referenciaModulo.idRegistro));
	  idDiaOperativo = parseInt(referenciaModulo.idRegistro);
    justificacion = $("#cmpObservacionesCierre").val();
  }  catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  }
  $.ajax({
    type: constantes.PETICION_TIPO_POST,
    url: referenciaModulo.URL_CERRAR_DIA, 
    //contentType: referenciaModulo.TIPO_CONTENIDO, 
    data: {idDiaOperativo:idDiaOperativo,justifificacion:justificacion},	
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {		    				    			    		
        referenciaModulo.listarRegistros();
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        referenciaModulo.obj.cntFormulario.hide();
        referenciaModulo.obj.cntResumenCierre.hide();
        referenciaModulo.obj.cntTabla.show();
      }
    },			    		    
    error: function() {
    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
};*/

moduloCierre.prototype.botonCambiarEstado= function(){  
	var referenciaModulo=this;
	referenciaModulo.mostrarDepuracion("botonCambiarEstado");
	try {
    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_CIERRE);
    //referenciaModulo.obj.cntTabla.hide();
    //referenciaModulo.obj.cntFormulario.show();
    referenciaModulo.obj.ocultaContenedorTabla.show();
    referenciaModulo.recuperarAutorizacionesXcodigoInterno();
    referenciaModulo.obj.frmValidarAutorizacion.modal("show");
    
    //limpiamos el formulario
    referenciaModulo.obj.cmpDescAutorizacion.val("");
    referenciaModulo.obj.cmpCodigoValidacion.val("");
    referenciaModulo.obj.cmpVigenciaHastaValidacion.val("");
    referenciaModulo.obj.cmpJustificacion.val("");
    
    var elemento = constantes.PLANTILLA_OPCION_SELECTBOX;
	elemento = elemento.replace(constantes.ID_OPCION_CONTENEDOR, "");
	elemento = elemento.replace(constantes.VALOR_OPCION_CONTENEDOR, "Seleccionar...");
	referenciaModulo.obj.cmpAprobador.empty().append(elemento).val("").trigger('change');
	referenciaModulo.obj.cmpAprobador.val("");

    //referenciaModulo.obj.ocultaContenedorFormulario.show();
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloCierre.prototype.botonCierraAlerta= function(){  
	var referenciaModulo=this;
	referenciaModulo.mostrarDepuracion("botonCierraAlerta");
	try {
    referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
    referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_AGREGAR_REGISTRO);
    referenciaModulo.obj.cntTabla.show();
    referenciaModulo.obj.frmAlertaValidacion.hide();
    referenciaModulo.obj.cntFormulario.hide();
    referenciaModulo.obj.frmValidarAutorizacion.modal("show");
    referenciaModulo.obj.ocultaContenedorTabla.show();
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloCierre.prototype.botonGuardarValidarAutorizacion= function(){
  var referenciaModulo=this;
  referenciaModulo.guardarValidacionAutorizacion();
};

moduloCierre.prototype.botonCancelarValidarAutorizacion=function(){
  var referenciaModulo=this;
  //referenciaModulo.resetearFormulario();
  //referenciaModulo.obj.cntFormulario.hide();
  referenciaModulo.obj.frmValidarAutorizacion.modal("hide");
  referenciaModulo.obj.ocultaContenedorTabla.hide();
  referenciaModulo.obj.cntTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.CANCELAR_OPERACION);
};

moduloCierre.prototype.botonGuardarCambioEstado=function(){
  var eRegistro = {};
  var referenciaModulo=this;
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  try {	  
    eRegistro.id = parseInt(referenciaModulo.idRegistro);
    eRegistro.estado = parseInt(referenciaModulo.obj.cmpEstado.val());
  }  catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  }
  $.ajax({
    type: constantes.PETICION_TIPO_POST,
    url: referenciaModulo.URL_ACTUALIZAR_ESTADO,   
    data: JSON.stringify(eRegistro),
    contentType: referenciaModulo.TIPO_CONTENIDO, 
    success: function(respuesta) {
      if (!respuesta.estado) {
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {		    				    			    		
        referenciaModulo.listarRegistros();
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        referenciaModulo.obj.cntFormulario.hide();	
        referenciaModulo.obj.cntTabla.show();
        referenciaModulo.obj.btnCerrar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		referenciaModulo.obj.btnCambiarEstado.addClass(constantes.CSS_CLASE_DESHABILITADA);
      }
    },			    		    
    error: function() {
    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
    }
    });
};

moduloCierre.prototype.botonCancelarCambioEstado=function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("botonCancelarCambioEstado");
  referenciaModulo.resetearFormulario();
  referenciaModulo.obj.cntFormulario.hide();
  referenciaModulo.obj.ocultaContenedorFormulario.hide();
  referenciaModulo.obj.cntTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
};

moduloCierre.prototype.llamadaAjaxGrilla=function(e,configuracion,json){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
  if (json.estado==true){
	console.log(json);
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

moduloCierre.prototype.inicializarGrilla=function(){
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
  
  moduloCierre.prototype.llamadaAjaxDiaOperativo = function(cfgDiaOperativo){
	  var referenciaModulo = this;
	  var rangoFecha = referenciaModulo.obj.filtroFechaPlanificada.val().split("-");
	  var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
	  var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
	  var indiceOrdenamiento = cfgDiaOperativo.order[0].column;
	  cfgDiaOperativo.registrosxPagina =  cfgDiaOperativo.length; 
	  cfgDiaOperativo.inicioPagina = cfgDiaOperativo.start; 
	  cfgDiaOperativo.campoOrdenamiento = cfgDiaOperativo.columns[indiceOrdenamiento].data;
	  cfgDiaOperativo.sentidoOrdenamiento = cfgDiaOperativo.order[0].dir;
	  cfgDiaOperativo.filtroOperacion = referenciaModulo.obj.filtroOperacion.val();
	  cfgDiaOperativo.filtroFechaInicio = fechaInicio;
	  cfgDiaOperativo.filtroFechaFinal = fechaFinal;
	};

  this.obj.datClienteApi= this.obj.tablaPrincipal.DataTable({
    "processing": true,
    "responsive": true,
    "dom": constantes.DT_ESTRUCTURA,
    "iDisplayLength": referenciaModulo.NUMERO_REGISTROS_PAGINA,
    "lengthMenu": referenciaModulo.TOPES_PAGINACION,
    "language": {
      "url": referenciaModulo.URL_LENGUAJE_GRILLA
    },
    "serverSide": true,
    "ajax": {
      "url": '../admin/dia_operativo/listarxdescarga',
      "type":constantes.PETICION_TIPO_GET,
      "data": function (cfgDiaOperativo) {
          referenciaModulo.llamadaAjaxDiaOperativo(cfgDiaOperativo);
        }
    },
    "columns": referenciaModulo.columnasGrilla,
    "columnDefs": referenciaModulo.definicionColumnas
	});	
	
	$('#tablaPrincipal tbody').on(referenciaModulo.NOMBRE_EVENTO_CLICK, 'tr', function () {
		if (referenciaModulo.obj.datClienteApi.data().length > 0){
		    if ( $(this).hasClass('selected') ) {
		        $(this).removeClass('selected');
		      } else {
		        referenciaModulo.obj.datClienteApi.$('tr.selected').removeClass('selected');
		        $(this).addClass('selected');
		      }
		  		var indiceFila = referenciaModulo.obj.datClienteApi.row( this ).index();
		  		var idRegistro = referenciaModulo.obj.datClienteApi.cell(indiceFila,1).data();
		      var fechaOperativa = referenciaModulo.obj.datClienteApi.cell(indiceFila,2).data();
		  		var estadoregistro = referenciaModulo.obj.datClienteApi.cell(indiceFila,9).data();
		  		referenciaModulo.idOperacionSeleccionada = referenciaModulo.obj.datClienteApi.cell(indiceFila,10).data();
		  		if(estadoregistro  == constantes.ESTADO_CERRADO){
		  			referenciaModulo.obj.btnCerrar.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  			referenciaModulo.obj.btnCambiarEstado.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  		} else {		  			
		  			referenciaModulo.obj.btnCerrar.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  			referenciaModulo.obj.btnCambiarEstado.addClass(constantes.CSS_CLASE_DESHABILITADA);
		  		}
		  		referenciaModulo.estadoDiaOperativo =estadoregistro ;
		  		referenciaModulo.obj.btnVerResumen.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		  		referenciaModulo.idRegistro = idRegistro;
		  		referenciaModulo.fechaOperativaSeleccionada = fechaOperativa;		
		  		referenciaModulo.grillaDespuesSeleccionar(indiceFila);
		}
	});
};

moduloCierre.prototype.ajaxGrillaResumen=function(e,configuracion,json){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
  referenciaModulo.existeObservacionesDescarga=false;
  if (json.estado==true){

    json.recordsTotal=json.contenido.totalRegistros;
    json.recordsFiltered=json.contenido.totalEncontrados;
    json.data= json.contenido.carga;
    var numeroRegistros = json.data.length;
    console.log(numeroRegistros);
    for(var indice=0;indice<numeroRegistros;indice++){
     console.log(json.data[indice].estadoDescarga);
    	if (json.data[indice].estadoDescarga==constantes.ESTADO_DESCARGA_OBSERVADO){
    		referenciaModulo.existeObservacionesDescarga=true;
    		console.log(referenciaModulo.existeObservacionesDescarga);
    		break;
    	}
    }
    console.log(referenciaModulo.existeObservacionesDescarga);
    // if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
    // referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    // }
  } else {
    json.recordsTotal=0;
    json.recordsFiltered=0;
    json.data= {};
    // if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
    // referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    // } else {

    // }
  }
  referenciaModulo.obj.ocultaContenedorResumenCierre.hide(); 
};


moduloCierre.prototype.cfgAjaxGrillaResumen = function(cfg){
	  var referenciaModulo = this;
    console.log(referenciaModulo.idRegistro);
    cfg.idDiaOperativo =referenciaModulo.idRegistro;
    cfg.sentidoOrdenamiento="ASC";
	};

moduloCierre.prototype.listarResumenCierre= function(){
  var ref=this;
  try {
    ref.obj.grillaResumenCierreApi.ajax.reload();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};


moduloCierre.prototype.obtenerObservacion= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: "./evento/recuperarXregistroYtipo", 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {IdRegistro:referenciaModulo.idRegistro,
	    	tipoRegistro:3},//3 dia_oper cierre	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} else {		 
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);	    		
	    			//referenciaModulo.obj.ocultaContenedorVista.hide();
	    		if(respuesta.contenido.carga.length>0){
	    			$("#cmpObservacionesCierre").val(respuesta.contenido.carga[0].descripcion);
	    		}else{
	    			$("#cmpObservacionesCierre").val("");
	    		}	    			    		
	    	}
	    },			    		    
	    error: function(xhr,estado,error) {
	    	$("#cmpObservacionesCierre").val("");
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
        	referenciaModulo.obj.ocultaContenedorTabla.hide();        
	    }
	});
	};

moduloCierre.prototype.inicializarGrillaResumen=function(){
  var referenciaModulo=this;
  this.obj.tablaResumenCierre.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
	   referenciaModulo.ajaxGrillaResumen(e,configuracion,json);
  });
  
  this.obj.tablaResumenCierre.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
    //Se ejecuta antes de cualquier llamada ajax
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
  });

  this.obj.tablaResumenCierre.on(constantes.DT_EVENTO_PAGINACION, function () {
  //Se ejecuta cuando se hace clic en boton de paginacion
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
  });

  this.obj.tablaResumenCierre.on(constantes.DT_EVENTO_ORDENACION, function () {
  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
  });

  this.obj.grillaResumenCierreApi= this.obj.tablaResumenCierre.DataTable({
    deferLoading: 0,
    processing: true,
    responsive: true,
    dom: constantes.DT_ESTRUCTURA,
    iDisplayLength: referenciaModulo.NUMERO_REGISTROS_PAGINA,
    lengthMenu: referenciaModulo.TOPES_PAGINACION,
    language: {
      url: referenciaModulo.URL_LENGUAJE_GRILLA
    },
    serverSide: true,
    "ajax": {
      "url": referenciaModulo.URL_LISTAR_RESUMEN,
      "type":constantes.PETICION_TIPO_GET,
      "data": function (configuracion) {
          console.log("inicializarGrillaDiaOperativo");
          referenciaModulo.cfgAjaxGrillaResumen(configuracion);
        }
    },
    "columns": referenciaModulo.columnasGrillaResumen,
    "columnDefs": referenciaModulo.definicionColumnasResumen
	});	
	
	$('#tablaResumenCierre tbody').on(referenciaModulo.NOMBRE_EVENTO_CLICK, 'tr', function () {

		if (referenciaModulo.obj.grillaResumenCierreApi.data().length > 0){
		    if ( $(this).hasClass('selected') ) {
		        $(this).removeClass('selected');
		      } else {
		        referenciaModulo.obj.grillaResumenCierreApi.$('tr.selected').removeClass('selected');
		        $(this).addClass('selected');
		      }
		  		var indiceFila = referenciaModulo.obj.grillaResumenCierreApi.row( this ).index();
		  		var idRegistro = referenciaModulo.obj.grillaResumenCierreApi.cell(indiceFila,1).data();
		  		var estadoregistro = referenciaModulo.obj.grillaResumenCierreApi.cell(indiceFila,9).data();
		  		referenciaModulo.idResumenCierre = idRegistro;		
		  		//referenciaModulo.grillaDespuesSeleccionar(indiceFila);
		}
	});  
  
   $('#tablaResumenCierre tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = referenciaModulo.obj.grillaResumenCierreApi.row( tr ); 
        if ( row.child.isShown() ) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        } else {
            // Open this row
            row.child( referenciaModulo.formatearDescripcion(row.data()) ).show();
            tr.addClass('shown');
        }
    });  
};

moduloCierre.prototype.formatearDescripcion = function ( d ) {
    // `d` is the original data object for the row
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>Descripcion:</td>'+
            '<td>'+d.descripcion+'</td>'+
        '</tr>'+
    '</table>';
};

moduloCierre.prototype.inicializarFormularioPrincipal= function(){  
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


moduloCierre.prototype.listarRegistros = function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("listarRegistros");  
  this.obj.datClienteApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	 	
};

moduloCierre.prototype.actualizarBandaInformacion=function(tipo, mensaje){
	console.log("actualizarBandaInformacion");
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

moduloCierre.prototype.iniciarListado= function(mensaje){
	var referenciaModulo = this;
	try {
		referenciaModulo.listarRegistros();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,mensaje);
		referenciaModulo.obj.cntFormulario.hide();	
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloCierre.prototype.iniciarContenedores = function(){
  var referenciaModulo = this;
  try {
    referenciaModulo.obj.cntFormulario.hide();	
    referenciaModulo.protegeFormulario(false);
    referenciaModulo.obj.cntTabla.show();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};



moduloCierre.prototype.actualizarEstadoRegistro= function(){
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
	
moduloCierre.prototype.guardarValidacionAutorizacion= function(){
//Implementar en cada caso
};
	
moduloCierre.prototype.recuperarAutorizacionesXcodigoInterno= function(){
//Implementar en cada caso
};
	
moduloCierre.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloCierre.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloCierre.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloCierre.prototype.grillaDespuesSeleccionar= function(indice){
//TODO implementar
};

moduloCierre.prototype.despuesListarRegistros=function(){
//Implementar en cada caso
};

moduloCierre.prototype.validaPermisos= function(){
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
		      if (referenciaModulo.descripcionPermiso == 'LEER_CIERRE'){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
		    	  referenciaModulo.mostrarResumenCierre();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_DIA_OPERATIVO'){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
		    	  referenciaModulo.botonCambiarEstado();
		      } else if (referenciaModulo.descripcionPermiso == 'EXPORTAR_PDF'){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
		    	  referenciaModulo.mostrarReporte(constantes.FORMATO_PDF);
		      } else if (referenciaModulo.descripcionPermiso == 'EXPORTAR_XLS'){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
		    	  referenciaModulo.mostrarReporte(constantes.FORMATO_CSV);
		      } else if (referenciaModulo.descripcionPermiso == 'CERRAR_DIA_OPERATIVO'){
		    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
		    	  referenciaModulo.solicitarCierreDia();
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