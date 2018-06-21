function moduloTransporte (){
  this.obj={};
  this.NUMERO_REGISTROS_PAGINA = constantes.NUMERO_REGISTROS_PAGINA;
  this.TOPES_PAGINACION = constantes.TOPES_PAGINACION;
  this.URL_LENGUAJE_GRILLA = "tema/datatable/language/es-ES.json";
  this.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;
  this.NOMBRE_EVENTO_CLICK=constantes.NOMBRE_EVENTO_CLICK;
  this.modoEdicion=constantes.MODO_LISTAR;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  this.URL_CONSULTAR_SAP="transporte/consultar-sap";
  this.URL_GRABAR_SAP="transporte/guardar-sap";
  //Inicializar propiedades
  this.urlBase='';  
  this.mensajeEsMostrado=false;
  this.idRegistro = 0;
  this.columnasGrilla={};
  this.definicionColumnas=[];
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};  
  this.NUMERO_REGISTROS_PAGINA_TRANSPORTE = constantes.NUMERO_REGISTROS_PAGINA_TRANSPORTE;
  this.TOPES_PAGINACION_TRANSPORTE = constantes.TOPES_PAGINACION_TRANSPORTE;  
  this.reglasValidacionFormularioEvento={};
  this.mensajesValidacionFormularioEvento={};  
  this.reglasValidacionFormularioPesaje={};
  this.mensajesValidacionFormularioPesaje={};  
  this.ordenGrilla=[[ 1, 'asc' ]];
  this.columnasGrilla=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnas=[{"targets": 0, "searchable": false, "orderable": false, "visible":false, "render": function (datos, tipo, fila, meta) {	var configuracion =meta.settings;
    																																				return configuracion._iDisplayStart + meta.row + 1;
    																																			  }   }];  
  this.columnasGrillaAsignacionTransporte={};
  this.definicionColumnasAsignacionTransporte=[];
  this.ordenGrillaAsignacionTransporte=[[ 1, 'asc' ]];
  this.columnasGrillaAsignacionTransporte=[{ "data": null} ];//Target 0
  this.definicionColumnasAsignacionTransporte=[{ "targets": 0, "searchable": false, "orderable": false, "visible":false, "render": function ( datos, tipo, fila, meta ) { var configuracion =meta.settings;
        																																								  return configuracion._iDisplayStart + meta.row + 1;
        																																								}   }]; 
  this.columnasDetalleDiaOperativo={};
  this.definicionColumnasDetalleDiaOperativo=[];
  this.ordenDetalleDiaOperativo=[[ 1, 'asc' ]];
  this.columnasDetalleDiaOperativo=[{ "data": null} ];//Target 0
  // this.cmpFitlroEstado.val("2");
  this.definicionColumnasDetalleDiaOperativo=[{"targets": 0, "searchable": false, "orderable": false, "visible":false, "render": function ( datos, tipo, fila, meta ) { var configuracion =meta.settings;
        																																								return configuracion._iDisplayStart + meta.row + 1;
        																																							  }      }]; 
};

moduloTransporte.prototype.mostrarDepuracion=function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloTransporte.prototype.inicializar=function(){
	this.configurarAjax();
  this.mostrarDepuracion("inicializar");
  this.inicializarControlesGenericos();
  this.inicializarGrilla();
  this.inicializarGrillaAsignacionTransporte();
  this.inicializarDetalleDiaOperativo();
  this.inicializarFormularioPrincipal();
  this.inicializarFormularioEvento();
  this.inicializarFormularioPesaje();
  this.inicializarCampos();
};

moduloTransporte.prototype.configurarAjax=function(){
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

moduloTransporte.prototype.resetearFormularioPrincipal= function(){
  var referenciaModulo= this;
  referenciaModulo.obj.frmPrincipal[0].reset();
  jQuery.each( this.obj, function( i, val ) {
    if (typeof referenciaModulo.obj[i].tipoControl != "undefined" ){
      if (referenciaModulo.obj[i].tipoControl =="select2"){
        referenciaModulo.obj[i].select2("val", null);
      }
    }
  });
};

moduloTransporte.prototype.validaFormularioXSS= function(formulario){
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
	
moduloTransporte.prototype.inicializarControlesGenericos=function(){
  this.mostrarDepuracion("inicializarControlesGenericos");
  var referenciaModulo=this;
  referenciaModulo.obj.tablaListaSap=$("#tablaListaSap");
  referenciaModulo.obj.plantillaNoRegistro=$("#plantillaNoRegistro");
  referenciaModulo.obj.plantillaDetalleSap=$("#plantillaDetalleSap");
  this.obj.tituloSeccion=$("#tituloSeccion");
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  //Contenedores
  this.obj.cntTabla=$("#cntTabla");							//1
  this.obj.cntDetalleTransporte=$("#cntDetalleTransporte"); //2
  this.obj.cntFormulario=$("#cntFormulario");				//3
  this.obj.cntFormularioImportar=$("#cntFormularioImportar");
  this.obj.ocultaContenedorFormularioImportar=$("#ocultaContenedorFormularioImportar");
  this.obj.cntVistaRegistro=$("#cntVistaRegistro");			//4
  this.obj.cntEventoTransporte=$("#cntEventoTransporte");	//5
  this.obj.cntPesajeTransporte=$("#cntPesajeTransporte");	//6
  //contenedor de detalle de transporte (sheepit)
  this.obj.contenedorDetalles=$("#contenedorDetalles");
  //formularios
  this.obj.frmPrincipal = $("#frmPrincipal");
  this.obj.frmPesaje = $("#frmPesaje");
  this.obj.frmEvento = $("#frmEvento");
  //dataTables
  this.obj.tablaPrincipal=$('#tablaPrincipal');
  this.obj.tablaAsignacionTransporte=$('#tablaAsignacionTransporte');
  this.obj.tablaDetalleDiaOperativo=$('#tablaDetalleDiaOperativo');
  //botones
  this.obj.btnFiltrar=$("#btnFiltrar");
  this.obj.btnDetalle=$("#btnDetalle");
  this.obj.btnAgregarTransporte=$("#btnAgregarTransporte");
  this.obj.btnModificarTransporte=$("#btnModificarTransporte");
  this.obj.btnImportar=$("#btnImportar");
  this.obj.btnVer=$("#btnVer");
  this.obj.btnEvento=$("#btnEvento");
  this.obj.btnPesaje=$("#btnPesaje");
  
  //Agregado por req 9000002570====================
  this.obj.btnTiempoEtapa=$("#btnTiempoEtapa");
  this.obj.ocultaContenedorTiempos=$("#ocultaContenedorTiempos");
  this.obj.cntFrmTiempos=$("#cntFrmTiempos");
  this.obj.btnGuardarTiempos=$("#btnGuardarTiempos");
  this.obj.btnCancelarTiempos=$("#btnCancelarTiempos");
  this.obj.tiempoTotal=$("#tiempoTotal");
  //===============================================
  
  this.obj.btnCerrarDetalleTransporte=$("#btnCerrarDetalleTransporte");
  this.obj.btnGuardar=$("#btnGuardar");
  this.obj.btnCancelarGuardarFormulario=$("#btnCancelarGuardarFormulario");
  this.obj.btnCerrarVista=$("#btnCerrarVista");
  this.obj.btnCancelarImportarTransporte=$("#btnCancelarImportarTransporte");
  //botones de formulario Evento
  this.obj.btnGuardarEvento=$("#btnGuardarEvento");
  this.obj.btnCancelarGuardarEvento=$("#btnCancelarGuardarEvento");
  //botones de formulario Pesaje
  this.obj.btnGuardarPesaje=$("#btnGuardarPesaje");
  this.obj.btnCancelarGuardarPesaje=$("#btnCancelarGuardarPesaje");
  //titulo de las pantallas
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");	
  //Protectores de pantallas
  this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
  this.obj.ocultaContenedorDetalleTransporte=$("#ocultaContenedorDetalleTransporte");
  this.obj.ocultaContenedorFormulario=$("#ocultaContenedorFormulario");
  this.obj.ocultaContenedorVista=$("#ocultaContenedorVista");
  this.obj.ocultaContenedorFormularioEvento=$("#ocultaContenedorFormularioEvento");
  this.obj.ocultaContenedorFormularioPesaje=$("#ocultaContenedorFormularioPesaje");
  //banda para mensajes de error o exito
  this.obj.bandaInformacion=$("#bandaInformacion");
  //Identificadores principales para la recuperacion del registro
  this.obj.idDiaOperativo=$("#idDiaOperativo");
  this.obj.idTransporte=$("#idTransporte");
  this.obj.clienteSeleccionado=$("#clienteSeleccionado");
  this.obj.idOperacionSeleccionado =$("#idOperacionSeleccionado");
  this.obj.operacionSeleccionado=$("#operacionSeleccionado");
  this.obj.fechaPlanificacionSeleccionado=$("#fechaPlanificacionSeleccionado");  
  this.obj.guiaRemisionSeleccionado=$("#guiaRemisionSeleccionado");
  this.obj.ordenEntregaSeleccionado=$("#ordenEntregaSeleccionado");
  this.obj.cisternaTractoSeleccionado=$("#cisternaTractoSeleccionado");  
  this.obj.cmpFechaInicial=$("#cmpFechaInicial");
  this.obj.cmpFechaFinal=$("#cmpFechaFinal");
  this.obj.idCliente=$("#idCliente");
  this.obj.idTransportista=$("#idTransportista");
  this.estadoDiaOperativo=$("#estadoDiaOperativo");
  this.estadoTransporte=$("#estadoTransporte");  
//  //Inicializar controles	
//	this.obj.frmConfirmarModificarEstado=$("#frmConfirmarModificarEstado");	
//	this.obj.frmConfirmarEliminar=$("#frmConfirmarEliminar");
//	this.obj.btnListar=$("#btnListar");
//	this.obj.btnAgregar=$("#btnAgregar");
//	this.obj.btnModificar=$("#btnModificar");
//	this.obj.btnModificarEstado=$("#btnModificarEstado");	
//	this.obj.btnConfirmarModificarEstado=$("#btnConfirmarModificarEstado");
//	this.obj.btnEliminar=$("#btnEliminar");
//	
//	this.obj.btnConfirmarEliminar=$("#btnConfirmarEliminar");	
//	
//	this.obj.btnArribo=$("#btnArribo");
//	this.obj.btnCancelarGuardar=$("#btnCancelarGuardar");
//	
//	//estos valores para hacer los filtros de los listados	
//	this.obj.txtFiltro=$("#txtFiltro");
//	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
//	
//	this.obj.filtroOperacion=$("#filtroOperacion");
//	this.obj.filtroFechaPlanificada=$("#filtroFechaPlanificada");
//	this.obj.cmpFiltroUsuario=$("#cmpFiltroUsuario");
//	this.obj.cmpFiltroTabla=$("#cmpFiltroTabla");
//	this.obj.cmpFiltroTipoFecha=$("#cmpFiltroTipoFecha");	
  this.obj.btnCancelarImportarTransporte.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.ocultarFormularioImportar();
	});
  
  this.obj.btnFiltrarSap=$("#btnFiltrarSap");
	this.obj.btnFiltrarSap.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.consultarSAP();
	});
		
	this.obj.btnImportarTransporte=$("#btnImportarTransporte");
	this.obj.btnImportarTransporte.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.guardarSAP();
	});
	//eventos click
	this.obj.btnFiltrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonFiltro();
	});

	this.obj.btnDetalle.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'LEER_TRANSPORTE';
		referenciaModulo.validaPermisos();
//		referenciaModulo.botonDetalle();
	});
	
	this.obj.btnAgregarTransporte.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'CREAR_TRANSPORTE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.botonAgregarTransporte();
	});
	
	this.obj.btnModificarTransporte.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'ACTUALIZAR_TRANSPORTE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.botonModificarTransporte();
	});
	
	this.obj.btnImportar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'IMPORTAR_TRANSPORTE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.mostrarFormularioImportar();
	});
	
	this.obj.btnVer.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'RECUPERAR_TRANSPORTE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.botonVer();
	});
	
	this.obj.btnEvento.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'CREAR_EVENTO_TRANSPORTE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.botonEvento();
	});
	
	this.obj.btnPesaje.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.descripcionPermiso = 'ACTUALIZAR_PESAJE';
		referenciaModulo.validaPermisos();
		//referenciaModulo.botonPesaje();		
	});	
	
	this.obj.btnCerrarDetalleTransporte.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonCerrarDetalleTransporte();		
	});	
	
	this.obj.btnGuardar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonGuardar();
	});
	
	this.obj.btnCancelarGuardarFormulario.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonCancelarGuardarFormulario();		
	});	
	
	//Agregado por req 9000002570=========================================
	this.obj.btnGuardarTiempos.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.iniciarGuardarTiempos();
	});
	  
	this.obj.btnTiempoEtapa.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.iniciarTiempoEtapas();		
	});
	
	this.obj.btnCancelarTiempos.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.iniciarCancelarTiempos();
	});
	  
    //====================================================================
	
	this.obj.btnCerrarVista.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonCerrarVista();
	});
	
	this.obj.btnGuardarEvento.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonGuardarEvento();
	});
	
	this.obj.btnCancelarGuardarEvento.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonCancelarGuardarEvento();
	});
	
	this.obj.btnGuardarPesaje.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonGuardarPesaje();
	});
	
	this.obj.btnCancelarGuardarPesaje.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
		referenciaModulo.botonCancelarGuardarPesaje();
	});
};

//Agregado por req 9000002570====================
moduloTransporte.prototype.iniciarGuardarTiempos=function(){
	 var referenciaModulo=this;
	  try {
		  
		  var validar = referenciaModulo.validarFechas();
		  
		  if(validar){
			  referenciaModulo.guardarRegistrosTiempos();
		  }

	  } catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	  };
};

moduloBase.prototype.recuperarValoresTiempos = function(registro){
	var eRegistro = {};
	//Implementar en cada caso
	return eRegistro;
};

moduloTransporte.prototype.guardarRegistrosTiempos= function(){
	var referenciaModulo = this;
	try {
		
		referenciaModulo.obj.ocultaContenedorTiempos.show();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		var eRegistro = referenciaModulo.recuperarValoresTiempos();
		
		if(eRegistro != null){
			$.ajax({
				type: constantes.PETICION_TIPO_POST,
				url: referenciaModulo.URL_GUARDAR_TIEMPOS,
				contentType: referenciaModulo.TIPO_CONTENIDO,
				data: JSON.stringify(eRegistro),
				success: function(respuesta) {
					if (!respuesta.estado) {
				          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
				        } else {
				      	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
				    	  referenciaModulo.obj.cntFrmTiempos.hide();
				    	  referenciaModulo.obj.cntDetalleTransporte.show();
				    	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
				        }
						referenciaModulo.obj.ocultaContenedorTiempos.hide();
				},
				error: function() {
					referenciaModulo.mostrarErrorServidor(xhr,estado,error);
					referenciaModulo.obj.ocultaContenedorTiempos.hide();
			    }
			});
			
		}else{
			referenciaModulo.obj.ocultaContenedorTiempos.hide();
		}
		
	} catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	    referenciaModulo.obj.ocultaContenedorTiempos.hide();
	};
	
};

moduloTransporte.prototype.iniciarCancelarTiempos=function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
	  referenciaModulo.obj.ocultaContenedorTiempos.show();
	  referenciaModulo.obj.cntFrmTiempos.hide();
	  referenciaModulo.obj.cntDetalleTransporte.show();
	  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
};

moduloTransporte.prototype.iniciarTiempoEtapas= function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_TIEMPOS_ETAPAS_REGISTRO);
	  referenciaModulo.obj.cntDetalleTransporte.hide();
	  referenciaModulo.obj.ocultaContenedorTiempos.show();
	  referenciaModulo.obj.cntFrmTiempos.show();
	  
	  referenciaModulo.recuperarRegistroTiempos();
	  
	  
};

moduloTransporte.prototype.recuperarRegistroTiempos= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR_TIEMPOS_ETAPA, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {idTransporte:referenciaModulo.idTransporte, idOperacion: referenciaModulo.obj.idOperacionSeleccionado},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	}else{		 
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    	referenciaModulo.llenarFormularioTiempos(respuesta.contenido.carga[0]);
		    	
		  	  if(referenciaModulo.estadoDiaOperativo== 4){
				  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Esta fecha planificada (" + utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado) + ") se encuentra CERRADA, no puede registrar tiempos ni guardar datos.");
				  referenciaModulo.obj.btnGuardarTiempos.addClass(constantes.CSS_CLASE_DESHABILITADA);
				  
				  referenciaModulo.inactivarGrillaEtapas();
				  
				  console.log("entro al caso CERRADA");
				  
			  }else{
				  referenciaModulo.obj.btnGuardarTiempos.removeClass(constantes.CSS_CLASE_DESHABILITADA);
			  }
    		}
	    	referenciaModulo.obj.ocultaContenedorTiempos.hide();
	    },			    		    
	    error: function(xhr,estado,error) {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error);        
	        referenciaModulo.obj.ocultaContenedorTiempos.hide();
	    }
	});
};
//==============================================

moduloTransporte.prototype.mostrarFormularioImportar= function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
    	 $("#tablaListaSap > tbody:first").children().remove();
	    referenciaModulo.modoEdicion = constantes.MODO_FORMULARIO_TRANSPORTE_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text("Importar transporte de SAP");
	    referenciaModulo.obj.contenedorDetalles.hide();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleTransporte.hide();
	    referenciaModulo.obj.cntFormulario.hide();    
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntEventoTransporte.hide();
	    referenciaModulo.obj.cntPesajeTransporte.hide();
	    referenciaModulo.obj.cntFormularioImportar.show();
	    referenciaModulo.obj.lblNombreCliente.text(referenciaModulo.obj.clienteSeleccionado);
	    referenciaModulo.obj.lblNombreOperacion.text(referenciaModulo.obj.operacionSeleccionado);
	    referenciaModulo.obj.lblFechaPlanificacion.text(utilitario.formatearFecha(referenciaModulo.obj.fechaPlanificacionSeleccionado));
	    referenciaModulo.obj.cmpFiltroFechaInicialImportacion.val(utilitario.formatearFecha(referenciaModulo.cmpFechaCarga));
	    referenciaModulo.obj.ocultaContenedorFormularioImportar.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error) {
	  console.log(error.message);
	}
};

moduloTransporte.prototype.ocultarFormularioImportar= function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
    	 $("#tablaListaSap > tbody:first").children().remove();
	    referenciaModulo.modoEdicion = constantes.MODO_FORMULARIO_TRANSPORTE_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_TRANSPORTE);
	    referenciaModulo.obj.contenedorDetalles.hide();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleTransporte.show();
	    referenciaModulo.obj.cntFormulario.hide();    
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntEventoTransporte.hide();
	    referenciaModulo.obj.cntPesajeTransporte.hide();
	    referenciaModulo.obj.cntFormularioImportar.hide();
	    referenciaModulo.obj.lblNombreCliente.text("");
	    referenciaModulo.obj.lblNombreOperacion.text("");
	    referenciaModulo.obj.lblFechaPlanificacion.text("");
	    referenciaModulo.obj.ocultaContenedorFormularioImportar.hide();
	    referenciaModulo.iniciarListado();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error) {
	  console.log(error.message);
	}
};

moduloTransporte.prototype.botonFiltro = function(){
  var referenciaModulo = this;
  try {							    
	referenciaModulo.listarRegistros();
  } catch(error){
    console.log(error.message);
  }
};

moduloTransporte.prototype.botonDetalle = function(){
  var referenciaModulo = this;
  try {
    referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,cadenas.INICIAR_DETALLE_TRANSPORTE);
    referenciaModulo.obj.cntTabla.hide();
    referenciaModulo.obj.ocultaContenedorDetalleTransporte.show();
    referenciaModulo.obj.cntDetalleTransporte.show();
    referenciaModulo.obj.cntFormulario.hide();
    referenciaModulo.obj.cntVistaRegistro.hide();
    referenciaModulo.obj.cntEventoTransporte.hide();
    referenciaModulo.obj.cntPesajeTransporte.hide();
    referenciaModulo.datosCabecera();//esto para llenar la cabecera
    this.obj.datAsignacionTransporteApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);
    this.obj.datTablaDetalleDiaOperativoApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
    /*referenciaModulo.inicializarGrillaAsignacionTransporte();
    referenciaModulo.inicializarDetalleDiaOperativo();*/
  	referenciaModulo.desactivarBotones();
  	referenciaModulo.obj.ocultaContenedorDetalleTransporte.hide();
  } catch(error){
    console.log(error.message);
  }
};

moduloTransporte.prototype.botonAgregarTransporte = function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
	    referenciaModulo.modoEdicion = constantes.MODO_FORMULARIO_TRANSPORTE_NUEVO;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_TRANSPORTE);
	    referenciaModulo.obj.contenedorDetalles.hide();
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleTransporte.hide();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntEventoTransporte.hide();
	    referenciaModulo.obj.cntPesajeTransporte.hide();
	    this.limpiarFormularioPrincipal();
	    referenciaModulo.datosCabecera();
	    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	} catch(error) {
	  console.log(error.message);
	}
};

moduloTransporte.prototype.botonModificarTransporte = function(){
	var referenciaModulo=this;
	referenciaModulo.inicializarFormularioPrincipal();
    try {
	    referenciaModulo.modoEdicion=constantes.MODO_FORMULARIO_TRANSPORTE_MODIFICAR;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MODIFICA_TRANSPORTE);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleTransporte.hide();
	    referenciaModulo.obj.ocultaContenedorFormulario.show();
	    referenciaModulo.obj.cntFormulario.show();
	    referenciaModulo.obj.cntVistaRegistro.hide();
	    referenciaModulo.obj.cntEventoTransporte.hide();
	    referenciaModulo.obj.cntPesajeTransporte.hide();
	    referenciaModulo.recuperarRegistro();
    } catch(error){
    	console.log(error.message);
    }
};

moduloTransporte.prototype.botonVer = function(){
	var referenciaModulo=this;
    try {
	    referenciaModulo.modoEdicion=constantes.MODO_VER_TRANSPORTE;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_VER_TRANSPORTE);
	    referenciaModulo.obj.cntTabla.hide();
	    referenciaModulo.obj.cntDetalleTransporte.hide();
	    referenciaModulo.obj.cntFormulario.hide();
	    referenciaModulo.obj.ocultaContenedorVista.show();
	    referenciaModulo.obj.cntVistaRegistro.show();
	    referenciaModulo.obj.cntEventoTransporte.hide();
	    referenciaModulo.obj.cntPesajeTransporte.hide();
	    referenciaModulo.recuperarRegistro();
    } catch(error){
	    console.log(error.message);
    }	
};

moduloTransporte.prototype.botonEvento = function(){
	var referenciaModulo=this;
    try {
    	referenciaModulo.modoEdicion=constantes.MODO_EVENTO_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_EVENTO_TRANSPORTE);
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
    	referenciaModulo.obj.cntTabla.hide();
    	referenciaModulo.obj.cntDetalleTransporte.hide();
    	referenciaModulo.obj.cntFormulario.hide();
    	referenciaModulo.obj.cntVistaRegistro.hide();
    	referenciaModulo.obj.ocultaContenedorFormularioEvento.show();
    	referenciaModulo.obj.cntEventoTransporte.show();
    	referenciaModulo.obj.cntPesajeTransporte.hide();
    	referenciaModulo.obj.frmEvento[0].reset();
    	referenciaModulo.llenarEventoTransporte();
    	referenciaModulo.obj.ocultaContenedorFormularioEvento.hide();
	} catch(error){
	    console.log(error.message);
	}
};

moduloTransporte.prototype.botonPesaje = function(){
	var referenciaModulo=this;
	try {
		referenciaModulo.modoEdicion=constantes.MODO_PESAJE_TRANSPORTE;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_PESAJE_TRANSPORTE);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntDetalleTransporte.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.ocultaContenedorFormularioPesaje.hide();
		referenciaModulo.obj.cntPesajeTransporte.show();
		referenciaModulo.obj.frmPesaje[0].reset();
		referenciaModulo.recuperarRegistro();
    } catch(error){
	    console.log(error.message);
    }
};

moduloTransporte.prototype.botonCerrarDetalleTransporte = function(){
	var referenciaModulo=this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CERRAR_DETALLE_TRANSPORTE);
		referenciaModulo.obj.cntTabla.show();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntDetalleTransporte.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
		referenciaModulo.obj.btnDetalle.removeClass("disabled");
		this.listarRegistros();
    } catch(error){
	    console.log(error.message);
    }
};

moduloTransporte.prototype.botonGuardar = function(){
	var referenciaModulo=this;
	if (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_TRANSPORTE_NUEVO){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Detalle de Transporte validado.");
		referenciaModulo.guardarRegistro();
	} else if  (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_TRANSPORTE_MODIFICAR){
		referenciaModulo.actualizarRegistro();
	}
		
};

moduloTransporte.prototype.botonCancelarGuardarFormulario = function(){
	var referenciaModulo=this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
	} catch (error) {
		console.log(error.message);
	}
};

moduloTransporte.prototype.botonCerrarVista= function(){  
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CERRAR_VISTA);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
	} catch(error){
		console.log(error.message);
	};
};

moduloTransporte.prototype.botonGuardarEvento= function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
		referenciaModulo.guardarEvento();
	} catch (error) {
		console.log(error.message);
	}
};

moduloTransporte.prototype.botonCancelarGuardarEvento= function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
	} catch (error) {
		console.log(error.message);
	}
};

moduloTransporte.prototype.botonGuardarPesaje= function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.guardarPesaje();
	} catch (error) {
		console.log(error.message);
	}
};

moduloTransporte.prototype.botonCancelarGuardarPesaje= function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
	} catch (error) {
		console.log(error.message);
	}
};

moduloTransporte.prototype.inicializarGrilla=function(){
  //Nota no retornar el objeto solo manipular directamente
	//Establecer grilla y su configuracion
  var referenciaModulo=this;
	this.obj.tablaPrincipal.on('xhr.dt', function (e,settings,json) {
    referenciaModulo.mostrarDepuracion("xhr.dt");
    referenciaModulo.desactivarBotones();
		if (json.estado==true){
			json.recordsTotal=json.contenido.totalRegistros;
			json.recordsFiltered=json.contenido.totalEncontrados;
			json.data= json.contenido.carga;
			if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
			}
			//
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
	});
  
  this.obj.tablaPrincipal.on('preXhr.dt', function ( e, settings, data ) {
    //Se ejecuta antes de cualquier llamada ajax
    referenciaModulo.mostrarDepuracion("preXhr");
    if (referenciaModulo.estaCargadaInterface==true){
      referenciaModulo.obj.ocultaContenedorTabla.show();
    }
  });
  
  this.obj.tablaPrincipal.on('page.dt', function () {
    //Se ejecuta cuando se hace clic en boton de paginacion
    referenciaModulo.mostrarDepuracion("page.dt");
  });
  
  this.obj.tablaPrincipal.on('order.dt', function () {
    //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
    referenciaModulo.mostrarDepuracion("order.dt");
  });

	this.obj.datClienteApi= this.obj.tablaPrincipal.DataTable({
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
		      "url": './dia_operativo/listar',
		      "type":"GET",
		      "data": function (d) {
		        var indiceOrdenamiento = d.order[0].column;
		        d.registrosxPagina =  d.length; 
		        d.inicioPagina = d.start; 
		        d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
		        d.sentidoOrdenamiento=d.order[0].dir;
		        d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
		        d.filtroFechaPlanificada= referenciaModulo.obj.filtroFechaPlanificada.val();
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
	
	$('#tablaPrincipal tbody').on( 'click', 'tr', function () {
		if (referenciaModulo.obj.datClienteApi.data().length > 0){
		    if ( $(this).hasClass('selected') ) {
		        $(this).removeClass('selected');
		      } else {
		        referenciaModulo.obj.datClienteApi.$('tr.selected').removeClass('selected');
		        $(this).addClass('selected');
		      }
		  		var indiceFila = referenciaModulo.obj.datClienteApi.row( this ).index();
		  		var idRegistro = referenciaModulo.obj.datClienteApi.cell(indiceFila,1).data();
		  		referenciaModulo.idRegistro = idRegistro;		

		  		referenciaModulo.idDiaOperativo = referenciaModulo.obj.datClienteApi.cell(indiceFila,1).data();
		  		referenciaModulo.idTransporte = -1;

		  		referenciaModulo.idCliente = referenciaModulo.obj.datClienteApi.cell(indiceFila,10).data();
		  		referenciaModulo.obj.clienteSeleccionado = referenciaModulo.obj.datClienteApi.cell(indiceFila,9).data();
		  		referenciaModulo.obj.idOperacionSeleccionado = referenciaModulo.obj.datClienteApi.cell(indiceFila,11).data();
		  		referenciaModulo.obj.operacionSeleccionado = referenciaModulo.obj.datClienteApi.cell(indiceFila,8).data();
		  		referenciaModulo.obj.fechaPlanificacionSeleccionado = referenciaModulo.obj.datClienteApi.cell(indiceFila,2).data();
		  		referenciaModulo.obj.btnDetalle.removeClass("disabled");
		  		referenciaModulo.cmpFechaCarga = referenciaModulo.obj.datClienteApi.cell(indiceFila,12).data();
		  		
		  		referenciaModulo.estadoDiaOperativo = referenciaModulo.obj.datClienteApi.cell(indiceFila,7).data();
		  		referenciaModulo.estadoTransporte = 2;
		}

	});
};

//ESTO PARA LA GRILLA DE ASIGNACION DE TRANSPORTE
moduloTransporte.prototype.llamadaAjaxGrillaAsignacionTransporte=function(e,configuracion,json){
	  var referenciaModulo=this;
	  referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
	  referenciaModulo.desactivarBotones();
	  if (json.estado==true){
	    json.recordsTotal=json.contenido.totalRegistros;
	    json.recordsFiltered=json.contenido.totalEncontrados;
	    json.data= json.contenido.carga;
	    if (referenciaModulo.modoEdicion==constantes.MODO_AUTORIZAR){
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
	    }
	  } else {
	    json.recordsTotal=0;
	    json.recordsFiltered=0;
	    json.data= {};
	    if (referenciaModulo.modoEdicion==constantes.MODO_AUTORIZAR){
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
	    } else {

	    }
	  }
	  if (referenciaModulo.estaCargadaInterface==false){        
	    referenciaModulo.estaCargadaInterface=true;
	  }
	 // referenciaModulo.obj.ocultaContenedorTabla.hide();  
	};

moduloTransporte.prototype.inicializarGrillaAsignacionTransporte=function(){
//Nota no retornar el objeto solo manipular directamente
	//Establecer grilla y su configuracion
var referenciaModulo=this;
this.obj.tablaAsignacionTransporte.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
	   referenciaModulo.llamadaAjaxGrillaAsignacionTransporte(e,configuracion,json);
});
};

moduloTransporte.prototype.inicializarGrillaAsignacionTransporte = function(){
	  //Nota no retornar el objeto solo manipular directamente
	  //Establecer grilla y su configuracion
	  var referenciaModulo=this;
	  //referenciaModulo.obj.ocultaContenedorAutorizacion.show();
	  
	  this.obj.tablaAsignacionTransporte.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxGrillaAsignacionTransporte(e,configuracion,json);
	  });
	  
	  this.obj.tablaAsignacionTransporte.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    //referenciaModulo.obj.ocultaContenedorAutorizacion.show();
	    if (referenciaModulo.estaCargadaInterface==true){
	    	//referenciaModulo.obj.ocultaContenedorAutorizacion.hide();
	    }
	  });

	  this.obj.tablaAsignacionTransporte.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaAsignacionTransporte.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });
	  try{
		  this.obj.datAsignacionTransporteApi = this.obj.tablaAsignacionTransporte.DataTable({
			"processing": true,
			"responsive": true,
			"dom": '<"row" <"col-sm-12" t> ><"row" <"col-sm-3"l> <"col-sm-4"i> <"col-sm-5"p>>',
			"iDisplayLength": referenciaModulo.NUMERO_REGISTROS_PAGINA_TRANSPORTE,
			"lengthMenu": referenciaModulo.TOPES_PAGINACION_TRANSPORTE,
			"language": {
			"url": referenciaModulo.URL_LENGUAJE_GRILLA
			},
		    "serverSide": true,
		    "ajax": {
			    "url": "./transporte/listarTransportes",
			    "type":constantes.PETICION_TIPO_GET,
	  		    "data": function (d) {
    		    	  var indiceOrdenamiento = d.order[0].column;
			          d.registrosxPagina =  d.length; 
			          d.inicioPagina = d.start; 
			          d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
			          d.sentidoOrdenamiento=d.order[0].dir;
    		    	  d.ID= referenciaModulo.idRegistro; 
	  		    },
			 },
		    "columns": referenciaModulo.columnasGrillaAsignacionTransporte,
		    "columnDefs": referenciaModulo.definicionColumnasAsignacionTransporte,
		});	

	  	$('#tablaAsignacionTransporte tbody').on( 'click', 'tr', function () {
	  		if (referenciaModulo.obj.datAsignacionTransporteApi.data().length > 0){
			     if ( $(this).hasClass('selected') ) {
			            $(this).removeClass('selected');
			            referenciaModulo.obj.btnTiempoEtapa.addClass(constantes.CSS_CLASE_DESHABILITADA);
			     } else {
			    	 referenciaModulo.obj.datAsignacionTransporteApi.$('tr.selected').removeClass('selected');
			     	$(this).addClass('selected');
			     }
				var indiceFilaTransporte = referenciaModulo.obj.datAsignacionTransporteApi.row( this ).index();
				referenciaModulo.idTransporte = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte, 1).data();
				referenciaModulo.estadoTransporte = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte,9).data();
				
				//Agregado por req 9000002570====================
				if ( $(this).hasClass('selected') ) {
					if(referenciaModulo.estadoTransporte== 3 || referenciaModulo.estadoTransporte == 4){
			      		referenciaModulo.obj.btnTiempoEtapa.removeClass(constantes.CSS_CLASE_DESHABILITADA);
			      	}else{
			      		referenciaModulo.obj.btnTiempoEtapa.addClass(constantes.CSS_CLASE_DESHABILITADA);
			      	}
				}
				//===============================================
				
				var origenRegistro = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte, 8).data();
				referenciaModulo.activarBotones();
				if (origenRegistro == "M" || origenRegistro == "P") {
					referenciaModulo.obj.btnModificarTransporte.removeClass("disabled");
				} else if (origenRegistro == "A") {
					referenciaModulo.obj.btnModificarTransporte.addClass("disabled");
				}
				
				//Desabilitadmos el botón modificar cuando el estado del transporte sea inactivo, descargado o anulado
			  	if(referenciaModulo.estadoTransporte == 2 || referenciaModulo.estadoTransporte == 4 || referenciaModulo.estadoTransporte == 5 || origenRegistro == "A" ){
			  		referenciaModulo.obj.btnModificarTransporte.addClass("disabled");
			  	}
			  	else{
			  		referenciaModulo.obj.btnModificarTransporte.removeClass("disabled");
			  	}
			  	
				referenciaModulo.obj.guiaRemisionSeleccionado = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte,2).data();
				referenciaModulo.obj.ordenEntregaSeleccionado = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte,10).data();
				referenciaModulo.obj.cisternaTractoSeleccionado = referenciaModulo.obj.datAsignacionTransporteApi.cell(indiceFilaTransporte,4).data();
	  		}

		});
	} catch(error){
	    console.log(error.message);
	}
};

//ESTO PARA LA GRILLA DETALLE DEL DIA OPERATIVO
moduloTransporte.prototype.llamadaAjaxDetalleDiaOperativo=function(e,configuracion,json){
	  var referenciaModulo=this;
	  referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
	  if (json.estado==true){
	    json.recordsTotal=json.contenido.totalRegistros;
	    json.recordsFiltered=json.contenido.totalEncontrados;
	    json.data= json.contenido.carga;
	    if (referenciaModulo.modoEdicion==constantes.MODO_AUTORIZAR){
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
	    }
	  } else {
	    json.recordsTotal=0;
	    json.recordsFiltered=0;
	    json.data= {};
	    if (referenciaModulo.modoEdicion==constantes.MODO_AUTORIZAR){
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
	    } else {

	    }
	  }
	  if (referenciaModulo.estaCargadaInterface==false){        
	    referenciaModulo.estaCargadaInterface=true;
	  }
	};

moduloTransporte.prototype.inicializarDetalleDiaOperativo=function(){
  //Nota no retornar el objeto solo manipular directamente
  //Establecer grilla y su configuracion
  var referenciaModulo=this;
  this.obj.tablaDetalleDiaOperativo.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
  referenciaModulo.llamadaAjaxDetalleDiaOperativo(e,configuracion,json);
  });
};

moduloTransporte.prototype.inicializarDetalleDiaOperativo = function(){
  //Nota no retornar el objeto solo manipular directamente
  //Establecer grilla y su configuracion
  var referenciaModulo=this;
  //referenciaModulo.obj.ocultaContenedorAutorizacion.show();
	  
	  this.obj.tablaDetalleDiaOperativo.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
		   referenciaModulo.llamadaAjaxDetalleDiaOperativo(e,configuracion,json);
	  });
	  
	  this.obj.tablaDetalleDiaOperativo.on(constantes.DT_EVENTO_PREAJAX, function (e,configuracion,datos) {
	    //Se ejecuta antes de cualquier llamada ajax
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PREAJAX);
	    //referenciaModulo.obj.ocultaContenedorAutorizacion.show();
	    if (referenciaModulo.estaCargadaInterface==true){
	    	//referenciaModulo.obj.ocultaContenedorAutorizacion.hide();
	    }
	  });

	  this.obj.tablaDetalleDiaOperativo.on(constantes.DT_EVENTO_PAGINACION, function () {
	  //Se ejecuta cuando se hace clic en boton de paginacion
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
	  });

	  this.obj.tablaDetalleDiaOperativo.on(constantes.DT_EVENTO_ORDENACION, function () {
	  //Se ejecuta cuando se hace clic en alguna cabecera de columna para ordenarla
	    referenciaModulo.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
	  });
	  try{
		  this.obj.datTablaDetalleDiaOperativoApi = this.obj.tablaDetalleDiaOperativo.DataTable({
		    "processing": true,
		    "responsive": true,
		    "dom": constantes.DT_ESTRUCTURA,
		    "iDisplayLength": referenciaModulo.NUMERO_REGISTROS_PAGINA,
		    "lengthMenu": referenciaModulo.TOPES_PAGINACION_TRANSPORTE,
		    "language": {
		      "url": referenciaModulo.URL_LENGUAJE_GRILLA,
		    },
		    "serverSide": true,
		    "ajax": {
		    "url": "./transporte/listarAsignacionTransportes",
		    "type":constantes.PETICION_TIPO_GET,
  		    "data": function (parametros) {
    		      return {
    		    	  ID:	referenciaModulo.idRegistro, 
    		      };
  		    	},
		    },
		    "columns": referenciaModulo.columnasDetalleDiaOperativo,
		    "columnDefs": referenciaModulo.definicionColumnasDetalleDiaOperativo,
		});	
	} catch(error){
	    console.log(error.message);
	}
};

moduloTransporte.prototype.inicializarFormularioPrincipal= function(){  
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

moduloTransporte.prototype.inicializarFormularioEvento= function(){  
	//Establecer validaciones del formulario
  var referenciaModulo=this;
  this.obj.frmEvento.validate({
    rules: referenciaModulo.reglasValidacionFormularioEvento,
    messages: referenciaModulo.mensajesValidacionFormularioEvento,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloTransporte.prototype.inicializarFormularioPesaje= function(){  
	//Establecer validaciones del formulario
  var referenciaModulo=this;
  this.obj.frmPesaje.validate({
    rules: referenciaModulo.reglasValidacionFormularioPesaje,
    messages: referenciaModulo.mensajesValidacionFormularioPesaje,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloTransporte.prototype.activarBotones=function(){
	console.log("activarBotones");
	var referenciaModulo=this;
	this.obj.btnDetalle.addClass("disabled");
	this.obj.btnAgregarTransporte.removeClass("disabled");
	//this.obj.btnImportar.addClass("disabled");
	this.obj.btnVer.removeClass("disabled");
	this.obj.btnEvento.removeClass("disabled");
	this.obj.btnPesaje.removeClass("disabled");
	//Desabilitadmos el botón agregar cuando el estado del día operativo sea descargando, cerrado o liquidado
  	if(referenciaModulo.estadoDiaOperativo == 3 || referenciaModulo.estadoDiaOperativo == 4 || referenciaModulo.estadoDiaOperativo == 5){
  		this.obj.btnAgregarTransporte.addClass("disabled");
  		this.obj.btnModificarTransporte.addClass("disabled");
  		this.obj.btnImportar.addClass("disabled");
  	}
  	else{
  		this.obj.btnImportar.removeClass("disabled");
  		this.obj.btnAgregarTransporte.removeClass("disabled");
  		this.obj.btnModificarTransporte.addClass("disabled");
  	}
  	//Desabilitadmos el botón modificar cuando el estado del transporte sea inactivo, descargado o anulado
  	if(referenciaModulo.estadoTransporte== 2 || referenciaModulo.estadoTransporte == 4 || referenciaModulo.estadoTransporte == 5){
  		this.obj.btnModificarTransporte.addClass("disabled");
  	}
  	else{
  		this.obj.btnModificarTransporte.removeClass("disabled");
  	}
  	
};

moduloTransporte.prototype.desactivarBotones=function(){
	var referenciaModulo=this;
    //habilitamos agregarTransporte
	this.obj.btnAgregarTransporte.removeClass("disabled");
 	//estos botones deshabilitados
	this.obj.btnDetalle.addClass("disabled");
	this.obj.btnModificarTransporte.addClass("disabled");
	//this.obj.btnImportar.addClass("disabled");
	this.obj.btnVer.addClass("disabled");
	this.obj.btnEvento.addClass("disabled");
	this.obj.btnPesaje.addClass("disabled");
	//Desabilitadmos el botón agregar cuando el estado del día operativo sea descargando, cerrado o liquidado
  	if(referenciaModulo.estadoDiaOperativo == 3 || referenciaModulo.estadoDiaOperativo == 4 || referenciaModulo.estadoDiaOperativo == 5){
  		this.obj.btnAgregarTransporte.addClass("disabled");
  		this.obj.btnModificarTransporte.addClass("disabled");
  	}
  	else{
  		this.obj.btnAgregarTransporte.removeClass("disabled");
  		this.obj.btnModificarTransporte.addClass("disabled");
  	}
  	//Desabilitadmos el botón modificar cuando el estado del transporte sea inactivo, descargado o anulado
  	if(referenciaModulo.estadoTransporte== 2 || referenciaModulo.estadoTransporte == 4 || referenciaModulo.estadoTransporte == 5){
  		this.obj.btnModificarTransporte.addClass("disabled");
  	}
  	else{
  		this.obj.btnModificarTransporte.removeClass("disabled");
  	}
  	
  	//Agregado por req9000002570========================
  	this.obj.btnTiempoEtapa.addClass("disabled");
  	//==================================================

};

moduloTransporte.prototype.listarRegistros = function(){
	var referenciaModulo=this;
    referenciaModulo.mostrarDepuracion("listarRegistros");
	this.obj.datClienteApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
};

moduloTransporte.prototype.listarTransportes = function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("listarRegistros de tabla AsignacionTransporte");
  this.obj.datAsignacionTransporteApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	 
  this.obj.datTablaDetalleDiaOperativoApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
};

moduloTransporte.prototype.actualizarBandaInformacion=function(tipo, mensaje){
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

moduloTransporte.prototype.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);

/*	var doperativo = referenciaModulo.idDiaOperativo;
	var transporte = referenciaModulo.idTransporte;*/
	//esto para recuperar los datos de la cabecera de los formularios.
	referenciaModulo.datosCabecera(); 

	$.ajax({
	    type: "GET",
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: "application/json", 
//	    data: {ID:doperativo, IDTransporte: transporte},
	    data: {ID:referenciaModulo.idTransporte},
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} else {
	    		if (referenciaModulo.modoEdicion == constantes.MODO_FORMULARIO_TRANSPORTE_MODIFICAR){
	    			referenciaModulo.llenarFormulario(respuesta.contenido.carga[0]);
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    			referenciaModulo.obj.ocultaContenedorFormulario.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_VER_TRANSPORTE){
	    			referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    			referenciaModulo.obj.ocultaContenedorVista.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_PESAJE_TRANSPORTE){
	    			referenciaModulo.llenarPesajeTransporte(respuesta.contenido.carga[0]);
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	    			referenciaModulo.obj.ocultaContenedorFormularioPesaje.hide();
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


moduloTransporte.prototype.consultarSAP= function(){
	var ref=this;
	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");	
	var fechaInicio=ref.obj.cmpFiltroFechaInicialImportacion.val();
	var fechaFinal=ref.obj.cmpFiltroFechaFinalImportacion.val();
	var idOperacion=ref.obj.filtroOperacion.val();
	var parametros ={"idOperacion":idOperacion,"filtroFechaInicio":fechaInicio,"filtroFechaFinal":fechaFinal};
	console.log(parametros);
	ref.obj.ocultaContenedorFormularioImportar.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: ref.URL_CONSULTAR_SAP, 
	    contentType: ref.TIPO_CONTENIDO, 
	    data: parametros,	
	    success: function(respuesta) {
	    	ref.obj.ocultaContenedorFormularioImportar.hide();
	    	if (!respuesta.estado) {
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} 	else {
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		ref.pintarSap(respuesta.contenido.carga);
	    		ref.obj.ocultaContenedorVista.show();
    		}
	    },			    		    
	    error: function() {
	    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	    	ref.obj.ocultaContenedorFormularioImportar.hide();
	    }
	});
};

moduloTransporte.prototype.limpiarTablaSAP= function(valores){
	var nombreElemento ="";
	var listaGuias = valores.split(",");
	var numeroGuia="";
	var contador=0;
	console.log("listaGuias");
	console.log(listaGuias);
	var filas =[];
	var fila=0;
	try {
	    $('#tablaListaSap input:checked').each(function() {
	    	fila = $(this).attr('data-fila');
	    	filas.push(fila);
	    });	    
	    console.log(filas);	    
	    for(contador=0; contador < filas.length;contador++){
	    	fila = filas[contador];
	    	nombreElemento= "cmpDetalleNumeroGuia" +fila;
	    	numeroGuia = $("#"+ nombreElemento).text();
	    	console.log("numeroGuia");
	    	console.log(numeroGuia);
	    	console.log(listaGuias.indexOf(numeroGuia));
	    	if (listaGuias.indexOf(numeroGuia)>-1){
	            nombreElemento ="plantillaDetalleSap"+fila;
	            console.log(nombreElemento);
	            $("#"+ nombreElemento).css("color","red");
	            nombreElemento = "cmpSelectorDetalle"+fila;
	            $("#"+ nombreElemento).attr("disabled",true);
	            $("#"+ nombreElemento).prop( "checked", false );
	            nombreElemento ="cmpSituacion"+fila;
	            $("#"+ nombreElemento).text(utilitario.formatearSituacion(constantes.SITUACION_IMPORTADO));
	    	}
	    }
	}catch(error){
		console.log(error);
	}
};

moduloTransporte.prototype.guardarSAP= function(){
	var ref=this;
	var numeroGuiaRemision="";
	var fila=0;
	var elemento=null;
	var registro = {};
	ref.obj.ocultaContenedorFormularioImportar.show();
	registro.detalle= [];
	registro.idDiaOperativo =referenciaModulo.idDiaOperativo ;
    $('#tablaListaSap input:checked').each(function() {
    	fila = $(this).attr('data-fila');    
        numeroGuiaRemision=$("#cmpDetalleNumeroGuia"+fila).text();
        elemento=ref.listaMaestroSAP[numeroGuiaRemision];
        registro.detalle.push(elemento);
    });
    console.log(registro);
    
	$.ajax({
	    type: constantes.PETICION_TIPO_POST,
	    url: ref.URL_GRABAR_SAP, 
	    contentType: ref.TIPO_CONTENIDO, 
	    data: JSON.stringify(registro),	
	    success: function(respuesta) {
	    	ref.obj.ocultaContenedorFormularioImportar.hide();
	    	console.log(respuesta.valor);
	    	if (!respuesta.estado) {
	    		ref.limpiarTablaSAP(respuesta.valor);
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} 	else {
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);	    		
	    		ref.limpiarTablaSAP(respuesta.valor);
	    		console.log("ref.cmpFechaCarga " + ref.cmpFechaCarga);
	    		ref.obj.ocultaContenedorFormularioImportar.hide();
	    		
    		}
	    },			    		    
	    error: function() {
	    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	    	ref.obj.ocultaContenedorFormularioImportar.hide();
	    }
	});
};

moduloTransporte.prototype.pintarSap= function(registros){
	console.log("pintarSap");
	  var ref=this;
	  var numeroRegistros = registros.length;
	  var registro = null;
	  var clonDetalle=null;
	  ref.listaMaestroSAP={};
	  try {
		  $("#tablaListaSap > tbody:first").children().remove();
		  for(var contador=0;contador<numeroRegistros;contador++){
			  registro = registros[contador];
			  registro.fechaEmision=utilitario.formatearStringToDateYYYY_MM_DD(registro.fechaEmision);
			  ref.listaMaestroSAP[registro.numeroGuiaRemision]= registro;
			  clonDetalle = ref.obj.plantillaDetalleSap.clone();
			  clonDetalle.attr('id', "plantillaDetalleSap" + contador);
			  clonDetalle.attr('data-fila', contador);
			  var selector=clonDetalle.find("#cmpSelectorDetalle");
			  if (registro.situacion==1){
				  
			  } else if (registro.situacion==2){
				  selector.attr("disabled",true);
				  clonDetalle.css("color","red");
			  }
			  selector.attr('data-fila', contador);			  
			  selector.change(function() {
				  console.log("click");
			        if($(this).is(":checked")) {
			        	console.log("marcado");			        	
			        }
			  });
			  selector.attr('id', "cmpSelectorDetalle" + contador);
        
			  var cmpDetalleNumeroGuia=clonDetalle.find("#cmpDetalleNumeroGuia");
			  cmpDetalleNumeroGuia.text(registro.numeroGuiaRemision);
			  cmpDetalleNumeroGuia.attr('id', "cmpDetalleNumeroGuia" + contador);			   
			  
			  var cmpFechaEmision=clonDetalle.find("#cmpFechaEmision");
			  cmpFechaEmision.text(utilitario.formatearFecha2Cadena(registro.fechaEmision));
			  cmpFechaEmision.attr('id', "cmpFechaEmision" + contador);			  
			  
			  var cmpTractoCisterna=clonDetalle.find("#cmpTractoCisterna");
			  cmpTractoCisterna.text(registro.placaTracto+"-"+registro.placaCisterna);
			  cmpTractoCisterna.attr('id', "cmpTractoCisterna" + contador);
        
			  var cmpSCOP=clonDetalle.find("#cmpSCOP");
			  cmpSCOP.text(registro.codigoScop);
			  cmpSCOP.attr('id', "cmpSCOP" + contador);
        
			  var cmpVolumenObservado=clonDetalle.find("#cmpVolumenObservado");
			  cmpVolumenObservado.text(registro.volumenObservadoGuia.toFixed(2));
			  cmpVolumenObservado.attr('id', "cmpVolumenObservado" + contador);
        
			  var cmpVolumenCorregido=clonDetalle.find("#cmpVolumenCorregido");
			  cmpVolumenCorregido.text(registro.volumenCorregidoGuia.toFixed(2));
			  cmpVolumenCorregido.attr('id', "cmpVolumenCorregido" + contador);
			  
			  var cmpSituacion=clonDetalle.find("#cmpSituacion");
			  cmpSituacion.text(utilitario.formatearSituacion(registro.situacion));
			  cmpSituacion.attr('data-situacion',registro.situacion );
			  cmpSituacion.attr('id', "cmpSituacion" + contador);			  
			  
			  $("#tablaListaSap > tbody:last-child").append(clonDetalle);			  
		  }
	  }catch(error){
		  console.log(error);
	  }
};

moduloTransporte.prototype.verRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
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
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	    	referenciaModulo.obj.ocultaContenedorVista.show();
	    }
	});
};

moduloTransporte.prototype.guardarRegistro= function(){
	var referenciaModulo = this;
	var validaFormulario =  this.validarFormulario();
	
	if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,  cadenas.ERROR_VALORES_FORMULARIO);
	//} else if (referenciaModulo.obj.frmPrincipal.valid()){
	} else if (validaFormulario.length == 0){
		//var validar = this.validarFechaEmision();
		var validarFechaCubicacion = this.validarFechaVigenciaTarjetaCubicacion();
		//if (this.validarUnDetalleTransporte() && this.validarDetallesTransporte() && validar && validarFechaCubicacion){
		if (this.validarUnDetalleTransporte() && this.validarDetallesTransporte() && validarFechaCubicacion){
			
			referenciaModulo.obj.ocultaContenedorFormulario.show();
			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
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
			        	
			        	//referenciaModulo.recuperarRegistro();
			        	referenciaModulo.iniciarListado();
			        	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
			        }
			        referenciaModulo.obj.ocultaContenedorFormulario.hide();
				},			    		    
				error: function() {
		        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		     }
		   });
		} else{
			//referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Al menos un Detalle de Transporte debe estar informado.");
			//referenciaModulo.obj.ocultaContenedorFormulario.hide();
		}
	} else {
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, validaFormulario);
		referenciaModulo.obj.ocultaContenedorFormulario.hide();
	}
};

moduloTransporte.prototype.iniciarListado= function(mensaje){
	var referenciaModulo = this;
	console.log("entra en iniciarListado");
	try{
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
		this.obj.datAsignacionTransporteApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);
		this.obj.datTablaDetalleDiaOperativoApi.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
		referenciaModulo.desactivarBotones();
		//referenciaModulo.recuperarRegistro();
	} catch(error){
	    referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloTransporte.prototype.actualizarRegistro= function(){
	//Ocultar alertas de mensaje
	var referenciaModulo = this;
	var validaFormulario =  this.validarFormulario();
	if (!referenciaModulo.validaFormularioXSS("#frmPrincipal")){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	//} else if (referenciaModulo.obj.frmPrincipal.valid()){
	} else if (validaFormulario.length == 0){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
    referenciaModulo.obj.ocultaContenedorFormulario.show();
    //var validar = this.validarFechaEmision();
    var validarFechaCubicacion = this.validarFechaVigenciaTarjetaCubicacion();
    //IAC-1303if (this.validarUnDetalleTransporte() && this.validarDetallesTransporte() && validar && validarFechaCubicacion){
    if (this.validarUnDetalleTransporte() && this.validarDetallesTransporte() && validarFechaCubicacion){
	    var eRegistro = referenciaModulo.recuperarValores();
	    console.log(eRegistro);
			$.ajax({
			    type: constantes.PETICION_TIPO_POST,
			    url: referenciaModulo.URL_ACTUALIZAR, 
			    contentType: referenciaModulo.TIPO_CONTENIDO, 
			    data: JSON.stringify(eRegistro),	
			    success: function(respuesta) {
			    	if (!respuesta.estado) {
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			    	} 	else {		    			
			    		referenciaModulo.recuperarRegistro();
			    		referenciaModulo.iniciarListado(respuesta.mensaje);
		    		}
	          referenciaModulo.obj.ocultaContenedorFormulario.hide();
			    },			    		    
			    error: function() {
			    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	          referenciaModulo.obj.ocultaContenedorFormulario.hide();
			    }
			});
		} else{
			referenciaModulo.obj.ocultaContenedorFormulario.hide();
		}
	} else {
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, validaFormulario);
		referenciaModulo.obj.ocultaContenedorFormulario.hide();
	}	
};

moduloTransporte.prototype.iniciarContenedores = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_DETALLE_TRANSPORTE;
    	referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_TRANSPORTE);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntDetalleTransporte.show();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.obj.cntVistaRegistro.hide();
		referenciaModulo.obj.cntEventoTransporte.hide();
		referenciaModulo.obj.cntPesajeTransporte.hide();
		referenciaModulo.recuperarRegistro();
	} catch (error) {
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloTransporte.prototype.solicitarActualizarEstado= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloTransporte.prototype.actualizarEstadoRegistro= function(){
  var eRegistro = {};
  var referenciaModulo=this;
	referenciaModulo.obj.frmConfirmarModificarEstado.modal("hide");
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
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
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la petición");
        //referenciaModulo.protegeFormulario(false);
    }
    });
	};

	
moduloTransporte.prototype.validarUnDetalleTransporte= function(){
//Implementar en cada caso
};
	
moduloTransporte.prototype.limpiarFormularioPrincipal= function(){
//Implementar en cada caso
};

moduloTransporte.prototype.validarFechaEmision= function(){
//Implementar en cada caso
};

moduloTransporte.prototype.validarFechaVigenciaTarjetaCubicacion= function(){
//Implementar en cada caso
};

moduloTransporte.prototype.datosCabecera= function(){
//Implementar en cada caso
};
	
moduloTransporte.prototype.inicializarCampos= function(){
//Implementar en cada cas
};

//Agregado por 9000002570============================================
moduloTransporte.prototype.llenarFormularioTiempos = function(registro){
//Implementar en cada caso	
};

moduloTransporte.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloTransporte.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloTransporte.prototype.llenarDetalleTransporte = function(){
//Implementar en cada caso
};

moduloTransporte.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloTransporte.prototype.grillaDespuesSeleccionar= function(indice){
//TODO implementar
};

moduloTransporte.prototype.despuesListarRegistros=function(){
//Implementar en cada caso
};

moduloTransporte.prototype.validarFormulario=function(){
//Implementar en cada caso
};

moduloTransporte.prototype.validaPermisos= function(){
  var referenciaModulo = this;
  try{
  console.log("Validando permiso para: " + referenciaModulo.descripcionPermiso);
  referenciaModulo.obj.ocultaContenedorTabla.show();
  referenciaModulo.obj.ocultaContenedorDetalleTransporte.show();
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
	    	  referenciaModulo.obj.ocultaContenedorDetalleTransporte.hide();
	      } else {
	      	
		      if (referenciaModulo.descripcionPermiso == 'LEER_TRANSPORTE'){
		    	  referenciaModulo.botonDetalle();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_TRANSPORTE'){
		    	  referenciaModulo.botonAgregarTransporte();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_TRANSPORTE'){
		    	  referenciaModulo.botonModificarTransporte();
		      } else if (referenciaModulo.descripcionPermiso == 'IMPORTAR_TRANSPORTE'){
		    	  referenciaModulo.mostrarFormularioImportar();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_TRANSPORTE'){
		    	  referenciaModulo.botonVer();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_EVENTO_TRANSPORTE'){
		    	  referenciaModulo.botonEvento();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_PESAJE'){
		    	  referenciaModulo.botonPesaje();
		      }
	      }
	      referenciaModulo.obj.ocultaContenedorTabla.hide();
    	  referenciaModulo.obj.ocultaContenedorDetalleTransporte.hide();
	    },
	    error: function() {
	    	referenciaModulo.obj.ocultaContenedorTabla.hide();
	    	referenciaModulo.obj.ocultaContenedorDetalleTransporte.hide();
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	    }
	  });
  } catch(error){
	  referenciaModulo.obj.ocultaContenedorTabla.hide();
	  referenciaModulo.obj.ocultaContenedorDetalleTransporte.hide();
	  referenciaModulo.mostrarDepuracion(error.message);
  };
};
