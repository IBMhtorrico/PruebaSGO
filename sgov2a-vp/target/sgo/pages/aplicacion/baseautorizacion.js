function moduloBaseAutorizacion (){
  this.obj={};
  this.NUMERO_REGISTROS_PAGINA = constantes.NUMERO_REGISTROS_PAGINA;
  this.TOPES_PAGINACION = constantes.TOPES_PAGINACION;
  this.URL_LENGUAJE_GRILLA = "tema/datatable/language/es-ES.json";
  this.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;
  this.NOMBRE_EVENTO_CLICK=constantes.NOMBRE_EVENTO_CLICK;
  this.modoEdicion=constantes.MODO_LISTAR;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  //Inicializar propiedades
  this.urlBase='';  
  this.mensajeEsMostrado=false;
  this.idRegistro = 0;
  this.ordenGrilla=[[ 1, 'asc' ]];
  this.columnasGrilla=[{ "data": null} ];//Target 0
 // this.cmpFitlroEstado.val("2");
  this.definicionColumnas=[ {
  	"targets": 0,
    "searchable": false,
    "orderable": false,      
    "visible":true,
    "render": function ( datos, tipo, fila, meta ) {
  	  var configuracion =meta.settings;
  	  return configuracion._iDisplayStart + meta.row + 1;
    }
  }];//Target 0
  
};

moduloBaseAutorizacion.prototype.mostrarErrorServidor=function(xhr,estado,error){
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

moduloBaseAutorizacion.prototype.mostrarDepuracion = function(mensaje){
  var referenciaModulo=this;
  if (referenciaModulo.depuracionActivada=true){
    console.log(mensaje);
  }
};
	
moduloBaseAutorizacion.prototype.inicializar=function(){
  this.mostrarDepuracion("inicializar");
  this.configurarAjax();
  this.inicializarControlesGenericos();
  //this.inicializarGrilla();
  this.recuperarAutorizaciones();
  this.inicializarFormularioPrincipal();
  this.inicializarCampos();
  
};

moduloBaseAutorizacion.prototype.configurarAjax=function(){
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

moduloBaseAutorizacion.prototype.recuperarAutorizaciones=function(){

};


moduloBaseAutorizacion.prototype.resetearFormulario= function(){
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

moduloBaseAutorizacion.prototype.inicializarControlesGenericos=function(){
  var referenciaModulo=this;
  	//Inicializa botones
  	this.obj.cntAutorizacion=$("#cntAutorizacion");
	this.obj.frmPrincipal = $("#frmPrincipal");
	
	this.obj.btnGuardarAutorizacion=$("#btnGuardarAutorizacion");
	this.obj.btnCancelarAutorizacion=$("#btnCancelarAutorizacion");
	
	
	this.obj.tablaPrincipal=$('#tablaPrincipal');

	this.obj.cntTabla=$("#cntTabla");
	this.obj.cntFormulario=$("#cntFormulario");
	this.obj.cmpTituloFormulario=$("#cmpTituloFormulario");
	
	this.obj.cntVistaRegistro=$("#cntVistaRegistro");
	//Inicializar controles	
	this.obj.frmConfirmarModificarEstado=$("#frmConfirmarModificarEstado");	
	this.obj.frmConfirmarEliminar=$("#frmConfirmarEliminar");
	this.obj.ocultaFormulario=$("#ocultaFormulario");
	this.obj.bandaInformacion=$("#bandaInformacion");
  	//Botones	
	this.obj.btnListar=$("#btnListar");
	this.obj.btnAgregar=$("#btnAgregar");
	this.obj.btnModificar=$("#btnModificar");
	this.obj.btnModificarEstado=$("#btnModificarEstado");
	
	
	this.obj.btnConfirmarModificarEstado=$("#btnConfirmarModificarEstado");
	this.obj.btnEliminar=$("#btnEliminar");
	this.obj.btnVer=$("#btnVer");
	this.obj.btnConfirmarEliminar=$("#btnConfirmarEliminar");	
	this.obj.btnGuardar=$("#btnGuardar");
	this.obj.btnArribo=$("#btnArribo");
	this.obj.btnCancelarGuardar=$("#btnCancelarGuardar");
	this.obj.btnCerrarVista=$("#btnCerrarVista");
	this.obj.btnFiltrar=$("#btnFiltrar");
	//estos valores para hacer los filtros de los listados
	
	this.obj.txtFiltro=$("#txtFiltro");
	this.obj.cmpFiltroEstado=$("#cmpFiltroEstado");
	this.obj.cmpFechaInicial=$("#cmpFechaInicial");
	this.obj.cmpFechaFinal=$("#cmpFechaFinal");
	this.obj.filtroOperacion=$("#filtroOperacion");
	this.obj.filtroFechaPlanificada=$("#filtroFechaPlanificada");
	this.obj.cmpFiltroUsuario=$("#cmpFiltroUsuario");
	this.obj.cmpFiltroTabla=$("#cmpFiltroTabla");
	this.obj.cmpFiltroTipoFecha=$("#cmpFiltroTipoFecha");
	
	this.obj.btnFiltrar.on("click",function(){
		referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
		referenciaModulo.listarRegistros();
	});
	
	this.obj.btnListar.on("click",function(){
		referenciaModulo.modoEdicion=constantes.MODO_LISTAR;
		referenciaModulo.listarRegistros();
	});

	this.obj.btnAgregar.on("click",function(){
		referenciaModulo.iniciarAgregar();
	});
	
	this.obj.btnModificar.on("click",function(){
		referenciaModulo.iniciarModificar();
	});
	
	this.obj.btnModificarEstado.on("click",function(){
		referenciaModulo.solicitarActualizarEstado();
	});
	
	this.obj.btnConfirmarModificarEstado.on("click",function(){
		referenciaModulo.actualizarEstadoRegistro();
	});	
	
	this.obj.btnEliminar.on("click",function(){
		referenciaModulo.solicitarEliminar();
	});
	
	this.obj.btnConfirmarEliminar.on("click",function(){
		referenciaModulo.eliminarRegistro();		
	});	

	this.obj.btnVer.on("click",function(){
		referenciaModulo.iniciarVer();		
	});
	
	this.obj.btnCancelarGuardar.on("click",function(){
		referenciaModulo.iniciarCancelar();
	});
	
	this.obj.btnCerrarVista.on("click",function(){
		referenciaModulo.iniciarCerrarVista();
	});
	
	this.obj.btnGuardar.on("click",function(){
		referenciaModulo.iniciarGuardar();
	});
};

moduloBaseAutorizacion.prototype.iniciarGuardar = function(){
	var referenciaModulo=this;
	try {
		if (referenciaModulo.modoEdicion == constantes.MODO_NUEVO){
			referenciaModulo.guardarRegistro();
		} else if  (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
			referenciaModulo.actualizarRegistro();
		}
	} catch(error){
		console.log(error.message);
	};
};

moduloBaseAutorizacion.prototype.iniciarAgregar= function(){
	var referenciaModulo=this;
	try {
	referenciaModulo.modoEdicion=constantes.MODO_NUEVO;
	referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_AGREGAR_REGISTRO);
	referenciaModulo.resetearFormulario();
	referenciaModulo.obj.cntTabla.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntFormulario.show();
	} catch(error){
		console.log(error.message);
	};
};

moduloBaseAutorizacion.prototype.iniciarModificar= function(){
	var referenciaModulo=this;
	referenciaModulo.modoEdicion=constantes.MODO_ACTUALIZAR;
	referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_MODIFICA_REGISTRO);
	referenciaModulo.obj.cntTabla.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntFormulario.show();
	referenciaModulo.recuperarRegistro();
};

moduloBaseAutorizacion.prototype.iniciarVer= function(){
	var referenciaModulo=this;
	referenciaModulo.modoEdicion=constantes.MODO_VER;
	referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_DETALLE_REGISTRO);
	referenciaModulo.obj.cntTabla.hide();
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.show();
	referenciaModulo.recuperarRegistro();
};

moduloBaseAutorizacion.prototype.iniciarCancelar=function(){
	var referenciaModulo=this;
	referenciaModulo.resetearFormulario();
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntTabla.show();
};

moduloBaseAutorizacion.prototype.iniciarCerrarVista=function(){
	var referenciaModulo=this;
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntTabla.show();
};

moduloBaseAutorizacion.prototype.inicializarGrilla=function(){
	var referenciaModulo=this;
	//Establecer grilla y su configuracion		
	this.obj.tablaPrincipal.on('xhr.dt', function (e,settings,json) {
		if (json.estado==true){
			json.recordsTotal=json.contenido.totalRegistros;
			json.recordsFiltered=json.contenido.totalEncontrados;
			json.data= json.contenido.carga;
			if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
			}
			//
		} else {
			//referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,json.mensaje);
			json.recordsTotal=0;
			json.recordsFiltered=0;
			json.data= {};
			if (referenciaModulo.modoEdicion==constantes.MODO_LISTAR){
				referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
			} else {
				
			}
		}
	   //Nota no retornar el objeto solo manipular directamente
	});

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
			    "url": referenciaModulo.URL_LISTAR,
			    "type":"GET",
			    "data": function (d) {
		            var indiceOrdenamiento = d.order[0].column;
					d.registrosxPagina =  d.length; 
					d.inicioPagina = d.start; 
		            d.campoOrdenamiento= d.columns[indiceOrdenamiento].data;
		            d.sentidoOrdenamiento=d.order[0].dir;
		            d.valorBuscado=d.search.value;
		            
		            //d.valorBuscado=referenciaModulo.obj.textoFiltro.val();
		            d.txtFiltro = referenciaModulo.obj.txtFiltro.val();
		            d.filtroEstado= referenciaModulo.obj.cmpFiltroEstado.val();
		            d.filtroOperacion= referenciaModulo.obj.filtroOperacion.val();
		            d.filtroFechaPlanificada= referenciaModulo.obj.filtroFechaPlanificada.val();
		            d.filtroFechaInicio= referenciaModulo.obj.cmpFechaInicial.val();	
		            d.filtroFechaFinal = referenciaModulo.obj.cmpFechaFinal.val();	
		            d.filtroUsuario= referenciaModulo.obj.cmpFiltroUsuario.val();
		            d.filtroTabla= referenciaModulo.obj.cmpFiltroTabla.val();
			    }
		},
		"columns": referenciaModulo.columnasGrilla,
		"columnDefs": referenciaModulo.definicionColumnas
		//"order": referenciaModulo.ordenGrilla
	});	
	
	$('#tablaPrincipal tbody').on( 'click', 'tr', function () {
		if (referenciaModulo.obj.datClienteApi.data().length>0){
		     if ( $(this).hasClass('selected') ) {
		            $(this).removeClass('selected');
		     } else {
		    	 referenciaModulo.obj.datClienteApi.$('tr.selected').removeClass('selected');
		     	$(this).addClass('selected');
		     }
			var indiceFila = referenciaModulo.obj.datClienteApi.row( this ).index();
			var idRegistro = referenciaModulo.obj.datClienteApi.cell(indiceFila,1).data();

			referenciaModulo.idRegistro=idRegistro;		
			referenciaModulo.grillaDespuesSeleccionar(indiceFila);
			referenciaModulo.activarBotones();
		}

	});
};

moduloBaseAutorizacion.prototype.grillaDespuesSeleccionar= function(indice){
	//todo implementar
};

moduloBaseAutorizacion.prototype.inicializarFormularioPrincipal= function(){
  var referenciaModulo=this;
	//Establecer validaciones del formulario
	this.obj.frmPrincipal.validate({
        rules: referenciaModulo.reglasValidacionFormulario,
        messages: referenciaModulo.mensajesValidacionFormulario,
        submitHandler: function(form) {
           // form.submit();
        }
    });
};

moduloBaseAutorizacion.prototype.activarBotones=function(){
	this.obj.btnModificar.removeClass("disabled");
	this.obj.btnModificarEstado.removeClass("disabled");
	this.obj.btnVer.removeClass("disabled");
};

moduloBaseAutorizacion.prototype.desactivarBotones=function(){
	this.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);
	this.obj.btnModificar.addClass("disabled");
	this.obj.btnModificarEstado.addClass("disabled");
	this.obj.btnVer.addClass("disabled");
};

moduloBaseAutorizacion.prototype.listarRegistros = function(){
	this.obj.datClienteApi.ajax.reload();
	this.desactivarBotones(); 	
};

moduloBaseAutorizacion.prototype.actualizarBandaInformacion=function(tipo, mensaje){
	this.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_ERROR);
	this.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_EXITO);
	this.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_INFORMACION);
	if (tipo==constantes.TIPO_MENSAJE_INFO){
		this.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_INFORMACION );
	} else if (tipo==constantes.TIPO_MENSAJE_EXITO){
		this.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_EXITO );
	} else if (tipo==constantes.TIPO_MENSAJE_ERROR){
		this.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_ERROR );
	}	
	this.obj.bandaInformacion.text(mensaje);
};

moduloBaseAutorizacion.prototype.protegeFormulario= function(condicion){
	if (condicion) {
		this.obj.ocultaFormulario.show();
	} else {
		this.obj.ocultaFormulario.hide();
	}	
};

moduloBaseAutorizacion.prototype.recuperarRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
	referenciaModulo.protegeFormulario(true);
	$.ajax({
	    type: "GET",
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: "application/json", 
	    data: {ID:referenciaModulo.idRegistro},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} 	else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		if (referenciaModulo.modoEdicion == constantes.MODO_ACTUALIZAR){
	    			referenciaModulo.llenarFormulario(respuesta.contenido.carga[0]);
	    		} else if (referenciaModulo.modoEdicion == constantes.MODO_VER){
	    			referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    		}
	    		referenciaModulo.protegeFormulario(false);
    		}
	    },			    		    
	    error: function() {
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	    	referenciaModulo.protegeFormulario(false);
	    }
	});
};

moduloBaseAutorizacion.prototype.verRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
	referenciaModulo.protegeFormulario(true);
	$.ajax({
	    type: "GET",
	    url: referenciaModulo.URL_RECUPERAR, 
	    contentType: "application/json", 
	    data: {ID:referenciaModulo.idRegistro},	
	    success: function(respuesta) {
	    	if (!respuesta.estado) {
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	    	} 	else {		 
	    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	    		referenciaModulo.llenarDetalles(respuesta.contenido.carga[0]);
	    		referenciaModulo.protegeFormulario(false);
    		}
	    },			    		    
	    error: function() {
	    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
	    	referenciaModulo.protegeFormulario(false);
	    }
	});
};

moduloBaseAutorizacion.prototype.guardarRegistro= function(){
	var referenciaModulo = this;
	//Ocultar alertas de mensaje
	if (referenciaModulo.obj.frmPrincipal.valid()){
		//moduloBaseAutorizacion.showLoading();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		referenciaModulo.protegeFormulario(true);
		var eRegistro = referenciaModulo.recuperarValores();
		$.ajax({
		    type: "POST",
		    url: referenciaModulo.URL_GUARDAR, 
		    contentType: "application/json", 
		    data: JSON.stringify(eRegistro),	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    		referenciaModulo.protegeFormulario(false);
		    	} 	else {		    				    			    		
		    		
		    		referenciaModulo.iniciarListado();
	    		}
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    	referenciaModulo.protegeFormulario(false);
		    }
		});
	} else {
		console.log("No valido");
		//var containers=moduloBaseAutorizacion.obj.frmPrincipal.find('.control-group.control-element.error');
		//$(window).scrollTop($(containers[0]).offset().top-70);	
	}	
};

moduloBaseAutorizacion.prototype.iniciarListado= function(){
	var referenciaModulo = this;
	try{
		referenciaModulo.listarRegistros();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		referenciaModulo.obj.cntFormulario.hide();	
		referenciaModulo.protegeFormulario(false);
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
		console.log(error.message);
	};
};

moduloBaseAutorizacion.prototype.actualizarRegistro= function(){
	//Ocultar alertas de mensaje
	var referenciaModulo = this;
	if (referenciaModulo.obj.frmPrincipal.valid()){
		//moduloBaseAutorizacion.showLoading();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		referenciaModulo.protegeFormulario(true);
    var eRegistro = referenciaModulo.recuperarValores();
    console.log(eRegistro);
		$.ajax({
		    type: "POST",
		    url: referenciaModulo.URL_ACTUALIZAR, 
		    contentType: "application/json", 
		    data: JSON.stringify(eRegistro),	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
		    		referenciaModulo.protegeFormulario(false);
		    	} 	else {		    				    			    		
		    		referenciaModulo.listarRegistros();
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
		    		referenciaModulo.iniciarContenedores();
		    		/*referenciaModulo.obj.cntFormulario.hide();	
		    		referenciaModulo.protegeFormulario(false);
		    		referenciaModulo.obj.cntTabla.show();*/
	    		}
		    },			    		    
		    error: function() {
		    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
		    	referenciaModulo.protegeFormulario(false);
		    }
		});
	} else {
		//var containers=moduloBaseAutorizacion.obj.frmPrincipal.find('.control-group.control-element.error');
		//$(window).scrollTop($(containers[0]).offset().top-70);	
	}	
};

moduloBaseAutorizacion.prototype.iniciarContenedores = function(){
	var referenciaModulo = this;
	try{
		referenciaModulo.obj.cntFormulario.hide();	
		referenciaModulo.protegeFormulario(false);
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
    	console.log(error.message);
    };
};

moduloBaseAutorizacion.prototype.solicitarActualizarEstado= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		console.log(error.message);
	}
};

moduloBaseAutorizacion.prototype.actualizarEstadoRegistro= function(){
	var referenciaModulo = this;
	referenciaModulo.obj.frmConfirmarModificarEstado.modal("hide");
	if (referenciaModulo.obj.frmPrincipal.valid()){
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		referenciaModulo.protegeFormulario(true);
		if (referenciaModulo.estadoRegistro==constantes.ESTADO_ACTIVO){
			referenciaModulo.estadoRegistro=constantes.ESTADO_INACTIVO;
		} else {
			referenciaModulo.estadoRegistro=constantes.ESTADO_ACTIVO;
		}

		var eRegistro = {};
	    var referenciaModulo=this;
	    try {
		    eRegistro.id = parseInt(referenciaModulo.idRegistro);
		    eRegistro.estado = parseInt(referenciaModulo.estadoRegistro);
	    }  catch(error){
	    	console.log(error.message);
	    }

		$.ajax({
		    type: "POST",
		    url: referenciaModulo.URL_ACTUALIZAR_ESTADO, 
		    contentType: "application/json", 
		    data: JSON.stringify(eRegistro),	
		    success: function(respuesta) {
		    	if (!respuesta.estado) {
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
			    		referenciaModulo.protegeFormulario(false);
			    	} 	else {		    				    			    		
			    		referenciaModulo.listarRegistros();
			    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
			    		referenciaModulo.obj.cntFormulario.hide();	
			    		referenciaModulo.protegeFormulario(false);
			    		referenciaModulo.obj.cntTabla.show();
		    		}
			    },			    		    
			    error: function() {
			    	referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
			    	referenciaModulo.protegeFormulario(false);
			    }
			});
		}
		
	};
	
moduloBaseAutorizacion.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloBaseAutorizacion.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloBaseAutorizacion.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloBaseAutorizacion.prototype.recuperarValores = function(registro){
		var eRegistro = {};
//Implementar en cada caso
    return eRegistro;
};