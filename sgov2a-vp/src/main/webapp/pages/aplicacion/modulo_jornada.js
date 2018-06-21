function moduloJornada(){
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
  this.NUMERO_DECIMALES=2;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  //Inicializar propiedades
  this.urlBase='';  
  this.mensajeEsMostrado=false;
  this.idRegistro = 0;
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};

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
  
};

moduloJornada.prototype.mostrarDepuracion=function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};

moduloJornada.prototype.mostrarErrorServidor=function(xhr,estado,error){
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

moduloJornada.prototype.inicializar=function(){
	this.configurarAjax();
  this.inicializarControlesGenericos();
  this.inicializarGrillaJornada();
  this.inicializarCampos();
  this.inicializarFormularioApertura();
};

moduloJornada.prototype.configurarAjax=function(){
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

moduloJornada.prototype.resetearFormulario= function(){
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

moduloJornada.prototype.inicializarControlesGenericos=function(){
  this.mostrarDepuracion("inicializarControlesGenericos");
  var referenciaModulo=this;
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  //general
  this.obj.tituloSeccion=$("#tituloSeccion");
  this.obj.frmConfirmarAnularRegistro=$("#frmConfirmarAnularRegistro");
  this.obj.bandaInformacion=$("#bandaInformacion");
  this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");
  //formularios
  this.obj.frmApertura = $("#frmApertura");
  this.obj.frmCierre = $("#frmCierre");
  this.obj.frmConfirmarReapertura = $("#frmConfirmarReapertura");
  this.obj.frmCambioTanque = $("#frmCambioTanque");
  this.obj.frmMuestreo = $("#frmMuestreo");
  this.obj.frmConfirmarEliminarMuestra = $("#frmConfirmarEliminarMuestra");
  //filtros de busqueda
  this.obj.filtroOperacion=$("#filtroOperacion");
  this.obj.filtroEstacion=$("#filtroEstacion");
  this.obj.idCliente=$("#idCliente");
  this.obj.idJornada =$("#idJornada");
  this.obj.estadoJornada =$("#estadoJornada");
  //tablas
  this.obj.tablaJornada=$('#tablaJornada');
  //contenedores
  this.obj.cntTabla=$("#cntTabla");
  this.obj.cntFormularioAperturaJornada=$("#cntFormularioAperturaJornada");
  this.obj.cntFormularioCierreJornada=$("#cntFormularioCierreJornada");
  this.obj.cntVistaJornada=$("#cntVistaJornada");
  this.obj.cntFormCambioTanque=$("#cntFormCambioTanque");
  this.obj.cntFormularioMuestreoJornada=$("#cntFormularioMuestreoJornada");
  //oculta contenedores
  this.obj.ocultaContenedorTabla=$("#ocultaContenedorTabla");
  this.obj.ocultaContenedorAperturaJornada=$("#ocultaContenedorAperturaJornada");
  this.obj.ocultaContenedorCierreJornada=$("#ocultaContenedorCierreJornada");
  this.obj.ocultaContenedorVistaJornada=$("#ocultaContenedorVistaJornada");
  this.obj.ocultaCntCambioTanque=$("#ocultaCntCambioTanque");
  this.obj.ocultaContenedorMuestreoJornada=$("#ocultaContenedorMuestreoJornada");
  //Botones	
  this.obj.btnFiltrar=$("#btnFiltrar");
  this.obj.btnApertura=$("#btnApertura");
  this.obj.btnCierre=$("#btnCierre");
  this.obj.btnReapertura=$("#btnReapertura");
  this.obj.btnConfirmarReapertura=$("#btnConfirmarReapertura");
  this.obj.btnVer=$("#btnVer");
  this.obj.btnCambioTanque=$("#btnCambioTanque");
  this.obj.btnMuestreo=$("#btnMuestreo");

  this.obj.btnGuardarApertura=$("#btnGuardarApertura");
  this.obj.btnCancelarApertura=$("#btnCancelarApertura");
  this.obj.btnGuardarCierre=$("#btnGuardarCierre");
  this.obj.btnCancelarCierre=$("#btnCancelarCierre");
  this.obj.btnCerrarVistaJornada=$("#btnCerrarVistaJornada");
  this.obj.btnGuardarCambioTanque=$("#btnGuardarCambioTanque");
  this.obj.btnCancelarCambioTanque=$("#btnCancelarCambioTanque");
  this.obj.btnGuardarMuestreo=$("#btnGuardarMuestreo");
  this.obj.btnCancelarMuestreo=$("#btnCancelarMuestreo");
  this.obj.btnAgregarMuestra=$("#btnAgregarMuestra");
  this.obj.btnConfirmaEliminarMuestra=$("#btnConfirmaEliminarMuestra");

  //TODO
  //para guardar los datos de la jornada seleccionada
  this.obj.jornadaSeleccionado=$("#jornadaSeleccionado");
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
  this.obj.cmpFiltroUsuario=$("#cmpFiltroUsuario");
  this.obj.cmpFiltroTabla=$("#cmpFiltroTabla");
  this.obj.cmpFiltroTipoFecha=$("#cmpFiltroTipoFecha");	
  
  this.obj.btnFiltrar.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){ 
	  referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
	  referenciaModulo.listarRegistros();
  });
  
  this.obj.btnApertura.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
    referenciaModulo.descripcionPermiso = 'CREAR_JORNADA';
    referenciaModulo.validaPermisos();
//	referenciaModulo.botonApertura();
  });
  
  this.obj.btnCierre.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ACTUALIZAR_JORNADA';
	  referenciaModulo.validaPermisos();
	  //referenciaModulo.botonCierre();
  });

  this.obj.btnReapertura.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'ACTUALIZAR_ESTADO_JORNADA';
      referenciaModulo.validaPermisos();
	  //referenciaModulo.botonReapertura();
  });
  
  this.obj.btnConfirmarReapertura.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonConfirmarReapertura();
  });

  this.obj.btnVer.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'RECUPERAR_JORNADA';
      referenciaModulo.validaPermisos();
	  //referenciaModulo.botonVer();
  });
  
  this.obj.btnCambioTanque.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'CAMBIO_TANQUE_JORNADA';
      referenciaModulo.validaPermisos();
//	  referenciaModulo.botonCambioTanque();
  });

  this.obj.btnMuestreo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.descripcionPermiso = 'MUESTREO_JORNADA';
      referenciaModulo.validaPermisos();
	  //referenciaModulo.botonMuestreo();
  });
  
  this.obj.btnGuardarApertura.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonGuardarApertura();
  });
  
  this.obj.btnCancelarApertura.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarApertura();
  });

  this.obj.btnGuardarCierre.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonGuardarCierre();
  });
  
  this.obj.btnCancelarCierre.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarCierre();
  });
  
  this.obj.btnCerrarVistaJornada.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCerrarVistaJornada();
  });
  
  this.obj.btnGuardarCambioTanque.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonGuardarCambioTanque();
  });
  
  this.obj.btnCancelarCambioTanque.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarCambioTanque();
  });
  
  this.obj.btnGuardarMuestreo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonGuardarMuestreo();
  });
  
  this.obj.btnCancelarMuestreo.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonCancelarMuestreo();
  });
  
  this.obj.btnAgregarMuestra.on(referenciaModulo.NOMBRE_EVENTO_CLICK,function(){
	  referenciaModulo.botonAgregarMuestra();
  });
  
  this.obj.btnConfirmaEliminarMuestra.on("click",function(){
	  referenciaModulo.btnConfirmaEliminarMuestra();
  });

};

moduloJornada.prototype.botonApertura = function(){
  var referenciaModulo = this;
  try {
	  referenciaModulo.obj.ocultaContenedorTabla.show();
	  referenciaModulo.recuperarApertura();
  } catch(error){
    console.log(error.message);
    
  }
};

moduloJornada.prototype.botonGuardarApertura = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.guardarApertura();
		this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCancelarApertura = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.iniciarListado("");
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCierre = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.ocultaContenedorAperturaJornada.show();
		referenciaModulo.modoEdicion = constantes.MODO_CERRAR_JORNADA;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_CIERRE_JORNADA);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.CERRAR_DETALLE_PROGRAMACION);
		referenciaModulo.validarTurno();
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.validarJornadaCerrada= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorTabla.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: "./tanqueJornada/listar", 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {
	    	idOperacion: referenciaModulo.obj.filtroOperacion.val(),
		    idJornada: referenciaModulo.obj.idJornada,
		    tanqueDeCierre: 1
		},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {		 
	    		if(respuesta.contenido.carga.length > 0){
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Opcion no habilitada por haber tanques cerrados.");
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    		} else {
	    			referenciaModulo.validarTurno();
	    		}
	    	}
	    },			    		    
	    error: function(xhr,estado,error) {
	        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	        referenciaModulo.obj.ocultaContenedorTabla.hide();     
	    }
	});
};

moduloJornada.prototype.validarTurno= function(){
	var referenciaModulo = this;
	referenciaModulo.obj.cantTurnosEstacion =$("#cantTurnosEstacion");
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorTabla.show();
	var existeTurnoAbierto = 0;
	var cantTurnosListado = 0;
	var cantidadTurnosEstacion = 0;

	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: "./turno/listar", 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {idJornada:referenciaModulo.obj.idJornada },	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		console.log(respuesta.contenido);
	    		if(respuesta.contenido.carga.length == 0){
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia operativo no tiene turnos, favor verifique.");
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    		} else {
	    			cantTurnosListado = respuesta.contenido.carga.length;
	    			for (var x = 0; x < respuesta.contenido.carga.length; x ++){
	    				var registro = respuesta.contenido.carga[x];
	    				if (registro.estado == constantes.TIPO_TURNO_ABIERTO){
	    					existeTurnoAbierto++;
	    				}
	    			}
	    			cantidadTurnosEstacion = respuesta.contenido.carga[0].jornada.estacion.cantidadTurnos;
		    		if(existeTurnoAbierto == 0){
		    			if(referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
		    				if(cantTurnosListado < cantidadTurnosEstacion){      
		    					referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "La Estación debe contar con " + cantidadTurnosEstacion + " turnos cerrados. Favor verifique.");
			    				referenciaModulo.obj.ocultaContenedorTabla.hide();
		    				} else {
				    			referenciaModulo.obj.cntFormularioAperturaJornada.hide();
				    		    referenciaModulo.obj.cntVistaJornada.hide();
				    		    referenciaModulo.obj.cntFormCambioTanque.hide();
				    		    referenciaModulo.obj.cntFormularioMuestreoJornada.hide();
				    		    referenciaModulo.obj.ocultaContenedorTabla.hide();
				    		    referenciaModulo.recuperarRegistro();
		    				}
		    			} else if (referenciaModulo.modoEdicion == constantes.MODO_CAMBIO_TANQUE_JORNADA){
		    				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia operativo no tiene turnos abiertos para hacer el cambio de tanques, favor verifique.");
		    				referenciaModulo.obj.ocultaContenedorTabla.hide();
		    			} else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
		    				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia operativo no tiene turnos abiertos para hacer registro de muestreos, favor verifique.");
		    				referenciaModulo.obj.ocultaContenedorTabla.hide();
		    			}
		    		} else {
		    			if(referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
		    				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia operativo tiene turnos abiertos, favor verifique.");
		    				referenciaModulo.obj.ocultaContenedorTabla.hide();
		    			} else if(referenciaModulo.modoEdicion == constantes.MODO_CAMBIO_TANQUE_JORNADA){
			    			referenciaModulo.obj.cntTabla.hide();
			    		    referenciaModulo.obj.cntFormularioAperturaJornada.hide();
			    		    referenciaModulo.obj.cntFormularioCierreJornada.hide();
			    		    referenciaModulo.obj.cntVistaJornada.hide();
			    		    referenciaModulo.obj.cntFormCambioTanque.show();
			    		    referenciaModulo.obj.cntFormularioMuestreoJornada.hide();
			    		    referenciaModulo.limpiarFormularioCambioTanque();
			    		    referenciaModulo.recuperarRegistro();
			    		} else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
			    			referenciaModulo.obj.cntTabla.hide();
			    			referenciaModulo.obj.cntFormularioAperturaJornada.hide();
			    		    referenciaModulo.obj.cntFormularioCierreJornada.hide();
			    		    referenciaModulo.obj.cntVistaJornada.hide();
			    		    referenciaModulo.obj.cntFormCambioTanque.hide();
			    		    referenciaModulo.obj.cntFormularioMuestreoJornada.show();
			    		    referenciaModulo.obj.ocultaContenedorMuestreoJornada.show();
			    		    referenciaModulo.recuperarRegistro();
			    		}
		    		}
	    		}
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorTabla.hide();     
        if (referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
        	referenciaModulo.obj.ocultaContenedorCierreJornada.hide();
        } else if (referenciaModulo.modoEdicion == constantes.MODO_DETALLE_JORNADA){
          referenciaModulo.obj.ocultaContenedorVistaJornada.hide();
	    } else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
          referenciaModulo.obj.ocultaContenedorMuestreoJornada.hide();
        } 
	    }
	});
};

moduloJornada.prototype.cantidadTurnos= function(){
	var referenciaModulo = this;
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: "./estacion/recuperar", 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.obj.filtroEstacion.val() },	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if(respuesta.contenido.carga.length == 0){
	    			referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El dia operativo no tiene turnos, favor verifique.");
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    		} else {
	    			var registro = respuesta.contenido.carga[0];
	    		}
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorTabla.hide();     
	    }
	});
};

moduloJornada.prototype.botonGuardarCierre = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.actualizarRegistro();
		this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCancelarCierre = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.iniciarListado("");
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonReapertura= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarReapertura.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloJornada.prototype.botonConfirmarReapertura= function(){
  var eRegistro = {};
  var referenciaModulo=this;
  referenciaModulo.obj.frmConfirmarReapertura.modal("hide");
  referenciaModulo.obj.ocultaContenedorTabla.show();
  referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  eRegistro.id = parseInt(referenciaModulo.obj.idJornada);
  eRegistro.estado = constantes.TIPO_JORNADA_ABIERTO;
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
        referenciaModulo.listarRegistros();
        referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
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

moduloJornada.prototype.botonVer = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion=constantes.MODO_DETALLE_JORNADA;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_JORNADA);
		referenciaModulo.obj.cntTabla.hide();
		referenciaModulo.obj.cntFormularioAperturaJornada.hide();
        referenciaModulo.obj.cntFormularioCierreJornada.hide();
        referenciaModulo.obj.cntVistaJornada.show();
        referenciaModulo.obj.cntFormCambioTanque.hide();
        referenciaModulo.obj.cntFormularioMuestreoJornada.hide();
        referenciaModulo.obj.ocultaContenedorVistaJornada.show();
        referenciaModulo.recuperarRegistro();
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCerrarVistaJornada = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.iniciarListado("");
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.limpiarFormularioCambioTanque = function(){
	  var referenciaModulo=this;
	  referenciaModulo.obj.frmCambioTanque[0].reset();
	  referenciaModulo.obj.GrupoCambioTanquesFinal.removeAllForms();
	  referenciaModulo.obj.GrupoCambioTanquesInicial.removeAllForms();
};

moduloJornada.prototype.botonCambioTanque = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.ocultaContenedorAperturaJornada.show();
		referenciaModulo.modoEdicion = constantes.MODO_CAMBIO_TANQUE_JORNADA;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_CAMBIO_TANQUE_JORNADA);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.INICIAR_OPERACION);
		
		referenciaModulo.validarJornadaCerrada();
		
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonGuardarCambioTanque = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.guardarCambioTanque();
		this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCancelarCambioTanque = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.iniciarListado("");
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.CANCELAR_OPERACION);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonMuestreo = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.obj.ocultaContenedorAperturaJornada.show();
		referenciaModulo.modoEdicion = constantes.MODO_MUESTREO_JORNADA;
		referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_MUESTREO_JORNADA);
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.CERRAR_DETALLE_PROGRAMACION);
		referenciaModulo.validarJornadaCerrada();
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonGuardarMuestreo = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.actualizarRegistro();
		this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);
	} catch(error){
	  console.log(error.message);
	}
};

moduloJornada.prototype.botonCancelarMuestreo = function(){
	var referenciaModulo = this;
	try {
		referenciaModulo.iniciarListado("");
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.CANCELAR_OPERACION);
	} catch(error){
	  console.log(error.message);
	}
};

  moduloJornada.prototype.btnConfirmaEliminarMuestra = function(){
	var referenciaModulo = this;
    moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    try {
      $.ajax({
    	type: constantes.PETICION_TIPO_POST,
	    url: './vigencia/eliminar', 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:moduloActual.registrosEliminar},	
	    success: function(respuesta) {
	      if (!respuesta.estado) {
	    	  moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	      } else {		    				    			    		
	    	moduloActual.listarRegistros();
	    	moduloActual.obj.frmConfirmarModificarEstado.modal("hide");
	    	moduloActual.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
	      };
	    },			    		    
	    error: function() {
	    	moduloActual.mostrarErrorServidor(xhr,estado,error); 
	    }
      });
    }  catch(error){
	  moduloActual.mostrarDepuracion(error.message);
    }
  };

//para el listado de Jornadas
moduloJornada.prototype.llamadaAjaxGrillaJornada=function(e,configuracion,json){
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
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,json.mensaje);
    } else {

    }
  }
  if (referenciaModulo.estaCargadaInterface==false){        
    referenciaModulo.estaCargadaInterface=true;
  }
  referenciaModulo.obj.ocultaContenedorTabla.hide();  
};

moduloJornada.prototype.inicializarGrillaJornada=function(){
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
    referenciaModulo.obj.ocultaContenedorTabla.show();
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
		    referenciaModulo.obj.idJornada = referenciaModulo.obj.datJornadaAPI.cell(indiceFila,1).data();
		    referenciaModulo.obj.estadoJornada = referenciaModulo.obj.datJornadaAPI.cell(indiceFila,8).data();

	    	referenciaModulo.desactivarBotones();
	    	referenciaModulo.obj.btnVer.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    if(referenciaModulo.obj.estadoJornada == 1 ||  referenciaModulo.obj.estadoJornada == 2){
		    	referenciaModulo.obj.btnCierre.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    	referenciaModulo.obj.btnCambioTanque.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    	referenciaModulo.obj.btnMuestreo.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    }
		    if(referenciaModulo.obj.estadoJornada == 3){
		    	referenciaModulo.obj.btnReapertura.removeClass(constantes.CSS_CLASE_DESHABILITADA);
		    }
		}
	});
};

moduloJornada.prototype.recuperarApertura= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	$.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: './jornada/recuperar-apertura', 
        contentType: referenciaModulo.TIPO_CONTENIDO, 
        data: {
        	idOperacion:referenciaModulo.obj.filtroOperacion.val(),
        	filtroEstacion : referenciaModulo.obj.filtroEstacion.val()
	    },
        success: function(respuesta) {
          if (!respuesta.estado) {
            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
          } else {
        	referenciaModulo.resetearFormularioApertura();
            referenciaModulo.modoEdicion = constantes.MODO_APERTURAR_JORNADA;
            referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_APERTURA_JORNADA);
            referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, cadenas.TITULO_DETALLE_JORNADA);
    	    referenciaModulo.datosCabecera();//esto para llenar la cabecera
    	    referenciaModulo.desactivarBotones();
    	    referenciaModulo.obj.ocultaContenedorTabla.hide();
            referenciaModulo.obj.cntTabla.hide();
            referenciaModulo.obj.cntFormularioCierreJornada.hide();
            referenciaModulo.obj.cntVistaJornada.hide();
            referenciaModulo.obj.cntFormCambioTanque.hide();
            referenciaModulo.obj.cntFormularioMuestreoJornada.hide();
            referenciaModulo.llenarApertura(respuesta.contenido.carga[0]);
            referenciaModulo.obj.cntFormularioAperturaJornada.show();
          }
          referenciaModulo.obj.ocultaContenedorTabla.hide();
          referenciaModulo.obj.ocultaContenedorAperturaJornada.hide();
        },
        error: function(xhr,estado,error) {
          referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        }
      }); 
};

moduloJornada.prototype.guardarApertura= function(){  
	var referenciaModulo = this;
   referenciaModulo.mostrarDepuracion("guardarApertura");
	if (referenciaModulo.obj.frmApertura.valid()){
		referenciaModulo.obj.ocultaContenedorAperturaJornada.show();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		var eRegistro = referenciaModulo.recuperarValoresApertura();
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
				referenciaModulo.obj.ocultaContenedorAperturaJornada.hide();
			},			    		    
			error: function() {
			referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
			}
		});
	} else {
    referenciaModulo.obj.ocultaContenedorFormulario.hide();
	}
};

moduloJornada.prototype.inicializarFormularioApertura= function(){  
    //Establecer validaciones del formulario
    var referenciaModulo=this;
    this.obj.frmApertura.validate({
    rules: referenciaModulo.reglasValidacionFormularioApertura,
    messages: referenciaModulo.mensajesValidacionFormularioApertura,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloJornada.prototype.inicializarFormularioCierre= function(){  
    //Establecer validaciones del formulario
    var referenciaModulo=this;
    this.obj.frmCierre.validate({
    rules: referenciaModulo.reglasValidacionFormularioCierre,
    messages: referenciaModulo.mensajesValidacionFormularioCierre,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloJornada.prototype.inicializarFormularioCambioTanque= function(){  
    //Establecer validaciones del formulario
    var referenciaModulo=this;
    this.obj.frmCambioTanque.validate({
    rules: referenciaModulo.reglasValidacionFormularioCambioTanque,
    messages: referenciaModulo.mensajesValidacionFormularioCambioTanque,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloJornada.prototype.inicializarFormularioMuestreo= function(){  
    //Establecer validaciones del formulario
    var referenciaModulo=this;
    this.obj.frmMuestreo.validate({
    rules: referenciaModulo.reglasValidacionFormularioMuestreo,
    messages: referenciaModulo.mensajesValidacionFormularioMuestreo,
    submitHandler: function(form) {
    // form.submit();
    }
  });
};

moduloJornada.prototype.activarBotones=function(){
	this.obj.btnCierre.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnReapertura.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnVer.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnCambioTanque.removeClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnMuestreo.removeClass(constantes.CSS_CLASE_DESHABILITADA);
};

moduloJornada.prototype.desactivarBotones=function(){
	this.obj.btnCierre.addClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnReapertura.addClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnVer.addClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnCambioTanque.addClass(constantes.CSS_CLASE_DESHABILITADA);
	this.obj.btnMuestreo.addClass(constantes.CSS_CLASE_DESHABILITADA);
 };

moduloJornada.prototype.listarRegistros = function(){
  var referenciaModulo=this;
  referenciaModulo.mostrarDepuracion("listarRegistros");  
  this.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	 	
};

moduloJornada.prototype.actualizarBandaInformacion=function(tipo, mensaje){
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

moduloJornada.prototype.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO, cadenas.PROCESANDO_PETICION);
	referenciaModulo.obj.ocultaContenedorTabla.show();
	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: referenciaModulo.TIPO_CONTENIDO, 
	    data: {ID:referenciaModulo.obj.idJornada },	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, respuesta.mensaje);
	    	} else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if (referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
	    			referenciaModulo.obj.cntTabla.hide();
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    			referenciaModulo.obj.cntFormularioCierreJornada.show();
	    			referenciaModulo.obj.ocultaContenedorCierreJornada.show();
	    			referenciaModulo.resetearFormularioCierre();
	    			referenciaModulo.datosCabecera();
	    			referenciaModulo.desactivarBotones();
	                referenciaModulo.llenarFormularioCierre(respuesta.contenido.carga[0]);
	                referenciaModulo.obj.ocultaContenedorCierreJornada.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_DETALLE_JORNADA){
	    			referenciaModulo.datosCabecera();
	    			referenciaModulo.desactivarBotones();
	    			referenciaModulo.llenarDetalleJornada(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    			referenciaModulo.obj.ocultaContenedorVistaJornada.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_CAMBIO_TANQUE_JORNADA){
	    			referenciaModulo.datosCabecera();
	    			referenciaModulo.desactivarBotones();
	    			referenciaModulo.llenarFormularioCambioTanque(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    			referenciaModulo.obj.ocultaCntCambioTanque.hide();
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
	    			referenciaModulo.datosCabecera();
	    			referenciaModulo.desactivarBotones();
	    			referenciaModulo.llenarFormularioMuestreo(respuesta.contenido.carga[0]);
	    			referenciaModulo.obj.ocultaContenedorTabla.hide();
	    			referenciaModulo.obj.ocultaContenedorMuestreoJornada.hide();
	    		}
    		}
	    },			    		    
	    error: function(xhr,estado,error) {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorTabla.hide();     
        if (referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
        	referenciaModulo.obj.ocultaContenedorCierreJornada.hide();
        } else if (referenciaModulo.modoEdicion == constantes.MODO_DETALLE_JORNADA){
          referenciaModulo.obj.ocultaContenedorVistaJornada.hide();
	    } else if (referenciaModulo.modoEdicion == constantes.MODO_CAMBIO_TANQUE_JORNADA){
          referenciaModulo.obj.ocultaCntCambioTanque.hide();
        } else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
          referenciaModulo.obj.ocultaContenedorMuestreoJornada.hide();
	    }
	  }
	});
};

moduloJornada.prototype.verRegistro= function(){
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

moduloJornada.prototype.guardarRegistro= function(){  
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
	}
};

moduloJornada.prototype.actualizarRegistro= function(){
  //Ocultar alertas de mensaje
  var referenciaModulo = this;
  var eRegistro = {};

  //if (referenciaModulo.obj.frmPrincipal.valid()){
  	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
  	if (referenciaModulo.modoEdicion == constantes.MODO_CERRAR_JORNADA){
  		console.log("entra en actualizarRegistro por cierre de jornada");
  		referenciaModulo.obj.ocultaContenedorCierreJornada.show();
  		eRegistro = referenciaModulo.recuperarValoresCierre();
  	} else if (referenciaModulo.modoEdicion == constantes.MODO_MUESTREO_JORNADA){
  		console.log("entra en actualizarRegistro por muestreo");
  		referenciaModulo.obj.ocultaContenedorMuestreoJornada.show();
  		eRegistro = referenciaModulo.recuperarValoresMuestreo();
	}
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: referenciaModulo.URL_ACTUALIZAR, 
      contentType: referenciaModulo.TIPO_CONTENIDO,
      data: JSON.stringify(eRegistro),	
      success: function(respuesta) {
        if (!respuesta.estado) {
          referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          referenciaModulo.iniciarListado(respuesta.mensaje);
        }
        referenciaModulo.obj.ocultaContenedorCierreJornada.hide();
        referenciaModulo.obj.ocultaContenedorMuestreoJornada.hide();
      },			    		    
      error: function() {
        referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
        referenciaModulo.obj.ocultaContenedorCierreJornada.hide();
        referenciaModulo.obj.ocultaContenedorMuestreoJornada.hide();
      }
    });
  /*} else {

  }	*/
};

moduloJornada.prototype.guardarCambioTanque= function(){  
	var referenciaModulo = this;
	var eRegistro = {};
	
   referenciaModulo.mostrarDepuracion("guardarCambioTanque");
	//if (referenciaModulo.obj.frmCambioTanque.valid()){
	referenciaModulo.obj.ocultaCntCambioTanque.show();
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	eRegistro = referenciaModulo.recuperarValoresCambioTanque();
	//var eTanqueJornadaInicial = referenciaModulo.recuperarValoresCambioTanqueInicial();
	$.ajax({
		type: constantes.PETICION_TIPO_POST,
		url: referenciaModulo.URL_GUARDAR_CAMBIO_TANQUE, 
		contentType: referenciaModulo.TIPO_CONTENIDO, 
		data: JSON.stringify(eRegistro),	
		success: function(respuesta) {
			if (!respuesta.estado) {
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			} else {
				referenciaModulo.iniciarListado(respuesta.mensaje);
			}
			referenciaModulo.obj.ocultaCntCambioTanque.hide();
		},			    		    
		error: function() {
		referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
		}
	});
};

moduloJornada.prototype.iniciarListado= function(mensaje){
	var referenciaModulo = this;
	try {
		referenciaModulo.modoEdicion = constantes.MODO_LISTAR;
	    referenciaModulo.obj.tituloSeccion.text(cadenas.TITULO_LISTADO_REGISTROS);
	    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, mensaje);
	    referenciaModulo.obj.datJornadaAPI.ajax.reload(referenciaModulo.despuesListarRegistros,true);	
	  	referenciaModulo.desactivarBotones();
	    referenciaModulo.obj.cntTabla.show();
        referenciaModulo.obj.cntFormularioCierreJornada.hide();
        referenciaModulo.obj.cntVistaJornada.hide();
        referenciaModulo.obj.cntFormCambioTanque.hide();
        referenciaModulo.obj.cntFormularioMuestreoJornada.hide();
        referenciaModulo.obj.cntFormularioAperturaJornada.hide();
	} catch(error){
		console.log(error);
    referenciaModulo.mostrarDepuracion(error.message);
	};
};

moduloJornada.prototype.iniciarContenedores = function(){
  var referenciaModulo = this;
  try {
   // referenciaModulo.obj.cntFormulario.hide();	
   // referenciaModulo.protegeFormulario(false);
    referenciaModulo.obj.cntTabla.show();
  } catch(error){
    referenciaModulo.mostrarDepuracion(error.message);
  };
};

moduloJornada.prototype.solicitarActualizarEstado= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		referenciaModulo.mostrarDepuracion(error.message);
	}
};

moduloJornada.prototype.actualizarEstadoRegistro= function(){
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

moduloJornada.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloJornada.prototype.llenarFormularioCierre = function(registro){
//Implementar en cada caso	
};

moduloJornada.prototype.llenarDetalleJornada = function(registro){
//Implementar en cada caso
};

moduloJornada.prototype.llenarApertura = function(registro){
//Implementar en cada caso
};

moduloJornada.prototype.recuperarValoresApertura = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloJornada.prototype.recuperarValoresCierre = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloJornada.prototype.recuperarValoresMuestreo = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloJornada.prototype.grillaDespuesSeleccionar= function(indice){
};

moduloJornada.prototype.despuesListarRegistros=function(){	//Implementar en cada caso	
};

moduloJornada.prototype.resetearFormularioApertura= function(){	//Implementar en cada caso
};

moduloJornada.prototype.botonAgregarMuestra= function(){	//Implementar en cada caso
};

moduloJornada.prototype.validaFormularioXSS= function(formulario){
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

moduloJornada.prototype.validaPermisos= function(){
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
		      if (referenciaModulo.descripcionPermiso == 'LEER_JORNADA'){
		    	  referenciaModulo.Listar();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_JORNADA'){
		    	  referenciaModulo.botonApertura();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_ESTADO_JORNADA'){
		    	  referenciaModulo.botonReapertura();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_JORNADA'){
		    	  referenciaModulo.botonCierre();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_JORNADA'){
		    	  referenciaModulo.botonVer();
		      } else if (referenciaModulo.descripcionPermiso == 'CAMBIO_TANQUE_JORNADA'){
		    	  referenciaModulo.botonCambioTanque();
		      } else if (referenciaModulo.descripcionPermiso == 'MUESTREO_JORNADA'){
		    	  referenciaModulo.botonMuestreo();
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