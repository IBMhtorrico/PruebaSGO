var constantes={	
	TIPO_MENSAJE_INFO:0,
	TIPO_MENSAJE_EXITO:1,
	TIPO_MENSAJE_ERROR:3,
	CLASE_MENSAJE_ERROR:"callout-danger",
	CLASE_MENSAJE_EXITO:"callout-success",
	CLASE_MENSAJE_INFORMACION:"callout-info",
	MODO_NUEVO:1,
	MODO_ACTUALIZAR:2,
	MODO_VER:3,
	MODO_LISTAR:4,	
	MODO_AUTORIZAR:5,
	
	//Agregado por req 9000002570====
	MODO_ETAPAS:6,
	//===============================
	
	MODO_LISTAR_TRANSPORTE:1,
	MODO_DETALLE_TRANSPORTE:2,
	MODO_FORMULARIO_TRANSPORTE_NUEVO:3,
	MODO_FORMULARIO_TRANSPORTE_MODIFICAR:4,
	MODO_IMPORTAR_TRANSPORTE:5,
	MODO_VER_TRANSPORTE:6,
	MODO_EVENTO_TRANSPORTE:7,
	MODO_PESAJE_TRANSPORTE:8,
	
	TITULO_AGREGAR_REGISTRO:"Agregar Nuevo Registro",
	TITULO_MODIFICA_REGISTRO:"Modificar Registro",
	TITULO_DETALLE_REGISTRO:"Detalle del Registro",
	TITULO_ACTIVAR_REGISTRO:"Activar",
	TITULO_DESACTIVAR_REGISTRO:"Desactivar",
	
	TITULO_ETAPAS_REGISTRO:"Etapas por Proceso de Abastecimiento",
	TITULO_TIEMPOS_ETAPAS_REGISTRO:"Tiempos por Etapa",
	
	ESTADO_ACTIVO:"1",
	ESTADO_INACTIVO:"0",

	ESTADO:{"1":"Activo","0":"Inactivo"},
	TIPO_PROPIETARIO:{"1":"Propietario","2":"Contratista"},
	TIPO_USUARIO:{"1":"Interno","2":"Externo"},
	TIPO_ESTACION:{"1":"Estandar","2":"Reparto en campo", "3": "Tuberia"},
	TIPO_CONTOMETRO:{"1":"Tipo 1", "2": "Tipo 2"},
	ESTADOS_DIA_OPERATIVO:{"1":"Planificado","2":"Asignado", "3": "Descargando", "4": "Cerrado", "5": "Liquidado"},
	ORIGEN_TRANSPORTE:{"M":"Manual","A":"Automatico"},
	TIPO_EVENTO:{"1":"Incidencia","2":"Accidente","3":"Falla Operacional"},
	
	PLANTILLA_OPCION_SELECTBOX:'<option value="XID_OPCIONX">XVALOR_OPCIONX</option>',
	ID_OPCION_CONTENEDOR:"XID_OPCIONX",
	VALOR_OPCION_CONTENEDOR:"XVALOR_OPCIONX",
	FORMATO_FECHA_HORA:'dd/mm/yyyy HH:mm:ss'
};

var utilitario={	
	//str: Cadena con formato constantes.FORMATO_FECHA
	formatearStringToDate: function(str){
		var parametros = str.split('/');
		var fecha =  new Date(parametros[2],'' + (parseInt(parametros[1]) - 1),parametros[0],0,0,0,0);
	    return fecha;
	},
	//str: fecha en formato dd/mm/yyyy hh:mm:ss
	formatearStringToDateHour: function(str){
		var parametros = str.split(' ');
		var parametrosDia = parametros[0].split('/');
		var parametrosHora = parametros[1].split(':');
		var fecha =  new Date(parametrosDia[2],'' + (parseInt(parametrosDia[1]) - 1), parametrosDia[0], parametrosHora[0], parametrosHora[1], parametrosHora[2], 0);
	    return fecha;
	},
	
	//Agregado por req 9000002570============================================
	validarFormatoFechaHora: function(str){
		var parametros = str.split(' ');

		if(parametros.length != 2){
		    return false;
		}else{
		    var parametrosDia = parametros[0].split('/');
		    if(parametrosDia.length != 3){
		    	return false;
		    }else{
		        var dia = parametrosDia[0];
		        var mes = parametrosDia[1];
		        var anio = parametrosDia[2];
		        if(isNaN(dia) || isNaN(mes) || isNaN(anio) 
		                    || dia < 1 || dia > 31 || mes < 1 || mes > 12 
		                    || dia.length != 2 || mes.length != 2 || anio.length != 4){
		        	return false;
		        }else{
		            var parametrosHora = parametros[1].split(':');
		            if(parametrosHora.length != 2){
		            	return false;
		            }else{
		                var hora = parametrosHora[0];
		                var min = parametrosHora[1];
		                if(isNaN(hora) || isNaN(min) || hora.length != 2 || min.length != 2
		                    || hora < 0 || hora > 24 || min < 0 || min > 59){
		                	return false;
		                }else{
		                	return true;
		                }
		            }
		        }
		    }
		}
	},
	//=======================================================================
	formatearTimestampToString: function(fecha) {
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
		var h   = "0" + date.getHours();
		var m  	= "0" + date.getMinutes();
		var s 	= "0" + date.getSeconds();  
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy +" " + h.substr(-2) + ":" + m.substr(-2) + ":" + s.substr(-2) ;
	},
	formatearTimestampToStringSinSeg: function(fecha) {
		var date = new Date(fecha);
		var yy 	= date.getFullYear().toString();
	    var mm 	= (date.getMonth()+1).toString(); // getMonth() is zero-based
	    var dd  = date.getDate().toString();
		var h   = "0" + date.getHours();
		var m  	= "0" + date.getMinutes();
	    return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yy +" " + h.substr(-2) + ":" + m.substr(-2) ;
	},
	formatearFecha2Cadena: function(fecha) {
		   var yyyy = fecha.getFullYear().toString();
		   var mm = (fecha.getMonth()+1).toString(); // getMonth() is zero-based
		   var dd  = fecha.getDate().toString();
		   //return yyyy +(mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]); 
		   return  (dd[1]?dd:"0"+dd[0])+"/"+(mm[1]?mm:"0"+mm[0]) +"/"+yyyy ;
	},
	//str: fecha en formato yyyy-mm-dd
	//retorna fecha en formato dd/mm/yyyy
	formatearFecha: function(str){
		var parametros = str.split('-');
		var fecha =  new String(parametros[2]+ '/' + parametros[1] + '/' + parametros[0]);
	    return fecha;
	},
	formatearEstado: function(datos, tipo, fila, meta ){
		var valorFormateado="";
		valorFormateado = constantes.ESTADO[datos];
	    return valorFormateado;
	},
	formatearTipoPropietario: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_PROPIETARIO[datos];
	    return valorFormateado;
	},
	formatearTipoEstacion: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_ESTACION[datos];
	    return valorFormateado;
	},
	formatearTipoContometro: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_CONTOMETRO[datos];
	    return valorFormateado;
	},
	formatearEstadoDiaOperativo: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ESTADOS_DIA_OPERATIVO[datos];
	    return valorFormateado;
	},
	formatearOrigenTransporte: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.ORIGEN_TRANSPORTE[datos];
	    return valorFormateado;
	},
	//Se ingresa el id del estado y retorna el texto del estado.
	formatearValorEstado: function(idEstado){
		var valorFormateado="";
		valorFormateado = constantes.ESTADO[idEstado];
	    return valorFormateado;
	},
	formatearTipoUsuario: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_USUARIO[datos];
	    return valorFormateado;
	},
	formatearTipoEvento: function(datos, tipo, fila, meta ){
		var valorFormateado=constantes.TIPO_EVENTO[datos];
	    return valorFormateado;
	},
	retornarRangoSemana : function(fechaActual) {
	    //fechaActual formato yyyy-mm-dd
	    var fechaInicial = new Date(fechaActual);
	    var fechaFinal = new Date(fechaActual);
	    var numeroDiasRestar = 0,numeroDiasAgregar = 0;
	    var diaSemana = fechaInicial.getDay();  
	    var DIA_SEMANA={DOMINGO:0,LUNES:1,MARTES:2,MIERCOLES:3,JUEVES:4,VIERNES:5,SABADO:6}
	    var rangoSemana={};
	    if(diaSemana == DIA_SEMANA.DOMINGO){
	      numeroDiasRestar = 6;
	      diaSemanaASumar = 0;
	    } else if (diaSemana == DIA_SEMANA.LUNES){
	      numeroDiasRestar = 0;
	      numeroDiasAgregar = 6;
	    } else {
	      numeroDiasRestar = diaSemana - 1;
	      numeroDiasAgregar = 7 - diaSemana;
	    }
      fechaInicial.setDate(fechaInicial.getDate() - parseInt(numeroDiasRestar));
	    fechaFinal.setDate(fechaFinal.getDate() + parseInt(numeroDiasAgregar));	    
	    rangoSemana = {"fechaInicial":fechaInicial,"fechaFinal":fechaFinal};
	    return rangoSemana;
	} 
};

function moduloSGO(){
	this.obj={};
	this.modoEdicion=constantes.MODO_LISTAR;
	//Inicializar propiedades
	this.urlBase='';
	this.urlDataTableLocalization='../themes/default/plugins/datatables/lang/es.js';
  this.NUMERO_REGISTROS_PAGINA=15;
  this.TOPES_PAGINACION=[[15, 30, 50, -1], [15, 30, 50, "All"]];
  this.URL_LENGUAJE_GRILLA="tema/datatable/language/es-ES.json";
	this.idRegistro = 0;
  this.columnasGrilla={};
  this.definicionColumnas=[];
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};
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

moduloSGO.prototype.inicializar=function(){
  this.inicializarControlesGenericos();
  this.inicializarGrilla();
  this.inicializarFormularioPrincipal();
  this.inicializarCampos();
};


moduloSGO.prototype.resetearFormulario= function(){
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

moduloSGO.prototype.inicializarControlesGenericos=function(){
  var referenciaModulo=this;
 
	this.obj.tablaPrincipal=$('#tablaPrincipal');
	this.obj.frmPrincipal = $("#frmPrincipal");
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

moduloSGO.prototype.iniciarGuardar = function(){
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

moduloSGO.prototype.iniciarAgregar= function(){
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

moduloSGO.prototype.iniciarModificar= function(){
	var referenciaModulo=this;
	referenciaModulo.modoEdicion=constantes.MODO_ACTUALIZAR;
	referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_MODIFICA_REGISTRO);
	referenciaModulo.obj.cntTabla.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntFormulario.show();
	referenciaModulo.recuperarRegistro();
};

moduloSGO.prototype.iniciarVer= function(){
	var referenciaModulo=this;
	referenciaModulo.modoEdicion=constantes.MODO_VER;
	referenciaModulo.obj.cmpTituloFormulario.text(constantes.TITULO_DETALLE_REGISTRO);
	referenciaModulo.obj.cntTabla.hide();
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.show();
	referenciaModulo.recuperarRegistro();
};

moduloSGO.prototype.iniciarCancelar=function(){
	var referenciaModulo=this;
	referenciaModulo.resetearFormulario();
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntTabla.show();
};

moduloSGO.prototype.iniciarCerrarVista=function(){
	var referenciaModulo=this;
	referenciaModulo.obj.cntFormulario.hide();
	referenciaModulo.obj.cntVistaRegistro.hide();
	referenciaModulo.obj.cntTabla.show();
};

moduloSGO.prototype.inicializarGrilla=function(){
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
		"dom": '<"row" <"col-sm-12" t> ><"row" <"col-sm-3"l> <"col-sm-4"i> <"col-sm-5"p>>',
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

moduloSGO.prototype.grillaDespuesSeleccionar= function(indice){
	//todo implementar
};

moduloSGO.prototype.inicializarFormularioPrincipal= function(){
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

moduloSGO.prototype.activarBotones=function(){
	this.obj.btnModificar.removeClass("disabled");
	this.obj.btnModificarEstado.removeClass("disabled");
	this.obj.btnVer.removeClass("disabled");
};

moduloSGO.prototype.desactivarBotones=function(){
	this.obj.btnModificarEstado.html('<i class="fa fa-cloud-upload"></i>'+constantes.TITULO_ACTIVAR_REGISTRO);
	this.obj.btnModificar.addClass("disabled");
	this.obj.btnModificarEstado.addClass("disabled");
	this.obj.btnVer.addClass("disabled");
};

moduloSGO.prototype.listarRegistros = function(){
	var referenciaModulo = this;
	this.obj.datClienteApi.ajax.reload();
	referenciaModulo.desactivarBotones(); 	
};

moduloSGO.prototype.actualizarBandaInformacion=function(tipo, mensaje){
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

moduloSGO.prototype.protegeFormulario= function(condicion){
	if (condicion) {
		this.obj.ocultaFormulario.show();
	} else {
		this.obj.ocultaFormulario.hide();
	}	
};

moduloSGO.prototype.recuperarRegistro= function(){
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

moduloSGO.prototype.verRegistro= function(){
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

moduloSGO.prototype.guardarRegistro= function(){
	var referenciaModulo = this;
	//Ocultar alertas de mensaje
	if (referenciaModulo.obj.frmPrincipal.valid()){
		//moduloSGO.showLoading();
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
		    		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, respuesta.mensaje);
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
		//var containers=moduloSGO.obj.frmPrincipal.find('.control-group.control-element.error');
		//$(window).scrollTop($(containers[0]).offset().top-70);	
	}	
};

moduloSGO.prototype.iniciarListado= function(){
	var referenciaModulo = this;
	try{
		referenciaModulo.listarRegistros();
		referenciaModulo.obj.cntFormulario.hide();
		referenciaModulo.protegeFormulario(false);
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
		console.log(error.message);
	};
};

moduloSGO.prototype.actualizarRegistro= function(){
	//Ocultar alertas de mensaje
	var referenciaModulo = this;
	if (referenciaModulo.obj.frmPrincipal.valid()){
		//moduloSGO.showLoading();
		referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		referenciaModulo.protegeFormulario(true);
    var eRegistro = referenciaModulo.recuperarValores();
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
		//var containers=moduloSGO.obj.frmPrincipal.find('.control-group.control-element.error');
		//$(window).scrollTop($(containers[0]).offset().top-70);	
	}	
};

moduloSGO.prototype.iniciarContenedores = function(){
	var referenciaModulo = this;
	try{
		referenciaModulo.obj.cntFormulario.hide();	
		referenciaModulo.protegeFormulario(false);
		referenciaModulo.obj.cntTabla.show();
	} catch(error){
    	console.log(error.message);
    };
};

moduloSGO.prototype.solicitarActualizarEstado= function(){
	try {
		this.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,"Procesando petición...");
		this.obj.frmConfirmarModificarEstado.modal("show");
	} catch(error){
		console.log(error.message);
	}
};

moduloSGO.prototype.actualizarEstadoRegistro= function(){
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
	
moduloSGO.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloSGO.prototype.llenarFormulario = function(registro){
//Implementar en cada caso	
};

moduloSGO.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloSGO.prototype.recuperarValores = function(registro){
		var eRegistro = {};
//Implementar en cada caso
    return eRegistro;
};