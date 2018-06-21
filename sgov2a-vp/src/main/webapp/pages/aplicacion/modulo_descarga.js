function moduloDescarga(){
  this.obj = {};  
  this.SEPARADOR_MILES = ",";
  this.NUMERO_REGISTROS_PAGINA = constantes.NUMERO_REGISTROS_PAGINA;
  this.TOPES_PAGINACION = constantes.TOPES_PAGINACION;
  this.TOPES_PAGINACION_CARGA_TANQUE = [[3, 6], [3, 6]];
  this.URL_LENGUAJE_GRILLA="tema/datatable/language/es-ES.json";
  this.TIPO_CONTENIDO=constantes.TIPO_CONTENIDO_JSON;
  this.modoEdicion=constantes.MODO_LISTAR;
  this.NOMBRE_EVENTO_CLICK = constantes.NOMBRE_EVENTO_CLICK;
  this.depuracionActivada=true;
  this.estaCargadaInterface=false;
  this.URL_BASE='descarga';
  this.TIPO_DATO_NO_DEFINIDO='undefined';
  this.ATRIBUTO_FILA_SELECCIONADA="data-selected";
  this.CLASE_FILA_SELECCIONADA="selected";
  this.FILA_SELECCIONADA=1;
  this.FILA_NO_SELECCIONADA=0;
  this.URL_LISTAR_DIA_OPERATIVO="../admin/dia_operativo/listarxdescarga";
  this.URL_FILTRAR_TRANSPORTES_ASIGNADOS="../admin/transporte/listar-asignados";
  this.URL_RECUPERAR_TRANSPORTE="../admin/transporte/recuperar-transporte";
  this.URL_RECUPERAR_AFORO_CISTERNA =  '../admin/aforo-cisterna/listar';
  this.URL_RECUPERAR_AFORO_TANQUE =  '../admin/aforo-tanque/listar';
  this.URL_RECUPERAR_RECUPERAR_VOLUMEN_CORREGIDO = "../admin/formula/recuperar-volumen-corregido";
  this.URL_RECUPERAR_RECUPERAR_TOLERANCIA = "../admin/formula/recuperar-tolerancia";
  this.URL_LISTAR_CARGA = this.URL_BASE + '/listar-carga';  
  this.URL_RECUPERAR_CARGA = this.URL_BASE + '/recuperar-carga';
  this.URL_GUARDAR_EVENTO ='../admin/evento/crear';
  this.URL_GUARDAR_CARGA = this.URL_BASE + '/crear-carga';
  this.URL_ACTUALIZAR_CARGA = this.URL_BASE + '/actualizar-carga';  
  this.URL_LISTAR_DESCARGA = this.URL_BASE + '/listar';
  this.URL_GUARDAR_DESCARGA = this.URL_BASE + '/crear';
  this.URL_ACTUALIZAR_DESCARGA = this.URL_BASE + '/actualizar';
  this.URL_RECUPERAR_DESCARGA = "../admin/descarga/recuperar-descarga";
  this.URL_RECUPERAR_FACTOR_MASA ="../admin/formula/recuperar-factor-masa";
  this.URL_RECUPERAR_APROBADOR="../admin/autorizacion/listar-aprobador";
  this.URL_VALIDAR_AUTORIZACION ="../admin/autorizacion/validar";  
  this.NUMERO_DECIMALES=2;
  this.ATRIBUTO_LECTURA="readonly";
  this.CodigoInternoAutorizacion="IVD";
  //Inicializar propiedades
  this.mensajeEsMostrado=false;
  this.SECCION_PRINCIPAL=1;
  this.SECCION_DETALLE_CARGA=2;
  this.SECCION_DETALLE_DESCARGA=2;
  this.INDICE_COLUMNA_ID_DIA_OPERATIVO=1;
  this.INDICE_COLUMNA_FECHA_DIA_OPERATIVO=2;
  this.INDICE_COLUMNA_ESTADO_DIA_OPERATIVO=7;
  this.GRILLA_DIA_OPERATIVO=1;
  this.GRILLA_CARGA_TANQUE=2;
  this.GRILLA_DESCARGA_CISTERNA=3;  
  this.urlDataTableLocalization='../themes/default/plugins/datatables/lang/es.js';
  this.idRegistro = 0;
  this.idAutorizacionEjecutada=0;
  this.factorMasaRecuperado=0;
  this.toleranciaRecuperada =0;
  this.idDiaOperativoSeleccionado=0;
  this.idOperacionSeleccionada=0;
  this.nombreOperacionSeleccionada="";
  this.idEstacionSeleccionada=0;
  this.fechaDiaOperativoSeleccionado="";
  this.apiTemperaturaBaseDespacho=0;
  this.idCargaTanque=0;
  this.idDescargaCisterna=0;
  this.columnasGrilla={};
  this.modoEdicionCargaTanque=null;
  this.modoEdicionDescarga=null;
  this.volumenTotalDespachadoCorregido=0;
  this.definicionColumnas=[];
  this.descargaSeleccionada=null;
  this.reglasValidacionFormulario={};
  this.mensajesValidacionFormulario={};
  this.ordenGrillaDiaOperativo=[[ 2, 'asc' ]];
  this.columnasGrillaDiaOperativo=[{ "data": null} ];
  this.idTipoTanqueSeleccionado=0;
  this.idTanqueSeleccionado=0;
  this.definicionColumnasGrillaDiaOperativo = [{
    "targets": 0,
    "searchable": false,
    "orderable": false,
    "visible": false,
    "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      return configuracion._iDisplayStart + meta.row + 1;
    }
  }];
  
  this.ordenGrillaCargaTanque=[[ 1, 'asc' ]];
  this.columnasGrillaCargaTanque=[{ "data": null} ];
  this.definicionColumnasGrillaCargaTanque = [{
    "targets": 0,
    "searchable": false,
    "orderable": false,
    "visible": false,
    "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      return configuracion._iDisplayStart + meta.row + 1;
    }
  }];
  
  this.ordenGrillaDescarga=[[ 1, 'asc' ]];
  this.columnasGrillaDescarga=[{ "data": null} ];
  this.definicionGrillaDescarga = [{
    "targets": 0,
    "searchable": false,
    "orderable": false,
    "visible": false,
    "render": function ( datos, tipo, fila, meta ) {
      var configuracion = meta.settings;
      return configuracion._iDisplayStart + meta.row + 1;
    }
  }];
}

moduloDescarga.prototype.mostrarDepuracion=function(mensaje,titulo){
  var ref=this;
  if (ref.depuracionActivada === true){
    if ((typeof titulo != "undefined") && (titulo != null)) {
      console.log(titulo);
    }
    console.log(mensaje);
  }
};

moduloDescarga.prototype.actualizarBandaInformacion=function(tipo, mensaje){
  var ref=this;
  ref.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_ERROR);
  ref.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_EXITO);
  ref.obj.bandaInformacion.removeClass(constantes.CLASE_MENSAJE_INFORMACION);
  if (tipo==constantes.TIPO_MENSAJE_INFO){
    ref.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_INFORMACION );
  } else if (tipo==constantes.TIPO_MENSAJE_EXITO){
    ref.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_EXITO );
  } else if (tipo==constantes.TIPO_MENSAJE_ERROR){
    ref.obj.bandaInformacion.addClass(constantes.CLASE_MENSAJE_ERROR );
  } 
  ref.obj.bandaInformacion.text(mensaje);
};

moduloDescarga.prototype.mostrarErrorServidor=function(xhr,estado,error){
  var ref=this;
  if (xhr.status === constantes.ERROR_SIN_CONEXION) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_NO_CONECTADO);
  } else if (xhr.status === constantes.ERROR_RECURSO_NO_DISPONIBLE) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_RECURSO_NO_DISPONIBLE);
  } else if (xhr.status === constantes.ERROR_INTERNO_SERVIDOR) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_INTERNO_SERVIDOR);
  } else if (estado === constantes.ERROR_INTERPRETACION_DATOS) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_GENERICO_SERVIDOR);
  } else if (estado === constantes.ERROR_TIEMPO_AGOTADO) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_TIEMPO_AGOTADO);
  } else if (estado === constantes.ERROR_CONEXION_ABORTADA) {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_GENERICO_SERVIDOR);
  } else {
  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.ERROR_GENERICO_SERVIDOR);
  }
};


moduloDescarga.prototype.inicializarControles=function(){
  var ref=this;
  //valida permisos de botones
  this.descripcionPermiso=$("#descripcionPermiso");
  //Controles  
  this.obj.tituloSeccion = $('#tituloSeccion');
  this.listaElementosTransporte={};
  this.listaElementosDescarga={};
  this.plantillaDetalleTransporte= $("#plantillaDetalleTransporte");
  this.plantillaDetalleDescarga= $("#plantillaDetalleDescarga");  
  this.plantillaVistaDetalleTransporte= $("#plantillaVistaDetalleTransporte");
  this.plantillaVistaDetalleDescarga= $("#plantillaVistaDetalleDescarga");
  this.plantillaEventoDescarga= $("#plantillaEventoDescarga");
  this.obj.ocultaSeccionCabeceraDescarga=$("#ocultaSeccionCabeceraDescarga");
  $("#tablaCompartimentoDespacho > tbody ").children().remove();
  $("#tablaCompartimentoDescarga > tbody ").children().remove();
  this.obj.cmpVolTotObsRecepcion=$("#cmpVolTotObsRecepcion");
  this.obj.cmpVolTotCorRecepcion=$("#cmpVolTotCorRecepcion");
  //Seccion Dia Operativo
  this.obj.cntTablaDiaOperativo = $('#cntTablaDiaOperativo');
  this.obj.filtroOperacion = $("#filtroOperacion");
  this.obj.filtroFechaPlanificada = $("#filtroFechaPlanificada");  
  this.obj.tablaDiaOperativo=$("#tablaDiaOperativo");
  this.obj.ocultaContenedorTablaDiaOperativo=$("#ocultaContenedorTablaDiaOperativo");
  this.obj.btnDetalleDiaOperativo=$("#btnDetalleDiaOperativo");
  this.obj.btnFiltrarDiaOperativo=$("#btnFiltrarDiaOperativo");
  //Detalle dia Operativo
  this.obj.cntDetalleDiaOperativo=$("#cntDetalleDiaOperativo");
  this.obj.cmpCliente=$("#cmpCliente");
  this.obj.cmpOperacionDetalleDiaOperativo=$("#cmpOperacionDetalleDiaOperativo");
  this.obj.cmpFechaPlanificacionDetalleDiaOperativo=$("#cmpFechaPlanificacionDetalleDiaOperativo");
  this.obj.filtroEstacionDetalleDiaOperativo=$("#filtroEstacionDetalleDiaOperativo");
  this.obj.btnAgregarCarga=$("#btnAgregarCarga");
  this.obj.btnModificarCarga=$("#btnModificarCarga");
  this.obj.btnFiltrarCargaTanque=$("#btnFiltrarCargaTanque");
  this.obj.tablaCargaTanque=$("#tablaCargaTanque");
  this.obj.ocultaContenedorTablaCargaTanque=$("#ocultaContenedorTablaCargaTanque");
  this.obj.ocultaContenedorTablaDescarga=$("#ocultaContenedorTablaDescarga");
  this.obj.btnRegresarPrincipal=$("#btnRegresarPrincipal"); 
  //botones
  this.obj.btnAgregarDescarga=$("#btnAgregarDescarga");
  this.obj.btnModificarDescarga=$("#btnModificarDescarga");
  this.obj.btnVerDescarga=$("#btnVerDescarga");
  this.obj.btnAgregarEvento=$("#btnAgregarEvento");
  this.obj.tablaDescargaCisterna=$("#tablaDescargaCisterna");
  //Seccion Agregar Carga Tanque
  this.obj.cntFormularioCargaTanque=$("#cntFormularioCargaTanque");
  this.obj.frmAgregarCargaTanque=$("#frmAgregarCargaTanque");
  this.obj.frmAgregarDescarga=$("#frmAgregarDescarga");
  this.obj.cmpMedidaInicial=$("#cmpMedidaInicial");
  this.obj.cmpFechaHoraInicial=$("#cmpFechaHoraInicial");
  this.obj.cmpFechaHoraInicial.inputmask("d/m/y h:s:s");  
  this.obj.cmpAlturaInicial=$("#cmpAlturaInicial");
  this.obj.cmpAlturaInicial.inputmask("integer");
  this.obj.cmpAlturaInicial.on("change",function(){ 
      ref.recuperarVolumenTanque("Inicial");
  });
  
  this.obj.cmpTemperaturaInicialCentro=$("#cmpTemperaturaInicialCentro");
  //this.obj.cmpTemperaturaInicialCentro.inputmask("decimal", {digits:2});
  this.obj.cmpTemperaturaInicialCentro.inputmask("99.9");
  this.obj.cmpTemperaturaInicialCentro.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoInicial").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaInicialProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaInicialCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenInicialObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    ref.calculaFactorCorreccionTanque(parametros,"Inicial");
  });  
  
  this.obj.cmpTemperaturaInicialProbeta=$("#cmpTemperaturaInicialProbeta");
  //this.obj.cmpTemperaturaInicialProbeta.inputmask("decimal", {digits:2});
  this.obj.cmpTemperaturaInicialProbeta.inputmask("99.9");
  this.obj.cmpTemperaturaInicialProbeta.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoInicial").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaInicialProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaInicialCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenInicialObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    ref.calculaFactorCorreccionTanque(parametros,"Inicial");
  });  
    
  this.obj.cmpAPIObservadoInicial=$("#cmpAPIObservadoInicial");
  this.obj.cmpAPIObservadoInicial.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoInicial").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaInicialProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaInicialCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenInicialObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    //parametros.filtroEstacion=idEstacion;
	    //parametros.filtroProducto=idProducto;	  
	    ref.calculaFactorCorreccionTanque(parametros,"Inicial");
  });
  
  
  //this.obj.cmpAPIObservadoInicial.inputmask("decimal", {digits:2});
  this.obj.cmpAPIObservadoInicial.inputmask("99.9");
  this.obj.cmpFactorInicial=$("#cmpFactorInicial");
  //this.obj.cmpFactorInicial.inputmask("1.999999");
  this.obj.cmpVolumenInicialObservado=$("#cmpVolumenInicialObservado");
  //this.obj.cmpVolumenInicialObservado.inputmask("decimal", {digits:2});
  this.obj.cmpVolumenInicialObservado.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
  this.obj.cmpVolumenInicialObservado.on("change",function(){ 
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoInicial").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaInicialProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaInicialCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.volumenObservado=$("#cmpVolumenInicialObservado").val().replaceAll(ref.SEPARADOR_MILES,""); 
	    ref.calculaFactorCorreccionTanque(parametros,"Inicial");
    });
  
  this.obj.cmpVolumenInicialCorregido=$("#cmpVolumenInicialCorregido");
  //this.obj.cmpVolumenInicialCorregido.inputmask("decimal", {digits:2});
  this.obj.cmpVolumenInicialCorregido.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
  this.obj.cmpMedidaFinal=$("#cmpMedidaFinal");
  this.obj.cmpFechaHoraFinal=$("#cmpFechaHoraFinal");
  this.obj.cmpFechaHoraFinal.inputmask("d/m/y h:s:s");  
  this.obj.cmpAlturaFinal=$("#cmpAlturaFinal");
  this.obj.cmpAlturaFinal.inputmask("integer");
  this.obj.cmpAlturaFinal.on("change",function(){ 
      ref.recuperarVolumenTanque("Final");
  });
  
  this.obj.cmpTemperaturaFinalCentro=$("#cmpTemperaturaFinalCentro");
  //this.obj.cmpTemperaturaFinalCentro.inputmask("decimal", {digits:2 });
  this.obj.cmpTemperaturaFinalCentro.inputmask("99.9");
  this.obj.cmpTemperaturaFinalCentro.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoFinal").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaFinalProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaFinalCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenFinalObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    ref.calculaFactorCorreccionTanque(parametros,"Final");
  });
  
  
  this.obj.cmpTemperaturaFinalProbeta=$("#cmpTemperaturaFinalProbeta");
  //this.obj.cmpTemperaturaFinalProbeta.inputmask("decimal", {digits:2});
  this.obj.cmpTemperaturaFinalProbeta.inputmask("99.9");
  this.obj.cmpTemperaturaFinalProbeta.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoFinal").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaFinalProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaFinalCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenFinalObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    ref.calculaFactorCorreccionTanque(parametros,"Final");
  });
    
  this.obj.cmpAPIObservadoFinal=$("#cmpAPIObservadoFinal");
  //this.obj.cmpAPIObservadoFinal.inputmask("decimal", {digits:2});
  this.obj.cmpAPIObservadoFinal.inputmask("99.9");
  this.obj.cmpAPIObservadoFinal.on("change",function(){	  
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoFinal").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaFinalProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaFinalCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
    	parametros.volumenObservado=$("#cmpVolumenFinalObservado").val().replaceAll(ref.SEPARADOR_MILES,"");
	    ref.calculaFactorCorreccionTanque(parametros,"Final");
});
  
  
  this.obj.cmpFactorFinal=$("#cmpFactorFinal");
  //this.obj.cmpFactorFinal.inputmask("1.999999");
  this.obj.cmpVolumenFinalObservado=$("#cmpVolumenFinalObservado");
  //this.obj.cmpVolumenFinalObservado.inputmask("decimal", {digits:2});
  this.obj.cmpVolumenFinalObservado.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
  this.obj.cmpVolumenFinalObservado.on("change",function(){ 
	    var parametros={};
	    parametros.apiObservado=$("#cmpAPIObservadoFinal").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaProbeta=$("#cmpTemperaturaFinalProbeta").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.temperaturaCentro=$("#cmpTemperaturaFinalCentro").val().replaceAll(ref.SEPARADOR_MILES,"");
	    parametros.volumenObservado=$("#cmpVolumenFinalObservado").val().replaceAll(ref.SEPARADOR_MILES,""); 
	    ref.calculaFactorCorreccionTanque(parametros,"Final");
  });  
  
  this.obj.cmpVolumenFinalCorregido=$("#cmpVolumenFinalCorregido");
  //this.obj.cmpVolumenFinalCorregido.inputmask("decimal", {digits:2});
  this.obj.cmpVolumenFinalCorregido.inputmask('decimal', {digits: 2, groupSeparator:',',autoGroup:true,groupSize:3});
  this.obj.btnGuardarCarga=$("#btnGuardarCarga");
  this.obj.btnMostrarObservaciones=$("#btnMostrarObservaciones");
  this.obj.btnModificarCarga=$("#btnModificarCarga");
  this.obj.frmAgregarCargaTanque=$("#frmAgregarCargaTanque");  
  this.obj.btnCancelarGuardarCarga=$("#btnCancelarGuardarCarga");
  this.obj.ocultaContenedorFormularioCargaTanque=$("#ocultaContenedorFormularioCargaTanque");  
  this.obj.cmpOperacionFormularioCargaTanque=$("#cmpOperacionFormularioCargaTanque");
  this.obj.cmpTanqueFormularioCargaTanque=$("#cmpTanqueFormularioCargaTanque");
  this.obj.cmpTanqueFormularioCargaTanque.on("change",function(){
	var idTanque = parseInt($("#cmpTanqueFormularioCargaTanque").val());
	ref.pintarProductoTanque(idTanque);
  });
  this.obj.cmpProductoFormularioCargaTanque=$("#cmpProductoFormularioCargaTanque");
  this.obj.cmpEstacionFormularioCargaTanque=$("#cmpEstacionFormularioCargaTanque");
  //Controles de la Seccion Descarga 
  this.obj.cntFormularioDescarga=$("#cntFormularioDescarga");
  this.obj.cmpOperacionFormularioDescarga=$("#cmpOperacionFormularioDescarga");
  this.obj.cmpEstacionFormularioDescarga=$("#cmpEstacionFormularioDescarga");
  this.obj.cmpTanqueFormularioDescarga=$("#cmpTanqueFormularioDescarga");
  this.obj.cmpIdProductoTanqueFormularioDescarga=$("#cmpIdProductoTanqueFormularioDescarga");
  this.obj.cmpProductoTanqueFormularioDescarga=$("#cmpProductoTanqueFormularioDescarga");
  this.obj.cmpMetodoFormularioDescarga=$("#cmpMetodoFormularioDescarga");
  this.obj.cmpBuscarCisternaFormularioDescarga=$("#cmpBuscarCisternaFormularioDescarga");
  this.obj.ocultaFormularioDescarga = $("#ocultaFormularioDescarga");
  this.obj.etiquetaVolDespachadoTotal = $("#etiquetaVolDespachadoTotal");
  this.obj.etiquetaVolRecibidoTotal = $("#etiquetaVolRecibidoTotal");
  //Seccion pie de descarga
  this.obj.cntVistaFormularioDescarga = $("#cntVistaFormularioDescarga");
  this.obj.cmpVolumenDespachadoCorregido=$("#cmpVolumenDespachadoCorregido");
  this.obj.cmpVolumenRecibidoCorregido=$("#cmpVolumenRecibidoCorregido");
  this.obj.cmpVariacionFinal=$("#cmpVariacionFinal");
  this.obj.cmpMermaPermisible=$("#cmpMermaPermisible");
  this.obj.cmpVolumenExcedenteObservado=$("#cmpVolumenExcedenteObservado");
  this.obj.cmpVolumenExcedenteCorregido=$("#cmpVolumenExcedenteCorregido");
  this.obj.cntDetalleTransporte=$("#cntDetalleTransporte");
  this.obj.cntDetalleDescarga=$("#cntDetalleDescarga");
  this.obj.cntDescargaComparacion=$("#cntDescargaComparacion");
  this.obj.ocultaVistaDescarga= $("#ocultaVistaDescarga");
  this.obj.btnCerrarVistaDescarga=$("#btnCerrarVistaDescarga");
  //Seccion Evento
  this.obj.cntEventoDescarga=$("#cntEventoDescarga");
  this.obj.btnGuardarEvento=$("#btnGuardarEvento");  
  this.obj.cmpEventoCliente=$("#cmpEventoCliente");
  this.obj.cmpEventoOperacion=$("#cmpEventoOperacion");
  this.obj.cmpEventoFechaPlanificacion=$("#cmpEventoFechaPlanificacion");
  this.obj.cmpEventoNumeroGuiaRemision=$("#cmpEventoNumeroGuiaRemision");
  this.obj.cmpEventoNumeroOrdenCompra=$("#cmpEventoNumeroOrdenCompra");
  this.obj.cmpEventoCisternaTracto=$("#cmpEventoCisternaTracto");
  this.obj.cmpEventoFechaHora=$("#cmpEventoFechaHora");
  this.obj.cmpEventoDescripcion=$("#cmpEventoDescripcion");
  this.obj.cmpEventoTipoEvento=$("#cmpEventoTipoEvento");  
  this.obj.cmpAprobador=$("#cmpAprobador");
  this.obj.btnGuardarValidarAutorizacion=$("#btnGuardarValidarAutorizacion");  
  this.obj.frmValidarAutorizacion=$("#frmValidarAutorizacion");
  this.obj.btnAutorizarCambioVolumen=$("#btnAutorizarCambioVolumen");
  this.obj.cmpGRFormularioDescarga=$("#cmpGRFormularioDescarga");  
  this.obj.cmpOEFormularioDescarga=$("#cmpOEFormularioDescarga");
  this.obj.cmpCisternaFormularioDescarga=$("#cmpCisternaFormularioDescarga");
  this.obj.cmpVolumenObservadoDespachado=$("#cmpVolumenObservadoDespachado");
  this.obj.cmpFEmisionOEFormularioDescarga=$("#cmpFEmisionOEFormularioDescarga");
  this.obj.cmpFArriboFormularioDescarga=$("#cmpFArriboFormularioDescarga");  
  this.obj.cmpFFiscalizacionFormularioDescarga=$("#cmpFFiscalizacionFormularioDescarga");
  this.obj.cmpFDescargaFormularioDescarga=$("#cmpFDescargaFormularioDescarga"); 
  //datos auditoria
  this.obj.vistaCreadoEl=$("#vistaCreadoEl");
  this.obj.vistaCreadoPor=$("#vistaCreadoPor");
  this.obj.vistaActualizadoPor=$("#vistaActualizadoPor");
  this.obj.vistaActualizadoEl=$("#vistaActualizadoEl");
  this.obj.vistaIpCreacion=$("#vistaIpCreacion");
  this.obj.vistaIpActualizacion=$("#vistaIpActualizacion");
  
  this.obj.btnGuardarDescarga=$("#btnGuardarDescarga");
  this.obj.btnCancelarGuardarDescarga=$("#btnCancelarGuardarDescarga");  
  //Pesaje
  this.obj.cntMetodoPesaje=$("#cntMetodoPesaje");
  this.obj.cmpProductoPesaje=$("#cmpProductoPesaje");
  this.obj.cmpPesajeInicial=$("#cmpPesajeInicial");
  this.obj.cmpPesajeFinal=$("#cmpPesajeFinal");
  this.obj.cmpPesoNeto=$("#cmpPesoNeto");
  this.obj.cmpVolPesaje=$("#cmpVolPesaje");
  this.obj.cmpFactorPesaje=$("#cmpFactorPesaje");
  //Lectura
  this.obj.cntMetodoContometro=$("#cntMetodoContometro");
  this.obj.cmpProductoContometro=$("#cmpProductoContometro");
  this.obj.cmpLecturaInicial=$("#cmpLecturaInicial");
  this.obj.cmpLecturaFinal=$("#cmpLecturaFinal");
  this.obj.cmpVolumenContometro=$("#cmpVolumenContometro");
  this.obj.btnCancelarGuardarEvento= $("#btnCancelarGuardarEvento");
  this.obj.btnCancelarValidarAutorizacion=$("#btnCancelarValidarAutorizacion");
  this.obj.cmpJustificacion = $("#cmpJustificacion");
  
  this.obj.btnDetalleDiaOperativo.on(ref.NOMBRE_EVENTO_CLICK,function(){
	  ref.descripcionPermiso = 'LEER_DESCARGA';
	  ref.validaPermisos();
	  //ref.mostrarDetalleDiaOperativo();
  });
  
  this.obj.btnAgregarCarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
	  ref.descripcionPermiso = 'CREAR_CARGA';
	  ref.validaPermisos();
	  //ref.iniciarAgregarCargaTanque();
  });
  
  this.obj.btnModificarCarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
	  ref.descripcionPermiso = 'ACTUALIZAR_CARGA';
	  ref.validaPermisos();
	  //ref.iniciarModificarCargaTanque();
  });
  
  this.obj.btnAgregarDescarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
	  ref.descripcionPermiso = 'CREAR_DESCARGA';
	  ref.validaPermisos();
	  //ref.iniciarAgregarDescarga();
  }); 
  
  this.obj.btnModificarDescarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
	  ref.descripcionPermiso = 'ACTUALIZAR_DESCARGA';
	  ref.validaPermisos(); 
	  //ref.iniciarModificarDescarga();
  });
	  
  this.obj.btnAgregarEvento.on("click",function(){
	  ref.descripcionPermiso = 'CREAR_EVENTO_DESCARGA';
	  ref.validaPermisos(); 
	  //ref.iniciarAgregarEvento();
  });
  
  this.obj.btnVerDescarga.on("click",function(){
	  ref.descripcionPermiso = 'RECUPERAR_DESCARGA';
	  ref.validaPermisos(); 
	  //ref.iniciarVerDescarga();
  });
  
  this.obj.btnCancelarValidarAutorizacion.on("click", function(){
    ref.obj.frmValidarAutorizacion.modal("hide");
  });

  this.obj.btnAutorizarCambioVolumen.on("click",function(){
    ref.iniciarAutorizacion();
  });
  
  this.obj.btnRegresarPrincipal.on("click",function(){
    ref.regresarDiaOperativo();
  });
 
 this.obj.btnGuardarValidarAutorizacion.on("click",function(e){
    ref.validarAutorizacion();
    e.preventDefault();
  });
  
  this.obj.btnMostrarObservaciones.on("click",function(e){
    ref.mostrarObservaciones();
    e.preventDefault();
  });
   
  
  this.obj.cmpEventoFechaHora.inputmask(constantes.FORMATO_FECHA, {
    "placeholder": constantes.FORMATO_FECHA,
    onincomplete: function(){
        $(this).val('');
    }
  });
  //
  this.obj.btnCerrarVistaDescarga.on("click",function(){
    ref.iniciarCerrarVistaDescarga();
  });
  
  this.obj.btnGuardarEvento.on("click",function(){
    ref.guardarEvento();
  });  
  
  this.obj.cmpVolTotCorRecepcion.on("change",function(){
    var total =parseFloat(ref.obj.cmpVolTotCorRecepcion.val());
    ref.obj.cmpVolumenRecibidoCorregido.val(total).change();
  });
  
  this.obj.cmpVolumenRecibidoCorregido.on("change",function(){
	//REQUERIMIENTO 8 SAR SGCO-004-2016
    var variacion = utilitario.redondearNumeroSinDecimales(parseFloat(ref.obj.cmpVolumenRecibidoCorregido.val()) - parseFloat(ref.obj.cmpVolumenDespachadoCorregido.val()));
    ref.obj.cmpVariacionFinal.val(variacion).change();
  });
  
  this.obj.cmpVariacionFinal.on("change",function(){
	//REQUERIMIENTO 8 SAR SGCO-004-2016
    var variacion = utilitario.redondearNumeroSinDecimales(parseFloat(ref.obj.cmpVolumenRecibidoCorregido.val()) - parseFloat(ref.obj.cmpVolumenDespachadoCorregido.val()));
    ref.obj.cmpVariacionFinal.val(variacion).change();
  });

  this.obj.cmpBuscarCisternaFormularioDescarga.on("change", function (e) {
	  if (ref.descripcionPermiso != 'ACTUALIZAR_DESCARGA'){
		    var opcionSeleccionada =$(this).val();
		    console.log(opcionSeleccionada);
		    ref.obj.cmpVolTotCorRecepcion.val(0);
		    ref.obj.cmpVolTotObsRecepcion.val(0);
		    ref.obj.cmpVolumenDespachadoCorregido.val(0);
		    ref.transporteSeleccionado = ref.cisternasDisponibles[opcionSeleccionada];
		    ref.descripcionPermiso = 'RECUPERAR_TRANSPORTE';
		    ref.validaPermisos();
		    //ref.(ref.cisternasDisponibles[opcionSeleccionada]);
	  }
  });
  
  this.obj.cmpBuscarCisternaFormularioDescarga.on("select2:open", function (e) {
    console.log("Open");
  });
 
  this.obj.cmpPesajeInicial.on("change",function(){
    ref.iniciarCalcularPesoNeto();
  });
  
  this.obj.cmpPesajeFinal.on("change",function(){
    ref.iniciarCalcularPesoNeto();
  });
  
  this.obj.cmpLecturaInicial.on("change",function(){
    ref.calcularVolumenContometro();
  });
  
  this.obj.cmpLecturaFinal.on("change",function(){
    ref.calcularVolumenContometro();
  });
  
  this.obj.btnCancelarGuardarDescarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
    ref.cancelarGuardarDescargaCisterna();
  });  
  //
  //
  this.obj.bandaInformacion=$("#bandaInformacion");  
  this.obj.btnFiltrarDiaOperativo.on(ref.NOMBRE_EVENTO_CLICK,function(){
    ref.listarDiasOperativos();
  });

  this.obj.btnFiltrarCargaTanque.on(ref.NOMBRE_EVENTO_CLICK,function(){
    console.log("btnFiltrarCargaTanque PRINCIPIO");
    ref.listarCargasTanque();
    console.log("btnFiltrarCargaTanque FIN");
  });
 
  this.obj.btnGuardarCarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
    if (ref.modoEdicionCargaTanque == constantes.MODO_NUEVO) {
      ref.guardarCargaTanque();
    } else if (ref.modoEdicionCargaTanque == constantes.MODO_ACTUALIZAR){
      ref.modificarCargaTanque();
    }   
  });
  
  this.obj.btnCancelarGuardarCarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
    ref.cancelarGuardarCargaTanque();
  });
  
  this.obj.btnGuardarDescarga.on(ref.NOMBRE_EVENTO_CLICK,function(){
    ref.guardarDescarga();
  });
  
  this.obj.btnCancelarGuardarEvento.on(ref.NOMBRE_EVENTO_CLICK,function(){
    ref.cancelarAgregarEvento();
  });
  
  this.obj.cmpAprobador.on("change",function(){
    var seleccion = $("option:selected", this);
    var fecha =seleccion.attr("data-fin-vigencia");
    $("#cmpVigenciaHastaValidacion").val(utilitario.formatearFecha(fecha));
  });
};

moduloDescarga.prototype.mostrarObservaciones=function(){
  var esVisible = parseInt( $("#cntObservacionesDescarga").attr("data-visible") );
  console.log(esVisible);
  if (esVisible == 0){
    $("#cntObservacionesDescarga").show();
    $("#cntObservacionesDescarga").attr("data-visible","1");
  } else if (esVisible == 1){
    $("#cntObservacionesDescarga").hide();
    $("#cntObservacionesDescarga").attr("data-visible","0");
  }
};

moduloDescarga.prototype.iniciarAutorizacion=function(){
  var ref=this;
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: ref.URL_RECUPERAR_APROBADOR, 
    contentType: ref.TIPO_CONTENIDO, 
    data: {CodigoInterno:ref.CodigoInternoAutorizacion},  
    success: function(respuesta) {
      if (!respuesta.estado) {
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {
      ref.cargarAutorizacion(respuesta.contenido.carga);
      }
    },                  
    error: function(xhr,estado,error) {
    ref.mostrarErrorServidor(xhr,estado,error); 
    }
  });
  
};

moduloDescarga.prototype.cargarAutorizacion=function(registros){
  var ref=this;
  var numeroRegistros = registros.length;
  ref.obj.cmpAprobador.children().remove();
      ref.obj.cmpAprobador.append($('<option>', { 
    value: "",
    text : "Seleccionar...",
    })); 
  for (var contador=0;contador<numeroRegistros;contador++){
    var registro = registros[contador];
    ref.obj.cmpAprobador.append($('<option>', { 
    value: registro.usuario,
    text : registro.identidad,
    "data-inicio-vigencia": registro.inicioVigencia,
    "data-fin-vigencia": registro.finVigencia
    })); 
  }
  ref.obj.frmValidarAutorizacion.modal("show");
  $("#cmpCodigoValidacion").val("");
  ref.obj.cmpJustificacion.val("");
};

moduloDescarga.prototype.validarAutorizacion= function(){
  var ref=this;
  try {
    var codigoAutorizacion = $("#cmpCodigoValidacion").val();
    var justificacion = ref.obj.cmpJustificacion.val();
    var aprobador =ref.obj.cmpAprobador.val();
    var parametros ={codigoAutorizacion:codigoAutorizacion,nombreUsuario:aprobador,justificacion:justificacion,codigoInterno:ref.CodigoInternoAutorizacion};
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_VALIDAR_AUTORIZACION,
      contentType: ref.TIPO_CONTENIDO, 
      data: (parametros),
      success: function(respuesta) {
        if (!respuesta.estado) {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        ref.idAutorizacionEjecutada=respuesta.valor;
        ref.habilitarIngresoVolumenDirecto();
        $('#frmValidarAutorizacion').modal('hide');
        }
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
      }
    });
  } catch(error){
    
  }
};

moduloDescarga.prototype.habilitarIngresoVolumenDirecto= function(){
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {    
    var cmpVolObsRecepcion = $(row).find('[name="cmpVolObsRecepcion"]');
    var cmpVolCorRecepcion = $(row).find('[name="cmpVolCorRecepcion"]');
    console.log(cmpVolObsRecepcion);
    cmpVolObsRecepcion.prop("disabled",false);
    cmpVolCorRecepcion.prop("disabled",false);
    cmpVolObsRecepcion.prop("readonly",false);
    cmpVolCorRecepcion.prop("readonly",false);
  });  
};

moduloDescarga.prototype.regresarDiaOperativo= function(){
  var ref =this;  
  try {
    ref.obj.cntFormularioDescarga.hide();
    ref.obj.cntFormularioCargaTanque.hide();
    ref.obj.ocultaFormularioDescarga.hide();  
    ref.obj.cntDetalleDiaOperativo.hide();
    ref.obj.cntTablaDiaOperativo.show();
  } catch (error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.iniciarModificarDescarga= function(){
  var ref =this; 
  try {
    ref.modoEdicionDescarga =  constantes.MODO_ACTUALIZAR;
    ref.obj.cntDetalleDiaOperativo.hide();
    ref.obj.cntFormularioCargaTanque.hide();
    ref.obj.ocultaFormularioDescarga.show();
    ref.obj.cntFormularioDescarga.show();
    ref.obj.cntDetalleTransporte.show();
    ref.obj.cntDetalleDescarga.show();
    ref.obj.cntMetodoContometro.hide();
    ref.obj.cntMetodoPesaje.hide();
    ref.obj.cntDescargaComparacion.show();
    ref.nombreEstacionSeleccionada= ref.obj.filtroEstacionDetalleDiaOperativo.find('option:selected').text();
    $("#cmpOperacionFormularioDescarga").val(ref.nombreOperacionSeleccionada);
    $("#cmpEstacionFormularioDescarga").val(ref.nombreEstacionSeleccionada);
    $("#cmpTanqueFormularioDescarga").val(ref.nombreTanqueSeleccionado);   
    //TODO
    //Requerimiento 7 SAR-SGCO-004-2016
    $("#cmpIdProductoTanqueFormularioDescarga").val(ref.idProductoSeleccionado);
    $("#cmpProductoTanqueFormularioDescarga").val(ref.productoSeleccionado);
       
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_DESCARGA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: {ID:ref.idDescargaCisterna},  
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          ref.llenarDescarga(respuesta.contenido.carga[0]);
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        }
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
      }
    });    
  } catch (error) {
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.llenarDescarga= function(registro){
  var ref=this;
  try {
    $("#cntObservacionesDescarga").hide();
    //
    var transporte = registro.transporte;
    ref.transporteSeleccionado = transporte;
    ref.idDescargaCisterna = registro.id;
    //ref.transporteSeleccionado= transporte.id;
    ref.metodoDescargaSeleccionado=registro.metodoDescarga;
    console.log(ref.obj.cmpBuscarCisternaFormularioDescarga);    
     
    //seterar combo BD segun idtransporte
    $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: ref.URL_FILTRAR_TRANSPORTES_ASIGNADOS, 
        contentType: ref.TIPO_CONTENIDO, 
        data: {filtroOperacion:ref.idOperacionSeleccionada,
        	filtroIdTransporte:ref.idTransporteSeleccionado}, 
        success: function(respuesta) {
          if (!respuesta.estado) {
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          } else {
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
            item = respuesta.contenido.carga[0];
			var etiqueta = "G/R : " + item.numeroGuiaRemision + " - Cisterna/Tracto " + item.cisternaTracto;    
			var elemento1 =constantes.PLANTILLA_OPCION_SELECTBOX;
			elemento1 = elemento1.replace(constantes.ID_OPCION_CONTENEDOR, item.id);
			elemento1 = elemento1.replace(constantes.VALOR_OPCION_CONTENEDOR, etiqueta);
			ref.obj.cmpBuscarCisternaFormularioDescarga.empty().append(elemento1).val(item.id).trigger('change');
			ref.obj.cmpBuscarCisternaFormularioDescarga.prop("disabled", true); 
	        }
	          ref.obj.ocultaFormularioDescarga.hide();
        },                  
        error: function(xhr,estado,error) {
          ref.mostrarErrorServidor(xhr,estado,error); 
          ref.obj.ocultaFormularioDescarga.hide();        
        }
      });
    ////////////fin setear combo cisterna//////////

    
    ref.obj.cmpMetodoFormularioDescarga.val(utilitario.formatearMetodoDescarga(ref.metodoDescargaSeleccionado));
    $("#cmpGRFormularioDescarga").val(transporte.numeroGuiaRemision);
    $("#cmpOEFormularioDescarga").val(transporte.numeroOrdenCompra);
    $("#cmpCisternaFormularioDescarga").val(transporte.cisternaTracto);
    $("#cmpVolumenObservadoDespachado").val(transporte.volumenTotalObservado);
    console.log(transporte.fechaEmisionGuia);
    $("#cmpFEmisionOEFormularioDescarga").val(utilitario.formatearFecha(transporte.fechaEmisionGuia));
    console.log(ref.fechaDiaOperativoSeleccionado);
    $("#cmpFDescargaFormularioDescarga").val(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
    console.log(ref.fechaDiaOperativoSeleccionado);
    $("#cmpFArriboFormularioDescarga").val(utilitario.formatearFecha(registro.fechaArribo));
    $("#cmpFFiscalizacionFormularioDescarga").val(utilitario.formatearFecha(registro.fechaFiscalizacion));    
    
    var detalles = transporte.detalles;
    var numeroDetalles = detalles.length;
    $("#tablaCompartimentoDespacho > tbody ").children().remove();
    $("#tablaCompartimentoDescarga > tbody ").children().remove();
    var nombreFila="detalleTransporte";
    ref.listaElementosTransporte={};
    volumenTotalDespachadoCorregido=0;
    for(var contador=0;contador<numeroDetalles;contador++){
      var detalle = detalles[contador];
      ref.apiTemperaturaBaseDespacho = detalle.apiTemperaturaBase;
      var clon = ref.plantillaDetalleTransporte.clone();
      ref.listaElementosTransporte[detalle.numeroCompartimento] = detalle;  
      ref.listaElementosTransporte[detalle.numeroCompartimento].agregado=false;   
      clon.attr('id', nombreFila + detalle.numeroCompartimento);
      clon.attr("data-numero-fila",detalle.numeroCompartimento);
      
      clon.find('#cmpComDespacho').val(detalle.numeroCompartimento);
      clon.find('#cmpComDespacho').attr("data-id-compartimento",detalle.idCompartimento);
      clon.find('#cmpComDespacho').attr('id', "cmpComDespacho" + detalle.numeroCompartimento);
      
      ref.idProductoSeleccionadoUnico = detalle.idProducto;
      clon.find('#cmpProDespacho').val(detalle.descripcionProducto);
      clon.find('#cmpProDespacho').attr('data-producto-id',detalle.idProducto);
      
      clon.find('#cmpProDespacho').attr('id', "cmpProDespacho" + detalle.numeroCompartimento);
      
      clon.find('#cmpVolObsDespacho').val(detalle.volumenTemperaturaObservada);
      clon.find('#cmpVolObsDespacho').attr('id', "cmpVolObsDespacho" + detalle.numeroCompartimento);
      
      clon.find('#cmpTemDespacho').val(detalle.temperaturaObservada);
      clon.find('#cmpTemDespacho').attr('id', "cmpTemDespacho" + detalle.numeroCompartimento);
      
      clon.find('#cmpAPIDespacho').val(detalle.apiTemperaturaBase);
      clon.find('#cmpAPIDespacho').attr('id', "cmpAPIDespacho" + detalle.numeroCompartimento);
      
      clon.find('#cmpFacDespacho').val(detalle.factorCorrecion);
      clon.find('#cmpFacDespacho').attr('id', "cmpFacDespacho" + detalle.numeroCompartimento);
      
      volumenTotalDespachadoCorregido =volumenTotalDespachadoCorregido+detalle.volumenTemperaturaBase;
    //REQUERIMIENTO 8 SAR SGCO-004-2016
      clon.find('#cmpVolCorDespacho').val(utilitario.redondearNumeroSinDecimales(detalle.volumenTemperaturaBase));
      clon.find('#cmpVolCorDespacho').attr('id', "cmpVolCorDespacho" + detalle.numeroCompartimento);

      clon.find('#copiarDetalle').attr('data-numero-comp', detalle.numeroCompartimento);
      clon.find('#copiarDetalle').attr('id', "copiarDetalle" + detalle.numeroCompartimento);
      clon.find("#"+"copiarDetalle" + detalle.numeroCompartimento).on("click",function(){
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.agregarDescargaCisterna(numeroCompartimento);
      });
      
      $("#tablaCompartimentoDespacho > tbody:last-child").append(clon);
    }
  
    var descargas = registro.compartimentos;
    console.log(descargas);
    var numeroDescargas = descargas.length;
    var totalObservado=0;
    var totalCorregido=0;
    for (var contador =0; contador< numeroDescargas;contador++) {
      var detalle = descargas[contador];
      console.log(detalle);
      console.log(detalle.numeroCompartimento);
      var numeroCompartimento= detalle.numeroCompartimento;
        var clonDescarga = ref.plantillaDetalleDescarga.clone();
        var nombreFila ="detalleDescarga";
        clonDescarga.attr('id', nombreFila + detalle.numeroCompartimento);
        
        var cmpCompartimento = clonDescarga.find('#cmpComRecepcion');
        cmpCompartimento.val(detalle.numeroCompartimento);
        cmpCompartimento.attr("data-id-compartimento",detalle.idCompartimento);
        cmpCompartimento.attr('id', "cmpComRecepcion" + detalle.numeroCompartimento);
        
        var cmpProducto = clonDescarga.find('#cmpProRecepcion');
        cmpProducto.val(detalle.producto.nombre);
        cmpProducto.attr('data-producto-id',detalle.idProducto);
        cmpProducto.attr('data-tipo-volumen',detalle.tipoVolumen);
        cmpProducto.attr('data-merma-porcentaje',detalle.mermaPorcentaje);
        cmpProducto.attr('id', "cmpProRecepcion" + detalle.numeroCompartimento);
        
        
        var alturaProducto=0;
        if (detalle.alturaProducto != null) {
          alturaProducto=detalle.alturaProducto;
        }        
        
        var cmpAlturaFlechaTC =clonDescarga.find('#cmpNivelFlechaDespacho');
        cmpAlturaFlechaTC.val(detalle.alturaCompartimento.toFixed(0));
        cmpAlturaFlechaTC.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpAlturaFlechaTC.attr('id', "cmpNivelFlechaDespacho" + detalle.numeroCompartimento);
        
        var cmpAlturaFlechaDescarga = clonDescarga.find('#cmpNivelFlechaDescarga');
        cmpAlturaFlechaDescarga.val(detalle.alturaFlecha.toFixed(0));
        cmpAlturaFlechaDescarga.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpAlturaFlechaDescarga.attr('id', "cmpNivelFlechaDescarga" + detalle.numeroCompartimento);
        
        var cmpAlturaProducto = clonDescarga.find('#cmpAlturaRecepcion');
        cmpAlturaProducto.val(alturaProducto.toFixed(0));
        cmpAlturaProducto.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpAlturaProducto.attr('id', "cmpAlturaRecepcion" + detalle.numeroCompartimento);
        cmpAlturaProducto.on("change",function(){
          var numeroCompartimento = $(this).attr('data-numero-comp');
          ref.recuperarVolumen(numeroCompartimento);
        });
        
        var temperaturaObservada=0;
        if (detalle.temperaturaObservada != null) {
          temperaturaObservada=detalle.temperaturaObservada;
        } 
        var cmpTemperaturaCentro =clonDescarga.find('#cmpTemCentroRecepcion');
        cmpTemperaturaCentro.inputmask("99.9");
        cmpTemperaturaCentro.val(temperaturaObservada.toFixed(1));
        cmpTemperaturaCentro.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpTemperaturaCentro.attr('id', "cmpTemCentroRecepcion" + detalle.numeroCompartimento);
        cmpTemperaturaCentro.on("change",function(){ 
          var numeroCompartimento = $(this).attr('data-numero-comp');
          ref.calculaFactorCorreccion(numeroCompartimento);
        });   
        
        var temperaturaProbeta=0;
        if (detalle.temperaturaProbeta != null) {
          temperaturaProbeta=detalle.temperaturaProbeta;
        } 
        var cmpTemperaturaProbeta =clonDescarga.find('#cmpTempProbetaRecepcion');
        cmpTemperaturaProbeta.inputmask("99.9");
        cmpTemperaturaProbeta.val(temperaturaProbeta.toFixed(1));
        cmpTemperaturaProbeta.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpTemperaturaProbeta.attr('id', "cmpTempProbetaRecepcion" + detalle.numeroCompartimento);
        cmpTemperaturaProbeta.on("change",function(){ 
          var numeroCompartimento = $(this).attr('data-numero-comp');
          console.log(numeroCompartimento);
          ref.calculaFactorCorreccion(numeroCompartimento);
        });    
        
        var apiObservado=0;
        if (detalle.apiTemperaturaObservada != null) {
          apiObservado=detalle.apiTemperaturaObservada;
        }        
        var cmpApiObservado =clonDescarga.find('#cmpAPIObsRecepcion');
        cmpApiObservado.inputmask("99.9");
        cmpApiObservado.val(apiObservado.toFixed(1));
        cmpApiObservado.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpApiObservado.attr('id', "cmpAPIObsRecepcion" + detalle.numeroCompartimento);
        cmpApiObservado.on("change",function(){ 
          var numeroCompartimento = $(this).attr('data-numero-comp');
          console.log(numeroCompartimento);
          ref.calculaFactorCorreccion(numeroCompartimento);
        });  
        
        var factorCorreccion=0;
        if (detalle.factorCorreccion != null) {
          factorCorreccion=detalle.factorCorreccion;
        }
        var cmpFactorCorreccion = clonDescarga.find('#cmpFactorRecepcion');
        cmpFactorCorreccion.val(factorCorreccion.toFixed(6));
        cmpFactorCorreccion.attr('data-numero-comp', detalle.numeroCompartimento);
        cmpFactorCorreccion.attr('id', "cmpFactorRecepcion" + detalle.numeroCompartimento);
        cmpFactorCorreccion.on("change",function(){ 
          var numeroCompartimento = $(this).attr('data-numero-comp');
          ref.calculaFactorCorreccion(numeroCompartimento);
        });  

        var volumenObservado=0;
        console.log(detalle.volumenRecibidoObservado);
        if (detalle.volumenRecibidoObservado != null) {
          volumenObservado=detalle.volumenRecibidoObservado;
        }
        totalObservado=parseFloat(totalObservado)+ parseFloat(volumenObservado) ;
        
        clonDescarga.find('#cmpVolObsRecepcion').val(volumenObservado.toFixed(2));
        clonDescarga.find('#cmpVolObsRecepcion').attr('data-numero-comp', detalle.numeroCompartimento);
        clonDescarga.find('#cmpVolObsRecepcion').attr('id', "cmpVolObsRecepcion" + detalle.numeroCompartimento);
        
        clonDescarga.find("#cmpVolObsRecepcion" + detalle.numeroCompartimento).on("change",function(){ 
          var numeroCompartimento = $(this).attr('data-numero-comp');
          ref.calculaFactorCorreccion(numeroCompartimento);
          ref.calculaVolumenObservadoTotal();
        }); 
        
        var volumenCorregido=0;
        if (detalle.volumenRecibidoCorregido != null) {
          volumenCorregido=detalle.volumenRecibidoCorregido;
        }
        totalCorregido=parseFloat(totalCorregido)+ parseFloat(volumenCorregido) ;
        //REQUERIMIENTO 8 SAR SGCO-004-2016
        clonDescarga.find('#cmpVolCorRecepcion').val(utilitario.redondearNumeroSinDecimales(volumenCorregido.toFixed(2)));
        clonDescarga.find('#cmpVolCorRecepcion').attr('data-numero-comp', detalle.numeroCompartimento);
        clonDescarga.find('#cmpVolCorRecepcion').attr('id', "cmpVolCorRecepcion" + detalle.numeroCompartimento);
        clonDescarga.find("#cmpVolCorRecepcion" + detalle.numeroCompartimento).on("change",function(){ 
          ref.calculaVolumenCorregidoTotal();
        });  
        
        clonDescarga.find('#eliminarDetalle').attr('data-numero-comp', detalle.numeroCompartimento);
        clonDescarga.find('#eliminarDetalle').attr('id', "eliminarDetalle" + detalle.numeroCompartimento);
        clonDescarga.find("#eliminarDetalle" + detalle.numeroCompartimento).on("click",function(){      
          var numeroCompartimento = $(this).attr('data-numero-comp');
          ref.removerDescargaCisterna(numeroCompartimento);
        });

        if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_WINCHA){
          
        } else if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_BALANZA){
          cmpAlturaProducto.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpTemperaturaCentro.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpTemperaturaProbeta.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpApiObservado.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        } else if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_CONTOMETRO){
          cmpAlturaProducto.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpTemperaturaCentro.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpTemperaturaProbeta.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
          cmpApiObservado.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        }  
        ref.listaElementosTransporte[numeroCompartimento].agregado=true;
        $("#tablaCompartimentoDescarga > tbody:last-child").append(clonDescarga);
    }  
    ref.obj.cmpVolTotObsRecepcion.val(totalObservado.toFixed(2));
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    ref.obj.cmpVolTotCorRecepcion.val(utilitario.redondearNumeroSinDecimales(totalCorregido.toFixed(2)));
    //
    if (registro.metodoDescarga == constantes.METODO_DESCARGA_BALANZA){
      console.log("METODO_DESCARGA_BALANZA");
      $("#cntMetodoPesaje").show();
      $("#cntMetodoContometro").hide();
      $("#cmpPesajeInicial").val(registro.pesajeInicial);
      $("#cmpPesajeFinal").val(registro.pesajeFinal);
      $("#cmpPesoNeto").val(registro.pesoNeto);    
      $("#cmpFactorPesaje").val(registro.factorConversion);
      $("#vistaVolPesaje").val(registro.volumenTotalDescargadoCorregido);    
    } else if (registro.metodoDescarga == constantes.METODO_DESCARGA_CONTOMETRO){
      console.log("METODO_DESCARGA_CONTOMETRO");
      $("#cntMetodoPesaje").hide();
      $("#cntMetodoContometro").show();    
      $("#cmpLecturaInicial").val(registro.lecturaInicial);
      $("#cmpLecturaFinal").val(registro.lecturaFinal);
      var volumenContometro = parseFloat(registro.lecturaFinal)-parseFloat(registro.lecturaInicial);
      $("#cmpVolumenContometro").val(volumenContometro.toFixed(2));
    } else if (registro.metodoDescarga == constantes.METODO_DESCARGA_WINCHA){
      console.log("METODO_DESCARGA_WINCHA");
      $("#cntMetodoPesaje").hide();
      $("#cntMetodoContometro").hide();
    }  
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    $("#cmpVolumenDespachadoCorregido").val(utilitario.redondearNumeroSinDecimales(transporte.volumenTotalCorregido.toFixed(2)));
    $("#cmpVolumenRecibidoCorregido").val(utilitario.redondearNumeroSinDecimales(registro.volumenTotalDescargadoCorregido.toFixed(2)));
    $("#cmpVariacionFinal").val(utilitario.redondearNumeroSinDecimales(registro.variacionVolumen.toFixed(2)));
    
    $("#cmpMermaPermisible").val(registro.mermaPermisible.toFixed(2));
    $("#cmpVolumenExcedenteObservado").val(registro.volumenExcedenteObservado.toFixed(2));
    $("#cmpVolumenExcedenteCorregido").val(registro.volumenExcedenteCorregido.toFixed(2));
    console.log(registro.eventos );
    if ((typeof registro.eventos != "undefined") && (registro.eventos != null)){
      var eventos = registro.eventos;
      var numeroEventos = eventos.length;
      for(var contador=0;contador < numeroEventos;contador++){
        var evento = eventos[contador];
        console.log(evento.tipoEvento== constantes.TIPO_EVENTO_OBSERVACIONES);
        if (evento.tipoEvento== constantes.TIPO_EVENTO_OBSERVACIONES){
          $("#cmpDescargaObservacion").val(evento.descripcion);
          $("#cmpDescargaObservacion").attr("data-id",evento.id);
        }
      }
    }
    //
    ref.obj.ocultaFormularioDescarga.hide();
  } catch (error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.cancelarAgregarEvento= function(){
  var ref = this;
  try {
    ref.obj.cntEventoDescarga.hide();
    ref.obj.cntDetalleDiaOperativo.show();
  } catch(error){
    
  }
};
  
moduloDescarga.prototype.iniciarAgregarEvento= function(){
  var ref = this;
  try {
    ref.nombreEstacionSeleccionada= ref.obj.filtroEstacionDetalleDiaOperativo.find('option:selected').text();
    ref.obj.cntDetalleDiaOperativo.hide();
    ref.obj.cmpEventoCliente.text(ref.nombreOperacionSeleccionada);
    ref.obj.cmpEventoOperacion.text(ref.nombreEstacionSeleccionada);
    ref.obj.cmpEventoFechaPlanificacion.text(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
    ref.obj.cmpEventoNumeroGuiaRemision.text(ref.numeroGuiaRemision);
    ref.obj.cmpEventoNumeroOrdenCompra.text(ref.numeroGuiaRemision);
    ref.obj.cmpEventoCisternaTracto.text(ref.placaCisternaSeleccionada+ " / " +ref.placaTractoSeleccionada);  
    ref.obj.cntEventoDescarga.show();    
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.validarDescarga = function(){
 console.log("validarDescarga");
  var resultado =true;
  var ref = this;
  var fGuiaRemision = utilitario.formatearStringToDate($("#cmpFEmisionOEFormularioDescarga").val());//fecha guia  
  var fArribo = utilitario.formatearStringToDate($("#cmpFArriboFormularioDescarga").val());
  var fDescarga = utilitario.formatearStringToDate($("#cmpFDescargaFormularioDescarga").val());
 /* f.guÃ­a <= fecha arribo <= F.Descarga (F.Planificada).*/
  if(fArribo.getTime() >= fGuiaRemision.getTime() ){
	  resultado =true;
  }else{
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La fecha de arribo debe ser mayor o igual que la fecha de gu\u00eda.");
	  ref.mostrarObservaciones();
	  return false;
  }
  if(fArribo.getTime() <= fDescarga.getTime()){
	  resultado =true;
  }else{
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La fecha de arribo debe de ser menor o igual que la fecha de descarga.");
	  ref.mostrarObservaciones();
	  return false;
  }
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {     
      var cmpAlturaFlechaCisterna = $(row).find('[name="cmpNivelFlechaDespacho"]');
      var cmpAlturaFlechaRecepcion = $(row).find('[name="cmpNivelFlechaDescarga"]');     
      var alturaCompartimento=parseInt(cmpAlturaFlechaCisterna.val());
      var alturaFlecha=parseInt(cmpAlturaFlechaRecepcion.val());
      if ((alturaCompartimento != alturaFlecha) && ($("#cmpDescargaObservacion").val()=="")){
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La altura compartimento es diferente a la altura flecha, El campo Observaciones es requerido");
        ref.mostrarObservaciones();
        console.log("alturaCompartimento != alturaFlecha");
       return false;
      }
  });
  
  console.log($("#cmpDescargaObservacion").val());
  console.log(parseFloat($("#cmpVolumenExcedenteObservado").val()));
  if ((parseFloat($("#cmpVolumenExcedenteObservado").val())<0) && ($("#cmpDescargaObservacion").val()=="")){
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"El Vol. Faltante T. Obs es negativo, El campo Observaciones es requerido");
    console.log("cmpVolumenExcedenteObservado");
    ref.mostrarObservaciones();
    return false;
  }

  return resultado;  
};

moduloDescarga.prototype.iniciarCerrarVistaDescarga= function(){
  var ref=this;
  try {
    ref.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESCARGA);
    ref.obj.cntFormularioCargaTanque.hide();
    ref.obj.cntTablaDiaOperativo.hide();
    ref.obj.cntVistaFormularioDescarga.hide();
    ref.obj.cntDetalleDiaOperativo.show();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  } 
};

moduloDescarga.prototype.iniciarVerDescarga =  function(){
  var ref = this;
  ref.obj.cntDetalleDiaOperativo.hide();
  ref.obj.cntVistaFormularioDescarga.show();
  this.obj.ocultaVistaDescarga.show();
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: ref.URL_RECUPERAR_DESCARGA, 
    contentType: ref.TIPO_CONTENIDO, 
    data: {ID:ref.idDescargaCisterna},  
    success: function(respuesta) {
      if (!respuesta.estado) {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
      } else {
    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        ref.llenarVistaDescarga(respuesta.contenido.carga[0]);
        ref.obj.ocultaVistaDescarga.hide();
      }
    },                  
    error: function(xhr,estado,error) {
      ref.mostrarErrorServidor(xhr,estado,error); 
    }
  });  
};

moduloDescarga.prototype.llenarVistaDescarga=function(registro){
  var ref= this;
  var numeroRegistros=0;
  try {
    var transporte = registro.transporte;
    var detalleDescarga = registro.compartimentos;
    var eventos =  registro.eventos;
    $("#vistaOperacionFormularioDescarga").text(registro.nombreOperacion);
    $("#vistaEstacionFormularioDescarga").text(registro.nombreEstacion);
    $("#vistaTanqueFormularioDescarga").text(registro.descripcionTanque);
    $("#vistaMetodoFormularioDescarga").text(utilitario.formatearMetodoDescarga(registro.metodoDescarga));
    $("#vistaFArriboFormularioDescarga").text(utilitario.formatearFecha(registro.fechaArribo));
    $("#vistaFFiscalizacionFormularioDescarga").text(utilitario.formatearFecha(registro.fechaFiscalizacion));
    $("#vistaVolTotObsRecepcion").text(registro.volumenTotalDescargadoObservado);
    $("#vistaVolTotCorRecepcion").text(registro.volumenTotalDescargadoCorregido);
    $("#vistaGRFormularioDescarga").text(transporte.numeroGuiaRemision);
    $("#vistaOEFormularioDescarga").text(transporte.numeroOrdenCompra);
    $("#vistaCisternaFormularioDescarga").text(transporte.cisternaTracto);
    $("#vistaVolumenObservadoDespachado").text(transporte.volumenTotalObservado);    //
    $("#vistaFEmisionOEFormularioDescarga").text(utilitario.formatearFecha(transporte.fechaEmisionGuia));
    $("#vistaFDescargaFormularioDescarga").text($("#cmpFechaPlanificacionDetalleDiaOperativo").val()); 

    $("#vistaCreadoEl").text(registro.fechaCreacion);
    $("#vistaCreadoPor").text(registro.usuarioCreacion);
    $("#vistaActualizadoEl").text(registro.fechaActualizacion);
    $("#vistaActualizadoPor").text(registro.usuarioActualizacion);
    $("#vistaIpCreacion").text(registro.ipCreacion);
    $("#vistaIpActualizacion").text(registro.ipActualizacion);
    
    if (registro.metodoDescarga == constantes.METODO_DESCARGA_BALANZA){
      console.log("METODO_DESCARGA_BALANZA");
      $("#cntVistaMetodoPesaje").show();
      $("#cntVistaMetodoContometro").hide();
      $("#vistaPesajeInicial").text(registro.pesajeInicial);
      $("#vistaPesajeFinal").text(registro.pesajeFinal);
      $("#vistaPesoNeto").text(registro.pesoNeto);    
      $("#vistaFactorPesaje").text(registro.pesajeInicial);
      $("#vistaVolPesaje").text(registro.pesajeInicial);    
    } else if (registro.metodoDescarga == constantes.METODO_DESCARGA_CONTOMETRO){
      console.log("METODO_DESCARGA_CONTOMETRO");
      $("#cntVistaMetodoPesaje").hide();
      $("#cntVistaMetodoContometro").show();    
      $("#vistaLecturaInicial").text(registro.lecturaInicial);
      $("#vistaLecturaFinal").text(registro.lecturaFinal);
      var volumenContometro = parseFloat(registro.lecturaFinal)-parseFloat(registro.lecturaInicial);
      $("#vistaVolumenContometro").text(volumenContometro.toFixed(2));

    } else if (registro.metodoDescarga == constantes.METODO_DESCARGA_WINCHA){
      console.log("METODO_DESCARGA_WINCHA");
      $("#cntVistaMetodoPesaje").hide();
      $("#cntVistaMetodoContometro").hide();
    }
    $("#vistaVolumenRecibidoCorregido").text(registro.volumenTotalDescargadoCorregido.toFixed(2));
    $("#vistaVariacionFinal").text(registro.variacionVolumen.toFixed(2));
    $("#vistaMermaPermisible").text(registro.mermaPermisible.toFixed(2));
    $("#vistaVolumenExcedenteObservado").text(registro.volumenExcedenteObservado.toFixed(2));
    $("#vistaVolumenExcedenteCorregido").text(registro.volumenExcedenteCorregido.toFixed(2));
    $("#vistaVolumenDespachadoCorregido").text(transporte.volumenTotalCorregido.toFixed(2));
    //
    var detalles = transporte.detalles;
    numeroRegistros = detalles.length;
    $("#tablaVistaCompartimentoDespacho > tbody").children().remove();
    //$("#tablaVistaCompartimentoDescarga > tbody").children().remove();
    $("#tablaVistaCompartimentoDescarga tbody tr").remove();
    $("#cntVistaEventosDescarga").children().remove(); 
    for(var contador=0;contador<numeroRegistros;contador++){
      var detalle = detalles[contador];
      var clon = ref.plantillaVistaDetalleTransporte.clone();

      var nombreFila ="detalleTransporte";
      clon.attr('id', nombreFila + detalle.numeroCompartimento);
      clon.attr("data-numero-fila",detalle.numeroCompartimento);
      
      var vistaCompartimento = clon.find('#vistaComDespacho');    
      vistaCompartimento.text(detalle.numeroCompartimento);
      vistaCompartimento.attr('id', "vistaComDespacho" + detalle.numeroCompartimento);

      var vistaProducto = clon.find('#vistaProDespacho');    
      vistaProducto.text(detalle.descripcionProducto);
      vistaProducto.attr('id', "vistaProDespacho" + detalle.numeroCompartimento);

      var vistaVolumenObservado = clon.find('#vistaVolObsDespacho');    
      vistaVolumenObservado.text(detalle.volumenTemperaturaObservada.toFixed(2));
      vistaVolumenObservado.attr('id', "vistaVolObsDespacho" + detalle.numeroCompartimento);
      
      var vistaTemperatura = clon.find('#vistaTemDespacho');    
      vistaTemperatura.text(detalle.temperaturaObservada.toFixed(1));
      vistaTemperatura.attr('id', "vistaTemDespacho" + detalle.numeroCompartimento);

      var vistaAPI = clon.find('#vistaAPIDespacho');    
      vistaAPI.text(detalle.apiTemperaturaBase.toFixed(1));
      vistaAPI.attr('id', "vistaAPIDespacho" + detalle.numeroCompartimento);

      var vistaFactor = clon.find('#vistaFacDespacho');    
      vistaFactor.text(detalle.factorCorrecion.toFixed(6));
      vistaFactor.attr('id', "vistaFacDespacho" + detalle.numeroCompartimento);

      var vistaVolumenCorregido = clon.find('#vistaVolCorDespacho');    
      vistaVolumenCorregido.text(detalle.volumenTemperaturaBase.toFixed(2));
      vistaVolumenCorregido.attr('id', "vistaVolCorDespacho" + detalle.numeroCompartimento);
      $("#tablaVistaCompartimentoDespacho > tbody:last-child").append(clon);
    }
    
    numeroRegistros = detalleDescarga.length;    
    for(var contador=0;contador < numeroRegistros;contador++){
    var detalle = detalleDescarga[contador];
    var clon = ref.plantillaVistaDetalleDescarga.clone();

    var nombreFila ="detalleDescarga";
    clon.attr('id', nombreFila + detalle.numeroCompartimento);
    clon.attr("data-numero-fila",detalle.numeroCompartimento);
    
    var vistaCompartimento = clon.find('#vistaComRecepcion');    
    vistaCompartimento.text(detalle.numeroCompartimento);
    vistaCompartimento.attr('id', "vistaComRecepcion" + detalle.numeroCompartimento);
    
    var vistaProducto = clon.find('#vistaProRecepcion');    
    vistaProducto.text(detalle.producto.nombre);
    vistaProducto.attr('id', "vistaProRecepcion" + detalle.numeroCompartimento);
    
        var vistaAlturaTC = clon.find('#vistaAlturaTC');    
    vistaAlturaTC.text(detalle.alturaCompartimento);
    vistaAlturaTC.attr('id', "vistaAlturaTC" + detalle.numeroCompartimento);
    
        var vistaAlturaFlecha = clon.find('#vistaAlturaFlecha');    
    vistaAlturaFlecha.text(detalle.alturaFlecha);
    vistaAlturaFlecha.attr('id', "vistaAlturaFlecha" + detalle.numeroCompartimento);
    
    var vistaAlturaProducto = clon.find('#vistaAlturaRecepcion');    
    vistaAlturaProducto.text(detalle.alturaProducto);
    vistaAlturaProducto.attr('id', "vistaAlturaRecepcion" + detalle.numeroCompartimento);
    
    var vistaTCentro = clon.find('#vistaTemCentroRecepcion');    
    vistaTCentro.text(detalle.temperaturaObservada.toFixed(1));
    vistaTCentro.attr('id', "vistaTemCentroRecepcion" + detalle.numeroCompartimento);

    var vistaTProbeta = clon.find('#vistaTempProbetaRecepcion');    
    vistaTProbeta.text(detalle.temperaturaProbeta.toFixed(1));
    vistaTProbeta.attr('id', "vistaTempProbetaRecepcion" + detalle.numeroCompartimento);

    var vistaApiObs = clon.find('#vistaAPIObsRecepcion');    
    vistaApiObs.text(detalle.apiTemperaturaObservada.toFixed(1));
    vistaApiObs.attr('id', "vistaAPIObsRecepcion" + detalle.numeroCompartimento);

    var vistaFactor = clon.find('#vistaFactorRecepcion');    
    vistaFactor.text(detalle.factorCorreccion.toFixed(6));
    vistaFactor.attr('id', "vistaFactorRecepcion" + detalle.numeroCompartimento);

    var vistaVolumenObservado = clon.find('#vistaVolObsRecepcion');    
    vistaVolumenObservado.text(detalle.volumenRecibidoObservado.toFixed(2));
    vistaVolumenObservado.attr('id', "vistaVolObsRecepcion" + detalle.numeroCompartimento);

    var vistaVolumenCorregido = clon.find('#vistaVolCorRecepcion');    
    vistaVolumenCorregido.text(detalle.volumenRecibidoCorregido.toFixed(2));
    vistaVolumenCorregido.attr('id', "vistaVolCorRecepcion" + detalle.numeroCompartimento);
    
    $("#tablaVistaCompartimentoDescarga > tbody:last-child").append(clon);
    }
    
    numeroRegistros =  eventos.length;   
    
    for(var contador=0;contador < numeroRegistros;contador++){
      var evento = eventos[contador];
      var clon = ref.plantillaEventoDescarga.clone();
      if (evento.tipoEvento == constantes.TIPO_EVENTO_OBSERVACIONES) {
        $("#vistaObservacionesTexto").text(evento.descripcion);
      } else {
        var vistaTipoEvento = clon.find('#vistaTipoEvento');    
        vistaTipoEvento.text( utilitario.formatearTipoEvento(evento.tipoEvento));      
        vistaTipoEvento.attr('id', "vistaTipoEvento" + contador);

        var vistaFechaHoraEvento = clon.find('#vistaFechaHoraEvento');    
        vistaFechaHoraEvento.text(utilitario.formatearTimestampToString(evento.fechaHora));
        vistaFechaHoraEvento.attr('id', "vistaFechaHoraEvento" + contador);

        var vistaObservacionesEvento = clon.find('#vistaObservacionesEvento');    
        vistaObservacionesEvento.text(evento.descripcion);
        vistaObservacionesEvento.attr('id', "vistaObservacionesEvento" + contador);

        $("#cntVistaEventosDescarga:last-child").append(clon);
      }
    }  
  } catch(error) {
    
  }

};

moduloDescarga.prototype.limpiarFormularioDescarga=function(){
  var ref=this;
  ref.obj.cntMetodoPesaje.hide();
  ref.obj.cntMetodoContometro.hide();
  ref.obj.cntDescargaComparacion.hide();
  ref.obj.cntDetalleDescarga.hide();
  ref.obj.cntDetalleTransporte.hide();
  ref.obj.cmpVolumenDespachadoCorregido.val(0);
  ref.obj.cmpVolumenRecibidoCorregido.val(0);
  ref.obj.cmpVariacionFinal.val(0);
  ref.obj.cmpMermaPermisible.val(0);
  ref.obj.cmpVolumenExcedenteObservado.val(0);
  ref.obj.cmpVolumenExcedenteCorregido.val(0);
  ref.obj.cmpLecturaInicial.val(0);
  ref.obj.cmpLecturaFinal.val(0);
  ref.obj.cmpVolumenContometro.val(0);
  ref.obj.cmpPesajeInicial.val(0);
  ref.obj.cmpPesajeFinal.val(0);
  ref.obj.cmpPesoNeto.val(0);
  ref.obj.cmpFactorPesaje.val(0);
  ref.obj.cmpVolPesaje.val(0);
  ref.factorMasaRecuperado = 0;
  ref.toleranciaRecuperada = 0;
  ref.obj.etiquetaVolRecibidoTotal.text("Vol. Despachado Corregido (gal)");
  ref.obj.etiquetaVolRecibidoTotal.text("Vol. Recibido Corregido (gal)");  
};

moduloDescarga.prototype.recuperarValoresDescarga=function(){
  var ref= this;
  try {
    var descarga={};
    if (ref.idAutorizacionEjecutada > 0) {
      descarga.idAutorizacionEjecutada =ref.idAutorizacionEjecutada;
    }
    descarga.id = ref.idDescargaCisterna;
    descarga.idCargaTanque= ref.idCargaTanque;
    descarga.idDoperativo = ref.idDiaOperativoSeleccionado;
    descarga.idEstacion = ref.idEstacionSeleccionada;
    descarga.idTanque = ref.idTanqueSeleccionado;
    descarga.tipoTanque= ref.idTipoTanqueSeleccionado;    
    descarga.idTransporte = ref.transporteSeleccionado.id;
    descarga.fechaArribo = utilitario.formatearStringToDate(ref.obj.cmpFArriboFormularioDescarga.val());
    descarga.fechaFiscalizacion = utilitario.formatearStringToDate(ref.obj.cmpFFiscalizacionFormularioDescarga.val());
    descarga.metodoDescarga = ref.metodoDescargaSeleccionado;
    descarga.numeroComprobante = "";
    descarga.lecturaInicial = ref.obj.cmpLecturaInicial.val();
    descarga.lecturaFinal = ref.obj.cmpLecturaFinal.val();
    descarga.pesajeInicial = ref.obj.cmpPesajeInicial.val();
    descarga.pesajeFinal = ref.obj.cmpPesajeFinal.val();
    descarga.factorConversion = ref.obj.cmpFactorPesaje.val();
    
    descarga.pesoNeto = ref.obj.cmpPesoNeto.val();
    descarga.volumenTotalDescargadoObservado = ref.obj.cmpVolTotObsRecepcion.val();
    descarga.volumenTotalDescargadoCorregido = ref.obj.cmpVolTotCorRecepcion.val();
    descarga.variacionVolumen = ref.obj.cmpVariacionFinal.val();
   
    descarga.mermaPorcentaje=0.2;
    descarga.mermaPermisible = ref.obj.cmpMermaPermisible.val();
    descarga.volumenExcedenteObservado = ref.obj.cmpVolumenExcedenteObservado.val();
    descarga.volumenExcedenteCorregido = ref.obj.cmpVolumenExcedenteCorregido.val();
    descarga.compartimentos=[];
    descarga.eventos=[];
    $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {
      var detalle={};      
      var cmpProducto = $(row).find('[name="cmpProRecepcion"]');
      var cmpAltura =  $(row).find('[name="cmpAlturaRecepcion"]');
      var cmpCompartimento =  $(row).find('[name="cmpComRecepcion"]');
      console.log(cmpCompartimento);
      var cmpTemCentroRecepcion =  $(row).find('[name="cmpTemCentroRecepcion"]');      
      var cmpTempProbetaRecepcion =  $(row).find('[name="cmpTempProbetaRecepcion"]');
      var cmpAPIObsRecepcion =  $(row).find('[name="cmpAPIObsRecepcion"]');
      var cmpFactorRecepcion =  $(row).find('[name="cmpFactorRecepcion"]');
      var cmpVolObsRecepcion = $(row).find('[name="cmpVolObsRecepcion"]');
      
      var cmpAlturaFlechaCisterna = $(row).find('[name="cmpNivelFlechaDespacho"]');
      var cmpAlturaFlechaRecepcion = $(row).find('[name="cmpNivelFlechaDescarga"]');
      
      var cmpVolCorRecepcion =  $(row).find('[name="cmpVolCorRecepcion"]');      
      var idProducto = parseInt(cmpProducto.attr("data-producto-id")); 
      var idCompartimento = cmpCompartimento.attr("data-id-compartimento");
      var numeroCompartimento = cmpCompartimento.val();
      console.log(idCompartimento);
      detalle.idProducto=idProducto;
      detalle.tipoVolumen= parseInt(cmpProducto.attr("data-tipo-volumen"));
      console.log(parseFloat(cmpProducto.attr("data-merma-porcentaje")));
      detalle.mermaPorcentaje= parseFloat(cmpProducto.attr("data-merma-porcentaje")); 
      detalle.capacidadVolumetricaCompartimento=0;
      detalle.alturaCompartimento=parseInt(cmpAlturaFlechaCisterna.val());
      detalle.alturaFlecha=parseInt(cmpAlturaFlechaRecepcion.val());
      detalle.alturaProducto= parseInt(cmpAltura.val());
      detalle.unidadMedidaVolumen="gal";
      detalle.numeroCompartimento = parseInt(numeroCompartimento);
      detalle.idCompartimento = parseInt(idCompartimento);
      detalle.temperaturaObservada = parseFloat(cmpTemCentroRecepcion.val());
      detalle.temperaturaProbeta= parseFloat(cmpTempProbetaRecepcion.val());
      detalle.apiTemperaturaObservada= parseFloat(cmpAPIObsRecepcion.val());
      detalle.apiTemperaturaBase= parseFloat(cmpAPIObsRecepcion.val());
      detalle.factorCorreccion= parseFloat(cmpFactorRecepcion.val());
      detalle.volumenRecibidoObservado= parseFloat(cmpVolObsRecepcion.val());      
      detalle.volumenRecibidoCorregido= parseFloat(cmpVolCorRecepcion.val());
      console.log(detalle);
      if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_BALANZA){
       detalle.tipoVolumen=ref.tipoVolumenMetodoBalanza;
       detalle.mermaPorcentaje=ref.toleranciaRecuperada;
      }
      if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_CONTOMETRO){
       detalle.tipoVolumen=ref.tipoVolumenMetodoBalanza;
       detalle.mermaPorcentaje=ref.toleranciaRecuperada;
      }
      descarga.tipoMetodo=constantes.METODO_DESCARGA_CONTOMETRO;
      descarga.compartimentos.push(detalle);
    });
   
    var cmpDescargaObservaciones = $("#cmpDescargaObservacion");
    var observaciones = $.trim(cmpDescargaObservaciones.val());
    if (observaciones.length > 0) {
      var evento={};
      evento.id = cmpDescargaObservaciones.attr("data-id");
      evento.descripcion= observaciones;
      evento.tipoEvento= constantes.TIPO_EVENTO_OBSERVACIONES;
      evento.tipoRegistro = (constantes.TIPO_REGISTRO_DESCARGA) ; 
      evento.fechaHora=utilitario.formatearFecha2IsoCompleto(new Date());
      console.log(evento);
      descarga.eventos.push(evento);
    }    
    console.log(descarga); 
  } catch (error) {
    ref.mostrarDepuracion(error.message);
  }
  return descarga;
};

moduloDescarga.prototype.guardarDescarga= function(){
  var ref = this;
  
  if (!ref.validaFormularioXSS("#frmAgregarDescarga")){
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (ref.validarDescarga()){
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    var eRegistro = ref.recuperarValoresDescarga();
    var urlAccion ="";
    if (ref.modoEdicionDescarga == constantes.MODO_NUEVO){
      urlAccion=ref.URL_GUARDAR_DESCARGA;
    } else if (ref.modoEdicionDescarga == constantes.MODO_ACTUALIZAR){
      urlAccion=ref.URL_ACTUALIZAR_DESCARGA;
    }
    console.log(urlAccion);
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: urlAccion, 
      contentType: ref.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),  
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          ref.obj.cntFormularioDescarga.hide();
          ref.obj.cntDetalleDiaOperativo.show();
          ref.listarDescargasCisterna();
        }        
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
      }
    });
  } else {
    //ref.desprotegerFormularioDescarga();
  }
};

moduloDescarga.prototype.iniciarCalcularPesoNeto=function(){
  var ref=this;
  try{
    var pesoInicial = parseFloat(ref.obj.cmpPesajeInicial.val());
    var pesoFinal = parseFloat(ref.obj.cmpPesajeFinal.val());
    var parametros ={apiObservado:ref.apiTemperaturaBaseDespacho};
    parametros.filtroEstacion=ref.idEstacionSeleccionada;
    parametros.filtroProducto=ref.idProductoSeleccionadoUnico;
    if ((pesoInicial >0) && (pesoFinal >0)){
      var pesoNeto =  pesoInicial - pesoFinal;
      ref.obj.cmpPesoNeto.val(pesoNeto);      
      if ((ref.factorMasaRecuperado == 0) && (ref.toleranciaRecuperada == 0 )){
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: ref.URL_RECUPERAR_FACTOR_MASA,
        contentType: ref.TIPO_CONTENIDO,
        data: parametros, 
        success: function(respuesta) {
          if (!respuesta.estado) {
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          } else {
            var registro = respuesta.contenido.carga[0];
            var factor = parseFloat(registro.factorCorreccion);
            var tolerancia = parseFloat(registro.tolerancia);
            var tipoVolumen = parseInt(registro.tipoVolumen);
            ref.factorMasaRecuperado=factor;
            ref.tipoVolumenMetodoBalanza = tipoVolumen;
            ref.toleranciaRecuperada=tolerancia;
            ref.calcularPesoNeto(pesoNeto,factor,tolerancia);
          }        
        },                  
        error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
        }
      });
      } else {
        var factorMasa = ref.factorMasaRecuperado;
        var tolerancia =  ref.toleranciaRecuperada;
        ref.calcularPesoNeto(pesoNeto,factorMasa, tolerancia);
      }
    } else {
      var pesoNeto = 0;  
    }
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};


moduloDescarga.prototype.calcularPesoNeto= function(pesoNeto,factorMasa, tolerancia){
  var ref = this;
  var mermaPermisible=0;
  var volumenExcedente=0;
  var variacionFinal =0;
  var volumenDespachadoCorregido=0;
  var volumenRecibidoCorregido = 0;
  var volumenDespachadoCorregido=0;
  var faltanteCorregido=0;
  var faltanteObservado=0;
  var factorVariacion =1;
  ref.mostrarDepuracion(pesoNeto,"pesoNeto");
  ref.mostrarDepuracion(factorMasa,"factorMasa");
  ref.mostrarDepuracion(tolerancia,"tolerancia");
  volumenDespachadoCorregido = parseFloat(ref.obj.cmpVolumenDespachadoCorregido.val());
  volumenRecibidoCorregido =   pesoNeto/factorMasa;  
  variacionFinal = volumenRecibidoCorregido - volumenDespachadoCorregido;
  if (variacionFinal < 0 ){
   factorVariacion =-1;
  }
  ref.mostrarDepuracion(volumenDespachadoCorregido,"volumenDespachadoCorregido");
  mermaPermisible = (tolerancia/100) * volumenDespachadoCorregido*factorVariacion; 
  ref.mostrarDepuracion(mermaPermisible,"mermaPermisible");
  mermaPermisible =Math.round(mermaPermisible * 100) / 100;
  ref.mostrarDepuracion(mermaPermisible,"mermaPermisible");
  if (isNaN(mermaPermisible)){
    mermaPermisible=0;
  }
  if (variacionFinal < 0 ) {
   if (variacionFinal > mermaPermisible){
    faltanteCorregido=0;
   } else {
    faltanteCorregido = (Math.abs(parseFloat(variacionFinal)) - Math.abs(parseFloat(mermaPermisible))) * factorVariacion;
   }   
  } else {
   faltanteCorregido=0;
  }
  if (isNaN(faltanteCorregido)){
    faltanteCorregido=0;
  }
  faltanteObservado=faltanteCorregido/ref.factorCorreccionMetodoBalanza;
  ref.obj.cmpVolPesaje.val(volumenRecibidoCorregido.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpFactorPesaje.val(factorMasa.toFixed(6)); 
  ref.obj.cmpVolumenRecibidoCorregido.val(volumenRecibidoCorregido.toFixed(ref.NUMERO_DECIMALES));  
  ref.obj.cmpVolTotCorRecepcion.val(volumenRecibidoCorregido.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpVariacionFinal.val(variacionFinal.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpMermaPermisible.val(mermaPermisible.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpVolumenExcedenteObservado.val(faltanteObservado.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpVolumenExcedenteCorregido.val(faltanteCorregido.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpVolumenExcedenteObservado.val(faltanteObservado.toFixed(ref.NUMERO_DECIMALES));
  ref.obj.cmpVolumenExcedenteCorregido.val(faltanteCorregido.toFixed(ref.NUMERO_DECIMALES));  
  ref.repartirVolumen(volumenRecibidoCorregido);  
};

moduloDescarga.prototype.repartirVolumen = function(volumen){
  var ref=this;
  var volumenTotalObservado=0;
  var numeroFilas = $('#tablaCompartimentoDescarga > tbody > tr').length;
  ref.mostrarDepuracion(numeroFilas,"numeroFilas");
  ref.mostrarDepuracion(volumen,"volumen");
  var volumenPromedio = volumen / numeroFilas;
  ref.mostrarDepuracion(volumenPromedio,"volumenPromedio");
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {
    var cmpFactorCorreccion =  $(row).find('[name="cmpFactorRecepcion"]');
    var cmpVolCorRecepcion =  $(row).find('[name="cmpVolCorRecepcion"]');
    var cmpVolObsRecepcion =  $(row).find('[name="cmpVolObsRecepcion"]');
    var volumenObservado =  parseFloat(volumenPromedio) /parseFloat(cmpFactorCorreccion.val());
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    cmpVolCorRecepcion.val(utilitario.redondearNumeroSinDecimales(volumenPromedio.toFixed(ref.NUMERO_DECIMALES)));
    cmpVolObsRecepcion.val(volumenObservado.toFixed(ref.NUMERO_DECIMALES));
    volumenTotalObservado= volumenTotalObservado + parseFloat(cmpVolObsRecepcion.val());
  });
  $("#cmpVolTotObsRecepcion").val(volumenTotalObservado.toFixed(ref.NUMERO_DECIMALES));
};

moduloDescarga.prototype.repartirVolumenObservado = function(volumen){
  var ref=this;
  var numeroFilas = $('#tablaCompartimentoDescarga > tbody > tr').length;
  ref.mostrarDepuracion(numeroFilas,"numeroFilas");
  ref.mostrarDepuracion(volumen,"volumen");
  var volumenPromedio = volumen / numeroFilas;
  var volumenTotalCorregido=0;
  ref.mostrarDepuracion(volumenPromedio,"volumenPromedio");
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {
    var cmpVolObsRecepcion =  $(row).find('[name="cmpVolObsRecepcion"]');
    var cmpVolCorRecepcion =  $(row).find('[name="cmpVolCorRecepcion"]');
    var cmpFactorCorreccion =  $(row).find('[name="cmpFactorRecepcion"]');
    var volumenCorregido = parseFloat(cmpFactorCorreccion.val()) * parseFloat(volumenPromedio);
    cmpVolObsRecepcion.val(volumenPromedio.toFixed(ref.NUMERO_DECIMALES));
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    cmpVolCorRecepcion.val(utilitario.redondearNumeroSinDecimales(volumenCorregido.toFixed(ref.NUMERO_DECIMALES)));
    volumenTotalCorregido= volumenTotalCorregido + parseFloat(cmpVolCorRecepcion.val());
  });
  $("#cmpVolTotCorRecepcion").val(volumenTotalCorregido.toFixed(ref.NUMERO_DECIMALES));
};

moduloDescarga.prototype.calcularVolumenContometro=function(){
  var ref=this;
  var valorInicial =0;
  var valorFinal = 0;
  try {
    valorInicial = parseFloat(ref.obj.cmpLecturaInicial.val());
    valorFinal = parseFloat(ref.obj.cmpLecturaFinal.val());
    var parametros ={};
    parametros.filtroEstacion=ref.idEstacionSeleccionada;
    parametros.filtroProducto=ref.idProductoSeleccionadoUnico;
    if ((valorInicial> 0) && (valorFinal>0)){
      $.ajax({
        type: constantes.PETICION_TIPO_GET,
        url: ref.URL_RECUPERAR_RECUPERAR_TOLERANCIA, 
        contentType: ref.TIPO_CONTENIDO, 
        data: parametros, 
        success: function(respuesta) {
          var volumenObservadoTotalRecibido = 0;
          var volumenObservadoTotalDespachado = 0;
          var variacionFinal = 0;
          var mermaPermisible = 0;
          var tolerancia=0;
          var registroTolerancia=null;
          var volumenExcedenteObservado = 0;
          var volumenExcedenteCorregido = 0;
          //
          if (!respuesta.estado) {        
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
            ref.obj.ocultaFormularioDescarga.hide();
          } else {            
            registroTolerancia =  respuesta.contenido.carga[0];
            tolerancia = registroTolerancia.porcentajeActual;
            volumenObservadoTotalRecibido = valorFinal- valorInicial;
            volumenObservadoTotalDespachado = ref.transporteSeleccionado.volumenTotalObservado;
            variacionFinal = volumenObservadoTotalRecibido - volumenObservadoTotalDespachado;
            mermaPermisible =  volumenObservadoTotalDespachado * (tolerancia/100);
            mermaPermisible = mermaPermisible * -1;
            if (variacionFinal > 0) {
              volumenExcedenteObservado=0;
            } else {
              if (variacionFinal< mermaPermisible){
                volumenExcedenteObservado=variacionFinal+mermaPermisible;
              }
            }
            // if (variacionFinal >= 0 ) {
              // volumenExcedenteObservado = variacionFinal  - mermaPermisible;
            // } else {
              // volumenExcedenteObservado = variacionFinal + mermaPermisible;
            // }
            //TODO
            volumenExcedenteCorregido = volumenExcedenteObservado;
            ref.obj.cmpMermaPermisible.val(mermaPermisible.toFixed(ref.NUMERO_DECIMALES));
            ref.obj.cmpVolumenExcedenteCorregido.val(volumenExcedenteCorregido.toFixed(ref.NUMERO_DECIMALES));
            ref.obj.cmpVolumenExcedenteObservado.val(volumenExcedenteObservado.toFixed(ref.NUMERO_DECIMALES));
            ref.obj.cmpVolumenContometro.val(volumenObservadoTotalRecibido.toFixed(ref.NUMERO_DECIMALES));
            //REQUERIMIENTO 8 SAR SGCO-004-2016
            ref.obj.cmpVolumenRecibidoCorregido.val(utilitario.redondearNumeroSinDecimales(volumenObservadoTotalRecibido.toFixed(ref.NUMERO_DECIMALES)));
            ref.obj.cmpVolTotObsRecepcion.val(utilitario.redondearNumeroSinDecimales(volumenObservadoTotalRecibido.toFixed(ref.NUMERO_DECIMALES)));
            ref.obj.cmpVariacionFinal.val(utilitario.redondearNumeroSinDecimales(variacionFinal.toFixed(ref.NUMERO_DECIMALES)));

            ref.repartirVolumenObservado(volumenObservadoTotalRecibido);
          }        
        },                  
        error: function(xhr,estado,error) {
          ref.mostrarErrorServidor(xhr,estado,error); 
        }
      });
    }
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.cancelarGuardarCargaTanque= function(){
  var ref=this;
  try {
    ref.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESCARGA);
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,cadenas.LISTADO_RECUPERA_EXITOSO);
    ref.obj.cntFormularioCargaTanque.hide();
    ref.obj.cntTablaDiaOperativo.hide();
    ref.obj.cntDetalleDiaOperativo.show();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.recuperarValoresCarga= function(){
  var ref= this;
  try {
    var cargaTanque={};
    cargaTanque.id= ref.idCargaTanque;
    cargaTanque.idTanque = parseInt(ref.obj.cmpTanqueFormularioCargaTanque.val());
    cargaTanque.idEstacion = parseInt(ref.idEstacionSeleccionada);
    cargaTanque.idDiaOperativo = parseInt(ref.idDiaOperativoSeleccionado);  
    //Inicial
    cargaTanque.fechaHoraInicial = utilitario.formatearStringToDateHour(ref.obj.cmpFechaHoraInicial.val());
    cargaTanque.alturaInicial = parseInt(ref.obj.cmpAlturaInicial.val());
    cargaTanque.temperaturaInicialCentro = parseFloat(ref.obj.cmpTemperaturaInicialCentro.val());
    cargaTanque.temperaturaIniciaProbeta = parseFloat(ref.obj.cmpTemperaturaInicialProbeta.val());
    cargaTanque.apiObservadoInicial = parseFloat(ref.obj.cmpAPIObservadoInicial.val());
    cargaTanque.factorCorreccionInicial = parseFloat(ref.obj.cmpFactorInicial.val());
    cargaTanque.volumenObservadoInicial = parseFloat(ref.obj.cmpVolumenInicialObservado.val().replace(ref.SEPARADOR_MILES,""));
    cargaTanque.volumenCorregidoInicial = parseFloat(ref.obj.cmpVolumenInicialCorregido.val().replace(ref.SEPARADOR_MILES,""));    
    //Final
    cargaTanque.fechaHoraFinal = utilitario.formatearStringToDateHour(ref.obj.cmpFechaHoraFinal.val());
    cargaTanque.alturaFinal= parseInt(ref.obj.cmpAlturaFinal.val());
    cargaTanque.temperaturaFinalCentro= parseFloat(ref.obj.cmpTemperaturaFinalCentro.val());
    cargaTanque.temperaturaFinalProbeta= parseFloat(ref.obj.cmpTemperaturaFinalProbeta.val());
    cargaTanque.apiObservadoFinal= parseFloat(ref.obj.cmpAPIObservadoFinal.val());
    cargaTanque.factorCorreccionFinal= parseFloat(ref.obj.cmpFactorFinal.val());
    cargaTanque.volumenObservadoFinal= parseFloat(ref.obj.cmpVolumenFinalObservado.val().replace(ref.SEPARADOR_MILES,""));
    cargaTanque.volumenCorregidoFinal= parseFloat(ref.obj.cmpVolumenFinalCorregido.val().replace(ref.SEPARADOR_MILES,""));
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
  return cargaTanque;
};

moduloDescarga.prototype.recuperarDetalleTransporte = function(){
  var ref= this;
  var transporte = ref.transporteSeleccionado;
  console.log(transporte);
   ref.obj.ocultaFormularioDescarga.show();
  $.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: ref.URL_RECUPERAR_TRANSPORTE, 
    contentType: ref.TIPO_CONTENIDO, 
    data: {ID: transporte.id, },
    success: function(respuesta) {
      if (!respuesta.estado) {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        ref.obj.ocultaFormularioDescarga.hide();
        ref.obj.cntDetalleTransporte.hide();
      } else {
        console.log(respuesta.contenido.carga[0]);
        var registro = respuesta.contenido.carga[0];
        var detalles= registro.detalles.length;
        var existeProducto = false;
        var descripcionProductoSeleccionado = ref.productoSeleccionado;
        var productoSeleccionado = ref.idProductoSeleccionado;
    	console.log("descripcionProductoSeleccionado " + descripcionProductoSeleccionado);
    	console.log("productoSeleccionado " + productoSeleccionado);
    	var nombreProducto = "";
      //$("#tablaVistaDetalle tr").remove();
        if (descripcionProductoSeleccionado.length == 0){
	    	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El tanque no tiene producto definido. Favor verifique.");
    		$("#btnGuardarDescarga").addClass(constantes.CSS_CLASE_DESHABILITADA);
	        ref.obj.ocultaFormularioDescarga.hide();
	        ref.obj.cntDetalleTransporte.hide();
	        ref.obj.cntDetalleDescarga.hide();
	        ref.obj.cntDescargaComparacion.hide();
	    } else{
		    for(var k = 0; k < detalles; k++){ 
		    	var idProducto =  registro.detalles[k].idProducto;
		    	nombreProducto = registro.detalles[k].descripcionProducto;
		    	console.log("idProducto de Cisterna " + idProducto);
		    	console.log("registro.detalles[k].descripcionProducto " + registro.detalles[k].descripcionProducto);
		    	if(idProducto == productoSeleccionado){
		    		existeProducto = true;
		    	}
		    }

		    if (!existeProducto) {
		        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El producto que intenta descargar (" + nombreProducto + ") no coincide con el producto del tanque. Favor verifique.");
		        $("#btnGuardarDescarga").addClass(constantes.CSS_CLASE_DESHABILITADA);
		        ref.obj.ocultaFormularioDescarga.hide();
		        ref.obj.cntDetalleTransporte.hide();
		        ref.obj.cntDetalleDescarga.hide();
		        ref.obj.cntDescargaComparacion.hide();
		    } else {
			    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
			    $("#btnGuardarDescarga").removeClass(constantes.CSS_CLASE_DESHABILITADA);
			    ref.llenarDetalleTransporte(respuesta.contenido.carga[0]);
			  };
	      };
      };
    },                  
    error: function(xhr,estado,error) {
      ref.mostrarErrorServidor(xhr,estado,error); 
    }
  });
};

moduloDescarga.prototype.llenarDetalleTransporte=function(registro){
  var ref= this;
  ref.obj.cntDetalleTransporte.show();
  ref.obj.cntDetalleDescarga.show();
  ref.obj.cntDescargaComparacion.show();  
  $("#cmpGRFormularioDescarga").val(registro.numeroGuiaRemision);
  $("#cmpOEFormularioDescarga").val(registro.numeroOrdenCompra);
  $("#cmpCisternaFormularioDescarga").val(registro.cisternaTracto);
  $("#cmpVolumenObservadoDespachado").val(registro.volumenTotalObservado);
  $("#cmpFEmisionOEFormularioDescarga").val(utilitario.formatearFecha(registro.fechaEmisionGuia));
  $("#cmpFDescargaFormularioDescarga").val(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
  $("#cmpFArriboFormularioDescarga").val(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
  $("#cmpFFiscalizacionFormularioDescarga").val(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
  
  var detalles = registro.detalles;
  ref.cisternaSeleccionada = registro.cisterna;
  var numeroDetalles = detalles.length;
  $("#tablaCompartimentoDespacho > tbody ").children().remove();
  $("#tablaCompartimentoDescarga > tbody ").children().remove();
  var nombreFila="detalleTransporte";
  ref.listaElementosTransporte={};
  var volumenTotalDespachadoCorregido=0;
  var volumenTotalDespachadoObservado=0;
  var nombreColumnaCompartimento = "cmpComDespacho";
  var nombreColumnaProducto = "cmpProDespacho";
  var nombreColumnaVolumenObservado = "cmpVolObsDespacho";
  var nombreColumnaTemperatura = "cmpTemDespacho";
  var nombreColumnaAPI="cmpAPIDespacho";
  var nombreColumnaFactorCorreccion ="cmpFacDespacho";
  var nombreColumnaVolumenCorregido ="cmpVolCorDespacho";
  for(var contador=0;contador<numeroDetalles;contador++){
    var detalle = detalles[contador];    
    ref.idProductoSeleccionadoUnico = detalle.idProducto;
    ref.apiTemperaturaBaseDespacho = detalle.apiTemperaturaBase;
    var clon = ref.plantillaDetalleTransporte.clone();
    ref.listaElementosTransporte[detalle.numeroCompartimento] = detalle;  
    ref.listaElementosTransporte[detalle.numeroCompartimento].agregado=false;   
    clon.attr('id', nombreFila + detalle.numeroCompartimento);
    clon.attr("data-numero-fila",detalle.numeroCompartimento);
    
    var cmpCompartimento = clon.find('#'+ nombreColumnaCompartimento);
    cmpCompartimento.val(detalle.numeroCompartimento);
    cmpCompartimento.attr("data-id-compartimento",detalle.idCompartimento);
    cmpCompartimento.attr('id', nombreColumnaCompartimento + detalle.numeroCompartimento);
    
    var cmpProducto = clon.find('#'+nombreColumnaProducto);
    cmpProducto.val(detalle.descripcionProducto);
    cmpProducto.attr('data-producto-id',detalle.idProducto);
    cmpProducto.attr('id', nombreColumnaProducto + detalle.numeroCompartimento);
    
    var cmpVolumenObservado = clon.find('#' + nombreColumnaVolumenObservado);
    cmpVolumenObservado.val(detalle.volumenTemperaturaObservada.toFixed(ref.NUMERO_DECIMALES));
    cmpVolumenObservado.attr('id', nombreColumnaVolumenObservado + detalle.numeroCompartimento);
    
    var cmpTemperatura = clon.find('#'+nombreColumnaTemperatura);
    cmpTemperatura.inputmask("99.9");
    cmpTemperatura.val(detalle.temperaturaObservada.toFixed(1));
    cmpTemperatura.attr('id', nombreColumnaTemperatura + detalle.numeroCompartimento);
    
    var cmpAPI = clon.find('#'+nombreColumnaAPI);
    cmpAPI.inputmask("99.9");
    cmpAPI.val(detalle.apiTemperaturaBase.toFixed(1));
    cmpAPI.attr('id', nombreColumnaAPI + detalle.numeroCompartimento);
    
    var cmpFactorCorreccion = clon.find('#'+nombreColumnaFactorCorreccion);
    cmpFactorCorreccion.val(detalle.factorCorrecion.toFixed(6));
    cmpFactorCorreccion.attr('id', nombreColumnaFactorCorreccion + detalle.numeroCompartimento);
    
    var volumenCorregido = detalle.volumenTemperaturaBase;
     var volumenObservado = detalle.volumenTemperaturaObservada;
//    detalle.volumenTemperaturaObservada * detalle.factorCorrecion;
    console.log(volumenCorregido);
    volumenCorregido = utilitario.redondearNumero(volumenCorregido,2);
    console.log(volumenCorregido);
    volumenTotalDespachadoCorregido =volumenTotalDespachadoCorregido+volumenCorregido;
    volumenTotalDespachadoObservado = volumenTotalDespachadoObservado + volumenObservado;
    //detalle.volumenTemperaturaBase;    
    var cmpVolumenCorregido = clon.find('#'+nombreColumnaVolumenCorregido);
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    cmpVolumenCorregido.val(utilitario.redondearNumeroSinDecimales(volumenCorregido.toFixed(ref.NUMERO_DECIMALES)));
    cmpVolumenCorregido.attr('id', nombreColumnaVolumenCorregido + detalle.numeroCompartimento);

    var cmpCopiarDetalle = clon.find('#copiarDetalle');
    cmpCopiarDetalle.attr('data-numero-comp', detalle.numeroCompartimento);
    cmpCopiarDetalle.attr('id', "copiarDetalle" + detalle.numeroCompartimento);
    cmpCopiarDetalle.on("click",function(){
      var numeroCompartimento = $(this).attr('data-numero-comp');
      ref.agregarDescargaCisterna(numeroCompartimento);
    });
    
    $("#tablaCompartimentoDespacho > tbody:last-child").append(clon);
    ref.agregarDescargaCisterna(detalle.numeroCompartimento);
  }
  //Pintar volumen total despachado corregido
//REQUERIMIENTO 8 SAR SGCO-004-2016
  ref.obj.cmpVolumenDespachadoCorregido.val(utilitario.redondearNumeroSinDecimales(volumenTotalDespachadoCorregido.toFixed(ref.NUMERO_DECIMALES)));
  if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_WINCHA){
    ref.obj.cntMetodoContometro.hide();
    ref.obj.cntMetodoPesaje.hide(); 
  } else if (ref.metodoDescargaSeleccionado == constantes.METODO_DESCARGA_BALANZA){
    ref.obj.cntMetodoContometro.hide();
    ref.obj.cntMetodoPesaje.show();      
  } else if (ref.metodoDescargaSeleccionado == constantes.METODO_DESCARGA_CONTOMETRO){
    ref.obj.etiquetaVolDespachadoTotal.text("Vol. Despachado Obs. (gal) ");
    ref.obj.etiquetaVolRecibidoTotal.text("Vol. Recibido Obs. (gal) ");
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    ref.obj.cmpVolumenDespachadoCorregido.val(utilitario.redondearNumeroSinDecimales(volumenTotalDespachadoObservado.toFixed(ref.NUMERO_DECIMALES)));
    ref.obj.cntMetodoPesaje.hide();  
    ref.obj.cntMetodoContometro.show();      
  }
  ref.obj.ocultaFormularioDescarga.hide();
};


moduloDescarga.prototype.recuperarVolumenTanque=function(medida){
	  var ref=this;
	  var nombreCampoVolumenObservado="";
	  var parametros={};
	  var volumenObservado = 0;
	  volumenObservado = volumenObservado.toFixed(ref.NUMERO_DECIMALES);
      $(nombreCampoVolumenObservado).val(volumenObservado).change();
	  try {
	    ref.obj.ocultaFormularioDescarga.show();
	    nombreCampoVolumenObservado="#cmpVolumen"+medida+"Observado";
	    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);    
	    parametros={filtroTanque:0,filtroCentimetros:0};
	    parametros.filtroTanque=parseInt($("#cmpTanqueFormularioCargaTanque").val());
	    parametros.filtroCentimetros = parseInt($("#cmpAltura" + medida).val());
	    $.ajax({
	      type: constantes.PETICION_TIPO_GET,
	      //url: ref.URL_RECUPERAR_AFORO_TANQUE,
	      url:'./aforo-tanque/interpolacion', 
	      contentType: ref.TIPO_CONTENIDO, 
	      data: parametros, 
	      success: function(respuesta) {
	        if (!respuesta.estado) {
	        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	        } else {
	          if (respuesta.contenido.carga.length > 0 ) {
	            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	            var registro = respuesta.contenido.carga[0];
	            volumenObservado = parseFloat(registro.volumen);
	            //Volumen Observado Recibido Redondea a entero
	            //volumenObservado = Math.ceil(volumenObservado);
	            volumenObservado = volumenObservado.toFixed(ref.NUMERO_DECIMALES);
	            $(nombreCampoVolumenObservado).val(volumenObservado).change();
	          } else {
	        	  volumenObservado = 0;
	              $(nombreCampoVolumenObservado).val(volumenObservado).change();
	            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.MENSAJE_ALTURA_NO_AFORADA);
	          }
	        }
	        ref.obj.ocultaFormularioDescarga.hide();
	      },                  
	      error: function(xhr,estado,error) {
	        ref.mostrarErrorServidor(xhr,estado,error);
	        ref.obj.ocultaFormularioDescarga.hide();
	      }
	    });
	  } catch(error){
	    ref.mostrarDepuracion(error.message);
	    ref.obj.ocultaFormularioDescarga.hide();
	  } 
	};

moduloDescarga.prototype.recuperarVolumen=function(numeroCompartimento){
  var ref=this;
  var nombreCampoVolumenObservado="";
  var parametros={};
  try {
    ref.obj.ocultaFormularioDescarga.show();
    nombreCampoVolumenObservado="#cmpVolObsRecepcion";
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);    
    parametros={filtroTracto:0,filtroCisterna:0,filtroCompartimento:0,filtroMilimetros:0};
    parametros.filtroTracto=ref.transporteSeleccionado.idTracto;
    parametros.filtroCisterna=ref.transporteSeleccionado.idCisterna;
    parametros.filtroCompartimento=parseInt($("#cmpComRecepcion" + numeroCompartimento).attr("data-id-compartimento"));
    parametros.filtroMilimetros = parseInt($("#cmpAlturaRecepcion" + numeroCompartimento).val());
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_AFORO_CISTERNA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: parametros, 
      success: function(respuesta) {
        if (!respuesta.estado) {
        ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          if (respuesta.contenido.carga.length > 0 ) {
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
            var registro = respuesta.contenido.carga[0];
            var volumenObservado = parseFloat(registro.volumen);
            //Volumen Observado Recibido Redondea a entero
            //volumenObservado = Math.ceil(volumenObservado);
            volumenObservado = volumenObservado.toFixed(ref.NUMERO_DECIMALES);
            $(nombreCampoVolumenObservado + numeroCompartimento).val(volumenObservado).change();
          } else {
            ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.MENSAJE_ALTURA_NO_AFORADA);
          }
        }
        ref.obj.ocultaFormularioDescarga.hide();
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error);
        ref.obj.ocultaFormularioDescarga.hide();
      }
    });
  } catch(error){
    ref.mostrarDepuracion(error.message);
    ref.obj.ocultaFormularioDescarga.hide();
  } 
};
moduloDescarga.prototype.calculaFactorCorreccionTanque=function(parametros,medida){
	  var ref=this;
	  var temperaturaCentro= parametros.temperaturaCentro;
	  var temperaturaProbeta= parametros.temperaturaProbeta;
	  var apiObservado= parametros.apiObservado;	  
	  if ((typeof parametros.volumenObservado == "undefined") || (parametros.volumenObservado == null) || (parametros.volumenObservado == '')){
		  parametros.volumenObservado = null;
	  }
	  
	  if ((temperaturaCentro>0) && (temperaturaProbeta>0) &&  (apiObservado > 0)){
	    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
	    ref.obj.ocultaFormularioDescarga.show();
//	    var parametros={};
//	    parametros.apiObservado=apiObservado;
//	    parametros.temperaturaProbeta=temperaturaProbeta;
//	    parametros.temperaturaCentro=temperaturaCentro;
//	    parametros.volumenObservado=volumenObservado;
//	    parametros.filtroEstacion=idEstacion;
//	    parametros.filtroProducto=idProducto;
	    $.ajax({
	      type: constantes.PETICION_TIPO_GET,
	      url: ref.URL_RECUPERAR_RECUPERAR_VOLUMEN_CORREGIDO, 
	      contentType: ref.TIPO_CONTENIDO, 
	      data: parametros, 
	      success: function(respuesta) {
	       var factorCorrecion = 0;
	       var mermaPorcentaje = 0;
	       var tipoVolumen = 0;
	       var apiCorregido =0;
	       var volumenCorregido =0;       
	        if (!respuesta.estado) {
	          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);          
	        } else {
	          var registro = respuesta.contenido.carga[0];
	          factorCorrecion = parseFloat(registro.factorCorreccion);
	          mermaPorcentaje = parseFloat(registro.tolerancia);
	          tipoVolumen = parseInt(registro.tipoVolumen);
	          factorCorrecion = factorCorrecion.toFixed(6);
	          apiCorregido =parseFloat(registro.apiCorregido);
	          volumenCorregido =parseFloat(registro.volumenCorregido);
	          //volumenCorregido = Math.ceil(volumenCorregido);
	          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	        }
//	        $("#cmpProRecepcion" + medida).val("data-merma-porcentaje",mermaPorcentaje);
//	        $("#cmpProRecepcion" + medida).attr("data-tipo-volumen",tipoVolumen);
//	        $("#cmpProRecepcion" + medida).attr("data-tipo-merma-porcentaje",mermaPorcentaje);
	        $("#cmpFactor" + medida).val(factorCorrecion);
	        //$("#cmpAPIObservado" + medida).val(apiCorregido);
	        $("#cmpVolumen"+medida+"Corregido").val(volumenCorregido.toFixed(ref.NUMERO_DECIMALES)).change();        
	        ref.obj.ocultaFormularioDescarga.hide();
	      },                  
	      error: function(xhr,estado,error) {
	        ref.mostrarErrorServidor(xhr,estado,error);
	        ref.obj.ocultaFormularioDescarga.hide();      
	      }
	    }); 
	  }
	};

moduloDescarga.prototype.calculaFactorCorreccion=function(numeroCompartimento){
  var ref=this;
  var temperaturaCentro= $("#cmpTemCentroRecepcion" + numeroCompartimento).val();
  var temperaturaProbeta= $("#cmpTempProbetaRecepcion" + numeroCompartimento).val();
  var apiObservado= $("#cmpAPIObsRecepcion" + numeroCompartimento).val();
  var volumenObservado= $("#cmpVolObsRecepcion" + numeroCompartimento).val();
  var idProducto = $("#cmpProRecepcion" + numeroCompartimento).attr("data-producto-id");
  var idEstacion = ref.idEstacionSeleccionada;
  if ((temperaturaCentro>0) && (temperaturaProbeta>0) &&  (apiObservado > 0)){
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    ref.obj.ocultaFormularioDescarga.show();
    var parametros={};
    parametros.apiObservado=apiObservado;
    parametros.temperaturaProbeta=temperaturaProbeta;
    parametros.temperaturaCentro=temperaturaCentro;
    parametros.volumenObservado=volumenObservado;
    parametros.filtroEstacion=idEstacion;
    parametros.filtroProducto=idProducto;
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_RECUPERAR_VOLUMEN_CORREGIDO, 
      contentType: ref.TIPO_CONTENIDO, 
      data: parametros, 
      success: function(respuesta) {
       var factorCorrecion = 0;
       var mermaPorcentaje = 0;
       var tipoVolumen = 0;
       var apiCorregido =0;
       var volumenCorregido =0;       
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);          
        } else {
          var registro = respuesta.contenido.carga[0];
          factorCorrecion = parseFloat(registro.factorCorreccion);
          mermaPorcentaje = parseFloat(registro.tolerancia);
          tipoVolumen = parseInt(registro.tipoVolumen);
          factorCorrecion = factorCorrecion.toFixed(6);
          apiCorregido =parseFloat(registro.apiCorregido);
          volumenCorregido =parseFloat(registro.volumenCorregido);
          //volumenCorregido = Math.ceil(volumenCorregido);
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
        }
        $("#cmpProRecepcion" + numeroCompartimento).attr("data-merma-porcentaje",mermaPorcentaje);
        $("#cmpProRecepcion" + numeroCompartimento).attr("data-tipo-volumen",tipoVolumen);
        $("#cmpProRecepcion" + numeroCompartimento).attr("data-tipo-merma-porcentaje",mermaPorcentaje);
        $("#cmpFactorRecepcion" + numeroCompartimento).val(factorCorrecion);
        $("#cmpAPIObsRecepcion" + numeroCompartimento).attr("data-api-corregido",apiCorregido);
      //REQUERIMIENTO 8 SAR SGCO-004-2016
        $("#cmpVolCorRecepcion" + numeroCompartimento).val(utilitario.redondearNumeroSinDecimales(volumenCorregido.toFixed(ref.NUMERO_DECIMALES))).change();        
        ref.obj.ocultaFormularioDescarga.hide();
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error);
        ref.obj.ocultaFormularioDescarga.hide();      
      }
    }); 
  }
};

moduloDescarga.prototype.calculaVolumenObservadoTotal= function(){
  var ref=this;
  var totalObservadoRecibido=0;
  ref.obj.cmpVolTotObsRecepcion.val(totalObservadoRecibido);
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {
    var cmpVolObsRecepcion = $(row).find('[name="cmpVolObsRecepcion"]');
    totalObservadoRecibido = totalObservadoRecibido + parseFloat(cmpVolObsRecepcion.val());
  });
  ref.obj.cmpVolTotObsRecepcion.val(totalObservadoRecibido).change();
};

moduloDescarga.prototype.calculaVolumenCorregidoTotal= function(){
  console.log("calculaVolumenCorregidoTotal");
  var ref=this;
  var totalCorregidoRecibido=0;
  var TotalVariacion = 0 ;
  var totalExcedenteObservado =0;
  var totalExcedenteCorregido=0;
  var mermaPermisibleTotal =0;
//REQUERIMIENTO 8 SAR SGCO-004-2016
  ref.obj.cmpVolTotCorRecepcion.val(utilitario.redondearNumeroSinDecimales(totalCorregidoRecibido));
  $('#tablaCompartimentoDescarga > tbody  > tr').each(function (i, row) {
    var excedenteObservado=0;
    var excedenteCorregido=0;
    var factorCorreccion=0;
    factorCorreccion=$(row).find('[name="cmpFactorRecepcion"]').val();
    factorCorreccion=parseFloat(factorCorreccion);
    var numeroCompartimento = $(row).find('[name="cmpComRecepcion"]').val();    
    var mermaPermisiblePorcentaje = $(row).find('[name="cmpProRecepcion"]').attr("data-merma-porcentaje");
    var volumenCorregidoRecibido = $(row).find('[name="cmpVolCorRecepcion"]').val();
    var volumenCorregidoDespachado = $("#cmpVolCorDespacho" + numeroCompartimento).val();
    mermaPermisiblePorcentaje =parseFloat(mermaPermisiblePorcentaje);
    volumenCorregidoRecibido= parseFloat(volumenCorregidoRecibido);
    volumenCorregidoDespachado= parseFloat(volumenCorregidoDespachado);
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    var variacion = utilitario.redondearNumeroSinDecimales((volumenCorregidoRecibido) - (volumenCorregidoDespachado)); 
    var mermaPermisible  = -1* (mermaPermisiblePorcentaje/100) * volumenCorregidoDespachado;
    
    mermaPermisible =Math.round(mermaPermisible * 100) / 100;
    if (isNaN(mermaPermisible)){
      mermaPermisible=0;
    }
    mermaPermisibleTotal = mermaPermisibleTotal +mermaPermisible;
    TotalVariacion=TotalVariacion + variacion;
  //REQUERIMIENTO 8 SAR SGCO-004-2016
    totalCorregidoRecibido = utilitario.redondearNumeroSinDecimales(totalCorregidoRecibido + volumenCorregidoRecibido); 
    excedenteCorregido = parseFloat(variacion)  - parseFloat(mermaPermisible);
        if (isNaN(excedenteCorregido)){
      excedenteCorregido=0;
    }
    excedenteObservado=excedenteCorregido/factorCorreccion;
    if (isNaN(excedenteObservado)){
      excedenteObservado=0;
    }    
    totalExcedenteObservado = totalExcedenteObservado + excedenteObservado;
    totalExcedenteCorregido = totalExcedenteCorregido + excedenteCorregido;
    
  });
  totalExcedenteObservado =TotalVariacion-mermaPermisibleTotal;
//REQUERIMIENTO 8 SAR SGCO-004-2016
  ref.obj.cmpVariacionFinal.val(utilitario.redondearNumeroSinDecimales(TotalVariacion.toFixed(ref.NUMERO_DECIMALES)));
  ref.obj.cmpVolTotCorRecepcion.val(utilitario.redondearNumeroSinDecimales(totalCorregidoRecibido.toFixed(ref.NUMERO_DECIMALES)));
  ref.obj.cmpVolumenRecibidoCorregido.val(utilitario.redondearNumeroSinDecimales(totalCorregidoRecibido.toFixed(ref.NUMERO_DECIMALES)));
  
  ref.obj.cmpMermaPermisible.val(mermaPermisibleTotal.toFixed(ref.NUMERO_DECIMALES));
  var valorFaltante = 0;
  if (TotalVariacion < 0) {
    ref.obj.cmpVolumenExcedenteObservado.val(totalExcedenteObservado.toFixed(1));
    ref.obj.cmpVolumenExcedenteCorregido.val(totalExcedenteCorregido.toFixed(1)); 
  } else {
    ref.obj.cmpVolumenExcedenteObservado.val(valorFaltante.toFixed(1));
    ref.obj.cmpVolumenExcedenteCorregido.val(valorFaltante.toFixed(1)); 
  }

};

moduloDescarga.prototype.agregarDescargaCisterna=function(numeroCompartimento){
  var ref = this;
  try {
    var detalle = ref.listaElementosTransporte[numeroCompartimento];
    if (detalle.agregado==false){
      var clonDescarga = ref.plantillaDetalleDescarga.clone();
      var nombreFila ="detalleDescarga";
      clonDescarga.attr('id', nombreFila + detalle.numeroCompartimento);
      console.log( ref.cisternaSeleccionada.compartimentos);
      var alturaFlechaDespacho = ref.cisternaSeleccionada.compartimentos[numeroCompartimento-1]["alturaFlecha"];
      console.log(alturaFlechaDespacho);

      var cmpNivelFlechaDespacho = clonDescarga.find('#cmpNivelFlechaDespacho');
      cmpNivelFlechaDespacho.val(alturaFlechaDespacho);
      cmpNivelFlechaDespacho.attr("data-id-compartimento",detalle.idCompartimento);
      cmpNivelFlechaDespacho.attr('id', "cmpNivelFlechaDespacho" + detalle.numeroCompartimento);
      
      var cmpNivelFlechaDescarga = clonDescarga.find('#cmpNivelFlechaDescarga');
      cmpNivelFlechaDescarga.val(0);
      cmpNivelFlechaDescarga.attr("data-id-compartimento",detalle.idCompartimento);
      cmpNivelFlechaDescarga.attr('id', "cmpNivelFlechaDescarga" + detalle.numeroCompartimento);
      
      
      var cmpCompartimento = clonDescarga.find('#cmpComRecepcion');
      cmpCompartimento.val(detalle.numeroCompartimento);
      cmpCompartimento.attr("data-id-compartimento",detalle.idCompartimento);
      cmpCompartimento.attr('id', "cmpComRecepcion" + detalle.numeroCompartimento);
      
      var cmpProducto = clonDescarga.find('#cmpProRecepcion');
      cmpProducto.val(detalle.descripcionProducto);
      cmpProducto.attr('data-producto-id',detalle.idProducto);
      cmpProducto.attr('id', "cmpProRecepcion" + detalle.numeroCompartimento);
      
      var cmpAlturaProducto =clonDescarga.find('#cmpAlturaRecepcion');
      cmpAlturaProducto.val(0);
      cmpAlturaProducto.attr('data-numero-comp', detalle.numeroCompartimento);
      cmpAlturaProducto.attr('id', "cmpAlturaRecepcion" + detalle.numeroCompartimento);
      cmpAlturaProducto.on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.recuperarVolumen(numeroCompartimento);
      });
      
      var cmpTemperaturaCentro =clonDescarga.find('#cmpTemCentroRecepcion');
      cmpTemperaturaCentro.inputmask("99.9");
      cmpTemperaturaCentro.val(0.00);
      cmpTemperaturaCentro.attr('data-numero-comp', detalle.numeroCompartimento);
      cmpTemperaturaCentro.attr('id', "cmpTemCentroRecepcion" + detalle.numeroCompartimento);
      cmpTemperaturaCentro.on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.calculaFactorCorreccion(numeroCompartimento);
      });   
      
      var cmpTemperaturaProbeta =clonDescarga.find('#cmpTempProbetaRecepcion');
      cmpTemperaturaProbeta.inputmask("99.9");
      cmpTemperaturaProbeta.val(0.00);
      cmpTemperaturaProbeta.attr('data-numero-comp', detalle.numeroCompartimento);
      cmpTemperaturaProbeta.attr('id', "cmpTempProbetaRecepcion" + detalle.numeroCompartimento);
      cmpTemperaturaProbeta.on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        console.log(numeroCompartimento);
        ref.calculaFactorCorreccion(numeroCompartimento);
      });    
      
      var cmpApiObservado =clonDescarga.find('#cmpAPIObsRecepcion');
      cmpApiObservado.inputmask("99.9");
      cmpApiObservado.val(0.00);
      cmpApiObservado.attr('data-numero-comp', detalle.numeroCompartimento);
      cmpApiObservado.attr('id', "cmpAPIObsRecepcion" + detalle.numeroCompartimento);
      cmpApiObservado.on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        console.log(numeroCompartimento);
        ref.calculaFactorCorreccion(numeroCompartimento);
      });  
      
      var cmpFactorCorreccion = clonDescarga.find('#cmpFactorRecepcion');
      cmpFactorCorreccion.val(0);
      cmpFactorCorreccion.attr('data-numero-comp', detalle.numeroCompartimento);
      cmpFactorCorreccion.attr('id', "cmpFactorRecepcion" + detalle.numeroCompartimento);
      cmpFactorCorreccion.on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.calculaFactorCorreccion(numeroCompartimento);
      });  

      clonDescarga.find('#cmpVolObsRecepcion').val(0);
      clonDescarga.find('#cmpVolObsRecepcion').attr('data-numero-comp', detalle.numeroCompartimento);
      clonDescarga.find('#cmpVolObsRecepcion').attr('id', "cmpVolObsRecepcion" + detalle.numeroCompartimento);
      clonDescarga.find("#cmpVolObsRecepcion" + detalle.numeroCompartimento).on("change",function(){ 
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.calculaFactorCorreccion(numeroCompartimento);
        ref.calculaVolumenObservadoTotal();
      });  

      clonDescarga.find('#cmpVolCorRecepcion').val(0);
      clonDescarga.find('#cmpVolCorRecepcion').attr('data-numero-comp', detalle.numeroCompartimento);
      clonDescarga.find('#cmpVolCorRecepcion').attr('id', "cmpVolCorRecepcion" + detalle.numeroCompartimento);
      clonDescarga.find("#cmpVolCorRecepcion" + detalle.numeroCompartimento).on("change",function(){ 
        ref.calculaVolumenCorregidoTotal();
      });  
      
      clonDescarga.find('#eliminarDetalle').attr('data-numero-comp', detalle.numeroCompartimento);
      clonDescarga.find('#eliminarDetalle').attr('id', "eliminarDetalle" + detalle.numeroCompartimento);
      clonDescarga.find("#eliminarDetalle" + detalle.numeroCompartimento).on("click",function(){      
        var numeroCompartimento = $(this).attr('data-numero-comp');
        ref.removerDescargaCisterna(numeroCompartimento);
        ref.calculaVolumenObservadoTotal();
        ref.calculaVolumenCorregidoTotal();
      });

      if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_WINCHA){
        
      } else if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_BALANZA){
        cmpAlturaProducto.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpTemperaturaCentro.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpTemperaturaProbeta.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpApiObservado.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpNivelFlechaDescarga.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpFactorCorreccion.val(detalle.factorCorrecion);
        ref.factorCorreccionMetodoBalanza = detalle.factorCorrecion;
      } else if (ref.metodoDescargaSeleccionado== constantes.METODO_DESCARGA_CONTOMETRO){
        cmpAlturaProducto.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpTemperaturaCentro.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpTemperaturaProbeta.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpApiObservado.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpNivelFlechaDescarga.attr(ref.ATRIBUTO_LECTURA,ref.ATRIBUTO_LECTURA);
        cmpFactorCorreccion.val(detalle.factorCorrecion);
      }  
      ref.listaElementosTransporte[numeroCompartimento].agregado=true;
      $("#tablaCompartimentoDescarga > tbody:last-child").append(clonDescarga);
    }
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.removerDescargaCisterna= function(numeroCompartimento){
  var ref=this;
  ref.listaElementosTransporte[numeroCompartimento].agregado=false;
  $("#detalleDescarga" + numeroCompartimento ).remove();  
};

moduloDescarga.prototype.cancelarGuardarDescargaCisterna= function(){
  var ref=this;
  try {
    ref.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESCARGA);
    ref.obj.cntFormularioCargaTanque.hide();
    ref.obj.cntTablaDiaOperativo.hide();
    ref.obj.cntFormularioDescarga.hide();
    ref.obj.cntDetalleDiaOperativo.show();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  } 
};

moduloDescarga.prototype.guardarCargaTanque= function(){
  var ref = this;
  if (!ref.validaFormularioXSS("#frmAgregarCargaTanque")){
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (ref.obj.frmAgregarCargaTanque.valid()){
    ref.obj.ocultaContenedorFormularioCargaTanque.show();
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    var eRegistro = ref.recuperarValoresCarga();
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: ref.URL_GUARDAR_CARGA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),  
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          ref.obj.ocultaContenedorFormularioCargaTanque.hide();
        } else {
          ref.listarCargasTanque();
          ref.obj.cntFormularioCargaTanque.hide();
          ref.obj.cntDetalleDiaOperativo.show();
        }        
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
      }
    });
  } else {
    ref.obj.ocultaContenedorFormularioCargaTanque.hide();
  }
};

moduloDescarga.prototype.modificarCargaTanque= function(){
  var ref = this;
  if (!ref.validaFormularioXSS("#frmAgregarCargaTanque")){
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (ref.obj.frmAgregarCargaTanque.valid()){
    ref.obj.ocultaContenedorFormularioCargaTanque.show();
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    var eRegistro = ref.recuperarValoresCarga();
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: ref.URL_ACTUALIZAR_CARGA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),  
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
          ref.obj.ocultaContenedorFormularioCargaTanque.hide();
        } else {
          ref.listarCargasTanque();
          ref.obj.cntFormularioCargaTanque.hide();
          ref.obj.cntDetalleDiaOperativo.show();
        }        
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error); 
      }
    });
  } else {
    ref.obj.ocultaContenedorFormularioCargaTanque.hide();
  }
};

moduloDescarga.prototype.inicializarGrillas = function(){
  var ref=this;
  ref.inicializarGrillaDiaOperativo();
  ref.inicializarGrillaCargaTanque();  
  ref.inicializarGrillaDescarga();
};

moduloDescarga.prototype.llamadaAjaxGrillaDiaOperativo = function(e,configuracion,json){
  var ref = this;
  try {
    ref.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
    ref.desactivarBotones(ref.GRILLA_DIA_OPERATIVO);
    if (json.estado === true){
      json.recordsTotal=json.contenido.totalRegistros;
      json.recordsFiltered=json.contenido.totalEncontrados;
      json.data= json.contenido.carga;
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    } else {
      json.recordsTotal=0;
      json.recordsFiltered=0;
      json.data= {};
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,json.mensaje);
    }
    if (ref.estaCargadaInterface === false){        
      ref.estaCargadaInterface = true;
    }
    ref.obj.ocultaContenedorTablaDiaOperativo.hide(); 
  } catch(error){
    ref.mostrarDepuracion(error.message);
  } 
};

moduloDescarga.prototype.llamadaAjaxDiaOperativo = function(cfgDiaOperativo){
  var ref = this;
  var rangoFecha = ref.obj.filtroFechaPlanificada.val().split("-");
  var fechaInicio = utilitario.formatearFecha2Iso(rangoFecha[0]) ;
  var fechaFinal = utilitario.formatearFecha2Iso(rangoFecha[1]);
  var indiceOrdenamiento = cfgDiaOperativo.order[0].column;
  cfgDiaOperativo.registrosxPagina =  cfgDiaOperativo.length; 
  cfgDiaOperativo.inicioPagina = cfgDiaOperativo.start; 
  cfgDiaOperativo.campoOrdenamiento = cfgDiaOperativo.columns[indiceOrdenamiento].data;
  cfgDiaOperativo.sentidoOrdenamiento = cfgDiaOperativo.order[0].dir;
  cfgDiaOperativo.filtroOperacion = ref.obj.filtroOperacion.val();
  cfgDiaOperativo.filtroFechaInicio = fechaInicio;
  cfgDiaOperativo.filtroFechaFinal = fechaFinal;
};

moduloDescarga.prototype.inicializarGrillaDiaOperativo=function(){
  var ref = this;
  try {
    this.obj.tablaDiaOperativo.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
      ref.llamadaAjaxGrillaDiaOperativo(e,configuracion,json);     
    });
      
    this.obj.tablaDiaOperativo.on(constantes.DT_EVENTO_PREAJAX, function(e,configuracion,json) {
      ref.obj.ocultaContenedorTablaDiaOperativo.show();
    });
    
    this.obj.tablaDiaOperativo.on(constantes.DT_EVENTO_PAGINACION, function () {
    });
    
    this.obj.tablaDiaOperativo.on(constantes.DT_EVENTO_ORDENACION, function () {
      ref.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
    });

    this.obj.tablaDiaOperativoAPI = this.obj.tablaDiaOperativo.DataTable({
    deferLoading: 0,
    processing: true,
    responsive: true,
    dom: constantes.DT_ESTRUCTURA,
    iDisplayLength: ref.NUMERO_REGISTROS_PAGINA,
    lengthMenu:ref.TOPES_PAGINACION,
    language: {
      url: ref.URL_LENGUAJE_GRILLA
    },
    serverSide: true,
    ajax: {
      url: ref.URL_LISTAR_DIA_OPERATIVO,
      type:constantes.PETICION_TIPO_GET,
      data: function (cfgDiaOperativo) {
        ref.llamadaAjaxDiaOperativo(cfgDiaOperativo);
      }
    },
    columns : ref.columnasGrillaDiaOperativo,
    columnDefs : ref.definicionColumnasGrillaDiaOperativo,
    order: ref.ordenGrillaDiaOperativo
    }); 
    
    $('#tablaDiaOperativo tbody').on(ref.NOMBRE_EVENTO_CLICK,'tr',function () {
      console.log("tablaDiaOperativo");
      var indiceFila=0;
      var selected=0;
      var estadoActual=null;
        if (ref.obj.tablaDiaOperativoAPI.data().length > 0){
          selected = parseInt($(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA)) ;
          if ((typeof selected == "undefined" ) || (selected==null) || (isNaN(selected))){
           selected=0;
          }
          if (selected == 1){
           //Remover la seleccion y el formato
           $(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA,ref.FILA_NO_SELECCIONADA);
           $(this).removeClass(ref.CLASE_FILA_SELECCIONADA);
           ref.desactivarBotones(ref.GRILLA_DIA_OPERATIVO);
          } else if (selected == 0) {
           //Elimina el formato de otras filas, Agregar el formato de seleccionado y la seleccion
           ref.obj.tablaDiaOperativoAPI.$('tr.selected').removeClass(ref.CLASE_FILA_SELECCIONADA);        
           $(this).addClass(ref.CLASE_FILA_SELECCIONADA);
           $(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA,ref.FILA_SELECCIONADA);           
           indiceFila = ref.obj.tablaDiaOperativoAPI.row( this ).index();
           ref.idDiaOperativoSeleccionado = ref.obj.tablaDiaOperativoAPI.cell(indiceFila,ref.INDICE_COLUMNA_ID_DIA_OPERATIVO).data();
           ref.fechaDiaOperativoSeleccionado = ref.obj.tablaDiaOperativoAPI.cell(indiceFila,ref.INDICE_COLUMNA_FECHA_DIA_OPERATIVO).data();
           estadoActual = ref.obj.tablaDiaOperativoAPI.cell(indiceFila,ref.INDICE_COLUMNA_ESTADO_DIA_OPERATIVO).data();
           ref.estadoDiaOperativo = estadoActual;
           ref.activarBotones(ref.GRILLA_DIA_OPERATIVO);
          }
        }  
    });
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.llamadaAjaxGrillaCargaTanque = function(e,configuracion,json){
  console.log("llamadaAjaxGrillaCargaTanque");
  var ref = this;
  try {
    ref.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
    ref.desactivarBotones(ref.GRILLA_CARGA_TANQUE);
    if (json.estado === true){
      json.recordsTotal=json.contenido.totalRegistros;
      json.recordsFiltered=json.contenido.totalEncontrados;
      json.data= json.contenido.carga;
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    } else {
      json.recordsTotal=0;
      json.recordsFiltered=0;
      json.data= {};
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,json.mensaje);
    }
    ref.obj.ocultaContenedorTablaCargaTanque.hide(); 
    ref.obj.ocultaContenedorTablaDescarga.hide();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  } 
};

moduloDescarga.prototype.llamadaAjaxCargaTanque = function(cfgCargaTanque){
  console.log("llamadaAjaxCargaTanque");
  var ref = this;
  var indiceOrdenamiento = cfgCargaTanque.order[0].column;
  cfgCargaTanque.registrosxPagina =  cfgCargaTanque.length; 
  cfgCargaTanque.inicioPagina = cfgCargaTanque.start; 
  cfgCargaTanque.campoOrdenamiento= cfgCargaTanque.columns[indiceOrdenamiento].data;
  cfgCargaTanque.sentidoOrdenamiento=cfgCargaTanque.order[0].dir;
  cfgCargaTanque.valorBuscado=cfgCargaTanque.search.value;
  cfgCargaTanque.filtroEstacion = ref.idEstacionSeleccionada;
  cfgCargaTanque.filtroFechaPlanificada = ref.idDiaOperativoSeleccionado;
};

moduloDescarga.prototype.inicializarGrillaCargaTanque=function(){
  var ref = this;
  try {
    this.obj.tablaCargaTanque.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
      ref.llamadaAjaxGrillaCargaTanque(e,configuracion,json);
    });
      
    this.obj.tablaCargaTanque.on(constantes.DT_EVENTO_PREAJAX, function ( e, settings, data ) {
      ref.obj.ocultaContenedorTablaCargaTanque.show();
      ref.obj.ocultaContenedorTablaDescarga.show();
      //ref.listar
    });
    
    this.obj.tablaCargaTanque.on(constantes.DT_EVENTO_PAGINACION, function () {
      ref.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
    });
    
    this.obj.tablaCargaTanque.on(constantes.DT_EVENTO_ORDENACION, function () {
      ref.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
    });
    
    this.obj.tablaCargaTanqueAPI = this.obj.tablaCargaTanque.DataTable({
    processing: true,
    deferLoading: 0,
    responsive: true,
    dom: constantes.DT_ESTRUCTURA,
    iDisplayLength: 3,
    lengthMenu:ref.TOPES_PAGINACION_CARGA_TANQUE,
    language: {
      url: ref.URL_LENGUAJE_GRILLA
    },
    serverSide: true,
    ajax: {
      url: ref.URL_LISTAR_CARGA,
      type:constantes.PETICION_TIPO_GET,
      data: function (cfgCargaTanque) {
        ref.llamadaAjaxCargaTanque(cfgCargaTanque);
      }
    },
    columns : ref.columnasGrillaCargaTanque,
    columnDefs : ref.definicionColumnasGrillaCargaTanque,
    order: ref.ordenGrillaCargaTanque
    }); 
   
    $('#tablaCargaTanque tbody').on(ref.NOMBRE_EVENTO_CLICK,'tr',function () {
     var indiceFila=0;
     var selected=0;
      if (ref.obj.tablaCargaTanqueAPI.data().length > 0){
        selected = parseInt($(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA)) ;
        if ((typeof selected == ref.TIPO_DATO_NO_DEFINIDO ) || (selected==null) || (isNaN(selected))){
         selected=0;
        }
        if (selected == ref.FILA_SELECCIONADA){
         //Remover la seleccion y el formato
         $(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA,ref.FILA_NO_SELECCIONADA);
         $(this).removeClass(ref.CLASE_FILA_SELECCIONADA);
         ref.desactivarBotones(ref.GRILLA_CARGA_TANQUE);
        } else if (selected == ref.FILA_NO_SELECCIONADA) {
         //Elimina el formato de otras filas, Agregar el formato de seleccionado y la seleccion
         ref.obj.tablaCargaTanqueAPI.$('tr.selected').removeClass(ref.CLASE_FILA_SELECCIONADA);        
         $(this).addClass(ref.CLASE_FILA_SELECCIONADA);
         $(this).attr(ref.ATRIBUTO_FILA_SELECCIONADA,ref.FILA_SELECCIONADA);
         indiceFila = ref.obj.tablaCargaTanqueAPI.row(this).index();
         ref.idCargaTanque = ref.obj.tablaCargaTanqueAPI.cell(indiceFila,1).data();
         ref.idTanqueSeleccionado= ref.obj.tablaCargaTanqueAPI.cell(indiceFila,11).data();
         ref.nombreTanqueSeleccionado = ref.obj.tablaCargaTanqueAPI.cell(indiceFila,2).data();
         ref.idTipoTanqueSeleccionado = ref.obj.tablaCargaTanqueAPI.cell(indiceFila,10).data();
         ref.idProductoSeleccionado = ref.obj.tablaCargaTanqueAPI.cell(indiceFila,13).data();
         ref.productoSeleccionado = ref.obj.tablaCargaTanqueAPI.cell(indiceFila,12).data();
         ref.activarBotones(ref.GRILLA_CARGA_TANQUE);
         ref.listarDescargasCisterna();
        }
      } else {
        ref.obj.tablaDescargaCisternaAPI.clear();
      }
    });
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};


moduloDescarga.prototype.llamadaAjaxCompletadaGrillaDescarga = function(e,configuracion,json){
  console.log("llamadaAjaxCompletadaGrillaDescarga");
  var ref = this;
  try {
    ref.mostrarDepuracion(constantes.DT_EVENTO_AJAX);
    ref.desactivarBotones(ref.GRILLA_DESCARGA_CISTERNA);
    if (json.estado === true){
      json.recordsTotal=json.contenido.totalRegistros;
      json.recordsFiltered=json.contenido.totalEncontrados;
      json.data= json.contenido.carga;
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,json.mensaje);
    } else {
      json.recordsTotal=0;
      json.recordsFiltered=0;
      json.data= {};
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,json.mensaje);
    }
    ref.obj.ocultaContenedorTablaDescarga.hide();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  } 
};

moduloDescarga.prototype.configuracionLlamadaAjaxGrillaDescarga = function(cfgDescarga){
  var ref = this;
  var indiceOrdenamiento = cfgDescarga.order[0].column;
  cfgDescarga.registrosxPagina =  cfgDescarga.length; 
  cfgDescarga.inicioPagina = cfgDescarga.start; 
  cfgDescarga.campoOrdenamiento= cfgDescarga.columns[indiceOrdenamiento].data;
  cfgDescarga.sentidoOrdenamiento=cfgDescarga.order[0].dir;
  console.log("configuracionLlamadaAjaxGrillaDescarga");
  console.log(ref.idCargaTanque);
  cfgDescarga.filtroCargaTanque = ref.idCargaTanque;
};

moduloDescarga.prototype.inicializarGrillaDescarga=function(){
  var ref = this;
  try {
    this.obj.tablaDescargaCisterna.on(constantes.DT_EVENTO_AJAX, function (e,configuracion,json) {
      ref.llamadaAjaxCompletadaGrillaDescarga(e,configuracion,json);     
    });
      
    this.obj.tablaDescargaCisterna.on(constantes.DT_EVENTO_PREAJAX, function ( e, settings, data ) {
      ref.obj.ocultaContenedorTablaDescarga.show();      
    });
    
    this.obj.tablaDescargaCisterna.on(constantes.DT_EVENTO_PAGINACION, function () {
      ref.mostrarDepuracion(constantes.DT_EVENTO_PAGINACION);
    });
    
    this.obj.tablaDescargaCisterna.on(constantes.DT_EVENTO_ORDENACION, function () {
      ref.mostrarDepuracion(constantes.DT_EVENTO_ORDENACION);
    });
    
    this.obj.tablaDescargaCisternaAPI = this.obj.tablaDescargaCisterna.DataTable({
    processing: true,
    deferLoading: 0,
    responsive: true,
    dom: constantes.DT_ESTRUCTURA,
    iDisplayLength: 3,
    lengthMenu:ref.TOPES_PAGINACION_CARGA_TANQUE,
    language: {
      url: ref.URL_LENGUAJE_GRILLA
    },
    serverSide: true,
    ajax: {
      url: ref.URL_LISTAR_DESCARGA,
      type:constantes.PETICION_TIPO_GET,
      data: function (cfgDescarga) {
        ref.configuracionLlamadaAjaxGrillaDescarga(cfgDescarga);
      }
    },
    columns : ref.columnasGrillaDescarga,
    columnDefs : ref.definicionGrillaDescarga,
    order: ref.ordenGrillaDescarga
    }); 
    
    $('#tablaDescargaCisterna tbody').on(ref.NOMBRE_EVENTO_CLICK,'tr',function () {
      if (ref.obj.tablaDescargaCisternaAPI.data().length > 0){
        if ( $(this).hasClass('selected') ) {
          $(this).removeClass('selected');
        } else {
          ref.obj.tablaDescargaCisternaAPI.$('tr.selected').removeClass('selected');
          $(this).addClass('selected');
        }
        var indiceFila = ref.obj.tablaDescargaCisternaAPI.row(this).index();
        ref.idDescargaCisterna = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,1).data();
        ref.placaCisternaSeleccionada = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,2).data();
        ref.placaTractoSeleccionada = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,3).data();
        ref.numeroGuiaRemision = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,4).data();
        ref.idTransporteSeleccionado = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,9).data();
        
        //ref.numeroGuiaRemision = ref.obj.tablaDescargaCisternaAPI.cell(indiceFila,4).data();
        ref.activarBotones(ref.GRILLA_DESCARGA_CISTERNA);
      }    
    });
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.activarBotones=function(grilla){ 
  var ref=this;
  if (grilla===ref.GRILLA_DIA_OPERATIVO) {
    ref.obj.btnDetalleDiaOperativo.removeClass(constantes.CSS_CLASE_DESHABILITADA);
  }
  if (grilla==ref.GRILLA_CARGA_TANQUE){
    switch(parseInt(ref.estadoDiaOperativo)){
    case parseInt(constantes.ESTADO_PLANIFICADO) :
    case parseInt(constantes.ESTADO_ASIGNADO):
    case parseInt(constantes.ESTADO_DESCARGANDO):
      ref.obj.btnAgregarCarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnModificarCarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnAgregarDescarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      break;
    case parseInt(constantes.ESTADO_CERRADO):
      ref.obj.btnAgregarCarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnModificarCarga.addClass(constantes.CSS_CLASE_DESHABILITADA); 
      ref.obj.btnAgregarDescarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
      break;
      default:
    }
  }
  if (grilla==ref.GRILLA_DESCARGA_CISTERNA){
  switch(parseInt(ref.estadoDiaOperativo)){
    case parseInt(constantes.ESTADO_PLANIFICADO) :
    case parseInt(constantes.ESTADO_ASIGNADO):
    case parseInt(constantes.ESTADO_DESCARGANDO):
      //ref.obj.btnAgregarDescarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnModificarDescarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnVerDescarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnAgregarEvento.removeClass(constantes.CSS_CLASE_DESHABILITADA);
      break;
    case parseInt(constantes.ESTADO_CERRADO):
      //ref.obj.btnAgregarDescarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
      ref.obj.btnModificarDescarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
        ref.obj.btnVerDescarga.removeClass(constantes.CSS_CLASE_DESHABILITADA);
        ref.obj.btnAgregarEvento.addClass(constantes.CSS_CLASE_DESHABILITADA);
      break;
    default:
  }
  }
};

moduloDescarga.prototype.desactivarBotones=function(grilla){
  var ref=this;
  if (grilla == ref.GRILLA_DIA_OPERATIVO) {
    this.obj.btnDetalleDiaOperativo.addClass(constantes.CSS_CLASE_DESHABILITADA);
  }
  if (grilla == ref.GRILLA_CARGA_TANQUE) {
    this.obj.btnModificarCarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
  }
  if (grilla==ref.GRILLA_DESCARGA_CISTERNA){
    ref.obj.btnModificarDescarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
    ref.obj.btnVerDescarga.addClass(constantes.CSS_CLASE_DESHABILITADA);
    ref.obj.btnAgregarEvento.addClass(constantes.CSS_CLASE_DESHABILITADA);
  }
};

moduloDescarga.prototype.mostrarDetalleDiaOperativo= function(){
  var ref=this;
  var item =null;
  var numeroEstaciones=0;
  var listadoEstaciones=null;
  var listaEstaciones=null;
  console.log(cadenas);
  try {
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.PROCESANDO_PETICION);
    ref.idOperacionSeleccionada=ref.obj.filtroOperacion.val();
    ref.idTanqueSeleccionado=0;
    ref.nombreOperacionSeleccionada = $("#filtroOperacion option:selected").text();
    listaEstaciones = cacheDescarga.estaciones[ref.idOperacionSeleccionada];

    if ((typeof listaEstaciones == "undefined") || (listaEstaciones.length==0)) {
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.TEXTO_NO_ESTACIONES_OPERACION);
    } else {
      ref.obj.tituloSeccion.text(cadenas.TITULO_DETALLE_DESCARGA);
    ref.activarBotones(ref.GRILLA_CARGA_TANQUE);
    ref.obj.cntTablaDiaOperativo.hide();
    ref.obj.cntDetalleDiaOperativo.show();
    ref.obj.cmpOperacionDetalleDiaOperativo.val(ref.nombreOperacionSeleccionada);
      ref.obj.cmpFechaPlanificacionDetalleDiaOperativo.val(utilitario.formatearFecha(ref.fechaDiaOperativoSeleccionado));
      ref.obj.ocultaContenedorTablaCargaTanque.show();
      ref.obj.ocultaContenedorTablaDescarga.show();
      ref.pintarEstaciones(listaEstaciones);
      ref.listarCargasTanque(); 
    }   
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.pintarCisternas=function(listadoCisternas){
  var ref=this;
  var numeroRegistros = listadoCisternas.length;
  ref.obj.cmpBuscarCisternaFormularioDescarga.children().remove();
  ref.obj.cmpBuscarCisternaFormularioDescarga.append("<option></option>");
  for(var contador = 0;contador < numeroRegistros;contador++){
    var item = listadoCisternas[contador];
    var etiqueta = "G/R : " + item.numeroGuiaRemision + " - Cisterna/Tracto " + item.cisternaTracto;
    ref.obj.cmpBuscarCisternaFormularioDescarga.append($('<option>', { 
      value: item.id,
      text : etiqueta
    })); 
  }
  ref.obj.cmpBuscarCisternaFormularioDescarga.select2({
    placeholder: "Seleccionar",
    allowClear: false
  });
};
moduloDescarga.prototype.pintarEstaciones=function(listadoEstaciones){
  var ref=this;
  var numeroEstaciones = listadoEstaciones.length;
  ref.obj.filtroEstacionDetalleDiaOperativo.children().remove();
  for(var contador = 0;contador < numeroEstaciones;contador++){
    var item = listadoEstaciones[contador];
    ref.obj.filtroEstacionDetalleDiaOperativo.append($('<option>', { 
      value: item.id,
      text : item.nombre,
      "data-metodo": item.metodo
    })); 
  }  
};

moduloDescarga.prototype.pintarTanques=function(listadoTanques){
  var ref=this;
  var numeroTanques = listadoTanques.length;
  ref.obj.cmpTanqueFormularioCargaTanque.children().remove();
  ref.pintarProductoTanque(parseInt(listadoTanques[0].id));
  for(var contador=0;contador < numeroTanques;contador++){
    item = listadoTanques[contador];
    ref.obj.cmpTanqueFormularioCargaTanque.append($('<option>', { 
    value: item.id,
    text : item.nombre
    }));
  }  
};

//TODO KANB
moduloDescarga.prototype.pintarProductoTanque = function(idTanque){
    var ref=this;
   	$.ajax({
	    type: constantes.PETICION_TIPO_GET,
	    url: './tanque/listar', 
	    contentType: ref.TIPO_CONTENIDO, 
	    data: {idTanque:parseInt(idTanque), },	
	    success: function(respuesta) {
	    	var registro = respuesta.contenido.carga[0];
	    	if(registro.producto.nombre.length == 0){
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "El tanque seleccionado no tiene Producto configurado. Favor verifique.");
	    		$("#btnGuardarCarga").addClass(constantes.CSS_CLASE_DESHABILITADA);
	    		ref.obj.cmpProductoFormularioCargaTanque.val("");
	    	} else{
	    		ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO, "El registro fue recuperado con éxito.");
	    		ref.obj.cmpProductoFormularioCargaTanque.val(registro.producto.nombre);
	    		$("#btnGuardarCarga").removeClass(constantes.CSS_CLASE_DESHABILITADA);
	    	}
	    },			    		    
	    error: function(xhr,estado,error) {
	    ref.mostrarErrorServidor(xhr,estado,error);        
        }
	});
  };	

/*//TODO KANB
moduloDescarga.prototype.validaProductoTanque = function(idEstacion){
	var ref=this;
 	$.ajax({
    type: constantes.PETICION_TIPO_GET,
    url: './tanque/listar', 
    contentType: ref.TIPO_CONTENIDO, 
    data: {filtroEstacion:parseInt(idEstacion), },	
    success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, "Esta estación no cuenta con Tanques. Favor verificar.");
        } else {
          registro = respuesta.contenido.carga;
          String error = "";
		  for(var contador = 0; contador < registro.length; contador++){
			  
          }
          if (ref.modoEdicionCargaTanque == constantes.MODO_ACTUALIZAR){
            ref.llenarFormularioCargaTanque(respuesta.contenido.carga[0]);
            ref.obj.ocultaContenedorFormularioCargaTanque.hide();
          }
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);         
        }
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error);        
      }
      
      
      
    success: function(respuesta) {
    	var registro = respuesta.contenido.carga[0];
    	ref.obj.cmpProductoFormularioCargaTanque.val(registro.producto.nombre);
    },			    		    
    error: function(xhr,estado,error) {
    ref.mostrarErrorServidor(xhr,estado,error);        
      }
});
};  */

moduloDescarga.prototype.listarDiasOperativos= function(){
  var ref=this;
  try {
    ref.obj.tablaDiaOperativoAPI.ajax.reload();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.listarCargasTanque= function(){
  var ref=this;
  console.log("listarCargasTanque PRINCIPIO");
  try {
    ref.idEstacionSeleccionada = parseInt(ref.obj.filtroEstacionDetalleDiaOperativo.val());
    ref.obj.tablaCargaTanqueAPI.ajax.reload();
    console.log(ref.obj.tablaDescargaCisternaAPI);
    ref.idCargaTanque=-1;
    //ref.obj.tablaDescargaCisternaAPI.ajax.reload();
    ref.obj.tablaDescargaCisternaAPI.clear().draw();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
  console.log("listarCargasTanque FIN");
};

moduloDescarga.prototype.listarDescargasCisterna= function(){
  var ref=this;
  try {
    ref.obj.tablaDescargaCisternaAPI.ajax.reload();
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }  
};

moduloDescarga.prototype.iniciarAgregarCargaTanque=function(){
  var ref=this;
  var listaTanques=null;
  try {
    ref.limpiarFormularioCargaTanque();
    ref.nombreEstacionSeleccionada = $("#filtroEstacionDetalleDiaOperativo option:selected").text();
    ref.idEstacionSeleccionada = ref.obj.filtroEstacionDetalleDiaOperativo.val();
    console.log(cacheDescarga.tanques[ref.idEstacionSeleccionada]);
    listaTanques = cacheDescarga.tanques[ref.idEstacionSeleccionada];
    if ((typeof listaTanques !="undefined") && (listaTanques.length >0)){
      ref.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_DESCARGA_TANQUE);
      ref.obj.ocultaContenedorFormularioCargaTanque.show();
      ref.obj.cntTablaDiaOperativo.hide();
      ref.obj.cntDetalleDiaOperativo.hide();
      ref.obj.cntFormularioCargaTanque.show();
      ref.modoEdicionCargaTanque=constantes.MODO_NUEVO;
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
      ref.obj.cmpOperacionFormularioCargaTanque.val(ref.nombreOperacionSeleccionada);
      ref.obj.cmpEstacionFormularioCargaTanque.val(ref.nombreEstacionSeleccionada);
//TODO
      //TODO
      
      
      
      console.log("-----------------------------------------------------------");
      console.log(listaTanques);
      console.log("-----------------------------------------------------------");
      ref.pintarTanques(listaTanques);    
      ref.obj.ocultaContenedorFormularioCargaTanque.hide();
    } else {
      ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,cadenas.TEXTO_NO_TANQUES_ESTACION);
    }   
  } catch(error){
    ref.mostrarDepuracion(error.message);
  }
};

moduloDescarga.prototype.iniciarAgregarDescarga=function(){
  var ref=this;
  var listaTanques=null;
  if(ref.idTanqueSeleccionado>0){
	  try {
	    ref.limpiarFormularioDescarga();
	    ref.modoEdicionDescarga = constantes.MODO_NUEVO;
	    ref.obj.cmpBuscarCisternaFormularioDescarga.prop("disabled",false);
	    ref.nombreEstacionSeleccionada = $("#filtroEstacionDetalleDiaOperativo option:selected").text();
	    ref.metodoDescargaSeleccionado = $("#filtroEstacionDetalleDiaOperativo option:selected").attr("data-metodo");
	    ref.obj.tituloSeccion.text(cadenas.TITULO_AGREGAR_DESCARGA);
	    ref.obj.ocultaFormularioDescarga.show();    
	    ref.obj.cntTablaDiaOperativo.hide();
	    ref.obj.cntDetalleDiaOperativo.hide();
	    ref.obj.cntFormularioCargaTanque.hide();
	    $("#cntObservacionesDescarga").hide();
	    ref.obj.cntFormularioDescarga.show();
	    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_INFO,cadenas.INICIAR_OPERACION);
	    //KANB
	    $("#btnGuardarDescarga").addClass(constantes.CSS_CLASE_DESHABILITADA);
	    $("#cmpDescargaObservacion").val("");
	    $("#cmpOperacionFormularioDescarga").val(ref.nombreOperacionSeleccionada);
	    $("#cmpEstacionFormularioDescarga").val(ref.nombreEstacionSeleccionada);
	    $("#cmpTanqueFormularioDescarga").val(ref.nombreTanqueSeleccionado);  
	    $("#cmpMetodoFormularioDescarga").val(utilitario.formatearMetodoDescarga(ref.metodoDescargaSeleccionado));  
	    console.log("ref.idProductoSeleccionado ----------" + ref.idProductoSeleccionado);
	    console.log("ref.productoSeleccionado ----------" + ref.productoSeleccionado);
	    $("#cmpIdProductoTanqueFormularioDescarga").val(ref.idProductoSeleccionado);
	    $("#cmpProductoTanqueFormularioDescarga").val(ref.productoSeleccionado);
	        $.ajax({
	            type: constantes.PETICION_TIPO_GET,
	            url: ref.URL_FILTRAR_TRANSPORTES_ASIGNADOS, 
	            contentType: ref.TIPO_CONTENIDO, 
	            data: {filtroOperacion:ref.idOperacionSeleccionada,filtroEstado:constantes.ESTADO_ASIGNADO_TRANSPORTE}, 
	            success: function(respuesta) {
	              if (!respuesta.estado) {
	                ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
	              } else {
	                ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
	                registros = respuesta.contenido.carga;
	                ref.cisternasDisponibles={};
	                var numeroRegistros = registros.length;
	                for(var contador=0;contador<numeroRegistros;contador++){
	                  var cisterna = registros[contador];
	                  ref.cisternasDisponibles[cisterna.id] =cisterna;
	                }
	                ref.pintarCisternas(registros);
	              }
	              ref.obj.ocultaFormularioDescarga.hide();
	            },                  
	            error: function(xhr,estado,error) {
	              ref.mostrarErrorServidor(xhr,estado,error); 
	              ref.obj.ocultaFormularioDescarga.hide();        
	            }
	          });	    
	  } catch(error){
	    ref.mostrarDepuracion(error.message);
	    ref.obj.ocultaFormularioDescarga.hide();
	  }
  }else{
  	ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Debe seleccionar un Tanque");
  }
};

moduloDescarga.prototype.limpiarFormularioCargaTanque=function(){
  var ref=this;
  ref.obj.cmpFechaHoraInicial.val(utilitario.formatearTimestampToString(Date.now()));
  ref.obj.cmpAlturaInicial.val("");    
  ref.obj.cmpTemperaturaInicialCentro.val("");
  ref.obj.cmpTemperaturaInicialProbeta.val("");
  ref.obj.cmpAPIObservadoInicial.val("");
  ref.obj.cmpFactorInicial.val("");
  ref.obj.cmpVolumenInicialObservado.val("");
  ref.obj.cmpVolumenInicialCorregido.val("");
  ref.obj.cmpFechaHoraFinal.val(utilitario.formatearTimestampToString(Date.now()));
  ref.obj.cmpAlturaFinal.val("");    
  ref.obj.cmpTemperaturaFinalCentro.val("");
  ref.obj.cmpTemperaturaFinalProbeta.val("");
  ref.obj.cmpAPIObservadoFinal.val("");
  ref.obj.cmpFactorFinal.val("");
  ref.obj.cmpVolumenFinalObservado.val("");
  ref.obj.cmpVolumenFinalCorregido.val("");    
};

moduloDescarga.prototype.iniciarModificarCargaTanque=function(){
  var ref=this;
  try {
    ref.obj.tituloSeccion.text(cadenas.TITULO_MODIFICAR_DESCARGA_TANQUE);
    ref.obj.ocultaContenedorFormularioCargaTanque.show();
    ref.nombreEstacionSeleccionada = $("#filtroEstacionDetalleDiaOperativo option:selected").text();
    ref.obj.cntTablaDiaOperativo.hide();
    ref.obj.cntDetalleDiaOperativo.hide();
    ref.obj.cntFormularioCargaTanque.show();
    ref.modoEdicionCargaTanque=constantes.MODO_ACTUALIZAR;
    ref.obj.cmpOperacionFormularioCargaTanque.val(ref.nombreOperacionSeleccionada);
    ref.obj.cmpEstacionFormularioCargaTanque.val(ref.nombreEstacionSeleccionada);
    $.ajax({
      type: constantes.PETICION_TIPO_GET,
      url: ref.URL_RECUPERAR_CARGA, 
      contentType: ref.TIPO_CONTENIDO, 
      data: {ID:ref.idCargaTanque}, 
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        } else {
          if (ref.modoEdicionCargaTanque == constantes.MODO_ACTUALIZAR){
            ref.llenarFormularioCargaTanque(respuesta.contenido.carga[0]);
            ref.obj.ocultaContenedorFormularioCargaTanque.hide();
          }
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);         
        }
      },                  
      error: function(xhr,estado,error) {
        ref.mostrarErrorServidor(xhr,estado,error);        
      }
    });
  } catch(error){
    
  }
};

moduloDescarga.prototype.llenarFormularioCargaTanque=function(registro){
  var ref = this;
  console.log(registro);
    this.idCargaTanque= registro.id;
    this.idEstacionSeleccionada = registro.idEstacion;
    this.idDiaOperativoSeleccionado = registro.idDiaOperativo;
    listaTanques = cacheDescarga.tanques[ref.idEstacionSeleccionada];
    ref.pintarTanques(listaTanques);
    ref.obj.cmpTanqueFormularioCargaTanque.val(registro.idTanque);
    this.pintarProductoTanque(registro.idTanque);
    ref.obj.cmpFechaHoraInicial.val(utilitario.formatearTimestampToString(registro.fechaHoraInicial));
    ref.obj.cmpAlturaInicial.val(registro.alturaInicial);
    ref.obj.cmpTemperaturaInicialCentro.val(parseFloat(registro.temperaturaInicialCentro).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpTemperaturaInicialProbeta.val(parseFloat(registro.temperaturaIniciaProbeta).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpAPIObservadoInicial.val(parseFloat(registro.apiObservadoInicial).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpFactorInicial.val(parseFloat(registro.factorCorreccionInicial).toFixed(6));
    ref.obj.cmpVolumenInicialObservado.val(parseFloat(registro.volumenObservadoInicial).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpVolumenInicialCorregido.val(parseFloat(registro.volumenCorregidoInicial).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpFechaHoraFinal.val(utilitario.formatearTimestampToString(registro.fechaHoraFinal));
    ref.obj.cmpAlturaFinal.val(registro.alturaFinal);    
    ref.obj.cmpTemperaturaFinalCentro.val(parseFloat(registro.temperaturaFinalCentro).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpTemperaturaFinalProbeta.val(parseFloat(registro.temperaturaFinalProbeta).toFixed(ref.NUMERO_DECIMALES));    
    ref.obj.cmpAPIObservadoFinal.val(parseFloat(registro.apiObservadoFinal).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpFactorFinal.val(parseFloat(registro.factorCorreccionFinal).toFixed(6));
    ref.obj.cmpVolumenFinalObservado.val(parseFloat(registro.volumenObservadoFinal).toFixed(ref.NUMERO_DECIMALES));
    ref.obj.cmpVolumenFinalCorregido.val(parseFloat(registro.volumenCorregidoFinal).toFixed(ref.NUMERO_DECIMALES)); 
    ref.obj.cmpEstacionFormularioCargaTanque.val(registro.estacion.nombre);
    ref.obj.cmpOperacionFormularioCargaTanque.val(ref.obj.cmpOperacionDetalleDiaOperativo.val());
};

moduloDescarga.prototype.validarEvento= function(){
  var ref = this;
  var respuesta = true;
  if(ref.obj.cmpEventoFechaHora.val() == ""){
    ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La fecha y hora debe estar informada.");
  return false;
  }
  if (ref.obj.cmpEventoDescripcion.val() == ""){
    referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"La observaci&oacute;n debe ser informada.");
  }
  return respuesta;
};

moduloDescarga.prototype.guardarEvento = function(){
  var ref = this;
  var eRegistro =null;
  if (!ref.validaFormularioXSS("#frmEvento")){
	  ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR, cadenas.ERROR_VALORES_FORMULARIO);
	} else if (ref.validarEvento()){
    eRegistro = ref.recuperarValoresEvento();
    $.ajax({
      type: constantes.PETICION_TIPO_POST,
      url: ref.URL_GUARDAR_EVENTO,
      contentType: ref.TIPO_CONTENIDO, 
      data: JSON.stringify(eRegistro),  
      success: function(respuesta) {
        if (!respuesta.estado) {
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,respuesta.mensaje);
        }   else {          
          ref.actualizarBandaInformacion(constantes.TIPO_MENSAJE_EXITO,respuesta.mensaje);
          ref.obj.cntEventoDescarga.hide();
          ref.obj.cntDetalleDiaOperativo.show();          
        }
      },                  
      error: function() {
      referenciaModulo.actualizarBandaInformacion(constantes.TIPO_MENSAJE_ERROR,"Hubo un error en la peticion");
      }
    });
  } else {
    
  }
};
  
  moduloDescarga.prototype.recuperarValoresEvento = function(registro){
    var eRegistro = {};
    var ref=this;
    try {
    eRegistro.tipoRegistro = constantes.TIPO_REGISTRO_DESCARGA; //1 porque el tipo de registro es transporte
    eRegistro.tipoEvento = parseInt(ref.obj.cmpEventoTipoEvento.val());
    eRegistro.fechaHora  = utilitario.formatearStringToDateHour(ref.obj.cmpEventoFechaHora.val());
    eRegistro.descripcion = ref.obj.cmpEventoDescripcion.val().toUpperCase();
    eRegistro.idRegistro = parseInt(ref.idDescargaCisterna);
    } catch(error){
    console.log(error.message);
    }
    return eRegistro;
  };

  
moduloDescarga.prototype.inicializarCampos= function(){
//Implementar en cada caso
};

moduloDescarga.prototype.llenarFormulario = function(registro){
//Implementar en cada caso  
};

moduloDescarga.prototype.llenarDetalles = function(registro){
//Implementar en cada caso
};

moduloDescarga.prototype.recuperarValores = function(registro){
var eRegistro = {};
//Implementar en cada caso
return eRegistro;
};

moduloDescarga.prototype.grillaDespuesSeleccionar= function(indice){
//TODO implementar
};

moduloDescarga.prototype.despuesListarRegistros=function(){
//Implementar en cada caso
};

moduloDescarga.prototype.inicializar=function(){
	this.configurarAjax();
  this.inicializarControles();
  this.inicializarGrillas();
  //this.inicializarFormularioPrincipal();
  this.inicializarCampos();
  this.listarDiasOperativos();
};

moduloDescarga.prototype.configurarAjax=function(){
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

moduloDescarga.prototype.resetearFormulario= function(){
  var ref= this;
  ref.obj.frmPrincipal[0].reset();
  jQuery.each( this.obj, function( i, val ) {
    if ( (typeof ref.obj[i].tipoControl ) === "undefined" ){
      //TODO
    } else {
      if (ref.obj[i].tipoControl === "select2"){
        ref.obj[i].select2("val", null);
      }
    }
  });
};

moduloDescarga.prototype.validaFormularioXSS= function(formulario){
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

moduloDescarga.prototype.validaPermisos= function(){
  var referenciaModulo = this;
  try{
  console.log("Validando permiso para: " + referenciaModulo.descripcionPermiso);
  referenciaModulo.obj.ocultaContenedorTablaDiaOperativo.show();
  referenciaModulo.obj.ocultaContenedorTablaCargaTanque.show();
  referenciaModulo.obj.ocultaContenedorTablaDescarga.show();
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
	    	  referenciaModulo.obj.ocultaContenedorTablaDiaOperativo.hide();
	    	  referenciaModulo.obj.ocultaContenedorTablaCargaTanque.hide();
	    	  referenciaModulo.obj.ocultaContenedorTablaDescarga.hide();
	      } else {
		      if (referenciaModulo.descripcionPermiso == 'LEER_DESCARGA'){
		    	  referenciaModulo.mostrarDetalleDiaOperativo();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_CARGA'){
		    	  referenciaModulo.iniciarAgregarCargaTanque();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_CARGA'){
		    	  referenciaModulo.iniciarModificarCargaTanque();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_DESCARGA'){
		    	  referenciaModulo.iniciarAgregarDescarga();
		      } else if (referenciaModulo.descripcionPermiso == 'ACTUALIZAR_DESCARGA'){
		    	  referenciaModulo.iniciarModificarDescarga();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_TRANSPORTE'){
		    	  referenciaModulo.recuperarDetalleTransporte();
		      } else if (referenciaModulo.descripcionPermiso == 'CREAR_EVENTO_DESCARGA'){
		    	  referenciaModulo.iniciarAgregarEvento();
		      } else if (referenciaModulo.descripcionPermiso == 'RECUPERAR_DESCARGA'){
		    	  referenciaModulo.iniciarVerDescarga();
		      }
	      }
	      referenciaModulo.obj.ocultaContenedorTablaDiaOperativo.hide();
    	  referenciaModulo.obj.ocultaContenedorTablaCargaTanque.hide();
    	  referenciaModulo.obj.ocultaContenedorTablaDescarga.hide();
	    },
	    error: function() {
	    	referenciaModulo.obj.ocultaContenedorTablaDiaOperativo.hide();
	    	referenciaModulo.obj.ocultaContenedorTablaCargaTanque.hide();
	    	referenciaModulo.obj.ocultaContenedorTablaDescarga.hide();
	    	referenciaModulo.mostrarErrorServidor(xhr,estado,error); 
	    }
	  });
  } catch(error){
	  referenciaModulo.obj.ocultaContenedorTablaDiaOperativo.hide();
  	  referenciaModulo.obj.ocultaContenedorTablaCargaTanque.hide();
  	  referenciaModulo.obj.ocultaContenedorTablaDescarga.hide();
	  referenciaModulo.mostrarDepuracion(error.message);
  };
};
